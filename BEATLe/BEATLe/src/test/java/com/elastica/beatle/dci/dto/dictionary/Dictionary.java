
package com.elastica.beatle.dci.dto.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Dictionary {

	@JsonProperty("api_response")
	private String apiResponse;
	@JsonProperty("objects")
	private List<com.elastica.beatle.dci.dto.dictionary.Object> objects = new ArrayList<com.elastica.beatle.dci.dto.dictionary.Object>();
	@JsonProperty("meta")
	private Meta meta;
	@JsonProperty("action_status")
	private String actionStatus;
	@JsonProperty("key")
	private String key;
	@JsonProperty("exceptions")
	private String exceptions;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return
	 * The apiResponse
	 */
	@JsonProperty("api_response")
	public String getApiResponse() {
		return apiResponse;
	}

	/**
	 * 
	 * @param apiResponse
	 * The api_response
	 */
	@JsonProperty("api_response")
	public void setApiResponse(String apiResponse) {
		this.apiResponse = apiResponse;
	}

	/**
	 * 
	 * @return
	 * The objects
	 */
	@JsonProperty("objects")
	public List<com.elastica.beatle.dci.dto.dictionary.Object> getObjects() {
		return objects;
	}

	/**
	 * 
	 * @param objects
	 * The objects
	 */
	@JsonProperty("objects")
	public void setObjects(List<com.elastica.beatle.dci.dto.dictionary.Object> objects) {
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
	 * The actionStatus
	 */
	@JsonProperty("action_status")
	public String getActionStatus() {
		return actionStatus;
	}

	/**
	 * 
	 * @param actionStatus
	 * The action_status
	 */
	@JsonProperty("action_status")
	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	/**
	 * 
	 * @return
	 * The key
	 */
	@JsonProperty("key")
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @param key
	 * The key
	 */
	@JsonProperty("key")
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 
	 * @return
	 * The exceptions
	 */
	@JsonProperty("exceptions")
	public String getExceptions() {
		return exceptions;
	}

	/**
	 * 
	 * @param exceptions
	 * The exceptions
	 */
	@JsonProperty("exceptions")
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
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

