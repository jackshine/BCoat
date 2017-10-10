package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class User {

	@JsonProperty("id")
	private String id;
	@JsonProperty("displayName")
	private String displayName;


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
	 * The displayName
	 */
	@JsonProperty("displayName")
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * @param displayName
	 * The displayName
	 */
	@JsonProperty("displayName")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}



}
