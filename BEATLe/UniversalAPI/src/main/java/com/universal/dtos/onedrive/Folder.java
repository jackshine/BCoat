package com.universal.dtos.onedrive;

import org.codehaus.jackson.annotate.JsonProperty;

public class Folder {

	@JsonProperty("@odata.context")
	private String OdataContext;
	@JsonProperty("@odata.type")
	private String OdataType;
	@JsonProperty("@odata.id")
	private String OdataId;
	@JsonProperty("@odata.etag")
	private String OdataEtag;
	@JsonProperty("@odata.editLink")
	private String OdataEditLink;
	@JsonProperty("createdBy")
	private CreatedBy createdBy;
	@JsonProperty("eTag")
	private String eTag;
	@JsonProperty("id")
	private String id;
	@JsonProperty("lastModifiedBy")
	private LastModifiedBy lastModifiedBy;
	@JsonProperty("name")
	private String name;
	@JsonProperty("parentReference")
	private ParentReference parentReference;
	@JsonProperty("size")
	private Integer size;
	@JsonProperty("dateTimeCreated")
	private String dateTimeCreated;
	@JsonProperty("dateTimeLastModified")
	private String dateTimeLastModified;
	@JsonProperty("type")
	private String type;
	@JsonProperty("webUrl")
	private String webUrl;
	@JsonProperty("childCount")
	private Integer childCount;
	

	/**
	 *
	 * @return
	 * The OdataContext
	 */
	@JsonProperty("@odata.context")
	public String getOdataContext() {
		return OdataContext;
	}

	/**
	 *
	 * @param OdataContext
	 * The @odata.context
	 */
	@JsonProperty("@odata.context")
	public void setOdataContext(String OdataContext) {
		this.OdataContext = OdataContext;
	}

	/**
	 *
	 * @return
	 * The OdataType
	 */
	@JsonProperty("@odata.type")
	public String getOdataType() {
		return OdataType;
	}

	/**
	 *
	 * @param OdataType
	 * The @odata.type
	 */
	@JsonProperty("@odata.type")
	public void setOdataType(String OdataType) {
		this.OdataType = OdataType;
	}

	/**
	 *
	 * @return
	 * The OdataId
	 */
	@JsonProperty("@odata.id")
	public String getOdataId() {
		return OdataId;
	}

	/**
	 *
	 * @param OdataId
	 * The @odata.id
	 */
	@JsonProperty("@odata.id")
	public void setOdataId(String OdataId) {
		this.OdataId = OdataId;
	}

	/**
	 *
	 * @return
	 * The OdataEtag
	 */
	@JsonProperty("@odata.etag")
	public String getOdataEtag() {
		return OdataEtag;
	}

	/**
	 *
	 * @param OdataEtag
	 * The @odata.etag
	 */
	@JsonProperty("@odata.etag")
	public void setOdataEtag(String OdataEtag) {
		this.OdataEtag = OdataEtag;
	}

	/**
	 *
	 * @return
	 * The OdataEditLink
	 */
	@JsonProperty("@odata.editLink")
	public String getOdataEditLink() {
		return OdataEditLink;
	}

	/**
	 *
	 * @param OdataEditLink
	 * The @odata.editLink
	 */
	@JsonProperty("@odata.editLink")
	public void setOdataEditLink(String OdataEditLink) {
		this.OdataEditLink = OdataEditLink;
	}

	/**
	 *
	 * @return
	 * The createdBy
	 */
	@JsonProperty("createdBy")
	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	/**
	 *
	 * @param createdBy
	 * The createdBy
	 */
	@JsonProperty("createdBy")
	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 *
	 * @return
	 * The eTag
	 */
	@JsonProperty("eTag")
	public String getETag() {
		return eTag;
	}

	/**
	 *
	 * @param eTag
	 * The eTag
	 */
	@JsonProperty("eTag")
	public void setETag(String eTag) {
		this.eTag = eTag;
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
	 * The lastModifiedBy
	 */
	@JsonProperty("lastModifiedBy")
	public LastModifiedBy getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 *
	 * @param lastModifiedBy
	 * The lastModifiedBy
	 */
	@JsonProperty("lastModifiedBy")
	public void setLastModifiedBy(LastModifiedBy lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
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
	 * The parentReference
	 */
	@JsonProperty("parentReference")
	public ParentReference getParentReference() {
		return parentReference;
	}

	/**
	 *
	 * @param parentReference
	 * The parentReference
	 */
	@JsonProperty("parentReference")
	public void setParentReference(ParentReference parentReference) {
		this.parentReference = parentReference;
	}

	/**
	 *
	 * @return
	 * The size
	 */
	@JsonProperty("size")
	public Integer getSize() {
		return size;
	}

	/**
	 *
	 * @param size
	 * The size
	 */
	@JsonProperty("size")
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 *
	 * @return
	 * The dateTimeCreated
	 */
	@JsonProperty("dateTimeCreated")
	public String getDateTimeCreated() {
		return dateTimeCreated;
	}

	/**
	 *
	 * @param dateTimeCreated
	 * The dateTimeCreated
	 */
	@JsonProperty("dateTimeCreated")
	public void setDateTimeCreated(String dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}

	/**
	 *
	 * @return
	 * The dateTimeLastModified
	 */
	@JsonProperty("dateTimeLastModified")
	public String getDateTimeLastModified() {
		return dateTimeLastModified;
	}

	/**
	 *
	 * @param dateTimeLastModified
	 * The dateTimeLastModified
	 */
	@JsonProperty("dateTimeLastModified")
	public void setDateTimeLastModified(String dateTimeLastModified) {
		this.dateTimeLastModified = dateTimeLastModified;
	}

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
	 * The webUrl
	 */
	@JsonProperty("webUrl")
	public String getWebUrl() {
		return webUrl;
	}

	/**
	 *
	 * @param webUrl
	 * The webUrl
	 */
	@JsonProperty("webUrl")
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	/**
	 *
	 * @return
	 * The childCount
	 */
	@JsonProperty("childCount")
	public Integer getChildCount() {
		return childCount;
	}

	/**
	 *
	 * @param childCount
	 * The childCount
	 */
	@JsonProperty("childCount")
	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	

}