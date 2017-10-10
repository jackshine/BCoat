package com.elastica.action.google;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.GooglePage;

public class GoogleAction extends Action {
	final int ACTIVITY_AND_DELAY = 15;
	final int WAIT_FOR_ELEMENT = 5;

	public void login(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		GooglePage gp =  AdvancedPageFactory.getPageObject(driver, GooglePage.class);
		try {
			Logger.info(" Urls " + suiteData.getSaasAppBaseUrl() + "Login " + 
					suiteData.getSaasAppUsername() + "/"+ suiteData.getSaasAppPassword());
			hardWait(ACTIVITY_AND_DELAY);
			driver.get(suiteData.getSaasAppBaseUrl());
			hardWait(ACTIVITY_AND_DELAY);
			driver.get(suiteData.getSaasAppBaseUrl());
			gp.username(driver).waitForLoading(driver);
			gp.username(driver).clear();gp.username(driver).type(suiteData.getSaasAppUsername());
			gp.nextbutton(driver).click();
			gp.password(driver).isElementPresent(driver);
			gp.password(driver).clear();gp.password(driver).type(suiteData.getSaasAppPassword());
			gp.signin(driver).click();
		} catch(Exception e) {
			Logger.info("Error" + e);
		}
	}

	public void addUser(WebDriver driver, String userName) throws InterruptedException {
		GooglePage gp =  AdvancedPageFactory.getPageObject(driver, GooglePage.class);
		try {
			gp.adminUser(driver).click();
			hardWait(10);
			if (gp.adminAddButton(driver).isDisplayed()) {
				gp.adminAddButton(driver).click();
				
				WebElement element = driver.findElement(By.cssSelector("input[name='firstName']"));
						JavascriptExecutor executor = (JavascriptExecutor)driver;
				if(gp.adminCreateFirstName(driver).isDisplayed()) {
					gp.adminCreateFirstName(driver).mouseOver(driver);
				}
				gp.adminCreateFirstName(driver).clear();

				String value= "googletest";
			    executor.executeScript("arguments[0].setAttribute('value', '" + value +"')", element);
				gp.adminCreateFirstName(driver).type(userName);
				gp.adminCreateLastName(driver).mouseOver(driver);
				gp.adminCreateLastName(driver).clear();
				gp.adminCreateLastName(driver).type(userName);
				gp.adminCreateemail(driver).mouseOver(driver);
				gp.adminCreateemail(driver).clear();
				gp.adminCreateemail(driver).type(userName);
				
				gp.adminCreateButton(driver).mouseOver(driver);
				gp.adminCreateButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			}
		} catch(Exception e) {
			Logger.info("Errors" + e.getLocalizedMessage());
		}
	}
	
	public void editCompanyProfile(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		GooglePage gp =  AdvancedPageFactory.getPageObject(driver, GooglePage.class);
		try {
			driver.get(suiteData.getSaasAppBaseUrl() + "/AdminHome?fral=1#CompanyProfile:flyout=profile");
			hardWait(ACTIVITY_AND_DELAY);
			String companyName = "Elastica QA"; 
			if(gp.adminEditByName(driver, "organizationName").isDisplayed()) {
				gp.adminEditByName(driver, "organizationName").getText();
				gp.adminEditByName(driver, "organizationName").type("Edit");
			}
			gp.adminSaveSettingButtton(driver).mouseOver(driver);
			gp.adminSaveSettingButtton(driver).mouseOverClick(driver);
			hardWait(10);
			driver.get(suiteData.getSaasAppBaseUrl());
			hardWait(5);
			driver.get(suiteData.getSaasAppBaseUrl() + "/AdminHome?fral=1#CompanyProfile:flyout=profile");
			hardWait(ACTIVITY_AND_DELAY);
			if(gp.adminEditByName(driver, "organizationName").isDisplayed()) {
				gp.adminEditByName(driver, "organizationName").clear();
				gp.adminEditByName(driver, "organizationName").type(companyName);
			}
			gp.adminSaveSettingButtton(driver).mouseOver(driver);
			gp.adminSaveSettingButtton(driver).mouseOverClick(driver);
			hardWait(10);
		} catch(Exception e) {
			Logger.info("Errors" + e.getLocalizedMessage());
		}
		
	}
	
