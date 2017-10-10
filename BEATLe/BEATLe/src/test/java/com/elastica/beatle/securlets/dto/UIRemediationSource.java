package com.elastica.beatle.securlets.dto;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class UIRemediationSource {

	@JsonProperty("objects")
	private UIRemediationInnerObject objects;
	@JsonProperty("app")
	private String app;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The objects1
	 */
	@JsonProperty("objects")
	public UIRemediationInnerObject getObjects() {
		return objects;
	}

	/**
	 * 
	 * @param objects1
	 * The objects1
	 */
	@JsonProperty("objects")
	public void setObjects(UIRemediationInnerObject objects) {
		this.objects = objects;
	}

	/**
	 * 
	 * @return
	 * The app
	 */
	@JsonProperty("app")
	public String getApp() {
		return app;
	}

	/**
	 * 
	 * @param app
	 * The app
	 */
	@JsonProperty("app")
	public void setApp(String app) {
		this.app = app;
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
