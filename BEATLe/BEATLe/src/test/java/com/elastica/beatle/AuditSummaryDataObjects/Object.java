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
@JsonPropertyOrder({ "company_brr", "datasource_id", "date_range",
		"earliest_date", "hardware_ids", "high_risk_services", "id",
		"is_valid", "latest_date", "med_risk_services", "request_type",
		"resource_uri", "top_risky_services", "top_used_services",
		"total_destinations", "total_services", "total_users" })
public class Object {

	@JsonProperty("company_brr")
	private CompanyBrr companyBrr;
	@JsonProperty("datasource_id")
	private String datasourceId;
	@JsonProperty("date_range")
	private String dateRange;
	@JsonProperty("earliest_date")
	private Integer earliestDate;
	@JsonProperty("hardware_ids")
	private String hardwareIds;
	@JsonProperty("high_risk_services")
	private Integer highRiskServices;
	@JsonProperty("id")
	private String id;
	@JsonProperty("is_valid")
	private Boolean isValid;
	@JsonProperty("latest_date")
	private Integer latestDate;
	@JsonProperty("med_risk_services")
	private Integer medRiskServices;
	@JsonProperty("request_type")
	private Integer requestType;
	@JsonProperty("resource_uri")
	private String resourceUri;
	@JsonProperty("top_risky_services")
	private List<TopRiskyService> topRiskyServices = new ArrayList<TopRiskyService>();
	@JsonProperty("top_used_services")
	private List<TopUsedService> topUsedServices = new ArrayList<TopUsedService>();
	@JsonProperty("total_destinations")
	private Integer totalDestinations;
	@JsonProperty("total_services")
	private Integer totalServices;
	@JsonProperty("total_users")
	private Integer totalUsers;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return The companyBrr
	 */
	@JsonProperty("company_brr")
	public CompanyBrr getCompanyBrr() {
		return companyBrr;
	}

	/**
	 * 
	 * @param companyBrr
	 *            The company_brr
	 */
	@JsonProperty("company_brr")
	public void setCompanyBrr(CompanyBrr companyBrr) {
		this.companyBrr = companyBrr;
	}

	/**
	 * 
	 * @return The datasourceId
	 */
	@JsonProperty("datasource_id")
	public String getDatasourceId() {
		return datasourceId;
	}

	/**
	 * 
	 * @param datasourceId
	 *            The datasource_id
	 */
	@JsonProperty("datasource_id")
	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	/**
	 * 
	 * @return The dateRange
	 */
	@JsonProperty("date_range")
	public String getDateRange() {
		return dateRange;
	}

	/**
	 * 
	 * @param dateRange
	 *            The date_range
	 */
	@JsonProperty("date_range")
	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	/**
	 * 
	 * @return The earliestDate
	 */
	@JsonProperty("earliest_date")
	public Integer getEarliestDate() {
		return earliestDate;
	}

	/**
	 * 
	 * @param earliestDate
	 *            The earliest_date
	 */
	@JsonProperty("earliest_date")
	public void setEarliestDate(Integer earliestDate) {
		this.earliestDate = earliestDate;
	}

	/**
	 * 
	 * @return The hardwareIds
	 */
	@JsonProperty("hardware_ids")
	public String getHardwareIds() {
		return hardwareIds;
	}

	/**
	 * 
	 * @param hardwareIds
	 *            The hardware_ids
	 */
	@JsonProperty("hardware_ids")
	public void setHardwareIds(String hardwareIds) {
		this.hardwareIds = hardwareIds;
	}

	/**
	 * 
	 * @return The highRiskServices
	 */
	@JsonProperty("high_risk_services")
	public Integer getHighRiskServices() {
		return highRiskServices;
	}

	/**
	 * 
	 * @param highRiskServices
	 *            The high_risk_services
	 */
	@JsonProperty("high_risk_services")
	public void setHighRiskServices(Integer highRiskServices) {
		this.highRiskServices = highRiskServices;
	}

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The isValid
	 */
	@JsonProperty("is_valid")
	public Boolean getIsValid() {
		return isValid;
	}

	/**
	 * 
	 * @param isValid
	 *            The is_valid
	 */
	@JsonProperty("is_valid")
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * 
	 * @return The latestDate
	 */
	@JsonProperty("latest_date")
	public Integer getLatestDate() {
		return latestDate;
	}

	/**
	 * 
	 * @param latestDate
	 *            The latest_date
	 */
	@JsonProperty("latest_date")
	public void setLatestDate(Integer latestDate) {
		this.latestDate = latestDate;
	}

	/**
	 * 
	 * @return The medRiskServices
	 */
	@JsonProperty("med_risk_services")
	public Integer getMedRiskServices() {
		return medRiskServices;
	}

	/**
	 * 
	 * @param medRiskServices
	 *            The med_risk_services
	 */
	@JsonProperty("med_risk_services")
	public void setMedRiskServices(Integer medRiskServices) {
		this.medRiskServices = medRiskServices;
	}

	/**
	 * 
	 * @return The requestType
	 */
	@JsonProperty("request_type")
	public Integer getRequestType() {
		return requestType;
	}

	/**
	 * 
	 * @param requestType
	 *            The request_type
	 */
	@JsonProperty("request_type")
	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	/**
	 * 
	 * @return The resourceUri
	 */
	@JsonProperty("resource_uri")
	public String getResourceUri() {
		return resourceUri;
	}

	/**
	 * 
	 * @param resourceUri
	 *            The resource_uri
	 */
	@JsonProperty("resource_uri")
	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

	/**
	 * 
	 * @return The topRiskyServices
	 */
	@JsonProperty("top_risky_services")
	public List<TopRiskyService> getTopRiskyServices() {
		return topRiskyServices;
	}

	/**
	 * 
	 * @param topRiskyServices
	 *            The top_risky_services
	 */
	@JsonProperty("top_risky_services")
	public void setTopRiskyServices(List<TopRiskyService> topRiskyServices) {
		this.topRiskyServices = topRiskyServices;
	}

	/**
	 * 
	 * @return The topUsedServices
	 */
	@JsonProperty("top_used_services")
	public List<TopUsedService> getTopUsedServices() {
		return topUsedServices;
	}

	/**
	 * 
	 * @param topUsedServices
	 *            The top_used_services
	 */
	@JsonProperty("top_used_services")
	public void setTopUsedServices(List<TopUsedService> topUsedServices) {
		this.topUsedServices = topUsedServices;
	}

	/**
	 * 
	 * @return The totalDestinations
	 */
	@JsonProperty("total_destinations")
	public Integer getTotalDestinations() {
		return totalDestinations;
	}

	/**
	 * 
	 * @param totalDestinations
	 *            The total_destinations
	 */
	@JsonProperty("total_destinations")
	public void setTotalDestinations(Integer totalDestinations) {
		this.totalDestinations = totalDestinations;
	}

	/**
	 * 
	 * @return The totalServices
	 */
	@JsonProperty("total_services")
	public Integer getTotalServices() {
		return totalServices;
	}

	/**
	 * 
	 * @param totalServices
	 *            The total_services
	 */
	@JsonProperty("total_services")
	public void setTotalServices(Integer totalServices) {
		this.totalServices = totalServices;
	}

	/**
	 * 
	 * @return The totalUsers
	 */
	@JsonProperty("total_users")
	public Integer getTotalUsers() {
		return totalUsers;
	}

	/**
	 * 
	 * @param totalUsers
	 *            The total_users
	 */
	@JsonProperty("total_users")
	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
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