package com.elastica.tests.investigate;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.elastica.common.CommonTest;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

/**
 * Dashboard Test Suite
 * @author Eldo Rajan
 *
 */
public class InvestigateTests extends CommonTest{

	@Priority(1)
	@Test(groups = { "sanity","smoke","regression1" })
	public void testInvestigateDashboardPage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify investigate dashboard page");
		Logger.info("1. Login into cloudsoc and navigate to investigate page");
		Logger.info("2. Verify atleast event is present and click on it");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToInvestigateDashboard(getWebDriver(), suiteData);
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
	}
	
	@Priority(2)
	@Test(groups = { "sanity","smoke","regression1" })
	public void testInvestigateFilterOnService() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify investigate dashboard page");
		Logger.info("1. Login into cloudsoc and navigate to investigate page");
		Logger.info("2. Verify atleast event is present");
		Logger.info("3. Verify filters appear, select one option from Services filter");
		Logger.info("4. Verify events loads after clicking on services filter, filter event count matches with activities count in detailed list");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToInvestigateDashboard(getWebDriver(), suiteData);
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnService(getWebDriver());
		
	}
	
	@Priority(3)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnInstance() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);
		investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnInstance(getWebDriver());
		
	}
	@Priority(4)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnUser() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnUser(getWebDriver());
		
	}
	@Priority(5)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnAccontType() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnAccountType(getWebDriver());
		
	}
	
	@Priority(6)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnObject() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnObject(getWebDriver());
		
	}
	
	@Priority(7)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnActivity() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnActivity(getWebDriver());
		
	}
	@Priority(8)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnSeverity() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnSeverity(getWebDriver());
		
	}
	@Priority(9)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnSourceLocation() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnSourceLocation(getWebDriver());
		
	}
	@Priority(10)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnBrowser() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnBrowser(getWebDriver());
		
	}
	@Priority(11)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnPlatform() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnPlatform(getWebDriver());
		
	}
	
	@Priority(12)
	@Test(groups = { "smoke","regression1" })
	public void testInvestigateFilterOnDevice () {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyInvestigateFilterOnDriver(getWebDriver());
		
	}
	@Priority(13)
	@Test(groups = { "smoke","regression1" })
	public void testExportas_CEF_Beforefilter () {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		//dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyExportas_CEF_Beforefilter(getWebDriver());
		
	}
	@Priority(14)
	@Test(groups = { "smoke","regression1" })
	public void testExportas_CSV_Beforefilter () {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		//dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyExportas_CSV_Beforefilter(getWebDriver());
		
	}
	@Priority(15)
	@Test(groups = { "smoke","regression2" })
	public void testExportas_LEEF_Beforefilter () {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		//dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyExportas_LEEF_Beforefilter(getWebDriver());
		
	}
	@Priority(16)
	@Test(groups = { "smoke","regression2" })
	public void testExportas_CEF_AfterFilterOnService () {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		//dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyExportas_CEF_AfterFilterOnService(getWebDriver());
		
	}
	@Priority(17)
	@Test(groups = { "smoke","regression2" })
	public void testExportas_CSV_AfterFilterOnService () {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		//dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyExportas_CSV_AfterFilterOnService(getWebDriver());
		
	}
	@Priority(18)
	@Test(groups = { "smoke","regression2" })
	public void testExportas_LEEF_AfterFilterOnService () {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		//dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyExportas_LEEF_AfterFilterOnService(getWebDriver());
		
	}
	
	@Priority(19)
	@Test(groups = { "smoke","regression2" })
	public void testLearnMore () {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.verifyLearnMore(getWebDriver());
		
	}
	@Priority(20)
	@Test(groups = { "smoke","regression2" })
	public void testInvestigatetTimePeriodDropdown () {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		investigate.clickOptionFromInvestigatetTimePeriodDropdown(getWebDriver(),"1 Day");
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");

		investigate.clickOptionFromInvestigatetTimePeriodDropdown(getWebDriver(),"1 Week");
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");

		investigate.clickOptionFromInvestigatetTimePeriodDropdown(getWebDriver(),"1 Month");
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");

		investigate.clickOptionFromInvestigatetTimePeriodDropdown(getWebDriver(),"1 Year");
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");

		investigate.clickOptionFromInvestigatetTimePeriodDropdown(getWebDriver(),"3 Months");
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");

		investigate.clickOptionFromInvestigatetTimePeriodDropdown(getWebDriver(),"6 Months");
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");

		investigate.clickOptionFromInvestigatetTimePeriodDropdown(getWebDriver(),"2 Years");
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");

		investigate.clickOptionFromInvestigatetTimePeriodDropdown(getWebDriver(),"3 Years");
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
	}
	@Priority(21)
	@Test(groups = { "smoke","regression2" })
	public void testFilterActivityDownloadInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Download_Informational(getWebDriver(),"Download","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_Download_InformationalSourcesPage(getWebDriver(),"Download","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}
	@Priority(22)
	@Test(groups = { "smoke","regression2" })
	public void testFilterActivityUploadInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Download_Informational(getWebDriver(),"Upload","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_Download_InformationalSourcesPage(getWebDriver(),"Upload","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}	

	@Priority(23)
	@Test(groups = { "smoke","regression2" })
	public void testFilterActivityDeleteInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Delete_Informational(getWebDriver(),"Delete","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_Delete_InformationalSourcesPage(getWebDriver(),"Delete","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}

	@Priority(24)
	@Test(groups = { "smoke","regression2" })
	public void testFilterActivityDeleteWarning() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Delete_Warning(getWebDriver(),"Delete","warning");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_Delete_Warning(getWebDriver(),"Delete","warning");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}

	@Priority(25)
	@Test(groups = { "smoke","regression2" })
	public void testFilterActivityContentInspectioncritical() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_ContentInspection_critical(getWebDriver(),"Content Inspection","critical");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_ContentInspection_criticalSourcesPage(getWebDriver(),"Content Inspection","critical");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}	

	@Priority(26)
	@Test(groups = { "smoke","regression2" })
	public void testFilterActivityContentInspectioninformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_ContentInspection_informational(getWebDriver(),"Content Inspection","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_ContentInspection_informationalSourcesPage(getWebDriver(),"Content Inspection","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}	
	@Priority(27)
	@Test(groups = { "smoke","regression2" })
	public void testFilterActivityLoginInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Login_Informational(getWebDriver(),"Login","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_Login_InformationalSourcesPage(getWebDriver(),"Login","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}

	@Priority(28)
	@Test(groups = { "smoke","regression2" })
	public void testFilterActivityReceiveInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Receive_Informational(getWebDriver(),"Receive","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_Receive_InformationalSourcesPage(getWebDriver(),"Receive","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}

	
	@Priority(29)
	@Test(groups = { "smoke","regression3" })
	public void testFilterActivityTrashWarning() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Trash_Warning(getWebDriver(),"Trash","warning");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_Trash_Warning(getWebDriver(),"Trash","warning");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}

	@Priority(30)
	@Test(groups = { "smoke","regression3" })
	public void testFilterActivityTrashInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		
		investigate.verifyFilterActivity_Trash_Informational(getWebDriver(),"Trash","informational");
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		
		validationMessage = investigate.verifyDetails_Trash_InformationalSourcesPage(getWebDriver(),"Trash","informational");
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
		
	}

	@Priority(31)
	@Test(groups = { "smoke","regression3" })
	public void testFilterActivityUnshareInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Unshare_Informational(getWebDriver(),"Unshare","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		validationMessage = investigate.verifyDetails_Unshare_InformationalSourcesPage(getWebDriver(),"Unshare","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
	}
	
	@Priority(32)
	@Test(groups = { "smoke","regression3" })
	public void testFilterActivityShareInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Share_Informational(getWebDriver(),"Share","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		validationMessage = investigate.verifyDetails_Share_InformationalSourcesPage(getWebDriver(),"Share","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
	}
	
	@Priority(33)
	@Test(groups = { "smoke","regression3" })
	public void testFilterActivityCreateInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Create_Informational(getWebDriver(),"Create","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		validationMessage = investigate.verifyDetails_Create_InformationalSourcesPage(getWebDriver(),"Create","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
	}

	@Priority(34)
	@Test(groups = { "smoke","regression3" })
	public void testFilterActivityAuthorizeInformational() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_Authorize_Informational(getWebDriver(),"Authorize","informational");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		validationMessage = investigate.verifyDetails_Authorize_InformationalSourcesPage(getWebDriver(),"Authorize","informational");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
	}

	@Priority(35)
	@Test(groups = { "smoke","regression3" })
	public void testFilterActivityInvalidLoginwarning() {
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		String validationMessage="";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
		investigate.verifyFilterActivity_InvalidLogin_warning(getWebDriver(),"InvalidLogin","warning");
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
		validationMessage = investigate.verifyDetails_InvalidLogin_warningSourcesPage(getWebDriver(),"InvalidLogin","warning");
		
		Assert.assertEquals(validationMessage, "", 
				"Sources Page is mismatch "+validationMessage);
	}




