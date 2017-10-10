package com.elastica.beatle.tests.infra;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.infra.InfraActions;
import java.io.File;
import java.io.IOException;

import com.elastica.beatle.tests.gmail.GmailDataProvider;
import com.elastica.beatle.tests.infra.InfraUtils;
/*
 * test classes for users and groups testcases
 */
public class Infra extends CommonTest{
	
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
	//	InfraActions Infractions = new InfraActions();
		
	}	
		
		/*Author: Vijay Gangwar
		*todo: delete user sysadmin 
		*params none
		*/
		@Test ( priority = 1,dataProviderClass = InfraDataProvider.class, dataProvider = "createDeleteData",description = "Create and Delete a user without user login in to portal" )
		public void CreateAndDeleteUser(String action,String email, String id,String payload) throws Exception {
			Reporter.log("****CreateDeleteUser() ***** Test Rail id:249679 ...",true);
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
			assertEquals(action_status,"success", "user Creation data  verification failed");
			Reporter.log("==========================", true);

			// search user after creation and before deletion
			userId = null;
			userId = Infractions.searchUserid(suiteData, headers, email);
			Reporter.log("The User Created is :" + userId, true);
			Reporter.log("Actual result : User is created "+userId,true);
			Reporter.log("Expected result :User is created",true);
			Reporter.log("Testcase execution Completed ",true);

		}

		//Depricated method
		/*Author: Vijay Gangwar
		*todo: delete user sysadmin 
		*params none
		*/
	//	@Test ( priority = 2 ,description = "Delete a user addby addUser testcase.")
		public void DeleteInactiveUser() throws Exception {
			Reporter.log("****DeleteInactiveUser() ***** Test Rail ID:C249676...",true);
			Reporter.log("1. search inactive user to delete. 2 delete user .",true);
			String uri =suiteData.getReferer()+"/"+tenantDB+InfraConstants.SEARCH_USER+"Rajday";
			Reporter.log("search inactive user uri ."+uri,true);
			///elasticaco/api/admin/v1/users/
			URI dataUri1 = ClientUtil.BuidURI(uri);
			HttpResponse response1 =  restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody1 = ClientUtil.getResponseBody(response1);
			//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
			System.out.println("****responseBody ***** ..."+responseBody1);
			Reporter.log("search user responseBody: ."+responseBody1,true);
			String userId=Infrautils.getUserid(responseBody1,"userslist","first_name","Rajday");
			System.out.println(" get list of users Response: "+responseBody1);
			
			
			// delete user may day
			String Deleteuri =suiteData.getScheme()+"://"+ suiteData.getApiserverHostName()+"/"+tenantDB+InfraConstants.API_CALL_USERS+userId+"/";
			Reporter.log("Request Method type: POST",true);
			Reporter.log("Request API :"+Deleteuri,true);
			System.out.println("exeuting testcase ****deleteUser() *****uri ..."+Deleteuri);
			URI dataUri = ClientUtil.BuidURI(Deleteuri);
			HttpResponse response =  restClient.doDelete(dataUri, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
			assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
			System.out.println("** deleteUser** Response: "+response.getStatusLine().getStatusCode());
			
			System.out.println(" ****cross check if use is present ***** ..."+responseBody1);
			Reporter.log("ross check if use is present: "+responseBody1,true);
			response1 =  restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			responseBody1 = ClientUtil.getResponseBody(response1);
			//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
			System.out.println("e ****responseBody ***** ..."+responseBody1);
			Reporter.log("search user responseBody: ."+responseBody1,true);
			userId=Infrautils.getUserid(responseBody1,"userslist","first_name","AAday");
			assertEquals( userId,null, "Response code verification failed");
			assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
			Reporter.log("userId...."+userId,true);
	
			
		}
		
		/*Author: Vijay Gangwar
		*todo: delete user sysadmin 
		*params none
		*/
		// #bug 31937
		@Test ( priority = 5, description = "Create and Delete a user without user login in to portal")
		public void DeleteUser() throws Exception {
			Reporter.log("****DeleteUser()() ***** Test Rail id:249679 ...",true);
			Reporter.log("Description 1.  delete user even without profiding id. 1.error case to  test null for api",true);
			
			String userId="";
			HttpResponse responseDeleteUser =Infractions.deleteUser(suiteData, headers, userId);
			assertEquals( responseDeleteUser.getStatusLine().getStatusCode(),HttpStatus.SC_METHOD_NOT_ALLOWED, "Response code verification failed");
			Reporter.log("Actual result :"+responseDeleteUser.getStatusLine().getStatusCode(),true);
			Reporter.log("Expected result :"+HttpStatus.SC_METHOD_NOT_ALLOWED,true);
			Reporter.log("Testcase execution Completed ",true);
			
		}
		
		/*Author: Vijay Gangwar
		*todo: delete user sysadmin 
		*params none
		*/
		// #bug 32310
		@Test ( priority =5, description = "Create and Delete a user without user login in to portal")
		public void DeleteGroup() throws Exception {
			Reporter.log("****DeleteGroup()() ***** Test Rail id: ...",true);
			Reporter.log("Description 1.  delete user even without profiding id. 1.error case to  test null for api",true);
			//InfraActions Infractions = new InfraActions();
			String groupId="";
			HttpResponse responseDeleteUser =Infractions.deleteGroup(suiteData, headers, groupId);
			assertEquals( responseDeleteUser.getStatusLine().getStatusCode(),HttpStatus.SC_METHOD_NOT_ALLOWED, "Response code verification failed");
		
			Reporter.log("Actual result :"+responseDeleteUser.getStatusLine().getStatusCode(),true);
			Reporter.log("Expected result :"+HttpStatus.SC_METHOD_NOT_ALLOWED,true);
			Reporter.log("Testcase execution Completed ",true);
			
		}
		

		/*Author: Vijay Gangwar
		*todo: delete user sysadmin 
		*params none
		*/
		@Test ( priority = 2 ,dataProviderClass = InfraDataProvider.class,dataProvider = "createDeleteData",description = "Delete a user addby addUser testcase.")
		public void DeleteUserDBCheck(String action,String email, String id,String payload) throws Exception {
			Reporter.log("****DeleteUser() ***** ...Test Rail id:C249675",true);
			Reporter.log("1. search user to delete. 2 delete user .",true);
			
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
			
			// user is deleted , check for the user in the userlist db
			HttpResponse response1=Infractions.SearchUser(suiteData, headers, email);
			
			String responseBody1 = ClientUtil.getResponseBody(response1);
			//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
			System.out.println(" searched userlist..."+responseBody1);
			//Reporter.log("search user responseBody: ."+responseBody1,true);
			
			JSONArray summaryObject=null;
			try {
				summaryObject = (JSONArray) new JSONObject(responseBody1).getJSONArray("userslist");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}			
			Reporter.log("get list of users : "+summaryObject.length(),true);			
			assertEquals( 0,summaryObject.length(), "userlist is not empty");
			Reporter.log("Expected  result: Total number of users 0",true);
			Reporter.log("Actual result :Total number of users "+summaryObject.length(),true);
			Reporter.log("Testcase execution Completed ",true);
		}
		
			
		@Test ( priority = 2 ,dataProviderClass = InfraDataProvider.class,dataProvider = "InactiveUser",description = "Delete a user addby addUser testcase.")
		public void DeleteGroupMemberActive(String payload1,String payload2,String payload3) throws Exception {
			Reporter.log("****DeleteGroupMemberActive() ***** Test Rail id:C249682 ...",true);
			Reporter.log("1. Create a group. 2 add inactive memeber 3.Delete Group  .",true);
			
			System.out.println("exeuting testcase ****DeleteGroupMemberActive() ***** ...");
			
			// create a group	
			String uri =suiteData.getReferer()+InfraConstants.API_ADD_GROUP;
			String payload =payload1;//"{\"group\":{\"name\":\"ActiveGroup \",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}";
			
			
			URI dataUri = ClientUtil.BuidURI(uri);
			Reporter.log("1. Create a group call==",true);
			
			
			Reporter.log("Request Method type: POST",true);
			Reporter.log("Request API :"+dataUri,true);
			
			HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			// create user 
			Reporter.log("Create active user to this group **ActiveDelete ",true);
		    payload=payload2;//"{\"user\":{\"first_name\":\"ActiveUser\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+33@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}";
			HttpResponse responseCreatUser = Infractions.createUser(suiteData,headers,payload);
			//String responseBody = ClientUtil.getResponseBody(response1);
			assertEquals(responseCreatUser.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			
			/*Add user to the group*/
			Reporter.log("***** Add user to the group**** ",true);
			String uri1 =suiteData.getReferer()+"/admin/user/assign";
			Reporter.log("search user uri ."+uri1,true);
			//String uri = backend+"/elasticaco/api/admin/v1/getusers/";
	//		String payload1=payload3"{\"email\":\"vijay.gangwar+33@infrabeatle.com\",\"deleted_groups\":[],\"added_groups\":[\"ActiveGroup \"]}";
			dataUri = ClientUtil.BuidURI(uri1);
			HttpResponse AddResponse =  restClient.doPost(dataUri, headers, null, new StringEntity(payload3));
			assertEquals(AddResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
//
//			///elasticaco/api/admin/v1/users/
			//Delete active group with active member
			
			Reporter.log("1. search group to delete. ",true);
			
			HttpResponse response2=Infractions.SearchGroup(suiteData, headers, "ActiveGroup");
			String responseBody2 = ClientUtil.getResponseBody(response2);
			//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
			System.out.println(" ****responseBody ***** ..."+responseBody2);
			//Reporter.log("search user responseBody: ."+responseBody2,true);
			
			String userId=null;//getUserid(responseBody2,"groupslist","name","ActiveGroup");
			{

				 JSONArray summaryObject = (JSONArray) new JSONObject(responseBody2).getJSONArray("groupslist");					
					//String Id=null;
					for(int i=0; i<summaryObject.length(); i++)
					{						
						JSONObject userObj=summaryObject.getJSONObject(i);
						String chekname =userObj.get("name").toString();
						if(chekname.contains("ActiveGroup"))
						{
							userId=(String)userObj.get("id");
							break;
						}						
					}
					Reporter.log("userId...."+userId,true);	
			}			
			//	delete the group
			HttpResponse response3=Infractions.deleteGroup(suiteData, headers, userId);
			assertEquals( response3.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
			
			Reporter.log("** deleteUser** Response: ",response3.getStatusLine().getStatusCode(),true);
			System.out.println("** deleteGroup** Response: "+response3.getStatusLine().getStatusCode());
		}
		/*Author: Vijay Gangwar
		*todo: delete user sysadmin 
		*params none
		*/
		@Test ( priority = 2 ,description = "Delete a user addby addUser testcase.")
		public void DeleteInactiveGroup() throws Exception {
			Reporter.log("****DeleteInactiveGroup() ***** Test Rail id:CC249681",true);
			Reporter.log("1. search inactive group to delete. 2 Delete inactive Group user .",true);
			
			System.out.println("exeuting testcase ****DeleteInactiveGroup() ***** ...");
			//String uri =suiteData.getReferer()+InfraConstants.API_ADD_GROUP;
			String name="AInactive_test";
			String payload =InfraConstants.INACTIVE_GROUP_PL;
			//String payload="{\"user\":{\"first_name\":\"raj1\",\"last_name\":\"raj1\",\"email\":\"raj1.co@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}";
			//Infractions.createGroup(suiteData, headers, payload);
			//URI dataUri = ClientUtil.BuidURI(uri);
			//Reporter.log("Request Method type: POST",true);
			//Reporter.log("Request API :"+dataUri,true);
			HttpResponse response =  Infractions.createGroup(suiteData, headers, payload);;//restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			
			//Reporter.log("****DeleteGroup() ***** ...",true);
			Reporter.log("1. search group to delete. ",true);
			
			//String uri1 =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+"AInactive_test";
			//Reporter.log("search user uri ."+uri1,true);
			//prepare search uri
			//URI dataUri1 = ClientUtil.BuidURI(uri1);
			HttpResponse response1 = Infractions.SearchGroup(suiteData, headers, name);// restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody1 = ClientUtil.getResponseBody(response1);
			String userId=Infractions.getGroupid(responseBody1, name);
			//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
//			System.out.println("e ****responseBody ***** ..."+responseBody1);
//			Reporter.log("search user responseBody: ."+responseBody1,true);
////			JSONArray summaryObject=null;
////			try{
//				JSONArray		summaryObject= (JSONArray) new JSONObject(responseBody1).getJSONArray("groupslist");
////				} catch (JSONException e) {
////				// TODO Auto-generated catch block
////				//e.printStackTrace();
////				System.out.println(" ****group not presetent in DB ***** ..."+responseBody1);
////				Reporter.log(" ****group not presetent in DB ***** ..."+responseBody1,true);
////				}
//				    String userId=null;
//			for(int i=0; i<summaryObject.length(); i++)
//			{
//				
//				JSONObject userObj=summaryObject.getJSONObject(i);
//				
//				if(userObj.get("name").equals("ADelete_test"))
//				{
//					userId=(String)userObj.get("id");
//					break;
//				}
//				
//			}
			Reporter.log("userId...."+userId,true);
			System.out.println(" get list of users Response: "+responseBody1);
			
			
			// delete user may day
			if((null!=userId )&&(!userId.isEmpty() ))
			{
		//	String Deleteuri =suiteData.getScheme()+"://"+ suiteData.getApiserverHostName()+"/"+tenantDB+"api/admin/v1/usergroups/"+userId+"/";
			
			//Reporter.log("Request API :"+Deleteuri,true);
			//System.out.println("exeuting testcase ****deleteUser() *****uri ..."+Deleteuri);
			//URI dataUri2 = ClientUtil.BuidURI(Deleteuri);
			URI dataUri2 = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(),"/"+suiteData.getTenantDomainName()+InfraConstants.API_GROUP_DEL+userId+"/");
			HttpResponse response2 =  restClient.doDelete(dataUri2, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
		//	String responseBody = ClientUtil.getResponseBody(response);
			
			assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			}
			else
			{
				Reporter.log("group not found",true);
			}
			
			
			
		}
		
		
		
		
		/*Author: Vijay Gangwar
		*todo: import enduser
		*params none
		*/
		@Test ( priority = 1, dataProvider = "ImportUser",dataProviderClass = InfraDataProvider.class )
		public void ImportUser(String ImportType,String id,String email,String filename,String caseType) throws Exception {
			Reporter.log("exeuting testcase ****ImportUser() ***** Test Rail id "+id,true);
			Reporter.log("description: 1. import user with csv file.\n .2. check the api response \n .3 search imported user",true);
			//build path of the file to be uplaoded
			String filepath = InfraConstants.INFRA_DATA_LOC + filename;
			//check the filename provided is absolute or not
			File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
			if(!uploadFile.exists()) {
				System.out.println("Sorry file not exists in the folder"); 
			}
			//create a multipart entity for file uplaod 
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();		
			builder.addTextBody("name", "file", ContentType.TEXT_PLAIN);
			//add the file parameter to the and build multipat entity
			builder.addBinaryBody("file", uploadFile, ContentType.APPLICATION_OCTET_STREAM, filename);		
			HttpEntity multipart = builder.build();

			headers = getHeaders();
			//headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=----------------------"+Long.toString(new Date().getTime())));
			String uri =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+"/"+tenantDB+InfraConstants.API_CALL_USERS+"import/";
			URI dataUri = ClientUtil.BuidURI(uri);
			
			HttpResponse response =  restClient.doPost(dataUri, headers, null, multipart);
			String userId=null;
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(HttpStatus.SC_OK,response.getStatusLine().getStatusCode(), "Response code verification failed");
			Reporter.log(" file upload response status ..."+responseBody,true);
			System.out.println(" ****responseBody ***** ..."+headers);
//			String uri1 =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
//			Reporter.log("search user uri ."+uri1,true);
//			//prepare uri for get user request 
//			URI dataUri1 = ClientUtil.BuidURI(uri1);
//			HttpResponse response1 =  Infractions.searchUserid(suiteData, headers, email);//restClient.doGet(dataUri1, headers);
//			Thread.sleep(5000);
//			String responseBody1 = ClientUtil.getResponseBody(response1);
			//Reporter.log("search user responseBody: "+responseBody1,true);
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
					SearchStatus=false;
				}
				assertEquals(SearchStatus,false, "Response data verification failed");
				
			}
			
		}
		
		
		/*Author: Vijay Gangwar
		*todo: import enduser
		*params none
		*/
		@Test ( priority = 2, dataProvider = "ActionData",dataProviderClass = InfraDataProvider.class )
		public void BulkEditAction(String action,String id,String email,String payload) throws Exception {
			Reporter.log("exeuting testcase ****BulkEditAction() ***** Test Rail id :C3473 ...",true);
			Reporter.log("description: 1. fileter user. 2 Select bulk Edit option . 3.Perform bulk edit options",true);
//			String SearchUri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
//			Reporter.log("search user uri ."+SearchUri,true);
//			URI dataUriSearch = ClientUtil.BuidURI(SearchUri);
//			HttpResponse SearchResponse =  restClient.doGet(dataUriSearch, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
//			String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
//			//print response body
//			System.out.println("search user responseBody: ."+SearchResponse);
			// searach for the  id of user
			String userId= Infractions.searchUserid(suiteData, headers, email);//Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
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
			
			HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			Reporter.log(" Bulk action responseBody : "+responseBody);
			String action_status =(String) new JSONObject(responseBody).get("api_response").toString();
			assertEquals(Integer.parseInt(action_status),InfraConstants.OK, "Response code verification failed");
			Reporter.log("Expected  result :0",true);
			Reporter.log("Actual result :"+action_status,true);
			Reporter.log("Testcase execution Completed ",true);
		}

		/*Author: Vijay Gangwar
		*todo: import Group
		*params none
		*/
		@Test (priority = 1, dataProvider = "ImportGroup",dataProviderClass = InfraDataProvider.class )
		public void ImportGroup(String ImportType,String id,String groupName,String filename,String caseType) throws Exception {
			Reporter.log("exeuting testcase ****ImportUser() ***** Test Rail id :C3473 ...",true);
			Reporter.log("description: 1. import user with csv file. 2 find the imported user .",true);
			System.out.println("exeuting testcase ****ImportUser() ***** ...");

		//	String filename="elastica_group_import_template.csv";
			
			String filepath = InfraConstants.INFRA_DATA_LOC + filename;
			
			//check the filename provided is absolute or not
			File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
			
			if(!uploadFile.exists()) {
				System.out.println("Sorry file not exists in the folder"); 
			}
			
			//String attributes = "{\"name\":\"file\", \"file\":\""+filename+ "\"}" ;//"name=file;filename="+filename;   //"{\"name\":\""+filename+ "\"}";
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();		
			builder.addTextBody("name", "file", ContentType.TEXT_PLAIN);
			//builder.addTextBody("file", "filename", ContentType.TEXT_PLAIN);
			builder.addBinaryBody("file", uploadFile, ContentType.APPLICATION_OCTET_STREAM, filename);		
			HttpEntity multipart = builder.build();

			headers = getHeaders();
			//headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=----------------------"+Long.toString(new Date().getTime())));
			String uri =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+"/"+tenantDB+InfraConstants.API_CALL+"groups/import/";
			URI dataUri = ClientUtil.BuidURI(uri);
			
			HttpResponse response =  restClient.doPost(dataUri, headers, null, multipart);
			String responseBody = ClientUtil.getResponseBody(response);
			System.out.println(" ****responseBody ***** ..."+responseBody);
			System.out.println(" ****responseBody ***** ..."+headers);
			// search group
			String userId=null;
			String uri1 =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+groupName;
			Reporter.log("search user uri ."+uri1,true);
			//prepare uri for get user request 
			//URI dataUri1 = ClientUtil.BuidURI(uri1);
			HttpResponse response1 =Infractions.SearchGroup(suiteData, headers, groupName);//  restClient.doGet(dataUri1, headers);
			String responseBody1 = ClientUtil.getResponseBody(response1);
			Reporter.log("search user responseBody: "+responseBody1,true);
			boolean SearchStatus=false;
			//userId=Infrautils.getUserid(responseBody1,"groupslist","name",groupName);
			userId=Infractions.getGroupid(responseBody1, groupName);
			if(caseType.contains("normal"))
			{
				
				if(userId != null)
				{
					SearchStatus=true;
				}
				assertEquals(SearchStatus,true, "Response data verification failed");
				Reporter.log("Expected  result :0"+true,true);
				Reporter.log("Actual result :"+SearchStatus,true);
				Reporter.log("Testcase execution Completed ",true);
				
			}
			else
			{
				if(userId != null)
				{
					SearchStatus=true;
				}
				assertEquals(SearchStatus,false, "Response data verification failed");
				Reporter.log("Expected  result :0"+false,true);
				Reporter.log("Actual result :"+SearchStatus,true);
				Reporter.log("Testcase execution Completed ",true);
				
			}
			
			
		}
		
		
		/*Author: Vijay Gangwar
		*todo: add enduser
		*params none
		*/
		@Test ( priority = 2 ,dataProvider ="editUserData",dataProviderClass = InfraDataProvider.class )
		public void EditUserRole(String Action,String id,String email,String valueUpdated,String updatedField,String payload,String type) throws Exception {
			Reporter.log("exeuting testcase ****EditUserRole() *****  Test Rail id C2104...",true);
			Reporter.log("Description:1.select the user. 1. Edit the  the user Details  ",true);
			
			//print response body
			//System.out.println("search user responseBody: ."+SearchResponse);
			Reporter.log("Perform edit operation on user with email id  "+email,true);
			String userId=Infractions.searchUserid(suiteData, headers, email);//Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
			payload=payload.replace(InfraConstants.DUMMY, userId);
			//perform the edit action
			String uri =suiteData.getReferer()+InfraConstants.EDIT_ACTION;
			URI dataUri = ClientUtil.BuidURI(uri);
			
			HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			Reporter.log("** EditUserRole** Response: "+responseBody,true);
			
			System.out.println("Response: "+responseBody);
			String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
			assertEquals(action_status,"success", "App status verification failed");
			
			// search edited user
//			 SearchUri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
//			Reporter.log("search user uri ."+SearchUri,true);
//			dataUriSearch = ClientUtil.BuidURI(SearchUri);
//			SearchResponse =  restClient.doGet(dataUriSearch, headers);
			HttpResponse SearchResponse=Infractions.SearchUser(suiteData, headers, email);
			String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
			
			//String nameUpdated=searchUser(email);
			
			String newValue=Infrautils.getUserFieldData(SearchResponseBody,"userslist","email",email,updatedField);
			assertEquals(valueUpdated,newValue, "search Data verification failed");
			Reporter.log(" Expected Result "+updatedField+" :",true);
			Reporter.log(" Actual Result first name: "+newValue,true);
			Reporter.log(" Test execution is completed",true);
			
		}

		/*Author: Vijay Gangwar
		*todo: add enduser
		*params none
		*/
		@Test ( priority = 2,dataProvider ="revertUserData",dataProviderClass = InfraDataProvider.class ) 
		public void RevertUserStatus(String Action,String email,String Payload) throws Exception {
			Reporter.log("exeuting testcase ****RevertUserRole() ***** Test Rail id:C2106...");
			System.out.println("exeuting testcase ****RevertUserRole() ***** ...");
		
			//serach  user to be deleted "mayday"
		//	String email="vijay.gangwar+14@infrabeatle.com";
	
			String userId=null;
			
			userId=Infractions.searchUserid(suiteData, headers, email);
			
			//Get headers
			//List<NameValuePair> headers = getHeaders();
			String uri1 =suiteData.getReferer()+"admin/user/ng/edit";
			//String Payload ="{"user":{"first_name":"raj","last_name":"raj","email":"raj.co@infrabeatle.com","secondary_user_id":"","password":"","title":"","work_phone":"","cell_phone":"","access_profiles":[],"is_admin":true,"is_active":true,"notes":"","is_dpo":false}}";
			String payload=Payload.replace(InfraConstants.DUMMY, userId);//"{\"user\":{\"first_name\":\"Edit\",\"last_name\":\"Admin\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false},\"id\":\""+userId+"\"}";
			Reporter.log("payload ::"+payload,true);
			URI dataUri1 = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(),"/admin/user/ng/edit");
			Reporter.log(Action,true);
			//Reporter.log("Request API :"+dataUri,true);
			HttpResponse response1 =  restClient.doPost(dataUri1, headers, null, new StringEntity(payload));
			String responseBody1 = ClientUtil.getResponseBody(response1);
			Reporter.log("Response API :"+responseBody1,true);
			String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
			assertEquals(action_status,"success", "Api Action status verification failed");
		//	String Status =(String) new JSONObject(responseBody1).get("is_active").toString();
			//String Status=Infrautils.getStatus(responseBody1,"updatedRow","is_active");
		//	Reporter.log(" Api status :"+Status,true);
			Reporter.log(" Expected Result success :",true);
			Reporter.log(" Actual Result first name: "+action_status,true);
			Reporter.log(" Test execution is completed",true);
			
		}
		
		
		
		
		/*Author: Vijay Gangwar
		*todo: export user
		*params none
		*/
		@Test ( priority = 1 )
		public void exportUsers() throws Exception {
			Reporter.log("exeuting testcase ****exportUsers() ***** ...",true);
			System.out.println("exeuting testcase ****exportUsers() ***** ...");
			
			//Get headers
		    //Map<String,String> headers = getRequestHeaders_eoe();
			String uri =suiteData.getReferer()+"/admin/user/export";
			//https://eoe.elastica-inc.com/admin/user/ng/list/0?limit=30&offset=0&order_by=first_name&query=raj1
			String QueryParams ="?query=raj";
			String url =uri+QueryParams;//"https://eoeapi.elastica-inc.com/bop/editSubscription/?name=Investigate&category=Elastica&action=revoke&user=vijay.gangwar%40infrabeatle.com&priDomain=newblr.com";	
			Reporter.log("Request Method type: GET",true);
			Reporter.log("Request API :"+url,true);
			URI dataUri = ClientUtil.BuidURI(url);
			HttpResponse response=restClient.doGet(dataUri, headers);
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode(), "Response code verification failed");
			System.out.println("** exportUsers** Response: "+response.getStatusLine().getStatusCode());
			Reporter.log("** exportUsers** Response: "+response.getStatusLine().getStatusCode(),true);
			Reporter.log("user are exported , admin to check email for link ",true);
			
			
		}
		
		
		/*Author: Vijay Gangwar
		*todo:  deactivateUser request 
		*params none
		*/
		@Test( priority = 2 )
		public void checkActiveUserFilter() throws Exception {
			Reporter.log("exeuting testcase ****checkActiveUserFilter() ***** Test Rail id:C2148...",true);
			//System.setProperty("jsse.enableSNIExtension", "false");
			//Added comment
			//Get headers
			//List<NameValuePair> headers = getHeaders();
			String uri =suiteData.getReferer()+"/admin/";
			//https://eoe.elastica-inc.com/admin/user/ng/list/0?is_active=true&limit=30&offset=0&order_by=first_name
			//https://eoe.elastica-inc.com/admin/user/ng/list/0?limit=30&offset=0&order_by=first_name&query=raj1
			String QueryParams = "user/ng/list/0?is_active=true&limit=30&offset=0&order_by=first_name";
			String url =uri+QueryParams;//"https://eoeapi.elastica-inc.com/bop/editSubscription/?name=Investigate&category=Elastica&action=revoke&user=vijay.gangwar%40infrabeatle.com&priDomain=newblr.com";	
			URI dataUri = ClientUtil.BuidURI(url);
			Reporter.log("Request Method type: GET",true);
			Reporter.log("Request API :"+dataUri,true);
			HttpResponse response =  restClient.doGet(dataUri, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
			assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			System.out.println("** checkActiveUserFilter** Response: "+response.getStatusLine().getStatusCode());
			String responseBody = ClientUtil.getResponseBody(response);
			System.out.println("Response: "+responseBody);
			Reporter.log("Expected status :200 ",true);
			Reporter.log("Actual status :"+HttpStatus.SC_OK,true);
			Reporter.log("Testcase execution Completed ",true);
			
		}
		/*Author: Vijay Gangwar
		*todo:  deactivateUser request 
		*params none
		*/
		@Test( priority = 3 )
		public void checkInctiveUserFilter() throws Exception {
			Reporter.log("exeuting testcase ****checkinActiveUserFilter() ***** ...",true);
			System.out.println("exeuting testcase ****checkinActiveUserFilter() ***** ...");
			 
			//Added comment
			//Reset the filters
		//	https://eoe.elastica-inc.com/admin/user/ng/list/0?limit=30&offset=0&order_by=first_name
			String uri =suiteData.getReferer()+"/admin/";
			String QueryParams = "user/ng/list/0?limit=30&offset=0&order_by=first_name";
				
			//Get headers
		   // Map<String,String> headers = getRequestHeaders_eoe();
		    String url =uri+QueryParams;//"https://eoeapi.elastica-inc.com/bop/editSubscription/?name=Investigate&category=Elastica&action=revoke&user=vijay.gangwar%40infrabeatle.com&priDomain=newblr.com";	
		    URI dataUri = ClientUtil.BuidURI(url);
		    HttpResponse response=restClient.doGet(dataUri,headers);
			Reporter.log("Request Method type: GET",true);
			Reporter.log("Request API :"+url,true);
			assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode(), "Response code verification failed");
			
			String uri1 =suiteData.getReferer()+"/admin/";
			String QueryParams1 = "user/ng/list/0?s_active=true&limit=30&offset=0&order_by=first_name";
			String url1 =uri1+QueryParams1;//"https://eoeapi.elastica-inc.com/bop/editSubscription/?name=Investigate&category=Elastica&action=revoke&user=vijay.gangwar%40infrabeatle.com&priDomain=newblr.com";	
			URI dataUri1 = ClientUtil.BuidURI(url1);
			HttpResponse response1=restClient.doGet(dataUri1,headers);
			assertEquals(HttpStatus.SC_OK, response1.getStatusLine().getStatusCode(), "Response code verification failed");
			System.out.println("** checkInctiveUserFilter** Response: "+response1.getStatusLine().getStatusCode());
			Reporter.log("** checkInctiveUserFilter** Response: "+response1.getStatusLine().getStatusCode(),true);
			
			Reporter.log("Expected status :200 ",true);
			Reporter.log("Actual status :"+HttpStatus.SC_OK,true);
			Reporter.log("Testcase execution Completed ",true);
			
		}
		
		
		/*Author: Vijay Gangwar
		*todo: sendResetPasswordUser request 
		*params none
		*/
		@Test( priority = 1 )
		public void sendResetPasswordUser() throws Exception {
			Reporter.log("Description:1. Admin to reset user password ** ...",true);
			Reporter.log("Description:2. user gets  reset password email ** ...",true);
			Reporter.log("Test Rail id: C2107",true);
			//search  user ,for password reset
			String email="vijay.gangwar+16@infrabeatle.com";
//			String uri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
//			Reporter.log("search user uri ."+uri,true);
//			//prepare uri for get user request 
//			URI dataUri = ClientUtil.BuidURI(uri);
//			HttpResponse response =  restClient.doGet(dataUri, headers);
//			String responseBody = ClientUtil.getResponseBody(response);
//			//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
//			System.out.println(" ****responseBody ***** ..."+responseBody);
			//Reporter.log("search user responseBody: ."+responseBody,true);
			String userId=Infractions.searchUserid(suiteData, headers, email);//Infrautils.getUserid(responseBody,"userslist","email","vijay.gangwar+16@infrabeatle.com");
			//System.out.println(" get list of users Response: "+responseBody);		
			String uri1 = suiteData.getReferer()+InfraConstants.RESET_PASSWORD;
			String payload=InfraConstants.PAYLOAD_RESET;
			payload=payload.replace("uid", userId);
			URI dataUri1 = ClientUtil.BuidURI(uri1);
			// execute the request
			HttpResponse response1 =  restClient.doPost(dataUri1, headers, null, new StringEntity(payload));
			String responseBody1 = ClientUtil.getResponseBody(response1);
			String action_status =(String) new JSONObject(responseBody1).get("api_response").toString();
			// validate Response 
			Reporter.log("Expected api response: 0 ",true);
			Reporter.log("Actual api response:  "+action_status,true);
			assertEquals("0", action_status, "Response code verification failed");
			Reporter.log("Password reset test completed ",true);		
			
		}
		/*Author: Vijay Gangwar
		*todo: addGroup request 
		*params none
		*/
		@Test ( priority = 1, dataProvider = "SecondaryIdUserData",dataProviderClass = InfraDataProvider.class )
		public void createUserSecondryID(String userType,String email,String id,String payload,String caseType) throws Exception {
			Reporter.log("****createUser() ***** Test Rail id: ..."+id,true);
			// check if user is alrady present.
			HttpResponse response=Infractions.SearchUser(suiteData, headers,  email);
			String responseBody = ClientUtil.getResponseBody(response);
			String userId = null;
			HttpResponse responseCreatUser=null;
			userId=Infractions.getUserid(responseBody, email);
			if(null!=userId&&(!(userId.isEmpty())))
			{
				// delete the existing user
				Reporter.log(" Delete user with id: "+userId);
				HttpResponse responseDeleteUser =Infractions.deleteUser(suiteData, headers, userId);
				assertEquals( responseDeleteUser.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");		
				Reporter.log("user is deleted",true);
				Thread.sleep(3000);
				
			}
			else
			{
				// do nothing
			}
			
			
			// add user
			response = Infractions.createUser(suiteData,headers,payload);
			//responseBody= ClientUtil.getResponseBody(responseCreatUser);
			
			if(caseType.equalsIgnoreCase("normal"))
			{
				responseBody= ClientUtil.getResponseBody(response);
				assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				
				
			}
			else
			{
				responseBody= ClientUtil.getResponseBody(response);
				if(responseBody.contains("500"))
				{
					
					System.out.println("** user list is empty** Response: ");
					assertEquals( 500,HttpStatus.SC_INTERNAL_SERVER_ERROR, "Response code verification failed");
					
				}
				else
				{
					String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
					
					if(action_status.contains("success"))
					{
						System.out.println("** user action :"+action_status);
					}
					else
					{
						System.out.println("** user_action failed :"+action_status);
					}
					
					System.out.println("** user list is empty** Response: "+responseBody);
					String uri =suiteData.getReferer()+"/admin/user/ng/list/0?limit=30&offset=0&order_by=first_name&query="+email.substring(0, 4);
					Reporter.log("search user uri ."+uri,true);
					//search the error user in db
					URI dataUri1 = ClientUtil.BuidURI(uri);
					HttpResponse response1 =  restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
					String responseBody1 = ClientUtil.getResponseBody(response1);
					if(!userType.contains("same email"))
					{
					assertEquals(Infrautils.getUserid(responseBody1,"userslist","email",email),null, "Response code verification failed");
					Reporter.log("Expected status :null ",true);
					Reporter.log("Actual status :"+null,true);
					Reporter.log("Testcase execution Completed ",true);
					}
				}
				
		}
			
		}
			
		
		@Test( priority = 1 ,dataProviderClass = InfraDataProvider.class, dataProvider = "groupData" )
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
//				HttpResponse response=Infractions.createGroup(suiteData, headers, payload);
//				
//				responseBody = ClientUtil.getResponseBody(response);
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
//			}
//			else
//			{
//				Reporter.log("Group already present: ",true);
//			}
			
			//System.out.println("** addGroup ** Response: "+response.getStatusLine().getStatusCode());
			
		}


		
		
		/*Author: Vijay Gangwar
		*todo: addGroup request 
		*params none
		*/
	
		@Test( priority = 2,dataProvider = "deleteGroupData",dataProviderClass = InfraDataProvider.class )
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
		/*Author: Vijay Gangwar
		*todo: addGroup request 
		*params none
		*/
		@Test( priority = 1 )
		public void addInactiveGroup() throws Exception {
			Reporter.log("exeuting testcase ****addGroup() ***** ...",true);
			System.out.println("exeuting testcase ****addGroup() ***** ...");
			//String uri =suiteData.getReferer()+"admin/group/ng/add/";
			String payload =InfraConstants.GROUP_PL;
			//String payload="{\"user\":{\"first_name\":\"raj1\",\"last_name\":\"raj1\",\"email\":\"raj1.co@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}";
			
			//URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/admin/group/ng/add/");//BuidURI(uri);
			//Reporter.log("Request Method type: POST",true);
			//Reporter.log("Request API :"+dataUri,true);
			HttpResponse response =  Infractions.createGroup(suiteData, headers, payload);//restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			Reporter.log("** addGroup** Response: ",response.getStatusLine().getStatusCode(),true);
			Reporter.log("** addGroup** Response: "+responseBody,true);
			
			System.out.println("** addGroup ** Response: "+response.getStatusLine().getStatusCode());
			
		}
		/*Author: Vijay Gangwar
		*todo: addGroupSpecial request 
		*params none
		*/
		@Test( priority = 1 )
		public void addGroupSpecial() throws Exception {
			Reporter.log("exeuting testcase ****addGroupSpecial() ***** Test Rail id:C2131...",true);
			System.out.println("exeuting testcase ****addGroupSpecial() ***** ...");
			//String uri =suiteData.getReferer()+"admin/group/ng/add/";
			String payload ="{\"group\":{\"name\":\"Blr##$$\",\"description\":\"testgrp\",\"is_active\":true,\"notes\":\"testgrp\"}}";
			//String payload="{\"user\":{\"first_name\":\"raj1\",\"last_name\":\"raj1\",\"email\":\"raj1.co@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}";
//			URI dataUri = ClientUtil.BuidURI(uri);
//			Reporter.log("Request Method type: POST",true);
//			Reporter.log("Request API :"+dataUri,true);
			HttpResponse response =  Infractions.createGroup(suiteData, headers, payload);// restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			Reporter.log("** addGroupSpecial** Response: ",response.getStatusLine().getStatusCode(),true);
			System.out.println("** addGroupSpecial ** Response: "+response.getStatusLine().getStatusCode());
			Reporter.log("Expected status : "+HttpStatus.SC_OK,true);
			Reporter.log("Actual status :"+response.getStatusLine().getStatusCode(),true);
			Reporter.log("Testcase execution Completed ",true);
			 

			
		}
		
		/*Author: Vijay Gangwar
		*todo: addGroupSpecial request 
		*params none
		*/
		@Test( priority = 1 )
		public void addDuplicategroup() throws Exception {
			Reporter.log("exeuting testcase ****addDuplicategroup() ***** Test Rail id :C2167...",true);
			System.out.println("exeuting testcase ****addDuplicategroup() ***** ...");
			//String uri =suiteData.getReferer()+"admin/group/ng/add/";
			String payload ="{\"group\":{\"name\":\"Blr\",\"description\":\"testgrp\",\"is_active\":true,\"notes\":\"testgrp\"}}";
			//String payload="{\"user\":{\"first_name\":\"raj1\",\"last_name\":\"raj1\",\"email\":\"raj1.co@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}";
//			URI dataUri = ClientUtil.BuidURI(uri);
//			Reporter.log("Request Method type: POST",true);
//			Reporter.log("Request API :"+dataUri,true);
			HttpResponse response = Infractions.createGroup(suiteData, headers, payload);// restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			//assertEquals(200, response.getStatusLine().getStatusCode(), "Response code verification failed");
			Reporter.log("** addDuplicategroup** Response: ",response.getStatusLine().getStatusCode(),true);
			System.out.println("** addDuplicategroup ** Response: "+response.getStatusLine().getStatusCode());
			Reporter.log("Expected  result :200"+false,true);
			Reporter.log("Actual result :"+HttpStatus.SC_OK,true);
			Reporter.log("Testcase execution Completed ",true);
			
			
		}
		
		/*Author: Vijay Gangwar
		*todo: check name for maximum length 
		*params none
		*/
		@Test( priority = 1 )
		public void addGroupMaxChar() throws Exception {
			Reporter.log("exeuting testcase ****addGroupSpecial() ***** Test Rail id :C2133...",true);
			System.out.println("exeuting testcase ****addGroupSpecial() ***** ...");
			String uri =suiteData.getReferer()+"/admin/group/ng/add/";
			String payload ="{\"group\":{\"name\":\"BlrTestGroupforMaxCharacterLenthTestBlrTestGroupforMaxCharacterLenthTest\",\"description\":\"testgrp\",\"is_active\":true,\"notes\":\"testgrp\"}}";
			//String payload="{\"user\":{\"first_name\":\"raj1\",\"last_name\":\"raj1\",\"email\":\"raj1.co@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}";
			System.out.println(" ## uri reques :" + uri );
			URI dataUri = ClientUtil.BuidURI(uri);
			Reporter.log("Request Method type: POST",true);
			Reporter.log("Request API :"+dataUri,true);
			HttpResponse response = Infractions.createGroup(suiteData, headers, payload);//restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			Reporter.log("** addGroupSpecial** Response: ",response.getStatusLine().getStatusCode(),true);
			System.out.println("** addGroupSpecial ** Response: "+response.getStatusLine().getStatusCode());
			Reporter.log("Expected  result :200"+false,true);
			Reporter.log("Actual result :"+HttpStatus.SC_OK,true);
			Reporter.log("Testcase execution Completed ",true);
			
			 
			
		}
		
		/*Author: Vijay Gangwar
		*todo: perform group edit operation
		*params : dataprovider
		*/
		@Test ( priority = 2 ,dataProvider ="editGroupData",dataProviderClass = InfraDataProvider.class )
		public void editGroupData(String Action,String id,String name,String valueUpdated,String updatedField,String payload,String type) throws Exception {
			Reporter.log("exeuting testcase ****editGroupDetails *****  ...",true);
			Reporter.log("Description:1.select the group. 1. Edit the  the group Details  ",true);
//			String SearchUri =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+name;
//			Reporter.log("search user uri ."+SearchUri,true);
//			URI dataUriSearch = ClientUtil.BuidURI(SearchUri);
			HttpResponse SearchResponse = Infractions.SearchGroup(suiteData, headers, name) ;//restClient.doGet(dataUriSearch, headers);
			String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
			//print response body
			//System.out.println("search user responseBody: ."+SearchResponse);
			String groupId=Infractions.getGroupid(SearchResponseBody, name);//Infrautils.getUserid(SearchResponseBody,"groupslist","name",name);
			payload=payload.replace(InfraConstants.DUMMY, groupId);
			//prepare url edit group actions
			Reporter.log("execute  edit group request ",true);
			String uri =suiteData.getReferer()+InfraConstants.GROUP_EDIT;
			URI dataUri = ClientUtil.BuidURI(uri);
			HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			if(type.contains("normal"))
			{
				System.out.println("Response: "+responseBody);
				String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				assertEquals(action_status,"success", "group data verification failed");
				
				// search edited user
//				SearchUri =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+name;
//				Reporter.log("search edited user ",true);
//				dataUriSearch = ClientUtil.BuidURI(SearchUri);
				SearchResponse = Infractions.SearchGroup(suiteData, headers, name);// restClient.doGet(dataUriSearch, headers);
				SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
				//validat searched query
				
				String newValue=Infrautils.getUserFieldData(SearchResponseBody,"groupslist","name",name,updatedField);
				assertEquals(valueUpdated,newValue, "App status verification failed");
				Reporter.log(" Expected Result "+valueUpdated+" :",true);
				Reporter.log(" Actual Result first name: "+newValue,true);
			}
			else
			{
				System.out.println("Response: "+responseBody);
				String api_responses =(String) new JSONObject(responseBody).get("api_response").toString();
				Reporter.log(" Test execution is completed"+api_responses,true);
			}
			
			Reporter.log(" Test execution is completed",true);
		
			
		}
		
			
				
		/*Author: Vijay Gangwar
		*todo: doAscendOrderGroup sorting 
		*params none
		*/
		@Test( priority = 1 )
		public void doAscendOrderGroup() throws Exception {
			Reporter.log("exeuting testcase ****doAscendOrderGroup() ***** ...",true);
			System.out.println("exeuting testcase ****doAscendOrderGroup() ***** ...");
			Reporter.log(" Description : 1 Send get request for ascending order of group list .2. Validate if they are in acending order",true);
			
			//Get headers
			//prepare the url for get order
			String uri =suiteData.getReferer()+"/admin/group/ng/list/0?";
			String QueryParams ="limit=30&offset=0&order_by=full_name";
			String url =uri+QueryParams;	
			URI dataUri = ClientUtil.BuidURI(url);
			Reporter.log("Request Method type: POST",true);
			Reporter.log("Request API :"+dataUri,true);
			HttpResponse response=restClient.doGet(dataUri, headers);//doGetRest(url,headers);
		//	System.out.println("** sortUserAscend** Response: "+response.getStatusLine().getStatusCode());
			Reporter.log("** doAscendOrderGroup** Response: "+response.getStatusLine().getStatusCode(),true);
			assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode(), "Response code verification failed");
			String responseBody = ClientUtil.getResponseBody(response);
			boolean status=Infrautils.orderCheck(responseBody,"groupslist", "name","ascend");
			Reporter.log(" Expected Result  api status: true",true);
			Reporter.log(" Actual Result api action status: "+status,true);
			assertEquals(status, true, "Response data verification failed");
			Reporter.log(" Test execution is completed",true);
		
