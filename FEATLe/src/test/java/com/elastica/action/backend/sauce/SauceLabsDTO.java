package com.elastica.action.backend.sauce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class SauceLabsDTO {

	@JsonProperty("browser_short_version")
	private String browserShortVersion;
	@JsonProperty("video_url")
	private String videoUrl;
	@JsonProperty("creation_time")
	private Integer creationTime;
	@JsonProperty("custom-data")
	private Object customData;
	@JsonProperty("browser_version")
	private String browserVersion;
	@JsonProperty("owner")
	private String owner;
	@JsonProperty("id")
	private String id;
	@JsonProperty("record_screenshots")
	private Boolean recordScreenshots;
	@JsonProperty("record_video")
	private Boolean recordVideo;
	@JsonProperty("build")
	private Object build;
	@JsonProperty("passed")
	private Boolean passed;
	@JsonProperty("public")
	private String _public;
	@JsonProperty("end_time")
	private Integer endTime;
	@JsonProperty("status")
	private String status;
	@JsonProperty("log_url")
	private String logUrl;
	@JsonProperty("start_time")
	private Integer startTime;
	@JsonProperty("proxied")
	private Boolean proxied;
	@JsonProperty("modification_time")
	private Integer modificationTime;
	@JsonProperty("tags")
	private List<Object> tags = new ArrayList<Object>();
	@JsonProperty("name")
	private String name;
	@JsonProperty("commands_not_successful")
	private Integer commandsNotSuccessful;
	@JsonProperty("consolidated_status")
	private String consolidatedStatus;
	@JsonProperty("assigned_tunnel_id")
	private Object assignedTunnelId;
	@JsonProperty("error")
	private Object error;
	@JsonProperty("os")
	private String os;
	@JsonProperty("breakpointed")
	private Object breakpointed;
	@JsonProperty("browser")
	private String browser;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The browserShortVersion
	 */
	@JsonProperty("browser_short_version")
	public String getBrowserShortVersion() {
		return browserShortVersion;
	}

	/**
	 * 
	 * @param browserShortVersion
	 * The browser_short_version
	 */
	@JsonProperty("browser_short_version")
	public void setBrowserShortVersion(String browserShortVersion) {
		this.browserShortVersion = browserShortVersion;
	}

	/**
	 * 
	 * @return
	 * The videoUrl
	 */
	@JsonProperty("video_url")
	public String getVideoUrl() {
		return videoUrl;
	}

	/**
	 * 
	 * @param videoUrl
	 * The video_url
	 */
	@JsonProperty("video_url")
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	/**
	 * 
	 * @return
	 * The creationTime
	 */
	@JsonProperty("creation_time")
	public Integer getCreationTime() {
		return creationTime;
	}

	/**
	 * 
	 * @param creationTime
	 * The creation_time
	 */
	@JsonProperty("creation_time")
	public void setCreationTime(Integer creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * 
	 * @return
	 * The customData
	 */
	@JsonProperty("custom-data")
	public Object getCustomData() {
		return customData;
	}

	/**
	 * 
	 * @param customData
	 * The custom-data
	 */
	@JsonProperty("custom-data")
	public void setCustomData(Object customData) {
		this.customData = customData;
	}

	/**
	 * 
	 * @return
	 * The browserVersion
	 */
	@JsonProperty("browser_version")
	public String getBrowserVersion() {
		return browserVersion;
	}

	/**
	 * 
	 * @param browserVersion
	 * The browser_version
	 */
	@JsonProperty("browser_version")
	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
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
	 * The recordScreenshots
	 */
	@JsonProperty("record_screenshots")
	public Boolean getRecordScreenshots() {
		return recordScreenshots;
	}

	/**
	 * 
	 * @param recordScreenshots
	 * The record_screenshots
	 */
	@JsonProperty("record_screenshots")
	public void setRecordScreenshots(Boolean recordScreenshots) {
		this.recordScreenshots = recordScreenshots;
	}

	/**
	 * 
	 * @return
	 * The recordVideo
	 */
	@JsonProperty("record_video")
	public Boolean getRecordVideo() {
		return recordVideo;
	}

	/**
	 * 
	 * @param recordVideo
	 * The record_video
	 */
	@JsonProperty("record_video")
	public void setRecordVideo(Boolean recordVideo) {
		this.recordVideo = recordVideo;
	}

	/**
	 * 
	 * @return
	 * The build
	 */
	@JsonProperty("build")
	public Object getBuild() {
		return build;
	}

	/**
	 * 
	 * @param build
	 * The build
	 */
	@JsonProperty("build")
	public void setBuild(Object build) {
		this.build = build;
	}

	/**
	 * 
	 * @return
	 * The passed
	 */
	@JsonProperty("passed")
	public Boolean getPassed() {
		return passed;
	}

	/**
	 * 
	 * @param passed
	 * The passed
	 */
	@JsonProperty("passed")
	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	/**
	 * 
	 * @return
	 * The _public
	 */
	@JsonProperty("public")
	public String getPublic() {
		return _public;
	}

	/**
	 * 
	 * @param _public
	 * The public
	 */
	@JsonProperty("public")
	public void setPublic(String _public) {
		this._public = _public;
	}

	/**
	 * 
	 * @return
	 * The endTime
	 */
	@JsonProperty("end_time")
	public Integer getEndTime() {
		return endTime;
	}

	/**
	 * 
	 * @param endTime
	 * The end_time
	 */
	@JsonProperty("end_time")
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	/**
	 * 
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return
	 * The logUrl
	 */
	@JsonProperty("log_url")
	public String getLogUrl() {
		return logUrl;
	}

	/**
	 * 
	 * @param logUrl
	 * The log_url
	 */
	@JsonProperty("log_url")
	public void setLogUrl(String logUrl) {
		this.logUrl = logUrl;
	}

	/**
	 * 
	 * @return
	 * The startTime
	 */
	@JsonProperty("start_time")
	public Integer getStartTime() {
		return startTime;
	}

	/**
	 * 
	 * @param startTime
	 * The start_time
	 */
	@JsonProperty("start_time")
	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	/**
	 * 
	 * @return
	 * The proxied
	 */
	@JsonProperty("proxied")
	public Boolean getProxied() {
		return proxied;
	}

	/**
	 * 
	 * @param proxied
	 * The proxied
	 */
	@JsonProperty("proxied")
	public void setProxied(Boolean proxied) {
		this.proxied = proxied;
	}

	/**
	 * 
	 * @return
	 * The modificationTime
	 */
	@JsonProperty("modification_time")
	public Integer getModificationTime() {
		return modificationTime;
	}

	/**
	 * 
	 * @param modificationTime
	 * The modification_time
	 */
	@JsonProperty("modification_time")
	public void setModificationTime(Integer modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * 
	 * @return
	 * The tags
	 */
	@JsonProperty("tags")
	public List<Object> getTags() {
		return tags;
	}

	/**
	 * 
	 * @param tags
	 * The tags
	 */
	@JsonProperty("tags")
	public void setTags(List<Object> tags) {
		this.tags = tags;
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
	 * The commandsNotSuccessful
	 */
	@JsonProperty("commands_not_successful")
	public Integer getCommandsNotSuccessful() {
		return commandsNotSuccessful;
	}

	/**
	 * 
	 * @param commandsNotSuccessful
	 * The commands_not_successful
	 */
	@JsonProperty("commands_not_successful")
	public void setCommandsNotSuccessful(Integer commandsNotSuccessful) {
		this.commandsNotSuccessful = commandsNotSuccessful;
	}

	/**
	 * 
	 * @return
	 * The consolidatedStatus
	 */
	@JsonProperty("consolidated_status")
	public String getConsolidatedStatus() {
		return consolidatedStatus;
	}

	/**
	 * 
	 * @param consolidatedStatus
	 * The consolidated_status
	 */
	@JsonProperty("consolidated_status")
	public void setConsolidatedStatus(String consolidatedStatus) {
		this.consolidatedStatus = consolidatedStatus;
	}

	/**
	 * 
	 * @return
	 * The assignedTunnelId
	 */
	@JsonProperty("assigned_tunnel_id")
	public Object getAssignedTunnelId() {
		return assignedTunnelId;
	}

	/**
	 * 
	 * @param assignedTunnelId
	 * The assigned_tunnel_id
	 */
	@JsonProperty("assigned_tunnel_id")
	public void setAssignedTunnelId(Object assignedTunnelId) {
		this.assignedTunnelId = assignedTunnelId;
	}

	/**
	 * 
	 * @return
	 * The error
	 */
	@JsonProperty("error")
	public Object getError() {
		return error;
	}

	/**
	 * 
	 * @param error
	 * The error
	 */
	@JsonProperty("error")
	public void setError(Object error) {
		this.error = error;
	}

	/**
	 * 
	 * @return
	 * The os
	 */
	@JsonProperty("os")
	public String getOs() {
		return os;
	}

	/**
	 * 
	 * @param os
	 * The os
	 */
	@JsonProperty("os")
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * 
	 * @return
	 * The breakpointed
	 */
	@JsonProperty("breakpointed")
	public Object getBreakpointed() {
		return breakpointed;
	}

	/**
	 * 
	 * @param breakpointed
	 * The breakpointed
	 */
	@JsonProperty("breakpointed")
	public void setBreakpointed(Object breakpointed) {
		this.breakpointed = breakpointed;
	}

	/**
	 * 
	 * @return
	 * The browser
	 */
	@JsonProperty("browser")
	public String getBrowser() {
		return browser;
	}

	/**
	 * 
	 * @param browser
	 * The browser
	 */
	@JsonProperty("browser")
	public void setBrowser(String browser) {
		this.browser = browser;
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