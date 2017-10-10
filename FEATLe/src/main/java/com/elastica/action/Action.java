package com.elastica.action;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.elastica.logger.Logger;

/**
 * Action Super Class
 * @author Eldo Rajan
 *
 */
public class Action{

	public void hardWait(int timeout){
		try {
			Logger.info("Waiting for "+timeout+" seconds");
			Thread.sleep(timeout*1000);
			Logger.info("Wait completed");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void implicitWait(WebDriver driver, int timeout) {
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}

	public int getWindowCount(WebDriver driver){
		return driver.getWindowHandles().size();
	}

	public String getWindowHandle(WebDriver driver){
		return driver.getWindowHandle();
	}

	public Set<String> getWindowHandles(WebDriver driver){
		return driver.getWindowHandles();
	}

	public void switchToWindow(WebDriver driver, String winHandle){
		driver.switchTo().window(winHandle);
	}

	public void switchToParentWindow(WebDriver driver){
		driver.switchTo().window("");
	}

	public void switchToWindowByTitle(String title, WebDriver driver) {
		Logger.info("Switch by window Title" + title);
		boolean found = false;
		while (found == false) {
			for (String handle : getWindowHandles(driver)) {
				switchToWindow(driver, handle);
				String myTitle = driver.getTitle();
				if (myTitle.equalsIgnoreCase(title)) {
					found = true;
					break;
				}
			}
			found = true;
		}
		Logger.info("Window is found " + found);
	}

	public void closeWindowNotHavingTitle(String title, WebDriver driver) {
		Logger.info("Close by window Title" + title);
		for (String handle : getWindowHandles(driver)) {
			switchToWindow(driver, handle);
			String myTitle = driver.getTitle();
			if (!myTitle.equalsIgnoreCase(title)) {
				Logger.info("Title" + myTitle + "getting closed");
				driver.close();
			}
		}
		Logger.info("Closed unneccessary window");
	}

	public void switchToWindowByTitleContains(String title, WebDriver driver) {
		Logger.info("Switch by window Title" + title);
		boolean found = false;
		while (found == false) {
			for (String handle : getWindowHandles(driver)) {
				switchToWindow(driver, handle);
				String myTitle = driver.getTitle();
				if (myTitle.contains(title)) {
					found = true;
					break;
				}
			}
			found = true;
		}
		Logger.info("Window is found " + found);
	}



	public String getCurrentUrl(WebDriver driver){
		return driver.getCurrentUrl().trim();
	}

	public void switchFrame(WebElement element, WebDriver driver) {
		driver.switchTo().frame(element);
	}

	public void switchTopWindow(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	public void cleanupFileInFolder(String folderPath, String filename) {
		try {
			File dir = new File(folderPath);
			if (dir.exists()) {
				if (dir.isDirectory()) {
					if (dir.listFiles().length == 0) {
						// dir.delete();
					} else {
						for (File f : dir.listFiles()){
							if (f.getName().equalsIgnoreCase(filename)) {
								f.delete();
								break;
							}
						}	
					}
					// dir.delete();
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public long getTimeDifference(String time1,String time2,String format,String timezone){
		long diff=0;

		diff = getUnixTimeStamp(time1, timezone, format)-getUnixTimeStamp(time2, timezone, format);
		Logger.info(diff);

		return diff;
	}

	public long getUnixTimeStamp(String time,String timezone,String format){
		DateFormat dfm = new SimpleDateFormat(format);  
		long unixtime = 0;
		dfm.setTimeZone(TimeZone.getTimeZone(timezone));//Specify your timezone 
		try{
			unixtime = dfm.parse(time).getTime();  
			unixtime=unixtime/1000;
		}catch (ParseException e){
			e.printStackTrace();
		}
		return unixtime;
	}

	@SuppressWarnings("deprecation")
	public String getDateInFormat(String date, String oldFormat, String newFormat) throws ParseException {

		SimpleDateFormat ftOld = new SimpleDateFormat(oldFormat);
		Date d = ftOld.parse(date);d.setYear(Calendar.getInstance().get(Calendar.YEAR)-1900);

		SimpleDateFormat ft = new SimpleDateFormat(newFormat);

		date = ft.format(d);
		Logger.info(date);

		return date; 
	}

	public void refresh(WebDriver driver,int timeout){
		driver.navigate().refresh();hardWait(timeout);
	}

	public void waitForPageToLoad(WebDriver driver, String title, int timeout) {
		long end = System.currentTimeMillis() + timeout*1000;
		long start=System.currentTimeMillis();
		while (System.currentTimeMillis() < end) {
			if (driver.getTitle().contains(title)){
				break;
			}
		}
		Logger.info("Page with title "+title+" get loads in "+(System.currentTimeMillis()-start)+ " milli seconds");
	}

}
