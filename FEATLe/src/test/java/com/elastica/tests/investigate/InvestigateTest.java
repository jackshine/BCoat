package com.elastica.tests.investigate;

import static org.testng.Assert.assertTrue;

import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.common.CommonTest;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class InvestigateTest extends CommonTest{
	String fromTime=null;

	@Priority(1)
	@Test(groups ={"Test"})
	public void gDriveOperations() throws Exception {
		fromTime=backend.getCurrentTime();
		Reporter.log("Starting performing activities on GDrive saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		gda.login(getWebDriver(), suiteData);
		Reporter.log("Finished performing activities on GDrive saas app", true);
	}

	@Priority(2)
	@Test(groups ={ "Test"}, dependsOnMethods = { "gDriveOperations" })
	public void gDrive_Test_01_Validate_GDrive_In_Investigate() throws Exception {
		Logger.info("Base url " + suiteData.getLoginUrl());
		login.homepage(getWebDriver(), suiteData);
		login.loginCloudSocPortalByAdmin(getWebDriver(), suiteData);
		dashboard.goToInvestigatePage(getWebDriver(), suiteData);
		investigate.clickFilter(getWebDriver());
		assertTrue(investigate.verifyFilterServiceTab(getWebDriver(), "Service", suiteData.getSaasAppName()), "Service Box is missing in Filter");
	}
	
	@Priority(3)
	@Test(groups ={ "Test"}, dependsOnMethods = { "gDriveOperations" })
	public void gDrive_Test_02_Validate_GDrive_LogCount_Present_In_Fillter() throws Exception {
		investigate.choiceApp(getWebDriver(), "Google Drive");
		int logCount = investigate.getLogCount(getWebDriver(), "1", "Google Drive");
		assertTrue(logCount > 1, "Log is not present, Log Count is :" +logCount );
	}
	
	@Priority(4)
	@Test(groups ={ "Test"}, dependsOnMethods = { "gDriveOperations" })
	public void gDrive_Test_03_Validate_GW_Logcount() throws Exception {
		int logCount = investigate.getGWLogCount(getWebDriver(), "GW");
		assertTrue(logCount > 1, "Log is not present, Log Count is :" +logCount );
		int gwServicecount = investigate.getServiceNameCount(getWebDriver(), "GW");
		assertTrue(gwServicecount > 1, "GW Service is not present in Table, Log Count is :" +logCount );
	}

}
