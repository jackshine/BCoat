package com.elastica.beatle.tests.audit.dummy;

import java.io.File;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.SFTPUtils;

public class AuditScpTestsNew extends AuditInitializeTests {
	
	protected Client restClient;
	protected String sourceID = null;
	protected String fileToBeUploaded;
	protected String sftpTenantUsername;
	protected String sftpServerHost;
	protected String sftpServerDestinationDir;
	protected String fireWallType;
	
	Properties firewallLogDataProps;
	protected ArrayList<String> datasourceIdsList=new ArrayList<String>();
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	List<String>firwallsList;
	Properties summaryProps=new Properties();
	
	


	public AuditScpTestsNew(String fireWallType) {
		restClient = new Client();
		
	}

	/**
	 * Prepares the payload for datasource creation
	 * set the firewall log path. 
	 * @throws Exception
	 */
	
	
	public Map<String,String> testFirewallDataSourceCreation(String fireWallType) throws Exception
	{
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through SCP Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);
		String scpPayload;
		String firewallLogFilePath;
		Map<String,String> dataSourcesMap=new HashMap<String,String>();
		scpPayload = AuditTestUtils.createSCPUploadBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SCP_DS_NAME);
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
		
		
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
		dataSourcesMap.put(fireWallType, sourceID);
		return dataSourcesMap;
		
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

	/*public Properties  testDatasourceProcessVerification(Map<String,String> dataSourcesMap) throws Exception
	{
		Properties processedDataSources=new Properties();
		List<String> dataSourceIDsList=new ArrayList<String>(dataSourcesMap.values());
		List<String> completedDSsList=new ArrayList<String>();
		String last_Status;
		String firstDataSourceID=dataSourceIDsList.get(0);
		String strFirewallType=null;
		
			last_Status=pollForDataSourceStatus(restClient,firstDataSourceID);
			Logger.info("Last Status of Datasource: "+ firstDataSourceID +" is "+ last_Status);
			
			long currentWaitTime = 0;
			while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || 
					"Processing".equals(last_Status))&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
				Reporter.log("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
				Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
				currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
				Reporter.log("dataSourceIDsList. outside"+dataSourceIDsList,true);
				if(!dataSourceIDsList.isEmpty()){
					Collections.shuffle(dataSourceIDsList);
					 firstDataSourceID=dataSourceIDsList.get(0);
					 last_Status=pollForDataSourceStatus(restClient,firstDataSourceID);
					Logger.info("shuffling and Last Status of Datasource: "+ firstDataSourceID +" is "+ last_Status);
					}
				if("Completed".equals(last_Status) )
				{
					strFirewallType=getKeysFromValue(dataSourcesMap,firstDataSourceID);
					Reporter.log(strFirewallType+"_"+firstDataSourceID+" processed in "+currentWaitTime,true);
					completedDSsList.add(firstDataSourceID);
					dataSourceIDsList.remove(firstDataSourceID);
					Reporter.log("completedDSsList. "+completedDSsList,true);
					Reporter.log("dataSourceIDsList. remains"+dataSourceIDsList,true);
					if(!dataSourceIDsList.isEmpty()){
						Collections.shuffle(dataSourceIDsList);
						 firstDataSourceID=dataSourceIDsList.get(0);
						 last_Status=pollForDataSourceStatus(restClient,firstDataSourceID);
						 Logger.info("shuffling and Last Status of Datasource: after atleast 1 ds completed "+ firstDataSourceID +" is "+ last_Status);
					
				}
		}}
			
			List<String> dataSourceIDsListFinal=new ArrayList<String>(dataSourcesMap.values());
			for(String strID: dataSourceIDsListFinal)
			{
				strFirewallType=getKeysFromValue(dataSourcesMap,strID);
				last_Status=pollForDataSourceStatus(restClient,strID);
				processedDataSources.put(strFirewallType, strID);
			}
			
			return processedDataSources;
	
	}*/
	
	public Properties  testDatasourceProcessVerification(Map<String,String> dataSourcesMap) throws Exception
	{
		long startTime = System.currentTimeMillis();
		Reporter.log("Datasources Process start time..." + startTime, true);

		Properties processedDataSources = new Properties();
		List<String> dataSourceIDsList = new ArrayList<String>(dataSourcesMap.values());
		String strFirewallType = null;
		long currentWaitTime = 0;
		process(dataSourceIDsList, currentWaitTime);

		List<String> dataSourceIDsListFinal = new ArrayList<String>(dataSourcesMap.values());
		for (String strID : dataSourceIDsListFinal) {
			strFirewallType = getKeysFromValue(dataSourcesMap, strID);
			processedDataSources.put(strFirewallType, strID);
		}
		long endTime = System.currentTimeMillis();
		Reporter.log("Datasources Process end time..." + endTime, true);

		Reporter.log("******************Datasources Process end time in Milliseconds::**************" + ((endTime - startTime)), true);
			
			return processedDataSources;
	
	}
	
	
