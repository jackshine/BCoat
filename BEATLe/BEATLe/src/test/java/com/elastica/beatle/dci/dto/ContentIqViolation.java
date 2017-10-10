package com.elastica.beatle.dci.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ContentIqViolation {

	@JsonProperty("updated_timestamp")
	private String updatedTimestamp;
	@JsonProperty("violations")
	private List<Violation> violations = new ArrayList<Violation>();
	@JsonProperty("key")
	private String key;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The updatedTimestamp
	 */
	@JsonProperty("updated_timestamp")
	public String getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	/**
	 * 
	 * @param updatedTimestamp
	 * The updated_timestamp
	 */
	@JsonProperty("updated_timestamp")
	public void setUpdatedTimestamp(String updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}

	/**
	 * 
	 * @return
	 * The violations
	 */
	@JsonProperty("violations")
	public List<Violation> getViolations() {
		return violations;
	}

	/**
	 * 
	 * @param violations
	 * The violations
	 */
	@JsonProperty("violations")
	public void setViolations(List<Violation> violations) {
		this.violations = violations;
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}