package com.universal.dtos.salesforce;
import org.codehaus.jackson.annotate.JsonProperty;

public class GroupDetail {

	@JsonProperty("additionalLabel")
	private Object additionalLabel;
	@JsonProperty("announcement")
	private Object announcement;
	@JsonProperty("canHaveChatterGuests")
	private Boolean canHaveChatterGuests;
	@JsonProperty("community")
	private Object community;
	@JsonProperty("description")
	private Object description;
	@JsonProperty("emailToChatterAddress")
	private String emailToChatterAddress;
	@JsonProperty("fileCount")
	private Integer fileCount;
	@JsonProperty("id")
	private String id;
	@JsonProperty("information")
	private Information information;
	@JsonProperty("isArchived")
	private Boolean isArchived;
	@JsonProperty("isAutoArchiveDisabled")
	private Boolean isAutoArchiveDisabled;
	@JsonProperty("lastFeedElementPostDate")
	private String lastFeedElementPostDate;
	@JsonProperty("memberCount")
	private Integer memberCount;
	@JsonProperty("motif")
	private Motif motif;
	@JsonProperty("myRole")
	private String myRole;
	@JsonProperty("mySubscription")
	private MySubscription mySubscription;
	@JsonProperty("name")
	private String name;
	@JsonProperty("owner")
	private Owner owner;
	@JsonProperty("pendingRequests")
	private Object pendingRequests;
	@JsonProperty("photo")
	private Photo photo;
	@JsonProperty("type")
	private String type;
	@JsonProperty("url")
	private String url;
	@JsonProperty("visibility")
	private String visibility;

	/**
	 * 
	 * @return
	 * The additionalLabel
	 */
	@JsonProperty("additionalLabel")
	public Object getAdditionalLabel() {
		return additionalLabel;
	}

	/**
	 * 
	 * @param additionalLabel
	 * The additionalLabel
	 */
	@JsonProperty("additionalLabel")
	public void setAdditionalLabel(Object additionalLabel) {
		this.additionalLabel = additionalLabel;
	}

	/**
	 * 
	 * @return
	 * The announcement
	 */
	@JsonProperty("announcement")
	public Object getAnnouncement() {
		return announcement;
	}

	/**
	 * 
	 * @param announcement
	 * The announcement
	 */
	@JsonProperty("announcement")
	public void setAnnouncement(Object announcement) {
		this.announcement = announcement;
	}

	/**
	 * 
	 * @return
	 * The canHaveChatterGuests
	 */
	@JsonProperty("canHaveChatterGuests")
	public Boolean getCanHaveChatterGuests() {
		return canHaveChatterGuests;
	}

	/**
	 * 
	 * @param canHaveChatterGuests
	 * The canHaveChatterGuests
	 */
	@JsonProperty("canHaveChatterGuests")
	public void setCanHaveChatterGuests(Boolean canHaveChatterGuests) {
		this.canHaveChatterGuests = canHaveChatterGuests;
	}

	/**
	 * 
	 * @return
	 * The community
	 */
	@JsonProperty("community")
	public Object getCommunity() {
		return community;
	}

