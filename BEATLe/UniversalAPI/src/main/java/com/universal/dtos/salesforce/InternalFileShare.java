package com.universal.dtos.salesforce;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class InternalFileShare {
	//can be V or C
	@JsonProperty("ShareType")
	private String ShareType;
	@JsonProperty("ContentDocumentId")
	private String ContentDocumentId;
	@JsonProperty("LinkedEntityId")
	private String LinkedEntityId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	
	
	public InternalFileShare(String shareType, String fileId, String instanceId) {
		this.ShareType = shareType;
		this.ContentDocumentId = fileId;
		this.LinkedEntityId = instanceId;
	}
	
	/**
	 * 
	 * @return
	 * The ShareType
	 */
	@JsonProperty("ShareType")
	public String getShareType() {
		return ShareType;
	}

	/**
	 * 
	 * @param ShareType
	 * The ShareType
	 */
	@JsonProperty("ShareType")
	public void setShareType(String ShareType) {
		this.ShareType = ShareType;
	}

	/**
	 * 
	 * @return
	 * The ContentDocumentId
	 */
	@JsonProperty("ContentDocumentId")
	public String getContentDocumentId() {
		return ContentDocumentId;
	}

	/**
	 * 
	 * @param ContentDocumentId
	 * The ContentDocumentId
	 */
	@JsonProperty("ContentDocumentId")
	public void setContentDocumentId(String ContentDocumentId) {
		this.ContentDocumentId = ContentDocumentId;
	}

	/**
	 * 
	 * @return
	 * The LinkedEntityId
	 */
	@JsonProperty("LinkedEntityId")
	public String getLinkedEntityId() {
		return LinkedEntityId;
	}

	/**
	 * 
	 * @param LinkedEntityId
	 * The LinkedEntityId
	 */
	@JsonProperty("LinkedEntityId")
	public void setLinkedEntityId(String LinkedEntityId) {
		this.LinkedEntityId = LinkedEntityId;
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
