package com.elastica.beatle.dci.splunk.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class Metadata {

	@JsonProperty("content-type")
	private List<String> contentType = new ArrayList<String>();
	@JsonProperty("file_class")
	private String fileClass;
	@JsonProperty("file_format")
	private String fileFormat;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The contentType
	 */
	@JsonProperty("content-type")
	public List<String> getContentType() {
		return contentType;
	}

	/**
	 * 
	 * @param contentType
	 * The content-type
	 */
	@JsonProperty("content-type")
	public void setContentType(List<String> contentType) {
		this.contentType = contentType;
	}

	/**
	 * 
	 * @return
	 * The fileClass
	 */
	@JsonProperty("file_class")
	public String getFileClass() {
		return fileClass;
	}

	/**
	 * 
	 * @param fileClass
	 * The file_class
	 */
	@JsonProperty("file_class")
	public void setFileClass(String fileClass) {
		this.fileClass = fileClass;
	}

	/**
	 * 
	 * @return
	 * The fileFormat
	 */
	@JsonProperty("file_format")
	public String getFileFormat() {
		return fileFormat;
	}

	/**
	 * 
	 * @param fileFormat
	 * The file_format
	 */
	@JsonProperty("file_format")
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
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