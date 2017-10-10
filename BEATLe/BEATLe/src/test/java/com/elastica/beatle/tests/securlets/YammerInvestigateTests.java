package com.elastica.beatle.tests.securlets;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.securlets.ESQueryBuilder;
import java.io.File;
import java.util.UUID;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import com.universal.common.Yammer;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class YammerInvestigateTests extends CommonTest {

    String parentNode = "$.hits.hits[*].source";
    int delayBetweenSaaSActivity;
    int waitTimeToReceiveLog;
    String yammerAppToken;
    int retryCount = 0; // Retry Count Initialization...
    int maxRetryCount = 2;
    SecurletsUtils securletsUtils = new SecurletsUtils();
    String json_Response = null;
    String SaasApp_Yammer = "Yammer";
    String from_jodaTime;
    Yammer yammer;
    String messageBody;

    @BeforeClass(alwaysRun = true)
    public void oneTimeSetUp() throws Exception {
        this.delayBetweenSaaSActivity = Integer.parseInt(getRegressionSpecificSuitParameters("delayBetweenSaaSActivity"));
        this.waitTimeToReceiveLog = Integer.parseInt(getRegressionSpecificSuitParameters("waitPeriodForElasticSearchEngine"));
        this.yammerAppToken = getRegressionSpecificSuitParameters("yammerToken");
        this.yammer = new Yammer(this.yammerAppToken);
    }
     
    public void updateDisplayLogResponse() throws Exception {
        ESQueryBuilder eSQueryBuilder = new ESQueryBuilder();
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", SaasApp_Yammer);
        String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
        String payload = "";
        payload = eSQueryBuilder.getESQuery(from_jodaTime, new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC).toString(),
                SaasApp_Yammer, termmap, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_Yammer);
        Reporter.log("Request body:" + payload, true);
        String path = suiteData.getAPIMap().get("getInvestigateLogs");
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
        Reporter.log("URI ::" + dataUri.toString(), true);
        HttpResponse response = restClient.doPost(dataUri, getHeaders(), null, new StringEntity(payload));
        this.json_Response = ClientUtil.getResponseBody(response);
        Reporter.log("Display Log Response : "+this.json_Response,true);
        System.out.println("Display Log Response : "+this.json_Response);
    }

    @Test
    public void performYammerMessageActivity() throws Exception {
        from_jodaTime = new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC).toString();
        securletsUtils.wait(delayBetweenSaaSActivity, " ## Waiting Before Performing SaaS Activity...");
        Reporter.log("Performing YAMMER Message Activities...", true);
        this.messageBody = "Warm Welcome to BLR-QA Team...Today is :" + new Date().toString();
        Reporter.log(" ## Message Body :" + messageBody, true);
        String postMessage = yammer.postMessage(messageBody, null);
        Reporter.log("## Post Message Response :" + postMessage, true);
        securletsUtils.wait(waitTimeToReceiveLog, " ## Waiting After Performing SaaS Activity...");
        updateDisplayLogResponse();  
    }

    @Test(dependsOnMethods = "performYammerMessageActivity")
    public void validatePostMesageToAll() throws Exception {
        Map expectedResult = new HashMap();
        expectedResult.put("Message Content", this.messageBody);
        expectedResult.put("message", "User admin@securleto365beatle.com created a post");
        expectedResult.put("Object_type", "Post");
        expectedResult.put("Activity_type", "Create");
        expectedResult.put("user_name", "Admin Securlet");
        expectedResult.put("user", "admin@securleto365beatle.com");
        RawJsonParser.LogValidator(this.json_Response,expectedResult,this.parentNode,"Message Content");
    }
}
