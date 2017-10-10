package com.elastica.tests.audit;

import java.io.File;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.common.CommonTest;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.audit.AuditPage;

/**
 * Audit Test Suite
 * @author Eldo Rajan
 *
 */
public class AuditTestsNew extends CommonTest{
	SoftAssert sAssert = null;
	

	@Priority(24)
	@Test(groups = { "smoke","test" })
	public void testAuditDashboard() {
		
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Go to audit, select data range filter in one tab "
				+ "and verify it is same in all other tabs");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to dashboard verify the dashboard");
		Logger.info("***********************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), "Audit");
		audit.validateAuditPage(getWebDriver());
		validateAuditPageSummaryScreensTop(getWebDriver());
		
		
	}

	public void validateAuditPageSummaryScreensTop(WebDriver driver) {
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			
			String FolderPathImage1 = System.getProperty("user.dir")+File.separator+"screenshots/AuditSummaryTop.png";
			//Pattern baseImagePattern = new Pattern(imagePath1);
			Pattern capturedImagePattern = new Pattern(FolderPathImage1);
			System.out.println(capturedImagePattern);
			Screen screen=new Screen();
			
			//Match pass = screen.exists(capturedImagePattern);
			System.out.println(screen);
			if (screen.exists(capturedImagePattern) != null) {
			     System.out.println("true");
			}
			else{
			     System.out.println("false");
			}
			
		} catch (Exception ex) {
			Assert.fail("Tesing " + ex.getLocalizedMessage());
		}
	}

	/*********************************************************************/
	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}
	/*********************************************************************/
	
	
}
