package com.elastica.beatle.tests.audit.spanvatests;

import java.io.File;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController3;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.GoldenSetData;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class Audit_SpanVA_Restore extends AuditInitializeTests {

	protected Client restClient = null;
	
	protected String rsa_key_file_path = null;
	protected ArrayList<String> datasourceIdsList = new ArrayList<String>();
	protected String fileToBeUploaded = null;
	protected String sftpTenantUsername = null;
	protected String sftpServerHost = null;
	protected String sftpServerDestinationDir = null;
	protected String FireWallType = null;
	protected String spanVAPayload = null;
	protected String firewallLogFilePath = null;
	protected String agentId = null;
	protected Properties firewallLogDataProps = null;
	protected Properties spanvaConfigProperties = null;
	protected String registrationToken = null;
	protected String spanVAUpdatedVersion = null;
	ArrayList<String> goldenSetErrorList = new ArrayList<String>();
	protected String scpcompltedCheckEmptyFilePath;
	protected String currentVersionSpanText;

	Properties spanvaconfigProps = new Properties();
	String simpleDateFormatPattern = "dd-MMM-YYYY";
	String existingSampleDsId = null;
	String spanvaNFSUserName = null;
	String spanvaNFSMountDir = null;
	String spanvaSrcDir = null;
	String spanvaFirewallSrcMountDir = null;
	String spanvaFirewalDestDir = null;
	String spanvaNFSServerCertPenFileLocation = null;
	String spanvaNfsServerHost = null;
	String spanvaHttpsPort = null;
	String spanvaScpPwd = null;
	String spanvaNFSLogsFolder = null;
	String spanvaDSCreationRequired = null;
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup = null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();
	String[] strFirewallDestAndSrcMountDir = null;
	
	Properties spanvaFirewalls = null;

	String spanvaAwsNodeConnectPEMLocationPath = null;
	String spanvaScpClientUsername = null;
	String spanVaScpClientUserPwd = null;
	String spanVaScpClientHost = null;
	String spanVaScpClientHostSourceDir = null;

	String spanvaFtpClientUsername = null;
	String spanVaFtpClientUserPwd = null;
	String spanVaFtpClientHost = null;
	String spanVaFtpClientHostSourceDir = null;
	String spanvaUpgradedVersion = null;
	private JSONObject s3BucketDetails;

	WebDriver driver = null;
	Properties scpdatasourcesProp = new Properties();
	Properties currentVersionProcessedDS = new Properties();


	Map<String,String> backupConfigDataMap=new HashMap<String,String>();


	String backuprestorefoldername;
	String backuprestoreFileName="";


	@BeforeClass(alwaysRun = true)
	public void initSpanVAUIConfiguration() throws Exception {
		Reporter.log("SpanVa Integration Tests started: ", true);
		// SpanVA properties loading
		spanvaConfigProperties = new Properties();
		spanvaConfigProperties
		.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.SPANVA_PROPS)));


		Reporter.log("spanva ip>>:" + suiteData.getSpanvaIp(), true);

		Reporter.log("spanva upgraded version>>:" + suiteData.getSpanvaUpdatedVersion(), true);

		spanvaUpgradedVersion=suiteData.getSpanvaUpdatedVersion();

		// Declaring and initialising the HtmlUnitWebDriver
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setPreference("xpinstall.signatures.required", false);
		driver = new FirefoxDriver(firefoxProfile);

		// driver.get(spanvaConfigProperties.getProperty("spanva_agent_imageverion_ip"));
		driver.get("https://" + suiteData.getSpanvaIp() + "/site/login");

		restClient = new Client();

		
		//Upload file to S3 folder
		s3BucketDetails=AuditTestUtils.getS3BucketDetails();

		backuprestoreFileName=suiteData.getEnvironmentName()+"_old_regkey_dss_br_data.txt";
		backuprestorefoldername= suiteData.getEnvironmentName()+"_old_regkey_dss_br_data";



		S3ActionHandler.readBackUpRestoreFileFromS3AndKeepIntoTempLocation(s3BucketDetails.getString("bucket"),backuprestorefoldername+"/"+backuprestoreFileName,backuprestoreFileName) ;
		backupConfigDataMap=readBackupFile(AuditTestConstants.AUDIT_BACKUP_RESTORE_FILE_TEMP_PATH+File.separator+backuprestoreFileName);


	}

	@Test(priority = 1)
	public void testSpanVAHomePage() throws Exception {
		Thread.sleep(10000);
		WebElement username = driver.findElement(By.id(spanvaConfigProperties.getProperty("loginusername")));
		username.sendKeys(spanvaConfigProperties.getProperty("loginusernameFieldValue"));
		WebElement password = driver.findElement(By.id(spanvaConfigProperties.getProperty("loginpassword")));
		password.sendKeys(spanvaConfigProperties.getProperty("loginpasswordFieldValue"));
		WebElement button = driver.findElement(By.name(spanvaConfigProperties.getProperty("loginbutton")));
		button.click();
		Thread.sleep(5000);
		Assert.assertEquals(spanvaConfigProperties.getProperty("spava_homepage_title"), driver.getTitle(),
				"SpanVa home pag Title ");

	}

	@Test(priority = 2, dependsOnMethods = "testSpanVAHomePage")
	public void testSpanVAAgentRegistration() throws Exception {
		// verify agent registration token configuration tab
		WebElement upgradeTab = driver.findElement(By.id("agent-tab"));
		upgradeTab.click();
		Thread.sleep(5000);

		// Check Already registered agent
		String agentName = spanvaConfigProperties.getProperty("spanva_configured_agent_name");
		String agentRegistrationToken = spanvaConfigProperties
				.getProperty("spanva_configured_agent_registration_token");

		boolean agentExist = eleExist(agentName);
		Reporter.log("agentExist:" + agentExist, true);
		boolean agentTokenExists = eleExist(agentRegistrationToken);
		Reporter.log("agentTokenExists:" + agentTokenExists, true);


		if (agentExist && agentTokenExists) // checking existency of the agent
		{
			Reporter.log("******Registered Agent Verification***********", true);
			WebElement configuredAgentName = driver.findElement(By.xpath(agentName));
			Reporter.log("configuredAgentName text::" + configuredAgentName.getText(), true);
			Assert.assertNotNull(driver.findElement(By.xpath(agentName)).getText(), "Agent name should be");

			// Verify Registered Agent status
			String registeredAgentStatus = getAgentRegistrationStatus();
			Assert.assertEquals(registeredAgentStatus, "Alive", "SpanVA Status should be:");

			// get Registred Agent
			agentId = getSpanvaAliveAgentOnThisInstance().get("agent_id");
			Reporter.log("Registed Agent id: " + agentId, true);

			

		} else {// else create new agent registration

			Reporter.log("******Agent Registration process started***********", true);
			// generate new Registration token
			registrationToken = backupConfigDataMap.get("registrationToken");

			WebElement spanvVaNameEle = driver.findElement(By.id(spanvaConfigProperties.getProperty("spanva_name")));
			//spanvVaNameEle.sendKeys(spanvaConfigProperties.getProperty("spanva_name_value"));
			spanvVaNameEle.sendKeys(suiteData.getSpanvaAgentName());
			WebElement spanvVaRegistrationTokenEle = driver
					.findElement(By.id(spanvaConfigProperties.getProperty("spanva_registration_token")));
			spanvVaRegistrationTokenEle.sendKeys(registrationToken);
			Thread.sleep(5000);
			WebElement spanVARegisterAgentSubmit = driver
					.findElement(By.xpath(spanvaConfigProperties.getProperty("spanVARegisterAgentSubmit")));
			// Reporter.log("spanVARegisterAgentSubmit
			// submit."+spanVARegisterAgentSubmit.getText(),true);

			spanVARegisterAgentSubmit.click();
			Thread.sleep(90000); // waiting time for agent registration process
			// - 90 sec

			boolean agentExist_new = eleExist(agentName);
			boolean agentTokenExists_new = eleExist(agentRegistrationToken);
			Assert.assertEquals(agentExist_new, agentTokenExists_new, " Agent Registered ");
			Reporter.log("***********Agent Registration process completed sucessfully********", true);

			// Verify Registered Agent status
			String registeredAgentStatus = getAgentRegistrationStatus();
			Assert.assertEquals(registeredAgentStatus, "Alive", "SpanVA Status should be:");

			// get Registred Agent
			agentId = getSpanvaAliveAgentOnThisInstance().get("agent_id");
			Reporter.log("Registed Agent id: " + agentId, true);

		}


	}

	//dataprovider to get the datasources of back up and registration key
	@DataProvider(name = "backupDatasources", parallel = true)
	public Object[][] populateCurrentSpanvaScpDataSources() throws Exception {

		Object[][] inputData = new Object[backupConfigDataMap.size()][2];
		int j = 0;

		for (Map.Entry<String, String> entry : backupConfigDataMap.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

			inputData[j][0] = entry.getKey();
			inputData[j][1] = entry.getValue();

			j++;
		}
		return inputData;
	}
	
	public String getDatsourceStatus(String datsourceId) throws Exception
	{
	   
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, datsourceId);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		return last_Status;
	}
	


	//verify users of datasources should be anony enabled

	//@Test(priority = 2, dataProvider = "backupDatasources", dependsOnMethods="testSpanVAAgentRegistration")
	public void testUsersStateShouldBeAnonyBeforeRestoreBackup(String fireWallType, String dataSourceID) throws Exception
	{
		Reporter.log("testUsersStateShouldBeAnonyBeforeRestoreBackup...test execution.....",true);
		Reporter.log("======================================================");
		Reporter.log("firewallType.."+fireWallType+"datasourceid.."+dataSourceID,true);
		
		if(!fireWallType.equals("registrationToken")){
			
		//Check Datasource status and make sure the datasource status should be always in completed state
		Assert.assertEquals(getDatsourceStatus(dataSourceID), "Completed","Datasource status should be");

		Reporter.log("sourceID:"+dataSourceID,true);
		SortedSet<String> actulAnonyUsersSet=null;
		SortedSet<String> expectedAnonyUsersSet=null;
		//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		List<GoldenSetData> goldenSetDataList = null;
		AuditGoldenSetDataController3 controller=null;
		SortedSet<String> revealUsersSet=null;

		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAnonyUsersSet=goldenSetTestDataSetup.getExpectedAnonyUsers(goldenSetDataList);
			actulAnonyUsersSet=AuditTestUtils.getDatasourceAnonyUsers(dataSourceID,"86400","1394928000","1397519999");
			Assert.assertEquals(actulAnonyUsersSet.size(), expectedAnonyUsersSet.size(),"Anony User count is not Matched");
			
			revealUsersSet=AuditTestUtils.getRevealUsersSet(actulAnonyUsersSet,dataSourceID,fireWallType,suiteData.getDpoUsername(),suiteData.getSessionID());
			
			//compare both reveal user set with golden set users
			Reporter.log(fireWallType+"-expectedAnonyUsersSet:"+expectedAnonyUsersSet,true);
			Reporter.log(fireWallType+"-revealUsersSet:"+revealUsersSet,true);
			AuditTestUtils.compareExpectedUsersWithRevealUsers(revealUsersSet,expectedAnonyUsersSet);
		break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_BZ2:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAnonyUsersSet=goldenSetTestDataSetup.getExpectedAnonyUsers(goldenSetDataList);
			actulAnonyUsersSet=AuditTestUtils.getDatasourceAnonyUsers(dataSourceID,"86400","1432857600","1435449599");
			Assert.assertEquals(actulAnonyUsersSet.size(), expectedAnonyUsersSet.size(),"Anony User count is not Matched");
			
			revealUsersSet=AuditTestUtils.getRevealUsersSet(actulAnonyUsersSet,dataSourceID,fireWallType,suiteData.getDpoUsername(),suiteData.getSessionID());
			//compare both reveal user set with golden set users
			Reporter.log(fireWallType+"-expectedAnonyUsersSet:"+expectedAnonyUsersSet,true);
			Reporter.log(fireWallType+"-revealUsersSet:"+revealUsersSet,true);
			AuditTestUtils.compareExpectedUsersWithRevealUsers(revealUsersSet,expectedAnonyUsersSet);
			break;
			
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV:
		case AuditTestConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditTestConstants.FIREWALL_BE_PANCSV_BZ2:
		case AuditTestConstants.FIREWALL_BE_PANCSV_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAnonyUsersSet=goldenSetTestDataSetup.getExpectedAnonyUsers(goldenSetDataList);
			actulAnonyUsersSet=AuditTestUtils.getDatasourceAnonyUsers(dataSourceID,"86400","1377388800","1379980799");
			Assert.assertEquals(actulAnonyUsersSet.size(), expectedAnonyUsersSet.size(),"Anony User count is not Matched");
			
			revealUsersSet=AuditTestUtils.getRevealUsersSet(actulAnonyUsersSet,dataSourceID,fireWallType,suiteData.getDpoUsername(),suiteData.getSessionID());
			//compare both reveal user set with golden set users
			Reporter.log(fireWallType+"-expectedAnonyUsersSet:"+expectedAnonyUsersSet,true);
			Reporter.log(fireWallType+"-revealUsersSet:"+revealUsersSet,true);
			AuditTestUtils.compareExpectedUsersWithRevealUsers(revealUsersSet,expectedAnonyUsersSet);
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_BZ2:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAnonyUsersSet=goldenSetTestDataSetup.getExpectedAnonyUsers(goldenSetDataList);
			actulAnonyUsersSet=AuditTestUtils.getDatasourceAnonyUsers(dataSourceID,"86400","1412467200","1415059199");
			Assert.assertEquals(actulAnonyUsersSet.size(), expectedAnonyUsersSet.size(),"Anony User count is not Matched");
			
			revealUsersSet=AuditTestUtils.getRevealUsersSet(actulAnonyUsersSet,dataSourceID,fireWallType,suiteData.getDpoUsername(),suiteData.getSessionID());
			//compare both reveal user set with golden set users
			Reporter.log(fireWallType+"-expectedAnonyUsersSet:"+expectedAnonyUsersSet,true);
			Reporter.log(fireWallType+"-revealUsersSet:"+revealUsersSet,true);
			AuditTestUtils.compareExpectedUsersWithRevealUsers(revealUsersSet,expectedAnonyUsersSet);
			break;
			
		}
		}
		}
	}
	

	@Test(priority = 3, dependsOnMethods = "testSpanVAAgentRegistration")
	public void testRestoreButton() throws Exception {
		Reporter.log("Complited - testRestoreButton");
		Thread.sleep(10000);
		WebElement backup_restore_tab = driver.findElement(By.id(spanvaConfigProperties.getProperty("backup_restore_tab")));
		backup_restore_tab.click();
		Thread.sleep(10000);
		WebElement backUpRestoeServerName = driver.findElement(By.id(spanvaConfigProperties.getProperty("backUpRestoeServerName")));
		backUpRestoeServerName.clear();
		backUpRestoeServerName.sendKeys(spanvaConfigProperties.getProperty("backUpRestoeServerNameText"));
		WebElement backUpRestoeUserName = driver.findElement(By.id(spanvaConfigProperties.getProperty("backUpRestoeUserName")));
		backUpRestoeUserName.clear();
		backUpRestoeUserName.sendKeys(spanvaConfigProperties.getProperty("backUpRestoeUserNameText"));
		WebElement backUpRestoePassword = driver.findElement(By.id(spanvaConfigProperties.getProperty("backUpRestoePassword")));
		backUpRestoePassword.clear();
		backUpRestoePassword.sendKeys(spanvaConfigProperties.getProperty("backUpRestoePasswordText"));
		WebElement backUpRestoeTagPath = driver.findElement(By.id(spanvaConfigProperties.getProperty("backUpRestoeTagPath")));
		backUpRestoeTagPath.clear();
		backUpRestoeTagPath.sendKeys(spanvaConfigProperties.getProperty("backUpRestoeTagPathText"));
		Thread.sleep(5000);
		WebElement fetch_restore_list = driver.findElement(By.id(spanvaConfigProperties.getProperty("fetch_restore_list")));
		fetch_restore_list.click();
		Thread.sleep(50000);
		WebElement backup_restore_last_list = driver.findElement(By.cssSelector(spanvaConfigProperties.getProperty("backup_restore_last_list")));
		System.out.println("lasted link - " + backup_restore_last_list.getText());
		backup_restore_last_list.click();
		Thread.sleep(10000);
		WebElement restore_state = driver.findElement(By.id(spanvaConfigProperties.getProperty("restore-state")));
		restore_state.click();
		long totalWaitTime = 5 * 60 * 1000;
		System.out.println(": - :"+totalWaitTime);
		long currentWaitTime = 30000;
		while (currentWaitTime <= totalWaitTime) {
			currentWaitTime += 1000;
			System.out.println(": currentWaitTime :"+currentWaitTime);
			try{
				WebElement backUpRestoeAlertMsg = driver.findElement(By.cssSelector(spanvaConfigProperties.getProperty("backUpRestoeAlertMsg")));
				backUpRestoeAlertMsg.isEnabled();
				String msg=backUpRestoeAlertMsg.getText();
				break;
			}catch(Exception e){
				Reporter.log("Backup Restore alert not came, waiting for one second");
				Thread.sleep(1000);
			}
		}
		WebElement backUpRestoeAlertMsg = driver.findElement(By.cssSelector(spanvaConfigProperties.getProperty("backUpRestoeAlertMsg")));
		String msg=backUpRestoeAlertMsg.getText();
		Reporter.log("backUpRestoeAlertMsg : " +msg);
		Assert.assertEquals(spanvaConfigProperties.getProperty("backUpBackupAlertText"), msg,
				"State backup successful. - Not Showing it's showing "+msg);
		Reporter.log("Complited - testRestoreButton");

	}
	
	@Test(priority = 4, dataProvider = "backupDatasources", dependsOnMethods="testRestoreButton")
	public void testverifyTheDatasourceShouldnotenterintoProcessingStateAfterRestore(String fireWallType, String dataSourceID) throws Exception
	{
	if(!fireWallType.equals("registrationToken")){
		
		//Check Datasource status and make sure the datasource status should be always in completed state
		Assert.assertEquals(getDatsourceStatus(dataSourceID), "Completed","Datasource status should be");
	}
	}
