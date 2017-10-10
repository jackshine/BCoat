/**
 * 
 */
package com.elastica.beatle.RestClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Reporter;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;

/**
 * @author anuvrath
 *
 */
public class ClientUtil {
		
	/**
	 * @param scheme
	 * @param host
	 * @param url
	 * @param queryParams
	 * @return
	 * @throws URISyntaxException
	 */
	public static URI BuidURI(String scheme, String host,String url, List<NameValuePair> queryParams) throws URISyntaxException {		
		if (queryParams != null) {
			return new URIBuilder().setScheme(scheme).setHost(host).setPath(url).setParameters(queryParams).build();
		} else {
			return new URIBuilder().setScheme(scheme).setHost(host).setPath(url).build();	
		}
		
	}
	
	/**
	 * @param scheme
	 * @param host
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static URI BuidURI(String scheme, String host,String url) throws Exception{
		return new URIBuilder().setScheme(scheme).setHost(host).setPath(url).build();
	}
	
	/**
	 * @param signedURL
	 * @return
	 * @throws URISyntaxException 
	 */
	public static URI BuidURI(String signedURL) throws URISyntaxException {
		return new URIBuilder(signedURL).build();
	}

	/**
	 * @param json
	 * @param key
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static String getJSONValue(String json, String key) throws JsonProcessingException, IOException {
        return new ObjectMapper(new JsonFactory()).readTree(json).get(key).toString();
    }
	
	/**
	 * 
	 * @param response
	 * @return
	 */	
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
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<NameValuePair> buildCookieHeaders(TestSuiteDTO suiteData) throws Exception{
		List<NameValuePair> reqHeader = new ArrayList<NameValuePair>();
		try{
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		String csrfToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
		String sessionID = AuthorizationHandler.getUserSessionID(CSRFHeader);
		reqHeader.add(new BasicNameValuePair("X-CSRFToken",csrfToken));
		reqHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + sessionID + "; csrftoken=" + csrfToken + ";"));
		reqHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		reqHeader.add(new BasicNameValuePair("Referer", "https://"+suiteData.getHost()));
		reqHeader.add(new BasicNameValuePair("X-Session", sessionID));
		reqHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		reqHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		}
		catch(Exception e) {
			Reporter.log("Exception occured on test case initialization and so the test cases will be skipped", true);
			e.printStackTrace();
		}
		return reqHeader;
	}
}