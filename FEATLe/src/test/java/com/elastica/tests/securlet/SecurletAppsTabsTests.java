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
public class SecurletAppsTabsTests extends CommonTest{

	SoftAssert sAssert = null;
	
	/**
	 * Verify redirection to securlet dashboard from home page
	 */
	@Priority(1)
	@Test(groups = { "regression" })
	public void testSecurletDashboardRedirectionFromHomePage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for redirection securlet dashboard from cloudsoc home page and validate all tabs are loading");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Click on hover menu and navigate to securlet dashboard");
		Logger.info("3. Validate Exposed files tab should be loaded by default and all 4 tabs appearing as well");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Exposed Files", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
	
		dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), "Other Risks", appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		if(suiteData.getSaasAppName().contains("Google Apps")){
			sAssert.assertEquals(securlet.countSecurletTabs(getWebDriver()),6,
					"Securlet tab count mismatch is seen");
		}else{
			sAssert.assertEquals(securlet.countSecurletTabs(getWebDriver()),5,
					"Securlet tab count mismatch is seen");
		} 
		
	
		sAssert.assertAll();
	}
	
	
	
	
	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}
	
}
