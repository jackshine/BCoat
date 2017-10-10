package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Order {

	@JsonProperty("by")
	private String by;
	@JsonProperty("direction")
	private String direction;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The by
	 */
	@JsonProperty("by")
	public String getBy() {
		return by;
	}

	/**
	 * 
	 * @param by
	 * The by
	 */
	@JsonProperty("by")
	public void setBy(String by) {
		this.by = by;
	}

	/**
	 * 
	 * @return
	 * The direction
	 */
	@JsonProperty("direction")
	public String getDirection() {
		return direction;
	}

	/**
	 * 
	 * @param direction
	 * The direction
	 */
	@JsonProperty("direction")
	public void setDirection(String direction) {
		this.direction = direction;
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