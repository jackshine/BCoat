package com.elastica.beatle.audit;

public class AuditSummaryServicesTabObj {
	
	private String serviceRating;
	private String serviceName;
	private long serviceSessions;
	private long serviceTotalUploads;
	private long serviceTotalDownloads;
    private long servicesUsersCount;
    private long serviceDestinations;
    private long serviceAvgDuration;
	@Override
	public String toString() {
		return "AuditSummaryServicesTabObj [serviceRating=" + serviceRating + ", serviceName=" + serviceName
				+ ", serviceSessions=" + serviceSessions + ", serviceTotalUploads=" + serviceTotalUploads
				+ ", serviceTotalDownloads=" + serviceTotalDownloads + ", servicesUsersCount=" + servicesUsersCount
				+ ", serviceDestinations=" + serviceDestinations + ", serviceAvgDuration=" + serviceAvgDuration + "]";
	}
	public String getServiceRating() {
		return serviceRating;
	}
	public void setServiceRating(String serviceRating) {
		this.serviceRating = serviceRating;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public long getServiceSessions() {
		return serviceSessions;
	}
	public void setServiceSessions(long serviceSessions) {
		this.serviceSessions = serviceSessions;
	}
	public long getServiceTotalUploads() {
		return serviceTotalUploads;
	}
	public void setServiceTotalUploads(long serviceTotalUploads) {
		this.serviceTotalUploads = serviceTotalUploads;
	}
	public long getServiceTotalDownloads() {
		return serviceTotalDownloads;
	}
	public void setServiceTotalDownloads(long serviceTotalDownloads) {
		this.serviceTotalDownloads = serviceTotalDownloads;
	}
	public long getServicesUsersCount() {
		return servicesUsersCount;
	}
	public void setServicesUsersCount(long servicesUsersCount) {
		this.servicesUsersCount = servicesUsersCount;
	}
	public long getServiceDestinations() {
		return serviceDestinations;
	}
	public void setServiceDestinations(long serviceDestinations) {
		this.serviceDestinations = serviceDestinations;
	}
	public long getServiceAvgDuration() {
		return serviceAvgDuration;
	}
	public void setServiceAvgDuration(long serviceAvgDuration) {
		this.serviceAvgDuration = serviceAvgDuration;
	}
    
    
    
    
    
}
