package com.universal.dtos.salesforce;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class SObjectInput {
	
	//Fields needed for Account creation
	private String Name;
	
	//Fields for Case creation
	private String AccountId;
	private String CommunityId;
	private String ContactId;
	private String Description;
	private String Origin;
	private String OwnerId;
	private String Priority;
	private String Reason;
	private String Status;
	private String Subject;
	private String SuppliedCompany;
	private String SuppliedEmail;
	private String SuppliedName;
	private String SuppliedPhone;
	private String Type;
	
	
	

	//Fields for contact creation
	private String Department;
		private String Email;
	private String EmailBouncedDate;
	private String EmailBouncedReason;
	private String Fax;
	private String FirstName;
	private String LastName;
	private String Title;
	private String MailingCity;		 
	private String MailingCountry;		 
	private String MailingCountryCode;		 
	private String MailingLatitude; 
	private String MailingLongitude;		 
	private String MailingPostalCode;		 
	private String MailingState;
	private String MailingStateCode;		 
	private String MailingStreet;	 
	private String MobilePhone;	 
	private String Phone;	 
	private String ReportsToId;		
	private String Salutation;
	
	//Fields for opportunity creation
	//CloseDate, Name, StageName are mandatory
	
	private String Amount;	 
	private String CampaignId;
	private String CloseDate;	 
	private String ForecastCategoryName;	 
	private String LeadSource;
	private String NextStep;
	private String Pricebook2Id;
	private String Probability;	 
	private String StageName;	 
	private String SyncedQuoteId;		
	
	//lastname and company are mandatory for Lead creation
	
	//Fields for user creation
	//Username, Alias, CommunityNickname, TimeZoneSidKey, LocaleSidKey, EmailEncodingKey, ProfileId, LanguageLocaleKey
	private String Username;	
	private String Alias;
	private String CommunityNickname;
	private String TimeZoneSidKey;
	private String LocaleSidKey;
	private String EmailEncodingKey;
	private String ProfileId;
	private String LanguageLocaleKey;
	
	
	private Integer ContractTerm;
	private String StartDate;
	private String EndDate;
	private String StatusCode;
	
	
	//solution fields
	
	private String SolutionName; 
	private String SolutionNote;
	
	//Folder
	private String DeveloperName;
	private String AccessType;
	
	//Report
	private String Format;
	
	//ApexPage
	private String MasterLabel;
	private String Markup;
	
	//ApexPage	
	private Integer DurationInMinutes;
	private String ActivityDateTime;
	
	//Feeditem
	
	private String Body;
	private String ParentId;
	
	
	
	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return ParentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		ParentId = parentId;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return Body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		Body = body;
	}

	/**
	 * @return the durationInMinutes
	 */
	public Integer getDurationInMinutes() {
		return DurationInMinutes;
	}

	/**
	 * @param durationInMinutes the durationInMinutes to set
	 */
	public void setDurationInMinutes(Integer durationInMinutes) {
		DurationInMinutes = durationInMinutes;
	}

	/**
	 * @return the activityDateTime
	 */
	public String getActivityDateTime() {
		return ActivityDateTime;
	}

	/**
	 * @param activityDateTime the activityDateTime to set
	 */
	public void setActivityDateTime(String activityDateTime) {
		ActivityDateTime = activityDateTime;
	}

	/**
	 * @return the markup
	 */
	public String getMarkup() {
		return Markup;
	}

	/**
	 * @param markup the markup to set
	 */
	public void setMarkup(String markup) {
		Markup = markup;
	}

	/**
	 * @return the masterLabel
	 */
	public String getMasterLabel() {
		return MasterLabel;
	}

	/**
	 * @param masterLabel the masterLabel to set
	 */
	public void setMasterLabel(String masterLabel) {
		MasterLabel = masterLabel;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return Format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		Format = format;
	}

	/**
	 * @return the accessType
	 */
	public String getAccessType() {
		return AccessType;
	}

	/**
	 * @param accessType the accessType to set
	 */
	public void setAccessType(String accessType) {
		AccessType = accessType;
	}

	/**
	 * @return the developerName
	 */
	public String getDeveloperName() {
		return DeveloperName;
	}

	/**
	 * @param developerName the developerName to set
	 */
	public void setDeveloperName(String developerName) {
		DeveloperName = developerName;
	}

	/**
	 * @return the solutionName
	 */
	public String getSolutionName() {
		return SolutionName;
	}

	/**
	 * @param solutionName the solutionName to set
	 */
	public void setSolutionName(String solutionName) {
		SolutionName = solutionName;
	}

	/**
	 * @return the solutionNote
	 */
	public String getSolutionNote() {
		return SolutionNote;
	}

	/**
	 * @param solutionNote the solutionNote to set
	 */
	public void setSolutionNote(String solutionNote) {
		SolutionNote = solutionNote;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return StatusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return StartDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return EndDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	
	
	/**
	 * @return the contractTerm
	 */
	public Integer getContractTerm() {
		return ContractTerm;
	}

	/**
	 * @param i the contractTerm to set
	 */
	public void setContractTerm(Integer i) {
		ContractTerm = i;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return Username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		Username = username;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return Alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		Alias = alias;
	}

	/**
	 * @return the communityNickname
	 */
	public String getCommunityNickname() {
		return CommunityNickname;
	}

	/**
	 * @param communityNickname the communityNickname to set
	 */
	public void setCommunityNickname(String communityNickname) {
		CommunityNickname = communityNickname;
	}

	/**
	 * @return the timeZoneSidKey
	 */
	public String getTimeZoneSidKey() {
		return TimeZoneSidKey;
	}

	/**
	 * @param timeZoneSidKey the timeZoneSidKey to set
	 */
	public void setTimeZoneSidKey(String timeZoneSidKey) {
		TimeZoneSidKey = timeZoneSidKey;
	}

	/**
	 * @return the localeSidKey
	 */
	public String getLocaleSidKey() {
		return LocaleSidKey;
	}

	/**
	 * @param localeSidKey the localeSidKey to set
	 */
	public void setLocaleSidKey(String localeSidKey) {
		LocaleSidKey = localeSidKey;
	}

	/**
	 * @return the emailEncodingKey
	 */
	public String getEmailEncodingKey() {
		return EmailEncodingKey;
	}

	/**
	 * @param emailEncodingKey the emailEncodingKey to set
	 */
	public void setEmailEncodingKey(String emailEncodingKey) {
		EmailEncodingKey = emailEncodingKey;
	}

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return ProfileId;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId) {
		ProfileId = profileId;
	}

	/**
	 * @return the languageLocaleKey
	 */
	public String getLanguageLocaleKey() {
		return LanguageLocaleKey;
	}

	/**
	 * @param languageLocaleKey the languageLocaleKey to set
	 */
	public void setLanguageLocaleKey(String languageLocaleKey) {
		LanguageLocaleKey = languageLocaleKey;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	
	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return AccountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		AccountId = accountId;
	}

	/**
	 * @return the communityId
	 */
	public String getCommunityId() {
		return CommunityId;
	}

	/**
	 * @param communityId the communityId to set
	 */
	public void setCommunityId(String communityId) {
		CommunityId = communityId;
	}

	/**
	 * @return the contactId
	 */
	public String getContactId() {
		return ContactId;
	}

	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(String contactId) {
		ContactId = contactId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return Origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		Origin = origin;
	}

	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return OwnerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		OwnerId = ownerId;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return Priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		Priority = priority;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return Reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		Reason = reason;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return Subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		Subject = subject;
	}

	/**
	 * @return the suppliedCompany
	 */
	public String getSuppliedCompany() {
		return SuppliedCompany;
	}

	/**
	 * @param suppliedCompany the suppliedCompany to set
	 */
	public void setSuppliedCompany(String suppliedCompany) {
		SuppliedCompany = suppliedCompany;
	}

	/**
	 * @return the suppliedEmail
	 */
	public String getSuppliedEmail() {
		return SuppliedEmail;
	}

	/**
	 * @param suppliedEmail the suppliedEmail to set
	 */
	public void setSuppliedEmail(String suppliedEmail) {
		SuppliedEmail = suppliedEmail;
	}

	/**
	 * @return the suppliedName
	 */
	public String getSuppliedName() {
		return SuppliedName;
	}

	/**
	 * @param suppliedName the suppliedName to set
	 */
	public void setSuppliedName(String suppliedName) {
		SuppliedName = suppliedName;
	}

	/**
	 * @return the suppliedPhone
	 */
	public String getSuppliedPhone() {
		return SuppliedPhone;
	}

	/**
	 * @param suppliedPhone the suppliedPhone to set
	 */
	public void setSuppliedPhone(String suppliedPhone) {
		SuppliedPhone = suppliedPhone;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return Type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		Type = type;
	}
	
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return Department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		Department = department;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return Email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		Email = email;
	}

	/**
	 * @return the emailBouncedDate
	 */
	public String getEmailBouncedDate() {
		return EmailBouncedDate;
	}

	/**
	 * @param emailBouncedDate the emailBouncedDate to set
	 */
	public void setEmailBouncedDate(String emailBouncedDate) {
		EmailBouncedDate = emailBouncedDate;
	}

	/**
	 * @return the emailBouncedReason
	 */
	public String getEmailBouncedReason() {
		return EmailBouncedReason;
	}

	/**
	 * @param emailBouncedReason the emailBouncedReason to set
	 */
	public void setEmailBouncedReason(String emailBouncedReason) {
		EmailBouncedReason = emailBouncedReason;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return Fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		Fax = fax;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return FirstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return LastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		LastName = lastName;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return Title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		Title = title;
	}

	/**
	 * @return the mailingCity
	 */
	public String getMailingCity() {
		return MailingCity;
	}

	/**
	 * @param mailingCity the mailingCity to set
	 */
	public void setMailingCity(String mailingCity) {
		MailingCity = mailingCity;
	}

	/**
	 * @return the mailingCountry
	 */
	public String getMailingCountry() {
		return MailingCountry;
	}

	/**
	 * @param mailingCountry the mailingCountry to set
	 */
	public void setMailingCountry(String mailingCountry) {
		MailingCountry = mailingCountry;
	}

	/**
	 * @return the mailingCountryCode
	 */
	public String getMailingCountryCode() {
		return MailingCountryCode;
	}

	/**
	 * @param mailingCountryCode the mailingCountryCode to set
	 */
	public void setMailingCountryCode(String mailingCountryCode) {
		MailingCountryCode = mailingCountryCode;
	}

	/**
	 * @return the mailingLatitude
	 */
	public String getMailingLatitude() {
		return MailingLatitude;
	}

	/**
	 * @param mailingLatitude the mailingLatitude to set
	 */
	public void setMailingLatitude(String mailingLatitude) {
		MailingLatitude = mailingLatitude;
	}

	/**
	 * @return the mailingLongitude
	 */
	public String getMailingLongitude() {
		return MailingLongitude;
	}

	/**
	 * @param mailingLongitude the mailingLongitude to set
	 */
	public void setMailingLongitude(String mailingLongitude) {
		MailingLongitude = mailingLongitude;
	}

	/**
	 * @return the mailingPostalCode
	 */
	public String getMailingPostalCode() {
		return MailingPostalCode;
	}

	/**
	 * @param mailingPostalCode the mailingPostalCode to set
	 */
	public void setMailingPostalCode(String mailingPostalCode) {
		MailingPostalCode = mailingPostalCode;
	}

	/**
	 * @return the mailingState
	 */
	public String getMailingState() {
		return MailingState;
	}

	/**
	 * @param mailingState the mailingState to set
	 */
	public void setMailingState(String mailingState) {
		MailingState = mailingState;
	}

	/**
	 * @return the mailingStateCode
	 */
	public String getMailingStateCode() {
		return MailingStateCode;
	}

	/**
	 * @param mailingStateCode the mailingStateCode to set
	 */
	public void setMailingStateCode(String mailingStateCode) {
		MailingStateCode = mailingStateCode;
	}

	/**
	 * @return the mailingStreet
	 */
	public String getMailingStreet() {
		return MailingStreet;
	}

	/**
	 * @param mailingStreet the mailingStreet to set
	 */
	public void setMailingStreet(String mailingStreet) {
		MailingStreet = mailingStreet;
	}

	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return MobilePhone;
	}

	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone) {
		MobilePhone = mobilePhone;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return Phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		Phone = phone;
	}

	/**
	 * @return the reportsToId
	 */
	public String getReportsToId() {
		return ReportsToId;
	}

	/**
	 * @param reportsToId the reportsToId to set
	 */
	public void setReportsToId(String reportsToId) {
		ReportsToId = reportsToId;
	}

	/**
	 * @return the salutation
	 */
	public String getSalutation() {
		return Salutation;
	}

	/**
	 * @param salutation the salutation to set
	 */
	public void setSalutation(String salutation) {
		Salutation = salutation;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return Amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		Amount = amount;
	}

	/**
	 * @return the campaignId
	 */
	public String getCampaignId() {
		return CampaignId;
	}

	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(String campaignId) {
		CampaignId = campaignId;
	}

	/**
	 * @return the closeDate
	 */
	public String getCloseDate() {
		return CloseDate;
	}

	/**
	 * @param closeDate the closeDate to set
	 */
	public void setCloseDate(String closeDate) {
		CloseDate = closeDate;
	}

	/**
	 * @return the forecastCategoryName
	 */
	public String getForecastCategoryName() {
		return ForecastCategoryName;
	}

	/**
	 * @param forecastCategoryName the forecastCategoryName to set
	 */
	public void setForecastCategoryName(String forecastCategoryName) {
		ForecastCategoryName = forecastCategoryName;
	}

	/**
	 * @return the leadSource
	 */
	public String getLeadSource() {
		return LeadSource;
	}

	/**
	 * @param leadSource the leadSource to set
	 */
	public void setLeadSource(String leadSource) {
		LeadSource = leadSource;
	}

	/**
	 * @return the nextStep
	 */
	public String getNextStep() {
		return NextStep;
	}

	/**
	 * @param nextStep the nextStep to set
	 */
	public void setNextStep(String nextStep) {
		NextStep = nextStep;
	}

	/**
	 * @return the pricebook2Id
	 */
	public String getPricebook2Id() {
		return Pricebook2Id;
	}

	/**
	 * @param pricebook2Id the pricebook2Id to set
	 */
	public void setPricebook2Id(String pricebook2Id) {
		Pricebook2Id = pricebook2Id;
	}

	/**
	 * @return the probability
	 */
	public String getProbability() {
		return Probability;
	}

	/**
	 * @param probability the probability to set
	 */
	public void setProbability(String probability) {
		Probability = probability;
	}

	/**
	 * @return the stageName
	 */
	public String getStageName() {
		return StageName;
	}

	/**
	 * @param stageName the stageName to set
	 */
	public void setStageName(String stageName) {
		StageName = stageName;
	}

	/**
	 * @return the syncedQuoteId
	 */
	public String getSyncedQuoteId() {
		return SyncedQuoteId;
	}

	/**
	 * @param syncedQuoteId the syncedQuoteId to set
	 */
	public void setSyncedQuoteId(String syncedQuoteId) {
		SyncedQuoteId = syncedQuoteId;
	}
	
}
