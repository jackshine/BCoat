/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditDashBoardReportResourceDataSetup;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

/**
 * @author Mallesh
 *
 */
public class AuditDashBoardReportTests extends AuditInitializeTests {
	protected Client restClient;	
	protected String FireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	static final String AUDIT_SOURCE_OBJECT="audit_details";
	protected String fireWallType;
	
	Properties reportSourceDataConfigProps=null;
	AuditDashBoardReportResourceDataSetup auditDashBoardReportResourceDataSetup=null;
	
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	
	/**
	 * @param FireWallName
	 */
	public AuditDashBoardReportTests(String fireWallType) {
		restClient = new Client();
		this.fireWallType = fireWallType;
		
		
	}		
	
	@BeforeClass
	public void init() throws Exception
	{
		auditDashBoardReportResourceDataSetup=new AuditDashBoardReportResourceDataSetup(fireWallType);
	}
	
	/**
	 * @throws IOException
	 * @throws Exception
	 */
	
	@BeforeClass
	public void initReportSourceConfig() throws Exception
	{
		reportSourceDataConfigProps=new Properties();
		reportSourceDataConfigProps.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.REPORT_SOURCE_DATA_PAYLOAD_REQUEST_FILE_NAMES)));
	
	}
	
	
	@Test(priority=1)
	public void testAuditReportSources() throws Exception
	{
		Reporter.log("Test Audit ReportSource: ",true);
		JSONObject jsonReportSourceSubObject =auditReportSourceObj();
		
		String auditReportSourceID = (String)jsonReportSourceSubObject.getString("id");
		String auditReportSourceDisplayTxt = (String)jsonReportSourceSubObject.getString("display_text");
		
		Reporter.log("auditReportSourceID.."+auditReportSourceID,true);
		Assert.assertNotNull(auditReportSourceID,"Audit Report Source null");
		Assert.assertNotNull(auditReportSourceDisplayTxt,"Audit Report Source Display text null");
		Assert.assertEquals(auditReportSourceID, AUDIT_SOURCE_OBJECT,"Audit Report Source should be:");
		
	}
	
	//@Test(priority=2)
	public void testAuditReportSourceConfigObject() throws Exception
	{
		Reporter.log("Test Audit ReportSource config object: ",true);
		JSONObject jsonReportSourceCfgObject=getAuditReportSourceConfig();
		
		JSONArray jsonArrSupportedAggregations=(JSONArray)jsonReportSourceCfgObject.getJSONArray("supported_aggregations");
		Assert.assertNotNull(jsonArrSupportedAggregations,"Audit Report Source Object null");
		
		try {
	       
	        if (jsonArrSupportedAggregations != null) {

	            for (int i = 0; i < jsonArrSupportedAggregations.length(); i++) {
	            	Reporter.log("supported aggregations:"+(String)jsonArrSupportedAggregations.get(i),true);
	            }
	        }
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
		
	  //Assert the Audit config object supported fields
		Properties expectedAuditReportSourceCfgObjFields=new Properties();
		expectedAuditReportSourceCfgObjFields.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.AUDIT_DASHBOARD_REPORT_SOURCES_CONFIG_FIELDS_PATH)));

		JSONObject fieldsObject=jsonReportSourceCfgObject.getJSONObject("fields");
		HashMap<String, JSONObject> actualReportConigFieldsmap=new HashMap<String,JSONObject>(); //map to store serviceid and brr rate (6700 services)
		List<String> slist = new ArrayList<String>();
		Iterator iter = fieldsObject.keys();
		while(iter.hasNext()){
			String key = (String)iter.next();
			slist.add(key); //list to store serviceids
			actualReportConigFieldsmap.put(key, new JSONObject(fieldsObject.getString(key)));
		}
		
		Reporter.log("Actual reportsource config object map..."+actualReportConigFieldsmap,true);
		boolean flag=false;
		for(String str: expectedAuditReportSourceCfgObjFields.stringPropertyNames())
		{
			Reporter.log("str::"+expectedAuditReportSourceCfgObjFields.get(str),true);
			flag=actualReportConigFieldsmap.containsKey(expectedAuditReportSourceCfgObjFields.get(str));
			Assert.assertTrue(flag,"ReportConfig resource field:"+str+" is not available"); //Validate Report config field
			if(flag)
				Assert.assertNotNull(actualReportConigFieldsmap.get(expectedAuditReportSourceCfgObjFields.get(str)), "ReportConfingField:"+str+" data is null"); //Validate Report config field Json Value
		}
	}
	//@Test
	public void testAllPredefinedWidgetLibraryTitles() throws Exception
	{
		JSONObject jsonAllPredefinedWidgetsObj =allPredefinedWidgets();
		JSONArray predefinedWidgetsArray = (JSONArray) jsonAllPredefinedWidgetsObj.getJSONArray("objects");	
		List<String> predefindWidgetsTitles=new ArrayList<String>();
		String predefinedWidgetTitle=null;
		for(int i=0; i<predefinedWidgetsArray.length(); i++)
		{
			predefinedWidgetTitle=predefinedWidgetsArray.getJSONObject(i).getString("title");
			Reporter.log("predefindWidgetsTitles list::"+predefindWidgetsTitles,true);
			Assert.assertNotNull(predefinedWidgetTitle,"Predefined widgets title should be ");
		}
	
	}
	
	
	@DataProvider(name = "reportsourcedatapayloadprovider")
	public Object[][] getReportSourceDataPayload() throws IOException {
		//File file = new File(FileHandlingUtils.getFileAbsolutePath(filePath));
		// 
		String reportSourceDataPath="/src/test/resources/Audit/reportsourcedatapayloadrequests/";
		File dir = new File(FileHandlingUtils.getFileAbsolutePath(reportSourceDataPath));
		String files[] = dir.list();
		InputStream stream = null;
		String jsonData = null;
		Object[][] inputData = new Object[1][2];
		int j=0;
		for(int i=0; i<files.length; i++) {
			String file = files[i];
			System.out.println("Report Source paload request file::"+file);
			
			stream = new FileInputStream(FileHandlingUtils.getFileAbsolutePath(reportSourceDataPath)+file);
			jsonData = IOUtils.toString(stream);
			inputData[j][0] = file;
			inputData[j][1] = jsonData;
			j++;					
		}
		/*
		for(int i=0; i<inputData.length; i++) {
			System.out.println("filename :" + inputData[i][0]);
			System.out.println("reportsource data payload:" + inputData[i][1]);
			
		}
		*/
		return inputData;
	}
	
	//@Test(dataProvider="reportsourcedatapayloadprovider")
	public void testReportDashBoardData(String filename, String reportSourceDataPayload) throws Exception
	{
		Reporter.log("started testReportDashBoardData ",true);
		Reporter.log("filename:: "+filename,true);
		//Reporter.log("reportSourceDataPayload "+reportSourceDataPayload,true);
		
		JSONObject reportSourceDataJsonObj=getReportSourceData(reportSourceDataPayload);
		Reporter.log("report Source:"+filename+" response: "+reportSourceDataJsonObj,true);
		
		JSONArray actualReportSourceDataJsonArray=reportSourceDataJsonObj.getJSONArray("data");
		if(actualReportSourceDataJsonArray!=null)
		{
			reportSourceDataValidations(reportSourceDataConfigProps.getProperty(filename),actualReportSourceDataJsonArray);
			
		}
	
		
	}
	
	private void reportSourceDataValidations(String reportSourceConfig,JSONArray actualReportSourceDataJsonArray) throws Exception
	{
		switch(reportSourceConfig)
		{
		   case AuditTestConstants.REPORT_SOURCE_DATA_SERVICE_COUNT:{
			   int expectedServiceCount=auditDashBoardReportResourceDataSetup.getExpectedServiceCount();
			   Integer actualServiceCount=(Integer)actualReportSourceDataJsonArray.get(0);
			   
			   Assert.assertEquals(actualServiceCount.intValue(), expectedServiceCount,
					   "Expected Service Count should be: "+expectedServiceCount+" but found: "+actualServiceCount.intValue());
		   break;
		   }
		   case AuditTestConstants.REPORT_SOURCE_DATA_TOP5_SERVICES_GROUPBY_BRR:{
			   int expectedServiceCount=auditDashBoardReportResourceDataSetup.getExpectedServiceCount();
			   Integer actualServiceCount=(Integer)actualReportSourceDataJsonArray.get(0);
			   
			   Assert.assertEquals(actualServiceCount.intValue(), expectedServiceCount,
					   "Expected Service Count should be: "+expectedServiceCount+" but found: "+actualServiceCount.intValue());
		   break;
		   }
		   
		   
		}
		
	}
	
	
	private JSONObject getReportSourceData(String reportSourcePayload) throws Exception
	{
		
		HttpResponse reportSourceDataHttpResp = AuditFunctions.reportSourceData(restClient,new StringEntity(reportSourcePayload));	
		Assert.assertEquals(reportSourceDataHttpResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response Code should be");
		String strReportSourceData = ClientUtil.getResponseBody(reportSourceDataHttpResp);
		
		JSONObject reportSourceDataRespJsonObj = new JSONObject(strReportSourceData);
		Assert.assertNotNull(reportSourceDataRespJsonObj,"ReportSource Data null");
		return reportSourceDataRespJsonObj;
		
	}
	
	private JSONObject allPredefinedWidgets() throws Exception
	{
		HttpResponse predefinedWidgetsHttpResp = AuditFunctions.getPredefinedWidgets(restClient);		
		Assert.assertEquals(predefinedWidgetsHttpResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strAllPredefinedWidgetsObj = ClientUtil.getResponseBody(predefinedWidgetsHttpResp);
		JSONObject jsonAllPredefinedWidgetsObj = new JSONObject(strAllPredefinedWidgetsObj);
		
		Assert.assertNotNull(jsonAllPredefinedWidgetsObj,"Audit All Predefined");
		
		return jsonAllPredefinedWidgetsObj;
		
	}
	
	private JSONObject auditReportSourceObj() throws Exception
	{
		HttpResponse reportSourcesHttpResp = AuditFunctions.getDashBoardReportSources(restClient);		
		Assert.assertEquals(reportSourcesHttpResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strReportSourceObj = ClientUtil.getResponseBody(reportSourcesHttpResp);
		JSONObject jsonReportSourceObject = new JSONObject(strReportSourceObj);
		JSONObject jsonReportSourceSubObject = jsonReportSourceObject.getJSONObject("Audit").getJSONObject("audit_details");
		
		Assert.assertNotNull(jsonReportSourceSubObject,"Audit Report Source Object null");
		return jsonReportSourceSubObject;
		
	}
	private JSONObject getAuditReportSourceConfig() throws Exception
	{
		List<NameValuePair> reportSourceConfigQueryParams = new ArrayList<NameValuePair>();				
		reportSourceConfigQueryParams.add(new BasicNameValuePair("source", (String)auditReportSourceObj().getString("id")));
		
		
		HttpResponse reportSourcesConfigHttpResp = AuditFunctions.getAuditReportSourceConfig(restClient,reportSourceConfigQueryParams);		
		Assert.assertEquals(reportSourcesConfigHttpResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strReportSourceCfgObj = ClientUtil.getResponseBody(reportSourcesConfigHttpResp);
		JSONObject jsonReportSourceCfgObject = new JSONObject(strReportSourceCfgObj);
		
		Reporter.log("audit report source config object:"+jsonReportSourceCfgObject,true);
		Assert.assertNotNull(jsonReportSourceCfgObject,"Audit Report Source Config Object null");
		return jsonReportSourceCfgObject;
		
	}	
	
	
	
	
}
