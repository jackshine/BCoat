package com.universal.dtos.onedrive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class SharingD {

	@JsonProperty("UpdateDocumentSharingInfo")
	private com.universal.dtos.onedrive.UpdateDocumentSharingInfo UpdateDocumentSharingInfo;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The UpdateDocumentSharingInfo
	 */
	@JsonProperty("UpdateDocumentSharingInfo")
	public com.universal.dtos.onedrive.UpdateDocumentSharingInfo getUpdateDocumentSharingInfo() {
		return UpdateDocumentSharingInfo;
	}

	/**
	 * 
	 * @param UpdateDocumentSharingInfo
	 * The UpdateDocumentSharingInfo
	 */
	@JsonProperty("UpdateDocumentSharingInfo")
	public void setUpdateDocumentSharingInfo(com.universal.dtos.onedrive.UpdateDocumentSharingInfo UpdateDocumentSharingInfo) {
		this.UpdateDocumentSharingInfo = UpdateDocumentSharingInfo;
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
