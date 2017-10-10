package com.elastica.beatle.tests.securlets.salesforce;

public class SalesforceActivityLog {
		//Chatter logs
		private String contentDocumentUpdateLog		= "User updated 'Content Document' with Title '{filename}'";
		private String contentVersionUpdateLog		= "User updated 'Content Version' with Title '{filename}'";
		
		private String contentDocumentDeleteLog		= "User deleted 'Content Document' with Title '{filename}'";
		private String contentVersionDeleteLog		= "User deleted 'Content Version' with Title '{filename}'";
		
		//Lead logs
		private String leadCreateLog				= "User created 'Lead' with Name '{leadname}'";
		private String leadUpdateLog				= "User updated 'Lead' with Name '{leadname}'";
		private String leadDeleteLog				= "User deleted 'Lead' with Name '{leadname}'";
		
		//ContentInspection logs
		private String pciRiskLog			 		= "File {filename} has risk(s) - {risktype}";
		private String piiRiskLog			 		= "File {filename} has risk(s) - {risktype}";
		private String hipaaRiskLog			 		= "File {filename} has risk(s) - {risktype}";
		private String riskLog						= "File {filename} has risk(s) - {risktype}";
		
		
		//Account logs
		private String accountCreateLog				= "User created 'Account' with Name '{accountname}'";
		private String accountUpdateLog				= "User updated 'Account' with Name '{accountname}'";
		private String accountDeleteLog				= "User deleted 'Account' with Name '{accountname}'";
		
		//Opportunity logs
		private String opportunityCreateLog			= "User created 'Opportunity' with Name '{opportunityname}'";
		private String opportunityUpdateLog			= "User updated 'Opportunity' with Name '{opportunityname}'";
		private String opportunityDeleteLog			= "User deleted 'Opportunity' with Name '{opportunityname}'";		
		
		//case logs
		
		//"User created 'Case' with CaseNumber '00001063'",
		private String caseCreateLog				= "User created 'Case' with CaseNumber '{casenumber}'";
		private String caseUpdateLog				= "User updated 'Case' with CaseNumber '{casenumber}'";
		private String caseDeleteLog				= "User deleted 'Case' with CaseNumber '{casenumber}'";	
		private String caseUpdateFieldLog			= "User updated the '{fieldname}' field of 'CaseId': {caseid}";
		
		private String contentInspectionBypassLog	= "File: \"{filename}\" not downloaded for content inspection due to size limit: 100MB";
		
		//contact logs
		private String contactCreateLog				= "User created 'Contact' with Name '{contactname}'";
		private String contactUpdateLog				= "User updated 'Contact' with Name '{contactname}'";
		private String contactDeleteLog				= "User deleted 'Contact' with Name '{contactname}'";	

		//contract logs
		private String contractCreateLog			= "User created 'Contract' with ContractNumber '{contractno}'";
		private String contractUpdateLog			= "User updated 'Contract' with ContractNumber '{contractno}'";
		private String contractDeleteLog			= "User deleted 'Contract' with ContractNumber '{contractno}'";
		
		//solution logs
		private String solutionCreateLog			= "User created 'Solution' with SolutionName '{solutionname}'";
		private String solutionUpdateLog			= "User updated 'Solution' with SolutionName '{solutionname}'";
		private String solutionDeleteLog			= "User deleted 'Solution' with SolutionName '{solutionname}'";
		
		
		//campaign logs
		private String campaignCreateLog			= "User created 'Campaign' with Name '{campaignname}'";
		private String campaignUpdateLog			= "User updated 'Campaign' with Name '{campaignname}'";
		private String campaignDeleteLog			= "User deleted 'Campaign' with Name '{campaignname}'";
		
		//folder
		private String folderCreateLog				= "User created 'Folder' with Name '{foldername}'";
		private String folderUpdateLog				= "User updated 'Folder' with Name '{foldername}'";
		private String folderDeleteLog				= "User deleted 'Folder' with Name '{foldername}'";
		
		//report
		private String reportCreateLog				= "User created 'Report' with Name '{reportname}'";
		private String reportUpdateLog				= "User updated 'Report' with Name '{reportname}'";
		private String reportDeleteLog				= "User deleted 'Report' with Name '{reportname}'";
		
