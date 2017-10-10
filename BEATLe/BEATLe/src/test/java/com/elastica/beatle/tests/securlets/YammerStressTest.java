/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle.tests.securlets;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.RestClient.CommonClient;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.protect.ProtectQueryBuilder;
import com.google.common.collect.HashBiMap;
import com.universal.common.GExcelDataProvider;
import com.universal.common.GoogleMailServices;
import com.universal.common.Yammer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class YammerStressTest extends CommonTest {

    public static Map<String, Object> successResult = new Hashtable<>();
    public static Map<String, Object> failureResult = new Hashtable<>();
    public static Map<String, Object> usersInfo = new Hashtable<>();
    public static Map<String, String> tokenMap = new HashMap();
    public static List<String> userIdList = new ArrayList();
    public static List<Map<String, Object>> YammerActivities = new ArrayList<>();
    public static String fileLocationIntervalReport = "/tmp/YammerReport_IntervalReport.xls";
    public static HSSFWorkbook workbook_IntervalReport = new HSSFWorkbook();
    public static HSSFSheet sheet_IntervalReport;
    public static HSSFSheet sheet_SoCReport;
    public static int rowCount_IntervalReport = 0;
    public static int rowCount_IntervalReportSoC = 0;
    public static String from_jodaTime = new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC).toString();
    public static String to_jodaTime = new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC).toString();
    public static List<NameValuePair> headers;
    public static TestSuiteDTO data;
    public static Client client;
    public static List<String> usersPresentInCloudSoc = new ArrayList<>();
    public static List<String> usersNotInCloudSoc = new ArrayList<>();

    @Test
    public void performYammerStressTest() throws IOException, InterruptedException, Exception {
        YammerStressTest.headers = getHeaders();
        YammerStressTest.data = suiteData;
        YammerStressTest.client = restClient;
        YammerStressTest.printTotalCount();
        YammerStressTest.main(null);
        System.out.println("## User Found in the SOC :"+YammerStressTest.usersPresentInCloudSoc.toString());
        System.out.println("## User NOT Found in the SOC :"+YammerStressTest.usersNotInCloudSoc.toString());
    }

    public static void printTotalCount() throws URISyntaxException, Exception {
        String url_TotalCount = "https://eoe.elastica-inc.com/admin/user/ng/list/0?limit=30&offset=0";
        HttpResponse doGet = YammerStressTest.client.doGet(new URI(url_TotalCount), YammerStressTest.headers);
        Reporter.log("Total Count Response :" + ClientUtil.getResponseBody(doGet), true);
    }

    public static Long getImportedUserInfo(String userId) throws Exception {
        String url = "https://eoe.elastica-inc.com/admin/user/ng/list/0?email=" + userId;
        HttpResponse doGet1 = YammerStressTest.client.doGet(new URI(url), YammerStressTest.headers);
        String query = "$.meta.total_count";
        String response=ClientUtil.getResponseBody(doGet1);
        Reporter.log("Fetch User Info <"+userId+">:"+response,true);
        long fetchSingleField = (Long) RawJsonParser.fetchSingleField(response, query);
        return fetchSingleField;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("======INITIAL WAIT BEFORE EXECUTION=======FOR : 2 MINS...");
        Thread.sleep(120000);
        int numberOfConcurrentUsers = 0;
        int durationInMins = 0;
        int intervalBetweenActions = 0;
        long CollectScriptReportinitialDelayinMilli = 5000; // Fixed....
        long CollectScriptReportintervalInMillis = 0;
        long collectSocLatency = 0;
        long waitTimeToSinkLogsAfterExecution = 0;

        List<String> reportReceiveMailList = new ArrayList<>();
        String cleanUp = null;
        Map<String, String> bodyMessage = new Hashtable<>();
        List<String> userIdList = new ArrayList();
        String[] accessToken1_5000 = {"AccessToken (1-1000)", "AccessToken (1000-2000)", "AccessToken (2000-3000)", "AccessToken (3000-4000)", "AccessToken (4000-5000)"};
        String[] accessToken5000_10000 = {"AccessToken (5000-6000)", "AccessToken (6000-7000)", "AccessToken (7000-8000)", "AccessToken (8000-9000)", "AccessToken (9000-10000)"};
        GExcelDataProvider excelDataProvider_tokenList = new GExcelDataProvider("1jkRZiD6WZYocpi0bF1QzO6soFBwesVB5GDUDLVXyo_o");
        for (String tokenRange : accessToken1_5000) {
            List<Map<String, Object>> dataAsMapList_tokenList_5000 = excelDataProvider_tokenList.getDataAsMapList("TokenList (1-5000)", tokenRange);
            for (Map<String, Object> dataAsMapList_tokenList : dataAsMapList_tokenList_5000) {
                Object accessToken = dataAsMapList_tokenList.get(tokenRange);
                if (accessToken != null) {
                    userIdList.add(accessToken.toString());
                }
            }
        }
        for (String tokenRange : accessToken5000_10000) {
            List<Map<String, Object>> dataAsMapList_tokenList_10000 = excelDataProvider_tokenList.getDataAsMapList("TokenList (5000-10000)", tokenRange);
            for (Map<String, Object> dataAsMapList_tokenList : dataAsMapList_tokenList_10000) {
                Object accessToken = dataAsMapList_tokenList.get(tokenRange);
                if (accessToken != null) {
                    userIdList.add(accessToken.toString());
                }
            }
        }

        GExcelDataProvider excelDataProvider_input = new GExcelDataProvider("10WU0CVSSKOW93i_8tRQMNwmlVCFZ2_l2psl9qKZbCnA");
        List<Map<String, Object>> YammerStressInput = excelDataProvider_input.getDataAsMapList("YammerStressInput", "Param Name", "Param Value");

        for (Map<String, Object> YammerInput : YammerStressInput) {
            String stressParam = YammerInput.get("Param Name").toString();
            if (stressParam.contains("Number of Concurrent Users")) {
                numberOfConcurrentUsers = Integer.parseInt(YammerInput.get("Param Value").toString());
            } else if (stressParam.contains("Interval between activities in seconds")) {
                intervalBetweenActions = Integer.parseInt(YammerInput.get("Param Value").toString()) * 1000;
            } else if (stressParam.contains("Email Report")) {
                String[] split = YammerInput.get("Param Value").toString().split(",");
                reportReceiveMailList = new ArrayList<String>(Arrays.asList(split));
            } else if (stressParam.contains("Duration in minutes")) {
                durationInMins = Integer.parseInt(YammerInput.get("Param Value").toString());
            } else if (stressParam.contains("Stress Data Clean Up")) {
                cleanUp = YammerInput.get("Param Value").toString();
            } else if (stressParam.contains("Report Interval in mins")) {
                CollectScriptReportintervalInMillis = Integer.parseInt(YammerInput.get("Param Value").toString()) * 60 * 1000;
            } else if (stressParam.contains("Cloud SoC Latency in mins")) {
                collectSocLatency = Integer.parseInt(YammerInput.get("Param Value").toString()) * 60 * 1000;
            } else if (stressParam.contains("Wait Time To Sink Logs after Execution in mins")) {
                waitTimeToSinkLogsAfterExecution = Integer.parseInt(YammerInput.get("Param Value").toString()) * 60 * 1000;
            } else {
                System.out.println("!!! Stress Param Not Found !!!!");
            }
        }

        List<String> userIdListWithoutnull = new ArrayList();

        for (String accessToken : userIdList) {
            if (!accessToken.equals("null")) {
                userIdListWithoutnull.add(accessToken);
            }
        }
        for (String accessToken : userIdListWithoutnull) {
            System.out.println("Access Token :" + accessToken);
        }

        Reporter.log("Number of Users found With Access Token <Including Null> :" + userIdList.size(), true);
        Reporter.log("Number of Users found With Access Token <Without Null>   :" + userIdListWithoutnull.size(), true);

        String yammerStressInput
                = "\n------------------------------------------------------"
                + "\nYammer Stress Params :------ "
                + "\n------------------------------------------------------"
                + "\nDuration :" + durationInMins + " mins"
                + "\nNumber of Concurrent Users :" + numberOfConcurrentUsers
                + "\nInterval Between Activities :" + intervalBetweenActions
                + "\nCleanUp :" + cleanUp
                + "\nReport Receive Mail List :" + reportReceiveMailList.toString();

        bodyMessage.put("yammerStressInput", yammerStressInput);
        String startTest = new Date().toString();
        long startTime = System.currentTimeMillis();
        long endTime = startTime + durationInMins * 60 * 1000;
        System.out.println("Duration Time in Millis :" + (endTime - startTime));
        ExecutorService executor = Executors.newFixedThreadPool(numberOfConcurrentUsers);

        YammerStressTest.YammerActivities = excelDataProvider_input.getDataAsMapList("Yammer Sequence of Activities",
                "Yammer Sequence of Activities for Stress Test", "Param Value 1", "Param Value 2");

        //================================Excel ROW Header Creation=====================
        YammerStressTest.sheet_IntervalReport = workbook_IntervalReport.createSheet("YammerStressTest_ScriptReport");
        YammerStressTest.sheet_SoCReport = workbook_IntervalReport.createSheet("YammerStressTest_SoCReport");

        //Header Row Creation...
        HSSFRow rowhead = sheet_IntervalReport.createRow((short) 0);
        int i = 0;
        rowhead.createCell(i++).setCellValue("Date and Time");
        for (Map<String, Object> YammerActivity : YammerStressTest.YammerActivities) {
            String activityName = YammerActivity.get("Yammer Sequence of Activities for Stress Test").toString();
            rowhead.createCell(i++).setCellValue(activityName);
        }

        //Header Row Creation...
        HSSFRow rowheadsheet_SoCReport = sheet_SoCReport.createRow((short) 0);
        int k = 0;
        rowheadsheet_SoCReport.createCell(k++).setCellValue("Date and Time");
        rowheadsheet_SoCReport.createCell(k++).setCellValue("Activities Count");
        //=============================================================================

        //----------------------------- Timer Task ------------------------------------------
        TimerTask taskCollectScriptReport = new CollectScriptReport();
        Timer timerCollectScriptReport = new Timer();
        timerCollectScriptReport.schedule(taskCollectScriptReport, CollectScriptReportinitialDelayinMilli, CollectScriptReportintervalInMillis);

        TimerTask taskCollectSoCReport = new CollectSocReport();
        Timer timerCollectSoCReport = new Timer();
        timerCollectSoCReport.schedule(taskCollectSoCReport, collectSocLatency, CollectScriptReportintervalInMillis);

        while (endTime > System.currentTimeMillis()) {
            for (String token : userIdListWithoutnull) {
                PerformYammerActivities performYammerActivities = new PerformYammerActivities(YammerStressTest.YammerActivities, token, endTime, intervalBetweenActions);
                System.out.println("## Time Remaining in Millis :" + (endTime - System.currentTimeMillis()));
                if (!((endTime - System.currentTimeMillis()) > 0)) {
                    System.out.println("####### Time Over ######## KILLING ALL THE THREADS.....");
                    executor.shutdownNow();
                    String stressTestDuration
                            = "----------------------------------------------------------------"
                            + "\n Stress Duration :-----"
                            + "----------------------------------------------------------------"
                            + "\nStart Time               :" + startTest
                            + "\nEnd Time                 :" + new Date().toString()
                            + "\nTotal Time Taken in Mins :" + ((System.currentTimeMillis() - startTime) / 1000) / 60
                            + "\nTotal Time Taken in Secs :" + (System.currentTimeMillis() - startTime) / 1000
                            + "\nTotal Time Taken in ms   :" + (System.currentTimeMillis() - startTime);
                    bodyMessage.put("stressTestDuration", stressTestDuration);
                    while (!executor.isTerminated()) {
                    }
                    System.out.println("Finished all threads");
                    try {
                        System.out.println("Wait time after execution : " + waitTimeToSinkLogsAfterExecution+ " ms");
                        Thread.sleep(waitTimeToSinkLogsAfterExecution);
                    } catch (InterruptedException ex) {
                        System.out.println("Issue Found in Sleep : " + ex.getLocalizedMessage());
                    }
                } else {
                    executor.execute(performYammerActivities);
                }
            }
        }
        timerCollectScriptReport.cancel();
        createandSendExcelReport(YammerActivities, YammerStressTest.successResult, YammerStressTest.failureResult, YammerStressTest.usersInfo, reportReceiveMailList, bodyMessage);
    }

    public static void createandSendExcelReport(List<Map<String, Object>> YammerActivities,
            Map<String, Object> successResult, Map<String, Object> failureResult,
            Map<String, Object> usersInfo, List<String> reportReceiveMailList, Map<String, String> bodyMessage) throws FileNotFoundException, IOException {

        Map<String, Integer> activitiesCountSuccess = new Hashtable<>();
        Map<String, Integer> activitiesCountFailure = new Hashtable<>();
        String fileLocation = "/tmp/YammerReport.xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("YammerStressTest");
        //Header Row Creation...
        HSSFRow rowhead = sheet.createRow((short) 0);
        int i = 0;
        rowhead.createCell(i++).setCellValue("User ID");
        rowhead.createCell(i++).setCellValue("User Name");
        for (Map<String, Object> YammerActivity : YammerActivities) {
            String activityName = YammerActivity.get("Yammer Sequence of Activities for Stress Test").toString();
            rowhead.createCell(i++).setCellValue(activityName + "-Success");
            activitiesCountSuccess.put(activityName, 0);
            activitiesCountFailure.put(activityName, 0);
            // rowhead.createCell(i++).setCellValue(activityName + "-Failure");
        }
        //Create Row And Set Values...    
        List<String> userIdlist = new ArrayList<>(usersInfo.keySet());
        int rowCount = 0;
        for (String userId : userIdlist) {
            int j = 0;
            HSSFRow row = sheet.createRow((short) 1 + rowCount++);
            row.createCell(j++).setCellValue(userId);
            row.createCell(j++).setCellValue(usersInfo.get(userId).toString());
            for (Map<String, Object> YammerActivity : YammerActivities) {
                String activityName = YammerActivity.get("Yammer Sequence of Activities for Stress Test").toString();
                row.createCell(j++).setCellValue(successResult.get(userId + "_" + activityName).toString());
                activitiesCountSuccess.put(activityName, activitiesCountSuccess.get(activityName) + Integer.parseInt(successResult.get(userId + "_" + activityName).toString()));
                //  row.createCell(j++).setCellValue(failureResult.get(userId + "_" + activityName).toString());
                activitiesCountFailure.put(activityName, activitiesCountFailure.get(activityName) + Integer.parseInt(failureResult.get(userId + "_" + activityName).toString()));
            }
        }

        FileOutputStream fileOut = new FileOutputStream(fileLocation);
        workbook.write(fileOut);
        fileOut.close();
        System.out.println("Your excel report has been generated!");

        FileOutputStream fileOutIntervalReport = new FileOutputStream(YammerStressTest.fileLocationIntervalReport);
        workbook_IntervalReport.write(fileOutIntervalReport);
        fileOutIntervalReport.close();
        System.out.println("Your excel report has been generated IntervalReport !");

        String failureCountReport
                = "\n\n-----------------------------------------------------"
                + "\nFailures Calls Across All the Users <RATE LIMIT ERROR Response CODE: 429>: ----------\n"
                + "----------------------------------------------------";
        int x = 1;
        for (Map.Entry<String, Integer> entrySet : activitiesCountFailure.entrySet()) {
            String key = entrySet.getKey();
            Integer value = entrySet.getValue();
            failureCountReport = failureCountReport + "\n" + x++ + ")" + key + ":" + value;
        }

        int y = 1;
        String successCountReport
                = "\n\n-----------------------------------------------------"
                + "\nSuccess Calls Across All the Users : ----------\n"
                + "----------------------------------------------------";
        for (Map.Entry<String, Integer> entrySet : activitiesCountSuccess.entrySet()) {
            String key = entrySet.getKey();
            Integer value = entrySet.getValue();
            successCountReport = successCountReport + "\n" + y++ + ")" + key + ":" + value;
        }

        String signature = "\n\nThanks \n Rahul \n +91-8123436054 \n Elastica Bangalore (India)";

        System.out.println("Send Mail Report....-----------------------------------------------------------------");

        GoogleMailServices googleMailServices = new GoogleMailServices("501464058594-u5l9b2kbs0pnaq0d22tb131sgutdk718.apps.googleusercontent.com", "h6qXovBfNGON3YXNkmU0slkh", "1/p0qgpfGaJ8pZNM1zollfnOJ9DcBI2A9M8xoCQ6S94FhIgOrJDtdun6zK6XiATCKT");
        googleMailServices.printLabelsInUserAccount();

        String subject = "Yammer Stress Test Result - " + new Date();
        List<String> fileLocations = new ArrayList<>();
        fileLocations.add(fileLocation);
        fileLocations.add(YammerStressTest.fileLocationIntervalReport);
        String bodyText = "Hello All , \n \n "
                + "Find the Consildated Report :-----------------\n";

        bodyText = bodyText + bodyMessage.get("stressTestDuration") + bodyMessage.get("yammerStressInput")
                + successCountReport + failureCountReport + "\n\nFind The attached detailed Excel Report..." + signature;

        String fileName = "Yammer Stress Test Result - " + new Date() + ".xls";

        googleMailServices.sendMessageWithMultipleAttachment(reportReceiveMailList, null, null, subject, bodyText,
                fileLocations);

    }

    /**
     *
     * @param restClient
     * @param appName
     * @param requestHeader
     * @param suiteData
     * @return
     * @throws Exception
     */
    public static Map<String, String> getAllActivityCount(Client restClient1, String tsfrom, String tsto, String appName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception {
        Map<String, String> activityCounts = new HashMap<String, String>();
        ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
        ElasticSearchLogs esLogs = new ElasticSearchLogs();
        String email = suiteData.getUsername();
        String payload = protectQueryBuilder.getESQueryForActivityCount(tsfrom, tsto, appName, suiteData.getApiserverHostName(), suiteData.getCSRFToken(), suiteData.getSessionID(), email);
        System.out.println("===PayLoad :" + payload);
        StringEntity entity = new StringEntity(payload);
        String responseBody = ClientUtil.getResponseBody(esLogs.getDisplayLogs(restClient1, requestHeader, suiteData.getApiserverHostName(), entity));
        activityCounts = YammerStressTest.getCount(responseBody);
        return activityCounts;
    }

    /**
     *
     * @param jsonResponse
     * @return
     */
    public static Map<String, String> getCount(String jsonResponse) {
        List<Map<String, Object>> fetchAllKeys = RawJsonParser.fetchAllKeys(jsonResponse, "$.facets.Activity_type.terms[*]");
        Map<String, String> result = new HashMap<String, String>();

        for (Map<String, Object> fetchAllKey : fetchAllKeys) {
            result.put(fetchAllKey.get("term").toString(), fetchAllKey.get("count").toString());
        }
        return result;
    }

}

