package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Result {

	@JsonProperty("__metadata")
	private com.universal.dtos.onedrive.Metadata Metadata;
	@JsonProperty("FirstUniqueAncestorSecurableObject")
	private com.universal.dtos.onedrive.FirstUniqueAncestorSecurableObject FirstUniqueAncestorSecurableObject;
	@JsonProperty("RoleAssignments")
	private com.universal.dtos.onedrive.RoleAssignments RoleAssignments;
	@JsonProperty("ContentTypes")
	private com.universal.dtos.onedrive.ContentTypes ContentTypes;
	@JsonProperty("CreatablesInfo")
	private com.universal.dtos.onedrive.CreatablesInfo CreatablesInfo;
	@JsonProperty("DefaultView")
	private com.universal.dtos.onedrive.DefaultView DefaultView;
	@JsonProperty("DescriptionResource")
	private com.universal.dtos.onedrive.DescriptionResource DescriptionResource;
	@JsonProperty("EventReceivers")
	private com.universal.dtos.onedrive.EventReceivers EventReceivers;
	@JsonProperty("Fields")
	private com.universal.dtos.onedrive.Fields Fields;
	@JsonProperty("Forms")
	private com.universal.dtos.onedrive.Forms Forms;
	@JsonProperty("InformationRightsManagementSettings")
	private com.universal.dtos.onedrive.InformationRightsManagementSettings InformationRightsManagementSettings;
	@JsonProperty("Items")
	private com.universal.dtos.onedrive.Items Items;
	@JsonProperty("ParentWeb")
	private com.universal.dtos.onedrive.ParentWeb ParentWeb;
	@JsonProperty("RootFolder")
	private com.universal.dtos.onedrive.RootFolder RootFolder;
	@JsonProperty("TitleResource")
	private com.universal.dtos.onedrive.TitleResource TitleResource;
	@JsonProperty("UserCustomActions")
	private com.universal.dtos.onedrive.UserCustomActions UserCustomActions;
	@JsonProperty("Views")
	private com.universal.dtos.onedrive.Views Views;
	@JsonProperty("WorkflowAssociations")
	private com.universal.dtos.onedrive.WorkflowAssociations WorkflowAssociations;
	@JsonProperty("AllowContentTypes")
	private boolean AllowContentTypes;
	@JsonProperty("BaseTemplate")
	private long BaseTemplate;
	@JsonProperty("BaseType")
	private long BaseType;
	@JsonProperty("ContentTypesEnabled")
	private boolean ContentTypesEnabled;
	@JsonProperty("CrawlNonDefaultViews")
	private boolean CrawlNonDefaultViews;
	@JsonProperty("Created")
	private String Created;
	@JsonProperty("DefaultContentApprovalWorkflowId")
	private String DefaultContentApprovalWorkflowId;
	@JsonProperty("Description")
	private String Description;
	@JsonProperty("Direction")
	private String Direction;
	@JsonProperty("DocumentTemplateUrl")
	private Object DocumentTemplateUrl;
	@JsonProperty("DraftVersionVisibility")
	private long DraftVersionVisibility;
	@JsonProperty("EnableAttachments")
	private boolean EnableAttachments;
	@JsonProperty("EnableFolderCreation")
	private boolean EnableFolderCreation;
	@JsonProperty("EnableMinorVersions")
	private boolean EnableMinorVersions;
	@JsonProperty("EnableModeration")
	private boolean EnableModeration;
	@JsonProperty("EnableVersioning")
	private boolean EnableVersioning;
	@JsonProperty("EntityTypeName")
	private String EntityTypeName;
	@JsonProperty("FileSavePostProcessingEnabled")
	private boolean FileSavePostProcessingEnabled;
	@JsonProperty("ForceCheckout")
	private boolean ForceCheckout;
	@JsonProperty("HasExternalDataSource")
	private boolean HasExternalDataSource;
	@JsonProperty("Hidden")
	private boolean Hidden;
	@JsonProperty("Id")
	private String Id;
	@JsonProperty("ImageUrl")
	private String ImageUrl;
	@JsonProperty("IrmEnabled")
	private boolean IrmEnabled;
	@JsonProperty("IrmExpire")
	private boolean IrmExpire;
	@JsonProperty("IrmReject")
	private boolean IrmReject;
	@JsonProperty("IsApplicationList")
	private boolean IsApplicationList;
	@JsonProperty("IsCatalog")
	private boolean IsCatalog;
	@JsonProperty("IsPrivate")
	private boolean IsPrivate;
	@JsonProperty("ItemCount")
	private long ItemCount;
	@JsonProperty("LastItemDeletedDate")
	private String LastItemDeletedDate;
	@JsonProperty("LastItemModifiedDate")
	private String LastItemModifiedDate;
	@JsonProperty("ListItemEntityTypeFullName")
	private String ListItemEntityTypeFullName;
	@JsonProperty("MajorVersionLimit")
	private long MajorVersionLimit;
	@JsonProperty("MajorWithMinorVersionsLimit")
	private long MajorWithMinorVersionsLimit;
	@JsonProperty("MultipleDataList")
	private boolean MultipleDataList;
	@JsonProperty("NoCrawl")
	private boolean NoCrawl;
	@JsonProperty("ParentWebUrl")
	private String ParentWebUrl;
	@JsonProperty("ParserDisabled")
	private boolean ParserDisabled;
	@JsonProperty("ServerTemplateCanCreateFolders")
	private boolean ServerTemplateCanCreateFolders;
	@JsonProperty("TemplateFeatureId")
	private String TemplateFeatureId;
	@JsonProperty("Title")
	private String Title;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The Metadata
	 */
	@JsonProperty("__metadata")
	public com.universal.dtos.onedrive.Metadata getMetadata() {
		return Metadata;
	}

	/**
	 * 
	 * @param Metadata
	 * The __metadata
	 */
	@JsonProperty("__metadata")
	public void setMetadata(com.universal.dtos.onedrive.Metadata Metadata) {
		this.Metadata = Metadata;
	}

	/**
	 * 
	 * @return
	 * The FirstUniqueAncestorSecurableObject
	 */
	@JsonProperty("FirstUniqueAncestorSecurableObject")
	public com.universal.dtos.onedrive.FirstUniqueAncestorSecurableObject getFirstUniqueAncestorSecurableObject() {
		return FirstUniqueAncestorSecurableObject;
	}

	/**
	 * 
	 * @param FirstUniqueAncestorSecurableObject
	 * The FirstUniqueAncestorSecurableObject
	 */
	@JsonProperty("FirstUniqueAncestorSecurableObject")
	public void setFirstUniqueAncestorSecurableObject(com.universal.dtos.onedrive.FirstUniqueAncestorSecurableObject FirstUniqueAncestorSecurableObject) {
		this.FirstUniqueAncestorSecurableObject = FirstUniqueAncestorSecurableObject;
	}

	/**
	 * 
	 * @return
	 * The RoleAssignments
	 */
	@JsonProperty("RoleAssignments")
	public com.universal.dtos.onedrive.RoleAssignments getRoleAssignments() {
		return RoleAssignments;
	}

	/**
	 * 
	 * @param RoleAssignments
	 * The RoleAssignments
	 */
	@JsonProperty("RoleAssignments")
	public void setRoleAssignments(com.universal.dtos.onedrive.RoleAssignments RoleAssignments) {
		this.RoleAssignments = RoleAssignments;
	}

	/**
	 * 
	 * @return
	 * The ContentTypes
	 */
	@JsonProperty("ContentTypes")
	public com.universal.dtos.onedrive.ContentTypes getContentTypes() {
		return ContentTypes;
	}

	/**
	 * 
	 * @param ContentTypes
	 * The ContentTypes
	 */
	@JsonProperty("ContentTypes")
	public void setContentTypes(com.universal.dtos.onedrive.ContentTypes ContentTypes) {
		this.ContentTypes = ContentTypes;
	}

	/**
	 * 
	 * @return
	 * The CreatablesInfo
	 */
	@JsonProperty("CreatablesInfo")
	public com.universal.dtos.onedrive.CreatablesInfo getCreatablesInfo() {
		return CreatablesInfo;
	}

	/**
	 * 
	 * @param CreatablesInfo
	 * The CreatablesInfo
	 */
	@JsonProperty("CreatablesInfo")
	public void setCreatablesInfo(com.universal.dtos.onedrive.CreatablesInfo CreatablesInfo) {
		this.CreatablesInfo = CreatablesInfo;
	}

	/**
	 * 
	 * @return
	 * The DefaultView
	 */
	@JsonProperty("DefaultView")
	public com.universal.dtos.onedrive.DefaultView getDefaultView() {
		return DefaultView;
	}

	/**
	 * 
	 * @param DefaultView
	 * The DefaultView
	 */
	@JsonProperty("DefaultView")
	public void setDefaultView(com.universal.dtos.onedrive.DefaultView DefaultView) {
		this.DefaultView = DefaultView;
	}

	/**
	 * 
	 * @return
	 * The DescriptionResource
	 */
	@JsonProperty("DescriptionResource")
	public com.universal.dtos.onedrive.DescriptionResource getDescriptionResource() {
		return DescriptionResource;
	}

	/**
	 * 
	 * @param DescriptionResource
	 * The DescriptionResource
	 */
	@JsonProperty("DescriptionResource")
	public void setDescriptionResource(com.universal.dtos.onedrive.DescriptionResource DescriptionResource) {
		this.DescriptionResource = DescriptionResource;
	}

	/**
	 * 
	 * @return
	 * The EventReceivers
	 */
	@JsonProperty("EventReceivers")
	public com.universal.dtos.onedrive.EventReceivers getEventReceivers() {
		return EventReceivers;
	}

	/**
	 * 
	 * @param EventReceivers
	 * The EventReceivers
	 */
	@JsonProperty("EventReceivers")
	public void setEventReceivers(com.universal.dtos.onedrive.EventReceivers EventReceivers) {
		this.EventReceivers = EventReceivers;
	}

	/**
	 * 
	 * @return
	 * The Fields
	 */
	@JsonProperty("Fields")
	public com.universal.dtos.onedrive.Fields getFields() {
		return Fields;
	}

	/**
	 * 
	 * @param Fields
	 * The Fields
	 */
	@JsonProperty("Fields")
	public void setFields(com.universal.dtos.onedrive.Fields Fields) {
		this.Fields = Fields;
	}

	/**
	 * 
	 * @return
	 * The Forms
	 */
	@JsonProperty("Forms")
	public com.universal.dtos.onedrive.Forms getForms() {
		return Forms;
	}

	/**
	 * 
	 * @param Forms
	 * The Forms
	 */
	@JsonProperty("Forms")
	public void setForms(com.universal.dtos.onedrive.Forms Forms) {
		this.Forms = Forms;
	}

	/**
	 * 
	 * @return
	 * The InformationRightsManagementSettings
	 */
	@JsonProperty("InformationRightsManagementSettings")
	public com.universal.dtos.onedrive.InformationRightsManagementSettings getInformationRightsManagementSettings() {
		return InformationRightsManagementSettings;
	}

	/**
	 * 
	 * @param InformationRightsManagementSettings
	 * The InformationRightsManagementSettings
	 */
	@JsonProperty("InformationRightsManagementSettings")
	public void setInformationRightsManagementSettings(com.universal.dtos.onedrive.InformationRightsManagementSettings InformationRightsManagementSettings) {
		this.InformationRightsManagementSettings = InformationRightsManagementSettings;
	}

	/**
	 * 
	 * @return
	 * The Items
	 */
	@JsonProperty("Items")
	public com.universal.dtos.onedrive.Items getItems() {
		return Items;
	}

	/**
	 * 
	 * @param Items
	 * The Items
	 */
	@JsonProperty("Items")
	public void setItems(com.universal.dtos.onedrive.Items Items) {
		this.Items = Items;
	}

	/**
	 * 
	 * @return
	 * The ParentWeb
	 */
	@JsonProperty("ParentWeb")
	public com.universal.dtos.onedrive.ParentWeb getParentWeb() {
		return ParentWeb;
	}

	/**
	 * 
	 * @param ParentWeb
	 * The ParentWeb
	 */
	@JsonProperty("ParentWeb")
	public void setParentWeb(com.universal.dtos.onedrive.ParentWeb ParentWeb) {
		this.ParentWeb = ParentWeb;
	}

	/**
	 * 
	 * @return
	 * The RootFolder
	 */
	@JsonProperty("RootFolder")
	public com.universal.dtos.onedrive.RootFolder getRootFolder() {
		return RootFolder;
	}

	/**
	 * 
	 * @param RootFolder
	 * The RootFolder
	 */
	@JsonProperty("RootFolder")
	public void setRootFolder(com.universal.dtos.onedrive.RootFolder RootFolder) {
		this.RootFolder = RootFolder;
	}

	/**
	 * 
	 * @return
	 * The TitleResource
	 */
	@JsonProperty("TitleResource")
	public com.universal.dtos.onedrive.TitleResource getTitleResource() {
		return TitleResource;
	}

	/**
	 * 
	 * @param TitleResource
	 * The TitleResource
	 */
	@JsonProperty("TitleResource")
	public void setTitleResource(com.universal.dtos.onedrive.TitleResource TitleResource) {
		this.TitleResource = TitleResource;
	}

	/**
	 * 
	 * @return
	 * The UserCustomActions
	 */
	@JsonProperty("UserCustomActions")
	public com.universal.dtos.onedrive.UserCustomActions getUserCustomActions() {
		return UserCustomActions;
	}

	/**
	 * 
	 * @param UserCustomActions
	 * The UserCustomActions
	 */
	@JsonProperty("UserCustomActions")
	public void setUserCustomActions(com.universal.dtos.onedrive.UserCustomActions UserCustomActions) {
		this.UserCustomActions = UserCustomActions;
	}

	/**
	 * 
	 * @return
	 * The Views
	 */
	@JsonProperty("Views")
	public com.universal.dtos.onedrive.Views getViews() {
		return Views;
	}

	/**
	 * 
	 * @param Views
	 * The Views
	 */
	@JsonProperty("Views")
	public void setViews(com.universal.dtos.onedrive.Views Views) {
		this.Views = Views;
	}

	/**
	 * 
	 * @return
	 * The WorkflowAssociations
	 */
	@JsonProperty("WorkflowAssociations")
	public com.universal.dtos.onedrive.WorkflowAssociations getWorkflowAssociations() {
		return WorkflowAssociations;
	}

	/**
	 * 
	 * @param WorkflowAssociations
	 * The WorkflowAssociations
	 */
	@JsonProperty("WorkflowAssociations")
	public void setWorkflowAssociations(com.universal.dtos.onedrive.WorkflowAssociations WorkflowAssociations) {
		this.WorkflowAssociations = WorkflowAssociations;
	}

	/**
	 * 
	 * @return
	 * The AllowContentTypes
	 */
	@JsonProperty("AllowContentTypes")
	public boolean isAllowContentTypes() {
		return AllowContentTypes;
	}

	/**
	 * 
	 * @param AllowContentTypes
	 * The AllowContentTypes
	 */
	@JsonProperty("AllowContentTypes")
	public void setAllowContentTypes(boolean AllowContentTypes) {
		this.AllowContentTypes = AllowContentTypes;
	}

	/**
	 * 
	 * @return
	 * The BaseTemplate
	 */
	@JsonProperty("BaseTemplate")
	public long getBaseTemplate() {
		return BaseTemplate;
	}

	/**
	 * 
	 * @param BaseTemplate
	 * The BaseTemplate
	 */
	@JsonProperty("BaseTemplate")
	public void setBaseTemplate(long BaseTemplate) {
		this.BaseTemplate = BaseTemplate;
	}

	/**
	 * 
	 * @return
	 * The BaseType
	 */
	@JsonProperty("BaseType")
	public long getBaseType() {
		return BaseType;
	}

	/**
	 * 
	 * @param BaseType
	 * The BaseType
	 */
	@JsonProperty("BaseType")
	public void setBaseType(long BaseType) {
		this.BaseType = BaseType;
	}

	/**
	 * 
	 * @return
	 * The ContentTypesEnabled
	 */
	@JsonProperty("ContentTypesEnabled")
	public boolean isContentTypesEnabled() {
		return ContentTypesEnabled;
	}

	/**
	 * 
	 * @param ContentTypesEnabled
	 * The ContentTypesEnabled
	 */
	@JsonProperty("ContentTypesEnabled")
	public void setContentTypesEnabled(boolean ContentTypesEnabled) {
		this.ContentTypesEnabled = ContentTypesEnabled;
	}

	/**
	 * 
	 * @return
	 * The CrawlNonDefaultViews
	 */
	@JsonProperty("CrawlNonDefaultViews")
	public boolean isCrawlNonDefaultViews() {
		return CrawlNonDefaultViews;
	}

	/**
	 * 
	 * @param CrawlNonDefaultViews
	 * The CrawlNonDefaultViews
	 */
	@JsonProperty("CrawlNonDefaultViews")
	public void setCrawlNonDefaultViews(boolean CrawlNonDefaultViews) {
		this.CrawlNonDefaultViews = CrawlNonDefaultViews;
	}

	/**
	 * 
	 * @return
	 * The Created
	 */
	@JsonProperty("Created")
	public String getCreated() {
		return Created;
	}

	/**
	 * 
	 * @param Created
	 * The Created
	 */
	@JsonProperty("Created")
	public void setCreated(String Created) {
		this.Created = Created;
	}

	/**
	 * 
	 * @return
	 * The DefaultContentApprovalWorkflowId
	 */
	@JsonProperty("DefaultContentApprovalWorkflowId")
	public String getDefaultContentApprovalWorkflowId() {
		return DefaultContentApprovalWorkflowId;
	}

	/**
	 * 
	 * @param DefaultContentApprovalWorkflowId
	 * The DefaultContentApprovalWorkflowId
	 */
	@JsonProperty("DefaultContentApprovalWorkflowId")
	public void setDefaultContentApprovalWorkflowId(String DefaultContentApprovalWorkflowId) {
		this.DefaultContentApprovalWorkflowId = DefaultContentApprovalWorkflowId;
	}

	/**
	 * 
	 * @return
	 * The Description
	 */
	@JsonProperty("Description")
	public String getDescription() {
		return Description;
	}

	/**
	 * 
	 * @param Description
	 * The Description
	 */
	@JsonProperty("Description")
	public void setDescription(String Description) {
		this.Description = Description;
	}

	/**
	 * 
	 * @return
	 * The Direction
	 */
	@JsonProperty("Direction")
	public String getDirection() {
		return Direction;
	}

	/**
	 * 
	 * @param Direction
	 * The Direction
	 */
	@JsonProperty("Direction")
	public void setDirection(String Direction) {
		this.Direction = Direction;
	}

	/**
	 * 
	 * @return
	 * The DocumentTemplateUrl
	 */
	@JsonProperty("DocumentTemplateUrl")
	public Object getDocumentTemplateUrl() {
		return DocumentTemplateUrl;
	}

	/**
	 * 
	 * @param DocumentTemplateUrl
	 * The DocumentTemplateUrl
	 */
	@JsonProperty("DocumentTemplateUrl")
	public void setDocumentTemplateUrl(Object DocumentTemplateUrl) {
		this.DocumentTemplateUrl = DocumentTemplateUrl;
	}

	/**
	 * 
	 * @return
	 * The DraftVersionVisibility
	 */
	@JsonProperty("DraftVersionVisibility")
	public long getDraftVersionVisibility() {
		return DraftVersionVisibility;
	}

	/**
	 * 
	 * @param DraftVersionVisibility
	 * The DraftVersionVisibility
	 */
	@JsonProperty("DraftVersionVisibility")
	public void setDraftVersionVisibility(long DraftVersionVisibility) {
		this.DraftVersionVisibility = DraftVersionVisibility;
	}

	/**
	 * 
	 * @return
	 * The EnableAttachments
	 */
	@JsonProperty("EnableAttachments")
	public boolean isEnableAttachments() {
		return EnableAttachments;
	}

	/**
	 * 
	 * @param EnableAttachments
	 * The EnableAttachments
	 */
	@JsonProperty("EnableAttachments")
	public void setEnableAttachments(boolean EnableAttachments) {
		this.EnableAttachments = EnableAttachments;
	}

	/**
	 * 
	 * @return
	 * The EnableFolderCreation
	 */
	@JsonProperty("EnableFolderCreation")
	public boolean isEnableFolderCreation() {
		return EnableFolderCreation;
	}

	/**
	 * 
	 * @param EnableFolderCreation
	 * The EnableFolderCreation
	 */
	@JsonProperty("EnableFolderCreation")
	public void setEnableFolderCreation(boolean EnableFolderCreation) {
		this.EnableFolderCreation = EnableFolderCreation;
	}

	/**
	 * 
	 * @return
	 * The EnableMinorVersions
	 */
	@JsonProperty("EnableMinorVersions")
	public boolean isEnableMinorVersions() {
		return EnableMinorVersions;
	}

	/**
	 * 
	 * @param EnableMinorVersions
	 * The EnableMinorVersions
	 */
	@JsonProperty("EnableMinorVersions")
	public void setEnableMinorVersions(boolean EnableMinorVersions) {
		this.EnableMinorVersions = EnableMinorVersions;
	}

	/**
	 * 
	 * @return
	 * The EnableModeration
	 */
	@JsonProperty("EnableModeration")
	public boolean isEnableModeration() {
		return EnableModeration;
	}

	/**
	 * 
	 * @param EnableModeration
	 * The EnableModeration
	 */
	@JsonProperty("EnableModeration")
	public void setEnableModeration(boolean EnableModeration) {
		this.EnableModeration = EnableModeration;
	}

	/**
	 * 
	 * @return
	 * The EnableVersioning
	 */
	@JsonProperty("EnableVersioning")
	public boolean isEnableVersioning() {
		return EnableVersioning;
	}

	/**
	 * 
	 * @param EnableVersioning
	 * The EnableVersioning
	 */
	@JsonProperty("EnableVersioning")
	public void setEnableVersioning(boolean EnableVersioning) {
		this.EnableVersioning = EnableVersioning;
	}

	/**
	 * 
	 * @return
	 * The EntityTypeName
	 */
	@JsonProperty("EntityTypeName")
	public String getEntityTypeName() {
		return EntityTypeName;
	}

	/**
	 * 
	 * @param EntityTypeName
	 * The EntityTypeName
	 */
	@JsonProperty("EntityTypeName")
	public void setEntityTypeName(String EntityTypeName) {
		this.EntityTypeName = EntityTypeName;
	}

	/**
	 * 
	 * @return
	 * The FileSavePostProcessingEnabled
	 */
	@JsonProperty("FileSavePostProcessingEnabled")
	public boolean isFileSavePostProcessingEnabled() {
		return FileSavePostProcessingEnabled;
	}

	/**
	 * 
	 * @param FileSavePostProcessingEnabled
	 * The FileSavePostProcessingEnabled
	 */
	@JsonProperty("FileSavePostProcessingEnabled")
	public void setFileSavePostProcessingEnabled(boolean FileSavePostProcessingEnabled) {
		this.FileSavePostProcessingEnabled = FileSavePostProcessingEnabled;
	}

	/**
	 * 
	 * @return
	 * The ForceCheckout
	 */
	@JsonProperty("ForceCheckout")
	public boolean isForceCheckout() {
		return ForceCheckout;
	}

	/**
	 * 
	 * @param ForceCheckout
	 * The ForceCheckout
	 */
	@JsonProperty("ForceCheckout")
	public void setForceCheckout(boolean ForceCheckout) {
		this.ForceCheckout = ForceCheckout;
	}

	/**
	 * 
	 * @return
	 * The HasExternalDataSource
	 */
	@JsonProperty("HasExternalDataSource")
	public boolean isHasExternalDataSource() {
		return HasExternalDataSource;
	}

	/**
	 * 
	 * @param HasExternalDataSource
	 * The HasExternalDataSource
	 */
	@JsonProperty("HasExternalDataSource")
	public void setHasExternalDataSource(boolean HasExternalDataSource) {
		this.HasExternalDataSource = HasExternalDataSource;
	}

	/**
	 * 
	 * @return
	 * The Hidden
	 */
	@JsonProperty("Hidden")
	public boolean isHidden() {
		return Hidden;
	}

	/**
	 * 
	 * @param Hidden
	 * The Hidden
	 */
	@JsonProperty("Hidden")
	public void setHidden(boolean Hidden) {
		this.Hidden = Hidden;
	}

	/**
	 * 
	 * @return
	 * The Id
	 */
	@JsonProperty("Id")
	public String getId() {
		return Id;
	}

	/**
	 * 
	 * @param Id
	 * The Id
	 */
	@JsonProperty("Id")
	public void setId(String Id) {
		this.Id = Id;
	}

	/**
	 * 
	 * @return
	 * The ImageUrl
	 */
	@JsonProperty("ImageUrl")
	public String getImageUrl() {
		return ImageUrl;
	}

	/**
	 * 
	 * @param ImageUrl
	 * The ImageUrl
	 */
	@JsonProperty("ImageUrl")
	public void setImageUrl(String ImageUrl) {
		this.ImageUrl = ImageUrl;
	}

	/**
	 * 
	 * @return
	 * The IrmEnabled
	 */
	@JsonProperty("IrmEnabled")
	public boolean isIrmEnabled() {
		return IrmEnabled;
	}

	/**
	 * 
	 * @param IrmEnabled
	 * The IrmEnabled
	 */
	@JsonProperty("IrmEnabled")
	public void setIrmEnabled(boolean IrmEnabled) {
		this.IrmEnabled = IrmEnabled;
	}

	/**
	 * 
	 * @return
	 * The IrmExpire
	 */
	@JsonProperty("IrmExpire")
	public boolean isIrmExpire() {
		return IrmExpire;
	}

	/**
	 * 
	 * @param IrmExpire
	 * The IrmExpire
	 */
	@JsonProperty("IrmExpire")
	public void setIrmExpire(boolean IrmExpire) {
		this.IrmExpire = IrmExpire;
	}

	/**
	 * 
	 * @return
	 * The IrmReject
	 */
	@JsonProperty("IrmReject")
	public boolean isIrmReject() {
		return IrmReject;
	}

	/**
	 * 
	 * @param IrmReject
	 * The IrmReject
	 */
	@JsonProperty("IrmReject")
	public void setIrmReject(boolean IrmReject) {
		this.IrmReject = IrmReject;
	}

	/**
	 * 
	 * @return
	 * The IsApplicationList
	 */
	@JsonProperty("IsApplicationList")
	public boolean isIsApplicationList() {
		return IsApplicationList;
	}

	/**
	 * 
	 * @param IsApplicationList
	 * The IsApplicationList
	 */
	@JsonProperty("IsApplicationList")
	public void setIsApplicationList(boolean IsApplicationList) {
		this.IsApplicationList = IsApplicationList;
	}

	/**
	 * 
	 * @return
	 * The IsCatalog
	 */
	@JsonProperty("IsCatalog")
	public boolean isIsCatalog() {
		return IsCatalog;
	}

	/**
	 * 
	 * @param IsCatalog
	 * The IsCatalog
	 */
	@JsonProperty("IsCatalog")
	public void setIsCatalog(boolean IsCatalog) {
		this.IsCatalog = IsCatalog;
	}

	/**
	 * 
	 * @return
	 * The IsPrivate
	 */
	@JsonProperty("IsPrivate")
	public boolean isIsPrivate() {
		return IsPrivate;
	}

	/**
	 * 
	 * @param IsPrivate
	 * The IsPrivate
	 */
	@JsonProperty("IsPrivate")
	public void setIsPrivate(boolean IsPrivate) {
		this.IsPrivate = IsPrivate;
	}

	/**
	 * 
	 * @return
	 * The ItemCount
	 */
	@JsonProperty("ItemCount")
	public long getItemCount() {
		return ItemCount;
	}

	/**
	 * 
	 * @param ItemCount
	 * The ItemCount
	 */
	@JsonProperty("ItemCount")
	public void setItemCount(long ItemCount) {
		this.ItemCount = ItemCount;
	}

	/**
	 * 
	 * @return
	 * The LastItemDeletedDate
	 */
	@JsonProperty("LastItemDeletedDate")
	public String getLastItemDeletedDate() {
		return LastItemDeletedDate;
	}

	/**
	 * 
	 * @param LastItemDeletedDate
	 * The LastItemDeletedDate
	 */
	@JsonProperty("LastItemDeletedDate")
	public void setLastItemDeletedDate(String LastItemDeletedDate) {
		this.LastItemDeletedDate = LastItemDeletedDate;
	}

	/**
	 * 
	 * @return
	 * The LastItemModifiedDate
	 */
	@JsonProperty("LastItemModifiedDate")
	public String getLastItemModifiedDate() {
		return LastItemModifiedDate;
	}

	/**
	 * 
	 * @param LastItemModifiedDate
	 * The LastItemModifiedDate
	 */
	@JsonProperty("LastItemModifiedDate")
	public void setLastItemModifiedDate(String LastItemModifiedDate) {
		this.LastItemModifiedDate = LastItemModifiedDate;
	}

	/**
	 * 
	 * @return
	 * The ListItemEntityTypeFullName
	 */
	@JsonProperty("ListItemEntityTypeFullName")
	public String getListItemEntityTypeFullName() {
		return ListItemEntityTypeFullName;
	}

	/**
	 * 
	 * @param ListItemEntityTypeFullName
	 * The ListItemEntityTypeFullName
	 */
	@JsonProperty("ListItemEntityTypeFullName")
	public void setListItemEntityTypeFullName(String ListItemEntityTypeFullName) {
		this.ListItemEntityTypeFullName = ListItemEntityTypeFullName;
	}

	/**
	 * 
	 * @return
	 * The MajorVersionLimit
	 */
	@JsonProperty("MajorVersionLimit")
	public long getMajorVersionLimit() {
		return MajorVersionLimit;
	}

	/**
	 * 
	 * @param MajorVersionLimit
	 * The MajorVersionLimit
	 */
	@JsonProperty("MajorVersionLimit")
	public void setMajorVersionLimit(long MajorVersionLimit) {
		this.MajorVersionLimit = MajorVersionLimit;
	}

	/**
	 * 
	 * @return
	 * The MajorWithMinorVersionsLimit
	 */
	@JsonProperty("MajorWithMinorVersionsLimit")
	public long getMajorWithMinorVersionsLimit() {
		return MajorWithMinorVersionsLimit;
	}

	/**
	 * 
	 * @param MajorWithMinorVersionsLimit
	 * The MajorWithMinorVersionsLimit
	 */
	@JsonProperty("MajorWithMinorVersionsLimit")
	public void setMajorWithMinorVersionsLimit(long MajorWithMinorVersionsLimit) {
		this.MajorWithMinorVersionsLimit = MajorWithMinorVersionsLimit;
	}

	/**
	 * 
	 * @return
	 * The MultipleDataList
	 */
	@JsonProperty("MultipleDataList")
	public boolean isMultipleDataList() {
		return MultipleDataList;
	}

	/**
	 * 
	 * @param MultipleDataList
	 * The MultipleDataList
	 */
	@JsonProperty("MultipleDataList")
	public void setMultipleDataList(boolean MultipleDataList) {
		this.MultipleDataList = MultipleDataList;
	}

	/**
	 * 
	 * @return
	 * The NoCrawl
	 */
	@JsonProperty("NoCrawl")
	public boolean isNoCrawl() {
		return NoCrawl;
	}

	/**
	 * 
	 * @param NoCrawl
	 * The NoCrawl
	 */
	@JsonProperty("NoCrawl")
	public void setNoCrawl(boolean NoCrawl) {
		this.NoCrawl = NoCrawl;
	}

	/**
	 * 
	 * @return
	 * The ParentWebUrl
	 */
	@JsonProperty("ParentWebUrl")
	public String getParentWebUrl() {
		return ParentWebUrl;
	}

	/**
	 * 
	 * @param ParentWebUrl
	 * The ParentWebUrl
	 */
	@JsonProperty("ParentWebUrl")
	public void setParentWebUrl(String ParentWebUrl) {
		this.ParentWebUrl = ParentWebUrl;
	}

	/**
	 * 
	 * @return
	 * The ParserDisabled
	 */
	@JsonProperty("ParserDisabled")
	public boolean isParserDisabled() {
		return ParserDisabled;
	}

	/**
	 * 
	 * @param ParserDisabled
	 * The ParserDisabled
	 */
	@JsonProperty("ParserDisabled")
	public void setParserDisabled(boolean ParserDisabled) {
		this.ParserDisabled = ParserDisabled;
	}

	/**
	 * 
	 * @return
	 * The ServerTemplateCanCreateFolders
	 */
	@JsonProperty("ServerTemplateCanCreateFolders")
	public boolean isServerTemplateCanCreateFolders() {
		return ServerTemplateCanCreateFolders;
	}

	/**
	 * 
	 * @param ServerTemplateCanCreateFolders
	 * The ServerTemplateCanCreateFolders
	 */
	@JsonProperty("ServerTemplateCanCreateFolders")
	public void setServerTemplateCanCreateFolders(boolean ServerTemplateCanCreateFolders) {
		this.ServerTemplateCanCreateFolders = ServerTemplateCanCreateFolders;
	}

	/**
	 * 
	 * @return
	 * The TemplateFeatureId
	 */
	@JsonProperty("TemplateFeatureId")
	public String getTemplateFeatureId() {
		return TemplateFeatureId;
	}

	/**
	 * 
	 * @param TemplateFeatureId
	 * The TemplateFeatureId
	 */
	@JsonProperty("TemplateFeatureId")
	public void setTemplateFeatureId(String TemplateFeatureId) {
		this.TemplateFeatureId = TemplateFeatureId;
	}

	/**
	 * 
	 * @return
	 * The Title
	 */
	@JsonProperty("Title")
	public String getTitle() {
		return Title;
	}

	/**
	 * 
	 * @param Title
	 * The Title
	 */
	@JsonProperty("Title")
	public void setTitle(String Title) {
		this.Title = Title;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

