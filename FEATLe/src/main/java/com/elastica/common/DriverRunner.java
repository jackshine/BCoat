package com.elastica.common;


import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.elastica.constants.FrameworkConstants;
import com.elastica.dataHandler.FileHandlingUtils;
import com.elastica.driver.Driver;
import com.elastica.driver.DriverType;
import com.elastica.driver.TestConfig;
import com.elastica.logger.Logger;
import com.saucelabs.saucerest.SauceREST;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;

/**
 * Common super class for driver running 
 * @author Eldo Rajan
 *
 */
public class DriverRunner extends Driver{


	DriverType mode=null;String browser=null;String baseUrl=null;String hubUrl=null;
	BrowserMobProxy server=null; Proxy proxy=null;
	String proxyUrl=null;String proxyExtension=null;String proxyExtensionVersion=null;
	String sslCertificate=null;
	String exportHar=null;String exportHarFolder=null;
	String downloadDir=null;String firefoxProfile=null;String env=null;

	// Separate driver instance for each thread
	protected ThreadLocal<WebDriver> localdriver = new ThreadLocal<WebDriver>();
	protected ThreadLocal<RemoteWebDriver> remotedriver = new ThreadLocal<RemoteWebDriver>();
	protected ThreadLocal<String> sessionId = new ThreadLocal<String>();
	TestConfig testconfig = null;   

	/**
	 * Before Method instantiations
	 * @param theTestContext
	 * @param theTestResult
	 */

