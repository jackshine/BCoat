package com.elastica.action.aws;

import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.action.Action;
import com.elastica.action.backend.BEAction;
import com.elastica.restClient.ClientUtil;
import com.elastica.common.GWCommonTest;
import com.elastica.common.SuiteData;
import com.elastica.constants.CommonConstants;
import com.elastica.constants.aws.AwsConstants;
import com.elastica.constants.salesforce.SalesforceConstants;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.gateway.RawJsonParser;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.AwsPage;
import com.elastica.pageobjects.LoginPage;
import com.elastica.pageobjects.SalesforceHomePage;
import com.elastica.pageobjects.investigate.InvestigatePage;
import com.elastica.restClient.Client;
import com.elastica.restClient.ESQueryBuilder;
import com.google.common.io.Files;
import com.thoughtworks.selenium.webdriven.commands.KeyEvent;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

public class AwsAction extends Action{

	
	public void loginAWS(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
		//try{
			Logger.info("Login in to AWS");
			Logger.info("Username: "+suiteData.getSaasAppUsername());
			Logger.info("Password: "+suiteData.getSaasAppPassword());
			driver.get(suiteData.getSaasAppBaseUrl());
			Thread.sleep(10000);
			aws.awsLoginEmail(driver).waitForLoading(driver);
			aws.awsLoginEmail(driver).clear();
			aws.awsLoginEmail(driver).type(suiteData.getSaasAppUsername());
			aws.awsLoginPassword(driver).waitForLoading(driver);
			aws.awsLoginPassword(driver).clear();
			aws.awsLoginPassword(driver).type(suiteData.getSaasAppPassword());
			Thread.sleep(10000);
			aws.awsLoginButton(driver).waitForLoading(driver);
			aws.awsLoginButton(driver).click();
			Logger.info("Logged in to AWS");
		/*}catch(Exception e){
			Logger.error("Login Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}*/
	}
	
	public void logoutAWS(WebDriver driver) throws InterruptedException{
		AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
		//try{
			Logger.info("Logout in to AWS");
			aws.awsLogoutMenu(driver).waitForLoading(driver);
			aws.awsLogoutMenu(driver).click();
			Thread.sleep(3000);
			aws.awsLogoutButton(driver).waitForLoading(driver);
			aws.awsLogoutButton(driver).click();
			Thread.sleep(3000);
			Logger.info("Logged out AWS completed");
		/*}catch(Exception e){
			Logger.error("Logout Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}*/
	}
	
