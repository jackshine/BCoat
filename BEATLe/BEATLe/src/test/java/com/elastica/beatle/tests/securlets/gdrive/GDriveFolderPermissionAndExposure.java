package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.RawJsonParser;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.universal.common.GDrive;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class GDriveFolderPermissionAndExposure extends GDriveUtils {

    String from_jodaTime;
    Map<String, String> folderIdWithPermissionId = new LinkedHashMap<>();
    String folderIDPermissionTest;
    String jsonResponse;
    String destinationFolderId;
    String internalSwitch;
    GDrive gDrive;

    @BeforeClass
    public void initialSetUp() {
        internalSwitch = getRegressionSpecificSuitParameters("internalSwitch");
        if (internalSwitch.equals("true")) {
            gDrive = gDriveAdmin;
        } else {
            gDrive = gDriveExternalUser;
        }
    }

    @Test(groups = {"FOLDERPERMISSION", "P1"})
    public void performFolderPermissionOperations() throws Exception {
        from_jodaTime = getCurrentJodaTime();
        folderInsertPermissionOperations();
        performFolderUpdatePermissionOperations();
        performRemoveFolderPermission();
        wait(180, "Waiting After Folder Permission Activities...");
        this.jsonResponse = getDisplayLogResponse("Waiting for ES logs after performing Folder Operations...", from_jodaTime);
    }

    public void folderInsertPermissionOperations() throws Exception {
        Reporter.log("Performing the Folder Insert Permission Operations....", true);
        String folderName = GDriveDataProvider.uniqueIdentifier + "_InsertFolderPermission-QA";
        destinationFolderId = gDrive.createFolder(folderName); // Create Folder in SaaS App.. 
        Reporter.log("\n================= GDRIVE SaaS Actions : INSERT FOLDER PERMISSION OPERATIONS  =====================", true);
        GDriveDataProvider.getDataProvider().updatedata("GDriveFolderPermission", "UNIQUESUFFIX", GDriveDataProvider.uniqueIdentifier);
        List<Map<String, Object>> dataAsMapList = GDriveDataProvider.getDataProvider().getDataAsMapList("GDriveFolderPermission",
                "Folder Name", "Permission Role", "Permission Type", "Permission Value", "WebLink");
        for (Map<String, Object> dataAsMapList1 : dataAsMapList) {
            List<String> folderIdList = new ArrayList();
            folderIdList.add(destinationFolderId);
            String folderId_newFolder = gDrive.createFolder(dataAsMapList1.get("Folder Name").toString(), folderIdList);
            Permission insertPermission = null;
            if (dataAsMapList1.toString().contains("WebLink=true")) {
                Reporter.log("===========Inserting Permission with WebLink...========================", true);
                insertPermission = gDrive.insertPermissionWithWebLink(folderId_newFolder,
                        dataAsMapList1.get("Permission Value").toString(), dataAsMapList1.get("Permission Type").toString(), dataAsMapList1.get("Permission Role").toString());
                folderIdWithPermissionId.put(folderId_newFolder, insertPermission.getId());
            } else {
                Reporter.log("===========Inserting Permission with Searchable...========================", true);
                insertPermission = gDrive.insertPermission(gDrive.getDriveService(),
                        folderId_newFolder,
                        dataAsMapList1.get("Permission Value").toString(),
                        dataAsMapList1.get("Permission Type").toString(),
                        dataAsMapList1.get("Permission Role").toString(),
                        Boolean.getBoolean(dataAsMapList1.get("WebLink").toString()));
                System.out.println("===>" + insertPermission.toString());
                folderIdWithPermissionId.put(folderId_newFolder, insertPermission.getId());
            }
            wait(30, "Wait after each Insert Permission Operation...");
        }
        wait(20, "Wait for 20 Sec..After each folder Insert Permissions..");
        Reporter.log("\nFolder ID and Permission Id  :" + folderIdWithPermissionId.toString());
    }

    public void performFolderUpdatePermissionOperations() throws InterruptedException, IOException, Exception {
        Reporter.log("======================= Update the Folder Permission Operations =========================", true);
        for (Map.Entry<String, String> entrySet : folderIdWithPermissionId.entrySet()) {
            String insertFolderId = entrySet.getKey();
            String permissionId = entrySet.getValue();
            Reporter.log(" ## Insert FileId :" + insertFolderId + " ## Permission Id :" + permissionId);
            wait(globalWaitTime / 3, "Waiting before Insert Permission");
            PermissionList retrievePermissionList = gDrive.retrievePermissionList(insertFolderId);
            Reporter.log("Before Update Permission  :" + retrievePermissionList.toPrettyString(), true);
            List<String> permissionMatch = new ArrayList();
            permissionMatch.add("reader");
            String updatePermissionResponse = null;
            boolean matchPermission = matchPermission(retrievePermissionList, permissionMatch);
            if (matchPermission) {
                updatePermissionResponse = gDrive.updatePermission(insertFolderId, permissionId, "writer");
            } else {
                updatePermissionResponse = gDrive.updatePermission(insertFolderId, permissionId, "reader");
            }
            Reporter.log(" ## Update Permission Response : " + updatePermissionResponse, true);
            Reporter.log("After Update Permission  :" + gDrive.retrievePermissionList(insertFolderId), true);
            wait(30, "Wait after each Update Permission Operation...");
        }
        Reporter.log(" ## Update Permission Activities Completed.......", true);
    }

    public void performRemoveFolderPermission() throws IOException, InterruptedException, Exception {
        Reporter.log("==========> Removing FOLDER Permission Operations.. <=================", true);
        for (Map.Entry<String, String> entrySet : folderIdWithPermissionId.entrySet()) {
            String insertFolderId = entrySet.getKey();
            String permissionId = entrySet.getValue();
            Reporter.log("## Folder ID : " + insertFolderId + " ## Permission ID : " + permissionId, true);
            gDrive.removePermission(gDrive.getDriveService(), insertFolderId, permissionId);
            wait(30, "Wait after each Remove Permission Operation...");
        }
    }

    @Test(groups = {"FOLDERPERMISSION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-FolderInsertPermission")
    public void validateFolderInsertPermissions(String testName, String name) throws Exception {
        System.out.println("Test Name :" + testName);
        Map expectedResult = new HashMap();
        expectedResult.put("message", "User shared " + name);
        expectedResult.put("name", name);
        expectedResult.put("Activity_type", "Share");
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

    @Test(groups = {"FOLDERPERMISSION", "P2"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-FolderUpdatePermission")
    public void validateUpdateFolderPermission(String testName, String name) throws Exception {
        Map expectedResult = new HashMap();
        expectedResult.put("message", "User changed permission on folder " + name);
        expectedResult.put("name", name);
        expectedResult.put("Activity_type", "Role Change");
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

    @Test(groups = {"FOLDERPERMISSION", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-FolderRemovePermission")
    public void validateFolderRemovePermissions(String testName, String name) throws Exception {
        System.out.println("Test Name :" + testName);
        Map expectedResult = new HashMap();
        expectedResult.put("message", "User unshared " + name);
        expectedResult.put("name", name);
        expectedResult.put("Activity_type", "Unshare");
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

}
