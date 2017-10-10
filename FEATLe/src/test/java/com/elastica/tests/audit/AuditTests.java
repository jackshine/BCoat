package com.elastica.tests.audit;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.common.CommonTest;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

/**
 * Audit Test Suite
 * @author Eldo Rajan
 *
 */
public class AuditTests extends CommonTest{
	SoftAssert sAssert = null;
	
	@Priority(1)
	@Test(groups = { "sanity","smoke","regression" })
	public void testDataRangeFilterAppliedOnOneTabIsPersistentOnAllTabs() throws Exception {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to audit dashboard page and verify date range filter is set to Last Day");
		Logger.info("3. Switch to other tabs and verify date rang filter is still Last Day");
		Logger.info("4  T4409435	Apply Date Range Filter and verify that it persists across all the tabs");
		Logger.info("5  T4410554	Verify that new column 'Actions' added in Service grid of Audit - > Service Tab.");
		Logger.info("***************************************");

		login.login(getWebDriver(), suiteData);
		
		//backend.createDSForAudit(client, suiteData);
		
		dashboard.navigateToAuditDashboard(getWebDriver(), suiteData);
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		audit.clickOptionFromAuditTimePeriodDropdown(getWebDriver(), "Last Day");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Summary Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Services Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		
		audit.validateauditServiceTableHeadingActions(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Users");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Users Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Destinations");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Destinations Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
	}
	
