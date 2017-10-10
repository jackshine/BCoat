package com.elastica.action.infra;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.elasticsearch.common.collect.Ordering;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Reporter;

//import com.elastica.beatle.RestClient.ClientUtil;

public class InfraUtils {
	public  String SearchUserInGrp(String Responsebody,String userlist,String field,String name,String returnfield) throws JSONException
	 {
		 JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
			
			String Name="";
			for(int i=0; i<summaryObject.length(); i++)
			{
				
				JSONObject userObj=summaryObject.getJSONObject(i);
				
				if(userObj.get(field).toString().contentEquals(name))
				{
					Name=(String)userObj.get(returnfield);
					break;
				}
				
			}
			Reporter.log("userId...."+Name,true);
			return Name;
			
	 }	
	public  String getAttributeFromResponseHeader(Header[] lHeaders, String sAttribute) {
		String sHeaderAttribute = null;

		try {
			for (Header header : lHeaders) {
				if (header.getValue().indexOf(sAttribute) > -1) {
					sAttribute = sAttribute + "=";
					int startAt = header.getValue().indexOf(sAttribute) + sAttribute.length();
					int endAt = header.getValue().indexOf(";", startAt);
					sHeaderAttribute = header.getValue().substring(startAt, endAt);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (sHeaderAttribute);
	}
	
	public  String AppSubscriptionStatus(String Responsebody,String userlist,String field,String name) throws JSONException
	 {
		 JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
			
			String Name=null;
			String AppStatus=null;
			for(int i=0; i<summaryObject.length(); i++)
			{
				
				JSONObject userObj=summaryObject.getJSONObject(i);
				
				if(userObj.get(InfraConstants.APP_NAME).toString().equals(name))
				{
					Name=(String)userObj.get(field);
					AppStatus=(String)userObj.get(InfraConstants.APP_STATUS);
					break;
				}
				else
				{
					AppStatus=null;
				}
				
			}
			Reporter.log("App Status...."+AppStatus,true);
			return AppStatus;
			
	 }

	public 	String getUserid(String testDesc,String Responsebody,String userlist,String field,String name) throws JSONException
	 {
		 JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
			
			String Id=null;
			for(int i=0; i<summaryObject.length(); i++)
			{
				
				JSONObject userObj=summaryObject.getJSONObject(i);
				
				if(userObj.get(field).toString().equals(name))
				{
					if( testDesc.contains("Gateway"))
					{
						Id=(String)userObj.get("gateway_subscription_id");
						break;
					}
					else
					{
					Id=(String)userObj.get("subscription_id");
					break;
					}
				}
				
			}
			Reporter.log("userId...."+Id,true);
			return Id;
			
	 }
	
	public 	boolean getSubscriptionStatus(String Responsebody,String userlist,String field,String name,String Subscription) throws JSONException
	 {
		 JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
			
		 boolean AppSubscription=false;
			for(int i=0; i<summaryObject.length(); i++)
			{
				
				JSONObject userObj=summaryObject.getJSONObject(i);
				
				if(userObj.get(field).toString().equals(name))
				{
					AppSubscription=(boolean)userObj.get(Subscription);
					break;
				}
				
			}
			Reporter.log("AppSubscription...."+AppSubscription,true);
			return AppSubscription;
			
	 }
	public 	String getUserid(String Responsebody,String userlist,String field,String name) throws JSONException
	 		{
		 	JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
			
			String Id=null;
			for(int i=0; i<summaryObject.length(); i++)
			{
				
				JSONObject userObj=summaryObject.getJSONObject(i);
				
				if(userObj.get(field).toString().equals(name))
				{
					Id=(String)userObj.get("id");
					break;
				}
				
			}
			Reporter.log("userId...."+Id,true);
			return Id;
			
	 }
	
	
	public 	String DeleteallPayload(String Responsebody,String userlist,List<String> data,String payload) throws JSONException
		{
		JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
	
		//List<String> data=new ArrayList<String>();
		for(int i=0; i<summaryObject.length(); i++)
		{
		
			JSONObject userObj=summaryObject.getJSONObject(i);
		
			String email=(String)userObj.get("email");
			String uri=(String)userObj.get("resource_uri");
			if(!email.contains("admin@infrabeatle.com"))
			{
				data.add(email);
				data.add(uri);
			}
			else
			{
				//do nothing
			}
		
		}
		Reporter.log("detail of all user....",true);
		 payload="{\"users\":[";
		for(int i=0; i<data.size();i++)
		{
			if((i%2==0))
			{
				payload+="{\"email\":\""+data.get(i)+"\",";
				
			}
			else
			{
				if(i+1!=data.size())
				{
					payload+="\"resource_uri\":\""+data.get(i)+"\"},";
				}
				else
				{
					payload+="\"resource_uri\":\""+data.get(i);
				}
				
			}
				
		}
		payload+="\"}]}";
		return payload;
	
	}

	
	public  boolean orderCheck(String responseBody,String groupslist,String name,String order) throws JSONException
			{
				//String responseBody = ClientUtil.getResponseBody(response);
				List<String> userlist = new ArrayList<String>();
				{
					JSONArray summaryObject = (JSONArray) new JSONObject(responseBody).getJSONArray(groupslist);
			
					String Id=null;
					for(int i=0; i<summaryObject.length(); i++)
						{
				
							JSONObject userObj=summaryObject.getJSONObject(i);
				
							if(!userObj.get(name).toString().isEmpty())
								{
								Id=(String)userObj.get(name);
								//break;
								userlist.add(i, Id);
								}
				
						}
				}
		System.out.println("group list: "+userlist);
		// Collections.sort(userlist);
		boolean sorted=false;
		if(order.contains("ascend"))
		{
			 sorted = Ordering.natural().isOrdered(userlist);
		}
		else
		{
			 sorted = Ordering.natural().reverse().isOrdered(userlist);
		}
		 return sorted;
	}
	
	public  String SearchName(String Responsebody,String userlist,String field,String name) throws JSONException
	 		{
				JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
			
				String Name=null;
				for(int i=0; i<summaryObject.length(); i++)
				{
				
					JSONObject userObj=summaryObject.getJSONObject(i);
				
					if(userObj.get(field).toString().equals(name))
					{
						Name=(String)userObj.get(field);
						break;
					}
				
				}
			Reporter.log("userId...."+Name,true);
			return Name;
			
	 }
	public  String getStatus(String Responsebody,String userlist,String field) throws JSONException
		{
		JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
	
		String Name=null;
		for(int i=0; i<summaryObject.length(); i++)
		{
		
			JSONObject userObj=summaryObject.getJSONObject(i);
		
			//if(userObj.get(field).toString().equals(name))
			{
				Name=(String)userObj.get(field);
				break;
			}
		
		}
	Reporter.log("userId...."+Name,true);
	return Name;
	
}
	
	public 	String getUserFieldData(String Responsebody,String userlist,String field,String name,String data) throws JSONException
	 {
		 JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
			
			String Name=null;
			for(int i=0; i<summaryObject.length(); i++)
			{
				
				JSONObject userObj=summaryObject.getJSONObject(i);
				
				if(userObj.get(field).toString().equals(name))
				{
					if(data.contains("is_admin"))
					{
						Name=   Boolean.toString((boolean)userObj.get(data));
						break;
					}
					else if(data.contains("is_active"))
					{
						Name=   Boolean.toString((boolean)userObj.get(data));
						break;
					}
					{
						Name=(String)userObj.get(data);
						break;
					}
					
				}
				
			}
			Reporter.log("Data value is...."+Name,true);
			return Name;
			
	 }
//	public static String SearchUserInGrp(String Responsebody,String userlist,String field,String name,String returnfield) throws JSONException
//	 {
//		 JSONArray summaryObject = (JSONArray) new JSONObject(Responsebody).getJSONArray(userlist);
//			
//			String Name=null;
//			for(int i=0; i<summaryObject.length(); i++)
//			{
//				
//				JSONObject userObj=summaryObject.getJSONObject(i);
//				
//				if(userObj.get(field).toString().equals(name))
//				{
//					Name=(String)userObj.get(returnfield);
//					break;
//				}
//				
//			}
//			Reporter.log("userId...."+Name,true);
//			return Name;
//			
//	 }	
}
