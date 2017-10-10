package com.elastica.action.backend;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.constants.CommonConstants;
import com.elastica.constants.audit.AuditConstants;
import com.elastica.constants.sso.SSOConstants;
import com.elastica.dataHandler.FileHandlingUtils;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.gateway.JsonCommonUtils;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;
import com.elastica.restClient.ESQueryBuilder;
import com.elastica.splunk.ParseSplunkResults;
import com.elastica.splunk.SplunkQueryHandlers;
import com.elastica.splunk.SplunkSearchQuery;
import com.elastica.splunk.SqlunkConstants.ServiceLogs;
import com.elastica.splunk.SqlunkConstants.SplunkHosts;
import com.splunk.Job;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

public class BEAction extends Action{

	public int logSize=100;
	private String response ;
	private String query = "$.hits.hits[*].source";
	private String expectedkey = "message";


	public enum SSOName {
		OneLogin, Okta, Centrify, AzureAD, PingOne, CASite, Bitium
	}

	public HttpResponse getDisplayLogs(Client client, SuiteData suiteData, List<NameValuePair> requestHeader, 
			String apiServer, StringEntity se)  {
		HttpResponse response = null;

		try{
			String requestUri = "/eslogs/displaylogs/";
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), apiServer, requestUri);
			response = client.doPost(dataUri, requestHeader, null, se);
		}catch(Exception e){
			e.getLocalizedMessage();
		}

		return response;
	}

	public HttpResponse getDisplayLogs(Client client, SuiteData suiteData, String apiServer, StringEntity se)  {
		HttpResponse response = null;
		List<NameValuePair>  requestHeader = getCookieHeaders(suiteData);
		try{
			String requestUri = "/eslogs/displaylogs/";
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), apiServer, requestUri);
			response = client.doPost(dataUri, requestHeader, null, se);
		}catch(Exception e){
			e.getLocalizedMessage();
		}

		return response;
	}


	public List<NameValuePair> getBasicHeaders(SuiteData suiteData)  {
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCsrfToken()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));

		return requestHeader;
	}


	public List<NameValuePair> getHeaders(SuiteData suiteData){
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		headers.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCsrfToken()));
		headers.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		headers.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, "csrftoken="+suiteData.getCsrfToken()+";sessionid="+suiteData.getSessionID()+";"));
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		return headers;
	}

	public List<NameValuePair> getGWHeaders(SuiteData suiteData) {
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair("Accept-Language", "en-US,en;q=0.8"));
		headers.add(new BasicNameValuePair("CSP", "active"));
		headers.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36"));
		headers.add(new BasicNameValuePair("Content-Type", "application/json;charset=UTF-8"));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		headers.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCsrfToken()));
		headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, "csrftoken="+suiteData.getCsrfToken()+";sessionid="+suiteData.getSessionID()+";"));
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		headers.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		return headers;
	}

	public List<NameValuePair> getCookieHeaders(SuiteData suiteData){
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair("X-CSRFToken",suiteData.getCsrfToken()));
		requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + suiteData.getSessionID() + "; csrftoken=" + suiteData.getCsrfToken() + ";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("Referer", "https://"+suiteData.getHost()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken",suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		return requestHeader;
	}

	public List<NameValuePair> getCookieHeaders(SuiteData suiteData,Set<Cookie> cookies){
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();

		//requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + suiteData.getSessionID() + "; csrftoken=" + suiteData.getCsrfToken() + ";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("Referer", "https://eoe.elastica-inc.com/static/ng/appAccount/index.html"));
		requestHeader.add(new BasicNameValuePair("X-TenantToken",suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));

		String csrfToken = null; String sessionId = null; String cookie = "";
		for (Cookie s : cookies) {
			//requestHeader.add(new BasicNameValuePair("Set-Cookie",s.getName()+"="+s.getValue()+";"));
			cookie += s.getName()+"="+s.getValue()+";";
			if(s.getName().equalsIgnoreCase("csrftoken")){
				csrfToken = s.getValue(); 
			}
			if(s.getName().equalsIgnoreCase("sessionid")){
				sessionId = s.getValue(); 
			}
		}

		requestHeader.add(new BasicNameValuePair("Cookie",cookie));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken",csrfToken));
		requestHeader.add(new BasicNameValuePair("X-Session", sessionId));


		return requestHeader;
	}

	public List<NameValuePair> getBrowserHeaders(SuiteData suiteData,WebDriver driver) throws IOException{
		String csrfToken="";String sessionId="";
		Set<Cookie> cookies = driver.manage().getCookies();
		for(Cookie c:cookies){
			if(c.getName().equalsIgnoreCase("csrfToken")){
				csrfToken=c.getValue();
			}
			if(c.getName().equalsIgnoreCase("sessionId")){
				sessionId=c.getValue();
			}
			/*if(c.getName().equalsIgnoreCase("__rid__")){
				String rid=c.getValue();
			}*/
		}

		if(csrfToken.isEmpty()||csrfToken.equals(null)){
			csrfToken = suiteData.getCsrfToken();
		}if(sessionId.isEmpty()||sessionId.equals(null)){
			sessionId = suiteData.getSessionID();
		}

		String cookie = cookies.toString().replace("[", "").replace("]", "");
		if(cookie.contains("csrf")&&cookie.contains("session")){}
		else{
			cookie = "csrftoken="+csrfToken+";sessionid="+sessionId+";"+cookies.toString().replace("[", "").replace("]", "");
		}

		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "*/*"));
		headers.add(new BasicNameValuePair("Content-Type", MediaType.MULTIPART_FORM_DATA + "; boundary=-----------------------------171146521411998837801575245130"));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
		headers.add(new BasicNameValuePair("Referer", "https://eoe.elastica-inc.com/static/ng/appAccount/index.html"));
		headers.add(new BasicNameValuePair("X-CSRFToken", csrfToken));
		headers.add(new BasicNameValuePair("X-Session", sessionId));
		headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		headers.add(new BasicNameValuePair("X-User", suiteData.getUsername().toLowerCase()));
		headers.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36"));
		headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, cookie));
		//headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, getAuthParam(suiteData.getUsername(), suiteData.getPassword())));

		//Logger.info(headers.toString());

		return headers;
	}

	public List<NameValuePair> getCookieHeaders(SuiteData suiteData,WebDriver driver) throws IOException{
		String csrfToken="";String sessionId="";
		Set<Cookie> cookies = driver.manage().getCookies();
		for(Cookie c:cookies){
			if(c.getName().equalsIgnoreCase("csrfToken")){
				csrfToken=c.getValue();
			}
			if(c.getName().equalsIgnoreCase("sessionId")){
				sessionId=c.getValue();
			}
		}

		if(csrfToken.isEmpty()||csrfToken.equals(null)){
			csrfToken = suiteData.getCsrfToken();
		}if(sessionId.isEmpty()||sessionId.equals(null)){
			sessionId = suiteData.getSessionID();
		}

		String cookie = cookies.toString().replace("[", "").replace("]", "");
		if(cookie.contains("csrf")&&cookie.contains("session")){}
		else{
			cookie = "csrftoken="+csrfToken+";sessionid="+sessionId+";"+cookies.toString().replace("[", "").replace("]", "");
		}

		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "*/*"));
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
		headers.add(new BasicNameValuePair("Referer", "https://eoe.elastica-inc.com/static/ng/appAccount/index.html"));
		headers.add(new BasicNameValuePair("X-CSRFToken", csrfToken));
		headers.add(new BasicNameValuePair("X-Session", sessionId));
		headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		headers.add(new BasicNameValuePair("X-User", suiteData.getUsername().toLowerCase()));
		headers.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36"));
		headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, cookie));
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));

		//Logger.info(headers.toString());

		return headers;
	}

	public String getGeoIP(Client client, List<NameValuePair> requestHeaders, String uri)  {
		HttpResponse response = null;
		StringBuffer responseString = new StringBuffer();
		try{
			URI dataUri = ClientUtil.BuidURI("https://freegeoip.net/json/");
			response = client.doGet(dataUri, requestHeaders);

			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String inputLine;

			while ((inputLine = reader.readLine()) != null) {
				responseString.append(inputLine);
			}
			reader.close();
		}catch(Exception e){
			e.getLocalizedMessage();
		}

		return responseString.toString();
	}


	public String getSAMLFile(Client client, List<NameValuePair> requestHeaders, String uri)  {
		HttpResponse response = null;
		StringBuffer responseString = new StringBuffer();
		try{
			URI dataUri = ClientUtil.BuidURI(uri);
			response = client.doGet(dataUri, requestHeaders);

			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String inputLine;

			while ((inputLine = reader.readLine()) != null) {
				responseString.append(inputLine);
			}
			reader.close();
		}catch(Exception e){
			e.getLocalizedMessage();
		}

		return responseString.toString();
	}

	public void configureSAMLMetadataFile(Client client, SuiteData suiteData, WebDriver driver) throws Exception {
		SSOName identifier = SSOName.valueOf(suiteData.getSaasAppName());
		HttpResponse response = null;
		switch(identifier){
		case OneLogin:{
			File uploadFile = new java.io.File(SSOConstants.oneLoginMetaFile);

			String serviceJson = "{\"entity_id\":\"https://app.onelogin.com/saml/metadata/\",\"secondary_id_attribute_name\":\"PersonImmutableID\",\"name\":\"OneLogin\",\"slo_supported\":false,\"resource_uri\":\"/api/admin/v1/idps/552c4f593db0e04120b784ef/\",\"last_name_attribute_name\":\"User.LastName\",\"name_attribute_name\":\"User.FirstName\",\"slo_service\":\"https://app.onelogin.com/trust/saml2/http-post/sso/" + suiteData.getSaasAppMetaData() + "\",\"hosts\":[\"app.onelogin.com\"],\"email_attribute_name\":\"User.email\",\"tenant_attribute_name\":\"tobedefined\",\"is_beta\":false,\"auth_supported\":false,\"sso_service\":\"https://app.onelogin.com/trust/saml2/http-post/sso/" + suiteData.getSaasAppMetaData() + "\",\"id\":\"552c4f593db0e04120b784ef\",\"tenant_independent\":false,\"connectorUrl\":\"\"}";

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			builder.setBoundary("-----------------------------171146521411998837801575245130");
			builder.setCharset(Charset.forName("UTF-8"));
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			FileBody fileBody = new FileBody(uploadFile, ContentType.create("text/xml"), "one_login_meta.xml");
			builder.addPart("file", fileBody);
			builder.addTextBody("service", serviceJson);
			//builder.addBinaryBody("file", uploadFile, ContentType.APPLICATION_XML, "onelogin_metadata_491800.xml");

			HttpEntity multipart = builder.build();

			//This is to print the entire entity
			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
					(int) multipart.getContentLength());
			multipart.writeTo(out);
			String entityContentAsString = new String(out.toByteArray());
			Logger.info("multipartEntity:" + entityContentAsString);


			String path = "/admin/user/tenant_sso_status";
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
			Logger.info("URI ::"+dataUri.toString());

			List<NameValuePair> headers = this.getBrowserHeaders(suiteData,driver);
			response =  client.doPost(dataUri, headers, null, multipart);

			break;
		}
		case Okta:{
			break;
		}
		case Centrify:{
			break;
		}
		case AzureAD:{
			break;
		}
		case PingOne:{
			Logger.info("The upload Path: " + SSOConstants.oneLoginMetaFile);
			File uploadFile = new java.io.File(SSOConstants.oneLoginMetaFile);
			String serviceJson = "{\"entity_id\":\"https://pingone.com/idp/cd-330283148.elastica\",\"secondary_id_attribute_name\":\"\",\"name\":\"PingOne\",\"slo_supported\":true,\"resource_uri\":\"/api/admin/v1/idps/5603eb00d567add9c43c623e/\",\"last_name_attribute_name\":\"User.LastName\",\"name_attribute_name\":\"User.FirstName\",\"slo_service\":\"https://sso.connect.pingidentity.com/sso/SLO.saml2\",\"hosts\":[\"pingone.com\"],\"email_attribute_name\":\"User.Email\",\"tenant_attribute_name\":\"tobedefined\",\"is_beta\":false,\"auth_supported\":false,\"sso_service\":\"https://sso.connect.pingidentity.com/sso/idp/SSO.saml2?idpid=0adbe037-065d-47df-8301-c91b8c5c0f03\",\"id\":\"5603eb00d567add9c43c623e\",\"tenant_independent\":false,\"connectorUrl\":\"\"}";

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			builder.setBoundary("-----------------------------171146521411998837801575245130");
			builder.setCharset(Charset.forName("UTF-8"));
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			FileBody fileBody = new FileBody(uploadFile, ContentType.create("text/xml"), "one_login_meta.xml");
			builder.addPart("file", fileBody);
			builder.addTextBody("service", serviceJson);
			//builder.addBinaryBody("file", uploadFile, ContentType.APPLICATION_XML, "onelogin_metadata_491800.xml");

			HttpEntity multipart = builder.build();

			//This is to print the entire entity
			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
					(int) multipart.getContentLength());
			multipart.writeTo(out);
			String entityContentAsString = new String(out.toByteArray());
			Logger.info("multipartEntity:" + entityContentAsString);


			String path = "/admin/user/tenant_sso_status";
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
			Logger.info("URI ::"+dataUri.toString());

			List<NameValuePair> headers = this.getBrowserHeaders(suiteData,driver);
			response =  client.doPost(dataUri, headers, null, multipart);

			break;
		}
		case CASite:{
			break;
		}
		case Bitium:{
			break;
		}
		default:{
			Assert.fail("No configured SSO present");
			break;
		}

		}

		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response Body:"+responseBody);

		Assert.assertEquals(responseBody,"{\"api_response\": 0, \"api_message\": \"Service configured successfully.\"}", 
				"Configuration SAML file failed for SSO provider:"+suiteData.getSaasAppName());


	}

	public String getSAMLMetaDataId(Client client, SuiteData suiteData, WebDriver driver) throws Exception {
		String id="";

		String path = "/admin/user/tenant_sso_status";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Logger.info("URI ::"+dataUri.toString());

		List<NameValuePair> headers = this.getCookieHeaders(suiteData,driver);

		HttpResponse response =  client.doGet(dataUri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response Body:"+responseBody);
		String ssoJson = ClientUtil.getJSONValue(responseBody, "sso");
		id = ClientUtil.getJSONValue(ssoJson, "id").replaceAll("\"", "");

		if(id.equalsIgnoreCase("")){
			Assert.fail("No configured SSO present for "+suiteData.getSaasAppName());
		}

		return id;
	}

	public void deleteSAMLMetadataFile(Client client, SuiteData suiteData, String id, WebDriver driver) throws Exception {

		String path = "/admin/user/remove_sso_provider";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Logger.info("URI ::"+dataUri.toString());

		String deleteJson="{\"app_id\":\""+id+"\"}";
		StringEntity entity = new StringEntity(deleteJson);
		List<NameValuePair> headers = this.getCookieHeaders(suiteData,driver);

		HttpResponse response =  client.doPost(dataUri, headers, null, entity);

		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response Body:"+responseBody);

		Assert.assertEquals("{\"message\": \"success\", \"sso\": [], \"key\": \"sso\", \"action_status\": \"success\"}", 
				responseBody,"Deletion of SAML file failed for SSO provider:"+suiteData.getSaasAppName());
	}

	public String getSAMLMetaDataId(Client client, SuiteData suiteData) throws Exception {
		String id="";

		String path = "/admin/user/tenant_sso_status";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Logger.info("URI ::"+dataUri.toString());

		List<NameValuePair> headers = this.getCookieHeaders(suiteData);

		HttpResponse response =  client.doGet(dataUri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response Body:"+responseBody);
		String ssoJson = ClientUtil.getJSONValue(responseBody, "sso");
		id = ClientUtil.getJSONValue(ssoJson, "id").replaceAll("\"", "");

		if(id.equalsIgnoreCase("")){
			Assert.fail("No configured SSO present for "+suiteData.getSaasAppName());
		}

		return id;
	}

	public void deleteSAMLMetadataFile(Client client, SuiteData suiteData, String id) throws Exception {

		String path = "/admin/user/remove_sso_provider";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Logger.info("URI ::"+dataUri.toString());

		String deleteJson="{\"app_id\":\""+id+"\"}";
		StringEntity entity = new StringEntity(deleteJson);
		List<NameValuePair> headers = this.getCookieHeaders(suiteData);

		HttpResponse response =  client.doPost(dataUri, headers, null, entity);

		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response Body:"+responseBody);

		Assert.assertEquals("{\"message\": \"success\", \"sso\": [], \"key\": \"sso\", \"action_status\": \"success\"}", 
				responseBody,"Deletion of SAML file failed for SSO provider:"+suiteData.getSaasAppName());
	}



	public void deleteSAMLFile(Client client, SuiteData suiteData, WebDriver driver) throws Exception {
		try {
			String id = getSAMLMetaDataId(client, suiteData, driver);
			deleteSAMLMetadataFile(client, suiteData, id, driver);
			Logger.info("After Test :Deleted SAML Meta Data");
		} catch(Exception e) {

		}
	}

	public void deleteSAMLFile(Client client, SuiteData suiteData) throws Exception {
		try {
			String id = getSAMLMetaDataId(client, suiteData);
			deleteSAMLMetadataFile(client, suiteData, id);
			Logger.info("After Test :Deleted SAML Meta Data");
		} catch(Exception e) {

		}
	}


	public void deleteMetaDataFile(SuiteData suiteData) throws Exception {
		try {

			SSOName identifier = SSOName.valueOf(suiteData.getSaasAppName());
			String mode = suiteData.getMode();

			if(mode.equalsIgnoreCase("local")){
				switch(identifier){
				case OneLogin:{
					cleanupFileInFolder(SSOConstants.metaDirectory, SSOConstants.oneLoginMetaFileName);
					break;
				}
				case Okta:{
					break;
				}
				case Centrify:{
					break;
				}
				case AzureAD:{
					break;
				}
				case PingOne:{
					//cleanupFileInFolder(SSOConstants.metaDirectory, SSOConstants.pingOneLoginMetaFileName);
					break;
				}
				case CASite:{
					break;
				}
				case Bitium:{
					break;
				}
				default:{
					Assert.fail("No configured SSO present");
					break;
				}

				}
			}

		} catch(Exception e) {

		}
	}

	public boolean validateLog(Client client,SuiteData suiteData,String fromTime, Map <String, Object> expectedResult) throws Exception {
		hardWait(GatewayTestConstants.INITIAL_WAIT);
		SoftAssert softAssert = new SoftAssert();
		boolean logMatch=false;
		expectedResult.put("transit_hosts", "");
		for (int i=1; i<=GatewayTestConstants.RETRY_COUNT;i++){
			response =fetchElasticSearchLogsUniversal(client,suiteData,fromTime);
			logMatch=RawJsonParser.findExpectedKeysAndPartialValues(response,query,expectedResult, expectedkey);
			if (logMatch){
				break;
			}
			Reporter.log("==================================================================================",true);
			Reporter.log("Expected Log did not appear in this try", true);
			Reporter.log("==================================================================================",true);
			Reporter.log("Hence Retrying again after: "+GatewayTestConstants.RETRY_WAIT+ " seconds for "+i+" time out of: "+GatewayTestConstants.RETRY_COUNT , true);
			Reporter.log("Waiting for 10 seconds...", true);
			Reporter.log("====================================================================================================================================================================================================",true);
			hardWait(GatewayTestConstants.RETRY_WAIT);

		}
		if (!logMatch){
			softAssert.assertEquals("", expectedResult.get(expectedkey).toString(), 
					"Actual value:"+"---Log not found in investiage logs---"+"  Expected value :-"+expectedResult.get(expectedkey).toString());

			Reporter.log("====================================================================================================================================================================================================",true);
			Reporter.log("Expected log did not appear in Investigate logs even after waiting for: "+GatewayTestConstants.RETRY_WAIT*GatewayTestConstants.RETRY_COUNT + " seconds.  Hence making this test failure", true);
			Reporter.log("====================================================================================================================================================================================================",true);
			Reporter.log("====================================================================================================================================================================================================",true);
			softAssert.assertAll();
		}

		//Reporter.log("All json fields matched: "+logMatch, true);
		return logMatch;
	}
	
	public boolean validateLogGeneric(Client client,SuiteData suiteData,String fromTime, Map <String, Object> expectedResult,HashMap<String, String> terms) throws Exception {
		hardWait(GatewayTestConstants.INITIAL_WAIT);
		SoftAssert softAssert = new SoftAssert();
		boolean logMatch=false;
		expectedResult.put("transit_hosts", "");
		for (int i=1; i<=GatewayTestConstants.RETRY_COUNT;i++){
			response =fetchElasticSearchLogsUniversalGeneric(client,suiteData,fromTime,terms);
			logMatch=RawJsonParser.findExpectedKeysAndPartialValues(response,query,expectedResult, expectedkey);
			if (logMatch){
				break;
			}
			Reporter.log("==================================================================================",true);
			Reporter.log("Expected Log did not appear in this try", true);
			Reporter.log("==================================================================================",true);
			Reporter.log("Hence Retrying again after: "+GatewayTestConstants.RETRY_WAIT+ " seconds for "+i+" time out of: "+GatewayTestConstants.RETRY_COUNT , true);
			Reporter.log("Waiting for 10 seconds...", true);
			Reporter.log("====================================================================================================================================================================================================",true);
			hardWait(GatewayTestConstants.RETRY_WAIT);

		}
		if (!logMatch){
			softAssert.assertEquals("", expectedResult.get(expectedkey).toString(), 
					"Actual value:"+"---Log not found in investiage logs---"+"  Expected value :-"+expectedResult.get(expectedkey).toString());

			Reporter.log("====================================================================================================================================================================================================",true);
			Reporter.log("Expected log did not appear in Investigate logs even after waiting for: "+GatewayTestConstants.RETRY_WAIT*GatewayTestConstants.RETRY_COUNT + " seconds.  Hence making this test failure", true);
			Reporter.log("====================================================================================================================================================================================================",true);
			Reporter.log("====================================================================================================================================================================================================",true);
			softAssert.assertAll();
		}

		//Reporter.log("All json fields matched: "+logMatch, true);
		return logMatch;
	}

	public void assertAndValidateLog1(Client client,SuiteData suiteData,String fromTime, Map <String, String> data) throws Exception {
		hardWait(15);
		validateLogsFields(fetchElasticSearchLogsUniversal(client,suiteData,fromTime), data);
		String result = getResult(getExpectedKeyAndValue(GatewayTestConstants.MESSAGE, data, true), 
				getActualKeyAndValue(GatewayTestConstants.MESSAGE, data, true));

		assertTrue(compareResult(getExpectedKeyAndValue(GatewayTestConstants.MESSAGE, data, true), 
				getActualKeyAndValue(GatewayTestConstants.MESSAGE, data, true)),"Test failed, expected log is not found. \n" + result);
	}


	public void assertAndValidateLogNotPresent(Client client,SuiteData suiteData,String fromTime, Map <String, String> data) throws Exception {
		validateLogsFields(fetchElasticSearchLogsUniversal(client,suiteData,fromTime), data);
		String result = getResult(getExpectedKeyAndValue(GatewayTestConstants.MESSAGE, data, true), 
				getActualKeyAndValue(GatewayTestConstants.MESSAGE, data, true));
		assertFalse(compareResult(getExpectedKeyAndValue(GatewayTestConstants.MESSAGE, data, true), 
				getActualKeyAndValue(GatewayTestConstants.MESSAGE, data, true)),"Test failed, expected log is not found. \n" + result);
	}


	public String  fetchElasticSearchLogsUniversal(Client client,SuiteData suiteData,String fromTime) throws Exception{
		HashMap<String, String> terms =new HashMap<String, String> ();
		HttpResponse response = null;
		String payLoad=payloadCommon(suiteData,terms,fromTime);
		StringEntity httpEntity = new StringEntity(payLoad);
		try {
			response=getDisplayLogs (client,suiteData, getHeaders(suiteData) ,suiteData.getApiServer() , httpEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(ClientUtil.getResponseStatusCode(response), HttpStatus.SC_OK, "Response code verification failed");
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("********************************Response Body********************************************");
		Logger.info(responseBody);
		Logger.info("*****************************************************************************************");
		return responseBody;
	}

	public String  fetchElasticSearchLogsUniversalGeneric(Client client,SuiteData suiteData,String fromTime,HashMap<String, String> terms) throws Exception{
		//HashMap<String, String> terms =new HashMap<String, String> ();
		HttpResponse response = null;
		String payLoad=payloadCommon(suiteData,terms,fromTime);
		StringEntity httpEntity = new StringEntity(payLoad);
		try {
			response=getDisplayLogs (client,suiteData, getHeaders(suiteData) ,suiteData.getApiServer() , httpEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(ClientUtil.getResponseStatusCode(response), HttpStatus.SC_OK, "Response code verification failed");
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("********************************Response Body********************************************");
		Logger.info(responseBody);
		Logger.info("*****************************************************************************************");
		return responseBody;
	}
	public String payloadCommon(SuiteData suiteData, HashMap<String, String> terms, String fromTime){
		String payLoad = null;
		ESQueryBuilder eSQueryBuilder = new ESQueryBuilder();
		terms.put("user", suiteData.getSaasAppUsername());
		terms.put("__source", "GW");
		try {
			payLoad = eSQueryBuilder.getESQuery(fromTime, suiteData.getSaasAppName(), terms, suiteData.getTestUsername(),suiteData.getApiServer(), 
					suiteData.getCsrfToken(), suiteData.getSessionID(), logSize, suiteData.getUsername());
			Logger.info("********************************Payload************************************************");
			Logger.info(payLoad);
			Logger.info("****************************************************************************************");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payLoad;
	}
	public String payloadCommonGeneric(SuiteData suiteData, HashMap<String, String> terms, String fromTime){
		String payLoad = null;
		ESQueryBuilder eSQueryBuilder = new ESQueryBuilder();

		try {
			payLoad = eSQueryBuilder.getESQuery(fromTime, suiteData.getSaasAppName(), terms, suiteData.getTestUsername(),suiteData.getApiServer(), 
					suiteData.getCsrfToken(), suiteData.getSessionID(), logSize, suiteData.getUsername());
			Logger.info("********************************Payload************************************************");
			Logger.info(payLoad);
			Logger.info("****************************************************************************************");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payLoad;
	}

	public boolean validateLogsFields(String response, Map <String, String> expectedResult) {
		JsonCommonUtils jsonCommonUtils = new JsonCommonUtils();

		String parentNode = "$.hits.hits[*].source";
		boolean filterFieldsFromResponse = jsonCommonUtils.filterFieldsFromResponse(response, parentNode, expectedResult);
		Logger.info("==>"+filterFieldsFromResponse);
		return filterFieldsFromResponse;
	}

	public boolean compareResult(List<String> expectedLogs, List<String> actualLogs){
		String logStatus =null;
		boolean testPassed = true;

		Logger.info("Expected logs");
		Logger.info("-----------------------------");

		int i=0;
		for(String expectedLog : expectedLogs){
			i++;
			logStatus = (actualLogs.contains(expectedLog)) ? "Passed" : "Failed, log not found";
			Logger.info(i + ") " +expectedLog + " : "+ logStatus);

			if(logStatus.equals("Failed, log not found")){
				Logger.info(" Actual value " + Arrays.toString(actualLogs.toArray()));
				testPassed = false;
			}
		}

		return testPassed;

	}

	public String getResult(List<String> expectedLogs, List<String> actualLogs){
		String logStatus =null;
		String passedLog = "";
		String failelog = "";
		Logger.info("Expected logs");
		Logger.info("-----------------------------");
		for(String expectedLog : expectedLogs){
			logStatus = (actualLogs.contains(expectedLog)) ? "Passed" : "Failed, log not found";
			if(logStatus.equals("Failed, log not found")){
				String actualLogStr = "\n";
				if(expectedLog.contains(GatewayTestConstants.MESSAGE)) { 
					actualLogStr = getActualValueByFilterByArrayToString(GatewayTestConstants.MESSAGE);
				} else if(expectedLog.contains(GatewayTestConstants.ACTIVITY_TYPE)) { 
					actualLogStr = getActualValueByFilterByArrayToString(GatewayTestConstants.ACTIVITY_TYPE);
				} else if(expectedLog.contains(GatewayTestConstants.OBJECT_TYPE)) { 
					actualLogStr = getActualValueByFilterByArrayToString(GatewayTestConstants.OBJECT_TYPE);
				}  else if(expectedLog.contains(GatewayTestConstants.ELASTICA_USER)) { 
					actualLogStr = getActualValueByFilterByArrayToString(GatewayTestConstants.ELASTICA_USER);
				} else if(expectedLog.contains(GatewayTestConstants.DOMAIN)) { 
					actualLogStr = getActualValueByFilterByArrayToString(GatewayTestConstants.DOMAIN);
				} else if(expectedLog.contains(GatewayTestConstants.USER)) { 
					actualLogStr = getActualValueByFilterByArrayToString(GatewayTestConstants.USER);
				} else {
					actualLogStr = Arrays.toString(actualLogs.toArray());
				}
				failelog += "Expected Log Is :\n\t" + expectedLog + "\n But Actual Log Found Are Given Below: \n" + actualLogStr + "\n";
			} else {
				passedLog += expectedLog + " : "+ logStatus + "\n";
			}
		}
		return failelog + passedLog ;
	}

	private String getActualValueByFilterByArrayToString(String activityName) {
		String actualLogStr = "";
		List<String> actualLogStrArr = getActualKeyAndValue(activityName, null, false);
		for(String actualStroriginal : actualLogStrArr) {
			actualLogStr += "\t" + actualStroriginal + "\n";
		}
		return actualLogStr;
	}

	public List<String> getExpectedKeyAndValue(String filter, Map <String, String> data, boolean all) {
		List<String> expectedLog = new ArrayList<String>();
		expectedLog.clear();
		for (Map.Entry<String, String> entry : data.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key.contains(filter) || (all == true)) {
				expectedLog.add(key + "=" + value);
			}
		}
		Logger.info("Expected String " + Arrays.toString(expectedLog.toArray()) + "Size is " + expectedLog.size());
		return expectedLog;
	}

	public List<String> getActualKeyAndValue(String filter, Map <String, String> data, boolean all) {
		List<String> actualLog = new ArrayList<String>();
		actualLog.clear();
		for (Map<String, String> result : GatewayTestConstants.LOG_MESSAGE) {
			Logger.info("Filtered Response :"+result.toString());
			for (Map.Entry<String, String> entry : result.entrySet()) {
				String key = entry.getKey();
				if (key.contains(filter) || (all == true)) {
					actualLog.add(key + "=" + result.get(key));
				}
			}
		}
		//Logger.info("Actual String "  + Arrays.toString(actualLog.toArray()) + "Size is " + actualLog.size());
		return actualLog;
	}

	public String getCurrentTime() {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		currentTime=currentTime.minusMinutes(2);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//.withZone(DateTimeZone.forID("America/Los_Angeles"));
		String tsfrom = currentTime.toString(df);
		Logger.info("Start time: "+tsfrom);
		return tsfrom;
	}

	public boolean validateSplunkLog(SuiteData suiteData) throws Exception {
		SplunkQueryHandlers splunk = new SplunkQueryHandlers();
		SplunkSearchQuery query = new SplunkSearchQuery();
		ParseSplunkResults parse = new ParseSplunkResults();
		String environment = suiteData.getEnvName();
		String pacUrl = suiteData.getProxyUrl();
		if (environment.contains("eoe")) {
			query.addRule(SplunkHosts.EOE);
		} else if (environment.contains("prod")) {
			query.addRule(SplunkHosts.PROD);
		} else if (environment.contains("cep")) {
			query.addRule(SplunkHosts.CEP);
		}
		query.addRule(ServiceLogs.GW);
		query.addRule(suiteData.getTestUsername());
		query.addLimit(10);
		boolean foundHost = false;
		int count;
		for (count = 0; count< CommonConstants.TRY_COUNT; count++) {
			Job job = splunk.executeSplunkQuery(query.getQuery(), "-15m@m");
			List<String> hosts = parse.getHosts(job);
			for (String host : hosts) {
				Logger.info(" Host Details " + host);
				if (environment.contains("eoe")) {
					if (host.contains("EoE") && pacUrl.contains("elastica.pac")) {
						foundHost = true;
						break;
					}
				} else if (environment.contains("prod")) {
					if (host.contains("prd-cul") && pacUrl.contains("elastica_eastcoast.pac")) {
						foundHost = true;
						break;
					} else if (host.contains("prd-sig") && pacUrl.contains("elastica_singapore.pac")) {
						foundHost = true;
						break;
					} else if (host.contains("prd-dub") && pacUrl.contains("elastica_dublin.pac")) {
						foundHost = true;
						break;
					} else if (host.contains("prd-syd") && pacUrl.contains("elastica_sydney.pac")) {
						foundHost = true;
						break;
					} else if (host.contains("prd-sjc") && pacUrl.contains("elastica.pac")) {
						foundHost = true;
						break;
					} 
				} else if (environment.contains("cep")) {
					if (host.contains("cep")) {
						foundHost = true;
						break;
					}
				}
			}
			if (foundHost == true) {
				break;
			} else {
				Thread.sleep(2000);
			}
		}
		if(count >= CommonConstants.TRY_COUNT) {
			Assert.fail("Issue while getting log");
		}
		return foundHost;
	}

	public void createDSForAudit(Client client, SuiteData suiteData) throws Exception{
		String fireWallType = "be_bluecoat_proxy";

		Logger.info("*************Datasource Creation started for:"+fireWallType+"****************");
		String requestPayload= createWebUploadPostBody(fireWallType,suiteData.getEnvName(),
				AuditConstants.AUDIT_WU_DS_NAME);
		Logger.info("Request Payload: "+requestPayload);

		HttpResponse createResp = createDataSource(client,new StringEntity(requestPayload), suiteData);		
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);		
		String createResponse = ClientUtil.getResponseBody(createResp);
		Logger.info("Actual Datasource Response:"+createResponse);

		JSONObject createResponseObject = new JSONObject(createResponse);
		String sourceID = (String) createResponseObject.get("id");

		Logger.info("Getting signed URl for "+fireWallType+" to upload");
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("filename", getFireWallLogFileName(fireWallType)));
		queryParams.add(new BasicNameValuePair("filetype", "application/zip"));		
		HttpResponse signedURLResp = getSignedDataResourceURL(suiteData, client, queryParams, sourceID);
		Assert.assertEquals(signedURLResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String signedURLRespString = ClientUtil.getResponseBody(signedURLResp);
		Logger.info("Actual signedURLResp:"+signedURLRespString);
		JSONObject signedURLObject = new JSONObject(signedURLRespString);	
		String signedURL = (String) signedURLObject.get("signed_request");
		String expectedsignedURLResponse=    " [signed_request=" + "Should not be null"+
				", url=" + "should not be null" +
				", signed_request=" +"signed_request is not empty" +
				", url=" + "url is not empty"+" ]";
		Logger.info("Expected signedURLResp:"+expectedsignedURLResponse);

		Logger.info("Uploading file using S3 signed url for "+ fireWallType);
		HttpResponse uploadFileResponse = uploadFirewallLogFile(client,signedURL.trim(), 
				getFirewallLogFilePath(fireWallType));
		Assert.assertEquals(uploadFileResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		

		Logger.info("Notifying the upload status for "+fireWallType);
		HttpResponse notifyResponse = notifyFileUploadStatus(suiteData, client, sourceID);		
		Assert.assertEquals(notifyResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

	}

	public HttpResponse uploadFirewallLogFile(Client restClient,String signedURL, String filePath) throws Exception {			
		HttpEntity putBodyEntity = new FileEntity(new File(FileHandlingUtils.getFileAbsolutePath(filePath))); 
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair("x-amz-acl", "private"));
		requestHeader.add(new BasicNameValuePair("Content-Type", "application/zip"));
		requestHeader.add(new BasicNameValuePair("x-amz-server-side-encryption", "AES256"));
		Logger.info("******Request payload for Uploading file using S3 signed url*******:"+putBodyEntity);
		Logger.info("Upload Firewall Log: ");
		return restClient.doPut(ClientUtil.BuidURI(signedURL), requestHeader, null, putBodyEntity);
	}

	public String createWebUploadPostBody(String fireWallType, String env, String transportType){
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(fireWallType),transportType));
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditConstants.AUDIT_WEBUPLOAD_FILEFORMAT);						
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));
		return json.toString();
	}

	public HttpResponse createDataSource(Client restClient, HttpEntity entity, SuiteData suiteData) throws Exception{
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(AuditConstants.AUDIT_API_CONFIGURATION_FILEPATH));

		String restAPI = replaceGenericParams(suiteData, suiteData.getAPIMap().get("createDataSource"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiServer(),restAPI);				
		return restClient.doPost(dataUri, getHeaders(suiteData), null, entity);								
	}

	public HttpResponse getSignedDataResourceURL(SuiteData suiteData, Client restClient, 
			List<NameValuePair> queryParams, String sourceID) throws Exception{
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(AuditConstants.AUDIT_API_CONFIGURATION_FILEPATH));

		String signedURL = replaceGenericParams(suiteData, suiteData.getAPIMap().get("getSignedURL"));
		signedURL = signedURL.replace("{sourceID}", sourceID);
		URI signedURI = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiServer(),signedURL,queryParams);
		Reporter.log(" Getting signed URL for dataSource: "+sourceID,true);
		return restClient.doGet(signedURI, getHeaders(suiteData));		
	}

	public HttpResponse notifyFileUploadStatus(SuiteData suiteData, Client restClient, String id) throws IOException, Exception{
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(AuditConstants.AUDIT_API_CONFIGURATION_FILEPATH));

		String notifyURL = replaceGenericParams(suiteData, suiteData.getAPIMap().get("notifyFileUpload"));
		notifyURL = notifyURL.replace("{sourceID}", id);
		String postBody =  "{\"datasource_id\":\""+id+"\", \"last_status\":\"success\"}";
		Logger.info("*******Request payload for Notifying the upload status********: "+postBody);
		StringEntity entity = new StringEntity(postBody);
		Logger.info("Notifying the upload status");
		return restClient.doPost(ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getApiServer(), notifyURL), getHeaders(suiteData), null, entity);			
	}

	private String getCompressFormat(String fireWallType){
		return (fireWallType.contains("7z")?"7z": fireWallType.contains("7za")?"7za" :"zip");
	}

	private String generateDatasourceName(String env, String transportType){
		return "FE_"+env+"_"+transportType+"_"+new SimpleDateFormat("MM:dd:yy_HH:mm:ss.SSS").format(new Date());
	}

	public String getFireWallLogFileName(String fireWallType) {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		return fileProperties.get(fireWallType+".filename");
	}

	public String getFirewallLogFilePath(String fireWallType) {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		return fileProperties.get(fireWallType+".LogFilePath");
	}

	public void deleteTag(Client client, SuiteData suiteData, String id) throws Exception {

		String path = "/admin/tenant/deletetag";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Logger.info("URI ::"+dataUri.toString());

		String deleteJson="{\"id\":\""+id+"\"}";
		StringEntity entity = new StringEntity(deleteJson);
		List<NameValuePair> headers = this.getCookieHeaders(suiteData);

		HttpResponse response =  client.doPost(dataUri, headers, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response Body:"+responseBody);
	}

	public List<String> getAllTags(Client client, SuiteData suiteData) throws Exception {
		List<String> ids=new ArrayList<String>();

		String path = "/admin/tenant/gettagslist";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Logger.info("URI ::"+dataUri.toString());

		List<NameValuePair> headers = this.getCookieHeaders(suiteData);

		HttpResponse response =  client.doGet(dataUri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response Body:"+responseBody);
		String objectsJson = ClientUtil.getJSONValue(responseBody, "objects");
		JSONArray objectsArray = (JSONArray) new JSONTokener(objectsJson).nextValue();
		for (int i = 0; i < objectsArray.size(); i++) {
			String tagName = ClientUtil.getJSONValue(objectsArray.getJSONObject(i).toString(), "tag_name").replace("\"","");
			String id = ClientUtil.getJSONValue(objectsArray.getJSONObject(i).toString(), "id").replace("\"","");
			if(tagName.equalsIgnoreCase("Sanctioned")||tagName.equalsIgnoreCase("Unsanctioned")||
					tagName.equalsIgnoreCase("Block@FW")||tagName.equalsIgnoreCase("Ignore")){
				Logger.info("System set tag name:"+tagName+" and will ignored");
			}else{
				ids.add(id);
			}

		}	
		return ids;
	}


	public void deleteAllTags(Client client, SuiteData suiteData) throws Exception {
		List<String> ids = getAllTags(client, suiteData);
		for(String id:ids){
			deleteTag(client, suiteData, id);
		}
	}

	public void deleteAllDataSources(Client client, SuiteData suiteData) throws Exception {
		List<String> ids = getAllDataSources(client, suiteData);
		for(String id:ids){
			deleteDataSource(client, suiteData, id);
		}
	}

	public void deleteDataSource(Client client, SuiteData suiteData, String id) throws Exception {

		String path = "/risks/deletedatasource";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Logger.info("URI ::"+dataUri.toString());

		String deleteJson="{\"id\":\""+id+"\"}";
		StringEntity entity = new StringEntity(deleteJson);
		List<NameValuePair> headers = this.getCookieHeaders(suiteData);

		HttpResponse response =  client.doPost(dataUri, headers, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response Body:"+responseBody);
	}

	public List<String> getAllDataSources(Client client, SuiteData suiteData) throws Exception {
		List<String> ids=new ArrayList<String>();

		String path = "/risks/datasources";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Logger.info("URI ::"+dataUri.toString());

		List<NameValuePair> headers = this.getCookieHeaders(suiteData);

		HttpResponse response =  client.doGet(dataUri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response Body:"+responseBody);
		String tenantJson = ClientUtil.getJSONValue(responseBody, "tenant");
		String dataSourcesJson = ClientUtil.getJSONValue(tenantJson, "datasources");
		JSONArray dsArray = (JSONArray) new JSONTokener(dataSourcesJson).nextValue();
		for (int i = 0; i < dsArray.size(); i++) {
			String id = ClientUtil.getJSONValue(dsArray.getJSONObject(i).toString(), "id").replace("\"","");
			ids.add(id);
		}
		return ids;
	}


	public String replaceGenericParams(SuiteData suiteData, String url){
		if(url.contains("tenantName"))
			url = url.replace("{tenantName}", suiteData.getTenantName());
		if(url.contains("version"))
			url = url.replace("{version}", suiteData.getBaseVersion());
		return url;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getGeoIPData(){
		Logger.info("**************************************************************************");
		Logger.info("Fetching the GeoIP location of target machine...");
		Map<String,String> result = new HashMap<String,String>();
		Client restClient = new Client();
		URI uri;
		try {
			uri = ClientUtil.BuidURI("http://ip-api.com/json");
			HttpResponse response = restClient.doGet(uri, null);
			String respBody = ClientUtil.getResponseBody(response);

			Map<String,Object> fetchSingleField = (Map<String,Object>) RawJsonParser.fetchSingleField(respBody, "$");
			RawJsonParser.printMap(fetchSingleField);

			result.clear();
			result.put(GatewayTestConstants.CITY, (String)fetchSingleField.get(GatewayTestConstants.CITY) );
			result.put(GatewayTestConstants.TIME_ZONE, (String)fetchSingleField.get("timezone") );
			result.put(GatewayTestConstants.REGION, (String)fetchSingleField.get(GatewayTestConstants.REGION) );
			result.put(GatewayTestConstants.COUNTRY, (String)fetchSingleField.get(GatewayTestConstants.COUNTRY) );
			result.put(GatewayTestConstants.HOST, (String)fetchSingleField.get("query") );
			String longi=Double.toString((Double)fetchSingleField.get("lon")) ;

			String latti=Double.toString((Double)fetchSingleField.get("lat")) ;


			String longitude= longi.substring(0, longi.indexOf('.')+3);
			String lattitude= latti.substring(0, latti.indexOf('.')+3);
			result.put(GatewayTestConstants.LONGITUDE, longitude );
			result.put(GatewayTestConstants.LATITUDE, lattitude );

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.info("GeoIP location fetching completed");
		return result;
	}
	
	public String getPACUrl(Client restClient, SuiteData suiteData) throws Exception{
		String proxyUrl = "/admin/user/get_proxy";
		List<NameValuePair> headers = getHeaders(suiteData);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), proxyUrl);
		HttpResponse response = restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		String pacUrl = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "objects"), "url");
		pacUrl = pacUrl.substring(1, pacUrl.length()-1);
		return pacUrl;
	}

}
