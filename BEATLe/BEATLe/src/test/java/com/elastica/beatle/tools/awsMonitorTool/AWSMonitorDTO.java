/**
 * 
 */
package com.elastica.beatle.tools.awsMonitorTool;

import java.util.Date;

/**
 * @author anuvrath
 *
 */
public class AWSMonitorDTO {
	
	private String resourceID;
	private String resourceState;
	private int resourceUpTime;
	private Date resourceLaunchDate;
	private String resourceType;
	private String availabilityZone;
	
	/**
	 * @return the resourceID
	 */
	public String getresourceID() {
		return resourceID;
	}
	/**
	 * @param resourceID the resourceID to set
	 */
	public void setresourceID(String resourceID) {
		this.resourceID = resourceID;
	}
	/**
	 * @return the resourceState
	 */
	public String getresourceState() {
		return resourceState;
	}
	/**
	 * @param resourceState the resourceState to set
	 */
	public void setresourceState(String resourceState) {
		this.resourceState = resourceState;
	}	
	/**
	 * @return the resourceType
	 */
	public String getresourceType() {
		return resourceType;
	}
	/**
	 * @param resourceType the resourceType to set
	 */
	public void setresourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	/**
	 * @return the availabilityZone
	 */
	public String getAvailabilityZone() {
		return availabilityZone;
	}
	/**
	 * @param availabilityZone the availabilityZone to set
	 */
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}
	/**
	 * @return the resourceUpTime
	 */
	public int getresourceUpTime() {
		return resourceUpTime;
	}
	/**
	 * @param resourceUpTime the resourceUpTime to set
	 */
	public void setresourceUpTime(int resourceUpTime) {
		this.resourceUpTime = resourceUpTime;
	}
	/**
	 * @return the resourceLaunchDate
	 */
	public Date getresourceLaunchDate() {
		return resourceLaunchDate;
	}
	/**
	 * @param resourceLaunchDate the resourceLaunchDate to set
	 */
	public void setresourceLaunchDate(Date resourceLaunchDate) {
		this.resourceLaunchDate = resourceLaunchDate;
	}	
}