	@Priority(2)
	@Test(groups = { "smoke","regression" })
	public void testServicePagesDefaultTags() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToAuditDashboard(getWebDriver(), suiteData);
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
	}
	
	@Priority(3)
	@Test(groups = { "smoke","regression" })
	public void testSummaryPagesSpiderChart() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4408159 - Spider chart.");
		Logger.info("***************************************");
		
		String validationMessage="";
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		validationMessage += audit.getSpiderChartDetails(getWebDriver());
		Assert.assertEquals(validationMessage, "", "Output Response Validation for audit Spider Chart Details and errors observed are "+validationMessage);
		
	}
	
	@Priority(4)
	@Test(groups = { "smoke","regression" })
	public void testSummaryPagesServicesSort() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4407898	Overview Page - Apply Sort on Services List Data.");
		Logger.info("***************************************");
		
		boolean validationMessage=false; 
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		validationMessage = audit.verifySummaryPagesServicesSort(getWebDriver());
		Assert.assertEquals(validationMessage, true, "Summary Pages Services Sort missmatch "+validationMessage);
		
	}
	
	@Priority(5)
	@Test(groups = { "smoke","regression" })
	public void testChartVerifyDiffrentTab() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to audit dashboard page and verify date range filter is set to Last Day");
		Logger.info("3. T4407933 - Overview page - Corresponding chart should be displayed based on selected tab");
		Logger.info("4  T4407860 - Overview page - Charts view");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		audit.clickOptionFromAuditTimePeriodDropdown(getWebDriver(), "Last Day");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Summary Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		
		audit.validateSpiderChart(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Services Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		
		audit.validateBarChart(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Users");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Users Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		
		audit.validateBarChart(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Destinations");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Destinations Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		
		audit.validateAuditMapChart(getWebDriver());
		
	}
	
	@Priority(6)
	@Test(groups = { "smoke","regression" })
	public void testAuditExportCSVButton() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4408100	Overview Page - Services Tab - Clicking Export CSV should download services export file");
		Logger.info("3. T4408101	Overview Page - Users Tab - Clicking Export CSV should download Users export file");
		Logger.info("2. T4408102	Overview Page - Destinations Tab - Clicking Export CSV should download");
		Logger.info("***************************************");

		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		audit.clickOptionFromAuditTimePeriodDropdown(getWebDriver(), "Last Day");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Summary Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Services Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		
		int ServicesbeforeFilecount = audit.getFielsCounts();
		Logger.info("Services - beforeFilecount : "+ServicesbeforeFilecount);
		
		audit.validateAuditExportCSVButton(getWebDriver());
		
		int ServicesafterFilecount = audit.getFielsCounts();
		Logger.info("Services - afterFilecount : "+ServicesafterFilecount);
		
		Assert.assertEquals(ServicesbeforeFilecount+1, ServicesafterFilecount , "Services Page ExportCSVButton missmatch");
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Users");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Users Tab: Data range filter is not set correctly Expected:Last Day but was"+
						getTextDateRangeFilter);
		int UsersbeforeFilecount = audit.getFielsCounts();
		Logger.info("Users - beforeFilecount : "+UsersbeforeFilecount);
		
		audit.validateAuditExportCSVButton(getWebDriver());
		
		int UsersafterFilecount = audit.getFielsCounts();
		Logger.info("Users - afterFilecount : "+UsersafterFilecount);
		
		Assert.assertEquals(UsersbeforeFilecount+1, UsersafterFilecount , "Users Page ExportCSVButton missmatch");

		
		audit.clickOptionFromAuditTab(getWebDriver(), "Destinations");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"Last Day","Destinations Tab: Data range filter is not set correctly Expected:Last Day but was"+getTextDateRangeFilter);
		
		int DestinationsbeforeFilecount = audit.getFielsCounts();
		Logger.info("Destinations - beforeFilecount : "+DestinationsbeforeFilecount);		
		audit.validateAuditExportCSVButton(getWebDriver());
		int DestinationsafterFilecount = audit.getFielsCounts();
		Logger.info("Destinations - afterFilecount : "+DestinationsafterFilecount);
		
		Assert.assertEquals(DestinationsbeforeFilecount+1, DestinationsafterFilecount , "Destinations Page ExportCSVButton missmatch");

		
	}
	
	@Priority(7)
	@Test(groups = { "smoke","regression" })
	public void testSummaryAuditServiceLink() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410546 - Verify that new link 'Manage Service Visibilit' is added on Audit Sum.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.validateauditServiceLink(getWebDriver());
		
	}
	
	@Priority(8)
	@Test(groups = { "smoke","regression" })
	public void testFiltersApplyServicesTags() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);

		audit.validateAuditServicesFilters(getWebDriver());
		
	}
	

	
	@Priority(9)
	@Test(groups = { "smoke","regression" })
	public void testFiltersApplyCategoriesTags() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);

		audit.validateAuditCategoriesFilters(getWebDriver());

	}	
	
	

	@Priority(10)
	@Test(groups = { "smoke","regression" })
	public void testFiltersApplyDefaultTags() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);

		audit.validateAuditDefaultTagsFilters(getWebDriver());

	}	
	

	@Priority(11)
	@Test(groups = { "smoke","regression" })
	public void testFiltersApplyCustomTags() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);

		audit.validateAuditCustomTagsFilters(getWebDriver());
	}	

	@Priority(12)
	@Test(groups = { "smoke","regression" })
	public void testFiltersRiskTab() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);

		audit.validateAuditRiskTabFilters(getWebDriver());
		}	

	

	@Priority(13)
	@Test(groups = { "smoke","regression" })
	public void testFiltersUsersTab() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);

		audit.validateAuditUsersTabFilters(getWebDriver());
		}	
	
	
	@Priority(14)
	@Test(groups = { "smoke","regression" })
	public void testFiltersCountriesTab() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);

		audit.validateAuditCountriesTabFilters(getWebDriver());
		}	
	
