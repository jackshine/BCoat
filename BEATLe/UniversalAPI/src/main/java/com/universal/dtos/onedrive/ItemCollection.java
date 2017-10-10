package com.universal.dtos.onedrive;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonIgnoreProperties(ignoreUnknown = true)


public class ItemCollection {

	@JsonProperty("odata.metadata")
	private String odataMetadata;
	@JsonProperty("odata.nextLink")
	private String odataNextLink;
	@JsonProperty("value")
	private List<ListItemValue> value = new ArrayList<ListItemValue>();
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
	 * The odataNextLink
	 */
	@JsonProperty("odata.nextLink")
	public String getOdataNextLink() {
		return odataNextLink;
	}

	/**
	 * 
	 * @param odataNextLink
	 * The odata.nextLink
	 */
	@JsonProperty("odata.nextLink")
	public void setOdataNextLink(String odataNextLink) {
		this.odataNextLink = odataNextLink;
	}

	/**
	 * 
	 * @return
	 * The value
	 */
	@JsonProperty("value")
	public List<ListItemValue> getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 * The value
	 */
	@JsonProperty("value")
	public void setValue(List<ListItemValue> value) {
		this.value = value;
	}

	
}
