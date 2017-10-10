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
import com.google.api.services.drive.model.Permission;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import com.universal.common.GDrive;
import com.universal.common.GoogleMailServices;
import com.universal.common.UniversalApi;
import com.universal.constants.CommonConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.GroupInput;
import com.universal.dtos.box.SharedLink;

public class DetectOnestepSequence_Gmail_i18N_API extends  DetectUtils {
private static final String TOO_MANY_SEQUENCE = "TOO_MANY_SEQUENCE_";
private static String UserRecieved ;
	
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	public static final String DETECT_FILE_UPLOAD_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload";
	public static final String DETECT_FILE_UPLOAD_PATH_TEMP = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload"+File.separator+"temp";

	private static List<String> to ;
	ArrayList<String> externalRecipients;
	ArrayList<String> groupRecipients;
	ArrayList<String> ExternalGroupRecipients;
	
	protected UniversalApi universalApi;
	protected UniversalApi universalGraphApi;
	
	protected UserAccount saasAppUserAccount;
	protected UserAccount saasAppGraphUserAccount;
	GoogleMailServices gmailApi;
	String userName;
	String clientId;
	String clientSecret;
	String refreshToken;
	
	//DetectUtils utils = new DetectUtils();
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	
	
	@BeforeClass(alwaysRun=true)
	public void initGmail() throws Exception {
		UserRecieved = suiteData.getSaasAppEndUser1Name();
		 to = new ArrayList<String>();
		to.add(UserRecieved);
		to.add(suiteData.getUsername());
		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add(UserRecieved);

		 externalRecipients = new ArrayList<String>();
		externalRecipients.add("v.sagar576@gmail.com");
		 groupRecipients = new ArrayList<String>();
		
		this.userName = suiteData.getSaasAppUsername();
		this.clientId 		= suiteData.getSaasAppClientId();	// getRegressionSpecificSuitParameters("saasAppClientId");
		this.clientSecret 	= suiteData.getSaasAppClientSecret();	// getRegressionSpecificSuitParameters("saasAppClientSecret");
		this.refreshToken 	= suiteData.getSaasAppToken();	// getRegressionSpecificSuitParameters("saasAppToken");
		Logger.info(clientId);
		Logger.info(clientSecret);
		Logger.info(refreshToken);
		Logger.info("all printed above..");
		this.gmailApi 		= new GoogleMailServices(clientId, clientSecret, refreshToken);
		Logger.info("Gmail api initialized");
	}
	
	@BeforeClass
	public void deleteSequenceDetectors() throws Exception{
		//clearIncidents();
		try {
		//	dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
/*	@DataProvider
	public static Object[][] sendPlainMailI18()
	{	
		
		return new Object[][] { 
			//Key,
			{ "Hindi",  "रामपाल यादव", "रामपाल यादव"},

			{ "Urdu",  "پاکستان - ‎کھیل - ‎ورلڈ - ‎آس پاس","پاکستان - ‎کھیل - ‎ورلڈ - ‎آس پاس"},

			{ "Chinese",  "蕡蕇蕱 蛃袚觙 谾踘遳 嫷 岈岋", "蕡蕇蕱 蛃袚觙 谾踘遳 嫷 岈岋"},
			
			{ "Japanese",  "みỦ馜碜餯 だぎゃ期詪䄦", "みỦ馜碜餯 だぎゃ期詪䄦"},
			
			{ "French",  "L'éducation doit être gratuite","L'éducation doit être gratuite" },
			{ "German",  "AUF DER FLUCHT", "AUF DER FLUCHT"},
			{ "Mexican",  "Fiestas Patrias de Chile", "Fiestas Patrias de Chile"},
			{ "russian",  "Я живу2 в Москве", "Я живу2 в Москве"},
			{ "Portuguese",  "é fácil de introduzir", "é fácil de introduzir"},
		};
	}
	*/
	
	@DataProvider
	public Object[][] AddEditDeleteDataProvider_Chinese() throws JAXBException, Exception  {
		
		
		String[] activitiesCreate = new String[]{"Create"};
		
		String[] failities = new String[]{"Gmail"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] userrecieved = new String[]{UserRecieved} ;
		String[] objects = new String[]{"__any"} ;
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Email_Message"} ;
		
		 
		  
			 
		SDInput  sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE, 2,400, false   ,false, false,
					  activitiesCreate, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
				
				 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_TWO, 2,600, true   ,true, true,
						 activitiesCreate, failities1, sources1, users1, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_THREE, 1,400, false   ,false, false,
							activitiesCreate, failities, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_FOUR, 1,400, true  ,true, true,
							activitiesCreate, failities1, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					 
			 Logger.info("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 3; j++) {
// "Chinese",  "蕡蕇蕱 蛃袚觙 谾踘遳 嫷 岈岋", "蕡蕇蕱 蛃袚觙 谾踘遳 嫷 岈岋"
					String uniqueId = new String (UUID.randomUUID().toString());
					String subject = new String("蕡蕇蕱 蛃袚觙 谾踘遳 嫷 岈岋" + uniqueId);
					String messageBody = "蕡蕇蕱 蛃袚觙 谾踘遳 嫷 岈岋";
					Draft draft =gmailApi.createDraft(to, null, null, subject, messageBody);
					Thread.sleep(5*1*1000);
					
				}
				
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
				Thread.sleep(5*60*1000);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CREATE},
			{ SequenceDetectorConstants.CREATE_TWO},
			{ SequenceDetectorConstants.CREATE_THREE},
			{ SequenceDetectorConstants.CREATE_FOUR},
			
				};
	}
	
