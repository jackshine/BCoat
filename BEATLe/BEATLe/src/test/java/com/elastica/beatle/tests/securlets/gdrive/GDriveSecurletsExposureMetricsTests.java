package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.tests.securlets.*;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.dto.BoxAction;
import com.elastica.beatle.securlets.dto.BoxMetaInfo;
import com.elastica.beatle.securlets.dto.BoxRemediation;
import com.elastica.beatle.securlets.dto.CiqProfile;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.TotalObject;
import com.elastica.beatle.securlets.dto.VlType;
import com.elastica.beatle.securlets.dto.VulnerabilityTypes;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.universal.common.GDrive;
import com.universal.common.GDriveAuthorization;
import com.universal.common.UniversalApi;
import com.universal.constants.CommonConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.Collaborations;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.IOReactorException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GDriveSecurletsExposureMetricsTests extends CommonTest {

    String url;
    SecurletsUtils securletsUtils;
    GDrive gDrive;
    List<String> childNodeList = new ArrayList<>();
    String environmentName;
    String parentNode;
    String activityType;
    String folderId;
    String fileId_publicExposedTest;
    String fileId_InternalExposedTest;
    int delayBetweenSaaSActivity;
    String qaTestFolderInCloud = "GDriveExposureTest";
    String localFileLocation;
    String fileToUpload = "glba-test.txt";
    String permissionId;
    Permission insertPermission;
    String externalUserId = "rahulsky.java@gmail.com";
    String internalUserId = "qa-admin@elasticaqa.net";
    String internalDomain = "elastica.co";
    String InternalSearchableTotalCount_before;
    URI uri;
    String riskyUrl;
    List<NameValuePair> headers;
    int waitAfterSaaSActivity;
    int waitforElasticaSearchToProcessLogs;
    String publicShareFile_searchableOnWeb_BeforetotalCount;
    long gobalSleepDuration = 60000;

    @BeforeClass(alwaysRun = true)
    public void oneTimeSetUp() throws Exception {
        initializeEnvironmentalVariable();
        initializeSaaSApp();
        initializeVariables();

    }

    public void initializeVariables() {
        localFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + fileToUpload;
        Reporter.log(" ## System File Location :" + localFileLocation);
    }

    private void initializeEnvironmentalVariable() throws IOException, InterruptedException, ExecutionException, KeyManagementException, NoSuchAlgorithmException, IOReactorException, KeyStoreException, URISyntaxException {
        Reporter.log("\n============== ## Initializing ENVIRONMENT SPECIFIC VARIABLES ## =======================", true);
        headers = getHeaders();
        securletsUtils = new SecurletsUtils();
        String hostName = suiteData.getApiserverHostName();
        String scheme = suiteData.getScheme();
        String hostUrl = scheme + "://" + hostName + "/";
        String tenant = suiteData.getTenantName();
        String path = "/api/admin/v1/el_google_apps/docs";
        url = hostUrl + tenant + path;
        riskyUrl = hostUrl + tenant + "/api/admin/v1/el_google_apps/risky_docs";
        Reporter.log("Scheme: " + suiteData.getScheme(), true);
        Reporter.log("Hostname: " + suiteData.getApiserverHostName(), true);
        Reporter.log("Tenant :" + suiteData.getTenantName(), true);
        Reporter.log("Exposure Metrics URL: " + url, true);
        delayBetweenSaaSActivity = Integer.parseInt(getRegressionSpecificSuitParameters("delayBetweenSaaSActivity"));
        waitAfterSaaSActivity = Integer.parseInt(getRegressionSpecificSuitParameters("waitPeriodForElasticSearchEngine"));
        waitforElasticaSearchToProcessLogs = waitAfterSaaSActivity;
    }

    public UserAccount getUserAccountForGdrive() throws IOException, GeneralSecurityException {
        String CLIENT_ID = getRegressionSpecificSuitParameters("gdriveClientId");
        String CLIENT_SECRET = getRegressionSpecificSuitParameters("gdriveClientSecret");
        String refreshToken = getRegressionSpecificSuitParameters("gdriveRefreshToken");
        GDriveAuthorization gDriveAuthorization = new GDriveAuthorization(CLIENT_ID, CLIENT_SECRET);
        UserAccount userAccount = new UserAccount("rahul.kumar@elastica.co", "123456", "ADMIN", gDriveAuthorization.getAceessTokenFromRefreshAccessToken(refreshToken));
        return userAccount;
    }

    public void initializeSaaSApp() throws IOException, GeneralSecurityException {
        Reporter.log("Initializing the SaaS App..............", true);
        System.setProperty("jsse.enableSNIExtension", "false");
        try {
            universalApi = new UniversalApi("GDRIVE", getUserAccountForGdrive());
            gDrive = universalApi.getgDrive();
            Drive.About about = gDrive.getDriveService().about();
            System.out.println("##### ==>" + about.toString());
        } catch (Exception ex) {
            System.out.println("Issue with creating Universal API " + ex.getLocalizedMessage());
        }
        Reporter.log("Initializing the SaaS App  !!! DONE !!! ", true);
    }

    @Test
    public void performFolderCreateInSaasApp() throws Exception {
        createFolder(qaTestFolderInCloud);
        Assert.assertEquals(folderId != null, true);
        securletsUtils.wait(delayBetweenSaaSActivity);
    }

    public String createFolder(String FolderLocationInCloud) throws Exception {
        Reporter.log("================= GDRIVE SaaS Actions : FOLDER CREATION  =====================", true);
        folderId = universalApi.createFolder(FolderLocationInCloud);
        System.out.println("Folder will be Created in Root Dir..");
        Reporter.log("Folder Name :" + FolderLocationInCloud + " ## Folder ID : " + folderId, true);
        return folderId;
    }

    @Test(groups={"EXPOSURE", "P1"})
    public void performFileUploadInSaaSApp() throws InterruptedException, Exception {
        uploadFile(localFileLocation);
        Assert.assertEquals(fileId_publicExposedTest != null, true);
        securletsUtils.wait(delayBetweenSaaSActivity);
    }

    public String uploadFile(String fileLocationInLocal) throws Exception {
        Reporter.log("================ GDRIVE SaaS Actions : UPLOAD FILE TO THE FOLDER  ===========", true);
        Reporter.log(" ## Uploading File :" + localFileLocation + " ## Cloud Folder :" + qaTestFolderInCloud, true);
        FileUploadResponse fileUploadResponse = universalApi.uploadFile(folderId, fileLocationInLocal);
        fileId_publicExposedTest = fileUploadResponse.getFileId();
        fileUploadResponse = universalApi.uploadFile(folderId, fileLocationInLocal);
        fileId_InternalExposedTest = fileUploadResponse.getFileId();
        Reporter.log(" ## FILE ID :" + fileId_publicExposedTest, true);
        return fileId_publicExposedTest;
    }

    public String getExposureMetricsResponse(Map<String, String> params) throws URISyntaxException, IOException, Exception, UnsupportedOperationException {
        // securletsUtils.wait(waitforElasticaSearchToProcessLogs);
        String toString = securletsUtils.getURI(url, params);
        HttpResponse doGet = restClient.doGet(new URI(toString), headers);
        StringBuffer result = securletsUtils.getStringResponseFromHttpResponse(doGet);
        String result_response = result.toString();
        System.out.println("Exposure Response :" + result_response);
        return result_response;
    }

    public String getRiskyDocResponse(Map<String, String> params) throws URISyntaxException, IOException, Exception, UnsupportedOperationException {
        // securletsUtils.wait(waitforElasticaSearchToProcessLogs);
        String toString = securletsUtils.getURI(riskyUrl, params);
        HttpResponse doGet = restClient.doGet(new URI(toString), headers);
        StringBuffer result = securletsUtils.getStringResponseFromHttpResponse(doGet);
        String result_response = result.toString();
        System.out.println("Exposure Response :" + result_response);
        return result_response;
    }

    @Test(groups={"EXPOSURE", "P1"})
    public void listAllInternallyExposedFiles() throws Exception {
        LogUtils.logTestDescription("Retrieve all internally exposed files and verify each and every field as per schema for all documents.");
        HashMap<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("exposures.types", "all_internal");
        Reporter.log("Fetching the GDrive exposed documents to find the total count.", true);
        Map<String, String> params = new HashMap<>();
        params.put("is_internal", "true");
        params.put("exposures.types", "all_internal,internal_searchable");
        params.put("exposed", "true");
        String exposureMetricsResponse = getExposureMetricsResponse(params);
        int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(exposureMetricsResponse, "$.meta.total_count"));
        Reporter.log("## Total Count :" + totalCount, true);
        //Fetch all the documents in one query
        int limit = totalCount;
        params.put("offset", "0");
        params.put("limit", String.valueOf(limit));
        Reporter.log("Fetching all the GDrive exposed documents", true);
        exposureMetricsResponse = getExposureMetricsResponse(params);
        //Verify all internally owned documents
        Reporter.log("Validating all the GDrive internally owned exposed documents...", true);
        Map<String, Object> expectedResult = new HashMap();
        List<Map<String, Object>> fetchAllKeys = RawJsonParser.fetchAllKeys(exposureMetricsResponse, "$.objects[*]");
        for (Map<String, Object> fetchAllKey : fetchAllKeys) {
            Assert.assertEquals(fetchAllKey.get("is_internal"), true, "Data is not Found Internal..");
        }
    }

    /**
     * Test 6
     *
     * List all internally exposed files after internal exposure
     *
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P1"})
    public void listAllInternallyExposedFilesAfterInternalExposure() throws Exception {
        LogUtils.logTestDescription("Retrieve all internally exposed files and verify only the exposed document and its fields.");
        HashMap<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("exposures.types", "all_internal");
        additionalParams.put("offset", "0");
        additionalParams.put("limit", "1000");
        Reporter.log("Getting all the documents ...", true);
        String exposureMetricsResponse = getExposureMetricsResponse(additionalParams);
        int countBefore = Integer.parseInt(RawJsonParser.getSingleKey(exposureMetricsResponse, "$.meta.total_count"));
        Reporter.log("## Total Count :" + countBefore, true);
        Reporter.log("Upload a file and share it ...", true);
        //Expose a document internally and check the count
        FileUploadResponse fileUploadResponse = universalApi.uploadFile(folderId, localFileLocation, "InternalExposureCountTest");
        Permission insertPermission = gDrive.insertPermissionWithWebLink(fileUploadResponse.getFileId(), internalDomain, "domain", "reader");
        //Wait for three mins
        sleep(CommonConstants.THREE_MINUTES_SLEEP);
        exposureMetricsResponse = getExposureMetricsResponse(additionalParams);
        int countAfter = Integer.parseInt(RawJsonParser.getSingleKey(exposureMetricsResponse, "$.meta.total_count"));
        //validate the newly added record count
        Reporter.log("Verifying the count before and after exposure ...", true);
        CustomAssertion.assertEquals(countAfter, countBefore + 1, "Internally exposed document not returned");

        Map<String, Object> expectedResult = new HashMap();

        expectedResult.put("name", "InternalExposureCountTest");

        boolean findExpectedKeysAndValues = RawJsonParser.findExpectedKeysAndValues(exposureMetricsResponse, "$.objects[*]", expectedResult, "name");

        Reporter.log("Disable the sharing on the file ...", true);
        //remove the exposure
        gDrive.removePermission(fileUploadResponse.getFileId(), insertPermission.getId());
        //Wait for three mins
        sleep(CommonConstants.THREE_MINUTES_SLEEP);
        exposureMetricsResponse = getExposureMetricsResponse(additionalParams);
        int countAfterRemovePermission = Integer.parseInt(RawJsonParser.getSingleKey(exposureMetricsResponse, "$.meta.total_count"));

        CustomAssertion.assertEquals(countAfterRemovePermission, countBefore, "Internally exposed document count not decremented");

    }

    /**
     * Test 3 1. Upload a risk file. 2. Expose it publically and check the file
     * classified as risk file and check exposed files. 3. Also check in other
     * risks. It should not listed there. 4. Remove the exposure. Check the file
     * listed in other risks but not in Exposed files
     *
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P1"})
    public void exposeRiskFileAndCheckExposedFilesAndOtherRisks() throws Exception {
        String steps[] = {
            "1. This test verify the exposed document list after uploading and exposing a document.",
            "2. Upload a risk file and expose it publically.",
            "3. Verify the publicly shared document is present in the exposed files tab.",
            "4. Verify the publicly shared document is not present in the other risks tab.",
            "5. Remove the exposure.",
            "6. Verify the publicly shared document is not present in the exposed files tab.",
            "7. Verify the publicly shared document is present in the other risks tab.",};
        LogUtils.logTestDescription(steps);
        //Create a file and share it
        Reporter.log("## 1. This test verify the exposed document list after uploading and exposing a document.");
        String fileName = "ExposeRiskFileTest_" + UUID.randomUUID();
        Reporter.log("## 2. Upload a risk file <" + fileName + "> and expose it publically.", true);
        Reporter.log("## 2.1  Uploading the risk the file .", true);
        FileUploadResponse fileUploadResponse = universalApi.uploadFile(folderId, localFileLocation, fileName);
        try {
            Reporter.log("## 2.2  Exposing the file  publically.", true);
            Permission insertPermission = gDrive.insertPermissionWithWebLink(fileUploadResponse.getFileId(), null, "anyone", "reader");
            //Wait for three mins
            sleep(gobalSleepDuration);
            //Get the exposed documents and check the document is publicly exposed
            HashMap<String, String> additionalParams = new HashMap<String, String>();
            additionalParams.put("is_internal", "true");
            additionalParams.put("exposures.types", "public");
            additionalParams.put("offset", "0");
            additionalParams.put("limit", "1000");
            Reporter.log("Getting all the documents ...", true);
            String exposureMetricsResponse = getExposureMetricsResponse(additionalParams);
            Reporter.log("## 3. Verify the publicly shared document is present in the exposed files tab. ## After exposure, checking the publicly shared document is present in the exposed files tab...", true);
            Map<String, Object> expectedResult = new HashMap();
            expectedResult.put("name", fileName);
            boolean findExpectedKeysAndValues = RawJsonParser.findExpectedKeysAndValuesIgnoreNullCheck(exposureMetricsResponse, "$.objects[*]", expectedResult, "name");
            if (findExpectedKeysAndValues) {
                Reporter.log("## Public Shared File Found in the Exposed Files Tab...", true);
            }
            Assert.assertTrue(findExpectedKeysAndValues, "Publicly shared document is NOT present in the exposed files tab");
            // Get the other risk and check the document it is not listed, Ignored For Now...
            Reporter.log("## 4. Verify the publicly shared document is not present in the other risks tab. ## After exposure, checking the publicly shared document is present in the other risks tab...", true);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("is_internal", "true");
            params.put("name", fileName);
            params.put("app", "Google Apps");
            Reporter.log("Getting all the Risky documents ...", true);
            String getResponse_riskyDocs = getRiskyDocResponse(params);
            int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(getResponse_riskyDocs, "$.meta.total_count"));
            Assert.assertTrue(totalCount == 0, "========= Publicly shared document is present in the other risks tab ========");

            Reporter.log("Disabling the share on  ", true);
            Reporter.log(" ## 5. Remove the exposure.", true);
            gDrive.removePermission(fileUploadResponse.getFileId(), insertPermission.getId());
            //3 mins sleep
            sleep(gobalSleepDuration);

            Reporter.log("## 6. Verify the publicly shared document is not present in the exposed files tab. ## After removing exposure, checking the publicly shared document is still present in the exposed files tab...", true);
            exposureMetricsResponse = getExposureMetricsResponse(additionalParams);
            findExpectedKeysAndValues = RawJsonParser.findExpectedKeysAndValuesIgnoreNullCheck(exposureMetricsResponse, "$.objects[*]", expectedResult, "name");
            Assert.assertTrue(!findExpectedKeysAndValues, "Publicly shared document is present in the exposed files tab");

            Reporter.log("## 7. Verify the publicly shared document is present in the other risks tab. ,After removing exposure, checking the exposure moved to other risk...", true);
            getResponse_riskyDocs = getRiskyDocResponse(params);
            Reporter.log("Risky Doc Response :" + getResponse_riskyDocs, true);
            totalCount = Integer.parseInt(RawJsonParser.getSingleKey(getResponse_riskyDocs, "$.meta.total_count"));
            Reporter.log("Total Risky Doc Count:" + totalCount, true);
            Assert.assertTrue(totalCount == 0, " Publicly shared document is present in the other risks tab");
        } finally {
            gDrive.deleteFile(fileUploadResponse.getFileId());
        }
    }

    /**
     * Test 4 1. Upload a risk file. 2. Expose it publically and check the file
     * classified as risk file and check exposed files. 3. Also check in other
     * risks. It should not listed there. 4. Remove the exposure. Check the file
     * listed in other risks but not in Exposed files
     *
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P1"})
    public void exposeNonRiskFileAndCheckExposedFilesAndOtherRisks() throws Exception {

        String steps[] = {
            "1. This test verify the exposed document list after uploading and exposing a document.",
            "2. Upload a nonrisk file and expose it publically.",
            "3. Verify the publicly shared document is present in the exposed files tab.",
            "4. Verify the publicly shared document is not present in the other risks tab.",
            "5. Remove the exposure.",
            "6. Verify the publicly shared document is not present in the exposed files tab.",
            "7. Verify the publicly shared document is not present in the other risks tab.",};

        LogUtils.logTestDescription(steps);
        Reporter.log("## 1. This test verify the exposed document list after uploading and exposing a document.", true);
        localFileLocation = "src/test/resources/uploads/box" + File.separator + "test.pdf";
        String fileName = "ExposeNonRiskFileTest_" + UUID.randomUUID();
        Reporter.log("## 2. Upload a nonrisk file and expose it publically.", true);
        Reporter.log("## 2.1 Uploading a nonrisk file <" + fileName + "> and expose it publically.", true);
        FileUploadResponse fileUploadResponse = universalApi.uploadFile(folderId, localFileLocation, fileName);
        try {
            Reporter.log("## 2.2 Exposing it publically.", true);
            Permission insertPermission = gDrive.insertPermissionWithWebLink(fileUploadResponse.getFileId(), null, "anyone", "reader");
            //Wait for three mins
            sleep(gobalSleepDuration);
            //Get the exposed documents and check the document is publicly exposed
            HashMap<String, String> additionalParams = new HashMap<String, String>();
            additionalParams.put("is_internal", "true");
            additionalParams.put("exposures.types", "public");
            additionalParams.put("offset", "0");
            additionalParams.put("limit", "1000");
            Reporter.log("Getting all the documents ...", true);
            String exposureMetricsResponse = getExposureMetricsResponse(additionalParams);
            Reporter.log("## 3. Verify the publicly shared document is present in the exposed files tab.After exposure, checking the publicly shared document is present in the exposed files tab...", true);
            Map<String, Object> expectedResult = new HashMap();
            expectedResult.put("name", fileName);
            boolean findExpectedKeysAndValues = RawJsonParser.findExpectedKeysAndValuesIgnoreNullCheck(exposureMetricsResponse, "$.objects[*]", expectedResult, "name");
            if (findExpectedKeysAndValues) {
                Reporter.log("Publicly FOUND in the Exposed Files Tab...", true);
            }
            Assert.assertTrue(findExpectedKeysAndValues, "Publicly shared document is NOT present in the exposed files tab");

            // Get the other risk and check the document it is not listed, Ignored For Now...
            Reporter.log("## 4. Verify the publicly shared document is not present in the other risks tab.,After exposure, checking the publicly shared document is present in the other risks tab...", true);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("is_internal", "true");
            params.put("name", fileName);
            params.put("app", "Google Apps");
            Reporter.log("Getting all the Risky documents ...", true);
            String getResponse_riskyDocs = getRiskyDocResponse(params);
            int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(getResponse_riskyDocs, "$.meta.total_count"));
            Assert.assertTrue(totalCount == 0, " Publicly shared document is present in the other risks tab");

            Reporter.log("## 5. Remove the exposure.Disabling the share on  ", true);
            gDrive.removePermission(fileUploadResponse.getFileId(), insertPermission.getId());
            //3 mins sleep
            sleep(gobalSleepDuration);

            Reporter.log("## 6. Verify the publicly shared document is not present in the exposed files tab.After removing exposure, checking the publicly shared document is still present in the exposed files tab...", true);
            exposureMetricsResponse = getExposureMetricsResponse(additionalParams);
            findExpectedKeysAndValues = RawJsonParser.findExpectedKeysAndValuesIgnoreNullCheck(exposureMetricsResponse, "$.objects[*]", expectedResult, "name");
            Assert.assertTrue(!findExpectedKeysAndValues, "Publicly shared document is present in the exposed files tab");

            Reporter.log("## 7. Verify the publicly shared document is not present in the other risks tab.After removing exposure, checking the exposure moved to other risk...", true);
            getResponse_riskyDocs = getRiskyDocResponse(params);
            Reporter.log("Risky Doc Response :" + getResponse_riskyDocs, true);

            totalCount = Integer.parseInt(RawJsonParser.getSingleKey(getResponse_riskyDocs, "$.meta.total_count"));
            Reporter.log("Total Risky Doc Count:" + totalCount, true);
            Assert.assertTrue(totalCount == 0, " Publicly shared document is present in the other risks tab");

        } finally {
            gDrive.deleteFile(fileUploadResponse.getFileId());
        }
    }

    /**
     * Test 7
     *
     * Get only the internal exposure totals And verify the internal count
     * incremented after one internal exposure Also it should be decremented
     * when the exposure is deleted
     *
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P1"})
    public void getAllInternalExposureTotalsOnly() throws Exception {
        LogUtils.logTestDescription("1. This test verify the Total Internal count after Internal exposure,\n"
                + "2. Upload a risk file and expose it internally.\",\n"
                + "3. Verify Internally Total Internal count is incremented by one\n"
                + "4. Remove the exposure.\",\n"
                + "6. Verify Internally Total Internal count is decremented by one");
        HashMap<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("file_exposures", "internal");
        //String beforeExposureTotals = getExposureMetricsResponse(additionalParams);

        int beforeExposureTotals = Integer.parseInt(RawJsonParser.getSingleKey(getExposureMetricsResponse(additionalParams), "$.meta.total_count"));

        //Expose a document internally and check the count
        FileUploadResponse fileUploadResponse = universalApi.uploadFile(folderId, "src/test/resources/uploads/box" + File.separator + "Hello.java", "ExposeNonRiskFileTest");
        Permission insertPermission_internalExposure = gDrive.insertPermissionWithWebLink(fileUploadResponse.getFileId(), internalDomain, "domain", "reader");

        //Wait for three mins
        sleep(gobalSleepDuration);

        int afterExpsoureTotals = Integer.parseInt(RawJsonParser.getSingleKey(getExposureMetricsResponse(additionalParams), "$.meta.total_count"));

        CustomAssertion.assertEquals(afterExpsoureTotals, beforeExposureTotals + 1, "Internally exposed document count not incremented");

        //remove the exposure
        //sharedFile = universalApi.disableSharedLink(sharedFile.getId());
        gDrive.removePermission(fileUploadResponse.getFileId(), insertPermission_internalExposure.getId());

        //Wait for three mins
        sleep(gobalSleepDuration);
        afterExpsoureTotals = Integer.parseInt(RawJsonParser.getSingleKey(getExposureMetricsResponse(additionalParams), "$.meta.total_count"));

        CustomAssertion.assertEquals(afterExpsoureTotals, beforeExposureTotals, "Internally exposed document count not decrmented");

        //clean up file
        gDrive.deleteFile(fileUploadResponse.getFileId());
    }

    /**
     * Test 8
     *
     * Verify the top 10 exposed file types and check count is always greater
     * than zero
     *
     *
     */
    @Test(groups={"EXPOSURE", "P1"})
    public void verifyTopExposedFileTypes() throws Exception {

        LogUtils.logTestDescription("Get top exposed file types");

        //After external exposure
        HashMap<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("is_internal", "true");
        additionalParams.put("top", "10");
        String exposureTotals = getExposureMetricsResponse(additionalParams);
        int beforeExposureTotals = Integer.parseInt(RawJsonParser.getSingleKey(exposureTotals, "$.meta.total_count"));
        CustomAssertion.assertTrue(beforeExposureTotals >= 0, "File type exposure is less than zero");
    }

    /**
     * Test 9 TODO...
     *
     * Expose a specific file and check the exposure count is incremented for
     * the file type
     *
     *
     */
    @Test(groups={"EXPOSURE", "P2"},dataProviderClass = GDriveDataProvider.class, dataProvider = "fileTypesExposuresTotal")
    public void exposeAFileTypeAndVerifyExposedFileTypes(String testname, String folderId, String filename, String access, String filetype, String server) throws Exception {
        LogUtils.logTestDescription("Expose a specific file and check the exposure count is incremented for the file type");
        //before exposure
        ExposureTotals exposedFileTypes = getExposedFileTypes("el_google_apps", "true", "10");
        HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();

        for (TotalObject totalObject : exposedFileTypes.getObjects()) {
            beforeMap.put(totalObject.getId(), totalObject.getCount());
        }

        //Expose the file Internally
        //Expose a document internally and check the count
        FileUploadResponse fileUploadResponse = universalApi.uploadFile(this.folderId, "src/test/resources/uploads/box" + File.separator + filename);
        Permission insertPermission_internalExposure = gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), internalDomain, "domain", "reader");

        sleep(gobalSleepDuration);

        //After the exposure get the count
        exposedFileTypes = getExposedFileTypes("el_google_apps", "true", "10");
        HashMap<String, Integer> afterMap = new HashMap<String, Integer>();

        for (TotalObject totalObject : exposedFileTypes.getObjects()) {
            afterMap.put(totalObject.getId(), totalObject.getCount());
        }

        for (String key : afterMap.keySet()) {
            Reporter.log("Verifying the type:" + key, true);
            if (key.equals(filetype)) {
                int expectedValue = beforeMap.containsKey(key) ? beforeMap.get(key) + 1 : 0;
                CustomAssertion.assertEquals(afterMap.get(key), expectedValue, key + " count not added after expsoure");
            } else {
                CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key), key + " count should not get incremented for no expsoure");
            }
        }

        //Disable shared link
        gDrive.removePermission(fileUploadResponse.getFileId(), insertPermission_internalExposure.getId());
        sleep(gobalSleepDuration);

        //After the exposure get the count
        exposedFileTypes = getExposedFileTypes("el_google_apps", "true", "10");
        afterMap = new HashMap<String, Integer>();

        for (TotalObject totalObject : exposedFileTypes.getObjects()) {
            afterMap.put(totalObject.getId(), totalObject.getCount());
        }

        for (String key : afterMap.keySet()) {
            Reporter.log("Verifying the count after removing exposure :" + key, true);
            CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key), key + " count should be decremented after delete expsoure");
        }
    }

    /**
     * Test 10
     *
     * Get all internally exposed content type filter like legal, executable,
     * health, image, business, video, design
     *
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P2"})
    public void getAllInternallyExposedContentTypes() throws Exception {
        LogUtils.logTestDescription("Get all internally exposed content types.");

        HashMap<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("is_internal", "true");

        //ExposureTotals expsoureTotals = 
        ExposureTotals exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);

        CustomAssertion.assertTrue(exposedContentTypes.getObjects().size() >= 0, "File type exposure is less than zero");

        for (TotalObject totalObject : exposedContentTypes.getObjects()) {
            CustomAssertion.assertTrue(totalObject.getId() != null, "Id is null");
            CustomAssertion.assertTrue(totalObject.getTotal() >= 0, "Count is less than or equal to zero");
        }
    }

    /**
     * Test 11
     *
     * Expose a specific content type and check the exposure count is
     * incremented for the content type
     *
     *
     */
    @Test(groups={"EXPOSURE", "P2"},dataProviderClass = GDriveDataProvider.class, dataProvider = "contentTypesExposuresTotal")
    public void exposeAFileTypeAndVerifyExposedContentTypes(String testname, String filename,
            String access, String filetype) throws Exception {

        LogUtils.logTestDescription("Expose a content type and check exposure count is incremented for the content type.");

        Reporter.log("Started " + testname, true);

        HashMap<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("is_internal", "true");

        //before exposure
        ExposureTotals exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);
        HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();

        for (TotalObject totalObject : exposedContentTypes.getObjects()) {
            beforeMap.put(totalObject.getId(), totalObject.getTotal());
        }

        //Expose the file
        //FileEntry sharedFile = this.uploadFileAndShareit(folderId, filename, access);
        FileUploadResponse fileUploadResponse = universalApi.uploadFile(this.folderId,
                "src/test/resources/uploads/box" + File.separator + filename);
        Permission insertPermission_internalExposure = gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), internalDomain, "domain", "reader");

        sleep(gobalSleepDuration);
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);

        //After the exposure get the count
        exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);
        HashMap<String, Integer> afterMap = new HashMap<String, Integer>();

        for (TotalObject totalObject : exposedContentTypes.getObjects()) {
            afterMap.put(totalObject.getId(), totalObject.getTotal());
        }

        for (String key : afterMap.keySet()) {
            Reporter.log("Verifying the type:" + key, true);
            if (key.equals(filetype)) {
                CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key) + 1, key + " count not added after expsoure");
            } else {
                int expectedValue = (beforeMap.get(key) == null) ? 0 : beforeMap.get(key);
                CustomAssertion.assertEquals(afterMap.get(key), expectedValue, key + " count should not get incremented for no expsoure");
            }
        }

        //Disable shared link
        gDrive.removePermission(fileUploadResponse.getFileId(), insertPermission_internalExposure.getId());
        sleep(gobalSleepDuration);

        //After the exposure get the count
        exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);
        afterMap = new HashMap<String, Integer>();

        for (TotalObject totalObject : exposedContentTypes.getObjects()) {
            afterMap.put(totalObject.getId(), totalObject.getTotal());
        }

        for (String key : afterMap.keySet()) {
            Reporter.log("Verifying the type:" + key, true);
            CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key), key + " count should be decremented after delete expsoure");
        }
    }

    /**
     * Test 12
     *
     * Check the vulnerability types and ciq profile filters
     *
     *
     */
    @Test(groups={"EXPOSURE", "P2"})
    public void verifyVulnerabilityTypes() throws Exception {

        LogUtils.logTestDescription("Get the vulnerability types and verify them");
        Logger.info("Getting vulnerability types ...");

        HashMap<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("is_internal", "true");
        additionalParams.put("vl_types", "all");

        //After external exposure
        VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);

        String vulnerabilities[] = {"pci", "pii", "hipaa", "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};

        for (String vl : vulnerabilities) {
            Reporter.log("Verifying the vulnerability type:" + vl, true);
            Reporter.log("Vulnerability:" + vl + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(vl), true);
            CustomAssertion.assertTrue(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) >= 0, "Count is less than zero");
        }

        for (CiqProfile ciqprofile : vulnerabilityTypes.getObjects().getCiqProfiles()) {
            Reporter.log("Verifying the CIQ profile name:" + ciqprofile.getId(), true);
            Reporter.log("Ciq Profile name:" + ciqprofile.getId() + ":: Count:" + ciqprofile.getTotal(), true);
            CustomAssertion.assertTrue(ciqprofile.getId() != null, "CIQ profile id is null");
            CustomAssertion.assertTrue(ciqprofile.getTotal() >= 0, "CIQ profile total is null");
        }
    }

    /**
     * Test 13 Expose a file of particular vulnerability and check the exposure
     * count
     *
     * @param testname
     * @param folderId
     * @param filename
     * @param access
     * @param filetype
     * @param server
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P1"},dataProviderClass = GDriveDataProvider.class, dataProvider = "vulnerabilityTypesExposuresTotal")
    public void exposeAFileTypeAndVerifyVulnerabilityTypes(String testname, String filename, String access, String filetype, String server) throws Exception {

        LogUtils.logTestDescription("Expose a file of particular vulnerability and check the exposure count gets incremented");

        Reporter.log("Started " + testname, true);

        HashMap<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("is_internal", "true");

        //before exposure
        VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
        HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();

        for (VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
            beforeMap.put(totalObject.getId(), totalObject.getTotal());
        }

        //Expose the file
        //FileEntry sharedFile = this.uploadFileAndShareit(folderId, filename, access);
        FileUploadResponse fileUploadResponse = universalApi.uploadFile(this.folderId,
                "src/test/resources/uploads/box" + File.separator + filename);
        Permission insertPermission_internalExposure = gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), internalDomain, "domain", "reader");

        sleep(gobalSleepDuration);

        //After the exposure get the count
        vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
        HashMap<String, Integer> afterMap = new HashMap<String, Integer>();

        for (VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
            afterMap.put(totalObject.getId(), totalObject.getTotal());
        }

        for (String key : afterMap.keySet()) {
            Reporter.log("Verifying the type:" + key, true);
            if (key.equals(filetype)) {
                CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key) + 1, key + " count not added after expsoure");
            } else {
                CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key), key + " count should not get incremented for no expsoure");
            }
        }

        // Disable shared link
        // universalApi.disableSharedLink(sharedFile.getId());
        gDrive.removePermission(fileUploadResponse.getFileId(), insertPermission_internalExposure.getId());
        sleep(gobalSleepDuration);

        //After the exposure get the count
        vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
        afterMap = new HashMap<String, Integer>();

        for (VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
            afterMap.put(totalObject.getId(), totalObject.getTotal());
        }

        for (String key : afterMap.keySet()) {
            Reporter.log("Verifying the type:" + key, true);
            CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key), key + " count should be decremented after delete expsoure");
        }
    }

    /**
     * Test 14
     *
     * Check the vulnerability types and ciq profile filters
     *
     *
     */
    @Test(groups={"EXPOSURE", "P2"})
    public void verifyVulnerabilityTypesWithFilters() throws Exception {

        Logger.info("Getting vulnerability types ...");

        HashMap<String, String> additionalParams = new HashMap<String, String>();
        additionalParams.put("is_internal", "true");
        additionalParams.put("vl_types", "all");

        HashMap<String, Integer> vlmap = new HashMap<String, Integer>();

        //getVulnerabilityTypes
        VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_box", additionalParams);

        String vulnerabilities[] = {"pci", "pii", "hipaa", "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};

        for (String vl : vulnerabilities) {
            vlmap.put(vl, vulnerabilityTypes.getObjects().getVulnerabilityCount(vl));
        }

        for (CiqProfile ciqprofile : vulnerabilityTypes.getObjects().getCiqProfiles()) {
            vlmap.put(ciqprofile.getId(), ciqprofile.getTotal());
        }

        //Filter it
        for (String vl : vulnerabilities) {
            additionalParams.clear();
            additionalParams.put("is_internal", "true");
            additionalParams.put("vl_types", vl);
            vulnerabilityTypes = getVulnerabilityTypes("el_box", additionalParams);
            CustomAssertion.assertEquals(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl), vlmap.get(vl), vl + " count should be equal");
        }
    }

    /**
     * Test 15
     *
     * Check the vulnerability types and ciq profile filters Expose a document
     * by a user and check the document count
     *
     */
    @Test(groups={"EXPOSURE", "P1"},dataProviderClass = GDriveDataProvider.class, dataProvider = "metricsExposuresTotal")
    public void verifyInternallyExposedUsers(String testname, String folderId, String fileName, String access, String exposureType, String server) throws Exception {

        // FileEntry sharedFile = null;
        FileUploadResponse fileUploadResponse = null;
        try {
            String steps[] = {
                "1. This test verify the metrics as depicted in venn diagram for users",
                "2. Upload a file and share it with " + access + " access.",
                "3. Verify the document count for an user get incremented after the exposure."};

            LogUtils.logTestDescription(steps);
            HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
            List<NameValuePair> docparams = new ArrayList<NameValuePair>();
            docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL, Boolean.TRUE.toString()));
            docparams.add(new BasicNameValuePair(SecurletsConstants.APP, "Drive"));
            //docparams.add(new BasicNameValuePair("name",  sharedFile.getName()));

            SecurletDocument documents = getExposedUsers(SecurletUtils.elapp.el_google_apps.name(), docparams);
            CustomAssertion.assertTrue(documents.getObjects().size() >= 0, "Exposed user documents should be greater than or equal to zero");

            for (com.elastica.beatle.securlets.dto.Object doc : documents.getObjects()) {
                beforeMap.put(doc.getEmail(), doc.getDocsExposed());
            }

            //As an internal user expose a document
            //Create a file and share it
            //sharedFile = uploadFileAndShareit(folderId, fileName, access);
            fileUploadResponse = universalApi.uploadFile(this.folderId,
                    "src/test/resources/uploads/box" + File.separator + fileName);
            Permission insertPermission_Exposure = gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), "rahulsky.java@gmail.com", "user", "reader");

            //3 mins sleep
            sleep(60000);

            HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
            //After  exposure
            Reporter.log("Verifying Internal user exposed document count after the exposure", true);
            documents = getExposedUsers(SecurletUtils.elapp.el_google_apps.name(), docparams);
            for (com.elastica.beatle.securlets.dto.Object doc : documents.getObjects()) {
                afterMap.put(doc.getEmail(), doc.getDocsExposed());
            }

            CustomAssertion.assertEquals(afterMap.get("admin@securletbeatle.com"), beforeMap.get("admin@securletbeatle.com") + 1,
                    "Exposed file count for user doesn't match");

            //Delete the shared link and check the count. It should be decremented by one
            // sharedFile = universalApi.disableSharedLink(sharedFile.getId());
            gDrive.removePermission(fileUploadResponse.getFileId(), insertPermission_Exposure.getId());

            //3 mins sleep
            sleep(gobalSleepDuration);
            //After  removing exposure
            Reporter.log("Verifying Internal user exposed document count after removing the exposure", true);
            documents = getExposedUsers(SecurletUtils.elapp.el_google_apps.name(), docparams);
            for (com.elastica.beatle.securlets.dto.Object doc : documents.getObjects()) {
                afterMap.put(doc.getEmail(), doc.getDocsExposed());
            }

            CustomAssertion.assertEquals(afterMap.get("admin@securletbeatle.com"), beforeMap.get("admin@securletbeatle.com"),
                    "Exposed file count for user doesn't match");

        } finally {
            gDrive.deleteFile(fileUploadResponse.getFileId());
        }
    }

    /**
     * Test 16
     *
     * @param testname
     * @param fileName
     * @param access
     * @param exposureType
     * @param collaborators
     * @param server
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P1"}, dataProviderClass = BoxDataProvider.class, dataProvider = "metricsFolderExposuresTotal")
    public void verifyFolderExposuresByInternalUsers(String testname, String fileName, String access,
            String exposureType, String[] collaborators, String server) throws Exception {

        String steps[] = {
            "1. This test verify the metrics as depicted in venn diagram for users",
            "2. Upload a folder and file and share it with " + access + " access.",
            "3. Verify the document count for an user get incremented after the exposure."};

        LogUtils.logTestDescription(steps);

        BoxFolder sharedFolder = null;
        String uniqueFolderId = UUID.randomUUID().toString();

        Reporter.log("Started test " + testname, true);

        //create folder1
//		BoxFolder folderObj = universalApi.createFolder(uniqueFolderId);
//		String folderId = folderObj.getId();
        String createFolderId = gDrive.createFolder("FolderExposuresByInternalUsers" + UUID.randomUUID());

        try {

            HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
            List<NameValuePair> docparams = new ArrayList<NameValuePair>();
            docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL, Boolean.TRUE.toString()));
            docparams.add(new BasicNameValuePair(SecurletsConstants.APP, "Drive"));

            SecurletDocument documents = getExposedUsers(SecurletUtils.elapp.el_google_apps.name(), docparams);
            CustomAssertion.assertTrue(documents.getObjects().size() >= 0, "Exposed user documents should be greater than or equal to zero");

            for (com.elastica.beatle.securlets.dto.Object doc : documents.getObjects()) {
                beforeMap.put(doc.getEmail(), doc.getDocsExposed());
            }

            //Share the folder as specified in data providers
            Reporter.log("Exposing the folder with a file...", true);

            //sharedFolder = shareTheFolderPubliclyOrWithCollaborators(folderId, fileName, access, collaborators);
            FileUploadResponse fileUploadResponse = universalApi.uploadFile(createFolderId,
                    "src/test/resources/uploads/box" + File.separator + fileName);
            Permission insertPermission_internalExposure = gDrive.insertPermission(gDrive.getDriveService(), createFolderId, internalDomain, "domain", "reader");

            //3 mins sleep
            sleep(gobalSleepDuration);

            //After exposure
            HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
            //After  exposure
            Reporter.log("Verifying Internal user exposed document count after the exposure", true);
            documents = getExposedUsers(SecurletUtils.elapp.el_google_apps.name(), docparams);
            for (com.elastica.beatle.securlets.dto.Object doc : documents.getObjects()) {
                afterMap.put(doc.getEmail(), doc.getDocsExposed());
            }

            CustomAssertion.assertEquals(afterMap.get(suiteData.getSaasAppUsername()), beforeMap.get(suiteData.getSaasAppUsername()) + 2,
                    "Exposed file/folder count for user doesn't match");

            //Delete the shared link and check the count. It should be decremented by one
            //sharedFolder = universalApi.disableSharedLinkForFolder(sharedFolder.getId());
            gDrive.removePermission(createFolderId, insertPermission_internalExposure.getId());

//			Collaborations collaborations = universalApi.getFolderCollaborations(folderId);
//			
//			for (BoxCollaboration collaboration : collaborations.getEntries()) {
//				universalApi.deleteCollaboration(collaboration);
//			}
            sleep(gobalSleepDuration);

            //After  removing exposure
            Reporter.log("Verifying Internal user exposed document count after removing the exposure", true);
            documents = getExposedUsers(SecurletUtils.elapp.el_google_apps.name(), docparams);
            for (com.elastica.beatle.securlets.dto.Object doc : documents.getObjects()) {
                afterMap.put(doc.getEmail(), doc.getDocsExposed());
            }

            CustomAssertion.assertEquals(afterMap.get(suiteData.getSaasAppUsername()), beforeMap.get(suiteData.getSaasAppUsername()),
                    "Exposed file count for user doesn't match");
        } finally {
            gDrive.deleteFile(createFolderId);
        }
    }

    /**
     * Test 17
     *
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P2"})
    public void verifyUserTotalsAsInVennDiagram() throws Exception {
        LogUtils.logTestDescription("This test verify the user totals as in venn diagram");
        Logger.info("Getting user totals ...");
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL, Boolean.TRUE.toString()));
        qparams.add(new BasicNameValuePair(SecurletsConstants.APP, "Drive"));

        ExposureTotals exposureTotals = getUserTotals(SecurletUtils.elapp.el_google_apps.name(), qparams);

        Reporter.log("Verifying the internal user totals is greater than zero", true);
        for (TotalObject totalObject : exposureTotals.getObjects()) {
            CustomAssertion.assertTrue(totalObject.getTotal() >= 0, totalObject.getId() + " user total >= 0", totalObject.getId() + " user total < 0");
        }
    }

    /**
     * Test 18
     *
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P1"})
    public void verifyUserDocumentExposures() throws Exception {
        LogUtils.logTestDescription("This test verify the user documemt exposures");
        Logger.info("Getting user totals ...");
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL, Boolean.TRUE.toString()));
        qparams.add(new BasicNameValuePair(SecurletsConstants.APP, "Drive"));

        SecurletDocument documents = getUserExposures(SecurletUtils.elapp.el_google_apps.name(), qparams);
        for (com.elastica.beatle.securlets.dto.Object object : documents.getObjects()) {
            CustomAssertion.assertTrue(object.getTotal() >= 0, "User document exposure total can't be null");
            Reporter.log(String.valueOf(object.getTotal()), true);
        }
    }

    /**
     * Test 19
     *
     * @throws Exception
     */
    @Test(groups={"EXPOSURE", "P1"})
    public void verifyUserVulnerabilities() throws Exception {
        LogUtils.logTestDescription("This test verify the user vulnerabilities");

        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL, Boolean.TRUE.toString()));
        qparams.add(new BasicNameValuePair(SecurletsConstants.APP, "Drive"));

        SecurletDocument documents = getUserVulnerabilities(SecurletUtils.elapp.el_google_apps.name(), qparams);
        for (com.elastica.beatle.securlets.dto.Object object : documents.getObjects()) {
            CustomAssertion.assertTrue(object.getTotal() >= 0, "User vulnerability exposure total can't be null");
            Reporter.log(String.valueOf(object.getTotal()), true);
        }
    }

    @Test (groups={"EXPOSURE", "P1"})//(dataProviderClass = BoxDataProvider.class, dataProvider = "metricsExposuresTotal", groups={"DASHBOARD"})
    public void verifyFileTypes() throws Exception {

        Logger.info("Getting file types ...");

        //After external exposure
        //getFileTypes("el_box", true, 10, true);
//		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};
//
//		for (String vl : vulnerabilities) {
//			Logger.info(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) );
//		}
    }

    @Test //(dataProviderClass = BoxDataProvider.class, dataProvider = "metricsExposuresTotal", groups={"DASHBOARD"})
    public void verifyRiskyDocuments() throws Exception {

        Logger.info("Getting risky documents ...");

        //After external exposure
//		getRiskyDocuments(true, 20);
//		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};
//
//		for (String vl : vulnerabilities) {
//			Logger.info(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) );
//		}
    }

    @Test(groups={"EXPOSURE", "P2"})
    public void VerifyInternallyExposedFileTypes() throws Exception {
        Logger.info("Getting file types ...");

        HashMap<String, String> hmap = new HashMap<String, String>();
        hmap.put("top", "10");

        //After external exposure
        ExposureTotals exposureTotals = null;//= getFileTypes(SecurletUtils.elapp.el_box.name(), hmap);

        HashMap<String, Integer> beforeCount = new HashMap<String, Integer>();

        for (TotalObject totalObj : exposureTotals.getObjects()) {
            beforeCount.put(totalObj.getId(), totalObj.getCount());
        }

        //upload a 
    }

    /**
     * This is the utility method to remediate the exposure thro' api.
     *
     * @param tenant
     * @param facility
     * @param user
     * @param documentId
     * @param userId
     * @param action
     * @throws Exception
     */
    public void remediateExposureWithAPI(BoxRemediation remediationObject) throws Exception {

        List<NameValuePair> headers = getHeaders();

        headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        String payload = "{\"objects\":[" + MarshallingUtils.marshall(remediationObject) + "]}";

        Reporter.log("Request body:" + payload, true);
        StringEntity stringEntity = new StringEntity(payload);
        String path = suiteData.getAPIMap().get("getBoxRemediation")
                .replace("{tenant}", suiteData.getTenantName())
                .replace("{version}", suiteData.getBaseVersion());

        URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
        HttpResponse response = restClient.doPatch(uri, headers, null, stringEntity);
        String responseBody = ClientUtil.getResponseBody(response);

        Reporter.log("Response body:" + responseBody, true);
        Reporter.log("Response code:" + response.getStatusLine().getReasonPhrase(), true);
    }

    private BoxRemediation getBulkRemediationObject(ArrayList<String> collaboratorName) {
        BoxRemediation boxRemediation = new BoxRemediation();

        boxRemediation.setDbName(suiteData.getTenantName());
        boxRemediation.setUser("__ALL_EL__");
        boxRemediation.setUserId("__ALL_EL__");
        boxRemediation.setDocType("__ALL_EL__");
        boxRemediation.setDocId("__ALL_EL__");

        //Meta Info
        BoxMetaInfo boxMetaInfo = new BoxMetaInfo();
        boxMetaInfo.setCurrentLink(null);
        boxMetaInfo.setCollabs(collaboratorName);
        boxMetaInfo.setExpireOn(null);
        boxMetaInfo.setAccess(null);

        List<BoxAction> actions = new ArrayList<BoxAction>();
        BoxAction boxActionForSharing = new BoxAction();
        boxActionForSharing.setCode("COLLAB_REMOVE");
        boxActionForSharing.setPossibleValues(new ArrayList<String>());
        boxActionForSharing.setMetaInfo(boxMetaInfo);
        boxActionForSharing.setCodeName("Remove");
        actions.add(boxActionForSharing);

        boxRemediation.setActions(actions);
        return boxRemediation;
    }

    public void createFolderAndCollaborateWithUser(String collaborator, String currentRole) throws Exception {

        String uniqueId = UUID.randomUUID().toString();
        String sourceFile = "Hello.java";
        String destinationFile = uniqueId + "_Hello.java";

        //create folder1
        BoxFolder folderObj = universalApi.createFolder(uniqueId);
        String folderId = folderObj.getId();

        //Perform folder operations
        //upload the file to folder1
        FileUploadResponse uploadResponse = universalApi.uploadFile(folderId, sourceFile, destinationFile);
        String fileId = uploadResponse.getFileId();

        Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

        //create collaboration object for saas app
        CollaborationInput collabInput = new CollaborationInput();
        Item item = new Item();
        item.setId(folderObj.getId());
        item.setType(folderObj.getType());

        AccessibleBy aby = new AccessibleBy();
        aby.setName(uniqueId);
        aby.setType("user");
        aby.setLogin(collaborator);

        collabInput.setItem(item);
        collabInput.setAccessibleBy(aby);
        collabInput.setRole(currentRole);

        //Create the collaboration
        BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);

        Reporter.log("Waiting for the collaboration action ...going to sleep " + CommonConstants.THREE_MINUTES_SLEEP + " ms", true);
        //Sleep time of atleast 3 mins is needed as our portal has to get the collaboration event. otherwise test will fail
        Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

        //get the collaborations and assert them
        Collaborations collaborations = universalApi.getFolderCollaborations(folderId);
    }

    // Other Missing Smoke Test.....
    
    private void removeAllPermission(String fileId){
        PermissionList retrievePermissionList = gDrive.retrievePermissionList(fileId);
        List<Permission> items = retrievePermissionList.getItems();
        for (Permission permission : items) {
            try{
             gDrive.removePermission(fileId, permission.getId());
            }
            catch(Exception e){
                System.out.println("Exception in Remove Permission :"+e.getLocalizedMessage());
            }
        }
        
    }
    
    
    @Test(groups={"EXPOSURE", "P1"})
    public void validateChildPermissionAfterParentSharing()  {
        String createFolderId = null;
        try {
            HashMap<String, String> additionalParams = new HashMap<>();
            additionalParams.put("is_internal", "true");
            additionalParams.put("exposures.types", "external");
            Reporter.log("Test Rail ID <T4243781> : Set the Sharing rights in the Parent Folder and keep the inside child file Private", true);
            // Create Folder..
            createFolderId = gDrive.createFolder("ChildPermissionTest_" + UUID.randomUUID());
            // Share this Folder to External User .. rahul.embeddedsystem@gmail.com
            gDrive.insertPermission(gDrive.getDriveService(), createFolderId, "rahul.embeddedsystem@gmail.com", "user", "writer"); // Role : writer,reader
            // Upload two files in this folder
            FileUploadResponse uploadFile1=gDrive.uploadFile(createFolderId, localFileLocation, "File1.txt");
            FileUploadResponse uploadFile2 = gDrive.uploadFile(createFolderId, localFileLocation, "File2.txt");
            securletsUtils.wait(60,"Waiting After File Upload....");
            
            int ExposureTotals = Integer.parseInt(RawJsonParser.getSingleKey(getExposureMetricsResponse(additionalParams), "$.meta.total_count"));
            Reporter.log("Exposure Count Before Remove Permissions on Inside Files :"+ExposureTotals,true);
            removeAllPermission(uploadFile1.getFileId());
            removeAllPermission(uploadFile2.getFileId());
            securletsUtils.wait(60,"Waiting After Remove Permission....");
            ExposureTotals = Integer.parseInt(RawJsonParser.getSingleKey(getExposureMetricsResponse(additionalParams), "$.meta.total_count"));
            Reporter.log("Exposure Count After Remove Permissions on Inside Files :"+ExposureTotals,true);           
        } catch (InterruptedException | IOException  ex) {
            Reporter.log("validateChildPermissionAfterParentSharing Test Exception :"+ex.getLocalizedMessage(),true);
        } catch (Exception ex) {
            Reporter.log("validateChildPermissionAfterParentSharing Test Exception :"+ex.getLocalizedMessage(),true);
        } 
        finally{
             gDrive.deleteFile(createFolderId);
        }
    }

    @Test(groups={"EXPOSURE", "P1"})
    public void validateVariousChildPermissionAfterParentSharing() throws InterruptedException, IOException {
        Reporter.log("Test rail ID <T4243782> : Set the Sharing rights in the Parent Folder and keep the inside child files with some other sharing type", true);
        String createFolderId = null;
        try {
            HashMap<String, String> additionalParams = new HashMap<>();
            additionalParams.put("is_internal", "true");
            additionalParams.put("exposures.types", "public");
            int ExposureTotals = Integer.parseInt(RawJsonParser.getSingleKey(getExposureMetricsResponse(additionalParams), "$.meta.total_count"));
            Reporter.log("Public Exposure Count before Upload :"+ExposureTotals,true);   
            Reporter.log("Test Rail ID <T4243781> : Set the Sharing rights in the Parent Folder and keep the inside child files with some other sharing type", true);
            // Create Folder..
            createFolderId = gDrive.createFolder("ChildPermissionTest_" + UUID.randomUUID());
            // Share this Folder to Public
            gDrive.insertPermission(gDrive.getDriveService(), createFolderId, null, "anyone", "writer"); // Role : writer,reader
            // Upload two files in this folder
            FileUploadResponse uploadFile1=gDrive.uploadFile(createFolderId, localFileLocation, "File1.txt");
            FileUploadResponse uploadFile2 = gDrive.uploadFile(createFolderId, localFileLocation, "File2.txt");
            securletsUtils.wait(60,"Waiting After File Upload....");
            int ExposureTotals_afterUpload = Integer.parseInt(RawJsonParser.getSingleKey(getExposureMetricsResponse(additionalParams), "$.meta.total_count"));
            Reporter.log("Exposure Count Before Remove Permissions/After File Upload on Inside Files :"+ExposureTotals_afterUpload,true);
            Assert.assertTrue(ExposureTotals_afterUpload==ExposureTotals+3,"Exposure Count Not Incremented After Upload...");
            removeAllPermission(uploadFile1.getFileId());
            removeAllPermission(uploadFile2.getFileId());
            gDrive.insertPermission(gDrive.getDriveService(), uploadFile1.getFileId(), internalDomain, "domain", "writer"); // Role : writer,reader
            gDrive.insertPermission(gDrive.getDriveService(), uploadFile2.getFileId(), internalDomain, "domain", "writer"); // Role : writer,reader
            securletsUtils.wait(60,"Waiting After Remove Permission....");
            int ExposureTotals_afterRemovePermission = Integer.parseInt(RawJsonParser.getSingleKey(getExposureMetricsResponse(additionalParams), "$.meta.total_count"));
            Reporter.log("Exposure Count After Remove Permissions on Inside Files :"+ExposureTotals_afterRemovePermission,true);  
            Assert.assertTrue(ExposureTotals_afterRemovePermission==ExposureTotals_afterUpload-2,"Exposure Count Not decremented After Remove Permission...");
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(GDriveSecurletsExposureMetricsTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(GDriveSecurletsExposureMetricsTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GDriveSecurletsExposureMetricsTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
             gDrive.deleteFile(createFolderId);
        }    
    }
    
    
    
    //==================== SUPPORTED METHODS =========================================================================================
    public ExposureTotals getUserTotals(String elappname, List<NameValuePair> qparams) throws Exception {
        List<NameValuePair> headers = getHeaders();

        String path = suiteData.getAPIMap().get("getUserTotals")
                .replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
                .replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
                .replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

        //System.out.println("Path:" + path);
        URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
        HttpResponse response = restClient.doGet(uri, headers);
        String responseBody = ClientUtil.getResponseBody(response);
        Logger.info("Response body:" + responseBody);
        ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
        return exposureTotals;
    }

    public SecurletDocument getExposedUsers(String elappname, List<NameValuePair> qparams) throws Exception {
        List<NameValuePair> headers = getHeaders();

        String path = suiteData.getAPIMap().get("getExposedUsers")
                .replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
                .replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
                .replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

        //System.out.println("Path:" + path);
        URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
        HttpResponse response = restClient.doGet(uri, headers);
        String responseBody = ClientUtil.getResponseBody(response);

        Reporter.log("Response body:" + responseBody, true);
        SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
        return documents;
    }

    public SecurletDocument getUserExposures(String elappname, List<NameValuePair> qparams) throws Exception {
        List<NameValuePair> headers = getHeaders();

        String path = suiteData.getAPIMap().get("getUserExposures")
                .replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
                .replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
                .replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

        //System.out.println("Path:" + path);
        URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
        HttpResponse response = restClient.doGet(uri, headers);
        String responseBody = ClientUtil.getResponseBody(response);

        Reporter.log("Response body:" + responseBody, true);
        SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
        return documents;
    }

    public SecurletDocument getUserVulnerabilities(String elappname, List<NameValuePair> qparams) throws Exception {
        List<NameValuePair> headers = getHeaders();

        String path = suiteData.getAPIMap().get("getUserVulnerabilities")
                .replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
                .replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
                .replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

        //System.out.println("Path:" + path);
        URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
        HttpResponse response = restClient.doGet(uri, headers);
        String responseBody = ClientUtil.getResponseBody(response);

        Reporter.log("Response body:" + responseBody, true);
        SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
        return documents;
    }

    public VulnerabilityTypes getVulnerabilityTypes(String elappname, HashMap<String, String> additionalParams) throws Exception {
        List<NameValuePair> headers = getHeaders();

        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        //Add all the keys
        for (String key : additionalParams.keySet()) {
            qparams.add(new BasicNameValuePair(key, additionalParams.get(key)));
        }

        String path = suiteData.getAPIMap().get("getVulnerabilityTypes")
                .replace("{elappname}", elappname)
                .replace("{tenant}", suiteData.getTenantName())
                .replace("{version}", suiteData.getBaseVersion());

        URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
        HttpResponse response = restClient.doGet(uri, headers);
        String responseBody = ClientUtil.getResponseBody(response);

        Logger.info("Response body:" + responseBody);
        VulnerabilityTypes vulnerabilityTypes = MarshallingUtils.unmarshall(responseBody, VulnerabilityTypes.class);
        return vulnerabilityTypes;
    }

    public ExposureTotals getExposedContentTypes(String elappname, HashMap<String, String> queryParams) throws Exception {
        List<NameValuePair> headers = getHeaders();
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();

        if (queryParams != null) {
            //Add all the keys
            for (String key : queryParams.keySet()) {
                qparams.add(new BasicNameValuePair(key, queryParams.get(key)));
            }
        }

        String path = suiteData.getAPIMap().get("getDocumentClass")
                .replace("{elappname}", elappname)
                .replace("{tenant}", suiteData.getTenantName())
                .replace("{version}", suiteData.getBaseVersion());

        URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
        HttpResponse response = restClient.doGet(uri, headers);
        String responseBody = ClientUtil.getResponseBody(response);

        Logger.info("Response body:" + responseBody);

        ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
        return exposureTotals;
    }

    public ExposureTotals getExposedFileTypes(String elappname, String isInternal, String top) throws Exception {
        List<NameValuePair> headers = getHeaders();

        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("is_internal", isInternal));
        qparams.add(new BasicNameValuePair("top", top));

        String path = suiteData.getAPIMap().get("getExposedFileTypes")
                .replace("{elappname}", elappname)
                .replace("{tenant}", suiteData.getTenantName())
                .replace("{version}", suiteData.getBaseVersion());

        //System.out.println("Path:" + path);
        URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
        HttpResponse response = restClient.doGet(uri, headers);
        String responseBody = ClientUtil.getResponseBody(response);

        Logger.info("Response body:" + responseBody);
        ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
        return exposureTotals;
    }

}
