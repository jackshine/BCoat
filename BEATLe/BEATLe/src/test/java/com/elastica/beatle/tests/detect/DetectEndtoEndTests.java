package com.elastica.beatle.tests.detect;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

public class DetectEndtoEndTests extends  DetectUtils  {
	
	private static final String OBJECTS = "detect_attributes";
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	public static final String DETECT_FILE_UPLOAD_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload";
	public static final String DETECT_FILE_UPLOAD_PATH_TEMP = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload"+File.separator+"temp";
	protected UniversalApi universalApi;
	protected UniversalApi universalGraphApi;
	
	protected UserAccount saasAppUserAccount;
	protected UserAccount saasAppGraphUserAccount;
	AttributeBean attributeBean ;
	
	//DetectUtils utils = new DetectUtils();
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	
	@BeforeClass()
	public void beforeClass() throws Exception {
		
		clearIncidents();
	
		try {
			dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	 attributeBean = new AttributeBean(1, 5, 2, null);
	attributeBean.setEnabled(true);
	
	
	HttpResponse resp = getDetectAttributes();
	String  responseBody = getResponseBody(resp);
	org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
	String ioi_Code = "TOO_MANY_SUM_LARGE_UPLOADS";
	boolean enabled = attributeBean.isEnabled();
	updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_Code);
	Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
	
	
	 attributeBean = new AttributeBean(2, 5, 3, null);
	attributeBean.setEnabled(true);
	
	
	 ioi_Code = IOI_Code.TOO_MANY_ENCRYPTED_FILES.toString();
	 enabled = attributeBean.isEnabled();
	updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_Code);
	Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
	
