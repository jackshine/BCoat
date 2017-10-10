package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class MembershipInput {

	@JsonProperty("user")
	private BoxUserInfo user;
	@JsonProperty("group")
	private BoxGroup group;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return
	 * The user
	 */
	@JsonProperty("user")
	public BoxUserInfo getUser() {
		return user;
	}

	/**
	 *
	 * @param user
	 * The user
	 */
	@JsonProperty("user")
	public void setUser(BoxUserInfo user) {
		this.user = user;
	}

	/**
	 *
	 * @return
	 * The group
	 */
	@JsonProperty("group")
	public BoxGroup getGroup() {
		return group;
	}

	/**
	 *
	 * @param group
	 * The group
	 */
	@JsonProperty("group")
	public void setGroup(BoxGroup group) {
		this.group = group;
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