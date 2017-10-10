package com.elastica.beatle.infra;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.infra.dto.Groups;
import com.elastica.beatle.infra.dto.Groupslist;
import com.elastica.beatle.infra.dto.Profile;
import com.elastica.beatle.infra.dto.ProfileList;
import com.elastica.beatle.infra.dto.Users;
import com.elastica.beatle.infra.dto.Userslist;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.tests.infra.InfraConstants;

public class InfraActions {
	protected Client restClient = new Client();;
	//public List<NameValuePair> headers;
	
	public HttpResponse SearchUser(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String email) throws Exception
	 {
		//String uri =suiteDatadto.getReferer()+InfraConstants.SEARCH_USER+Name;
		Reporter.log("search user with first name"+email,true);
		List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("limit", "90"));
		queryParams.add(new BasicNameValuePair("offset", "0"));
		queryParams.add(new BasicNameValuePair("order_by", "first_name"));
		queryParams.add(new BasicNameValuePair("query", email));
		URI dataUri1 = ClientUtil.BuidURI(suiteDatadto.getScheme(),suiteDatadto.getHost(), "/admin/user/ng/list/0",queryParams);
		//restClient = new Client();
		HttpResponse response =  restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
		return response;
	 }
	public String getUserid(String responseBody,String email ) throws JAXBException
		{
			Users allUsers =  MarshallingUtils.unmarshall( responseBody, Users.class);	
			String userId = "";
			for(Userslist user : allUsers.getUserslist()) {			
				
				if (user.getEmail().equals(email)) {
				userId = user.getId();
				break;
			}
		}
			
			return userId;
	}
	

	public String getProfileResourceId(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String name) throws Exception
	 {
		// search profile
		HttpResponse response=SearchProfile(suiteDatadto,headers);
		String responseBody = ClientUtil.getResponseBody(response);
		
		// find profile id
		ProfileList allProfile =  MarshallingUtils.unmarshall( responseBody, ProfileList.class);	
		String resourceUri = "";
		for(Profile profile : allProfile.getProfiles()) {			
		//	System.out.println("User:"+MarshallingUtils.marshall(profile));
			if (profile.getName().equals(name)) {
				resourceUri = profile.getResourceUri();
				break;
		}
	}
		return resourceUri;
	 }
	
	public String getProfileid(String responseBody,String name ) throws JAXBException
	{
		// find profile id
				ProfileList allProfile =  MarshallingUtils.unmarshall( responseBody, ProfileList.class);	
				String Pid = "";
				for(Profile profile : allProfile.getProfiles()) {			
					//System.out.println("User:"+MarshallingUtils.marshall(profile));
					if (profile.getName().equals(name)) {
						Pid = profile.getId();
						break;
				}
			}
				return Pid;
}
	
	public HttpResponse createUser(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String payload) throws Exception
	 {
		String uri =suiteDatadto.getReferer()+InfraConstants.API_ADD_USER;
		Reporter.log(" Creating new user  ",true);
		//Reporter.log("Request API :"+uri,true);
		//URI dataUri = ClientUtil.BuidURI(uri);
		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(),suiteDatadto.getHost(), InfraConstants.API_ADD_USER);
		Reporter.log("create API :"+dataUri,true);
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload,"UTF-8"));
		return response;
	 }
	
	public String createAdmin(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String ProfilePayload,String payload,String profileName,String email) throws Exception
	 {
		
		//1. search the Profile to be created
		// deleat old profile and create new profile
		//2. Search the Admin to be created
		//3. delete old admin and recreate new admin 
		//4 verify if Admin by showing profile id created.
		HttpResponse response =  SearchProfile(suiteDatadto, headers);//restClient.doGet(dataUriSearc, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
		Thread.sleep(3000);
		String responseBody = ClientUtil.getResponseBody(response);
		//Assert the Response data
		String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
	//	assertEquals(action_status,"success", "Api status verification failed");
    	Reporter.log("Profile search status : "+action_status,true);
		String profileId=null;
		String SearcheduserId=null;
		profileId=getProfileid(responseBody, profileName);
		// if profile id is present then create Admin			
		if(profileId.isEmpty())
		{
			//profile not present
			Reporter.log(" Access profile not present",true);
			// create the profile
			createProfile(suiteDatadto, headers,ProfilePayload);
			
		}
		else
		{
			// delete the existing profile.
			deleteProfile( suiteDatadto,headers, profileId);
			//create new profile
			Thread.sleep(3000);
			createProfile(suiteDatadto, headers,ProfilePayload);
			//search id for newly created proifle
			HttpResponse SearchResponse =  SearchProfile(suiteDatadto, headers);//restClient.doGet(dataUriSearc, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
			Thread.sleep(3000);
			String SearchResponseBody = ClientUtil.getResponseBody(SearchResponse);
			//Assert the Response data
			String status =(String) new JSONObject(responseBody).get("action_status").toString();
	    	//	assertEquals(action_status,"success", "Api status verification failed");
	    	Reporter.log("Profile search status : "+status,true);
			
			profileId=getProfileid(SearchResponseBody, profileName);//SearchProfile(suiteDatadto, headers);
			
			
		
		}// search if admin is present with the given email
			String userId=null;				
			userId=searchUserid(suiteDatadto, headers, email);
			if(userId.isEmpty())
			{
				// do nothing
			}
			else
			{
				Reporter.log(" admin aready present with id: "+userId,true);
				//delete user 
				deleteUser(suiteDatadto, headers, userId);
			}
			
				// create Admin if userId is empty
				HttpResponse response1=createAdmin(suiteDatadto, headers, payload, profileId);
				String responseBody1= ClientUtil.getResponseBody(response1);
				//Reporter.log("response1 :"+response1,true);
				System.out.println("Response: "+responseBody1);
				String action_status1 =(String) new JSONObject(responseBody1).get("action_status").toString();
				//assertEquals(action_status,"success", "Api status verification failed");
				//Reporter.log(" Expected Result:success",true);
				Reporter.log(" Actual Result first name: "+action_status1,true);
				
				SearcheduserId=searchUserid(suiteDatadto, headers, email);//Infrautils.getUserid(reSearchResponseBody,"userslist","email",email);
				
				Reporter.log(" New Admin created with id :"+SearcheduserId,true);
				
				
			
//			else
//			{
//				Reporter.log(" admin aready present with id: "+userId,true);
//				//delete user 
//				deleteUser(suiteDatadto, headers, userId);
//			}
			return SearcheduserId;
		
	 }
	public HttpResponse createAdmin(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String payload,String profileId) throws Exception
	 {
		payload=payload.replace("55d8671e9dfa5156bed7601b", profileId);
		HttpResponse response=createUser(suiteDatadto, headers,payload);
		return response;
	 }
	
	public HttpResponse deleteUser(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String userId) throws Exception
	 {
		Reporter.log("Delete the user with userid :"+userId,true);
		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(), suiteDatadto.getApiserverHostName(),"/"+suiteDatadto.getTenantDomainName()+InfraConstants.API_CALL_USERS+userId+"/");
		Reporter.log("create API :"+dataUri,true);
		HttpResponse response =  restClient.doDelete(dataUri, headers);
		return response;
	 }
	public HttpResponse SearchGroup(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String name) throws Exception
	 {
		
		Reporter.log("search Group by name"+name,true);
		List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("limit", "90"));
		queryParams.add(new BasicNameValuePair("offset", "0"));
		queryParams.add(new BasicNameValuePair("order_by", "name"));
		queryParams.add(new BasicNameValuePair("query", name));
		URI dataUri1 = ClientUtil.BuidURI(suiteDatadto.getScheme(),suiteDatadto.getHost(), "/admin/group/ng/list/0",queryParams);
		//restClient = new Client();
		HttpResponse response =  restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
		return response;
	 }
	
	public String searchUserid(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String email) throws Exception
	{
	HttpResponse response=SearchUser(suiteDatadto, headers,  email);
	String responseBody = ClientUtil.getResponseBody(response);
	String userId = null;
	System.out.println("User  :"+responseBody);
	userId=getUserid(responseBody, email);
	return userId;
	}
	public String getUseremail(String responseBody,String email ) throws JAXBException
	{
		Users allUsers =  MarshallingUtils.unmarshall( responseBody, Users.class);	
		String userEmail = "";
		for(Userslist user : allUsers.getUserslist()) {			
			System.out.println("User:"+MarshallingUtils.marshall(user));
			if (user.getEmail().equals(email)) {
				userEmail = user.getEmail();
			}
		}
		return userEmail;
	}
	
	public HttpResponse createGroup(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String payload) throws Exception
	 {
		//create group
		Logger.info(" Creating Group: ");
		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(),suiteDatadto.getHost(), InfraConstants.API_ADD_GROUP);
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload, "UTF-8"));
		return response;
	 }
	
	public String getGroupid(String responseBody,String name ) throws JAXBException
	{
		Groups allGroups =  MarshallingUtils.unmarshall( responseBody, Groups.class);	
		String groupId = "";
		for(Groupslist group : allGroups.getGroupslist()) {			
			System.out.println("User:"+MarshallingUtils.marshall(group));
			if (group.getName().equals(name)) {
				groupId = group.getId();
				break;
			}
		}
		return groupId;
	}
	public String getGroupName(String responseBody,String name ) throws JAXBException
	{
		Groups allGroups =  MarshallingUtils.unmarshall( responseBody, Groups.class);	
		String groupName = "";
		for(Groupslist group : allGroups.getGroupslist()) {			
			System.out.println("User:"+MarshallingUtils.marshall(group));
			if (group.getName().equals(name)) {
				groupName = group.getName();
				break;
			}
		}
		return groupName;
	}
	
	public String getGroupCreatedAndModifiedTime(String responseBody, String name, String time) throws JAXBException{
		Groups allGroups =  MarshallingUtils.unmarshall( responseBody, Groups.class);	
		String timeStamp = null;
		for(Groupslist group : allGroups.getGroupslist()) {			
			System.out.println("User:"+MarshallingUtils.marshall(group));
			if (group.getName().equals(name)) {
				if(time.equalsIgnoreCase("modified")){
					timeStamp = group.getModifiedOn().toString();
				}
				if(time.equalsIgnoreCase("created")){
					timeStamp = group.getCreatedOn();
				}
				break;
			}
		}
		return timeStamp;
	}
	public HttpResponse deleteGroup(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String userId) throws Exception
	 {
		Reporter.log("Delete  Group with id :"+userId,true);
		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(), suiteDatadto.getApiserverHostName(),"/"+suiteDatadto.getTenantDomainName()+InfraConstants.GROUP_API+userId+"/");
		HttpResponse response =  restClient.doDelete(dataUri, headers);
		return response;
	 }
	
	public HttpResponse SearchProfile(TestSuiteDTO suiteDatadto,List<NameValuePair> headers) throws Exception
	 {
		// search profile
		Reporter.log("search profile ",true);
		List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
		//queryParams.add(new BasicNameValuePair("limit", "200"));
	//	queryParams.add(new BasicNameValuePair("offset", "0"));
		queryParams.add(new BasicNameValuePair("order_by", "name"));
		//queryParams.add(new BasicNameValuePair("query", name));
		URI dataUri1 = ClientUtil.BuidURI(suiteDatadto.getScheme(),suiteDatadto.getHost(), "/admin/profiles/list/",queryParams);
		//restClient = new Client();
		HttpResponse response =  restClient.doGet(dataUri1, headers);
		
		
		return response;
	 }

	public HttpResponse createProfile(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String payload) throws Exception
	 {
		//profile not exist 
		//create uri for add profile request
		
		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(),suiteDatadto.getHost(), "/admin/profiles/add");
		//Reporter.log("Request Method type: POST",true);
		Reporter.log("Request API :"+payload,true);
		//execute  request
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload,"UTF-8"));
		return response;
	 }
	public HttpResponse deleteProfile(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String Pid) throws Exception
	 {
		Reporter.log("Delete  Group with id :"+Pid,true);
		List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("id", Pid));
		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(), suiteDatadto.getHost(), "/admin/profiles/delete", queryParams);
		//execute  request
		HttpResponse response =  restClient.doGet(dataUri, headers);//doPost(dataUri, headers, null, new StringEntity(payload));
		Thread.sleep(3000);
		return response;
	 }
	
	public HttpResponse importUser(TestSuiteDTO suiteDatadto,List<NameValuePair> headers,String filename,String tenantDB) throws Exception
	 {
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

	//	headers = getHeaders();
		//headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=----------------------"+Long.toString(new Date().getTime())));
		String uri =suiteDatadto.getScheme()+"://"+suiteDatadto.getApiserverHostName()+"/"+tenantDB+InfraConstants.API_CALL_USERS+"import/";
		URI dataUri = ClientUtil.BuidURI(uri);
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, multipart);

		return response;
	 }
	
	public HttpResponse editGroup(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, Map<String, String> groupData) throws Exception{
		String payload = "{\"group\":{\"name\":\""+groupData.get("name")+"\",\"is_active\":"+groupData.get("is_active")+",\"notes\":\""+groupData.get("notes")+"\",\"description\":\""+groupData.get("description")+"\"},\"id\":\""+groupData.get("id")+"\"}";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), "/admin/group/ng/edit");
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		return response;
	}
	
	public HttpResponse editUser(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, Map<String, String> userData) throws Exception{
		String payload = "{\"user\":{\"first_name\":\""+userData.get("first_name")+"\",\"last_name\":\""+userData.get("last_name")+"\",\"title\":\""+userData.get("title")+"\",\"secondary_user_id\":\"\",\"work_phone\":\"1-123123123\",\"cell_phone\":\"1-123123123\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":"+userData.get("is_active")+",\"notes\":\"Notes\",\"is_dpo\":false},\"id\":\""+userData.get("id")+"\"}";
		System.out.println(payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), "/admin/user/ng/edit");
		return restClient.doPost(dataUri, headers, null, new StringEntity(payload));
	}
	
	public HashMap<String, String> getGroupData(String responseBody, String groupName) throws JSONException, JsonParseException, JsonMappingException, IOException{
		HashMap<String, String> groupData = new HashMap<String, String>();
		JSONObject groupResponseObject  = new JSONObject(responseBody);
		JSONArray resultArray = groupResponseObject.getJSONArray("groupslist");
		for(int i = 0 ; i<resultArray.length();i++ ){
			String groupJson = resultArray.getJSONObject(i).toString();
			groupData = new ObjectMapper().readValue(groupJson, HashMap.class);
		}
		return groupData;
	}
	
	public HashMap<String, String> getUserData(String responseBody, String email) throws JSONException, JsonParseException, JsonMappingException, IOException{
		HashMap<String, String> userData = new HashMap<String, String>();
		JSONObject userResponseObject  = new JSONObject(responseBody);
		JSONArray resultArray = userResponseObject.getJSONArray("userslist");
		for(int i = 0 ; i<resultArray.length();i++ ){
			String userJson = resultArray.getJSONObject(i).toString();
			userData = new ObjectMapper().readValue(userJson, HashMap.class);
		}
		return userData;
	}
	
	public void deleteIfUserExist(TestSuiteDTO suiteData, List<NameValuePair> requestHeaders, String email){
		try{
			HttpResponse searchUserResponse = SearchUser(suiteData, requestHeaders, email);
			String searchUserResponseBody = ClientUtil.getResponseBody(searchUserResponse);
			String userId = getUserid(searchUserResponseBody, email);
			deleteUser(suiteData, requestHeaders, userId);
		}catch(Exception e){}
	}
	
	public void deleteIfGroupExists(TestSuiteDTO suiteData, List<NameValuePair> requestHeaders, String name){
		try{
			HttpResponse searchGroupResponse = SearchGroup(suiteData, requestHeaders, name);
			String searchGroupResponseBody = ClientUtil.getResponseBody(searchGroupResponse);
			String groupId = getGroupid(searchGroupResponseBody, name);
			deleteGroup(suiteData, requestHeaders, groupId);
		}catch(Exception e){}
	}
	
	public void addUserToGroup(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String group, String user) throws Exception{
		String assignUserUri = "/admin/group/assign_users";
		String payload = "{\"name\":\""+group+"\",\"deleted_users\":[],\"added_users\":[\""+user+"\"]}";
		StringEntity entity = new StringEntity(payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), assignUserUri);
		HttpResponse response = restClient.doPost(dataUri, headers, null, entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String responseBody = ClientUtil.getResponseBody(response);
		String actionStatus = ClientUtil.getJSONValue(responseBody, "action_status");
		Assert.assertEquals(actionStatus.substring(1, actionStatus.length()-1), "success");
	}
	
	public void deleteUserFromGroup(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, HashMap<String, String> groupUserDetails) throws Exception{
		String assignUserUri = "/admin/group/assign_users";
		String payload = "{\"name\":\""+groupUserDetails.get("group_name")+"\",\"deleted_users\":[\""+groupUserDetails.get("resource_uri")+"\"],\"added_users\":[]}";
		StringEntity entity = new StringEntity(payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), assignUserUri);
		HttpResponse response = restClient.doPost(dataUri, headers, null, entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String responseBody = ClientUtil.getResponseBody(response);
		String actionStatus = ClientUtil.getJSONValue(responseBody, "action_status");
		Assert.assertEquals(actionStatus.substring(1, actionStatus.length()-1), "success");
	}
	
	public HashMap<String, String> getGroupUserDetails(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String group, String user) throws Exception{
		List<NameValuePair> queryParams= new ArrayList<NameValuePair>();
		HashMap<String, String> respPara = new HashMap<String, String>();
		queryParams.add(new BasicNameValuePair("group_name", group));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/admin/group/users", queryParams);
		HttpResponse response = restClient.doGet(dataUri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		String objects = ClientUtil.getJSONValue(responseBody, "tenantgroupusers");
		JSONArray objectArray = new JSONArray(objects);
		if(objectArray.length()>0){
			for(int i=0;i<objectArray.length();i++){
				JSONObject object = objectArray.getJSONObject(i);
				String userEmail = object.getString("user_email");
				if(userEmail.equals(user)){
					respPara = new ObjectMapper().readValue(objectArray.getString(i), HashMap.class);
				}
			}
		}
		return respPara;
	}
	
	public void activateUser(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String userId) throws Exception{
		String userUri = "/controls/make_api_request";
		String payload = "{\"url\":\"users\",\"id\":\""+userId+"\",\"action\":\"patch\",\"params\":null,\"data\":{\"is_active\":true}}";
		StringEntity entity = new StringEntity(payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), userUri);
		HttpResponse response = restClient.doPost(dataUri, headers, null, entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
	}
	
	public void deactivateUser(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String userId) throws Exception{
		String userUri = "/controls/make_api_request";
		String payload = "{\"url\":\"users\",\"id\":\""+userId+"\",\"action\":\"patch\",\"params\":null,\"data\":{\"is_active\":false}}";
		StringEntity entity = new StringEntity(payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(), userUri);
		HttpResponse response = restClient.doPost(dataUri, headers, null, entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
	}
}