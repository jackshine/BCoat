/**
 * 
 */
package com.elastica.beatle.tests.splunkTests;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.splunk.SplunkQueries;
import com.elastica.beatle.splunk.SplunkQueryResult;

/**
 * @author anuvrath
 *
 */
public class SplunkTest extends SplunkInitializer{
	private SoftAssert s_assert = null;
	 
	@BeforeMethod(alwaysRun=true)
	public void initTests(){
		s_assert = new SoftAssert();
	}
	
	@Test
	public void testAuditDailyReport(){
		Logger.info("This test looks for daily audit report in splunk");
		SplunkQueryResult splunkResult = SplunkQueries.lookforAuditReportLogInSplunk(suiteData.getEnvironmentName(),emailRecipientName,auditDailyReportName, "-24h");
		Assert.assertTrue(splunkResult.getEventsCount()>0,"The total number of event count found is 0. Expecting atleast 1");
		Logger.info("Expecting at least 1 events for daily audit report and found "+splunkResult.getEventsCount()+" Test passed");
	}	
	
	@Test
	public void testAuditWeeklyReport(){
		Logger.info("This test looks for Weekly audit report in splunk");
		SplunkQueryResult splunkResult = SplunkQueries.lookforAuditReportLogInSplunk(suiteData.getEnvironmentName(),emailRecipientName,auditWeeklyReportName, "-7d");
		Assert.assertTrue(splunkResult.getEventsCount()>0,"The total number of event count found is 0. Expecting atleast 1");		
		Logger.info("Expecting at least 1 events for Weekly audit report and found "+splunkResult.getEventsCount()+" Test passed");
	}
	
	@Test
	public void testAuditMonthlyReport(){
		SplunkQueryResult splunkResult = SplunkQueries.lookforAuditReportLogInSplunk(suiteData.getEnvironmentName(),emailRecipientName,auditMonthlyReportName, "-30d");
		Assert.assertTrue(splunkResult.getEventsCount()>0,"The total number of event count found is 0. Expecting atleast 1");		
		Logger.info("Expecting at least 1 events for Monthly audit report and found "+splunkResult.getEventsCount()+" Test passed");
	}
	
	@Test
	public void testDCIQueueSize() throws JSONException{
		SplunkQueryResult splunkResult = SplunkQueries.lookForCIJobPoolSizes(suiteData.getEnvironmentName(), "-2h");
		JSONArray result = splunkResult.getQueryResult().getJSONArray("results");
		for(int i = 0;i<result.length();i++){
			JSONObject resultObject = result.getJSONObject(i);
			Logger.info("Host: "+ resultObject.getString("host") +" Size: "+ resultObject.getString("first(cnt)"));
			s_assert.assertTrue(Integer.parseInt(resultObject.getString("first(cnt)"))<=40,"job pool size is more than 40 for the host "+resultObject.getString("host")+". Expecting it to be less than 40");
		}
		s_assert.assertAll();
		Logger.info("Expecting pool size for all hosts related to CI to be less than 40. And all of them are less than 40");		
	}
	
	@Test
	public void testGatewayInternalServerInIcapServer() throws JSONException{
		SplunkQueryResult splunkResult = SplunkQueries.lookforGatewayInternalServerInIcapServerViaSplunk(suiteData.getEnvironmentName(), "-2h");		
		Logger.info("Logs found for these tenants in these clouds. ");
		Logger.info("clouds\t count\t\t Tenant");
		JSONArray result = splunkResult.getQueryResult().getJSONArray("results");
		for(int i = 0;i<result.length();i++){
			JSONObject resultObject = result.getJSONObject(i);
			Logger.info(resultObject.getString("cloud")+"\t "+resultObject.getString("count")+"\t\t "+resultObject.getString("tenant"));
		}					
		Assert.assertTrue(splunkResult.getEventsCount()==0,"The total number of event count found is "+splunkResult.getEventsCount()+". Expecting atleast 0");
		Logger.info("Expecting 0 events for this error. And found "+splunkResult.getEventsCount()+" Test passed");
	}
	
	@Test
	public void testGooleAPIDailyLimitTests(){
		SplunkQueryResult splunkResult = SplunkQueries.lookForGmailDailyLimit(suiteData.getEnvironmentName(), "-2h");
		Assert.assertTrue(splunkResult.getEventsCount() == 0,"The total number of event count found is 0. Expecting atleast 0");		
		Logger.info("Expecting 0 events and found "+splunkResult.getEventsCount()+" Test passed");
	}
	
	@Test
	public void testEvolvingProfileLogs(){
		SplunkQueryResult splunkResult = SplunkQueries.lookForEvolvingProfileLogs("Upload.SumFile_Size.GW.Box.detect_YsYae23Ps0zF_largeUploadprofileforEP1", suiteData.getEnvironmentName(), "-2h");
		//Assert.assertTrue(splunkResult.getQueryResult() == 0,"The total number of event count found is 0. Expecting atleast 0");		
		Logger.info("Results "+splunkResult.getQueryResult()+" Test passed");
	}
	

}