	public void editCommunicationPreference(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		GooglePage gp =  AdvancedPageFactory.getPageObject(driver, GooglePage.class);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		try {
			/* On and Off communciation preference */
			for (int i =0; i< 2; i++) {
				String section = "/AdminHome?fral=1&chromeless=1#CompanyProfile:flyout=communication";
				driver.get(suiteData.getSaasAppBaseUrl() + section);
				hardWait(ACTIVITY_AND_DELAY);
				WebElement performance = driver.findElement(gp.getBy(gp.adminCommunicationPreferenceCheckbox(driver, "Performance")));
				jse.executeScript("arguments[0].scrollIntoView(true);", performance);
				performance.click();
				hardWait(2);
				WebElement featureAnnouncement = driver.findElement(gp.getBy(gp.adminCommunicationPreferenceCheckbox(driver, "Feature announcements")));
				jse.executeScript("arguments[0].scrollIntoView(true);", featureAnnouncement);
				featureAnnouncement.click();
				hardWait(2);
				WebElement offers = driver.findElement(gp.getBy(gp.adminCommunicationPreferenceCheckbox(driver, "Offers")));
				jse.executeScript("arguments[0].scrollIntoView(true);", offers);
				offers.click();
				hardWait(2);
				WebElement feedback = driver.findElement(gp.getBy(gp.adminCommunicationPreferenceCheckbox(driver, "Feedback")));
				jse.executeScript("arguments[0].scrollIntoView(true);", feedback);
				feedback.click();
				hardWait(2);
				gp.adminSaveSettingButtton(driver).mouseOver(driver);
				gp.adminSaveSettingButtton(driver).mouseOverClick(driver);
				hardWait(10);
			}
		} catch(Exception e) {
			Logger.info("Error" + e.getLocalizedMessage());
		}
		
	}
	
	public void click(WebElement a,WebElement b, WebDriver driver) {
	    try 
	     {
	         String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
	         ((JavascriptExecutor) driver).executeScript(mouseOverScript,a);
	         Thread.sleep(1000);
	         ((JavascriptExecutor) driver).executeScript(mouseOverScript,b);
	         Thread.sleep(1000);
	         ((JavascriptExecutor)driver).executeScript("arguments[0].click();",b);
	    } catch (Exception e) {
	    }
	}
	
	public void editPersonalization(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		GooglePage gp =  AdvancedPageFactory.getPageObject(driver, GooglePage.class);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		try {
			/* On and Off Personalization preference */

			String section = "/AdminHome?fral=1#CompanyProfile:flyout=personalization";
			driver.get(suiteData.getSaasAppBaseUrl() + section);
			hardWait(10);
			
			WebElement logo = driver.findElement(gp.getBy(gp.adminuserchangelogostr(driver, "Default logo")));
			jse.executeScript("arguments[0].scrollIntoView(true);", logo);
			click(logo, logo, driver);

			gp.adminSaveSettingButtton(driver).mouseOver(driver);
			gp.adminSaveSettingButtton(driver).mouseOverClick(driver);
			hardWait(10);
			
			WebElement customlogo = driver.findElement(gp.getBy(gp.adminuserchangelogostr(driver, "Custom logo")));
			jse.executeScript("arguments[0].scrollIntoView(true);", customlogo);
			click(customlogo, customlogo, driver);
			gp.adminSaveSettingButtton(driver).mouseOver(driver);
			gp.adminSaveSettingButtton(driver).mouseOverClick(driver);
			hardWait(10);
		} catch(Exception e) {
			Logger.info("Error " + e.getLocalizedMessage());
		}
	}
	
	public void homepage(WebDriver driver, SuiteData suiteData) {
		GooglePage gp =  AdvancedPageFactory.getPageObject(driver, GooglePage.class);
		try {
			driver.get(suiteData.getSaasAppBaseUrl());
			if (gp.adminUser(driver).isDisplayed()) {
				return;
			}
		} catch(Exception e) {
			Logger.info("Error" + e.getLocalizedMessage());
		}
	}
	
