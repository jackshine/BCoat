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
public class SecurletExposedFilesTabsTests extends CommonTest{

	SoftAssert sAssert = null;
	
	@Priority(33)
	@Test(groups = { "regression","smoke","sanityO365" })
	public void testSecurletDashboardExposedFilesTab_PopUpTable_ViewFileLink(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1877367 - Exposed Files - Verify that the 'View file' option is present over internally owned tab for each & every file/folder.");
		Logger.info("3. C918671 - Number of Files header is showing correct data at the top of file list i.e. (Showing 20 of 286)");
		Logger.info("4. C918736 - Once exposed file is clicked slide out window slide out smoothly");
		Logger.info("5. C918737 - Once close sign is clicked the slide out window is closed smoothly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToSecurletDashboard(getWebDriver(), suiteData);
		
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifySecurletDashboardExposedFilesTab_PopUpTable_ViewFileLink(getWebDriver()),"",
				"Verification of View File failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(1)
	@Test(groups = { "regression" })
	public void testSecurletDashboardExposedFilesTab_ExposuresBox_WidgetValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Exposed Files - Exposures venn diagram validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Exposed files tab: validate the exposures venn diagram tool tip data"
				+ " is matching with numbers should below to the report");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		
		securlet.clickTabSecurletDashboard(getWebDriver(),"Exposed Files");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		sAssert.assertEquals(securlet.verifyExposuresWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
		String[] textVenn = securlet.getTextFromVennCircle(getWebDriver());
		String[] textWidget = securlet.getTextFromWidget(getWebDriver());
		Integer[] numberVenn = securlet.getNumberFromVennCircle(getWebDriver());
		Integer[] numberWidget = securlet.getNumberFromWidget(getWebDriver());
		
		sAssert.assertTrue(dashboard.arrayComparison(textVenn, textWidget),
				"Verification of widget text is failing in venn and widget."
				+ "From Venn tool tip:"+textVenn+" and from widget:"+textWidget);
		sAssert.assertTrue(dashboard.arrayComparison(numberVenn, numberWidget),
				"Verification of widget numbers is failing in venn and widget."
				+ "From Venn tool tip:"+numberVenn+" and from widget:"+numberWidget);
		
		sAssert.assertAll();
	}
	
	@Priority(2)
	@Test(groups = { "regression" })
	public void testSecurletDashboardExposedFilesTab_TopRiskTypes_WidgetValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Exposed Files - Top risk types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Exposed files tab: validate the top risk types widget and values appearing is descending sorted order");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		securlet.clickTabSecurletDashboard(getWebDriver(),"Exposed Files");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		sAssert.assertEquals(securlet.verifyTopRiskTypesWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
		
		sAssert.assertAll();
	}
	
	@Priority(3)
	@Test(groups = { "regression" })
	public void testSecurletDashboardExposedFilesTab_TopContentTypes_WidgetValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Exposed Files - Top content types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Exposed files tab: validate the top content types widget and values appearing is descending sorted order");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		securlet.clickTabSecurletDashboard(getWebDriver(),"Exposed Files");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		sAssert.assertEquals(securlet.verifyTopContentTypesWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
		sAssert.assertAll();
	}
	
	@Priority(4)
	@Test(groups = { "regression" })
	public void testSecurletDashboardExposedFilesTab_TableValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Exposed Files - File list table validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Exposed files tab: validate the files table is loading with all fields like "
				+ "Document/Owner/Change Count/Size/Risks/Exposures");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		securlet.clickTabSecurletDashboard(getWebDriver(),"Exposed Files");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		sAssert.assertEquals(securlet.verifyExposedFilesListTableHeader(getWebDriver()),"",
				"Verification of exposed files table list headers is failing");
		sAssert.assertEquals(securlet.verifyExposedFilesListTableBody(getWebDriver()),"",
				"Verification of exposed files table list body is failing");
		
		sAssert.assertAll();
	}
	
	
	
	@Priority(34)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardExposedFilesTab_PopUpTable_ViewFileOpenExactFiles(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C1877368 - Exposed Files - Verify over the internally owned tab the 'View file' option to view the file(s) is accessible & redirecting to that same exact file");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Exposed Files", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedFilesTab_PopUpTable_ViewFileOpenExactFiles(getWebDriver()),"",
				"Verification of Open Exact File failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(35)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardExposedFilesTab_AllCharts_WidgetValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Exposed Files - Top content types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. C918636	Exposures - Venn Diagram is Shown");
		Logger.info("3. C918637	Top Risk Types Charts are available");
		Logger.info("4. C918638	Top Content Type Charts are available");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		securlet.clickTabSecurletDashboard(getWebDriver(),"Exposed Files");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
		
		sAssert.assertEquals(securlet.verifyTopContentTypesWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
		sAssert.assertEquals(securlet.verifyTopRiskTypesWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
		
		String[] textVenn = securlet.getTextFromVennCircle(getWebDriver());
		String[] textWidget = securlet.getTextFromWidget(getWebDriver());
		Integer[] numberVenn = securlet.getNumberFromVennCircle(getWebDriver());
		Integer[] numberWidget = securlet.getNumberFromWidget(getWebDriver());
		
		sAssert.assertTrue(dashboard.arrayComparison(textVenn, textWidget),
				"Verification of widget text is failing in venn and widget."
				+ "From Venn tool tip:"+textVenn+" and from widget:"+textWidget);
		sAssert.assertTrue(dashboard.arrayComparison(numberVenn, numberWidget),
				"Verification of widget numbers is failing in venn and widget."
				+ "From Venn tool tip:"+numberVenn+" and from widget:"+numberWidget);
		
		sAssert.assertAll();
	}
	
	@Priority(33)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardExposedFilesTab_PopupDetailsTab(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("C918740	Details Tab - All the sections under Details tab are available");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Exposed Files", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifySecurletDashboardExposedFilesTab_PopupDetailsTab(getWebDriver()),"",
				"Verification of View PopupDetailsTab failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(33)
	@Test(groups = { "regression","smoke" })
	public void testSecurletDashboardExposedFilesTab_PopupExposureTab(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("C918741 Exposure Tab - All the sections under Exposure tab are available");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Exposed Files", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Exposed Files",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifySecurletDashboardExposedFilesTab_PopupExposureTab(getWebDriver()),"",
				"Verification of View File failing");
		
		sAssert.assertAll();
		
	}
	
	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}
	
}
