package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.RawJsonParser;
import java.io.IOException;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.tests.securlets.SecurletsUtils;
import com.google.api.services.gmail.model.Message;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.joda.time.DateTime;

/**
 *
 * @author rahulkumar
 */
public class GDriveSecurletCSVExportWithoutFilter extends GDriveUtils {

    LogValidator logValidator;
    String parentNode = "$.hits.hits[*].source";
    String from_jodaTime;
    int waitTimeToReceiveLog;
    int delayBetweenSaaSActivity;
    String folderId;
    String tmpFolderId;
    String fileId;
    String json_Response = null;
    List<String> filesAndFoldersToBeDeleted = new ArrayList();
    ESQueryBuilder eSQueryBuilder = new ESQueryBuilder();
   
    @Test(groups={"EXPORTACTIVITIES", "P1"})
    public void performExportActivities() throws Exception {
        DateTime dateTime = new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC);
        String endDate = dateTime.toString();
        String startDate = dateTime.minusDays(7).toString();
        List<NameValuePair> headers = getHeaders();
        String path = suiteData.getAPIMap().get("getActivityLogExport");
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
        Reporter.log("URI ::" + dataUri.toString(), true);
        LogUtils.logTestDescription("Export the exposed files to user email and check");
        String[] formatList = {"csv", "cef", "leef"};
        for (String format : formatList) {
            String exportPayload = getExportPayloadForGDrive(format, SaasApp_GoogleApps, suiteData.getCSRFToken(), suiteData.getSessionID(), suiteData.getApiserverHostName(), suiteData.getUsername(), startDate, endDate);
            Reporter.log("Export PayLoad :" + exportPayload, true);
            HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(exportPayload));
            String responseBody = ClientUtil.getResponseBody(response);
            Reporter.log("Response body:" + responseBody, true);
            Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        }
        wait(30, "Waiting After Export Activities...");
    }

    @Test(groups={"EXTERNALUSERACTIVITIES", "P1"},dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-ExportActivities")
    public void validateExportActivities(String testName, String format) throws IOException, URISyntaxException, Exception {
        String query = format + " Log Export Request";
        Message latestMail = this.googleMailServices.getLatestMail(query);
        String downloadHref = this.googleMailServices.getDownloadHref(latestMail);
        Reporter.log("Download URL :" + downloadHref, true);
        HttpResponse response = restClient.doGet(new URI(downloadHref), this.headers);
        Header lastHeader = response.getLastHeader("Content-Disposition");
        HeaderElement[] elements = lastHeader.getElements();
        String fileName = lastHeader.getValue();
        Reporter.log("File Name As In Header <Content-Disposition> :" + fileName, true);
        fileName = fileName.replace("attachment; filename=\"", "").replaceAll("\"", "");
        Reporter.log("Actual File Name Downloaded :" + fileName, true);
        String expectedSubStringInFileName = "log_" + format + "_" + DateUtils.getCurrentDate();
        Reporter.log("Expected SubString In File Name :" + expectedSubStringInFileName, true);
        Assert.assertTrue(fileName.contains(expectedSubStringInFileName), "Wrong Exported File Name Found...");
    }

    @Test(groups={"EXTERNALUSERACTIVITIES", "P1"},dataProviderClass = GDriveDataProvider.class, dataProvider = "data-provider-ExportCSVSecurlet")
    public void validateSecurletCSVExport(String testName, String attachmentQuery, String payload) throws Exception {
        LogUtils.logTestDescription(testName);
        String mailSearchQuery = "GoogleApps Securlet Data Export";
        String path = suiteData.getAPIMap().get("getUIExportCSV");
        if (testName.contains("validate Export Exposed Apps")) {
            path = "/admin/application/list/get_export_csv_apps_data";
            mailSearchQuery = "Google Apps Securlet Data Export";
        }
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, null);
        HttpResponse response = restClient.doPost(dataUri, getHeaders(), null, new StringEntity(payload));
        String responseBody = ClientUtil.getResponseBody(response);
        Reporter.log("Response body:" + responseBody, true);
        wait(30, "## Waiting after securlet Export..." + testName);//Waiting for 30 secs.....
        
        Message latestMail = googleMailServices.getLatestMail(mailSearchQuery);
        String actualFileName = null;
        int fileSize = 0;
        Map<String, byte[]> attachments = googleMailServices.getAttachments(latestMail);
        for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
            String key = entrySet.getKey();
            byte[] value = entrySet.getValue();
            Reporter.log("File Name :" + key + " ## Attachment Size :" + value.length, true);
            actualFileName = key;
            fileSize = value.length;
        }
        String expectedStringInFileName = attachmentQuery;
        // String expectedStringInFileName=attachmentQuery+DateUtils.getCurrentDate();
        Reporter.log("Expected String in File Name :" + expectedStringInFileName, true);
        Assert.assertEquals(fileSize > 0, true, testName + ": File size should not be zero , File Size Found :" + fileSize);
        Assert.assertEquals(actualFileName.contains(expectedStringInFileName), true, testName + " File not found in the attachemnt...");
    }

    public void updateDisplayLogResponse(String msg) throws Exception {
        RawJsonParser.wait(180000,msg);  
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", "Google Drive");
        String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
        String payload = "";
        payload = eSQueryBuilder.getESQuery(from_jodaTime, new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC).toString(),
                SaasApp_GoogleApps, termmap, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_GoogleApps);
        Reporter.log("Request body:" + payload, true);
        String path = suiteData.getAPIMap().get("getInvestigateLogs");
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
        Reporter.log("URI ::" + dataUri.toString(), true);
        HttpResponse response = restClient.doPost(dataUri, getHeaders(), null, new StringEntity(payload));
        this.json_Response = ClientUtil.getResponseBody(response);
    }  

}
