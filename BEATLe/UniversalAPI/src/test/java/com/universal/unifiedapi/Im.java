package com.universal.unifiedapi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Im {

	@JsonProperty("provider")
	private String provider;
	@JsonProperty("username")
	private String username;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The provider
	 */
	@JsonProperty("provider")
	public String getProvider() {
		return provider;
	}

	/**
	 * 
	 * @param provider
	 * The provider
	 */
	@JsonProperty("provider")
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * 
	 * @return
	 * The username
	 */
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param username
	 * The username
	 */
	@JsonProperty("username")
	public void setUsername(String username) {
		this.username = username;
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
