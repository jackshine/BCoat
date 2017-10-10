package com.elastica.beatle.audit;

import java.util.HashMap;
import java.util.List;

public class AuditSummary {
	
	
	 private String auditScore;
	 private int saas_services_count;
	 private long users_count;
	 private int destination_count;
	 private int high_risky_services_count;
	 private int med_risky_services_count;
	 private long low_riksy_services_count;
	 private List<AuditSummaryTopRiskyServices> summaryTopRiskyServicesList;
	 private List<AuditSummaryTopUsedServices> summaryTopUsedServicesList;
	 private List<AuditSummaryServicesTabObj> summaryServicesTabObjList;
	 private List<AuditSummaryUsersTabObj> summaryUsersTabObjList;
	 private List<AuditSummaryDestinationTabObj> summaryDestinationsTabObjList;
	 private List<String> totalAuditServicesList;
	 private HashMap<String, String> serviceBrrMap;

	 

	@Override
	public String toString() {
		return "AuditSummary [auditScore=" + auditScore + ", saas_services_count=" + saas_services_count
				+ ", users_count=" + users_count + ", destination_count=" + destination_count
				+ ", high_risky_services_count=" + high_risky_services_count + ", med_risky_services_count="
				+ med_risky_services_count + ", low_riksy_services_count=" + low_riksy_services_count
				+ ", summaryTopRiskyServicesList=" + summaryTopRiskyServicesList + ", summaryTopUsedServicesList="
				+ summaryTopUsedServicesList + ", summaryServicesTabObjList=" + summaryServicesTabObjList
				+ ", summaryUsersTabObjList=" + summaryUsersTabObjList + ", summaryDestinationsTabObjList="
				+ summaryDestinationsTabObjList + ", totalAuditServicesList=" + totalAuditServicesList
				+ ", serviceBrrMap=" + serviceBrrMap + "]";
	}

	public List<String> getTotalAuditServicesList() {
		return totalAuditServicesList;
	}

	public void setTotalAuditServicesList(List<String> totalAuditServicesList) {
		this.totalAuditServicesList = totalAuditServicesList;
	}


	public String getAuditScore() {
		return auditScore;
	}

	public void setAuditScore(String auditScore) {
		this.auditScore = auditScore;
	}

	public int getSaas_services_count() {
		return saas_services_count;
	}

	public void setSaas_services_count(int saas_services_count) {
		this.saas_services_count = saas_services_count;
	}

	public long getUsers_count() {
		return users_count;
	}

	public void setUsers_count(long users_count) {
		this.users_count = users_count;
	}

	public int getDestination_count() {
		return destination_count;
	}

	public void setDestination_count(int destination_count) {
		this.destination_count = destination_count;
	}

	public int getMed_risky_services_count() {
		return med_risky_services_count;
	}

	public void setMed_risky_services_count(int med_risky_services_count) {
		this.med_risky_services_count = med_risky_services_count;
	}

	public int getHigh_risky_services_count() {
		return high_risky_services_count;
	}

	public void setHigh_risky_services_count(int high_risky_services_count) {
		this.high_risky_services_count = high_risky_services_count;
	}

	public long getLow_riksy_services_count() {
		return low_riksy_services_count;
	}

	public void setLow_riksy_services_count(long low_riksy_services_count) {
		this.low_riksy_services_count = low_riksy_services_count;
	}

	public List<AuditSummaryTopRiskyServices> getSummaryTopRiskyServicesList() {
		return summaryTopRiskyServicesList;
	}

	public void setSummaryTopRiskyServicesList(List<AuditSummaryTopRiskyServices> summaryTopRiskyServicesList) {
		this.summaryTopRiskyServicesList = summaryTopRiskyServicesList;
	}

	public List<AuditSummaryTopUsedServices> getSummaryTopUsedServicesList() {
		return summaryTopUsedServicesList;
	}

	public void setSummaryTopUsedServicesList(List<AuditSummaryTopUsedServices> summaryTopUsedServicesList) {
		this.summaryTopUsedServicesList = summaryTopUsedServicesList;
	}

	public List<AuditSummaryServicesTabObj> getSummaryServicesTabObjList() {
		return summaryServicesTabObjList;
	}

	public void setSummaryServicesTabObjList(List<AuditSummaryServicesTabObj> summaryServicesTabObjList) {
		this.summaryServicesTabObjList = summaryServicesTabObjList;
	}

	public List<AuditSummaryUsersTabObj> getSummaryUsersTabObjList() {
		return summaryUsersTabObjList;
	}

	public void setSummaryUsersTabObjList(List<AuditSummaryUsersTabObj> summaryUsersTabObjList) {
		this.summaryUsersTabObjList = summaryUsersTabObjList;
	}

	public List<AuditSummaryDestinationTabObj> getSummaryDestinationsTabObjList() {
		return summaryDestinationsTabObjList;
	}

	public void setSummaryDestinationsTabObjList(List<AuditSummaryDestinationTabObj> summaryDestinationsTabObjList) {
		this.summaryDestinationsTabObjList = summaryDestinationsTabObjList;
	}
	 
	 
	
	 
	public HashMap<String, String> getServiceBrrMap() {
		return serviceBrrMap;
	}

	public void setServiceBrrMap(HashMap<String, String> serviceBrrMap) {
		this.serviceBrrMap = serviceBrrMap;
	}
	 
	 
	 
	 

}
