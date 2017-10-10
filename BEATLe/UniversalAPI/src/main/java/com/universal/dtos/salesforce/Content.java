package com.universal.dtos.salesforce;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import java.util.HashMap;
import java.util.Map;

public class Content {

	@JsonProperty("contentDocumentId")
	private String contentDocumentId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The contentDocumentId
	 */
	@JsonProperty("contentDocumentId")
	public String getContentDocumentId() {
		return contentDocumentId;
	}

	/**
	 * 
	 * @param contentDocumentId
	 * The contentDocumentId
	 */
	@JsonProperty("contentDocumentId")
	public void setContentDocumentId(String contentDocumentId) {
		this.contentDocumentId = contentDocumentId;
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
