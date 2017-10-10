package com.elastica.beatle.dci.dto.tp;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class TrainingProfile {

	@JsonProperty("status")
	private Integer status;
	@JsonProperty("data")
	private Data data;
	@JsonProperty("ok")
	private Boolean ok;
	@JsonProperty("__error")
	private String Error;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

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
	public Data getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 * The data
	 */
	@JsonProperty("data")
	public void setData(Data data) {
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
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}