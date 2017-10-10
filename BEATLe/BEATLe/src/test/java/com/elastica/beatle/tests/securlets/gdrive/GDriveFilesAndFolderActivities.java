
package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.dci.DCIConstants;
import com.universal.common.GDrive;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class GDriveFilesAndFolderActivities extends GDriveUtils{
    
    String from_jodaTime;
    String jsonResponse; 
    String SaaSAppUser;
    GDrive gDrive;
    
    @BeforeClass
    public void initialSetUp(){
        SaaSAppUser=getRegressionSpecificSuitParameters("SaaSAppUser");
        if(SaaSAppUser.equals("admin")){
           gDrive=gDriveAdmin; 
        }
        else{
            gDrive=gDriveInternalUser;
        }     
    }
     
    @Test(groups={"FOLDER", "P1"})
    public void performFolderOperations() throws InterruptedException, IOException, Exception {
        
        from_jodaTime = getCurrentJodaTime();
        wait(globalWaitTime, "Waiting Before Performing Folder Operation .....");
        Reporter.log("================= GDRIVE SaaS Actions : FOLDER ACTIVITIES  =====================", true);
        String folderIdtemporary = gDrive.createFolder("QATesting"); // Creating Folder....
        wait(globalWaitTime, "Waiting Before Folder Trash ......");
        gDrive.trashFile(folderIdtemporary);
        wait(globalWaitTime, "Waiting Before Folder Restore ......");
        gDrive.restoreFile(folderIdtemporary);
        wait(globalWaitTime, "Waiting Before Folder Rename ......");
        gDrive.renameFile(folderIdtemporary, "QATestingAfterRename");
        wait(globalWaitTime, "Waiting Before Folder Permanent Delete .....");
        gDrive.deleteFile(folderIdtemporary);
        Reporter.log("Folder Permanent Delete Completed....", true);
        wait(globalWaitTime, "Waiting After SaaS App Operations ....");
        this.jsonResponse=getDisplayLogResponse("Waiting for ES logs after performing Folder Operations...",from_jodaTime);
    }
 
    @Test(groups={"FOLDER", "P1"} ,dataProviderClass = GDriveDataProvider.class,dataProvider = "data-provider-FolderOperations")
    public void validateFolderOperations(String testName, String message) throws Exception {
        Map expectedResult = new HashMap();
        expectedResult.put("message", message);
        RawJsonParser.LogValidator(this.jsonResponse,expectedResult,"$.hits.hits[*].source","message");
    }

    String actualFileNameToUpload = "glba-test.txt";
    String uniqueFolderName = uniqueIdentifier + "_QATest";
    String uniqueTmpFolderName = uniqueIdentifier + "_QATempTest";
    String uniqueFileNameToUpload = uniqueIdentifier + "_"+actualFileNameToUpload;
    String uniqueFileNameToCopy = uniqueIdentifier + "_copyFile.txt";
    String uniqueFileNameToMove = uniqueIdentifier + "_moveFile.txt";
    String uniqueFileNameToRename = uniqueIdentifier + "_renameFile.txt";
    String uniqueFileNameToUpdate = uniqueIdentifier + "_fileUpdate.txt";
    String localFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + actualFileNameToUpload;
    String folderId;
    String tmpFolderId;
    String fileId;
    
    String uniqueFileCreation = uniqueIdentifier + "_fileCreate.ppt";
    @Test(groups={"FILE", "P1"})
    public void performFileOperations() throws Exception {
        from_jodaTime = getCurrentJodaTime();
        Reporter.log(" ## Creating Temporary Folder..." + uniqueTmpFolderName, true);
        tmpFolderId = gDrive.createFolder(uniqueTmpFolderName);
        Reporter.log("================= GDRIVE SaaS Actions : FILE CREATE  =====================", true);
        Reporter.log(" ## Creating Different Types of Files in Temporary Folder :" + uniqueTmpFolderName, true);
        gDrive.createFile("QATestPPT", "Some Desc", tmpFolderId, "application/vnd.google-apps.presentation", "QATestPPT");
        gDrive.createFile("QATest.xls", "Some Desc", tmpFolderId, "application/vnd.google-apps.spreadsheet", "QATest.xls");
        gDrive.createFile("QATest.jpg", "Some Desc", tmpFolderId, "application/vnd.google-apps.drawing", "QATest.jpg");
        gDrive.createFile("QATestForm", "Some Desc", tmpFolderId, "application/vnd.google-apps.form", "QATestForm");
        gDrive.createFile("QATestDocument", "Some Desc", tmpFolderId, "application/vnd.google-apps.document", "QATestDocument");
        wait(globalWaitTime, "## Waiting for 10 secs after file Create..## ");
        Reporter.log("================= GDRIVE SaaS Actions : FILE UPLOAD  =====================", true);
        folderId = createFolder(uniqueFolderName,gDrive); // Creating Folder....
        fileId = gDrive.uploadFile(folderId, localFileLocation, uniqueFileNameToUpload).getFileId();
        Assert.assertEquals(fileId != null, true);
        wait(globalWaitTime,"Waiting Before File Trash...");
        Reporter.log("================= GDRIVE SaaS Actions : FILE TRASH  =====================", true);
        String trashFile = gDrive.trashFile(fileId);
        Reporter.log("## Trash File Response :" + trashFile, true);
        Assert.assertEquals(trashFile != null, true);
        wait(globalWaitTime,"Waiting Before File Rstore....");
        Reporter.log("================= GDRIVE SaaS Actions : FILE RESTORE  =====================", true);
        String restoreFileResponse = gDrive.restoreFile(fileId);
        System.out.println("## File Successfully Restored : " + restoreFileResponse);
        Assert.assertEquals(restoreFileResponse != null, true);
        wait(globalWaitTime,"Waiting Before File COPY...");
        Reporter.log("================= GDRIVE SaaS Actions : FILE COPY  =====================", true);
        tmpFolderId = gDrive.createFolder(uniqueTmpFolderName);
        Reporter.log("Creating Tmp Folder <" + uniqueTmpFolderName + ">..Tmp Folder Id :" + tmpFolderId, true);
        Reporter.log("File Copy to Tmp Folder.....", true);
        gDrive.insertFileIntoFolder(fileId, tmpFolderId); // Copy File...
        wait(globalWaitTime,"Waiting Before File Rename....");
        Reporter.log("================= GDRIVE SaaS Actions : FILE RENAME  =====================", true);
        com.google.api.services.drive.model.File renameFile = gDrive.renameFile(fileId, uniqueFileNameToRename);
        Reporter.log("File :" + localFileLocation + " ## File Renamed In Cloud As :" + uniqueFileNameToRename, true);
        Assert.assertEquals(uniqueFileNameToRename, renameFile.getTitle());
        wait(globalWaitTime,"Waiting Before File MOve....");
        Reporter.log("================= GDRIVE SaaS Actions : FILE MOVE  =====================", true);
        gDrive.moveFileToFolder(fileId, tmpFolderId);
        Reporter.log("================= GDRIVE SaaS Actions : FILE TOUCH  =====================", true);
        com.google.api.services.drive.model.File fileTouched = gDrive.updateModifiedDate(fileId); // File Touch..
        Reporter.log(" ## File Touch Response :" + fileTouched.toPrettyString(), true);
        wait(globalWaitTime,"Waiting Before File MetaDataUpdate...");
        Reporter.log("================= GDRIVE SaaS Actions : FILE METADATA UPDATE  =====================", true);
        com.google.api.services.drive.model.File updateFile = gDrive.updateFile(fileId, uniqueFileNameToUpdate, "QA Automation", null, localFileLocation, false);
        Reporter.log(" ## Update File Response :" + updateFile.toPrettyString(), true);
        Assert.assertEquals(uniqueFileNameToUpdate, updateFile.getTitle());
        wait(globalWaitTime,"Waiting Before File Permanent Delete....");
        Reporter.log("================= GDRIVE SaaS Actions : FILE PERMANENT DELETE  =====================", true);
        Reporter.log("Deleting the File :..." + fileId, true);
        universalApi.deleteFile(fileId, null);
        Reporter.log("File Successfully Deleted:" + fileId, true);
        Reporter.log("================= GDRIVE SaaS Actions : <T4243798> Delete the Publically shared file and see if the records do not appear on UI. ==================",true);
        String publicSharedFileId = gDrive.uploadFile(folderId, localFileLocation, "FileSharedPublic").getFileId();
        gDrive.insertPermission(gDrive.getDriveService(), publicSharedFileId, "null", "anyone", "writer"); // Role : writer,reader
        wait(globalWaitTime,"Waiting Before File Delete< Public Shared File > ....");
        gDrive.deleteFile(publicSharedFileId);
        wait(globalWaitTime,"Waiting After File Activities....");
        this.jsonResponse=getDisplayLogResponse("Waiting for ES logs after performing Folder Operations...",from_jodaTime);
        System.out.println("===>Response==>"+this.jsonResponse);
    }

    
    @DataProvider(name = "data-provider-FileOperations")
    public Object[][] dataProviderMethodFileOperations() {
        this.dataProvider.updatedata("GDriveFileActivities", "uniqueFolderName", uniqueFolderName);
        this.dataProvider.updatedata("GDriveFileActivities", "uniqueTmpFolderName", uniqueTmpFolderName);
        this.dataProvider.updatedata("GDriveFileActivities", "uniqueFileNameToUpload", uniqueFileNameToUpload);
        this.dataProvider.updatedata("GDriveFileActivities", "uniqueFileNameToCopy", uniqueFileNameToCopy);
        this.dataProvider.updatedata("GDriveFileActivities", "uniqueFileNameToMove", uniqueFileNameToMove);
        this.dataProvider.updatedata("GDriveFileActivities", "uniqueFileNameToRename", uniqueFileNameToRename);
        this.dataProvider.updatedata("GDriveFileActivities", "uniqueFileNameToUpdate", uniqueFileNameToUpdate);
        this.dataProvider.updatedata("GDriveFileActivities", "USERNAME", gDrive.getUsername());
        return this.dataProvider.getData("GDriveFileActivities", "Test Name", "Message","Activity Type","Facility","Object Type");
    }

    @Test(groups={"FILE", "P1"} ,dataProvider = "data-provider-FileOperations")
    public void validateFileOperations(String testName, String message,String activityType,String facility,String objectType) throws Exception {
        Map expectedResult = new HashMap();
        expectedResult.put("message", message);
        expectedResult.put("Activity_type", activityType);
        expectedResult.put("facility", facility);
        expectedResult.put("Object_type", objectType);
        RawJsonParser.LogValidator(this.jsonResponse,expectedResult,"$.hits.hits[*].source","message");
    }   
}
