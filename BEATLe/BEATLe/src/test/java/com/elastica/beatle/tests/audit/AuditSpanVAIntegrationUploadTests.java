package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
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

public class AuditSpanVAIntegrationUploadTests extends AuditInitializeTests{
	
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
	
	WebDriver driver=null;
	
	

public AuditSpanVAIntegrationUploadTests(String FireWallName) {
	this.FireWallType = FireWallName;
	firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(FireWallType);
	
	}
  
@BeforeClass(alwaysRun = true)
public void initSpanVAUIConfiguration() throws Exception{
	Reporter.log("SpanVa Integration Tests started: ",true);
	//SpanVA properties loading
	spanvaConfigProperties=new Properties();
	spanvaConfigProperties.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.SPANVA_PROPS)));

	//Declaring and initialising the HtmlUnitWebDriver
	Logger logger = Logger.getLogger("");
	logger.setLevel(Level.OFF);
	java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 

	DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
	//capability.setJavascriptEnabled(true);
	capability.setBrowserName("htmlunit");
	capability.setVersion("internet explorer");
	capability.setPlatform(org.openqa.selenium.Platform.ANY);
	driver = new HtmlUnitDriver(capability);
	driver.get(spanvaConfigProperties.getProperty("spanva_agent_imageverion_ip"));

	restClient = new Client();
	registrationToken=loadRegistrationToken();
	  
	  }


public String loadRegistrationToken() throws Exception
{
	HttpResponse createResp = AuditFunctions.generateAgentRegistrationKey(restClient);
	Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
	JSONObject registrationKeyJsonResponse = (JSONObject) new JSONObject(ClientUtil.getResponseBody(createResp)).getJSONArray("objects").get(0);
	Assert.assertNotNull(registrationKeyJsonResponse.get("registration_token"), "Registration token is null");
	String registrationToken=(String)registrationKeyJsonResponse.get("registration_token");
	Reporter.log("registrationToken: "+registrationToken,true);
	return registrationToken;
	
}


@Test(priority=1)
public void testSpanVAHomePage() throws Exception{
	Thread.sleep(20000);
	WebElement username=driver.findElement(By.id(spanvaConfigProperties.getProperty("loginusername")));
	username.sendKeys(spanvaConfigProperties.getProperty("loginusernameFieldValue"));
	WebElement password=driver.findElement(By.id(spanvaConfigProperties.getProperty("loginpassword")));
	password.sendKeys(spanvaConfigProperties.getProperty("loginpasswordFieldValue"));
	WebElement button=driver.findElement(By.name(spanvaConfigProperties.getProperty("loginbutton")));
	button.click();
	Thread.sleep(5000);
	Assert.assertEquals(spanvaConfigProperties.getProperty("spava_homepage_title"), driver.getTitle(),"SpanVa home pag Title ");
	

	
	boolean upgradeTabCheck=eleExistById("agent-tab");
	if(upgradeTabCheck)
	{
		Reporter.log("Running test for SpanVA Image versions 2.41,2,42 and 2.44: ",true);
	}
	else{
		Reporter.log("Running test for SpanVA Image versions 2.46,2.47...: ",true);
		//change pwd screen pwd details
		WebElement updatedFormPwd=driver.findElement(By.id("settingsform-current_password"));
		updatedFormPwd.sendKeys(spanvaConfigProperties.getProperty("loginpasswordFieldValue"));
		WebElement updatedFormNewPwd=driver.findElement(By.id("settingsform-new_password"));
		updatedFormNewPwd.sendKeys(spanvaConfigProperties.getProperty("spanva_updatedpage_new_pwd"));
		WebElement updatedFormNewPwdRepeat=driver.findElement(By.id("settingsform-new_password_repeat"));
		updatedFormNewPwdRepeat.sendKeys(spanvaConfigProperties.getProperty("spanva_updatedpage_conf_new_pwd"));
		WebElement changePassword=driver.findElement(By.xpath(spanvaConfigProperties.getProperty("spanva_changepassword_button")));
		changePassword.click();
		
	}
	
	
	 
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
		Reporter.log("enter into agent registration if block",true);
		Reporter.log("******Registered Agent Verification***********",true);
		WebElement configuredAgentName=driver.findElement(By.xpath(agentName));
		Reporter.log("configuredAgentName text"+configuredAgentName.getText(),true);
		Assert.assertNotNull(driver.findElement(By.xpath(agentName)).getText(), "Agent name should be");
	}
	else{// else create new agent registration
		//Reporter.log("enter into agent registration else block to create new agent",true);
		Reporter.log("******Agent Registration process started***********",true);
		
		WebElement spanvVaNameEle=driver.findElement(By.id(spanvaConfigProperties.getProperty("spanva_name")));
		spanvVaNameEle.sendKeys(spanvaConfigProperties.getProperty("spanva_name_value"));
		WebElement spanvVaRegistrationTokenEle=driver.findElement(By.id(spanvaConfigProperties.getProperty("spanva_registration_token")));
		spanvVaRegistrationTokenEle.sendKeys(registrationToken);

		WebElement spanVARegisterAgentSubmit=driver.findElement(By.xpath(spanvaConfigProperties.getProperty("spanVARegisterAgentSubmit")));
		//Reporter.log("spanVARegisterAgentSubmit submit."+spanVARegisterAgentSubmit.getText(),true);

		spanVARegisterAgentSubmit.click();
		Thread.sleep(90000); //waiting time for agent registration process - 90 sec

		boolean agentExist_new=eleExist(agentName);
		boolean agentTokenExists_new=eleExist(agentRegistrationToken);
		Assert.assertEquals(agentExist_new, agentTokenExists_new," Agent Registered ");
		WebElement manageAgentEle=driver.findElement(By.xpath(spanvaConfigProperties.getProperty("spanva_agent_edit")));
		Assert.assertEquals(manageAgentEle.getText(), "Edit"," Edit Registration ");
		Reporter.log("***********Agent Registration process completed sucessfully********",true);
	}
	
}

