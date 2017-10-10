package com.elastica.infra.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class Userslist {

	@JsonProperty("is_dpo")
	private Boolean isDpo;
	@JsonProperty("last_name")
	private String lastName;
	@JsonProperty("changed_password")
	private String changedPassword;
	@JsonProperty("is_alerting")
	private Boolean isAlerting;
	@JsonProperty("is_super_admin")
	private Boolean isSuperAdmin;
	@JsonProperty("quarantine_info")
	private Object quarantineInfo;
	@JsonProperty("created_on")
	private String createdOn;
	@JsonProperty("access_profiles")
	private List<String> accessProfiles = new ArrayList<String>();
	@JsonProperty("timezone")
	private Object timezone;
	@JsonProperty("id")
	private String id;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("is_blocked")
	private Boolean isBlocked;
	@JsonProperty("modified_by")
	private String modifiedBy;
	@JsonProperty("title")
	private String title;
	@JsonProperty("work_phone")
	private String workPhone;
	@JsonProperty("created_by")
	private String createdBy;
	@JsonProperty("is_partner")
	private Boolean isPartner;
	@JsonProperty("email")
	private String email;
	@JsonProperty("is_two_factor_auth_enabled")
	private Boolean isTwoFactorAuthEnabled;
	@JsonProperty("is_active")
	private Boolean isActive;
	@JsonProperty("threatscore")
	private Threatscore threatscore;
	@JsonProperty("two_factor_auth_key")
	private String twoFactorAuthKey;
	@JsonProperty("secondary_user_id")
	private String secondaryUserId;
	@JsonProperty("default_selected_range")
	private Object defaultSelectedRange;
	@JsonProperty("blocked_apps")
	private BlockedApps blockedApps;
	@JsonProperty("is_admin")
	private Boolean isAdmin;
	@JsonProperty("groups")
	private List<Object> groups = new ArrayList<Object>();
	@JsonProperty("modified_on")
	private String modifiedOn;
	@JsonProperty("force_logout")
	private Boolean forceLogout;
	@JsonProperty("epagent_id")
	private Object epagentId;
	@JsonProperty("is_dummy")
	private Boolean isDummy;
	@JsonProperty("lock_time")
	private Object lockTime;
	@JsonProperty("is_quarantined")
	private Boolean isQuarantined;
	@JsonProperty("notes")
	private String notes;
	@JsonProperty("risk_status")
	private String riskStatus;
	@JsonProperty("cell_phone")
	private String cellPhone;
	@JsonProperty("resource_uri")
	private String resourceUri;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The isDpo
	 */
	@JsonProperty("is_dpo")
	public Boolean getIsDpo() {
		return isDpo;
	}

	/**
	 * 
	 * @param isDpo
	 * The is_dpo
	 */
	@JsonProperty("is_dpo")
	public void setIsDpo(Boolean isDpo) {
		this.isDpo = isDpo;
	}

	/**
	 * 
	 * @return
	 * The lastName
	 */
	@JsonProperty("last_name")
	public String getLastName() {
		return lastName;
	}

	/**
	 * 
	 * @param lastName
	 * The last_name
	 */
	@JsonProperty("last_name")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * 
	 * @return
	 * The changedPassword
	 */
	@JsonProperty("changed_password")
	public String getChangedPassword() {
		return changedPassword;
	}

	/**
	 * 
	 * @param changedPassword
	 * The changed_password
	 */
	@JsonProperty("changed_password")
	public void setChangedPassword(String changedPassword) {
		this.changedPassword = changedPassword;
	}

	/**
	 * 
	 * @return
	 * The isAlerting
	 */
	@JsonProperty("is_alerting")
	public Boolean getIsAlerting() {
		return isAlerting;
	}

	/**
	 * 
	 * @param isAlerting
	 * The is_alerting
	 */
	@JsonProperty("is_alerting")
	public void setIsAlerting(Boolean isAlerting) {
		this.isAlerting = isAlerting;
	}

	/**
	 * 
	 * @return
	 * The isSuperAdmin
	 */
	@JsonProperty("is_super_admin")
	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	/**
	 * 
	 * @param isSuperAdmin
	 * The is_super_admin
	 */
	@JsonProperty("is_super_admin")
	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	/**
	 * 
	 * @return
	 * The quarantineInfo
	 */
	@JsonProperty("quarantine_info")
	public Object getQuarantineInfo() {
		return quarantineInfo;
	}

	/**
	 * 
	 * @param quarantineInfo
	 * The quarantine_info
	 */
	@JsonProperty("quarantine_info")
	public void setQuarantineInfo(Object quarantineInfo) {
		this.quarantineInfo = quarantineInfo;
	}

	/**
	 * 
	 * @return
	 * The createdOn
	 */
	@JsonProperty("created_on")
	public String getCreatedOn() {
		return createdOn;
	}

	/**
	 * 
	 * @param createdOn
	 * The created_on
	 */
	@JsonProperty("created_on")
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * 
	 * @return
	 * The accessProfiles
	 */
	@JsonProperty("access_profiles")
	public List<String> getAccessProfiles() {
		return accessProfiles;
	}

	/**
	 * 
	 * @param accessProfiles
	 * The access_profiles
	 */
	@JsonProperty("access_profiles")
	public void setAccessProfiles(List<String> accessProfiles) {
		this.accessProfiles = accessProfiles;
	}

	/**
	 * 
	 * @return
	 * The timezone
	 */
	@JsonProperty("timezone")
	public Object getTimezone() {
		return timezone;
	}

	/**
	 * 
	 * @param timezone
	 * The timezone
	 */
	@JsonProperty("timezone")
	public void setTimezone(Object timezone) {
		this.timezone = timezone;
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
	 * The firstName
	 */
	@JsonProperty("first_name")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * @param firstName
	 * The first_name
	 */
	@JsonProperty("first_name")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * 
	 * @return
	 * The isBlocked
	 */
	@JsonProperty("is_blocked")
	public Boolean getIsBlocked() {
		return isBlocked;
	}

	/**
	 * 
	 * @param isBlocked
	 * The is_blocked
	 */
	@JsonProperty("is_blocked")
	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	/**
	 * 
	 * @return
	 * The modifiedBy
	 */
	@JsonProperty("modified_by")
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * 
	 * @param modifiedBy
	 * The modified_by
	 */
	@JsonProperty("modified_by")
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * 
	 * @return
	 * The title
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 * The title
	 */
	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return
	 * The workPhone
	 */
	@JsonProperty("work_phone")
	public String getWorkPhone() {
		return workPhone;
	}

	/**
	 * 
	 * @param workPhone
	 * The work_phone
	 */
	@JsonProperty("work_phone")
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	/**
	 * 
	 * @return
	 * The createdBy
	 */
	@JsonProperty("created_by")
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * 
	 * @param createdBy
	 * The created_by
	 */
	@JsonProperty("created_by")
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * 
	 * @return
	 * The isPartner
	 */
	@JsonProperty("is_partner")
	public Boolean getIsPartner() {
		return isPartner;
	}

	/**
	 * 
	 * @param isPartner
	 * The is_partner
	 */
	@JsonProperty("is_partner")
	public void setIsPartner(Boolean isPartner) {
		this.isPartner = isPartner;
	}

	/**
	 * 
	 * @return
	 * The email
	 */
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 * The email
	 */
	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return
	 * The isTwoFactorAuthEnabled
	 */
	@JsonProperty("is_two_factor_auth_enabled")
	public Boolean getIsTwoFactorAuthEnabled() {
		return isTwoFactorAuthEnabled;
	}

	/**
	 * 
	 * @param isTwoFactorAuthEnabled
	 * The is_two_factor_auth_enabled
	 */
	@JsonProperty("is_two_factor_auth_enabled")
	public void setIsTwoFactorAuthEnabled(Boolean isTwoFactorAuthEnabled) {
		this.isTwoFactorAuthEnabled = isTwoFactorAuthEnabled;
	}

	/**
	 * 
	 * @return
	 * The isActive
	 */
	@JsonProperty("is_active")
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * 
	 * @param isActive
	 * The is_active
	 */
	@JsonProperty("is_active")
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * 
	 * @return
	 * The threatscore
	 */
	@JsonProperty("threatscore")
	public Threatscore getThreatscore() {
		return threatscore;
	}

	/**
	 * 
	 * @param threatscore
	 * The threatscore
	 */
	@JsonProperty("threatscore")
	public void setThreatscore(Threatscore threatscore) {
		this.threatscore = threatscore;
	}

	/**
	 * 
	 * @return
	 * The twoFactorAuthKey
	 */
	@JsonProperty("two_factor_auth_key")
	public String getTwoFactorAuthKey() {
		return twoFactorAuthKey;
	}

	/**
	 * 
	 * @param twoFactorAuthKey
	 * The two_factor_auth_key
	 */
	@JsonProperty("two_factor_auth_key")
	public void setTwoFactorAuthKey(String twoFactorAuthKey) {
		this.twoFactorAuthKey = twoFactorAuthKey;
	}

	/**
	 * 
	 * @return
	 * The secondaryUserId
	 */
	@JsonProperty("secondary_user_id")
	public String getSecondaryUserId() {
		return secondaryUserId;
	}

	/**
	 * 
	 * @param secondaryUserId
	 * The secondary_user_id
	 */
	@JsonProperty("secondary_user_id")
	public void setSecondaryUserId(String secondaryUserId) {
		this.secondaryUserId = secondaryUserId;
	}

	/**
	 * 
	 * @return
	 * The defaultSelectedRange
	 */
	@JsonProperty("default_selected_range")
	public Object getDefaultSelectedRange() {
		return defaultSelectedRange;
	}

	/**
	 * 
	 * @param defaultSelectedRange
	 * The default_selected_range
	 */
	@JsonProperty("default_selected_range")
	public void setDefaultSelectedRange(Object defaultSelectedRange) {
		this.defaultSelectedRange = defaultSelectedRange;
	}

	/**
	 * 
	 * @return
	 * The blockedApps
	 */
	@JsonProperty("blocked_apps")
	public BlockedApps getBlockedApps() {
		return blockedApps;
	}

	/**
	 * 
	 * @param blockedApps
	 * The blocked_apps
	 */
	@JsonProperty("blocked_apps")
	public void setBlockedApps(BlockedApps blockedApps) {
		this.blockedApps = blockedApps;
	}

	/**
	 * 
	 * @return
	 * The isAdmin
	 */
	@JsonProperty("is_admin")
	public Boolean getIsAdmin() {
		return isAdmin;
	}

	/**
	 * 
	 * @param isAdmin
	 * The is_admin
	 */
	@JsonProperty("is_admin")
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * 
	 * @return
	 * The groups
	 */
	@JsonProperty("groups")
	public List<Object> getGroups() {
		return groups;
	}

	/**
	 * 
	 * @param groups
	 * The groups
	 */
	@JsonProperty("groups")
	public void setGroups(List<Object> groups) {
		this.groups = groups;
	}

	/**
	 * 
	 * @return
	 * The modifiedOn
	 */
	@JsonProperty("modified_on")
	public String getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * 
	 * @param modifiedOn
	 * The modified_on
	 */
	@JsonProperty("modified_on")
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * 
	 * @return
	 * The forceLogout
	 */
	@JsonProperty("force_logout")
	public Boolean getForceLogout() {
		return forceLogout;
	}

	/**
	 * 
	 * @param forceLogout
	 * The force_logout
	 */
	@JsonProperty("force_logout")
	public void setForceLogout(Boolean forceLogout) {
		this.forceLogout = forceLogout;
	}

	/**
	 * 
	 * @return
	 * The epagentId
	 */
	@JsonProperty("epagent_id")
	public Object getEpagentId() {
		return epagentId;
	}

	/**
	 * 
	 * @param epagentId
	 * The epagent_id
	 */
	@JsonProperty("epagent_id")
	public void setEpagentId(Object epagentId) {
		this.epagentId = epagentId;
	}

	/**
	 * 
	 * @return
	 * The isDummy
	 */
	@JsonProperty("is_dummy")
	public Boolean getIsDummy() {
		return isDummy;
	}

	/**
	 * 
	 * @param isDummy
	 * The is_dummy
	 */
	@JsonProperty("is_dummy")
	public void setIsDummy(Boolean isDummy) {
		this.isDummy = isDummy;
	}

	/**
	 * 
	 * @return
	 * The lockTime
	 */
	@JsonProperty("lock_time")
	public Object getLockTime() {
		return lockTime;
	}

	/**
	 * 
	 * @param lockTime
	 * The lock_time
	 */
	@JsonProperty("lock_time")
	public void setLockTime(Object lockTime) {
		this.lockTime = lockTime;
	}

	/**
	 * 
	 * @return
	 * The isQuarantined
	 */
	@JsonProperty("is_quarantined")
	public Boolean getIsQuarantined() {
		return isQuarantined;
	}

	/**
	 * 
	 * @param isQuarantined
	 * The is_quarantined
	 */
	@JsonProperty("is_quarantined")
	public void setIsQuarantined(Boolean isQuarantined) {
		this.isQuarantined = isQuarantined;
	}

	/**
	 * 
	 * @return
	 * The notes
	 */
	@JsonProperty("notes")
	public String getNotes() {
		return notes;
	}

	/**
	 * 
	 * @param notes
	 * The notes
	 */
	@JsonProperty("notes")
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * 
	 * @return
	 * The riskStatus
	 */
	@JsonProperty("risk_status")
	public String getRiskStatus() {
		return riskStatus;
	}

	/**
	 * 
	 * @param riskStatus
	 * The risk_status
	 */
	@JsonProperty("risk_status")
	public void setRiskStatus(String riskStatus) {
		this.riskStatus = riskStatus;
	}

	/**
	 * 
	 * @return
	 * The cellPhone
	 */
	@JsonProperty("cell_phone")
	public String getCellPhone() {
		return cellPhone;
	}

	/**
	 * 
	 * @param cellPhone
	 * The cell_phone
	 */
	@JsonProperty("cell_phone")
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	/**
	 * 
	 * @return
	 * The resourceUri
	 */
	@JsonProperty("resource_uri")
	public String getResourceUri() {
		return resourceUri;
	}

	/**
	 * 
	 * @param resourceUri
	 * The resource_uri
	 */
	@JsonProperty("resource_uri")
	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
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
