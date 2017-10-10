package com.universal.dtos.salesforce;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class FileShares {

	@JsonProperty("currentPageUrl")
	private String currentPageUrl;
	@JsonProperty("linkShare")
	private Object linkShare;
	@JsonProperty("nextPageUrl")
	private Object nextPageUrl;
	@JsonProperty("previousPageUrl")
	private Object previousPageUrl;
	@JsonProperty("shares")
	private List<Share> shares = new ArrayList<Share>();
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
	 * The linkShare
	 */
	@JsonProperty("linkShare")
	public Object getLinkShare() {
		return linkShare;
	}

	/**
	 * 
	 * @param linkShare
	 * The linkShare
	 */
	@JsonProperty("linkShare")
	public void setLinkShare(Object linkShare) {
		this.linkShare = linkShare;
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
	@JsonProperty("shares")
	public List<Share> getShares() {
		return shares;
	}

	/**
	 * 
	 * @param shares
	 * The shares
	 */
	@JsonProperty("shares")
	public void setShares(List<Share> shares) {
		this.shares = shares;
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

