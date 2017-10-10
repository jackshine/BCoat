package com.elastica.beatle.tests.infra;

import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.infra.InfraActions;

public class InfraRbacValidation  extends CommonTest{
	//private final static CloseableHttpClient client = HttpClientBuilder.create().build();
	List<NameValuePair> headers;
	String tenantDB;
	InfraActions Infractions;
	InfraUtils Infrautils;
	 @BeforeClass(alwaysRun= true)
	public void initTests(ITestContext suiteConfigurations) throws Exception {		
		headers =getHeaders();
		Infrautils= new InfraUtils();
		Infractions = new InfraActions();
		tenantDB=getRegressionSpecificSuitParameters("tanentdb");
		
	}
	
	
	@Test( priority = 2,dataProvider="ResetPassword" )
	public void sendResetPasswordUser(String Description,String id,String email,String paylod,String caseType) throws Exception {
		Reporter.log("Description:1. Admin to reset user password ** ...",true);
		Reporter.log("Description:2. user gets  reset password email ** ...",true);
		
		//search  user ,for password reset
		String uri =suiteData.getReferer()+InfraConstants.SEARCH_USER+"vijay.gangwar+16@infrabeatle.com";
		Reporter.log("search user uri ."+uri,true);
		//prepare uri for get user request 
		URI dataUri = ClientUtil.BuidURI(uri);
		HttpResponse response =  restClient.doGet(dataUri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
		System.out.println("e ****responseBody ***** ..."+responseBody);
		Reporter.log("search user responseBody: ."+responseBody,true);
		String userId=Infrautils.getUserid(responseBody,"userslist","email","vijay.gangwar+16@infrabeatle.com");
		
		System.out.println(" get list of users Response: "+responseBody);		
		String uri1 = suiteData.getReferer()+InfraConstants.RESET_PASSWORD;
		String payload=InfraConstants.PAYLOAD_RESET;
		payload=payload.replace("uid", userId);
		URI dataUri1 = ClientUtil.BuidURI(uri1);
		// execute the request
		HttpResponse response1 =  restClient.doPost(dataUri1, headers, null, new StringEntity(payload));
		String responseBody1 = ClientUtil.getResponseBody(response1);
		String action_status =(String) new JSONObject(responseBody1).get("api_response").toString();
		// validate Response 
		if(caseType.contains("error"))
		{
			Reporter.log("Expected api response: 10 ",true);
			Reporter.log("Actual api response:  "+action_status,true);
			assertEquals("10", action_status, "Response code verification failed");
		}
		else
		{
			Reporter.log("Expected api response: 0 ",true);
			Reporter.log("Actual api response:  "+action_status,true);
			assertEquals("0", action_status, "Response code verification failed");
		}
		
		Reporter.log("Password reset test completed ",true);		
		
	}
	@DataProvider
	public Object[][] ResetPassword() {
		return new Object[][]{
			// Action    		id	   		payload         
			{ "Reset other Inactive user password","TBM",  "vijay.gangwar+16@infrabeatle.com",InfraConstants.PAYLOAD_RESET,"error" },
			{ "Reset other Active user password", "C2162","vijay.gangwar+56@infrabeatle.com",  "{\"user\":{\"is_dpo\":false,\"last_name\":\"Signing\",\"changed_password\":\"2015-11-30T07:18:00.246000\",\"is_alerting\":false,\"is_super_admin\":false,\"quarantine_info\":null,\"created_on\":\"2015-11-30T07:16:58.009000\",\"access_profiles\":[],\"timezone\":null,\"id\":\"565bf7ea27d02e5c2ec0ddb8\",\"first_name\":\"User\",\"is_blocked\":false,\"modified_by\":null,\"title\":\"\",\"work_phone\":\"\",\"created_by\":\"admin@infrabeatle.com\",\"is_partner\":false,\"email\":\"vijay.gangwar+56@infrabeatle.com\",\"is_two_factor_auth_enabled\":false,\"is_active\":true,\"threatscore\":{},\"two_factor_auth_key\":\"\",\"secondary_user_id\":\"\",\"default_selected_range\":null,\"blocked_apps\":{},\"is_admin\":false,\"groups\":[],\"modified_on\":null,\"force_logout\":false,\"epagent_id\":null,\"is_dummy\":false,\"lock_time\":null,\"is_quarantined\":false,\"notes\":\"\",\"risk_status\":\"\",\"cell_phone\":\"\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/uid/\"}}","error"},
			{ "Reset other Active Admin password", "C2162","vijay.gangwar+55@infrabeatle.com",  "{\"user\":{\"is_dpo\":false,\"last_name\":\"Default\",\"changed_password\":\"2015-11-30T07:14:40.975000\",\"is_alerting\":false,\"is_super_admin\":false,\"quarantine_info\":null,\"created_on\":\"2015-11-30T07:13:28.191000\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"timezone\":null,\"id\":\"565bf71827d02e0279d8ed05\",\"first_name\":\"Admin\",\"is_blocked\":false,\"modified_by\":null,\"title\":\"\",\"work_phone\":\"\",\"created_by\":\"admin@infrabeatle.com\",\"is_partner\":false,\"email\":\"vijay.gangwar+55@infrabeatle.com\",\"is_two_factor_auth_enabled\":false,\"is_active\":true,\"threatscore\":{},\"two_factor_auth_key\":\"\",\"secondary_user_id\":\"\",\"default_selected_range\":null,\"blocked_apps\":{},\"is_admin\":false,\"groups\":[],\"modified_on\":null,\"force_logout\":false,\"epagent_id\":null,\"is_dummy\":false,\"lock_time\":null,\"is_quarantined\":false,\"notes\":\"\",\"risk_status\":\"\",\"cell_phone\":\"\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/uid/\"}}","error"},
			
			
		};
	}
	
	/*Author: Vijay Gangwar
	*todo: delete user sysadmin 
	*params none
	*/
	@Test ( priority = 2 ,dataProvider = "creatDelUser")
	public void modifyAdminOperations(String Action,String id,String email,String payload,String caseType) throws Exception {
		Reporter.log("****CreateDeleteUser() ***** Test rail id:"+id,true);
		Reporter.log("1. Admin action to be performed :"+Action,true);
		if(Action.contains("CreateAdmin"))
		{
			//create the Admin.
			//search admin profile to attache it to new admin
			String uriSearch= suiteData.getReferer()+"admin/profiles/list?order_by=name";
			URI dataUriSearc = ClientUtil.BuidURI(uriSearch);
			HttpResponse response =  restClient.doGet(dataUriSearc, headers);
			Thread.sleep(3000);
			String responseBody = ClientUtil.getResponseBody(response);
			
			String profileId=null;
			profileId=Infrautils.SearchUserInGrp(responseBody, "profiles", "name", "UsersView", "id");
			// check if user is alredy present
			String SearchUri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
			Reporter.log("search user uri ."+SearchUri,true);
			URI dataUriSearch = ClientUtil.BuidURI(SearchUri);
			HttpResponse SearchResponse =  restClient.doGet(dataUriSearch, headers);
			String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
			//print response body
			Reporter.log("search user responseBody: ."+SearchResponse,true);
			// searach for the  id of user
			String userId=Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
			if(null==userId)
			{
				String uri =suiteData.getReferer()+InfraConstants.API_ADD_USER;
				URI dataUri = ClientUtil.BuidURI(uri);
				payload=payload.replace("Pid", profileId);
				HttpResponse CreateResponse =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				String CreateResponseBody = ClientUtil.getResponseBody(CreateResponse);
				
				System.out.println("Response: "+CreateResponseBody);
				String action_status =(String) new JSONObject(CreateResponseBody).get("action_status").toString();
				assertEquals(action_status,"success", "Api status verification failed");
				Reporter.log(" Expected Result:success",true);
				Reporter.log(" Actual Result first name: "+action_status,true);
			}
			else
			{
				Reporter.log(" User already present: ",true);
			}
			
			
			
		}
		else if(Action.contains("deactivateAdmin"))
		{
			//search admin profile to attache it to new admin
			String uriSearch= suiteData.getReferer()+"admin/profiles/list?order_by=name";
			URI dataUriSearc = ClientUtil.BuidURI(uriSearch);
			HttpResponse response =  restClient.doGet(dataUriSearc, headers);
			Thread.sleep(3000);
			String responseBody = ClientUtil.getResponseBody(response);			
			String profileId=null;
			profileId=Infrautils.SearchUserInGrp(responseBody, "profiles", "name", "UsersView", "id");
			payload=payload.replace("Pid", profileId);
			//search admin to be deactivated
			String SearchUri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
			Reporter.log("search user uri ."+SearchUri,true);
			URI dataUriSearch = ClientUtil.BuidURI(SearchUri);
			HttpResponse SearchResponse =  restClient.doGet(dataUriSearch, headers);
			String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
			//print response body
			Reporter.log("search user responseBody: ."+SearchResponse,true);
			// searach for the  id of user
			String userId=Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
			if(null!=userId)
			{
				payload=payload.replace("Uid", userId);
				String UpdateUri= suiteData.getReferer()+"admin/user/ng/update";
				URI UpdatedataUri = ClientUtil.BuidURI(UpdateUri);
				HttpResponse response1 =  restClient.doPost(UpdatedataUri, headers,null,new StringEntity(payload));
				Thread.sleep(3000);
				String responseBody1 = ClientUtil.getResponseBody(response1);
				System.out.println("Response: "+responseBody1);
				String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
				assertEquals(action_status,"success", "Api status verification failed");
				Reporter.log(" Expected Result:success",true);
				Reporter.log(" Actual Result first name: "+action_status,true);
				
			}
			else
			{
				Reporter.log("search not found: "+userId,true);
			}
			
		}
		else if(Action.contains("activateAdmin"))
		{

			//search admin profile to attache it to new admin
			String uriSearch= suiteData.getReferer()+"admin/profiles/list?order_by=name";
			URI dataUriSearc = ClientUtil.BuidURI(uriSearch);
			HttpResponse response =  restClient.doGet(dataUriSearc, headers);
			Thread.sleep(3000);
			String responseBody = ClientUtil.getResponseBody(response);			
			String profileId=null;
			profileId=Infrautils.SearchUserInGrp(responseBody, "profiles", "name", "UsersView", "id");
			payload=payload.replace("Pid", profileId);
			//search admin to be deactivated
			String SearchUri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
			Reporter.log("search user uri ."+SearchUri,true);
			URI dataUriSearch = ClientUtil.BuidURI(SearchUri);
			HttpResponse SearchResponse =  restClient.doGet(dataUriSearch, headers);
			String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
			//print response body
			Reporter.log("search user responseBody: ."+SearchResponse,true);
			// searach for the  id of user
			String userId=Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
			if(null!=userId)
			{
				payload=payload.replace("Uid", userId);
				String UpdateUri= suiteData.getReferer()+"admin/user/ng/update";
				URI UpdatedataUri = ClientUtil.BuidURI(UpdateUri);
				HttpResponse response1 =  restClient.doPost(UpdatedataUri, headers,null,new StringEntity(payload));//doPost(dataUri, headers, null, new StringEntity(payload));
				Thread.sleep(3000);
				String responseBody1 = ClientUtil.getResponseBody(response1);
				System.out.println("Response: "+responseBody1);
				String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
				assertEquals(action_status,"success", "Api status verification failed");
				Reporter.log(" Expected Result:success",true);
				Reporter.log(" Actual Result first name: "+action_status,true);
				
			}
			else
			{
				Reporter.log("search not found: "+userId,true);
			}
			
		
			
		}
		else if(Action.contains("deleteAdmin"))
		{
			//Delete newly created user
			String uri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
			Reporter.log("search user uri ."+uri,true);
//			///elasticaco/api/admin/v1/users/
			URI dataUri1 = ClientUtil.BuidURI(uri);
			HttpResponse response1 =  restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody1 = ClientUtil.getResponseBody(response1);
			//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
			System.out.println("e ****responseBody ***** ..."+responseBody1);
			Reporter.log("search user responseBody: ."+responseBody1,true);
			
			String userId=Infrautils.getUserid(responseBody1,"userslist","email",email);
			System.out.println(" get list of users Response: "+responseBody1);			
			Reporter.log("delete the created user with id"+userId,true);
			// delete user may day
			String Deleteuri =suiteData.getScheme()+"://"+ suiteData.getApiserverHostName()+"/"+tenantDB+InfraConstants.API_CALL_USERS+userId+"/";
			System.out.println("exeuting  Deleteuri : "+Deleteuri);
			URI dataUri = ClientUtil.BuidURI(Deleteuri);
			HttpResponse response =  restClient.doDelete(dataUri, headers);
			
			assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
			//Reporter.log("** deleteUser** Response: ",response.getStatusLine().getStatusCode(),true);
			Reporter.log("** deleteUser** Response: "+response.getStatusLine().getStatusCode(),true);
			
			// cross verify for user deletion
			String uri1 =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
			Reporter.log("search user uri ."+uri1,true);
			//prepare uri for get user request 
			URI dataUri2 = ClientUtil.BuidURI(uri);
			HttpResponse response2 =  restClient.doGet(dataUri2, headers);
			String responseBody2 = ClientUtil.getResponseBody(response2);
			System.out.println("e ****responseBody ***** ..."+responseBody2);
			//Reporter.log("search user responseBody: ."+responseBody2,true);
			userId=Infrautils.getUserid(responseBody2,"userslist","email",email);
			
			Reporter.log(" Delete user with id: "+userId);
			assertEquals( null,userId, "Response code verification failed");
			Reporter.log(" Expected result for deleted  user: null ",true);
			Reporter.log(" Actual result for deleted  user: null "+userId,true);
			
		}
		else if(Action.contains("Edit"))
		{
			//search admin profile to attache it to new admin
			String uriSearch= suiteData.getReferer()+"admin/profiles/list?order_by=name";
			URI dataUriSearc = ClientUtil.BuidURI(uriSearch);
			HttpResponse response =  restClient.doGet(dataUriSearc, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
			Thread.sleep(3000);
			String responseBody = ClientUtil.getResponseBody(response);			
			String profileId=null;
			profileId=Infrautils.SearchUserInGrp(responseBody, "profiles", "name", "UsersView", "id");
			if(!Action.contains("EditRoleToUser"))
			{
				payload=payload.replace("Pid", profileId);
			}
			
			//search admin to be deactivated
			String SearchUri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
			Reporter.log("search user uri ."+SearchUri,true);
			URI dataUriSearch = ClientUtil.BuidURI(SearchUri);
			HttpResponse SearchResponse =  restClient.doGet(dataUriSearch, headers);
			String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
			//print response body
			Reporter.log("search user responseBody: ."+SearchResponse,true);
			// searach for the  id of user
			String userId=Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
			if(null!=userId)
			{
				payload=payload.replace("Uid", userId);
				
				//perform the edit action
				String uri =suiteData.getReferer()+InfraConstants.EDIT_ACTION;
				URI dataUri = ClientUtil.BuidURI(uri);
				
				HttpResponse response1 =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				String responseBody1 = ClientUtil.getResponseBody(response1);
				System.out.println("Response: "+responseBody1);
				String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
				assertEquals(action_status,"success", "Api status verification failed");
				Reporter.log(" Expected Result:success",true);
				Reporter.log(" Actual Result first name: "+action_status,true);
				
			}
			else
			{
				Reporter.log("search not found: "+userId,true);
			}
		}
		else
		{
			//fall through
			Reporter.log("un-supported Action requested: ",true);
		}

		
		
	}
	
	@DataProvider
	public Object[][] creatDelUser() {
		return new Object[][]{
			// Action    		id	   		payload         
			{ "CreateAdmin","TBM",  "vijay.gangwar+54@infrabeatle.com","{\"user\":{\"first_name\":\"Admin\",\"last_name\":\"CURD\",\"email\":\"vijay.gangwar+54@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user\",\"is_dpo\":false}}","normal" },
			{ "deactivateAdmin ", "TBM","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"is_dpo\":false,\"last_name\":\"CURD\",\"changed_password\":\"2015-12-01T07:23:05.861000\",\"is_alerting\":false,\"is_super_admin\":false,\"quarantine_info\":null,\"created_on\":\"2015-12-01T07:23:05.757000\",\"access_profiles\":[\"Pid\"],\"timezone\":null,\"id\":\"Uid\",\"first_name\":\"User\",\"is_blocked\":false,\"modified_by\":null,\"title\":\"\",\"work_phone\":\"\",\"created_by\":\"vijay.gangwar+87@infrabeatle.com\",\"is_partner\":false,\"email\":\"vijay.gangwar+54@infrabeatle.com\",\"is_two_factor_auth_enabled\":false,\"is_active\":true,\"threatscore\":{},\"two_factor_auth_key\":\"\",\"secondary_user_id\":\"\",\"default_selected_range\":null,\"blocked_apps\":{},\"is_admin\":false,\"groups\":[],\"modified_on\":null,\"force_logout\":false,\"epagent_id\":null,\"is_dummy\":false,\"lock_time\":null,\"is_quarantined\":false,\"notes\":\"CURD user\",\"risk_status\":\"\",\"cell_phone\":\"\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/Uid/\"},\"action\":false}","normal"},
			{ "activateAdmin", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"is_dpo\":false,\"last_name\":\"CURD\",\"changed_password\":\"2015-12-01T07:23:05.861000\",\"is_alerting\":false,\"is_super_admin\":false,\"quarantine_info\":null,\"created_on\":\"2015-12-01T07:23:05.757000\",\"access_profiles\":[\"Pid\"],\"timezone\":null,\"id\":\"Uid\",\"first_name\":\"User\",\"is_blocked\":false,\"modified_by\":null,\"title\":\"\",\"work_phone\":\"\",\"created_by\":\"vijay.gangwar+87@infrabeatle.com\",\"is_partner\":false,\"email\":\"vijay.gangwar+54@infrabeatle.com\",\"is_two_factor_auth_enabled\":false,\"is_active\":true,\"threatscore\":{},\"two_factor_auth_key\":\"\",\"secondary_user_id\":\"\",\"default_selected_range\":null,\"blocked_apps\":{},\"is_admin\":false,\"groups\":[],\"modified_on\":null,\"force_logout\":false,\"epagent_id\":null,\"is_dummy\":false,\"lock_time\":null,\"is_quarantined\":false,\"notes\":\"CURD user\",\"risk_status\":\"\",\"cell_phone\":\"\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/Uid/\"},\"action\":true}","normal"},
			{ "EditFnameAdmin", "TBM","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURD\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "EditLnameAdmin", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURDEdited\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "EditWorkPhoneAdmin", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURDEdited\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"1-8856223\",\"cell_phone\":\"\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "EditCellPhoneAdmin", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURDEdited\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"1-998856223\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "EditTitle", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURDEdited\",\"title\":\"Mr.\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "EditSecondryId", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURDEdited\",\"title\":\"Mr\",\"secondary_user_id\":\"202478\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "EditNote", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURDEdited\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user is created\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "EditInactive", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURDEdited\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":false,\"notes\":\"CURD useris created\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "EditActive", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURDEdited\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"Pid\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user is created\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "EditRoleToUser", "C2162","vijay.gangwar+54@infrabeatle.com",  "{\"user\":{\"first_name\":\"AdminEdited\",\"last_name\":\"CURDEdited\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"1-8856223\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"CURD user\",\"is_dpo\":false},\"id\":\"Uid\"}","normal"},
			{ "deleteAdmin", "C2162","vijay.gangwar+54@infrabeatle.com",  "payload","normal"},

			
		};
	}
}

