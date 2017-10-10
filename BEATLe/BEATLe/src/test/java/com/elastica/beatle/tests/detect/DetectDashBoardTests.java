package com.elastica.beatle.tests.detect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.detect.dto.UserObject;
import com.elastica.beatle.es.ElasticSearchLogs;

public class DetectDashBoardTests extends  DetectUtils {
	
	
	@BeforeClass()
	public void beforeClass()   {
		clearIncidents();
		}
	
	@DataProvider()
    public static Object[][] dataProvider(Method method)  {
		
		Object[][] atrributesDP = new Object[5][];
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
		Calendar cal = Calendar.getInstance();
    	String endDate = formatDate.format(cal.getTime());
    	Calendar cal1 = Calendar.getInstance();
    	cal1.add(Calendar.DATE, -1);
    	String StartDate1day= formatDate.format(cal1.getTime());
    	Calendar cal2 = Calendar.getInstance();
    	cal2.add(Calendar.DATE, -2);
    	String StartDate1week= formatDate.format(cal2.getTime());
    	Calendar cal3 = Calendar.getInstance();
    	cal3.add(Calendar.DATE, -3);
    	String StartDate1month= formatDate.format(cal3.getTime());
    	Calendar cal4 = Calendar.getInstance();
    	cal4.add(Calendar.DATE, -4);
    	String StartDate3Months= formatDate.format(cal4.getTime());
    	Calendar cal5 = Calendar.getInstance();
    	cal5.add(Calendar.DATE, -5);
    	String StartDate6Months= formatDate.format(cal4.getTime());
		
		atrributesDP[0] = new Object[] {StartDate1day, endDate};
		atrributesDP[1] = new Object[] {StartDate1week, endDate  };
		atrributesDP[2] = new Object[] {StartDate1month, endDate   };
		atrributesDP[3] = new Object[] {StartDate3Months, endDate   };
		atrributesDP[4] = new Object[] {StartDate6Months, endDate   };
		return atrributesDP;
	}

	
	@Test(dataProvider="dataProvider")
	public void DetectIncidents(String strDateTime, String EndDate) throws UnsupportedEncodingException, JAXBException, Exception{
		
		String responseBody=null;
		HttpResponse response=null;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		response = esLogs.getanomalyvalues(restClient, buildCookieHeaders(), suiteData);
		String emailId=null;
		String id=null;
		
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		responseBody = getResponseBody(response);

    	
		String payload = esQueryBuilder.getSearchQueryForDetect(strDateTime,EndDate);
		response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(), new StringEntity(payload),suiteData);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		responseBody = getResponseBody(response);
		Reporter.log("ResponseBody Of GetCloudServiceAnomalies ::::: "+responseBody, true);

		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		
		String ioi=null; String responsibleLogs=null; String userName=null;
		String service=null; String severity=null; String threatScore=null;
		String email1=null; String userCountry=null;String userCity=null;
		String userMessage=null;String activityType=null; String userCreatedTimeStamp = null;
		String IOIActual= null;
		
		Map<String, UserObject> userData =null;
		
		if (jnode.isArray()) {
		for (final JsonNode objNode : jnode) {
		    	
			id = getJSONValue(objNode.toString(), "id");
	    	ioi = getJSONValue(objNode.toString(), "ioi").replace("_", " ");
	    	severity = getJSONValue(objNode.toString(), "s");
	    	emailId = getJSONValue(objNode.toString(), "e");
	    	responsibleLogs = getJSONValue(objNode.toString(), "rl");
	    	userName = getJSONValue(objNode.toString(), "u");
			service = getJSONValue(objNode.toString(),"f");
			threatScore = getJSONValue(objNode.toString(), "ar");
			email1 = emailId.replace("\"", "");
			userCountry = getJSONValue(objNode.toString(),"cy");
			userCity = getJSONValue(objNode.toString(), "ct");
			userMessage = getJSONValue(objNode.toString(),"m");
			activityType = getJSONValue(objNode.toString(),"at");
			userCreatedTimeStamp = getJSONValue(objNode.toString(), "its");
			IOIActual= getJSONValue(objNode.toString(),"m");
			String subMessage=null;
			int iend = IOIActual.indexOf(":");
			if (iend != -1) {
				subMessage = IOIActual.substring(0 , iend).replace("\"", "");
			}
			 								/*Creating user object*/
			userData = new HashMap<>();
			UserObject userdata1 = new UserObject(severity, ioi, userName, service, id, threatScore, 0,userCountry, userCity, userMessage, activityType, responsibleLogs, userCreatedTimeStamp, subMessage);
			userData.put(emailId, userdata1);
		
											/* Automating getUserThreatsData*/	
			
		String csfrtoken = suiteData.getCSRFToken();
		String sessionId =  suiteData.getSessionID();
		String payload1 = "{\"sourceName\":\"DETECT\",\"apiServerUrl\":\"https://api-eoe.elastica-inc.com/\",\"csrftoken\":\""+csfrtoken+"\",\"sessionid\":\""+sessionId+"\",\"userid\":\""+suiteData.getUsername()+"\"}";
		StringEntity se = new StringEntity(payload1);
		
		response = esLogs.getUserThreatsData(restClient, buildBasicHeaders(csfrtoken, sessionId), se, suiteData);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		responseBody = getResponseBody(response);
		Reporter.log("ResponseBody Of GetUserThreatsData ::::: "+responseBody, true);

		
		JsonNode User_List = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "users_list"));
		