		//task
		private String taskCreateLog				= "User created 'Task' with Subject '{subject}'";
		private String taskUpdateLog				= "User updated 'Task' with Subject '{subject}'";
		private String taskDeleteLog				= "User deleted 'Task' with Subject '{subject}'";
		
		//event
		private String eventCreateLog				= "User created 'Event' with Subject '{subject}'";
		private String eventUpdateLog				= "User updated 'Event' with Subject '{subject}'";
		private String eventDeleteLog				= "User deleted 'Event' with Subject '{subject}'";

		
		
		/**
		 * @return the eventCreateLog
		 */
		public String getEventCreateLog() {
			return eventCreateLog;
		}
		/**
		 * @param eventCreateLog the eventCreateLog to set
		 */
		public void setEventCreateLog(String eventCreateLog) {
			this.eventCreateLog = eventCreateLog;
		}
		/**
		 * @return the eventUpdateLog
		 */
		public String getEventUpdateLog() {
			return eventUpdateLog;
		}
		/**
		 * @param eventUpdateLog the eventUpdateLog to set
		 */
		public void setEventUpdateLog(String eventUpdateLog) {
			this.eventUpdateLog = eventUpdateLog;
		}
		/**
		 * @return the eventDeleteLog
		 */
		public String getEventDeleteLog() {
			return eventDeleteLog;
		}
		/**
		 * @param eventDeleteLog the eventDeleteLog to set
		 */
		public void setEventDeleteLog(String eventDeleteLog) {
			this.eventDeleteLog = eventDeleteLog;
		}
		/**
		 * @return the taskCreateLog
		 */
		public String getTaskCreateLog() {
			return taskCreateLog;
		}
		/**
		 * @param taskCreateLog the taskCreateLog to set
		 */
		public void setTaskCreateLog(String taskCreateLog) {
			this.taskCreateLog = taskCreateLog;
		}
		/**
		 * @return the taskUpdateLog
		 */
		public String getTaskUpdateLog() {
			return taskUpdateLog;
		}
		/**
		 * @param taskUpdateLog the taskUpdateLog to set
		 */
		public void setTaskUpdateLog(String taskUpdateLog) {
			this.taskUpdateLog = taskUpdateLog;
		}
		/**
		 * @return the taskDeleteLog
		 */
		public String getTaskDeleteLog() {
			return taskDeleteLog;
		}
		/**
		 * @param taskDeleteLog the taskDeleteLog to set
		 */
		public void setTaskDeleteLog(String taskDeleteLog) {
			this.taskDeleteLog = taskDeleteLog;
		}
		
		
		/**
		 * @return the reportCreateLog
		 */
		public String getReportCreateLog() {
			return reportCreateLog;
		}
		/**
		 * @param reportCreateLog the reportCreateLog to set
		 */
		public void setReportCreateLog(String reportCreateLog) {
			this.reportCreateLog = reportCreateLog;
		}
		/**
		 * @return the reportUpdateLog
		 */
		public String getReportUpdateLog() {
			return reportUpdateLog;
		}
		/**
		 * @param reportUpdateLog the reportUpdateLog to set
		 */
		public void setReportUpdateLog(String reportUpdateLog) {
			this.reportUpdateLog = reportUpdateLog;
		}
		/**
		 * @return the reportDeleteLog
		 */
		public String getReportDeleteLog() {
			return reportDeleteLog;
		}
		/**
		 * @param reportDeleteLog the reportDeleteLog to set
		 */
		public void setReportDeleteLog(String reportDeleteLog) {
			this.reportDeleteLog = reportDeleteLog;
		}
		
		
		
		
		/**
		 * @return the folderCreateLog
		 */
		public String getFolderCreateLog() {
			return folderCreateLog;
		}
		/**
		 * @param folderCreateLog the folderCreateLog to set
		 */
		public void setFolderCreateLog(String folderCreateLog) {
			this.folderCreateLog = folderCreateLog;
		}
		/**
		 * @return the folderUpdateLog
		 */
		public String getFolderUpdateLog() {
			return folderUpdateLog;
		}
		/**
		 * @param folderUpdateLog the folderUpdateLog to set
		 */
		public void setFolderUpdateLog(String folderUpdateLog) {
			this.folderUpdateLog = folderUpdateLog;
		}
		/**
		 * @return the folderDeleteLog
		 */
		public String getFolderDeleteLog() {
			return folderDeleteLog;
		}
		/**
		 * @param folderDeleteLog the folderDeleteLog to set
		 */
		public void setFolderDeleteLog(String folderDeleteLog) {
			this.folderDeleteLog = folderDeleteLog;
		}
		
		
		
				
		