	public synchronized void startBrowser(ITestContext theTestContext) {
		testconfig = new TestConfig();
		String suiteName = theTestContext.getSuite().getXmlSuite().getName();
		Map<String, String> config = FileHandlingUtils.readPropertyFile(FrameworkConstants.FRAMEWORK_CONFIGURATION_FILEPATH);
		Map<String, String> dbConfig = FileHandlingUtils.readPropertyFile(FrameworkConstants.FRAMEWORK_DBCREDENTIAL_FILEPATH);

		try {

			browser = System.getProperty("browser");
			mode = DriverType.getDriverType(System.getProperty("mode"));
			env=System.getProperty("env");
			if (env==null)
				env=theTestContext.getCurrentXmlTest().getParameter("env");
			
			baseUrl = config.get(env.concat(".baseUrl"));
			hubUrl = System.getProperty("huburl");
			
			if(baseUrl==null){
				baseUrl = System.getProperty("baseurl");	
			}
			
			if(browser==null){
				browser = theTestContext.getCurrentXmlTest().getParameter("browser");            	
			}
			if(mode==null){
				mode = DriverType.getDriverType(theTestContext.getCurrentXmlTest().getParameter("mode"));
			}
			if(baseUrl==null){
				baseUrl = theTestContext.getCurrentXmlTest().getParameter("baseurl");	
			}
			if(hubUrl==null){
				hubUrl = theTestContext.getCurrentXmlTest().getParameter("huburl");	
			} 
			try{
				if(theTestContext.getCurrentXmlTest().getParameter("proxyExtension")!=null){
					if(proxyUrl==null){
						if(theTestContext.getCurrentXmlTest().getParameter("proxyUrl")==null){
							proxyUrl = dbConfig.get(env.toLowerCase().concat(".").concat(theTestContext.getCurrentXmlTest().getParameter("tenantDomainName")));	
						}
						else {
							proxyUrl = theTestContext.getCurrentXmlTest().getParameter("proxyUrl");	
						
						}
						if (!(proxyUrl.contains("https://")||proxyUrl.contains("http://"))){
							try{
								URL resourceUrl = getClass().getResource(proxyUrl);
								Path resourcePath = Paths.get(resourceUrl.toURI());
								proxyUrl = resourcePath.toString();
							} catch (Exception e) {}
						}
					}
				}
			} catch (Exception e) {}

			try{
				if(proxyExtension==null){
					proxyExtension = dbConfig.get(browser.toLowerCase().concat(".").concat("proxyExtension"));
					//proxyExtension = theTestContext.getCurrentXmlTest().getParameter("proxyExtension");
					try{
						URL resourceUrl = getClass().getResource(proxyExtension);
						Path resourcePath = Paths.get(resourceUrl.toURI());
						proxyExtension = resourcePath.toString();
					} catch (Exception e) {}
				}
			} catch (Exception e) {}

			try{
				if(proxyExtensionVersion==null){
					proxyExtensionVersion = dbConfig.get(browser.toLowerCase().concat(".").concat("proxyExtensionVersion"));
					//proxyExtensionVersion = theTestContext.getCurrentXmlTest().getParameter("proxyExtensionVersion");
				}
			} catch (Exception e) {}
			
			try{
				if(sslCertificate==null){
					sslCertificate = theTestContext.getCurrentXmlTest().getParameter("sslCertificate");
					if(sslCertificate==null){
						sslCertificate="true";
					}
				}
			} catch (Exception e) {sslCertificate="true";}
			
			try{
				if(downloadDir==null){
					downloadDir = theTestContext.getCurrentXmlTest().getParameter("downloadDir");
				}
			} catch (Exception e) {}
			
			try{
				if(exportHar==null){
					exportHar = theTestContext.getCurrentXmlTest().getParameter("exportHar");
				}
			} catch (Exception e) {}
			
			try{
				if(exportHarFolder==null){
					exportHarFolder = theTestContext.getCurrentXmlTest().getParameter("exportHarFolder");
				}
			} catch (Exception e) {}
			
			try{
				if(firefoxProfile==null){
					firefoxProfile = theTestContext.getCurrentXmlTest().getParameter("firefoxProfile");
				}
			} catch (Exception e) {}
			
			if(browser!=null){
				testconfig.setBrowser(browser);
			}
			if(mode!=null){
				testconfig.setMode(mode);
			}
			if(baseUrl!=null){
				testconfig.setBaseUrl(baseUrl);
			}
			if(hubUrl!=null){
				testconfig.setHuburl(hubUrl);
			}
			if(proxyExtension!=null){
				testconfig.setProxyExtension(proxyExtension);
			}
			if(downloadDir!=null){
				testconfig.setDownloadDir(System.getProperty("user.dir")+File.separator+downloadDir);
				downloadDir = System.getProperty("user.dir")+File.separator+downloadDir;
			}else{
				testconfig.setDownloadDir(System.getProperty("user.home")+File.separator+"Downloads");
				downloadDir = System.getProperty("user.home")+File.separator+"Downloads";
			}


			String ieDriver = testconfig.getIEDriver();
			String chromeDriver = testconfig.getChromeDriver();
			String ghostDriver = testconfig.getGhostDriver();
			String bmpflag = testconfig.getBmpFlag();

			browser = testconfig.getBrowser();
			mode = testconfig.getMode();
			hubUrl = testconfig.getHuburl();
			baseUrl = testconfig.getBaseUrl();

			Map<String,String> browserCapabilities = new HashMap<String,String >();
			browserCapabilities.put("browserType", browser);
			browserCapabilities.put("ieDriver", ieDriver);
			browserCapabilities.put("chromeDriver", chromeDriver);
			browserCapabilities.put("ghostDriver", ghostDriver);
			browserCapabilities.put("browsermobFlag", bmpflag);
			browserCapabilities.put("host", hubUrl);
			browserCapabilities.put("suiteName", suiteName);

			browserCapabilities.put("proxyUrl", proxyUrl);
			browserCapabilities.put("proxyExtension", proxyExtension);
			browserCapabilities.put("proxyExtensionVersion", proxyExtensionVersion);
			browserCapabilities.put("sslCertificate", sslCertificate);
			browserCapabilities.put("exportHar", exportHar);
			browserCapabilities.put("exportHarFolder", exportHarFolder);
			browserCapabilities.put("downloadDir", downloadDir);
			browserCapabilities.put("firefoxProfile", firefoxProfile);
			Logger.info("Browser configuration settings...");
			Logger.info("**************************************************************************");
			switch (mode) {
			case Local: {            
				Logger.info("No browser capabilities to be set explicitly");
				break;
			}
			case Grid: {
				Logger.info("No browser capabilities to be set explicitly");
				break;
			}
			case Cloud: {
				Logger.info("Browser capabilities to be set explicitly for sauce labs run");
				browserCapabilities.put("platform", theTestContext.getCurrentXmlTest().getParameter("platform"));
				browserCapabilities.put("browserVersion", theTestContext.getCurrentXmlTest().getParameter("browserVersion"));
				browserCapabilities.put("browserName", theTestContext.getCurrentXmlTest().getParameter("browserName"));
				break;
			}
			default: {
				Logger.info("Mode not defined");
				break;
			}
			}

			if(Boolean.parseBoolean(bmpflag)){
				Logger.info("Starting browsermobproxy server");

				server = new BrowserMobProxyServer();
				server.start(0);

				proxy = ClientUtil.createSeleniumProxy(server);
				browserCapabilities.put("proxy", proxy.toString());

				Logger.info("Started browsermobproxy server");
			}

			switch (mode) {
			case Local: {            
				localdriver.set(browserProfileConfiguration(browserCapabilities));
				break;
			}
			case Grid: {
				remotedriver.set(browserProfileConfigurationRemote(browserCapabilities));
				break;
			}
			case Cloud: {
				remotedriver.set(browserProfileConfigurationCloud(browserCapabilities));
				sessionId.set(((RemoteWebDriver) getWebDriver()).getSessionId().toString());
				break;
			}

			default:           
				try {
					Logger.error(
							"Fail to intialize the driver, please check mode parameter.");
					throw new Exception(
							"Fail to intialize the driver, please check mode parameter.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.getLocalizedMessage();
				}
			}

			getWebDriver().get(baseUrl);
			getWebDriver().manage().window().maximize();
			getWebDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);   
			
			String envName = (String) theTestContext.getAttribute("EnvironmentName");
			if(!(envName.equalsIgnoreCase("prod")||envName.equalsIgnoreCase("eu"))){
				Cookie cookie = new Cookie("mf_authenticated",(String) theTestContext.getAttribute("authToken"));
				getWebDriver().manage().addCookie(cookie);
			}
			
			
		} catch (Exception e) {
			e.getLocalizedMessage();
			/*throw new RuntimeException("Exception during browser startup: ",
                    e);*/
		}


	}

