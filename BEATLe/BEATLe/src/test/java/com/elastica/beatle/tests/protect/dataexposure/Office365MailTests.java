package com.elastica.beatle.tests.protect.dataexposure;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.O365DataProvider;
import com.universal.common.Office365MailActivities;

/**
 * This test script verifies Protect related Office365 Email cases 
 * 
 * @author Shri
 *
 */
public class Office365MailTests extends ProtectInitializeTests {

	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Office365MailActivities mailSender;
	DCIFunctions dciFunctions = new DCIFunctions();
	String ciqDictProfileName="DCI_DIS"; String ciqTermsProfileName="DCI_USALPN";
	String ciqDictProfileType="Diseases";String ciqTermsProfileType="US License Plate Numbers";
	String ciqDictFileName = "Diseases.txt"; String ciqTermsFileName = "US_License_Plate_Number.txt";


	/**
	 * Method to initialize test case
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
		mailSender = new Office365MailActivities(suiteData.getUsername(), suiteData.getPassword());
	}

	/**
	 * This method creates policy and sends email to external/ internal user
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void createPolicyAndSendEmail(List<String[]> list) throws Exception {
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		String o365MailType = null;
		File file = null;
		while (iterator.hasNext()) {
			try {

				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				file = createDynamicFile(policyBean);
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);

				// Send Email
				o365MailType = "office365mailbody";
				if (policyBean.getRemediationActivity().contains(ProtectTestConstants.DELETE_EMAIL_ATTACHMENT))
					o365MailType = suiteData.getSaasApp();
				if (o365MailType.contains("sanity") && o365MailType.contains("office365mailattachment"))
					o365MailType = "office365mailattachment";
					
				
				
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL))
					protectFunctions.sendMailWithAttachment(o365MailType, suiteData.getSaasAppUsername(),
							suiteData.getSaasAppPassword(), file, ProtectTestConstants.EXTERNAL_USER_O365);
				else
					protectFunctions.sendMailWithAttachment(o365MailType, suiteData.getSaasAppUsername(),
							suiteData.getSaasAppPassword(), file, ProtectTestConstants.INTERNAL_PROTECT_O365BEATLE);
				
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}
	   protectFunctions.waitForMinutes(15);
	}



	/**
	 * This method verifies Protect Alerts, policy Violation and remediation activities
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposure", priority = 2)
	public void verifyPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		// Assert Policy violation logs
		verifyPolicyViolation(policyBean, policyName, fileName);
		// Assert Remediation Activity
		verifyRemediationAction(policyBean, fileName);

	}

	/**
	 * This test method validates policy deletion action
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void deletePolicies(List<String[]> list) throws Exception {
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: deletePolicies - " + policyName, true);
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Completed testcase: deletePolicies - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
	}
	
	/**
	 * 
	 * @param policyBean
	 * @param fileName
	 * @throws Exception
	 */
	private void verifyRemediationAction(PolicyBean policyBean, String fileName) throws Exception {
		if (policyBean.getRemediationActivity().contains(ProtectTestConstants.DELETE_EMAIL_ATTACHMENT) && (policyBean.getExposureType().contains(ProtectTestConstants.EXTERNAL) || policyBean.getExposureType().contains(ProtectTestConstants.UNEXPOSED))) {
			String deleteMessage = "Admin has remediated/deleted an email with subject \"" + fileName + " Mail With Attachment\"";

			Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, ProtectTestConstants.OFFICE_365, fileName,
					requestHeader, suiteData, "Admin has remediated/deleted an email");
			
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				protectFunctions.checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
				return;
			}
			
