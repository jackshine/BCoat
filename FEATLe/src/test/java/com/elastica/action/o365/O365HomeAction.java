package com.elastica.action.o365;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.cluster.routing.allocation.allocator.GatewayAllocator;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Reporter;
import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.constants.CommonConstants;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.LoginPage;
import com.elastica.pageobjects.O365HomePage;
import com.elastica.pageobjects.O365LoginPage;


public class O365HomeAction extends Action{
	Exception exception = null;
	final int ACTIVITY_AND_DELAY = 15;
	final int WAIT_FOR_ELEMENT = 5;
	final String DASH_BOARD_URL = "/Admin/Default.aspx?switchtoclassic=true#EAdminDefaultPage_AdminHomePageESKU_AdminDashboardPage";

	public void loadEmailApp(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading o365 mail app");
		driver.get(GatewayTestConstants.O365_EMAIL_APP_LINK);
			/*try {	
				lp.emailIcon(driver).waitForLoading(driver);
				lp.emailIcon(driver).click();
				lp.newemailbutton(driver).waitForLoading(driver);
			} catch(Exception e) {
				exception = e;
		}*/
		waitForPageToLoad(driver, "Mail", 30);
		//hardWait(10);
		Logger.info("o365 mail app loadded successful");
	}
	
	public void loadAdminApp(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading o365 mail app");
		try {	
			lp.modulenIcon(driver, "AdminTile_link").waitForLoading(driver);
			lp.modulenIcon(driver, "AdminTile_link").click();
		} catch(Exception e) {
			exception = e;
		}
		Logger.info("o365 admin app loadded successful");
	}

