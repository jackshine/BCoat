package com.universal.unifiedapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Contact {

	@JsonProperty("im")
	private Im im;
	@JsonProperty("phone_numbers")
	private List<Object> phoneNumbers = new ArrayList<Object>();
	@JsonProperty("email_addresses")
	private List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();
	@JsonProperty("has_fake_email")
	private Boolean hasFakeEmail;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The im
	 */
	@JsonProperty("im")
	public Im getIm() {
		return im;
	}

	/**
	 * 
	 * @param im
	 * The im
	 */
	@JsonProperty("im")
	public void setIm(Im im) {
		this.im = im;
	}

	/**
	 * 
	 * @return
	 * The phoneNumbers
	 */
	@JsonProperty("phone_numbers")
	public List<Object> getPhoneNumbers() {
		return phoneNumbers;
	}

	/**
	 * 
	 * @param phoneNumbers
	 * The phone_numbers
	 */
	@JsonProperty("phone_numbers")
	public void setPhoneNumbers(List<Object> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	/**
	 * 
	 * @return
	 * The emailAddresses
	 */
	@JsonProperty("email_addresses")
	public List<EmailAddress> getEmailAddresses() {
		return emailAddresses;
	}

	/**
	 * 
	 * @param emailAddresses
	 * The email_addresses
	 */
	@JsonProperty("email_addresses")
	public void setEmailAddresses(List<EmailAddress> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	/**
	 * 
	 * @return
	 * The hasFakeEmail
	 */
	@JsonProperty("has_fake_email")
	public Boolean getHasFakeEmail() {
		return hasFakeEmail;
	}

	/**
	 * 
	 * @param hasFakeEmail
	 * The has_fake_email
	 */
	@JsonProperty("has_fake_email")
	public void setHasFakeEmail(Boolean hasFakeEmail) {
		this.hasFakeEmail = hasFakeEmail;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
