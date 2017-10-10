package com.elastica.beatle.tests.securlets;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxSharing;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RawJsonParser;
import static com.elastica.beatle.RawJsonParser.getURI;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIConstants;
import com.universal.common.DropBox;
import com.universal.common.GExcelDataProvider;
import com.universal.common.GoogleMailServices;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class DropboxSecurletsExposureTests extends CommonTest {

    String SaasApp_DropBox = "Dropbox";
    String internalSharedFolder = "/Elastica QA Team Folder";
    String uniquePrefix = DropboxDataProvider.uniqueIdentifier;
    String qaTestFolderInCloudInternalSwitch = "/" + uniquePrefix + "QAMetricsInternalSwitch";
    String qaTestFolderInCloudExternalSwitch = "/" + uniquePrefix + "QAMetricsExternalSwitch";
    String externalUserId = "admin@securletfeatle.com";
    String externalUserAccessToken = "SWj60UQU0SAAAAAAAAAACBaFAAec7UEF7_kucoqOP_Hx4vknuRGsFvEtvgXWoOZO";
    int noOfFiles = 0;
    long sleepDuration = 30000;
    List<String> uploadedFilesInternalSwitch = new ArrayList<>();
    List<String> uploadedFilesExternalSwitch = new ArrayList<>();
    List<String> fileNames = new ArrayList<>();
    Map<String, Long> dashbaordCountMetrics = new HashMap();
    String from_jodaTime;
    UniversalApi universalApi;
    String localFolderLocation;
    int retryCount = 0; // Variable Initialization...
    int maxRetryCount;
    int waitTimeToReceiveLog;//secs
    int delayBetweenSaaSActivity; //ms
    String memberId;
    List<NameValuePair> headers;
    String url;
    String environmentName;
    GoogleMailServices googleMailServices;
    DropBox dropbox;
    String json_Response = null;
    GExcelDataProvider dataProvider;
    String riskyUrl;

    Long publicExposedCount_InternalSwitch;
    Long internalExposedCount_InternalSwitch;
    Long externalExposedCount_InternalSwitch;
    Long totalExposedCount_InternalSwitch;

    Long external_publicExposedCount;
    Long external_AllInternalExposedCount;
    Long external_externalExposedCount;
    Long external_TotalExposedCount;

    @BeforeClass(alwaysRun = true)
    public void oneTimeSetUp() throws Exception {
        initializeVariables();
        initializeSaaSApp();
    }

    private void initializeVariables() throws IOException {

        Reporter.log("\n ====== #### Variable Initialization #### ===========", true);
        headers = getHeaders();
        //Relative Local file location..
        String hostName = suiteData.getApiserverHostName();
        String scheme = suiteData.getScheme();
        String hostUrl = scheme + "://" + hostName + "/";
        String tenant = suiteData.getTenantName();
        String path = "/api/admin/v1/el_dropbox/docs";
        url = hostUrl + tenant + path;
        riskyUrl = hostUrl + tenant + "/api/admin/v1/el_dropbox/risky_docs";

        localFolderLocation = DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH + File.separator;
        fileNames.add("glba.txt");
//        fileNames.add("hipaa.txt");
//        fileNames.add("ferpa.txt");
//        fileNames.add("pci.txt");
//        fileNames.add("pii.txt");
//        fileNames.add("virus.html");
//        fileNames.add("vba_macro.xls");

        noOfFiles = fileNames.size();
        delayBetweenSaaSActivity = Integer.parseInt(getRegressionSpecificSuitParameters("delayBetweenSaaSActivity")) * 1000;
        maxRetryCount = Integer.parseInt(getRegressionSpecificSuitParameters("maxRetryCount"));
        waitTimeToReceiveLog = Integer.parseInt(getRegressionSpecificSuitParameters("waitPeriodForElasticSearchEngine"));
        environmentName = getRegressionSpecificSuitParameters("environmentName");
        Reporter.log("Environment :" + environmentName + " Delay Between SaaS Activity :" + delayBetweenSaaSActivity / 1000 + " secs", true);
        Reporter.log("Environment :" + environmentName + " Wait Time To Sink Logs :" + waitTimeToReceiveLog + " secs", true);
        Reporter.log("====== #### Loading Expected Response And Variable Initialization !!! DONE !!! ===========", true);
    }

    public void initializeSaaSApp() {
        DropboxDataProvider.initialize();
        dataProvider = DropboxDataProvider.getDataProvider();
        dataProvider.updatedata("DropboxDashboradMetricsAndFilters", "uniquePrefix", uniquePrefix);
        Reporter.log(" ## System File Location :" + localFolderLocation, true);
        String accessToken = getRegressionSpecificSuitParameters("dropboxAdminAccessToken");
        UserAccount account = new UserAccount("rahul.kumar@elastica.co", "xxx", "ADMIN", accessToken); // Initialize the Useraccount for Dropbox  
        try {
            universalApi = new UniversalApi("DROPBOX", account);// Create Universal API reference
            this.dropbox = universalApi.getDropbox();
        } catch (Exception ex) {
            Reporter.log("Issue with creating Universal API " + ex.getLocalizedMessage());
        }
        Reporter.log("============== ## Initializing SaaS APP Client  DONE !! ## =======================", true);
    }

    @Test(groups={"EXPOSURE", "P1"})
    public void performDropboxActivitiesAndCollectMetricsInternalSwitch() throws IOException, DbxException, InterruptedException, Exception {

        collectMetricsFromInternalSwitch();
        dashbaordCountMetrics.put("publicExposedCount_InternalSwitch_before", publicExposedCount_InternalSwitch);
        dashbaordCountMetrics.put("internalExposedCount_InternalSwitch_before", internalExposedCount_InternalSwitch);
        dashbaordCountMetrics.put("externalExposedCount_InternalSwitch_before", externalExposedCount_InternalSwitch);
        dashbaordCountMetrics.put("totalExposedCount_InternalSwitch_before", totalExposedCount_InternalSwitch); 

        // Internal All Sharing....
        for (String fileName : fileNames) {
            RawJsonParser.wait(sleepDuration, "Upload a file to All Internal Shared Folder..Internal Sharing...");
            dropbox.uploadFile(internalSharedFolder + "/" + uniquePrefix + "InternalMetricsTest", localFolderLocation + fileName, uniquePrefix + "_" + fileName.substring(0, fileName.lastIndexOf('.')) + "_internalExposureTest.txt");
        }

        // External Sharing....
        RawJsonParser.wait(sleepDuration, "Create a folder to test External Sharing Metrics....");
        dropbox.createFolder("/" + uniquePrefix + "ExternalSharingMetricTest");
        RawJsonParser.wait(sleepDuration, "Share and Mount Folder.....");
        dropbox.shareAndMountFolderToUser("/" + uniquePrefix + "ExternalSharingMetricTest", externalUserId, DbxSharing.AccessLevel.editor, externalUserAccessToken);
        for (String fileName : fileNames) {
            RawJsonParser.wait(sleepDuration, "Upload a file To Externally Shared Folder...");
            dropbox.uploadFile("/" + uniquePrefix + "ExternalSharingMetricTest", localFolderLocation + fileName, uniquePrefix + "_" + fileName.substring(0, fileName.lastIndexOf('.')) + "_externalExposureTest.txt");
        }
        
        // Public Sharing...
        for (String fileName : fileNames) {
            RawJsonParser.wait(sleepDuration, "Upload and Share Public Sharing of Uploaded File...");
            dropbox.uploadFile(qaTestFolderInCloudInternalSwitch, localFolderLocation + fileName, uniquePrefix + "_" + fileName.substring(0, fileName.lastIndexOf('.')) + "_publicExposureTest.txt");
            dropbox.createSharedLinkForFolderORFile(qaTestFolderInCloudInternalSwitch + "/" + uniquePrefix + "_" + fileName.substring(0, fileName.lastIndexOf('.')) + "_publicExposureTest.txt");
        }

        RawJsonParser.wait(sleepDuration * 6, "Waiting : Dashboard Count Update <INCREMENT>...");

        collectMetricsFromInternalSwitch();
        dashbaordCountMetrics.put("publicExposedCount_InternalSwitch_after", publicExposedCount_InternalSwitch);
        dashbaordCountMetrics.put("internalExposedCount_InternalSwitch_after", internalExposedCount_InternalSwitch);
        dashbaordCountMetrics.put("externalExposedCount_InternalSwitch_after", externalExposedCount_InternalSwitch);
        dashbaordCountMetrics.put("totalExposedCount_InternalSwitch_after", totalExposedCount_InternalSwitch);
    }

    @Test(groups={"EXPOSURE", "P1"},dataProviderClass = DropboxDataProvider.class, dataProvider = "ExposedFilesMetadataInternalSwitch")
    public void validateExposedFilesMetadataInternalSwitch(String testDesc, String fileName, String doc_type,
            String is_public, String all_internal, String external) throws Exception {
        
        String docInfo = getDocInfo(fileName);
        Reporter.log("============================ Exposure Validation ==================================",true);
        Map<String, Object> actualExposure = (Map<String, Object>) RawJsonParser.fetchSingleField(docInfo, "$.objects[0].exposures"); 
        Reporter.log("Expected Exposure :------------"+"\nPublic :"+is_public + "\nAll Internal :"+all_internal  + "\nExternal :"+external,true);
        Reporter.log("Actual Exposure :--------"+actualExposure,true);
        if (is_public.equals("true")) {     
          Assert.assertEquals(actualExposure.get("all_internal"),false);
          Assert.assertEquals(actualExposure.get("public"),true);
        } else if (all_internal.equals("true")) {
            Assert.assertEquals(actualExposure.get("all_internal"),true);
            Assert.assertEquals(actualExposure.get("public"),false);
        } else if (external.equals("true")) {
            Assert.assertEquals(actualExposure.get("ext_count"),new Long("1"));
            Assert.assertTrue(actualExposure.get("external").toString().contains("admin@securletfeatle.com"),"Exposure Not Found....");
        } else {
            Reporter.log("No expected Exposue",true);
        }
        Reporter.log("============================ File Name Validation ==================================",true);
        String actualName = (String) RawJsonParser.fetchSingleField(docInfo, "$.objects[0].name");
        Reporter.log("Expected File Name :"+fileName + "\nActual File Name :"+actualName,true);
        
        Reporter.log("============================ Doc Type Validation ==================================",true);
        String actualdoc_type = (String) RawJsonParser.fetchSingleField(docInfo, "$.objects[0].doc_type");
        Reporter.log("Expected Doc Type :"+doc_type + "\nActual Doc Type :"+actualdoc_type,true);
        
        Assert.assertEquals(actualdoc_type,doc_type);
        Assert.assertEquals(actualName,fileName);
    }

    @Test(groups={"EXPOSURE", "P1"},dataProviderClass = DropboxDataProvider.class, dataProvider = "CountIncrementInternalSwitch")
    public void validateCountIncrementInternalSwitch(String testDesc, String param) throws Exception {
        Reporter.log("Dashboard Metrics Before SaaS Actions <" + param + "> :" + dashbaordCountMetrics.get(param + "_InternalSwitch_before"), true);
        Reporter.log("Dashboard Metrics After SaaS Actions <" + param + "> :" + dashbaordCountMetrics.get(param + "_InternalSwitch_after"), true);
        if (param.equals("totalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_InternalSwitch_after"), (long) dashbaordCountMetrics.get(param + "_InternalSwitch_before") + noOfFiles * 3 + 2, " ## Count is not Incremented ## for " + testDesc);
        } else if (param.equals("externalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_InternalSwitch_after"), (long) dashbaordCountMetrics.get(param + "_InternalSwitch_before") + noOfFiles + 1, " ## Count is not Incremented ## for " + testDesc);
        } else if (param.equals("publicExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_InternalSwitch_after"), (long) dashbaordCountMetrics.get(param + "_InternalSwitch_before") + noOfFiles, " ## Count is not Incremented ## for " + testDesc);
        } else if (param.equals("internalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_InternalSwitch_after"), (long) dashbaordCountMetrics.get(param + "_InternalSwitch_before") + noOfFiles + 1, " ## Count is not Incremented ## for " + testDesc);
        } else {
            Reporter.log("## ===Invalid Param=== ##", true);
        }
    }

    @Test
    public void validateCountInFileTypesIncrementInternalSwitch() {
        //https://api-eoe.elastica-inc.com/securletbeatlecom/api/admin/v1/el_dropbox/docs?doc_type=file&is_internal=true&format=txt

    }

    @Test
    public void validateCountInRiskTypeIncrementInternalSwitch() {
       // https://api-eoe.elastica-inc.com/securletbeatlecom/api/admin/v1/el_dropbox/docs?is_internal=true&expo sed=true&content_checks.vl_types=glba

    }

    @Test(groups={"EXPOSURE", "P1"})
    public void removeSharedFilesAndFolderInternalSwitch() throws Exception {
        RawJsonParser.wait(sleepDuration, "Permananent Deleting folder :" + qaTestFolderInCloudInternalSwitch);
        dropbox.deleteFileOrFolder(qaTestFolderInCloudInternalSwitch); // Public Sharing...
        RawJsonParser.wait(sleepDuration, "Permananent Deleting folder inside the Internal Shared Folder :" + internalSharedFolder);
        dropbox.deleteFileOrFolder(internalSharedFolder + "/" + uniquePrefix + "InternalMetricsTest"); // Internal ALL Sharing...
        RawJsonParser.wait(sleepDuration, "Permananent Deleting External Shared Folder :" + "/" + uniquePrefix + "ExternalSharingMetricTest");
        dropbox.deleteFileOrFolder("/" + uniquePrefix + "ExternalSharingMetricTest"); // External...
        RawJsonParser.wait(sleepDuration * 3, "Waiting : Dashboard Count Update <DECREMENT>...");
        collectMetricsFromInternalSwitch();
        dashbaordCountMetrics.put("publicExposedCount_InternalSwitch_afterDecrement", publicExposedCount_InternalSwitch);
        dashbaordCountMetrics.put("internalExposedCount_InternalSwitch_afterDecrement", internalExposedCount_InternalSwitch);
        dashbaordCountMetrics.put("externalExposedCount_InternalSwitch_afterDecrement", externalExposedCount_InternalSwitch);
        dashbaordCountMetrics.put("totalExposedCount_InternalSwitch_afterDecrement", totalExposedCount_InternalSwitch);
    }

    @Test(groups={"EXPOSURE", "P1"},dataProviderClass = DropboxDataProvider.class, dataProvider = "CountDecrementInternalSwitch")
    public void validateCountDecrementInternalSwitch(String testDesc, String param) {
        Reporter.log("Dashboard Metrics Before SaaS Actions <" + param + "> :" + dashbaordCountMetrics.get(param + "_InternalSwitch_after"), true);
        Reporter.log("Dashboard Metrics After SaaS Actions <" + param + "> :" + dashbaordCountMetrics.get(param + "_InternalSwitch_afterDecrement"), true);
        if (param.equals("totalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_InternalSwitch_afterDecrement"), (long) dashbaordCountMetrics.get(param + "_InternalSwitch_before"), " ## Count is not Incremented ## for " + testDesc);
        } else if (param.equals("externalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_InternalSwitch_afterDecrement"), (long) dashbaordCountMetrics.get(param + "_InternalSwitch_before"), " ## Count is not Incremented ## for " + testDesc);
        } else if (param.equals("publicExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_InternalSwitch_afterDecrement"), (long) dashbaordCountMetrics.get(param + "_InternalSwitch_before"), " ## Count is not Incremented ## for " + testDesc);
        } else if (param.equals("internalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_InternalSwitch_afterDecrement"), (long) dashbaordCountMetrics.get(param + "_InternalSwitch_before"), " ## Count is not Incremented ## for " + testDesc);
        } else {
            Reporter.log("## ===Invalid Param=== ##", true);
        }
    }

    @Test
    public void validateCountInFileTypesDecrementInternalSwitch() {

    }

    @Test
    public void validateCountInRiskTypeDecrementInternalSwitch() {

    }

    DropBox dropboxExternal = null;

    @Test(groups={"EXPOSURE", "P1"})
    public void performDropboxActivitiesAndCollectMetricsExternalSwitch() throws IOException, DbxException, InterruptedException, Exception {
        collectMetricsFromExternalSwitch();
        dashbaordCountMetrics.put("publicExposedCount_ExternalSwitch_before", external_publicExposedCount);
        dashbaordCountMetrics.put("internalExposedCount_ExternalSwitch_before", external_AllInternalExposedCount);
        dashbaordCountMetrics.put("externalExposedCount_ExternalSwitch_before", external_externalExposedCount);
        dashbaordCountMetrics.put("totalExposedCount_ExternalSwitch_before", external_TotalExposedCount);
        UserAccount accountExternal = new UserAccount("rahul.kumar@elastica.co", "xxx", "ADMIN", externalUserAccessToken); // Initialize the Useraccount for Dropbox
        try {
            this.dropboxExternal = new UniversalApi("DROPBOX", accountExternal).getDropbox();// Create Universal API reference

        } catch (Exception ex) {
            Reporter.log("Issue with creating Universal API " + ex.getLocalizedMessage());
        }

        // External Public Exposure....and External Exposure Test
        for (String fileName : fileNames) {
            RawJsonParser.wait(sleepDuration, "Creating and Uploading a file to a folder <PUBLIC Exposure>...");
            dropboxExternal.uploadFile(qaTestFolderInCloudExternalSwitch, localFolderLocation + fileName, uniquePrefix + fileName + "_publicExposureTest.txt");
            RawJsonParser.wait(sleepDuration, "Creating and Uploading a file to a folder <External Exposure>...");
            dropboxExternal.uploadFile(qaTestFolderInCloudExternalSwitch, localFolderLocation + fileName, uniquePrefix + fileName + "_externalExposureTest.txt");
        }

        RawJsonParser.wait(sleepDuration, "Share and Mount the folder with Internal User....");
        dropboxExternal.shareAndMountFolderToUser(qaTestFolderInCloudExternalSwitch, "admin@securletbeatle.com", DbxSharing.AccessLevel.editor, getRegressionSpecificSuitParameters("dropboxAdminAccessToken"));

        for (String fileName : fileNames) {
            RawJsonParser.wait(sleepDuration, "Public Sharing of Uploaded File from Internal User...");
            dropbox.createSharedLinkForFolderORFile(qaTestFolderInCloudExternalSwitch + "/" + uniquePrefix + fileName + "_publicExposureTest.txt");
        }
        // All Internal Test : #36855 // Bug..need to be fixed..
        RawJsonParser.wait(sleepDuration * 3, "Waiting : EXTERNAL SWITCH :Dashboard Count Update <INCREMENT>...");
        collectMetricsFromExternalSwitch();
        dashbaordCountMetrics.put("publicExposedCount_ExternalSwitch_after", external_publicExposedCount);
        dashbaordCountMetrics.put("internalExposedCount_ExternalSwitch_after", external_AllInternalExposedCount);
        dashbaordCountMetrics.put("externalExposedCount_ExternalSwitch_after", external_externalExposedCount);
        dashbaordCountMetrics.put("totalExposedCount_ExternalSwitch_after", external_TotalExposedCount);
    }

    @Test(groups={"EXPOSURE", "P1"},dataProviderClass = DropboxDataProvider.class, dataProvider = "CountIncrementExternalSwitch")
    public void validateCountIncrementExternalSwitch(String testDesc, String param) {
        Reporter.log("Dashboard Metrics Before SaaS Actions <" + param + "> :" + dashbaordCountMetrics.get(param + "_ExternalSwitch_before"), true);
        Reporter.log("Dashboard Metrics After SaaS Actions <" + param + "> :" + dashbaordCountMetrics.get(param + "_ExternalSwitch_after"), true);
        if (param.equals("totalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_ExternalSwitch_after"), (long) dashbaordCountMetrics.get(param + "_ExternalSwitch_before") + noOfFiles * 2 + 1, " ## Count is not Incremented ## for " + testDesc);
        } else if (param.equals("externalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_ExternalSwitch_after"), (long) dashbaordCountMetrics.get(param + "_ExternalSwitch_before") + noOfFiles * 2 + 1, " ## Count is not Incremented ## for " + testDesc);
        } else if (param.equals("publicExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_ExternalSwitch_after"), (long) dashbaordCountMetrics.get(param + "_ExternalSwitch_before") + noOfFiles, " ## Count is not Incremented ## for " + testDesc);
        } else if (param.equals("internalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_ExternalSwitch_after"), (long) dashbaordCountMetrics.get(param + "_ExternalSwitch_before") + noOfFiles + 1, " ## Count is not Incremented ## for " + testDesc);
        } else {
            Reporter.log("## ===Invalid Param=== ##", true);
        }
    }

    @Test(groups={"EXPOSURE", "P1"})
    public void removeSharedFilesAndFolderExternalSwitch() throws Exception {
        RawJsonParser.wait(sleepDuration, "Permananent Deleting folder from External User Account :" + qaTestFolderInCloudInternalSwitch);
        dropboxExternal.deleteFileOrFolder(qaTestFolderInCloudExternalSwitch);
        RawJsonParser.wait(sleepDuration, "Deleting the folder from Internal Account...");
        dropbox.deleteFileOrFolder(qaTestFolderInCloudExternalSwitch);
        RawJsonParser.wait(sleepDuration * 3, "Waiting : EXTERNAL SWITCH Dashboard Count Update <DECREMENT>...");
        collectMetricsFromExternalSwitch();
        dashbaordCountMetrics.put("publicExposedCount_ExternalSwitch_afterDecrement", external_publicExposedCount);
        dashbaordCountMetrics.put("internalExposedCount_ExternalSwitch_afterDecrement", external_AllInternalExposedCount);
        dashbaordCountMetrics.put("externalExposedCount_ExternalSwitch_afterDecrement", external_externalExposedCount);
        dashbaordCountMetrics.put("totalExposedCount_ExternalSwitch_afterDecrement", external_TotalExposedCount);
        System.out.println("===>" + dashbaordCountMetrics);
    }

    @Test(groups={"EXPOSURE", "P1"},dataProviderClass = DropboxDataProvider.class, dataProvider = "CountDecrementExternalSwitch")
    public void validateCountDecrementExternalSwitch(String testDesc, String param) {
        Reporter.log("Dashboard Metrics Before SaaS Actions <" + param + "> :" + dashbaordCountMetrics.get(param + "_ExternalSwitch_after"), true);
        Reporter.log("Dashboard Metrics After SaaS Actions <" + param + "> :" + dashbaordCountMetrics.get(param + "_ExternalSwitch_afterDecrement"), true);
        if (param.equals("totalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_ExternalSwitch_afterDecrement"), (long) dashbaordCountMetrics.get(param + "_ExternalSwitch_before"), " ## Count is not Decremented ## for " + testDesc);
        } else if (param.equals("externalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_ExternalSwitch_afterDecrement"), (long) dashbaordCountMetrics.get(param + "_ExternalSwitch_before"), " ## Count is not Decremented ## for " + testDesc);
        } else if (param.equals("publicExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_ExternalSwitch_afterDecrement"), (long) dashbaordCountMetrics.get(param + "_ExternalSwitch_before"), " ## Count is not Decremented ## for " + testDesc);
        } else if (param.equals("internalExposedCount")) {
            Assert.assertEquals((long) dashbaordCountMetrics.get(param + "_ExternalSwitch_afterDecrement"), (long) dashbaordCountMetrics.get(param + "_ExternalSwitch_before"), " ## Count is not Decremented ## for " + testDesc);
        } else {
            Reporter.log("## ===Invalid Param=== ##", true);
        }
    }

    private void collectMetricsFromInternalSwitch() throws Exception {
        publicExposedCount_InternalSwitch = getPublicExposedCount_InternalSwitch();
        System.out.println("INTERNAL Public Exposed Count :" + publicExposedCount_InternalSwitch);

        internalExposedCount_InternalSwitch = getInternalExposedCount_InternalSwitch();
        System.out.println("INTERNAL Internal Exposed Count :" + internalExposedCount_InternalSwitch);

        externalExposedCount_InternalSwitch = getExternalExposedCount_InternalSwitch();
        System.out.println("INTERNAL external Exposed Count :" + externalExposedCount_InternalSwitch);

        totalExposedCount_InternalSwitch = getTotalCount_InternalSwitch();
        System.out.println("INTERNAL total Exposed Count :" + totalExposedCount_InternalSwitch);

    }

    public String getHttpResponse(String url, Map<String, String> params) {
        try {
            RawJsonParser.wait(5000, "Waiting .....");
            HttpResponse doGet = restClient.doGet(new URI(RawJsonParser.getURI(url, params)), this.headers);
            String jsonResponse = ClientUtil.getResponseBody(doGet);
            return jsonResponse;
        } catch (Exception ex) {
            getHttpResponse(url, params);
            Logger.getLogger(DropboxSecurletsExposureTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String getDocInfo(String docName) throws IOException, Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", docName);
        String httpResponse = getHttpResponse(url, params);
        Reporter.log("Json Response :" + httpResponse, true);
        return httpResponse;
    }

    private Long getMetrics(Map<String, String> params) throws Exception {
        String httpResponse = getHttpResponse(url, params);
        Reporter.log("Metrics Response :" + httpResponse, true);
        Long publicExposedCount = (long) RawJsonParser.fetchSingleField(httpResponse, "$.meta.total_count");
        return publicExposedCount;
    }

    private Long getTotalCount_InternalSwitch() throws Exception {
        Map<String, String> Internal_total = new HashMap<>();
        Internal_total.put("is_internal", "true");
        Internal_total.put("exposed", "true");
        Long internalTotalExposedCount = getMetrics(Internal_total);
        return internalTotalExposedCount;
    }

    private Long getExternalExposedCount_InternalSwitch() throws Exception {
        Map<String, String> Internal_external = new HashMap<>();
        Internal_external.put("is_internal", "true");
        Internal_external.put("exposures.types", "ext_count");
        Internal_external.put("exposed", "true");
        Long externalExposedCount = getMetrics(Internal_external);
        return externalExposedCount;
    }

    private Long getInternalExposedCount_InternalSwitch() throws Exception {
        Map<String, String> Internal_internal = new HashMap<>();
        Internal_internal.put("is_internal", "true");
        Internal_internal.put("exposures.types", "all_internal");
        Internal_internal.put("exposed", "true");
        Long internalExposedCount = getMetrics(Internal_internal);
        return internalExposedCount;
    }

    private Long getPublicExposedCount_InternalSwitch() throws Exception {
        Map<String, String> Internal_public = new HashMap<>();
        Internal_public.put("is_internal", "true");
        Internal_public.put("exposures.types", "public");
        Internal_public.put("exposed", "true");
        Long publicExposedCount = getMetrics(Internal_public);
        return publicExposedCount;
    }

    private void collectMetricsFromExternalSwitch() throws Exception {

        getPublicExposedCount_ExternalSwitch();
        System.out.println("EXTERNAL Public Exposed Count :" + external_publicExposedCount);

        getInternalExposedCount_ExternalSwitch();
        System.out.println("EXTERNAL Internal Exposed Count :" + external_AllInternalExposedCount);

        getExternalExposedCount_ExternalSwitch();
        System.out.println("EXTERNAL external Exposed Count :" + external_externalExposedCount);

        getTotalExposedCount_InternalSwitch();
        System.out.println("EXTERNAL total Exposed Count :" + external_TotalExposedCount);
    }

    private void getTotalExposedCount_InternalSwitch() throws Exception {
        Map<String, String> External_total = new HashMap<>();
        External_total.put("is_internal", "false");
        External_total.put("exposed", "true");
        external_TotalExposedCount = getMetrics(External_total);
    }

    private void getExternalExposedCount_ExternalSwitch() throws Exception {
        Map<String, String> External_external = new HashMap<>();
        External_external.put("is_internal", "false");
        External_external.put("exposed", "true");
        external_externalExposedCount = getMetrics(External_external);
    }

    private void getInternalExposedCount_ExternalSwitch() throws Exception {
        Map<String, String> External_internal = new HashMap<>();
        External_internal.put("is_external", "true");
        External_internal.put("exposures.types", "all_internal");
        External_internal.put("exposed", "true");
        external_AllInternalExposedCount = getMetrics(External_internal);
    }

    private void getPublicExposedCount_ExternalSwitch() throws Exception {
        Map<String, String> External_public = new HashMap<>();
        External_public.put("is_external", "true");
        External_public.put("exposures.types", "public");
        External_public.put("exposed", "true");
        external_publicExposedCount = getMetrics(External_public);
    }

}
