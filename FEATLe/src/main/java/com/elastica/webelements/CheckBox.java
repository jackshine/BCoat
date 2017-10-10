package com.elastica.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Checkbox Element
 * @author Eldo Rajan
 *
 */
public class CheckBox extends Element{

	public CheckBox(WebElement element) {
		super(element);
	}
	
	public CheckBox(WebDriver driver, By by) {
		super(driver, by);
	}

	public CheckBox(WebDriver driver, String locator) {
		super(driver, locator);
	}
	
}
