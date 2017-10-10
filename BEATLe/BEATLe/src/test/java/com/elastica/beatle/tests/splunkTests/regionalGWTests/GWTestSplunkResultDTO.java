/**
 * 
 */
package com.elastica.beatle.tests.splunkTests.regionalGWTests;

/**
 * @author anuvrath
 *
 */
public class GWTestSplunkResultDTO {
	private String hostName;
	private String timeStamp;
	private int eventCount;
	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}
	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the eventCount
	 */
	public int getEventCount() {
		return eventCount;
	}
	/**
	 * @param eventCount the eventCount to set
	 */
	public void setEventCount(int eventCount) {
		this.eventCount = eventCount;
	}
}