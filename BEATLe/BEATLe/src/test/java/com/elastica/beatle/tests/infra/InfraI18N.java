package com.elastica.beatle.tests.infra;

import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.i18n.I18N;
import com.elastica.beatle.infra.InfraActions;

public class InfraI18N extends CommonTest{

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
	@Test( priority = 1 ,dataProviderClass = InfraDataProvider.class, dataProvider = "groupDataI18n" )
	public void CreateGroupI18N(String description,String action,String id,String payload,String type,String name) throws Exception {
		Reporter.log("exeuting testcase ****CreateGroupI18N() ***** Description"+description,true);
		Reporter.log("group Action"+action+" testcase type"+type,true);
		
		// search before creation
		HttpResponse responseSearch=Infractions.SearchGroup(suiteData, headers, name);
		String SearchResponseBody = ClientUtil.getResponseBody(responseSearch);
		String groupid=Infractions.getGroupid(SearchResponseBody, name);
		String responseBody="";
		if(!groupid.isEmpty())
		{
			
			
			HttpResponse response2 =  Infractions.deleteGroup(suiteData, headers, groupid);			
			Reporter.log("** deleteUser** Response: ",response2.getStatusLine().getStatusCode(),true);
			assertEquals( response2.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
			Reporter.log("** deleteGroup** Response: "+response2.getStatusLine().getStatusCode());

			
		}
		else// create group request
		{
//			HttpResponse response=Infractions.createGroup(suiteData, headers, payload);
//			
//			responseBody = ClientUtil.getResponseBody(response);
		}
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+";charset=UTF-8"));
		
		HttpResponse response=Infractions.createGroup(suiteData, headers, payload);
			
		    responseBody = ClientUtil.getResponseBody(response);
//			
//			
			
			if(type.contains("normal"))
			{
				//Assert the Response data
				String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				assertEquals(action_status,"success", "group data verification failed");
				// Search the Added group
				Reporter.log("Create group  Response: "+action_status,true);
				HttpResponse searchResponse=Infractions.SearchGroup(suiteData, headers, name);
				String searchResponseBody = ClientUtil.getResponseBody(searchResponse);
				//Assert the Response data
				assertEquals(searchResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				Reporter.log("** addGroup** Response: ",searchResponse.getStatusLine().getStatusCode(),true);
				String Searchname=Infractions.getGroupName(searchResponseBody, name);
				assertEquals(name,Searchname, "Name searched failed");
				Reporter.log("Expected status : "+Searchname,true);
				Reporter.log("Actual status :"+name,true);
				Reporter.log("Testcase execution Completed ",true);
			}
			else
			{
				//Assert the Response data
				String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				assertEquals(action_status,"", "group data verification failed");
				Reporter.log("Testcase execution Completed "+action_status,true);
			}
//		}
//		else
//		{
//			Reporter.log("Group already present: ",true);
//		}
		
		//System.out.println("** addGroup ** Response: "+response.getStatusLine().getStatusCode());
		
	}


