package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ListItemAllFields {

	@JsonProperty("odata.metadata")
	private String odataMetadata;
	@JsonProperty("odata.type")
	private String odataType;
	@JsonProperty("odata.id")
	private String odataId;
	@JsonProperty("odata.etag")
	private String odataEtag;
	@JsonProperty("odata.editLink")
	private String odataEditLink;
	@JsonProperty("FileSystemObjectType")
	private long FileSystemObjectType;
	@JsonProperty("Id")
	private long Id;
	@JsonProperty("ContentTypeId")
	private String ContentTypeId;
	@JsonProperty("Title")
	private String Title;
	@JsonProperty("IsMyDocuments")
	private Object IsMyDocuments;
	@JsonProperty("SharedWithInternalId")
	private Object SharedWithInternalId;
	@JsonProperty("SharedWithUsersId")
	private Object SharedWithUsersId;
	@JsonProperty("SharedWithDetails")
	private Object SharedWithDetails;
	@JsonProperty("Created")
	private String Created;
	@JsonProperty("AuthorId")
	private long AuthorId;
	@JsonProperty("Modified")
	private String Modified;
	@JsonProperty("EditorId")
	private long EditorId;
	@JsonProperty("OData__CopySource")
	private Object ODataCopySource;
	@JsonProperty("CheckoutUserId")
	private Object CheckoutUserId;
	@JsonProperty("OData__UIVersionString")
	private String ODataUIVersionString;
	@JsonProperty("GUID")
	private String GUID;
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
	 * The odataEtag
	 */
	@JsonProperty("odata.etag")
	public String getOdataEtag() {
		return odataEtag;
	}

	/**
	 * 
	 * @param odataEtag
	 * The odata.etag
	 */
	@JsonProperty("odata.etag")
	public void setOdataEtag(String odataEtag) {
		this.odataEtag = odataEtag;
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
	 * The FileSystemObjectType
	 */
	@JsonProperty("FileSystemObjectType")
	public long getFileSystemObjectType() {
		return FileSystemObjectType;
	}

	/**
	 * 
	 * @param FileSystemObjectType
	 * The FileSystemObjectType
	 */
	@JsonProperty("FileSystemObjectType")
	public void setFileSystemObjectType(long FileSystemObjectType) {
		this.FileSystemObjectType = FileSystemObjectType;
	}

	/**
	 * 
	 * @return
	 * The Id
	 */
	@JsonProperty("Id")
	public long getId() {
		return Id;
	}

	/**
	 * 
	 * @param Id
	 * The Id
	 */
	@JsonProperty("Id")
	public void setId(long Id) {
		this.Id = Id;
	}

	/**
	 * 
	 * @return
	 * The ContentTypeId
	 */
	@JsonProperty("ContentTypeId")
	public String getContentTypeId() {
		return ContentTypeId;
	}

	/**
	 * 
	 * @param ContentTypeId
	 * The ContentTypeId
	 */
	@JsonProperty("ContentTypeId")
	public void setContentTypeId(String ContentTypeId) {
		this.ContentTypeId = ContentTypeId;
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
	 * The IsMyDocuments
	 */
	@JsonProperty("IsMyDocuments")
	public Object getIsMyDocuments() {
		return IsMyDocuments;
	}

	/**
	 * 
	 * @param IsMyDocuments
	 * The IsMyDocuments
	 */
	@JsonProperty("IsMyDocuments")
	public void setIsMyDocuments(Object IsMyDocuments) {
		this.IsMyDocuments = IsMyDocuments;
	}

	/**
	 * 
	 * @return
	 * The SharedWithInternalId
	 */
	@JsonProperty("SharedWithInternalId")
	public Object getSharedWithInternalId() {
		return SharedWithInternalId;
	}

	/**
	 * 
	 * @param SharedWithInternalId
	 * The SharedWithInternalId
	 */
	@JsonProperty("SharedWithInternalId")
	public void setSharedWithInternalId(Object SharedWithInternalId) {
		this.SharedWithInternalId = SharedWithInternalId;
	}

	/**
	 * 
	 * @return
	 * The SharedWithUsersId
	 */
	@JsonProperty("SharedWithUsersId")
	public Object getSharedWithUsersId() {
		return SharedWithUsersId;
	}

	/**
	 * 
	 * @param SharedWithUsersId
	 * The SharedWithUsersId
	 */
	@JsonProperty("SharedWithUsersId")
	public void setSharedWithUsersId(Object SharedWithUsersId) {
		this.SharedWithUsersId = SharedWithUsersId;
	}

	/**
	 * 
	 * @return
	 * The SharedWithDetails
	 */
	@JsonProperty("SharedWithDetails")
	public Object getSharedWithDetails() {
		return SharedWithDetails;
	}

	/**
	 * 
	 * @param SharedWithDetails
	 * The SharedWithDetails
	 */
	@JsonProperty("SharedWithDetails")
	public void setSharedWithDetails(Object SharedWithDetails) {
		this.SharedWithDetails = SharedWithDetails;
	}

	

	/**
	 * 
	 * @return
	 * The Created
	 */
	@JsonProperty("Created")
	public String getCreated() {
		return Created;
	}

	/**
	 * 
	 * @param Created
	 * The Created
	 */
	@JsonProperty("Created")
	public void setCreated(String Created) {
		this.Created = Created;
	}

	/**
	 * 
	 * @return
	 * The AuthorId
	 */
	@JsonProperty("AuthorId")
	public long getAuthorId() {
		return AuthorId;
	}

	/**
	 * 
	 * @param AuthorId
	 * The AuthorId
	 */
	@JsonProperty("AuthorId")
	public void setAuthorId(long AuthorId) {
		this.AuthorId = AuthorId;
	}

	/**
	 * 
	 * @return
	 * The Modified
	 */
	@JsonProperty("Modified")
	public String getModified() {
		return Modified;
	}

	/**
	 * 
	 * @param Modified
	 * The Modified
	 */
	@JsonProperty("Modified")
	public void setModified(String Modified) {
		this.Modified = Modified;
	}

	/**
	 * 
	 * @return
	 * The EditorId
	 */
	@JsonProperty("EditorId")
	public long getEditorId() {
		return EditorId;
	}

	/**
	 * 
	 * @param EditorId
	 * The EditorId
	 */
	@JsonProperty("EditorId")
	public void setEditorId(long EditorId) {
		this.EditorId = EditorId;
	}

	/**
	 * 
	 * @return
	 * The ODataCopySource
	 */
	@JsonProperty("OData__CopySource")
	public Object getODataCopySource() {
		return ODataCopySource;
	}

	/**
	 * 
	 * @param ODataCopySource
	 * The OData__CopySource
	 */
	@JsonProperty("OData__CopySource")
	public void setODataCopySource(Object ODataCopySource) {
		this.ODataCopySource = ODataCopySource;
	}

	/**
	 * 
	 * @return
	 * The CheckoutUserId
	 */
	@JsonProperty("CheckoutUserId")
	public Object getCheckoutUserId() {
		return CheckoutUserId;
	}

	/**
	 * 
	 * @param CheckoutUserId
	 * The CheckoutUserId
	 */
	@JsonProperty("CheckoutUserId")
	public void setCheckoutUserId(Object CheckoutUserId) {
		this.CheckoutUserId = CheckoutUserId;
	}

	/**
	 * 
	 * @return
	 * The ODataUIVersionString
	 */
	@JsonProperty("OData__UIVersionString")
	public String getODataUIVersionString() {
		return ODataUIVersionString;
	}

	/**
	 * 
	 * @param ODataUIVersionString
	 * The OData__UIVersionString
	 */
	@JsonProperty("OData__UIVersionString")
	public void setODataUIVersionString(String ODataUIVersionString) {
		this.ODataUIVersionString = ODataUIVersionString;
	}

	/**
	 * 
	 * @return
	 * The GUID
	 */
	@JsonProperty("GUID")
	public String getGUID() {
		return GUID;
	}

	/**
	 * 
	 * @param GUID
	 * The GUID
	 */
	@JsonProperty("GUID")
	public void setGUID(String GUID) {
		this.GUID = GUID;
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
