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
public class DropboxDownloadFileInfo {
    
     private String iconName;
     private String mightHaveThumbnail;
     private long numBytes;
     private String humanSize;
     private String lastModified;
     private String clientMtime;
     private String rev;
     
     public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getMightHaveThumbnail() {
        return mightHaveThumbnail;
    }

    public void setMightHaveThumbnail(String mightHaveThumbnail) {
        this.mightHaveThumbnail = mightHaveThumbnail;
    }

    public long getNumBytes() {
        return numBytes;
    }

    public void setNumBytes(long numBytes) {
        this.numBytes = numBytes;
    }

    public String getHumanSize() {
        return humanSize;
    }

    public void setHumanSize(String humanSize) {
        this.humanSize = humanSize;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getClientMtime() {
        return clientMtime;
    }

    public void setClientMtime(String clientMtime) {
        this.clientMtime = clientMtime;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }
    
    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
        }
}