	/*Author: Vijay Gangwar
	*todo: delete user sysadmin 
	*params none
	*/
	@Test ( priority = 1,dataProviderClass = InfraDataProvider.class, dataProvider = "createDeleteDataI18n",description = "Create and Delete a user without user login in to portal" )
	public void CreateAndDeleteUserI18N(String action,String email, String id,String payload) throws Exception {
		Reporter.log("****CreateDeleteUser() ***** Test Rail id:249679 ...",true);
		Reporter.log("Description 1. create user. 2 delete user even without login.",true);
		// search user before creation
		// search user after creation and before deletion
		String userId = null;
		userId = Infractions.searchUserid(suiteData, headers, email);
		Reporter.log("searched  User id is :" + userId, true);
		if(!userId.isEmpty())
		{
			// delete user
			Reporter.log(" delete old existing user with id"+userId,true);
			HttpResponse responseDeleteUser =Infractions.deleteUser(suiteData, headers, userId);
			Thread.sleep(10000);

		}
		else
		{
			//
		}

		// create user with different nationality
		Reporter.log("==========================", true);
		Reporter.log(action, true);
		HttpResponse responseCreatUser=null;
		responseCreatUser = Infractions.createUser(suiteData,headers,payload);
		String responseBody1 = ClientUtil.getResponseBody(responseCreatUser);
		System.out.println("Response: "+responseBody1);
		String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
		assertEquals(action_status,"success", "user Creation failed with reason"+responseBody1);
		Reporter.log("==========================", true);

		// search user after creation and before deletion
		userId = null;
		userId = Infractions.searchUserid(suiteData, headers, email);
		Reporter.log("The User Created is :" + userId, true);
		Reporter.log("Actual result : User is created "+userId,true);
		Reporter.log("Expected result :User is created",true);
		Reporter.log("Testcase execution Completed ",true);

	}
	 @Test( priority = 1 , dataProvider = "CreateDeleteProfileI18N")
		public void CreateProfile(String description,String action,String id,String payload,String type,String name) throws Exception {
			Reporter.log("exeuting testcase ****CreateProfile() ***** Description"+description,true);
			Reporter.log("group Action  "+action+" testcase type  "+type,true);
			// search profile

			String resource_uri=null;
			resource_uri=Infractions.getProfileResourceId(suiteData, headers, name);//Infrautils.SearchUserInGrp(responseBody, "profiles", "name", name, "resource_uri");
			if(!(resource_uri.isEmpty()))
			{
				// profile already exit
				//HttpResponse response1=Infractions.deleteUser(suiteData, headers, userId);
				HttpResponse response =Infractions.SearchProfile(suiteData, headers);
				String responseBody = ClientUtil.getResponseBody(response);
				//Assert the Response data
				//assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				Reporter.log("** CreateProfile** Response: ",response.getStatusLine().getStatusCode(),true);
			//	Reporter.log("** CreateProfile** Response: "+responseBody,true);
				String Pid=null;
				Pid=Infractions.getProfileid(responseBody, name );
				Reporter.log("deleting old exixting profile",true);
				HttpResponse Response=Infractions.deleteProfile(suiteData, headers, Pid);
				Thread.sleep(3000);
				}
			else
			{
				// do nothing
				Reporter.log("profile is not  present",true);
			}
				//Reporter.log("Profile already present ",true);
			
			
				
				//create uri for add profile request
				HttpResponse addResponse=Infractions.createProfile( suiteData,headers, payload);
				String addResponseBody = ClientUtil.getResponseBody(addResponse);
				Reporter.log("Create profile response log :"+addResponseBody,true);
				//Assert the Response data
				String action_status =(String) new JSONObject(addResponseBody).get("action_status").toString();
				assertEquals(action_status,"success", "Profile data verification failed");
				// cross check added profile
				HttpResponse SearchResponse =  Infractions.SearchProfile(suiteData, headers);//restClient.doGet(dataUriReSearc, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
				Thread.sleep(4000);
				String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
				//Assert the Response data
				String action_status1 =(String) new JSONObject(SearchResponseBody).get("action_status").toString();
				Reporter.log("Expected test result :success",true);
				Reporter.log("Actual test result :"+action_status1,true);
				assertEquals(action_status1,"success", "profile data verification failed");
		
			
			
			Reporter.log("Test execution is complete ",true);
		}
		
	 @DataProvider
		public Object[][] CreateDeleteProfileI18N() {
			return new Object[][]{
				// type    		id	   		value         
			//	{ "Securlets all OneDrive Profile All", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllOnedriveAll\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllOnedriveAll"},
				{ "Create Chinesse user", "Create", "C416155", "{\"profile\":{\"name\":\""+I18N.getString("groupname","zh_CN")+"\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal",I18N.getString("groupname","zh_CN")},
				{ "Create Japanese user", "Create", "C416155", "{\"profile\":{\"name\":\"みỦ馜碜餯\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","みỦ馜碜餯"},
				{ "Create French user", "Create", "C416155", "{\"profile\":{\"name\":\"L'éducation\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","L'éducation"},
				{ "Create German user", "Create", "C416155", "{\"profile\":{\"name\":\"Fiestas\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","Fiestas"},
				{ "Create Mexican user", "Create", "C416155", "{\"profile\":{\"name\":\"Москве\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","Москве"},
				{ "Create Russian user", "Create", "C416155", "{\"profile\":{\"name\":\"fácil\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","fácil"},
				{ "Create Hebrew user", "Create", "C416155", "{\"profile\":{\"name\":\"שלהואהואהוא\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","שלהואהואהוא"},
				{ "Create Arabic user", "Create", "C416155", "{\"profile\":{\"name\":\"پاکستان\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","پاکستان"},
				{ "Create Hindi user", "Create", "C416155", "{\"profile\":{\"name\":\"रामपाल\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","रामपाल"},
		
			};
	 }

