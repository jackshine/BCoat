package com.elastica.beatle.tests.audit.spanvatests.SpanvaUpgradeTests;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;

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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditGoldenSetDataController3;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditSummary;
import com.elastica.beatle.audit.AuditSummaryTopRiskyServices;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.GoldenSetData;
import com.elastica.beatle.audit.SummaryTabDto;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class Audit_SpanVA_Scp_to_Upgraded_Scp_ScpClient_FtpClient_Https_Put_Post_Tests extends AuditInitializeTests {

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
	String spanvaUpgradedVersion=null;

	WebDriver driver = null;
	Properties scpdatasourcesProp=new Properties();
	Properties currentVersionProcessedDS=new Properties();
	
	

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
	
	@Test(dependsOnMethods={"testSpanVAScpDatasourcesVerification"})
	public void getUserAnonymizationBeforeUpgrade() throws Exception
	{
		
	}
	
	
	@Test(priority = 4, dependsOnMethods = "testSpanVAScpDatasourcesVerification")
	public void testVerifySpanvaLatestUpdates() throws Exception {
		String upgradeTabLocatorXPath = "//*[@id='upgrade-tab']";
		WebElement upgradeTab = driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min
		Reporter.log("upgrade tabe page source::" + driver.getPageSource(), true);

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

		// Install updates button enable status verification
		String strUpdatedEnabeldEleXPath = null;
		strUpdatedEnabeldEleXPath = spanvaConfigProperties.getProperty("spanva_updates_enabled_text");

		boolean updatesEnabledFlag = false;
		updatesEnabledFlag = updatesEnabled();
		Reporter.log("Download and Install Updates Button Enable status:" + updatesEnabledFlag, true);

		// get the current agent id
		// Reporter.log("configured AgentId.."+agentId,true);
		if (updatesEnabledFlag) {

			WebElement element = driver
					.findElement(By.cssSelector(".upgrade-view>a.e-update-btn.btn.btn-success.enabled"));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);

			Reporter.log("SpanVA updates installataion in progress..!", true);
			verifyUpdatesFinish(1200000); // 20 wait time for upgrades completion

			/*
			 * Thread.sleep(1200000);//wait here 20 mins for the updates
			 * verifyUpdates(); driver.navigate().refresh();
			 */

		} else {
			Thread.sleep(10000);
			updatesEnabledFlag = updatesEnabled();
			Reporter.log("Download and Install Updates Button Enable status inside else blk:" + updatesEnabledFlag,
					true);

			long currentWaitTime = 0;
			while ((!updatesEnabledFlag) && currentWaitTime <= 900000) {
				Thread.sleep(30000);
				currentWaitTime += 30000;
				updatesEnabledFlag = updatesEnabled();
				Reporter.log("Updates install button status: " + updatesEnabledFlag, true);
				if (updatesEnabledFlag)
					break;
			}

			Assert.assertEquals(updatesEnabledFlag, true,
					"updates button is not enabled after 30 min due to Updates are not available ");

			WebElement element = driver
					.findElement(By.cssSelector(".upgrade-view>a.e-update-btn.btn.btn-success.enabled"));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			Reporter.log("SpanVA updates installataion in progress..!");
			verifyUpdatesFinish(1200000); // 20 wait time for upgrades completion
			/*
			 * Thread.sleep(1200000);//wait here 20 mins for the updates
			 * driver.navigate().refresh(); verifyUpdates();
			 */

		}

	}

	

	@DataProvider(name = "scpSpanvaDataSourcesOnUpgradedVersion", parallel = true)
	public Object[][] populateUpgradedSpanvaSCPDataSources() throws Exception {

		String upgradeTabLocatorXPath = "//*[@id='upgrade-tab']";
		WebElement upgradeTab = driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min

		String currentVersionSpanLocator = ".upgrade-status>div>span";
		WebElement currentversionSpanEle = driver.findElement(By.cssSelector(currentVersionSpanLocator));
		String currentVersionSpanText = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionSpanEle);
		Reporter.log("Current SpanVA Image Version text::" + currentVersionSpanText, true);

		Object[][] inputData = new Object[spanvaFirewalls.size()][3];
		int j = 0;
		String firewallType;
		for (String key : spanvaFirewalls.stringPropertyNames()) {
			firewallType = spanvaFirewalls.getProperty(key);
			Reporter.log("SpanVA scp datasource creation test started on version:" + currentVersionSpanText
					+ " for the firewall" + firewallType, true);

			spanVAPayload = AuditTestUtils.createSpanVAUploadBody(firewallType,
					getSpanvaAliveAgentOnThisInstance().get("agent_id"), suiteData.getEnvironmentName(),
					AuditTestConstants.AUDIT_SPANVA_DS_NAME + "Upgraded");
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

	@Test(priority = 5, dependsOnMethods = "testVerifySpanvaLatestUpdates", dataProvider = "scpSpanvaDataSourcesOnUpgradedVersion", threadPoolSize = 7)
	public void testCreateSCPDatasourcesOnUpgradedVersion(String firewallType, String datasourcename,
			String datasourceId) throws Exception {
		Reporter.log("firewallType:- " + firewallType + " datasourcename:-" + datasourcename + " datasourceid:-"
				+ datasourceId, true);
		scpdatasourcesProp.put(firewallType, datasourceId);
		this.testDataSourceProcessAndMonitorLogsVerificationCheck(datasourceId);
	}
	
	@Test(dependsOnMethods={"testCreateSCPDatasourcesOnUpgradedVersion"})
	public void getUserAnonymizationAfterUpgrade() throws Exception
	{
		
	}
	
	
	/*@DataProvider(name = "test", parallel = true)
	public Object[][] populateUpgradedSpanvaScpDataSources1() throws Exception {
		
		Reporter.log("scpdatasourcesProp size.."+scpdatasourcesProp.size(),true);

		Object[][] inputData = new Object[scpdatasourcesProp.size()][2];
		int j = 0;
		for (String key : scpdatasourcesProp.stringPropertyNames()) {
			inputData[j][0] = key;
			inputData[j][1] = scpdatasourcesProp.getProperty(key);
			j++;
		}
		Reporter.log("inputData.." + inputData, true);

		return inputData;
	}

	
	

	@Test(priority=6,dependsOnMethods={"testVerifySpanvaLatestUpdates"},dataProvider = "test", threadPoolSize = 7)
	public void testAuditSummarySCPDatasourcesOnUpgradedVersion(String fireWallType, String datasourceid) throws Exception {
		
		sourceID=datasourceid;
		
		Reporter.log("******************AuditSummary Test started for :****************"+fireWallType+"sourceID:"+datasourceid,true);
		goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		//Reporter.log("fireWallType:"+fireWallType,true);
		//Reporter.log("sourceID:"+datasourceid,true);
		AuditSummary expectedAuditSummary=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;

		AuditSummary actualAuditSummary=null;

		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA+" Audit summary Info wrong ");
				
			}

			Reporter.log("*****************scp tests Regression: Audit summary Validation Errors for  "+fireWallType,true);
			for(String str: auditSummaryValidationsErrors)
			{
				Reporter.log(str,true);
			}	

			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_BZ2:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			if(AuditTestConstants.FIREWALL_BE_ZSCALAR.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR_7ZA+" Audit summary Info wrong ");
				
			}

			Reporter.log("*****************scp tests Regression: Audit summary Validation Errors for  "+fireWallType,true);
			for(String str: auditSummaryValidationsErrors)
			{
				Reporter.log(str,true);
			}	

			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV:
		case AuditTestConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditTestConstants.FIREWALL_BE_PANCSV_BZ2:
		case AuditTestConstants.FIREWALL_BE_PANCSV_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV+" Audit summary Info wrong ");
			
			if(AuditTestConstants.FIREWALL_BE_PANCSV.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_PANCSV_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_7ZA+" Audit summary Info wrong ");
				
			}

			Reporter.log("*****************scp tests Regression: Audit summary Validation Errors for  "+fireWallType,true);
			for(String str: auditSummaryValidationsErrors)
			{
				Reporter.log(str,true);
			}	

			
			break;
		}

		case AuditTestConstants.FIREWALL_BE_WSAW3C:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_BZ2:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
			
			if(AuditTestConstants.FIREWALL_BE_WSAW3C.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_WSAW3C_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C_7ZA+" Audit summary Info wrong ");
				
			}

			Reporter.log("*****************scp tests Regression: Audit summary Validation Errors for  "+fireWallType,true);
			for(String str: auditSummaryValidationsErrors)
			{
				Reporter.log(str,true);
			}	

			
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_7Z:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_BZ2:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_GZ:
		{

			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
			
			if(AuditTestConstants.FIREWALL_BE_WSAW3C.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_WSAW3C_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C_7ZA+" Audit summary Info wrong ");
				
			}
			
			break;
		
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_7Z:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_GZ:{
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_7Z:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_BZ2:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_GZ:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		}	
		

		
	}*/
	

	// https- put
	@DataProvider(name = "populateHttpsPutDataSources", parallel = true)
	public Object[][] populateUpgradedSpanvaHttpsPutDataSources() throws Exception {

		String upgradeTabLocatorXPath = "//*[@id='upgrade-tab']";
		WebElement upgradeTab = driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min

		String currentVersionSpanLocator = ".upgrade-status>div>span";
		WebElement currentversionSpanEle = driver.findElement(By.cssSelector(currentVersionSpanLocator));
		String currentVersionSpanText = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionSpanEle);
		Reporter.log("Current SpanVA Image Version text::" + currentVersionSpanText, true);

		scpcompltedCheckEmptyFilePath = AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);

		Object[][] inputData = new Object[spanvaFirewalls.size()][3];
		int j = 0;
		String firewallType;
		for (String key : spanvaFirewalls.stringPropertyNames()) {
			firewallType = spanvaFirewalls.getProperty(key);
			Reporter.log("SpanVA https put datasource creation test started on version:" + currentVersionSpanText
					+ " for the firewall" + firewallType, true);

			firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(firewallType);
			spanVAPayload = AuditTestUtils.createSpanVAUploadBody(firewallType,
					getSpanvaAliveAgentOnThisInstance().get("agent_id"), suiteData.getEnvironmentName(),
					AuditTestConstants.AUDIT_SPANVA_DS_NAME + "HTTPS_PUT_Upgraded");

			Reporter.log("Request Payload for SpanVA Https Datasource: " + spanVAPayload, true);
			HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,
					new StringEntity(spanVAPayload));
			Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));

			Reporter.log("Actual Datasource Response:" + spanVAConnectionObject, true);

			String dsName = (String) spanVAConnectionObject.get("name");
			Reporter.log("dsName::" + dsName, true);
			String expected_str_setup_by = suiteData.getUsername();
			String expectedResponse = " [log_transport=" + AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT
					+ ", DatasourceId=" + "DatasourceId is not null" + ", resource_uri=" + "resource_uri is not null"
					+ ", SetupBy=" + expected_str_setup_by + ", datasource_format=" + "datasource_format is not empty"
					+ ", agent_id=" + "agent_id is not empty" + ", agent_name=" + "agent_name is not empty"
					+ ", log_collection_type=" + "log_collection_type is not empty" + ", user=" + "user is not empty"
					+ ", dst_dir=" + "dst_dir is not empty" + ", datasource_type=" + "datasource_type is not empty"
					+ " ]";
			Reporter.log("Expected Datasource Response fields:" + expectedResponse, true);
			validateSpanVADataSource(spanVAConnectionObject);
			sourceID = (String) spanVAConnectionObject.get("id");
			JSONObject agentInfoObject = (JSONObject) spanVAConnectionObject.get("agent_info");

			Reporter.log("agentInfoObject:" + agentInfoObject, true);
			String username = (String) agentInfoObject.get("user");
			String host = (String) agentInfoObject.get("host");
			String dest_dir = (String) agentInfoObject.get("dst_dir");

			// getting data source from getDatasources list
			Reporter.log("getting the datasource from the datasources list:", true);
			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
			queryParam.add(new BasicNameValuePair("fields", "datasources"));
			HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient, queryParam);
			Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

			String strDatataSourceListResp = ClientUtil.getResponseBody(datataSourceListResp);
			Reporter.log("List of Datasources:" + strDatataSourceListResp, true);

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
			String stCmd = null;

			// execute curl command
			Reporter.log("fireWallType:-" + firewallType + "sourceID:-" + sourceID + " https source PUT", true);
			httpsPutFileTranser(firewallType, sourceID, username,
					AuditTestUtils.getSpanVATenantScpPwd(suiteData.getTenantName()), host, "20200", dest_dir,
					firewallLogFilePath, "log_destination" + "_" + sourceID + ".zip");

			SFTPUtils sftpUtils = getSftpUtilsConfiguration(username, host, dest_dir);
			String result = "";
			// upload completed check
			FileInputStream fuploadCompltedCheckInputStream;
			File uploadCompletedFile = new File(System.getProperty("user.dir") + scpcompltedCheckEmptyFilePath);
			fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
			String uploadCompletedStatus = "";
			Reporter.log("******************scp upload completed started:********************", true);
			result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
			Reporter.log("scp completed status:  " + uploadCompletedStatus, true);
			Reporter.log("******************SCP COMPLETED:********************", true);
			// upload conpleted check end

			Thread.sleep(10000);// wait 10 sec

			inputData[j][0] = firewallType;
			inputData[j][1] = dsName;
			inputData[j][2] = sourceID;
			j++;
		}
		Reporter.log("Https PUT Datasouces:- " + inputData, true);

		return inputData;
	}

	@Test(priority = 7, dependsOnMethods = "testVerifySpanvaLatestUpdates", dataProvider = "populateHttpsPutDataSources", threadPoolSize = 7)
	public void testCreateHTTPSPUTDatasourcesOnUpgradedVersion(String firewallType, String datasourcename,
			String datasourceId) throws Exception {
		Reporter.log("firewallType:- " + firewallType + " datasourcename:-" + datasourcename + " datasourceid:-"
				+ datasourceId, true);
		this.testDataSourceProcessAndMonitorLogsVerificationCheck(datasourceId);
	}
 
	@DataProvider(name = "populateHttpsPostDataSources", parallel = true)
	public Object[][] populateUpgradedSpanvaHttpsPostDataSources() throws Exception {
		String upgradeTabLocatorXPath = "//*[@id='upgrade-tab']";
		WebElement upgradeTab = driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min

		String currentVersionSpanLocator = ".upgrade-status>div>span";
		WebElement currentversionSpanEle = driver.findElement(By.cssSelector(currentVersionSpanLocator));
		String currentVersionSpanText = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionSpanEle);
		Reporter.log("Current SpanVA Image Version text::" + currentVersionSpanText, true);

		scpcompltedCheckEmptyFilePath = AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);

		Object[][] inputData = new Object[spanvaFirewalls.size()][3];
		int j = 0;
		String firewallType;
		for (String key : spanvaFirewalls.stringPropertyNames()) {
			firewallType = spanvaFirewalls.getProperty(key);
			Reporter.log("SpanVA https post datasource creation test started on version:" + currentVersionSpanText
					+ " for the firewall" + firewallType, true);

			firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(firewallType);
			spanVAPayload = AuditTestUtils.createSpanVAUploadBody(firewallType,
					getSpanvaAliveAgentOnThisInstance().get("agent_id"), suiteData.getEnvironmentName(),
					AuditTestConstants.AUDIT_SPANVA_DS_NAME + "HTTPS_POST_Upgraded");

			Reporter.log("Request Payload for SpanVA Https Datasource: " + spanVAPayload, true);
			HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,
					new StringEntity(spanVAPayload));
			Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));

			Reporter.log("Actual Datasource Response:" + spanVAConnectionObject, true);

			String dsName = (String) spanVAConnectionObject.get("name");
			Reporter.log("dsName::" + dsName, true);
			String expected_str_setup_by = suiteData.getUsername();
			String expectedResponse = " [log_transport=" + AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT
					+ ", DatasourceId=" + "DatasourceId is not null" + ", resource_uri=" + "resource_uri is not null"
					+ ", SetupBy=" + expected_str_setup_by + ", datasource_format=" + "datasource_format is not empty"
					+ ", agent_id=" + "agent_id is not empty" + ", agent_name=" + "agent_name is not empty"
					+ ", log_collection_type=" + "log_collection_type is not empty" + ", user=" + "user is not empty"
					+ ", dst_dir=" + "dst_dir is not empty" + ", datasource_type=" + "datasource_type is not empty"
					+ " ]";
			Reporter.log("Expected Datasource Response fields:" + expectedResponse, true);
			validateSpanVADataSource(spanVAConnectionObject);
			sourceID = (String) spanVAConnectionObject.get("id");
			JSONObject agentInfoObject = (JSONObject) spanVAConnectionObject.get("agent_info");

			Reporter.log("agentInfoObject:" + agentInfoObject, true);
			String username = (String) agentInfoObject.get("user");
			String host = (String) agentInfoObject.get("host");
			String dest_dir = (String) agentInfoObject.get("dst_dir");

			// getting data source from getDatasources list
			Reporter.log("getting the datasource from the datasources list:", true);
			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
			queryParam.add(new BasicNameValuePair("fields", "datasources"));
			HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient, queryParam);
			Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

			String strDatataSourceListResp = ClientUtil.getResponseBody(datataSourceListResp);
			Reporter.log("List of Datasources:" + strDatataSourceListResp, true);

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
			String stCmd = null;

			// execute curl command
			Reporter.log("fireWallType:-" + firewallType + "sourceID:-" + sourceID + " https source PUT", true);
			httpsPostFileTransfer(firewallType, sourceID, username,
					AuditTestUtils.getSpanVATenantScpPwd(suiteData.getTenantName()), host, "20200", dest_dir,
					firewallLogFilePath);

			SFTPUtils sftpUtils = getSftpUtilsConfiguration(username, host, dest_dir);
			String result = "";
			// upload completed check
			FileInputStream fuploadCompltedCheckInputStream;
			File uploadCompletedFile = new File(System.getProperty("user.dir") + scpcompltedCheckEmptyFilePath);
			fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
			String uploadCompletedStatus = "";
			Reporter.log("******************scp upload completed started:********************", true);
			result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
			Reporter.log("scp completed status:  " + uploadCompletedStatus, true);
			Reporter.log("******************SCP COMPLETED:********************", true);
			// upload conpleted check end

			Thread.sleep(10000);// wait 10 sec

			inputData[j][0] = firewallType;
			inputData[j][1] = dsName;
			inputData[j][2] = sourceID;
			j++;
		}
		Reporter.log("Https Post Datasouces:- " + inputData, true);

		return inputData;
	}

	@Test(priority = 8, dependsOnMethods = "testVerifySpanvaLatestUpdates", dataProvider = "populateHttpsPostDataSources", threadPoolSize = 7)
	public void testCreateHTTPSPOSTDatasourcesOnUpgradedVersion(String firewallType, String datasourcename,
			String datasourceId) throws Exception {
		Reporter.log("firewallType:- " + firewallType + " datasourcename:-" + datasourcename + " datasourceid:-"
				+ datasourceId, true);
		this.testDataSourceProcessAndMonitorLogsVerificationCheck(datasourceId);
	}

	
	public void populateSpanvaScpClientData(String fireWallType) throws Exception {

		Reporter.log("************Load SpanVa ScpClient Data*********************", true);
		InputStream inputStream = new FileInputStream(
				FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.AUDIT_CONFIGURED_AGENTS_PATH));
		spanvaconfigProps.load(inputStream);

		Reporter.log("**************Read SpanVa ScpClient configuration Data*********************", true);
		spanvaAwsNodeConnectPEMLocationPath = spanvaconfigProps.getProperty("spanva_nfs_server_cert_pem_location"); 
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
		spanvaScpClientUsername = spanvaconfigProps.getProperty("spanva_scp_client_username"); 
		spanVaScpClientUserPwd = spanvaconfigProps.getProperty("spanva_scp_client_userpwd"); 
		spanVaScpClientHost = spanvaconfigProps.getProperty("spanva_nfs_server_host");
		spanVaScpClientHostSourceDir = getScpClientSourceDirPath(fireWallType); 
		
		Reporter.log("****************Print spanva scpclient configuraion Details************************ ", true);
		Reporter.log("spanva spanvaAwsNodeConnectPEMLocationPath:- " + spanvaAwsNodeConnectPEMLocationPath, true);
		Reporter.log("spanva scpclient firewall location path:- " + firewallLogFilePath, true);
		Reporter.log("spanva scpclient Host:- " + spanVaScpClientHost, true);
		Reporter.log("spanva scpclient username:- " + spanvaScpClientUsername, true);
		Reporter.log("spanva scpclient pwd:- " + spanVaScpClientUserPwd, true);
		Reporter.log("spanva scpclient srcdir:- " + spanVaScpClientHostSourceDir, true);

		Reporter.log("Spanva ScpClient Datasource creation payload request******************", true);
		scpcompltedCheckEmptyFilePath = AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
		Reporter.log("Scp Completed Data file path***************" + scpcompltedCheckEmptyFilePath, true);

	}

	// upgrade tests for scp client
	@DataProvider(name = "populateScpClientDataSources", parallel = true)
	public Object[][] populateUpgradedSpanvaScpClientDataSources() throws Exception {

		String upgradeTabLocatorXPath = "//*[@id='upgrade-tab']";
		WebElement upgradeTab = driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min

		String currentVersionSpanLocator = ".upgrade-status>div>span";
		WebElement currentversionSpanEle = driver.findElement(By.cssSelector(currentVersionSpanLocator));
		String currentVersionSpanText = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionSpanEle);
		Reporter.log("Current SpanVA Image Version text::" + currentVersionSpanText, true);

		scpcompltedCheckEmptyFilePath = AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);

		Object[][] inputData = new Object[spanvaFirewalls.size()][3];
		int j = 0;
		String firewallType;
		for (String key : spanvaFirewalls.stringPropertyNames()) {
			firewallType = spanvaFirewalls.getProperty(key);
			Reporter.log("SpanVA https put datasource creation test started on version:" + currentVersionSpanText
					+ " for the firewall" + firewallType, true);
			this.populateSpanvaScpClientData(firewallType);

			spanVAPayload = AuditTestUtils.createSCPClientSUploadPayload(firewallType,
					getSpanvaAliveAgentOnThisInstance().get("agent_id"), suiteData.getEnvironmentName(),
					AuditTestConstants.AUDIT_SPANVA_DS_NAME + "SCP_CLIENT", spanVaScpClientHostSourceDir,
					spanVaScpClientHost, spanvaScpClientUsername, spanVaScpClientUserPwd);

			Reporter.log("NFS Request Payload: " + spanVAPayload, true);
			HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,
					new StringEntity(spanVAPayload));
			Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
			Reporter.log("Actual Datasource Response:" + spanVAConnectionObject, true);

			String dsName = (String) spanVAConnectionObject.get("name");
			Reporter.log("dsName::" + dsName, true);
			String expected_str_setup_by = suiteData.getUsername();
			String expectedResponse = " [log_transport=" + AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT
					+ ", DatasourceId=" + "DatasourceId is not null" + ", resource_uri=" + "resource_uri is not null"
					+ ", SetupBy=" + expected_str_setup_by + ", datasource_format=" + "datasource_format is not empty"
					+ ", agent_id=" + "agent_id is not empty" + ", agent_name=" + "agent_name is not empty"
					+ ", log_collection_type=" + "log_collection_type is not empty" + ", user=" + "user is not empty"
					+ ", dst_dir=" + "dst_dir is not empty" + ", datasource_type=" + "datasource_type is not empty"
					+ " ]";
			Reporter.log("Expected Datasource Response fields:" + expectedResponse, true);
			validateSpanVADataSource(spanVAConnectionObject);
			sourceID = (String) spanVAConnectionObject.get("id");
			JSONObject agentInfoObject = (JSONObject) spanVAConnectionObject.get("agent_info");

			Reporter.log("agentInfoObject:" + agentInfoObject, true);
			String host = (String) agentInfoObject.get("host");

			// getting data source from getDatasources list
			Reporter.log("getting the datasource from the datasources list:", true);
			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
			queryParam.add(new BasicNameValuePair("fields", "datasources"));
			HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient, queryParam);
			Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

			String strDatataSourceListResp = ClientUtil.getResponseBody(datataSourceListResp);
			Reporter.log("List of Datasources:" + strDatataSourceListResp, true);

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

			Reporter.log("******************Upload file using Scp:****************************************", true);
			SFTPUtils sftpUtils = getScpClientSftpUtilsConfiguration(spanvaScpClientUsername, spanVaScpClientUserPwd,
					spanVaScpClientHost, spanVaScpClientHostSourceDir);

			FileInputStream fin;
			File file = new File(System.getProperty("user.dir") + firewallLogFilePath);
			fin = new FileInputStream(file);
			String result = "";
			Reporter.log("******************scp upload started:********************", true);
			result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
			// result = sftpUtils.uploadNFSFile(file.getName(), fin,
			// true,spanvaAwsNodeConnectPEMLocationPath);

			Reporter.log("scp file upload status:  " + result, true);
			Reporter.log("******************scp upload completed sucessfully:********************", true);

			// SCPCOMPLETED CHECK
			Reporter.log("SCPCOMPLETED CHECK started******************************" + result, true);
			FileInputStream fuploadCompltedCheckInputStream;
			File uploadCompletedFile = new File(System.getProperty("user.dir") + scpcompltedCheckEmptyFilePath);
			fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
			String uploadCompletedStatus = "";
			result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
			// result = sftpUtils.uploadNFSFile(uploadCompletedFile.getName(),
			// fuploadCompltedCheckInputStream,
			// true,spanvaAwsNodeConnectPEMLocationPath);

			Reporter.log("SCPCOMPLETED status:  " + uploadCompletedStatus, true);
			Reporter.log("******************SSCPCOMPLETED completed successfuly********************", true);

			Thread.sleep(10000);// wait 10 sec

			inputData[j][0] = firewallType;
			inputData[j][1] = dsName;
			inputData[j][2] = sourceID;
			j++;
		}
		Reporter.log("ScpClient Datasouces:- " + inputData, true);

		return inputData;
	}

	//@Test(priority = 8, dependsOnMethods = "testVerifySpanvaLatestUpdates", dataProvider = "populateScpClientDataSources", threadPoolSize = 7)
	public void testCreateScpClientDatasourcesOnUpgradedVersion(String firewallType, String datasourcename,
			String datasourceId) throws Exception {
		Reporter.log("firewallType:- " + firewallType + " datasourcename:-" + datasourcename + " datasourceid:-"
				+ datasourceId, true);
		this.testDataSourceProcessAndMonitorLogsVerificationCheck(datasourceId);
	}

	// upgrade tests for ftp client

	public void populateSpanvaftpClientData(String fireWallType) throws Exception {

		Reporter.log("************Load SpanVa FTPClient Data*********************", true);
		InputStream inputStream = new FileInputStream(
				FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.AUDIT_CONFIGURED_AGENTS_PATH));
		spanvaconfigProps.load(inputStream);

		Reporter.log("**************Read SpanVa FtpClient configuration Data*********************", true);
		spanvaAwsNodeConnectPEMLocationPath = spanvaconfigProps.getProperty("spanva_nfs_server_cert_pem_location"); 
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
		spanvaFtpClientUsername = spanvaconfigProps.getProperty("spanva_ftp_client_username"); 
		spanVaFtpClientUserPwd = spanvaconfigProps.getProperty("spanva_ftp_client_userpwd"); 
		spanVaFtpClientHost = spanvaconfigProps.getProperty("spanva_nfs_server_host"); 
		spanVaFtpClientHostSourceDir = getFtpClientSourceDirPath(fireWallType);

		Reporter.log("****************Print spanva FtpClient configuraion Details************************ ", true);
		Reporter.log("spanva spanvaAwsNodeConnectPEMLocationPath:- " + spanvaAwsNodeConnectPEMLocationPath, true);
		Reporter.log("spanva ftpclient firewall location path:- " + firewallLogFilePath, true);
		Reporter.log("spanva ftpclient Host:- " + spanVaFtpClientHost, true);
		Reporter.log("spanva ftpclient username:- " + spanvaFtpClientUsername, true);
		Reporter.log("spanva ftpclient pwd:- " + spanVaFtpClientUserPwd, true);
		Reporter.log("spanva ftpclient srcdir:- " + spanVaFtpClientHostSourceDir, true);

		spanVAPayload = AuditTestUtils.createFTPClientSUploadPayload(fireWallType, agentId,
				suiteData.getEnvironmentName(), AuditTestConstants.AUDIT_SPANVA_DS_NAME + "_FTP_CLIENT",
				spanVaFtpClientHostSourceDir, spanVaFtpClientHost, spanvaFtpClientUsername, spanVaFtpClientUserPwd);

		scpcompltedCheckEmptyFilePath = AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
		Reporter.log("Scp Completed Data file path***************" + scpcompltedCheckEmptyFilePath, true);

	}

	@DataProvider(name = "populateFtpClientDataSources", parallel = true)
	public Object[][] populateUpgradedSpanvaFTPClientDataSources() throws Exception {

		String upgradeTabLocatorXPath = "//*[@id='upgrade-tab']";
		WebElement upgradeTab = driver.findElement(By.xpath(upgradeTabLocatorXPath));
		upgradeTab.click();

		Thread.sleep(60000); // sleep for 1 min

		String currentVersionSpanLocator = ".upgrade-status>div>span";
		WebElement currentversionSpanEle = driver.findElement(By.cssSelector(currentVersionSpanLocator));
		String currentVersionSpanText = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionSpanEle);
		Reporter.log("Current SpanVA Image Version text::" + currentVersionSpanText, true);

		scpcompltedCheckEmptyFilePath = AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);

		Object[][] inputData = new Object[spanvaFirewalls.size()][3];
		int j = 0;
		String firewallType;
		for (String key : spanvaFirewalls.stringPropertyNames()) {
			firewallType = spanvaFirewalls.getProperty(key);
			Reporter.log("SpanVA https put datasource creation test started on version:" + currentVersionSpanText
					+ " for the firewall" + firewallType, true);
			this.populateSpanvaftpClientData(firewallType);

			spanVAPayload = AuditTestUtils.createFTPClientSUploadPayload(firewallType, agentId,
					suiteData.getEnvironmentName(), AuditTestConstants.AUDIT_SPANVA_DS_NAME + "_FTP_CLIENT",
					spanVaFtpClientHostSourceDir, spanVaFtpClientHost, spanvaFtpClientUsername, spanVaFtpClientUserPwd);

			Reporter.log("FTP Client Request Payload: " + spanVAPayload, true);
			HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,
					new StringEntity(spanVAPayload));
			Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
			Reporter.log("Actual Datasource Response:" + spanVAConnectionObject, true);

			String dsName = (String) spanVAConnectionObject.get("name");
			Reporter.log("dsName::" + dsName, true);
			String expected_str_setup_by = suiteData.getUsername();
			String expectedResponse = " [log_transport=" + AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT
					+ ", DatasourceId=" + "DatasourceId is not null" + ", resource_uri=" + "resource_uri is not null"
					+ ", SetupBy=" + expected_str_setup_by + ", datasource_format=" + "datasource_format is not empty"
					+ ", agent_id=" + "agent_id is not empty" + ", agent_name=" + "agent_name is not empty"
					+ ", log_collection_type=" + "log_collection_type is not empty" + ", user=" + "user is not empty"
					+ ", dst_dir=" + "dst_dir is not empty" + ", datasource_type=" + "datasource_type is not empty"
					+ " ]";
			Reporter.log("Expected Datasource Response fields:" + expectedResponse, true);
			validateSpanVADataSource(spanVAConnectionObject);
			sourceID = (String) spanVAConnectionObject.get("id");
			JSONObject agentInfoObject = (JSONObject) spanVAConnectionObject.get("agent_info");

			Reporter.log("agentInfoObject:" + agentInfoObject, true);
			String host = (String) agentInfoObject.get("host");

			// getting data source from getDatasources list
			Reporter.log("getting the datasource from the datasources list:", true);
			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
			queryParam.add(new BasicNameValuePair("fields", "datasources"));
			HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient, queryParam);
			Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

			String strDatataSourceListResp = ClientUtil.getResponseBody(datataSourceListResp);
			Reporter.log("List of Datasources:" + strDatataSourceListResp, true);

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

			Reporter.log("******************Upload file using Scp:****************************************", true);
			SFTPUtils sftpUtils = getScpClientSftpUtilsConfiguration(spanvaScpClientUsername, spanVaScpClientUserPwd,
					spanVaScpClientHost, spanVaScpClientHostSourceDir);

			FileInputStream fin;
			File file = new File(System.getProperty("user.dir") + firewallLogFilePath);
			fin = new FileInputStream(file);
			String result = "";
			Reporter.log("******************scp upload started:********************", true);
			result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
			// result = sftpUtils.uploadNFSFile(file.getName(), fin,
			// true,spanvaAwsNodeConnectPEMLocationPath);

			Reporter.log("scp file upload status:  " + result, true);
			Reporter.log("******************scp upload completed sucessfully:********************", true);

			// SCPCOMPLETED CHECK
			Reporter.log("SCPCOMPLETED CHECK started******************************" + result, true);
			FileInputStream fuploadCompltedCheckInputStream;
			File uploadCompletedFile = new File(System.getProperty("user.dir") + scpcompltedCheckEmptyFilePath);
			fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
			String uploadCompletedStatus = "";
			result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
			// result = sftpUtils.uploadNFSFile(uploadCompletedFile.getName(),
			// fuploadCompltedCheckInputStream,
			// true,spanvaAwsNodeConnectPEMLocationPath);

			Reporter.log("SCPCOMPLETED status:  " + uploadCompletedStatus, true);
			Reporter.log("******************SSCPCOMPLETED completed successfuly********************", true);

			Thread.sleep(10000);// wait 10 sec

			inputData[j][0] = firewallType;
			inputData[j][1] = dsName;
			inputData[j][2] = sourceID;
			j++;
		}
		Reporter.log("ScpClient Datasouces:- " + inputData, true);

		return inputData;
	}

	//@Test(priority = 9, dependsOnMethods = "testVerifySpanvaLatestUpdates", dataProvider = "populateFtpClientDataSources", threadPoolSize = 7)
	public void testCreateFtpClientDatasourcesOnUpgradedVersion(String firewallType, String datasourcename,
			String datasourceId) throws Exception {
		Reporter.log("firewallType:- " + firewallType + " datasourcename:-" + datasourcename + " datasourceid:-"
				+ datasourceId, true);
		this.testDataSourceProcessAndMonitorLogsVerificationCheck(datasourceId);
	}
	
	
	//Utility methods

	private SFTPUtils getScpClientSftpUtilsConfiguration(String sftpTenantUsername, String userPwd,
			String sftpServerHost, String sftpServerDestinationDir) {
		SFTPUtils sftpUtils = new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		sftpUtils.setPassWord(userPwd);
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
	}

	public void httpsPostFileTransfer(String firewallType, String datasourceid, String dsUsername, String scppwd,
			String spanVaHost, String port, String destinationFolder, String firewallLogPath) throws Exception

	{
		String outputString;

		// curl command
		// curl -k -i -u ds_55bf01a9bf831217762dac78:f0yMP9cB -XPOST
		// https://10.0.62.12:20200/ds_55bf01a9bf831217762dac78/56aa9e2069533f0d2d165102
		// -F
		// file=@/Users/mallesh/Downloads/spanvahttpstest/bluecoat-vald-auto.log.zip

		StringBuffer sbCurl = new StringBuffer();
		sbCurl.append("curl -k -i -u ");
		sbCurl.append(dsUsername);
		sbCurl.append(":");
		sbCurl.append(scppwd);
		sbCurl.append(" -XPOST ");
		sbCurl.append("https://" + spanVaHost + ":" + port + destinationFolder);
		sbCurl.append(" -F");
		sbCurl.append(" file=@" + System.getProperty("user.dir") + firewallLogFilePath);
		// sbCurl.append(FileHandlingUtils.getFileAbsolutePath(firewallLogPath));

		String command = sbCurl.toString();
		Reporter.log(firewallType + " httpsPut FileTranser command::" + command, true);

		List<String> listFirewallUploadedStatus = new ArrayList<String>();
		Process curlProc;
		try {
			Thread.sleep(120000);
			curlProc = Runtime.getRuntime().exec(command);
			DataInputStream curlIn = new DataInputStream(curlProc.getInputStream());
			Reporter.log("firewallType upload through HttpsPost Response:", true);
			while ((outputString = curlIn.readLine()) != null) {
				listFirewallUploadedStatus.add(firewallType + "-" + datasourceid + ":-" + outputString);
				// Reporter.log(firewallType+"upload stat"outputString,true);
			}
			for (String str : listFirewallUploadedStatus) {
				Reporter.log(str, true);
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void httpsPutFileTranser(String firewallType, String datsourceid, String dsUsername, String scppwd,
			String spanVaHost, String port, String destinationFolder, String firewallLogPath, String logDestinationPath)
					throws Exception {

		String outputString;
		String[] strcmd2 = { "curl",
				"-k", "-i", "https://" + dsUsername + ":" + scppwd + "@" + spanVaHost + ":" + port + destinationFolder
						+ "/" + logDestinationPath,
				"--upload-file", System.getProperty("user.dir") + firewallLogFilePath };

		StringBuilder builder = new StringBuilder();
		for (String s : strcmd2) {
			builder.append(s + " ");
		}
		Reporter.log("https put command:" + builder.toString(), true);
		/* Create the ProcessBuilder */
		ProcessBuilder pb = new ProcessBuilder(strcmd2);
		pb.redirectErrorStream(true);

		/* Start the process */
		Thread.sleep(120000);
		Process proc = pb.start();
		System.out.println("Process started !");
		// Thread.sleep(60000);

		/* Read the process's output */
		String line;
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		Reporter.log("firewallType upload through HttpsPut Response:", true);
		while ((line = in.readLine()) != null) {
			System.out.println("" + line);
		}

		/* Clean-up */
		proc.destroy();
		System.out.println("Process ended !");

	}

	// @Test(priority=8,dependsOnMethods={"testDataSourceProcessAndMonitorLogsVerificationCheckAfterSpanVAUpdates"})
	public void TestAuditSummary() throws Exception {
		Reporter.log("Getting summary for " + FireWallType + " its ID is: " + sourceID, true);
		AuditGoldenSetDataController controller = null;
		String range = "1mo";
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response = AuditFunctions.getAuditSummary(restClient, queryParam);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response))
				.getJSONArray("objects").get(0);

		switch (FireWallType) {

		case AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG: {
			controller = new AuditGoldenSetDataController(AuditTestConstants.BLUECOATPROXY_DATA_SHEET);
			// goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(),
			// summaryObject,FireWallType,goldenSetErrorList);
			// Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG+"
			// Summary Results are wrong");
			break;
		}
		}
	}

	// @Test(priority=9,dependsOnMethods={"TestAuditSummary"})
	public void testAuditReport() throws Exception {
		Reporter.log("Getting Report for " + FireWallType + " its ID is: " + sourceID, true);
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		String range = "1mo";

		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response = AuditFunctions.getAuditReport(restClient, queryParam);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response))
				.getJSONArray("objects").get(0);
		Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
		Assert.assertEquals(reportObject.get("date_range"), range);
		Assert.assertNotNull(reportObject.get("earliest_date"), "earliest date is null");
		Assert.assertNotNull(reportObject.get("generated_date"), "earliest date is null");
		Assert.assertNotNull(reportObject.get("latest_date"), "Latest date is null");

	}

	// @Test(priority=10,dependsOnMethods={"testAuditReport"})
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("Deleting Data Source " + FireWallType + " its ID is: " + sourceID, true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
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

	private void validateNFSSpanVADataSource(JSONObject spanVAConnectionObject) throws Exception {
		Assert.assertEquals(spanVAConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);
		Assert.assertNotNull(spanVAConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) spanVAConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertFalse(((String) spanVAConnectionObject.get("datasource_format")).isEmpty(),
				"Data source format is empty");
		Assert.assertFalse(((String) spanVAConnectionObject.get("datasource_type")).isEmpty(),
				"Data source type is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_id"), "Agent Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_name"), "Agent Id is null");
		JSONObject agentInfoObject = (JSONObject) spanVAConnectionObject.get("agent_info");
		Assert.assertNotNull(agentInfoObject.get("host"), "Agent host is null");
		Assert.assertNotNull(agentInfoObject.get("log_collection_type"), "log_collection_type is null");
		// Assert.assertNotNull(agentInfoObject.get("user"), "user is null");
		// Assert.assertNotNull(agentInfoObject.get("dst_dir"), "dst_diris
		// null");

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

	private SFTPUtils getSftpUtilsConfigurationForNFS(String sftpTenantUsername, String sftpServerHost,
			String sftpServerDestinationDir) {
		SFTPUtils sftpUtils = new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
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

	private String spanvaCurrentVersion() throws Exception {
		String currentSpanvaversion = null;
		// Click upgrade tab
		String upgradeTabLocatorXPath = "//*[@id='upgrade-tab']";
		WebElement updates = driver.findElement(By.xpath(upgradeTabLocatorXPath));
		updates.click();

		Thread.sleep(5000); // sleep for 5 sec

		Reporter.log("upgrade tabe page source::" + driver.getPageSource());

		// upgrade tab current version
		String currentVersionStrongLocator = ".upgrade-status>div>strong";
		WebElement currentversionStrongEle = driver.findElement(By.cssSelector(currentVersionStrongLocator));
		String currentVersionStrongLable = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", currentversionStrongEle);
		Reporter.log("Current SpanVA Image Version lable::" + currentVersionStrongLable, true);

		String currentVersionSpanLocator = ".upgrade-status>div>span";
		WebElement currentversionSpanEle = driver.findElement(By.cssSelector(currentVersionSpanLocator));
		currentSpanvaversion = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;",
				currentversionSpanEle);
		Reporter.log("Current SpanVA Image Version text::" + currentVersionSpanText, true);
		return currentSpanvaversion;
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
	
	public boolean checkCurrentAndUpdatedVersionEqualityDuringInstallUpdatesInprogress() throws Exception {
		Map<String, String> getUpdatedSpanVADetails = getSpanvaAliveAgentOnThisInstance();
		boolean flag = false;
		String currentVersion = getUpdatedSpanVADetails.get("current_version");
		//String upgradedVersion = getUpdatedSpanVADetails.get("updated_version");
		String upgradedVersion = spanvaUpgradedVersion;
		
		Reporter.log("upgrade details*********************************************************", true);
		Reporter.log("upgradedVersion:-" + upgradedVersion + "currentVersion:-" + currentVersion, true);
		return currentVersion.equals(upgradedVersion);
	}

	public void verifyUpdatesFinish(long updatesWaiTime) throws Exception {

		long currentWaitTime = 0;
		boolean checkCurrentAndUpdatedVersionEqual = false;
		checkCurrentAndUpdatedVersionEqual = checkCurrentAndUpdatedVersionEqualityDuringInstallUpdatesInprogress();
		driver.navigate().refresh();

		while (!checkCurrentAndUpdatedVersionEqual && currentWaitTime <= updatesWaiTime) {
			Reporter.log("wait 300000 msec for updates...", true);
			Thread.sleep(300000);// wait for 5 mins
			currentWaitTime += 300000;
			driver.navigate().refresh();

			checkCurrentAndUpdatedVersionEqual = checkCurrentAndUpdatedVersionEqualityDuringInstallUpdatesInprogress();

			if (checkCurrentAndUpdatedVersionEqual) {
				break;
			} else {
				// Check the button enabled status if it is true then click
				// again for installUpdates: this loop is for 2time click
				boolean updatesEnabledFlag = false;
				updatesEnabledFlag = updatesEnabled();
				Reporter.log("Download and Install Updates Button Enable status:" + updatesEnabledFlag, true);

				if (updatesEnabledFlag) {
					WebElement element = driver
							.findElement(By.cssSelector(".upgrade-view>a.e-update-btn.btn.btn-success.enabled"));
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", element);
					Reporter.log("SpanVA updates installataion in progress..!", true);
				}
			}

		}
		Assert.assertTrue(checkCurrentAndUpdatedVersionEqualityDuringInstallUpdatesInprogress(),
				"Upgraded Version and Current Versions are not equal");

	}

	public void verifyUpdates() throws Exception {
		
		Thread.sleep(30000);
		Map<String, String> getUpdatedSpanVADetails = getSpanvaAliveAgentOnThisInstance();

		if (getUpdatedSpanVADetails.get("updated_version") != null
				&& (getUpdatedSpanVADetails.get("current_version") != null)) {
			String currentVersion = getUpdatedSpanVADetails.get("current_version");
			String upgradedVersion = getUpdatedSpanVADetails.get("updated_version");

			Reporter.log("upgrade details*********************************************************", true);
			Reporter.log("upgradedVersion:-" + upgradedVersion + "currentVersion:-" + currentVersion, true);
			Assert.assertEquals(upgradedVersion, currentVersion, "Upgraded Version and Current Versions are same");

			String updatedVersion = getSpanvaAliveAgentOnThisInstance().get("updated_version");
			Reporter.log("SpanVA Version is migrated to:" + updatedVersion, true);

			Assert.assertNotNull(updatedVersion, " Migrated SpanVa Image: ");

			spanVAUpdatedVersion = updatedVersion;
		} else {
			Assert.assertTrue(false, "SpanVa Status is disconnected:");

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

	public String[] getFirewallDestAndSrcMountDirArray(String firewallType) {
		String src_mount_dir = null;
		String base_dest_dir = null;
		String[] strNFSBaseAndMountDir = new String[2];

		switch (firewallType) {
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: {
			src_mount_dir = spanvaconfigProps.getProperty("spanva_nfs_src_mount_dir_bluecoat");
			strNFSBaseAndMountDir[0] = src_mount_dir;
			base_dest_dir = spanvaconfigProps.getProperty("spanva_nfs_base_dest_dir_bluecoat");
			strNFSBaseAndMountDir[1] = base_dest_dir;
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR: {
			src_mount_dir = spanvaconfigProps.getProperty("spanva_nfs_src_mount_dir_zscalarnss");
			strNFSBaseAndMountDir[0] = src_mount_dir;
			base_dest_dir = spanvaconfigProps.getProperty("spanva_base_dest_dir_zscalarnss");
			strNFSBaseAndMountDir[1] = base_dest_dir;
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV: {
			src_mount_dir = spanvaconfigProps.getProperty("spanva_nfs_src_mount_dir_pan_csv");
			strNFSBaseAndMountDir[0] = src_mount_dir;
			base_dest_dir = spanvaconfigProps.getProperty("spanva_base_dest_dir_pan_csv");
			strNFSBaseAndMountDir[1] = base_dest_dir;
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			src_mount_dir = spanvaconfigProps.getProperty("spanva_nfs_src_mount_dir_cisowsa_w3c");
			strNFSBaseAndMountDir[0] = src_mount_dir;
			base_dest_dir = spanvaconfigProps.getProperty("spanva_base_dest_dir_cisowsa_w3c");
			strNFSBaseAndMountDir[1] = base_dest_dir;
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES: {
			src_mount_dir = spanvaconfigProps.getProperty("spanva_nfs_src_mount_dir_ciscoasa");
			strNFSBaseAndMountDir[0] = src_mount_dir;
			base_dest_dir = spanvaconfigProps.getProperty("spanva_base_dest_dir_ciscoasa");
			strNFSBaseAndMountDir[1] = base_dest_dir;
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			src_mount_dir = spanvaconfigProps.getProperty("spanva_nfs_src_mount_dir_mcaWebGateWay");
			strNFSBaseAndMountDir[0] = src_mount_dir;
			base_dest_dir = spanvaconfigProps.getProperty("spanva_base_dest_dir_mcaWebGateWay");
			strNFSBaseAndMountDir[1] = base_dest_dir;
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED: {
			src_mount_dir = spanvaconfigProps.getProperty("spanva_nfs_src_mount_dir_websenseproxy_hosted");
			strNFSBaseAndMountDir[0] = src_mount_dir;
			base_dest_dir = spanvaconfigProps.getProperty("spanva_base_dest_dir_websenseproxy_hosted");
			strNFSBaseAndMountDir[1] = base_dest_dir;
			break;
		}
		}
		return strNFSBaseAndMountDir;
	}
	
	public String getScpClientSourceDirPath(String firewallType) {

		String logFileSourceDirPath = null;

		switch (firewallType) {
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_scp_client_bluecoat_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_scp_client_zscalar_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_scp_client_pan_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_scp_client_ciscoWsa_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_scp_client_ciscoAsa_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_scp_client_mcaWebGateWay_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_scp_client_websenseProxy_sourcedirpath");
			break;
		}
		}
		return logFileSourceDirPath;

	}


	public String getFtpClientSourceDirPath(String firewallType) {

		String logFileSourceDirPath = null;

		switch (firewallType) {
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_bluecoat_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_zscalar_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_pan_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_ciscoWsa_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_ciscoAsa_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_mcaWebGateWay_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED: {
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_websenseProxy_sourcedirpath");
			break;
		}
		}
		return logFileSourceDirPath;

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
	
	/*public AuditSummary populateActualAuditSummaryObject(String fireWallType, String sourceID ) throws Exception
	{

		String range ="";

		if(fireWallType.equals(AuditTestConstants.FIREWALL_SQUID_PROXY) ||
				fireWallType.equals(AuditTestConstants.FIREWALL_BE_WSA_ACCESS ) )
		{
			range="1y";
		}
		else{
			range = "1mo";	
		}
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);				
		Reporter.log("Actual Audit summar Api Response: "+fireWallType+"_"+sourceID+"::"+summaryObject,true);


		String summaryLabel;

		AuditSummary actualAuditSummaryObj=new AuditSummary();
		JSONObject companyBrr = summaryObject.getJSONObject("company_brr");
		String actual_audit_score= companyBrr.getString("value"); 
		actualAuditSummaryObj.setAuditScore(actual_audit_score);

		summaryLabel="total_services";
		String actual_audit_saasservices=summaryObject.getString(summaryLabel);
		actualAuditSummaryObj.setSaas_services_count(Integer.parseInt(actual_audit_saasservices));

		summaryLabel="total_destinations";
		String actual_audit_destinations=summaryObject.getString(summaryLabel);
		actualAuditSummaryObj.setDestination_count(Integer.parseInt(actual_audit_destinations));

		summaryLabel="total_users";
		String actual_audit_users=summaryObject.getString(summaryLabel);
		actualAuditSummaryObj.setUsers_count(Integer.parseInt(actual_audit_users));

		//Risky services
		String label="top_risky_services";
		int topRiskyServicesArrayLength=0;
		String top_risky_serviceBRR=null;
		String serviceId=null;
		String userCount=null;
		String serviceName=null;
		AuditSummaryTopRiskyServices auditSummaryTopRiskyServices=null;
		List<AuditSummaryTopRiskyServices> auditSummaryTopRiskyServicesList=new ArrayList<AuditSummaryTopRiskyServices>();
		Set<String> setServices=new HashSet<String>();
		List<String> totalAuditServicesList=new ArrayList<String>();;


		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topRiskyServicesArray = summaryObject.getJSONArray(label);
			topRiskyServicesArrayLength =topRiskyServicesArray.length();


			for(int i=0; i<topRiskyServicesArrayLength; i++)
			{
				auditSummaryTopRiskyServices= new AuditSummaryTopRiskyServices();
				serviceId=((JSONObject)topRiskyServicesArray.get(i)).getString("service_id");
				serviceName=AuditTestUtils.getServiceName(serviceId);
				auditSummaryTopRiskyServices.setServicename(serviceName);
				//setServices.add(serviceName);

				top_risky_serviceBRR=((JSONObject)topRiskyServicesArray.get(i)).getString("sort_key_brr");
				auditSummaryTopRiskyServices.setService_brr(top_risky_serviceBRR);

				userCount=((JSONObject)topRiskyServicesArray.get(i)).getString("users_count");
				auditSummaryTopRiskyServices.setService_user_count(Integer.parseInt(userCount));
				auditSummaryTopRiskyServicesList.add(auditSummaryTopRiskyServices);

			}
		}

		actualAuditSummaryObj.setSummaryTopRiskyServicesList(auditSummaryTopRiskyServicesList);

		//top used services
		label="top_used_services";
		int topUsedServicesArrayLength=0;

		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topUsedServicesArray = summaryObject.getJSONArray(label);
			topUsedServicesArrayLength =topUsedServicesArray.length();

			for(int i=0; i<topUsedServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topUsedServicesArray.get(i)).getString("service_id");
				serviceName=AuditTestUtils.getServiceName(serviceId);
				//setServices.add(serviceName);

			}

		}

		SummaryTabDto summaryTabDto=AuditTestUtils.getServicesTabData(sourceID);
		for(String serviceID:summaryTabDto.getServiceUsersMap().keySet())
		{
			serviceName=AuditTestUtils.getServiceName(serviceID);
			setServices.add(serviceName);
		}
		actualAuditSummaryObj.setTotalAuditServicesList(new ArrayList<>(setServices));


		label="high_risk_services";
		String actual_audit_high_risky_services=summaryObject.getString(label);
		actualAuditSummaryObj.setHigh_risky_services_count(Integer.parseInt(actual_audit_high_risky_services));


		label="med_risk_services";
		String actual_audit_med_risk_services=summaryObject.getString(label);
		actualAuditSummaryObj.setMed_risky_services_count(Integer.parseInt(actual_audit_med_risk_services));

		return actualAuditSummaryObj;

	}
	*/

	
	

}
