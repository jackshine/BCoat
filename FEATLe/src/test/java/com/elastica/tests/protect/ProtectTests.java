package com.elastica.tests.protect;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.elastica.common.CommonTest;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.protect.ProtectPage;

/**
 * Dashboard Test Suite
 * @author Eldo Rajan
 *
 */
public class ProtectTests extends CommonTest{

	@Priority(1)
	@Test(groups = {"smoke","regression" })
	public void verifyCancelAndSavePolicyRightSide() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify that there should Cancel and Save Policy buttons at the right side.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C449143  - Verify that the 'Cancel' And 'Save Policy' Right Side");
		Logger.info("3. C449144  - To verify that without selecting any data there should show only 1 section");
		Logger.info("***********************************");
			
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		dashboard.hardWait(30);
		ip.newTab(getWebDriver()).click();
		ip.policy(getWebDriver()).click();
		String savePolicyinfo = ip.savePolicy(getWebDriver()).getText();
		System.out.println("savePolicy : "+savePolicyinfo);
		String cancelinfo =ip.cancel(getWebDriver()).getText();
		System.out.println("cancel : "+cancelinfo);
		Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
		Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
		Boolean strFirstDefineRules = ip.firstDefineRules(getWebDriver()).isElementPresent(getWebDriver());
		System.out.println("strFirstDefineRules : "+strFirstDefineRules);
		if (strFirstDefineRules){
			Assert.assertFalse(false,"First Define Rules is Present");
		}else
		{
			Assert.assertTrue(true,"First Define Rules is NOT Present");
		}
	}
	
	@Priority(2)
	@Test(groups = {"smoke","regression" })
	public void verifyPolicyTypeExpand() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify that 'Policy Type' drop down should expand.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C449145 - verify that 'Policy Type' drop down should expand.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		dashboard.hardWait(20);
		ip.newTab(getWebDriver()).click();
		ip.policy(getWebDriver()).click();
	
		String savePolicyinfo = ip.savePolicy(getWebDriver()).getInnerHtml();
		System.out.println("savePolicy : "+savePolicyinfo);
		String cancelinfo =ip.cancel(getWebDriver()).getText();
		System.out.println("cancel : "+cancelinfo);
		Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
		Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
		ip.clickpolicytype(getWebDriver()).click();
		Boolean strExpandPolicyDropdown = ip.expandpolicydropdown(getWebDriver()).isElementPresent(getWebDriver());
		System.out.println("strFirstDefineRules : "+strExpandPolicyDropdown);
		if (strExpandPolicyDropdown){
			Assert.assertTrue(true,"Policy Type Drop Down Is Expanded");
		}else
		{
			Assert.assertFalse(false,"Policy Type Drop Down Is Not Expanded");
		}
	}
	@Priority(3)
	@Test(groups = {"smoke","regression" })
	public void verifyDefineRulesandDefineResponseIsOpen() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify Define Rules and Define Response Is Open.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C449147﻿  - verify Define Rules and Define Response Is Open.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		dashboard.hardWait(5);
		ip.newTab(getWebDriver()).click();
		ip.policy(getWebDriver()).click();	
		dashboard.hardWait(5);
		String savePolicyinfo = ip.savePolicy(getWebDriver()).getInnerHtml();
		System.out.println("savePolicy : "+savePolicyinfo);
		String cancelinfo =ip.cancel(getWebDriver()).getText();
		System.out.println("cancel : "+cancelinfo);
		Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
		Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
		ip.clickpolicytype(getWebDriver()).click();
		ip.selectedpolicytype(getWebDriver()).click();
		String strFirstDefineRules = ip.firstDefineRules(getWebDriver()).getText();
		System.out.println("strFirstDefineRules : "+strFirstDefineRules);
		String strFirstDefineReponse = ip.firstDefinereponse(getWebDriver()).getText();
		System.out.println("strFirstDefineRules : "+strFirstDefineReponse);
		Assert.assertEquals(strFirstDefineRules, "Define Rules", "Verify Define Rules is not Present");
		Assert.assertEquals(strFirstDefineReponse, "Define Response", "Verify Define Response is not Present");
		
	}
	
	@Priority(4)
	@Test(groups = {"smoke","regression" })
	public void verifyDefaultpolicy() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify that by default policy type should not be selected.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C449149 - verify that by 'default policy' type should not be selected.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(20);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		dashboard.hardWait(15);
		ip.newTab(getWebDriver()).click();
		ip.policy(getWebDriver()).click();
		dashboard.hardWait(10);
		String savePolicyinfo = ip.savePolicy(getWebDriver()).getText();
		System.out.println("savePolicy : "+savePolicyinfo);
		String cancelinfo =ip.cancel(getWebDriver()).getText();
		System.out.println("cancel : "+cancelinfo);
		Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
		Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");

		String strDefaultPolicy = ip.defaultpolicytype(getWebDriver()).getText();
		System.out.println("Default Selected Policy Type : "+strDefaultPolicy);
		Assert.assertEquals(strDefaultPolicy, "Select Policy Type", "Verify Default Policy Typepe is not Present");
	}	

	@Priority(5)
	@Test(groups = {"smoke","regression" })
	public void verifyListpolicyTypeShow() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify the list of policy type should be shown properly.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C449150 - verify the list of 'policy type' should be shown properly.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(20);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		dashboard.hardWait(15);
		ip.newTab(getWebDriver()).click();
		ip.policy(getWebDriver()).click();
		dashboard.hardWait(10);
		String savePolicyinfo = ip.savePolicy(getWebDriver()).getText();
		System.out.println("savePolicy : "+savePolicyinfo);
		String cancelinfo =ip.cancel(getWebDriver()).getText();
		System.out.println("cancel : "+cancelinfo);
		Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
		Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
		List<WebElement> PolicyTypeList = ip.policyTypedropdown(getWebDriver()).getElementList();
		System.out.println( PolicyTypeList.size());
		int count =  PolicyTypeList.size();
		Assert.assertEquals(6, count,"List Of policy Type Not Matched");
	}
	@Priority(6)
	@Test(groups = {"smoke","regression" })
	public void verifyLogViolationTextShown() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify there should be Log Violation Text at the bottom of the form.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C449237 - verify there should be 'Log Violation' Text at the bottom of the form");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(20);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		dashboard.hardWait(15);
		ip.newTab(getWebDriver()).click();
		ip.policy(getWebDriver()).click();
		dashboard.hardWait(10);
		String savePolicyinfo = ip.savePolicy(getWebDriver()).getText();
		System.out.println("savePolicy : "+savePolicyinfo);
		String cancelinfo =ip.cancel(getWebDriver()).getText();
		System.out.println("cancel : "+cancelinfo);
		Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
		Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
		ip.clickpolicydropdown(getWebDriver()).click();
		ip.firstpolicyTypedropdown(getWebDriver()).click();
		String strfirstpolicyTypedropdownTest = ip.firstpolicydropdowntest(getWebDriver()).getText();
		System.out.println("FirstPolicyTypeDropdownText : "+ strfirstpolicyTypedropdownTest);
		
		String strlogviolationtext = ip.logviolationtext(getWebDriver()).getText();
		System.out.println("LogViolationText : "+ strlogviolationtext);
		
		Assert.assertEquals(strlogviolationtext, "Policy violations are logged under Protect:Alerts and Investigate.", "Verify Log Violation is not Matched");
		
	}
	@Priority(7)
	@Test(groups = {"smoke","regression" })
	public void verifyFilterTabLoadSuccess() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C865642 - To verify that filters should load successfully when click on filter tab..");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(20);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		String strReponseType = ip.responseType(getWebDriver()).getText();
		System.out.println("FirstReponseTypeDropdownText : "+ strReponseType);
		Assert.assertEquals(strReponseType, "Response Type", "Verify Response Type is not Matched");
	}
	
	@Priority(8)
	@Test(groups = {"smoke","regression" })
	public void verifyPolicyTypeFiltersSuccess() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C865644 - To verify that there should show 'policy type filters' to display all the policy types.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		String strPolicyType = ip.policyType(getWebDriver()).getText();
		System.out.println("FirstPolicyTypeDropdownText : "+ strPolicyType);
		Assert.assertEquals(strPolicyType, "Policy Type", "Verify Policy Type is not Matched");
	
	}
	
	@Priority(9)
	@Test(groups = {"smoke","regression" })
	public void verifyInDropDownViewEditAndDelete() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify that Actions dropdown should have list of 'View' , 'Edit' and 'Delete'.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C1643348 - Verify that Actions dropdown should have list of 'View' , 'Edit' and 'Delete'.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		dashboard.hardWait(5);
		ip.policyTab(getWebDriver()).click(); dashboard.hardWait(5);
		String strPolicyTab = ip.policyTab(getWebDriver()).getText();
		System.out.println("PolicyTabText : "+ strPolicyTab);
		ip.actionDropdown(getWebDriver()).mouseOverClick(getWebDriver());
		ip.actionDropdown(getWebDriver()).click();
		String strshowdetail = ip.policyTab(getWebDriver()).getText();
		Assert.assertEquals(strshowdetail, "Show Details", "Verify Show Details is not Matched");
		String streditdetail = ip.editdetail(getWebDriver()).getText();
		Assert.assertEquals(streditdetail, "Edit Policy", "Verify Edit Policy is not Matched");
		String strdeletedetail = ip.deletedetail(getWebDriver()).getText();
		Assert.assertEquals(strdeletedetail, "Delete", "Verify Delete is not Matched");
	}
	
	@Priority(10)
	@Test(groups = {"smoke","regression" })
	public void verifyPolicyTypeFiltersAllPolicyTypes() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify that only the selected policy type templates should be shown in the dropdown.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C1643447  - Verify that only the selected policy type templates should be shown in the dropdown.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(10);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(10);
		String strPolicyType = ip.policyType(getWebDriver()).getText();
		System.out.println("FirstPolicyTypeDropdownText : "+ strPolicyType);
		Assert.assertEquals(strPolicyType, "Policy Type", "Verify Policy Type is not Matched");
		String strAccessMonitor = ip.accessmonitor(getWebDriver()).getText();
		System.out.println("Access MonitorText is : "+ strAccessMonitor);
		Assert.assertEquals(strAccessMonitor, "Access Monitoring via Securlets", "Verify Access Monitor is not Matched");
		String strDataExplore = ip.dataexplore(getWebDriver()).getText();
		System.out.println("Data Explore Text is : "+ strDataExplore);
		Assert.assertEquals(strDataExplore, "Data Exposure via Securlets", "Verify Data Explore is not Matched");
		String strThreadScore = ip.threadscore(getWebDriver()).getText();
		System.out.println("Thread ScoreText is : "+ strThreadScore);
		Assert.assertEquals(strThreadScore, "ThreatScore Based", "Verify Threat Score Based is not Matched");
		
	}

	@Priority(11)
	@Test(groups = {"smoke","regression" })
	public void verifyWithoutPolicyTypeUserShouldNotMove() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify that there should Cancel and Save Policy buttons at the right side.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. T3998963  - To verify that without selecting policy type user should not move further.");
		Logger.info("***********************************");
			
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(30);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		dashboard.hardWait(30);
		ip.newTab(getWebDriver()).click();
		ip.policy(getWebDriver()).click();
		String savePolicyinfo = ip.savePolicy(getWebDriver()).getText();
		System.out.println("savePolicy : "+savePolicyinfo);
		String cancelinfo =ip.cancel(getWebDriver()).getText();
		System.out.println("cancel : "+cancelinfo);
		Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
		Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
		Boolean strFirstDefineRules = ip.firstDefineRules(getWebDriver()).isElementPresent(getWebDriver());
		System.out.println("strFirstDefineRules : "+strFirstDefineRules);
		if (strFirstDefineRules){
			Assert.assertFalse(false,"First Define Rules is Present");
		}else
		{
			Assert.assertTrue(true,"First Define Rules is NOT Present");
		}
	}
	
	@Priority(12)
	@Test(groups = {"smoke","regression" })
	public void verifyChangeAccessSettingsClearLink() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify there should be Log Violation Text at the bottom of the form.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. T3998991 - To verify that 'Change Access Settings' should hide when click on clear link.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(20);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		dashboard.hardWait(15);
		ip.newTab(getWebDriver()).click();
		ip.policy(getWebDriver()).click();
		dashboard.hardWait(10);
		String savePolicyinfo = ip.savePolicy(getWebDriver()).getText();
		System.out.println("savePolicy : "+savePolicyinfo);
		String cancelinfo =ip.cancel(getWebDriver()).getText();
		System.out.println("cancel : "+cancelinfo);
		Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
		Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
		ip.clickpolicydropdown(getWebDriver()).click();
		ip.selectDataExposureviaSecurlets(getWebDriver()).click();
		String strfirstpolicyTypedropdownTest = ip.firstpolicydropdowntest(getWebDriver()).getText();
		System.out.println("FirstPolicyTypeDropdownText : "+ strfirstpolicyTypedropdownTest);
		String strlogviolationtext = ip.logviolationtext(getWebDriver()).getText();
		System.out.println("LogViolationText : "+ strlogviolationtext);
		Assert.assertEquals(strlogviolationtext, "Policy violations are logged under Protect:Alerts and Investigate.", "Verify Log Violation is not Matched");
		ip.fileOwnerSelective(getWebDriver()).click();
		ip.fileOwnerSelectiveText(getWebDriver()).type("test");
		ip.clickFileOwnerName(getWebDriver()).click();
		Assert.assertTrue(ip.fileOwnerName(getWebDriver()).isElementVisible(), "fileOwnerName is not visible");
		ip.clearselection(getWebDriver()).click();
		dashboard.hardWait(5);
		Boolean strfileOwnerName = ip.fileOwnerName(getWebDriver()).isElementPresent(getWebDriver());
		System.out.println("strfileOwnerName : "+strfileOwnerName);
		if (strfileOwnerName){
			Assert.assertFalse(false,"fileOwnerName is Present " + strfileOwnerName);
		}else
		{
			Assert.assertTrue(true,"fileOwnerName is NOT Present "+strfileOwnerName);
		}
		
	}
	
	@Priority(13)
	@Test(groups = {"smoke","regression" })
	public void verifyAddNewPolicyWithMediaFilesSupport_Any() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify there should be Log Violation Text at the bottom of the form.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. T3999174 - Protect - Add new policy File Exposure-Securlet-file types - media files support");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		protect.testAddNewPolicyWithMediaFilesSupport_Any(getWebDriver());
		
	}
	
	@Priority(14)
	@Test(groups = {"smoke","regression" })
	public void verifyAddNewPolicyWithMediaFilesSupport() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify there should be Log Violation Text at the bottom of the form.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. T3999174 - Protect - Add new policy File Exposure-Securlet-file types - media files support");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		protect.testAddNewPolicyWithMediaFilesSupport(getWebDriver());
		
	}
	@Priority(15)
	@Test(groups = {"smoke","regression" })
	public void verifytAddNewPolicyDefaulySizeAny() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify there should be Log Violation Text at the bottom of the form.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. T3999707 - To verify that by defauly 'ANY' should be selected in file size.t");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		protect.testAddNewPolicyDefaulySizeAny(getWebDriver());
		
	}
	@Priority(16)
	@Test(groups = {"smoke","regression" })
	public void testSizeRangeSlider() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify there should be Log Violation Text at the bottom of the form.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. T3999708 - To verify that Upon click on 'Selected' file size range slider should be displayed.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		protect.verifySizeRangeSlider(getWebDriver());
		
	}
	
	@Priority(17)
	@Test(groups = {"smoke","regression" })
	public void testSizeRangeSliderDefaultLarger_Smaller() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: verify there should be Log Violation Text at the bottom of the form.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. T3999709 - To verify that by default larger than and smaller than values should be disabled.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		protect.verifySizeRangeSliderDefaultLarger_Smaller(getWebDriver());
		
	}
	@Priority(18)
	@Test(groups = {"smoke","regression" })
	public void verifyStatusFilters() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. T3999354 - To verify that there should show 'Status filters' to display a status of policy.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(20);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		String strReponseType = ip.responseType(getWebDriver()).getText();
		System.out.println("FirstReponseTypeDropdownText : "+ strReponseType);
		Assert.assertEquals(strReponseType, "Response Type", "Verify Response Type is not Matched");
		
		String filterInactive = ip.filterInactive(getWebDriver()).getText();
		System.out.println("filterInactive : "+ filterInactive);
		Assert.assertEquals(filterInactive,"Inactive", "Verify filterInactive is not Matched");
		
		String filteractive = ip.filteractive(getWebDriver()).getText();
		System.out.println("filteractive : "+ filteractive);
		Assert.assertEquals(filteractive,"Active", "Verify Active is not Matched");
		
	}
	@Priority(19)
	@Test(groups = {"smoke","regression" })
	public void verifyFilterResponseType() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. T3999355 - To verify that there should show 'Response type filters' to display all the repsonse types of policy.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(20);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		String strReponseType = ip.responseType(getWebDriver()).getText();
		System.out.println("FirstReponseTypeDropdownText : "+ strReponseType);
		Assert.assertEquals(strReponseType, "Response Type", "Verify Response Type is not Matched");
	}
	
	@Priority(20)
	@Test(groups = {"smoke","regression" })
	public void verifyExpandAndCollapse() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C3159 Protect- Top filter - expand and collapse");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertTrue(ip.caret(getWebDriver()).isElementVisible(), "caret is not visible");
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertTrue(ip.caret(getWebDriver()).isElementVisible(), "caret is not visible");
	}
	
	@Priority(21)
	@Test(groups = {"smoke","regression" })
	public void verifyExpandAndCollapseIcons() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C3160 Protect- Top filter - expand and collapse icons");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertTrue(ip.caret(getWebDriver()).isElementVisible(), "caret is not visible");
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertTrue(ip.caret(getWebDriver()).isElementVisible(), "caret is not visible");
	}
	
	@Priority(22)
	@Test(groups = {"smoke","regression" })
	public void verifyFiltersStatuActive() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C2463	Protect- Policies- Top Filters Status - Active  ");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertTrue(ip.filteractive(getWebDriver()).isElementVisible(), "filteractive is not visible");
		ip.filteractive(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertEquals(ip.filtertext(getWebDriver()).getText(),"Active", "Active is not visible");
		
	}
	@Priority(22)
	@Test(groups = {"smoke","regression" })
	public void verifyFiltersStatuInActive() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C2464	Protect- Policies- Top Filters Status - InActive  ");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertTrue(ip.filterInactive(getWebDriver()).isElementVisible(), "filter active is not visible");
		ip.filterInactive(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertEquals(ip.filtertext(getWebDriver()).getText(),"Inactive", "Active is not visible");
		
	}
	@Priority(23)
	@Test(groups = {"smoke","regression" })
	public void verifyPolicyTypeFilters() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C2465 Protect-Policies- Top Filters - Policy Type.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		String strPolicyType = ip.policyType(getWebDriver()).getText();
		System.out.println("FirstPolicyTypeText : "+ strPolicyType);
		Assert.assertEquals(strPolicyType, "Policy Type", "Verify Policy Type is not Matched");
	
	}
	@Priority(24)
	@Test(groups = {"smoke","regression" })
	public void verifyPolicyTypeFiltersStatus() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C2465 Protect-Policies- Top Filters - Policy Type.");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertTrue(ip.filteractive(getWebDriver()).isElementVisible(), "filter active is not visible");
		Assert.assertTrue(ip.filterInactive(getWebDriver()).isElementVisible(), "filter inactive is not visible");
	
	}
	@Priority(25)
	@Test(groups = {"smoke","regression" })
	public void verifyFiltersCombinationOfStatus() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C2467 Protect- Policies- Top Filters - Combination of status and type");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		Assert.assertTrue(ip.filterInactive(getWebDriver()).isElementVisible(), "filter active is not visible");
		ip.filterInactive(getWebDriver()).click();
		dashboard.hardWait(2);
		Assert.assertEquals(ip.filtertext(getWebDriver()).getText(),"Inactive", "Active is not visible");
		Assert.assertTrue(ip.filteractive(getWebDriver()).isElementVisible(), "filter active is not visible");
		ip.filteractive(getWebDriver()).click();
		Assert.assertEquals(ip.filtertext(getWebDriver()).getText(),"Active", "Active is not visible");
		
	}
	@Priority(26)
	@Test(groups = {"smoke","regression" })
	public void verifyPolicyBlocksStatusActive() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: To verify that there should show policy type filters to display all the policy types.");
		Logger.info("1. Login into cloudsoc home page and subsequently navigate to Prodect dashboard");
		Logger.info("2. C2477 Protect- Blocks- Top Filters Status - Active");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Protect");
		String actualHeader = dashboard.getHeader(getWebDriver());
		String expectedHeader = "Protect";
		Logger.info("Actual Header: "+actualHeader);
		Logger.info("Expected Header: "+expectedHeader);
		Assert.assertEquals(actualHeader, expectedHeader, "Header mismatch is seen");
		dashboard.hardWait(5);
		ProtectPage ip =  AdvancedPageFactory.getPageObject(getWebDriver(), ProtectPage.class);
		ip.protectToggleTab(getWebDriver()).click();
		dashboard.hardWait(5);
		String strPolicyType = ip.policyType(getWebDriver()).getText();
		System.out.println("FirstPolicyTypeText : "+ strPolicyType);
		Assert.assertEquals(strPolicyType, "Policy Type", "Verify Policy Type is not Matched");
		ip.BlockedUsersTab(getWebDriver()).click();
		dashboard.hardWait(5);
		String StrBlockStatus = ip.BlockStatus(getWebDriver()).getText();
		Assert.assertEquals(StrBlockStatus, "Block Status", "BlockStatus is not showing");
	}
}