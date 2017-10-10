package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
//import com.elastica.beatle.audit.auditDTO.CreateDataSourceDTO;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class DummyAuditSpanVAUploadTests extends AuditInitializeTests{
	
	protected Client restClient;
	//protected CreateDataSourceDTO dataSourceDTO;
	protected String sourceID = null;
	// scp/sftp upload params
	protected String rsa_key_file_path;
	protected ArrayList<String> datasourceIdsList=new ArrayList<String>();
	
	protected String fileToBeUploaded;
	protected String sftpTenantUsername;
	protected String sftpServerHost;
	protected String sftpServerDestinationDir;
	
	private String FireWallType;
	String spanVAPayload;
	String firewallLogFilePath;
	String agentId;
	Properties firewallLogDataProps;
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	protected Properties spanVADSProps=new Properties();

  public DummyAuditSpanVAUploadTests(String FireWallName) {
		restClient = new Client();
		this.FireWallType = FireWallName;
	}
  
  //@BeforeClass(alwaysRun = true)
	public void intScpSftpData() throws Exception{
	  
	  Properties p=new Properties();
		
		InputStream inputStream = new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.AUDIT_CONFIGURED_AGENTS_PATH));
		p.load(inputStream);
		//EOE_auditbecom_SpanVA
		 agentId=p.getProperty("EOE_SpanVaAgent_248_1");
		
	  spanVAPayload = AuditTestUtils.createSpanVAUploadBody(FireWallType,agentId,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SPANVA_DS_NAME);
	  firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(FireWallType);
	  }
  
	@DataProvider(name="AuditSpanVADatasourcesList")
	public Object[][] getWUDSList() throws Exception
	{
		spanVADSProps.put(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES, "560b7ba09dfa511e1b848138");
		spanVADSProps.put(AuditTestConstants.FIREWALL_ZSCALAR, "560b7b9fbf831244c87c9e4f");
		spanVADSProps.put(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG, "560b7b9f21f13f023016fb0a");
		
		
		Object[][] inputData = new Object[spanVADSProps.size()][2];
		int j=0;

		for(String key:spanVADSProps.stringPropertyNames() )
		{
			inputData[j][0] = key;
			inputData[j][1] = spanVADSProps.get(key);
			j++;
		}
		Reporter.log("webupload dssources legth::"+inputData.length,true);
		return inputData;
	}
  
  @Test(priority=1, dataProvider="AuditSpanVADatasourcesList")
  public void testSpanVADatasourceCreation(String firewallType, String sourceID) throws Exception
  {
	  this.FireWallType=firewallType;
	  this.sourceID=sourceID;
	  /*
	  
	  //create spanva datasource
		HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,new StringEntity(spanVAPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		
		Reporter.log("spanVAConnectionObject:"+spanVAConnectionObject,true);
		
		String dsName=(String)spanVAConnectionObject.get("name");
		Reporter.log("dsName::"+dsName,true);
		
		validateSpanVADataSource(spanVAConnectionObject);
		sourceID = (String) spanVAConnectionObject.get("id");
		JSONObject agentInfoObject=(JSONObject) spanVAConnectionObject.get("agent_info");
		
		Reporter.log("agentInfoObject:"+agentInfoObject,true);
		String username=(String)agentInfoObject.get("user");
		String host=(String)agentInfoObject.get("host");
		String dest_dir=(String)agentInfoObject.get("dst_dir");
		
		
     //getting data source from getDatasources list
        
        List<NameValuePair> queryParam = new ArrayList<NameValuePair>();		
		queryParam.add(new BasicNameValuePair("fields", "datasources"));
        HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient,queryParam);
		Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		
		String strDatataSourceListResp=ClientUtil.getResponseBody(datataSourceListResp);
		Reporter.log("strDatataSourceListResp:"+strDatataSourceListResp,true);
		
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
		
		
		//upload file to the span VA datasource
	    SFTPUtils sftpUtils=getSftpUtilsConfiguration(username, host,dest_dir);
		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
        result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
        System.out.println("sftp file upload status:  " + result);
        
      	 */
        
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
       

     
  }
  

	@Test(dependsOnMethods={"testSpanVADatasourceCreation"}, priority=2,dataProvider="AuditSpanVADatasourcesList")	
	public void TestAuditSummary(String firewallType, String sourceID) throws Exception
	  {
		  this.FireWallType=firewallType;
		  this.sourceID=sourceID;
		//Reporter.log("Getting summary for "+ FireWallType +" its ID is: "+sourceID, true);
		AuditGoldenSetDataController controller=null;
		String range = "1mo";			
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		
		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);				
		Reporter.log("summaryObject>>"+summaryObject,true);
		
		 // Commenting these assertions for now because of this bug
		  Assert.assertEquals(summaryObject.get("datasource_id"), sourceID);										   
		  Assert.assertEquals(summaryObject.get("date_range"), range);
		  Assert.assertNotNull(summaryObject.get("id"), "Id is null");
		  Assert.assertFalse(((String) summaryObject.get("id")).isEmpty(),"ID is empty");
		  Assert.assertNotNull(summaryObject.get("resource_uri"),"resource_uri is null");
		  Assert.assertFalse(((String)summaryObject.get("resource_uri")).isEmpty(),"resource_uri is empty");
		  Assert.assertTrue(summaryObject.getBoolean("is_valid"));		
		  Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
		  Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");	
		
		
		  switch(FireWallType)
			{
			
			case AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG: {
				controller=new AuditGoldenSetDataController(AuditTestConstants.BLUECOATPROXY_DATA_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,FireWallType,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG+" Summary Results are wrong");
				break;
			}
			case AuditTestConstants.FIREWALL_ZSCALAR: {
				controller=new AuditGoldenSetDataController(AuditTestConstants.ZSCALAR_DATA_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,FireWallType,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_ZSCALAR+" Summary Results are wrong");
				break;
			}
			
			default: {

				break;
			}
			}
	}
	
	@Test(dependsOnMethods={"TestAuditSummary"}, priority=3,dataProvider="AuditSpanVADatasourcesList")
	public void testAuditReport(String firewallType, String sourceID) throws Exception
	  {
		  this.FireWallType=firewallType;
		  this.sourceID=sourceID;
		Reporter.log("Getting Report for "+ FireWallType +" its ID is: "+sourceID, true);
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		String range = "1mo";
		
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response  = AuditFunctions.getAuditReport(restClient, queryParam);		
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
		Assert.assertEquals(reportObject.get("date_range"), range);
		Assert.assertNotNull(reportObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("generated_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("latest_date"),"Latest date is null");		
		
	}
	@Test(dependsOnMethods={"testAuditReport"}, priority=4,dataProvider="AuditSpanVADatasourcesList")
	public void deleteDataSourceTests(String firewallType, String sourceID) throws Exception {}
	
	/*
	 * This test case deletes the data source
	 */
	//@Test(dependsOnMethods={"testAuditReport"}, priority=4)
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("Deleting Data Source "+ FireWallType +" its ID is: "+sourceID, true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
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
  private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String sftpServerHost, String sftpServerDestinationDir ) {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
        sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
        sftpUtils.setUserName(sftpTenantUsername);
        sftpUtils.setPassWord(AuditTestConstants.AUDIT_SPANVA_TENANT_PWD);
        sftpUtils.setDestinationDir(sftpServerDestinationDir);
        return sftpUtils;
	}
  
  @AfterClass
	public void testPopulateAuditSummaryFailures() throws Exception
	{
		Reporter.log("*****************SpanVA Regression: Audit Summary Validation Errors***********************",true);
		
		 for(String str: goldenSetErrorList)
		  {
			  Reporter.log(str,true);
		  }
	}
}
