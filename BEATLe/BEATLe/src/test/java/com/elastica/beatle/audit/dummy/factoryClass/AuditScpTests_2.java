package com.elastica.beatle.audit.dummy.factoryClass;

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
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
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
import com.elastica.beatle.audit.AuditReportRiskyServices;
import com.elastica.beatle.audit.AuditReportServiceCategories;
import com.elastica.beatle.audit.AuditReportTotals;
import com.elastica.beatle.audit.AuditSummary;
import com.elastica.beatle.audit.AuditSummaryTopRiskyServices;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.GoldenSetData;
import com.elastica.beatle.SFTPUtils;

public class AuditScpTests_2 extends AuditInitializeTests {
	
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
	
	
	
	


	public AuditScpTests_2(String fireWallType) {
		restClient = new Client();
		this.fireWallType = fireWallType;
	}
	
	
	/*
	
	

	*//**
	 * Prepares the payload for datasource creation
	 * set the firewall log path. 
	 * @throws Exception
	 *//*
	@BeforeClass(alwaysRun = true)
	public void intScpSftpData() throws Exception{
		scpPayload = AuditTestUtils.createSCPUploadBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SCP_DS_NAME);
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
	}


	*//**
	 * The below test case do the below tasks
	 * 1. Create Datasource of the type Scp/sftp 
	 * 2. Verify the scp/sftp credentials
	 * 3. Upload the firewall log file using jsch library to the datasource
	 * 4. Verify the datasource status continueously for 4 hours (SLA time) till the firewall log file process COMPLETED. 
	 * @throws Exception
	 *//*
	@Test(description="Dataource creation for Scp/Sftp ")
	public void testSCPDatasourceCreation() throws Exception {
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through SCP Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);
		
		Reporter.log("*************Datasource Creation started for:"+fireWallType+"****************",true);
		Reporter.log("Request Payload: "+scpPayload,true);
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
		Reporter.log("******************scp upload completed sucessfully:********************",true);
		
		Reporter.log("******************Get Datasource sttaus verification****************************************",true);
		//verification logfile process attached to the datasource
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);
		String last_Status = pollRespObject.getString("last_status");
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);long currentWaitTime = 0;
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
		
		if(!"Completed".equals(last_Status) )
		{
			//call summary and will not produce any results.
		    testVerifyAuditResultsNotProducedForUnProcessedLogs(sourceID);
		    
		    pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			
			auditDSStatusDTO= AuditTestUtils.populateInCompleteDataSources(pollRespObject);
			inCompleteDsList.add(auditDSStatusDTO);
		}
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);
		Reporter.log("************************** Scp Datasource process Completed sucessfully ******************************", true);
		Reporter.log("**************************Scp Datasource creation Test Completed**************************",true);
		
	}
	
	*//**
	 * validate created datasource object
	 * @param scpConnectionObject
	 * @throws Exception
	 *//*
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
	
	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String sftpServerHost, String sftpServerDestinationDir ) {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
        sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
        sftpUtils.setUserName(sftpTenantUsername);
        if(suiteData.getApiserverHostName().contains("qa-vpc")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_VPC_PWD);}
        else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD);
        }
        else if(suiteData.getApiserverHostName().contains("api-cep.elastica.net")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CEP_SCP_PWD);
        }
        else if(suiteData.getApiserverHostName().contains("api-cep.elastica.net")){
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
				setServices.add(serviceName);
				
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
				setServices.add(serviceName);
				
			}

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
	@Test(dependsOnMethods={"testSCPDatasourceCreation"})	
	public void testAuditSummaryNew() throws Exception {
		 Reporter.log("******************AuditSummary Test started for :****************"+fireWallType,true);
		
		goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		//Reporter.log("fireWallType:"+fireWallType,true);
		Reporter.log("sourceID:"+sourceID,true);
		AuditSummary expectedAuditSummary=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;
		
		AuditSummary actualAuditSummary=null;
		
		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: {
			//sourceID="567f6d4bc5b7d163ca5cc3ea";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			Reporter.log("actualAuditSummary: for::"+fireWallType+"  "+actualAuditSummary,true);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS: {
			//sourceID="567f6d68c5b7d161e6398bea";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS+" Audit summary Info wrong ");
			break;
		}
		
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: {
			//sourceID="567f6e981ef4af680aa9df1a";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY+" Audit summary Info wrong ");
			break;
		}
		

		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH: {
			//sourceID="567f6daec5b7d1604cb9339d";
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
			//sourceID="567f6f59c5b7d16217fe5835";
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
		//sourceID="567f6ee823c57c62b836393f";
		
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
			
		//sourceID="567f6dce23c57c637fa6b83d";
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
		//sourceID="567f6de9c5b7d16888334169";
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
		//sourceID="568101ddc5b7d1252167028f";
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
		//sourceID="567f6e02c5b7d164c043bbd3";
		
			controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			//sourceID="567f6f7f520907643de9242d";
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
		sourceID="567f6fa41ef4af65517f83d7";
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
			
		//sourceID="567f6fbf52090767e65d08b0";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			break;
		}
		
		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
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
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		
		
		 * case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_CH: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_CH+" Audit summary Info wrong ");
			break;
		}
		 
		
	}
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
		
		
		String firewallType=null;
		String dsStatus=null;
		AuditReportTotals actualAuditReportTotal=null;
		AuditReportRiskyServices auditReportRiskyServices=null;
		AuditReportMostUsedServices auditReportMostUsedServices=null;
		AuditReportServiceCategories auditReportServiceCategories=null;
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
				   auditReport.setAuditMostUsedServices(auditReportMostUsedServices);
					
				}
		
		
    	return auditReport;
		
	}
	
	@Test(dependsOnMethods={"testAuditSummaryNew"})
	public void testAuditReport() throws Exception {
		
	    Reporter.log("********************Audit Report test started*****************************"+fireWallType,true);
	   goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		
		Reporter.log("************************sourceID:"+sourceID,true);
		
		AuditReport expectedAuditReport=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;
		AuditReport actualAuditReport=null;
		
		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: {
		 // sourceID="567f6d4bc5b7d163ca5cc3ea";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
		break;
		}
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS: {
			//sourceID="567f6d68c5b7d161e6398bea";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
		break;
		}
		
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: {
		//sourceID="567f6e981ef4af680aa9df1a";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
	    break;
		}
		
		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH: {
			//sourceID="567f6daec5b7d1604cb9339d";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit Report Info wrong ");
	     break;
		}
		
		case AuditTestConstants.FIREWALL_CHECKPOINT_CSV: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW: {
			//sourceID="567f6f59c5b7d16217fe5835";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
	     break;
		}
		
		case AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS: {
		//sourceID="567f6ee823c57c62b836393f";
		
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS+" Audit Report Info wrong ");
	     break;
		}
		
		
		case AuditTestConstants.FIREWALL_MCAFEE_SEF: {
		//sourceID="567f6dce23c57c637fa6b83d";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_MCAFEE_SEF+" Audit Report Info wrong ");
	     break;
		}
		
		case AuditTestConstants.FIREWALL_BE_PANCSV: {
			//sourceID="567f6de9c5b7d16888334169";
		   controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH: {
		  	//sourceID="568101ddc5b7d1252167028f";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_SQUID_PROXY: {
			//sourceID="567f6e02c5b7d164c043bbd3";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			//sourceID="567f6f7f520907643de9242d";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_BE_WSA_ACCESS: {
			//sourceID="567f6fa41ef4af65517f83d7";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSA_ACCESS+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR: {
			//sourceID="567f6fbf52090767e65d08b0";
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
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
	
	
	
	
	 * This test case deletes the data source
	 
	@Test(dependsOnMethods={"testAuditReport"})
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Datasource delete Api for the created Datasource", true);
		Reporter.log("2. Deleting Data Source for "+ fireWallType +" its ID is: "+sourceID+" started",true);
		Reporter.log("************************************************************************** ", true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
		Reporter.log("*****************************************Datasource: "+sourceID+" deleted sucessfully",true);
		Reporter.log("**************************Datasource Deletion Test Completed**************************",true);
		
	}
	
	//@AfterClass
	public void testPopulateIncompletedDataSourcesData() throws Exception
	{
		Reporter.log("===============SCP Regression: In-Complete Datasources Reached SLA Time Failure analysis"+fireWallType, true);
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
		Reporter.log("*****************SCP Regression: Audit Summary Validation Errors for "+fireWallType,true);
		
		 for(String str: auditSummaryValidationsErrors)
		  {
			  Reporter.log(str,true);
		  }
	}
	
	@AfterClass
	public void testPopulateAuditReportFailures() throws Exception
	{
		Reporter.log("*****************SCP Regression: Audit Report Validation Errors for  "+fireWallType,true);
		
		 for(String str: auditReportValidationsErrors)
		  {
			  Reporter.log(str,true);
		  }
	}
	
	*//**
	 * This method will verify the Audit summary/Report results for un-processed logs
	 * @param sourceID
	 * @throws Exception
	 *//*
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

	}*/
	
	
}
