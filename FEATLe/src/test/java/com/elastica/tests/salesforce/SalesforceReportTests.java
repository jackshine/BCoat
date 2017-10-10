package com.elastica.tests.salesforce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.elastica.action.investigate.InvestigateAction;
import com.elastica.common.CommonTest;
import com.elastica.constants.salesforce.SalesforceConstants;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;


public class SalesforceReportTests extends CommonTest{
	Map <String, String> data = new HashMap<String, String>();
	String fromTime=null;
	WebDriver driver = null;
	InvestigateAction investigateAction = new InvestigateAction();

	@Priority(1)
	@Test(groups ={"Regression", "Sanity"})
	public void performingActivitiesOnSaasAppo365() throws Exception {
		driver = getWebDriver();
		fromTime=backend.getCurrentTime();
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(driver, suiteData);
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Priority(2)
	@Test(groups ={"Regression", "Sanity", "Reach1"})
	public void salesforces_Test_TestLogin() throws Exception {
		data.clear();
		data.put("message", "User logged in");
		salesforceLogin.login(getWebDriver(), suiteData);
	}
	
/* ------------    Salesforce Files Tab Files Upload Start    ------------ */
	
	@Test(groups={"Files", "Reach1"}, priority=3)
	public void salesforceFilesTabFileActivities() throws InterruptedException, IOException{
		Reporter.log("Starting testcase: salesforceFilesTabFileActivities");
		fromTime=backend.getCurrentTime();
		filename = salesforceHomeAction.getTimestamp();
		Thread.sleep(1000*60);
		file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.uploadFileFromFilesTab(driver, file, suiteData);
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.viewAndDownloadFileFromFilesTab(driver, filename);
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.shareFileViaLinkFromFilesTab(driver, filename);
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.deleteFileFromFilesTab(driver, filename);
		Reporter.log("Completed testcase: salesforceFilesTabFileActivities");
	}
	
	@Test(groups={"Files", "Reach"}, priority=4)
	public void verifyUploadFileFilesTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String uploadFile = "User uploaded file named "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=5)
	public void verifyShareFileFilesTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String shareFile = "User shared a file named "+filename+" via public link";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, shareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=6)
	public void verifyViewFileFilesTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewFile = "User previewed a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=7)
	public void verifyDownloadFileFilesTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Files Tab Files Upload End    ------------ */
	
	/* ------------    Salesforce Home Tab Files Upload Start    ------------ */
	File file = null;
	String filename = null;
	@Test(groups={"Files", "Sanity", "Reach"}, priority=8)
	public void salesforceHomeTabFileActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceHomeTabFileActivities");
		fromTime=backend.getCurrentTime();
		driver.get(suiteData.getSaasAppBaseUrl());
		filename = salesforceHomeAction.getTimestamp();
		file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.viewFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.downloadFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.commentAccountChatterTab(driver, filename);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.deleteFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.deleteFile(file);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceHomeTabFileActivities");
	}
	
	@Test(groups={"Files", "Sanity", "Reach"}, priority=9)
	public void verifyUploadFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Sanity", "Reach"}, priority=10)
	public void verifyShareFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String ShareFile = "User shared file named "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		//HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, ShareFile);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, ShareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Sanity", "Reach"}, priority=11)
	public void verifyViewFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewFile = "User viewed File named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		//HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, viewFile);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Sanity", "Reach"}, priority=12)
	public void verifyDownloadFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Files", "Sanity", "Reach"}, priority=13)
	public void verifyCommentFileHomeMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String commentMessage = "User posted "+filename+" on original "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Comment");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Post");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, commentMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Home Tab Files Upload End    ------------ */
	
	/* ------------    Salesforce Chatter Tab Files Upload Start    ------------ */
	
	@Test(groups={"Files", "Reach"}, priority=14)
	public void salesforceChatterTabFileActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceChatterTabFileActivities");
		fromTime=backend.getCurrentTime();
		driver.get(suiteData.getSaasAppBaseUrl());
		filename = salesforceHomeAction.getTimestamp();
		file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.viewFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.downloadFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.deleteFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.deleteFile(file);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceChatterTabFileActivities");
	}
	
	@Test(groups={"Files", "Reach"}, priority=15)
	public void verifyUploadFileChatterTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=16)
	public void verifyShareFileChatterTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String ShareFile = "User shared file named "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, ShareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=17)
	public void verifyViewFileChatterTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewFile = "User viewed File named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=18)
	public void verifyDownloadFileChatterTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Chatter Tab Files Upload End    ------------ */
	/*
	 ------------    Salesforce Account Tab Files Upload Start    ------------ 
	
	@Test(groups={"Files1"})
	public void salesforceAccountsTabFileActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceAccountsTabFileActivities");
		fromTime=backend.getCurrentTime();
		driver.get(suiteData.getSaasAppBaseUrl());
		filename = salesforceHomeAction.getTimestamp();
		file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoAccountsTab(driver);
		salesforceHomeAction.uploadFileFromAccountsTab(driver, file);
		salesforceHomeAction.deleteFile(file);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceAccountsTabFileActivities");
	}
	
	@Test(groups={"Files1"})
	public void verifyUploadFileAccountsTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"})
	public void verifyShareFileAccountsTabMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String shareFile = "User shared a file named "+file.getName()+" via public link";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, shareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"})
	public void verifyViewFileAccountsTabMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewFile = "User viewed File named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"})
	public void verifyDownloadFileAccountsTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	 ------------    Salesforce Account Tab Files Upload End    ------------ 
	
	 ------------    Salesforce Contacts Tab Files Upload Start    ------------ 
	
	@Test(groups={"Files1"})
	public void salesforceContactsTabFileActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceContactsTabFileActivities");
		fromTime=backend.getCurrentTime();
		driver.get(suiteData.getSaasAppBaseUrl());
		filename = salesforceHomeAction.getTimestamp();
		file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoContactsTab(driver);
		salesforceHomeAction.uploadFileFromAccountsTab(driver, file);
		salesforceHomeAction.deleteFile(file);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceContactsTabFileActivities");
	}
	
	@Test(groups={"Files1"})
	public void verifyUploadFileContactsTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"})
	public void verifyShareFileContactsTabMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String shareFile = "User shared a file named "+file.getName()+" via public link";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, shareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"})
	public void verifyViewFileContactsTabMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewFile = "User viewed File named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"})
	public void verifyDownloadFileContactsTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	 ------------    Salesforce Contacts Tab Files Upload End    ------------ 
	
	 ------------    Salesforce Leads Tab Files Upload Start    ------------ 
	
	@Test(groups={"Files1"})
	public void salesforceLeadsTabFileActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceLeadsTabFileActivities");
		fromTime=backend.getCurrentTime();
		driver.get(suiteData.getSaasAppBaseUrl());
		filename = salesforceHomeAction.getTimestamp();
		file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoLeadsTab(driver);
		salesforceHomeAction.uploadFileFromAccountsTab(driver, file);
		salesforceHomeAction.deleteFile(file);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceLeadsTabFileActivities");
	}
	
	@Test(groups={"Files1"})
	public void verifyUploadFileLeadsTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"})
	public void verifyShareFileLeadsTabMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String shareFile = "User shared a file named "+file.getName()+" via public link";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, shareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"})
	public void verifyViewFileLeadsTabMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewFile = "User viewed File named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"}, priority=32)
	public void verifyDownloadFileLeadsTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	 ------------    Salesforce Leads Tab Files Upload End    ------------ 
	
	 ------------    Salesforce Opportunities Tab Files Upload Start    ------------ 
	
	@Test(groups={"Files1"}, priority=33)
	public void salesforceOpportunitiesTabFileActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceOpportunitiesTabFileActivities");
		fromTime=backend.getCurrentTime();
		driver.get(suiteData.getSaasAppBaseUrl());
		filename = salesforceHomeAction.getTimestamp();
		file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoOpportunityTab(driver);
		salesforceHomeAction.uploadFileFromAccountsTab(driver, file);
		salesforceHomeAction.deleteFile(file);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceOpportunitiesTabFileActivities");
	}
	
	@Test(groups={"Files1"}, priority=34)
	public void verifyUploadFileOpportunitiesTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"}, priority=35)
	public void verifyShareFileOpportunitiesTabMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String shareFile = "User shared a file named "+file.getName()+" via public link";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, shareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"}, priority=36)
	public void verifyViewFileOpportunitiesTabMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewFile = "User viewed File named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files1"}, priority=37)
	public void verifyDownloadFileOpportunitiesTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	 ------------    Salesforce Opportunities Tab Files Upload End    ------------ 
	*/
	/* ------------    Salesforce Opportunities Tab Files Upload (Upload docx) Start    ------------ */
	
	@Test(groups={"Files", "Reach"}, priority=19)
	public void salesforceHomeTabDocxFileActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceOpportunitiesTabFileActivities");
		fromTime=backend.getCurrentTime();
		driver.get(suiteData.getSaasAppBaseUrl());
		filename = salesforceHomeAction.getTimestamp();
		file = salesforceHomeAction.createFile(filename, "docx");
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.viewFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.downloadFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.commentAccountChatterTab(driver, filename);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.deleteFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.deleteFile(file);
		Thread.sleep(1000*30);
		Reporter.log("Completed testcase: salesforceOpportunitiesTabFileActivities");
	}
	
	@Test(groups={"Files", "Reach"}, priority=20)
	public void verifyUploadDocxFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=21)
	public void verifyShareDocxFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String shareFile = "User shared file named "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, shareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=22)
	public void verifyViewDocxFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewFile = "User viewed File named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=23)
	public void verifyDownloadDocxFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	 
	/* ------------    Salesforce Opportunities Tab Files Upload (Upload docx) End    ------------ */
	
	/* ------------    Salesforce Opportunities Tab Files Upload (Upload xlsx) Start    ------------ */
	
	@Test(groups={"Files", "Reach"}, priority=24)
	public void salesforceHomeTabXlsxFileActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceOpportunitiesTabFileActivities");
		fromTime=backend.getCurrentTime();
		driver.get(suiteData.getSaasAppBaseUrl());
		filename = salesforceHomeAction.getTimestamp();
		file = salesforceHomeAction.createFile(filename, "xlsx");
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.viewFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.downloadFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.commentAccountChatterTab(driver, filename);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.deleteFileFromHomeChatterTab(driver, filename);
		salesforceHomeAction.deleteFile(file);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceOpportunitiesTabFileActivities");
	}
	
	@Test(groups={"Files", "Reach"}, priority=25)
	public void verifyUploadXlsxFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=26)
	public void verifyShareXlsxFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String shareFile = "User shared file named "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, shareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=27)
	public void verifyViewXlsxFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewFile = "User viewed File named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Files", "Reach"}, priority=28)
	public void verifyDownloadXlsxFileHomeTabMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	 
	/* ------------    Salesforce Opportunities Tab Files Upload (Upload docx) End    ------------ */
	
	/* ------------    Salesforce Reports Start    ------------ */
	Map<String, String> reportDetails = new HashMap<String, String>();
	String reportName = null;
	
	@Test(groups ={"Report", "Reach"}, priority=48)
	public void salesforceReportsActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceReportsActivities");
		fromTime=backend.getCurrentTime();
		String timestamp = salesforceHomeAction.getTimestamp();
		reportName = "Report"+timestamp;
		reportDetails.put(SalesforceConstants.REPORT_NAME, reportName);
		reportDetails.put(SalesforceConstants.REPORT_DESCRIPTION, "Report Desc");
		reportDetails.put(SalesforceConstants.REPORT_TYPE, "File and Content Report");
		salesforceHomeAction.createReport(driver, reportDetails);
		reportDetails.put(SalesforceConstants.REPORT_DESCRIPTION_EDIT, "Report Desc Edit");
		salesforceHomeAction.editReport(driver, reportDetails);
		salesforceHomeAction.runReport(driver, reportDetails);
		salesforceHomeAction.exportReport(driver, reportDetails);
		salesforceHomeAction.deleteReport(driver, reportDetails);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceReportsActivities");
	}

	@Test(groups ={"Report", "Reach"}, priority=49)
	public void verifyCreateReportMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createMessage = "User created a new report Unsaved Report";
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Report");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Report", "Reach"}, priority=50)
	public void verifyEditReportMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editMessage = "User edited report "+reportName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Report");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Report", "Reach"}, priority=51)
	public void verifyViewReportMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewMessage = "User viewed report named "+reportName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Report");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Report", "Reach"}, priority=52)
	public void verifyRunReportMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String runMessage = "User ran a report named "+reportName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, runMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Report");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Run");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Report", "Reach"}, priority=53)
	public void verifyExportReportMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String exportMessage = "User downloaded a report "+reportName+" " + "as xls";
		expectedDataMap.put(GatewayTestConstants.MESSAGE, exportMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Report");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Export");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Report", "Reach"}, priority=54)
	public void verifyDeleteReportMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteMessage = "User deleted a report named "+reportName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Report");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Reports End    ------------ */
	
	/* ------------    Salesforce Accounts Start    ------------ */
	
	String accountName = null;
	
	@Test(groups ={"Account", "Reach"}, priority=55)
	public void salesforceAccountActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceAccountActivities");
		fromTime=backend.getCurrentTime();
		accountName = "Account"+salesforceHomeAction.getTimestamp();
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.gotoAccountsTab(driver);
		salesforceHomeAction.createAccountOnAccountsPage(driver, accountDetails);
		salesforceHomeAction.gotoAccountsTab(driver);
		salesforceHomeAction.editAccountInSalesforce(driver, accountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		salesforceHomeAction.gotoAccountsTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceAccountActivities");
	}

	@Test(groups ={"Account", "Reach"}, priority=56)
	public void verifyCreateAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createMessage = "User created a new account with name "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account", "Reach"}, priority=57)
	public void verifyViewAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewMessage = "User viewed Account named "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account", "Reach"}, priority=58)
	public void verifyEditAccountMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editMessage = "User edited an account named "+accountName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account", "Reach"}, priority=59)
	public void verifyDeleteAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteMessage = "User deleted the account "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Accounts End    ------------ */
	
	/* ------------    Salesforce Home Tab Accounts Start    ------------ */
	
	@Test(groups ={"Account1"}, priority=60)
	public void salesforceHomeTabAccountActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceHomeTabAccountActivities");
		accountName = null;
		driver = getWebDriver();
		accountName = "Account"+salesforceHomeAction.getTimestamp();
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Account");
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnHomePage(driver, accountDetails);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.editAccountInSalesforce(driver, accountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceHomeTabAccountActivities");
	}

	@Test(groups ={"Account1"}, priority=61)
	public void verifyCreateHomeAccountMessage() throws Exception{
		String createMessage = "User created a new account with name "+accountName;
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account1"}, priority=62)
	public void verifyViewHomeAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewMessage = "User viewed Account named "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account1"}, priority=63)
	public void verifyEditHomeAccountMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editMessage = "User edited an account named "+accountName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants._DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account1"}, priority=64)
	public void verifyDeleteHomeAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteMessage = "User deleted the account "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Home Tab Accounts End    ------------ */
	
	/* ------------    Salesforce Chatter Tab Accounts Start    ------------ */
	
	@Test(groups ={"Account1"}, priority=65)
	public void salesforceChatterTabAccountActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceChatterTabAccountActivities");
		accountName = null;
		driver = getWebDriver();
		driver.get(suiteData.getSaasAppBaseUrl());
		accountName = "Account"+salesforceHomeAction.getTimestamp();
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Account");
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnHomePage(driver, accountDetails);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.editAccountInSalesforce(driver, accountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.commentAccountChatterTab(driver, accountName);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceChatterTabAccountActivities");
	}
	
	@Test(groups ={"Account1"}, priority=66)
	public void verifyCreateChatterAccountMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createMessage = "User created a new account with name "+accountName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account1"}, priority=67)
	public void verifyViewChatterAccountMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewMessage = "User viewed Account named "+accountName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account1"}, priority=68)
	public void verifyEditChatterAccountMessage() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editMessage = "User edited an account named "+accountName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account1"}, priority=69)
	public void verifyDeleteChatterAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteMessage = "User deleted the account "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Account1"}, priority=70)
	public void verifyCommentChatterAccountMessage1() throws Exception{
		HashMap<String, Object> expectedDataMap = new HashMap<String, Object>();
		String commentMessage = "User posted "+accountName+" on original "+accountName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Comment");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Post");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, commentMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Chatter Tab Accounts End    ------------ */
	
	/* ------------    Salesforce Home Tab Opportunity Start    ------------ */
	
	String opportunityName = null;
	String opportunityAccountName = null;
	String closeDate = null;
	@Test(groups ={"Opportunity1"}, priority=71)
	public void salesforceHomeTabOpportunityActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceHomeTabAccountActivities");
		driver = getWebDriver();
		//driver.get(suiteData.getSaasAppBaseUrl());
		String timestamp = salesforceHomeAction.getTimestamp();
		opportunityName = "Opportunity"+ timestamp;
		opportunityAccountName = "OppoAccount" + timestamp;
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Account");
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, opportunityAccountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnHomePage(driver, accountDetails);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Opportunity");
		Map<String, String> opportunityDetails = new HashMap<String, String>();
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_NAME, opportunityName);
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_ACCOUNT_NAME, opportunityAccountName);
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_AMOUNT, "100.00");
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_STAGE, SalesforceConstants.QUALIFICATION);
		closeDate = salesforceHomeAction.createOpportunity(driver, opportunityDetails);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.editOpportunity(driver, opportunityDetails);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.editAccountInSalesforce(driver, opportunityAccountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, opportunityName);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, opportunityAccountName);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceHomeTabAccountActivities");
	}

	@Test(groups ={"Opportunity1"}, priority=72)
	public void verifyCreateHomeOpportunityAccountMessage() throws Exception{
		List<String> objects = new ArrayList<String>();
		objects.add("Account");
		driver.get(suiteData.getBaseUrl());
		investigateAction.gotoInvestigatePage(driver);
		Thread.sleep(1000*15);
		investigateAction.clickFilter(driver);
		investigateAction.choiceApp(driver, SalesforceConstants.SALESFORCE);
		investigateAction.selectObjects(driver, objects);
		investigateAction.checkGateway(driver);
		String createMessage = "User created a new account with name \""+opportunityAccountName+"\"";
		String actualMessage = investigateAction.clickActivityLog(driver, createMessage);
		driver.get(suiteData.getSaasAppBaseUrl());
		Assert.assertEquals(actualMessage, createMessage);
	}
	
	@Test(groups ={"Opportunity1"}, priority=73)
	public void verifyViewHomeOpportunityAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewMessage = "User viewed Account named "+opportunityAccountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity1"}, priority=74)
	public void verifyEditHomeOpportunityAccountMessage() throws Exception{
		HashMap<String, String> expectedDataMap = new HashMap<String, String>();
		String editMessage = "User edited an account named \\\""+opportunityAccountName+"\\\"";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, editMessage);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants._DOMAIN, suiteData.getTenantDomainName());
		salesforceHomeAction.verifySalesforceParameters(actualResult, expectedDataMap);
	}
	
	@Test(groups ={"Opportunity1"}, priority=75)
	public void verifyDeleteHomeOpportunityAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteMessage = "User deleted the account "+opportunityAccountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity1"}, priority=76)
	public void verifyCreateHomeOpportunity() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createOpportunityMessage = "User created a new opportunity with name "+opportunityName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createOpportunityMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity1"}, priority=77)
	public void verifyEditHomeOpportunity() throws Exception{
		HashMap<String, String> expectedDataMap = new HashMap<String, String>();
		String editMessage = "User edited an opportunity \\\""+opportunityName+"\\\", amount \\\"100.00\\\", close date \\\""+closeDate+"\\\"";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, editMessage);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants._DOMAIN, suiteData.getTenantDomainName());
		salesforceHomeAction.verifySalesforceParameters(actualResult, expectedDataMap);
	}
	
	@Test(groups ={"Opportunity1"}, priority=78)
	public void verifyViewHomeOpportunity() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewOpportunityMessage = "User viewed Opportunity named "+opportunityName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewOpportunityMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity1"}, priority=79)
	public void verifyDeleteHomeOpportunity() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteOppotunityMessage = "User deleted the opportunity "+opportunityName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteOppotunityMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Home Tab Opportunity End    ------------ */
	
	/* ------------    Salesforce Chatter Tab Opportunity Start    ------------ */
	
	@Test(groups ={"Opportunity1"}, priority=80)
	public void salesforceChatterTabOpportunityActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceHomeTabAccountActivities");
		driver = getWebDriver();
		String timestamp = salesforceHomeAction.getTimestamp();
		opportunityName = "Opportunity"+ timestamp;
		opportunityAccountName = "OppoAccount" + timestamp;
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Account");
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, opportunityAccountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnHomePage(driver, accountDetails);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Opportunity");
		Map<String, String> opportunityDetails = new HashMap<String, String>();
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_NAME, opportunityName);
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_ACCOUNT_NAME, opportunityAccountName);
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_AMOUNT, "100.00");
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_STAGE, SalesforceConstants.QUALIFICATION);
		closeDate = salesforceHomeAction.createOpportunity(driver, opportunityDetails);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.editOpportunity(driver, opportunityDetails);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.editAccountInSalesforce(driver, opportunityAccountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, opportunityName);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, opportunityAccountName);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceHomeTabAccountActivities");
	}

	@Test(groups ={"Opportunity1"}, priority=81)
	public void verifyCreateChatterOpportunityAccountMessage() throws Exception{
		List<String> objects = new ArrayList<String>();
		Thread.sleep(1000*60);
		objects.add("Account");
		driver.get(suiteData.getBaseUrl());
		investigateAction.gotoInvestigatePage(driver);
		Thread.sleep(1000*15);
		investigateAction.clickFilter(driver);
		investigateAction.choiceApp(driver, SalesforceConstants.SALESFORCE);
		investigateAction.selectObjects(driver, objects);
		investigateAction.checkGateway(driver);
		String createMessage = "User created a new account with name \""+opportunityAccountName+"\"";
		String actualMessage = investigateAction.clickActivityLog(driver, createMessage);
		driver.get(suiteData.getSaasAppBaseUrl());
		Assert.assertEquals(actualMessage, createMessage);
	}
	
	@Test(groups ={"Opportunity1"}, priority=82)
	public void verifyViewChatterOpportunityAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewMessage = "User viewed Account named "+opportunityAccountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity1"}, priority=83)
	public void verifyEditChatterOpportunityAccountMessage() throws Exception{
		HashMap<String, String> expectedDataMap = new HashMap<String, String>();
		String editMessage = "User edited an account named \\\""+opportunityAccountName+"\\\"";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, editMessage);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants._DOMAIN, suiteData.getTenantDomainName());
		salesforceHomeAction.verifySalesforceParameters(actualResult, expectedDataMap);
	}
	
	@Test(groups ={"Opportunity1"}, priority=84)
	public void verifyDeleteChatterOpportunityAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteMessage = "User deleted the account "+opportunityAccountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity1"}, priority=85)
	public void verifyCreateChatterOpportunityMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createOpportunity = "User created a new opportunity with name "+opportunityName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createOpportunity);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity1"}, priority=86)
	public void verifyEditChatterOpportunityMessage() throws Exception{
		HashMap<String, String> expectedDataMap = new HashMap<String, String>();
		String editMessage = "User edited an opportunity \\\""+opportunityName+"\\\", amount \\\"100.00\\\", close date \\\""+closeDate+"\\\"";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, editMessage);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants._DOMAIN, suiteData.getTenantDomainName());
		salesforceHomeAction.verifySalesforceParameters(actualResult, expectedDataMap);
	}
	
	@Test(groups ={"Opportunity1"}, priority=87)
	public void verifyViewChatterOpportunityMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewOpportunity = "User viewed Opportunity named "+opportunityName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewOpportunity);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity1"}, priority=88)
	public void verifyDeleteChatterOpportunityMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteOppotunity = "User deleted the opportunity "+opportunityName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteOppotunity);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Chatter Tab Opportunity End    ------------ */
	
	/* ------------    Salesforce Account Opportunity Start    ------------ */
	
	@Test(groups ={"Opportunity", "Reach"}, priority=89)
	public void salesforceOpportunityTabAccountActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceOpportunityTabAccountActivities");
		driver = getWebDriver();
		//driver.get(suiteData.getSaasAppBaseUrl());
		opportunityAccountName = "Account"+salesforceHomeAction.getTimestamp();
		opportunityName = "Opportunity"+salesforceHomeAction.getTimestamp();
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, opportunityAccountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.gotoAccountsTab(driver);
		salesforceHomeAction.createAccountOnAccountsPage(driver, accountDetails);
		salesforceHomeAction.gotoAccountsTab(driver);
		salesforceHomeAction.editAccountInSalesforce(driver, opportunityAccountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		salesforceHomeAction.gotoOpportunityTab(driver);
		salesforceHomeAction.clickCreateNewAccount(driver);
		Map<String, String> opportunityDetails = new HashMap<String, String>();
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_NAME, opportunityName);
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_ACCOUNT_NAME, opportunityAccountName);
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_AMOUNT, "100.00");
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_DESCRIPTION, "Opportunity Desc");
		opportunityDetails.put(SalesforceConstants.OPPORTUNITY_STAGE, SalesforceConstants.QUALIFICATION);
		closeDate = salesforceHomeAction.createOpportunity1(driver, opportunityDetails);
		salesforceHomeAction.gotoOpportunityTab(driver);
		salesforceHomeAction.editOpportunity(driver, opportunityDetails);
		salesforceHomeAction.gotoOpportunityTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, opportunityName);
		salesforceHomeAction.gotoAccountsTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, opportunityAccountName);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceOpportunityTabAccountActivities");
	}

	@Test(groups ={"Opportunity", "Reach"}, priority=90)
	public void verifyCreateOpportunityAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createMessage = "User created a new account with name "+opportunityAccountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity", "Reach"}, priority=91)
	public void verifyViewOpportunityAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewMessage = "User viewed Account named "+opportunityAccountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity", "Reach"}, priority=92)
	public void verifyEditOpportunityAccountMessage() throws Exception{
		HashMap<String, String> expectedDataMap = new HashMap<String, String>();
		String editMessage = "User edited an account named \\\""+opportunityAccountName+"\\\"";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, editMessage);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants._DOMAIN, suiteData.getTenantDomainName());
		salesforceHomeAction.verifySalesforceParameters(actualResult, expectedDataMap);
	}
	
	@Test(groups ={"Opportunity", "Reach"}, priority=93)
	public void verifyDeleteOpportunityAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteMessage = "User deleted the account "+opportunityAccountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity", "Reach"}, priority=94)
	public void verifyCreateOpportunityMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createOpportunity = "User created a new opportunity with name "+opportunityName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createOpportunity);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity", "Reach"}, priority=95)
	public void verifyEditOpportunityMessage() throws Exception{
		HashMap<String, String> expectedDataMap = new HashMap<String, String>();
		String editMessage = "User edited an opportunity \\\""+opportunityName+"\\\", amount \\\"100.00\\\", close date \\\""+closeDate+"\\\"";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, editMessage);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants._DOMAIN, suiteData.getTenantDomainName());
		salesforceHomeAction.verifySalesforceParameters(actualResult, expectedDataMap);
	}
	
	@Test(groups ={"Opportunity", "Reach"}, priority=96)
	public void verifyViewOpportunityMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewOpportunity = "User viewed Opportunity named "+opportunityName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewOpportunity);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Opportunity", "Reach"}, priority=97)
	public void verifyDeleteOpportunityMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteOppotunity = "User deleted the opportunity "+opportunityName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteOppotunity);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Opportunity");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Account Opportunity End    ------------ */
	
	/* ------------    Salesforce Leads Start    ------------ */
	
	Map<String, String> leadDetails = new HashMap<String, String>();
	@Test(groups ={"Lead", "Reach"}, priority=98)
	public void salesforceLeadsActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceLeadsActivities");
		driver = getWebDriver();
		String timestamp = salesforceHomeAction.getTimestamp();
		salesforceHomeAction.gotoLeadsTab(driver);
		salesforceHomeAction.clickCreateNewAccount(driver);
		leadDetails.put(SalesforceConstants.SALUTATION, "Mr.");
		leadDetails.put(SalesforceConstants.FIRST_NAME, "FN");
		leadDetails.put(SalesforceConstants.MIDDLE_NAME, "MN");
		leadDetails.put(SalesforceConstants.LAST_NAME, "LN");
		leadDetails.put(SalesforceConstants.SUFFIX, timestamp);
		leadDetails.put(SalesforceConstants.EMAIL, "fn.ln@email.com");
		leadDetails.put(SalesforceConstants.PHONE, "1553287340");
		leadDetails.put(SalesforceConstants.TITLE, "Title");
		leadDetails.put(SalesforceConstants.COMPANY, "ABC Company");
		salesforceHomeAction.editAndCreateLeads(driver, leadDetails, "no");
		salesforceHomeAction.gotoLeadsTab(driver);
		leadDetails.put(SalesforceConstants.COMPANY, "XYZ Company");
		salesforceHomeAction.editAndCreateLeads(driver, leadDetails, "yes");
		salesforceHomeAction.gotoLeadsTab(driver);
		String leadName = leadDetails.get(SalesforceConstants.LAST_NAME)+", "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+",  "+leadDetails.get(SalesforceConstants.SUFFIX);
		salesforceHomeAction.deleteAccountInSalesForce(driver, leadName);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceLeadsActivities");
	}

	@Test(groups ={"Lead", "Reach"}, priority=99)
	public void verifyCreateLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createdName = leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME);
		String createMessage = "User created a new lead with name "+createdName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Lead", "Reach"}, priority=100)
	public void verifyViewLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewName =  leadDetails.get(SalesforceConstants.SALUTATION)+" "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX);
		String viewMessage = "User viewed Lead named "+viewName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Lead", "Reach"}, priority=101)
	public void verifyEditLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editName = leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME);
		String editMessage = "User edited a lead "+editName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Lead", "Reach"}, priority=102)
	public void verifyDeleteLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteName = leadDetails.get(SalesforceConstants.SALUTATION)+" "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX);
		String deleteMessage = "User deleted the lead "+deleteName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Leads End    ------------ */
	
	/* ------------    Salesforce Home Tab Leads Start    ------------ */
	
	@Test(groups ={"Lead", "Reach"}, priority=103)
	public void salesforceHomeTabLeadActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceHomeTabLeadActivities");
		driver = getWebDriver();
		String timestamp = salesforceHomeAction.getTimestamp();
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Lead");
		leadDetails.put(SalesforceConstants.SALUTATION, "Mr.");
		leadDetails.put(SalesforceConstants.FIRST_NAME, "FN");
		leadDetails.put(SalesforceConstants.MIDDLE_NAME, "MN");
		leadDetails.put(SalesforceConstants.LAST_NAME, "LN");
		leadDetails.put(SalesforceConstants.SUFFIX, timestamp);
		leadDetails.put(SalesforceConstants.EMAIL, "fn.ln@email.com");
		leadDetails.put(SalesforceConstants.PHONE, "1553287340");
		leadDetails.put(SalesforceConstants.TITLE, "Title");
		leadDetails.put(SalesforceConstants.COMPANY, "ABC Company");
		salesforceHomeAction.createLeads(driver, leadDetails);
		salesforceHomeAction.gotoHomeTab(driver);
		leadDetails.put(SalesforceConstants.PHONE, "3551287340");
		salesforceHomeAction.editAndCreateLeads(driver, leadDetails, "yes");
		salesforceHomeAction.gotoHomeTab(driver);
		String leadName = leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX)+", "+leadDetails.get(SalesforceConstants.COMPANY);
		salesforceHomeAction.deleteAccountInSalesForce(driver, leadName);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceHomeTabLeadActivities");
	}

	@Test(groups ={"Lead", "Reach"}, priority=104)
	public void verifyCreateHomeLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createdName = leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME);
		String createMessage = "User created a new lead with name "+createdName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Lead", "Reach"}, priority=105)
	public void verifyViewHomeLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewName =  leadDetails.get(SalesforceConstants.SALUTATION)+" "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX);
		String viewMessage = "User viewed Lead named "+viewName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Lead", "Reach"}, priority=106)
	public void verifyEditHomeLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editName = leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME);
		String editMessage = "User edited a lead "+editName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Lead", "Reach"}, priority=107)
	public void verifyDeleteHomeLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteName = leadDetails.get(SalesforceConstants.SALUTATION)+" "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX);
		String deleteMessage = "User deleted the lead "+deleteName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Home Tab Leads End    ------------ */
	
	/* ------------    Salesforce Chatter Tab Leads Start    ------------ */
	
	@Test(groups ={"Lead", "Reach"}, priority=108)
	public void salesforceChatterTabLeadActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceChatterTabLeadActivities");
		driver = getWebDriver();
		String timestamp = salesforceHomeAction.getTimestamp();
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Lead");
		leadDetails.put(SalesforceConstants.SALUTATION, "Mr.");
		leadDetails.put(SalesforceConstants.FIRST_NAME, "FN");
		leadDetails.put(SalesforceConstants.MIDDLE_NAME, "MN");
		leadDetails.put(SalesforceConstants.LAST_NAME, "LN");
		leadDetails.put(SalesforceConstants.SUFFIX, timestamp);
		leadDetails.put(SalesforceConstants.EMAIL, "fn.ln@email.com");
		leadDetails.put(SalesforceConstants.PHONE, "1553287340");
		leadDetails.put(SalesforceConstants.TITLE, "Title");
		leadDetails.put(SalesforceConstants.COMPANY, "ABC Company");
		salesforceHomeAction.createLeads(driver, leadDetails);
		salesforceHomeAction.gotoChatterTab(driver);
		leadDetails.put(SalesforceConstants.PHONE, "3551287340");
		salesforceHomeAction.editAndCreateLeads(driver, leadDetails, "yes");
		salesforceHomeAction.gotoChatterTab(driver);
		String leadName = leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX)+", "+leadDetails.get(SalesforceConstants.COMPANY);
		salesforceHomeAction.deleteAccountInSalesForce(driver, leadName);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceChatterTabLeadActivities");
	}

	@Test(groups ={"Lead", "Reach"}, priority=109)
	public void verifyCreateChatterLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createdName = leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME);
		String createMessage = "User created a new lead with name "+createdName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Lead", "Reach"}, priority=110)
	public void verifyViewChatterLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewName =  leadDetails.get(SalesforceConstants.SALUTATION)+" "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX);
		String viewMessage = "User viewed Lead named "+viewName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Lead", "Reach"}, priority=111)
	public void verifyEditChatterLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editName = leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME);
		String editMessage = "User edited a lead "+editName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Lead", "Reach"}, priority=112)
	public void verifyDeleteChatterLeadMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteName = leadDetails.get(SalesforceConstants.SALUTATION)+" "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX);
		String deleteMessage = "User deleted the lead "+deleteName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Lead");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Chatter Tab Leads End    ------------ */
	
	/* ------------    Salesforce Contacts Start    ------------ */
	
	Map<String, String> contactDetails = new HashMap<String, String>();
	String contactName = null;
	String contactFullName = null; 
	String contactNameSuffix = null;
	String contactNameSuffixSalutation = null;
	
	@Test(groups ={"Contact", "Reach"}, priority=113)
	public void salesforceContactActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceContactActivities");
		driver = getWebDriver();
		String timestamp = salesforceHomeAction.getTimestamp();
		accountName = "Account" + timestamp;
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.gotoAccountsTab(driver);
		salesforceHomeAction.createAccountOnAccountsPage(driver, accountDetails);
		contactDetails.put(SalesforceConstants.SALUTATION, "Mr.");
		contactDetails.put(SalesforceConstants.FIRST_NAME, "FN");
		contactDetails.put(SalesforceConstants.MIDDLE_NAME, "MN");
		contactDetails.put(SalesforceConstants.LAST_NAME, "LN");
		contactDetails.put(SalesforceConstants.SUFFIX, timestamp);
		contactDetails.put(SalesforceConstants.EMAIL, "fn.ln@email.com");
		contactDetails.put(SalesforceConstants.PHONE, "1553287340");
		contactDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		contactDetails.put(SalesforceConstants.TITLE, "Title");
		contactDetails.put(SalesforceConstants.MOBILE, "9834512388");
		salesforceHomeAction.gotoContactsTab(driver);
		salesforceHomeAction.editAndCreateContact(driver, contactDetails, "no");
		contactDetails.put(SalesforceConstants.MOBILE, "7434512388");
		salesforceHomeAction.gotoContactsTab(driver);
		salesforceHomeAction.editAndCreateContact(driver, contactDetails, "yes");
		salesforceHomeAction.gotoContactsTab(driver);
		salesforceHomeAction.deleteContact(driver, contactDetails);
		salesforceHomeAction.gotoAccountsTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		contactName = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME);
		contactFullName = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME);
		contactNameSuffixSalutation = contactDetails.get(SalesforceConstants.SALUTATION)+" "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME)+" "+contactDetails.get(SalesforceConstants.SUFFIX);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceContactActivities");
	}

	@Test(groups ={"Contact", "Reach"}, priority=114)
	public void verifyCreateContact() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createContact = "User created a new contact with name "+contactName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact", "Reach"}, priority=115)
	public void verifyEditContact() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editContact = "User edited  contact "+contactName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact", "Reach"}, priority=116)
	public void verifyViewContact() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewContact = "User viewed Contact named "+contactNameSuffixSalutation;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact", "Reach"}, priority=117)
	public void verifyDeleteContact() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteContact = "User deleted the contact "+contactNameSuffixSalutation;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Contacts End    ------------ */
	
	/* ------------    Salesforce Home Tab Contacts Start    ------------ */
	
	@Test(groups ={"Contact1"}, priority=118)
	public void salesforceHomeTabContactActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceHomeTabContactActivities");
		driver = getWebDriver();
		String timestamp = salesforceHomeAction.getTimestamp();
		accountName = "Account" + timestamp;
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Account");
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnHomePage(driver, accountDetails);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.editAccountInSalesforce(driver, accountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Contact");
		contactDetails.put(SalesforceConstants.SALUTATION, "Mr.");
		contactDetails.put(SalesforceConstants.FIRST_NAME, "FN");
		contactDetails.put(SalesforceConstants.MIDDLE_NAME, "MN");
		contactDetails.put(SalesforceConstants.LAST_NAME, "LN");
		contactDetails.put(SalesforceConstants.SUFFIX, timestamp);
		contactDetails.put(SalesforceConstants.EMAIL, "fn.ln@email.com");
		contactDetails.put(SalesforceConstants.PHONE, "1553287340");
		contactDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		contactDetails.put(SalesforceConstants.TITLE, "Title");
		salesforceHomeAction.createContact(driver, contactDetails);
		salesforceHomeAction.gotoHomeTab(driver);
		contactDetails.put(SalesforceConstants.MOBILE, "9834512388");
		salesforceHomeAction.editAndCreateContact(driver, contactDetails, "yes");
		salesforceHomeAction.gotoHomeTab(driver);
		contactNameSuffix = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME)+" "+contactDetails.get(SalesforceConstants.SUFFIX);
		salesforceHomeAction.deleteAccountInSalesForce(driver, contactNameSuffix);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		contactName = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME);
		contactFullName = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME);
		contactNameSuffixSalutation = contactDetails.get(SalesforceConstants.SALUTATION)+" "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME)+" "+contactDetails.get(SalesforceConstants.SUFFIX);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceHomeTabContactActivities");
	}

	@Test(groups ={"Contact1"}, priority=119)
	public void verifyCreateHomeContactAccountMessage() throws Exception{
		List<String> objects = new ArrayList<String>();
		Thread.sleep(1000*60);
		objects.add("Account");
		driver.get(suiteData.getBaseUrl());
		investigateAction.gotoInvestigatePage(driver);
		Thread.sleep(1000*15);
		investigateAction.clickFilter(driver);
		investigateAction.choiceApp(driver, SalesforceConstants.SALESFORCE);
		investigateAction.selectObjects(driver, objects);
		investigateAction.checkGateway(driver);
		String createMessage = "User created a new account with name \""+accountName+"\"";
		String actualMessage = investigateAction.clickActivityLog(driver, createMessage);
		driver.get(suiteData.getSaasAppBaseUrl());
		Assert.assertEquals(actualMessage, createMessage);
	}
	
	@Test(groups ={"Contact1"}, priority=120)
	public void verifyViewHomeContactAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewMessage = "User viewed Account named "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=121)
	public void verifyEditHomeContactAccountMessage() throws Exception{
		HashMap<String, String> expectedDataMap = new HashMap<String, String>();
		String editMessage = "User edited an account named \\\""+accountName+"\\\"";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, editMessage);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants._DOMAIN, suiteData.getTenantDomainName());
		salesforceHomeAction.verifySalesforceParameters(actualResult, expectedDataMap);
		
	}
	
	@Test(groups ={"Contact1"}, priority=122)
	public void verifyDeleteHomeContactAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteMessage = "User deleted the account "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=123)
	public void verifyCreateHomeContactMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createContact = "User created a contact with name "+contactFullName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=124)
	public void verifyEditHomeContactMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editContact = "User edited  contact "+contactName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=125)
	public void verifyViewHomeContactMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewContact = "User viewed Contact named "+contactNameSuffixSalutation;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=126)
	public void verifyDeleteHomeContactMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteContact = "User deleted the contact "+contactNameSuffixSalutation;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Home Tab Contacts End    ------------ */
	
	/* ------------    Salesforce Chatter Tab Contacts Start    ------------ */
	
	@Test(groups ={"Contact1"}, priority=127)
	public void salesforceChatterTabContactActivities() throws InterruptedException{
		Reporter.log("Starting testcase: salesforceChatterTabContactActivities");
		driver = getWebDriver();
		String timestamp = salesforceHomeAction.getTimestamp();
		accountName = "Account" + timestamp;
		driver.get(suiteData.getSaasAppBaseUrl());
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Account");
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnHomePage(driver, accountDetails);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.editAccountInSalesforce(driver, accountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.clickMoreTabOption(driver, "Contact");
		contactDetails.put(SalesforceConstants.SALUTATION, "Mr.");
		contactDetails.put(SalesforceConstants.FIRST_NAME, "FN");
		contactDetails.put(SalesforceConstants.MIDDLE_NAME, "MN");
		contactDetails.put(SalesforceConstants.LAST_NAME, "LN");
		contactDetails.put(SalesforceConstants.SUFFIX, timestamp);
		contactDetails.put(SalesforceConstants.EMAIL, "fn.ln@email.com");
		contactDetails.put(SalesforceConstants.PHONE, "1553287340");
		contactDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		contactDetails.put(SalesforceConstants.TITLE, "Title");
		salesforceHomeAction.createContact(driver, contactDetails);
		salesforceHomeAction.gotoChatterTab(driver);
		contactDetails.put(SalesforceConstants.MOBILE, "9834512388");
		salesforceHomeAction.editAndCreateContact(driver, contactDetails, "yes");
		salesforceHomeAction.gotoChatterTab(driver);
		contactNameSuffix = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME)+" "+contactDetails.get(SalesforceConstants.SUFFIX);
		salesforceHomeAction.deleteAccountInSalesForce(driver, contactNameSuffix);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		contactName = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME);
		contactFullName = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME);
		contactNameSuffixSalutation = contactDetails.get(SalesforceConstants.SALUTATION)+" "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME)+" "+contactDetails.get(SalesforceConstants.SUFFIX);
		Thread.sleep(1000*60);
		Reporter.log("Completed testcase: salesforceChatterTabContactActivities");
	}

	@Test(groups ={"Contact1"}, priority=128)
	public void verifyCreateChatterContactAccountMessage() throws Exception{
		List<String> objects = new ArrayList<String>();
		Thread.sleep(1000*60);
		objects.add("Account");
		driver.get(suiteData.getBaseUrl());
		investigateAction.gotoInvestigatePage(driver);
		Thread.sleep(1000*15);
		investigateAction.clickFilter(driver);
		investigateAction.choiceApp(driver, SalesforceConstants.SALESFORCE);
		investigateAction.selectObjects(driver, objects);
		investigateAction.checkGateway(driver);
		String createMessage = "User created a new account with name \""+accountName+"\"";
		String actualMessage = investigateAction.clickActivityLog(driver, createMessage);
		driver.get(suiteData.getSaasAppBaseUrl());
		Assert.assertEquals(actualMessage, createMessage);
	}
	
	@Test(groups ={"Contact1"}, priority=129)
	public void verifyViewChatterContactAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewMessage = "User viewed Account named "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=130)
	public void verifyEditChatterContactAccountMessage() throws Exception{
		HashMap<String, String> expectedDataMap = new HashMap<String, String>();
		String editMessage = "User edited an account named \\\""+accountName+"\\\"";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		HashMap<String, String> actualResult = salesforceHomeAction.getInvestigateLogs(client, expectedDataMap, suiteData, fromTime, editMessage);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants._DOMAIN, suiteData.getTenantDomainName());
		salesforceHomeAction.verifySalesforceParameters(actualResult, expectedDataMap);
	}
	
	@Test(groups ={"Contact1"}, priority=131)
	public void verifyDeleteChatterContactAccountMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteMessage = "User deleted the account "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=132)
	public void verifyCreateChatterContactMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String createContact = "User created a contact with name "+contactFullName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=133)
	public void verifyEditChatterContactMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String editContact = "User edited  contact "+contactName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=134)
	public void verifyViewChatterContactMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String viewContact = "User viewed Contact named "+contactNameSuffixSalutation;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, viewContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Contact1"}, priority=135)
	public void verifyDeleteChatterContactMessage() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String deleteContact = "User deleted the contact "+contactNameSuffixSalutation;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		driver.get(suiteData.getSaasAppBaseUrl());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	/* ------------    Salesforce Chatter Tab Contacts End    ------------ */
	
	@Test(groups ={"Regression", "Sanity", "Reach"}, priority=136)
	public void salesforces_Test_TestLogout() throws Exception {
		data.clear();
		data.put("message", "User logged out");
		salesforceLogin.logout(getWebDriver(), suiteData);
		//backend.assertAndValidateLog(client, suiteData, fromTime, data);
	}
}