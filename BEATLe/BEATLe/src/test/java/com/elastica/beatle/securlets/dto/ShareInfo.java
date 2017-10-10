package com.elastica.beatle.securlets.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class ShareInfo {
	@JsonProperty("access")
	private String access;
	
	

	@JsonProperty("effective_access")
	private String effectiveAccess;

	
	/**
	 * @return the effectiveAccess
	 */
	public String getEffectiveAccess() {
		return effectiveAccess;
	}

	/**
	 * @param effectiveAccess the effectiveAccess to set
	 */
	public void setEffectiveAccess(String effectiveAccess) {
		this.effectiveAccess = effectiveAccess;
	}

	/**
	 * @return the access
	 */
	public String getAccess() {
		return access;
	}

	/**
	 * @param access the access to set
	 */
	public void setAccess(String access) {
		this.access = access;
	}
	
	
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	@JsonAnyGetter
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}