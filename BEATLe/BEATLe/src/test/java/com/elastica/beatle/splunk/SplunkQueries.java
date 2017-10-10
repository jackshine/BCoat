/**
 * 
 */
package com.elastica.beatle.splunk;

import com.elastica.beatle.splunk.SplunkConstants.ServiceLogs;
import com.elastica.beatle.splunk.SplunkConstants.SplunkHosts;

/**
 * @author anuvrath
 *
 */
public class SplunkQueries {
	
	
	/**
	 * @param envrionement
	 * @return
	 */
	private static SplunkHosts getEnvEmailHostName(String envrionement) {
		return SplunkHosts.EMAILPROD;
	}

	/**
	 * @param envrionement
	 * @return
	 */
	private static SplunkHosts getEnvHostName(String envrionement){
		return SplunkHosts.valueOf(envrionement.toUpperCase());	
	}

	/**
	 * This method looks for audit reports cron jobs in Splunk for a given environment, recipient and report name in the requested period
	 * @param environmentName
	 * @param emailRecepient
	 * @param reportName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookforAuditReportLogInSplunk(String environmentName, String emailRecepient, String reportName, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+emailRecepient+"\"  \""+reportName+"\"", getEnvHostName(environmentName), ServiceLogs.REPORTERWORKER, timeDuration);
	}
	
	/**
	 * This method looks for emailing logs for policy voilations/any jobs that are handled by the mailing process
	 * @param emailID
	 * @param envrionement
	 * @param duration
	 * @return
	 */
	public static SplunkQueryResult lookForEmailInEmailLogs(String emailID,String envrionement,String duration){
		if(envrionement.equalsIgnoreCase("PROD"))
			return new SplunkQueryHandlers(envrionement).executeSplunkQuery(emailID,getEnvEmailHostName(envrionement), ServiceLogs.EMAIL, duration);
		else
			return new SplunkQueryHandlers(envrionement).executeSplunkQuery(emailID,getEnvHostName(envrionement), ServiceLogs.EMAIL, duration);
	}
	
