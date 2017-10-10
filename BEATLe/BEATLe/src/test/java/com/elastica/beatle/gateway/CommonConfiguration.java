package com.elastica.beatle.gateway;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.elastica.beatle.InitializeTests;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.RestClient.GatewayClient;
import com.elastica.beatle.RestClient.GatewayClientWrapper;
import com.elastica.beatle.es.ESQueryBuilder;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;
import com.elastica.beatle.gateway.dto.GWHit;
import com.elastica.beatle.gateway.dto.GWSource;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.replayTool.EPDV3LogParser;
import com.elastica.beatle.replayTool.LogParser;
import com.elastica.beatle.replayTool.LogReplayConstant;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;
import com.elastica.beatle.tests.infra.InfraUtils;


public class CommonConfiguration extends InitializeTests {
	
	protected Client restClient;
	protected HttpResponse CSRFHeader ;
	protected String csfrToken;
	protected String sessionID;
	public String parentNode="$.hits.hits[*].source";
	public int size1=400;
	public String size="400";
	String currentTimeInJodaFormat;
	public String fromTime;
	public TestSuiteDTO suiteData;
	public static String envtName="";
	public static TenantToken tenantToken;	
	public static String token="";
	public static String testUserName;
	LogValidator logValidator;
	StringEntity httpEntity;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	public static List<NameValuePair> requestHeader;
	JsonCommonUtils   jsonCommonUtils = new JsonCommonUtils();
	ESQueryBuilder eSQueryBuilder = new ESQueryBuilder();
	ElasticSearchLogs  es= new ElasticSearchLogs ();
	public Map <String, String> data = new HashMap<String, String>();
	final int  globalWait=40000;
	final int localWait=10000;
	
	public enum TenantToken {
			ELASTICAQANET, 
			ELASTICACO, 
			ELASTICAQAINC, 
			ELASTICAQACOM,
			GATEWAYBEATLECOM,
			FAKEGATEWAYBEATLECOM, 
			GATEWAYO365BEATLECOM, 
			SALESFORCEGATEWAYBEATLECOM, 
			BOXGATEWAYBEATLECOM, 
			DEBUGGATEWAYBEATLECOM,
			GWEXTERNALO365BEATLECOM
		}
	
