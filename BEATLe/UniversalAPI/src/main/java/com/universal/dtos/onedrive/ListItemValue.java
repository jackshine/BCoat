package com.universal.dtos.onedrive;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSetter;
import org.codehaus.jackson.map.annotate.JsonSerialize;


public class ListItemValue {

	@JsonProperty("odata.type")
	private String odataType;
	@JsonProperty("odata.id")
	private String odataId;
	@JsonProperty("odata.etag")
	private String odataEtag;
	@JsonProperty("odata.editLink")
	private String odataEditLink;
	@JsonProperty("FileSystemObjectType")
	private Integer fileSystemObjectType;
	@JsonProperty("Id")
	private Integer id;
	@JsonProperty("ServerRedirectedEmbedUrl")
	private String serverRedirectedEmbedUrl;
	@JsonProperty("ContentTypeId")
	private String contentTypeId;
	@JsonProperty("Title")
	private String title;
	@JsonProperty("IsMyDocuments")
	private Object isMyDocuments;
	@JsonProperty("SharedWithInternalId")
	private Object sharedWithInternalId;
	@JsonProperty("SharedWithInternalStringId")
	private Object sharedWithInternalStringId;
	@JsonProperty("SharedWithUsersId")
	private List<Integer> sharedWithUsersId = new ArrayList<Integer>();
	@JsonProperty("SharedWithDetails")
	private String sharedWithDetails;
	@JsonProperty("Created")
	private String created;
	@JsonProperty("AuthorId")
	private Integer authorId;
	@JsonProperty("Modified")
	private String modified;
	@JsonProperty("EditorId")
	private Integer editorId;
	@JsonProperty("OData__CopySource")
	private Object oDataCopySource;
	@JsonProperty("CheckoutUserId")
	private Object checkoutUserId;
	@JsonProperty("OData__UIVersionString")
	private String oDataUIVersionString;
	@JsonProperty("GUID")
	private String gUID;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
	 * The fileSystemObjectType
	 */
	@JsonProperty("FileSystemObjectType")
	public Integer getFileSystemObjectType() {
		return fileSystemObjectType;
	}

	/**
	 * 
	 * @param fileSystemObjectType
	 * The FileSystemObjectType
	 */
	@JsonProperty("FileSystemObjectType")
	public void setFileSystemObjectType(Integer fileSystemObjectType) {
		this.fileSystemObjectType = fileSystemObjectType;
	}

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("Id")
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The Id
	 */
	@JsonProperty("Id")
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The serverRedirectedEmbedUrl
	 */
	@JsonProperty("ServerRedirectedEmbedUrl")
	public String getServerRedirectedEmbedUrl() {
		return serverRedirectedEmbedUrl;
	}

	/**
	 * 
	 * @param serverRedirectedEmbedUrl
	 * The ServerRedirectedEmbedUrl
	 */
	@JsonProperty("ServerRedirectedEmbedUrl")
	public void setServerRedirectedEmbedUrl(String serverRedirectedEmbedUrl) {
		this.serverRedirectedEmbedUrl = serverRedirectedEmbedUrl;
	}

	/**
	 * 
	 * @return
	 * The contentTypeId
	 */
	@JsonProperty("ContentTypeId")
	public String getContentTypeId() {
		return contentTypeId;
	}

