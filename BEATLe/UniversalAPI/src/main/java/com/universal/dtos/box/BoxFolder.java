package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class BoxFolder {

	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private String id;
	@JsonProperty("sequence_id")
	private Object sequenceId;
	@JsonProperty("etag")
	private String etag;
	@JsonProperty("name")
	private String name;
	@JsonProperty("created_at")
	private Object createdAt;
	@JsonProperty("modified_at")
	private Object modifiedAt;
	@JsonProperty("description")
	private String description;
	@JsonProperty("size")
	private Long size;
	@JsonProperty("path_collection")
	private PathCollection pathCollection;
	@JsonProperty("created_by")
	private CreatedBy createdBy;
	@JsonProperty("modified_by")
	private ModifiedBy modifiedBy;
	@JsonProperty("trashed_at")
	private Object trashedAt;
	@JsonProperty("purged_at")
	private Object purgedAt;
	@JsonProperty("content_created_at")
	private Object contentCreatedAt;
	@JsonProperty("content_modified_at")
	private Object contentModifiedAt;
	@JsonProperty("owned_by")
	private OwnedBy ownedBy;
	@JsonProperty("shared_link")
	private SharedLink sharedLink;
	@JsonProperty("folder_upload_email")
	private Object folderUploadEmail;
	@JsonProperty("parent")
	private Parent parent;
	@JsonProperty("item_status")
	private String itemStatus;
	@JsonProperty("item_collection")
	private ItemCollection itemCollection;
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
	public Object getSequenceId() {
		return sequenceId;
	}

	/**
	 * 
	 * @param sequenceId
	 * The sequence_id
	 */
	@JsonProperty("sequence_id")
	public void setSequenceId(Object sequenceId) {
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
	 * The createdAt
	 */
	@JsonProperty("created_at")
	public Object getCreatedAt() {
		return createdAt;
	}

	/**
	 * 
	 * @param createdAt
	 * The created_at
	 */
	@JsonProperty("created_at")
	public void setCreatedAt(Object createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * 
	 * @return
	 * The modifiedAt
	 */
	@JsonProperty("modified_at")
	public Object getModifiedAt() {
		return modifiedAt;
	}

	/**
	 * 
	 * @param modifiedAt
	 * The modified_at
	 */
	@JsonProperty("modified_at")
	public void setModifiedAt(Object modifiedAt) {
		this.modifiedAt = modifiedAt;
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
	 * The size
	 */
	@JsonProperty("size")
	public Long getSize() {
		return size;
	}

	/**
	 * 
	 * @param size
	 * The size
	 */
	@JsonProperty("size")
	public void setSize(Long size) {
		this.size = size;
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
	 * The contentCreatedAt
	 */
	@JsonProperty("content_created_at")
	public Object getContentCreatedAt() {
		return contentCreatedAt;
	}

	/**
	 * 
	 * @param contentCreatedAt
	 * The content_created_at
	 */
	@JsonProperty("content_created_at")
	public void setContentCreatedAt(Object contentCreatedAt) {
		this.contentCreatedAt = contentCreatedAt;
	}

	/**
	 * 
	 * @return
	 * The contentModifiedAt
	 */
	@JsonProperty("content_modified_at")
	public Object getContentModifiedAt() {
		return contentModifiedAt;
	}

	/**
	 * 
	 * @param contentModifiedAt
	 * The content_modified_at
	 */
	@JsonProperty("content_modified_at")
	public void setContentModifiedAt(Object contentModifiedAt) {
		this.contentModifiedAt = contentModifiedAt;
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

	/**
	 * 
	 * @return
	 * The sharedLink
	 */
	@JsonProperty("shared_link")
	public SharedLink getSharedLink() {
		return sharedLink;
	}

	/**
	 * 
	 * @param sharedLink
	 * The shared_link
	 */
	@JsonProperty("shared_link")
	public void setSharedLink(SharedLink sharedLink) {
		this.sharedLink = sharedLink;
	}

	/**
	 * 
	 * @return
	 * The folderUploadEmail
	 */
	@JsonProperty("folder_upload_email")
	public Object getFolderUploadEmail() {
		return folderUploadEmail;
	}

	/**
	 * 
	 * @param folderUploadEmail
	 * The folder_upload_email
	 */
	@JsonProperty("folder_upload_email")
	public void setFolderUploadEmail(Object folderUploadEmail) {
		this.folderUploadEmail = folderUploadEmail;
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
	 * The itemCollection
	 */
	@JsonProperty("item_collection")
	public ItemCollection getItemCollection() {
		return itemCollection;
	}

	/**
	 * 
	 * @param itemCollection
	 * The item_collection
	 */
	@JsonProperty("item_collection")
	public void setItemCollection(ItemCollection itemCollection) {
		this.itemCollection = itemCollection;
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