	/**
	 * After method disembodifying 
	 */
	public synchronized void stopBrowser() {
		Logger.info("Closing the browser session after the method gets executed in progress");
		if (localdriver.get() != null) {
			getWebDriver().quit();
			localdriver.remove();
		}else if (remotedriver.get() != null) {
			getWebDriver().quit();
			remotedriver.remove();
			switch (mode) {
			case Local: {            
				break;
			}
			case Grid: {
				break;
			}
			case Cloud: {
				ITestResult testStatus = Reporter.getCurrentTestResult();
				SauceREST client = new SauceREST(hubUrl.split(":")[0],hubUrl.split(":")[1]);

				if (testStatus.isSuccess()) {
					client.jobPassed(getSessionId());
				} else {
					client.jobFailed(getSessionId());
				}

				break;
			}
			default:           
				break;
			}
		}else{
			getWebDriver().quit();
		}

		if(server!=null){
			Logger.info("Stopping browsermobproxy server");
			server.stop();
			Logger.info("Stopped browsermobproxy server");
		}

		Logger.info("Closed the browser session after the method gets executed");
	}

	/**
	 * gets webdriver instance   
	 * @return
	 */
	public WebDriver getWebDriver(){
		WebDriver driver=null;
		switch (mode) {
		case Local: {            
			driver = localdriver.get();
			break;
		}
		case Grid: {
			driver = remotedriver.get();
			break;
		}
		case Cloud: {
			driver = remotedriver.get();
			break;
		}

		default:           
			try {
				Logger.error(
						"Fail to intialize the driver, please check mode parameter.");
				throw new Exception(
						"Fail to intialize the driver, please check mode parameter.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.getLocalizedMessage();
			}
		}

		return driver;

	}

	/**
	 * gets session id   
	 * @return
	 */
	public String getSessionId(){
		String jobId=null;
		try {
			jobId = sessionId.get();	

		} catch (Exception e) {
			e.getLocalizedMessage();
			Logger.error(
					"Fail to intialize the session, please check session parameter.");
		}
		return jobId;

	}

	/**
	 * gets browsermob proxy server
	 * @return
	 */
	public BrowserMobProxy getServer(){
		BrowserMobProxy server=null;
		try {
			server = this.server;
		} catch (Exception e) {
			e.getLocalizedMessage();
			Logger.error(
					"Fail to intialize the server, please check server parameter.");
		}
		return server;

	}
	
}
