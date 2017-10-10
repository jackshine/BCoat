package com.universal.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Reporter;

import com.universal.constants.BoxConstants;
import com.universal.constants.CommonConstants;
import com.universal.constants.ServiceNowConstants;
import com.universal.constants.ServiceNowConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxFile;
import com.universal.dtos.box.BoxToken;
import com.universal.dtos.box.Entry;
import com.universal.dtos.box.FileConflict;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.salesforce.APIVersion;
import com.universal.dtos.salesforce.ChatterFile;
import com.universal.dtos.salesforce.GroupDetail;
import com.universal.dtos.salesforce.GroupInput;
import com.universal.dtos.salesforce.Resources;
import com.universal.dtos.servicenow.Incident;
import com.universal.dtos.servicenow.IncidentInput;
import com.universal.dtos.servicenow.Record;
import com.universal.dtos.servicenow.RecordInput;
import com.universal.dtos.servicenow.Result;
import com.universal.util.OAuth20Token;
import com.universal.util.TokenProducer;
import com.universal.util.Utility;

public class ServiceNow extends CommonTest{
	private TokenProducer tokenProducer;
	private UserAccount userAccount;
	ResourceBundle bundle;
	String tenant;

	public ServiceNow(UserAccount userAccount) throws Exception{

	}

