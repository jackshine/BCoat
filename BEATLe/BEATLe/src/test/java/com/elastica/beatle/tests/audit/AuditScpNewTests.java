package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.elasticsearch.common.joda.time.DateTime;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditGoldenSetDataController3;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditReport;
import com.elastica.beatle.audit.AuditReportMostUsedServices;
import com.elastica.beatle.audit.AuditReportNewDiscoveredServices;
import com.elastica.beatle.audit.AuditReportRiskyServices;
import com.elastica.beatle.audit.AuditReportServiceCategories;
import com.elastica.beatle.audit.AuditReportServiceDetails;
import com.elastica.beatle.audit.AuditReportTotals;
import com.elastica.beatle.audit.AuditSummary;
import com.elastica.beatle.audit.AuditSummaryTopRiskyServices;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.GoldenSetData;
import com.elastica.beatle.audit.SummaryTabDto;
import com.elastica.beatle.audit.TestDsDelete;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.SFTPUtils;

public class AuditScpNewTests extends AuditInitializeTests {

	protected Client restClient;
	protected String sourceID = null;
	protected String fileToBeUploaded;
	protected String sftpTenantUsername;
	protected String sftpServerHost;
	protected String sftpServerDestinationDir;
	protected String fireWallType;
	protected String scpPayload;
	protected String firewallLogFilePath;
	Properties firewallLogDataProps;
	protected ArrayList<String> datasourceIdsList=new ArrayList<String>();
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();
	protected String scpcompltedCheckEmptyFilePath;

	Properties firewallsList = null;
	Properties scpdatasourcesProp=new Properties();
	Properties finalFirewallList = null;





	public AuditScpNewTests(String fireWallType) {
		restClient = new Client();
		this.fireWallType = fireWallType;
	}

