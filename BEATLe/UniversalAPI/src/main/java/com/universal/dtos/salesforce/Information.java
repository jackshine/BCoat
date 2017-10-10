package com.universal.dtos.salesforce;
import org.codehaus.jackson.annotate.JsonProperty;

public class Information {


	@JsonProperty("text")
	private String text;
	@JsonProperty("title")
	private String title;

	/**
	 * 
	 * @return
	 * The text
	 */
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	/**
	 * 
	 * @param text
	 * The text
	 */
	@JsonProperty("text")
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 
	 * @return
	 * The title
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 * The title
	 */
	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

}
