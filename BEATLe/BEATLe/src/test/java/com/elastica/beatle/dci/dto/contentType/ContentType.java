package com.elastica.beatle.dci.dto.contentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class ContentType {

	@JsonProperty("results")
	private List<Results> results = new ArrayList<Results>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The results
	 */
	@JsonProperty("results")
	public List<Results> getResults() {
		return results;
	}

	/**
	 * 
	 * @param results
	 * The results
	 */
	@JsonProperty("results")
	public void setResults(List<Results> results) {
		this.results = results;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
	
	public int getContentTypeCount(String contentType) {
		int count = 0;
		for ( Results r : getResults()) {
			if(r.getId().equalsIgnoreCase(contentType)) {
				count =  r.getTotal();
			}
		}
		return count;
	}

}