	@BeforeTest(alwaysRun=true)
	public void initializeTests(ITestContext suiteConfigurations) throws Exception {
		System.setProperty("jsse.enableSNIExtension", "false");
		suiteData = new TestSuiteDTO();
		Reporter.log("Total tests: "+suiteConfigurations.getAllTestMethods().length,true);
		super.initSuiteConfigurations(suiteConfigurations, suiteData);
		Reporter.log("Saas App: "+suiteData.getSaasAppUsername(), true);
		testUserName= suiteData.getUsername().replace("@"+suiteData.getTenantDomainName(), "");
		Reporter.log("saas App Name: "+suiteData.getSaasApp(), true);
		restClient = new Client();
		CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		csfrToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
		sessionID = AuthorizationHandler.getUserSessionID(CSRFHeader);
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(ProtectTestConstants.PROTECT_API_CONFIGURATION_FILEPATH));
		requestHeader = buildCookieHeaders();
		
	}
	
	@BeforeMethod(alwaysRun=true)
	public void beforeTestMethod(Method method) throws Exception {
		fromTime=getCurrentTime();
		data.clear();
		Reporter.log("FromTime: "+fromTime);
		Reporter.log("Test Name: "+method.getName(), true);
		Reporter.log("------------------ :API Call: --------------------------", true);
		//Reporter.log(getDisplayLogApi() , true);
	}
	
	
	public static String getResponseBody(HttpResponse response) {
		try {
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	protected String getBasicAuthHeader(String username, String password) {
		String authString = username + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		return authStringEnc;
	}
	
	
	public int getResponseStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();

	}
	
	public static <T> T unmarshallJSON(InputStream is, Class<T> klass)
			throws Exception {
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
			objectMapper.setAnnotationIntrospector(introspector);
			objectMapper.setAnnotationIntrospector(introspector);
			return objectMapper.readValue(is, klass);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	public HttpResponse getCSRFHeader() {
		return CSRFHeader;
	}

	public void setCSRFHeader(HttpResponse cSRFHeader) {
		CSRFHeader = cSRFHeader;
	}

	public String getCsfrToken() {
		return csfrToken;
	}

	public void setCsfrToken(String csfrToken) {
		this.csfrToken = csfrToken;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * 
	 * @param <T>
	 * @param data
	 * @param klass
	 * @return
	 * @throws JAXBException 
	 */
	public <T> T unmarshall(String data, final Class<T> klass) throws JAXBException {		
		return unmarshallJSON(data, klass);		
	}
	
	protected static <T> T unmarshallJSON(final String json, final Class<T> klass) {
		final ObjectMapper mapper = new ObjectMapper();
		final AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		// make deserializer use JAXB annotations (only)
		mapper.setAnnotationIntrospector(introspector);
		// make serializer use JAXB annotations (only)		
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			return mapper.readValue(json, klass);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	public String getCurrentTime() {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//.withZone(DateTimeZone.forID("America/Los_Angeles"));
		String tsfrom = currentTime.toString(df);
		return tsfrom;
	}
	
    public String getTenantToken(){
		 String tenantName=suiteData.getTenantName();
	    	System.out.println("Tenant Name "+tenantName);
	    	tenantToken= TenantToken.valueOf(tenantName.toUpperCase());
			String tenantTokenFinal="";
			switch(tenantToken){
			case ELASTICAQANET:
				tenantTokenFinal=GatewayTestConstants.ELASTICAQANET;
				break;
			case ELASTICACO:
				tenantTokenFinal=GatewayTestConstants.ELASTICACO;
				break;
			case ELASTICAQAINC:
				tenantTokenFinal=GatewayTestConstants.ELASTICAQAINC;
				break;
			case ELASTICAQACOM:
				tenantTokenFinal=GatewayTestConstants.ELASTICAQACOM;
				break;
			case GATEWAYBEATLECOM:
				tenantTokenFinal=GatewayTestConstants.GATEWAYBEATLECOM;
				break;
			case SALESFORCEGATEWAYBEATLECOM:
				tenantTokenFinal=GatewayTestConstants.SALESFORCEGATEWAYBEATLECOM;
				break;
			case BOXGATEWAYBEATLECOM:
				tenantTokenFinal=GatewayTestConstants.BOXGATEWAYBEATLECOM;
				break;
			case DEBUGGATEWAYBEATLECOM:
				tenantTokenFinal=GatewayTestConstants.DEBUGGATEWAYBEATLECOM;
				break;
			case GATEWAYO365BEATLECOM:
				tenantTokenFinal=GatewayTestConstants.GATEWAYO365BEATLECOM;
				break;
			case FAKEGATEWAYBEATLECOM:
				tenantTokenFinal=GatewayTestConstants.FAKEGATEWAYBEATLECOM;
				break;
			case GWEXTERNALO365BEATLECOM:
				tenantTokenFinal=GatewayTestConstants.GWEXTERNALO365BEATLECOM;
				break;
			}
			return tenantTokenFinal;
			
	    }
    

	 public List<NameValuePair> getHeaders() {
			String authStringEnc = getBasicAuthHeader(suiteData.getUsername(), suiteData.getPassword());
			List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
			headers.add(new BasicNameValuePair("Accept-Language", "en-US,en;q=0.8"));
			headers.add(new BasicNameValuePair("CSP", "active"));
			headers.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36"));
			headers.add(new BasicNameValuePair("ontent-Type", "application/json;charset=UTF-8"));
			headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
			headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
			headers.add(new BasicNameValuePair("X-CSRFToken", csfrToken));
			headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, "csrftoken="+csfrToken+";sessionid="+sessionID+";"));
			headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, "Basic "+authStringEnc));
			headers.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
			headers.add(new BasicNameValuePair("X-TenantToken",getTenantToken() ));
			return headers;
		}
	
		   
		 
		 // @BeforeTest(alwaysRun=true)
		  public void initTests(ITestContext suiteConfigurations) throws Exception {		
				suiteData = new TestSuiteDTO();
				super.initSuiteConfigurations(suiteConfigurations, suiteData);		
				suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(ProtectTestConstants.PROTECT_API_CONFIGURATION_FILEPATH));
				requestHeader = buildCookieHeaders();
				//System.out.println("Headers "+requestHeader);
				//System.out.println("Suite Data "+suiteData);
			}
		  
		  
		  
		  protected List<NameValuePair> buildCookieHeaders() throws Exception{
				List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
				//HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
				String csrfToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
				String sessionID = AuthorizationHandler.getUserSessionID(CSRFHeader);
				requestHeader.add(new BasicNameValuePair("X-CSRFToken",csrfToken));
				requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + sessionID + "; csrftoken=" + csrfToken + ";"));
				requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
				requestHeader.add(new BasicNameValuePair("Referer", "https://"+suiteData.getHost()));
				requestHeader.add(new BasicNameValuePair("X-Session", sessionID));
				requestHeader.add(new BasicNameValuePair("X-TenantToken", "8Mcfo3OUrXx33ByfPIlCCA=="));
				requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
				return requestHeader;
			}
		  
		  
		
		  public String  fetchElasticSearchLogsUniversal(String activityType, String objectType, String severity ) throws JAXBException, IOException, InterruptedException{
			  String responseBody="";
			  boolean hitCount=false;
				for (int counter =0; counter<=(globalWait/localWait);counter++){
				 responseBody=fetchElasticSearchLogsUniversalRetry( activityType, objectType,  severity);
					fsr = unmarshall(responseBody, GWForensicSearchResults.class);
					hitCount=fsr.getHits().getHits().isEmpty();
					if (hitCount){
						Reporter.log("-----------------------------------------------------", true);
						Reporter.log("Waiting for the local wait time: "+(localWait*counter)+"/"+globalWait, true);
						Reporter.log("-----------------------------------------------------", true);
						Thread.sleep(localWait);
						continue;
					}
					else {
						break;
					}
					}
				if (!hitCount){
					logValidator = new LogValidator(fsr);
					logValidator.validateAll();
				}
					
					return responseBody;
			}
		  
		  public String  fetchElasticSearchLogsUniversalRetry(String activityType, String objectType,  String severity) throws JAXBException, IOException, InterruptedException{
			  HashMap<String, String> terms =new HashMap<String, String> ();
			  terms.put("Activity_type",activityType);
			  terms.put("severity",severity);
			  terms.put("Object_type",objectType);
			  HttpResponse response = null;
			  String payLoad=payloadCommon(terms);
			  StringEntity httpEntity = new StringEntity(payLoad);
				try {
					 response=	es.getDisplayLogs (restClient, getHeaders() ,suiteData.getApiserverHostName() , httpEntity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
				String responseBody = getResponseBody(response);
				Reporter.log("Response body:-"+ responseBody, true);
				return responseBody;
			}
		
		  
		  public String  fetchElasticSearchLogsUniversal(String activityType, String severity) throws JAXBException, IOException, InterruptedException{
			  String responseBody="";
			  boolean hitCount=false;
				for (int counter =0; counter<=(globalWait/localWait);counter++){
				 responseBody=fetchElasticSearchLogsUniversalRetry( activityType,  severity);
					fsr = unmarshall(responseBody, GWForensicSearchResults.class);
					System.out.println("Hits Count: "+fsr.getHits().getHits().size());
					hitCount=fsr.getHits().getHits().isEmpty();
					if (hitCount){
						Reporter.log("-----------------------------------------------------", true);
						Reporter.log("Waiting for the local wait time: "+(localWait*counter)+"/"+globalWait, true);
						Reporter.log("-----------------------------------------------------", true);
						Thread.sleep(localWait);
						continue;
					}
					else {
						break;
					}
					}
				if (!hitCount){
					logValidator = new LogValidator(fsr);
					logValidator.validateAll();
				}
					
					return responseBody;
			}
		  
		  public String  fetchElasticSearchLogsUniversalRetry(String activityType,  String severity) throws JAXBException, IOException, InterruptedException{
			  HashMap<String, String> terms =new HashMap<String, String> ();
			  terms.put("Activity_type",activityType);
			  terms.put("severity",severity);
			  HttpResponse response = null;
				String payLoad=payloadCommon(terms);
				ElasticSearchLogs  es= new ElasticSearchLogs ();
				StringEntity httpEntity = new StringEntity(payLoad);
				try {
					 response=	es.getDisplayLogs (restClient, getHeaders() ,suiteData.getApiserverHostName() , httpEntity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
				String responseBody = getResponseBody(response);
				Reporter.log("Response body:-"+ responseBody, true);
				return responseBody;
			}
		  
		  public String  fetchElasticSearchLogsUniversal(String activityType) throws JAXBException, IOException, InterruptedException{
			  String responseBody="";
			  boolean hitCount=false;
				for (int counter =0; counter<=(globalWait/localWait);counter++){
				 responseBody=fetchElasticSearchLogsUniversalRetry( activityType);
					fsr = unmarshall(responseBody, GWForensicSearchResults.class);
					hitCount=fsr.getHits().getHits().isEmpty();
					if (hitCount){
						Reporter.log("-----------------------------------------------------", true);
						Reporter.log("Waiting for the local wait time: "+(localWait*counter)+"/"+globalWait, true);
						Reporter.log("-----------------------------------------------------", true);
						Thread.sleep(localWait);
						continue;
					}
					else {
						break;
					}
					}
				if (!hitCount){
					logValidator = new LogValidator(fsr);
					logValidator.validateAll();
				}
					
					return responseBody;
			}
		  
		  public String  fetchElasticSearchLogsUniversalRetry(String activityType) throws JAXBException, IOException, InterruptedException{
			  HashMap<String, String> terms =new HashMap<String, String> ();
			  terms.put("Activity_type",activityType);
			  HttpResponse response = null;
				String payLoad=payloadCommon(terms);
				ElasticSearchLogs  es= new ElasticSearchLogs ();
				StringEntity httpEntity = new StringEntity(payLoad);
				try {
					 response=	es.getDisplayLogs (restClient, getHeaders() ,suiteData.getApiserverHostName() , httpEntity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
				String responseBody = getResponseBody(response);
				Reporter.log("Response body:-"+ responseBody, true);
				return responseBody;
			}
		  
		  public String payloadCommon(HashMap<String, String> terms) {
			  
			  String payLoad = null;
			  terms.put("__source", "GW");
			  String activityType = terms.get("Activity_type"); 
			  if (activityType != null && (!(activityType.equals("Policy Violation") ||
					  activityType.equals("Download")))) {
				  terms.put("user", suiteData.getSaasAppUsername());
			  }
			  
			  try {
				  payLoad = eSQueryBuilder.getESQuery(fromTime, suiteData.getSaasApp(), terms, suiteData.getSaasAppUsername(),suiteData.getApiserverHostName(), csfrToken, sessionID, size1,suiteData.getUsername());
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
			  
			  return payLoad;
		  }

		  public String  fetchElasticSearchLogsUniversal() throws JAXBException, IOException, InterruptedException{
			  String responseBody="";
			  boolean hitCount=false;
				for (int counter =0; counter<=(globalWait/localWait);counter++){
				 responseBody=fetchElasticSearchLogsUniversalRetry();
					fsr = unmarshall(responseBody, GWForensicSearchResults.class);
					hitCount=fsr.getHits().getHits().isEmpty();
					if (hitCount){
						Reporter.log("-----------------------------------------------------", true);
						Reporter.log("Waiting for the local wait time: "+(localWait*counter)+"/"+globalWait, true);
						Reporter.log("-----------------------------------------------------", true);
						Thread.sleep(localWait);
						continue;
					}
					else {
						break;
					}
					}
				if (!hitCount){
					logValidator = new LogValidator(fsr);
					logValidator.validateAll();
				}
					
					return responseBody;
			}
		  
		  
		  public String  fetchElasticSearchLogsUniversalRetry() throws JAXBException, IOException, InterruptedException{
			  HashMap<String, String> terms =new HashMap<String, String> ();
			  HttpResponse response = null;
				String payLoad=payloadCommon(terms);
				Reporter.log("Request Payload:-"+ payLoad, true);
				ElasticSearchLogs  es= new ElasticSearchLogs ();
				StringEntity httpEntity = new StringEntity(payLoad);
				try {
					 response=	es.getDisplayLogs (restClient, getHeaders() ,suiteData.getApiserverHostName() , httpEntity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String responseBody = getResponseBody(response);
				Reporter.log("Response body:-"+ responseBody, true);
				assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
				
				return responseBody;
			}
		  
		  
		  public String  fetchElasticSearchLogsAllUsersUniversal() throws JAXBException, IOException, InterruptedException{
			  String responseBody="";
			  boolean hitCount=false;
				for (int counter =0; counter<=(globalWait/localWait);counter++){
				 responseBody=fetchElasticSearchLogsUniversalAllUsersRetry();
					fsr = unmarshall(responseBody, GWForensicSearchResults.class);
					hitCount=fsr.getHits().getHits().isEmpty();
					if (hitCount){
						Reporter.log("-----------------------------------------------------", true);
						Reporter.log("Waiting for the local wait time: "+(localWait*counter)+"/"+globalWait, true);
						Reporter.log("-----------------------------------------------------", true);
						Thread.sleep(localWait);
						continue;
					}
					else {
						break;
					}
					}
				if (!hitCount){
					logValidator = new LogValidator(fsr);
					logValidator.validateAll();
				}
					
					return responseBody;
			}
		  
		  
		  public String  fetchElasticSearchLogsUniversalAllUsersRetry() throws JAXBException, IOException, InterruptedException{
			  HashMap<String, String> terms =new HashMap<String, String> ();
			  HttpResponse response = null;
				String payLoad=payloadCommonAllUsers();
				ElasticSearchLogs  es= new ElasticSearchLogs ();
				StringEntity httpEntity = new StringEntity(payLoad);
				try {
					 response=	es.getDisplayLogs (restClient, getHeaders() ,suiteData.getApiserverHostName() , httpEntity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
				String responseBody = getResponseBody(response);
				Reporter.log("Response body:-"+ responseBody, true);
				return responseBody;
			}
		  
		  
		  public String payloadCommonAllUsers(){
			  HashMap<String, String> terms = new  HashMap<String, String>();
			  String payLoad = null;
			 // terms.put("user", suiteData.getSaasAppUsername());
			  terms.put("__source", "GW");
			try {
				payLoad = eSQueryBuilder.getESQuery(fromTime, suiteData.getSaasApp(), terms, suiteData.getSaasAppUsername(),suiteData.getApiserverHostName(), csfrToken, sessionID, size1,suiteData.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
			}
			  return payLoad;
		  
		  }
		  
		  
			
			public void replayLogs(String fileName) throws UnsupportedEncodingException, Exception {
				if (fileName!=null){
				Thread.sleep(5000);
				//HttpHost gateway = new HttpHost("10.0.54.16", 443, "http");
				
				String gwHostname = (suiteData.getGwHostname()!=null ? suiteData.getGwHostname() : "10.0.50.68");
				HttpHost gateway = new HttpHost(gwHostname, 443, "http");
				Logger.info("GW Hostname : " + gwHostname);
				//HttpHost gateway = new HttpHost("10.0.52.30", 443, "http");
				
				//HttpHost gateway = new HttpHost("10.0.54.115", 443, "http");
				GatewayClient client = new GatewayClient(gateway);
				LogParser parser = new LogParser();
				String replayFileName = suiteData.getSaasApp()+"/"+fileName;
				Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
				Logger.info("Number of Requests to be Played : " + requestList.size());
				Logger.info("\n Started Replaying Logs\n");
				Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
				System.out.println("User Name: "+suiteData.getSaasAppUsername());
				while (iterator.hasNext()) {  
			        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
			        if (requestPair.getKey().getRequestURI() == null) {
			        	continue;
			        }
			        if (requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("img.en25.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("mozilla.net") ||
			        		requestPair.getKey().getRequestURI().toString().contains("gstatic.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("eloqua.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("office_online")||
			        		requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("versioncheck-bg.addons.mozilla.org")||
			        		requestPair.getKey().getRequestURI().toString().contains("addons.seleniumhq.org)")||
			        		//requestPair.getKey().getRequestURI().toString().contains("office.net")||
			        		
			        		//(requestPair.getKey().getRequestURI().toString().contains("static.sharepointonline.com"))||
			        		//(requestPair.getKey().getRequestURI().toString().contains("gwo365beatle-my.sharepoint.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("download.cdn.mozilla.net"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("ns=PendingRequest&ev=PendingNotificationRequest&"))||
			        		(requestPair.getKey().getRequestURI().toString().contains(".css"))||
			        		(requestPair.getKey().getRequestURI().toString().contains(".js"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("secure.aadcdn.microsoftonline-p.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("prod.msocdn.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("aus4.mozilla.org"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("download.mozilla.org"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("s1-word-edit-15"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("notes.services.box.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("https://elasticaqa1.app.box.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("https://app.box.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("browser.pipe.aria.microsoft.com"))||
			        		
			        		(requestPair.getKey().getRequestURI().toString().contains("opswat.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("screenhero.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("cloudfront.net"))||
			        		
			        		(requestPair.getKey().getRequestURI().toString().contains("avatars.slack-edge.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("secure.gravatar.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("wp.com"))||
			        		
			        		(requestPair.getKey().getRequestURI().toString().contains("autodiscover"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("jamfcloud.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("apple.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("https://fonts.googleapis.com"))||
			        		
			        		requestPair.getKey().getRequestURI().toString().contains("clientlog.portal.office.com")) {
			        	System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
			        	continue;
			        }  
			        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), suiteData.getSaasAppUsername());
			        //Thread.sleep(5000);
			        Logger.info("Response Received from gateway");
			       // validateResponseStatus(requestPair.getValue(), response);
			    }
			   // Thread.sleep(55000);
				}
				
			}
			
			// This is for Version3
			public void replayLogsEPDV3(String fileName) throws UnsupportedEncodingException, Exception {
				if (fileName!=null){
					Thread.sleep(5000);
					HttpHost gateway = new HttpHost("10.0.52.30", 443, "http");

					GatewayClient client = new GatewayClient(gateway);
					EPDV3LogParser parser = new EPDV3LogParser();
					String replayFileName = suiteData.getSaasApp()+"/"+fileName;//"AFolder,CreateFolder.log";
					System.out.println("repla "+replayFileName);
					Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
					Logger.info("Number of Requests to be Played : " + requestList.size());
					Logger.info("\n Started Replaying Logs\n");
					Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
					System.out.println("User Name: "+suiteData.getSaasAppUsername());
					while (iterator.hasNext()) {  
						Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
						if (requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com") || 
								requestPair.getKey().getRequestURI().toString().contains("google-analytics.com") ||
								requestPair.getKey().getRequestURI().toString().contains("img.en25.com") || 
								requestPair.getKey().getRequestURI().toString().contains("gstatic.com") ||
								requestPair.getKey().getRequestURI().toString().contains("eloqua.com")||
								requestPair.getKey().getRequestURI().toString().contains("office_online")||
								requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com")||
								requestPair.getKey().getRequestURI().toString().contains("addons.seleniumhq.org)")||
								//requestPair.getKey().getRequestURI().toString().contains("office.net")||

								(requestPair.getKey().getRequestURI().toString().contains("mozilla.net")) ||
								(requestPair.getKey().getRequestURI().toString().contains("shavar.services.mozilla.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("aus4.mozilla.org"))||
								(requestPair.getKey().getRequestURI().toString().contains("download.mozilla.org"))||
								(requestPair.getKey().getRequestURI().toString().contains("versioncheck-bg.addons.mozilla.org"))||
								
								(requestPair.getKey().getRequestURI().toString().contains("spoprod-a.akamaihd.net"))||
								
								//(requestPair.getKey().getRequestURI().toString().contains("static.sharepointonline.com"))||
								//(requestPair.getKey().getRequestURI().toString().contains("gwo365beatle-my.sharepoint.com"))||
								
								(requestPair.getKey().getRequestURI().toString().contains("ns=PendingRequest&ev=PendingNotificationRequest&"))||
								(requestPair.getKey().getRequestURI().toString().contains(".css"))||
								(requestPair.getKey().getRequestURI().toString().contains(".js"))||
								(requestPair.getKey().getRequestURI().toString().contains(".gif"))||
								
								(requestPair.getKey().getRequestURI().toString().contains("secure.aadcdn.microsoftonline-p.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("prod.msocdn.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("s1-word-edit-15"))||
								(requestPair.getKey().getRequestURI().toString().contains("eoe.elastica-inc.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("https://www.dropbox.com/subscribe?host_int"))||
								(requestPair.getKey().getRequestURI().toString().contains("safebrowsing-cache.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("safebrowsing.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("www.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("clients2.google.com/availability"))||
								(requestPair.getKey().getRequestURI().toString().contains("apis.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("hangouts.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("plus.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("admin.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("gg.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("mail.google.com/favicon.ico"))||
								
								(requestPair.getKey().getRequestURI().toString().contains("setgmail"))||
								(requestPair.getKey().getRequestURI().toString().contains("accounts.youtube.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("play.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("fonts.googleapis.com"))||

								//these things are not blocking but i think its not required..
								(requestPair.getKey().getRequestURI().toString().contains("clients6.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("clients5.google.com"))||
								(requestPair.getKey().getRequestURI().toString().contains("clients4.google.com"))||

								(requestPair.getKey().getRequestURI().toString().contains("browser.pipe.aria.microsoft.com"))||
								
								requestPair.getKey().getRequestURI().toString().contains("clientlog.portal.office.com")) {

							System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
							continue;
						}  
						HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), suiteData.getSaasAppUsername());
						Logger.info("Response Received from gateway");
//						Assert.assertTrue(validateResponseStatus(requestPair.getValue(), response), "Response Status Validation Failed!!");
					}
				}

			}

			private boolean validateResponseStatus(LogReplayReponseDTO value, HttpResponse response) throws ParseException, IOException {
				Logger.info("Validating the Response Status");
				Logger.info("Actual Response Status : " + response.getStatusLine().toString());
				Logger.info("Expected Response Status : " + value.getResponseStatus().toString());

				if(value.getResponseStatus().trim().equals(response.getStatusLine().toString()) || 
						value.getResponseStatus().contains("302") ||
						value.getResponseStatus().contains("301") ||
						value.getResponseStatus().contains("242")) {
					Logger.info("Response Status matched!");
					return true;
				}

				Logger.info("Response Status NOT matched!");
				return false;
			}
			
			public void replayLogsWithDelay(String fileName) throws UnsupportedEncodingException, Exception {
				if (fileName!=null){
				Thread.sleep(5000);
				//HttpHost gateway = new HttpHost("10.0.54.16", 443, "http");
					
				HttpHost gateway = new HttpHost("10.0.50.68", 443, "http");
				//HttpHost gateway = new HttpHost("10.0.54.115", 443, "http");
				GatewayClient client = new GatewayClient(gateway);
				LogParser parser = new LogParser();
				String replayFileName = suiteData.getSaasApp()+"/"+fileName;//"AFolder,CreateFolder.log";
				//String replayFileName = "logs/"+fileName;//"AFolder,CreateFolder.log";
				System.out.println("repla "+replayFileName);
				Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
				Logger.info("Number of Requests to be Played : " + requestList.size());
				Logger.info("\n Started Replaying Logs\n");
				Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
				System.out.println("User Name: "+suiteData.getSaasAppUsername());
				while (iterator.hasNext()) {  
			        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
			        if (requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("img.en25.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("mozilla.net") ||
			        		requestPair.getKey().getRequestURI().toString().contains("gstatic.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("eloqua.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("office_online")||
			        		requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("versioncheck-bg.addons.mozilla.org")||
			        		requestPair.getKey().getRequestURI().toString().contains("addons.seleniumhq.org)")||
			        		(requestPair.getKey().getRequestURI().toString().contains("download.cdn.mozilla.net"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("ns=PendingRequest&ev=PendingNotificationRequest&"))||
			        		(requestPair.getKey().getRequestURI().toString().contains(".css"))||
			        		(requestPair.getKey().getRequestURI().toString().contains(".js"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("secure.aadcdn.microsoftonline-p.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("prod.msocdn.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("aus4.mozilla.org"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("download.mozilla.org"))||
			        		requestPair.getKey().getRequestURI().toString().contains("clientlog.portal.office.com")) {
			        	
			        	
			          	
			        	System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
			        	continue;
			        }  
			        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), suiteData.getSaasAppUsername());
			        //Thread.sleep(5000);
			        Logger.info("Response Received from gateway");
			       // validateResponseStatus(requestPair.getValue(), response);
			    }
			    Thread.sleep(55000);
				}
				
			}
			
			
			public void replayLogsDebug(String fileName) throws UnsupportedEncodingException, Exception {
				if (fileName!=null){
				Thread.sleep(5000);
				//HttpHost gateway = new HttpHost("10.0.54.16", 443, "http");
					
				//HttpHost gateway = new HttpHost("10.0.50.68", 443, "http");
				HttpHost gateway = new HttpHost("10.0.52.30", 443, "http");
				
				//HttpHost gateway = new HttpHost("10.0.54.115", 443, "http");
				GatewayClient client = new GatewayClient(gateway);
				LogParser parser = new LogParser();
				//String replayFileName = "nwlogs/"+fileName;//"AFolder,CreateFolder.log";
				String replayFileName = suiteData.getSaasApp()+"/"+fileName;//"AFolder,CreateFolder.log";
				//String replayFileName = "logs/"+fileName;//"AFolder,CreateFolder.log";
				System.out.println("repla "+replayFileName);
				Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
				Logger.info("Number of Requests to be Played : " + requestList.size());
				Logger.info("\n Started Replaying Logs\n");
				Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
				System.out.println("User Name: "+suiteData.getSaasAppUsername());
				while (iterator.hasNext()) {  
			        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
			        if (requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("img.en25.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("mozilla.net") ||
			        		requestPair.getKey().getRequestURI().toString().contains("gstatic.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("eloqua.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("office_online")||
			        		requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("versioncheck-bg.addons.mozilla.org")||
			        		requestPair.getKey().getRequestURI().toString().contains("addons.seleniumhq.org)")||
			        		//requestPair.getKey().getRequestURI().toString().contains("office.net")||
			        		
			        		//(requestPair.getKey().getRequestURI().toString().contains("static.sharepointonline.com"))||
			        		//(requestPair.getKey().getRequestURI().toString().contains("gwo365beatle-my.sharepoint.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("download.cdn.mozilla.net"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("ns=PendingRequest&ev=PendingNotificationRequest&"))||
			        		(requestPair.getKey().getRequestURI().toString().contains(".css"))||
			        		(requestPair.getKey().getRequestURI().toString().contains(".js"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("secure.aadcdn.microsoftonline-p.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("prod.msocdn.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("aus4.mozilla.org"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("download.mozilla.org"))||
			        		//(requestPair.getKey().getRequestURI().toString().contains("|"))||
			        		
			        		requestPair.getKey().getRequestURI().toString().contains("clientlog.portal.office.com")) {
			        	
			        	
			        	
			        	
			        	System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
			        	continue;
			        }  
			        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), suiteData.getSaasAppUsername());
			        //Thread.sleep(5000);
			        Logger.info("Response Received from gateway");
			       // validateResponseStatus(requestPair.getValue(), response);
			    }
			   // Thread.sleep(55000);
				}
				
			}
			
			
			
			
			public void replayLogsSalesforceCIQ(String logFileName, String fileName) throws UnsupportedEncodingException, Exception {
				
				HttpHost gateway = new HttpHost("10.0.52.30", 443, "http");
				GatewayClient client = new GatewayClient(gateway);
				
				LogParser parser = new LogParser();
				String replayFileName = suiteData.getSaasApp()+"/"+logFileName;
				//String replayFileName = "Salesforce/Salesforce,Files,upload_Test_txt.log";
				Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
				
				Logger.info("Number of Requests to be Played : " + requestList.size());
				
				Logger.info("\n Started Replaying Logs\n");
				
				Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
			    while (iterator.hasNext()) {
			        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
			        NameValuePair multipartHeader = getMultipartFormDataHeader(requestPair.getKey().getRequestHeader());
			        if (multipartHeader != null) {
			        	Logger.info("Upload Request");
			        	
			        	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			        	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			        	
			        	Logger.info("Header Name : " + multipartHeader.getName() + " : " + multipartHeader.getValue());
			        	String boundary = getBoundaryFromMPHeader(multipartHeader);
			        	
			        	String reqBody = requestPair.getKey().getRequestBody();
			        	Logger.info("Request Body : " + reqBody);
			        	String[] bodyMultiPart = reqBody.split("--" + boundary);
			        	for (int i=1; i<bodyMultiPart.length; i++) {
			        		String mpItem = bodyMultiPart[i];
			        		String[] mpSplit = mpItem.split(";");
			        		if (mpSplit.length>2) {
			        			String s = mpSplit[1].trim();
			        			String name = s.substring(s.indexOf("\"")+1, s.length()-1);
			        			s = mpSplit[2].trim();
			        			String[] ss = s.split("\"");
			        			
			        			//String fileName = ss[1];
			    	        	String completeFilePath = FileHandlingUtils.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH + "uploadFiles/" + fileName);
			    	        	System.out.println("Complete file path: "+completeFilePath);
			    	        	File file = new File(completeFilePath);
			    	        	builder.addBinaryBody(name, file, ContentType.TEXT_PLAIN, fileName);
			        		} else if (mpSplit.length == 2) {
			        			String s = mpSplit[1].trim();
			        			String[] ss = s.split("\"");
			        			if (ss.length>2) {
			        				builder.addTextBody(ss[1], ss[2], ContentType.TEXT_PLAIN);
			        			} else if (ss.length == 2){
			        				builder.addTextBody(ss[1], "", ContentType.TEXT_PLAIN);
			        			}
			        		}
			        	}
			        	
			        	
			        	HttpEntity entity = builder.build();
			        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), entity, suiteData.getSaasAppUsername());
			        } else {
			        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), suiteData.getSaasAppUsername());
			        }
			        
			        Logger.info("Response Received from gateway");
//			        validateResponseStatus(requestPair.getValue(), response);
			    }
			    Thread.sleep(60000);
			}
		
	
			
			
			public void replayCIQLogs(String logFileName, String fileName) throws UnsupportedEncodingException, Exception {
				
				HttpHost gateway = new HttpHost("10.0.50.68", 443, "http");
				GatewayClient client = new GatewayClient(gateway);
				
				LogParser parser = new LogParser();
				String replayFileName = suiteData.getSaasApp()+"/"+logFileName;
				Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
				
				Logger.info("Number of Requests to be Played : " + requestList.size());
				
				Logger.info("\n Started Replaying Logs\n");
				
				Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
			    while (iterator.hasNext()) {
			        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
			       
			        if (requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("img.en25.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("mozilla.net") ||
			        		requestPair.getKey().getRequestURI().toString().contains("gstatic.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("eloqua.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("office_online")||
			        		requestPair.getKey().getRequestURI().toString().contains("googleads.g.doubleclick.net")||
			        		requestPair.getKey().getRequestURI().toString().contains("sfdcstatic.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("google.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com")||
			        		
			        		requestPair.getKey().getRequestURI().toString().contains("office.net")) {
			        	
			        	System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
			        	continue;
			        }  
			        
			        
			        
			        
			        
			        if (isContainsFileNameHeader(requestPair.getKey().getRequestHeader())) {
			        	Logger.info("Upload Request");
			        	//String fileName = "FoothillSchedule.rtf";
			        	String completeFilePath = FileHandlingUtils.getFileAbsolutePath(LogReplayConstant.REPLAY_LOGFOLDER_PATH + "uploadFiles/" + fileName);
			        	File file = new File(completeFilePath);
			        	FileBody fileBody = new FileBody(file);
			        	 
			        	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			        	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			        	builder.addPart("upfile", fileBody);
			        	HttpEntity entity = builder.build();
			        	
			        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), entity, "testuser1@gatewaybeatle.com");
			        } else {
			        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "testuser1@gatewaybeatle.com");
			        }
			        
			        Logger.info("Response Received from gateway");
			       
//			        validateResponseStatus(requestPair.getValue(), response);
			    }
			    Thread.sleep(60000);
			}
		
			private NameValuePair getMultipartFormDataHeader(List<NameValuePair> headers) {
				NameValuePair multipartHeader = null;
				Iterator<NameValuePair> iterator1 = headers.listIterator();
				while(iterator1.hasNext()) {
					NameValuePair header = iterator1.next();
					if (header.getName().equals("Content-Type") && header.getValue().startsWith("multipart/form-data")) {
						multipartHeader = header;
						Logger.info("Content-Type : " + header.getValue());
						break;
					}
				}
				
				return multipartHeader;
			}
			
			private String getBoundaryFromMPHeader(NameValuePair header) {
				String boundaryLbl = "boundary=";
				String value = header.getValue();
				String boundary = value.substring(value.indexOf(boundaryLbl)+boundaryLbl.length());
				
				return boundary;
			}
			
	
			public void replayInlineInspectionLogs(String logFileName, String fileName) throws UnsupportedEncodingException, Exception {
				
				HttpHost gateway = new HttpHost("10.0.50.68", 443, "http");
				GatewayClient client = new GatewayClient(gateway);
				
				LogParser parser = new LogParser();
				String replayFileName = suiteData.getSaasApp()+"/"+logFileName;
				Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
				
				Logger.info("Number of Requests to be Played : " + requestList.size());
				
				Logger.info("\n Started Replaying Logs\n");
				
				Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
			    while (iterator.hasNext()) {
			        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
			       
			        if (requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("img.en25.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("mozilla.net") ||
			        		requestPair.getKey().getRequestURI().toString().contains("gstatic.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("eloqua.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("office_online")||
			        		requestPair.getKey().getRequestURI().toString().contains("googleads.g.doubleclick.net")||
			        		requestPair.getKey().getRequestURI().toString().contains("sfdcstatic.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("google.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com")||
			        		
			        		requestPair.getKey().getRequestURI().toString().contains("office.net")) {
			        	
			        	System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
			        	continue;
			        }  
			        
			        
			        if (isContainsFileNameHeader(requestPair.getKey().getRequestHeader())) {
			        	Logger.info("Upload Request");
			        	//String fileName = "FoothillSchedule.rtf";
			        	String completeFilePath = FileHandlingUtils.getFileAbsolutePath(fileName);
			        	File file = new File(completeFilePath);
			        	FileBody fileBody = new FileBody(file);
			        	 
			        	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			        	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			        	builder.addPart("upfile", fileBody);
			        	HttpEntity entity = builder.build();
			        	
			        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), entity, "testuser1@gatewaybeatle.com");
			        } else {
			        	HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), "testuser1@gatewaybeatle.com");
			        }
			        
			        Logger.info("Response Received from gateway");
			       
//			        validateResponseStatus(requestPair.getValue(), response);
			    }
			    Thread.sleep(120000);
			}
			
			
			
			private boolean isContainsFileNameHeader(List<NameValuePair> headers) {
				
				Iterator<NameValuePair> iterator1 = headers.listIterator();
				boolean contains = false;
				while(iterator1.hasNext()) {
					NameValuePair header = iterator1.next();
					if (header.getName().equals("X-File-Name")) {
						contains = true;
						Logger.info("X-File-Name : " + header.getValue());
						break;
					}
				}
				
				return contains;
			}
			
			
			public void deleteAllPolicies() throws Exception{
				Reporter.log("Deleting all policies in progress", true);
				PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
				Reporter.log("Deleting all policies is done", true);
			}
			
			public boolean validateLogsFields(String response, Map <String, String> expectedResult  ) {
			       String parentNode = "$.hits.hits[*].source";
			       boolean filterFieldsFromResponse = jsonCommonUtils.filterFieldsFromResponse(response, parentNode, expectedResult);
			       Reporter.log("==>"+filterFieldsFromResponse, true);
			       return filterFieldsFromResponse;
			}
			
			
			@AfterSuite(alwaysRun=true)
			public void beforMethod() throws Exception {
				Reporter.log("--------------------------------------------", true);
				Reporter.log("Deleting all policies", true);
				//deleteAllPolicies();
				Reporter.log("---------------Done-------------------", true);
			}
			
		

			public void addUserToGroups(String userName, String[] groupNames) throws UnsupportedEncodingException, Exception {
				for (String group : groupNames) {
					addUserToGroup(userName, group);
				}
			}

			public void addUserToGroup(String userName, String groupName) throws UnsupportedEncodingException, Exception {
				if (!groupName.isEmpty() && !userName.isEmpty()) {
					String restAPI = "/admin/user/assign";
					String payload = "{\"email\":\"" + userName + "\",\"deleted_groups\":[],\"added_groups\":[\"" + groupName + "\"]}";
					URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);
					System.out.println("Uri:"+uri);
					HttpResponse response = restClient.doPost(uri, requestHeader, null, new StringEntity(payload));
					Reporter.log("Response body:-"+ getResponseBody(response), true);
				}
			}

			public void removeUserFromGroups(String userName, String[] groupNames) throws UnsupportedEncodingException, Exception {
				for (String group : groupNames) {
					removeUserFromGroup(userName, group);
				}
			}

			public void removeUserFromGroup(String userName, String groupName) throws UnsupportedEncodingException, Exception {
				InfraUtils Infrautils = new InfraUtils();
				
				if (!groupName.isEmpty() && !userName.isEmpty()) {
					List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
					queryParams.add(new BasicNameValuePair("group_name", groupName));
					URI groupUsersUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(),"/admin/group/users",queryParams);
					HttpResponse response =  restClient.doGet(groupUsersUri, requestHeader);
					String responseBody = getResponseBody(response);
					Reporter.log("Response Body1 : " + responseBody, true);
					String resource_uri=Infrautils.SearchUserInGrp(responseBody, "tenantgroupusers", "user_email", userName, "resource_uri");
					if(!resource_uri.isEmpty() && (null != resource_uri))
					{
						String restAPI = "/admin/user/assign";
						String payload = "{\"email\":\"" + userName + "\",\"deleted_groups\":[\"" + resource_uri + "\"],\"added_groups\":[]}";
						URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);
						response = restClient.doPost(uri, requestHeader, null, new StringEntity(payload));
						Reporter.log("Response body:-"+ getResponseBody(response), true);
					}
					else
					{
						Reporter.log("User not not present in the Group : " + groupName, true);
					}
				}
			}

			public String getTenantAccountInfo() throws Exception {
				String path = "/admin/tenant/account/info" ;
				URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
				Reporter.log("URI ::"+dataUri.toString(), true);
				List<NameValuePair> headers = getHeaders();
				HttpResponse response =  restClient.doGet(dataUri, headers);
				String responseBody = ClientUtil.getResponseBody(response);
				Reporter.log(responseBody, true);
				return responseBody;
			}
			
			public String getTenantAccountId() throws Exception {
				String accountInfo = this.getTenantAccountInfo();
				JSONObject jso = new JSONObject(accountInfo);
				JSONObject jsdetails = jso.getJSONObject("details");
				org.json.JSONArray tenantArray = jsdetails.getJSONArray("tenant");
				String id = tenantArray.getJSONObject(0).getString("id");
				Reporter.log("Tenant Account Id:" + id, true);
				return id;
			}
			
			public String updateUserAnonymization(String payload) throws Exception {
				String path = "/admin/tenant/update_user_anonymization" ;
				URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
				Reporter.log("URI ::"+dataUri.toString(), true);
				List<NameValuePair> headers = getHeaders();
				HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				String responseBody = ClientUtil.getResponseBody(response);
				return responseBody;
			}

			public String updateMonitoringStatus(String payload) throws Exception {
				String path = "/admin/tenant/update_monitoring_status/";
				URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
				Reporter.log("URI ::"+dataUri.toString(), true);
				List<NameValuePair> headers = getHeaders();
				HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				String responseBody = ClientUtil.getResponseBody(response);
				return responseBody;
			}
}