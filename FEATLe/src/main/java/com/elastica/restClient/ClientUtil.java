/**
 * 
 */
package com.elastica.restClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

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
	
	public static int getResponseStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();

	}
	
	public static String getAuthParam(String username, String password){		
		return "Basic "+new String(Base64.encodeBase64((username + ":" + password).getBytes()));
	}		
	
}