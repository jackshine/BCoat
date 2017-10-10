/**
 * 
 */
package com.elastica.beatle.tests.audit.spanvatests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.ProcessHandler;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.SpanVATestsUtils;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AuditSpanVASyslogLatestImageTests  extends AuditInitializeTests{

	private String fireWallType;
	private Client restClient;
	private String spanVASyslogCreateRequestPayload;
	private Map<String,String> spanVAMachineInfo;
	private SpanVATestsUtils spavaUtil;
	protected String sourceID;
	
	
	public AuditSpanVASyslogLatestImageTests(String firwallType){
		restClient = new Client();
		this.fireWallType=firwallType;
	}
	
	@BeforeClass(alwaysRun = true)
	public void initSpanvaHttpsData() throws Exception{
		spavaUtil = new SpanVATestsUtils();
		spanVAMachineInfo = spavaUtil.getSpanvaAliveAgentOnThisInstance(suiteData, restClient);
		spanVASyslogCreateRequestPayload = AuditTestUtils.createSpanVAUploadDSPayloadForSyslog(fireWallType,spanVAMachineInfo.get("agent_id"),suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SPANVA_DS_NAME+"SysLog_Server");
		
	}
	
	@Test
	public void createDataSourceTest() throws Exception {
		Logger.info("********************************* Test Description ****************************************************** ");
		Logger.info("1. Create Datasource through Syslog Server Transportation for SpanVa lastest Versions "+spanVAMachineInfo.get("current_version"));
		Logger.info("2. Process the Datasource ");
		Logger.info("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ");
		Logger.info("********************************************************************************************************* ");
		Logger.info("*************Datasource Creation started for:"+fireWallType+"****************");
		Logger.info("DS Creationg request payload:"+spanVASyslogCreateRequestPayload);
		HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,new StringEntity(spanVASyslogCreateRequestPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,"Failed to create the data source");
		
		JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		Logger.info("Datasource create Response:"+spanVAConnectionObject);
		Logger.info("dsName:: "+spanVAConnectionObject.getString("name"));
	
		Logger.info("Validating all fields from the data source creation request response");
		spavaUtil.validateSpanVADataSourceCommonFields(spanVAConnectionObject);
		spavaUtil.validateSpanVASysLogDSCreateResponse(spanVAConnectionObject);
		sourceID = (String) spanVAConnectionObject.get("id");
		
		//getting data source from getDatasources list
		Logger.info("getting the datasource from the datasources list:");
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();		
		queryParam.add(new BasicNameValuePair("fields", "datasources"));
		HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient,queryParam);
		Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strDatataSourceListResp=ClientUtil.getResponseBody(datataSourceListResp);
		Logger.info("List of Datasources:"+strDatataSourceListResp);
		
		boolean isDSPresentFlag = false;
		JSONArray datasourcesList = new JSONObject(strDatataSourceListResp).getJSONObject("objects").getJSONArray("datasources");
		for(int i=0; i<datasourcesList.length(); i++)
		{
			if(spanVAConnectionObject.getString("name").equals(((JSONObject)datasourcesList.get(i)).getString("name")))
			{
				isDSPresentFlag = true;
				sourceID=((JSONObject)datasourcesList.get(i)).getString("id");
				Logger.info("Newly created DS found in the DS list API");
				break;
			}
		}
		Assert.assertTrue(isDSPresentFlag,"Newly created DS not found in DS list api response");
		
		String ip = spanVAConnectionObject.getJSONObject("agent_info").getString("host");
		Logger.info("SpanVA IP from Datasource response: "+ip);
		
		int port = spanVAConnectionObject.getJSONObject("agent_info").getInt("port");
		Logger.info("SpanVA Port from Datasource response: "+port);
		
		String logFilePath = FileHandlingUtils.getFileAbsolutePath(AuditTestUtils.getFirewallLogFilePath(fireWallType));
		Logger.info("Log file path: "+logFilePath);
		
		int processStatus = new ProcessHandler().executeCommand(ip, port, logFilePath, 180000, false);						
		Assert.assertNotEquals(processStatus, -1,"Data pushing process failed. ");
		Assert.assertEquals(processStatus, 0,"Data pushing process status not successful. ");
		
		// Poll for data source upload status
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Logger.info("Actual Datasource Response:"+pollRespObject);
		String last_Status = pollRespObject.getString("last_status");
		Logger.info("Actual Datasource Status:"+last_Status);
		String expectedCompletedStatus="Completed";
		Logger.info("Expected Datasource Status:"+expectedCompletedStatus);
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Logger.info("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME);
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Logger.info("Actual Last Status of Datasource: "+ sourceID +" is "+ last_Status);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))			
				break;
		}
		if(!"Completed".equals(last_Status) || !"Failed".equals(last_Status))
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

		Logger.info("************************** Datasource process Completed sucessfully ******************************");
		Logger.info("**************************Datasource creation Test Completed**************************");
	}
	
	/*
	 * This test case deletes the data source
	 */
	@Test(dependsOnMethods={"createDataSourceTest"})
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
	
}