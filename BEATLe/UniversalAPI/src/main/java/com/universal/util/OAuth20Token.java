package com.universal.util;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.testng.Reporter;

public class OAuth20Token extends Error implements Serializable{
	private static final long serialVersionUID = 1L;
    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private String userId;
    //Current time in milliseconds
    private long createdTime;

    //Added additional vars for salesforce
    private String instanceUrl;
   	private String id;
   	private String issuedAt;
    private String signature;
    private String instanceId;
    
    public String getInstanceId() {
    	if(this.getId()!=null) {
    		this.instanceId = this.getId().split("/")[4];
    	}
    	return this.instanceId;
    }
    
    
	public String getAccessToken(){
		if(this.getExpiresIn() != 0) {
			long expirationTime = this.getExpiresIn() * 1000 + this.getCreatedTime() + 1000; // Added a buffer of 10 secs
			//if expiration time is ahead of current time, return the token. Else send null 
			if(expirationTime > System.currentTimeMillis()) {
				return this.accessToken;
			} else {
				return null;
			}
		} else {
			return this.accessToken;
		}
    }
    
    @JsonProperty("access_token")
    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
        //Set the current time in ms
        this.setCreatedTime(System.currentTimeMillis());
    }
    
    public int getExpiresIn(){
        return this.expiresIn;
    }
    
    @JsonProperty("expires_in")  
    public void setExpiresIn(int expiresIn){
        this.expiresIn = expiresIn;
    }
    
    public String getRefreshToken(){
        return this.refreshToken;
    }
    
    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    
    public String getScope(){
        return this.scope;
    }
    
    public void setScope(String scope){
        this.scope = scope;
    }
    
    public String getTokenType(){
        return this.tokenType;
    }
    
    @JsonProperty("token_type")
    public void setTokenType(String tokenType){
        this.tokenType = tokenType;
    }
    
    /**
	 * @return the createdTime
	 */
	public long getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

    
    public String getUserId(){
        return this.userId;
    }
    
    public void setUserId(String userId){
        this.userId = userId;
    }
    
    /**
	 * @return the instanceUrl
	 */
	public String getInstanceUrl() {
		return instanceUrl;
	}

	/**
	 * @param instanceUrl the instanceUrl to set
	 */
	@JsonProperty("instance_url")
	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the issuedAt
	 */
	public String getIssuedAt() {
		return issuedAt;
	}

	/**
	 * @param issuedAt the issuedAt to set
	 */
	@JsonProperty("issued_at")
	public void setIssuedAt(String issuedAt) {
		this.issuedAt = issuedAt;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
}

