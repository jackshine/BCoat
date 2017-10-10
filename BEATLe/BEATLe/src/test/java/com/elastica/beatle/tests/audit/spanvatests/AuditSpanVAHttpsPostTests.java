package com.elastica.beatle.tests.audit.spanvatests;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.testng.annotations.DataProvider;
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

public class AuditSpanVAHttpsPostTests extends AuditInitializeTests{
	
	
	protected Client restClient;
	protected String sourceID = null;
	protected String rsa_key_file_path;
	protected ArrayList<String> datasourceIdsList=new ArrayList<String>();
	protected String fileToBeUploaded;
	protected String sftpTenantUsername;
	protected String sftpServerHost;
	protected String sftpServerDestinationDir;

	private String fireWallType;
	String spanVAPayload;
	String firewallLogFilePath;
	String agentId;
	Properties firewallLogDataProps;
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	protected String scpcompltedCheckEmptyFilePath;
	String spanvaScpPwd = null;
	String spanvaHttpsPort = null;
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();
	SpanVATestsUtils spnavaUtils=null;
	private Map<String,String> spanVAAliveAgentDetailsMap;
	ArrayList<String> auditSummaryForConsumerServicesValidationsErrors = new ArrayList<String>();


	
	Properties p=new Properties();
	
	public AuditSpanVAHttpsPostTests(String firwallType){
		restClient = new Client();
		this.fireWallType=firwallType;
		
	}
	
	@BeforeClass(alwaysRun = true)
	public void initSpanvaHttpsData() throws Exception{
		
		
		//Download the log file from s3 and keeping temfolder
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		
		spnavaUtils = new SpanVATestsUtils();
		spanVAAliveAgentDetailsMap = spnavaUtils.getSpanvaAliveAgentOnThisInstance(suiteData, restClient);
		agentId=spanVAAliveAgentDetailsMap.get("agent_id");
		
		Reporter.log("*******************populate Spanva Data for the test****************************************",true);
		Reporter.log("Spanva Host:- "+suiteData.getSpanvaIp(),true);
		Reporter.log("Spanva Host credentials: username:- "+suiteData.getSpanvausername()+" password:- "+suiteData.getSpanvapwd()+" spnavaAgentName:- "+suiteData.getSpanvaAgentName(),true);
		Reporter.log("Spanva AgentId:- "+agentId,true);
		Reporter.log("Spanva version:- "+spanVAAliveAgentDetailsMap.get("current_version"),true);
		
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
		spanVAPayload = AuditTestUtils.createSpanVAUploadBody(fireWallType,agentId,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SPANVA_DS_NAME+"HTTPS_POST");
		
		
		
	}
	
private String getScpPwd(String apiServerHostName ) {
		
		String scppwd;
		
		if(apiServerHostName.contains("qa-vpc") && suiteData.getTenantName().contains("vpcauditscpco")){
			scppwd=AuditTestConstants.AUDIT_TENANT_VPC_PWD;}
		else if(apiServerHostName.contains("api-vip.elastica.net")){
			scppwd=AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD;
		}
		
		else if(apiServerHostName.contains("api.eu.elastica.net")){
			scppwd=AuditTestConstants.AUDIT_TENANT_CEP_SCP_PWD;
		}
		
		else if(apiServerHostName.contains("api-cwsintg.elastica-inc.com")){
			scppwd=AuditTestConstants.AUDIT_TENANT_CWS_SCP_PWD;
		}
		else if(suiteData.getApiserverHostName().contains("chaapi.elastica-inc.com")){
			scppwd=AuditTestConstants.AUDIT_CHA_TENANT_SCP_PWD;
		}
		else{
			scppwd=AuditTestConstants.AUDIT_EOE_SPANVA_ING_SCP_PWD;
		}
		
		return scppwd;
	}

	
	@Test(priority=1)
	public void testSpanVADatasourceCreationHttpsPOSTSourceType() throws Exception
	{
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through SpanVA Https POST Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);

		Reporter.log("*************Datasource Creation started for:"+fireWallType+"****************",true);
        

