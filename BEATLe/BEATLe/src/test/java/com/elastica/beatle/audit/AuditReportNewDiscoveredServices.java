package com.elastica.beatle.audit;

public class AuditReportNewDiscoveredServices {
	
	
	private long medium_risk_services;
	private long users;
	private long sessions;
	private long downloads;
	private long mostused_services;
	private long locations;
	private long total_services;
	private long uploads;
	private long high_risk_services;
	private long total_traffic;
	private long categories;
	
	
	public long getMedium_risk_services() {
		return medium_risk_services;
	}
	public void setMedium_risk_services(long medium_risk_services) {
		this.medium_risk_services = medium_risk_services;
	}
	public long getUsers() {
		return users;
	}
	public void setUsers(long users) {
		this.users = users;
	}
	public long getSessions() {
		return sessions;
	}
	public void setSessions(long sessions) {
		this.sessions = sessions;
	}
	public long getDownloads() {
		return downloads;
	}
	public void setDownloads(long downloads) {
		this.downloads = downloads;
	}
	public long getMostused_services() {
		return mostused_services;
	}
	public void setMostused_services(long mostused_services) {
		this.mostused_services = mostused_services;
	}
	public long getLocations() {
		return locations;
	}
	public void setLocations(long locations) {
		this.locations = locations;
	}
	public long getTotal_services() {
		return total_services;
	}
	public void setTotal_services(long total_services) {
		this.total_services = total_services;
	}
	public long getUploads() {
		return uploads;
	}
	public void setUploads(long uploads) {
		this.uploads = uploads;
	}
	public long getHigh_risk_services() {
		return high_risk_services;
	}
	public void setHigh_risk_services(long high_risk_services) {
		this.high_risk_services = high_risk_services;
	}
	public long getTotal_traffic() {
		return total_traffic;
	}
	public void setTotal_traffic(long total_traffic) {
		this.total_traffic = total_traffic;
	}
	public long getCategories() {
		return categories;
	}
	public void setCategories(long categories) {
		this.categories = categories;
	}
	
	@Override
	public String toString() {
		return "AuditReportNewDiscoveredServices [medium_risk_services=" + medium_risk_services + ", users=" + users
				+ ", sessions=" + sessions + ", downloads=" + downloads + ", mostused_services=" + mostused_services
				+ ", locations=" + locations + ", total_services=" + total_services + ", uploads=" + uploads
				+ ", high_risk_services=" + high_risk_services + ", total_traffic=" + total_traffic + ", categories="
				+ categories + "]";
	}
	
	
	
	

}