	public void resetPassword(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		GooglePage gp = AdvancedPageFactory.getPageObject(driver, GooglePage.class);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		try {
			gp.adminUser(driver).click();
			if (gp.adminuserselect(driver, "testgoogleuser testgoogleuser").isDisplayed()) {
				WebElement adminuserselectElement = driver.findElement(By.xpath("//a[contains(.,'testgoogleuser testgoogleuser')]"));
				jse.executeScript("arguments[0].scrollIntoView(true);", adminuserselectElement);
				gp.adminuserselect(driver, "testgoogleuser testgoogleuser").click();
				gp.adminresetpassword(driver).click();
				if (gp.admininputbyname(driver, "password.newPassword.alpha").isDisplayed()) {
				gp.admininputbyname(driver, "password.newPassword.alpha").mouseOver(driver);
				gp.admininputbyname(driver, "password.newPassword.alpha").clear();
					//gp.admininputbyname(driver, "password.newPassword.alpha").type(userName);
					gp.admininputbyname(driver, "password.newPassword.alpha").type("test1234");
					gp.admininputbyname(driver, "password.newPassword.beta").type("test1234");
					gp.adminresetbutton(driver).click();
				}
				
			}
		} catch(Exception e) {
		Logger.info("Error" + e.getLocalizedMessage());
		}
	}
	
	public void renameUser(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		GooglePage gp = AdvancedPageFactory.getPageObject(driver, GooglePage.class);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		try {
			gp.adminUser(driver).click();
			if (gp.adminuserselect(driver, "testgoogleuser").isDisplayed()) {
				WebElement adminuserselectElement = driver.findElement(By.xpath("//a[contains(.,'testgoogleuser testgoogleuser')]"));
				jse.executeScript("arguments[0].scrollIntoView(true);", adminuserselectElement);
				gp.adminuserselect(driver, "testgoogleuser testgoogleuser").click();
				if (gp.adminusermore(driver).isDisplayed()) {
					gp.adminusermore(driver).click();
					gp.adminusersubmenu(driver, "Rename user").click();
					gp.admininputbyname(driver, "firstName").type("edit");
					gp.adminrenamebutton(driver).mouseOver(driver);
					gp.adminrenamebutton(driver).click();
					hardWait(10);
					gp.adminusermore(driver).click();
					gp.adminusersubmenu(driver, "Rename user").click();
					gp.admininputbyname(driver, "firstName").clear();
					gp.admininputbyname(driver, "firstName").type("testgoogleuser");
					gp.adminrenamebutton(driver).mouseOver(driver);
					gp.adminrenamebutton(driver).click();
					hardWait(10);
				}
			}
		} catch(Exception e) {
			Logger.info("Error " + e.getLocalizedMessage());
		}
	}
	
	public void sharingAndLinking(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		GooglePage gp = AdvancedPageFactory.getPageObject(driver, GooglePage.class);
		String section = "/AdminHome?fral=1#AppDetails:service=Drive+and+Docs&flyout=sharing";
		try {
			driver.get(suiteData.getSaasAppBaseUrl() + section);
			hardWait(10);
			WebElement adminSharingOff = driver.findElement(gp.getBy(gp.adminGdriveSharingOff(driver)));
			click(adminSharingOff, adminSharingOff, driver);
			if (gp.adminSaveSettingButtton(driver).isDisplayed()) {
				gp.adminSaveSettingButtton(driver).mouseOver(driver);
				gp.adminSaveSettingButtton(driver).mouseOverClick(driver);
				hardWait(10);
			}
			
			WebElement adminSharingOn = driver.findElement(gp.getBy(gp.adminGdriveSharingOn(driver)));
			click(adminSharingOn, adminSharingOn, driver);
			if (gp.adminSaveSettingButtton(driver).isDisplayed()) {
				gp.adminSaveSettingButtton(driver).mouseOver(driver);
				gp.adminSaveSettingButtton(driver).mouseOverClick(driver);
				hardWait(10);
			}
		} catch(Exception e) {
			Logger.info("Error " + e.getLocalizedMessage());
		}		
	}
	
}