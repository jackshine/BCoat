package com.elastica.beatle.tests.detect;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FilenameUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.tests.infra.InfraConstants;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import com.universal.common.GoogleMailServices;

public class DetectGMailTests extends DetectUtils {

	GoogleMailServices gmailApi;
	String userName;
	String clientId;
	String clientSecret;
	String refreshToken;

	@BeforeClass(alwaysRun=true)
	public void initGmail() throws Exception {
		this.userName = suiteData.getSaasAppUsername();
		this.clientId 		= suiteData.getSaasAppClientId();	// getRegressionSpecificSuitParameters("saasAppClientId");
		this.clientSecret 	= suiteData.getSaasAppClientSecret();	// getRegressionSpecificSuitParameters("saasAppClientSecret");
		this.refreshToken 	= suiteData.getSaasAppToken();	// getRegressionSpecificSuitParameters("saasAppToken");
		Reporter.log(clientId, true);
		Reporter.log(clientSecret, true);
		Reporter.log(refreshToken, true);
		Reporter.log("all printed above..", true);
		this.gmailApi 		= new GoogleMailServices(clientId, clientSecret, refreshToken);
		Reporter.log("Gmail api initialized", true);
	}
	

	@Test(description="Send email and generate Frequent Session BBI profile", groups={PRIORITY_1, ALL})
	public void sendMail_frequent_session_BBI(Method method) throws Exception {
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
		String senderEmail = "admin@detectbbibeatle.com";
		String ioiCode = "ANOMALOUSLY_FREQUENT_SESSIONS";
		
		clearIncidentAndProfile(senderEmail, ioiCode);
		Assert.assertFalse(isIncidentListed(senderEmail, ioiCode), "Incident CleanUP failed!!");
		//verifyNoProfile(senderEmail, ioiCode);
		
		ArrayList<String> toList = new ArrayList<String>();
		toList.add("detectbbi_10confidence@detectbbibeatle.com");
		ArrayList<String> ccList = null;
		ArrayList<String> bccList = null;
		
		
		String messageBody = "Please find the status of gmail DETECT testing";
		
		Reporter.log("Going to Send Mails - To Create the Profile.", true);
		for( int idx = 1; idx <= 33; idx++ ) {
			Reporter.log("Going to Send " + idx + "th Mail.", true);
			
			String uniqueId = new String (UUID.randomUUID().toString());
			String subject = new String("Detect testing " + uniqueId);
			
			boolean status = gmailApi.sendPlainMessage(toList, ccList, bccList, subject, messageBody);
			Assert.assertTrue(status, "Email Sent Failed!!");
			
			int waitInterval = getRandomInterval(idx);
			Reporter.log("Going to wait for " + waitInterval + " seconds before sending next mail!!", true);
			Thread.sleep(waitInterval * 1000);
		}
		
		verifyProfile(senderEmail, ioiCode);
		
		Reporter.log("Going to Send Mails - To Create the Incident.", true);
		for( int idx = 1; idx <= 30; idx++ ) {
			Reporter.log("Going to Send " + idx + "th Mail.", true);
			
			String uniqueId = new String (UUID.randomUUID().toString());
			String subject = new String("Detect testing " + uniqueId);
			
			boolean status = gmailApi.sendPlainMessage(toList, ccList, bccList, subject, messageBody);
			Assert.assertTrue(status, "Email Sent Failed!!");
			
			Reporter.log("Going to wait for 1 seconds before sending next mail!!", true);
			Thread.sleep(1 * 1 * 1000);
		}
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioiCode);
		verifyStateIncidents(senderEmail, expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}

	public int getRandomInterval(int idx) {
		int highEnd;
		int lowEnd;
		Random rand = new Random();
		
		if (idx <= 11) {
			highEnd = 48;
			lowEnd = 42;
		} else if (idx <= 22) {
			highEnd = 50;
			lowEnd = 40;
		} else if (idx <= 33) {
			highEnd = 52;
			lowEnd = 42;
		} else {
			highEnd = 54;
			lowEnd = 40;
		}
		
		int interval = rand.nextInt((highEnd - lowEnd) + 1) + lowEnd;
		
		return interval;
	}
	
}
