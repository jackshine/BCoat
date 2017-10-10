package com.elastica.action.azure;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.SettingsPage;
import com.elastica.pageobjects.azure.AzurePage;
import com.elastica.pageobjects.box.BoxPage;


public class AzureAction extends Action{
	

	public void login(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("User credentials: "+suiteData.getSaasAppUsername()+"/"+suiteData.getSaasAppPassword());
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		driver.get(suiteData.getSaasAppBaseUrl());hardWait(10);
		azp.emailId(driver).clear();azp.emailId(driver).type(suiteData.getSaasAppUsername());
		azp.password(driver).clear();azp.password(driver).type(suiteData.getSaasAppPassword());
		azp.loginButton(driver).click();
		hardWait(2);
		azp.loginButton(driver).click();
		hardWait(10);
//		azp.loginButton(driver).mouseOverClick(driver);hardWait(10);
		azp.accountLink(driver).waitForElementToBeVisible(driver);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void invalidLogin(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("User credentials: "+suiteData.getSaasAppUsername()+"/"+"wrongPassword");
		
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		driver.get(suiteData.getSaasAppBaseUrl());hardWait(20);
		
		if(azp.loginUserAnotherAccountLink(driver).isElementPresent(driver)){
			azp.loginUserAnotherAccountLink(driver).click();hardWait(5);
		}
		azp.emailId(driver).clear();azp.emailId(driver).type(suiteData.getSaasAppUsername());
		azp.password(driver).clear();azp.password(driver).type("wrongPassword");
		azp.loginButton(driver).click();
		hardWait(2);
		azp.loginButton(driver).click();
		hardWait(10);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void logout(WebDriver driver) throws InterruptedException{
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		azp.accountLink(driver).waitForElementToBeVisible(driver);
		azp.accountLink(driver).click();hardWait(10);
		azp.logoutLink(driver).click();hardWait(20);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void clickOnSideBarLink(WebDriver driver, String linkText, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("Clicking on side bar link:"+linkText );
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		azp.sidebarLink(driver, linkText).click();	hardWait(waitTime);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void accessActionMenu(WebDriver driver, String vmName, String vmOperation, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("Clicking on Action Menu:"+vmOperation+ " for VM:"+vmName );
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		azp.contextMenu(driver, vmName).click();hardWait(2);
		azp.contextMenuItem(driver, vmOperation).click();hardWait(5);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public boolean checkServiceExists(WebDriver driver, String vmName){
		Logger.info("Checking if VM already present");
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		if(azp.serviceItemLabel(driver, vmName).isElementPresent(driver))
		{
			Logger.info("VM already exists: "+vmName);
			return true;
		}
		else{
			Logger.info("VM doesn't exists: "+vmName);
			return false;
		}
//		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void vmOperations(WebDriver driver, String vmName,String vmOperation, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("Virtual MachineName:"+vmName + " Action:"+vmOperation);
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		
		
		
//		azp.sidebarLink(driver, "Virtual machines").waitForElementToBeVisible(driver);
		clickOnSideBarLink(driver, "Virtual machines",30);
		azp.contextMenu(driver, vmName).waitForElementToBeVisible(driver);
		azp.contextMenu(driver, vmName).click();hardWait(10);
		
//		azp.contextMenuItem(driver, vmOperation).waitForElementToBeVisible(driver);
		
		
		if( vmOperation.equals("Start") && azp.contextMenuTopElement(driver,vmOperation ).getAttribute("class").contains("itemDisabled"))
		{
			Logger.info("Stopping the currently running vm");
			accessActionMenu(driver, vmName, "Stop", 10);
			if(azp.cofirmMessageYesButton(driver).isDisplayed()){
				azp.cofirmMessageYesButton(driver).click();
				Logger.info("Waiting for 3 minute for service to stop");
				hardWait(180);
			}
			
		}
		else if( vmOperation.equals("Stop") && azp.contextMenuTopElement(driver,vmOperation ).getAttribute("class").contains("itemDisabled")){
			Logger.info("Starting the currently stopped vm");
			accessActionMenu(driver, vmName, "Start", 10);
			if(azp.cofirmMessageYesButton(driver).isDisplayed()){
				azp.cofirmMessageYesButton(driver).click();
				Logger.info("Waiting for 3 minute for service to start");
				hardWait(180);
			}
		}
		
		accessActionMenu(driver, vmName, vmOperation, 10);
		
		
		if(azp.cofirmMessageYesButton(driver).isDisplayed()){
			azp.cofirmMessageYesButton(driver).click();hardWait(waitTime);
		}
		else{
			Logger.info("Confirmation message is not dispalyed");
		}
		
		if(vmOperation.equals("Start")){
			Logger.info("Waiting for 5 minutes to start the virtual machine");
			hardWait(300);
		}
		else{
			Logger.info("Waiting for 3 minute after the operation");
			hardWait(180);
		}
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void certificateOperations(WebDriver driver, String cldServName,String certOperation, int waitTime, String certFile){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("Cloud service name"+cldServName + "Item:Certificate Action:"+certOperation);
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		
		
		
//		azp.sidebarLink(driver, "Virtual machines").waitForElementToBeVisible(driver);
		clickOnSideBarLink(driver, "Cloud services (classic)",15);
		azp.contextMenu(driver, cldServName).waitForElementToBeVisible(driver);
//		azp.contextMenu(driver, cldServName).click();hardWait(10);
		if((checkServiceExists(driver, cldServName)==true)){
			azp.serviceItemLabel(driver, cldServName).click();hardWait(2);
			azp.linkInPage(driver, "Settings").click();hardWait(2);
			azp.elementInSettings(driver, "Certificates").click();hardWait(2);
			
			boolean noCertificate = azp.certificateNameText(driver,"No certificates found.").isElementPresent(driver);
			
			//if certificate upload action or (delete action when no certificate present) then upload certificate
			if(certOperation.equals("Upload") || (certOperation.equals("Delete") && noCertificate)){
				azp.iconUnderSettings(driver, "Certificates","Upload").click();hardWait(2);
				azp.uploadFileInput(driver).type(certFile);hardWait(2);
				if(azp.uploadFilePassword(driver).isEnabled()){
					azp.uploadFilePassword(driver).type("test");hardWait(2);
				}
				azp.uploadButton(driver).click();hardWait(30);
			}
			
			if(certOperation.equals("Delete")){
				String certicateName="E=service-engineering@elastica.co, CN=Elastica, OU=Development, O=\"Elastica, Inc.\", L=San Jose, S=California, C=US";
				azp.certificateNameText(driver,certicateName).click();hardWait(3);
				azp.iconUnderSettings(driver, "Certificates","Delete").click();hardWait(2);
				if(azp.cofirmMessageYesButton2(driver).isElementPresent(driver)){
					azp.cofirmMessageYesButton2(driver).click();
					Logger.info("Waiting for "+certOperation+" to complete.");
					hardWait(30);
				}
				else{
					Logger.info("Confirmation message not displayed");
				}
			}
		}
		else{
			Logger.info("Cloud service:"+ cldServName + "is not found, please add the same manually and run the test!" );
		}
		
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void createVM(WebDriver driver, String vmName,String userName, String password, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("Virtual MachineName:"+vmName );
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		
//		azp.sidebarLink(driver, "Virtual machines").waitForElementToBeVisible(driver);
		clickOnSideBarLink(driver, "Virtual machines",20);
		azp.addItemLink(driver).click();
		hardWait(waitTime);
//		azp.addVMServerLink(driver,"WindowsServer2012R2Datacenter").waitForElementToBeVisible(driver);
		azp.addVMServerLink(driver,"WindowsServer2012R2Datacenter").click();
		hardWait(waitTime);
//		azp.selectVMServerLink(driver,"Windows Server 2008 R2 SP1").waitForElementToBeVisible(driver);
		azp.selectVMServerLink(driver,"Windows Server 2008 R2 SP1").click();
		hardWait(waitTime);
//		azp.createButton(driver).waitForElementToBeVisible(driver);
		azp.createButton(driver).click();
		hardWait(waitTime);
//		azp.createButton(driver).waitForElementToBeVisible(driver);
//		azp.OkOrSelectButton(driver, "OK").waitForElementToBeVisible(driver);
		hardWait(waitTime);
		azp.formPageLink(driver).click();
		azp.formDetailsInputText(driver, "Name").type(vmName);hardWait(2);
		azp.formDetailsInputText(driver, "User name").type(userName);hardWait(2);
		azp.formDetailsInputText(driver, "Password").type(password);hardWait(2);
		azp.formDetailsDropdownArrowLink(driver, "Subscription").waitForElementToBeVisible(driver);
//		hardWait(2);
		azp.formDetailsDropdownArrowLink(driver, "Subscription").click();hardWait(5);
		azp.DropdownSelectItemLink(driver, "Pay-As-You-Go").click();
//		azp.formDetailsDropdownArrowLink(driver, "Resource group").waitForElementToBeVisible(driver);
//		hardWait(10);
		azp.useExistingRadioLink(driver, "Use existing").click();hardWait(5);
		azp.formDetailsDropdownArrowLink(driver, "Resource group").click();hardWait(5);
		azp.DropdownSelectItemLink(driver, "AutomationResourceGroup").click();hardWait(2);
		
//		String newResourceGrpName= getTimestamp();
//		azp.formDetailsInputText(driver, "New resource group name").type(newResourceGrpName);hardWait(2);
		
		azp.formDetailsDropdownArrowLink(driver, "Location").click();hardWait(10);
		azp.DropdownSelectItemLink(driver, "North Central US").click();hardWait(5);
		azp.OkOrSelectButton(driver, "OK").click();
//		azp.OkOrSelectButton(driver, "Select").waitForElementToBeVisible(driver);
		hardWait(waitTime);
		azp.vmSizeCodeLink(driver, "D1").click();hardWait(10);
//		azp.formPageBottomLink(driver).click();
		azp.OkOrSelectButton(driver, "Select").click();hardWait(waitTime);
//		azp.formPageBottomLink(driver).click();
		azp.OkOrSelectButton(driver, "OK").click();hardWait(waitTime);
//		azp.serviceItemLabel(driver, "Validation passed").waitForElementToBeVisible(driver);
		azp.OkOrSelectButton(driver, "OK").click();
//		azp.notificationLink(driver).click();hardWait(2);
		Logger.info("Waiting for 10 minute for the virtual machine creation");
		hardWait(600);
//		azp.notificationStatusInprogress(driver).waitForElementToBeVisible(driver);
//		azp.notificationStatusCompleted(driver).waitForElementToBeVisible(driver);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void createDatabase(WebDriver driver, String vmName,String userName, String password, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("Virtual MachineName:"+vmName );
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		
		azp.sidebarLink(driver, "SQL databases").waitForElementToBeVisible(driver);
		clickOnSideBarLink(driver, "SQL databases",20);
		azp.addItemLink(driver).click();
		hardWait(waitTime);

		azp.OkOrSelectButton(driver, "OK").waitForElementToBeVisible(driver);
		hardWait(waitTime);
		azp.formPageLink(driver).click();
		azp.formDetailsInputText(driver, "Name").type(vmName);
		azp.formDetailsInputText(driver, "User name").type(userName);
		azp.formDetailsInputText(driver, "Password").type(password);hardWait(2);
		azp.formDetailsDropdownArrowLink(driver, "Subscription").waitForElementToBeVisible(driver);
//		hardWait(2);
//		azp.formDetailsDropdownArrowLink(driver, "Subscription").click();hardWait(2);
//		azp.DropdownSelectItemLink(driver, "Pay-As-You-Go").click();
//		azp.formDetailsDropdownArrowLink(driver, "Resource group").waitForElementToBeVisible(driver);
//		hardWait(10);
//		azp.formDetailsDropdownArrowLink(driver, "Resource group").click();hardWait(2);
//		azp.DropdownSelectItemLink(driver, "AutomationResourceGroup").click();hardWait(2);
		
		String newResourceGrpName= getTimestamp();
		azp.formDetailsInputText(driver, "New resource group name").type(newResourceGrpName);hardWait(2);

		azp.formDetailsDropdownArrowLink(driver, "Location").click();hardWait(10);
		azp.DropdownSelectItemLink(driver, "North Central US").click();hardWait(5);
//		azp.createButton(driver).waitForElementToBeVisible(driver);
		azp.OkOrSelectButton(driver, "OK").click();
//		azp.OkOrSelectButton(driver, "Select").waitForElementToBeVisible(driver);
		hardWait(waitTime);
		azp.vmSizeCodeLink(driver, "DS1").click();hardWait(10);
//		azp.formPageBottomLink(driver).click();
		azp.OkOrSelectButton(driver, "Select").click();hardWait(waitTime);
//		azp.formPageBottomLink(driver).click();
		azp.OkOrSelectButton(driver, "OK").click();hardWait(waitTime);
//		azp.serviceItemLabel(driver, "Validation passed").waitForElementToBeVisible(driver);
		azp.OkOrSelectButton(driver, "OK").click();
//		azp.notificationLink(driver).click();hardWait(2);
		hardWait(30+waitTime);
//		azp.notificationStatusInprogress(driver).waitForElementToBeVisible(driver);
//		azp.notificationStatusCompleted(driver).waitForElementToBeVisible(driver);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void createSQLDatabase(WebDriver driver, String dbName, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("Databasename:"+dbName );
		AzurePage azp =  AdvancedPageFactory.getPageObject(driver, AzurePage.class);
		
		azp.sidebarLink(driver, "SQL databases").waitForElementToBeVisible(driver);
		clickOnSideBarLink(driver, "SQL databases",30);
		azp.formDetailsInputText(driver, "Database name").waitForElementToBeVisible(driver);
		azp.formDetailsInputText(driver, "Database name").type(dbName);
		azp.createButton(driver);
		
		
		Logger.info("Waiting for 1 minute after the operation");
		hardWait(60);
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public String getTimestamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
		Date date = new Date();
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
	
	
//	public void grantAccess(WebDriver driver, SuiteData suiteData) throws InterruptedException{
//		AzureAction azp =  AdvancedPageFactory.getPageObject(driver, AzureAction.class);
//		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
//		String title = driver.getTitle();
//		sp.idpConfigureButton(driver).click();
//		Thread.sleep(5000);
//		switchToWindowByTitle("Sign in to Microsoft App Access Panel", driver);
//		azp.emailId(driver).waitForElementPresent(driver, 60);
//		// Sign using Microsoft email id
//		azp.emailId(driver).type(suiteData.getSaasAppUsername());
//		azp.loginButton(driver).click();
//		//Select Microsoft Account
//		azp.microsoftAccount(driver).click();
//		azp.username(driver).waitForElementPresent(driver, 60);
//		azp.username(driver).type(suiteData.getSaasAppUsername());
//		azp.password(driver).type(suiteData.getSaasAppPassword());
//		azp.signinbutton(driver).click();
//		// Click Grant Access By Switch in iframe
//		switchFrame(driver.findElement(azp.getBy(azp.grantaccessiframe(driver))), driver);
//		azp.grantaccessbutton(driver).waitForElementPresent(driver, 60);
//		azp.grantaccessbutton(driver).click();
//		driver.switchTo().defaultContent();
//		Thread.sleep(5000);
//		//Logout 
//		//driver.get(suiteData.getSsoBaseurl() + "/Account/Logout");
//		//Thread.sleep(5000);
//		// Go To CloudSoc
//		//driver.get(suiteData.getBaseUrl());
//		//Thread.sleep(5000);
//		switchToWindowByTitle(title, driver);
//		Logger.info("Granted Access");
//	}

}	