	public void invalidateLoginAWS(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
		try{
			Logger.info("Login in to AWS with invalid passowrd");
			Logger.info("Username: "+suiteData.getSaasAppUsername());
			Logger.info("Password: "+"Elastica123");
			driver.get(suiteData.getSaasAppBaseUrl());
			Thread.sleep(10000);
			aws.awsLoginEmail(driver).waitForLoading(driver);
			aws.awsLoginEmail(driver).clear();
			aws.awsLoginEmail(driver).type(suiteData.getSaasAppUsername());
			aws.awsLoginPassword(driver).waitForLoading(driver);
			aws.awsLoginPassword(driver).clear();
			aws.awsLoginPassword(driver).type("Elastica123");
			Thread.sleep(10000);
			aws.awsLoginButton(driver).waitForLoading(driver);
			aws.awsLoginButton(driver).click();
			Logger.info("Login failed with invalid credencials");
		}catch(Exception e){
			Logger.error("Invalidate Login Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String getRegion(WebDriver driver){
		String region = driver.getCurrentUrl();
		region = region.substring(region.lastIndexOf('=')+1, region.length());
		return region;
	}
	
	public String createBucket(WebDriver driver, String bucketName, String regionName) throws InterruptedException{
		String region = null;
		try{
			Logger.info("User creating the bucket: "+bucketName);
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			driver.get(AwsConstants.AWS_S3_URL);
			Thread.sleep(10000);
			region = driver.getCurrentUrl();
			region = region.substring(region.lastIndexOf('=')+1, region.length());
			Logger.info("Navigating to the frame: ("+region+")");
			WebElement element = driver.findElement(By.xpath(".//iframe[contains(@src,'"+region+"')]"));
			driver.switchTo().frame(element);
			Thread.sleep(3000);
			aws.awss3CreateBucket(driver).waitForLoading(driver);
			aws.awss3CreateBucket(driver).click();
			Thread.sleep(3000);
			aws.awss3CreateBucketName(driver).waitForLoading(driver);
			aws.awss3CreateBucketName(driver).type(bucketName);
			Thread.sleep(3000);
			aws.awss3regiondropdown(driver).waitForLoading(driver);
			aws.awss3regiondropdown(driver).click();
			Thread.sleep(3000);
			aws.awss3selectregion(driver, regionName).waitForLoading(driver);
			aws.awss3selectregion(driver, regionName).click();
			Thread.sleep(3000);
			aws.awss3CreateBucketButton(driver).waitForLoading(driver);
			aws.awss3CreateBucketButton(driver).click();
			Logger.info("User created the bucket: "+bucketName);
		}catch(Exception e){
			Logger.error("Create bucket Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
		return region;
	}
	
	public void deletebucket(WebDriver driver, String bucketName, String region) throws InterruptedException{
		try{
			Logger.info("User deleting the bucket: "+bucketName);
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			driver.navigate().to(AwsConstants.AWS_S3_URL);
			Thread.sleep(10000);
			this.switchtoFrame(driver, region);
			aws.awss3BucketSelect(driver, bucketName).waitForLoading(driver);
			aws.awss3BucketSelect(driver, bucketName).click();
			Thread.sleep(5000);
			aws.awss3BucketActionButton(driver).waitForLoading(driver);
			aws.awss3BucketActionButton(driver).click();
			aws.awss3DeleteBucket(driver).waitForLoading(driver);
			aws.awss3DeleteBucket(driver).click();
			Thread.sleep(5000);
			aws.awss3DeleteBucketName(driver).waitForLoading(driver);
			aws.awss3DeleteBucketName(driver).type(bucketName);
			aws.awss3DeleteBucketOk(driver).waitForLoading(driver);
			aws.awss3DeleteBucketOk(driver).click();
			Logger.info("User deleted the bucket: "+bucketName);
		}catch(Exception e){
			Logger.error("Delete bucket Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void emptybucket(WebDriver driver, String bucketName, String region) throws InterruptedException{
		try{
			Logger.info("User tring to empty the bucket: "+bucketName);
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			driver.navigate().to(AwsConstants.AWS_S3_URL);
			Thread.sleep(10000);
			this.switchtoFrame(driver, region);
			aws.awss3BucketSelect(driver, bucketName).waitForLoading(driver);
			aws.awss3BucketSelect(driver, bucketName).click();
			Thread.sleep(5000);
			aws.awss3BucketActionButton(driver).waitForLoading(driver);
			aws.awss3BucketActionButton(driver).click();
			aws.awss3EmptyBucket(driver).waitForLoading(driver);
			aws.awss3EmptyBucket(driver).click();
			Thread.sleep(5000);
			aws.awss3EmptyBucketName(driver).waitForLoading(driver);
			aws.awss3EmptyBucketName(driver).type(bucketName);
			aws.awss3EmptyBucketOk(driver).waitForLoading(driver);
			aws.awss3EmptyBucketOk(driver).click();
			Logger.info("Bucket is empty : "+bucketName);
		}catch(Exception e){
			Logger.error("Delete bucket Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void switchtoFrame(WebDriver driver, String region) throws InterruptedException{
		WebElement element = driver.findElement(By.xpath(".//iframe[contains(@src,'"+region+"')]"));
			driver.switchTo().frame(element);
	}
	
	public void createFolder(WebDriver driver, String bucketName, String folderName, String region) throws InterruptedException{
		try{
			Logger.info("User creating the folder: "+folderName);
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			Thread.sleep(5000);
			aws.awss3BucketClick(driver, bucketName).waitForLoading(driver);
			aws.awss3BucketClick(driver, bucketName).click();
			Thread.sleep(5000);
			aws.awss3CreateFolderButton(driver).waitForLoading(driver);
			aws.awss3CreateFolderButton(driver).click();
			Thread.sleep(2000);
			aws.awss3CreateFolderEnterName(driver).type(folderName);
			aws.awss3CreateFolderEnterName(driver).type(Keys.TAB.toString());
			Logger.info("User created the folder: "+folderName);
		}catch(Exception e){
			Logger.error("Create folder Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void clickBucket(WebDriver driver, String bucketName, String region){
		try{
			Logger.info("Click on the bucket: "+bucketName);
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			driver.navigate().to(AwsConstants.AWS_S3_URL);
			Thread.sleep(10000);
			this.switchtoFrame(driver, region);
			Thread.sleep(5000);
			aws.awss3BucketClick(driver, bucketName).waitForLoading(driver);
			aws.awss3BucketClick(driver, bucketName).click();
		}catch(Exception e){
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void deleteFolder(WebDriver driver, String folderName, SuiteData suiteData) throws InterruptedException{
		try{
			Logger.info("User deleting the folder: "+folderName);
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			Thread.sleep(3000);
			aws.awss3SelectFolder(driver, folderName).waitForLoading(driver);
			aws.awss3SelectFolder(driver, folderName).click();
			Thread.sleep(3000);
			aws.awss3FolderActionButton(driver).waitForLoading(driver);	
			aws.awss3FolderActionButton(driver).click();
			Thread.sleep(3000);
			aws.awss3DeleteFolderMenu(driver).waitForLoading(driver);
			aws.awss3DeleteFolderMenu(driver).click();
			Thread.sleep(5000);
			driver.switchTo().alert().accept();
			Thread.sleep(15000);
			new GWCommonTest().clickOkInPopup(suiteData);
			if(aws.awss3SelectFolder(driver, folderName).isDisplayed()){
				aws.awss3SelectFolder(driver, folderName).waitForLoading(driver);
				aws.awss3SelectFolder(driver, folderName).click();
				Thread.sleep(3000);
				aws.awss3FolderActionButton(driver).waitForLoading(driver);	
				aws.awss3FolderActionButton(driver).click();
				Thread.sleep(3000);
				aws.awss3DeleteFolderMenu(driver).waitForLoading(driver);
				aws.awss3DeleteFolderMenu(driver).click();
				Thread.sleep(5000);
				driver.switchTo().alert().accept();
				Thread.sleep(15000);
				new GWCommonTest().clickOkInPopup(suiteData);
			}
			driver.get(AwsConstants.AWS_S3_URL);
			Thread.sleep(5000);
			Logger.info("User deleted the folder: "+folderName);
		}catch(Exception e){
			Logger.error("Delete folder Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void uploadFile(WebDriver driver, File file, SuiteData suiteData) throws InterruptedException, IOException{
		try{
			Logger.info("User uploading the file: "+file.getName());
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			Thread.sleep(3000);
			aws.awss3UploadButton(driver).waitForLoading(driver);
			aws.awss3UploadButton(driver).click();
			Thread.sleep(3000);
			driver.findElement(By.cssSelector("input[type='file']")).sendKeys(file.getAbsolutePath());
			Thread.sleep(3000);
			aws.awss3StartUploadButton(driver).waitForLoading(driver);
			aws.awss3StartUploadButton(driver).click();
			Thread.sleep(5000);
			Logger.info("User uploaded the file: "+file.getName());
		}catch(Exception e){
			Logger.error("Upload file Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void shareFile(WebDriver driver, String fileName, String region) throws InterruptedException{
		try{
			Logger.info("User sharing the file: "+fileName);
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			driver.switchTo().activeElement();
			Thread.sleep(5000);
			aws.awss3SelectFolder(driver, fileName).waitForLoading(driver);
			aws.awss3SelectFolder(driver, fileName).click();
			Thread.sleep(3000);
			aws.awss3FolderActionButton(driver).waitForLoading(driver);	
			aws.awss3FolderActionButton(driver).click();
			Thread.sleep(3000);
			aws.awss3ShareFileMenu(driver).waitForLoading(driver);
			aws.awss3ShareFileMenu(driver).click();
			Thread.sleep(3000);
			driver.switchTo().alert().accept();
			Thread.sleep(5000);
			Logger.info("User shared the file: "+fileName);
		}catch(Exception e){
			Logger.error("Share file Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void openFile(WebDriver driver, String fileName) throws InterruptedException{
		try{
			Logger.info("User opening the file: "+fileName);
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			Thread.sleep(3000);
			aws.awss3SelectFolder(driver, fileName).waitForLoading(driver);
			aws.awss3SelectFolder(driver, fileName).click();
			Thread.sleep(3000);
			aws.awss3FolderActionButton(driver).waitForLoading(driver);	
			aws.awss3FolderActionButton(driver).click();
			Thread.sleep(3000);
			aws.awss3OpenFileMenu(driver).waitForLoading(driver);
			aws.awss3OpenFileMenu(driver).click();
			Thread.sleep(10000);
			String currentWindowId = driver.getWindowHandle();
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				if(!driver.getWindowHandle().equals(currentWindowId)){
					driver.close();
					break;
				}
			}
			driver.switchTo().window(currentWindowId);
			Logger.info("User opened the file: "+fileName);
		}catch(Exception e){
			Logger.error("Open file Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void deleteFile(WebDriver driver, String fileName, String region) throws InterruptedException{
		try{
			Logger.info("User deleting the file: "+fileName);
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			Thread.sleep(3000);
			this.switchtoFrame(driver, region);
			aws.awss3SelectFolder(driver, fileName).waitForLoading(driver);
			aws.awss3SelectFolder(driver, fileName).click();
			Thread.sleep(3000);
			aws.awss3FolderActionButton(driver).waitForLoading(driver);	
			aws.awss3FolderActionButton(driver).click();
			Thread.sleep(3000);
			aws.awss3DeleteFolderMenu(driver).waitForLoading(driver);
			aws.awss3DeleteFolderMenu(driver).click();
			Thread.sleep(5000);
			driver.switchTo().alert().accept();
			Thread.sleep(5000);
			Logger.info("User deleted the file: "+fileName);
		}catch(Exception e){
			Logger.error("Open file Fail");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void downloadFileAndHandleBlockPopup(WebDriver driver, File file, String region, SuiteData suiteData){
		try{
			Logger.info("User downloading the file: "+file.getName());
			AwsPage aws =  AdvancedPageFactory.getPageObject(driver, AwsPage.class);
			Thread.sleep(3000);
			aws.awss3SelectFolder(driver, file.getName()).waitForLoading(driver);
			aws.awss3SelectFolder(driver, file.getName()).click();
			Thread.sleep(3000);
			aws.awss3FolderActionButton(driver).waitForLoading(driver);	
			aws.awss3FolderActionButton(driver).click();
			Thread.sleep(3000);
			aws.awss3DownloadFileMenu(driver).waitForLoading(driver);
			aws.awss3DownloadFileMenu(driver).click();
			Thread.sleep(5000);
			String currentWindowId = driver.getWindowHandle();
			aws.awss3DownloadLink(driver).waitForLoading(driver);
			aws.awss3DownloadLink(driver).click();
			boolean flag = new GWCommonTest().clickOkInPopup(suiteData);
			Assert.assertTrue(flag, "Popup doesnot exists");
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				if(!driver.getWindowHandle().equals(currentWindowId)){
					driver.close();
					break;
				}
			}
			driver.switchTo().window(currentWindowId);
			this.switchtoFrame(driver, region);
			aws.awss3DownloadPanelOk(driver).waitForLoading(driver);
			aws.awss3DownloadPanelOk(driver).click();
		}catch(Exception e){
			
		}
	}
	
	public String getFromTime() {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		currentTime=currentTime.minusDays(1);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//.withZone(DateTimeZone.forID("America/Los_Angeles"));
		String tsfrom = currentTime.toString(df);
		Logger.info("Start time: "+tsfrom);
		return tsfrom;
	}
	
	public void uploadFileUsingScript(SuiteData suiteData, File file) throws IOException, InterruptedException{
		File uploadScript = null;
		String fileAbsolutePath = file.getAbsolutePath();
		String uploadScriptPath = null;
		if(System.getProperty("os.name").contains("Window")){
			if(suiteData.getBrowser().equalsIgnoreCase("firefox")){
				uploadScript = new File(CommonConstants.FILE_UPLOAD_FIREFOX_EXE);
			}else if(suiteData.getBrowser().equalsIgnoreCase("chrome")){
				uploadScript = new File(CommonConstants.FILE_UPLOAD_CHROME_EXE);
			}
			String uploadScriptAbsolutePath = uploadScript.getAbsolutePath();
			String args[]   = 	new String[2];
			args[0] 		= 	uploadScriptAbsolutePath;
			args[1]			=	fileAbsolutePath;
			Runtime run = Runtime.getRuntime();
			run.exec(args);
		}else if(System.getProperty("os.name").contains("Mac")){
			if(suiteData.getBrowser().equalsIgnoreCase("firefox")){
				uploadScript = new File(CommonConstants.FILE_UPLOAD_FIREFOX_SCPT);
				uploadScriptPath = uploadScript.getAbsolutePath();
				System.out.println("is File Exists: "+uploadScript.exists());
				System.out.println("is File Exists: "+file.exists());
				String command="osascript " + uploadScriptPath+" "+fileAbsolutePath+" "+suiteData.getBrowser();
				System.out.println(command);
				Process p = Runtime.getRuntime().exec(command);
			    p.waitFor();
			    Thread.sleep(5000);
			}
		}
	}
	
	public void uploadFileUsingScript1(SuiteData suiteData, String file) throws IOException, InterruptedException{
		File uploadScript = null;
		String fileAbsolutePath = file;
		String uploadScriptPath = null;
		if(System.getProperty("os.name").contains("Window")){
			if(suiteData.getBrowser().equalsIgnoreCase("firefox")){
				uploadScript = new File(CommonConstants.FILE_UPLOAD_FIREFOX_EXE);
			}else if(suiteData.getBrowser().equalsIgnoreCase("chrome")){
				uploadScript = new File(CommonConstants.FILE_UPLOAD_CHROME_EXE);
			}
			String uploadScriptAbsolutePath = uploadScript.getAbsolutePath();
			String args[]   = 	new String[2];
			args[0] 		= 	uploadScriptAbsolutePath;
			args[1]			=	fileAbsolutePath;
			Runtime run = Runtime.getRuntime();
			run.exec(args);
		}else if(System.getProperty("os.name").contains("Mac")){
			if(suiteData.getBrowser().equalsIgnoreCase("firefox")){
				uploadScript = new File(CommonConstants.FILE_UPLOAD_FIREFOX_SCPT);
				uploadScriptPath = uploadScript.getAbsolutePath();
				System.out.println("is File Exists: "+uploadScript.exists());
			//	System.out.println("is File Exists: "+file.exists());
				String command="osascript " + uploadScriptPath+" "+fileAbsolutePath+" "+suiteData.getBrowser();
				System.out.println(command);
				Process p = Runtime.getRuntime().exec(command);
			    p.waitFor();
			    Thread.sleep(5000);
			}
		}
	}
}
