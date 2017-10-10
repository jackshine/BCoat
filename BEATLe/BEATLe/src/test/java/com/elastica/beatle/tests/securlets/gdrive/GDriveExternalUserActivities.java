package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.RawJsonParser;
import com.google.api.services.drive.model.Permission;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar Perform Permission/Sharing Operations by external users to
 * Securlet beatle Account..
 */
public class GDriveExternalUserActivities extends GDriveUtils {

    String from_jodaTime;
    Map<String, String> fileAndfolderIdFromExternalUser = new HashMap();
    String jsonResponse;
    List<String> externalSharedFiles = new ArrayList<>();
    List<String> externalSharedFolder = new ArrayList<>();

    @Test(groups = {"EXTERNALUSERACTIVITIES", "P1"})
    public void performSharingActionsToSeurletbeatleAccountBYExtrnalUser() throws IOException, GeneralSecurityException, Exception {
        from_jodaTime = getCurrentJodaTime();
        // Update Credentials for External User...
        Reporter.log("## Perform Sharing Actions To Seurletbeatle Account BY Extrnal User ...", true);
        fileOperationsByExternalUser();
        folderActivitiesByExternalUser();
        wait(globalWaitTime * 2, "--- Waiting for mins after External Activities --- ...");
        this.jsonResponse = getDisplayLogResponse("Waiting for ES logs after performing Folder Operations...", from_jodaTime);
    }

