package com.universal.dtos.onedrive;

import org.codehaus.jackson.annotate.JsonProperty;


public class BasePermissions {

	@JsonProperty("High")
	private String High;
	/**
	 * @return the high
	 */
	public String getHigh() {
		return High;
	}
	/**
	 * @param high the high to set
	 */
	public void setHigh(String high) {
		High = high;
	}
	/**
	 * @return the low
	 */
	public String getLow() {
		return Low;
	}
	/**
	 * @param low the low to set
	 */
	public void setLow(String low) {
		Low = low;
	}
	@JsonProperty("Low")
	private String Low;
	

}
