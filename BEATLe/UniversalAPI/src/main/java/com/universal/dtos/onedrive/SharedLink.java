package com.universal.dtos.onedrive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class SharedLink {

	@JsonProperty("id")
	private String id;
	@JsonProperty("roles")
	private List<String> roles = new ArrayList<String>();
	@JsonProperty("link")
	private Link link;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
	 * The roles
	 */
	@JsonProperty("roles")
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * 
	 * @param roles
	 * The roles
	 */
	@JsonProperty("roles")
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	/**
	 * 
	 * @return
	 * The link
	 */
	@JsonProperty("link")
	public Link getLink() {
		return link;
	}

	/**
	 * 
	 * @param link
	 * The link
	 */
	@JsonProperty("link")
	public void setLink(Link link) {
		this.link = link;
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