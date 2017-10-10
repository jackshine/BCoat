package com.universal.common;

import com.google.api.services.drive.Drive.Properties.Delete;
import com.google.api.services.drive.Drive.Properties.Patch;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.compute.ComputeCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.model.*;
import java.util.List;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.Drive.Properties.Get;
import com.google.api.services.drive.Drive.Properties.Update;
import com.google.api.services.drive.model.Property;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.universal.dtos.box.FileUploadResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.testng.Reporter;

public class GDrive {

    String rootFolderID;
    private static final String APPLICATION_NAME = "QaTest";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;
    Drive service;
    String access_token;
    int maxRetryCount = 10;
    long waitTime = 180000; // 30 secs...
    int retryCounter = 0;

    public String getRootFolderID() {
        return rootFolderID;
    }

    public void setRootFolderID(String rootFolderID) {
        this.rootFolderID = rootFolderID;
    }

    public GDrive(String access_token) {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(GDrive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GDrive.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.access_token = access_token;
            this.service = getDriveService();
            Map<String, String> printAbout = printAbout();
            setRootFolderID(printAbout.get("rootfolderId"));
        } catch (IOException ex) {
            Logger.getLogger(GDrive.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Credential authorize() throws IOException {
        Credential credential = new ComputeCredential(HTTP_TRANSPORT, JSON_FACTORY);
        credential.setAccessToken(this.access_token);
        Reporter.log(" ## Access Token :" + credential.getAccessToken());
        Reporter.log(" ## Refresh Token :" + credential.getRefreshToken());
        return credential;
    }

    public Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public InputStream downloadFile(String fileId) {
        File file = null;
        try {
            file = service.files().get(fileId).execute();
            Reporter.log("Title: " + file.getTitle(), true);
            Reporter.log("Description: " + file.getDescription(), true);
            Reporter.log("MIME type: " + file.getMimeType(), true);
        } catch (IOException e) {
            Reporter.log("An error occured: " + e, true);
        }
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                HttpResponse resp
                        = service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                        .execute();
                return resp.getContent();
            } catch (IOException e) {
                // An error occurred.
                e.printStackTrace();
                return null;
            }
        } else {
            Reporter.log("The file doesn't have any content stored on Drive.", true);
            return null;
        }
    }
    
    public Map<String, InputStream> downloadFilesFromFolder(String folderId) throws IOException {
        Map<String, InputStream> map = new HashMap<>();
        List<String> printFilesInFolder = printFilesInFolder(folderId);
        for (String fileId : printFilesInFolder) {
            File file = null;
            String fileName="";
            try {
                file = service.files().get(fileId).execute();
                fileName=file.getTitle();
                Reporter.log("Title: " + fileName, true);
                Reporter.log("Description: " + file.getDescription(), true);
                Reporter.log("MIME type: " + file.getMimeType(), true);
                
            } catch (IOException e) {
                Reporter.log("An error occured: " + e, true);
            }
            if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
                try {
                    HttpResponse resp
                            = service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                            .execute();
                    System.out.println(" Download URL :"+file.getDownloadUrl());
                    InputStream content = resp.getContent();
                    
                    map.put(fileName, content);
                   // return resp.getContent();
                } catch (IOException e) {
                    // An error occurred.
                    e.printStackTrace();
                   // return null;
                }
            } else {
                Reporter.log("The file doesn't have any content stored on Drive.", true);
               // return null;
            }
        }
        return map;
    }

    public List<String> listFileNames() throws IOException{
        List<File> listFile = listFile();
        List<String> fileNameList=new ArrayList<>();
        for (File file : listFile) {
            fileNameList.add(file.getTitle());
        }
        return fileNameList;
    }

    public List<File> listFile() throws IOException {
        try {
            // Print the names and IDs for up to 1000 files.
            Reporter.log("======|| Below Files/Folder Found From the Root Folder || Email ID :" + getEmailId(), true);
        } catch (InterruptedException ex) {
            Reporter.log("======Issue with File/Folder list from Root Folder :"+ex.getLocalizedMessage(),true);
        }
        FileList result = service.files().list().setMaxResults(1000).execute();
        List<File> files = result.getItems();
        if (files == null || files.size() == 0) {
            Reporter.log("No files found.", true);
        } else {
            int i=1;
            Reporter.log("Files:");
            for (File file : files) {
                Reporter.log(i++ +") ## File Title : " + file.getTitle() + " || File ID : " + file.getId(), true);
            }
        }
        return files;
    }

