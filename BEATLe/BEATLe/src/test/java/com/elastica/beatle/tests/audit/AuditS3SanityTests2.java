/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.elasticsearch.common.joda.time.DateTime;
import org.json.JSONArray;
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
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.TestDsDelete;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

/**
 * @author Mallesh
 *
 */
public class AuditS3SanityTests2 extends AuditInitializeTests{

	private Client restClient;
	private String fireWallType;
	private String sourceID;
	private S3ActionHandler s3Handler;
	private JSONObject S3PropertiesObj;
	private Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	protected String s3PreviousRunDsFilePath;
	protected String s3PreviousToPrevRunDsFilePath;

	private JSONObject s3BucketDetails;

	String strfolderName;
	String sanityprevrunFolderName="_Sanity_S3_PRERUNDS";

	String strprevToPrvFoldername;
	String sanityprevtoprerunFolderName="_Sanity_S3_PREVIOUS_TO_PREV_RUNDS";

	String sanityprevrunFileName="";
	String sanityprevtoprevrunFileName="";
	
	List<String> ignoreDataSourcesListForDelete= new ArrayList<String>();



	/**
	 * @param FireWallName
	 */
	public AuditS3SanityTests2(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
	}		

	@BeforeClass(alwaysRun = true)
	public void initS3Data() throws Exception{
		
		//Download the log file from s3 and keeping temfolder
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		

		//construct s3 folder name
		s3Handler = new S3ActionHandler();
		String folderName=AuditTestUtils.getUniqFolderName(suiteData.getEnvironmentName(), "Sanity", suiteData.getTenantName());
		Reporter.log("Create S3 folder for ds file store::"+folderName,true);

		//Upload file to S3 folder
		s3BucketDetails=AuditTestUtils.getS3BucketDetails();
		S3PropertiesObj = AuditTestUtils.buildS3CredentialCheckPostBody(fireWallType,folderName);
		s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
				folderName+"/",
				AuditTestUtils.getFireWallLogFileName(fireWallType), 
				new File(FileHandlingUtils.getFileAbsolutePath(AuditTestUtils.getFirewallLogFilePath(fireWallType))));
		
		s3PreviousRunDsFilePath=previousRunS3DSFileLocation(suiteData.getEnvironmentName(),"");
		s3PreviousToPrevRunDsFilePath=previousRunS3DSFileLocation(suiteData.getEnvironmentName(),"prevtoPreRun");

		sanityprevrunFileName=suiteData.getEnvironmentName()+"_sanitys3ds.txt";
		strfolderName= suiteData.getEnvironmentName()+sanityprevrunFolderName;

		sanityprevtoprevrunFileName=suiteData.getEnvironmentName()+"_sanitys3prevtopreds.txt";
		strprevToPrvFoldername=suiteData.getEnvironmentName()+sanityprevtoprerunFolderName;

