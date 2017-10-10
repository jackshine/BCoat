package com.elastica.beatle.tests.dci.temp;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class GoogleCrawler {


	public static void main(String[] args) {


		GoogleCrawler obj = new GoogleCrawler();
		String fileExtn=".upx";
		obj.getDataFromGoogle(fileExtn,10);
		
	}

	public void saveFile(String sourceUrl,String fileExtn) throws Exception{

		System.out.println("Link:"+sourceUrl);
		 URL url = new URL(sourceUrl);
		 String[] f=url.getFile().split("/");
		 String fileName = f[f.length-1];
		 Path targetPath = new File(System.getProperty("user.dir") +File.separator+"FileCrawler"+File.separator + fileName).toPath();
		 Files.copy(url.openStream(), targetPath,
		            StandardCopyOption.REPLACE_EXISTING);
	}


	private void getDataFromGoogle(String query,int count) {

		String locator = "a[href$=\""+query+"\"]";
		String request = "https://www.google.com/search?q=" + query + "&num=100";
		System.out.println("Sending request..." + request);

		try {

			DesiredCapabilities capability= DesiredCapabilities.firefox();
			capability.setJavascriptEnabled(true);
			capability.setBrowserName("htmlunit");
			capability.setVersion("internet explorer");
			capability.setPlatform(org.openqa.selenium.Platform.ANY);
			WebDriver driver = new HtmlUnitDriver(capability);
			driver.get(request);
			
			for(int i=0;i<count;i++){
				List<WebElement> links = driver.findElements(By.cssSelector(locator));
				if(links.size()==0){
					driver.findElement(By.partialLinkText("Next")).click();
					links = driver.findElements(By.cssSelector(locator));
				}
				for(int j=0;j<links.size();j++){
					String temp = links.get(j).getAttribute("href");		
					saveFile(temp, query);
					if(j==links.size()-1){
						driver.findElement(By.partialLinkText("Next")).click();
					}
				}
				if(links.size()==0){
					driver.findElement(By.partialLinkText("Next")).click();
				}	
					
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}