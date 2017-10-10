package com.elastica.pageobjects.box;

import org.openqa.selenium.WebDriver;

import com.elastica.logger.Logger;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.CheckBox;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.SelectList;
import com.elastica.webelements.Element;
import com.elastica.webelements.TextBox;

public class BoxPage extends BasePage {
	public TextBox usernameTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox passwordTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button signInButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}

	public Button uploadButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	
	public TextBox uploadFiles(WebDriver driver){
		return new TextBox(driver, getLocator()); 
	}
	
	public Button uploadButtonPopup(WebDriver driver){
		return new Button(driver, getLocator()); 
	}

	public Button newButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public HyperLink userMenuDropDown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink logOutLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public Button createFolderButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	public HyperLink popUpWindow(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public Element uploadFilesOverlay(WebDriver driver) {
		return new Element(driver, getLocator()); 

	}
	
	public TextBox popUpFolderNameTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button popUpOkayButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public HyperLink itemNameLink(WebDriver driver, String itemName){
		return new HyperLink(driver,getLocator().replaceAll("NAME", itemName));
	}
	
	public HyperLink folderOptionsButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink deleteLinkInFolderOptions(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Button deleteConfirmButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	
	public HyperLink closePreviewLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public Button deleteSelectedButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public HyperLink selectItemCheckBox(WebDriver driver, String itemName){
		Logger.info("element="+getLocator().replaceAll("NAME", itemName));
		return new HyperLink(driver, getLocator().replaceAll("NAME", itemName)); 
	}
	public HyperLink homepageLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink downloadItemMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public Button downloadSelectedButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}

	public HyperLink selectAllCheckBoxTopElement(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink moreActionMenu(WebDriver driver, String itemName){
		return new HyperLink(driver, getLocator().replaceAll("NAME", itemName)); 
	}
	public HyperLink sharingItemMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink shareLinkItemMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink shareEmbedWidgetItemMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink sharePopup(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public TextBox shareEmailAddressTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public Button sendButtonInSharePopUp(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	
	public HyperLink shareLinkTopElement(WebDriver driver, String itemName){
		Logger.info("locator="+getLocator().replaceAll("NAME", itemName));
		return new HyperLink(driver,getLocator().replaceAll("NAME", itemName)); 
	}
	
	public Button shareChangeMenu(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Button unshareRemoveLink(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Button ConfirmOkay(WebDriver driver){
		return new Button(driver, getLocator()); 
	}

	public HyperLink newBoxNoteMenuLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink newBookmarkMenuLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink newWordDocMenuLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink newPowerPointDocMenuLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink newExcelDocMenuLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink newGoogleDocMenuLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink newGoogleSheetMenuLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink newItemAddDescLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public TextBox newItemNameTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public TextBox newItemUrlTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public TextBox newItemDescTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public TextBox newItemDescTextArea(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button newItemPopupOkayButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public HyperLink unlockItemMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public Button launchingPopupCloseButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	
	public Button tagSelectedItemButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	
	public TextBox tagNameTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public HyperLink lockItemMenu(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public Button lockItemPopupContinueButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	
	public CheckBox lockItemPopupPreventDownloadCheckbox(WebDriver driver){
		return new CheckBox(driver, getLocator()); 
	}
	public CheckBox lockItemPopupSetDurationCheckbox(WebDriver driver){
		return new CheckBox(driver, getLocator()); 
	}
	public SelectList lockItemPopupDurationDropdown(WebDriver driver){
		return new SelectList(driver, getLocator()); 
	}
	public Button postCommentButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Button deleteCommentButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public TextBox enterCommentTextarea(WebDriver driver){
		Logger.info("textare loc="+getLocator());
		return new TextBox(driver,getLocator());
	}
	public Button deleteCommentConfirmButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Button sharePeopleInYourCompany(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Button sharePeopleInThisFolder(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Button sharePeopleWithLink(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Element errorNotificationDiv(WebDriver driver){
		return new Element(driver, getLocator()); 
	}
	public Button errorNotificationCloseButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public HyperLink propertiesItemMenu(WebDriver driver){
		return new HyperLink(driver, getLocator()); 
	}
	public Button shareSelectedItemButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Button moveCopySelectedItemButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public TextBox emailTextBoxSelectedItemSharePopup(WebDriver driver){
		return new TextBox(driver, getLocator()); 
	}
	public Button sendButtonShareSelectedItemPopup(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Button movePopupButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public Button copyPopupButton(WebDriver driver){
		return new Button(driver, getLocator()); 
	}
	public HyperLink moveToFolderLink(WebDriver driver, String folderName){
		Logger.info("locator="+getLocator().replace("NAME", folderName));
		return new HyperLink(driver, getLocator().replace("NAME", folderName)); 
	}
	
	public HyperLink inviteCollaboratorMenuItem(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public TextBox shareWithCollabsEmailAddressTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public Button sendInvitesButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	public HyperLink boxLogo(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	
					
	
	
	
}