/*
	@Test(priority = 4, dataProvider = "backupDatasources", dependsOnMethods="testRestoreButton")
	public void testUsersStateShouldBeActualBeforeRestoreBackup(String fireWallType, String dataSourceID) throws Exception
	{
		Reporter.log("testUsersStateShouldBeActualBeforeRestoreBackup...test execution.....",true);
		Reporter.log("======================================================");
		Reporter.log("firewallType.."+fireWallType+"datasourceid.."+dataSourceID,true);
		
		//Check Datasource status and make sure the datasource status should be always in completed state
		Assert.assertEquals(getDatsourceStatus(dataSourceID), "Completed","Datasource status should be");

		Reporter.log("sourceID:"+sourceID,true);
		SortedSet<String> actulAnonyUsersSet=null;
		SortedSet<String> expectedAnonyUsersSet=null;
		//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		List<GoldenSetData> goldenSetDataList = null;
		AuditGoldenSetDataController3 controller=null;
		SortedSet<String> revealUsersSet=null;

		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAnonyUsersSet=goldenSetTestDataSetup.getExpectedAnonyUsers(goldenSetDataList);
			actulAnonyUsersSet=AuditTestUtils.getDatasourceAnonyUsers(sourceID,"86400","1394928000","1397519999");
			Assert.assertEquals(actulAnonyUsersSet.size(), expectedAnonyUsersSet.size(),"Anony User count is not Matched");
			
			revealUsersSet=AuditTestUtils.getRevealUsersSet(actulAnonyUsersSet,sourceID,fireWallType,suiteData.getDpoUsername(),suiteData.getSessionID());
			
			//compare both reveal user set with golden set users
			Reporter.log(fireWallType+"-expectedAnonyUsersSet:"+expectedAnonyUsersSet,true);
			Reporter.log(fireWallType+"-revealUsersSet:"+revealUsersSet,true);
			compareExpectedUsersWithActualUsersAfterRestore(revealUsersSet,expectedAnonyUsersSet);
		break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_BZ2:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAnonyUsersSet=goldenSetTestDataSetup.getExpectedAnonyUsers(goldenSetDataList);
			actulAnonyUsersSet=AuditTestUtils.getDatasourceAnonyUsers(sourceID,"86400","1432857600","1435449599");
			Assert.assertEquals(actulAnonyUsersSet.size(), expectedAnonyUsersSet.size(),"Anony User count is not Matched");
			
			revealUsersSet=AuditTestUtils.getRevealUsersSet(actulAnonyUsersSet,sourceID,fireWallType,suiteData.getDpoUsername(),suiteData.getSessionID());
			//compare both reveal user set with golden set users
			Reporter.log(fireWallType+"-expectedAnonyUsersSet:"+expectedAnonyUsersSet,true);
			Reporter.log(fireWallType+"-revealUsersSet:"+revealUsersSet,true);
			compareExpectedUsersWithActualUsersAfterRestore(revealUsersSet,expectedAnonyUsersSet);
			break;
			
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV:
		case AuditTestConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditTestConstants.FIREWALL_BE_PANCSV_BZ2:
		case AuditTestConstants.FIREWALL_BE_PANCSV_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAnonyUsersSet=goldenSetTestDataSetup.getExpectedAnonyUsers(goldenSetDataList);
			actulAnonyUsersSet=AuditTestUtils.getDatasourceAnonyUsers(sourceID,"86400","1377388800","1379980799");
			Assert.assertEquals(actulAnonyUsersSet.size(), expectedAnonyUsersSet.size(),"Anony User count is not Matched");
			
			revealUsersSet=AuditTestUtils.getRevealUsersSet(actulAnonyUsersSet,sourceID,fireWallType,suiteData.getDpoUsername(),suiteData.getSessionID());
			//compare both reveal user set with golden set users
			Reporter.log(fireWallType+"-expectedAnonyUsersSet:"+expectedAnonyUsersSet,true);
			Reporter.log(fireWallType+"-revealUsersSet:"+revealUsersSet,true);
			compareExpectedUsersWithActualUsersAfterRestore(revealUsersSet,expectedAnonyUsersSet);
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_BZ2:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAnonyUsersSet=goldenSetTestDataSetup.getExpectedAnonyUsers(goldenSetDataList);
			actulAnonyUsersSet=AuditTestUtils.getDatasourceAnonyUsers(sourceID,"86400","1412467200","1415059199");
			Assert.assertEquals(actulAnonyUsersSet.size(), expectedAnonyUsersSet.size(),"Anony User count is not Matched");
			
			revealUsersSet=AuditTestUtils.getRevealUsersSet(actulAnonyUsersSet,sourceID,fireWallType,suiteData.getDpoUsername(),suiteData.getSessionID());
			//compare both reveal user set with golden set users
			Reporter.log(fireWallType+"-expectedAnonyUsersSet:"+expectedAnonyUsersSet,true);
			Reporter.log(fireWallType+"-revealUsersSet:"+revealUsersSet,true);
			compareExpectedUsersWithActualUsersAfterRestore(revealUsersSet,expectedAnonyUsersSet);
			break;
			
		}

		}
	}	*/

	public static void compareExpectedUsersWithActualUsersAfterRestore(SortedSet<String> expectedAnonyUsersSet,
			SortedSet<String> actulAnonyUsersSet)
	{
		SoftAssert softAssert=new SoftAssert();
		for(String actualUser:actulAnonyUsersSet)
		{

			softAssert.assertFalse(expectedAnonyUsersSet.contains(actualUser)," User "+actualUser +" not exist in the Expected Users List..");

		}
		softAssert.assertAll();
	}

	public Map<String,String> readBackupFile(String filePath) throws Exception{

		Map<String,String> finalMapBeforeranprovisioning=null;
		ObjectInputStream objInputStream=null;

		try{

			objInputStream  = new ObjectInputStream(new FileInputStream(new File(filePath)));  
			finalMapBeforeranprovisioning = (Map<String,String>)objInputStream.readObject();  
			System.out.println("finalMapBeforeprovisioning.."+finalMapBeforeranprovisioning);

			objInputStream.close();

		}catch(Exception ex)
		{


		}
		finally{

			objInputStream=null;
		}
		return finalMapBeforeranprovisioning;


	}


	public boolean eleExist(String updateEnabled) {
		boolean present;
		try {
			driver.findElement(By.xpath(updateEnabled));
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}
		return present;
	}
	public String getAgentRegistrationStatus() throws Exception {
		String agentRegistrationStatus = null;
		// String
		// agent_ip=spanvaConfigProperties.getProperty("spanva_agent_ip");
		String agent_ip = suiteData.getSpanvaIp();
		//String agent_name = spanvaConfigProperties.getProperty("spanva_name_value");
		String agent_name=suiteData.getSpanvaAgentName();

		HttpResponse getAllAgents = AuditFunctions.getAllAgentsList(restClient);
		Assert.assertEquals(getAllAgents.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strAgents = (ClientUtil.getResponseBody(getAllAgents));
		JSONArray agentsList = (JSONArray) new JSONObject(strAgents).getJSONArray("objects");
		int size = agentsList.length();
		for (int i = 0; i < size; i++) {
			JSONObject object = agentsList.getJSONObject(i);
			if (agent_ip.equals(object.get("agent_ip")) && agent_name.equals(object.get("agent_name"))) {

				agentRegistrationStatus = ((String) object.get("agent_state"));
				break;
			}

		}
		return agentRegistrationStatus;

	}

	public Map<String, String> getSpanvaAliveAgentOnThisInstance() throws Exception {

		String agent_state = "Alive";
		String agent_ip = suiteData.getSpanvaIp();
		String agent_name=suiteData.getSpanvaAgentName();


		Reporter.log("getting updated spanvaversion:", true);
		HttpResponse getAllAgents = AuditFunctions.getAllAgentsList(restClient);
		Assert.assertEquals(getAllAgents.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strAgents = (ClientUtil.getResponseBody(getAllAgents));
		// Reporter.log("strAgents:"+strAgents,true);
		JSONArray agentsList = (JSONArray) new JSONObject(strAgents).getJSONArray("objects");
		Map<String, String> agentInfoMap = new HashMap<String, String>();

		int size = agentsList.length();
		for (int i = 0; i < size; i++) {
			JSONObject object = agentsList.getJSONObject(i);

			if (agent_state.equals(object.get("agent_state")) && agent_ip.equals(object.get("agent_ip"))
					&& agent_name.equals(object.get("agent_name"))) {

				agentInfoMap.put("agent_name", (String) object.get("agent_name"));
				//agentInfoMap.put("updated_version", (String) object.get("update_version"));
				agentInfoMap.put("agent_id", (String) object.get("agent_id"));
				agentInfoMap.put("current_version", (String) object.get("version"));
				break;
			}

		}
		return agentInfoMap;

	}


}
