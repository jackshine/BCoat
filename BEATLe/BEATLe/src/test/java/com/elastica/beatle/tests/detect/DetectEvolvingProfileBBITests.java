package com.elastica.beatle.tests.detect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.splunk.SplunkQueryResult;

public class DetectEvolvingProfileBBITests extends DetectUtils {
	
	private static final String OBJECTS = "detect_attributes";
	String user = null;
	AttributeBean attributeBean ;
	
	
	
	@BeforeClass()
	public void beforeClass() throws Exception {
		
		
		
	}	
	
	
	
	
	@Test(description = "")
	public void large_Uploads_Tests(Method method) throws Exception {
		
		
		String[] ioi_Codes  = { IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString()};
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		attributeBean = new AttributeBean(60, 2, true);
		attributeBean.setEnabled(true);
		boolean enabled = attributeBean.isEnabled();
		updateBBIAttributes1(enabled, attributeBean, getResponseArray, ioi_Codes);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		
		String dateDiff ="18,18,18,18,18,18,18,18,18,18,198,18,18,18,18,18,18,18,18,18,198,18,18,18,18,18,18,18,18,18,198,60,60,60,198,60,60,60,198,90,90,198,60,60,60";
		
		InputBean inputBean = createFileUpdateDataforEvolvingProfile("largeUploadprofileforEP1", attributeBean, dateDiff);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString();
		 enabled = attributeBean.isEnabled();
		
		Log(method, attributeBean,inputBean);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

		if(enabled){
			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {
			/*verify profile is create for the first time*/
			
			
		verifyProfile(inputBean.getUser(), ioi_Code);
		String searchQuery = "Upload.SumFile_Size.GW.Box."+inputBean.getUser();
//		
//		SplunkQueryResult splunkQueryResult =	com.elastica.beatle.splunk.SplunkQueries.lookForEvolvingProfileLogs(searchQuery, suiteData.getEnvironmentName(), "-2h");
//		Logger.info("verify first stable profile  "+splunkQueryResult.toString());
//		Logger.info("verify first stable profile  "+splunkQueryResult.getQueryResult());
//		Logger.info("verify first stable profile  "+splunkQueryResult.getNumberOfResults());
//		Logger.info("verify first stable profile  "+splunkQueryResult.getEventsCount());
			
				 	//verifyStateIncidents(inputBean.getUser(), expIncidents);
		Thread.sleep(3 * 60 * 1000);
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		SplunkQueryResult splunkQueryResult =	com.elastica.beatle.splunk.SplunkQueries.lookForEvolvingProfileLogs(searchQuery, suiteData.getEnvironmentName(), "-2h");
		//	Logger.info("verify profile stabilaization "+splunkQueryResult.getQueryResult());
			
			JSONObject splunkLogs = splunkQueryResult.getQueryResult();		
			Reporter.log("       #####################################");
			Reporter.log(" ");
			Logger.info("splunkLog for Evolving Profile :::: "+splunkLogs.get("results"));
			Reporter.log(" ");
			Reporter.log("       #####################################");		
		Logger.info("verify for "+"SdR Evolving profile become unstabled ("+searchQuery+")");
		String splunkLogsString = splunkLogs.toString();
		Assert.assertTrue(splunkLogsString.contains("SdR Evolving profile become unstabled ("+searchQuery+")"), "Test failed due to above log is not found in the server(DGF) logs ");
		
		
		
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	
	@Test(description = "")
	public void frequent_deletes_Tests(Method method) throws Exception {
		
		
		String[] ioi_Codes  = { IOI_Code.ANOMALOUSLY_FREQUENT_DELETES.toString()};
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		attributeBean = new AttributeBean(30, 2, true);
		attributeBean.setEnabled(true);
		boolean enabled = attributeBean.isEnabled();
		updateBBIAttributes1(enabled, attributeBean, getResponseArray, ioi_Codes);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		
		String dateDiff ="30,30,30,30,30,180,30,30,30,30,180,30,30,30,30,180,30,30,30,30,180,30,30,30,30,180,30,30,30,30,180,22,22,22,22,22,22,22,180,60,60";
		
		InputBean inputBean = createFileUpdateDataforEvolvingProfile("frequentDeleteEP", attributeBean, dateDiff);
		String ioi_Code = IOI_Code.ANOMALOUSLY_FREQUENT_DELETES.toString();
		 enabled = attributeBean.isEnabled();
		
		Log(method, attributeBean,inputBean);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

		if(enabled){
			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {
			/*verify profile is create for the first time*/
			
		verifyProfile(inputBean.getUser(), ioi_Code);
		//TODO::: 
		String  searchQuery = "Delete.GW.Box."+inputBean.getUser();
		
		Thread.sleep(3 * 60 * 1000);
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		SplunkQueryResult splunkQueryResult =	com.elastica.beatle.splunk.SplunkQueries.lookForEvolvingProfileLogs(searchQuery, suiteData.getEnvironmentName(), "-2h");
		//	Logger.info("verify profile stabilaization "+splunkQueryResult.getQueryResult());
			
		JSONObject splunkLogs = splunkQueryResult.getQueryResult();		
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Logger.info("splunkLog for Evolving Profile :::: "+splunkLogs.get("results"));
		Reporter.log(" ");
		Reporter.log("       #####################################");
		
		Logger.info("verify for "+"SdR Evolving profile become unstabled ("+searchQuery+")");
		String splunkLogsString = splunkLogs.toString();
		Assert.assertTrue(splunkLogsString.contains("SdR Evolving profile become unstabled ("+searchQuery+")"), "Test failed due to above log is not found in the server(DGF) logs ");
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}

	
	
	@Test(description = "")
	public void frequent_Share_Tests(Method method) throws Exception {
		
		
		String[] ioi_Codes  = { IOI_Code.ANOMALOUSLY_FREQUENT_SHARING.toString()};
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		attributeBean = new AttributeBean(30, 2, true);
		attributeBean.setEnabled(true);
		boolean enabled = attributeBean.isEnabled();
		updateBBIAttributes1(enabled, attributeBean, getResponseArray, ioi_Codes);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		
		String dateDiff ="30,30,30,30,30,180,30,30,30,30,180,30,30,30,30,180,30,30,30,30,180,30,30,30,30,180,30,30,30,30,180,22,22,22,22,22,22,22,180,60,60";
		
		InputBean inputBean = createFileUpdateDataforEvolvingProfile("frequentShareEP", attributeBean, dateDiff);
		String ioi_Code = IOI_Code.ANOMALOUSLY_FREQUENT_SHARING.toString();
		 enabled = attributeBean.isEnabled();
		
		Log(method, attributeBean,inputBean);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

		if(enabled){
			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {
			/*verify profile is create for the first time*/
			
		verifyProfile(inputBean.getUser(), ioi_Code);
		//TODO::: SdR Evolving profile become unstabled (Delete.GW.Box.detect_ALPRgTOLj2u2_frequentDeleteEP@detectbeatle.com)
		String  searchQuery = "Share.GW.Box."+inputBean.getUser();
		
		Thread.sleep(3 * 60 * 1000);
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		SplunkQueryResult splunkQueryResult =	com.elastica.beatle.splunk.SplunkQueries.lookForEvolvingProfileLogs(searchQuery, suiteData.getEnvironmentName(), "-2h");
	//	Logger.info("verify profile stabilaization "+splunkQueryResult.getQueryResult());
		
		JSONObject splunkLogs = splunkQueryResult.getQueryResult();		
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Logger.info("splunkLog for Evolving Profile :::: "+splunkLogs.get("results"));
		Reporter.log(" ");
		Reporter.log("       #####################################");
		
		
		Logger.info("verify for "+"SdR Evolving profile become unstabled ("+searchQuery+")");
		String splunkLogsString = splunkLogs.toString();
		Assert.assertTrue(splunkLogsString.contains("SdR Evolving profile become unstabled ("+searchQuery+")"), "Test failed due to above log is not found in the server(DGF) logs ");
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	private void Log(Method method, AttributeBean attributeBean, InputBean inputBean) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log("This Test is to validate " + method.getName().replace("_Tests", "")+",   And preferences for this test are  preference enabled???:::::: "+attributeBean.isEnabled() +" confidence:: "
				+ ""+attributeBean.getConfidence()+" importance:::: "+attributeBean.getImportance(), true);
		Reporter.log(" ");
		Reporter.log("Test details :::::::  userName::  " + inputBean.getUserName()+"  user::::: "+inputBean.getUser()+" testId:::  "+inputBean.getTestId(), true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and pass word for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}

	
	@AfterMethod
	public void tearDown(ITestResult results) throws Exception {
		System.out.println("Cleaning up Resources");
		
		HttpResponse response;
		String responseBody;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		if (results.isSuccess() && user != null) {
			
			response = esLogs.clearThreatscore(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);

			response = esLogs.clearState(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);

			response = esLogs.clearDetectProfile(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);
			
			user = null;
		} else {
			System.out.println("No Cleanup Required - I am not cleaning up data to debug if needed!");
		}
		
		System.out.println("Cleaning up Resources - COMPLETED");
	}

}
