package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class PasswordProfile {

	@JsonProperty("password")
	private String password;
	@JsonProperty("forceChangePasswordNextLogin")
	private Boolean forceChangePasswordNextLogin;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The password
	 */
	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password
	 * The password
	 */
	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return
	 * The forceChangePasswordNextLogin
	 */
	@JsonProperty("forceChangePasswordNextLogin")
	public Boolean getForceChangePasswordNextLogin() {
		return forceChangePasswordNextLogin;
	}

	/**
	 * 
	 * @param forceChangePasswordNextLogin
	 * The forceChangePasswordNextLogin
	 */
	@JsonProperty("forceChangePasswordNextLogin")
	public void setForceChangePasswordNextLogin(Boolean forceChangePasswordNextLogin) {
		this.forceChangePasswordNextLogin = forceChangePasswordNextLogin;
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