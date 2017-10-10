package com.elastica.action.google;
//import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.action.Action;
import com.elastica.asserts.SoftAsserter;
import com.elastica.common.GWCommonTest;
//import com.elastica.beatle.tests.infra.UIConstants;
import com.elastica.common.SuiteData;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.GDrivePage;

public class GDriveAction extends Action {
	final int ACTIVITY_AND_DELAY = 15;
	final int WAIT_FOR_ELEMENT = 5;
	final String baseUrl ="https://drive.google.com";
	GWCommonTest objCommonTests ;
	
	
	public void GDriveAction(){
		objCommonTests	= new GWCommonTest();
	}
	
	public void login(WebDriver driver, SuiteData suiteData) throws InterruptedException {
			GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
			Logger.info("Login to GDrive");
			Logger.info("Saas App credentials: "+suiteData.getSaasAppUsername()+"/"+suiteData.getSaasAppPassword());
			driver.get(suiteData.getSaasAppBaseUrl());
			hardWait(15);
			driver.navigate().refresh();
			gdp.username(driver).waitForLoading(driver, GatewayTestConstants.DEFAULT_ELEMENT_LOAD_TIME );
			Logger.info("Typing in username field: "+suiteData.getSaasAppUsername());
			gdp.username(driver).clear();gdp.username(driver).type(suiteData.getSaasAppUsername()); hardWait(2);
			gdp.nextbutton(driver).click();
			gdp.password(driver).waitForLoading(driver, GatewayTestConstants.DEFAULT_ELEMENT_LOAD_TIME );
			Logger.info("Typing in password field: "+"*******");
			gdp.password(driver).clear();gdp.password(driver).type(suiteData.getSaasAppPassword());
			gdp.signin(driver).click();
			hardWait(10);
			Logger.info("Login Completed");
	}
	
	public void loginBlock(WebDriver driver, SuiteData suiteData) throws InterruptedException {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Logger.info("Login to GDrive");
		driver.get(suiteData.getSaasAppBaseUrl());
		hardWait(5);
		driver.navigate().refresh();
		gdp.username(driver).waitForLoading(driver, GatewayTestConstants.DEFAULT_ELEMENT_LOAD_TIME );
		gdp.username(driver).clear();gdp.username(driver).type(suiteData.getSaasAppUsername());
		gdp.nextbutton(driver).click();
		hardWait(10);
		Logger.info("Login Completed");
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
	
	
	public boolean reloginGmail(WebDriver driver, SuiteData suiteData) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			driver.get(suiteData.getSaasAppBaseUrl());
			gdp.password(driver).isElementPresent(driver);
			gdp.password(driver).clear();gdp.password(driver).type(suiteData.getSaasAppPassword());
			gdp.signin(driver).click();
			if(gdp.gmailSidemenu(driver, "Sent Mail").isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
	}
	
	public void logout(WebDriver driver) throws InterruptedException{
		
		driver.get("https://accounts.google.com/Logout?service=wise");
		hardWait(ACTIVITY_AND_DELAY*2);
		Logger.info("Logout");
	}
	
	public void createDocFile(WebDriver driver, String item) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions builder = new Actions(driver);
		try {
			gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.createButton(driver).click();
			gdp.createNewButtonMenu(driver, "Google Docs").waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.createNewButtonMenu(driver,  "Google Docs").mouseOver(driver);
			builder.sendKeys(Keys.ENTER).perform();
			hardWait(2);
			switchToWindowByTitle("Untitled document - Google Docs", driver);
			hardWait(ACTIVITY_AND_DELAY);
			gdp.docFileNameTextbox(driver).waitForLoading(driver);
			gdp.docFileNameTextbox(driver).clear();
			gdp.docFileNameTextbox(driver).type(item);
			gdp.docFileNameTextbox(driver).sendKeys(Keys.ENTER);
			hardWait(ACTIVITY_AND_DELAY);
			driver.close();
			switchToWindowByTitle("My Drive - Google Drive", driver);
			hardWait(ACTIVITY_AND_DELAY);
			hardWait(5);
		} catch(Exception e) {
			switchToWindowByTitle("My Drive - Google Drive", driver);
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	public void download_item(WebDriver driver, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions action = new Actions(driver);
		gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
		WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
		try {
			if (selectedDocElement.isDisplayed()) {
				selectedDocElement.click();
				action.moveToElement(selectedDocElement);
				action.contextClick().build().perform();
				hardWait(2);
				gdp.rightClickSubmenu(driver, "Download").mouseOver(driver);
				Actions builder = new Actions(driver);
			    builder.sendKeys(Keys.ENTER).perform();
				hardWait(ACTIVITY_AND_DELAY);
			}
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	
	public void copyFile(WebDriver driver, String item) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions action = new Actions(driver);
		gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
		WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
		try {
			if (selectedDocElement.isDisplayed()) {
				selectedDocElement.click();
				action.moveToElement(selectedDocElement);
				action.contextClick().build().perform();
				hardWait(2);
				gdp.rightClickSubmenu(driver, "Make a copy").mouseOver(driver);
				Actions builder = new Actions(driver);
			    builder.sendKeys(Keys.ENTER).perform();
				hardWait(ACTIVITY_AND_DELAY);
			}
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	
	public void sendMailWithAttachment(WebDriver driver,String sub,String to, String fileName) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			if (gdp.composeButton(driver).isDisplayed()) {
				gdp.composeButton(driver).click();	
				hardWait(ACTIVITY_AND_DELAY);
				gdp.toField(driver).click();
				gdp.toField(driver).type(to);
				hardWait(5);
			    gdp.subjectField(driver).type(sub);
			    gdp.bodyField(driver).type(sub);
			    gdp.gmailInsertFileGdriveLogo(driver).click();
			    hardWait(5);
			    switchFrame(driver.findElement(gdp.getBy(gdp.gmailFileAttachIframe(driver))), driver);
			    if (gdp.gmailSelectFileFromGdrive(driver, fileName).isDisplayed()) {
			    	 gdp.gmailSelectFileFromGdrive(driver, fileName).click();
				     gdp.gmailAttachInsertButton(driver).click();
				     driver.switchTo().defaultContent();
				     if (gdp.sendButton(driver).isDisplayed()) {
				    	gdp.sendButton(driver).click();
				     }
				     try {
					     driver.switchTo().defaultContent();
					     switchFrame(driver.findElement(gdp.getBy(gdp.gmailPopupShareIframe(driver))), driver); 
					     if (gdp.gmailPopupShareButton(driver).isDisplayed()) {
					    	 gdp.gmailPopupShareButton(driver).click();
					     }
				     } catch(Exception e) {
				    	 Logger.info("No Share Popup");
				     }
				     hardWait(5);
				    
			    }
			    driver.switchTo().defaultContent();
			}
		} catch(Exception e) {
			Logger.info("Error in Email composition" + e.getMessage());
		}
	}
	
	
	public void sendMailWithAttachmentFileLocal(WebDriver driver,String sub,String to) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			if (gdp.composeButton(driver).isDisplayed()) {
				gdp.composeButton(driver).click();	
				hardWait(ACTIVITY_AND_DELAY);
				gdp.toField(driver).click();
				gdp.toField(driver).type(to);
				hardWait(5);
				gdp.subjectField(driver).click();
				hardWait(5);
			    gdp.subjectField(driver).type(sub);
			    gdp.bodyField(driver).click();
			    hardWait(5);
			    gdp.bodyField(driver).type(sub);
			    gdp.gmailInsertFileGdriveLocal(driver).click();
			    hardWait(5);
			}
		} catch(Exception e) {
			Logger.info("Error in Email composition" + e.getMessage());
		}
	}
	
	public void sentEmail(WebDriver driver) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		hardWait(ACTIVITY_AND_DELAY);
		 if (gdp.sendButton(driver).isDisplayed()) {
			 
		    	gdp.sendButton(driver).click();
		     }
		     try {
			     driver.switchTo().defaultContent();
			     switchFrame(driver.findElement(gdp.getBy(gdp.gmailPopupShareIframe(driver))), driver); 
			     if (gdp.gmailPopupShareButton(driver).isDisplayed()) {
			    	 gdp.gmailPopupShareButton(driver).click();
			     }
		     }catch(Exception e) {
						Logger.info("Error in Email composition" + e.getMessage());
					}
	}
	
