package com.elastica.pageobjects.azure;

import org.openqa.selenium.WebDriver;

import com.elastica.logger.Logger;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.CheckBox;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.SelectList;
import com.elastica.webelements.Element;
import com.elastica.webelements.TextBox;

public class AzurePage extends BasePage {
	public TextBox emailId(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox usernameBusiness(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox passwordBusiness(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button signinButtonBusiness(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button loginButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element  microsoftAccount(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element grantAccessIframe(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox signinbutton(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public TextBox uploadFileInput(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public TextBox uploadFilePassword(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public Element grantaccessbutton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public String grantaccessiframe(WebDriver driver){
		return getLocator();
	}
	
	public HyperLink adTitle(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink profileName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink profileSingout(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Element existingAccount(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public HyperLink sidebarLink(WebDriver driver, String linkName){
		return new HyperLink(driver,getLocator().replace("LINK_NAME", linkName));
	}
	public HyperLink linkInPage(WebDriver driver, String linkName){
		return new HyperLink(driver,getLocator().replace("LINK_NAME", linkName));
	}
	public HyperLink contextMenu(WebDriver driver, String vmName){
		return new HyperLink(driver,getLocator().replace("VM_NAME", vmName));
	}
	public HyperLink contextMenuItem(WebDriver driver, String action){
		Logger.info("Locator"+getLocator().replace("ACTION", action));
		return new HyperLink(driver,getLocator().replace("ACTION", action));
	}
	public Button cofirmMessageYesButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	public HyperLink contextMenuTopElement(WebDriver driver, String action){
		return new HyperLink(driver,getLocator().replace("ACTION", action));
	}
	public HyperLink accountLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink logoutLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink addItemLink(WebDriver driver){
		Logger.info("clicking on Add link"+getLocator());
		return new HyperLink(driver,getLocator());
	}
	public HyperLink addVMServerLink(WebDriver driver, String serverNameIconName){
		Logger.info("Locator"+getLocator().replace("SERVER_ICON_NAME", serverNameIconName));
		return new HyperLink(driver,getLocator().replace("SERVER_ICON_NAME", serverNameIconName));
	}
	public HyperLink selectVMServerLink(WebDriver driver,String serverType){
		Logger.info("Locator"+getLocator().replace("SERVER_TYPE", serverType));
		return new HyperLink(driver,getLocator().replace("SERVER_TYPE", serverType));
	}
	public SelectList selectDeploymentModelDropdown(WebDriver driver){
		return new SelectList(driver,getLocator());
	}
	public Button createButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	public TextBox formDetailsInputText(WebDriver driver, String fieldName){
		Logger.info("Locator"+getLocator().replace("FIELD_NAME", fieldName));
		return new TextBox(driver,getLocator().replace("FIELD_NAME", fieldName));
	}
	public Button createOKButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink notificationLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink notificationStatusCompleted(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink notificationStatusInprogress(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink vmSizeCodeLink(WebDriver driver,String sizeName){
		Logger.info("Locator"+getLocator().replace("SIZE_NAME", sizeName));
		return new HyperLink(driver,getLocator().replace("SIZE_NAME", sizeName));
	}
	public HyperLink OkOrSelectButton(WebDriver driver, String buttonName){
		Logger.info("Locator"+getLocator().replace("BUTTON_NAME", buttonName));
		return new HyperLink(driver,getLocator().replace("BUTTON_NAME", buttonName));
	}
	
	public HyperLink formDetailsDropdownArrowLink(WebDriver driver,String fieldName){
		Logger.info("Locator"+getLocator().replace("FIELD_NAME", fieldName));
		return new HyperLink(driver,getLocator().replace("FIELD_NAME", fieldName));
	}
	public HyperLink DropdownSelectItemLink(WebDriver driver, String selectedItem){
		Logger.info("Locator"+getLocator().replace("SELECTED_ITEM", selectedItem));
		return new HyperLink(driver,getLocator().replace("SELECTED_ITEM", selectedItem));
	}
	public HyperLink serviceItemLabel(WebDriver driver, String text){
		Logger.info("Locator"+getLocator().replace("TEXT", text));
		return new HyperLink(driver,getLocator().replace("TEXT", text));
	}
	public HyperLink formPageLink(WebDriver driver){
		Logger.info("Locator"+getLocator());
		return new HyperLink(driver,getLocator());
	}
	public HyperLink formPageBottomLink(WebDriver driver){
		Logger.info("Locator"+getLocator());
		return new HyperLink(driver,getLocator());
	}
	public HyperLink loginUserAnotherAccountLink(WebDriver driver){
		Logger.info("Locator"+getLocator());
		return new HyperLink(driver,getLocator());
	}
	public HyperLink useExistingRadioLink(WebDriver driver, String text){
		Logger.info("Locator"+getLocator().replace("RADIO_BUTTON_TEXT", text));
		return new HyperLink(driver,getLocator().replace("RADIO_BUTTON_TEXT", text));
	}
	public HyperLink settingsLink(WebDriver driver, String text){
		Logger.info("Locator"+getLocator().replace("LINK_NAME", text));
		return new HyperLink(driver,getLocator().replace("LINK_NAME", text));
	}
	public HyperLink elementInSettings(WebDriver driver, String text){
		Logger.info("Locator"+getLocator().replace("LINK_NAME", text));
		return new HyperLink(driver,getLocator().replace("LINK_NAME", text));
	}
	public HyperLink iconUnderSettings(WebDriver driver, String settingsName, String iconName){
		Logger.info("Locator"+getLocator().replace("SETTING_NAME", settingsName).replace("ICON_NAME", iconName));
		return new HyperLink(driver,getLocator().replace("SETTING_NAME", settingsName).replace("ICON_NAME", iconName));
	}
	
	public Button uploadButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	public Element certificateNameText(WebDriver driver, String text){
		return new Element(driver,getLocator().replace("TEXT",text));
	}
	public Button cofirmMessageYesButton2(WebDriver driver){
		return new Button(driver,getLocator());
	}
}