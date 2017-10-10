package com.elastica.beatle.tests.audit;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.logger.Logger;

public class AuditIntegrationTests extends CommonTest{
	
		SoftAssert softAssert = null;
	
		@BeforeMethod(groups ={"sanity"})
		public void testData(Method method, Object[] testData) {
			softAssert = new SoftAssert();
		}
		
		
		@Test(groups = { "sanity" })
		public void testAuditPageSingleSignOn(ITestContext context) {
			Logger.info("Starting audit SSO test via scansafe");
			
			String username = suiteData.getUsername();String password = suiteData.getPassword();
			String scanSafeUrl = context.getCurrentXmlTest().getParameter("loginScanSafeUrl");
			String auditUrl = context.getCurrentXmlTest().getParameter("auditUrl");
			
			Logger.info("****************Test Case Description****************");
			Logger.info("Test Case Description: Login to Audit page via scan safe and verify audit compoenets are loading or not");
			Logger.info("*****************************************************");
			Logger.info("****************Input****************");
			Logger.info("UserName:"+username+"\t"+"Password:"+password);
			Logger.info("Scansafe Url:"+scanSafeUrl);
			Logger.info("*****************************************************");
			Logger.info("****************Expected Output****************");
			Logger.info("Audit Url:"+auditUrl);
			Logger.info("*****************************************************");

			
			
			loginCiscoPage(getWebDriver(), username, password, scanSafeUrl, auditUrl);
			
			softAssert.assertAll();
			
			
			Logger.info("Completed audit SSO test via scansafe successfully");
		}

		private void loginCiscoPage(WebDriver driver, String username, String password, String loginUrl, String auditUrl) {
			driver.get(loginUrl);
			try {Thread.sleep(10000);} catch (Exception e) {}
			
			if(driver.getTitle().trim().equalsIgnoreCase("Cisco")){
				Logger.info("Login into cisco page is successful");
			}else{
				try {
					WebElement ciscoUsername = driver.findElement(By.cssSelector(".userName"));
					WebElement ciscoPassword = driver.findElement(By.cssSelector(".password"));
					WebElement ciscoLoginButton = driver.findElement(By.id("submit"));
					
					
					softAssert.assertTrue(ciscoUsername.isDisplayed(), "Cisco Page:Username text box is not visible");
					softAssert.assertTrue(ciscoPassword.isDisplayed(), "Cisco Page:Password text box is not visible");
					softAssert.assertTrue(ciscoLoginButton.isDisplayed(), "Cisco Page:Login button is not visible");
					
					ciscoUsername.clear();ciscoPassword.clear();
					ciscoUsername.sendKeys(username);ciscoPassword.sendKeys(password);
					ciscoLoginButton.click();
					Thread.sleep(30000);
				} catch (Exception ex) {
					Assert.fail("Cisco single sign on login failed:" + ex.getLocalizedMessage());
				}	
				
				try {
					int windowCountBefore = getWindowCount(driver);
					String winHandleBefore = getWindowHandle(driver);
					
					WebElement ciscoRedirectLink = driver.findElement(By.cssSelector(".externalLink"));
					softAssert.assertTrue(ciscoRedirectLink.isDisplayed(), "Cisco Page:Username text box is not visible");
					softAssert.assertEquals(driver.getTitle().trim(),"Cisco", "Cisco Page:Page Title is not matching");
					softAssert.assertEquals(ciscoRedirectLink.getText().trim(),"Launch Cisco Cloud Access Security by Elastica.", "Cisco Page:Audit redirect link text is not matching");
					
					ciscoRedirectLink = driver.findElement(By.cssSelector(".externalLink"));
					ciscoRedirectLink.click();
					try {Thread.sleep(180000);} catch (Exception e) {}
					
					Set<String> winHandlesAfter = getWindowHandles(driver);
					int windowCountAfter = getWindowCount(driver);

					for(String winHandle : winHandlesAfter){
						if(winHandle.equalsIgnoreCase(winHandleBefore)){
							Logger.info("Control is in current window, switching of windows will not be enforced");
						}else{
							Logger.info("Control is in new window, switching of windows will be enforced");
							switchToWindow(driver,winHandle);
							softAssert.assertEquals(getCurrentUrl(driver).trim(),auditUrl, 
									"Audit Page:APP-CEP url is not matching");
							break;
						}
					}

					Assert.assertEquals(windowCountAfter,windowCountBefore+1, "Cisco Page:New window is not getting opened after clicking audit redirect link");

				} catch (Exception ex) {
					Assert.fail("Cisco single sign on switching to new window failed:" + ex.getLocalizedMessage());
				}		
					
				try{
					
					WebElement auditPageHeader = driver.findElement(By.cssSelector("h1"));		
					softAssert.assertTrue(auditPageHeader.isDisplayed(), "Audit-CEP Page:Username text box is not visible");
					softAssert.assertEquals(auditPageHeader.getText().trim(),"Audit", "Audit-CEP Page:Page Title is not matching");
							
				}catch(Exception ex){
					Assert.fail("Audit page validation in new window is failed due to:" + ex.getLocalizedMessage());
				}
				
			}
		}
		
		public void clickOnAuditRedirectLink(WebDriver driver) {
			
			List<WebElement> ciscoRedirectLink = driver.findElements(By.cssSelector(".level2links>a"));
			
			
			/*try {*/
				
				int windowCountBefore = getWindowCount(driver);
				String winHandleBefore = getWindowHandle(driver);

			 //ciscoRedirectLink = driver.findElement(By.cssSelector("#SaaSAuditDashboard>span"));
				
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", ciscoRedirectLink);
				try {Thread.sleep(30000);} catch (Exception e) {}
				
				Set<String> winHandlesAfter = getWindowHandles(driver);
				int windowCountAfter = getWindowCount(driver);

				for(String winHandle : winHandlesAfter){
					if(winHandle.equalsIgnoreCase(winHandleBefore)){
						Logger.info("Control is in current window, switching of windows will not be enforced");
					}else{
						Logger.info("Control is in new window, switching of windows will be enforced");
						switchToWindow(driver,winHandle);Logger.info(getCurrentUrl(driver));
						break;
					}
				}

				Assert.assertEquals(windowCountAfter,windowCountBefore+1, "New window is not getting opened after clicking audit redirect link");

			/*} catch (Exception ex) {
				Assert.fail("Issue with Redirect to Elastica CEP portal from Scan center Operation " + ex.getLocalizedMessage());
			}*/

		}
		
		
		private WebDriver getWebDriver() {
			WebDriver driver = null;
			try
			{
				DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
				capability.setJavascriptEnabled(true);
				capability.setBrowserName("htmlunit");
				capability.setVersion("internet explorer");
				capability.setPlatform(org.openqa.selenium.Platform.ANY);
				driver = new HtmlUnitDriver(capability);
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return driver;
		}
		
		public int getWindowCount(WebDriver driver){
			return driver.getWindowHandles().size();
		}
		
		public String getWindowHandle(WebDriver driver){
			return driver.getWindowHandle();
		}

		public Set<String> getWindowHandles(WebDriver driver){
			return driver.getWindowHandles();
		}
		
		public void switchToWindow(WebDriver driver, String winHandle){
			driver.switchTo().window(winHandle);
		}
		
		public String getCurrentUrl(WebDriver driver){
			return driver.getCurrentUrl();
		}
		
}

