package com.universal.dtos.onedrive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class SiteFolderUpdateInput {

	@JsonProperty("__metadata")
	private Metadata metadata;
	@JsonProperty("FileLeafRef")
	private String fileLeafRef;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The metadata
	 */
	@JsonProperty("__metadata")
	public Metadata getMetadata() {
		return metadata;
	}

	/**
	 * 
	 * @param metadata
	 * The __metadata
	 */
	@JsonProperty("__metadata")
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	/**
	 * 
	 * @return
	 * The fileLeafRef
	 */
	@JsonProperty("FileLeafRef")
	public String getFileLeafRef() {
		return fileLeafRef;
	}

	/**
	 * 
	 * @param fileLeafRef
	 * The FileLeafRef
	 */
	@JsonProperty("FileLeafRef")
	public void setFileLeafRef(String fileLeafRef) {
		this.fileLeafRef = fileLeafRef;
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