/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
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
public class AuditMiscellaneousTests extends AuditInitializeTests{
	protected Client restClient;	
	protected String fireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected String scpPayload;
	protected String firewallLogFilePath;
	protected String sftpTenantUsername;
	protected String sftpServerHost;
	protected String sftpServerDestinationDir;
	Properties dataSourceProps=new Properties();
	Map<String, JSONObject> webUploadDSAuditResults=new HashMap<String, JSONObject>();
	Map<String, JSONObject> scpUploadDSAuditResults=new HashMap<String, JSONObject>();
	
	
	/**
	 * @param FireWallName
	 */
	public AuditMiscellaneousTests(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
		
	}	
	
	
	/**
	 * @throws IOException
	 * @throws Exception
	 */
	@Test(priority=1)
	public void testVerifyDataSourcesForInvalidLogFiles() throws IOException, Exception{						
				
		Logger.info("Creating Data Source for: "+fireWallType);
		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(AuditTestUtils.createWebUploadPostBodyForInvalidLogs(fireWallType,suiteData.getEnvironmentName())));		
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
		Reporter.log("signedURL:"+signedURL);
						
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
		while(("Pending Data".equals(last_Status) ||  "Queued".equals(last_Status) || "Processing".equals(last_Status))&& currentWaitTime <= 9000000){
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Logger.info("Last Status of "+ sourceID +" is "+ last_Status);
			if("Pending Validation".equals(last_Status) )	
			{
				Assert.assertNotEquals(AuditTestConstants.AUDIT_LOG_PROCESS_SUCESS_MSG, pollRespObject.get("last_status_message"));
				break;
			}
			if("Completed".equals(last_Status)  && (fireWallType.equals(AuditTestConstants.FIREWALL_PAN_TRAFFIC_FILE) ||
					fireWallType.equals(AuditTestConstants.FIREWALL_PAN_URL_FILE) )	)
			{
				Assert.assertEquals(AuditTestConstants.AUDIT_LOG_PROCESS_SUCESS_MSG, pollRespObject.get("last_status_message"));
				break;
			}
				
			
			
		}
	
		if(!"Pending Validation".equals(last_Status) || !"Failed".equals(last_Status))
			Assert.assertTrue(currentWaitTime <= 9000000," File processing took more than SLA. Last status of this source file was "+last_Status);
		
	}
	
	
	

	 //* This test case deletes the data source
	 
	@Test(dependsOnMethods={"testVerifyDataSourcesForInvalidLogFiles"})
	public void deleteDataSourceTest() throws Exception {
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
	}
	
	
	
}
