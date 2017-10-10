package com.elastica.beatle.dci.dto.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
public class Notification {

	@JsonProperty("exception")
	private String exception;
	@JsonProperty("objects")
	private List<com.elastica.beatle.dci.dto.notification.Object> objects = new ArrayList<com.elastica.beatle.dci.dto.notification.Object>();
	@JsonProperty("meta")
	private Meta meta;
	@JsonProperty("__error")
	private String error;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return
	 * The exception
	 */
	@JsonProperty("exception")
	public String getException() {
		return exception;
	}

	/**
	 * 
	 * @param exception
	 * The exception
	 */
	@JsonProperty("exception")
	public void setException(String exception) {
		this.exception = exception;
	}

	/**
	 * 
	 * @return
	 * The objects
	 */
	@JsonProperty("objects")
	public List<com.elastica.beatle.dci.dto.notification.Object> getObjects() {
		return objects;
	}

	/**
	 * 
	 * @param objects
	 * The objects
	 */
	@JsonProperty("objects")
	public void setObjects(List<com.elastica.beatle.dci.dto.notification.Object> objects) {
		this.objects = objects;
	}

	/**
	 * 
	 * @return
	 * The meta
	 */
	@JsonProperty("meta")
	public Meta getMeta() {
		return meta;
	}

	/**
	 * 
	 * @param meta
	 * The meta
	 */
	@JsonProperty("meta")
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	/**
	 * 
	 * @return
	 * The error
	 */
	@JsonProperty("__error")
	public String getError() {
		return error;
	}

	/**
	 * 
	 * @param error
	 * The __error
	 */
	@JsonProperty("__error")
	public void setError(String error) {
		this.error = error;
	}

	@JsonAnyGetter
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}