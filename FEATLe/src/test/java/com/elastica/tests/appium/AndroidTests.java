package com.elastica.tests.appium;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class AndroidTests{
	
	private AndroidDriver driver;
	@BeforeClass(alwaysRun=true)
	public void setUp() throws MalformedURLException {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		
		desiredCapabilities.setCapability("deviceName","Emulator1");
		desiredCapabilities.setCapability("orientation", ScreenOrientation.PORTRAIT);
		desiredCapabilities.setCapability("platformVersion","6.0");
		desiredCapabilities.setCapability("platformName", "Android");
		desiredCapabilities.setCapability("name", "Sample");
		desiredCapabilities.setCapability("appPackage", "com.android.calculator2");
		desiredCapabilities.setCapability("appActivity","com.android.calculator2.Calculator");
		
		
		URL hubUrl = new URL("http://localhost:4723/wd/hub");

		driver = new AndroidDriver(hubUrl, desiredCapabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@AfterClass(alwaysRun=true)
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void testUIComputation() {
		MobileElement element1 = (MobileElement) driver.findElement(By.name("4"));
		MobileElement element2 = (MobileElement) driver.findElement(By.name("+"));
		MobileElement element3 = (MobileElement) driver.findElement(By.name("2"));
		MobileElement element4 = (MobileElement) driver.findElement(By.name("="));
		
		element1.click();element2.click();element3.click();element4.click();
		
		MobileElement element5 = (MobileElement) driver.findElement(By.id("com.android.calculator2:id/formula"));
		String text = element5.getText();
		System.out.println(text);
	}
	

}