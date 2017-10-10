package com.elastica.beatle.dci.dto.tp;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class TrainingProfileDetailed {

	@JsonProperty("status")
	private Integer status;
	@JsonProperty("data")
	private DataDetailed data;
	@JsonProperty("ok")
	private Boolean ok;
	@JsonProperty("__error")
	private String Error;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public Integer getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 
	 * @return
	 * The data
	 */
	@JsonProperty("data")
	public DataDetailed getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 * The data
	 */
	@JsonProperty("data")
	public void setData(DataDetailed data) {
		this.data = data;
	}

	/**
	 * 
	 * @return
	 * The ok
	 */
	@JsonProperty("ok")
	public Boolean getOk() {
		return ok;
	}

	/**
	 * 
	 * @param ok
	 * The ok
	 */
	@JsonProperty("ok")
	public void setOk(Boolean ok) {
		this.ok = ok;
	}

	/**
	 * 
	 * @return
	 * The Error
	 */
	@JsonProperty("__error")
	public String getError() {
		return Error;
	}

	/**
	 * 
	 * @param Error
	 * The __error
	 */
	@JsonProperty("__error")
	public void setError(String Error) {
		this.Error = Error;
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