		//create spanva datasource
		Reporter.log("Request Payload for SpanVA Https Datasource: "+spanVAPayload,true);
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
		String username=(String)agentInfoObject.get("user");
		String host=(String)agentInfoObject.get("host");
		String dest_dir=(String)agentInfoObject.get("dst_dir");


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
		 String stCmd=null;
	
		 
		//File Upload through Https POST 
		 Reporter.log("fireWallType:-"+fireWallType+"sourceID:-"+sourceID+"https source type:POST",true);
		httpsPostFileTransfer(fireWallType,sourceID, username, getScpPwd(suiteData.getApiserverHostName()), host, "20200", dest_dir, firewallLogFilePath);
	
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(username, host,dest_dir);
		String result = "";
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
	
	@Test(priority=2,dependsOnMethods={"testSpanVADatasourceCreationHttpsPOSTSourceType"})
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
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1432857600","1435449599");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
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

	@Test(priority=3,dependsOnMethods={"testSpanVADatasourceCreationHttpsPOSTSourceType"})
	public void testAuditSummaryOfConsumerServices() throws Exception {
		Reporter.log("******************AuditSummary Test started for :****************"+fireWallType,true);
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
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1432857600","1435449599");
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
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
	

		case AuditTestConstants.FIREWALL_BE_WSAW3C:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_BZ2:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_GZ:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1412467200","1415059199");
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			//Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
			
			if(AuditTestConstants.FIREWALL_BE_WSAW3C.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_WSAW3C_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C_7ZA+" Audit summary Info wrong ");
				
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
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			//Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
			
			if(AuditTestConstants.FIREWALL_BE_WSAW3C.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
				
			}else if(AuditTestConstants.FIREWALL_BE_WSAW3C_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C_7ZA+" Audit summary Info wrong ");
				
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
				//auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			//Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
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


	
	
	//need to do summary tests
	//need to do report tests
	
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
		Assert.assertNotNull(agentInfoObject.get("user"), "user is null");
		Assert.assertNotNull(agentInfoObject.get("dst_dir"), "dst_diris null");


	}
	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String sftpServerHost, String sftpServerDestinationDir ) throws Exception {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_SPANVA_VPC_TENANT_PWD);}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CEP_SCP_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("chaapi.elastica-inc.com")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_CHA_TENANT_SCP_PWD);
		}
		else{
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_EOE_SPANVA_ING_SCP_PWD);
			//sftpUtils.setPassWord(AuditTestUtils.getSpanVATenantScpPwd(suiteData.getTenantName()));
			}
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
	}



public void httpsPostFileTransfer(String firewallType,String datasourceid, String dsUsername, String scppwd, String spanVaHost, String port, String destinationFolder, String firewallLogPath) throws Exception
	
	{
		 String outputString;
		    
		    //curl command
		    // curl -k -i -u ds_55bf01a9bf831217762dac78:f0yMP9cB -XPOST https://10.0.62.12:20200/ds_55bf01a9bf831217762dac78/56aa9e2069533f0d2d165102 -F 
		    //file=@/Users/mallesh/Downloads/spanvahttpstest/bluecoat-vald-auto.log.zip
		 
		    StringBuffer sbCurl=new StringBuffer();
		    sbCurl.append("curl -k -i -u ");
		    sbCurl.append(dsUsername);
		    sbCurl.append(":");
		    sbCurl.append(scppwd);
		    sbCurl.append(" -XPOST ");
		    sbCurl.append("https://"+spanVaHost+":"+port+destinationFolder);
		    sbCurl.append(" -F");
		    sbCurl.append(" file=@"+System.getProperty("user.dir")+firewallLogFilePath);
		 //   sbCurl.append(FileHandlingUtils.getFileAbsolutePath(firewallLogPath));
			
		    String command=sbCurl.toString();
		    Reporter.log(firewallType+" httpsPut FileTranser command::"+command,true);
		    
		  	List<String> listFirewallUploadedStatus=new ArrayList<String>();
		    Process curlProc;
		    try {
		    	Thread.sleep(120000);
		        curlProc = Runtime.getRuntime().exec(command);
		        DataInputStream curlIn = new DataInputStream(curlProc.getInputStream());
		        Reporter.log("firewallType upload through HttpsPost Response:",true);
		        while ((outputString = curlIn.readLine()) != null) {
		        	listFirewallUploadedStatus.add(firewallType+"-"+datasourceid+":-"+outputString);
		        	// Reporter.log(firewallType+"upload stat"outputString,true);
		        }
		       for(String str:listFirewallUploadedStatus)
		       {
		    	   Reporter.log(str,true);
		       }

		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    }
	}	
	



	
	
}
