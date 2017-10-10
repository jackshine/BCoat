package com.elastica.beatle.htmlunit;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


public class WebDriver {

	/**
	 * Enum for locator types
	 * @author Eldo Rajan
	 *
	 */
	public enum LocatorType {
		CSS, XPATH, ID, NAME, CLASS, DOM, LINK, LINKP
	}
	
	public By getBy(String locator) {
		LocatorType identifier = LocatorType.valueOf(locator.toUpperCase().substring(0, locator.indexOf("=")));
		locator = locator.substring(locator.indexOf("=")+1);

		By locatorIdentifiedBy=null;
		switch (identifier) {
		case XPATH:
			locatorIdentifiedBy = By.xpath(locator);
			break;
		case CSS:
			locatorIdentifiedBy = By.cssSelector(locator);
			break;
		case ID:
			locatorIdentifiedBy = By.id(locator);
			break;
		case NAME:
			locatorIdentifiedBy = By.name(locator);
			break;
		case CLASS:
			locatorIdentifiedBy = By.className(locator);
			break;
		case LINK:
			locatorIdentifiedBy = By.linkText(locator);
			break;
		case LINKP:
			locatorIdentifiedBy = By.partialLinkText(locator);
			break;
		default:
			locatorIdentifiedBy = By.cssSelector(locator);
			break;
		}
		return locatorIdentifiedBy;
	}
	
	public HtmlUnitDriver getWebDriver() {
		HtmlUnitDriver driver = null;
		try{
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
	
	
}
