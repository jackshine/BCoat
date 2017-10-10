package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class O365MetaInfo {

	@JsonProperty("access")
	private String access;
	@JsonProperty("current_link")
	private String currentLink;
	@JsonProperty("expires_on")
	private String expireOn;

	@JsonProperty("role")
	private String role;

	@JsonProperty("collabs")
	private List<String> collabs = new ArrayList<String>();


	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The access
	 */
	@JsonProperty("access")
	public String getAccess() {
		return access;
	}

	/**
	 * 
	 * @param access
	 * The access
	 */
	@JsonProperty("access")
	public void setAccess(String access) {
		this.access = access;
	}

	/**
	 * 
	 * @return
	 * The currentLink
	 */
	@JsonProperty("current_link")
	public String getCurrentLink() {
		return currentLink;
	}

	/**
	 * 
	 * @param currentLink
	 * The current_link
	 */
	@JsonProperty("current_link")
	public void setCurrentLink(String currentLink) {
		this.currentLink = currentLink;
	}


	/**
	 * @return the expireOn
	 */
	@JsonProperty("expires_on")
	public String getExpireOn() {
		return expireOn;
	}

	/**
	 * @param expireOn the expireOn to set
	 */
	@JsonProperty("expires_on")
	public void setExpireOn(String expireOn) {
		this.expireOn = expireOn;
	}

	/**
	 *
	 * @return
	 * The role
	 */
	@JsonProperty("role")
	public String getRole() {
		return role;
	}

	/**
	 *
	 * @param role
	 * The role
	 */
	@JsonProperty("role")
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 *
	 * @return
	 * The collabs
	 */
	@JsonProperty("collabs")
	public List<String> getCollabs() {
		return collabs;
	}

	/**
	 *
	 * @param collabs
	 * The collabs
	 */
	@JsonProperty("collabs")
	public void setCollabs(List<String> collabs) {
		this.collabs = collabs;
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