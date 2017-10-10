package com.elastica.beatle.tests.infra;
import static org.testng.Assert.assertEquals;


import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.elastica.beatle.tests.infra.InfraUtils;
import com.universal.constants.CommonConstants;
import com.elastica.beatle.RestClient.*;
import com.elastica.beatle.infra.InfraActions;
import com.elastica.beatle.securlets.SecurletUtils;

import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.Authorization.AuthorizationHandler;
//com.elastica.beatle.Authorization;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.tests.gmail.GmailUtils;
public class Signin extends AuthorizationHandler {
	public Signin() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	List<NameValuePair> headerArray;// = new ArrayList<NameValuePair>();
	Client restClient = new Client();
	HttpResponse loginResponse = null;
	InfraActions Infractions;
	InfraUtils Infrautils;
	String loginurl=null;
	long maxWaitTime = 20;
	long minWaitTime = 5;
	GmailUtils Securletutil;
	//AuthorizationHandler AutHandle= AuthorizationHandler();
	protected ForensicSearchResults sentMessageLogs, receiveMessageLogs;
	@Parameters("hostName")
	@BeforeClass
	public void InitTest(String hostName ) throws Exception {
		headerArray = new ArrayList<NameValuePair>();
		loginurl= hostName;
		Securletutil = new GmailUtils();
		Infrautils= new InfraUtils();
		Infractions = new InfraActions();
		signinTesr( headerArray,loginurl);
		//requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword(), suiteData.getLoginURL())));		
		
		URI loginURI = ClientUtil.BuidURI("https",loginurl,"/user/loginapi");
		headerArray.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam("admin@infrabeatle.com","Matrix#123")));		

		}
	@Test( priority = 1 , dataProvider = "loginData")
	public void LoginTest(String userType,String id,String uName,String pwd,String Scenario) throws Exception
	{
		String steps[] = {	"1. Login to the Soc portal with valid user Credential.", 
							"2. validate landing page details."};
		Reporter.log("Test Descritption: "+steps,true);
		Reporter.log("Test Method: LoginTest ",true);
		Reporter.log("****LoginTest() ***** Test Rail id:"+id,true);
		HttpResponse loginResponse = null;
		URI loginURI = ClientUtil.BuidURI("https",loginurl,"/user/loginapi");
		//update password for prod link.
		if(loginurl.contains("app.elastica.net")&&(uName.contains("admin@infrabeatle.com")))
		{
			pwd="Elastica123!";
			
		}
		StringEntity sEntity = new StringEntity(new JSONObject().put("email", uName).put("password", pwd).toString());
		loginResponse = restClient.doPost(loginURI, headerArray, null, sEntity);
		String responseBody=ClientUtil.getResponseBody(loginResponse);
		System.out.println("Response: "+responseBody);
		String action_status =(String) new JSONObject(responseBody).get("api_response").toString();
		if(Scenario.contains("Normal"))
		{
			assertEquals(action_status,"0", "Response data verification failed");
			// verify landing page for 
			String role =(String) new JSONObject(responseBody).get("role").toString();
			if(role.contains("sysadmin"))
			{
				String ExpectedLandingPage="../appDashboards/index.html#/";
				String ActualPage =(String) new JSONObject(responseBody).get("tenant_landing_page").toString();
				assertEquals(ActualPage,ExpectedLandingPage, "Response data verification failed");
				Reporter.log("Expected status :"+ExpectedLandingPage,true);
				Reporter.log("Actual status :"+ActualPage,true);
				Reporter.log("Testcase execution Completed ",true);
				
			}
			else if(role.contains("admin"))
			{
				String ExpectedLandingPage="../appDashboards/index.html#/";
				String ActualPage =(String) new JSONObject(responseBody).get("tenant_landing_page").toString();
				assertEquals(ActualPage,ExpectedLandingPage, "Response data verification failed");
				Reporter.log("Expected status :"+ExpectedLandingPage,true);
				Reporter.log("Actual status :"+ActualPage,true);
				Reporter.log("Testcase execution Completed ",true);
				
			}
			else
			{
				String ExpectedLandingPage="../appAccount/index.html#/profile";
				String ActualPage =(String) new JSONObject(responseBody).get("tenant_landing_page").toString();
				assertEquals(ActualPage,ExpectedLandingPage, "Response data verification failed");
				Reporter.log("Expected status :"+ExpectedLandingPage,true);
				Reporter.log("Actual status :"+ActualPage,true);
				Reporter.log("Testcase execution Completed ",true);
				
			}
				
			
		}
		else
		{
			assertEquals(action_status,"10", "Response data verification failed");
		}
		Reporter.log("Testcase execution Completed ",true);
	}
	
	@DataProvider
	public Object[][] loginData() {
		return new Object[][]{
			// userType    		id		email ,			password, 			casetype	        
			{ "SysAdmin login", "TMB", "admin@infrabeatle.com","Elastica@1234","Normal"},
			{ "Admin login", "TMB", "vijay.gangwar+55@infrabeatle.com","Elastica@123","Normal"},
			{ "EndUser login", "C1783", "vijay.gangwar+56@infrabeatle.com","Elastica@123","Normal"},
			{ "DPO login", "TMB", "vijay.gangwar+57@infrabeatle.com","Elastica@123","Normal"},
			{ "Admin login", "TMB", "vijay.gangwar+68@infrabeatle.com","Elastica123","error"},
			{ "EndUser login", "TMB", "vijay.gangwar+68@infrabeatle.com","Elastica123","error"},
			{ "EndUser login", "TMB", "vijay.gangwar+68@infrabeatle.com","","error"},
			{ "InactiveSysAdmin login", "TMB", "vijay.gangwar+70@infrabeatle.com","Elastica123","error"},
			{ "Inactive Admin login", "TMB", "vijay.gangwar+70@infrabeatle.com","Elastica123","error"},
			{ "Inactive end user login", "TMB", "vijay.gangwar+70@infrabeatle.com","Elastica123","error"}
//			{ "EndUser login", "TMB", "vijay.gangwar+70@infrabeatle.com","Elastica123","error"}
		};
	}
	
	
	
	@Test( priority = 1 , dataProvider = "passwordData")
	public void forgotPassword(String description,String id,String payload,String caseType) throws Exception
	{
		Reporter.log("****forgotPassword() ***** Test Rail id:"+id,true);
		Reporter.log("Descritpion.Reset the user password ",true);
		//https://qa-vpc-ui.elastica-inc.com/user/forgotpasswordapi
		URI loginURI = ClientUtil.BuidURI("https",loginurl,InfraConstants.FORGOT_PWD);
		loginResponse = restClient.doPost(loginURI, headerArray, null, new StringEntity(payload));
		String responseBody=ClientUtil.getResponseBody(loginResponse);
		System.out.println("Response: "+responseBody);
		String action_status =(String) new JSONObject(responseBody).get("api_response").toString();
		assertEquals(action_status,"0", "Response data verification failed");
		Reporter.log("Expected status :0 ",true);
		Reporter.log("Actual status :"+action_status,true);
		Reporter.log("Testcase execution Completed ",true);
	}
	
	@DataProvider
	public Object[][] passwordData() {
		return new Object[][]{
			// userType    		id		email ,			password, 			casetype	        
			{ "SysAdmin login","C1806", "{\"email\":\"vijay.gangwar+26@infrabeatle.com\"}","Normal"},
		//	{ "invalid id", "TMB", "vijay.gangwar+123@infrabeatle.com","Matrix$123","Normal"},
		//	{ "EndUserDpo login", "TMB", "vijay.gangwar+68@infrabeatle.com","Elastica123!","Normal"},
			
		};
	}
	public   void signinTesr(List<NameValuePair> headerArray,String loginurl)
		{
		Client restClient = new Client();
		//HttpResponse loginResponse = null;
		
	
		try {
			URI adminLoginURI = ClientUtil.BuidURI("https", loginurl, "/admin/deployment");
			HttpResponse preLoginResponse = restClient.doGet(adminLoginURI, null);
			ClientUtil Clientutil = new ClientUtil();
			String preLoginResponseboday=Clientutil.getResponseBody(preLoginResponse);
			System.out.println(" Responseboday : "+preLoginResponseboday);
			
			String csrfToken = Infrautils.getAttributeFromResponseHeader(preLoginResponse.getAllHeaders(), "csrftoken");			
			URI appForensicURI = ClientUtil.BuidURI("https", loginurl, "/static/ng/appForensics/index.html");			
		//	List<NameValuePair> headerArray = new ArrayList<NameValuePair>();
			headerArray.add(new BasicNameValuePair("Connection", "keep-alive"));
			headerArray.add(new BasicNameValuePair("Accept", "application/json, text/plain, */*"));
			headerArray.add(new BasicNameValuePair("Origin", "https://eoe.elastica-inc.com"));
			headerArray.add(new BasicNameValuePair("X-Requested-With", "XMLHttpRequest"));
			headerArray.add(new BasicNameValuePair("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"));
			headerArray.add(new BasicNameValuePair("X-CSRFToken", csrfToken));
			headerArray.add(new BasicNameValuePair("Content-Type", "application/json;charset=UTF-8"));
			headerArray.add(new BasicNameValuePair("Referer", appForensicURI.toString()));
			headerArray.add(new BasicNameValuePair("Accept-Language", "en-US,en;q=0.8"));
			headerArray.add(new BasicNameValuePair("Cookie", "csrftoken=" + csrfToken + ";"+"mf_authenticated=Neo8xdmy0DY3dqpmGvMiaiahKELzMBEFsgZX8wyk;"));
		
//			URI loginURI = ClientUtil.BuidURI("https","eoe.elastica-inc.com","/user/loginapi");
//			StringEntity sEntity = new StringEntity(new JSONObject().put("email", uName).put("password", pwd).toString());
//			loginResponse = restClient.doPost(loginURI, headerArray, null, sEntity);

	} catch (Exception e) {
		e.printStackTrace();
	}

	//return loginResponse;
}
	/*Author: Vijay Gangwar
	*todo: addGroup request 
	*Admin: Secondray domin user
	*Profile:infauser with view export Import Report permission on infra.com 
	*Secondary domain: Infra.com
	*/
	
	@Test( priority = 1,dataProvider="AdminloginData" )
	public void UserAdmin(String userType,String profile,String uName,String pwd,String Scenario,String payload) throws Exception
	{
		String steps[] = {	"1. Login to the Soc portal with valid user Credential.", 
		"2. validate landing page details."};
		Reporter.log("Test Descritption: "+steps,true);
		Reporter.log("Test Method: LoginTest ",true);
		HttpResponse loginResponse = null;
		URI loginURI = ClientUtil.BuidURI("https",loginurl,"/user/loginapi");
		StringEntity sEntity = new StringEntity(new JSONObject().put("email", uName).put("password", pwd).toString());
		loginResponse = restClient.doPost(loginURI, headerArray, null, sEntity);
		String responseBody=ClientUtil.getResponseBody(loginResponse);
		System.out.println("Response: "+responseBody);
		String action_status =(String) new JSONObject(responseBody).get("api_response").toString();
		//create a user
		String uri ="https://eoe.elastica-inc.com/"+InfraConstants.API_ADD_USER;
		Reporter.log("Request Method type: POST",true);
		Reporter.log("Request API :"+uri,true);
		System.out.println("exeuting testcase ****UserAdmin() *****uri ..."+uri);
		URI dataUri = ClientUtil.BuidURI(uri);
		responseBody=null;
		HttpResponse response =  restClient.doPost(dataUri, headerArray, null, new StringEntity(payload));
		responseBody= ClientUtil.getResponseBody(response);

	}
	@DataProvider
	public Object[][] AdminloginData() {
		return new Object[][]{
			// userType    		id		email ,			password, 			casetype	        
			{ "Admin login ", "Infauser", "vijay.gangwar+61@infrabeatle.com","Matrix$123","Normal","{\"user\":{\"first_name\":\"Ajay\",\"last_name\":\"User\",\"email\":\"vijay.gangwar+12@infra.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
		//	{ "Admin login", "TMB", "vijay.gangwar+67@infrabeatle.com","Matrix$123","Normal"},
		//	{ "EndUserDpo login", "TMB", "vijay.gangwar+68@infrabeatle.com","Elastica123!","Normal"},
			
		};
	}
	
	
	//@Test( priority = 1,dataProvider="GoogleAdminloginData" )
	public void UserSysAdminRbac(String userType,String profile,String uName,String pwd,String Scenario,String payload) throws Exception
	{
		String steps[] = {	"1. Login to the Soc portal with valid user Credential.", 
		"2. validate landing page details."};
		Reporter.log("Test Descritption: "+steps,true);
		Reporter.log("Test Method: LoginTest ",true);
		//HttpResponse loginResponse = null;
		URI loginURI = ClientUtil.BuidURI("https",loginurl,"/user/loginapi");
		System.out.println("Test case complete ..."+headerArray);
		headerArray.remove(10);
		headerArray.add((new BasicNameValuePair("Set-Cookie",null)));
		headerArray.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam("admin+2@securletbeatle.com","Elastica1234!")));
		StringEntity sEntity = new StringEntity(new JSONObject().put("email", uName).put("password", pwd).toString());
		loginResponse = restClient.doPost(loginURI, headerArray, null, sEntity);
		String responseBody=ClientUtil.getResponseBody(loginResponse);
		System.out.println("Response: "+responseBody);
		String action_status =(String) new JSONObject(responseBody).get("api_response").toString();
		Long count=fetchActivityLogs();
		
		System.out.println("activity logs count ..."+count);
		System.out.println("Test case complete ...");
		// fetch activity logs
		
		
		
		//create a user
