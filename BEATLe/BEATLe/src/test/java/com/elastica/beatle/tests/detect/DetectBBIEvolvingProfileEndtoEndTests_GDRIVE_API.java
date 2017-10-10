package com.elastica.beatle.tests.detect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.google.api.services.drive.model.Permission;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

public class DetectBBIEvolvingProfileEndtoEndTests_GDRIVE_API extends DetectUtils{

	private static final String OBJECTS = "detect_attributes";
	private static final String INSERT = "insert";
	private static final String TOO_MANY_SEQUENCE = "TOO_MANY_SEQUENCE_";
	
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
	String[] ioi_Codes  = {IOI_Code.ANOMALOUSLY_FREQUENT_DELETES.toString(), IOI_Code.ANOMALOUSLY_FREQUENT_SESSIONS.toString(), IOI_Code.ANOMALOUSLY_FREQUENT_SHARING.toString(),
			IOI_Code.ANOMALOUSLY_LARGE_DELETES.toString(), IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString(), IOI_Code.ANOMALOUSLY_LARGE_SHARING.toString(), IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString()
			,IOI_Code.ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC.toString()};
	
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
	    	String attr_set = null;
	    	if(null!=getJSONValue(objNode.toString(), "ats")){
	    	 attr_set = getJSONValue(objNode.toString(), "ats").toString().replace("\"", "");
	    	}
	    	String cdrs = getJSONValue(objNode.toString(), "cdrs").toString().replace("\"", "");
	    	String Activity_type = getJSONValue(objNode.toString(), "at").toString().replace("\"", "");
	    	String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
	    	String ObjectType = getJSONValue(objNode.toString(), "ot").toString().replace("\"", "");
	    	
	    	updateLog(report_id, severity, ObjectType, email, Activity_type, index, facilty,cdrs, source, attr_set);
	    	
	       }
	    }
	
	
		attributeBean =  new  AttributeBean(60, 2,true);
		attributeBean.setEnabled(true);
		
		
		HttpResponse resp = getDetectAttributes();
		 responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		
		boolean enabled = attributeBean.isEnabled();
		updateBBIAttributes1(enabled, attributeBean, getResponseArray, ioi_Codes);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		
		try {
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), "DETECT_BE_AUTOMATION"+uniqueId);
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
		
		
	}
	
	@BeforeClass()
	public void clearIncident() throws Exception {
		
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
	    	String ioi_code = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
	    	
	    	
	    	updateLog(report_id, severity, ObjectType, email, email, Activity_type, index, facilty, source, attr_set, ioi_code);
	    	
	       }
	    }
	
	}
	
	//TODO Verify profile is there or not, If not there create profile, validate incidents.
	
	@Test()    
	public void googleDrive_large_upload_BBI_Test(Method method) throws Exception{
		try {
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			String ioiCode = IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString();
			
			
			clearIncidentAndProfile(suiteData.getSaasAppUsername(), ioiCode);
			Assert.assertFalse(isIncidentListed(suiteData.getSaasAppUsername(), ioiCode), "Incident CleanUP failed!!");
			//verifyNoProfile(senderEmail, ioiCode);
			
			Reporter.log("Going to Send Mails - To Create the Profile.", true);
			for( int idx = 1; idx <= 33; idx++ ) {
				
				
			}
			
			verifyProfile(suiteData.getSaasAppUsername(), ioiCode);
			
			Reporter.log("Going to Send Mails - To Create the Incident.", true);
			for( int idx = 1; idx <= 30; idx++ ) {
				
				Thread.sleep(1 * 1 * 1000);
			}
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioiCode);
			verifyStateIncidents(suiteData.getSaasAppUsername(), expIncidents);
			
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		} catch (Exception e) {
			throw e;
		}finally {
			Reporter.log("       #####################################");
			Reporter.log(" ");
			Reporter.log("Execution Completed - Test Case Name::::: " + method.getName(), true);
			Reporter.log(" ");
			Reporter.log("       #####################################");
			
			
		}
		
	}
	
	
	
	
//	@Test
//	public void googleDrive_lagre_delete_BBI_Test(Method method) throws Exception{
//		try {
//			
//			
//			validateIncidents(IOI_Code.ANOMALOUSLY_LARGE_DELETES.toString());
//			
//				//Thread.sleep(60000);
//			 verifyDetectedUser(suiteData.getSaasAppUsername());
//			
//			
//			
//			
//		} catch (Exception e) {
//			throw e;
//		}finally {
//			
//		}
//		
//	}
	
	

	
	

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
		for (; ite<=10; ite++) {
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
	
	public void verifyActivityCount(String query, List<String> messageList) throws Exception {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, -3);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response = null;
		try {
			int messageCount = messageList.size();
			int count = 0;
		
			for (int i = 1; i<=10; i++) {
				String payload = esQueryBuilder.getSearchQueryForDisplayLogs(strDateTimeFrom + ":00:00.000Z",
						strDateTimeTo + ":59:59.999Z", query, "Elastica", 1000, apiServer, suiteData.getCSRFToken(), suiteData.getSessionID(),
						suiteData.getUsername());
				Reporter.log("getting investigate logs ::::::  payload::::   " + payload, true);
				
				response = esLogs.getDisplayLogs(restClient, buildBasicHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()),suiteData.getApiserverHostName(),
						new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				String responseBody = getResponseBody(response);
				Reporter.log("responseBody:::::::::::::: "+responseBody, true);
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
				
				if (arrNode.isArray()) {
					if(arrNode.size()!=0){
				    for (final JsonNode objNode : arrNode) {
				    	 JsonNode _source = objNode.path("_source");
				    	JsonNode message = _source.path("message");
				    	if(messageList.contains(message.asText())){
				    		Reporter.log("Activity log message on invetsigate page::::::::::  "+message, true);
				    		messageList.remove(message.asText());
				    		count++;
				    	}
				    	
				    	}
					}
				}
				
			if(messageList.size()!=0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				Reporter.log("Saas App activities are not equal with the investigate logs, Retrying :::::"+i+" times wait time between each retry is:::: 1 min", true);
				Thread.sleep(1*60*1000);
				continue;
			}else if(messageList.size()==0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				break;
				}
			}
			if(messageCount!=count){
			Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
			throw new SkipException("Number of Activities returned are not Equal");
			}
			
		}  catch (Exception e) {
			throw e;
		}
	}
	
	private void Log(Method method, String name, String user, String userName) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log("This Test is to validate " + method.getName().replace("_Tests", "")+",   And preferences for this test are  preference enabled???:::::: "+attributeBean.isEnabled() +" confidence:: "
				+ ""+attributeBean.getConfidence()+" importance:::: "+attributeBean.getImportance(), true);
		Reporter.log(" ");
		Reporter.log("Test details :::::::  userName::  " + userName+"  user::::: "+user, true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and pass word for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}
	
	
	/*//			if(suiteData.getEnvironmentName().equalsIgnoreCase("eoe")){
//	boolean profileCreated = 	verifyProfile(suiteData.getSaasAppUsername(), suiteData.getSaasApp() ,IOI_code);
//		
//	if(!profileCreated){
//		List<String> messageList =	createProfile(suiteData.getSaasAppUsername(), IOI_code, attributeBean);
//		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
//	}
//	
//}else{
//	List<String> messageList =	createProfile(suiteData.getSaasAppUsername(), IOI_code, attributeBean);
//	
//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
//}
*/

	
}
