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
public class SecurletExposedUsersTabsTests extends CommonTest{

	SoftAssert sAssert = null;
	
	@Priority(1)
	@Test(groups = { "regression","smoke"})
	public void testSecurletDashboardExposedUsersTab_TapInternalAndTabExternalRowCount(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1938710 - Exposed Users tab - Apply Filter and verify the user count under Internal and External Collaborator Section.");
		Logger.info("3. C1877353 - Doughnut chart will show total number of Internal & External exposed users.");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Exposed Users", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Users",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedUsersTableBody_TapInternalAndExternal(getWebDriver()),"",
				"Verification of Search Box failing");
		
		sAssert.assertAll();
		
	}
	
	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}
	
}
