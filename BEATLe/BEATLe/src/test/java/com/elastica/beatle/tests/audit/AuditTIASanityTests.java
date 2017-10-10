/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.elasticsearch.common.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
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
import com.elastica.beatle.audit.TIA_IOI_Code;
import com.elastica.beatle.audit.TestDsDelete;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.es.ESQueryBuilder;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author Mallesh
 *
 */
public class AuditTIASanityTests extends AuditInitializeTests
{
	protected Client restClient;	
	protected String FireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	ESQueryBuilder esQueryBuilder;
	private static final String OBJECTS = "audit_preferences";
	private static final String TIA_SUCESS_LAST_DETECT_MSG="Anomalies analysis completed";
	private static final String TIA_FAILED_LAST_DETECT_MSG="Anomalies analysis failed";
	@SuppressWarnings("unused")
	private static final String EXPECTED_TIA_LAST_DETECT_STATUS="Completed";

	String prevRunDsID;
	String folderName;
	String sanityprevrunFolderName="_Sanity_WU_PRERUNDS";
	String sanityprevrunFileName="";
	private S3ActionHandler s3Handler;
	private JSONObject s3BucketDetails;
	protected String wuPreviousRunDsFilePath;
	
	List<String> ignoreDataSourcesListForDelete= new ArrayList<String>();
	
	
	

	/**
	 * @param FireWallName
	 */
	public AuditTIASanityTests(String FireWallName) {
		restClient = new Client();
		this.FireWallType = FireWallName;
		esQueryBuilder = new ESQueryBuilder();
	
	}	
	
	@BeforeClass(alwaysRun=true)
	public void init() throws Exception
	{
		
		//Download the log file from s3 and keeping temfolder
		AuditFunctions.DownloadFileFormS3(FireWallType);
		Thread.sleep(30000);
		
		
		s3Handler = new S3ActionHandler();
		s3BucketDetails=AuditTestUtils.getS3BucketDetails();
		wuPreviousRunDsFilePath=previousRunWUDSFileLocation(suiteData.getEnvironmentName());
		Reporter.log("webupload previous run datasource fie name:"+wuPreviousRunDsFilePath,true);
		
		sanityprevrunFileName=suiteData.getEnvironmentName()+"_sanitywuds.txt";
		folderName= suiteData.getEnvironmentName()+sanityprevrunFolderName;
		
		
	}

	//@Test(priority=1)
	public void setTIAPreferences() throws Exception {
		Reporter.log("********************************* setTIAPreferences:Test Description ****************************************************** ", true);
		Reporter.log("***************TIA preferences Reset Test to the preference: TOO_MANY_LOW_REPUTATION_DESTINATIONS *************************************",true);
		Reporter.log("***************Resetting the preferences (threshold=1, window=3 mins, importance=important(2) ) to the preference TOO_MANY_LOW_REPUTATION_DESTINATIONS  *************************************",true);
		//AttributeBean(Integer threshold, Integer window, Integer importance, boolean enabled)
		AttributeBean attributeBean = new AttributeBean(1, 3, 2, true);
		String responseBody;
		Reporter.log("get All the TIA preferences",true);
		HttpResponse resp = AuditFunctions.getTIAAuditPreferences(restClient);
		responseBody = ClientUtil.getResponseBody(resp);
		Reporter.log("get All the TIA preference Response..."+responseBody,true);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = TIA_IOI_Code.TOO_MANY_LOW_REPUTATION_DESTINATIONS.toString();
		boolean enabled = attributeBean.isEnabled();

		Reporter.log("************update the preferences setting for the preference: TOO_MANY_LOW_REPUTATION_DESTINATIONS",true);
		AuditTestUtils.updateTIAAttributes(enabled, attributeBean, getResponseArray, ioi_Code);
		Reporter.log("************Preferences setting are updated***********",true);

	}
	/**
	 * 
	 * @throws IOException
	 * @throws Exception
	 * process the datasource
	 */
	
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
	@Test(priority=2,dependsOnMethods={"testCleanupDataSourcesToEnsueMaxLimitCheck"})
	//@Test
	public void createDataSourceTestForTIALogs() throws IOException, Exception{	

		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create TIA Datasource through WebUpload Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);

