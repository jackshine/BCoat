/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
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
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author Mallesh
 *
 */
public class AuditDSEqualityTests extends AuditInitializeTests{
	protected Client restClient;	
	protected String fireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected String scpPayload;
	protected String firewallLogFilePath;
	protected String sftpTenantUsername;
	protected String sftpServerHost;
	protected String sftpServerDestinationDir;
	protected Properties webUploadDSProps=new Properties();
	protected Properties scpUploadDSProps=new Properties();
	protected Properties s3UploadDSProps=new Properties();
	protected Map<String, JSONObject> webUploadDSAuditResults=new HashMap<String, JSONObject>();
	protected Map<String, JSONObject> scpUploadDSAuditResults=new HashMap<String, JSONObject>();
	protected Map<String, JSONObject> s3UploadDSAuditResults=new HashMap<String, JSONObject>();
	private S3ActionHandler s3Handler;
	private JSONObject S3PropertiesObj;
	protected String scpcompltedCheckEmptyFilePath;
	private JSONObject s3BucketDetails;
	
	
	/**
	 * @param FireWallName
	 */
	public AuditDSEqualityTests(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
		
	}	
	
	@BeforeClass(alwaysRun = true)
	public void init() throws Exception{
		

		//Download the log file from s3 and keeping temfolder
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		
		
		scpPayload = AuditTestUtils.createSCPUploadBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SCP_DS_NAME+"eq");
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
		
		
	/*	S3PropertiesObj = AuditTestUtils.buildS3CredentialCheckPostBody(fireWallType);
		s3Handler = new S3ActionHandler();
		s3Handler.uploadLogFileToS3(S3PropertiesObj.getString("bucket"),
				S3PropertiesObj.getString("input_folder")+"/", 
				AuditTestUtils.getFireWallLogFileName(fireWallType), 
				new File(FileHandlingUtils.getFileAbsolutePath(AuditTestUtils.getFirewallLogFilePath(fireWallType))));
		*/
		//construct s3 folder name
				s3Handler = new S3ActionHandler();
				String folderName=AuditTestUtils.getUniqFolderName(suiteData.getEnvironmentName(), "EqReg", suiteData.getTenantName());
				Reporter.log("Create S3 folder for ds equality test::"+folderName,true);

