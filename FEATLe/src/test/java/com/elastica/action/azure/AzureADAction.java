package com.elastica.action.azure;

import org.openqa.selenium.WebDriver;
import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.AzureADPage;
import com.elastica.pageobjects.SettingsPage;


public class AzureADAction extends Action{

	public void login(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		AzureADPage azp =  AdvancedPageFactory.getPageObject(driver, AzureADPage.class);
		azp.emailId(driver).clear();azp.emailId(driver).type(suiteData.getSaasAppUsername());
		azp.password(driver).clear();azp.password(driver).type(suiteData.getSaasAppPassword());
		azp.loginButton(driver).click();
		Thread.sleep(5000);
	}
	
	public void grantAccess(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		AzureADPage azp =  AdvancedPageFactory.getPageObject(driver, AzureADPage.class);
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		String title = driver.getTitle();
		sp.idpConfigureButton(driver).click();
		Thread.sleep(5000);
		switchToWindowByTitle("Sign in to Microsoft App Access Panel", driver);
		azp.emailId(driver).waitForElementPresent(driver, 60);
		// Sign using Microsoft email id
		azp.emailId(driver).type(suiteData.getSaasAppUsername());
		azp.loginButton(driver).click();
		//Select Microsoft Account
		azp.microsoftAccount(driver).click();
		azp.username(driver).waitForElementPresent(driver, 60);
		azp.username(driver).type(suiteData.getSaasAppUsername());
		azp.password(driver).type(suiteData.getSaasAppPassword());
		azp.signinbutton(driver).click();
		// Click Grant Access By Switch in iframe
		switchFrame(driver.findElement(azp.getBy(azp.grantaccessiframe(driver))), driver);
		azp.grantaccessbutton(driver).waitForElementPresent(driver, 60);
		azp.grantaccessbutton(driver).click();
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		//Logout 
		//driver.get(suiteData.getSsoBaseurl() + "/Account/Logout");
		//Thread.sleep(5000);
		// Go To CloudSoc
		//driver.get(suiteData.getBaseUrl());
		//Thread.sleep(5000);
		switchToWindowByTitle(title, driver);
		Logger.info("Granted Access");
	}

}	