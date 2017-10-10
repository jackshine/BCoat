/**
 * 
 */
package com.elastica.beatle.tests.audit.dummy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AuditDSUploadWithS3TestsNew extends AuditInitializeTests{

	private Client restClient;
	//private String FireWallType;
	private String sourceID;
	private S3ActionHandler s3Handler;
	private JSONObject S3PropertiesObj;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	List<String>firwallsList;
	Properties s3DatasourcesProcessedAndUnProcessed=new Properties();
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	
	
	
	/**
	 * @param FireWallName
	 */
	public AuditDSUploadWithS3TestsNew() {
		restClient = new Client();
		
	}	
	
	@BeforeClass(alwaysRun=true)
	public void initializeTests() throws Exception {
		Reporter.log("AuditWebuploadTestsNew3 before class is calling..",true);
		 goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup();
	}
	
		
	//@BeforeClass(alwaysRun = true)
	public void initS3Data(String fireWallType) throws Exception{
		S3PropertiesObj = AuditTestUtils.buildS3CredentialCheckPostBody(fireWallType);
		s3Handler = new S3ActionHandler();
		s3Handler.uploadLogFileToS3(S3PropertiesObj.getString("bucket"),
				S3PropertiesObj.getString("input_folder")+"/", 
				AuditTestUtils.getFireWallLogFileName(fireWallType), 
				new File(FileHandlingUtils.getFileAbsolutePath(AuditTestUtils.getFirewallLogFilePath(fireWallType))));
	}
	
	
	
	public Map<String,String> testFirewallDataSourceCreation(String fireWallType) throws Exception
	{
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through S3 Transportation", true);
		Reporter.log("********************************************************************************************************* ", true);
		Map<String,String> dataSourcesMap=new HashMap<String,String>();
		
		initS3Data(fireWallType);
		
		HttpEntity entity = new StringEntity(S3PropertiesObj.toString());		
		HttpResponse response =  AuditFunctions.checkS3Credentials(restClient,entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject responseObject = new JSONObject(ClientUtil.getResponseBody(response));
		Assert.assertEquals(responseObject.getInt("reason"), 0, "Reason for failure is");
		
		// Create datasource
		JSONObject createS3DSBody = new JSONObject(AuditTestUtils.createS3UploadPostBody(fireWallType, S3PropertiesObj,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_S3_DS_NAME));						
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
		
		dataSourcesMap.put(fireWallType, sourceID);
		return dataSourcesMap;	
	}
	
	public Properties  testDatasourceProcessVerification(Map<String,String> dataSourcesMap) throws Exception
	{
		Properties processedDataSources=new Properties();
		List<String> dataSourceIDsList=new ArrayList<String>(dataSourcesMap.values());
		String strFirewallType=null;
		long currentWaitTime=0;
		process(dataSourceIDsList,currentWaitTime);
			
			List<String> dataSourceIDsListFinal=new ArrayList<String>(dataSourcesMap.values());
			for(String strID: dataSourceIDsListFinal)
			{
				strFirewallType=getKeysFromValue(dataSourcesMap,strID);
				processedDataSources.put(strFirewallType, strID);
			}
			
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
      while (("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status)
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
	
	@DataProvider(name="s3DataSourcesDP")//processedDataSourcesList getCompletedDSs
	
	public Object[][] s3DataSourcesProcessDP() throws Exception
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
		Reporter.log("S3 Datasources"+inputData,true);
		Reporter.log("S3 completed datasources length::"+inputData.length,true);
		return inputData;
	}
	
	@Test(priority=1,dataProvider="s3DataSourcesDP")
	public void testDataSourceCreationAndProcessForDifferentFirewalls(String firewallType, String dsID,String dsStatus) throws Exception
	{
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through S3 Transportation", true);
		Reporter.log("********************************************************************************************************* ", true);
		Reporter.log(". Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		
		s3DatasourcesProcessedAndUnProcessed.put(firewallType+"~"+dsStatus, dsID);
		
		Reporter.log("firewallType:"+firewallType+ " dsID:"+dsID+" dsStatus:"+dsStatus);;
		if("Completed".equals(dsStatus) ){
			 Assert.assertTrue(true);
		}
		else{
				Assert.assertFalse(!"Completed".equals(dsStatus),
					firewallType+" DataSource "+dsID+" not processed in "+( (28800000 / 1000)  / 60)+" minutes and current status is "+dsStatus);
		}
		
	}
	
	@DataProvider(name="dataSourcesTestData")//processedDataSourcesList getCompletedDSs
		public Object[][] processedDataSourcesListForSummary() throws Exception
		{
			
			Reporter.log("dataSourceDPForSummay:::"+s3DatasourcesProcessedAndUnProcessed+"  size::"+s3DatasourcesProcessedAndUnProcessed.size(),true);
			Object[][] inputData = new Object[s3DatasourcesProcessedAndUnProcessed.size()][3];
			int j=0;
			String last_Status=null;
			

			for(String firewallType_status:s3DatasourcesProcessedAndUnProcessed.stringPropertyNames())
			{
				inputData[j][0] = firewallType_status.split("~")[0];
				inputData[j][1] = s3DatasourcesProcessedAndUnProcessed.getProperty(firewallType_status);
				last_Status=pollForDataSourceStatus(restClient,s3DatasourcesProcessedAndUnProcessed.getProperty(firewallType_status));
				inputData[j][2] = last_Status;
				j++;
			}
			Reporter.log("Webupload Datasources for summary"+inputData,true);
			Reporter.log("webupload completed datasources for summary length::"+inputData.length,true);
			return inputData;
		}
	
	@Test(priority=2,dataProvider="dataSourcesTestData",dependsOnMethods={"testDataSourceCreationAndProcessForDifferentFirewalls"})
			public void deleteS3DataSourceTest(String firewallType, String datasourceId,String ds_status) throws Exception {
				Logger.info("Deleting Data Source "+ firewallType +" its ID is: "+datasourceId);
				HttpResponse response = AuditFunctions.deleteDataSource(restClient, datasourceId);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
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
	
	
	
}
