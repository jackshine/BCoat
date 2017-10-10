package com.elastica.beatle.reporting;

import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.logger.Logger;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.universal.common.GoogleMailServices;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author Rahul...
 */
public class AuditDashBoardReportTests extends ReportingUtils {

    protected String sourceID = null;
    protected AuditDSStatusDTO auditDSStatusDTO;
    protected ArrayList<AuditDSStatusDTO> inCompleteDsList = new ArrayList<AuditDSStatusDTO>();

    @Test(threadPoolSize = 10, groups = {"FOLDER", "P1"}, dataProviderClass = AuditDataProvider.class,
            dataProvider = "createDataSource")
    public void createDataSourceTest(String fireWallType) throws IOException, Exception {

        if (DSToBeUploaded.contains(fireWallType)) {
            Reporter.log("********************************* Test Description ****************************************************** ", true);
            Reporter.log("1. Create Datasource through WebUpload Transportation", true);
            Reporter.log("2. Process the Datasource ", true);
            Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
            Reporter.log("********************************************************************************************************* ", true);
            Reporter.log("*************Datasource Creation started for:" + fireWallType + "****************", true);
            String requestPayload = createWebUploadPostBody(fireWallType, suiteData.getEnvironmentName(), AuditTestConstants.AUDIT_WU_DS_NAME);
            Reporter.log("Request Payload: " + requestPayload, true);
            HttpResponse createResp = createDataSource(restClient, new StringEntity(requestPayload));
            Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
            String createResponse = ClientUtil.getResponseBody(createResp);
            Reporter.log("Actual Datasource Response:" + createResponse, true);
            JSONObject createResponseObject = new JSONObject(createResponse);
//Expected values preparation
            JSONObject expectedDSResponse = new JSONObject(requestPayload);
            String expected_str_datasource_name = (String) expectedDSResponse.get("name");
            String expected_str_datasource_format = (String) expectedDSResponse.get("datasource_format");
            String expected_str_datasource_type = (String) expectedDSResponse.get("datasource_type");
            String expected_str_setup_by = suiteData.getUsername();
            String str_last_status_or_detectstatus = "Pending Data";
            String expectedResponse = " [Datasourcename=" + expected_str_datasource_name
                    + ", DatasourceFormat=" + expected_str_datasource_format
                    + ", DatasourceType=" + expected_str_datasource_type
                    + ", SetupBy=" + expected_str_setup_by
                    + ", last_status=" + str_last_status_or_detectstatus
                    + ", last_detect_status=" + str_last_status_or_detectstatus + " ]";

            Reporter.log("Expected Datasource Response fields:" + expectedResponse, true);
            Assert.assertEquals(createResponseObject.get("name"), expected_str_datasource_name);
            Assert.assertNotNull(createResponseObject.get("datasource_format"), "Data Source Format is null");
            Assert.assertFalse(((String) createResponseObject.get("datasource_format")).isEmpty(), "Data source format is empty");
            Assert.assertEquals(createResponseObject.get("datasource_format"), expected_str_datasource_format);
            Assert.assertNotNull(createResponseObject.get("datasource_type"), "Data Source Type is null");
            Assert.assertFalse(((String) createResponseObject.get("datasource_type")).isEmpty(), "Data source type is empty");
            Assert.assertEquals(createResponseObject.get("datasource_type"), expected_str_datasource_type);
            Assert.assertNotNull(createResponseObject.get("setup_by"), "SetUp by is null");
            Assert.assertFalse(((String) createResponseObject.get("setup_by")).isEmpty(), "Set Up by is empty");
            Assert.assertEquals(createResponseObject.get("setup_by"), expected_str_setup_by);
            Assert.assertNotNull(createResponseObject.get("id"), "Data Source Id is null");
            Assert.assertFalse(((String) createResponseObject.get("id")).isEmpty(), "ID is empty");
            Assert.assertNotNull(createResponseObject.get("resource_uri"), "Resource URI is null");
            Assert.assertFalse(((String) createResponseObject.get("resource_uri")).isEmpty(), "resource URI is empty");
            Assert.assertEquals(createResponseObject.get("last_status"), str_last_status_or_detectstatus, "Last status is not \"Pending Data\"");
            Assert.assertEquals(createResponseObject.get("last_detect_status"), str_last_status_or_detectstatus, "Last status is not \"Pending Data\"");
            sourceID = (String) createResponseObject.get("id");
// Get Signed URL for the data Source
            Logger.info("Getting signed URl for " + fireWallType + " to upload");
            List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
            queryParams.add(new BasicNameValuePair("filename", fileProperties.get(fireWallType + ".filename")));
            queryParams.add(new BasicNameValuePair("filetype", "application/zip"));
            HttpResponse signedURLResp = getSignedDataResourceURL(restClient, queryParams, sourceID);
            Assert.assertEquals(signedURLResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
            String signedURLRespString = ClientUtil.getResponseBody(signedURLResp);
            Reporter.log("Actual signedURLResp:" + signedURLRespString, true);
            JSONObject signedURLObject = new JSONObject(signedURLRespString);
            String expectedsignedURLResponse = " [signed_request=" + "Should not be null"
                    + ", url=" + "should not be null"
                    + ", signed_request=" + "signed_request is not empty"
                    + ", url=" + "url is not empty" + " ]";

            Reporter.log("Expected signedURLResp:" + expectedsignedURLResponse, true);
            Assert.assertNotNull(signedURLObject.get("signed_request"), "Signed Request is null");
            Assert.assertNotNull(signedURLObject.get("url"), "Signed URL is null");
            Assert.assertFalse(((String) signedURLObject.get("url")).isEmpty(), "URL is empty");
            Assert.assertFalse(((String) signedURLObject.get("signed_request")).isEmpty(), "Signed request is empty");
            String signedURL = (String) signedURLObject.get("signed_request");

// upload firewall using amazon S3 URL
            Logger.info("Uploading file using S3 signed url for " + fireWallType);
            HttpResponse uploadFileResponse = AuditFunctions.uploadFirewallLogFile(restClient, signedURL.trim(), fileProperties.get(fireWallType + ".LogFilePath"));
            Assert.assertEquals(uploadFileResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
// Poll for data source upload status
            HttpResponse pollForStatusResponse = pollForDataSourceStatus(restClient, sourceID);
            Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
            JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
            Reporter.log("Actual Datasource Response:" + pollRespObject, true);
            String last_Status = pollRespObject.getString("last_status");
            Reporter.log("Actual Datasource Status:" + last_Status, true);
            String expectedCompletedStatus = "Completed";
            Reporter.log("Expected Datasource Status:" + expectedCompletedStatus, true);
            long currentWaitTime = 0;
            while (("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status)) && currentWaitTime <= ReportingUtils.AUDIT_PROCESSING_MAX_WAITTIME) {
                Reporter.log("Datasource Process Wait Time*************** :" + AuditTestConstants.AUDIT_THREAD_WAITTIME, true);
                Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
                currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
                pollForStatusResponse = pollForDataSourceStatus(restClient, sourceID);
                Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
                pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
                last_Status = pollRespObject.getString("last_status");
                Logger.info("Actual Last Status of Datasource: " + sourceID + " is " + last_Status);
                if ("Completed".equals(last_Status) || "Failed".equals(last_Status)) {
                    break;
                }

            }

            if (!"Completed".equals(last_Status)) {
                //call summary and will not produce any results.
                pollForStatusResponse = pollForDataSourceStatus(restClient, sourceID);
                Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
                pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
                auditDSStatusDTO = AuditTestUtils.populateInCompleteDataSources(pollRespObject);
                inCompleteDsList.add(auditDSStatusDTO);
            }

            if (!"Completed".equals(last_Status) || !"Failed".equals(last_Status)) {
                Assert.assertTrue(currentWaitTime <= ReportingUtils.AUDIT_PROCESSING_MAX_WAITTIME, " File processing took more than SLA. Last status of this source file was " + last_Status);
            }
            Assert.assertEquals(pollRespObject.get("last_status"), "Completed", "create data soure procesing is not completed. Last status of this source file was " + last_Status);
            Assert.assertNotNull(pollRespObject.get("resource_uri"), "Resource URI is null. Last status of this source file was " + last_Status);
            Assert.assertFalse(((String) pollRespObject.get("resource_uri")).isEmpty(), "resource URI is empty. Last status of this source file was " + last_Status);
            Assert.assertNotNull(pollRespObject.get("log_format"), "Resource URI is null. Last status of this source file was " + last_Status);
            Assert.assertFalse(((String) pollRespObject.get("log_format")).isEmpty(), "resource URI is empty. Last status of this source file was " + last_Status);
            Assert.assertNotNull(pollRespObject.get("log_transport"), "Resource URI is null. Last status of this source file was " + last_Status);
            Assert.assertFalse(((String) pollRespObject.get("log_transport")).isEmpty(), "resource URI is empty. Last status of this source file was " + last_Status);
            Assert.assertEquals(pollRespObject.getString("log_transport"), AuditTestConstants.AUDIT_WEBUPLOAD_FILEFORMAT, "Log Transport method doesn't match. Last status of this source file was " + last_Status);
            Assert.assertNotNull(pollRespObject.get("id"), "Data Source Id is null. Last status of this source file was " + last_Status);
            Assert.assertFalse(((String) pollRespObject.get("id")).isEmpty(), "ID is empty. Last status of this source file was " + last_Status);
            Assert.assertEquals(pollRespObject.get("last_status_message"), "Logs processed successfully", "Logs processing was not successful. Last status of this source file was " + last_Status);
            Reporter.log("************************** Datasource process Completed sucessfully ******************************", true);
            Reporter.log("**************************Datasource creation Test Completed**************************", true);
        } else {
            String fileName = fileProperties.get(fireWallType + ".filename") + "_" + currentDate;
            Reporter.log("## File Already Exits for TODAY..:" + new Date() + " ## Data Source NAME :" + fileName, true);
            Assert.assertTrue(fileName.contains(currentDate), "Updated Data does not Exists");
        }
    }

    @Test(groups = {"FOLDER", "P1"}, dataProviderClass = AuditDataProvider.class, dataProvider = "rawParameters")
    public void testAuditReportRawParams(String rawParams, String duration) throws Exception {
        Reporter.log("Test Audit ReportSource config object: ", true);

        int expectedCount = 12;
        if (duration.equals("-1d") || duration.equals("-7d") || duration.equals("-1mon")) {
            expectedCount = 2;
        }

        if (duration.equals("-6mon")) {
            expectedCount = 4;
        }

        if (duration.equals("-3mon")) {
            expectedCount = 3;
        }

        if (duration.equals("-1y")) {
            expectedCount = 8;
        }

        List<String> fields = Arrays.asList(rawParams.split(","));
        String reportDataReportJsonResponse = getReportDataRawSelection(fields, ReportingUtils.SOURCE_OBJECT.audit_details.toString(), duration, "eq", "YES");
        long actualCount = (long) RawJsonParser.fetchSingleField(reportDataReportJsonResponse, "$.total");
        int actualcount = (int) actualCount;
        Reporter.log("====================Assertion========================================", true);
        Reporter.log("Duration : " + duration, true);
        Reporter.log("Expected Count : " + expectedCount, true);
        Reporter.log("Actual Count : " + actualcount, true);
        Assert.assertTrue((actualcount - expectedCount) >= 0, "Count Did'nt match...");
    }

    @Test(threadPoolSize = 10, groups = {"FOLDER", "P1"}, dataProviderClass = AuditDataProvider.class, dataProvider = "rawParametersRegression")
    public void testAuditReportRawParamsRegression_ZSCALAR(String rawParams, String duration) throws Exception {
        List<String> fields = Arrays.asList(rawParams.split(","));
        String reportDataReportJsonResponse = getReportDataRawSelection(fields, ReportingUtils.SOURCE_OBJECT.audit_details.toString(), duration, "eq", "YES");
        boolean validateDashboardReport = validateDashboardReport("be_zscalar", duration, reportDataReportJsonResponse, fields, AuditDataProvider.getIgnoredKeyList());
        Reporter.log("==================== Assertion ========================================", true);
        Reporter.log("Assertion validateDashboardReport : " + validateDashboardReport, true);
        Assert.assertTrue(validateDashboardReport);

    }

    @Test(threadPoolSize = 10, groups = {"FOLDER", "P1"}, dataProviderClass = AuditDataProvider.class, dataProvider = "rawParametersRegression")
    public void testAuditReportRawParamsRegression_BLUECOATPROXY(String rawParams, String duration) throws Exception {
        List<String> fields = Arrays.asList(rawParams.split(","));
        String reportDataReportJsonResponse = getReportDataRawSelection(fields, ReportingUtils.SOURCE_OBJECT.audit_details.toString(), duration, "eq", "YES");
        boolean validateDashboardReport = validateDashboardReport("be_bluecoat_proxy", duration, reportDataReportJsonResponse, fields, AuditDataProvider.getIgnoredKeyList());
        Reporter.log("==================== Assertion ========================================", true);
        Reporter.log("Assertion validateDashboardReport : " + validateDashboardReport, true);
        Assert.assertTrue(validateDashboardReport);
    }

    @Test
    public void validateAuditScheduleReport() throws IOException {
        String query = "Elastica Cloud Services Risk Assessment Report";
        GoogleMailServices googleMailServices = new GoogleMailServices(admin_clientID_gmail, admin_clientSecret_gmail, admin_refreshToken_gmail);
        Message latestMail = googleMailServices.getLatestMail(query);
        Message message = googleMailServices.getMessage(latestMail.getId());
        MessagePart payload = message.getPayload();
        List<MessagePartHeader> headers = payload.getHeaders();
        String actualdate = null;
        for (MessagePartHeader header : headers) {
            String key = header.getName();
            String value = header.getValue();
            if (key.equals("Date")) {
                actualdate = value;
            }
        }
        Reporter.log("----------------- Date Validation -----------", true);
        Reporter.log("Actual Date :" + actualdate, true);
        String pattern = "EEE, dd MMM yyyy ";
        String todayDate = new SimpleDateFormat(pattern).format(new Date());
        Reporter.log("Expected Curent Date :" + todayDate, true);
        boolean dateValidation = actualdate.contains(todayDate);
        Reporter.log("Data Validation/Assertion :" + dateValidation, true);
        Assert.assertTrue(dateValidation, "Daily Report Audit Report Mail Not Found...");

        Reporter.log("----------------- Snippet Validation -----------", true);
        String actualSnippet = message.getSnippet();
        String expectedSnippet = "Hi , Please Download your Daily Cloud Services Risk Assessment Report of your organization to gain";
        Reporter.log("Actual Snippet :" + actualSnippet, true);
        Reporter.log("Expected Snippet :" + expectedSnippet, true);
        boolean SnippetValidation = actualSnippet.contains(expectedSnippet);
        Reporter.log("Data Validation/Assertion :" + dateValidation, true);
        Assert.assertTrue(SnippetValidation, "Incorrent Snippet Found in Message Body...");
    }

}