	 /*Author: Vijay Gangwar
		*todo: add enduser
		*params none
		*/
		@Test (  priority = 1,dataProvider = "UsertoGrpI18n"  )
		public void AddUsertoGroupI18n(String email,String id,String payload,String group,String createPayload) throws Exception {
			Reporter.log("exeuting testcase ****AddUsertoGroup() ***** Assembl id:C2094 ...",true);
			Reporter.log("Description: Create the  user Enable request for Elastica  App  :*** ",true);
			Reporter.log(" Steps : 1 search if user already exist. 2.  Add  DPO user.3. add user to group and search added user ",true);			
			Reporter.log("search if user is already present",true);
			String userId =Infractions.searchUserid(suiteData, headers, email);
			// add end user if not present
			if(userId.isEmpty())
			{
				Reporter.log(" Create user is already present",true);
				Infractions.createUser(suiteData, headers, createPayload);
			}
			
				///search group and
				HttpResponse	 response2=Infractions.SearchGroup(suiteData, headers, group);
				String responseBody2 = ClientUtil.getResponseBody(response2);
				//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
				System.out.println("e ****responseBody ***** ..."+responseBody2);
				Reporter.log("search user responseBody: ."+responseBody2,true);
				String GroupId=Infrautils.getUserid(responseBody2,"groupslist","name",group);
				
				Reporter.log(" 1. Check if  user is already present",true);
				Reporter.log(" 2. Add user to the group",true);
				Reporter.log(" 1. Search user  in group",true);
				// check if user already present in group 
				//  add user to group
				// search user 
				String searchUri =suiteData.getReferer()+"/admin/user/assign";
				Reporter.log("search user uri ."+searchUri,true);
				
				URI SearchdataUri = ClientUtil.BuidURI(searchUri);
				HttpResponse Searchresponse =  restClient.doPost(SearchdataUri, headers, null, new StringEntity(payload));
				Thread.sleep(3000);
				String SearchResponseBody = ClientUtil.getResponseBody(Searchresponse);
				Reporter.log("responseBody: "+SearchResponseBody,true);
				String action_status =(String) new JSONObject(SearchResponseBody).get("action_status").toString();
				assertEquals(action_status,"success", "Response code verification failed");
				// cross check for user asignment
				 searchUri =suiteData.getReferer()+"/admin/group/users?group_name="+group;
				Reporter.log("search user uri ."+searchUri,true);
				//String payload=InfraConstants.ASSING_USER_PL;
				 SearchdataUri = ClientUtil.BuidURI(searchUri);
				HttpResponse Searchresponse1 =  restClient.doGet(SearchdataUri, headers);//doPost(SearchdataUri, headers, null, new StringEntity(payload));
				Thread.sleep(3000);
				String SearchResponseBody1 = ClientUtil.getResponseBody(Searchresponse1);
				// search user in group
				String user=Infrautils.SearchUserInGrp(SearchResponseBody1,"tenantgroupusers","user_email",email,"group_name");
				assertEquals(user,group, "Group id do not match verification failed");
				Reporter.log(" Expected Result: Added user:"+group,true);
				Reporter.log(" Actual Result: added user found:"+user,true);
				
				Reporter.log(" Test execution is completed",true);

		}
		
		@DataProvider
		public static Object [][] UsertoGrpI18n()
		{
			return new Object[][]{
				//user type    	email	id	   		payload      Group   																																																						casetype
				{ "vijay.gangwar+16@infrabeatle.com",  "C18886", "{\"email\":\"vijay.gangwar+16@infrabeatle.com\",\"deleted_groups\":[],\"added_groups\":[\"L'éducation\"]}","Blr","{\"user\":{\"first_name\":\"AjayEUser\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+16@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"}
				
			};
		}
	 
	 
	 
}
