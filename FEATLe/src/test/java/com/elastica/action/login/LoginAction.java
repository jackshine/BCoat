package com.elastica.action.login;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import com.elastica.action.Action;
import com.elastica.action.backend.BEAction.SSOName;
import com.elastica.common.SuiteData;
import com.elastica.constants.CommonConstants;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.AzureADPage;
import com.elastica.pageobjects.CentrifyPage;
import com.elastica.pageobjects.dashboard.DashboardPage;
import com.elastica.pageobjects.LoginPage;
import com.elastica.pageobjects.OktaPage;
import com.elastica.pageobjects.OneLoginPage;
import com.elastica.pageobjects.PingOnePage;

public class LoginAction extends Action{

	public enum UserType {
		SysAdmin, Admin, DPO, EndUser
	}

	public void login(WebDriver driver, SuiteData suiteData){
		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver,DashboardPage.class);
		int count=0;
		
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("Username:"+ suiteData.getUsername()+" &&& Password:" + suiteData.getPassword());
		driver.get(suiteData.getBaseUrl());hardWait(20);refresh(driver, 20);
		Logger.info("Expected Login url:"+suiteData.getBaseUrl());
		Logger.info("Actual Login url:"+getCurrentUrl(driver));
		if(getCurrentUrl(driver).equalsIgnoreCase(suiteData.getBaseUrl()+"/static/ng/appDashboards/index.html#/")){
			if (dp.loadingIcon(driver).isElementPresent(driver)){
				dp.loadingIcon(driver).waitForElementToDisappear(driver);
			}
			Logger.info("Login to CloudSOC completed, no need to login again");
		}else{
			count = loginRecursive(suiteData, driver, UserType.SysAdmin, 
					suiteData.getUsername(), suiteData.getPassword(), CommonConstants.TRY_COUNT);
			if(count > CommonConstants.TRY_COUNT) {
				Assert.fail("Login to CloudSOC is failed after "
						+CommonConstants.TRY_COUNT+" tries");
			}
		}

		Assert.assertTrue(dp.header(driver).isElementVisible(), "Dashboard Page: Header is not visible even after "+count+" retries");
		Assert.assertEquals(dp.header(driver).getText().trim(), "Dashboard", 
				"Dashboard Page: Header text is not matching even after "+count+" retries");

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void login(WebDriver driver, SuiteData suiteData, UserType userType, String headerText){
		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver,DashboardPage.class);
		
		String userName=getUserName(suiteData, userType);String userPassword=getUserPassword(suiteData, userType);
		int count = 0;
		Logger.info("Login as "+userType+" started");
		Logger.info(userType+" Username:"+ userName+" &&& "+userType+" Password:" + userPassword);
		
		driver.get(suiteData.getBaseUrl());hardWait(20);refresh(driver, 20);
		
		Logger.info("Expected Login url:"+suiteData.getBaseUrl());
		Logger.info("Actual Login url:"+getCurrentUrl(driver));
		
		String expectedCurrentUrl = getCurrentUrl(suiteData, userType);
		String actualCurrentUrl = getCurrentUrl(driver);
		
		if(actualCurrentUrl.equalsIgnoreCase(expectedCurrentUrl)){
			if (dp.loadingIcon(driver).isElementPresent(driver)){
				dp.loadingIcon(driver).waitForElementToDisappear(driver);
			}
			Logger.info("Login to CloudSOC completed, no need to login again");
		}else{
			count = loginRecursive(suiteData, driver, userType, userName, userPassword, 
					CommonConstants.TRY_COUNT_EXT);
			if(count > CommonConstants.TRY_COUNT_EXT) {
				Assert.fail("Login to CloudSOC is failed after "
						+CommonConstants.TRY_COUNT_EXT+" tries");
			}
		}


		Assert.assertTrue(dp.header(driver).isElementVisible(), "Dashboard Page: Header is not visible even after "+count+" retries");
		Assert.assertEquals(dp.header(driver).getText().trim(), headerText, 
				"Dashboard Page: Header text is not matching even after "+count+" retries");

