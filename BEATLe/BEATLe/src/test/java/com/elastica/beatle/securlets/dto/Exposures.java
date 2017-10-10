package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;


public class Exposures {

	@JsonProperty("all_internal")
	private Boolean allInternal;
	@JsonProperty("ext_count")
	private Integer extCount;
	@JsonProperty("external")
	private List<java.lang.Object> external = new ArrayList<java.lang.Object>();
	@JsonProperty("id")
	private String id;
	@JsonProperty("int_count")
	private Integer intCount;
	@JsonProperty("internal")
	private List<String> internal = new ArrayList<String>();
	@JsonProperty("public")
	private Boolean _public;
	

	/**
	 *
	 * @return
	 * The allInternal
	 */
	@JsonProperty("all_internal")
	public Boolean getAllInternal() {
		return allInternal;
	}

	/**
	 *
	 * @param allInternal
	 * The all_internal
	 */
	@JsonProperty("all_internal")
	public void setAllInternal(Boolean allInternal) {
		this.allInternal = allInternal;
	}

	/**
	 *
	 * @return
	 * The extCount
	 */
	@JsonProperty("ext_count")
	public Integer getExtCount() {
		return extCount;
	}

	/**
	 *
	 * @param extCount
	 * The ext_count
	 */
	@JsonProperty("ext_count")
	public void setExtCount(Integer extCount) {
		this.extCount = extCount;
	}

	/**
	 *
	 * @return
	 * The external
	 */
	@JsonProperty("external")
	public List<java.lang.Object> getExternal() {
		return external;
	}

	/**
	 *
	 * @param external
	 * The external
	 */
	@JsonProperty("external")
	public void setExternal(List<java.lang.Object> external) {
		this.external = external;
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
	 * The intCount
	 */
	@JsonProperty("int_count")
	public Integer getIntCount() {
		return intCount;
	}

	/**
	 *
	 * @param intCount
	 * The int_count
	 */
	@JsonProperty("int_count")
	public void setIntCount(Integer intCount) {
		this.intCount = intCount;
	}

	/**
	 *
	 * @return
	 * The internal
	 */
	@JsonProperty("internal")
	public List<String> getInternal() {
		return internal;
	}

	/**
	 *
	 * @param internal
	 * The internal
	 */
	@JsonProperty("internal")
	public void setInternal(List<String> internal) {
		this.internal = internal;
	}

	/**
	 *
	 * @return
	 * The _public
	 */
	@JsonProperty("public")
	public Boolean getPublic() {
		return _public;
	}

	/**
	 *
	 * @param _public
	 * The public
	 */
	@JsonProperty("public")
	public void setPublic(Boolean _public) {
		this._public = _public;
	}

}
