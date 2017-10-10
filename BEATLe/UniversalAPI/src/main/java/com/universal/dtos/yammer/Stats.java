
package com.universal.dtos.yammer;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Stats {

    @SerializedName("updates")
    @Expose
    private Long updates;
    @SerializedName("first_reply_id")
    @Expose
    private Object firstReplyId;
    @SerializedName("first_reply_at")
    @Expose
    private Object firstReplyAt;
    @SerializedName("latest_reply_id")
    @Expose
    private Long latestReplyId;
    @SerializedName("latest_reply_at")
    @Expose
    private String latestReplyAt;
    @SerializedName("shares")
    @Expose
    private Long shares;

    /**
     * 
     * @return
     *     The updates
     */
    public Long getUpdates() {
        return updates;
    }

    /**
     * 
     * @param updates
     *     The updates
     */
    public void setUpdates(Long updates) {
        this.updates = updates;
    }

    /**
     * 
     * @return
     *     The firstReplyId
     */
    public Object getFirstReplyId() {
        return firstReplyId;
    }

    /**
     * 
     * @param firstReplyId
     *     The first_reply_id
     */
    public void setFirstReplyId(Object firstReplyId) {
        this.firstReplyId = firstReplyId;
    }

    /**
     * 
     * @return
     *     The firstReplyAt
     */
    public Object getFirstReplyAt() {
        return firstReplyAt;
    }

    /**
     * 
     * @param firstReplyAt
     *     The first_reply_at
     */
    public void setFirstReplyAt(Object firstReplyAt) {
        this.firstReplyAt = firstReplyAt;
    }

    /**
     * 
     * @return
     *     The latestReplyId
     */
    public Long getLatestReplyId() {
        return latestReplyId;
    }

    /**
     * 
     * @param latestReplyId
     *     The latest_reply_id
     */
    public void setLatestReplyId(Long latestReplyId) {
        this.latestReplyId = latestReplyId;
    }

    /**
     * 
     * @return
     *     The latestReplyAt
     */
    public String getLatestReplyAt() {
        return latestReplyAt;
    }

    /**
     * 
     * @param latestReplyAt
     *     The latest_reply_at
     */
    public void setLatestReplyAt(String latestReplyAt) {
        this.latestReplyAt = latestReplyAt;
    }

    /**
     * 
     * @return
     *     The shares
     */
    public Long getShares() {
        return shares;
    }

    /**
     * 
     * @param shares
     *     The shares
     */
    public void setShares(Long shares) {
        this.shares = shares;
    }

}
