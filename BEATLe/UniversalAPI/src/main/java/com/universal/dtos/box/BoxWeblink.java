package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class BoxWeblink {

	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private String id;
	@JsonProperty("sequence_id")
	private String sequenceId;
	@JsonProperty("etag")
	private String etag;
	@JsonProperty("name")
	private String name;
	@JsonProperty("url")
	private String url;
	@JsonProperty("created_by")
	private CreatedBy createdBy;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("modified_at")
	private String modifiedAt;
	@JsonProperty("parent")
	private Parent parent;
	@JsonProperty("description")
	private String description;
	@JsonProperty("item_status")
	private String itemStatus;
	@JsonProperty("trashed_at")
	private Object trashedAt;
	@JsonProperty("purged_at")
	private Object purgedAt;
	@JsonProperty("shared_link")
	private Object sharedLink;
	@JsonProperty("path_collection")
	private PathCollection pathCollection;
	@JsonProperty("modified_by")
	private ModifiedBy modifiedBy;
	@JsonProperty("owned_by")
	private OwnedBy ownedBy;
	

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
	 * The sequenceId
	 */
	@JsonProperty("sequence_id")
	public String getSequenceId() {
		return sequenceId;
	}

	/**
	 * 
	 * @param sequenceId
	 * The sequence_id
	 */
	@JsonProperty("sequence_id")
	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * 
	 * @return
	 * The etag
	 */
	@JsonProperty("etag")
	public String getEtag() {
		return etag;
	}

	/**
	 * 
	 * @param etag
	 * The etag
	 */
	@JsonProperty("etag")
	public void setEtag(String etag) {
		this.etag = etag;
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
	 * The url
	 */
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 * The url
	 */
	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
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
	 * The parent
	 */
	@JsonProperty("parent")
	public Parent getParent() {
		return parent;
	}

	/**
	 * 
	 * @param parent
	 * The parent
	 */
	@JsonProperty("parent")
	public void setParent(Parent parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * @return
	 * The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 * The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 * The itemStatus
	 */
	@JsonProperty("item_status")
	public String getItemStatus() {
		return itemStatus;
	}

	/**
	 * 
	 * @param itemStatus
	 * The item_status
	 */
	@JsonProperty("item_status")
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	/**
	 * 
	 * @return
	 * The trashedAt
	 */
	@JsonProperty("trashed_at")
	public Object getTrashedAt() {
		return trashedAt;
	}

	/**
	 * 
	 * @param trashedAt
	 * The trashed_at
	 */
	@JsonProperty("trashed_at")
	public void setTrashedAt(Object trashedAt) {
		this.trashedAt = trashedAt;
	}

	/**
	 * 
	 * @return
	 * The purgedAt
	 */
	@JsonProperty("purged_at")
	public Object getPurgedAt() {
		return purgedAt;
	}

	/**
	 * 
	 * @param purgedAt
	 * The purged_at
	 */
	@JsonProperty("purged_at")
	public void setPurgedAt(Object purgedAt) {
		this.purgedAt = purgedAt;
	}

	/**
	 * 
	 * @return
	 * The sharedLink
	 */
	@JsonProperty("shared_link")
	public Object getSharedLink() {
		return sharedLink;
	}

	/**
	 * 
	 * @param sharedLink
	 * The shared_link
	 */
	@JsonProperty("shared_link")
	public void setSharedLink(Object sharedLink) {
		this.sharedLink = sharedLink;
	}

	/**
	 * 
	 * @return
	 * The pathCollection
	 */
	@JsonProperty("path_collection")
	public PathCollection getPathCollection() {
		return pathCollection;
	}

	/**
	 * 
	 * @param pathCollection
	 * The path_collection
	 */
	@JsonProperty("path_collection")
	public void setPathCollection(PathCollection pathCollection) {
		this.pathCollection = pathCollection;
	}

	/**
	 * 
	 * @return
	 * The modifiedBy
	 */
	@JsonProperty("modified_by")
	public ModifiedBy getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * 
	 * @param modifiedBy
	 * The modified_by
	 */
	@JsonProperty("modified_by")
	public void setModifiedBy(ModifiedBy modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * 
	 * @return
	 * The ownedBy
	 */
	@JsonProperty("owned_by")
	public OwnedBy getOwnedBy() {
		return ownedBy;
	}

	/**
	 * 
	 * @param ownedBy
	 * The owned_by
	 */
	@JsonProperty("owned_by")
	public void setOwnedBy(OwnedBy ownedBy) {
		this.ownedBy = ownedBy;
	}

	

}
