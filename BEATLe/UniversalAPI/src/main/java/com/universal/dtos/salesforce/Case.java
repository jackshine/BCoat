package com.universal.dtos.salesforce;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Case {

	@JsonProperty("attributes")
	private Attributes attributes;
	@JsonProperty("Id")
	private String Id;
	@JsonProperty("IsDeleted")
	private Boolean IsDeleted;
	@JsonProperty("CaseNumber")
	private String CaseNumber;
	@JsonProperty("ContactId")
	private String ContactId;
	@JsonProperty("AccountId")
	private Object AccountId;
	@JsonProperty("CommunityId")
	private Object CommunityId;
	@JsonProperty("SuppliedName")
	private Object SuppliedName;
	@JsonProperty("SuppliedEmail")
	private Object SuppliedEmail;
	@JsonProperty("SuppliedPhone")
	private Object SuppliedPhone;
	@JsonProperty("SuppliedCompany")
	private Object SuppliedCompany;
	@JsonProperty("Type")
	private Object Type;
	@JsonProperty("Status")
	private String Status;
	@JsonProperty("Reason")
	private String Reason;
	@JsonProperty("Origin")
	private Object Origin;
	@JsonProperty("Subject")
	private Object Subject;
	@JsonProperty("Priority")
	private String Priority;
	@JsonProperty("Description")
	private String Description;
	@JsonProperty("IsClosed")
	private Boolean IsClosed;
	@JsonProperty("ClosedDate")
	private Object ClosedDate;
	@JsonProperty("HasCommentsUnreadByOwner")
	private Boolean HasCommentsUnreadByOwner;
	@JsonProperty("HasSelfServiceComments")
	private Boolean HasSelfServiceComments;
	@JsonProperty("OwnerId")
	private String OwnerId;
	@JsonProperty("CreatedDate")
	private String CreatedDate;
	@JsonProperty("CreatedById")
	private String CreatedById;
	@JsonProperty("LastModifiedDate")
	private String LastModifiedDate;
	@JsonProperty("LastModifiedById")
	private String LastModifiedById;
	@JsonProperty("SystemModstamp")
	private String SystemModstamp;
	@JsonProperty("LastViewedDate")
	private String LastViewedDate;
	@JsonProperty("LastReferencedDate")
	private String LastReferencedDate;
	@JsonProperty("CreatorFullPhotoUrl")
	private String CreatorFullPhotoUrl;
	@JsonProperty("CreatorSmallPhotoUrl")
	private String CreatorSmallPhotoUrl;
	@JsonProperty("CreatorName")
	private Object CreatorName;
	@JsonProperty("sf4twitter__Author_External_Id__c")
	private Object sf4twitterAuthorExternalIdC;
	@JsonProperty("sf4twitter__Twitter_Username__c")
	private Object sf4twitterTwitterUsernameC;
	@JsonProperty("sf4twitter__twitterId__c")
	private Object sf4twitterTwitterIdC;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The attributes
	 */
	@JsonProperty("attributes")
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * @param attributes
	 * The attributes
	 */
	@JsonProperty("attributes")
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * 
	 * @return
	 * The Id
	 */
	@JsonProperty("Id")
	public String getId() {
		return Id;
	}

	/**
	 * 
	 * @param Id
	 * The Id
	 */
	@JsonProperty("Id")
	public void setId(String Id) {
		this.Id = Id;
	}

	/**
	 * 
	 * @return
	 * The IsDeleted
	 */
	@JsonProperty("IsDeleted")
	public Boolean getIsDeleted() {
		return IsDeleted;
	}

	/**
	 * 
	 * @param IsDeleted
	 * The IsDeleted
	 */
	@JsonProperty("IsDeleted")
	public void setIsDeleted(Boolean IsDeleted) {
		this.IsDeleted = IsDeleted;
	}

	/**
	 * 
	 * @return
	 * The CaseNumber
	 */
	@JsonProperty("CaseNumber")
	public String getCaseNumber() {
		return CaseNumber;
	}

	/**
	 * 
	 * @param CaseNumber
	 * The CaseNumber
	 */
	@JsonProperty("CaseNumber")
	public void setCaseNumber(String CaseNumber) {
		this.CaseNumber = CaseNumber;
	}

	/**
	 * 
	 * @return
	 * The ContactId
	 */
	@JsonProperty("ContactId")
	public String getContactId() {
		return ContactId;
	}

	/**
	 * 
	 * @param ContactId
	 * The ContactId
	 */
	@JsonProperty("ContactId")
	public void setContactId(String ContactId) {
		this.ContactId = ContactId;
	}

	/**
	 * 
	 * @return
	 * The AccountId
	 */
	@JsonProperty("AccountId")
	public Object getAccountId() {
		return AccountId;
	}

	/**
	 * 
	 * @param AccountId
	 * The AccountId
	 */
	@JsonProperty("AccountId")
	public void setAccountId(Object AccountId) {
		this.AccountId = AccountId;
	}

	/**
	 * 
	 * @return
	 * The CommunityId
	 */
	@JsonProperty("CommunityId")
	public Object getCommunityId() {
		return CommunityId;
	}

	/**
	 * 
	 * @param CommunityId
	 * The CommunityId
	 */
	@JsonProperty("CommunityId")
	public void setCommunityId(Object CommunityId) {
		this.CommunityId = CommunityId;
	}

	/**
	 * 
	 * @return
	 * The SuppliedName
	 */
	@JsonProperty("SuppliedName")
	public Object getSuppliedName() {
		return SuppliedName;
	}

	/**
	 * 
	 * @param SuppliedName
	 * The SuppliedName
	 */
	@JsonProperty("SuppliedName")
	public void setSuppliedName(Object SuppliedName) {
		this.SuppliedName = SuppliedName;
	}

	/**
	 * 
	 * @return
	 * The SuppliedEmail
	 */
	@JsonProperty("SuppliedEmail")
	public Object getSuppliedEmail() {
		return SuppliedEmail;
	}

	/**
	 * 
	 * @param SuppliedEmail
	 * The SuppliedEmail
	 */
	@JsonProperty("SuppliedEmail")
	public void setSuppliedEmail(Object SuppliedEmail) {
		this.SuppliedEmail = SuppliedEmail;
	}

	/**
	 * 
	 * @return
	 * The SuppliedPhone
	 */
	@JsonProperty("SuppliedPhone")
	public Object getSuppliedPhone() {
		return SuppliedPhone;
	}

	/**
	 * 
	 * @param SuppliedPhone
	 * The SuppliedPhone
	 */
	@JsonProperty("SuppliedPhone")
	public void setSuppliedPhone(Object SuppliedPhone) {
		this.SuppliedPhone = SuppliedPhone;
	}

	/**
	 * 
	 * @return
	 * The SuppliedCompany
	 */
	@JsonProperty("SuppliedCompany")
	public Object getSuppliedCompany() {
		return SuppliedCompany;
	}

	/**
	 * 
	 * @param SuppliedCompany
	 * The SuppliedCompany
	 */
	@JsonProperty("SuppliedCompany")
	public void setSuppliedCompany(Object SuppliedCompany) {
		this.SuppliedCompany = SuppliedCompany;
	}

	/**
	 * 
	 * @return
	 * The Type
	 */
	@JsonProperty("Type")
	public Object getType() {
		return Type;
	}

	/**
	 * 
	 * @param Type
	 * The Type
	 */
	@JsonProperty("Type")
	public void setType(Object Type) {
		this.Type = Type;
	}

	/**
	 * 
	 * @return
	 * The Status
	 */
	@JsonProperty("Status")
	public String getStatus() {
		return Status;
	}

	/**
	 * 
	 * @param Status
	 * The Status
	 */
	@JsonProperty("Status")
	public void setStatus(String Status) {
		this.Status = Status;
	}

	/**
	 * 
	 * @return
	 * The Reason
	 */
	@JsonProperty("Reason")
	public String getReason() {
		return Reason;
	}

	/**
	 * 
	 * @param Reason
	 * The Reason
	 */
	@JsonProperty("Reason")
	public void setReason(String Reason) {
		this.Reason = Reason;
	}

	/**
	 * 
	 * @return
	 * The Origin
	 */
	@JsonProperty("Origin")
	public Object getOrigin() {
		return Origin;
	}

	/**
	 * 
	 * @param Origin
	 * The Origin
	 */
	@JsonProperty("Origin")
	public void setOrigin(Object Origin) {
		this.Origin = Origin;
	}

	/**
	 * 
	 * @return
	 * The Subject
	 */
	@JsonProperty("Subject")
	public Object getSubject() {
		return Subject;
	}

	/**
	 * 
	 * @param Subject
	 * The Subject
	 */
	@JsonProperty("Subject")
	public void setSubject(Object Subject) {
		this.Subject = Subject;
	}

	/**
	 * 
	 * @return
	 * The Priority
	 */
	@JsonProperty("Priority")
	public String getPriority() {
		return Priority;
	}

	/**
	 * 
	 * @param Priority
	 * The Priority
	 */
	@JsonProperty("Priority")
	public void setPriority(String Priority) {
		this.Priority = Priority;
	}

	/**
	 * 
	 * @return
	 * The Description
	 */
	@JsonProperty("Description")
	public String getDescription() {
		return Description;
	}

	/**
	 * 
	 * @param Description
	 * The Description
	 */
	@JsonProperty("Description")
	public void setDescription(String Description) {
		this.Description = Description;
	}

	/**
	 * 
	 * @return
	 * The IsClosed
	 */
	@JsonProperty("IsClosed")
	public Boolean getIsClosed() {
		return IsClosed;
	}

	/**
	 * 
	 * @param IsClosed
	 * The IsClosed
	 */
	@JsonProperty("IsClosed")
	public void setIsClosed(Boolean IsClosed) {
		this.IsClosed = IsClosed;
	}

	/**
	 * 
	 * @return
	 * The ClosedDate
	 */
	@JsonProperty("ClosedDate")
	public Object getClosedDate() {
		return ClosedDate;
	}

	/**
	 * 
	 * @param ClosedDate
	 * The ClosedDate
	 */
	@JsonProperty("ClosedDate")
	public void setClosedDate(Object ClosedDate) {
		this.ClosedDate = ClosedDate;
	}

	/**
	 * 
	 * @return
	 * The HasCommentsUnreadByOwner
	 */
	@JsonProperty("HasCommentsUnreadByOwner")
	public Boolean getHasCommentsUnreadByOwner() {
		return HasCommentsUnreadByOwner;
	}

	/**
	 * 
	 * @param HasCommentsUnreadByOwner
	 * The HasCommentsUnreadByOwner
	 */
	@JsonProperty("HasCommentsUnreadByOwner")
	public void setHasCommentsUnreadByOwner(Boolean HasCommentsUnreadByOwner) {
		this.HasCommentsUnreadByOwner = HasCommentsUnreadByOwner;
	}

	/**
	 * 
	 * @return
	 * The HasSelfServiceComments
	 */
	@JsonProperty("HasSelfServiceComments")
	public Boolean getHasSelfServiceComments() {
		return HasSelfServiceComments;
	}

	/**
	 * 
	 * @param HasSelfServiceComments
	 * The HasSelfServiceComments
	 */
	@JsonProperty("HasSelfServiceComments")
	public void setHasSelfServiceComments(Boolean HasSelfServiceComments) {
		this.HasSelfServiceComments = HasSelfServiceComments;
	}

	/**
	 * 
	 * @return
	 * The OwnerId
	 */
	@JsonProperty("OwnerId")
	public String getOwnerId() {
		return OwnerId;
	}

	/**
	 * 
	 * @param OwnerId
	 * The OwnerId
	 */
	@JsonProperty("OwnerId")
	public void setOwnerId(String OwnerId) {
		this.OwnerId = OwnerId;
	}

	/**
	 * 
	 * @return
	 * The CreatedDate
	 */
	@JsonProperty("CreatedDate")
	public String getCreatedDate() {
		return CreatedDate;
	}

	/**
	 * 
	 * @param CreatedDate
	 * The CreatedDate
	 */
	@JsonProperty("CreatedDate")
	public void setCreatedDate(String CreatedDate) {
		this.CreatedDate = CreatedDate;
	}

	/**
	 * 
	 * @return
	 * The CreatedById
	 */
	@JsonProperty("CreatedById")
	public String getCreatedById() {
		return CreatedById;
	}

	/**
	 * 
	 * @param CreatedById
	 * The CreatedById
	 */
	@JsonProperty("CreatedById")
	public void setCreatedById(String CreatedById) {
		this.CreatedById = CreatedById;
	}

	/**
	 * 
	 * @return
	 * The LastModifiedDate
	 */
	@JsonProperty("LastModifiedDate")
	public String getLastModifiedDate() {
		return LastModifiedDate;
	}

	/**
	 * 
	 * @param LastModifiedDate
	 * The LastModifiedDate
	 */
	@JsonProperty("LastModifiedDate")
	public void setLastModifiedDate(String LastModifiedDate) {
		this.LastModifiedDate = LastModifiedDate;
	}

	/**
	 * 
	 * @return
	 * The LastModifiedById
	 */
	@JsonProperty("LastModifiedById")
	public String getLastModifiedById() {
		return LastModifiedById;
	}

	/**
	 * 
	 * @param LastModifiedById
	 * The LastModifiedById
	 */
	@JsonProperty("LastModifiedById")
	public void setLastModifiedById(String LastModifiedById) {
		this.LastModifiedById = LastModifiedById;
	}

	/**
	 * 
	 * @return
	 * The SystemModstamp
	 */
	@JsonProperty("SystemModstamp")
	public String getSystemModstamp() {
		return SystemModstamp;
	}

	/**
	 * 
	 * @param SystemModstamp
	 * The SystemModstamp
	 */
	@JsonProperty("SystemModstamp")
	public void setSystemModstamp(String SystemModstamp) {
		this.SystemModstamp = SystemModstamp;
	}

	/**
	 * 
	 * @return
	 * The LastViewedDate
	 */
	@JsonProperty("LastViewedDate")
	public String getLastViewedDate() {
		return LastViewedDate;
	}

	/**
	 * 
	 * @param LastViewedDate
	 * The LastViewedDate
	 */
	@JsonProperty("LastViewedDate")
	public void setLastViewedDate(String LastViewedDate) {
		this.LastViewedDate = LastViewedDate;
	}

	/**
	 * 
	 * @return
	 * The LastReferencedDate
	 */
	@JsonProperty("LastReferencedDate")
	public String getLastReferencedDate() {
		return LastReferencedDate;
	}

	/**
	 * 
	 * @param LastReferencedDate
	 * The LastReferencedDate
	 */
	@JsonProperty("LastReferencedDate")
	public void setLastReferencedDate(String LastReferencedDate) {
		this.LastReferencedDate = LastReferencedDate;
	}

	/**
	 * 
	 * @return
	 * The CreatorFullPhotoUrl
	 */
	@JsonProperty("CreatorFullPhotoUrl")
	public String getCreatorFullPhotoUrl() {
		return CreatorFullPhotoUrl;
	}

	/**
	 * 
	 * @param CreatorFullPhotoUrl
	 * The CreatorFullPhotoUrl
	 */
	@JsonProperty("CreatorFullPhotoUrl")
	public void setCreatorFullPhotoUrl(String CreatorFullPhotoUrl) {
		this.CreatorFullPhotoUrl = CreatorFullPhotoUrl;
	}

	/**
	 * 
	 * @return
	 * The CreatorSmallPhotoUrl
	 */
	@JsonProperty("CreatorSmallPhotoUrl")
	public String getCreatorSmallPhotoUrl() {
		return CreatorSmallPhotoUrl;
	}

	/**
	 * 
	 * @param CreatorSmallPhotoUrl
	 * The CreatorSmallPhotoUrl
	 */
	@JsonProperty("CreatorSmallPhotoUrl")
	public void setCreatorSmallPhotoUrl(String CreatorSmallPhotoUrl) {
		this.CreatorSmallPhotoUrl = CreatorSmallPhotoUrl;
	}

	/**
	 * 
	 * @return
	 * The CreatorName
	 */
	@JsonProperty("CreatorName")
	public Object getCreatorName() {
		return CreatorName;
	}

	/**
	 * 
	 * @param CreatorName
	 * The CreatorName
	 */
	@JsonProperty("CreatorName")
	public void setCreatorName(Object CreatorName) {
		this.CreatorName = CreatorName;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterAuthorExternalIdC
	 */
	@JsonProperty("sf4twitter__Author_External_Id__c")
	public Object getSf4twitterAuthorExternalIdC() {
		return sf4twitterAuthorExternalIdC;
	}

	/**
	 * 
	 * @param sf4twitterAuthorExternalIdC
	 * The sf4twitter__Author_External_Id__c
	 */
	@JsonProperty("sf4twitter__Author_External_Id__c")
	public void setSf4twitterAuthorExternalIdC(Object sf4twitterAuthorExternalIdC) {
		this.sf4twitterAuthorExternalIdC = sf4twitterAuthorExternalIdC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterUsernameC
	 */
	@JsonProperty("sf4twitter__Twitter_Username__c")
	public Object getSf4twitterTwitterUsernameC() {
		return sf4twitterTwitterUsernameC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterUsernameC
	 * The sf4twitter__Twitter_Username__c
	 */
	@JsonProperty("sf4twitter__Twitter_Username__c")
	public void setSf4twitterTwitterUsernameC(Object sf4twitterTwitterUsernameC) {
		this.sf4twitterTwitterUsernameC = sf4twitterTwitterUsernameC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterIdC
	 */
	@JsonProperty("sf4twitter__twitterId__c")
	public Object getSf4twitterTwitterIdC() {
		return sf4twitterTwitterIdC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterIdC
	 * The sf4twitter__twitterId__c
	 */
	@JsonProperty("sf4twitter__twitterId__c")
	public void setSf4twitterTwitterIdC(Object sf4twitterTwitterIdC) {
		this.sf4twitterTwitterIdC = sf4twitterTwitterIdC;
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
