/**
 * 
 */
package com.elastica.restClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.elastica.constants.FrameworkConstants;
import com.elastica.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AuthorizationHandler {
	public HttpResponse getCSRFHeaders(String username, String password, String scheme, String host){
		return getLoginResponse(username, password, scheme, host);		
	}	
	public String getCSRFToken(HttpResponse response){
				
		Header[] cookieHeaders = response.getHeaders("Set-Cookie");
		HashMap<String, String> cookieMap = new HashMap<String, String>(cookieHeaders.length);

		String csrfToken = null;

		for (Header header : cookieHeaders) {
			cookieMap.put(header.getName(), header.getValue());

			if (header.getValue().startsWith("csrftoken")) {
				csrfToken = header.getValue().substring(header.getValue().indexOf("=") + 1, header.getValue().indexOf(";"));
			}
		}
		Logger.info("**************************************************************************");
		Logger.info("csrfToken: "+csrfToken);
		return csrfToken;
	}
	
	public String getUserSessionID(HttpResponse response){
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
		Logger.info("**************************************************************************");
		return sessionId;
	}
	
	private HttpResponse getLoginResponse(String username, String password, String scheme, String host) {
		
		Client restClient = new Client();
		HttpResponse loginResponse = null;
		
		try {
			URI adminLoginURI = ClientUtil.BuidURI(scheme, host, "/admin/deployment");
			HttpResponse preLoginResponse = restClient.doGet(adminLoginURI, null);
			
			String csrfToken = getAttributeFromResponseHeader(preLoginResponse.getAllHeaders(), "csrftoken");			
			URI appForensicURI = ClientUtil.BuidURI(scheme, host, "/static/ng/appForensics/index.html");			
			List<NameValuePair> headerArray = new ArrayList<NameValuePair>();
			headerArray.add(new BasicNameValuePair("Connection", "keep-alive"));
			headerArray.add(new BasicNameValuePair("Accept", "application/json, text/plain, */*"));
			headerArray.add(new BasicNameValuePair("Origin", scheme+"://"+host));
			headerArray.add(new BasicNameValuePair("X-Requested-With", "XMLHttpRequest"));
			headerArray.add(new BasicNameValuePair("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"));
			headerArray.add(new BasicNameValuePair("X-CSRFToken", csrfToken));
			headerArray.add(new BasicNameValuePair("Content-Type", "application/json;charset=UTF-8"));
			headerArray.add(new BasicNameValuePair("Referer", appForensicURI.toString()));
			headerArray.add(new BasicNameValuePair("Accept-Language", "en-US,en;q=0.8"));
			if(host.contains("eoe")){
				headerArray.add(new BasicNameValuePair("Cookie", "csrftoken=" + csrfToken + ";mf_authenticated="+FrameworkConstants.accessTokenEoE+";"));
			}else if(host.contains("qa")){
				headerArray.add(new BasicNameValuePair("Cookie", "csrftoken=" + csrfToken + ";mf_authenticated="+FrameworkConstants.accessTokenQAVPC+";"));
			}else{
				headerArray.add(new BasicNameValuePair("Cookie", "csrftoken=" + csrfToken + ";mf_authenticated="+FrameworkConstants.accessTokenOthers+";"));
			}
			
			URI loginURI = ClientUtil.BuidURI(scheme,host,"/user/loginapi");
			StringEntity sEntity = new StringEntity(new JSONObject().put("email",username).put("password", password).toString());
			loginResponse = restClient.doPost(loginURI, headerArray, null, sEntity);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return loginResponse;
	}		
	
	public String getAttributeFromResponseHeader(Header[] lHeaders, String sAttribute) {
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
}