package com.universal.dtos.box;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.universal.util.OAuth20Token;

public class BoxToken implements Serializable{
	OAuth20Token oauthToken;
	long expiresOn;
	
	public BoxToken(OAuth20Token oauthToken, long expiresOn) {
		this.oauthToken = oauthToken;
		this.expiresOn  = expiresOn;
	}
	
	public OAuth20Token getOauthToken() {
		return oauthToken;
	}
	public void setOauthToken(OAuth20Token oauthToken) {
		this.oauthToken = oauthToken;
	}
	public long getExpiresOn() {
		return expiresOn;
	}
	public void setExpirationTime(long expiresOn) {
		this.expiresOn = expiresOn;
	}
	
	public boolean isTokenExpired() {
		long now = System.currentTimeMillis();
		
		if(this.expiresOn < now) {
			return true;
		}
		return false;
	}
	
	
	
	
	
}
