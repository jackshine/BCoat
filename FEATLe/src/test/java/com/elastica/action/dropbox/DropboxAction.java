package com.elastica.action.dropbox;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.Reporter;
import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.DropboxPage;
import com.elastica.pageobjects.GDrivePage;
import org.openqa.selenium.Keys;

public class DropboxAction extends Action {

	final int ACTIVITY_AND_DELAY = 15;
	final int WAIT_FOR_ELEMENT = 5;
	
	public void login(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		DropboxPage dbp =  AdvancedPageFactory.getPageObject(driver,DropboxPage.class);
		try {
			
			String subWindowHandler = null;
			driver.get(suiteData.getSaasAppBaseUrl());
			hardWait(ACTIVITY_AND_DELAY);
			hardWait(4);
			driver.get(suiteData.getSaasAppBaseUrl());
			hardWait(5);
			dbp.username(driver).waitForLoading(driver);
			dbp.username(driver).type(suiteData.getSaasAppUsername());
			dbp.password(driver).isElementPresent(driver);
			dbp.password(driver).type(suiteData.getSaasAppPassword());
			dbp.signin(driver).click();
			hardWait(ACTIVITY_AND_DELAY);
			//popup handler
			if(dbp.popupWindow(driver).isDisplayed())
			{
				Reporter.log(" inside popup handler ",true);
				dbp.closePopUp(driver).click();
				Reporter.log(" popup closed sucessfully ",true);
			}
			else
			{
				Reporter.log(" popup not encountered ",true);
			}
				
			Reporter.log(" login to dropbox is successfull ",true);
			
			
			
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	
	public boolean relogin(WebDriver driver, SuiteData suiteData) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			driver.get(suiteData.getSaasAppBaseUrl());
			gdp.password(driver).isElementPresent(driver);
			gdp.password(driver).clear();gdp.password(driver).type(suiteData.getSaasAppPassword());
			gdp.signin(driver).click();
			gdp.sidebutton(driver, "My Drive").waitForLoading(driver);
			hardWait(ACTIVITY_AND_DELAY);
			if(driver.getPageSource().contains("My Drive")) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
	}
	
	public void logout(WebDriver driver) throws InterruptedException{
		DropboxPage dbp =  AdvancedPageFactory.getPageObject(driver,DropboxPage.class);
		try {
			dbp.logoutOption(driver).click();
			hardWait(WAIT_FOR_ELEMENT);
			dbp.logout(driver).click();
			hardWait(ACTIVITY_AND_DELAY*3);
			driver.quit();
			
		}catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail("Error in log out " + e.getMessage());
		}
		
	}
	
	public void download(WebDriver driver, String fileName) throws InterruptedException{
		DropboxPage dbp =  AdvancedPageFactory.getPageObject(driver,DropboxPage.class);
		try {
			
			dbp.publicShare(driver,fileName).click();
			hardWait(WAIT_FOR_ELEMENT);
			WebElement downloadSelectedFile = dbp.downloadFile(driver);
			System.out.println(downloadSelectedFile.isDisplayed());
			System.out.println(dbp.downloadFile(driver).isEnabled());
			if (dbp.downloadFile(driver).isEnabled()) {
				downloadSelectedFile.click();
				String subWindowHandler = null;
				hardWait(ACTIVITY_AND_DELAY);
				Set<String> handles = driver.getWindowHandles(); // get all window handles
				Iterator<String> iterator = handles.iterator();
				while (iterator.hasNext()){
					subWindowHandler = iterator.next();
				}
				driver.switchTo().window(subWindowHandler);
			} 
		} catch(Exception e) {
			e.printStackTrace();
			//Assert.fail("Error in download " + e.getMessage());
		}
	}
	
	public void publicShare(WebDriver driver, String fileName) {
		//driver.get(suiteData.getSaasAppBaseUrl());
		DropboxPage dbp =  AdvancedPageFactory.getPageObject(driver,DropboxPage.class);
		try {
			System.out.println(fileName);
			dbp.publicShare(driver,fileName).click();
			System.out.println(dbp.publicShare(driver, fileName).isDisplayed());
			WebElement shareButton =dbp.shareButton(driver, fileName) ;
			System.out.println(dbp.shareButton(driver, fileName).isDisplayed());
			shareButton.click();
			hardWait(ACTIVITY_AND_DELAY);
			WebElement createlink=dbp.createlink(driver); 
			createlink.click();
			hardWait(ACTIVITY_AND_DELAY);
			dbp.closeLink(driver).click();
			System.out.println("close button");
			hardWait(ACTIVITY_AND_DELAY);

		} 
		catch(Exception e) {
			//	driver.close();
			//Assert.fail("Error in publicShare " + e.getMessage());

		}
	}

	
	public void publicUnshare(WebDriver driver, String fileName) {
		//driver.get(suiteData.getSaasAppBaseUrl());
		DropboxPage dbp =  AdvancedPageFactory.getPageObject(driver,DropboxPage.class);
		try{
			dbp.publicShare(driver,fileName).mouseOver(driver);
			WebElement shareButton =dbp.shareButton(driver, fileName) ;
			System.out.println(dbp.shareButton(driver, fileName).isDisplayed());
			shareButton.click();
			hardWait(ACTIVITY_AND_DELAY);
			WebElement linkSetting=dbp.linkSetting(driver); 
			linkSetting.click();
			hardWait(ACTIVITY_AND_DELAY);
			WebElement delLink=dbp.deleteLink(driver); 
			delLink.click();
			hardWait(ACTIVITY_AND_DELAY);
			WebElement delbutton=dbp.deleteButton(driver); 
			delbutton.click();
			hardWait(ACTIVITY_AND_DELAY);
			dbp.closeLink(driver).click();
			System.out.println("close button");
			
			
		hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			//driver.close();
			//Assert.fail("Error in public Unshare " + e.getMessage());
			
		}
		
	}
	
