/**
 * 
 */
package com.elastica.beatle.replayTool.replayToolDTO;

import java.net.URI;
import java.util.List;

import org.apache.http.NameValuePair;

/**
 * @author anuvrath
 *
 */
public class LogReplayRequestDTO {

	private String methodType;
	private URI requestURI;
	private String httpRequestVersion;
	private List<NameValuePair> requestHeader;
	private String requestBody;		
	
	/**
	 * @return the methodType
	 */
	public String getMethodType() {
		return methodType;
	}
	/**
	 * @param methodType the methodType to set
	 */
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	/**
	 * @return the requestURI
	 */
	public URI getRequestURI() {
		return requestURI;
	}
	/**
	 * @param requestURI the requestURI to set
	 */
	public void setRequestURI(URI requestURI) {
		this.requestURI = requestURI;
	}
	/**
	 * @return the httpRequestVersion
	 */
	public String getHttpRequestVersion() {
		return httpRequestVersion;
	}
	/**
	 * @param httpRequestVersion the httpRequestVersion to set
	 */
	public void setHttpRequestVersion(String httpRequestVersion) {
		this.httpRequestVersion = httpRequestVersion;
	}
	/**
	 * @return the requestHeader
	 */
	public List<NameValuePair> getRequestHeader() {
		return requestHeader;
	}
	/**
	 * @param requestHeader the requestHeader to set
	 */
	public void setRequestHeader(List<NameValuePair> requestHeader) {
		this.requestHeader = requestHeader;
	}
	
	/**
	 * @return the requestBody
	 */
	public String getRequestBody() {
		return requestBody;
	}
	/**
	 * @param requestBody the requestBody to set
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
}