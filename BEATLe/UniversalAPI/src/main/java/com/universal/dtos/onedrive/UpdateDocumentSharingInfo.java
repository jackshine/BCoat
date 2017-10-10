package com.universal.dtos.onedrive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class UpdateDocumentSharingInfo {

	@JsonProperty("__metadata")
	private com.universal.dtos.onedrive.Metadata Metadata;
	@JsonProperty("results")
	private List<SharingResult> results = new ArrayList<SharingResult>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The Metadata
	 */
	@JsonProperty("__metadata")
	public com.universal.dtos.onedrive.Metadata getMetadata() {
		return Metadata;
	}

	/**
	 * 
	 * @param Metadata
	 * The __metadata
	 */
	@JsonProperty("__metadata")
	public void setMetadata(com.universal.dtos.onedrive.Metadata Metadata) {
		this.Metadata = Metadata;
	}

	/**
	 * 
	 * @return
	 * The results
	 */
	@JsonProperty("results")
	public List<SharingResult> getResults() {
		return results;
	}

	/**
	 * 
	 * @param results
	 * The results
	 */
	@JsonProperty("results")
	public void setResults(List<SharingResult> results) {
		this.results = results;
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