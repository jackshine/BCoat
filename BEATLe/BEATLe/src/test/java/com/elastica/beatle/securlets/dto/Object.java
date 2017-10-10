package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;


public class Object {

	@JsonProperty("activity_count")
	private Integer activityCount;
	@JsonProperty("content_checks")
	private ContentChecks contentChecks;
	@JsonProperty("created_by")
	private String createdBy;
	@JsonProperty("doc_type")
	private String docType;
	@JsonProperty("exposed")
	private Boolean exposed;
	@JsonProperty("exposures")
	private Exposures exposures;
	@JsonProperty("format")
	private String format;
	@JsonProperty("identification")
	private String identification;
	@JsonProperty("is_internal")
	private Boolean isInternal;
	@JsonProperty("name")
	private String name;
	@JsonProperty("object_type")
	private java.lang.Object objectType;
	@JsonProperty("owned_by")
	private String ownedBy;
	@JsonProperty("owner_id")
	private String ownerId;
	@JsonProperty("parent_id")
	private String parentId;
	@JsonProperty("parent_name")
	private String parentName;
	@JsonProperty("path")
	private String path;
	@JsonProperty("scan_state")
	private java.lang.Object scanState;
	@JsonProperty("scan_ts")
	private java.lang.Object scanTs;
	@JsonProperty("share_info")
	private ShareInfo shareInfo;
	@JsonProperty("size")
	private Integer size;
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("avatar_url")
	private String avatarUrl;
	

	@JsonProperty("docs_exposed")
	private int docsExposed;
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("_id")
	private String userObjectId;
	
	@JsonProperty("docs_exposed_non_actionable")
	private int docsExposedNonActionable;

	@JsonProperty("is_pending")
	private Boolean isPending;
	@JsonProperty("groups")
	private List<java.lang.Object> groups = new ArrayList<java.lang.Object>();
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("total")
	private int total;
	
	@JsonProperty("exchange_scan_state")
	private int exchangeScanState;
	

	@JsonProperty("is_synced")
	private boolean isSynced;
	
	@JsonProperty("onedrive_scan_state")
	private int onedriveScanState;
	
	@JsonProperty("subscriptions")
	private int subscriptions;
	
	@JsonProperty("events_state")
	private int eventsState;
	
	/**
	 * @return the docsExposedNonActionable
	 */
	public int getDocsExposedNonActionable() {
		return docsExposedNonActionable;
	}

	/**
	 * @param docsExposedNonActionable the docsExposedNonActionable to set
	 */
	public void setDocsExposedNonActionable(int docsExposedNonActionable) {
		this.docsExposedNonActionable = docsExposedNonActionable;
	}
	
	/**
	 * @return the userObjectId
	 */
	public String getUserObjectId() {
		return userObjectId;
	}

