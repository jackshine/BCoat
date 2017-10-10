package com.elastica.beatle.tests.securlets;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxSharing;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.google.api.services.gmail.model.Message;
import com.universal.common.DropBox;
import com.universal.common.DropboxBusinessAccActivities;
import com.universal.common.GExcelDataProvider;
import com.universal.common.GoogleMailServices;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.FileUploadResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.joda.time.DateTime;
import org.jsfr.json.JsonSurfer;
import org.testng.Assert;
import static org.testng.Assert.assertTrue;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DropboxSecurletTestDisplaylog extends CommonTest {

    String SaasApp_DropBox="Dropbox";
    String from_jodaTime;
    UniversalApi universalApi;
    String uniquePrefix=DropboxDataProvider.uniqueIdentifier;
    String qaTestFolderInCloud="/"+uniquePrefix+"QARegressionTest";
    String qaTestTmpFolderInCloud;
    String fileToUpload="glba-test.txt";
    String fileToBeCopied;
    String fileTobeMoved;
    String fileTobeRenamed;
    String userIdForFileSharing = "rahul.kumar@elastica.co";
    String qaTestFolderInCloudForSharedFiles;
    String localFileLocation;
    int retryCount = 0; // Variable Initialization...
    int maxRetryCount;
    int waitTimeToReceiveLog;//secs
    int delayBetweenSaaSActivity; //ms
    
    String methodName;
    static SecurletsUtils securletsUtils = new SecurletsUtils();
    List<Map<String, String>> totalResponseList;
    List<String> childNodeList;
    String parentNode="$.hits.hits[*].source";
    String activityType;
    String externalUser;
    DropboxBusinessAccActivities dropboxBusinessAccActivities;
    JsonSurfer jsonSurfer = JsonSurfer.simple();
    String memberId;
    List<NameValuePair> headers;
    String url;
    String environmentName;
    GoogleMailServices googleMailServices;
    DropBox dropbox;
    String json_Response = null;
    GExcelDataProvider dataProvider;
    
   
    private void initializeVariables() {
        totalResponseList = new ArrayList<>();
        Reporter.log("\n ====== #### Variable Initialization #### ===========", true);
        //Relative Local file location..
        localFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + fileToUpload;
        delayBetweenSaaSActivity = Integer.parseInt(getRegressionSpecificSuitParameters("delayBetweenSaaSActivity")) * 1000;
        maxRetryCount = Integer.parseInt(getRegressionSpecificSuitParameters("maxRetryCount"));
        waitTimeToReceiveLog = Integer.parseInt(getRegressionSpecificSuitParameters("waitPeriodForElasticSearchEngine"));
        environmentName = getRegressionSpecificSuitParameters("environmentName");
        Reporter.log("Environment :" + environmentName + " Delay Between SaaS Activity :" + delayBetweenSaaSActivity/1000 + " secs", true);
        Reporter.log("Environment :" + environmentName + " Wait Time To Sink Logs :" + waitTimeToReceiveLog + " secs", true);
        Reporter.log("====== #### Loading Expected Response And Variable Initialization !!! DONE !!! ===========", true);
    }

    @BeforeClass(alwaysRun = true)
    public void oneTimeSetUp() throws Exception {
        System.setProperty("jsse.enableSNIExtension","false");
        System.setProperty("java.net.preferIPv4Stack" , "true");
        System.out.println("##### Disabled the Java Security to fix the Hand Shake Alert issue...###### ");
        initializeVariables();
        initializeSaaSApp();
    }

    public void initializeSaaSApp() {
        DropboxDataProvider.initialize();
        Reporter.log(" ## System File Location :" + localFileLocation, true);
        String accessToken = getRegressionSpecificSuitParameters("dropboxAdminAccessToken");
        String dropboxTeamMemberManagementToken = getRegressionSpecificSuitParameters("dropboxTeamMemberManagementToken");
        String dropboxTeamMemberFileAccessToken = getRegressionSpecificSuitParameters("dropboxTeamMemberFileAccessToken");
        UserAccount account = new UserAccount("rahul.kumar@elastica.co", "xxx", "ADMIN", accessToken); // Initialize the Useraccount for Dropbox
        dropboxBusinessAccActivities = new DropboxBusinessAccActivities(dropboxTeamMemberManagementToken, dropboxTeamMemberFileAccessToken);
        try {
            universalApi = new UniversalApi("DROPBOX", account);// Create Universal API reference
            this.dropbox=universalApi.getDropbox();
        } catch (Exception ex) {
            Reporter.log("Issue with creating Universal API " + ex.getLocalizedMessage());
        }
        Reporter.log("============== ## Initializing SaaS APP Client  DONE !! ## =======================", true);
    }
    
    @Test(groups={"FILESANDFOLDER", "P1"})
    public void performDropboxFilesAndFolderCrudAndPermissionActivities() throws IOException, DbxException, InterruptedException, Exception {
        from_jodaTime = RawJsonParser.getCurrentJodaTime();
        RawJsonParser.wait(10 * 1000, "Waiting Before Performing File And Folder Operation .....");
        performFileAndFolderActivities();
        performFilePermissionActivities();
        performFolderPermissionActivities();
        performGroupActivities();
        performMemberActivities();
        RawJsonParser.wait(waitTimeToReceiveLog * 1000, "Waiting Before Elastica Search engine to sink Logs");
        this.json_Response = RawJsonParser.getDisplayLogResponse(suiteData, getHeaders(), SaasApp_DropBox, from_jodaTime);
    }

    
    public void performFileAndFolderActivities() throws Exception {
        long sleepDuration=30000;
        RawJsonParser.wait(sleepDuration, "Waiting Before Performing File And Folder Operation .....");
        Reporter.log("\n================= DROPBOX SaaS Actions : FOLDER CREATION & FILE UPLOAD =====================", true);
        String tempFolder = qaTestFolderInCloud + "Temp";
        Reporter.log(" ## Local File Location :" + localFileLocation, true);
        Reporter.log(" ## Creating Folder in Cloud :-" + qaTestFolderInCloud, true);
        Reporter.log(" ## Uploading File :" + fileToUpload + " to the  Folder :" + qaTestFolderInCloud, true);
        RawJsonParser.wait(sleepDuration, "Waiting Before Folder Create Folder and Upload a File... .....");
        dropbox.uploadFile(qaTestFolderInCloud, localFileLocation);
        RawJsonParser.wait(sleepDuration, "Waiting Before Folder CREATE TMP FOLDER .....");
        dropbox.createFolder(tempFolder);
        RawJsonParser.wait(sleepDuration, "Waiting Before COPY File To Temp Folder .....");
        dropbox.copyFile(qaTestFolderInCloud + "/" + fileToUpload, tempFolder + "/" + uniquePrefix + "CopyFile.txt");
        RawJsonParser.wait(sleepDuration, "Waiting Before MOVE File To Temp Folder .....");
        dropbox.moveFile(qaTestFolderInCloud + "/" + fileToUpload, tempFolder + "/" + uniquePrefix + "MoveFile.txt");// No event for file 
        RawJsonParser.wait(sleepDuration, "Waiting Before COPY Folder Operation .....");
        dropbox.copyFolder(qaTestFolderInCloud, tempFolder + "/" + uniquePrefix + "QATestCopyFolder");
        RawJsonParser.wait(sleepDuration, "Waiting Before Performing MOVE Folder Operation .....");
        dropbox.moveFolder(qaTestFolderInCloud, tempFolder + "/" + uniquePrefix + "QATestMoveFolder");// Move Folder..
        RawJsonParser.wait(sleepDuration, "Waiting Before Performing File DELETE .....");
        dropbox.deleteFileOrFolder(tempFolder + "/" + uniquePrefix + "MoveFile.txt");
        RawJsonParser.wait(sleepDuration, "Waiting Before  Folder DELETE .....");
        dropbox.deleteFileOrFolder(tempFolder + "/" + uniquePrefix + "QATestCopyFolder");
        RawJsonParser.wait(sleepDuration, "Waiting Before CREATING PUBLIC SHARED LINK OF A FILE .....");
        dropbox.createSharedLinkForFolderORFile(tempFolder + "/" + uniquePrefix + "CopyFile.txt");
        RawJsonParser.wait(sleepDuration, "Waiting Before TRASH TMP FOLDER.....");
        dropbox.deleteFileOrFolder(tempFolder);
    }
    
    private void performFilePermissionActivities() throws InterruptedException, Exception {
        long sleepDuration=30000;
        Reporter.log("\n================= DROPBOX SaaS Actions : FILE Permission Activities..... =====================", true);
        String folderName = qaTestFolderInCloud + "_FilePermissionTest" ;
        String uniquefileName=uniquePrefix+"FilePermissionTest"+".txt";
        FileUploadResponse uploadFile = dropbox.uploadFile(folderName, localFileLocation,uniquefileName);
        String fileId=uploadFile.getFileId();
        RawJsonParser.wait(sleepDuration, "Waiting Before Creating Shared LINK .....");
        String createSharedLinkForFolder = universalApi.createSharedLinkForFolder(folderName+"/"+uniquefileName);
        Reporter.log(" #### Public Shared Link :" + createSharedLinkForFolder, true);
        RawJsonParser.wait(sleepDuration, "Waiting After Creating Shared LINK.....");
        restClient.doGet(new URI(createSharedLinkForFolder), null);
        RawJsonParser.wait(sleepDuration, "Waiting After OPENING the Shared LINK.....");
        universalApi.deleteFile(folderName+"/"+uniquefileName,null);
    }

    private void performFolderPermissionActivities() throws InterruptedException, IOException, DbxException, Exception {
        long sleepDuration = 30000;//in ms
        Reporter.log("\n================= DROPBOX SaaS Actions : Folder Permission Activities..... =====================", true);  
        String[] permissions = {"viewer", "editor"};
        Map<String,String> memberWithAccessToken=new HashMap<>();
        
        memberWithAccessToken.put("rahul.embeddedsystem@gmail.com#Internal", "9Y_ElxgLzkkAAAAAAAADC_fxoAgezpSt8GUX6uOD4maIFciJLKRuoCJ9P8JR8PwZ");
        memberWithAccessToken.put("admin@securletfeatle.com#External", "SWj60UQU0SAAAAAAAAAACBaFAAec7UEF7_kucoqOP_Hx4vknuRGsFvEtvgXWoOZO");
        
        for (Map.Entry<String, String> entrySet : memberWithAccessToken.entrySet()) {
            String key = entrySet.getKey();
            String accessToken = entrySet.getValue();
            String memberEmailId=key.split("#")[0];
            String userType=key.split("#")[1];
            
        for (String permission : permissions) {
            String folderName = qaTestFolderInCloud + "_" + permission+userType;
            String folderNameUnShareTest = qaTestFolderInCloud + "_" + "folderNameUnShareTest-"+permission+userType;
            dropbox.uploadFile(folderName, localFileLocation);
            RawJsonParser.wait(sleepDuration, "Waiting After FOLDER CREATE :"+folderName);
            if (permission.equals("viewer")) {
                dropbox.shareAndMountFolderToUser(folderName, memberEmailId, DbxSharing.AccessLevel.viewer, accessToken);
            } else {
                dropbox.shareAndMountFolderToUser(folderName, memberEmailId, DbxSharing.AccessLevel.editor, accessToken);
            }
            RawJsonParser.wait(sleepDuration, "Waiting After share And Mount :"+folderName);
            if (permission.equals("viewer")) {
                dropbox.updateFolderShare(folderName, memberEmailId, DbxSharing.AccessLevel.editor);
            } else {
                dropbox.updateFolderShare(folderName, memberEmailId, DbxSharing.AccessLevel.viewer);
            }
            
            RawJsonParser.wait(sleepDuration, "Waiting Before Folder TRASNSFER :"+folderName);
            dropbox.transferFolderOwnerShip(folderName, accessToken);
            RawJsonParser.wait(sleepDuration, "Waiting After FOLDER unshare :"+folderName);
            dropbox.unshareFolder(folderNameUnShareTest); 
        }
        }
    }
    
     String emailIDForNewMember = "rahul.embeddedsystem@gmail.com";
    public void performGroupActivities() throws Exception {
        long sleepDuration = 30000;//in ms
        String uniqueGroupName = "AutomationGroup" + uniquePrefix;
        String memberIdExistingUser = dropboxBusinessAccActivities.getMemberId(emailIDForNewMember);
        Reporter.log("Member ID :"+memberIdExistingUser);
        
        RawJsonParser.wait(sleepDuration, " Create Group:" + uniqueGroupName);
        String createGroup = dropboxBusinessAccActivities.createGroup(uniqueGroupName);
        Reporter.log("Create Group Response:"+createGroup,true);
        String groupId = (String) RawJsonParser.fetchSingleField(createGroup, "$.group_id"); 
        String groupInfo = dropboxBusinessAccActivities.getGroupInfo(groupId);
        Reporter.log("Get Group Info :" + groupInfo,true);
        
        RawJsonParser.wait(sleepDuration, " Add members To the Group:" + uniqueGroupName);
        String addMember = dropboxBusinessAccActivities.addMembersToGroup(groupId, memberIdExistingUser, "member");
        Reporter.log("Add Member To the Group :" + addMember,true);
        
        RawJsonParser.wait(sleepDuration, " Set Group members AccessType :" + uniqueGroupName);
        String setGroupMemberAccessType = dropboxBusinessAccActivities.setGroupMemberAccessType(groupId, memberIdExistingUser, "member");
        Reporter.log("Set Group Access Type :" + setGroupMemberAccessType,true);

        RawJsonParser.wait(sleepDuration, " Remove member from the Group :" + uniqueGroupName);
        String removeMembersFromGroup = dropboxBusinessAccActivities.removeMembersFromGroup(groupId, memberIdExistingUser);
        Reporter.log("Remove Member From Group :" + removeMembersFromGroup,true);
       
        RawJsonParser.wait(sleepDuration, " Delete/Remove Group :" + uniqueGroupName);
        String deleteGroup = dropboxBusinessAccActivities.deleteGroup(groupId);
        Reporter.log("Delete Group :" + deleteGroup,true);     
    }
    
    public void performMemberActivities(){
        long sleepDuration = 30000;//in ms
        Reporter.log("========= Perform Member Activities ===========",true);
        
        String memberIdExistingUser = dropboxBusinessAccActivities.getMemberId(emailIDForNewMember);
        Reporter.log("Member ID :"+memberIdExistingUser);
        
//        RawJsonParser.wait(sleepDuration, " Remove Member...");// Requries Email..Notification...
//        String removeMember = dropboxBusinessAccActivities.removeMember(memberIdExistingUser);
//        Reporter.log("Remove Member :"+removeMember,true);
//        
//        RawJsonParser.wait(sleepDuration, " Add Member....");//Requries Email..Notification...
//        String addMember = dropboxBusinessAccActivities.addMember(memberIdExistingUser,"Rahul","Kumar");
//        Reporter.log("Add Member :"+addMember,true);
        
        RawJsonParser.wait(sleepDuration, " Set Member Profile ");
        memberIdExistingUser = dropboxBusinessAccActivities.getMemberId(emailIDForNewMember);
        Reporter.log("Member ID :"+memberIdExistingUser);
        String setProfile = dropboxBusinessAccActivities.setProfile(memberIdExistingUser, emailIDForNewMember, "Mohammad Kallamuddin Ansari_"+uniquePrefix);
        Reporter.log("Set Profile Response :"+setProfile,true);
        
        RawJsonParser.wait(sleepDuration, " Set Member Permission <ADMIN> ");
        String setPermission=dropboxBusinessAccActivities.setPermission(memberIdExistingUser, "true");
        Reporter.log("Set Permission :" + setPermission,true);
        
        RawJsonParser.wait(sleepDuration, " Set Member Permission <USER> ");
        setPermission=dropboxBusinessAccActivities.setPermission(memberIdExistingUser, "false");
        Reporter.log("Set Permission :" + setPermission,true);
        
        RawJsonParser.wait(sleepDuration, " Send Welcome Mail ");
        String sendWelcomeMail = dropboxBusinessAccActivities.sendWelcomeMail(memberIdExistingUser);
        Reporter.log("Send Welcome Mail :" + sendWelcomeMail,true);   
        
    }
    
    @Test(groups={"FILESANDFOLDER", "P1"},dataProviderClass = DropboxDataProvider.class,dataProvider = "data-provider-FilesAndFolderActivities")
    public void validateFilesAndFolderActivities(String desc,String msg){
        Map<String, String> expectedResult = new HashMap();
        expectedResult.put("message", msg.replaceAll("UNIQUEPREFIX", uniquePrefix));
        RawJsonParser.LogValidator(this.json_Response,expectedResult,parentNode,"message");   
    }
    
    @Test(groups={"FILESANDFOLDER", "P1"},dataProviderClass = DropboxDataProvider.class,dataProvider = "data-provider-FilesAndFolderPermissionActivities")
    public void validateFilesAndFolderPermissionActivities(String testName,String msg){
        Map<String, String> expectedResult = new HashMap();
        expectedResult.put("message", msg.replaceAll("UNIQUEPREFIX", uniquePrefix));
        RawJsonParser.LogValidator(this.json_Response,expectedResult,parentNode,"message");   
    }

    @Test(groups={"FILESANDFOLDER", "P1"},dataProviderClass = DropboxDataProvider.class,dataProvider = "data-provider-GroupActivities")
    public void validateBusinessActivities(String testName,String msg){
        Map<String, String> expectedResult = new HashMap();
        expectedResult.put("message", msg.replaceAll("UNIQUEPREFIX", uniquePrefix));
        RawJsonParser.LogValidator(this.json_Response,expectedResult,parentNode,"message");   
    }
    
    @Test(groups={"FILTER", "P2"},dataProviderClass = DropboxDataProvider.class, dataProvider = "LocationFilter")
    public void dashboardLocationTypeFilters(String severityType, String location) throws Exception {
        ForensicSearchResults logs;
        LogUtils.logTestDescription("Retrieve the activities and filter them by name:" + severityType + " and location:" + location);
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", SaasApp_DropBox);
        termmap.put("severity", severityType);
        termmap.put("__source", "API");
        termmap.put("location", location);
        termmap.put("user", getRegressionSpecificSuitParameters("userName"));
        List<String> keyList = new ArrayList();
        keyList.add("severity");
        for (int retry = 0; retry < 1; retry++) {
            try {
                String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
                //Fetch the activity logs from yesterday to tomorrow and limited to 500
                //Get file related logs
                String responseBody = getInvestigateLogs(-18000, 10, SaasApp_DropBox, termmap, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_DropBox);
                List<Map<String, Object>> filterRequiredFieldsFromResponse = RawJsonParser.fetchAllKeys(responseBody, parentNode);
                Reporter.log(" Total Number of Results : " + filterRequiredFieldsFromResponse.size(), true);
                for (Map<String, Object> map : filterRequiredFieldsFromResponse) {
                    String value = map.get("severity").toString();
                    CustomAssertion.assertTrue(value.equals(severityType), "Severity Type is " + severityType, "Severity Type is not " + severityType);
                }

                int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(responseBody, "$.hits.total"));
                assertTrue(totalCount > 0, "SeverityType " + severityType + " related messages are not present");
            } catch (Exception e) {
            }
        }
    }

