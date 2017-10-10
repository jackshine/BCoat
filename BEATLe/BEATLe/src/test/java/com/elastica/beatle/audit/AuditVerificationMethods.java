/**
 * 
 */
package com.elastica.beatle.audit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;

/**
 * @author anuvrath
 *
 */
public class AuditVerificationMethods {
	
	private Client restClient;
	/**
	 * @param restClient
	 */
	public AuditVerificationMethods(Client restClient) {
		super();
		this.restClient = restClient;
	}
	
	/**
	 * This method will verify the Audit summary/Report results for un-processed logs
	 * @param sourceID
	 * @throws Exception
	 */
	public synchronized void testVerifyAuditResultsNotProducedForUnProcessedLogs(String sourceID) throws Exception
	{
		HttpResponse response=null;
		JSONObject summaryObject=null;
		JSONObject reportObject=null;
		
		String range = "1mo";			
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		response  = AuditFunctions.getAuditSummary(restClient, queryParam);	
		
		//The below code temporarly added to enable latest audit api's changed behavior 
		if(HttpStatus.SC_METHOD_NOT_ALLOWED == response.getStatusLine().getStatusCode()){
			Reporter.log("entering into  new audit summary api behavior section",true);
			//try post method
			response  = AuditFunctions.getSummary(new Client(), new StringEntity(AuditTestUtils.getAuditSummaryNewPayload(sourceID, range)));				
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			
			String strResponse=ClientUtil.getResponseBody(response);
			JSONArray jsonarray = new JSONArray(strResponse);
			summaryObject = jsonarray.getJSONObject(0);				
			
		}
		else{
			Reporter.log("entering into  old audit summary api behavior section",true);
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		}
		//summary validation
						
		Assert.assertEquals(summaryObject.get("total_users"), 0, "From Summary: Total users should be ");
		Assert.assertEquals(summaryObject.get("total_destinations"), 0, "From Summary: Total Destinations should be ");
		Assert.assertEquals(summaryObject.get("total_services"), 0, "From Summary: Total Services should be ");
		Assert.assertEquals(summaryObject.get("high_risk_services"), 0, "From Summary: Total high_risk_services should be ");
		
		
		//report results verification
		response  = AuditFunctions.getAuditReport(restClient, queryParam);		
		if(HttpStatus.SC_METHOD_NOT_ALLOWED == response.getStatusLine().getStatusCode()){//for eoe
			Reporter.log("entering into  new audit report api behavior section",true);	
			response  = AuditFunctions.getAuditReport(new Client(), new StringEntity(AuditTestUtils.getAuditReportNewPayload(sourceID,range)));				
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			
			String strResponse=ClientUtil.getResponseBody(response);
			JSONArray jsonarray = new JSONArray(strResponse);
			reportObject = jsonarray.getJSONObject(0);				
			
		}else{//for cep/prod 
		Reporter.log("entering into  old audit report api behavior section",true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		}	
		
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
