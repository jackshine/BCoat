/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.universal.dtos.dropbox;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author rahulkumar
 */
public class DropboxAccountInfo {
    
    String displayName;
    String country;
    String referralLink;
    String userId;
    String quota;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReferralLink() {
        return referralLink;
    }

    public void setReferralLink(String referralLink) {
        this.referralLink = referralLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }
    
    @Override
	public String toString() {
          return ToStringBuilder.reflectionToString(this);
    
}
}