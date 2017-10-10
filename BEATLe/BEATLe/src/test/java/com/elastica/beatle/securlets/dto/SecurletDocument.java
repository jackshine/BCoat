package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class SecurletDocument {

	@JsonProperty("meta")
	private Meta meta;
	@JsonProperty("objects")
	private List<com.elastica.beatle.securlets.dto.Object> objects = new ArrayList<com.elastica.beatle.securlets.dto.Object>();
	
	@JsonProperty("docs")
	private List<com.elastica.beatle.securlets.dto.Object> docs = new ArrayList<com.elastica.beatle.securlets.dto.Object>();
	
	@JsonProperty("users")
	private List<com.elastica.beatle.securlets.dto.Object> users = new ArrayList<com.elastica.beatle.securlets.dto.Object>();
	
	@JsonProperty("collabs")
	private List<com.elastica.beatle.securlets.dto.Object> collabs = new ArrayList<com.elastica.beatle.securlets.dto.Object>();
	
	
	/**
	 * @return the users
	 */
	public List<com.elastica.beatle.securlets.dto.Object> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<com.elastica.beatle.securlets.dto.Object> users) {
		this.users = users;
	}

	/**
	 * @return the collabs
	 */
	public List<com.elastica.beatle.securlets.dto.Object> getCollabs() {
		return collabs;
	}

	/**
	 * @param collabs the collabs to set
	 */
	public void setCollabs(List<com.elastica.beatle.securlets.dto.Object> collabs) {
		this.collabs = collabs;
	}
	
	/**
	 *
	 * @return
	 * The meta
	 */
	@JsonProperty("meta")
	public Meta getMeta() {
		return meta;
	}

	/**
	 *
	 * @param meta
	 * The meta
	 */
	@JsonProperty("meta")
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	/**
	 *
	 * @return
	 * The objects
	 */
	@JsonProperty("objects")
	public List<com.elastica.beatle.securlets.dto.Object> getObjects() {
		return objects;
	}

	/**
	 *
	 * @param objects
	 * The objects
	 */
	@JsonProperty("objects")
	public void setObjects(List<com.elastica.beatle.securlets.dto.Object> objects) {
		this.objects = objects;
	}

	/**
	 *
	 * @return
	 * The objects
	 */
	@JsonProperty("docs")
	public List<com.elastica.beatle.securlets.dto.Object> getDocs() {
		return docs;
	}

	/**
	 *
	 * @param objects
	 * The objects
	 */
	@JsonProperty("docs")
	public void setDocs(List<com.elastica.beatle.securlets.dto.Object> objects) {
		this.docs = objects;
	}
	

}

