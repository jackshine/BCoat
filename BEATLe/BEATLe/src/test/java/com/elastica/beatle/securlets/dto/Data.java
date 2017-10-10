package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Data {

	@JsonProperty("api_scan_policy")
	private ApiScanPolicy apiScanPolicy;
	@JsonProperty("instance")
	private String instance;
	@JsonProperty("app_name")
	private String appName;
	
	@JsonProperty("securlet_state")
	private String securletState;
	
	
	

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	/**
	 * 
	 * @return
	 * The apiScanPolicy
	 */
	@JsonProperty("api_scan_policy")
	public ApiScanPolicy getApiScanPolicy() {
		return apiScanPolicy;
	}

	/**
	 * 
	 * @param apiScanPolicy
	 * The api_scan_policy
	 */
	@JsonProperty("api_scan_policy")
	public void setApiScanPolicy(ApiScanPolicy apiScanPolicy) {
		this.apiScanPolicy = apiScanPolicy;
	}

	/**
	 * 
	 * @return
	 * The instance
	 */
	@JsonProperty("instance")
	public String getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param instance
	 * The instance
	 */
	@JsonProperty("instance")
	public void setInstance(String instance) {
		this.instance = instance;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	/**
	 * @return the securletState
	 */
	public String getSecurletState() {
		return securletState;
	}

	/**
	 * @param securletState the securletState to set
	 */
	public void setSecurletState(String securletState) {
		this.securletState = securletState;
	}

}