    public Map<String, String> printAbout() {
        Map<String, String> printAbout = new HashMap<String, String>();
        try {
            About about = service.about().get().execute();
            printAbout.put("name", about.getName());
            printAbout.put("rootfolderId", about.getRootFolderId());
            printAbout.put("totalQuota", about.getQuotaBytesTotal().toString());
            printAbout.put("quotaUsed", about.getQuotaBytesUsed().toString());
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return printAbout;
    }

    public void deleteFile(String fileId) {
        try {
            Void execute = service.files().delete(fileId).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
    }

    public InputStream downloadFile(File file) {
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                HttpResponse resp = service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
                return resp.getContent();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public File printFile(String fileId) throws IOException {
        File file = null;
        try {
            file = service.files().get(fileId).execute();
            Reporter.log("Title: " + file.getTitle(), true);
            Reporter.log("Description: " + file.getDescription(), true);
            Reporter.log("MIME type: " + file.getMimeType(), true);
        } catch (IOException e) {
            Reporter.log("An error occured: " + e, true);
        }
        return file;
    }

    public FileUploadResponse uploadFile(String parentID, String localFileLocation, String fileName) {
        Reporter.log("File To be Uploaded in GDRIVE :" + fileName, true);
        File insertFile = insertFile(fileName, "Automation for file Upload", parentID, null, localFileLocation);
        String response = insertFile.toString();
        Reporter.log("GDrive File Upload Response :" + response, true);
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        fileUploadResponse.setEtag(insertFile.getEtag());
        fileUploadResponse.setFileId(insertFile.getId());
        fileUploadResponse.setResponseCode(200);
        fileUploadResponse.setResponseMessage(response);
        return fileUploadResponse;
    }

    public FileUploadResponse uploadFile(String parentID, String localFileLocation) {
        java.io.File file = new java.io.File(localFileLocation);
        String fileName = file.getName();
        Reporter.log("File To be Uploaded in GDRIVE :" + fileName, true);
        File insertFile = insertFile(fileName, "Automation for file Upload", parentID, null, localFileLocation);
        String response = insertFile.toString();
        Reporter.log("GDrive File Upload Response :" + response, true);
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        fileUploadResponse.setEtag(insertFile.getEtag());
        fileUploadResponse.setFileId(insertFile.getId());
        fileUploadResponse.setResponseCode(200);
        fileUploadResponse.setResponseMessage(response);
        return fileUploadResponse;
    }

    public <T> T uploadFile(String filename) {
        Reporter.log(" ### File will be uploaded in the root Folder....", true);
        return (T) uploadFile(null, filename);
    }

    public <T> T createFolder(String folderName) throws InterruptedException, IOException {
        Reporter.log(" ### It will create folder in root folder...### ", true);
        File body = new File();
        body.setTitle(folderName);
        body.setMimeType("application/vnd.google-apps.folder");
        File file = null;
        Random randomGenerator = new Random();
        for (int n = 0; n < 10; ++n) {
            try {
                file = service.files().insert(body).execute();
                Reporter.log("## Title: " + file.getTitle(), true);
                Reporter.log("## Description: " + file.getDescription(), true);
                Reporter.log("## MIME type: " + file.getMimeType(), true);
                return (T) file.getId();
            } catch (GoogleJsonResponseException e) {
                Reporter.log(" ### Exception found in Folder Creation : " + e.getDetails().getMessage(), true);
                // Apply exponential backoff.
                createFolder(folderName);
                Thread.sleep((1 << n) * 1000 + randomGenerator.nextInt(1001));
            }
        }
        Reporter.log(" ## There has been an error, the request never succeeded.", true);
        return null;
    }

    public File insertFile(String title, String description,
            String parentId, String mimeType, String filename) {
        // File's metadata.
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);
        // Set the parent folder.
        if (parentId != null && parentId.length() > 0) {
            body.setParents(
                    Arrays.asList(new ParentReference().setId(parentId)));
        }
        // File's content.
        java.io.File fileContent = new java.io.File(filename);
        FileContent mediaContent = new FileContent(mimeType, fileContent);
        File file = null;
        try {
            file = service.files().insert(body, mediaContent).execute();
            // Uncomment the following line to print the File ID.
            Reporter.log("## File ID: " + file.getId(), true);
            return file;
        } catch (IOException e) {
            Reporter.log("Issue with File Upload in GDrive.... Will TRY Again..", true);
            retryCounter++;
            if (retryCounter <= maxRetryCount) {
                Reporter.log("Issue with File Upload in GDrive.... Will TRY Again..Retry Count:" + retryCounter, true);
                Logger.getLogger(GDrive.class.getName()).log(Level.SEVERE, null, e);
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GDrive.class.getName()).log(Level.SEVERE, null, ex);
                }
                insertFile(title, description, parentId, mimeType, filename);
                Reporter.log("An error occured: " + e, true);
            }
        }
        return file;
    }

    public Void emptyTrash() {
        try {
            Reporter.log("## Trash clean Up in PROGRESS.... for user :"+getEmailId(),true);
            service.files().emptyTrash().execute();
            Reporter.log("## Trash clean Up DONE for user :"+getEmailId(),true);
        } catch (IOException | InterruptedException e) {
            Reporter.log("An error occurred in empty Trash: " + e.getLocalizedMessage(), true);
        }
        return null;
    }
     
