package com.elastica.beatle.reporting;

import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.reporting.AuditLogHelper.LogFormat;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.universal.common.GExcelDataProvider;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ReportingUtils extends CommonTest {

    LogFormat logFormat = AuditLogHelper.LogFormat.ZSCALER;
    String expectedDataSheet = "ZScalar Expected Data Set.xlsx"; // expected-bluecoat-vald-auto
    public static long AUDIT_PROCESSING_MAX_WAITTIME = 1800000; // in Minutes..webupload SLA..
    String admin_clientID_gmail = "998314684213-34jm3g4k92nejio174qnnb32vojbqg0n.apps.googleusercontent.com";
    String admin_clientSecret_gmail = "YkMqf5GWiHQgHbA1P7BNhxto";
    String admin_refreshToken_gmail = "1/fPDwECsPhJAPh6F_TsHHhz7Q1AArPFC-fa6XdV_bNko";

    public enum SOURCE_OBJECT {

        audit_details, file_sharing_securlets
    };

    String reportUrl;
    List<NameValuePair> headers;

    public String AUDIT_FIREWALL_CONFIG_FILEPATH = "/src/test/resources/reporting/suites/metrics/LogFileAttribute.xml";
    Map<String, String> fileProperties;
    String zipFileLocation;
    String absoluteZipFileLocation;
    String tmpdir = System.getProperty("user.dir") + File.separator;
    boolean dataSourceUpload = false;
    String tmpZipFileLocation;
    String dataSourceInfo;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    GExcelDataProvider expectedData;
    AuditLogHelper helper = new AuditLogHelper();
    String suitName;

    Map<String, LogFormat> logFormats = new HashMap<>();
    Map<String, String> seedData = new HashMap<>();

    List<String> fireWallTypes = new ArrayList<>();
    List<String> DSToBeUploaded= new ArrayList<>();

    public void initFireWallTypes() {
        fireWallTypes.add("be_zscalar");
        fireWallTypes.add("be_bluecoat_proxy");
    }

    public void initLogFormat() {
        logFormats.put("be_zscalar", AuditLogHelper.LogFormat.ZSCALER);
        logFormats.put("be_bluecoat_proxy", AuditLogHelper.LogFormat.BLUECOAT);
    }

    public void initSeedData() {
        seedData.put("be_zscalar", "ZScalar");
        seedData.put("be_bluecoat_proxy", "Bluecoat");
    }

    @BeforeTest(alwaysRun = true)
    public void initTests(ITestContext suiteConfigurations) throws Exception {
        Logger.info("=================================Initializing Framework related configurations=========================================================");
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), "/reportsources/data/");
        Reporter.log("## Report URI ::" + dataUri.toString(), true);
        this.reportUrl = dataUri.toString();
        Logger.info("## Tenant Name :" + suiteData.getTenantName());
        HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(), suiteData.getPassword(), suiteData);
        suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
        suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));
        suiteData.setAuthParam(AuthorizationHandler.getAuthParam(suiteData.getUsername(), suiteData.getPassword()));

        this.headers = getHeaders();
        Reporter.log("Loading the configuration into Proterties File...", true);
        this.fileProperties = FileHandlingUtils.readPropertyFile(AUDIT_FIREWALL_CONFIG_FILEPATH);
        initFireWallTypes();
        initLogFormat();
        Map<String, String> allDataSourceIdAndName = getAllDataSourceIdAndName();

        //=====
        this.suitName = suiteConfigurations.getCurrentXmlTest().getSuite().getName();
        Reporter.log("### Suite Name :" + this.suitName, true);
        if (this.suitName.contains("Smoke")) {
            Reporter.log("=========================================================",true);
            Reporter.log(" ################## Sanity Suite Found ##################",true);
            Reporter.log("=========================================================",true);
            this.fireWallTypes.clear();
            this.fireWallTypes.add("be_zscalar");
        }

        performDSCleanUp(allDataSourceIdAndName);
                
        for (String fireWallType : fireWallTypes) {
            this.zipFileLocation = fileProperties.get(fireWallType + ".LogFilePath");
            this.absoluteZipFileLocation = System.getProperty("user.dir") + File.separator + this.zipFileLocation;
            String zipFileName = new File(this.zipFileLocation).getName();
            this.tmpZipFileLocation = tmpdir + zipFileName;
            File file = new File(tmpdir + zipFileName);
            if (file.exists()) {
                file.delete();
            }
            String FileName = fileProperties.get(fireWallType + ".filename") + "_" + currentDate;
            if (allDataSourceIdAndName.containsKey(FileName)) {
                Reporter.log("## File Already Exits for TODAY..:" + new Date() + " ## Data Source NAME :" + FileName, true);
                this.dataSourceUpload = false;
            } else {
                Reporter.log("New File needs to be created and uploaded...", true);
                this.dataSourceUpload = true;
                Reporter.log("## Delete the existing older DataSource...", true);
            }
            if (dataSourceUpload) {
                String fileInsideZip = getFileFromZip(this.absoluteZipFileLocation);
                unzipFile(this.absoluteZipFileLocation, this.tmpdir);
                helper.produceAuditLogs(tmpdir + fileInsideZip, tmpdir + "latest_" + fileInsideZip, logFormats.get(fireWallType));
                zipFile(tmpdir + fileInsideZip + ".zip", tmpdir + "latest_" + fileInsideZip);
                new File(tmpdir + "latest_" + fileInsideZip).delete();
                new File(tmpdir + fileInsideZip).delete();
                DSToBeUploaded.add(fireWallType);
            }
            fileProperties.put(fireWallType + ".LogFilePath", File.separator + zipFileName);
        }

        initExpectedData();
    }

    public void performDSCleanUp(Map<String, String> allDataSourceIdAndName) {
        // Perform Clean Up for Older DS..
        Set<String> keySet = allDataSourceIdAndName.keySet();
        for (String DSname : keySet) {
            if(!DSname.contains(currentDate))
                if(!DSname.equals("SAMPLE_DATASOURCE")){
                deleteDataSource(allDataSourceIdAndName.get(DSname));
                }
        }
    }

    public void initExpectedData() {
        this.expectedData = new GExcelDataProvider(System.getProperty("user.dir") + "/src/test/resources/reporting/suites/metrics/" + expectedDataSheet);
    }

    public String getReportDataRawSelection(List<String> fields, String sourceObject, String duration,
            String operatorType, String operatorValue) {
        HttpResponse response = null;
        String samplePayLoad = getPayLoad(fields, sourceObject, duration, operatorType, operatorValue);
        try {
            response = restClient.doPost(new URI(reportUrl), headers, null, new StringEntity(samplePayLoad));
            Reporter.log("### ------------------------------------------------------------###", true);
            Reporter.log("Payload for Report :" + samplePayLoad, true);
            Reporter.log("### ------------------------------------------------------------###", true);
        } catch (Exception ex) {
            Reporter.log("Report GET data Exception :" + ex.getLocalizedMessage(), true);
        }
        String jsonResponse = ClientUtil.getResponseBody(response);
        Reporter.log("Report/Result Response :" + jsonResponse, true);
        return jsonResponse;
    }

    public String getDataSourceId() {
        String dataSourceId = (String) RawJsonParser.fetchSingleField(this.dataSourceInfo, "$.tenant.datasources[0].id");
        Reporter.log("## DataSourceId :" + dataSourceId, true);
        return dataSourceId;
    }

    public Map<String, String> getAllDataSourceIdAndName() {
        List<String> keys = new ArrayList<>();
        keys.add("id");
        keys.add("name");
        Map<String, String> dataSourceMap = new HashMap<>();
        List<Map<String, Object>> requiredKeys = RawJsonParser.getRequiredKeys(getExistingDataSourceInfo(), "$.tenant.datasources[*]", keys);
        for (Map<String, Object> requiredKey : requiredKeys) {
            dataSourceMap.put(requiredKey.get("name").toString(), requiredKey.get("id").toString());
        }
        Reporter.log("#### Available Data Source" + dataSourceMap, true);
        return dataSourceMap;
    }

    private String getExistingDataSourceInfo() {
        String responseBody = null;
        try {
            List<NameValuePair> qparams = new ArrayList<>();
            qparams.add(new BasicNameValuePair("order_by", "setup_date"));
            URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/risks/datasources", qparams);
            HttpResponse response = restClient.doGet(uri, headers);
            responseBody = ClientUtil.getResponseBody(response);
            Reporter.log("## Existing Data Source Info :" + responseBody, true);
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(ReportingUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ReportingUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseBody;

    }

    public HttpResponse deleteDataSource(Client restClient, String sourceId) {
        try {
            String restAPI = replaceGenericParams("/{tenantName}/api/admin/{version}/datasources/{sourceID}/");
            restAPI = restAPI.replace("{sourceID}", sourceId);
            URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), restAPI);
            Logger.info("Deleting the data resource: " + sourceId);
            return restClient.doDelete(uri, buildBasicHeaders());
        } catch (Exception ex) {
            Reporter.log("##### Issue Found In Delete Data Source :"+ex.getLocalizedMessage(),true);
        }
        return null;
    }

    public String deleteDataSource(String dataSourceId) {
        String payLoad = "{\"id\":\"" + dataSourceId + "\"}";
        String responseBody = null;
        try {
            List<NameValuePair> qparams = new ArrayList<>();
            qparams.add(new BasicNameValuePair("order_by", "setup_date"));
            URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/risks/deletedatasource", qparams);
            HttpResponse response = restClient.doPost(uri, headers, null, new StringEntity(payLoad));
            responseBody = ClientUtil.getResponseBody(response);
            Reporter.log("## Delete Data Source Response :" + responseBody, true);
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(ReportingUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ReportingUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseBody;
    }

    private String getPayLoadForAggregated(List<String> fields, String sourceObject, String duration,
            String operatorType, String operatorValue) {
        String rawParams = "";
        java.util.Date date = new java.util.Date();
        String timeStamp = new Timestamp(date.getTime()).toString();
        for (String field : fields) {
            String s = "\"" + field + "\"" + ",";
            rawParams = rawParams + s;
        }
        rawParams = rawParams.substring(0, rawParams.length() - 1);
        String samplePayLoad = null;
        try {
            samplePayLoad = "{\"source\":\""
                    + sourceObject
                    + "\",\"reportconfig\":{\"start\":0,\"filters\":{\"AND\":[{\"field\":\"service_info.sc_16\",\"type\":\"choice\","
                    + "\"op\":\""
                    + operatorType
                    + "\",\"value\":[\""
                    + operatorValue
                    + "\"]}]},\"limit\":100,"
                    + "\"fields\":"
                    + "["
                    + rawParams
                    + "],"
                    + "\"duration\":"
                    + "\""
                    + duration
                    + "\"},"
                    + "\"apiServerUrl\":\""
                    + "https://api-eoe.elastica-inc.com/"
                    + "\","
                    + "\"csrftoken\":\""
                    + suiteData.getCSRFToken()
                    + "\","
                    + "\"sessionid\":\""
                    + suiteData.getSessionID()
                    + "\","
                    + "\"userid\":\""
                    + suiteData.getUsername()
                    + "\","
                    + "\"userip\":\""
                    + getIPAddressInfo()
                    + "\"request_time\":\""
                    + timeStamp
                    + "\"}";
        } catch (Exception ex) {
            Reporter.log("Exception in payload generation :" + ex.getLocalizedMessage(), true);
        }
        return samplePayLoad;
    }

    public String getPayLoad(List<String> fields, String sourceObject, String duration,
            String operatorType, String operatorValue) {
        String samplePayLoad = null;
        try {
            String rawParams = "";
            java.util.Date date = new java.util.Date();
            String timeStamp = new Timestamp(date.getTime()).toString();
            for (String field : fields) {
                String s = "\"" + field + "\"" + ",";
                rawParams = rawParams + s;
            }
            rawParams = rawParams.substring(0, rawParams.length() - 1);
            samplePayLoad = "{\"source\":\""
                    + "audit_details"
                    + "\",\"reportconfig\":{\"start\":0,\"filters\":{},\"limit\":100,\""
                    + "fields\":"
                    + "["
                    + rawParams
                    + "]"
                    + ",\""
                    + "duration\":\""
                    + duration
                    + "\"},\"apiServerUrl\":\"https://api-eoe.elastica-inc.com/\",\"csrftoken\":\""
                    + suiteData.getCSRFToken()
                    + "\",\"sessionid\":\""
                    + suiteData.getSessionID()
                    + "\",\"userid\":\""
                    + suiteData.getUsername()
                    + "\",\"userip\":\""
                    + getIPAddressInfo()
                    + "\"}";
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ReportingUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return samplePayLoad;
    }

    /**
     * @param fireWallType
     * @return
     * @throws JSONException
     */
    public String createWebUploadPostBody(String fireWallType, String env, String transportType) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", generateDatasourceName(fireWallType, env + getCompressFormat(fireWallType), transportType));
        json.put("datasource_type", fileProperties.get(fireWallType + ".DataSourceType"));
        json.put("datasource_format", fileProperties.get(fireWallType + ".DataSourceFormat"));
        json.put("log_transport", AuditTestConstants.AUDIT_WEBUPLOAD_FILEFORMAT);
        json.put("log_format", fileProperties.get(fireWallType + ".log_format"));
        json.put("type", fileProperties.get(fireWallType + ".type"));
        return json.toString();
    }

    private String generateDatasourceName(String fireWallType, String env, String transportType) {
        return fileProperties.get(fireWallType + ".filename") + "_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private String getCompressFormat(String fireWallType) {
        return (fireWallType.contains("7z") ? "7z" : fireWallType.contains("7za") ? "7za" : fireWallType.contains("bz2") ? "bz2" : fireWallType.contains("gz") ? "gz" : "zip");
    }

    public HttpResponse getDataSource(Client restClient, HttpEntity entity) throws Exception {
        String restAPI = replaceGenericParams("/{tenantName}/api/admin/{version}/datasources/");
        List<NameValuePair> queryParam = new ArrayList<>();
        queryParam.add(new BasicNameValuePair("fields", "datasources"));
        queryParam.add(new BasicNameValuePair("limit", "100"));
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), restAPI, queryParam);
        Reporter.log("Headers :" + GetHeaders(), true);
        return restClient.doGet(dataUri, GetHeaders());
    }

    public HttpResponse createDataSource(Client restClient, HttpEntity entity) throws Exception {
        String restAPI = replaceGenericParams("/{tenantName}/api/admin/{version}/datasources/");
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), restAPI);
        Reporter.log("Headers :" + GetHeaders(), true);
        return restClient.doPost(dataUri, GetHeaders(), null, entity);
    }

    public HttpResponse getSignedDataResourceURL(Client restClient, List<NameValuePair> queryParams, String sourceID) throws Exception {
        String signedURL = replaceGenericParams("/{tenantName}/api/admin/{version}/datasources/upload_url/{sourceID}/");
        signedURL = signedURL.replace("{sourceID}", sourceID);
        URI signedURI = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), signedURL, queryParams);
        Logger.info(" Getting signed URL for dataSource: " + sourceID);
        return restClient.doGet(signedURI, buildBasicHeaders());
    }

    private String replaceGenericParams(String url) {
        if (url.contains("tenantName")) {
            url = url.replace("{tenantName}", suiteData.getTenantName());
        }
        if (url.contains("version")) {
            url = url.replace("{version}", suiteData.getBaseVersion());
        }
        return url;
    }

    public HttpResponse pollForDataSourceStatus(Client restClient, String sourceID) throws Exception {
        String restAPI = replaceGenericParams("/{tenantName}/api/admin/{version}/datasources/{sourceID}");
        restAPI = restAPI.replace("{sourceID}", sourceID);
        URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), restAPI);
        Logger.info("Looking for Data Source Creating Status: ");
        return restClient.doGet(uri, buildBasicHeaders());
    }

    public List<NameValuePair> GetHeaders() {
        List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
        requestHeader.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
        requestHeader.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
        requestHeader.add(new BasicNameValuePair(HttpHeaders.COOKIE, "sessionid=" + suiteData.getSessionID() + "; csrftoken=" + suiteData.getCSRFToken() + ";"));
        requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, suiteData.getAuthParam()));
        requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
        requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
        Reporter.log("requestHeader Mallesh..." + requestHeader, true);
        return requestHeader;
    }

    public HttpResponse notifyFileUploadStatus(Client restClient, String id) throws IOException, Exception {
        String notifyURL = replaceGenericParams("/{tenantName}/api/admin/{version}/datasources/upload_status/{sourceID}");
        notifyURL = notifyURL.replace("{sourceID}", id);
        String postBody = "{\"datasource_id\":\"" + id + "\", \"last_status\":\"success\"}";
        Logger.info("*******Request payload for Notifying the upload status********: " + postBody);
        StringEntity entity = new StringEntity(postBody);
        Logger.info("Notifying the upload status");
        return restClient.doPost(ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), notifyURL), GetHeaders(), null, entity);
    }

    public void zipFile(String destinationZipLocation, String sourceFileLocation) {
        byte[] buffer = new byte[1024];
        try {
            FileOutputStream fos = new FileOutputStream(destinationZipLocation);
            try (ZipOutputStream zos = new ZipOutputStream(fos)) {
                ZipEntry ze = new ZipEntry(new File(sourceFileLocation).getName());
                zos.putNextEntry(ze);
                try (FileInputStream in = new FileInputStream(sourceFileLocation)) {
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
                zos.closeEntry();  //remember close it     
            }
            System.out.println("Done");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void unzipFile(String zipFilePath, String destDirectory) {
        Reporter.log("Unzipping the file From :" + zipFilePath + "....TO :" + destDirectory, true);
        ZipInputStream zipIn = null;
        try {
            File destDir = new File(destDirectory);
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (FileNotFoundException ex) {
            //java.util.logging.Logger.getLogger(AppMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //java.util.logging.Logger.getLogger(AppMain.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                zipIn.close();
            } catch (IOException ex) {
                //java.util.logging.Logger.getLogger(AppMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Reporter.log("## File Unzipp is successfully Completed !!!!", true);
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        int BUFFER_SIZE = 4096;
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public static String getFileCreationDate(String fileLocation) {
        Path path = Paths.get(fileLocation);
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ReportingUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        String creationTime = attr.creationTime().toString();
        Reporter.log("Creation date:" + creationTime, true);
        return creationTime;
    }

    public String getFileFromZip(String zipFileLocation) {
        String fileName = null;
        try (ZipFile zipFile = new ZipFile(zipFileLocation)) {
            Enumeration zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                fileName = ((ZipEntry) zipEntries.nextElement()).getName();
                System.out.println(fileName);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ReportingUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileName;

    }

     public List<Map<String, Object>> getExpectedResultForFirewallType(List<String> fields, String duration,String fireWallType) {
        List<Map<String, Object>> durationFilteredMapList = new ArrayList();
        initSeedData();
        List<String> paramColumns = AuditDataProvider.getParamColumns(fields);
        Reporter.log("## Param Columns :" + paramColumns, true);
            List<Map<String, Object>> dataAsMapList = this.expectedData.getDataAsMapList(seedData.get(fireWallType), paramColumns);
            duration = duration.replace("-", "");
            for (Map<String, Object> dataAsMapList1 : dataAsMapList) {
                if (dataAsMapList1.get("DURATION").toString().contains(duration)) {
                    durationFilteredMapList.add(dataAsMapList1);
                }
            } 
        return durationFilteredMapList;
    }

    
    public List<Map<String, Object>> getExpectedResult(List<String> fields, String duration) {
        List<Map<String, Object>> durationFilteredMapList = new ArrayList();
        initSeedData();
        List<String> paramColumns = AuditDataProvider.getParamColumns(fields);
        Reporter.log("## Param Columns :" + paramColumns, true);
        for (String fireWallType : fireWallTypes) {
            List<Map<String, Object>> dataAsMapList = this.expectedData.getDataAsMapList(seedData.get(fireWallType), paramColumns);
            duration = duration.replace("-", "");
            for (Map<String, Object> dataAsMapList1 : dataAsMapList) {
                if (dataAsMapList1.get("DURATION").toString().contains(duration)) {
                    durationFilteredMapList.add(dataAsMapList1);
                }
            }
        }
        return durationFilteredMapList;
    }

    public static Object[][] arrayListToDataProvider(List<List> listOfArrayList) {
        Object[][] objArray = new Object[listOfArrayList.get(0).size()][];
        for (int i = 0; i < listOfArrayList.get(0).size(); i++) {
            objArray[i] = new Object[listOfArrayList.size()];
            for (int x = 0; x < listOfArrayList.size(); x++) {
                objArray[i][x] = listOfArrayList.get(x).get(i);
            }
        }
        return objArray;
    }

    public boolean validateDashboardReport(String firewallType,String duration, String jsonResponse, List<String> fields, List<String> ignoredkeyList) {
        boolean matchMapFromMapList = true;
        List<Map<String, Object>> expectedResult = getExpectedResultForFirewallType(fields, duration,firewallType);
        Reporter.log("---------------------------------------------------------------------------------------------",true);
        Reporter.log("--------------------------------- | EXPECTED RESULT | --------------------------------------", true);
        Reporter.log("---------------------------------------------------------------------------------------------",true);
        List<Map<String, Object>> actualMapList = getFilteredMap(expectedResult, fields);
        RawJsonParser.printMapList(actualMapList);
        Map<Integer,Map<String, Object>> conclusion=new HashMap();
        Reporter.log("---------------------------------------------------------------------------------------------",true);
        Reporter.log("--------------------------------- | ACTUAL RESULT | --------------------------------------", true);
        Reporter.log("---------------------------------------------------------------------------------------------",true);
        List<Map<String, Object>> expectedMapList = getMaplistFromJsonResponse(jsonResponse);
        RawJsonParser.printMapList(expectedMapList);
        boolean areEqual = true;
        Reporter.log("---------------------------------------------------------------------------------------------",true);
        Reporter.log("--------------------------------- | VALIDATION | --------------------------------------", true);
        Reporter.log("---------------------------------------------------------------------------------------------",true);
        for (Map<String, Object> actualMapList1 : actualMapList) {
            Map<String, MapDifference.ValueDifference<Object>> entriesDiffering;   
            for (int y = 0; y < expectedMapList.size(); y++) {             
                MapDifference<String, Object> difference = Maps.difference(actualMapList1, expectedMapList.get(y));
                areEqual = difference.areEqual();  
                entriesDiffering = difference.entriesDiffering();
                if(entriesDiffering.size()>0){
                conclusion.put(entriesDiffering.size(), expectedMapList.get(y));
                }  
                if (!areEqual) {   
                    List<String> keys = new ArrayList<String>(entriesDiffering.keySet());
                    if (ignoredkeyList.containsAll(keys)) {
                        areEqual = true;
                    } else {
                        areEqual = false;
                    }
                }
                if (areEqual) {
                    Reporter.log("$$ Found in Actual Result : "+expectedMapList.get(y),true);
                    expectedMapList.remove(y);  
                    break;
                } 
            }
            if (areEqual == false) {
                Reporter.log("---------------------------------------------------------------------------------------------",true);
                Reporter.log("------------------------| Detailed Assertion Failure Explanation |----------------------------", true);
                Reporter.log("---------------------------------------------------------------------------------------------",true);
                Reporter.log("Expected Result  :--", true);
                Reporter.log("---------------------------------------------------------------------------------------------",true);
                RawJsonParser.printMap(actualMapList1);
                Reporter.log("---------------------------------------------------------------------------------------------",true);
                Reporter.log("Actual Result  :--", true);
                Reporter.log("---------------------------------------------------------------------------------------------",true);
                Set<Integer> keySet = conclusion.keySet();
                Object[] toArray = keySet.toArray();
                Arrays.sort(toArray);
                Map<String, Object> get = conclusion.get(toArray[0]);
                RawJsonParser.printMap(get);
                Reporter.log("---------------------------------------------------------------------------------------------",true);
                Reporter.log("--------------------------------| Differences Found |----------------------------------------",true);
                Reporter.log("---------------------------------------------------------------------------------------------",true);
                MapDifference<String, Object> differencew = Maps.difference(actualMapList1, get);
                Reporter.log("Ignored <Dynamic> Keys :--",true);
                Reporter.log(ignoredkeyList.toString(),true);
                Reporter.log("Difference  :--",true);
                Reporter.log(differencew.toString(),true);
                Reporter.log("---------------------------------------------------------------------------------------------",true);
                return false;
            }
        }
        Reporter.log("---------------------------------------------------------------------------------------------",true);
        Reporter.log("!!! All the Expected Result Found in Actual Response !!!",true);
        Reporter.log("---------------------------------------------------------------------------------------------",true);
        return matchMapFromMapList;
    }

    public boolean validateDashboardReport(String duration, String jsonResponse, List<String> fields, List<String> ignoredkeyList) {
        boolean matchMapFromMapList = true;
        List<Map<String, Object>> expectedResult = getExpectedResult(fields, duration);
        Reporter.log("##### =============== EXPECTED Results ================ #####", true);
        List<Map<String, Object>> expectedMapList = getFilteredMap(expectedResult, fields);
        RawJsonParser.printMapList(expectedMapList);

        Reporter.log("##### =============== ACTUAL Results ================ #####", true);
        List<Map<String, Object>> actualMapList = getMaplistFromJsonResponse(jsonResponse);
        RawJsonParser.printMapList(actualMapList);
        boolean actualMapFound = true;
        boolean areEqual = true;
        Reporter.log("##### =============== VALIDATION ACTUAL RESULT================ #####");
        for (Map<String, Object> actualMapList1 : actualMapList) {

            for (int y = 0; y < expectedMapList.size(); y++) {
                Reporter.log("=========================================================", true);
                Reporter.log("Proceeding to Perform check to Next Element/Map :Expected :" + expectedMapList.get(y) + " #Actual :" + actualMapList1, true);
                Reporter.log("=========================================================", true);
                MapDifference<String, Object> difference = Maps.difference(actualMapList1, expectedMapList.get(y));
                areEqual = difference.areEqual();
                Reporter.log("Equality Check : " + difference.areEqual(), true);
                Reporter.log("Difference :" + difference.toString(), true);
                if (!areEqual) {
                    Map<String, MapDifference.ValueDifference<Object>> entriesDiffering = difference.entriesDiffering();
                    List<String> keys = new ArrayList<String>(entriesDiffering.keySet());
                    if (ignoredkeyList.containsAll(keys)) {
                        areEqual = true;
                        Reporter.log("Only Ignored Keys Found in the Result Diff :" + keys, true);
                    } else {
                        Reporter.log("Few Keys Value Mismatch Found in the Result Diff :" + entriesDiffering.toString(), true);
                        areEqual = false;
                    }
                }
                if (areEqual) {
                    expectedMapList.remove(y);
                    actualMapFound = true;
                    break;
                }
            }
            if (areEqual == false) {
                Reporter.log("========================== Assertion Failure Explanation ===================================", true);
                Reporter.log("Assertion Failure Actual Result :" + actualMapList1 + " ## Not Found in Below Expected Results ....", true);
                RawJsonParser.printMapList(expectedMapList);
                Reporter.log("=========================================================", true);
                return false;
            }
        }
        return matchMapFromMapList;
    }

    public List<Map<String, Object>> getMaplistFromJsonResponse(String jsonResponse) {
        List<Map<String, Object>> actualResultSet = new ArrayList<>();
        List<String> resultheaders = (List) RawJsonParser.fetchSingleField(jsonResponse, "$.headers");
        Reporter.log("Headers :" + resultheaders, true);
        List<List> fetchSingleField = (List<List>) RawJsonParser.fetchSingleField(jsonResponse, "$.data");
        for (List singleResultSet : fetchSingleField) {
            int i = 0;
            Map<String, Object> map = new HashMap();
            for (Object singleResultSet1 : singleResultSet) {

                if (singleResultSet1 == null) {
                    map.put(AuditDataProvider.rawParamMap.get(resultheaders.get(i++)), "null");
                } else {
                    Class<? extends Object> aClass = singleResultSet1.getClass();
                    String name = aClass.getName();
                    if (name.equals("java.lang.Integer")) {
                        map.put(AuditDataProvider.rawParamMap.get(resultheaders.get(i++)), String.valueOf(singleResultSet1));
                    } else if (name.equals("java.lang.Integer")) {
                        map.put(AuditDataProvider.rawParamMap.get(resultheaders.get(i++)), String.valueOf(singleResultSet1));
                    } else {
                        map.put(AuditDataProvider.rawParamMap.get(resultheaders.get(i++)), singleResultSet1.toString());
                    }
                }
            }

            actualResultSet.add(map);
        }
        return actualResultSet;
    }

    public List<Map<String, Object>> getFilteredMap(List<Map<String, Object>> expectedResult, List<String> keyList) {
        keyList = AuditDataProvider.getParamColumns(keyList);
        List<Map<String, Object>> exResult = new ArrayList();
        Reporter.log(" $$ Result Count :" + expectedResult.size(), true);
        for (Map<String, Object> expectedResult1 : expectedResult) {
            Map<String, Object> map = new HashMap();
            for (Map.Entry<String, Object> entrySet : expectedResult1.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue().toString();
                if (keyList.contains(key)) {
                    if (value.equals("BLANK")) {
                        map.put(key, "");
                    } else {
                        map.put(key, value.toString().replaceAll("#", ""));
                    }
                }
            }
            exResult.add(map);
        }
        return exResult;
    }

}
