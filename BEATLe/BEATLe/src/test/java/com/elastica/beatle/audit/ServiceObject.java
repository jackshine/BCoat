package com.elastica.beatle.audit;

public class ServiceObject {
	
	private String serviceId;
	private String serviceName;
	private String serviceResearchCompleted;
	private String serviceUrl;
	private String serviceCategory;
	
	
	public String getServiceCategory() {
		return serviceCategory;
	}
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceResearchCompleted() {
		return serviceResearchCompleted;
	}
	public void setServiceResearchCompleted(String serviceResearchCompleted) {
		this.serviceResearchCompleted = serviceResearchCompleted;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	@Override
	public String toString() {
		return "ServiceObject [serviceId=" + serviceId + ", serviceName=" + serviceName + ", serviceResearchCompleted="
				+ serviceResearchCompleted + ", serviceUrl=" + serviceUrl + ", serviceCategory=" + serviceCategory
				+ "]";
	}

	

}
