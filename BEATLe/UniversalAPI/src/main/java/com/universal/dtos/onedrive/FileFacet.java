package com.universal.dtos.onedrive;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileFacet {
	
	private String mimeType;
	private HashesType hashesType;
	
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public HashesType getHashesType() {
		return hashesType;
	}
	public void setHashesType(HashesType hashesType) {
		this.hashesType = hashesType;
	}
	
}
