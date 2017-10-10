package com.elastica.webelements;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.elastica.logger.Logger;
import com.elastica.webelements.BasePage.LocatorType;

/**
 * Base Element
 * @author Eldo Rajan
 *
 */
public class Element implements WebElement {
	protected int DEFAULT_IMPLICIT_WAIT_TIMEOUT = 5;
	protected int DEFAULT_EXPLICIT_WAIT_TIME_OUT_MEDIUM = 30;
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

		if(findElements(driver, getBy(locator)).size()>0){
			this.element = findElement(driver, getBy(locator));
		}else{
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
		}
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
		try {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.info("Going to clear element:"+elements[2].getMethodName());
			element.clear();
			Logger.info("Cleared element:"+elements[2].getMethodName());
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
		}
	}

	/**
	 * click
	 */
	public void click() {
		try {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.info("Going to click element:"+elements[2].getMethodName());
			element.click();
			Logger.info("Clicked element:"+elements[2].getMethodName());
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
		}
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
			return null;
			//throw new NoSuchElementException("can't findElement " + by + " in WebElement[" + toString() + "]", e);
		} catch (StaleElementReferenceException e) {
			return null;
			//throw new StaleElementReferenceException("WebElement[" + toString() + "] has been removed (unable to find " + by + ")", e);
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

	public WebElement findElement(By by) {
		try {
			return driver.findElement(by);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("can't findElement" + by + " in WebElement[" + toString() + "]", e);
		} catch (StaleElementReferenceException e) {
			throw new StaleElementReferenceException("WebElement[" + toString() + "] has been removed (unable to find " + by + ")", e);
		}
	}

	public List<WebElement> findElements(By by) {
		try {
			return driver.findElements(by);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("can't findElement" + by + " in WebElement[" + toString() + "]", e);
		} catch (StaleElementReferenceException e) {
			throw new StaleElementReferenceException("WebElement[" + toString() + "] has been removed (unable to find " + by + ")", e);
		}
	}

	public <X> X getScreenshotAs(OutputType<X> output) throws WebDriverException {
		return null;
	}

	public Rectangle getRect() {
		return null;
	}



	/**
	 * get attribute
	 */
	public String getAttribute(String attribute) {
		try {	
			return element.getAttribute(attribute);
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
			return null;
		}
	}

	/**
	 * get css value
	 */
	public String getCssValue(String cssValue) {
		try {	
			return element.getCssValue(cssValue);
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
			return null;
		}
	}

	/**
	 * get location
	 */
	public Point getLocation() {
		try {	
			return element.getLocation();
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
			return null;
		}
	}

	/**
	 * get size
	 */
	public Dimension getSize() {
		try {	
			return element.getSize();
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
			return null;
		}
	}

	/**
	 * get tag name
	 */
	public String getTagName() {
		try {	
			return element.getTagName().trim();
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
			return null;
		}
	}

	/**
	 * get text
	 */
	public String getText() {
		try {	
			return element.getText().trim();
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
			return null;
		}
	}

	/**
	 * get inner html
	 */
	public String getInnerHtml() {
		try {	
			return element.getAttribute("innerHTML").trim();
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
			return null;
		}
	}

	/**
	 * get text from textbox
	 */
	public String getTextFromTextBox() {
		try {	
			return element.getAttribute("value").trim();
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
			return null;
		}
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
		try {	element.sendKeys(text);
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
		}
	}

	/**
	 * submit
	 */
	public void submit() {
		try {	element.submit();
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
		}
	}

	/**
	 * send keys
	 */
	@Deprecated
	public void sendKeys(CharSequence... text) {
		try {	element.sendKeys(text);
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
		}
	}

	/**
	 * is element present
	 * @param driver
	 * @return
	 */
	public boolean isElementPresent(WebDriver driver) {
		boolean flag=false;
		try{
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			flag =  driver.findElements(getBy(locator)).size()>0;
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}catch(Exception e){
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} 

		return flag;
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
	 * 
	 * @param driver
	 */
	public void focus() {
		try {	element.sendKeys("");
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
		}
	}


	/**
	 * 
	 * @param driver
	 */
	public void mouseOverJS(WebDriver driver) {
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(mouseOverScript, element);
	}

	/**
	 * mouse over
	 * @param driver
	 */
	public void mouseOver(WebDriver driver){
		if ((driver instanceof SafariDriver) || (((RemoteWebDriver) driver).getCapabilities().getBrowserName().contains("safari"))) {
			try{
				String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript(mouseOverScript, element);
			}catch (Exception e){
				e.printStackTrace();
			}

		}else{
			Actions action = new Actions(driver);		 
			action.moveToElement(element).build().perform();
		}
	}

	/**
	 * mouse over click
	 * @param driver
	 */
	public void mouseOverClick(WebDriver driver){
		if ((driver instanceof SafariDriver) || (((RemoteWebDriver) driver).getCapabilities().getBrowserName().contains("safari"))) {
			try{
				String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('click', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onclick');}";
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript(mouseOverScript, element);
			}catch (Exception e){
				e.printStackTrace();
			}

		}else{
			Actions action = new Actions(driver);		 
			Actions action2 = action.moveToElement(element);
			action2.click().build().perform();
		}
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
		try {	element.click();
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
		}
	}

	/**
	 * uncheck
	 */
	public void uncheck(){
		try {
			if(element.isSelected()){
				element.click();
			}
		} catch (NullPointerException e) {
			StackTraceElement[] elements = new Throwable().getStackTrace();
			Logger.error(elements[2].getMethodName()+" element not found");
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
	 * @param timeout in seconds
	 */
	public void waitForLoading(WebDriver driver, int timeout) {
		long start=System.currentTimeMillis();
		long end = System.currentTimeMillis() + (timeout*1000);
		while (System.currentTimeMillis() < end) {
			try {
				WebElement result = driver.findElement(getBy(locator));
				if (result.isDisplayed()) {
					Logger.info("Element get displayed in miliseconds: "+(System.currentTimeMillis()-start));
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




}
