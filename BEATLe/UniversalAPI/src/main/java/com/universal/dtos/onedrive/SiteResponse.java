package com.universal.dtos.onedrive;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SiteResponse {

	@JsonProperty("odata.metadata")
	private String odataMetadata;
	@JsonProperty("odata.type")
	private String odataType;
	@JsonProperty("odata.id")
	private String odataId;
	@JsonProperty("odata.editLink")
	private String odataEditLink;
	@JsonProperty("Configuration")
	private Integer Configuration;
	@JsonProperty("Created")
	private String Created;
	@JsonProperty("Description")
	private String Description;
	@JsonProperty("Id")
	private String Id;
	@JsonProperty("Language")
	private Integer Language;
	@JsonProperty("LastItemModifiedDate")
	private String LastItemModifiedDate;
	@JsonProperty("ServerRelativeUrl")
	private String ServerRelativeUrl;
	@JsonProperty("Title")
	private String Title;
	@JsonProperty("WebTemplate")
	private String WebTemplate;
	@JsonProperty("WebTemplateId")
	private Integer WebTemplateId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The odataMetadata
	 */
	@JsonProperty("odata.metadata")
	public String getOdataMetadata() {
		return odataMetadata;
	}

	/**
	 * 
	 * @param odataMetadata
	 * The odata.metadata
	 */
	@JsonProperty("odata.metadata")
	public void setOdataMetadata(String odataMetadata) {
		this.odataMetadata = odataMetadata;
	}

	/**
	 * 
	 * @return
	 * The odataType
	 */
	@JsonProperty("odata.type")
	public String getOdataType() {
		return odataType;
	}

	/**
	 * 
	 * @param odataType
	 * The odata.type
	 */
	@JsonProperty("odata.type")
	public void setOdataType(String odataType) {
		this.odataType = odataType;
	}

	/**
	 * 
	 * @return
	 * The odataId
	 */
	@JsonProperty("odata.id")
	public String getOdataId() {
		return odataId;
	}

	/**
	 * 
	 * @param odataId
	 * The odata.id
	 */
	@JsonProperty("odata.id")
	public void setOdataId(String odataId) {
		this.odataId = odataId;
	}

	/**
	 * 
	 * @return
	 * The odataEditLink
	 */
	@JsonProperty("odata.editLink")
	public String getOdataEditLink() {
		return odataEditLink;
	}

	/**
	 * 
	 * @param odataEditLink
	 * The odata.editLink
	 */
	@JsonProperty("odata.editLink")
	public void setOdataEditLink(String odataEditLink) {
		this.odataEditLink = odataEditLink;
	}

	/**
	 * 
	 * @return
	 * The Configuration
	 */
	@JsonProperty("Configuration")
	public Integer getConfiguration() {
		return Configuration;
	}

	/**
	 * 
	 * @param Configuration
	 * The Configuration
	 */
	@JsonProperty("Configuration")
	public void setConfiguration(Integer Configuration) {
		this.Configuration = Configuration;
	}

	/**
	 * 
	 * @return
	 * The Created
	 */
	@JsonProperty("Created")
	public String getCreated() {
		return Created;
	}

	/**
	 * 
	 * @param Created
	 * The Created
	 */
	@JsonProperty("Created")
	public void setCreated(String Created) {
		this.Created = Created;
	}

	/**
	 * 
	 * @return
	 * The Description
	 */
	@JsonProperty("Description")
	public String getDescription() {
		return Description;
	}

	/**
	 * 
	 * @param Description
	 * The Description
	 */
	@JsonProperty("Description")
	public void setDescription(String Description) {
		this.Description = Description;
	}

	/**
	 * 
	 * @return
	 * The Id
	 */
	@JsonProperty("Id")
	public String getId() {
		return Id;
	}

	/**
	 * 
	 * @param Id
	 * The Id
	 */
	@JsonProperty("Id")
	public void setId(String Id) {
		this.Id = Id;
	}

	/**
	 * 
	 * @return
	 * The Language
	 */
	@JsonProperty("Language")
	public Integer getLanguage() {
		return Language;
	}

	/**
	 * 
	 * @param Language
	 * The Language
	 */
	@JsonProperty("Language")
	public void setLanguage(Integer Language) {
		this.Language = Language;
	}

	/**
	 * 
	 * @return
	 * The LastItemModifiedDate
	 */
	@JsonProperty("LastItemModifiedDate")
	public String getLastItemModifiedDate() {
		return LastItemModifiedDate;
	}

	/**
	 * 
	 * @param LastItemModifiedDate
	 * The LastItemModifiedDate
	 */
	@JsonProperty("LastItemModifiedDate")
	public void setLastItemModifiedDate(String LastItemModifiedDate) {
		this.LastItemModifiedDate = LastItemModifiedDate;
	}

	/**
	 * 
	 * @return
	 * The ServerRelativeUrl
	 */
	@JsonProperty("ServerRelativeUrl")
	public String getServerRelativeUrl() {
		return ServerRelativeUrl;
	}

	/**
	 * 
	 * @param ServerRelativeUrl
	 * The ServerRelativeUrl
	 */
	@JsonProperty("ServerRelativeUrl")
	public void setServerRelativeUrl(String ServerRelativeUrl) {
		this.ServerRelativeUrl = ServerRelativeUrl;
	}

	/**
	 * 
	 * @return
	 * The Title
	 */
	@JsonProperty("Title")
	public String getTitle() {
		return Title;
	}

	/**
	 * 
	 * @param Title
	 * The Title
	 */
	@JsonProperty("Title")
	public void setTitle(String Title) {
		this.Title = Title;
	}

	/**
	 * 
	 * @return
	 * The WebTemplate
	 */
	@JsonProperty("WebTemplate")
	public String getWebTemplate() {
		return WebTemplate;
	}

	/**
	 * 
	 * @param WebTemplate
	 * The WebTemplate
	 */
	@JsonProperty("WebTemplate")
	public void setWebTemplate(String WebTemplate) {
		this.WebTemplate = WebTemplate;
	}

	/**
	 * 
	 * @return
	 * The WebTemplateId
	 */
	@JsonProperty("WebTemplateId")
	public Integer getWebTemplateId() {
		return WebTemplateId;
	}

	/**
	 * 
	 * @param WebTemplateId
	 * The WebTemplateId
	 */
	@JsonProperty("WebTemplateId")
	public void setWebTemplateId(Integer WebTemplateId) {
		this.WebTemplateId = WebTemplateId;
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