			fileName = fileName + " Mail With Attachment";
			protectFunctions.assertRemediationForO365Mail(messagesLogs, ProtectTestConstants.INFORMATIONAL, ProtectTestConstants.OFFICE_365, fileName,
					"Delete", deleteMessage);

		} else if (policyBean.getRemediationActivity().contains(ProtectTestConstants.DELETE_EMAIL_BODY) && (policyBean.getExposureType().contains(ProtectTestConstants.EXTERNAL) || policyBean.getExposureType().contains(ProtectTestConstants.UNEXPOSED))) {
			String deleteMessage = "Admin has remediated/deleted an email with subject \"" + fileName + " Mail With Body\"";

			Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, ProtectTestConstants.OFFICE_365, fileName,
					requestHeader, suiteData, "Admin has remediated/deleted an email");
			
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				protectFunctions.checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
				return;
			}
			
			fileName = fileName + " Mail With Body";
			protectFunctions.assertRemediationForO365Mail(messagesLogs, ProtectTestConstants.INFORMATIONAL, ProtectTestConstants.OFFICE_365, fileName,
					"Delete", deleteMessage);

		}
	}

	/**
	 * 
	 * @param policyBean
	 * @param policyName
	 * @param fileName
	 * @throws Exception
	 */
	private void verifyPolicyViolation(PolicyBean policyBean, String policyName, String fileName) throws Exception {
		
		Map<String, String> policyViolationLogsMessage = protectFunctions.getProtectPolicyViolationAlertLogMessage(restClient, fileName,
				policyName, requestHeader, suiteData);
		Map<String, String> displayLogsMessage = protectFunctions.getSecurletPolicyViolationLogs(restClient, ProtectTestConstants.OFFICE_365, fileName,
				policyName, requestHeader, suiteData);
		
		Map<String, String> ciPolicyViolationLogsMessage = protectFunctions.getProtectPolicyViolationCIAlertLogMessage(restClient, fileName,
				policyName, requestHeader, suiteData);
		Map<String, String> ciDisplayLogsMessage = protectFunctions.getSecurletCIPolicyViolationLogs(restClient, ProtectTestConstants.OFFICE_365, fileName,
				policyName, requestHeader, suiteData);
		
	    String shareLog = policyViolationLogsMessage.get(ProtectTestConstants.MESSAGE);
	    String ciLog = ciPolicyViolationLogsMessage.get(ProtectTestConstants.MESSAGE);
	    String shareDisplayLog = displayLogsMessage.get(ProtectTestConstants.MESSAGE);
	    String ciDisplayLog = ciDisplayLogsMessage.get(ProtectTestConstants.MESSAGE);
	    
		// if both share and ci logs are null, skip verification and fail the tests
		if (shareLog == null && ciLog == null) {
			Logger.info("Test case failed because Policy Violation Logs are not triggered for the activity performed after a wait time of 15 minutes");
			protectFunctions.checkSplunkLogs(suiteData, fileName, false);
			Assert.assertTrue(false);
			return;
		} 
		
		if ((  (shareLog != null && shareDisplayLog != null) && ciLog == null) || ( (shareLog !=null && shareDisplayLog != null) && (ciLog!=null && ciDisplayLog != null))) {
			if (!policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.UNEXPOSED)) {

				if (policyBean.getRemediationActivity().contains(ProtectTestConstants.DELETE_EMAIL_ATTACHMENT))
					protectFunctions.assertPolicyViolation(policyViolationLogsMessage, ProtectTestConstants.OFFICE_365, fileName, policyName);
				else
					protectFunctions.assertPolicyViolationForO365MailBody(policyViolationLogsMessage, ProtectTestConstants.OFFICE_365, fileName, policyName);


				// Assert Policy Violation Activity
				if (policyBean.getRemediationActivity().contains(ProtectTestConstants.DELETE_EMAIL_ATTACHMENT))
					protectFunctions.assertPolicyViolation(displayLogsMessage, ProtectTestConstants.OFFICE_365, fileName, policyName);
				else
					protectFunctions.assertPolicyViolationForO365MailBody(displayLogsMessage, ProtectTestConstants.OFFICE_365, fileName, policyName);

			}
		}
		
	// is share log is null, verify ci log
		else if ((ciLog != null && shareLog ==null)) {
			if (!policyBean.getRiskType().equalsIgnoreCase("no")) {
				if (policyBean.getRemediationActivity().contains(ProtectTestConstants.DELETE_EMAIL_ATTACHMENT))
					protectFunctions.assertCIPolicyViolation(ciPolicyViolationLogsMessage, ProtectTestConstants.OFFICE_365, fileName, policyName);
				else
					protectFunctions.assertCIPolicyViolationForO365MailBody(ciPolicyViolationLogsMessage, ProtectTestConstants.OFFICE_365, fileName, policyName);


				if (policyBean.getRemediationActivity().contains(ProtectTestConstants.DELETE_EMAIL_ATTACHMENT))
					protectFunctions.assertCIPolicyViolation(ciDisplayLogsMessage, ProtectTestConstants.OFFICE_365, fileName, policyName);
				else
					protectFunctions.assertCIPolicyViolationForO365MailBody(ciDisplayLogsMessage, ProtectTestConstants.OFFICE_365, fileName, policyName);

			}
		}
		
		

	}
	
	private File createDynamicFile(PolicyBean policyBean) {
		File file = null;
		if(policyBean.getRemediationActivity().equalsIgnoreCase("deleteEmailAttachment")) {
			if (policyBean.getCiqProfile().equals("PreDefDict")) {
				suiteData.setCSRFToken(requestHeader.get(0).getValue());
				suiteData.setSessionID(requestHeader.get(4).getValue());
				dciFunctions.createContentIqProfile(restClient, suiteData, "PreDefDict", ciqDictProfileName, ciqDictProfileType+" Description", ciqDictProfileType, false);
				dciFunctions.waitForSeconds(1);
				ciqDictFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH,
						ciqDictFileName, dciFunctions.getCurrentDateTime());
				file = new File(DCIConstants.DCI_FILE_TEMP_PATH + File.separator + ciqDictFileName);
				fileCollection.put(policyBean.getPolicyName(), ciqDictFileName);
				policyBean.setFileName(ciqDictFileName);
				policyBean.setCiqProfile(ciqDictProfileName);
			} else if (policyBean.getCiqProfile().equals("PreDefTerms")) {
				suiteData.setCSRFToken(requestHeader.get(0).getValue());
				suiteData.setSessionID(requestHeader.get(4).getValue());
				dciFunctions.createContentIqProfile(restClient, suiteData, "PreDefTerms", ciqTermsProfileName, ciqTermsProfileType+" Description", ciqTermsProfileType, false);
				dciFunctions.waitForSeconds(1);
				ciqTermsFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH,
						ciqTermsFileName, dciFunctions.getCurrentDateTime());
				file = new File(DCIConstants.DCI_FILE_TEMP_PATH + File.separator + ciqTermsFileName);
				fileCollection.put(policyBean.getPolicyName(), ciqTermsFileName);
				policyBean.setFileName(ciqTermsFileName);
				policyBean.setCiqProfile(ciqTermsProfileName);
				
			}
			else {
			    file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
					policyBean.getFileExt());
				fileCollection.put(policyBean.getPolicyName(), file.getName());
				policyBean.setFileName(file.getName());
			}
		}
		else if(policyBean.getRemediationActivity().equalsIgnoreCase("deleteEmailBody")) {
		    file = protectFunctions.createDynamicCIFile(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH, new File(""),
				policyBean.getFileExt(), policyBean.getRiskType());
			fileCollection.put(policyBean.getPolicyName(), file.getName());
			policyBean.setFileName(file.getName());
		}
		return file;
	}
	
	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>(); 
		if (suiteData.getSaasApp().endsWith("sanity"))
			list.add(O365DataProvider.getO365MailPolicySanityData());
		else
			list.add(O365DataProvider.getO365MailPolicyData());
		return new Object[][] { { list } };  
	}	
	
	
	@DataProvider(name = "DataExposure")
	public Object[][] getData() {
		if (suiteData.getSaasApp().endsWith("sanity"))
			return O365DataProvider.getO365MailPolicySanityData();
		else
			return O365DataProvider.getO365MailPolicyData();
	}	
	
}