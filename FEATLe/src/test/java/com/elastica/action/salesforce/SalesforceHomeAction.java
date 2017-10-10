package com.elastica.action.salesforce;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.action.Action;
import com.elastica.action.aws.AwsAction;
import com.elastica.action.backend.BEAction;
import com.elastica.restClient.ClientUtil;
import com.elastica.common.GWCommonTest;
import com.elastica.common.SuiteData;
import com.elastica.constants.salesforce.SalesforceConstants;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.gateway.RawJsonParser;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.LoginPage;
import com.elastica.pageobjects.SalesforceHomePage;
import com.elastica.restClient.Client;
import com.elastica.restClient.ESQueryBuilder;
import com.google.common.io.Files;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;
import net.sourceforge.htmlunit.corejs.javascript.regexp.SubString;

public class SalesforceHomeAction extends Action{

	
	
	public void loadEmailApp(WebDriver driver, SuiteData suiteData) throws InterruptedException{
	/*
		O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
		//DashboardPage dp =  AdvancedPageFactory.getPageObject(driver,DashboardPage.class);
		lp.username(driver).clear();lp.username(driver).type(suiteData.getUsername());
		lp.password(driver).clear();lp.password(driver).type(suiteData.getPassword());
		lp.loginButton(driver).mouseOver(driver);
		lp.loginButton(driver).mouseOverClick(driver);
		lp.loginButton(driver).click();
		Thread.sleep(15000);*/
		
		//Assert.assertTrue(dp.header(driver).isElementVisible(), "Dashboard Page: Header is not visible");
		//Assert.assertEquals(dp.header(driver).getText().trim(), "Dashboard", "Dashboard Page: Header text is not matching");
	}
	
	public String getSignInLabelText(WebDriver driver) {
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		return lp.signInLabel(driver).getText().trim();
	}
	
	public void loginViaSSO(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		/*LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		OneLoginPage ollp;// = AdvancedPageFactory.getPageObject(driver, OneLoginPage.class);
		lp.singleSignOnLink(driver).mouseOver(driver);
		lp.singleSignOnLink(driver).click();
		Thread.sleep(5000);
		Assert.assertTrue(lp.username(driver).isElementVisible(), "Login Page: User Text Box is not visible");
		lp.username(driver).clear();lp.username(driver).type(suiteData.getUsernameSSO());
		lp.loginButton(driver).click();
		Thread.sleep(5000);
		ollp.username(driver).clear();ollp.username(driver).type(suiteData.getUsername());
		ollp.password(driver).clear();ollp.password(driver).type(suiteData.getPasswordSSO());
		ollp.loginButton(driver).click();
		Thread.sleep(10000);*/
	}

	
	public void loggedInViaSSO(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		Assert.assertTrue(lp.username(driver).isElementVisible(), "Login Page: User Text Box is not visible");
		lp.username(driver).clear();lp.username(driver).type(suiteData.getUsername());
		Thread.sleep(3000);
		lp.loginButton(driver).click();
		Thread.sleep(5000);
	}
	
