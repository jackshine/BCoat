package com.elastica.tests.sources;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.elastica.common.CommonTest;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

/**
 * Sources Test Suite
 * @author Eldo Rajan
 *
 */
public class SourcesTests extends CommonTest{	
	
	@Priority(1)
	@Test(groups = { "smoke","regression" })
	public void testValidateSourcePageBug35383() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to sources page and validate if it is loading with all data"
				+ " and verify bug 35383");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Validate Bug 35383");
		Logger.info("3. Validate sources page is loading with correct data");
		Logger.info("***********************************");
		
		String validationMessage="";
		login.login(getWebDriver(), suiteData);
		
		validationMessage = sources.verifySourcesPage(getWebDriver());
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}
	
	@Priority(2)
	@Test(groups = { "sanity","smoke","regression" })
	public void testValidateClickingOfDeviceLogsFromSourcesPage() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to sources page and validate if it is loading with all data"
				+ " and verify clicking on any device logs redirects to audit page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Validate sources page is loading with correct data");
		Logger.info("***********************************");
		
		String validationMessage="";
		login.login(getWebDriver(), suiteData);
		
		validationMessage = sources.verifyDeviceLogsInSourcesPage(getWebDriver(), suiteData);
		Assert.assertEquals(validationMessage, "", 
				"Errors from device logs redirection "+validationMessage);
		
	}
	
	@Priority(3)
	@Test(groups = {"smoke","regression" })
	public void testValidateClickingOfSecurletsFromSourcesPage() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to sources page and validate if it is loading with all data"
				+ " and verify clicking on any device logs redirects to audit page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Validate sources page is loading with correct data");
		Logger.info("***********************************");
		
		String validationMessage="";
		login.login(getWebDriver(), suiteData);
		
		validationMessage = sources.verifySecurletsGateletsInSourcesPage(getWebDriver(), suiteData, "Securlets");
		Assert.assertEquals(validationMessage, "", 
				"Errors from device logs redirection "+validationMessage);
		
	}
	
	
	@Priority(4)
	@Test(groups = {"smoke","regression" })
	public void testValidateClickingOfGateletsFromSourcesPage() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to sources page and validate if it is loading with all data"
				+ " and verify clicking on any device logs redirects to audit page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Validate sources page is loading with correct data");
		Logger.info("***********************************");
		
		String validationMessage="";
		login.login(getWebDriver(), suiteData);
		
		validationMessage = sources.verifySecurletsGateletsInSourcesPage(getWebDriver(), suiteData, "Gatelets");
		Assert.assertEquals(validationMessage, "", 
				"Errors from device logs redirection "+validationMessage);
		
	}

}