				//Upload file to S3 folder
				s3BucketDetails=AuditTestUtils.getS3BucketDetails();
				S3PropertiesObj = AuditTestUtils.buildS3CredentialCheckPostBody(fireWallType,folderName);
				s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
						folderName+"/",
						AuditTestUtils.getFireWallLogFileName(fireWallType), 
						new File(FileHandlingUtils.getFileAbsolutePath(AuditTestUtils.getFirewallLogFilePath(fireWallType))));
				

	}
	
	
	//@DataProvider(name="webUploadDSList")
	public Object[][] getWUDSList() throws Exception
	{
		
		Object[][] inputData = new Object[webUploadDSProps.size()][2];
		int j=0;

		for(String key:webUploadDSProps.stringPropertyNames() )
		{
			inputData[j][0] = key;
			inputData[j][1] = webUploadDSProps.get(key);
			j++;
		}
		Reporter.log("webupload dssources legth::"+inputData.length,true);
		return inputData;
	}
	
	@DataProvider(name="scpDSList")
	public Object[][] getSCPDSList() throws Exception
	{
		
		Object[][] inputData = new Object[scpUploadDSProps.size()][2];
		int j=0;

		for(String key:scpUploadDSProps.stringPropertyNames() )
		{
			inputData[j][0] = key;
			inputData[j][1] = scpUploadDSProps.get(key);
			j++;
		}
		Reporter.log("scp dssources legth::"+inputData.length,true);
		return inputData;
	}
	
	@DataProvider(name="s3DSList")
	public Object[][] getS3DSList() throws Exception
	{
		
		Object[][] inputData = new Object[s3UploadDSProps.size()][2];
		int j=0;

		for(String key:s3UploadDSProps.stringPropertyNames() )
		{
			inputData[j][0] = key;
			inputData[j][1] = s3UploadDSProps.get(key);
			j++;
		}
		Reporter.log("s3 dssources legth::"+inputData.length,true);
		return inputData;
	}

	
	/**
	 * @throws IOException
	 * @throws Exception
	 */
	//@Test(priority=1)
	public void createDataSourceTestForWebUpload() throws IOException, Exception{						
				
		Logger.info("Creating Data Source for: "+fireWallType);
		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(AuditTestUtils.createWebUploadPostBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_WU_DS_NAME+"eq")));		
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);		
		String createResponse = ClientUtil.getResponseBody(createResp);
		JSONObject createResponseObject = new JSONObject(createResponse);
		Assert.assertNotNull(createResponseObject.get("datasource_format"), "Data Source Format is null");
		Assert.assertNotNull(createResponseObject.get("datasource_type"), "Data Source Type is null");
		Assert.assertNotNull(createResponseObject.get("id"), "Data Source Id is null");
		Assert.assertFalse(((String) createResponseObject.get("id")).isEmpty(),"ID is empty");
		Assert.assertNotNull(createResponseObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) createResponseObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertNotNull(createResponseObject.get("setup_by"), "SetUp by is null");							
		Assert.assertFalse(((String) createResponseObject.get("datasource_format")).isEmpty(),"Data source format is empty");
		Assert.assertFalse(((String) createResponseObject.get("datasource_type")).isEmpty(),"Data source type is empty");		
		Assert.assertFalse(((String) createResponseObject.get("setup_by")).isEmpty(),"Set Up by is empty");
		Assert.assertEquals(createResponseObject.get("last_status"), "Pending Data","Last status is not \"Pending Data\"");
		Assert.assertEquals(createResponseObject.get("last_detect_status"), "Pending Data","Last status is not \"Pending Data\"");		
		sourceID = (String) createResponseObject.get("id");
				
		// Get Signed URL for the data Source
		Logger.info("Getting signed URl for "+fireWallType+" to upload");
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("filename", AuditTestUtils.getFireWallLogFileName(fireWallType)));
		queryParams.add(new BasicNameValuePair("filetype", "application/zip"));		
		HttpResponse signedURLResp = AuditFunctions.getSignedDataResourceURL(restClient, queryParams, sourceID);
		Assert.assertEquals(signedURLResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String signedURLRespString = ClientUtil.getResponseBody(signedURLResp);
		JSONObject signedURLObject = new JSONObject(signedURLRespString);		
		Assert.assertNotNull(signedURLObject.get("signed_request"),"Signed Request is null");
		Assert.assertNotNull(signedURLObject.get("url"),"Signed URL is null");		
		Assert.assertFalse(((String) signedURLObject.get("url")).isEmpty(),"URL is empty");
		Assert.assertFalse(((String) signedURLObject.get("signed_request")).isEmpty(),"Signed request is empty");		
		String signedURL = (String) signedURLObject.get("signed_request");
						
		// upload firewall using amazon S3 URL
		Logger.info("Uploading file using S3 signed url for "+ fireWallType);
		HttpResponse uploadFileResponse = AuditFunctions.uploadFirewallLogFile(restClient,signedURL.trim(), AuditTestUtils.getFirewallLogFilePath(fireWallType));
		Assert.assertEquals(uploadFileResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		
		// Notify successful upload
		Logger.info("Notifying the upload status for "+fireWallType);
		HttpResponse notifyResponse = AuditFunctions.notifyFileUploadStatus(restClient, sourceID);		
		Assert.assertEquals(notifyResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject notifyObject = new JSONObject(ClientUtil.getResponseBody(notifyResponse));
		Assert.assertEquals(notifyObject.get("message"), "Upload notification successful.");
		Assert.assertEquals(notifyObject.get("status"), "success");					
		
		// Poll for data source upload status
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || 
				"Processing".equals(last_Status))&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			//Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Logger.info("Last Status of "+ sourceID +" is "+ last_Status);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))	
			{
				webUploadDSProps.put(fireWallType, sourceID);
				break;
			}
		}
		
		
	
		if(!"Completed".equals(last_Status) || !"Failed".equals(last_Status))
			Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);
		}
	
	
	@Test(description="Dataource creation for Scp/Sftp ", priority=2)
	public void createDataSourceTestForSCP() throws Exception {
		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(scpPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject scpConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		validateCreatedDataSource(scpConnectionObject);
		sourceID = (String) scpConnectionObject.get("id");
		sftpServerDestinationDir=(String)scpConnectionObject.get("upload_path");

		// get credentials of scp/sftp connection
		HttpResponse getScpCredentialsResponse = AuditFunctions.getCredentials(restClient);
		Assert.assertEquals(getScpCredentialsResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK);
		JSONObject scpCredentialsMetaData = (JSONObject) new JSONObject(ClientUtil.getResponseBody(getScpCredentialsResponse)).getJSONArray("objects").get(0);	
		//validatescpCrentials(scpCredentialsMetaData);
		sftpTenantUsername=(String) scpCredentialsMetaData.get("username");
		//sftpServerHost=(String) scpCredentialsMetaData.get("server");
		sftpServerHost=getScpSftpServerHost();


		//upload file to the datasource using scp
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(sftpTenantUsername, sftpServerHost,sftpServerDestinationDir);
		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
		result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
		Reporter.log("sftp file upload status:  " + result);
		
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
		

		//verification logfile process attached to the datasource
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&&
				currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of "+ sourceID +" is "+ last_Status, true);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))	{
				scpUploadDSProps.put(fireWallType, sourceID);
				break;
			}
		}
	
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);

	}
	@Test(description="Dataource creation for S3 ", priority=3)
	public void createDataSourceWithS3() throws Exception{

		// Validate S3 Credentials
				
		HttpEntity entity = new StringEntity(S3PropertiesObj.toString());		
		HttpResponse response =  AuditFunctions.checkS3Credentials(restClient,entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject responseObject = new JSONObject(ClientUtil.getResponseBody(response));
		Assert.assertEquals(responseObject.getInt("reason"), 0, "Reason for failure is");
		
		// Create datasource
		JSONObject createS3DSBody = new JSONObject(AuditTestUtils.createS3UploadPostBody(fireWallType, S3PropertiesObj,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_S3_DS_NAME+"eq"));						
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
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of "+ sourceID +" is "+ last_Status, true);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))	{
				s3UploadDSProps.put(fireWallType, sourceID);
				break;}
		}
		
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);
		}	
	
	//@Test(priority=4,dependsOnMethods={"createDataSourceTestForWebUpload"},dataProvider="webUploadDSList")	
	public void testAuditSummaryForWebUpload(String transportationType, String datasourceID) throws Exception {
		Logger.info("Getting summary for "+ transportationType +" its ID is: "+datasourceID);
		String range = "1mo";			
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", datasourceID));
		HttpResponse response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		
		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);				
		Assert.assertTrue(summaryObject.getBoolean("is_valid"));		
		Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");
	
		summaryObject.put("datasource_id", "");
		summaryObject.put("id", "");
		summaryObject.put("resource_uri", "");
		
		webUploadDSAuditResults.put(transportationType, summaryObject);
		
	}
	
	@Test(priority=5,dependsOnMethods={"createDataSourceTestForSCP"},dataProvider="scpDSList")	
	public void testAuditSummaryForScp(String transportationType, String datasourceID) throws Exception {
		Logger.info("Getting summary for "+ transportationType +" its ID is: "+datasourceID);
		JSONObject summaryObject =null;
		HttpResponse response=null;
		
		response  = AuditFunctions.getSummary(new Client(), new StringEntity(AuditTestUtils.getAuditSummaryLatest(sourceID,"86400","1391385600","1393977599")));			
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		
		String strResponse=ClientUtil.getResponseBody(response);
		JSONArray jsonarray = new JSONArray(strResponse);
		summaryObject = jsonarray.getJSONObject(0);	
		
		
		/*
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);*/				
		//Assert.assertTrue(summaryObject.getBoolean("is_valid"));		
		Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");
		
		//The below attribute values are not simillar for the data sources which are created for same firewall type of different transport types. So equality summary comparision
		//we are keeping them as empty
		summaryObject.put("datasource_id", "");
		//summaryObject.put("id", "");
		//summaryObject.put("resource_uri", "");
		scpUploadDSAuditResults.put(transportationType, summaryObject);
		
		
		
	}
	
	@Test(priority=6,dependsOnMethods={"createDataSourceWithS3"},dataProvider="s3DSList")	
	public void testAuditSummaryForS3(String transportationType, String datasourceID) throws Exception {
		Logger.info("Getting summary for "+ transportationType +" its ID is: "+datasourceID);
		JSONObject summaryObject=null;
		HttpResponse response =null;
		
		response  = AuditFunctions.getSummary(new Client(), new StringEntity(AuditTestUtils.getAuditSummaryLatest(sourceID,"86400","1391385600","1393977599")));			
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		
		String strResponse=ClientUtil.getResponseBody(response);
		JSONArray jsonarray = new JSONArray(strResponse);
		summaryObject = jsonarray.getJSONObject(0);	
		//Assert.assertTrue(summaryObject.getBoolean("is_valid"));		
		Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");
		
		//The below attribute values are not simillar for the data sources which are created for same firewall type of different transport types. So equality summary comparision
		//we are keeping them as empty
		summaryObject.put("datasource_id", "");
		//summaryObject.put("id", "");
		//summaryObject.put("resource_uri", "");
		s3UploadDSAuditResults.put(transportationType, summaryObject);
		
		
		
	}
	
	//@Test(priority=5,dependsOnMethods={"testAuditSummaryForWebUpload","testAuditSummaryForScp"})	
	public void testCompareAuditResultsOfSameFiewallForWebUploadAndScp() throws Exception {
		
       if(webUploadDSAuditResults!= null && scpUploadDSAuditResults!=null )
       {
    	   Reporter.log("webUploadDSAuditResults:"+webUploadDSAuditResults,true);
    	   Reporter.log("scpUploadDSAuditResults:"+scpUploadDSAuditResults,true);
    	   compareDatsourcesForSameFirewallType(webUploadDSAuditResults,scpUploadDSAuditResults);
    		
       }
	}
	
	@Test(priority=6,dependsOnMethods={"testAuditSummaryForScp","testAuditSummaryForS3"})	
	public void testCompareAuditResultsOfSameFiewallForScpAndS3() throws Exception {
		
       if(scpUploadDSAuditResults!=null && s3UploadDSAuditResults!=null )
       {
    	   Reporter.log("scpUploadDSAuditResults:"+scpUploadDSAuditResults,true);
    	   Reporter.log("s3UploadDSAuditResults:"+s3UploadDSAuditResults,true);
    	   compareDatsourcesForSameFirewallType(scpUploadDSAuditResults,s3UploadDSAuditResults);
    		
       }
	}
	

	public static void compareDatsourcesForSameFirewallType(Map<String, JSONObject> mapWebUpload, Map<String, JSONObject> mapScpUpload) throws Exception{
		
		for ( Map.Entry<String, JSONObject> webuploadEntry : mapWebUpload.entrySet() ) {
			   if ( mapScpUpload.containsKey(webuploadEntry.getKey()) ) {
			 	   Assert.assertTrue(AuditTestUtils.jsonsEqual(webuploadEntry.getValue(),mapScpUpload.get(webuploadEntry.getKey())),"two datsources are verified");
			
			   }
			}
		
	}
	
	
	 //* This test case deletes the data source
	 
   @Test(dependsOnMethods={"testCompareAuditResultsOfSameFiewallForScpAndS3"},priority=7,dataProvider="s3DSList")
	public void deleteS3DataSourceTest(String transportionType, String datasourceId) throws Exception {
		Logger.info("Deleting Data Source "+ transportionType +" its ID is: "+datasourceId);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, datasourceId);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
	}
   
   @Test(dependsOnMethods={"testCompareAuditResultsOfSameFiewallForScpAndS3"},priority=7,dataProvider="scpDSList")
  	public void deleteSCPDataSourceTest(String transportionType, String datasourceId) throws Exception {
  		Logger.info("Deleting Data Source "+ transportionType +" its ID is: "+datasourceId);
  		HttpResponse response = AuditFunctions.deleteDataSource(restClient, datasourceId);
  		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
  	}
   

@AfterClass
	public void deleteS3File() throws Exception {
  		Thread.sleep(30000);
		s3Handler.deleteLogFileFromS3(S3PropertiesObj.getString("bucket"),
				S3PropertiesObj.getString("input_folder")+"/", 
				AuditTestUtils.getFireWallLogFileName(fireWallType));
	}
	
	
	
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
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_VPC_PWD);}
        else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD);
        }
        else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CEP_SCP_PWD);
        }
        else if(suiteData.getTenantName().contains("auditweeklyscpco") && suiteData.getEnvironmentName().contains("eoe") ){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_EOE_SPANVA_ING_SCP_PWD);
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
	
	
}
