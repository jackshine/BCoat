package com.elastica.tests.dashboard;

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
public class DashboardTests extends CommonTest{

	@Priority(1)
	@Test(groups = { "sanity","smoke","regression" })
	public void testDashboardPage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as Admin and dashboard is loading");
		Logger.info("1. Login into cloudsoc home page as admin");
		Logger.info("2. Verify dashboard is loading properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Dashboard";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(10);
		
		Assert.assertTrue((dashboard.getDashboardWidgetCount(getWebDriver())>0), 
				"Dashboard main page is not loading with all active widgets");
	}
	
	@Priority(2)
	@Test(groups = { "sanity","smoke","regression" })
	public void testAuditDashboardPage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as Admin and audit dashboard is loading");
		Logger.info("1. Login into cloudsoc home page as admin");
		Logger.info("2. Navigate to audit dashboard and verify audit dashboard is loading properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToAuditDashboard(getWebDriver(), suiteData);
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Audit";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(10);
		
		Assert.assertTrue((audit.getScoreCount(getWebDriver())>0), 
				"Audit score is not loading in audit dashboard page");
	}
	
	@Priority(3)
	@Test(groups = { "sanity","smoke","regression" })
	public void testDetectDashboardPage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as Admin and detect dashboard is loading");
		Logger.info("1. Login into cloudsoc home page as admin");
		Logger.info("2. Navigate to detect dashboard and verify detect dashboard is loading properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToDetectDashboard(getWebDriver(), suiteData);
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Detect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(10);
		
		Assert.assertTrue((detect.getDashboardWidgetCount(getWebDriver())>0), 
				"Detect dashboard page is not loading with all active widgets");
	}
	
	@Priority(4)
	@Test(groups = { "sanity","smoke","regression" })
	public void testSecurletDashboardPage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as Admin and securlet dashboard is loading");
		Logger.info("1. Login into cloudsoc home page as admin");
		Logger.info("2. Navigate to securlet dashboard and verify securlet dashboard is loading properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToSecurletDashboardTabs(getWebDriver(), suiteData, "Activities");
		Assert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		Assert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Activities",
				"Active tab header text mismatch is seen");
		dashboard.hardWait(30);
		
		Assert.assertTrue((securlet.getAllActivitiesLogsText(getWebDriver()).
				contains("logs available from")), 
				"Securlet dashboard page is not loading with all activities");
	}
	
	@Priority(5)
	@Test(groups = { "sanity","smoke","regression" })
	public void testProtectDashboardPage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as Admin and protect dashboard is loading");
		Logger.info("1. Login into cloudsoc home page as admin");
		Logger.info("2. Navigate to protect dashboard and verify protect dashboard is loading properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToProtectDashboard(getWebDriver(), suiteData);
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(10);
		
		Assert.assertEquals(protect.getNoDataHeadertext(getWebDriver()),
				"No policies exist\nPlease create a new Policy.",
				"Protect no data header mismatch is seen");
	}
	
	@Priority(6)
	@Test(groups = { "sanity","smoke","regression" })
	public void testInvestigateDashboardPage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as Admin and investigate dashboard is loading");
		Logger.info("1. Login into cloudsoc home page as admin");
		Logger.info("2. Navigate to investigate dashboard and verify investigate dashboard is loading properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToInvestigateDashboard(getWebDriver(), suiteData);
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Investigate";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);
		
		Assert.assertTrue((investigate.getAllActivitiesLogsText(getWebDriver()).
				contains("matching logs")), 
				"Investigate dashboard page is not loading with all activities");
	}
	
	@Priority(7)
	@Test(groups = { "sanity","smoke","regression" })
	public void testStorePage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as Admin and store page is loading");
		Logger.info("1. Login into cloudsoc home page as admin");
		Logger.info("2. Navigate to store page and verify store page is loading properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToStoreDashboard(getWebDriver(), suiteData);
		dashboard.hardWait(20);
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Store";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
	}
	
	
}
