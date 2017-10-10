/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author mallesh
 *
 */
public class AuditAccessRoleBasedTests extends AuditInitializeTests{
	protected Client restClient;	
	protected String fireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();
	protected Properties datasourcesProps=new Properties();



	/**
	 * @param FireWallName
	 */
	public AuditAccessRoleBasedTests(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
	}		

	/**
	 * @throws IOException
	 * @throws Exception
	 */
	@Test()
	public void createDataSourceTest() throws IOException, Exception{	

		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through WebUpload Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);

		Reporter.log("*************Datasource Creation started for:"+fireWallType+"****************",true);
		String requestPayload=AuditTestUtils.createWebUploadPostBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_WU_DS_NAME);
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
		Logger.info("Getting signed URl for "+fireWallType+" to upload");
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("filename", AuditTestUtils.getFireWallLogFileName(fireWallType)));
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

		// Download file using amazon S3 URL
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		
		// upload firewall using amazon S3 URL
		Logger.info("Uploading file using S3 signed url for "+ fireWallType);
		HttpResponse uploadFileResponse = AuditFunctions.uploadFirewallLogFile(restClient,signedURL.trim(), AuditTestUtils.getFirewallLogFilePath(fireWallType));
		Assert.assertEquals(uploadFileResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		

		// Notify successful upload
		Logger.info("Notifying the upload status for "+fireWallType);
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
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Reporter.log("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Logger.info("Actual Last Status of Datasource: "+ sourceID +" is "+ last_Status);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))	{
				datasourcesProps.put(fireWallType, sourceID);
				break;
			}
				
		}

		Reporter.log("************************** Datasource process Completed sucessfully ******************************", true);
		Reporter.log("**************************Datasource creation Test Completed**************************",true);
	}
	
	@DataProvider(name="dsSources")
	public Object[][] getSCPDSList() throws Exception
	{
		//datasourcesProps.put(fireWallType, "572a14d906a43c0c1141fc62");
		Object[][] inputData = new Object[datasourcesProps.size()][2];
		int j=0;

		for(String key:datasourcesProps.stringPropertyNames() )
		{
			inputData[j][0] = key;
			inputData[j][1] = datasourcesProps.get(key);
			j++;
		}
		Reporter.log("scp dssources legth::"+inputData.length,true);
		return inputData;
	}
	
	
	
	@Test(priority=2,dataProvider="dsSources", dependsOnMethods="createDataSourceTest")
	public void testTenantADssAccessByTenantB(String firewallType, String dataSourceID) throws Exception
	{
		Reporter.log("==========================================================testTenantADssAccessByTenantB start===========",true);
		Reporter.log("firewallType:"+firewallType+" datasourceId:"+dataSourceID,true);
		//access the datasource by another tenant
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatusAclCheck(
				                                                     restClient, 
				                                                     dataSourceID,
				                                                     suiteData.getUser2TenantName(),
				                                                     suiteData.getUser2SessionID(),
				                                                     suiteData.getUser2CsrfToken(),
				                                                     suiteData.getUser2AuthParam()
				                                                     );
		
		//HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, dataSourceID);
		
		Reporter.log("testTenantADssAccessByTenantB response code:::"+pollForStatusResponse.getStatusLine().getStatusCode(),true);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
		Reporter.log("==========================================================testTenantADssAccessByTenantB end===========",true);
		
		
	}
		
	
	@Test(priority=3,dataProvider="dsSources", dependsOnMethods="createDataSourceTest")
	public void testTenantADssSummaryAccessByTenantB(String firewallType, String dataSourceID) throws Exception
	{
		Reporter.log("==========================================================testTenantADssSummaryAccessByTenantB start===========",true);
		
		Reporter.log("firewallType:"+firewallType+" datasourceId:"+dataSourceID);
		HttpResponse summaryResponse =null;
		JSONObject summaryObject=null;
		String range="";
		//access the data source summary by another tenant
		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA: {
			range = "1mo";	
			summaryResponse  = AuditFunctions.getSummaryACLCheck(
					                                          new Client(),
					                                          new StringEntity(AuditTestUtils.getAuditSummaryLatest(dataSourceID,"86400","1394928000","1397519999")),
					                                          suiteData.getUser2TenantName(),
			                                                  suiteData.getUser2SessionID(),
			                                                  suiteData.getUser2CsrfToken(),
			                                                  suiteData.getUser2AuthParam()
					                                          );
			//summaryResponse  = AuditFunctions.getSummary(new Client(), new StringEntity(AuditTestUtils.getAuditSummaryNewPayload(dataSourceID, range)));
				Reporter.log("testTenantADssSummaryAccessByTenantB response code:::"+summaryResponse.getStatusLine().getStatusCode(),true);
				String strResponse=ClientUtil.getResponseBody(summaryResponse);
				JSONArray jsonarray = new JSONArray(strResponse);
				summaryObject = jsonarray.getJSONObject(0);		
				Reporter.log("summaryObject::"+summaryObject,true);
			Assert.assertEquals(summaryResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
		}
		Reporter.log("==========================================================testTenantADssSummaryAccessByTenantB end===========",true);
		
		}
	}
	@Test(priority=4,dataProvider="dsSources", dependsOnMethods="createDataSourceTest")
	public void testTenantADssReportAccessByTenantB(String firewallType, String dataSourceID) throws Exception
	{
		Reporter.log("==========================================================testTenantADssReportAccessByTenantB start===========",true);
		
		Reporter.log("firewallType:"+firewallType+" datasourceId:"+dataSourceID);
		HttpResponse reportResponse =null;
		JSONObject reportObject=null;
		String range="";
		//access the data source report by another tenant
		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA: {
			range = "1mo";	
			reportResponse  = AuditFunctions.getAuditReportACLCheck(
					                                              new Client(),
					                                              new StringEntity(AuditTestUtils.getAuditReportPayloadLatest(dataSourceID,"86400","1394928000","1397519999")),
					                                              suiteData.getUser2TenantName(),
				                                                  suiteData.getUser2SessionID(),
				                                                  suiteData.getUser2CsrfToken(),
				                                                  suiteData.getUser2AuthParam()
				                                                  );	
			Reporter.log("testTenantADssReportAccessByTenantB response code:::"+reportResponse.getStatusLine().getStatusCode(),true);
			String strResponse=ClientUtil.getResponseBody(reportResponse);
			JSONArray jsonarray = new JSONArray(strResponse);
			reportObject = jsonarray.getJSONObject(0);	
			Reporter.log("reportObject::"+reportObject,true);
			Reporter.log("Report call response code:::"+reportResponse.getStatusLine().getStatusCode(),true);
			Assert.assertEquals(reportResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
		}
		Reporter.log("==========================================================testTenantADssReportAccessByTenantB end===========",true);
		
		}
	}
	@Test(priority=5,dataProvider="dsSources", dependsOnMethods="createDataSourceTest")
	public void testVerifyTenantADssCannotbeDeletedByTenantB(String firewallType, String dataSourceID) throws Exception
	{
		Reporter.log("==========================================================testVerifyTenantADssCannotbeDeletedByTenantB start===========",true);
		
		//try to delete the ds created by another tenant
		HttpResponse response = AuditFunctions.deleteDataSourceACLCheck(
				                                                 restClient, 
				                                                 dataSourceID,
				                                                 suiteData.getUser2TenantName(),
				                                                 suiteData.getUser2SessionID(),
				                                                 suiteData.getUser2CsrfToken(),
				                                                 suiteData.getUser2AuthParam()
				                                                 );
		Reporter.log("testVerifyTenantADssCannotbeDeletedByTenantB response code:::"+response.getStatusLine().getStatusCode(),true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
		Reporter.log("==========================================================testVerifyTenantADssCannotbeDeletedByTenantB end===========",true);
		
	}
	
	@Test(priority=6,dataProvider="dsSources", dependsOnMethods="createDataSourceTest")
	public void testAdminAccessTheDatasourcesCreatedByTenASysAdmin(String firewallType, String dataSourceID) throws Exception
	{
		Reporter.log("==========================================================testAdminAccessTheDatasourcesCreatedBySysAdmin start===========",true);
		
		Reporter.log("firewallType:"+firewallType+" datasourceId:"+dataSourceID,true);
		//access the datasource by another tenant
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatusAclCheck(
				                                                     restClient, 
				                                                     dataSourceID,
				                                                     suiteData.getAdminUserTen(),
				                                                     suiteData.getAdminUserSessionID(),
				                                                     suiteData.getAdminUserCsrfToken(),
				                                                     suiteData.getAdminUserAuthParam()
				                                                     );
		
		//HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, dataSourceID);
		
		Reporter.log("testAdminAccessTheDatasourcesCreatedBySysAdmin response code:::"+pollForStatusResponse.getStatusLine().getStatusCode(),true);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
		Reporter.log("==========================================================testAdminAccessTheDatasourcesCreatedBySysAdmin start===========",true);
		
	}
	
	//@Test(priority=7,dataProvider="dsSources", dependsOnMethods="createDataSourceTest")
	public void testAdminAccessTheSummaryofDSSCreatedBySysAdmin(String firewallType, String dataSourceID) throws Exception
	{
		Reporter.log("==========================================================testAdminAccessTheSummaryofDSSCreatedBySysAdmin start===========",true);
		
		Reporter.log("firewallType:"+firewallType+" datasourceId:"+dataSourceID);
		HttpResponse summaryResponse =null;
		//access the data source summary by another tenant
		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA: {
			summaryResponse  = AuditFunctions.getSummaryACLCheck(
					                                          new Client(),
					                                          new StringEntity(AuditTestUtils.getAuditSummaryLatest(dataSourceID,"86400","1394928000","1397519999")),
					                                          suiteData.getAdminUserTen(),
			                                                  suiteData.getAdminUserSessionID(),
			                                                  suiteData.getAdminUserCsrfToken(),
			                                                  suiteData.getAdminUserAuthParam()
					                                          );
				Reporter.log("testAdminAccessTheSummaryofDSSCreatedBySysAdmin response code:::"+summaryResponse.getStatusLine().getStatusCode(),true);
			    Assert.assertEquals(summaryResponse.getStatusLine().getStatusCode(), HttpStatus.SC_UNAUTHORIZED);
		}
		}
		Reporter.log("==========================================================testAdminAccessTheSummaryofDSSCreatedBySysAdmin start===========",true);
		
	}
	//@Test(priority=8,dataProvider="dsSources", dependsOnMethods="createDataSourceTest")
	public void testAdminAccessTheReportOfDSSCreatedBySysAdmin(String firewallType, String dataSourceID) throws Exception
	{
		Reporter.log("==========================================================testAdminAccessTheReportOfDSSCreatedBySysAdmin start===========",true);
		
		Reporter.log("firewallType:"+firewallType+" datasourceId:"+dataSourceID);
		HttpResponse reportResponse =null;
		//access the data source report by another tenant
		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA: {
			reportResponse  = AuditFunctions.getAuditReportACLCheck(
					                                              new Client(),
					                                              new StringEntity(AuditTestUtils.getAuditReportPayloadLatest(dataSourceID,"86400","1394928000","1397519999")),
						                                              suiteData.getAdminUserTen(),
				                                                  suiteData.getAdminUserSessionID(),
				                                                  suiteData.getAdminUserCsrfToken(),
				                                                  suiteData.getAdminUserAuthParam()
				                                                  );
			Reporter.log("testAdminAccessTheReportOfDSSCreatedBySysAdmin response code:::"+reportResponse.getStatusLine().getStatusCode(),true);
			Reporter.log("Report call response code:::"+reportResponse.getStatusLine().getStatusCode(),true);
			Assert.assertEquals(reportResponse.getStatusLine().getStatusCode(), HttpStatus.SC_UNAUTHORIZED);
			
		}
		}
		Reporter.log("==========================================================testAdminAccessTheReportOfDSSCreatedBySysAdmin end===========",true);
		
		
	}
	
	//@Test(priority=9,dataProvider="dsSources", dependsOnMethods="createDataSourceTest")
	public void testAdminDeleteTheDSSCreatedBySysAdmin(String firewallType, String dataSourceID) throws Exception
	{
		Reporter.log("==========================================================testAdminDeleteTheDSSCreatedBySysAdmin start===========",true);
		
		//try to delete the ds created by another tenant
				HttpResponse response = AuditFunctions.deleteDataSourceACLCheck(
						                                                 restClient, 
						                                                 dataSourceID,
						                                                 suiteData.getAdminUserTen(),
						                                                 suiteData.getAdminUserSessionID(),
						                                                 suiteData.getAdminUserCsrfToken(),
						                                                 suiteData.getAdminUserAuthParam());
				Reporter.log("testAdminDeleteTheDSSCreatedBySysAdmin response code:::"+response.getStatusLine().getStatusCode(),true);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_UNAUTHORIZED);
				Reporter.log("==========================================================testAdminDeleteTheDSSCreatedBySysAdmin end===========",true);
				
	}

	@Test(priority=10,dataProvider="dsSources", dependsOnMethods="createDataSourceTest")
	public void testDPOUserAccessTheDSSCreatedBySysAdmin(String firewallType, String dataSourceID) throws Exception
	{
		Reporter.log("==========================================================testDPOUserAccessTheDSSCreatedBySysAdmin start===========",true);
		
		Reporter.log("firewallType:"+firewallType+" datasourceId:"+dataSourceID,true);
		//access the datasource by another tenant
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatusAclCheck(
				                                                     restClient, 
				                                                     dataSourceID,
				                                                     suiteData.getDpoUserTen(),
				                                                     suiteData.getDpoSessionID(),
				                                                     suiteData.getDpoCsrfToken(),
				                                                     suiteData.getDpoAuthParam()
				                                                     );
		
		//HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, dataSourceID);
		
		Reporter.log("testDPOUserAccessTheDSSCreatedBySysAdmin response code:::"+pollForStatusResponse.getStatusLine().getStatusCode(),true);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_UNAUTHORIZED);
		Reporter.log("==========================================================testDPOUserAccessTheDSSCreatedBySysAdmin start===========",true);
		
	}
	
	@Test
	public void testTenantBShouldNotProvisionSpanvaToTenantA() throws Exception
	{
		
	}

	
	
}
