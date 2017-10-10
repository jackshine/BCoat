package com.elastica.beatle.tests.detect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.codehaus.jackson.map.ObjectMapper;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.universal.dtos.UserAccount;

public class DetectMultiUsersSDTests_GW extends DetectUtils{
	
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
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	Map<String, String> filedIdMap = new HashMap<>();
	
	@BeforeClass()
	public void beforeClass()  {
		
	 clearIncidents();
		
	}

	
	@BeforeClass
	public void deleteallSDs(){
		try {
		dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@DataProvider
	public Object[][] CreateDataProvider_2users() throws UnsupportedEncodingException, JAXBException, Exception  {
		
		String[] activities = new String[]{"Create"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(),suiteData.getSaasAppEndUser1Name()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Account"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		sdInput.setMultiuser(true);
		sdInput.setUsers(users);
		
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int i = 0; i < users.length; i++) {
			
		for (int j = 0; j < 4; j++) {
			dcUtils.replayLogs("Account,Salesforce,create.log", suiteData, users[i]);
			
			Thread.sleep(1*5*1000);
			}
		}
		
		return new Object[][]{ {suiteData.getSaasAppUsername()},
			{suiteData.getSaasAppEndUser1Name()}
			
			};
	}
	
	@Test(dataProvider="CreateDataProvider_2users")
	public void multipleUsersSDTests_2users(Method method,String user) throws Exception{
			
			String SequenceName =	SequenceDetectorConstants.CREATE_ONE_SF;
			Log(method, SequenceName, user, user );
			String ioicode = TOO_MANY_SEQUENCE+SequenceName;
			validateIncidents(ioicode);
            verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		}
	
	@DataProvider
	public Object[][] ViewDataProvider_3users() throws UnsupportedEncodingException, JAXBException, Exception  {
		
		String[] activities = new String[]{"View"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(),suiteData.getSaasAppEndUser1Name(),suiteData.getSaasAppEndUser2Name()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Account"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		sdInput.setMultiuser(true);
		sdInput.setUsers(users);
		
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int i = 0; i < users.length; i++) {
			
		for (int j = 0; j < 4; j++) {
			dcUtils.replayLogs("Account,Salesforce,create.log", suiteData, users[i]);
			
			Thread.sleep(1*5*1000);
			}
		}
		
		return new Object[][]{ {suiteData.getSaasAppUsername()},
			{suiteData.getSaasAppEndUser1Name()},
			{suiteData.getSaasAppEndUser2Name()}
			};
	}
	
	@Test(dataProvider="ViewDataProvider_3users")
	public void multipleUsersSDTests_3users(Method method,String user) throws Exception{
			
			String SequenceName =	SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF;
			Log(method, SequenceName, user, user );
			String ioicode = TOO_MANY_SEQUENCE+SequenceName;
			validateIncidents(ioicode);
            verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		}
		
		
	
	

	private void Log(Method method, String name, String user, String userName) {
		Reporter.log("       ##################:::::::::::###################     ");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log(" This test  will create squence detector , inject activities and verify the incident::: name of squence detector:::"
				+ ": "+name+"  ::::user::::  "+user+"  :::::: user name:::: "+userName,true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and pass word for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #################::::::::::::####################    ");
	}
	
	
}