	public void clickMoreTabOption(WebDriver driver, String option) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceMoreTab(driver).waitForLoading(driver);
		salesforceHomePage.salesforceMoreTab(driver).click();
		if(option.equals("Account")){
			salesforceHomePage.salesforceMoreOption(driver, "Account").waitForLoading(driver);
			salesforceHomePage.salesforceMoreOption(driver, "Account").click();
			Thread.sleep(3000);
		}
		if(option.equals("Opportunity")){
			salesforceHomePage.salesforceMoreOption(driver, "New Opportunity").waitForLoading(driver);
			salesforceHomePage.salesforceMoreOption(driver, "New Opportunity").click();
			Thread.sleep(3000);
		}
		if(option.equals("Contact")){
			salesforceHomePage.salesforceMoreOption(driver, "Contact").waitForLoading(driver);
			salesforceHomePage.salesforceMoreOption(driver, "Contact").click();
			Thread.sleep(3000);
		}
		if(option.equals("Lead")){
			salesforceHomePage.salesforceMoreOption(driver, "New Lead").waitForLoading(driver);
			salesforceHomePage.salesforceMoreOption(driver, "New Lead").click();
			Thread.sleep(3000);
		}
	}
	

	public void gotoHomeTab(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceHomeTab(driver).waitForLoading(driver);
		salesforceHomePage.salesforceHomeTab(driver).click();
		Thread.sleep(5000);
	}
	
	public void gotoLeadsTab(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceLeadsTab(driver).waitForLoading(driver);
		salesforceHomePage.salesforceLeadsTab(driver).click();
		Thread.sleep(5000);
	}
	
	public void gotoChatterTab(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(3000);
		salesforceHomePage.salesforceChatterTab(driver).waitForLoading(driver);
		salesforceHomePage.salesforceChatterTab(driver).click();
		Thread.sleep(3000);
	}
	
	public void gotoAccountsTab(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		Logger.info("Clicking on acct Tab");
		salesforceHomePage.salesforceAccountTab(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountTab(driver).click();
		Logger.info("Clicked on acct Tab");
		Thread.sleep(5000);
	}
	
	public void gotoOpportunityTab(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceOpportunityTab(driver).waitForLoading(driver);
		salesforceHomePage.salesforceOpportunityTab(driver).click();
		Thread.sleep(5000);
	}
	
	public void gotoContactsTab(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceContactTab(driver).waitForLoading(driver);
		salesforceHomePage.salesforceContactTab(driver).click();
		Thread.sleep(5000);
	}
	
	public void gotoFilesTab(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		System.out.println(salesforceHomePage.salesforceFilesTab(driver));
		salesforceHomePage.salesforceFilesTab(driver).click();
	}
	
	/**
	 * Create a account in salesforce
	 * @param driver
	 * @param accountDetails
	 * @throws InterruptedException 
	 */
	public void createAccountOnHomePage(WebDriver driver, Map<String, String> accountDetails) throws InterruptedException{
		Reporter.log("Creating a new account name "+accountDetails.get(SalesforceConstants.ACCOUNT_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceAccountName(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountName(driver).type(accountDetails.get(SalesforceConstants.ACCOUNT_NAME));
		Reporter.log("Account Name: "+accountDetails.get(SalesforceConstants.ACCOUNT_NAME), true);
		salesforceHomePage.salesforceAccountDescription(driver).type(accountDetails.get(SalesforceConstants.ACCOUNT_DESCRIPTION));
		Reporter.log("Account Description: "+accountDetails.get(SalesforceConstants.ACCOUNT_DESCRIPTION), true);
		salesforceHomePage.salesforceAccountWebsite(driver).type(accountDetails.get(SalesforceConstants.WEBSITE));
		Reporter.log("Account Website: "+accountDetails.get(SalesforceConstants.WEBSITE), true);
		salesforceHomePage.salesforceCreateAccountButton(driver).waitForLoading(driver);
		Thread.sleep(2000);
		salesforceHomePage.salesforceCreateAccountButton(driver).click();
		Thread.sleep(5000);
		driver.navigate().refresh();
		Thread.sleep(5000);
		Reporter.log("Created a new account name "+accountDetails.get(SalesforceConstants.ACCOUNT_NAME), true);
	}
	
	public void createAccountOnAccountsPage(WebDriver driver, Map<String, String> accountDetails) throws InterruptedException{
		Reporter.log("Creating a new account name "+accountDetails.get(SalesforceConstants.ACCOUNT_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(10000);
		salesforceHomePage.salesforceAccountNewButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountNewButton(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountName1(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountName1(driver).type(accountDetails.get(SalesforceConstants.ACCOUNT_NAME));
		Reporter.log("Account Name: "+accountDetails.get(SalesforceConstants.ACCOUNT_NAME), true);
		salesforceHomePage.salesforceAccountDescription1(driver).type(accountDetails.get(SalesforceConstants.ACCOUNT_DESCRIPTION));
		Reporter.log("Account Description: "+accountDetails.get(SalesforceConstants.ACCOUNT_DESCRIPTION), true);
		salesforceHomePage.salesforceAccountWebsite1(driver).type(accountDetails.get(SalesforceConstants.WEBSITE));
		Reporter.log("Account Website: "+accountDetails.get(SalesforceConstants.WEBSITE), true);
		salesforceHomePage.salesforceAccountSaveButton(driver).waitForLoading(driver);
		Thread.sleep(2000);
		salesforceHomePage.salesforceAccountSaveButton(driver).click();
		Reporter.log("Created a new account name "+accountDetails.get(SalesforceConstants.ACCOUNT_NAME), true);
	}
	
	/**
	 * viewAccount
	 * @param driver
	 * @param accountDetails
	 * @throws InterruptedException
	 */
	public void viewAccount(WebDriver driver, String accountName) throws InterruptedException{
		Reporter.log("Editing the account "+accountName, true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		driver.navigate().refresh();
		Thread.sleep(5000);
		Logger.info("Clicking on acct Tab");
		salesforceHomePage.salesforceAccountTab(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountTab(driver).click();
		Logger.info("Clicked on acct Tab");
		Thread.sleep(5000);
		salesforceHomePage.salesForceAccountNameLink(driver, accountName).waitForLoading(driver);
		salesforceHomePage.salesForceAccountNameLink(driver, accountName).click();
		Thread.sleep(5000);
		Reporter.log("Viewed the account "+accountName, true);
	}
	
	/**
	 * Edit a account in salesforce
	 * @param driver
	 * @param accountName
	 * @param newDescription
	 * @throws InterruptedException 
	 */
	public void editAccountInSalesforce(WebDriver driver, String accountName, String newDescription) throws InterruptedException{
		Reporter.log("Editing the account "+accountName, true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(10000);
		salesforceHomePage.salesForceAccountNameLink(driver, accountName).waitForLoading(driver);
		salesforceHomePage.salesForceAccountNameLink(driver, accountName).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountEditButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountEditButton(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountEditDescription(driver).clear();
		Reporter.log("Edited description: "+newDescription, true);
		salesforceHomePage.salesforceAccountEditDescription(driver).type(newDescription);
		salesforceHomePage.salesforceAccountSaveButton(driver).click();
		Thread.sleep(5000);
		Reporter.log("Edited the account "+accountName, true);
	}
	
	public void commentAccountChatterTab(WebDriver driver, String accountName) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceChatterCommentButton(driver, accountName).waitForLoading(driver);
		salesforceHomePage.salesforceChatterCommentButton(driver, accountName).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceChatterCommentTextarea(driver, accountName).waitForLoading(driver);
		salesforceHomePage.salesforceChatterCommentTextarea(driver, accountName).type(accountName);
		Thread.sleep(5000);
		salesforceHomePage.salesforceChatterCommentSubmit(driver, accountName).waitForLoading(driver);
		salesforceHomePage.salesforceChatterCommentSubmit(driver, accountName).click();
	}
	
	/**
	 * Delete a account in salesforce
	 * @param driver
	 * @param accountName
	 * @throws InterruptedException 
	 */
	public void deleteAccountInSalesForce(WebDriver driver, String accountName) throws InterruptedException{
		Reporter.log("Deleting the account "+accountName, true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesForceAccountNameLink(driver, accountName).waitForLoading(driver);
		salesforceHomePage.salesForceAccountNameLink(driver, accountName).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountDeleteButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountDeleteButton(driver).click();
		driver.switchTo().alert().accept();
		//Reporter.log("Deleted the account "+accountName, true);
	}
	
	public String getTimestamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
		Date date = new Date();
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
	
	public void clickCreateNewAccount(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountNewButton(driver).waitForLoading(driver);;
		salesforceHomePage.salesforceAccountNewButton(driver).click();
		Thread.sleep(5000);
	}
	
	public String createOpportunity(WebDriver driver, Map<String, String> opportunityDetails) throws InterruptedException{
		Reporter.log("Creating a new opportunity name "+opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceOpportunityName(driver).waitForLoading(driver);
		salesforceHomePage.salesforceOpportunityName(driver).type(opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME));
		salesforceHomePage.salesforceOpportunityAccountName(driver).type(opportunityDetails.get(SalesforceConstants.OPPORTUNITY_ACCOUNT_NAME));
		salesforceHomePage.salesforceOpportunityAmount(driver).type(opportunityDetails.get(SalesforceConstants.OPPORTUNITY_AMOUNT));
		salesforceHomePage.salesforceOpportunityStage(driver).selectValueFromDropDown(SalesforceConstants.QUALIFICATION);
		Thread.sleep(5000);
		String closeDate = salesforceHomePage.salesforceOpportunityCloseDate(driver).getAttribute("value");
		salesforceHomePage.salesforceCreateAccountButton(driver).click();
		Thread.sleep(5000);
		Reporter.log("Created a new opportunity name "+opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME), true);
		return closeDate;
	}
	
	public void editOpportunity(WebDriver driver, Map<String, String> opportunityDetails) throws InterruptedException{
		Reporter.log("Editing a new opportunity name "+opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		driver.navigate().refresh();
		salesforceHomePage.salesForceAccountNameLink(driver, opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME)).waitForLoading(driver);
		salesforceHomePage.salesForceAccountNameLink(driver, opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME)).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountEditButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountEditButton(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceOpportunityDescription(driver).waitForLoading(driver);
		salesforceHomePage.salesforceOpportunityDescription(driver).type(opportunityDetails.get(SalesforceConstants.OPPORTUNITY_DESCRIPTION));
		salesforceHomePage.salesforceAccountSaveButton(driver).click();
		Thread.sleep(5000);
		Reporter.log("Edited a new opportunity name "+opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME), true);
	}
	
	public String createOpportunity1(WebDriver driver, Map<String, String> opportunityDetails) throws InterruptedException{
		Reporter.log("Creating a new opportunity name "+opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceOpportunityName1(driver).waitForLoading(driver);
		salesforceHomePage.salesforceOpportunityName1(driver).type(opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME));
		salesforceHomePage.salesforceOpportunityAccountName1(driver).type(opportunityDetails.get(SalesforceConstants.OPPORTUNITY_ACCOUNT_NAME));
		salesforceHomePage.salesforceOpportunityAmount1(driver).type(opportunityDetails.get(SalesforceConstants.OPPORTUNITY_AMOUNT));
		salesforceHomePage.salesforceOpportunityStage1(driver).selectValueFromDropDown(SalesforceConstants.QUALIFICATION);
		salesforceHomePage.salesforceOpportunityCloseDate1(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceOpportunityDescription(driver).type(opportunityDetails.get(SalesforceConstants.OPPORTUNITY_DESCRIPTION));
		Thread.sleep(5000);
		String closeDate = salesforceHomePage.salesforceOpportunityCloseDate1(driver).getText();
		salesforceHomePage.salesforceAccountSaveButton(driver).click();
		Thread.sleep(5000);
		Reporter.log("Created a new opportunity name "+opportunityDetails.get(SalesforceConstants.OPPORTUNITY_NAME), true);
		return closeDate;
	}
	
	public void createContact(WebDriver driver, Map<String, String> contactDetails) throws InterruptedException{
		Reporter.log("Creating a new contact name "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceContactSalutation(driver).waitForLoading(driver);
		salesforceHomePage.salesforceContactSalutation(driver).selectValueFromDropDown(contactDetails.get(SalesforceConstants.SALUTATION));
		salesforceHomePage.salesforceContactFirstname(driver).type(contactDetails.get(SalesforceConstants.FIRST_NAME));
		salesforceHomePage.salesforceContactMiddlename(driver).type(contactDetails.get(SalesforceConstants.MIDDLE_NAME));
		salesforceHomePage.salesforceContactLastname(driver).type(contactDetails.get(SalesforceConstants.LAST_NAME));
		salesforceHomePage.salesforceContactSuffix(driver).type(contactDetails.get(SalesforceConstants.SUFFIX));
		salesforceHomePage.salesforceContactEmail(driver).type(contactDetails.get(SalesforceConstants.EMAIL));
		salesforceHomePage.salesforceContactPhone(driver).type(contactDetails.get(SalesforceConstants.PHONE));
		salesforceHomePage.salesforceContactTitle(driver).type(contactDetails.get(SalesforceConstants.TITLE));
		salesforceHomePage.salesforceCreateAccountButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceCreateAccountButton(driver).click();
		Thread.sleep(5000);
		Reporter.log("Created a new contact name "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME), true);
	}
	
	public void editAndCreateContact(WebDriver driver, Map<String, String> contactDetails, String edit) throws InterruptedException{
		Reporter.log("Creating or editing a contact name "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		if(edit.equalsIgnoreCase("yes")){
			Thread.sleep(5000);
			String contactName = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME)+" "+contactDetails.get(SalesforceConstants.SUFFIX);
			if(salesforceHomePage.salesforceContactLink(driver, contactName).isDisplayed()){
				salesforceHomePage.salesforceContactLink(driver, contactName).click();
				Thread.sleep(5000);
			}else{
				contactName = contactDetails.get(SalesforceConstants.LAST_NAME)+", "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+",  "+contactDetails.get(SalesforceConstants.SUFFIX);
				if(salesforceHomePage.salesForceAccountNameLink(driver, contactName).isDisplayed()){
					salesforceHomePage.salesForceAccountNameLink(driver, contactName).click();
					Thread.sleep(5000);
				}
			}
			salesforceHomePage.salesforceAccountEditButton(driver).click();
			Thread.sleep(5000);
		}else{
			Thread.sleep(5000);
			salesforceHomePage.salesforceAccountNewButton(driver).waitForLoading(driver);;
			salesforceHomePage.salesforceAccountNewButton(driver).click();
			Thread.sleep(5000);
		}
		salesforceHomePage.salesforceContactSalutation1(driver).waitForLoading(driver);
		salesforceHomePage.salesforceContactSalutation1(driver).selectValueFromDropDown(contactDetails.get(SalesforceConstants.SALUTATION));
		salesforceHomePage.salesforceContactFirstname1(driver).clear();
		salesforceHomePage.salesforceContactFirstname1(driver).type(contactDetails.get(SalesforceConstants.FIRST_NAME));
		salesforceHomePage.salesforceContactMiddlename1(driver).clear();
		salesforceHomePage.salesforceContactMiddlename1(driver).type(contactDetails.get(SalesforceConstants.MIDDLE_NAME));
		salesforceHomePage.salesforceContactLastname1(driver).clear();
		salesforceHomePage.salesforceContactLastname1(driver).type(contactDetails.get(SalesforceConstants.LAST_NAME));
		salesforceHomePage.salesforceContactSuffix1(driver).clear();
		salesforceHomePage.salesforceContactSuffix1(driver).type(contactDetails.get(SalesforceConstants.SUFFIX));
		salesforceHomePage.salesforceContactEmail1(driver).clear();
		salesforceHomePage.salesforceContactEmail1(driver).type(contactDetails.get(SalesforceConstants.EMAIL));
		salesforceHomePage.salesforceContactPhone1(driver).clear();
		salesforceHomePage.salesforceContactPhone1(driver).type(contactDetails.get(SalesforceConstants.PHONE));
		salesforceHomePage.salesforceContactTitle1(driver).clear();
		salesforceHomePage.salesforceContactTitle1(driver).type(contactDetails.get(SalesforceConstants.TITLE));
		salesforceHomePage.salesforceContactMobile(driver).clear();
		salesforceHomePage.salesforceContactMobile(driver).type(contactDetails.get(SalesforceConstants.MOBILE));
		salesforceHomePage.salesforceContactAccountname1(driver).clear();
		salesforceHomePage.salesforceContactAccountname1(driver).type(contactDetails.get(SalesforceConstants.ACCOUNT_NAME));
		salesforceHomePage.salesforceAccountSaveButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountSaveButton(driver).click();
		Thread.sleep(5000);
		Reporter.log("Created or edited a contact name "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME), true);
	}
	
	public void deleteContact(WebDriver driver, Map<String, String> contactDetails) throws InterruptedException{
		Reporter.log("Deleting a contact name "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		String contactName = contactDetails.get(SalesforceConstants.LAST_NAME)+", "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.MIDDLE_NAME)+",  "+contactDetails.get(SalesforceConstants.SUFFIX);
		salesforceHomePage.salesforceContactLink(driver, contactName).waitForLoading(driver);
		salesforceHomePage.salesforceContactLink(driver, contactName).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountDeleteButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountDeleteButton(driver).click();
		driver.switchTo().alert().accept();
		Reporter.log("Deleted a contact name "+contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME), true);
	}
	
	public void createLeads(WebDriver driver, Map<String, String> leadDetails) throws InterruptedException{
		Reporter.log("Creating a Lead name "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceContactSalutation(driver).waitForLoading(driver);
		salesforceHomePage.salesforceContactSalutation(driver).selectValueFromDropDown(leadDetails.get(SalesforceConstants.SALUTATION));
		salesforceHomePage.salesforceContactFirstname(driver).type(leadDetails.get(SalesforceConstants.FIRST_NAME));
		salesforceHomePage.salesforceContactMiddlename(driver).type(leadDetails.get(SalesforceConstants.MIDDLE_NAME));
		salesforceHomePage.salesforceContactLastname(driver).type(leadDetails.get(SalesforceConstants.LAST_NAME));
		salesforceHomePage.salesforceContactSuffix(driver).type(leadDetails.get(SalesforceConstants.SUFFIX));
		salesforceHomePage.salesforceContactEmail(driver).type(leadDetails.get(SalesforceConstants.EMAIL));
		salesforceHomePage.salesforceContactPhone(driver).type(leadDetails.get(SalesforceConstants.PHONE));
		salesforceHomePage.salesforceContactTitle(driver).type(leadDetails.get(SalesforceConstants.TITLE));
		salesforceHomePage.salesforceLeadCompany(driver).type(leadDetails.get(SalesforceConstants.COMPANY));
		salesforceHomePage.salesforceCreateAccountButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceCreateAccountButton(driver).click();
		Thread.sleep(5000);
		Reporter.log("Created a Lead name "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX), true);
	}
	
	public void editAndCreateLeads(WebDriver driver, Map<String, String> leadDetails, String edit) throws InterruptedException{
		Reporter.log("Creating or editing a leads name "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		if(edit.equalsIgnoreCase("yes")){
			String leadName = leadDetails.get(SalesforceConstants.LAST_NAME)+", "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+",  "+leadDetails.get(SalesforceConstants.SUFFIX);
			System.out.println("LeadName: "+leadName);
			if(salesforceHomePage.salesforceContactLink(driver, leadName).isDisplayed()){
				salesforceHomePage.salesforceContactLink(driver, leadName).click();
				Thread.sleep(5000);
			}else{
				leadName = leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.MIDDLE_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME)+" "+leadDetails.get(SalesforceConstants.SUFFIX)+", "+leadDetails.get(SalesforceConstants.COMPANY);
				System.out.println("LeadName: "+leadName);
				if(salesforceHomePage.salesforceLeadHomeLink(driver, leadName).isDisplayed()){
					salesforceHomePage.salesforceLeadHomeLink(driver, leadName).click();
					Thread.sleep(5000);
				}
			}
			if(salesforceHomePage.salesforceAccountEditButton(driver).isDisplayed()){
				salesforceHomePage.salesforceAccountEditButton(driver).click();
				Thread.sleep(5000);
			}
		}
		salesforceHomePage.salesforceContactSalutation1(driver).waitForLoading(driver);
		salesforceHomePage.salesforceContactSalutation1(driver).selectValueFromDropDown(leadDetails.get(SalesforceConstants.SALUTATION));
		salesforceHomePage.salesforceContactFirstname1(driver).clear();
		salesforceHomePage.salesforceContactFirstname1(driver).type(leadDetails.get(SalesforceConstants.FIRST_NAME));
		salesforceHomePage.salesforceContactMiddlename1(driver).clear();
		salesforceHomePage.salesforceContactMiddlename1(driver).type(leadDetails.get(SalesforceConstants.MIDDLE_NAME));
		salesforceHomePage.salesforceContactLastname1(driver).clear();
		salesforceHomePage.salesforceContactLastname1(driver).type(leadDetails.get(SalesforceConstants.LAST_NAME));
		salesforceHomePage.salesforceContactSuffix1(driver).clear();
		salesforceHomePage.salesforceContactSuffix1(driver).type(leadDetails.get(SalesforceConstants.SUFFIX));
		salesforceHomePage.salesforceContactEmail1(driver).clear();
		salesforceHomePage.salesforceContactEmail1(driver).type(leadDetails.get(SalesforceConstants.EMAIL));
		salesforceHomePage.salesforceContactPhone1(driver).clear();
		salesforceHomePage.salesforceContactPhone1(driver).type(leadDetails.get(SalesforceConstants.PHONE));
		salesforceHomePage.salesforceContactTitle1(driver).clear();
		salesforceHomePage.salesforceContactTitle1(driver).type(leadDetails.get(SalesforceConstants.TITLE));
		salesforceHomePage.salesforceLeadCompany1(driver).clear();
		salesforceHomePage.salesforceLeadCompany1(driver).type(leadDetails.get(SalesforceConstants.COMPANY));
		salesforceHomePage.salesforceAccountSaveButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountSaveButton(driver).click();
		Thread.sleep(5000);
		Reporter.log("Created or edited a leads name "+leadDetails.get(SalesforceConstants.FIRST_NAME)+" "+leadDetails.get(SalesforceConstants.LAST_NAME), true);
	}
	
	public void gotoReportsTab(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportTab(driver).waitForLoading(driver);
		salesforceHomePage.salesforceReportTab(driver).click();
		Thread.sleep(5000);
	}

	public void createReport(WebDriver driver, Map<String, String> reportDetails) throws InterruptedException{
		Reporter.log("Creating a new report name: "+reportDetails.get(SalesforceConstants.REPORT_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceReportTab(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportTab(driver).click();
		salesforceHomePage.salesforceReportNewReportButton(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportNewReportButton(driver).click();
		salesforceHomePage.salesforceQuickFindInput(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceQuickFindInput(driver).clear();
		salesforceHomePage.salesforceQuickFindInput(driver).type(reportDetails.get(SalesforceConstants.REPORT_TYPE));
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportType(driver, reportDetails.get(SalesforceConstants.REPORT_TYPE)).waitForLoading(driver);
		salesforceHomePage.salesforceReportType(driver, reportDetails.get(SalesforceConstants.REPORT_TYPE)).click();
		salesforceHomePage.salesforceCreateReportButton(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceCreateReportButton(driver).click();
		salesforceHomePage.salesforceSaveReportButton(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceSaveReportButton(driver).click();
		salesforceHomePage.salesforceReportName(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportName(driver).type(reportDetails.get(SalesforceConstants.REPORT_NAME));
		salesforceHomePage.salesforceReportUniqueName(driver).clear();
		salesforceHomePage.salesforceReportUniqueName(driver).type(reportDetails.get(SalesforceConstants.REPORT_NAME));
		salesforceHomePage.salesforceReportDescription(driver).clear();
		salesforceHomePage.salesforceReportDescription(driver).type(reportDetails.get(SalesforceConstants.REPORT_DESCRIPTION));
		Thread.sleep(5000);
		salesforceHomePage.salesforceSaveReportDlgButton(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceSaveReportDlgButton(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportCloseButton(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportCloseButton(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportSaveAndCloseDlgButton(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportSaveAndCloseDlgButton(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportTab(driver).waitForElementPresent(driver);
		Reporter.log("Created a new report name: "+reportDetails.get(SalesforceConstants.REPORT_NAME), true);
	}
	
	public void editReport(WebDriver driver, Map<String, String> reportDetails) throws InterruptedException{
		Reporter.log("Editing a report name: "+reportDetails.get(SalesforceConstants.REPORT_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceReportTab(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportTab(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportNameLink(driver, reportDetails.get(SalesforceConstants.REPORT_NAME)).waitForLoading(driver);
		salesforceHomePage.salesforceReportNameLink(driver, reportDetails.get(SalesforceConstants.REPORT_NAME)).click();
		salesforceHomePage.salesforceReportEditSave(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportEditSave(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportEditDescription(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportEditDescription(driver).clear();
		salesforceHomePage.salesforceReportEditDescription(driver).type(reportDetails.get(SalesforceConstants.REPORT_DESCRIPTION_EDIT));
		salesforceHomePage.salesforceReportEditSave(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportEditSave(driver).click();
		Thread.sleep(5000);
		Reporter.log("Edited a report name: "+reportDetails.get(SalesforceConstants.REPORT_NAME), true);
	}
	
	public void exportReport(WebDriver driver, Map<String, String> reportDetails) throws InterruptedException{
		Reporter.log("Export report with a name: "+reportDetails.get(SalesforceConstants.REPORT_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceReportTab(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportTab(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportNameLink(driver, reportDetails.get(SalesforceConstants.REPORT_NAME)).waitForLoading(driver);
		salesforceHomePage.salesforceReportNameLink(driver, reportDetails.get(SalesforceConstants.REPORT_NAME)).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportExportDetails(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportExportDetails(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportExportDone(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportExportDone(driver).click();
		Thread.sleep(5000);
		Reporter.log("Exported report with a name: "+reportDetails.get(SalesforceConstants.REPORT_NAME), true);
	}
	
	public void runReport(WebDriver driver, Map<String, String> reportDetails) throws InterruptedException{
		Reporter.log("Run report with a name: "+reportDetails.get(SalesforceConstants.REPORT_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceReportTab(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportTab(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportNameLink(driver, reportDetails.get(SalesforceConstants.REPORT_NAME)).waitForLoading(driver);
		salesforceHomePage.salesforceReportNameLink(driver, reportDetails.get(SalesforceConstants.REPORT_NAME)).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportRun(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportRun(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportRun(driver).waitForElementPresent(driver);
	}
	
	public void deleteReport(WebDriver driver, Map<String, String> reportDetails) throws InterruptedException{
		Reporter.log("Deleting report with a name: "+reportDetails.get(SalesforceConstants.REPORT_NAME), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceReportTab(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportTab(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportNameLink(driver, reportDetails.get(SalesforceConstants.REPORT_NAME)).waitForLoading(driver);
		salesforceHomePage.salesforceReportNameLink(driver, reportDetails.get(SalesforceConstants.REPORT_NAME)).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceReportDelete(driver).waitForElementPresent(driver);
		salesforceHomePage.salesforceReportDelete(driver).click();
		driver.switchTo().alert().accept();
		Reporter.log("Deleted report with a name: "+reportDetails.get(SalesforceConstants.REPORT_NAME), true);
	}
	
	public void uploadFilesForHomeChatter(WebDriver driver, File file) throws InterruptedException{
		Reporter.log("User uploading a file: "+file.getName(), true);
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceHomeChatterFileLink(driver).waitForLoading(driver);
		salesforceHomePage.salesforceHomeChatterFileLink(driver).click();
		salesforceHomePage.salesforceHomeChatterUploadButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceHomeChatterUploadButton(driver).click();
		salesforceHomePage.salesforceHomeChatterBrowserInput(driver).waitForLoading(driver);
		salesforceHomePage.salesforceHomeChatterBrowserInput(driver).type(file.getAbsolutePath());
		Reporter.log("User uploaded a file: "+file.getName(), true);
	}
	
	public void shareFileForHomeChatter(WebDriver driver) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(10000);
		salesforceHomePage.salesforceHomeChatterShareButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceHomeChatterShareButton(driver).click();
		Thread.sleep(10000);
	}
	
	public void shareFileWithPerson(WebDriver driver, String email, String filename) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(10000);
		gotoFilesTab(driver);
		salesforceHomePage.salesforceFilesShareWithPoeple(driver, filename).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareWithPoeple(driver, filename).click();
		salesforceHomePage.salesforceFilesShareEmail(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareEmail(driver).type(email);
		salesforceHomePage.salesforceFilesShareMessage(driver).type("Share Message");
	}
	
	public void viewFileFromHomeChatterTab(WebDriver driver, String filename) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceHomeChatterFileLink(driver).waitForLoading(driver);
		salesforceHomePage.salesforceHomeChatterFileLink(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceHomeChatterFilenameLink(driver, filename).waitForLoading(driver);
		salesforceHomePage.salesforceHomeChatterFilenameLink(driver, filename).click();
	}
	
	public void downloadFileFromHomeChatterTab(WebDriver driver, String filename){
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		//salesforceHomePage.salesforceHomeChatterDownloadFileLink(driver, filename).waitForLoading(driver);
		salesforceHomePage.salesforceHomeChatterDownloadFileLink(driver, filename).click();
	}
	
	public void deleteFileFromHomeChatterTab(WebDriver driver, String filename) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceHomeChatterDeleteDropdown(driver, filename).waitForLoading(driver);
		salesforceHomePage.salesforceHomeChatterDeleteDropdown(driver, filename).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceHomeChatterDelete(driver, filename).waitForLoading(driver);
		salesforceHomePage.salesforceHomeChatterDelete(driver, filename).click();
		Thread.sleep(3000);
		driver.switchTo().alert().accept();
	}
	
	public void uploadFileFromFilesTab(WebDriver driver, File file, SuiteData suiteData) throws InterruptedException, IOException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		AwsAction awsAction = new AwsAction();
		Thread.sleep(5000);
		salesforceHomePage.salesforceUploadButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceUploadButton(driver).click();
		Thread.sleep(5000);
		awsAction.uploadFileUsingScript(suiteData, file);
		Thread.sleep(5000);
		new GWCommonTest().clickOkInPopup(suiteData);
		salesforceHomePage.salesforceFilesShareClose(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareClose(driver).click();
		Thread.sleep(5000);
	}
	public void uploadFileFromFilesTabCIQ(WebDriver driver, String file, SuiteData suiteData) throws InterruptedException, IOException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		AwsAction awsAction = new AwsAction();
		Thread.sleep(5000);
		salesforceHomePage.salesforceUploadButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceUploadButton(driver).click();
		Thread.sleep(5000);
		awsAction.uploadFileUsingScript1(suiteData, file);
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesShareClose(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareClose(driver).click();
		Thread.sleep(5000);
	}
	
	public void viewAndDownloadFileFromFilesTab(WebDriver driver, String filename) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesFilenameLink(driver, filename).waitForLoading(driver);
		salesforceHomePage.salesforceFilesFilenameLink(driver, filename).click();
		Thread.sleep(30000);
		//salesforceHomePage.salesforceFilesDownloadLink(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesDownloadLink(driver).click();
		Thread.sleep(5000);
	}
	
	public void shareFileViaLinkFromFilesTab(WebDriver driver, String filename) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(10000);
		salesforceHomePage.salesforceFilesShareDropdown(driver, filename).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareDropdown(driver, filename).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesShareViaLink(driver, filename).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareViaLink(driver, filename).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesShareOk(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareOk(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesShareClose(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareClose(driver).click();
		Thread.sleep(5000);
		
	}
	
	public void deleteFileFromFilesTab(WebDriver driver, String filename) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesFilenameLink(driver, filename).waitForLoading(driver);
		salesforceHomePage.salesforceFilesFilenameLink(driver, filename).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesDeleteLink(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesDeleteLink(driver).click();
		Thread.sleep(3000);
		driver.switchTo().alert().accept();
		Thread.sleep(5000);
	}
	
	public void uploadFileFromAccountsTab(WebDriver driver, File file) throws InterruptedException{
		SalesforceHomePage salesforceHomePage = AdvancedPageFactory.getPageObject(driver, SalesforceHomePage.class);
		salesforceHomePage.salesforceAccountCreateNewDropdown(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountCreateNewDropdown(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountCreateNewFileMenu(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountCreateNewFileMenu(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountUploadFile(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountUploadFile(driver).type(file.getAbsolutePath());
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountShareDropdownButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountShareDropdownButton(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountShareViaLinkMenu(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountShareViaLinkMenu(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceAccountUploadButton(driver).waitForLoading(driver);
		salesforceHomePage.salesforceAccountUploadButton(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesShareOk(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareOk(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesShareClose(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesShareClose(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesDownloadLink(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesDownloadLink(driver).click();
		Thread.sleep(5000);
		salesforceHomePage.salesforceFilesDeleteLink(driver).waitForLoading(driver);
		salesforceHomePage.salesforceFilesDeleteLink(driver).click();
		Thread.sleep(5000);
		driver.switchTo().alert().accept();
	}
	
	
	
	public File createFile(String fileName, String fileExt){
		File originalFile = null, renameFile;
		String filepath = SalesforceConstants.SALESFORCE_ACTION_PATH;
		if(fileExt.equals("txt")){
			originalFile = new File(filepath + File.separator + "sf_txt.txt");
		}
		if(fileExt.equals("xlsx")){
			originalFile = new File(filepath + File.separator + "sf_xlsx.xlsx");
		}
		if(fileExt.equals("docx")){
			originalFile = new File(filepath + File.separator + "sf_docx.docx");
		}
		String newFileName = fileName+"."+fileExt;
		renameFile = new File(filepath+File.separator+newFileName);
		try {
			Files.copy(originalFile, renameFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return renameFile;
	}
	
	public void deleteFile(File file){
		file.delete();
	}
	
	public HashMap<String,String> getInvestigateLogs(Client client, HashMap<String, String> terms, SuiteData suiteData, String fromTime, String message) throws Exception{
		HttpResponse response = null;
		BEAction action = new BEAction();
		Map<String,String> result = new HashMap<String,String>();
		String payLoad = action.payloadCommon(suiteData, terms, fromTime);
		StringEntity entity = new StringEntity(payLoad);
		try {
			response=action.getDisplayLogs (client, suiteData, action.getHeaders(suiteData) ,suiteData.getApiServer(), entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(ClientUtil.getResponseStatusCode(response), HttpStatus.SC_OK, "Response code verification failed");
		String responseBody = ClientUtil.getResponseBody(response);
		/*Logger.info("********************************Response Logs************************************************");
		Logger.info("ResponseBody: "+responseBody);
		Logger.info("****************************************************************************************");*/
		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "hits"), "hits");
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size() < 2){
			Logger.info("********************************Response Logs************************************************");
			Logger.info("ResponseBody: "+responseBody);
			Logger.info("****************************************************************************************");
		}
		for (int i = 0; i < jArray.size(); i++) {
			String source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String messageLog = ClientUtil.getJSONValue(source, "message");
			messageLog = messageLog.substring(1, messageLog.length()-1);
			if(messageLog.equals(message)){
				result = new ObjectMapper().readValue(source, HashMap.class);
				result.remove(GatewayTestConstants.MESSAGE);
				result.put(GatewayTestConstants.MESSAGE, messageLog);
				Logger.info("********************************Response Logs************************************************");
				Logger.info("ResponseBody: "+result);
				Logger.info("****************************************************************************************");
				break;
			}
		}
		if(result.isEmpty()){
			Logger.info("****************************************************************************************");
			Logger.info("************     log not generated/Incorrect log generated     ************");
			Logger.info("Actual Log is <<  "+message+"  >>");
			Logger.info("****************************************************************************************");
		}
		return (HashMap<String, String>) result;
	}
	
	public void verifySalesforceParameters(HashMap<String, String> actual, HashMap<String, String> expected){
		Map<String, String> failPara = new HashMap<String, String>();
		boolean flag = true;
		String errorMessage = null;
		Iterator iteratorNull = actual.entrySet().iterator();
		if(!actual.isEmpty()){
			Logger.info("***********************Check All Parameters not blank/null**********************************");
			while(iteratorNull.hasNext()){
				Map.Entry checkNotNull = (Map.Entry)iteratorNull.next();
				if(checkNotNull.getValue().equals(null) || checkNotNull.getValue().equals("")){
					errorMessage = errorMessage + checkNotNull.getValue() + "is null,";
				}else{
					Logger.info("["+checkNotNull.getKey() + " = " + checkNotNull.getValue()+"] - is not null");
				}
			}
			Logger.info("********************************************************************************************");
			
			Iterator iterator = expected.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry pair = (Map.Entry)iterator.next();
				if(actual.containsKey(pair.getKey())){
					if(actual.get(pair.getKey()).contains( pair.getValue().toString())){
						Logger.info("Verified Parameter: ["+pair.getKey() + " = " + pair.getValue()+"]");
					}
					else{
						flag = false;
						Logger.error("****   Failed Parameter: ["+pair.getKey() + " = " + pair.getValue()+"]");
						failPara.put(pair.getKey().toString(), pair.getValue().toString());
						errorMessage = errorMessage + "(Expected[" +pair.getKey() + " = " + pair.getValue()+ "]ACTUAL[" +pair.getKey() + " = " + actual.get(pair.getKey())+ "]),";
					}
				}else{
					flag = false;
					Logger.error("####  Parameter not found: ["+pair.getKey() + " = " + pair.getValue()+"]");
					errorMessage = errorMessage + "(Parameter not available[" +pair.getKey() + " = " + pair.getValue()+ "]";
				}
			}
		}else{
			flag = false;
		}
		
		
		Iterator failIterator = failPara.entrySet().iterator();
		if(failPara.size()>0){
			Logger.info("********************************Fail Parameter************************************************");
		}
		while(failIterator.hasNext()){
			Map.Entry failpair = (Map.Entry)failIterator.next();
			Logger.error("Expected Parameter: ["+failpair.getKey() + " = " + failpair.getValue()+"]");
			Logger.error("Actual   Parameter: ["+failpair.getKey() + " = " + actual.get(failpair.getKey())+"]");
			Logger.info("********************************************************************************************");
		}
		Assert.assertTrue(flag, errorMessage);
	}
	
	public String camelCasing(String string){
		String newString = "";
		String[] stringArray = string.split(" ");
		for(int i=0;i<stringArray.length; i++){
			char[] charArray = stringArray[i].toCharArray();
			char[] newchar = new char[charArray.length];
			for(int j=0;j<charArray.length; j++){
				if(j==0){
					char c = charArray[j];
					String s = String.valueOf(c);
					s = s.toUpperCase();
					c = s.charAt(0);
					newchar[j] = (char)c;
				}else{
					newchar[j] = charArray[j];
				}
			}
			String charString = String.valueOf(newchar);
			newString = newString.concat(charString);
			newString = newString.concat(" ");
		}
		newString = (String) newString.subSequence(0, newString.length()-1);
		return newString;
	}
	
	public Map<String, String> getGeoLocation(){
		Map<String, String> geoIPData = new HashMap<String, String>();
		Client restClient = new Client();
		HttpResponse response;
		URI uri;
		try {
			uri = ClientUtil.BuidURI("http://ip-api.com/json");
			response = restClient.doGet(uri, null);
			String respBody = ClientUtil.getResponseBody(response);
			geoIPData = new ObjectMapper().readValue(respBody, HashMap.class);
		}catch(Exception e){
			
		}
		return geoIPData;
	}
}
