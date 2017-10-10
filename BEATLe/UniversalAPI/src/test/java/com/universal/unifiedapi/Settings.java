package com.universal.unifiedapi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Settings {

	@JsonProperty("xdr_proxy")
	private String xdrProxy;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The xdrProxy
	 */
	@JsonProperty("xdr_proxy")
	public String getXdrProxy() {
		return xdrProxy;
	}

	/**
	 * 
	 * @param xdrProxy
	 * The xdr_proxy
	 */
	@JsonProperty("xdr_proxy")
	public void setXdrProxy(String xdrProxy) {
		this.xdrProxy = xdrProxy;
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
