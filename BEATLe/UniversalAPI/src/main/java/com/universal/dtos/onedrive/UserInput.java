package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class UserInput {

	@JsonProperty("accountEnabled")
	private Boolean accountEnabled;
	@JsonProperty("displayName")
	private String displayName;
	@JsonProperty("mailNickname")
	private String mailNickname;
	@JsonProperty("passwordProfile")
	private PasswordProfile passwordProfile;
	@JsonProperty("userPrincipalName")
	private String userPrincipalName;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The accountEnabled
	 */
	@JsonProperty("accountEnabled")
	public Boolean getAccountEnabled() {
		return accountEnabled;
	}

	/**
	 * 
	 * @param accountEnabled
	 * The accountEnabled
	 */
	@JsonProperty("accountEnabled")
	public void setAccountEnabled(Boolean accountEnabled) {
		this.accountEnabled = accountEnabled;
	}

	/**
	 * 
	 * @return
	 * The displayName
	 */
	@JsonProperty("displayName")
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * @param displayName
	 * The displayName
	 */
	@JsonProperty("displayName")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 
	 * @return
	 * The mailNickname
	 */
	@JsonProperty("mailNickname")
	public String getMailNickname() {
		return mailNickname;
	}

	/**
	 * 
	 * @param mailNickname
	 * The mailNickname
	 */
	@JsonProperty("mailNickname")
	public void setMailNickname(String mailNickname) {
		this.mailNickname = mailNickname;
	}

	/**
	 * 
	 * @return
	 * The passwordProfile
	 */
	@JsonProperty("passwordProfile")
	public PasswordProfile getPasswordProfile() {
		return passwordProfile;
	}

	/**
	 * 
	 * @param passwordProfile
	 * The passwordProfile
	 */
	@JsonProperty("passwordProfile")
	public void setPasswordProfile(PasswordProfile passwordProfile) {
		this.passwordProfile = passwordProfile;
	}

	/**
	 * 
	 * @return
	 * The userPrincipalName
	 */
	@JsonProperty("userPrincipalName")
	public String getUserPrincipalName() {
		return userPrincipalName;
	}

	/**
	 * 
	 * @param userPrincipalName
	 * The userPrincipalName
	 */
	@JsonProperty("userPrincipalName")
	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
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