
package com.elastica.beatle.dci.dto.ciq.create;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class CreateCIQProfile {

	@JsonProperty("key")
	private String key;
	@JsonProperty("action_status")
	private boolean actionStatus;
	@JsonProperty("ciq")
	private List<String> ciq;
	@JsonProperty("api_message")
	private String apiMessage;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	

	/**
	 * @return the key
	 */
	@JsonProperty("key")
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	@JsonProperty("key")
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the action_status
	 */
	@JsonProperty("action_status")
	public boolean getActionStatus() {
		return actionStatus;
	}

	/**
	 * @param action_status the action_status to set
	 */
	@JsonProperty("action_status")
	public void setActionStatus(boolean actionStatus) {
		this.actionStatus = actionStatus;
	}

	/**
	 * @return the ciq
	 */
	@JsonProperty("ciq")
	public List<String> getCiq() {
		return ciq;
	}

	/**
	 * @param ciq the ciq to set
	 */
	@JsonProperty("ciq")
	public void setCiq(List<String> ciq) {
		this.ciq = ciq;
	}

	/**
	 * @return the api_message
	 */
	@JsonProperty("api_message")
	public String getApiMessage() {
		return apiMessage;
	}

	/**
	 * @param api_message the api_message to set
	 */
	@JsonProperty("api_message")
	public void setApiMessage(String apiMessage) {
		this.apiMessage = apiMessage;
	}

	/**
	 * @param additionalProperties the additionalProperties to set
	 */
	public void setAdditionalProperties(Map<String, java.lang.Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@JsonAnyGetter
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}
