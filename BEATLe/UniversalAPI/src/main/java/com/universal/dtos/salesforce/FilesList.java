package com.universal.dtos.salesforce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class FilesList {
	@JsonProperty("currentPageUrl")
	private String currentPageUrl;
	@JsonProperty("nextPageUrl")
	private Object nextPageUrl;
	@JsonProperty("previousPageUrl")
	private Object previousPageUrl;
	@JsonProperty("files")
	private List<ChatterFile> files = new ArrayList<ChatterFile>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The currentPageUrl
	 */
	@JsonProperty("currentPageUrl")
	public String getCurrentPageUrl() {
		return currentPageUrl;
	}

	/**
	 * 
	 * @param currentPageUrl
	 * The currentPageUrl
	 */
	@JsonProperty("currentPageUrl")
	public void setCurrentPageUrl(String currentPageUrl) {
		this.currentPageUrl = currentPageUrl;
	}

	
	/**
	 * 
	 * @return
	 * The nextPageUrl
	 */
	@JsonProperty("nextPageUrl")
	public Object getNextPageUrl() {
		return nextPageUrl;
	}

	/**
	 * 
	 * @param nextPageUrl
	 * The nextPageUrl
	 */
	@JsonProperty("nextPageUrl")
	public void setNextPageUrl(Object nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}

	/**
	 * 
	 * @return
	 * The previousPageUrl
	 */
	@JsonProperty("previousPageUrl")
	public Object getPreviousPageUrl() {
		return previousPageUrl;
	}

	/**
	 * 
	 * @param previousPageUrl
	 * The previousPageUrl
	 */
	@JsonProperty("previousPageUrl")
	public void setPreviousPageUrl(Object previousPageUrl) {
		this.previousPageUrl = previousPageUrl;
	}

	/**
	 * 
	 * @return
	 * The shares
	 */
	@JsonProperty("files")
	public List<ChatterFile> getFiles() {
		return files;
	}

	/**
	 * 
	 * @param shares
	 * The shares
	 */
	@JsonProperty("files")
	public void setFiles(List<ChatterFile> files) {
		this.files = files;
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