public void process( List<String> dataSourceIDsList,long currentWaitTime ) throws Exception
 {
		String last_Status = "";
		Reporter.log("dataSourceIDsList. outside while" + dataSourceIDsList, true);
		String firstDataSourceID = "";

		if (!dataSourceIDsList.isEmpty() && dataSourceIDsList.size() > 0) {
			firstDataSourceID = ((!dataSourceIDsList.isEmpty() && dataSourceIDsList.size() > 0)
					? dataSourceIDsList.get(0) : "");
			last_Status = pollForDataSourceStatus(restClient, firstDataSourceID);

		}

		if ("Completed".equals(last_Status)) {
			Reporter.log("if block Datasource Process total Wait Time outside*************** :" + currentWaitTime,
					true);
			dataSourceIDsList.remove(firstDataSourceID);
			if (!dataSourceIDsList.isEmpty() && dataSourceIDsList.size() > 0) {
				Collections.shuffle(dataSourceIDsList);
				process(dataSourceIDsList, currentWaitTime);
			}
		} else {
      while (("Pending Data".equals(last_Status) 
					|| "Queued".equals(last_Status) || "Processing".equals(last_Status))
					&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME) {
				Reporter.log("Wait Time*************** :" + AuditTestConstants.AUDIT_THREAD_WAITTIME, true);
				Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
				currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
				Reporter.log("dataSourceIDsList. inside while" + dataSourceIDsList, true);

				if (dataSourceIDsList.size() == 0)
					break;
				else {
					if (!dataSourceIDsList.isEmpty()) {
						Collections.shuffle(dataSourceIDsList);
						firstDataSourceID = dataSourceIDsList.get(0);
						last_Status = pollForDataSourceStatus(restClient, firstDataSourceID);
					}

					Logger.info("shuffling and Last Status of Datasource: " + firstDataSourceID + " is " + last_Status);
					Reporter.log("Datasource list size *************** :" + dataSourceIDsList.size(), true);

					if ("Completed".equals(last_Status)) {
						Reporter.log("else block Datasource Process total Wait Time outside*************** :"
								+ currentWaitTime, true);
						dataSourceIDsList.remove(firstDataSourceID);
					}
					Reporter.log("Datasource atleast list size after *************** :" + dataSourceIDsList.size(),
							true);

					if (!dataSourceIDsList.isEmpty()) {
						Collections.shuffle(dataSourceIDsList);
						process(dataSourceIDsList, currentWaitTime);
					}
				}

			}
		}
	}
	
	@DataProvider(name="dataSourceDP")//processedDataSourcesList getCompletedDSs
	public Object[][] processedDataSourcesList() throws Exception
	{
		
		firwallsList=AuditTestConstants.getFirewallsList();
		Properties dsProps=new Properties();
	       
	       Map<String,String> dataSourcesMap1=new HashMap<String,String>();
		   //Create Datasources
	       for(String key:firwallsList )
			{
	    	   dataSourcesMap1.putAll(this.testFirewallDataSourceCreation(key));
			 
			}
	       
	       Reporter.log("dataSourcesMap1::"+dataSourcesMap1,true);
	       
	       //Verify Datasource process check and get the all the datasources
	       dsProps=testDatasourceProcessVerification(dataSourcesMap1);
	
		Reporter.log("processedDataSources:::"+dsProps+"  size::"+dsProps.size(),true);
		Object[][] inputData = new Object[dsProps.size()][3];
		int j=0;
		String last_Status=null;
		

		for(String firewallType:dsProps.stringPropertyNames())
		{
			inputData[j][0] = firewallType;
			inputData[j][1] = dsProps.getProperty(firewallType);
			last_Status=pollForDataSourceStatus(restClient,dsProps.getProperty(firewallType));
			inputData[j][2] = last_Status;
			j++;
		}
		Reporter.log("scp Datasources"+inputData,true);
		Reporter.log("scp completed datasources length::"+inputData.length,true);
		return inputData;
	}
	
	@Test(priority=1,dataProvider="dataSourceDP")
	public void testDataSourceCreationAndProcessForDifferentFirewalls(String firewallType, String dsID,String dsStatus) throws Exception
	{
		Reporter.log("started testDataSourceCreationAndProcessForDifferentFirewalls****************");
		
		summaryProps.put(firewallType+"~"+dsStatus, dsID);
		
		Reporter.log("firewallType:"+firewallType+ " dsID:"+dsID+" dsStatus:"+dsStatus);;
		if("Completed".equals(dsStatus) ){
			 Assert.assertTrue(true);
		}
		else{
				Assert.assertFalse(!"Completed".equals(dsStatus),
					firewallType+" DataSource "+dsID+" not processed in "+( (AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME / 1000)  / 60)+" minutes and current status is "+dsStatus);
		}
		
	}
	
	//@DataProvider(name="dataSourcesTestData")//processedDataSourcesList getCompletedDSs
	public Object[][] processedDataSourcesListForSummary() throws Exception
	{
		
		
		Reporter.log("dataSourceDPForSummay:::"+summaryProps+"  size::"+summaryProps.size(),true);
		Object[][] inputData = new Object[summaryProps.size()][3];
		int j=0;
		String last_Status=null;
		

		for(String firewallType_status:summaryProps.stringPropertyNames())
		{
			inputData[j][0] = firewallType_status.split("~")[0];
			inputData[j][1] = summaryProps.getProperty(firewallType_status);
			last_Status=pollForDataSourceStatus(restClient,summaryProps.getProperty(firewallType_status));
			inputData[j][2] = last_Status;
			j++;
		}
		Reporter.log("Webupload Datasources for summary"+inputData,true);
		Reporter.log("webupload completed datasources for summary length::"+inputData.length,true);
		return inputData;
	}
	
	//@Test(priority=2,dataProvider="dataSourcesTestData")	
	public void testAuditSummary(String fireWallType, String sourceID,String dsStatus) throws Exception{
		Reporter.log("**********Audit Summary Verification Test for "+ fireWallType +" its ID is: "+sourceID+" started********* ",true);
		
		if(!"Completed".equals(dsStatus) ){
			 throw new SkipException("Skipping the test due to "+fireWallType+" DataSource "+sourceID+" not processed"+ "and the datasource status is:"+dsStatus );
		}
		
	}
	
	//@Test(priority=3,dataProvider="dataSourcesTestData")	
	public void testAuditReport(String fireWallType, String sourceID,String dsStatus) throws Exception{
		Reporter.log("**********Audit Summary Verification Test for "+ fireWallType +" its ID is: "+sourceID+" started********* ",true);
		
		if(!"Completed".equals(dsStatus) ){
			 throw new SkipException("Skipping the test due to "+fireWallType+" DataSource "+sourceID+" not processed"+ "and the datasource status is:"+dsStatus );
		}
		
	}
	
	//@Test(priority=4,dataProvider="dataSourcesTestData")
		public void deleteS3DataSourceTest(String transportionType, String datasourceId,String ds_status) throws Exception {
			Logger.info("Deleting Data Source "+ transportionType +" its ID is: "+datasourceId);
			HttpResponse response = AuditFunctions.deleteDataSource(restClient, datasourceId);
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
		}
	   
	
	 public static String getKeysFromValue(Map<String, String> hm, Object value){
		    List <String>list = new ArrayList<String>();
		    String key=null;
		    for(String o:hm.keySet()){
		        if(hm.get(o).equals(value)) {
		        	key=o;
		        }
		    }
		    return key;
		  }
	
	
	public String shuffleAndVerifyDataSourceStatus(List<String> dataSourceIDsList) throws Exception
	{
		Collections.shuffle(dataSourceIDsList);
		String firstDataSourceID=dataSourceIDsList.get(0);
		String last_Status=pollForDataSourceStatus(restClient,firstDataSourceID);
		Logger.info("shuffling and Last Status of Datasource: "+ firstDataSourceID +" is "+ last_Status);
		return last_Status;
	}
	
	
	public String pollForDataSourceStatus(Client restClient,String dataSourceID) throws Exception
	{
		
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, dataSourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);
		String last_Status = pollRespObject.getString("last_status");
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);
		return last_Status;
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
        	sftpUtils.setPassWord("PXF4msab");
        }
        sftpUtils.setDestinationDir(sftpServerDestinationDir);
        return sftpUtils;
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
	
}
