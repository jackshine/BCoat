package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class AuditSpanVAIntegrationUploadTests2  extends AuditInitializeTests{

	protected Client restClient=null;
	protected String sourceID = null;
	protected String rsa_key_file_path=null;
	protected ArrayList<String> datasourceIdsList=new ArrayList<String>();
	protected String fileToBeUploaded=null;
	protected String sftpTenantUsername=null;
	protected String sftpServerHost=null;
	protected String sftpServerDestinationDir=null;
	protected String FireWallType=null;
	protected String spanVAPayload=null;
	protected String firewallLogFilePath=null;
	protected String agentId=null;
	protected Properties firewallLogDataProps=null;
	protected Properties spanvaConfigProperties=null;
	protected String registrationToken=null;
	protected String spanVAUpdatedVersion=null;
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	protected String scpcompltedCheckEmptyFilePath;
	protected String currentVersionSpanText;
	
	WebDriver driver=null;
	public AuditSpanVAIntegrationUploadTests2(String FireWallName) {
		this.FireWallType = FireWallName;
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(FireWallType);
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);


	}

	@BeforeClass(alwaysRun = true)
	public void initSpanVAUIConfiguration() throws Exception{
		Reporter.log("SpanVa Integration Tests started: ",true);
		//SpanVA properties loading
		spanvaConfigProperties=new Properties();
		spanvaConfigProperties.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.SPANVA_PROPS)));
		
		Reporter.log("spanva ip>>:"+suiteData.getSpanvaIp(),true);


		//Declaring and initialising the HtmlUnitWebDriver
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setPreference("xpinstall.signatures.required", false);
		driver = new FirefoxDriver(firefoxProfile);
		
		//driver.get(spanvaConfigProperties.getProperty("spanva_agent_imageverion_ip"));
		driver.get("https://"+suiteData.getSpanvaIp()+"/site/login");

		restClient = new Client();
	}


	@Test(priority=1)
	public void testSpanVAHomePage() throws Exception{
		Thread.sleep(10000);
		WebElement username=driver.findElement(By.id(spanvaConfigProperties.getProperty("loginusername")));
		username.sendKeys(spanvaConfigProperties.getProperty("loginusernameFieldValue"));
		WebElement password=driver.findElement(By.id(spanvaConfigProperties.getProperty("loginpassword")));
		password.sendKeys(spanvaConfigProperties.getProperty("loginpasswordFieldValue"));
		WebElement button=driver.findElement(By.name(spanvaConfigProperties.getProperty("loginbutton")));
		button.click();
		Thread.sleep(5000);
		Assert.assertEquals(spanvaConfigProperties.getProperty("spava_homepage_title"), driver.getTitle(),"SpanVa home pag Title ");

	}
	

	@Test(priority=2, dependsOnMethods="testSpanVAHomePage")
	public void testSpanVAAgentRegistration() throws Exception
	{
		//verify agent registration token configuration tab
		WebElement upgradeTab=driver.findElement(By.id("agent-tab"));
		upgradeTab.click();
		Thread.sleep(5000);

		//Check Already registered agent
		String agentName=spanvaConfigProperties.getProperty("spanva_configured_agent_name");
		String agentRegistrationToken=spanvaConfigProperties.getProperty("spanva_configured_agent_registration_token");

		boolean agentExist=eleExist(agentName);
		Reporter.log("agentExist:"+agentExist,true);
		boolean agentTokenExists=eleExist(agentRegistrationToken);
		Reporter.log("agentTokenExists:"+agentTokenExists,true);


		if(agentExist && agentTokenExists )// checking existency of the agent
		{
			Reporter.log("******Registered Agent Verification***********",true);
			WebElement configuredAgentName=driver.findElement(By.xpath(agentName));
			Reporter.log("configuredAgentName text::"+configuredAgentName.getText(),true);
			Assert.assertNotNull(driver.findElement(By.xpath(agentName)).getText(), "Agent name should be");
			
			//Verify Registered Agent status
			String registeredAgentStatus=getAgentRegistrationStatus();
			Assert.assertEquals(registeredAgentStatus, "Alive", "SpanVA Status should be:");
			
			//get Registred Agent
			agentId=getSpanvaAliveAgentOnThisInstance().get("agent_id");
			Reporter.log("Registed Agent id: "+agentId,true);
		}
		else{// else create new agent registration
			
			Reporter.log("******Agent Registration process started***********",true);
			//generate new Registration token
			registrationToken=generateNewRegistrationToken();

			WebElement spanvVaNameEle=driver.findElement(By.id(spanvaConfigProperties.getProperty("spanva_name")));
			spanvVaNameEle.sendKeys(spanvaConfigProperties.getProperty("spanva_name_value"));
			WebElement spanvVaRegistrationTokenEle=driver.findElement(By.id(spanvaConfigProperties.getProperty("spanva_registration_token")));
			spanvVaRegistrationTokenEle.sendKeys(registrationToken);
			Thread.sleep(5000);
			WebElement spanVARegisterAgentSubmit=driver.findElement(By.xpath(spanvaConfigProperties.getProperty("spanVARegisterAgentSubmit")));
			//Reporter.log("spanVARegisterAgentSubmit submit."+spanVARegisterAgentSubmit.getText(),true);

			spanVARegisterAgentSubmit.click();
			Thread.sleep(90000); //waiting time for agent registration process - 90 sec

			boolean agentExist_new=eleExist(agentName);
			boolean agentTokenExists_new=eleExist(agentRegistrationToken);
			Assert.assertEquals(agentExist_new, agentTokenExists_new," Agent Registered ");
			Reporter.log("***********Agent Registration process completed sucessfully********",true);
			
			//Verify Registered Agent status
			String registeredAgentStatus=getAgentRegistrationStatus();
			Assert.assertEquals(registeredAgentStatus, "Alive", "SpanVA Status should be:");
			
			//get Registred Agent
			agentId=getSpanvaAliveAgentOnThisInstance().get("agent_id");
			Reporter.log("Registed Agent id: "+agentId,true);
			
		}

	}
	
	
	

	@Test(priority=3,dependsOnMethods="testSpanVAAgentRegistration")
	public void testSpanVADatasourceCreation() throws Exception
	{
		
		String upgradeTabLocatorXPath="//*[@id='upgrade-tab']";
		WebElement upgradeTab=driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min

		//upgrade tab current version
		String currentVersionStrongLocator=".upgrade-status>div>strong";
		WebElement currentversionStrongEle=driver.findElement(By.cssSelector(currentVersionStrongLocator));
		String currentVersionStrongLable = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", currentversionStrongEle); 
		Reporter.log("Current SpanVA Image Version lable::"+currentVersionStrongLable,true);

		String currentVersionSpanLocator=".upgrade-status>div>span";
		WebElement currentversionSpanEle=driver.findElement(By.cssSelector(currentVersionSpanLocator));
		String currentVersionSpanText = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", currentversionSpanEle); 
		Reporter.log("Current SpanVA Image Version text::"+currentVersionSpanText,true);
		
		
		Reporter.log("SpanVA datasource creation test started on version:"+currentVersionSpanText,true);
		
		agentId=getSpanvaAliveAgentOnThisInstance().get("agent_id");
		spanVAPayload = AuditTestUtils.createSpanVAUploadBody(FireWallType,agentId,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SPANVA_DS_NAME);

		//create spanva datasource
		HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,new StringEntity(spanVAPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		Reporter.log("spanVAConnectionObject..."+spanVAConnectionObject,true);
		String dsName=(String)spanVAConnectionObject.get("name");
		Reporter.log("dsName::"+dsName,true);

		validateSpanVADataSource(spanVAConnectionObject);
		sourceID = (String) spanVAConnectionObject.get("id");
		JSONObject agentInfoObject=(JSONObject) spanVAConnectionObject.get("agent_info");
		String username=(String)agentInfoObject.get("user");
		String host=(String)agentInfoObject.get("host");
		String dest_dir=(String)agentInfoObject.get("dst_dir");
		
		sftpServerHost=getScpSftpServerHost();

		//getting data source from getDatasources list

		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();		
		queryParam.add(new BasicNameValuePair("fields", "datasources"));
		HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient,queryParam);
		Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		String strDatataSourceListResp=ClientUtil.getResponseBody(datataSourceListResp);
		Reporter.log("strDatataSourceListResp:"+strDatataSourceListResp,true);

		JSONObject listObj=new JSONObject(strDatataSourceListResp);
		JSONObject tenantObj=listObj.getJSONObject("objects");
		JSONArray datasourcesList = tenantObj.getJSONArray("datasources");
		String datasourcename=null;
		for(int i=0; i<datasourcesList.length(); i++)
		{
			datasourcename=((JSONObject)datasourcesList.get(i)).getString("name");
			if(dsName.equals(datasourcename))
			{
				sourceID=((JSONObject)datasourcesList.get(i)).getString("id");
				break;
			}
		}

		
		//upload file to the span VA datasource
		Reporter.log("******************Upload file using Scp:****************************************",true);
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(username, host,dest_dir);
		Reporter.log("******************scp upload detais:********************",true);
		Reporter.log("scpUsername:"+username,true);
		Reporter.log("scpPassword:"+sftpUtils.getPassWord(),true);
		Reporter.log("scpServerHost:"+host,true);
		Reporter.log("scpServerDestinationDir:"+dest_dir,true);
		Reporter.log("scpUploadedfirewallLogFilePath:"+firewallLogFilePath,true);
		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
		Reporter.log("******************scp upload started:********************",true);
		result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
		Reporter.log("scp file upload status:  " + result,true);
		Reporter.log("******************scp upload completed sucessfully:********************",true);

		//upload completed check
		FileInputStream fuploadCompltedCheckInputStream;
		File uploadCompletedFile = new File(System.getProperty("user.dir")+scpcompltedCheckEmptyFilePath);
		fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
		String uploadCompletedStatus = "";
		Reporter.log("******************scp upload completed started:********************",true);
		result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
		Reporter.log("scp completed status:  " + uploadCompletedStatus,true);
		Reporter.log("******************SCP COMPLETED:********************",true);
		//upload conpleted check end

		Thread.sleep(10000);//wait 10 sec
		
		
		
		
		

	}


	@Test(priority=4,dependsOnMethods="testSpanVADatasourceCreation")
	public void testDataSourceProcessAndMonitorLogsVerificationCheck() throws Exception
	{
		ArrayList<String> finalMonitorLogElementsOfCompletedDS=new ArrayList<String>();
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&&
				currentWaitTime <= AuditTestConstants.AUDIT_SCP_FILE_PROCESSING_MAX_WAITTIME){
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of "+ sourceID +" is "+ last_Status, true);
			finalMonitorLogElementsOfCompletedDS.addAll(verifyMonitorLogsDuringDataSourceProcess(sourceID));
			for(String str: finalMonitorLogElementsOfCompletedDS)
			{
				Assert.assertFalse(str.contains("Upload failed"),"Upload failed Message captured in the SpanVA Monitor Logs");
			}
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))			
				break;
		}
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_SCP_FILE_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);


	}


	@Test(priority=5, dependsOnMethods="testDataSourceProcessAndMonitorLogsVerificationCheck")
	public void testVerifySpanvaLatestUpdates() throws Exception
	{
		String upgradeTabLocatorXPath="//*[@id='upgrade-tab']";
		WebElement upgradeTab=driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min
		Reporter.log("upgrade tabe page source::"+driver.getPageSource(),true);
		
		//upgrade tab current version
		String currentVersionStrongLocator=".upgrade-status>div>strong";
		WebElement currentversionStrongEle=driver.findElement(By.cssSelector(currentVersionStrongLocator));
		String currentVersionStrongLable = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", currentversionStrongEle); 
		Reporter.log("Current SpanVA Image Version lable::"+currentVersionStrongLable,true);

		String currentVersionSpanLocator=".upgrade-status>div>span";
		WebElement currentversionSpanEle=driver.findElement(By.cssSelector(currentVersionSpanLocator));
		String currentVersionSpanText = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", currentversionSpanEle); 
		Reporter.log("Current SpanVA Image Version text::"+currentVersionSpanText,true);

		// Install updates button enable status verification
		String strUpdatedEnabeldEleXPath=null;
		strUpdatedEnabeldEleXPath=spanvaConfigProperties.getProperty("spanva_updates_enabled_text");

		boolean updatesEnabledFlag=false;
		updatesEnabledFlag=updatesEnabled();
		Reporter.log("Download and Install Updates Button Enable status:"+updatesEnabledFlag,true);
		
		//get the current agent id
		//Reporter.log("configured AgentId.."+agentId,true);
		if(updatesEnabledFlag){

			WebElement element = driver.findElement(By.cssSelector(".upgrade-view>a.e-update-btn.btn.btn-success.enabled"));
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", element);


			Reporter.log("SpanVA updates installataion in progress..!",true);
			Thread.sleep(120000);//wait here 2 mins for the updates
			verifyUpdates();

		}
		else{
			Thread.sleep(10000);
			updatesEnabledFlag=updatesEnabled();
			Reporter.log("Download and Install Updates Button Enable status inside else blk:"+updatesEnabledFlag,true);


			long currentWaitTime = 0;
			while( (!updatesEnabledFlag) && currentWaitTime <= 900000){
				Thread.sleep(30000);
				currentWaitTime += 30000;
				updatesEnabledFlag=updatesEnabled();
				Reporter.log("Updates install button status: "+ updatesEnabledFlag,true);
				if(updatesEnabledFlag)			
					break;
			}

			Assert.assertEquals(updatesEnabledFlag, true,"updates button is not enabled after 30 min due to Updates are not available ");

			WebElement element = driver.findElement(By.cssSelector(".upgrade-view>a.e-update-btn.btn.btn-success.enabled"));
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", element);
			Reporter.log("SpanVA updates installataion in progress..!");
			Thread.sleep(300000);//wait here 5 mins for the updates

			verifyUpdates();


		}


	}

	@Test(priority=6,dependsOnMethods="testVerifySpanvaLatestUpdates")
	public void testSpanVADatasourceCreationonUpgradedSpanVA() throws Exception
	{
		currentVersionSpanText=spanvaCurrentVersion();

		Reporter.log("SpanVA datasource creation test on updated  version:"+currentVersionSpanText,true);
		agentId=getSpanvaAliveAgentOnThisInstance().get("agent_id");
		Reporter.log("agentId:: of updated Spanva: "+agentId,true);
		spanVAPayload = AuditTestUtils.createSpanVAUploadBody(FireWallType,agentId,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SPANVA_DS_NAME);

		//create spanva datasource
		HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,new StringEntity(spanVAPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		Reporter.log("spanVAConnectionObject..."+spanVAConnectionObject,true);
		String dsName=(String)spanVAConnectionObject.get("name");
		Reporter.log("dsName::"+dsName,true);

		validateSpanVADataSource(spanVAConnectionObject);
		sourceID = (String) spanVAConnectionObject.get("id");
		JSONObject agentInfoObject=(JSONObject) spanVAConnectionObject.get("agent_info");
		String username=(String)agentInfoObject.get("user");
		String host=(String)agentInfoObject.get("host");
		String dest_dir=(String)agentInfoObject.get("dst_dir");
		sftpServerHost=getScpSftpServerHost();

		//getting data source from getDatasources list

		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();		
		queryParam.add(new BasicNameValuePair("fields", "datasources"));
		HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient,queryParam);
		Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		String strDatataSourceListResp=ClientUtil.getResponseBody(datataSourceListResp);
		Reporter.log("strDatataSourceListResp:"+strDatataSourceListResp,true);

		JSONObject listObj=new JSONObject(strDatataSourceListResp);
		JSONObject tenantObj=listObj.getJSONObject("objects");
		JSONArray datasourcesList = tenantObj.getJSONArray("datasources");
		String datasourcename=null;
		for(int i=0; i<datasourcesList.length(); i++)
		{
			datasourcename=((JSONObject)datasourcesList.get(i)).getString("name");
			if(dsName.equals(datasourcename))
			{
				sourceID=((JSONObject)datasourcesList.get(i)).getString("id");
				break;
			}
		}

			
		//upload file to the span VA datasource
		Reporter.log("******************Upload file using Scp:****************************************",true);
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(username, host,dest_dir);
		Reporter.log("******************scp upload detais:********************",true);
		Reporter.log("scpUsername:"+username,true);
		Reporter.log("scpPassword:"+sftpUtils.getPassWord(),true);
		Reporter.log("scpServerHost:"+host,true);
		Reporter.log("scpServerDestinationDir:"+dest_dir,true);
		Reporter.log("scpUploadedfirewallLogFilePath:"+firewallLogFilePath,true);
		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
		Reporter.log("******************scp upload started:********************",true);
		result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
		Reporter.log("scp file upload status:  " + result,true);
		Reporter.log("******************scp upload completed sucessfully:********************",true);

		//upload completed check
		FileInputStream fuploadCompltedCheckInputStream;
		File uploadCompletedFile = new File(System.getProperty("user.dir")+scpcompltedCheckEmptyFilePath);
		fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
		String uploadCompletedStatus = "";
		Reporter.log("******************scp upload completed started:********************",true);
		result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
		Reporter.log("scp completed status:  " + uploadCompletedStatus,true);
		Reporter.log("******************SCP COMPLETED:********************",true);
		//upload conpleted check end

		Thread.sleep(10000);//wait 10 sec
	}



	@Test(priority=7,dependsOnMethods="testSpanVADatasourceCreationonUpgradedSpanVA")
	public void testDataSourceProcessAndMonitorLogsVerificationCheckAfterSpanVAUpdates() throws Exception
	{
		ArrayList<String> finalMonitorLogElementsOfCompletedDS=new ArrayList<String>();
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&&
				currentWaitTime <= AuditTestConstants.AUDIT_SCP_FILE_PROCESSING_MAX_WAITTIME){
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of "+ sourceID +" is "+ last_Status, true);
			finalMonitorLogElementsOfCompletedDS.addAll(verifyMonitorLogsDuringDataSourceProcess(sourceID));
			for(String str: finalMonitorLogElementsOfCompletedDS)
			{
				Assert.assertFalse(str.contains("Upload failed"),"Upload Message captured in the SpanVA Monitor Logs");
			}
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))			
				break;
		}
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_SCP_FILE_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);


	}
	@Test(priority=8,dependsOnMethods={"testDataSourceProcessAndMonitorLogsVerificationCheckAfterSpanVAUpdates"})	
	public void TestAuditSummary() throws Exception {
		Reporter.log("Getting summary for "+ FireWallType +" its ID is: "+sourceID, true);
		AuditGoldenSetDataController controller=null;
		String range = "1mo";			
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		

		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);				


		switch(FireWallType)
		{

		case AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG: {
			controller=new AuditGoldenSetDataController(AuditTestConstants.BLUECOATPROXY_DATA_SHEET);
			//goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,FireWallType,goldenSetErrorList);
			//Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG+" Summary Results are wrong");
			break;
		}}
	}

	@Test(priority=9,dependsOnMethods={"TestAuditSummary"})
	public void testAuditReport() throws Exception {
		Reporter.log("Getting Report for "+ FireWallType +" its ID is: "+sourceID, true);
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		String range = "1mo";

		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response  = AuditFunctions.getAuditReport(restClient, queryParam);		
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
		Assert.assertEquals(reportObject.get("date_range"), range);
		Assert.assertNotNull(reportObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("generated_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("latest_date"),"Latest date is null");		


	}



	@Test(priority=10,dependsOnMethods={"testAuditReport"})
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("Deleting Data Source "+ FireWallType +" its ID is: "+sourceID, true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
	}


	@AfterClass
	public void testPopulateAuditSummaryFailures() throws Exception
	{
		Reporter.log("*****************SCP Logs: Audit Summary Validation Errors***********************",true);

		for(String str: goldenSetErrorList)
		{
			Reporter.log(str,true);
		}
	}


	//private methods used in this class:
	
	private void validateSpanVADataSource(JSONObject spanVAConnectionObject)
			throws Exception {
		Assert.assertEquals(spanVAConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);
		Assert.assertNotNull(spanVAConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) spanVAConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		//Assert.assertFalse(((String) spanVAConnectionObject.get("datasource_format")).isEmpty(),"Data source format is empty");
		//Assert.assertFalse(((String) spanVAConnectionObject.get("datasource_type")).isEmpty(),"Data source type is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_id"), "Agent Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_name"), "Agent Id is null");
		JSONObject agentInfoObject=(JSONObject) spanVAConnectionObject.get("agent_info");
		Assert.assertNotNull(agentInfoObject.get("host"), "Agent host is null");
		Assert.assertNotNull(agentInfoObject.get("log_collection_type"), "log_collection_type is null");
		Assert.assertNotNull(agentInfoObject.get("user"), "user is null");
		Assert.assertNotNull(agentInfoObject.get("dst_dir"), "dst_diris null");


	}
	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String sftpServerHost, String sftpServerDestinationDir ) {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		sftpUtils.setPassWord(AuditTestConstants.AUDIT_EOE_SPANVA_ING_SCP_PWD);
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
	}

	public boolean eleExist(String updateEnabled)
	{
		boolean present;
		try {
			driver.findElement(By.xpath(updateEnabled));
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}
		return present;
	}

	public boolean eleExistByCSSLocator(String updateEnabled)
	{
		boolean present;
		try {
			WebElement updatesEnabledEle= driver.findElement(By.cssSelector(updateEnabled));

			Reporter.log("updatesEnabledEle.isEnabled() in cssfunction:"+updatesEnabledEle.isEnabled(),true);
			Reporter.log("updatesEnabledEle.isDisplayed():"+updatesEnabledEle.isDisplayed(),true);
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}
		return present;
	}

	public boolean eleExistById(String updateEnabled)
	{
		boolean present;
		try {
			driver.findElement(By.id(updateEnabled));
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}
		return present;
	}


	private String spanvaCurrentVersion() throws Exception
	{
		String currentSpanvaversion=null;
		//Click upgrade tab
		String upgradeTabLocatorXPath="//*[@id='upgrade-tab']";
		WebElement updates=driver.findElement(By.xpath(upgradeTabLocatorXPath));
		updates.click();

		Thread.sleep(5000); // sleep for 5 sec

		Reporter.log("upgrade tabe page source::"+driver.getPageSource());

		//upgrade tab current version
		String currentVersionStrongLocator=".upgrade-status>div>strong";
		WebElement currentversionStrongEle=driver.findElement(By.cssSelector(currentVersionStrongLocator));
		String currentVersionStrongLable = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", currentversionStrongEle); 
		Reporter.log("Current SpanVA Image Version lable::"+currentVersionStrongLable,true);

		String currentVersionSpanLocator=".upgrade-status>div>span";
		WebElement currentversionSpanEle=driver.findElement(By.cssSelector(currentVersionSpanLocator));
		currentSpanvaversion = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", currentversionSpanEle); 
		Reporter.log("Current SpanVA Image Version text::"+currentVersionSpanText,true);
		return currentSpanvaversion;
	}

	public String generateNewRegistrationToken() throws Exception
	{
		HttpResponse createResp = AuditFunctions.generateAgentRegistrationKey(restClient,new StringEntity("{}"));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject registrationKeyJsonResponse = (JSONObject) new JSONObject(ClientUtil.getResponseBody(createResp));
		Assert.assertNotNull(registrationKeyJsonResponse.get("registration_token"), "Registration token is null");
		String registrationToken=(String)registrationKeyJsonResponse.get("registration_token");
		Reporter.log("registrationToken: "+registrationToken,true);
		return registrationToken;

	}

	public  boolean isClickable(WebElement webe)      
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.elementToBeClickable(webe));
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}


	public  boolean updatesEnabled()      
	{
		boolean updatesEnabledFlag=false;
		if(driver.findElement(By.cssSelector(".upgrade-view>a")).getAttribute("class").contains("enabled")){
			updatesEnabledFlag=true;
		}
		return updatesEnabledFlag;
	}

	public  void mouseClickByLocator( String locator ) {    
		WebElement element = driver.findElement( By.cssSelector(locator ) ); 


		Reporter.log("toString..."+element.toString(),true);
		Reporter.log("getAttribute..."+element.getAttribute("e-update-btn btn btn-success enabled"),true);
		Reporter.log("isEnabled..."+element.isEnabled(),true);
		Reporter.log("isDisplayed..."+element.isDisplayed(),true);
		Reporter.log("getText..."+element.getText(),true);

		Actions builder = new Actions(driver);    
		builder.moveToElement( element ).click( element );    
		builder.perform();    
	}

	public void testClick() throws InterruptedException{


		driver.manage().window().setSize(new Dimension(200, 200));

		List<WebElement> elements = driver.findElements(By.tagName("a"));

		Reporter.log("elements size..."+elements.size(),true);

		// check visibility
		for (WebElement element : elements) {
			Reporter.log("toString..."+element.toString(),true);
			Reporter.log("isDisplayed..."+element.isDisplayed(),true);
			// Assert.assertTrue(element.isDisplayed());
		}



		WebElement element = driver.findElement(By.cssSelector(".upgrade-view>a.e-update-btn.btn.btn-success.enabled"));

		Point point = element.getLocation();  
		int xcord = point.getX();  
		int ycord = point.getY();  
		System.out.println(xcord + ", " + ycord);  
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].click();", element);

		//eleClick(element);

		WebElement ele = driver.findElement(By.cssSelector("a.e-update-btn.btn.btn-success.enabled"));

		if (ele.isEnabled()) {
			System.out.println("Color Changed successfully after we enabled....");
		}
		if (ele.isDisplayed()) {
			System.out.println("Color Changed successfully after we double clicked");
		}



	}

	public void eleClick(WebElement element) {
		try {
			Actions action = new Actions(driver).doubleClick(element);
			action.build().perform();

			System.out.println("Double clicked the element");
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document "
					+ e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element " + element + " was not found in DOM "
					+ e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Element " + element + " was not clickable "
					+ e.getStackTrace());
		}
	}


	public void verifyUpdates() throws Exception
	{
		//Thread.sleep(180000);//w
	/*	WebElement updatedFormUsernme=driver.findElement(By.id("settingsform-current_password"));
		Assert.assertNotNull(updatedFormUsernme.getText(),"Updated Form username ");
		WebElement updatedFormNewPwd=driver.findElement(By.id("settingsform-new_password"));
		updatedFormNewPwd.sendKeys(spanvaConfigProperties.getProperty("spanva_updatedpage_new_pwd"));
		WebElement updatedFormNewPwdRepeat=driver.findElement(By.id("settingsform-new_password_repeat"));
		updatedFormNewPwdRepeat.sendKeys(spanvaConfigProperties.getProperty("spanva_updatedpage_conf_new_pwd"));
		//spanva_changepassword_button
		//WebElement changePassword=driver.findElement(By.xpath(".//*[@id='reset-password-form']/div[4]/div/button"));
		WebElement changePassword=driver.findElement(By.xpath(spanvaConfigProperties.getProperty("spanva_changepassword_button")));
		changePassword.click();*/

		Thread.sleep(30000);
		String updatedVersion=getSpanvaAliveAgentOnThisInstance().get("updated_version");
		Reporter.log("SpanVA Version is migrated to:"+updatedVersion,true);

		Assert.assertNotNull(updatedVersion," Migrated SpanVa Image: ");

		spanVAUpdatedVersion=updatedVersion;

	}

	public Map<String,String> getUpdatedSpanVaAgentDetails() throws Exception
	{

		Reporter.log("getting updated spanvaversion:", true);
		HttpResponse getAllAgents = AuditFunctions.getAllAgentsList(restClient);
		Assert.assertEquals(getAllAgents.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strAgents=(ClientUtil.getResponseBody(getAllAgents));
		//Reporter.log("strAgents:"+strAgents,true);
		JSONArray agentsList = (JSONArray) new JSONObject(strAgents).getJSONArray("objects");
		Map<String,String> agentInfoMap=new HashMap<String,String>();

		int size=agentsList.length();
		for( int i=0; i<size; i++)
		{
			JSONObject object = agentsList.getJSONObject(i);

			if(spanvaConfigProperties.getProperty("spanva_name_value").equals(object.get("agent_name")))
			{

				agentInfoMap.put("agent_name", (String)object.get("agent_name"));
				agentInfoMap.put("updated_version", (String)object.get("update_version"));
				agentInfoMap.put("agent_id", (String)object.get("agent_id"));
				break;
			}

		}
		return agentInfoMap;

	}
	
	public String getAgentRegistrationStatus() throws Exception
	{
		String agentRegistrationStatus=null;
		//String agent_ip=spanvaConfigProperties.getProperty("spanva_agent_ip");
		String agent_ip=suiteData.getSpanvaIp();
		String agent_name=spanvaConfigProperties.getProperty("spanva_name_value");

		HttpResponse getAllAgents = AuditFunctions.getAllAgentsList(restClient);
		Assert.assertEquals(getAllAgents.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strAgents=(ClientUtil.getResponseBody(getAllAgents));
		JSONArray agentsList = (JSONArray) new JSONObject(strAgents).getJSONArray("objects");
		int size=agentsList.length();
		for( int i=0; i<size; i++)
		{
			JSONObject object = agentsList.getJSONObject(i);
			if(agent_ip.equals(object.get("agent_ip")) && agent_name.equals(object.get("agent_name")))
			{

				agentRegistrationStatus= ((String)object.get("agent_state"));
				break;
			}

		}
		return agentRegistrationStatus;
		
	}
	
	public Map<String,String> getSpanvaAliveAgentOnThisInstance() throws Exception
	{

		String agent_state="Alive";
		//String agent_ip=spanvaConfigProperties.getProperty("spanva_agent_ip");
		String agent_ip=suiteData.getSpanvaIp();
		String agent_name=spanvaConfigProperties.getProperty("spanva_name_value");

		
		Reporter.log("getting updated spanvaversion:", true);
		HttpResponse getAllAgents = AuditFunctions.getAllAgentsList(restClient);
		Assert.assertEquals(getAllAgents.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strAgents=(ClientUtil.getResponseBody(getAllAgents));
		//Reporter.log("strAgents:"+strAgents,true);
		JSONArray agentsList = (JSONArray) new JSONObject(strAgents).getJSONArray("objects");
		Map<String,String> agentInfoMap=new HashMap<String,String>();

		int size=agentsList.length();
		for( int i=0; i<size; i++)
		{
			JSONObject object = agentsList.getJSONObject(i);

			if(agent_state.equals(object.get("agent_state")) && agent_ip.equals(object.get("agent_ip"))  && agent_name.equals(object.get("agent_name")) )
			{

				agentInfoMap.put("agent_name", (String)object.get("agent_name"));
				agentInfoMap.put("updated_version", (String)object.get("update_version"));
				agentInfoMap.put("agent_id", (String)object.get("agent_id"));
				break;
			}
			
		}
		return agentInfoMap;

	}

	public List<String> verifyMonitorLogsDuringDataSourceProcess(String dataSourceID) throws Exception
	{
		WebElement monitorLogsTab=driver.findElement(By.xpath(spanvaConfigProperties.getProperty("spanva_monitor_logs_tab")));
		monitorLogsTab.click();

		Thread.sleep(5000);
		List<WebElement> elements=driver.findElements(By.cssSelector(".ui-widget-content.slick-row .slick-cell"));

		Reporter.log("monitor log elemnets::"+elements.size(),true);
		//WebElement monitorLogsTab1=driver.findElement(By.cssSelector(".div.e-monitor-logs.slickgrid_729994.ui-widget>div.slick-viewport>div>div:nth-child(1)>div"));
		List<String> monitorLogsElementsList=new ArrayList<String>();
		String strMonitorLogElement=null;
		for(WebElement wele:elements)
		{
			strMonitorLogElement=wele.getText();
			if(strMonitorLogElement.contains(suiteData.getTenantName()) || strMonitorLogElement.contains(dataSourceID))
			{
				monitorLogsElementsList.add(strMonitorLogElement);
			}

		}
		return monitorLogsElementsList;


	}
	public String getScpSftpServerHost()
	{
		String sftpServerHost=null;
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			sftpServerHost=AuditTestConstants.AUDIT_QAVPC_SCP_SERVER;}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_PROD;
		}
		else if(suiteData.getApiserverHostName().contains("api-cep.elastica.net")){
			sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_CEP;
		}
		else{
			sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER;
		}
		Reporter.log("scp/sftp serverhost: "+sftpServerHost,true);
		return sftpServerHost;
	}


}


