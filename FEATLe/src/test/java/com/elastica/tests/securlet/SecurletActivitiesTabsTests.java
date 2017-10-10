package com.elastica.tests.securlet;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.common.CommonTest;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

/**
 * Securlet Test Suite
 * @author Eldo Rajan
 *
 */
public class SecurletActivitiesTabsTests extends CommonTest{

	SoftAssert sAssert = null;
	
	@Priority(1)
	@Test(groups = { "regression","smoke","sanityBox" })
	public void testSecurletDashboardActivitiesTab_FiltersUsersBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1877680 - Activities tab - Applying filters on different users.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToSecurletDashboardTabs(getWebDriver(), suiteData, "Activities");
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Activities",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyActivitiesTab_FiltersUsersBox(getWebDriver()),"",
				"Verification of Search Box failing");
		
		sAssert.assertAll();
		
	}
	@Priority(2)
	@Test(groups = { "regression" })
	public void testSecurletDashboardActivitiesTab_FiltersUsersBoxAndSeverityBoxAccordingly(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1877682 - Activities tab - Applying filters on different users & severity accordingly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		securlet.clickTabSecurletDashboard(getWebDriver(), "Activities");
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Activities",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifySecurletDashboardActivitiesTab_FiltersUsersBoxAndSeverityBoxAccordingly(getWebDriver()),"",
				"Verification of Search Box failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(3)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardActivitiesTab_FiltersObjectBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1877683 - Activities tab - Applying filters on different objects labels.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Activities", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Activities",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyActivitiesTab_FiltersObjectBox(getWebDriver()),"",
				"Verification of Search Box failing");
		
		sAssert.assertAll();
		
	}
	@Priority(4)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardExposedUsersTab_TapFileExposure_Internal(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1877523 - Exposed Users tab - Applying filters on exposure type (Internal).");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Exposed Users", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Users",
				"Active tab header text mismatch is seen");
		sAssert.assertEquals(securlet.verifySecurletDashboardExposedUsersTab_TapFileExposure_Internal(getWebDriver(),suiteData.getSaasAppName()),"",
				"Verification of Search Box failing");
		sAssert.assertAll();
		
	}
	
	@Priority(5)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardExposedUsersTab_TapFileExposure_RiskType(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1877525 - Exposed Users tab - Applying filters on vulnerability type (PCI).");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Exposed Users", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Users",
				"Active tab header text mismatch is seen");
		sAssert.assertEquals(securlet.verifySecurletDashboardExposedUsersTab_TapFileExposure_RiskType(getWebDriver()),"",
				"Verification of Search Box failing");
		sAssert.assertAll();
		
	}
	
	@Priority(5)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardExposedUsersTab_TapFileExposure_RiskTypeAnsInternal(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1877528 - Exposed Users tab - Applying filters on any file type & file exposure (External).");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Exposed Users", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Users",
				"Active tab header text mismatch is seen");
		sAssert.assertEquals(securlet.verifySecurletDashboardExposedUsersTab_TapFileExposure_RiskTypeAnsInternal(getWebDriver(),suiteData.getSaasAppName()),"",
				"Verification of Search Box failing");
		sAssert.assertAll();
		
	}
	
	
	@Priority(6)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardActivitiesTab_FiltersSpecificUsersBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1877719 - Activities tab - Searching for a specific User.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Activities", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Activities",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifySecurletDashboardActivitiesTab_FiltersSpecificUsersBox(getWebDriver()),"",
				"Verification of Search Box failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(1)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardActivitiesTab_UIFiltersAndUnFilters(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1875888 - UI Filters are available by clicking the drop down button (located near Tab name).");
		Logger.info("3. C1875921 - When a Filter is unchecked the Stats (Charts, numbers, file listing) on the Exposed File tabs are updated properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Activities", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Activities",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifySecurletDashboardActivitiesTab_UIFiltersAndUnFilters(getWebDriver()),"",
				"Verification of Search Box failing");
		
		sAssert.assertAll();
		
	}
	
	
	
	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}
	
}
