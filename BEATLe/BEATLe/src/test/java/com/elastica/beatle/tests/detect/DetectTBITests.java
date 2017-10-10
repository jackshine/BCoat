package com.elastica.beatle.tests.detect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.elastica.beatle.FixRetryListener;
import com.elastica.beatle.RetryAnalyzer;
import com.elastica.beatle.detect.DetectDataProviders;
import com.elastica.beatle.detect.DetectInitializeTests;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.detect.dto.Objects;
import com.elastica.beatle.es.ActivityLogs;
import com.elastica.beatle.es.ElasticSearchLogs;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;


public class DetectTBITests extends DetectUtils {

	private static final String OBJECTS = "detect_attributes";
	String user = null;
	String responseBody ;
	DetectSequenceDetector dsd = new DetectSequenceDetector();

	@BeforeClass
	public void deleteSequenceDetectors() throws Exception{
		try {
		dsd.deleteSequenceDetectors(suiteData);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	@Test( dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, 
			description = "This test operates on elastic search injected data, and generates  encripted files incidents.", groups={PRIORITY_1, ALL})
	public void suspicious_Logins_Tests(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		
		
		InputBean inputBean = createFileUpdateData1("test_login_001", attributeBean);
		
		Log(method, attributeBean, inputBean);
		
		String responseBody;
		HttpResponse resp = getDetectAttributes();
		 responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.TOO_MANY_SUSPICIOUS_LOGINS.toString();
		
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);

		scpActivityLogsAndValidate(method, inputBean);
		
		user = inputBean.getUser();
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
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}


	@Test( dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, description = "This test operates on elastic search injected data, and generates invalid login incidents.")
	public void invalid_Login_Test(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		
		InputBean 	inputBean = createFileUpdateData1("test_login_003", attributeBean);
		
		Log(method, attributeBean, inputBean);
		
		String ioi_Code = "TOO_MANY_INVALID_LOGINS";
		boolean enabled = attributeBean.isEnabled();
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		
		
		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(30000);
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

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test( dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class,
			description = "This test operates on elastic search injected data, and generates large download incidents.", groups={PRIORITY_1, ALL})
	public void download_Limit_Tests(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		
		InputBean inputBean = createFileUpdateData1("test_D_01", attributeBean);
		
		Log(method, attributeBean, inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_SUM_LARGE_DOWNLOADS";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		
		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(30000);
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

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}

		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test( dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, 
			description = "This test operates on elastic search injected data, and generates large upload incidents.", groups={PRIORITY_1, ALL})
	public void upload_Limit_Tests(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		
		InputBean inputBean = createFileUpdateData1("test_u_01", attributeBean);
		
		Log(method, attributeBean, inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_SUM_LARGE_UPLOADS";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		
		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(30000);
		
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

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}

		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test( dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class,
			description = "This test operates on elastic search injected data, and generates policy violtion incidents.", groups={PRIORITY_1, ALL})
	public void policy_Violations_Tests(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		InputBean inputBean = createFileUpdateData1("test_p_01", attributeBean);
		Log(method, attributeBean,inputBean);
		//###########create policy##############
		//createPolicy();
		Reporter.log("Polcy created sucesfully ::::::     ",true);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_POLICY_VIOLATIONS";
		
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		

		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(1* 60 * 1000);
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

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}

		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test( dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, 
			description = "This test operates on elastic search injected data, and generates too many devices incidents.", groups={PRIORITY_1, ALL})
	public void divices_Tests(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		InputBean inputBean = createFileUpdateData1("test_b_01",attributeBean );
		Log(method, attributeBean,inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		
		String ioi_Code = "TOO_MANY_DEVICES";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		

		scpActivityLogsAndValidate(method, inputBean);
		user = inputBean.getUser();
		

		//Thread.sleep(1* 10 * 1000);	
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

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test( dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, 
			description = "This test operates on elastic search injected data, and generates too many browsers incidents.", groups={PRIORITY_1, ALL})
	public void browsers_Tests(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		InputBean inputBean = createFileUpdateData1("test001",attributeBean);
		Log(method, attributeBean,inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_BROWSERS";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		

		//Thread.sleep(30000);
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

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(inputBean.getUser());
		}
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test( dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, 
			description = "This test operates on elastic search injected data, and generates too encripted files incidents.", groups={PRIORITY_1, ALL})
	public void encripted_Files_Tests(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		
		InputBean inputBean = createFileUpdateData1("test_e_1",attributeBean);
		Log(method, attributeBean,inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_ENCRYPTED_FILES";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		

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

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(inputBean.getUser());
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}
	
	
	@Test( dataProvider = "suspiciousLocationdataProvider", dataProviderClass=DetectDataProviders.class,
			description = "This test operates on real GW data, and generates Suspicious Location incidents.", groups={PRIORITY_1, ALL})
	public void suspicious_Location_Test(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		InputBean inputBean = createFileUpdateData1("test_login_002", attributeBean);
		Log(method, attributeBean,inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_INFEASIBLE_LOCATIONS";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		

		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(30000);
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

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");		
	}
	
	

	@Test(  description = "This test operates on elastic search injected data, and generates  encripted files incidents.", groups={PRIORITY_1, ALL})
	public void InvalidLogins_Group_Tests(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(2, 1, 3, true);
	
		String tcId = "invalidLGTests";
		InputBean inputBean = null;
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String  fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		 String randomString1 = RandomStringUtils.randomAlphanumeric(12);
		 String testId = randomString1+ "_" +tcId;
	 		
		inputBean = new InputBean(tmplFileName, fileName, null, user, testId);
		
		Log(method, attributeBean, inputBean);
		
		String ioi_Code = "TOO_MANY_INVALID_LOGINS_GROUP";
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		attributeBean.setIoi_code(ioi_Code);
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		

		
		System.out.println("Test Details ###  " + inputBean.toString());


		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			Set<String> saasApps = new HashSet<>();
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + fileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		 		
		    	count++;
		    	Calendar cal = Calendar.getInstance();
		    	cal.add(Calendar.SECOND, idx);
		    	String strDateTime = formatDate.format(cal.getTime());
		    	String strYear = formatYear.format(cal.getTime());
		    	
		    	String sourceId = RandomStringUtils.randomAlphanumeric(22);
		    	
		    	
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        	((ObjectNode)activity).put("_id", sourceId);
		        
		        ((ObjectNode)_source).put("user_name", sourceId);
		        
		        ((ObjectNode)_source).put("user", sourceId+"@"+suiteData.getTenantDomainName());
		        
		        ((ObjectNode)_source).put("elastica_user", sourceId+"@"+suiteData.getTenantDomainName());
		        
		        
		        
		        ((ObjectNode)_source).put("test_id", testId);
		       
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        ((ObjectNode)_source).put("inserted_timestamp", strDateTime);
		        ((ObjectNode)activity).put("_index", "alias_logs_" + suiteData.getTenantName() + "-" + strYear);
		        
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	

		scpActivityLogsAndValidate(method, inputBean);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

		if(enabled){
			
			
		validateIncidents(ioi_Code);
			
//			Date dateTo = new Date();
//	    	 formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
//	    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
//	    	String strDateTimeTo = formatDate.format(dateTo);
//	    	
//			String[] users = {"sagar2@elastica.co","sagar3@elastica.co","sagar4@elastica.co","sagar5@elastica.co","sagar6@elastica.co",
//					"sagar7@elastica.co","sagar8@elastica.co","sagar9@elastica.co","sagar10@elastica.co","sagar11@elastica.co","sagar12@elastica.co"};
//			JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
//			Boolean foundUser = false;
//			for (String user : users) {
//				foundUser = false;
//				for (int i = 0; i < jArray.size(); i++) {
//					String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
//					String rUser = getJSONValue(sourceJson, "user");
//					String cTime = getJSONValue(sourceJson, "created_timestamp");
//					if (rUser.equalsIgnoreCase("\"" + user + "\"") || cTime.startsWith(strDateTimeTo)) {
//						foundUser = true;
//						break;
//					}
//				}
//				if (!foundUser) {
//					break;
//				}
//			}
//			Assert.assertTrue(foundUser, "One or More Users Information is NOT listed Under DETECT");
			
			System.out.println("Execution Completed - Test Case Name: " + method.getName());}else{
			verifyNoIncidents(inputBean.getUser());
			Reporter.log("");
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
			Reporter.log("");
		}
	}

	/*@Test(dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, description = "This test operates on elastic search injected data, and generates upload/download incidents.")
	public void upload_AND_LargeTotal_Trafic_Test(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		Log(method, attributeBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String[] ioi_Codes = {IOI_Code.TOO_MANY_SUM_LARGE_TOTAL_TRAFFIC.toString(), IOI_Code.TOO_MANY_SUM_LARGE_UPLOADS.toString()};
		boolean enabled = attributeBean.isEnabled();
		for (String ioi_Code: ioi_Codes) {
			updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_Code);
		}
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		InputBean inputBean =	createFileUpdateData1("test_u_01", attributeBean);
		
		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		Thread.sleep(30000);
		HashSet<String> expIncidents = new HashSet<String>();
		Set<String> ioi_codes = new HashSet<>();
		if(enabled){
			for (String ioi_Code : ioi_Codes) {
				expIncidents.add(ioi_Code);
				ioi_codes.add(ioi_Code);
			}
			
			responseBody =	verifyStateIncidents(user, expIncidents);
			 
			 
			validateIncidents(responseBody, attributeBean, inputBean,ioi_codes );

			verifyThreatscoreIncidets(user, expIncidents);
		}else{
			verifyNoIncidents(user);
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	

	}
	
	@Test(dataProvider = "dataProvider",dataProviderClass=DetectDataProviders.class, description = "This test operates on elastic search injected data, and generates upload/download incidents.")
	public void download_AND_LargeTotal_Trafic_Test(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		Log(method, attributeBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String[] ioi_Codes = { IOI_Code.TOO_MANY_SUM_LARGE_DOWNLOADS.toString(), IOI_Code.TOO_MANY_SUM_LARGE_TOTAL_TRAFFIC.toString()};
		boolean enabled = attributeBean.isEnabled();
		for (String ioi_Code: ioi_Codes) {
			DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
			updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_Code);
		}
		InputBean inputBean =	createFileUpdateData1("test_D_01", attributeBean);
		
		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		Thread.sleep(30000);
		HashSet<String> expIncidents = new HashSet<String>();
		 Set<String> ioi_codes = new HashSet<>();
		if(enabled){
			for (String ioi_Code : ioi_Codes) {
				expIncidents.add(ioi_Code);
				ioi_codes.add(ioi_Code);
			}
			
			responseBody =	verifyStateIncidents(user, expIncidents);
			
			 
			validateIncidents(responseBody, attributeBean, inputBean,ioi_codes );

			verifyThreatscoreIncidets(user, expIncidents);
		}else{
			verifyNoIncidents(user);
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	

	}*/
	
	
	
	/*
	@Test(dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, description = "This test operates on elastic search injected data, and generates upload/download incidents.")
	public void Download_Upload_LargeTotal_Trafic_Test(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		Reporter.log("Execution Started - Test Case Name::::"+method.getName(), true);
		
		String[] ioi_Codes = { IOI_Code.TOO_MANY_SUM_LARGE_DOWNLOADS.toString(), IOI_Code.TOO_MANY_SUM_LARGE_TOTAL_TRAFFIC.toString(), IOI_Code.TOO_MANY_SUM_LARGE_UPLOADS.toString() };
		boolean enabled = attributeBean.isEnabled();
		for (String ioi_Code: ioi_Codes) {
			DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
			updateDetectAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		}
		InputBean inputBean =	createFileUpdateData1("test037",attributeBean);
		
		String user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		Thread.sleep(30000);
		HashSet<String> expIncidents = new HashSet<String>();
		if(enabled){
			for (String ioi_Code : ioi_Codes) {
				expIncidents.add(ioi_Code);
			}
			
			verifyStateIncidents(user, expIncidents);
			 Set<String> ioi_codes = new HashSet<>();
			 ioi_codes.add(ioi_Code);
			validateIncidents(responseBody, attributeBean, inputBean,ioi_codes );

			verifyThreatscoreIncidets(user, expIncidents);
		}else{
			verifyNoIncidents(user);
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	

	}
	
	@Test(dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, description = "This test operates on elastic search injected data, and generates upload/download incidents.")
	public void Download_Upload_Test(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		Reporter.log("Execution Started - Test Case Name::::"+method.getName(), true);
		
		String[] ioi_Codes = {IOI_Code.TOO_MANY_SUM_LARGE_UPLOADS.toString(), IOI_Code.TOO_MANY_SUM_LARGE_DOWNLOADS.toString() };
		boolean enabled = attributeBean.isEnabled();
		for (String ioi_Code: ioi_Codes) {
			DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
			updateDetectAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		}
		InputBean inputBean =	createFileUpdateData1("test037", attributeBean);
		
		String user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		Thread.sleep(30000);
		HashSet<String> expIncidents = new HashSet<String>();
		if(enabled){
			for (String ioi_Code : ioi_Codes) {
				expIncidents.add(ioi_Code);
			}
			
			verifyStateIncidents(user, expIncidents);
			 Set<String> ioi_codes = new HashSet<>();
			 ioi_codes.add(ioi_Code);
			validateIncidents(responseBody, attributeBean, inputBean,ioi_codes );

			verifyThreatscoreIncidets(user, expIncidents);
		}else{
			verifyNoIncidents(user);
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	

	}*/
	

	

	/*@Test(dataProvider = "dataProvider", dataProviderClass=DetectDataProviders.class, description = "This test operates on elastic search injected data, and generates upload/download incidents.")
	public void uploadDownLoadLimit_Test(Method method, AttributeBean attributeBean, String fileName) throws Exception {
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log("Detect Attributes::::::::   " + attributeBean.toString(), true);
		String ioi_Code = "TOO_MANY_SUM_LARGE_TOTAL_TRAFFIC";
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		boolean enabled = attributeBean.isEnabled();
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		InputBean inputBean =	createFileUpdateData1("test_u_01", attributeBean);
		
		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		Thread.sleep(30000);
		HashSet<String> expIncidents = new HashSet<String>();
		if(enabled){
			expIncidents.add(ioi_Code);
			responseBody =	verifyStateIncidents(user, expIncidents);
			 Set<String> ioi_codes = new HashSet<>();
			 ioi_codes.add(ioi_Code);
			validateIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			
		verifyThreatscoreIncidets(user, expIncidents);
		}else{
			verifyNoIncidents(user);
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	

	}*/
	
	
	
	@AfterMethod
	public void tearDown(ITestResult results) throws Exception {
		Reporter.log("         :::::::::::::::::::::::::::::::::::::::::::::::::::::",true);
		Reporter.log("",true);
		String result ="FAILED";
		if(results.isSuccess()){
			result = "PASSED";
		}
		
		Reporter.log("TestCase Name::::::: "+results.getName()+"   Test result::::::: "+result,true);
		if(!results.isSuccess()){
		Reporter.log("Please check  Assertion exception for  failures");
		}
		Reporter.log("",true);
		Reporter.log("         :::::::::::::::::::::::::::::::::::::::::::::::::::::",true);
		
		Reporter.log("Cleaning up Resources",true);
		
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
			Reporter.log("No Cleanup Required - I am not cleaning up data to debug if needed!",true);
		}
		Reporter.log("         :::::::::::::::::::::::::::::::::::::::::::::::::::::",true);
		Reporter.log("",true);
		Reporter.log("Cleaning up Resources - COMPLETED",true);
		Reporter.log("",true);
		Reporter.log("         :::::::::::::::::::::::::::::::::::::::::::::::::::::",true);
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
