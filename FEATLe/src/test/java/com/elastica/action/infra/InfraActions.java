package com.elastica.action.infra;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import com.elastica.common.SuiteData;
import com.elastica.infra.dto.Groups;
import com.elastica.infra.dto.Groupslist;
import com.elastica.infra.dto.Profile;
import com.elastica.infra.dto.ProfileList;
import com.elastica.infra.dto.Users;
import com.elastica.infra.dto.Userslist;
import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;

public class InfraActions {
	protected Client restClient = new Client();;
	
	
	public HttpResponse SearchUser(SuiteData suiteDatadto,List<NameValuePair> headers,String email) throws Exception
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
				System.out.println("User:"+MarshallingUtils.marshall(user));
				if (user.getEmail().equals(email)) {
				userId = user.getId();
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
			//System.out.println("User:"+MarshallingUtils.marshall(profile));
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
	
	public HttpResponse createUser(SuiteData suiteDatadto,List<NameValuePair> headers,String payload) throws Exception
	 {
		String uri =suiteDatadto.getReferer()+InfraConstants.API_ADD_USER;
		Reporter.log(" Create user requeste ",true);
		//Reporter.log("Request API :"+uri,true);
		//URI dataUri = ClientUtil.BuidURI(uri);
		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(),suiteDatadto.getHost(), InfraConstants.API_ADD_USER);
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		return response;
	 }
	
	public HttpResponse createAdmin(SuiteData suiteDatadto,List<NameValuePair> headers,String payload,String profileId) throws Exception
	 {
		
		
		payload=payload.replace("55d8671e9dfa5156bed7601b", profileId);
		HttpResponse response=createUser(suiteDatadto, headers,payload);
		return response;
		
	 }
	
	public HttpResponse deleteUser(SuiteData suiteDatadto,List<NameValuePair> headers,String userId) throws Exception
	 {
		Reporter.log("Delete the user with userid :"+userId,true);
		
		System.out.println(" the value " +  suiteDatadto.getApiServer());

		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(), suiteDatadto.getApiServer(),"/"+suiteDatadto.getTenantDomainName()+InfraConstants.API_CALL_USERS+userId+"/");
		HttpResponse response =  restClient.doDelete(dataUri, headers);
		return response;
	 }
	
	public HttpResponse SearchGroup(SuiteData suiteDatadto,List<NameValuePair> headers,String name) throws Exception
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
	
	public String searchUserid(SuiteData suiteDatadto,List<NameValuePair> headers,String email) throws Exception
	{
	HttpResponse response=SearchUser(suiteDatadto, headers,  email);
	String responseBody = ClientUtil.getResponseBody(response);
	String userId = null;
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
	
	public HttpResponse createGroup(SuiteData suiteDatadto,List<NameValuePair> headers,String payload) throws Exception
	 {
		//create group
		Reporter.log(" Create user requeste ",true);
		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(),suiteDatadto.getHost(), InfraConstants.API_ADD_GROUP);
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		return response;
	 }
	
	public String getGroupid(String responseBody,String name ) throws JAXBException
	{
		Groups allGroups =  MarshallingUtils.unmarshall( responseBody, Groups.class);	
		String groupId = "";
		if(allGroups != null) {
			for(Groupslist group : allGroups.getGroupslist()) {			
				System.out.println("User:"+MarshallingUtils.marshall(group));
				if (group.getName().equals(name)) {
					groupId = group.getId();
					break;
				}
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
		queryParams.add(new BasicNameValuePair("limit", "90"));
		queryParams.add(new BasicNameValuePair("offset", "0"));
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
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
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
	
}