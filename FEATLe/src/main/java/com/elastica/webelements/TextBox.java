package com.elastica.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * TextBox Element
 * @author Eldo Rajan
 *
 */
public class TextBox extends Element {

	public TextBox(WebElement element) {
		super(element);
	}

	public TextBox(WebDriver driver, By by) {
		super(driver, by);
	}
	
	public TextBox(WebDriver driver, String locator) {
		super(driver, locator);
	}

	
}