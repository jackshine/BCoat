package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;


public class FileEntry {

	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private String id;
	@JsonProperty("file_version")
	private FileVersion fileVersion;
	@JsonProperty("sequence_id")
	private String sequenceId;
	@JsonProperty("etag")
	private String etag;
	@JsonProperty("sha1")
	private String sha1;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("size")
	private int size;
	@JsonProperty("path_collection")
	private PathCollection pathCollection;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("modified_at")
	private String modifiedAt;
	@JsonProperty("trashed_at")
	private Object trashedAt;
	@JsonProperty("purged_at")
	private Object purgedAt;
	@JsonProperty("content_created_at")
	private String contentCreatedAt;
	@JsonProperty("content_modified_at")
	private String contentModifiedAt;
	@JsonProperty("created_by")
	private CreatedBy createdBy;
	@JsonProperty("modified_by")
	private ModifiedBy modifiedBy;
	@JsonProperty("owned_by")
	private OwnedBy ownedBy;
	@JsonProperty("shared_link")
	private SharedLink sharedLink;
	@JsonProperty("parent")
	private Parent parent;
	@JsonProperty("item_status")
	private String itemStatus;
	

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
	 * The fileVersion
	 */
	@JsonProperty("file_version")
	public FileVersion getFileVersion() {
		return fileVersion;
	}

	/**
	 *
	 * @param fileVersion
	 * The file_version
	 */
	@JsonProperty("file_version")
	public void setFileVersion(FileVersion fileVersion) {
		this.fileVersion = fileVersion;
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
	 * The sha1
	 */
	@JsonProperty("sha1")
	public String getSha1() {
		return sha1;
	}

	/**
	 *
	 * @param sha1
	 * The sha1
	 */
	@JsonProperty("sha1")
	public void setSha1(String sha1) {
		this.sha1 = sha1;
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
	public int getSize() {
		return size;
	}

	/**
	 *
	 * @param size
	 * The size
	 */
	@JsonProperty("size")
	public void setSize(int size) {
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
	public String getContentCreatedAt() {
		return contentCreatedAt;
	}

	/**
	 *
	 * @param contentCreatedAt
	 * The content_created_at
	 */
	@JsonProperty("content_created_at")
	public void setContentCreatedAt(String contentCreatedAt) {
		this.contentCreatedAt = contentCreatedAt;
	}

	/**
	 *
	 * @return
	 * The contentModifiedAt
	 */
	@JsonProperty("content_modified_at")
	public String getContentModifiedAt() {
		return contentModifiedAt;
	}

	/**
	 *
	 * @param contentModifiedAt
	 * The content_modified_at
	 */
	@JsonProperty("content_modified_at")
	public void setContentModifiedAt(String contentModifiedAt) {
		this.contentModifiedAt = contentModifiedAt;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}



}
