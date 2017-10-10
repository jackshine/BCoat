package com.universal.dtos.onedrive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class DUser {

	@JsonProperty("results")
	private List<UserResult> results = new ArrayList<UserResult>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The results
	 */
	@JsonProperty("results")
	public List<UserResult> getResults() {
		return results;
	}

	/**
	 * 
	 * @param results
	 * The results
	 */
	@JsonProperty("results")
	public void setResults(List<UserResult> results) {
		this.results = results;
	}

}