	public ServiceNow(TokenProducer tokenProducer, UserAccount userAccount) throws Exception {
		this.tokenProducer = tokenProducer;
		this.userAccount   = userAccount;
		bundle = new Utility().getProperties("com.universal.configuration.servicenow_configuration");

		int dotcount = StringUtils.countMatches(userAccount.getUsername(), ".");
		if (dotcount >= 2) {
			String valid = userAccount.getUsername().substring(0,userAccount.getUsername().lastIndexOf("."));
			this.tenant = StringUtils.split(valid, "@")[1].replace(".", "");
		} else {
			this.tenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "");
		}
	}
	
	public List<NameValuePair> getToken() throws Exception {
		return this.getAuthHeadersOnly();
	}
	
	
	//Create an incident
	public Incident createIncident(IncidentInput incidentInput) throws Exception {
		String payload = marshall(incidentInput);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = ServiceNowConstants.SN_INCIDENT_CREATE_URI;
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), ServiceNowConstants.SN_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		
		Incident incidentDetail = unmarshall(responseBody, Incident.class);
		return incidentDetail;
	}
	
	
	public Incident updateIncident(Result result, ArrayList<NameValuePair> queryParameters) throws Exception {
		String payload = marshall(result);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = ServiceNowConstants.SN_INCIDENT_UPDATE_URI;
		uriPath = uriPath.replace("{sysid}", result.getSysId());
		
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), ServiceNowConstants.SN_API_HOST, getPort(), uriPath, queryParameters, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		
		Incident incidentDetail = unmarshall(responseBody, Incident.class);
		return incidentDetail;
	}
	
	
	
	//Retrieve incident details
	public Incident getIncident(String sysId) throws Exception {
		Reporter.log("Get the incident with sysid "+sysId, true);
		String uriPath = ServiceNowConstants.SN_INCIDENT_GET_URI;
		uriPath = uriPath.replace("{sysid}", sysId);
		
		URI uri = Utility.getURI(getNetworkProtocol(), ServiceNowConstants.SN_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		
		Incident incidentDetail = unmarshall(responseBody, Incident.class);
		return incidentDetail;
	}
	
	//Delete the incident
	public int deleteIncident(String sysId) throws Exception {
		Reporter.log("Delete the incident with sysid "+sysId, true);
		String uriPath = ServiceNowConstants.SN_INCIDENT_DELETE_URI;
		uriPath = uriPath.replace("{sysid}", sysId);
		URI uri = Utility.getURI(getNetworkProtocol(), ServiceNowConstants.SN_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, getHeaders(), null, null);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		return response.getStatusLine().getStatusCode();
	}


	
	//Create an incident
	public Record createRecord(String tablename, RecordInput recordInput) throws Exception {
		String payload = marshall(recordInput);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = ServiceNowConstants.SN_RECORD_CREATE_URI;
		uriPath = uriPath.replace("{tablename}", tablename);
		
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), ServiceNowConstants.SN_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		
		Record recordDetail = unmarshall(responseBody, Record.class);
		return recordDetail;
	}
	
	
	public Record updateRecord(String tablename, Result result, ArrayList<NameValuePair> queryParameters) throws Exception {
		String payload = marshall(result);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = ServiceNowConstants.SN_RECORD_UPDATE_URI;
		uriPath = uriPath.replace("{sysid}", result.getSysId()).replace("{tablename}", tablename);
		
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), ServiceNowConstants.SN_API_HOST, getPort(), uriPath, queryParameters, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		
		Record recordDetail = unmarshall(responseBody, Record.class);
		return recordDetail;
	}
	
	
	
	//Retrieve record details
	public Record getRecord(String tablename, String sysId) throws Exception {
		Reporter.log("Get the incident with sysid "+sysId, true);
		String uriPath = ServiceNowConstants.SN_RECORD_GET_URI;
		uriPath = uriPath.replace("{sysid}", sysId).replace("{tablename}", tablename);
		
		URI uri = Utility.getURI(getNetworkProtocol(), ServiceNowConstants.SN_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		
		Record recordDetail = unmarshall(responseBody, Record.class);
		return recordDetail;
	}
	
	//Delete the record
	public int deleteRecord(String tablename, String sysId) throws Exception {
		Reporter.log("Delete the record from the table "+ tablename +" with sysid "+sysId, true);
		String uriPath = ServiceNowConstants.SN_RECORD_DELETE_URI;
		uriPath = uriPath.replace("{sysid}", sysId).replace("{tablename}", tablename);
		URI uri = Utility.getURI(getNetworkProtocol(), ServiceNowConstants.SN_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, getHeaders(), null, null);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		return response.getStatusLine().getStatusCode();
	}
	
	
	private List<NameValuePair> getHeaders() throws Exception {
		if (this.tokenProducer.getoAuth20Token().getAccessToken() == null) {
			//regenerate it
			this.initAccessTokenByUsernameAndPassword();
		}

		headers.clear();
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, "Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken())); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		
		Reporter.log(HttpHeaders.AUTHORIZATION + " Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken(), true);
		return headers;
	}

	private List<NameValuePair> getAuthHeadersOnly() throws Exception {
		headers.clear();
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, " Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken()));  
		return headers;
	}


	public boolean initAccessTokenByUsernameAndPassword() throws Exception {
		if (userAccount.getUsername() != null || userAccount.getPassword() != null) {
			this.tokenProducer.getoAuth20Token().setAccessToken(getAccessTokenByUsernameAndPassword().getAccessToken());
		}
		return false;
	}


	private String getHost() {
		return bundle.getString(CommonConstants.SN_HOST);
	}

	private int getPort() {
		if (bundle.getString(CommonConstants.SN_PORT).length() > 0) {
			return Integer.parseInt(bundle.getString(CommonConstants.SN_PORT));
		} else {
			return -1;
		}
	}

	private String getNetworkProtocol() {
		return bundle.getString(CommonConstants.SN_NETWORK_PROTOCOL);
	}


	public OAuth20Token getAccessTokenByUsernameAndPassword() throws Exception {
		OAuth20Token oAuth20Token = new OAuth20Token();

		URI uri = Utility.getURI(getNetworkProtocol(), ServiceNowConstants.SN_OAUTH20_HOST, getPort(), ServiceNowConstants.SN_OAUTH20_TOKEN_URL, queryParams, null);

		String username = StringUtils.split(this.userAccount.getUsername(), "@")[0];
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_ID, 		tokenProducer.getClientId()));
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_SECRET, 	tokenProducer.getClientSecret()));
		formparams.add(new BasicNameValuePair(CommonConstants.USERNAME, 		username));
		formparams.add(new BasicNameValuePair(CommonConstants.PASSWORD, 		this.userAccount.getPassword()));
		formparams.add(new BasicNameValuePair(CommonConstants.GRANT_TYPE, 		CommonConstants.PASSWORD));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams); 
		Reporter.log("Request body:"+ IOUtils.toString(entity.getContent(), "UTF-8"), true);
		//Add the entity header application/x-www-form-urlencoded"
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE));   	    	
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, 		 MediaType.APPLICATION_JSON)); 

		boolean isTokenGenerated = false;
		int attempts = 1;
		//Try for 
		while(!isTokenGenerated ) {
			Reporter.log("Trying to get auth token for service now...Attempt:"+attempts, true);
			HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);		
			String strResponse = getResponseBody(response);
			Reporter.log("Response body:"+strResponse, true);
			Reporter.log("Response Code:"+response.getStatusLine().getStatusCode(), true);
			oAuth20Token =  unmarshall(strResponse, OAuth20Token.class); 


			//If the response is not 200
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Reporter.log("Unable to fetch the token. Please check user name and password", true);
				attempts++;
				if(attempts > CommonConstants.REQEUST_RETRY_ATTEMPTS) {
					Reporter.log("Unable to generate the token after "+CommonConstants.REQEUST_RETRY_ATTEMPTS+ " attempts.", true);
					isTokenGenerated = true;
				}
			} else {
				isTokenGenerated = true;
			}
		}
		return oAuth20Token;
	}

}
