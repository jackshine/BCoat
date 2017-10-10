package com.elastica.tests.sso;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import com.elastica.logger.Logger;

public class SSOFileUploadTests{


	@Test(groups ={"regression"})
	public void testSSOLevel1() throws Exception {

		RemoteWebDriver driver = null;
		String huburl = "http://eldoelastica:b5d80759-f9bf-4dda-a729-b80d7ae254e8@ondemand.saucelabs.com:80/wd/hub";

		try {
			driver = new RemoteWebDriver(new URL(huburl), getDesiredCapabilitiesCloud());
			driver.setFileDetector(new LocalFileDetector());
		} catch (Exception e) {
			Logger.error("Fail to start browser in cloud configuration");
			e.printStackTrace();
		}
		
		driver.get("http://www.google.com");

	}	

	public DesiredCapabilities getDesiredCapabilitiesCloud() {

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(false);
		profile.setEnableNativeEvents(true);

		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.dir", "downloadDir");
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

		

		desiredCapabilities.setCapability(CapabilityType.PLATFORM, "Windows 8.1");
		desiredCapabilities.setCapability(CapabilityType.VERSION, "40.0");
		desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, "firefox");

		
//		Map<String, String> prefs = new HashMap<String, String>();
//		prefs.put("executable", "sauce-storage: FileUploadFirefox.exe");
//		prefs.put("background", "false");
//		desiredCapabilities.setCapability("prerun", prefs);
		
		desiredCapabilities.setCapability("prerun", "{'executable':'sauce-storage: FileUploadFirefox.exe','background': 'false'}");
		 
		desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		desiredCapabilities.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, 1);
		desiredCapabilities.setCapability("screenResolution", "1024x768");
		desiredCapabilities.setCapability("seleniumVersion", "2.48.0");
		desiredCapabilities.setCapability("chromedriverVersion", "2.20");
		desiredCapabilities.setCapability("iedriverVersion", "2.48.0");

		desiredCapabilities.setCapability("maxDuration", 10800);
		desiredCapabilities.setCapability("commandTimeout", 600);
		desiredCapabilities.setCapability("idleTimeout", 1000);

		return desiredCapabilities;
	}

}