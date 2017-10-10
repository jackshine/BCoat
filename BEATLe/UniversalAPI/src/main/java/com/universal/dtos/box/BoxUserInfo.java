package com.universal.dtos.box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BoxUserInfo {

	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("login")
	private String login;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("modified_at")
	private String modifiedAt;
	@JsonProperty("role")
	private String role;
	@JsonProperty("language")
	private String language;
	@JsonProperty("space_amount")
	private Long spaceAmount;
	@JsonProperty("space_used")
	private Long spaceUsed;
	@JsonProperty("max_upload_size")
	private Long maxUploadSize;
	@JsonProperty("tracking_codes")
	private List<Object> trackingCodes = new ArrayList<Object>();
	@JsonProperty("can_see_managed_users")
	private Boolean canSeeManagedUsers;
	@JsonProperty("is_sync_enabled")
	private Boolean isSyncEnabled;
	@JsonProperty("status")
	private String status;
	@JsonProperty("job_title")
	private String jobTitle;
	@JsonProperty("phone")
	private String phone;
	@JsonProperty("address")
	private String address;
	@JsonProperty("avatar_url")
	private String avatarUrl;
	@JsonProperty("is_exempt_from_device_limits")
	private Boolean isExemptFromDeviceLimits;
	@JsonProperty("is_exempt_from_login_verification")
	private Boolean isExemptFromLoginVerification;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return
	 * The type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 *
	 * @param type
	 * The type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

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
	 * The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 * The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 * The login
	 */
	@JsonProperty("login")
	public String getLogin() {
		return login;
	}

	/**
	 *
	 * @param login
	 * The login
	 */
	@JsonProperty("login")
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 *
	 * @return
	 * The createdAt
	 */
	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 *
	 * @param createdAt
	 * The created_at
	 */
	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 *
	 * @return
	 * The modifiedAt
	 */
	@JsonProperty("modified_at")
	public String getModifiedAt() {
		return modifiedAt;
	}

	/**
	 *
	 * @param modifiedAt
	 * The modified_at
	 */
	@JsonProperty("modified_at")
	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	/**
	 *
	 * @return
	 * The role
	 */
	@JsonProperty("role")
	public String getRole() {
		return role;
	}

	/**
	 *
	 * @param role
	 * The role
	 */
	@JsonProperty("role")
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 *
	 * @return
	 * The language
	 */
	@JsonProperty("language")
	public String getLanguage() {
		return language;
	}

	/**
	 *
	 * @param language
	 * The language
	 */
	@JsonProperty("language")
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 *
	 * @return
	 * The spaceAmount
	 */
	@JsonProperty("space_amount")
	public Long getSpaceAmount() {
		return spaceAmount;
	}

	/**
	 *
	 * @param spaceAmount
	 * The space_amount
	 */
	@JsonProperty("space_amount")
	public void setSpaceAmount(Long spaceAmount) {
		this.spaceAmount = spaceAmount;
	}

	/**
	 *
	 * @return
	 * The spaceUsed
	 */
	@JsonProperty("space_used")
	public Long getSpaceUsed() {
		return spaceUsed;
	}

	/**
	 *
	 * @param spaceUsed
	 * The space_used
	 */
	@JsonProperty("space_used")
	public void setSpaceUsed(Long spaceUsed) {
		this.spaceUsed = spaceUsed;
	}

	/**
	 *
	 * @return
	 * The maxUploadSize
	 */
	@JsonProperty("max_upload_size")
	public Long getMaxUploadSize() {
		return maxUploadSize;
	}

	/**
	 *
	 * @param maxUploadSize
	 * The max_upload_size
	 */
	@JsonProperty("max_upload_size")
	public void setMaxUploadSize(Long maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}

	/**
	 *
	 * @return
	 * The trackingCodes
	 */
	@JsonProperty("tracking_codes")
	public List<Object> getTrackingCodes() {
		return trackingCodes;
	}

	/**
	 *
	 * @param trackingCodes
	 * The tracking_codes
	 */
	@JsonProperty("tracking_codes")
	public void setTrackingCodes(List<Object> trackingCodes) {
		this.trackingCodes = trackingCodes;
	}

	/**
	 *
	 * @return
	 * The canSeeManagedUsers
	 */
	@JsonProperty("can_see_managed_users")
	public Boolean getCanSeeManagedUsers() {
		return canSeeManagedUsers;
	}

	/**
	 *
	 * @param canSeeManagedUsers
	 * The can_see_managed_users
	 */
	@JsonProperty("can_see_managed_users")
	public void setCanSeeManagedUsers(Boolean canSeeManagedUsers) {
		this.canSeeManagedUsers = canSeeManagedUsers;
	}

	/**
	 *
	 * @return
	 * The isSyncEnabled
	 */
	@JsonProperty("is_sync_enabled")
	public Boolean getIsSyncEnabled() {
		return isSyncEnabled;
	}

	/**
	 *
	 * @param isSyncEnabled
	 * The is_sync_enabled
	 */
	@JsonProperty("is_sync_enabled")
	public void setIsSyncEnabled(Boolean isSyncEnabled) {
		this.isSyncEnabled = isSyncEnabled;
	}

	/**
	 *
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 *
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 *
	 * @return
	 * The jobTitle
	 */
	@JsonProperty("job_title")
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 *
	 * @param jobTitle
	 * The job_title
	 */
	@JsonProperty("job_title")
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 *
	 * @return
	 * The phone
	 */
	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}

	/**
	 *
	 * @param phone
	 * The phone
	 */
	@JsonProperty("phone")
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 *
	 * @return
	 * The address
	 */
	@JsonProperty("address")
	public String getAddress() {
		return address;
	}

	/**
	 *
	 * @param address
	 * The address
	 */
	@JsonProperty("address")
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 *
	 * @return
	 * The avatarUrl
	 */
	@JsonProperty("avatar_url")
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 *
	 * @param avatarUrl
	 * The avatar_url
	 */
	@JsonProperty("avatar_url")
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 *
	 * @return
	 * The isExemptFromDeviceLimits
	 */
	@JsonProperty("is_exempt_from_device_limits")
	public Boolean getIsExemptFromDeviceLimits() {
		return isExemptFromDeviceLimits;
	}

	/**
	 *
	 * @param isExemptFromDeviceLimits
	 * The is_exempt_from_device_limits
	 */
	@JsonProperty("is_exempt_from_device_limits")
	public void setIsExemptFromDeviceLimits(Boolean isExemptFromDeviceLimits) {
		this.isExemptFromDeviceLimits = isExemptFromDeviceLimits;
	}

	/**
	 *
	 * @return
	 * The isExemptFromLoginVerification
	 */
	@JsonProperty("is_exempt_from_login_verification")
	public Boolean getIsExemptFromLoginVerification() {
		return isExemptFromLoginVerification;
	}

	/**
	 *
	 * @param isExemptFromLoginVerification
	 * The is_exempt_from_login_verification
	 */
	@JsonProperty("is_exempt_from_login_verification")
	public void setIsExemptFromLoginVerification(Boolean isExemptFromLoginVerification) {
		this.isExemptFromLoginVerification = isExemptFromLoginVerification;
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