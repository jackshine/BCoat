package com.elastica.beatle.securlets;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.Source;
import com.elastica.beatle.tests.gmail.GmailActivity;
import com.elastica.beatle.tests.securlets.BoxActivity;
import com.elastica.beatle.tests.securlets.CustomAssertion;
import com.elastica.beatle.tests.securlets.OneDriveBusinessActivity;
import com.elastica.beatle.tests.securlets.OneDriveSiteActivity;
import com.elastica.beatle.tests.securlets.salesforce.SalesforceActivity;

import org.testng.asserts.SoftAssert;

public class LogValidator {
	ForensicSearchResults fsr;
	SoftAssert softAssert = new SoftAssert();
	
	public LogValidator() {
		
	}
	
	public LogValidator(ForensicSearchResults fsr) {
		this.fsr = fsr;
	}
	
	public void verifyHitCountGreater(int count) {
		CustomAssertion.assertTrue(count > 0, "Hits count do not match");
	}
	
	public void validateAll() {
		verifyHitCountGreater(this.fsr.getHits().getHits().size());
		verifyMetadata();
	}
	
	
	public void verifyMetadata() {
		for (Hit hit : fsr.getHits().getHits()) {
			//Assert.assertTrue(hit.getSort().size() > 0, "Sort is not present");
			//Assert.assertTrue(hit.getSort().get(0).longValue() > 0, "Sort value is not present");
			
			Assert.assertNotNull(hit.getId(), "_id is null");
			Assert.assertNotNull(hit.getIndex(), "_index is null");
			Assert.assertNotNull(hit.getScore(), "_score is null");
			Assert.assertNotNull(hit.getType(), "_type is null");
			Assert.assertNotNull(hit.getSource(), "_source is null");	
			this.verifySourceForNotNullValues(hit.getSource());
		}
	}
	
	public void verifyActivityLog(String message) {
		ArrayList<String> alist = new ArrayList<String>();
		
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			alist.add(source.getMessage());
		}
		CustomAssertion.assertTrue(alist.contains(message), "Expected activity not available in the log");
	}
	
	public void verifyLog(ArrayList<String> logs,  String message) {
		Reporter.log("Expected value::" + message, true);
		CustomAssertion.assertTrue(logs.contains(message), message);
	}
	
	
	public void verifySourceForNotNullValues(Source source) {
		if (source.getSource().equals("__API")) { 
			
			assertNotNull(source.getFacility(), "Facility is null");
			assertNotNull(source.getFileSize(), "Filesize is null");
			Assert.assertNotNull(source.getCreatedTimestamp(), "Created timestamp is null");
			Assert.assertNotNull(source.getInsertedTimestamp(), "Inserted timestamp is null");

			Assert.assertNotNull(source.getMessage(), "Message is null");
			Assert.assertNotNull(source.getActivityType(), "ActivityType is null");
			Assert.assertNotNull(source.getSeverity(), "Severity is null");
			Assert.assertNotNull(source.getObjectType(), "ObjectType is null");
			Assert.assertNotNull(source.getUserName(), "Username is null");
			Assert.assertNotNull(source.getParent(), "Parent is null");
			Assert.assertNotNull(source.getResourceId(), "Resource Id is null");
			Assert.assertNotNull(source.getHost(), "Host is null");
			Assert.assertNotNull(source.getUser(), "User is null");
			Assert.assertNotNull(source.getObjectName(), "Object name is null");
			Assert.assertNotNull(source.getSource(), "Source is null");

			Assert.assertNotNull(source.getCountry(), "City is null");
			Assert.assertNotNull(source.getCity(), "City is null");
			Assert.assertNotNull(source.getLatitude(), "Latitude is null");
			Assert.assertNotNull(source.getLongitude(), "Longitude is null");
			Assert.assertNotNull(source.getLocation(), "Location is null");

		}
	}
	
	
	public void verifySource(Source source, String objectType, String facility, String username) {
		CustomAssertion.assertEquals(source.getSource(), "__API", "Source info should be API");
		CustomAssertion.assertEquals(source.getSeverity(), "informational", "Severity info is not matching");
		CustomAssertion.assertEquals(source.getObjectType(), objectType, "Object type is not matching");
		CustomAssertion.assertEquals(source.getFacility(), facility, "Facility info is not matching");
		CustomAssertion.assertEquals(source.getUserName(), username, "Username info is not matching");
	}
	
	
	
	
	
	public void verifyUploadShareUnshareLogs(ArrayList<String> logs,  String destinationFile, String violationType) {

		String risk = violationType.equals("source_code") ? "SourceCode": "";  
		
		String risklog = "File " + destinationFile + " has risk(s)-"+risk;
		assertTrue(logs.contains(risklog), risklog + "not available in the grepped logs");
		
		String uploadLog = "User uploaded "+ destinationFile +" to All Files";
		assertTrue(logs.contains(uploadLog), uploadLog + "not available in the grepped logs");
		
		String downloadLog = "User downloaded "+ destinationFile ;
		assertTrue(logs.contains(downloadLog), downloadLog + "not available in the grepped logs");
		
		String sharedLog = "User shared " + destinationFile;
		assertTrue(logs.contains(sharedLog), sharedLog + "not available in the grepped logs");
		
		String unsharedLog = "User unshared " + destinationFile;
		assertTrue(logs.contains(unsharedLog), unsharedLog + "not available in the grepped logs");
		
	}

	public void verifyLogs(ArrayList<String> logs, String destinationFile, String violationType) {
		//assertTrue(logs.contains(message), "Expected activity:"+message+" not available in the log");
		
	}
	
	
	public void verifyObjectType(String objectType) {
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			CustomAssertion.assertTrue(source.getObjectType().equals(objectType), "ObjectType is "+objectType, "ObjectType is not "+objectType);
		}
	}
	
	public void verifyActivityType(String activityType) {
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			CustomAssertion.assertTrue(source.getActivityType().equals(activityType), "ActivityType is "+activityType, "ActivityType is not "+activityType);
		}
	}
	
	public void verifySeverityType(String severityType) {
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			CustomAssertion.assertTrue(source.getSeverity().equals(severityType), "severityType is "+severityType, "severityType is not "+severityType);
		}
	}
	
	public void verifyAnonymizedLogs() {
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			CustomAssertion.assertTrue(source.getUserName().startsWith("Anon"), "Anonymized Username:"+source.getUserName(), "Username not anonymized:"+source.getUserName());
			CustomAssertion.assertTrue(source.getUser().startsWith(source.getUserName().toLowerCase()), "Anonymized User"+source.getUser(), "User not anonymized:"+source.getUser());
		}
	}
	
	
	
	
	
	public void verifyUnAnonymizedLogs() {
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			if(source.getUserName()!=null)
				CustomAssertion.assertTrue(!source.getUserName().startsWith("Anon"), "UnAnonymized Username:"+source.getUserName(), "Username not unanonymized:"+source.getUserName());
			
			if(source.getUser()!=null)
				CustomAssertion.assertTrue(!source.getUser().startsWith("anon"), "UnAnonymized User:"+source.getUser(), "User not unanonymized:"+source.getUser());
		}
	}
	
	public void verifyHistoryLogs(String message) {
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			CustomAssertion.assertTrue(source.getMessage().equals(message), "Message Present:"+source.getMessage(), "Message not present:"+source.getMessage());
		}
	}
	
	
	public void verifyDomainAdminLogs() {
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			
			CustomAssertion.assertTrue(source.getUser().contains("secondbeatle"), "DVAC logs available ", "Logs other than DVAC is available"+source.getUser());
		}
	}
	
	
	
	public void verifyActivityLog(ForensicSearchResults logs,  BoxActivity boxActivity) {
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getMessage().equals(boxActivity.getMessage()) && hit.getSource().getSeverity().equals(boxActivity.getSeverity())
					&& hit.getSource().getHost().equals(boxActivity.getHostname()) && hit.getSource().getResourceId().equals(boxActivity.getResourceId())
					) {
				actualSource = hit.getSource();
			}
		}
		
		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		Reporter.log(boxActivity.getExpectedValues(), true);
		Reporter.log("================", true);
