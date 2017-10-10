package com.universal.dtos.salesforce;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Contact {

	@JsonProperty("attributes")
	private Attributes attributes;
	@JsonProperty("Id")
	private String Id;
	@JsonProperty("IsDeleted")
	private Boolean IsDeleted;
	@JsonProperty("MasterRecordId")
	private Object MasterRecordId;
	@JsonProperty("AccountId")
	private Object AccountId;
	@JsonProperty("LastName")
	private String LastName;
	@JsonProperty("FirstName")
	private String FirstName;
	@JsonProperty("Salutation")
	private Object Salutation;
	@JsonProperty("Name")
	private String Name;
	@JsonProperty("MailingStreet")
	private Object MailingStreet;
	@JsonProperty("MailingCity")
	private Object MailingCity;
	@JsonProperty("MailingState")
	private Object MailingState;
	@JsonProperty("MailingPostalCode")
	private Object MailingPostalCode;
	@JsonProperty("MailingCountry")
	private String MailingCountry;
	@JsonProperty("MailingStateCode")
	private Object MailingStateCode;
	@JsonProperty("MailingCountryCode")
	private String MailingCountryCode;
	@JsonProperty("MailingLatitude")
	private Object MailingLatitude;
	@JsonProperty("MailingLongitude")
	private Object MailingLongitude;
	@JsonProperty("MailingAddress")
	private MailingAddress MailingAddress;
	@JsonProperty("Phone")
	private Object Phone;
	@JsonProperty("Fax")
	private Object Fax;
	@JsonProperty("MobilePhone")
	private Object MobilePhone;
	@JsonProperty("ReportsToId")
	private Object ReportsToId;
	@JsonProperty("Email")
	private String Email;
	@JsonProperty("Title")
	private Object Title;
	@JsonProperty("Department")
	private String Department;
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
	@JsonProperty("LastActivityDate")
	private Object LastActivityDate;
	@JsonProperty("LastCURequestDate")
	private Object LastCURequestDate;
	@JsonProperty("LastCUUpdateDate")
	private Object LastCUUpdateDate;
	@JsonProperty("LastViewedDate")
	private String LastViewedDate;
	@JsonProperty("LastReferencedDate")
	private String LastReferencedDate;
	@JsonProperty("EmailBouncedReason")
	private Object EmailBouncedReason;
	@JsonProperty("EmailBouncedDate")
	private Object EmailBouncedDate;
	@JsonProperty("IsEmailBounced")
	private Boolean IsEmailBounced;
	@JsonProperty("PhotoUrl")
	private String PhotoUrl;
	@JsonProperty("JigsawContactId")
	private Object JigsawContactId;
	@JsonProperty("awduplicate2__AWDisableDuplicateCheck__c")
	private Boolean awduplicate2AWDisableDuplicateCheckC;
	@JsonProperty("sf4twitter__Contact_Source__c")
	private Object sf4twitterContactSourceC;
	@JsonProperty("sf4twitter__Fcbk_Image_Url__c")
	private Object sf4twitterFcbkImageUrlC;
	@JsonProperty("sf4twitter__Fcbk_Picture__c")
	private String sf4twitterFcbkPictureC;
	@JsonProperty("sf4twitter__Fcbk_Profile_Url__c")
	private Object sf4twitterFcbkProfileUrlC;
	@JsonProperty("sf4twitter__Fcbk_User_Id__c")
	private Object sf4twitterFcbkUserIdC;
	@JsonProperty("sf4twitter__Fcbk_Username__c")
	private Object sf4twitterFcbkUsernameC;
	@JsonProperty("sf4twitter__Follow_Status__c")
	private String sf4twitterFollowStatusC;
	@JsonProperty("sf4twitter__Influencer_Type__c")
	private Object sf4twitterInfluencerTypeC;
	@JsonProperty("sf4twitter__Influencer__c")
	private String sf4twitterInfluencerC;
	@JsonProperty("sf4twitter__Klout_Number__c")
	private Object sf4twitterKloutNumberC;
	@JsonProperty("sf4twitter__Klout_Score__c")
	private Object sf4twitterKloutScoreC;
	@JsonProperty("sf4twitter__Languages__c")
	private Object sf4twitterLanguagesC;
	@JsonProperty("sf4twitter__Level__c")
	private Object sf4twitterLevelC;
	@JsonProperty("sf4twitter__Origin__c")
	private Object sf4twitterOriginC;
	@JsonProperty("sf4twitter__Research_this_Contact__c")
	private String sf4twitterResearchThisContactC;
	@JsonProperty("sf4twitter__Twitter_Bio__c")
	private Object sf4twitterTwitterBioC;
	@JsonProperty("sf4twitter__Twitter_Creation_Date__c")
	private Object sf4twitterTwitterCreationDateC;
	@JsonProperty("sf4twitter__Twitter_Follower_Count__c")
	private Object sf4twitterTwitterFollowerCountC;
	@JsonProperty("sf4twitter__Twitter_Following_count__c")
	private Object sf4twitterTwitterFollowingCountC;
	@JsonProperty("sf4twitter__Twitter_Image_Url__c")
	private Object sf4twitterTwitterImageUrlC;
	@JsonProperty("sf4twitter__Twitter_Last_Tweet__c")
	private Object sf4twitterTwitterLastTweetC;
	@JsonProperty("sf4twitter__Twitter_Location__c")
	private Object sf4twitterTwitterLocationC;
	@JsonProperty("sf4twitter__Twitter_Picture__c")
	private String sf4twitterTwitterPictureC;
	@JsonProperty("sf4twitter__Twitter_Status_count__c")
	private Object sf4twitterTwitterStatusCountC;
	@JsonProperty("sf4twitter__Twitter_User_Id__c")
	private Object sf4twitterTwitterUserIdC;
	@JsonProperty("sf4twitter__Twitter_Username__c")
	private Object sf4twitterTwitterUsernameC;
	@JsonProperty("sf4twitter__followed__c")
	private Boolean sf4twitterFollowedC;
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
	 * The MasterRecordId
	 */
	@JsonProperty("MasterRecordId")
	public Object getMasterRecordId() {
		return MasterRecordId;
	}

	/**
	 * 
	 * @param MasterRecordId
	 * The MasterRecordId
	 */
	@JsonProperty("MasterRecordId")
	public void setMasterRecordId(Object MasterRecordId) {
		this.MasterRecordId = MasterRecordId;
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
	 * The LastName
	 */
	@JsonProperty("LastName")
	public String getLastName() {
		return LastName;
	}

	/**
	 * 
	 * @param LastName
	 * The LastName
	 */
	@JsonProperty("LastName")
	public void setLastName(String LastName) {
		this.LastName = LastName;
	}

	/**
	 * 
	 * @return
	 * The FirstName
	 */
	@JsonProperty("FirstName")
	public String getFirstName() {
		return FirstName;
	}

	/**
	 * 
	 * @param FirstName
	 * The FirstName
	 */
	@JsonProperty("FirstName")
	public void setFirstName(String FirstName) {
		this.FirstName = FirstName;
	}

	/**
	 * 
	 * @return
	 * The Salutation
	 */
	@JsonProperty("Salutation")
	public Object getSalutation() {
		return Salutation;
	}

	/**
	 * 
	 * @param Salutation
	 * The Salutation
	 */
	@JsonProperty("Salutation")
	public void setSalutation(Object Salutation) {
		this.Salutation = Salutation;
	}

	/**
	 * 
	 * @return
	 * The Name
	 */
	@JsonProperty("Name")
	public String getName() {
		return Name;
	}

	/**
	 * 
	 * @param Name
	 * The Name
	 */
	@JsonProperty("Name")
	public void setName(String Name) {
		this.Name = Name;
	}

	/**
	 * 
	 * @return
	 * The MailingStreet
	 */
	@JsonProperty("MailingStreet")
	public Object getMailingStreet() {
		return MailingStreet;
	}

	/**
	 * 
	 * @param MailingStreet
	 * The MailingStreet
	 */
	@JsonProperty("MailingStreet")
	public void setMailingStreet(Object MailingStreet) {
		this.MailingStreet = MailingStreet;
	}

	/**
	 * 
	 * @return
	 * The MailingCity
	 */
	@JsonProperty("MailingCity")
	public Object getMailingCity() {
		return MailingCity;
	}

	/**
	 * 
	 * @param MailingCity
	 * The MailingCity
	 */
	@JsonProperty("MailingCity")
	public void setMailingCity(Object MailingCity) {
		this.MailingCity = MailingCity;
	}

	/**
	 * 
	 * @return
	 * The MailingState
	 */
	@JsonProperty("MailingState")
	public Object getMailingState() {
		return MailingState;
	}

	/**
	 * 
	 * @param MailingState
	 * The MailingState
	 */
	@JsonProperty("MailingState")
	public void setMailingState(Object MailingState) {
		this.MailingState = MailingState;
	}

	/**
	 * 
	 * @return
	 * The MailingPostalCode
	 */
	@JsonProperty("MailingPostalCode")
	public Object getMailingPostalCode() {
		return MailingPostalCode;
	}

	/**
	 * 
	 * @param MailingPostalCode
	 * The MailingPostalCode
	 */
	@JsonProperty("MailingPostalCode")
	public void setMailingPostalCode(Object MailingPostalCode) {
		this.MailingPostalCode = MailingPostalCode;
	}

	/**
	 * 
	 * @return
	 * The MailingCountry
	 */
	@JsonProperty("MailingCountry")
	public String getMailingCountry() {
		return MailingCountry;
	}

	/**
	 * 
	 * @param MailingCountry
	 * The MailingCountry
	 */
	@JsonProperty("MailingCountry")
	public void setMailingCountry(String MailingCountry) {
		this.MailingCountry = MailingCountry;
	}

	/**
	 * 
	 * @return
	 * The MailingStateCode
	 */
	@JsonProperty("MailingStateCode")
	public Object getMailingStateCode() {
		return MailingStateCode;
	}

	/**
	 * 
	 * @param MailingStateCode
	 * The MailingStateCode
	 */
	@JsonProperty("MailingStateCode")
	public void setMailingStateCode(Object MailingStateCode) {
		this.MailingStateCode = MailingStateCode;
	}

	/**
	 * 
	 * @return
	 * The MailingCountryCode
	 */
	@JsonProperty("MailingCountryCode")
	public String getMailingCountryCode() {
		return MailingCountryCode;
	}

	/**
	 * 
	 * @param MailingCountryCode
	 * The MailingCountryCode
	 */
	@JsonProperty("MailingCountryCode")
	public void setMailingCountryCode(String MailingCountryCode) {
		this.MailingCountryCode = MailingCountryCode;
	}

	/**
	 * 
	 * @return
	 * The MailingLatitude
	 */
	@JsonProperty("MailingLatitude")
	public Object getMailingLatitude() {
		return MailingLatitude;
	}

	/**
	 * 
	 * @param MailingLatitude
	 * The MailingLatitude
	 */
	@JsonProperty("MailingLatitude")
	public void setMailingLatitude(Object MailingLatitude) {
		this.MailingLatitude = MailingLatitude;
	}

	/**
	 * 
	 * @return
	 * The MailingLongitude
	 */
	@JsonProperty("MailingLongitude")
	public Object getMailingLongitude() {
		return MailingLongitude;
	}

	/**
	 * 
	 * @param MailingLongitude
	 * The MailingLongitude
	 */
	@JsonProperty("MailingLongitude")
	public void setMailingLongitude(Object MailingLongitude) {
		this.MailingLongitude = MailingLongitude;
	}

	/**
	 * 
	 * @return
	 * The MailingAddress
	 */
	@JsonProperty("MailingAddress")
	public MailingAddress getMailingAddress() {
		return MailingAddress;
	}

	/**
	 * 
	 * @param MailingAddress
	 * The MailingAddress
	 */
	@JsonProperty("MailingAddress")
	public void setMailingAddress(MailingAddress MailingAddress) {
		this.MailingAddress = MailingAddress;
	}

	/**
	 * 
	 * @return
	 * The Phone
	 */
	@JsonProperty("Phone")
	public Object getPhone() {
		return Phone;
	}

	/**
	 * 
	 * @param Phone
	 * The Phone
	 */
	@JsonProperty("Phone")
	public void setPhone(Object Phone) {
		this.Phone = Phone;
	}

	/**
	 * 
	 * @return
	 * The Fax
	 */
	@JsonProperty("Fax")
	public Object getFax() {
		return Fax;
	}

	/**
	 * 
	 * @param Fax
	 * The Fax
	 */
	@JsonProperty("Fax")
	public void setFax(Object Fax) {
		this.Fax = Fax;
	}

	/**
	 * 
	 * @return
	 * The MobilePhone
	 */
	@JsonProperty("MobilePhone")
	public Object getMobilePhone() {
		return MobilePhone;
	}

	/**
	 * 
	 * @param MobilePhone
	 * The MobilePhone
	 */
	@JsonProperty("MobilePhone")
	public void setMobilePhone(Object MobilePhone) {
		this.MobilePhone = MobilePhone;
	}

	/**
	 * 
	 * @return
	 * The ReportsToId
	 */
	@JsonProperty("ReportsToId")
	public Object getReportsToId() {
		return ReportsToId;
	}

	/**
	 * 
	 * @param ReportsToId
	 * The ReportsToId
	 */
	@JsonProperty("ReportsToId")
	public void setReportsToId(Object ReportsToId) {
		this.ReportsToId = ReportsToId;
	}

	/**
	 * 
	 * @return
	 * The Email
	 */
	@JsonProperty("Email")
	public String getEmail() {
		return Email;
	}

	/**
	 * 
	 * @param Email
	 * The Email
	 */
	@JsonProperty("Email")
	public void setEmail(String Email) {
		this.Email = Email;
	}

	/**
	 * 
	 * @return
	 * The Title
	 */
	@JsonProperty("Title")
	public Object getTitle() {
		return Title;
	}

	/**
	 * 
	 * @param Title
	 * The Title
	 */
	@JsonProperty("Title")
	public void setTitle(Object Title) {
		this.Title = Title;
	}

	/**
	 * 
	 * @return
	 * The Department
	 */
	@JsonProperty("Department")
	public String getDepartment() {
		return Department;
	}

	/**
	 * 
	 * @param Department
	 * The Department
	 */
	@JsonProperty("Department")
	public void setDepartment(String Department) {
		this.Department = Department;
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
	 * The LastActivityDate
	 */
	@JsonProperty("LastActivityDate")
	public Object getLastActivityDate() {
		return LastActivityDate;
	}

	/**
	 * 
	 * @param LastActivityDate
	 * The LastActivityDate
	 */
	@JsonProperty("LastActivityDate")
	public void setLastActivityDate(Object LastActivityDate) {
		this.LastActivityDate = LastActivityDate;
	}

	/**
	 * 
	 * @return
	 * The LastCURequestDate
	 */
	@JsonProperty("LastCURequestDate")
	public Object getLastCURequestDate() {
		return LastCURequestDate;
	}

	/**
	 * 
	 * @param LastCURequestDate
	 * The LastCURequestDate
	 */
	@JsonProperty("LastCURequestDate")
	public void setLastCURequestDate(Object LastCURequestDate) {
		this.LastCURequestDate = LastCURequestDate;
	}

	/**
	 * 
	 * @return
	 * The LastCUUpdateDate
	 */
	@JsonProperty("LastCUUpdateDate")
	public Object getLastCUUpdateDate() {
		return LastCUUpdateDate;
	}

	/**
	 * 
	 * @param LastCUUpdateDate
	 * The LastCUUpdateDate
	 */
	@JsonProperty("LastCUUpdateDate")
	public void setLastCUUpdateDate(Object LastCUUpdateDate) {
		this.LastCUUpdateDate = LastCUUpdateDate;
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
	 * The EmailBouncedReason
	 */
	@JsonProperty("EmailBouncedReason")
	public Object getEmailBouncedReason() {
		return EmailBouncedReason;
	}

	/**
	 * 
	 * @param EmailBouncedReason
	 * The EmailBouncedReason
	 */
	@JsonProperty("EmailBouncedReason")
	public void setEmailBouncedReason(Object EmailBouncedReason) {
		this.EmailBouncedReason = EmailBouncedReason;
	}

	/**
	 * 
	 * @return
	 * The EmailBouncedDate
	 */
	@JsonProperty("EmailBouncedDate")
	public Object getEmailBouncedDate() {
		return EmailBouncedDate;
	}

	/**
	 * 
	 * @param EmailBouncedDate
	 * The EmailBouncedDate
	 */
	@JsonProperty("EmailBouncedDate")
	public void setEmailBouncedDate(Object EmailBouncedDate) {
		this.EmailBouncedDate = EmailBouncedDate;
	}

	/**
	 * 
	 * @return
	 * The IsEmailBounced
	 */
	@JsonProperty("IsEmailBounced")
	public Boolean getIsEmailBounced() {
		return IsEmailBounced;
	}

	/**
	 * 
	 * @param IsEmailBounced
	 * The IsEmailBounced
	 */
	@JsonProperty("IsEmailBounced")
	public void setIsEmailBounced(Boolean IsEmailBounced) {
		this.IsEmailBounced = IsEmailBounced;
	}

	/**
	 * 
	 * @return
	 * The PhotoUrl
	 */
	@JsonProperty("PhotoUrl")
	public String getPhotoUrl() {
		return PhotoUrl;
	}

	/**
	 * 
	 * @param PhotoUrl
	 * The PhotoUrl
	 */
	@JsonProperty("PhotoUrl")
	public void setPhotoUrl(String PhotoUrl) {
		this.PhotoUrl = PhotoUrl;
	}

	/**
	 * 
	 * @return
	 * The JigsawContactId
	 */
	@JsonProperty("JigsawContactId")
	public Object getJigsawContactId() {
		return JigsawContactId;
	}

	/**
	 * 
	 * @param JigsawContactId
	 * The JigsawContactId
	 */
	@JsonProperty("JigsawContactId")
	public void setJigsawContactId(Object JigsawContactId) {
		this.JigsawContactId = JigsawContactId;
	}

	/**
	 * 
	 * @return
	 * The awduplicate2AWDisableDuplicateCheckC
	 */
	@JsonProperty("awduplicate2__AWDisableDuplicateCheck__c")
	public Boolean getAwduplicate2AWDisableDuplicateCheckC() {
		return awduplicate2AWDisableDuplicateCheckC;
	}

	/**
	 * 
	 * @param awduplicate2AWDisableDuplicateCheckC
	 * The awduplicate2__AWDisableDuplicateCheck__c
	 */
	@JsonProperty("awduplicate2__AWDisableDuplicateCheck__c")
	public void setAwduplicate2AWDisableDuplicateCheckC(Boolean awduplicate2AWDisableDuplicateCheckC) {
		this.awduplicate2AWDisableDuplicateCheckC = awduplicate2AWDisableDuplicateCheckC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterContactSourceC
	 */
	@JsonProperty("sf4twitter__Contact_Source__c")
	public Object getSf4twitterContactSourceC() {
		return sf4twitterContactSourceC;
	}

	/**
	 * 
	 * @param sf4twitterContactSourceC
	 * The sf4twitter__Contact_Source__c
	 */
	@JsonProperty("sf4twitter__Contact_Source__c")
	public void setSf4twitterContactSourceC(Object sf4twitterContactSourceC) {
		this.sf4twitterContactSourceC = sf4twitterContactSourceC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterFcbkImageUrlC
	 */
	@JsonProperty("sf4twitter__Fcbk_Image_Url__c")
	public Object getSf4twitterFcbkImageUrlC() {
		return sf4twitterFcbkImageUrlC;
	}

	/**
	 * 
	 * @param sf4twitterFcbkImageUrlC
	 * The sf4twitter__Fcbk_Image_Url__c
	 */
	@JsonProperty("sf4twitter__Fcbk_Image_Url__c")
	public void setSf4twitterFcbkImageUrlC(Object sf4twitterFcbkImageUrlC) {
		this.sf4twitterFcbkImageUrlC = sf4twitterFcbkImageUrlC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterFcbkPictureC
	 */
	@JsonProperty("sf4twitter__Fcbk_Picture__c")
	public String getSf4twitterFcbkPictureC() {
		return sf4twitterFcbkPictureC;
	}

	/**
	 * 
	 * @param sf4twitterFcbkPictureC
	 * The sf4twitter__Fcbk_Picture__c
	 */
	@JsonProperty("sf4twitter__Fcbk_Picture__c")
	public void setSf4twitterFcbkPictureC(String sf4twitterFcbkPictureC) {
		this.sf4twitterFcbkPictureC = sf4twitterFcbkPictureC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterFcbkProfileUrlC
	 */
	@JsonProperty("sf4twitter__Fcbk_Profile_Url__c")
	public Object getSf4twitterFcbkProfileUrlC() {
		return sf4twitterFcbkProfileUrlC;
	}

	/**
	 * 
	 * @param sf4twitterFcbkProfileUrlC
	 * The sf4twitter__Fcbk_Profile_Url__c
	 */
	@JsonProperty("sf4twitter__Fcbk_Profile_Url__c")
	public void setSf4twitterFcbkProfileUrlC(Object sf4twitterFcbkProfileUrlC) {
		this.sf4twitterFcbkProfileUrlC = sf4twitterFcbkProfileUrlC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterFcbkUserIdC
	 */
	@JsonProperty("sf4twitter__Fcbk_User_Id__c")
	public Object getSf4twitterFcbkUserIdC() {
		return sf4twitterFcbkUserIdC;
	}

	/**
	 * 
	 * @param sf4twitterFcbkUserIdC
	 * The sf4twitter__Fcbk_User_Id__c
	 */
	@JsonProperty("sf4twitter__Fcbk_User_Id__c")
	public void setSf4twitterFcbkUserIdC(Object sf4twitterFcbkUserIdC) {
		this.sf4twitterFcbkUserIdC = sf4twitterFcbkUserIdC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterFcbkUsernameC
	 */
	@JsonProperty("sf4twitter__Fcbk_Username__c")
	public Object getSf4twitterFcbkUsernameC() {
		return sf4twitterFcbkUsernameC;
	}

	/**
	 * 
	 * @param sf4twitterFcbkUsernameC
	 * The sf4twitter__Fcbk_Username__c
	 */
	@JsonProperty("sf4twitter__Fcbk_Username__c")
	public void setSf4twitterFcbkUsernameC(Object sf4twitterFcbkUsernameC) {
		this.sf4twitterFcbkUsernameC = sf4twitterFcbkUsernameC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterFollowStatusC
	 */
	@JsonProperty("sf4twitter__Follow_Status__c")
	public String getSf4twitterFollowStatusC() {
		return sf4twitterFollowStatusC;
	}

	/**
	 * 
	 * @param sf4twitterFollowStatusC
	 * The sf4twitter__Follow_Status__c
	 */
	@JsonProperty("sf4twitter__Follow_Status__c")
	public void setSf4twitterFollowStatusC(String sf4twitterFollowStatusC) {
		this.sf4twitterFollowStatusC = sf4twitterFollowStatusC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterInfluencerTypeC
	 */
	@JsonProperty("sf4twitter__Influencer_Type__c")
	public Object getSf4twitterInfluencerTypeC() {
		return sf4twitterInfluencerTypeC;
	}

	/**
	 * 
	 * @param sf4twitterInfluencerTypeC
	 * The sf4twitter__Influencer_Type__c
	 */
	@JsonProperty("sf4twitter__Influencer_Type__c")
	public void setSf4twitterInfluencerTypeC(Object sf4twitterInfluencerTypeC) {
		this.sf4twitterInfluencerTypeC = sf4twitterInfluencerTypeC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterInfluencerC
	 */
	@JsonProperty("sf4twitter__Influencer__c")
	public String getSf4twitterInfluencerC() {
		return sf4twitterInfluencerC;
	}

	/**
	 * 
	 * @param sf4twitterInfluencerC
	 * The sf4twitter__Influencer__c
	 */
	@JsonProperty("sf4twitter__Influencer__c")
	public void setSf4twitterInfluencerC(String sf4twitterInfluencerC) {
		this.sf4twitterInfluencerC = sf4twitterInfluencerC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterKloutNumberC
	 */
	@JsonProperty("sf4twitter__Klout_Number__c")
	public Object getSf4twitterKloutNumberC() {
		return sf4twitterKloutNumberC;
	}

	/**
	 * 
	 * @param sf4twitterKloutNumberC
	 * The sf4twitter__Klout_Number__c
	 */
	@JsonProperty("sf4twitter__Klout_Number__c")
	public void setSf4twitterKloutNumberC(Object sf4twitterKloutNumberC) {
		this.sf4twitterKloutNumberC = sf4twitterKloutNumberC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterKloutScoreC
	 */
	@JsonProperty("sf4twitter__Klout_Score__c")
	public Object getSf4twitterKloutScoreC() {
		return sf4twitterKloutScoreC;
	}

	/**
	 * 
	 * @param sf4twitterKloutScoreC
	 * The sf4twitter__Klout_Score__c
	 */
	@JsonProperty("sf4twitter__Klout_Score__c")
	public void setSf4twitterKloutScoreC(Object sf4twitterKloutScoreC) {
		this.sf4twitterKloutScoreC = sf4twitterKloutScoreC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterLanguagesC
	 */
	@JsonProperty("sf4twitter__Languages__c")
	public Object getSf4twitterLanguagesC() {
		return sf4twitterLanguagesC;
	}

	/**
	 * 
	 * @param sf4twitterLanguagesC
	 * The sf4twitter__Languages__c
	 */
	@JsonProperty("sf4twitter__Languages__c")
	public void setSf4twitterLanguagesC(Object sf4twitterLanguagesC) {
		this.sf4twitterLanguagesC = sf4twitterLanguagesC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterLevelC
	 */
	@JsonProperty("sf4twitter__Level__c")
	public Object getSf4twitterLevelC() {
		return sf4twitterLevelC;
	}

	/**
	 * 
	 * @param sf4twitterLevelC
	 * The sf4twitter__Level__c
	 */
	@JsonProperty("sf4twitter__Level__c")
	public void setSf4twitterLevelC(Object sf4twitterLevelC) {
		this.sf4twitterLevelC = sf4twitterLevelC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterOriginC
	 */
	@JsonProperty("sf4twitter__Origin__c")
	public Object getSf4twitterOriginC() {
		return sf4twitterOriginC;
	}

	/**
	 * 
	 * @param sf4twitterOriginC
	 * The sf4twitter__Origin__c
	 */
	@JsonProperty("sf4twitter__Origin__c")
	public void setSf4twitterOriginC(Object sf4twitterOriginC) {
		this.sf4twitterOriginC = sf4twitterOriginC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterResearchThisContactC
	 */
	@JsonProperty("sf4twitter__Research_this_Contact__c")
	public String getSf4twitterResearchThisContactC() {
		return sf4twitterResearchThisContactC;
	}

	/**
	 * 
	 * @param sf4twitterResearchThisContactC
	 * The sf4twitter__Research_this_Contact__c
	 */
	@JsonProperty("sf4twitter__Research_this_Contact__c")
	public void setSf4twitterResearchThisContactC(String sf4twitterResearchThisContactC) {
		this.sf4twitterResearchThisContactC = sf4twitterResearchThisContactC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterBioC
	 */
	@JsonProperty("sf4twitter__Twitter_Bio__c")
	public Object getSf4twitterTwitterBioC() {
		return sf4twitterTwitterBioC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterBioC
	 * The sf4twitter__Twitter_Bio__c
	 */
	@JsonProperty("sf4twitter__Twitter_Bio__c")
	public void setSf4twitterTwitterBioC(Object sf4twitterTwitterBioC) {
		this.sf4twitterTwitterBioC = sf4twitterTwitterBioC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterCreationDateC
	 */
	@JsonProperty("sf4twitter__Twitter_Creation_Date__c")
	public Object getSf4twitterTwitterCreationDateC() {
		return sf4twitterTwitterCreationDateC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterCreationDateC
	 * The sf4twitter__Twitter_Creation_Date__c
	 */
	@JsonProperty("sf4twitter__Twitter_Creation_Date__c")
	public void setSf4twitterTwitterCreationDateC(Object sf4twitterTwitterCreationDateC) {
		this.sf4twitterTwitterCreationDateC = sf4twitterTwitterCreationDateC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterFollowerCountC
	 */
	@JsonProperty("sf4twitter__Twitter_Follower_Count__c")
	public Object getSf4twitterTwitterFollowerCountC() {
		return sf4twitterTwitterFollowerCountC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterFollowerCountC
	 * The sf4twitter__Twitter_Follower_Count__c
	 */
	@JsonProperty("sf4twitter__Twitter_Follower_Count__c")
	public void setSf4twitterTwitterFollowerCountC(Object sf4twitterTwitterFollowerCountC) {
		this.sf4twitterTwitterFollowerCountC = sf4twitterTwitterFollowerCountC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterFollowingCountC
	 */
	@JsonProperty("sf4twitter__Twitter_Following_count__c")
	public Object getSf4twitterTwitterFollowingCountC() {
		return sf4twitterTwitterFollowingCountC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterFollowingCountC
	 * The sf4twitter__Twitter_Following_count__c
	 */
	@JsonProperty("sf4twitter__Twitter_Following_count__c")
	public void setSf4twitterTwitterFollowingCountC(Object sf4twitterTwitterFollowingCountC) {
		this.sf4twitterTwitterFollowingCountC = sf4twitterTwitterFollowingCountC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterImageUrlC
	 */
	@JsonProperty("sf4twitter__Twitter_Image_Url__c")
	public Object getSf4twitterTwitterImageUrlC() {
		return sf4twitterTwitterImageUrlC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterImageUrlC
	 * The sf4twitter__Twitter_Image_Url__c
	 */
	@JsonProperty("sf4twitter__Twitter_Image_Url__c")
	public void setSf4twitterTwitterImageUrlC(Object sf4twitterTwitterImageUrlC) {
		this.sf4twitterTwitterImageUrlC = sf4twitterTwitterImageUrlC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterLastTweetC
	 */
	@JsonProperty("sf4twitter__Twitter_Last_Tweet__c")
	public Object getSf4twitterTwitterLastTweetC() {
		return sf4twitterTwitterLastTweetC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterLastTweetC
	 * The sf4twitter__Twitter_Last_Tweet__c
	 */
	@JsonProperty("sf4twitter__Twitter_Last_Tweet__c")
	public void setSf4twitterTwitterLastTweetC(Object sf4twitterTwitterLastTweetC) {
		this.sf4twitterTwitterLastTweetC = sf4twitterTwitterLastTweetC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterLocationC
	 */
	@JsonProperty("sf4twitter__Twitter_Location__c")
	public Object getSf4twitterTwitterLocationC() {
		return sf4twitterTwitterLocationC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterLocationC
	 * The sf4twitter__Twitter_Location__c
	 */
	@JsonProperty("sf4twitter__Twitter_Location__c")
	public void setSf4twitterTwitterLocationC(Object sf4twitterTwitterLocationC) {
		this.sf4twitterTwitterLocationC = sf4twitterTwitterLocationC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterPictureC
	 */
	@JsonProperty("sf4twitter__Twitter_Picture__c")
	public String getSf4twitterTwitterPictureC() {
		return sf4twitterTwitterPictureC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterPictureC
	 * The sf4twitter__Twitter_Picture__c
	 */
	@JsonProperty("sf4twitter__Twitter_Picture__c")
	public void setSf4twitterTwitterPictureC(String sf4twitterTwitterPictureC) {
		this.sf4twitterTwitterPictureC = sf4twitterTwitterPictureC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterStatusCountC
	 */
	@JsonProperty("sf4twitter__Twitter_Status_count__c")
	public Object getSf4twitterTwitterStatusCountC() {
		return sf4twitterTwitterStatusCountC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterStatusCountC
	 * The sf4twitter__Twitter_Status_count__c
	 */
	@JsonProperty("sf4twitter__Twitter_Status_count__c")
	public void setSf4twitterTwitterStatusCountC(Object sf4twitterTwitterStatusCountC) {
		this.sf4twitterTwitterStatusCountC = sf4twitterTwitterStatusCountC;
	}

	/**
	 * 
	 * @return
	 * The sf4twitterTwitterUserIdC
	 */
	@JsonProperty("sf4twitter__Twitter_User_Id__c")
	public Object getSf4twitterTwitterUserIdC() {
		return sf4twitterTwitterUserIdC;
	}

	/**
	 * 
	 * @param sf4twitterTwitterUserIdC
	 * The sf4twitter__Twitter_User_Id__c
	 */
	@JsonProperty("sf4twitter__Twitter_User_Id__c")
	public void setSf4twitterTwitterUserIdC(Object sf4twitterTwitterUserIdC) {
		this.sf4twitterTwitterUserIdC = sf4twitterTwitterUserIdC;
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
	 * The sf4twitterFollowedC
	 */
	@JsonProperty("sf4twitter__followed__c")
	public Boolean getSf4twitterFollowedC() {
		return sf4twitterFollowedC;
	}

	/**
	 * 
	 * @param sf4twitterFollowedC
	 * The sf4twitter__followed__c
	 */
	@JsonProperty("sf4twitter__followed__c")
	public void setSf4twitterFollowedC(Boolean sf4twitterFollowedC) {
		this.sf4twitterFollowedC = sf4twitterFollowedC;
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
