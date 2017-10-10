
/**
 * 
 */
package com.elastica.splunk;

import java.util.Date;
import java.util.List;

/**
 * @author anuvrath
 *
 */
public class SplunkQueryResult {
	private int eventsCount;
	private String jobID;
	private int numberOfResults;
	private String searchDuration;
	private Date earliestTime;
	private Date latestTime;
	private String searchQuery;
	private List<String> queryResult;
	/**
	 * @return the queryResult
	 */
	public List<String> getQueryResult() {
		return queryResult;
	}

	/**
	 * @param queryResult the queryResult to set
	 */
	public void setQueryResult(List<String> queryResult) {
		this.queryResult = queryResult;
	}

	/**
	 * @return the eventsCount
	 */
	public int getEventsCount() {
		return eventsCount;
	}

	/**
	 * @param eventsCount the eventsCount to set
	 */
	public void setEventsCount(int eventsCount) {
		this.eventsCount = eventsCount;
	}

	/**
	 * @return the jobID
	 */
	public String getJobID() {
		return jobID;
	}

	/**
	 * @param jobID the jobID to set
	 */
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	/**
	 * @return the numberOfResults
	 */
	public int getNumberOfResults() {
		return numberOfResults;
	}

	/**
	 * @param i the numberOfResults to set
	 */
	public void setNumberOfResults(int i) {
		this.numberOfResults = i;
	}

	/**
	 * @return the searchDuration
	 */
	public String getSearchDuration() {
		return searchDuration;
	}

	/**
	 * @param searchDuration the searchDuration to set
	 */
	public void setSearchDuration(String searchDuration) {
		this.searchDuration = searchDuration;
	}

	/**
	 * @return the latestTime
	 */
	public Date getLatestTime() {
		return latestTime;
	}

	/**
	 * @param date the latestTime to set
	 */
	public void setLatestTime(Date date) {
		this.latestTime = date;
	}

	/**
	 * @return the searchQuery
	 */
	public String getSearchQuery() {
		return searchQuery;
	}

	/**
	 * @param searchQuery the searchQuery to set
	 */
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	/**
	 * @return the earliestTime
	 */
	public Date getEarliestTime() {
		return earliestTime;
	}

	/**
	 * @param date the earliestTime to set
	 */
	public void setEarliestTime(Date date) {
		this.earliestTime = date;
	}
}