package com.elastica.pageobjects.store;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.CheckBox;
import com.elastica.webelements.Element;
import com.elastica.webelements.ElementList;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

/**
 * Store page
 * @author Eldo Rajan
 *
 */
public class StorePage extends BasePage{

	public HyperLink header(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletSection(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletSectionHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletSectionSeeAllLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletSectionAllTiles(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public ElementList<Element> securletSectionTileElement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> securletSectionTileLinkElement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> securletSectionTileImageElement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> securletSectionTileTitleElement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> securletSectionTileDescriptionElement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> securletSectionTileMoreActionsElement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public HyperLink securletSectionAppAdded(WebDriver driver, int appNo){
		return new HyperLink(driver,getLocator().replace("TILE_NO", Integer.toString(appNo)));
	}

	public HyperLink securletSectionTileTitle(WebDriver driver, int appNo){
		return new HyperLink(driver,getLocator().replace("TILE_NO", Integer.toString(appNo)));
	}

	public HyperLink securletHeaderDetailedView(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletConfigureButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink securletCancelButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink securletDeactivateButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink securletDeactivateButton1(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public CheckBox securletPurgeDataButton(WebDriver driver){
		return new CheckBox(driver,getLocator());
	}
	
	public CheckBox securletPurgeDataButton1(WebDriver driver){
		return new CheckBox(driver,getLocator());
	}

	public HyperLink securletPurgeDataText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink securletPurgeDataText1(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink deactivateDialogBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink deactivateDialogBoxTextOne(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink deactivateDialogBoxTextTwo(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink deactivateDialogBoxCancelButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink deactivateDialogBoxRemoveButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink deactivateAlertBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink deactivateAlertBoxCloseButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink deactivateAlertBoxMessage(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletActivateButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletScanContainerImage(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletScanContainerHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletScanContainerFullScanOption(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletScanContainerSelectiveScanOption(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}	

	public HyperLink securletScanContainerFullScanOptionText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletScanContainerSelectiveScanOptionText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletScanContainerSelectiveScanOptionMessageText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletScanContainerMessage(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletScanContainerCancelButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink securletScanContainerActivateButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	
	public TextBox securletDomainNameTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox boxUsernameTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox boxPasswordTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public Button boxAuthorizeButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button boxGrantAccessButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button boxDenyAccessButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public TextBox dropboxUsernameTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox dropboxPasswordTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public Button dropboxAuthorizeButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button storeAuthorizationSaveButton(WebDriver driver){
		return new Button(driver,getLocator());
	}

	public HyperLink googleIframe(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink googleIframe2(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink googleInstallAppButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink googleInstallAppSaveButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public TextBox googleUsernameTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox googlePasswordTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public Button googleNextButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button googleSignInButton(WebDriver driver){
		return new Button(driver,getLocator());
	}

	public HyperLink ActivateButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink DeactivateButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink ConfigureButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Button AccountInformationButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public ElementList<Element> AccountInformationDropdown(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public Button SecurletDashboardSelectedAccount(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public ElementList<Element> AccountSelectorButton(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public Button SalesforceEditionSelectionDropdownButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public ElementList<Element> SalesforceEditionSelectionDropdownList(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public TextBox SalesforceAccountName(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox SalesforceSandboxName(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public ElementList<Element> SalesforceTabPanelDetailsLabel(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> SalesforceTabPanelDetailsDescription(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public Button AccountInformationDropdownButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink AccountInformationRegisterAccountLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Button SalesforceRegisterAccountButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public TextBox SalesforceLoginUsername(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox SalesforceLoginPassword(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button SalesforceLoginButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button SalesforceUpgradeButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button SalesforceDoneButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink SalesforceNamePrefix(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink SalesforceAllTabs(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public ElementList<Element> SalesforceElasticaSecurletSetup(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink SalesforceConfigureLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public TextBox SalesforceVerificationCodeTextbox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button SalesforceVerificationCodeVerifyButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button DeleteAccountButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
}
