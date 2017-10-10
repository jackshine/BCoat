package com.elastica.beatle.dci.splunk.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class DCISplunkResults {

	@JsonProperty("content_checks")
	private ContentChecks contentChecks;
	@JsonProperty("file_id")
	private String fileId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The contentChecks
	 */
	@JsonProperty("content_checks")
	public ContentChecks getContentChecks() {
		return contentChecks;
	}

	/**
	 * 
	 * @param contentChecks
	 * The content_checks
	 */
	@JsonProperty("content_checks")
	public void setContentChecks(ContentChecks contentChecks) {
		this.contentChecks = contentChecks;
	}

	/**
	 * 
	 * @return
	 * The fileId
	 */
	@JsonProperty("file_id")
	public String getFileId() {
		return fileId;
	}

	/**
	 * 
	 * @param fileId
	 * The file_id
	 */
	@JsonProperty("file_id")
	public void setFileId(String fileId) {
		this.fileId = fileId;
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
