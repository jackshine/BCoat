package com.elastica.action.pingone;

import org.openqa.selenium.WebDriver;
import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.PingOnePage;

public class PingOneAction extends Action{

	public void loginPortal(WebDriver driver, SuiteData testData) throws InterruptedException {
		PingOnePage pop = AdvancedPageFactory.getPageObject(driver, PingOnePage.class);
		driver.get(testData.getSaasAppBaseUrl() +"/login");
		pop.adminusername(driver).waitForElementPresent(driver);
		pop.adminusername(driver).waitForElementToBeVisible(driver);
		pop.adminusername(driver).clear();pop.adminusername(driver).type(testData.getSaasAppUsername());
		pop.adminpassword(driver).clear();pop.adminpassword(driver).type(testData.getSaasAppPassword());
		pop.adminsubmitbutton(driver).click();
		hardWait(10);
	}
	
	public String getSamlDataUrl(WebDriver driver, SuiteData testData) throws InterruptedException {
		PingOnePage pop = AdvancedPageFactory.getPageObject(driver, PingOnePage.class);
		driver.get(testData.getSaasAppBaseUrl() + "/cas/connections");
		pop.application(driver).click();
		Thread.sleep(5000);
		 pop.downloadlink(driver).click();
		String downloadUrl = pop.downloadlink(driver).getAttribute("href");
//		Logger.info("Download Url" + downloadUrl);
		return downloadUrl;
	}
	

}
