package com.elastica.action.o365;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.LoginPage;
import com.elastica.pageobjects.O365LoginPage;

public class O365LoginAction extends Action{
	
	public void login(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		Logger.info("Loading Saas App");
		O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
		driver.get(suiteData.getSaasAppBaseUrl());
		hardWait(5);
		driver.get(suiteData.getSaasAppBaseUrl());
		waitForPageToLoad(driver, "Sign in", 30);
		//hardWait(20);
		//if (lp.username(driver).isDisplayed()) {
			lp.username(driver).waitForLoading(driver, GatewayTestConstants.DEFAULT_ELEMENT_LOAD_TIME*2);
			Logger.info("Typeing in username: "+suiteData.getSaasAppUsername());
			lp.username(driver).clear();hardWait(GatewayTestConstants.INITIAL_WAIT);lp.username(driver).type(suiteData.getSaasAppUsername());
			Logger.info("Typeing in password: "+suiteData.getSaasAppPassword());
			lp.password(driver).clear();hardWait(GatewayTestConstants.INITIAL_WAIT);lp.password(driver).type(suiteData.getSaasAppPassword());
			hardWait(GatewayTestConstants.INITIAL_WAIT);
			Logger.info("Clicking Login button");
			lp.loginButton(driver).mouseOverClick(driver);
			
			//lp.loginButton(driver).click();
			Logger.info("Login to 0365");
			waitForPageToLoad(driver, "Office 365", 30);
			//hardWait(8);
			Logger.info("Loging  to Saas App "+suiteData.getSaasAppName()+" successful");
			Logger.info("Saas App "+suiteData.getSaasAppName()+" home page loadded");
		//}
	//	else{
			//Logger.info("Loging  to Saas App failure ");
		//}
		//System.out.println(driver.getTitle());
	}
	
	
	public void loginWithBlock(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		Logger.info("Loading Saas App");
		O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
		driver.get(suiteData.getSaasAppBaseUrl());
		hardWait(5);
		driver.get(suiteData.getSaasAppBaseUrl());
		waitForPageToLoad(driver, "Sign in", 30);
		//hardWait(20);
		//if (lp.username(driver).isDisplayed()) {
			lp.username(driver).waitForLoading(driver, GatewayTestConstants.DEFAULT_ELEMENT_LOAD_TIME*2);
			Logger.info("Typeing in username: "+suiteData.getSaasAppUsername());
			lp.username(driver).clear();hardWait(GatewayTestConstants.INITIAL_WAIT);lp.username(driver).type(suiteData.getSaasAppUsername());
			Logger.info("Typeing in password: "+suiteData.getSaasAppPassword());
			lp.password(driver).clear();hardWait(GatewayTestConstants.INITIAL_WAIT);lp.password(driver).type(suiteData.getSaasAppPassword());
			hardWait(GatewayTestConstants.INITIAL_WAIT);
			Logger.info("Clicking Login button");
			lp.loginButton(driver).mouseOverClick(driver);
			
			//lp.loginButton(driver).click();
			Logger.info("Login to 0365");
			//waitForPageToLoad(driver, "Office 365", 30);
			//hardWait(8);
			Logger.info("Loging  to Saas App "+suiteData.getSaasAppName()+" successful");
			Logger.info("Saas App "+suiteData.getSaasAppName()+" home page loadded");
		//}
	//	else{
			//Logger.info("Loging  to Saas App failure ");
		//}
		//System.out.println(driver.getTitle());
	}
	
	
	public void logout(WebDriver driver) throws InterruptedException{
		Logger.info("Logging out from Saas App ");
		O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
		//lp.userProfileButton(driver).mouseOverClick(driver);
		//lp.logoutButton(driver).mouseOverClick(driver);
		driver.get("https://login.microsoftonline.com/logout.srf");
		hardWait(20);
		Logger.info("Logout successfull");
	}
	
	
	public String getSignInLabelText(WebDriver driver) {
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		return lp.signInLabel(driver).getText().trim();
	}
	
	public void reloginInvalidAndValid(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		Logger.info("Relogin"+suiteData.getSaasAppUsername()+"/"+suiteData.getSaasAppPassword());
		O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
		driver.get(suiteData.getSaasAppBaseUrl());
		lp.loginuser(driver).click();
		lp.password(driver).clear();lp.password(driver).type("Wrong");
		hardWait(15);
		lp.loginButton(driver).mouseOverClick(driver);
		hardWait(5);
		Logger.info("Trying Valid Password" +suiteData.getSaasAppPassword());
		driver.get(suiteData.getSaasAppBaseUrl());
		lp.loginuser(driver).click();
		hardWait(10);
		lp.password(driver).clear();lp.password(driver).type(suiteData.getSaasAppPassword());
		hardWait(5);
		lp.loginButton(driver).mouseOverClick(driver);
		Logger.info("Invalida login "+suiteData.getSaasAppName()+" successful");
		Logger.info("Saas App "+suiteData.getSaasAppName()+" home page loadded");
	}
	
	
	public void relogin(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		Logger.info("Relogin"+suiteData.getSaasAppUsername()+"/"+suiteData.getSaasAppPassword());
		O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
		driver.get(suiteData.getSaasAppBaseUrl());
		if(lp.loginuser(driver).isDisplayed()) {
			lp.loginuser(driver).waitForLoading(driver);
			lp.loginuser(driver).click();
			lp.password(driver).clear();lp.password(driver).type(suiteData.getSaasAppPassword());
			hardWait(5);
			lp.loginButton(driver).mouseOverClick(driver);
			hardWait(5);
		}
		Logger.info("Relogin using  "+suiteData.getSaasAppPassword() +" successful");
	}
	
	public void onedriveLogin(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		Logger.info("Logging to Saas app with username/password "+suiteData.getSaasAppUsername()+"/"+suiteData.getSaasAppPassword());
		O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
		driver.get(suiteData.getSaasAppBaseUrl());
		hardWait(4);
		driver.get(suiteData.getSaasAppBaseUrl());
		hardWait(10);
		if (lp.personalSignin(driver).isDisplayed()) {
			lp.personalSignin(driver).click();
			if (lp.personalPopupemail(driver).isDisplayed()) {
				lp.personalPopupemail(driver).clear(); lp.personalPopupemail(driver).type(suiteData.getSaasAppUsername());
				lp.personalPopupnext(driver).click();
				if (lp.personalLoginPassword(driver).isDisplayed()) {
					lp.personalLoginEmail(driver).clear(); lp.personalLoginEmail(driver).type(suiteData.getSaasAppUsername());
					lp.personalLoginPassword(driver).type(suiteData.getSaasAppPassword());
					lp.personalLoginSubmit(driver).click();
					hardWait(10);
				}
			}
		} else if (lp.personalLoginEmail(driver).isDisplayed()) {
			lp.personalLoginEmail(driver).clear(); lp.personalLoginEmail(driver).type(suiteData.getSaasAppUsername());
			lp.personalLoginPassword(driver).type(suiteData.getSaasAppPassword());
			lp.personalLoginSubmit(driver).click();
			hardWait(10);
		}
		Logger.info("Loggged in Personal Account");
	}
	
}