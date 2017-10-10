package com.elastica.beatle.protect;

public class ProtectAlertFilter {

	private String service;
	private String user;
	private String policyType;
	private String policy;
	private String severity;
	private String fileName;
	boolean isSearchFilter;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isSearchFilter() {
		return isSearchFilter;
	}

	public void setSearchFilter(boolean isSearchFilter) {
		this.isSearchFilter = isSearchFilter;
	}
}
