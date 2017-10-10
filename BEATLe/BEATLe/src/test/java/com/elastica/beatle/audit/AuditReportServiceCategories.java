package com.elastica.beatle.audit;

import java.util.Map;

public class AuditReportServiceCategories {
	
	private long high_risky_categories;
	private long med_risky_categories;
	private long total_categories;
	private Map<String,String> risky_Categories_onData;
	private Map<String,Long> risky_Categories_onDataCount;
	private Map<String,Long> risky_Categories_onSessions;
	private Map<String,Long> risky_Categories_onSessionsCount;
	private Map<String,Long> risky_Categories_onUsers;
	private Map<String,Long> risky_Categories_onUsersCount;
	@Override
	public String toString() {
		return "AuditReportServiceCategories [high_risky_categories=" + high_risky_categories
				+ ", med_risky_categories=" + med_risky_categories + ", total_categories=" + total_categories
				+ ", risky_Categories_onData=" + risky_Categories_onData + ", risky_Categories_onDataCount="
				+ risky_Categories_onDataCount + ", risky_Categories_onSessions=" + risky_Categories_onSessions
				+ ", risky_Categories_onSessionsCount=" + risky_Categories_onSessionsCount
				+ ", risky_Categories_onUsers=" + risky_Categories_onUsers + ", risky_Categories_onUsersCount="
				+ risky_Categories_onUsersCount + "]";
	}
	public long getHigh_risky_categories() {
		return high_risky_categories;
	}
	public void setHigh_risky_categories(long high_risky_categories) {
		this.high_risky_categories = high_risky_categories;
	}
	public long getMed_risky_categories() {
		return med_risky_categories;
	}
	public void setMed_risky_categories(long med_risky_categories) {
		this.med_risky_categories = med_risky_categories;
	}
	public long getTotal_categories() {
		return total_categories;
	}
	public void setTotal_categories(long total_categories) {
		this.total_categories = total_categories;
	}
	public Map<String, String> getRisky_Categories_onData() {
		return risky_Categories_onData;
	}
	public void setRisky_Categories_onData(Map<String, String> risky_Categories_onData) {
		this.risky_Categories_onData = risky_Categories_onData;
	}
	public Map<String, Long> getRisky_Categories_onDataCount() {
		return risky_Categories_onDataCount;
	}
	public void setRisky_Categories_onDataCount(Map<String, Long> risky_Categories_onDataCount) {
		this.risky_Categories_onDataCount = risky_Categories_onDataCount;
	}
	public Map<String, Long> getRisky_Categories_onSessions() {
		return risky_Categories_onSessions;
	}
	public void setRisky_Categories_onSessions(Map<String, Long> risky_Categories_onSessions) {
		this.risky_Categories_onSessions = risky_Categories_onSessions;
	}
	public Map<String, Long> getRisky_Categories_onSessionsCount() {
		return risky_Categories_onSessionsCount;
	}
	public void setRisky_Categories_onSessionsCount(Map<String, Long> risky_Categories_onSessionsCount) {
		this.risky_Categories_onSessionsCount = risky_Categories_onSessionsCount;
	}
	public Map<String, Long> getRisky_Categories_onUsers() {
		return risky_Categories_onUsers;
	}
	public void setRisky_Categories_onUsers(Map<String, Long> risky_Categories_onUsers) {
		this.risky_Categories_onUsers = risky_Categories_onUsers;
	}
	public Map<String, Long> getRisky_Categories_onUsersCount() {
		return risky_Categories_onUsersCount;
	}
	public void setRisky_Categories_onUsersCount(Map<String, Long> risky_Categories_onUsersCount) {
		this.risky_Categories_onUsersCount = risky_Categories_onUsersCount;
	}
	

}
