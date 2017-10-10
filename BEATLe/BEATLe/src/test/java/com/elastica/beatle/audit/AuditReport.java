package com.elastica.beatle.audit;

import java.util.List;

public class AuditReport {
	
	private AuditReportTotals auditReportTotals;
	private AuditReportRiskyServices auditReportRiskyServices;
	private AuditReportMostUsedServices auditMostUsedServices;
	private AuditReportServiceCategories auditReportServiceCategories;
	private List<AuditReportServiceDetails>  auditReportServiceDetailsList;
	private AuditReportNewDiscoveredServices auditReportNewDiscoveredServices;
	
	
	
	public AuditReportNewDiscoveredServices getAuditReportNewDiscoveredServices() {
		return auditReportNewDiscoveredServices;
	}

	public void setAuditReportNewDiscoveredServices(AuditReportNewDiscoveredServices auditReportNewDiscoveredServices) {
		this.auditReportNewDiscoveredServices = auditReportNewDiscoveredServices;
	}

	public AuditReportServiceCategories getAuditReportServiceCategories() {
		return auditReportServiceCategories;
	}

	public void setAuditReportServiceCategories(AuditReportServiceCategories auditReportServiceCategories) {
		this.auditReportServiceCategories = auditReportServiceCategories;
	}

	public AuditReportMostUsedServices getAuditMostUsedServices() {
		return auditMostUsedServices;
	}

	public void setAuditMostUsedServices(AuditReportMostUsedServices auditMostUsedServices) {
		this.auditMostUsedServices = auditMostUsedServices;
	}

	
	public AuditReportRiskyServices getAuditReportRiskyServices() {
		return auditReportRiskyServices;
	}

	public void setAuditReportRiskyServices(AuditReportRiskyServices auditReportRiskyServices) {
		this.auditReportRiskyServices = auditReportRiskyServices;
	}

	public AuditReportTotals getAuditReportTotals() {
		return auditReportTotals;
	}

	public void setAuditReportTotals(AuditReportTotals auditReportTotals) {
		this.auditReportTotals = auditReportTotals;
	}
	
	
	public List<AuditReportServiceDetails> getAuditReportServiceDetailsList() {
		return auditReportServiceDetailsList;
	}

	public void setAuditReportServiceDetailsList(List<AuditReportServiceDetails> auditReportServiceDetailsList) {
		this.auditReportServiceDetailsList = auditReportServiceDetailsList;
	}

	@Override
	public String toString() {
		return "AuditReport [auditReportTotals=" + auditReportTotals + ", auditReportRiskyServices="
				+ auditReportRiskyServices + ", auditMostUsedServices=" + auditMostUsedServices
				+ ", auditReportServiceCategories=" + auditReportServiceCategories + ", auditReportServiceDetailsList="
				+ auditReportServiceDetailsList + ", auditReportNewDiscoveredServices="
				+ auditReportNewDiscoveredServices + "]";
	}
	
	
	
	

}