//		Reporter.log("Actual Values:", true);
//		Reporter.log("================", true);
//		Reporter.log(getActualValues(actualSource), true);
//		Reporter.log("================", true);
		CustomAssertion.assertTrue(actualSource != null, boxActivity.getMessage() + " is present", boxActivity.getMessage() + " is not present");
		
		//Reporter.log("Actual value:" + actualSource.getSeverity() + "," + "Expected value:"+ boxActivity.getSeverity(), true);
		
		softAssert.assertEquals(actualSource.getSeverity(), boxActivity.getSeverity(), 
							"Expected Severity "+ boxActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		softAssert.assertEquals(actualSource.getObjectType(), boxActivity.getObjectType(), 
							"Expected Object type "+ boxActivity.getObjectType() + " is not matching with actual object type "+actualSource.getObjectType());
		
		softAssert.assertEquals(actualSource.getActivityType(), boxActivity.getActivityType(), 
							"Expected Activity type "+ boxActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		softAssert.assertEquals(actualSource.getUser(), boxActivity.getBoxUser(), 
							"Expected User "+ boxActivity.getBoxUser() + " is not matching with actual user "+actualSource.getUser());
		
		//commented temporarily
		/*
		softAssert.assertEquals(actualSource.getUserName(), boxActivity.getBoxUsername(), 
							"Expected Username "+ boxActivity.getBoxUsername() + " is not matching with actual user "+actualSource.getUserName());
		
		softAssert.assertEquals(Double.valueOf(actualSource.getLatitude()), Double.valueOf(boxActivity.getLatitude()), 
							"Expected latitude "+ boxActivity.getLatitude() + " is not matching with actual latitude "+actualSource.getLatitude());
		
		softAssert.assertEquals(Double.valueOf(actualSource.getLongitude()), Double.valueOf(boxActivity.getLongitude()), 
							"Expected longitude "+ boxActivity.getLongitude() + " is not matching with actual longitude "+actualSource.getLongitude());
		*/
		softAssert.assertEquals(actualSource.getCity(), boxActivity.getCity(), 
							"Expected city "+ boxActivity.getCity() + " is not matching with actual city "+actualSource.getCity());
		
		softAssert.assertEquals(actualSource.getCountry(), boxActivity.getCountry(), 
							"Expected country "+ boxActivity.getCountry() + " is not matching with actual country "+actualSource.getCountry());
		
		softAssert.assertEquals(actualSource.getLocation(), boxActivity.getLocation(), 
							"Expected location "+ boxActivity.getLocation() + " is not matching with actual location "+actualSource.getLocation());
		
		softAssert.assertEquals(actualSource.getResourceId(), boxActivity.getResourceId(), 
							"Expected resourceId "+ boxActivity.getResourceId() + " is not matching with actual resourceId "+actualSource.getResourceId());
		
		//softAssert.assertTrue(actualSource.getCreatedTimestamp().startsWith(boxActivity.getCreatedTime()), 
			//				"Expected Created time "+boxActivity.getCreatedTime() + " is not starting with "+actualSource.getCreatedTimestamp());
		
		//Validate name only if it is not null
		if (boxActivity.getName() != null) {
			
			if (actualSource.getName() != null) {
				softAssert.assertEquals(actualSource.getName(), boxActivity.getName(), 
						"Expected name "+ boxActivity.getName() + " is not matching with actual name "+actualSource.getName());
			} else if(actualSource.getObject() != null) {
				softAssert.assertEquals(actualSource.getObject(), boxActivity.getName(), 
						"Expected object "+ boxActivity.getName() + " is not matching with actual object "+actualSource.getObject());
			}
		}
		
		//Validate parent only if it is not null
		if (boxActivity.getParent() != null) {
			
			if (actualSource.getParent() != null) {
				softAssert.assertEquals(actualSource.getParent(), boxActivity.getParent(), 
						"Expected parent "+ boxActivity.getParent() + " is not matching with actual parent "+actualSource.getParent());
			} else if (actualSource.getParentId() != null) {
				softAssert.assertEquals(actualSource.getParentId(), boxActivity.getParent(), 
						"Expected parent id "+ boxActivity.getParent() + " is not matching with actual parent id "+actualSource.getParentId());
			}
		}
		
		if (boxActivity.getParentId() != null) {
				/*
				softAssert.assertEquals(actualSource.getParentId(), boxActivity.getParentId(), 
						"Expected parent id "+ boxActivity.getParentId() + " is not matching with actual parent id "+actualSource.getParentId());
				*/
				softAssert.assertNotNull(actualSource.getParentId(), "Expected parent id is null");
		}
		
		if (boxActivity.getDocumentType() != null) {
			softAssert.assertEquals(actualSource.getDocumentType(), boxActivity.getDocumentType(), 
					"Expected doctype "+ boxActivity.getDocumentType() + " is not matching with actual doc type "+actualSource.getDocumentType());
		}
		
		if (boxActivity.getFileSize() != 0) {
			//softAssert.assertEquals(String.valueOf(actualSource.getFileSize()), String.valueOf(boxActivity.getFileSize()), 
					//"Expected file size "+ boxActivity.getFileSize() + " is not matching with actual file size "+actualSource.getFileSize());
		}
		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		//Validate each event is recorded within 20 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 20, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
		//CustomAssertion.assertTrue(logs.contains(message), message);
	}
	
	public void verifyCIActivityLog(ForensicSearchResults logs,  OneDriveBusinessActivity expectedActivity) {
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getMessage().equals(expectedActivity.getMessage()) && hit.getSource().getSeverity().equals(expectedActivity.getSeverity())) {
				actualSource = hit.getSource();
			}
		}
		
		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		Reporter.log(expectedActivity.getExpectedValues(), true);
		Reporter.log("================", true);

		CustomAssertion.assertTrue(actualSource != null, expectedActivity.getMessage() + " is present", expectedActivity.getMessage() + " is not present");
		
		softAssert.assertEquals(actualSource.getSeverity(), expectedActivity.getSeverity(), 
							"Expected Severity "+ expectedActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		softAssert.assertEquals(actualSource.getActivityType(), expectedActivity.getActivityType(), 
							"Expected Activity type "+ expectedActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		softAssert.assertEquals(actualSource.getUser().toLowerCase(), expectedActivity.getUser().toLowerCase(), 
							"Expected User "+ expectedActivity.getUser().toLowerCase() + " is not matching with actual user "+actualSource.getUser().toLowerCase());
		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		//Validate each event is recorded within 20 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 20, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
		//CustomAssertion.assertTrue(logs.contains(message), message);
	}
	
	
	public void verifyCIActivityLog(ForensicSearchResults logs,  BoxActivity boxActivity) {
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getMessage().equals(boxActivity.getMessage()) && hit.getSource().getSeverity().equals(boxActivity.getSeverity())) {
				actualSource = hit.getSource();
			}
		}
		
		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		Reporter.log(boxActivity.getExpectedValues(), true);
		Reporter.log("================", true);

		CustomAssertion.assertTrue(actualSource != null, boxActivity.getMessage() + " is present", boxActivity.getMessage() + " is not present");
		
		softAssert.assertEquals(actualSource.getSeverity(), boxActivity.getSeverity(), 
							"Expected Severity "+ boxActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		softAssert.assertEquals(actualSource.getActivityType(), boxActivity.getActivityType(), 
							"Expected Activity type "+ boxActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		softAssert.assertEquals(actualSource.getUser(), boxActivity.getBoxUser(), 
							"Expected User "+ boxActivity.getBoxUser() + " is not matching with actual user "+actualSource.getUser());
		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		//Validate each event is recorded within 20 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 20, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
		//CustomAssertion.assertTrue(logs.contains(message), message);
	}
	
	
	
	public void verifyGmailActivityLog(ForensicSearchResults logs,  GmailActivity expectedActivity) {

		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		Reporter.log(expectedActivity.getExpectedValues(), true);
		Reporter.log("================", true);
		
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		try
		{
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getName().equals(expectedActivity.getName()) && 
					hit.getSource().getSeverity().equals(expectedActivity.getSeverity())) {
				actualSource = hit.getSource();
			}
		}
		}
		catch(Exception e)
		{
			Reporter.log("Expected logs not recieved" ,true);
		}
		

		CustomAssertion.assertTrue(actualSource != null, expectedActivity.getMessage() + " is present", expectedActivity.getMessage() + " is not present");
		
		Reporter.log("Actual value:" + actualSource.getSeverity() + "," + "Expected value:"+ expectedActivity.getSeverity(), true);
		
		
		
		softAssert.assertEquals(actualSource.getSeverity(), expectedActivity.getSeverity(), 
							"Expected Severity "+ expectedActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		softAssert.assertEquals(actualSource.getObjectType(), expectedActivity.getObjectType(), 
							"Expected Object type "+ expectedActivity.getObjectType() + " is not matching with actual object type "+actualSource.getObjectType());
		
		softAssert.assertEquals(actualSource.getActivityType(), expectedActivity.getActivityType(), 
							"Expected Activity type "+ expectedActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		// adding other validation point for gmail
		
		if (expectedActivity.getInternalRecipients() != null) {
			
			ArrayList<String> actualInternalRecipients = new ArrayList<String>(Arrays.asList(actualSource.getInternal_recipients().split(","))); 
			
			softAssert.assertTrue(actualInternalRecipients.containsAll(expectedActivity.getInternalRecipients()), 
					"Expected Internal Recipients "+ StringUtils.join(expectedActivity.getInternalRecipients(), ",") + " is not matching with actual Internal Recipients "+actualSource.getInternalRecipients());
		}

		if(expectedActivity.getExternalRecipients() != null) {
			ArrayList<String> actualExternalRecipients = new ArrayList<String>(Arrays.asList(actualSource.getExternal_Recipients().split(","))); 
			
			softAssert.assertTrue(actualExternalRecipients.containsAll(expectedActivity.getExternalRecipients()), 
					"Expected getExternal_Recipients "+ StringUtils.join(expectedActivity.getExternalRecipients(), ",") + " is not matching with actual getExternal_Recipients "+actualSource.getExternal_Recipients());
		}
		
		if(expectedActivity.getInFolder() != null) {
		ArrayList<String> actualInFolder = new ArrayList<String>(Arrays.asList(actualSource.getInFolder().split(","))); 
		
		softAssert.assertTrue(actualInFolder.containsAll(expectedActivity.getInFolder()), 
				"Expected InFolder "+ expectedActivity.getInFolder() + " is not matching with actual InFolder "+actualSource.getInFolder());
		}
		softAssert.assertEquals(actualSource.getMessage(), expectedActivity.getMessage(), 
				"Expected getMessage "+ expectedActivity.getMessage() + " is not matching with actual getMessage "+actualSource.getMessage());
		
		softAssert.assertEquals(actualSource.getUser(), expectedActivity.getUser(), 
				"Expected User "+ expectedActivity.getUser() + " is not matching with actual User "+actualSource.getUser());
		
		if(expectedActivity.getSender() !=null) {
			softAssert.assertEquals(actualSource.getSender(), expectedActivity.getSender(), 
					"Expected sender "+ expectedActivity.getSender() + " is not matching with actual sender "+actualSource.getSender());
		}
		
		
		
		softAssert.assertEquals(actualSource.getSubject(), expectedActivity.getSubject(), 
				"Expected subject "+ expectedActivity.getSubject()+ " is not matching with actual subject "+actualSource.getSubject());
		
		softAssert.assertEquals(actualSource.getName(), expectedActivity.getName(), 
				"Expected name "+ expectedActivity.getName()+ " is not matching with actual name "+actualSource.getName());
		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		//Validate each event is recorded within 20 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 30, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
		//CustomAssertion.assertTrue(logs.contains(message), message);
	}
	
	
	public void verifyGmailCIActivityLog(ForensicSearchResults logs,  GmailActivity expectedActivity) {

		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		Reporter.log(expectedActivity.getExpectedValues(), true);
		Reporter.log("================", true);
		
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getName().equals(expectedActivity.getName()) && 
					hit.getSource().getSeverity().equals(expectedActivity.getSeverity())) {
				actualSource = hit.getSource();
			}
		}

		CustomAssertion.assertTrue(actualSource != null, expectedActivity.getMessage() + " is present", expectedActivity.getMessage() + " is not present");
		
		softAssert.assertEquals(actualSource.getSeverity(), expectedActivity.getSeverity(), 
							"Expected Severity "+ expectedActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		
		softAssert.assertEquals(actualSource.getActivityType(), expectedActivity.getActivityType(), 
							"Expected Activity type "+ expectedActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		softAssert.assertEquals(actualSource.getMessage(), expectedActivity.getMessage(), 
							"Expected getMessage "+ expectedActivity.getMessage() + " is not matching with actual getMessage "+actualSource.getMessage());
		
//		softAssert.assertEquals(actualSource.getUser(), expectedActivity.getUser(), 
//							"Expected getUser "+ expectedActivity.getUser() + " is not matching with actual getUser "+actualSource.getUser());
		
		softAssert.assertEquals(actualSource.getName(), expectedActivity.getName(), 
							"Expected name "+ expectedActivity.getName()+ " is not matching with actual name "+actualSource.getName());
		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		Reporter.log("Actual Size :" + actualSource.getFileSize()+ " - Expected size:" + expectedActivity.getFilesize(), true);
		
		//Validate each event is recorded within 20 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 30, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
	}
	
	
	
	
	
	
	
	public void verifyOnedriveActivityLog(ForensicSearchResults logs,  OneDriveBusinessActivity expectedActivity) {

		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		expectedActivity.getExpectedValues();
		Reporter.log("================", true);
		
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getMessage().equals(expectedActivity.getMessage()) && 
					hit.getSource().getSeverity().equals(expectedActivity.getSeverity()) && 
					hit.getSource().getResourceId().equals(expectedActivity.getResourceId())
					
					) {
				actualSource = hit.getSource();
			}
		}
		

		CustomAssertion.assertTrue(actualSource != null, expectedActivity.getMessage() + " is present", expectedActivity.getMessage() + " is not present");
		
		Reporter.log("Actual value:" + actualSource.getSeverity() + "," + "Expected value:"+ expectedActivity.getSeverity(), true);
		
		softAssert.assertEquals("Office 365", expectedActivity.getFacility(), 
							"Expected facility "+ expectedActivity.getFacility() + " is not matching with actual facility Office 365");
		
		softAssert.assertEquals(actualSource.getSeverity(), expectedActivity.getSeverity(), 
							"Expected Severity "+ expectedActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		softAssert.assertEquals(actualSource.getObjectType(), expectedActivity.getObjectType(), 
							"Expected Object type "+ expectedActivity.getObjectType() + " is not matching with actual object type "+actualSource.getObjectType());
		
		softAssert.assertEquals(actualSource.getActivityType(), expectedActivity.getActivityType(), 
							"Expected Activity type "+ expectedActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		
		softAssert.assertEquals(actualSource.getUser().toLowerCase(), expectedActivity.getUser().toLowerCase(), 
							"Expected User "+ expectedActivity.getUser().toLowerCase() + " is not matching with actual user "+actualSource.getUser().toLowerCase());

		//commented temporarily
		
//		softAssert.assertEquals(actualSource.getUserName(), expectedActivity.getUsername(), 
//							"Expected Username "+ expectedActivity.getUsername() + " is not matching with actual user "+actualSource.getUserName());
//		
		
		softAssert.assertEquals(actualSource.getResourceId(), expectedActivity.getResourceId(), 
				"Expected resourceId "+ expectedActivity.getResourceId() + " is not matching with actual resourceId "+actualSource.getResourceId());

		
		//Validate name only if it is not null
		if (expectedActivity.getParent() != null) {
			softAssert.assertEquals(actualSource.getOneDriveParent(), expectedActivity.getParent(), 
					"Expected parent "+ expectedActivity.getParent() + " is not matching with actual parent "+actualSource.getOneDriveParent());
		}
		
		
		if (expectedActivity.getFileSize() != 0) {
			softAssert.assertEquals(String.valueOf(actualSource.getFileSize()), String.valueOf(expectedActivity.getFileSize()), 
					"Expected file size "+ expectedActivity.getFileSize() + " is not matching with actual file size "+actualSource.getFileSize());
		}
		
		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		//Validate each event is recorded within 20 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 30, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
		//CustomAssertion.assertTrue(logs.contains(message), message);
	}
	
	
	public void verifySiteActivityLog(ForensicSearchResults logs,  OneDriveBusinessActivity expectedActivity) {

		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		expectedActivity.getExpectedValues();
		Reporter.log("================", true);
		
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getMessage().equals(expectedActivity.getMessage())) {
				actualSource = hit.getSource();
			}
		}
		

		CustomAssertion.assertTrue(actualSource != null, expectedActivity.getMessage() + " is present", expectedActivity.getMessage() + " is not present");
		
		Reporter.log("Actual value:" + actualSource.getSeverity() + "," + "Expected value:"+ expectedActivity.getSeverity(), true);
		
		softAssert.assertEquals("Office 365", expectedActivity.getFacility(), 
							"Expected facility "+ expectedActivity.getFacility() + " is not matching with actual facility Office 365");
		
		softAssert.assertEquals(actualSource.getSeverity(), expectedActivity.getSeverity(), 
							"Expected Severity "+ expectedActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		softAssert.assertEquals(actualSource.getObjectType(), expectedActivity.getObjectType(), 
							"Expected Object type "+ expectedActivity.getObjectType() + " is not matching with actual object type "+actualSource.getObjectType());
		
		softAssert.assertEquals(actualSource.getActivityType(), expectedActivity.getActivityType(), 
							"Expected Activity type "+ expectedActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		softAssert.assertEquals(actualSource.getTitle(), expectedActivity.getTitle(), 
							"Expected Title "+ expectedActivity.getTitle() + " is not matching with actual Title "+actualSource.getTitle());
		
		softAssert.assertEquals(actualSource.getSite(), expectedActivity.getSite(), 
							"Expected Site "+ expectedActivity.getSite() + " is not matching with actual value "+actualSource.getSite());


		softAssert.assertEquals(actualSource.getObject_name(), expectedActivity.getObjectnames(), 
							"Expected object name "+ expectedActivity.getObjectnames() + " is not matching with actual object name "+actualSource.getObject_name());
		
		//Validate only if it is not null
		if (expectedActivity.getParent() != null) {
			softAssert.assertEquals(actualSource.getParent(), expectedActivity.getParent(), 
					"Expected parent "+ expectedActivity.getParent() + " is not matching with actual parent "+actualSource.getParent());
		}
		
		if (expectedActivity.getInstance() != null) {
			softAssert.assertEquals(actualSource.getInstance(), expectedActivity.getInstance(), 
					"Expected instance "+ expectedActivity.getInstance() + " is not matching with actual instance "+actualSource.getInstance());
		}
		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		//Validate each event is recorded within 60 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 60, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
		//CustomAssertion.assertTrue(logs.contains(message), message);
	}
	
	
	public void verifySalesforceActivityLog(ForensicSearchResults logs,  SalesforceActivity expectedActivity) {

		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		expectedActivity.getExpectedValues();
		Reporter.log("================", true);
		
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getMessage().equals(expectedActivity.getMessage()) && 
					hit.getSource().getSeverity().equals(expectedActivity.getSeverity())) {
				actualSource = hit.getSource();
			}
		}
		
		CustomAssertion.assertTrue(actualSource != null, expectedActivity.getMessage() + " is present", expectedActivity.getMessage() + " is not present");
		
		softAssert.assertEquals("Salesforce", expectedActivity.getService(), 
							"Expected service "+ expectedActivity.getService() + " is not matching with actual service");
		
		softAssert.assertEquals(actualSource.getSeverity(), expectedActivity.getSeverity(), 
							"Expected Severity "+ expectedActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		softAssert.assertEquals(actualSource.getObjectType(), expectedActivity.getObjectType(), 
							"Expected Object type "+ expectedActivity.getObjectType() + " is not matching with actual object type "+actualSource.getObjectType());
		
		softAssert.assertEquals(actualSource.getObject_Name(), expectedActivity.getObjectName(), 
							"Expected Object name "+ expectedActivity.getObjectName() + " is not matching with actual object name "+actualSource.getObject_Name());
		
		/*//Commented due to the object id mismatch in content version
		softAssert.assertEquals(actualSource.getObjectId(), expectedActivity.getObjectId(), 
							"Expected Object Id "+ expectedActivity.getObjectId() + " is not matching with actual object id "+actualSource.getObjectId());
		
		
		softAssert.assertEquals(actualSource.getObject_url(), expectedActivity.getObjectUrl(), 
							"Expected Object Url "+ expectedActivity.getObjectUrl() + " is not matching with actual object url "+actualSource.getObject_url());
		*/
		softAssert.assertEquals(actualSource.getInstance(), expectedActivity.getInstance(), 
							"Expected instance "+ expectedActivity.getInstance() + " is not matching with actual instance "+actualSource.getInstance());
		
		softAssert.assertEquals(actualSource.getActivityType(), expectedActivity.getActivityType(), 
							"Expected Activity type "+ expectedActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		softAssert.assertEquals(actualSource.getUser().toLowerCase(), expectedActivity.getUser().toLowerCase(), 
							"Expected User "+ expectedActivity.getUser().toLowerCase() + " is not matching with actual user "+actualSource.getUser().toLowerCase());
		

		//commented temporarily
		softAssert.assertEquals(actualSource.getUserName(), expectedActivity.getUsername(), 
							"Expected Username "+ expectedActivity.getUsername() + " is not matching with actual user "+actualSource.getUserName());


		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		//Validate each event is recorded within 20 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 30, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
		//CustomAssertion.assertTrue(logs.contains(message), message);
		 
	}
	
	
	public void verifySalesforceCIActivityLog(ForensicSearchResults logs,  SalesforceActivity expectedActivity) {

		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		Reporter.log(expectedActivity.getExpectedValues(), true);
		Reporter.log("================", true);
		
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getMessage().equals(expectedActivity.getMessage()) && 
					hit.getSource().getSeverity().equals(expectedActivity.getSeverity())) {
				actualSource = hit.getSource();
			}
		}

		CustomAssertion.assertTrue(actualSource != null, expectedActivity.getMessage() + " is present", expectedActivity.getMessage() + " is not present");
		
		softAssert.assertEquals(actualSource.getSeverity(), expectedActivity.getSeverity(), 
							"Expected Severity "+ expectedActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		softAssert.assertEquals(actualSource.getActivityType(), expectedActivity.getActivityType(), 
							"Expected Activity type "+ expectedActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		softAssert.assertEquals(actualSource.getMessage(), expectedActivity.getMessage(), 
							"Expected getMessage "+ expectedActivity.getMessage() + " is not matching with actual getMessage "+actualSource.getMessage());
//		
//		softAssert.assertEquals(actualSource.getUser(), expectedActivity.getUser(), 
//							"Expected getUser "+ expectedActivity.getUser() + " is not matching with actual getUser "+actualSource.getUser());
//		
		softAssert.assertEquals(actualSource.getName(), expectedActivity.getFileName(), 
							"Expected name "+ expectedActivity.getFileName()+ " is not matching with actual name "+actualSource.getName());
		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		//Validate each event is recorded within 20 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 30, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
	}
	
	
	
	public void verifyOnedriveSiteActivityLog(ForensicSearchResults logs,  OneDriveSiteActivity expectedActivity)  {

		//If the expected activity is not there fail
		Reporter.log("Expected values:", true);
		Reporter.log("================", true);
		Reporter.log(expectedActivity.getExpectedValues(), true);
		Reporter.log("================", true);
		
		softAssert = new SoftAssert();
		Source actualSource = null;
		//Iterate over the logs and find the message is there
		for (Hit hit : logs.getHits().getHits()) {
			if ( hit.getSource().getMessage().equals(expectedActivity.getMessage()) && 
					hit.getSource().getSeverity().equals(expectedActivity.getSeverity()) && 
					hit.getSource().getResourceId().equals(expectedActivity.getResourceId())
					
					) {
				actualSource = hit.getSource();
			}
			
			if (hit.getSource().getActivityType().equals("Share") || hit.getSource().getActivityType().equals("Unshare"))
				if ( hit.getSource().getMessage().contains(expectedActivity.getMessage()) && 
						hit.getSource().getSeverity().equals(expectedActivity.getSeverity()) && 
						hit.getSource().getResourceId().equals(expectedActivity.getResourceId()) ) {
					actualSource = hit.getSource();
				}
			
			
		}
		if (actualSource != null) {
			Reporter.log("Actual activity log values..", true);
			try{
				Reporter.log(MarshallingUtils.marshall(actualSource), true);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			Reporter.log("=====================================================", true);
		}

		CustomAssertion.assertTrue(actualSource != null, expectedActivity.getMessage() + " is present", expectedActivity.getMessage() + " is not present");
		
		Reporter.log("Actual value:" + actualSource.getSeverity() + "," + "Expected value:"+ expectedActivity.getSeverity(), true);
		
		softAssert.assertEquals("Office 365", expectedActivity.getFacility(), 
							"Expected facility "+ expectedActivity.getFacility() + " is not matching with actual facility Office 365");
		
		softAssert.assertEquals(actualSource.getSeverity(), expectedActivity.getSeverity(), 
							"Expected Severity "+ expectedActivity.getSeverity() + " is not matching with actual severity "+actualSource.getSeverity());
		
		softAssert.assertEquals(actualSource.getObjectType(), expectedActivity.getObjectType(), 
							"Expected Object type "+ expectedActivity.getObjectType() + " is not matching with actual object type "+actualSource.getObjectType());
		
		softAssert.assertEquals(actualSource.getActivityType(), expectedActivity.getActivityType(), 
							"Expected Activity type "+ expectedActivity.getActivityType() + " is not matching with actual activity type "+actualSource.getActivityType());
		
		softAssert.assertEquals(actualSource.getUser().toLowerCase(), expectedActivity.getUser().toLowerCase(), 
							"Expected User "+ expectedActivity.getUser().toLowerCase() + " is not matching with actual user "+actualSource.getUser().toLowerCase());
		
		softAssert.assertEquals(actualSource.getResourceId(), expectedActivity.getResourceId(), 
							"Expected resourceId "+ expectedActivity.getResourceId() + " is not matching with actual resourceId "+actualSource.getResourceId());

		softAssert.assertEquals(actualSource.getList(), expectedActivity.getListname(), 
							"Expected list name "+ expectedActivity.getListname() + " is not matching with actual list name "+actualSource.getList());
		
		softAssert.assertEquals(actualSource.getInstance(), expectedActivity.getInstance(), 
							"Expected instance "+ expectedActivity.getInstance() + " is not matching with actual instance "+actualSource.getInstance());
		
		//Validate name only if it is not null
		if (expectedActivity.getParent() != null) {
			softAssert.assertEquals(actualSource.getOneDriveParent(), expectedActivity.getParent(), 
							"Expected parent "+ expectedActivity.getParent() + " is not matching with actual parent "+actualSource.getOneDriveParent());
		}
		
		
		if (expectedActivity.getFileSize() != null) {
			softAssert.assertEquals(String.valueOf(actualSource.getFileSize()), String.valueOf(expectedActivity.getFileSize()), 
							"Expected file size "+ expectedActivity.getFileSize() + " is not matching with actual file size "+actualSource.getFileSize());
		}
		
		
		if (expectedActivity.getCurrentlySharedWith() != null) {
			softAssert.assertTrue(actualSource.getCurrentSharedWith().contains(expectedActivity.getCurrentlySharedWith()), 
					"Expected currently shared with "+ expectedActivity.getCurrentlySharedWith() + " is not matching with actual value "+actualSource.getCurrentSharedWith());
		}
		
		
		double timeDiffInMinutes = DateUtils.timeDiff(actualSource.getInsertedTimestamp(), actualSource.getCreatedTimestamp());
		Reporter.log("Recorded time :" + actualSource.getInsertedTimestamp() + " - Happened Time:" + actualSource.getCreatedTimestamp(), true);
		Reporter.log("Diff between Recorded time and Happened time :" + String.format("%.2f", timeDiffInMinutes) + " min(s)", true);
		
		//Validate each event is recorded within 20 mins from happened time
		softAssert.assertTrue(timeDiffInMinutes <= 20, "Recorded time is greater than happened time by " + timeDiffInMinutes);
		
		softAssert.assertAll();
		//CustomAssertion.assertTrue(logs.contains(message), message);
	}
	
	public String getActualValues(Source source) {
		StringBuffer sb = new StringBuffer();

		Field[] fields = Source.class.getDeclaredFields(); //Get all fields incl. private ones

		for (Field field : fields){

		    try {

		        field.setAccessible(true);
		        String key=field.getName();
		        String value;

		        try{
		            value = (String) field.get(this);
		        } catch (ClassCastException e){
		            value="";
		        }

		        sb.append(key).append(": ").append(value).append("\n");

		    } catch (IllegalArgumentException e) {
		        e.printStackTrace();
		    } catch (SecurityException e) {
		        e.printStackTrace();
		    } catch (IllegalAccessException e) {
		        e.printStackTrace();
		    }
		}
		return sb.toString(); 
		
	}
	public void verifyUser(String userEmail) {
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			CustomAssertion.assertTrue(source.getUser().equals(userEmail), "User is "+userEmail, "User is not "+userEmail);
		}
	}
	
	
	public void verifyExportedDocument(SecurletDocument docs, List<Map<String, String>> allrows, int startIndex) {
		int nextIndex = startIndex;
		for (com.elastica.beatle.securlets.dto.Object document : docs.getDocs()) {
			
			Reporter.log("Verifying the document name ...", true);
			CustomAssertion.assertEquals(document.getName(), allrows.get(nextIndex).get("Name"), 
					document.getName() +" is not matching with the exported list element in index "+nextIndex);
			
			Reporter.log("Verifying the document owner ...", true);
			CustomAssertion.assertEquals(document.getOwnedBy(), allrows.get(nextIndex).get("Owner"), 
					document.getOwnedBy() +" is not matching with the exported list element in index "+nextIndex);
			
			Reporter.log("Verifying the document owner ...", true);
			CustomAssertion.assertEquals(document.getFormat(), allrows.get(nextIndex).get("Format"), 
					document.getFormat() +" is not matching with the exported list element in index "+nextIndex);
			
			Reporter.log("Verifying the document whether internally exposed ...", true);
			String isInternal = document.getIsInternal().booleanValue() ? "Yes" : "No"; 
			CustomAssertion.assertEquals(isInternal, allrows.get(nextIndex).get("Internal"), 
					"IsInternal " + isInternal +" is not matching with the exported list element in index "+nextIndex);
			
			Reporter.log("Verifying the document Parent name ...", true);
			if (document.getParentName()!=null) {
				CustomAssertion.assertEquals(document.getParentName(), allrows.get(nextIndex).get("Parent Name"), 
						"Parent name " + document.getParentName() +" is not matching with the exported list element in index "+nextIndex);
			} else {
				CustomAssertion.assertTrue(allrows.get(nextIndex).get("Parent Name").equals(""), 
						"Parent name is not blank with the exported list element in index "+nextIndex);
			}
			
			if (document.getUrl() != null) {
				Reporter.log("Verifying the document url ...", true);
				CustomAssertion.assertEquals(document.getUrl(), allrows.get(nextIndex).get("URL"), 
						"URL " + document.getUrl() +" is not matching with the expoerted list element in index "+nextIndex);
			}
			
			Reporter.log("Verifying the document public exposures ...", true);
			String isPublic = document.getExposures().getPublic().booleanValue() ? "Yes" : "No"; 
			String csvPublic = allrows.get(nextIndex).get("Public Exposure").trim().equals("") || allrows.get(nextIndex).get("Public Exposure").trim().equals("No") ? "No" : "Yes";
			CustomAssertion.assertEquals(isPublic, csvPublic, "Public Exposure " + document.getExposures().getPublic() +" is not matching with the expoerted list element in index "+nextIndex);
			
			Reporter.log("Verifying the document company exposures ...", true);
			String allInternal = document.getExposures().getAllInternal().booleanValue() ? "Yes" : "No"; 
			String csvInternal = allrows.get(nextIndex).get("All Company Exposure").trim().equals("") || allrows.get(nextIndex).get("All Company Exposure").trim().equals("No") ? "No" : "Yes";
			CustomAssertion.assertEquals(csvInternal, allInternal, "Internal Exposure " + document.getExposures().getAllInternal() +" is not matching with the expoerted list element in index "+nextIndex);
			
			Reporter.log("Verifying the internal collaborators", true);
			String allInternalCollabs = allrows.get(nextIndex).get("Internal Collaborators").trim();
			Reporter.log(allInternalCollabs + " against " + document.getExposures().getInternal(), true);
			
			if (allInternalCollabs.length() > 0 || document.getExposures().getInternal().size() > 0) {
				List<String> allIntCollabs = Arrays.asList(StringUtils.split(allInternalCollabs, ","));
				CustomAssertion.assertEquals(allIntCollabs.size(), document.getExposures().getInternal().size(), "Internal collaborator list size don't match");
				CustomAssertion.assertTrue(document.getExposures().getInternal().containsAll(allIntCollabs), 
							"Internal Collaborators " + allIntCollabs +" is matching with the exported list "+document.getExposures().getInternal(),
							"Internal Collaborators " + allIntCollabs +" is not matching with the exported list "+document.getExposures().getInternal());
			}
			
			Reporter.log("Verifying the external collaborators", true);
			String allExternalCollabs = allrows.get(nextIndex).get("External Collaborators").trim();
			Reporter.log(allExternalCollabs + " against " + document.getExposures().getExternal(), true);
			
			if (allExternalCollabs.length() > 0 || document.getExposures().getExternal().size() > 0) {
				List<String> allExtCollabs = Arrays.asList(StringUtils.split(allExternalCollabs, ","));
				CustomAssertion.assertEquals(allExtCollabs.size(), document.getExposures().getExternal().size(), "External collaborator list size don't match");
				CustomAssertion.assertTrue(document.getExposures().getExternal().containsAll(allExtCollabs), 
							"External Collaborators " + allExtCollabs +" is matching with the exported list "+document.getExposures().getExternal(),
							"External Collaborators " + allExtCollabs +" is not matching with the exported list "+document.getExposures().getExternal());
			}
			
			
			//Verifying the DCI part of the document
			Reporter.log("Verifying the PII vulnerability", true);
			int vkpii = allrows.get(nextIndex).get("PII Vulnerability").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("PII Vulnerability"));
			CustomAssertion.assertEquals(document.getContentChecks().getVkPii(), vkpii, 
													"PII vulnerability list size don't match");
			
			int vkpci = allrows.get(nextIndex).get("PCI Vulnerability").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("PCI Vulnerability"));
			Reporter.log("Verifying the PCI vulnerability", true);
			CustomAssertion.assertEquals(document.getContentChecks().getVkPci(), vkpci, 
													"PCI vulnerability list size don't match");
			
			int vkhipaa = allrows.get(nextIndex).get("HIPAA Vulnerability").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("HIPAA Vulnerability"));
			Reporter.log("Verifying the HIPAA vulnerability", true);
			CustomAssertion.assertEquals(document.getContentChecks().getVkHipaa(), vkhipaa, 
													"HIPAA vulnerability list size don't match");
			
			
			Reporter.log("Verifying the Source code vulnerability", true);
			int vkscode = allrows.get(nextIndex).get("Source Code Vulnerability").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("Source Code Vulnerability"));
			CustomAssertion.assertEquals(document.getContentChecks().getVkSourceCode(), vkscode,  
													"Source Code vulnerability list size don't match");
			
			int vkvba = allrows.get(nextIndex).get("VBA Vulnerability").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("VBA Vulnerability"));
			Reporter.log("Verifying the VBA vulnerability", true);
			CustomAssertion.assertEquals(document.getContentChecks().getVkVbaMacros(), vkvba, 
													"VBA vulnerability list size don't match");
			
			Reporter.log("Verifying the Encryption vulnerability", true);
			int vkenc = allrows.get(nextIndex).get("Encryption").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("Encryption"));
			CustomAssertion.assertEquals(document.getContentChecks().getVkEncryption(), vkenc, 
													"Encryption list size don't match");
			
			Reporter.log("Verifying the Virus vulnerability", true);
			int vkvirus = allrows.get(nextIndex).get("Virus Vulnerability").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("Virus Vulnerability"));
			CustomAssertion.assertEquals(document.getContentChecks().getVkVirus(), vkvirus, 
													"Virus list size don't match");
			
			
			Reporter.log("Verifying the DLP Vulnerability", true);
			int vkdlp = allrows.get(nextIndex).get("DLP Vulnerability").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("DLP Vulnerability"));
			CustomAssertion.assertEquals(document.getContentChecks().getVkDlp(), vkdlp, 
													"DLP Vulnerability list size don't match");
			
			Reporter.log("Verifying the GLBA Vulnerability", true);
			int vkglba = allrows.get(nextIndex).get("GLBA Vulnerability").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("GLBA Vulnerability"));
			CustomAssertion.assertEquals(document.getContentChecks().getVkGlba(), vkglba, 
													"GLBA Vulnerability list size don't match");
			
			Reporter.log("Verifying the FERPA Vulnerability", true);
			int vkferpa = allrows.get(nextIndex).get("FERPA Vulnerability").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("FERPA Vulnerability"));
			CustomAssertion.assertEquals(document.getContentChecks().getVkFerpa(), vkferpa, 
													"FERPA Vulnerability list size don't match");
			
			Reporter.log("Verifying the violations ...", true);
			String violations = document.getContentChecks().getViolations().booleanValue() ? "Yes" : "No"; 
			CustomAssertion.assertEquals(violations, allrows.get(nextIndex).get("Violations"), 
							"Violations " + violations +" is not matching with the exported list element in index "+nextIndex);
			
			
			Reporter.log("Verifying the Content IQ Vulnerability ", true);
			CustomAssertion.assertEquals(document.getContentChecks().getVkContentIqViolations().size(), allrows.get(nextIndex).get("Content IQ Vulnerability").length(), 
													"Content IQ Vulnerability list size don't match");
			
			nextIndex++;
		}
	}
	
	
	public void verifyExportedUsers(SecurletDocument docs, List<Map<String, String>> allrows, int startIndex) {
		int nextIndex = startIndex;
		for (com.elastica.beatle.securlets.dto.Object document : docs.getObjects()) {
			
			Reporter.log("Verifying the user email ...", true);
			CustomAssertion.assertEquals(document.getEmail(), allrows.get(nextIndex).get("Email"), 
					document.getEmail() +" is not matching with the exported list element in index "+nextIndex);
			
			Reporter.log("Verifying the user name ...", true);
			CustomAssertion.assertEquals(document.getUsername(), allrows.get(nextIndex).get("Username"), 
					document.getUsername() +" is not matching with the exported list element in index "+nextIndex);
			
			Reporter.log("Verifying the user internally exposed ...", true);
			String isInternal = document.getIsInternal() ? "Yes" : "No"; 
			CustomAssertion.assertEquals(isInternal, allrows.get(nextIndex).get("Internal"), 
					"IsInternal " + isInternal +" is not matching with the exported list element in index "+nextIndex);
			
			Reporter.log("Verifying the groups", true);
			String allgroups = allrows.get(nextIndex).get("Groups").trim();
			Reporter.log(allgroups + " against " + document.getGroups(), true);
			
			if (allgroups.length() > 0 || document.getGroups().size() > 0) {
				List<String> allIntGrps = Arrays.asList(StringUtils.split(allgroups, ","));
				CustomAssertion.assertEquals(allIntGrps.size(), document.getGroups().size(), "Groups list size don't match");
				CustomAssertion.assertTrue(document.getGroups().containsAll(allIntGrps), 
							"groups " + allIntGrps +" is matching with the exported list "+document.getGroups(),
							"groups " + allIntGrps +" is not matching with the exported list "+document.getGroups());
			}
			
			Reporter.log("Verifying the exposed documents ...", true);
			
			int totalDocsExposed =  document.getDocsExposed() + document.getDocsExposedNonActionable();
			
			int totalCount = allrows.get(nextIndex).get("Docs Exposed").trim() == "" ? 0 : Integer.parseInt(allrows.get(nextIndex).get("Docs Exposed"));
			CustomAssertion.assertEquals(totalCount, totalDocsExposed, 
					document.getUsername() +" is not matching with the exported list element in index "+nextIndex);
			
			nextIndex++;
		}
	}
	
}
