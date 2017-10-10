/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.dci.DCIConstants;


/**
 * @author Mallesh
 *
 */
public class AuditDataSourcesCleanUpScriptForAllAutomationTenants extends AuditInitializeTests{
	protected Client restClient;	
	
	/**
	 * @param FireWallName
	 */
	public AuditDataSourcesCleanUpScriptForAllAutomationTenants(String FireWallName) {
		restClient = new Client();
		
	}	
	
	@Test(groups ={"getData"})
	public void getFolderFromS3Bucket() throws Exception
	{
		S3ActionHandler s3 = new S3ActionHandler();
		s3.downloadFolderFromS3Bucket(AuditTestConstants.AUDIT_S3_BUCKET, "Audit/be_firewall_logs_7z", 
				AuditTestConstants.AUDIT_FILE_TEMP_PATH);
		s3.downloadFolderFromS3Bucket(AuditTestConstants.AUDIT_S3_BUCKET, "Audit/be_firewall_logs_7za", 
				AuditTestConstants.AUDIT_FILE_TEMP_PATH);
		s3.downloadFolderFromS3Bucket(AuditTestConstants.AUDIT_S3_BUCKET, "Audit/be_firewall_logs_bz2", 
				AuditTestConstants.AUDIT_FILE_TEMP_PATH);
		s3.downloadFolderFromS3Bucket(AuditTestConstants.AUDIT_S3_BUCKET, "Audit/be_firewall_logs_gz", 
				AuditTestConstants.AUDIT_FILE_TEMP_PATH);
		s3.downloadFolderFromS3Bucket(AuditTestConstants.AUDIT_S3_BUCKET, "Audit/be_firewall_logs_zip", 
				AuditTestConstants.AUDIT_FILE_TEMP_PATH);
	}
	
	
	@Test
	public void cleanupDatasources() throws Exception
	{
		//get List of Datasources
		String sourceID=null;
		
		  Reporter.log("getting the datasource from the datasources list:",true);
	      List<NameValuePair> queryParam = new ArrayList<NameValuePair>();		
		  queryParam.add(new BasicNameValuePair("fields", "datasources"));
		  queryParam.add(new BasicNameValuePair("limit", "100"));
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
					dsList.add(sourceID);
					
				}
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

@Test
public void cleanUpSpanvaDataSources() throws Exception {
		
		
		//get Spanva Agent Datasources List
		HttpResponse response=null;
		response = AuditFunctions.listSpanvaAgentDSs(restClient);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject serviceJsonObject = new JSONObject(ClientUtil.getResponseBody(response));
		
		JSONArray agentDsArray = serviceJsonObject.getJSONArray("objects");
		int agentDsArraySize=agentDsArray.length();
		String spnvaAgentDatasourceId=null;
		for(int i=0; i<agentDsArraySize; i++)
		{
			
			spnvaAgentDatasourceId=((JSONObject)agentDsArray.get(i)).getString("id");
			response = AuditFunctions.deleteSpanaAgentDataSource(restClient, spnvaAgentDatasourceId);
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
			
		}
		
		
		
		Reporter.log("**************************Audit Datasource delete  Test Completed**************************",true);
	}
}
