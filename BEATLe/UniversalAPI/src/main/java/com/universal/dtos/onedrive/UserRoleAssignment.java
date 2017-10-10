package com.universal.dtos.onedrive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class UserRoleAssignment {

	@JsonProperty("__metadata")
	private com.universal.dtos.onedrive.Metadata Metadata;
	@JsonProperty("Role")
	private long Role;
	@JsonProperty("UserId")
	private String UserId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The Metadata
	 */
	@JsonProperty("__metadata")
	public com.universal.dtos.onedrive.Metadata getMetadata() {
		return Metadata;
	}

	/**
	 * 
	 * @param Metadata
	 * The __metadata
	 */
	@JsonProperty("__metadata")
	public void setMetadata(com.universal.dtos.onedrive.Metadata Metadata) {
		this.Metadata = Metadata;
	}

	/**
	 * 
	 * @return
	 * The Role
	 */
	@JsonProperty("Role")
	public long getRole() {
		return Role;
	}

	/**
	 * 
	 * @param Role
	 * The Role
	 */
	@JsonProperty("Role")
	public void setRole(long Role) {
		this.Role = Role;
	}

	/**
	 * 
	 * @return
	 * The UserId
	 */
	@JsonProperty("UserId")
	public String getUserId() {
		return UserId;
	}

	/**
	 * 
	 * @param UserId
	 * The UserId
	 */
	@JsonProperty("UserId")
	public void setUserId(String UserId) {
		this.UserId = UserId;
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