@Priority(36)
@Test(groups = { "smoke","regression3" })
public void testFilterActivityAllowInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterActivity_Allow_Informational(getWebDriver(),"Allow","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Allow_InformationalSourcesPage(getWebDriver(),"Allow","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(37)
@Test(groups = { "smoke","regression3" })
public void testFilterActivitySendInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterActivity_Send_Informational(getWebDriver(),"Send","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Send_InformationalSourcesPage(getWebDriver(),"Send","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(38)
@Test(groups = { "smoke","regression3" })
public void testFilterActivityEditInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterActivity_Edit_Informational(getWebDriver(),"Edit","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Edit_InformationalSourcesPage(getWebDriver(),"Edit","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(39)
@Test(groups = { "smoke","regression3" })
public void testFilterActivityPreviewInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterActivity_Preview_Informational(getWebDriver(),"Preview","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Preview_InformationalSourcesPage(getWebDriver(),"Preview","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}
@Priority(40)
@Test(groups = { "smoke","regression3" })
public void testFilterActivityMoveInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterActivity_Move_Informational(getWebDriver(),"Move","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Move_InformationalSourcesPage(getWebDriver(),"Move","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}
@Priority(41)
@Test(groups = { "smoke","regression3" })
public void testFilterObjectFileInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_File_Informational(getWebDriver(),"File","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_File_InformationalSourcesPage(getWebDriver(),"File","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}
@Priority(42)
@Test(groups = { "smoke","regression3" })
public void testFilterObjectFilewarning() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_File_warning(getWebDriver(),"File","warning");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_File_warningSourcesPage(getWebDriver(),"File","warning");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}
@Priority(43)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectfileInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_file_Informational(getWebDriver(),"file","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_file_InformationalSourcesPage(getWebDriver(),"file","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}
@Priority(44)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectfilewarning() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_file_warning(getWebDriver(),"file","warning");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_file_warningSourcesPage(getWebDriver(),"file","warning");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
}

@Priority(45)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectSessionInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_Session_Informational(getWebDriver(),"Session","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Session_InformationalSourcesPage(getWebDriver(),"Session","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(46)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectSessionwarning() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_Session_warning(getWebDriver(),"Session","warning");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Session_warningSourcesPage(getWebDriver(),"Session","warning");	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
}

@Priority(47)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectUnknown_DeviceInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_Unknown_Device_Informational(getWebDriver(),"Unknown Device","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Unknown_Device_InformationalSourcesPage(getWebDriver(),"Unknown Device","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(47)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectFolderInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_Folder_Informational(getWebDriver(),"Folder","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Folder_InformationalSourcesPage(getWebDriver(),"Folder","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(48)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectFolderwarning() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_Folder_warning(getWebDriver(),"Folder","warning");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Folder_warningSourcesPage(getWebDriver(),"Folder","warning");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
}


@Priority(49)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectEmailMessageInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_EmailMessage_Informational(getWebDriver(),"Email Message","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_EmailMessage_InformationalSourcesPage(getWebDriver(),"Email Message","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(50)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectfolderInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_folder_Informational(getWebDriver(),"folder","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_folder_InformationalSourcesPage(getWebDriver(),"folder","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}


@Priority(51)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectfolderwarning() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_folder_warning(getWebDriver(),"folder","warning");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_folder_warningSourcesPage(getWebDriver(),"folder","warning");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(52)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectApplicationInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_Application_Informational(getWebDriver(),"Application","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Application_InformationalSourcesPage(getWebDriver(),"Application","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(53)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectAppInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_App_Informational(getWebDriver(),"App","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_App_InformationalSourcesPage(getWebDriver(),"App","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

@Priority(54)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectAuthenticationInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_Authentication_Informational(getWebDriver(),"Authentication","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_Authentication_InformationalSourcesPage(getWebDriver(),"Authentication","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}
@Priority(55)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectNotAvailableInformational() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_NotAvailable_Informational(getWebDriver(),"Not Available","informational");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_NotAvailable_InformationalSourcesPage(getWebDriver(),"Not Available","informational");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}
@Priority(56)
@Test(groups = { "smoke","regression4" })
public void testFilterObjectNotAvailablecritical() {
	login.login(getWebDriver(), suiteData);
	dashboard.clickOnSidebarLinks(getWebDriver(), "Investigate");
	String actualHeader = dashboard.getHeader(getWebDriver());
	String expectedHeader = "Investigate";
	String validationMessage="";
	Logger.info("Actual Header: "+actualHeader);
	Logger.info("Expected Header: "+expectedHeader);
	Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	dashboard.hardWait(30);investigate.changeLogResultTo_3_Years(getWebDriver());
	investigate.verifyFilterObject_NotAvailable_critical(getWebDriver(),"Not Available","critical");
	
	Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
			contains("matching logs")), 
			"Investigate dashboard page is not loading with all activities");
	
	validationMessage = investigate.verifyDetails_NotAvailable_criticalSourcesPage(getWebDriver(),"Not Available","critical");
	
	Assert.assertEquals(validationMessage, "", 
			"Sources Page is mismatch "+validationMessage);
	
}

}