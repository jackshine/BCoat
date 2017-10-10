package com.universal.dtos.salesforce;

import org.codehaus.jackson.annotate.JsonProperty;

public class Motif {

	@JsonProperty("color")
	private String color;
	@JsonProperty("largeIconUrl")
	private String largeIconUrl;
	@JsonProperty("mediumIconUrl")
	private String mediumIconUrl;
	@JsonProperty("smallIconUrl")
	private String smallIconUrl;
	@JsonProperty("svgIconUrl")
	private Object svgIconUrl;

	/**
	 * 
	 * @return
	 * The color
	 */
	@JsonProperty("color")
	public String getColor() {
		return color;
	}

	/**
	 * 
	 * @param color
	 * The color
	 */
	@JsonProperty("color")
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * 
	 * @return
	 * The largeIconUrl
	 */
	@JsonProperty("largeIconUrl")
	public String getLargeIconUrl() {
		return largeIconUrl;
	}

	/**
	 * 
	 * @param largeIconUrl
	 * The largeIconUrl
	 */
	@JsonProperty("largeIconUrl")
	public void setLargeIconUrl(String largeIconUrl) {
		this.largeIconUrl = largeIconUrl;
	}

	/**
	 * 
	 * @return
	 * The mediumIconUrl
	 */
	@JsonProperty("mediumIconUrl")
	public String getMediumIconUrl() {
		return mediumIconUrl;
	}

	/**
	 * 
	 * @param mediumIconUrl
	 * The mediumIconUrl
	 */
	@JsonProperty("mediumIconUrl")
	public void setMediumIconUrl(String mediumIconUrl) {
		this.mediumIconUrl = mediumIconUrl;
	}

	/**
	 * 
	 * @return
	 * The smallIconUrl
	 */
	@JsonProperty("smallIconUrl")
	public String getSmallIconUrl() {
		return smallIconUrl;
	}

	/**
	 * 
	 * @param smallIconUrl
	 * The smallIconUrl
	 */
	@JsonProperty("smallIconUrl")
	public void setSmallIconUrl(String smallIconUrl) {
		this.smallIconUrl = smallIconUrl;
	}

	/**
	 * 
	 * @return
	 * The svgIconUrl
	 */
	@JsonProperty("svgIconUrl")
	public Object getSvgIconUrl() {
		return svgIconUrl;
	}

	/**
	 * 
	 * @param svgIconUrl
	 * The svgIconUrl
	 */
	@JsonProperty("svgIconUrl")
	public void setSvgIconUrl(Object svgIconUrl) {
		this.svgIconUrl = svgIconUrl;
	}

}

