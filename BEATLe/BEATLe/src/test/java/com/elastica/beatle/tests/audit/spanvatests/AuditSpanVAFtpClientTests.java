package com.elastica.beatle.tests.audit.spanvatests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
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
import com.elastica.beatle.audit.SpanVATestsUtils;
import com.elastica.beatle.audit.SummaryTabDto;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class AuditSpanVAFtpClientTests extends AuditInitializeTests {
	
	protected Client restClient;
	protected String sourceID = null;
	String fireWallType=null;
	String spanVAPayload=null;
	String firewallLogFilePath=null;
	String agentId=null;
	String scpcompltedCheckEmptyFilePath=null;
	String spanvaFtpClientUsername=null;
	String spanVaFtpClientUserPwd=null;
	String spanVaFtpClientHost=null;
	String spanVaFtpClientHostSourceDir=null;
	String spanvaAwsNodeConnectPEMLocationPath = null;
	String spanvaNfsServerHost=null;
	String spanvaHttpsPort = null;
	Properties spanvaconfigProps = new Properties();
	Properties firewallLogDataProps=null;
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();
	ArrayList<String> goldenSetErrorList = new ArrayList<String>();
	SpanVATestsUtils spnavaUtils=null;
	private Map<String,String> spanVAAliveAgentDetailsMap;
	ArrayList<String> auditSummaryForConsumerServicesValidationsErrors = new ArrayList<String>();

	
	
	
	public AuditSpanVAFtpClientTests(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
	}
	
	
	@BeforeClass(alwaysRun = true)
	public void populateSpanvaftpClientData() throws Exception {
		
		Reporter.log("************Load SpanVa FTPClient Data*********************",true);
		//Download the log file from s3 and keeping temfolder
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		
		InputStream inputStream = new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.AUDIT_CONFIGURED_AGENTS_PATH));
		spanvaconfigProps.load(inputStream);
		
		Reporter.log("**************Read SpanVa FtpClient configuration Data*********************",true);
		spanvaAwsNodeConnectPEMLocationPath = spanvaconfigProps.getProperty("spanva_nfs_server_cert_pem_location"); //pem file location path to connect to FtpClient sever
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType); //get the upload firewall path
		spanvaFtpClientUsername=spanvaconfigProps.getProperty("spanva_ftp_client_username"); //get the spanva FtpClient username
		spanVaFtpClientUserPwd=spanvaconfigProps.getProperty("spanva_ftp_client_userpwd"); //get the spanva FtpClient userpwd
		spanVaFtpClientHost=spanvaconfigProps.getProperty("spanva_nfs_server_host"); //get the spanva FtpClient host
		spanVaFtpClientHostSourceDir=getFtpClientSourceDirPath(fireWallType); //get the firewall source directory path of FtpClient
		
		Reporter.log("****************Print spanva FtpClient configuraion Details************************ ",true);
		Reporter.log("spanva spanvaAwsNodeConnectPEMLocationPath:- "+spanvaAwsNodeConnectPEMLocationPath,true);
		Reporter.log("spanva ftpclient firewall location path:- "+firewallLogFilePath,true);
		Reporter.log("spanva ftpclient Host:- "+spanVaFtpClientHost,true);
		Reporter.log("spanva ftpclient username:- "+spanvaFtpClientUsername,true);
		Reporter.log("spanva ftpclient pwd:- "+spanVaFtpClientUserPwd,true);
		Reporter.log("spanva ftpclient srcdir:- "+spanVaFtpClientHostSourceDir,true);
		
		
		spnavaUtils = new SpanVATestsUtils();
		spanVAAliveAgentDetailsMap = spnavaUtils.getSpanvaAliveAgentOnThisInstance(suiteData, restClient);
		agentId=spanVAAliveAgentDetailsMap.get("agent_id");
		
		Reporter.log("*******************populate Spanva Data for the test****************************************",true);
		Reporter.log("Spanva Host:- "+suiteData.getSpanvaIp(),true);
		Reporter.log("Spanva Host credentials: username:- "+suiteData.getSpanvausername()+" password:- "+suiteData.getSpanvapwd()+" spnavaAgentName:- "+suiteData.getSpanvaAgentName(),true);
		Reporter.log("Spanva AgentId:- "+agentId,true);
		Reporter.log("Spanva version:- "+spanVAAliveAgentDetailsMap.get("current_version"),true);
		
		Reporter.log("Spanva FtpClient Datasource creation payload request******************",true);
		spanVAPayload = AuditTestUtils.createFTPClientSUploadPayload
				(fireWallType,agentId,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SPANVA_DS_NAME+"_FTP_CLIENT",
						spanVaFtpClientHostSourceDir,spanVaFtpClientHost, spanvaFtpClientUsername, spanVaFtpClientUserPwd);
	
		
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
		Reporter.log("Scp Completed Data file path***************"+scpcompltedCheckEmptyFilePath,true);
		   
		
	}


	@Test(priority=1)
	public void testSpanvaFtpClientDataSourceCreationAndProcess() throws Exception
	{
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through SpanVA SCP ClientTransportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);

		Reporter.log("*************Datasource Creation started for:"+fireWallType+"****************",true);
		
		
		
		//create spanva datasource
		Reporter.log("FTP Client Request Payload: "+spanVAPayload,true);
		HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,new StringEntity(spanVAPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		Reporter.log("Actual Datasource Response:"+spanVAConnectionObject,true);

		String dsName=(String)spanVAConnectionObject.get("name");
		Reporter.log("dsName::"+dsName,true);
		String expected_str_setup_by=suiteData.getUsername();
		String expectedResponse=    " [log_transport=" + AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT+
				", DatasourceId=" + "DatasourceId is not null" +
				", resource_uri=" + "resource_uri is not null" +
				", SetupBy=" + expected_str_setup_by +
				", datasource_format=" + "datasource_format is not empty" +
				", agent_id=" + "agent_id is not empty" +
				", agent_name=" + "agent_name is not empty" +
				", log_collection_type=" + "log_collection_type is not empty" +
				", user=" + "user is not empty" +
				", dst_dir=" + "dst_dir is not empty" +
				", datasource_type=" + "datasource_type is not empty"+" ]";
		Reporter.log("Expected Datasource Response fields:"+expectedResponse,true);
		validateSpanVADataSource(spanVAConnectionObject);
		sourceID = (String) spanVAConnectionObject.get("id");
		JSONObject agentInfoObject=(JSONObject) spanVAConnectionObject.get("agent_info");

		Reporter.log("agentInfoObject:"+agentInfoObject,true);
		String host=(String)agentInfoObject.get("host");
		

		//getting data source from getDatasources list
		Reporter.log("getting the datasource from the datasources list:",true);
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();		
		queryParam.add(new BasicNameValuePair("fields", "datasources"));
		HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient,queryParam);
		Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		String strDatataSourceListResp=ClientUtil.getResponseBody(datataSourceListResp);
		Reporter.log("List of Datasources:"+strDatataSourceListResp,true);

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

		

		Reporter.log("******************Upload file using Scp:****************************************",true);
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(spanvaFtpClientUsername,spanVaFtpClientUserPwd,spanVaFtpClientHost,spanVaFtpClientHostSourceDir);
		//SFTPUtils sftpUtils = getSftpUtilsConfiguration("ubuntu", spanvaNfsServerHost, spanVaFtpClientHostSourceDir);

		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
		Reporter.log("******************scp upload started:********************",true);
		result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
		//result = sftpUtils.uploadNFSFile(file.getName(), fin, true,spanvaAwsNodeConnectPEMLocationPath);
		
		Reporter.log("scp file upload status:  " + result,true);
		Reporter.log("******************scp upload completed sucessfully:********************",true);

		//SCPCOMPLETED CHECK
		Reporter.log("SCPCOMPLETED CHECK started******************************" + result,true);
		FileInputStream fuploadCompltedCheckInputStream;
		File uploadCompletedFile = new File(System.getProperty("user.dir")+scpcompltedCheckEmptyFilePath);
		fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
		String uploadCompletedStatus = "";
		result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
		//result = sftpUtils.uploadNFSFile(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true,spanvaAwsNodeConnectPEMLocationPath);
		
		Reporter.log("SCPCOMPLETED status:  " + uploadCompletedStatus,true);
		Reporter.log("******************SSCPCOMPLETED completed successfuly********************",true);
		
		Thread.sleep(10000);//wait 10 sec
     	
		Reporter.log("******************Get Datasource status verification****************************************",true);
		//verification logfile process attached to the datasource
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);	
		String last_Status = pollRespObject.getString("last_status");
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&&
				currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Reporter.log("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
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
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.get("last_status"), "Completed","create data soure procesing is not completed. Last status of this source file was "+last_Status);				
		Assert.assertNotNull(pollRespObject.get("resource_uri"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("resource_uri")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_format"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_format")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_transport"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_transport")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.getString("log_transport"), AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT,"Log Transport method doesn't match. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("id"), "Data Source Id is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("id")).isEmpty(),"ID is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.get("last_status_message"), "Logs processed successfully","Logs processing was not successful. Last status of this source file was "+last_Status);

		Reporter.log("**************************Scp Datasource creation Test Completed**************************",true);

	}
	
	
	@Test(priority=2,dependsOnMethods={"testSpanvaFtpClientDataSourceCreationAndProcess"})
	public void testAuditSummaryNew() throws Exception {
		Reporter.log("******************AuditSummary Test started for :****************"+fireWallType,true);
		//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		//Reporter.log("fireWallType:"+fireWallType,true);
		Reporter.log("sourceID:"+sourceID,true);
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
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1394928000","1397519999");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_BZ2:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1432857600","1435449599");
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			if(AuditTestConstants.FIREWALL_BE_ZSCALAR.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV:
		case AuditTestConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditTestConstants.FIREWALL_BE_PANCSV_BZ2:
		case AuditTestConstants.FIREWALL_BE_PANCSV_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1377388800","1379980799");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV+" Audit summary Info wrong ");
			
			if(AuditTestConstants.FIREWALL_BE_PANCSV.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_PANCSV_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_7ZA+" Audit summary Info wrong ");
				
			}
			
			break;
		}

		case AuditTestConstants.FIREWALL_BE_WSAW3C:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_BZ2:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1412467200","1415059199");
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
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_7Z:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_BZ2:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_GZ:
		{
/*
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
			
			break;*/
		
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_7Z:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_GZ:{
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_7Z:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_BZ2:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_GZ:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1414886400","1417478399");
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		}	

		
	}
	
	@Test(priority=3,dependsOnMethods={"testSpanvaFtpClientDataSourceCreationAndProcess"})
	public void testAuditSummaryOfConsumerServices() throws Exception {
		Reporter.log("******************AuditSummary Test started for consumer services:****************"+fireWallType,true);
		//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		//Reporter.log("fireWallType:"+fireWallType,true);
		Reporter.log("sourceID:"+sourceID,true);
		AuditSummary expectedAuditSummary=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;

		AuditSummary actualAuditSummary=null;
		final String CONSUMER="consumer";

		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1394928000","1397519999");
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			
			if(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_BZ2:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1432857600","1435449599");
			//Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			if(AuditTestConstants.FIREWALL_BE_ZSCALAR.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}
		
		}	

		
	}




	@Test(dependsOnMethods={"testAuditSummaryNew"})
	public void testAuditReport() throws Exception {

		Reporter.log("********************Audit Report test started*****************************"+fireWallType,true);
		//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();

		Reporter.log("************************sourceID:"+sourceID,true);

		AuditReport expectedAuditReport=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;
		AuditReport actualAuditReport=null;

		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1394928000","1397519999");
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_BZ2:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1432857600","1435449599");
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV:
		case AuditTestConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditTestConstants.FIREWALL_BE_PANCSV_BZ2:
		case AuditTestConstants.FIREWALL_BE_PANCSV_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1377388800","1379980799");
				//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_BZ2:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1412467200","1415059199");
				//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit Report Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_7Z:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_BZ2:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_GZ:{
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_7Z:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_GZ:{
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_7Z:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_BZ2:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_GZ:
		{
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1414886400","1417478399");
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
	
		}
	}

	

	/*
	 * This test case deletes the data source
	 */
	@Test(dependsOnMethods={"testAuditReport"}, priority=4)
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Datasource delete Api for the created Datasource", true);
		Reporter.log("2. Deleting Data Source for "+ fireWallType +" its ID is: "+sourceID+" started",true);
		Reporter.log("************************************************************************** ", true);
	
		
		//get Spanva Agent Datasources List
		HttpResponse response=null;
		response = AuditFunctions.listSpanvaAgentDSs(restClient);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject serviceJsonObject = new JSONObject(ClientUtil.getResponseBody(response));
		
		JSONArray agentDsArray = serviceJsonObject.getJSONArray("objects");
		int agentDsArraySize=agentDsArray.length();
		String spnvaAgentDatasourceId=null;
		String discovery_id=null;
		for(int i=0; i<agentDsArraySize; i++)
		{
			discovery_id=((JSONObject)agentDsArray.get(i)).getString("discovery_ds_id");
			if(discovery_id.equals(sourceID))
			{
				spnvaAgentDatasourceId=((JSONObject)agentDsArray.get(i)).getString("id");
				break;
			}
			
		}
		
		response = AuditFunctions.deleteSpanaAgentDataSource(restClient, spnvaAgentDatasourceId);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
		
		Reporter.log("**************************Audit Datasource delete  Test Completed**************************",true);
	}

	@AfterClass
	public void testPopulateAuditSummaryFailures() throws Exception
	{
		Reporter.log("*****************SpanVA Regression: Audit Summary Validation Errors***********************",true);

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
	


	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String userPwd,String sftpServerHost, String sftpServerDestinationDir ) {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		sftpUtils.setPassWord(userPwd);
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
	}


	private void validateSpanVADataSource(JSONObject spanVAConnectionObject)
			throws Exception {
		Assert.assertEquals(spanVAConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);
		Assert.assertNotNull(spanVAConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) spanVAConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertFalse(((String) spanVAConnectionObject.get("datasource_format")).isEmpty(),"Data source format is empty");
		Assert.assertFalse(((String) spanVAConnectionObject.get("datasource_type")).isEmpty(),"Data source type is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_id"), "Agent Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_name"), "Agent Id is null");
		JSONObject agentInfoObject=(JSONObject) spanVAConnectionObject.get("agent_info");
		Assert.assertNotNull(agentInfoObject.get("host"), "Agent host is null");
		Assert.assertNotNull(agentInfoObject.get("log_collection_type"), "log_collection_type is null");
		//Assert.assertNotNull(agentInfoObject.get("user"), "user is null");
		//Assert.assertNotNull(agentInfoObject.get("dst_dir"), "dst_diris null");


	}
	
	
	public String getFtpClientSourceDirPath(String firewallType) {

		String logFileSourceDirPath = null;
	

		switch (firewallType) {
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:
		{
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_bluecoat_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_BZ2:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_GZ:
		{
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_zscalar_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV:
		case AuditTestConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditTestConstants.FIREWALL_BE_PANCSV_BZ2:
		case AuditTestConstants.FIREWALL_BE_PANCSV_GZ:
		{
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_pan_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_BZ2:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_GZ:
		{
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_ciscoWsa_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_7Z:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_BZ2:
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_GZ:
		{
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_ciscoAsa_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_7Z:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2:
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_GZ:
		{
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_mcaWebGateWay_sourcedirpath");
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_7Z:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_BZ2:
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_GZ:
		{
			logFileSourceDirPath = spanvaconfigProps.getProperty("spanva_ftp_client_websenseProxy_sourcedirpath");
			break;
		}
		}
		return logFileSourceDirPath;	
		
	}

}
