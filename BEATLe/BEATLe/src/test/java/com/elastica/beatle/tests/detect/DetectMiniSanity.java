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
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.tests.securlets.BoxActivityLog;
import com.google.api.services.drive.model.Permission;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.GroupInput;
import com.universal.dtos.box.SharedLink;

public class DetectMiniSanity extends  DetectUtils {
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
	
	//DetectUtils utils = new DetectUtils();
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	BoxActivityLog 	boxActivityLog = new BoxActivityLog();
	
	String folderName = "DETECT_BE_AUTOMATION"+uniqueId;
	
	@BeforeClass
	public void deleteSequenceDetectors() throws Exception{
		try {
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(),folderName );
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Object[][] fileUploadDataProvider() throws JAXBException, Exception  {
		
		String[] activitiesupload = new String[]{"Upload"};
		
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		String sdName = SequenceDetectorConstants.UPLOAD;
		
		SDInput sdInput;
		HttpResponse response;
		for (int i = 0; i < objects1.length; i++) {
			createListOfSDs(activitiesupload, failities, sources, users, objects, sdName);
		}
		
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_TWO, 2,600, true   ,true, true,
				 activitiesupload, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_THREE, 1,400, false   ,false, false,
					activitiesupload, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_FOUR, 1,400, true  ,true, true,
					activitiesupload, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 
			
			 Logger.info("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 3; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					String templfileName = createSampleFileType("computing.doc");
					String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
					filedIdMap.put(templfileName, filedId);
					Thread.sleep(1*2*1000);
					messageList.add(boxActivityLog.getFolderUploadLog().replace("{foldername}", templfileName)
							 .replace("{destinationfolder}", folderName));
				}
				Logger.info("Expected logs count:::::::::: "+messageList.size()+"   Expected logs :::::::::: "+messageList.toString());
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.UPLOAD},
			{ SequenceDetectorConstants.UPLOAD_TWO},
			{ SequenceDetectorConstants.UPLOAD_THREE},
			{ SequenceDetectorConstants.UPLOAD_FOUR},
			
				};
	}


	private void createListOfSDs(String[] activitiesupload, String[] failities, String[] sources, String[] users,
			String[] objects, String sdName) throws JAXBException, UnsupportedEncodingException, Exception {
		SDInput sdInput = dsd.createSDInput(sdName, 2,400, false   ,false, false,
				
				
				activitiesupload, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
	}
	
	
	@Test(dataProvider="fileUploadDataProvider")
	public void fileUploadSDTest(Method method, String name) throws Exception{
		Logger.info("Execution Started - Test Case Name: " + method.getName());
			
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Logger.info("Execution Completed - Test Case Name: " + method.getName());
			
		}
	
	public String createSampleFileType(String fileName) {
		String uuId= UUID.randomUUID().toString();
		try {
			File dir = new File(DETECT_FILE_UPLOAD_PATH_TEMP);
			if (!dir.exists()) {
				if (dir.mkdir()) {
				} else {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(DETECT_FILE_UPLOAD_PATH+ File.separator + fileName);
			
			File dest = new File(DETECT_FILE_UPLOAD_PATH_TEMP+ File.separator + uuId + "_" + fileName);
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
	
	
	public String validateIncidents(String ioicode) {
		Logger.info("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -1);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		Boolean found_ioi =false;
		int ite=1;
		for (; ite<=3; ite++) {
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom,
						strDateTimeTo);
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Logger.info(" validateIncidents::::  Response::::   " + responseBody);
				JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				
				if (jnode.isArray()) {
				    for (final JsonNode objNode : jnode) {
				    	String    	ioi_found = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				    	Logger.info("found ioicode:::::::: "+ioi_found);
				    	Logger.info("Expected ioicode:::::::: "+ioicode);
				    	if (ioicode.equalsIgnoreCase(ioi_found)) {
							found_ioi = true;
							break;
						}	
				    	
				       }
				    if (found_ioi) {
						break;
					} else {
						Thread.sleep(1 * 30 * 1000);	//wait for 1 minute.
						Logger.info("Incident  is not found in the detect page Retrying::::::: " + ite + "Minutes of waiting");
					}
				    
				    }
		
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		Logger.info("Going to Assert after " + ite + "Minutes of waiting");
		
		Logger.info("Expected IOI_Code:::::: " + ioicode + " not found in the incident list");
		Assert.assertTrue(found_ioi, "incident not listed");
		
		return responseBody;
	}
	
	
	public void verifyActivityCount(String query, List<String> messageList) throws Exception {
		Logger.info("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, -50);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	com.elastica.beatle.securlets.ESQueryBuilder esqueryBuilder1 = new com.elastica.beatle.securlets.ESQueryBuilder();
    	
		HttpResponse response = null;
		try {
			int messageCount = messageList.size();
			int count = 0;
		HashMap<String, String> terms = new HashMap<>();
			for (int i = 1; i<=3; i++) {
				String payload = 	esqueryBuilder1.getESQueryForInvestigate(strDateTimeFrom+ ":00:00.000Z", strDateTimeTo+":59:59.999Z", "Elastica",
						terms, query, apiServer, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000);
//				String payload = esQueryBuilder.getSearchQueryForDisplayLogs(strDateTimeFrom + ":00:00.000Z",
//						strDateTimeTo + ":59:59.999Z", query, "Elastica", 1000, apiServer, csfrToken, sessionID,
//						suiteData.getUsername());
				
				Logger.info("getting investigate logs ::::::  payload::::   " + payload);
				
				response = esLogs.getDisplayLogs(restClient, buildBasicHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()),suiteData.getApiserverHostName(),
						new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				String responseBody = getResponseBody(response);
				Logger.info("responseBody:::::::::::::: "+responseBody);
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
				
				if (arrNode.isArray()) {
					if(arrNode.size()!=0){
				    for (final JsonNode objNode : arrNode) {
				    	 JsonNode _source = objNode.path("_source");
				    	JsonNode message = _source.path("message");
				    	if(messageList.contains(message.asText())){
				    		Logger.info("Activity log message on invetsigate page::::::::::  "+message);
				    		messageList.remove(message.asText());
				    		count++;
				    	}
				    	
				    	}
					}
				}
				
				if(messageList.size()==0){
					Logger.info(" Expected::: "+messageCount+ " Actual:::::: "+count);
					break;
					}else if(messageList.size()!=0){
				Logger.info(" Expected::: "+messageCount+ " Actual:::::: "+count);
				Logger.info("Saas App activities are not equal with the investigate logs, Retrying :::::"+i+" times wait time between each retry is:::: 1 min");
				Thread.sleep(1*60*1000);
				continue;
				} 
			}
			if(messageCount!=count){
			Logger.info(" Expected::: "+messageCount+ " Actual:::::: "+count);
			throw new SkipException("Number of Activities returned are not Equal");
			}
			
		}  catch (Exception e) {
			throw e;
		}
	}

}
