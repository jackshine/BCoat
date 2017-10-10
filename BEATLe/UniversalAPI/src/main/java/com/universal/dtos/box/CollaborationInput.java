package com.universal.dtos.box;

import org.codehaus.jackson.annotate.JsonProperty;

public class CollaborationInput {

	@JsonProperty("fields")
	private String fields;
	@JsonProperty("notify")
	private Boolean notify;
	@JsonProperty("item")
	private Item item;
	@JsonProperty("accessible_by")
	private AccessibleBy accessibleBy;
	@JsonProperty("role")
	private String role;
	

	/**
	 *
	 * @return
	 * The fields
	 */
	@JsonProperty("fields")
	public String getFields() {
		return fields;
	}

	/**
	 *
	 * @param fields
	 * The fields
	 */
	@JsonProperty("fields")
	public void setFields(String fields) {
		this.fields = fields;
	}

	/**
	 *
	 * @return
	 * The notify
	 */
	@JsonProperty("notify")
	public Boolean getNotify() {
		return notify;
	}

	/**
	 *
	 * @param notify
	 * The notify
	 */
	@JsonProperty("notify")
	public void setNotify(Boolean notify) {
		this.notify = notify;
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
}