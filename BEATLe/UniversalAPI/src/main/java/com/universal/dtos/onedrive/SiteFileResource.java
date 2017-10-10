package com.universal.dtos.onedrive;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class SiteFileResource {

	@JsonProperty("odata.metadata")
	private String odataMetadata;
	@JsonProperty("odata.type")
	private String odataType;
	@JsonProperty("odata.id")
	private String odataId;
	@JsonProperty("odata.editLink")
	private String odataEditLink;
	@JsonProperty("CheckInComment")
	private String checkInComment;
	@JsonProperty("CheckOutType")
	private Integer checkOutType;
	@JsonProperty("ContentTag")
	private String contentTag;
	@JsonProperty("CustomizedPageStatus")
	private Integer customizedPageStatus;
	@JsonProperty("ETag")
	private String eTag;
	@JsonProperty("Exists")
	private Boolean exists;
	@JsonProperty("IrmEnabled")
	private Boolean irmEnabled;
	@JsonProperty("Length")
	private String length;
	@JsonProperty("Level")
	private Integer level;
	@JsonProperty("LinkingUrl")
	private String linkingUrl;
	@JsonProperty("MajorVersion")
	private Integer majorVersion;
	@JsonProperty("MinorVersion")
	private Integer minorVersion;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("ServerRelativeUrl")
	private String serverRelativeUrl;
	@JsonProperty("TimeCreated")
	private String timeCreated;
	@JsonProperty("TimeLastModified")
	private String timeLastModified;
	@JsonProperty("Title")
	private Object title;
	@JsonProperty("UIVersion")
	private Integer uIVersion;
	@JsonProperty("UIVersionLabel")
	private String uIVersionLabel;
	@JsonProperty("UniqueId")
	private String uniqueId;
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
	 * The checkInComment
	 */
	@JsonProperty("CheckInComment")
	public String getCheckInComment() {
		return checkInComment;
	}

	/**
	 * 
	 * @param checkInComment
	 * The CheckInComment
	 */
	@JsonProperty("CheckInComment")
	public void setCheckInComment(String checkInComment) {
		this.checkInComment = checkInComment;
	}

	/**
	 * 
	 * @return
	 * The checkOutType
	 */
	@JsonProperty("CheckOutType")
	public Integer getCheckOutType() {
		return checkOutType;
	}

	/**
	 * 
	 * @param checkOutType
	 * The CheckOutType
	 */
	@JsonProperty("CheckOutType")
	public void setCheckOutType(Integer checkOutType) {
		this.checkOutType = checkOutType;
	}

	/**
	 * 
	 * @return
	 * The contentTag
	 */
	@JsonProperty("ContentTag")
	public String getContentTag() {
		return contentTag;
	}

	/**
	 * 
	 * @param contentTag
	 * The ContentTag
	 */
	@JsonProperty("ContentTag")
	public void setContentTag(String contentTag) {
		this.contentTag = contentTag;
	}

	/**
	 * 
	 * @return
	 * The customizedPageStatus
	 */
	@JsonProperty("CustomizedPageStatus")
	public Integer getCustomizedPageStatus() {
		return customizedPageStatus;
	}

	/**
	 * 
	 * @param customizedPageStatus
	 * The CustomizedPageStatus
	 */
	@JsonProperty("CustomizedPageStatus")
	public void setCustomizedPageStatus(Integer customizedPageStatus) {
		this.customizedPageStatus = customizedPageStatus;
	}

	/**
	 * 
	 * @return
	 * The eTag
	 */
	@JsonProperty("ETag")
	public String getETag() {
		return eTag;
	}

	/**
	 * 
	 * @param eTag
	 * The ETag
	 */
	@JsonProperty("ETag")
	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	/**
	 * 
	 * @return
	 * The exists
	 */
	@JsonProperty("Exists")
	public Boolean getExists() {
		return exists;
	}

	/**
	 * 
	 * @param exists
	 * The Exists
	 */
	@JsonProperty("Exists")
	public void setExists(Boolean exists) {
		this.exists = exists;
	}

	/**
	 * 
	 * @return
	 * The irmEnabled
	 */
	@JsonProperty("IrmEnabled")
	public Boolean getIrmEnabled() {
		return irmEnabled;
	}

	/**
	 * 
	 * @param irmEnabled
	 * The IrmEnabled
	 */
	@JsonProperty("IrmEnabled")
	public void setIrmEnabled(Boolean irmEnabled) {
		this.irmEnabled = irmEnabled;
	}

	/**
	 * 
	 * @return
	 * The length
	 */
	@JsonProperty("Length")
	public String getLength() {
		return length;
	}

	/**
	 * 
	 * @param length
	 * The Length
	 */
	@JsonProperty("Length")
	public void setLength(String length) {
		this.length = length;
	}

	/**
	 * 
	 * @return
	 * The level
	 */
	@JsonProperty("Level")
	public Integer getLevel() {
		return level;
	}

	/**
	 * 
	 * @param level
	 * The Level
	 */
	@JsonProperty("Level")
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * 
	 * @return
	 * The linkingUrl
	 */
	@JsonProperty("LinkingUrl")
	public String getLinkingUrl() {
		return linkingUrl;
	}

	/**
	 * 
	 * @param linkingUrl
	 * The LinkingUrl
	 */
	@JsonProperty("LinkingUrl")
	public void setLinkingUrl(String linkingUrl) {
		this.linkingUrl = linkingUrl;
	}

	/**
	 * 
	 * @return
	 * The majorVersion
	 */
	@JsonProperty("MajorVersion")
	public Integer getMajorVersion() {
		return majorVersion;
	}

	/**
	 * 
	 * @param majorVersion
	 * The MajorVersion
	 */
	@JsonProperty("MajorVersion")
	public void setMajorVersion(Integer majorVersion) {
		this.majorVersion = majorVersion;
	}

	/**
	 * 
	 * @return
	 * The minorVersion
	 */
	@JsonProperty("MinorVersion")
	public Integer getMinorVersion() {
		return minorVersion;
	}

	/**
	 * 
	 * @param minorVersion
	 * The MinorVersion
	 */
	@JsonProperty("MinorVersion")
	public void setMinorVersion(Integer minorVersion) {
		this.minorVersion = minorVersion;
	}

	/**
	 * 
	 * @return
	 * The name
	 */
	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 * The Name
	 */
	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 * The serverRelativeUrl
	 */
	@JsonProperty("ServerRelativeUrl")
	public String getServerRelativeUrl() {
		return serverRelativeUrl;
	}

	/**
	 * 
	 * @param serverRelativeUrl
	 * The ServerRelativeUrl
	 */
	@JsonProperty("ServerRelativeUrl")
	public void setServerRelativeUrl(String serverRelativeUrl) {
		this.serverRelativeUrl = serverRelativeUrl;
	}

	/**
	 * 
	 * @return
	 * The timeCreated
	 */
	@JsonProperty("TimeCreated")
	public String getTimeCreated() {
		return timeCreated;
	}

	/**
	 * 
	 * @param timeCreated
	 * The TimeCreated
	 */
	@JsonProperty("TimeCreated")
	public void setTimeCreated(String timeCreated) {
		this.timeCreated = timeCreated;
	}

	/**
	 * 
	 * @return
	 * The timeLastModified
	 */
	@JsonProperty("TimeLastModified")
	public String getTimeLastModified() {
		return timeLastModified;
	}

	/**
	 * 
	 * @param timeLastModified
	 * The TimeLastModified
	 */
	@JsonProperty("TimeLastModified")
	public void setTimeLastModified(String timeLastModified) {
		this.timeLastModified = timeLastModified;
	}

	/**
	 * 
	 * @return
	 * The title
	 */
	@JsonProperty("Title")
	public Object getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 * The Title
	 */
	@JsonProperty("Title")
	public void setTitle(Object title) {
		this.title = title;
	}

	/**
	 * 
	 * @return
	 * The uIVersion
	 */
	@JsonProperty("UIVersion")
	public Integer getUIVersion() {
		return uIVersion;
	}

	/**
	 * 
	 * @param uIVersion
	 * The UIVersion
	 */
	@JsonProperty("UIVersion")
	public void setUIVersion(Integer uIVersion) {
		this.uIVersion = uIVersion;
	}

	/**
	 * 
	 * @return
	 * The uIVersionLabel
	 */
	@JsonProperty("UIVersionLabel")
	public String getUIVersionLabel() {
		return uIVersionLabel;
	}

	/**
	 * 
	 * @param uIVersionLabel
	 * The UIVersionLabel
	 */
	@JsonProperty("UIVersionLabel")
	public void setUIVersionLabel(String uIVersionLabel) {
		this.uIVersionLabel = uIVersionLabel;
	}

	/**
	 * 
	 * @return
	 * The uniqueId
	 */
	@JsonProperty("UniqueId")
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * 
	 * @param uniqueId
	 * The UniqueId
	 */
	@JsonProperty("UniqueId")
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
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