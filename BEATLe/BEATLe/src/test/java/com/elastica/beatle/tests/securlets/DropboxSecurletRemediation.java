/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle.tests.securlets;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIConstants;
import com.universal.common.DropboxBusinessAccActivities;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.FileUploadResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import junit.framework.Assert;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.IOReactorException;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class DropboxSecurletRemediation extends CommonTest {

    String tenant;
    SecurletsUtils securletsUtils;
    List<NameValuePair> headers;
    String user;
    String urlRemediation;
    String urlDocInfo;
    String SaaSApp = "el_dropbox";
    String localFileLocation;
    String fileToUpload = "glba-test.txt";
    DropboxBusinessAccActivities dropboxBusinessAccActivities;
    int delayBetweenSaaSActivity;
    String qaTestFolderInCloud;
    String uniqueFileName_delete;
    String uniqueFileName_quarantine;

    @BeforeClass(alwaysRun = true)
    public void oneTimeSetUp() throws Exception {
        initializeEnvironmentalVariable();
        initializeSaaSApp();
    }

    private void initializeEnvironmentalVariable() throws IOException, InterruptedException, ExecutionException, KeyManagementException, NoSuchAlgorithmException, IOReactorException, KeyStoreException, URISyntaxException, Exception {
        Reporter.log("\n============== ## Initializing ENVIRONMENT SPECIFIC VARIABLES ## =======================", true);
        List<NameValuePair> buildBasicHeaders = buildBasicHeaders();
        NameValuePair nameValuePair1 = new BasicNameValuePair("Content-Type", "application/json");
        buildBasicHeaders.add(nameValuePair1);
        headers = buildBasicHeaders;
        securletsUtils = new SecurletsUtils();
        String hostName = suiteData.getApiserverHostName();
        String scheme = suiteData.getScheme();
        tenant = suiteData.getTenantName();
        user = getRegressionSpecificSuitParameters("userName");
        urlRemediation = scheme + "://" + hostName + "/" + tenant + "/api/admin/v1/" + SaaSApp + "/remediation/";
        urlDocInfo = scheme + "://" + hostName + "/" + tenant + "/api/admin/v1/" + SaaSApp + "/docs/";
        Reporter.log("Scheme: " + suiteData.getScheme(), true);
        Reporter.log("Hostname: " + suiteData.getApiserverHostName(), true);
        Reporter.log("Tenant :" + suiteData.getTenantName(), true);
        Reporter.log("User :" + user, true);
        Reporter.log("Remediation URL : " + urlRemediation, true);
        Reporter.log("\n============== ## Initializing ENVIRONMENT SPECIFIC VARIABLES  DONE !!! ## =======================", true);
        delayBetweenSaaSActivity = Integer.parseInt(getRegressionSpecificSuitParameters("delayBetweenSaaSActivity"));
    }

    @AfterClass
    public void oneTimeAfterTestSuit(){
        Reporter.log("Post CleanUp ==> deleting file :"+uniqueFileName_delete,true);
        try{
        universalApi.deleteFile(qaTestFolderInCloud, null);
        universalApi.deleteFile("/QARemediationTest.quarantined", null);
        }
        catch(Exception e){
         Reporter.log("Folder/Files Not Found"+e.getLocalizedMessage(),true);   
        }
    }

    public void initializeSaaSApp() {
        qaTestFolderInCloud = File.separator + "QARemediationTest";
        localFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + fileToUpload;
        Reporter.log(" ## System File Location :" + localFileLocation, true);
        String accessToken = getRegressionSpecificSuitParameters("dropboxAdminAccessToken");
        String dropboxTeamMemberManagementToken = getRegressionSpecificSuitParameters("dropboxTeamMemberManagementToken");
        String dropboxTeamMemberFileAccessToken = getRegressionSpecificSuitParameters("dropboxTeamMemberFileAccessToken");
        UserAccount account = new UserAccount("rahul.kumar@elastica.co", "xxx", "ADMIN", accessToken); // Initialize the Useraccount for Dropbox
        dropboxBusinessAccActivities = new DropboxBusinessAccActivities(dropboxTeamMemberManagementToken, dropboxTeamMemberFileAccessToken);
        try {
            universalApi = new UniversalApi("DROPBOX", account);// Create Universal API reference
        } catch (Exception ex) {
            Reporter.log("Issue with creating Universal API " + ex.getLocalizedMessage());
        }
        Reporter.log("============== ## Initializing SaaS APP Client  DONE !! ## =======================", true);
    }
    
    //GDrive: Limitation: No Download logs for file Download

    @Test(groups={"REMEDIATION", "P1"})
    public void performFileUploadAndFolderCreationInSaasApp() throws Exception {
        Reporter.log("********************* TEST DESCRIPTION **********************************",true);
        Reporter.log("=====> DROPBOX SaaS Actions : FOLDER CREATION & FILE UPLOAD <============", true);
        Reporter.log("*************************************************************************",true); 
        Reporter.log(" ## Local File Location :" + localFileLocation, true);
        Reporter.log(" ## Creating Folder in Cloud :-" + qaTestFolderInCloud, true);
        Reporter.log(" ## Uploading File :" + fileToUpload + " to the  Folder :" + qaTestFolderInCloud, true);
        uniqueFileName_delete = securletsUtils.generateUniqueKeyUsingUUID() + "_" + fileToUpload;
        uniqueFileName_quarantine = securletsUtils.generateUniqueKeyUsingUUID() + "_" + fileToUpload;
        FileUploadResponse uploadFile = null;
        try{
        uploadFile = universalApi.uploadFile(qaTestFolderInCloud, localFileLocation, uniqueFileName_delete);
        }
        catch(Exception e){
            Reporter.log("Exception Found in the file Upload :"+e.getLocalizedMessage(),true);
        }
        String fileName = uploadFile.getFileName();
        Reporter.log(" ### File Name of Uploaded File : " + fileName, true);
        FileUploadResponse uploadFile1 = universalApi.uploadFile(qaTestFolderInCloud, localFileLocation, uniqueFileName_quarantine);
        String fileName1 = uploadFile1.getFileName();
        Reporter.log(" ### File Name of Uploaded File : " + fileName1, true);
        String expectedResult="File Name should not be NULL";
        String actualResult=fileName1;
        Reporter.log("## Expected Result :"+expectedResult,true);
        Reporter.log("## Actual Result / File Name  :"+actualResult,true);
        Assert.assertNotNull(actualResult);
        securletsUtils.wait(delayBetweenSaaSActivity,"Waiting after SaaS Operations...");
    }

    // Create Public Link...
    String createSharedLinkForFolder_revokeLink;
    @Test(groups={"REMEDIATION", "P1"},dependsOnMethods = "performFileUploadAndFolderCreationInSaasApp")
    public void createPublicLinkOfFileinSaasApp() throws Exception {
        Reporter.log("*********************TEST DESCRIPTION************************************",true);
        Reporter.log("================= DROPBOX SaaS Actions : CREATE PUBLIC LINK ============", true);
        Reporter.log("*************************************************************************",true);
        Reporter.log("Creating Public Link... for file :" + qaTestFolderInCloud + File.separator + uniqueFileName_quarantine, true);
        createSharedLinkForFolder_revokeLink = universalApi.createSharedLinkForFolder(qaTestFolderInCloud + File.separator + uniqueFileName_quarantine);
        Reporter.log("#### Shared Link before Revoke :" + createSharedLinkForFolder_revokeLink, true);
        Reporter.log("Creating Public Link... for file :" + qaTestFolderInCloud + File.separator + uniqueFileName_delete, true);
        String createSharedLinkForFolder1 = universalApi.createSharedLinkForFolder(qaTestFolderInCloud + File.separator + uniqueFileName_delete);
        Reporter.log("#### Shared Link :" + createSharedLinkForFolder1, true);
        String expectedResult="Created Shared Public Link shuld not be NULL ";
        String actualResult=createSharedLinkForFolder1;
        Reporter.log("Expected Result :"+expectedResult,true);
        Reporter.log("Actual Result / Shared Link :"+actualResult,true);
        Assert.assertNotNull(createSharedLinkForFolder1, "Link Not created....");
        securletsUtils.wait(120);
    }

    @Test(groups={"REMEDIATION", "P1"},dependsOnMethods = "createPublicLinkOfFileinSaasApp")
    public void performSharedLinkRevokeRemediationAndValidateInSaaSApp() throws UnsupportedEncodingException, Exception {
        String userId = dropboxBusinessAccActivities.getMemberId(user);
        Reporter.log("## Unique File Name To Be Quarantined :"+uniqueFileName_quarantine,true);
        String docId = getDocID(uniqueFileName_quarantine);
        String code = "SHARED_LINK_REVOKE";
        Reporter.log(" ## User ID:"+userId,true);
        Reporter.log(" ## Doc Id:"+docId,true);
        String generatePayLoadForDropboxRemediation = securletsUtils.generatePayLoadForDropboxRemediation(tenant, user, userId, docId, code);
        Reporter.log(" ## Generated Payload for Dropbox Remediation :\n" + generatePayLoadForDropboxRemediation);
        HttpEntity entity = new StringEntity(generatePayLoadForDropboxRemediation);
        HttpResponse doPatch = restClient.doPatch(new URI(urlRemediation), headers, null, entity);
        StringBuffer result1 = securletsUtils.getStringResponseFromHttpResponse(doPatch);
        String result_response1 = result1.toString();
        Reporter.log("Response :" + result_response1,true);
        Reporter.log("SHARED_LINK_REVOKE Remediation Applied.. ",true);
        securletsUtils.wait(120,"Waiting After SHARED_LINK_REVOKE");
        Reporter.log("Cross Check the SHARED_LINK_REVOKE remediation applied in Dropbox....",true);
        HttpResponse doGet = restClient.doGet(new URI(createSharedLinkForFolder_revokeLink), headers);
        String expectedResponse="We can&rsquo;t find what you&rsquo;re looking for";
        String response = ClientUtil.getResponseBody(doGet);
        Reporter.log("Response :"+response,true);
        Assert.assertTrue(response.contains(expectedResponse));
    }

    @Test(groups={"REMEDIATION", "P1"},dependsOnMethods = "createPublicLinkOfFileinSaasApp")
    public void performDeleteRemediationAndValidateInSaaSApp() throws UnsupportedEncodingException, Exception {
        Reporter.log("*********************TEST DESCRIPTION************************************",true);
        Reporter.log("Applying Delete Remediation and Validation in SaAS APP..",true);
        Reporter.log("*************************************************************************",true);
        String userId = dropboxBusinessAccActivities.getMemberId(user);
        Reporter.log("## Unique File Name To Be Deleted :"+uniqueFileName_delete,true);
        securletsUtils.wait(900," ## Waiting before Receiving Doc ID... for the file :"+uniqueFileName_delete);
        String docId = getDocID(uniqueFileName_delete);
        Reporter.log("Expected Result : Documented Id for file <"+ uniqueFileName_delete +">should not  be null");
        Reporter.log("Actual Result : Documented Id for file <"+ uniqueFileName_delete +"> :"+docId);
        org.testng.Assert.assertNotNull(docId,"Document ID for file >> "+uniqueFileName_delete +" is NULL");
        if(!(docId==null)){
        String code = "MOVE_TO_TRASH";
        Reporter.log(" ## User ID:"+userId,true);
        Reporter.log(" ## Doc Id:"+docId,true);
        String generatePayLoadForDropboxRemediation = securletsUtils.generatePayLoadForDropboxRemediation(tenant, user, userId, docId, code);
        Reporter.log(" ## Generated Payload for Dropbox Remediation :\n" + generatePayLoadForDropboxRemediation);
        HttpEntity entity = new StringEntity(generatePayLoadForDropboxRemediation);
        HttpResponse doPatch = restClient.doPatch(new URI(urlRemediation), headers, null, entity);
        StringBuffer result1 = securletsUtils.getStringResponseFromHttpResponse(doPatch);
        String result_response1 = result1.toString();
        Reporter.log("Response :" + result_response1,true);
        Reporter.log("Delete Remediation Applied.. ",true);
        securletsUtils.wait(delayBetweenSaaSActivity);
        Reporter.log("Cross Check the remediation applied in Dropbox....",true);
        Map foldersItems = universalApi.getFoldersItems(qaTestFolderInCloud, 0, 0);
        boolean containsKey = foldersItems.containsKey(uniqueFileName_delete);
        Reporter.log("Items in the folder :"+foldersItems.toString(),true);
        Reporter.log("Files in the Folder  :"+foldersItems.toString(),true);
        Reporter.log("## Expected Result: File Should Not be present after delete remediation",true);
        if(containsKey){
            Reporter.log("## Actual Result : File is Present after Delete remediation .. Remediation Failure...");
        }
        else{
            Reporter.log("## Actual Result : File is NOT Present after Delete remediation .. Remediation Success..");
        }
        Assert.assertTrue("File is Present after Delete remediation .. Remediation Failure...",!containsKey);
        }
    }
    
    public String getDocID(String fileName) throws Exception {
        Map<String, String> params = new HashMap();
        params.put("name", fileName);
        StringBuffer result = securletsUtils.getStringResponseFromHttpResponse(restClient.doGet(new URI(securletsUtils.getURI(urlDocInfo, params)), headers));
        String result_response = result.toString();
        Reporter.log("#### \n Result Response :" + result_response,true);
        String query = "$..identification";
        String docID = null;
        try{
        docID= RawJsonParser.fetchSingleField(result_response, query).toString();
        }
        catch(Exception e){
            Reporter.log("Issue Found in Scanning...Doc ID for the file to be remediated is found NULL",true);
        }
        return docID;
    }

}
