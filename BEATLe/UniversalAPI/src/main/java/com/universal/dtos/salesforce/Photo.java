package com.universal.dtos.salesforce;

import org.codehaus.jackson.annotate.JsonProperty;

public class Photo {

	@JsonProperty("fullEmailPhotoUrl")
	private String fullEmailPhotoUrl;
	@JsonProperty("largePhotoUrl")
	private String largePhotoUrl;
	@JsonProperty("photoVersionId")
	private String photoVersionId;
	@JsonProperty("smallPhotoUrl")
	private String smallPhotoUrl;
	@JsonProperty("standardEmailPhotoUrl")
	private String standardEmailPhotoUrl;
	@JsonProperty("url")
	private String url;

	/**
	 * 
	 * @return
	 * The fullEmailPhotoUrl
	 */
	@JsonProperty("fullEmailPhotoUrl")
	public String getFullEmailPhotoUrl() {
		return fullEmailPhotoUrl;
	}

	/**
	 * 
	 * @param fullEmailPhotoUrl
	 * The fullEmailPhotoUrl
	 */
	@JsonProperty("fullEmailPhotoUrl")
	public void setFullEmailPhotoUrl(String fullEmailPhotoUrl) {
		this.fullEmailPhotoUrl = fullEmailPhotoUrl;
	}

	/**
	 * 
	 * @return
	 * The largePhotoUrl
	 */
	@JsonProperty("largePhotoUrl")
	public String getLargePhotoUrl() {
		return largePhotoUrl;
	}

	/**
	 * 
	 * @param largePhotoUrl
	 * The largePhotoUrl
	 */
	@JsonProperty("largePhotoUrl")
	public void setLargePhotoUrl(String largePhotoUrl) {
		this.largePhotoUrl = largePhotoUrl;
	}

	/**
	 * 
	 * @return
	 * The photoVersionId
	 */
	@JsonProperty("photoVersionId")
	public String getPhotoVersionId() {
		return photoVersionId;
	}

	/**
	 * 
	 * @param photoVersionId
	 * The photoVersionId
	 */
	@JsonProperty("photoVersionId")
	public void setPhotoVersionId(String photoVersionId) {
		this.photoVersionId = photoVersionId;
	}

	/**
	 * 
	 * @return
	 * The smallPhotoUrl
	 */
	@JsonProperty("smallPhotoUrl")
	public String getSmallPhotoUrl() {
		return smallPhotoUrl;
	}

	/**
	 * 
	 * @param smallPhotoUrl
	 * The smallPhotoUrl
	 */
	@JsonProperty("smallPhotoUrl")
	public void setSmallPhotoUrl(String smallPhotoUrl) {
		this.smallPhotoUrl = smallPhotoUrl;
	}

	/**
	 * 
	 * @return
	 * The standardEmailPhotoUrl
	 */
	@JsonProperty("standardEmailPhotoUrl")
	public String getStandardEmailPhotoUrl() {
		return standardEmailPhotoUrl;
	}

	/**
	 * 
	 * @param standardEmailPhotoUrl
	 * The standardEmailPhotoUrl
	 */
	@JsonProperty("standardEmailPhotoUrl")
	public void setStandardEmailPhotoUrl(String standardEmailPhotoUrl) {
		this.standardEmailPhotoUrl = standardEmailPhotoUrl;
	}

	/**
	 * 
	 * @return
	 * The url
	 */
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 * The url
	 */
	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

}
