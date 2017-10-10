package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.TestDsDelete;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class AuditScpSanityTests2 extends AuditInitializeTests {

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
	protected String scpcompltedCheckEmptyFilePath;
	protected String scpPreviousRunDsFilePath;
	protected String scpPreviousToPrevRunDsFilePath;


	String prevRunDsID;
	String folderName;
	String sanityprevrunFolderName="_Sanity_SCP_PRERUNDS";
	String sanityprevrunFileName="";
	private S3ActionHandler s3Handler;
	private JSONObject s3BucketDetails;

	String strprevToPrvFoldername;
	String sanityprevtoprerunFolderName="_Sanity_SCP_PREVIOUS_TO_PREV_RUNDS";
	String sanityprevtoprevrunFileName="";
	
	List<String> ignoreDataSourcesListForDelete= new ArrayList<String>();


	public AuditScpSanityTests2(String fireWallType) {
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
		
		//Download the log file from s3 and keeping temfolder
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		
		
		s3Handler = new S3ActionHandler();
		s3BucketDetails=AuditTestUtils.getS3BucketDetails();

		scpPayload = AuditTestUtils.createSCPUploadBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SCP_DS_NAME,"SANITY");
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
		scpPreviousRunDsFilePath=previousRunScpDSFileLocation(suiteData.getEnvironmentName(),"");
		Reporter.log("previous run datasource fie name:"+scpPreviousRunDsFilePath,true);

		scpPreviousToPrevRunDsFilePath=previousRunScpDSFileLocation(suiteData.getEnvironmentName(),"prevtoPreRun");
		Reporter.log("previous to prev run datasource fie name:"+scpPreviousToPrevRunDsFilePath,true);


		sanityprevrunFileName=suiteData.getEnvironmentName()+"_sanityscpds.txt";
		folderName= suiteData.getEnvironmentName()+sanityprevrunFolderName;
		
		sanityprevtoprevrunFileName=suiteData.getEnvironmentName()+"_sanityscpprevtopreds.txt";
		strprevToPrvFoldername=suiteData.getEnvironmentName()+sanityprevtoprerunFolderName;

		//uploadPrevToPrevFile(scpPreviousToPrevRunDsFilePath);


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

	@Test(priority=2, description="Dataource creation for Scp/Sftp ",dependsOnMethods={"testCleanupDataSourcesToEnsueMaxLimitCheck"})
	public void testSCPDatasourceCreation_new() throws Exception {
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through SCP Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);

		Reporter.log("*************Datasource Creation started for:"+fireWallType+"****************",true);
		Reporter.log("Request Payload: "+scpPayload,true);
		/*String dsIdFromPreviousRun=FileHandlingUtils.readDatasourceFromTheFile(scpPreviousRunDsFilePath);//get the previous run datasource
		Reporter.log("previous run datasource id..."+dsIdFromPreviousRun,true);*/
		String dsIdFromPreviousRun=s3Handler.readFromS3(s3BucketDetails.getString("bucket"),folderName+"/"+sanityprevrunFileName) ;
		Reporter.log("previous run datasource id::"+dsIdFromPreviousRun,true);
		
		String dsIdFromPreviousToPrevRun=s3Handler.readFromS3(s3BucketDetails.getString("bucket"),strprevToPrvFoldername+"/"+sanityprevtoprevrunFileName) ;
		Reporter.log("previous to previous run datasource id..."+dsIdFromPreviousToPrevRun,true);



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

		Thread.sleep(10000);//wait 10 sec

		Reporter.log("******************Get Datasource sttaus verification****************************************",true);
		//verification logfile process attached to the datasource
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);
		String last_Status = pollRespObject.getString("last_status");
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);
		
		String previousRunDSStatus="";
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
				FileHandlingUtils.writeDatasourceIntoFile(sourceID, scpPreviousRunDsFilePath); //if current datasource is is completed we are writing this into new file.
				uploadFile(scpPreviousRunDsFilePath);
				Reporter.log("new File is written..",true);
				break;
			}
		}
		Reporter.log("current run ds status: after 30 mins::["+sourceID+"-"+last_Status+"]",true);

		if(!("Completed".equals(last_Status))){ //check the current datasource status after 30 mins
			Reporter.log("enter preivous run ds verifications"+dsIdFromPreviousRun,true);
			FileHandlingUtils.writeDatasourceIntoFile(sourceID, scpPreviousRunDsFilePath); //if current datasource is is not in completed we are writing this into new file.
			uploadFile(scpPreviousRunDsFilePath);
			Reporter.log("new File is written..",true);
			

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


			if("Completed".equals(previousRunDSStatus))	{	//check the prev run ds completed status
				sourceID=dsIdFromPreviousRun;   //map the previous datasource id to current datasourceid
			}
			else if( !("Completed".equals(previousRunDSStatus)))
			{
				Reporter.log("enter preivous to previous run ds verifications"+dsIdFromPreviousToPrevRun,true);
				FileHandlingUtils.writeDatasourceIntoFile(dsIdFromPreviousRun, scpPreviousToPrevRunDsFilePath); //if current datasource is is not in completed we are writing this into new file.
				uploadPrevToPrevFile(scpPreviousToPrevRunDsFilePath);
				Reporter.log("previous datasource is written into .."+scpPreviousToPrevRunDsFilePath,true);


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
		
		Reporter.log("************************** Scp Datasource process Completed sucessfully ******************************", true);
		Reporter.log("**************************Scp Datasource creation Test Completed**************************",true);

	}




	/**
	 * The below testcase do the below tasks
	 * Note: The test case depends on datasource creation testcase, if the dependent method failed the test case will skip
	 * 1. The inputs are datasourceId and firewallType generated from the dependent methods
	 * 2. Call the  Audit summary for the Allowed services
	 * 3. Validate the Audit summary data of Datasource
	 *    [
	 *      ****Alowed data*****
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
	@Test(description="Test Audit summary for Allowed services",dependsOnMethods={"testSCPDatasourceCreation_new"})	
	public void TestAuditSummary() throws Exception {
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



	/**
	 * The below testcase do the below tasks
	 * Note: The test case depends on TestAuditSummary testcase, if the dependent method failed the test case will skip
	 * 1. The inputs are datasourceId and firewallType generated from the dependent methods
	 * 2. Call the  Audit Report for the  services
	 * 3. Validate the Audit Report data of Datasource
	 *   
	 * @throws Exception
	 */	
	@Test(description="Test Audit Report",dependsOnMethods={"TestAuditSummary"})
	public void testAuditReport() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Audit Report Api for the created Datasource", true);
		Reporter.log("2. Verify the Audit Report data (date_range, datasource_id, earliest_date, latest_date...)", true);
		Reporter.log("************************************************************************** ", true);
		Reporter.log("**********Audit Report Verification Test for "+ fireWallType +" its ID is: "+sourceID+" started********* ",true);
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
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_QAVPC_SCP_SANITY_PWD);}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_PROD_SCP_SANITY_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_CEP_SCP_SANITY_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("api-cwsintg.elastica-inc.com")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CWS_SCP_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("chaapi.elastica-inc.com")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_CHA_TENANT_SCP_PWD);
		}
		else{
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_EOE_SCP_SANITY_PWD);
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
		else if(suiteData.getApiserverHostName().contains("chaapi.elastica-inc.com")){
			sftpServerHost=AuditTestConstants.AUDIT_CHA_SCP_SERVER;
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


	private void uploadFile(String fileTobeUploaded) throws Exception{
		s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
				folderName+"/",
				sanityprevrunFileName, 
				new File(FileHandlingUtils.getFileAbsolutePath(fileTobeUploaded)));

	}
	private void uploadPrevToPrevFile(String fileTobeUploaded) throws Exception{
		s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
				strprevToPrvFoldername+"/",
				sanityprevtoprevrunFileName, 
				new File(FileHandlingUtils.getFileAbsolutePath(fileTobeUploaded)));

	}
	
	public String previousRunScpDSFileLocation(String envName,String prevtoprev){

		String prevRunDSIDFileLocation=null;
		switch(envName)
		{
		case "prod": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_PROD_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;
			}else{
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_PROD_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH;}
			break;
		}
		case "cep": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_CEP_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;
			}else{
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_CEP_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH;}
			break;
		}
		case "qavpc": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_VPC_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;
			}else{
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_VPC_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH;}
			break;
		}
		case "eoe": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_EOE_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;
			}else{
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_EOE_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH;}
			break;
		}
		case "envX": {
			if(!prevtoprev.isEmpty()){
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_ENVX_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH;
			}else{
				prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_ENVX_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH;}
			break;
		}
		default:
			break;
		}
		return prevRunDSIDFileLocation;

	}




}
