package com.elastica.pageobjects.audit;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.ElementList;
import com.elastica.webelements.HyperLink;

/**
 * Audit page
 * @author Eldo Rajan
 *
 */
public class AuditPage extends BasePage{

	public HyperLink auditheader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditvideolog(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditicon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public ElementList<Element> auditsidebaroptionlist(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public HyperLink hideoverviewvideo(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink showoverviewvideo(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public ElementList<Element> audittab(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> audittabtext(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public Button audittimeperiodselectorbutton(WebDriver driver){
		return new Button(driver,getLocator());
	}

	public HyperLink audittimeperiodselectorbuttonvalue(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink audittimeperiodselectorbuttonvalueothertabs(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public ElementList<Element> audittimeperiodselectordropdown(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink audittimeperiodselectorLast7Daysdropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink audittimeperiodselectorLastDaydropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink audittimeperiodselectorLastCalendarWeekdropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink audittimeperiodselectorLast30Daysdropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink audittimeperiodselectorLastCalendarMonthdropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink audittimeperiodselectorLast12Monthsdropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}


	public HyperLink auditscorecount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditsaasservicescount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditriskpercentagecount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditriskservicescount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditusercount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditdestinationcount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public ElementList<Element> auditsummarytabservicestableelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> auditsummarytabservicescolumntableelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> auditsummarytabservicesnumbertableelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> auditsummarytabservicestitletableelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> auditsummarytabservicestitlenametableelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> auditsummarytabservicestitledescriptiontableelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> auditsummarytabservicesriskscoretableelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> auditsummarytabservicesuserstableelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public Button audittopoptionselectorbutton(WebDriver driver){
		return new Button(driver,getLocator());
	}

	public HyperLink audittopoptionselectorriskyservicesdropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink audittopoptionselectorusedservicesdropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditsummarydetailpanelappname(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditsummarydetailpanelappsubheading(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public ElementList<Element> auditsummarydetailpaneltopusers(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditsummarydetailpaneltopusersip(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditsummarydetailpaneltopuserssession(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditsummarydetailpaneltopusersdataconsumed(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink auditsummarydetailpaneldestinationheader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditsummarydetailpaneldestinationcountrycount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditsummarydetailpaneldestinationcitycount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public ElementList<Element> auditservicetabtopelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtablerowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtableratingrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtablenamerowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtablenametitlerowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtablenamedescriptionrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtablesessionsrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtableuploadrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtabledownloadrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtableusersrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtabledestinationrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtableplatformrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditservicetabtableavgdurationrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> auditusertabtablerowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditusertabtableratingrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditusertabtablenamerowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditusertabtablesessionsrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditusertabtableuploadrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditusertabtabledownloadrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditusertabtableusersrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditusertabtabledestinationrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}



	public ElementList<Element> auditlocationtabtablerowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditlocationtabtableratingrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditlocationtabtablenamerowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditlocationtabtablesessionsrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditlocationtabtableuploadrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditlocationtabtabledownloadrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditlocationtabtableusersrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditlocationtabtabledestinationrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditlocationtabtableplatformrowelement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public HyperLink auditsummarySpiderChart(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditmapChart(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditsummarySpiderChartTable(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public ElementList<Element> auditsummarySpiderChartLineCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditsummarySpiderChartTableLineCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditsummarySpiderChartTableLineName(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditsummarySpiderChartTableLineNumber(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditsummaryTopServiceCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageSourcesRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> SourcesPageGateletsSourcesRowName(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowRecord(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowRecordCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowUser(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowUserCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowviolations(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowviolationsCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public HyperLink auditsummaryTopServiceNumber(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	public HyperLink auditsummaryTopServiceName(WebDriver driver,int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	public HyperLink auditsummaryTopServiceRiskScore(WebDriver driver,int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	public HyperLink auditsummaryTopServiceUserCount(WebDriver driver,int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	public HyperLink auditBarChart(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditExportButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServiceLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServiceVisibilityLable(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServiceVisibilityIgnoreLable(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServiceTableHeadingActions(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersTag(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditClearFiltersBtn(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersServiceTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersCategoriesTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersDefaultTagsTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersCustomTagsTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersRiskTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersUsersTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersCountriesTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditFiltersDropDown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersCitiesTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersPlatformsTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public ElementList<Element> auditFiltersServiceRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditFiltersCategoriesRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditFiltersDefaultTagsRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditFiltersCustomTagsRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditRiskTabRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditUsersTabRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditCountriesTabRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditCitiesTabRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> auditPlatformsRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink auditSaaSSERVICESCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditSaaSSERVICESCountInTableHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersServiceRowFirstCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersServiceRowFirstCheckBoxText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersCategoriesRowFirstCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersCategoriesRowFirstCheckBoxText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditFiltersCustomTagsRowFirstCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersCustomTagsRowFirstCheckBoxText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditFiltersDefaultTagsRowFirstCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersDefaulTagsRowFirstCheckBoxText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersRiskTabFirstCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditFiltersRiskTabFirstCheckBoxText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditFiltersUsersTabFirstCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersUsersTabFirstCheckBoxText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}


	public HyperLink auditFiltersCountriesTabFirstCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersCountriesTabFirstCheckBoxText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditFiltersCitiesTabFirstCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersCitiesTabFirstCheckBoxText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersPlatformsTabFirstCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersPlatformsTabFirstCheckBoxText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}


	public HyperLink auditServicesTableActionComments(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServicesTableActionSelect(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServicesTableCommentsTextBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServicesTableAddCommentBtn(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServicesTableAddCommentSubject(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditFiltersSelectTag(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditClearFiltersValue(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditClearFilters(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditDataSourcesSourceSelected(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditServicesServiceDetailsTableFirstUserName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink auditServicesServiceDetailsTableFirstUserNameSelect(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServicesServiceDetailsH1(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServicesServiceDetailsH2(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink clickAdminDCIAdmin(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink clickSettings(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink clickTags(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SettingsNewTag(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SettingsTagText(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SettingsSaveTag(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink SelectDropDownTabClicked(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink EditTabClicked(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink messageBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SettingsTagFirstName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink TagCounntNumber(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink EditLabelBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink BeforeEditLabel(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}


	public HyperLink SettingsTagFirstCreated(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SettingsTagFirstModify(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServicesTableFirstServicesName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServicesServiceDetailsTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditServicesServiceDetailsSearchBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink Configure(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

}

