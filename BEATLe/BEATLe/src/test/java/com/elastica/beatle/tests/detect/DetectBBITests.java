package com.elastica.beatle.tests.detect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.es.ElasticSearchLogs;

public class DetectBBITests extends DetectUtils {
	
	private static final String OBJECTS = "detect_attributes";
	String user = null;
	
	
	
	
	 @DataProvider()
	    public Object[][] BBIDataProvider(Method method)  {
		 List<AttributeBean> list = new ArrayList<>();
		 list.add(new AttributeBean(10, 1,true));
		 list.add(new AttributeBean(20, 2,true));
		 list.add(  new AttributeBean(30, 4,true));
		 list.add(new AttributeBean(40, 3,true));
		 list.add(new AttributeBean(50, 4,true));
		 list.add(new AttributeBean(10, 4,false));
		 Object[][] atrributesDP = new Object[list.size()][];
			for(int i=0; i<atrributesDP.length;i++){
				atrributesDP[i] = new Object[] { "", list.get(i)};
			}
			return atrributesDP;
		}		
	 
	 
	
	@DataProvider()
	    public Object[][] BBIDataProvider1(Method method)  {
		 List<AttributeBean> list = 	getAttributeList();
		 Object[][] atrributesDP = new Object[list.size()][];
			
			
			for(int i=0; i<atrributesDP.length;i++){
				atrributesDP[i] = new Object[] { "", list.get(i)};
			}
				
			return atrributesDP;
		}
	 
	 
	 protected static List<AttributeBean> getAttributeList() {
			
			List<AttributeBean> list = new ArrayList<>();
			
			AttributeBean  ab1 = new  AttributeBean(10, 1,true);
			AttributeBean  ab2 = new AttributeBean(20, 2,true);
			AttributeBean  ab3 = new AttributeBean(30, 3,true);
			AttributeBean  ab4 = new AttributeBean(40, 3,true);
			AttributeBean  ab5 = new AttributeBean(50, 4,true);
			AttributeBean  ab6 = new AttributeBean(60, 2,true);
			AttributeBean  ab7 = new AttributeBean(70, 3,true);
			AttributeBean  ab8 = new AttributeBean(80, 3,true);
			AttributeBean  ab9 = new  AttributeBean(90, 4,true);
			AttributeBean  ab14 = new AttributeBean(10, 4,false);
			
				list.add(ab1);
				list.add(ab2);
				list.add(ab3);
				list.add(ab4);
				list.add(ab5);
				list.add(ab6);
				list.add(ab7);
				list.add(ab8);
				list.add(ab9);
				list.add(ab14);
			
			
			System.out.println("list of attributes :::: "+list);
			
			return list;
		}

	@Test(dataProvider = "BBIDataProvider", description = "This test operates on real API data, and generates BBI frequent sessions  incidents.", groups={PRIORITY_1, ALL})
	public void frequent_Sessions_Tests(Method method, String fileName, AttributeBean attributeBean) throws Exception {

		InputBean inputBean = createFileUpdateDataforfrequent("frequentUserActions", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_FREQUENT_SESSIONS";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}

		@Test(dataProvider = "BBIDataProvider", description = "This test operates on real API data, and generates BBI for  frequent deletes incidents.", groups={PRIORITY_1, ALL})
	public void frequent_Deletes_Tests(Method method, String fileName, AttributeBean attributeBean) throws Exception {
			
			InputBean inputBean = createFileUpdateDataforfrequent("frequentDeletesTest", attributeBean);
			
			Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_FREQUENT_DELETES";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(dataProvider = "BBIDataProvider",  description = "This test operates on real API data, and generates BBI frequent sharing incidents.", groups={PRIORITY_1, ALL})
	public void frequent_Sharing_Tests(Method method, String fileName, AttributeBean attributeBean) throws Exception {

		InputBean inputBean = createFileUpdateDataforfrequent("largeShareTest", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_FREQUENT_SHARING";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	

	@Test(dataProvider = "BBIDataProvider1", description = "This test operates on real API data, and generates BBI for large sharing incidents.", groups={PRIORITY_1, ALL})
	public void large_Sharing_Tests(Method method, String fileName, AttributeBean attributeBean) throws Exception {
		
		InputBean inputBean = createFileUpdateDataforLargeBBI("largeShareTest", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_SHARING";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}

	

  @Test(dataProvider = "BBIDataProvider1", description = "This test operates on real API data, and generates BBI large delets incidents.", groups={PRIORITY_1, ALL})
	public void large_Deletes_Tests(Method method, String fileName, AttributeBean attributeBean) throws Exception {
		   
		InputBean inputBean = createFileUpdateDataforLargeBBI("largeDeletesTest", attributeBean);
		
	    Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_DELETES";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}



	@Test(dataProvider = "BBIDataProvider1",  description = "This test operates on real API data, and generates BBI large upload incidents.", groups={PRIORITY_1, ALL})
	public void large_Uploads_Tests(Method method, String fileName, AttributeBean attributeBean) throws Exception {
		
		InputBean inputBean = createFileUpdateDataforLargeBBI("largeUploadTest", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_UPLOAD";
		
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		boolean enabled = attributeBean.isEnabled();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	

	@Test(dataProvider = "BBIDataProvider1",  description = "This test operates on real API data, and generates BBI large download incidents.", groups={PRIORITY_1, ALL})
	public void large_Downloads_Tests(Method method, String fileName, AttributeBean attributeBean) throws Exception {
		
		InputBean inputBean = createFileUpdateDataforLargeBBI("largeDownloadTest", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_DOWNLOAD";
		boolean enabled = attributeBean.isEnabled();
		
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
	}

	/*@Test(dataProvider = "BBIDataProvider1",  description = "This test operates on real API data, and generates BBI large upload across services incidents.")
	public void large_Upload_Across_Services_Tests_5services(Method method, String fileName, AttributeBean attributeBean)
			throws Exception {
		
		InputBean inputBean = createFileUpdateDataforLargeBBI("largeUploadASTest", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}*/
	
	
	@Test(dataProvider = "BBIDataProvider1",  description = "This test operates on real API data, and generates BBI large upload across services incidents.", groups={PRIORITY_1, ALL})
	public void large_Upload_Across_Services_Tests_2services(Method method, String fileName, AttributeBean attributeBean)
			throws Exception {
		
		InputBean inputBean = createFileUpdateDataforLargeBBI("largeUploadASTest_twoservices", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	/*@Test(dataProvider = "BBIDataProvider1",  description = "This test operates on real API data, and generates BBI large upload across services incidents.")
	public void large_Upload_Across_Services_Tests_3services(Method method, String fileName, AttributeBean attributeBean)
			throws Exception {
		
		InputBean inputBean = createFileUpdateDataforLargeBBI("largeUploadASTest_3services", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
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
			verifyProfile(inputBean.getUser(), ioi_Code);
			
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
*/
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

	
	/*@Test(dataProvider = "BBIDataProvider1",  description = "This test operates on real API data, and generates BBI large upload/download incidents.")
	public void large_UpDownloads_Tests(Method method, String fileName, AttributeBean attributeBean) throws Exception {
		Log(method, attributeBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_UPDOWNLOAD";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		String strDateStart = "2015-03-19T01:27:06";
		String strDateStop = "2015-03-19T02:39:06";  
		InputBean inputBean = createFileUpdateData("largeDownloadTest", strDateStart, strDateStop);
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
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						abortTest=true;
					}
				}
			}
		if (!abortTest) {
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}*/
	
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
