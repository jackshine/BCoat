package com.universal.dtos.salesforce;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class MailingAddress {

	@JsonProperty("city")
	private Object city;
	@JsonProperty("country")
	private String country;
	@JsonProperty("countryCode")
	private String countryCode;
	@JsonProperty("geocodeAccuracy")
	private Object geocodeAccuracy;
	@JsonProperty("latitude")
	private Object latitude;
	@JsonProperty("longitude")
	private Object longitude;
	@JsonProperty("postalCode")
	private Object postalCode;
	@JsonProperty("state")
	private Object state;
	@JsonProperty("stateCode")
	private Object stateCode;
	@JsonProperty("street")
	private Object street;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The city
	 */
	@JsonProperty("city")
	public Object getCity() {
		return city;
	}

	/**
	 * 
	 * @param city
	 * The city
	 */
	@JsonProperty("city")
	public void setCity(Object city) {
		this.city = city;
	}

	/**
	 * 
	 * @return
	 * The country
	 */
	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	/**
	 * 
	 * @param country
	 * The country
	 */
	@JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 
	 * @return
	 * The countryCode
	 */
	@JsonProperty("countryCode")
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * 
	 * @param countryCode
	 * The countryCode
	 */
	@JsonProperty("countryCode")
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * 
	 * @return
	 * The geocodeAccuracy
	 */
	@JsonProperty("geocodeAccuracy")
	public Object getGeocodeAccuracy() {
		return geocodeAccuracy;
	}

	/**
	 * 
	 * @param geocodeAccuracy
	 * The geocodeAccuracy
	 */
	@JsonProperty("geocodeAccuracy")
	public void setGeocodeAccuracy(Object geocodeAccuracy) {
		this.geocodeAccuracy = geocodeAccuracy;
	}

	/**
	 * 
	 * @return
	 * The latitude
	 */
	@JsonProperty("latitude")
	public Object getLatitude() {
		return latitude;
	}

	/**
	 * 
	 * @param latitude
	 * The latitude
	 */
	@JsonProperty("latitude")
	public void setLatitude(Object latitude) {
		this.latitude = latitude;
	}

	/**
	 * 
	 * @return
	 * The longitude
	 */
	@JsonProperty("longitude")
	public Object getLongitude() {
		return longitude;
	}

	/**
	 * 
	 * @param longitude
	 * The longitude
	 */
	@JsonProperty("longitude")
	public void setLongitude(Object longitude) {
		this.longitude = longitude;
	}

	/**
	 * 
	 * @return
	 * The postalCode
	 */
	@JsonProperty("postalCode")
	public Object getPostalCode() {
		return postalCode;
	}

	/**
	 * 
	 * @param postalCode
	 * The postalCode
	 */
	@JsonProperty("postalCode")
	public void setPostalCode(Object postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * 
	 * @return
	 * The state
	 */
	@JsonProperty("state")
	public Object getState() {
		return state;
	}

	/**
	 * 
	 * @param state
	 * The state
	 */
	@JsonProperty("state")
	public void setState(Object state) {
		this.state = state;
	}

	/**
	 * 
	 * @return
	 * The stateCode
	 */
	@JsonProperty("stateCode")
	public Object getStateCode() {
		return stateCode;
	}

	/**
	 * 
	 * @param stateCode
	 * The stateCode
	 */
	@JsonProperty("stateCode")
	public void setStateCode(Object stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * 
	 * @return
	 * The street
	 */
	@JsonProperty("street")
	public Object getStreet() {
		return street;
	}

	/**
	 * 
	 * @param street
	 * The street
	 */
	@JsonProperty("street")
	public void setStreet(Object street) {
		this.street = street;
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
