package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Link {

	@JsonProperty("type")
	private String type;
	@JsonProperty("webUrl")
	private String webUrl;
	@JsonProperty("application")
	private Application application;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return
	 * The type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 *
	 * @param type
	 * The type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	/**
	 *
	 * @return
	 * The webUrl
	 */
	@JsonProperty("webUrl")
	public String getWebUrl() {
		return webUrl;
	}

	/**
	 *
	 * @param webUrl
	 * The webUrl
	 */
	@JsonProperty("webUrl")
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	/**
	 *
	 * @return
	 * The application
	 */
	@JsonProperty("application")
	public Application getApplication() {
		return application;
	}

	/**
	 *
	 * @param application
	 * The application
	 */
	@JsonProperty("application")
	public void setApplication(Application application) {
		this.application = application;
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