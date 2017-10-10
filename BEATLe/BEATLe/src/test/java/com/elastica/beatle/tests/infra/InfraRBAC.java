package com.elastica.beatle.tests.infra;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.infra.InfraActions;

public class InfraRBAC extends CommonTest{
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
	 
	 @Test( priority = 1 , dataProvider = "CreateprofileDataDVC",groups = { "P1","EOE","SANITY","REGRESSION" })
		public void CreateProfileDVC(String description,String action,String id,String payload,String type,String name) throws Exception {
			Reporter.log("exeuting testcase ****CreateProfileDVC() ***** Description"+description,true);
			Reporter.log("group Action  "+action+" testcase type  "+type,true);
			// search profile 
			String resource_uri=null;
			resource_uri=Infractions.getProfileResourceId(suiteData, headers, name);
			if(null == resource_uri)
			{
				//profile not exist 
				//create uri for add profile request
				HttpResponse addResponse=Infractions.createProfile( suiteData,headers, payload);
				String addResponseBody = ClientUtil.getResponseBody(addResponse);
				//Assert the Response data
				String action_status =(String) new JSONObject(addResponseBody).get("action_status").toString();
				assertEquals(action_status,"success", "group data verification failed");
				// cross check added profile
				HttpResponse SearchResponse =  Infractions.SearchProfile(suiteData, headers);//restClient.doGet(dataUriReSearc, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
				Thread.sleep(4000);
				String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
				//Assert the Response data	
				String status =(String) new JSONObject(SearchResponseBody).get("action_status").toString();
				Reporter.log("Expected test result :success",true);
				Reporter.log("Actual test result :"+status,true);
				assertEquals(action_status,"success", "profile data verification failed");
			
			}
			else
			{
				// profile already exit
				
				Reporter.log("Profile already present ",true);
				
			}
			Reporter.log("Test execution is complete ",true);
		}
		

