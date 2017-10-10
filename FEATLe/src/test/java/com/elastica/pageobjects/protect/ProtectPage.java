package com.elastica.pageobjects.protect;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Element;
import com.elastica.webelements.ElementList;
import com.elastica.webelements.HyperLink;

/**
 * Protect page
 * @author Eldo Rajan
 *
 */
public class ProtectPage extends BasePage{

	public HyperLink header(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink activeTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink noDataHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink activityLogCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink newTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink policy(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink cancel(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink savePolicy(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink defaultpolicytype(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink clickpolicytype(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink expandpolicydropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink clickpolicydropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink selectedpolicytype(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink firstDefinereponse(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink firstDefineRules(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public ElementList<Element> policyTypedropdown(WebDriver driver){
		return new ElementList<Element>(driver,  getLocator());
	}
	public ElementList<Element> policyTypelist(WebDriver driver){
		return new ElementList<Element>(driver,  getLocator());
	}
	public ElementList<Element> policyTypeSize(WebDriver driver){
		return new ElementList<Element>(driver,  getLocator());
	}
	
	public ElementList<Element> policyTypedropdownlist(WebDriver driver){
		return new ElementList<Element>(driver,  getLocator());
	}
	public HyperLink firstpolicyTypedropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink firstpolicydropdowntest(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink logviolationtext(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink protectToggleTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink accessmonitor(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink dataexplore(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink threadscore(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink responseType(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink policyType(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink policyTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink actionDropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink showdetail(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink editdetail(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink deletedetail(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink selectDataExposureviaSecurlets(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink fileOwnerSelective(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink fileOwnerSelectiveText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink fileOwnerName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink clearselection(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink clickFileOwnerName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink policyName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink cloudServiceDropdownlist(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink selectBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink selectinternalCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink savePolicyButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink alert(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink successAlert(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink selectFileTypes(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink filetypeSelectiveText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink selectFileSizeAny(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink selectFileSizeSelective(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink FileSizeBar(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink barLargerThan(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink barSmallerThan(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink filterInactive(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink filteractive(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink caret(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink filtertext(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink BlockedUsersTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink BlockStatus(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
}
