package com.universal.dtos.box;

import org.codehaus.jackson.annotate.JsonProperty;

public class Permissions {

	@JsonProperty("can_download")
	private Boolean canDownload;
	@JsonProperty("can_preview")
	private Boolean canPreview;
	

	/**
	 *
	 * @return
	 * The canDownload
	 */
	@JsonProperty("can_download")
	public Boolean getCanDownload() {
		return canDownload;
	}

	/**
	 *
	 * @param canDownload
	 * The can_download
	 */
	@JsonProperty("can_download")
	public void setCanDownload(Boolean canDownload) {
		this.canDownload = canDownload;
	}

	/**
	 *
	 * @return
	 * The canPreview
	 */
	@JsonProperty("can_preview")
	public Boolean getCanPreview() {
		return canPreview;
	}

	/**
	 *
	 * @param canPreview
	 * The can_preview
	 */
	@JsonProperty("can_preview")
	public void setCanPreview(Boolean canPreview) {
		this.canPreview = canPreview;
	}

	
}