		/**
		 * @return the campaignCreateLog
		 */
		public String getCampaignCreateLog() {
			return campaignCreateLog;
		}
		/**
		 * @param campaignCreateLog the campaignCreateLog to set
		 */
		public void setCampaignCreateLog(String campaignCreateLog) {
			this.campaignCreateLog = campaignCreateLog;
		}
		/**
		 * @return the campaignUpdateLog
		 */
		public String getCampaignUpdateLog() {
			return campaignUpdateLog;
		}
		/**
		 * @param campaignUpdateLog the campaignUpdateLog to set
		 */
		public void setCampaignUpdateLog(String campaignUpdateLog) {
			this.campaignUpdateLog = campaignUpdateLog;
		}
		/**
		 * @return the campaignDeleteLog
		 */
		public String getCampaignDeleteLog() {
			return campaignDeleteLog;
		}
		/**
		 * @param campaignDeleteLog the campaignDeleteLog to set
		 */
		public void setCampaignDeleteLog(String campaignDeleteLog) {
			this.campaignDeleteLog = campaignDeleteLog;
		}
		/**
		 * @return the solutionCreateLog
		 */
		public String getSolutionCreateLog() {
			return solutionCreateLog;
		}
		/**
		 * @param solutionCreateLog the solutionCreateLog to set
		 */
		public void setSolutionCreateLog(String solutionCreateLog) {
			this.solutionCreateLog = solutionCreateLog;
		}
		/**
		 * @return the solutionUpdateLog
		 */
		public String getSolutionUpdateLog() {
			return solutionUpdateLog;
		}
		/**
		 * @param solutionUpdateLog the solutionUpdateLog to set
		 */
		public void setSolutionUpdateLog(String solutionUpdateLog) {
			this.solutionUpdateLog = solutionUpdateLog;
		}
		/**
		 * @return the solutionDeleteLog
		 */
		public String getSolutionDeleteLog() {
			return solutionDeleteLog;
		}
		/**
		 * @param solutionDeleteLog the solutionDeleteLog to set
		 */
		public void setSolutionDeleteLog(String solutionDeleteLog) {
			this.solutionDeleteLog = solutionDeleteLog;
		}
		

		
		/**
		 * @return the riskLog
		 */
		public String getRiskLog() {
			return riskLog;
		}
		/**
		 * @param riskLog the riskLog to set
		 */
		public void setRiskLog(String riskLog) {
			this.riskLog = riskLog;
		}
		
