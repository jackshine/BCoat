package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.RawJsonParser;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.universal.common.GDrive;
import com.universal.dtos.box.FileUploadResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class GDriveFilePermissionAndExposure extends GDriveUtils {

    String from_jodaTime;
    Map<String, String> fileIdWithPermissionId = new LinkedHashMap<>();
    String jsonResponse;
    Map<String, Long> dashboardMeritcs = new HashMap();
    String internalSwitch;
    GDrive gDrive;

    @Test(groups = {"FILEPERMISSION", "P1"})
    public void fileInsertPermissionOperations() {
        internalSwitch = getRegressionSpecificSuitParameters("internalSwitch");
        Reporter.log("======= Internal Switch :" + internalSwitch, true);
        try {
           dashboardMeritcs=getDashboardMetrics(internalSwitch);
        } catch (Exception ex) {
            Reporter.log("Issue with Retreving Dashboard Metrics ",true);
        }
        if (internalSwitch.equals("true")) {
            gDrive = gDriveAdmin;
        } else {
            gDrive = gDriveExternalUser;
        }
        from_jodaTime = getCurrentJodaTime();
        wait(globalWaitTime, "WAITING AFTER  dashboard count FETCH ..before Insert Permission...");
        Reporter.log("\n================= GDRIVE SaaS Actions : INSERT PERMISSION OPERATIONS  =====================", true);
        String folderName = uniqueIdentifier + "_InsertOperation-QA";
        String createFolderId = null; 
        try {
            createFolderId = gDrive.createFolder(folderName); // Create Folder in SaaS App..
        } catch (InterruptedException ex) {
            Reporter.log("Issue With Folder Creation...",true);
        } catch (IOException ex) {
            Reporter.log("Issue With Folder Creation...",true);
        }
        dataProvider.updatedata("GDriveInputPermission", "UNIQUESUFFIX", uniqueIdentifier);
        dataProvider.updatedata("GDrivePermissions", "UNIQUESUFFIX", uniqueIdentifier);
        List<Map<String, Object>> dataAsMapList = GDriveDataProvider.getDataProvider().getDataAsMapList("GDriveInputPermission","File Name", "Permission Role", "Permission Type", "Permission Value", "Additional Value/Comment", "WebLink");
        for (Map<String, Object> dataAsMapList1 : dataAsMapList) {
            String FileName = (String) dataAsMapList1.get("File Name");
            wait(10, "10 sec... sleep after each upload and insert permission....File Name:" + FileName);
            FileUploadResponse uploadFile = gDrive.uploadFile(createFolderId, localFileLocation, dataAsMapList1.get("File Name").toString()); // UploadFile...
            Permission insertPermission;
            Reporter.log("========================== INPUT PERMISSION =================================",true);
            RawJsonParser.printMap(dataAsMapList1);
            insertPermission = insertFilePermission(uploadFile, dataAsMapList1);
            try { 
                Reporter.log("## Insert Permission Response :"+insertPermission.toPrettyString(),true);
            } catch (IOException ex) {
                insertPermission = insertFilePermission(uploadFile, dataAsMapList1);
                Reporter.log("Issue With Insert Permission...",true);
            }
            if (internalSwitch.equals("false")) {
                shareFileToAdmin(uploadFile, dataAsMapList1);
            }
            fileIdWithPermissionId.put(uploadFile.getFileId(), insertPermission.getId());
            PermissionList retrievePermissionList = gDrive.retrievePermissionList(uploadFile.getFileId());
            try {
                Reporter.log("Permission Applied To the file <"+FileName + ">:"+retrievePermissionList.toPrettyString(),true);
            } catch (IOException ex) {
                Reporter.log("## Retrieve Permission List Pretty Exception :"+ex.getLocalizedMessage(),true);
            }
        }
        wait(180, "Waiting for the dashboard count to be incremented...");
        try {
            this.jsonResponse = getDisplayLogResponse("Waiting for ES logs after performing File Insert Permission Operations...", from_jodaTime);
        } catch (Exception ex) {
            Reporter.log("Issue with Get Display Logs :"+ex.getLocalizedMessage(),true);
        }
    }

    public void shareFileToAdmin(FileUploadResponse uploadFile, Map<String, Object> dataAsMapList1) {
        Permission insertPermission1 = null;
        try {
            insertPermission1 = gDrive.insertPermission(
                    uploadFile.getFileId(),
                    gDriveAdmin.getEmailId(),
                    "user",
                    dataAsMapList1.get("Permission Role").toString(),
                    Boolean.getBoolean(dataAsMapList1.get("Additional Value/Comment").toString()),
                    Boolean.getBoolean(dataAsMapList1.get("WebLink").toString()));
        } catch (InterruptedException ex) {
            Reporter.log("Issue in Applying Permission with admin :"+ex.getLocalizedMessage(),true);
        }
        try {
            Reporter.log("EXTERNAL SWITCH : File Shared with admin user <Insert Permission Resonse> :" + insertPermission1.toPrettyString(), true);
        } catch (IOException ex) {
            Reporter.log("## Insert Permission Pretty Exception :"+ex.getLocalizedMessage(),true);
        }
    }

    private Permission insertFilePermission(FileUploadResponse uploadFile, Map<String, Object> dataAsMapList1) {
        Permission insertPermission;
        Reporter.log("=============================================================================",true);
        Reporter.log("====== Insert Permission ======", true);
        insertPermission = gDrive.insertPermission(
                uploadFile.getFileId(),
                dataAsMapList1.get("Permission Value").toString(),
                dataAsMapList1.get("Permission Type").toString(),
                dataAsMapList1.get("Permission Role").toString(),
                Boolean.getBoolean(dataAsMapList1.get("Additional Value/Comment").toString()),
                Boolean.getBoolean(dataAsMapList1.get("WebLink").toString())
        );
        return insertPermission;
    }

    @Test(groups = {"FILEPERMISSION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-IncrementCount")
    public void validateIncrementCountAfterFilePermissionActivities(String testDesc, String exposure) throws Exception {
        long preCount = 0;
        long postCount = 0;
        if (exposure.equals("public_searchable")) {
            
            preCount = dashboardMeritcs.get("countPublicSearchAbleInternalSwitch");
            postCount = getPublicExposedCountSearchable(internalSwitch);
            Reporter.log("Test Desc :" + testDesc, true);
            Reporter.log("Expected Result <Dashboard Count AFTER> :" + (preCount + 4), true);
            Reporter.log("Actual Result <Dashboard Count BEFORE> :" + postCount, true);
            Assert.assertEquals(postCount, preCount + 4, " ## Count is not Incremented ## for " + exposure);
        } else if (exposure.equals("public_weblink")) {
            preCount = dashboardMeritcs.get("countPublicWebLinkInternalSwitch");
            postCount = getPublicExposedCountwithLink(internalSwitch);
            Reporter.log("================================ ASSERTION ==========================================", true);
            Reporter.log("Test Desc :" + testDesc, true);
            Reporter.log("Pre Count :" + preCount + " ## Post Count :" + postCount, true);
            Reporter.log("Expected Result <Dashboard Count AFTER> :" + (preCount + 2), true);
            Reporter.log("Actual Result <Dashboard Count BEFORE> :" + postCount, true);
            Assert.assertEquals(postCount, preCount + 2, " ## Count is not Incremented ## for " + exposure);
        } else if (exposure.equals("internal_searchable")) {
            preCount = dashboardMeritcs.get("countAllInternalSearchAbleInternalSwitch");
            postCount = getInternalExposedCountSearchable(internalSwitch);
            Reporter.log("================================ ASSERTION ==========================================", true);
            Reporter.log("Test Desc :" + testDesc, true);
            Reporter.log("Pre Count :" + preCount + " ## Post Count :" + postCount, true);
            Reporter.log("Expected Result <Dashboard Count AFTER> :" + (preCount + 4), true);
            Reporter.log("Actual Result <Dashboard Count BEFORE> :" + postCount, true);
            Assert.assertEquals(postCount, preCount + 4, " ## Count is not Incremented ## for " + exposure);
        } else if (exposure.equals("internal_weblink")) {
            preCount = dashboardMeritcs.get("countAllInternalwithLinkInternalSwitch");
            postCount = getInternalExposedCountwithLink(internalSwitch);
            Reporter.log("================================ ASSERTION ==========================================", true);
            Reporter.log("Test Desc :" + testDesc, true);
            Reporter.log("Pre Count :" + preCount + " ## Post Count :" + postCount, true);
            Reporter.log("Expected Result <Dashboard Count AFTER> :" + (preCount + 2), true);
            Reporter.log("Actual Result <Dashboard Count BEFORE> :" + postCount, true);
            Assert.assertEquals(postCount, preCount + 2, " ## Count is not Incremented ## for " + exposure);
        } else if (exposure.equals("external")) {
            preCount = dashboardMeritcs.get("countExternalInternalSwitch");
            postCount = getExternalExposedCount(internalSwitch);
            Reporter.log("================================ ASSERTION ==========================================", true);
            Reporter.log("Test Desc :" + testDesc, true);
            Reporter.log("Pre Count :" + preCount + " ## Post Count :" + postCount, true);
            Reporter.log("Expected Result <Dashboard Count AFTER> :" + (preCount + 6), true);
            Reporter.log("Actual Result <Dashboard Count BEFORE> :" + postCount, true);
            Assert.assertEquals(postCount, preCount + 6, " ## Count is not Incremented ## for " + exposure);
        } else if (exposure.equals("total")) {
            preCount = dashboardMeritcs.get("totalCountInternalSwitch");
            postCount = getTotalCount(internalSwitch);
            Reporter.log("================================ ASSERTION ==========================================", true);
            Reporter.log("Test Desc :" + testDesc, true);
            Reporter.log("Pre Count :" + preCount + " ## Post Count :" + postCount, true);
            Reporter.log("Expected Result <Dashboard Count AFTER> :" + (preCount + 18), true);
            Reporter.log("Actual Result <Dashboard Count BEFORE> :" + postCount, true);
            Assert.assertEquals(postCount, preCount + 18, " ## Count is not Incremented ## for " + exposure);
        } else {
            Reporter.log("Exposure Not Found...", true);
        }
    }

    @Test(groups = {"FILEPERMISSION", "P2"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-ExposedFileInternalSwitch")
    public void validateExposedFileAfterApplyingExposure(String testDesc, String fileName, String expectedExposure) throws Exception {
        Reporter.log("File Name :" + fileName, true);
        String docInfo = getDocInfo(fileName);
        Map<String, Object> expectedResult = new HashMap();
        if (internalSwitch.equals("true")) {
            expectedResult.put("name", fileName);
            expectedResult.put("exposures", expectedExposure);
            expectedResult.put("object_type", "Drive");
            expectedResult.put("path", null);
            expectedResult.put("changeset_id", null);
            expectedResult.put("owner_id", null);
            expectedResult.put("download_status_code", null);
            List<String> ignoredKeyList = new ArrayList();
            ignoredKeyList.add("scan_ts");
            boolean matchResult = RawJsonParser.findExpectedKeysAndPartialValues(docInfo, "$.objects[0]", expectedResult, "name", ignoredKeyList);
            Reporter.log(" ### Result Found :" + matchResult, true);
            Assert.assertTrue(matchResult, "Message Not Found :" + expectedResult);
        } else {
            // External Switch....
            expectedResult.put("name", fileName);
            if (fileName.contains("User")) {
                expectedResult.put("exposures", "internal=[admin@securletbeatle.com]");
            } else {
                expectedResult.put("exposures", expectedExposure);
            }
            expectedResult.put("object_type", "Drive");
            expectedResult.put("path", null);
            expectedResult.put("changeset_id", null);
            expectedResult.put("owner_id", null);
            expectedResult.put("download_status_code", null);
            expectedResult.put("parent_id", null);
            expectedResult.put("org_unit", null);
            List<String> ignoredKeyList = new ArrayList();
            ignoredKeyList.add("scan_ts");
            boolean matchResult = RawJsonParser.findExpectedKeysAndPartialValues(getDocInfo(fileName), "$.objects[0]", expectedResult, "name", ignoredKeyList);
            Reporter.log(" ### Result Found :" + matchResult, true);
            Assert.assertTrue(matchResult, "Message Not Found :" + expectedResult);
        }

    }

    @Test(groups = {"FILEPERMISSION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-FileInsertPermission")
    public void validateFileInsertPermission(String testName, String filename) throws Exception {
        Map expectedResult = new HashMap();
        if (internalSwitch.equals("true")) {
            expectedResult.put("message", "User shared " + filename);
            expectedResult.put("name", filename);
            expectedResult.put("Activity_type", "Share");
            expectedResult.put("source", "API");
            expectedResult.put("facility", "Google Drive");
        } else {
            // External Switch...
            expectedResult.put("message", "User shared " + filename);
            expectedResult.put("name", filename);
            expectedResult.put("Activity_type", "Share");
            expectedResult.put("source", "API");
            expectedResult.put("facility", "Google Drive");
        }
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

    @Test(groups = {"FILEPERMISSION", "P2"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-FileInsertPermission")
    public void validateCILogsVariousExposedFile(String testName, String filename) throws Exception {
        Map expectedResult = new HashMap();
        if (internalSwitch.equals("true")) {
            expectedResult.put("message", "File " + filename + " has risk(s)");
            expectedResult.put("Activity_type", "Content Inspection");
        } else {
            // External Switch...
            expectedResult.put("message", "File " + filename + " has risk(s)");
            expectedResult.put("Activity_type", "Content Inspection");
        }
        RawJsonParser.LogValidatorPartialCheck(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

    @Test(groups = {"FILEPERMISSION", "P1"})
    public void performFile_Update_Patch_Remove_PermissionOperations() throws Exception {
        RawJsonParser.wait(5000, "Capturing Dashboard Count Before Remove Permission..");
        dashboardMeritcs=getDashboardMetrics(internalSwitch);
        from_jodaTime = getCurrentJodaTime();
        wait(30, "Waiting before Update And Patch Permission Operation");
        updatePermissionOperations();
        performPatchPermissionOperations();
        removeFilePermission();
        wait(180, "Waiting After Update And Patch And Remove Permission Operation");
        this.jsonResponse = getDisplayLogResponse("Waiting for ES logs after performing Folder Operations...", from_jodaTime);
    }

    public void updatePermissionOperations() throws InterruptedException, IOException, Exception {
        Reporter.log("======================= Update the permission Operations =========================", true);
        for (Map.Entry<String, String> entrySet : fileIdWithPermissionId.entrySet()) {
            String insertFileId = entrySet.getKey();
            String permissionId = entrySet.getValue();
            Reporter.log(" ## Insert FileId :" + insertFileId + " ## Permission Id :" + permissionId);
            Thread.sleep(globalWaitTime * 1000 / 3);
            PermissionList retrievePermissionList = gDrive.retrievePermissionList(insertFileId);
            Reporter.log("Before Update Permission  :" + retrievePermissionList.toPrettyString(), true);
            List<String> permissionMatch = new ArrayList();
            permissionMatch.add("reader");
            String updatePermissionResponse = null;
            boolean matchPermission = matchPermission(retrievePermissionList, permissionMatch);
            if (matchPermission) {
                updatePermissionResponse = gDrive.updatePermission(insertFileId, permissionId, "writer");
            } else {
                updatePermissionResponse = gDrive.updatePermission(insertFileId, permissionId, "reader");
            }
            Reporter.log(" ## Update Permission Response : " + updatePermissionResponse, true);
            Reporter.log("After Update Permission  :" + gDrive.retrievePermissionList(insertFileId), true);
            wait(globalWaitTime/3,"waiting After Update Permission...");
        }
        Reporter.log(" ## Update Permission Activities Completed.......", true);
    }

    public void performPatchPermissionOperations() throws IOException, InterruptedException, Exception {
        Reporter.log("###### Patcing the permission ############..............", true);
        for (Map.Entry<String, String> entrySet : fileIdWithPermissionId.entrySet()) {
            String insertFileId = entrySet.getKey();
            String permissionId = entrySet.getValue();
            PermissionList retrievePermissionList = gDrive.retrievePermissionList(insertFileId);
            Reporter.log(" ## Before PATCH Permission  :" + retrievePermissionList.toPrettyString(), true);
            List<String> permissionMatch = new ArrayList();
            permissionMatch.add("reader");
            String patchPermissionResponse = null;
            boolean matchPermission = matchPermission(retrievePermissionList, permissionMatch);
            if (matchPermission) {
                patchPermissionResponse = gDrive.patchPermission(insertFileId, permissionId, "writer");
            } else {
                patchPermissionResponse = gDrive.patchPermission(insertFileId, permissionId, "reader");
            }
            System.out.println("Patch Permission <READER> Reponse :" + patchPermissionResponse);
            wait(globalWaitTime / 3, "Wait After Patch Permission....");
        }
    }

    public void removeFilePermission() throws IOException, InterruptedException, Exception {
        Reporter.log("==========> Removing File Permission Operations.. <=================", true);
        for (Map.Entry<String, String> entrySet : fileIdWithPermissionId.entrySet()) {
            String insertFileId = entrySet.getKey();
            String permissionId = entrySet.getValue();
            Reporter.log("## File ID : " + insertFileId + " ## Permission ID : " + permissionId, true);
            gDrive.removePermission(gDrive.getDriveService(), insertFileId, permissionId);
            wait(globalWaitTime / 3, "Wait after each Remove Permission Operation...");
        }
    }

    @Test(groups = {"FILEPERMISSION", "P2"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-FileUpdatePermission")
    public void validateFileUpdatePermission(String testName, String name) throws Exception {
        Map expectedResult = new HashMap();
        if (internalSwitch.equals("true")) {
            expectedResult.put("message", "User changed permission on file " + name);
            expectedResult.put("name", name);
            expectedResult.put("Activity_type", "Role Change");
        } else {
            // External Switch
            expectedResult.put("message", "User changed permission on file " + name);
            expectedResult.put("name", name);
            expectedResult.put("Activity_type", "Role Change");
        }
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

    @Test(groups = {"FILEPERMISSION", "P2"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-FilePatchPermission")
    public void validatePatchFilePermission(String testName, String name) throws Exception {
        Reporter.log(" ## Test Name :" + testName, true);
        Map expectedResult = new HashMap();
        if (internalSwitch.equals("true")) {
            expectedResult.put("message", "User changed permission on file " + name);
            expectedResult.put("name", name);
            expectedResult.put("Activity_type", "Role Change");
        } else {
            // External Switch
            expectedResult.put("message", "User unshared " + name);
            expectedResult.put("name", name);
            expectedResult.put("Activity_type", "Unshare");
        }
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

    @Test(groups = {"FILEPERMISSION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-FileRemovePermission")
    public void validateUnShareFile(String testName, String name) throws Exception {
        Map expectedResult = new HashMap();
        if (internalSwitch.equals("true")) {
            expectedResult.put("message", "User unshared " + name);
            expectedResult.put("name", name);
            expectedResult.put("Activity_type", "Unshare");
        } else {
            // External Switch
            expectedResult.put("message", "User unshared " + name);
            expectedResult.put("name", name);
            expectedResult.put("Activity_type", "Unshare");
        }
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

    @Test(groups = {"FILEPERMISSION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-DecrementCount")
    public void validateDecrementCountAfterFilePermissionActivities(String testDesc, String exposure) throws Exception {
        long preCount = 0;
        long postCount = 0;
        if (exposure.equals("public_searchable")) {
            preCount = dashboardMeritcs.get("countPublicSearchAbleInternalSwitch");
            postCount = getPublicExposedCountSearchable(internalSwitch);
            Assert.assertEquals(postCount, (preCount - 4), " ## Count is not Decremented ## for " + exposure);
        } else if (exposure.equals("public_weblink")) {
            preCount = dashboardMeritcs.get("countPublicWebLinkInternalSwitch");
            postCount = getPublicExposedCountwithLink(internalSwitch);
            Assert.assertEquals(postCount, (preCount - 2), " ## Count is not Decremented ## for " + exposure);
        } else if (exposure.equals("internal_searchable")) {
            preCount = dashboardMeritcs.get("countAllInternalSearchAbleInternalSwitch");
            postCount = getInternalExposedCountSearchable(internalSwitch);
            Assert.assertEquals(postCount, (preCount - 4), " ## Count is not Decremented ## for " + exposure);
        } else if (exposure.equals("internal_weblink")) {
            preCount = dashboardMeritcs.get("countAllInternalwithLinkInternalSwitch");
            postCount = getInternalExposedCountwithLink(internalSwitch);
            Assert.assertEquals(postCount, (preCount - 2), " ## Count is not Decremented ## for " + exposure);
        } else if (exposure.equals("external")) {
            preCount = dashboardMeritcs.get("countExternalInternalSwitch");
            postCount = getExternalExposedCount(internalSwitch);
            Assert.assertEquals(postCount, (preCount - 6), " ## Count is not Decremented ## for " + exposure);
        } else if (exposure.equals("total")) {
            preCount = dashboardMeritcs.get("totalCountInternalSwitch");
            postCount = getTotalCount(internalSwitch);
            Assert.assertEquals(postCount, (preCount - 18), " ## Count is not Decremented ## for " + exposure);
        } else {
            Reporter.log("Exposure Not Found...", true);
        }
        Reporter.log(testDesc, true);
        Reporter.log("Dashboard Count before :" + preCount, true);
        Reporter.log("Dashboard Count after :" + postCount, true);
    }

    @Test(groups = {"FILEPERMISSION", "P2"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-ExposedFileInternalSwitch")
    public void validateExposedFileAfterRemovingExposure(String testDesc, String fileName, String expectedExposure) throws Exception {
        Reporter.log("File Name :" + fileName, true);
        String docInfo = getRiskyDocInfo(fileName);
        if (internalSwitch.equals("true")) {
            Map<String, Object> expectedResult = new HashMap();
            expectedResult.put("name", fileName);
            expectedResult.put("object_type", "Drive");
            expectedResult.put("path", null);
            expectedResult.put("exp_state", 0);
            expectedResult.put("owner_id", null);
            expectedResult.put("exposed", false);
            expectedResult.put("changeset_id", null);
            expectedResult.put("download_status_code", null);
            boolean matchResult = RawJsonParser.findExpectedKeysAndPartialValues(docInfo, "$.objects[0]", expectedResult, "name");
            Reporter.log(" ### Result Found :" + matchResult, true);
            Assert.assertTrue(matchResult, "Message Not Found :" + expectedResult);
        } else {
            // External Switch....
            String expectedJsonResponse = "\"total_count\": 0";
            Assert.assertTrue(getRiskyDocInfo(fileName).contains(expectedJsonResponse), "Expected Sub String <" + expectedJsonResponse + "> not found in the Json Response :" + docInfo);
        }
    }
}
