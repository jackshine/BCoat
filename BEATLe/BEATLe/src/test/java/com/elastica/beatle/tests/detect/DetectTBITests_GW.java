/*package com.elastica.beatle.tests.detect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Method;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectDataProviders;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.universal.dtos.UserAccount;

public class DetectTBITests_GW extends DetectUtils{
	
	private static final String OBJECTS = "detect_attributes";
	private static final String INSERT = "insert";
	private static final String TOO_MANY_SEQUENCE = "TOO_MANY_SEQUENCE_";
	
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	DetectCommonutils dcUtils = new DetectCommonutils();
	
	protected UserAccount saasAppUserAccount;
	protected UserAccount saasAppGraphUserAccount;
	
	//DetectUtils utils = new DetectUtils();
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	
	@BeforeClass()
	public void beforeClass() throws Exception {
		
	 props =	utils.getPropertyValues();
	String responseBody =  getListOfIncidents();
	JsonNode jnode = unmarshall(responseBody, JsonNode.class);
	if (jnode.isArray()) {
	    for (final JsonNode objNode : jnode) {
	    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
	    	String facilty = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
	    	String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
	    	String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
	    	String source = getJSONValue(objNode.toString(), "src").toString().replace("\"", "");
	    	String attr_set = getJSONValue(objNode.toString(), "ats").toString().replace("\"", "");
	    	String Activity_type = getJSONValue(objNode.toString(), "at").toString().replace("\"", "");
	    	String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
	    	String ObjectType = getJSONValue(objNode.toString(), "ot").toString().replace("\"", "");
	    	
	    	updateLog(report_id, severity, ObjectType, email, Activity_type, index, facilty, source, attr_set);
	    	
	       }
	    }
		
	}

	@Test(  description = "This test operates on elastic search injected data, and generates large upload incidents.")
	public void upload_Limit_Tests(Method method) throws Exception {		
		String ioi_Code = "TOO_MANY_SUM_LARGE_UPLOADS";
		AttributeBean attributeBean = new AttributeBean(1, 10, 4, true);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		updateDetectAttributesForTBI(true, attributeBean, getResponseArray, ioi_Code);
		
		System.out.println("Response Body is -----"+responseBody);
		System.out.println("Respone aray is "+getResponseArray);
		System.out.println("User Name is ::::::"+suiteData.getSaasAppUsername());
  
		
//		if(enabled){
//
//			resp = getDetectAttributes();
//			responseBody = getResponseBody(resp);
//			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
//			for (int index = 0; index < getResponseArray.length(); index++) {
//				JSONObject attributeObject = getResponseArray.getJSONObject(index);
//				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
//					if(attributeObject.get("enabled").toString().equals("false")){
//						Reporter.log("");
//						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
//						Reporter.log("");
//						throw new SkipException("preference got changed in between test excetuion so skipping test");
//					}
//				}
//			}
//		}
		
	System.out.println("Out of loop");
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 10; j++) {
			
			dcUtils.replayLogs("AllFiles,Box,uploadLarger20MBFile.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add("User uploaded file \"Test.txt\" to folder \"\"");
			Thread.sleep(1*45*1000);
			
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList,20,2);
		
		
		Thread.sleep(60000);
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		
		 validateIncidents(ioi_Code);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@Test( dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, description = "This test operates on elastic search injected data, and generates large download incidents.")
	public void download_Limit_Tests(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		
		InputBean inputBean = createFileUpdateData1("test_D_01", attributeBean);
		
		String ioi_Code = "TOO_MANY_SUM_LARGE_DOWNLOADS";
		boolean enabled = attributeBean.isEnabled();
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		System.out.println("Response Body is -----"+responseBody);
		System.out.println("Respone aray is "+getResponseArray);
		System.out.println("User Name is ::::::"+suiteData.getSaasAppUsername());

		
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
	
		}
		
		System.out.println("Out of loop");
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			
			dcUtils.replayLogs("AllFiles,Box,uploadLarger10MBFile.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add("User uploaded file \"Test.txt\" to folder \"\"");
			Thread.sleep(1*45*1000);
			
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList,20,2);
		
		
		Thread.sleep(60000);
		verifyDetectedUser(suiteData.getSaasAppUsername());
		
		validateIncidents(ioi_Code);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
		

	public String validateIncidents(String ioicode) {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, -5);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom,
						strDateTimeTo);
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Reporter.log(" validateIncidents::::  Response::::   " + responseBody, true);
				JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				List<String> iois = new ArrayList<>();
				if (jnode.isArray()) {
				    for (final JsonNode objNode : jnode) {
				    	String ioi = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				    	iois.add(ioi);
				    	
				       }
				    }
		
				Assert.assertTrue(iois.contains(ioicode), "incident not listed");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseBody;
	}



	private void Log(Method method, AttributeBean attributeBean, InputBean inputBean) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log("This Test is to validate " + method.getName().replace("_Tests", "")+",   And preferences for this test are  preference enabled???:::::: "+attributeBean.isEnabled() +" window:: "
				+ ""+attributeBean.getWindow()+" event::: "+attributeBean.getThreshold()+" importance:::: "+attributeBean.getImportance(), true);
		Reporter.log(" ");
		Reporter.log("Test details :::::::  userName::  " + inputBean.getUserName()+"  user::::: "+inputBean.getUser()+" testId:::  "+inputBean.getTestId(), true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and password for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}

}
	
	
	
	
*/