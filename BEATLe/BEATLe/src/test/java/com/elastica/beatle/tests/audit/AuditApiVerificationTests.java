package com.elastica.beatle.tests.audit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;

public class AuditApiVerificationTests extends AuditInitializeTests{
	
	protected Client restClient;

	@BeforeClass(alwaysRun = true)
	public void init() throws Exception{
		restClient = new Client();
	}
	
	
	@Test
	public void deleteDatasources() throws Exception
	{
		//get List of Datasources
		String sourceID=null;
		
		  Reporter.log("getting the datasource from the datasources list:",true);
	      List<NameValuePair> queryParam = new ArrayList<NameValuePair>();		
		  queryParam.add(new BasicNameValuePair("fields", "datasources"));
		  queryParam.add(new BasicNameValuePair("limit", "200"));
	      HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient,queryParam);
			Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			
			String strDatataSourceListResp=ClientUtil.getResponseBody(datataSourceListResp);
			Reporter.log("List of Datasources:"+strDatataSourceListResp,true);
			
			JSONObject listObj=new JSONObject(strDatataSourceListResp);
			JSONObject tenantObj=listObj.getJSONObject("objects");
			JSONArray datasourcesList = tenantObj.getJSONArray("datasources");
			ArrayList<String> dsList=new ArrayList<String>();
			for(int i=0; i<datasourcesList.length(); i++)
			  {
				if(  ((JSONObject)datasourcesList.get(i)).getString("name").startsWith("BE_")){
					sourceID=((JSONObject)datasourcesList.get(i)).getString("id");
					dsList.add(sourceID);}
			  }
			Reporter.log("dsList"+dsList,true);
			
			HttpResponse response=null;
		for(String dsId: dsList)
		{
			response=AuditFunctions.deleteDataSource(restClient, dsId);
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
			
		}
		Reporter.log("*****************************************Datasource: "+sourceID+" deleted sucessfully",true);
		Reporter.log("**************************Datasource Deletion Test Completed**************************",true);
		
	}	
  
//@Test(description="serviceverificaitontests")
  public void testVerifyServiceDetails() throws Exception {
		
	List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
	queryParam.add(new BasicNameValuePair("limit", "1000"));
	HttpResponse response  = AuditFunctions.getServiceDetails(restClient, queryParam);
	
	JSONObject serviceJsonObject = new JSONObject(ClientUtil.getResponseBody(response));
	JSONArray servicesJsonArray = serviceJsonObject.getJSONArray("objects");
	
	String serviceId=null;
	String serviceName=null;
	String serviceScore=null;
	Map<String,String> servicesMap=new HashMap<String,String>();
	int serviceArrayCount=servicesJsonArray.length();
	Reporter.log("serviceArrayCount.."+serviceArrayCount);
	
	for(int i=0; i<serviceArrayCount; i++)
	{
		serviceId=((JSONObject)servicesJsonArray.get(i)).getString("service_id");
		serviceName=((JSONObject)servicesJsonArray.get(i)).getString("service_name");
		serviceScore=((JSONObject)servicesJsonArray.get(i)).getString("service_score");
		servicesMap.put(serviceId, serviceName);
		
		Assert.assertNotNull(serviceId,"ServiceId is null");
		Assert.assertNotNull(serviceName,"ServiceName is null");
		Assert.assertNotNull(serviceScore,"ServiceScore is null");
		
	}
	
  }
}
