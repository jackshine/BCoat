package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.CheckBox;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextArea;
import com.elastica.webelements.TextBox;

public class AwsPage extends BasePage {
	
	public HyperLink awsLoginEmail(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awsLoginPassword(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awsLoginButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awsLogoutMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awsLogoutButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3BucketClick(WebDriver driver, String bucketName){
		return new HyperLink(driver,getLocator().replaceAll("bucname", bucketName));
	}
	
	public HyperLink awss3BucketSelect(WebDriver driver, String bucketName){
		return new HyperLink(driver,getLocator().replaceAll("bucname", bucketName));
	}
	
	public HyperLink awss3CreateBucket(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3CreateBucketName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3CreateBucketButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3DeleteBucket(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3EmptyBucket(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3DeleteBucketName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3EmptyBucketName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3DeleteBucketOk(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3EmptyBucketOk(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Button awss3BucketActionButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink awss3CreateFolderButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3CreateFolderEnterName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3SelectFolder(WebDriver driver, String folderName){
		return new HyperLink(driver,getLocator().replaceAll("foldername", folderName));
	}
	
	public HyperLink awss3FolderActionButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3DeleteFolderMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3regiondropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
			
	public HyperLink awss3selectregion(WebDriver driver, String regionName){
		return new HyperLink(driver,getLocator().replaceAll("regionname", regionName));
	}
	
	public HyperLink awss3UploadButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3AddFilesButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3StartUploadButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3ShareFileMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3OpenFileMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3DownloadFileMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3DownloadLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink awss3DownloadPanelOk(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
}