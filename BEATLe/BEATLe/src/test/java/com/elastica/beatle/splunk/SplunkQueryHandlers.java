/**
 * 
 */
package com.elastica.beatle.splunk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;
import com.elastica.beatle.splunk.SplunkConstants.ServiceLogs;
import com.elastica.beatle.splunk.SplunkConstants.SplunkHosts;
import com.elastica.beatle.logger.Logger;
import com.splunk.Args;
import com.splunk.HttpService;
import com.splunk.Job;
import com.splunk.JobArgs;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;

/**
 * @author anuvrath
 */
public class SplunkQueryHandlers {

	private String environment;
	
	/**
	 * @param environment
	 */
	public SplunkQueryHandlers(String envi) {
		super();
		this.environment = envi;
	}	
	
	/**
	 * @return
	 */
	private Service getConnection(){
		Service service;
		if(environment.equals("prod"))
			service = new Service(SplunkConstants.PROD_SPLUNK_HOST, SplunkConstants.SPLUNK_CONNECTION_PORT);
		else if( environment.equalsIgnoreCase("EU") || environment.equalsIgnoreCase("cep"))
			service = new Service(SplunkConstants.EU_SPLUNK_HOST,SplunkConstants.SPLUNK_CONNECTION_PORT);
		else
			service = new Service(SplunkConstants.EOE_SPLUNK_HOST, SplunkConstants.SPLUNK_CONNECTION_PORT);				
		
	    service.setToken(SplunkConstants.SPLUNK_BASIC_AUTH_KEY);
        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        return service;
	}
	
	/**
	 * @param timeDuration
	 * @param searchQuery
	 * @return
	 */
	private SplunkQueryResult executequery(String timeDuration,String searchQuery){
		JobArgs jobargs = getJobArgs(timeDuration);		
		Job job = getConnection().getJobs().create(searchQuery, jobargs);
		Logger.info("Executing the Splunk query: " + searchQuery);						
		Logger.info("Connected to splunk!!!..");
		Logger.info("Waiting for query to complete");
		while (!job.isDone()) {
		    try {
		        Thread.sleep(500);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		}
		Logger.info("Completed execution of Splunk query: "+job.getSearch()+ " for latestTime "+job.getLatestTime()+" and for earliest time "+job.getEarliestTime());
		Logger.info("Time took: "+job.getRunDuration());
		Logger.info("Number of events found:"+job.getEventCount());
		
		return populateAndSendJobResult(job);
		
	}
	
	/**
	 * @param timeDuration
	 * @return
	 */
	private JobArgs getJobArgs(String timeDuration) {
		JobArgs args = new JobArgs();
		args.setExecutionMode(JobArgs.ExecutionMode.NORMAL);
		args.setEarliestTime(timeDuration);
		args.setLatestTime("now");
		args.setSearchMode(JobArgs.SearchMode.NORMAL);
		return args;
	}
	
	/**
	 * @param searchString
	 * @param environment
	 * @param serviceSource
	 * @param timeDuration
	 * @return
	 */
	public SplunkQueryResult executeSplunkQuery(String searchString, String hostName, ServiceLogs serviceSource, String timeDuration){
		return executequery(timeDuration,buildSearchString(hostName, serviceSource, searchString));
	}
	
	/**
	 * @param string
	 * @param envHostName
	 * @param timeDuration
	 * @return
	 */
	public SplunkQueryResult executeSplunkQuery(String searchString, SplunkHosts environment, String timeDuration) {
		return executequery(timeDuration,buildSearchString(environment, null, searchString));
	}
	
	/**
	 * @param searchString
	 * @param environment
	 * @param serviceSource
	 * @param timeDuration
	 * @return
	 */
	public SplunkQueryResult executeSplunkQuery(String searchString,SplunkHosts environment,ServiceLogs serviceSource, String timeDuration){
		return executequery(timeDuration,buildSearchString(environment, serviceSource, searchString));
	}
	
	/**
	 * @param job
	 * @return
	 */
	private SplunkQueryResult populateAndSendJobResult(Job job){
		SplunkQueryResult result = new SplunkQueryResult();
		try {			
			result.setEventsCount(job.getEventCount());
			result.setJobID(job.getSid());
			result.setLatestTime(job.getLatestTime());
			result.setEarliestTime(job.getEarliestTime());
			result.setNumberOfResults(job.getResultCount());
			result.setSearchDuration(job.getRunDuration()+" seconds");
			result.setSearchQuery(job.getSearch());
			
			Args outputArgs = new Args();
			outputArgs.put("output_mode","json");			
			InputStream results = job.getResults(outputArgs);
			
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line=new BufferedReader(new InputStreamReader(results)).readLine()) != null) {
			    sb.append(line);
			}
			JSONObject jo = new JSONObject(sb.toString());
			Logger.info("Splunk Response in JSON: "+ jo.toString());
			result.setQueryResult(jo);
		} catch(Exception e)	{
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * @param serviceSource 
	 * @param environment 
	 * @return
	 */
	private String buildSearchString(SplunkHosts environment, ServiceLogs serviceSource, String searchString) {
		if(environment != null && serviceSource != null)
			return "search "+environment.getHost()+" "+serviceSource.getServiceLogPath()+" "+searchString;
		if(environment == null && serviceSource != null)
			return "search "+serviceSource.getServiceLogPath()+" "+searchString;
		else if(serviceSource == null && environment != null)
			return "search "+environment.getHost()+" "+searchString;
		else
			return "search "+searchString;
	}
			
	/**
	 * @param serviceSource 
	 * @param environment 
	 * @return
	 */
	private String buildSearchString(String hostName, ServiceLogs serviceSource, String searchString) {
		if(hostName != null && serviceSource != null)
			return "search host="+hostName+" "+serviceSource.getServiceLogPath()+" "+searchString;
		if(hostName == null && serviceSource != null)
			return "search "+serviceSource.getServiceLogPath()+" "+searchString;
		else if(serviceSource == null && hostName != null)
			return "search host="+hostName+" "+searchString;
		else
			return "search "+searchString;
	}
}