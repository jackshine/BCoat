package com.universal.dtos.onedrive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;




public class UserList {

	@JsonProperty("odata.metadata")
	private String odataMetadata;
	@JsonProperty("value")
	private List<Value> value = new ArrayList<Value>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The odataMetadata
	 */
	@JsonProperty("odata.metadata")
	public String getOdataMetadata() {
		return odataMetadata;
	}

	/**
	 * 
	 * @param odataMetadata
	 * The odata.metadata
	 */
	@JsonProperty("odata.metadata")
	public void setOdataMetadata(String odataMetadata) {
		this.odataMetadata = odataMetadata;
	}

	/**
	 * 
	 * @return
	 * The value
	 */
	@JsonProperty("value")
	public List<Value> getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 * The value
	 */
	@JsonProperty("value")
	public void setValue(List<Value> value) {
		this.value = value;
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