//	@Priority(15)
//	@Test(groups = { "smoke1","regression" })
//	public void testFiltersApplyCitiesTab() {
//		
//		Logger.info("************Test Case Steps************");
//		Logger.info("Description: Go to audit, select data range filter in one tab "
//				+ "and verify it is same in all other tabs");
//		Logger.info("1. Login into cloudsoc home page");
//		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
//		Logger.info("***************************************");
//		
//		login.login(getWebDriver(), suiteData);
//		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
//		audit.validateAuditPage(getWebDriver());
//		audit.clickCloseOverviewVideo(getWebDriver());
//		
//		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
//		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
//		sAssert.assertEquals(getTextDateRangeFilter,
//				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
//						getTextDateRangeFilter);
//		
//		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
//		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
//		sAssert.assertEquals(getTextDateRangeFilter,
//				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
//						getTextDateRangeFilter);
//
//		audit.validateAuditCitiesTabFilters(getWebDriver());
//
//	}	
	
	@Priority(15)
	@Test(groups = { "smoke","regression" })
	public void testFiltersApplyPlatformsTab() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410794 - Verify that Audit Report shows the Default Tags in the Service pages.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);

		audit.validateAuditPlatformsabFilters(getWebDriver());

	}		
	
	@Priority(16)
	@Test(groups = { "smoke","regression" })
	public void testUserCanAddComments() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410864 - Verify that user can add comments if he has audit modification rights.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
		audit.validateUserCanAddComments(getWebDriver());
		
	}
	@Priority(17)
	@Test(groups = { "smoke","regression" })
	public void testAuditUserTabExportCSVButton() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4409205 - Audit - UD - click export csv button should download csv file properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Users");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
		int DestinationsbeforeFilecount = audit.getFielsCounts();
		Logger.info("Destinations - beforeFilecount : "+DestinationsbeforeFilecount);		
		audit.validateAuditExportCSVButton(getWebDriver());
		int DestinationsafterFilecount = audit.getFielsCounts();
		Logger.info("Destinations - afterFilecount : "+DestinationsafterFilecount);
		
		Assert.assertEquals(DestinationsbeforeFilecount+1, DestinationsafterFilecount , "Destinations Page ExportCSVButton missmatch");

		
	}
	
	@Priority(18)
	@Test(groups = { "smoke","regression" })
	public void testServiceTabSearchUserBox() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4408005 Audit - user detail page- search on service usage search box.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
		audit.validateServiceTabSearchUserBox(getWebDriver());
		
	}
	
	@Priority(19)
	@Test(groups = { "smoke","regression" })
	public void testSettingsTag() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410535 Smoke - Verify that new section 'Tags' added in the side panel on Settings page.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
		audit.testSettingsTag(getWebDriver());
		
	}
	
	@Priority(20)
	@Test(groups = { "smoke","regression" })
	public void testSettingsAddTag() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410536	[D] Smoke - Verify that when user clicks + New Tag slide opens to add new tag..");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
		audit.validateSettingsAddTag(getWebDriver());
		
	}

	@Priority(21)
	@Test(groups = { "smoke","regression" })
	public void testSettingsTagCreated_LastModified() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410537	[D] Verify that Tag, Created, Last Modified and Actions columns displayed as soon the first tag created..");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
		audit.validateSettingsTagCreated_LastModified(getWebDriver());
		
	}
	
	@Priority(22)
	@Test(groups = { "smoke","regression" })
	public void testSettingsTagCountIncreasedByOne() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410538	Verify that as soon the user creates the first Tag the Tag count is increased by 1 every time.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
		audit.validateSettingsTagCountIncreasedByOne(getWebDriver());
		
	}
	
	@Priority(23)
	@Test(groups = { "smoke","regression" })
	public void testSettingsEditAddedTag() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410540	Verify that clicking Edit link under Action column opens the Edit Tag slider and user can Edit Tags accordingly..");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
		audit.validateSettingsEditAddedTag(getWebDriver());
		
	}
	@Priority(24)
	@Test(groups = { "smoke","regression" })
	public void testTagColumnIsSortByAscendingAndDescending() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. T4410542	Verify that user can sort the grid with Tags column in ascending and descending order.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		String getTextDateRangeFilter = audit.getDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Summary Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		getTextDateRangeFilter = audit.getOtherTabsDateRangeFilterValue(getWebDriver());
		sAssert.assertEquals(getTextDateRangeFilter,
				"1 Week","Services Tab: Data range filter is not set correctly Expected:1 Week but was"+
						getTextDateRangeFilter);
	
		audit.validateTagColumnIsSortByAsendingAndDesending(getWebDriver());
		
	}
	
	@Priority(25)
	@Test(groups = { "regression" })
	public void testAuditTabsValidation() {
		String validationMessage="";
		
		String[] servicesName = {"Box","YouTube","Zendesk","LinkedIn","Facebook","Dropbox","GitHub","Trimble"};
		String[] servicesDescription = {"File Sharing, Storage","Video Hosting","Customer Support","Social Network","Social Network","File Sharing, Storage","Code Hosting","Surveys"};
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		audit.clickCloseOverviewVideo(getWebDriver());
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Summary");
		audit.clickOptionFromAuditTimePeriodDropdown(getWebDriver(), "Last Day");
		
		int auditScoreCount = audit.getScoreCount(getWebDriver());
		int auditSaasServicesCount = audit.getSaasServicesCount(getWebDriver());
		int auditRiskPercentageCount = audit.getRiskPercentageCount(getWebDriver());
		int auditRiskServicesCount = audit.getRiskServicesCount(getWebDriver());
		int auditUserCount = audit.getUserCount(getWebDriver());
		int auditDestinationCount = audit.getDestinationCount(getWebDriver());
		
		validationMessage += (auditScoreCount>0) ? "" : "Summary Tab: Expecting Audit Score but was " + auditScoreCount;
		validationMessage += (auditSaasServicesCount>=5) ? "" : "Summary Tab: Expecting Saas Services Count but was " + auditSaasServicesCount;
		validationMessage += (auditRiskPercentageCount>0) ? "" : "Summary Tab: Expecting Risk Percentage Donut Count but was " + auditRiskPercentageCount;
		validationMessage += (auditRiskServicesCount>=1) ? "" : "Summary Tab: Expecting Risk Services Donut Count but was " + auditRiskServicesCount;
		validationMessage += (auditUserCount>=1) ? "" : "Summary Tab: Expecting User Count but was " + auditUserCount;
		validationMessage += (auditDestinationCount>=1) ? "" : "Summary Tab: Expecting Destination Count Score but was " + auditDestinationCount;
		
		audit.clickAuditTopOptionsSelectorButton(getWebDriver(),"Top Risky Services");
		int tableCount = audit.getSummaryTableDetailsCount(getWebDriver());
		validationMessage += (auditRiskServicesCount==tableCount) ? "" : "Summary Tab: Top Risky services is not matching"
				+ " from the header Expected "+auditRiskServicesCount+" but was " + tableCount;
		validationMessage += audit.getSummaryTableDetails(getWebDriver(),"Top Risky Services",servicesName,servicesDescription);
		validationMessage += audit.getSummaryPanelDetails(getWebDriver(),"Top Risky Services",servicesName,servicesDescription);
		
		audit.clickAuditTopOptionsSelectorButton(getWebDriver(),"Top Used Services");
		tableCount = audit.getSummaryTableDetailsCount(getWebDriver());
		validationMessage += (auditSaasServicesCount==tableCount) ? "" : "Summary Tab: Top Used services is not matching"
				+ " from the header Expected "+auditSaasServicesCount+" but was " + tableCount;
		validationMessage += audit.getSummaryTableDetails(getWebDriver(),"Top Used Services",servicesName,servicesDescription);
		validationMessage += audit.getSummaryPanelDetails(getWebDriver(),"Top Used Services",servicesName,servicesDescription);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		validationMessage += audit.getServicesHeaderDetails(getWebDriver(), "Services");
		validationMessage += audit.getServicesTableDetails(getWebDriver(), "Services", servicesName, servicesDescription);
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Users");
		validationMessage += audit.getUsersHeaderDetails(getWebDriver(), "Users");
		validationMessage += audit.getUsersTableDetails(getWebDriver(), "Users");
		
		audit.clickOptionFromAuditTab(getWebDriver(), "Destinations");
		validationMessage += audit.getDestinationsHeaderDetails(getWebDriver(), "Destinations");
		validationMessage += audit.getDestinationsTableDetails(getWebDriver(), "Destinations");
		
		
		Assert.assertEquals(validationMessage, "", "Output Response Validation for audit page and errors observed are "+validationMessage);
		
		/*audit.clickOptionFromAuditTimePeriodDropdown(getWebDriver(), "1 Week");
		audit.clickOptionFromAuditTimePeriodDropdown(getWebDriver(), "1 Month");
		audit.clickOptionFromAuditTimePeriodDropdown(getWebDriver(), "1 Year");
		*/
		/*audit.clickOptionFromAuditTab(getWebDriver(), "Services");
		audit.clickOptionFromAuditTab(getWebDriver(), "Users");
		audit.clickOptionFromAuditTab(getWebDriver(), "Destinations");*/
	}
	

	@Priority(100)
	@Test(groups = {"smoke","regression" })
	public void testDeleteTags() throws Exception {
		login.login(getWebDriver(), suiteData);
		backend.deleteAllTags(client, suiteData);
	}


	/*********************************************************************/
	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}
	/*********************************************************************/
	
	
}
