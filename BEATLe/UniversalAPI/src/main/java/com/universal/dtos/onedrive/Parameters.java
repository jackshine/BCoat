package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Parameters {

	@JsonProperty("Url")
	private String Url;
	@JsonProperty("Title")
	private String Title;
	@JsonProperty("Description")
	private String Description;
	@JsonProperty("Language")
	private Integer Language;
	@JsonProperty("WebTemplate")
	private String WebTemplate;
	@JsonProperty("UseUniquePermissions")
	private Boolean UseUniquePermissions;
	@JsonProperty("UseSamePermissionsAsParentSite")
	private Boolean UseSamePermissionsAsParentSite;
	
	
	public Parameters() {
		
	}
	
	public Parameters(Integer language, String url, String title, String description, String webtemplate, boolean useUniquePermissions) {
		this.Language = language;
		this.Url = url;
		this.Title = title;
		this.Description = description;
		this.WebTemplate = webtemplate;
		this.UseUniquePermissions = useUniquePermissions;
	}
	
	
	/**
	 * @return the useSamePermissionsAsParentSite
	 */
	public Boolean getUseSamePermissionsAsParentSite() {
		return UseSamePermissionsAsParentSite;
	}

	/**
	 * @param useSamePermissionsAsParentSite the useSamePermissionsAsParentSite to set
	 */
	public void setUseSamePermissionsAsParentSite(Boolean useSamePermissionsAsParentSite) {
		UseSamePermissionsAsParentSite = useSamePermissionsAsParentSite;
	}

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The Url
	 */
	@JsonProperty("Url")
	public String getUrl() {
		return Url;
	}

	/**
	 * 
	 * @param Url
	 * The Url
	 */
	@JsonProperty("Url")
	public void setUrl(String Url) {
		this.Url = Url;
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
	 * The UseUniquePermissions
	 */
	@JsonProperty("UseUniquePermissions")
	public Boolean getUseUniquePermissions() {
		return UseUniquePermissions;
	}

	/**
	 * 
	 * @param UseUniquePermissions
	 * The UseUniquePermissions
	 */
	@JsonProperty("UseUniquePermissions")
	public void setUseUniquePermissions(Boolean UseUniquePermissions) {
		this.UseUniquePermissions = UseUniquePermissions;
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
