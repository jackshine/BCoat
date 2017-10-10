package com.elastica.beatle.audit;

import java.util.List;

public class AuditSummaryTopUsedServices {
	
	private String servicename;
	private long service_brr;
	private long service_user_count;
	private List<AuditSummaryUserObject> summaryUserObjList;
	private long userLocations;
	@Override
	public String toString() {
		return "AuditSummaryTopUsedServices [servicename=" + servicename + ", service_brr=" + service_brr
				+ ", service_user_count=" + service_user_count + ", summaryUserObjList=" + summaryUserObjList
				+ ", userLocations=" + userLocations + "]";
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public long getService_brr() {
		return service_brr;
	}
	public void setService_brr(long service_brr) {
		this.service_brr = service_brr;
	}
	public long getService_user_count() {
		return service_user_count;
	}
	public void setService_user_count(long service_user_count) {
		this.service_user_count = service_user_count;
	}
	public List<AuditSummaryUserObject> getSummaryUserObjList() {
		return summaryUserObjList;
	}
	public void setSummaryUserObjList(List<AuditSummaryUserObject> summaryUserObjList) {
		this.summaryUserObjList = summaryUserObjList;
	}
	public long getUserLocations() {
		return userLocations;
	}
	public void setUserLocations(long userLocations) {
		this.userLocations = userLocations;
	}
	
	
	

}
