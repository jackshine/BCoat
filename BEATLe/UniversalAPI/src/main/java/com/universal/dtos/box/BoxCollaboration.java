package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class BoxCollaboration {

	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private String id;
	@JsonProperty("created_by")
	private CreatedBy createdBy;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("modified_at")
	private String modifiedAt;
	@JsonProperty("expires_at")
	private String expiresAt;
	@JsonProperty("status")
	private String status;
	@JsonProperty("accessible_by")
	private AccessibleBy accessibleBy;
	@JsonProperty("role")
	private String role;
	@JsonProperty("acknowledged_at")
	private String acknowledgedAt;
	@JsonProperty("item")
	private Item item;
	

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
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The createdBy
	 */
	@JsonProperty("created_by")
	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	/**
	 * 
	 * @param createdBy
	 * The created_by
	 */
	@JsonProperty("created_by")
	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
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
	 * The modifiedAt
	 */
	@JsonProperty("modified_at")
	public String getModifiedAt() {
		return modifiedAt;
	}

	/**
	 * 
	 * @param modifiedAt
	 * The modified_at
	 */
	@JsonProperty("modified_at")
	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	/**
	 * 
	 * @return
	 * The expiresAt
	 */
	@JsonProperty("expires_at")
	public String getExpiresAt() {
		return expiresAt;
	}

	/**
	 * 
	 * @param expiresAt
	 * The expires_at
	 */
	@JsonProperty("expires_at")
	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}

	/**
	 * 
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return
	 * The accessibleBy
	 */
	@JsonProperty("accessible_by")
	public AccessibleBy getAccessibleBy() {
		return accessibleBy;
	}

	/**
	 * 
	 * @param accessibleBy
	 * The accessible_by
	 */
	@JsonProperty("accessible_by")
	public void setAccessibleBy(AccessibleBy accessibleBy) {
		this.accessibleBy = accessibleBy;
	}

	/**
	 * 
	 * @return
	 * The role
	 */
	@JsonProperty("role")
	public String getRole() {
		return role;
	}

	/**
	 * 
	 * @param role
	 * The role
	 */
	@JsonProperty("role")
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * 
	 * @return
	 * The acknowledgedAt
	 */
	@JsonProperty("acknowledged_at")
	public String getAcknowledgedAt() {
		return acknowledgedAt;
	}

	/**
	 * 
	 * @param acknowledgedAt
	 * The acknowledged_at
	 */
	@JsonProperty("acknowledged_at")
	public void setAcknowledgedAt(String acknowledgedAt) {
		this.acknowledgedAt = acknowledgedAt;
	}

	/**
	 * 
	 * @return
	 * The item
	 */
	@JsonProperty("item")
	public Item getItem() {
		return item;
	}

	/**
	 * 
	 * @param item
	 * The item
	 */
	@JsonProperty("item")
	public void setItem(Item item) {
		this.item = item;
	}

}