@Test(priority=3, dependsOnMethods="testSpanVAAgentRegistration")
public void testSpanVALatestUpdates() throws Exception{
	//loading the upgrade tab
	WebElement upgradeTab=driver.findElement(By.xpath(spanvaConfigProperties.getProperty("spanva_upgrate_tab")));
	upgradeTab.click();

	Thread.sleep(5000);

	String strUpdatedEnabeldEleXPath=null;
	strUpdatedEnabeldEleXPath=spanvaConfigProperties.getProperty("spanva_updates_enabled_text");

	boolean updatesEnabledFlag=false;
	updatesEnabledFlag=eleExist(strUpdatedEnabeldEleXPath);
	Reporter.log("Download and Install Updates Button Enable status:"+updatesEnabledFlag,true);
	WebElement currentversion=null;

	currentversion=driver.findElement(By.cssSelector(".upgrade-view>.upgrade-status>div>span"));
	String currentVersionText=currentversion.getText();
	Reporter.log("Current SpanVA Image Version::"+currentVersionText,true);

	if(updatesEnabledFlag){
		//do nothing

		WebElement updatesEnabledEle=null;
		updatesEnabledEle=driver.findElement(By.xpath(strUpdatedEnabeldEleXPath));
		updatesEnabledEle.click();

		Thread.sleep(300000);//wait here 5 mins for the updates
		
		verifyUpdates(currentVersionText);


	}else{

		Thread.sleep(10000);
		WebElement link=driver.findElement(By.cssSelector(".upgrade-view>a"));
		Reporter.log("link text.."+link.getText(),true);
		String strUpdateDisabledEleXPath=spanvaConfigProperties.getProperty("spanva_updates_disabled_text");
		//Assert.assertEquals(true, eleExist(strUpdateDisabledEleXPath),"SpanVa Updates Available ");
		updatesEnabledFlag=false;
		updatesEnabledFlag=eleExist(strUpdatedEnabeldEleXPath);
		Reporter.log("updatesEnabledFlag."+updatesEnabledFlag,true);
		

		long currentWaitTime = 0;
		while( (!eleExist(strUpdatedEnabeldEleXPath)) && currentWaitTime <= 900000){
			Thread.sleep(30000);
			currentWaitTime += 30000;
			updatesEnabledFlag=eleExist(strUpdatedEnabeldEleXPath);
			Reporter.log("Updates install button status: "+ updatesEnabledFlag,true);
			if(updatesEnabledFlag)			
				break;
		}
		
		Assert.assertEquals(eleExist(strUpdatedEnabeldEleXPath), true,"Updates install button status should be:");
		
		WebElement updatesEnabledEle=null;
		updatesEnabledEle=driver.findElement(By.xpath(strUpdatedEnabeldEleXPath));
		updatesEnabledEle.click();
        Reporter.log("SpanVA updates installataion in progress..!");
		Thread.sleep(300000);//wait here 5 mins for the updates
		verifyUpdates(currentVersionText);


	}

	}
