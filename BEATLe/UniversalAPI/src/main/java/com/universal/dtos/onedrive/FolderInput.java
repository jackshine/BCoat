package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
 
public class FolderInput {

	@JsonProperty("name")
	private String name;
	@JsonProperty("folder")
	private Folder folder;
	@JsonProperty("@name.conflictBehavior")
	private String NameConflictBehavior;
	

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
	 * The folder
	 */
	@JsonProperty("folder")
	public Folder getFolder() {
		return folder;
	}

	/**
	 * 
	 * @param folder
	 * The folder
	 */
	@JsonProperty("folder")
	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	/**
	 * 
	 * @return
	 * The NameConflictBehavior
	 */
	@JsonProperty("@name.conflictBehavior")
	public String getNameConflictBehavior() {
		return NameConflictBehavior;
	}

	/**
	 * 
	 * @param NameConflictBehavior
	 * The @name.conflictBehavior
	 */
	@JsonProperty("@name.conflictBehavior")
	public void setNameConflictBehavior(String NameConflictBehavior) {
		this.NameConflictBehavior = NameConflictBehavior;
	}

}