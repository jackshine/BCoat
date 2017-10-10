package com.universal.dtos.salesforce;
import org.codehaus.jackson.annotate.JsonProperty;

public class ChatterFile {

	@JsonProperty("checksum")
	private String checksum;
	@JsonProperty("contentHubRepository")
	private Object contentHubRepository;
	@JsonProperty("contentModifiedDate")
	private String contentModifiedDate;
	@JsonProperty("contentSize")
	private Integer contentSize;
	@JsonProperty("contentUrl")
	private Object contentUrl;
	@JsonProperty("description")
	private Object description;
	@JsonProperty("downloadUrl")
	private String downloadUrl;
	@JsonProperty("externalDocumentUrl")
	private Object externalDocumentUrl;
	@JsonProperty("fileExtension")
	private String fileExtension;
	@JsonProperty("fileType")
	private String fileType;
	@JsonProperty("flashRenditionStatus")
	private String flashRenditionStatus;
	@JsonProperty("id")
	private String id;
	@JsonProperty("isInMyFileSync")
	private Boolean isInMyFileSync;
	@JsonProperty("isMajorVersion")
	private Boolean isMajorVersion;
	@JsonProperty("mimeType")
	private String mimeType;
	@JsonProperty("moderationFlags")
	private Object moderationFlags;
	@JsonProperty("modifiedDate")
	private String modifiedDate;
	@JsonProperty("motif")
	private Motif motif;
	@JsonProperty("mySubscription")
	private Object mySubscription;
	@JsonProperty("name")
	private String name;
	@JsonProperty("origin")
	private String origin;
	@JsonProperty("owner")
	private Owner owner;
	@JsonProperty("pageCount")
	private Integer pageCount;
	@JsonProperty("parentFolder")
	private Object parentFolder;
	@JsonProperty("pdfRenditionStatus")
	private String pdfRenditionStatus;
	@JsonProperty("publishStatus")
	private String publishStatus;
	@JsonProperty("renditionUrl")
	private Object renditionUrl;
	@JsonProperty("renditionUrl240By180")
	private Object renditionUrl240By180;
	@JsonProperty("renditionUrl720By480")
	private Object renditionUrl720By480;
	@JsonProperty("repositoryFileUrl")
	private Object repositoryFileUrl;
	@JsonProperty("sharingRole")
	private String sharingRole;
	@JsonProperty("textPreview")
	private Object textPreview;
	@JsonProperty("thumb120By90RenditionStatus")
	private String thumb120By90RenditionStatus;
	@JsonProperty("thumb240By180RenditionStatus")
	private String thumb240By180RenditionStatus;
	@JsonProperty("thumb720By480RenditionStatus")
	private String thumb720By480RenditionStatus;
	@JsonProperty("title")
	private String title;
	@JsonProperty("type")
	private String type;
	@JsonProperty("url")
	private String url;
	@JsonProperty("versionNumber")
	private String versionNumber;

	/**
	 * 
	 * @return
	 * The checksum
	 */
	@JsonProperty("checksum")
	public String getChecksum() {
		return checksum;
	}

	/**
	 * 
	 * @param checksum
	 * The checksum
	 */
	@JsonProperty("checksum")
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	/**
	 * 
	 * @return
	 * The contentHubRepository
	 */
	@JsonProperty("contentHubRepository")
	public Object getContentHubRepository() {
		return contentHubRepository;
	}

	/**
	 * 
	 * @param contentHubRepository
	 * The contentHubRepository
	 */
	@JsonProperty("contentHubRepository")
	public void setContentHubRepository(Object contentHubRepository) {
		this.contentHubRepository = contentHubRepository;
	}

	/**
	 * 
	 * @return
	 * The contentModifiedDate
	 */
	@JsonProperty("contentModifiedDate")
	public String getContentModifiedDate() {
		return contentModifiedDate;
	}

	/**
	 * 
	 * @param contentModifiedDate
	 * The contentModifiedDate
	 */
	@JsonProperty("contentModifiedDate")
	public void setContentModifiedDate(String contentModifiedDate) {
		this.contentModifiedDate = contentModifiedDate;
	}

