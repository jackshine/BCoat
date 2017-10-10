package com.elastica.beatle.dci.dto.ciq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Operand {

	@JsonProperty("name")
	private String name;
	@JsonProperty("weight")
	private Double weight;
	@JsonProperty("is_not")
	private Boolean isNot;
	@JsonProperty("value")
	private List<String> value = new ArrayList<String>();
	@JsonProperty("source")
	private String source;
	@JsonProperty("min_count")
	private Integer minCount;
	@JsonProperty("is_high_sensitivity")
	private Boolean isHighSensitivity;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return
	 * The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 * The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 * The weight
	 */
	@JsonProperty("weight")
	public Double getWeight() {
		return weight;
	}

	/**
	 * 
	 * @param weight
	 * The weight
	 */
	@JsonProperty("weight")
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	/**
	 * 
	 * @return
	 * The isNot
	 */
	@JsonProperty("is_not")
	public Boolean getIsNot() {
		return isNot;
	}

	/**
	 * 
	 * @param isNot
	 * The is_not
	 */
	@JsonProperty("is_not")
	public void setIsNot(Boolean isNot) {
		this.isNot = isNot;
	}

	/**
	 * 
	 * @return
	 * The value
	 */
	@JsonProperty("value")
	public List<String> getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 * The value
	 */
	@JsonProperty("value")
	public void setValue(List<String> value) {
		this.value = value;
	}

	/**
	 * 
	 * @return
	 * The source
	 */
	@JsonProperty("source")
	public String getSource() {
		return source;
	}

	/**
	 * 
	 * @param source
	 * The source
	 */
	@JsonProperty("source")
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 
	 * @return
	 * The minCount
	 */
	@JsonProperty("min_count")
	public Integer getMinCount() {
		return minCount;
	}

	/**
	 * 
	 * @param minCount
	 * The min_count
	 */
	@JsonProperty("min_count")
	public void setMinCount(Integer minCount) {
		this.minCount = minCount;
	}

	/**
	 * 
	 * @return
	 * The isHighSensitivity
	 */
	@JsonProperty("is_high_sensitivity")
	public Boolean getIsHighSensitivity() {
		return isHighSensitivity;
	}

	/**
	 * 
	 * @param isHighSensitivity
	 * The is_high_sensitivity
	 */
	@JsonProperty("is_high_sensitivity")
	public void setIsHighSensitivity(Boolean isHighSensitivity) {
		this.isHighSensitivity = isHighSensitivity;
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