@Test(groups={"FILTERS", "P2"},dataProviderClass = DropboxDataProvider.class, dataProvider = "ObjectTypeFilter")
    public void dashboardObjectTypeFilters(String objType) throws Exception {
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", SaasApp_DropBox);
        termmap.put("Object_type", objType);
        LogUtils.logTestDescription("Retrieve the objecttype and filter them by name:" + objType);
        List<String> keyList = new ArrayList();
        keyList.add("Object_type");
         int from = -18000;
         if (objType.equals("Authentication")) {
             from = -90000;
           }
        for (int retry = 0; retry < 1; retry++) {
            try {
                String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		//Fetch the activity logs from yesterday to tomorrow and limited to 500
                //Get file related logs
               String responseBody= getInvestigateLogs(from, 10, SaasApp_DropBox, termmap, suiteData.getUsername().toLowerCase(),
                        apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_DropBox);
                
               System.out.println("Response Body :"+responseBody);
               List<Map<String, Object>> filterRequiredFieldsFromResponse = RawJsonParser.fetchAllKeys(responseBody, parentNode);
                for (Map<String, Object> map : filterRequiredFieldsFromResponse) {
                    String value = map.get("Object_type").toString();
                    CustomAssertion.assertTrue(value.equals(objType), "Object Type Type is " + objType, "Severity Type is not " + objType);
                }
                Reporter.log(" Total Number of Results : " + filterRequiredFieldsFromResponse.size(), true);
                int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(responseBody, "$.hits.total"));
                assertTrue(totalCount > 0, "ObjectType " + objType + " related messages are not present");
            } catch (Exception e) {
                //if any exception, please retry after waiting for 20 secs 
                sleep(20);
            }
        }
    }

