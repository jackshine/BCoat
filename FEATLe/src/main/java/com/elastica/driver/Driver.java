package com.elastica.driver;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.elastica.constants.FrameworkConstants;
import com.elastica.logger.Logger;
import com.opera.core.systems.OperaDriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;



/**
 * Driver Class
 * 
 * @author Eldo Rajan
 *
 */
public class Driver {

	/**
	 * browser profile configuration
	 * 
	 * @param browserCapabilities
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public synchronized WebDriver browserProfileConfiguration(Map<String, String> browserCapabilities) {

		BrowserType browserType = BrowserType.getBrowserType(browserCapabilities.get("browserType"));
		String ieDriver = browserCapabilities.get("ieDriver");
		String chromeDriver = browserCapabilities.get("chromeDriver");
		// String ghostDriver = browserCapabilities.get("ghostDriver");
		// Boolean browsermobFlag =
		// Boolean.parseBoolean(browserCapabilities.get("browsermobFlag"));

		Logger.info("Starting " + browserType + " browser in local configuration");
		WebDriver driver = null;

		switch (browserType) {
		case Firefox: {
			driver = new FirefoxDriver(getDesiredCapabilities(browserCapabilities));
			break;
		}
		case InternetExplorer: {
			System.setProperty("webdriver.ie.driver", ieDriver);
			driver = new InternetExplorerDriver(getDesiredCapabilities(browserCapabilities));
			break;
		}
		case Chrome: {
			if (System.getProperty("os.name").contains("Mac")) {
				System.setProperty("webdriver.chrome.driver", "/usr/local/selenium/chromedriver");
			} else {
				System.setProperty("webdriver.chrome.driver", chromeDriver);
			}
			
			driver = new ChromeDriver(getDesiredCapabilities(browserCapabilities));
			break;
		}
		case Safari: {
			driver = new SafariDriver(getDesiredCapabilities(browserCapabilities));
			break;
		}
		case PhantomJS: {
			driver = new PhantomJSDriver(getDesiredCapabilities(browserCapabilities));
			break;
		}
		case HtmlUnit: {
			driver = new HtmlUnitDriver(getDesiredCapabilities(browserCapabilities));
			break;
		}
		case Opera: {
			System.setProperty("os.name", "windows");
			driver = new OperaDriver(getDesiredCapabilities(browserCapabilities));
			break;
		}
		case Android: {
			try {
				driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),
						getDesiredCapabilities(browserCapabilities));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		}
		case IPhone: {
			
			break;
		}
		
		default: {
			try {
				Logger.error("Fail to start browser, please check browser parameter.");
				throw new Exception("Fail to start browser, please check browser parameter.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		}
		
		Logger.info("Started " + browserType + " browser in local configuration");
		Logger.info("**************************************************************************");
		return driver;
	}

	/**
	 * browser profile configuration for remote
	 * 
	 * @param browserCapabilities
	 * @return
	 */
	public synchronized RemoteWebDriver browserProfileConfigurationRemote(Map<String, String> browserCapabilities) {
		BrowserType browserType = BrowserType.getBrowserType(browserCapabilities.get("browserType"));
		String host = browserCapabilities.get("host");

		Logger.info("Starting " + browserType + " browser in remote configuration");

		RemoteWebDriver driver = null;
		String huburl = "http://" + host + "/wd/hub";

		try {
			driver = new RemoteWebDriver(new URL(huburl), getDesiredCapabilities(browserCapabilities));
		} catch (Exception e) {
			Logger.error("Fail to start browser in remote configuration");
			e.printStackTrace();
		}
		Logger.info("Started " + browserType + " browser in remote configuration");
		return driver;
	}

