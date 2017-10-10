package com.elastica.tests.dashboard;

import org.openqa.selenium.Cookie;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.elastica.action.login.LoginAction.UserType;
import com.elastica.common.CommonTest;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

/**
 * Dashboard Login Test Suite
 * @author Eldo Rajan
 *
 */
public class DashboardLoginTests extends CommonTest{

	@Priority(1)
	@Test(groups = { "sanity","smoke","regression" })
	public void testLoginAsSysAdmin() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as SysAdmin and dashboard is loading");
		Logger.info("1. Login into cloudsoc home page as SysAdmin");
		Logger.info("2. Verify dashboard is loading properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData, UserType.SysAdmin, "Dashboard");
	}

	@Priority(2)
	@Test(groups = { "sanity","smoke","regression" })
	public void testLoginAsAdmin() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as Admin and dashboard is loading");
		Logger.info("1. Login into cloudsoc home page as Admin");
		Logger.info("2. Verify dashboard is loading properly");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData, UserType.Admin, "Dashboard");
	}

	@Priority(3)
	@Test(groups = { "sanity","smoke","regression" })
	public void testLoginAsDataProtectionOfficer() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as Data Protection Officer and user profile page is loading");
		Logger.info("1. Login into cloudsoc home page as DPO");
		Logger.info("2. Verify user profile page is loading");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData, UserType.DPO, "User Profile");
	}

	@Priority(4)
	@Test(groups = { "sanity","smoke","regression" })
	public void testLoginAsEndUser() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify login as End User and user profile page is loading");
		Logger.info("1. Login into cloudsoc home page as end user");
		Logger.info("2. Verify user profile page is loading");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData, UserType.EndUser, "User Profile");
	}


	/*@AfterMethod(alwaysRun=true)
	public void testData(){
		getWebDriver().manage().deleteAllCookies();
		getWebDriver().manage().addCookie(new Cookie("mf_authenticated",suiteData.getAccessToken()));
	}*/

}
