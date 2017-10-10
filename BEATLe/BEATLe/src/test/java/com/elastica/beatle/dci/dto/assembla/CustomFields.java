package com.elastica.beatle.dci.dto.assembla;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class CustomFields {

	@JsonProperty("QA Verified?")
	private String qAVerified;
	@JsonProperty("Dev Assignee")
	private String devAssignee;
	@JsonProperty("Dev Reviewer")
	private String devReviewer;
	@JsonProperty("QA Assignee")
	private String qAAssignee;
	@JsonProperty("QA Reviewer")
	private String qAReviewer;
	@JsonProperty("Browser")
	private String browser;
	@JsonProperty("Component")
	private String component;
	@JsonProperty("Sub-component")
	private String subComponent;
	@JsonProperty("Customer Request or Defect?")
	private String customerRequestOrDefect;
	@JsonProperty("Customer Ticket or Name")
	private String customerTicketOrName;
	@JsonProperty("Defect Severity")
	private String defectSeverity;
	@JsonProperty("Environment")
	private String environment;
	@JsonProperty("Ticket Type")
	private String ticketType;
	@JsonProperty("Release Gating for Target Version?")
	private String releaseGatingForTargetVersion;
	@JsonProperty("Regression?")
	private String regression;
	@JsonProperty("Ticket(s) causing the regression")
	private String ticketSCausingTheRegression;
	@JsonProperty("Reported Version")
	private String reportedVersion;
	@JsonProperty("Target Version")
	private String targetVersion;
	@JsonProperty("Fixed Version")
	private String fixedVersion;
	@JsonProperty("Fix LOC")
	private String fixLOC;
	@JsonProperty("APIs Changed?")
	private String aPIsChanged;
	@JsonProperty("Test Case Id")
	private String testCaseId;
	@JsonProperty("Category")
	private String category;
	@JsonProperty("Sub-Category")
	private String subCategory;
	@JsonProperty("KB Article Status")
	private String kBArticleStatus;
	@JsonProperty("KB Article Zendesk link")
	private String kBArticleZendeskLink;
	@JsonProperty("Gatelet Root Cause")
	private String gateletRootCause;
	@JsonProperty("Activity Type")
	private String activityType;
	@JsonProperty("Object Type")
	private String objectType;
	@JsonProperty("Rank")
	private String rank;
	@JsonProperty("SaaS App")
	private String saaSApp;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The qAVerified
	 */
	@JsonProperty("QA Verified?")
	public String getQAVerified() {
		return qAVerified;
	}

	/**
	 * 
	 * @param qAVerified
	 * The QA Verified?
	 */
	@JsonProperty("QA Verified?")
	public void setQAVerified(String qAVerified) {
		this.qAVerified = qAVerified;
	}

	/**
	 * 
	 * @return
	 * The devAssignee
	 */
	@JsonProperty("Dev Assignee")
	public String getDevAssignee() {
		return devAssignee;
	}

	/**
	 * 
	 * @param devAssignee
	 * The Dev Assignee
	 */
	@JsonProperty("Dev Assignee")
	public void setDevAssignee(String devAssignee) {
		this.devAssignee = devAssignee;
	}

	/**
	 * 
	 * @return
	 * The devReviewer
	 */
	@JsonProperty("Dev Reviewer")
	public String getDevReviewer() {
		return devReviewer;
	}

	/**
	 * 
	 * @param devReviewer
	 * The Dev Reviewer
	 */
	@JsonProperty("Dev Reviewer")
	public void setDevReviewer(String devReviewer) {
		this.devReviewer = devReviewer;
	}

	/**
	 * 
	 * @return
	 * The qAAssignee
	 */
	@JsonProperty("QA Assignee")
	public String getQAAssignee() {
		return qAAssignee;
	}

	/**
	 * 
	 * @param qAAssignee
	 * The QA Assignee
	 */
	@JsonProperty("QA Assignee")
	public void setQAAssignee(String qAAssignee) {
		this.qAAssignee = qAAssignee;
	}

	/**
	 * 
	 * @return
	 * The qAReviewer
	 */
	@JsonProperty("QA Reviewer")
	public String getQAReviewer() {
		return qAReviewer;
	}

	/**
	 * 
	 * @param qAReviewer
	 * The QA Reviewer
	 */
	@JsonProperty("QA Reviewer")
	public void setQAReviewer(String qAReviewer) {
		this.qAReviewer = qAReviewer;
	}

	/**
	 * 
	 * @return
	 * The browser
	 */
	@JsonProperty("Browser")
	public String getBrowser() {
		return browser;
	}

	/**
	 * 
	 * @param browser
	 * The Browser
	 */
	@JsonProperty("Browser")
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * 
	 * @return
	 * The component
	 */
	@JsonProperty("Component")
	public String getComponent() {
		return component;
	}

	/**
	 * 
	 * @param component
	 * The Component
	 */
	@JsonProperty("Component")
	public void setComponent(String component) {
		this.component = component;
	}

	/**
	 * 
	 * @return
	 * The subComponent
	 */
	@JsonProperty("Sub-component")
	public String getSubComponent() {
		return subComponent;
	}

	/**
	 * 
	 * @param subComponent
	 * The Sub-component
	 */
	@JsonProperty("Sub-component")
	public void setSubComponent(String subComponent) {
		this.subComponent = subComponent;
	}

	/**
	 * 
	 * @return
	 * The customerRequestOrDefect
	 */
	@JsonProperty("Customer Request or Defect?")
	public String getCustomerRequestOrDefect() {
		return customerRequestOrDefect;
	}

	/**
	 * 
	 * @param customerRequestOrDefect
	 * The Customer Request or Defect?
	 */
	@JsonProperty("Customer Request or Defect?")
	public void setCustomerRequestOrDefect(String customerRequestOrDefect) {
		this.customerRequestOrDefect = customerRequestOrDefect;
	}

	/**
	 * 
	 * @return
	 * The customerTicketOrName
	 */
	@JsonProperty("Customer Ticket or Name")
	public String getCustomerTicketOrName() {
		return customerTicketOrName;
	}

	/**
	 * 
	 * @param customerTicketOrName
	 * The Customer Ticket or Name
	 */
	@JsonProperty("Customer Ticket or Name")
	public void setCustomerTicketOrName(String customerTicketOrName) {
		this.customerTicketOrName = customerTicketOrName;
	}

	/**
	 * 
	 * @return
	 * The defectSeverity
	 */
	@JsonProperty("Defect Severity")
	public String getDefectSeverity() {
		return defectSeverity;
	}

	/**
	 * 
	 * @param defectSeverity
	 * The Defect Severity
	 */
	@JsonProperty("Defect Severity")
	public void setDefectSeverity(String defectSeverity) {
		this.defectSeverity = defectSeverity;
	}

	/**
	 * 
	 * @return
	 * The environment
	 */
	@JsonProperty("Environment")
	public String getEnvironment() {
		return environment;
	}

	/**
	 * 
	 * @param environment
	 * The Environment
	 */
	@JsonProperty("Environment")
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * 
	 * @return
	 * The ticketType
	 */
	@JsonProperty("Ticket Type")
	public String getTicketType() {
		return ticketType;
	}

	/**
	 * 
	 * @param ticketType
	 * The Ticket Type
	 */
	@JsonProperty("Ticket Type")
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 * 
	 * @return
	 * The releaseGatingForTargetVersion
	 */
	@JsonProperty("Release Gating for Target Version?")
	public String getReleaseGatingForTargetVersion() {
		return releaseGatingForTargetVersion;
	}

	/**
	 * 
	 * @param releaseGatingForTargetVersion
	 * The Release Gating for Target Version?
	 */
	@JsonProperty("Release Gating for Target Version?")
	public void setReleaseGatingForTargetVersion(String releaseGatingForTargetVersion) {
		this.releaseGatingForTargetVersion = releaseGatingForTargetVersion;
	}

	/**
	 * 
	 * @return
	 * The regression
	 */
	@JsonProperty("Regression?")
	public String getRegression() {
		return regression;
	}

	/**
	 * 
	 * @param regression
	 * The Regression?
	 */
	@JsonProperty("Regression?")
	public void setRegression(String regression) {
		this.regression = regression;
	}

	/**
	 * 
	 * @return
	 * The ticketSCausingTheRegression
	 */
	@JsonProperty("Ticket(s) causing the regression")
	public String getTicketSCausingTheRegression() {
		return ticketSCausingTheRegression;
	}

	/**
	 * 
	 * @param ticketSCausingTheRegression
	 * The Ticket(s) causing the regression
	 */
	@JsonProperty("Ticket(s) causing the regression")
	public void setTicketSCausingTheRegression(String ticketSCausingTheRegression) {
		this.ticketSCausingTheRegression = ticketSCausingTheRegression;
	}

	/**
	 * 
	 * @return
	 * The reportedVersion
	 */
	@JsonProperty("Reported Version")
	public String getReportedVersion() {
		return reportedVersion;
	}

	/**
	 * 
	 * @param reportedVersion
	 * The Reported Version
	 */
	@JsonProperty("Reported Version")
	public void setReportedVersion(String reportedVersion) {
		this.reportedVersion = reportedVersion;
	}

	/**
	 * 
	 * @return
	 * The targetVersion
	 */
	@JsonProperty("Target Version")
	public String getTargetVersion() {
		return targetVersion;
	}

	/**
	 * 
	 * @param targetVersion
	 * The Target Version
	 */
	@JsonProperty("Target Version")
	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	/**
	 * 
	 * @return
	 * The fixedVersion
	 */
	@JsonProperty("Fixed Version")
	public String getFixedVersion() {
		return fixedVersion;
	}

	/**
	 * 
	 * @param fixedVersion
	 * The Fixed Version
	 */
	@JsonProperty("Fixed Version")
	public void setFixedVersion(String fixedVersion) {
		this.fixedVersion = fixedVersion;
	}

	/**
	 * 
	 * @return
	 * The fixLOC
	 */
	@JsonProperty("Fix LOC")
	public String getFixLOC() {
		return fixLOC;
	}

	/**
	 * 
	 * @param fixLOC
	 * The Fix LOC
	 */
	@JsonProperty("Fix LOC")
	public void setFixLOC(String fixLOC) {
		this.fixLOC = fixLOC;
	}

	/**
	 * 
	 * @return
	 * The aPIsChanged
	 */
	@JsonProperty("APIs Changed?")
	public String getAPIsChanged() {
		return aPIsChanged;
	}

	/**
	 * 
	 * @param aPIsChanged
	 * The APIs Changed?
	 */
	@JsonProperty("APIs Changed?")
	public void setAPIsChanged(String aPIsChanged) {
		this.aPIsChanged = aPIsChanged;
	}

	/**
	 * 
	 * @return
	 * The testCaseId
	 */
	@JsonProperty("Test Case Id")
	public String getTestCaseId() {
		return testCaseId;
	}

	/**
	 * 
	 * @param testCaseId
	 * The Test Case Id
	 */
	@JsonProperty("Test Case Id")
	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	/**
	 * 
	 * @return
	 * The category
	 */
	@JsonProperty("Category")
	public String getCategory() {
		return category;
	}

	/**
	 * 
	 * @param category
	 * The Category
	 */
	@JsonProperty("Category")
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 
	 * @return
	 * The subCategory
	 */
	@JsonProperty("Sub-Category")
	public String getSubCategory() {
		return subCategory;
	}

	/**
	 * 
	 * @param subCategory
	 * The Sub-Category
	 */
	@JsonProperty("Sub-Category")
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	/**
	 * 
	 * @return
	 * The kBArticleStatus
	 */
	@JsonProperty("KB Article Status")
	public String getKBArticleStatus() {
		return kBArticleStatus;
	}

	/**
	 * 
	 * @param kBArticleStatus
	 * The KB Article Status
	 */
	@JsonProperty("KB Article Status")
	public void setKBArticleStatus(String kBArticleStatus) {
		this.kBArticleStatus = kBArticleStatus;
	}

	/**
	 * 
	 * @return
	 * The kBArticleZendeskLink
	 */
	@JsonProperty("KB Article Zendesk link")
	public String getKBArticleZendeskLink() {
		return kBArticleZendeskLink;
	}

	/**
	 * 
	 * @param kBArticleZendeskLink
	 * The KB Article Zendesk link
	 */
	@JsonProperty("KB Article Zendesk link")
	public void setKBArticleZendeskLink(String kBArticleZendeskLink) {
		this.kBArticleZendeskLink = kBArticleZendeskLink;
	}

	/**
	 * 
	 * @return
	 * The gateletRootCause
	 */
	@JsonProperty("Gatelet Root Cause")
	public String getGateletRootCause() {
		return gateletRootCause;
	}

	/**
	 * 
	 * @param gateletRootCause
	 * The Gatelet Root Cause
	 */
	@JsonProperty("Gatelet Root Cause")
	public void setGateletRootCause(String gateletRootCause) {
		this.gateletRootCause = gateletRootCause;
	}

	/**
	 * 
	 * @return
	 * The activityType
	 */
	@JsonProperty("Activity Type")
	public String getActivityType() {
		return activityType;
	}

	/**
	 * 
	 * @param activityType
	 * The Activity Type
	 */
	@JsonProperty("Activity Type")
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * 
	 * @return
	 * The objectType
	 */
	@JsonProperty("Object Type")
	public String getObjectType() {
		return objectType;
	}

	/**
	 * 
	 * @param objectType
	 * The Object Type
	 */
	@JsonProperty("Object Type")
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	/**
	 * 
	 * @return
	 * The rank
	 */
	@JsonProperty("Rank")
	public String getRank() {
		return rank;
	}

	/**
	 * 
	 * @param rank
	 * The Rank
	 */
	@JsonProperty("Rank")
	public void setRank(String rank) {
		this.rank = rank;
	}

	/**
	 * 
	 * @return
	 * The saaSApp
	 */
	@JsonProperty("SaaS App")
	public String getSaaSApp() {
		return saaSApp;
	}

	/**
	 * 
	 * @param saaSApp
	 * The SaaS App
	 */
	@JsonProperty("SaaS App")
	public void setSaaSApp(String saaSApp) {
		this.saaSApp = saaSApp;
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