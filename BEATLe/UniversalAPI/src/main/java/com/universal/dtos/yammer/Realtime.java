
package com.universal.dtos.yammer;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Realtime {

    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("authentication_token")
    @Expose
    private String authenticationToken;
    @SerializedName("channel_id")
    @Expose
    private String channelId;

    /**
     * 
     * @return
     *     The uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * 
     * @param uri
     *     The uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 
     * @return
     *     The authenticationToken
     */
    public String getAuthenticationToken() {
        return authenticationToken;
    }

    /**
     * 
     * @param authenticationToken
     *     The authentication_token
     */
    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    /**
     * 
     * @return
     *     The channelId
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * 
     * @param channelId
     *     The channel_id
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

}
