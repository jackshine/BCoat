package com.elastica.beatle.audit;

public class AuditSummaryDestinationTabObj {


	
	private String destinationName;
	private String destinationCountry;
	private String destinationServicesCount;
	private String destinationLocationsCount;
	private String destinationTotalUploadCount;
	private String destinationTotalDownloadCount;
	private String destinationTotalUsersCount;
	
	
	
	
	@Override
	public String toString() {
		return "AuditSummaryDestinationTabObj [destinationName=" + destinationName + ", destinationCountry="
				+ destinationCountry + ", destinationServicesCount=" + destinationServicesCount
				+ ", destinationLocationsCount=" + destinationLocationsCount + ", destinationTotalUploadCount="
				+ destinationTotalUploadCount + ", destinationTotalDownloadCount=" + destinationTotalDownloadCount
				+ ", destinationTotalUsersCount=" + destinationTotalUsersCount + "]";
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getDestinationCountry() {
		return destinationCountry;
	}
	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}
	public String getDestinationServicesCount() {
		return destinationServicesCount;
	}
	public void setDestinationServicesCount(String destinationServicesCount) {
		this.destinationServicesCount = destinationServicesCount;
	}
	public String getDestinationLocationsCount() {
		return destinationLocationsCount;
	}
	public void setDestinationLocationsCount(String destinationLocationsCount) {
		this.destinationLocationsCount = destinationLocationsCount;
	}
	public String getDestinationTotalUploadCount() {
		return destinationTotalUploadCount;
	}
	public void setDestinationTotalUploadCount(String destinationTotalUploadCount) {
		this.destinationTotalUploadCount = destinationTotalUploadCount;
	}
	public String getDestinationTotalDownloadCount() {
		return destinationTotalDownloadCount;
	}
	public void setDestinationTotalDownloadCount(String destinationTotalDownloadCount) {
		this.destinationTotalDownloadCount = destinationTotalDownloadCount;
	}
	public String getDestinationTotalUsersCount() {
		return destinationTotalUsersCount;
	}
	public void setDestinationTotalUsersCount(String destinationTotalUsersCount) {
		this.destinationTotalUsersCount = destinationTotalUsersCount;
	}
	
	
	
	
	
	
	
	
}
