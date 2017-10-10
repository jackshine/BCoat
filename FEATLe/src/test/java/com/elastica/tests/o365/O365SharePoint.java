package com.elastica.tests.o365;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;

public class O365SharePoint extends GWCommonTest{

	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=null;
	String user=null;
	SoftAssert softAssert = new SoftAssert();
	Client restClient= new Client();
	CiqUtils utils=new CiqUtils();
	HashMap<String, String> terms =new HashMap<String, String> ();
	
	@BeforeClass(alwaysRun= true)
	public void clearDataMap() throws Exception{
		
		user=CIQConstants.SHARED_USER;	
		Reporter.log("create ciq risk profile", true);
		Reporter.log("Completed -- profile creation", true);
		terms.put("__source", "GW");
		terms.put("facility", "Sites");
	}

	@Priority(1)
	@Test(groups ={"SANITY","P1","REGRESSION"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Reporter.log("Finished login activities on cloudSoc", true);
	}

	@Priority(2)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"}) 
	public void o365SitesValidateLoginActivityEvent() throws Exception {
		fromTime=backend.getCurrentTime();
		Reporter.log("Verifying the login event", true);
		o365Login.login(getWebDriver(), suiteData);
 		o365HomeAction.loadSharePointApp(getWebDriver());
		o365HomeAction.spTeamSite(getWebDriver());
		Thread.sleep(15000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Login event verification successful");
		Logger.info("==================================================================================");
		Reporter.log("Login event verification successfull", true);
	}

   @Priority(3)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateUpload() throws Exception {
	   Logger.info("==================================================================================");
		Logger.info("File upload  Verification test");
		Logger.info("==================================================================================");
		String file=CIQConstants.FILE_DOWNLOAD;
		Reporter.log("delete old file from  saas app ", true);
		deletefile(file);
		Thread.sleep(15000);
		Reporter.log("Completed: delete old file from  saas app ", true);	
		Reporter.log("Performing saas app file upload action for file "+file, true);
		String filePath=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"ciq"+File.separator+"contentType"+File.separator ;
		o365HomeAction.spTeamSiteUpload(getWebDriver(),filePath+file);
		Reporter.log("Completed Performing saas app file upload action for file "+file, true);	
		expectedDataMap.clear();	
		setCommonFieldsInExpectedDataMap();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file named "+file);	
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),file+"File upload Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload Log Verification Successfull");
		Logger.info("==================================================================================");

	}

	@Priority(4)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateDownload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("File download  Verification Test");
		Logger.info("==================================================================================");

