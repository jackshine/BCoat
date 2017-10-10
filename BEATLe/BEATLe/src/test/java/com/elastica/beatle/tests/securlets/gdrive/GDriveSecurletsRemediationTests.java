package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.RestClient.ClientUtil;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class GDriveSecurletsRemediationTests extends GDriveUtils {

    String folderId;
    String fileId_publicExposedTest;
    String fileId_InternalExposedTest;
    String qaTestFolderInCloud = "GDriveRemediationTest";
    int waitTimeToReceiveDocId=360;
    String permissionId;
    Permission insertPermission;
    String InternalSearchableTotalCount_before;
    String publicShareFile_searchableOnWeb_BeforetotalCount;
    Map<String, String> remediationValues;
    String docID;
    String fileName=uniqueIdentifier + "_GDriveRemediationTest.txt";;

    @Test(groups={"UIREMEDIATION", "P1"},description = "SaaS Operation : It will create a folder with unique name in GDrive...")
    public void performFolderCreateInSaasApp() throws Exception {
        folderId = createFolder(qaTestFolderInCloud,gDriveAdmin);
        String expectedResult = "FolderId should not be NULL";
        String actualResult = folderId;
        Reporter.log("Expected Result :" + expectedResult, true);
        Reporter.log("Actual Result/folderId :" + actualResult, true);
        Assert.assertEquals(folderId != null, true, "Folder Not Created in GDrive...");
        wait(globalWaitTime,"Wait After folder Creation...");
    }

    @Test(groups={"UIREMEDIATION", "P1"},description = "SaaS Operation : Going to Upload a dynamically created file in to the folder..")
    public void performFileUploadInSaaSApp() throws InterruptedException, Exception {
        Reporter.log("********************* TEST DESCRIPTION *************************", true);
        Reporter.log("SaaS Operation : Going to Upload a dynamically created file in to the folder..", true);
        Reporter.log("****************************************************************", true);
        String expectedResult = "FileID should not be NULL";
        this.fileId_publicExposedTest=uploadFileWithMultipleAttempt(fileName,localFileLocation,qaTestFolderInCloud,gDriveAdmin);
        Reporter.log(" ## Expected Result :" + expectedResult, true);
        Reporter.log(" ## Actual Result/FILE ID :" + fileId_publicExposedTest, true);
        Assert.assertNotNull(fileId_publicExposedTest, " File Not Uploaded to GDrive...");
        wait(globalWaitTime,"Wait After file Upload...");
    }

    
    @Test(groups={"UIREMEDIATION", "P1"},dependsOnMethods = "performFileUploadInSaaSApp", description = "SaaS Operation : It will publicly expose the uploaded file...")
    public void publicExposedFile() throws Exception {
        // Publicly exposed file....
        Reporter.log("********************* TEST DESCRIPTION *************************", true);
        Reporter.log("It will publicly expose the uploaded file...", true);
        Reporter.log("****************************************************************", true);
        String expectedResult = "It should not be NULL";
        Permission insertPermission = gDriveAdmin.insertPermission(gDriveAdmin.getDriveService(), this.fileId_publicExposedTest, null, "anyone", "reader");
        String actualResult = insertPermission.toPrettyString();
        Reporter.log("Expected Result :" + expectedResult, true);
        Reporter.log("Actual Result/Insert Permission Response :" + actualResult, true);
        Assert.assertNotNull(actualResult, "Permision is found to be NULL");
    }

    @Test(groups={"UIREMEDIATION", "P1"},dependsOnMethods = "publicExposedFile", description = "It will fetch doc ID for publicly exposed file from the Elastica Search Engine..")
    public void validateUploadedFileInElasticaSearchEngine() throws Exception {
        Reporter.log("********************* TEST DESCRIPTION *************************", true);
        Reporter.log("It will fetch  and validate doc ID of publicly exposed file from the Elastica Search Engine...", true);
        Reporter.log("****************************************************************", true);
        wait(waitTimeToReceiveDocId, "Waiting Time after SaaS Activity to reach the event to Elastica Search Engine ...");// Wait for 5 mins
        this.docID = getDocID(this.fileName);
        String expectedResult = "Doc Id should not be NULL";
        String actualResult = this.docID;
        Reporter.log("Expected Result :" + expectedResult, true);
        Reporter.log("Actual Result/DocId :" + actualResult, true);
        if(actualResult==null){
            getSplunkResult();
        }
        Assert.assertNotNull(actualResult, " DocId is found NULL, Uploaded Exposed File is not found in Elastca Search Engine...");
    }

    @Test(groups={"UIREMEDIATION", "P1"},dependsOnMethods = "validateUploadedFileInElasticaSearchEngine", dataProviderClass = GDriveDataProvider.class,dataProvider = "dataProviderRemediationValues", description = " Perform Remediation actions and validate For actions applied from the SaaS App Side...")
    public void validateRemediation(String action, String value,String expectedResult,String currentLink) throws IOException, URISyntaxException, Exception {
        Reporter.log("********************* TEST DESCRIPTION *************************", true);
        Reporter.log("Perform Remediation actions and validate For actions applied from the SaaS App Side...", true);
        // Get Current Permission from Gdrive SaaS App after Public Expose.....
        Reporter.log("Remediation Test : Action ==>" + action + " ### Value :" + value, true);
        Reporter.log("****************************************************************", true);
        Reporter.log("=================== Before Remediation :======================", true);
        PermissionList permissionBeforeRemediation = gDriveAdmin.retrievePermissionList(fileId_publicExposedTest);
        Reporter.log("File Permission Before Remediation :" + permissionBeforeRemediation.toPrettyString(), true);
        wait(5, "Validating the Applicable Permission before Applying Remediation , Type Acceptable : domain or anyone , Type NOT Acceptable :user");
        List<String> expectedPermissionDomain = new ArrayList<>();
        expectedPermissionDomain.add("domain");
        boolean matchPermissionBeforeRemediationDomainCheck = matchPermission(permissionBeforeRemediation, expectedPermissionDomain);
        List<String> expectedPermissionPublic = new ArrayList<>();
        expectedPermissionPublic.add("anyone");
        boolean matchPermissionBeforeRemediationAnyoneCheck = matchPermission(permissionBeforeRemediation, expectedPermissionPublic);
        if ((matchPermissionBeforeRemediationDomainCheck || matchPermissionBeforeRemediationAnyoneCheck)) {
            if (matchPermissionBeforeRemediationDomainCheck) {
                Reporter.log("!! Domain Permission Found Before Remediation..OK to proceed for Remediation !!", true);
            } else {
                Reporter.log("!! Public Permission Found Before Remediation..OK to proceed for Remediation !!", true);
            }
        } else {
            Reporter.log("%% User Permission Found Before Remediation..Not Proceeding to apply for Remediation %%", true);
            return;
        }

        wait(10, "Wait Time Before Remediation..");
        String tenant = this.tenant;
        String user = this.user;
        String docId = docID;
        String docType = "file";
        String accessType = "SHARE_ACCESS";
        String newValue = value;

        if (action.equals("Remove link")) {
            // @TODO..
        }
        String generatePayLoadForGDriveRemediation;
        generatePayLoadForGDriveRemediation = generatePayLoadForGDriveRemediation(tenant, user, docId, docType, accessType, newValue,currentLink);
        Reporter.log("\nPayload For Gdrive Remediation :" + generatePayLoadForGDriveRemediation, true);
        HttpEntity entity = new StringEntity(generatePayLoadForGDriveRemediation);
        HttpResponse doPatch = restClient.doPatch(new URI(urlRemediation), headers, null, entity);
        StringBuffer result1 = getStringResponseFromHttpResponse(doPatch);
        String result_response1 = result1.toString();
        Reporter.log("Response :" + result_response1, true);
        Reporter.log("Remediation Applied.. ", true);
        Reporter.log("=============== After Remediation ================", true);
        wait(10, "Wait Time After Remediation....");
        // Check Updated Permission of this particular file from GDrive SaaS App..
        PermissionList retrievePermissionList = gDriveAdmin.retrievePermissionList(fileId_publicExposedTest);
        List<String> expectedPermission = new ArrayList(Arrays.asList(expectedResult.split(",")));
        boolean matchPermission = matchPermission(retrievePermissionList, expectedPermission);
        Reporter.log("Actual Permission / Actual Result / File Permission After Remediation :" + retrievePermissionList.toPrettyString(), true);
        Reporter.log("Expected Permission / Expected Result :" + expectedResult, true);
        Reporter.log(" ### Match Permission :" + matchPermission, true);
        Assert.assertTrue(matchPermission, " Expected Permission After Remediation is Not Applied in GDrive Files...");
    }

    long countBeforeBulkRemediation=0;
    long countAfterBulkRemediation=0;
    //Bulk Remediation../ Remove Access...of User... TEST..<End To End Test>
    @Test(groups={"UIREMEDIATION", "P1"})
    public void validateBulkRemediation() throws URISyntaxException, Exception {
        String fileNameExternalShare = uniqueIdentifier + "BulkRemediationTestRemediationTest";
        String insertFileId = uploadFileWithMultipleAttempt(fileNameExternalShare,localFileLocation,gDriveAdmin.getRootFolderID(),gDriveAdmin);

        Permission insertPermission = gDriveAdmin.insertPermission(gDriveAdmin.getDriveService(), insertFileId, "rahul.embeddedsystem@gmail.com", "user", "writer"); // Role : writer,reader
        Reporter.log(fileNameExternalShare + " File Insert Permission Response :" + insertPermission.toPrettyString(), true);
        wait(120, "Waiting after file Upload and Share....");
        countBeforeBulkRemediation=getExposedDataCountForAPartucularUser(gDriveExternalUser.getEmailId());
        boolean contains_beforeRemediation = gDriveAdmin.listFileNames().contains(fileNameExternalShare);
        // Perform Bulk Remediation..
        String bulkRemediationPayLoad = "{\"source\":{\"objects\":{\"objects\":[{\"db_name\":\"securletbeatlecom\",\"user\":\"__ALL_EL__\",\"user_id\":\"__ALL_EL__\",\"doc_id\":\"__ALL_EL__\",\"doc_type\":\"__ALL_EL__\",\"actions\":[{\"code\":\"COLLAB_REMOVE\",\"possible_values\":[],\"meta_info\":{\"collabs\":[\""
                + gDriveExternalUser.getEmailId()
                + "\"]},\"codeName\":\"Remove\"}]}]},\"app\":\"Google Apps\"}}";
        HttpResponse response = restClient.doPost(new URI(urlUIRemediation), getHeaders(), null, new StringEntity(bulkRemediationPayLoad));
        String responseBody = ClientUtil.getResponseBody(response);
        Reporter.log("Bulk Remediation Response :" + responseBody, true);
        wait(60, "Waiting after Bulk Remediation...");
        Reporter.log("Again Check the Files Shared in External User Account...", true);
        boolean contains_afterRemediation = gDriveExternalUser.listFileNames().contains(fileNameExternalShare);
        countAfterBulkRemediation=getExposedDataCountForAPartucularUser(gDriveExternalUser.getEmailId());
        Assert.assertEquals(contains_afterRemediation, false, " File should not present in External GDrive Account after Remediation");
    }

    @Test(groups={"UIREMEDIATION", "P2"})
    public void validateCountForExternalUserAfterBulkRemediation(){   
        Reporter.log("Count Before Bulk Remediation :"+countBeforeBulkRemediation,true);
        Reporter.log("Count After Bulk Remediation :"+countAfterBulkRemediation,true);
        Assert.assertTrue(countAfterBulkRemediation<=countBeforeBulkRemediation,"Count is not decremented");     
    }
}