//			
		}
		
		/*Author: Vijay Gangwar
		*todo: Groupdeactivate request 
		*params none
		*/
	//	@Test( priority = 1 )
		public void doGroupDeactivate() throws Exception {
			Reporter.log("exeuting testcase ****doGroupDeactivate() ***** Test Rail id :C2145...",true);
			System.out.println("exeuting testcase ****doGroupDeactivate() ***** ...");
			String uri =suiteData.getReferer()+"/admin/group/toggle_activation";
			String payload ="{\"group\":{\"modified_by\":\"admin@infrabeatle.com\",\"description\":\"test group\",\"name\":\"Blr test\",\"notes\":\",\"is_active\":true,\"created_by\":\"admin@infrabeatle.com\",\"id\":\"55b733999dfa5113fb353679\",\"parent_group\":null,\"created_on\":\"2015-07-28T07:47:37.574000\",\"modified_on\":\"2015-07-30T06:57:59.433000\",\"email\":\"Blr test\",\"resource_uri\":\"/elasticaco/api/admin/v1/groups/55b733999dfa5113fb353679/\"},\"action\":false}";
			//String payload="{\"user\":{\"first_name\":\"raj1\",\"last_name\":\"raj1\",\"email\":\"raj1.co@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}";
			
			URI dataUri = ClientUtil.BuidURI(uri);
			Reporter.log("Request Method type: POST",true);
			Reporter.log("Request API :"+dataUri,true);
			HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			System.out.println("** doGroupDeactivate** Response: "+response.getStatusLine().getStatusCode());
			Reporter.log("** doGroupDeactivate** Response: "+response.getStatusLine().getStatusCode(),true);
			assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode(), "Response code verification failed");
				