public void verifyUpdates(String currentVersion) throws Exception
{
    Thread.sleep(10000);
	WebElement updatedFormUsernme=driver.findElement(By.id("settingsform-current_password"));
	Assert.assertNotNull(updatedFormUsernme.getText(),"Updated Form username ");
	WebElement updatedFormNewPwd=driver.findElement(By.id("settingsform-new_password"));
	updatedFormNewPwd.sendKeys(spanvaConfigProperties.getProperty("spanva_updatedpage_new_pwd"));
	WebElement updatedFormNewPwdRepeat=driver.findElement(By.id("settingsform-new_password_repeat"));
	updatedFormNewPwdRepeat.sendKeys(spanvaConfigProperties.getProperty("spanva_updatedpage_conf_new_pwd"));
	//spanva_changepassword_button
	//WebElement changePassword=driver.findElement(By.xpath(".//*[@id='reset-password-form']/div[4]/div/button"));
	WebElement changePassword=driver.findElement(By.xpath(spanvaConfigProperties.getProperty("spanva_changepassword_button")));
	changePassword.click();
	
	Thread.sleep(30000);
	String updatedVersion=getUpdatedSpanVaAgentDetails().get("updated_version");
	Reporter.log("SpanVA Version is migrated from :"+currentVersion+"to "+updatedVersion,true);
	
	Assert.assertNotEquals(currentVersion,updatedVersion," Migrated SpanVa Image: ");
	
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

  
 @Test(priority=4, dependsOnMethods="testSpanVALatestUpdates")
  public void testSpanVADatasourceCreation() throws Exception
  {
	  Reporter.log("SpanVA datasource creation test started on version:"+spanVAUpdatedVersion,true);
	  agentId=getUpdatedSpanVaAgentDetails().get("agent_id");
	  spanVAPayload = AuditTestUtils.createSpanVAUploadBody(FireWallType,agentId,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SPANVA_DS_NAME);
	  
	  //create spanva datasource
		HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,new StringEntity(spanVAPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		String dsName=(String)spanVAConnectionObject.get("name");
		Reporter.log("dsName::"+dsName,true);
		
		validateSpanVADataSource(spanVAConnectionObject);
		sourceID = (String) spanVAConnectionObject.get("id");
		JSONObject agentInfoObject=(JSONObject) spanVAConnectionObject.get("agent_info");
		String username=(String)agentInfoObject.get("user");
		String host=(String)agentInfoObject.get("host");
		String dest_dir=(String)agentInfoObject.get("dst_dir");
		
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
	    SFTPUtils sftpUtils=getSftpUtilsConfiguration(username, host,dest_dir);
		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
        result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
        Reporter.log("sftp file upload status:  " + result,true);
        
        //verification logfile process attached to the datasource
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
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))			
				break;
		}
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_SCP_FILE_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);

     
  }
  

@Test(dependsOnMethods={"testSpanVADatasourceCreation"})	
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
			goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,FireWallType,goldenSetErrorList);
			Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG+" Summary Results are wrong");
			break;
		}}
	}
	
	@Test(dependsOnMethods={"TestAuditSummary"})
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
		
		/*
		 * Need to validate other parameters. Get the data from Bhaskar
		 */
	}
	
	/*
	 * This test case deletes the data source
	 */
	@Test(dependsOnMethods={"testAuditReport"})
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("Deleting Data Source "+ FireWallType +" its ID is: "+sourceID, true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
	}
  
  
  
private void validateSpanVADataSource(JSONObject spanVAConnectionObject)
			throws Exception {
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
        sftpUtils.setPassWord(AuditTestConstants.AUDIT_SPANVA_TENANT_PWD);
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

@AfterClass
public void testPopulateAuditSummaryFailures() throws Exception
{
	Reporter.log("*****************SCP Logs: Audit Summary Validation Errors***********************",true);
	
	 for(String str: goldenSetErrorList)
	  {
		  Reporter.log(str,true);
	  }
}
 
}

