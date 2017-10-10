package com.elastica.action.box;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.box.BoxPage;
import  com.elastica.common.GWCommonTest;

/**
 * Box common actions
 * @author eldorajan
 *
 */
public class BoxAction extends Action{
	
	GWCommonTest objCommonTests ;
	
	public void BoxAction(){
		objCommonTests	= new GWCommonTest();
	}
	
	public void login(WebDriver driver, SuiteData suiteData) {
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver,BoxPage.class);
		
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("User credentials: "+suiteData.getSaasAppUsername()+"/"+suiteData.getSaasAppPassword());
		driver.get(suiteData.getSaasAppBaseUrl());hardWait(10);
		bp.usernameTextBox(driver).clear();bp.usernameTextBox(driver).type(suiteData.getSaasAppUsername());hardWait(2);
		bp.passwordTextBox(driver).clear();bp.passwordTextBox(driver).type(suiteData.getSaasAppPassword());hardWait(2);
		bp.signInButton(driver).click();hardWait(10);
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
	}
	
	
	public void loginBlock(WebDriver driver, SuiteData suiteData) {
		driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.MINUTES);
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver,BoxPage.class);
		
		Logger.info("Started the  method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		Logger.info("User credentials: "+suiteData.getSaasAppUsername()+"/"+suiteData.getSaasAppPassword());
		driver.get(suiteData.getSaasAppBaseUrl());hardWait(10);
		bp.usernameTextBox(driver).clear();bp.usernameTextBox(driver).type(suiteData.getSaasAppUsername());hardWait(2);
		bp.passwordTextBox(driver).clear();bp.passwordTextBox(driver).type(suiteData.getSaasAppPassword());hardWait(2);
		try {
			driver.findElement(By.cssSelector(".form-buttons>button")).click();
			//bp.signInButton(driver).submit();
		} catch(Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		hardWait(10);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
	}
	
	
	public void uploadFile(WebDriver driver, String filePath, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		
		clickUploadButton(driver);
		bp.uploadFiles(driver).type(filePath);
		
		if(bp.uploadButtonPopup(driver).isDisplayed()){
			clickPopupUploadButton(driver, waitTime);
		}
		else{
			hardWait(waitTime);
		}
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void uploadFileIfNotPresent(WebDriver driver, String filePath, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		File myfile = new File(filePath);
		String myFileName =myfile.getName();
		Logger.info("File name from path="+myfile.getName());
		
		if(bp.selectItemCheckBox(driver, myFileName).isElementVisible()==false){
			uploadFile(driver, filePath, waitTime);
		}
		else{
			Logger.info("File already present:"+myFileName);
		}
		
		
		
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickUploadButton(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.uploadButton(driver).click();hardWait(1);
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	
	public void clickPopupUploadButton(WebDriver driver, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.uploadButtonPopup(driver).click();hardWait(waitTime);
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void clickLogout(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.userMenuDropDown(driver).click();hardWait(5);
		bp.logOutLink(driver).click();hardWait(5);
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void invalidLogin(WebDriver driver, SuiteData suiteData) {
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver,BoxPage.class);
		
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		driver.get(suiteData.getSaasAppBaseUrl());hardWait(10);
		bp.usernameTextBox(driver).type(suiteData.getSaasAppUsername());hardWait(5);
		bp.passwordTextBox(driver).type("wrongPassword");hardWait(5);
		bp.signInButton(driver).click();hardWait(10);
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
	}
	
	public void createFolder(WebDriver driver, String folderName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.newButton(driver).click();hardWait(5);
		bp.createFolderButton(driver).click();hardWait(5);
		bp.popUpWindow(driver).click();hardWait(5);
		bp.popUpFolderNameTextBox(driver).type(folderName);hardWait(5);
		bp.popUpOkayButton(driver).click();hardWait(5);
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void createFolderIfNotPresent(WebDriver driver, String folderName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		if(bp.selectItemCheckBox(driver, folderName).isDisplayed()==false){
			bp.newButton(driver).click();hardWait(5);
			bp.createFolderButton(driver).click();hardWait(5);
			bp.popUpWindow(driver).click();hardWait(5);
			bp.popUpFolderNameTextBox(driver).type(folderName);hardWait(5);
			bp.popUpOkayButton(driver).click();hardWait(5);
		}
		else{
			Logger.info("Folder already present:"+folderName);
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void createFileIfNotPresent(WebDriver driver, String folderName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		if(bp.selectItemCheckBox(driver, folderName).isDisplayed()==false){
			bp.newButton(driver).click();hardWait(5);
			bp.createFolderButton(driver).click();hardWait(5);
			bp.popUpWindow(driver).click();hardWait(5);
			bp.popUpFolderNameTextBox(driver).type(folderName);hardWait(5);
			bp.popUpOkayButton(driver).click();hardWait(5);
		}
		else{
			Logger.info("Folder already present:"+folderName);
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void openItem(WebDriver driver, String itemName,int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		Logger.info("itemName="+itemName);
		bp.itemNameLink(driver,itemName).click();hardWait(waitTime);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void deleteOpenedFolder(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.folderOptionsButton(driver).click();hardWait(5);
		bp.deleteLinkInFolderOptions(driver).click();hardWait(5);
		bp.deleteConfirmButton(driver).click();hardWait(5);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void closePreview(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.closePreviewLink(driver).click();hardWait(5);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void selectItemLink(WebDriver driver,String itemName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.selectItemCheckBox(driver,itemName).click();hardWait(5);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void gotoHomePage(WebDriver driver,String homePageUrl){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		
		Logger.info("Loading home page");
		driver.get(homePageUrl);
		hardWait(10);
		
		Logger.info("Home page load successful");
		
//		refresh(driver, 10);
//		bp.homepageLink(driver).click();hardWait(10);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public String getTimestamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
		Date date = new Date();
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
	
	public void deleteSelectedItem(WebDriver driver, String itemName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.selectItemCheckBox(driver, itemName).click();hardWait(5);
		bp.deleteSelectedButton(driver).waitForElementPresent(driver);
		bp.deleteSelectedButton(driver).click();hardWait(5);
		bp.deleteConfirmButton(driver).click();hardWait(5);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void downloadSelectedItem(WebDriver driver, String itemName, int waitTime, String browserName) throws InterruptedException{
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.selectItemCheckBox(driver, itemName).click();hardWait(15);
		bp.downloadSelectedButton(driver).click();hardWait(waitTime);
		
		if(browserName.equals("ie")){
			Logger.info("IE browser detected");
			objCommonTests.ieDownloadFile();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
	}
	
	public void deleteAllItems(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		
		if(bp.selectAllCheckBoxTopElement(driver).getAttribute("class").contains("disabled")){
			Logger.info("As select all checkbox is disabled, there is nothing in the page to delete.");
		}else{
			bp.selectAllCheckBoxTopElement(driver).click();hardWait(2);
			bp.deleteSelectedButton(driver).waitForElementToBeVisible(driver);
			bp.deleteSelectedButton(driver).click();hardWait(5);
			bp.deleteConfirmButton(driver).click();hardWait(5);
		}
		
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void shareItem(WebDriver driver, String itemName, String sharingType, String emailId, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
//		bp.moreActionMenu(driver, itemName).click();hardWait(5);
//		bp.sharingItemMenu(driver).waitForElementToBeVisible(driver);
//		bp.sharingItemMenu(driver).click();hardWait(5);
//		bp.shareLinkItemMenu(driver).click();hardWait(5);
		bp.shareLinkTopElement(driver,itemName).click();hardWait(5);
		if(sharingType.equals("people_with_link")){
			bp.shareChangeMenu(driver).click();hardWait(2);
			bp.sharePeopleWithLink(driver).click();hardWait(10);
		}
		else if(sharingType.equals("people_in_your_company")){
			bp.shareChangeMenu(driver).click();hardWait(2);
			bp.sharePeopleInYourCompany(driver).click();hardWait(10);
		}
		else if(sharingType.equals("people_in_this_folder")){
			bp.shareChangeMenu(driver).click();hardWait(2);
			bp.sharePeopleInThisFolder(driver).click();hardWait(10);
			
		}
		
//		bp.sharePopup(driver).focus();
		bp.shareEmailAddressTextBox(driver).type(emailId);hardWait(10);
		bp.sendButtonInSharePopUp(driver).waitForElementPresent(driver);
		bp.sendButtonInSharePopUp(driver).click();hardWait(waitTime);
		
		if(bp.errorNotificationDiv(driver).isDisplayed()){
			bp.errorNotificationDiv(driver).focus();
			
			bp.homepageLink(driver).click();hardWait(10);
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void unshareItem(WebDriver driver, String itemName, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		refresh(driver, 15);
		if(bp.shareLinkTopElement(driver,itemName).getText().equals("Share")){
			Logger.info("File is not shared.");
		}
		else
		{
			bp.shareLinkTopElement(driver,itemName).click();hardWait(2);
			bp.shareChangeMenu(driver).click();hardWait(2);
			bp.unshareRemoveLink(driver).click();hardWait(2);
			bp.ConfirmOkay(driver).click();hardWait(waitTime);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}
	
	public void createDoc(WebDriver driver, String docType,String docName, String docDesc){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.newButton(driver).click();hardWait(5);

		if(docType.equals("boxnote")){
			bp.newBoxNoteMenuLink(driver).click();hardWait(5);
		}
		else if(docType.equals("word")){
			bp.newWordDocMenuLink(driver).click();hardWait(5);
		}
		else if(docType.equals("powerpoint")){
			bp.newPowerPointDocMenuLink(driver).click();hardWait(5);
		}
		else if(docType.equals("excel")){
			bp.newExcelDocMenuLink(driver).click();hardWait(5);
		}
		else if(docType.equals("googledoc")){
			bp.newGoogleDocMenuLink(driver).click();hardWait(5);
		}
		else if(docType.equals("googlesheet")){
			bp.newGoogleSheetMenuLink(driver).click();hardWait(5);
		}
		else{
			Logger.info("Invalid document type mentioned:"+docType);
			System.exit(0);
		}
		
		bp.newItemNameTextBox(driver).type(docName);hardWait(5);
		bp.newItemAddDescLink(driver).click();hardWait(5);
		bp.newItemDescTextArea(driver).type(docDesc);hardWait(5);
		bp.newItemPopupOkayButton(driver).click();hardWait(15);
		this.switchToWindowByTitleContains(docName, driver);
		driver.close();
		this.switchToParentWindow(driver);
		
		if(bp.launchingPopupCloseButton(driver).isElementVisible()){
			bp.launchingPopupCloseButton(driver).click();hardWait(5);
		}
		
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void unlockItem(WebDriver driver, String itemName, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.moreActionMenu(driver, itemName).click();hardWait(5);
		if(bp.unlockItemMenu(driver).isElementVisible()){
			bp.unlockItemMenu(driver).click();hardWait(5);
//			bp.popUpOkayButton(driver).click();hardWait(waitTime);
		}
		else{
			Logger.info("File is already in unlocked state");
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void createBookmark(WebDriver driver, String url,String name, String desc){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.newButton(driver).click();hardWait(5);

		bp.newBookmarkMenuLink(driver).click();hardWait(5);
		
		bp.newItemUrlTextBox(driver).clear();
		bp.newItemUrlTextBox(driver).type(url);hardWait(5);
		bp.newItemNameTextBox(driver).type(name);hardWait(5);
		bp.newItemDescTextBox(driver).type(desc);hardWait(5);
		bp.newItemPopupOkayButton(driver).click();hardWait(15);
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void tagUntagSelectedItem(WebDriver driver, String itemName,String tagName, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.selectItemCheckBox(driver, itemName).click();hardWait(5);
		bp.tagSelectedItemButton(driver).click();hardWait(waitTime);
		bp.tagNameTextBox(driver).clear();
		bp.tagNameTextBox(driver).type(tagName);hardWait(waitTime);
		bp.popUpOkayButton(driver).click();hardWait(15);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void lockItem(WebDriver driver, String itemName, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.moreActionMenu(driver, itemName).click();hardWait(5);
		if(bp.lockItemMenu(driver).isElementVisible()){
			bp.lockItemMenu(driver).click();hardWait(5);
			bp.lockItemPopupContinueButton(driver).click();hardWait(waitTime);
		}
		else{
			Logger.info("File is already in unlocked state");
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void postComment(WebDriver driver, String comments, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
//		bp.enterCommentTextarea(driver).mouseOver(driver);hardWait(5);
//		bp.enterCommentTextarea(driver).click();hardWait(5);
		bp.enterCommentTextarea(driver).focus();hardWait(5);
		bp.enterCommentTextarea(driver).type(comments);hardWait(5);
		bp.postCommentButton(driver).click();hardWait(5);
	}
	public void deleteComment(WebDriver driver, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.deleteCommentButton(driver).click();hardWait(5);
		if(bp.deleteCommentConfirmButton(driver).isElementPresent(driver)){
			bp.deleteCommentConfirmButton(driver).click();hardWait(5);
		}
		else{
			Logger.info("Delete comment confirm notification is not found");
		}
	}
	
	public void shareSelectedItemByMail(WebDriver driver, String itemName, String emailId, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.selectItemCheckBox(driver, itemName).focus();
		bp.selectItemCheckBox(driver, itemName).click();hardWait(10);
		bp.shareSelectedItemButton(driver).click();hardWait(5);
		bp.emailTextBoxSelectedItemSharePopup(driver).type(emailId);hardWait(2);
		bp.sendButtonShareSelectedItemPopup(driver).click();hardWait(5);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void copySelectedItem(WebDriver driver, String itemName, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.selectItemCheckBox(driver, itemName).click();hardWait(5);
		bp.moveCopySelectedItemButton(driver).click();hardWait(5);
		bp.copyPopupButton(driver).click();hardWait(5);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void moveSelectedItem(WebDriver driver, String itemName,String destFolderName, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.selectItemCheckBox(driver, itemName).click();hardWait(5);
		bp.moveCopySelectedItemButton(driver).click();hardWait(5);
		bp.moveToFolderLink(driver, destFolderName).click();
		bp.movePopupButton(driver).click();hardWait(5);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void shareWithCollaborators(WebDriver driver, String itemName, String emailId, int waitTime){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		bp.moreActionMenu(driver, itemName).click();
		bp.sharingItemMenu(driver).waitForElementToBeVisible(driver);
		bp.sharingItemMenu(driver).click();hardWait(5);
		bp.inviteCollaboratorMenuItem(driver).waitForElementToBeVisible(driver);
		bp.inviteCollaboratorMenuItem(driver).click();hardWait(5);
		
//		bp.sharePopup(driver).focus();
		bp.shareWithCollabsEmailAddressTextBox(driver).type(emailId);hardWait(5);
		bp.sendInvitesButton(driver).waitForElementPresent(driver);
		bp.sendInvitesButton(driver).click();hardWait(waitTime);
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void deleteMultipleItems(WebDriver driver, ArrayList<String> fileNames){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		BoxPage bp =  AdvancedPageFactory.getPageObject(driver, BoxPage.class);
		for (String filename : fileNames) {
			if(bp.selectItemCheckBox(driver, filename).isElementPresent(driver)){
				bp.selectItemCheckBox(driver, filename).click();hardWait(5);
			}
		}
		bp.deleteSelectedButton(driver).waitForElementPresent(driver);
		bp.deleteSelectedButton(driver).click();hardWait(5);
		bp.deleteConfirmButton(driver).click();hardWait(5);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	/**
	 * This method copy the content of  source file and creates a file mentioned in destination
	 * @param source
	 * @param dest
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void copyFile(String sourcePath, String destPath)
			throws IOException, InterruptedException {
		
		File source = new File(sourcePath);
		File dest = new File(destPath);
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}
	
	/**
	 * This method deletes the files passed in the array
	 * @param myFiles
	 * @throws InterruptedException
	 */
	public void deleteFiles(ArrayList<File> myFiles) throws InterruptedException
	{
		
		if(myFiles.size()>=1){
		
			Reporter.log("-----------------------------",true);
			Reporter.log("Deleting temp files",true);
			Reporter.log("-----------------------------",true);
			int i=1;
		   for (File file : myFiles) {
				file.delete();

			    Reporter.log(i+") "+ file.getName(),true);
				i++;
		   }
			
			Reporter.log("-----------------------------",true);
		}
		
	}
	
	public String createFileForUpload(String fileName) throws IOException, InterruptedException{
		String newFileName=getTimestamp()+"_"+fileName;
		String oldFile= GatewayTestConstants.BOX_ORIGINAL_FILE_PATH+fileName;
		String newFile= GatewayTestConstants.BOX_ORIGINAL_FILE_PATH+newFileName;
		
		Logger.info("Filename: "+newFileName);
		Logger.info("newFile: "+newFile);
		//creating temp file with time stamp
		copyFile(oldFile,newFile);
		return newFileName;
		
	}
	
	
	
}