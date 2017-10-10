package com.universal.common;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.universal.constants.CommonConstants;
import com.universal.dtos.yammer.Example;
import com.universal.dtos.yammer.Message;
import com.universal.util.Utility;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

/**
 *
 * @author rahulkumar
 */
public class Yammer extends CommonTest {

    private String accessToken;
    Map<String, String> headers = new HashMap();
    List<NameValuePair> headerList = new ArrayList<>();
    Gson gson;

    public Yammer(String accessToken) throws Exception {
        System.out.println("Yammer Header Initialization....In Progress..");
        System.out.println("Get Doc On Access Token Generation : https://developer.yammer.com/docs/authentication-1");
        this.accessToken = accessToken;
        headerList.add(new BasicNameValuePair("Authorization", "Bearer " + accessToken));
        headers.put("Authorization", "Bearer " + accessToken);
        gson = new Gson();
        System.out.println("!!! Yammer Client Initialization Completed !!!");
    }

    public Yammer() throws Exception {
        System.out.println("!!! Yammer Client Initialization Completed !!!");
    }

    public String getAllRawMessages() {
        String fetchMessageUrl = "https://www.yammer.com/api/v1/messages.json";
        String sendGET = HttpClient.sendGET(fetchMessageUrl, headers);
        return sendGET;
    }

    public List<Message> getAllMessages() {
        Example yammerBody = gson.fromJson(getAllRawMessages(), Example.class);
        return yammerBody.getMessages();
    }

    public List<Message> getAllMessages(String older_than, String newer_than, String limit) {
        String fetchMessageUrl = "https://www.yammer.com/api/v1/messages.json";
        fetchMessageUrl = updateUrl(older_than, newer_than, limit, fetchMessageUrl);
        String sendGET = HttpClient.sendGET(fetchMessageUrl, headers);
        Example yammerBody = gson.fromJson(sendGET, Example.class);
        return yammerBody.getMessages();
    }

    public String feedUserMessage(String older_than, String newer_than, String limit) {
        String feedUserMsgUrl = "https://www.yammer.com/api/v1/messages/my_feed.json";
        feedUserMsgUrl = updateUrl(older_than, newer_than, limit, feedUserMsgUrl);
        String sendGET = HttpClient.sendGET(feedUserMsgUrl, headers);
        return sendGET;
    }

    public String algorithmicUserMessage(String older_than, String newer_than, String limit) {
        String algorithmicUserMsgUrl = "https://www.yammer.com/api/v1/messages/algo.json";
        algorithmicUserMsgUrl = updateUrl(older_than, newer_than, limit, algorithmicUserMsgUrl);
        String sendGET = HttpClient.sendGET(algorithmicUserMsgUrl, headers);
        return sendGET;
    }

    public String followingUserMessage(String older_than, String newer_than, String limit) {
        String followingUserMsgUrl = "https://www.yammer.com/api/v1/messages/following.json";
        followingUserMsgUrl = updateUrl(older_than, newer_than, limit, followingUserMsgUrl);
        String sendGET = HttpClient.sendGET(followingUserMsgUrl, headers);
        return sendGET;
    }

    public String getSentMessage(String older_than, String newer_than, String limit) {
        String sentMsgUrl = "https://www.yammer.com/api/v1/messages/sent.json";
        sentMsgUrl = updateUrl(older_than, newer_than, limit, sentMsgUrl);
        String sendGET = HttpClient.sendGET(sentMsgUrl, headers);
        return sendGET;
    }

    public String getPrivateMessage(String older_than, String newer_than, String limit) {
        String privateMsgUrl = "https://www.yammer.com/api/v1/messages/private.json";
        privateMsgUrl = updateUrl(older_than, newer_than, limit, privateMsgUrl);
        String sendGET = HttpClient.sendGET(privateMsgUrl, headers);
        return sendGET;
    }

    public String getReceivedMessage(String older_than, String newer_than, String limit) {
        String receivedMsgUrl = "https://www.yammer.com/api/v1/messages/received.json";
        receivedMsgUrl = updateUrl(older_than, newer_than, limit, receivedMsgUrl);
        String sendGET = HttpClient.sendGET(receivedMsgUrl, headers);
        return sendGET;
    }

    public String postMessageToReply(String body, String replied_to_id) {
        String postMessageUrl = "https://www.yammer.com/api/v1/messages.json";
        Map<String, String> postMessageParams = new HashMap();
        postMessageParams.put("replied_to_id", replied_to_id);
        postMessageParams.put("body", body);
        String sendPOST = HttpClient.sendPOST(postMessageUrl, headers, postMessageParams);
        return sendPOST;
        }
    