	public void downloadEmailFileAttached(WebDriver driver, String sub) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			if (gdp.gmailSidemenu(driver, "Sent Mail").isDisplayed()) {
				gdp.gmailSidemenu(driver, "Sent Mail").click();
				gdp.gmailSelectFirstEmailBySubject(driver, sub).click();
				if (gdp.gmailPreviewDownloadFile(driver).isDisplayed()) {
					gdp.gmailPreviewDownloadFile(driver).click();
					if (gdp.gmailDownloadFile(driver).isDisplayed()) {
						gdp.gmailDownloadFile(driver).waitForLoading(driver);
						gdp.gmailDownloadFile(driver).click();
						hardWait(ACTIVITY_AND_DELAY);
					}
					gdp.dialogClose(driver).click();
				}
			}
		} catch(Exception e) {
			Assert.fail("Error in Email composition " + e.getMessage());
		}
	}
	
	public void searchdownloadEmailFileAttached(WebDriver driver, String sub) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			if (gdp.gmailSeachBox(driver).isDisplayed()) {
				gdp.gmailSeachBox(driver).type(sub);
				gdp.gmailSearchbutton(driver).click();
				hardWait(10);
				gdp.gmailSelectFirstEmailBySubject(driver, sub).click();
				if (gdp.gmailPreviewDownloadFile(driver).isDisplayed()) {
					gdp.gmailPreviewDownloadFile(driver).click();
					if (gdp.gmailDownloadFile(driver).isDisplayed()) {
						gdp.gmailDownloadFile(driver).waitForLoading(driver);
						gdp.gmailDownloadFile(driver).click();
						hardWait(ACTIVITY_AND_DELAY);
					}
					gdp.dialogClose(driver).click();
				}
			}
		} catch(Exception e) {
			Assert.fail("Error in Email composition " + e.getMessage());
		}
	}
	
	public void calenderDownloadAndUpload(WebDriver driver, String sub, String fileName) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		driver.get("https://calendar.google.com");
		hardWait(10);
		gdp.calenderSearch(driver).type(sub);
		gdp.calenderSearchButton(driver).click();
		gdp.calenderTitle(driver, sub).click();
		gdp.calenderEventButton(driver).click();
		gdp.calenderDownloadAttached(driver, fileName).click();
		String title = driver.getTitle();
		switchToWindowByTitle(fileName + " - Google Drive", driver);
		if (gdp.gmailDownloadFile(driver).isDisplayed()) {
			gdp.gmailDownloadFile(driver).waitForLoading(driver);
			gdp.gmailDownloadFile(driver).click();
			hardWait(ACTIVITY_AND_DELAY );
			driver.close();
		}
		hardWait(5);
		switchToWindowByTitle(title, driver);
		if (  gdp.calenderRemoveFile(driver, "readme1.pdf").isDisplayed()) {
			gdp.calenderRemoveFile(driver, "readme1.pdf").click();
		}
	    gdp.calenderUploadfile(driver).click();
	    hardWait(5);
	    switchFrame(driver.findElement(gdp.getBy(gdp.gmailFileAttachIframe(driver))), driver);
	    if (gdp.gmailSelectFileFromGdrive(driver, "readme1.pdf").isDisplayed()) {
	    	 gdp.gmailSelectFileFromGdrive(driver, "readme1.pdf").click();
		     gdp.calenderAttachSendButton(driver).click();
		     hardWait(ACTIVITY_AND_DELAY);
		     gdp.calenderRemoveFile(driver, "readme1.pdf").click();
	    }
	    driver.switchTo().defaultContent();
	    gdp.calenderSave(driver).click();
	    
	}
	
	public void sendMail(WebDriver driver,String sub,String to) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions action = new Actions(driver);
		try {
			if (gdp.composeButton(driver).isDisplayed()) {
					gdp.composeButton(driver).click();	
					hardWait(ACTIVITY_AND_DELAY);
					gdp.toField(driver).click();
					gdp.toField(driver).type(to);
					
					hardWait(ACTIVITY_AND_DELAY);
					gdp.selectFirstUserInTooltip(driver).mouseOver(driver);
					action.sendKeys(Keys.ENTER).perform();
					//gdp.selectFirstUserInTooltip(driver).click();
					//gdp.toField(driver).click();
					gdp.toField(driver).mouseOver(driver);
					action.sendKeys(Keys.ENTER).perform();
					Logger.info("get text test "+ gdp.toField(driver).isDisplayed());
					if (gdp.subjectField(driver).isDisplayed()) {
						gdp.subjectField(driver).mouseOver(driver);
						action.sendKeys(Keys.ENTER).perform();
						//gdp.subjectField(driver).click();
					    try {
						gdp.subjectField(driver).type(sub);
					    } catch(Exception e) {
					    	Logger.info("No Subject" + e);
					    }
					}
				    if (gdp.bodyField(driver).isDisplayed()) {
				    	gdp.bodyField(driver).mouseOver(driver);
				    	action.sendKeys(Keys.ENTER).perform();
				    	//gdp.bodyField(driver).click();
				    	gdp.bodyField(driver).type(sub);
				    }
				    if (gdp.sendButton(driver).isDisplayed()) {
				    	hardWait(10);
				    	//gdp.sendButton(driver).mouseOver(driver);
				    	//action.sendKeys(Keys.ENTER).perform();
				    	gdp.sendButton(driver).click();
				    }
				    hardWait(ACTIVITY_AND_DELAY);
			}
		} catch(Exception e) {
			Logger.info("Error in Email composition " + e.getMessage());
		}
	}	
	
	public void deleteMail(WebDriver driver) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions action = new Actions(driver);
		try {
			if (gdp.gmailSelectFirstEmailCheckbox(driver).isDisplayed()) {
				gdp.gmailSelectFirstEmailCheckbox(driver).click();
				hardWait(5);
				Logger.info("Click Delete");
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.gmailselectfirstemailstr(driver)));
				action.moveToElement(selectedDocElement);
				action.contextClick().build().perform();
				
				gdp.gmailPopupDeleteButton(driver).mouseOver(driver);
				Actions builder = new Actions(driver);
			    builder.sendKeys(Keys.ENTER).perform();
				Logger.info("Clicked Delete Button");
				if(gdp.gmailPopupOkButton(driver).isDisplayed()) {
					gdp.gmailPopupOkButton(driver).click();
				}
				Logger.info("Deleted Email ");
			}
		} catch(Exception e) {
			Logger.info("Error in Email Delete" + e.getMessage());
		}
	}	
	
	public void createDraft(WebDriver driver,String sub,String to) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		WebElement composeButton =gdp.composeButton(driver);
			try {
			if (composeButton.isDisplayed()) {
				//if(composeButton.isDisplayed()
					composeButton.click();	
						hardWait(ACTIVITY_AND_DELAY);
						WebElement toMail =gdp.toField(driver);
						toMail.click();
						toMail.sendKeys(to);
						hardWait(ACTIVITY_AND_DELAY);
						Reporter.log("get text test "+toMail.isDisplayed(),true);
					    gdp.subjectField(driver).sendKeys(sub);
					    //fill message body
					    
					    gdp.bodyField(driver).sendKeys(sub);
					   // click send button
					   // driver.findElement(By.xpath(".//div[text()='Send']")).click();
					    gdp.createDraft(driver).click();
			}
		} catch(Exception e) {
			Assert.fail("Error in Email composition " + e.getMessage());
		}
	}	

	
	public void createFolder(WebDriver driver, String item) { 
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions builder = new Actions(driver);
		try {
			gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.createButton(driver).click();
			gdp.createNewButtonMenu(driver, "Folder").waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.createNewButtonMenu(driver,  "Folder").mouseOver(driver);
			builder.sendKeys(Keys.ENTER).perform();
			hardWait(2);
			gdp.dialogfilenametext(driver).type(item);
			gdp.dialogCreatebutton(driver).click();
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	
	public void uploadFileSilent(WebDriver driver, String uploadFile) {
		Logger.info("chooseFileToUploadUsingRobot in progress");
		
		//driver.manage().window().setPosition(new Point(-2000, 0));
		//Runtime run = Runtime.getRuntime();
		//run.exec("‪C:\\Users\\Administrator\\Documents\\minimizeAll.exe");
		
		try {
			Process p = Runtime.getRuntime().exec("C:\\Users\\Administrator\\Documents\\minimizeAll.exe");
			//Process p1 = Runtime.getRuntime().exec("C:\\Users\\Administrator\\Documents\\minimizeAll.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hardWait(3);
		
		
		
		WebElement neww = driver.findElement(By.xpath(".//*[@id='drive_main_page']/div/div[1]/div[2]/div/div/div[1]/div/div/div[1]/div"));
		neww.click();
		//neww.sendKeys("\n");
		hardWait(5);
		
		
		/*String jsScript = "var input = document.getElementsByTagName('input')[0];"
		        +"input.value='/Users/usman/Desktop/test.html';";
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript(jsScript);*/
		WebElement elem1 = driver.findElement(By.xpath(".//*[@role='menu']/div[2]/div/span[2]/span[1]/div"));
	//	neww.click();
		//elem1 = driver.findElement(By.xpath(".//*[@role='menu']/div[2]/div/span[2]/span[1]/div"));
		hardWait(2);
		System.out.println("1: "+elem1.isDisplayed());
		System.out.println("2: "+elem1.isEnabled());
		System.out.println("3: "+elem1.isSelected());
		
		elem1.click();
		
		System.out.println("1: "+elem1.isDisplayed());
		System.out.println("2: "+elem1.isEnabled());
		System.out.println("3: "+elem1.isSelected());
		//if(elem1.isDisplayed())
		//elem1.click();
		hardWait(5);
		
		WebElement elem = driver.findElement(By.cssSelector("input[type=\"file\"]"));
		hardWait(10);
		
		System.out.println(elem.getTagName());
		System.out.println(elem.isDisplayed());
		System.out.println(elem.isSelected());
		System.out.println(elem.getLocation());
		System.out.println(elem.getSize());
		elem.sendKeys("C:\\jenkins\\"+uploadFile);
		//elem.sendKeys("/Users/usman/Downloads/readme.pdf");
		hardWait(12);
		Logger.info("chooseFileToUploadUsingRobot completed successfully");
	}

	
	
	
	public void uploadFile1(WebDriver driver, String uploadFile) {
		Logger.info("chooseFileToUploadUsingRobot in progress");
//Comment
		//String hostId = TestEnvironment.getTestEnvironmentObject().getSeleniumHost(); 
		try {
			
			
			
			 driver.findElement(By.xpath("//*[@id='drive_main_page']/div/div[1]/div[2]/div/div/div[1]/div/div/div[1]/div")).click();;
			
			Thread.sleep(4000);
			//neww.click();
			
			
			
			
			/*String jsScript = "var input = document.getElementsByTagName('input')[0];"
			        +"input.value='/Users/usman/Desktop/test.html';";
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript(jsScript);*/
			
			
			
			 driver.findElement(By.xpath("//*[@role='menu']/div[2]/div")).click();
			Thread.sleep(5000);
			
			
			
			//GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
			//Actions builder = new Actions(driver);
			
				//gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
				//gdp.createButton(driver).click();
				//gdp.createNewButtonMenu(driver, "File upload").waitForLoading(driver);
				//gdp.createNewButtonMenu(driver,  "File upload").mouseOver(driver);
				
				
				//builder.sendKeys(Keys.ENTER).perform();
				//AwtRobot.uploadFile(uploadFile);
				//hardWait(ACTIVITY_AND_DELAY);
			
				
				//File fileLocation   =	new File(AssignmentsConstants.ASSIGNMENT_FILES_PATH);
				File fileLocation   =	new File(uploadFile);

				String absolutePath = 	fileLocation.getAbsolutePath();// + "\\" + theAssignmentUploadData.get(AssignmentsConstants.KEY_FILE_NAME);
				File exeLocation = null;;
				/*if (TestEnvironment.getTestEnvironmentObject().getBrowserType().toString().equalsIgnoreCase("googlechrome")){
					 exeLocation	= new File(AssignmentsConstants.AUTO_IT_EXE_PATH_CHROME_CMD);
				}
				
				else if (TestEnvironment.getTestEnvironmentObject().getBrowserType().toString().equalsIgnoreCase("ie")){
					 exeLocation	= new File(AssignmentsConstants.AUTO_IT_EXE_PATH_IE);
				}
				
				else if (TestEnvironment.getTestEnvironmentObject().getBrowserType().toString().equalsIgnoreCase("safari")){
					 exeLocation	= new File(AssignmentsConstants.AUTO_IT_EXE_PATH_SAFARI);
					 System.out.println("exeLocation "+exeLocation);
				}*/
				//else if (TestEnvironment.getTestEnvironmentObject().getBrowserType().toString().equalsIgnoreCase("firefox")){
					// exeLocation	= new File(AssignmentsConstants.AUTO_IT_EXE_PATH);
					 exeLocation	= new File(GatewayTestConstants.AUTOIT_FILE_PATH+"\\FileUploadFirefox.exe");
					 System.out.println("exeLocation "+exeLocation);
				//}
				//File exeLocation	= new File(AssignmentsConstants.AUTO_IT_EXE_PATH);
				String exeAbsolutePath  =  exeLocation.getAbsolutePath().replace("\\", "\\\\");

				String args[]   = 	new String[2];
				args[0] 		= 	exeAbsolutePath;
				args[1]			=	absolutePath;
				System.out.println(args[0]);
				System.out.println(args[1]);
				Runtime run = Runtime.getRuntime();
				System.out.println("Before file executed");
				run.exec(args);
				System.out.println("After file executed");
				Thread.sleep(10000);
			
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		Logger.info("chooseFileToUploadUsingRobot completed successfully");
	}

	
	
	
	public void uploadFile(WebDriver driver, String uploadFile) {
		Logger.info("Started Uploading  File" + uploadFile);
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions builder = new Actions(driver);
		Logger.info("Uploading file " + uploadFile);
		try {
			Logger.info("Clicking the New Button");
			gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.createButton(driver).click();
			Logger.info("New Button Clicked");
			gdp.createNewButtonMenu(driver, "File upload").waitForLoading(driver);
			Logger.info("Taking mouse over on Upload item");
			gdp.createNewButtonMenu(driver,  "File upload").mouseOver(driver);
			Logger.info("Selecting Upload option");
			builder.sendKeys(Keys.ENTER).perform();
			Logger.info("Selected upload option... Now starting upload file");
			//AwtRobot.uploadFile(uploadFile);
			hardWait(8);
			((JavascriptExecutor) driver).executeScript("document.querySelector('input[type=file]').style.display='inline';");
			((JavascriptExecutor) driver).executeScript("document.querySelector('input[type=file]').style.visibility='visible';");
			hardWait(10);
			gdp.fileInput(driver).type(uploadFile);
			//elem.sendKeys(uploadFile);
			hardWait(12);
			Logger.info("Completed Upload " + uploadFile);
		} catch(Exception e) {
			//Assert.fail("Error in login " + e.getMessage());
			Logger.info("Uploaded file unsuccessful " + uploadFile);
		}
	}
	
	
	public void clickUploadFile(WebDriver driver) {
		Logger.info("Started Uploading  File");
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions builder = new Actions(driver);
		Logger.info("Uploading file ");
		try {
			Logger.info("Clicking the New Button");
			gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.createButton(driver).click();
			Logger.info("New Button Clicked");
			gdp.createNewButtonMenu(driver, "File upload").waitForLoading(driver);
			Logger.info("Taking mouse over on Upload item");
			gdp.createNewButtonMenu(driver,  "File upload").mouseOver(driver);
			Logger.info("Selecting Upload option");
			builder.sendKeys(Keys.ENTER).perform();
			Logger.info("Selected upload option... Now starting upload file");
			//AwtRobot.uploadFile(uploadFile);
		} catch(Exception e) {
			//Assert.fail("Error in login " + e.getMessage());
			Logger.info("Uploaded file unsuccessful ");
		}
	}
	
	public void MultipleFileupload(WebDriver driver, String [] ciqfiles) {
		
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions builder = new Actions(driver);
		String uploadFile="";
		Logger.info("Uploading file " + uploadFile);
		try {
			gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.createButton(driver).click();
			gdp.createNewButtonMenu(driver, "File upload").waitForLoading(driver);
			gdp.createNewButtonMenu(driver,  "File upload").mouseOver(driver);
			builder.sendKeys(Keys.ENTER).perform();
			//AwtRobot.uploadFile(uploadFile);
			hardWait(5);
			for(int i=0; i<ciqfiles.length;i++){
				//gda.login(getWebDriver(), suiteData);
				uploadFile=	System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i];
				Logger.info("Started Uploading  File" + uploadFile);
				gdp.fileInput(driver).type(uploadFile);
				
				hardWait(12);		
				Logger.info("Completed Upload " + uploadFile);
			}
			
		} catch(Exception e) {
			//Assert.fail("Error in login " + e.getMessage());
			Logger.info("Uploaded file unsuccessful " + uploadFile);
		}
	}
	
	
	
	public void move(WebDriver driver, String fileName, String folderName) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		hardWait(ACTIVITY_AND_DELAY);
		try {
			SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
			gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			selectedDocElement.click();
			Actions action = new Actions(driver);	
			action.moveToElement(selectedDocElement);
			action.contextClick().build().perform();
			hardWait(2);
			gdp.rightClickSubmenu(driver, "Move to..").mouseOver(driver);
			Actions builder = new Actions(driver);
		    builder.sendKeys(Keys.ENTER).perform();
		    hardWait(2);
		    if (folderName.length() > 0) {
		    	gdp.moveFoldername(driver, folderName).click();
		    	gdp.moveButton(driver).click();
		    	if(gdp.alertOkButton(driver).isDisplayed()) {
		    		gdp.alertOkButton(driver).click();
		    	}
		    }
		    selectFolder(driver, folderName);
		    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			selectedDocElement.click();
			action = new Actions(driver);	
			action.moveToElement(selectedDocElement);
			action.contextClick().build().perform();
			hardWait(2);
			gdp.rightClickSubmenu(driver, "Move to..").mouseOver(driver);
			builder = new Actions(driver);
		    builder.sendKeys(Keys.ENTER).perform();
		    hardWait(2);
		    gdp.backbutton(driver).click();
		    hardWait(2);
	    	gdp.moveButton(driver).click();
	    	hardWait(2);
	    	if(gdp.alertOkButton(driver).isDisplayed()) {
	    		gdp.alertOkButton(driver).click();
	    	}
		    gdp.sidebutton(driver, "My Drive").waitForLoading(driver);
			gdp.sidebutton(driver, "My Drive").click();
			hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	
	
	public void homepage(WebDriver driver, SuiteData suiteData)throws InterruptedException{
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Logger.info("Loading home page...");
		driver.get(baseUrl);
		gdp.sidebutton(driver, "My Drive").waitForLoading(driver, WAIT_FOR_ELEMENT);
	}
	
	public void selectFolder(WebDriver driver, String folder) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		hardWait(10);
		try {
			Logger.info("Selecting item " + folder);
			gdp.sidebutton(driver, "My Drive").waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.sidebutton(driver, "My Drive").click();
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, folder)));
			selectedDocElement.isDisplayed();
			for (int i = 0; i< 3; i++) {
				if (gdp.toplabel(driver, folder).isDisplayed()) {
					Logger.info("Inside the Folder " + folder);
					break;
				}
				selectedDocElement.click();
				hardWait(4);
				doubleClick(driver, selectedDocElement);
				hardWait(7);
			}
			Logger.info("Open dialog box popup for  " + folder);
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	
	public String getFolderId(WebDriver driver, String folder) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			Logger.info("Selecting item " + folder);
			gdp.sidebutton(driver, "My Drive").waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.sidebutton(driver, "My Drive").click();
			String id = driver.findElement(gdp.getBy(gdp.getIdFolder(driver, folder))).getAttribute("data-id");
			Logger.info("ID " + id);
			return id;
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
		return "";
	}
	
	public void goToFolder(WebDriver driver, String folder) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			driver.get(folder);
			gdp.sidebutton(driver, "My Drive").waitForLoading(driver, WAIT_FOR_ELEMENT);
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	
	
	public boolean deleteFile(WebDriver driver, String fileName) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		hardWait(ACTIVITY_AND_DELAY);
		try {
			gdp.sidebutton(driver, "My Drive").waitForLoading(driver, WAIT_FOR_ELEMENT);
			SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			if (selectedDocElement.isDisplayed()) {
				hardWait(5);
				selectedDocElement.click();
				hardWait(5);
				gdp.topMenu(driver, "Remove").click();
				hardWait(5);
				Logger.info("Deleted file " + fileName);
				return true;
			} else {
				return false;
			}
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public void selectFile(WebDriver driver, String fileName) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		hardWait(ACTIVITY_AND_DELAY);
		try {
			gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.sidebutton(driver, "My Drive").waitForElementToBeVisible(driver);
			SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			selectedDocElement.click();
			hardWait(5);
		} catch(Exception e) {
			Assert.fail("Error in Selet  file  " + fileName  + e.getMessage());
		}
	}
	
	
	public void deleteForever(WebDriver driver, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver, GDrivePage.class);
		try {
			driver.navigate().refresh();
			gdp.sidebutton(driver, "Trashed items").waitForLoading(driver, WAIT_FOR_ELEMENT);
			gdp.sidebutton(driver, "Trashed items").click();
			hardWait(10);
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			if (selectedDocElement.isDisplayed()) {
				selectedDocElement.click();
				gdp.topMenu(driver, "Delete forever").click();
				gdp.dialogDeleteforeverbutton(driver).click();
			}
			hardWait(ACTIVITY_AND_DELAY); 
		} catch(Exception e) {
			gdp.sidebutton(driver, "My Drive").waitForElementToBeVisible(driver);
			Assert.fail("Error in restore file  " + item  + e.getMessage());
		}
	}
	
	public void restoreFile(WebDriver driver, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver, GDrivePage.class);
		hardWait(ACTIVITY_AND_DELAY);
		for(int i = 0; i < 3; i++) {
			try {
				driver.navigate().refresh();
				gdp.sidebutton(driver, "Trashed items").waitForLoading(driver, WAIT_FOR_ELEMENT);
				gdp.sidebutton(driver, "Trashed items").click();
				Logger.info("The Xpath " + gdp.getBy(gdp.selectDoc(driver, item)));
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
				if (selectedDocElement.isDisplayed()) {
					selectedDocElement.click();
					hardWait(5);
					gdp.topMenu(driver, "Restore").click();
					gdp.sidebutton(driver, "My Drive").waitForElementToBeVisible(driver);
					hardWait(ACTIVITY_AND_DELAY); 
				}
			} catch(Exception e) {
				continue;
			}
			return;
		}
	}
	
	public void downloadFolder(WebDriver driver, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions action = new Actions(driver);
		try {
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
		    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
		    if (selectedDocElement.isDisplayed()) {
			    selectedDocElement.click();
				action.moveToElement(selectedDocElement);
				action.contextClick().build().perform();
				hardWait(2);
				gdp.rightClickSubmenu(driver, "Download").mouseOver(driver);
				action = new Actions(driver);
				action.sendKeys(Keys.ENTER).perform();
				hardWait(ACTIVITY_AND_DELAY*2);
		    } 		
		} catch(Exception e) {
			Assert.fail("Error ShareByemailRightClick  " + item  + e.getMessage());
		}
	}
	
	public void shareActivity(WebDriver driver, String email, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
		hardWait(ACTIVITY_AND_DELAY);
		Logger.info("Going to start Sharing ");
		Actions action = new Actions(driver);
		for (int i = 0; i< 3; i++) {
			Logger.info("Trying "+(i+1)+" times sharing file");
			try {
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    if (selectedDocElement.isDisplayed()) {
			    	Logger.info("File displayed... ");
				    selectedDocElement.click();
				    Logger.info("File selected... ");
					action.moveToElement(selectedDocElement);
					action.contextClick().build().perform();
					hardWait(8);
					gdp.rightClickSubmenu(driver, "Share...").mouseOver(driver);
					action = new Actions(driver);
					Logger.info("Clicking the share option on file... ");
					action.sendKeys(Keys.ENTER).perform();
					Logger.info("Clicked the share dialog ");
					Logger.info("Waiting for share dialog to get appear... ");
					hardWait(15);
			    	//Share Functionality
					Logger.info("Share Popup Displayed");
					Logger.info("Switching to share popup dialog");
					switchFrame(driver.findElement(gdp.getBy(gdp.shareWithOtherIframe(driver))), driver);
					//switchFrame(driver.findElement(gdp.("//iframe[@title='Share with others']")), driver);
					gdp.shareEmailTextbox(driver).waitForLoading(driver);
					/* Share to Public*/
					if(gdp.publicShareOffLink(driver).isDisplayed()) {
						hardWait(10);
						gdp.publicShareOffLink(driver).click();
					    gdp.canviewdropdown(driver).click();// Remove shared option
						hardWait(5);
						gdp.noShare(driver).mouseOver(driver);
						hardWait(5);
						action.sendKeys(Keys.ENTER).perform();
						hardWait(10);
					    gdp.publicShareOffLink(driver).waitForLoading(driver);
					    Logger.info("After selecting share off  Value" + gdp.canviewdropdown(driver).getText());
					    gdp.publicShareOffLink(driver).waitForLoading(driver);
					    hardWait(5);
					    gdp.publicShareOffLink(driver).click();
					    hardWait(5);
					    gdp.publicShareOnLink(driver).waitForLoading(driver);
					    Logger.info("Public Sharing completed");
		
					} else {
					    gdp.canviewdropdown(driver).click();// Remove shared option
						hardWait(5);
						gdp.noShare(driver).mouseOver(driver);
						hardWait(5);
						action.sendKeys(Keys.ENTER).perform();
						hardWait(10);
					    gdp.publicShareOffLink(driver).waitForLoading(driver);
					    Logger.info("After selecting share off  Value" + gdp.canviewdropdown(driver).getText());
					    
					    gdp.publicShareOffLink(driver).waitForLoading(driver);
					    gdp.publicShareOffLink(driver).click();
					    gdp.publicShareOnLink(driver).waitForLoading(driver);
					    Logger.info("Public Sharing completed");
					}    
					/* End Share to Public*/
					hardWait(10);
					Logger.info("Enter Email " + email + " to Share to other");
					gdp.shareEmailTextbox(driver).clear(); 
					hardWait(3);
					gdp.shareEmailTextbox(driver).type(email);
					action.sendKeys(Keys.ENTER).perform();
					hardWait(10);
					gdp.dialogPermissionDropdown(driver).click();
					hardWait(8);
					Logger.info("Click on Edit Option");
					gdp.dialogPermission(driver, "Can edit").mouseOver(driver);
					action.sendKeys(Keys.ENTER).perform();
		
					gdp.shareOkButton(driver).waitForLoading(driver);
					gdp.shareOkButton(driver).click();
					hardWait(5);
					if (gdp.yesDialogbutton(driver).isElementVisible()) {
						gdp.yesDialogbutton(driver).click();
					}
			    }
				driver.switchTo().defaultContent();
				Logger.info("Completed Sharing");
				break;
			} catch(Exception e) {
				driver.switchTo().defaultContent();
				driver.navigate().refresh();
				Logger.info("Error"  + e);
				continue;
			}
		}
	} 
	
	public void shareEmail(WebDriver driver, String email, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
		hardWait(ACTIVITY_AND_DELAY);
		Logger.info("Started Sharing ");
		Actions action = new Actions(driver);
		for (int i = 0; i< 3; i++) {
			try {
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    if (selectedDocElement.isDisplayed()) {
				    selectedDocElement.click();
					action.moveToElement(selectedDocElement);
					action.contextClick().build().perform();
					hardWait(2);
					gdp.rightClickSubmenu(driver, "Share...").mouseOver(driver);
					action = new Actions(driver);
					action.sendKeys(Keys.ENTER).perform();
					hardWait(ACTIVITY_AND_DELAY*2);
			    	//Share Functionality
					Logger.info("Share Popup Displayed");
					switchFrame(driver.findElement(gdp.getBy(gdp.shareWithOtherIframe(driver))), driver);
					//switchFrame(driver.findElement(gdp.("//iframe[@title='Share with others']")), driver);
					gdp.shareEmailTextbox(driver).waitForLoading(driver);
					Logger.info("Enter Email " + email + "To Share to other");
					gdp.shareEmailTextbox(driver).clear(); gdp.shareEmailTextbox(driver).type(email);
					action.sendKeys(Keys.ENTER).perform();
					hardWait(5);
					gdp.dialogPermissionDropdown(driver).click();
					hardWait(5);
					Logger.info("Click on Edit Option");
					gdp.dialogPermission(driver, "Can edit").mouseOver(driver);
					action.sendKeys(Keys.ENTER).perform();
					gdp.shareOkButton(driver).waitForLoading(driver);
					gdp.shareOkButton(driver).click();
					hardWait(5);
					if (gdp.yesDialogbutton(driver).isElementVisible()) {
						gdp.yesDialogbutton(driver).click();
					}
			    }
				driver.switchTo().defaultContent();
				Logger.info("Completed Sharing");
				break;
			} catch(Exception e) {
				driver.switchTo().defaultContent();
				driver.navigate().refresh();
				Logger.info("Error"  + e);
				continue;
			}
		}
	} 
	
	
	public void shareEmailBlock(WebDriver driver, String email, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
		hardWait(ACTIVITY_AND_DELAY);
		Logger.info("Started Sharing ");
		Actions action = new Actions(driver);
		for (int i = 0; i< 3; i++) {
			try {
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    if (selectedDocElement.isDisplayed()) {
				    selectedDocElement.click();
					action.moveToElement(selectedDocElement);
					action.contextClick().build().perform();
					hardWait(2);
					gdp.rightClickSubmenu(driver, "Share...").mouseOver(driver);
					action = new Actions(driver);
					action.sendKeys(Keys.ENTER).perform();
					hardWait(ACTIVITY_AND_DELAY*2);
			    	//Share Functionality
					Logger.info("Share Popup Displayed");
					switchFrame(driver.findElement(gdp.getBy(gdp.shareWithOtherIframe(driver))), driver);
					//switchFrame(driver.findElement(gdp.("//iframe[@title='Share with others']")), driver);
					gdp.shareEmailTextbox(driver).waitForLoading(driver);
					Logger.info("Enter Email " + email + "To Share to other");
					gdp.shareEmailTextbox(driver).clear(); gdp.shareEmailTextbox(driver).type(email);
					action.sendKeys(Keys.ENTER).perform();
					hardWait(5);
					gdp.dialogPermissionDropdown(driver).click();
					hardWait(5);
					Logger.info("Click on Edit Option");
					gdp.dialogPermission(driver, "Can edit").mouseOver(driver);
					action.sendKeys(Keys.ENTER).perform();
					gdp.shareOkButton(driver).waitForLoading(driver);
					gdp.shareOkButton(driver).click();
					hardWait(5);
			    }
				break;
			} catch(Exception e) {
				driver.switchTo().defaultContent();
				driver.navigate().refresh();
				Logger.info("Error"  + e);
				continue;
			}
		}
	} 
	
	public void shareByLinkByTopMenu(WebDriver driver, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
		hardWait(ACTIVITY_AND_DELAY);
		Logger.info("Link Share");
		for (int i = 0; i< 3; i++) {
			try {
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    if (selectedDocElement.isDisplayed()) {
				    selectedDocElement.click();
				    hardWait(5);
				    gdp.topMenu(driver, "Get shareable").click();
				    hardWait(5);
				    if (gdp.linksharingoff(driver).isDisplayed()) {
				    	gdp.linksharingoff(driver).click();
				    } else {
				    	gdp.linksharingon(driver).click();
				    }
				    if (gdp.linksharingon(driver).isDisplayed()) {
				    	gdp.linksharingon(driver).click();
				    } else {
				    	gdp.linksharingoff(driver).click();
				    }
					hardWait(ACTIVITY_AND_DELAY);
					break;
			    }
		    } catch(Exception e) {
				Logger.info("Error"  + e);
				continue;
			}
		}
	} 
	
	public void shareByLinkByTopMenuWithOutBlock(WebDriver driver, String item) {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
		hardWait(ACTIVITY_AND_DELAY);
		Logger.info("Link Share");
		for (int i = 0; i< 3; i++) {
			try {
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    jse.executeScript("arguments[0].scrollIntoView(true);", selectedDocElement);
			    if (selectedDocElement.isDisplayed()) {
				    selectedDocElement.click();
				    hardWait(5);
				    gdp.topMenu(driver, "Get shareable").click();
				    hardWait(5);
				    if (gdp.linksharingoff(driver).isDisplayed()) {
				    	gdp.linksharingoff(driver).click();
				    } 

				    if (gdp.linksharingon(driver).isDisplayed()) {
				    	gdp.linksharingon(driver).click();
				    } 
					hardWait(ACTIVITY_AND_DELAY);
					break;
			    }
		    } catch(Exception e) {
				Logger.info("Error"  + e);
				continue;
			}
		}
	} 
	
	public void shareByLinkByTopMenuBlock(WebDriver driver, String item) {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
		hardWait(ACTIVITY_AND_DELAY);
		Logger.info("Link Share");
		for (int i = 0; i< 3; i++) {
			try {
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    jse.executeScript("arguments[0].scrollIntoView(true);", selectedDocElement);
			    if (selectedDocElement.isDisplayed()) {
				    selectedDocElement.click();
				    hardWait(5);
				    gdp.topMenu(driver, "Get shareable").click();
				    hardWait(5);
				    if (gdp.linksharingoff(driver).isDisplayed()) {
				    	gdp.linksharingoff(driver).click();
				    } 
					hardWait(ACTIVITY_AND_DELAY);
					break;
			    } else {
			    	Logger.info(" Item " +item  + " Not found" );
			    }
		    } catch(Exception e) {
				Logger.info("Error"  + e);
				continue;
			}
		}
	} 
	
	public boolean isExist(WebDriver driver, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Logger.info("Searching if file exist " + item);
		hardWait(ACTIVITY_AND_DELAY);
		try {
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
		    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
		    if (selectedDocElement.isDisplayed()) {
		    	Logger.info("Search completed "+item+ " found");
		    	return true;
		    } else {
		    	Logger.info("Search completed "+item+ " NOT found");
		    	return false;
		    }
		} catch(Exception e) {
			Logger.info("Search completed "+item+ " NOT found");
		}
		return false;
	}
	
	public void rightClickAndshareByEmailBothEditAndView(WebDriver driver, String email, String item) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		gdp.createButton(driver).waitForLoading(driver, WAIT_FOR_ELEMENT);
		hardWait(20);
		Actions action = new Actions(driver);
		for (int i = 0; i< 3; i++) {
			try {
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, item)));
			    if (selectedDocElement.isDisplayed()) {
				    selectedDocElement.click();
					action.moveToElement(selectedDocElement);
					action.contextClick().build().perform();
					hardWait(2);
					gdp.rightClickSubmenu(driver, "Share...").mouseOver(driver);
					action = new Actions(driver);
					action.sendKeys(Keys.ENTER).perform();
					hardWait(10);
			    	//Share Functionality
					switchFrame(driver.findElement(By.xpath("//iframe[@title='Share with others']")), driver);
					gdp.shareEmailTextbox(driver).waitForLoading(driver);
					/* Share to Public*/
					Logger.info("Share to public");
					if(gdp.publicShareOffLink(driver).isDisplayed()) {
						hardWait(10);
						gdp.publicShareOffLink(driver).click();
						if (gdp.canviewdropdown(driver).isDisplayed()) {
							gdp.canviewdropdown(driver).click();
						}
						hardWait(5);
						gdp.noShare(driver).mouseOver(driver);
						hardWait(5);
						action.sendKeys(Keys.ENTER).perform();
						hardWait(10);
					    gdp.publicShareOffLink(driver).waitForLoading(driver);
					    Logger.info("After selecting share off  Value" + gdp.canviewdropdown(driver).getText());
					    gdp.publicShareOffLink(driver).waitForLoading(driver);
					    hardWait(5);
					    gdp.publicShareOffLink(driver).click();
					    hardWait(5);
					    gdp.publicShareOnLink(driver).waitForLoading(driver);
		
					} else {
						if (gdp.canviewdropdown(driver).isDisplayed()) {
							gdp.canviewdropdown(driver).click();
						}
						hardWait(5);
						gdp.noShare(driver).mouseOver(driver);
						hardWait(5);
						action.sendKeys(Keys.ENTER).perform();
						hardWait(10);
					    gdp.publicShareOffLink(driver).waitForLoading(driver);
					    Logger.info("After selecting share off  Value" + gdp.canviewdropdown(driver).getText());
					    
					    gdp.publicShareOffLink(driver).waitForLoading(driver);
					    gdp.publicShareOffLink(driver).click();
					    gdp.publicShareOnLink(driver).waitForLoading(driver);
					    Logger.info("After selecting share on value" + gdp.canviewdropdown(driver).getText());
					}   
					Logger.info("End of public share");
					/* End Share to Public*/
					gdp.shareEmailTextbox(driver).clear(); gdp.shareEmailTextbox(driver).type(email);
					action.sendKeys(Keys.ENTER).perform();
					hardWait(5);
					gdp.dialogPermissionDropdown(driver).click();
					hardWait(5);
					gdp.dialogPermission(driver, "Can edit").mouseOver(driver);
					action.sendKeys(Keys.ENTER).perform();
		
					gdp.shareOkButton(driver).waitForLoading(driver);
					gdp.shareOkButton(driver).click();
					hardWait(5);
					if (gdp.yesDialogbutton(driver).isElementVisible()) {
						gdp.yesDialogbutton(driver).click();
					}
			    }
				driver.switchTo().defaultContent();
				//Can View 
				Logger.info("Can View");
				if (selectedDocElement.isDisplayed()) {
					hardWait(10);
				    selectedDocElement.click();
					action.moveToElement(selectedDocElement);
					action.contextClick().build().perform();
					hardWait(2);
					Logger.info("Right click share ");
					gdp.rightClickSubmenu(driver, "Share...").mouseOver(driver);
					action = new Actions(driver);
					action.sendKeys(Keys.ENTER).perform();
					hardWait(10);
			    	//Share Functionality
					Logger.info("Share with Other ");
					switchFrame(driver.findElement(By.xpath("//iframe[@title='Share with others']")), driver);
					gdp.shareEmailTextbox(driver).waitForLoading(driver);
					gdp.shareEmailTextbox(driver).clear(); gdp.shareEmailTextbox(driver).type(email);
					action.sendKeys(Keys.ENTER).perform();
					gdp.dialogPermissionDropdown(driver).click();
					hardWait(5);
					Logger.info("Can View");
					gdp.dialogPermission(driver, "Can view").mouseOver(driver);
					action.sendKeys(Keys.ENTER).perform();
					hardWait(5);
					gdp.shareOkButton(driver).waitForLoading(driver);
					hardWait(5);
					gdp.shareOkButton(driver).click();
					hardWait(5);
					if (gdp.yesDialogbutton(driver).isElementVisible()) {
						gdp.yesDialogbutton(driver).click();
					}
					driver.switchTo().defaultContent();
					break;
				}
				hardWait(ACTIVITY_AND_DELAY);
			} catch(Exception e) {
				Logger.info(" Count " + i + " " + e.getMessage());
				driver.switchTo().defaultContent();
				driver.navigate().refresh();
				continue;
				//Assert.fail("Error ShareByemailRightClick  " + item  + e.getMessage());
			}
			
		} 
		
	}
	
	public void shareByEmail(WebDriver driver, String email, String fileName) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions builder = new Actions(driver);
		try {
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			selectedDocElement.click();
			doubleClick(driver, selectedDocElement);
			hardWait(5);
			switchToWindowByTitle("rename - Google Docs", driver);
			hardWait(5);
			gdp.docFileNameTextbox(driver).waitForLoading(driver);
			hardWait(5);
			gdp.docShareButton(driver).waitForLoading(driver);
			gdp.docShareButton(driver).click();
			hardWait(2);
			switchFrame(driver.findElement(By.xpath("//iframe[@title='Share with others']")), driver);
			gdp.shareEmailTextbox(driver).waitForLoading(driver);
			/* Share to Public*/
			if(gdp.publicShareOffLink(driver).isDisplayed()) {
				gdp.publicShareOffLink(driver).click();
			} else {
			    gdp.canviewdropdown(driver).click();// Remove shared option
				hardWait(5);
				gdp.noShare(driver).mouseOver(driver);
				
			    builder.sendKeys(Keys.ENTER).perform();
			    gdp.publicShareOffLink(driver).waitForLoading(driver);
			    Logger.info("After selecting share off  Value" + gdp.canviewdropdown(driver).getText());
			    
			    gdp.publicShareOffLink(driver).waitForLoading(driver);
			    gdp.publicShareOffLink(driver).click();
			    gdp.publicShareOnLink(driver).waitForLoading(driver);
			    Logger.info("After selecting share on value" + gdp.canviewdropdown(driver).getText());
			}    
			/* End Share to Public*/
			gdp.shareEmailTextbox(driver).clear(); gdp.shareEmailTextbox(driver).type(email);
			builder.sendKeys(Keys.ENTER).perform();
			//gdp.shareEmailTextbox(driver).click();
			hardWait(5);
			gdp.shareOkButton(driver).waitForLoading(driver);
			gdp.shareOkButton(driver).click();
			hardWait(5);
			if (gdp.yesDialogbutton(driver).isElementVisible()) {
				gdp.yesDialogbutton(driver).click();
			}
			driver.close();
		} catch(Exception e) {
			driver.close();
			Assert.fail("Error in login " + e.getMessage());
			
		}
	}
	
	public void renameFile(WebDriver driver, String fileName, String renameFileName) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		try {
			SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			selectedDocElement.click();
			Actions action = new Actions(driver);	
			Actions action2 = action.moveToElement(selectedDocElement);
			action2.contextClick().build().perform();
			hardWait(2);
			gdp.rightClickSubmenu(driver, "Rename...").mouseOver(driver);
			Actions builder = new Actions(driver);
		    builder.sendKeys(Keys.ENTER).perform();
		    gdp.renameTextbox(driver).clear();gdp.renameTextbox(driver).type(renameFileName);
		    gdp.renameOkButton(driver).click();
		    hardWait(ACTIVITY_AND_DELAY);
		} catch(Exception e) {
			Assert.fail("Error in login " + e.getMessage());
		}
	}
	
	public void rename(WebDriver driver, String fileName) {
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
			try {
				driver.navigate().refresh();
				SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
				WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
				selectedDocElement.click();
				doubleClick(driver, selectedDocElement);
				hardWait(5);
				switchToWindowByTitle("rename - Google Docs", driver);
				hardWait(5);
				gdp.docFileNameTextbox(driver).waitForLoading(driver);
				gdp.docFileNameTextbox(driver).clear();
				gdp.docFileNameTextbox(driver).type(fileName + "2");
				gdp.docFileNameTextbox(driver).sendKeys(Keys.ENTER);
				hardWait(15);
				//Rename file to previous one
				gdp.docFileNameTextbox(driver).waitForLoading(driver);
				gdp.docFileNameTextbox(driver).clear();
				gdp.docFileNameTextbox(driver).type(fileName);
				gdp.docFileNameTextbox(driver).sendKeys(Keys.ENTER);
				// Waiting  for Rename and Log appear
				hardWait(ACTIVITY_AND_DELAY);
				//break;
			} catch(Exception e) {
				Assert.fail("Error in login " + e.getMessage());
			}
	}
	
	public void download(WebDriver driver, String fileName) throws InterruptedException{
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Logger.info("Downloading file " + fileName);
		try {
			driver.navigate().refresh();
			SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("The xpath " + gdp.selectDoc(driver, fileName)  + "processed " + gdp.getBy(gdp.selectDoc(driver, fileName)) );
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			if (selectedDocElement.isEnabled()) {
				Logger.info("Selected file " + fileName);
				selectedDocElement.click();
				Logger.info("Double Click " + fileName);
				doubleClick(driver, selectedDocElement);
				Logger.info("Waiting Download Button For The File " + fileName);
				gdp.dialogDownloadFile(driver).waitForLoading(driver);
				hardWait(10);
				Logger.info("Clicking Download Button For The File " + fileName);
				gdp.dialogDownloadFile(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				Logger.info("Clicking Close Button For The File " + fileName);
				gdp.dialogClose(driver).click();
				Logger.info("Downloaded file " + fileName);
			} 
		} catch(Exception e) {
			Logger.info("Downlaod issue" + e);
		}
	}
	
	public void downloadWithIE(WebDriver driver, String fileName, String browserName) throws InterruptedException{
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Logger.info("Downloading file " + fileName);
		try {
			driver.navigate().refresh();
			SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
			hardWait(ACTIVITY_AND_DELAY);
			Logger.info("The xpath " + gdp.selectDoc(driver, fileName)  + "processed " + gdp.getBy(gdp.selectDoc(driver, fileName)) );
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			if (selectedDocElement.isEnabled()) {
				Logger.info("Selected file " + fileName);
				selectedDocElement.click();
				Logger.info("Double Click " + fileName);
				doubleClick(driver, selectedDocElement);
				Logger.info("Waiting Download Button For The File " + fileName);
				gdp.dialogDownloadFile(driver).waitForLoading(driver);
				hardWait(10);
				Logger.info("Clicking Download Button For The File " + fileName);
				gdp.dialogDownloadFile(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				if(browserName.equals("ie")){
					Logger.info("IE browser detected");
					objCommonTests.ieDownloadFile();
				}
				Logger.info("Clicking Close Button For The File " + fileName);
				gdp.dialogClose(driver).click();
				Logger.info("Downloaded file " + fileName);
			} 
		} catch(Exception e) {
			Logger.info("Downlaod issue" + e);
		}
	}
	
	
	public void downloadBlock(WebDriver driver, String fileName) throws InterruptedException{
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Logger.info("Downloading file " + fileName);
		try {
			driver.navigate().refresh();
			SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
			Logger.info("The xpath " + gdp.selectDoc(driver, fileName)  + "processed " + gdp.getBy(gdp.selectDoc(driver, fileName)) );
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			if (selectedDocElement.isEnabled()) {
				selectedDocElement.click();
				doubleClick(driver, selectedDocElement);
				gdp.dialogDownloadFile(driver).waitForLoading(driver);
				gdp.dialogDownloadFile(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
			} 
		} catch(Exception e) {
			Logger.info("Downlaod issue" + e);
		}
	}
	
	
	public void downloadBigFile(WebDriver driver, String fileName) throws InterruptedException{
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Logger.info("Downloading file " + fileName);
		try {
			driver.navigate().refresh();
			SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
			Logger.info("The xpath " + gdp.selectDoc(driver, fileName)  + "processed " + gdp.getBy(gdp.selectDoc(driver, fileName)) );
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			if (selectedDocElement.isEnabled()) {
				selectedDocElement.click();
				doubleClick(driver, selectedDocElement);
				gdp.dialogDownloadFile(driver).waitForLoading(driver);
				gdp.dialogDownloadFile(driver).click();
				hardWait(ACTIVITY_AND_DELAY);
				if (gdp.downloadAnywayButton(driver).isDisplayed()) {
					gdp.downloadAnywayButton(driver).click();
				}
				gdp.dialogClose(driver).click();
				Logger.info("Downloaded file " + fileName);
			} 
		} catch(Exception e) {
			Logger.info("Downlaod issue" + e);
		}
	}
	
	public void downloadFromPage(WebDriver driver, String fileName) throws InterruptedException{
		GDrivePage gdp =  AdvancedPageFactory.getPageObject(driver,GDrivePage.class);
		Actions action = new Actions(driver);	
		try {
			driver.navigate().refresh();
			SoftAsserter.assertFalse(gdp.selectDoc(driver, fileName).contains("FILENAME"), "File name is missing, Xpath: " + gdp.selectDoc(driver, fileName));
			Logger.info("The xpath " + gdp.selectDoc(driver, fileName)  + "processed " + gdp.getBy(gdp.selectDoc(driver, fileName)) );
			WebElement selectedDocElement = driver.findElement(gdp.getBy(gdp.selectDoc(driver, fileName)));
			if (selectedDocElement.isEnabled()) {
				selectedDocElement.click();
				action.moveToElement(selectedDocElement);
				action.contextClick().build().perform();
				hardWait(2);
				gdp.clickDownloadSubmenu(driver).mouseOver(driver);
				Actions builder = new Actions(driver);
			    builder.sendKeys(Keys.ENTER).perform();
			    hardWait(5);
			} 
		} catch(Exception e) {
			Assert.fail("Error  in login " + e.getMessage());
		}
	}

	public void waitForFileUpload(WebDriver driver, String fileName, int wait) {
		for (int i = 0; i< wait; i++) {
			try {
				if (isExist(driver, fileName)) {
					break;
				}
			} catch (Exception e) {
				Logger.info("The error " + e.getMessage());
				hardWait(1);
			}
		}
	}
	
	/**
	 * doubleClick is used to double click element
	 * @param driver
	 * @param element
	 */
	private void doubleClick(WebDriver driver, WebElement element) {
		hardWait(5);
		Actions action = new Actions(driver);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
		hardWait(5);
		action.moveToElement(element).doubleClick().build().perform();
	}
	
	public void gmailHome(WebDriver driver, SuiteData suiteData) {
		driver.get(suiteData.getSaasAppBaseUrl());
		hardWait(ACTIVITY_AND_DELAY);
	}

}
