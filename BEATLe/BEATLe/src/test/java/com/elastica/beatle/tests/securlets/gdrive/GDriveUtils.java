package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.splunk.SplunkConstants;
import com.elastica.beatle.splunk.SplunkQueryHandlers;
import com.elastica.beatle.splunk.SplunkQueryResult;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.universal.common.GDrive;
import com.universal.common.GDriveAuthorization;
import com.universal.common.GExcelDataProvider;
import com.universal.common.GoogleMailServices;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.FileUploadResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class GDriveUtils extends CommonTest {

    String SaasApp_GoogleApps = "Google Apps";
    String actualFileNameToUpload = "glba-test.txt";
    ESQueryBuilder eSQueryBuilder = new ESQueryBuilder();
    int globalWaitTime=60;

    GExcelDataProvider dataProvider;
    List<NameValuePair> headers;
    String url;
    String riskyDoc;
    GDrive gDriveAdmin;
    GDrive gDriveInternalUser;
    GDrive gDriveExternalUser;
    GoogleMailServices googleMailServices;
    String localFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + actualFileNameToUpload;
    String user;
    String urlRemediation;
    String tenant;
    String urlDocInfo;
    String urlUIRemediation;
    int retryCount = 0;
    String environmentName;
    String uniqueIdentifier = GDriveDataProvider.uniqueIdentifier;

    String admin_clientID = "531581515230-0k9p8gne8a0uh116u4fff7j93o7nsdu1.apps.googleusercontent.com";
    String admin_clientSecret = "EQUZEc3QoEa0EqBzHVpoMLgr";
    String admin_refreshToken = "1/eckCHbIndshTd6zxoq5Ds13OqYondPtDxMNV2BUIzGhIgOrJDtdun6zK6XiATCKT";

    String internal_clientID = "812119916773-hb47rkktb3p8appsc93cr8tokau0r1gs.apps.googleusercontent.com";
    String internal_clientSecret = "iSZQfwpG9RP8dKdusaevG1y6";
    String internal_refreshToken = "1/CGpA7p1rtHOWjMGJsNRLKiCoRKIkOpV7ZLo1LPMiKBkMEudVrK5jSpoR30zcRFq6";

    String external_clientID = "379357503205-avpf0206mrta8e52m6cpacme3g98giv7.apps.googleusercontent.com";
    String external_clientSecret = "ovbkbH6N-RX7eETzF96US6Tb";
    String external_refreshToken = "1/x--1EEIYbUPbnzdzDsivTFQ24uT6Xn3CrLu52Qve_9HBactUREZofsF9C7PrpE-j";

    String admin_clientID_gmail = "998314684213-34jm3g4k92nejio174qnnb32vojbqg0n.apps.googleusercontent.com";
    String admin_clientSecret_gmail = "YkMqf5GWiHQgHbA1P7BNhxto";
    String admin_refreshToken_gmail = "1/fPDwECsPhJAPh6F_TsHHhz7Q1AArPFC-fa6XdV_bNko";

//    String [] filePattern={"Prevent","External","InsertOperation-QA",
//        "GDriveRemediation","GDriveExposure","_QATest",
//        "_InsertFolder","Bulk","QATemptest","QATesting"};
//    @Test
//    public void SaaSCleanUp() throws IOException, InterruptedException{
//        gDriveAdmin.trashRootFolderItems(filePattern);
//        gDriveInternalUser.trashRootFolderItems(filePattern);
//        gDriveExternalUser.trashRootFolderItems(filePattern);
//    }
    @BeforeClass
    public void getDefaultDriveInstance() throws IOException {
        GDriveDataProvider.initialize();
        this.dataProvider = GDriveDataProvider.getDataProvider();
        environmentName = getRegressionSpecificSuitParameters("environmentName");
        String username = suiteData.getUsername();
        Reporter.log("Username :" + username, true);

        if (username.equals("enggadmin@securletdddbeatle.com")) {
            this.admin_clientID = "1003079369238-6guq73r9qq1d7840l75k93bib531qn1t.apps.googleusercontent.com";
            this.admin_clientSecret = "fjUrtuYmYDfz9lKGwL1w4RgP";
            this.admin_refreshToken = "1/8EbhXpnPOvcwQSDCsaB-QvTylnBqfuF2X7Z9pO6kXpA";

            this.admin_clientID_gmail = "1003079369238-6guq73r9qq1d7840l75k93bib531qn1t.apps.googleusercontent.com";
            this.admin_clientSecret_gmail = "fjUrtuYmYDfz9lKGwL1w4RgP";
            this.admin_refreshToken_gmail = "1/8EbhXpnPOvcwQSDCsaB-QvTylnBqfuF2X7Z9pO6kXpA";
        }
        if (username.equals("admin@securletdddbeatle.com")) {

        }

        this.gDriveAdmin = getUniversalApi_gDrive(admin_clientID, admin_clientSecret, admin_refreshToken).getgDrive();
        this.gDriveInternalUser = getUniversalApi_gDrive(internal_clientID, internal_clientSecret, internal_refreshToken).getgDrive();
        this.gDriveExternalUser = getUniversalApi_gDrive(external_clientID, external_clientSecret, external_refreshToken).getgDrive();
        this.googleMailServices = new GoogleMailServices(admin_clientID_gmail, admin_clientSecret_gmail, admin_refreshToken_gmail);

        String hostUrl = suiteData.getScheme() + "://" + suiteData.getApiserverHostName() + "/";
        url = hostUrl + suiteData.getTenantName() + "/api/admin/v1/el_google_apps/docs";
        riskyDoc = hostUrl + suiteData.getTenantName() + "/api/admin/v1/el_google_apps/risky_docs";
        List<NameValuePair> buildBasicHeaders = null;
        try {
            buildBasicHeaders = buildBasicHeaders();
        } catch (Exception ex) {
            Reporter.log("Issue with Building Basic headers" + ex.getLocalizedMessage(), true);
        }
        NameValuePair nameValuePair1 = new BasicNameValuePair("Content-Type", "application/json");
        buildBasicHeaders.add(nameValuePair1);
        headers = buildBasicHeaders;
        String hostName = suiteData.getApiserverHostName();
        String scheme = suiteData.getScheme();
        tenant = suiteData.getTenantName();
        user = getRegressionSpecificSuitParameters("userName");
        String SaaSApp = "el_google_apps";
        this.environmentName = suiteData.getEnvironmentName();
        urlRemediation = scheme + "://" + hostName + "/" + tenant + "/api/admin/v1/" + SaaSApp + "/remediation/";
        urlDocInfo = scheme + "://" + hostName + "/" + tenant + "/api/admin/v1/" + SaaSApp + "/docs/";
        urlUIRemediation = scheme + "://" + suiteData.getHost() + "/admin/application/apply_remediation";
        Reporter.log("Scheme: " + suiteData.getScheme(), true);
        Reporter.log("Domain: " + suiteData.getDomainName(), true);
        Reporter.log("Host Name: " + suiteData.getHost(), true);
        Reporter.log("API Hostname: " + suiteData.getApiserverHostName(), true);
        Reporter.log("Tenant :" + suiteData.getTenantName(), true);
        Reporter.log("Remediation URL : " + urlRemediation, true);
        try {
            this.headers = getHeaders();
            this.headers.add(nameValuePair1);
        } catch (IOException ex) {
            Reporter.log("Issue Encountered in Getting Headers :" + ex.getLocalizedMessage(), true);
        }
    }

    /**
     * @param ClientId
     * @param ClientSecret
     * @param refreshToken
     * @return
     */
    public UniversalApi getUniversalApi_gDrive(String ClientId, String ClientSecret, String refreshToken) {
        Reporter.log("Initializing the SaaS App..............", true);
        try {
            UserAccount userAccount = new UserAccount("rahul.kumar@elastica.co", "123456", "ADMIN", new GDriveAuthorization(ClientId, ClientSecret).getAceessTokenFromRefreshAccessToken(refreshToken));
            universalApi = new UniversalApi("GDRIVE", userAccount);
            Drive.About about = universalApi.getgDrive().getDriveService().about();
            System.out.println("##### ==>" + about.toString());
            Reporter.log("Initializing the SaaS App  !!! DONE !!! ", true);
            try {
                Reporter.log("### !!!! GDrive Successfully Initialized for USER MAIL ID :" + universalApi.getgDrive().getEmailId(), true);
            } catch (InterruptedException ex) {
                Logger.getLogger(GDriveUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            return universalApi;
        } catch (Exception ex) {
            Reporter.log("Issue with creating Universal API " + ex.getLocalizedMessage(), true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex1) {
                Logger.getLogger(GDriveUtils.class.getName()).log(Level.SEVERE, null, ex1);
            }
            getUniversalApi_gDrive(ClientId, ClientSecret, refreshToken);
        }

        return null;
    }

    public void wait(int secs, String msg) {
        Reporter.log("--------------------------------------------------------", true);
        Reporter.log(msg.toUpperCase() + " >> ..Waiting for <" + secs + "> secs........", true);
        Reporter.log("--------------------------------------------------------", true);
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException ex) {

        }
    }

    public String getCurrentJodaTime() {
        return new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC).toString();
    }

    public String getDisplayLogResponse(String msg, String from_jodaTime) throws Exception {
        wait(globalWaitTime * 3, msg);
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", "Google Drive");
        String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
        String payload = "";
        payload = eSQueryBuilder.getESQuery(from_jodaTime, getCurrentJodaTime(),
                SaasApp_GoogleApps, termmap, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_GoogleApps);
        Reporter.log("Request body:" + payload, true);
        String path = suiteData.getAPIMap().get("getInvestigateLogs");
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
        Reporter.log("URI ::" + dataUri.toString(), true);
        HttpResponse response = restClient.doPost(dataUri, getHeaders(), null, new StringEntity(payload));
        return ClientUtil.getResponseBody(response);
    }

    public Map<String, Long> getDashboardMetrics(String internalSwitch) throws Exception {
        Reporter.log("## Retrieving DashBoard Metrics For GDrive....", true);
        Map<String, Long> dashboardCount = new HashMap();
        dashboardCount.put("countPublicSearchAbleInternalSwitch", getPublicExposedCountSearchable(internalSwitch));
        dashboardCount.put("countPublicWebLinkInternalSwitch", getPublicExposedCountwithLink(internalSwitch));
        dashboardCount.put("countAllInternalSearchAbleInternalSwitch", getInternalExposedCountSearchable(internalSwitch));
        dashboardCount.put("countAllInternalwithLinkInternalSwitch", getInternalExposedCountwithLink(internalSwitch));
        dashboardCount.put("countExternalInternalSwitch", getExternalExposedCount(internalSwitch));
        dashboardCount.put("totalCountInternalSwitch", getTotalCount(internalSwitch));
        Reporter.log("Count External Internal Switch :" + dashboardCount.get("countExternalInternalSwitch"), true);
        Reporter.log("Count AllInternal SearchAble InternalSwitch :" + dashboardCount.get("countAllInternalSearchAbleInternalSwitch"), true);
        Reporter.log("Count AllInternal withLink InternalSwitch :" + dashboardCount.get("countAllInternalwithLinkInternalSwitch"), true);
        Reporter.log("Count PublicSearchAble InternalSwitch :" + dashboardCount.get("countPublicSearchAbleInternalSwitch"), true);
        Reporter.log("Count Public WebLink InternalSwitch :" + dashboardCount.get("countPublicWebLinkInternalSwitch"), true);
        Reporter.log("Total Count Internal Switch :" + dashboardCount.get("totalCountInternalSwitch"), true);
        return dashboardCount;
    }

    public String getHttpResponse(String url, Map<String, String> params) {
        try {
            RawJsonParser.wait(5000, "Waiting Before API Call .....");
            com.elastica.beatle.RestClient.Client client = new Client();
            HttpResponse doGet = client.doGet(new URI(RawJsonParser.getURI(url, params)), this.headers);
            String jsonResponse = ClientUtil.getResponseBody(doGet);
            return jsonResponse;
        } catch (Exception ex) {
            Reporter.log("Exception in Get Response in Query :" + ex.getLocalizedMessage(), true);
        }
        return null;
    }

    public String getDocInfo(String docName) throws IOException, Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", docName);
        String httpResponse = getHttpResponse(url, params);
        Reporter.log("Json Response :" + httpResponse, true);
        return httpResponse;
    }

    public String getRiskyDocInfo(String docName) throws IOException, Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", docName);
        String httpResponse = getHttpResponse(riskyDoc, params);
        Reporter.log("Json Response :" + httpResponse, true);
        return httpResponse;
    }

    public Long getMetrics(Map<String, String> params) throws Exception {
        String httpResponse = getHttpResponse(url, params);
        Reporter.log("Metrics Response :" + httpResponse, true);
        Long ExposedCount = (long) RawJsonParser.fetchSingleField(httpResponse, "$.meta.total_count");
        return ExposedCount;
    }

    public Long getTotalCount(String internalSwitch) throws Exception {
        Map<String, String> Internal_total = new HashMap<>();
        Internal_total.put("is_internal", internalSwitch);
        Internal_total.put("exposed", "true");
        Internal_total.put("object_type", "Drive");
        Long internalTotalExposedCount = getMetrics(Internal_total);
        return internalTotalExposedCount;
    }

    public Long getExternalExposedCount(String internalSwitch) throws Exception {
        Map<String, String> Internal_external = new HashMap<>();
        Internal_external.put("is_internal", internalSwitch);
        Internal_external.put("exposures.types", "ext_count");
        Internal_external.put("exposed", "true");
        Internal_external.put("object_type", "Drive");
        Long externalExposedCount = getMetrics(Internal_external);
        return externalExposedCount;
    }

    public Long getInternalExposedCountSearchable(String internalSwitch) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("is_internal", internalSwitch);
        params.put("object_type", "Drive");
        params.put("exposed", "true");
        params.put("exposures.types", "all_internal,internal_searchable");
        Long internalExposedCount = getMetrics(params);
        return internalExposedCount;
    }

    public Long getInternalExposedCountwithLink(String internalSwitch) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("is_internal", internalSwitch);
        params.put("object_type", "Drive");
        params.put("exposed", "true");
        params.put("exposures.types", "all_internal,internal_withlink");
        Long internalTotalExposedCount = getMetrics(params);
        return internalTotalExposedCount;
    }

    public Long getPublicExposedCountSearchable(String internalSwitch) throws Exception {
        Map<String, String> Internal_internal = new HashMap<>();
        Internal_internal.put("is_internal", internalSwitch);
        Internal_internal.put("object_type", "Drive");
        Internal_internal.put("exposures.types", "public,public_searchable");
        Internal_internal.put("exposed", "true");
        Long internalExposedCount = getMetrics(Internal_internal);
        return internalExposedCount;
    }

    public Long getPublicExposedCountwithLink(String internalSwitch) throws Exception {
        Map<String, String> Internal_total = new HashMap<>();
        Internal_total.put("is_internal", internalSwitch);
        Internal_total.put("object_type", "Drive");
        Internal_total.put("exposures.types", "public,public_withlink");
        Long internalTotalExposedCount = getMetrics(Internal_total);
        return internalTotalExposedCount;
    }

    public boolean matchPermission(PermissionList retrievePermissionList, List<String> expectedPermissionValues) throws IOException {
        boolean permissionFound = true;
        List<Permission> permissionList = retrievePermissionList.getItems();
        for (Permission permission : permissionList) {
            permissionFound = true;
            for (String expectedPermissionValue : expectedPermissionValues) {
                boolean containsValue = permission.containsValue(expectedPermissionValue);
                if (containsValue == false) {
                    permissionFound = false;
                }
            }
            if (permissionFound) {
                return permissionFound;

            }
        }// External For Loop...
        return false;
    }

    /**
     *
     * @param fileId_comment
     * @param gDrive
     * @return
     * @throws IOException
     */
    public Permission insertCommentPermission(String fileId_comment, GDrive gDrive) throws IOException {
        Permission permission = new Permission();
        List<String> additionalRoles = new ArrayList<>();
        additionalRoles.add("commenter");
        permission.setAdditionalRoles(additionalRoles);
        permission.setValue("admin@securletbeatle.com");
        permission.setType("user");
        permission.setRole("reader");
        Permission insertPermission_comment = gDrive.insertPermission(fileId_comment, permission);
        Reporter.log("## Insert Comment Permission Response :" + insertPermission_comment.toPrettyString(), true);
        return insertPermission_comment;
    }

    public String uploadFileWithMultipleAttempt(String fileName, String fileLocationInLocal, String cloudLocation, GDrive gDrive) throws InterruptedException {
        Reporter.log("================ GDRIVE SaaS Actions : UPLOAD FILE TO THE FOLDER  ===========", true);
        Reporter.log(" ## Uploading File :" + localFileLocation + " ## Cloud Folder :" + cloudLocation, true);
        String rootFolderID = "0AEFlk_9Az3bxUk9PVA";
        Reporter.log("## Root Folder ID.... :" + rootFolderID, true);
        FileUploadResponse fileUploadResponse = null;
        String fileId_publicExposedTest = null;

        try {
            fileUploadResponse = gDrive.uploadFile(rootFolderID, fileLocationInLocal, fileName);
            fileId_publicExposedTest = fileUploadResponse.getFileId();

        } catch (Exception ex) {
            Reporter.log("### Issue Found with File Upload :" + ex.getLocalizedMessage(), true);
            retryCount++;
            if (retryCount < 10) {
                Reporter.log("Waiting for 30 secs", true);
                Reporter.log("Refreshing the GDrive Instance", true);
                wait(10, "Waiting before Retry...");
                uploadFileWithMultipleAttempt(fileName, fileLocationInLocal, cloudLocation, getUniversalApi_gDrive(getRegressionSpecificSuitParameters("gdriveClientId"), getRegressionSpecificSuitParameters("gdriveClientSecret"), getRegressionSpecificSuitParameters("gdriveRefreshToken")).getgDrive());
            }
        }
        retryCount = 0;//reset the retry count...   
        wait(10, "Waiting after File Upload Operation...");
        return fileId_publicExposedTest;
    }

    public String uploadFileInToFolderWithMultipleAttempt(String fileName, String fileLocationInLocal, String folderId, GDrive gDrive) throws InterruptedException {
        Reporter.log("================ GDRIVE SaaS Actions : UPLOAD FILE TO THE FOLDER  ===========", true);
        Reporter.log(" ## Uploading File :" + localFileLocation + " ## Cloud Folder id :" + folderId, true);
        Reporter.log("## Folder ID.... :" + folderId, true);
        FileUploadResponse fileUploadResponse = null;
        String fileId = null;
        try {
            fileUploadResponse = gDrive.uploadFile(folderId, fileLocationInLocal, fileName);
            fileId = fileUploadResponse.getFileId();
        } catch (Exception ex) {
            Reporter.log("### Issue Found with File Upload :" + ex.getLocalizedMessage(), true);
            retryCount++;
            if (retryCount < 10) {
                Reporter.log("Waiting for 30 secs", true);
                Reporter.log("Refreshing the GDrive Instance", true);
                wait(10, "Waiting before Retry...");
                uploadFileWithMultipleAttempt(fileName, fileLocationInLocal, folderId, getUniversalApi_gDrive(getRegressionSpecificSuitParameters("gdriveClientId"), getRegressionSpecificSuitParameters("gdriveClientSecret"), getRegressionSpecificSuitParameters("gdriveRefreshToken")).getgDrive());
            }
        }
        retryCount = 0;//reset the retry count...   
        wait(10, "Waiting after File Upload Operation...");
        return fileId;
    }

    public String createFolder(String FolderLocationInCloud, GDrive gDrive) throws Exception {
        Reporter.log("================= GDRIVE SaaS Actions : FOLDER CREATION  =====================", true);
        Reporter.log("## Folder To Be Created :" + FolderLocationInCloud, true);
        String folderId = null;
        for (int i = 0; i < 5; i++) {
            try {
                folderId = gDrive.createFolder(FolderLocationInCloud);
                Reporter.log(" ## Folder Name :" + FolderLocationInCloud + " ## Folder ID : " + folderId, true);
                return folderId;
            } catch (Exception e) {
                Reporter.log(" ### Exception caught due to some issue...Retrying...after 30  secs" + e.getLocalizedMessage(), true);
                Thread.sleep(30000);
                folderId = gDrive.createFolder(FolderLocationInCloud);
            }
        }
        Reporter.log("After Multiple Retry Folder not Created", true);
        return null;
    }

    //https://api-eoe.elastica-inc.com/securletbeatlecom/api/admin/v1/el_google_apps/docs/collabs/?is_internal=true
    public long getExposedDataCountForAPartucularUser(String userMailId) throws URISyntaxException, Exception {
        String docCountUrl = suiteData.getScheme() + "://" + suiteData.getApiserverHostName() + "/" + suiteData.getTenantName() + "/api/admin/v1/el_google_apps/users?email=" + userMailId;
        HttpResponse response = restClient.doGet(new URI(docCountUrl), getHeaders());
        String responseBody = ClientUtil.getResponseBody(response);
        Long fetchSingleField = (Long) RawJsonParser.fetchSingleField(responseBody, "$.meta.total_count");
        Reporter.log("Exposed Count for Particular User:" + fetchSingleField, true);
        return fetchSingleField;
    }

    public StringBuffer getStringResponseFromHttpResponse(HttpResponse doGet) throws IOException, UnsupportedOperationException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(doGet.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }

    public String getDocID(String fileName) throws IOException, Exception {
        Map<String, String> params = new HashMap();
        params.put("name", fileName);
        StringBuffer result = getStringResponseFromHttpResponse(restClient.doGet(new URI(getURI(urlDocInfo, params)), headers));
        String result_response = result.toString();
        Reporter.log("#### \n Get Doc info... Result Response :" + result_response, true);
        String query = "$..identification";
        String docId = null;
        try {
            docId = RawJsonParser.fetchSingleField(result_response, query).toString();
        } catch (Exception e) {
            Reporter.log("Doc ID is not found in Elsatica Search Engine...", true);
        }
        return docId;
    }

    public String getURI(String url, Map<String, String> params) throws URISyntaxException {
        URIBuilder uRIBuilder = new URIBuilder(url);
        if (params == null) {
            return uRIBuilder.toString();
        }
        for (Map.Entry<String, String> entrySet : params.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            uRIBuilder.addParameter(key, value);
            Reporter.log("Appending Query Params to the URL ... KEY :" + key + "  VALUE :" + value);
        }
        String toString = uRIBuilder.toString();
        return toString;
    }

    public void applyRestriction(String docId, String restrictionType) throws URISyntaxException, Exception {
        Reporter.log("Doc ID :" + docId + "  Restriction Type :" + restrictionType, true);
        String payload = "{\"source\":{\"objects\":{\"objects\":[{\"db_name\":\"securletbeatlecom\",\"user\":\"admin@securletbeatle.com\",\"user_id\":null,\"doc_id\":\""
                + docId
                + "\",\"doc_type\":\"file\",\"actions\":[{\"code\":\"ACCESS_RESTRICTION\",\"possible_values\":[\"copy-print-download\",\"writers-can-share\"],\"meta_info\":{\"access\":\""
                + restrictionType
                + "\",\"collabs\":[]}}]}]},\"app\":\"Google Apps\"}}";
        Reporter.log("Remediation Payload :" + payload, true);
        HttpResponse response = restClient.doPost(new URI(urlUIRemediation), getHeaders(), null, new StringEntity(payload));
        String responseBody = ClientUtil.getResponseBody(response);
        Reporter.log("Prevent Writer Apply Remediation Response :" + responseBody, true);
    }

    public String generatePayLoadForGDriveRemediation(String tenant, String user,
            String docId, String docType, String accessType, String newValue, String currentLink) {
        String Sample_displayLogJonPayload = "{\"objects\":[{\"db_name\":\""
                + tenant
                + "\",\"user\":\""
                + user
                + "\",\"user_id\":null,\""
                + "doc_id"
                + "\":\""
                + docId
                + "\",\""
                + "doc_type"
                + "\":\""
                + docType
                + "\",\"actions\":[{\"code\":\""
                + accessType
                + "\",\"possible_values\":[\"open-writer\",\"open-reader\",\"open-commenter\",\"open-withlink-writer\",\"open-withlink-reader\",\"open-withlink-commenter\",\"company-writer\",\"company-reader\",\"company-commenter\",\"company-withlink-writer\",\"company-withlink-reader\",\"company-withlink-commenter\"],\"meta_info\":{\"access\":\""
                + newValue
                + "\",\"collabs\":[],\""
                + currentLink
                + "\":\"company\"}}]}]}";

        System.out.println("#### Generated PayLoad :" + Sample_displayLogJonPayload);
        return Sample_displayLogJonPayload;
    }

    public void getSplunkResult() throws SkipException {
        String sourceQuery = "source=\"/var/log/elastica/api/Googleapps_" + suiteData.getTenantName() + "*.log\"";
        String env = suiteData.getEnvironmentName();
        Reporter.log("Actual Doc ID in ES is found NUll..", true);
        Reporter.log("Trying to get the splunk logs and adding into the report...", true);
        Reporter.log("Connector logs: ");
        SplunkQueryResult splunkResult = new SplunkQueryHandlers(suiteData.getEnvironmentName()).executeSplunkQuery(sourceQuery, SplunkConstants.SplunkHosts.valueOf(env.toUpperCase()), "-60m");
        Reporter.log("Number of results for the query " + splunkResult.getSearchQuery() + "::" + splunkResult.getNumberOfResults(), true);
        if (splunkResult.getNumberOfResults() == 0) {
            Reporter.log("Connector is not running/activated or no logs found so far..", true);
        }
        Reporter.log("Result ::" + splunkResult.getQueryResult().toString(), true);
        throw new SkipException("Skipping this exception");
    }

    public String getInvestigateLogs(int from, int to, String facility, HashMap<String, String> hmap, String email,
            String apiServerUrl, String csrfToken, String sessionId, int offset, int limit, String sourceName) throws Exception {
        Reporter.log("Retrieving the logs from Elastic Search ...", true);
        String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
        String tsto = DateUtils.getMinutesFromCurrentTime(to);
        List<NameValuePair> headers = getHeaders();
        String payload = "";
        payload = eSQueryBuilder.getESQuery(tsfrom, tsto, facility, hmap, email, apiServerUrl, csrfToken, sessionId, offset, limit, sourceName);
        Reporter.log("Request body:" + payload, true);
        String path = suiteData.getAPIMap().get("getInvestigateLogs");
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
        Reporter.log("URI ::" + dataUri.toString(), true);
        HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(payload));
        String responseBody = ClientUtil.getResponseBody(response);
        return responseBody;
    }

    public String getExportPayloadForGDrive(String format, String SaaSApp, String csrftoken,
            String sessionid, String apiServerUrl, String userid, String startDate, String endDate) {
        String payLoad = "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""
                + startDate
                + "\",\"to\":\""
                + endDate
                + "\"}}},{\"term\":{\"facility\":\""
                + SaaSApp
                + "\"}}],\"must_not\":[{\"term\":{\"facility\":\"Elastica\"}}]}},\"filter\":{}}},\"from\":0,\"size\":93772,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"facets\":{}},\"startDate\":\""
                + startDate
                + "\",\"endDate\":\""
                + endDate
                + "\",\"format\":\""
                + format
                + "\",\"app\":\""
                + SaaSApp
                + "\",\"sourceName\":\"investigate\",\"apiServerUrl\":\""
                + apiServerUrl
                + "\",\"csrftoken\":\""
                + csrftoken
                + "\",\"sessionid\":\""
                + sessionid
                + "\",\"userid\":\""
                + userid
                + "\"}";
        return payLoad;
    }

    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public Object[][] arrayListToDataProvider(List<List> listOfArrayList) {
        Object[][] objArray = new Object[listOfArrayList.get(0).size()][];
        for (int i = 0; i < listOfArrayList.get(0).size(); i++) {
            objArray[i] = new Object[listOfArrayList.size()];
            for (int x = 0; x < listOfArrayList.size(); x++) {
                objArray[i][x] = listOfArrayList.get(x).get(i);
            }
        }
        return objArray;
    }

    public List<String> generateString(String emailId) {
        List<String> generatedStrings = new ArrayList();
        String[] split = emailId.split("@");
        String s1 = split[0] + "@" + split[1].toUpperCase();
        generatedStrings.add(s1);
        String s2 = split[0].toUpperCase() + "@" + split[1];
        generatedStrings.add(s2);
        String s3 = split[0].toUpperCase() + "@" + split[1].toUpperCase();
        generatedStrings.add(s3);
        int l = emailId.length();
        int s = 0;
        Random rand = new Random();
        s = rand.nextInt(l / 2);
        String s4 = emailId.substring(0, s).toUpperCase() + emailId.substring(s);
        generatedStrings.add(s4);
        String s5 = emailId.substring(0, s) + emailId.substring(s).toUpperCase();
        generatedStrings.add(s5);
        String s6 = emailId.substring(0, s + 1).toUpperCase() + emailId.substring(s + 1);
        generatedStrings.add(s6);
        String s7 = emailId.substring(0, s + 1) + emailId.substring(s + 1).toUpperCase();
        generatedStrings.add(s7);
        String s8 = emailId.substring(0, s + 2).toUpperCase() + emailId.substring(s + 2);
        generatedStrings.add(s8);
        String s9 = emailId.substring(0, s + 2) + emailId.substring(s + 2).toUpperCase();
        generatedStrings.add(s9);
        String s10 = emailId.substring(0, s + 3) + emailId.substring(s + 3).toUpperCase();
        generatedStrings.add(s10);
        int size = generatedStrings.size();
        System.out.println("Size :" + size);
        Reporter.log("====== RANDOM GENERATED EMAIL ID WITH DIFFERENT COMBINATION OF UPPER CASE ============", true);
        int i = 1;
        for (String x : generatedStrings) {
            Reporter.log(i++ + ") " + x, true);
        }
        Reporter.log("======================================================================================", true);
        return generatedStrings;
    }

    public List<String> getStringInMultipleLanguage() {
        List<String> multipleLanguage = new ArrayList();
        multipleLanguage.add("Hindi:" + "नमस्ते");
        multipleLanguage.add("English:" + "Hello");
        multipleLanguage.add("Spanish:" + "Hola");
        multipleLanguage.add("Bangala:" + "হ্যালো");
        multipleLanguage.add("Kannada:" + "ಹಲೋ");
        multipleLanguage.add("Tamil:" + "வணக்கம்");
        multipleLanguage.add("Malyalam:" + "ഹലോ");
        multipleLanguage.add("Bulgarian:" + "Здравей");
        multipleLanguage.add("French:" + "Bonjour");
        multipleLanguage.add("Gujrati:" + "હેલો");
        multipleLanguage.add("Chinese:" + "你好");
        multipleLanguage.add("Greek:" + "Χαίρετε");
        multipleLanguage.add("Arabic:" + "مرحبا");
        multipleLanguage.add("Burmese:" + "ဟယ်လို");
        multipleLanguage.add("Georgian:" + "გაუმარჯოს");
        multipleLanguage.add("Hungarian:" + "Helló");
        multipleLanguage.add("Japanese:" + "こんにちは");
        multipleLanguage.add("Kazakh:" + "Сәлеметсіз бе");
        multipleLanguage.add("Khmer:" + "ជំរាបសួរ");
        multipleLanguage.add("Korean:" + "안녕하세요");
        multipleLanguage.add("Lao:" + "ສະບາຍດີ");
        multipleLanguage.add("Macdonian:" + "Здраво");
        multipleLanguage.add("Mangolian:" + "Сайн уу");
        multipleLanguage.add("Telgu:" + "హలో");
        multipleLanguage.add("Sindhi:" + "سلام");
        multipleLanguage.add("Yidish:" + "העלא");
        multipleLanguage.add("Yorkuba:" + "Pẹlẹ o");
        multipleLanguage.add("Thai:" + "สวัสดี");
        multipleLanguage.add("Tajik:" + "Салом");
        return multipleLanguage;
    }

}
