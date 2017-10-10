package com.elastica.beatle.audit;

import java.util.List;
import java.util.Set;

public class GoldenSetData {
	
	
	protected String serviceName;
	protected String serviceID;
	protected Set<String> usernameSet;
	protected List<String> totalBytesList;
	protected List<String> uploadBytesList;
	protected List<String> downloadBytesList;
	protected List<String> uniqueLocationsList;
	protected List<String> sessionForEachUserList;
	
	
	

	@Override
	public String toString() {
		return "GoldenSetData [serviceName=" + serviceName + ", serviceID=" + serviceID + ", usernameSet=" + usernameSet
				+ ", totalBytesList=" + totalBytesList + ", uploadBytesList=" + uploadBytesList + ", downloadBytesList="
				+ downloadBytesList + ", uniqueLocationsList=" + uniqueLocationsList + ", sessionForEachUserList="
				+ sessionForEachUserList + "]";
	}
	public String getServiceName() {
		return serviceName;
	}
	public String getServiceID() {
		return serviceID;
	}
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Set<String> getUsernameSet() {
		return usernameSet;
	}
	public void setUsernameSet(Set<String> usernameSet) {
		this.usernameSet = usernameSet;
	}
	public List<String> getTotalBytesList() {
		return totalBytesList;
	}
	public void setTotalBytesList(List<String> totalBytesList) {
		this.totalBytesList = totalBytesList;
	}
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
	
	


}
