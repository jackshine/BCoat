package com.elastica.tests.aws;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.elastica.action.investigate.InvestigateAction;
import com.elastica.common.CommonTest;
import com.elastica.gateway.GatewayTestConstants;


public class AWSFileSizeTests extends CommonTest{
	Map <String, String> data = new HashMap<String, String>();
	String fromTime=backend.getCurrentTime();
	String bucketName = null;
	String folderName = null;
	String fileName = null;
	String region = null;
	String regionName = null;
	String regionCode = null;
	File file = null;

	@Parameters({"regionName", "regionCode"})
	@Test(groups={"Regression", "Sanity"}, priority=1)
	public void loginToPortal(String name, String code) throws Exception {
		regionName = name;
		regionCode = code;
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Thread.sleep(60000);
		awsAction.loginAWS(getWebDriver(), suiteData);
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Test(groups={"Regression", "Sanity"}, dataProvider = "FileSize", priority=2)
	public void aws100MBFileUpload() throws Exception{
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "100mb.zip";
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = new File("C:\\Files\\100mb.zip");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		awsAction.shareFile(getWebDriver(), file.getName(), region);
		awsAction.openFile(getWebDriver(), file.getName());
		awsAction.deleteFile(getWebDriver(), file.getName(), region);  //This line is deleting the file, operation is same as deleting the folder
		awsAction.deleteFolder(getWebDriver(), folderName, suiteData);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		awsAction.logoutAWS(getWebDriver());
		salesforceHomeAction.deleteFile(file);
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=3)
	public void verify100MBUploadFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User uploaded file "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=4)
	public void verify100MBShareFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User shared file "+file.getName()+" from bucket "+bucketName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=5)
	public void verify100MBDownloadFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User initiated download of file "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=6)
	public void verify100MBDeleteFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User deleted file "+file.getName()+" from bucket "+bucketName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=7)
	public void aws200MBFileUpload() throws Exception{
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "200mb.zip";
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = new File("C:\\Files\\200mb.zip");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		awsAction.shareFile(getWebDriver(), file.getName(), region);
		awsAction.openFile(getWebDriver(), file.getName());
		awsAction.deleteFile(getWebDriver(), file.getName(), region);  //This line is deleting the file, operation is same as deleting the folder
		awsAction.deleteFolder(getWebDriver(), folderName, suiteData);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		awsAction.logoutAWS(getWebDriver());
		salesforceHomeAction.deleteFile(file);
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=8)
	public void verify200MBUploadFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User uploaded file "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=9)
	public void verify200MBShareFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User shared file "+file.getName()+" from bucket "+bucketName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=10)
	public void verify200MBDownloadFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User initiated download of file "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=11)
	public void verify200MBDeleteFileMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "User deleted file "+file.getName()+" from bucket "+bucketName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/*@DataProvider(name="FileSize")
	public String[][] fileData(){
		return new String[][]{
			{"100mb.zip", "C:\\Files\\100mb.zip"},
			{"200mb.zip", "C:\\Files\\200mb.zip"}
		};
	}*/
}