	/**
	 * browser profile configuration for cloud
	 * 
	 * @param browserCapabilities
	 * @return
	 */
	public synchronized RemoteWebDriver browserProfileConfigurationCloud(Map<String, String> browserCapabilities) {
		BrowserType browserType = BrowserType.getBrowserType(browserCapabilities.get("browserType"));
		String host = browserCapabilities.get("host");

		Logger.info("Starting " + browserType + " browser in cloud configuration");

		RemoteWebDriver driver = null;
		String huburl = "http://" + host + "@ondemand.saucelabs.com:80/wd/hub";

		try {
			driver = new RemoteWebDriver(new URL(huburl), getDesiredCapabilitiesCloud(browserCapabilities));
			driver.setFileDetector(new LocalFileDetector());
		} catch (Exception e) {
			Logger.error("Fail to start browser in cloud configuration");
			e.printStackTrace();
		}
		Logger.info("Started " + browserType + " browser in cloud configuration");
		return driver;
	}

	/**
	 * get desired capabilities
	 * 
	 * @param browserCapabilities
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public synchronized DesiredCapabilities getDesiredCapabilities(Map<String, String> browserCapabilities) {

		BrowserType browserType = BrowserType.getBrowserType(browserCapabilities.get("browserType"));
		String ghostDriver = browserCapabilities.get("ghostDriver");
		Boolean browsermobFlag = Boolean.parseBoolean(browserCapabilities.get("browsermobFlag"));
		Boolean sslCertificate = Boolean.parseBoolean(browserCapabilities.get("sslCertificate"));

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		switch (browserType) {
		case InternetExplorer: {
			desiredCapabilities = DesiredCapabilities.internetExplorer();
			desiredCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
					true);
			desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, sslCertificate);
			desiredCapabilities.setCapability("ignoreProtectedModeSettings", true);
			desiredCapabilities.setCapability(CapabilityType.ENABLE_PERSISTENT_HOVERING, false);
			desiredCapabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, 1);
			desiredCapabilities.setCapability("javascriptEnabled", true);
			desiredCapabilities.setCapability("autoAcceptAlerts", true);
			desiredCapabilities.setCapability("unexpectedAlertBehaviour", UnexpectedAlertBehaviour.DISMISS);
			desiredCapabilities.setCapability("handlesAlerts", "dismissAlerts");
			desiredCapabilities.setCapability("requireWindowFocus", true);
			desiredCapabilities.setCapability("ie.ensureCleanSession", true);
			desiredCapabilities.setBrowserName("internet explorer");
			desiredCapabilities.setVersion("11");
			break;
		}
		case Chrome: {
			desiredCapabilities = DesiredCapabilities.chrome();
			desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, sslCertificate);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			options.addArguments("allow-running-insecure-content");
			options.addArguments("ignore-certificate-errors");
			
			String proxyUrl = browserCapabilities.get("proxyUrl");
			String proxyExtension = browserCapabilities.get("proxyExtension");
			//String proxyExtensionVersion = browserCapabilities.get("proxyExtensionVersion");
			Proxy proxy = new Proxy();
			try {
				if(!proxyUrl.isEmpty()||!proxyUrl.equals(null)){
					proxy.setProxyType(ProxyType.PAC);
					proxy.setProxyAutoconfigUrl(proxyUrl);
					File file = new File(proxyExtension);
					options.addExtensions(file);
				}
			} catch (Exception e) {
				Logger.info("Proxy not to be set");
			}
			desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
			desiredCapabilities.setCapability("elementScrollBehavior", 1);
			
			try {
				if(!proxyUrl.isEmpty()||!proxyUrl.equals(null)){
					desiredCapabilities.setCapability("proxy", proxy);
					desiredCapabilities.setCapability("chrome.switches", 
							Arrays.asList("--proxy-pac-url="+proxyUrl));
				}
			} catch (Exception e) {}
			
			break;
		}
		case Safari: {
			System.setProperty("webdriver.safari.noinstall", "true");
			desiredCapabilities = DesiredCapabilities.safari();
			desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, sslCertificate);
			desiredCapabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, 1);
			desiredCapabilities.setCapability("javascriptEnabled", true);
			desiredCapabilities.setCapability("autoAcceptAlerts", true);
			desiredCapabilities.setCapability("unexpectedAlertBehaviour", UnexpectedAlertBehaviour.DISMISS);
			desiredCapabilities.setCapability("handlesAlerts", "dismissAlerts");
			desiredCapabilities.setCapability("safari.options.dataDir",
					System.getProperty("user.home") + File.separator + "Downloads");
			break;
		}
		case Firefox: {
			boolean pFlag=false;String firefoxProfile = browserCapabilities.get("firefoxProfile");
			try {
				if(!firefoxProfile.isEmpty()||!firefoxProfile.equals(null)){
					Logger.info("Loading existing "+firefoxProfile+" profile with webdriver");
					ProfilesIni allProfiles = new ProfilesIni();
					FirefoxProfile profile = allProfiles.getProfile(firefoxProfile);
					desiredCapabilities = DesiredCapabilities.firefox();
					desiredCapabilities.setCapability("elementScrollBehavior", 1);
					desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);
				}	
			} catch (Exception e) {
				Logger.info("Creating new firefox profile with webdriver");pFlag=true;
			}
			
			if(pFlag){
				FirefoxProfile profile = new FirefoxProfile();
				profile.setAcceptUntrustedCertificates(sslCertificate);
				profile.setAssumeUntrustedCertificateIssuer(false);
				profile.setEnableNativeEvents(true);
				
				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.download.dir", browserCapabilities.get("downloadDir"));
				profile.setPreference("browser.download.manager.showWhenStarting", false);
				profile.setPreference("browser.download.manager.focusWhenStarting", false);
				profile.setPreference("browser.download.useDownloadDir", true);
				profile.setPreference("browser.helperApps.alwaysAsk.force", false);
				profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
				profile.setPreference("browser.download.manager.closeWhenDone", true);
				profile.setPreference("browser.download.manager.showAlertOnComplete", false);
				profile.setPreference("browser.download.manager.useWindow", false);
				profile.setPreference("plugin.disable_full_page_plugin_for_types", "application/pdf");
				profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
				profile.setPreference("pdfjs.disabled", true);
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
						"text/plain,application/zip,application/octet-stream,text/xml,application/pdf,application/vnd.ms-powerpoint,application/octet-stream,application/msword,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.openxmlformats-officedocument.spreadsheetml.template,application/vnd.openxmlformats-officedocument.presentationml.template,application/vnd.openxmlformats-officedocument.presentationml.slideshow,application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.openxmlformats-officedocument.presentationml.slide,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.wordprocessingml.template,application/vnd.ms-excel.addin.macroEnabled.12,application/vnd.ms-excel.sheet.binary.macroEnabled.12,application/vnd.ms-word.document.macroEnabled.12,application/vnd.ms-word.template.macroEnabled.12,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.template,application/vnd.ms-excel.sheet.macroEnabled.12,application/vnd.ms-excel.template.macroEnabled.12,application/vnd.openxmlformats-officedocument.presentationml.template,application/vnd.ms-powerpoint.addin.macroEnabled.12,application/vnd.ms-powerpoint.presentation.macroEnabled.12,application/vnd.ms-powerpoint.template.macroEnabled.12,application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
				profile.setPreference("security.mixed_content.block_active_content", false);
				profile.setPreference("security.mixed_content.block_display_content", true);
				profile.setPreference("capability.policy.default.Window.QueryInterface", "allAccess");
				profile.setPreference("capability.policy.default.Window.frameElement.get", "allAccess");
				profile.setPreference("xpinstall.signatures.required", false);
				String proxyUrl = browserCapabilities.get("proxyUrl");
				String proxyExtension = browserCapabilities.get("proxyExtension");
				String proxyExtensionVersion = browserCapabilities.get("proxyExtensionVersion");
				Proxy proxy = new Proxy();
				try {
					if(!proxyUrl.isEmpty()||!proxyUrl.equals(null)){
						proxy.setProxyType(ProxyType.PAC);
						proxy.setProxyAutoconfigUrl(proxyUrl);
						File file = new File(proxyExtension);
						profile.addExtension(file);
						profile.setPreference("extensions.elastica_auth.currentVersion", proxyExtensionVersion);
					}
				} catch (Exception e) {
					Logger.info("Proxy not to be set");
				}
				

				String exportHar = browserCapabilities.get("exportHar");
				String exportHarFolder = browserCapabilities.get("exportHarFolder"); 
				try {
					if(!exportHar.isEmpty()||!exportHar.equals(null)){
						if(Boolean.parseBoolean(exportHar)){
							try {
								profile.addExtension(new File(FrameworkConstants.fireBugPath));
								profile.addExtension(new File(FrameworkConstants.netExportPath));
								profile.addExtension(new File(FrameworkConstants.fireStarterPath));
								
								profile.setPreference("app.update.enabled", false);
								profile.setPreference("extensions.firebug.currentVersion", "2.0.14b1");
								profile.setPreference("extensions.firebug.DBG_NETEXPORT", true);
								profile.setPreference("extensions.firebug.onByDefault", true);
								profile.setPreference("extensions.firebug.addonBarOpened", true);
								profile.setPreference("extensions.firebug.allPagesActivation", "on");
								profile.setPreference("extensions.firebug.defaultPanelName", "net");
								profile.setPreference("extensions.firebug.net.enableSites", true);
								profile.setPreference("extensions.firebug.net.persistent", false);
								profile.setPreference("extensions.firebug.netexport.alwaysEnableAutoExport", true);
								profile.setPreference("extensions.firebug.netexport.autoExportToFile", true);
								profile.setPreference("extensions.firebug.netexport.autoExportToServer", false);
								try{					    	
									profile.setPreference("extensions.firebug.netexport.defaultLogDir", exportHarFolder);
								} catch (Exception e){
									profile.setPreference("extensions.firebug.netexport.defaultLogDir", System.getProperty("user.dir")+File.separator+"har");
								}

								profile.setPreference("extensions.firebug.net.logLimit",50000);
								profile.setPreference("extensions.firebug.netexport.showPreview", true);
								profile.setPreference("extensions.firebug.netexport.sendToConfirmation", false);
								profile.setPreference("extensions.firebug.netexport.pageLoadedTimeout", 15000);
								profile.setPreference("extensions.firebug.netexport.Automation", true);
								
							} catch (Exception e) {
								throw new RuntimeException("Could not load required extensions, did you download them to the above location? ", e);
							}
							
						}	
					}
				} catch (Exception e) {
					Logger.info("Exporting har is not set");
				}
				
				desiredCapabilities = DesiredCapabilities.firefox();
				desiredCapabilities.setCapability("elementScrollBehavior", 1);
				desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);
				try {
					if(!proxyUrl.isEmpty()||!proxyUrl.equals(null)){
						desiredCapabilities.setCapability("proxy", proxy);
					}
				} catch (Exception e) {}

				profile.setPreference("network.proxy.type", 0);
			}
			
			
			
			break;
		}
		case PhantomJS: {
			desiredCapabilities = DesiredCapabilities.phantomjs();
			ArrayList<String> cliArgsDesiredCapabilities = new ArrayList<String>();
			cliArgsDesiredCapabilities.add("--ssl-protocol=any");
			cliArgsDesiredCapabilities.add("--ignore-ssl-errors=true");
			cliArgsDesiredCapabilities.add("--web-security=false");
			desiredCapabilities.setCapability("takesScreenshot", true);
			desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsDesiredCapabilities);
			desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
					new String[] { "--logLevel=2" });

			if (System.getProperty("os.name").contains("Mac")) {
				desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
						"/usr/local/bin/phantomjs");
			} else {
				desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
						ghostDriver);
			}

			desiredCapabilities.setCapability("phantomjs.page.settings.loadImages", true);
			desiredCapabilities.setCapability("phantomjs.page.settings.localToRemoteUrlAccessEnabled", true);
			desiredCapabilities.setJavascriptEnabled(true);

			break;
		}
		case HtmlUnit: {
			desiredCapabilities= DesiredCapabilities.htmlUnit();
			desiredCapabilities.setJavascriptEnabled(true);
			desiredCapabilities.setBrowserName("htmlunit");
			desiredCapabilities.setVersion("internet explorer");
			desiredCapabilities.setPlatform(org.openqa.selenium.Platform.ANY);
			break;
		}
		case Opera: {
			desiredCapabilities = DesiredCapabilities.opera();
			desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, sslCertificate);
			desiredCapabilities.setCapability("opera.arguments", "-nowin -nomail -fullscreen");
			break;
		}
		case Android: {
			File appDir = new File("src/test/resources/data");
			File app = new File(appDir, "appium.apk");
			desiredCapabilities = DesiredCapabilities.android();
			desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
			desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
			desiredCapabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
			desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 120);
			break;
		}
		default: {
			desiredCapabilities = DesiredCapabilities.firefox();
			desiredCapabilities.setCapability("elementScrollBehavior", 1);
			break;
		}
		}

		if (browsermobFlag) {
			desiredCapabilities.setCapability(CapabilityType.PROXY, browserCapabilities.get("proxy"));
		}

		return desiredCapabilities;
	}

	/**
	 * get desired capabilities for cloud
	 * 
	 * @param browserType
	 * @param suiteName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public synchronized DesiredCapabilities getDesiredCapabilitiesCloud(Map<String, String> browserCapabilities) {

		BrowserType browserType = BrowserType.getBrowserType(browserCapabilities.get("browserType"));
		String suiteName = browserCapabilities.get("suiteName");

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		switch (browserType) {
		case InternetExplorer: {
			desiredCapabilities = DesiredCapabilities.internetExplorer();
			desiredCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
					true);
			desiredCapabilities.setCapability("ignoreProtectedModeSettings", true);
			desiredCapabilities.setCapability(CapabilityType.ENABLE_PERSISTENT_HOVERING, false);
			desiredCapabilities.setCapability("javascriptEnabled", true);
			desiredCapabilities.setCapability("autoAcceptAlerts", true);
			desiredCapabilities.setCapability("unexpectedAlertBehaviour", UnexpectedAlertBehaviour.DISMISS);
			desiredCapabilities.setCapability("handlesAlerts", "dismissAlerts");
			desiredCapabilities.setCapability("requireWindowFocus", true);
			desiredCapabilities.setCapability("ie.ensureCleanSession", true);
			break;
		}	
		case Chrome: {
			desiredCapabilities = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			options.addArguments("allow-running-insecure-content");
			options.addArguments("ignore-certificate-errors");
			// desiredCapabilities.setCapability(ChromeOptions.CAPABILITY,
			// options);
			break;
		}	
		case Safari: {
			desiredCapabilities = DesiredCapabilities.safari();
			desiredCapabilities.setCapability("javascriptEnabled", true);
			desiredCapabilities.setCapability("autoAcceptAlerts", true);
			desiredCapabilities.setCapability("unexpectedAlertBehaviour", UnexpectedAlertBehaviour.DISMISS);
			desiredCapabilities.setCapability("handlesAlerts", "dismissAlerts");
			desiredCapabilities.setCapability("safari.options.dataDir",
					System.getProperty("user.home") + File.separator + "Downloads");
			break;
		}	
		case Firefox: {
			FirefoxProfile profile = new FirefoxProfile();
			profile.setAcceptUntrustedCertificates(true);
			profile.setAssumeUntrustedCertificateIssuer(false);
			profile.setEnableNativeEvents(true);

			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.dir", browserCapabilities.get("downloadDir"));
			profile.setPreference("browser.download.manager.showWhenStarting", false);
			profile.setPreference("browser.download.manager.focusWhenStarting", false);
			profile.setPreference("browser.download.useDownloadDir", true);
			profile.setPreference("browser.helperApps.alwaysAsk.force", false);
			profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
			profile.setPreference("browser.download.manager.closeWhenDone", true);
			profile.setPreference("browser.download.manager.showAlertOnComplete", false);
			profile.setPreference("browser.download.manager.useWindow", false);
			profile.setPreference("plugin.disable_full_page_plugin_for_types", "application/pdf");
			profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
			profile.setPreference("pdfjs.disabled", true);
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
					"text/xml,application/pdf,application/vnd.ms-powerpoint,application/octet-stream,application/msword,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.openxmlformats-officedocument.spreadsheetml.template,application/vnd.openxmlformats-officedocument.presentationml.template,application/vnd.openxmlformats-officedocument.presentationml.slideshow,application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.openxmlformats-officedocument.presentationml.slide,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.wordprocessingml.template,application/vnd.ms-excel.addin.macroEnabled.12,application/vnd.ms-excel.sheet.binary.macroEnabled.12,application/vnd.ms-word.document.macroEnabled.12,application/vnd.ms-word.template.macroEnabled.12,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.template,application/vnd.ms-excel.sheet.macroEnabled.12,application/vnd.ms-excel.template.macroEnabled.12,application/vnd.openxmlformats-officedocument.presentationml.template,application/vnd.ms-powerpoint.addin.macroEnabled.12,application/vnd.ms-powerpoint.presentation.macroEnabled.12,application/vnd.ms-powerpoint.template.macroEnabled.12,application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
			profile.setPreference("security.mixed_content.block_active_content", false);
			profile.setPreference("security.mixed_content.block_display_content", true);
			profile.setPreference("capability.policy.default.Window.QueryInterface", "allAccess");
			profile.setPreference("capability.policy.default.Window.frameElement.get", "allAccess");

			String proxyUrl = browserCapabilities.get("proxyUrl");
			String proxyExtension = browserCapabilities.get("proxyExtension");
			String proxyExtensionVersion = browserCapabilities.get("proxyExtensionVersion");
			Proxy proxy = new Proxy();
			try {
				if(!proxyUrl.isEmpty()||!proxyUrl.equals(null)){
					proxy.setProxyType(ProxyType.PAC);
					proxy.setProxyAutoconfigUrl(proxyUrl);
					File file = new File(proxyExtension);
					profile.addExtension(file);
					profile.setPreference("extensions.elastica_auth.currentVersion", proxyExtensionVersion);
				}
			} catch (Exception e) {
				Logger.info("Proxy not to be set");
			}

			desiredCapabilities = DesiredCapabilities.firefox();
			desiredCapabilities.setCapability("elementScrollBehavior", 1);
			desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);
			try {
				if(!proxyUrl.isEmpty()||!proxyUrl.equals(null)){
					desiredCapabilities.setCapability("proxy", proxy);
				}
			} catch (Exception e) {}

			break;
		}
		default: {
			desiredCapabilities = DesiredCapabilities.chrome();
			break;
		}	
		}

		desiredCapabilities.setCapability(CapabilityType.PLATFORM, browserCapabilities.get("platform"));
		desiredCapabilities.setCapability(CapabilityType.VERSION, browserCapabilities.get("browserVersion"));
		desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, browserCapabilities.get("browserName"));

/*		desiredCapabilities.setCapability("prerun", 
		"{'executable': 'sauce-storage:FileUploadFirefox.exe'}");	
		desiredCapabilities.setCapability("prerun", "sauce-storage: FileUploadFirefox.exe");
*/
		desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		desiredCapabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, 1);
		desiredCapabilities.setCapability("screenResolution", "1024x768");
		desiredCapabilities.setCapability("name", suiteName);
		desiredCapabilities.setCapability("seleniumVersion", "2.48.0");
		desiredCapabilities.setCapability("chromedriverVersion", "2.20");
		desiredCapabilities.setCapability("iedriverVersion", "2.48.0");

		desiredCapabilities.setCapability("maxDuration", 10800);
		desiredCapabilities.setCapability("commandTimeout", 600);
		desiredCapabilities.setCapability("idleTimeout", 1000);

		return desiredCapabilities;
	}
}
