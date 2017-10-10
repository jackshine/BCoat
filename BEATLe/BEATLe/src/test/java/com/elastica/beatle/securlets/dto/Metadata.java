package com.elastica.beatle.securlets.dto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Metadata {

	@JsonProperty("file_class")
	private String fileClass;
	@JsonProperty("file_format")
	private String fileFormat;
	@JsonProperty("id")
	private String id;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id; 
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
