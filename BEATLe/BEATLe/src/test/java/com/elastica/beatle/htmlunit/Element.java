package com.elastica.beatle.htmlunit;

import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.elastica.beatle.logger.Logger;

/**
 * Base Element
 * @author Eldo Rajan
 *
 */
public class Element implements WebElement {
	
	/**
	 * Enum for locator types
	 * @author Eldo Rajan
	 *
	 */
	public enum LocatorType {
		CSS, XPATH, ID, NAME, CLASS, DOM, LINK, LINKP
	}
	
	protected int DEFAULT_IMPLICIT_WAIT_TIMEOUT = 5;
	protected int DEFAULT_EXPLICIT_WAIT_TIME_OUT = 60;
	protected int DEFAULT_PAGE_LOAD_TIMEOUT = 90;

	protected WebElement element;
	protected WebDriver driver;
	protected String locator;

	public Element(WebElement element){
		this.element = element;
	}
	
	public Element(WebDriver driver,By by){
		this.driver = driver;
		try{
			this.element = driver.findElement(by);
		}catch(Exception e){} 
	}

	public Element(WebDriver driver,String locator){
		this.locator = locator;
		this.driver = driver;
		this.element = findElement(driver, getBy(locator));
		/*try{
			this.element = driver.findElement(getBy(locator));
		}catch(Exception e){} */

	}

	/**
	 * get element
	 * @return
	 */
	public WebElement getElement() {
		return element;
	}

	/**
	 * set element
	 * @param element
	 */
	public void setElement(WebElement element) {
		this.element = element;
	}

	/**
	 * clear
	 */
	public void clear() {
		element.clear();
	}

	/**
	 * click
	 */
	public void click() {
		element.click();
	}

	/**
	 * 	find element
	 * @param driver
	 * @param by
	 * @return
	 */
	public WebElement findElement(WebDriver driver,By by) {
		try {
			return driver.findElement(by);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("can't findElement " + by + " in WebElement[" + toString() + "]", e);
		} catch (StaleElementReferenceException e) {
			throw new StaleElementReferenceException("WebElement[" + toString() + "] has been removed (unable to find " + by + ")", e);
		}		
	}

	/**
	 * find elements
	 * @param driver
	 * @param by
	 * @return
	 */
	public List<WebElement> findElements(WebDriver driver,By by) {
		try {
			return driver.findElements(by);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("can't findElements " + by + " in WebElement[" + toString() + "]", e);
		} catch (StaleElementReferenceException e) {
			throw new StaleElementReferenceException("WebElement[" + toString() + "] has been removed (unable to find " + by + ")", e);
		}		
	}

	/**
	 * get attribute
	 */
	public String getAttribute(String attribute) {
		return element.getAttribute(attribute);
	}

	/**
	 * get css value
	 */
	public String getCssValue(String cssValue) {
		return element.getCssValue(cssValue);
	}

	/**
	 * get location
	 */
	public Point getLocation() {
		return element.getLocation();
	}

	/**
	 * get size
	 */
	public Dimension getSize() {
		return element.getSize();
	}

	/**
	 * get tag name
	 */
	public String getTagName() {
		return element.getTagName();
	}

	/**
	 * get text
	 */
	public String getText() {
		return element.getText();
	}
	
	/**
	 * get inner html
	 */
	public String getInnerHtml() {
		return element.getAttribute("innerHTML");
	}

	/**
	 * get text from textbox
	 */
	public String getTextFromTextBox() {
		return element.getAttribute("value");
	}

	/**
	 * is displayed
	 */
	public boolean isDisplayed() {
		try{
			return element.isDisplayed();
		}catch(Exception e){
			return false;
		} 		
	}

	/**
	 * is enabled
	 */
	public boolean isEnabled() {
		try{
			return element.isEnabled();
		}catch(Exception e){
			return false;
		} 
	}

