package com.elastica.beatle.tests.securlets;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.dto.SalesforceHit;
import com.elastica.beatle.securlets.dto.SalesforceLogs;
import com.elastica.beatle.securlets.dto.SalesforceSource;
import com.universal.constants.CommonConstants;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.servicenow.Incident;
import com.universal.dtos.servicenow.IncidentInput;
import com.universal.dtos.servicenow.Record;
import com.universal.dtos.servicenow.RecordInput;
import com.universal.dtos.servicenow.Result;

import junit.framework.Assert;



public class ServiceNowSanityTests extends CommonTest {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	private String incidentId;
	private String problemId;
	private String query;
	ArrayList<String> actualFileMessages = new ArrayList<String>();
	String resourceId;
	BoxUserInfo userInfo;
	

	public ServiceNowSanityTests() throws Exception {
		super();
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
	}	
	
	@Test(groups={"INCIDENT", "P1"})
	public void performSaasIncidentActivities() throws Exception {
		
		String steps[] = {	"1. This test create, update, retrieve and delete the incident in Service now.", 
		  					"2. Call Investigate API and retrieve the incident related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the operations in Service Now ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			incidentId ="";
			query = randomId;


			IncidentInput input = new IncidentInput();
			input.setComments("Automation Testing of ServiceNow Sanity");
			input.setShortDescription("This is the short description of incident for Sanity Test");
			
			Incident incident = snapi.createIncident(input);
			Reporter.log("Incident created:" + incident.getResult().getSysId(), true);
			
			
			query = incident.getResult().getSysId();
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			Result updateInput = new Result();
			updateInput.setSysId(incident.getResult().getSysId());
			updateInput.setState("4");
			updateInput.setShortDescription(randomId+"_updated the short description for Sanity Test");
			snapi.updateIncident(updateInput, null);
			
			incident = snapi.getIncident(incident.getResult().getSysId());
			Reporter.log("Incident retrieved:" + incident.getResult().getSysId(), true);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			incidentId = incident.getResult().getNumber();
			snapi.deleteIncident(incident.getResult().getSysId());
			Reporter.log("Incident deleted:" + incident.getResult().getSysId(), true);
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			Reporter.log("ServiceNow incident operations for sanity test completed ... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			e.printStackTrace();
			Reporter.log("Exception caught ... so skipping the test...", true);
			throw new SkipException("Skipping this test");
		}
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		ArrayList<String> logs;
		
		try {
			for (int i = 0; i <= (15 * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				String apiHost = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), null).toString();
				logs = searchDisplayLogs(-30, 5, "ServiceNow", query, suiteData.getUsername(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID());
				actualFileMessages.addAll(logs);
				Set<String> hs = new HashSet<>();
				hs.addAll(actualFileMessages);
				actualFileMessages.clear();
				actualFileMessages.addAll(hs);

				Reporter.log("Actual file messages1:" + actualFileMessages, true);
				if (actualFileMessages.size() >= 2) {break;}
			}
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			//throw new SkipException("Skipping this exception");
		}
	}
	
	
	@Test(groups={"PROBLEM", "P1"})
	public void performSaasProblemActivities() throws Exception {
		
		String steps[] = {	"1. This test create, update, retrieve and delete the problem in Service now.", 
		  					"2. Call Investigate API and retrieve the problem related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the operations in Service Now ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			problemId ="";
			query = randomId;
			
			RecordInput recordInput = new RecordInput();
			recordInput.setShortDescription("Sanity problem");
			recordInput.setComments("Reported a problem in sanity.");
			Record record = snapi.createRecord("problem", recordInput);
			Reporter.log("Problem created:" + record.getResult().getSysId(), true);
			
			sleep(CommonConstants.TWO_MINUTES_SLEEP);
			
			query = record.getResult().getSysId();
			Result updateInput = new Result();
			updateInput.setSysId(record.getResult().getSysId());
			updateInput.setState("4");
			updateInput.setShortDescription("Updated description");
			snapi.updateRecord("problem", updateInput, null);
			sleep(CommonConstants.TWO_MINUTES_SLEEP);
			
			record = snapi.getRecord("problem", record.getResult().getSysId());
			Reporter.log("Problem retrieved:" + record.getResult().getSysId(), true);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			problemId = record.getResult().getNumber();
			
			snapi.deleteRecord("problem", record.getResult().getSysId());
			Reporter.log("Record deleted:" + record.getResult().getSysId(), true);
			
			Reporter.log("ServiceNow problem operations for sanity test completed ... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			e.printStackTrace();
			Reporter.log("Exception caught ... so skipping the test...", true);
			throw new SkipException("Skipping this test");
		}
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		ArrayList<String> logs;
		
		try {
			for (int i = 0; i <= (10 * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				String apiHost = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), null).toString();
				logs = searchDisplayLogs(-30, 5, "ServiceNow", query, suiteData.getUsername(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID());
				actualFileMessages.addAll(logs);
				Set<String> hs = new HashSet<>();
				hs.addAll(actualFileMessages);
				actualFileMessages.clear();
				actualFileMessages.addAll(hs);

				Reporter.log("Actual file messages1:" + actualFileMessages, true);
				if (actualFileMessages.size() >= 4) {break;}
			}
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			//throw new SkipException("Skipping this exception");
		}
	}
	
	
	
	@Test(dependsOnMethods = "performSaasIncidentActivities", groups={"INCIDENT", "P1"})
	public void verifyIncidentCreationActivity() {
		LogUtils.logTestDescription("After creating the incident, verify the create incident in the activity logs.");
		String expectedLog = "User created new \"Incident\" with number \""+ incidentId +"\"";
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}
	
	@Test(dependsOnMethods = "performSaasIncidentActivities", groups={"INCIDENT", "P1"})
	public void verifyIncidentUpdateActivity() {
		LogUtils.logTestDescription("After updating the incident, verify the update incident in the activity logs.");
		String expectedLog = "User modified \"incident_state\" of \"Incident\" with number \""+ incidentId +"\" from \"1\" to \"4\"";
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}

	//@Test(dependsOnMethods = "performSaasIncidentActivities", groups={"FILE", "P1"})
	public void verifyIncidentDeleteActivity() {
		LogUtils.logTestDescription("After deleting the incident, verify the delete incident in the activity logs.");
		String expectedLog = "User updated 'Content Version History' with Title '"+incidentId+"'";
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}
	
	@Test(dependsOnMethods = "performSaasProblemActivities", groups={"PROBLEM", "P1"})
	public void verifyProblemCreationActivity() {
		LogUtils.logTestDescription("After creating the problem, verify the create problem in the activity logs.");
		String expectedLog = "User created new \"Problem\" with number \""+ problemId +"\"";
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}
	
	@Test(dependsOnMethods = "performSaasProblemActivities", groups={"PROBLEM", "P1"})
	public void verifyProblemUpdateActivity() {
		LogUtils.logTestDescription("After updating the problem, verify the update problem in the activity logs.");
		String expectedLog = "User modified \"problem_state\" of \"Problem\" with number \""+ problemId +"\" from \"1\" to \"4\"";
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}
	
	/*
	@Test
	public void testAssert() throws Exception {
		
		//String attributes = "{\"title\":\""+destinationFilename+ "\"}";
		
		File uploadFile = new java.io.File("/Users/pushparajt/Downloads/onelogin_metadata_491800.xml");
		
		String serviceJson = "{\"entity_id\":\"https://app.onelogin.com/saml/metadata/\",\"secondary_id_attribute_name\":\"PersonImmutableID\"\n,\"name\":\"OneLogin\",\"slo_supported\":false,\"resource_uri\":\"/api/admin/v1/idps/552c4f593db0e04120b784ef/\",\"last_name_attribute_name\":\"User.LastName\",\"name_attribute_name\":\"User.FirstName\",\"slo_service\":\"https://app.onelogin.com/trust/saml2/http-post/sso/440480\",\"hosts\":[\"app.onelogin.com\"],\"email_attribute_name\"\n:\"User.email\",\"tenant_attribute_name\":\"tobedefined\",\"is_beta\":false,\"auth_supported\":false,\"sso_service\"\n:\"https://app.onelogin.com/trust/saml2/http-post/sso/440480\",\"id\":\"552c4f593db0e04120b784ef\",\"tenant_independent\"\n:false,\"connectorUrl\":\"\"}";
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		
		builder.setBoundary("----WebKitFormBoundary3kopbBvEXXICdhKS");
		builder.setCharset(Charset.forName("UTF-8"));
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		FileBody fileBody = new FileBody(uploadFile, ContentType.create("text/xml"), "onelogin_metadata_491800.xml");
		builder.addPart("file", fileBody);
		builder.addTextBody("service", serviceJson);
		HttpEntity multipart = builder.build();
		
		//This is to print the entire entity
		java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
				(int) multipart.getContentLength());
		multipart.writeTo(out);
		String entityContentAsString = new String(out.toByteArray());
		System.out.println("multipartEntitty:" + entityContentAsString);
		
		
		String path = "/admin/user/tenant_sso_status";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), "eoe.elastica-inc.com", path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		
		List<NameValuePair> headers = this.getHeaders1();
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, multipart);
		
		//HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, multipart, null);	
		String responseBody = ClientUtil.getResponseBody(response);
		
		System.out.println("Response Body:"+responseBody);
		
	}*/
	
	//Utilities
	
	public ArrayList<String> searchDisplayLogs(int from, int to, String facilty, String query, String email, String apiServerUrl, String csrfToken, String sessionId) throws Exception {
		
		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);

		//Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQueryForDisplayLogs(tsfrom, tsto, facilty, query, email, apiServerUrl, csrfToken, sessionId);
		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("==============================================================================");
		
		
		SalesforceLogs fsr = null;
		try {
		 fsr = MarshallingUtils.unmarshall(responseBody, SalesforceLogs.class);
		}
		catch(Exception e){}
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
		return this.retrieveActualMessages(fsr);
		
	}	

	private ArrayList<String> retrieveActualMessages(SalesforceLogs fsr) {
		ArrayList<String> alist = new ArrayList<String>();

		for (SalesforceHit hit : fsr.getHits().getHits()) {
			SalesforceSource source  = hit.getSource();
			alist.add(source.getMessage());
		}
		return alist;
	}
	
		
}