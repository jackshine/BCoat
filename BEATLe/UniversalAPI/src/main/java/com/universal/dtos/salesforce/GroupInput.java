package com.universal.dtos.salesforce;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class GroupInput {

	@JsonProperty("announcement")
	private String announcement;
	@JsonProperty("canHaveChatterGuests")
	private String canHaveChatterGuests;
	@JsonProperty("description")
	private String description;
	@JsonProperty("information")
	private Information information;
	@JsonProperty("isArchived")
	private String isArchived;
	@JsonProperty("isAutoArchiveDisabled")
	private String isAutoArchiveDisabled;
	@JsonProperty("name")
	private String name;
	@JsonProperty("owner")
	private String owner;
	@JsonProperty("visibility")
	private String visibility;

	/**
	 * 
	 * @return
	 * The announcement
	 */
	@JsonProperty("announcement")
	public String getAnnouncement() {
		return announcement;
	}

	/**
	 * 
	 * @param announcement
	 * The announcement
	 */
	@JsonProperty("announcement")
	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	/**
	 * 
	 * @return
	 * The canHaveChatterGuests
	 */
	@JsonProperty("canHaveChatterGuests")
	public String getCanHaveChatterGuests() {
		return canHaveChatterGuests;
	}

	/**
	 * 
	 * @param canHaveChatterGuests
	 * The canHaveChatterGuests
	 */
	@JsonProperty("canHaveChatterGuests")
	public void setCanHaveChatterGuests(String canHaveChatterGuests) {
		this.canHaveChatterGuests = canHaveChatterGuests;
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
	public String getIsArchived() {
		return isArchived;
	}

	/**
	 * 
	 * @param isArchived
	 * The isArchived
	 */
	@JsonProperty("isArchived")
	public void setIsArchived(String isArchived) {
		this.isArchived = isArchived;
	}

	/**
	 * 
	 * @return
	 * The isAutoArchiveDisabled
	 */
	@JsonProperty("isAutoArchiveDisabled")
	public String getIsAutoArchiveDisabled() {
		return isAutoArchiveDisabled;
	}

	/**
	 * 
	 * @param isAutoArchiveDisabled
	 * The isAutoArchiveDisabled
	 */
	@JsonProperty("isAutoArchiveDisabled")
	public void setIsAutoArchiveDisabled(String isAutoArchiveDisabled) {
		this.isAutoArchiveDisabled = isAutoArchiveDisabled;
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
	public String getOwner() {
		return owner;
	}

	/**
	 * 
	 * @param owner
	 * The owner
	 */
	@JsonProperty("owner")
	public void setOwner(String owner) {
		this.owner = owner;
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