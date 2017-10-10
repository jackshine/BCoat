package com.elastica.beatle.tests.infra;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
//private PhantomJSDriver driver;
public class BoxSeleniumTest {
	private final static CloseableHttpClient client = HttpClientBuilder.create().build();
	public static void main(String[] args) throws Exception {
		// precondition : saas app grant should done from BOP
		String referer=UIConstants.EOE_url;
		getboxActivated( referer,UIConstants.EOE_USER_ID,UIConstants.EOE_PASSWORD,UIConstants.BOX_USER_ID,UIConstants.BOX_PWD) ;
		//removBoxActivation(referer); //workes with firefox
		//getGoogleAppsActivated( referer,UIConstants.EOE_USER_ID,UIConstants.EOE_PASSWORD,UIConstants.BOX_USER_ID,UIConstants.BOX_PWD) ;
	//	removGoogleAppActivation(referer);
	//	getDropBoxActivated( referer,"admin@securletbeatle.com","Elastica1234!","admin@securletbeatle.com","ZG4nfWZ&@jHp54fV781B") ;
	//	removAppActivation(referer,UIConstants.DROPBOX_URL,"admin@securletbeatle.com","Elastica1234!");
	}

	public  static void getboxActivated(String referer,String socId,String socPassword,String boxId,String boxPwd) throws Exception {
		
		WebDriver driver = null;
		//driver = new FirefoxDriver(DesiredCapabilities.firefox());
		try
		{
			DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
			//capability.setJavascriptEnabled(true);
			capability.setBrowserName("htmlunit");
			capability.setVersion("internet explorer");
			capability.setPlatform(org.openqa.selenium.Platform.ANY);
			driver = new HtmlUnitDriver(capability);
			 
			driver.get(referer+UIConstants.SOC_EOE_URL);
			Thread.sleep(10000);
			Reporter.log(" window title : "+driver.getTitle(),true);
			WebElement login = driver.findElement(By.name(UIConstants.SOC_USER));//(By.cssSelector(".form-control.input-lg.ng-invalid.ng-invalid-required.ng-valid-pattern.ng-dirty"));
			login.sendKeys(socId);
			WebElement pwd = driver.findElement(By.name(UIConstants.SOC_PASSWORD));
			pwd.sendKeys(socPassword);
			WebElement loginbutton = driver.findElement(By.cssSelector(UIConstants.SOC_SIGNIN_LOC));
			loginbutton.click();
			System.out.println(" after login to soc");
			Thread.sleep(10000);
			// Sleep until the div we want is visible or 5 seconds is over
			driver.get(referer+UIConstants.BOX_APP_URL);
			Thread.sleep(25000);
			// check for box securlet and activate it.
	        WebElement activate=driver.findElement(By.cssSelector(UIConstants.EOE_ACTIVATE_LOC));
	        activate.click(); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", activate);
	        System.out.println(" click on activate button");
	         // driver.get("https://eoe.elastica-inc.com/static/ng/appStore/index.html#/configurepolicy/box");
	        Thread.sleep(30000);
	        System.out.println("current url:  "+driver.getCurrentUrl());
	        System.out.println(" select  full scan :");  
			List<WebElement> radios=driver.findElements(By.cssSelector("input"));
			System.out.println(radios.size());
			radios.get(0).click();
			//	select full scan of all users and folders
			Thread.sleep(20000);
			//activate securlate
			System.out.println("click on activate securlate");
			WebElement ActivateSeculet=driver.findElement(By.cssSelector(UIConstants.BOX_SECURLET_ACTIVATE_LOC));
			ActivateSeculet.click();
			Thread.sleep(10000);
			//loging to box and activate
			driver.getCurrentUrl();
			System.out.println("get box url: "+driver.getTitle());

			// login to authenticate box admin
			WebElement boxLogin = driver.findElement(By.id(UIConstants.BOX_USER_LOC));
			boxLogin.sendKeys(boxId);
			WebElement boxPassword = driver.findElement(By.id(UIConstants.BOX_PWD_LOC));
			boxPassword.sendKeys(boxPwd);
			WebElement boxLoginButton = driver.findElement(By.name(UIConstants.BOX_SIGNIN_LOC));
			boxLoginButton.click();
			Reporter.log(" Saas app grant activation ",true);
			Thread.sleep(5000);
			// here admin can grant or deny
			// going for grant access
			
			WebElement grantBoxAccess = driver.findElement(By.id(UIConstants.BOX_GRANT_LOC));
			grantBoxAccess.click();
			Thread.sleep(5000);
			Reporter.log(" box is activated",true);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			driver.quit();
		}
	}
	public  static void removBoxActivation(String referer) throws InterruptedException
	{

		DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
		//capability.setJavascriptEnabled(true);
		capability.setBrowserName("htmlunit");
		capability.setVersion("internet explorer");
		capability.setPlatform(org.openqa.selenium.Platform.ANY);
		WebDriver driver = new HtmlUnitDriver(capability);
	//	WebDriver driver = new FirefoxDriver(DesiredCapabilities.firefox());
		driver.get(referer+UIConstants.SOC_EOE_URL);
		Thread.sleep(10000);
		// Enter the query string "Cheese"
		Reporter.log("get driver"+driver.getTitle(),true);
		// Thread.sleep(5000);
		WebElement login = driver.findElement(By.name(UIConstants.SOC_USER));
		login.sendKeys(UIConstants.EOE_USER_ID);
		//  Thread.sleep(5000);
		WebElement pwd = driver.findElement(By.name(UIConstants.SOC_PASSWORD));
		pwd.sendKeys(UIConstants.EOE_PASSWORD);
		WebElement loginbutton = driver.findElement(By.cssSelector(UIConstants.SOC_SIGNIN_LOC));
		loginbutton.click();
		System.out.println(" after login to soc");
		Thread.sleep(10000);
		// Sleep 10 sec for  the app page loads
		driver.get(referer+UIConstants.BOX_APP_URL);
		Thread.sleep(20000);
		WebElement deactivate=driver.findElement(By.cssSelector("span>div:nth-child(2)>a"));
        deactivate.click(); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deactivate);
        Thread.sleep(10000);
        String subWindowHandler = null;

