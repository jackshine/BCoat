package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UpdateList {

	@JsonProperty("__metadata")
	private com.universal.dtos.onedrive.Metadata Metadata;
	@JsonProperty("Title")
	private String Title;
	
	
	
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
	 * The Title
	 */
	@JsonProperty("Title")
	public String getTitle() {
		return Title;
	}

	/**
	 * 
	 * @param Title
	 * The Title
	 */
	@JsonProperty("Title")
	public void setTitle(String Title) {
		this.Title = Title;
	}
	
	
	
}
