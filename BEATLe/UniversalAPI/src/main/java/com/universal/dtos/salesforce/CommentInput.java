package com.universal.dtos.salesforce;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import java.util.HashMap;
import java.util.Map;

public class CommentInput {

	@JsonProperty("body")
	private Body body;
	@JsonProperty("capabilities")
	private Capabilities capabilities;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return
	 * The body
	 */
	@JsonProperty("body")
	public Body getBody() {
		return body;
	}

	/**
	 *
	 * @param body
	 * The body
	 */
	@JsonProperty("body")
	public void setBody(Body body) {
		this.body = body;
	}

	/**
	 *
	 * @return
	 * The capabilities
	 */
	@JsonProperty("capabilities")
	public Capabilities getCapabilities() {
		return capabilities;
	}

	/**
	 *
	 * @param capabilities
	 * The capabilities
	 */
	@JsonProperty("capabilities")
	public void setCapabilities(Capabilities capabilities) {
		this.capabilities = capabilities;
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
