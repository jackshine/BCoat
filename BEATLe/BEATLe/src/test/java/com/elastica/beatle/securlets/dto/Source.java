package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.elastica.beatle.ciq.dto.ContentChecks;
import com.elastica.beatle.ciq.dto.Pii;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Source {

	@JsonProperty("activity_count")
	private String activityCount;
	@JsonProperty("external_recipients")
	private String externalRecipients;
	@JsonProperty("External_recipient_count")
	private String externalRecipientsCount;
	@JsonProperty("Internal_recipient_count")
	private int internalReciptCount;
	

	//Added this field as gmail returns with capitalized. No fix available for case insensitive mapping
	@JsonProperty("External_recipients")
	private String External_Recipients;
	
	@JsonProperty("attachments")
	private String attachments;
	
	@JsonProperty("internal_recipients")
	private String internalRecipients;
	
	@JsonProperty("Internal_recipients")
	private String Internal_recipients;
	
	@JsonProperty("In_Folder")
	private String InFolder;
	@JsonProperty("subject")
	private String subject;
	

	@JsonProperty("version")
	private String version;
	@JsonProperty("user-agent")
	private String userAgent;
	@JsonProperty("req_uri")
	private String reqUri;
	@JsonProperty("req_size")
	private String reqSize;
	@JsonProperty("resp_size")
	private String respSize;
	@JsonProperty("resp_code")
	private String respCode;
	@JsonProperty("web_path")
	private String webPath;
	@JsonProperty("region")
	private String region;
	@JsonProperty("is_anonymous_proxy")
	private String isAnonymousProxy;
	@JsonProperty("referer_uri")
	private String refererUri;
	@JsonProperty("elastica_user")
	private String elasticaUser;
	@JsonProperty("transit_hosts")
	private String transitHosts;
	@JsonProperty("time_zone")
	private String timeZone;
	@JsonProperty("facility")
	private String facility;
	@JsonProperty("File_Size")
	private Integer FileSize;
	
	@JsonProperty("file_size")
	private long filesize;
	
	
	@JsonProperty("created_timestamp")
	private String createdTimestamp;
	@JsonProperty("previous_path")
	private String previous_path;
	@JsonProperty("current_path")
	private String currentPath;
	
	@JsonProperty("Previous_Path")
	private String Previous_Path;
	
	@JsonProperty("Current_Path")
	private String Current_Path;
	
	@JsonProperty("message")
	private String message;
	@JsonProperty("Activity_type")
	private String ActivityType;
	@JsonProperty("city")
	private String city;
	@JsonProperty("severity")
	private String severity;
	@JsonProperty("Object_type")
	private String ObjectType;
	@JsonProperty("location")
	private String location;
	@JsonProperty("latitude")
	private String latitude;
	@JsonProperty("user_name")
	private String userName;
	@JsonProperty("parent")
	private String parent;
	@JsonProperty("Risks")
	private String Risks;
	@JsonProperty("Parent")
	private String OneDriveParent;
	@JsonProperty("Resource_Id")
	private String ResourceId;
	@JsonProperty("host")
	private String host;
	@JsonProperty("user")
	private String user;
	@JsonProperty("inserted_timestamp")
	private String insertedTimestamp;
	@JsonProperty("name")
	private String name;
	@JsonProperty("_ObjectName")
	private String ObjectName;
	@JsonProperty("country")
	private String country;
	@JsonProperty("longitude")
	private String longitude;
	@JsonProperty("__source")
	private String Source;
	
	@JsonProperty("_domain")
	private String domain;
	@JsonProperty("_latency")
	private String latency;
	
	@JsonProperty("updated_timestamp")
	private String updatedTimestamp;
	
	@JsonProperty("parent_id")
	private String parentId;
	
	@JsonProperty("sender")
	private String sender;
	
	@JsonProperty("event_type")
	private String eventType;
	
	@JsonProperty("details")
	private String details;
	
	@JsonProperty("device")
	private String device;
	
	@JsonProperty("browser")
	private String browser;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("language")
	private String language;
	
	@JsonProperty("max_upload_size")
	private String max_upload_size;
	
	@JsonProperty("space_used")
	private String space_used;
	
	@JsonProperty("space_amount")
	private String space_amount;
	
	@JsonProperty("job_title")
	private String job_title;
	
	@JsonProperty("phone")
	private String phone;
	
	@JsonProperty("file_type")
	private String file_type;
	
	@JsonProperty("file_name")
	private String file_name;
	
	@JsonProperty("file_extension")
	private String file_extension;
	
	@JsonProperty("OldValue")
	private String OldValue;
	
	@JsonProperty("NewValue")
	private String NewValue;
	
	@JsonProperty("instance")
	private String instance;
	
	@JsonProperty("object_url")
	private String object_url;
	
	@JsonProperty("Object Id")
	private String objectId;
	
	@JsonProperty("Object Name")
	private String object_Name;
	
	@JsonProperty("object_name")
	private String object_name;
	
	
	@JsonProperty("user_type")
	private String userType;
	
	@JsonProperty("owned_by_name")
	private String owned_by_name;
	
	@JsonProperty("owned_by")
	private String owned_by;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("timezone")
	private String timezone;
	
	@JsonProperty("Status")
	private String Status;
	
	@JsonProperty("LoginType")
	private String LoginType;
	
	@JsonProperty("ClientVersion")
	private String ClientVersion;
	
	@JsonProperty("LoginUrl")
	private String LoginUrl;
	
	
	@JsonProperty("LoginTime")
	private String LoginTime;
	
	@JsonProperty("Application")
	private String Application;
	
	@JsonProperty("ApiType")
	private String ApiType;
	
	@JsonProperty("_PolicyViolated")
	private String _PolicyViolated;
	
	@JsonProperty("policy_action")
	private String policy_action;
	
	@JsonProperty("policy_type")
	private String policy_type;
	
	@JsonProperty("verified")
	private int verified;
	
	@JsonProperty("UserId")
	private String UserId;
	
	@JsonProperty("target_account_type")
	private String targetAccountType;
	
	@JsonProperty("account_type")
	private String accountType;
	
	@JsonProperty("org_unit")
	private String orgUnit;
	
	@JsonProperty("external_recipient_count")
	private int externalRecipientCount;
	
	@JsonProperty("internal_recipient_count")
	private int internalRecipientCount;
	
	@JsonProperty("ResultStatus")
	private String resultStatus;
	
	//Salesforce old and new value
	@JsonProperty("Old Value")
	private String sfOldValue;
	
	@JsonProperty("New Value")
	private String sfNewValue;
	
	@JsonProperty("modified_at")
	private String modifiedAt;
	
	
	@JsonProperty("Title")
	private String title;
	
	@JsonProperty("Site")
	private String site;
	
	@JsonProperty("List")
	private String list;
	
	
	
	
	/**
	 * @return the list
	 */
	public String getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(String list) {
		this.list = list;
	}


	/**
	 * @return the object_name
	 */
	@JsonProperty("object_name")
	public String getObject_name() {
		return object_name;
	}


	/**
	 * @param object_name the object_name to set
	 */
	@JsonProperty("object_name")
	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}

	
	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}


	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the modifiedAt
	 */
	public String getModifiedAt() {
		return modifiedAt;
	}


	/**
	 * @param modifiedAt the modifiedAt to set
	 */
	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	
	public int getInternalReciptCount() {
		return internalReciptCount;
	}


	public void setInternalReciptCount(int internalReciptCount) {
		this.internalReciptCount = internalReciptCount;
	}


	public String getExternalRecipientsCount() {
		return externalRecipientsCount;
	}


	public void setExternalRecipientsCount(String externalRecipientsCount) {
		this.externalRecipientsCount = externalRecipientsCount;
	}
	/**
	 * @return the sfOldValue
	 */
	@JsonProperty("Old Value")
	public String getSfOldValue() {
		return sfOldValue;
	}


	/**
	 * @param sfOldValue the sfOldValue to set
	 */
	@JsonProperty("Old Value")
	public void setSfOldValue(String sfOldValue) {
		this.sfOldValue = sfOldValue;
	}


	/**
	 * @return the sfNewValue
	 */
	@JsonProperty("New Value")
	public String getSfNewValue() {
		return sfNewValue;
	}


	/**
	 * @param sfNewValue the sfNewValue to set
	 */
	@JsonProperty("New Value")
	public void setSfNewValue(String sfNewValue) {
		this.sfNewValue = sfNewValue;
	}


	/**
	 * @return the accountType
	 */
	
	public String getAccountType() {
		return accountType;
	}
	
	
	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	/**
	 * @return the targetAccountType
	 */
	public String getTargetAccountType() {
		return targetAccountType;
	}


	/**
	 * @param targetAccountType the targetAccountType to set
	 */
	public void setTargetAccountType(String targetAccountType) {
		this.targetAccountType = targetAccountType;
	}


	/**
	 * @return the address
	 */
	@JsonProperty("address")
	public String getAddress() {
		return address;
	}


	/**
	 * @param address the address to set
	 */
	@JsonProperty("address")
	public void setAddress(String address) {
		this.address = address;
	}


	/**
	 * @return the timezone
	 */
	@JsonProperty("timezone")
	public String getTimezone() {
		return timezone;
	}


	/**
	 * @param timezone the timezone to set
	 */
	@JsonProperty("timezone")
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}


	/**
	 * @return the owned_by
	 */
	public String getOwned_by() {
		return owned_by;
	}


	/**
	 * @param owned_by the owned_by to set
	 */
	public void setOwned_by(String owned_by) {
		this.owned_by = owned_by;
	}


	/**
	 * @return the owned_by_name
	 */
	public String getOwned_by_name() {
		return owned_by_name;
	}


	/**
	 * @param owned_by_name the owned_by_name to set
	 */
	public void setOwned_by_name(String owned_by_name) {
		this.owned_by_name = owned_by_name;
	}


	/**
	 * @return the userType
	 */
	@JsonProperty("user_type")
	public String getUserType() {
		return userType;
	}


	/**
	 * @param userType the userType to set
	 */
	@JsonProperty("user_type")
	public void setUserType(String userType) {
		this.userType = userType;
	}


	/**
	 * @return the object_Name
	 */
	@JsonProperty("Object Name")
	public String getObject_Name() {
		return object_Name;
	}


	/**
	 * @param object_Name the object_Name to set
	 */
	@JsonProperty("Object Name")
	public void setObject_Name(String object_Name) {
		this.object_Name = object_Name;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}


	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}


	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}


	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}


	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}


	/**
	 * @param device the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}


	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}


	/**
	 * @param browser the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}


	
	/**
	 * @return the eventType
	 */
	@JsonProperty("event_type")
	public String getEventType() {
		return eventType;
	}


	/**
	 * @param eventType the eventType to set
	 */
	@JsonProperty("event_type")
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}


	/**
	 * @return the previous_Path
	 */
	public String getPrevious_Path() {
		return Previous_Path;
	}


	/**
	 * @param previous_Path the previous_Path to set
	 */
	public void setPrevious_Path(String previous_Path) {
		Previous_Path = previous_Path;
	}


	/**
	 * @return the current_Path
	 */
	public String getCurrent_Path() {
		return Current_Path;
	}


	/**
	 * @param current_Path the current_Path to set
	 */
	public void setCurrent_Path(String current_Path) {
		Current_Path = current_Path;
	}
	
	/**
	 * @return the internal_recipients
	 */
	@JsonProperty("Internal_recipients")
	public String getInternal_recipients() {
		return Internal_recipients;
	}


	/**
	 * @param internal_recipients the internal_recipients to set
	 */
	@JsonProperty("Internal_recipients")
	public void setInternal_recipients(String internal_recipients) {
		Internal_recipients = internal_recipients;
	}
	
	/**
	 * @return the external_Recipients
	 */
	public String getExternal_Recipients() {
		return External_Recipients;
	}


	/**
	 * @param external_Recipients the external_Recipients to set
	 */
	public void setExternal_Recipients(String external_Recipients) {
		External_Recipients = external_Recipients;
	}
	

	/**
	 * @return the parentId
	 */
	@JsonProperty("parent_id")
	public String getParentId() {
		return parentId;
	}


	/**
	 * @param parentId the parentId to set
	 */
	@JsonProperty("parent_id")
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	/**
	 * @return the updatedTimestamp
	 */
	public String getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @param updatedTimestamp the updatedTimestamp to set
	 */
	public void setUpdatedTimestamp(String updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}


	/**
	 * @return the latency
	 */
	@JsonProperty("_latency")
	public String getLatency() {
		return latency;
	}


	/**
	 * @param latency the latency to set
	 */
	@JsonProperty("_latency")
	public void setLatency(String latency) {
		this.latency = latency;
	}


	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}


	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	@JsonProperty("__mail_group_id")
	private String mailGroupId;
	
	@JsonProperty("_SharedWith")
	private String SharedWith;
	
	@JsonProperty("Unshared_With")
	private String unsharedWith;
	
	

	@JsonProperty("Shared_With")
	private String o365sharedWith;
	
	@JsonProperty("Current_Shared_With")
	private String currentSharedWith;
	
		@JsonProperty("_PolicyViolated")
	private String PolicyViolated;
	
	
	@JsonProperty("content_vulnerability")
	private List<String> contentVulnerability = new ArrayList<String>();
	


	@JsonProperty("Document_type")
	private String DocumentType;
	@JsonProperty("content_checks")
	private ContentChecks contentChecks;

	@JsonProperty("Object")
	private String Object;
	@JsonProperty("Collaborator")
	private String Collaborator;



	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	
	
	

	/**
	 * @return the currentSharedWith
	 */
	public String getCurrentSharedWith() {
		return currentSharedWith;
	}


	/**
	 * @param currentSharedWith the currentSharedWith to set
	 */
	public void setCurrentSharedWith(String currentSharedWith) {
		this.currentSharedWith = currentSharedWith;
	}


	
	/**
	 * @return the unsharedWith
	 */
	public String getUnsharedWith() {
		return unsharedWith;
	}
	
	
	/**
	 * @param unsharedWith the unsharedWith to set
	 */
	public void setUnsharedWith(String unsharedWith) {
		this.unsharedWith = unsharedWith;
	}
	
	
	/**
	 * @return the o365sharedWith
	 */
	public String getO365sharedWith() {
		return o365sharedWith;
	}
	
	
	/**
	 * @param o365sharedWith the o365sharedWith to set
	 */
	public void setO365sharedWith(String o365sharedWith) {
		this.o365sharedWith = o365sharedWith;
	}
	
	

	/**
	 * @return the contentVulnerability
	 */
	@JsonProperty("content_vulnerability")
	public List<String> getContentVulnerability() {
		return contentVulnerability;
	}
	/**
	 * @param contentVulnerability the contentVulnerability to set
	 */
	@JsonProperty("content_vulnerability")
	public void setContentVulnerability(List<String> contentVulnerability) {
		this.contentVulnerability = contentVulnerability;
	}
	

	@JsonProperty("Risks")
	public String getRisks() {
		return Risks;
	}
	@JsonProperty("Risks")
	public void setRisks(String risks) {
		Risks = risks;
	}

	
	/**
	 * @return the policyViolated
	 */
	@JsonProperty("_PolicyViolated")
	public String getPolicyViolated() {
		return PolicyViolated;
	}
	/**
	 * @param policyViolated the policyViolated to set
	 */
	@JsonProperty("_PolicyViolated")
	public void setPolicyViolated(String policyViolated) {
		PolicyViolated = policyViolated;
	}
	
	
	/**
	 * @return the sharedWith
	 */
	@JsonProperty("_SharedWith")
	public String getSharedWith() {
		return SharedWith;
	}
	/**
	 * @param sharedWith the sharedWith to set
	 */
	@JsonProperty("_SharedWith")
	public void setSharedWith(String sharedWith) {
		SharedWith = sharedWith;
	}
	
	
	/**
	 *
	 * @return
	 * The Object
	 */
	@JsonProperty("Object")
	public String getObject() {
		return Object;
	}

	/**
	 *
	 * @param Object
	 * The Object
	 */
	@JsonProperty("Object")
	public void setObject(String Object) {
		this.Object = Object;
	}

	/**
	 *
	 * @return
	 * The Collaborator
	 */
	@JsonProperty("Collaborator")
	public String getCollaborator() {
		return Collaborator;
	}

	/**
	 *
	 * @param Collaborator
	 * The Collaborator
	 */
	@JsonProperty("Collaborator")
	public void setCollaborator(String Collaborator) {
		this.Collaborator = Collaborator;
	}

	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("version")
	public String getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("req_uri")
	public String getReqUri() {
		return reqUri;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("req_uri")
	public void setReqUri(String reqUri) {
		this.reqUri = reqUri;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("req_size")
	public String getReqSize() {
		return reqSize;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("req_size")
	public void setReqSize(String reqSize) {
		this.reqSize = reqSize;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("resp_size")
	public String getRespSize() {
		return respSize;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("resp_size")
	public void setRespSize(String respSize) {
		this.respSize = respSize;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("resp_code")
	public String getRespCode() {
		return respCode;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("resp_code")
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("web_path")
	public String getWebPath() {
		return webPath;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("web_path")
	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("region")
	public String getRegion() {
		return region;
	}
	
	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("region")
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("is_anonymous_proxy")
	public String getIsAnonymousProxy() {
		return isAnonymousProxy;
	}
	
	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("is_anonymous_proxy")
	public void getIsAnonymousProxy(String isAnonymousProxy) {
		this.isAnonymousProxy = isAnonymousProxy;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("referer_uri")
	public String getRefererUri() {
		return refererUri;
	}
	
	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("referer_uri")
	public void setRefererUri(String refererUri) {
		this.refererUri = refererUri;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("elastica_user")
	public String getElasticaUser() {
		return elasticaUser;
	}
	
	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("elastica_user")
	public void setElasticaUser(String elasticaUser) {
		this.elasticaUser = elasticaUser;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("transit_hosts")
	public String getTransitHosts() {
		return transitHosts;
	}
	
	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("transit_hosts")
	public void setTransitHosts(String transitHosts) {
		this.transitHosts = transitHosts;
	}
	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("time_zone")
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("time_zone")
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 * 
	 * @return
	 * The facility
	 */
	@JsonProperty("activity_count")
	public String getActivityCount() {
		return activityCount;
	}

	/**
	 * 
	 * @param facility
	 * The facility
	 */
	@JsonProperty("activity_count")
	public void setActivityCount(String activityCount) {
		this.activityCount = activityCount;
	}/**
	 * 
	 * @return
	 * The facility
	 */
	@JsonProperty("external_recipients")
	public String getExternalRecipients() {
		return externalRecipients;
	}

	/**
	 * 
	 * @param externalRecipients
	 * The externalRecipients
	 */
	@JsonProperty("external_recipients")
	public void setExternalRecipients(String externalRecipients) {
		this.externalRecipients = externalRecipients;
	}/**
	 * 
	 * @return
	 * The facility
	 */
	@JsonProperty("internal_recipients")
	public String getInternalRecipients() {
		return internalRecipients;
	}

	/**
	 * 
	 * @param facility
	 * The facility
	 */
	@JsonProperty("internal_recipients")
	public void setInternalRecipients(String internalRecipients) {
		this.internalRecipients = internalRecipients;
	}/**
	 * 
	 * @return
	 * The In_Folder
	 */
	@JsonProperty("In_Folder")
	public String getInFolder() {
		return InFolder;
	}

	/**
	 * 
	 * @param In_Folder
	 * The In_Folder
	 */
	@JsonProperty("In_Folder")
	public void setInFolder(String InFolder) {
		this.InFolder = InFolder;
	}
	
	/**
	 * 
	 * @return
	 * The facility
	 */
	@JsonProperty("facility")
	public String getFacility() {
		return facility;
	}

	/**
	 * 
	 * @param facility
	 * The facility
	 */
	@JsonProperty("facility")
	public void setFacility(String facility) {
		this.facility = facility;
	}

	
	/**
	 * 
	 * @return
	 * The FileSize
	 */
	@JsonProperty("file_size")
	public long getfilesize() {
		return this.filesize;
	}

	/**
	 * 
	 * @param FileSize
	 * The File_Size
	 */
	@JsonProperty("file_size")
	public void setfilesize(long filesize) {
		this.filesize = filesize;
	}
	
	/**
	 * 
	 * @return
	 * The FileSize
	 */
	@JsonProperty("File_Size")
	public Integer getFileSize() {
		return FileSize;
	}

	/**
	 * 
	 * @param FileSize
	 * The File_Size
	 */
	@JsonProperty("File_Size")
	public void setFileSize(Integer FileSize) {
		this.FileSize = FileSize;
	}

	/**
	 * 
	 * @return
	 * The createdTimestamp
	 */
	@JsonProperty("created_timestamp")
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * 
	 * @param createdTimestamp
	 * The created_timestamp
	 */
	@JsonProperty("created_timestamp")
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	/**
	 * 
	 * @return
	 * The message
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 * The message
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return
	 * The ActivityType
	 */
	@JsonProperty("Activity_type")
	public String getActivityType() {
		return ActivityType;
	}

	/**
	 * 
	 * @param ActivityType
	 * The Activity_type
	 */
	@JsonProperty("Activity_type")
	public void setActivityType(String ActivityType) {
		this.ActivityType = ActivityType;
	}

	/**
	 * 
	 * @return
	 * The city
	 */
	@JsonProperty("city")
	public String getCity() {
		return city;
	}

	/**
	 * 
	 * @param city
	 * The city
	 */
	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}


	/**
	 * @return the previous_path
	 */
	@JsonProperty("previous_path")
	public String getPreviousPath() {
		return previous_path;
	}


	/**
	 * @param previous_path the previous_path to set
	 */
	@JsonProperty("previous_path")
	public void setPreviousPath(String previousPath) {
		this.previous_path = previousPath;
	}


	/**
	 * @return the currentPath
	 */
	@JsonProperty("current_path")
	public String getCurrentPath() {
		return currentPath;
	}

	/**
	 * @param currentPath the currentPath to set
	 */
	@JsonProperty("current_path")
	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}


	/**
	 * 
	 * @return
	 * The severity
	 */
	@JsonProperty("severity")
	public String getSeverity() {
		return severity;
	}

	/**
	 * 
	 * @param severity
	 * The severity
	 */
	@JsonProperty("severity")
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * 
	 * @return
	 * The ObjectType
	 */
	@JsonProperty("Object_type")
	public String getObjectType() {
		return ObjectType;
	}

	/**
	 * 
	 * @param ObjectType
	 * The Object_type
	 */
	@JsonProperty("Object_type")
	public void setObjectType(String ObjectType) {
		this.ObjectType = ObjectType;
	}

	/**
	 * 
	 * @return
	 * The location
	 */
	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location
	 * The location
	 */
	@JsonProperty("location")
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 
	 * @return
	 * The latitude
	 */
	@JsonProperty("latitude")
	public String getLatitude() {
		return latitude;
	}

	/**
	 * 
	 * @param latitude
	 * The latitude
	 */
	@JsonProperty("latitude")
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * 
	 * @return
	 * The userName
	 */
	@JsonProperty("user_name")
	public String getUserName() {
		return userName;
	}

	/**
	 * 
	 * @param userName
	 * The user_name
	 */
	@JsonProperty("user_name")
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 
	 * @return
	 * The userName
	 */
	@JsonProperty("user-agent")
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * 
	 * @param userName
	 * The user_name
	 */
	@JsonProperty("user-agent")
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * 
	 * @return
	 * The parent
	 */
	@JsonProperty("parent")
	public String getParent() {
		return parent;
	}

	/**
	 * 
	 * @param parent
	 * The parent
	 */
	@JsonProperty("parent")
	public void setParent(String parent) {
		this.parent = parent;
	}


	@JsonProperty("Parent")
	public String getOneDriveParent() {
		return OneDriveParent;
	}
	@JsonProperty("Parent")
	public void setOneDriveParent(String oneDriveParent) {
		OneDriveParent = oneDriveParent;
	}


	/**
	 * 
	 * @return
	 * The ResourceId
	 */
	@JsonProperty("Resource_Id")
	public String getResourceId() {
		return ResourceId;
	}

	/**
	 * 
	 * @param ResourceId
	 * The Resource_Id
	 */
	@JsonProperty("Resource_Id")
	public void setResourceId(String ResourceId) {
		this.ResourceId = ResourceId;
	}

	/**
	 * 
	 * @return
	 * The host
	 */
	@JsonProperty("host")
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * @param host
	 * The host
	 */
	@JsonProperty("host")
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * @return
	 * The user
	 */
	@JsonProperty("user")
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 * The user
	 */
	@JsonProperty("user")
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * 
	 * @return
	 * The insertedTimestamp
	 */
	@JsonProperty("inserted_timestamp")
	public String getInsertedTimestamp() {
		return insertedTimestamp;
	}

	/**
	 * 
	 * @param insertedTimestamp
	 * The inserted_timestamp
	 */
	@JsonProperty("inserted_timestamp")
	public void setInsertedTimestamp(String insertedTimestamp) {
		this.insertedTimestamp = insertedTimestamp;
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
	 * The ObjectName
	 */
	@JsonProperty("_ObjectName")
	public String getObjectName() {
		return ObjectName;
	}

	/**
	 * 
	 * @param ObjectName
	 * The _ObjectName
	 */
	@JsonProperty("_ObjectName")
	public void setObjectName(String ObjectName) {
		this.ObjectName = ObjectName;
	}
	
	
	/**
	 * 
	 * @return
	 * The country
	 */
	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	/**
	 * 
	 * @param country
	 * The country
	 */
	@JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 
	 * @return
	 * The longitude
	 */
	@JsonProperty("longitude")
	public String getLongitude() {
		return longitude;
	}

	/**
	 * 
	 * @param longitude
	 * The longitude
	 */
	@JsonProperty("longitude")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * 
	 * @return
	 * The Source
	 */
	@JsonProperty("__source")
	public String getSource() {
		return Source;
	}
	
	/**
	 * 
	 * @param Source
	 * The __source
	 */
	@JsonProperty("__source")
	public void setSource(String Source) {
		this.Source = Source;
	}
	/**
	 * 
	 * @return
	 * The mailGroupId
	 */
	@JsonProperty("__mail_group_id")
	public String setMailGroupId() {
		return mailGroupId;
	}

	/**
	 * 
	 * @param mailGroupId
	 * The __mail_group_id
	 */
	@JsonProperty("__mail_group_id")
	public void setMailGroupId(String mailGroupId) {
		this.mailGroupId = mailGroupId;
	}

	/**
	 * 
	 * @return
	 * The DocumentType
	 */
	@JsonProperty("Document_type")
	public String getDocumentType() {
		return DocumentType;
	}

	/**
	 * 
	 * @param DocumentType
	 * The Document_type
	 */
	@JsonProperty("Document_type")
	public void setDocumentType(String DocumentType) {
		this.DocumentType = DocumentType;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	/**
	 * 
	 * @return
	 * The pii
	 */
	@JsonProperty("content_checks")
	public ContentChecks getContentChecks() {
		return contentChecks;
	}

	/**
	 * 
	 * @param pii
	 * The pii
	 */
	@JsonProperty("content_checks")
	public void setContentChecks(ContentChecks contentChecks) {
		this.contentChecks = contentChecks;
	}


	/**
	 * @return the attachments
	 */
	public String getAttachments() {
		return attachments;
	}


	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}


	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}


	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}


	/**
	 * @return the file_type
	 */
	public String getFile_type() {
		return file_type;
	}


	/**
	 * @param file_type the file_type to set
	 */
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}


	/**
	 * @return the file_name
	 */
	public String getFile_name() {
		return file_name;
	}


	/**
	 * @param file_name the file_name to set
	 */
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}


	/**
	 * @return the file_extension
	 */
	public String getFile_extension() {
		return file_extension;
	}


	/**
	 * @param file_extension the file_extension to set
	 */
	public void setFile_extension(String file_extension) {
		this.file_extension = file_extension;
	}


	/**
	 * @return the oldValue
	 */
	public String getOldValue() {
		return OldValue;
	}


	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(String oldValue) {
		OldValue = oldValue;
	}


	/**
	 * @return the newValue
	 */
	public String getNewValue() {
		return NewValue;
	}


	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(String newValue) {
		NewValue = newValue;
	}


	/**
	 * @return the instance
	 */
	public String getInstance() {
		return instance;
	}


	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}


	/**
	 * @return the object_url
	 */
	public String getObject_url() {
		return object_url;
	}


	/**
	 * @param object_url the object_url to set
	 */
	public void setObject_url(String object_url) {
		this.object_url = object_url;
	}


	/**
	 * @return the objectId
	 */
	@JsonProperty("Object Id")
	public String getObjectId() {
		return objectId;
	}


	/**
	 * @param objectId the objectId to set
	 */
	@JsonProperty("Object Id")
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	/**
	 * @return the org_unit
	 */
	
	public String getOrgUnit() {
		return orgUnit;
	}
	
	
	/**
	 * @param set org_unit
	 */
	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}
	/**
	 * @return the externalRecipientCount
	 */
	
	public int getExternalRecipientCount() {
		return externalRecipientCount;
	}
	
	
	/**
	 * @param set externalRecipientCount
	 */
	public void setExternalRecipientCount(int externalRecipientCount) {
		this.externalRecipientCount = externalRecipientCount;
	}
	
	/**
	 * @return the internalRecipientCount
	 */
	
	public int getInternalRecipientCount() {
		return internalRecipientCount;
	}


	/**
	 * @param set internalRecipientCount
	 */
	public void setInternalRecipientCount(int internalRecipientCount) {
		this.externalRecipientCount = internalRecipientCount;
	}

	/**
	 * @return the ResultStatus
	 */
	
	public String getResultStatus() {
		return resultStatus;
	}
	
	
	/**
	 * @param set ResultStatus
	 */
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
}