package com.universal.dtos.salesforce;

import org.codehaus.jackson.annotate.JsonProperty;

public class Shares {

	@JsonProperty("id")
	private String id;
	@JsonProperty("sharingType")
	private String sharingType;

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
	 * The sharingType
	 */
	@JsonProperty("sharingType")
	public String getSharingType() {
		return sharingType;
	}

	/**
	 * 
	 * @param sharingType
	 * The sharingType
	 */
	@JsonProperty("sharingType")
	public void setSharingType(String sharingType) {
		this.sharingType = sharingType;
	}

}
