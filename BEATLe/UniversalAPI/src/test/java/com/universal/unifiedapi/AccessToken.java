package com.universal.unifiedapi;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class AccessToken {

	@JsonProperty("user_id")
	private Integer userId;
	@JsonProperty("network_id")
	private Integer networkId;
	@JsonProperty("network_permalink")
	private String networkPermalink;
	@JsonProperty("network_name")
	private String networkName;
	@JsonProperty("token")
	private String token;
	@JsonProperty("view_members")
	private Boolean viewMembers;
	@JsonProperty("view_groups")
	private Boolean viewGroups;
	@JsonProperty("view_messages")
	private Boolean viewMessages;
	@JsonProperty("view_subscriptions")
	private Boolean viewSubscriptions;
	@JsonProperty("modify_subscriptions")
	private Boolean modifySubscriptions;
	@JsonProperty("modify_messages")
	private Boolean modifyMessages;
	@JsonProperty("view_tags")
	private Boolean viewTags;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("authorized_at")
	private String authorizedAt;
	@JsonProperty("expires_at")
	private Object expiresAt;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The userId
	 */
	@JsonProperty("user_id")
	public Integer getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId
	 * The user_id
	 */
	@JsonProperty("user_id")
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return
	 * The networkId
	 */
	@JsonProperty("network_id")
	public Integer getNetworkId() {
		return networkId;
	}

	/**
	 * 
	 * @param networkId
	 * The network_id
	 */
	@JsonProperty("network_id")
	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

	/**
	 * 
	 * @return
	 * The networkPermalink
	 */
	@JsonProperty("network_permalink")
	public String getNetworkPermalink() {
		return networkPermalink;
	}

	/**
	 * 
	 * @param networkPermalink
	 * The network_permalink
	 */
	@JsonProperty("network_permalink")
	public void setNetworkPermalink(String networkPermalink) {
		this.networkPermalink = networkPermalink;
	}

	/**
	 * 
	 * @return
	 * The networkName
	 */
	@JsonProperty("network_name")
	public String getNetworkName() {
		return networkName;
	}

	/**
	 * 
	 * @param networkName
	 * The network_name
	 */
	@JsonProperty("network_name")
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	/**
	 * 
	 * @return
	 * The token
	 */
	@JsonProperty("token")
	public String getToken() {
		return token;
	}

	/**
	 * 
	 * @param token
	 * The token
	 */
	@JsonProperty("token")
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * 
	 * @return
	 * The viewMembers
	 */
	@JsonProperty("view_members")
	public Boolean getViewMembers() {
		return viewMembers;
	}

	/**
	 * 
	 * @param viewMembers
	 * The view_members
	 */
	@JsonProperty("view_members")
	public void setViewMembers(Boolean viewMembers) {
		this.viewMembers = viewMembers;
	}

	/**
	 * 
	 * @return
	 * The viewGroups
	 */
	@JsonProperty("view_groups")
	public Boolean getViewGroups() {
		return viewGroups;
	}

	/**
	 * 
	 * @param viewGroups
	 * The view_groups
	 */
	@JsonProperty("view_groups")
	public void setViewGroups(Boolean viewGroups) {
		this.viewGroups = viewGroups;
	}

	/**
	 * 
	 * @return
	 * The viewMessages
	 */
	@JsonProperty("view_messages")
	public Boolean getViewMessages() {
		return viewMessages;
	}

	/**
	 * 
	 * @param viewMessages
	 * The view_messages
	 */
	@JsonProperty("view_messages")
	public void setViewMessages(Boolean viewMessages) {
		this.viewMessages = viewMessages;
	}

	/**
	 * 
	 * @return
	 * The viewSubscriptions
	 */
	@JsonProperty("view_subscriptions")
	public Boolean getViewSubscriptions() {
		return viewSubscriptions;
	}

	/**
	 * 
	 * @param viewSubscriptions
	 * The view_subscriptions
	 */
	@JsonProperty("view_subscriptions")
	public void setViewSubscriptions(Boolean viewSubscriptions) {
		this.viewSubscriptions = viewSubscriptions;
	}

	/**
	 * 
	 * @return
	 * The modifySubscriptions
	 */
	@JsonProperty("modify_subscriptions")
	public Boolean getModifySubscriptions() {
		return modifySubscriptions;
	}

	/**
	 * 
	 * @param modifySubscriptions
	 * The modify_subscriptions
	 */
	@JsonProperty("modify_subscriptions")
	public void setModifySubscriptions(Boolean modifySubscriptions) {
		this.modifySubscriptions = modifySubscriptions;
	}

	/**
	 * 
	 * @return
	 * The modifyMessages
	 */
	@JsonProperty("modify_messages")
	public Boolean getModifyMessages() {
		return modifyMessages;
	}

	/**
	 * 
	 * @param modifyMessages
	 * The modify_messages
	 */
	@JsonProperty("modify_messages")
	public void setModifyMessages(Boolean modifyMessages) {
		this.modifyMessages = modifyMessages;
	}

	/**
	 * 
	 * @return
	 * The viewTags
	 */
	@JsonProperty("view_tags")
	public Boolean getViewTags() {
		return viewTags;
	}

	/**
	 * 
	 * @param viewTags
	 * The view_tags
	 */
	@JsonProperty("view_tags")
	public void setViewTags(Boolean viewTags) {
		this.viewTags = viewTags;
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
	 * The authorizedAt
	 */
	@JsonProperty("authorized_at")
	public String getAuthorizedAt() {
		return authorizedAt;
	}

	/**
	 * 
	 * @param authorizedAt
	 * The authorized_at
	 */
	@JsonProperty("authorized_at")
	public void setAuthorizedAt(String authorizedAt) {
		this.authorizedAt = authorizedAt;
	}

	/**
	 * 
	 * @return
	 * The expiresAt
	 */
	@JsonProperty("expires_at")
	public Object getExpiresAt() {
		return expiresAt;
	}

	/**
	 * 
	 * @param expiresAt
	 * The expires_at
	 */
	@JsonProperty("expires_at")
	public void setExpiresAt(Object expiresAt) {
		this.expiresAt = expiresAt;
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
