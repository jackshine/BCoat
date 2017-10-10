package com.elastica.beatle;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import com.amazonaws.regions.Regions;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.dto.IpInfo;
import com.universal.common.Salesforce;
import com.universal.common.ServiceNow;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

public class CommonTest extends InitializeTests {
	protected Client restClient;

	protected TestSuiteDTO suiteData;
	protected UniversalApi universalApi;
	protected UniversalApi universalGraphApi, universalSiteApi;	
	protected UserAccount saasAppUserAccount;
	protected UserAccount saasAppGraphUserAccount;
	protected UserAccount saasAppSiteUserAccount;
	protected String AWSaccessKey;
	protected String AWSaccessKeySecret;     
	protected String AWSRegion;
	private ITestContext suiteConfigurations;
	protected Salesforce sfapi;
	protected ServiceNow snapi;
	protected IpInfo ipInfo;
	protected String socUserName; 
	protected HashMap<String, String> instanceParams = new HashMap<String, String>();

	/**
	 * @param suiteConfigurations
	 * @throws Exception
	 */
	@BeforeTest(alwaysRun=true)
	public void initializeTests(ITestContext suiteConfigurations) throws Exception {
                
		System.setProperty("jsse.enableSNIExtension","false");
		System.setProperty("java.net.preferIPv4Stack" , "true");
		//System.setProperty("javax.net.debug", "all");
        
		this.suiteConfigurations=suiteConfigurations;
		suiteData = new TestSuiteDTO();		
		super.initSuiteConfigurations(suiteConfigurations, suiteData);
		
		restClient = new Client();
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));		
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(SecurletsConstants.SECURLETS_API_CONFIGURATION_FILEPATH));
		
		AWSaccessKey = suiteConfigurations.getCurrentXmlTest().getParameter("accessKey");
		AWSaccessKeySecret = suiteConfigurations.getCurrentXmlTest().getParameter("accessKeySecreat");
		AWSRegion = suiteConfigurations.getCurrentXmlTest().getParameter("AWSregionToRun");	
		
		
		
		
		if (suiteData.getSaasAppUsername() != null && suiteData.getSaasAppPassword() !=null && 		
				suiteData.getSaasAppUserRole() !=null && suiteData.getSaasApp() !=null) {
				
			saasAppUserAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
					suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), null, suiteData.getDomainName());
				
			saasAppGraphUserAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
					suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), "graph");
				
			
			saasAppSiteUserAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
					suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), "site", suiteData.getDomainName());
			
		    if(suiteData.getSaasAppClientId() !=null) {
		    	instanceParams.put("clientId", suiteData.getSaasAppClientId());
		    }
		    
		    if(suiteData.getSaasAppClientSecret() !=null) {
		    	instanceParams.put("clientSecret", suiteData.getSaasAppClientSecret());
		    }
		    
		    if(suiteData.getSaasAppToken() !=null) {
		    	instanceParams.put("token", suiteData.getSaasAppToken());
		    }
			
			if(suiteData.getSaasAppLoginHost() !=null) {
				instanceParams.put("apiHost", suiteData.getSaasAppLoginHost());
				
				saasAppUserAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
											suiteData.getSaasAppUserRole(), instanceParams);
				universalApi = new UniversalApi(suiteData.getSaasApp(), saasAppUserAccount);
			} else {
				universalApi = new UniversalApi(suiteData.getSaasApp(), saasAppUserAccount);
			}
			sfapi = universalApi.getSalesforce();
			snapi = universalApi.getServiceNow();
		}
		ipInfo = this.getIPAddressInfo();
		if (ipInfo == null) {
			/*{
				  "ip": "54.186.103.182",
				  "hostname": "ec2-54-186-103-182.us-west-2.compute.amazonaws.com",
				  "city": "Boardman",
				  "region": "Oregon",
				  "country": "US",
				  "loc": "45.8399,-119.7006",
				  "org": "AS16509 Amazon.com, Inc.",
				  "postal": "97818"
				}*/
			ipInfo = new IpInfo();
			ipInfo.setIp("54.186.103.182");
			ipInfo.setHostname("ec2-54-186-103-182.us-west-2.compute.amazonaws.com");
			ipInfo.setCity("Boardman");
			ipInfo.setRegion("Oregon");
			ipInfo.setCountry("US");
			ipInfo.setLoc("45.8399,-119.7006");
			ipInfo.setOrg("AS16509 Amazon.com, Inc.");
			ipInfo.setPostal("97818");
		}
		
		if (suiteData.getSocUserName() != null) {
			socUserName = suiteData.getSocUserName();
		}
		
		suiteData.setGoldenInputTmplPath(suiteConfigurations.getCurrentXmlTest().getParameter("goldenInputTmplPath"));
		suiteData.setGoldenInputFilePath(suiteConfigurations.getCurrentXmlTest().getParameter("goldenInputPath"));
		suiteData.setEsScriptsHostName(suiteConfigurations.getCurrentXmlTest().getParameter("esScriptsHostName"));
		suiteData.setEsScriptsUserName(suiteConfigurations.getCurrentXmlTest().getParameter("esScriptsUserName"));
			
	}	

	
	/**
	 * Method to regenerate the csrftoken and session id after invalidating current session
	 * 
	 * @throws Exception
	 */
	public void regenerateSession() throws Exception {
		Reporter.log("Invalidating cloudSOC session...", true);
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));	
		Reporter.log("cloudSOC Session regenerated.", true);
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	protected List<NameValuePair> buildBasicHeaders() throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));		
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		return requestHeader;
	}	

	/**
	 * @param response
	 * @return
	 */
	public int getResponseStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();

	}

	/**
	 * @return the accessKey
	 */
	public String getAccessKey() {
		return AWSaccessKey;
	}

	/**
	 * @return the accessKeySecret
	 */
	public String getAccessKeySecret() {
		return AWSaccessKeySecret;
	}	
        
	/**
	 * @param parameter
	 * @return
	 */
	public String getRegressionSpecificSuitParameters(String parameter){
		return suiteConfigurations.getCurrentXmlTest().getParameter(parameter);
	}

	/**
	 * @return the aWSRegion
	 */
	public String getAWSRegion() {
		return AWSRegion;
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	protected List<NameValuePair> getHeaders() throws IOException {
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		headers.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		headers.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		headers.add(new BasicNameValuePair("X-User", suiteData.getUsername().toLowerCase()));
		headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, "csrftoken="+suiteData.getCSRFToken()+";sessionid="+suiteData.getSessionID()+";"));
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		return headers;
	}
		
	public void sleep(long sleeptime) throws Exception {
		try {
			
			double mins = sleeptime/60000.0;
			Reporter.log("Going to wait for " + String.format("%.2f", mins) + " minute(s)", true);
			Thread.sleep(sleeptime);
		} 
		catch(Exception e) {} 
	}
	
	public void sleep(double sleeptime) throws Exception {
		try {
			double mins = Math.round(sleeptime/60000.0);
			Reporter.log("Going to wait for " + mins + " minute(s)", true);
			Thread.sleep((long)sleeptime);
		} 
		catch(Exception e) {} 
	}
	
	public IpInfo getIPAddressInfo() throws Exception {
		//HttpRequest
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		URI dataUri = ClientUtil.BuidURI("http", "ipinfo.io/json", "");
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doGet(dataUri, headers);

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);
		
		IpInfo ipinfo = MarshallingUtils.unmarshall(responseBody, IpInfo.class);
		return ipinfo;
	}
	
	public String getIPLocationInfo(String field) throws Exception {
		//HttpRequest
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		URI dataUri = ClientUtil.BuidURI("http", "http://ipinfo.io/field", "");
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doGet(dataUri, headers);

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);
		return responseBody;
	}
	
	
	
	
	
	
}
