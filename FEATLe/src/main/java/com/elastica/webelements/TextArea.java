package com.elastica.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * TextBox Element
 * @author Eldo Rajan
 *
 */
public class TextArea extends Element {

	public TextArea(WebElement element) {
		super(element);
	}

	public TextArea(WebDriver driver, By by) {
		super(driver, by);
	}
	
	public TextArea(WebDriver driver, String locator) {
		super(driver, locator);
	}

	
}