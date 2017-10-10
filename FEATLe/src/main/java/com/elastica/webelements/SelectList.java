package com.elastica.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * SelectList Element
 * @author Eldo Rajan
 *
 */
public class SelectList extends Element{

	public SelectList(WebElement element) {
		super(element);
	}

	public SelectList(WebDriver driver, By by) {
		super(driver, by);
	}
	
	public SelectList(WebDriver driver, String locator) {
		super(driver, locator);
	}
	
}