		/**
		 * @return the contentInspectionBypassLog
		 */
		public String getContentInspectionBypassLog() {
			return contentInspectionBypassLog;
		}
		/**
		 * @param contentInspectionBypassLog the contentInspectionBypassLog to set
		 */
		public void setContentInspectionBypassLog(String contentInspectionBypassLog) {
			this.contentInspectionBypassLog = contentInspectionBypassLog;
		}
		/**
		 * @return the caseUpdateFieldLog
		 */
		public String getCaseUpdateFieldLog() {
			return caseUpdateFieldLog;
		}
		/**
		 * @param caseUpdateFieldLog the caseUpdateFieldLog to set
		 */
		public void setCaseUpdateFieldLog(String caseUpdateFieldLog) {
			this.caseUpdateFieldLog = caseUpdateFieldLog;
		}
		
		
		/**
		 * @return the contractCreateLog
		 */
		public String getContractCreateLog() {
			return contractCreateLog;
		}
		/**
		 * @param contractCreateLog the contractCreateLog to set
		 */
		public void setContractCreateLog(String contractCreateLog) {
			this.contractCreateLog = contractCreateLog;
		}
		/**
		 * @return the contractUpdateLog
		 */
		public String getContractUpdateLog() {
			return contractUpdateLog;
		}
		/**
		 * @param contractUpdateLog the contractUpdateLog to set
		 */
		public void setContractUpdateLog(String contractUpdateLog) {
			this.contractUpdateLog = contractUpdateLog;
		}
		/**
		 * @return the contractDeleteLog
		 */
		public String getContractDeleteLog() {
			return contractDeleteLog;
		}
		/**
		 * @param contractDeleteLog the contractDeleteLog to set
		 */
		public void setContractDeleteLog(String contractDeleteLog) {
			this.contractDeleteLog = contractDeleteLog;
		}
		/**
		 * @return the contactCreateLog
		 */
		public String getContactCreateLog() {
			return contactCreateLog;
		}
		/**
		 * @param contactCreateLog the contactCreateLog to set
		 */
		public void setContactCreateLog(String contactCreateLog) {
			this.contactCreateLog = contactCreateLog;
		}
		/**
		 * @return the contactUpdateLog
		 */
		public String getContactUpdateLog() {
			return contactUpdateLog;
		}
		/**
		 * @param contactUpdateLog the contactUpdateLog to set
		 */
		public void setContactUpdateLog(String contactUpdateLog) {
			this.contactUpdateLog = contactUpdateLog;
		}
		/**
		 * @return the contactDeleteLog
		 */
		public String getContactDeleteLog() {
			return contactDeleteLog;
		}
		/**
		 * @param contactDeleteLog the contactDeleteLog to set
		 */
		public void setContactDeleteLog(String contactDeleteLog) {
			this.contactDeleteLog = contactDeleteLog;
		}
		/**
		 * @return the opportunityCreateLog
		 */
		public String getOpportunityCreateLog() {
			return opportunityCreateLog;
		}
		/**
		 * @param opportunityCreateLog the opportunityCreateLog to set
		 */
		public void setOpportunityCreateLog(String opportunityCreateLog) {
			this.opportunityCreateLog = opportunityCreateLog;
		}
		/**
		 * @return the opportunityUpdateLog
		 */
		public String getOpportunityUpdateLog() {
			return opportunityUpdateLog;
		}
		/**
		 * @param opportunityUpdateLog the opportunityUpdateLog to set
		 */
		public void setOpportunityUpdateLog(String opportunityUpdateLog) {
			this.opportunityUpdateLog = opportunityUpdateLog;
		}
		/**
		 * @return the opportunityDeleteLog
		 */
		public String getOpportunityDeleteLog() {
			return opportunityDeleteLog;
		}
		/**
		 * @param opportunityDeleteLog the opportunityDeleteLog to set
		 */
		public void setOpportunityDeleteLog(String opportunityDeleteLog) {
			this.opportunityDeleteLog = opportunityDeleteLog;
		}
		/**
		 * @return the caseCreateLog
		 */
		public String getCaseCreateLog() {
			return caseCreateLog;
		}
		/**
		 * @param caseCreateLog the caseCreateLog to set
		 */
		public void setCaseCreateLog(String caseCreateLog) {
			this.caseCreateLog = caseCreateLog;
		}
		/**
		 * @return the caseUpdateLog
		 */
		public String getCaseUpdateLog() {
			return caseUpdateLog;
		}
		/**
		 * @param caseUpdateLog the caseUpdateLog to set
		 */
		public void setCaseUpdateLog(String caseUpdateLog) {
			this.caseUpdateLog = caseUpdateLog;
		}
		/**
		 * @return the caseDeleteLog
		 */
		public String getCaseDeleteLog() {
			return caseDeleteLog;
		}
		/**
		 * @param caseDeleteLog the caseDeleteLog to set
		 */
		public void setCaseDeleteLog(String caseDeleteLog) {
			this.caseDeleteLog = caseDeleteLog;
		}
		/**
		 * @return the accountCreateLog
		 */
		public String getAccountCreateLog() {
			return accountCreateLog;
		}
		/**
		 * @param accountCreateLog the accountCreateLog to set
		 */
		public void setAccountCreateLog(String accountCreateLog) {
			this.accountCreateLog = accountCreateLog;
		}
		/**
		 * @return the accountUpdateLog
		 */
		public String getAccountUpdateLog() {
			return accountUpdateLog;
		}
		/**
		 * @param accountUpdateLog the accountUpdateLog to set
		 */
		public void setAccountUpdateLog(String accountUpdateLog) {
			this.accountUpdateLog = accountUpdateLog;
		}
		/**
		 * @return the accountDeleteLog
		 */
		public String getAccountDeleteLog() {
			return accountDeleteLog;
		}
		/**
		 * @param accountDeleteLog the accountDeleteLog to set
		 */
		public void setAccountDeleteLog(String accountDeleteLog) {
			this.accountDeleteLog = accountDeleteLog;
		}
		/**
		 * @return the contentDocumentDeleteLog
		 */
		public String getContentDocumentDeleteLog() {
			return contentDocumentDeleteLog;
		}
		/**
		 * @param contentDocumentDeleteLog the contentDocumentDeleteLog to set
		 */
		public void setContentDocumentDeleteLog(String contentDocumentDeleteLog) {
			this.contentDocumentDeleteLog = contentDocumentDeleteLog;
		}
		/**
		 * @return the contentVersionDeleteLog
		 */
		public String getContentVersionDeleteLog() {
			return contentVersionDeleteLog;
		}
		/**
		 * @param contentVersionDeleteLog the contentVersionDeleteLog to set
		 */
		public void setContentVersionDeleteLog(String contentVersionDeleteLog) {
			this.contentVersionDeleteLog = contentVersionDeleteLog;
		}
		
		
		/**
		 * @return the contentDocumentUpdateLog
		 */
		public String getContentDocumentUpdateLog() {
			return contentDocumentUpdateLog;
		}
		/**
		 * @param contentDocumentUpdateLog the contentDocumentUpdateLog to set
		 */
		public void setContentDocumentUpdateLog(String contentDocumentUpdateLog) {
			this.contentDocumentUpdateLog = contentDocumentUpdateLog;
		}
		/**
		 * @return the contentVersionUpdateLog
		 */
		public String getContentVersionUpdateLog() {
			return contentVersionUpdateLog;
		}
		/**
		 * @param contentVersionUpdateLog the contentVersionUpdateLog to set
		 */
		public void setContentVersionUpdateLog(String contentVersionUpdateLog) {
			this.contentVersionUpdateLog = contentVersionUpdateLog;
		}
		
