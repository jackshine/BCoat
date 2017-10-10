package com.elastica.beatle.dci.splunk.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Pii {

	@JsonProperty("updated_timestamp")
	private String updatedTimestamp;
	@JsonProperty("expressions")
	private List<Expression> expressions = new ArrayList<Expression>();
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
	 * The expressions
	 */
	@JsonProperty("expressions")
	public List<Expression> getExpressions() {
		return expressions;
	}

	/**
	 * 
	 * @param expressions
	 * The expressions
	 */
	@JsonProperty("expressions")
	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
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