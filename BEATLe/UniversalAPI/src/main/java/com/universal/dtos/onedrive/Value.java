package com.universal.dtos.onedrive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Value {

	@JsonProperty("odata.type")
	private String odataType;
	@JsonProperty("objectType")
	private String objectType;
	@JsonProperty("objectId")
	private String objectId;
	@JsonProperty("deletionTimestamp")
	private Object deletionTimestamp;
	@JsonProperty("accountEnabled")
	private Boolean accountEnabled;
	@JsonProperty("assignedLicenses")
	private List<Object> assignedLicenses = new ArrayList<Object>();
	@JsonProperty("assignedPlans")
	private List<Object> assignedPlans = new ArrayList<Object>();
	@JsonProperty("city")
	private Object city;
	@JsonProperty("companyName")
	private Object companyName;
	@JsonProperty("country")
	private Object country;
	@JsonProperty("department")
	private Object department;
	@JsonProperty("dirSyncEnabled")
	private Object dirSyncEnabled;
	@JsonProperty("displayName")
	private String displayName;
	@JsonProperty("facsimileTelephoneNumber")
	private Object facsimileTelephoneNumber;
	@JsonProperty("givenName")
	private Object givenName;
	@JsonProperty("immutableId")
	private Object immutableId;
	@JsonProperty("jobTitle")
	private Object jobTitle;
	@JsonProperty("lastDirSyncTime")
	private Object lastDirSyncTime;
	@JsonProperty("mail")
	private String mail;
	@JsonProperty("mailNickname")
	private String mailNickname;
	@JsonProperty("mobile")
	private Object mobile;
	@JsonProperty("onPremisesSecurityIdentifier")
	private Object onPremisesSecurityIdentifier;
	@JsonProperty("otherMails")
	private List<String> otherMails = new ArrayList<String>();
	@JsonProperty("passwordPolicies")
	private Object passwordPolicies;
	@JsonProperty("passwordProfile")
	private Object passwordProfile;
	@JsonProperty("physicalDeliveryOfficeName")
	private Object physicalDeliveryOfficeName;
	@JsonProperty("postalCode")
	private Object postalCode;
	@JsonProperty("preferredLanguage")
	private Object preferredLanguage;
	@JsonProperty("provisionedPlans")
	private List<Object> provisionedPlans = new ArrayList<Object>();
	@JsonProperty("provisioningErrors")
	private List<Object> provisioningErrors = new ArrayList<Object>();
	@JsonProperty("proxyAddresses")
	private List<String> proxyAddresses = new ArrayList<String>();
	@JsonProperty("sipProxyAddress")
	private Object sipProxyAddress;
	@JsonProperty("state")
	private Object state;
	@JsonProperty("streetAddress")
	private Object streetAddress;
	@JsonProperty("surname")
	private Object surname;
	@JsonProperty("telephoneNumber")
	private Object telephoneNumber;
	@JsonProperty("usageLocation")
	private Object usageLocation;
	@JsonProperty("userPrincipalName")
	private String userPrincipalName;
	@JsonProperty("userType")
	private String userType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The odataType
	 */
	@JsonProperty("odata.type")
	public String getOdataType() {
		return odataType;
	}

	/**
	 * 
	 * @param odataType
	 * The odata.type
	 */
	@JsonProperty("odata.type")
	public void setOdataType(String odataType) {
		this.odataType = odataType;
	}

	/**
	 * 
	 * @return
	 * The objectType
	 */
	@JsonProperty("objectType")
	public String getObjectType() {
		return objectType;
	}

	/**
	 * 
	 * @param objectType
	 * The objectType
	 */
	@JsonProperty("objectType")
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	/**
	 * 
	 * @return
	 * The objectId
	 */
	@JsonProperty("objectId")
	public String getObjectId() {
		return objectId;
	}

	/**
	 * 
	 * @param objectId
	 * The objectId
	 */
	@JsonProperty("objectId")
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * 
	 * @return
	 * The deletionTimestamp
	 */
	@JsonProperty("deletionTimestamp")
	public Object getDeletionTimestamp() {
		return deletionTimestamp;
	}

	/**
	 * 
	 * @param deletionTimestamp
	 * The deletionTimestamp
	 */
	@JsonProperty("deletionTimestamp")
	public void setDeletionTimestamp(Object deletionTimestamp) {
		this.deletionTimestamp = deletionTimestamp;
	}

	/**
	 * 
	 * @return
	 * The accountEnabled
	 */
	@JsonProperty("accountEnabled")
	public Boolean getAccountEnabled() {
		return accountEnabled;
	}

	/**
	 * 
	 * @param accountEnabled
	 * The accountEnabled
	 */
	@JsonProperty("accountEnabled")
	public void setAccountEnabled(Boolean accountEnabled) {
		this.accountEnabled = accountEnabled;
	}

	/**
	 * 
	 * @return
	 * The assignedLicenses
	 */
	@JsonProperty("assignedLicenses")
	public List<Object> getAssignedLicenses() {
		return assignedLicenses;
	}

	/**
	 * 
	 * @param assignedLicenses
	 * The assignedLicenses
	 */
	@JsonProperty("assignedLicenses")
	public void setAssignedLicenses(List<Object> assignedLicenses) {
		this.assignedLicenses = assignedLicenses;
	}

	/**
	 * 
	 * @return
	 * The assignedPlans
	 */
	@JsonProperty("assignedPlans")
	public List<Object> getAssignedPlans() {
		return assignedPlans;
	}

	/**
	 * 
	 * @param assignedPlans
	 * The assignedPlans
	 */
	@JsonProperty("assignedPlans")
	public void setAssignedPlans(List<Object> assignedPlans) {
		this.assignedPlans = assignedPlans;
	}

	/**
	 * 
	 * @return
	 * The city
	 */
	@JsonProperty("city")
	public Object getCity() {
		return city;
	}

	/**
	 * 
	 * @param city
	 * The city
	 */
	@JsonProperty("city")
	public void setCity(Object city) {
		this.city = city;
	}

	/**
	 * 
	 * @return
	 * The companyName
	 */
	@JsonProperty("companyName")
	public Object getCompanyName() {
		return companyName;
	}

	/**
	 * 
	 * @param companyName
	 * The companyName
	 */
	@JsonProperty("companyName")
	public void setCompanyName(Object companyName) {
		this.companyName = companyName;
	}

	/**
	 * 
	 * @return
	 * The country
	 */
	@JsonProperty("country")
	public Object getCountry() {
		return country;
	}

	/**
	 * 
	 * @param country
	 * The country
	 */
	@JsonProperty("country")
	public void setCountry(Object country) {
		this.country = country;
	}

	/**
	 * 
	 * @return
	 * The department
	 */
	@JsonProperty("department")
	public Object getDepartment() {
		return department;
	}

	/**
	 * 
	 * @param department
	 * The department
	 */
	@JsonProperty("department")
	public void setDepartment(Object department) {
		this.department = department;
	}

	/**
	 * 
	 * @return
	 * The dirSyncEnabled
	 */
	@JsonProperty("dirSyncEnabled")
	public Object getDirSyncEnabled() {
		return dirSyncEnabled;
	}

	/**
	 * 
	 * @param dirSyncEnabled
	 * The dirSyncEnabled
	 */
	@JsonProperty("dirSyncEnabled")
	public void setDirSyncEnabled(Object dirSyncEnabled) {
		this.dirSyncEnabled = dirSyncEnabled;
	}

	/**
	 * 
	 * @return
	 * The displayName
	 */
	@JsonProperty("displayName")
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * @param displayName
	 * The displayName
	 */
	@JsonProperty("displayName")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 
	 * @return
	 * The facsimileTelephoneNumber
	 */
	@JsonProperty("facsimileTelephoneNumber")
	public Object getFacsimileTelephoneNumber() {
		return facsimileTelephoneNumber;
	}

	/**
	 * 
	 * @param facsimileTelephoneNumber
	 * The facsimileTelephoneNumber
	 */
	@JsonProperty("facsimileTelephoneNumber")
	public void setFacsimileTelephoneNumber(Object facsimileTelephoneNumber) {
		this.facsimileTelephoneNumber = facsimileTelephoneNumber;
	}

	/**
	 * 
	 * @return
	 * The givenName
	 */
	@JsonProperty("givenName")
	public Object getGivenName() {
		return givenName;
	}

	/**
	 * 
	 * @param givenName
	 * The givenName
	 */
	@JsonProperty("givenName")
	public void setGivenName(Object givenName) {
		this.givenName = givenName;
	}

	/**
	 * 
	 * @return
	 * The immutableId
	 */
	@JsonProperty("immutableId")
	public Object getImmutableId() {
		return immutableId;
	}

	/**
	 * 
	 * @param immutableId
	 * The immutableId
	 */
	@JsonProperty("immutableId")
	public void setImmutableId(Object immutableId) {
		this.immutableId = immutableId;
	}

	/**
	 * 
	 * @return
	 * The jobTitle
	 */
	@JsonProperty("jobTitle")
	public Object getJobTitle() {
		return jobTitle;
	}

	/**
	 * 
	 * @param jobTitle
	 * The jobTitle
	 */
	@JsonProperty("jobTitle")
	public void setJobTitle(Object jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * 
	 * @return
	 * The lastDirSyncTime
	 */
	@JsonProperty("lastDirSyncTime")
	public Object getLastDirSyncTime() {
		return lastDirSyncTime;
	}

	/**
	 * 
	 * @param lastDirSyncTime
	 * The lastDirSyncTime
	 */
	@JsonProperty("lastDirSyncTime")
	public void setLastDirSyncTime(Object lastDirSyncTime) {
		this.lastDirSyncTime = lastDirSyncTime;
	}

	/**
	 * 
	 * @return
	 * The mail
	 */
	@JsonProperty("mail")
	public String getMail() {
		return mail;
	}

	/**
	 * 
	 * @param mail
	 * The mail
	 */
	@JsonProperty("mail")
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * 
	 * @return
	 * The mailNickname
	 */
	@JsonProperty("mailNickname")
	public String getMailNickname() {
		return mailNickname;
	}

	/**
	 * 
	 * @param mailNickname
	 * The mailNickname
	 */
	@JsonProperty("mailNickname")
	public void setMailNickname(String mailNickname) {
		this.mailNickname = mailNickname;
	}

	/**
	 * 
	 * @return
	 * The mobile
	 */
	@JsonProperty("mobile")
	public Object getMobile() {
		return mobile;
	}

	/**
	 * 
	 * @param mobile
	 * The mobile
	 */
	@JsonProperty("mobile")
	public void setMobile(Object mobile) {
		this.mobile = mobile;
	}

	/**
	 * 
	 * @return
	 * The onPremisesSecurityIdentifier
	 */
	@JsonProperty("onPremisesSecurityIdentifier")
	public Object getOnPremisesSecurityIdentifier() {
		return onPremisesSecurityIdentifier;
	}

	/**
	 * 
	 * @param onPremisesSecurityIdentifier
	 * The onPremisesSecurityIdentifier
	 */
	@JsonProperty("onPremisesSecurityIdentifier")
	public void setOnPremisesSecurityIdentifier(Object onPremisesSecurityIdentifier) {
		this.onPremisesSecurityIdentifier = onPremisesSecurityIdentifier;
	}

	/**
	 * 
	 * @return
	 * The otherMails
	 */
	@JsonProperty("otherMails")
	public List<String> getOtherMails() {
		return otherMails;
	}

	/**
	 * 
	 * @param otherMails
	 * The otherMails
	 */
	@JsonProperty("otherMails")
	public void setOtherMails(List<String> otherMails) {
		this.otherMails = otherMails;
	}

	/**
	 * 
	 * @return
	 * The passwordPolicies
	 */
	@JsonProperty("passwordPolicies")
	public Object getPasswordPolicies() {
		return passwordPolicies;
	}

	/**
	 * 
	 * @param passwordPolicies
	 * The passwordPolicies
	 */
	@JsonProperty("passwordPolicies")
	public void setPasswordPolicies(Object passwordPolicies) {
		this.passwordPolicies = passwordPolicies;
	}

	/**
	 * 
	 * @return
	 * The passwordProfile
	 */
	@JsonProperty("passwordProfile")
	public Object getPasswordProfile() {
		return passwordProfile;
	}

	/**
	 * 
	 * @param passwordProfile
	 * The passwordProfile
	 */
	@JsonProperty("passwordProfile")
	public void setPasswordProfile(Object passwordProfile) {
		this.passwordProfile = passwordProfile;
	}

	/**
	 * 
	 * @return
	 * The physicalDeliveryOfficeName
	 */
	@JsonProperty("physicalDeliveryOfficeName")
	public Object getPhysicalDeliveryOfficeName() {
		return physicalDeliveryOfficeName;
	}

	/**
	 * 
	 * @param physicalDeliveryOfficeName
	 * The physicalDeliveryOfficeName
	 */
	@JsonProperty("physicalDeliveryOfficeName")
	public void setPhysicalDeliveryOfficeName(Object physicalDeliveryOfficeName) {
		this.physicalDeliveryOfficeName = physicalDeliveryOfficeName;
	}

	/**
	 * 
	 * @return
	 * The postalCode
	 */
	@JsonProperty("postalCode")
	public Object getPostalCode() {
		return postalCode;
	}

	/**
	 * 
	 * @param postalCode
	 * The postalCode
	 */
	@JsonProperty("postalCode")
	public void setPostalCode(Object postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * 
	 * @return
	 * The preferredLanguage
	 */
	@JsonProperty("preferredLanguage")
	public Object getPreferredLanguage() {
		return preferredLanguage;
	}

	/**
	 * 
	 * @param preferredLanguage
	 * The preferredLanguage
	 */
	@JsonProperty("preferredLanguage")
	public void setPreferredLanguage(Object preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	/**
	 * 
	 * @return
	 * The provisionedPlans
	 */
	@JsonProperty("provisionedPlans")
	public List<Object> getProvisionedPlans() {
		return provisionedPlans;
	}

	/**
	 * 
	 * @param provisionedPlans
	 * The provisionedPlans
	 */
	@JsonProperty("provisionedPlans")
	public void setProvisionedPlans(List<Object> provisionedPlans) {
		this.provisionedPlans = provisionedPlans;
	}

	/**
	 * 
	 * @return
	 * The provisioningErrors
	 */
	@JsonProperty("provisioningErrors")
	public List<Object> getProvisioningErrors() {
		return provisioningErrors;
	}

	/**
	 * 
	 * @param provisioningErrors
	 * The provisioningErrors
	 */
	@JsonProperty("provisioningErrors")
	public void setProvisioningErrors(List<Object> provisioningErrors) {
		this.provisioningErrors = provisioningErrors;
	}

	/**
	 * 
	 * @return
	 * The proxyAddresses
	 */
	@JsonProperty("proxyAddresses")
	public List<String> getProxyAddresses() {
		return proxyAddresses;
	}

	/**
	 * 
	 * @param proxyAddresses
	 * The proxyAddresses
	 */
	@JsonProperty("proxyAddresses")
	public void setProxyAddresses(List<String> proxyAddresses) {
		this.proxyAddresses = proxyAddresses;
	}

	/**
	 * 
	 * @return
	 * The sipProxyAddress
	 */
	@JsonProperty("sipProxyAddress")
	public Object getSipProxyAddress() {
		return sipProxyAddress;
	}

	/**
	 * 
	 * @param sipProxyAddress
	 * The sipProxyAddress
	 */
	@JsonProperty("sipProxyAddress")
	public void setSipProxyAddress(Object sipProxyAddress) {
		this.sipProxyAddress = sipProxyAddress;
	}

	/**
	 * 
	 * @return
	 * The state
	 */
	@JsonProperty("state")
	public Object getState() {
		return state;
	}

	/**
	 * 
	 * @param state
	 * The state
	 */
	@JsonProperty("state")
	public void setState(Object state) {
		this.state = state;
	}

	/**
	 * 
	 * @return
	 * The streetAddress
	 */
	@JsonProperty("streetAddress")
	public Object getStreetAddress() {
		return streetAddress;
	}

	/**
	 * 
	 * @param streetAddress
	 * The streetAddress
	 */
	@JsonProperty("streetAddress")
	public void setStreetAddress(Object streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * 
	 * @return
	 * The surname
	 */
	@JsonProperty("surname")
	public Object getSurname() {
		return surname;
	}

	/**
	 * 
	 * @param surname
	 * The surname
	 */
	@JsonProperty("surname")
	public void setSurname(Object surname) {
		this.surname = surname;
	}

	/**
	 * 
	 * @return
	 * The telephoneNumber
	 */
	@JsonProperty("telephoneNumber")
	public Object getTelephoneNumber() {
		return telephoneNumber;
	}

	/**
	 * 
	 * @param telephoneNumber
	 * The telephoneNumber
	 */
	@JsonProperty("telephoneNumber")
	public void setTelephoneNumber(Object telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	/**
	 * 
	 * @return
	 * The usageLocation
	 */
	@JsonProperty("usageLocation")
	public Object getUsageLocation() {
		return usageLocation;
	}

	/**
	 * 
	 * @param usageLocation
	 * The usageLocation
	 */
	@JsonProperty("usageLocation")
	public void setUsageLocation(Object usageLocation) {
		this.usageLocation = usageLocation;
	}

	/**
	 * 
	 * @return
	 * The userPrincipalName
	 */
	@JsonProperty("userPrincipalName")
	public String getUserPrincipalName() {
		return userPrincipalName;
	}

	/**
	 * 
	 * @param userPrincipalName
	 * The userPrincipalName
	 */
	@JsonProperty("userPrincipalName")
	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}

	/**
	 * 
	 * @return
	 * The userType
	 */
	@JsonProperty("userType")
	public String getUserType() {
		return userType;
	}

	/**
	 * 
	 * @param userType
	 * The userType
	 */
	@JsonProperty("userType")
	public void setUserType(String userType) {
		this.userType = userType;
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
