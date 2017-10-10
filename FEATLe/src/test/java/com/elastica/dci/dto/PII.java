package com.elastica.dci.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"expressions"
})
public class PII {

	@JsonProperty("expressions")
	private List<Expression> expressions = new ArrayList<Expression>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

