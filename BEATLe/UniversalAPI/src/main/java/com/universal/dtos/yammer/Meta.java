
package com.universal.dtos.yammer;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Meta {

    @SerializedName("older_available")
    @Expose
    private Boolean olderAvailable;
    @SerializedName("requested_poll_interval")
    @Expose
    private Long requestedPollInterval;
    @SerializedName("realtime")
    @Expose
    private Realtime realtime;
    @SerializedName("last_seen_message_id")
    @Expose
    private Object lastSeenMessageId;
    @SerializedName("current_user_id")
    @Expose
    private Long currentUserId;
    @SerializedName("followed_references")
    @Expose
    private List<Object> followedReferences = new ArrayList<Object>();
    @SerializedName("ymodules")
    @Expose
    private List<Object> ymodules = new ArrayList<Object>();
    @SerializedName("newest_message_details")
    @Expose
    private Object newestMessageDetails;
    @SerializedName("feed_name")
    @Expose
    private String feedName;
    @SerializedName("feed_desc")
    @Expose
    private String feedDesc;
    @SerializedName("direct_from_body")
    @Expose
    private Boolean directFromBody;

    /**
     * 
     * @return
     *     The olderAvailable
     */
    public Boolean getOlderAvailable() {
        return olderAvailable;
    }

    /**
     * 
     * @param olderAvailable
     *     The older_available
     */
    public void setOlderAvailable(Boolean olderAvailable) {
        this.olderAvailable = olderAvailable;
    }

    /**
     * 
     * @return
     *     The requestedPollInterval
     */
    public Long getRequestedPollInterval() {
        return requestedPollInterval;
    }

    /**
     * 
     * @param requestedPollInterval
     *     The requested_poll_interval
     */
    public void setRequestedPollInterval(Long requestedPollInterval) {
        this.requestedPollInterval = requestedPollInterval;
    }

    /**
     * 
     * @return
     *     The realtime
     */
    public Realtime getRealtime() {
        return realtime;
    }

    /**
     * 
     * @param realtime
     *     The realtime
     */
    public void setRealtime(Realtime realtime) {
        this.realtime = realtime;
    }

    /**
     * 
     * @return
     *     The lastSeenMessageId
     */
    public Object getLastSeenMessageId() {
        return lastSeenMessageId;
    }

    /**
     * 
     * @param lastSeenMessageId
     *     The last_seen_message_id
     */
    public void setLastSeenMessageId(Object lastSeenMessageId) {
        this.lastSeenMessageId = lastSeenMessageId;
    }

    /**
     * 
     * @return
     *     The currentUserId
     */
    public Long getCurrentUserId() {
        return currentUserId;
    }

    /**
     * 
     * @param currentUserId
     *     The current_user_id
     */
    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    /**
     * 
     * @return
     *     The followedReferences
     */
    public List<Object> getFollowedReferences() {
        return followedReferences;
    }

    /**
     * 
     * @param followedReferences
     *     The followed_references
     */
    public void setFollowedReferences(List<Object> followedReferences) {
        this.followedReferences = followedReferences;
    }

    /**
     * 
     * @return
     *     The ymodules
     */
    public List<Object> getYmodules() {
        return ymodules;
    }

    /**
     * 
     * @param ymodules
     *     The ymodules
     */
    public void setYmodules(List<Object> ymodules) {
        this.ymodules = ymodules;
    }

    /**
     * 
     * @return
     *     The newestMessageDetails
     */
    public Object getNewestMessageDetails() {
        return newestMessageDetails;
    }

    /**
     * 
     * @param newestMessageDetails
     *     The newest_message_details
     */
    public void setNewestMessageDetails(Object newestMessageDetails) {
        this.newestMessageDetails = newestMessageDetails;
    }

    /**
     * 
     * @return
     *     The feedName
     */
    public String getFeedName() {
        return feedName;
    }

    /**
     * 
     * @param feedName
     *     The feed_name
     */
    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    /**
     * 
     * @return
     *     The feedDesc
     */
    public String getFeedDesc() {
        return feedDesc;
    }

    /**
     * 
     * @param feedDesc
     *     The feed_desc
     */
    public void setFeedDesc(String feedDesc) {
        this.feedDesc = feedDesc;
    }

    /**
     * 
     * @return
     *     The directFromBody
     */
    public Boolean getDirectFromBody() {
        return directFromBody;
    }

    /**
     * 
     * @param directFromBody
     *     The direct_from_body
     */
    public void setDirectFromBody(Boolean directFromBody) {
        this.directFromBody = directFromBody;
    }

}
