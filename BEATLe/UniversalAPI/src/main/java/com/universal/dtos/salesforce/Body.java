package com.universal.dtos.salesforce;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;

public class Body {

	@JsonProperty("messageSegments")
	private List<MessageSegment> messageSegments = new ArrayList<MessageSegment>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The messageSegments
	 */
	@JsonProperty("messageSegments")
	public List<MessageSegment> getMessageSegments() {
		return messageSegments;
	}

	/**
	 * 
	 * @param messageSegments
	 * The messageSegments
	 */
	@JsonProperty("messageSegments")
	public void setMessageSegments(List<MessageSegment> messageSegments) {
		this.messageSegments = messageSegments;
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