		Logger.info("Login as "+userType+" completed");	
	}
	
	public int loginRecursive(SuiteData suiteData, WebDriver driver, UserType userType,
			String username, String password, int timeOut){
		int count=0;
		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver,DashboardPage.class);
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		
		for (count = 0; count <= timeOut; count ++) {
			
			try {
				if(getCurrentUrl(driver).equalsIgnoreCase(getCurrentUrl(suiteData, userType))){
					Logger.info("Login to CloudSOC completed, no need to login again");
					break;
				}else{
					Logger.info("**** Login to CloudSOC in progress, Try no:"+(count+1)+" ***");
					Logger.info("Try no:"+(count+1)+" Expected Login url:"+suiteData.getBaseUrl());
					Logger.info("Try no:"+(count+1)+" Actual Login url:"+getCurrentUrl(driver));
					
					driver.get(suiteData.getBaseUrl());hardWait(20);
					refresh(driver, 20);
					lp.username(driver).waitForElementPresent(driver);
					lp.username(driver).waitForElementToBeVisible(driver);
					lp.password(driver).waitForElementPresent(driver);
					lp.password(driver).waitForElementToBeVisible(driver);
					
					if(lp.username(driver).isDisplayed()) {
						lp.username(driver).clear();lp.username(driver).type(username);
						lp.password(driver).clear();lp.password(driver).type(password);
						lp.loginButton(driver).click();
						if (dp.loadingIcon(driver).isElementPresent(driver)){
							dp.loadingIcon(driver).waitForElementToDisappear(driver);
						}
						dp.header(driver).waitForElementPresent(driver, 60);
						dp.header(driver).waitForElementToBeVisible(driver);
					}else{
						continue;
					}
				}
			} catch(Exception e) {
				continue;
			}
			
			if(count > timeOut) {
				break;
			}
		}	
		
		return count;
	}

	public void loginCloudSocPortal(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		GatewayTestConstants.REACH = false;
		Logger.info("Loading CloudSOC portal...");
		try {
			LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
			driver.navigate().refresh();
			hardWait(5);
			waitForPageToLoad(driver, "ELastica", 50);
			Logger.info("Typing in username field: "+suiteData.getTestUsername());
			lp.username(driver).waitForElementToBeVisible(driver);
			lp.username(driver).clear();lp.username(driver).click();lp.username(driver).type(suiteData.getTestUsername());
			Logger.info("Typing in password field: "+suiteData.getTestPassword());
			lp.password(driver).clear();lp.password(driver).type(suiteData.getTestPassword());
			Logger.info("Login to CloudSOC in Progress");
			lp.loginButton(driver).click();
			waitForPageToLoad(driver, "|", 90);
			
			/*if (suiteData.getBaseUrl().contains("cha.elastica-inc.com")) {
				if (lp.selectTenantlabel(driver).isDisplayed()) {
					lp.selectTenantlabel(driver).click();
					lp.dropDownBoxSelectTenant(driver, suiteData.getTenantDomainName()).click();
					lp.loginButton(driver).click();
				}
			}*/
			Logger.info("Waiting for the page to load..."+CommonConstants.PAGE_LOAD_TIME+" Seconds");
			hardWait(CommonConstants.PAGE_LOAD_TIME);
			Logger.info("Logon to CloudSOC Successful");
		} catch(Exception e) {
		}
	}

	public void loginCloudSocPortalByAdmin(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		Logger.info("Loading CloudSOC portal for SSO Login");
		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver,DashboardPage.class);
		Exception exception = null;
		int count;
		for (count = 0; count <= CommonConstants.TRY_COUNT; count ++) {
			try {
				LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
				driver.get(suiteData.getLoginUrl());
				hardWait(10);
				Logger.info("User name: " + suiteData.getUsername()  + " and Password:" + suiteData.getTestPassword() );
				Logger.info("Typing in username field: "+suiteData.getUsername());
				lp.username(driver).waitForLoading(driver);
				lp.username(driver).clear();lp.username(driver).click();lp.username(driver).type(suiteData.getUsername());
				Logger.info("Typing in password field: "+suiteData.getPassword());
				lp.password(driver).clear();lp.password(driver).type(suiteData.getPassword());
				Logger.info("Login to CloudSOC in Progress");
				lp.loginButton(driver).waitForLoading(driver);
				lp.loginButton(driver).click();
				Logger.info("Waiting for the page to load..."+CommonConstants.PAGE_LOAD_TIME+" Seconds");
				hardWait(CommonConstants.PAGE_LOAD_TIME);
				dp.header(driver).waitForElementPresent(driver);
				dp.header(driver).waitForElementToBeVisible(driver);
				Assert.assertTrue(dp.header(driver).isElementVisible(), "Dashboard Page: Header is not visible");
				Assert.assertEquals(dp.header(driver).getText().trim(), "Dashboard", "Dashboard Page: Header text is not matching");
				Logger.info("Logon to CloudSOC Successful");
				break;

			} catch(Exception e) {
				exception = e;
				continue;
			}
		}	
		if(count >= CommonConstants.TRY_COUNT) {
			Assert.fail("Issue with Portal Login Page Operation " + exception);
		}
	}


	public String getSignInLabelText(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return lp.signInLabel(driver).getText().trim();
	}

	public void loginViaSSO(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		SSOName identifier = SSOName.valueOf(suiteData.getSaasAppName());
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		OneLoginPage ollp = AdvancedPageFactory.getPageObject(driver, OneLoginPage.class);
		AzureADPage azp = AdvancedPageFactory.getPageObject(driver, AzureADPage.class);
		OktaPage okta =  AdvancedPageFactory.getPageObject(driver, OktaPage.class);
		CentrifyPage cp =  AdvancedPageFactory.getPageObject(driver,CentrifyPage.class);
		PingOnePage pop =  AdvancedPageFactory.getPageObject(driver, PingOnePage.class);
		try {

			lp.singleSignOnLink(driver).mouseOver(driver);
			lp.singleSignOnLink(driver).click();
			hardWait(5);
			Assert.assertTrue(lp.username(driver).isElementVisible(), "Login Page: User Text Box is not visible");
			lp.username(driver).clear();lp.username(driver).type(suiteData.getSaasAppUsername());
			lp.loginButton(driver).click();
			hardWait(5);
			switch(identifier) {
			case OneLogin: {
				ollp.username(driver).clear();ollp.username(driver).type(suiteData.getUsername());
				ollp.password(driver).clear();ollp.password(driver).type(suiteData.getSaasAppPassword());
				ollp.loginButton(driver).click();
				break;
			}
			case Okta:{
				okta.username(driver).waitForElementPresent(driver);
				okta.username(driver).waitForElementToBeVisible(driver);
				okta.username(driver).clear();okta.username(driver).type(suiteData.getSaasAppUsername());
				okta.password(driver).clear();okta.password(driver).type(suiteData.getSaasAppPassword());
				okta.signIn(driver).click();
				break;
			}
			case Centrify:{
				if(getCurrentUrl(driver).contains("centrify")) {
					if (cp.username(driver).isElementPresent(driver)) {
						cp.username(driver).clear();cp.username(driver).type(suiteData.getSaasAppUsername());
						cp.usernextbutton(driver).waitForElementToBeVisible(driver);
						hardWait( 2);
						cp.usernextbutton(driver).click();
						hardWait( 5);
						cp.password(driver).clear();cp.password(driver).type(suiteData.getSaasAppPassword());
						cp.passwordnextbutton(driver).waitForElementToBeVisible(driver);
						hardWait( 2);
						cp.passwordnextbutton(driver).click();
						hardWait( 20);
					}
				}
				break;
			}
			case AzureAD:{
				/*if(azp.existingAccount(driver).isElementPresent(driver)){
						if(azp.existingAccount(driver).isElementVisible()){
							azp.existingAccount(driver).click();
						}
					}*/	
				if(getCurrentUrl(driver).contains("microsoftonline")){
					if(azp.usernameBusiness(driver).isElementPresent(driver)){
						if(azp.usernameBusiness(driver).isElementVisible()){
							azp.usernameBusiness(driver).clear();azp.usernameBusiness(driver).type(suiteData.getSaasAppUsername());hardWait(5);
							azp.passwordBusiness(driver).clear();

							if(azp.existingAccount(driver).isElementPresent(driver)){
								if(azp.existingAccount(driver).isElementVisible()){
									azp.existingAccount(driver).click();
								}
							}

							azp.passwordBusiness(driver).type(suiteData.getSaasAppPassword());hardWait(5);
							azp.signinButtonBusiness(driver).click();
							hardWait(10);
							if(azp.microsoftAccount(driver).isElementPresent(driver)){
								if(azp.microsoftAccount(driver).isElementVisible()){
									azp.microsoftAccount(driver).click();

									if(azp.username(driver).isElementPresent(driver)){
										if(azp.username(driver).isElementVisible()){
											azp.username(driver).waitForElementPresent(driver, 60);
											azp.username(driver).type(suiteData.getSaasAppUsername());
											azp.password(driver).type(suiteData.getSaasAppPassword());
											azp.signinbutton(driver).click();
											Logger.info("Clicked Azure sign in button ");
											hardWait(20);
										}
									}

								}
							}	

						}
					}
				}


				break;
			}
			case PingOne:{
				pop.username(driver).waitForElementPresent(driver);
				pop.username(driver).waitForElementToBeVisible(driver);
				pop.username(driver).clear();pop.username(driver).type(suiteData.getSaasAppUsername());
				pop.password(driver).clear();pop.password(driver).type(suiteData.getSaasAppPassword());
				pop.loginButton(driver).click();
				break;
			}
			case CASite:{
				break;
			}
			case Bitium:{
				break;
			}
			default:{
				Assert.fail("No configured SSO present");
				break;
			}
			}
			hardWait(10);

		} catch (Exception ex) {
			Logger.info("Issue with Portal Login SSO Page Operation " + ex.getLocalizedMessage());
		}

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void loggedInViaSSO(WebDriver driver, SuiteData suiteData){
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Assert.assertTrue(lp.username(driver).isElementVisible(), "Login Page: User Text Box is not visible");
		lp.username(driver).clear();lp.username(driver).type(suiteData.getUsername());
		hardWait(3);
		lp.loginButton(driver).click();
		hardWait(10);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void openBaseUrl(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			driver.get(suiteData.getBaseUrl());
			hardWait(5);

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void homepage(WebDriver driver, SuiteData suiteData) {
		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver,DashboardPage.class);
		driver.get(suiteData.getLoginUrl());
		dp.header(driver).waitForElementToBeVisible(driver);

	}

	public String getUserName(SuiteData suiteData, UserType userType){
		String userName=""; 
		switch(userType) {
		case SysAdmin: {
			userName=suiteData.getUsername();
			break;
		}
		case Admin: {
			userName=suiteData.getAdminUsername();
			break;
		}
		case DPO: {
			userName=suiteData.getDpoUsername();
			break;
		}
		case EndUser: {
			userName=suiteData.getEndUsername();
			break;
		}
		default: {
			userName=suiteData.getUsername();
			break;
		}
		}
		return userName;	
	}

	public String getUserPassword(SuiteData suiteData, UserType userType){
		String userName=""; 
		switch(userType) {
		case SysAdmin: {
			userName=suiteData.getPassword();
			break;
		}
		case Admin: {
			userName=suiteData.getAdminPassword();
			break;
		}
		case DPO: {
			userName=suiteData.getDpoPassword();
			break;
		}
		case EndUser: {
			userName=suiteData.getEndUserPassword();
			break;
		}
		default: {
			userName=suiteData.getPassword();
			break;
		}
		}
		return userName;	
	}
	
	public String getCurrentUrl(SuiteData suiteData, UserType userType){
		String currentUrl=""; 
		switch(userType) {
		case SysAdmin: {
			currentUrl=suiteData.getBaseUrl()+"/static/ng/appDashboards/index.html#/";
			break;
		}
		case Admin: {
			currentUrl=suiteData.getBaseUrl()+"/static/ng/appDashboards/index.html#/";
			break;
		}
		case DPO: {
			currentUrl=suiteData.getBaseUrl()+"/static/ng/appAccount/index.html#/profile";
			break;
		}
		case EndUser: {
			currentUrl=suiteData.getBaseUrl()+"/static/ng/appAccount/index.html#/profile";
			break;
		}
		default: {
			currentUrl=suiteData.getBaseUrl()+"/static/ng/appDashboards/index.html#/";
			break;
		}
		}
		return currentUrl;	
	}
}