	/**
	 * @param userObjectId the userObjectId to set
	 */
	public void setUserObjectId(String userObjectId) {
		this.userObjectId = userObjectId;
	}
	
	
	
	
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}


	/**
	 *
	 * @return
	 * The activityCount
	 */
	@JsonProperty("activity_count")
	public Integer getActivityCount() {
		return activityCount;
	}

	/**
	 *
	 * @param activityCount
	 * The activity_count
	 */
	@JsonProperty("activity_count")
	public void setActivityCount(Integer activityCount) {
		this.activityCount = activityCount;
	}

	/**
	 *
	 * @return
	 * The contentChecks
	 */
	@JsonProperty("content_checks")
	public ContentChecks getContentChecks() {
		return contentChecks;
	}

	/**
	 *
	 * @param contentChecks
	 * The content_checks
	 */
	@JsonProperty("content_checks")
	public void setContentChecks(ContentChecks contentChecks) {
		this.contentChecks = contentChecks;
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
	 * The docType
	 */
	@JsonProperty("doc_type")
	public String getDocType() {
		return docType;
	}

	/**
	 *
	 * @param docType
	 * The doc_type
	 */
	@JsonProperty("doc_type")
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 *
	 * @return
	 * The exposed
	 */
	@JsonProperty("exposed")
	public Boolean getExposed() {
		return exposed;
	}

	/**
	 *
	 * @param exposed
	 * The exposed
	 */
	@JsonProperty("exposed")
	public void setExposed(Boolean exposed) {
		this.exposed = exposed;
	}

	/**
	 *
	 * @return
	 * The exposures
	 */
	@JsonProperty("exposures")
	public Exposures getExposures() {
		return exposures;
	}

	/**
	 *
	 * @param exposures
	 * The exposures
	 */
	@JsonProperty("exposures")
	public void setExposures(Exposures exposures) {
		this.exposures = exposures;
	}

	/**
	 *
	 * @return
	 * The format
	 */
	@JsonProperty("format")
	public String getFormat() {
		return format;
	}

	/**
	 *
	 * @param format
	 * The format
	 */
	@JsonProperty("format")
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 *
	 * @return
	 * The identification
	 */
	@JsonProperty("identification")
	public String getIdentification() {
		return identification;
	}

	/**
	 *
	 * @param identification
	 * The identification
	 */
	@JsonProperty("identification")
	public void setIdentification(String identification) {
		this.identification = identification;
	}

	/**
	 *
	 * @return
	 * The isInternal
	 */
	@JsonProperty("is_internal")
	public Boolean getIsInternal() {
		return isInternal;
	}

	/**
	 *
	 * @param isInternal
	 * The is_internal
	 */
	@JsonProperty("is_internal")
	public void setIsInternal(Boolean isInternal) {
		this.isInternal = isInternal;
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
	 * The objectType
	 */
	@JsonProperty("object_type")
	public java.lang.Object getObjectType() {
		return objectType;
	}

	/**
	 *
	 * @param objectType
	 * The object_type
	 */
	@JsonProperty("object_type")
	public void setObjectType(java.lang.Object objectType) {
		this.objectType = objectType;
	}

	/**
	 *
	 * @return
	 * The ownedBy
	 */
	@JsonProperty("owned_by")
	public String getOwnedBy() {
		return ownedBy;
	}

	/**
	 *
	 * @param ownedBy
	 * The owned_by
	 */
	@JsonProperty("owned_by")
	public void setOwnedBy(String ownedBy) {
		this.ownedBy = ownedBy;
	}

	/**
	 *
	 * @return
	 * The ownerId
	 */
	@JsonProperty("owner_id")
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 *
	 * @param ownerId
	 * The owner_id
	 */
	@JsonProperty("owner_id")
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 *
	 * @return
	 * The parentId
	 */
	@JsonProperty("parent_id")
	public String getParentId() {
		return parentId;
	}

	/**
	 *
	 * @param parentId
	 * The parent_id
	 */
	@JsonProperty("parent_id")
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 *
	 * @return
	 * The parentName
	 */
	@JsonProperty("parent_name")
	public String getParentName() {
		return parentName;
	}

	/**
	 *
	 * @param parentName
	 * The parent_name
	 */
	@JsonProperty("parent_name")
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 *
	 * @return
	 * The scanState
	 */
	@JsonProperty("scan_state")
	public java.lang.Object getScanState() {
		return scanState;
	}

	/**
	 *
	 * @param scanState
	 * The scan_state
	 */
	@JsonProperty("scan_state")
	public void setScanState(java.lang.Object scanState) {
		this.scanState = scanState;
	}

	/**
	 *
	 * @return
	 * The scanTs
	 */
	@JsonProperty("scan_ts")
	public java.lang.Object getScanTs() {
		return scanTs;
	}

	/**
	 *
	 * @param scanTs
	 * The scan_ts
	 */
	@JsonProperty("scan_ts")
	public void setScanTs(java.lang.Object scanTs) {
		this.scanTs = scanTs;
	}

	/**
	 *
	 * @return
	 * The shareInfo
	 */
	@JsonProperty("share_info")
	public ShareInfo getShareInfo() {
		return shareInfo;
	}

	/**
	 *
	 * @param shareInfo
	 * The share_info
	 */
	@JsonProperty("share_info")
	public void setShareInfo(ShareInfo shareInfo) {
		this.shareInfo = shareInfo;
	}

	/**
	 *
	 * @return
	 * The size
	 */
	@JsonProperty("size")
	public Integer getSize() {
		return size;
	}

	/**
	 *
	 * @param size
	 * The size
	 */
	@JsonProperty("size")
	public void setSize(Integer size) {
		this.size = size;
	}

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
	 * @return the avatarUrl
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * @param avatarUrl the avatarUrl to set
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * @return the docsExposed
	 */
	public int getDocsExposed() {
		return docsExposed;
	}

	/**
	 * @param docsExposed the docsExposed to set
	 */
	public void setDocsExposed(int docsExposed) {
		this.docsExposed = docsExposed;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the isPending
	 */
	public Boolean getIsPending() {
		return isPending;
	}

	/**
	 * @param isPending the isPending to set
	 */
	public void setIsPending(Boolean isPending) {
		this.isPending = isPending;
	}

	/**
	 * @return the groups
	 */
	public List<java.lang.Object> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<java.lang.Object> groups) {
		this.groups = groups;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the exchangeScanState
	 */
	public int getExchangeScanState() {
		return exchangeScanState;
	}

	/**
	 * @param exchangeScanState the exchangeScanState to set
	 */
	public void setExchangeScanState(int exchangeScanState) {
		this.exchangeScanState = exchangeScanState;
	}

	/**
	 * @return the isSynced
	 */
	public boolean getIsSynced() {
		return isSynced;
	}

	/**
	 * @param isSynced the isSynced to set
	 */
	public void setIsSynced(boolean isSynced) {
		this.isSynced = isSynced;
	}

	/**
	 * @return the onedriveScanState
	 */
	public int getOnedriveScanState() {
		return onedriveScanState;
	}

	/**
	 * @param onedriveScanState the onedriveScanState to set
	 */
	public void setOnedriveScanState(int onedriveScanState) {
		this.onedriveScanState = onedriveScanState;
	}

	/**
	 * @return the subscriptions
	 */
	public int getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @param subscriptions the subscriptions to set
	 */
	public void setSubscriptions(int subscriptions) {
		this.subscriptions = subscriptions;
	}

	/**
	 * @return the eventsState
	 */
	public int getEventsState() {
		return eventsState;
	}

	/**
	 * @param eventsState the eventsState to set
	 */
	public void setEventsState(int eventsState) {
		this.eventsState = eventsState;
	}

	

}