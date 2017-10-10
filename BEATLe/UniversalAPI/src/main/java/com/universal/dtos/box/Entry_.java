package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Generated("org.jsonschema2pojo")
public class Entry_ {

	

		@JsonProperty("type")
		private String type;
		@JsonProperty("id")
		private String id;
		@JsonProperty("sequence_id")
		private String sequenceId;
		@JsonProperty("etag")
		private String etag;
		@JsonProperty("name")
		private String name;
		@JsonProperty("file_version")
		private FileVersion fileVersion;
		@JsonProperty("sha1")
		private String sha1;
		@JsonIgnore
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
		 * The sequenceId
		 */
		@JsonProperty("sequence_id")
		public String getSequenceId() {
			return sequenceId;
		}

		/**
		 * 
		 * @param sequenceId
		 * The sequence_id
		 */
		@JsonProperty("sequence_id")
		public void setSequenceId(String sequenceId) {
			this.sequenceId = sequenceId;
		}

		/**
		 * 
		 * @return
		 * The etag
		 */
		@JsonProperty("etag")
		public String getEtag() {
			return etag;
		}

		/**
		 * 
		 * @param etag
		 * The etag
		 */
		@JsonProperty("etag")
		public void setEtag(String etag) {
			this.etag = etag;
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
		 * The fileVersion
		 */
		@JsonProperty("file_version")
		public FileVersion getFileVersion() {
			return fileVersion;
		}

		/**
		 * 
		 * @param fileVersion
		 * The file_version
		 */
		@JsonProperty("file_version")
		public void setFileVersion(FileVersion fileVersion) {
			this.fileVersion = fileVersion;
		}

		/**
		 * 
		 * @return
		 * The sha1
		 */
		@JsonProperty("sha1")
		public String getSha1() {
			return sha1;
		}

		/**
		 * 
		 * @param sha1
		 * The sha1
		 */
		@JsonProperty("sha1")
		public void setSha1(String sha1) {
			this.sha1 = sha1;
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
