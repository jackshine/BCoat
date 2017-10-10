package com.elastica.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.elastica.logger.Logger;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

public class DropboxPage extends BasePage {
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
	
	public Element logoutOption(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Element logout(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public String selectDoc(WebDriver driver, String fileName){
		return getLocator().replaceAll("FILENAME", fileName);
	}
	
	public String selectdoccontains(WebDriver driver, String fileName){
		return getLocator().replaceAll("FILENAME", fileName);
	}
	
	public Element moreAction(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public HyperLink downloadFile(WebDriver driver){
		return new HyperLink(driver,getLocator());
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
	
	public Element uploadFileButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element basicUploader(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element fileBox(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element uploadSubmit(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Button newButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element deleteFile(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public Element clickAway(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public Element dialogMoreActions(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element rightClickSubmenu(WebDriver driver, String menu){
		return new Element(driver, getLocator().replace("MENU", menu));
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
	public Element createfolder(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element newfolder(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element publicShare(WebDriver driver,String file){
		return new Element(driver, getLocator().replace("File", file));
	}
	
	public Element shareButton(WebDriver driver,String file){
		return new Element(driver, getLocator().replace("File", file));
	}
	public Element createlink(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element linkSetting(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element closeLink(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element deleteLink(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element deleteButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element closeButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element popupWindow(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element closePopUp(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element publicUnshare(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element removeLink(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element linkButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	public Element canviewdropdown(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element noShare(WebDriver driver){
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
	
	public Element dialogDeleteButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element dialogPermissionDropdown(WebDriver driver){
		return new Element(driver, getLocator());
	}

	public Element dialogPermission(WebDriver driver, String permission){
		return new Element(driver, getLocator().replace("PERMISSION", permission) );
	}

	
}
