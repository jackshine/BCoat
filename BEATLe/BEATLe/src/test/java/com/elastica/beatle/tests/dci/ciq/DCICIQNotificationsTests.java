package com.elastica.beatle.tests.dci.ciq;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.Priority;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCICommonTest;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.logger.Logger;

public class DCICIQNotificationsTests extends DCICommonTest{

	public String ciqPDT = "DCI_USALPN";
	public String ciqPDT2 = "DCI_ABAN";
	public String dictName = "DCI_DICT";
	public List<String> keywords=Arrays.asList("Keyword_1", "Keyword_2", "Keyword_3");
	public List<String> keywords1=Arrays.asList("Keyword_4", "Keyword_5", "Keyword_6", "Keyword_7");
	public String contentIQProfileId = null;
	public String dictionaryId = null;
	
	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	
	/**********************************************TEST METHODS***********************************************/
	
	@Priority(1)
	@Test(groups ={"Notification"})
	public void testClearNotifications() throws Exception {
		dciFunctions = new DCIFunctions();
		
		Logger.info(
				"************************************ Starting the checking of notifications clearing when CIQ profile changes"
				+ " after clicking Clear notifications link is in progress");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Click on clear notifications link and verify there are no unseen notifications");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output**********************");
		Logger.info("Count of unseen notifications should be zero");
		Logger.info("*****************************************************");
		
		HttpResponse notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		String notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countBefore = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		
		if(countBefore==0){
			Logger.info("There are notifications to be cleared, so create a CIQ profile to generate a notification");
			dciFunctions.createCIQPredefinedTerms(restClient, suiteData, dciFunctions.predefinedTermsCIQProfileText(ciqPDT2), 
					dciFunctions.predefinedTermsCIQProfileName(ciqPDT2), dciFunctions.predefinedTermsCIQProfileDescription(ciqPDT2), "high", 1, true, 1, false);
		}
		
		dciFunctions.clearNotificationsList(restClient, suiteData);
		
		notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countAfter = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		
		Logger.info("****************Actual Output************************");
		Logger.info("Notification Unseen count before clicking clear notifications link:"+countBefore);
		Logger.info("Notification Unseen count after clicking clear notifications link:"+countAfter);
		Logger.info("*****************************************************");
		
		Assert.assertEquals(countAfter,0,"Notification unseen count is mismatching Expected:0 but Actual:" +countAfter);
		
