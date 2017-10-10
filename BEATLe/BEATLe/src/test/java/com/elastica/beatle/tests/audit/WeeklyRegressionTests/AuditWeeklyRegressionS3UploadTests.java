/**
 * 
 */
package com.elastica.beatle.tests.audit.WeeklyRegressionTests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController3;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditReport;
import com.elastica.beatle.audit.AuditSummary;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.AuditVerificationMethods;
import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.audit.GoldenSetData;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AuditWeeklyRegressionS3UploadTests extends AuditInitializeTests{

	private Client restClient;
	private String fireWallType;
	private AuditVerificationMethods auditVerify;
	
	private String sourceID;
	private S3ActionHandler s3Handler;
	private JSONObject S3PropertiesObj;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryForConsumerServicesValidationsErrors = new ArrayList<String>();
	private JSONObject s3BucketDetails;
	
	//private AuditTestUtils autiTestUtils;
	
	
	/**
	 * @param FireWallName
	 */
	public AuditWeeklyRegressionS3UploadTests(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
		auditVerify =  new AuditVerificationMethods(restClient);
		//autiTestUtils = new AuditTestUtils();
	}		
		
	
	@BeforeClass(alwaysRun = true)
	public void initS3Data() throws Exception{
		
		// Download file using amazon S3 URL
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		
		//construct s3 folder name
		s3Handler = new S3ActionHandler();
		String beRegS3LogsBucketFolder= AuditTestUtils.s3RegEnvBucketName(suiteData.getEnvironmentName(), fireWallType, suiteData.getTenantName());
		Logger.info("Create S3 folder for BE regression Tests:-"+beRegS3LogsBucketFolder);

		//Upload file to S3 folder
		s3BucketDetails=AuditTestUtils.getS3BucketDetails();
		S3PropertiesObj = AuditTestUtils.buildS3CredentialCheckPostBody(fireWallType,beRegS3LogsBucketFolder);
		s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
						beRegS3LogsBucketFolder+"/",
						AuditTestUtils.getFireWallLogFileName(fireWallType), 
						new File(FileHandlingUtils.getFileAbsolutePath(AuditTestUtils.getFirewallLogFilePath(fireWallType))));				
	}

	
	@Test
	public void createDataSourceWithS3() throws Exception{

		// Validate S3 Credentials
				
		HttpEntity entity = new StringEntity(S3PropertiesObj.toString());		
		HttpResponse response =  AuditFunctions.checkS3Credentials(restClient,entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject responseObject = new JSONObject(ClientUtil.getResponseBody(response));
		Assert.assertEquals(responseObject.getInt("reason"), 0, "Reason for failure is");
		
		// Create datasource
		JSONObject createS3DSBody = new JSONObject(AuditTestUtils.createS3UploadPostBody(fireWallType, S3PropertiesObj,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_S3_DS_NAME));						
		HttpEntity createDSEntity = new StringEntity(createS3DSBody.toString());
		Logger.info("Create Data Source for "+fireWallType);
		HttpResponse createDataSourceResponse = AuditFunctions.createDataSource(restClient, createDSEntity);
		Assert.assertEquals(createDataSourceResponse.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject createDSRespObject =  new JSONObject(ClientUtil.getResponseBody(createDataSourceResponse));
		Assert.assertEquals(createDSRespObject.getBoolean("__audit_done__"), true);
		Assert.assertEquals(createDSRespObject.getString("bucket"), createS3DSBody.getString("bucket"));
		Assert.assertEquals((String)createDSRespObject.getString("completed_steps"),"null");
		Assert.assertFalse(createDSRespObject.getBoolean("delete_source"), "Delete source should be false");
		Assert.assertEquals(createDSRespObject.getString("host_base"), createS3DSBody.getString("host_base"));
		Assert.assertNotNull(createDSRespObject.getString("id"));
		Assert.assertFalse(((String)createDSRespObject.getString("id")).isEmpty());
		Assert.assertEquals(createDSRespObject.getString("input_folder"), createS3DSBody.getString("input_folder"));
		Assert.assertFalse(createDSRespObject.getBoolean("is_cws"), "is_cws should be false");
		Assert.assertFalse(createDSRespObject.getBoolean("is_sample"), "is_sample needs to be false");
	//	Assert.assertTrue(((String)createDSRespObject.getString("last_detect_message")).isEmpty());
		Assert.assertEquals(createDSRespObject.getString("last_detect_status"), "Pending Data");
		Assert.assertEquals(createDSRespObject.getString("last_status"), "Pending Data");
	//	Assert.assertTrue(((String)createDSRespObject.getString("last_status_message")).isEmpty());		
		Assert.assertEquals(createDSRespObject.getString("log_format"), createS3DSBody.getString("log_format"),"There is type mismatch");
		Assert.assertEquals(createDSRespObject.getString("log_transport"), AuditTestConstants.AUDIT_S3_LOGTRANSPORT);
	//	Assert.assertEquals((String)createDSRespObject.getString("logfile_headers"),"null");		
		Assert.assertNotNull(createDSRespObject.getString("name"));
		Assert.assertFalse(((String)createDSRespObject.getString("name")).isEmpty(),"");
		Assert.assertEquals(createDSRespObject.getString("no_of_logs"), String.valueOf(0));		
		Assert.assertNotNull(createDSRespObject.getString("resource_uri"));
		Assert.assertFalse(((String)createDSRespObject.getString("resource_uri")).isEmpty());
		Assert.assertEquals((String)createDSRespObject.getString("result_sets"),"null");		
		Assert.assertEquals(createDSRespObject.getString("size_of_logs"), String.valueOf(0));		
		Assert.assertTrue(((String)createDSRespObject.getString("time_zone")).isEmpty());
		Assert.assertEquals(createDSRespObject.getString("type"), createS3DSBody.getString("type"),"There is type mismatch");		
		Assert.assertTrue(createDSRespObject.getBoolean("use_https"), "use_https should be true");
		Assert.assertEquals((String)createDSRespObject.getString("valid_fields"),"null","Valid fields shoulb be null");
		sourceID = createDSRespObject.getString("id");
		
		
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Logger.info("Last Status of "+ sourceID +" is "+ last_Status);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))			
				break;
		}
		if(!"Completed".equals(last_Status) )
		{
			//call summary and will not produce any results.
			auditVerify.testVerifyAuditResultsNotProducedForUnProcessedLogs(sourceID);
		    
		    pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			
			auditDSStatusDTO= AuditTestUtils.populateInCompleteDataSources(pollRespObject);
			inCompleteDsList.add(auditDSStatusDTO);
		}
		
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.get("last_status"), "Completed","create data soure procesing is not completed. Last status of this source file was "+last_Status);				
		Assert.assertNotNull(pollRespObject.get("resource_uri"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("resource_uri")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_format"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_format")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_transport"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_transport")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.getString("log_transport"), AuditTestConstants.AUDIT_S3_LOGTRANSPORT,"Log Transport method doesn't match. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("id"), "Data Source Id is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("id")).isEmpty(),"ID is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.get("last_status_message"), "Logs processed successfully","Logs processing was not successful. Last status of this source file was "+last_Status);
	}
	
	@Test(dependsOnMethods={"createDataSourceWithS3"})
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
		
		
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: 
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{
			Reporter.log("Audit Summary Verification Test stated for ."+fireWallType,true);
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1391385600","1393977599");
			Reporter.log("actualAuditSummary: for::"+fireWallType+"  "+actualAuditSummary,true);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			//summary results validation
			if( AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit summary Info wrong ");
			}else if(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z.equals(fireWallType))
			{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA+" Audit summary Info wrong ");
			}
			
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1391385600","1393977599");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP+" Audit summary Info wrong ");
				
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ+" Audit summary Info wrong ");
				
			}
			else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z+" Audit summary Info wrong ");
				
			}
			else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA+" Audit summary Info wrong ");
				
			}
			else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2+" Audit summary Info wrong ");
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:	
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1394928000","1397519999");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ+" Audit summary Info wrong ");
				
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z+" Audit summary Info wrong ");
					
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1442448000","1445039999");
			
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit summary Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7Z:{
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1437609600","1440201599");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}


		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1387152000","1389743999");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP+" Audit summary Info wrong ");
				
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1387670400","1390262399");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_MCAFEE_SEF_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2+" Audit summary Info wrong ");
				
			}
			
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1377388800","1379980799");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_BZ2+" Audit summary Info wrong ");
			}
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1442275200","1444867199");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"2592000","1372636800","1404172799");
			
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit summary Info wrong ");
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1412467200","1415059199");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2+" Audit summary Info wrong ");
				
			}
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"2592000","1388534400","1420070399");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1432857600","1435449599");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2+" Audit summary Info wrong ");		
			}
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_BZ2:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_BZ2:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1414886400","1417478399");
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC_TAR:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_BZ2:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1394064000","1396655999");
			
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}

		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_BZ2: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1434326400","1436918399");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_BZ2: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1399161600","1401753599");
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_BZ2: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}

		}
		
		
	
	}
	
	@Test(dependsOnMethods={"createDataSourceWithS3"})
	public void testAuditSummaryOfConsumerServices() throws Exception {

		Reporter.log("******************AuditSummary for consumer services Test started for :****************"+fireWallType,true);
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
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: 
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{
			Reporter.log("Audit Summary Verification Test stated for ."+fireWallType,true);
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1391385600","1393977599");
			Reporter.log("actualAuditSummary: for::"+fireWallType+"  "+actualAuditSummary,true);
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			
			//summary results validation
			if( AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit summary Info wrong ");
			}else if(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z.equals(fireWallType))
			{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA+" Audit summary Info wrong ");
			}
			
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1391385600","1393977599");
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:	
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA:  {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1394928000","1397519999");
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}


		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7ZA:{
			controller = new AuditGoldenSetDataController3("be_bswoh_consumer");
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1442448000","1445039999");
			
			//auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			//Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit summary Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7Z:{
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA:{
			controller = new AuditGoldenSetDataController3("be_chkpt_sm_consumer");
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1437609600","1440201599");
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1387152000","1389743999");
			
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP+" Audit summary Info wrong ");
				
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ+" Audit summary Info wrong ");
			
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1387670400","1390262399");
			
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2+" Audit summary Info wrong ");
				
			}
			
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1442275200","1444867199");
			
			
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"2592000","1372636800","1404172799");
			
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			//Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit summary Info wrong ");
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1412467200","1415059199");
			
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2+" Audit summary Info wrong ");
				
			}
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"2592000","1388534400","1420070399");
			
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET+"_"+CONSUMER);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1432857600","1435449599");
			
			auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2+" Audit summary Info wrong ");
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
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: 
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{ 
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1391385600","1393977599");
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			
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
		

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1391385600","1393977599");
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP+" Audit Report Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ+" Audit Report Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z+" Audit Report Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA+" Audit Report Info wrong ");
			}else{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2+" Audit Report Info wrong ");
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:	
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA:  
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1394928000","1397519999");
			
			
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1442448000","1445039999");
			
			
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit Report Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7Z: {
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1437609600","1440201599");
			
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1385251200","1387843199");
			
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS+" Audit Report Info wrong ");
			break;
		}


		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1387670400","1390262399");
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_MCAFEE_SEF+" Audit Report Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1377388800","1379980799");
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1442275200","1444867199");
			
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH+" Audit Report Info wrong ");
			break;
		}
		
		
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_BZ2:{
			//controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			//goldenSetDataList = controller.loadXlData();
			//expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"2592000","1372636800","1404172799");
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1412467200","1415059199");
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit Report Info wrong ");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"2592000","1388534400","1420070399");
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSA_ACCESS+" Audit Report Info wrong ");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2:{
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
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_BZ2:
		{
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"2592000","1414886400","1417478399");
			
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_BZ2:
		{
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1414886400","1417478399");
			
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_BZ2:
		{
		//	actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC_TAR:
		{
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		
		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}


		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_BZ2: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_BZ2: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_BZ2: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}

		}
	
		
	
		
	}
	
	/*
	 * This test case deletes the data source
	 */
	@Test(dependsOnMethods={"testAuditReport"})
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("Deleting Data Source "+ fireWallType +" its ID is: "+sourceID, true);
		 if(!(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("datasourcemulti"))){
		        
				HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
				 }
	}
	
	//@AfterClass(alwaysRun=true,dep)
	@Test(dependsOnMethods={"createDataSourceWithS3"})
	public void deleteS3File() throws Exception {
		Logger.info("Deleting file from S3 bucket: "+S3PropertiesObj.getString("bucket")+" with prefix: "+S3PropertiesObj.getString("input_folder")+" with fileName "+AuditTestUtils.getFireWallLogFileName(fireWallType));
		s3Handler.deleteLogFileFromS3(S3PropertiesObj.getString("bucket"),
				S3PropertiesObj.getString("input_folder")+"/", 
				AuditTestUtils.getFireWallLogFileName(fireWallType));
	}
	
	@AfterClass(alwaysRun=true)
	public void testPopulateIncompletedDataSourcesData() throws Exception
	{
		Reporter.log("===============S3 Regression: In-Complete Datasources Reached SLA Time Failure analysis================", true);
		if(!inCompleteDsList.isEmpty()){
			for(AuditDSStatusDTO dto:inCompleteDsList)
			{
				Reporter.log(""+dto,true);
			}
		}
		
	}
	
	@AfterClass(alwaysRun=true)
	public void testPopulateAuditSummaryFailures() throws Exception
	{
		Reporter.log("*****************WebUpload Regression: Audit summary Validation Errors for  "+fireWallType,true);

		 for(String str: goldenSetErrorList)
		  {
			  Reporter.log(str,true);
		  }
	}
	@AfterClass(alwaysRun=true)
	public void testPopulateAuditSummaryOfConsumerServicesFailures() throws Exception
	{
		Reporter.log("*****************WebUpload Regression: Audit summary Validation Errors for consumer services "+fireWallType,true);

		 for(String str: auditSummaryForConsumerServicesValidationsErrors)
		  {
			  Reporter.log(str,true);
		  }
	}
	
	
	
	@AfterClass(alwaysRun=true)
	public void testPopulateAuditReportFailures() throws Exception
	{
		Reporter.log("*****************WebUpload Regression: Audit Report Validation Errors for  "+fireWallType,true);

		 for(String str: auditReportValidationsErrors)
		  {
			  Reporter.log(str,true);
		  }
	}
}
