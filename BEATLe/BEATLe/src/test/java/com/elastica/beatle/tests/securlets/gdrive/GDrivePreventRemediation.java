
package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.securlets.LogUtils;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 *
 * Remediation GDrive New Features... Prevent External user from Sharing...//
 * only applicable to Files shared with Writer Permission...
 *
 */
public class GDrivePreventRemediation extends GDriveUtils {

    String qaTestFolderInCloud = "GDriveRemediationTest";
    Map<String, String> preventTestFileInfo = new HashMap();
    String from;
    String jsonResponse;

    @Test
    public void performFileUploadsBeforePreventShareTest() throws Exception {
        from=getCurrentJodaTime();
        wait(20,"Waiting before Necessary File Upload for Prevent Remediation Test");
        dataProvider.updatedata("GDriveRemediation", "UNIQUEPREFIX", uniqueIdentifier);
        List<Map<String, Object>> dataAsMapList = dataProvider.getDataAsMapList("GDriveRemediation", "File Name");
        String uniqueFolderName = qaTestFolderInCloud + "PreventShare_" + uniqueIdentifier;
        String folderIdPreventRemediation = null;
        try {
            folderIdPreventRemediation = createFolder(qaTestFolderInCloud, gDriveAdmin);
        } catch (Exception ex) {
            Logger.getLogger(GDriveSecurletsRemediationTests.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        for (Map<String, Object> dataAsMapList1 : dataAsMapList) {
            String fileName = (String) dataAsMapList1.get("File Name");
            wait(10, "Wait Before Each File Upload... ");
            Reporter.log("## Uplaod a file.. with Name RemediationForExternalSharedFile in Unique Folder :" + uniqueFolderName, true);
            String insertFileId = null;
            try {
                insertFileId = uploadFileInToFolderWithMultipleAttempt(fileName, localFileLocation, folderIdPreventRemediation, gDriveAdmin);
            } catch (InterruptedException ex) {
                Logger.getLogger(GDriveSecurletsRemediationTests.class.getName()).log(Level.SEVERE, null, ex);
            }
            preventTestFileInfo.put(fileName, insertFileId);
        }
        wait(globalWaitTime*2, "Waiting After File Upload Activities Before PreventExternalUser Remediation...");   
    }

    @Test(groups = {"UIREMEDIATION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "preventRemediation")
    public void validatePreventExternalUserFromSharing(String TestName, String RestrictionType,
            String FileName, String PermissionRole, String PermissionType,
            String PermissionValue, String Comment, String WebLink) throws Exception {
        
        LogUtils.logTestDescription(TestName + " ## Restriction TO be Applied :" + RestrictionType);
        String insertFileId = preventTestFileInfo.get(FileName).trim();
        Reporter.log(" ## FILE ID :" + insertFileId, true);
        Reporter.log("## Share this File <RemediationForExternalSharedFile> To \n"
                + "## External User :" + PermissionValue
                + "## Permission Role : " + PermissionRole
                + "## Permission Type :" + PermissionType
                + "## Additional Comment Permission :" + Comment
                + "## WebLink :" + WebLink, true);

        Permission insertPermission = gDriveAdmin.insertPermission(
                insertFileId,
                PermissionValue,
                PermissionType,
                PermissionRole,
                Boolean.getBoolean(Comment),
                Boolean.getBoolean(WebLink)
        );

        Reporter.log("Insert Permission Response" + insertPermission.toPrettyString(), true);
        PermissionList retrievePermissionList_before = gDriveAdmin.retrievePermissionList(insertFileId);
        Reporter.log("Before Remediation :" + retrievePermissionList_before.toPrettyString(), true);
        wait(10, "Waiting for DOC ID from Elastica search Engine after External Share...");
        String docID1 = getDocID(FileName);
        Reporter.log(" ## DOC ID / File ID :" + docID1, true);
        Reporter.log(" ## Apply  " + RestrictionType, true);
        applyRestriction(insertFileId, RestrictionType);
        wait(20, "Waiting after " + RestrictionType + ".....");

        String insertPermissionResponseFromExternalUser;
        String downloadLoadStream;
        if (RestrictionType.equals("copy-print-download")) {
            InputStream downloadFile = gDriveExternalUser.downloadFile(insertFileId);
            Reporter.log("DownloadFile Content is NULL", true);
            Assert.assertTrue(downloadFile == null, "Prevent /Restrict Sharing Not Applied..on file :" + FileName);
            return;
        }
        try {
            Permission insertPermission_ExternalUser = gDriveExternalUser.insertPermission(gDriveExternalUser.getDriveService(), insertFileId, "rahulsky.java@gmail.com", "user", "writer"); // Role : writer,reader
            insertPermissionResponseFromExternalUser = insertPermission_ExternalUser.toPrettyString();
        } catch (Exception e) {
            insertPermissionResponseFromExternalUser = e.getMessage();
            Reporter.log("File Insert Permission Response after <writers-can-share> Restriction :" + insertPermissionResponseFromExternalUser, true);
        }
        String expectedResult = "You do not have permission to share these item(s): " + FileName;
        String actualResult = insertPermissionResponseFromExternalUser;
        Reporter.log("Actual Result/Insert Permission Response from External User :" + actualResult, true);
        Assert.assertTrue(actualResult.contains(expectedResult), "Prevent /Restrict Sharing Not Applied..on file :" + FileName);
    }
    
    @Test(groups = {"UIREMEDIATION", "P1"})
    public void fetchDisplayLogResponse() throws Exception{
        this.jsonResponse=getDisplayLogResponse("Fetching the Display Logs Response After Prevent Remediation", from);
    }
    
    @Test(groups = {"UIREMEDIATION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "preventRemediation")
    public void validatePreventRemediationActivityDisplayLogs(String TestName, String RestrictionType,
            String FileName, String PermissionRole, String PermissionType,
            String PermissionValue, String Comment, String WebLink){
        Map expectedResult = new HashMap();
        if(FileName.contains("Writer")){
        expectedResult.put("message", "Prevent writers from sharing "+FileName);
        }
        else{
         expectedResult.put("message", "Prevent copy, print and download of "+FileName);   
        }    
        RawJsonParser.LogValidator(this.jsonResponse,expectedResult,"$.hits.hits[*].source","message");    
    }

    @Test(groups = {"UIREMEDIATION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "preventRemediation")
    public void validatePreventCIDisplayLogs(String TestName, String RestrictionType,
            String FileName, String PermissionRole, String PermissionType,
            String PermissionValue, String Comment, String WebLink) throws Exception{
        
         Map expectedResult = new HashMap();
         expectedResult.put("message", "File "+FileName+" has risk(s)");
         
         RawJsonParser.LogValidatorPartialCheck(this.jsonResponse,expectedResult,"$.hits.hits[*].source","message");       
    }
    
    @Test(groups = {"UIREMEDIATION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "preventRemediation")
    public void validatePreventFileUploadDisplayLogs(String TestName, String RestrictionType,
            String FileName, String PermissionRole, String PermissionType,
            String PermissionValue, String Comment, String WebLink){
       
       Map expectedResult = new HashMap();
       expectedResult.put("message", "User uploaded file "+FileName);
        
       RawJsonParser.LogValidator(this.jsonResponse,expectedResult,"$.hits.hits[*].source","message");     
    }
    
    @Test(groups = {"UIREMEDIATION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "preventRemediation")
    public void validatePreventFileShareActivityDisplayLogs(String TestName, String RestrictionType,
            String FileName, String PermissionRole, String PermissionType,
            String PermissionValue, String Comment, String WebLink){
        
        Map expectedResult = new HashMap();
        expectedResult.put("message", "User shared "+FileName);
        
        RawJsonParser.LogValidator(this.jsonResponse,expectedResult,"$.hits.hits[*].source","message");    
    }
    
    

}
