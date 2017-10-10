package com.elastica.beatle.dci.dto.ciq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Group {

	@JsonProperty("operator")
	private String operator;
	@JsonProperty("operands")
	private List<Operand> operands = new ArrayList<Operand>();
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return
	 * The operator
	 */
	@JsonProperty("operator")
	public String getOperator() {
		return operator;
	}

	/**
	 * 
	 * @param operator
	 * The operator
	 */
	@JsonProperty("operator")
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 
	 * @return
	 * The operands
	 */
	@JsonProperty("operands")
	public List<Operand> getOperands() {
		return operands;
	}

	/**
	 * 
	 * @param operands
	 * The operands
	 */
	@JsonProperty("operands")
	public void setOperands(List<Operand> operands) {
		this.operands = operands;
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

