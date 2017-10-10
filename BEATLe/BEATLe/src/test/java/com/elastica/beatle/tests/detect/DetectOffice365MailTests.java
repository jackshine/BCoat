package com.elastica.beatle.tests.detect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.logger.Logger;
import com.universal.common.Office365MailActivities;

public class DetectOffice365MailTests extends DetectUtils {

	String userName;
	Office365MailActivities objMail;
	AttributeBean attributeBean ;

	@BeforeClass(alwaysRun=true)
	public void initmail() throws Exception {
		this.userName = suiteData.getSaasAppUsername();
		Logger.info("all printed above..");
		objMail = new Office365MailActivities( suiteData.getSaasAppUsername(),suiteData.getSaasAppPassword()); 
		Logger.info("Office365mail api initialized");
		
		String[] ioi_Codes  = {IOI_Code.ANOMALOUSLY_FREQUENT_DELETES.toString(), IOI_Code.ANOMALOUSLY_FREQUENT_SESSIONS.toString(), IOI_Code.ANOMALOUSLY_FREQUENT_SHARING.toString(),
				IOI_Code.ANOMALOUSLY_LARGE_DELETES.toString(), IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString(), IOI_Code.ANOMALOUSLY_LARGE_SHARING.toString(), IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString()
				,IOI_Code.ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC.toString()};
		
		
		
		attributeBean =  new  AttributeBean(10, 4,true);
		attributeBean.setEnabled(true);
		
		
		HttpResponse resp = getDetectAttributes();
		String 	 responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		
		boolean enabled = attributeBean.isEnabled();
		updateBBIAttributes1(enabled, attributeBean, getResponseArray, ioi_Codes);
		Logger.info("updated preferences ::::::     " + attributeBean.toString());
		
		
	}
	
	
	@DataProvider()
	public Object[][] o365Dataprovider() throws Exception, InterruptedException {
		String senderEmail = suiteData.getUsername();
		String ioiCode = "ANOMALOUSLY_FREQUENT_SESSIONS";
		clearIncidentAndProfile(senderEmail, ioiCode);
		Assert.assertFalse(isIncidentListed(senderEmail, ioiCode), "Incident CleanUP failed!!");
		//verifyNoProfile(senderEmail, ioiCode);
		
		List<String> messageList = new ArrayList<>();
		String messageBody = "Please find the status of email DETECT testing";
		
		Logger.info("Going to Send Mails - To Create the Profile.");
		for( int idx = 1; idx <= 40; idx++ ) {
			Logger.info("Going to Send " + idx + "th Mail.");
			
			String uniqueId = new String (UUID.randomUUID().toString());
			String subject = new String("Detect testing " + uniqueId);
			String recipientEmail = "testuser3@securleto365beatle.com";
			
			boolean status =	objMail.sendMail(recipientEmail, subject, messageBody, "text", null, true);
			Logger.info("Sending email "+messageBody+" as body in saas app is completed");
			Assert.assertTrue(status, "Email Sent Failed!!");
			messageList.add("User sent an email to testuser3@securleto365beatle.com with subject \""+subject+"\"");
			//"User sent an email to testuser3@securleto365beatle.com with subject \"+subject+"\"";
//			status = objMail.forwardMail( subject,recipientEmail);
//			Logger.info("Forward email "+subject+" as subject in saas app is completed");
//			Assert.assertTrue(status, "Forward email Failed!!");
			
			
			subject= subject+"Saved to daraft";
			status = objMail.saveToDraft(subject, messageBody);
			Logger.info("SaveToDraft email "+subject+" as subject in saas app is completed");
			Assert.assertTrue(status, "Darft saving failed!");
			
			
			
			int waitInterval = getRandomInterval(idx);
			Logger.info("Going to wait for " + waitInterval + " seconds before sending next mail!!");
			Thread.sleep(waitInterval * 1000);
		}
		if("eoe".endsWith(suiteData.getEnvironmentName())){
			
		//verifyProfile(senderEmail, ioiCode);
		}
		verifyActivityCount(suiteData.getUsername(), messageList);
		
		Logger.info("Going to Send Mails - To Create the Incident.");
		for( int idx = 1; idx <= 60; idx++ ) {
			Logger.info("Going to Send " + idx + "th Mail.");
			
			String uniqueId = new String (UUID.randomUUID().toString());
			String subject = new String("Detect testing " + uniqueId);
			String recipientEmail = "testuser3@securleto365beatle.com";
			
			boolean status =	objMail.sendMail(recipientEmail, subject, messageBody, "text", null, true);
			Logger.info("Sending email "+messageBody+" as body in saas app is completed");
			Assert.assertTrue(status, "Email Sent Failed!!");
			
//			status = objMail.forwardMail( subject,recipientEmail);
//			Logger.info("Forward email "+subject+" as subject in saas app is completed");
//			Assert.assertTrue(status, "Forward email Failed!!");
			
			
			subject= subject+"Saved to daraft";
			status = objMail.saveToDraft(subject, messageBody);
			Logger.info("SaveToDraft email "+subject+" as subject in saas app is completed");
			Assert.assertTrue(status, "Darft saving failed!");
			
			Logger.info("Going to wait for 1 seconds before sending next mail!!");
			Thread.sleep(1 * 3 * 1000);
		}
		return new Object[][]{
	
		{ ioiCode , "send email"},
		
			
		{ioiCode , "save email" },
		
			};
		}
		
	
	
	
	
	

	@Test(dataProvider="o365Dataprovider",  description="Send email and generate Frequent Session BBI profile")
	public void sendMail_frequent_session_BBI(Method method, String ioiCode, String mailType) throws Exception {
		
		Logger.info("Execution Completed - Test Case Name: " + method.getName());
	
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioiCode);
		validateIncidents(ioiCode);
		
		Logger.info("Execution Completed - Test Case Name: " + method.getName());
	}



	public int getRandomInterval(int idx) {
		int highEnd;
		int lowEnd;
		Random rand = new Random();
		
		if (idx <= 11) {
			highEnd = 28;
			lowEnd = 20;
		} else if (idx <= 22) {
			highEnd = 30;
			lowEnd = 20;
		} else if (idx <= 33) {
			highEnd = 40;
			lowEnd = 30;
		} else {
			highEnd = 40;
			lowEnd = 30;
		}
		
		int interval = rand.nextInt((highEnd - lowEnd) + 1) + lowEnd;
		
		return interval;
	}
	
}
