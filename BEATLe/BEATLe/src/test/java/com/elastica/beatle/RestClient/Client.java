package com.elastica.beatle.RestClient;


import java.net.URI;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.testng.Reporter;

import com.elastica.beatle.constants.FrameworkConstants;
import com.elastica.beatle.logger.Logger;
import com.google.api.client.util.Sleeper;

/**
 * @author anuvrath
 *
 */
public class Client extends CommonClient{

	public Client() {
		super();
	}	
	
	/**
	 * @param uri
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public HttpResponse doGet(URI uri, List<NameValuePair> headers) throws Exception {
		HttpUriRequest request = new HttpGet(uri);
		Logger.info("************************************************");
		Logger.info("GET Request:"+uri.toString());
		int retry = 0;
		HttpResponse response = null;
		
		do {
			response = executeRestRequest(request, headers);
			if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				retry++;
				try {
					Reporter.log("Request failed with "+ HttpStatus.SC_INTERNAL_SERVER_ERROR +" error. Retrying the request...", true);
					Reporter.log("Waiting for a minute before retry...", true);
					Thread.sleep(60000);
				} catch (InterruptedException e) {}
			} else {
				break;
			}
		} while (retry <=FrameworkConstants.retryCount);
		
		if (!isSuccessfulResponse(response.getStatusLine().toString())) {
			printHeaders(response.getAllHeaders());
		}		
		Logger.info("Response Status:"+response.getStatusLine());
		Logger.info("************************************************");
		return response;
	}

	/**
	 * @param uri
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public HttpResponse doGetNoRedirect(URI uri, List<NameValuePair> headers) throws Exception {		
		HttpParams params = new BasicHttpParams();
	    params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
	    
		HttpUriRequest request = new HttpGet(uri);
		request.setParams(params);
		Logger.info("************************************************");
		Logger.info("GET Request:"+uri.toString());
		HttpResponse response = executeRestRequest(request, headers);
		if (!isSuccessfulResponse(response.getStatusLine().toString())) {
			printHeaders(response.getAllHeaders());
		}		
		Logger.info("Response Status:"+response.getStatusLine());
		Logger.info("************************************************");
		return response;
	}
	
	/**
	 * @param uri
	 * @param headers
	 * @param matrixParams
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public HttpResponse doPost(URI uri, List<NameValuePair> headers, List<NameValuePair> matrixParams, HttpEntity entity) throws Exception {
		HttpPost httpPost = new HttpPost(uri);
		if(entity != null) {
			httpPost.setEntity(entity);
		}
		if(matrixParams != null && matrixParams.size() > 0) {
			httpPost.setEntity(new UrlEncodedFormEntity(matrixParams, "UTF-8"));
		}
		HttpUriRequest request = httpPost;
		Logger.info("************************************************");
		Logger.info("POST Request:"+uri.toString());
		HttpResponse response = executeRestRequest(request, headers);
		Logger.info("Response Status:"+response.getStatusLine());
		Logger.info("************************************************");
		if (!isSuccessfulResponse(response.getStatusLine().toString())) {
			printHeaders(response.getAllHeaders());
		}
		return response;
	}
	
	
	/**
	 * @param uri
	 * @param headers
	 * @param matrixParams
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public HttpResponse doPut(URI uri, List<NameValuePair> headers, List<NameValuePair> matrixParams, HttpEntity entity) throws Exception {
		HttpPut httpPut = new HttpPut(uri);
		if(entity != null) 
			httpPut.setEntity(entity);
		if(matrixParams != null && !matrixParams.isEmpty()) 
			httpPut.setEntity(new UrlEncodedFormEntity(matrixParams, "UTF-8"));
		HttpUriRequest request = httpPut;
		Logger.info("************************************************");
		Logger.info("PUT Request:"+uri.toString());
		HttpResponse response = executeRestRequest(request, headers);
		if (!isSuccessfulResponse(response.getStatusLine().toString())) {
			printHeaders(response.getAllHeaders());
		}	
		Logger.info("Response Status:"+response.getStatusLine());
		Logger.info("************************************************");
		return response;
	}
	
	/**
	 * @param uri
	 * @param headers
	 * @param matrixParams
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public HttpResponse doPatch(URI uri, List<NameValuePair> headers, List<NameValuePair> matrixParams, HttpEntity entity) throws Exception {
		HttpPatch httpPatch = new HttpPatch(uri);
		if(entity != null) 
			httpPatch.setEntity(entity);
		if(matrixParams != null && !matrixParams.isEmpty()) 
			httpPatch.setEntity(new UrlEncodedFormEntity(matrixParams, "UTF-8"));
		HttpUriRequest request = httpPatch;
		Logger.info("************************************************");
		Logger.info("PATCH Request: "+uri.toString());
		HttpResponse response = executeRestRequest(request, headers);
		if (!isSuccessfulResponse(response.getStatusLine().toString())) {
			printHeaders(response.getAllHeaders());
		}	
		Logger.info("Response Status:"+response.getStatusLine());
		Logger.info("************************************************");
		return response;
	}
	
	
	/**
	 * @param uri
	 * @param headers
	 * @param matrixParams
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public HttpResponse doPatchWithRetry(URI uri, List<NameValuePair> headers, List<NameValuePair> matrixParams, HttpEntity entity) throws Exception {
		HttpPatch httpPatch = new HttpPatch(uri);
		int retry = 0;
		HttpResponse response = null;
		if(entity != null) 
			httpPatch.setEntity(entity);
		if(matrixParams != null && !matrixParams.isEmpty()) 
			httpPatch.setEntity(new UrlEncodedFormEntity(matrixParams, "UTF-8"));
		HttpUriRequest request = httpPatch;
		Logger.info("************************************************");
		Logger.info("PATCH Request: "+uri.toString());
		
		do {
			response = executeRestRequest(request, headers);
			if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				retry++;
				try {
					Reporter.log("Waiting for a minute before retry...");
					Thread.sleep(60000);
				} catch (InterruptedException e) {}
			} else {
				break;
			}
		} while (retry <=FrameworkConstants.retryCount);
		
		
		if (!isSuccessfulResponse(response.getStatusLine().toString())) {
			printHeaders(response.getAllHeaders());
		}	
		
		
		Logger.info("Response Status:"+response.getStatusLine());
		Logger.info("************************************************");
		return response;
	}
	
		
	/**
	 * @param uri
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public HttpResponse doDelete(URI uri, List<NameValuePair> headers) throws Exception {
		HttpUriRequest request = new HttpDelete(uri);
		Logger.info("************************************************");
		Logger.info("DELETE Request:"+uri.toString());
		HttpResponse response = executeRestRequest(request, headers);
		if (!isSuccessfulResponse(response.getStatusLine().toString())) {
			printHeaders(response.getAllHeaders());
		}	
		Logger.info("Response Status:"+response.getStatusLine());
		Logger.info("************************************************");
		return response;
	}
	
	/**
	 * @param uri
	 * @param headers
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public HttpResponse doDelete(URI uri, List<NameValuePair> headers, HttpEntity entity) throws Exception {
		HttpDeleteWithBody httpDeleteWithBody = new HttpDeleteWithBody(uri);
        httpDeleteWithBody.setEntity(entity);
		Logger.info("************************************************");
        Logger.info("DELETE Request:"+uri.toString());
        HttpResponse response = executeRestRequest(httpDeleteWithBody, headers);
		if (!isSuccessfulResponse(response.getStatusLine().toString())) {
			printHeaders(response.getAllHeaders());
		}	
		Logger.info("Response Status:"+response.getStatusLine());
		Logger.info("************************************************");
		return response;
	}
		
	/**
	 * @param uri
	 * @param headers
	 * @return
	 */
	public HttpResponse doOptions(URI uri,List<NameValuePair> headers){
		HttpUriRequest request = new HttpOptions(uri);
		Logger.info("************************************************");
		Logger.info("OPTIONS Request: "+uri.toString());
		HttpResponse response = executeRestRequest(request, headers);
		if (!isSuccessfulResponse(response.getStatusLine().toString())) {
			printHeaders(response.getAllHeaders());
		}	
		Logger.info("Response Status:"+response.getStatusLine());
		Logger.info("************************************************");
		return response;
	}			
	
	/**
	 * @param allHeaders
	 */
	private synchronized void printHeaders(Header[] allHeaders) {
		Logger.info("Response Headers:");
		for(int i=0;i<allHeaders.length;i++){
			Logger.info(allHeaders[i].getName()+":"+allHeaders[i].getValue());
		}
	}		
	
	private synchronized boolean isSuccessfulResponse(String responseStatus) {
		return responseStatus.contains("200") || responseStatus.contains("201") || responseStatus.contains("204");
	}
}
