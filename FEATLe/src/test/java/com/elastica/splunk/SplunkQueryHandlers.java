/**
 * 
 */
package com.elastica.splunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.elastica.logger.Logger;
import com.elastica.splunk.SqlunkConstants.ServiceLogs;
import com.elastica.splunk.SqlunkConstants.SplunkHosts;

import com.splunk.HttpService;
import com.splunk.Job;
import com.splunk.JobArgs;

import com.splunk.ResultsReaderXml;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;

/**
 * @author anuvrath
 *
 */
public class SplunkQueryHandlers {

	public JobArgs getJobArgs(String timeDuration) {
		JobArgs args = new JobArgs();
		args.setExecutionMode(JobArgs.ExecutionMode.NORMAL);
		args.setEarliestTime(timeDuration);
		args.setLatestTime("now");
		args.setSearchMode(JobArgs.SearchMode.NORMAL);
		
		return args;
	}
	
	/**
	 * 
	 * @param query
	 * @param args
	 * @return
	 */
	public Job executeSplunkQuery(String query, JobArgs args) {
		Job job;
		
		job = getConnection().getJobs().create(query, args);
		Logger.info("Executing the Splunk query: " + query);						
		Logger.info("Connected to splunk!!!..");
		Logger.info("Waiting for query to complete");
		while (!job.isDone()) {
		    try {
		        Thread.sleep(500);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		}
		
		return job;
	}
	
	public Job executeSplunkQuery(String query, String timeDuration) {
		
		return executeSplunkQuery(query, getJobArgs(timeDuration));
	}
	
	/**
	 * @param searchString
	 * @param envrionement
	 * @param serviceSource
	 * @param timeDuration
	 * @return
	 */
	public SplunkQueryResult executeSplunkQuery(String searchString,SplunkHosts envrionement,ServiceLogs serviceSource, String timeDuration){
		JobArgs jobargs = getJobArgs(timeDuration);
		String searchQuery = builsSearchSting(envrionement, serviceSource, searchString);
		Job job = executeSplunkQuery(searchQuery, jobargs);
		
		Logger.info("Completed execution of Splunk query: "+job.getSearch()+ " for latestTime "+job.getLatestTime()+" and for earliest time "+job.getEarliestTime());
		Logger.info("Time took: "+job.getRunDuration());
		Logger.info("Number of events found:"+job.getEventCount());						
		return populateAndSendJobResult(job);
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

			List<String > eventLogs = new ArrayList<String>();
			try {
				ResultsReaderXml resultsReaderNormalSearch = new ResultsReaderXml(job.getResults());
			    HashMap<String, String> event;
			    while ((event = resultsReaderNormalSearch.getNextEvent()) != null) {			        
			        for (String key: event.keySet()){
			        	eventLogs.add(key+": "+event.get(key));
			        }
			        				            
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			}
			result.setQueryResult(eventLogs);
		} catch(Exception e)	{
			e.printStackTrace();
		}

		return result;
	}
	/**
	 * @param serviceSource 
	 * @param envrionement 
	 * @return
	 */
	private String builsSearchSting(SplunkHosts envrionement, ServiceLogs serviceSource, String searchString) {
		if(envrionement != null && serviceSource != null)
			return "search "+envrionement.getHost()+" "+serviceSource.getServiceLogPath()+" "+searchString+" | head 2";
		if(envrionement == null && serviceSource != null)
			return "search "+serviceSource.getServiceLogPath()+" "+searchString+" | head 1";
		else if(serviceSource == null && envrionement != null)
			return "search "+envrionement.getHost()+" "+searchString+" | head 1";
		else
			return "search "+searchString + " | head 1";
	}

	/**
	 * @return
	 */
	private Service getConnection(){
		Service service = new Service("splunk.pub.elastica.net", 8089);		 
	    service.setToken("Basic cWEtdGVzdGluZzozWTZFN0J4eGZnOXUydGFj");
        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        return service;
	}
}
