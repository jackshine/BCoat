package com.elastica.beatle.audit;

public class AuditSummaryUsersTabObj {
	

private String user;
private long userSessionsCount;
private long userTotalUpload;
private long userTotalDownload;
private long userServicesCount;
private long userDestinationsCount;



@Override
public String toString() {
	return "AuditSummaryUsersTabObj [user=" + user + ", userSessionsCount=" + userSessionsCount + ", userTotalUpload="
			+ userTotalUpload + ", userTotalDownload=" + userTotalDownload + ", userServicesCount=" + userServicesCount
			+ ", userDestinationsCount=" + userDestinationsCount + "]";
}
public String getUser() {
	return user;
}
public void setUser(String user) {
	this.user = user;
}
public long getUserSessionsCount() {
	return userSessionsCount;
}
public void setUserSessionsCount(long userSessionsCount) {
	this.userSessionsCount = userSessionsCount;
}
public long getUserTotalUpload() {
	return userTotalUpload;
}
public void setUserTotalUpload(long userTotalUpload) {
	this.userTotalUpload = userTotalUpload;
}
public long getUserTotalDownload() {
	return userTotalDownload;
}
public void setUserTotalDownload(long userTotalDownload) {
	this.userTotalDownload = userTotalDownload;
}
public long getUserServicesCount() {
	return userServicesCount;
}
public void setUserServicesCount(long userServicesCount) {
	this.userServicesCount = userServicesCount;
}
public long getUserDestinationsCount() {
	return userDestinationsCount;
}
public void setUserDestinationsCount(long userDestinationsCount) {
	this.userDestinationsCount = userDestinationsCount;
}





}
