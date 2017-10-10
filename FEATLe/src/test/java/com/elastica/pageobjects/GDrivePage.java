package com.elastica.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.elastica.logger.Logger;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

public class GDrivePage extends BasePage{
	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public Button nextbutton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public TextBox password(WebDriver driver) {
		return new TextBox(driver,getLocator());
	}
	
	public Button signin(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink userProfile(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink logout(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public String selectDoc(WebDriver driver, String fileName){
		Logger.info("The Xpath of File/Folder: " + getLocator().replaceAll("FILENAME", fileName));
		return getLocator().replaceAll("FILENAME", fileName);
	}
	
	public String getIdFolder(WebDriver driver, String fileName){
		return getLocator().replaceAll("FILENAME", fileName);
	}
	
	public Element toplabel(WebDriver driver, String menu){
		return new Element(driver, getLocator().replace("LABEL", menu));
	}
	
	public String selectdoccontains(WebDriver driver, String fileName){
		return getLocator().replaceAll("FILENAME", fileName);
	}
	
	public Element moreAction(WebDriver driver) {
		return new Element(driver,getLocator());
	}
	
	public Element downloadFile(WebDriver driver){
		return new Element(driver,downloadFileLocotor(driver));
	}
	
	public Element topMenu(WebDriver driver, String menu){
		return new Element(driver, getLocator().replace("MENU", menu));
	}
	
	public Element topMenuBreadCrumb(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public String downloadFileLocotor(WebDriver driver){
		return getLocator();
	}
	
	public Element dialogDownloadFile(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element dialogClose(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public TextBox uploadFile(WebDriver driver){
		return new TextBox(driver, getLocator());
	}
	
	public Button newButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element dialogMoreActions(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element rightClickSubmenu(WebDriver driver, String menu){
		return new Element(driver, getLocator().replace("MENU", menu));
	}
	
	public Element moveSubmenu(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element clickDownloadSubmenu(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public TextBox renameTextbox(WebDriver driver) {
		return new TextBox(driver,getLocator());
	}
	
	public Button renameOkButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element docFileNameTextbox(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element docShareButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element shareEmailTextbox(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Button shareOkButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button yesDialogbutton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element sidebutton(WebDriver driver, String menu){
		return new Element(driver, getLocator().replace("MENU", menu));
	}
	
	public Element shareSettingLink(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element publicShareOffLink(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element publicShareOnLink(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element canviewdropdown(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element noShare(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element sharepublic(WebDriver driver, String permission){
		return new Element(driver, getLocator().replace("PERMISSION", permission));
	}
	
	public String shareWithOtherIframe(WebDriver driver){
		return getLocator();
	}
	
	public Element fileInput(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element fileViewOption(WebDriver driver, String index){
		Logger.info("the get locator " + getLocator().replace("INDEX", index));
		return new Element(driver, getLocator().replace("INDEX", index));
	}
	
	public Element moveFoldername(WebDriver driver, String folderName){
		Logger.info("the get locator " + getLocator().replace("FOLDER", folderName));
		return new Element(driver, getLocator().replace("FOLDER", folderName));
	}

	public Element moveButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element backbutton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element alertOkButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	
	public Element createButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element createDraft(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element composeButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element toField(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element selectFirstUserInTooltip(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element subjectField(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element selectMail(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element deleteMailInboxButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element selectMailToDelete(WebDriver driver){
		List<WebElement> allLinks=null;
		return new Element(driver, getLocator());
		//return new Element(driver, getLocator().replace("subject", subject));
		//return new findElements(driver, getLocator());
	}
	
	

	public Element sentMailTab(WebDriver driver){
		return new Element(driver, getLocator());
	}

	public Element sentMailDelete(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element bodyField(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element sendButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element createNewButtonMenu(WebDriver driver, String menu){
		return new Element(driver, getLocator().replace("MENU", menu));
	}
	
	public Element dialogfilenametext(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element dialogCreatebutton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element dialogDeleteforeverbutton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element dialogPermissionDropdown(WebDriver driver){
		return new Element(driver, getLocator());
	}

	public Element dialogPermission(WebDriver driver, String permission){
		Logger.info("xpath " + getLocator().replace("PERMISSION", permission));
		return new Element(driver, getLocator().replace("PERMISSION", permission) );
	}
	
	public Element downloadAnywayButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element linksharingoff(WebDriver driver){
		return new Element(driver, getLocator());
	}

	public Element linksharingon(WebDriver driver){
		return new Element(driver, getLocator());
	}

	public Element gmailSelectFileFromGdrive(WebDriver driver, String fileName){
		return new Element(driver, getLocator().replace("FILENAME", fileName));
	}
	
	public Element gmailInsertFileGdriveLogo(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element gmailInsertFileGdriveLocal(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public String gmailFileAttachIframe(WebDriver driver){
		return getLocator();
	}

	public Button gmailAttachInsertButton(WebDriver driver){
		return new Button(driver, getLocator());
	}

	public Button gmailAttachedButton(WebDriver driver){
		return new Button(driver, getLocator());
	}
	
	public String gmailPopupShareIframe(WebDriver driver){
		return getLocator();
	}
	
	public Element gmailPopupShareButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element gmailSidemenu(WebDriver driver, String title){
		return new Element(driver, getLocator().replace("TITLE", title));
	}
	
	public Element gmailSelectFirstEmailBySubject(WebDriver driver, String subject){
		return new Element(driver, getLocator().replace("SUBJECT", subject));
	}
	
	public Element gmailPreviewDownloadFile(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element gmailDownloadFile(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element gmailTopMenu(WebDriver driver, String title){
		return new Element(driver, getLocator().replace("MENU", title));
	}
	
	public Element gmailSelectFirstEmailCheckbox(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public String gmailselectfirstemailstr(WebDriver driver){
		return getLocator();
	}
	
	public Element gmailPopupOkButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element gmailPopupDeleteButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element gmailSeachBox(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element gmailSearchbutton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element calenderSearch(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element calenderSearchButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element calenderEventButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element calenderTitle(WebDriver driver, String title) {
		return new Element(driver, getLocator().replace("TITLE", title));
	}
	
	public Element calenderDownloadAttached(WebDriver driver, String file){
		return new Element(driver, getLocator().replace("FILE", file));
	}
	
	public Element calenderUploadfile(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element calenderAttachSendButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element calenderSave(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element calenderRemoveFile(WebDriver driver, String file){
		return new Element(driver, getLocator().replace("FILE", file));
	}
	
	
	
	
	
}