    public void fileOperationsByExternalUser() throws IOException, InterruptedException {
        // File Sharing...
        Reporter.log("Upload File To External User Account and Share the file with Securlet Beatle Account...", true);
        String folderId = null;
        try {
            folderId = this.gDriveExternalUser.createFolder("ExternalUserOperationForGDriveSecurletQA-" +uniqueIdentifier);
        } catch (InterruptedException ex) {
            Reporter.log("Issue With Folder Create :" + ex.getLocalizedMessage(), true);
        } catch (IOException ex) {
            Reporter.log("Issue With Folder Create :" + ex.getLocalizedMessage(), true);
        }
        Reporter.log("Folder To be created :ExternalUserOperationForGDriveSecurletQA-" + uniqueIdentifier + "## Folder ID :" + folderId, true);
        String file_readerPermission = "ExternallyFileShared_reader-" + uniqueIdentifier;
        String file_writerPermission = "ExternallyFileShared_writer-" + uniqueIdentifier;
        String file_commentPermission = "ExternallyFileShared_comment-" + uniqueIdentifier;
        externalSharedFiles.add(file_readerPermission);
        externalSharedFiles.add(file_writerPermission);
        externalSharedFiles.add(file_commentPermission);
        wait(globalWaitTime / 3, "Wait before file Upload - Reader Permission");
        String fileId_reader = this.gDriveExternalUser.uploadFile(folderId, localFileLocation, file_readerPermission).getFileId();
        wait(globalWaitTime / 3, "Wait before file Upload - Writer Permission");
        String fileId_writer = this.gDriveExternalUser.uploadFile(folderId, localFileLocation, file_writerPermission).getFileId();
        wait(globalWaitTime / 3, "Wait before file Upload - Comment Permission");
        String fileId_comment = this.gDriveExternalUser.uploadFile(folderId, localFileLocation, file_commentPermission).getFileId();
        wait(globalWaitTime / 3, "File Uploads Done !!!!");

        Reporter.log("## Share the Uploaded File with "+gDriveAdmin.getEmailId(), true);
        /* File Permission */
        // Insert Reader Permission...
        Permission insertPermission_read = null;
        try {
            insertPermission_read = this.gDriveExternalUser.insertPermission(this.gDriveExternalUser.getDriveService(), fileId_reader, gDriveAdmin.getEmailId(), "user", "reader"); // Role : writer,reader
        } catch (IOException ex) {
            Reporter.log("Issue with Insert Permission :" + ex.getLocalizedMessage(), true);
        }
        try {
            Reporter.log("## Insert Read Permission Response :" + insertPermission_read.toPrettyString(), true);
        } catch (IOException ex) {
            Logger.getLogger(GDriveExternalUserActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
        wait(globalWaitTime / 3, "Wait before Insert Write Permission");
        // Insert Write Permission...
        Permission insertPermission_write = null;
        try {
            insertPermission_write = this.gDriveExternalUser.insertPermission(this.gDriveExternalUser.getDriveService(), fileId_writer, gDriveAdmin.getEmailId(), "user", "writer"); // Role : writer,reader
        } catch (IOException ex) {
            Logger.getLogger(GDriveExternalUserActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Reporter.log("## Insert Write Permission Response :" + insertPermission_write.toPrettyString(), true);
        } catch (IOException ex) {
            Logger.getLogger(GDriveExternalUserActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
        wait(globalWaitTime / 3, "Wait before Insert Comment Permission");
        // Insert Comment Permission...
        Permission insertCommentPermission = null;
        try {
            insertCommentPermission = insertCommentPermission(fileId_comment, this.gDriveExternalUser);
        } catch (IOException ex) {
            Logger.getLogger(GDriveExternalUserActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
        wait(globalWaitTime / 3, "Wait before File Update Permission");
        //File Update Permission..
        Reporter.log("File Update Operation.....by External User...", true);
        String updatePermission = this.gDriveExternalUser.updatePermission(fileId_reader, insertPermission_read.getId(), "writer");
        Reporter.log("Update Permission Response< Reader to Writer >:" + updatePermission, true);
        wait(globalWaitTime / 3, "Wait before File Update Permission");
        String updatePermission1 = this.gDriveExternalUser.updatePermission(fileId_writer, insertPermission_write.getId(), "reader");
        Reporter.log("Update Permission Response< Writer To Reader >:" + updatePermission1, true);
        wait(globalWaitTime, "Wait before File Remove Permission");
        // Remove Permission...
        this.gDriveExternalUser.removePermission(this.gDriveExternalUser.getDriveService(), fileId_reader, insertPermission_read.getId());
        wait(globalWaitTime / 3, "Wait before File Remove Permission");
        this.gDriveExternalUser.removePermission(this.gDriveExternalUser.getDriveService(), fileId_writer, insertPermission_write.getId());
        wait(globalWaitTime / 3, "Wait before File Remove Permission");

        try {
            this.gDriveExternalUser.removePermission(this.gDriveExternalUser.getDriveService(), fileId_comment, insertCommentPermission.getId());
        } catch (Exception e) {
            Reporter.log("Exception in Remove File Comment Permission :" + e.getLocalizedMessage());
        }
        wait(globalWaitTime, "Wait After File Remove Permission");
    }

    public void folderActivitiesByExternalUser() throws InterruptedException, IOException {
        // Folder Permission Activities ...
        String folderId_reader = this.gDriveExternalUser.createFolder("ExternallyFolderShared_reader-" + uniqueIdentifier);

        wait(globalWaitTime / 3, "Wait : Create Folder");
        String folderId_writer = this.gDriveExternalUser.createFolder("ExternallyFolderShared_writer-" + uniqueIdentifier);
        externalSharedFolder.add("ExternallyFolderShared_reader-" + uniqueIdentifier);
        externalSharedFolder.add("ExternallyFolderShared_writer-" + uniqueIdentifier);

        wait(globalWaitTime / 3, "Wait : Insert Read Permission in Folder");
        // Insert Read Permission...
        Permission insertPermission_readFolder = this.gDriveExternalUser.insertPermission(this.gDriveExternalUser.getDriveService(), folderId_reader, gDriveAdmin.getEmailId(), "user", "reader"); // Role : writer,reader
        Reporter.log("## Folder Insert Read Permission Response :" + insertPermission_readFolder.toPrettyString(), true);
        // Insert Write Permission...
        wait(globalWaitTime / 3, "Wait : Insert Write Permission in Folder");
        Permission insertPermission_writeFolder = this.gDriveExternalUser.insertPermission(this.gDriveExternalUser.getDriveService(), folderId_writer, gDriveAdmin.getEmailId(), "user", "writer"); // Role : writer,reader
        Reporter.log("## Folder Insert Write Permission Response :" + insertPermission_writeFolder.toPrettyString(), true);
        //Update Folder Permission
        Reporter.log(" ## Folder Update Permission Activities...", true);
        // Reader To Writer...
        String updatePermission = this.gDriveExternalUser.updatePermission(folderId_reader, insertPermission_readFolder.getId(), "writer");
        Reporter.log(" ## Folder Update Permission <Reader To Writer> :" + updatePermission, true);
        wait(globalWaitTime / 3, "Wait : Update Writer TO Read Permission in Folder");
        // Writer To Reader...
        String updatePermission1 = this.gDriveExternalUser.updatePermission(folderId_writer, insertPermission_writeFolder.getId(), "reader");
        Reporter.log(" ## Folder Update Permission <Writer To Reader> :" + updatePermission1, true);
        wait(globalWaitTime / 3, "Wait After : Update Writer TO Reader Permission in Folder");
        //UnShare Folder..
        wait(globalWaitTime / 3, "Wait : Remove Permission in Folder");
        this.gDriveExternalUser.removePermission(this.gDriveExternalUser.getDriveService(), folderId_reader, insertPermission_readFolder.getId());
        wait(globalWaitTime / 3, "Wait : Remove Permission in Folder");
        this.gDriveExternalUser.removePermission(this.gDriveExternalUser.getDriveService(), folderId_writer, insertPermission_writeFolder.getId());
    }

    @Test(groups = {"EXTERNALUSERACTIVITIES", "P1"}, dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-ExternalActivities")
    public void validateExternalUserActivities(String testName, String msg) throws Exception {
        System.out.println("Test Name :" + testName);
        Map<String, String> expectedResult = new HashMap();
        expectedResult.put("message", msg);
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

    // @Test(groups={"EXTERNALUSERACTIVITIES", "P2"})
    public void validateCIForExternalySharedFile() throws Exception {
        Map<String, String> expectedResult = new HashMap();
        expectedResult.put("message", "File ExternallyFileShared_comment has risk(s) - GLBA");
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

}
