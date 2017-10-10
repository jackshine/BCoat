package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.elastica.logger.Logger;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.CheckBox;
import com.elastica.webelements.Element;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextArea;
import com.elastica.webelements.TextBox;

public class O365HomePage extends BasePage {
	
	public Button emailIcon(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button newemailbutton(WebDriver driver){
		Logger.info("Locator " + getLocator());
		return new Button(driver,getLocator());
	}
	
	public TextBox toList(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextArea ccList(WebDriver driver){
		return new TextArea(driver,getLocator());
	}
	
	public TextBox subject(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextArea body(WebDriver driver){
		return new TextArea(driver,getLocator());
	}
	
	public TextArea body1(WebDriver driver){
		return new TextArea(driver,getLocator());
	}

	public HyperLink onedriveIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink sharePointIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public Element spTeamSite(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public HyperLink sendButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink onedriveUploadLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink onedriveDownloadLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink selectAccessTypeDropown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink selectAccessInDropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink onedriveShareLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Button shareDialogCloseButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	public Button spUpload(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element spFrame(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public Element worldDocFrame(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public Element spUploadFileButton(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public Element spOk(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public Element newButton(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public Element newfolder(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public TextBox newFolderRenam(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public Element newWordDoc(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public Element newDocShare(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public Element siteShareLink(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public Element editSite(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public Element saveSite(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public TextBox newDocText(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public Element createButton(WebDriver driver){
		return new Element(driver,getLocator());
	}
	public TextBox onedriveUploadTextbox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public TextBox uploadButton(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public HyperLink o365EmailAttachLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Element o365EmailTopMenuButton(WebDriver driver, String menu){
		return new Element(driver, getLocator().replaceAll("MENU", menu));
	}
	
	public Element o365emailDiscardMessage(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element o365Emailattachfile(WebDriver driver, String File){
		return new Element(driver, getLocator().replaceAll("FILE", File));
	}
	
	public Element o365EmailAttachFileByCopy(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element o365emailattachfilefromcomputer(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element o365EmailAttachFileByonedrive(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element o365EmailAttachNextButton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public CheckBox onedriveItemListCheckbox(WebDriver driver, String index){
		return new CheckBox(driver,getLocator().replaceAll("INDEX", index));
	}
	
	public HyperLink onedriveFileName(WebDriver driver, String fileName){
		//Logger.info("Locator " + getLocator().replaceAll("FILENAME", fileName) );
		return new HyperLink(driver, getLocator().replaceAll("FILENAME", fileName));
	}
	
	public HyperLink onedrivedeletelink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public Button onedrivedeletebutton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element moreaction(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element moreactionsubmenu(WebDriver driver, String menu){
		return new Element(driver, getLocator().replace("MENU", menu));
	}
	
	public Element dialogboxinput(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element savebutton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element selectfolder(WebDriver driver, String folder) {
		return new Element(driver, getLocator().replace("FOLDER", folder));
	}
	
	public Element moveButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element copyButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element gotofolder(WebDriver driver, String folder) {
		return new Element(driver, getLocator().replace("FOLDER", folder));
	}
	
	public Element topmenu(WebDriver driver, String menu){
		return new Element(driver, getLocator().replace("MENU", menu));
	}
	
	public Element fileUploadSubmenu(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element folderUploadsubmenu(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element submenu(WebDriver driver, String menu) {
		return new Element(driver, getLocator().replace("MENU", menu));
	}
	
	public Element create(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element dialogboxinputgeneral(WebDriver driver, String input) {
		return new Element(driver, getLocator().replace("INPUT", input));
	}
	
	public Element dialogboxbuttongeneral(WebDriver driver, String activity) {
		return new Element(driver, getLocator().replace("ACTIVITY", activity));
	}
	
	public HyperLink selecteditem(WebDriver driver, String fileName){
		return new HyperLink(driver, getLocator().replaceAll("FILENAME", fileName));
	}
	
	public Element dialogsharedsidemenu(WebDriver driver, String menu) {
		return new Element(driver, getLocator().replace("MENU", menu));
	}
	public Element selectFile(WebDriver driver, String fileName) {
		//Logger.info(" locator "+getLocator().replace("FILE", fileName));
		return new Element(driver, getLocator().replace("FILE", fileName));
	}
	public Element spMenu(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	public Element spDownload(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	public Element spSelectFile(WebDriver driver,String fileName) {
		Logger.info(" locator "+getLocator().replace("FILE", fileName));
		return new Element(driver, getLocator().replace("FILE", fileName));
	}
	
	public Element spSelectFolder(WebDriver driver,String fileName) {
		Logger.info(" locator "+getLocator().replace("FILE", fileName));
		return new Element(driver, getLocator().replace("FILE", fileName));
	}
	public Element spFileActionMenu(WebDriver driver,String fileName) {
		//Logger.info(" locator "+getLocator().replace("FILE", fileName));
		return new Element(driver, getLocator().replace("FILE", fileName));
	}
	public Element spDelete(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	public Element spShare(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element spFolderShare(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public TextBox toShare(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public Element shareButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	public Element spFile(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	public Element saveAs(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	public Element reNameOption(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	public TextBox reNameText(WebDriver driver) {
		return new TextBox(driver, getLocator());
	}
	public Element reNameOkButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	public Element dialoguserid(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element dialogsharebutton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element dialogUserDropdown(WebDriver driver, String userId) {
		return new Element(driver, getLocator().replace("TITLE", userId));
	}
	
	public Element usernameapp(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element sidemenu(WebDriver driver, String menu) {
		return new Element(driver, getLocator().replace("MENU", menu));
	}
	
	public Element closedeletedialog(WebDriver driver) {
		return new Element(driver, getLocator());
	}

	public Element recycleselectallcheckbox(WebDriver driver) {
		return new Element(driver, getLocator());
	}

	public Element recycledialogdeletebutton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element recycleDialogSelectFirstItem(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element recycleDialogAscendingorder(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element recycleDialogRestoreButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element recycleDialogDeleteButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element emptyRecyleBinButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element dialogOnedriveCreate(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element dialogOnedriveInputFilename(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public HyperLink emailMoreOptions(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink saveDarftLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Element modulenIcon(WebDriver driver, String module) {
		return new Element(driver, getLocator().replaceAll("MODULE", module));
	}
	
	public Element sharegetlinktab(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element shareremovelink(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element sharebuttonunshare(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element sharerestrictedlink(WebDriver driver) {
		return new Element(driver, getLocator());
	}

	public Element sharewithviewlink(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element sharebuttonCloseShareDialog(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element topLabelFolder(WebDriver driver, String folder) {
		return new Element(driver, getLocator().replaceAll("FOLDER", folder));
	}
	
	public Element listView(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminSection(WebDriver driver, String section) {
		return new Element(driver, getLocator().replaceAll("MENU", section));
	}
	
	public Element adminInputUser(WebDriver driver, String title) {
		return new Element(driver, getLocator().replaceAll("TITLE", title));
	}
	
	public Element adminCreateUserbutton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminCloseButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public String adminIframe(WebDriver driver) {
		return getLocator();
	}
	
	public String adminPopupIframe(WebDriver driver) {
		return getLocator();
	}
	
	public String adminActiveUserSideIframe(WebDriver driver) {
		return getLocator();
	}
	
	public String adminIframeByName(WebDriver driver, String iframeName) {
		return getLocator().replaceAll("NAME", iframeName);
	}
	
	public Element adminActiveUserSelect(WebDriver driver, String user) {
		return new Element(driver, getLocator().replaceAll("USER", user));
	}
	
	public Element adminDeleteUserSelect(WebDriver driver, String user) {
		return new Element(driver, getLocator().replaceAll("USER", user));
	}

	public Element adminActiveUserSideMenu(WebDriver driver, String menu) {
		return new Element(driver, getLocator().replaceAll("MENU", menu));
	}
	
	public Element adminActiveUserSideDeleteButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminActiveUserSideCloseButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminActiveUserEditMenu(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminEditUserFirstnameInput(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminEditUserSaveButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public String adminEditUserIframe(WebDriver driver) {
		return getLocator();
	}
	
	public String adminCompanyIframe(WebDriver driver) {
		return getLocator();
	}
	
	public Element adminCompanyAddressInput(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminCompanyProfileSaveButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminActiveUserRestoreButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminRestoreNextPaging(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminResetPasswordButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element multipleFileDeleteButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element isFileFound(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element selectAllFilesIfInactive(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element dialogDeletebutton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element shareLinkDropdown(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element selectlist(WebDriver driver, String menu) {
		return new Element(driver, getLocator().replaceAll("MENU", menu));
	}
	
	public Element personalGetlink(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element personalEmail(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element personalEmailInput(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element personalInviteMessage(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element personalEmailShareButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element personalRemoveLink(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element personalRemoveLinkPopup(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element personalDetailsButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element personalInfoPanel(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element personalProfile(WebDriver driver) {
		Logger.info("Xpath " + getLocator());
		return new Element(driver, getLocator());
	}
	
	public Element personalSignout(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element oneDriveIconPersonal(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminChangeToOldStyle(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminRoleCheckBox(WebDriver driver, String role) {
		return new Element(driver, getLocator().replaceAll("ROLE", role));
	}
	
	public Element adminRoleAlternateEmail(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminRoleSaveButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminRoleCloseButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminPopupinput(WebDriver driver, String id) {
		return new Element(driver, getLocator().replaceAll("ID", id));
	}
	
	public Element adminPopupsave(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public String adminContactRightSideIframe(WebDriver driver) {
		return getLocator();
	}
	
	public Element adminContactDelete(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminContactCreate(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminPopup2Yes(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminWarningPopupButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminSideButton(WebDriver driver, String title) {
		return new Element(driver, getLocator().replaceAll("TITLE", title));
	}
	
	public Element adminCreateSharedEmail(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminPopupSearchInput(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminPopuDropdownList(WebDriver driver, String title) {
		return new Element(driver, getLocator().replaceAll("TITLE", title));
	}
	
	public Element adminSkypePublicAccessCheckbox(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminSkypePublicSaveButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminSkypePublicTextarea(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminskypepublictext(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminSiteAccesscheckbox(WebDriver driver) {
		return new Element(driver, getLocator());
	}
		
	public Element adminExternalShareSaveButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}	
	
	public Element adminSiteGiveAccess(WebDriver driver) {
		return new Element(driver, getLocator());
	}	
	
	public Element adminSiteNoAccess(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminSiteNoAccessPopupYes(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminSiteCheckboxDisable(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminCalenderAccessButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminCalenderAccessActiveCheckbox(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminCreateDomainButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminDomaininput(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminDomainGetStarted(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminDomainNextButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminDomainSelect(WebDriver driver, String title) {
		return new Element(driver, getLocator().replaceAll("TITLE", title));
	}
	
	public Element adminDomainDelete(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public String iframeById(WebDriver driver, String id) {
		return getLocator().replaceAll("ID", id);
	}
	
	public Element adminDomainRemoveYes(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminswayPublicOnToggleButton(WebDriver driver) {
		Logger.info("The path " + getLocator());
		return new Element(driver, getLocator());
	}
	
	public Element adminswayPublicOfftogglebutton(WebDriver driver) {
		Logger.info("The path " + getLocator());
		return new Element(driver, getLocator());
	}
	
	public Element adminSwayExternalSharingOnToggleButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminSwayExternalSharingOffToggleButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingEnablemobile(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingPopupCheckbox(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingPopupok(WebDriver driver) {
		Logger.info("The locator " + getLocator());
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingRemoveMobile(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingPopupYes(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingUserSoftwareOfficeCheckbox(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettinguserSoftwareLyncCheckbox(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingUserSoftwareSaveButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingPasswordCheckbox(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingPasswordSaveButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingCommunityOn(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingCommunityOff(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingCortanaOn(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminServiceSettingCortanaOff(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminExternalSharingOverviewSiteOn(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminExternalSharingOverviewPopupYes(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminExternalSharingOverviewSiteOff(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminExternalSharingOverviewSkypeForBusinessOn(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminExternalSharingOverviewSkypeForBusinessOff(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminExternalSharingOverviewIntegratedAppsOn(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminExternalSharingOverviewIntegratedAppsOff(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element o365emailcclist(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element o365emailsavedarftLink(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element o365emailtolist(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element o365emailMoreOptions(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailNewFolder(WebDriver driver) {
		Logger.info("Folder " +getLocator() );
		return new Element(driver, getLocator());
	} 
	
	public Element emailFolderPanel(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailInputFolder(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailSelectFolder(WebDriver driver, String folderName) {
		return new Element(driver, getLocator().replaceAll("FOLDER", folderName));
	} 
	
	public String emailSelectFolderStr(WebDriver driver, String folderName) {
		return  getLocator().replaceAll("FOLDER", folderName);
	} 
	
	
	public Element emailSelectFolderHighlight(WebDriver driver, String folderName) {
		return new Element(driver, getLocator().replaceAll("FOLDER", folderName));
	} 
	
	public Element emailDeleteHeader(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailBacktomainfolder(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailSelectDeleteFolder(WebDriver driver, String folderName) {
		return new Element(driver, getLocator().replaceAll("FOLDER", folderName));
	} 
	
	public Element emailSelectmenu(WebDriver driver, String menu) {
		return new Element(driver, getLocator().replaceAll("MENU", menu));
	} 
	
	public Element emailPopupOkButton(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailFolderMoreButton(WebDriver driver) {
		return new Element(driver, getLocator());
	} 

	public Element emailBackModernNavigationButton(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailRenameFolder(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailSelectSubfolder(WebDriver driver, String folderName) {
		return new Element(driver, getLocator().replaceAll("FOLDER", folderName));
	} 
	
	public String emailSelectSubfolderStr(WebDriver driver, String folderName) {
		return  getLocator().replaceAll("FOLDER", folderName);
	} 
	
	public Element emailSelectSubfolderTitle(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailselectsubfolderdownarrow(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	
	public Element emailInputSubfolder(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailMovePopupFolderSelect(WebDriver driver, String folderName) {
		return new Element(driver, getLocator().replaceAll("FOLDER", folderName));
	} 
	
	public Element emailMovePopupFolderMove(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailSelectSubfolderByParent(WebDriver driver, String parentFolder, String folderName) {
		return new Element(driver, getLocator().replaceAll("PARENT", parentFolder).replaceAll("FOLDER",  folderName));
	} 
	
	public Element emailFoldersExpand(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailSearchBox(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailSearchInputbox(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element emailSearchMailBySubject(WebDriver driver, String subject) {
		return new Element(driver, getLocator().replaceAll("SUBJECT",  subject));
	} 
	
	public Element emailAttachDownloadFile(WebDriver driver, String file) {
		return new Element(driver, getLocator().replaceAll("FILE",  file));
	} 
	
	public Element emailAttachedDownloadButon(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalEmailSelectByTitle(WebDriver driver, String title) {
		return new Element(driver, getLocator().replaceAll("TITLE", title));
	} 
	
	public Element personalEmailBody(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalEmailNewButton(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalMoreOptions(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalSubMenu(WebDriver driver, String title) {
		return new Element(driver, getLocator().replaceAll("TITLE", title));
	} 
	
	
	public Element personalEmailMenu(WebDriver driver, String title) {
		return new Element(driver, getLocator().replaceAll("TITLE", title));
	} 
	
	public Element personalAttachasCopy(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalmoreoption(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalsavedraft(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element createSite(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public TextBox inputSiteName(WebDriver driver) {
		return new TextBox(driver, getLocator());
	}
	public Element createSiteButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	public Element newSubSite(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element subSiteTitle(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public TextArea subSiteDescription(WebDriver driver){
		return new TextArea(driver,getLocator());
	}
	
	public TextBox subSiteAddress(WebDriver driver) {
		return new TextBox(driver, getLocator());
	}
	
}