package com.elastica.action.protect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProtectDTO {
	
	private String policyName;
	private String policyDescription;
	private List<String> applicationList;
	private List<String> userList;
	private List<String> groupList;
	private List<String> domainList;
	private List<String> userWhiteList;
	private List<String> groupWhiteList;
	private int threadScore=0;
	private List<String> userRecipientList;
	private List<String> groupRecipientList;
	private List<String> userRecipientWhiteList;
	private List<String> groupRecipientWhiteList;
	private List<String> geoIPScope;
	private List<String> geoIPWhiteList;
	private List<String> vulnerabilityType;
	private List<String> contentProfiles;
	private List<String> filenamePattern;
	private List<String> fileScopeTypes;
	private List<String> fileScopeWhitelist;
	private List<String> accountType;
	private boolean status = false;
	private String policyType;
	private List<String> transferType;
	private int largerSize = 0;
	private int smallerSize = 0;
	private String severity = "high";
	private List<Map<String, String>> platformList;
	private List<Map<String, String>> browserList;
	private List<String> actionList;
	private String activities;
	
	public String getPolicyName(){
		return policyName;
	}
	
	public void setPolicyName(String policyName){
		this.policyName = policyName;
	}
	
	public String getPolicyDescription(){
		return policyDescription;
	}
	
	public void setPolicyDescription(String policyDescription){
		this.policyDescription = policyDescription;
	}
	
	public List<String> getApplicationList(){
		return applicationList;
	}
	
	public void setApplicationList(List<String> applicationList){
		this.applicationList = applicationList;
	}
	
	public List<String> getUserList(){
		return userList;
	}
	
	public void setUserList(List<String> userList){
		this.userList = userList;
	}
	
	public List<String> getGroupList(){
		return groupList;
	}
	
	public void setGroupList(List<String> groupList){
		this.groupList = groupList;
	}
	
	public List<String> getDomainList(){
		return domainList;
	}
	
	public void setDomainList(List<String> domainList){
		this.domainList = domainList;
	}
	
	public List<String> getUserWhiteList(){
		return userWhiteList;
	}
	
	public void setUserWhiteList(List<String> userWhiteList){
		this.userWhiteList = userWhiteList;
	}
	
	public List<String> getGroupWhiteList(){
		return groupWhiteList;
	}
	
	public void setGroupWhiteList(List<String> groupWhiteList){
		this.groupWhiteList = groupWhiteList;
	}
	
	public int getThreadScore(){
		return threadScore;
	}
	
	public void setThreadScore(int threadScore){
		this.threadScore = threadScore;
	}
	
	public List<String> getUserRecipientList(){
		return userRecipientList;
	}
	
	public void setUserRecipientList(List<String> userRecipientList){
		this.userRecipientList = userRecipientList;
	}
	
	public List<String> getGroupRecipientList(){
		return groupRecipientList;
	}
	
	public void setGroupRecipientList(List<String> groupRecipientList){
		this.groupRecipientList = groupRecipientList;
	}
	
	public List<String> getUserRecipientWhiteList(){
		return userRecipientWhiteList;
	}
	
	public void setUserRecipientWhiteList(List<String> userRecipientWhiteList){
		this.userRecipientWhiteList = userRecipientWhiteList;
	}
	
	public List<String> getGroupRecipientWhiteList(){
		return groupRecipientWhiteList;
	}
	
	public void setGroupRecipientWhiteList(List<String> groupRecipientWhiteList){
		this.groupRecipientWhiteList = groupRecipientWhiteList;
	}
	
	public List<String> getGeoIPScope(){
		return geoIPScope;
	}
	
	public void setGeoIPScope(List<String> geoIPScope){
		this.geoIPScope = geoIPScope;
	}
	
	public List<String> getGeoIPWhiteList(){
		return geoIPWhiteList;
	}
	
	public void setGeoIPWhiteList(List<String> geoIPWhiteList){
		this.geoIPWhiteList = geoIPWhiteList;
	}
	
	public List<String> getVulnerabilityType(){
		return vulnerabilityType;
	}
	
	public void setVulnerabilityType(List<String> vulnerabilityType){
		this.vulnerabilityType = vulnerabilityType;
	}
	
	public List<String> getContentProfiles(){
		return contentProfiles;
	}
	
	public void setContentProfiles(List<String> contentProfiles){
		this.contentProfiles = contentProfiles;
	}
	
	public List<String> getFilenamePattern(){
		return filenamePattern;
	}
	
	public void setFilenamePattern(List<String> filenamePattern){
		this.filenamePattern = filenamePattern;
	}
	
	public List<String> getFileScopeTypes(){
		return fileScopeTypes;
	}
	
	public void setFileScopeTypes(List<String> fileScopeTypes){
		this.fileScopeTypes = fileScopeTypes;
	}
	
	public List<String> getFileScopeWhiteList(){
		return fileScopeWhitelist;
	}
	
	public void setFileScopeWhiteList(List<String> fileScopeWhitelist){
		this.fileScopeWhitelist = fileScopeWhitelist;
	}
	
	public List<String> getAccountType(){
		return accountType;
	}
	
	public void setAccountType(List<String> accountType){
		this.accountType = accountType;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public void setStatus(boolean status){
		this.status = status;
	}
	
	public String getPolicyType(){
		return policyType;
	}
	
	public void setPolicytype(String policyType){
		this.policyType = policyType;
	}
	
	public List<String> getTransferType(){
		return transferType;
	}
	
	public void setTransferType(List<String> transferType){
		this.transferType = transferType;
	}
	
	public int getSmallerSize(){
		return smallerSize;
	}
	
	public void setSmallerSize(int smallerSize){
		this.smallerSize = smallerSize;
	}
	
	public int getLargerSize(){
		return largerSize;
	}
	
	public void setLargerSize(int largerSize){
		this.largerSize = largerSize;
	}
	
	public String getSeverity(){
		return severity;
	}
	
	public void setSeverity(String severity){
		this.severity = severity;
	}
	
	public void setPlatformList(List<Map<String, String>> platformList){
		this.platformList = platformList;
	}
	
	public List<Map<String, String>> getPlatformList(){
		return platformList;
	}
	
	public void setBrowserList(List<Map<String, String>> browserList){
		this.browserList = browserList;
	}
	
	public List<Map<String, String>> getBrowserList(){
		return browserList;
	}
	
	public List<String> getActionList(){
		return actionList;
	}
	
	public List<String> setAllActionList(List<String> actionList){
		actionList.add("SEVERITY_LEVEL");
		return actionList;
	}
	
	public void setActionList(List<String> actionList){
		this.actionList = actionList;
		setAllActionList(actionList);
	}
	
	public String getActivities(){
		return activities;
	}
	
	public void setActivities(String activities){
		this.activities = activities;
	}
}
