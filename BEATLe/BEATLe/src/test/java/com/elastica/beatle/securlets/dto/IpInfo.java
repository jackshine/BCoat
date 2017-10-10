package com.elastica.beatle.securlets.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class IpInfo {

	@JsonProperty("ip")
	private String ip;
	@JsonProperty("hostname")
	private String hostname;
	@JsonProperty("city")
	private String city;
	@JsonProperty("region")
	private String region;
	@JsonProperty("country")
	private String country;
	@JsonProperty("loc")
	private String loc;
	@JsonProperty("org")
	private String org;
	@JsonProperty("postal")
	private String postal;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	
	/**
	 * @return the postal
	 */
	public String getPostal() {
		return postal;
	}

	/**
	 * @param postal the postal to set
	 */
	public void setPostal(String postal) {
		this.postal = postal;
	}

	
	/**
	 * 
	 * @return
	 * The ip
	 */
	@JsonProperty("ip")
	public String getIp() {
		return ip;
	}

	/**
	 * 
	 * @param ip
	 * The ip
	 */
	@JsonProperty("ip")
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 
	 * @return
	 * The hostname
	 */
	@JsonProperty("hostname")
	public String getHostname() {
		return hostname;
	}

	/**
	 * 
	 * @param hostname
	 * The hostname
	 */
	@JsonProperty("hostname")
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * 
	 * @return
	 * The city
	 */
	@JsonProperty("city")
	public String getCity() {
		return city;
	}

	/**
	 * 
	 * @param city
	 * The city
	 */
	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 
	 * @return
	 * The region
	 */
	@JsonProperty("region")
	public String getRegion() {
		return region;
	}

	/**
	 * 
	 * @param region
	 * The region
	 */
	@JsonProperty("region")
	public void setRegion(String region) {
		this.region = region;
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
	 * The loc
	 */
	@JsonProperty("loc")
	public String getLoc() {
		return loc;
	}

	/**
	 * 
	 * @param loc
	 * The loc
	 */
	@JsonProperty("loc")
	public void setLoc(String loc) {
		this.loc = loc;
	}

	/**
	 * 
	 * @return
	 * The org
	 */
	@JsonProperty("org")
	public String getOrg() {
		return org;
	}

	/**
	 * 
	 * @param org
	 * The org
	 */
	@JsonProperty("org")
	public void setOrg(String org) {
		this.org = org;
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
