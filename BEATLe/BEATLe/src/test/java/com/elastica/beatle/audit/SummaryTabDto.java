package com.elastica.beatle.audit;

import java.util.Map;

public class SummaryTabDto {
	
	
	
	private Map<String,String> serviceUsersMap;
	private String total_users;
	private Map<String,String> locationsMap;
	
	
	public Map<String, String> getServiceUsersMap() {
		return serviceUsersMap;
	}
	public void setServiceUsersMap(Map<String, String> serviceUsersMap) {
		this.serviceUsersMap = serviceUsersMap;
	}
	public String getTotal_users() {
		return total_users;
	}
	public void setTotal_users(String total_users) {
		this.total_users = total_users;
	}
	public Map<String, String> getLocationsMap() {
		return locationsMap;
	}
	public void setLocationsMap(Map<String, String> locationsMap) {
		this.locationsMap = locationsMap;
	}
	
	@Override
	public String toString() {
		return "SummaryTabDto [serviceUsersMap=" + serviceUsersMap + ", total_users=" + total_users + ", locationsMap="
				+ locationsMap + "]";
	}
	
	
	
	
	

}
