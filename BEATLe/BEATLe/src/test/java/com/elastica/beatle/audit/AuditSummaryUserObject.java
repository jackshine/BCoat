package com.elastica.beatle.audit;

public class AuditSummaryUserObject {
	
	private String serviceUser;
	private long userSession;
	private long userTraffic;
	
	
	
	@Override
	public String toString() {
		return "AuditSummaryUserObject [serviceUser=" + serviceUser + ", userSession=" + userSession + ", userTraffic="
				+ userTraffic + "]";
	}
	public String getServiceUser() {
		return serviceUser;
	}
	public void setServiceUser(String serviceUser) {
		this.serviceUser = serviceUser;
	}
	public long getUserSession() {
		return userSession;
	}
	public void setUserSession(long userSession) {
		this.userSession = userSession;
	}
	public long getUserTraffic() {
		return userTraffic;
	}
	public void setUserTraffic(long userTraffic) {
		this.userTraffic = userTraffic;
	}
	
	
	

}
