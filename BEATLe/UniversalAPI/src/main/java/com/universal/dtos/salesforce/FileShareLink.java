package com.universal.dtos.salesforce;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class FileShareLink {

	@JsonProperty("fileViewUrl")
	private String fileViewUrl;
	@JsonProperty("sharingType")
	private String sharingType;

	/**
	 * 
	 * @return
	 * The fileViewUrl
	 */
	@JsonProperty("fileViewUrl")
	public String getFileViewUrl() {
		return fileViewUrl;
	}

	/**
	 * 
	 * @param fileViewUrl
	 * The fileViewUrl
	 */
	@JsonProperty("fileViewUrl")
	public void setFileViewUrl(String fileViewUrl) {
		this.fileViewUrl = fileViewUrl;
	}

	/**
	 * 
	 * @return
	 * The sharingType
	 */
	@JsonProperty("sharingType")
	public String getSharingType() {
		return sharingType;
	}

	/**
	 * 
	 * @param sharingType
	 * The sharingType
	 */
	@JsonProperty("sharingType")
	public void setSharingType(String sharingType) {
		this.sharingType = sharingType;
	}

}
