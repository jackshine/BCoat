package com.universal.dtos.jive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ContentInput {

	@JsonProperty("content")
	private Content content;
	@JsonProperty("subject")
	private String subject;
	@JsonProperty("type")
	private String type;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The content
	 */
	@JsonProperty("content")
	public Content getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 * The content
	 */
	@JsonProperty("content")
	public void setContent(Content content) {
		this.content = content;
	}

	/**
	 * 
	 * @return
	 * The subject
	 */
	@JsonProperty("subject")
	public String getSubject() {
		return subject;
	}

	/**
	 * 
	 * @param subject
	 * The subject
	 */
	@JsonProperty("subject")
	public void setSubject(String subject) {
		this.subject = subject;
	}

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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
