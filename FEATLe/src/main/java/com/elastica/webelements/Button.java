package com.elastica.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Button Element
 * @author Eldo Rajan
 *
 */
public class Button extends Element {

	public Button(WebElement element) {
		super(element);
	}
	
	public Button(WebDriver driver, By by) {
		super(driver, by);
	}

	public Button(WebDriver driver, String locator) {
		super(driver, locator);
	}

}
