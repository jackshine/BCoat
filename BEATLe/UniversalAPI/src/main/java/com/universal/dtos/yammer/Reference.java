
package com.universal.dtos.yammer;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Reference {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("network_id")
    @Expose
    private Long networkId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("web_url")
    @Expose
    private String webUrl;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("job_title")
    @Expose
    private String jobTitle;
    @SerializedName("mugshot_url")
    @Expose
    private String mugshotUrl;
    @SerializedName("mugshot_url_template")
    @Expose
    private String mugshotUrlTemplate;
    @SerializedName("activated_at")
    @Expose
    private String activatedAt;
    @SerializedName("auto_activated")
    @Expose
    private Boolean autoActivated;
    @SerializedName("stats")
    @Expose
    private Stats stats;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("sender_id")
    @Expose
    private Long senderId;
    @SerializedName("replied_to_id")
    @Expose
    private Object repliedToId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("sender_type")
    @Expose
    private String senderType;
    @SerializedName("thread_id")
    @Expose
    private Long threadId;
    @SerializedName("message_type")
    @Expose
    private String messageType;
    @SerializedName("system_message")
    @Expose
    private Boolean systemMessage;
    @SerializedName("content_excerpt")
    @Expose
    private String contentExcerpt;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("direct_message")
    @Expose
    private Boolean directMessage;
    @SerializedName("body")
    @Expose
    private Body_ body;
    @SerializedName("thread_starter_id")
    @Expose
    private Long threadStarterId;
    @SerializedName("group_id")
    @Expose
    private Object groupId;
    @SerializedName("topics")
    @Expose
    private List<Object> topics = new ArrayList<Object>();
    @SerializedName("has_attachments")
    @Expose
    private Boolean hasAttachments;
    @SerializedName("invited_user_ids")
    @Expose
    private List<Object> invitedUserIds = new ArrayList<Object>();
    @SerializedName("read_only")
    @Expose
    private Boolean readOnly;
    @SerializedName("normalized_name")
    @Expose
    private String normalizedName;
    @SerializedName("permalink")
    @Expose
    private String permalink;

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The networkId
     */
    public Long getNetworkId() {
        return networkId;
    }

    /**
     * 
     * @param networkId
     *     The network_id
     */
    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The webUrl
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * 
     * @param webUrl
     *     The web_url
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    /**
     * 
     * @return
     *     The state
     */
    public String getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * @param fullName
     *     The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 
     * @return
     *     The jobTitle
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * 
     * @param jobTitle
     *     The job_title
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * 
     * @return
     *     The mugshotUrl
     */
    public String getMugshotUrl() {
        return mugshotUrl;
    }

    /**
     * 
     * @param mugshotUrl
     *     The mugshot_url
     */
    public void setMugshotUrl(String mugshotUrl) {
        this.mugshotUrl = mugshotUrl;
    }

    /**
     * 
     * @return
     *     The mugshotUrlTemplate
     */
    public String getMugshotUrlTemplate() {
        return mugshotUrlTemplate;
    }

    /**
     * 
     * @param mugshotUrlTemplate
     *     The mugshot_url_template
     */
    public void setMugshotUrlTemplate(String mugshotUrlTemplate) {
        this.mugshotUrlTemplate = mugshotUrlTemplate;
    }

    /**
     * 
     * @return
     *     The activatedAt
     */
    public String getActivatedAt() {
        return activatedAt;
    }

    /**
     * 
     * @param activatedAt
     *     The activated_at
     */
    public void setActivatedAt(String activatedAt) {
        this.activatedAt = activatedAt;
    }

    /**
     * 
     * @return
     *     The autoActivated
     */
    public Boolean getAutoActivated() {
        return autoActivated;
    }

    /**
     * 
     * @param autoActivated
     *     The auto_activated
     */
    public void setAutoActivated(Boolean autoActivated) {
        this.autoActivated = autoActivated;
    }

    /**
     * 
     * @return
     *     The stats
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * 
     * @param stats
     *     The stats
     */
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    /**
     * 
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     *     The senderId
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     * 
     * @param senderId
     *     The sender_id
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * 
     * @return
     *     The repliedToId
     */
    public Object getRepliedToId() {
        return repliedToId;
    }

    /**
     * 
     * @param repliedToId
     *     The replied_to_id
     */
    public void setRepliedToId(Object repliedToId) {
        this.repliedToId = repliedToId;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The senderType
     */
    public String getSenderType() {
        return senderType;
    }

    /**
     * 
     * @param senderType
     *     The sender_type
     */
    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    /**
     * 
     * @return
     *     The threadId
     */
    public Long getThreadId() {
        return threadId;
    }

    /**
     * 
     * @param threadId
     *     The thread_id
     */
    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    /**
     * 
     * @return
     *     The messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * 
     * @param messageType
     *     The message_type
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * 
     * @return
     *     The systemMessage
     */
    public Boolean getSystemMessage() {
        return systemMessage;
    }

    /**
     * 
     * @param systemMessage
     *     The system_message
     */
    public void setSystemMessage(Boolean systemMessage) {
        this.systemMessage = systemMessage;
    }

    /**
     * 
     * @return
     *     The contentExcerpt
     */
    public String getContentExcerpt() {
        return contentExcerpt;
    }

    /**
     * 
     * @param contentExcerpt
     *     The content_excerpt
     */
    public void setContentExcerpt(String contentExcerpt) {
        this.contentExcerpt = contentExcerpt;
    }

    /**
     * 
     * @return
     *     The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 
     * @param language
     *     The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 
     * @return
     *     The privacy
     */
    public String getPrivacy() {
        return privacy;
    }

    /**
     * 
     * @param privacy
     *     The privacy
     */
    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    /**
     * 
     * @return
     *     The directMessage
     */
    public Boolean getDirectMessage() {
        return directMessage;
    }

    /**
     * 
     * @param directMessage
     *     The direct_message
     */
    public void setDirectMessage(Boolean directMessage) {
        this.directMessage = directMessage;
    }

    /**
     * 
     * @return
     *     The body
     */
    public Body_ getBody() {
        return body;
    }

    /**
     * 
     * @param body
     *     The body
     */
    public void setBody(Body_ body) {
        this.body = body;
    }

    /**
     * 
     * @return
     *     The threadStarterId
     */
    public Long getThreadStarterId() {
        return threadStarterId;
    }

    /**
     * 
     * @param threadStarterId
     *     The thread_starter_id
     */
    public void setThreadStarterId(Long threadStarterId) {
        this.threadStarterId = threadStarterId;
    }

    /**
     * 
     * @return
     *     The groupId
     */
    public Object getGroupId() {
        return groupId;
    }

    /**
     * 
     * @param groupId
     *     The group_id
     */
    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }

    /**
     * 
     * @return
     *     The topics
     */
    public List<Object> getTopics() {
        return topics;
    }

    /**
     * 
     * @param topics
     *     The topics
     */
    public void setTopics(List<Object> topics) {
        this.topics = topics;
    }

    /**
     * 
     * @return
     *     The hasAttachments
     */
    public Boolean getHasAttachments() {
        return hasAttachments;
    }

    /**
     * 
     * @param hasAttachments
     *     The has_attachments
     */
    public void setHasAttachments(Boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    /**
     * 
     * @return
     *     The invitedUserIds
     */
    public List<Object> getInvitedUserIds() {
        return invitedUserIds;
    }

    /**
     * 
     * @param invitedUserIds
     *     The invited_user_ids
     */
    public void setInvitedUserIds(List<Object> invitedUserIds) {
        this.invitedUserIds = invitedUserIds;
    }

    /**
     * 
     * @return
     *     The readOnly
     */
    public Boolean getReadOnly() {
        return readOnly;
    }

    /**
     * 
     * @param readOnly
     *     The read_only
     */
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * 
     * @return
     *     The normalizedName
     */
    public String getNormalizedName() {
        return normalizedName;
    }

    /**
     * 
     * @param normalizedName
     *     The normalized_name
     */
    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    /**
     * 
     * @return
     *     The permalink
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * 
     * @param permalink
     *     The permalink
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

}
