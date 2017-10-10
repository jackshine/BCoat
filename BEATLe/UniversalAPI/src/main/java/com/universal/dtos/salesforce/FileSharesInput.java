package com.universal.dtos.salesforce;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FileSharesInput {

	@JsonProperty("message")
	private String message;
	@JsonProperty("shares")
	private List<Shares> shares = new ArrayList<Shares>();

	/**
	 * 
	 * @return
	 * The message
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 * The message
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return
	 * The shares
	 */
	@JsonProperty("shares")
	public List<Shares> getShares() {
		return shares;
	}

	/**
	 * 
	 * @param shares
	 * The shares
	 */
	@JsonProperty("shares")
	public void setShares(List<Shares> shares) {
		this.shares = shares;
	}

}
