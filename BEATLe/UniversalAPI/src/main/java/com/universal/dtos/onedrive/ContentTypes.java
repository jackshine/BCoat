package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class ContentTypes {

	@JsonProperty("__deferred")
	private Deferred Deferred;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The Deferred
	 */
	@JsonProperty("__deferred")
	public Deferred getDeferred() {
		return Deferred;
	}

	/**
	 * 
	 * @param Deferred
	 * The __deferred
	 */
	@JsonProperty("__deferred")
	public void setDeferred(Deferred Deferred) {
		this.Deferred = Deferred;
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