//		String uri ="https://eoe.elastica-inc.com/"+InfraConstants.API_ADD_USER;
//		Reporter.log("Request Method type: POST",true);
//		Reporter.log("Request API :"+uri,true);
//		System.out.println("exeuting testcase ****addInactiveUser() *****uri ..."+uri);
//		URI dataUri = ClientUtil.BuidURI(uri);
//		responseBody=null;
//		HttpResponse response =  restClient.doPost(dataUri, headerArray, null, new StringEntity(payload));
//		responseBody= ClientUtil.getResponseBody(response);

	}
	@DataProvider
	public Object[][] GoogleAdminloginData() {
		return new Object[][]{
			// userType    		id		email ,			password, 			casetype	        
			{ "google SysAdmin login ", "Infauser", "admin+2@securletbeatle.com","Elastica1234!","Normal","{\"user\":{\"first_name\":\"Ajay\",\"last_name\":\"User\",\"email\":\"vijay.gangwar+12@infra.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
		//	{ "Admin login", "TMB", "vijay.gangwar+67@infrabeatle.com","Matrix$123","Normal"},
		//	{ "EndUserDpo login", "TMB", "vijay.gangwar+68@infrabeatle.com","Elastica123!","Normal"},
			
		};
	}
	
	public long fetchActivityLogs() throws Exception {
		//Fetch the logs

		try {

			for (int i = 0; i <= (minWaitTime * 60 * 1000); i+=60000 ) {
				Thread.sleep(3000);
				//sleep(CommonConstants.TEN_MINUTES_SLEEP);

				
				//Get Receive related logs
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Gmail.name());
				termmap.put("Object_type","Email_Message");
				termmap.put("Activity_type", "Receive");
				this.receiveMessageLogs = Securletutil.getInvestigateLogs(-30, 10, facility.Gmail.name(), termmap, "admin+2@securletbeatle.com", "api-eoe.elastica-inc.com", 
						getCSRFToken(loginResponse), getUserSessionID(loginResponse), 0, 200, "Google Apps");

		
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		//Reporter.log(MarshallingUtils.marshall(sentMessageLogs), true);
		Reporter.log(MarshallingUtils.marshall(receiveMessageLogs), true);
		long total = receiveMessageLogs.getHits().getTotal();
		Reporter.log("Total sent messages logs count:"+ total, true);

		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for sent messages in gmail");
		}
		return total;
	}

}