	public void changeToOldStyle(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading o365 mail app");
		try {	
			lp.adminChangeToOldStyle(driver).waitForLoading(driver);
			lp.adminChangeToOldStyle(driver).click();
		} catch(Exception e) {
			exception = e;
		}
		Logger.info("o365 admin app loadded successful");
	}
	public void getPortal(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		Logger.info("Relogin"+suiteData.getSaasAppUsername()+"/"+suiteData.getSaasAppPassword());
		O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
		driver.get(suiteData.getSaasAppBaseUrl());
		Logger.info("Relogin using  "+suiteData.getSaasAppPassword() +" successful");
	}
	public void createSite(WebDriver driver, SuiteData suiteData,String siteName) throws InterruptedException{
		Logger.info("starting creating new site");
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try{
		lp.createSite(driver).click();
		hardWait(ACTIVITY_AND_DELAY);
		lp.inputSiteName(driver).type(siteName);
		lp.createButton(driver).click();
		} catch(Exception e) {
			exception = e;
			Logger.info(" Failed site creation");
		}
		Logger.info("successful - site creation");
	}
	
	
	public void loadhomePage(WebDriver driver, String homeLink) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading home page");
			try {	
				driver.get(homeLink);
				lp.onedriveIcon(driver).waitForLoading(driver);
			} catch(Exception e) {
				exception = e;
			}
			Logger.info("Home page load successful");
	}
	
	public void loadhomePagePersonal(WebDriver driver, String homeLink) {
		Logger.info("Loading home page");
			try {	
				driver.get(homeLink);
			} catch(Exception e) {
				exception = e;
			}
			Logger.info("Home page load successful");
	}
	
	public void loadgeneralhomepage(WebDriver driver, String homeLink) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading home page");
			try {	
				driver.get(homeLink);
			} catch(Exception e) {
				exception = e;
			}
			Logger.info("Home page load successful");
		}
	
	public void loadOnedriveApp(WebDriver driver) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading one drive App");
			try {	
				lp.onedriveIcon(driver).click();
			} catch(Exception e) {
				exception = e;
			}
			Logger.info("One drive App loadded successfully");
	}
	
	public void loadOnedriveAppViaLink(WebDriver driver, String oneDriveLink) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading one drive App");
		driver.get(oneDriveLink);
		waitForPageToLoad(driver, "OneDrive", 30);
		Logger.info("One drive App loadded successfully");

	}
	
	public void loadSharePointApp(WebDriver driver) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading SharePoint App");
			try {	
				lp.sharePointIcon(driver).click();
			} catch(Exception e) {
				exception = e;
			}
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("SharePoint App loadded successfully");
	}

	public void spTeamSite(WebDriver driver) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading one drive App");
			try {	
				lp.spTeamSite(driver).click();
				Reporter.log("Performing saas app file upload acition for file ", true);
				String Parent_Window = driver.getWindowHandle();    
			      // Switching from parent window to child window   
			     for (String Child_Window : driver.getWindowHandles()){  
			    	 driver.switchTo().window(Child_Window); 
			    	 driver.getCurrentUrl();
					System.out.println("get child url: "+driver.getTitle());
			     }  
			     //Switching back to Parent Window 
			     System.out.println("get child url: "+driver.getTitle());
			} catch(Exception e) {
				exception = e;
				Logger.info(" Team site  load Failed");
			}
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info(" Team site  loaded successfully");
	}
	public void spTeamSiteUpload(WebDriver driver,String filePath ) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading one drive App");
		String fileName = filePath.substring(filePath.lastIndexOf(File.separator) +1, filePath.length());
			try {	
//				lp.spTeamSite(driver).click();
//				Reporter.log("Performing saas app file upload acition for file "+fileName, true);
//				String Parent_Window = driver.getWindowHandle();    
//			      // Switching from parent window to child window   
//			     for (String Child_Window : driver.getWindowHandles()){  
//			    	 driver.switchTo().window(Child_Window); 
//			    	 driver.getCurrentUrl();
//					System.out.println("get child url: "+driver.getTitle());
//			     }  
//			     //Switching back to Parent Window 
//			     System.out.println("get child url: "+driver.getTitle());
			     hardWait(ACTIVITY_AND_DELAY);
			     lp.spUpload(driver).click();
			    // driver.findElement(By.xpath(".//*[@id='QCB1_Button2']")).click();
			 // driver.switchTo().frame(lp.spFrame(driver));
			     driver.switchTo().frame(driver.findElement(By.xpath("//.[@class='ms-dlgFrameContainer']/iframe")));
			     hardWait(WAIT_FOR_ELEMENT);
			    // driver.findElement(By.xpath(".//input[contains(@id,'InputFile')]")).sendKeys(filePath);//.sendKeys(filePath);
			     lp.spUploadFileButton(driver).type(filePath);
			     hardWait(WAIT_FOR_ELEMENT);
			     lp.spOk(driver).click();
			     
			     hardWait(WAIT_FOR_ELEMENT);
			    // driver.findElement(By.xpath(".//input[@value='OK']")).click();
			     //.//*[@id='ctl00_PlaceHolderMain_ctl04_RptControls_btnOK']
			     Reporter.log("**Completed saas app file upload acition for file "+fileName, true);
			hardWait(10);
			} catch(Exception e) {
				 Reporter.log(fileName+"  **file cann't be uploaded ", true);
				exception = e;
				e.printStackTrace();
			}
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("One drive App loadded successfully");
	}

	
	public void loadOnedriveAppPersonal(WebDriver driver) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading one drive App");
			try {	
				lp.oneDriveIconPersonal(driver).click();
			} catch(Exception e) {
				exception = e;
			}
			Logger.info("One drive Personal loadded successfully");
	}
	
	public void newButton(WebDriver driver) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading one drive App");
			try {	
				lp.newButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				exception = e;
			}
			Logger.info("One drive Personal loadded successfully");
	}
	
	public void CreateNewFolder(WebDriver driver,String Fname) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("New folder creation starting");
			try {	
				lp.newButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				
				lp.newfolder(driver).click();;
				lp.newFolderRenam(driver).type(Fname);
				lp.createButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				exception = e;
				Logger.info(" New folder creation failed");
			}
			Logger.info("New folder creation failed successfully");
	}
	
	public void CreateNewDoc(WebDriver driver,String Fname,String user) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading one drive App");
			try {	
				lp.newButton(driver).click();
				lp.newWordDoc(driver).click();
					hardWait(ACTIVITY_AND_DELAY*3);
				driver.switchTo().frame("WebApplicationFrame");
				hardWait(ACTIVITY_AND_DELAY);
//				String Parent_Window = driver.getWindowHandle();    
//			      // Switching from parent window to child window   
//			     for (String Child_Window : driver.getWindowHandles()){  
//			    	 driver.switchTo().window(Child_Window); 
//			    	 driver.getCurrentUrl();
//					System.out.println("get child url: "+driver.getTitle());
//			     }
//				
				lp.newDocText(driver).type(Fname);
				hardWait(ACTIVITY_AND_DELAY);
				//share after creating
				lp.newDocShare(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.toShare(driver).type(user);
				lp.shareButton(driver).click();
				
			} catch(Exception e) {
				exception = e;
			}
			Logger.info("One drive Personal loadded successfully");
	}
	
	public void siteShare(WebDriver driver,String user) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading one drive App");
			try {				
				//share after creating
				lp.siteShareLink(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.toShare(driver).type(user);
				lp.shareButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				
			} catch(Exception e) {
				exception = e;
			}
			Logger.info("One drive Personal loadded successfully");
	}
	public void editSite(WebDriver driver) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Loading one drive App");
			try {				
				//share after creating
				lp.editSite(driver).click();
				hardWait(ACTIVITY_AND_DELAY*2);
				
				lp.saveSite(driver).click();
				hardWait(ACTIVITY_AND_DELAY*2);
				
			} catch(Exception e) {
				exception = e;
			}
			Logger.info("site edit actin failed");
	}
	public void renameItem(WebDriver driver, String original, String rename) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		Logger.info("Rename item " + original + " Renamed to "  + rename);
		try {
			driver.manage().window().maximize();
			if (selectItem(driver, original)) {
				if (o365Home.topmenu(driver, "Rename").isDisplayed()) {
					o365Home.topmenu(driver, "Rename").click();
					hardWait(2);
				} else {
					o365Home.moreaction(driver).waitForLoading(driver);
					o365Home.moreaction(driver).click();
					hardWait(2);
					o365Home.moreactionsubmenu(driver, "Rename").mouseOver(driver);
					o365Home.moreactionsubmenu(driver, "Rename").click();
					hardWait(2);
				}
				o365Home.dialogboxinput(driver).clear();
				o365Home.dialogboxinput(driver).type(rename);
				hardWait(5);
				o365Home.savebutton(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).perform();
				hardWait(5);
				Logger.info("Rename file completed successful, Rename file name" + rename  + " Original File " + original);
			}
			
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void renameItemtopmenu(WebDriver driver, String original, String rename) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		Logger.info("Rename top menu " + original + " Renamed to "  + rename);
		try {
			if (selectItem(driver, original)) {
				hardWait(5);
				o365Home.topmenu(driver, "Rename").click();
				hardWait(5);
				o365Home.dialogboxinput(driver).clear();
				o365Home.dialogboxinput(driver).type(rename);
				o365Home.savebutton(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).perform();
				hardWait(ACTIVITY_AND_DELAY);
				Logger.info("Successful Rename top menu " + original + " Renamed to "  + rename);
			}
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void selectView(WebDriver driver) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {
			if (o365Home.listView(driver).isDisplayed()) {
				o365Home.listView(driver).click();
				Logger.info("Select View");
			}
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void renameFolder(WebDriver driver, String original, String rename) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		Logger.info("Rename " + original + " Renamed to "  + rename);
		try {
			if (selectFileName(driver, original)) {
				o365Home.topmenu(driver, "Rename").waitForLoading(driver);
				o365Home.topmenu(driver, "Rename").click();
				hardWait(2);
				o365Home.dialogboxinputgeneral(driver, "Enter your new name").clear();
				o365Home.dialogboxinputgeneral(driver, "Enter your new name").type(rename);
				o365Home.savebutton(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).perform();
				hardWait(ACTIVITY_AND_DELAY);
				Logger.info("Successful Rename " + original + " Renamed to "  + rename);
			}
		} catch(Exception e) {
			exception = e;
		}
	}

	public void goToFolder(WebDriver driver, String folder ) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Go To Folder " + folder);
		hardWait(ACTIVITY_AND_DELAY);
		try {
			o365Home.gotofolder(driver, folder).waitForLoading(driver);
			o365Home.gotofolder(driver, folder).click();
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("Successful Go To Folder " + folder);
		} catch(Exception e) {
			createFolder(driver, folder);
			o365Home.gotofolder(driver, folder).waitForLoading(driver);
			o365Home.gotofolder(driver, folder).click();
			exception = e;
			Logger.info("Successful Go To Folder " + folder);
		}
	}
			
	public void moveItem(WebDriver driver, String original, String folderName) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		Logger.info("Move item to " + original + " to "  + folderName);
		try {
			if (selectItem(driver, original)) {
				o365Home.topmenu(driver, "Move to").waitForLoading(driver);
				o365Home.topmenu(driver, "Move to").click();
				hardWait(5);
				o365Home.selectfolder(driver, folderName).click();
				hardWait(2);
				o365Home.moveButton(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).perform();
				hardWait(ACTIVITY_AND_DELAY);
				Logger.info("Successful Move item to " + original + " to "  + folderName);
			}
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public boolean selectItem(WebDriver driver, String original) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Select item " + original);
		try {
			if(o365Home.selecteditem(driver, original).isDisplayed())
				return true;
			else {
				selectFileName(driver, original);
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public void copyItem(WebDriver driver, String original, String folderName) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		Logger.info("Copy item to " + original + " to "  + folderName);
		try {
			if (selectItem(driver, original)) {
				hardWait(5);
				if (o365Home.topmenu(driver, "Copy to").isDisplayed()) {
					o365Home.topmenu(driver, "Copy to").click();
				} else {
					o365Home.moreaction(driver).waitForLoading(driver);
					o365Home.moreaction(driver).click();
					hardWait(2);
					o365Home.moreactionsubmenu(driver, "Copy to").mouseOver(driver);
					o365Home.moreactionsubmenu(driver, "Copy to").click();
					hardWait(2);
				}
				hardWait(5);
				o365Home.selectfolder(driver, folderName).click();
				hardWait(2);
				o365Home.copyButton(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).perform();
				hardWait(ACTIVITY_AND_DELAY);
				Logger.info("Successful Copy item to " + original + " to "  + folderName);
			}
		} catch(Exception e) {
			exception = e;
		}
	}

	public void createFolder(WebDriver driver, String folderName) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		Logger.info("Create Folder " + folderName);
		try {
			o365Home.create(driver).waitForLoading(driver);
			hardWait(8);
			o365Home.create(driver).click();
			hardWait(2);
			o365Home.submenu(driver, "Folder").click();
			hardWait(5);
			o365Home.dialogboxinputgeneral(driver, "Enter your folder name").waitForLoading(driver);
			o365Home.dialogboxinputgeneral(driver, "Enter your folder name").clear();
			o365Home.dialogboxinputgeneral(driver, "Enter your folder name").click();
			o365Home.dialogboxinputgeneral(driver, "Enter your folder name").type(folderName);
			hardWait(5);
			o365Home.dialogboxbuttongeneral(driver, "Create").mouseOver(driver);
			action.sendKeys(Keys.ENTER).perform();
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("Successful Created Folder " + folderName);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void createOneNote(WebDriver driver, String appName, String title) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Creating One Note title " + title);
		try {
			o365Home.create(driver).waitForLoading(driver);
			o365Home.create(driver).click();
			hardWait(2);
			o365Home.submenu(driver, appName).click();
			hardWait(5);
			o365Home.dialogOnedriveInputFilename(driver).type(title);
			hardWait(5);
			o365Home.dialogOnedriveCreate(driver).mouseOverClick(driver);
			hardWait(ACTIVITY_AND_DELAY);
			driver.navigate().back();
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("Created One Note title" + title);
		} catch(Exception e) {
			exception = e;
			Logger.info("Error " + e.getMessage());
		}
	}
	
	public void createItem(WebDriver driver, String appName, String title) {
		O365HomePage o365Home =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		hardWait(ACTIVITY_AND_DELAY);
		try {
			o365Home.create(driver).waitForLoading(driver);
			o365Home.create(driver).click();
			hardWait(2);
			o365Home.submenu(driver, appName).click();
			hardWait(ACTIVITY_AND_DELAY*3);
			/* Open App File Window */
			
//			Logger.info("wait for loading");
//			driver.navigate().back();
//			hardWait(ACTIVITY_AND_DELAY);
//			o365Home.create(driver).waitForLoading(driver);
//			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("Created " + appName  + " file " + title);
		} catch(Exception e) {
			exception = e;
			Logger.info("Error " + e.getMessage());
		}
	}

	public void selectIndexItemList(WebDriver driver, String index) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {	
			lp.onedriveItemListCheckbox(driver, index).click();
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public boolean selectFileName(WebDriver driver, String fileName) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Searching a file "+fileName+" before selecting...");	
		
		try {	
			boolean status=lp.onedriveFileName(driver, fileName).isElementPresent(driver);
			if (status){
				Logger.info("File found selecting it");
				lp.onedriveFileName(driver, fileName).click();
				Logger.info("File selected for download");
				return status;
			}
			} catch(Exception e) {
				exception = e;
			}
		Logger.info("File not found or already deleted");
		return false;
	}
	
	public void downloadItemFile(WebDriver driver, String index) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			try {	
				Logger.info("Selecting a file to download");
				selectIndexItemList(driver, index);
				hardWait(3);
				Logger.info("File download in progress...");
				lp.onedriveDownloadLink(driver).click();
				Logger.info("File Downloaded successfull");
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				exception = e;
		}
	}
	
	public void downloadItemFileByName(WebDriver driver, String name) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			try {	
				Logger.info("Selecting a file to download");
				//hardWait(17);
				Boolean status=selectFileName(driver, name);
				Logger.info("Selected " + status);
				hardWait(4);
				if (status){
				Logger.info("File " + name + " download in progress...");
				//lp.topmenu(driver, "Download").waitForLoading(driver);
				lp.topmenu(driver, "Download").click();
				//lp.onedriveDownloadLink(driver).click();
				Logger.info("File Downloaded successful");
				hardWait(8);
				}
				else{
					Logger.info("File not found and hence cant be downloaded");
				}
			} catch(Exception e) {
				exception = e;
		}
	}
	
	public void downloadItemFileByNameIE(WebDriver driver, String name) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			try {	
				Logger.info("Selecting a file to download");
				hardWait(12);
				Boolean status=selectFileName(driver, name);
				Logger.info("Selected " + status);
				hardWait(5);
				if (status){
				Logger.info("File " + name + " download in progress...");
				//lp.topmenu(driver, "Download").waitForLoading(driver);
				lp.topmenu(driver, "Download").click();
				//lp.onedriveDownloadLink(driver).click();
				hardWait(8);
				Logger.info("going to click save button");
				hardWait(8);
				}
				else{
					Logger.info("File not found and hence cant be downloaded");
				}
			} catch(Exception e) {
				exception = e;
		}
	}
	
	public boolean selectSPFilebyName(WebDriver driver, String name) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		boolean status=false;
			try {	
				Logger.info("Select file :"+name);
				name=name.split("\\.")[0];
				hardWait(ACTIVITY_AND_DELAY);
				 status=lp.spSelectFile(driver, name).isElementVisible();
				System.out.println("file present status "+status);
				if(status){
					  lp.spSelectFile(driver, name).click();
					  hardWait(ACTIVITY_AND_DELAY);
						lp.spFileActionMenu(driver, name).click();
						hardWait(ACTIVITY_AND_DELAY);
						//return status;
				}
				else
				{
					//return status;
				}
				
			} catch(Exception e) {
				Logger.info(name+" file not found");
				exception = e;
		}
			return status;
	}
	
	public void downloadSPFile(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			try {	
				Logger.info("Selecting a file to download");
				hardWait(ACTIVITY_AND_DELAY);
				
				lp.spDownload(driver).click();
		
				
			} catch(Exception e) {
			exception = e;
		}
	}
	
	public void deleteSPFile(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			try {	
				Logger.info("delete a file");
				hardWait(ACTIVITY_AND_DELAY);
				lp.spDelete(driver).click();
				Alert alert = driver.switchTo().alert();
			   alert.accept();
			   Logger.info("accepted delete action");
		
				
			} catch(Exception e) {
			exception = e;
		}
	}
	
	public void shareSPFile(WebDriver driver,String user) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			try {	
				Logger.info("Share a file");
				hardWait(ACTIVITY_AND_DELAY);
				lp.spShare(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.toShare(driver).type(user);
				lp.shareButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY*3);
			   Logger.info("File share to the user :"+user);
		
				
			} catch(Exception e) {
				Logger.info("File Not shared  :");
				exception = e;
		}
	}
	public void shareSPFolder(WebDriver driver,String folder,String user) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {	
			boolean	 status=lp.spSelectFolder(driver, folder).isElementVisible();
			System.out.println("file present status "+status);
			if(status){
				lp.spSelectFolder(driver, folder).click();
				Logger.info("File found for selection");
				hardWait(ACTIVITY_AND_DELAY);
			}
			else
			{
				Logger.info("File not found for selection");
			}
			lp.spFolderShare(driver).click();
			hardWait(ACTIVITY_AND_DELAY);
			lp.toShare(driver).type(user);
			lp.shareButton(driver).click();
			hardWait(ACTIVITY_AND_DELAY*3);
			Logger.info("File shared to the user :"+user);
		} catch(Exception e) {
			Logger.info("File Not shared  :");
			exception = e;
		}
	}

	public void renameDocFile(WebDriver driver,String rename) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {	
			boolean	 status=lp.spFile(driver).isElementPresent(driver);
			System.out.println("file present status "+status);
			if(status){
				lp.spFile(driver).click();
				lp.saveAs(driver).click();
				lp.reNameOption(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.reNameText(driver).type(rename);
				lp.reNameOkButton(driver).click();
			}
			else
			{
				Logger.info("File not found for selection");
			}
			lp.spFolderShare(driver).click();
			hardWait(ACTIVITY_AND_DELAY);
			
		} catch(Exception e) {
			Logger.info("File Not shared  :");
			exception = e;
		}
	}

	
	
	
	public void downloadItemFileForBlock(WebDriver driver, String name) throws InterruptedException {
		driver.manage().timeouts().pageLoadTimeout(2, TimeUnit.MINUTES);
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			try {	
				Logger.info("Selecting a file to download");
				hardWait(12);
				Boolean status=selectFileName(driver, name);
				hardWait(5);
				if (status){
				Logger.info("File " + name + " download in progress...");
				lp.topmenu(driver, "Download").waitForLoading(driver);
				lp.topmenu(driver, "Download").click();
				//lp.onedriveDownloadLink(driver).click();
				Logger.info("File Downloaded in progress");
				hardWait(8);
				}
				else{
					Logger.info("File not found and hence cant be downloaded");
				}
			} catch(Exception e) {
				exception = e;
				Logger.info(" File not  Downloaded ..");
		}
	}
	
	
	public void shareItemFileByName(WebDriver driver, String name) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			try {	
				Logger.info("Selecting a file to share");
				selectFileName(driver, name);
				hardWait(3);
				Logger.info("File Share in progress...");
				lp.topmenu(driver, "Share").click();
				hardWait(3);
				lp.selectAccessTypeDropown(driver).click();
				hardWait(3);
				lp.selectAccessInDropdown(driver).click();
				hardWait(3);
				lp.shareDialogCloseButton(driver).click();
				Logger.info("File Share successful");
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				exception = e;
			}
		}
	
	public void shareItemViaEmail(WebDriver driver, String name, String userId) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		userId = userId.replace("o365beatle", "O365beatle");
		try {	
			Logger.info("Selecting a file to share to " + userId);
			selectFileName(driver, name);
			hardWait(3);
			Logger.info("File Share in progress...");
			lp.topmenu(driver, "Share").waitForLoading(driver);
			lp.topmenu(driver, "Share").click();
			hardWait(ACTIVITY_AND_DELAY);
			if (lp.dialogsharedsidemenu(driver, "Invite people").isDisplayed()) {
				lp.dialogsharedsidemenu(driver, "Invite people").waitForLoading(driver);
			}
			//lp.dialoguserid(driver).clear();
			lp.dialoguserid(driver).type(userId);
			hardWait(3);
			lp.dialogUserDropdown(driver, userId).click();
			hardWait(3);
			lp.dialogsharebutton(driver).click();
			Logger.info("File Share successful");
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("Share Error " + e.getMessage());
			exception = e;
		}
	}
	
	public void shareItemViaEmailPersonal(WebDriver driver, String name, String userId) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		userId = userId.replace("o365beatle", "O365beatle");
		try {	
			Logger.info("Selecting a file to share to " + userId);
			selectFileName(driver, name);
			hardWait(3);
			Logger.info("File Share in progress...");
			lp.topmenu(driver, "Share").waitForLoading(driver);
			lp.topmenu(driver, "Share").click();
			hardWait(ACTIVITY_AND_DELAY);
			if (lp.personalEmail(driver).isDisplayed()) {
				lp.personalEmail(driver).click();
				hardWait(5);
				
				lp.personalEmailInput(driver).type(userId);
				lp.personalInviteMessage(driver).click();
				hardWait(10);
				if (lp.personalEmailShareButton(driver).isDisplayed()) {
					lp.personalEmailShareButton(driver).click();
				}
			}
			Logger.info("File Share successful");
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("Share Error" + e.getMessage());
			exception = e;
		}
	}
	
	public void shareItemByLink(WebDriver driver, String name) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {	
			Logger.info("Selecting a file " + name);
			selectFileName(driver, name);
			hardWait(3);
			Logger.info("File Share in progress...");
			lp.topmenu(driver, "Share").waitForLoading(driver);
			lp.topmenu(driver, "Share").click();
			hardWait(ACTIVITY_AND_DELAY);
			if (lp.dialogsharedsidemenu(driver, "Get a link").isDisplayed()) {
				lp.dialogsharedsidemenu(driver, "Get a link").click();
				/* Make it Remove link */
				lp.shareLinkDropdown(driver).click();
				hardWait(3);
				lp.selectlist(driver, "Restricted link - Only specific people can open this link").click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.shareLinkDropdown(driver).click();
				hardWait(3);
				lp.selectlist(driver, "View link - no sign-in required").click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.shareLinkDropdown(driver).click();
				hardWait(3);
				lp.selectlist(driver, "Restricted link - Only specific people can open this link").click();
				hardWait(ACTIVITY_AND_DELAY);
				
	/*			if (lp.shareremovelink(driver).isDisplayed()) {
					lp.shareremovelink(driver).click();
					lp.sharebuttonunshare(driver).click();
					hardWait(10);
				}
				 Make it View link 
				lp.shareLinkDropdown(driver).click();
				hardWait(10);
				if (lp.sharerestrictedlink(driver).isDisplayed()) {
					lp.sharerestrictedlink(driver).click();
					lp.sharewithviewlink(driver).click();
				}*/
			}
			hardWait(3);
			lp.sharebuttonCloseShareDialog(driver).click();
			Logger.info("File Share successful");
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("Share Error " + e.getMessage());
			exception = e;
		}
	}
	
	public void shareItemClick(WebDriver driver, String name) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {	
			Logger.info("Selecting a file " + name);
			selectFileName(driver, name);
			hardWait(3);
			Logger.info("File Share in progress...");
			lp.topmenu(driver, "Share").waitForLoading(driver);
			lp.topmenu(driver, "Share").click();
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("Share Item");
		} catch(Exception e) {
			Logger.info("Share Error " + e.getMessage());
			exception = e;
		}
	}
	
	public void shareItemByLinkPerson(WebDriver driver, String name) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {	
			Logger.info("Selecting a file " + name);
			selectFileName(driver, name);
			hardWait(3);
			Logger.info("File Share in progress...");
			Logger.info("Remove if already shared link");
			if (!lp.personalInfoPanel(driver).isDisplayed()) {
				lp.personalDetailsButton(driver).click();
				hardWait(5);
			}
			if (lp.personalRemoveLink(driver).isDisplayed()) {
				lp.personalRemoveLink(driver).click();
				if (lp.personalRemoveLinkPopup(driver).isDisplayed()) {
					lp.personalRemoveLinkPopup(driver).click();
				}
				hardWait(ACTIVITY_AND_DELAY);
			}
			Logger.info("Remove Done");
			lp.topmenu(driver, "Share").waitForLoading(driver);
			lp.topmenu(driver, "Share").click();
			hardWait(ACTIVITY_AND_DELAY);
			if (lp.personalGetlink(driver).isDisplayed()) {
				lp.personalGetlink(driver).click();
				hardWait(10);
				if (lp.closedeletedialog(driver).isDisplayed()) {
					lp.closedeletedialog(driver).click();
				}
				if (!lp.personalInfoPanel(driver).isDisplayed()) {
					lp.personalDetailsButton(driver).click();
					hardWait(5);
				}
				if (lp.personalRemoveLink(driver).isDisplayed()) {
					lp.personalRemoveLink(driver).click();
					if (lp.personalRemoveLinkPopup(driver).isDisplayed()) {
						lp.personalRemoveLinkPopup(driver).click();
					}
					
					hardWait(ACTIVITY_AND_DELAY);
				}
			}
		} catch(Exception e) {
			Logger.info("Share Error " + e.getMessage());
			exception = e;
		}
	}
	
	
	public void uploadItemFile(WebDriver driver, String filePath) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Starting File Upload");
			try {		
				//lp.onedriveUploadTextbox(driver).waitForLoading(driver);
				// Get filename from filePath
				String fileName = filePath.substring(filePath.lastIndexOf(File.separator) +1, filePath.length());
				/*if (selectFileName(driver, fileName)) {
					lp.onedrivedeletelink(driver).waitForLoading(driver);
					lp.onedrivedeletelink(driver).click();
					lp.onedrivedeletebutton(driver).waitForLoading(driver);
					lp.onedrivedeletebutton(driver).click();
					hardWait(10);
				}
				*/	
				//lp.onedriveUploadTextbox(driver).waitForLoading(driver);
				lp.onedriveUploadTextbox(driver).type(filePath);;
				Logger.info("File Path " + filePath);
				Logger.info("File upload in progress "+fileName);
				// Need this delay because it takes long time to upload
				for (int i =0; i < CommonConstants.TRY_COUNT; i++ ) {
				Logger.info("Waiting every 10 seconds for "+(i+1)+" time if file upload finished");
					if (selectFileName(driver, fileName)) {
						break;
					} else  {
						hardWait(10);
					}
				}
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				exception = e;
				Logger.info("File upload incomplete");
			}
			Logger.info("File upload completed");
			
		hardWait(10);
	}
	
	public void uploadItemFile1(WebDriver driver, String filePath) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Starting File Upload");
			try {		
				//lp.onedriveUploadTextbox(driver).waitForLoading(driver);
				// Get filename from filePath
				String fileName = filePath;//.substring(filePath.lastIndexOf(File.separator) +1, filePath.length());				
				lp.spUpload(driver).click();
				
				Logger.info("File Path " + filePath);
				lp.uploadButton(driver).type(filePath);;
				Logger.info("File upload in progress "+fileName);
				// Need this delay because it takes long time to upload
				for (int i =0; i < CommonConstants.TRY_COUNT; i++ ) {
				Logger.info("Waiting every 10 seconds for "+(i+1)+" time if file upload finished");
					if (selectFileName(driver, fileName)) {
						break;
					} else  {
						hardWait(10);
					}
				}
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				exception = e;
				Logger.info("File upload incomplete");
			}
			Logger.info("File upload completed");
			
		hardWait(10);
	}

	public void uploadItemFileClick(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Starting File Upload");
			try {		
				lp.topmenu(driver, "Upload").click();
				hardWait(5);
				lp.fileUploadSubmenu(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				exception = e;
				Logger.info("File upload click not appears");
			}
			Logger.info("File upload click completed");
			
		hardWait(10);
	}
	
	public void uploadItemFolderClick(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Starting File Upload");
			try {		
				lp.topmenu(driver, "Upload").click();
				hardWait(5);
				lp.folderUploadsubmenu(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				exception = e;
				Logger.info("Folder upload click not appears");
			}
			Logger.info("Folder upload click completed");
			
		hardWait(10);
	}
	
	public void uploadItemFileWithBlock(WebDriver driver, String filePath) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Starting File Upload");
			try {		
				String fileName = filePath.substring(filePath.lastIndexOf(File.separator) +1, filePath.length());
				lp.onedriveUploadTextbox(driver).type(filePath);;
				Logger.info("File Path " + filePath);
				Logger.info("File upload in progress "+fileName);
//				for (int i =0; i < CommonConstants.TRY_COUNT; i++ ) {
//				Logger.info("Waiting every 10 seconds for "+(i+1)+" time if file upload finished");
//					if (selectFileName(driver, fileName)) {
//						break;
//					} else  {
//						hardWait(10);
//					}
//				}
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				exception = e;
				Logger.info("File upload incomplete");
			}
			Logger.info("File upload inprogress after waiting : "+ACTIVITY_AND_DELAY);
			hardWait(10);
	}
	
	
	public void deleteFile(WebDriver driver, String name) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {		
				Logger.info("First searching a file to delete");
				Logger.info("Delete file " + name + " inprogress..");
				if (selectFileName(driver, name)){
					Logger.info("File found...and selected to delete");
					//selectFileName(driver, "readme1.pdf");
					//lp.onedrivedeletelink(driver).waitForLoading(driver);
					//lp.onedrivedeletelink(driver).waitForLoading(driver);
					//lp.onedrivedeletelink(driver).click();
					lp.topmenu(driver, "Delete").waitForLoading(driver);
					lp.topmenu(driver, "Delete").click();
					//lp.onedrivedeletebutton(driver).waitForLoading(driver);
					Logger.info("File deletion in progess..");
					lp.onedrivedeletebutton(driver).click();
					hardWait(ACTIVITY_AND_DELAY);
					Logger.info("File deleted successfully...");
				}
				else {
					Logger.info("No file exist to delete...");
				}
			} catch(Exception e) {
				exception = e;
			}
	}
	
	public boolean verifyFolderSelected(WebDriver driver, String folder) throws InterruptedException { 
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Verify Folder Is Selected");
		try {		
			if (lp.topLabelFolder(driver, folder).isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			Logger.info("Error " + e);
			exception = e;
			return false;
		}
	}
	
	public void deleteAllFiles(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Delete All Files");
		try {		
			if (lp.isFileFound(driver).isDisplayed()){
				lp.selectAllFilesIfInactive(driver).mouseOver(driver);
				lp.selectAllFilesIfInactive(driver).click();
				hardWait(5);
				lp.multipleFileDeleteButton(driver).click();
				hardWait(5);
				lp.dialogDeletebutton(driver).click();
				Logger.info("Successful Delete All Files");
			} else {
				Logger.info("No files exist to delete...");
			}
		} catch(Exception e) {
			Logger.info("Error " + e);
			exception = e;
		}
	}
	
	public void restore(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {	
			lp.sidemenu(driver, "Recycle bin").click();
			hardWait(5);
			//switchFrame(driver.findElement(By.xpath("//iframe[@class='ms-IframeLoader-iframe ms-IframeLoader-iframe--visible']")), driver);
			if(lp.recycleDialogAscendingorder(driver).isElementVisible()) {
				lp.recycleDialogAscendingorder(driver).click();
			}
			if (lp.recycleDialogSelectFirstItem(driver).isDisplayed()) {
				lp.recycleDialogSelectFirstItem(driver).click();
				lp.recycleDialogRestoreButton(driver).click();
				Alert simpleAlert = driver.switchTo().alert();
				String alertText = simpleAlert.getText();
				Logger.info("Alert text is " + alertText);
				simpleAlert.accept();
				hardWait(ACTIVITY_AND_DELAY);
				//driver.switchTo().defaultContent();
				//lp.closedeletedialog(driver).click();
				Logger.info("Successful Restore");
			} else {
				//lp.closedeletedialog(driver).click();
			}
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			if (lp.closedeletedialog(driver).isDisplayed()) {
				lp.closedeletedialog(driver).click();
			}
			exception = e;
		}
	}
	
	public void deleteLastFileFromRecyle(WebDriver driver) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {	
			lp.sidemenu(driver, "Recycle bin").click();
			hardWait(5);
			//switchFrame(driver.findElement(By.xpath("//iframe[@class='ms-IframeLoader-iframe ms-IframeLoader-iframe--visible']")), driver);
			if(lp.recycleDialogAscendingorder(driver).isElementVisible()) {
				lp.recycleDialogAscendingorder(driver).click();
			}
			if (lp.recycleDialogSelectFirstItem(driver).isDisplayed()) {
				lp.recycleDialogSelectFirstItem(driver).click();
				lp.recycleDialogDeleteButton(driver).click();
				Alert simpleAlert = driver.switchTo().alert();
				String alertText = simpleAlert.getText();
				Logger.info("Alert text is " + alertText);
				simpleAlert.accept();
				hardWait(ACTIVITY_AND_DELAY);
				driver.switchTo().defaultContent();
				//lp.closedeletedialog(driver).click();
			} else {
				//lp.closedeletedialog(driver).click();
			}
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("Successful Delete Last File From Recyle");
		} catch(Exception e) {
			if (lp.closedeletedialog(driver).isDisplayed()) {
				lp.closedeletedialog(driver).click();
			}
			exception = e;
		}
	}
	
	public void deleteAllFilesFromRecycle(WebDriver driver) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {	
			lp.sidemenu(driver, "Recycle bin").click();
			hardWait(5);
			//switchFrame(driver.findElement(By.xpath("//iframe[@class='ms-IframeLoader-iframe ms-IframeLoader-iframe--visible']")), driver);
			if (lp.recycleselectallcheckbox(driver).isDisplayed()) {
				lp.recycleselectallcheckbox(driver).click();
				lp.emptyRecyleBinButton(driver).click();
				Alert simpleAlert = driver.switchTo().alert();
				String alertText = simpleAlert.getText();
				Logger.info("Alert text is " + alertText);
				simpleAlert.accept();
				hardWait(ACTIVITY_AND_DELAY);
				//driver.switchTo().defaultContent();
				//lp.closedeletedialog(driver).click();
				Logger.info("Delete All Files From Recyle");
			} else {
				//lp.closedeletedialog(driver).click();
			}
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			/*if (lp.closedeletedialog(driver).isDisplayed()) {
				lp.closedeletedialog(driver).click();
			}
			*/
			exception = e;
		}
		
	}
	
	
	public void sendEmail(WebDriver driver) throws InterruptedException {
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.newemailbutton(driver).waitForLoading(driver, 20000);
			lp.newemailbutton(driver).click();
			Logger.info("Preparing sending mail to users");
			hardWait(10);
			if (lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").isDisplayed()) {
				lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").click();
				lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.")
					.type("admin@gatewayO365beatle.com");
			}
			hardWait(3);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(3);
			//lp.personalEmailSelectByTitle(driver, "Cc recipients. Enter an email address or a name from your contact list.").click();
			//lp.personalEmailSelectByTitle(driver, "Cc recipients. Enter an email address or a name from your contact list.").type("testuser1@gatewayO365beatle.com");
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			lp.subject(driver).waitForLoading(driver);
			lp.subject(driver).click();
			lp.subject(driver).type("This is subject");
			hardWait(5);
			
			if (lp.personalEmailBody(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.personalEmailBody(driver).click();
				lp.personalEmailBody(driver).type("body");
			} else if (lp.body(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.body(driver).click();
				lp.body(driver).type("body");
			} else {
				Logger.info("Trying with xpath locator");
				lp.body1(driver).click();
				lp.body1(driver).type("body");
			}
			Logger.info("Sending email to admin@gatewayO365beatle.com with subject 'This is subject' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void sendEmail(WebDriver driver, String subject) throws InterruptedException {
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.newemailbutton(driver).waitForLoading(driver, 20000);
			lp.newemailbutton(driver).click();
			Logger.info("Preparing sending mail to users");
			hardWait(10);
			if (lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").isDisplayed()) {
				lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").click();
				lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.")
					.type("admin@gatewayO365beatle.com");
			}
			hardWait(3);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(3);
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			lp.subject(driver).waitForLoading(driver);
			lp.subject(driver).click();
			lp.subject(driver).type(subject);
			hardWait(5);
			
			if (lp.personalEmailBody(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.personalEmailBody(driver).click();
				lp.personalEmailBody(driver).type(subject);
			} else if (lp.body(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.body(driver).click();
				lp.body(driver).type(subject);
			} else {
				Logger.info("Trying with xpath locator");
				lp.body1(driver).click();
				lp.body1(driver).type(subject);
			}
			Logger.info("Sending email to admin@gatewayO365beatle.com with subject 'This is subject' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	
	public void sendEmailWithToEmail(WebDriver driver, String toEmail, String subject) throws InterruptedException {
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.newemailbutton(driver).waitForLoading(driver, 20000);
			lp.newemailbutton(driver).click();
			Logger.info("Preparing sending mail to users");
			hardWait(10);
			if (lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").isDisplayed()) {
				lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").click();
				lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.")
					.type(toEmail);
			}
			hardWait(3);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(3);
			//lp.personalEmailSelectByTitle(driver, "Cc recipients. Enter an email address or a name from your contact list.").click();
			//lp.personalEmailSelectByTitle(driver, "Cc recipients. Enter an email address or a name from your contact list.").type("testuser1@gatewayO365beatle.com");
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			lp.subject(driver).waitForLoading(driver);
			lp.subject(driver).click();
			lp.subject(driver).type(subject);
			hardWait(5);
			
			if (lp.personalEmailBody(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.personalEmailBody(driver).click();
				lp.personalEmailBody(driver).type("body");
			} else if (lp.body(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.body(driver).click();
				lp.body(driver).type("body");
			} else {
				Logger.info("Trying with xpath locator");
				lp.body1(driver).click();
				lp.body1(driver).type("body");
			}
			Logger.info("Sending email to " + toEmail + " with subject 'This is subject' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void sendEmailPersonal(WebDriver driver, String toEmail, String subject) throws InterruptedException {
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			if (lp.personalEmailNewButton(driver).isDisplayed()) {
				lp.personalEmailNewButton(driver).click();
			}
			Logger.info("Preparing sending mail to users");
			hardWait(5);
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").waitForLoading(driver);
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").click();
			
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.")
				.type(toEmail);
			hardWait(3);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(3);
			lp.personalEmailSelectByTitle(driver, "Subject,").waitForLoading(driver);
			lp.personalEmailSelectByTitle(driver, "Subject,").click();
			lp.personalEmailSelectByTitle(driver, "Subject,").type(subject);
			hardWait(5);
			if (lp.personalEmailBody(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.personalEmailBody(driver).click();
				lp.personalEmailBody(driver).type("body");
			}
			Logger.info("Sending email to admin@gatewayO365beatle.com with subject 'This is subject' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent ");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	
	public void createEmailClickAttach(WebDriver driver, String attacheFile) throws InterruptedException {
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			if (lp.personalEmailNewButton(driver).isDisplayed()) {
				lp.personalEmailNewButton(driver).click();
			}
			Logger.info("Preparing sending mail to users");
			hardWait(5);
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").waitForLoading(driver);
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").click();
			
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.")
				.type("admin@gatewayO365beatle.com");
			hardWait(3);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(3);
			lp.personalEmailSelectByTitle(driver, "Subject,").waitForLoading(driver);
			lp.personalEmailSelectByTitle(driver, "Subject,").click();
			lp.personalEmailSelectByTitle(driver, "Subject,").type("This is subject");
			hardWait(5);
			if (lp.personalEmailBody(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.personalEmailBody(driver).click();
				lp.personalEmailBody(driver).type("body");
			}
			lp.personalEmailMenu(driver, "Insert").click();
			hardWait(3);
			lp.personalEmailMenu(driver, "Select files from Computer").click();
			Logger.info("Sending email to admin@gatewayO365beatle.com with subject 'This is subject' and body 'body'");
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void sendEmailButtonPersonal(WebDriver driver) throws InterruptedException {
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.personalAttachasCopy(driver).click();
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("Sending email to admin@gatewayO365beatle.com with subject 'This is subject' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent Button");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	
	public void sendEmail1(WebDriver driver) throws InterruptedException {
			try {		
				O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
				lp.newemailbutton(driver).click();
				Logger.info("Preparing sending mail to users");
				hardWait(5);
				lp.toList(driver).type("admin@gatewayO365beatle.com");
				hardWait(3);
				Actions action = new Actions(driver);
				action.sendKeys(Keys.ENTER).build().perform();
				hardWait(3);
				lp.ccList(driver).click();
				lp.ccList(driver).type("testuser1@gatewayO365beatle.com");
				hardWait(3);
				action.sendKeys(Keys.ENTER).build().perform();
				hardWait(5);
				lp.subject(driver).waitForLoading(driver);
				lp.subject(driver).click();
				lp.subject(driver).type("This is subject");
				hardWait(5);
				lp.body(driver).click();
				lp.body(driver).type("body");
				Logger.info("Sending email to admin@gatewayO365beatle.com with subhect 'This is subject' and body 'body'");
				lp.sendButton(driver).click();
				Logger.info("Email sent");
				hardWait(10);
			} catch(Exception e) {
				exception = e;
			}
	}
	
	public void discardEmail(WebDriver driver) throws InterruptedException {
		Actions action = new Actions(driver);
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.newemailbutton(driver).click();
			lp.o365emailtolist(driver).type("admin@gatewayO365beatle.com");
			action.sendKeys(Keys.ENTER).build().perform();
			lp.o365emailcclist(driver).click();
			lp.o365emailcclist(driver).type("testuser1@gatewayO365beatle.com");
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			lp.subject(driver).waitForLoading(driver);
			lp.subject(driver).click();
			lp.subject(driver).type("This is subject to discard");
			hardWait(5);
			
			Logger.info("Saving email in draft");
			hardWait(10);
			lp.o365emailMoreOptions(driver).click();
			hardWait(4);
			lp.o365emailsavedarftLink(driver).click();
			hardWait(10);
			Logger.info("Discare Email");
			if(lp.o365EmailTopMenuButton(driver, "Discard").isDisplayed()) {
				lp.o365EmailTopMenuButton(driver, "Discard").click();
				hardWait(5);
				lp.o365emailDiscardMessage(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).build().perform();
			}
			Logger.info("Email Discarded");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void discardEmailPersonal(WebDriver driver) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		try {		
			if (lp.personalEmailNewButton(driver).isDisplayed()) {
				lp.personalEmailNewButton(driver).click();
			}
			Logger.info("Preparing sending mail to users");
			hardWait(5);
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").waitForLoading(driver);
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").click();
			
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.")
				.type("admin@gatewayO365beatle.com");
			hardWait(3);
			
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(3);
			lp.personalEmailSelectByTitle(driver, "Subject,").waitForLoading(driver);
			lp.personalEmailSelectByTitle(driver, "Subject,").click();
			lp.personalEmailSelectByTitle(driver, "Subject,").type("This is subject");
			hardWait(5);
			Logger.info("Saving email in draft");
			hardWait(10);
			lp.emailMoreOptions(driver).click();
			hardWait(4);
			lp.saveDarftLink(driver).click();
			hardWait(10);
			Logger.info("Discare Email");
			if(lp.o365EmailTopMenuButton(driver, "Discard").isDisplayed()) {
				lp.o365EmailTopMenuButton(driver, "Discard").click();
				hardWait(5);
				lp.o365emailDiscardMessage(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).build().perform();
			}
			Logger.info("Email Discarded");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
		
	}
	
	public void sendEmailWithAttach(WebDriver driver, String fileNameFromOneDrive) throws InterruptedException {
		Actions action = new Actions(driver);
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.newemailbutton(driver).click();
			Logger.info("Preparing sending mail to users");
			hardWait(5);
			if(lp.o365EmailTopMenuButton(driver, "Attach").isDisplayed()) {
				lp.o365EmailTopMenuButton(driver, "Attach").click();
				hardWait(5);
				lp.o365Emailattachfile(driver, fileNameFromOneDrive).click();
				hardWait(10);
				lp.o365EmailAttachNextButton(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).build().perform();
				hardWait(5);
				
				lp.o365EmailAttachFileByonedrive(driver).click();
				hardWait(10);
			}
			lp.toList(driver).type("admin@gatewayO365beatle.com");
			action.sendKeys(Keys.ENTER).build().perform();
			lp.ccList(driver).click();
			lp.ccList(driver).type("testuser1@gatewayO365beatle.com");
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			lp.subject(driver).waitForLoading(driver);
			lp.subject(driver).click();
			lp.subject(driver).type("This is subject with attached");
			hardWait(5);
			lp.body1(driver).click();
			lp.body1(driver).type("body");
			Logger.info("Sending email to admin@gatewayO365beatle.com with subhect 'This is subject with attached' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void sendEmailWithAttachGeneric(WebDriver driver, String fileNameFromOneDrive,String subject,String body) throws InterruptedException {
		Actions action = new Actions(driver);
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.newemailbutton(driver).click();
			Logger.info("Preparing sending mail to users");
			hardWait(5);
			if(lp.o365EmailTopMenuButton(driver, "Attach").isDisplayed()) {
				lp.o365EmailTopMenuButton(driver, "Attach").click();
				hardWait(5);
				lp.o365Emailattachfile(driver, fileNameFromOneDrive).click();
				hardWait(10);
				lp.o365EmailAttachNextButton(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).build().perform();
				hardWait(5);
				
				lp.o365EmailAttachFileByonedrive(driver).click();
				hardWait(10);
			}
			lp.toList(driver).type("admin@gatewayO365beatle.com");
			action.sendKeys(Keys.ENTER).build().perform();
			lp.ccList(driver).click();
			lp.ccList(driver).type("testuser1@gatewayO365beatle.com");
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			lp.subject(driver).waitForLoading(driver);
			lp.subject(driver).click();
			lp.subject(driver).type(subject);
			hardWait(5);
			lp.body1(driver).click();
			lp.body1(driver).type(body);
			Logger.info("Sending email to admin@gatewayO365beatle.com with subhect 'This is subject with attached' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	public void sendEmailWithAttachAsCopy(WebDriver driver, String fileNameFromOneDrive) throws InterruptedException {
		Actions action = new Actions(driver);
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.newemailbutton(driver).click();
			Logger.info("Preparing sending mail to users");
			hardWait(5);
			if(lp.o365EmailTopMenuButton(driver, "Attach").isDisplayed()) {
				lp.o365EmailTopMenuButton(driver, "Attach").click();
				hardWait(5);
				lp.o365Emailattachfile(driver, fileNameFromOneDrive).click();
				hardWait(10);
				lp.o365EmailAttachNextButton(driver).mouseOver(driver);
				action.sendKeys(Keys.ENTER).build().perform();
				hardWait(5);
				lp.o365EmailAttachFileByCopy(driver).click();
				//lp.o365EmailAttachFileByonedrive(driver).click();
				hardWait(10);
			}
			lp.toList(driver).type("admin@gatewayO365beatle.com");
			action.sendKeys(Keys.ENTER).build().perform();
			lp.ccList(driver).click();
			lp.ccList(driver).type("testuser1@gatewayO365beatle.com");
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			lp.subject(driver).waitForLoading(driver);
			lp.subject(driver).click();
			lp.subject(driver).type("This is subject with attached"+fileNameFromOneDrive);
			hardWait(5);
			lp.body1(driver).click();
			lp.body1(driver).type("body");
			Logger.info("Sending email to admin@gatewayO365beatle.com with subhect 'This is subject with attached' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	public void sendEmailWithAttach1(WebDriver driver, String fileNameFromOneDrive) throws InterruptedException {
		Actions action = new Actions(driver);
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.newemailbutton(driver).click();
			Logger.info("Preparing sending mail to users");
			hardWait(5);
			if(lp.o365EmailTopMenuButton(driver, "Attach").isDisplayed()) {
				lp.o365EmailTopMenuButton(driver, "Attach").click();
				hardWait(5);
				lp.o365emailattachfilefromcomputer(driver).type(fileNameFromOneDrive);
				
				lp.onedriveUploadTextbox(driver).type(fileNameFromOneDrive);;
				Logger.info("File Path " + fileNameFromOneDrive);
				Logger.info("File upload in progress "+fileNameFromOneDrive);
				// Need this delay because it takes long time to upload
				for (int i =0; i < CommonConstants.TRY_COUNT; i++ ) {
				Logger.info("Waiting every 10 seconds for "+(i+1)+" time if file upload finished");
					if (selectFileName(driver, fileNameFromOneDrive)) {
						break;
					} else  {
						hardWait(10);
					}
				}
				hardWait(ACTIVITY_AND_DELAY);

				
				
				hardWait(10);
				//lp.o365EmailAttachNextButton(driver).mouseOver(driver);
				//action.sendKeys(Keys.ENTER).build().perform();
				//hardWait(5);
				lp.o365EmailAttachFileByCopy(driver).click();
				hardWait(5);
			}
			lp.toList(driver).type("admin@gatewayO365beatle.com");
			action.sendKeys(Keys.ENTER).build().perform();
			lp.ccList(driver).click();
			lp.ccList(driver).type("testuser1@gatewayO365beatle.com");
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			lp.subject(driver).waitForLoading(driver);
			lp.subject(driver).click();
			lp.subject(driver).type("This is subject with attached");
			hardWait(5);
			lp.body1(driver).click();
			lp.body1(driver).type("body");
			Logger.info("Sending email to admin@gatewayO365beatle.com with subhect 'This is subject with attached' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void sendEmailByUser(String user, String subject, WebDriver driver) throws InterruptedException {
		try {		
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			lp.newemailbutton(driver).click();
			Logger.info("Preparing sending mail to users");
			hardWait(10);
			if (lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").isDisplayed()) {
				lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").click();
				lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.")
					.type(user);
			}
			hardWait(3);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(5);
			lp.subject(driver).waitForLoading(driver);
			lp.subject(driver).click();
			lp.subject(driver).type(subject);
			hardWait(5);
			
			if (lp.personalEmailBody(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.personalEmailBody(driver).click();
				lp.personalEmailBody(driver).type("body");
			} else if (lp.body(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.body(driver).click();
				lp.body(driver).type("body");
			} else {
				Logger.info("Trying with xpath locator");
				lp.body1(driver).click();
				lp.body1(driver).type("body");
			}
			Logger.info("Sending email to " + user + " with subject 'This is subject' and body 'body'");
			lp.sendButton(driver).click();
			Logger.info("Email sent");
			hardWait(10);
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void searchBySubjectAndDownload(WebDriver driver, String subject, String fileName) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		Logger.info("Search By Subject " + subject + " and download");
		try {		
			lp.emailSearchBox(driver).click();
			hardWait(3);
			lp.emailSearchInputbox(driver).click();
			lp.emailSearchInputbox(driver).clear();
			lp.emailSearchInputbox(driver).type(subject);
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			if (lp.emailSearchMailBySubject(driver, "Subject").isDisplayed()) {
				lp.emailSearchMailBySubject(driver, "Subject").click();
			}
			if (lp.emailAttachDownloadFile(driver, fileName).isDisplayed()) {
				lp.emailAttachDownloadFile(driver, fileName).click();
			}
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("Successful ");
		} catch(Exception e) {
			exception = e;
		}
	}
	
	public void searchBySubjectAndDownloadFile(WebDriver driver, String subject, String fileName) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		Logger.info("Search By Subject " + subject + " and download");
		try {		
			lp.emailSearchBox(driver).click();
			hardWait(3);
			lp.emailSearchInputbox(driver).click();
			lp.emailSearchInputbox(driver).clear();
			lp.emailSearchInputbox(driver).type(subject);
			hardWait(3);
			action.sendKeys(Keys.ENTER).build().perform();
			if (lp.emailSearchMailBySubject(driver, "Subject").isDisplayed()) {
				lp.emailSearchMailBySubject(driver, "Subject").click();
			}
			if (lp.emailAttachedDownloadButon(driver).isDisplayed()) {
				lp.emailAttachedDownloadButon(driver).click();
			}
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("Successful ");
		} catch(Exception e) {
			exception = e;
		}
	}
	

	public void draftEmail(WebDriver driver) throws InterruptedException {

		try {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		lp.newemailbutton(driver).click();
		Logger.info("Preparing sending mail to users");
		hardWait(5);
		lp.o365emailtolist(driver).type("admin@gatewayO365beatle.com");
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ENTER).build().perform();
		lp.o365emailcclist(driver).click();
		lp.o365emailcclist(driver).type("testuser1@gatewayO365beatle.com");
		hardWait(3);
		action.sendKeys(Keys.ENTER).build().perform();
		hardWait(5);
		lp.subject(driver).waitForLoading(driver);
		lp.subject(driver).click();
		lp.subject(driver).type("This is subject");
		hardWait(5);
		lp.body(driver).click();
		lp.body(driver).type("body");
		Logger.info("Saving email in draft");
		hardWait(4);
		lp.o365emailMoreOptions(driver).click();
		hardWait(4);
		lp.o365emailsavedarftLink(driver).click();
		hardWait(5);
		Logger.info("Saved email in draft");
		} catch(Exception e) {
			exception = e;
		}
	}
	
	
	public void draftEmailPersonal(WebDriver driver) throws InterruptedException {
		try {
			O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
			if (lp.personalEmailNewButton(driver).isDisplayed()) {
				lp.personalEmailNewButton(driver).click();
			}
			Logger.info("Preparing sending mail to users");
			hardWait(5);
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").waitForLoading(driver);
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.").click();
			
			lp.personalEmailSelectByTitle(driver, "To recipients. Enter an email address or a name from your contact list.")
				.type("admin@gatewayO365beatle.com");
			hardWait(3);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER).build().perform();
			hardWait(3);
			lp.personalEmailSelectByTitle(driver, "Subject,").waitForLoading(driver);
			lp.personalEmailSelectByTitle(driver, "Subject,").click();
			lp.personalEmailSelectByTitle(driver, "Subject,").type("This is subject");
			hardWait(5);
			if (lp.personalEmailBody(driver).isDisplayed()){
				Logger.info("Trying with css locator");
				lp.personalEmailBody(driver).click();
				lp.personalEmailBody(driver).type("body");
			}
			lp.personalMoreOptions(driver).click();
			hardWait(5);
			lp.personalSubMenu(driver, "Save draft").click();
			hardWait(5);
			Logger.info("Saved email in draft");
		} catch(Exception e) {
			exception = e;
		}
	}
	
	
	public void loadEmailApp(WebDriver driver, SuiteData suiteData) throws InterruptedException{
			try {	
				O365LoginPage lp =  AdvancedPageFactory.getPageObject(driver,O365LoginPage.class);
				lp.username(driver).clear();lp.username(driver).type(suiteData.getUsername());
				lp.password(driver).clear();lp.password(driver).type(suiteData.getPassword());
				lp.loginButton(driver).mouseOver(driver);
				lp.loginButton(driver).mouseOverClick(driver);
				lp.loginButton(driver).click();
				hardWait(15);
			} catch(Exception e) {
				exception = e;
			}
	}
	
	public String getSignInLabelText(WebDriver driver) {
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);
		return lp.signInLabel(driver).getText().trim();
	}
	
	public void addUser(WebDriver driver, String user) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Add User ");
		try {	
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			hardWait(2);
			lp.adminSection(driver, "Add new users").click();
			driver.switchTo().defaultContent();
			switchFrame(driver.findElement(lp.getBy(lp.adminPopupIframe(driver))), driver);
			if(lp.adminInputUser(driver, "First name").isDisplayed()) {
				lp.adminInputUser(driver, "First name").type(user);
				lp.adminInputUser(driver, "Last name").type(user);
				lp.adminInputUser(driver, "User name. This is a required field.").type(user);
				lp.adminInputUser(driver, "Display name. This is a required field.").type(user);
				lp.adminCreateUserbutton(driver).click();
				hardWait(10);
				if (lp.adminCloseButton(driver).isDisplayed()) {
					lp.adminCloseButton(driver).click();
					
				}
				
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Created user successful");
	}
	
	public void resetPassword(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Deleted User");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#ActiveUsersPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
				lp.adminActiveUserSelect(driver, user).click();
				hardWait(5);
				switchFrame(driver.findElement(lp.getBy(lp.adminActiveUserSideIframe(driver))), driver);
				if (lp.adminActiveUserSideMenu(driver, "RESET PASSWORD").isDisplayed()) {
					lp.adminActiveUserSideMenu(driver, "RESET PASSWORD").click();
					driver.switchTo().defaultContent();
					hardWait(5);
					switchFrame(driver.findElement(lp.getBy(lp.adminIframeByName(driver, "FlyoutIframe"))), driver);
					if (lp.adminResetPasswordButton(driver).isDisplayed()) {
						lp.adminResetPasswordButton(driver).click();
						hardWait(5);
					}
				}
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Delete user successful");
	}
	
	public void changeRole(WebDriver driver, String user, SuiteData suiteData, String role) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Change Role to " + role );
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#ActiveUsersPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
				lp.adminActiveUserSelect(driver, user).click();
				hardWait(5);
				switchFrame(driver.findElement(lp.getBy(lp.adminActiveUserSideIframe(driver))), driver);
				if (lp.adminActiveUserSideMenu(driver, "EDIT USER ROLES").isDisplayed()) {
					lp.adminActiveUserSideMenu(driver, "EDIT USER ROLES").click();
					driver.switchTo().defaultContent();
					hardWait(ACTIVITY_AND_DELAY);
					switchFrame(driver.findElement(lp.getBy(lp.adminIframeByName(driver, "FlyoutIframe"))), driver);
					if (lp.adminRoleCheckBox(driver, role).isDisplayed()) {
						lp.adminRoleCheckBox(driver, role).click();
						lp.adminRoleAlternateEmail(driver).clear();
						lp.adminRoleAlternateEmail(driver).type(suiteData.getSaasAppUsername());
						lp.adminRoleSaveButton(driver).click();
						lp.adminRoleCloseButton(driver).click();
						hardWait(5);
					}
				}
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Change Role successful");
	}
	
	public void addGroup(WebDriver driver, String user, String groupName ,SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Add Group");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#ActiveUsersPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
				lp.adminActiveUserSelect(driver, user).click();
				hardWait(5);
				switchFrame(driver.findElement(lp.getBy(lp.adminActiveUserSideIframe(driver))), driver);
				if (lp.adminActiveUserSideMenu(driver, "ADD TO GROUP").isDisplayed()) {
					lp.adminActiveUserSideMenu(driver, "ADD TO GROUP").click();
					driver.switchTo().defaultContent();
					hardWait(5);
					driver.switchTo().defaultContent();
					switchFrame(driver.findElement(lp.getBy(lp.adminPopupIframe(driver))), driver);
					hardWait(ACTIVITY_AND_DELAY);
					if (lp.adminActiveUserSelect(driver, groupName).isDisplayed()) {
						lp.adminActiveUserSelect(driver, groupName).click();
						lp.adminPopupsave(driver).click();
						if (lp.adminCloseButton(driver).isDisplayed()) {
							lp.adminCloseButton(driver).click();
						}
						
					}
					
				}
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Add Group Completed");
	}
	
	public void deleteUser(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		
		
		Logger.info("Deleted User");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#ActiveUsersPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
				lp.adminActiveUserSelect(driver, user).click();
				hardWait(10);
				switchFrame(driver.findElement(lp.getBy(lp.adminActiveUserSideIframe(driver))), driver);
				if (lp.adminActiveUserSideMenu(driver, "DELETE").isDisplayed()) {
					lp.adminActiveUserSideMenu(driver, "DELETE").click();
					driver.switchTo().defaultContent();
					hardWait(15);
					switchFrame(driver.findElement(lp.getBy(lp.adminIframeByName(driver, "FlyoutIframe"))), driver);
					if (lp.adminActiveUserSideDeleteButton(driver).isDisplayed()) {
						lp.adminActiveUserSideDeleteButton(driver).click();
						lp.adminActiveUserSideCloseButton(driver).click();
					}
				}
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Delete user successful");
	}
	
	public void restoreUser(WebDriver driver, String user, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Restore User");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#EAdminDeletedUsersPage");
			hardWait(ACTIVITY_AND_DELAY);
			for (int i = 0; i< 10; i++) {
				driver.switchTo().defaultContent();
				switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
				if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
					lp.adminActiveUserSelect(driver, user).click();
					hardWait(10);
					switchFrame(driver.findElement(lp.getBy(lp.adminActiveUserSideIframe(driver))), driver);
					if (lp.adminActiveUserRestoreButton(driver).isDisplayed()) {
						lp.adminActiveUserRestoreButton(driver).click();
						break;
					}
				}
				lp.adminRestoreNextPaging(driver).click();
				hardWait(5);
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Restore User Successful");
	}
	
	public void editUser(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Deleted User");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#ActiveUsersPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
				lp.adminActiveUserSelect(driver, user).click();
				hardWait(10);
				switchFrame(driver.findElement(lp.getBy(lp.adminActiveUserSideIframe(driver))), driver);
				if (lp.adminActiveUserEditMenu(driver).isDisplayed()) {
					lp.adminActiveUserEditMenu(driver).click();
					driver.switchTo().defaultContent();
					hardWait(15);
					switchFrame(driver.findElement(lp.getBy(lp.adminEditUserIframe(driver))), driver);
					if (lp.adminEditUserFirstnameInput(driver).isDisplayed()) {
						lp.adminEditUserFirstnameInput(driver).type(user + "Rename");
						lp.adminEditUserSaveButton(driver).click();
					}
				}
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Delete user successful");
	}
	
	public void editCompany(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Edited Company Profile");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#@/CompanyManagement/CompanyProfile.aspx?embedded=1");
			hardWait(ACTIVITY_AND_DELAY);
			driver.switchTo().defaultContent();
			switchFrame(driver.findElement(lp.getBy(lp.adminCompanyIframe(driver))), driver);
			String value = lp.adminCompanyAddressInput(driver).getAttribute("value");
			Logger.info("Value is " + value);
			lp.adminCompanyAddressInput(driver).clear();lp.adminCompanyAddressInput(driver).type(value + " Edited");
			lp.adminCompanyProfileSaveButton(driver).click();
			hardWait(5);
			lp.adminCompanyAddressInput(driver).clear();lp.adminCompanyAddressInput(driver).type(value);
			lp.adminCompanyProfileSaveButton(driver).click();
			hardWait(5);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Delete user successful");
	}
	
	
	public void addContact(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Add Contact");
		try {	
		
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#EAdminMailContactsPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			lp.adminContactCreate(driver).click();
			
			driver.switchTo().defaultContent();
			switchFrame(driver.findElement(lp.getBy(lp.adminPopupIframe(driver))), driver);
			hardWait(ACTIVITY_AND_DELAY);
			if(lp.adminPopupinput(driver, "txtFirstName2").isDisplayed()) {
				lp.adminPopupinput(driver, "txtFirstName2").type(user);
				lp.adminPopupinput(driver, "txtLastName2").type(user);
				lp.adminPopupinput(driver, "txtExternalEmailAddress").type("testuser55@gatewayo365beatle.com");
				
				lp.adminPopupsave(driver).click();
				hardWait(10);
				if (lp.adminCloseButton(driver).isDisplayed()) {
					lp.adminCloseButton(driver).click();
				}
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Add Contact Successful");
	}
	
	public void deleteContact(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Delete Contact");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#EAdminMailContactsPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
				lp.adminActiveUserSelect(driver, user).click();
				hardWait(ACTIVITY_AND_DELAY);
				switchFrame(driver.findElement(lp.getBy(lp.adminContactRightSideIframe(driver))), driver);
				lp.adminContactDelete(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				driver.switchTo().defaultContent();
				if (lp.adminPopup2Yes(driver).isDisplayed()) {
					lp.adminPopup2Yes(driver).click();
				}
				hardWait(10);
			}
			
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Delete Contact Successful");
	}
	
	public void addSharedMailBox(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Add Shared Mail Box");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#SharedMailboxPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			lp.adminCreateSharedEmail(driver).click();
			
			driver.switchTo().defaultContent();
			switchFrame(driver.findElement(lp.getBy(lp.adminPopupIframe(driver))), driver);
			hardWait(ACTIVITY_AND_DELAY);
			if(lp.adminPopupinput(driver, "txtDisplayName").isDisplayed()) {
				lp.adminPopupinput(driver, "txtDisplayName").type(user);
				lp.adminPopupinput(driver, "ctl06_contentContainer_txtUserName").type("testuser55");
				lp.adminPopupsave(driver).click();
				hardWait(10);
				if (lp.adminPopup2Yes(driver).isDisplayed()) {
					lp.adminPopup2Yes(driver).click();
				}
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Add Shared Mail BoxSuccessful");
	}
	
	public void editShareMailBox(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Edit Share Mail Box");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#SharedMailboxPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
				lp.adminActiveUserSelect(driver, user).click();
				hardWait(ACTIVITY_AND_DELAY);
				switchFrame(driver.findElement(lp.getBy(lp.adminContactRightSideIframe(driver))), driver);
				lp.adminSideButton(driver, "Edit the members of this shared mailbox").click();
				driver.switchTo().defaultContent();
				switchFrame(driver.findElement(lp.getBy(lp.adminPopupIframe(driver))), driver);
				if (lp.adminPopupSearchInput(driver).isDisplayed()) {
					lp.adminPopupSearchInput(driver).type("Admin");
					lp.adminPopuDropdownList(driver, "QA Admin").click();
					lp.adminPopupsave(driver).click();
				}
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Edit Share Mail Box Successful");
	}
	
	public void deleteShareMailBox(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Delete Share Mail Box");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#SharedMailboxPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
				lp.adminActiveUserSelect(driver, user).click();
				hardWait(ACTIVITY_AND_DELAY);
				switchFrame(driver.findElement(lp.getBy(lp.adminContactRightSideIframe(driver))), driver);
				if(lp.adminSideButton(driver, "Delete this shared mailbox").isDisplayed()) {
					lp.adminSideButton(driver, "Delete this shared mailbox").click();
					driver.switchTo().defaultContent();
					if (lp.adminPopup2Yes(driver).isDisplayed()) {
						lp.adminPopup2Yes(driver).click();
					}
				}
				hardWait(ACTIVITY_AND_DELAY);
			}
			
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Delete Share Mail Box");
	}
	
	public void addMeetingRooms(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Add Meeting Rooms");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#ResourceMailboxPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			lp.adminCreateSharedEmail(driver).click();
			
			driver.switchTo().defaultContent();
			switchFrame(driver.findElement(lp.getBy(lp.adminPopupIframe(driver))), driver);
			hardWait(ACTIVITY_AND_DELAY);
			if(lp.adminPopupinput(driver, "txtDisplayName").isDisplayed()) {
				lp.adminPopupinput(driver, "txtDisplayName").type(user);
				lp.adminPopupinput(driver, "ctl06_contentContainer_txtUserName").type("testuser55");
				lp.adminPopupinput(driver, "txtResourceCapacity").type("4");
				lp.adminPopupsave(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			}
			driver.switchTo().defaultContent();
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Add Shared Mail BoxSuccessful");
	}
	
	public void deleteMeetingRooms(WebDriver driver, String user, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Delete Meeting Rooms");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + DASH_BOARD_URL);
			hardWait(10);
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#ResourceMailboxPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminActiveUserSelect(driver, user).isDisplayed()) {
				lp.adminActiveUserSelect(driver, user).click();
				hardWait(ACTIVITY_AND_DELAY);
				switchFrame(driver.findElement(lp.getBy(lp.adminContactRightSideIframe(driver))), driver);
				if(lp.adminSideButton(driver, "Delete this meeting room").isDisplayed()) {
					Logger.info("Clicking Delete button");
					lp.adminSideButton(driver, "Delete this meeting room").click();
					hardWait(5);
					driver.switchTo().defaultContent();
					if (lp.adminPopup2Yes(driver).isDisplayed()) {
						lp.adminPopup2Yes(driver).click();
					}
				}
			} else {
				Logger.info("Already selected");
				switchFrame(driver.findElement(lp.getBy(lp.adminContactRightSideIframe(driver))), driver);
				if(lp.adminSideButton(driver, "Delete this meeting room").isDisplayed()) {
					Logger.info("Already selected, Clicking Delete button");
					lp.adminSideButton(driver, "Delete this meeting room").click();
					hardWait(5);
					driver.switchTo().defaultContent();
					if (lp.adminPopup2Yes(driver).isDisplayed()) {
						lp.adminPopup2Yes(driver).click();
					}
				}
			}
			hardWait(ACTIVITY_AND_DELAY);
			
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Delete Meeting Rooms");
	}
	
	public void sharePublicSkype(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Share Public For Skype");
		Logger.info("Share Public Skype");
		try {	
			Logger.info("Render : " + suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#LyncPage");
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#LyncPage");
			
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if(lp.adminskypepublictext(driver).isDisplayed()) {
				Logger.info("Public IM is displayed clicking, Uncheck the checkbox for skype for business and Save");
				lp.adminSkypePublicAccessCheckbox(driver).click();
				lp.adminSkypePublicSaveButton(driver).click();
				driver.switchTo().defaultContent();
				
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
				hardWait(ACTIVITY_AND_DELAY);
				
				switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
				Logger.info("Again Checkbox skype for business and Save");
				lp.adminSkypePublicAccessCheckbox(driver).click();
				lp.adminSkypePublicSaveButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				driver.switchTo().defaultContent();
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
			} else {
				Logger.info("Since already it is uncheck, Checkbox skype for business and Save");
				switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
				lp.adminSkypePublicAccessCheckbox(driver).click();
				lp.adminSkypePublicSaveButton(driver).click();	
				driver.switchTo().defaultContent();
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
			}
			
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Share Public For Skype Completed");
	}
	
	public void externalSharingSiteAllow(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Site Allow");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#SitesPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if(lp.adminSiteCheckboxDisable(driver).isDisplayed()) {
				lp.adminSiteAccesscheckbox(driver).click();
				lp.adminSiteGiveAccess(driver).click();
				lp.adminExternalShareSaveButton(driver).click();
				driver.switchTo().defaultContent();
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
			} else {
				lp.adminSiteNoAccess(driver).click();
				lp.adminSiteGiveAccess(driver).click();
				lp.adminExternalShareSaveButton(driver).click();
				driver.switchTo().defaultContent();
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Site Allow Completed");
	}
	
	public void externalSharingSiteNotAllow(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Site Not Allow");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#SitesPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if(lp.adminSiteCheckboxDisable(driver).isDisplayed()) {
				lp.adminSiteAccesscheckbox(driver).click();
				lp.adminExternalShareSaveButton(driver).click();
				driver.switchTo().defaultContent();
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
				
				lp.adminSiteAccesscheckbox(driver).click();
				lp.adminExternalShareSaveButton(driver).click();
				driver.switchTo().defaultContent();
				if (lp.adminSiteNoAccessPopupYes(driver).isDisplayed()) {
					lp.adminSiteNoAccessPopupYes(driver).click();
				}
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
			} else {
				lp.adminSiteAccesscheckbox(driver).click();
				lp.adminExternalShareSaveButton(driver).click();
				driver.switchTo().defaultContent();
				if (lp.adminSiteNoAccessPopupYes(driver).isDisplayed()) {
					lp.adminSiteNoAccessPopupYes(driver).click();
				}
				
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
				
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Site Not Allow Completed");
	}
	
	public void externalSharingCalender(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("External Sharing Access");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#CalendarPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			hardWait(5);
			if(lp.adminCalenderAccessActiveCheckbox(driver).isDisplayed()) {
				//Make it Inactive
				lp.adminCalenderAccessButton(driver).click();
				hardWait(5);
				// Again Make It Active
				lp.adminCalenderAccessButton(driver).click();
				lp.adminExternalShareSaveButton(driver).click();
				driver.switchTo().defaultContent();
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
				hardWait(5);
			} else {
				lp.adminCalenderAccessButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.adminExternalShareSaveButton(driver).click();
				driver.switchTo().defaultContent();
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
				hardWait(5);
				driver.switchTo().defaultContent();
				switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
				lp.adminCalenderAccessButton(driver).click();
				lp.adminExternalShareSaveButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				driver.switchTo().defaultContent();
				if(lp.adminWarningPopupButton(driver).isDisplayed()) {
					lp.adminWarningPopupButton(driver).click();
				}
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			
			driver.switchTo().defaultContent();
			exception = e;
		}
		Logger.info("Site Allow Completed");
	}	
	
	public void addDomain(WebDriver driver, String domainName, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("External Sharing Access");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#DomainManagerPageLayout");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			hardWait(5);
			if(lp.adminCreateDomainButton(driver).isDisplayed()) {
				lp.adminCreateDomainButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.adminDomainGetStarted(driver).click();
				lp.adminDomaininput(driver).type(domainName);
				lp.adminDomainNextButton(driver).click();
				hardWait(5);
			}
		
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void deleteDomain(WebDriver driver, String domainName, SuiteData suiteData) throws InterruptedException {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("External Sharing Access");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#DomainManagerPageLayout");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			hardWait(5);
			if(lp.adminDomainSelect(driver, domainName).isDisplayed()) {
				lp.adminDomainSelect(driver, domainName).click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.adminDomainDelete(driver).click();
				hardWait(5);
				driver.switchTo().defaultContent();
				switchFrame(driver.findElement(lp.getBy(lp.iframeById(driver, "ContentIFrame_0"))), driver);
				lp.adminDomainRemoveYes(driver).click();
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void publicSharingPublicShare(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("External Sharing Access");
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#SwaySettingPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminswayPublicOnToggleButton(driver).isDisplayed()) {
				lp.adminswayPublicOnToggleButton(driver).click();
			} else if (lp.adminswayPublicOfftogglebutton(driver).isDisplayed()) {
				lp.adminswayPublicOfftogglebutton(driver).click();
			}
			hardWait(ACTIVITY_AND_DELAY);
			if (lp.adminswayPublicOnToggleButton(driver).isDisplayed()) {
				lp.adminswayPublicOnToggleButton(driver).click();
			} else if (lp.adminswayPublicOfftogglebutton(driver).isDisplayed()) {
				lp.adminswayPublicOfftogglebutton(driver).click();
			}
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("");
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void publicExternalShare(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("External Sharing Access");
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#SwaySettingPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminSwayExternalSharingOnToggleButton(driver).isDisplayed()) {
				lp.adminSwayExternalSharingOnToggleButton(driver).click();
			} else {
				lp.adminSwayExternalSharingOffToggleButton(driver).click();
			}
			hardWait(ACTIVITY_AND_DELAY);
			if (lp.adminSwayExternalSharingOnToggleButton(driver).isDisplayed()) {
				lp.adminSwayExternalSharingOnToggleButton(driver).click();
			} else {
				lp.adminSwayExternalSharingOffToggleButton(driver).click();
			}
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void mobileEnableService(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
	
		Logger.info("Mobile Enable");
		driver.switchTo().defaultContent();
		Logger.info("Enable mobile, check yes, ok, and remove service yes for remove mobile service");
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#MobileSettingsEnterprisePage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingEnablemobile(driver).isDisplayed()) {
				lp.adminServiceSettingEnablemobile(driver).click();
			} 
			driver.switchTo().defaultContent();
			hardWait(ACTIVITY_AND_DELAY);
			if (lp.adminServiceSettingPopupCheckbox(driver).isDisplayed()) {
				lp.adminServiceSettingPopupCheckbox(driver).click();
				hardWait(10);
				lp.adminServiceSettingPopupok(driver).mouseOver(driver);
				lp.adminServiceSettingPopupok(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void mobileDeleteService(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("External Sharing Access");
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#MobileSettingsEnterprisePage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingRemoveMobile(driver).isDisplayed()) {
				lp.adminServiceSettingRemoveMobile(driver).click();
			} 
			driver.switchTo().defaultContent();
			hardWait(ACTIVITY_AND_DELAY);
			if (lp.adminServiceSettingPopupYes(driver).isDisplayed()) {
				lp.adminServiceSettingPopupYes(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("");
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void userSoftwareForOfficeEnableAndDisable(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Office Enabled");
		driver.switchTo().defaultContent();	
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#DownloadsPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingUserSoftwareOfficeCheckbox(driver).isDisplayed()) {
				lp.adminServiceSettingUserSoftwareOfficeCheckbox(driver).click();
				lp.adminServiceSettingUserSoftwareSaveButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			} 
			driver.switchTo().defaultContent();
			driver.get(suiteData.getSaasAppBaseUrl() + DASH_BOARD_URL);
			hardWait(5);
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#DownloadsPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingUserSoftwareOfficeCheckbox(driver).isDisplayed()) {
				lp.adminServiceSettingUserSoftwareOfficeCheckbox(driver).click();
				lp.adminServiceSettingUserSoftwareSaveButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			} 
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("");
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void userSoftwareForLyncEnableAndDisable(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Lync Enabled");
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#DownloadsPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettinguserSoftwareLyncCheckbox(driver).isDisplayed()) {
				lp.adminServiceSettinguserSoftwareLyncCheckbox(driver).click();
				lp.adminServiceSettingUserSoftwareSaveButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			} 
			driver.switchTo().defaultContent();
			driver.get(suiteData.getSaasAppBaseUrl() + DASH_BOARD_URL);
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#DownloadsPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettinguserSoftwareLyncCheckbox(driver).isDisplayed()) {
				lp.adminServiceSettinguserSoftwareLyncCheckbox(driver).click();
				lp.adminServiceSettingUserSoftwareSaveButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			} 
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void setPasswordExpiry(WebDriver driver, SuiteData suiteData) {
		/* Service Setting -> Passwords ->  check "Set passwords to never expire " */
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Set Passwrod Expiry");
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#PasswordsSettingsEnterprisePage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingPasswordCheckbox(driver).isDisplayed()) {
				lp.adminServiceSettingPasswordCheckbox(driver).click();
				lp.adminServiceSettingPasswordSaveButton(driver).click();
			} 
			driver.switchTo().defaultContent();
			driver.get(suiteData.getSaasAppBaseUrl() + DASH_BOARD_URL);
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#PasswordsSettingsEnterprisePage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingPasswordCheckbox(driver).isDisplayed()) {
				lp.adminServiceSettingPasswordCheckbox(driver).click();
				lp.adminServiceSettingPasswordSaveButton(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
			
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void setCommunity(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Community Expiry");
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#PrivacySettingsEnterprisePage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingCommunityOn(driver).isDisplayed()) {
				lp.adminServiceSettingCommunityOn(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
			driver.switchTo().defaultContent();
			driver.get(suiteData.getSaasAppBaseUrl() + DASH_BOARD_URL);
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#PrivacySettingsEnterprisePage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingCommunityOff(driver).isDisplayed()) {
				lp.adminServiceSettingCommunityOff(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void setCortana(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Community Expiry");
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#CortanaSettingsPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingCortanaOn(driver).isDisplayed()) {
				lp.adminServiceSettingCortanaOn(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
			driver.switchTo().defaultContent();
			driver.get(suiteData.getSaasAppBaseUrl() + DASH_BOARD_URL);
			driver.get(suiteData.getSaasAppBaseUrl() + "/Admin/Default.aspx?switchtoclassic=true#CortanaSettingsPage");
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminServiceSettingCortanaOff(driver).isDisplayed()) {
				lp.adminServiceSettingCortanaOff(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void siteOnOverview(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Community Expiry");
		String url = "/Admin/Default.aspx?switchtoclassic=true#SharingDashboardPage";
		Logger.info("if on make it off and Check Sites off make it on and click yes popup ");
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + url);
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminExternalSharingOverviewSiteOn(driver).isDisplayed()) {
				lp.adminExternalSharingOverviewSiteOn(driver).click();
				
			} 
			hardWait(ACTIVITY_AND_DELAY);
			driver.switchTo().defaultContent();
			driver.get(suiteData.getSaasAppBaseUrl() + DASH_BOARD_URL);
			driver.get(suiteData.getSaasAppBaseUrl() + url);
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminExternalSharingOverviewSiteOff(driver).isDisplayed()) {
				lp.adminExternalSharingOverviewSiteOff(driver).click();
				driver.switchTo().defaultContent();
				if (lp.adminExternalSharingOverviewPopupYes(driver).isDisplayed()) {
					lp.adminExternalSharingOverviewPopupYes(driver).click();
				}
			} 
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void skypeForBusinessOverview(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Community Expiry");
		String url = "/Admin/Default.aspx?switchtoclassic=true#SharingDashboardPage";
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + url);
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminExternalSharingOverviewSkypeForBusinessOn(driver).isDisplayed()) {
				lp.adminExternalSharingOverviewSkypeForBusinessOn(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
			driver.switchTo().defaultContent();
			driver.get(suiteData.getSaasAppBaseUrl() + DASH_BOARD_URL);
			driver.get(suiteData.getSaasAppBaseUrl() + url);
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminExternalSharingOverviewSkypeForBusinessOff(driver).isDisplayed()) {
				lp.adminExternalSharingOverviewSkypeForBusinessOff(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void integratedAppOverview(WebDriver driver, SuiteData suiteData) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Community Expiry");
		String url = "/Admin/Default.aspx?switchtoclassic=true#SharingDashboardPage";
		driver.switchTo().defaultContent();
		try {	
			driver.get(suiteData.getSaasAppBaseUrl() + url);
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminExternalSharingOverviewIntegratedAppsOn(driver).isDisplayed()) {
				lp.adminExternalSharingOverviewIntegratedAppsOn(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
			driver.switchTo().defaultContent();
			driver.get(suiteData.getSaasAppBaseUrl() + DASH_BOARD_URL);
			driver.get(suiteData.getSaasAppBaseUrl() + url);
			hardWait(ACTIVITY_AND_DELAY);
			switchFrame(driver.findElement(lp.getBy(lp.adminIframe(driver))), driver);
			if (lp.adminExternalSharingOverviewIntegratedAppsOff(driver).isDisplayed()) {
				lp.adminExternalSharingOverviewIntegratedAppsOff(driver).click();
			} 
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void hightlightFolder(WebDriver driver, String folderName) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Actions action = new Actions(driver);
		try {
			WebElement selectedDocElement = driver.findElement(
					lp.getBy(lp.emailSelectFolderStr(driver, folderName)));
			action.moveToElement(selectedDocElement);
			action.contextClick().build().perform();
			
			lp.emailSelectFolder(driver, folderName).mouseOverJS(driver);
			lp.emailSelectFolder(driver, folderName).mouseOverClick(driver);
			
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
		}
	}
	
	public void hightlightSubFolder(WebDriver driver, String folderName) {
/*		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {
			lp.emailSelectSubfolder(driver, folderName).mouseOverJS(driver);
			lp.emailSelectSubfolder(driver, folderName).mouseOverClick(driver);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
		}*/
	}
	
	public void emailCreateSubFolder(WebDriver driver, String folderName, String subFolder) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Sub Folder");
		try {	
			if (lp.emailBacktomainfolder(driver).isDisplayed()) {
				lp.emailBacktomainfolder(driver).click();
				hardWait(10);
			}
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
			}
			hardWait(5);
			hightlightFolder(driver, folderName);
			lp.emailSelectFolder(driver, folderName).mouseOverRightClick(driver);
			lp.emailSelectmenu(driver, "Create new subfolder").click();
			hardWait(5);
			lp.emailInputSubfolder(driver).type(subFolder);
			hightlightFolder(driver, folderName);
			hardWait(5);
			lp.emailBackModernNavigationButton(driver).click();
			
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void emailAddFavoriteSubFolder(WebDriver driver, String subfolder) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Sub Folder");
		try {	
			if (lp.emailBacktomainfolder(driver).isDisplayed()) {
				lp.emailBacktomainfolder(driver).click();
				hardWait(10);
			}
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
				hardWait(5);
				hightlightSubFolder(driver, subfolder);
				lp.emailSelectSubfolder(driver, subfolder).mouseOverRightClick(driver);
				lp.emailSelectmenu(driver, "Add to Favorites").click();
				hardWait(5);
				lp.emailBackModernNavigationButton(driver).click();
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void emailRenameSubFolder(WebDriver driver, String subfolder) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Click Sub Folder");
		Actions action = new Actions(driver);
		try {	
			if (lp.emailBacktomainfolder(driver).isDisplayed()) {
				lp.emailBacktomainfolder(driver).click();
				hardWait(10);
			}
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				lp.emailSelectSubfolderTitle(driver).click();
				if (lp.emailselectsubfolderdownarrow(driver).isDisplayed()) {
					lp.emailselectsubfolderdownarrow(driver).click();
				}
				lp.emailSelectSubfolder(driver, subfolder).mouseOverRightClick(driver);
				WebElement selectedDocElement = driver.findElement(lp.getBy(lp.emailSelectSubfolderStr(driver,subfolder)));
				action.moveToElement(selectedDocElement);
				action.contextClick().build().perform();
				lp.emailSelectmenu(driver, "Rename").click();
				hardWait(5);
				lp.emailInputSubfolder(driver).type("RenameSubFolder");
				lp.emailDeleteHeader(driver).click();
				
				lp.emailSelectSubfolderTitle(driver).click();
				lp.emailSelectSubfolder(driver, "RenameSubFolder").mouseOverRightClick(driver);
				lp.emailSelectmenu(driver, "Rename").click();
				hardWait(5);
				lp.emailInputSubfolder(driver).type(subfolder);
				lp.emailDeleteHeader(driver).click();
				hardWait(5);

				lp.emailBackModernNavigationButton(driver).click();
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	
	public void emailEmptySubFolder(WebDriver driver, String subfolder) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Click Sub Folder");
		try {	
			if (lp.emailBacktomainfolder(driver).isDisplayed()) {
				lp.emailBacktomainfolder(driver).click();
				hardWait(10);
			}
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				hightlightSubFolder(driver, subfolder);
				lp.emailSelectSubfolder(driver, subfolder).mouseOverRightClick(driver);
				lp.emailSelectmenu(driver, "Empty folder").click();
				hardWait(5);
				popupOkClick(driver);
				lp.emailBackModernNavigationButton(driver).click();
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void emailDeleteSubFolder(WebDriver driver, String subfolder) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Click Sub Folder");
		try {
			if (lp.emailBacktomainfolder(driver).isDisplayed()) {
				lp.emailBacktomainfolder(driver).click();
				hardWait(10);
			}
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				
				lp.emailSelectSubfolderTitle(driver).click();
				if (lp.emailselectsubfolderdownarrow(driver).isDisplayed()) {
					lp.emailselectsubfolderdownarrow(driver).click();
					hardWait(5);
				}

				hightlightFolder(driver, subfolder);
				if (lp.emailSelectSubfolder(driver, subfolder).isDisplayed()) {
					lp.emailSelectSubfolder(driver, subfolder).click();
					lp.emailSelectSubfolder(driver, subfolder).mouseOverRightClick(driver);
					lp.emailSelectmenu(driver, "Delete").click();
					hardWait(5);
					popupOkClick(driver);
					hardWait(5);
				}
				lp.emailDeleteHeader(driver).click();
				hardWait(5);
				if (lp.emailSelectDeleteFolder(driver, subfolder).isDisplayed()) {
					lp.emailSelectDeleteFolder(driver, subfolder).click();
					lp.emailSelectDeleteFolder(driver, subfolder).mouseOverRightClick(driver);
					lp.emailSelectmenu(driver, "Delete").click();
					hardWait(5);
					popupOkClick(driver);
				}
				lp.emailBackModernNavigationButton(driver).click();
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception =e;
		}
	}
	
	public void emailMoveSubFolder(WebDriver driver, String subfolder, String moveFolder) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Move SubFolder");
		try {	
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
				hardWait(5);
				/* Move subfolder to AFolder */
				hightlightSubFolder(driver, subfolder);
				lp.emailSelectSubfolder(driver, subfolder).mouseOverRightClick(driver);
				lp.emailSelectmenu(driver, "Move...").click();
				hardWait(5);
				lp.emailMovePopupFolderSelect(driver, moveFolder).click();
				hardWait(5);
				lp.emailMovePopupFolderMove(driver).mouseOverClick(driver);
				/* Move AFolder to sufolder */
				lp.emailSelectSubfolderByParent(driver, moveFolder, subfolder).click();
				lp.emailSelectSubfolder(driver, subfolder).mouseOverRightClick(driver);
				lp.emailSelectmenu(driver, "Move...").click();
				hardWait(5);
				lp.emailMovePopupFolderSelect(driver, "subfolder").click();
				hardWait(5);
				lp.emailMovePopupFolderMove(driver).mouseOverClick(driver);
				hardWait(5);
				lp.emailBackModernNavigationButton(driver).click();
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	/*public void clickMoveButtonjs(WebDriver driver) {
		WebElement element = driver.findElement(By.xpath("//div[@class='popupShadow']//button[@type='button']//span[contains(.,'Move')]"));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
	}*/
	
	public void emailCreateFolder(WebDriver driver, String folderName) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Create Folder");
		try {	
			if (lp.emailBacktomainfolder(driver).isDisplayed()) {
				lp.emailBacktomainfolder(driver).click();
				hardWait(10);
			}
			if (lp.emailFolderPanel(driver).isDisplayed()) {
				lp.emailFolderPanel(driver).click();
				lp.emailNewFolder(driver).click();
				lp.emailInputFolder(driver).type(folderName);
				lp.emailFolderMoreButton(driver).click();
			} else {
				lp.emailNewFolder(driver).click();
				lp.emailInputFolder(driver).type(folderName);
				lp.emailFolderMoreButton(driver).click();
			}
			hardWait(5);
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void popupOkClick(WebDriver driver ) {
		WebElement element = driver.findElement(By.xpath("//button//span[contains(.,'OK')]"));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
	}
	
	public void emailDeleteFolder(WebDriver driver, String folderName) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Delete Folder with name");
		if (lp.emailBacktomainfolder(driver).isDisplayed()) {
			lp.emailBacktomainfolder(driver).click();
			hardWait(10);
		}
		/*Expand Folder if it is not expanded */
		if (lp.emailFoldersExpand(driver).isDisplayed()) {
			lp.emailFoldersExpand(driver).click();
			hardWait(5);
		}
		try {	
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
				hardWait(5);
				if (lp.emailSelectFolder(driver, folderName).isDisplayed()) {
					lp.emailSelectFolder(driver, folderName).mouseOverRightClick(driver);
					lp.emailSelectmenu(driver, "Delete").click();
					hardWait(5);
					popupOkClick(driver);
					hardWait(5);
					lp.emailDeleteHeader(driver).click();
					if (lp.emailSelectDeleteFolder(driver, folderName).isDisplayed()) {
						lp.emailSelectDeleteFolder(driver, folderName).click();
						lp.emailSelectDeleteFolder(driver, folderName).mouseOverRightClick(driver);
						lp.emailSelectmenu(driver, "Delete").click();
						hardWait(5);
						popupOkClick(driver);
					}
				}
				lp.emailBackModernNavigationButton(driver).click();
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void removeFromFavorites(WebDriver driver, String folderName) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Delete Folder with name");
		try {	
			if (lp.emailBacktomainfolder(driver).isDisplayed()) {
				lp.emailBacktomainfolder(driver).click();
				hardWait(10);
			}
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
				hardWait(5);
				if (lp.emailSelectFolder(driver, folderName).isDisplayed()) {
					lp.emailSelectFolder(driver, folderName).mouseOverRightClick(driver);
					lp.emailSelectmenu(driver, "Remove from Favorites").click();
				}
				lp.emailBackModernNavigationButton(driver).click();
				hardWait(5);
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	
	public void emptyFolder(WebDriver driver, String folderName) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Email Delete Folder with name");
		try {	
			if (lp.emailBacktomainfolder(driver).isDisplayed()) {
				lp.emailBacktomainfolder(driver).click();
				hardWait(10);
			}
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
				hardWait(5);
				if (lp.emailSelectFolder(driver, folderName).isDisplayed()) {
					lp.emailSelectFolder(driver, folderName).mouseOverRightClick(driver);
					lp.emailSelectmenu(driver, "Empty folder").click();
					popupOkClick(driver);
				}
				lp.emailBackModernNavigationButton(driver).click();
				hardWait(5);
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void emailRenameFolder(WebDriver driver, String folderName) {
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		Logger.info("Rename Folder with name");
		/*Expand Folder if it is not expanded */
		if (lp.emailBacktomainfolder(driver).isDisplayed()) {
			lp.emailBacktomainfolder(driver).click();
			hardWait(10);
		}
		
		if (lp.emailFoldersExpand(driver).isDisplayed()) {
			lp.emailFoldersExpand(driver).click();
			hardWait(5);
		}
		try {	
			if(lp.emailFolderMoreButton(driver).isDisplayed()) {
				lp.emailFolderMoreButton(driver).click();
				hardWait(5);
				if (lp.emailSelectFolder(driver, folderName).isDisplayed()) {
					lp.emailSelectFolder(driver, folderName).mouseOverRightClick(driver);
					lp.emailSelectmenu(driver, "Rename").click();
					hardWait(5);
					lp.emailInputFolder(driver).type("RenameFolder");
					lp.emailDeleteHeader(driver).click();
					lp.emailSelectFolder(driver, "RenameFolder").mouseOverRightClick(driver);
					lp.emailSelectmenu(driver, "Rename").click();
					hardWait(5);
					lp.emailInputFolder(driver).type(folderName);
					lp.emailDeleteHeader(driver).click();
					hardWait(5);
				}
				lp.emailBackModernNavigationButton(driver).click();
				hardWait(5);
			}
		} catch(Exception e) {
			Logger.info("The Error " + e.getMessage());
			driver.switchTo().defaultContent();
			exception = e;
		}
	}
	
	public void oneDriveLogout(WebDriver driver) throws InterruptedException{
		Logger.info("Logging out from Saas App ");
		O365HomePage lp =  AdvancedPageFactory.getPageObject(driver, O365HomePage.class);
		try {
			lp.personalProfile(driver).mouseOverClick(driver);
			hardWait(5);
			lp.personalSignout(driver).click();
			hardWait(20);
		} catch(Exception e) {
			Logger.info("Error  " + e.getMessage());
		}
		Reporter.log("Logout successfull   ", true);
	}
	
}