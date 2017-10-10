package com.elastica.beatle.audit;

import java.util.List;
import java.util.Set;

public class CompanyBRRDto {
	
	String service;
	String serviceId;
	String users_count;
	Set<String> usernameSet;
	List<String> uploadBytesList;
	List<String> downloadBytesList;
	List<String> uniqueLocationsList;
	List<String> sessionForEachUserList;
	public List<String> getUploadBytesList() {
		return uploadBytesList;
	}
	public void setUploadBytesList(List<String> uploadBytesList) {
		this.uploadBytesList = uploadBytesList;
	}
	public List<String> getDownloadBytesList() {
		return downloadBytesList;
	}
	public void setDownloadBytesList(List<String> downloadBytesList) {
		this.downloadBytesList = downloadBytesList;
	}
	public List<String> getUniqueLocationsList() {
		return uniqueLocationsList;
	}
	public void setUniqueLocationsList(List<String> uniqueLocationsList) {
		this.uniqueLocationsList = uniqueLocationsList;
	}
	public List<String> getSessionForEachUserList() {
		return sessionForEachUserList;
	}
	public void setSessionForEachUserList(List<String> sessionForEachUserList) {
		this.sessionForEachUserList = sessionForEachUserList;
	}
	double log_users_count;
	double brr_log_users_count;
	String brr;
	
	
	
	public String getUsers_count() {
		return users_count;
	}
	public void setUsers_count(String users_count) {
		this.users_count = users_count;
	}
	public String getBrr() {
		return brr;
	}
	public Set<String> getUsernameSet() {
		return usernameSet;
	}
	public void setUsernameSet(Set<String> usernameSet) {
		this.usernameSet = usernameSet;
	}
	public void setBrr(String brr) {
		this.brr = brr;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}

	public double getLog_users_count() {
		return log_users_count;
	}
	public void setLog_users_count(double log_users_count) {
		this.log_users_count = log_users_count;
	}
	public double getBrr_log_users_count() {
		return brr_log_users_count;
	}
	public void setBrr_log_users_count(double brr_log_users_count) {
		this.brr_log_users_count = brr_log_users_count;
	}
	@Override
	public String toString() {
		return "CompanyBRRDto [service=" + service + ", serviceId=" + serviceId + ", users_count=" + users_count
				+ ", usernameSet=" + usernameSet + ", uploadBytesList=" + uploadBytesList + ", downloadBytesList="
				+ downloadBytesList + ", uniqueLocationsList=" + uniqueLocationsList + ", sessionForEachUserList="
				+ sessionForEachUserList + ", log_users_count=" + log_users_count + ", brr_log_users_count="
				+ brr_log_users_count + ", brr=" + brr + "]";
	}

	

}
