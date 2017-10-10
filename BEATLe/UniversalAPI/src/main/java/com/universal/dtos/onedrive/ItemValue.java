package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class ItemValue {
	@JsonProperty("odata.type")
	private String odataType;
	@JsonProperty("odata.id")
	private String odataId;
	@JsonProperty("odata.editLink")
	private String odataEditLink;
	@JsonProperty("DeletedDate")
	private String DeletedDate;
	@JsonProperty("DirName")
	private String DirName;
	@JsonProperty("Id")
	private String Id;
	@JsonProperty("ItemState")
	private long ItemState;
	@JsonProperty("ItemType")
	private long ItemType;
	@JsonProperty("LeafName")
	private String LeafName;
	@JsonProperty("Size")
	private String Size;
	@JsonProperty("Title")
	private String Title;
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
	 * The DeletedDate
	 */
	@JsonProperty("DeletedDate")
	public String getDeletedDate() {
		return DeletedDate;
	}

	/**
	 * 
	 * @param DeletedDate
	 * The DeletedDate
	 */
	@JsonProperty("DeletedDate")
	public void setDeletedDate(String DeletedDate) {
		this.DeletedDate = DeletedDate;
	}

	/**
	 * 
	 * @return
	 * The DirName
	 */
	@JsonProperty("DirName")
	public String getDirName() {
		return DirName;
	}

	/**
	 * 
	 * @param DirName
	 * The DirName
	 */
	@JsonProperty("DirName")
	public void setDirName(String DirName) {
		this.DirName = DirName;
	}

	/**
	 * 
	 * @return
	 * The Id
	 */
	@JsonProperty("Id")
	public String getId() {
		return Id;
	}

	/**
	 * 
	 * @param Id
	 * The Id
	 */
	@JsonProperty("Id")
	public void setId(String Id) {
		this.Id = Id;
	}

	/**
	 * 
	 * @return
	 * The ItemState
	 */
	@JsonProperty("ItemState")
	public long getItemState() {
		return ItemState;
	}

	/**
	 * 
	 * @param ItemState
	 * The ItemState
	 */
	@JsonProperty("ItemState")
	public void setItemState(long ItemState) {
		this.ItemState = ItemState;
	}

	/**
	 * 
	 * @return
	 * The ItemType
	 */
	@JsonProperty("ItemType")
	public long getItemType() {
		return ItemType;
	}

	/**
	 * 
	 * @param ItemType
	 * The ItemType
	 */
	@JsonProperty("ItemType")
	public void setItemType(long ItemType) {
		this.ItemType = ItemType;
	}

	/**
	 * 
	 * @return
	 * The LeafName
	 */
	@JsonProperty("LeafName")
	public String getLeafName() {
		return LeafName;
	}

	/**
	 * 
	 * @param LeafName
	 * The LeafName
	 */
	@JsonProperty("LeafName")
	public void setLeafName(String LeafName) {
		this.LeafName = LeafName;
	}

	/**
	 * 
	 * @return
	 * The Size
	 */
	@JsonProperty("Size")
	public String getSize() {
		return Size;
	}

	/**
	 * 
	 * @param Size
	 * The Size
	 */
	@JsonProperty("Size")
	public void setSize(String Size) {
		this.Size = Size;
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
