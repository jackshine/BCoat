package com.elastica.beatle.tests.audit.spanvatests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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
import org.openqa.selenium.JavascriptExecutor;
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

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class Audit_SpanVA_Backup extends AuditInitializeTests {

	protected Client restClient = null;
	protected String sourceID = null;
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
	private S3ActionHandler s3Handler;

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
	
	
	Map<String,String> backupConfigDataMap1=new HashMap<String,String>();
	
	String getRegKeyBackUpRestoreFilePath=null;
	
	String backuprestorefoldername;
	String backuprestoreFileName="";
	
	
	
	@BeforeClass(alwaysRun = true)
	public void initSpanVAUIConfiguration() throws Exception {
		Reporter.log("SpanVa Integration Tests started: ", true);
		// SpanVA properties loading
		spanvaConfigProperties = new Properties();
		spanvaConfigProperties
				.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.SPANVA_PROPS)));

		// load firewalls
		spanvaFirewalls = new Properties();
		spanvaFirewalls.load(new FileInputStream(
				FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.SPANVA_FIREWALLSLIST_GZ)));
		
	

		
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
			
			//getting the existing registration token
			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
			queryParam.add(new BasicNameValuePair("id", agentId));
			registrationToken=getRegistrationkey(queryParam);
			
		} else {// else create new agent registration

			Reporter.log("******Agent Registration process started***********", true);
			// generate new Registration token
			registrationToken = generateNewRegistrationToken();

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
		backupConfigDataMap1.put("registrationToken",registrationToken);
		Reporter.log("backupConfigDataMap1.. at register" + backupConfigDataMap1, true);

	}
	

	
	
	@DataProvider(name = "currentSpanvaScpDataSources", parallel = true)
	public Object[][] populateCurrentSpanvaScpDataSources() throws Exception {

		Object[][] inputData = new Object[spanvaFirewalls.size()][3];
		int j = 0;
		String firewallType;
		for (String key : spanvaFirewalls.stringPropertyNames()) {
			firewallType = spanvaFirewalls.getProperty(key);
			// Download file using amazon S3 URL
			AuditFunctions.DownloadFileFormS3(firewallType);
			Thread.sleep(30000);
			Reporter.log("SpanVA datasource creation test started on version:" + getCurrentSpanvaVersion()
					+ " for the firewall" + firewallType, true);

			spanVAPayload = AuditTestUtils.createSpanVAUploadBody(firewallType,
					getSpanvaAliveAgentOnThisInstance().get("agent_id"), suiteData.getEnvironmentName(),
					AuditTestConstants.AUDIT_SPANVA_DS_NAME);
			Reporter.log("firewallType spanVAPayload::" + firewallType + "---:" + spanVAPayload, true);

			firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(firewallType);
			// firewallLogFilePath=firewallLogPath;
			scpcompltedCheckEmptyFilePath = AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);

			// create spanva datasource
			HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,
					new StringEntity(spanVAPayload));
			Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
			Reporter.log("spanVAConnectionObject..." + spanVAConnectionObject, true);
			String dsName = (String) spanVAConnectionObject.get("name");
			Reporter.log("dsName::" + dsName, true);

			validateSpanVADataSource(spanVAConnectionObject);
			sourceID = (String) spanVAConnectionObject.get("id");
			JSONObject agentInfoObject = (JSONObject) spanVAConnectionObject.get("agent_info");
			String username = (String) agentInfoObject.get("user");
			String host = (String) agentInfoObject.get("host");
			String dest_dir = (String) agentInfoObject.get("dst_dir");

			sftpServerHost = getScpSftpServerHost();

			// getting data source from getDatasources list

			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
			queryParam.add(new BasicNameValuePair("fields", "datasources"));
			HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient, queryParam);
			Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

			String strDatataSourceListResp = ClientUtil.getResponseBody(datataSourceListResp);
			Reporter.log("strDatataSourceListResp:" + strDatataSourceListResp, true);

			JSONObject listObj = new JSONObject(strDatataSourceListResp);
			JSONObject tenantObj = listObj.getJSONObject("objects");
			JSONArray datasourcesList = tenantObj.getJSONArray("datasources");
			String datasourcename = null;
			for (int i = 0; i < datasourcesList.length(); i++) {
				datasourcename = ((JSONObject) datasourcesList.get(i)).getString("name");
				if (dsName.equals(datasourcename)) {
					sourceID = ((JSONObject) datasourcesList.get(i)).getString("id");
					break;
				}
			}

			// upload file to the span VA datasource
			Reporter.log("******************Upload file using Scp:****************************************", true);
			SFTPUtils sftpUtils = getSftpUtilsConfiguration(username, host, dest_dir);
			Reporter.log("******************scp upload detais:********************", true);
			Reporter.log("scpUsername:" + username, true);
			Reporter.log("scpPassword:" + sftpUtils.getPassWord(), true);
			Reporter.log("scpServerHost:" + host, true);
			Reporter.log("scpServerDestinationDir:" + dest_dir, true);
			Reporter.log("scpUploadedfirewallLogFilePath:" + firewallLogFilePath, true);
			FileInputStream fin;
			File file = new File(System.getProperty("user.dir") + firewallLogFilePath);
			fin = new FileInputStream(file);
			String result = "";
			Reporter.log("******************scp upload started:********************", true);
			result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
			Reporter.log("scp file upload status:  " + result, true);
			Reporter.log("******************scp upload completed sucessfully:********************", true);

			// upload completed check
			FileInputStream fuploadCompltedCheckInputStream;
			File uploadCompletedFile = new File(System.getProperty("user.dir") + scpcompltedCheckEmptyFilePath);
			fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
			String uploadCompletedStatus = "";
			Reporter.log("******************scp upload completed started:********************", true);
			result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
			Reporter.log("scp completed status:  " + uploadCompletedStatus, true);
			Reporter.log("******************SCP COMPLETED:********************", true);

			inputData[j][0] = firewallType;
			inputData[j][1] = dsName;
			inputData[j][2] = sourceID;
			backupConfigDataMap1.put(firewallType,sourceID);
			j++;
		}
		Reporter.log("inputData.." + inputData, true);
		Reporter.log("backupConfigDataMap1..at ds" + backupConfigDataMap1, true);

		return inputData;
	}

	@Test(priority = 3, dependsOnMethods = "testSpanVAAgentRegistration", dataProvider = "currentSpanvaScpDataSources", threadPoolSize = 7)
	public void testSpanVAScpDatasourcesVerification(String firewallType, String dsName, String datasourceid)
			throws Exception {
		Reporter.log(
				"firewallType:- " + firewallType + "datasource name:- " + dsName + " datasourceid:- " + datasourceid,
				true);
		currentVersionProcessedDS.put(firewallType, datasourceid);
		
		this.testDataSourceProcessAndMonitorLogsVerificationCheck(datasourceid);
	}
	
	
	@Test(priority = 4, dependsOnMethods = "testSpanVAScpDatasourcesVerification")
	public void uploadBackupRegistraionAndDss() throws Exception{
		
		//construct s3 folder name
		s3Handler = new S3ActionHandler();

		//Upload file to S3 folder
		s3BucketDetails=AuditTestUtils.getS3BucketDetails();
		
		getRegKeyBackUpRestoreFilePath=getOldRegKeyAndDssForBackupRestore(suiteData.getEnvironmentName());
		backuprestoreFileName=suiteData.getEnvironmentName()+"_old_regkey_dss_br_data.txt";
		backuprestorefoldername= suiteData.getEnvironmentName()+"_old_regkey_dss_br_data";
	
		
		writeBackupRestoreFile(backupConfigDataMap1, getRegKeyBackUpRestoreFilePath); //if current datasource is is completed we are writing this into new file.
		uploadFile(getRegKeyBackUpRestoreFilePath);
		
	}
	
	@Test(priority = 5, dependsOnMethods = "uploadBackupRegistraionAndDss")
    public void testBackupButton() throws Exception {
        Thread.sleep(10000);
        Reporter.log("Started - testBackupButton",true);
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
        
        List<WebElement> backup_restore_list_count_before = driver.findElements(By.cssSelector(spanvaConfigProperties.getProperty("backup_restore_list_count")));
        int restore_list_count_before = backup_restore_list_count_before.size();
        System.out.println("restore_list_count_before : "+restore_list_count_before);
        
        WebElement backUpRestoeBackup = driver.findElement(By.cssSelector(spanvaConfigProperties.getProperty("backUpRestoeBackup")));
        backUpRestoeBackup.click();
        Reporter.log("Clicked on BackUp Button",true);
        
        long totalWaitTime = 15 * 60 * 1000;
        System.out.println(": - :"+totalWaitTime);
        long currentWaitTime = 30000;
        while (currentWaitTime <= totalWaitTime) {
            currentWaitTime += 1000;
            System.out.println(": currentWaitTime :"+currentWaitTime);
            try{
            	WebElement backUpRestoeAlertMsg = driver.findElement(By.cssSelector(spanvaConfigProperties.getProperty("backUpRestoeAlertMsg")));
                backUpRestoeAlertMsg.isEnabled();
                String msg=backUpRestoeAlertMsg.getText();
                //System.out.println("backUpRestoeAlertMsg : " +msg);
                Reporter.log("backUpRestoeAlertMsg : " +currentWaitTime,true);
            	break;
            }catch(Exception e){
            	Reporter.log("Backup Restore alert not came, waiting for one second",true);
            	//System.out.println(": - :");
            	Thread.sleep(1000);
            }
        }
        WebElement backUpRestoeAlertMsg = driver.findElement(By.cssSelector(spanvaConfigProperties.getProperty("backUpRestoeAlertMsg")));
        String msg=backUpRestoeAlertMsg.getText();
        Reporter.log("backUpRestoeAlertMsg : " +msg,true);
        Assert.assertEquals(spanvaConfigProperties.getProperty("backUpRestoeAlertText"), msg,
                "State backup successful. - Not Showing it's showing "+msg);
        Thread.sleep(30000);
        List<WebElement> backup_restore_list_count_after = driver.findElements(By.cssSelector(spanvaConfigProperties.getProperty("backup_restore_list_count")));
        int restore_list_count_before_after = backup_restore_list_count_after.size();
        System.out.println("restore_list_count_before_after : "+restore_list_count_before_after);
      	Assert.assertEquals(restore_list_count_before_after,restore_list_count_before+1, "MissMatch with list for count restore list");
      	Reporter.log("Complited - testBackupButton",true);
        }

	
	public String getRegistrationkey(List<NameValuePair> queryParam ) throws Exception
	{
		HttpResponse response = AuditFunctions.getAgentRegistrationToken(restClient, queryParam);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String regTokenResp = (ClientUtil.getResponseBody(response));
		JSONObject getRegistrationTokenJsonObj = new JSONObject(regTokenResp).getJSONObject("object");
		Reporter.log("Registration Token jsob obj: " + regTokenResp, true);
		String registrationToken = (String) getRegistrationTokenJsonObj.get("token");
		return registrationToken;
		
	}
	
	public void testDataSourceProcessAndMonitorLogsVerificationCheck(String sourceID) throws Exception {
		ArrayList<String> finalMonitorLogElementsOfCompletedDS = new ArrayList<String>();
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		long totalWaitTime = 36000000;
		while (("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status)
				|| "Queued".equals(last_Status) || "Processing".equals(last_Status))
				&& currentWaitTime <= totalWaitTime) {
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of " + sourceID + " is " + last_Status, true);
			/*
			 * finalMonitorLogElementsOfCompletedDS.addAll(
			 * verifyMonitorLogsDuringDataSourceProcess(sourceID)); for(String
			 * str: finalMonitorLogElementsOfCompletedDS) {
			 * Assert.assertFalse(str.contains("Upload failed"),
			 * "Upload Message captured in the SpanVA Monitor Logs"); }
			 */
			if ("Completed".equals(last_Status) || "Failed".equals(last_Status))
				break;
		}
		Assert.assertTrue(currentWaitTime <= totalWaitTime,
				" File processing took " + (int) ((totalWaitTime / (1000 * 60)) % 60)
						+ " minutes. Current Datasource details id - status:[" + sourceID + "-" + last_Status + "]");

	}
	

	
	private void uploadFile(String fileTobeUploaded) throws Exception{
		s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
				backuprestorefoldername+"/",
				backuprestoreFileName, 
				new File(FileHandlingUtils.getFileAbsolutePath(fileTobeUploaded)));

	}

	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername, String sftpServerHost,
			String sftpServerDestinationDir) throws Exception {
		SFTPUtils sftpUtils = new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		// sftpUtils.setPassWord(AuditTestConstants.AUDIT_EOE_SPANVA_ING_SCP_PWD);
		sftpUtils.setPassWord(AuditTestUtils.getSpanVATenantScpPwd(suiteData.getTenantName()));
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
	}
	private void validateSpanVADataSource(JSONObject spanVAConnectionObject) throws Exception {
		Assert.assertEquals(spanVAConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);
		Assert.assertNotNull(spanVAConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) spanVAConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		// Assert.assertFalse(((String)
		// spanVAConnectionObject.get("datasource_format")).isEmpty(),"Data
		// source format is empty");
		// Assert.assertFalse(((String)
		// spanVAConnectionObject.get("datasource_type")).isEmpty(),"Data source
		// type is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_id"), "Agent Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_name"), "Agent Id is null");
		JSONObject agentInfoObject = (JSONObject) spanVAConnectionObject.get("agent_info");
		Assert.assertNotNull(agentInfoObject.get("host"), "Agent host is null");
		Assert.assertNotNull(agentInfoObject.get("log_collection_type"), "log_collection_type is null");
		Assert.assertNotNull(agentInfoObject.get("user"), "user is null");
		Assert.assertNotNull(agentInfoObject.get("dst_dir"), "dst_diris null");

	}
	public String getScpSftpServerHost() {
		String sftpServerHost = null;
		if (suiteData.getApiserverHostName().contains("qa-vpc")) {
			sftpServerHost = AuditTestConstants.AUDIT_QAVPC_SCP_SERVER;
		} else if (suiteData.getApiserverHostName().contains("api-vip.elastica.net")) {
			sftpServerHost = AuditTestConstants.AUDIT_SCP_SERVER_PROD;
		} else if (suiteData.getApiserverHostName().contains("api-cep.elastica.net")) {
			sftpServerHost = AuditTestConstants.AUDIT_SCP_SERVER_CEP;
		} else {
			sftpServerHost = AuditTestConstants.AUDIT_SCP_SERVER;
		}
		Reporter.log("scp/sftp serverhost: " + sftpServerHost, true);
		return sftpServerHost;
	}
	
	public String getCurrentSpanvaVersion() throws Exception {
		String upgradeTabLocatorXPath = "//*[@id='upgrade-tab']";
		WebElement upgradeTab = driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min

		// upgrade tab current version
		String currentVersionStrongLocator = ".upgrade-status>div>strong";
		WebElement currentversionStrongEle = driver.findElement(By.cssSelector(currentVersionStrongLocator));
		String currentVersionStrongLable = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionStrongEle);
		Reporter.log("Current SpanVA Image Version lable::" + currentVersionStrongLable, true);

		String currentVersionSpanLocator = ".upgrade-status>div>span";
		WebElement currentversionSpanEle = driver.findElement(By.cssSelector(currentVersionSpanLocator));
		String currentVersionSpanText = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionSpanEle);
		Reporter.log("Current SpanVA Image Version text::" + currentVersionSpanText, true);

		return currentVersionSpanText;
	}

	public void writeBackupRestoreFile(Map<String,String> backupConfigDataMap1, String filePath) throws Exception{


		File file=null;

		ObjectOutputStream objOutputStream=null;
		try{
			file = new File(
					FileHandlingUtils.getFileAbsolutePath(filePath));
			objOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objOutputStream.writeObject(backupConfigDataMap1);
			objOutputStream.flush();

		}catch(Exception ex)
		{


		}
		finally{
			file=null;
			objOutputStream=null;

		}
		
		Reporter.log("write finished",true);


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
	
	public String generateNewRegistrationToken() throws Exception {
		HttpResponse createResp = AuditFunctions.generateAgentRegistrationKey(restClient, new StringEntity("{}"));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject registrationKeyJsonResponse = (JSONObject) new JSONObject(ClientUtil.getResponseBody(createResp));
		Assert.assertNotNull(registrationKeyJsonResponse.get("registration_token"), "Registration token is null");
		String registrationToken = (String) registrationKeyJsonResponse.get("registration_token");
		Reporter.log("registrationToken: " + registrationToken, true);
		return registrationToken;

	}
	
	public String getOldRegKeyAndDssForBackupRestore(String envName){
		
		
	
		String getOldRegKeyAndDssForBackupRestoreLoc=null;
		switch(envName)
		{
		case "prod": {
			getOldRegKeyAndDssForBackupRestoreLoc=AuditTestConstants.AUDIT_PROD_OLD_REGKEY_DSS_BR_LOCATION_PATH;
			break;
		}
		case "cep": {
			getOldRegKeyAndDssForBackupRestoreLoc=AuditTestConstants.AUDIT_EU_OLD_REGKEY_DSS_BR_LOCATION_PATH;
			break;
		}
		case "qavpc": {
			getOldRegKeyAndDssForBackupRestoreLoc=AuditTestConstants.AUDIT_QAVPC_OLD_REGKEY_DSS_BR_LOCATION_PATH;
			break;
		}
		case "eoe": {
			getOldRegKeyAndDssForBackupRestoreLoc=AuditTestConstants.AUDIT_EOE_OLD_REGKEY_DSS_BR_LOCATION_PATH;
			break;
			
		}
		case "envX": {
			getOldRegKeyAndDssForBackupRestoreLoc=AuditTestConstants.AUDIT_CHA_OLD_REGKEY_DSS_BR_LOCATION_PATH;
			break;
		}
		default:
			break;
		}
		return getOldRegKeyAndDssForBackupRestoreLoc;

	}
	
	
/*
	@BeforeClass(alwaysRun = true)
	public void initSpanVAUIConfiguration() throws Exception {
		Reporter.log("SpanVa Integration Tests started: ", true);
		// SpanVA properties loading
		spanvaConfigProperties = new Properties();
		spanvaConfigProperties
				.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.SPANVA_PROPS)));

		// load firewalls
		spanvaFirewalls = new Properties();
		spanvaFirewalls.load(
				new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.SPANVA_FIREWALLSLIST_GZ)));

		Reporter.log("spanva ip>>:" + suiteData.getSpanvaIp(), true);

		Reporter.log("spanva upgraded version>>:" + suiteData.getSpanvaUpdatedVersion(), true);

		spanvaUpgradedVersion = suiteData.getSpanvaUpdatedVersion();

		// Declaring and initialising the HtmlUnitWebDriver
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setPreference("xpinstall.signatures.required", false);
		driver = new FirefoxDriver(firefoxProfile);

		// driver.get(spanvaConfigProperties.getProperty("spanva_agent_imageverion_ip"));
		driver.get("https://" + suiteData.getSpanvaIp() + "/site/login");

		restClient = new Client();
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
			registrationToken = generateNewRegistrationToken();

			WebElement spanvVaNameEle = driver.findElement(By.id(spanvaConfigProperties.getProperty("spanva_name")));
			// spanvVaNameEle.sendKeys(spanvaConfigProperties.getProperty("spanva_name_value"));
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

	public String getCurrentSpanvaVersion() throws Exception {
		String upgradeTabLocatorXPath = "//*[@id='upgrade-tab']";
		WebElement upgradeTab = driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min

		// upgrade tab current version
		String currentVersionStrongLocator = ".upgrade-status>div>strong";
		WebElement currentversionStrongEle = driver.findElement(By.cssSelector(currentVersionStrongLocator));
		String currentVersionStrongLable = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionStrongEle);
		Reporter.log("Current SpanVA Image Version lable::" + currentVersionStrongLable, true);

		String currentVersionSpanLocator = ".upgrade-status>div>span";
		WebElement currentversionSpanEle = driver.findElement(By.cssSelector(currentVersionSpanLocator));
		String currentVersionSpanText = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionSpanEle);
		Reporter.log("Current SpanVA Image Version text::" + currentVersionSpanText, true);

		return currentVersionSpanText;
	}

	@DataProvider(name = "currentSpanvaScpDataSources", parallel = true)
	public Object[][] populateCurrentSpanvaScpDataSources() throws Exception {

		Object[][] inputData = new Object[spanvaFirewalls.size()][3];
		int j = 0;
		String firewallType;
		for (String key : spanvaFirewalls.stringPropertyNames()) {
			firewallType = spanvaFirewalls.getProperty(key);
			Reporter.log("SpanVA datasource creation test started on version:" + getCurrentSpanvaVersion()
					+ " for the firewall" + firewallType, true);

			spanVAPayload = AuditTestUtils.createSpanVAUploadBody(firewallType,
					getSpanvaAliveAgentOnThisInstance().get("agent_id"), suiteData.getEnvironmentName(),
					AuditTestConstants.AUDIT_SPANVA_DS_NAME);
			Reporter.log("firewallType spanVAPayload::" + firewallType + "---:" + spanVAPayload, true);

			firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(firewallType);
			// firewallLogFilePath=firewallLogPath;
			scpcompltedCheckEmptyFilePath = AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);

			// create spanva datasource
			HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,
					new StringEntity(spanVAPayload));
			Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
			Reporter.log("spanVAConnectionObject..." + spanVAConnectionObject, true);
			String dsName = (String) spanVAConnectionObject.get("name");
			Reporter.log("dsName::" + dsName, true);

			validateSpanVADataSource(spanVAConnectionObject);
			sourceID = (String) spanVAConnectionObject.get("id");
			JSONObject agentInfoObject = (JSONObject) spanVAConnectionObject.get("agent_info");
			String username = (String) agentInfoObject.get("user");
			String host = (String) agentInfoObject.get("host");
			String dest_dir = (String) agentInfoObject.get("dst_dir");

			sftpServerHost = getScpSftpServerHost();

			// getting data source from getDatasources list

			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
			queryParam.add(new BasicNameValuePair("fields", "datasources"));
			HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient, queryParam);
			Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

			String strDatataSourceListResp = ClientUtil.getResponseBody(datataSourceListResp);
			Reporter.log("strDatataSourceListResp:" + strDatataSourceListResp, true);

			JSONObject listObj = new JSONObject(strDatataSourceListResp);
			JSONObject tenantObj = listObj.getJSONObject("objects");
			JSONArray datasourcesList = tenantObj.getJSONArray("datasources");
			String datasourcename = null;
			for (int i = 0; i < datasourcesList.length(); i++) {
				datasourcename = ((JSONObject) datasourcesList.get(i)).getString("name");
				if (dsName.equals(datasourcename)) {
					sourceID = ((JSONObject) datasourcesList.get(i)).getString("id");
					break;
				}
			}

			// upload file to the span VA datasource
			Reporter.log("******************Upload file using Scp:****************************************", true);
			SFTPUtils sftpUtils = getSftpUtilsConfiguration(username, host, dest_dir);
			Reporter.log("******************scp upload detais:********************", true);
			Reporter.log("scpUsername:" + username, true);
			Reporter.log("scpPassword:" + sftpUtils.getPassWord(), true);
			Reporter.log("scpServerHost:" + host, true);
			Reporter.log("scpServerDestinationDir:" + dest_dir, true);
			Reporter.log("scpUploadedfirewallLogFilePath:" + firewallLogFilePath, true);
			FileInputStream fin;
			File file = new File(System.getProperty("user.dir") + firewallLogFilePath);
			fin = new FileInputStream(file);
			String result = "";
			Reporter.log("******************scp upload started:********************", true);
			result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
			Reporter.log("scp file upload status:  " + result, true);
			Reporter.log("******************scp upload completed sucessfully:********************", true);

			// upload completed check
			FileInputStream fuploadCompltedCheckInputStream;
			File uploadCompletedFile = new File(System.getProperty("user.dir") + scpcompltedCheckEmptyFilePath);
			fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
			String uploadCompletedStatus = "";
			Reporter.log("******************scp upload completed started:********************", true);
			result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
			Reporter.log("scp completed status:  " + uploadCompletedStatus, true);
			Reporter.log("******************SCP COMPLETED:********************", true);

			inputData[j][0] = firewallType;
			inputData[j][1] = dsName;
			inputData[j][2] = sourceID;
			j++;
		}
		Reporter.log("inputData.." + inputData, true);

		return inputData;
	}

	@Test(priority = 3, dependsOnMethods = "testSpanVAAgentRegistration", dataProvider = "currentSpanvaScpDataSources", threadPoolSize = 7)
	public void testSpanVAScpDatasourcesVerification(String firewallType, String dsName, String datasourceid)
			throws Exception {
		Reporter.log(
				"firewallType:- " + firewallType + "datasource name:- " + dsName + " datasourceid:- " + datasourceid,
				true);
		currentVersionProcessedDS.put(firewallType, datasourceid);

		this.testDataSourceProcessAndMonitorLogsVerificationCheck(datasourceid);
	}

	@Test(priority = 4, dependsOnMethods = "testSpanVAScpDatasourcesVerification")
	public void testBackupButton() throws Exception {
		Thread.sleep(10000);
		WebElement backup_restore_tab = driver
				.findElement(By.id(spanvaConfigProperties.getProperty("backup_restore_tab")));
		backup_restore_tab.click();
		Thread.sleep(10000);
		WebElement backUpRestoeServerName = driver
				.findElement(By.id(spanvaConfigProperties.getProperty("backUpRestoeServerName")));
		backUpRestoeServerName.clear();
		backUpRestoeServerName.sendKeys(spanvaConfigProperties.getProperty("backUpRestoeServerNameText"));
		WebElement backUpRestoeUserName = driver
				.findElement(By.id(spanvaConfigProperties.getProperty("backUpRestoeUserName")));
		backUpRestoeUserName.clear();
		backUpRestoeUserName.sendKeys(spanvaConfigProperties.getProperty("backUpRestoeUserNameText"));
		WebElement backUpRestoePassword = driver
				.findElement(By.id(spanvaConfigProperties.getProperty("backUpRestoePassword")));
		backUpRestoePassword.clear();
		backUpRestoePassword.sendKeys(spanvaConfigProperties.getProperty("backUpRestoePasswordText"));
		WebElement backUpRestoeTagPath = driver
				.findElement(By.id(spanvaConfigProperties.getProperty("backUpRestoeTagPath")));
		backUpRestoeTagPath.clear();
		backUpRestoeTagPath.sendKeys(spanvaConfigProperties.getProperty("backUpRestoeTagPathText"));
		Thread.sleep(5000);

		List<WebElement> backup_restore_list_count_before = driver
				.findElements(By.cssSelector(spanvaConfigProperties.getProperty("backup_restore_list_count")));
		int restore_list_count_before = backup_restore_list_count_before.size();
		System.out.println("restore_list_count_before : " + restore_list_count_before);

		WebElement backUpRestoeBackup = driver
				.findElement(By.cssSelector(spanvaConfigProperties.getProperty("backUpRestoeBackup")));
		backUpRestoeBackup.click();

		long totalWaitTime = 5 * 60 * 1000;
		System.out.println(": - :" + totalWaitTime);
		long currentWaitTime = 30000;
		while (currentWaitTime <= totalWaitTime) {
			currentWaitTime += 1000;
			System.out.println(": currentWaitTime :" + currentWaitTime);
			try {
				WebElement backUpRestoeAlertMsg = driver
						.findElement(By.cssSelector(spanvaConfigProperties.getProperty("backUpRestoeAlertMsg")));
				backUpRestoeAlertMsg.isEnabled();
				String msg = backUpRestoeAlertMsg.getText();
				// System.out.println("backUpRestoeAlertMsg : " +msg);
				Reporter.log("backUpRestoeAlertMsg : " + msg);
				break;
			} catch (Exception e) {
				Reporter.log("Backup Restore alert not came, waiting for one second");
				// System.out.println(": - :");
				Thread.sleep(1000);
			}
		}
		WebElement backUpRestoeAlertMsg = driver
				.findElement(By.cssSelector(spanvaConfigProperties.getProperty("backUpRestoeAlertMsg")));
		String msg = backUpRestoeAlertMsg.getText();
		Reporter.log("backUpRestoeAlertMsg : " + msg);
		Assert.assertEquals(spanvaConfigProperties.getProperty("backUpRestoeAlertText"), msg,
				"State backup successful. - Not Showing it's showing " + msg);
		Thread.sleep(30000);
		List<WebElement> backup_restore_list_count_after = driver
				.findElements(By.cssSelector(spanvaConfigProperties.getProperty("backup_restore_list_count")));
		int restore_list_count_before_after = backup_restore_list_count_after.size();
		System.out.println("restore_list_count_before_after : " + restore_list_count_before_after);
		Assert.assertEquals(restore_list_count_before_after, restore_list_count_before + 1,
				"MissMatch with list for count restore list");

	}

	// private methods used in this class:

	private void validateSpanVADataSource(JSONObject spanVAConnectionObject) throws Exception {
		Assert.assertEquals(spanVAConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);
		Assert.assertNotNull(spanVAConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) spanVAConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		// Assert.assertFalse(((String)
		// spanVAConnectionObject.get("datasource_format")).isEmpty(),"Data
		// source format is empty");
		// Assert.assertFalse(((String)
		// spanVAConnectionObject.get("datasource_type")).isEmpty(),"Data source
		// type is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_id"), "Agent Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_name"), "Agent Id is null");
		JSONObject agentInfoObject = (JSONObject) spanVAConnectionObject.get("agent_info");
		Assert.assertNotNull(agentInfoObject.get("host"), "Agent host is null");
		Assert.assertNotNull(agentInfoObject.get("log_collection_type"), "log_collection_type is null");
		Assert.assertNotNull(agentInfoObject.get("user"), "user is null");
		Assert.assertNotNull(agentInfoObject.get("dst_dir"), "dst_diris null");

	}

	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername, String sftpServerHost,
			String sftpServerDestinationDir) throws Exception {
		SFTPUtils sftpUtils = new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		// sftpUtils.setPassWord(AuditTestConstants.AUDIT_EOE_SPANVA_ING_SCP_PWD);
		sftpUtils.setPassWord(AuditTestUtils.getSpanVATenantScpPwd(suiteData.getTenantName()));
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
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

	public boolean eleExistByCSSLocator(String updateEnabled) {
		boolean present;
		try {
			WebElement updatesEnabledEle = driver.findElement(By.cssSelector(updateEnabled));

			Reporter.log("updatesEnabledEle.isEnabled() in cssfunction:" + updatesEnabledEle.isEnabled(), true);
			Reporter.log("updatesEnabledEle.isDisplayed():" + updatesEnabledEle.isDisplayed(), true);
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}
		return present;
	}

	public boolean eleExistById(String updateEnabled) {
		boolean present;
		try {
			driver.findElement(By.id(updateEnabled));
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}
		return present;
	}

	public String generateNewRegistrationToken() throws Exception {
		HttpResponse createResp = AuditFunctions.generateAgentRegistrationKey(restClient, new StringEntity("{}"));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject registrationKeyJsonResponse = (JSONObject) new JSONObject(ClientUtil.getResponseBody(createResp));
		Assert.assertNotNull(registrationKeyJsonResponse.get("registration_token"), "Registration token is null");
		String registrationToken = (String) registrationKeyJsonResponse.get("registration_token");
		Reporter.log("registrationToken: " + registrationToken, true);
		return registrationToken;

	}

	public boolean isClickable(WebElement webe) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.elementToBeClickable(webe));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updatesEnabled() {
		boolean updatesEnabledFlag = false;
		if (driver.findElement(By.cssSelector(".upgrade-view>a")).getAttribute("class").contains("enabled")) {
			updatesEnabledFlag = true;
		}
		return updatesEnabledFlag;
	}

	public void mouseClickByLocator(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));

		Reporter.log("toString..." + element.toString(), true);
		Reporter.log("getAttribute..." + element.getAttribute("e-update-btn btn btn-success enabled"), true);
		Reporter.log("isEnabled..." + element.isEnabled(), true);
		Reporter.log("isDisplayed..." + element.isDisplayed(), true);
		Reporter.log("getText..." + element.getText(), true);

		Actions builder = new Actions(driver);
		builder.moveToElement(element).click(element);
		builder.perform();
	}

	public void testClick() throws InterruptedException {

		driver.manage().window().setSize(new Dimension(200, 200));

		List<WebElement> elements = driver.findElements(By.tagName("a"));

		Reporter.log("elements size..." + elements.size(), true);

		// check visibility
		for (WebElement element : elements) {
			Reporter.log("toString..." + element.toString(), true);
			Reporter.log("isDisplayed..." + element.isDisplayed(), true);
			// Assert.assertTrue(element.isDisplayed());
		}

		WebElement element = driver.findElement(By.cssSelector(".upgrade-view>a.e-update-btn.btn.btn-success.enabled"));

		Point point = element.getLocation();
		int xcord = point.getX();
		int ycord = point.getY();
		System.out.println(xcord + ", " + ycord);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

		// eleClick(element);

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
			System.out.println("Element is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element " + element + " was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Element " + element + " was not clickable " + e.getStackTrace());
		}
	}

	public Map<String, String> getUpdatedSpanVaAgentDetails() throws Exception {

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

			if (spanvaConfigProperties.getProperty("spanva_name_value").equals(object.get("agent_name"))) {

				agentInfoMap.put("agent_name", (String) object.get("agent_name"));
				agentInfoMap.put("updated_version", (String) object.get("update_version"));
				agentInfoMap.put("agent_id", (String) object.get("agent_id"));
				break;
			}

		}
		return agentInfoMap;

	}

	public String getAgentRegistrationStatus() throws Exception {
		String agentRegistrationStatus = null;
		// String
		// agent_ip=spanvaConfigProperties.getProperty("spanva_agent_ip");
		String agent_ip = suiteData.getSpanvaIp();
		// String agent_name =
		// spanvaConfigProperties.getProperty("spanva_name_value");
		String agent_name = suiteData.getSpanvaAgentName();

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
		String agent_name = suiteData.getSpanvaAgentName();

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
				// agentInfoMap.put("updated_version", (String)
				// object.get("update_version"));
				agentInfoMap.put("agent_id", (String) object.get("agent_id"));
				agentInfoMap.put("current_version", (String) object.get("version"));
				break;
			}

		}
		return agentInfoMap;

	}

	public List<String> verifyMonitorLogsDuringDataSourceProcess(String dataSourceID) throws Exception {
		WebElement monitorLogsTab = driver
				.findElement(By.xpath(spanvaConfigProperties.getProperty("spanva_monitor_logs_tab")));
		monitorLogsTab.click();

		Thread.sleep(5000);
		List<WebElement> elements = driver.findElements(By.cssSelector(".ui-widget-content.slick-row .slick-cell"));

		Reporter.log("monitor log elemnets::" + elements.size(), true);
		// WebElement
		// monitorLogsTab1=driver.findElement(By.cssSelector(".div.e-monitor-logs.slickgrid_729994.ui-widget>div.slick-viewport>div>div:nth-child(1)>div"));
		List<String> monitorLogsElementsList = new ArrayList<String>();
		String strMonitorLogElement = null;
		for (WebElement wele : elements) {
			strMonitorLogElement = wele.getText();
			if (strMonitorLogElement.contains(suiteData.getTenantName())
					|| strMonitorLogElement.contains(dataSourceID)) {
				monitorLogsElementsList.add(strMonitorLogElement);
			}

		}
		return monitorLogsElementsList;

	}

	public String getScpSftpServerHost() {
		String sftpServerHost = null;
		if (suiteData.getApiserverHostName().contains("qa-vpc")) {
			sftpServerHost = AuditTestConstants.AUDIT_QAVPC_SCP_SERVER;
		} else if (suiteData.getApiserverHostName().contains("api-vip.elastica.net")) {
			sftpServerHost = AuditTestConstants.AUDIT_SCP_SERVER_PROD;
		} else if (suiteData.getApiserverHostName().contains("api-cep.elastica.net")) {
			sftpServerHost = AuditTestConstants.AUDIT_SCP_SERVER_CEP;
		} else {
			sftpServerHost = AuditTestConstants.AUDIT_SCP_SERVER;
		}
		Reporter.log("scp/sftp serverhost: " + sftpServerHost, true);
		return sftpServerHost;
	}

	public void testDataSourceProcessAndMonitorLogsVerificationCheck(String sourceID) throws Exception {
		ArrayList<String> finalMonitorLogElementsOfCompletedDS = new ArrayList<String>();
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		long totalWaitTime = 36000000;
		while (("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status)
				|| "Queued".equals(last_Status) || "Processing".equals(last_Status))
				&& currentWaitTime <= totalWaitTime) {
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of " + sourceID + " is " + last_Status, true);
			
			 * finalMonitorLogElementsOfCompletedDS.addAll(
			 * verifyMonitorLogsDuringDataSourceProcess(sourceID)); for(String
			 * str: finalMonitorLogElementsOfCompletedDS) {
			 * Assert.assertFalse(str.contains("Upload failed"),
			 * "Upload Message captured in the SpanVA Monitor Logs"); }
			 
			if ("Completed".equals(last_Status) || "Failed".equals(last_Status))
				break;
		}
		Assert.assertTrue(currentWaitTime <= totalWaitTime,
				" File processing took " + (int) ((totalWaitTime / (1000 * 60)) % 60)
						+ " minutes. Current Datasource details id - status:[" + sourceID + "-" + last_Status + "]");

	}
	
	@AfterClass
	public void storeRegistrationKeyAndDatsource() throws Exception
	{
		
		
		
	}
*/
}