	try {
		UserAccount account = detectFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
		
		folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), "DETECT_BE_AUTOMATION"+uniqueId);
	} catch (Exception ex) {
		Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
	}
	
	
	}
	
	
	
	
	@Test(priority=1)    
	public void googleDrive_upload_TBI_Test(Method method) throws Exception{
		
		try {
			final String[] FILE_NAME = {"1mb001.bin","1mb002.bin","1mb003.bin","1mb004.bin", "1mb005.bin"};
			//Set preferences 
			
			Log(method, attributeBean);
			
			
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < FILE_NAME.length; j++) {
				String templfileName = createSampleFileType(FILE_NAME[j] );
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				filedIdMap.put(templfileName, filedId);
				messageList.add("User uploaded file "+templfileName);
				Thread.sleep(1*2*1000);
			}
			
			
			verifyActivityCount(suiteData.getUsername(), messageList);
			
			validateIncidents(IOI_Code.TOO_MANY_SUM_LARGE_UPLOADS.toString());
			
				//Thread.sleep(60000);
			 verifyDetectedUser(suiteData.getSaasAppUsername());
			
			 
			 Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Test(priority=1)
	public void googleDrive_Encyption_TBI_Test(Method method) throws Exception{
		
		try {
			final String[] FILE_NAME = {"Encrypted001.doc","Encrypted002.pptx","Encrypted003.ppt","Encrypted004.docx"};
			//Set preferences 
			Log(method, attributeBean);
					
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < FILE_NAME.length; j++) {
				String templfileName = createSampleFileType(FILE_NAME[j] );
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				filedIdMap.put(templfileName, filedId);
				messageList.add("User uploaded file "+templfileName);
				Thread.sleep(1*2*1000);
			}
			
			verifyActivityCount(suiteData.getUsername(), messageList);
			Thread.sleep(3*60*1000);
			validateIncidents(IOI_Code.TOO_MANY_ENCRYPTED_FILES.toString());
			
				//Thread.sleep(60000);
			verifyDetectedUser(suiteData.getSaasAppUsername());
			
			 
			 Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		} catch (Exception e) {
			throw e;
		}
	}

	
	@Test()
	public void dropBox_upload_TBI_Test(Method method) throws Exception{
		
		try {
			final String[] FILE_NAME = {"1mb001.bin","1mb002.bin","1mb003.bin","1mb004.bin", "1mb005.bin","1mb001.bin","1mb002.bin","1mb003.bin","1mb004.bin", "1mb005.bin"};
			//Set preferences 
			Log(method, attributeBean);
			
			
		

			String folderName ="DETECT_BE_AUTOMATION"+uniqueId;
			
			

			try {
				UserAccount account = detectFunctions.getUserAccount(suiteData);
				UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
				
				folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), folderName);
			} catch (Exception ex) {
				Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
			}
			
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < FILE_NAME.length; j++) {
				String templfileName = createSampleFileType(FILE_NAME[j] );
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				//messageList.add("User added file /"+folderName+"/"+templfileName);
				filedIdMap.put(templfileName, filedId);
				Thread.sleep(1*10*1000);
			}
			
			verifyActivityCount(suiteData.getUsername(), messageList);
			Thread.sleep(3*60*1000);
			validateIncidents(IOI_Code.TOO_MANY_SUM_LARGE_UPLOADS.toString());
			
			//Thread.sleep(60000);
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			 
			 Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
			 
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Test()
	public void dropBox_Encyption_TBI_Test(Method method) throws Exception{
		
		try {
			final String[] FILE_NAME = {"Encrypted001.doc","Encrypted002.pptx","Encrypted003.ppt","Encrypted004.docx"};
			//Set preferences 
			Log(method, attributeBean);
			
			
			String folderName ="DETECT_BE_AUTOMATION"+uniqueId;
			
			

			try {
				UserAccount account = detectFunctions.getUserAccount(suiteData);
				UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
				
				folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), folderName);
			} catch (Exception ex) {
				Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
			}
			
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < FILE_NAME.length; j++) {
				String templfileName = createSampleFileType(FILE_NAME[j] );
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				messageList.add("User added file /"+folderName+"/"+templfileName);
				filedIdMap.put(templfileName, filedId);
				Thread.sleep(1*10*1000);
			}
			
			verifyActivityCount(suiteData.getUsername(), messageList);
			validateIncidents(IOI_Code.TOO_MANY_ENCRYPTED_FILES.toString());
			
			//Thread.sleep(60000);
			// verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			 
			 Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
			 
		} catch (Exception e) {
			throw e;
		}
	}


	
	@Test()
	public void box_upload_TBI_Test(Method method) throws Exception{
		
		try {
			final String[] FILE_NAME = {"1mb001.bin","1mb002.bin","1mb003.bin","1mb004.bin", "1mb005.bin","1mb001.bin","1mb002.bin","1mb003.bin","1mb004.bin", "1mb005.bin"};
			//Set preferences 
			Log(method, attributeBean);
			
			
						
			String folderName ="DETECT_BE_AUTOMATION"+uniqueId;
			
			try {
				UserAccount account = detectFunctions.getUserAccount(suiteData);
				UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
				
				folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), folderName);
			} catch (Exception ex) {
				Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
			}
			
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < FILE_NAME.length; j++) {
				String templfileName = createSampleFileType(FILE_NAME[j] );
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				//User uploaded 1mb003.bin to DETECT_BE_AUTOMATION065e2ed1-c388-4613-a5a1-3aa092c505ee
				messageList.add("User uploaded "+templfileName+" to "+folderName);
				filedIdMap.put(templfileName, filedId);
				Thread.sleep(1*10*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			validateIncidents(IOI_Code.TOO_MANY_SUM_LARGE_UPLOADS.toString());
			
			//Thread.sleep(60000);
			// verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			 Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
			 
		} catch (Exception e) {
			throw e;
		}
	}

	
	@Test()
	public void box_Encyption_TBI_Test(Method method) throws Exception{
		
		try {
			final String[] FILE_NAME = {"Encrypted001.doc","Encrypted002.pptx","Encrypted003.ppt","Encrypted004.docx"};
			//Set preferences 
			Log(method, attributeBean);
			
						
			String folderName ="DETECT_BE_AUTOMATION"+uniqueId;
			
			try {
				UserAccount account = detectFunctions.getUserAccount(suiteData);
				UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
				
				folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), folderName);
			} catch (Exception ex) {
				Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
			}
			
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < FILE_NAME.length; j++) {
				String templfileName = createSampleFileType(FILE_NAME[j] );
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				//User uploaded 1mb003.bin to DETECT_BE_AUTOMATION065e2ed1-c388-4613-a5a1-3aa092c505ee
				messageList.add("User uploaded "+templfileName+" to "+folderName);
				filedIdMap.put(templfileName, filedId);
				Thread.sleep(1*10*1000);
			}
			
			verifyActivityCount(suiteData.getUsername(), messageList);
			
			validateIncidents(IOI_Code.TOO_MANY_ENCRYPTED_FILES.toString());
			
			//Thread.sleep(60000);
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			 Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
			 
		} catch (Exception e) {
			throw e;
		}
	}

	
	
	
	
	@Test()
	public void o365_upload_TBI_Test(Method method) throws Exception{
		
		try {
			final String[] FILE_NAME = {"1mb001.bin","1mb002.bin","1mb003.bin","1mb004.bin", "1mb005.bin",};
			//Set preferences 
			Log(method, attributeBean);
			
			
			String folderName ="DETECT_BE_AUTOMATION"+uniqueId;
			try {
				UserAccount account = detectFunctions.getUserAccount(suiteData);
				UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
				
				folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), folderName);
			} catch (Exception ex) {
				Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
			}
			
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < FILE_NAME.length; j++) {
				String templfileName = createSampleFileType(FILE_NAME[j] );
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				//User added '1mb004.bin'.
				messageList.add("User added '"+templfileName+"'.");
				filedIdMap.put(templfileName, filedId);
				Thread.sleep(1*10*1000);
			}
			//Thread.sleep(3*60*1000);
			verifyActivityCount(suiteData.getUsername(), messageList);
			
			validateIncidents(IOI_Code.TOO_MANY_SUM_LARGE_UPLOADS.toString());
			
			//Thread.sleep(60000);
			// verifyDetectedUser(suiteData.getSaasAppUsername());
			 Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
			 
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	@Test()
	public void oneDrive_upload_TBI_Test(Method method) throws Exception{
		
		try {
			final String[] FILE_NAME = {"1mb001.bin","1mb002.bin","1mb003.bin","1mb004.bin", "1mb005.bin"};
			//Set preferences 
			Log(method, attributeBean);
			
						
			try {
				UserAccount account = detectFunctions.getUserAccount(suiteData);
				UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
				
				folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), "DETECT_BE_AUTOMATION"+uniqueId);
			} catch (Exception ex) {
				Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
			}
			
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < FILE_NAME.length; j++) {
				String templfileName = createSampleFileType(FILE_NAME[j] );
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				//User added '1mb004.bin'.
				messageList.add("User added '"+templfileName+"'.");
				filedIdMap.put(templfileName, filedId);
				Thread.sleep(1*10*1000);
			}
			
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getUsername(), messageList);
			
			verifyActivityCount(suiteData.getUsername(), messageList);

			 //verifyDetectedUser(suiteData.getUsername());
			 Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
			 
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	@AfterClass
	public void deleteFolder() {
		try {
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			detectFunctions.deleteFolder(universalApi, suiteData.getSaasApp(), folderInfo);
			detectFunctions.cleanupTempFolder();
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}
	
	
	protected int verifyDetectedUser(String user) {
		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("source", "{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"facility\":\"_ALL\"}}]}},\"filter\":{\"and\":[{\"or\":[{\"term\":{\"__source\":\"\"}},{\"term\":{\"__source\":\"GW\"}},{\"term\":{\"__source\":\"API\"}},{\"missing\":{\"field\":\"__source\"}}]}]}}},\"sort\":{\"threat_score\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"size\":100000}"));
		queryParams.add(new BasicNameValuePair("sourceName", "DETECT"));		
		int rThreatScore = 0;
		Boolean foundUser = false;
		int ite=1;
		for (; ite<=10; ite++) {
			try {
				HttpResponse response = esLogs.getUserScoreAnomalies(restClient, this.buildCookieHeaders(),suiteData.getHost(), queryParams);
				String responseBody = getResponseBody(response);
				Reporter.log("Response Body Printing....... : " + responseBody, true);
				
				
				JsonNode hits = new ObjectMapper().readTree(getJSONValue(getJSONValue(responseBody, "hits"), "hits"));
				String rUser;
				
				
				if (hits.isArray()) {
				    for (final JsonNode objNode : hits) {
						
						JsonNode source = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "_source"));
					rUser = getJSONValue(source.toString(), "user");
					rThreatScore =  Integer.parseInt(getJSONValue(source.toString(), "threat_score"));
					System.out.println("Msg Printing....." + rUser + " " + rThreatScore);
					if (rUser.equalsIgnoreCase("\"" + user + "\"")) {
						foundUser = true;
						break;
					}
				}
				
				if (foundUser) {
					break;
				} else {
					Thread.sleep(1 * 60 * 1000);	//wait for 1 minute.
					Reporter.log("Retrying  for incident     " + ite + "Minutes of waiting", true);
				}
			} }catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Reporter.log("Going to Assert after " + ite + "Minutes of waiting",true);
		Assert.assertTrue(foundUser, "Users Information is not listed Under DETECT");
		return rThreatScore;
	}	
	
	
	public String validateIncidents() {
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
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseBody;
	}
	
	/**
	 * Generate sample file type from files and current date time
	 * @param fileName
	 * @param currentDateTime
	 * @return
	 */
	public String createSampleFileType(String fileName) {
		String uuId= UUID.randomUUID().toString();
		try {
			File dir = new File(DETECT_FILE_UPLOAD_PATH_TEMP);
			if (!dir.exists()) {
				if (dir.mkdir()) {
					// Logger.info("Temp Directory is created!");
				} else {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(DETECT_FILE_UPLOAD_PATH+ File.separator + fileName);
			
			File dest = new File(DETECT_FILE_UPLOAD_PATH_TEMP+ File.separator + uuId + "_" + fileName);
			// Logger.info(dest);
			if (!dest.exists()) {
				dest.createNewFile();
			}
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uuId + "_" + fileName;
	}

	
	private void Log(Method method, AttributeBean attributeBean) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log("This Test is to validate " + method.getName().replace("_Tests", "")+",   And preferences for this test are  preference enabled???:::::: "+attributeBean.isEnabled() +" window:: "
				+ ""+attributeBean.getWindow()+" event::: "+attributeBean.getThreshold()+" importance:::: "+attributeBean.getImportance(), true);
		Reporter.log(" ");
		Reporter.log("Test Details:::: saas app name::  "+suiteData.getSaasApp()+" :::username:::::  "+suiteData.getSaasAppUsername()+" :::password::::  "
				+ ""+suiteData.getSaasAppPassword()+" :::user role::::    "+suiteData.getSaasAppUserRole(), true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user name:: "+suiteData.getUsername()+" and password for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}
	
	

	
	
	public void validateIncidentList(String IOI_Code, String saasUser, String saasApp){
		SoftAssert asert = new SoftAssert();
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, -10);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom + ":00:00.000Z",
						strDateTimeTo + ":59:59.999Z");
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
					JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				 if (jnode.isArray()) {
					    for (final JsonNode objNode : jnode) {
					    	String ioi_Code = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
					    	String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
					    	String facilty = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
					    	if(ioi_Code.equalsIgnoreCase(ioi_Code) && email.equalsIgnoreCase(saasUser) && facilty.equalsIgnoreCase(saasApp)){
					    		
					    		
					    		
					    	}
					    	
					    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
					    	
					       }
					    }
				 
				
				 
				Reporter.log(" List of Incidents::::::  Response::::   " + responseBody, true);
				
				
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
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
    	cal.add(Calendar.MINUTE, -50);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		Boolean found_ioi =false;
		int ite=1;
		for (; ite<=15; ite++) {
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom,
						strDateTimeTo);
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Reporter.log(" validateIncidents::::  Response::::   " + responseBody, true);
				JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				
				if (jnode.isArray()) {
				    for (final JsonNode objNode : jnode) {
				    	String    	ioi_found = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				    	Reporter.log("found ioicode:::::::: "+ioi_found, true);
				    	Reporter.log("Expected ioicode:::::::: "+ioicode, true);
				    	if (ioicode.equalsIgnoreCase(ioi_found)) {
							found_ioi = true;
							break;
						}
				    	
				       }
				    if (found_ioi) {
						break;
					} else {
						Thread.sleep(1 * 60 * 1000);	//wait for 1 minute.
						Reporter.log("Incident  is not found in the detect page Retrying::::::: " + ite + "Minutes of waiting");
					}
				    
				    }
		
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		Reporter.log("Going to Assert after " + ite + "Minutes of waiting");
		
		Reporter.log("Expected IOI_Code:::::: " + ioicode + " not found in the incident list",true);
		Assert.assertTrue(found_ioi, "incident not listed");
		
		return responseBody;
	}

}
