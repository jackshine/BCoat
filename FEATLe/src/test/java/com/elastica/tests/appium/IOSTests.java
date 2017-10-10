package com.elastica.tests.appium;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class IOSTests{
	
	private IOSDriver driver;
	@BeforeClass(alwaysRun=true)
	public void setUp() throws MalformedURLException {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(MobileCapabilityType.APP, 
				System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+
						File.separator+"resources"+File.separator+"mobile"+File.separator+
						"app.zip");
		desiredCapabilities.setCapability(MobileCapabilityType.UDID,"8D52F309-87A4-4DDB-80E6-F40FA4233BD6");
		desiredCapabilities.setCapability("deviceName","iPhone 6(Simulator)");
		desiredCapabilities.setCapability("orientation", ScreenOrientation.PORTRAIT);
		desiredCapabilities.setCapability("platformVersion","9.3");
		desiredCapabilities.setCapability("platformName", "iOS");
		desiredCapabilities.setCapability("name", "Sample");

		
		
		URL sauceUrl = new URL("http://localhost:4723/wd/hub");

		driver = new IOSDriver(sauceUrl, desiredCapabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.getSessionId().toString();
	}

	@AfterClass(alwaysRun=true)
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void testUIComputation() {

		// populate text fields with values
		MobileElement fieldOne = (MobileElement) driver.findElementByAccessibilityId("TextField1");
		fieldOne.sendKeys("12");

		MobileElement fieldTwo = (MobileElement) driver.findElementsByClassName("UIATextField").get(1);
		fieldTwo.sendKeys("8");

		// they should be the same size, and the first should be above the second
		Assert.assertTrue(fieldOne.getLocation().getY() < fieldTwo.getLocation().getY());
		Assert.assertEquals(fieldOne.getSize(), fieldTwo.getSize());

		// trigger computation by using the button
		driver.findElementByAccessibilityId("ComputeSumButton").click();

		// is sum equal?
		MobileElement sumElement = (MobileElement) driver.findElementsByClassName("UIAStaticText").get(0);
		String sum  = sumElement.getText();
		Assert.assertEquals(Integer.parseInt(sum), 20);
	}

}