	/**
	 * 
	 * @return
	 * The contentSize
	 */
	@JsonProperty("contentSize")
	public Integer getContentSize() {
		return contentSize;
	}

	/**
	 * 
	 * @param contentSize
	 * The contentSize
	 */
	@JsonProperty("contentSize")
	public void setContentSize(Integer contentSize) {
		this.contentSize = contentSize;
	}

	/**
	 * 
	 * @return
	 * The contentUrl
	 */
	@JsonProperty("contentUrl")
	public Object getContentUrl() {
		return contentUrl;
	}

	/**
	 * 
	 * @param contentUrl
	 * The contentUrl
	 */
	@JsonProperty("contentUrl")
	public void setContentUrl(Object contentUrl) {
		this.contentUrl = contentUrl;
	}

	/**
	 * 
	 * @return
	 * The description
	 */
	@JsonProperty("description")
	public Object getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 * The description
	 */
	@JsonProperty("description")
	public void setDescription(Object description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 * The downloadUrl
	 */
	@JsonProperty("downloadUrl")
	public String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * 
	 * @param downloadUrl
	 * The downloadUrl
	 */
	@JsonProperty("downloadUrl")
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	/**
	 * 
	 * @return
	 * The externalDocumentUrl
	 */
	@JsonProperty("externalDocumentUrl")
	public Object getExternalDocumentUrl() {
		return externalDocumentUrl;
	}

	/**
	 * 
	 * @param externalDocumentUrl
	 * The externalDocumentUrl
	 */
	@JsonProperty("externalDocumentUrl")
	public void setExternalDocumentUrl(Object externalDocumentUrl) {
		this.externalDocumentUrl = externalDocumentUrl;
	}

	/**
	 * 
	 * @return
	 * The fileExtension
	 */
	@JsonProperty("fileExtension")
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * 
	 * @param fileExtension
	 * The fileExtension
	 */
	@JsonProperty("fileExtension")
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * 
	 * @return
	 * The fileType
	 */
	@JsonProperty("fileType")
	public String getFileType() {
		return fileType.toUpperCase();
	}

	/**
	 * 
	 * @param fileType
	 * The fileType
	 */
	@JsonProperty("fileType")
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 
	 * @return
	 * The flashRenditionStatus
	 */
	@JsonProperty("flashRenditionStatus")
	public String getFlashRenditionStatus() {
		return flashRenditionStatus;
	}

	/**
	 * 
	 * @param flashRenditionStatus
	 * The flashRenditionStatus
	 */
	@JsonProperty("flashRenditionStatus")
	public void setFlashRenditionStatus(String flashRenditionStatus) {
		this.flashRenditionStatus = flashRenditionStatus;
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
	 * The isInMyFileSync
	 */
	@JsonProperty("isInMyFileSync")
	public Boolean getIsInMyFileSync() {
		return isInMyFileSync;
	}

	/**
	 * 
	 * @param isInMyFileSync
	 * The isInMyFileSync
	 */
	@JsonProperty("isInMyFileSync")
	public void setIsInMyFileSync(Boolean isInMyFileSync) {
		this.isInMyFileSync = isInMyFileSync;
	}

	/**
	 * 
	 * @return
	 * The isMajorVersion
	 */
	@JsonProperty("isMajorVersion")
	public Boolean getIsMajorVersion() {
		return isMajorVersion;
	}

	/**
	 * 
	 * @param isMajorVersion
	 * The isMajorVersion
	 */
	@JsonProperty("isMajorVersion")
	public void setIsMajorVersion(Boolean isMajorVersion) {
		this.isMajorVersion = isMajorVersion;
	}

	/**
	 * 
	 * @return
	 * The mimeType
	 */
	@JsonProperty("mimeType")
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * 
	 * @param mimeType
	 * The mimeType
	 */
	@JsonProperty("mimeType")
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * 
	 * @return
	 * The moderationFlags
	 */
	@JsonProperty("moderationFlags")
	public Object getModerationFlags() {
		return moderationFlags;
	}

	/**
	 * 
	 * @param moderationFlags
	 * The moderationFlags
	 */
	@JsonProperty("moderationFlags")
	public void setModerationFlags(Object moderationFlags) {
		this.moderationFlags = moderationFlags;
	}

	/**
	 * 
	 * @return
	 * The modifiedDate
	 */
	@JsonProperty("modifiedDate")
	public String getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * 
	 * @param modifiedDate
	 * The modifiedDate
	 */
	@JsonProperty("modifiedDate")
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * 
	 * @return
	 * The motif
	 */
	@JsonProperty("motif")
	public Motif getMotif() {
		return motif;
	}

	/**
	 * 
	 * @param motif
	 * The motif
	 */
	@JsonProperty("motif")
	public void setMotif(Motif motif) {
		this.motif = motif;
	}

	/**
	 * 
	 * @return
	 * The mySubscription
	 */
	@JsonProperty("mySubscription")
	public Object getMySubscription() {
		return mySubscription;
	}

	/**
	 * 
	 * @param mySubscription
	 * The mySubscription
	 */
	@JsonProperty("mySubscription")
	public void setMySubscription(Object mySubscription) {
		this.mySubscription = mySubscription;
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
	 * The origin
	 */
	@JsonProperty("origin")
	public String getOrigin() {
		return origin;
	}

	/**
	 * 
	 * @param origin
	 * The origin
	 */
	@JsonProperty("origin")
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * 
	 * @return
	 * The owner
	 */
	@JsonProperty("owner")
	public Owner getOwner() {
		return owner;
	}

	/**
	 * 
	 * @param owner
	 * The owner
	 */
	@JsonProperty("owner")
	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	/**
	 * 
	 * @return
	 * The pageCount
	 */
	@JsonProperty("pageCount")
	public Integer getPageCount() {
		return pageCount;
	}

	/**
	 * 
	 * @param pageCount
	 * The pageCount
	 */
	@JsonProperty("pageCount")
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * 
	 * @return
	 * The parentFolder
	 */
	@JsonProperty("parentFolder")
	public Object getParentFolder() {
		return parentFolder;
	}

	/**
	 * 
	 * @param parentFolder
	 * The parentFolder
	 */
	@JsonProperty("parentFolder")
	public void setParentFolder(Object parentFolder) {
		this.parentFolder = parentFolder;
	}

	/**
	 * 
	 * @return
	 * The pdfRenditionStatus
	 */
	@JsonProperty("pdfRenditionStatus")
	public String getPdfRenditionStatus() {
		return pdfRenditionStatus;
	}

	/**
	 * 
	 * @param pdfRenditionStatus
	 * The pdfRenditionStatus
	 */
	@JsonProperty("pdfRenditionStatus")
	public void setPdfRenditionStatus(String pdfRenditionStatus) {
		this.pdfRenditionStatus = pdfRenditionStatus;
	}

	/**
	 * 
	 * @return
	 * The publishStatus
	 */
	@JsonProperty("publishStatus")
	public String getPublishStatus() {
		return publishStatus;
	}

	/**
	 * 
	 * @param publishStatus
	 * The publishStatus
	 */
	@JsonProperty("publishStatus")
	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	/**
	 * 
	 * @return
	 * The renditionUrl
	 */
	@JsonProperty("renditionUrl")
	public Object getRenditionUrl() {
		return renditionUrl;
	}

	/**
	 * 
	 * @param renditionUrl
	 * The renditionUrl
	 */
	@JsonProperty("renditionUrl")
	public void setRenditionUrl(Object renditionUrl) {
		this.renditionUrl = renditionUrl;
	}

	/**
	 * 
	 * @return
	 * The renditionUrl240By180
	 */
	@JsonProperty("renditionUrl240By180")
	public Object getRenditionUrl240By180() {
		return renditionUrl240By180;
	}

	/**
	 * 
	 * @param renditionUrl240By180
	 * The renditionUrl240By180
	 */
	@JsonProperty("renditionUrl240By180")
	public void setRenditionUrl240By180(Object renditionUrl240By180) {
		this.renditionUrl240By180 = renditionUrl240By180;
	}

	/**
	 * 
	 * @return
	 * The renditionUrl720By480
	 */
	@JsonProperty("renditionUrl720By480")
	public Object getRenditionUrl720By480() {
		return renditionUrl720By480;
	}

	/**
	 * 
	 * @param renditionUrl720By480
	 * The renditionUrl720By480
	 */
	@JsonProperty("renditionUrl720By480")
	public void setRenditionUrl720By480(Object renditionUrl720By480) {
		this.renditionUrl720By480 = renditionUrl720By480;
	}

	/**
	 * 
	 * @return
	 * The repositoryFileUrl
	 */
	@JsonProperty("repositoryFileUrl")
	public Object getRepositoryFileUrl() {
		return repositoryFileUrl;
	}

	/**
	 * 
	 * @param repositoryFileUrl
	 * The repositoryFileUrl
	 */
	@JsonProperty("repositoryFileUrl")
	public void setRepositoryFileUrl(Object repositoryFileUrl) {
		this.repositoryFileUrl = repositoryFileUrl;
	}

	/**
	 * 
	 * @return
	 * The sharingRole
	 */
	@JsonProperty("sharingRole")
	public String getSharingRole() {
		return sharingRole;
	}

	/**
	 * 
	 * @param sharingRole
	 * The sharingRole
	 */
	@JsonProperty("sharingRole")
	public void setSharingRole(String sharingRole) {
		this.sharingRole = sharingRole;
	}

	/**
	 * 
	 * @return
	 * The textPreview
	 */
	@JsonProperty("textPreview")
	public Object getTextPreview() {
		return textPreview;
	}

	/**
	 * 
	 * @param textPreview
	 * The textPreview
	 */
	@JsonProperty("textPreview")
	public void setTextPreview(Object textPreview) {
		this.textPreview = textPreview;
	}

	/**
	 * 
	 * @return
	 * The thumb120By90RenditionStatus
	 */
	@JsonProperty("thumb120By90RenditionStatus")
	public String getThumb120By90RenditionStatus() {
		return thumb120By90RenditionStatus;
	}

	/**
	 * 
	 * @param thumb120By90RenditionStatus
	 * The thumb120By90RenditionStatus
	 */
	@JsonProperty("thumb120By90RenditionStatus")
	public void setThumb120By90RenditionStatus(String thumb120By90RenditionStatus) {
		this.thumb120By90RenditionStatus = thumb120By90RenditionStatus;
	}

	/**
	 * 
	 * @return
	 * The thumb240By180RenditionStatus
	 */
	@JsonProperty("thumb240By180RenditionStatus")
	public String getThumb240By180RenditionStatus() {
		return thumb240By180RenditionStatus;
	}

	/**
	 * 
	 * @param thumb240By180RenditionStatus
	 * The thumb240By180RenditionStatus
	 */
	@JsonProperty("thumb240By180RenditionStatus")
	public void setThumb240By180RenditionStatus(String thumb240By180RenditionStatus) {
		this.thumb240By180RenditionStatus = thumb240By180RenditionStatus;
	}

	/**
	 * 
	 * @return
	 * The thumb720By480RenditionStatus
	 */
	@JsonProperty("thumb720By480RenditionStatus")
	public String getThumb720By480RenditionStatus() {
		return thumb720By480RenditionStatus;
	}

	/**
	 * 
	 * @param thumb720By480RenditionStatus
	 * The thumb720By480RenditionStatus
	 */
	@JsonProperty("thumb720By480RenditionStatus")
	public void setThumb720By480RenditionStatus(String thumb720By480RenditionStatus) {
		this.thumb720By480RenditionStatus = thumb720By480RenditionStatus;
	}

	/**
	 * 
	 * @return
	 * The title
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 * The title
	 */
	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
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
	 * The versionNumber
	 */
	@JsonProperty("versionNumber")
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * 
	 * @param versionNumber
	 * The versionNumber
	 */
	@JsonProperty("versionNumber")
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

}