        Set<String> handles = driver.getWindowHandles(); // get all window handles
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()){
             subWindowHandler = iterator.next();
         	}
         	driver.switchTo().window(subWindowHandler);       	
         	Thread.sleep(10000);
         	WebElement popUp=driver.findElement(By.cssSelector(".modal-footer .btn-primary"));
         	System.out.println("new window displayed: "+popUp.isDisplayed());
         	popUp.click();
         	Thread.sleep(5000);
         	Reporter.log(" box is deactivated is completed",true);
	}
	
	
	
	public  static void getGoogleAppsActivated(String referer,String socId,String socPassword,String boxId,String boxPwd) throws Exception {
		
		WebDriver driver = null;
		//driver = new FirefoxDriver(DesiredCapabilities.firefox());
		try
		{
			DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
			//capability.setJavascriptEnabled(true);
			capability.setBrowserName("htmlunit");
			capability.setVersion("internet explorer");
			capability.setPlatform(org.openqa.selenium.Platform.ANY);
			driver = new HtmlUnitDriver(capability);
			 
			driver.get(referer+UIConstants.SOC_EOE_URL);
			Thread.sleep(10000);
			Reporter.log(" window title : "+driver.getTitle(),true);
			WebElement login = driver.findElement(By.name(UIConstants.SOC_USER));//(By.cssSelector(".form-control.input-lg.ng-invalid.ng-invalid-required.ng-valid-pattern.ng-dirty"));
			login.sendKeys(socId);
			WebElement pwd = driver.findElement(By.name(UIConstants.SOC_PASSWORD));
			pwd.sendKeys(socPassword);
			WebElement loginbutton = driver.findElement(By.cssSelector(UIConstants.SOC_SIGNIN_LOC));
			loginbutton.click();
			System.out.println(" after login to soc");
			Thread.sleep(10000);
			// Sleep until the div we want is visible or 5 seconds is over
			driver.get(referer+UIConstants.GOOGLE_APP_URL);
			Thread.sleep(25000);
			// check for box securlet and activate it.
	        WebElement activate=driver.findElement(By.cssSelector(UIConstants.EOE_ACTIVATE_LOC));
	        activate.click(); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", activate);
	        System.out.println(" click on activate button");
	         // driver.get("https://eoe.elastica-inc.com/static/ng/appStore/index.html#/configurepolicy/box");
	        Thread.sleep(20000);
	        System.out.println("current url:  "+driver.getCurrentUrl());
	        System.out.println(" select  full scan :");  
			List<WebElement> radios=driver.findElements(By.cssSelector("input"));
			System.out.println(radios.size());
			radios.get(0).click();
			//	select full scan of all users and folders
			Thread.sleep(20000);
			//activate securlate
			System.out.println("click on activate securlate");
			WebElement ActivateSeculet=driver.findElement(By.cssSelector(UIConstants.BOX_SECURLET_ACTIVATE_LOC));
			ActivateSeculet.click();
			Thread.sleep(10000);
			// save the google setting
			WebElement ActivateGoogle=driver.findElement(By.cssSelector(UIConstants.BOX_SECURLET_ACTIVATE_LOC));
			ActivateGoogle.click();
			//.btn.btn-success.ng-scope
			//loging to box and activate
			// go to app install
			System.out.println("get current url: "+driver.getCurrentUrl());
			Thread.sleep(20000);
			//boolean status=driver.findElement(By.cssSelector("#___additnow_0")).isDisplayed();
			
		//	System.out.println("get visibility status : "+status);
			 String mainwindow=driver.getWindowHandle();
			WebElement installAppFrame=driver.findElement(By.cssSelector(UIConstants.GOOGLE_APP_INSTALL));//cssSelector("#aincontent"));
			driver = driver.switchTo().frame(installAppFrame);
			Thread.sleep(2000);
			driver.findElement(By.cssSelector(UIConstants.GOOGLE_APP_INSTALL_BUTTON)).click();;
			//driver.getCurrentUrl();
			System.out.println("get box url: "+driver.getCurrentUrl());
			for(String winHandle :driver.getWindowHandles()){
                driver.switchTo().window(winHandle);
                
                {
                    System.out.println("Title of the page after - switchingTo: " + driver.getTitle());
                }
            }
			// 
			Thread.sleep(10000);
			WebElement boxLogin = driver.findElement(By.id(UIConstants.GOOGLE_APP_USERNAME_LOC));
			boxLogin.sendKeys(boxId);
			WebElement boxPassword = driver.findElement(By.id(UIConstants.GOOGLE_APP_NEXT_LOC));
			boxPassword.click();
			//.sendKeys(boxPwd);
			Thread.sleep(10000);
			WebElement boxLoginButton = driver.findElement(By.name(UIConstants.GOOGLE_APP_PWD_LOC));
			boxLoginButton.sendKeys(boxPwd);
			Thread.sleep(2000);
			WebElement signIn=driver.findElement(By.id(UIConstants.GOOGLE_APP_SIGNIN_LOC));
		//	signIn.click();
			System.out.println("isEnabled : "+signIn.isEnabled());
			System.out.println("get text : "+signIn.isDisplayed());
			//Reporter.log(" Saas app grant activation ",true);
			Thread.sleep(5000);
			signIn.click();
			Thread.sleep(10000);
			System.out.println("CheckBoxCondition : "+driver.findElement(By.cssSelector(".INB4VEC-g-c >div")).isDisplayed());
			WebElement CheckBoxCondition = driver.findElement(By.cssSelector(UIConstants.GOOGLE_APP_CHKBOX_LOC));
			System.out.println("CheckBoxCondition : "+CheckBoxCondition.isEnabled());
			CheckBoxCondition.click();
			// here admin can grant or deny
			// going for grant access
			WebElement grantBoxAccess = driver.findElement(By.cssSelector(UIConstants.GOOGLE_APP_SAVE_LOC));
			grantBoxAccess.click();
			Thread.sleep(5000);
			Reporter.log(" Google App is activated",true);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			driver.quit();
		}
	}
	
