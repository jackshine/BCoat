package com.universal.dtos.salesforce;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAnyGetter;

public class Bookmarks {

	@JsonProperty("isBookmarkedByCurrentUser")
	private Boolean isBookmarkedByCurrentUser;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The isBookmarkedByCurrentUser
	 */
	@JsonProperty("isBookmarkedByCurrentUser")
	public Boolean getIsBookmarkedByCurrentUser() {
		return isBookmarkedByCurrentUser;
	}

	/**
	 * 
	 * @param isBookmarkedByCurrentUser
	 * The isBookmarkedByCurrentUser
	 */
	@JsonProperty("isBookmarkedByCurrentUser")
	public void setIsBookmarkedByCurrentUser(Boolean isBookmarkedByCurrentUser) {
		this.isBookmarkedByCurrentUser = isBookmarkedByCurrentUser;
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
