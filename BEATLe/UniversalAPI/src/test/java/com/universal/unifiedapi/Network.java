package com.universal.unifiedapi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Network {

	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("email")
	private String email;
	@JsonProperty("name")
	private String name;
	@JsonProperty("community")
	private Boolean community;
	@JsonProperty("permalink")
	private String permalink;
	@JsonProperty("web_url")
	private String webUrl;
	@JsonProperty("show_upgrade_banner")
	private Boolean showUpgradeBanner;
	@JsonProperty("header_background_color")
	private String headerBackgroundColor;
	@JsonProperty("header_text_color")
	private String headerTextColor;
	@JsonProperty("navigation_background_color")
	private String navigationBackgroundColor;
	@JsonProperty("navigation_text_color")
	private String navigationTextColor;
	@JsonProperty("paid")
	private Boolean paid;
	@JsonProperty("moderated")
	private Boolean moderated;
	@JsonProperty("is_org_chart_enabled")
	private Boolean isOrgChartEnabled;
	@JsonProperty("is_group_enabled")
	private Boolean isGroupEnabled;
	@JsonProperty("is_chat_enabled")
	private Boolean isChatEnabled;
	@JsonProperty("is_translation_enabled")
	private Boolean isTranslationEnabled;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("profile_fields_config")
	private ProfileFieldsConfig profileFieldsConfig;
	@JsonProperty("browser_deprecation_url")
	private Object browserDeprecationUrl;
	@JsonProperty("external_messaging_state")
	private String externalMessagingState;
	@JsonProperty("state")
	private String state;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 * The type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The email
	 */
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 * The email
	 */
	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return
	 * The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 * The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 * The community
	 */
	@JsonProperty("community")
	public Boolean getCommunity() {
		return community;
	}

	/**
	 * 
	 * @param community
	 * The community
	 */
	@JsonProperty("community")
	public void setCommunity(Boolean community) {
		this.community = community;
	}

	/**
	 * 
	 * @return
	 * The permalink
	 */
	@JsonProperty("permalink")
	public String getPermalink() {
		return permalink;
	}

	/**
	 * 
	 * @param permalink
	 * The permalink
	 */
	@JsonProperty("permalink")
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	/**
	 * 
	 * @return
	 * The webUrl
	 */
	@JsonProperty("web_url")
	public String getWebUrl() {
		return webUrl;
	}

	/**
	 * 
	 * @param webUrl
	 * The web_url
	 */
	@JsonProperty("web_url")
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	/**
	 * 
	 * @return
	 * The showUpgradeBanner
	 */
	@JsonProperty("show_upgrade_banner")
	public Boolean getShowUpgradeBanner() {
		return showUpgradeBanner;
	}

	/**
	 * 
	 * @param showUpgradeBanner
	 * The show_upgrade_banner
	 */
	@JsonProperty("show_upgrade_banner")
	public void setShowUpgradeBanner(Boolean showUpgradeBanner) {
		this.showUpgradeBanner = showUpgradeBanner;
	}

	/**
	 * 
	 * @return
	 * The headerBackgroundColor
	 */
	@JsonProperty("header_background_color")
	public String getHeaderBackgroundColor() {
		return headerBackgroundColor;
	}

	/**
	 * 
	 * @param headerBackgroundColor
	 * The header_background_color
	 */
	@JsonProperty("header_background_color")
	public void setHeaderBackgroundColor(String headerBackgroundColor) {
		this.headerBackgroundColor = headerBackgroundColor;
	}

	/**
	 * 
	 * @return
	 * The headerTextColor
	 */
	@JsonProperty("header_text_color")
	public String getHeaderTextColor() {
		return headerTextColor;
	}

	/**
	 * 
	 * @param headerTextColor
	 * The header_text_color
	 */
	@JsonProperty("header_text_color")
	public void setHeaderTextColor(String headerTextColor) {
		this.headerTextColor = headerTextColor;
	}

	/**
	 * 
	 * @return
	 * The navigationBackgroundColor
	 */
	@JsonProperty("navigation_background_color")
	public String getNavigationBackgroundColor() {
		return navigationBackgroundColor;
	}

	/**
	 * 
	 * @param navigationBackgroundColor
	 * The navigation_background_color
	 */
	@JsonProperty("navigation_background_color")
	public void setNavigationBackgroundColor(String navigationBackgroundColor) {
		this.navigationBackgroundColor = navigationBackgroundColor;
	}

	/**
	 * 
	 * @return
	 * The navigationTextColor
	 */
	@JsonProperty("navigation_text_color")
	public String getNavigationTextColor() {
		return navigationTextColor;
	}

	/**
	 * 
	 * @param navigationTextColor
	 * The navigation_text_color
	 */
	@JsonProperty("navigation_text_color")
	public void setNavigationTextColor(String navigationTextColor) {
		this.navigationTextColor = navigationTextColor;
	}

	/**
	 * 
	 * @return
	 * The paid
	 */
	@JsonProperty("paid")
	public Boolean getPaid() {
		return paid;
	}

	/**
	 * 
	 * @param paid
	 * The paid
	 */
	@JsonProperty("paid")
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	/**
	 * 
	 * @return
	 * The moderated
	 */
	@JsonProperty("moderated")
	public Boolean getModerated() {
		return moderated;
	}

	/**
	 * 
	 * @param moderated
	 * The moderated
	 */
	@JsonProperty("moderated")
	public void setModerated(Boolean moderated) {
		this.moderated = moderated;
	}

	/**
	 * 
	 * @return
	 * The isOrgChartEnabled
	 */
	@JsonProperty("is_org_chart_enabled")
	public Boolean getIsOrgChartEnabled() {
		return isOrgChartEnabled;
	}

	/**
	 * 
	 * @param isOrgChartEnabled
	 * The is_org_chart_enabled
	 */
	@JsonProperty("is_org_chart_enabled")
	public void setIsOrgChartEnabled(Boolean isOrgChartEnabled) {
		this.isOrgChartEnabled = isOrgChartEnabled;
	}

	/**
	 * 
	 * @return
	 * The isGroupEnabled
	 */
	@JsonProperty("is_group_enabled")
	public Boolean getIsGroupEnabled() {
		return isGroupEnabled;
	}

	/**
	 * 
	 * @param isGroupEnabled
	 * The is_group_enabled
	 */
	@JsonProperty("is_group_enabled")
	public void setIsGroupEnabled(Boolean isGroupEnabled) {
		this.isGroupEnabled = isGroupEnabled;
	}

	/**
	 * 
	 * @return
	 * The isChatEnabled
	 */
	@JsonProperty("is_chat_enabled")
	public Boolean getIsChatEnabled() {
		return isChatEnabled;
	}

	/**
	 * 
	 * @param isChatEnabled
	 * The is_chat_enabled
	 */
	@JsonProperty("is_chat_enabled")
	public void setIsChatEnabled(Boolean isChatEnabled) {
		this.isChatEnabled = isChatEnabled;
	}

	/**
	 * 
	 * @return
	 * The isTranslationEnabled
	 */
	@JsonProperty("is_translation_enabled")
	public Boolean getIsTranslationEnabled() {
		return isTranslationEnabled;
	}

	/**
	 * 
	 * @param isTranslationEnabled
	 * The is_translation_enabled
	 */
	@JsonProperty("is_translation_enabled")
	public void setIsTranslationEnabled(Boolean isTranslationEnabled) {
		this.isTranslationEnabled = isTranslationEnabled;
	}

	/**
	 * 
	 * @return
	 * The createdAt
	 */
	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * 
	 * @param createdAt
	 * The created_at
	 */
	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * 
	 * @return
	 * The profileFieldsConfig
	 */
	@JsonProperty("profile_fields_config")
	public ProfileFieldsConfig getProfileFieldsConfig() {
		return profileFieldsConfig;
	}

	/**
	 * 
	 * @param profileFieldsConfig
	 * The profile_fields_config
	 */
	@JsonProperty("profile_fields_config")
	public void setProfileFieldsConfig(ProfileFieldsConfig profileFieldsConfig) {
		this.profileFieldsConfig = profileFieldsConfig;
	}

	/**
	 * 
	 * @return
	 * The browserDeprecationUrl
	 */
	@JsonProperty("browser_deprecation_url")
	public Object getBrowserDeprecationUrl() {
		return browserDeprecationUrl;
	}

	/**
	 * 
	 * @param browserDeprecationUrl
	 * The browser_deprecation_url
	 */
	@JsonProperty("browser_deprecation_url")
	public void setBrowserDeprecationUrl(Object browserDeprecationUrl) {
		this.browserDeprecationUrl = browserDeprecationUrl;
	}

	/**
	 * 
	 * @return
	 * The externalMessagingState
	 */
	@JsonProperty("external_messaging_state")
	public String getExternalMessagingState() {
		return externalMessagingState;
	}

	/**
	 * 
	 * @param externalMessagingState
	 * The external_messaging_state
	 */
	@JsonProperty("external_messaging_state")
	public void setExternalMessagingState(String externalMessagingState) {
		this.externalMessagingState = externalMessagingState;
	}

	/**
	 * 
	 * @return
	 * The state
	 */
	@JsonProperty("state")
	public String getState() {
		return state;
	}

	/**
	 * 
	 * @param state
	 * The state
	 */
	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
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