		Reporter.log("*************Datasource Creation started for:"+FireWallType+"****************",true);
		
		String dsIdFromPreviousRun=s3Handler.readFromS3(s3BucketDetails.getString("bucket"),folderName+"/"+sanityprevrunFileName) ;
		Reporter.log("wu previous run datasource id::"+dsIdFromPreviousRun,true);


		String requestPayload=AuditTestUtils.createWebUploadPostBody(FireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_WU_DS_NAME,"SANITY");
		Reporter.log("Request Payload: "+requestPayload,true);

		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(requestPayload));		
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);		
		String createResponse = ClientUtil.getResponseBody(createResp);
		Reporter.log("Actual Datasource Response:"+createResponse,true);
		JSONObject createResponseObject = new JSONObject(createResponse);


		//Expected values preparation
		JSONObject expectedDSResponse = new JSONObject(requestPayload);
		String expected_str_datasource_name=(String)expectedDSResponse.get("name");
		String expected_str_datasource_format=(String)expectedDSResponse.get("datasource_format");
		String expected_str_datasource_type=(String)expectedDSResponse.get("datasource_type");
		String expected_str_setup_by=suiteData.getUsername();
		String str_last_status_or_detectstatus="Pending Data";
		String expectedResponse=    " [Datasourcename=" + expected_str_datasource_name+
				", DatasourceFormat=" + expected_str_datasource_format +
				", DatasourceType=" + expected_str_datasource_type +
				", SetupBy=" + expected_str_setup_by +
				", last_status=" + str_last_status_or_detectstatus +
				", last_detect_status=" + str_last_status_or_detectstatus+" ]";
		Reporter.log("Expected Datasource Response fields:"+expectedResponse,true);
		Assert.assertEquals(createResponseObject.get("name"), expected_str_datasource_name);
		Assert.assertNotNull(createResponseObject.get("datasource_format"), "Data Source Format is null");
		Assert.assertFalse(((String) createResponseObject.get("datasource_format")).isEmpty(),"Data source format is empty");
		Assert.assertEquals(createResponseObject.get("datasource_format"), expected_str_datasource_format);
		Assert.assertNotNull(createResponseObject.get("datasource_type"), "Data Source Type is null");
		Assert.assertFalse(((String) createResponseObject.get("datasource_type")).isEmpty(),"Data source type is empty");	
		Assert.assertEquals(createResponseObject.get("datasource_type"), expected_str_datasource_type);
		Assert.assertNotNull(createResponseObject.get("setup_by"), "SetUp by is null");
		Assert.assertFalse(((String) createResponseObject.get("setup_by")).isEmpty(),"Set Up by is empty");
		Assert.assertEquals(createResponseObject.get("setup_by"), expected_str_setup_by);
		Assert.assertNotNull(createResponseObject.get("id"), "Data Source Id is null");
		Assert.assertFalse(((String) createResponseObject.get("id")).isEmpty(),"ID is empty");
		Assert.assertNotNull(createResponseObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) createResponseObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertEquals(createResponseObject.get("last_status"), str_last_status_or_detectstatus,"Last status is not \"Pending Data\"");
		Assert.assertEquals(createResponseObject.get("last_detect_status"), str_last_status_or_detectstatus,"Last status is not \"Pending Data\"");	


		sourceID = (String) createResponseObject.get("id");

		// Get Signed URL for the data Source
		Logger.info("Getting signed URl for "+FireWallType+" to upload");
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("filename", AuditTestUtils.getFireWallLogFileName(FireWallType)));
		queryParams.add(new BasicNameValuePair("filetype", "application/zip"));		
		HttpResponse signedURLResp = AuditFunctions.getSignedDataResourceURL(restClient, queryParams, sourceID);
		Assert.assertEquals(signedURLResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String signedURLRespString = ClientUtil.getResponseBody(signedURLResp);
		Reporter.log("Actual signedURLResp:"+signedURLRespString,true);
		JSONObject signedURLObject = new JSONObject(signedURLRespString);	
		String expectedsignedURLResponse=    " [signed_request=" + "Should not be null"+
				", url=" + "should not be null" +
				", signed_request=" +"signed_request is not empty" +
				", url=" + "url is not empty"+" ]";
		Reporter.log("Expected signedURLResp:"+expectedsignedURLResponse,true);

		Assert.assertNotNull(signedURLObject.get("signed_request"),"Signed Request is null");
		Assert.assertNotNull(signedURLObject.get("url"),"Signed URL is null");		
		Assert.assertFalse(((String) signedURLObject.get("url")).isEmpty(),"URL is empty");
		Assert.assertFalse(((String) signedURLObject.get("signed_request")).isEmpty(),"Signed request is empty");		
		String signedURL = (String) signedURLObject.get("signed_request");

		// upload firewall using amazon S3 URL
		Logger.info("Uploading file using S3 signed url for "+ FireWallType);
		HttpResponse uploadFileResponse = AuditFunctions.uploadFirewallLogFile(restClient,signedURL.trim(), AuditTestUtils.getFirewallLogFilePath(FireWallType));
		Assert.assertEquals(uploadFileResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		

		// Notify successful upload
		Logger.info("Notifying the upload status for "+FireWallType);
		HttpResponse notifyResponse = AuditFunctions.notifyFileUploadStatus(restClient, sourceID);		
		Assert.assertEquals(notifyResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject notifyObject = new JSONObject(ClientUtil.getResponseBody(notifyResponse));
		Reporter.log("Actual Notifying the upload status Response:"+notifyObject,true);
		String uploadNotification="Upload notification successful.";
		String sucess="success";
		String expectedNotifyUploadResp= " [uploadNotification=" + uploadNotification+", sucess=" + sucess+" ]";
		Reporter.log("Expected Notifying the upload status Response:"+expectedNotifyUploadResp,true);
		Assert.assertEquals(notifyObject.get("message"), uploadNotification);
		Assert.assertEquals(notifyObject.get("status"), sucess);					

		// Poll for data source upload status
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);
		String last_Status = pollRespObject.getString("last_status");
		String last_detect_msg ="";
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME){
			Reporter.log("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			last_detect_msg=pollRespObject.getString("last_detect_message");
			Logger.info("Actual Last Status & tia anomaly status of Datasource: "+ sourceID +" is "+ last_Status+","+last_detect_msg);
			
			if(  ("Completed".equals(last_Status) || "Failed".equals(last_Status) )){//checking the datasource status
				Thread.sleep(600000);// wait 10 mins here for tia process
				pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
				Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
				pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
				
				last_detect_msg=pollRespObject.getString("last_detect_message");
				Logger.info("tia anomaly status of Datasource: "+ sourceID +" is "+last_detect_msg);
				if(TIA_SUCESS_LAST_DETECT_MSG.equals(last_detect_msg))
				{
					Reporter.log("enter into TIA anomalies completed section:",true);
					FileHandlingUtils.writeDatasourceIntoFile(sourceID, wuPreviousRunDsFilePath); //if current datasource is is completed we are writing this into new file.
					uploadFile(wuPreviousRunDsFilePath);
					Reporter.log("new File is written..",true);
					break;
				}
				else{
					Reporter.log("enter into TIA failed section:"+last_detect_msg,true);
					Assert.assertEquals(last_detect_msg,TIA_SUCESS_LAST_DETECT_MSG,"Expected TIA last_detect_message is:"+TIA_SUCESS_LAST_DETECT_MSG+" but found:"+last_detect_msg);
				
				}
			}
			
			
		/*	if(  ("Completed".equals(last_Status) || "Failed".equals(last_Status)) && 
				 (TIA_SUCESS_LAST_DETECT_MSG.equals(last_detect_msg) ||TIA_FAILED_LAST_DETECT_MSG.equals(last_detect_msg) )
			  )	
				{
				FileHandlingUtils.writeDatasourceIntoFile(sourceID, wuPreviousRunDsFilePath); //if current datasource is is completed we are writing this into new file.
				uploadFile(wuPreviousRunDsFilePath);
				Reporter.log("new File is written..",true);
				break;
				}*/
			
		}
		
		Reporter.log("current run ds status: after 30 mins::["+sourceID+"-"+last_Status+"]",true);

		if(!("Completed".equals(last_Status))){ //check the current datasource status after 30 mins
			Reporter.log("enter preivous run ds verifications"+dsIdFromPreviousRun,true);
			FileHandlingUtils.writeDatasourceIntoFile(sourceID, wuPreviousRunDsFilePath); //if current datasource is is not in completed we are writing this into new file.
			uploadFile(wuPreviousRunDsFilePath);
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

			if("Completed".equals(previousRunDSStatus))	{	//check the prev run ds completed status
				sourceID=dsIdFromPreviousRun;   //map the previous datasource id to current datasourceid
			}
			else{ //if the prevous run ds staus also not completed then failing the test with both previous and current ds id's with status.
				pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
				Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
				pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
				last_Status = pollRespObject.getString("last_status");
				Assert.assertTrue(false," Current Datasoure process took more than "+
						(int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. Current Datasource id - status:["+sourceID+"-"+last_Status+"]" +" and TIA anomaly status was: "+last_detect_msg+",previous run Datasource id - status:["+dsIdFromPreviousRun+"-"+previousRunDSStatus+"]");

			}
		}
		
		/*
		if(    ( !"Completed".equals(last_Status) || !"Failed".equals(last_Status)) &&
			   (!TIA_SUCESS_LAST_DETECT_MSG.equals(last_detect_msg) || !TIA_FAILED_LAST_DETECT_MSG.equals(last_detect_msg) ) 
		   )
			Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME," File processing took more than "+  (int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. Last status of this source file was "+last_Status+" and TIA anomaly status was: "+last_detect_msg);
		Assert.assertEquals(pollRespObject.get("last_status"), "Completed","create data soure procesing is not completed. Last status of this source file was "+last_Status);				
		Assert.assertNotNull(pollRespObject.get("resource_uri"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("resource_uri")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_format"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_format")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_transport"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_transport")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.getString("log_transport"), AuditTestConstants.AUDIT_WEBUPLOAD_FILEFORMAT,"Log Transport method doesn't match. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("id"), "Data Source Id is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("id")).isEmpty(),"ID is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.get("last_status_message"), "Logs processed successfully","Logs processing was not successful. Last status of this source file was "+last_Status);
*/
		ignoreDataSourcesListForDelete.add(sourceID);
		ignoreDataSourcesListForDelete.add(dsIdFromPreviousRun);
		Reporter.log("************************** Datasource process & TIA anomaly status completed sucessfully ******************************", true);
		Reporter.log("**************************Datasource creation Test Completed**************************",true);


	}
	
	@Test(priority=3,dependsOnMethods={"createDataSourceTestForTIALogs"})	
	public void testAuditSummary() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Audit summary Api for the created Datasource", true);
		Reporter.log("2. Verify the Audit summary data (date_range, id, earliest_date, latest_date...)", true);
		Reporter.log("************************************************************************** ", true);
		Reporter.log("**********Audit Summary Verification Test for "+ FireWallType +" its ID is: "+sourceID+" started********* ",true);
		
		String range = "1mo";
		HttpResponse response=null;
		JSONObject summaryObject=null;
		
		response  = AuditFunctions.getSummary(restClient, new StringEntity(AuditTestUtils.getAuditSummaryLatest(sourceID,"86400","1456963200","1459555199")));				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		
		String strResponse=ClientUtil.getResponseBody(response);
		JSONArray jsonarray = new JSONArray(strResponse);
		summaryObject = jsonarray.getJSONObject(0);	
		
		
		//summary validation
		Reporter.log("Actual Audit summary Api Response:"+summaryObject,true);
		String expectedAuditSummaryResponse=    " [datasource_id=" + "Datasource Id not be null/empty"+
                ", date_range=" + range +
                ", earliest_date=" + "earliest_date not be null" +
                ", latest_date=" + "latest_date not be null" +
                ", resource_uri=" +"resource_uri is not empty" +"]";
		Reporter.log("Expected Audit summary few fields:"+expectedAuditSummaryResponse,true);
		
		Assert.assertEquals(summaryObject.get("datasource_id"), sourceID);										   
		/*Assert.assertEquals(summaryObject.get("date_range"), range);
		Assert.assertNotNull(summaryObject.get("id"), "Id is null");
		Assert.assertFalse(((String) summaryObject.get("id")).isEmpty(),"ID is empty");
		Assert.assertNotNull(summaryObject.get("resource_uri"),"resource_uri is null");
		Assert.assertFalse(((String)summaryObject.get("resource_uri")).isEmpty(),"resource_uri is empty");*/
		//Assert.assertTrue(summaryObject.getBoolean("is_valid"));		
		Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");	
	
		Reporter.log("**************************Audit Summary  Test Completed**************************",true);
		

	}
	
	@Test(priority=4,dependsOnMethods={"testAuditSummary"})
	public void testAuditReport() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Audit Report Api verification test for the created Datasource", true);
		Reporter.log("2. Verify the Audit Report data (date_range, datasource_id, earliest_date, latest_date...)", true);
		Reporter.log("************************************************************************** ", true);
		Reporter.log("**********Audit Report Verification Test for "+ FireWallType +" its ID is: "+sourceID+" started********* ",true);
		
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		String range = "1mo";
		HttpResponse response =null;
		JSONObject reportObject=null;
		
		response  = AuditFunctions.getAuditReport(restClient, new StringEntity(AuditTestUtils.getAuditReportPayloadLatest(sourceID,"86400","1456963200","1459555199")));				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);

		String strResponse=ClientUtil.getResponseBody(response);
		JSONArray jsonarray = new JSONArray(strResponse);
		reportObject = jsonarray.getJSONObject(0);
		Reporter.log("Actual Audit Report Response:"+reportObject,true);
		
		String expectedAuditReportResponse=    " [datasource_id=" + "Datasource Id not be null/empty"+
                ", date_range=" + range +
                ", earliest_date=" + "earliest_date not be null" +
                ", latest_date=" + "latest_date not be null" +
                ", generated_date=" +"generated_date not be null" +"]";
		Reporter.log("Expected Audit Report:"+expectedAuditReportResponse,true);
		
		//Report validations
		Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
		//Assert.assertEquals(reportObject.get("date_range"), range);
		Assert.assertNotNull(reportObject.get("earliest_date"),"earliest date is null");		
		//Assert.assertNotNull(reportObject.get("generated_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("latest_date"),"Latest date is null");	
		
		
		Reporter.log("**************************Audit Report Verification Test Completed**************************",true);
		
	}


	//@Test(priority=5,dependsOnMethods={"testAuditReport"})	
	public void testValidateTIAIncidents() throws Exception{
		Reporter.log("********************************* testValidateTIAIncidents:Test Description ****************************************************** ", true);
		Reporter.log("*****************Validate  TIA Incidents started ***************************",true);
		//validateIncidents("",TIA_IOI_Code.TOO_MANY_LOW_REPUTATION_DESTINATIONS.toString());
		Reporter.log("********************************* Completed testValidateTIAIncidents ****************************************************** ", true);
		
		//clear the incident
		//clearTIAIncident();
	}
	
	
	public void clearTIAIncident() throws Exception {
		Reporter.log("********************************* clearTIAIncident:Test Description ****************************************************** ", true);
		Reporter.log("***************Cleanup up test for all the incidents for last 6 months for the tenant*************************************",true);
		Reporter.log("Get all the TIA incidents for the last 6 months************",true);
		String responseBody =  getListOfIncidents();
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		if (jnode.isArray()) {
			for (final JsonNode objNode : jnode) {

				String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
				String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
				String facilty = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
				String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
				String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
				String source = getJSONValue(objNode.toString(), "src").toString().replace("\"", "");
				String attr_set = getJSONValue(objNode.toString(), "ats").toString().replace("\"", "");
				String Activity_type = getJSONValue(objNode.toString(), "at").toString().replace("\"", "");
				String ObjectType = getJSONValue(objNode.toString(), "ot").toString().replace("\"", "");

				Reporter.log("facilty:"+facilty+" email:"+email+" index:"+index+" source:"+source+" attr_set:"+attr_set+" Activity_type:"+Activity_type+" ObjectType:"+ObjectType,true);
				updateLog(report_id, severity, ObjectType, email, Activity_type, index, facilty, source, attr_set);
				Reporter.log("*************Completed TIA incidents cleanup test************",true);

			}
		}
		else{
			Reporter.log("*************No TIA Preferences are available to cleanup***********",true);
		}

	}

	

	public void validateIncidents(String user, String ioi_Code) throws Exception {
		Reporter.log("Sleep 1 min for generated incidents************",true);
		String responseBody =  getListOfIncidents();
		boolean isIncidentsListed=false;
		isIncidentsListed=isIncidentListed(responseBody,ioi_Code);
		
		long totalWaitTime=10*60*1000;
		long incidentsWaittime=1* 60* 1000;
		long currentWaitTime=0;
		Reporter.log("Incidents generated -"+isIncidentsListed,true);
		while(!(isIncidentsListed) && currentWaitTime <= totalWaitTime){
			Reporter.log("Incidents generated Wait Time*************** :"+currentWaitTime,true);
			Thread.sleep(incidentsWaittime);
			currentWaitTime += incidentsWaittime;
			isIncidentsListed=isIncidentListed(responseBody,ioi_Code);
			if(isIncidentsListed)
			{
				Reporter.log("Incidents generated -time["+isIncidentsListed+"-"+currentWaitTime+"]",true);
				break;
			}
			
		}
		if(isIncidentsListed){//then do the validation
			JsonNode jnode = unmarshall(responseBody, JsonNode.class);
			if (jnode.isArray()) {
				for (final JsonNode objNode : jnode) {

					String actualSeverity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
					String expectedSeverity="low";
					Assert.assertEquals(actualSeverity, expectedSeverity," Expected severity: "+expectedSeverity+" but found:"+actualSeverity);

					String actualService = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
					String expectedService="Across Services";
					Assert.assertEquals(actualService, expectedService," Expected service: "+expectedService+" but found:"+actualService);

					String usr = getJSONValue(objNode.toString(), "u").toString().replace("\"", "");
					//Assert.assertEquals(usr, "UserTIA30 Hilton");

					String date = getJSONValue(objNode.toString(), "its").toString().replace("\"", "");
					Assert.assertNotNull(date,"timestamp is null");

					String incidentType = getJSONValue(objNode.toString(), "m").toString().replace("\"", "");
					Assert.assertTrue(incidentType.contains(TIA_IOI_Code.TOO_MANY_LOW_REPUTATION_DESTINATIONS.getMessage()));

					String threadScore = getJSONValue(objNode.toString(), "ar").toString().replace("\"", "");
					Assert.assertNotNull(threadScore,"Threadscore is null");

					Reporter.log("severity:"+actualSeverity+" service:"+actualService+" usr:"+usr+" date:"+date+"incidentType "+incidentType+"threadScore:"+threadScore, true);

				}
			}
		}
		else{
			Assert.assertTrue(isIncidentsListed,"Incidents not generated to the preference:("+ioi_Code+") even after "+(int)((totalWaitTime / (1000*60)) % 60)+" minutes wait time");
		}
		//return false;
	}

	//test listed incidents


	public String getListOfIncidents() {
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
		formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
		String strDateTimeTo = formatDate.format(dateTo);
		Date dateFrom = fromDateRange(dateTo,DateFilterEnum._6MONTHS);
		String strDateTimeFrom = formatDate.format(dateFrom);
		Reporter.log("Capture TIA Incidents in the date range between:  "+(strDateTimeFrom+"-"+strDateTimeTo),true);

		HttpResponse response;
		try {

			String payload = esQueryBuilder.getSearchQueryForTIALogs(strDateTimeFrom + ":00:00.000Z",
					strDateTimeTo + ":59:59.999Z");
			response = esLogs.getCloudServiceAnomalies(restClient, AuditFunctions.buildBasicHeaders(), new StringEntity(payload),suiteData.getHost());
			Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
			responseBody = ClientUtil.getResponseBody(response);
			Reporter.log(" List of Incidents::::::  Response::::   " + responseBody, true);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseBody;
	}

	public Boolean isIncidentListed(String responseBody, String ioi_Code) throws Exception {
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		System.out.println("isIncidentListed Respon : " + responseBody);
		if (jnode.isArray()) {
			for (final JsonNode objNode : jnode) {
				String ioi = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				//String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");

				if ( ioi.equals(ioi_Code)) {
					return true;
				}
			}
		}

		return false;
	}


	enum DateFilterEnum
	{
		_1DAY, _1WEEK, _1MONTH, _3MONTHS, _6MONTHS, ALL 
	}

	public static Date fromDateRange(Date date,DateFilterEnum dateFilter)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch (dateFilter) {
		case _1DAY:
			cal.add(Calendar.DATE, -1);
			break;
		case _1WEEK:
			cal.add(Calendar.DATE, -7);
			break;
		case _1MONTH:
			cal.add(Calendar.MONTH, -1);
			break;
		case _3MONTHS:
			cal.add(Calendar.MONTH, -3);
			break;
		case _6MONTHS:
			cal.add(Calendar.MONTH, -6);
			break;
		case ALL:
			cal.add(Calendar.MONTH, -36);
			break;
		default:
		}
		return cal.getTime();
	}

	public  void updateLog(String id, String severity, String Object_type, String user, 
			String Activity_type, String index,String facility,String source,String attr_set) throws Exception {
		Reporter.log("************Started incidents Cleanup*************************",true);

		String value = "{\"id\":\"%s\",\"type\":\"elastica_state\",\"severity\":\"%s\",\"Object_type\":\"%s\",\"user\":\"%s\",\"Activity_type\":\"%s\",\"index\":\"%s\",\"facility\":\"%s\",\"child_drs\":[\"\"],"
				+ "\"__source\":\"%s\",\"attr_set\":\"%s\",\"anomaly_status\":1,\"event_type\":\"AnomalyReport\",\"message\":\"\",\"notes\":\"User changed'Verified Alert?' from  Unknown to Yes(Cleared Alert)\",\"updated_sev\":\"informational\"}";
		String value1 = String.format(value, id, severity, Object_type, user, Activity_type, index,facility,source,attr_set);
		JSONObject object = new JSONObject() ;
		object.put("source", value1);

		StringEntity se = new StringEntity(object.toString());
		Reporter.log("incidents Cleanup Request Body  ::::: " + object.toString(), true);
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		HttpResponse response =	esLogs.updateLog(restClient, AuditFunctions.buildBasicHeaders(), se, suiteData.getHost());
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("incidents Cleanup Response   ::::: " + responseBody, true);
	}

	public <T> T unmarshall(String data, final Class<T> klass) throws JAXBException {		
		return unmarshallJSON(data, klass);		
	}

	protected static <T> T unmarshallJSON(final String json, final Class<T> klass) {
		final ObjectMapper mapper = new ObjectMapper();
		final AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		// make deserializer use JAXB annotations (only)
		mapper.setAnnotationIntrospector(introspector);
		// make serializer use JAXB annotations (only)		
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			return mapper.readValue(json, klass);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public  String getJSONValue(String json, String key){
		JsonFactory factory = new JsonFactory();

		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode rootNode;
		try {
			rootNode = mapper.readTree(json);
			return  rootNode.get(key).toString();
		} catch (Exception e) {
			Reporter.log("key::::::    "+key, true);
			Reporter.log("error::::  "+e.getMessage(), true);
			//e.printStackTrace();
		}
		return null;


	}
	
	public String previousRunWUDSFileLocation(String envName){

		String prevRunDSIDFileLocation=null;
		switch(envName)
		{
		case "prod": {
			prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_PROD_WU_PREVIOUS_RUN_DSID_LOCATION_PATH;
			break;
		}
		case "cep": {
			prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_CEP_WU_PREVIOUS_RUN_DSID_LOCATION_PATH;
			break;
		}
		case "qavpc": {
			prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_VPC_WU_PREVIOUS_RUN_DSID_LOCATION_PATH;
			break;
		}
		case "eoe": {
			prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_EOE_WU_PREVIOUS_RUN_DSID_LOCATION_PATH;
			break;
		}
		case "envX": {
			prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_ENVX_WU_PREVIOUS_RUN_DSID_LOCATION_PATH;
			break;
		}
		default:
			break;
		}
		return prevRunDSIDFileLocation;

	}
	
	private void uploadFile(String fileTobeUploaded) throws Exception{
		s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
				folderName+"/",
				sanityprevrunFileName, 
				new File(FileHandlingUtils.getFileAbsolutePath(fileTobeUploaded)));

	}



}