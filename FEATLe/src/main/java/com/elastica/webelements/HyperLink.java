package com.elastica.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * HyperLink Element
 * @author Eldo Rajan
 *
 */
public class HyperLink extends Element {

	public HyperLink(WebElement element) {
		super(element);
	}
	
	public HyperLink(WebDriver driver, By by) {
		super(driver, by);
	}

	public HyperLink(WebDriver driver, String locator) {
		super(driver, locator);
	}

}