	/**
	 * is selected
	 */
	public boolean isSelected() {
		try{
			return element.isSelected();
		}catch(Exception e){
			return false;
		} 
	}

	/**
	 * type
	 * @param text
	 */
	public void type(String text) {
		element.sendKeys(text);
	}

	/**
	 * submit
	 */
	public void submit() {
		element.submit();
	}

	/**
	 * send keys
	 */
	public void sendKeys(CharSequence... text) {
		element.sendKeys(text);
	}

	/**
	 * is element present
	 * @param driver
	 * @return
	 */
	public boolean isElementPresent(WebDriver driver) {
		try{
			return driver.findElements(getBy(locator)).size()>0;
		}catch(Exception e){
			return false;
		} 
	}

	/**
	 * is element visible
	 * @return
	 */
	public boolean isElementVisible() {
		try{
			return element.isDisplayed();
		}catch(Exception e){
			return false;
		} 
	}

	/**
	 * select dropdown
	 * @param valToSelect
	 */
	public void selectDropDown(String valToSelect){
		Select select = new Select(element);  
		// Get a list of the options 
		List<WebElement> options = select.getOptions();  
		// For each option in the list, verify if it's the one you want and then click it 
		for (WebElement we : options) {     
			if (we.getText().equalsIgnoreCase(valToSelect)) {         
				Logger.info(String.format("Value is: %s", we.getText()));
				we.click();
				break;     
			} 
		} 

	}

	/**
	 * select value from dropdown
	 * @param valToSelect
	 */
	public void selectValueFromDropDown(String valToSelect){
		Select select = new Select(element);  
		// Get a list of the options 
		List<WebElement> options = select.getOptions();  
		// For each option in the list, verify if it's the one you want and then click it 
		for (WebElement we : options) {     
			if (we.getText().equalsIgnoreCase(valToSelect)) {         
				Logger.info(String.format("Value is: %s", we.getText()));
				select.selectByValue(valToSelect);
				break;     
			} 
		} 

	}

	/**
	 * select value visible
	 * @param valToSelect
	 */
	public void selectValueVisible(String valToSelect){
		new Select(element).selectByVisibleText(valToSelect);  
	}


	/**
	 * get value from dropdown
	 * @return
	 */
	public String[] getValueFromDropDown(){
		Select select = new Select(element);  
		// Get a list of the options 
		List<WebElement> options = select.getOptions();
		String[] value = new String[options.size()];
		for (int i=0;i<options.size();i++) {
			value[i] =options.get(i).getText();
		}
		return value;
	}

	/**
	 * get selected item
	 * @return
	 */
	public String selectedItem(){ 
		Select select = new Select(element); 
		WebElement element = select.getFirstSelectedOption();
		return element.getText();     
	}

	/**
	 * mouse over
	 * @param driver
	 */
	public void mouseOver(WebDriver driver){
		Actions action = new Actions(driver);		 
		action.moveToElement(element).build().perform();
	}

	/**
	 * mouse over click
	 * @param driver
	 */
	public void mouseOverClick(WebDriver driver){
		Actions action = new Actions(driver);		 
		Actions action2 = action.moveToElement(element);
		action2.click().build().perform();
	}

	/**
	 * mouse over right click
	 * @param driver
	 */
	public void mouseOverRightClick(WebDriver driver){
		Actions action = new Actions(driver);		 
		Actions action2 = action.moveToElement(element);
		action2.contextClick().build().perform();
	}

	/**
	 * check
	 */
	public void check() {
		element.click();
	}

	/**
	 * uncheck
	 */
	public void uncheck(){
		if(element.isSelected()){
			element.click();
		}
	}

	/**
	 * is checked
	 * @return
	 */
	public boolean isChecked(){
		try{
			return element.isSelected();
		}catch(Exception e){
			return false;
		} 
	}

	/**
	 * select
	 */
	public void select() {
		element.click();
	}

	/**
	 * unselect
	 */
	public void unselect(){
		if(element.isSelected()){
			element.click();
		}
	}