class PerformYammerActivities extends Thread {

    List<Map<String, Object>> YammerActivities;
    Yammer yammer;
    long endTime;
    String sendMsgToAll = "Broadcast/Send message to All";
    String sendMsgToAGroup = "Send Message to a Group (QA)";
    String sendMsgToPrivate = "Send Message to a Private";
    String msgLikePostToAll = "Message Like  (Post message to All)";
    String msgUnlikePostToAll = "Message Unlike (Post message to All)";
    String msgLikePostToGroup = "Message Like  (Post Message to a Group (QA))";
    String msgUnlikePostToGroup = "Message Unlike (Post Message to a Group (QA))";
    String deleteMsgToAll = "Delete Message (Post message to All)";
    String deleteMsgToAGroup = "Delete Message (Post Message to a Group (QA))";
    String deletePrivateMsg = "Delete Private Message";
    String fileUpload = "File Upload";
    String deleteUploadedFile = "Remove File";
    String userID = null;
    int waitTime = 0;
    String rateLimitMsg = "Rate limited due to excessive requests";
    int maxRetryCount = 3;
    boolean IsUserfoundInSOC = true;

    public PerformYammerActivities(List<Map<String, Object>> YammerActivities, String accessToken, long endTime, int waitTime) {

        try {
            this.waitTime = waitTime;
            this.endTime = endTime;
            this.YammerActivities = YammerActivities;
            if (!((this.endTime - System.currentTimeMillis()) > 0)) {
                System.out.println(" ### TIME OVER ### KILLING THREAD for JOBS for USER ID");
                Thread.currentThread().interrupt();
                return;
            }
            yammer = new Yammer(accessToken);
            String currentUserInfo = yammer.getCurrentUserInfo();
            String userMailId = RawJsonParser.getSingleKey(currentUserInfo, "$.email");
            int retryCount = 1;
            while (userMailId == null) {
                retryCount++;
                if (retryCount > maxRetryCount) {
                    System.out.println("Retry : User info Null for Access Token :" + accessToken + " ### User info Response :" + currentUserInfo);
                    currentUserInfo = yammer.getCurrentUserInfo();
                    userMailId = RawJsonParser.getSingleKey(currentUserInfo, "$.email");
                    Thread.sleep(this.waitTime);
                } else {
                    Reporter.log("Invalid Access Token Found :" + accessToken, true);
                    Thread.currentThread().interrupt();
                }
            }
            String userName = RawJsonParser.getSingleKey(currentUserInfo, "$.full_name");
            System.out.println("User Name : " + userName);
            userMailId = RawJsonParser.getSingleKey(currentUserInfo, "$.email");
            System.out.println("User Email ID : " + userMailId);
            this.userID = userMailId;
            if (YammerStressTest.usersPresentInCloudSoc.contains(this.userID)) {
                this.IsUserfoundInSOC = true;
            } else if (YammerStressTest.usersNotInCloudSoc.contains(this.userID)) {
                this.IsUserfoundInSOC = false;
            } else {
                Long importedUserInfo = YammerStressTest.getImportedUserInfo(this.userID);
                if (importedUserInfo == 0) {
                    YammerStressTest.usersNotInCloudSoc.add(this.userID);
                    this.IsUserfoundInSOC = false;
                    Reporter.log(" ## User Not found in Cloud SoC :" + this.userID,true);
                } else {
                    YammerStressTest.usersPresentInCloudSoc.add(this.userID);
                    this.IsUserfoundInSOC = true;
                    Reporter.log(" ## User Found in Cloud SoC :" + this.userID,true);
                }
            }
            if (this.IsUserfoundInSOC) {
                YammerStressTest.usersInfo.put(userID, userName);
                YammerStressTest.userIdList.add(RawJsonParser.getSingleKey(currentUserInfo, "$.id"));
                for (Map<String, Object> YammerActivity : YammerActivities) {
                    String activityName = YammerActivity.get("Yammer Sequence of Activities for Stress Test").toString();
                    if (!YammerStressTest.successResult.containsKey(userID + "_" + activityName)) {
                        YammerStressTest.successResult.put(userID + "_" + activityName, 0);
                    }
                    if (!YammerStressTest.failureResult.containsKey(userID + "_" + activityName)) {
                        YammerStressTest.failureResult.put(userID + "_" + activityName, 0);
                    }
                }
            } else {
                Reporter.log("User Not found in Cloud SoC <User Operation Escaped> :" + this.userID,true);
            }

        } catch (Exception ex) {
            Logger.getLogger(PerformYammerActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        if (this.IsUserfoundInSOC) {
            String msgIdPostToAll = null;
            String msgIdPostToGroup = null;
            String msgIdPostToPrivate = null;
            String attachmentId = null;
            String YammerStressTestPrefix = "Yammer Stress Test :" + new Date() + " : ";

            try {
                for (Map<String, Object> YammerActivity : YammerActivities) {
                    String activityName = YammerActivity.get("Yammer Sequence of Activities for Stress Test").toString();
                    if (activityName.contains(sendMsgToAll)) {
                        String postMessage = yammer.postMessage(YammerStressTestPrefix + YammerActivity.get("Param Value 2").toString(), null);
                        while (postMessage.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + sendMsgToAll);
                            postMessage = yammer.postMessage(YammerStressTestPrefix + YammerActivity.get("Param Value 2").toString(), null);
                            System.out.println("Retry :" + sendMsgToAll + " Response :" + postMessage);
                            YammerStressTest.failureResult.put(userID + "_" + sendMsgToAll, (Integer) YammerStressTest.failureResult.get(userID + "_" + sendMsgToAll) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + sendMsgToAll, (Integer) YammerStressTest.successResult.get(userID + "_" + sendMsgToAll) + 1);
                        msgIdPostToAll = RawJsonParser.getSingleKey(postMessage, "$.messages[0].id");

                    } else if (activityName.contains(sendMsgToAGroup)) {
                        String postMessage = yammer.postMessage(YammerStressTestPrefix + YammerActivity.get("Param Value 2").toString(), YammerActivity.get("Param Value 1").toString());
                        while (postMessage.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + sendMsgToAGroup);
                            postMessage = yammer.postMessage(YammerStressTestPrefix + YammerActivity.get("Param Value 2").toString(), YammerActivity.get("Param Value 1").toString());
                            System.out.println("Retry :" + sendMsgToAGroup + " Response :" + postMessage);
                            YammerStressTest.failureResult.put(userID + "_" + sendMsgToAGroup, (Integer) YammerStressTest.failureResult.get(userID + "_" + sendMsgToAGroup) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + sendMsgToAGroup, (Integer) YammerStressTest.successResult.get(userID + "_" + sendMsgToAGroup) + 1);
                        msgIdPostToGroup = RawJsonParser.getSingleKey(postMessage, "$.messages[0].id");
                    } else if (activityName.contains(sendMsgToPrivate)) {
                        String userId = YammerStressTest.usersPresentInCloudSoc.get(new Random().nextInt(YammerStressTest.usersPresentInCloudSoc.size()));
                        String postMessage = yammer.postMessageToUser(YammerStressTestPrefix + YammerActivity.get("Param Value 2").toString(), userId);
                        while (postMessage.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + sendMsgToPrivate);
                            postMessage = yammer.postMessage(YammerStressTestPrefix + YammerActivity.get("Param Value 2").toString(), userId);
                            System.out.println("Retry :" + sendMsgToPrivate + " Response :" + postMessage);
                            YammerStressTest.failureResult.put(userID + "_" + sendMsgToPrivate, (Integer) YammerStressTest.failureResult.get(userID + "_" + sendMsgToPrivate) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + sendMsgToPrivate, (Integer) YammerStressTest.successResult.get(userID + "_" + sendMsgToPrivate) + 1);
                        msgIdPostToPrivate = RawJsonParser.getSingleKey(postMessage, "$.messages[0].id");
                    } else if (activityName.contains(msgLikePostToAll)) {
                        String performMessageLikeByCurrentUser = yammer.performMessageLikeByCurrentUser(msgIdPostToAll);
                        while (performMessageLikeByCurrentUser.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + msgLikePostToAll);
                            performMessageLikeByCurrentUser = yammer.performMessageLikeByCurrentUser(msgIdPostToAll);
                            System.out.println("Retry :" + msgLikePostToAll + " Response :" + performMessageLikeByCurrentUser);
                            YammerStressTest.failureResult.put(userID + "_" + msgLikePostToAll, (Integer) YammerStressTest.failureResult.get(userID + "_" + msgLikePostToAll) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + msgLikePostToAll, (Integer) YammerStressTest.successResult.get(userID + "_" + msgLikePostToAll) + 1);
                    } else if (activityName.contains(msgUnlikePostToAll)) {
                        String removeMessageLikeByCurrentUser = yammer.removeMessageLikeByCurrentUser(msgIdPostToAll);
                        while (removeMessageLikeByCurrentUser.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + msgUnlikePostToAll);
                            removeMessageLikeByCurrentUser = yammer.removeMessageLikeByCurrentUser(msgIdPostToAll);
                            System.out.println("Retry :" + msgUnlikePostToAll + " Response :" + removeMessageLikeByCurrentUser);
                            YammerStressTest.failureResult.put(userID + "_" + msgUnlikePostToAll, (Integer) YammerStressTest.failureResult.get(userID + "_" + msgUnlikePostToAll) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + msgUnlikePostToAll, (Integer) YammerStressTest.successResult.get(userID + "_" + msgUnlikePostToAll) + 1);
                    } else if (activityName.contains(msgLikePostToGroup)) {
                        String performMessageLikeByCurrentUser = yammer.performMessageLikeByCurrentUser(msgIdPostToGroup);
                        while (performMessageLikeByCurrentUser.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + msgLikePostToGroup);
                            performMessageLikeByCurrentUser = yammer.performMessageLikeByCurrentUser(msgIdPostToGroup);
                            System.out.println("Retry :" + msgLikePostToGroup + " Response :" + performMessageLikeByCurrentUser);
                            YammerStressTest.failureResult.put(userID + "_" + msgLikePostToGroup, (Integer) YammerStressTest.failureResult.get(userID + "_" + msgLikePostToGroup) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + msgLikePostToGroup, (Integer) YammerStressTest.successResult.get(userID + "_" + msgLikePostToGroup) + 1);
                    } else if (activityName.contains(msgUnlikePostToGroup)) {
                        String removeMessageLikeByCurrentUser = yammer.removeMessageLikeByCurrentUser(msgIdPostToGroup);
                        while (removeMessageLikeByCurrentUser.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + msgLikePostToGroup);
                            removeMessageLikeByCurrentUser = yammer.removeMessageLikeByCurrentUser(msgIdPostToAll);
                            System.out.println("Retry :" + msgLikePostToGroup + " Response :" + removeMessageLikeByCurrentUser);
                            YammerStressTest.failureResult.put(userID + "_" + msgUnlikePostToGroup, (Integer) YammerStressTest.failureResult.get(userID + "_" + msgUnlikePostToGroup) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + msgUnlikePostToGroup, (Integer) YammerStressTest.successResult.get(userID + "_" + msgUnlikePostToGroup) + 1);
                    } else if (activityName.contains(deleteMsgToAll)) {
                        String deleteMessage = yammer.deleteMessage(msgIdPostToAll);
                        while (deleteMessage.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + deleteMsgToAll);
                            deleteMessage = yammer.deleteMessage(msgIdPostToAll);
                            System.out.println("Retry :" + deleteMsgToAll + " Response :" + deleteMessage);
                            YammerStressTest.failureResult.put(userID + "_" + deleteMsgToAll, (Integer) YammerStressTest.failureResult.get(userID + "_" + deleteMsgToAll) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + deleteMsgToAll, (Integer) YammerStressTest.successResult.get(userID + "_" + deleteMsgToAll) + 1);
                    } else if (activityName.contains(deleteMsgToAGroup)) {
                        String deleteMessage = yammer.deleteMessage(msgIdPostToGroup);
                        while (deleteMessage.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + deleteMsgToAGroup);
                            deleteMessage = yammer.deleteMessage(msgIdPostToGroup);
                            System.out.println("Retry :" + deleteMsgToAGroup + " Response :" + deleteMessage);
                            YammerStressTest.failureResult.put(userID + "_" + deleteMsgToAGroup, (Integer) YammerStressTest.failureResult.get(userID + "_" + deleteMsgToAGroup) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + deleteMsgToAGroup, (Integer) YammerStressTest.successResult.get(userID + "_" + deleteMsgToAGroup) + 1);
                    } else if (activityName.contains(deletePrivateMsg)) {
                        String deleteMessage = yammer.deleteMessage(msgIdPostToPrivate);
                        while (deleteMessage.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + deletePrivateMsg);
                            deleteMessage = yammer.deleteMessage(msgIdPostToPrivate);
                            System.out.println("Retry :" + deletePrivateMsg + " Response :" + deleteMessage);
                            YammerStressTest.failureResult.put(userID + "_" + deletePrivateMsg, (Integer) YammerStressTest.failureResult.get(userID + "_" + deletePrivateMsg) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + deletePrivateMsg, (Integer) YammerStressTest.successResult.get(userID + "_" + deletePrivateMsg) + 1);
                    } else if (activityName.contains(fileUpload)) {
                        String pendAttachment = yammer.pendAttachment(YammerActivity.get("Param Value 2").toString(),
                                YammerActivity.get("Param Value 1").toString());
                        System.out.println("Pend Attchement Response :" + pendAttachment);
                        while (pendAttachment.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + fileUpload);
                            pendAttachment = yammer.pendAttachment(YammerActivity.get("Param Value 2").toString(),
                                    YammerActivity.get("Param Value 1").toString());
                            System.out.println("Retry :" + fileUpload + " Response :" + pendAttachment);
                            YammerStressTest.failureResult.put(userID + "_" + fileUpload, (Integer) YammerStressTest.failureResult.get(userID + "_" + fileUpload) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + fileUpload, (Integer) YammerStressTest.successResult.get(userID + "_" + fileUpload) + 1);
                        attachmentId = RawJsonParser.getSingleKey(pendAttachment, "$.id");
                    } else if (activityName.contains(deleteUploadedFile)) {
                        String deleteFileResponse = yammer.deleteAttachment(attachmentId);
                        System.out.println("Delete Attchement Response :" + deleteFileResponse);
                        while (deleteFileResponse.contains(rateLimitMsg)) {
                            try {
                                Thread.sleep(this.waitTime);
                            } catch (Exception e) {
                            }
                            System.out.println("Retrying again for :" + deleteUploadedFile);
                            deleteFileResponse = yammer.deleteAttachment(attachmentId);
                            System.out.println("Retry :" + deleteUploadedFile + " Response :" + deleteFileResponse);
                            YammerStressTest.failureResult.put(userID + "_" + deleteUploadedFile, (Integer) YammerStressTest.failureResult.get(userID + "_" + deleteUploadedFile) + 1);
                        }
                        YammerStressTest.successResult.put(userID + "_" + deleteUploadedFile, (Integer) YammerStressTest.successResult.get(userID + "_" + deleteUploadedFile) + 1);
                    } else {
                        System.out.println("!!!!! ========= Activity Not Found ======== !!!!!");
                    }

                }

            } catch (Exception ex) {
                Reporter.log("Exception In Thread Execution :" + ex.getLocalizedMessage(), true);
            }
        } else {
            System.out.println("#### User NOT Found in SOC ####");
        }
    }
}

class CollectSocReport extends TimerTask {

    @Override
    public void run() {
        Map<String, String> allActivityCount = null;
        String tsto = new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC).toString();
        try {
            allActivityCount = YammerStressTest.getAllActivityCount(YammerStressTest.client, YammerStressTest.from_jodaTime, tsto, "Yammer", YammerStressTest.headers, YammerStressTest.data);
        } catch (Exception ex) {
            Logger.getLogger(CollectSocReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("======= Collecting SOC Report From Yammer Investigate ============:" + allActivityCount.toString());
        // Excel Sheet Entry....
        HSSFRow row = YammerStressTest.sheet_SoCReport.createRow((short) 1 + YammerStressTest.rowCount_IntervalReportSoC++);
        int j = 0;
        row.createCell(j++).setCellValue(new Date().toString());
        row.createCell(j++).setCellValue(allActivityCount.toString());
    }
}

class CollectScriptReport extends TimerTask {

    @Override
    public void run() {
        String reportGenerationTime = new Date().toString();
        System.out.println("------Generating Report at :" + reportGenerationTime + "---------");
        Map<String, Integer> regularActivityCount = new LinkedHashMap<>();
        for (Map<String, Object> YammerActivity : YammerStressTest.YammerActivities) {
            String activityName = YammerActivity.get("Yammer Sequence of Activities for Stress Test").toString();
            int count = 0;
            for (Map.Entry<String, Object> entrySet : YammerStressTest.successResult.entrySet()) {
                String key = entrySet.getKey();
                Integer value = (Integer) entrySet.getValue();
                if (key.contains(activityName)) {
                    count = count + value;
                }
            }
            regularActivityCount.put(activityName, count);
        }
        // Excel Sheet Entry....
        HSSFRow row = YammerStressTest.sheet_IntervalReport.createRow((short) 1 + YammerStressTest.rowCount_IntervalReport++);
        int j = 0;
        row.createCell(j++).setCellValue(reportGenerationTime);
        System.out.println("ScriptReport :" + regularActivityCount.toString());
        for (Map.Entry<String, Integer> entrySet : regularActivityCount.entrySet()) {
            row.createCell(j++).setCellValue(entrySet.getValue());
        }
    }

}