//			
		}
		/*Author: Vijay Gangwar
		*todo: ascending order sorting request 
		*params none
		*/
		@Test( priority = 1 )
		public void dosortUserAscend() throws Exception {
			Reporter.log("exeuting testcase ****dosortUserAscend() ***** Test Rail id:C75...",true);
		    String uri =suiteData.getReferer()+"/admin/user/ng/list/0?";
			String QueryParams ="limit=60&offset=0&order_by=full_name";
			String url =uri+QueryParams;
			URI dataUri = ClientUtil.BuidURI(url);
			Reporter.log("Request Method type: GET",true);
			Reporter.log("Request API :"+dataUri,true);
			HttpResponse response=restClient.doGet(dataUri, headers);//doGetRest(url,headers);
			System.out.println("** sortUserAscend** Response: "+response.getStatusLine().getStatusCode());
			Reporter.log("** revokeDetect** Response: "+response.getStatusLine().getStatusCode(),true);
			assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode(), "Response code verification failed");
			String responseBody = ClientUtil.getResponseBody(response);
			boolean status=Infrautils.orderCheck(responseBody,"userslist", "first_name","ascend");
			Reporter.log(" Expected Result  api status: true",true);
			Reporter.log(" Actual Result api action status: "+status,true);
			assertEquals(status, true, "Response data verification failed");
			Reporter.log(" Test execution is completed",true);
		
					
			
		}
		/*Author: Vijay Gangwar
		*todo: descending order sorting request 
		*params none
		*/
		@Test( priority = 1 )
		public void sortUserDescend() throws Exception {
			Reporter.log("exeuting testcase ****sortUserDescend() ***** ...",true);
			//Get headers
			//prepare the url for the list
		    String uri =suiteData.getReferer()+"/admin/user/ng/list/0?";
			String QueryParams ="limit=60&offset=0&order_by=-full_name";
			
			String url =uri+QueryParams;
			URI dataUri = ClientUtil.BuidURI(url);
			Reporter.log("Request Method type: POST",true);
			Reporter.log("Request API :"+dataUri,true);
			HttpResponse response=restClient.doGet(dataUri, headers);//doGetRest(url,headers);S
			Reporter.log("** sortUserDescend** Response: "+response.getStatusLine().getStatusCode(),true);
			assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode(), "Response code verification failed");
			//response.
			String responseBody = ClientUtil.getResponseBody(response);
			boolean status=Infrautils.orderCheck(responseBody,"userslist", "first_name","descend");
			Reporter.log(" Expected Result  api status: true",true);
			Reporter.log(" Actual Result api action status: "+status,true);
			assertEquals(status, true, "Response data verification failed");
			Reporter.log(" Test execution is completed",true);
		
		}
		
		
		
		/*Author: Vijay Gangwar
		*todo: Create users
		*params dataProvider
		*/
		
		@Test ( priority = 1, dataProvider = "createUserData",dataProviderClass = InfraDataProvider.class )
		public void createUser(String userType,String email,String id,String payload,String caseType) throws Exception {
			Reporter.log("****createUser() ***** Test Rail id: ..."+id,true);
			// check if user is alrady present.
			HttpResponse response=Infractions.SearchUser(suiteData, headers,  email);
			String responseBody = ClientUtil.getResponseBody(response);
			String userId = null;
			HttpResponse responseCreatUser=null;
			userId=Infractions.getUserid(responseBody, email);
			if(null!=userId&&(!(userId.isEmpty())))
			{
				// delete the existing user
				Reporter.log(" Delete user with id: "+userId);
				HttpResponse responseDeleteUser =Infractions.deleteUser(suiteData, headers, userId);
				assertEquals( responseDeleteUser.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");		
				Reporter.log("user is deleted",true);
				Thread.sleep(3000);
				
			}
			else
			{
				// do nothing
			}
			
			
			// add user
			responseCreatUser = Infractions.createUser(suiteData,headers,payload);
			responseBody= ClientUtil.getResponseBody(responseCreatUser);
			if(caseType.equalsIgnoreCase("normal"))
			{
				
				assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				assertEquals(action_status,"success", "Response code verification failed");
					
				//search added user
				HttpResponse SearchResponse=Infractions.SearchUser(suiteData, headers,  email);
				String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
					
				String uid=Infractions.getUserid(SearchResponseBody, email);					
				Reporter.log("Expected Result :new user creatd with id :",true);
				Reporter.log("Actual Result :new user creatd with id :"+uid,true);
				   
				
			}
			else
			{
				

				if(responseCreatUser.getStatusLine().getStatusCode()==400)
				//if(response.getStatusLine().getStatusCode()==500||responseBody.contains("500"))
				{
					
					System.out.println("** user list is empty** Response: ");
					assertEquals( InfraConstants.BAD_REQUEST,HttpStatus.SC_BAD_REQUEST, "Response code verification failed");
					//String api_response =(String) new JSONObject(responseBody).get("api_response").toString();
					System.out.println("**api_response for error cases :"+responseBody);
					
				}
				else
				{
					String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
					//String action_status =(String) new JSONObject(responseBody).get("action_status").toString();

					assertEquals(action_status,"", "Response code verification failed");
					Reporter.log("Error User is not added  ...",true);
					 
					
				}
				
			
				
			}
			
		}
		
		
		/*Author: Vijay Gangwar
		*todo: add enduser
		*params none
		*/
		
		@Test ( priority = 1 )
		public void AddDpoUser() throws Exception {
			Reporter.log("exeuting testcase ****AddDpoUser() ***** Assembl id:C2094 ...",true);
			Reporter.log("Description: Create the DPO user Enable request for Elastica  App  :*** ",true);
			Reporter.log(" Steps : 1 search if user already exist. 2.  Add  DPO user.3. search added user ",true);			
			// search user 
			// check if user is alrady present.
			String userId =Infractions.searchUserid(suiteData, headers, InfraConstants.DPO_EMAIL);
			if(null!=userId&&(!(userId.isEmpty())))
				{
					// delete the existing user
					Reporter.log(" Delete user with id: "+userId);
					HttpResponse responseDeleteUser =Infractions.deleteUser(suiteData, headers, userId);
					assertEquals( responseDeleteUser.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");		
					Reporter.log("user is deleted",true);
					Thread.sleep(3000);
							
					HttpResponse SearchResponse=Infractions.SearchUser(suiteData, headers,  InfraConstants.DPO_EMAIL);
					String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
					String SearchUserId = null;
							
					SearchUserId=Infractions.getUserid(SearchResponseBody, InfraConstants.DPO_EMAIL);
					assertEquals( SearchUserId.isEmpty(),true, "Response code verification failed");
							
				}
				else
				{
					// do nothing						
				}
						
				// add user
				HttpResponse responseCreatUser = Infractions.createUser(suiteData,headers,InfraConstants.ADD_DPO_pl);
				String responseBody= ClientUtil.getResponseBody(responseCreatUser);
				String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				assertEquals(action_status,"success", "Response code verification failed");
				// search newly created dpo user
				HttpResponse responseSearch=Infractions.SearchUser(suiteData, headers, InfraConstants.DPO_EMAIL);
				String responseSearchBody = ClientUtil.getResponseBody(responseSearch);
				String SearchUserId = null;
				SearchUserId=Infractions.getUseremail(responseSearchBody, InfraConstants.DPO_EMAIL);
				//String user =Infractions.searchUserid(suiteData, headers, InfraConstants.DPO_EMAIL);
				Reporter.log(" Expected Result: Added user: "+InfraConstants.DPO_EMAIL,true);
				Reporter.log(" Actual Result: added user found:"+SearchUserId,true);
				assertEquals(action_status,"success", "Response code verification failed");		
	
		}

		/*Author: Vijay Gangwar
		*todo: add enduser
		*params none
		*/
		@Test (  priority = 1, dataProviderClass = InfraDataProvider.class,dataProvider = "UsertoGrp"  )
		public void AddUsertoGroup(String email,String id,String payload,String group,String createPayload) throws Exception {
			Reporter.log("exeuting testcase ****AddUsertoGroup() ***** Assembl id:C2094 ...",true);
			Reporter.log("Description: Create the  user Enable request for Elastica  App  :*** ",true);
			Reporter.log(" Steps : 1 search if user already exist. 2.  Add  DPO user.3. add user to group and search added user ",true);			
			// search user			
			// check if user is alrady present.
			String userId =Infractions.searchUserid(suiteData, headers, email);
			// add end user if not present
			if(userId.isEmpty())
			{

				Infractions.createUser(suiteData, headers, createPayload);
			}
			
				///search group and
				HttpResponse	 response2=Infractions.SearchGroup(suiteData, headers, group);
				String responseBody2 = ClientUtil.getResponseBody(response2);
				//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
				System.out.println("e ****responseBody ***** ..."+responseBody2);
				Reporter.log("search user responseBody: ."+responseBody2,true);
				String GroupId=Infrautils.getUserid(responseBody2,"groupslist","name",group);
				
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
//			}
//			else
//			{
//				Reporter.log("user not not present: ",true);
//				Reporter.log(" Test execution is completed",true);
//			}
		}
		


		/*Author: Vijay Gangwar
		*todo: add enduser
		*params none
		*/
		@Test (  priority = 1, dataProviderClass = InfraDataProvider.class,dataProvider = "GrouptoUser" )
		public void AssignGroupToUser(String email,String id,String payload,String group) throws Exception {
			Reporter.log("exeuting testcase ****AssignGroupToUser() ***** Assembl id:C2094 ...",true);
			Reporter.log("Description: Assign User to the group  :*** ",true);
			Reporter.log(" Steps : 1 search if user already exist. 2.  assing user to group  .3. search added user ",true);
			
			// search user
			String userId =Infractions.searchUserid(suiteData, headers, email);
			// add end user if not present
			if(null!=userId)
			{

				///search group and
				String uri2 =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+group;
				//Reporter.log("search user uri ."+uri,true);
				URI dataUri2 = ClientUtil.BuidURI(uri2);
				HttpResponse	 response2 =  restClient.doGet(dataUri2, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
				String responseBody2 = ClientUtil.getResponseBody(response2);
				//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
				System.out.println("e ****responseBody ***** ..."+responseBody2);
				//Reporter.log("search user responseBody: ."+responseBody2,true);
				String GroupId=Infrautils.getUserid(responseBody2,"groupslist","name",group);
				
				// check if user already present in group 
				//  add user to group
				// search user 
				String	searchUri1 =suiteData.getReferer()+"/admin/group/users?group_name="+group;
				Reporter.log("search user uri ."+searchUri1,true);
				//String payload=InfraConstants.ASSING_USER_PL;
				URI SearchdataUri1 = ClientUtil.BuidURI(searchUri1);
				HttpResponse Searchresponse =  restClient.doGet(SearchdataUri1, headers);//doPost(SearchdataUri, headers, null, new StringEntity(payload));
				Thread.sleep(3000);
				String SearchResponseBody = ClientUtil.getResponseBody(Searchresponse);
				String user1= null;
				 user1=Infrautils.SearchUserInGrp(SearchResponseBody,"tenantgroupusers","user_email",email,"group_name");
				//String grpId=getUserid(SearchResponseBody1,"tenantgroupusers","user_email",email);
				if(null==user1)
				{
					String searchUri =suiteData.getReferer()+"/admin/group/assign_users";
					Reporter.log("search user uri ."+searchUri,true);
					
					URI SearchdataUri = ClientUtil.BuidURI(searchUri);
					HttpResponse Searchresponse2 =  restClient.doPost(SearchdataUri, headers, null, new StringEntity(payload));
					String SearchResponseBody2 = ClientUtil.getResponseBody(Searchresponse2);
					Reporter.log("responseBody: "+SearchResponseBody2,true);
					String action_status =(String) new JSONObject(SearchResponseBody2).get("action_status").toString();
					assertEquals(action_status,"success", "Response code verification failed");
					// cross check for user asignment
					 searchUri =suiteData.getReferer()+"/admin/group/users?group_name="+group;
					Reporter.log("search user uri ."+searchUri,true);
					//String payload=InfraConstants.ASSING_USER_PL;
					 SearchdataUri = ClientUtil.BuidURI(searchUri);
					HttpResponse Searchresponse1 =  restClient.doGet(SearchdataUri, headers);//doPost(SearchdataUri, headers, null, new StringEntity(payload));
					Thread.sleep(3000);
					String SearchResponseBody1 = ClientUtil.getResponseBody(Searchresponse1);
					//String user= SearchName(SearchResponseBody,"tenantgroupusers","user_email",email);
					String user=Infrautils.SearchUserInGrp(SearchResponseBody1,"tenantgroupusers","user_email",email,"group_name");
					assertEquals(user,group, "Group id do not match verification failed");
					Reporter.log(" Expected Result: Added user:"+group,true);
					Reporter.log(" Actual Result: added user found:"+user,true);
					
					Reporter.log(" Test execution is completed",true);
				}
				else
				{
					Reporter.log("user already  present in group: ",true);
					Reporter.log(" Test execution is completed",true);
				}
				
				
			}
			else
			{
				Reporter.log("user not not present: ",true);
				Reporter.log(" Test execution is completed",true);
			}
			
			
				
			
		}
		
		/*Author: Vijay Gangwar
		*todo: add enduser
		*params none
		*/
		@Test (  priority = 1, dataProviderClass = InfraDataProvider.class,dataProvider = "GrouptoUser" )
		public void RemoveGrouptoUser(String email,String id,String payload,String group) throws Exception {
			Reporter.log("exeuting testcase ****RemoveGrouptoUser(String, String, String, String)() ***** Assembl id:C2094 ...",true);
			Reporter.log("Description: Assign User to the group  :*** ",true);
			Reporter.log(" Steps : 1 search if user already exist. 2.  assing user to group  .3. search added user ",true);
			
			// search user
			String userId =Infractions.searchUserid(suiteData, headers, email);
			// add end user if not present
			if(null!=userId)
			{

				///search group and
				String uri2 =suiteData.getReferer()+InfraConstants.SEARCH_GROUP+group;
				//Reporter.log("search user uri ."+uri,true);
				URI dataUri2 = ClientUtil.BuidURI(uri2);
				HttpResponse	 response2 =  restClient.doGet(dataUri2, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
				String responseBody2 = ClientUtil.getResponseBody(response2);
				//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
				System.out.println("e ****responseBody ***** ..."+responseBody2);
				//Reporter.log("search user responseBody: ."+responseBody2,true);
				String GroupId=Infrautils.getUserid(responseBody2,"groupslist","name",group);
				
				// check if user already present in group 
				//  add user to group
				// search user 
				String	searchUri1 =suiteData.getReferer()+"/admin/group/users?group_name="+group;
				Reporter.log("search user uri ."+searchUri1,true);
				//String payload=InfraConstants.ASSING_USER_PL;
				URI SearchdataUri1 = ClientUtil.BuidURI(searchUri1);
				HttpResponse Searchresponse =  restClient.doGet(SearchdataUri1, headers);//doPost(SearchdataUri, headers, null, new StringEntity(payload));
				Thread.sleep(3000);
				String SearchResponseBody = ClientUtil.getResponseBody(Searchresponse);
				String user1= null;
				 user1=Infrautils.SearchUserInGrp(SearchResponseBody,"tenantgroupusers","user_email",email,"group_name");
				//String grpId=getUserid(SearchResponseBody1,"tenantgroupusers","user_email",email);
				if(null==user1)
				{
					String searchUri =suiteData.getReferer()+"/admin/group/assign_users";
					Reporter.log("search user uri ."+searchUri,true);
					
					URI SearchdataUri = ClientUtil.BuidURI(searchUri);
					HttpResponse Searchresponse2 =  restClient.doPost(SearchdataUri, headers, null, new StringEntity(payload));
					String SearchResponseBody2 = ClientUtil.getResponseBody(Searchresponse2);
					Reporter.log("responseBody: "+SearchResponseBody2,true);
					String action_status =(String) new JSONObject(SearchResponseBody2).get("action_status").toString();
					assertEquals(action_status,"success", "Response code verification failed");
					// cross check for user asignment
					 searchUri =suiteData.getReferer()+"/admin/group/users?group_name="+group;
					Reporter.log("search user uri ."+searchUri,true);
					//String payload=InfraConstants.ASSING_USER_PL;
					 SearchdataUri = ClientUtil.BuidURI(searchUri);
					HttpResponse Searchresponse1 =  restClient.doGet(SearchdataUri, headers);//doPost(SearchdataUri, headers, null, new StringEntity(payload));
					Thread.sleep(3000);
					String SearchResponseBody1 = ClientUtil.getResponseBody(Searchresponse1);
					//String user= SearchName(SearchResponseBody,"tenantgroupusers","user_email",email);
					String user=Infrautils.SearchUserInGrp(SearchResponseBody1,"tenantgroupusers","user_email",email,"group_name");
					assertEquals(user,group, "Group id do not match verification failed");
					Reporter.log(" Expected Result: Added user:"+group,true);
					Reporter.log(" Actual Result: added user found:"+user,true);
					
					Reporter.log(" Test execution is completed",true);
				}
				else
				{
					Reporter.log("user already  present in group: ",true);
					Reporter.log(" Test execution is completed",true);
				}
				
				
			}
			else
			{
				Reporter.log("user not not present: ",true);
				Reporter.log(" Test execution is completed",true);
			}
			
			
				
			
		}
		
		@Test (  priority = 2, dataProvider = "DelUserfmGrp" ,dataProviderClass = InfraDataProvider.class )
		public void DeleteUserFromGroup(String email,String id,String payload,String group) throws Exception {
			Reporter.log("exeuting testcase ****DeleteUserFromGroup() ***** Assembl id:C2094 ...",true);
			Reporter.log("Description: remove user from the group  :*** ",true);
			Reporter.log(" Steps : 1 search if user already exist ",true);
			Reporter.log(" Steps  2.  remove  user in group .3. search removed user ",true);
			Reporter.log(" Steps 3. search removed user ",true);
			
			// search user
			String userId =Infractions.searchUserid(suiteData, headers, email);
			// add end user if not present
			if(!userId.isEmpty())
			{

					//search group and
					List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
					queryParams.add(new BasicNameValuePair("group_name", group));
					String searchUri =suiteData.getReferer()+"/admin/group/users?group_name="+group;
					Reporter.log("search user uri ."+searchUri,true);
					//String payload=InfraConstants.ASSING_USER_PL;
					URI SearchdataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(),"/admin/group/users",queryParams);
					HttpResponse Searchresponse1 =  restClient.doGet(SearchdataUri, headers);//doPost(SearchdataUri, headers, null, new StringEntity(payload));
					Thread.sleep(3000);
					String SearchResponseBody1 = ClientUtil.getResponseBody(Searchresponse1);
					//String user= SearchName(SearchResponseBody,"tenantgroupusers","user_email",email);
					String resource_uri=null;
					resource_uri=Infrautils.SearchUserInGrp(SearchResponseBody1,"tenantgroupusers","user_email",email,"resource_uri");
					//String grpId=getUserid(SearchResponseBody1,"tenantgroupusers","user_email",email);
					if(!resource_uri.isEmpty()&&(null != resource_uri))
					{
						

						// user is  present in group
						// proceed for deletion
						//String Uri =suiteData.getReferer()+"admin/group/assign_users";
						Reporter.log("search user uri ."+searchUri,true);
						
						URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(),"/admin/group/assign_users");
						payload=payload.replace(InfraConstants.DUMMY, resource_uri);
						HttpResponse Searchresponse =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
						String SearchResponseBody = ClientUtil.getResponseBody(Searchresponse);
						System.out.println("responseBody: "+SearchResponseBody);
						String action_status =(String) new JSONObject(SearchResponseBody).get("action_status").toString();
						assertEquals(action_status,"success", "Response code verification failed");
						
						// cross check after deletion
						//search group and
						
						//searchUri =suiteData.getReferer()+"admin/group/users?group_name="+group;
						Reporter.log("search user uri ."+searchUri,true);
						//String payload=InfraConstants.ASSING_USER_PL;
						SearchdataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(),"/admin/group/users",queryParams);
						HttpResponse ReSearchResponse =  restClient.doGet(SearchdataUri, headers);//doPost(SearchdataUri, headers, null, new StringEntity(payload));
						Thread.sleep(3000);
						String ReSearchResponseBody = ClientUtil.getResponseBody(ReSearchResponse);
						//String user= SearchName(SearchResponseBody,"tenantgroupusers","user_email",email);
						 resource_uri=Infrautils.SearchUserInGrp(ReSearchResponseBody,"tenantgroupusers","user_email",email,"resource_uri");
						//String grpId=getUserid(SearchResponseBody1,"tenantgroupusers","user_email",email);
						 Reporter.log(" ****responseBody ***** ..."+resource_uri,true);
						
					
					}
					else
					{
						// user is not present in group
						Reporter.log("user not not present: ",true);
						Reporter.log(" Test execution is completed",true);
					}
				//assertEquals(user,group, "Group id do not match verification failed");
				Reporter.log(" Test execution is completed",true);
			}
			else
			{
				Reporter.log("user not not present: ",true);
				Reporter.log(" Test execution is completed",true);
			}
			
			
				
			
		}
		
		/*Author: Vijay Gangwar
		*todo: Delete users
		*params dataProvider
		*/
		
		
		@Test ( priority = 3 ,dataProvider = "deleteUserData",description = "Delete a user addby addUser testcase.",dataProviderClass = InfraDataProvider.class )
		public void DeleteUser(String userType,String id,String name,String email) throws Exception {
			Reporter.log("****DeleteUser() ***** Test Rail id:C249674...",true);
			Reporter.log("1. search user to delete. 2 delete user .",true);
			
			System.out.println("exeuting testcase ****deleteUser() ***** ...");
			//search  user to be deleted"
			String userId="";
			userId=Infractions.searchUserid(suiteData, headers, email);
			//userId=Infrautils.getUserid(responseBody,"userslist","email",email);
			if(!userId.isEmpty())
				{
				Reporter.log(" Delete user with id: "+userId);
				// delete user with retrieved userid	
				HttpResponse response1=Infractions.deleteUser(suiteData, headers, userId);
			//	HttpResponse response1 =  restClient.doDelete(dataUri1, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
				assertEquals( response1.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
				//Reporter.log("** deleteUser** Response: ",response.getStatusLine().getStatusCode(),true);
				Reporter.log("deleteUser Response: "+response1.getStatusLine().getStatusCode(),true);
				
				// cross verify for user deletion
				userId=Infractions.searchUserid(suiteData, headers, email);
				Reporter.log(" Delete user with id: "+userId);
				assertEquals(true ,userId.isEmpty(), "Response code verification failed");
				Reporter.log(" Expected result for deleted  user:  ",true);
				Reporter.log(" Actual result for deleted  user: "+userId,true);
				}
			else
			{
				Reporter.log(" user not found: "+name);
			}
	
		}
		
		
		/*Author: Vijay Gangwar
		*todo: Apply single filters
		*params dataProvider
		*/
		@Test ( priority = 2, dataProvider = "filter",dataProviderClass = InfraDataProvider.class )
		public void ApplyFilter(String filter,String filterName,String id,String value,String caseType) throws Exception {
			Reporter.log("****ApplyFilter() ***** Test Rail id.."+id,true);
			
			
			System.out.println("exeuting testcase ****ApplyFilter() ***** ...");
			//apply filter and execute the api 
			String uri=null;
			if(filter.contentEquals(InfraConstants.USER))
			{
				 //Set filter for Users
				uri =suiteData.getReferer()+InfraConstants.LIST_USERS+filterName+"="+value+InfraConstants.DEFAULT_SEARCH;
				
			}
			else if(filter.contentEquals(InfraConstants.GROUP))
			{
				//set Group filter
				uri =suiteData.getReferer()+InfraConstants.LIST_GROUP+filterName+"%7C"+value+InfraConstants.GROUP_SEARCH;
				
			}
			else if(filter.contentEquals(InfraConstants.PROFILES))
			{
				//set profile filter
				uri =suiteData.getReferer()+InfraConstants.LIST_PROFILE+filterName+"%7C"+value+InfraConstants.GROUP_SEARCH;
				
			}
			else
			{
				System.out.println("check request type uri error  ..."+uri);
				// remove filter
				uri=suiteData.getReferer()+InfraConstants.REMOVE_FILTER;
			}
			Reporter.log("search user uri ."+uri,true);
			///elasticaco/api/admin/v1/users/
			URI dataUri = ClientUtil.BuidURI(uri);
			HttpResponse response =  restClient.doGet(dataUri, headers);
			Thread.sleep(5000);
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			Reporter.log("filtered data: "+responseBody,true);
			
			// validate filtered data
			JSONArray summaryObject=null;
			{
				if(filter.contentEquals(InfraConstants.USER))
				{
					 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
					 String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
					 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
					 assertEquals(action_status,"success", "Response code verification failed");
					 Reporter.log(" user filter api action status."+action_status,true);
					 

					 
				}
				else if(filter.contentEquals(InfraConstants.GROUP))
				{
					 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("groupslist");
					 String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
					 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("groupslist");
					 assertEquals(action_status,"success", "Response code verification failed");
					 Reporter.log(" group filter api action status."+action_status,true);
					 
				}
				else if(filter.contentEquals(InfraConstants.PROFILES))
				{
					summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("profiles");
					String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
					summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("profiles");
					 assertEquals(action_status,"success", "Response code verification failed");
					 Reporter.log(" profile filter api action status."+action_status,true);
				}
				else
				{
					//remove all the filters
					summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				}
				//JSONArray summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
					
				if(!(filter.contains("remove filter")))
				{
					String Id=null;
					for(int i=0; i<summaryObject.length(); i++)
					{						
						JSONObject userObj=summaryObject.getJSONObject(i);
						{
							if(value.equalsIgnoreCase("admin"))
							{
								boolean Fvalue=userObj.get("access_profiles").toString().isEmpty();
								Reporter.log("expected value :"+Fvalue+" Retrived value"+false,true);
								assertEquals(false,Fvalue ,filter+" value Validation failed");
								
							}
							else
							{
								String Fvalue=userObj.get(filterName).toString();
								Reporter.log("expected value :"+Fvalue+" Retrived value"+value,true);
								assertEquals(value,Fvalue ,filter+" value Validation failed");
								
							}
							
						}					
					}
					//Reporter.log("userId...."+Id,true);						
				}
				else
				{
					Reporter.log("filter value is cleared ",true);
				}			
			}		
		}

		
		/*Author: Vijay Gangwar
		*todo: Apply multiple filters
		*params dataProvider
		*/

		/*Author: Vijay Gangwar
		*todo: import enduser
		*params none
		*/
		@Test ( priority = 4, dataProvider = "DeleteAll",dataProviderClass = InfraDataProvider.class )
		public void BulkDeleteAll(String action,String id,String email,String payload) throws Exception {
			Reporter.log("exeuting testcase ****BulkEditAction() ***** Test Rail id :C3473 ...",true);
			Reporter.log("description: 1. fileter user. 2 Select bulk Edit option . 3.Perform bulk edit options",true);
//			String SearchUri =suiteData.getReferer()+InfraConstants.SEARCH_USER+email;
//			Reporter.log("search user uri ."+SearchUri,true);
//			URI dataUriSearch = ClientUtil.BuidURI(SearchUri);
//			HttpResponse SearchResponse =  restClient.doGet(dataUriSearch, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			HttpResponse SearchResponse=Infractions.SearchUser(suiteData, headers, email);
			String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
			//print response body
			System.out.println("search user responseBody: ."+SearchResponseBody);
			// searach for the  id of user
			//String userId=Infrautils.getUserid(SearchResponseBody,"userslist","email",email);
			//List <Object> list;
			//payload=payload.replace("55dd6d73bf831205b4b8232f", userId);
			List<String> listData= new ArrayList<String>();
			String load=null;
			String payloadReturn=Infrautils.DeleteallPayload(SearchResponseBody,"userslist",listData,load);
			//Reporter.log("detail of all user...."+PayloadData,true);
			String uri=null;
			Reporter.log("detail of all user payload1...."+payloadReturn,true);
			if(action.contains("delete"))
			{
				//uri=suiteData.getReferer()+"/admin/user/delete_users/";
				uri="/admin/user/delete_users/";
			}
			else
			{
				 
				// uri =suiteData.getReferer()+"admin/user/ng/bulkupdate";
				 uri ="/admin/user/ng/bulkupdate";
			}
			//String uri =suiteData.getReferer()+"admin/user/ng/bulkupdate";//suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.API_CALL_USERS+"import/";
			//URI dataUri = ClientUtil.BuidURI(uri);
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), uri);
			HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payloadReturn));
			String responseBody = ClientUtil.getResponseBody(response);
			Reporter.log(" Bulk action responseBody : "+responseBody,true);
			String action_status =(String) new JSONObject(responseBody).get("api_response").toString();
			assertEquals(Integer.parseInt(action_status),InfraConstants.OK, "Response code verification failed");
			Reporter.log("Expected  result :0",true);
			Reporter.log("Actual result :"+action_status,true);
			Reporter.log("Testcase execution Completed ",true);
		}

		@Test ( priority = 2, dataProvider = "multiFilter",dataProviderClass = InfraDataProvider.class )
		public void ApplyMultiFilter(String filter,String is_active,String created_by,String roles,String access_profiles) throws Exception {
			
			Reporter.log("****ApplyMultiFilter() ***** Test Rail id..",true);
			Reporter.log("1. apply two search filter . ",true);
			Reporter.log(" apply filter for " +filter,true);
			
			//apply filter and execute the api 
			String uri=null;
			if(filter.contentEquals(InfraConstants.USER))
			{
				if((null==access_profiles)&(null==roles))
				{
					uri =suiteData.getReferer()+InfraConstants.LIST_USERS+InfraConstants.QUERY_PARAM_CREATED_BY+"="+created_by+"&"+InfraConstants.QUERY_PARAM_ACTIVE+"="+is_active+"&"+InfraConstants.DEFAULT_SEARCH;
						
				}
				else if((null==access_profiles)&(null==created_by))
				{
					
					//	prepare user uri
						uri =suiteData.getReferer()+InfraConstants.LIST_USERS+InfraConstants.QUERY_PARAM_CREATED_BY+"="+created_by+"&"+InfraConstants.QUERY_PARAM_ACTIVE+"="+is_active+"&"+InfraConstants.DEFAULT_SEARCH;
							
					
				}
				else if((null==access_profiles)&(null==is_active))
				{
					
						//prepare user uri
						uri =suiteData.getReferer()+InfraConstants.LIST_USERS+InfraConstants.QUERY_PARAM_CREATED_BY+"="+created_by+"&"+InfraConstants.QUERY_PARAM_ROLE+"&"+InfraConstants.DEFAULT_SEARCH;
							
					
				}
				else if((null==roles)&(null==is_active))
				{
					
						//prepare user uri
						uri =suiteData.getReferer()+InfraConstants.LIST_USERS+InfraConstants.QUERY_PARAM_PROFILE+"="+access_profiles+"&"+InfraConstants.QUERY_PARAM_CREATED_BY+"="+created_by+"&"+InfraConstants.DEFAULT_SEARCH;
							
					
				}
				else if((null==roles)&(null==created_by))
				{
					
						//uri =suiteData.getReferer()+InfraConstants.LIST_USERS+"created_by=admin@infrabeatle.com&is_active=true&limit=30&offset=0&order_by=first_name&roles=admin";//suiteData.getReferer()+"admin/user/ng/list/0?is_active="+value+"&limit=30&offset=0&order_by=first_name&query=first_name";
					uri =suiteData.getReferer()+InfraConstants.LIST_USERS+InfraConstants.QUERY_PARAM_PROFILE+"="+access_profiles+"&"+InfraConstants.QUERY_PARAM_ACTIVE+"="+is_active+"&"+InfraConstants.DEFAULT_SEARCH;
						
					
				}
				else
				{
					uri=suiteData.getReferer()+InfraConstants.LIST_USERS+"access_profiles=Admin-all&created_by=admin@infrabeatle.com&is_active=true&limit=30&offset=0&order_by=first_name&roles=admin";
							
				}
				
				
			}
			else if(filter.contentEquals(InfraConstants.GROUP))
			{
				//prepare group uri
				uri=suiteData.getReferer()+InfraConstants.LIST_GROUP+InfraConstants.QUERY_PARAM_ACTIVE+"%7C"+is_active+"%7C%7C"+InfraConstants.QUERY_PARAM_CREATED_BY+"%7C"+created_by+"&"+InfraConstants.GROUP_SEARCH;
				
			}
			else if(filter.contentEquals(InfraConstants.PROFILES))
			{
				//prepare group uri
				uri=suiteData.getReferer()+InfraConstants.LIST_PROFILE+InfraConstants.QUERY_PARAM_ACTIVE+"%7C"+is_active+"%7C%7C"+InfraConstants.QUERY_PARAM_CREATED_BY+"%7C"+created_by+"&"+InfraConstants.GROUP_SEARCH;
				
			}
			else if(filter.contentEquals("UserFilter"))
			{
				
				// uri for user filter
				uri=suiteData.getReferer()+InfraConstants.USER_FILTER+InfraConstants.QUERY_PARAM_CREATED_BY+"="+created_by+"&"+InfraConstants.QUERY_PARAM_ACTIVE+"="+is_active+"&"+InfraConstants.GROUP_SEARCH;
			}
			
			else if(filter.contentEquals("GropFilter"))
			{
				System.out.println("check request type uri error  ..."+uri);
				// uri for  group filter
				uri=suiteData.getReferer()+InfraConstants.USER_FILTER+InfraConstants.QUERY_PARAM_CREATED_BY+"="+created_by+"&"+InfraConstants.QUERY_PARAM_ACTIVE+"="+is_active;
			}
			else if(filter.contentEquals("ProfileFilter"))
			{
				// uri profile filter
				uri=suiteData.getReferer()+InfraConstants.USER_FILTER+InfraConstants.QUERY_PARAM_CREATED_BY+"="+created_by+"&"+InfraConstants.QUERY_PARAM_ACTIVE+"="+is_active;
			}
			else if(filter.contentEquals("removeUserFilter"))
			{
				// remove profile filter
				uri=suiteData.getReferer()+InfraConstants.REMOVE_FILTER ;
			}
			else if(filter.contentEquals("removeGroupFilter"))
			{
				System.out.println("check request type uri error  ..."+uri);
				// remove group filter
				uri=suiteData.getReferer()+InfraConstants.REMOVE_GROUP_FILTER ;
			}
			else if(filter.contentEquals("removeProfileFilter"))
			{
				// remove profile filter
				uri=suiteData.getReferer()+InfraConstants.REMOVE_PROFILE_FILTER ;
			}
			//Reporter.log("search user uri ."+uri,true);
			Thread.sleep(5000);
			URI dataUri = ClientUtil.BuidURI(uri);
			HttpResponse response =  restClient.doGet(dataUri, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
			Reporter.log(" ****filtered data ***** ..."+responseBody,true);
			//Reporter.log("search user responseBody: ."+responseBody,true);
			String Name=null;
			// validate filtered data
			JSONArray summaryObject=null;
			// validate filtered data
			if(filter.contains("user"))
			{
				 Name = Infrautils.SearchName(responseBody,"userslist","created_by","admin@infrabeatle.com");
				 summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("userslist");
				// String Id=null;
				
				 
			}
			else if(filter.contains("group"))
			{
				Name =Infrautils.SearchName(responseBody,"groupslist","created_by","admin@infrabeatle.com");
				summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("groupslist");

			}
			else if(filter.contains("profiles"))
			{
				Name = Infrautils.SearchName(responseBody,"profiles","created_by","admin@infrabeatle.com");
				summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("profiles");

			}
			else
			{
				
			}
			// final data validation
			String Fvalue=null;
			String fetch_active=null;
			String fetch_created=null;
			String fetch_roles=null;
			for(int i=0; i<summaryObject.length(); i++)
			{						
				JSONObject userObj=summaryObject.getJSONObject(i);
				{
					if(!is_active.contains("null"))
					{
						Fvalue=userObj.get("is_active").toString();
						fetch_active = Fvalue;
						assertEquals(Fvalue,is_active, filter+" value Validation failed");

					}
					else
					{

					}
					if(!created_by.contains("null"))
					{
						Fvalue=userObj.get("created_by").toString();
						fetch_created = Fvalue;
						assertEquals(Fvalue,created_by, filter+" value Validation failed");

					}
					else
					{

					}
					if(!roles.contains("null"))
					{
						Fvalue=userObj.get("roles").toString();
						fetch_roles=Fvalue;
						assertEquals(Fvalue,roles, filter+" value Validation failed");

					}
					else 
					{

					}
					if(!access_profiles.contains("null"))
					{
						Fvalue=userObj.get("access_profile").toString();
						assertEquals(Fvalue,access_profiles, filter+" value Validation failed");

					}
					else
					{

					}
							
				}					
			}
			
			if(!is_active.contains("null"))
			{
				Reporter.log(" Expected Result  Activation status : "+is_active,true);
				Reporter.log(" Actual Result Activation  status:"+fetch_active,true);
				Reporter.log(" Test execution is completed",true);

			}
			if(!created_by.contains("null"))
			{
				Reporter.log(" Expected Result  Activation status : "+created_by,true);
				Reporter.log(" Actual Result Activation  status:"+fetch_created,true);
				Reporter.log(" Test execution is completed",true);

			}
		}
		
		
		/*
		 * Author:Vijay Gangwar
		 * To do: get import notification for admin
		 */
		
		@Test( priority = 2 )	public 	void getImportNotification ()throws Exception
		{

			Reporter.log(" **getImportNotification**",true);
			Reporter.log("1.Import the user through csv.",true);
			Reporter.log("2.validate the notification recieved ",true);
			String filename="Admin_import.csv";
			String filepath = InfraConstants.INFRA_DATA_LOC + filename;
			//check the filename provided is absolute or not
			File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
			if(!uploadFile.exists()) {
				System.out.println("Sorry file not exists in the folder"); 
			}
			//create a multipart entity for file uplaod 
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();		
			builder.addTextBody("name", "file", ContentType.TEXT_PLAIN);
			//add the file parameter to the and build multipat entity
			builder.addBinaryBody("file", uploadFile, ContentType.APPLICATION_OCTET_STREAM, filename);		
			HttpEntity multipart = builder.build();

			headers = getHeaders();
			//headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=----------------------"+Long.toString(new Date().getTime())));
			String uri =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+"/"+tenantDB+InfraConstants.API_CALL_USERS+"import/";
			URI dataUri = ClientUtil.BuidURI(uri);

			HttpResponse response =  restClient.doPost(dataUri, headers, null, multipart);
			String userId=null;
			String responseBody = ClientUtil.getResponseBody(response);
			assertEquals(HttpStatus.SC_OK,response.getStatusLine().getStatusCode(), "Response code verification failed");
			Reporter.log("wait for the notification request to reflect.",true);
			Thread.sleep(60000);
			// user api for notification list https://eoe.elastica-inc.com/admin/notification/list -GET
			//String url =suiteData.getReferer()+InfraConstants.API_NOTIFICATION_LIST;//"https://eoe.elastica-inc.com/admin/notification/list";//
			Reporter.log("1.Check for the notification after csv import Request",true);
			Reporter.log("Request API :",true);
			URI dataUri1 = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(),InfraConstants.API_NOTIFICATION_LIST);
			Reporter.log("Request Method type: GET",true);
			Reporter.log("Request API :"+dataUri,true);
			HttpResponse NotificationResponse=restClient.doGet(dataUri1, headers);//doGetRest(url,headers);
			//System.out.println("** NotificationResponse** Response: "+response.getStatusLine().getStatusCode());
			Reporter.log("** NotificationResponse** Response: "+NotificationResponse.getStatusLine().getStatusCode(),true);
			assertEquals(HttpStatus.SC_OK, NotificationResponse.getStatusLine().getStatusCode(), "Response code verification failed");
			String NotificationResponseBody = ClientUtil.getResponseBody(NotificationResponse);
			System.out.println("** getNotification** responseBody: "+NotificationResponseBody);
			String admin=null;			
			{

				JSONArray summaryObject = (JSONArray) new JSONObject(NotificationResponseBody).getJSONArray("objects");

				String Id=null;
				String Subject=null;
				for(int i=0; i<summaryObject.length(); i++)
				{

					JSONObject userObj=summaryObject.getJSONObject(i);
					// validate the notification data
					if(userObj.get("subject").toString().equals("Bulk User Import Complete"))
					{
						admin=(String)userObj.get("email");
						String summary=(String)userObj.get("details");
						Reporter.log("userId...."+admin,true);
						Reporter.log("userId...."+summary,true);
						break;

					}
				}
			}
			Reporter.log(" Expected Result  notification for : "+suiteData.getUsername(),true);
			Reporter.log(" Actual Result notification for :"+admin,true);
			assertEquals(suiteData.getUsername(),admin, "Response code verification failed");

			//return notification;
		}
		 
	//	@Test( priority = 2 )
		public 	void AdminNotification ()throws Exception
		{
			//https://eoe.elastica-inc.com/admin/notification/list -GET
			Reporter.log("Get admin notification for the imports actions",true);
			String uri =suiteData.getScheme()+"://"+ suiteData.getApiserverHostName()+InfraConstants.API_ADMIN_NOTIFICATIONS;
			//String uri = https://eoe.elastica-inc.com/admin/notification/list;
			URI dataUri = ClientUtil.BuidURI(uri);
			Reporter.log("Request Method type: GET",true);
			Reporter.log("Request API :"+dataUri,true);
			// send get Request 
			HttpResponse response=restClient.doGet(dataUri, headers);//doGetRest(url,headers);
			Reporter.log("** AdminNotification** Response: "+response.getStatusLine().getStatusCode(),true);
			assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode(), "Response code verification failed");
			String responseBody = ClientUtil.getResponseBody(response);
			System.out.println("** AgetNotification** responseBody: "+responseBody);
			
			
			{

				 JSONArray summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("objects");
					
					String Id=null;
					String Subject=null;
					for(int i=0; i<summaryObject.length(); i++)
					{
						
						JSONObject userObj=summaryObject.getJSONObject(i);
						
						if(userObj.get("seen").toString().equals("true"))
						{
							Id=(String)userObj.get("id");
							Subject=(String)userObj.get("subject");
							Reporter.log("userId...."+Id,true);
							Reporter.log("userId...."+Subject,true);
							
						}
						
					}
					//Reporter.log("userId...."+Id,true);
					
					
			 
			}
			
			//return notification;
		}
		//Depricated 
	//	@Test( priority = 1 )
		public 	void AdminNotificatinLogs ()throws Exception
		{
			//https://eoe.elastica-inc.com/admin/notification/list -GET
			
			String uri = "https://api-eoe.elastica-inc.com/eslogs/displaylogs/";//suiteData.getScheme()+"://"+ suiteData.getApiserverHostName()+"elasticaco/api/admin/v1/notifications/";
			String payload ="{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\"2015-08-03T18:30:00.000Z\",\"to\":\"2015-08-07T18:29:59.999Z\"}}},{\"term\":{\"facility\":\"Elastica\"}},{\"term\":{\"user\":\"admin@infrabeatle.com\"}}]}},\"filter\":{}}},\"from\":0,\"size\":0,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"facets\":{}},\"sourceName\":\"history\",\"apiServerUrl\":\"https://api-eoe.elastica-inc.com/\",\"csrftoken\":\"noIwodAKDiCbgQhGg46rAAF6FL4a16vg\",\"sessionid\":\"z38iefvllk3rpqs2gti3dznn5cz8zqog\",\"userid\":\"admin@infrabeatle.com\"}";
			
			//String QueryParams ="limit=60&offset=0&order_by=first_name";
			//String url ="https://eoe.elastica-inc.com/admin/notification/list";//uri+QueryParams;//"https://eoeapi.elastica-inc.com/bop/editSubscription/?name=Investigate&category=Elastica&action=revoke&user=vijay.gangwar%40infrabeatle.com&priDomain=newblr.com";	
			//	URI url=new URI(uri);	
				//String Response = sentGet(uri,headers);
			URI dataUri = ClientUtil.BuidURI(uri);
		
			Reporter.log("Request Method type: POST",true);
			Reporter.log("Request API :"+dataUri,true);
			HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			//HttpResponse response=restClient.doPost(uri, headers, matrinxParams, entity).doGet(dataUri, headers);//doGetRest(url,headers);
			System.out.println("** sortUserAscend** Response: "+response.getStatusLine().getStatusCode());
			Reporter.log("** revokeDetect** Response: "+response.getStatusLine().getStatusCode(),true);
			assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode(), "Response code verification failed");
			//String responseBody = ClientUtil.ClientUtil.getResponseBody(response);
			System.out.println("** getNotification** responseBody: "+responseBody);
			
			
			{

				 JSONArray summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray("objects");
					
					String Id=null;
					String Subject=null;
					for(int i=0; i<summaryObject.length(); i++)
					{
						
						JSONObject userObj=summaryObject.getJSONObject(i);
						
						if(userObj.get("seen").toString().equals("true"))
						{
							Id=(String)userObj.get("id");
							Subject=(String)userObj.get("subject");
							Reporter.log("userId...."+Id,true);
							Reporter.log("userId...."+Subject,true);
							
						}
						
					}
					//Reporter.log("userId...."+Id,true);
					
					
			 
			}
			
			//return notification;
		}
		
		
					
		
	/********************************************************************************************************/	
		

		
		
		
		 public static String getJSONValue(String json, String key) throws JsonProcessingException, IOException {
				JsonFactory factory = new JsonFactory();
				ObjectMapper mapper = new ObjectMapper(factory);
				JsonNode rootNode = mapper.readTree(json);
				return rootNode.get(key).toString();

			}

		 
	
		
		
	public String searchUser(String user) throws Exception{
		String uri =suiteData.getReferer()+InfraConstants.SEARCH_USER+user;
		Reporter.log("search user uri ."+uri,true);
		URI dataUri1 = ClientUtil.BuidURI(uri);
		HttpResponse response1 =  restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
		Thread.sleep(5000);
		String responseBody1 = ClientUtil.getResponseBody(response1);
		//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
		System.out.println("e ****responseBody ***** ..."+responseBody1);
		Reporter.log("search user responseBody: ."+responseBody1,true);
		//String userId=getUserid(responseBody1,"userslist","first_name",user);
		String name = Infrautils.SearchName(responseBody1,"userslist","email",user);
		return name;
	}
	
		public HttpEntity getEntity(String filePath)
		{
			HttpEntity putBodyEntity = new FileEntity(new File(FileHandlingUtils.getFileAbsolutePath(filePath))); 
			
			return putBodyEntity;
		}
		
		

		
}