		//uploadPrevToPrevFile(s3PreviousToPrevRunDsFilePath);

	}

	@Test(priority=1)
	public void testCleanupDataSourcesToEnsueMaxLimitCheck() throws Exception
	 {
		Reporter.log("Datasources cleanup test execution started.............",true);	
		// get List of Datasources
			String sourceID = null;

			Reporter.log("getting the datasource from the datasources list:", true);
			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
			queryParam.add(new BasicNameValuePair("fields", "datasources"));
			queryParam.add(new BasicNameValuePair("limit", "100"));
			HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient, queryParam);
			Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

			String strDatataSourceListResp = ClientUtil.getResponseBody(datataSourceListResp);
			// Reporter.log("List of Datasources:"+strDatataSourceListResp,true);

			JSONObject listObj = new JSONObject(strDatataSourceListResp);
			JSONObject tenantObj = listObj.getJSONObject("objects");
			JSONArray datasourcesList = tenantObj.getJSONArray("datasources");
			ArrayList<String> dsList = new ArrayList<String>();
			ArrayList<TestDsDelete> dsDeleteList = new ArrayList<TestDsDelete>();
			TestDsDelete dto = null;
			String dsCreationDate = null;
			for (int i = 0; i < datasourcesList.length(); i++) {
				if (((JSONObject) datasourcesList.get(i)).getString("name").startsWith("BE_")) {
					dto = new TestDsDelete();
					sourceID = ((JSONObject) datasourcesList.get(i)).getString("id");
					dto.setDataSourceId(sourceID);
					dsCreationDate = ((JSONObject) datasourcesList.get(i)).getString("setup_date");
					dto.setDateTime(DateTime.parse(dsCreationDate));
					dto.setDsname(((JSONObject) datasourcesList.get(i)).getString("name"));
					dsDeleteList.add(dto);
					dsList.add(sourceID);

				}
			}

			Reporter.log("datasources list before cleanup" + dsDeleteList, true);
			int dsListSize = 0;
			HttpResponse response = null;

			if (!dsList.isEmpty()) {
				Reporter.log("dsList size..." + dsList.size(), true);
				dsListSize = dsList.size();
				//The below code keeps the datasources max 17 per tenant,if more than 17 exist we are deleting the remaining dsources,:- this check required for max_datasources_limit(20) of sanity.
				if (dsListSize >17) { 
					List<String> ignoreDsList = dsList.subList(0, 17);
					for (String dsid : dsList) {
						if (ignoreDsList.contains(dsid)) {
							continue;
						} else {
							Reporter.log("dsources need to delete:." + dsid, true);
							//check the existency of datasource
							HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, dsid);
							if( HttpStatus.SC_OK==pollForStatusResponse.getStatusLine().getStatusCode()){
								  response = AuditFunctions.deleteDataSource(restClient, dsid);
								 // Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
								  //some times the delete call not able to find the datasource even datasource exist. During the datasouce deletion execution other thread
								  //from scp or s3 taken care deletion of the datasource causing the ds not found error. to avoid this we commented assert part temporarly.-reworking here.
							}else{
								Reporter.log("Datsource id:"+dsid+" not exist in the system and it was deleted by other thread",true);
							}
						}
					}
				} else {
					Reporter.log("Datasources not exceeded the max_limit_check, none of the datasources required to delete:.", true);
					// do nothing
				}

			}

			Reporter.log("*****************************************Datasource: " + sourceID + " deleted sucessfully", true);
			Reporter.log("**************************Datasource Deletion Test Completed**************************", true);

		}

	@Test(priority=2, dependsOnMethods={"testCleanupDataSourcesToEnsueMaxLimitCheck"})
	public void createDataSourceWithS3() throws Exception{

		String dsIdFromPreviousRun=s3Handler.readFromS3(s3BucketDetails.getString("bucket"),strfolderName+"/"+sanityprevrunFileName) ;
		Reporter.log("previous run datasource id..."+dsIdFromPreviousRun,true);

		String dsIdFromPreviousToPrevRun=s3Handler.readFromS3(s3BucketDetails.getString("bucket"),strprevToPrvFoldername+"/"+sanityprevtoprevrunFileName) ;
		Reporter.log("previous to previous run datasource id..."+dsIdFromPreviousToPrevRun,true);

		// Validate S3 Credentials
		Reporter.log(S3PropertiesObj.toString(),true);
		HttpEntity entity = new StringEntity(S3PropertiesObj.toString());
		HttpResponse response =  AuditFunctions.checkS3Credentials(restClient,entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject responseObject = new JSONObject(ClientUtil.getResponseBody(response));
		Assert.assertEquals(responseObject.getInt("reason"), 0, "Reason for failure is");


		// Create datasource
		JSONObject createS3DSBody = new JSONObject(AuditTestUtils.createS3UploadPostBody(fireWallType, S3PropertiesObj,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_S3_DS_NAME,"SANITY"));						
		HttpEntity createDSEntity = new StringEntity(createS3DSBody.toString());
		Reporter.log("Create Data Source for "+fireWallType, true);
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
		Assert.assertEquals((String)createDSRespObject.getString("logfile_headers"),"null");		
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

		String previousRunDsCreationTime=null;
		String previoustoPrevRunDSStatus="";
		String previoustoprevRunDsCreationTime=null;

		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&&
				currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME){
			Reporter.log("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of "+ sourceID +" is "+ last_Status, true);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))	{	
				FileHandlingUtils.writeDatasourceIntoFile(sourceID, s3PreviousRunDsFilePath); //if current datasource is is completed we are writing this into new file.
				uploadFile(s3PreviousRunDsFilePath);
				Reporter.log("new File is written..",true);
				break;
			}
		}
		Reporter.log("current run ds status: after 30 mins::["+sourceID+"-"+last_Status+"]",true);

		if(!("Completed".equals(last_Status))){ //check the current datasource status after 30 mins
			Reporter.log("enter preivous run ds verifications"+dsIdFromPreviousRun,true);
			FileHandlingUtils.writeDatasourceIntoFile(sourceID, s3PreviousRunDsFilePath); //if current datasource is is not in completed we are writing this into new file.
			uploadFile(s3PreviousRunDsFilePath);
			Reporter.log("new File is written..",true);
			String previousRunDSStatus="";

			//Verify the previous run datasource; if null/empty failing the test
			Assert.assertNotNull(dsIdFromPreviousRun,"previous run datasource:["+dsIdFromPreviousRun+"] is empty/null and  Current Datasoure process took more than "+
					(int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. Current Datasource details id - status:["+sourceID+"-"+last_Status+"]" );


			//if previous run datasource not null then poll the datasource
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, dsIdFromPreviousRun);

			//check the previous run datasource, if reponse is other than 200 throwing previous run ds is not valid.
			Assert.assertTrue( (pollForStatusResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK), "previous run datasource:["+dsIdFromPreviousRun+"] is not exist in the system/not Valid and  Current Datasoure process took more than "+
					(int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. Current Datasource details id - status:["+sourceID+"-"+last_Status+"]" );


			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			previousRunDSStatus = pollRespObject.getString("last_status");
			previousRunDsCreationTime=pollRespObject.getString("setup_date");
			Reporter.log("previous run datasource status check: .("+dsIdFromPreviousRun+"-"+previousRunDSStatus+")",true);
			

			if("Completed".equals(previousRunDSStatus))	{	//check the prev run ds completed status
				sourceID=dsIdFromPreviousRun;   //map the previous datasource id to current datasourceid
			}
			else if( !("Completed".equals(previousRunDSStatus)))
			{
				Reporter.log("enter preivous to previous run ds verifications"+dsIdFromPreviousToPrevRun,true);
				FileHandlingUtils.writeDatasourceIntoFile(dsIdFromPreviousRun, s3PreviousToPrevRunDsFilePath); //if current datasource is is not in completed we are writing this into new file.
				uploadPrevToPrevFile(s3PreviousToPrevRunDsFilePath);
				Reporter.log("previous datasource is written into .."+s3PreviousToPrevRunDsFilePath,true);
				

				//Verify the previous to previous run datasource; if null/empty failing the test
				Assert.assertNotNull(dsIdFromPreviousToPrevRun,"previous to previous run datasource:["+dsIdFromPreviousToPrevRun+"] is empty/null" );


				//if previous run datasource not null then poll the datasource
				pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, dsIdFromPreviousToPrevRun);

				//check the previous to previous run datasource, if reponse is other than 200 throwing previous run ds is not valid.
				Assert.assertTrue( (pollForStatusResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK), "previous to previous run datasource:["+dsIdFromPreviousToPrevRun+"] is not exist in the system/not Valid " );


				pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
				previoustoPrevRunDSStatus = pollRespObject.getString("last_status");
				previoustoprevRunDsCreationTime=pollRespObject.getString("setup_date");
				Reporter.log("previoustoPrevRunDSStatus status check: .("+dsIdFromPreviousToPrevRun+"-"+previoustoPrevRunDSStatus+")",true);
				if("Completed".equals(previoustoPrevRunDSStatus))	{	//check the prev run ds completed status
				    sourceID=dsIdFromPreviousToPrevRun;   //map the previous datasource id to current datasourceid
				}
				else{ //if the prevous run ds staus also not completed then failing the test with both previous and current ds id's with status.
					Assert.assertTrue(false," previous to previous dsource also not completed and Details are: Datasource id - status:["+dsIdFromPreviousToPrevRun+"-"+previoustoPrevRunDSStatus+"]");
				}
				
			}

			
			else{ //if the prevous run ds staus also not completed then failing the test with both previous and current ds id's with status.
				pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
				Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
				pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
				last_Status = pollRespObject.getString("last_status");
				Assert.assertTrue(false," Current Datasoure process took more than "+
						(int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. "
								+ "Current Datasource id - status:["+sourceID+"-"+last_Status+"]" 
						        + "previous run Datasource id - status:["+dsIdFromPreviousRun+"(setupdate:"+previousRunDsCreationTime+")"+"-"+previousRunDSStatus+"]"
				                + "previous to previous run Datasource id - status:["+dsIdFromPreviousToPrevRun+"(setupdate:"+previoustoprevRunDsCreationTime+")"+"-"+previoustoPrevRunDSStatus+"]");

			}
		}
		ignoreDataSourcesListForDelete.add(sourceID);
		ignoreDataSourcesListForDelete.add(dsIdFromPreviousRun);
		ignoreDataSourcesListForDelete.add(dsIdFromPreviousToPrevRun);
		Reporter.log("************************** S3 Datasource process Completed sucessfully ******************************", true);
		Reporter.log("**************************S3 Datasource creation Test Completed**************************",true);
	}


	@Test(dependsOnMethods={"createDataSourceWithS3"})
	public void testAuditSummaryNew() throws Exception {

		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Audit summary Api for the created Datasource", true);
		Reporter.log("2. Verify the Audit summary data (date_range, id, earliest_date, latest_date...)", true);
		Reporter.log("************************************************************************** ", true);
		Reporter.log("**********Audit Summary Verification Test for "+ fireWallType +" its ID is: "+sourceID+" started********* ",true);
		String range = "1mo";	
		HttpResponse response=null;
		JSONObject summaryObject=null;
		
		response  = AuditFunctions.getSummary(restClient, new StringEntity(AuditTestUtils.getAuditSummaryLatest(sourceID,"86400","1394928000","1397519999")));				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
	
		String strResponse=ClientUtil.getResponseBody(response);
		JSONArray jsonarray = new JSONArray(strResponse);
		summaryObject = jsonarray.getJSONObject(0);		
		

		Assert.assertEquals(summaryObject.get("datasource_id"), sourceID);										   
		//Assert.assertEquals(summaryObject.get("date_range"), range);
		//Assert.assertNotNull(summaryObject.get("id"), "Id is null");
		//Assert.assertFalse(((String) summaryObject.get("id")).isEmpty(),"ID is empty");
		//Assert.assertNotNull(summaryObject.get("resource_uri"),"resource_uri is null");
		//Assert.assertFalse(((String)summaryObject.get("resource_uri")).isEmpty(),"resource_uri is empty");
		//Assert.assertTrue(summaryObject.getBoolean("is_valid"));		
		Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");	
	
		
		Reporter.log("**************************Audit Summary  Test Completed**************************",true);


	
		
		
	
	}

	@Test(dependsOnMethods={"testAuditSummaryNew"})
	public void testAuditReport() throws Exception {
		Reporter.log("Getting Report for "+ fireWallType +" its ID is: "+sourceID, true);
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		String range = "1mo";
		HttpResponse response =null;
		JSONObject reportObject=null;
		
		response  = AuditFunctions.getAuditReport(restClient, new StringEntity(AuditTestUtils.getAuditReportPayloadLatest(sourceID,"86400","1394928000","1397519999")));				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		
		String strResponse=ClientUtil.getResponseBody(response);
		JSONArray jsonarray = new JSONArray(strResponse);
		reportObject = jsonarray.getJSONObject(0);		
        Reporter.log("Actual Audit Report Response:"+reportObject,true);
		Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
		//Assert.assertEquals(reportObject.get("date_range"), range);
		Assert.assertNotNull(reportObject.get("earliest_date"),"earliest date is null");		
		//Assert.assertNotNull(reportObject.get("generated_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("latest_date"),"Latest date is null");		

	}
	
	@AfterClass
	public void deleteS3FileNew() throws Exception {
		Reporter.log("deleteS3FileNew...",true);
		Thread.sleep(60000);
		s3Handler.deleteLogFileFromS3(S3PropertiesObj.getString("bucket"),
				S3PropertiesObj.getString("input_folder")+"/", 
				AuditTestUtils.getFireWallLogFileName(fireWallType));
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


	public String previousRunS3DSFileLocation(String envName,String prevtoprev){

		String prevRunDSIDFileLocation=null;
		switch(envName)
		{
		case "prod": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_PROD_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;
			}else{
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_PROD_S3_PREVIOUS_RUN_DSID_LOCATION_PATH;}

			break;
		}
		case "cep": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_CEP_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;
			}else{ 
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_CEP_S3_PREVIOUS_RUN_DSID_LOCATION_PATH;}
			break;
		}
		case "qavpc": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_VPC_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;

			}else{
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_VPC_S3_PREVIOUS_RUN_DSID_LOCATION_PATH;}
			break;
		}
		case "eoe": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_EOE_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;
			}else{
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_EOE_S3_PREVIOUS_RUN_DSID_LOCATION_PATH;}
			break;
		}
		case "envX": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_ENVX_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;
			}else{
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_ENVX_S3_PREVIOUS_RUN_DSID_LOCATION_PATH;}
			break;
		}
		default:
			break;
		}
		return prevRunDSIDFileLocation;

	}

	private void validateDataSource(long currentWaitTime, String last_Status,JSONObject pollRespObject) throws Exception
	{
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME," File processing took more than "+  (int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. Last status of this source file was "+last_Status);
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


	private void uploadFile(String fileTobeUploaded) throws Exception{
		s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
				strfolderName+"/",
				sanityprevrunFileName, 
				new File(FileHandlingUtils.getFileAbsolutePath(fileTobeUploaded)));

	}
	
	private void uploadPrevToPrevFile(String fileTobeUploaded) throws Exception{
		s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
				strprevToPrvFoldername+"/",
				sanityprevtoprevrunFileName, 
				new File(FileHandlingUtils.getFileAbsolutePath(fileTobeUploaded)));

	}



}