	public void uploadFile(WebDriver driver, String uploadFile) {
		Logger.info("Saas app uploadFile in progress");
		DropboxPage dbp =  AdvancedPageFactory.getPageObject(driver,DropboxPage.class);
		
		try {
			dbp.uploadFileButton(driver).click();
			Thread.sleep(4000);

			File fileLocation   =	new File(uploadFile);

			String absolutePath = 	fileLocation.getAbsolutePath();
			File exeLocation = null;
			System.out.println(dbp.basicUploader(driver).isDisplayed());
			if(dbp.basicUploader(driver).isDisplayed())
			{
				System.out.println("basic downloader button is visible");
				dbp.basicUploader(driver).click();
				
			}
			/*System.out.println(	dbp.fileBox(driver).isDisplayed());
			System.out.println(	dbp.fileBox(driver).isElementPresent(driver));
			System.out.println(	dbp.fileBox(driver).isElementVisible());
			System.out.println(	dbp.fileBox(driver).isEnabled());*/
			
			((JavascriptExecutor) driver).executeScript("document.querySelector('input[type=file]').style.display='inline';");
			((JavascriptExecutor) driver).executeScript("document.querySelector('input[type=file]').style.visibility='visible';");
			
			System.out.println(	dbp.fileBox(driver).isDisplayed());
		/*	System.out.println(	dbp.fileBox(driver).isElementPresent(driver));
			System.out.println(	dbp.fileBox(driver).isElementVisible());
			System.out.println(	dbp.fileBox(driver).isEnabled());
			System.out.println(	dbp.fileBox(driver).isDisplayed());*/
			
			dbp.fileBox(driver).type(absolutePath);
			Logger.info("uploadFile in progress");
			hardWait(ACTIVITY_AND_DELAY);
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		Logger.info("completed successfully");
	}

	public void deleteFile(WebDriver driver, String fileName) {
		Logger.info("Starting delete file operation ");
		DropboxPage dbp =  AdvancedPageFactory.getPageObject(driver,DropboxPage.class);
		try {
			hardWait(WAIT_FOR_ELEMENT);
			dbp.publicShare(driver,fileName).click();

			if(dbp.deleteFile(driver).isDisplayed())
			{
				dbp.deleteFile(driver).click();
				dbp.dialogDeleteButton(driver).click();
				hardWait(WAIT_FOR_ELEMENT);
				Logger.info("delete file operation successfull");
			}
		} 
		catch(Exception e) {
			//	driver.close();
			Assert.fail("Error in publicShare " + e.getMessage());

		}
	}
	
	public void homepage(WebDriver driver, SuiteData suiteData)throws InterruptedException{
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		driver.get(suiteData.getBaseUrl());
		gdp.sidebutton(driver, "My Drive").waitForLoading(driver, WAIT_FOR_ELEMENT);
	}
	
	public void selectFolder(WebDriver driver, String folder) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			gdp.sidebutton(driver, "My Drive").waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.sidebutton(driver, "My Drive").click();
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, folder)));
			selectedDocElement.isDisplayed();
			selectedDocElement.click();
			doubleClick(driver, selectedDocElement);
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	public void CreateFolder(WebDriver driver, String folder) {
		Logger.info("Starting delete file operation ");
		DropboxPage dbp =  AdvancedPageFactory.getPageObject(driver,DropboxPage.class);
		try {
			hardWait(WAIT_FOR_ELEMENT);
			dbp.createfolder(driver).click();
			// create new folder
			dbp.newfolder(driver).type(folder);
			dbp.newfolder(driver).sendKeys(Keys.RETURN);
			hardWait(WAIT_FOR_ELEMENT);

			Logger.info("create  folder operation successfull");

		} 
		catch(Exception e) {
			//	driver.close();
			Assert.fail("Error in publicShare " + e.getMessage());

		}
	}
	


	/**
	 * doubleClick is used to double click element
	 * @param driver
	 * @param element
	 */
	private void doubleClick(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).doubleClick().build().perform();
	}



}
