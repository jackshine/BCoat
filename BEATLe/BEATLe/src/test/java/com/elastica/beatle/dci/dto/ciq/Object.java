package com.elastica.beatle.dci.dto.ciq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class Object {

	@JsonProperty("groups")
	private List<Group> groups = new ArrayList<Group>();
	@JsonProperty("modified_by")
	private java.lang.Object modifiedBy;
	@JsonProperty("description")
	private String description;
	@JsonProperty("policy_list")
	private List<java.lang.Object> policyList = new ArrayList<java.lang.Object>();
	@JsonProperty("rules")
	private Integer rules;
	@JsonProperty("api_enabled")
	private Boolean apiEnabled;
	@JsonProperty("org_unit")
	private List<java.lang.Object> orgUnit = new ArrayList<java.lang.Object>();
	@JsonProperty("created_by")
	private String createdBy;
	@JsonProperty("threshold")
	private Double threshold;
	@JsonProperty("created_on")
	private String createdOn;
	@JsonProperty("profile_name")
	private String profileName;
	@JsonProperty("version")
	private Integer version;
	@JsonProperty("modified_on")
	private java.lang.Object modifiedOn;
	@JsonProperty("domains")
	private List<String> domains = new ArrayList<String>();
	@JsonProperty("id")
	private String id;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return
	 * The groups
	 */
	@JsonProperty("groups")
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * 
	 * @param groups
	 * The groups
	 */
	@JsonProperty("groups")
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	/**
	 * 
	 * @return
	 * The modifiedBy
	 */
	@JsonProperty("modified_by")
	public java.lang.Object getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * 
	 * @param modifiedBy
	 * The modified_by
	 */
	@JsonProperty("modified_by")
	public void setModifiedBy(java.lang.Object modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * 
	 * @return
	 * The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 * The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 * The policyList
	 */
	@JsonProperty("policy_list")
	public List<java.lang.Object> getPolicyList() {
		return policyList;
	}

	/**
	 * 
	 * @param policyList
	 * The policy_list
	 */
	@JsonProperty("policy_list")
	public void setPolicyList(List<java.lang.Object> policyList) {
		this.policyList = policyList;
	}

	/**
	 * 
	 * @return
	 * The rules
	 */
	@JsonProperty("rules")
	public Integer getRules() {
		return rules;
	}

	/**
	 * 
	 * @param rules
	 * The rules
	 */
	@JsonProperty("rules")
	public void setRules(Integer rules) {
		this.rules = rules;
	}

	/**
	 * 
	 * @return
	 * The apiEnabled
	 */
	@JsonProperty("api_enabled")
	public Boolean getApiEnabled() {
		return apiEnabled;
	}

	/**
	 * 
	 * @param apiEnabled
	 * The api_enabled
	 */
	@JsonProperty("api_enabled")
	public void setApiEnabled(Boolean apiEnabled) {
		this.apiEnabled = apiEnabled;
	}

	/**
	 * 
	 * @return
	 * The orgUnit
	 */
	@JsonProperty("org_unit")
	public List<java.lang.Object> getOrgUnit() {
		return orgUnit;
	}

	/**
	 * 
	 * @param orgUnit
	 * The org_unit
	 */
	@JsonProperty("org_unit")
	public void setOrgUnit(List<java.lang.Object> orgUnit) {
		this.orgUnit = orgUnit;
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
	 * The threshold
	 */
	@JsonProperty("threshold")
	public Double getThreshold() {
		return threshold;
	}

	/**
	 * 
	 * @param threshold
	 * The threshold
	 */
	@JsonProperty("threshold")
	public void setThreshold(Double threshold) {
		this.threshold = threshold;
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
	 * The profileName
	 */
	@JsonProperty("profile_name")
	public String getProfileName() {
		return profileName;
	}

	/**
	 * 
	 * @param profileName
	 * The profile_name
	 */
	@JsonProperty("profile_name")
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("version")
	public Integer getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("version")
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * 
	 * @return
	 * The modifiedOn
	 */
	@JsonProperty("modified_on")
	public java.lang.Object getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * 
	 * @param modifiedOn
	 * The modified_on
	 */
	@JsonProperty("modified_on")
	public void setModifiedOn(java.lang.Object modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * 
	 * @return
	 * The domains
	 */
	@JsonProperty("domains")
	public List<String> getDomains() {
		return domains;
	}

	/**
	 * 
	 * @param domains
	 * The domains
	 */
	@JsonProperty("domains")
	public void setDomains(List<String> domains) {
		this.domains = domains;
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

	@JsonAnyGetter
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}

