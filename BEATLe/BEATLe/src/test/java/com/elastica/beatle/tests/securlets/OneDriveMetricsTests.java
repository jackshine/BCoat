package com.elastica.beatle.tests.securlets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.O365DataProvider;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.ActivityType;
import com.elastica.beatle.securlets.SecurletUtils.ObjectType;
import com.elastica.beatle.securlets.SecurletUtils.Severity;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.RemedialAction;
import com.elastica.beatle.securlets.dto.RemediationMetaInfo;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.SecurletRemediation;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.elastica.beatle.securlets.dto.UIRemediationInnerObject;
import com.elastica.beatle.securlets.dto.UIRemediationObject;
import com.elastica.beatle.securlets.dto.UIRemediationSource;
import com.elastica.beatle.securlets.dto.UISource;
import com.universal.constants.CommonConstants;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.ItemRoleAssignment;
import com.universal.dtos.onedrive.ListItemAllFields;
import com.universal.dtos.onedrive.RoleDefinitions;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.SiteUserList;
import com.universal.dtos.onedrive.UserResult;
import com.universal.dtos.onedrive.UserValue;



public class OneDriveMetricsTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	DocumentValidator docValidator;
	LogValidator logValidator;
	OneDriveActivityLog onedrivelog;
	OneDriveUtils onedriveUtils;
	
	
	List<String> possibleValues = new ArrayList<String>();
	List<String> readonlyValues = new ArrayList<String>();
	HashMap<String, Long> usermap = new HashMap<String, Long>();
	HashMap<String, Long> rolemap = new HashMap<String, Long>();
	
	String uniqueId, sourceFile, destinationFile, itemlink, createdTime;
	ItemResource itemResource;
	ForensicSearchResults fileLogs, folderLogs;
	
	public OneDriveMetricsTests() throws Exception {
		esQueryBuilder = new ESQueryBuilder();
		docValidator = new DocumentValidator();
		logValidator = new LogValidator();
		
		uniqueId = String.valueOf(System.currentTimeMillis());//UUID.randomUUID().toString();
		onedriveUtils = new OneDriveUtils();
		onedrivelog = new OneDriveActivityLog();

		//Populate possible values
		possibleValues.add("Everyone-Read"); 
		possibleValues.add("Everyone except external users-Read");
		possibleValues.add("Everyone-Contribute");
		possibleValues.add("Everyone except external users-Contribute");
		possibleValues.add("Everyone-Edit"); 
		possibleValues.add("Everyone except external users-Edit");
		possibleValues.add("Everyone-Design");
		possibleValues.add("Everyone except external users-Design");
		possibleValues.add("Everyone-Full Control");
		possibleValues.add("Everyone except external users-Full Control");
		
		
		//Populate readonly values
		readonlyValues.add("open-view"); 
		readonlyValues.add("open-edit");
	}

	
	@BeforeClass(alwaysRun=true)
	public void initOffice() throws Exception {
		AuthorizationHandler.disableAnonymization(suiteData);
		
		SiteUserList splist = universalApi.getSPUserList();
		for (UserResult ur : splist.getD().getResults()){
			usermap.put(ur.getTitle(), ur.getPrincipalType());
		}

		RoleDefinitions roledefs = universalApi.getSharePointRolesDefinitions();
		for (UserValue ur : roledefs.getValue()){
			rolemap.put(ur.getName(), ur.getId());
		}
		
		
		uniqueId = String.valueOf(System.currentTimeMillis());
		sourceFile = "Hello.java";
		destinationFile = uniqueId + "_" + sourceFile;
		Reporter.log("Uploaded the file :"+destinationFile, true);

		//Upload a source code file to box root folder
		itemResource = universalApi.uploadSimpleFile("/", sourceFile, destinationFile);
		Reporter.log(itemResource.getName() + " " + itemResource.getId());
		Reporter.log(MarshallingUtils.marshall(itemResource), true);

		//Get the list item link
		ListItemAllFields listitemfields = universalApi.getListItemAllFieldsByUrl("Documents", destinationFile);
		this.itemlink = listitemfields.getOdataEditLink();
		Reporter.log("Item link:"+ itemlink, true);		
	}

	
	
	
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ExposureRemediationProvider")
	public void exposeAndPerformUnshareRemediation(String currentUser, String currentRole, String remediationRole) throws Exception {
		String steps[] = 
			{	"1. This test expose the file with  "+ currentRole +" permission to " + currentUser, 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		Reporter.log("1. File uploaded and itemlink is ready." + destinationFile, true);
		universalApi.breakRoleInheritanceForListItem(itemlink, false, false);

		//Assign role inheritance
		Reporter.log("2. Assign the file " + currentRole +" permissions to " +currentUser, true);
		universalApi.addRoleAssignmentForListItem(itemlink, String.valueOf(usermap.get(currentUser)), String.valueOf(rolemap.get(currentRole)));
		ItemRoleAssignment rolesBeforeRemediation = universalApi.getSharePointItemRolesAssignments(destinationFile);
		
		//Prepare the log for share activity verification
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		onedrivelog = new OneDriveActivityLog();
		onedrivelog.setFileShareNoUserLog(onedrivelog.getFileShareNoUserLog().replace("{filename}", destinationFile));
		OneDriveBusinessActivity fileShareLog = new OneDriveBusinessActivity(onedrivelog.getFileShareNoUserLog(), createdTime, Severity.informational.name(),  
				ObjectType.File.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
				socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");
		
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		String etagAsDocId = getETagAsDocumentId(itemResource.getETag());
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), etagAsDocId), "Exposed document is available in db", "Exposed document is not available in db");
		
		//Prepare the UI remediation object
		String[] code     = { remediationRole };
		String[] metaInfo = { currentUser + "-" + currentRole };
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(itemResource.getCreatedBy().getUser().getId(), itemResource.getType(), 
																		suiteData.getDomainName(), etagAsDocId,  code, metaInfo );
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);

		Reporter.log("4. Applying the remediation..",  true);
		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		//Prepare the log for unshare activity verification
		String unshare = currentUser+"("+currentRole+")";
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		
		String users  = currentUser;
		if (currentUser.equals("Everyone")) {
			users = suiteData.getSaasAppExternalUser1Name() + "," + currentUser;
		}
		onedrivelog.setFileUnShareLog(onedrivelog.getFileUnShareLog().replace("{filename}", destinationFile)
																		.replace("{username}", users)
																		.replace("{permission}", currentRole)
																		);
		OneDriveBusinessActivity fileUnshareLog = new OneDriveBusinessActivity(onedrivelog.getFileUnShareLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
					socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365", currentUser, unshare, suiteData.getDomainName());

		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalApi.getSharePointItemRolesAssignments(destinationFile);
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		CustomAssertion.assertTrue(!checkDocumentInDB(elapp.el_office_365.name(), etagAsDocId), "Exposed document is not available in db", "Exposed document is available in db");

		Reporter.log("6. Verifying the remediation..",  true);

		int beforeRemediation = rolesBeforeRemediation.getValue().size();
		int afterRemediation  = rolesAfterRemediation.getValue().size();
		
		CustomAssertion.assertTrue(beforeRemediation != afterRemediation, "Remediation is successful", "Remediation not happened it seems");
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
		Reporter.log("8. Verifying share and unshare logs ..", true);
		
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Office 365");
		termmap.put("Object_type", ObjectType.File.name());
		termmap.put("query", uniqueId);
		
		//Get file related logs
		fileLogs = this.getInvestigateLogs(-35, 10, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
				suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Office 365");
		
		//Verify share log
		logValidator.verifyOnedriveActivityLog(fileLogs, fileShareLog);
		
		//Verify unshare log
		logValidator.verifyOnedriveActivityLog(fileLogs, fileUnshareLog);

	}
	
	
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "exposeAndRemediateDataProvider")
	public void exposeFileAndPerformUIRemediation(int role, String sharedWithUsers, String[] remedialAction, String[] metaInfo) throws Exception {

		String roleString = (role == 1) ? "View" : "Edit";
		String steps[] = 
			{	"1. This test uploads the file and exposed to "+ sharedWithUsers +" with permission  " + roleString, 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		long beforePublic , beforeInternal, beforeExternal = 0;
		long afterPublic , afterInternal, afterExternal = 0;


		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));

		ExposureTotals expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

		beforePublic = expsoureTotals.getPublicExposouresCount();
		beforeInternal = expsoureTotals.getInternalExposouresCount();
		beforeExternal = expsoureTotals.getExternalExposouresCount();

		//Upload a 
		ItemResource itemResource = null;
		SharingUserRoleAssignment shareObject = null;
		
		try {
			String filename = "test.pdf";
			//Upload the file
			itemResource = universalApi.uploadSimpleFile("/", filename, uniqueId +"_"+filename);
			
			Reporter.log("1. Uploaded the file :"+itemResource.getName(), true);
			
			shareObject = onedriveUtils.getFileShareObject(itemResource, role, sharedWithUsers);
			DocumentSharingResult docSharingResult =  universalApi.shareWithCollaborators(shareObject);
			Reporter.log(MarshallingUtils.marshall(docSharingResult), true);
			
			Reporter.log("2. Shared the file and going to wait...", true);
			
			sleep(CommonConstants.FIVE_MINUTES_SLEEP);
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
		}
		catch(Exception e) { }

		ExposureTotals expsoureTotalsAfter = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);
		afterPublic 	= expsoureTotalsAfter.getPublicExposouresCount();
		afterInternal 	= expsoureTotalsAfter.getInternalExposouresCount();
		afterExternal	= expsoureTotalsAfter.getExternalExposouresCount();		
		
		/*
		CustomAssertion.assertEquals(afterPublic, beforePublic , 
				"Public count after exposure "+afterPublic + " is not matching with before exposure "+beforePublic);

		CustomAssertion.assertEquals(afterInternal, beforeInternal + 1, 
				"Internal count after exposure "+afterInternal + " is not matching with before exposure "+beforeInternal + 1 );

		CustomAssertion.assertEquals(afterExternal, beforeExternal, 
				"External count after exposure "+afterExternal + " is not matching with before exposure "+beforeExternal);
		 */
		
		Reporter.log("3. Check the document is exposed before remediation...", true);
		String etagAsDocId = getETagAsDocumentId(itemResource.getETag());
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), etagAsDocId), "Exposed document is available in db", "Exposed document is not available in db");
		
		//Prepare the UI remediation object
		UIRemediationObject UIRemedObject =  getUIRemediationObject(itemResource.getCreatedBy().getUser().getId(), itemResource.getType(), 
							suiteData.getDomainName(), etagAsDocId,  remedialAction, metaInfo );
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);

		Reporter.log("4. Going to remediate with UI server call...", true);

		//Remediate the exposure with UI server call
		remediateExposureWithUIServer(UIRemedObject);

		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		Reporter.log("5. Check the document exposure removed after remediation...", true);
		CustomAssertion.assertTrue(!checkDocumentInDB(elapp.el_office_365.name(), etagAsDocId), "Exposed document is not available in db", "Exposed document is available in db");

		
		//Todo
		//verifyShareAndUnshareLog();
		
		expsoureTotalsAfter = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);
		afterPublic 	= expsoureTotalsAfter.getPublicExposouresCount();
		afterInternal 	= expsoureTotalsAfter.getInternalExposouresCount();
		afterExternal	= expsoureTotalsAfter.getExternalExposouresCount();		
		
		/*
		CustomAssertion.assertEquals(afterPublic, beforePublic, 
				"Public count after exposure "+afterPublic + " is not matching with before exposure "+beforePublic );

		CustomAssertion.assertEquals(afterInternal, beforeInternal, 
				"Internal count after exposure "+afterInternal + " is not matching with before exposure "+beforeInternal );

		CustomAssertion.assertEquals(afterExternal, beforeExternal, 
				"External count after exposure "+afterExternal + " is not matching with before exposure "+beforeExternal );
		 */		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Test 1
	 * Check the exposure metrics totals as in Venn Diagram
	 * @throws Exception
	 */

	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES"})
	public void verifyVennDiagramMetrics() throws Exception {
		Reporter.log("Verifying the exposure totals for internally owned documents as depicted in Venn diagram...", true);
		long beforePublic , beforeInternal, beforeExternal = 0;
		long afterPublic , afterInternal, afterExternal = 0;
		
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));

		ExposureTotals expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

		beforePublic = expsoureTotals.getPublicExposouresCount();
		beforeInternal = expsoureTotals.getInternalExposouresCount();
		beforeExternal = expsoureTotals.getExternalExposouresCount();
		
		//Upload a 
		ItemResource itemResource = null;
		SharingUserRoleAssignment shareObject = null;
		try {

			String filename = "test.pdf";
			//Upload the file
			itemResource = universalApi.uploadSimpleFile("/", filename, uniqueId +"_"+filename);
			
			shareObject = onedriveUtils.getPublicShareObject(itemResource.getWebUrl(), 1);
			DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
			Reporter.log(MarshallingUtils.marshall(docSharingResult), true);
			
			shareObject = onedriveUtils.getFileShareObject(itemResource, 1, "Everyone except external users");
			docSharingResult = universalApi.shareWithCollaborators(shareObject);
			Reporter.log(MarshallingUtils.marshall(docSharingResult), true);
		
			shareObject = onedriveUtils.getFileShareObject(itemResource, 1, this.suiteData.getSaasAppExternalUser1Name());
			docSharingResult = universalApi.shareWithCollaborators(shareObject);
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
		}
		catch(Exception e) { }

		ExposureTotals expsoureTotalsAfter = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);
		afterPublic 	= expsoureTotalsAfter.getPublicExposouresCount();
		afterInternal 	= expsoureTotalsAfter.getInternalExposouresCount();
		afterExternal	= expsoureTotalsAfter.getExternalExposouresCount();		

		CustomAssertion.assertEquals(afterPublic, beforePublic + 1, 
				"Public count after exposure "+afterPublic + " is not matching with before exposure "+beforePublic + 1 );
		
		CustomAssertion.assertEquals(afterInternal, beforeInternal + 1, 
				"Internal count after exposure "+afterInternal + " is not matching with before exposure "+beforeInternal + 1 );
		
		CustomAssertion.assertEquals(afterExternal, beforeExternal + 1, 
				"External count after exposure "+afterExternal + " is not matching with before exposure "+beforeExternal + 1 );
		
		String etagAsDocId = getETagAsDocumentId(itemResource.getETag());
		
		String[] code     = { "UNSHARE", "UNSHARE", "COLLAB_REMOVE" };
		String[] metaInfo = { "Everyone except external users-Read", "open-view", suiteData.getSaasAppExternalUser1Name() };
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(itemResource.getCreatedBy().getUser().getId(), itemResource.getType(), 
															suiteData.getDomainName(), etagAsDocId,  code, metaInfo );
		//Prepare the UI remediation object
		
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
				
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), etagAsDocId), "Exposed document is available in db", "Exposed document is not available in db");
		
		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		CustomAssertion.assertTrue(!checkDocumentInDB(elapp.el_office_365.name(), etagAsDocId), "Exposed document is not available in db", "Exposed document is available in db");
		
		expsoureTotalsAfter = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);
		afterPublic 	= expsoureTotalsAfter.getPublicExposouresCount();
		afterInternal 	= expsoureTotalsAfter.getInternalExposouresCount();
		afterExternal	= expsoureTotalsAfter.getExternalExposouresCount();		

		CustomAssertion.assertEquals(afterPublic, beforePublic, 
				"Public count after exposure "+afterPublic + " is not matching with before exposure "+beforePublic );
		
		CustomAssertion.assertEquals(afterInternal, beforeInternal, 
				"Internal count after exposure "+afterInternal + " is not matching with before exposure "+beforeInternal );
		
		CustomAssertion.assertEquals(afterExternal, beforeExternal, 
				"External count after exposure "+afterExternal + " is not matching with before exposure "+beforeExternal );
		

	}
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ExposureRemediationProvider")
	public void exposeFileAndPerformUnshareRemediation(String currentUser, String currentRole, String remediationRole) throws Exception {
		//Prepare the file upload 
		String uniqueId = String.valueOf(System.currentTimeMillis());//UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_"+sourceFile;
		Reporter.log("1. Uploaded the file :"+destinationFile, true);

		//Upload a source code file to box root folder
		ItemResource itemResource = universalApi.uploadSimpleFile("/", sourceFile, destinationFile);
		Reporter.log(itemResource.getName() + " " + itemResource.getId());
		Reporter.log(MarshallingUtils.marshall(itemResource), true);

		//Get the list item link
		ListItemAllFields listitemfields = universalApi.getListItemAllFieldsByUrl("Documents", destinationFile);
		String itemlink = listitemfields.getOdataEditLink();

		universalApi.breakRoleInheritanceForListItem(itemlink, false, false);

		//Assign role inheritance
		universalApi.addRoleAssignmentForListItem(itemlink, String.valueOf(usermap.get(currentUser)), String.valueOf(rolemap.get(currentRole)));
		ItemRoleAssignment rolesBeforeRemediation = universalApi.getSharePointItemRolesAssignments(destinationFile);
		Reporter.log("Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		Reporter.log(MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		
		String etagAsDocId = getETagAsDocumentId(itemResource.getETag());
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), etagAsDocId), "Exposed document is available in db", "Exposed document is not available in db");

		Reporter.log("3. Applying the remediation..",  true);
		
		String[] code     = { remediationRole };
		String[] metaInfo = { currentUser + "-" + currentRole };
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(itemResource.getCreatedBy().getUser().getId(), itemResource.getType(), 
																		suiteData.getDomainName(), etagAsDocId,  code, metaInfo );
		//Prepare the UI remediation object

		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);

		

		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);

		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalApi.getSharePointItemRolesAssignments(destinationFile);
		Reporter.log("Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		CustomAssertion.assertTrue(!checkDocumentInDB(elapp.el_office_365.name(), etagAsDocId), "Exposed document is not available in db", "Exposed document is available in db");

		Reporter.log("4. Verifying the remediation..",  true);

		int beforeRemediation = rolesBeforeRemediation.getValue().size();
		int afterRemediation  = rolesAfterRemediation.getValue().size();
		
		CustomAssertion.assertTrue(beforeRemediation != afterRemediation, "Remediation is successful", "Remediation not happened it seems");
		
		Reporter.log("5. Remediation applied successfully..",  true);
		Reporter.log("Cleaning the files...", true);

	}
	
	
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES"})
	public void addPermissionsForDesign() throws Exception {



		SiteUserList splist = universalApi.getSPUserList();
		HashMap<String, Long> usermap = new HashMap<String, Long>();
		HashMap<String, Long> rolemap = new HashMap<String, Long>();

		for (UserResult ur : splist.getD().getResults()){
			usermap.put(ur.getTitle(), ur.getPrincipalType());
		}

		RoleDefinitions roledefs = universalApi.getSharePointRolesDefinitions();
		for (UserValue ur : roledefs.getValue()){
			rolemap.put(ur.getName(), ur.getId());
		}

		//Prepare the file upload 
		String uniqueId = String.valueOf(System.currentTimeMillis());//UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_"+sourceFile;
		Reporter.log("1. Uploaded the file :"+destinationFile, true);

		//Upload a source code file to box root folder
		ItemResource itemResource = universalApi.uploadSimpleFile("/", sourceFile, destinationFile);
		Reporter.log(itemResource.getName() + " " + itemResource.getId());
		Reporter.log(MarshallingUtils.marshall(itemResource), true);

		
		//Get the list item link
		ListItemAllFields listitemfields = universalApi.getListItemAllFieldsByUrl("Documents", destinationFile);
		String itemlink = listitemfields.getOdataEditLink();
		
		universalApi.breakRoleInheritanceForListItem(itemlink, false, false);
		
		//Reset role inheritance
		//universalApi.resetRoleInheritance(itemlink);
		
		//Assign role inheritance
		universalApi.addRoleAssignmentForListItem(itemlink, String.valueOf(usermap.get("Everyone")), String.valueOf(rolemap.get("Full Control")));
		
	}
	
	
	
	private UIRemediationObject getUIRemediationObject(String userId, String docType, String instance, String docId, String code[], String[] metaInfo) {
		SecurletRemediation remediationObject =  getOneDriveRemediationObject(userId, docType, 
				instance, docId,  code, metaInfo );
		
		UIRemediationObject UIRemedObject = new UIRemediationObject();
		UIRemediationSource UISource = new UIRemediationSource();
		UIRemediationInnerObject UIInnerObject = new UIRemediationInnerObject();
		ArrayList<SecurletRemediation> remedList = new ArrayList<SecurletRemediation>();
		remedList.add(remediationObject);
		UIInnerObject.setObjects(remedList);
		UISource.setObjects(UIInnerObject);
		UISource.setApp("Office 365");
		UIRemedObject.setSource(UISource);
		return UIRemedObject;
	}
	
	/*private boolean checkDocumentInDB(String name, String fileId) throws Exception {
		// TODO Auto-generated method stub
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair("identification", fileId));
		SecurletDocument documents = getExposedDocuments(name, docparams);
		if (documents.getMeta().getTotalCount() > 0) {
			return true;
		} else {
			return false;
		}
		
	}*/
	
	
	
	
	/**
	 * Test 2
	 * verify  the internally exposed document count with UI and API server call
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES"})
	public void verifyExposedDocsFilter() throws Exception {
		Reporter.log("Verifying the internally exposed docs with UI call and API server call ...", true);
		
		UIExposedDoc payload = new UIExposedDoc();
		UISource uisource = new UISource();
		uisource.setApp("Office 365");
		uisource.setLimit(20);
		uisource.setOffset(0);
		uisource.setIsInternal(Boolean.TRUE.booleanValue());
		uisource.setSearchTextFromTable("");
		uisource.setOrderBy("name");
		uisource.setExportType("docs");
		//uisource.setObjectType("OneDrive");
		payload.setSource(uisource);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call");
		SecurletDocument uiCallDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the list of exposed documents with API server call");
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
		SecurletDocument apiCallDocs = getExposedDocuments(elapp.el_office_365.name(), docparams);
		LogUtils.logStep("3. Verify the total docs returned by api and ui calls are same");
		CustomAssertion.assertEquals(uiCallDocs.getMeta().getTotalCount(), apiCallDocs.getMeta().getTotalCount(), 
												"UI server docs count:"+uiCallDocs.getMeta().getTotalCount() +" is not matching "
														+ "with api server docs count:"+apiCallDocs.getMeta().getTotalCount());	
		for (com.elastica.beatle.securlets.dto.Object obj : apiCallDocs.getObjects()) {
			CustomAssertion.assertTrue(obj.getIsInternal(), "Doc is internally exposed", "Doc is not internally exposed");	
		}

	}
	
	
	
	/**
	 * Test 3
	 * Check the exposure metrics totals as in Venn Diagram
	 * @throws Exception
	 */

	@Test(groups={"DASHBOARD", "INTERNAL"})
	public void verifyExposedFileMetrics() throws Exception {
		Reporter.log("Verifying the exposure totals for internally owned documents with UI and API server call...", true);
		long countPublic   = 0;
		long countInternal = 0;
		long countExternal = 0;

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
		LogUtils.logStep("1. Get the exposure metrics total with API server call");
		ExposureTotals apiTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

		countPublic = apiTotals.getPublicExposouresCount();
		countInternal = apiTotals.getInternalExposouresCount();
		countExternal = apiTotals.getExternalExposouresCount();
		
		
		LogUtils.logStep("2. Get the exposure metrics total with UI server call");
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
		ExposureTotals uiTotals = getUIExposuresMetricsTotal("Office 365", qparams);

		CustomAssertion.assertEquals(uiTotals.getInternalExposouresCount(), countInternal, 
										"UI server count:"+uiTotals.getInternalExposouresCount() +" is not matching with api server count:"+countInternal);
		
		CustomAssertion.assertEquals(uiTotals.getExternalExposouresCount(), countExternal, 
										"UI server count:"+uiTotals.getExternalExposouresCount() +" is not matching with api server count:"+countExternal);

		CustomAssertion.assertEquals(uiTotals.getPublicExposouresCount(), countPublic, 
										"UI server count:"+uiTotals.getPublicExposouresCount() +" is not matching with api server count:"+countPublic);
		
		LogUtils.logStep("3. Get the exposed docs count and verify it is sum of all exposures");
		//Also get the exposed docs count and check
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
		SecurletDocument apiCallDocs = getExposedDocuments(elapp.el_office_365.name(), docparams);
		
		CustomAssertion.assertTrue((countPublic + countInternal + countExternal) >=
				apiCallDocs.getMeta().getTotalCount(), "Exposed docs and Exposure metrics total are not same");

	}
	

	/**
	 * Test 2
	 * 
	 * This test exposes/unexposes a file publically, internally and externally and check the exposure total getting incremented/decremented
	 * 
	 * @throws Exception
	 */
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "exposureTypeProvider", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposureCountAfterExposure(String filename, String exposureType, int role,  String server) throws Exception {
		Reporter.log("Starting the test to upload and share "+ filename + " with " + exposureType + " access "
				+ "and role "+ role + " (1-Read, 2-Edit) and check the exposure totals", true);
		ItemResource itemResource = null;
		
		Reporter.log("**********************************************************", true);
		Reporter.log("External User:" + suiteData.getSaasAppExternalUser1Name(), true);
		Reporter.log("**********************************************************", true);
		
		try {

			long countPublic   = 0;
			long countInternal = 0;
			long countExternal = 0;

			List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
			qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));

			ExposureTotals expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

			countPublic   = expsoureTotals.getPublicExposouresCount();
			countInternal = expsoureTotals.getInternalExposouresCount();
			countExternal = expsoureTotals.getExternalExposouresCount();

			Reporter.log("Verifying the exposure totals for internally owned documents before exposure", true);
			Reporter.log("Public count:"  + countPublic,   true);
			Reporter.log("Internal count:"+ countInternal, true);
			Reporter.log("External count:"+ countExternal, true);

			//Create a file and share it
			itemResource = uploadAndShareDocument(filename, exposureType, role);

			//3 mins sleep
			sleep(CommonConstants.FIVE_MINUTES_SLEEP);

			//After external exposure
			Reporter.log("Getting the exposure count after exposure", true);
			expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

			Reporter.log("Public count after exposure  :"+ expsoureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count after exposure:"+ expsoureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count after exposure:"+ expsoureTotals.getExternalExposouresCount(), true);

			if (exposureType.equalsIgnoreCase(ExposureTypes.PUBLIC.name())) {
				CustomAssertion.assertEquals(expsoureTotals.getPublicExposouresCount(),   countPublic+1,  "Public exposure count not incremented");
				CustomAssertion.assertEquals(expsoureTotals.getInternalExposouresCount(), countInternal,  "Internal exposure shouldn't change");
				CustomAssertion.assertEquals(expsoureTotals.getExternalExposouresCount(), countExternal, "External exposure shouldn't change");

			} else if(exposureType.equalsIgnoreCase(ExposureTypes.EXTERNAL.name())) {
				CustomAssertion.assertEquals(expsoureTotals.getPublicExposouresCount(),   countPublic, "Public exposure shouldn't change");
				CustomAssertion.assertEquals(expsoureTotals.getInternalExposouresCount(), countInternal, "Internal exposure shouldn't change");
				CustomAssertion.assertEquals(expsoureTotals.getExternalExposouresCount(), countExternal+1,  "External exposure count not incremented");

			} else if(exposureType.equalsIgnoreCase(ExposureTypes.INTERNAL.name())) {
				CustomAssertion.assertEquals(expsoureTotals.getPublicExposouresCount(),   countPublic,    "Public exposure count shouldn't change");
				CustomAssertion.assertEquals(expsoureTotals.getInternalExposouresCount(), countInternal+1, "Internal exposure count not incremented");
				CustomAssertion.assertEquals(expsoureTotals.getExternalExposouresCount(), countExternal, "External exposure shouldn't change");
			}

			Reporter.log("Disabling the sharing ", true);
			universalApi.disableSharing(itemResource.getName());

			//5 mins sleep
			sleep(CommonConstants.FIVE_MINUTES_SLEEP);

			expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

			Reporter.log("Getting the exposure count after removing the exposure", true);
			Reporter.log("Public count after exposure  :"+ expsoureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count after exposure:"+ expsoureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count after exposure:"+ expsoureTotals.getExternalExposouresCount(), true);

			
			if(exposureType.equalsIgnoreCase(ExposureTypes.EXTERNAL.name())) {
				assertEquals(expsoureTotals.getExternalExposouresCount(), countExternal, "External exposure count not decremented");
			}
			
			if(exposureType.equalsIgnoreCase(ExposureTypes.INTERNAL.name())) {
				assertEquals(expsoureTotals.getInternalExposouresCount(), countInternal, "Internal exposure count not decremented");
			}
			
			if(exposureType.equalsIgnoreCase(ExposureTypes.PUBLIC.name())) {
				assertEquals(expsoureTotals.getPublicExposouresCount(),   countPublic,   "Public exposure count not decremented");
			}

		}
		finally{
			universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
			Reporter.log("*******************************************************************************************************************", true);
		}
	}


	/**
	 * Test 3
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL"})
	public void verifyExposedDocumentsCountAfterExposure() throws Exception {
		String[] messages = {"1. Upload a file and expose it.",
				"2. Get the list of exposed files and check the parameters.", 
				"3. Get the other risks and the file should not be present.",
				"4. Unexpose the file and check the exposed files.",
		"5. Get the list of exposed files and files should not be present."};

		LogUtils.logTestDescription(messages);
		ItemResource itemResource = null;
		try {

			String filename = "test.pdf";
			//Create a file and share it

			itemResource = uploadAndShareDocument(filename, ExposureTypes.PUBLIC.name(), 1);

			//3 mins sleep
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
			docparams.add(new BasicNameValuePair("name",  itemResource.getName()));
			SecurletDocument documents = getExposedDocuments(elapp.el_office_365.name(), docparams);

			Reporter.log("Checking the publicly shared document is present in the expsosed files tab...", true);
			docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount, itemResource.getName(), "File");

			//Get the other risk and check the document it is not listed
			Reporter.log("Checking the publicly shared document is present in the other risks tab...", true);
			SecurletDocument riskyDocuments = getRiskyDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");

			Reporter.log("Disabling the share on  "+ itemResource.getName(), true);
			universalApi.disableSharing(itemResource.getName());

			//3 mins sleep
			Reporter.log("Going to Sleep now ... " + CommonConstants.THREE_MINUTES_SLEEP, true);
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

			documents = getExposedDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Other risk is not zero");

			riskyDocuments = getRiskyDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");
		}

		finally{
			universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		}
	}

	/**
	 * Test 4
	 * Upload a risky file 
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL"})
	public void verifyExposedRiskyDocumentsCountAfterExposure() throws Exception {
		String[] messages = {"1. Upload a risk file and expose it.",
				"2. Get the list of exposed files and check the parameters.", 
				"3. Get the other risks and the file should not be present.",
				"4. Unexpose the file and check the exposed files.",
		"5. Get the list of exposed files and files should not be present."};

		LogUtils.logTestDescription(messages);
		ItemResource itemResource = null;
		try {

			String filename = "PII_PCI_EmployeeList.xlsx";
			//Create a file and share it

			itemResource = uploadAndShareDocument(filename, ExposureTypes.PUBLIC.name(), 1);

			//3 mins sleep
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
			docparams.add(new BasicNameValuePair("name",  itemResource.getName()));
			SecurletDocument documents = getExposedDocuments(elapp.el_office_365.name(), docparams);

			Reporter.log("Checking the publicly shared document is present in the expsosed files tab...", true);
			docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount, itemResource.getName(), "File");

			//Get the other risk and check the document it is not listed
			Reporter.log("Checking the publicly shared document is present in the other risks tab...", true);
			SecurletDocument riskyDocuments = getRiskyDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");

			Reporter.log("Disabling the share on  "+ itemResource.getName(), true);
			universalApi.disableSharing(itemResource.getName());

			//3 mins sleep
			Reporter.log("Going to Sleep now ... " + CommonConstants.THREE_MINUTES_SLEEP, true);
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

			documents = getExposedDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Other risk is not zero");

			riskyDocuments = getRiskyDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 1, "Other risk should not be zero");
		}

		finally{
			//universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		}
	}

	
	@Test
	public void getObject() throws Exception {
		SecurletRemediation sr =  getOneDriveRemediationObject("userId", "file", "DociD", "", new String[]{"SHARE_ACCESS", "UNSHARE", "UNSHARE", "COLLAB_REMOVE"},
		new String[]{"Everyone-Read", "Everyone except external users-Read", "open-view", "pushpan@gmail.com"});
		
		Reporter.log(MarshallingUtils.marshall(sr), true);
		
		
		
		//Prepare the UI remediation object
		UIRemediationObject UIRemedObject = new UIRemediationObject();
		UIRemediationSource UISource = new UIRemediationSource();
		UIRemediationInnerObject UIInnerObject = new UIRemediationInnerObject();
		ArrayList<SecurletRemediation> remedList = new ArrayList<SecurletRemediation>();
		remedList.add(sr);
		UIInnerObject.setObjects(remedList);
		UISource.setObjects(UIInnerObject);
		UISource.setApp(facility.Salesforce.name());
		UIRemedObject.setSource(UISource);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
		
		//Perform the UI remediation
		//remediateExposureWithUIServer(UIRemedObject);
		
	}
	
	
	private SecurletRemediation getOneDriveRemediationObject(String userId, String docType, String instance, String docId, String code[], String[] metaInfo) {
		SecurletRemediation remediation = new SecurletRemediation();

		remediation.setDbName(suiteData.getTenantName().toLowerCase());
		remediation.setUser(suiteData.getSaasAppUsername());
		remediation.setUserId("");
		remediation.setDocType(docType);
		remediation.setDocId(docId);
		remediation.setInstance(instance);
		
		List<RemedialAction> actions = new ArrayList<RemedialAction>();
		
		
		for (int i = 0; i < code.length; i++) {
			RemedialAction remedAction = new RemedialAction();
			if (code[i].equals("COLLAB_REMOVE")) {
				remedAction.setCode(code[i]);
				remedAction.setReadonlyValues(null);
				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				String[] collabs = StringUtils.split(metaInfo[i], ",");
				remedMetaInfo.setCollabs(Arrays.asList(collabs));
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			}
			
			if (code[i].equals("UNSHARE")) {
				remedAction.setCode(code[i]);
				remedAction.setReadonlyValues(readonlyValues);
				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				remedMetaInfo.setCurrentLink(metaInfo[i]);
				remedAction.setMetaInfo(remedMetaInfo);
				remedMetaInfo.setCollabs(null);
				actions.add(remedAction);
			}
			
			if (code[i].equals("SHARE_ACCESS")) {
				remedAction.setCode(code[i]);
				remedAction.setPossibleValues(possibleValues);
				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				//remedMetaInfo.setCurrentLink(metaInfo[i]);
				remedMetaInfo.setAccess(metaInfo[i]);
				remedMetaInfo.setCollabs(null);
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			}
			
			
		}
		
		remediation.setActions(actions);
		return remediation;
		
	}
//		for(int i=0; i<remedialAction.length; i++) {
//			RemedialAction remedAction = new RemedialAction();
//			
//			if(remedialAction[i].equals("UNSHARE")) {
//
//				remedAction.setCode(remedialAction[i]);
//				remedAction.setReadonlyValues(readonlyValues);
//				//remedAction.setPossibleValues(possibleValues);
//				
//				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
//				remedMetaInfo.setCollabs(null);
//				remedMetaInfo.setCurrentLink(metaInfo[i]);
//				
//				remedAction.setMetaInfo(remedMetaInfo);
//				actions.add(remedAction);
//				
//			}
//			
//			if(remedialAction[i].equals("SHARE_ACCESS")) {
//				
//				remedAction.setCode(remedialAction[i]);
//				//remedAction.setReadonlyValues(readonlyValues);
//				remedAction.setPossibleValues(possibleValues);
//				
//				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
//				remedMetaInfo.setCollabs(null);
//				remedMetaInfo.setAccess(metaInfo[i]);
//				remedMetaInfo.setCurrentLink(currentLink);
//				
//				remedAction.setMetaInfo(remedMetaInfo);
//				actions.add(remedAction);
//			} 
//		}
//	
//		remediation.setActions(actions);
//		return remediation;
//	}
	
	
	
	public ExposureTotals  getOffice365MetricsWithAPI(boolean isInternal, List<NameValuePair> queryParams) throws Exception  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
		ExposureTotals exposureTotals = getUIExposuresMetricsTotal(elapp.el_office_365.name(), qparams);
		return exposureTotals;
	}
	
	private SecurletRemediation getBulkRemediationObject(ArrayList<String> collaboratorName) {
		SecurletRemediation securletRemediation = new SecurletRemediation();

		securletRemediation.setDbName(suiteData.getTenantName());
		securletRemediation.setUser("__ALL_EL__");
		securletRemediation.setUserId("__ALL_EL__");
		securletRemediation.setDocType("__ALL_EL__");
		securletRemediation.setDocId("__ALL_EL__");

		//Meta Info
		RemediationMetaInfo remediationMetaInfo = new RemediationMetaInfo();
		remediationMetaInfo.setCurrentLink(null);
		remediationMetaInfo.setCollabs(collaboratorName);
		remediationMetaInfo.setExpireOn(null);
		remediationMetaInfo.setAccess(null);

		List<RemedialAction> actions = new ArrayList<RemedialAction>();
		RemedialAction remedialAction = new RemedialAction();
		remedialAction.setCode("COLLAB_REMOVE");
		remedialAction.setPossibleValues(new ArrayList<String>());
		remedialAction.setMetaInfo(remediationMetaInfo);
		remedialAction.setCodeName("Remove");
		actions.add(remedialAction);

		securletRemediation.setActions(actions);
		return securletRemediation;
	}	

	public ItemResource uploadAndShareDocument(String filename, String exposureType, int role) throws Exception {
		//Upload file 
		ItemResource itemResource = universalApi.uploadSimpleFile("/", filename, uniqueId +"_"+filename);
		Reporter.log("Item Resource:"+ itemResource.getId(), true);
		Reporter.log("Item Name:"+ itemResource.getName(), true);

		SharingUserRoleAssignment shareObject = null;
		if (exposureType.equalsIgnoreCase("public")) {
			Reporter.log("Going to upload "+ itemResource.getName() + " and share the document publicly ...", true);
			shareObject = onedriveUtils.getPublicShareObject(itemResource.getWebUrl(), role);
			DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
			Reporter.log(MarshallingUtils.marshall(docSharingResult), true);

		} else if (exposureType.equalsIgnoreCase("internal")) {
			Reporter.log("Going to upload "+ itemResource.getName() + " and share the document with internal user ...", true);
			shareObject = onedriveUtils.getFileShareObject(itemResource, role, "Everyone except external users");
			DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
			Reporter.log(MarshallingUtils.marshall(docSharingResult), true);

		} else if (exposureType.equalsIgnoreCase("external")) {
			Reporter.log("Going to upload "+ itemResource.getName() + " and share the document with external user ...", true);
			shareObject = onedriveUtils.getFileShareObject(itemResource, role, this.suiteData.getSaasAppExternalUser1Name());
			DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
			Reporter.log(MarshallingUtils.marshall(docSharingResult), true);
		}

		return itemResource;
	}
	
	

}