		String file=CIQConstants.RISK_HDFC;
		try{
		boolean status=	o365HomeAction.selectSPFilebyName(getWebDriver(), file);
		  if(status)
		  {
			  o365HomeAction.downloadSPFile(getWebDriver());
				Thread.sleep(5000);
				o365HomeAction.refresh(getWebDriver(), 5);
		  }
		  else
		  {
			  Reporter.log("file not present for download ", true);
		  }		
		}
		catch(Exception e) {
			Reporter.log("error in download  the file "+file, true);
		}
		Thread.sleep(30000);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded "+file+" from app Shared Documents");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Completed file download Verification test");
		Logger.info("==================================================================================");
	}
	@Priority(5)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateFileShare() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("File share  Verification Test");
		Logger.info("==================================================================================");
				
		String files=CIQConstants.RISK_HDFC;
		o365HomeAction.selectSPFilebyName(getWebDriver(), files);
		o365HomeAction.shareSPFile(getWebDriver(),user);
		Thread.sleep(30000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared file/folder \""+files+"\" with \""+user+"\" of Team Site");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("completed File share  Verification Test");
		Logger.info("==================================================================================");

	}
	
	


	@Priority(6)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateFileDelete() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("file delete Verification Test");
		Logger.info("==================================================================================");
		//Thread.sleep(15000);
		String  file=CIQConstants.FILE_DOWNLOAD;

		try{
			deletefile(file);
			Thread.sleep(5000);
			String filePath=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
					"resources"+File.separator+"ciq"+File.separator+"contentType"+File.separator ;
			o365HomeAction.spTeamSiteUpload(getWebDriver(),filePath+file);

			Thread.sleep(15000);
			deletefile(file);
			Thread.sleep(5000);
		}
		catch(Exception e) {

			Reporter.log("error in uploading  the file "+file, true);
		}
		Thread.sleep(30000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, " deleted "+file+" from Team Site");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("file delete Log Verification Successfull");
		Logger.info("==================================================================================");

	}
	
	@Priority(6)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateFolderDelete() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Folder delete Log Verification test");
		Logger.info("==================================================================================");
		String  Folder=CIQConstants.FOLDER_DELETE;
		try{
			deletefile(Folder);
			Thread.sleep(5000);
			o365HomeAction.refresh(getWebDriver(), 5);		
			o365HomeAction.CreateNewFolder(getWebDriver(), Folder);
			Thread.sleep(5000);
			deletefile(Folder);	
		}		
		catch(Exception e) {

			Reporter.log("error in uploading  the file "+Folder, true);
		}
		Thread.sleep(10000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted "+Folder+" from Team Site");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Folder delete Log Verification Successfull");
		Logger.info("==================================================================================");

	}
	
	@Priority(7)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateCreateNewFolder() throws Exception {
		String  Folder="ABC";

		try{
			o365HomeAction.CreateNewFolder(getWebDriver(), Folder);
			Thread.sleep(10000);
			//o365HomeAction.refresh(getWebDriver(), 5);
		}
		catch(Exception e) {

			Reporter.log("error in uploading  the file "+Folder, true);
		}
	//	Thread.sleep(30000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created Folder "+Folder+" on Team Site");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");
		//
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");

	}
	
	@Priority(7)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateFolderShare() throws Exception {
		String  Folder="Shared Folder";
		try{
			deletefile(Folder);
			Thread.sleep(5000);
			o365HomeAction.CreateNewFolder(getWebDriver(), Folder);		
			Thread.sleep(5000);
			o365HomeAction.selectSPFilebyName(getWebDriver(),Folder);			
			Thread.sleep(5000);
			o365HomeAction.shareSPFile(getWebDriver(),user);
		}
		catch(Exception e) {

			Reporter.log("Folder share not successful for folder: "+Folder, true);
		}
		Thread.sleep(5000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared file/folder "+Folder+" with "+user+" of Team Site");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");

	}

	@Priority(10)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateFileDocRename() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Document file rename  Log Verification test");
		Logger.info("==================================================================================");

		String renfiles="Renameddoc";
		try{
		o365HomeAction.deleteFile(getWebDriver(), renfiles);
		o365HomeAction.CreateNewDoc(getWebDriver(), CIQConstants.DOC_BODY,user);
		o365HomeAction.renameDocFile(getWebDriver(), renfiles);
		Thread.sleep(5000);
		}
		catch(Exception e) {
			Reporter.log("error in renaming  file ", true);
		}
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed word document to "+renfiles+" in browser");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs for Document share does not match");

		Logger.info("==================================================================================");
		Logger.info("Document file rename Log Verification successfull");
		Logger.info("==================================================================================");

	}
	
	@Priority(9)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateCreateDocNShare() throws Exception {
		String docname ="Document1";
		try{
			o365HomeAction.deleteFile(getWebDriver(), docname);
			o365HomeAction.CreateNewDoc(getWebDriver(), CIQConstants.DOC_BODY,user);
			Thread.sleep(5000);
		}
		catch(Exception e) {
			Reporter.log("error in uploading  the file ", true);
		}
		Thread.sleep(30000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared file/folder Document1 with "+user+" of Team Site");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");

		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new word document named Document6.docx in browser");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("create and share doc  Log Verification Successfull");
		Logger.info("==================================================================================");

	}
	
	
	
	@Priority(8)
	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateSiteShare() throws Exception {
			String  body=CIQConstants.DOC_BODY;		
			
		try{
			o365HomeAction.siteShare(getWebDriver(),user);
			Thread.sleep(5000);
			//o365HomeAction.refresh(getWebDriver(), 5);
		}
		catch(Exception e) {

			Reporter.log("error in uploading  the file ", true);
		}
		Thread.sleep(30000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared Team Site with "+user );
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");

	}
/*test need admin account
 * 
 */
	@Priority(9)
//	@Test(groups ={"SANITY","P1","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateEditSite() throws Exception {
			
			
		try{
			Reporter.log("Performing site edite action", true);
			o365HomeAction.editSite(getWebDriver());
			Thread.sleep(5000);
			Reporter.log("Completed -- site edite action", true);
			
		}
		catch(Exception e) {

			Reporter.log("Failed- site edite action ", true);
		}
		Thread.sleep(30000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created Folder  on Site entitled ");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");

	}

	
	@Priority(13)
	@Test(groups ={"REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateCIQDownload() throws Exception {
		// navigate to share point team
		//	o365HomeAction.spTeamSite(getWebDriver());
		Reporter.log("** Test for CIQ file download **", true);
		Thread.sleep(15000);
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, CIQConstants.RISK_PROFIFLE,CIQConstants.RISK_PROFIFLE,CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		Thread.sleep(5000);
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		String [] ciqfiles={"pci.txt"};
		for(int i=0; i<ciqfiles.length;i++){
			try{
				o365HomeAction.selectSPFilebyName(getWebDriver(), ciqfiles[i]);
				o365HomeAction.downloadSPFile(getWebDriver());
				Thread.sleep(5000);
				o365HomeAction.refresh(getWebDriver(), 5);
			}
			catch(Exception e) {

				Reporter.log("error in uploading  the file "+ciqfiles[i], true);
			}
		}
		Thread.sleep(30000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pci.txt download has risk(s) - ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");

	}
	@Priority(14)
	@Test(groups ={"REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateCIQUpload() throws Exception {
		// navigate to share point team
		String file=CIQConstants.RISK_FILE;
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName()+"SP", suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");
		Reporter.log("Performing saas app file upload acitn for file "+file, true);
		String filePath=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator ;
		o365HomeAction.spTeamSiteUpload(getWebDriver(),filePath+file);

		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName()+"SP", suiteData, backend.getHeaders(suiteData));
		Thread.sleep(30000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pci_sp.txt upload has risk(s) - ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");

	}
/*
 * site creation action need admin login
 */
	@Priority(9)
	@Test(groups ={"SANITY","P1_ADMIN","REGRESSION","REACH_AGENT"} ) 
	public void o365SitesValidateSiteCreation() throws Exception {
			String Sitename="gwo365beatleTest";
			
		try{
			Reporter.log("Performing site edite action", true);
			o365HomeAction.createSite(getWebDriver(), suiteData, Sitename);
			Thread.sleep(5000);
			Reporter.log("Completed -- site edite action", true);
			
		}
		catch(Exception e) {

			Reporter.log("Failed- site edite action ", true);
		}
		Thread.sleep(30000);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created Folder  on Site entitled ");
		Assert.assertTrue(backend.validateLogGeneric(client, suiteData, fromTime, expectedDataMap, terms),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("site cretion log  Verification Successfull");
		Logger.info("==================================================================================");

	}


	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}

	public void deletefile(String filename) throws InterruptedException{
		boolean filestatus=o365HomeAction.selectSPFilebyName(getWebDriver(), filename);
		if(filestatus)
		{
		o365HomeAction.deleteSPFile(getWebDriver());
		}
		else
		{
			Logger.info("==================================================================================");
			Logger.info("file not present for deletion.");
			Logger.info("==================================================================================");
		}
	}
	public void setCommonFieldsInExpectedDataMap(){
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.BROWSER, suiteData.getBrowser());
		expectedDataMap.put(GatewayTestConstants.DEVICE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
//		expectedDataMap.put(GatewayTestConstants.HOST, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.CREATED_TIME_STAMP, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.INSERTED_TIME_STAMP, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, GatewayTestConstants.IS_ANONYMOUS_PROXY_FALSE);
		expectedDataMap.put(GatewayTestConstants.REQ_SIZE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.RESP_SIZE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
//		expectedDataMap.put(GatewayTestConstants.TIME_ZONE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.USER, suiteData.getSaasAppUsername());
		expectedDataMap.put(GatewayTestConstants.USER_NAME, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.VERSION, "NOT_EMPTY");
	}

}
