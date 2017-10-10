package com.universal.unifiedapi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class User {

	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("network_id")
	private Integer networkId;
	@JsonProperty("state")
	private String state;
	@JsonProperty("guid")
	private Object guid;
	@JsonProperty("job_title")
	private Object jobTitle;
	@JsonProperty("location")
	private Object location;
	@JsonProperty("significant_other")
	private Object significantOther;
	@JsonProperty("kids_names")
	private Object kidsNames;
	@JsonProperty("interests")
	private Object interests;
	@JsonProperty("summary")
	private Object summary;
	@JsonProperty("expertise")
	private Object expertise;
	@JsonProperty("full_name")
	private String fullName;
	@JsonProperty("activated_at")
	private String activatedAt;
	@JsonProperty("auto_activated")
	private Boolean autoActivated;
	@JsonProperty("show_ask_for_photo")
	private Boolean showAskForPhoto;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("last_name")
	private String lastName;
	@JsonProperty("network_name")
	private String networkName;
	@JsonProperty("network_domains")
	private List<String> networkDomains = new ArrayList<String>();
	@JsonProperty("url")
	private String url;
	@JsonProperty("web_url")
	private String webUrl;
	@JsonProperty("name")
	private String name;
	@JsonProperty("mugshot_url")
	private String mugshotUrl;
	@JsonProperty("mugshot_url_template")
	private String mugshotUrlTemplate;
	@JsonProperty("birth_date")
	private String birthDate;
	@JsonProperty("timezone")
	private String timezone;
	@JsonProperty("external_urls")
	private List<Object> externalUrls = new ArrayList<Object>();
	@JsonProperty("admin")
	private String admin;
	@JsonProperty("verified_admin")
	private String verifiedAdmin;
	@JsonProperty("supervisor_admin")
	private String supervisorAdmin;
	@JsonProperty("can_broadcast")
	private String canBroadcast;
	@JsonProperty("department")
	private Object department;
	@JsonProperty("email")
	private String email;
	@JsonProperty("can_create_new_network")
	private Boolean canCreateNewNetwork;
	@JsonProperty("can_browse_external_networks")
	private Boolean canBrowseExternalNetworks;
	@JsonProperty("previous_companies")
	private List<Object> previousCompanies = new ArrayList<Object>();
	@JsonProperty("schools")
	private List<Object> schools = new ArrayList<Object>();
	@JsonProperty("contact")
	private Contact contact;
	@JsonProperty("stats")
	private Stats stats;
	@JsonProperty("settings")
	private Settings settings;
	@JsonProperty("show_invite_lightbox")
	private Boolean showInviteLightbox;
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
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The networkId
	 */
	@JsonProperty("network_id")
	public Integer getNetworkId() {
		return networkId;
	}

	/**
	 * 
	 * @param networkId
	 * The network_id
	 */
	@JsonProperty("network_id")
	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

	/**
	 * 
	 * @return
	 * The state
	 */
	@JsonProperty("state")
	public String getState() {
		return state;
	}

	/**
	 * 
	 * @param state
	 * The state
	 */
	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 
	 * @return
	 * The guid
	 */
	@JsonProperty("guid")
	public Object getGuid() {
		return guid;
	}

	/**
	 * 
	 * @param guid
	 * The guid
	 */
	@JsonProperty("guid")
	public void setGuid(Object guid) {
		this.guid = guid;
	}

	/**
	 * 
	 * @return
	 * The jobTitle
	 */
	@JsonProperty("job_title")
	public Object getJobTitle() {
		return jobTitle;
	}

	/**
	 * 
	 * @param jobTitle
	 * The job_title
	 */
	@JsonProperty("job_title")
	public void setJobTitle(Object jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * 
	 * @return
	 * The location
	 */
	@JsonProperty("location")
	public Object getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location
	 * The location
	 */
	@JsonProperty("location")
	public void setLocation(Object location) {
		this.location = location;
	}

	/**
	 * 
	 * @return
	 * The significantOther
	 */
	@JsonProperty("significant_other")
	public Object getSignificantOther() {
		return significantOther;
	}

	/**
	 * 
	 * @param significantOther
	 * The significant_other
	 */
	@JsonProperty("significant_other")
	public void setSignificantOther(Object significantOther) {
		this.significantOther = significantOther;
	}

	/**
	 * 
	 * @return
	 * The kidsNames
	 */
	@JsonProperty("kids_names")
	public Object getKidsNames() {
		return kidsNames;
	}

	/**
	 * 
	 * @param kidsNames
	 * The kids_names
	 */
	@JsonProperty("kids_names")
	public void setKidsNames(Object kidsNames) {
		this.kidsNames = kidsNames;
	}

	/**
	 * 
	 * @return
	 * The interests
	 */
	@JsonProperty("interests")
	public Object getInterests() {
		return interests;
	}

	/**
	 * 
	 * @param interests
	 * The interests
	 */
	@JsonProperty("interests")
	public void setInterests(Object interests) {
		this.interests = interests;
	}

	/**
	 * 
	 * @return
	 * The summary
	 */
	@JsonProperty("summary")
	public Object getSummary() {
		return summary;
	}

	/**
	 * 
	 * @param summary
	 * The summary
	 */
	@JsonProperty("summary")
	public void setSummary(Object summary) {
		this.summary = summary;
	}

	/**
	 * 
	 * @return
	 * The expertise
	 */
	@JsonProperty("expertise")
	public Object getExpertise() {
		return expertise;
	}

	/**
	 * 
	 * @param expertise
	 * The expertise
	 */
	@JsonProperty("expertise")
	public void setExpertise(Object expertise) {
		this.expertise = expertise;
	}

	/**
	 * 
	 * @return
	 * The fullName
	 */
	@JsonProperty("full_name")
	public String getFullName() {
		return fullName;
	}

	/**
	 * 
	 * @param fullName
	 * The full_name
	 */
	@JsonProperty("full_name")
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * 
	 * @return
	 * The activatedAt
	 */
	@JsonProperty("activated_at")
	public String getActivatedAt() {
		return activatedAt;
	}

	/**
	 * 
	 * @param activatedAt
	 * The activated_at
	 */
	@JsonProperty("activated_at")
	public void setActivatedAt(String activatedAt) {
		this.activatedAt = activatedAt;
	}

	/**
	 * 
	 * @return
	 * The autoActivated
	 */
	@JsonProperty("auto_activated")
	public Boolean getAutoActivated() {
		return autoActivated;
	}

	/**
	 * 
	 * @param autoActivated
	 * The auto_activated
	 */
	@JsonProperty("auto_activated")
	public void setAutoActivated(Boolean autoActivated) {
		this.autoActivated = autoActivated;
	}

	/**
	 * 
	 * @return
	 * The showAskForPhoto
	 */
	@JsonProperty("show_ask_for_photo")
	public Boolean getShowAskForPhoto() {
		return showAskForPhoto;
	}

	/**
	 * 
	 * @param showAskForPhoto
	 * The show_ask_for_photo
	 */
	@JsonProperty("show_ask_for_photo")
	public void setShowAskForPhoto(Boolean showAskForPhoto) {
		this.showAskForPhoto = showAskForPhoto;
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
	 * The networkName
	 */
	@JsonProperty("network_name")
	public String getNetworkName() {
		return networkName;
	}

	/**
	 * 
	 * @param networkName
	 * The network_name
	 */
	@JsonProperty("network_name")
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	/**
	 * 
	 * @return
	 * The networkDomains
	 */
	@JsonProperty("network_domains")
	public List<String> getNetworkDomains() {
		return networkDomains;
	}

	/**
	 * 
	 * @param networkDomains
	 * The network_domains
	 */
	@JsonProperty("network_domains")
	public void setNetworkDomains(List<String> networkDomains) {
		this.networkDomains = networkDomains;
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
	 * 
	 * @return
	 * The webUrl
	 */
	@JsonProperty("web_url")
	public String getWebUrl() {
		return webUrl;
	}

	/**
	 * 
	 * @param webUrl
	 * The web_url
	 */
	@JsonProperty("web_url")
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
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
	 * The mugshotUrl
	 */
	@JsonProperty("mugshot_url")
	public String getMugshotUrl() {
		return mugshotUrl;
	}

	/**
	 * 
	 * @param mugshotUrl
	 * The mugshot_url
	 */
	@JsonProperty("mugshot_url")
	public void setMugshotUrl(String mugshotUrl) {
		this.mugshotUrl = mugshotUrl;
	}

	/**
	 * 
	 * @return
	 * The mugshotUrlTemplate
	 */
	@JsonProperty("mugshot_url_template")
	public String getMugshotUrlTemplate() {
		return mugshotUrlTemplate;
	}

	/**
	 * 
	 * @param mugshotUrlTemplate
	 * The mugshot_url_template
	 */
	@JsonProperty("mugshot_url_template")
	public void setMugshotUrlTemplate(String mugshotUrlTemplate) {
		this.mugshotUrlTemplate = mugshotUrlTemplate;
	}

	/**
	 * 
	 * @return
	 * The birthDate
	 */
	@JsonProperty("birth_date")
	public String getBirthDate() {
		return birthDate;
	}

	/**
	 * 
	 * @param birthDate
	 * The birth_date
	 */
	@JsonProperty("birth_date")
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * 
	 * @return
	 * The timezone
	 */
	@JsonProperty("timezone")
	public String getTimezone() {
		return timezone;
	}

	/**
	 * 
	 * @param timezone
	 * The timezone
	 */
	@JsonProperty("timezone")
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * 
	 * @return
	 * The externalUrls
	 */
	@JsonProperty("external_urls")
	public List<Object> getExternalUrls() {
		return externalUrls;
	}

	/**
	 * 
	 * @param externalUrls
	 * The external_urls
	 */
	@JsonProperty("external_urls")
	public void setExternalUrls(List<Object> externalUrls) {
		this.externalUrls = externalUrls;
	}

	/**
	 * 
	 * @return
	 * The admin
	 */
	@JsonProperty("admin")
	public String getAdmin() {
		return admin;
	}

	/**
	 * 
	 * @param admin
	 * The admin
	 */
	@JsonProperty("admin")
	public void setAdmin(String admin) {
		this.admin = admin;
	}

	/**
	 * 
	 * @return
	 * The verifiedAdmin
	 */
	@JsonProperty("verified_admin")
	public String getVerifiedAdmin() {
		return verifiedAdmin;
	}

	/**
	 * 
	 * @param verifiedAdmin
	 * The verified_admin
	 */
	@JsonProperty("verified_admin")
	public void setVerifiedAdmin(String verifiedAdmin) {
		this.verifiedAdmin = verifiedAdmin;
	}

	/**
	 * 
	 * @return
	 * The supervisorAdmin
	 */
	@JsonProperty("supervisor_admin")
	public String getSupervisorAdmin() {
		return supervisorAdmin;
	}

	/**
	 * 
	 * @param supervisorAdmin
	 * The supervisor_admin
	 */
	@JsonProperty("supervisor_admin")
	public void setSupervisorAdmin(String supervisorAdmin) {
		this.supervisorAdmin = supervisorAdmin;
	}

	/**
	 * 
	 * @return
	 * The canBroadcast
	 */
	@JsonProperty("can_broadcast")
	public String getCanBroadcast() {
		return canBroadcast;
	}

	/**
	 * 
	 * @param canBroadcast
	 * The can_broadcast
	 */
	@JsonProperty("can_broadcast")
	public void setCanBroadcast(String canBroadcast) {
		this.canBroadcast = canBroadcast;
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
	 * The canCreateNewNetwork
	 */
	@JsonProperty("can_create_new_network")
	public Boolean getCanCreateNewNetwork() {
		return canCreateNewNetwork;
	}

	/**
	 * 
	 * @param canCreateNewNetwork
	 * The can_create_new_network
	 */
	@JsonProperty("can_create_new_network")
	public void setCanCreateNewNetwork(Boolean canCreateNewNetwork) {
		this.canCreateNewNetwork = canCreateNewNetwork;
	}

	/**
	 * 
	 * @return
	 * The canBrowseExternalNetworks
	 */
	@JsonProperty("can_browse_external_networks")
	public Boolean getCanBrowseExternalNetworks() {
		return canBrowseExternalNetworks;
	}

	/**
	 * 
	 * @param canBrowseExternalNetworks
	 * The can_browse_external_networks
	 */
	@JsonProperty("can_browse_external_networks")
	public void setCanBrowseExternalNetworks(Boolean canBrowseExternalNetworks) {
		this.canBrowseExternalNetworks = canBrowseExternalNetworks;
	}

	/**
	 * 
	 * @return
	 * The previousCompanies
	 */
	@JsonProperty("previous_companies")
	public List<Object> getPreviousCompanies() {
		return previousCompanies;
	}

	/**
	 * 
	 * @param previousCompanies
	 * The previous_companies
	 */
	@JsonProperty("previous_companies")
	public void setPreviousCompanies(List<Object> previousCompanies) {
		this.previousCompanies = previousCompanies;
	}

	/**
	 * 
	 * @return
	 * The schools
	 */
	@JsonProperty("schools")
	public List<Object> getSchools() {
		return schools;
	}

	/**
	 * 
	 * @param schools
	 * The schools
	 */
	@JsonProperty("schools")
	public void setSchools(List<Object> schools) {
		this.schools = schools;
	}

	/**
	 * 
	 * @return
	 * The contact
	 */
	@JsonProperty("contact")
	public Contact getContact() {
		return contact;
	}

	/**
	 * 
	 * @param contact
	 * The contact
	 */
	@JsonProperty("contact")
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * 
	 * @return
	 * The stats
	 */
	@JsonProperty("stats")
	public Stats getStats() {
		return stats;
	}

	/**
	 * 
	 * @param stats
	 * The stats
	 */
	@JsonProperty("stats")
	public void setStats(Stats stats) {
		this.stats = stats;
	}

	/**
	 * 
	 * @return
	 * The settings
	 */
	@JsonProperty("settings")
	public Settings getSettings() {
		return settings;
	}

	/**
	 * 
	 * @param settings
	 * The settings
	 */
	@JsonProperty("settings")
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * 
	 * @return
	 * The showInviteLightbox
	 */
	@JsonProperty("show_invite_lightbox")
	public Boolean getShowInviteLightbox() {
		return showInviteLightbox;
	}

	/**
	 * 
	 * @param showInviteLightbox
	 * The show_invite_lightbox
	 */
	@JsonProperty("show_invite_lightbox")
	public void setShowInviteLightbox(Boolean showInviteLightbox) {
		this.showInviteLightbox = showInviteLightbox;
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
