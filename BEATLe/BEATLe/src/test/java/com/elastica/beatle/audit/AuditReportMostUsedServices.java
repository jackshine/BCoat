package com.elastica.beatle.audit;

public class AuditReportMostUsedServices {
	
	private long riskyServices;
	private long users;
	private long sessions;
	private long total_services;
	private long total_traffic;
	private long downloads;
	private long uploads;
	private long total_new_discoServices;
	private long locations;
	private long categories;
	private long high_risky_services;
	private long med_risky_services;
	
	
	@Override
	public String toString() {
		return "AuditReportMostUsedServices [riskyServices=" + riskyServices + ", users=" + users + ", sessions="
				+ sessions + ", total_services=" + total_services + ", total_traffic=" + total_traffic
				+ ", downloads=" + downloads + ", uploads=" + uploads + ", total_new_discoServices="
				+ total_new_discoServices + ", locations=" + locations + ", categories=" + categories
				+ ", high_risky_services=" + high_risky_services + ", med_risky_services=" + med_risky_services
				+ "]";
	}

	public long getHigh_risky_services() {
		return high_risky_services;
	}

	public void setHigh_risky_services(long high_risky_services) {
		this.high_risky_services = high_risky_services;
	}

	public long getMed_risky_services() {
		return med_risky_services;
	}

	public void setMed_risky_services(long med_risky_services) {
		this.med_risky_services = med_risky_services;
	}

	
	
	public long getRiskyServices() {
		return riskyServices;
	}
	public void setRiskyServices(long riskyServices) {
		this.riskyServices = riskyServices;
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
	public long getTotal_services() {
		return total_services;
	}
	public void setTotal_services(long total_services) {
		this.total_services = total_services;
	}
	public long getTotal_traffic() {
		return total_traffic;
	}
	public void setTotal_traffic(long total_traffic) {
		this.total_traffic = total_traffic;
	}
	public long getDownloads() {
		return downloads;
	}
	public void setDownloads(long downloads) {
		this.downloads = downloads;
	}
	public long getUploads() {
		return uploads;
	}
	public void setUploads(long uploads) {
		this.uploads = uploads;
	}
	public long getTotal_new_discoServices() {
		return total_new_discoServices;
	}
	public void setTotal_new_discoServices(long total_new_discoServices) {
		this.total_new_discoServices = total_new_discoServices;
	}
	public long getLocations() {
		return locations;
	}
	public void setLocations(long locations) {
		this.locations = locations;
	}
	public long getCategories() {
		return categories;
	}
	public void setCategories(long categories) {
		this.categories = categories;
	}

}
