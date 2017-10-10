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
public class InclusionRule {

	@JsonProperty("groups")
	private List<String> groups = new ArrayList<String>();
	@JsonProperty("users")
	private List<String> users = new ArrayList<String>();
	@JsonProperty("folders")
	private List<String> folders = new ArrayList<String>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The groups
	 */
	@JsonProperty("groups")
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * 
	 * @param groups
	 * The groups
	 */
	@JsonProperty("groups")
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	/**
	 * 
	 * @return
	 * The users
	 */
	@JsonProperty("users")
	public List<String> getUsers() {
		return users;
	}

	/**
	 * 
	 * @param users
	 * The users
	 */
	@JsonProperty("users")
	public void setUsers(List<String> users) {
		this.users = users;
	}

	/**
	 * 
	 * @return
	 * The folders
	 */
	@JsonProperty("folders")
	public List<String> getFolders() {
		return folders;
	}

	/**
	 * 
	 * @param folders
	 * The folders
	 */
	@JsonProperty("folders")
	public void setFolders(List<String> folders) {
		this.folders = folders;
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
