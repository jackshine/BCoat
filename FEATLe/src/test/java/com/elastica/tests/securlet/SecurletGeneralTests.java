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
public class SecurletGeneralTests extends CommonTest{

	SoftAssert sAssert = null;
	
	/**
	 * Verify redirection to securlet dashboard from home page
	 */
	@Priority(1)
	@Test(groups = { "regression","sanityGoogleApps" })
	public void testSecurletDashboardAllTab_RedirectionFromHomePage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for redirection securlet dashboard from cloudsoc home page and validate all tabs are loading");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Click on hover menu and navigate to securlet dashboard");
		Logger.info("3. Validate Exposed files tab should be loaded by default and all 4 tabs appearing as well");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToSecurletDashboard(getWebDriver(), suiteData);
		
		String headerText = securlet.getSecurletHeader(getWebDriver());
		sAssert.assertEquals(headerText,"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen Expected:"+"Securlet for\n"+suiteData.getSaasAppName()
				+" Actual:"+headerText);
		
		String tabText = securlet.getActiveTabText(getWebDriver());
		sAssert.assertEquals(tabText,"Exposed Files",
				"Active tab header text mismatch is seen Expected:Exposed Files"
				+" Actual:"+tabText);
		
		int countTabs = securlet.countSecurletTabs(getWebDriver());
		if(suiteData.getSaasAppName().contains("Google Apps")){
			sAssert.assertEquals(countTabs,6,
					"Securlet tab count mismatch is seen Expected:6"
				+" Actual:"+headerText);
		}else{
			sAssert.assertEquals(countTabs,5,
					"Securlet tab count mismatch is seen Expected:5"
				+" Actual:"+headerText);
		} 
		
	
		sAssert.assertAll();
	}
	
	@Priority(5)
	@Test(groups = { "regression","smoke","sanityDropbox" })
	public void testSecurletDashboardAllTab_ExternallyOwnedWidgetValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C168199 - An Admin can view the Externally Owned on Dashboard");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToSecurletDashboard(getWebDriver(), suiteData);
		
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");

		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyMoveToExteralTab(getWebDriver()),"",
				"Verification of Show Overview Videois failing");
														   
		securlet.clickTabSecurletDashboard(getWebDriver(),"Activities");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Activities",
				"Active tab header text mismatch is seen");
		
		securlet.clickTabSecurletDashboard(getWebDriver(),"Exposed Files");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		sAssert.assertEquals(securlet.verifyExposuresWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
		sAssert.assertEquals(securlet.verifyTopContentTypesWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
	    securlet.clickTabSecurletDashboard(getWebDriver(),"Exposed Users");
	    
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Users",
					"Active tab header text mismatch is seen");
		
		sAssert.assertAll();
		
	}
	
	@Priority(2)
	@Test(groups = { "regression" })
	public void testSecurletDashboardAllTab_NavigationBetweenTabs() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Navigation between tabs in securlet dashboard");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Click on each tab and validate navigation between all 4 tabs are happening sucessfully");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		securlet.clickTabSecurletDashboard(getWebDriver(),"Exposed Users");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Users",
				"Active tab header text mismatch is seen");
		
		securlet.clickTabSecurletDashboard(getWebDriver(),"Other Risks");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
		
		if(suiteData.getSaasAppName().contains("Google Apps")){
			securlet.clickTabSecurletDashboard(getWebDriver(),"Apps");
			sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Apps",
					"Active tab header text mismatch is seen");
		}
														   
		securlet.clickTabSecurletDashboard(getWebDriver(),"Activities");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Activities",
				"Active tab header text mismatch is seen");
		
		securlet.clickTabSecurletDashboard(getWebDriver(),"Exposed Files");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
	
		sAssert.assertAll();
	}
	
	@Priority(3)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_LearnMoreLink(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Validate the Learn More link");
		Logger.info("***************************************");
	
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");

		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		sAssert.assertEquals(securlet.verifyOtherRisks_LearnMore(getWebDriver(),suiteData.getSaasAppName()),"",
				"Verification of Show Learn More failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(4)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_OptionsButton(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Validate the  Options on top of securlet tab");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");

		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyOtherRisks_Options(getWebDriver()),"",
				"Verification of Show Options failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(5)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_ShowOverviewVideo() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the Show overview video");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");

		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyOtherRisks_ShowOverviewVideo(getWebDriver()),"",
				"Verification of Show Overview Videois failing");
		
		sAssert.assertAll();
		
	}
	
	
	
	
	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}
	
}
