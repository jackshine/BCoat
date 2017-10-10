package com.universal.dtos.salesforce;


import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class APIVersion {


	@JsonProperty("label")
	private String label;
	@JsonProperty("url")
	private String url;
	@JsonProperty("version")
	private String version;
	
	private ArrayList<APIVersion> versions;

	/**
	 * @return the versions
	 */
	public ArrayList<APIVersion> getVersions() {
		return versions;
	}

	/**
	 * @param versions the versions to set
	 */
	public void setVersions(ArrayList<APIVersion> versions) {
		this.versions = versions;
	}

	/**
	 * 
	 * @return
	 * The label
	 */
	@JsonProperty("label")
	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * @param label
	 * The label
	 */
	@JsonProperty("label")
	public void setLabel(String label) {
		this.label = label;
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

	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("version")
	public String getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
	}

}