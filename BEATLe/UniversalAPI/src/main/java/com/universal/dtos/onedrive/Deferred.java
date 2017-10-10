package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Deferred {

	@JsonProperty("uri")
	private String uri;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The uri
	 */
	@JsonProperty("uri")
	public String getUri() {
		return uri;
	}

	/**
	 * 
	 * @param uri
	 * The uri
	 */
	@JsonProperty("uri")
	public void setUri(String uri) {
		this.uri = uri;
	}
}