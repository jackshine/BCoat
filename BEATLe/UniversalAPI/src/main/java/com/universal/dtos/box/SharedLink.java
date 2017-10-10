package com.universal.dtos.box;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SharedLink {

	@JsonProperty("url")
	private String url;
	@JsonProperty("download_url")
	private String downloadUrl;
	@JsonProperty("vanity_url")
	private Object vanityUrl;
	@JsonProperty("effective_access")
	private String effectiveAccess;
	@JsonProperty("is_password_enabled")
	private Boolean isPasswordEnabled;
	@JsonProperty("unshared_at")
	private Object unsharedAt;
	@JsonProperty("download_count")
	private Long downloadCount;
	@JsonProperty("preview_count")
	private Long previewCount;
	@JsonProperty("access")
	private String access;
	@JsonProperty("permissions")
	private Permissions permissions;
	

	/**
	 *
	 * @return
	 * The url
	 */
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	/**
	 *
	 * @param url
	 * The url
	 */
	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 *
	 * @return
	 * The downloadUrl
	 */
	@JsonProperty("download_url")
	public String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 *
	 * @param downloadUrl
	 * The download_url
	 */
	@JsonProperty("download_url")
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	/**
	 *
	 * @return
	 * The vanityUrl
	 */
	@JsonProperty("vanity_url")
	public Object getVanityUrl() {
		return vanityUrl;
	}

	/**
	 *
	 * @param vanityUrl
	 * The vanity_url
	 */
	@JsonProperty("vanity_url")
	public void setVanityUrl(Object vanityUrl) {
		this.vanityUrl = vanityUrl;
	}

	/**
	 *
	 * @return
	 * The effectiveAccess
	 */
	@JsonProperty("effective_access")
	public String getEffectiveAccess() {
		return effectiveAccess;
	}

	/**
	 *
	 * @param effectiveAccess
	 * The effective_access
	 */
	@JsonProperty("effective_access")
	public void setEffectiveAccess(String effectiveAccess) {
		this.effectiveAccess = effectiveAccess;
	}

	/**
	 *
	 * @return
	 * The isPasswordEnabled
	 */
	@JsonProperty("is_password_enabled")
	public Boolean getIsPasswordEnabled() {
		return isPasswordEnabled;
	}

	/**
	 *
	 * @param isPasswordEnabled
	 * The is_password_enabled
	 */
	@JsonProperty("is_password_enabled")
	public void setIsPasswordEnabled(Boolean isPasswordEnabled) {
		this.isPasswordEnabled = isPasswordEnabled;
	}

	/**
	 *
	 * @return
	 * The unsharedAt
	 */
	@JsonProperty("unshared_at")
	public Object getUnsharedAt() {
		return unsharedAt;
	}

	/**
	 *
	 * @param unsharedAt
	 * The unshared_at
	 */
	@JsonProperty("unshared_at")
	public void setUnsharedAt(Object unsharedAt) {
		this.unsharedAt = unsharedAt;
	}

	/**
	 *
	 * @return
	 * The downloadCount
	 */
	@JsonProperty("download_count")
	public Long getDownloadCount() {
		return downloadCount;
	}

	/**
	 *
	 * @param downloadCount
	 * The download_count
	 */
	@JsonProperty("download_count")
	public void setDownloadCount(Long downloadCount) {
		this.downloadCount = downloadCount;
	}

	/**
	 *
	 * @return
	 * The previewCount
	 */
	@JsonProperty("preview_count")
	public Long getPreviewCount() {
		return previewCount;
	}

	/**
	 *
	 * @param previewCount
	 * The preview_count
	 */
	@JsonProperty("preview_count")
	public void setPreviewCount(Long previewCount) {
		this.previewCount = previewCount;
	}

	/**
	 *
	 * @return
	 * The access
	 */
	@JsonProperty("access")
	public String getAccess() {
		return access;
	}

	/**
	 *
	 * @param access
	 * The access
	 */
	@JsonProperty("access")
	public void setAccess(String access) {
		this.access = access;
	}

	/**
	 *
	 * @return
	 * The permissions
	 */
	@JsonProperty("permissions")
	public Permissions getPermissions() {
		return permissions;
	}

	/**
	 *
	 * @param permissions
	 * The permissions
	 */
	@JsonProperty("permissions")
	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}

	
}