package com.elastica.beatle.dci.dto.fileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class FileType {

	@JsonProperty("results")
	private List<Result> results = new ArrayList<Result>();
	@JsonProperty("api_response")
	private String apiResponse;
	@JsonProperty("meta")
	private Object meta;
	@JsonProperty("action_status")
	private String actionStatus;
	@JsonProperty("key")
	private String key;
	@JsonProperty("exceptions")
	private String exceptions;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The results
	 */
	@JsonProperty("results")
	public List<Result> getResults() {
		return results;
	}

	/**
	 * 
	 * @param results
	 * The results
	 */
	@JsonProperty("results")
	public void setResults(List<Result> results) {
		this.results = results;
	}

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
	 * The meta
	 */
	@JsonProperty("meta")
	public Object getMeta() {
		return meta;
	}

	/**
	 * 
	 * @param meta
	 * The meta
	 */
	@JsonProperty("meta")
	public void setMeta(Object meta) {
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
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
	
	public int getFileTypeCount(String fileType) {
		int count = 0;
		for ( Result r : getResults()) {
			if(r.getTerm().equalsIgnoreCase(fileType)) {
				count =  r.getTotal();
			}
		}
		return count;
	}

}