		Logger.info(
				"************************************ Completed the checking of notifications clearing when CIQ profile changes"
				+ " after clicking Clear notifications link");
		
	}
	
	@Priority(2)
	@Test(groups ={"Notification"})
	public void testCreateCIQProfileGenerateNotifications() throws Exception {
		dciFunctions = new DCIFunctions();
		
		Logger.info(
				"************************************ Starting the checking of notifications generating when CIQ profile is added"
				+ " is in progress");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Create CIQ profile and verify new notification is generated");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output**********************");
		Logger.info("Count of unseen notifications should increase once new profile is created");
		Logger.info("*****************************************************");
		
		HttpResponse notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		String notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countBefore = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		
		contentIQProfileId = dciFunctions.createCIQPredefinedTerms(restClient, suiteData, dciFunctions.predefinedTermsCIQProfileText(ciqPDT), 
				dciFunctions.predefinedTermsCIQProfileName(ciqPDT), dciFunctions.predefinedTermsCIQProfileDescription(ciqPDT), "high", 1, false, 0, false);
	
		notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countAfter = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		String validationMessage = dciFunctions.verifyNotificationResponse(suiteData, notificationResponseBody);
		
		Logger.info("****************Actual Output************************");
		Logger.info("Notification Unseen count before creating new CIQ profile:"+countBefore);
		Logger.info("Notification Unseen count after creating new CIQ profile:"+countAfter);
		Logger.info("*****************************************************");
		
		Assert.assertEquals(countAfter,countBefore+1,"Notification unseen count is not increasing Expected:"+(countBefore+1)+" but Actual:" +countAfter);
		Assert.assertEquals(validationMessage,"","Notification details json validation is failing with "+validationMessage);
	
		Logger.info(
				"************************************ Completed the checking of notifications generating when CIQ profile is added");
		
	}
	
	@Priority(3)
	@Test(groups ={"Notification"})
	public void testUpdateCIQProfileGenerateNotifications() throws Exception {
		dciFunctions = new DCIFunctions();
		
		Logger.info(
				"************************************ Starting the checking of notifications generating when CIQ profile is updated"
				+ " is in progress");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Update CIQ profile and verify new notification is generated");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output**********************");
		Logger.info("Count of unseen notifications should increase once new profile is updated");
		Logger.info("*****************************************************");
		
		HttpResponse notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		String notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countBefore = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		
		dciFunctions.updateCIQPredefinedTerm(restClient, suiteData, contentIQProfileId, dciFunctions.predefinedTermsCIQProfileText(ciqPDT), 
				dciFunctions.predefinedTermsCIQProfileName(ciqPDT), dciFunctions.predefinedTermsCIQProfileDescription(ciqPDT), "high", 1, true, 1, false);
	
		notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countAfter = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		String validationMessage = dciFunctions.verifyNotificationResponse(suiteData, notificationResponseBody);
		
		Logger.info("****************Actual Output************************");
		Logger.info("Notification Unseen count before updating CIQ profile:"+countBefore);
		Logger.info("Notification Unseen count after updating CIQ profile:"+countAfter);
		Logger.info("*****************************************************");
		
		Assert.assertEquals(countAfter,countBefore+1,"Notification unseen count is not increasing Expected:"+(countBefore+1)+" but Actual:" +countAfter);
		Assert.assertEquals(validationMessage,"","Notification details json validation is failing with "+validationMessage);
	
		Logger.info(
				"************************************ Completed the checking of notifications generating when CIQ profile is updated");
		
	}
	
	@Priority(4)
	@Test(groups ={"Notification"})
	public void testDeleteCIQProfileGenerateNotifications() throws Exception {
		dciFunctions = new DCIFunctions();
		
		Logger.info(
				"************************************ Starting the checking of notifications generating when CIQ profile is deleted"
				+ " is in progress");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Delete CIQ profile and verify new notification is generated");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output**********************");
		Logger.info("Count of unseen notifications should increase once new profile is deleted");
		Logger.info("*****************************************************");
		
		HttpResponse notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		String notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countBefore = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		
		dciFunctions.deleteContentIQProfileById(restClient, suiteData, contentIQProfileId);dciFunctions.waitForSeconds(5);
	
		notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countAfter = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		String validationMessage = dciFunctions.verifyNotificationResponse(suiteData, notificationResponseBody);
		
		Logger.info("****************Actual Output************************");
		Logger.info("Notification Unseen count before deleting CIQ profile:"+countBefore);
		Logger.info("Notification Unseen count after deleting CIQ profile:"+countAfter);
		Logger.info("*****************************************************");
		
		Assert.assertEquals(countAfter,countBefore+1,"Notification unseen count is not increasing Expected:"+(countBefore+1)+" but Actual:" +countAfter);
		Assert.assertEquals(validationMessage,"","Notification details json validation is failing with "+validationMessage);
	
		Logger.info(
				"************************************ Completed the checking of notifications generating when CIQ profile is deleted");
		
	}
	
	@Priority(5)
	@Test(groups ={"Notification"})
	public void testCreateDictionaryProfileGenerateNotifications() throws Exception {
		dciFunctions = new DCIFunctions();
		
		Logger.info(
				"************************************ Starting the checking of notifications generating when Dictionary profile is added"
				+ " is in progress");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Create CIQ profile and verify new notification is not generated");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output**********************");
		Logger.info("Count of unseen notifications should not increase once new Dictionary is created");
		Logger.info("*****************************************************");
		
		HttpResponse notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		String notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countBefore = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		
		dciFunctions.createDictionary(restClient, suiteData, dictName,
				dictName+" Description", null, keywords, dciFunctions.getCookieHeaders(suiteData));
		HttpResponse response = dciFunctions.listDictionary(restClient, dciFunctions.getCookieHeaders(suiteData),
				suiteData.getScheme(), suiteData.getHost());
		dictionaryId = dciFunctions.getDictionaryId(response, dictName);
		
		notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countAfter = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		String validationMessage = dciFunctions.verifyNotificationResponse(suiteData, notificationResponseBody);
		
		Logger.info("****************Actual Output************************");
		Logger.info("Notification Unseen count before creating new Dictionary profile:"+countBefore);
		Logger.info("Notification Unseen count after creating new Dictionary profile:"+countAfter);
		Logger.info("*****************************************************");
		
		Assert.assertEquals(countAfter,countBefore,"Notification unseen count is increasing Expected:"+(countBefore)+" but Actual:" +countAfter);
		Assert.assertEquals(validationMessage,"","Notification details json validation is failing with "+validationMessage);
	
		Logger.info(
				"************************************ Completed the checking of notifications generating when Dictionary profile is added");
		
	}
	
	@Priority(6)
	@Test(groups ={"Notification"})
	public void testUpdateDictionaryProfileGenerateNotifications() throws Exception {
		dciFunctions = new DCIFunctions();
		Logger.info(
				"************************************ Starting the checking of notifications generating when Dictionary profile is updated"
				+ " is in progress");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Update Dictionary profile and verify new notification is generated");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output**********************");
		Logger.info("Count of unseen notifications should increase once new Dictionary is updated");
		Logger.info("*****************************************************");
		
		HttpResponse notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		String notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countBefore = dciFunctions.getNotificationsUnseen(notificationResponseBody); 

		dciFunctions.updateDictionary(restClient, suiteData, dictionaryId, dictName,
				dictName+" Description", null, keywords1, dciFunctions.getCookieHeaders(suiteData));
		
		notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countAfter = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		String validationMessage = dciFunctions.verifyNotificationResponse(suiteData, notificationResponseBody);
		
		Logger.info("****************Actual Output************************");
		Logger.info("Notification Unseen count before updating Dictionary profile:"+countBefore);
		Logger.info("Notification Unseen count after updating Dictionary profile:"+countAfter);
		Logger.info("*****************************************************");
		
		Assert.assertEquals(countAfter,countBefore,"Notification unseen count is increasing Expected:"+(countBefore)+" but Actual:" +countAfter);
		Assert.assertEquals(validationMessage,"","Notification details json validation is failing with "+validationMessage);
	
		Logger.info(
				"************************************ Completed the checking of notifications generating when Dictionary profile is updated");
		
	}
	
	@Priority(7)
	@Test(groups ={"Notification"})
	public void testDeleteDictionaryProfileGenerateNotifications() throws Exception {
		dciFunctions = new DCIFunctions();
		
		Logger.info(
				"************************************ Starting the checking of notifications generating when Dictionary profile is deleted"
				+ " is in progress");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Delete Dictionary profile and verify new notification is not generated");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output**********************");
		Logger.info("Count of unseen notifications should not increase once new Dictionary is deleted");
		Logger.info("*****************************************************");
		
		HttpResponse notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		String notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countBefore = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		
		dciFunctions.deleteDictionary(restClient, suiteData, dictionaryId);
	
		notificationResponse = dciFunctions.getNotificationsList(restClient, suiteData);
		notificationResponseBody = ClientUtil.getResponseBody(notificationResponse);
		int countAfter = dciFunctions.getNotificationsUnseen(notificationResponseBody); 
		String validationMessage = dciFunctions.verifyNotificationResponse(suiteData, notificationResponseBody);
		
		Logger.info("****************Actual Output************************");
		Logger.info("Notification Unseen count before deleting Dictionary profile:"+countBefore);
		Logger.info("Notification Unseen count after deleting Dictionary profile:"+countAfter);
		Logger.info("*****************************************************");
		
		Assert.assertEquals(countAfter,countBefore,"Notification unseen count is increasing Expected:"+(countBefore)+" but Actual:" +countAfter);
		Assert.assertEquals(validationMessage,"","Notification details json validation is failing with "+validationMessage);
	
		Logger.info(
				"************************************ Completed the checking of notifications generating when Dictionary profile is deleted");
		
	}
	
	/**********************************************TEST METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/
	/**
	 * Delete content iq profile
	 * @throws Exception 
	 */
	@BeforeClass(groups ={"Notification"})
	public void deleteContentIqProfileBeforeTestStarts() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	/**
	 * Delete content iq profile
	 * @throws Exception 
	 */
	@AfterClass(groups ={"Notification"})
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}
	/**********************************************BEFORE/AFTER CLASS*****************************************/
}