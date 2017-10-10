package com.elastica.beatle.protect;

public class PolicyBean {
	
	private String policyName;
	private String policyDesc;
	private String policyType;
	private String cloudService;
	private String exposureType;
	private String fileType; 

	private String fileOwnerUser;
	private String fileOwnerGroup;
	private String domainName;
	private String fileOwnerUserException;
	private String fileOwnerGroupException;
	private String sharedWithUser;

	private String sharedWithGroup;
	private String sharedWithUserException;
	private String sharedWithGroupException;
	private String fileName;
	private String fileFormat;

	private String fileSize;
	private String riskType;
	private String ciqProfile;
	private String remediationActivity;
	private String fileExt;
	
	private String threatScore;
	private String activityScope;
	private String block;
	private String optionalField;
	private String severity;
	private String notifySms;
	private String ciqProfileException;

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getPolicyDesc() {
		return policyDesc;
	}

	public void setPolicyDesc(String policyDesc) {
		this.policyDesc = policyDesc;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getCloudService() {
		return cloudService;
	}

	public void setCloudService(String cloudService) {
		this.cloudService = cloudService;
	}

	public String getExposureType() {
		return exposureType;
	}

	public void setExposureType(String exposureType) {
		this.exposureType = exposureType;
	}

	public String getFileOwnerUser() {
		return fileOwnerUser;
	}

	public void setFileOwnerUser(String fileOwnerUser) {
		this.fileOwnerUser = fileOwnerUser;
	}

	public String getFileOwnerGroup() {
		return fileOwnerGroup;
	}

	public void setFileOwnerGroup(String fileOwnerGroup) {
		this.fileOwnerGroup = fileOwnerGroup;
	}
	
	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getFileOwnerUserException() {
		return fileOwnerUserException;
	}

	public void setFileOwnerUserException(String fileOwnerUserException) {
		this.fileOwnerUserException = fileOwnerUserException;
	}

	public String getFileOwnerGroupException() {
		return fileOwnerGroupException;
	}

	public void setFileOwnerGroupException(String fileOwnerGroupException) {
		this.fileOwnerGroupException = fileOwnerGroupException;
	}

	public String getSharedWithUser() {
		return sharedWithUser;
	}

	public void setSharedWithUser(String sharedWithUser) {
		this.sharedWithUser = sharedWithUser;
	}

	public String getSharedWithGroup() {
		return sharedWithGroup;
	}

	public void setSharedWithGroup(String sharedWithGroup) {
		this.sharedWithGroup = sharedWithGroup;
	}

	public String getSharedWithUserException() {
		return sharedWithUserException;
	}

	public void setSharedWithUserException(String sharedWithUserException) {
		this.sharedWithUserException = sharedWithUserException;
	}

	public String getSharedWithGroupException() {
		return sharedWithGroupException;
	}

	public void setSharedWithGroupException(String sharedWithGroupException) {
		this.sharedWithGroupException = sharedWithGroupException;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getCiqProfile() {
		return ciqProfile;
	}

	public void setCiqProfile(String ciqProfile) {
		this.ciqProfile = ciqProfile;
	}

	public String getRemediationActivity() {
		return remediationActivity;
	}

	public void setRemediationActivity(String remediationActivity) {
		this.remediationActivity = remediationActivity;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getThreatScore(){
		return threatScore;
	}
	
	public void setThreatScore(String threatScore){
		this.threatScore = threatScore;
	}
	
	public String getActivityScope(){
		return activityScope;
	}
	
	public void setActivityScope(String activityScope){
		this.activityScope = activityScope;
	}
	
	public String getBlock(){
		return block;
	}
	
	public void setBlock(String block){
		this.block = block;
	}
	
	public void setOptionalField(String optionalField){
		this.optionalField = optionalField;
	}
	
	public String getOptionalField(){
		return optionalField;
	}
	public void setSeverity(String severity){
		this.severity = severity;
	}
	
	public String getSeverity(){
		return severity;
	}

	public String getNotifySms() {
		return notifySms;
	}

	public void setNotifySms(String notifySms) {
		this.notifySms = notifySms;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getCiqProfileException() {
		return ciqProfileException;
	}

	public void setCiqProfileException(String ciqProfileException) {
		this.ciqProfileException = ciqProfileException;
	}
}
