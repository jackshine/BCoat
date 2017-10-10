package com.elastica.tests.dci;

import java.io.File;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.common.CommonTest;
import com.elastica.constants.dci.DCIConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

/**
 * DCI Test Suite
 * @author Eldo Rajan
 *
 */
public class DCITests extends CommonTest{

	@Priority(1)
	@Test(groups = { "sanity","smoke","regression" })
	public void testRiskTypesTests() throws Exception {
		Map<String, String> ciq = dci.createCIQProfileWithRiskTypes(client, suiteData);
		dci.createCIQPolicy("DCI_RISKS_POLICY_NAME", ciq, suiteData, backend.getHeaders(suiteData));
		ciq = dci.createCIQProfileWithContentTypes(client, suiteData);
		dci.createCIQPolicy("DCI_CONTENT_POLICY_NAME", ciq, suiteData, backend.getHeaders(suiteData));
		
		login.login(getWebDriver(), suiteData);
		box.login(getWebDriver(), suiteData);
		String filePath=DCIConstants.DCI_FILE_UPLOAD_PATH+File.separator+"pii.txt";
		box.uploadFile(getWebDriver(), filePath, 5);
	}
	
	
	/**********************************************BEFORE/AFTER CLASS*****************************************/

	/**
	 * Enable content inspection
	 */
	@BeforeClass(alwaysRun=true)
	public void enableContentInspection() {
		try {
			String payload = dci.getEnableContentInspectionLog();
			dci.enableContentInspection(client, 
					dci.getCookieHeaders(suiteData), 
					new StringEntity(payload), suiteData.getScheme(), suiteData.getHost());

		} catch (Exception ex) {
			Logger.info("Issue with Enabling of Content Inspection" + ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete all CIQ profiles and policies
	 * @throws Exception 
	 */
	@BeforeClass(alwaysRun=true)
	public void cleanUpPoliciesProfilesBeforeClass() throws Exception {
		dci.deleteAllPolicies(client, backend.getCookieHeaders(suiteData), suiteData);
		dci.deleteAllCIQProfile(client, suiteData);
	}
	
	/**
	 * Delete all CIQ profiles and policies
	 * @throws Exception 
	 */
	@AfterClass(alwaysRun=true)
	public void cleanUpPoliciesProfilesAfterClass() throws Exception {
		dci.deleteAllPolicies(client, backend.getCookieHeaders(suiteData), suiteData);
		dci.deleteAllCIQProfile(client, suiteData);
	}
}
