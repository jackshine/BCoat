/**
 * 
 */
package com.elastica.beatle.Authorization;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.testng.Reporter;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.constants.FrameworkConstants;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AuthorizationHandler {
	
	public static String getAuthParam(String username, String password) throws IOException {
		String authParam = "Basic "+new String(Base64.encodeBase64((username + ":" + password).getBytes()));
		Logger.info("AuthParam for "+username +" and "+password+" is "+ authParam);
		return authParam;
	}		
	
	public static HttpResponse getCSRFHeaders(String username, String password, TestSuiteDTO suiteData) throws Exception {
		return getLoginResponse(username, password, suiteData);		
	}	
	
	public static synchronized String getCSRFToken(HttpResponse response){
		
		String csrfToken = null;
		
		
		if (null==csrfToken) {
			
			Header[] cookieHeaders = response.getHeaders("Set-Cookie");
			HashMap<String, String> cookieMap = new HashMap<String, String>(cookieHeaders.length);
			for (Header header : cookieHeaders) {
				cookieMap.put(header.getName(), header.getValue());

				if (header.getValue().startsWith("csrftoken")) {
					csrfToken = header.getValue().substring(header.getValue().indexOf("=") + 1,
							header.getValue().indexOf(";"));

				}
			} 
		}
		Logger.info("csrfToken: "+csrfToken);
		return csrfToken;
	}
	
	public static synchronized String getUserSessionID(HttpResponse response){
		Header[] cookieHeaders = response.getHeaders("Set-Cookie");

		HashMap<String, String> cookieMap = new HashMap<String, String>(cookieHeaders.length);

		String sessionId = null;

		for (Header header : cookieHeaders) {
			cookieMap.put(header.getName(), header.getValue());

			if (header.getValue().startsWith("sessionid")) {
				sessionId = header.getValue().substring(header.getValue().indexOf("=") + 1, header.getValue().indexOf(";"));
			}
		}
		Logger.info("SessionID: "+sessionId);
		return sessionId;
	}
	
	private static synchronized HttpResponse getLoginResponse(String uName, String pwd,TestSuiteDTO suiteData) {
		
		Client restClient = new Client();
		HttpResponse loginResponse = null;
		
		try {
			URI adminLoginURI = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/admin/deployment");
			HttpResponse preLoginResponse = restClient.doGet(adminLoginURI, null);
			
			String csrfToken = getAttributeFromResponseHeader(preLoginResponse.getAllHeaders(), "csrftoken");			
			URI appForensicURI = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/static/ng/appForensics/index.html");			
			List<NameValuePair> headerArray = new ArrayList<NameValuePair>();
			headerArray.add(new BasicNameValuePair("Connection", "keep-alive"));
			headerArray.add(new BasicNameValuePair("Accept", "application/json, text/plain, */*"));
			headerArray.add(new BasicNameValuePair("Origin", suiteData.getScheme()+"://"+suiteData.getHost()));
			headerArray.add(new BasicNameValuePair("X-Requested-With", "XMLHttpRequest"));
			headerArray.add(new BasicNameValuePair("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"));
			headerArray.add(new BasicNameValuePair("X-CSRFToken", csrfToken));
			headerArray.add(new BasicNameValuePair("Content-Type", "application/json;charset=UTF-8"));
			headerArray.add(new BasicNameValuePair("Referer", appForensicURI.toString()));
			headerArray.add(new BasicNameValuePair("Accept-Language", "en-US,en;q=0.8"));

			if (suiteData.getEnvironmentName().equalsIgnoreCase("eoe")) {
				headerArray.add(new BasicNameValuePair("Cookie", "csrftoken=" + csrfToken + ";mf_authenticated="+FrameworkConstants.EOE_COOKIE+";"));
			} else if (suiteData.getEnvironmentName().contains("qa")) {
				headerArray.add(new BasicNameValuePair("Cookie", "csrftoken=" + csrfToken + ";mf_authenticated="+FrameworkConstants.QAVPC_COOKIE+";"));
			} else {
				headerArray.add(new BasicNameValuePair("Cookie", "csrftoken=" + csrfToken + ";mf_authenticated="+FrameworkConstants.OTHERS_COOKIE+";"));
			}

			JSONObject loginObject = new JSONObject().put("email", uName).put("password", pwd);
			if (suiteData.getCustDomain() != null && suiteData.getCustDomain().trim().length() > 0) {
				loginObject.put("custdomain", suiteData.getCustDomain());
			}
			
			URI loginURI = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(),"/user/loginapi");
			StringEntity sEntity = new StringEntity(loginObject.toString());
			loginResponse = restClient.doPost(loginURI, headerArray, null, sEntity);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return loginResponse;
	}		
	
	public static String getAttributeFromResponseHeader(Header[] lHeaders, String sAttribute) {
		String sHeaderAttribute = null;

		try {
			for (Header header : lHeaders) {
				if (header.getValue().indexOf(sAttribute) > -1) {
					sAttribute = sAttribute + "=";
					int startAt = header.getValue().indexOf(sAttribute) + sAttribute.length();
					int endAt = header.getValue().indexOf(";", startAt);
					sHeaderAttribute = header.getValue().substring(startAt, endAt);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (sHeaderAttribute);
	}
	
	public static void enableAnonymization(TestSuiteDTO suiteData) throws Exception {
		String tenantAcctId = getTenantAccountId(suiteData);
		String payload = "{\"userAnonymization\":true,\"dpoName\": \"\", \"dpoPassword\": \"\",\"id\":\""+tenantAcctId+"\"}";
		Reporter.log("Payload:"+ payload, true);
		String responseBody = updateUserAnonymization(suiteData, payload);
		Reporter.log("Enable anonymization response:"+responseBody, true);
		
		Reporter.log("Invalidating cloudSOC session...", true);
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));
	}
	
	public static void disableAnonymization(TestSuiteDTO suiteData) throws Exception {
		String tenantAcctId = getTenantAccountId(suiteData);
		String payload = "{\"userAnonymization\":false,\"dpoName\":\""+ suiteData.getDpoUsername() +"\",\"dpoPassword\":\""+ suiteData.getDpoPassword() +"\", \"id\":\""+tenantAcctId+"\"}";
		Reporter.log("Payload:"+ payload, true);
		String responseBody = updateUserAnonymization(suiteData, payload);
		Reporter.log("Disable anonymization response:"+responseBody, true);

		Reporter.log("Invalidating cloudSOC session...", true);
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));
	}
	
	public static String updateUserAnonymization(TestSuiteDTO suiteData, String payload) throws Exception {
		
		Client restClient = new Client();
		String path = suiteData.getAPIMap().get("getUIUserAnonymizationInfo") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		
		Reporter.log("URI ::"+dataUri.toString(), true);
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		headers.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		headers.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		headers.add(new BasicNameValuePair("X-User", suiteData.getUsername().toLowerCase()));
		headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, "csrftoken="+suiteData.getCSRFToken()+";sessionid="+suiteData.getSessionID()+";"));
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		return responseBody;
	}
	
	public static String getTenantAccountId(TestSuiteDTO suiteData) throws Exception {
		String path = suiteData.getAPIMap().get("getUITenantAccountInfo") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		headers.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		headers.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		headers.add(new BasicNameValuePair("X-User", suiteData.getUsername().toLowerCase()));
		headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, "csrftoken="+suiteData.getCSRFToken()+";sessionid="+suiteData.getSessionID()+";"));
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		
		Client restClient = new Client();
		HttpResponse response =  restClient.doGet(dataUri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log(responseBody, true);
		JSONObject jso = new JSONObject(responseBody);
		JSONObject jsdetails = jso.getJSONObject("details");
		org.json.JSONArray tenantArray = jsdetails.getJSONArray("tenant");
		String id = tenantArray.getJSONObject(0).getString("id");
		Reporter.log("Tenant Account Id:" + id, true);
		return id;
	}

	
}