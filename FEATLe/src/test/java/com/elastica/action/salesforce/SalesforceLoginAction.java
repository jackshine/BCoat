package com.elastica.action.salesforce;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.LoginPage;
import com.elastica.pageobjects.OneLoginPage;
import com.elastica.pageobjects.SalesforceLoginPage;
import com.universal.common.Office365MailActivities;

import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.property.complex.ItemId;

public class SalesforceLoginAction extends Action{

	
	
	public void login(WebDriver driver, SuiteData suiteData) throws ServiceLocalException, Exception{
	
		SalesforceLoginPage lp =  AdvancedPageFactory.getPageObject(driver,SalesforceLoginPage.class);
		hardWait(5);
		driver.get(suiteData.getSaasAppBaseUrl());
		hardWait(4);
		driver.get(suiteData.getSaasAppBaseUrl());
		System.out.println(suiteData.getTestUsername());
		System.out.println(suiteData.getTestPassword());
		lp.username(driver).clear();lp.username(driver).type(suiteData.getSaasAppUsername());
		lp.password(driver).clear();lp.password(driver).type(suiteData.getSaasAppPassword());
		hardWait(5);
		lp.loginButton(driver).mouseOver(driver);
		lp.loginButton(driver).mouseOverClick(driver);
		//lp.loginButton(driver).click();
		hardWait(10);
		
		String username = suiteData.getSaasAppUsername();String password=suiteData.getSaasAppPassword();
        Office365MailActivities o365 = new Office365MailActivities(username, password);
        //microsoft.exchange.webservices.data.core.service.item.Item
        Item item = o365.findItemInDecending("Verify your identity in Salesforce");
        ItemId id = item.getId();
        EmailMessage emailMessage = o365.getEmailMessage(id.getUniqueId());
        String htmlContent=emailMessage.getBody().toString();
        String codeString = "Verification Code: ";
        String verificationCode = htmlContent.substring(htmlContent.indexOf(codeString)+codeString.length(), htmlContent.indexOf(codeString)+codeString.length()+6);
        Logger.info("Entering the verification code: "+verificationCode);
        lp.verifyCode(driver).waitForLoading(driver);
        lp.verifyCode(driver).type(verificationCode);
        //lp.verifyButton(driver).mouseOver(driver);
        //lp.verifyButton(driver).mouseOverClick(driver);
	}
	
	
	public void relogin(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		SalesforceLoginPage lp =  AdvancedPageFactory.getPageObject(driver,SalesforceLoginPage.class);
		driver.get(suiteData.getBaseUrl());
		Thread.sleep(10000);
		lp.username(driver).clear();lp.username(driver).type(suiteData.getTestUsername());
		lp.password(driver).clear();lp.password(driver).type(suiteData.getTestPassword());
		Thread.sleep(5000);
		lp.loginButton(driver).mouseOver(driver);
		lp.loginButton(driver).mouseOverClick(driver);
		//lp.loginButton(driver).click();
		Thread.sleep(15000);
	}
	
	public void logout(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		
//		O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
//		lp.userProfileButton(driver).mouseOverClick(driver);
//		lp.logoutButton(driver).mouseOverClick(driver);
		//lp.loginButton(driver).click();
		//Thread.sleep(15000);
		 driver.get("https://na34.salesforce.com/secur/logout.jsp");
		//Assert.assertTrue(dp.header(driver).isElementVisible(), "Dashboard Page: Header is not visible");
		//Assert.assertEquals(dp.header(driver).getText().trim(), "Dashboard", "Dashboard Page: Header text is not matching");
	}
	public String getSignInLabelText(WebDriver driver) {
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		return lp.signInLabel(driver).getText().trim();
	}
	
	public void loginViaSSO(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		OneLoginPage ollp = AdvancedPageFactory.getPageObject(driver, OneLoginPage.class);
		lp.singleSignOnLink(driver).mouseOver(driver);
		lp.singleSignOnLink(driver).click();
		Thread.sleep(5000);
		Assert.assertTrue(lp.username(driver).isElementVisible(), "Login Page: User Text Box is not visible");
		lp.username(driver).clear();lp.username(driver).type(suiteData.getSaasAppPassword());
		lp.loginButton(driver).click();
		Thread.sleep(5000);
		ollp.username(driver).clear();ollp.username(driver).type(suiteData.getUsername());
		ollp.password(driver).clear();ollp.password(driver).type(suiteData.getSaasAppPassword());
		ollp.loginButton(driver).click();
		Thread.sleep(10000);
	}

	
	public void loggedInViaSSO(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		Assert.assertTrue(lp.username(driver).isElementVisible(), "Login Page: User Text Box is not visible");
		lp.username(driver).clear();lp.username(driver).type(suiteData.getUsername());
		Thread.sleep(3000);
		lp.loginButton(driver).click();
		Thread.sleep(5000);
	}
	
	public void salesforceNewUILogout(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		/*SalesforceNewHomePage sfNewHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceNewHomePage.class);
		Thread.sleep(5000);
		sfNewHomePage.salesforceNewProfilePopup(driver).waitForLoading(driver);
		sfNewHomePage.salesforceNewProfilePopup(driver).click();
		Thread.sleep(5000);
		sfNewHomePage.salesforceNewLogout(driver).waitForLoading(driver);
		sfNewHomePage.salesforceNewLogout(driver).click();*/
	}
}
