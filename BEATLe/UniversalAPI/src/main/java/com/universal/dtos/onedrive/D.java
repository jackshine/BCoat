package com.universal.dtos.onedrive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class D {

	@JsonProperty("results")
	private List<Result> results = new ArrayList<Result>();
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

}