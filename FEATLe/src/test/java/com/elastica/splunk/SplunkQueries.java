/**
 * 
 */
package com.elastica.splunk;
import com.elastica.splunk.SqlunkConstants.ServiceLogs;
import com.elastica.splunk.SqlunkConstants.SplunkHosts;
/**
 * @author anuvrath
 *
 */
public class SplunkQueries {
		
	/**
	 * This method looks for audit reports cron jobs in Splunk for a given environment, recipient and report name in the requested period
	 * @param environmentName
	 * @param emailRecepient
	 * @param reportName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookforAuditReportLogInSplunk(String environmentName, String emailRecepient, String reportName, String timeDuration){
		return new SplunkQueryHandlers().executeSplunkQuery("\""+emailRecepient+"\"  \""+reportName+"\"", getEnvHostName(environmentName), ServiceLogs.REPORTERWORKER, timeDuration);
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
			return new SplunkQueryHandlers().executeSplunkQuery(emailID,getEnvEmailHostName(envrionement), ServiceLogs.EMAIL, duration);
		else
			return new SplunkQueryHandlers().executeSplunkQuery(emailID,getEnvHostName(envrionement), ServiceLogs.EMAIL, duration);
	}
	
	/**
	 * This method gives the queue size in DCI 
	 * @param environmentName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForCIJobPoolSizes(String environmentName,String timeDuration){
		return new SplunkQueryHandlers().executeSplunkQuery("ci.small_job_pool.size | rex \"(?:.*)ci.small_job_pool.size:(?<cnt>[0-9]+)\" |stats  first(cnt) by host", getEnvHostName(environmentName), ServiceLogs.CI, timeDuration);
		//return new SplunkQueryHandlers().executeSplunkQuery("ci.small_job_pool.size | rex \"(?:.*)ci.small_job_pool.size:(?<cnt>[0-9]+)\"", getEnvHostName(environmentName), ServiceLogs.CI, timeDuration);
	}
	
	/**
	 * @param envrionement
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForAboutToSendFileLogForAllTenantFromCelery(String envrionement, String timeDuration){
		return new SplunkQueryHandlers().executeSplunkQuery("\"About to send file\" | rex \"/mnt/tmp/(?<type>[a-zA-Z0-9_]+)/(?<tenant>[a-zA-Z0-9-_]+)/\" | top 100 type host",getEnvHostName(envrionement), ServiceLogs.CELERY, timeDuration);
	}
	
	/**
	 * @param environment
	 * @param recordID
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForAboutToSendFileLogForSpecificRecordFromCelery(String environment, String recordID, String timeDuration){
		return new SplunkQueryHandlers().executeSplunkQuery("\"About to send file\" "+ recordID+" | rex \"/mnt/tmp/(?<type>[a-zA-Z0-9_]+)/(?<tenant>[a-zA-Z0-9-_]+)/\" | top 100 type host",getEnvHostName(environment), ServiceLogs.CELERY, timeDuration);
	}		
	
	/**
	 * @param environmentName
	 * @param timeDuration
	 * @return
	 */
	public static SplunkQueryResult lookForAvgFielProcessTimeInCI(String environmentName, String timeDuration){
		return new SplunkQueryHandlers().executeSplunkQuery("\"classification in ms\" | rex \"(?:.* classification in ms:)(?<time>[0-9]+)\" | stats avg(time)", getEnvHostName(environmentName), ServiceLogs.CI, timeDuration);
	}
	
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
}
