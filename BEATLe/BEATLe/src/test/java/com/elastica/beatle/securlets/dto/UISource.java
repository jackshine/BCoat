package com.elastica.beatle.securlets.dto;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UISource {

	@JsonProperty("limit")
	private Integer limit;
	@JsonProperty("offset")
	private Integer offset;
	@JsonProperty("isInternal")
	private Boolean isInternal;
	@JsonProperty("searchTextFromTable")
	private String searchTextFromTable;
	@JsonProperty("searchText")
	private String searchText;
	@JsonProperty("orderBy")
	private String orderBy;
	@JsonProperty("exportType")
	private String exportType;
	@JsonProperty("objectType")
	private String objectType;
	@JsonProperty("app")
	private String app;
	@JsonProperty("fileExposure")
	private String fileExposure;
	@JsonProperty("vulnerabilityType")
	private String vulnerabilityType;
	
	@JsonProperty("format")
	private String format;
	@JsonProperty("contentType")
	private String contentType;
	
	@JsonProperty("requestType")
	private String requestType;
	

	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the vulnerabilityType
	 */
	public String getVulnerabilityType() {
		return vulnerabilityType;
	}

	/**
	 * @param vulnerabilityType the vulnerabilityType to set
	 */
	public void setVulnerabilityType(String vulnerabilityType) {
		this.vulnerabilityType = vulnerabilityType;
	}

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * @param searchText the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}


	/**
	 * @return the fileExposure
	 */
	public String getFileExposure() {
		return fileExposure;
	}

	/**
	 * @param fileExposure the fileExposure to set
	 */
	public void setFileExposure(String fileExposure) {
		this.fileExposure = fileExposure;
	}
	
	/**
	 * 
	 * @return
	 * The limit
	 */
	@JsonProperty("limit")
	public Integer getLimit() {
		return limit;
	}

	/**
	 * 
	 * @param limit
	 * The limit
	 */
	@JsonProperty("limit")
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * 
	 * @return
	 * The offset
	 */
	@JsonProperty("offset")
	public Integer getOffset() {
		return offset;
	}

	/**
	 * 
	 * @param offset
	 * The offset
	 */
	@JsonProperty("offset")
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * 
	 * @return
	 * The isInternal
	 */
	@JsonProperty("isInternal")
	public Boolean getIsInternal() {
		return isInternal;
	}

	/**
	 * 
	 * @param isInternal
	 * The isInternal
	 */
	@JsonProperty("isInternal")
	public void setIsInternal(Boolean isInternal) {
		this.isInternal = isInternal;
	}

	/**
	 * 
	 * @return
	 * The searchTextFromTable
	 */
	@JsonProperty("searchTextFromTable")
	public String getSearchTextFromTable() {
		return searchTextFromTable;
	}

	/**
	 * 
	 * @param searchTextFromTable
	 * The searchTextFromTable
	 */
	@JsonProperty("searchTextFromTable")
	public void setSearchTextFromTable(String searchTextFromTable) {
		this.searchTextFromTable = searchTextFromTable;
	}

	/**
	 * 
	 * @return
	 * The orderBy
	 */
	@JsonProperty("orderBy")
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 
	 * @param orderBy
	 * The orderBy
	 */
	@JsonProperty("orderBy")
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 
	 * @return
	 * The exportType
	 */
	@JsonProperty("exportType")
	public String getExportType() {
		return exportType;
	}

	/**
	 * 
	 * @param exportType
	 * The exportType
	 */
	@JsonProperty("exportType")
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	/**
	 * 
	 * @return
	 * The objectType
	 */
	@JsonProperty("objectType")
	public String getObjectType() {
		return objectType;
	}

	/**
	 * 
	 * @param objectType
	 * The objectType
	 */
	@JsonProperty("objectType")
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	/**
	 * 
	 * @return
	 * The app
	 */
	@JsonProperty("app")
	public String getApp() {
		return app;
	}

	/**
	 * 
	 * @param app
	 * The app
	 */
	@JsonProperty("app")
	public void setApp(String app) {
		this.app = app;
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
