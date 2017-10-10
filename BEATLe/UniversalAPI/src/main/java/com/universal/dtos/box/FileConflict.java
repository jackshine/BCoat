package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;


@JsonPropertyOrder({
	"type",
	"status",
	"code",
	"context_info",
	"help_url",
	"message",
	"request_id"
})
public class FileConflict {

	@JsonProperty("type")
	private String type;
	@JsonProperty("status")
	private Long status;
	@JsonProperty("code")
	private String code;
	@JsonProperty("context_info")
	private ContextInfo contextInfo;
	@JsonProperty("help_url")
	private String helpUrl;
	@JsonProperty("message")
	private String message;
	@JsonProperty("request_id")
	private String requestId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 * The type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public Long getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(Long status) {
		this.status = status;
	}

	/**
	 * 
	 * @return
	 * The code
	 */
	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * @param code
	 * The code
	 */
	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 
	 * @return
	 * The contextInfo
	 */
	@JsonProperty("context_info")
	public ContextInfo getContextInfo() {
		return contextInfo;
	}

	/**
	 * 
	 * @param contextInfo
	 * The context_info
	 */
	@JsonProperty("context_info")
	public void setContextInfo(ContextInfo contextInfo) {
		this.contextInfo = contextInfo;
	}

	/**
	 * 
	 * @return
	 * The helpUrl
	 */
	@JsonProperty("help_url")
	public String getHelpUrl() {
		return helpUrl;
	}

	/**
	 * 
	 * @param helpUrl
	 * The help_url
	 */
	@JsonProperty("help_url")
	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}

	/**
	 * 
	 * @return
	 * The message
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 * The message
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return
	 * The requestId
	 */
	@JsonProperty("request_id")
	public String getRequestId() {
		return requestId;
	}

	/**
	 * 
	 * @param requestId
	 * The request_id
	 */
	@JsonProperty("request_id")
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}