package com.universal.dtos.onedrive;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
	private String id;
	private String name;
	private String ctag;
	private String etag;
	private IdentitySet createdBy;
	private String createdDateTime;
	private IdentitySet lastModifiedBy;
	private String lastModifiedDateTime;
	private long size;	
	private ItemReference parentReference;
	
	private String webUrl;
	private FileFacet file;
	private FolderFacet folder;
	private ImageFacet image;
	private PhotoFacet photo;
	private AudioFacet audio;
	private VideoFacet video;
	private LocationFacet location;
	private DeletedFacet deleted;
	
	//The @content.downloadUrl is a short-lived URL and can't be cached. The URL will only be available for a short period of time before it is invalidated
	//private String @name.conflictBehavior;
	//private String @content.downloadUrl;
	//private String @content.sourceUrl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCtag() {
		return ctag;
	}
	public void setCtag(String ctag) {
		this.ctag = ctag;
	}
	public String getEtag() {
		return etag;
	}
	public void setEtag(String etag) {
		this.etag = etag;
	}
	public IdentitySet getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(IdentitySet createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public IdentitySet getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(IdentitySet lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLastModifiedDateTime() {
		return lastModifiedDateTime;
	}
	public void setLastModifiedDateTime(String lastModifiedDateTime) {
		this.lastModifiedDateTime = lastModifiedDateTime;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public ItemReference getParentReference() {
		return parentReference;
	}
	public void setParentReference(ItemReference parentReference) {
		this.parentReference = parentReference;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public FileFacet getFile() {
		return file;
	}
	public void setFile(FileFacet file) {
		this.file = file;
	}
	public FolderFacet getFolder() {
		return folder;
	}
	public void setFolder(FolderFacet folder) {
		this.folder = folder;
	}
	public ImageFacet getImage() {
		return image;
	}
	public void setImage(ImageFacet image) {
		this.image = image;
	}
	public PhotoFacet getPhoto() {
		return photo;
	}
	public void setPhoto(PhotoFacet photo) {
		this.photo = photo;
	}
	public AudioFacet getAudio() {
		return audio;
	}
	public void setAudio(AudioFacet audio) {
		this.audio = audio;
	}
	public VideoFacet getVideo() {
		return video;
	}
	public void setVideo(VideoFacet video) {
		this.video = video;
	}
	public LocationFacet getLocation() {
		return location;
	}
	public void setLocation(LocationFacet location) {
		this.location = location;
	}
	public DeletedFacet getDeleted() {
		return deleted;
	}
	public void setDeleted(DeletedFacet deleted) {
		this.deleted = deleted;
	}	
}