		if(User_List.has(email1)){
		
		JsonNode userInfo = new ObjectMapper().readTree(getJSONValue(User_List.toString(), email1 ));
		String alert_ids =getJSONValue(userInfo.toString(), "alert_ids").replace("[", "").replace("]", "");
		JsonNode servicesNode = new ObjectMapper().readTree(getJSONValue(userInfo.toString(), "services"));
		
		String threatScoreActual=null;
		String servicesName = null;
		String ioiName = null;
		
		if(servicesNode.isArray()){
				for(final JsonNode serviceDetails : servicesNode){
					servicesName = getJSONValue(serviceDetails.toString(), "name");
					threatScoreActual= getJSONValue(serviceDetails.toString(), "threat_score");
				}
			}
			
		JsonNode ioiNode = new ObjectMapper().readTree(getJSONValue(userInfo.toString(), "ioi"));

			if(ioiNode.isArray()){
				for(final JsonNode ioioDetails : ioiNode){
					ioiName = getJSONValue(ioioDetails.toString(), "name");
				}
			}
	
		String usrName =getJSONValue(userInfo.toString(), "user_name").replace("[", "").replace("]", "");
			
										/*Assertions*/
		Reporter.log("ExpectedName ::::: "+usrName+" ActualName:::: "+userData.get(emailId).getUserName(), true);
		Assert.assertEquals(usrName, userData.get(emailId).getUserName(),"UserName does not match");
		
		Reporter.log("ExpectedService ::::: "+servicesName+" ActualService::::::: "+userData.get(emailId).getService(), true);
		Assert.assertEquals(servicesName.replace("\"", ""), userData.get(emailId).getService().replace("\"", ""),"Services does not match");
		
		Reporter.log("ExpectedAlert_Ids ::::: "+alert_ids+" ActualAlertIds::::::: "+userData.get(emailId).getId(), true);
		//Assert.assertEquals(alert_ids, userData.get(emailId).getId(),"Id does not match");
		Reporter.log("threatScoreActual ::::: "+threatScoreActual+" Expected::::::: "+userData.get(emailId).getThreatScore().replaceAll("\\.0*$", ""), true);
		System.out.println(Integer.parseInt(threatScoreActual)>=Integer.parseInt(userData.get(emailId).getThreatScore().replaceAll("\\.0*$", "")));
		Assert.assertTrue(Integer.parseInt(threatScoreActual)>=Integer.parseInt(userData.get(emailId).getThreatScore().replaceAll("\\.0*$", "")),"Threat Score Does not Match");
		String ioiActual = ioiName.replace("\"", "");
		String ioiExp =(userData.get(emailId).getIOIActual().replace("\"", ""));
		String ioiExpected1 = ioiExp.replace(" sequence threshold exceeded", "");
		String ioiExpected = ioiExpected1.substring(1, ioiExpected1.length() - 1);
		Reporter.log("Expected ::::: "+ioiExpected+" ::::::::: "+ioiActual, true);
		//TODO:::re
		if(!containsDigit(ioiActual)){
		Assert.assertTrue((ioiActual.toLowerCase()).contains(ioiExpected.toLowerCase()),"ioi does not match");
		}


										/*Automating getNotes*/
		
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		String id1 = id.replace("\"", "");
		queryParams.add(new BasicNameValuePair("source", "{\"id\":\""+id1+"\",\"type\":\"elastica_state\",\"index\":\"logs_detectbeatlecom-2016\",\"event_type\":\"AnomalyReport\"}"));
		queryParams.add(new BasicNameValuePair("sourceName", "DETECT"));
		
		response = esLogs.getNotes(restClient, buildCookieHeaders(), queryParams, suiteData);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");

		responseBody = getResponseBody(response);
		Reporter.log("ResponseBody Of GetNotes ::::: "+responseBody, true);
		
		JsonNode _id = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "_id"));
		JsonNode source = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "_source"));
		JsonNode host = new ObjectMapper().readTree(getJSONValue(source.toString(), "host"));
		String countryValidation=userData.get(emailId).getCountry().replace("[", "").replace("]", "");
		
		if(countryValidation!=null){ 
			if(countryValidation.contains("Unknown")) continue;
			JsonNode country = new ObjectMapper().readTree(getJSONValue(host.toString(), "country"));
			JsonNode city = new ObjectMapper().readTree(getJSONValue(host.toString(), "city"));

			Reporter.log("ExpectedCountry ::::: "+country.toString()+" ActualCountry:::: "+userData.get(emailId).getCountry(), true);
			Assert.assertEquals(country.toString(), userData.get(emailId).getCountry(), "Country does not match");
			Reporter.log("ExpectedCity ::::: "+city.toString()+" ActualCity:::: "+userData.get(emailId).getCity(), true);
			Assert.assertEquals(city.toString(), userData.get(emailId).getCity(), "City does not match");
			
		}
		
			
		String anomaly_ratio =getJSONValue(source.toString(), "anomaly_ratio");
		String facility =getJSONValue(source.toString(), "facility");
		String created_timestamp =getJSONValue(source.toString(), "created_timestamp");
		String message =getJSONValue(source.toString(), "message");
		String Activity_type =getJSONValue(source.toString(), "Activity_type");
		String sevrity=getJSONValue(source.toString(), "severity");
		String user_name =getJSONValue(source.toString(), "user_name");
		String userEmail =getJSONValue(source.toString(), "user");
		String responsible_logs =getJSONValue(source.toString(), "responsible_logs");
		String ioi_code =getJSONValue(source.toString(), "ioi_code");
		
								    /*Assertions*/
		Reporter.log("ExpectedName ::::: "+user_name+" ActualName:::: "+userData.get(emailId).getUserName(), true);
		Assert.assertEquals(user_name, userData.get(emailId).getUserName(), "Name does not match");
		Reporter.log("ExpectedUserEmail ::::: "+userEmail+" ActualUserEmail:::: "+emailId, true);
		Assert.assertEquals(userEmail, emailId, "Activity type does not match");
		Reporter.log("ExpectedThreatScore ::::: "+anomaly_ratio+" ActualThreatScore:::: "+userData.get(emailId).getThreatScore(), true);
		Assert.assertEquals(anomaly_ratio, userData.get(emailId).getThreatScore(), "Anomaly ratio does not match");
		Reporter.log("ExpectedUserID ::::: "+_id.toString()+" ActualUserID:::: "+userData.get(emailId).getId(), true);
		Assert.assertEquals(_id.toString(), userData.get(emailId).getId(),"Id does not match");
		Reporter.log("ExpectedMessage ::::: "+message+" ActualMessage:::: "+userData.get(emailId).getMessage(), true);
		Assert.assertEquals(message, userData.get(emailId).getMessage(), "Message does not match");
		Reporter.log("Expectedfacility ::::: "+facility+" Actualfacility:::: "+userData.get(emailId).getService(), true);
		Assert.assertEquals(facility, userData.get(emailId).getService(), "facility does not match");
		Reporter.log("ExpectedCreated_TimeStamp ::::: "+created_timestamp+" ActualCreated_TimeStamp:::: "+userData.get(emailId).getUserCreatedTimeStamp(), true);
		Assert.assertEquals(created_timestamp, userData.get(emailId).getUserCreatedTimeStamp(), "Creates TimeStamp Does not match");
		Reporter.log("ExpectedActivityType ::::: "+Activity_type+" ActualActivityType:::: "+userData.get(emailId).getActivityType(), true);
		if(Activity_type!=null){
		Assert.assertEquals(Activity_type, userData.get(emailId).getActivityType(), "Activity type does not match");
		}
		Reporter.log("ExpectedSeverity::::: "+sevrity+" ActualSeverity:::: "+userData.get(emailId).getSeverity(), true);
		Assert.assertEquals(sevrity, userData.get(emailId).getSeverity(), "Severity does not match");
		Reporter.log("ExpectedIOI ::::: "+ioi_code+" ActualIOI:::: "+userData.get(emailId).getIoiName(), true);
		Assert.assertEquals(ioi_code.replace("_", " "), userData.get(emailId).getIoiName(), "IOI Code does not match");
		Reporter.log("ResponsiblelogsExpected ::::: "+responsible_logs+" :::::::::" +"ResponsiblelogsExpectedActual::::"+userData.get(emailId).getResponsibleLogs(), true);
		Assert.assertEquals(responsible_logs.replace("[", "").replace("]", ""), userData.get(emailId).getResponsibleLogs().replace("[", "").replace("]", ""), "Responsible Logs type does not match");
		
		
			}
		
		}
		
	}	
		Thread.sleep(1*15*1000);
}		
	
	protected List<NameValuePair> buildBasicHeaders(String csfrtoken, String sessionId) throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", csfrtoken));
		requestHeader.add(new BasicNameValuePair("X-Session", sessionId));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		requestHeader.add(new BasicNameValuePair("Referer", "https://eoe.elastica-inc.com/static/ng/appThreats/index.html"));
		requestHeader.add(new BasicNameValuePair("Host", "api-eoe.elastica-inc.com"));
		requestHeader.add(new BasicNameValuePair("Accept", "application/json"));
		requestHeader.add(new BasicNameValuePair("Content-Type", "application/json"));
		return requestHeader;
	}
	
	public final boolean containsDigit(String s){  
	    boolean containsDigit = false;

	    if(s != null && !s.isEmpty()){
	        for(char c : s.toCharArray()){
	            if(containsDigit = Character.isDigit(c)){
	                break;
	            }
	        }
	    }

	    return containsDigit;
	}
	
}
