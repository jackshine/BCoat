package com.universal.dtos.onedrive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class SiteFolderInput {
	@JsonProperty("ServerRelativeUrl")
	private String serverRelativeUrl;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The serverRelativeUrl
	 */
	@JsonProperty("ServerRelativeUrl")
	public String getServerRelativeUrl() {
		return serverRelativeUrl;
	}

	/**
	 * 
	 * @param serverRelativeUrl
	 * The ServerRelativeUrl
	 */
	@JsonProperty("ServerRelativeUrl")
	public void setServerRelativeUrl(String serverRelativeUrl) {
		this.serverRelativeUrl = serverRelativeUrl;
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
