
package com.universal.dtos.yammer;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Message {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("sender_id")
    @Expose
    private Long senderId;
    @SerializedName("replied_to_id")
    @Expose
    private Object repliedToId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("network_id")
    @Expose
    private Long networkId;
    @SerializedName("message_type")
    @Expose
    private String messageType;
    @SerializedName("sender_type")
    @Expose
    private String senderType;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("web_url")
    @Expose
    private String webUrl;
    @SerializedName("body")
    @Expose
    private Body body;
    @SerializedName("thread_id")
    @Expose
    private Long threadId;
    @SerializedName("client_type")
    @Expose
    private String clientType;
    @SerializedName("client_url")
    @Expose
    private String clientUrl;
    @SerializedName("system_message")
    @Expose
    private Boolean systemMessage;
    @SerializedName("direct_message")
    @Expose
    private Boolean directMessage;
    @SerializedName("chat_client_sequence")
    @Expose
    private Object chatClientSequence;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("notified_user_ids")
    @Expose
    private List<Object> notifiedUserIds = new ArrayList<Object>();
    @SerializedName("system_message_properties")
    @Expose
    private SystemMessageProperties systemMessageProperties;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("attachments")
    @Expose
    private List<Object> attachments = new ArrayList<Object>();
    @SerializedName("liked_by")
    @Expose
    private LikedBy likedBy;
    @SerializedName("content_excerpt")
    @Expose
    private String contentExcerpt;
    @SerializedName("group_created_id")
    @Expose
    private Object groupCreatedId;

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
     *     The body
     */
    public Body getBody() {
        return body;
    }

    /**
     * 
     * @param body
     *     The body
     */
    public void setBody(Body body) {
        this.body = body;
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
     *     The clientType
     */
    public String getClientType() {
        return clientType;
    }

    /**
     * 
     * @param clientType
     *     The client_type
     */
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    /**
     * 
     * @return
     *     The clientUrl
     */
    public String getClientUrl() {
        return clientUrl;
    }

    /**
     * 
     * @param clientUrl
     *     The client_url
     */
    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
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
     *     The chatClientSequence
     */
    public Object getChatClientSequence() {
        return chatClientSequence;
    }

    /**
     * 
     * @param chatClientSequence
     *     The chat_client_sequence
     */
    public void setChatClientSequence(Object chatClientSequence) {
        this.chatClientSequence = chatClientSequence;
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
     *     The notifiedUserIds
     */
    public List<Object> getNotifiedUserIds() {
        return notifiedUserIds;
    }

    /**
     * 
     * @param notifiedUserIds
     *     The notified_user_ids
     */
    public void setNotifiedUserIds(List<Object> notifiedUserIds) {
        this.notifiedUserIds = notifiedUserIds;
    }

    /**
     * 
     * @return
     *     The systemMessageProperties
     */
    public SystemMessageProperties getSystemMessageProperties() {
        return systemMessageProperties;
    }

    /**
     * 
     * @param systemMessageProperties
     *     The system_message_properties
     */
    public void setSystemMessageProperties(SystemMessageProperties systemMessageProperties) {
        this.systemMessageProperties = systemMessageProperties;
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
     *     The attachments
     */
    public List<Object> getAttachments() {
        return attachments;
    }

    /**
     * 
     * @param attachments
     *     The attachments
     */
    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    /**
     * 
     * @return
     *     The likedBy
     */
    public LikedBy getLikedBy() {
        return likedBy;
    }

    /**
     * 
     * @param likedBy
     *     The liked_by
     */
    public void setLikedBy(LikedBy likedBy) {
        this.likedBy = likedBy;
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
     *     The groupCreatedId
     */
    public Object getGroupCreatedId() {
        return groupCreatedId;
    }

    /**
     * 
     * @param groupCreatedId
     *     The group_created_id
     */
    public void setGroupCreatedId(Object groupCreatedId) {
        this.groupCreatedId = groupCreatedId;
    }

}