    public String postMessageToUser(String body, String direct_to_id) {
            String postMessageUrl = "https://www.yammer.com/api/v1/messages.json";
        Map<String, String> postMessageParams = new HashMap();
        postMessageParams.put("direct_to_id", direct_to_id);
        postMessageParams.put("body", body);
        String sendPOST = HttpClient.sendPOST(postMessageUrl, headers, postMessageParams);
        return sendPOST;
        }
    
    
    public String postMessage(String body, String group_id) {
            String postMessageUrl = "https://www.yammer.com/api/v1/messages.json";
            Map<String, String> postMessageParams = new HashMap();
            if (group_id != null) {
                postMessageParams.put("group_id", group_id);
            }
            postMessageParams.put("body", body);
        String sendPOST = HttpClient.sendPOST(postMessageUrl, headers, postMessageParams);
        return sendPOST;
        }
    
    public String deleteMessage(String messageId) throws URISyntaxException, Exception {
        String deleteMessageUrl = "https://www.yammer.com/api/v1/messages/" + messageId;
        URI uri = new URI(deleteMessageUrl);
        HttpResponse executeRestRequest = executeRestRequest(DELETE_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String getUserInfo(String userID) throws URISyntaxException, Exception {
        String getUserInfoUrl = "https://www.yammer.com/api/v1/users/" + userID + ".json";
        URI uri = new URI(getUserInfoUrl);
        HttpResponse executeRestRequest = executeRestRequest(GET_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String getCurrentUserInfo() throws URISyntaxException, Exception {
        String currentUserInfoUrl = "https://www.yammer.com/api/v1/users/current.json";
        URI uri = new URI(currentUserInfoUrl);
        HttpResponse executeRestRequest = executeRestRequest(GET_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    //Send the current user a copy of the message specified by the numeric string ID.
    public String sendEmailToAutenticatedUser(String messageId) throws Exception {
        String sendMailUrl = "https://www.yammer.com/api/v1/messages/email.json?message_id=" + messageId;
        HttpResponse executeRestRequest = executeRequest(POST_METHOD, new URI(sendMailUrl), headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String updateUser(String userEmail) throws Exception {
        String updateUserUrl = "https://www.yammer.com/api/v1/users/by_email.json?email=" + userEmail;
        HttpResponse executeRestRequest = executeRequest(GET_METHOD, new URI(updateUserUrl), headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String createUser(String username, String emailID) throws URISyntaxException, Exception {
        String createUserUrl = "https://www.yammer.com/api/v1/users.json";
        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair("email", emailID));
        queryParams.add(new BasicNameValuePair("full_name", username));
        HttpResponse executeRestRequest = executeRequest(POST_METHOD, new URI(createUserUrl), headerList, null, queryParams);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String pendAttachment(String localFileLocation, String fileName) throws URISyntaxException, Exception {
        String pendAttachmentUrl = "https://www.yammer.com/api/v1/pending_attachments";
        return HttpClient.multipartRequest(pendAttachmentUrl, headers, localFileLocation, fileName);
    }
    
    public String deleteAttachment(String attachmentID) throws URISyntaxException, Exception {
        String deleteAttachmentUrl = "https://www.yammer.com/api/v1/pending_attachments/" + attachmentID;
        URI uri = new URI(deleteAttachmentUrl);
        HttpResponse executeRestRequest = executeRestRequest(DELETE_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String getMessageThread(String threadId) throws URISyntaxException, Exception {
        String getThreadUrl = "https://www.yammer.com/api/v1/threads/" + threadId + ".json";
        URI uri = new URI(getThreadUrl);
        HttpResponse executeRestRequest = executeRestRequest(GET_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String performMessageLikeByCurrentUser(String message_id) throws URISyntaxException, Exception {
        String messageLikeUrl = "https://www.yammer.com/api/v1/messages/liked_by/current.json?message_id=" + message_id;
        URI uri = new URI(messageLikeUrl);
        HttpResponse executeRestRequest = executeRestRequest(POST_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String removeMessageLikeByCurrentUser(String message_id) throws URISyntaxException, Exception {
        String messageLikeUrl = "https://www.yammer.com/api/v1/messages/liked_by/current.json?message_id=" + message_id;
        URI uri = new URI(messageLikeUrl);
        HttpResponse executeRestRequest = executeRestRequest(DELETE_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String addTopic(String Topic_ID) throws URISyntaxException, Exception {
        String messageLikeUrl = "https://www.yammer.com/api/v1/topics/" + Topic_ID + ".json";
        URI uri = new URI(messageLikeUrl);
        HttpResponse executeRestRequest = executeRestRequest(GET_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String getMessagesAboutTopic(String Topic_ID) throws URISyntaxException, Exception {
        String messageLikeUrl = "https://www.yammer.com/api/v1/messages/about_topic/" + Topic_ID + ".json";
        URI uri = new URI(messageLikeUrl);
        HttpResponse executeRestRequest = executeRestRequest(GET_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String joinGroup(String group_id) throws URISyntaxException, Exception {
        String messageLikeUrl = "https://www.yammer.com/api/v1/group_memberships.json?group_id=" + group_id;
        URI uri = new URI(messageLikeUrl);
        HttpResponse executeRestRequest = executeRestRequest(POST_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }

    public String leaveGroup(String group_id) throws URISyntaxException, Exception {
        String messageLikeUrl = "https://www.yammer.com/api/v1/group_memberships.json?group_id=" + group_id;
        URI uri = new URI(messageLikeUrl);
        HttpResponse executeRestRequest = executeRestRequest(DELETE_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }
    
    public String getUsersInGroup(String group_id) throws URISyntaxException, Exception {
        String messageLikeUrl = "https://www.yammer.com/api/v1/users/in_group/"+group_id+".json";
        URI uri = new URI(messageLikeUrl);
        HttpResponse executeRestRequest = executeRestRequest(GET_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }
    
    public void exportYammerData() throws URISyntaxException, Exception{
       
//        GoogleMailServices googleMailServices = new GoogleMailServices("501464058594-u5l9b2kbs0pnaq0d22tb131sgutdk718.apps.googleusercontent.com", "h6qXovBfNGON3YXNkmU0slkh", "1/p0qgpfGaJ8pZNM1zollfnOJ9DcBI2A9M8xoCQ6S94FhIgOrJDtdun6zK6XiATCKT");
//        googleMailServices.printLabelsInUserAccount();
        Reporter.log("Export Doc : https://developer.yammer.com/docs/data-export-api",true);
        String exportUrl="https://www.yammer.com/api/v1/export";
        List<NameValuePair> params=new ArrayList<>();
        
        params.add(new BasicNameValuePair("since","2016-02-27T00:00:00+00:00"));
        params.add(new BasicNameValuePair("access_token",this.accessToken));
        
        HttpResponse executeRestRequest = executeRestRequest(GET_METHOD, new URI(exportUrl), headerList, null, params);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        
        HttpEntity entity = executeRestRequest.getEntity();
        InputStream content = entity.getContent();
        System.out.println("Content Size :"+content.available());
       
        
        
        
    }
    
    
    
    public String performGenericSearch(String query,int pageCount,int num_per_page) throws URISyntaxException, Exception {
        String searchUrl = "https://www.yammer.com/api/v1/search.json?search="+query+"&page="+pageCount+"&num_per_page="+num_per_page;
        URI uri = new URI(searchUrl);
        HttpResponse executeRestRequest = executeRestRequest(GET_METHOD, uri, headerList, null, null);
        System.out.println("Response Code :" + CommonTest.getResponseStatusCode(executeRestRequest));
        return CommonTest.getResponseBody(executeRestRequest);
    }
    
    
    
    public String updateUrl(String older_than, String newer_than, String limit, String fetchMessageUrl) {
        if (older_than != null & newer_than != null & limit != null) {
            fetchMessageUrl = fetchMessageUrl + "?older_than=" + older_than + "&newer_than=" + newer_than + "&limit=" + limit;
        } else if (older_than != null & newer_than != null) {
            fetchMessageUrl = fetchMessageUrl + "?older_than=" + older_than + "&newer_than=" + newer_than;
        } else if (older_than != null) {
            fetchMessageUrl = fetchMessageUrl + "?older_than=" + older_than;
        } else if (newer_than != null) {
            fetchMessageUrl = fetchMessageUrl + "?newer_than=" + newer_than;
        } else if (limit != null) {
            fetchMessageUrl = fetchMessageUrl + "?limit=" + limit;
        } else {
            fetchMessageUrl = fetchMessageUrl;
        }
        System.out.println("## Yammer Request :" + fetchMessageUrl);
        return fetchMessageUrl;
    }
 
    public String regenerateRefreshTokenUsingClientFlow(String username, String password) throws Exception {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        DesiredCapabilities capability = DesiredCapabilities.htmlUnit();
        capability.setJavascriptEnabled(true);
        capability.setBrowserName("htmlunit");
        capability.setVersion("internet explorer");
        capability.setPlatform(org.openqa.selenium.Platform.ANY);
        WebDriver driver = new HtmlUnitDriver(capability);
        Reporter.log("Trying to authorize yammer ..." + username, true);
        //Auth URL
        String authUrl = "";
        authUrl = "https://www.yammer.com/dialog/oauth?client_id=QrQJky1mx1M1DubuecwekA&redirect_uri=https%3A%2F%2Fwww.elastica.net&response_type=token";
        Reporter.log("Auth url:" + authUrl, true);
        try{
        driver.get(authUrl);
        }
        catch(Exception e){
            
        }
        Reporter.log("Waiting for the page to load ...", true);
        Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
        String currentUrl = driver.getCurrentUrl();
        Reporter.log("Current url1:" + currentUrl, true);
        driver.findElement(By.cssSelector("#login")).sendKeys(username);
        driver.findElement(By.cssSelector("#password")).sendKeys(password);
        driver.findElement(By.cssSelector(".yj-btn.yj-btn-secondary")).click();
        Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
        currentUrl = driver.getCurrentUrl();
        Reporter.log("Current url2:" + currentUrl, true);
        driver.findElement(By.cssSelector("#cred_userid_inputtext")).sendKeys(username);
        driver.findElement(By.cssSelector("#cred_password_inputtext")).sendKeys(password);
        driver.findElement(By.cssSelector("#cred_sign_in_button")).click();
        try {
            Thread.sleep(8000);
            driver.findElement(By.cssSelector("#cred_sign_in_button")).click();
        } catch (Exception e) {
            Reporter.log("CSS SELECTION EXCEPTION :" + e.getLocalizedMessage(), true);
        }
        currentUrl = driver.getCurrentUrl();
        Reporter.log("Current url3:" + currentUrl, true);
        Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
        currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains("access_token")) {
            WebElement elem = (new WebDriverWait(driver, 30)) //added this line
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='oauth2-authorize']/div[3]/div[3]/form/input[1]")));
            driver.findElement(By.xpath(".//*[@id='oauth2-authorize']/div[3]/div[3]/form/input[1]")).click();
            Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
            currentUrl = driver.getCurrentUrl();
        }
        String decodedUrl = URLDecoder.decode(currentUrl, "UTF-8");
        Reporter.log("Current url:" + currentUrl, true);
        String query = decodedUrl.split("\\#")[1];
        final Map<String, String> map = Splitter.on('#').trimResults().withKeyValueSeparator("=").split(query);
        Reporter.log("Token:" + map.get("access_token"), true);
        String usertoken = username + "," + map.get("access_token");
        Reporter.log("Token String:" + usertoken, true);
        driver.quit();
        return map.get("access_token").toString();
    }

    public WebElement visibilityOfElementLocated(WebDriver driver, By locator) {
        WebElement element = null;
        WebDriverWait wait = new WebDriverWait(driver, 120);
        element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element;
    }

    public static void main(String[] args) throws Exception {

        String username = "qa-stress1@o365security.net"; // Aut0mat10n#123
        String accessToken = "2224894-2E9gSIThJzwNUD1Q9mg";

        Yammer yammer = new Yammer(accessToken);
       // System.out.println("==>" + yammer.getAllRawMessages());
        // 1. Share To a Public Group...Public Broadcasting...
       /* String postMessage = yammer.postMessage("Warm Welcome to BLR-QA Team <Stress Test>..."+new Date(), null); 
         System.out.println("Post Message Response :"+postMessage);*/
        //2. Delete Message
        // String deleteMessage = yammer.deleteMessage("650464986");
        //System.out.println("==>"+deleteMessage);
        // 3. Send the current user a copy of the message specified by the numeric string ID.
        // yammer.sendEmailToAutenticatedUser("650464986");
        // System.out.println("User Info :"+yammer.getUserInfo("1565269612"));
        // System.out.println("User Info :"+yammer.getCurrentUserInfo());
//        List<Message> allMessages = yammer.getAllMessages("649298400","0","500");
//        for (Message allMessage : allMessages) {
//            System.out.println("Message ID :" + allMessage.getId());
//        }
          String pendAttachment = yammer.pendAttachment("/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/UniversalAPI/src/main/java/com/universal/common/Box.java",
               "RahulBangalore.java");
          System.out.println("Pend Attchement Response:"+pendAttachment);
          
         // String id = RawJsonParser.getSingleKey(pendAttachment, "$.id");
        //  String deleteAttchment=yammer.deleteAttachment("50977925");
        //  System.out.println("==>"+deleteAttchment);
          
        // Group Id = 6736336;
        //System.out.println("Perform Search :"+yammer.performGenericSearch("Group", 1, 5)); 
      //  System.out.println("Post Message to a group :"+yammer.postMessage("Post msg to group12345", "6736336"));
        
    }
}
