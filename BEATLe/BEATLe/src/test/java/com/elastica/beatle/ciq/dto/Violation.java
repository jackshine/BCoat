package com.elastica.beatle.ciq.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class Violation {

	@JsonProperty("count")
	private Long count;
	@JsonProperty("predefined_term")
	private String predefinedTerm;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The count
	 */
	@JsonProperty("count")
	public Long getCount() {
		return count;
	}

	/**
	 * 
	 * @param count
	 * The count
	 */
	@JsonProperty("count")
	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * 
	 * @return
	 * The predefinedTerm
	 */
	@JsonProperty("predefined_term")
	public String getPredefinedTerm() {
		return predefinedTerm;
	}

	/**
	 * 
	 * @param predefinedTerm
	 * The predefined_term
	 */
	@JsonProperty("predefined_term")
	public void setPredefinedTerm(String predefinedTerm) {
		this.predefinedTerm = predefinedTerm;
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