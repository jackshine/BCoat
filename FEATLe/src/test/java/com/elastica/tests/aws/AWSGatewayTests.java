package com.elastica.tests.aws;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.elastica.action.investigate.InvestigateAction;
import com.elastica.common.CommonTest;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;


public class AWSGatewayTests extends GWCommonTest{
	Map <String, String> data = new HashMap<String, String>();
	String fromTime=backend.getCurrentTime();
	String bucketName = null;
	String folderName = null;
	String fileName = null;
	String region = null;
	String regionName = null;
	String regionCode = null;
	File file = null;
	String ipAddress = null;

	@Parameters({"regionName", "regionCode"})
	@Test(groups={"Regression", "Sanity"}, priority=1)
	public void loginToPortal(String name, String code) throws Exception {
		regionName = name;
		regionCode = code;
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Thread.sleep(60000);
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Test(groups={"Regression", "Sanity", "Reach"}, priority=2)
	public void awsActivities() throws Exception{
		String timeStamp = salesforceHomeAction.getTimestamp();
		printCredentials();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "f"+timeStamp;
		//policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME, suiteData, backend.getHeaders(suiteData));
		//policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME, suiteData, backend.getHeaders(suiteData));
		awsAction.loginAWS(getWebDriver(), suiteData);
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = salesforceHomeAction.createFile(fileName, "txt");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		awsAction.shareFile(getWebDriver(), file.getName(), region);
		awsAction.openFile(getWebDriver(), file.getName());
		awsAction.deleteFile(getWebDriver(), file.getName(), region);  //This line is deleting the file, operation is same as deleting the folder
		awsAction.deleteFolder(getWebDriver(), folderName, suiteData);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		awsAction.logoutAWS(getWebDriver());
		salesforceHomeAction.deleteFile(file);
	}
	
	@Test(groups={"Regression", "Sanity", "Reach"}, priority=3)
	public void verifyCreateBucketMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User created a bucket "+bucketName+" in region "+regionCode;
		printCredentials();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Bucket");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.AWS_REGION, regionCode);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");

		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity", "Reach"}, priority=4)
	public void verifyDeleteBucketMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User deleted a bucket "+bucketName;
		printCredentials();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Bucket");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity", "Reach"}, priority=5)
	public void verifyCreateFolderMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User created a folder "+bucketName+"/"+folderName;
		printCredentials();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity", "Reach"}, priority=6)
	public void verifyDeleteFolderMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User deleted folder(s) "+folderName;
		printCredentials();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity", "Reach"}, priority=7)
	public void verifyUploadFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User uploaded file "+file.getName();
		printCredentials();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity", "Reach"}, priority=8)
	public void verifyShareFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User shared file "+file.getName()+" from bucket "+bucketName;
		printCredentials();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity", "Reach"}, priority=9)
	public void verifyDownloadFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User initiated download of file "+file.getName();
		printCredentials();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity", "Reach"}, priority=10)
	public void verifyDeleteFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User deleted file "+file.getName()+" from bucket "+bucketName;
		printCredentials();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
}