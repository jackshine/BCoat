package com.elastica.beatle.AuditSummaryDataObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "service_id", "sort_key_brr", "sort_key_brr_class",
		"top_destinations", "top_users", "users_count" })
public class TopRiskyService {

	@JsonProperty("service_id")
	private Integer serviceId;
	@JsonProperty("sort_key_brr")
	private Integer sortKeyBrr;
	@JsonProperty("sort_key_brr_class")
	private Integer sortKeyBrrClass;
	@JsonProperty("top_destinations")
	private List<TopDestination> topDestinations = new ArrayList<TopDestination>();
	@JsonProperty("top_users")
	private List<TopUser> topUsers = new ArrayList<TopUser>();
	@JsonProperty("users_count")
	private Integer usersCount;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return The serviceId
	 */
	@JsonProperty("service_id")
	public Integer getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * @param serviceId
	 *            The service_id
	 */
	@JsonProperty("service_id")
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * 
	 * @return The sortKeyBrr
	 */
	@JsonProperty("sort_key_brr")
	public Integer getSortKeyBrr() {
		return sortKeyBrr;
	}

	/**
	 * 
	 * @param sortKeyBrr
	 *            The sort_key_brr
	 */
	@JsonProperty("sort_key_brr")
	public void setSortKeyBrr(Integer sortKeyBrr) {
		this.sortKeyBrr = sortKeyBrr;
	}

	/**
	 * 
	 * @return The sortKeyBrrClass
	 */
	@JsonProperty("sort_key_brr_class")
	public Integer getSortKeyBrrClass() {
		return sortKeyBrrClass;
	}

	/**
	 * 
	 * @param sortKeyBrrClass
	 *            The sort_key_brr_class
	 */
	@JsonProperty("sort_key_brr_class")
	public void setSortKeyBrrClass(Integer sortKeyBrrClass) {
		this.sortKeyBrrClass = sortKeyBrrClass;
	}

	/**
	 * 
	 * @return The topDestinations
	 */
	@JsonProperty("top_destinations")
	public List<TopDestination> getTopDestinations() {
		return topDestinations;
	}

	/**
	 * 
	 * @param topDestinations
	 *            The top_destinations
	 */
	@JsonProperty("top_destinations")
	public void setTopDestinations(List<TopDestination> topDestinations) {
		this.topDestinations = topDestinations;
	}

	/**
	 * 
	 * @return The topUsers
	 */
	@JsonProperty("top_users")
	public List<TopUser> getTopUsers() {
		return topUsers;
	}

	/**
	 * 
	 * @param topUsers
	 *            The top_users
	 */
	@JsonProperty("top_users")
	public void setTopUsers(List<TopUser> topUsers) {
		this.topUsers = topUsers;
	}

	/**
	 * 
	 * @return The usersCount
	 */
	@JsonProperty("users_count")
	public Integer getUsersCount() {
		return usersCount;
	}

	/**
	 * 
	 * @param usersCount
	 *            The users_count
	 */
	@JsonProperty("users_count")
	public void setUsersCount(Integer usersCount) {
		this.usersCount = usersCount;
	}

	@JsonAnyGetter
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}