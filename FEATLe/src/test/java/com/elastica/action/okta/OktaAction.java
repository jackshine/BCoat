package com.elastica.action.okta;

import java.util.Date;

import org.openqa.selenium.WebDriver;

import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.OktaPage;

public class OktaAction extends Action{
	String token = "";

	public void loginPortal(WebDriver driver, SuiteData testData) throws InterruptedException {
		OktaPage okta =  AdvancedPageFactory.getPageObject(driver, OktaPage.class);
		driver.get(testData.getSaasAppBaseUrl());
		okta.username(driver).waitForElementPresent(driver);
		okta.username(driver).waitForElementToBeVisible(driver);
		okta.username(driver).clear();okta.username(driver).type(testData.getSaasAppUsername());
		okta.password(driver).clear();okta.password(driver).type(testData.getSaasAppPassword());
		okta.signIn(driver).click();
		Thread.sleep(5000);
	}
	
	
	public void deactiveAppIfFound(WebDriver driver, SuiteData testData) throws InterruptedException {
		OktaPage okta =  AdvancedPageFactory.getPageObject(driver, OktaPage.class);
		String link = "https://" +  testData.getSaasAppBaseDomain() + ".okta.com/admin/apps/active";
		try {
			driver.get(link);
			if(okta.adminactivebutton(driver).isElementPresent(driver)) {
				okta.adminactivebutton(driver).click();
				okta.admindeactivebutton(driver).waitForLoading(driver);
				okta.admindeactivebutton(driver).click();
				okta.admindeactivedialogbutton(driver).waitForLoading(driver);
				okta.admindeactivedialogbutton(driver).click();
				Thread.sleep(15000);
			}
		} catch(Exception e) {
			
		}
	}
	
	public void addApplication(WebDriver driver, SuiteData testData) throws InterruptedException {
		OktaPage okta =  AdvancedPageFactory.getPageObject(driver, OktaPage.class);
		String link = "https://" +  testData.getSaasAppBaseDomain() + ".okta.com" + "/admin/access/api";
		Logger.info("Get API link " + testData.getSaasAppBaseUrl() );
		driver.get(link);
		Thread.sleep(5000);
		while(true) {
			if (okta.deleteTokenLink(driver).isElementVisible()) {
				okta.deleteTokenLink(driver).click();
				okta.deleteButton(driver).click();
				Thread.sleep(5000);
			} else {
				break;
			}
		}
		if (testData.getSaasAppToken().length() == 0) {
			okta.createTokenButton(driver).click();
			okta.tokenNameTexbox(driver).type("New Token" + String.format("%tR", new Date()) + testData.getEnvName());
			okta.createtokensubmit(driver).click();
			
			okta.tokenValue(driver).waitForElementPresent(driver);
			okta.tokenValue(driver).waitForElementToBeVisible(driver);
			testData.setSaasAppToken(okta.tokenValue(driver).getText());
			Logger.info("Token is  : " +testData.getSaasAppToken() );
			okta.okgotitButton(driver).click();
		}
		
	}
	
	public void signOut(WebDriver driver, SuiteData testData) {
		String link = "https://" +  testData.getSaasAppBaseDomain() + ".okta.com"+ "/login/admin/signout";
		driver.get(link);
	}
}