public  static void getDropBoxActivated(String referer,String socId,String socPassword,String boxId,String boxPwd) throws Exception {
		
		WebDriver driver = null;
		driver = new FirefoxDriver(DesiredCapabilities.firefox());
		try
		{
			DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
			//capability.setJavascriptEnabled(true);
			capability.setBrowserName("htmlunit");
			capability.setVersion("internet explorer");
			capability.setPlatform(org.openqa.selenium.Platform.ANY);
			//driver = new HtmlUnitDriver(capability);
			 
			driver.get(referer+UIConstants.SOC_EOE_URL);
			Thread.sleep(10000);
			Reporter.log(" window title : "+driver.getTitle(),true);
			WebElement login = driver.findElement(By.name(UIConstants.SOC_USER));//(By.cssSelector(".form-control.input-lg.ng-invalid.ng-invalid-required.ng-valid-pattern.ng-dirty"));
			login.sendKeys(socId);
			WebElement pwd = driver.findElement(By.name(UIConstants.SOC_PASSWORD));
			pwd.sendKeys(socPassword);
			WebElement loginbutton = driver.findElement(By.cssSelector(UIConstants.SOC_SIGNIN_LOC));
			loginbutton.click();
			System.out.println(" after login to soc");
			Thread.sleep(10000);
			// Sleep until the div we want is visible or 5 seconds is over
			driver.get(referer+UIConstants.DROPBOX_URL);
			https://eoe.elastica-inc.com/static/ng/appStore/index.html#/appdetails/dropbox
			Thread.sleep(25000);
			// check for box securlet and activate it.
	        WebElement activate=driver.findElement(By.cssSelector(UIConstants.EOE_ACTIVATE_LOC));
	        activate.click(); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", activate);
	        System.out.println(" click on activate button");
	         // driver.get("https://eoe.elastica-inc.com/static/ng/appStore/index.html#/configurepolicy/box");
	        Thread.sleep(20000);
	        System.out.println("current url:  "+driver.getCurrentUrl());
	        System.out.println(" select  full scan :");  
			List<WebElement> radios=driver.findElements(By.cssSelector("input"));
			System.out.println(radios.size());
			radios.get(0).click();
			//	select full scan of all users and folders
			Thread.sleep(20000);
			//activate securlate
			System.out.println("click on activate securlate");
			WebElement ActivateSeculet=driver.findElement(By.cssSelector(UIConstants.BOX_SECURLET_ACTIVATE_LOC));
			ActivateSeculet.click();
			Thread.sleep(10000);
			Thread.sleep(5000);
			WebElement boxLogin = driver.findElement(By.cssSelector(".credentials-form__fields .login-email .text-input-input"));//.name("login_email"));
			System.out.println("isEnabled : "+boxLogin.isEnabled());
			System.out.println("get text : "+boxLogin.isDisplayed());
			boxLogin.sendKeys(boxId);
			WebElement boxPassword = driver.findElement(By.cssSelector(".password-input"));//.cssSelector(".login-password"));//.name("login_password"));
			System.out.println("isEnabled : "+boxPassword.isEnabled());
			System.out.println("get text : "+boxPassword.isDisplayed());
			boxPassword.sendKeys(boxPwd);
			//.sendKeys(boxPwd);
			Thread.sleep(10000);
			WebElement boxLoginButton = driver.findElement(By.cssSelector(".sign-in-text"));
			boxLoginButton.click();
			Thread.sleep(2000);
			Thread.sleep(15000);
			Reporter.log(" DropBox App is activated",true);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			driver.quit();
		}
	}
	public  static void removGoogleAppActivation(String referer) throws InterruptedException
	{

		DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
		//capability.setJavascriptEnabled(true);
		capability.setBrowserName("htmlunit");
		capability.setVersion("internet explorer");
		capability.setPlatform(org.openqa.selenium.Platform.ANY);
		WebDriver driver = new HtmlUnitDriver(capability);
	//	WebDriver driver = new FirefoxDriver(DesiredCapabilities.firefox());
		driver.get(referer+UIConstants.SOC_EOE_URL);
		Thread.sleep(10000);
		// Enter the query string "Cheese"
		Reporter.log("get driver"+driver.getTitle(),true);
		// Thread.sleep(5000);
		WebElement login = driver.findElement(By.name(UIConstants.SOC_USER));
		login.sendKeys(UIConstants.EOE_USER_ID);
		//  Thread.sleep(5000);
		WebElement pwd = driver.findElement(By.name(UIConstants.SOC_PASSWORD));
		pwd.sendKeys(UIConstants.EOE_PASSWORD);
		WebElement loginbutton = driver.findElement(By.cssSelector(UIConstants.SOC_SIGNIN_LOC));
		loginbutton.click();
		System.out.println(" after login to soc");
		Thread.sleep(10000);
		// Sleep 10 sec for  the app page loads
		driver.get(referer+UIConstants.GOOGLE_APP_URL);
		Thread.sleep(20000);
		WebElement deactivate=driver.findElement(By.cssSelector("span>div:nth-child(2)>a"));
        deactivate.click(); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deactivate);
        Thread.sleep(10000);
        String subWindowHandler = null;

        Set<String> handles = driver.getWindowHandles(); // get all window handles
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()){
             subWindowHandler = iterator.next();
         	}
         	driver.switchTo().window(subWindowHandler);       	
         	Thread.sleep(10000);
         	WebElement popUp=driver.findElement(By.cssSelector(".modal-footer .btn-primary"));
         	System.out.println("new window displayed: "+popUp.isDisplayed());
         	popUp.click();
         	Thread.sleep(5000);
         	Reporter.log(" box is deactivated is completed",true);
	}

	public  static void removAppActivation(String referer,String AppUrl,String Email ,String Pwd) throws InterruptedException
	{

		DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
		//capability.setJavascriptEnabled(true);
		capability.setBrowserName("htmlunit");
		capability.setVersion("internet explorer");
		capability.setPlatform(org.openqa.selenium.Platform.ANY);
		WebDriver driver = new HtmlUnitDriver(capability);
	//	WebDriver driver = new FirefoxDriver(DesiredCapabilities.firefox());
		driver.get(referer+UIConstants.SOC_EOE_URL);
		Thread.sleep(10000);
		// Enter the query string "Cheese"
		Reporter.log("get driver"+driver.getTitle(),true);
		// Thread.sleep(5000);
		WebElement login = driver.findElement(By.name(UIConstants.SOC_USER));
		login.sendKeys(Email);
		//  Thread.sleep(5000);
		WebElement pwd = driver.findElement(By.name(UIConstants.SOC_PASSWORD));
		pwd.sendKeys(Pwd);
		WebElement loginbutton = driver.findElement(By.cssSelector(UIConstants.SOC_SIGNIN_LOC));
		loginbutton.click();
		System.out.println(" after login to soc");
		Thread.sleep(10000);
		// Sleep 10 sec for  the app page loads
		driver.get(referer+AppUrl);
		Thread.sleep(20000);
		WebElement deactivate=driver.findElement(By.cssSelector("span>div:nth-child(2)>a"));
        deactivate.click(); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deactivate);
        Thread.sleep(10000);
        String subWindowHandler = null;

        Set<String> handles = driver.getWindowHandles(); // get all window handles
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()){
             subWindowHandler = iterator.next();
         	}
         	driver.switchTo().window(subWindowHandler);       	
         	Thread.sleep(10000);
         	WebElement popUp=driver.findElement(By.cssSelector(".modal-footer .btn-primary"));
         	System.out.println("new window displayed: "+popUp.isDisplayed());
         	popUp.click();
         	Thread.sleep(5000);
         	Reporter.log(" DropBox is deactivated is completed",true);
	}

	public static HttpResponse  doGetRest(String url,Map<String,String> headers) throws IOException{
		System.out.println("** Get Rest Call**");
		HttpGet request = new HttpGet(url);

		for (Map.Entry<String, String> entrySet : headers.entrySet()) {
			String key = entrySet.getKey();
			String value = entrySet.getValue();
			request.setHeader(key,value);
		}
		HttpResponse response = client.execute(request);
		return response;
	}
	
}