	/**
	 * This method gives the queue size in DCI 
	 * @param environmentName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForCIJobPoolSizes(String environmentName,String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("ci.small_job_pool.size | rex \"(?:.*)ci.small_job_pool.size:(?<cnt>[0-9]+)\" |stats  first(cnt) by host", getEnvHostName(environmentName), ServiceLogs.CI, timeDuration);
	}
	
	/**
	 * @param envrionement
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForAboutToSendFileLogForAllTenantFromCelery(String envrionement, String timeDuration){
		return new SplunkQueryHandlers(envrionement).executeSplunkQuery("\"About to send file\" | rex \"/mnt/tmp/(?<type>[a-zA-Z0-9_]+)/(?<tenant>[a-zA-Z0-9-_]+)/\" | top 100 type host",getEnvHostName(envrionement), ServiceLogs.CELERY, timeDuration);
	}
	
	/**
	 * @param environment
	 * @param recordID
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForAboutToSendFileLogForSpecificRecordFromCelery(String environment, String recordID, String timeDuration){
		return new SplunkQueryHandlers(environment).executeSplunkQuery("\"About to send file\" "+ recordID+" | rex \"/mnt/tmp/(?<type>[a-zA-Z0-9_]+)/(?<tenant>[a-zA-Z0-9-_]+)/\" | top 100 type host",getEnvHostName(environment), ServiceLogs.CELERY, timeDuration);
	}		
	
	/**
	 * @param environmentName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForAvgFielProcessTimeInCI(String environmentName, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\"classification in ms\" | rex \"(?:.* classification in ms:)(?<time>[0-9]+)\" | stats avg(time)", getEnvHostName(environmentName), ServiceLogs.CI, timeDuration);
	}

	/**
	 * @param environmentName
	 * @param emailRecipientName
	 * @return
	 */
	public static SplunkQueryResult lookforGatewayInternalServerInIcapServerViaSplunk(String environmentName, String timeDuration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\"Operation failed due to internal error\" \"ELASTICA LOG\" | rex field=_raw \"_domain..(?<tenant>[^,]*),\" | eval cloud=substr(host,1,7) | stats count by tenant, cloud", getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeDuration);
	}
	
	/**
	 * @param environmentName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForGmailDailyLimit(String environmentName, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\"Daily Limit Exceeded\"", getEnvHostName(environmentName), timeDuration);
	}	
	
	/**
	 * @param environmentName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForEvolvingProfileLogs(String searchString,  String environmentName, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery(searchString,getEnvHostName(environmentName), ServiceLogs.DGF, timeDuration);
	}	
	
	/**
	 * To check for processed CI logs for an individual file
	 * @param environmentName
	 * @param resourceId
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForProcessedCILogsForAFile(String environmentName,String fileName, String timeDuration){
		String splunkQuery = "\"Finished job\" \""+fileName+"\"";

		return new SplunkQueryHandlers(environmentName).executeSplunkQuery(splunkQuery, getEnvHostName(environmentName), timeDuration);
	}

	/*
	 * Regional GW test queries
	 */
	
	/**
	 * This function gets the list of GW hosts after policy updates(this includes create/activate/de-activate/edit)
	 * @param environmentName
	 * @param policyName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForGWPolicyCreateORUpdateLog(String environmentName, String policyName, String timeDuration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\"Writing *\"  \""+policyName+"\" \"to cache\" | stats count by host _time", getEnvHostName(environmentName), ServiceLogs.GW_ICAPServer, timeDuration);
	}

	/**
	 * This function gets all the Live hosts that are generating traffic in all regional GW
	 * @param environmentName
	 * @return
	 */
	public static SplunkQueryResult getAllLiveGWHosts(String environmentName){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\"Authed/allowed: authed=\" \", req_type=\"  | eval cloud=substr(host,1,6) | stats count by host", getEnvHostName(environmentName), ServiceLogs.GW_ICAPServer, "-120m");
	}
	
	/**
	 * This function gets the list of GW hosts after policy deletes
	 * @param environmentName
	 * @param policyName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForGWPolicyDeleteLog(String environmentName, String policyName, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\"Going to delete object *\" \""+policyName+"\" \"from cache\" | stats count by host _time", getEnvHostName(environmentName), ServiceLogs.GW_ICAPServer, timeDuration);
	}
	
	
	/**
	 * @param environmentName
	 * @param tenantName
	 * @param SaasApp
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForGateletDeActivationLogs(String environmentName, String tenantName, String SaasApp,String timeDuration){
		return new  SplunkQueryHandlers(environmentName).executeSplunkQuery("\"config_update tenant\" \"app="+SaasApp+";\" \"is_active=0\"" +"\""+tenantName+"\" | stats count by host _time",getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeDuration);
	}
	
	/**
	 * @param environmentName
	 * @param tenantName
	 * @param SaasApp
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForGateletActivationLogs(String environmentName, String tenantName, String SaasApp,String timeDuration){
		return new  SplunkQueryHandlers(environmentName).executeSplunkQuery("\"config_update tenant\" \"app="+SaasApp+";\" \"is_active=1\" " +"\""+tenantName+"\" | stats count by host _time",getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeDuration);
	}

	/**
	 * 
	 * @param environmentName
	 * @param logFileName
	 * @param fileName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForPolicyWrapperLogs(String environmentName,String logFileName, String fileName, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("source=\"/var/log/elastica/api/"+logFileName+".log\" \"data sent to the content inspection engine\"  \""+fileName+"\"" , getEnvHostName(environmentName), timeDuration);
	}

	/**
	 * 
	 * @param environmentName
	 * @param id
	 * @param SaasApp
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForPolicyEvalLogs(String environmentName, String id, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("source=\"/var/log/elastica/policy_evaluator.log\" \""+id+"\" Reserved job_id", getEnvHostName(environmentName), timeDuration);
	}
	
	/**
	 * 
	 * @param environmentName
	 * @param id
	 * @param SaasApp
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForPEvalLogs(String environmentName, String id, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("source=\"/var/log/elastica/policy_evaluator.log\" \""+id+"\"", getEnvHostName(environmentName), timeDuration);
	}	
	
	/**
	 * 
	 * @param environmentName
	 * @param tenantName
	 * @param SaasApp
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForPolicyRemediationLogs(String environmentName, String id, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("source=\"/var/log/elastica/policy/policy_enforcement.log\" \""+id+"\"" , getEnvHostName(environmentName), timeDuration);
																		   	
	}
	
	/**
	 * @param environmentName
	 * @param id
	 * @param timeduration
	 * @return
	 */
	public static SplunkQueryResult lookForGroupCreatingLogs(String environmentName, String tenantName, String id, String timeduration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_groups\" \"id="+id+"\" \"oper=upsert\" | stats count by host _time" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}
	
	/**
	 * @param environmentName
	 * @param id
	 * @param timeduration
	 * @return
	 */
	public static SplunkQueryResult lookForGroupDeletionLogs(String environmentName, String tenantName, String id, String timeduration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_groups\" \"id="+id+"\" \"oper=delete\" | stats count by host _time" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}
	
	/**
	 * 
	 * @param environmentName
	 * @param tenantName
	 * @param id
	 * @param timeduration
	 * @return
	 */
	public static SplunkQueryResult lookForUserCreateEditLogs(String environmentName, String tenantName, String id, String timeduration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_users\" \"id="+id+"\" \"oper=upsert\" | stats count by host _time" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}
	
	/**
	 * 
	 * @param environmentName
	 * @param tenantName
	 * @param id
	 * @param timeduration
	 * @return
	 */
	public static SplunkQueryResult lookForUserGroupsCreateEditLogs(String environmentName, String tenantName, String id, String timeduration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_usergroups\" \"id="+id+"\" \"oper=upsert\" | stats count by host _time" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}
	
	/**
	 * @param environmentName
	 * @param tenantName
	 * @param id
	 * @param timeduration
	 * @return
	 */
	public static SplunkQueryResult lookForCreateResponseTemplate(String environmentName, String tenantName, String id, String timeduration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_responsetemplate\" \"id="+id+"\" \"oper=upsert\" | stats count by host _time" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}
	
	/**
	 * @param environmentName
	 * @param tenantName
	 * @param id
	 * @param timeduration
	 * @return
	 */
	public static SplunkQueryResult lookForDeleteResponseTemplate(String environmentName, String tenantName, String id, String timeduration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_responsetemplate\" \"id="+id+"\" \"oper=delete\" | stats count by host _time" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}

	/**
	 * @param environmentName
	 * @param tenantName
	 * @param templateID
	 * @param splunkQueryTimeGap
	 * @return
	 */
	public static SplunkQueryResult lookForCreateResponseTemplateWithoutTime(String environmentName, String tenantName, String templateID, String timeduration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_responsetemplate\" \"id="+templateID+"\" \"oper=upsert\" | stats count by host" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}
	
	/**
	 * @param environmentName
	 * @param tenantName
	 * @param templateID
	 * @param timeduration
	 * @return
	 */
	public static SplunkQueryResult lookForCreateKeySecureWithoutTime(String environmentName, String tenantName, String keyID, String timeduration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_keyserver\" \"id="+keyID+"\" \"oper=upsert\" | stats count by host" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}

	/**
	 * @param environmentName
	 * @param tenantName
	 * @param keyID
	 * @param splunkQueryTimeGap
	 * @return
	 */
	public static SplunkQueryResult lookForDeleteKeySecureWithoutTime(String environmentName, String tenantName,String keyID, String timeduration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_keyserver\" \"id="+keyID+"\" \"oper=delete\" | stats count by host" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}

	/**
	 * @param environmentName
	 * @param tenantName
	 * @param id
	 * @param splunkQueryTimeGap
	 * @return
	 */
	public static SplunkQueryResult lookForEnableAzureWithoutTime(String environmentName, String tenantName, String id, String timeduration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_identityproviders\" \"id="+id+"\" \"oper=upsert\" | stats count by host" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}

	/**
	 * @param environmentName
	 * @param tenantName
	 * @param id
	 * @param splunkQueryTimeGap
	 * @return
	 */
	public static SplunkQueryResult lookForDisableAzureWithoutTime(String environmentName, String tenantName, String id, String timeduration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_identityproviders\" \"id="+id+"\" \"oper=delete\" | stats count by host" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}

	/**
	 * @param environmentName
	 * @param tenantName
	 * @param groupID
	 * @param splunkQueryTimeGap
	 * @return
	 */
	public static SplunkQueryResult lookForGroupCreatingLogsWithoutTime(String environmentName, String tenantName, String groupID, String timeduration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_groups\" \"id="+groupID+"\" \"oper=upsert\" | stats count by host" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeduration);
	}

	/**
	 * @param environmentName
	 * @param policyName
	 * @param splunkQueryTimeGap
	 * @return
	 */
	public static SplunkQueryResult lookForGWPolicyCreateORUpdateLogWithoutTime(String environmentName, String policyName, String timeDuration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\"Writing *\"  \""+policyName+"\" \"to cache\" | stats count by host", getEnvHostName(environmentName), ServiceLogs.GW_ICAPServer, timeDuration);
	}
	
	/**
	 * @param environmentName
	 * @param phonoNumber
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForSMSSentConfirmationForPolicyViolation(String environmentName, String policyName, String phoneNumber, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("source =\"/var/log/elastica/sms_service.log\" \"SMS sent successfully\" \""+phoneNumber+"\" \""+policyName+"\"", getEnvHostName(environmentName), timeDuration);		
	}
	
	
	public static SplunkQueryResult checkUserIsThrottled(String environmentName, String userName, String timeDuration){
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\"Sending throttle message for key\" source=\"/var/log/elastica/celery/penforce_workers.log\" \""+userName+"\"", getEnvHostName(environmentName), timeDuration);		
	}

	/**
	 * @param environmentName
	 * @param tenantName
	 * @param userId
	 * @param splunkQueryTimeGap
	 * @return
	 */
	public static SplunkQueryResult lookForUserDeletionLogs(String environmentName, String tenantName, String userId,String timeDuration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_users\" \"id="+userId+"\" \"oper=delete\" | stats count by host _time" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeDuration);
	}

	/**
	 * @param environmentName
	 * @param tenantName
	 * @param userId
	 * @param splunkQueryTimeGap
	 * @return
	 */
	public static SplunkQueryResult lookForUserGroupsDeleteEditLogs(String environmentName, String tenantName, String userId,String timeDuration) {
		return new SplunkQueryHandlers(environmentName).executeSplunkQuery("\""+tenantName+"\" \"Got new config updated;\" \"collection=tenant_usergroups\" \"id="+userId+"\" \"oper=delete\" | stats count by host _time" , getEnvHostName(environmentName),ServiceLogs.GW_ICAPServer, timeDuration);
	}
	
}