		/**
		 * @return the pciRiskLog
		 */
		public String getPciRiskLog() {
			return pciRiskLog;
		}
		/**
		 * @param pciRiskLog the pciRiskLog to set
		 */
		public void setPciRiskLog(String pciRiskLog) {
			this.pciRiskLog = pciRiskLog;
		}
		/**
		 * @return the piiRiskLog
		 */
		public String getPiiRiskLog() {
			return piiRiskLog;
		}
		/**
		 * @param piiRiskLog the piiRiskLog to set
		 */
		public void setPiiRiskLog(String piiRiskLog) {
			this.piiRiskLog = piiRiskLog;
		}
		/**
		 * @return the hippaRiskLog
		 */
		public String getHipaaRiskLog() {
			return hipaaRiskLog;
		}
		/**
		 * @param hippaRiskLog the hippaRiskLog to set
		 */
		public void setHipaaRiskLog(String hippaRiskLog) {
			this.hipaaRiskLog = hippaRiskLog;
		}
		
		
		/**
		 * @return the leadCreateLog
		 */
		public String getLeadCreateLog() {
			return leadCreateLog;
		}
		/**
		 * @param leadCreateLog the leadCreateLog to set
		 */
		public void setLeadCreateLog(String leadCreateLog) {
			this.leadCreateLog = leadCreateLog;
		}
		/**
		 * @return the leadUpdateLog
		 */
		public String getLeadUpdateLog() {
			return leadUpdateLog;
		}
		/**
		 * @param leadUpdateLog the leadUpdateLog to set
		 */
		public void setLeadUpdateLog(String leadUpdateLog) {
			this.leadUpdateLog = leadUpdateLog;
		}
		/**
		 * @return the leadDeleteLog
		 */
		public String getLeadDeleteLog() {
			return leadDeleteLog;
		}
		/**
		 * @param leadDeleteLog the leadDeleteLog to set
		 */
		public void setLeadDeleteLog(String leadDeleteLog) {
			this.leadDeleteLog = leadDeleteLog;
		}

}
