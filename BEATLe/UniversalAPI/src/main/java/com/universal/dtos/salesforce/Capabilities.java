package com.universal.dtos.salesforce;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import java.util.HashMap;
import java.util.Map;

public class Capabilities {

	@JsonProperty("bookmarks")
	private Bookmarks bookmarks;
	@JsonProperty("content")
	private Content content;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The bookmarks
	 */
	@JsonProperty("bookmarks")
	public Bookmarks getBookmarks() {
		return bookmarks;
	}

	/**
	 * 
	 * @param bookmarks
	 * The bookmarks
	 */
	@JsonProperty("bookmarks")
	public void setBookmarks(Bookmarks bookmarks) {
		this.bookmarks = bookmarks;
	}

	/**
	 * 
	 * @return
	 * The content
	 */
	@JsonProperty("content")
	public Content getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 * The content
	 */
	@JsonProperty("content")
	public void setContent(Content content) {
		this.content = content;
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
