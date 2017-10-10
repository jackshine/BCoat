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
public class SecurletOtherRisksTabsTests extends CommonTest{

	SoftAssert sAssert = null;
	
	@Priority(4)
	@Test(groups = { "regression","sanityBox" })
	public void testOtherRisksTab_ExportCSV() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the ExportCSV button and alert message");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToSecurletDashboardTabs(getWebDriver(), suiteData, "Other Risks");
		
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyOtherRisksTab_ExportCSV_AlertMessage(getWebDriver()),"",
				"Verification of Export CSV is failing");
		
		sAssert.assertAll();
		
	}
	
	
	
	@Priority(1)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_TopRiskTypes_WidgetValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top risk types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the top risk types widget and values appearing is descending sorted order");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyOtherRisks_TopRiskTypesWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(2)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_TopContentTypes_WidgetValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top content types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the top content types widget and values appearing is descending sorted order");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyOtherRisks_TopContentTypesWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(3)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_TopFileTypes_WidgetValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the top files types widget and values appearing is descending sorted order");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyOtherRisks_TopFileTypesWidget(getWebDriver()),"",
				"Verification of widgets is failing");
		
		sAssert.assertAll();
		
	}
	
	
	
	
	@Priority(5)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_TableValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Exposed Files - File list table validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the files table is loading with all fields like "
				+ "Document/Owner/Change Count/Size/Risks/Exposures");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");		
		sAssert.assertEquals(securlet.verifyOtherRisksTableHeader(getWebDriver()),"",
				"Verification of OtherRisks files table list headers is failing");
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody(getWebDriver()),"",
				"Verification of OtherRisks files table list body is failing");
		
		sAssert.assertAll();
	}
	
	@Priority(6)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_TableDetailPageValidation() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Exposed Files - File list table validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the files table is loading with all fields like "
				+ "Document/Owner/Change Count/Size/Risks/Exposures");
		Logger.info("3. Click on Document link : Vaidate the files on pop is loading with all fields ");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");	
		sAssert.assertEquals(securlet.verifyOtherRisksTableHeader(getWebDriver()),"",
				"Verification of OtherRisks files table list headers is failing");
		sAssert.assertEquals(securlet.verifyOtherRisksTableDetailPage(getWebDriver()),"",
				"Verification of OtherRisks files table list body is failing");
		
		sAssert.assertAll();
	}
	
	
	@Priority(7)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_TableFilter_Document(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the  Table Filter");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_FilterDocument(getWebDriver()),"",
				"Verification of Show Filter Documen failing");
		
		sAssert.assertAll();
		
	}
	@Priority(8)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_TableFilter_Owner(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the  Table Filter");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_FilterOwner(getWebDriver()),"",
				"Verification of Show Filter Documen failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(9)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_TableFilter_Size(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the  Table Filter");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_FilterSize(getWebDriver()),"",
				"Verification of Show Filter Documen failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(10)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_FilterToggleTab(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the  Filter Toggle Tab");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(),"Other Risks", appCount, sCount);
		
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_FilterToggleTab(getWebDriver(),suiteData.getSaasAppName()),"",
				"Verification of Show Filter Documen failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(11)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_TableSort(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the  Sort in Table");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_SizeSort(getWebDriver()),"",
				"Verification of Table Size Sort failing");
		
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_OwnerSort(getWebDriver()),"",
				"Verification of Table Owenr Sort failing");
		
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_DocumentSort(getWebDriver()),"",
				"Verification of Table Document Sort failing");
		
		
		sAssert.assertAll();
		
	}
	
	@Priority(12)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_FiltersContentTypeBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the Content Type Search Box");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_SearchBox_ContentType(getWebDriver()),"",
				"Verification of Search Box failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(13)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_FiltersFileTypesBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the File Types Search Box");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_SearchBox_FileTypes(getWebDriver()),"",
				"Verification of Search Box failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(14)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_FiltersContentIQProfileBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the ContentIQ Profile Search Box");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_SearchBox_ContentIQProfile(getWebDriver()),"",
				"Verification of SearchBox ContentIQProfile failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(15)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_FiltersRiskTypeBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the Risk Type Search Box");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_SearchBox_RiskType(getWebDriver()),"",
				"Verification of SearchBox ContentIQProfile failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(16)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_FiltersTopRiskTypesBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the  Top Risk Type Search Box");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_SearchBoxTopRiskTypes(getWebDriver()),"",
				"Verification of TopRiskType failing");
		
		sAssert.assertAll();
		
	}
	@Priority(17)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_FiltersTopFileTypesBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the  Top File Types Search Box");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_SearchBoxTopFileTypes(getWebDriver()),"",
				"Verification of TopRiskType failing");
		
		sAssert.assertAll();
		
	}
	
	@Priority(18)
	@Test(groups = { "regression" })
	public void testSecurletDashboardOtherRisksTab_FiltersTopContentTypeBox(){
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Other risks - Top files types widget validation");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to securlet dashboard");
		Logger.info("2. Other risks tab: validate the TopContentType Search Box");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), "Securlet");
		int sCount = dashboard.hoverOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
		dashboard.clickOnSidebarDeepMenuLinks(getWebDriver(), "Other Risks", appCount, sCount);
		sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
				"Securlet header mismatch is seen");
		sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),"Other Risks",
				"Active tab header text mismatch is seen");
	
		sAssert.assertEquals(securlet.verifyExposedOtherRisksTableBody_TapContentType(getWebDriver()),"",
				"Verification of Search Box failing");
		
		sAssert.assertAll();
		
	}
	
	
	
	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}
	
}
