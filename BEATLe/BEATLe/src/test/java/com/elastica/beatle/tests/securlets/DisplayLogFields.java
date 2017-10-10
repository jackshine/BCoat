/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle.tests.securlets;

/**
 *
 * @author rahulkumar
 */
public class DisplayLogFields {
    
    String message;
    String activityType;
    String fileOrFolderName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message.trim();
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getFileOrFolderName() {
        return fileOrFolderName;
    }

    public void setFileOrFolderName(String fileOrFolderName) {
        this.fileOrFolderName = fileOrFolderName;
    }
    
    @Override
    public String toString() {
        return "DisplayLogFields{" + "message=" + message + ", activityType=" + activityType + ", fileOrFolderName=" + fileOrFolderName + '}';
    }  
    
}
