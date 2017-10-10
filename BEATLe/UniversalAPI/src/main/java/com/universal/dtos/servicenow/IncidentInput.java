package com.universal.dtos.servicenow;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class IncidentInput {
	
	@JsonProperty("comments")
	private String comments;
	@JsonProperty("short_description")
	private String shortDescription;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The comments
	 */
	@JsonProperty("comments")
	public String getComments() {
		return comments;
	}

	/**
	 * 
	 * @param comments
	 * The comments
	 */
	@JsonProperty("comments")
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * 
	 * @return
	 * The shortDescription
	 */
	@JsonProperty("short_description")
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * 
	 * @param shortDescription
	 * The short_description
	 */
	@JsonProperty("short_description")
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
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