	/**
	 * select value
	 * @param sItem
	 */
	public  void selectValue(String sItem) {
		selectValueFromDropDown(sItem);	
	}

	/**
	 * get item list
	 * @return
	 */
	public String[] getItemList(){
		return getValueFromDropDown();
	}

	/**
	 * select item
	 * @param sItem
	 */
	public void selectItem(String sItem) {
		selectDropDown(sItem);	
	}

	/**
	 * wait for element to be checked
	 * @param driver
	 * @param element
	 */
	public void waitForElementChecked(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT_TIME_OUT);
		wait.until(ExpectedConditions.elementToBeSelected(element));
	}

	/**
	 * wait for element to be editable
	 * @param driver
	 * @param element
	 */
	public void waitForElementEditable(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT_TIME_OUT);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * wait for element to be present
	 * @param driver
	 * @param by
	 */
	public void waitForElementPresent(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT_TIME_OUT);
		wait.until(ExpectedConditions.presenceOfElementLocated(getBy(locator)));
	}

	/**
	 * wait for element to be present with timeout
	 * @param driver
	 * @param by
	 * @param timeout
	 */
	public void waitForElementPresent(WebDriver driver,int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.presenceOfElementLocated(getBy(locator)));
	}


	/**
	 * wait for element to be present
	 * @param driver
	 * @param element
	 */
	public void waitForElementToBeVisible(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT_TIME_OUT);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * wait for element to be disappear
	 * @param driver
	 * @param by
	 */
	public void waitForElementToDisappear(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT_TIME_OUT);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(getBy(locator)));
	}

	/**
	 * wait for text to be present
	 * @param driver
	 * @param element
	 * @param text
	 */
	public void waitForTextPresent(WebDriver driver,String text) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT_TIME_OUT);
		wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	/**
	 * wait for loading
	 * @param driver
	 * @param timeoutMillis
	 */
	public void waitForLoading(WebDriver driver, int timeoutMillis) {
		long end = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < end) {
			try {
				WebElement result = driver.findElement(getBy(locator));
				if (result.isDisplayed()) {
					break;
				}
			} catch (NoSuchElementException e) {
				// nothing to do
			}
		}
	}

	/**
	 * wait for loading
	 * @param driver
	 */
	public void waitForLoading(WebDriver driver) {
		long end = System.currentTimeMillis() + (DEFAULT_IMPLICIT_WAIT_TIMEOUT*1000);
		while (System.currentTimeMillis() < end) {
			try {
				WebElement result = driver.findElement(getBy(locator));
				if (result.isDisplayed()) {
					break;
				}
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException("can't findElement " + getBy(locator) + " in WebElement[" + toString() + "]", e);
			}
		}
	}


	/**
	 * wait for loading
	 * @param driver
	 * @param timeoutMillis
	 */
	public void waitForElementNotToBePresent(WebDriver driver, int timeoutMillis) {
		long end = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < end) {
			try {
				List<WebElement> result = driver.findElements(getBy(locator));
				if (!result.isEmpty()) {
					// nothing to do
				}
			} catch (Exception e) {
				break;
			}
		}
	}

	/**
	 * wait for loading
	 * @param driver
	 */
	public void waitForElementNotToBePresent(WebDriver driver) {
		long end = System.currentTimeMillis() + (1*1000);
		try{
			while (System.currentTimeMillis() < end) {
				try {
					List<WebElement> result = driver.findElements(getBy(locator));
					if (!result.isEmpty()) {
						// nothing to do
					}
				} catch (Exception e) {
					break;
				}
			}
		} catch (Exception e) {
			// nothing to do
		}
	}

	/**
	 * get by
	 * @param locator
	 * @return
	 */
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


	@Override
	public WebElement findElement(By arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WebElement> findElements(By arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getRect() {
		// TODO Auto-generated method stub
		return null;
	}



}
