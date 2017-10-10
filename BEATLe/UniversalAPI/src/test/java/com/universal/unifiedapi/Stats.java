package com.universal.unifiedapi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Stats {

	@JsonProperty("following")
	private Integer following;
	@JsonProperty("followers")
	private Integer followers;
	@JsonProperty("updates")
	private Integer updates;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The following
	 */
	@JsonProperty("following")
	public Integer getFollowing() {
		return following;
	}

	/**
	 * 
	 * @param following
	 * The following
	 */
	@JsonProperty("following")
	public void setFollowing(Integer following) {
		this.following = following;
	}

	/**
	 * 
	 * @return
	 * The followers
	 */
	@JsonProperty("followers")
	public Integer getFollowers() {
		return followers;
	}

	/**
	 * 
	 * @param followers
	 * The followers
	 */
	@JsonProperty("followers")
	public void setFollowers(Integer followers) {
		this.followers = followers;
	}

	/**
	 * 
	 * @return
	 * The updates
	 */
	@JsonProperty("updates")
	public Integer getUpdates() {
		return updates;
	}

	/**
	 * 
	 * @param updates
	 * The updates
	 */
	@JsonProperty("updates")
	public void setUpdates(Integer updates) {
		this.updates = updates;
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