    public String trashFile(String fileId) {
        try {
            return service.files().trash(fileId).execute().toPrettyString();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    public String restoreFile(String fileId) {
        try {
            return service.files().untrash(fileId).execute().toPrettyString();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    public File createFile(String title, String description, String parentId, String mimeType, String filename) {
        // File's metadata.
        Reporter.log("Supporter MIME Type : https://developers.google.com/drive/web/mime-types", true);
        File file = new File();
        file.setTitle(title);
        file.setDescription(description);
        String defaultMimeType = "application/vnd.google-apps.form";
        if (mimeType != null) {
            file.setMimeType(mimeType);
        } else {
            file.setMimeType(defaultMimeType);
        }
        try {
             if (parentId != null && parentId.length() > 0) {
            file.setParents(Arrays.asList(new ParentReference().setId(parentId)));
             }
            file = service.files().insert(file).execute();
             // Set the parent folder.
        } catch (IOException ex) {
            Reporter.log("Issue Found in File Creation  :" + ex.getLocalizedMessage(), true);
        }
        // Print the new file ID.
        System.out.println("File ID: %s" + file.getId());
        return file;
    }

    /**
     * Update a file's modified date.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update the modified date for.
     * @return The updated file if successful, {@code null} otherwise.
     */
    public File updateModifiedDate(String fileId) {
        try {
            return service.files().touch(fileId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return null;
    }

    /**
     * Start watching for changes to a file.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to watch.
     * @param channelId Unique string that identifies this channel.
     * @param channelType Type of delivery mechanism used for this channel.
     * @param channelAddress Address where notifications are delivered.
     * @return The created channel if successful, {@code null} otherwise.
     */
    public Channel watchFile(String fileId,
            String channelId, String channelType, String channelAddress) {
        Channel channel = new Channel();
        channel.setId(channelId);
        channel.setType(channelType);
        channel.setAddress(channelAddress);
        try {
            return service.files().watch(fileId, channel).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File copyFile(String originFileId, String copyTitle) {
        System.out.println(" For now..as Parents are not set it will be copied to the root folder...");
        File copiedFile = new File();
        copiedFile.setTitle(copyTitle);

        try {
            return service.files().copy(originFileId, copiedFile).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return null;
    }

    public void removeFileFromFolder(String folderId, String fileId) {
        try {
            service.children().delete(folderId, fileId).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
    }

    public boolean isFileInFolder(String folderId, String fileId) throws IOException {
        try {
            service.children().get(folderId, fileId).execute();
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                return false;
            } else {
                Reporter.log("An error occurred: " + e, true);
                throw e;
            }
        }
        return true;
    }

    public <T> T insertFileIntoFolder(String fileId, String destinationFolderId) {
        ChildReference newChild = new ChildReference();
        newChild.setId(fileId);
        try {
            ChildReference execute = service.children().insert(destinationFolderId, newChild).execute();
            execute.getId();
            return (T) execute;
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    public List printFilesInFolder(String folderId) throws IOException {
        List<String> filIds = new ArrayList<>();
        Children.List request = service.children().list(folderId);
        do {
            ChildList children = request.execute();
            for (ChildReference child : children.getItems()) {
                Reporter.log("File Id: " + child.getId());
                filIds.add(child.getId());
            }
            request.setPageToken(children.getNextPageToken());
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);
        return filIds;
    }

    public List<String> printParentIDs(String fileId) {
        List<String> parentIdList = new ArrayList<>();
        try {
            ParentList parents = service.parents().list(fileId).execute();
            for (ParentReference parent : parents.getItems()) {
                Reporter.log("Parent Id: " + parent.getId(), true);
                parentIdList.add(parent.getId());
            }
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return parentIdList;
    }

    public Void removePermission(String fileId, String permissionId) {
        try {
            return service.permissions().delete(fileId, permissionId).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
            return null;
        }
    }

    public Void removePermission(Drive service, String fileId, String permissionId) {
        try {
            return service.permissions().delete(fileId, permissionId).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
            return null;
        }
    }

    public File setUseContentAsIndexableText(String fileId) {
        try {
            File file = new File();
            Files.Patch patchRequest = service.files().patch(fileId, file);
            patchRequest.setUseContentAsIndexableText(Boolean.TRUE);
            File updatedFile = patchRequest.execute();
            return updatedFile;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            return null;
        }
    }

    public File renameFile(String fileId, String newFileName) {
        try {
            File file = new File();
            file.setTitle(newFileName);

            // Rename the file.
            Files.Patch patchRequest = service.files().patch(fileId, file);
            patchRequest.setFields("title");

            File updatedFile = patchRequest.execute();
            return updatedFile;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            return null;
        }
    }

    // https://developers.google.com/drive/web/mime-types
    public File updateFolder(String folderId, String newTitle,
            String newDescription, String newMimeType, String newFolderName, boolean newRevision) {
        try {
            // First retrieve the file from the API ...
            File file = service.files().get(folderId).execute();
            // File's new metadata ...
            file.setTitle(newTitle);
            file.setDescription(newDescription);
            file.setMimeType(newMimeType);
            // File's new content.
            java.io.File fileContent = new java.io.File(newFolderName);
            FileContent mediaContent = new FileContent(newMimeType, fileContent);
            // Send the request to the API.
            File updatedFile = service.files().update(folderId, file, mediaContent).execute();
            return updatedFile;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            return null;
        }
    }

    public void moveFileToFolder(String fileId, String folderId) throws IOException {
        Reporter.log("Move File To A Folder", true);
        File file = service.files().get(fileId).execute();
        File targetFolder = service.files().get(folderId).execute();
        ParentReference newParent = new ParentReference();
        newParent.setSelfLink(targetFolder.getSelfLink());
        // newParent.setParentLink(targetFolder.g);
        newParent.setId(folderId);
        newParent.setKind(targetFolder.getKind());
        newParent.setIsRoot(false);
        List<ParentReference> parentsList = new ArrayList<ParentReference>();
        parentsList.add(newParent);
        file.setParents(parentsList);
        File updatedFile = service.files().update(fileId, file).execute();
    }

    /**
     * Update an existing file's metadata and content.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update.
     * @param newTitle New title for the file.
     * @param newDescription New description for the file.
     * @param newMimeType New MIME type for the file.
     * @param newFilename Filename of the new content to upload.
     * @param newRevision Whether or not to create a new revision for this file.
     * @return Updated file metadata if successful, {@code null} otherwise.
     */
    public File updateFile(String fileId, String newFileName, String newDescription, String newMimeType, String localFileLocation, boolean newRevision) {
        try {
            // First retrieve the file from the API.
            File file = service.files().get(fileId).execute();

            // File's new metadata.
            file.setTitle(newFileName);
            file.setDescription(newDescription);
            file.setMimeType(newMimeType);

            // File's new content.
            java.io.File fileContent = new java.io.File(localFileLocation);
            FileContent mediaContent = new FileContent(newMimeType, fileContent);

            // Send the request to the API.
            File updatedFile = service.files().update(fileId, file, mediaContent).execute();

            return updatedFile;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            return null;
        }
    }

    public Map<String, String> printPermission(String fileId, String permissionId) {
        Map<String, String> permissionMap = new HashMap<>();
        try {
            Permission permission = service.permissions().get(fileId, permissionId).execute();
            permissionMap.put("Name", permission.getName());
            permissionMap.put("Role", permission.getRole());
            permissionMap.put("Additional role", permission.getAdditionalRoles().toString());
        } catch (IOException e) {
            Reporter.log("An error occured: " + e, true);
        }
        return permissionMap;
    }

    public Map<String, String> printPermission(Drive service, String fileId, String permissionId) {
        Map<String, String> permissionMap = new HashMap<>();
        try {
            Permission permission = service.permissions().get(fileId, permissionId).execute();
            permissionMap.put("Name", permission.getName());
            permissionMap.put("Role", permission.getRole());
            permissionMap.put("Additional role", permission.getAdditionalRoles().toString());
        } catch (IOException e) {
            Reporter.log("An error occured: " + e, true);
        }
        return permissionMap;
    }

    public Permission insertPermissionWithComment(String fileId, String value, String type, String role) {
        Permission newPermission = new Permission();
        newPermission.setValue(value);
        newPermission.setType(type);
        newPermission.setRole(role);
        newPermission.setWithLink(Boolean.TRUE);
        List<String> additionalRoles=new ArrayList<>();
        additionalRoles.add("commenter");
        newPermission.setAdditionalRoles(additionalRoles);
        try {
            return service.permissions().insert(fileId, newPermission).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }
    
    public Permission insertPermission(String fileId, String value, String type, String role,boolean commentRole,boolean webLink) {
        Permission newPermission = new Permission();
        newPermission.setValue(value);
        newPermission.setType(type);
        newPermission.setRole(role);
        if(webLink){
        newPermission.setWithLink(Boolean.TRUE);
        }
        else{
          newPermission.setWithLink(null);  
        }
        if(commentRole){
        List<String> additionalRoles=new ArrayList<>();
        additionalRoles.add("commenter");
        newPermission.setAdditionalRoles(additionalRoles);
        }
        try {
            return service.permissions().insert(fileId, newPermission).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }
    
    // Searchable on Web....
    public Permission insertPermissionWithWebLink(String fileId, String value, String type, String role) {
        Permission newPermission = new Permission();
        newPermission.setValue(value);
        newPermission.setType(type);
        newPermission.setRole(role);
        newPermission.setWithLink(Boolean.TRUE);
        try {
            return service.permissions().insert(fileId, newPermission).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }
    
    public Permission insertPermissionWithWebLink(String fileId, boolean commentRole,String value, String type, String role) {
        Permission newPermission = new Permission();
        newPermission.setValue(value);
        newPermission.setType(type);
        newPermission.setRole(role);
        newPermission.setWithLink(Boolean.TRUE);
        if(commentRole){
        List<String> additionalRoles=new ArrayList<>();
        additionalRoles.add("commenter");
        newPermission.setAdditionalRoles(additionalRoles);
        }
        try {
            return service.permissions().insert(fileId, newPermission).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    public Permission insertPermission(String fileId, Permission permission) {
        try {
            return service.permissions().insert(fileId, permission).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    public <T> T createFolder(String folderName,List<String> parentIdList) throws InterruptedException, IOException {
        Reporter.log(" ### It will create folder in root folder...### ", true);
        File body = new File();
        List<ParentReference> parentReferences=new ArrayList<>();
        for (String parentID : parentIdList) {
           ParentReference parentReference=new ParentReference();
           parentReference.setId(parentID);
           parentReferences.add(parentReference);
        }      
        body.setTitle(folderName);
        body.setParents(parentReferences);
        body.setMimeType("application/vnd.google-apps.folder");
        File file = null;
        Random randomGenerator = new Random();
        for (int n = 0; n < 10; ++n) {
            try {
                file = service.files().insert(body).execute();
                Reporter.log("## Title: " + file.getTitle(), true);
                Reporter.log("## Description: " + file.getDescription(), true);
                Reporter.log("## MIME type: " + file.getMimeType(), true);
                return (T) file.getId();
            } catch (GoogleJsonResponseException e) {
                Reporter.log(" ### Exception found in Folder Creation : " + e.getDetails().getMessage(), true);
                // Apply exponential backoff.
                createFolder(folderName);
                Thread.sleep((1 << n) * 1000 + randomGenerator.nextInt(1001));
            }
        }
        Reporter.log(" ## There has been an error, the request never succeeded.", true);
        return null;
    }
    
    public Permission insertPermission(Drive service, String fileId, String value, String type, String role, Boolean setLink) {
        Permission newPermission = new Permission();
        newPermission.setValue(value);
        newPermission.setType(type);
        newPermission.setRole(role);
        newPermission.setWithLink(setLink);
        try {
            return service.permissions().insert(fileId, newPermission).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    
    
    /**
     * Insert a new permission.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to insert permission for.
     * @param value User or group e-mail address, domain name or {@code null}
     * "default" type.
     * @param type The value "user", "group", "domain" or "default".
     * @param role The value "owner", "writer" or "reader".
     * @return The inserted permission if successful, {@code null} otherwise.
     */
    public Permission insertPermission(Drive service, String fileId, String value, String type, String role) throws IOException {
        Permission newPermission = new Permission();
        newPermission.setValue(value);
        newPermission.setType(type);
        newPermission.setRole(role);
        return service.permissions().insert(fileId, newPermission).execute();  
    }

    public String retrievePermissions(String fileId) {
        try {
            return service.permissions().list(fileId).execute().toPrettyString();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    public PermissionList retrievePermissionList(String fileId) {
        Random randomGenerator = new Random(); 
        for (int n = 0; n < 10; ++n) {
        try {
            return service.permissions().list(fileId).execute();
        } catch (Exception e) {
            Reporter.log("An error occurred: " + e, true);
            Reporter.log(" ### Exception found in retrievePermissionList : " + e.getLocalizedMessage(), true);
            try {
                // Apply exponential backoff.
                retrievePermissionList(fileId);
                Thread.sleep((1 << n) * 1000 + randomGenerator.nextInt(1001));
            } catch (InterruptedException ex) {
                Reporter.log("Error in Retrieve Permission :"+ex.getLocalizedMessage(),true);
            }
        }
        }
        Reporter.log(" ## There has been an error, the request never succeeded.", true);
        return null;
    }

    /**
     * Patch a permission's role.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update permission for.
     * @param permissionId ID of the permission to patch.
     * @param newRole The value "owner", "writer" or "reader".
     * @return The patched permission if successful, {@code null} otherwise.
     */
    public String patchPermission(String fileId, String permissionId, String newRole) {
        Permission patchedPermission = new Permission();
        patchedPermission.setRole(newRole);
        try {
            return service.permissions().patch(
                    fileId, permissionId, patchedPermission).execute().toPrettyString();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Update a permission's role.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update permission for.
     * @param permissionId ID of the permission to update.
     * @param newRole The value "owner", "writer" or "reader".
     * @return The updated permission if successful, {@code null} otherwise.
     */
    public String updatePermission(String fileId, String permissionId, String newRole) {
        try {
            // First retrieve the permission from the API.
            Permission permission = service.permissions().get(fileId, permissionId).execute();
            permission.setRole(newRole);
            return service.permissions().update(fileId, permissionId, permission).execute().toPrettyString();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }
    
    public Permission updatePermissionOn(String fileId, String permissionId, String newRole) {
        try {
            // First retrieve the permission from the API.
            Permission permission = service.permissions().get(fileId, permissionId).execute();
            permission.setRole(newRole);
            Permission execute = service.permissions().update(fileId, permissionId, permission).execute();
            return execute;
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Print the Permission ID for an email address.
     *
     * @param service Drive API service instance.
     * @param email Email address to retrieve ID for.
     */
    public String printPermissionIdForEmail(Drive service, String email) {
        try {
            PermissionId permissionId
                    = service.permissions().getIdForEmail(email).execute();
            return permissionId.getId();
        } catch (IOException e) {
            Reporter.log("An error occured: " + e, true);
        }
        return null;
    }

    /**
     * Retrieve a list of App resources.
     *
     * @param service Drive API service instance.
     * @return List of App resources.
     */
    public List<App> retrieveAllApps() throws IOException {
        try {
            AppList apps = service.apps().list().execute();
            return apps.getItems();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Print an app's metadata.
     *
     * @param service Drive API service instance.
     * @param appId ID of the file to print metadata for.
     */
    public Map<String, String> printApp(String appId) {
        Map<String, String> appMetadata = new HashMap<>();
        try {
            App app = service.apps().get(appId).execute();
            appMetadata.put("Name", app.getName());
            appMetadata.put("Object type", app.getObjectType());
            appMetadata.put("Product URL", app.getProductUrl());
        } catch (IOException e) {
            Reporter.log("An error occured in Print App : " + e, true);
        }
        return appMetadata;
    }

    /**
     * Remove a custom property.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to remove the property for.
     * @param key ID of the property to remove.
     * @param visibility The type of property ('PUBLIC' or 'PRIVATE').
     */
    public void removeProperty(String fileId, String key, String visibility) {
        try {
            Reporter.log("Deleting the property # fileId :" + fileId + " # Key :" + key + " # visibility :" + visibility, true);
            Delete request = service.properties().delete(fileId, key);
            request.setVisibility(visibility);
            request.execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e);
        }
    }

    /**
     * Print information about the specified custom property.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to print property for.
     * @param key ID of the property to print.
     * @param visibility The type of property ('PUBLIC' or 'PRIVATE').
     */
    public Property printProperty(String fileId, String key, String visibility) {
        try {
            Get request = service.properties().get(fileId, key);
            request.setVisibility(visibility);
            Property property = request.execute();
            Reporter.log("Key: " + property.getKey());
            Reporter.log("Value: " + property.getValue());
            Reporter.log("Visibility: " + property.getVisibility());
            return property;
        } catch (IOException e) {
            Reporter.log("An error occured: " + e, true);
        }
        return null;
    }

    /**
     * Insert a new custom file property.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to insert property for.
     * @param key ID of the property.
     * @param value Property value.
     * @param visibility 'PUBLIC' to make the property visible by all apps, or
     * 'PRIVATE' to make it only available to the app that created it.
     * @return The inserted custom file property if successful, {@code null}
     * otherwise.
     */
    public Property insertProperty(String fileId, String key, String value, String visibility) {
        Property newProperty = new Property();

        newProperty.setKey(key);
        newProperty.setValue(value);
        newProperty.setVisibility(visibility);
        Reporter.log("Inserting Property : # key :" + key + " # value :" + value + " # visibility :" + visibility, true);
        try {
            return service.properties().insert(fileId, newProperty).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Retrieve a list of custom file properties.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to retrieve properties for.
     * @return List of custom properties.
     */
    public List<Property> retrieveProperties(String fileId) {
        try {
            PropertyList properties = service.properties().list(fileId).execute();
            return properties.getItems();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }

        return null;
    }

    /**
     * Patch a custom property's value.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to patch property for.
     * @param key ID of the property to patch.
     * @param newValue The new value for the property.
     * @param visibility The type of property ('PUBLIC' or 'PRIVATE').
     * @return The patched property's if successful, {@code null} otherwise.
     */
    public Property patchProperty(String fileId, String key, String newValue, String visibility) {
        Property patchedProperty = new Property();
        patchedProperty.setValue(newValue);
        try {
            Patch request = service.properties().patch(fileId, key, patchedProperty);
            request.setVisibility(visibility);
            return request.execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Update a custom property's value.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update property for.
     * @param key ID of the property to update.
     * @param newValue The new value for the property.
     * @param visibility The type of property ('PUBLIC' or 'PRIVATE').
     * @return The updated property's if successful, {@code null} otherwise.
     */
    public Property updateProperty(String fileId, String key, String newValue, String visibility) {
        try {
            // First retrieve the property from the API.
            Get request = service.properties().get(fileId, key);
            request.setVisibility(visibility);
            Property property = request.execute();
            property.setValue(newValue);
            Update update = service.properties().update(fileId, key, property);
            update.setVisibility(visibility);
            return update.execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    public PermissionId printPermissionIdForEmail(String email) {
        try {
            PermissionId permissionId = service.permissions().getIdForEmail(email).execute();
            Reporter.log("ID: " + permissionId.getId());
            return permissionId;
        } catch (IOException e) {
            Reporter.log("An error occured: " + e, true);
        }
        return null;
    }

    // Revision Operations...
    /**
     * Retrieve a list of revisions.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to retrieve revisions for.
     * @return List of revisions.
     */
    public List<Revision> retrieveRevisions(
            String fileId) {
        try {
            RevisionList revisions = service.revisions().list(fileId).execute();
            return revisions.getItems();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Remove a revision.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to remove the revision for.
     * @param revisionId ID of the revision to remove.
     */
    public void removeRevision(String fileId,
            String revisionId) {
        try {
            service.revisions().delete(fileId, revisionId).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
    }

    /**
     * Print information about the specified revision.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to print revision for.
     * @param revisionId ID of the revision to print.
     */
    public Revision printRevision(String fileId,
            String revisionId) {
        try {
            Revision revision = service.revisions().get(
                    fileId, revisionId).execute();

            Reporter.log("Revision ID: " + revision.getId(), true);
            Reporter.log("Modified Date: " + revision.getModifiedDate(), true);
            if (revision.getPinned()) {
                Reporter.log("This revision is pinned", true);
            }
            return revision;
        } catch (IOException e) {
            Reporter.log("An error occured: " + e, true);
        }
        return null;
    }

    /**
     * Pin a revision.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update revision for.
     * @param revisionId ID of the revision to pin.
     * @return The patched revision if successful, {@code null} otherwise.
     */
    public Revision pinRevision(String fileId,
            String revisionId) {
        Revision patchedRevision = new Revision();
        patchedRevision.setPinned(true);
        try {
            return service.revisions().patch(
                    fileId, revisionId, patchedRevision).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Pin a revision.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update revision for.
     * @param revisionId ID of the revision to update.
     * @return The updated revision if successful, {@code null} otherwise.
     */
    public Revision updateRevision(String fileId,
            String revisionId) {
        try {
            // First retrieve the revision from the API.
            Revision revision = service.revisions().get(
                    fileId, revisionId).execute();
            revision.setPinned(true);
            return service.revisions().update(
                    fileId, revisionId, revision).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    // ========= Comment Operations =============================
    /**
     * Remove a comment.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to remove the comment for.
     * @param commentId ID of the comment to remove.
     */
    public void removeComment(String fileId,
            String commentId) {
        try {
            Reporter.log("Comment Successfully Removed", true);
            service.comments().delete(fileId, commentId).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
    }

    /**
     * Print information about the specified comment.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to print comment for.
     * @param commentId ID of the comment to print.
     */
    public Comment printComment(String fileId,
            String commentId) {
        try {
            Comment comment = service.comments().get(
                    fileId, commentId).execute();

            Reporter.log("Modified Date: " + comment.getModifiedDate(), true);
            Reporter.log("Author: " + comment.getAuthor(), true);
            Reporter.log("Content: " + comment.getContent(), true);
            return comment;
        } catch (IOException e) {
            Reporter.log("An error occured: " + e, true);
        }
        return null;
    }

    /**
     * Insert a new document-level comment.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to insert comment for.
     * @param content Text content of the comment.
     * @return The inserted comment if successful, {@code null} otherwise.
     */
    public Comment insertComment(String fileId,
            String content) {
        Comment newComment = new Comment();
        newComment.setContent(content);
        try {
            return service.comments().insert(fileId, newComment).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Retrieve a list of comments.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to retrieve comments for.
     * @return List of comments.
     */
    public List<Comment> retrieveComments(
            String fileId) {
        try {
            CommentList comments = service.comments().list(fileId).execute();
            return comments.getItems();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Patch a comment's content.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update comment for.
     * @param commentId ID of the comment to patch.
     * @param newContent The new text content for the comment.
     * @return The patched comment if successful, {@code null} otherwise.
     */
    public Comment patchComment(String fileId,
            String commentId, String newContent) {
        Comment patchedComment = new Comment();
        patchedComment.setContent(newContent);
        try {
            return service.comments().patch(
                    fileId, commentId, patchedComment).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Update a comment's content.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update comment for.
     * @param commentId ID of the comment to update.
     * @param newContent The new text content for the comment.
     * @return The updated comment if successful, {@code null} otherwise.
     */
    public Comment updateComment(String fileId,
            String commentId, String newContent) {
        try {
            // First retrieve the comment from the API.
            Comment comment = service.comments().get(
                    fileId, commentId).execute();
            comment.setContent(newContent);
            return service.comments().update(
                    fileId, commentId, comment).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    //============ Reply Operations====================
    /**
     * Remove a reply.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to remove the reply for.
     * @param commentId ID of the comment to remove the reply for.
     * @param replyId ID of the reply to remove.
     */
    public void removeReply(String fileId,
            String commentId, String replyId) {
        try {
            Reporter.log("Removing Reply.. ", true);
            Void execute = service.replies().delete(fileId, commentId, replyId).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
    }

    /**
     * Print information about the specified reply.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to print reply for.
     * @param commentId ID of the comment to print reply for.
     * @param replyId ID of the reply to print.
     */
    public CommentReply printReply(String fileId,
            String commentId, String replyId) {
        try {
            CommentReply reply = service.replies().get(
                    fileId, commentId, replyId).execute();

            Reporter.log("Modified Date: " + reply.getModifiedDate(), true);
            Reporter.log("Author: " + reply.getAuthor(), true);
            Reporter.log("Content: " + reply.getContent(), true);
            return reply;
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Insert a new reply to a comment.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to insert reply for.
     * @param commentId ID of the comment to insert reply for.
     * @param content Text content of the reply.
     * @return The inserted reply if successful, {@code null} otherwise.
     */
    public CommentReply insertReply(String fileId,
            String commentId, String content) {
        CommentReply newReply = new CommentReply();
        newReply.setContent(content);
        try {
            return service.replies().insert(fileId, commentId, newReply).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Retrieve a list of replies.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to retrieve replies for.
     * @param commentId ID of the comment to retrieve replies for.
     * @return List of replies.
     */
    public List<CommentReply> retrieveReplies(
            String fileId, String commentId) {
        try {
            CommentReplyList replies = service.replies().list(fileId, commentId).execute();
            return replies.getItems();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Patch a reply's content.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update reply for.
     * @param commentId ID of the comment to update reply for.
     * @param replyId ID of the reply to patch.
     * @param newContent The new text content for the reply.
     * @return The patched reply if successful, {@code null} otherwise.
     */
    public CommentReply patchReply(String fileId,
            String commentId, String replyId, String newContent) {
        CommentReply patchedReply = new CommentReply();
        patchedReply.setContent(newContent);
        try {
            return service.replies().patch(
                    fileId, commentId, replyId, patchedReply).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }

    /**
     * Update a reply's content.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to update reply for.
     * @param commentId ID of the comment to update reply for.
     * @param replyId ID of the reply to update.
     * @param newContent The new text content for the reply.
     * @return The updated reply if successful, {@code null} otherwise.
     */
    public CommentReply updateReply(String fileId,
            String commentId, String replyId, String newContent) {
        try {
            // First retrieve the reply from the API.
            CommentReply reply = service.replies().get(
                    fileId, commentId, replyId).execute();
            reply.setContent(newContent);
            return service.replies().update(
                    fileId, commentId, replyId, reply).execute();
        } catch (IOException e) {
            Reporter.log("An error occurred: " + e, true);
        }
        return null;
    }
    
    public String getUsername(){
        Map<String, String> printAbout = printAbout();
        return printAbout.get("name");    
    }
  
  public String getEmailId() throws InterruptedException {
    try { 
        About about = service.about().get().execute();
        User user = about.getUser();
        return user.getEmailAddress();
    } catch (IOException e) {
      System.out.println("An error occured: " + e);
    }
        return null;
  }
  
    public void trashRootFolderItems(String[] patterns) throws IOException, InterruptedException {
        List<File> listFile = listFile();
        int i=1;
        for (File file : listFile) {
            String fileID = file.getId();
            String fileName = file.getTitle();
            Reporter.log("======|| Below Files/Folder Trashed From the Root Folder || Email ID :" + getEmailId(), true);
            for (String pattern : patterns) {
                if (pattern.length() < 3) {
                    Reporter.log("Pattern :" + pattern + " length should be greater than 3", true);
                    Reporter.log("Files/Folder Not Trashed From the ROOT Foldr", true);
                } else {
                    if (fileName.contains(pattern)) {
                        Thread.sleep(500);
                        Reporter.log(i++ +") Pattern Matched <"+pattern+"> Trash Action on File/Folder... :"+fileName,true);
                        trashFile(fileID);          
                    }
                }
            }
        }
        i=i-1;
        Reporter.log("$$ Total Files/Folder Deleted from Root Folder <"+getEmailId()+"> :"+ i, true);  
    }
    public void trashRootFolderItems(String pattern) throws IOException {
        if (pattern.length() < 3) {
            Reporter.log("Pattern :" + pattern + " length should be greater than 3", true);
            Reporter.log("Files/Folder Not Trashed From the ROOT Foldr", true);
        }
        List<String> filesDeleted=new ArrayList();
        List<File> listFile = listFile();
        for (File file : listFile) {
            String fileID = file.getId();
            String fileName = file.getTitle();
            if (fileName.contains(pattern)) {
                trashFile(fileID);
                filesDeleted.add(fileName);
            } 
        }
        Reporter.log("====== Below Files/Folder Trashed From the Root Folder ======",true);
        int i=0;
        for (String fileDeleted : filesDeleted) {
            Reporter.log(++i +")"+fileDeleted,true);
        }
    }
    
  //=================== Properties Operations ===========================================
    public static void main(String[] args) throws IOException, GeneralSecurityException, InterruptedException {
        
        // File Sharing to External Domain....
        
        String externalDomain="securletbeatle.com";
        String ClientId="531581515230-0k9p8gne8a0uh116u4fff7j93o7nsdu1.apps.googleusercontent.com";
        String ClientSecret="EQUZEc3QoEa0EqBzHVpoMLgr";
        String refreshToken="1/eckCHbIndshTd6zxoq5Ds13OqYondPtDxMNV2BUIzGhIgOrJDtdun6zK6XiATCKT";
        String accessToken=new GDriveAuthorization(ClientId, ClientSecret).getAceessTokenFromRefreshAccessToken(refreshToken);
        
        GDrive gd=new GDrive(accessToken);
      
        
        
     
    }

    
}