		@DataProvider
		public Object[][] CreateprofileDataDVC() {
			return new Object[][]{
				// type    		id	   		value         
			//	{ "Add profile", "Create", "tbm", "{\"profile\":{\"name\":\"Dropbox\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","Dropbox"},
				{ "Audit ALL -Add profile infra.com", "Create" ,"tbm", "{\"profile\":{\"name\":\"AuditALLDVC\",\"description\":\"test\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Audit\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"infra.com\"]}}","normal","AuditALLDVC"},
				{ "Audit View -Add profile infra.com", "Create" ,"tbm", "{\"profile\":{\"name\":\"AuditViewDVC\",\"description\":\"test\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Audit\":[\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"infra.com\"]}}","normal","AuditViewDVC"},
				{ "Audit Modify -Add profile infra.com", "Create" ,"tbm", "{\"profile\":{\"name\":\"AuditModifyDVC\",\"description\":\"test\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Audit\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"infra.com\"]}}","normal","AuditModifyDVC"},
				{ "Users ALL -Add profile infra.com", "Create" ,"tbm", "{\"profile\":{\"name\":\"UsersallDVC\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"infrautobeatle.com\"]}}","normal","UsersallDVC"},
				{ "Users View -Add profile infra.com", "Create" ,"tbm", "{\"profile\":{\"name\":\"UsersViewDVC\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"infrautobeatle.com\"]}}","normal","UsersViewDVC"},
				{ "single domian -Add profile infra.com", "Create" ,"tbm", "{\"profile\":{\"name\":\"SingleDomianDVC\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"infrautobeatle.com\"]}}","normal","SingleDomianDVC"},
				{ "Multi Domain profile", "Create" ,"tbm",  "{\"profile\":{\"name\":\"MultiDomianDVC\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"infrautobeatle.com\",\"infra.com\"]}}","normal","MultiDomianDVC"},
				{ "All Domain profile", "Create" ,"tbm",  "{\"profile\":{\"name\":\"AllDomainProfile\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","AllDomainProfile"},
//				{ "Multi Domain profile", "add" ,"C2162",  "{\"group\":{\"name\":\"BlrInactivegrp\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","BlrInactivegrp"},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","AuditModify"},
//				
				
			};
		}
		@Test( priority = 2 , dataProvider = "DeleteProfileDataDVC",groups = {"REGRESSION"})
		public void DeleteProfileDVC(String description,String action,String id,String type,String name) throws Exception {
			Reporter.log("exeuting testcase ****CreateProfile() ***** Description"+description,true);
			Reporter.log("group Action "+action+" testcase type "+type,true);
			System.out.println("exeuting testcase ****addGroup() ***** ...");
			// search profile if it already exist 
			HttpResponse response =Infractions.SearchProfile(suiteData, headers);
			String responseBody = ClientUtil.getResponseBody(response);
			
			//Assert the Response data
			assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			Reporter.log("** CreateProfile** Response: ",response.getStatusLine().getStatusCode(),true);
			Reporter.log("** CreateProfile** Response: "+responseBody,true);
			String Pid=null;
			Pid=Infractions.getProfileid(responseBody, name );//Infrautils.SearchUserInGrp(responseBody, "profiles", "name", name, "id");
			if((null != Pid)&&(!Pid.isEmpty()))
			{
				//profile  exist 
				//proceed for deletion request
				HttpResponse Response=Infractions.deleteProfile(suiteData, headers, Pid);
				Thread.sleep(3000);
				String ResponseBody = ClientUtil.getResponseBody(Response);
				//Assert the Response data
				String action_status =(String) new JSONObject(ResponseBody).get("message").toString();
				assertEquals(action_status,"success", "group data verification failed");
				// cross check added profile
				String uriReSearch= suiteData.getReferer()+"admin/profiles/list?order_by=name";
				URI dataUriReSearc = ClientUtil.BuidURI(uriReSearch);
				HttpResponse SearchResponse =  restClient.doGet(dataUriReSearc, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
				Thread.sleep(3000);
				String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
				//Assert the Response data
				String  resource_uri=null;
				resource_uri=Infrautils.SearchUserInGrp(SearchResponseBody, "profiles", "name", name, "resource_uri");
				Reporter.log("** profile added with resource_uri : "+resource_uri,true);
				
			    String action_status1 =(String) new JSONObject(SearchResponseBody).get("action_status").toString();
				Reporter.log("Expected test result "+null,true);
				Reporter.log("Actual test result "+resource_uri,true);
				assertEquals(action_status1,"success", "profile data verification failed");
				assertEquals(null,resource_uri, "profile data verification failed");
				
			
			}
			else
			{
				// profile already exit
				
				Reporter.log("Profile not present ",true);
				
			}
			
			Reporter.log("Test execution is complete ",true);
			
		}
		

		@DataProvider
		public Object[][] DeleteProfileDataDVC() {
			return new Object[][]{
				// type    		id	   		value         
				//{ "Add profile", "add", "C2129", "normal","Dropbox"},
				{ "Audit ALL -Add profile infra.com", "add" ,"tbm","normal","AuditALLDVC"},
				{ "Audit View -Add profile infra.com", "add" ,"tbm", "normal","AuditViewDVC"},
				{ "Audit Modify -Add profile infra.com", "add" ,"tbm", "normal","AuditModifyDVC"},
				{ "Users ALL -Add profile infra.com", "add" ,"tbm", "normal","UsersallDVC"},
				{ "Users ALL -Add profile infra.com", "add" ,"tbm", "normal","UsersallDVC"},
				
			};
		}
		/*Author: Vijay Gangwar
		*todo: CreateProfile  
		*params dataprovider
		*/
		
		 @Test( priority = 1 , dataProvider = "profileData",groups = {"REGRESSION","CEP"})
			public void CreateProfile(String description,String action,String id,String payload,String type,String name) throws Exception {
				Reporter.log("exeuting testcase ****CreateProfile() ***** Description"+description,true);
				Reporter.log("group Action  "+action+" testcase type  "+type,true);
				// search profile

				String resource_uri=null;
				resource_uri=Infractions.getProfileResourceId(suiteData, headers, name);//Infrautils.SearchUserInGrp(responseBody, "profiles", "name", name, "resource_uri");
				if((resource_uri.isEmpty()))
				{
//					// profile already exit
//					//HttpResponse response1=Infractions.deleteUser(suiteData, headers, userId);
//					HttpResponse response =Infractions.SearchProfile(suiteData, headers);
//					String responseBody = ClientUtil.getResponseBody(response);
//					//Assert the Response data
//					//assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
//					Reporter.log("** CreateProfile** Response: ",response.getStatusLine().getStatusCode(),true);
//				//	Reporter.log("** CreateProfile** Response: "+responseBody,true);
//					String Pid=null;
//					Pid=Infractions.getProfileid(responseBody, name );
//					Reporter.log("deleting old exixting profile",true);
//					HttpResponse Response=Infractions.deleteProfile(suiteData, headers, Pid);
//					Thread.sleep(3000);
//					
//					Reporter.log("Profile already present ",true);
				
				
					
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
				}
				else
				{
					// do nothing
					Reporter.log("profile is already present",true);
				}
				
				Reporter.log("Test execution is complete ",true);
			}
			

			@DataProvider
			public Object[][] profileData() {
				return new Object[][]{
					// type    		id	   		value         
				//	{ "Securlets all OneDrive Profile All", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllOnedriveAll\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllOnedriveAll"},
					{ "Dashboard Securlet all and box profile", "Create", "tbm", "{\"profile\":{\"name\":\"Dropbox\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","Dropbox"},
					{ "Dashboard Securlet all and Dropbox View profile", "Create", "tbm", "{\"profile\":{\"name\":\"DropboxView\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","DropboxView"},
					{ "Dashboard Securlet all and Dropbox Modify profile", "Create", "tbm", "{\"profile\":{\"name\":\"DropboxModify\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","DropboxModify"},
					{ "Dashboard Securlet all and Dropbox ExportImport profile", "Create", "tbm", "{\"profile\":{\"name\":\"DropboxEXIM\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","DropboxEXIM"},
					{ "Dashboard Securlet all and Dropbox Export profile", "Create", "tbm", "{\"profile\":{\"name\":\"DropboxExport\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","DropboxExport"},
					{ "Dashboard Securlet all and Dropbox Import profile", "Create", "tbm", "{\"profile\":{\"name\":\"DropboxImport\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","DropboxImport"},
					{ "Audit ALL -Add profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"AuditALL\",\"description\":\"test\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Audit\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","AuditALL"},
					{ "Audit View -Add profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"AuditView\",\"description\":\"test\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Audit\":[\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","AuditView"},
					{ "Audit Modify -Add profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"AuditModify\",\"description\":\"test\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Audit\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","AuditModify"},
					{ "Users ALL -Add profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"Usersall\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","Usersall"},
					{ "Users View -Add profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"UsersView\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","UsersView"},
					{ "duplicate profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"Usersall\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","error","Usersall"},
					{ "User Export Import Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"UserExIm\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","UserExIm"},
					{ "User Export Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"UserExport\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"EXPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","UserExport"},
					{ "User import Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"UserImport\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"IMPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","UserExport"},
					{ "DashBoard Modify Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"DBModify\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\",\"MODIFY\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","DBModify"},
					{ "DashBoard View only Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"DBViewOnly\",\"description\":\"users\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","DBViewOnly"},
					{ "Securlets all Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletBoxAll\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletBoxAll"},
					{ "Inactive Securlets all Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletBoxInactive\",\"description\":\"\",\"is_active\":false,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletBoxInactive"},
					{ "Securlets View only Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletBoxView\",\"description\":\"\",\"is_active\":false,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletBoxView"},
					{ "Securlets Modify only Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletModify\",\"description\":\"\",\"is_active\":false,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"VIEW\",\"MODIFY\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletBoxView"},
					{ "Securlets all file sharing saas apps Profile ", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllSaaS\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Office 365\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Box\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Salesforce\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Google Drive\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Zendesk\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Amazon Web Services\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllSaaS"},
					{ "Securlets all file sharing saas apps Profile Modify", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllSaaSModify\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"MODIFY\",\"VIEW\"],\"Office 365\":[\"MODIFY\",\"VIEW\"],\"Dropbox\":[\"MODIFY\",\"VIEW\"],\"Box\":[\"MODIFY\",\"VIEW\"],\"Salesforce\":[\"MODIFY\",\"VIEW\"],\"Google Drive\":[\"MODIFY\",\"VIEW\"],\"Zendesk\":[\"MODIFY\",\"VIEW\"],\"Amazon Web Services\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllSaaS"},
					{ "Securlets all file sharing saas apps Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllSaaSView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"VIEW\"],\"Office 365\":[\"VIEW\"],\"Dropbox\":[\"VIEW\"],\"Box\":[\"VIEW\"],\"Salesforce\":[\"VIEW\"],\"Google Drive\":[\"VIEW\"],\"Zendesk\":[\"VIEW\"],\"Amazon Web Services\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllSaaS"},
					{ "Securlets all Box all", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllBoxAll\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllBoxAll"},
					{ "Securlets all Box Modify", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllBoxModify\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllBoxModify"},
					{ "Securlets all Box ViewOonly", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllBoxViewOonly\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllBoxViewOonly"},
					{ "Securlets Modify Box View", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllBoxViewReport\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllBoxViewReport"},
					{ "Securlets Modify Box Modify", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletModifyBoxModify\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllBoxAll"},
					{ "Securlets all OneDrive Profile All", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllOnedriveAll\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllOnedriveAll"},
					{ "Securlets all OneDrive Profile Modify", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllOnedriveModify\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllOnedriveModify"},
					{ "Securlets all OneDrive Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllOnedriveView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllOnedriveView"},
					{ "Securlets modify OneDrive Profile Modify", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletModifyOnedriveModify\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletModifyOnedriveModify"},
					{ "Store modify box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"StoreModifyBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Store\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","StoreModifyBoxView"},
					{ "Store View box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"StoreViewBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Store\":[\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","StoreModifyBoxView"},
					{ "Detect all  box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"DetectALLBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Detect\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","DetectALLBoxView"},
					{ "Detect Modify  box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"DetectModifyBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Detect\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","DetectModifyBoxView"},
					{ "Detect View box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"DetectViewBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Detect\":[\"VIEW\"]},\"permitted_services\":{\"Box\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","DetectViewBoxView"},
					{ "Detect ViewReport box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"DetectReportBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Detect\":[\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","DetectReportBoxView"},
					{ "Investigate ViewAll box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"InvestigateAllBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Investigate\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","InvestigateAllBoxView"},
					{ "Investigate ViewExIm box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"InvestigateExImBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Investigate\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","InvestigateExImBoxView"},
					{ "Investigate Modify box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"InvestigateModifyBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Investigate\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","InvestigateModifyBoxView"},
					{ "Investigate_Securlet Modify box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"InvestigateModifyBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"MODIFY\",\"VIEW\"],\"Investigate\":[\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","InvestigateModifyBoxView"},
					{ "Protect securlet Modify box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"ProtectAllSeculetModifyBoxModify\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"MODIFY\",\"VIEW\"],\"Protect\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","ProtectAllSeculetModifyBoxModify"},
					{ "Protect  all box Profile View", "Create" ,"tbm", "{\"profile\":{\"name\":\"ProtectAllBoxModify\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Protect\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","ProtectAllBoxModify"},
					//		{ "duplicate profile", "Create" ,"tbm",  "{\"profile\":{\"name\":\"Dropbox\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","Error","Dropbox"},
//					{ "Active inactive mix", "add" ,"C2162",  "{\"group\":{\"name\":\"BlrInactivegrp\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","BlrInactivegrp"},
//					
					
				};
			}
			/*Author: Vijay Gangwar
			*Desc: CreateProfile  P1 test 
			*params dataprovider
			*/
			
			/*Author: Vijay Gangwar
			*todo: CreateProfile  
			*params dataprovider
			*/
			
			 @Test( priority = 1 , dataProvider = "EditData",groups={"P1","EOE","REGRESSION","CEP"})
				public void EditProfile(String description,String action,String id,String payload,String Editpayload,String type,String name) throws Exception {
					Reporter.log("exeuting testcase ****CreateProfile() ***** Description"+description,true);
					Reporter.log("group Action  "+action+" testcase type  "+type,true);
					// search profile

					String resource_uri="";
					String Pid=null;
					resource_uri=Infractions.getProfileResourceId(suiteData, headers, name);//Infrautils.SearchUserInGrp(responseBody, "profiles", "name", name, "resource_uri");
					if((resource_uri.isEmpty()))
					{
						//create uri for add profile request
						HttpResponse addResponse=Infractions.createProfile( suiteData,headers, payload);
						String addResponseBody = ClientUtil.getResponseBody(addResponse);
						Reporter.log("Create profile response log :"+addResponseBody,true);
						//Assert the Response data
						String action_status =(String) new JSONObject(addResponseBody).get("action_status").toString();
						//assertEquals(action_status,"success", "Profile data verification failed");
					}
					else
					{
						// do nothing
						Reporter.log("profile is already present",true);
					}
					
					// search added profile
					HttpResponse SearchResponse =  Infractions.SearchProfile(suiteData, headers);//restClient.doGet(dataUriReSearc, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
					Thread.sleep(4000);
					String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
					Pid=Infractions.getProfileid(SearchResponseBody, name );
					//edit the profile
					{
						URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), "/admin/profiles/edit");
						//Reporter.log("Request Method type: POST",true);
						Reporter.log("Request API :"+payload,true);
						Editpayload=Editpayload.replace("dummy", Pid);
						//execute  request
						HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(Editpayload));
						//return response;
					}
					https://eoe.elastica-inc.com/admin/profiles/edit
					
					Reporter.log("Test execution is complete ",true);
				}
				
			 @DataProvider
				public Object[][] EditData() {
					return new Object[][]{
						// type    		id	   		value         
					//	{ "Securlets all OneDrive Profile All", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllOnedriveAll\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllOnedriveAll"},
						{ "Create the profile for performing edit operations", "Create", "C1643607", "{\"profile\":{\"name\":\"EditProfile\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"IMPORT\",\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"_EL_CAT_\":{\"Accounting\":[\"VIEW\",\"MODIFY\",\"IMPORT\",\"EXPORT\",\"REPORT\"]},\"Xero\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"Detect\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Securlets\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Investigate\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Audit\":{\"all\":false,\"levels\":[\"Not anonymized\"]}},\"global_settings\":{},\"permitted_domains\":[\"infrabeatle.com\"],\"org_unit\":[]}}",
							"{\"profile\":{\"id\":\"dummy\",\"name\":\"EditProfile\",\"is_active\":true,\"notes\":\"\",\"description\":\"\",\"permitted_apps\":{\"Dashboard\":[\"IMPORT\",\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Xero\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"],\"_EL_CAT_\":{\"Accounting\":[\"VIEW\",\"MODIFY\",\"IMPORT\",\"EXPORT\",\"REPORT\"]}},\"information_level\":{\"Audit\":{\"all\":false,\"levels\":[\"Not anonymized\"]},\"Investigate\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Detect\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Securlets\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]}},\"global_settings\":{},\"permitted_domains\":[\"infra.com\"],\"org_unit\":[\"__ALL_EL__\"]}}","normal","EditProfile"},
				
						{ "edit the  profile to all domains", "edit", "C1643607", "only edit actions",
								"{\"profile\":{\"id\":\"dummy\",\"name\":\"EditProfile\",\"is_active\":true,\"notes\":\"\",\"description\":\"\",\"permitted_apps\":{\"Dashboard\":[\"IMPORT\",\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Xero\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"],\"_EL_CAT_\":{\"Accounting\":[\"VIEW\",\"MODIFY\",\"IMPORT\",\"EXPORT\",\"REPORT\"]}},\"information_level\":{\"Audit\":{\"all\":false,\"levels\":[\"Not anonymized\"]},\"Investigate\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Detect\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Securlets\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]}},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"],\"org_unit\":[\"__ALL_EL__\"]}}","normal","EditProfile"},

						{ "edit the  profile to selective information level", "edit", "C1643607", "only edit actions",
									"{\"profile\":{\"id\":\"dummy\",\"name\":\"EditProfile\",\"is_active\":true,\"notes\":\"\",\"description\":\"\",\"permitted_apps\":{\"Dashboard\":[\"IMPORT\",\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Xero\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"],\"_EL_CAT_\":{\"Accounting\":[\"VIEW\",\"MODIFY\",\"IMPORT\",\"EXPORT\",\"REPORT\"]}},\"information_level\":{\"Audit\":{\"all\":false,\"levels\":[\"Not anonymized\"]},\"Investigate\":{\"all\":false,\"levels\":[\"WARNING\"]},\"Detect\":{\"all\":false,\"levels\":[\"MEDIUM\"]},\"Securlets\":{\"all\":false,\"levels\":[\"EXTERNAL\"]}},\"global_settings\":{},\"permitted_domains\":[\"infra.com\"],\"org_unit\":[\"__ALL_EL__\"]}}","normal","EditProfile"},


					};
				}
		
			
			
			
			@Test( priority = 1 , dataProvider = "CreateProfileDataP1",groups = { "P1","EOE","SANITY" })
			public void CreateProfileP1(String description,String action,String id,String payload,String type,String name) throws Exception {
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
				public Object[][] CreateProfileDataP1() {
					return new Object[][]{
						// type    		id	   		value         
				
						{ "Create and Delet securlet profile", "Create", "tbm", "{\"profile\":{\"name\":\"Createdropboxp1\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","Createdropboxp1"},
								
					};
				}

		
			
			@Test( priority = 3 , dataProvider = "DeleteData",groups = {"P1"})
			public void DeleteProfile(String description,String action,String id,String type,String name) throws Exception {
				Reporter.log("exeuting testcase ****CreateProfile() ***** Description"+description,true);
				Reporter.log("group Action "+action+" testcase type "+type,true);
				System.out.println("exeuting testcase ****addGroup() ***** ...");
				// search the profile . If it exist

				HttpResponse response =Infractions.SearchProfile(suiteData, headers);
				String responseBody = ClientUtil.getResponseBody(response);
				//Assert the Response data
				assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				Reporter.log("** CreateProfile** Response: ",response.getStatusLine().getStatusCode(),true);
				Reporter.log("** CreateProfile** Response: "+responseBody,true);
				String Pid=null;
				Pid=Infractions.getProfileid(responseBody, name );//Infrautils.SearchUserInGrp(responseBody, "profiles", "name", name, "id");
				if(!Pid.isEmpty())
				{
					//profile  exist 
					//proceed for deletion request				
					HttpResponse Response=Infractions.deleteProfile(suiteData, headers, Pid);
					Thread.sleep(3000);
					String ResponseBody = ClientUtil.getResponseBody(Response);
					//Assert the Response data
					if(type.contains("error"))
					{
						System.out.println(" ResponseBody for error case..."+ResponseBody);
						String action_status =(String) new JSONObject(ResponseBody).get("message").toString();
						Reporter.log(" Response::: "+action_status,true);
						assertEquals(action_status,"Can not delete this Profile, It has assigned users.", "group data verification failed");
					}
					else
					{
						String action_status =(String) new JSONObject(ResponseBody).get("message").toString();
						assertEquals(action_status,"success", "group data verification failed");
						// cross check added profile
						String uriReSearch= suiteData.getReferer()+"/admin/profiles/list?order_by=name";
						URI dataUriReSearc = ClientUtil.BuidURI(uriReSearch);
						HttpResponse SearchResponse =  restClient.doGet(dataUriReSearc, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
						Thread.sleep(3000);
						String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
						//Assert the Response data
						String  resource_uri=null;
						String SearchedResource_uri=Infrautils.SearchUserInGrp(SearchResponseBody, "profiles", "name", name, "resource_uri");
						Reporter.log("** profile added with resource_uri : "+resource_uri,true);
						
					    String action_status1 =(String) new JSONObject(SearchResponseBody).get("action_status").toString();
						Reporter.log("Expected test result "+null,true);
						Reporter.log("Actual test result "+SearchedResource_uri,true);
						assertEquals(action_status1,"success", "profile data verification failed");
						assertEquals(SearchedResource_uri,null, "profile data verification failed");
					}
				}
				else
				{
					// profile already exit
					
					Reporter.log("Profile not present ",true);
					
				}
				
				Reporter.log("Test execution is complete ",true);
				
			}
			

			@DataProvider
			public Object[][] DeleteData() {
				return new Object[][]{
					// type    		id	   		value         
					{ "Add profile", "add", "C2129", "normal","Dropbox"},
					//{ "Audit ALL -Add profile infra.com", "add" ,"tbm","normal","AuditALL"},
					{ "Audit View -Add profile infra.com", "add" ,"tbm", "normal","AuditView"},
					{ "Audit Modify -Add profile infra.com", "add" ,"tbm", "normal","AuditModify"},
					{ "Delete profile - user is vijay.gangwar+80@infrabeatle.com", "error" ,"C416154", "error","Box"},
					{ "Delete profile", "add", "C2129", "error","BoxInfra"},
					
					
				};
			}
		/*
		 * test for null/blank pid
		 */
			
		//	@Test( priority = 1 )
			public void ADeleteProfile() throws Exception {
				//Reporter.log("exeuting testcase ****CreateProfile() ***** Description"+description,true);
				//Reporter.log("group Action "+action+" testcase type "+type,true);
				System.out.println("exeuting testcase ****addGroup() ***** ...");
				// search the profile . If it exist

				String pid ="";
					//profile  exist 
					//proceed for deletion request				
					HttpResponse Response=Infractions.deleteProfile(suiteData, headers, pid);
					Thread.sleep(3000);
					String ResponseBody = ClientUtil.getResponseBody(Response);
					//Assert the Response data
					assertEquals(Response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
					Reporter.log("** CreateProfile** Response: ",Response.getStatusLine().getStatusCode(),true);
					
				
				
				Reporter.log("Test execution is complete ",true);
				
			}	
			/*sys admin create policy
			 * 
			 */
			@Test( priority = 3,groups={"P1"} )
			public void createdByFilter() throws Exception {
				
				
				// search profile
				String admin="admin@infrabeatle.com";
				Reporter.log("search profile ",true);
				List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
				queryParams.add(new BasicNameValuePair("filters", "created_by%7Cadmin@infrabeatle.com"));
			//	queryParams.add(new BasicNameValuePair("offset", "0"));
				queryParams.add(new BasicNameValuePair("order_by", "name"));
				//queryParams.add(new BasicNameValuePair("query", name));
				URI dataUri1 = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), "/admin/profiles/list/",queryParams);
				//restClient = new Client();
				HttpResponse response =  restClient.doGet(dataUri1, headers);
				String ResponseBody = ClientUtil.getResponseBody(response);
				// search the profile . If it exist
				
				//validate the filter values for deletion request
				JSONArray	summaryObject = (JSONArray) new JSONObject(ResponseBody).getJSONArray("profiles");
				String action_status =(String) new JSONObject(ResponseBody).get("action_status").toString();
			
				 assertEquals(action_status,"success", "Response code verification failed");
				 Reporter.log(" profile filter api action status."+action_status,true);
				
				 String Id=null;
					for(int i=0; i<summaryObject.length(); i++)
					{						
						JSONObject userObj=summaryObject.getJSONObject(i);
						{
							// match values for admin@infrabeatle.com	
							
								String Fvalue=userObj.get("created_by").toString();
								Reporter.log("expected value :"+admin+" Retrived value"+Fvalue,true);
								if(Fvalue.contains(admin))
								{
									assertEquals(admin,Fvalue ," created by filter value Validation failed");
								}
								else
								{
									// match values for admin@infrabeatle.com	
								}
								
						}					
					}
					//Reporter.log("userId...."+Id,true);	


				Reporter.log("Test execution is complete ",true);

			}

			/*sys admin create policy
			 * 
			 */
			@Test( priority = 3,groups={"P1"}, dataProvider="ProfileFilterData")
			public void profileFilterTest(String filter,String filterName,String value) throws Exception {
				// search profile
				//String admin="admin@infrabeatle.com";
				Reporter.log("search profile ",true);
				List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
				queryParams.add(new BasicNameValuePair("filters", "created_by"));
				queryParams.add(new BasicNameValuePair("order_by", "name"));
				// access profile option should not be accessible to normal admin.
				//filters=created_by%7Cadmin@infrabeatle.com&order_by=name
				//https://eoe.elastica-inc.com/admin/profiles/list?filters=created_by%7Cadmin@infrabeatle.com&order_by=name
				String uri =suiteData.getReferer()+InfraConstants.LIST_PROFILE+filterName+"%7C"+value+"&order_by=name";
				URI dataUri1 =  ClientUtil.BuidURI(uri);ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), "/admin/profiles/list?",queryParams);
				//restClient = new Client();
				HttpResponse response =  restClient.doGet(dataUri1, headers);
				String ResponseBody = ClientUtil.getResponseBody(response);
				// search the profile . If it exist
				Reporter.log("search profile "+ResponseBody,true);
				JSONArray summaryObject=null;
				 summaryObject=(JSONArray)new JSONObject(ResponseBody).getJSONArray("profiles");
				//JSONObject jsobj1= new JSONObject(jsobj).getJSONObject("filters");
						//.getJSONArray("access_profiles");
				//summaryObject = (JSONArray) new JSONObject(jsobj1).getJSONArray("access_profiles");
				//String action_status =(String) new JSONObject(ResponseBody).get("action_status").toString();
				boolean Fvalue=false;
				//String value="";
				for(int i=0; i<summaryObject.length(); i++)
				{						
					JSONObject userObj=summaryObject.getJSONObject(i);
					{
						
							String getvalue=userObj.get(filterName).toString();
							if(getvalue.contains(value))
							{
								Fvalue=true;
							}
							else
							{
								Fvalue=false;
							}
							
						}
					Reporter.log("fiterd created by  value :"+value,true);
					assertEquals(true,Fvalue ," filter  Validation failed");
					Reporter.log("Expected test result :true",true);
					Reporter.log("Actual test result :"+Fvalue,true);
					Reporter.log("Test execution is complete ",true);
						
						
					}					
									
			
			}

			@DataProvider
			public Object[][] ProfileFilterData() {
				return new Object[][]{
					// type    		id	   		value         
				//	{ "list profile created by sys admin", "admin@infrabeatle.com","access_profiles" },
					{ "list profile created by admin admin","created_by","admin@infrabeatle.com" },
					{ "list profile created by admin admin","created_by","System" },
					{ "list profile created by admin admin","is_active","true" },
					{ "list profile created by admin admin","is_active","false" },
				//	{ "Create and Delet DB and dropbox profile", "Create", "tbm", "","normal",""},
							
				};
			}
			
			/*sys admin filter in combination
			 * 
			 */
			@Test( priority = 3,groups={"P1"}, dataProvider="ProfileFilterCombData")
			public void profileCombinationFilter(String filter,String filterName,String value,String filterName1,String value1) throws Exception {
				// search profile
				//String admin="admin@infrabeatle.com";
				Reporter.log("search profile ",true);
				List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
				queryParams.add(new BasicNameValuePair("filters", "created_by"));
				queryParams.add(new BasicNameValuePair("order_by", "name"));
				// access profile option should not be accessible to normal admin.
				String uri =suiteData.getReferer()+InfraConstants.LIST_PROFILE+filterName1+"%7C"+value1+"%7C%7C"+filterName+"%7C"+value+"&order_by=name";
				URI dataUri1 =  ClientUtil.BuidURI(uri);ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), "/admin/profiles/list?",queryParams);
				//restClient = new Client();
				HttpResponse response =  restClient.doGet(dataUri1, headers);
				String ResponseBody = ClientUtil.getResponseBody(response);
				// search the profile . If it exist
				Reporter.log("search profile "+ResponseBody,true);
				JSONArray summaryObject=null;
				 summaryObject=(JSONArray)new JSONObject(ResponseBody).getJSONArray("profiles");
				//JSONObject jsobj1= new JSONObject(jsobj).getJSONObject("filters");
						//.getJSONArray("access_profiles");
				//summaryObject = (JSONArray) new JSONObject(jsobj1).getJSONArray("access_profiles");
				//String action_status =(String) new JSONObject(ResponseBody).get("action_status").toString();
				boolean Fvalue=false;
				//String value="";
				for(int i=0; i<summaryObject.length(); i++)
				{						
					JSONObject userObj=summaryObject.getJSONObject(i);
					{
						
							String getvalue=userObj.get(filterName).toString();
							String getvalue1=userObj.get(filterName1).toString();
							if(getvalue.contains(value)&&getvalue1.contains(value1))
							{
								Fvalue=true;
							}
							else
							{
								Fvalue=false;
							}
							
						}
					Reporter.log("fiterd created by  value :"+value,true);
					assertEquals(true,Fvalue ," filter  Validation failed");
					Reporter.log("Expected test result :true",true);
					Reporter.log("Actual test result :"+Fvalue,true);
					Reporter.log("Test execution is complete ",true);
						
						
					}					
									
			
			}

			@DataProvider
			public Object[][] ProfileFilterCombData() {
				return new Object[][]{
					// type    		id	   		value         
					{ "list profile created by admin admin","created_by","System","is_active","true" },
					{ "list profile created by admin admin","created_by","admin@infrabeatle.com","is_active","true" },
					{ "list profile created by admin admin","created_by","admin@infrabeatle.com","is_active","false" },
					
				};
			}
			
			 @Test( priority = 1 , dataProvider = "CreateDeleteProfileData",groups = {"REGRESSION"})
				public void DeleteCreateProfile(String description,String action,String id,String payload,String type,String name) throws Exception {
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
				public Object[][] CreateDeleteProfileData() {
					return new Object[][]{
						// type    		id	   		value         
					//	{ "Securlets all OneDrive Profile All", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllOnedriveAll\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllOnedriveAll"},
						{ "Create and Delet securlet profile", "Create", "C416155", "{\"profile\":{\"name\":\"Createdeldropbox\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","Createdeldropbox"},
						{ "Create and Delet DB and dropbox profile", "Create", "tbm", "{\"profile\":{\"name\":\"Createdelbox\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","Createdelbox"},
								
					};
				}

				
			/*Author: Vijay Gangwar
			*todo: Creat admin with the corresponding profile
			*params: dataprovider
			*/
			
		
			@Test( priority = 2 , dataProvider = "AdminData",groups = {"REGRESSION","CEP"})
			public void CreateAdmin(String userType,String email,String description,String profileName,String id,String payload,String type) throws Exception {
			Reporter.log("exeuting testcase ****CreateProfile() ***** Test rail"+id,true);
			Reporter.log("group Action  "+description+" testcase type  "+type,true);
			String SearcheduserId=null;
			
			//1. search the Admin to be created
			//2. Search the profile to be created
			//3. send post  request  add admin 
			//4 verify if user is created.
			
			//search profile to be used for admin creation
			HttpResponse response =  Infractions.SearchProfile(suiteData, headers);//restClient.doGet(dataUriSearc, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
			Thread.sleep(3000);
			String responseBody = ClientUtil.getResponseBody(response);
			//Assert the Response data
			String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
			assertEquals(action_status,"success", "Api status verification failed");
//			Reporter.log("** CreateProfile** Response: "+responseBody,true);
			String profileId=null;
			profileId=Infractions.getProfileid(responseBody, profileName);//SearchProfile(suiteDatadto, headers);
			// if profile id is present then create Admin			
			if(!profileId.isEmpty())
			{
				// search if admin is present with the given email
				String userId=null;				
				userId=Infractions.searchUserid(suiteData, headers, email);
				if(userId.isEmpty())
				{
					// create Admin if userId is empty
					HttpResponse response1=Infractions.createAdmin(suiteData, headers, payload, profileId);
					String responseBody1= ClientUtil.getResponseBody(response1);
					//Reporter.log("response1 :"+response1,true);
					System.out.println("Response: "+responseBody1);
					String action_status1 =(String) new JSONObject(responseBody1).get("action_status").toString();
					assertEquals(action_status,"success", "Api status verification failed");
					Reporter.log(" Expected Result:success",true);
					Reporter.log(" Actual Result first name: "+action_status1,true);
					
					SearcheduserId=Infractions.searchUserid(suiteData, headers, email);//Infrautils.getUserid(reSearchResponseBody,"userslist","email",email);
					
					Reporter.log(" New Admin created with id :"+SearcheduserId,true);
					
					Reporter.log(" Expected Result:user id",true);
					Reporter.log(" Actual Result first name: "+SearcheduserId,true);
				}
				else
				{
					Reporter.log(" admin aready present with id: "+userId,true);
				}
					Reporter.log(" Test execution is completed",true);
				
			}
			else
			{
				//profile not present
				Reporter.log(" Access profile not present",true);
				Reporter.log(" Test execution is completed",true);
			}
	 }							
			@DataProvider
			public Object[][] AdminData() {
		//            Admin type 		 emailid 						description	access profile	testrail id,	 payload							Testcasetype
				return new Object[][]{
					{ "Box Admin user","vijay.gangwar+80@infrabeatle.com", "Create admin with Box pofile name","Box", "TBM", "{\"user\":{\"first_name\":\"Dropbox\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+80@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "Dropbox Admin user","vijay.gangwar+81@infrabeatle.com", "Create admin with Dropbox pofile name","DropboxView", "TBM", "{\"user\":{\"first_name\":\"DropboxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+81@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "Dropbox Admin user","vijay.gangwar+82@infrabeatle.com", "Create admin with Dropbox pofile name","DropboxModify", "TBM", "{\"user\":{\"first_name\":\"DropboxModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+82@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "Dropbox Admin user","vijay.gangwar+83@infrabeatle.com", "Create admin with Dropbox pofile name","DropboxEXIM", "TBM", "{\"user\":{\"first_name\":\"DropboxEXIM\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+83@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "Dropbox Admin user","vijay.gangwar+84@infrabeatle.com", "Create admin with Dropbox pofile name","DropboxExport", "TBM", "{\"user\":{\"first_name\":\"DropboxExport\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+84@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "Dropbox Admin user","vijay.gangwar+85@infrabeatle.com", "Create admin with Dropbox pofile name","DropboxImport", "TBM", "{\"user\":{\"first_name\":\"DropboxImport\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+85@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "Audit Admin user","vijay.gangwar+86@infrabeatle.com", "Create admin with AuditALL pofile name","AuditALL", "TBM", "{\"user\":{\"first_name\":\"AuditALL\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+86@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "Users Admin user","vijay.gangwar+87@infrabeatle.com", "Create admin with Usersall pofile name","Usersall", "TBM", "{\"user\":{\"first_name\":\"Usersall\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+87@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "Users Admin user","vijay.gangwar+88@infrabeatle.com", "Create admin with UsersView pofile name","UsersView", "TBM", "{\"user\":{\"first_name\":\"UsersView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+88@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "UserExIm Admin user","vijay.gangwar+89@infrabeatle.com", "Create admin with UserExIm pofile name","UserExIm", "TBM", "{\"user\":{\"first_name\":\"UserExIm\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+89@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "UserExport Admin user","vijay.gangwar+90@infrabeatle.com", "Create admin with UserExport pofile name","UserExport", "TBM", "{\"user\":{\"first_name\":\"UserExport\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+90@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "UserImport Admin user","vijay.gangwar+91@infrabeatle.com", "Create admin with UserImport pofile name","UserImport", "TBM", "{\"user\":{\"first_name\":\"UserImport\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+91@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "DBModify Admin user","vijay.gangwar+92@infrabeatle.com", "Create admin with DBModify pofile name","DBModify", "TBM", "{\"user\":{\"first_name\":\"DBModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+92@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "DBViewOnly Admin user","vijay.gangwar+93@infrabeatle.com", "Create admin with DBViewOnly pofile name","DBViewOnly", "TBM", "{\"user\":{\"first_name\":\"DBViewOnly\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+93@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletBoxAll Admin user","vijay.gangwar+94@infrabeatle.com", "Create admin with SecurletBoxAll pofile name","SecurletBoxAll", "TBM", "{\"user\":{\"first_name\":\"SecurletBoxAll\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+94@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletBoxInactive Admin user","vijay.gangwar+95@infrabeatle.com", "Create admin with SecurletBoxInactive pofile name","SecurletBoxInactive", "TBM", "{\"user\":{\"first_name\":\"SecurletBoxInactive\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+95@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					//{ "Box Admin user","vijay.gangwar+84@infrabeatle.com", "Create admin with Box pofile name","DropboxImport", "TBM", "{\"user\":{\"first_name\":\"DropboxImport\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+85@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletBoxView Admin user","vijay.gangwar+96@infrabeatle.com", "Create admin with SecurletBoxView pofile name","SecurletBoxView", "TBM", "{\"user\":{\"first_name\":\"SecurletBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+96@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletModify Admin user","vijay.gangwar+97@infrabeatle.com", "Create admin with SecurletModify pofile name","SecurletModify", "TBM", "{\"user\":{\"first_name\":\"SecurletModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+97@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllSaaS Admin user","vijay.gangwar+98@infrabeatle.com", "Create admin with SecurletAllSaaS pofile name","SecurletAllSaaS", "TBM", "{\"user\":{\"first_name\":\"SecurletAllSaaS\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+98@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllSaaSModify Admin user","vijay.gangwar+99@infrabeatle.com", "Create admin with SecurletAllSaaSModify pofile name","SecurletAllSaaSModify", "TBM", "{\"user\":{\"first_name\":\"SecurletAllSaaSModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+99@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllSaaSView Admin user","vijay.gangwar+100@infrabeatle.com", "Create admin with SecurletAllSaaSView pofile name","SecurletAllSaaSView", "TBM", "{\"user\":{\"first_name\":\"SecurletAllSaaSView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+100@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllBoxAll Admin user","vijay.gangwar+101@infrabeatle.com", "Create admin with SecurletAllBoxAll pofile name","SecurletAllBoxAll", "TBM", "{\"user\":{\"first_name\":\"SecurletAllBoxAll\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+101@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllBoxModify Admin user","vijay.gangwar+102@infrabeatle.com", "Create admin with SecurletAllBoxModify pofile name","SecurletAllBoxModify", "TBM", "{\"user\":{\"first_name\":\"SecurletAllBoxModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+102@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllBoxViewOonly Admin user","vijay.gangwar+103@infrabeatle.com", "Create admin with SecurletAllBoxViewOonly pofile name","SecurletAllBoxViewOonly", "TBM", "{\"user\":{\"first_name\":\"SecurletAllBoxViewOonly\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+103@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllBoxViewReport Admin user","vijay.gangwar+104@infrabeatle.com", "Create admin with SecurletAllBoxViewReport pofile name","SecurletAllBoxViewReport", "TBM", "{\"user\":{\"first_name\":\"SecurletAllBoxViewReport\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+104@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletModifyBoxModify Admin user","vijay.gangwar+105@infrabeatle.com", "Create admin with SecurletModifyBoxModify pofile name","SecurletModifyBoxModify", "TBM", "{\"user\":{\"first_name\":\"SecurletModifyBoxModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+105@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllOnedriveAll Admin user","vijay.gangwar+106@infrabeatle.com", "Create admin with SecurletAllOnedriveAll pofile name","SecurletAllOnedriveAll", "TBM", "{\"user\":{\"first_name\":\"SecurletAllOnedriveAll\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+106@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllOnedriveModify Admin user","vijay.gangwar+107@infrabeatle.com", "Create admin with SecurletAllOnedriveModify pofile name","SecurletAllOnedriveModify", "TBM", "{\"user\":{\"first_name\":\"SecurletAllOnedriveModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+107@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletAllOnedriveView Admin user","vijay.gangwar+108@infrabeatle.com", "Create admin with SecurletAllOnedriveView pofile name","SecurletAllOnedriveView", "TBM", "{\"user\":{\"first_name\":\"SecurletAllOnedriveView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+108@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "SecurletModifyOnedriveModify Admin user","vijay.gangwar+109@infrabeatle.com", "Create admin with SecurletModifyOnedriveModify pofile name","SecurletModifyOnedriveModify", "TBM", "{\"user\":{\"first_name\":\"SecurletModifyOnedriveModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+109@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "StoreModifyBoxView Admin user","vijay.gangwar+110@infrabeatle.com", "Create admin with StoreModifyBoxView pofile name","StoreModifyBoxView", "TBM", "{\"user\":{\"first_name\":\"StoreModifyBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+110@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "StoreViewBoxView Admin user","vijay.gangwar+111@infrabeatle.com", "Create admin with StoreViewBoxView pofile name","StoreViewBoxView", "TBM", "{\"user\":{\"first_name\":\"StoreViewBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+111@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "DetectALLBoxView Admin user","vijay.gangwar+112@infrabeatle.com", "Create admin with DetectALLBoxView pofile name","DetectALLBoxView", "TBM", "{\"user\":{\"first_name\":\"DetectALLBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+112@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "DetectModifyBoxView Admin user","vijay.gangwar+113@infrabeatle.com", "Create admin with DetectModifyBoxView pofile name","DetectModifyBoxView", "TBM", "{\"user\":{\"first_name\":\"DetectModifyBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+113@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "DetectViewBoxView Admin user","vijay.gangwar+114@infrabeatle.com", "Create admin with DetectViewBoxView pofile name","DetectViewBoxView", "TBM", "{\"user\":{\"first_name\":\"DetectViewBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+114@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "DetectReportBoxView Admin user","vijay.gangwar+115@infrabeatle.com", "Create admin with DetectReportBoxView pofile name","DetectReportBoxView", "TBM", "{\"user\":{\"first_name\":\"DetectReportBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+115@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "InvestigateAllBoxView Admin user","vijay.gangwar+116@infrabeatle.com", "Create admin with InvestigateAllBoxView pofile name","InvestigateAllBoxView", "TBM", "{\"user\":{\"first_name\":\"InvestigateAllBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+116@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "InvestigateExImBoxView Admin user","vijay.gangwar+117@infrabeatle.com", "Create admin with InvestigateExImBoxView pofile name","InvestigateExImBoxView", "TBM", "{\"user\":{\"first_name\":\"InvestigateExImBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+117@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "InvestigateModifyBoxView Admin user","vijay.gangwar+118@infrabeatle.com", "Create admin with InvestigateModifyBoxView pofile name","InvestigateModifyBoxView", "TBM", "{\"user\":{\"first_name\":\"InvestigateModifyBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+118@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "InvestigateModifyBoxView Admin user","vijay.gangwar+119@infrabeatle.com", "Create admin with InvestigateModifyBoxView pofile name","InvestigateModifyBoxView", "TBM", "{\"user\":{\"first_name\":\"InvestigateModifyBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+119@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "ProtectAllSeculetModifyBoxModify Admin user","vijay.gangwar+120@infrabeatle.com", "Create admin with ProtectAllSeculetModifyBoxModify pofile name","ProtectAllSeculetModifyBoxModify", "TBM", "{\"user\":{\"first_name\":\"ProtectAllSeculetModifyBoxModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+120@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
					{ "ProtectAllBoxModify Admin user","vijay.gangwar+121@infrabeatle.com", "Create admin with ProtectAllBoxModify pofile name","ProtectAllBoxModify", "TBM", "{\"user\":{\"first_name\":\"ProtectAllBoxModify\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+121@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
		//			{ "Box Admin user","vijay.gangwar+99@infrabeatle.com", "Create admin with Box pofile name","DetectModifyBoxView", "TBM", "{\"user\":{\"first_name\":\"DetectModifyBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+84@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
		//			{ "Box Admin user","vijay.gangwar+100@infrabeatle.com", "Create admin with Box pofile name","DetectViewBoxView", "TBM", "{\"user\":{\"first_name\":\"DetectViewBoxView\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+85@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
				
				};
		 }
			
			/*Author: Vijay Gangwar
			*todo: import enduser
			*params none
			*/
			@Test( priority = 2 , dataProvider = "AdminDataRBAC",groups = {"REGRESSION"})
			public void CreateAdminRBAC(String userType,String email,String description,String profileName,String id,String AdminPayload,String ProfilePayload) throws Exception {
			Reporter.log("exeuting testcase ****CreateProfile() ***** Test rail"+id,true);
			//Reporter.log("group Action  "+description+" testcase type  "+type,true);
			Infractions.createAdmin(suiteData, headers,ProfilePayload, AdminPayload, profileName, email);
			
			}
			@DataProvider
			public Object[][] AdminDataRBAC() {
				//          Admin type 		 emailid 						description	access profile	testrail id,	 AdminDataPayload							ProfilePl
				return new Object[][]{
					{ "Detect Admin user","vijay.gangwar+180@infrabeatle.com", "Create admin with DetectALLBoxView pofile name","DetectALLBoxView", "TBM", "{\"user\":{\"first_name\":\"Rocky\",\"last_name\":\"Admin\",\"email\":\"vijay.gangwar+180@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[\"55d8671e9dfa5156bed7601b\"],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","{\"profile\":{\"name\":\"DetectALLBoxView\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Detect\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}"}
				};
				}
			/*Author: Vijay Gangwar
			*todo: import enduser
			*params none
			*/
			@Test ( priority = 1, dataProvider = "RBACImportUser",dataProviderClass = InfraDataProvider.class,groups = {"REGRESSION"} )
			public void ImportUser(String ImportType,String id,String email,String filename,String caseType) throws Exception {
				Reporter.log("exeuting testcase ****ImportUser() ***** Test Rail id "+id,true);
				Reporter.log("description: 1. import user with csv file.\n .2. check the api response \n .3 search imported user",true);
			
				HttpResponse response=Infractions.importUser(suiteData,headers,filename,tenantDB) ;
				String userId=null;
				String responseBody = ClientUtil.getResponseBody(response);
				assertEquals(HttpStatus.SC_OK,response.getStatusLine().getStatusCode(), "Response code verification failed");
				Reporter.log(" file upload response status ..."+responseBody,true);
				System.out.println(" ****responseBody ***** ..."+headers);
				boolean SearchStatus=false;
				userId= Infractions.searchUserid(suiteData, headers, email);
				
				if(caseType.contains("normal"))
				{
					
					if(userId != null)
					{
						SearchStatus=true;
					}
					assertEquals(SearchStatus,true, "Response data verification failed");
					
				}
				else
				{
					if(userId != null)
					{
						SearchStatus=true;
					}
					assertEquals(SearchStatus,false, "Response data verification failed");
					
				}
				
			}	
	
			
			/*Author: Vijay Gangwar
			* Singldomain
			*todo: import enduser
			*params none
			*/
			@Test ( priority = 1, dataProvider = "singleDomainImportUser",groups={"SingleDomain"} )
			public void ImportSingleDomainUser(String ImportType,String id,String email,String filename,String caseType) throws Exception {
				Reporter.log("exeuting testcase ****ImportSingleDomainUser() ***** Test Rail id "+id,true);
				Reporter.log("description: 1. import user with csv file.\n .2. check the api response \n .3 search imported user",true);
				//build path of the file to be uplaoded

				HttpResponse response=Infractions.importUser(suiteData,headers,filename,tenantDB) ;
				String userId=null;
				String responseBody = ClientUtil.getResponseBody(response);
				assertEquals(HttpStatus.SC_OK,response.getStatusLine().getStatusCode(), "Response code verification failed");
				Reporter.log(" file upload response status ..."+responseBody,true);
				System.out.println(" ****responseBody ***** ..."+headers);
				boolean SearchStatus=false;
				userId= Infractions.searchUserid(suiteData, headers, email);			
				if(caseType.contains("normal"))
				{					
					if(userId != null)
					{
						SearchStatus=true;
					}
					assertEquals(SearchStatus,true, "Response data verification failed");					
				}
				else
				{
					if(userId != null)
					{
						SearchStatus=true;
					}
					assertEquals(SearchStatus,false, "Response data verification failed");					
				}

			}
			
			@DataProvider
			public static Object[][] singleDomainImportUser() {
				return new Object[][]{
					//user type    		id	   		first name         
					{ "Rbac Single domain user import",  "TBD","vijay.gangwar+7@infra.com", "Rbac_SingleDomainImport.csv","normal"},
				//	{ "other domain admin Error",   "TBD", "vijay.gangwar+54@seculetbeatle.com", "Rbac_AdminImport_error.csv","error"}
					
					
				};
			}
			
			/*Author: Vijay Gangwar
			* ImportMultiDomainUser
			*todo: import enduser
			*params none
			*/
			@Test ( priority = 1, dataProvider = "multiDomainImportUser",groups={"MultiDomain"} )
			public void ImportMultiDomainUser(String ImportType,String id,String email,String filename,String caseType) throws Exception {
				Reporter.log("exeuting testcase ****ImportMultiDomainUser() ***** Test Rail id "+id,true);
				Reporter.log("description: 1. import user with csv file.\n .2. check the api response \n .3 search imported user",true);
				//build path of the file to be uplaoded

				HttpResponse response=Infractions.importUser(suiteData,headers,filename,tenantDB) ;
				String userId=null;
				String responseBody = ClientUtil.getResponseBody(response);
				assertEquals(HttpStatus.SC_OK,response.getStatusLine().getStatusCode(), "Response code verification failed");
				Reporter.log(" file upload response status ..."+responseBody,true);
				System.out.println(" ****responseBody ***** ..."+headers);
				boolean SearchStatus=false;
				userId= Infractions.searchUserid(suiteData, headers, email);			
				if(caseType.contains("normal"))
				{					
					if(userId != null)
					{
						SearchStatus=true;
					}
					assertEquals(SearchStatus,true, "Response data verification failed");					
				}
				else
				{
					if(userId != null)
					{
						SearchStatus=true;
					}
					assertEquals(SearchStatus,false, "Response data verification failed");					
				}

			}
			
			@DataProvider
			public static Object[][] multiDomainImportUser() {
				return new Object[][]{
					//user type    		id	   		first name         
					{ "Rbac Single domain user import",  "TBD","vijay.gangwar+8@infrautobeatle.com", "Rbac_MultiDomainImport.csv","normal"},
				//	{ "other domain admin Error",   "TBD", "vijay.gangwar+54@seculetbeatle.com", "Rbac_AdminImport_error.csv","error"}
					
					
				};
			}

			
			@Test( priority = 1 , dataProvider = "CloneProfileData",groups = {"P1"})
			public void cloneProfile(String description,String action,String id,String payload,String type,String name) throws Exception {
				Reporter.log("exeuting testcase ****CreateProfile() ***** Description"+description,true);
				Reporter.log("group Action  "+action+" testcase type  "+type,true);
				// search profile
                String CloneName=name+" - copy";
				String resource_uri=null;
				resource_uri=Infractions.getProfileResourceId(suiteData, headers, CloneName);//Infrautils.SearchUserInGrp(responseBody, "profiles", "name", name, "resource_uri");
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
					Pid=Infractions.getProfileid(responseBody, CloneName );
					Reporter.log("deleting old exixting profile",true);
					HttpResponse Response=Infractions.deleteProfile(suiteData, headers, Pid);
					String addResponseBody = ClientUtil.getResponseBody(Response);
					Reporter.log("deleting old exixting profile"+addResponseBody,true);
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
			public Object[][] CloneProfileData() {
				return new Object[][]{
					// type    		id	   		value         
				//	{ "Securlets all OneDrive Profile All", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllOnedriveAll\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllOnedriveAll"},
					{ "clone MultiDomainAccess ", "clone", "C1684506", "{\"profile\":{\"name\":\"MultiDomainAccess - copy\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"MODIFY\",\"VIEW\"]},\"information_level\":{\"Audit\":{\"all\":false,\"levels\":[\"Not anonymized\"]},\"Investigate\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Detect\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Securlets\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]}},\"global_settings\":{},\"permitted_domains\":[\"infrautobeatle.com\",\"infra.com\"],\"org_unit\":[\"__ALL_EL__\"]}}","normal","MultiDomainAccess"},
					{ "clone Boxaall", "clone", "C1684504", "{\"profile\":{\"name\":\"Boxaall - copy\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Box\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"Detect\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Securlets\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Investigate\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Audit\":{\"all\":false,\"levels\":[\"Not anonymized\"]}},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"],\"org_unit\":[\"__ALL_EL__\"]}}","normal","Boxaall"},
					{ "clone GlobalSettingProfile", "clone", "C1684504", "{\"profile\":{\"name\":\"GlobalSettingProfile - copy\",\"description\":\"GlobalSettingProfile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"MODIFY\",\"VIEW\"],\"Store\":[\"MODIFY\",\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"]},\"permitted_services\":{\"Google Apps\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"Audit\":{\"all\":false,\"levels\":[\"Not anonymized\"]},\"Investigate\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Detect\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Securlets\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]}},\"global_settings\":{\"CONTACT\":[\"MODIFY\",\"VIEW\"],\"GATEWAY\":[\"MODIFY\",\"VIEW\"],\"GENERAL\":[\"MODIFY\",\"VIEW\"]},\"permitted_domains\":[\"__ALL_EL__\"],\"org_unit\":[\"__ALL_EL__\"]}}","normal","GlobalSettingProfile"},
					{ "clone Single Domain", "clone", "C1684504", "{\"profile\":{\"name\":\"SingleDomain - copy\",\"description\":\"Single domain profile with usersall\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Investigate\":[\"MODIFY\",\"VIEW\"],\"Securlets\":[\"MODIFY\",\"VIEW\"],\"Users\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"],\"Dashboard\":[\"VIEW\"]},\"permitted_services\":{\"Google Apps\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"Audit\":{\"all\":false,\"levels\":[\"Not anonymized\"]},\"Investigate\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Detect\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]},\"Securlets\":{\"all\":true,\"levels\":[\"__ALL_EL__\"]}},\"global_settings\":{},\"permitted_domains\":[\"infra.com\"],\"org_unit\":[\"__ALL_EL__\"]}}","normal","SingleDomain"},
							
				};
				
				
			}
		 /*Author: Vijay Gangwar
			*todo: use user vijay.gangwar+130@infrabeatle.com 
			* Assing profile "SingleDomain"
			*params none
			*/
		 @Test( priority = 3,groups={"SingleDomain"} )
		 public void displaySingleDomainUser() throws Exception {
			 Reporter.log("1.display user of Infa.com domain only :",true);	

			 HttpResponse response = Infractions.SearchUser(suiteData, headers, "");

			 String responseBody = ClientUtil.getResponseBody(response);
			 assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			 Reporter.log("filtered data: "+responseBody,true);

			 // validate filtered data
			 JSONArray summaryObject=null;
			 {

				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 assertEquals(action_status,"success", "Response code verification failed");
				 // Reporter.log(" user filter api action status."+action_status,true);
			 }

			 String Id=null;
			 for(int i=0; i<summaryObject.length(); i++)
			 {						
				 JSONObject userObj=summaryObject.getJSONObject(i);
				 {					
					 String userEmail=userObj.get("email").toString();
					 boolean status=userEmail.contains("infra.com");
					 Reporter.log(" Retrived value :"+userEmail,true);
					 assertEquals(true,status ," different  domain user found");

				 }					
			 }

		 }
		 
		 /*Author: Vijay Gangwar
			*todo: use user vijay.gangwar+130@infrabeatle.com 
			* Assing profile "SingleDomain"
			*params none
			*/
		 @Test( priority = 3,groups={"MultiDomain"} )
		 public void displayMultiDomainUser() throws Exception {
			 Reporter.log("1.display user of Infa.com domain only :",true);	

			 HttpResponse response = Infractions.SearchUser(suiteData, headers, "");

			 String responseBody = ClientUtil.getResponseBody(response);
			 assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			 Reporter.log("filtered data: "+responseBody,true);

			 // validate filtered data
			 JSONArray summaryObject=null;
			 {

				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 assertEquals(action_status,"success", "Response code verification failed");
				 // Reporter.log(" user filter api action status."+action_status,true);
			 }
			 boolean status=false;
			 String Id=null;
			 for(int i=0; i<summaryObject.length(); i++)
			 {						
				 JSONObject userObj=summaryObject.getJSONObject(i);
				 {					
					 String userEmail=userObj.get("email").toString();
					 
					 

					 
					 if(userEmail.contains("infra.com")||userEmail.contains("infrautobeatle.com")||userEmail.contains("infrautobeatle.com"))
					 {
						  status=true;
					 }
					 else
					 {
						 status=false;
						 Reporter.log("*************************",true);
						 Reporter.log(" different domain  user found :"+userEmail,true);
						 Reporter.log("*************************",true);
					 }
					 
					 Reporter.log(" Retrived value :"+userEmail,true);
					 assertEquals(true,status ," user domain Validation failed");
				 }					
			 }

		 }

		 /*Author: Vijay Gangwar
			*todo: use user vijay.gangwar+130@infrabeatle.com 
			* Assing profile "SingleDomain"
			*params none
			*/
		 @Test( priority = 3,groups={"AllDomain"} )
		 public void displayAllDomainUser() throws Exception {
			 Reporter.log("1.display user of all domains infrabeatle.com,infra.com,infrautobeatle.com only :",true);	

			 HttpResponse response = Infractions.SearchUser(suiteData, headers, "");

			 String responseBody = ClientUtil.getResponseBody(response);
			 assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			 Reporter.log("filtered data: "+responseBody,true);

			 // validate filtered data
			 JSONArray summaryObject=null;
			 {

				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 assertEquals(action_status,"success", "Response code verification failed");
				 // Reporter.log(" user filter api action status."+action_status,true);
			 }
			 boolean status=false;
			 String Id=null;
			 for(int i=0; i<summaryObject.length(); i++)
			 {						
				 JSONObject userObj=summaryObject.getJSONObject(i);
				 {					
					 String userEmail=userObj.get("email").toString();
					 
					 if(userEmail.contains("infra.com")||userEmail.contains("infrabeatle.com")||userEmail.contains("infrautobeatle.com"))
					 {
						  status=true;
					 }
					 else
					 {
						 status=false;
						 Reporter.log("*************************",true);
						 Reporter.log(" different domain  User found :"+userEmail,true);
						 Reporter.log("*************************",true);
					 }
					 Reporter.log(" Retrived value :"+userEmail,true);
					 assertEquals(true,status ," user domain Validation failed");

				 }					
			 }

		 }

		 
		 /*Author: Vijay Gangwar
			*todo: use user vijay.gangwar+130@infrabeatle.com 
			* Assing profile "SingleDomain"
			*params none
			*/
		 @Test( priority = 3,groups={"SingleDomain"} )
		 public void searchOtherDomainUser() throws Exception {
			 Reporter.log("1.display user of Infa.com domain only :",true);	

			 HttpResponse response = Infractions.SearchUser(suiteData, headers, "vijay.gangwar+55@infrabeatle.com");

			 String responseBody = ClientUtil.getResponseBody(response);
			 assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			 Reporter.log("filtered data: "+responseBody,true);

			 // validate filtered data
			 JSONArray summaryObject=null;
			 {

				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 assertEquals(action_status,"success", "Response code verification failed");
				 // Reporter.log(" user filter api action status."+action_status,true);
			 }

			 String Id=null;
			 for(int i=0; i<summaryObject.length(); i++)
			 {						
				 JSONObject userObj=summaryObject.getJSONObject(i);
				 {					
					 String userEmail=userObj.get("email").toString();
					 boolean status=userEmail.contains("vijay.gangwar+55@infrabeatle.com");
					 Reporter.log(" Retrived value :"+userEmail,true);
					 assertEquals(false,status ," user domain Validation failed");

				 }					
			 }

		 }
		 /*Author: Vijay Gangwar
		*todo: use user vijay.gangwar+131@infrabeatle.com 
		* Assing profile "MultiDomain"
		*params none
		*/
		 @Test( priority = 3,groups={"MultiDomain"} )
		 public void searchMultiDomainUser() throws Exception {
			 Reporter.log("1.display user of Infa.com domain only :",true);	

			 HttpResponse response = Infractions.SearchUser(suiteData, headers, "");

			 String responseBody = ClientUtil.getResponseBody(response);
			 assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			 Reporter.log("filtered data: "+responseBody,true);

			 // validate filtered data
			 JSONArray summaryObject=null;
			 {

				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				 assertEquals(action_status,"success", "Response code verification failed");
				 // Reporter.log(" user filter api action status."+action_status,true);
			 }
			 boolean status=false;
			 String Id=null;
			 for(int i=0; i<summaryObject.length(); i++)
			 {						
				 JSONObject userObj=summaryObject.getJSONObject(i);
				 {					
					 String userEmail=userObj.get("email").toString();
					 if(userEmail.contains("infra.com")||userEmail.contains("infrautobeatle.com"))
					 {
						 status=true;
					 }
					 else
					 {
						 status=false;
						 Reporter.log("*************************",true);
						 Reporter.log(" different domain  user found :"+userEmail,true);
						 Reporter.log("*************************",true);
					 }
					 Reporter.log(" Retrived value :"+userEmail,true);
					 assertEquals(true,status ," user domain Validation failed");

				 }					
			 }

		 }
		 
		 /*Author: Vijay Gangwar
			*todo: use user vijay.gangwar+131@infrabeatle.com 
			* Assing profile "MultiDomain"
			*params none
			*/
			 @Test( priority = 3,groups={"AllDomain"} )
			 public void searchAllDomainUser() throws Exception {
				 Reporter.log("1.display user of Infa.com domain only :",true);	

				 HttpResponse response = Infractions.SearchUser(suiteData, headers, "");

				 String responseBody = ClientUtil.getResponseBody(response);
				 assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				 Reporter.log("filtered data: "+responseBody,true);

				 // validate filtered data
				 JSONArray summaryObject=null;
				 {

					 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
					 String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
					 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
					 assertEquals(action_status,"success", "Response code verification failed");
					 // Reporter.log(" user filter api action status."+action_status,true);
				 }
				 boolean status=false;
				 String Id=null;
				 for(int i=0; i<summaryObject.length(); i++)
				 {						
					 JSONObject userObj=summaryObject.getJSONObject(i);
					 {					
						 String userEmail=userObj.get("email").toString();
						 if(userEmail.contains("infra.com")||userEmail.contains("infrautobeatle.com")||userEmail.contains("infrabeatle.com"))
						 {
							 status=true;
						 }
						 else
						 {
							 status=false;
							 Reporter.log("*************************",true);
							 Reporter.log(" different domain user  found :"+userEmail,true);
							 Reporter.log("*************************",true);
						 }
						 Reporter.log(" Retrived email :"+userEmail,true);
						 assertEquals(true,status ," user domain Validation failed");

					 }					
				 }

			 }
			 
			 @Test( priority = 1,groups={"AllDomain"},dataProvider = "searchGroupDataAllDomain" )
				public void searchGroupAllDomain(String description,String id,String name) throws Exception
				{
					Reporter.log("Description . 1. search group 2. delete group \n",true);
					Reporter.log("test case id \n"+id,true);
					String uri =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+name;
					Reporter.log("search user uri ."+uri,true);
					// build uri for group deletion 
					URI dataUri = ClientUtil.BuidURI(uri);
					// send get request for group search  
					HttpResponse response =  restClient.doGet(dataUri, headers);
					String responseBody = ClientUtil.getResponseBody(response);
					//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
					System.out.println("e ****responseBody ***** ..."+responseBody);
					//Reporter.log("search user responseBody: ."+responseBody,true);
					
					String userId=Infrautils.getUserid(responseBody,"groupslist","name",name);
					Reporter.log("Group Id for deletion : "+userId,true);
					boolean status=userId.isEmpty();
					assertEquals( false,status, "Group detail not found");
					
					Reporter.log("Expected status : group found with id ",true);
					Reporter.log("Expected status : group found with id "+userId,true);
					Reporter.log("Testcase execution Completed ",true);
					
				}
			 @DataProvider
				public static Object[][] searchGroupDataAllDomain() {
					return new Object[][]{
						// type    		id	   		value         
						{ "Search  Group created by All domain Group",  "C1643637", "AllUser",},
						{ "Search  Group created by All domain admin",  "C1643637", "ActiveTest2",}
					};
				}
		 
		 /*Author: Vijay Gangwar
			*todo: bulk actions on the Single domain
			*user:use user vijay.gangwar+130@infrabeatle.com 
			*params none
			*/
			@Test ( priority = 2, dataProvider = "BulkDeleteSingleData",groups={"SingleDomain"} )
			public void BulkDeleteSingleDomain(String action,String id,String email,String payload,String payloadCreate) throws Exception {
				Reporter.log("exeuting testcase ****BulkEditAction() ***** Test Rail id :C3473 ...",true);
				Reporter.log("description: 1. filter user with email. 2 Select bulk Edit option . 3.Perform bulk edit options",true);

				// searach for the  id of user
				String userId= Infractions.searchUserid(suiteData, headers, email);//Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
				//userId=Infractions.getUserid(responseBody, email);
				if(null==userId||(userId.isEmpty()))
				{
					HttpResponse	responseCreatUser = Infractions.createUser(suiteData,headers,payloadCreate);
					String responseBody1 = ClientUtil.getResponseBody(responseCreatUser);
					System.out.println("Response: "+responseBody1);
					String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
					assertEquals(action_status,"success", "user Creation data  verification failed");
				}
				Reporter.log("replace dummy id with the dynamic id "+userId,true);
				payload=payload.replace(InfraConstants.DUMMY, userId);
				String uri=null;
				if(action.contains("delete"))
				{
					uri=suiteData.getReferer()+"/admin/user/delete_users/";
				}
				else
				{
					 
					 uri =suiteData.getReferer()+"/admin/user/ng/bulkupdate";
				}
				//String uri =suiteData.getReferer()+"admin/user/ng/bulkupdate";//suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.API_CALL_USERS+"import/";
				URI dataUri = ClientUtil.BuidURI(uri);
				
				HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				String responseBody = ClientUtil.getResponseBody(response);
				Reporter.log(" Bulk action responseBody : "+responseBody);
				String action_status =(String) new JSONObject(responseBody).get("api_response").toString();
				assertEquals(Integer.parseInt(action_status),InfraConstants.OK, "Response code verification failed");
				Reporter.log("Expected  result :0",true);
				Reporter.log("Actual result :"+action_status,true);
				Reporter.log("Testcase execution Completed ",true);
			}
			
			@DataProvider
			public static Object[][] BulkDeleteSingleData() {
				return new Object[][]{
					//Action    		id	   searchdata		payload        
	//				{ "deactivate","C18886","vijay.gangwar+130@infra.com","{\"action\":\"deactivate\",\"users\":[{\"email\":\"vijay.gangwar+130@infra.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}"},
	//				{ "activate", "C3473","vijay.gangwar+23@infrabeatle.com", "{\"action\":\"activate\",\"users\":[{\"email\":\"vijay.gangwar+23@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}"},
					{ "delete", "C18881","vijay.gangwar+5@infra.com", "{\"users\":[{\"email\":\"vijay.gangwar+5@infra.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}","{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+5@infra.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"}
				};
			}
			
			/*Author: Vijay Gangwar
			*todo: bulk actions on the Single domain
			*user:use user vijay.gangwar+130@infrabeatle.com 
			*params none
			*/
			@Test ( priority = 2, dataProvider = "BulkDeleteAllDomainData",groups={"AllDomain"} )
			public void BulkDeleteAllDomain(String action,String id,String email,String payload,String Payload1) throws Exception {
				Reporter.log("exeuting testcase ****BulkEditAction() ***** Test Rail id :C3473 ...",true);
				Reporter.log("description: 1. filter user with email. 2 Select bulk Edit option . 3.Perform bulk edit options",true);

				// search user before creation
				HttpResponse response=Infractions.SearchUser(suiteData, headers,  email);
				String responseBody = ClientUtil.getResponseBody(response);
				String userId = null;
				HttpResponse responseCreatUser=null;
				userId=Infractions.getUserid(responseBody, email);
				if(null==userId||(userId.isEmpty()))
				{
					responseCreatUser = Infractions.createUser(suiteData,headers,Payload1);
					String responseBody1 = ClientUtil.getResponseBody(responseCreatUser);
					System.out.println("Response: "+responseBody1);
					String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
					assertEquals(action_status,"success", "user Creation data  verification failed");
				}
				// search user after creation and before deletion
				userId = null;
				userId = Infractions.searchUserid(suiteData, headers, email);
				Reporter.log("The User Created is :" + userId, true);
				
				
				// searach for the  id of user
				userId= Infractions.searchUserid(suiteData, headers, email);//Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
				Reporter.log("replace dummy id with the dynmic id "+userId,true);
				payload=payload.replace(InfraConstants.DUMMY, userId);
				String uri=null;
				if(action.contains("delete"))
				{
					uri=suiteData.getReferer()+"/admin/user/delete_users/";
				}
				else
				{
					 
					 uri =suiteData.getReferer()+"/admin/user/ng/bulkupdate";
				}
				//String uri =suiteData.getReferer()+"admin/user/ng/bulkupdate";//suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.API_CALL_USERS+"import/";
				URI dataUri = ClientUtil.BuidURI(uri);
				
				HttpResponse response1 =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				String responseBody1 = ClientUtil.getResponseBody(response1);
				Reporter.log(" Bulk action responseBody : "+responseBody1);
				String action_status =(String) new JSONObject(responseBody1).get("api_response").toString();
				assertEquals(Integer.parseInt(action_status),InfraConstants.OK, "Response code verification failed");
				Reporter.log("Expected  result :0",true);
				Reporter.log("Actual result :"+action_status,true);
				Reporter.log("Testcase execution Completed ",true);
			}
			
			@DataProvider
			public static Object[][] BulkDeleteAllDomainData() {
				return new Object[][]{
					//Action    		id	   searchdata		payload        
					{ "delete", "C18881","vijay.gangwar+5@infrautobeatle.com", "{\"users\":[{\"email\":\"vijay.gangwar+5@infrautobeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}","{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+5@infrautobeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
					{ "delete", "C18881","vijay.gangwar+136@infrabeatle.com", "{\"users\":[{\"email\":\"vijay.gangwar+136@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}","{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+136@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
					{ "delete", "C18881","vijay.gangwar+6@infra.com", "{\"users\":[{\"email\":\"vijay.gangwar+6@infra.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}","{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+6@infra.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"}
				};
			}
			
			/*Author: Vijay Gangwar
			*todo: bulk actions on the Single domain
			*user:use user vijay.gangwar+130@infrabeatle.com 
			*params none
			*/
			@Test ( priority = 2, dataProvider = "BulkDeleteMultiDomainData",groups={"AllDomain"} )
			public void BulkDeleteMultiDomain(String action,String id,String email,String payload,String Payload1) throws Exception {
				Reporter.log("exeuting testcase ****BulkEditAction() ***** Test Rail id :C3473 ...",true);
				Reporter.log("description: 1. filter user with email. 2 Select bulk Edit option . 3.Perform bulk edit options",true);

				// search user before creation
				HttpResponse response=Infractions.SearchUser(suiteData, headers,  email);
				String responseBody = ClientUtil.getResponseBody(response);
				String userId = null;
				HttpResponse responseCreatUser=null;
				userId=Infractions.getUserid(responseBody, email);
				if(null==userId||(userId.isEmpty()))
				{
					responseCreatUser = Infractions.createUser(suiteData,headers,Payload1);
					String responseBody1 = ClientUtil.getResponseBody(responseCreatUser);
					System.out.println("Response: "+responseBody1);
					String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
					assertEquals(action_status,"success", "user Creation data  verification failed");
				}
				// search user after creation and before deletion
				userId = null;
				userId = Infractions.searchUserid(suiteData, headers, email);
				Reporter.log("The User Created is :" + userId, true);
				
				
				// searach for the  id of user
				userId= Infractions.searchUserid(suiteData, headers, email);//Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
				Reporter.log("replace dummy id with the dynmic id "+userId,true);
				payload=payload.replace(InfraConstants.DUMMY, userId);
				String uri=null;
				if(action.contains("delete"))
				{
					uri=suiteData.getReferer()+"/admin/user/delete_users/";
				}
				else
				{
					 
					 uri =suiteData.getReferer()+"/admin/user/ng/bulkupdate";
				}
				//String uri =suiteData.getReferer()+"admin/user/ng/bulkupdate";//suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.API_CALL_USERS+"import/";
				URI dataUri = ClientUtil.BuidURI(uri);
				
				HttpResponse response1 =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				String responseBody1 = ClientUtil.getResponseBody(response1);
				Reporter.log(" Bulk action responseBody : "+responseBody1);
				String action_status =(String) new JSONObject(responseBody1).get("api_response").toString();
				assertEquals(Integer.parseInt(action_status),InfraConstants.OK, "Response code verification failed");
				Reporter.log("Expected  result :0",true);
				Reporter.log("Actual result :"+action_status,true);
				Reporter.log("Testcase execution Completed ",true);
			}
			
			@DataProvider
			public static Object[][] BulkDeleteMultiDomainData() {
				return new Object[][]{
					//Action    		id	   searchdata		payload        
					{ "delete", "C18881","vijay.gangwar+6@infrautobeatle.com", "{\"users\":[{\"email\":\"vijay.gangwar+6@infrautobeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}","{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+6@infrautobeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
					{ "delete", "C18881","vijay.gangwar+7@infra.com", "{\"users\":[{\"email\":\"vijay.gangwar+7@infra.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}","{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+7@infra.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"}
				};
			}

			
			/*Author: Vijay Gangwar
			*todo: delete user sysadmin 
			*params none
			*/
			@Test ( priority = 1,groups={"SingleDomain"}, dataProvider = "createDeleteData",description = "Create and Delete a user without user login in to portal" )
			public void CreateAndDeleteUser(String action,String email, String id,String payload) throws Exception {
				Reporter.log("Create delete Single doimain user ",true);
				Reporter.log("Description 1. create user. 2 delete user even without login.",true);
				// search user before creation
				System.out.println("printing headr for single user: "+headers);
				HttpResponse response=Infractions.SearchUser(suiteData, headers,  email);
				String responseBody = ClientUtil.getResponseBody(response);
				String userId = null;
				HttpResponse responseCreatUser=null;
				userId=Infractions.getUserid(responseBody, email);
				if(null==userId||(userId.isEmpty()))
				{
					responseCreatUser = Infractions.createUser(suiteData,headers,payload);
					System.out.println("payload: "+payload);
					String responseBody1 = ClientUtil.getResponseBody(responseCreatUser);
					System.out.println("Response: "+responseBody1);
					String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
					assertEquals(action_status,"success", "user Creation data  verification failed");
				}
				// search user after creation and before deletion
				userId = null;
				userId = Infractions.searchUserid(suiteData, headers, email);
				Reporter.log("The User Created is :" + userId, true);
				// delete user
				Reporter.log(" delete user with id"+userId,true);
				HttpResponse responseDeleteUser =Infractions.deleteUser(suiteData, headers, userId);
			//	assertEquals( responseDeleteUser.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
				
				Reporter.log("user is deleted",true);
			//	Reporter.log("Actual result :"+responseDeleteUser.getStatusLine().getStatusCode(),true);
				Reporter.log("Expected result :"+HttpStatus.SC_NO_CONTENT,true);
				Reporter.log("Testcase execution Completed ",true);
				
			}
			@DataProvider
			public static Object[][] createDeleteData() {
				return new Object[][]{
					// Action    		id	   		payload         
					{ "Create Delete user","vijay.gangwar+3@infra.com",  "C2129", "{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+3@infra.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
					//	{ "Active inactive mix", "C2162",  "{\"group\":{\"name\":\"BlrInactivegrp\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","BlrInactivegrp"},


				};
			}
			
			/*Author: Vijay Gangwar
			*todo: delete user sysadmin 
			*params none
			*/
			@Test ( priority = 1,groups={"AllDomain"}, dataProvider = "DeleteAllDomianData",description = "Create and Delete a user without user login in to portal" )
			public void DeleteUserAllDomin(String action,String email, String id,String payload) throws Exception {
				Reporter.log("Create delete Single doimain user ",true);
				Reporter.log("Description 1. create user. 2 delete user even without login.",true);
				// search user before creation
				HttpResponse response=Infractions.SearchUser(suiteData, headers,  email);
				String responseBody = ClientUtil.getResponseBody(response);
				String userId = null;
				HttpResponse responseCreatUser=null;
				userId=Infractions.getUserid(responseBody, email);
				if(null==userId||(userId.isEmpty()))
				{
					responseCreatUser = Infractions.createUser(suiteData,headers,payload);
					String responseBody1 = ClientUtil.getResponseBody(responseCreatUser);
					System.out.println("Response: "+responseBody1);
					String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
					assertEquals(action_status,"success", "user Creation data  verification failed");
				}
				// search user after creation and before deletion
				userId = null;
				userId = Infractions.searchUserid(suiteData, headers, email);
				Reporter.log("The User Created is :" + userId, true);
				// delete user
				Reporter.log(" delete user with id"+userId,true);
				HttpResponse responseDeleteUser =Infractions.deleteUser(suiteData, headers, userId);
			//	assertEquals( responseDeleteUser.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
				
				Reporter.log("user is deleted",true);
			//	Reporter.log("Actual result :"+responseDeleteUser.getStatusLine().getStatusCode(),true);
				Reporter.log("Expected result :"+HttpStatus.SC_NO_CONTENT,true);
				Reporter.log("Testcase execution Completed ",true);
				
			}
			@DataProvider
			public static Object[][] DeleteAllDomianData() {
				return new Object[][]{
					// Action    		id	   		payload         
					{ "Create Delete user","vijay.gangwar+4@infra.com",  "C2129", "{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+4@infra.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
					{ "Create Delete user","vijay.gangwar+3@infrautobeatle.com",  "C2129", "{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+3@infrautobeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
					{ "Create Delete user","vijay.gangwar+135@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"Single\",\"last_name\":\"DomainUser\",\"email\":\"vijay.gangwar+135@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},

				};
			}
			/*Author: Vijay Gangwar
			*todo: admin to add securlet
			*user:use user vijay.gangwar+133@infrabeatle.com 
			*params none
			*/
			@Test ( priority = 2, groups={"multiDomain"} )
			public void adminSecurletActivation() throws Exception {
				Reporter.log("exeuting testcase ****BulkEditAction() ***** Test Rail id :C3473 ...",true);
				Reporter.log("description: 1. filter user with email. 2 Select bulk Edit option . 3.Perform bulk edit options",true);

				BoxSeleniumTest.getboxActivated(suiteData.getReferer(),"vijay.gangwar+133@infrabeatle.com",suiteData.getPassword(),UIConstants.BOX_USER_ID,UIConstants.BOX_PWD);
				Reporter.log("Actual result :",true);
				Reporter.log("Testcase execution Completed ",true);
			}
			
			/*Author: Vijay Gangwar
			*todo: admin to add securlet
			*user:use user vijay.gangwar+133@infrabeatle.com 
			*params none
			*/
			@Test ( priority = 2, groups={"multiDomain"} )
			public void adminSecurletDeactivation() throws Exception {
				Reporter.log("exeuting testcase ****BulkEditAction() ***** Test Rail id :C3473 ...",true);
				Reporter.log("description: 1. filter user with email. 2 Select bulk Edit option . 3.Perform bulk edit options",true);

				BoxSeleniumTest.removBoxActivation(suiteData.getReferer());
				Reporter.log("Actual result :",true);
				Reporter.log("Testcase execution Completed ",true);
			}
			
			/*Author: Vijay Gangwar
			*todo: admin to add securlet
			*user:use user vijay.gangwar+133@infrabeatle.com 
			*params none
			*/
			@Test ( priority = 2, groups={"multiDomain"} )
			public void ActivateSecurlet() throws Exception {
				Reporter.log("exeuting testcase ****BulkEditAction() ***** Test Rail id :C3473 ...",true);
				Reporter.log("description: 1. filter user with email. 2 Select bulk Edit option . 3.Perform bulk edit options",true);

				BoxSeleniumTest.removBoxActivation(suiteData.getReferer());
				Reporter.log("Actual result :",true);
				Reporter.log("Testcase execution Completed ",true);
			}
			

			@Test( priority = 1 , groups={"multiDomain"} , dataProvider = "multiDomaingroupData" )
			public void CreateGroup(String description,String action,String id,String payload,String type,String name) throws Exception {
				Reporter.log("exeuting testcase ****CreateGroup() ***** Description"+description,true);
				Reporter.log("group Action"+action+" testcase type"+type,true);
				
				// search before creation
				HttpResponse responseSearch=Infractions.SearchGroup(suiteData, headers, name);
				String SearchResponseBody = ClientUtil.getResponseBody(responseSearch);
				String groupid=Infractions.getGroupid(SearchResponseBody, name);
				if(groupid.isEmpty())
				{
					// create group request
					HttpResponse response=Infractions.createGroup(suiteData, headers, payload);
					
					String responseBody = ClientUtil.getResponseBody(response);
					
					
					
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
				}
				else
				{
					Reporter.log("Group already present: ",true);
				}
				
				//System.out.println("** addGroup ** Response: "+response.getStatusLine().getStatusCode());
				
			}
			
			@DataProvider
			public static Object[][] multiDomaingroupData() {
				return new Object[][]{
					// type    		id	   		value         
					{ "Add Group", "add", "C2129", "{\"group\":{\"name\":\"multiDomain\",\"description\":\"multiDomain test group\",\"is_active\":true,\"notes\":\"\"}}","normal","multiDomain"},
//					//	{ "Active inactive mix", "add" ,"C2162",  "{\"group\":{\"name\":\"InactiveActivegrp\",\"description\":\"test group\",\"is_active\":false,\"notes\":\"\"}}","normal","InactiveActivegrp"},
					
				};
			}
			
			/*Author: Vijay Gangwar
			*todo: addGroup request 
			*params none
			*/
		
			@Test( priority = 1,groups={"multiDomain"},dataProvider = "deleteMultiGroupData" )
			public void deleteGroup(String description,String id,String name) throws Exception
			{
				Reporter.log("Description . 1. search group 2. delete group \n",true);
				Reporter.log("test case id \n"+id,true);
				String uri =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+name;
				Reporter.log("search user uri ."+uri,true);
				// build uri for group deletion 
				URI dataUri = ClientUtil.BuidURI(uri);
				// send get request for group search  
				HttpResponse response =  restClient.doGet(dataUri, headers);
				String responseBody = ClientUtil.getResponseBody(response);
				//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
				System.out.println("e ****responseBody ***** ..."+responseBody);
				//Reporter.log("search user responseBody: ."+responseBody,true);
				
				String userId=Infrautils.getUserid(responseBody,"groupslist","name",name);
				Reporter.log("Group Id for deletion : "+userId,true);
				if(null!=userId)
				{
					// delete  Group				
					HttpResponse response2 =  Infractions.deleteGroup(suiteData, headers, userId);			
					Reporter.log("** deleteUser** Response: ",response2.getStatusLine().getStatusCode(),true);
					assertEquals( response2.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
					Reporter.log("** deleteGroup** Response: "+response2.getStatusLine().getStatusCode());
					
					//cross verfiy for group deletion
					String uri1 =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+name;
					// build uri for group deletion 
					URI dataUri1 = ClientUtil.BuidURI(uri1);
					// send get request for group search  
					HttpResponse response1 =  restClient.doGet(dataUri1, headers);
					String responseBody1 = ClientUtil.getResponseBody(response1);
					userId=Infrautils.getUserid(responseBody1,"groupslist","name",name);
					assertEquals( userId,null, "Response code verification failed");
					Reporter.log("group is deleted :"+name,true);
					Reporter.log("Expected status : "+null,true);
					Reporter.log("Actual status :"+userId,true);
					Reporter.log("Testcase execution Completed ",true);
					
				}
				else
				{
					Reporter.log("group not found :"+name,true);
				}
			}
			
			@DataProvider
			public static Object[][] deleteMultiGroupData() {
				return new Object[][]{
					// type    		id	   		value         
					{ "Delete Group",  "C1643637", "multiDomain",}
				};
			}
			
			@Test( priority = 1,groups={"multiDomain"},dataProvider = "searchGroupData" )
			public void searchGroup(String description,String id,String name) throws Exception
			{
				Reporter.log("Description . 1. search group 2. delete group \n",true);
				Reporter.log("test case id \n"+id,true);
				String uri =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+name;
				Reporter.log("search user uri ."+uri,true);
				// build uri for group deletion 
				URI dataUri = ClientUtil.BuidURI(uri);
				// send get request for group search  
				HttpResponse response =  restClient.doGet(dataUri, headers);
				String responseBody = ClientUtil.getResponseBody(response);
				//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
				System.out.println("e ****responseBody ***** ..."+responseBody);
				//Reporter.log("search user responseBody: ."+responseBody,true);
				
				String userId=Infrautils.getUserid(responseBody,"groupslist","name",name);
				Reporter.log("Group Id for deletion : "+userId,true);
				boolean status=userId.isEmpty();
				assertEquals( false,status, "Group detail not found");
				
				Reporter.log("Expected status : group found with id ",true);
				Reporter.log("Expected status : group found with id "+userId,true);
				Reporter.log("Testcase execution Completed ",true);
				
			}
			
			@DataProvider
			public static Object[][] searchGroupData() {
				return new Object[][]{
					// type    		id	   		value         
					{ "Search  Group created by multi domain admin",  "C1643637", "multiDomain",},
					{ "Search  Group created by single domain admin",  "C1643637", "multiDomain",}
				};
			}
			

			@Test( priority = 2,dataProvider="ResetPassword",groups={"AllDomain"} )
			public void sendResetPasswordUser(String Description,String id,String email,String paylod,String caseType) throws Exception {
				Reporter.log("Description:1. Admin to reset user password ** ...",true);
				Reporter.log("Description:2. user gets  reset password email ** ...",true);
				
				//search  user ,for password reset
				String uri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
				Reporter.log("search user uri ."+uri,true);
				//prepare uri for get user request 
				URI dataUri = ClientUtil.BuidURI(uri);
				HttpResponse response =  restClient.doGet(dataUri, headers);
				String responseBody = ClientUtil.getResponseBody(response);
				//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
				System.out.println("e ****responseBody ***** ..."+responseBody);
				Reporter.log("search user responseBody: ."+responseBody,true);
				String userId=Infrautils.getUserid(responseBody,"userslist","email",email);
				
				System.out.println(" get list of users Response: "+responseBody);		
				String uri1 = suiteData.getReferer()+InfraConstants.RESET_PASSWORD;
				String payload=InfraConstants.PAYLOAD_RESET;
				payload=payload.replace("uid", userId);
				payload=payload.replace("vijay.gangwar+16@infrabeatle.com", email);
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
					{ "Reset other Inactive user password","C1508566",  "vijay.gangwar+16@infrabeatle.com",InfraConstants.PAYLOAD_RESET,"normal" },
					{ "Reset other Active user password", "C1508566","vijay.gangwar+11@infrabeatle.com",  InfraConstants.PAYLOAD_RESET,"normal"},
					{ "Reset other Active Admin password", "C1508566","vijay.gangwar+88@infrabeatle.com",  InfraConstants.PAYLOAD_RESET,"normal"},
					
					
				};
			}

}
