package com.universal.common;

import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxWriteMode;
import com.google.gson.Gson;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.dropbox.DropboxAccountInfo;
import com.universal.dtos.dropbox.DropboxDownloadFileInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import com.dropbox.core.*;
import com.dropbox.core.v2.*;
import com.dropbox.core.v2.DbxFiles.Metadata;
import com.dropbox.core.v2.DbxSharing.AddMember;
import com.dropbox.core.v2.DbxSharing.MemberSelector;
import com.dropbox.core.v2.DbxSharing.SharedFolderMetadata;
import com.universal.dtos.UserAccount;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsfr.json.JsonSurfer;
import org.testng.Reporter;

//https://www.dropbox.com/help/6271
public class DropBox {

    DbxClientV2 dbx_client;
    int maxRetryCount = 10;
    long waitTime = 180000; // 30 secs...
    int retryCounter = 0;
    JsonSurfer surfer = JsonSurfer.simple();

    public DropBox(DbxClientV2 dbx_client) throws Exception {
        super();
        this.dbx_client = dbx_client;
    }
    
    public DbxClientV2 getDbxClientV2(){
        return this.dbx_client;
    }
    
    public <T> T getFile(String fileName) {
        Reporter.log("File Name :"+ fileName);
        Reporter.log("Downloading to the Current location in ur System....");
        return downloadFile(fileName, "/");
    }

    public <T> T getFolder(String folderId) {
        // TODO Auto-generated method stub
        return null;
    }


