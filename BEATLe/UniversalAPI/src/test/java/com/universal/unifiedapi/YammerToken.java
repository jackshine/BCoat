package com.universal.unifiedapi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class YammerToken {

	@JsonProperty("access_token")
	private AccessToken accessToken;
	@JsonProperty("user")
	private User user;
	@JsonProperty("network")
	private Network network;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The accessToken
	 */
	@JsonProperty("access_token")
	public AccessToken getAccessToken() {
		return accessToken;
	}

	/**
	 * 
	 * @param accessToken
	 * The access_token
	 */
	@JsonProperty("access_token")
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * 
	 * @return
	 * The user
	 */
	@JsonProperty("user")
	public User getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 * The user
	 */
	@JsonProperty("user")
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 
	 * @return
	 * The network
	 */
	@JsonProperty("network")
	public Network getNetwork() {
		return network;
	}

	/**
	 * 
	 * @param network
	 * The network
	 */
	@JsonProperty("network")
	public void setNetwork(Network network) {
		this.network = network;
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
