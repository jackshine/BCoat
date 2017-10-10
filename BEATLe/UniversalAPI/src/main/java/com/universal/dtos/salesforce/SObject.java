package com.universal.dtos.salesforce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;



public class SObject {

	@JsonProperty("id")
	private String id;
	@JsonProperty("success")
	private Boolean success;
	@JsonProperty("ContractNumber")
	private String contractNumber;
	@JsonProperty("errors")
	private List<Object> errors = new ArrayList<Object>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	
	
	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The success
	 */
	@JsonProperty("success")
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * 
	 * @param success
	 * The success
	 */
	@JsonProperty("success")
	public void setSuccess(Boolean success) {
		this.success = success;
	}


	/**
	 * @return the contractNumber
	 */
	public String getContractNumber() {
		return contractNumber;
	}

	/**
	 * @param contractNumber the contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}


	
	/**
	 * 
	 * @return
	 * The errors
	 */
	@JsonProperty("errors")
	public List<Object> getErrors() {
		return errors;
	}

	/**
	 * 
	 * @param errors
	 * The errors
	 */
	@JsonProperty("errors")
	public void setErrors(List<Object> errors) {
		this.errors = errors;
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
