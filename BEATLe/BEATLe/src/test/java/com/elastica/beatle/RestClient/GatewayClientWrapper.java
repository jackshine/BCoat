/**
 * 
 */
package com.elastica.beatle.RestClient;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.util.Iterator;
import java.util.List;

import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.MagicCookie;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;

/**
 * @author anuvrath, Rocky
 *
 */
public class GatewayClientWrapper {
		
	public static HttpResponse executeGatewayRequest(GatewayClient client, LogReplayRequestDTO request, String elasticaUser) throws UnsupportedEncodingException, Exception{		
		List<NameValuePair> headers = request.getRequestHeader();
		headers = removeContentLength(headers);
		headers = updateCookieElasticaUser(headers, elasticaUser);
		headers.add(getMagicCookie(elasticaUser));
		
		if(request.getMethodType().equalsIgnoreCase("GET"))
			return client.doGetNoRedirect(request.getRequestURI(), headers);
		else if(request.getMethodType().equalsIgnoreCase("POST"))
			return client.doPost(request.getRequestURI(), headers, null, new StringEntity(request.getRequestBody()));
		else if(request.getMethodType().equalsIgnoreCase("PUT"))
			return client.doPut(request.getRequestURI(), headers, null, new StringEntity(request.getRequestBody()));
		else if(request.getMethodType().equalsIgnoreCase("DELETE")){
			if(request.getRequestBody() == null)
				return client.doDelete(request.getRequestURI(), headers);
			else
				return client.doDelete(request.getRequestURI(), headers,new StringEntity(request.getRequestBody()));
		}
		else if(request.getMethodType().equalsIgnoreCase("OPTIONS")){
			return client.doOptions(request.getRequestURI(), headers);
		}
		else
			return null;
	}
	
	public static HttpResponse executeGatewayRequest(GatewayClient client, LogReplayRequestDTO request, HttpEntity mEntity, String elasticaUser) throws UnsupportedEncodingException, Exception{		
		List<NameValuePair> headers = request.getRequestHeader();
		headers = removeContentLength(headers);
		headers = updateCookieElasticaUser(headers, elasticaUser);
		headers.add(getMagicCookie(elasticaUser));
		
		if(request.getMethodType().equalsIgnoreCase("GET"))
			return client.doGet(request.getRequestURI(), headers);
		else if(request.getMethodType().equalsIgnoreCase("POST"))
			return client.doPost(request.getRequestURI(), headers, null, mEntity);
		else if(request.getMethodType().equalsIgnoreCase("PUT"))
			return client.doPut(request.getRequestURI(), headers, null, new StringEntity(request.getRequestBody()));
		else if(request.getMethodType().equalsIgnoreCase("DELETE")){
			if(request.getRequestBody() == null)
				return client.doDelete(request.getRequestURI(), headers);
			else
				return client.doDelete(request.getRequestURI(), headers,new StringEntity(request.getRequestBody()));
		}
		else if(request.getMethodType().equalsIgnoreCase("OPTIONS")){
			return client.doOptions(request.getRequestURI(), headers);
		}
		else
			return null;
	}
	
	public static HttpResponse executeGatewayRequest(GatewayClient client, LogReplayRequestDTO request) throws UnsupportedEncodingException, Exception {
		String defaultElasticaUser = "rocky.mohanraj@elastica.co";
		
		return executeGatewayRequest(client, request, defaultElasticaUser);
	}
	
	private static NameValuePair getMagicCookie(String elasticaUserName) {
		return new BasicNameValuePair("ELASTICA_MAGIC_COOKIE", new MagicCookie().getMagicCookie() + ":" + elasticaUserName);
	}
	
	private static List<NameValuePair> updateCookieElasticaUser(List<NameValuePair> headers, String elasticaUser) {
		
		Iterator<NameValuePair> iterator1 = headers.listIterator();
		int cookieIdx = -1;
		while(iterator1.hasNext()) {
			cookieIdx++;
			NameValuePair header = iterator1.next();
			if (header.getName().equals("Cookie")) {
				Logger.info("Cookie : " + header.getValue());
				String cookie = header.getValue();
				String updatedCookie = "";
				for (String cItem: cookie.split(";")){
					if (cItem.contains("ELASTICA_UNAME")) {
						updatedCookie += " ELASTICA_UNAME=" + elasticaUser + ";";
					} else {
						updatedCookie += cItem + ";";
					}
				}
				Logger.info("UpdatedCookie : " + updatedCookie);
				headers.set(cookieIdx, new BasicNameValuePair("Cookie", updatedCookie));
			}
		}
		
		return headers;
	}
	
	private static List<NameValuePair> removeContentLength(List<NameValuePair> headers) {
		
		Iterator<NameValuePair> iterator1 = headers.listIterator();
		int clIdx = -1;
		while(iterator1.hasNext()) {
			clIdx++;
			NameValuePair header = iterator1.next();
			if (header.getName().equals("Content-Length")) {
				Logger.info("Content-Length : " + header.getValue());
				headers.remove(clIdx);
				break;
			}
		}
		
		return headers;
	}
}