	@DataProvider
	public Object[][] AddEditDeleteDataProvider_Urdu() throws JAXBException, Exception  {
		
		
		String[] activitiesCreate = new String[]{"Create"};
		
		String[] failities = new String[]{"Gmail"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] userrecieved = new String[]{UserRecieved} ;
		String[] objects = new String[]{"__any"} ;
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Email_Message"} ;
		
		 
			SDInput  sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE, 2,400, false   ,false, false,
						  activitiesCreate, failities, sources, users, objects);
			HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
					
					 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_TWO, 2,600, true   ,true, true,
							 activitiesCreate, failities1, sources1, users1, objects1);
						 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_THREE, 1,400, false   ,false, false,
								activitiesCreate, failities, sources1, users, objects1);
						 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_FOUR, 1,400, true  ,true, true,
								activitiesCreate, failities1, sources1, users, objects1);
						 response = dsd.createSequenceDetector(sdInput, suiteData);


			 Logger.info("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 3; j++) {
//"Urdu",  "پاکستان - ‎کھیل - ‎ورلڈ - ‎آس پاس","پاکستان - ‎کھیل - ‎ورلڈ - ‎آس پاس"
					String uniqueId = new String (UUID.randomUUID().toString());
					String subject = new String("پاکستان - ‎کھیل - ‎ورلڈ - ‎آس پاس" + uniqueId);
					String messageBody = "پاکستان - ‎کھیل - ‎ورلڈ - ‎آس پاس";
					Draft draft =gmailApi.createDraft(to, null, null, subject, messageBody);
					Thread.sleep(5*1*1000);
				}
				
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
				Thread.sleep(5*60*1000);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CREATE},
			{ SequenceDetectorConstants.CREATE_TWO},
			{ SequenceDetectorConstants.CREATE_THREE},
			{ SequenceDetectorConstants.CREATE_FOUR},
			
				};
	}
	
	@DataProvider
	public Object[][] AddEditDeleteDataProvider_Hindi() throws JAXBException, Exception  {
		
		
		String[] activitiesCreate = new String[]{"Create"};
		
		
		String[] failities = new String[]{"Gmail"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] userrecieved = new String[]{UserRecieved} ;
		String[] objects = new String[]{"__any"} ;
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Email_Message"} ;
		
		 
			SDInput  sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE, 2,400, false   ,false, false,
						  activitiesCreate, failities, sources, users, objects);
			HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
					
					 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_TWO, 2,600, true   ,true, true,
							 activitiesCreate, failities1, sources1, users1, objects1);
						 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_THREE, 1,400, false   ,false, false,
								activitiesCreate, failities, sources1, users, objects1);
						 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_FOUR, 1,400, true  ,true, true,
								activitiesCreate, failities1, sources1, users, objects1);
						 response = dsd.createSequenceDetector(sdInput, suiteData);


			 Logger.info("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 3; j++) {
//,  "", "रामपाल यादव"
					String uniqueId = new String (UUID.randomUUID().toString());
					String subject = new String("रामपाल यादव " + uniqueId);
					String messageBody = "रामपाल यादव";
					Draft draft =gmailApi.createDraft(to, null, null, subject, messageBody);
					Thread.sleep(5*1*1000);
				}
				
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
				Thread.sleep(5*60*1000);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CREATE},
			{ SequenceDetectorConstants.CREATE_TWO},
			{ SequenceDetectorConstants.CREATE_THREE},
			{ SequenceDetectorConstants.CREATE_FOUR},
			
				};
	}
	
	
	@Test(dataProvider="AddEditDeleteDataProvider_Chinese")
	public void addCreateDeleteemailSDTest_Chinese(Method method, String name) throws Exception{
		
		Logger.info("Execution Started - Test Case Name: " + method.getName());
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Logger.info("Execution Completed - Test Case Name: " + method.getName());
			
		}
	
	
	@Test(dataProvider="AddEditDeleteDataProvider_Urdu")
	public void addCreateDeleteemailSDTest_Urdu(Method method, String name) throws Exception{
		
		Logger.info("Execution Started - Test Case Name: " + method.getName());
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Logger.info("Execution Completed - Test Case Name: " + method.getName());
			
		}
	
	@Test(dataProvider="AddEditDeleteDataProvider_Hindi")
	public void addCreateDeleteemailSDTest_Hindi(Method method, String name) throws Exception{
		
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
		for (; ite<=10; ite++) {
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
						Thread.sleep(1 * 5 * 1000);	//wait for 1 minute.
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
		if(!found_ioi){
		Logger.info("Expected IOI_Code:::::: " + ioicode + " not found in the incident list");
		}else {
			Logger.info("Expected IOI_Code:::::: " + ioicode + "  found in the incident list");
		}
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
			for (int i = 1; i<=20; i++) {
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
				
			if(messageList.size()!=0){
				Logger.info(" Expected::: "+messageCount+ " Actual:::::: "+count);
				Logger.info("Saas App activities are not equal with the investigate logs, Retrying :::::"+i+" times wait time between each retry is:::: 1 min");
				Thread.sleep(1*60*1000);
				continue;
			}else if(messageList.size()==0){
				Logger.info(" Expected::: "+messageCount+ " Actual:::::: "+count);
				break;
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
