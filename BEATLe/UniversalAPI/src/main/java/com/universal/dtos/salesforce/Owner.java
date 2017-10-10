package com.universal.dtos.salesforce;

import org.codehaus.jackson.annotate.JsonProperty;

public class Owner {

	@JsonProperty("additionalLabel")
	private Object additionalLabel;
	@JsonProperty("communityNickname")
	private String communityNickname;
	@JsonProperty("companyName")
	private String companyName;
	@JsonProperty("displayName")
	private String displayName;
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("id")
	private String id;
	@JsonProperty("isActive")
	private Boolean isActive;
	@JsonProperty("isInThisCommunity")
	private Boolean isInThisCommunity;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("motif")
	private Motif motif;
	@JsonProperty("mySubscription")
	private Object mySubscription;
	@JsonProperty("name")
	private String name;
	@JsonProperty("photo")
	private Photo photo;
	@JsonProperty("reputation")
	private Object reputation;
	@JsonProperty("title")
	private String title;
	@JsonProperty("type")
	private String type;
	@JsonProperty("url")
	private String url;
	@JsonProperty("userType")
	private String userType;

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
	 * The communityNickname
	 */
	@JsonProperty("communityNickname")
	public String getCommunityNickname() {
		return communityNickname;
	}

	/**
	 * 
	 * @param communityNickname
	 * The communityNickname
	 */
	@JsonProperty("communityNickname")
	public void setCommunityNickname(String communityNickname) {
		this.communityNickname = communityNickname;
	}

	/**
	 * 
	 * @return
	 * The companyName
	 */
	@JsonProperty("companyName")
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * 
	 * @param companyName
	 * The companyName
	 */
	@JsonProperty("companyName")
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * 
	 * @return
	 * The displayName
	 */
	@JsonProperty("displayName")
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * @param displayName
	 * The displayName
	 */
	@JsonProperty("displayName")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 
	 * @return
	 * The firstName
	 */
	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * @param firstName
	 * The firstName
	 */
	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	 * The isActive
	 */
	@JsonProperty("isActive")
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * 
	 * @param isActive
	 * The isActive
	 */
	@JsonProperty("isActive")
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * 
	 * @return
	 * The isInThisCommunity
	 */
	@JsonProperty("isInThisCommunity")
	public Boolean getIsInThisCommunity() {
		return isInThisCommunity;
	}

	/**
	 * 
	 * @param isInThisCommunity
	 * The isInThisCommunity
	 */
	@JsonProperty("isInThisCommunity")
	public void setIsInThisCommunity(Boolean isInThisCommunity) {
		this.isInThisCommunity = isInThisCommunity;
	}

	/**
	 * 
	 * @return
	 * The lastName
	 */
	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	/**
	 * 
	 * @param lastName
	 * The lastName
	 */
	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * The reputation
	 */
	@JsonProperty("reputation")
	public Object getReputation() {
		return reputation;
	}

	/**
	 * 
	 * @param reputation
	 * The reputation
	 */
	@JsonProperty("reputation")
	public void setReputation(Object reputation) {
		this.reputation = reputation;
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
	 * The userType
	 */
	@JsonProperty("userType")
	public String getUserType() {
		return userType;
	}

	/**
	 * 
	 * @param userType
	 * The userType
	 */
	@JsonProperty("userType")
	public void setUserType(String userType) {
		this.userType = userType;
	}

}
