package com.universal.dtos.onedrive;

import org.codehaus.jackson.annotate.JsonProperty;

public class ParentReference {

	@JsonProperty("driveId")
	private String driveId;
	@JsonProperty("id")
	private String id;
	@JsonProperty("path")
	private String path;
	

	/**
	 * 
	 * @return
	 * The driveId
	 */
	@JsonProperty("driveId")
	public String getDriveId() {
		return driveId;
	}

	/**
	 * 
	 * @param driveId
	 * The driveId
	 */
	@JsonProperty("driveId")
	public void setDriveId(String driveId) {
		this.driveId = driveId;
	}

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
	 * The path
	 */
	@JsonProperty("path")
	public String getPath() {
		return path;
	}

	/**
	 * 
	 * @param path
	 * The path
	 */
	@JsonProperty("path")
	public void setPath(String path) {
		this.path = path;
	}

	

}