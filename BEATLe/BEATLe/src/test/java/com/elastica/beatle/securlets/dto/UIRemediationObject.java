package com.elastica.beatle.securlets.dto;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class UIRemediationObject {

	@JsonProperty("source")
	private UIRemediationSource source;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The source1
	 */
	@JsonProperty("source")
	public UIRemediationSource getSource() {
		return source;
	}

	/**
	 * 
	 * @param source1
	 * The source1
	 */
	@JsonProperty("source")
	public void setSource(UIRemediationSource source) {
		this.source = source;
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