@Test(groups={"FILTERS", "P2"},dataProviderClass = DropboxDataProvider.class, dataProvider = "SeverityFilter")
    public void dashboardSeverityTypeFilters(String severityType) throws Exception {
        ForensicSearchResults logs;
        LogUtils.logTestDescription("Retrieve the activities and filter them by name:" + severityType);
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", SaasApp_DropBox);
        termmap.put("severity", severityType);
        termmap.put("__source", "API");
        List<String> keyList = new ArrayList();
        keyList.add("severity");
        for (int retry = 0; retry < 1; retry++) {

            try {
                String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
                //Fetch the activity logs from yesterday to tomorrow and limited to 500
                //Get file related logs
                String responseBody = getInvestigateLogs(-18000, 10, SaasApp_DropBox, termmap, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_DropBox);
                List<Map<String, Object>> filterRequiredFieldsFromResponse = RawJsonParser.fetchAllKeys(responseBody, parentNode);
                Reporter.log(" Total Number of Results : " + filterRequiredFieldsFromResponse.size(), true);
                for (Map<String, Object> map : filterRequiredFieldsFromResponse) {
                    String value = map.get("severity").toString();
                    CustomAssertion.assertTrue(value.equals(severityType), "Severity Type is " + severityType, "Severity Type is not " + severityType);
                }
               // validateNullCheck(filterRequiredFieldsFromResponse);
                int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(responseBody, "$.hits.total"));
                assertTrue(totalCount > 0, "SeverityType " + severityType + " related messages are not present");
            } catch (Exception e) {
                Reporter.log("Expection Found in Parsing :"+e.getLocalizedMessage(),true);
            }
        }
    }
    
    // Activity Filters...
    @Test(groups={"FILTERS", "P2"},dataProviderClass = DropboxDataProvider.class, dataProvider = "ActivityTypeFilter")
    public void dashboardActivityTypeFilters(String activityType) throws Exception {
       // ForensicSearchResults logs;
        LogUtils.logTestDescription("Retrieve the activities and filter them by name:" + activityType);
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", SaasApp_DropBox);
        termmap.put("Activity_type", activityType);

        for (int retry = 0; retry < 1; retry++) {

            try {
                String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		//Fetch the activity logs from yesterday to tomorrow and limited to 500
                //Get file related logs
                List<String> keyList = new ArrayList();
                keyList.add("Activity_type");
                keyList.add("Object_type");
                keyList.add("facility");
                keyList.add("latency");
                keyList.add("user");
                keyList.add("File_Size");
                int from=-18000;      
                if(activityType.equals("Login")){
                    from=-90000;
                }
                String responseBody = getInvestigateLogs(from, 10, SaasApp_DropBox, termmap, suiteData.getUsername().toLowerCase(),
                        apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_DropBox);
                List<Map<String, Object>> filterRequiredFieldsFromResponse = RawJsonParser.fetchAllKeys(responseBody, parentNode);
                Reporter.log(" Total Number of Results : "+filterRequiredFieldsFromResponse.size(),true);
                for (Map<String, Object> map : filterRequiredFieldsFromResponse) {
                   String value= map.get("Activity_type").toString();
                   CustomAssertion.assertTrue(value.equals(activityType), "ActivityType is "+activityType, "ActivityType is not "+activityType);
                }
               // validateNullCheck(filterRequiredFieldsFromResponse);
                int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(responseBody, "$.hits.total"));
                assertTrue(totalCount > 0, "ActivityType " + activityType + " related messages are not present");   
            } catch (Exception e) {
            }
        }
    }
    
    
    @Test(groups={"EXPORT", "P1"},dataProviderClass = DropboxDataProvider.class,dataProvider = "data-provider-ExportCSVSecurlet")
    public void validateSecurletCSVExport(String testName,String attachmentQuery,String payload) throws Exception{
        String refreshToken_gmail = getRegressionSpecificSuitParameters("refreshToken_gmail");
        String CLIENT_ID_gmail = getRegressionSpecificSuitParameters("CLIENT_ID_gmail");
        String CLIENT_SECRET_gmail = getRegressionSpecificSuitParameters("CLIENT_SECRET_gmail");
        this.googleMailServices = new GoogleMailServices(CLIENT_ID_gmail, CLIENT_SECRET_gmail, refreshToken_gmail);
        Reporter.log("Waiting before CSV Export",true);
        Thread.sleep(30000);
        LogUtils.logTestDescription(testName); 
        String path=suiteData.getAPIMap().get("getUIExportCSV");
        if(testName.equals("Test Name:validate Export Exposed Apps")){
            path="/admin/application/list/get_export_csv_apps_data";
        }
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, null);
        HttpResponse response = restClient.doPost(dataUri, getHeaders(), null, new StringEntity(payload));
        String responseBody = ClientUtil.getResponseBody(response);
        Reporter.log("Response body:" + responseBody, true);
        securletsUtils.wait(30, "## Waiting after securlet Export..."+testName);//Waiting for 30 secs.....
        String mailSearchQuery="Dropbox Securlet Data Export";
        Message latestMail = googleMailServices.getLatestMail(mailSearchQuery);
        String actualFileName = null;
        int fileSize = 0;
        Map<String, byte[]> attachments = googleMailServices.getAttachments(latestMail);
        for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
            String key = entrySet.getKey();
            byte[] value = entrySet.getValue();
            Reporter.log("File Name :"+key  + " ## Attachment Size :"+value.length,true);  
            actualFileName=key;
            fileSize=value.length;
        }
        String expectedStringInFileName=attachmentQuery+DateUtils.getCurrentDate();
        Reporter.log("Expected String in File Name :"+expectedStringInFileName,true);
        Assert.assertEquals(fileSize>0,true,testName+ ": File size should not be zero , File Size Found :"+fileSize);
        Assert.assertEquals(actualFileName.contains(expectedStringInFileName),true,testName+ " File not found in the attachemnt..."); 
    }
    
    @Test(groups={"EXPORT", "P1"},priority = 23)
    public void performExportActivities() throws Exception {
        String refreshToken_gmail = getRegressionSpecificSuitParameters("refreshToken_gmail");
        String CLIENT_ID_gmail = getRegressionSpecificSuitParameters("CLIENT_ID_gmail");
        String CLIENT_SECRET_gmail = getRegressionSpecificSuitParameters("CLIENT_SECRET_gmail");
        this.googleMailServices = new GoogleMailServices(CLIENT_ID_gmail, CLIENT_SECRET_gmail, refreshToken_gmail);
        DateTime dateTime = new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC);
        String endDate = dateTime.toString();
        String startDate = dateTime.minusDays(7).toString();
        List<NameValuePair> headers = getHeaders();
        String path = suiteData.getAPIMap().get("getActivityLogExport") ;
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
        Reporter.log("URI ::" + dataUri.toString(), true);
        LogUtils.logTestDescription("Export the exposed files to user email and check");
        String[] formatList = {"csv","cef","leef"};
        for (String format : formatList) {
        String exportPayload = securletsUtils.getExportPayloadForGDrive(format, SaasApp_DropBox, suiteData.getCSRFToken(), suiteData.getSessionID(), suiteData.getApiserverHostName(), "admin@securletbeatle.com", startDate, endDate);
        Reporter.log("Export PayLoad :"+exportPayload,true);
        HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(exportPayload));
        String responseBody = ClientUtil.getResponseBody(response);
        Reporter.log("Response body:" + responseBody, true);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        }
        securletsUtils.wait(30, "Waiting After Export Activities...");
        }
    
    @Test(groups={"EXPORT", "P1"},priority = 24, dataProviderClass = DropboxDataProvider.class,dataProvider = "data-provider-ExportActivities")
    public void validateExportActivities(String testName, String format) throws IOException, URISyntaxException, Exception {
        String query = format + " Log Export Request";
        Message latestMail = googleMailServices.getLatestMail(query);
        String downloadHref = googleMailServices.getDownloadHref(latestMail);
        Reporter.log("Download URL :"+downloadHref,true);
        List<NameValuePair> headers = getHeaders();
        HttpResponse response = restClient.doGet(new URI(downloadHref), headers);
        Header lastHeader = response.getLastHeader("Content-Disposition");
        HeaderElement[] elements = lastHeader.getElements();
        String fileName=lastHeader.getValue();
        Reporter.log("File Name As In Header <Content-Disposition> :"+fileName,true);
        fileName = fileName.replace("attachment; filename=\"", "").replaceAll("\"", "");
        Reporter.log("Actual File Name Downloaded :"+fileName,true);
        String expectedSubStringInFileName="log_"+format+"_"+DateUtils.getCurrentDate();
        Reporter.log("Expected SubString In File Name :"+expectedSubStringInFileName,true);
        Assert.assertTrue(fileName.contains(expectedSubStringInFileName),"Wrong Exported File Name Found...");
    } 
    
    
public String getInvestigateLogs(int from, int to, String facility, HashMap<String, String> hmap, String email,
            String apiServerUrl, String csrfToken, String sessionId, int offset, int limit, String sourceName) throws Exception {
        Reporter.log("Retrieving the logs from Elastic Search ...", true);
        ESQueryBuilder esQueryBuilder = new ESQueryBuilder();
        String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
        String tsto = DateUtils.getMinutesFromCurrentTime(to);
        //Get headers
        List<NameValuePair> headers = getHeaders();
        String payload = "";
        payload = esQueryBuilder.getESQuery(tsfrom, tsto, facility, hmap, email, apiServerUrl, csrfToken, sessionId, offset, limit, sourceName);
        Reporter.log("Request body:" + payload, true);
        //HttpRequest
        String path = suiteData.getAPIMap().get("getInvestigateLogs");
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
        Reporter.log("URI ::" + dataUri.toString(), true);
        HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(payload));
        String responseBody = ClientUtil.getResponseBody(response);
        Reporter.log("==============================================================================");
        System.out.println("Response Body :"+responseBody);
        return responseBody;
    }

}