    public void permanentDelete(String filePath){
        try {
            dbx_client.files.permanentlyDelete(filePath);
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public DbxFiles.FileMetadata restore(String filePath,String restoreId) throws DbxException{ 
        DbxFiles.FileMetadata restoreFile = dbx_client.files.restore(filePath, restoreId);
        return restoreFile;
    }
    
    public FileUploadResponse uploadFile(String folderlocationInCloud, String localfilelocation,String destinationFileName) {
        Reporter.log("## File Upload in Progress...");
        File inputFile = new File(localfilelocation);
        //String file_Name = destinationFileName;
        Reporter.log("Local Path Location... :" + localfilelocation);
        Reporter.log("Cloud Path Location... :" + folderlocationInCloud + "/" + destinationFileName);
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        try (FileInputStream inputStream = new FileInputStream(localfilelocation)) {
        	DbxFiles.FileMetadata uploadedFile = null;	
        	try {
                    uploadedFile = dbx_client.files.uploadBuilder(folderlocationInCloud + "/" + destinationFileName).run(inputStream);
				       
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Pojo Update.....    
            Map<String, String> map = new HashMap<>();
            String responseMessage = convertToJson(map);
            fileUploadResponse.setFileId(uploadedFile.id);
            fileUploadResponse.setFileName(uploadedFile.name);
            fileUploadResponse.setResponseMessage(responseMessage);
            fileUploadResponse.setResponseCode(200);
            Reporter.log("## Uploaded: " + uploadedFile.toString(),true);
        } catch (FileNotFoundException ex) {
            Reporter.log("Dropbox File Upload ... File Not Found :" + ex.getLocalizedMessage(),true);
        } catch (IOException ex) {
            Reporter.log("Dropbox File Upload ... IO Exception :" + ex.getLocalizedMessage(),true);
        }
        return fileUploadResponse;
    }

    
    
    
    public FileUploadResponse uploadFile(String folderlocationInCloud, String localfilelocation) {
        Reporter.log("## File Upload in Progress...");
        File inputFile = new File(localfilelocation);
        String file_Name = inputFile.getName();
        Reporter.log("Local Path Location... :" + localfilelocation);
        Reporter.log("Cloud Path Location... :" + folderlocationInCloud + "/" + file_Name);
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        try (FileInputStream inputStream = new FileInputStream(localfilelocation)) {
            
        	DbxFiles.FileMetadata uploadedFile = null;
			
                uploadedFile = dbx_client.files.uploadBuilder(folderlocationInCloud + "/" + file_Name).run(inputStream);
            // Pojo Update.....    
            Map<String, String> map = new HashMap<>();
            map.put("humanSize", uploadedFile.size+"");
//            map.put("iconName", uploadedFile.mediaInfo.toString());
            map.put("fileName", uploadedFile.name);
            map.put("revision", uploadedFile.rev);
            map.put("clientMtime", uploadedFile.clientModified.toString());
            map.put("lastModified", uploadedFile.serverModified.toString());
       
            String responseMessage = convertToJson(map);
            fileUploadResponse.setFileId(uploadedFile.id);
            fileUploadResponse.setFileName(localfilelocation);
            fileUploadResponse.setResponseMessage(responseMessage);
            fileUploadResponse.setResponseCode(200);
            Reporter.log("## Uploaded: " + uploadedFile.toString(),true);
        } catch (FileNotFoundException ex) {
            Reporter.log("Dropbox File Upload ... File Not Found :" + ex.getLocalizedMessage(),true);
        } catch (IOException ex) {
            Reporter.log("Dropbox File Upload ... IO Exception :" + ex.getLocalizedMessage(),true);
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileUploadResponse;
    }

    public String convertToJson(Map<String, String> map) {
        Gson gson = new Gson();
        String responseMessage = gson.toJson(map).toString();
        return responseMessage;
    }

    public <T> T downloadFile(String fileId, String targetFilename) {
        Reporter.log("### File Download .." + targetFilename + ".. Download in progress........");
        Reporter.log("Local Path Location :" + fileId);
        Reporter.log("Cloud Path Location :" + targetFilename);
        DropboxDownloadFileInfo dropboxDownloadFileInfo = new DropboxDownloadFileInfo();
        String FileName = targetFilename.substring(targetFilename.lastIndexOf("/") + 1, targetFilename.length());
        // DropboxDownloadFileInfo downloadFileInfo=new DropboxDownloadFileInfo();
        try (FileOutputStream outputStream = new FileOutputStream(FileName)) {
            DbxFiles.FileMetadata downloadedFile = dbx_client.files.downloadBuilder(targetFilename).run(outputStream);
            Reporter.log("MetadataInfo_downloadedFile: " + downloadedFile.toString());
            //Pojo Update....
            dropboxDownloadFileInfo.setClientMtime(downloadedFile.toString());
            dropboxDownloadFileInfo.setHumanSize(downloadedFile.size+"");
            dropboxDownloadFileInfo.setMightHaveThumbnail(downloadedFile.mediaInfo.toString());
            dropboxDownloadFileInfo.setLastModified(downloadedFile.clientModified.toString());
            dropboxDownloadFileInfo.setRev(downloadedFile.rev);
        } catch (FileNotFoundException ex) {
            Reporter.log("Dropbox File Download ... File Not Found :" + ex.getLocalizedMessage());
        } catch (IOException ex) {
            Reporter.log("Dropbox File Download ... IO Exception :" + ex.getLocalizedMessage());
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) dropboxDownloadFileInfo;
    }

    public DropboxAccountInfo getLinkedAccount() throws DbxException {
        DropboxAccountInfo dropboxAccountInfo = new DropboxAccountInfo();
        dropboxAccountInfo.setDisplayName(dbx_client.users.getCurrentAccount().email);
        dropboxAccountInfo.setCountry(dbx_client.users.getCurrentAccount().country);
        dropboxAccountInfo.setReferralLink(dbx_client.users.getCurrentAccount().referralLink);
        dropboxAccountInfo.setUserId(String.valueOf(dbx_client.users.getCurrentAccount().accountId));
        Reporter.log("Dropbox Account Details :" + dropboxAccountInfo.toString());
        return dropboxAccountInfo;
    }
    
    public <T> T renameFile(String cloudSourceFileLocation,String newFileName) throws DbxException{
        String destinationFileLocationInCloud=cloudSourceFileLocation.substring(0,cloudSourceFileLocation.lastIndexOf("/")+1)+newFileName;
        Reporter.log("Old File Name :"+cloudSourceFileLocation);
        Reporter.log("New File Name :"+destinationFileLocationInCloud);
        moveFile(cloudSourceFileLocation,destinationFileLocationInCloud);
        return (T) "File Rename Done";
    }
    
    public DbxFiles.Metadata getFolderInfo(String cloudFolderLocation){
        DbxFiles.Metadata asFolder=null;
        try {
            DbxFiles.Metadata metadata = dbx_client.files.getMetadata(cloudFolderLocation, true);
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return asFolder;   
    }
    
    public void deleteFileOrFolder(String cloudLocation){
        try {
            DbxFiles.Metadata delete = dbx_client.files.delete(cloudLocation);
        } catch (DbxException ex) {
            Reporter.log("File/Folder :"+cloudLocation +" not Found in cloud",true);
        }
        Reporter.log("Folder/File Deleted in the Cloud :"+cloudLocation);
    }
    
    public <T> T copyFolder(String cloudFolderLocation,String cloudNewFolderLocation){
        try {
            DbxFiles.Metadata copy = dbx_client.files.copy(cloudFolderLocation, cloudNewFolderLocation);
            return (T) copy;
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public <T> T copyFile(String cloudFileLocation,String cloudNewFolderLocation){
        try {
            DbxFiles.Metadata copy = dbx_client.files.copy(cloudFileLocation, cloudNewFolderLocation);
            return (T) copy;
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public <T> T createFolder(String cloudLocation){
        DbxFiles.FolderMetadata createFolder = null;
        try {
            createFolder = dbx_client.files.createFolder(cloudLocation);
        } catch (Exception ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) createFolder;     
    }
    
    public <T> T createFolder(String folderId, String parentFolderId){
        DbxFiles.FolderMetadata createFolder = null;
         try {
            createFolder = dbx_client.files.createFolder(parentFolderId+"/"+folderId);
        } catch (Exception ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
         return (T) createFolder;     
    }
    
    public <T> T createSharedLinkForFolderORFile(String folderId){
        DbxSharing.PathLinkMetadata createShareableUrl=null;
        try {
            createShareableUrl = dbx_client.sharing.createSharedLink(folderId);
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) createShareableUrl.url;  
    }
    
    public void transferFolderOwnerShip(String cloudFolderLocation,String accessTokenForExternalUser) throws DbxException, Exception{
        String shareFolderID = getShareFolderId(cloudFolderLocation);
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("Elastica", Locale.getDefault().toString()); 
        DbxClientV2 dbx_client_external = new DbxClientV2(dbxRequestConfig,accessTokenForExternalUser);
        DropBox dropBox=new DropBox(dbx_client_external);
        DropboxAccountInfo linkedAccount = dropBox.getLinkedAccount();
        String userId = linkedAccount.getUserId();
        dbx_client.sharing.transferFolder(shareFolderID, userId);
    }
    
    public String getRootFolderItems(String searchPath,String query) throws DbxException{
        DbxFiles.SearchResult listFolder = dbx_client.files.search(searchPath, query);
        String itemList=listFolder.toJson(Boolean.TRUE);
        Reporter.log("## Search File/Folder Result : \n"+itemList,true);
        return itemList;
    }
    
    public void deleteFileOrFolder(String searchPath,String query) throws DbxException, InterruptedException{
        Reporter.log("##Search Location :"+searchPath +" ##Search Query Param :"+query,true);
        String jsonResponse = getRootFolderItems(searchPath,query).replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        Collection<Object> fetchMultipleFields = surfer.collectAll(jsonResponse,"$.matches[*].metadata.name");
        Object[] toArray = fetchMultipleFields.toArray();
        int i=0;
        for (Object fetchMultipleField : toArray) {
           // Thread.sleep(5000);
            Reporter.log(++i +">File/Folder Deleted from <"+searchPath+">:"+fetchMultipleField.toString(),true); 
            deleteFileOrFolder("/"+fetchMultipleField.toString());
        }
    }
      
    @SuppressWarnings("unchecked")
    public <T> T getFoldersItems(String cloudLocation) {
        HashMap<String, String> map = new HashMap<>();
        try {
            DbxFiles.ListFolderBuilder listFolderBuilder = dbx_client.files.listFolderBuilder(cloudLocation);
            DbxFiles.ListFolderResult start = listFolderBuilder.start();
            Reporter.log("#### Files in the path ##### : "+cloudLocation);
            for (Iterator<Metadata> iterator = start.entries.listIterator(); iterator.hasNext();) {
                Metadata next = iterator.next();
                map.put(next.name, next.pathLower);  
            }    
        } catch (Exception e) {
            Reporter.log("### Dropbox .. Issue with listing files in root Dir.." + e.getLocalizedMessage());
        }
        return (T) map;
    }
    
    public <T> T getFileInfo(String cloudFileLocation){
        DbxFiles.Metadata metadata1 = null;
        try {
            metadata1 = dbx_client.files.getMetadata(cloudFileLocation);
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) metadata1;
    }
    
    public <T> T moveFile(String CloudfileLocation,String cloudFileLocation) {
        DbxFiles.Metadata move = null;
        try {
            move = dbx_client.files.move(CloudfileLocation, cloudFileLocation);
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) move;
    } 
    
    public <T> T moveFolder(String CloudfolderLocation,String cloudFolderLocation) {  
        DbxFiles.Metadata move = null;
        try {
            move = dbx_client.files.move(CloudfolderLocation, cloudFolderLocation);
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) move;
    } 
    
    public SharedFolderMetadata mountSharedFolder(String sharedFileOrFolderId,String accessToken) throws DbxException{
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("Elastica", Locale.getDefault().toString()); 
        DbxClientV2 dbx_client_external = new DbxClientV2(dbxRequestConfig,accessToken);
        return dbx_client_external.sharing.mountFolder(sharedFileOrFolderId);
    }
    
    public void shareAndMountFolderToUser(String cloudLocation, String emailId, DbxSharing.AccessLevel accessLevel, String accessToken) throws IOException, DbxException, InterruptedException {
        AddMember addMember = new AddMember(DbxSharing.MemberSelector.email(emailId), accessLevel);
        ArrayList<DbxSharing.AddMember> memberList = new ArrayList();
        memberList.add(addMember);
        String shareFolderID = getShareFolderId(cloudLocation);
        dbx_client.sharing.addFolderMember(shareFolderID, memberList);
        mountSharedFolder(shareFolderID, accessToken);
    }
    
    public Map<String,String> shareAndMountFolder(String cloudLocation, String emailId, DbxSharing.AccessLevel accessLevel, String accessToken) 
    		throws IOException, DbxException, InterruptedException {
        AddMember addMember = new AddMember(DbxSharing.MemberSelector.email(emailId), accessLevel);
        ArrayList<DbxSharing.AddMember> memberList = new ArrayList();
        memberList.add(addMember);
        String shareFolderId = getShareFolderId(cloudLocation);
        dbx_client.sharing.addFolderMember(shareFolderId, memberList);
        SharedFolderMetadata sharedMeta = mountSharedFolder(shareFolderId, accessToken);
        Map<String, String> folderInfo=new HashMap<String,String>();
        folderInfo.put("folderId", shareFolderId);
		return folderInfo;
    }
    
    public void updateFolderShare(String cloudLocation, String emailId, DbxSharing.AccessLevel accessLevel) throws DbxException{ 
        String shareFolderID = getShareFolderId(cloudLocation);
        DbxSharing.SharedFolderMetadata updateFolderPolicy = dbx_client.sharing.updateFolderPolicy(shareFolderID);
        dbx_client.sharing.updateFolderMember(updateFolderPolicy.sharedFolderId, MemberSelector.email(emailId), accessLevel);       
    }
    
    
    
    public void shareFolderToUser(String cloudLocation, String emailId, DbxSharing.AccessLevel accessLevel) throws IOException, DbxException, InterruptedException {
        AddMember addMember = new AddMember(DbxSharing.MemberSelector.email(emailId), accessLevel);
        addMember = new AddMember(DbxSharing.MemberSelector.email(emailId), accessLevel);
        ArrayList<DbxSharing.AddMember> memberList=new ArrayList();
        memberList.add(addMember);   
        String shareFolderID = getShareFolderId(cloudLocation);
        dbx_client.sharing.addFolderMember(shareFolderID,memberList);   
    }
    
    public void unshareFolder(String cloudLocation){
        String shareFolderID = getShareFolderId(cloudLocation);
        try {
            dbx_client.sharing.unshareFolder(shareFolderID, false);
        } catch (DbxException ex) {  
            Reporter.log("Folder Unshared Issue :"+ex.getLocalizedMessage(),true);
        }
        Reporter.log("Folder Unshared :"+cloudLocation,true);
    }
    
    public void removeFolderMember(String cloudLocation, String emailId) throws DbxException{
        String shareFolderID = getShareFolderId(cloudLocation);
        DbxSharing.SharedFolderMetadata updateFolderPolicy = dbx_client.sharing.updateFolderPolicy(shareFolderID);
        dbx_client.sharing.removeFolderMember(updateFolderPolicy.sharedFolderId, MemberSelector.email(emailId), false);
    }
    
    public void revokeSharedLinkFolder(String cloudLocation) throws DbxException{
        String shareFolderID = getShareFolderId(cloudLocation);
        dbx_client.sharing.revokeSharedLink(shareFolderID);
    }
    
    public void relinquishSharedFolder(String cloudLocation,String accessToken) throws DbxException{
        String shareFolderID = getShareFolderId(cloudLocation);
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("Elastica", Locale.getDefault().toString()); 
        DbxClientV2 dbx_client_external = new DbxClientV2(dbxRequestConfig,accessToken);
        dbx_client_external.sharing.relinquishFolderMembership(shareFolderID);
    }
    
    public String shareFolder(String cloudFolderLocation){
        try {
            DbxSharing.ShareFolderLaunch shareFolder = dbx_client.sharing.shareFolder(cloudFolderLocation);
            DbxSharing.SharedFolderMetadata sharedFolderMetadata = shareFolder.getComplete();
            return sharedFolderMetadata.sharedFolderId;    
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getShareFolderId(String cloudFolderLocation){  
        Metadata metadata = null;
        String tempFolder=cloudFolderLocation+File.separator+"temp";
        try {
            dbx_client.files.createFolder(tempFolder);
            metadata = dbx_client.files.getMetadata(tempFolder, true);
            deleteFileOrFolder(tempFolder);
            if(metadata.parentSharedFolderId==null){
                String shareFolderId = shareFolder(cloudFolderLocation);
                System.out.println("Shared Folder ID :"+shareFolderId);
                return shareFolderId;
            }    
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        System.out.println("Shared Folder ID :"+metadata.parentSharedFolderId);
        return metadata.parentSharedFolderId;
    }
    
    public String getParentShareFolderId(String cloudFileLocation){  
        Metadata metadata = null;
        try {
            metadata = dbx_client.files.getMetadata(cloudFileLocation, true);
            System.out.println("Parent Shared Folder ID :"+metadata.parentSharedFolderId);
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return metadata.parentSharedFolderId;
    }
    
    public static String sentPost(String url, Map<String, String> headers, byte[] payLoad) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        Reporter.log("--- Elastic Rest Call ---");
        HttpPost request = new HttpPost(url);
        
        HttpEntity httpEntity = new ByteArrayEntity(payLoad);
        request.setEntity(httpEntity);
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            request.setHeader(key, value);
        }
        HttpResponse response = client.execute(request);

        String postResponse = EntityUtils.toString(response.getEntity(), "utf-8");

        Reporter.log(" ## POST RESPONSE :" + postResponse);
        return postResponse;

    }
    


    
    public static String sentPost(String url, Map<String, String> headers, String payLoad) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        Reporter.log("--- Elastic Rest Call ---");
        HttpPost request = new HttpPost(url);
        
        HttpEntity httpEntity = new StringEntity(payLoad);
        request.setEntity(httpEntity);
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            request.setHeader(key, value);
        }
        HttpResponse response = client.execute(request);

        String postResponse = EntityUtils.toString(response.getEntity(), "utf-8");

        Reporter.log(" ## POST RESPONSE :" + postResponse);
        return postResponse;

    }
     public static String sentPost(String url, Map<String, String> headers) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
       // System.out.println("--- Elastic Rest Call ---");
        HttpPost request = new HttpPost(url);
        
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            request.setHeader(key, value);
        }
        HttpResponse response = client.execute(request);

        String postResponse = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("Closing the connection...........");
        client.close();
        //System.out.println(" ## POST RESPONSE :" + postResponse);
        return postResponse;

    }
     
     public static void main(String[] args) throws Exception {

         String accessToken = "R2VaTghjreAAAAAAAAAACLC6oj6NaOAhJFEtiu-GxlkGFm_PXYZnJzYW0hEIERP5";
         UserAccount account = new UserAccount(accessToken);
         account.setUsername("rahulsky.java@gmail.com");

         UniversalApi universalApi = new UniversalApi("DROPBOX", account);

         String cloudFolderLocation = "/RahulQATest";
         DropBox dropbox = universalApi.getDropbox();
         // Create Folder and Upload a File...
         dropbox.uploadFile(cloudFolderLocation, "/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/UniversalAPI/src/main/java/com/universal/common/CommonTest.java", "hello.java");
         // Create Temp Folder...
         String tempFolder=cloudFolderLocation+"Temp";
         dropbox.createFolder(tempFolder);
         // Copy File To Temp Folder
         dropbox.copyFile(cloudFolderLocation+"/hello.java",tempFolder+"/CopyFile.java" );
         dropbox.moveFile(cloudFolderLocation+"/hello.java",tempFolder+"/MoveFile.java" );
         dropbox.copyFolder("/RahulQATest",tempFolder+"/RahulQATestCopyFolder");
         dropbox.moveFolder("/RahulQATest",tempFolder+"/RahulQATest");// Move Folder..
         dropbox.deleteFileOrFolder(tempFolder+"/MoveFile.java");
         dropbox.deleteFileOrFolder(tempFolder+"/RahulQATest");
         dropbox.createSharedLinkForFolderORFile(tempFolder+"/CopyFile.java");
         Thread.sleep(20000);
         dropbox.deleteFileOrFolder(tempFolder);
         
        
         
    }
    

}
