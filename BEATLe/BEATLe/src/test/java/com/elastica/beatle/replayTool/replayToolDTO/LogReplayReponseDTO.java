/**
 * 
 */
package com.elastica.beatle.replayTool.replayToolDTO;

import java.util.List;

import org.apache.http.NameValuePair;

/**
 * @author anuvrath
 *
 */
public class LogReplayReponseDTO {

	private List<NameValuePair> responseHeader;	
	private String responseBody;	
	private String responseStatus;
	
	/**
	 * @return the responseHeader
	 */
	public List<NameValuePair> getResponseHeader() {
		return responseHeader;
	}
	/**
	 * @param responseHeader the responseHeader to set
	 */
	public void setResponseHeader(List<NameValuePair> responseHeader) {
		this.responseHeader = responseHeader;
	}
	/**
	 * @return the responseBody
	 */
	public String getResponseBody() {
		return responseBody;
	}
	/**
	 * @param responseBody the responseBody to set
	 */
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	/**
	 * @return the responseStatus
	 */
	public String getResponseStatus() {
		return responseStatus;
	}
	/**
	 * @param responseStatus the responseStatus to set
	 */
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
}