	/**
	 * 
	 * @param community
	 * The community
	 */
	@JsonProperty("community")
	public void setCommunity(Object community) {
		this.community = community;
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
	 * The emailToChatterAddress
	 */
	@JsonProperty("emailToChatterAddress")
	public String getEmailToChatterAddress() {
		return emailToChatterAddress;
	}

	/**
	 * 
	 * @param emailToChatterAddress
	 * The emailToChatterAddress
	 */
	@JsonProperty("emailToChatterAddress")
	public void setEmailToChatterAddress(String emailToChatterAddress) {
		this.emailToChatterAddress = emailToChatterAddress;
	}

	/**
	 * 
	 * @return
	 * The fileCount
	 */
	@JsonProperty("fileCount")
	public Integer getFileCount() {
		return fileCount;
	}

	/**
	 * 
	 * @param fileCount
	 * The fileCount
	 */
	@JsonProperty("fileCount")
	public void setFileCount(Integer fileCount) {
		this.fileCount = fileCount;
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
	 * The information
	 */
	@JsonProperty("information")
	public Information getInformation() {
		return information;
	}

	/**
	 * 
	 * @param information
	 * The information
	 */
	@JsonProperty("information")
	public void setInformation(Information information) {
		this.information = information;
	}

	/**
	 * 
	 * @return
	 * The isArchived
	 */
	@JsonProperty("isArchived")
	public Boolean getIsArchived() {
		return isArchived;
	}

	/**
	 * 
	 * @param isArchived
	 * The isArchived
	 */
	@JsonProperty("isArchived")
	public void setIsArchived(Boolean isArchived) {
		this.isArchived = isArchived;
	}

	/**
	 * 
	 * @return
	 * The isAutoArchiveDisabled
	 */
	@JsonProperty("isAutoArchiveDisabled")
	public Boolean getIsAutoArchiveDisabled() {
		return isAutoArchiveDisabled;
	}

	/**
	 * 
	 * @param isAutoArchiveDisabled
	 * The isAutoArchiveDisabled
	 */
	@JsonProperty("isAutoArchiveDisabled")
	public void setIsAutoArchiveDisabled(Boolean isAutoArchiveDisabled) {
		this.isAutoArchiveDisabled = isAutoArchiveDisabled;
	}

	/**
	 * 
	 * @return
	 * The lastFeedElementPostDate
	 */
	@JsonProperty("lastFeedElementPostDate")
	public String getLastFeedElementPostDate() {
		return lastFeedElementPostDate;
	}

	/**
	 * 
	 * @param lastFeedElementPostDate
	 * The lastFeedElementPostDate
	 */
	@JsonProperty("lastFeedElementPostDate")
	public void setLastFeedElementPostDate(String lastFeedElementPostDate) {
		this.lastFeedElementPostDate = lastFeedElementPostDate;
	}

	/**
	 * 
	 * @return
	 * The memberCount
	 */
	@JsonProperty("memberCount")
	public Integer getMemberCount() {
		return memberCount;
	}

	/**
	 * 
	 * @param memberCount
	 * The memberCount
	 */
	@JsonProperty("memberCount")
	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
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
	 * The myRole
	 */
	@JsonProperty("myRole")
	public String getMyRole() {
		return myRole;
	}

	/**
	 * 
	 * @param myRole
	 * The myRole
	 */
	@JsonProperty("myRole")
	public void setMyRole(String myRole) {
		this.myRole = myRole;
	}

	/**
	 * 
	 * @return
	 * The mySubscription
	 */
	@JsonProperty("mySubscription")
	public MySubscription getMySubscription() {
		return mySubscription;
	}

	/**
	 * 
	 * @param mySubscription
	 * The mySubscription
	 */
	@JsonProperty("mySubscription")
	public void setMySubscription(MySubscription mySubscription) {
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
	 * The pendingRequests
	 */
	@JsonProperty("pendingRequests")
	public Object getPendingRequests() {
		return pendingRequests;
	}

	/**
	 * 
	 * @param pendingRequests
	 * The pendingRequests
	 */
	@JsonProperty("pendingRequests")
	public void setPendingRequests(Object pendingRequests) {
		this.pendingRequests = pendingRequests;
	}

	/**
	 * 
	 * @return
	 * The photo
	 */
	@JsonProperty("photo")
	public Photo getPhoto() {
		return photo;
	}

	/**
	 * 
	 * @param photo
	 * The photo
	 */
	@JsonProperty("photo")
	public void setPhoto(Photo photo) {
		this.photo = photo;
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
	 * The visibility
	 */
	@JsonProperty("visibility")
	public String getVisibility() {
		return visibility;
	}

	/**
	 * 
	 * @param visibility
	 * The visibility
	 */
	@JsonProperty("visibility")
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

}