	/**
	 * Prepares the payload for datasource creation
	 * set the firewall log path. 
	 * @throws Exception
	 */
	@BeforeClass(alwaysRun = true)
	public void intScpSftpData() throws Exception{
		firewallsList=new Properties();
		finalFirewallList=new Properties();
		firewallsList.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.AUDIT_FIREWALLS_LIST)));
		for (String key : firewallsList.stringPropertyNames()) {
			  if(key.contains(suiteData.getFirewallSet()))
			    finalFirewallList.put(key, firewallsList.getProperty(key));
			  else
				  continue;
		}
		Reporter.log("Scp Regression Firewalls "+suiteData.getFirewallSet()+"size.."+finalFirewallList.size(),true);
	}
	
	//cleanup the datasources

	@Test(priority=1)
	public void cleanupAllTheDataSources() throws Exception
	{
		//get List of Datasources
		String sourceID=null;
		
		  Reporter.log("getting the datasource from the datasources list:",true);
	      List<NameValuePair> queryParam = new ArrayList<NameValuePair>();		
		  queryParam.add(new BasicNameValuePair("fields", "datasources"));
		  queryParam.add(new BasicNameValuePair("limit", "100"));
	      HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient,queryParam);
			Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			
			String strDatataSourceListResp=ClientUtil.getResponseBody(datataSourceListResp);
			Reporter.log("List of Datasources:"+strDatataSourceListResp,true);
			
			JSONObject listObj=new JSONObject(strDatataSourceListResp);
			JSONObject tenantObj=listObj.getJSONObject("objects");
			JSONArray datasourcesList = tenantObj.getJSONArray("datasources");
			ArrayList<String> dsList=new ArrayList<String>();
			for(int i=0; i<datasourcesList.length(); i++)
			  {
				if(  ((JSONObject)datasourcesList.get(i)).getString("name").startsWith("BE_")){
					sourceID=((JSONObject)datasourcesList.get(i)).getString("id");
					dsList.add(sourceID);
					
				}
			  }
			Reporter.log("dsList"+dsList,true);
			
			HttpResponse response=null;
	   
		for(String dsId: dsList)
		{
			response=AuditFunctions.deleteDataSource(restClient, dsId);
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
		}
		Reporter.log("*****************************************Datasource: "+sourceID+" deleted sucessfully",true);
		Reporter.log("**************************Datasource Deletion Test Completed**************************",true);
		
	}	
	
	
	
	@DataProvider(name = "scpDatasources", parallel = true)
	public Object[][] populateSCPDataSources() throws Exception {

		Reporter.log("dataprovider size: "+finalFirewallList.size(),true);
		Object[][] inputData = new Object[finalFirewallList.size()][3];
		int j = 0;
		String firewallType;
		for (String key : finalFirewallList.stringPropertyNames()) {
			  
				  firewallType = firewallsList.getProperty(key);
				  scpPayload = AuditTestUtils.createSCPUploadBody(firewallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SCP_DS_NAME);
				  firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(firewallType);
				  scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
				
				   HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(scpPayload));
					Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
					JSONObject scpConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
					Reporter.log("Actual ConnectionObject Response: "+scpConnectionObject,true);
					String str_last_status_or_detectstatus="Pending Data";
					String expectedConnectionObject=    " [sourceID=" + "sourceID should not be null/empty"+
							", sftpServerDestinationDir=" + "uploaded_path should not be null/empty" +
							", log_transport=" + AuditTestConstants.AUDIT_SCP_FILEFORMAT +
							", Resource URI =" + "Resource URI should not be null/empty" +
							", datasource_type=" + "datasource_type should not be null/empty" +
							", datasource_format=" + "datasource_format should not be null/empty"+
							", last_status=" + str_last_status_or_detectstatus +
							", last_detect_status=" + str_last_status_or_detectstatus+" ]";
					Reporter.log("Expected ConnectionObject Response fields: "+expectedConnectionObject,true);
					validateCreatedDataSource(scpConnectionObject);
					sourceID = (String) scpConnectionObject.get("id");
					sftpServerDestinationDir=(String)scpConnectionObject.get("upload_path");
					
					String dsName = (String) scpConnectionObject.get("name");
					Reporter.log("dsName::" + dsName, true);

					// get credentials of scp/sftp connection
					Reporter.log("**********************Get Scp Credentials***************************************",true);
					HttpResponse getScpCredentialsResponse = AuditFunctions.getCredentials(restClient);
					Assert.assertEquals(getScpCredentialsResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK);
					JSONObject scpCredentialsMetaData = (JSONObject) new JSONObject(ClientUtil.getResponseBody(getScpCredentialsResponse)).getJSONArray("objects").get(0);
					Reporter.log("Actual Credentials Response: "+scpCredentialsMetaData,true);
					String expectedCredentials=    " [sourceID=" + "sourceID should not be null/empty"+
							", username=" + "scp server username should not be null/empty" +
							", password=" + "scp server password should not be null/empty" +
							", created_on =" + "created_on should not be null/empty" +
							", modified_on=" + "modified_on should not be null/empty" +
							", resource_uri=" + "resource_uri should not be null/empty"+
							", server=" + "server should not be null/empty" +" ]";
					Reporter.log("Expected Credentials Response: "+expectedCredentials,true);
					Reporter.log("Validating the credentials object:");
					//validatescpCrentials(scpCredentialsMetaData);
					sftpTenantUsername=(String) scpCredentialsMetaData.get("username");
					//sftpServerHost=(String) scpCredentialsMetaData.get("server");
					sftpServerHost=getScpSftpServerHost();

					Reporter.log("******************Upload file using Scp:****************************************",true);
					//upload file to the datasource using scp
					SFTPUtils sftpUtils=getSftpUtilsConfiguration(sftpTenantUsername, sftpServerHost,sftpServerDestinationDir);
					Reporter.log("******************scp upload detais:********************",true);
					Reporter.log("scpUsername:"+sftpTenantUsername,true);
					Reporter.log("scpPassword:"+sftpUtils.getPassWord(),true);
					Reporter.log("scpServerHost:"+sftpServerHost,true);
					Reporter.log("scpServerDestinationDir:"+sftpServerDestinationDir,true);
					Reporter.log("scpUploadedfirewallLogFilePath:"+firewallLogFilePath,true);

					FileInputStream fin;
					File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
					fin = new FileInputStream(file);
					String result = "";
					Reporter.log("******************scp upload started:********************",true);
					result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
					Reporter.log("scp file upload status:  " + result,true);
					Reporter.log("******************scp file upload completed sucessfully:********************",true);
					
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
			
					inputData[j][0] = firewallType;
					inputData[j][1] = dsName;
					inputData[j][2] = sourceID;
					j++;
			
				Reporter.log("inputData.." + inputData, true);
				Reporter.log("inputData size.." + inputData.length, true);
			  }
			
	return inputData;
	}
	
	@Test(priority = 2, dataProvider = "scpDatasources", dependsOnMethods={"cleanupAllTheDataSources"},threadPoolSize = 20)
	public void testScpDatasourcesVerification(String firewallType, String dsName, String datasourceid)
			throws Exception {
		Reporter.log(
				"firewallType:- " + firewallType + "datasource name:- " + dsName + " datasourceid:- " + datasourceid,
				true);
		scpdatasourcesProp.put(firewallType, datasourceid);
		Reporter.log("scpdatasourcesProp size.." + scpdatasourcesProp.size(), true);
		this.testDataSourceProcessAndMonitorLogsVerificationCheck(datasourceid);
	}
	
	
	@DataProvider(name = "scpCompletedDatasources", parallel = true)
	public Object[][] populateScpDatasourceForSummary() throws Exception {
		
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

	
	
	@Test(priority=3,dependsOnMethods={"testScpDatasourcesVerification"},dataProvider = "scpCompletedDatasources", threadPoolSize = 20)
	public void testAuditSummaryNew(String fireWallType, String datasourceid) throws Exception {
		Reporter.log("******************AuditSummary Test started for :****************"+fireWallType,true);
		goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		//Reporter.log("fireWallType:"+fireWallType,true);
		sourceID=datasourceid;
		Reporter.log("sourceID:"+sourceID,true);
		AuditSummary expectedAuditSummary=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;

		AuditSummary actualAuditSummary=null;

		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			Reporter.log("actualAuditSummary: for::"+fireWallType+"  "+actualAuditSummary,true);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit summary Info wrong ");
			
			if( AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit summary Info wrong ");
			}else if(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z.equals(fireWallType))
			{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA+" Audit summary Info wrong ");
			}
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit summary Info wrong ");
			
			
			break;
		}
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS+" Audit summary Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY+" Audit summary Info wrong ");
			if(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA+" Audit summary Info wrong ");
				
			}
			
			
			break;
		}


		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit summary Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_CHECKPOINT_CSV: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW+" Audit summary Info wrong ");
			break;
		}


		case AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS+" Audit summary Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_MCAFEE_SEF: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_MCAFEE_SEF+" Audit summary Info wrong ");
			break;
		}



		case AuditTestConstants.FIREWALL_BE_PANCSV: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH+" Audit summary Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_SQUID_PROXY: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSA_ACCESS: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSA_ACCESS+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC_TAR:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		case AuditTestConstants.FIREWALL_SONICWALL:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}

		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}

		case AuditTestConstants.FIREWALL_JUNIPER_SRX: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_SCANSAFE: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}

		}
	}


	/**
	 * The below testcase do the below tasks
	 * Note: The test case depends on datasource creation testcase, if the dependent method failed the test case will skip
	 * 1. The inputs are datasourceId and firewallType generated from the dependent methods
	 * 2. Call the  Audit summary for the Allowed services
	 * 3. Validate the Audit summary data of Datasource
	 *    [
	 *      ****Denied data*****
	 *      Audit score
	 *      No of Sass services 
	 *      Total users
	 *      Total Destinations
	 *      services (top used/top risky)
	 *      service usercount, user sessions
	 *      medium risky services
	 *    ]
	 * @throws Exception
	 */

	//@Test(description="Test Audit summary for Denied services",dependsOnMethods={"testSCPDatasourceCreation"})	
	public void testAuditSummaryForBlockedServices() throws Exception
	{
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Audit Summary Api for the created Datasource", true);
		Reporter.log("2. Verify the Blocked Audit summary data (date_range, datasource_id, earliest_date, latest_date...)", true);
		Reporter.log("************************************************************************** ", true);
		Reporter.log("**********Audit Report Verification Test for "+ fireWallType +" its ID is: "+sourceID+" started********* ",true);
		String range = "1mo";			
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("allowed", "false"));
		queryParam.add(new BasicNameValuePair("blocked", "true"));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		

		JSONObject summaryObjectForBlockedServices = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);	


		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: 
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{ 
			break;
		}
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS: {
			break;
		}

		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA:{
			break;
		}
		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH: {
			break;
		}

		case AuditTestConstants.FIREWALL_CHECKPOINT_CSV: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW: {
			break;
		}


		case AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS: {
		}

		case AuditTestConstants.FIREWALL_MCAFEE_SEF: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH: {
			break;
		}

		case AuditTestConstants.FIREWALL_SQUID_PROXY: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSA_ACCESS: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR: {
			break;
		}

		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			break;
		}

		case AuditTestConstants.FIREWALL_JUNIPER_SRX: {
			break;
		}
		case AuditTestConstants.FIREWALL_SCANSAFE: {
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES: {
			break;
		}

		}

	}

	@Test(priority=4,dependsOnMethods={"testAuditSummaryNew"},dataProvider = "scpCompletedDatasources", threadPoolSize = 20)
	public void testAuditReport(String fireWallType, String datasourceid) throws Exception {

		Reporter.log("********************Audit Report test started*****************************"+fireWallType,true);
		goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();

		sourceID=datasourceid;
		Reporter.log("************************sourceID:"+sourceID,true);

		AuditReport expectedAuditReport=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;
		AuditReport actualAuditReport=null;

		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: 
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{ 
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			if(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit Report Info wrong ");
			}else if(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z+" Audit Report Info wrong ");
			}else{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA+" Audit Report Info wrong ");
			}
			break;
		}
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit Report Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_CHECKPOINT_CSV: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS+" Audit Report Info wrong ");
			break;
		}


		case AuditTestConstants.FIREWALL_MCAFEE_SEF: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_MCAFEE_SEF+" Audit Report Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_BE_PANCSV: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_SQUID_PROXY: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSA_ACCESS: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSA_ACCESS+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC:
		{
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED:
		{
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_SONICWALL:
		{
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC_TAR:
		{
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}

		case AuditTestConstants.FIREWALL_JUNIPER_SRX: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_SCANSAFE: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}

		}
	}

	/*
	 * This test case deletes the data source
	 */
	@Test(priority=5,dependsOnMethods={"testAuditReport"},dataProvider = "scpCompletedDatasources", threadPoolSize = 20)
	public void deleteDataSourceTest() throws Exception {

		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Datasource delete Api for the created Datasource", true);
		Reporter.log("2. Deleting Data Source for "+ fireWallType +" its ID is: "+sourceID+" started",true);
		Reporter.log("************************************************************************** ", true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
		Reporter.log("************************** deleteDataSourceTest comleted ****************************** ", true);

	}
	@AfterClass
	public void testPopulateIncompletedDataSourcesData() throws Exception
	{
		Reporter.log("===============SCP/SFTP Regression: In-Complete Datasources Reached SLA Time Failure analysis:================", true);
		if(!inCompleteDsList.isEmpty()){
			for(AuditDSStatusDTO dto:inCompleteDsList)
			{
				Reporter.log(""+dto,true);
			}
		}

	}
	@AfterClass
	public void testPopulateAuditSummaryFailures() throws Exception
	{
		Reporter.log("*****************scp tests Regression: Audit summary Validation Errors for  "+fireWallType,true);

		for(String str: auditSummaryValidationsErrors)
		{
			Reporter.log(str,true);
		}
	}
	@AfterClass
	public void testPopulateAuditReportFailures() throws Exception
	{
		Reporter.log("*****************scp tests regression : Audit Report Validation Errors for  "+fireWallType,true);

		for(String str: auditReportValidationsErrors)
		{
			Reporter.log(str,true);
		}
	}
	
	//
	
	public void testDataSourceProcessAndMonitorLogsVerificationCheck(String sourceID) throws Exception {
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		long totalWaitTime = AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME;
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
			
			if ("Completed".equals(last_Status) || "Failed".equals(last_Status))
				break;
		}
		Assert.assertTrue(currentWaitTime <= totalWaitTime,
				" File processing took " + (int) ((totalWaitTime / (1000 * 60)) % 60)
						+ " minutes. Current Datasource details id - status:[" + sourceID + "-" + last_Status + "]");

	}


	public AuditReport populateActualAuditReportData(String fireWallType,String sourceID) throws Exception
	{

		String range="";
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
		HttpResponse response = AuditFunctions.getAuditReport(restClient, queryParam);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response))
				.getJSONArray("objects").get(0);
		Reporter.log("Actual Audit Report Response:" + reportObject, true);


		String dsStatus=null;
		AuditReportTotals actualAuditReportTotal=null;
		AuditReportRiskyServices auditReportRiskyServices=null;
		AuditReportMostUsedServices auditReportMostUsedServices=null;
		AuditReportServiceCategories auditReportServiceCategories=null;
		AuditReportNewDiscoveredServices auditReportNewDiscoveredServices=null;
		
		AuditReport auditReport=new AuditReport();


		//Totals
		if (reportObject.has("total") && !reportObject.isNull("total")) {
			JSONObject totalobj = reportObject.getJSONObject("total");
			actualAuditReportTotal=new AuditReportTotals();
			actualAuditReportTotal.setUsers(Long.parseLong(totalobj.getString("users")));
			actualAuditReportTotal.setSessions(Long.parseLong(totalobj.getString("sessions")));
			actualAuditReportTotal.setServices(Long.parseLong(totalobj.getString("services")));
			actualAuditReportTotal.setLocations(Long.parseLong(totalobj.getString("locations")));
			actualAuditReportTotal.setCategories(Long.parseLong(totalobj.getString("categories")));
			actualAuditReportTotal.setTraffic(Long.parseLong(totalobj.getString("traffic")));
			actualAuditReportTotal.setUploads(Long.parseLong(totalobj.getString("uploads")));
			actualAuditReportTotal.setDownloads(Long.parseLong(totalobj.getString("downloads")));
			Reporter.log("actual actualAuditReportTotal::"+actualAuditReportTotal,true);

			auditReport.setAuditReportTotals(actualAuditReportTotal);
		}

		//Risky_services
		if (reportObject.has("risky_services") && !reportObject.isNull("risky_services")) {
			JSONObject risky_services_Obj = reportObject.getJSONObject("risky_services");
			auditReportRiskyServices=new AuditReportRiskyServices();

			auditReportRiskyServices.setMed_risky_services(Long.parseLong(risky_services_Obj.getString("medium_risk_services")));
			auditReportRiskyServices.setUsers(Long.parseLong(risky_services_Obj.getString("users")));
			auditReportRiskyServices.setSessions(Long.parseLong(risky_services_Obj.getString("sessions")));
			auditReportRiskyServices.setDownloads(Long.parseLong(risky_services_Obj.getString("downloads")));
			auditReportRiskyServices.setMost_used_services(Long.parseLong(risky_services_Obj.getString("mostused_services")));
			auditReportRiskyServices.setLocations(Long.parseLong(risky_services_Obj.getString("locations")));
			auditReportRiskyServices.setTotal_services(Long.parseLong(risky_services_Obj.getString("total_services")));
			auditReportRiskyServices.setUploads(Long.parseLong(risky_services_Obj.getString("uploads")));
			auditReportRiskyServices.setHigh_risky_services(Long.parseLong(risky_services_Obj.getString("high_risk_services")));
			auditReportRiskyServices.setTotal_traffic(Long.parseLong(risky_services_Obj.getString("total_traffic")));
			//auditReportRiskyServices.setUsers(Long.parseLong(risky_services_Obj.getString("new_disco_services")));
			auditReportRiskyServices.setCategories(Long.parseLong(risky_services_Obj.getString("categories")));
			Reporter.log("actual auditReportRiskyServices::"+auditReportRiskyServices,true);

			auditReport.setAuditReportRiskyServices(auditReportRiskyServices);
		}

		//most_used_services

		if (reportObject.has("most_used_services") && !reportObject.isNull("most_used_services")) {
			JSONObject most_used_services = reportObject.getJSONObject("most_used_services");
			auditReportMostUsedServices=new AuditReportMostUsedServices();

			auditReportMostUsedServices.setMed_risky_services(Long.parseLong(most_used_services.getString("medium_risk_services")));
			auditReportMostUsedServices.setUsers(Long.parseLong(most_used_services.getString("users")));
			auditReportMostUsedServices.setSessions(Long.parseLong(most_used_services.getString("sessions")));
			auditReportMostUsedServices.setDownloads(Long.parseLong(most_used_services.getString("downloads")));
			auditReportMostUsedServices.setLocations(Long.parseLong(most_used_services.getString("locations")));
			auditReportMostUsedServices.setTotal_services(Long.parseLong(most_used_services.getString("total_services")));
			auditReportMostUsedServices.setUploads(Long.parseLong(most_used_services.getString("uploads")));
			auditReportMostUsedServices.setHigh_risky_services(Long.parseLong(most_used_services.getString("high_risk_services")));
			auditReportMostUsedServices.setTotal_traffic(Long.parseLong(most_used_services.getString("total_traffic")));
			auditReportMostUsedServices.setCategories(Long.parseLong(most_used_services.getString("categories")));
			auditReportMostUsedServices.setRiskyServices(auditReportMostUsedServices.getHigh_risky_services()+auditReportMostUsedServices.getMed_risky_services());
			Reporter.log("actual auditReportMostUsedServices::"+auditReportMostUsedServices,true);
			auditReport.setAuditMostUsedServices(auditReportMostUsedServices);

		}

		//servicedetails object
		if (reportObject.has("service_details") && !reportObject.isNull("service_details")) {
			JSONObject serviceDetailsObject = reportObject.getJSONObject("service_details");

			JSONArray serviceDetailsDataArray = serviceDetailsObject.getJSONArray("data");
			int serviceDetailsArrayLength =serviceDetailsDataArray.length();

			JSONArray serviceDataArray;
			List<AuditReportServiceDetails> auditReportServiceDetailsList=new ArrayList<AuditReportServiceDetails>();
			AuditReportServiceDetails  auditReportServiceDetails= null;
			for(int i=0; i<serviceDetailsArrayLength; i++)
			{
				auditReportServiceDetails=new AuditReportServiceDetails();
				serviceDataArray=(JSONArray)serviceDetailsDataArray.get(i);
				auditReportServiceDetails.setServiceId((String)serviceDataArray.getString(0));
				auditReportServiceDetails.setIs_new((String)serviceDataArray.getString(1));
				auditReportServiceDetails.setIs_Most_Used((String)serviceDataArray.getString(2));
				auditReportServiceDetails.setCat1((String)serviceDataArray.getString(3));
				auditReportServiceDetails.setCat2((String)serviceDataArray.getString(4));
				auditReportServiceDetails.setCat3((String)serviceDataArray.getString(5));
				auditReportServiceDetails.setCat4((String)serviceDataArray.getString(6));
				auditReportServiceDetails.setUsers_Count(new Long((String)serviceDataArray.getString(7)).longValue());
				auditReportServiceDetails.setUploads(new Long((String)serviceDataArray.getString(8)).longValue());
				auditReportServiceDetails.setDownloads(new Long((String)serviceDataArray.getString(9)).longValue());
				auditReportServiceDetails.setSessions(new Long((String)serviceDataArray.getString(10)).longValue());
				auditReportServiceDetails.setLocations_Count(new Long((String)serviceDataArray.getString(11)).longValue());
				auditReportServiceDetails.setService_Brr((String)serviceDataArray.getString(12));
				auditReportServiceDetails.setService_Url((String)serviceDataArray.getString(13));
				auditReportServiceDetailsList.add(auditReportServiceDetails);


			}
			auditReport.setAuditReportServiceDetailsList(auditReportServiceDetailsList);
		}
		//new services object preparation
		if (reportObject.has("new_discovered_services") && !reportObject.isNull("new_discovered_services")) {
			JSONObject newDiscoveredServicesObj = reportObject.getJSONObject("new_discovered_services");
			auditReportNewDiscoveredServices=new AuditReportNewDiscoveredServices();
			
			auditReportNewDiscoveredServices.setMedium_risk_services(Long.parseLong(newDiscoveredServicesObj.getString("medium_risk_services")));
			auditReportNewDiscoveredServices.setUsers(Long.parseLong(newDiscoveredServicesObj.getString("users")));
			auditReportNewDiscoveredServices.setSessions(Long.parseLong(newDiscoveredServicesObj.getString("sessions")));
			auditReportNewDiscoveredServices.setDownloads(Long.parseLong(newDiscoveredServicesObj.getString("downloads")));
			auditReportNewDiscoveredServices.setMostused_services(Long.parseLong(newDiscoveredServicesObj.getString("mostused_services")));
			auditReportNewDiscoveredServices.setLocations(Long.parseLong(newDiscoveredServicesObj.getString("locations")));
			auditReportNewDiscoveredServices.setTotal_services(Long.parseLong(newDiscoveredServicesObj.getString("total_services")));
			auditReportNewDiscoveredServices.setUploads(Long.parseLong(newDiscoveredServicesObj.getString("uploads")));
			auditReportNewDiscoveredServices.setHigh_risk_services(Long.parseLong(newDiscoveredServicesObj.getString("high_risk_services")));
			auditReportNewDiscoveredServices.setTotal_traffic(Long.parseLong(newDiscoveredServicesObj.getString("total_traffic")));
			auditReportNewDiscoveredServices.setCategories(Long.parseLong(newDiscoveredServicesObj.getString("categories")));
			
			Reporter.log("actual auditReportNewDiscoveredServices::"+auditReportNewDiscoveredServices,true);

			auditReport.setAuditReportNewDiscoveredServices(auditReportNewDiscoveredServices);
		}




		return auditReport;

	}

	public AuditSummary populateActualAuditSummaryObject(String fireWallType, String sourceID ) throws Exception
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

		SummaryTabDto summaryTabDto=AuditTestUtils.getServicesTabData(sourceID,"","","");
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
	

	/**
	 * validate created datasource object
	 * @param scpConnectionObject
	 * @throws Exception
	 */
	private void validateCreatedDataSource(JSONObject scpConnectionObject)
			throws Exception {
		Assert.assertEquals(scpConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SCP_FILEFORMAT);
		//Assert.assertEquals(scpConnectionObject.get("datasource_type"), dataSourceDTO.get("datasource_type"));
		Assert.assertNotNull(scpConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(scpConnectionObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) scpConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertNotNull(scpConnectionObject.get("setup_by"), "SetUp by is null");
		Assert.assertFalse(((String) scpConnectionObject.get("datasource_format")).isEmpty(),
				"Data source format is empty");
		Assert.assertFalse(((String) scpConnectionObject.get("datasource_type")).isEmpty(),
				"Data source type is empty");
		Assert.assertFalse(((String) scpConnectionObject.get("setup_by")).isEmpty(), "Set Up by is empty");
		Assert.assertEquals(scpConnectionObject.get("last_status"), "Pending Data",
				"Last status is not \"Pending Data\"");
		Assert.assertEquals(scpConnectionObject.get("last_detect_status"), "Pending Data",
				"Last status is not \"Pending Data\"");
	}

	/**
	 * validate getcredentials api
	 * @param scpCredentialsObject
	 * @throws Exception
	 */
	private void validatescpCrentials(JSONObject scpCredentialsObject) throws Exception
	{
		Assert.assertNotNull(scpCredentialsObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(scpCredentialsObject.get("username"), "scp server username is null");
		Assert.assertNotNull(scpCredentialsObject.get("password"), "scp server password is null");
		Assert.assertNotNull(scpCredentialsObject.get("created_on"), "scp server created on  is null");
		Assert.assertNotNull(scpCredentialsObject.get("modified_on"), "scp server modified on is null");
		Assert.assertNotNull(scpCredentialsObject.get("resource_uri"), "scp server getCredentials resource url is null");
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_QAVPC_SCP_SERVER);
		}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER_PROD);
		}
		else if(suiteData.getApiserverHostName().contains("api-cep.elastica.net")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER_CEP);
		}
		else if(suiteData.getApiserverHostName().contains("api-cwsintg.elastica-inc.com")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER_CWSINTG);
		}
		else{
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER);

		}


	}	
	/**
	 * prepare sftpUtils configuration
	 * @param sftpTenantUsername
	 * @param sftpServerHost
	 * @param sftpServerDestinationDir
	 * @return
	 */
	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String sftpServerHost, String sftpServerDestinationDir ) {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		if(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("vpcauditscpco")){
	        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_VPC_PWD);}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD);
		}
		
		else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CEP_SCP_PWD);
		}
		
		else if(suiteData.getApiserverHostName().contains("api-cwsintg.elastica-inc.com")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CWS_SCP_PWD);
		}
		else{
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PWD);
		}
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
	}

	public String getScpSftpServerHost()
	{
		String sftpServerHost=null;
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			sftpServerHost=AuditTestConstants.AUDIT_QAVPC_SCP_SERVER;}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_PROD;
		}
		else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
			sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_CEP;
		}
		else{
			sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER;
		}
		Reporter.log("scp/sftp serverhost: "+sftpServerHost,true);
		return sftpServerHost;
	}


	/**
	 * This method will verify the Audit summary/Report results for un-processed logs
	 * @param sourceID
	 * @throws Exception
	 */
	private  void testVerifyAuditResultsNotProducedForUnProcessedLogs(String sourceID) throws Exception
	{
		HttpResponse response=null;

		String range = "1mo";			
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		//summary results verification
		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);				
		Assert.assertEquals(summaryObject.get("total_users"), 0, "From Summary: Total users should be ");
		Assert.assertEquals(summaryObject.get("total_destinations"), 0, "From Summary: Total Destinations should be ");
		Assert.assertEquals(summaryObject.get("total_services"), 0, "From Summary: Total Services should be ");
		Assert.assertEquals(summaryObject.get("high_risk_services"), 0, "From Summary: Total high_risk_services should be ");


		//report results verification

		response  = AuditFunctions.getAuditReport(restClient, queryParam);		
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		JSONObject totalReport = (JSONObject) reportObject.getJSONObject("total");
		Assert.assertEquals(totalReport.get("users"), 0, "From Report: users should be ");
		Assert.assertEquals(totalReport.get("sessions"), 0, "From Report: Sessions should be ");
		Assert.assertEquals(totalReport.get("services"), 0, "From Report: Services should be ");
		Assert.assertEquals(totalReport.get("traffic"), 0, "From Report: traffic should be ");
		Assert.assertEquals(totalReport.get("locations"), 0, "From Report: locations should be ");
		Assert.assertEquals(totalReport.get("categories"), 0, "From Report: categories should be ");
		Assert.assertEquals(totalReport.get("uploads"), 0, "From Report: uploads should be ");
		Assert.assertEquals(totalReport.get("downloads"), 0, "From Report: downloads should be ");
		Assert.assertEquals(totalReport.get("new_services"), 0, "From Report: new_services should be ");

	}

}