	/**
	 * 
	 * @param contentTypeId
	 * The ContentTypeId
	 */
	@JsonProperty("ContentTypeId")
	public void setContentTypeId(String contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	/**
	 * 
	 * @return
	 * The title
	 */
	@JsonProperty("Title")
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 * The Title
	 */
	@JsonProperty("Title")
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return
	 * The isMyDocuments
	 */
	@JsonProperty("IsMyDocuments")
	public Object getIsMyDocuments() {
		return isMyDocuments;
	}

	/**
	 * 
	 * @param isMyDocuments
	 * The IsMyDocuments
	 */
	@JsonProperty("IsMyDocuments")
	public void setIsMyDocuments(Object isMyDocuments) {
		this.isMyDocuments = isMyDocuments;
	}

	/**
	 * 
	 * @return
	 * The sharedWithInternalId
	 */
	@JsonProperty("SharedWithInternalId")
	public Object getSharedWithInternalId() {
		return sharedWithInternalId;
	}

	/**
	 * 
	 * @param sharedWithInternalId
	 * The SharedWithInternalId
	 */
	@JsonProperty("SharedWithInternalId")
	public void setSharedWithInternalId(Object sharedWithInternalId) {
		this.sharedWithInternalId = sharedWithInternalId;
	}

	/**
	 * 
	 * @return
	 * The sharedWithInternalStringId
	 */
	@JsonProperty("SharedWithInternalStringId")
	public Object getSharedWithInternalStringId() {
		return sharedWithInternalStringId;
	}

	/**
	 * 
	 * @param sharedWithInternalStringId
	 * The SharedWithInternalStringId
	 */
	@JsonProperty("SharedWithInternalStringId")
	public void setSharedWithInternalStringId(Object sharedWithInternalStringId) {
		this.sharedWithInternalStringId = sharedWithInternalStringId;
	}

	/**
	 * 
	 * @return
	 * The sharedWithUsersId
	 */
	@JsonProperty("SharedWithUsersId")
	public List<Integer> getSharedWithUsersId() {
		return sharedWithUsersId;
	}

	/**
	 * 
	 * @param sharedWithUsersId
	 * The SharedWithUsersId
	 */
	@JsonProperty("SharedWithUsersId")
	public void setSharedWithUsersId(List<Integer> sharedWithUsersId) {
		this.sharedWithUsersId = sharedWithUsersId;
	}

	/**
	 * 
	 * @return
	 * The sharedWithDetails
	 */
	@JsonProperty("SharedWithDetails")
	public String getSharedWithDetails() {
		return sharedWithDetails;
	}

	/**
	 * 
	 * @param sharedWithDetails
	 * The SharedWithDetails
	 */
	@JsonProperty("SharedWithDetails")
	public void setSharedWithDetails(String sharedWithDetails) {
		this.sharedWithDetails = sharedWithDetails;
	}

	

	/**
	 * 
	 * @return
	 * The created
	 */
	@JsonProperty("Created")
	public String getCreated() {
		return created;
	}

	/**
	 * 
	 * @param created
	 * The Created
	 */
	@JsonProperty("Created")
	public void setCreated(String created) {
		this.created = created;
	}

	/**
	 * 
	 * @return
	 * The authorId
	 */
	@JsonProperty("AuthorId")
	public Integer getAuthorId() {
		return authorId;
	}

	/**
	 * 
	 * @param authorId
	 * The AuthorId
	 */
	@JsonProperty("AuthorId")
	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	/**
	 * 
	 * @return
	 * The modified
	 */
	@JsonProperty("Modified")
	public String getModified() {
		return modified;
	}

	/**
	 * 
	 * @param modified
	 * The Modified
	 */
	@JsonProperty("Modified")
	public void setModified(String modified) {
		this.modified = modified;
	}

	/**
	 * 
	 * @return
	 * The editorId
	 */
	@JsonProperty("EditorId")
	public Integer getEditorId() {
		return editorId;
	}

	/**
	 * 
	 * @param editorId
	 * The EditorId
	 */
	@JsonProperty("EditorId")
	public void setEditorId(Integer editorId) {
		this.editorId = editorId;
	}

	/**
	 * 
	 * @return
	 * The oDataCopySource
	 */
	@JsonProperty("OData__CopySource")
	public Object getODataCopySource() {
		return oDataCopySource;
	}

	/**
	 * 
	 * @param oDataCopySource
	 * The OData__CopySource
	 */
	@JsonProperty("OData__CopySource")
	public void setODataCopySource(Object oDataCopySource) {
		this.oDataCopySource = oDataCopySource;
	}

	/**
	 * 
	 * @return
	 * The checkoutUserId
	 */
	@JsonProperty("CheckoutUserId")
	public Object getCheckoutUserId() {
		return checkoutUserId;
	}

	/**
	 * 
	 * @param checkoutUserId
	 * The CheckoutUserId
	 */
	@JsonProperty("CheckoutUserId")
	public void setCheckoutUserId(Object checkoutUserId) {
		this.checkoutUserId = checkoutUserId;
	}

	/**
	 * 
	 * @return
	 * The oDataUIVersionString
	 */
	@JsonProperty("OData__UIVersionString")
	public String getODataUIVersionString() {
		return oDataUIVersionString;
	}

	/**
	 * 
	 * @param oDataUIVersionString
	 * The OData__UIVersionString
	 */
	@JsonProperty("OData__UIVersionString")
	public void setODataUIVersionString(String oDataUIVersionString) {
		this.oDataUIVersionString = oDataUIVersionString;
	}

	/**
	 * 
	 * @return
	 * The gUID
	 */
	@JsonProperty("GUID")
	public String getGUID() {
		return gUID;
	}

	/**
	 * 
	 * @param gUID
	 * The GUID
	 */
	@JsonProperty("GUID")
	public void setGUID(String gUID) {
		this.gUID = gUID;
	}

	

}
