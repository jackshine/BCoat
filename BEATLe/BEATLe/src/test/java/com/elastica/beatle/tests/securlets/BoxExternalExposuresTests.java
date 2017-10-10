package com.elastica.beatle.tests.securlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.dto.CiqProfile;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.TotalObject;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.elastica.beatle.securlets.dto.UISource;
import com.elastica.beatle.securlets.dto.VulnerabilityTypes;
import com.universal.common.UniversalApi;
import com.universal.constants.CommonConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.Collaborations;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;



public class BoxExternalExposuresTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	DocumentValidator docValidator;
	CIQValidator ciqValidator;
	String resourceId;
	BoxUserInfo userInfo;
	String shareExpiry ;
	String instanceId;
	UniversalApi universalApiExternal;
	protected UserAccount saasAppExternalUserAccount;
	//Variables to hold internal collaborator change
	protected String folderId;
	protected BoxFolder folderObj;
	protected BoxCollaboration collaboration;
	protected String collaborationId;
	String collaborator = "testuser1@securletbeatle.com";

	public BoxExternalExposuresTests() throws Exception {
		esQueryBuilder = new ESQueryBuilder();
		docValidator = new DocumentValidator();
		shareExpiry = DateUtils.getDaysFromCurrentTime(1);
	}
	
	
	@BeforeTest(alwaysRun=true)
	public void initInstance() throws Exception {
		this.instanceId = this.getTenantInstanceId(facility.Box.name());
		instanceId = "";
		Reporter.log("Instance Name:"+instanceId, true);
		
		saasAppExternalUserAccount = new UserAccount(suiteData.getSaasAppExternalUser1Name(), suiteData.getSaasAppExternalUser1Password(), 
																				suiteData.getSaasAppUserRole());
		universalApiExternal = new UniversalApi(suiteData.getSaasApp(), saasAppExternalUserAccount);
	}
	
	/**
	 * Test 1
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyMetricsTotalBeforeExposureWithUIAndAPIServerCalls() throws Exception {
		String steps[] = {
				"1. Get exposed file metrics with UI Server call.",
				"2. Get exposed file metrics with API Server call.",
				"3. Verify both are same." };

		LogUtils.logTestDescription(steps);

		


		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));

		Reporter.log("External exposure metrics totals with API server call", true);
		ExposureTotals apiCallTotals = getExposuresMetricsTotal(elapp.el_box.name(), qparams);
		long apiCountPublic 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternal 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternal 	= apiCallTotals.getExternalExposouresCount();

		Reporter.log("API call Public count  :"+apiCountPublic, true);
		Reporter.log("API call Internal count:"+apiCountInternal, true);
		Reporter.log("API call External count:"+apiCountExternal, true);

		Reporter.log("External exposure metrics totals with UI server call", true);
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  Boolean.FALSE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));

		ExposureTotals uiCallTotals = getUIExposuresMetricsTotal(elapp.el_box.name(), qparams);
		long uiCountPublic 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternal 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternal 	= uiCallTotals.getExternalExposouresCount();

		Reporter.log("UI Call Public count  :"+uiCountPublic, true);
		Reporter.log("UI Call Internal count:"+uiCountInternal, true);
		Reporter.log("UI Call External count:"+uiCountExternal, true);

		CustomAssertion.assertEquals(apiCountPublic, uiCountPublic, 
				"API public count "+apiCountPublic+" is not matching with UI public count "+uiCountPublic);
		CustomAssertion.assertEquals(apiCountInternal, uiCountInternal, 
				"API internal count "+apiCountInternal+" is not matching with UI internal count "+uiCountInternal);
		CustomAssertion.assertEquals(apiCountExternal, uiCountExternal, 
				"API internal count "+apiCountExternal+" is not matching with UI internal count "+uiCountExternal);


	}
	
	
	/**
	 * Test 2
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyVennDiagramMetricsAfterExternalExposure() throws Exception {
		
		String steps[] = {
				"1. This test check the metrics as depicted in venn diagram after a external exposure and after removing it",
				"2. Check the metrics as depicted in Venn Diagram with API server call.",
				"3. Check the metrics as depicted in Venn Diagram with UI  server call.",
				"4. Verify both metrics are same."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. GET exposure metrics totals with API server call and UI Server call before exposure");
		
		
		ExposureTotals apiCallTotalsBeforeExposure = getBoxExposureMetricsWithAPI(false, null);
		long apiCountInternalBeforeExposure = apiCallTotalsBeforeExposure.getInternalExposouresCount();
		long apiCountExternalBeforeExposure = apiCallTotalsBeforeExposure.getExternalExposouresCount();
		long apiCountPublicBeforeExposure 	= apiCallTotalsBeforeExposure.getPublicExposouresCount();
		
		ExposureTotals uiCallTotalsBeforeExposure = getBoxExposureMetricsWithUI(false, null);
		long uiCountExternalBeforeExposure = uiCallTotalsBeforeExposure.getExternalExposouresCount();
		
		
		LogUtils.logStep("2. Expose a file externally and get the metrics");
		
		String collaboratorRole 	= "editor";
		this.createFolderAndCollaborateWithUser(collaborator, collaboratorRole);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		LogUtils.logStep("3. GET exposure metrics totals with API server call and UI Server call after exposure");
		
		ExposureTotals uiCallTotalsAfterExposure =  getBoxExposureMetricsWithUI(false, null);
		long uiCountPublicAfterExposure 	= uiCallTotalsAfterExposure.getPublicExposouresCount();
		long uiCountInternalAfterExposure 	= uiCallTotalsAfterExposure.getInternalExposouresCount();
		long uiCountExternalAfterExposure 	= uiCallTotalsAfterExposure.getExternalExposouresCount();
		
		
		ExposureTotals apiCallTotalsAfterExposure = getBoxExposureMetricsWithAPI(false, null);
		long apiCountPublicAfterExposure 	= apiCallTotalsAfterExposure.getPublicExposouresCount();
		long apiCountInternalAfterExposure 	= apiCallTotalsAfterExposure.getInternalExposouresCount();
		long apiCountExternalAfterExposure 	= apiCallTotalsAfterExposure.getExternalExposouresCount();
		
		LogUtils.logStep("4. Verifying external exposure count");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure, apiCountExternalBeforeExposure+1, "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(uiCountExternalAfterExposure, uiCountExternalBeforeExposure+1, "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure,  uiCountExternalAfterExposure,  "UI External exposure count and API External exposure count should be equal");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  uiCountPublicAfterExposure,  "UI public exposure count and API public exposure count should be equal");
		CustomAssertion.assertEquals(apiCountInternalAfterExposure,  apiCountInternalBeforeExposure,  "Internal count should not change after External exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  apiCountPublicBeforeExposure,  "UI public exposure count and API public exposure count should be equal");
		
		LogUtils.logStep("5. Delete the collaboration from the external user account.");
		universalApiExternal.deleteCollaboration(collaboration);
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("6. GET External exposure metrics totals with API server and UI server call");
		
		ExposureTotals apiCallTotals = getBoxExposureMetricsWithAPI(false, null);
		long apiCountPublicAfterCleaningExposure 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternalAfterCleaningExposure 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternalAfterCleaningExposure 	= apiCallTotals.getExternalExposouresCount();
		
		ExposureTotals uiCallTotals = getBoxExposureMetricsWithUI(false, null);
		long uiCountPublicAfterCleaningExposure 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternalAfterCleaningExposure 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternalAfterCleaningExposure 	= uiCallTotals.getExternalExposouresCount();
		
		LogUtils.logStep("7. Verifying External exposure after cleaning up the exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure, apiCountExternalBeforeExposure, "API External exposure count not decremented by 1 after cleaning exposure");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure,  uiCountInternalAfterCleaningExposure,  "API internal count and UI internal count are not same");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure,  uiCountExternalAfterCleaningExposure,  "API external count and UI external count are not same");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure,    uiCountPublicAfterCleaningExposure, "API Public exposure count  and UI public count are not same");
	}	
	
	
	
	/**
	 * Test 3
	 * @param collaborator
	 * @param collaboratorRole
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"}, dataProviderClass = BoxDataProvider.class, dataProvider = "ExternalUserInternalCollaborationDataProvider")
	public void verifyCollaborateFolderAndChangeExternalCollaboratorRoles(String collaborator, String collaboratorRole) throws Exception {
		String steps[] = {
					"1. Create a folder in box with an external user account and add an internal user as collaborator ",
					"2. Get the list of exposed files in cloudSoc. It should list the file with the external switch.",
					"3. Remove the collaboration and check the file is removed from the list." };
		
		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Get the list of exposed files before exposure.");
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		SecurletDocument apiCallDocs = getExposedDocuments(elapp.el_box.name(), docparams);
		int beforeExposure = apiCallDocs.getMeta().getTotalCount();
	
		
		LogUtils.logStep("2. Create a new folder and collaborate with a collaborator.");
		//Create the folder and collaboration as a prerequisite
		//Possible roles editor, viewer, previewer, uploader, previewer uploader, viewer uploader, co-owner, or owner
		createFolderAndCollaborateWithUser(collaborator, collaboratorRole);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		LogUtils.logStep("3. Get the list of exposed files before exposure.");
		apiCallDocs = getExposedDocuments(elapp.el_box.name(), docparams);
		int afterExposure = apiCallDocs.getMeta().getTotalCount();
		CustomAssertion.assertEquals(afterExposure, beforeExposure + 1, "Exposed files not incremented after external exposure with external switch.");
		
		String foldername = "External Folder("+ this.folderObj.getId() +")";
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		qparams.add(new BasicNameValuePair("name",  foldername));
		
		LogUtils.logStep("4. After exposure, checking the externally shared document is present in the exposed files tab with api server call...");
		SecurletDocument documents = getExposedDocuments(elapp.el_box.name(), qparams);
		docValidator.verifyExternallyExposedDocumentWithExternalSwitch(documents, this.saasAppExternalUserAccount,  foldername, "folder", collaborator);
		
		//UI Server 
		/*
		LogUtils.logStep("5. Checking the externally shared document is present in the exposed files tab with UI server call..");
		SecurletDocument uiDocuments = getExposedDocumentsWithUIServerCall(facility.Box.name(), foldername);
		CustomAssertion.assertTrue((documents.getMeta().getTotalCount() == uiDocuments.getMeta().getTotalCount()), "UI Exposed docs and API Exposed docs are same", "UI Exposed docs and API Exposed docs are not same");
		*/
		
		LogUtils.logStep("6. Remove the collaboration with the external user account...");
		universalApiExternal.deleteCollaboration(collaboration);
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		LogUtils.logStep("7. Remove the collaboration with the external user account...");
		universalApiExternal.deleteCollaboration(collaboration);
		
		LogUtils.logStep("8. After removing collaboration, checking the externally shared document is still present in the exposed files tab...");
		documents = getExposedDocuments(elapp.el_box.name(), qparams);
		CustomAssertion.assertTrue((documents.getMeta().getTotalCount() == 0), "External folder collaborated with internal user is removed.", "External folder collaborated with internal user is not removed.");
		
		//UI Server 
		/*
		uiDocuments = getExposedDocumentsWithUIServerCall(facility.Box.name(), foldername);
		CustomAssertion.assertTrue((uiDocuments.getMeta().getTotalCount() == 0), "External folder collaborated with internal user is removed.", "External folder collaborated with internal user is not removed.");
		*/
	}
	
	
	/**
	 * Test 4
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyVulnerabilityTypes() throws Exception {
		
		LogUtils.logTestDescription("Get the vulnerability types and verify them");
		Logger.info( "Getting vulnerability types ...");
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", "false");
		additionalParams.put("vl_types", 	"all");
		
		//After external exposure
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_box", additionalParams);

		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};

		for (String vl : vulnerabilities) {
			Reporter.log("Verifying the vulnerability type:"+vl, true);
			Reporter.log("Vulnerability:" + vl + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(vl), true );
			CustomAssertion.assertTrue(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) >= 0, "Count is less than zero");
			additionalParams.put("vl_types", 	vl);
			//Check the filtered response as well.
			VulnerabilityTypes specificVulnerability = getVulnerabilityTypes("el_box", additionalParams);
			CustomAssertion.assertEquals(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) ,
					specificVulnerability.getObjects().getVulnerabilityCount(vl), "Specific count is not equal");
			
		}
		
		for(CiqProfile ciqprofile : vulnerabilityTypes.getObjects().getCiqProfiles()) {
			Reporter.log("Verifying the CIQ profile name:"+ciqprofile.getId(), true);
			Reporter.log("Ciq Profile name:" +ciqprofile.getId() + ":: Count:"+ciqprofile.getTotal(), true);
			CustomAssertion.assertTrue(ciqprofile.getId()!=null, "CIQ profile id is null");
			CustomAssertion.assertTrue(ciqprofile.getTotal() >= 0, "CIQ profile total is null");
		}
	}
	
	/**
	 * Test 5
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyContentTypes() throws Exception {
		LogUtils.logTestDescription("Get the ContentTypes and check.");
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", "false");
		
		ExposureTotals exposedContentTypes =  getExposedContentTypes(elapp.el_box.name(), additionalParams);
		
		CustomAssertion.assertTrue(exposedContentTypes.getObjects().size() >= 0, "File type exposure is less than zero");
		
		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getId()!= null, "Id is not null");
			CustomAssertion.assertTrue(totalObject.getTotal() >= 0, "Count is greater than or equal to zero");
		}
	}
	
	/**
	 * Test 6
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyFileTypes() throws Exception {
		Logger.info( "Getting file types ...");
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("top", "10");
		hmap.put("is_internal", "false");
		//After external exposure
		ExposureTotals fileTypes = getFileTypes(elapp.el_box.name(), hmap);
		
		CustomAssertion.assertTrue(fileTypes.getObjects().size() >= 0, "File type exposure is less than zero");
		
		for(TotalObject totalObject : fileTypes.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getId()!= null, "Id is null");
			CustomAssertion.assertTrue(totalObject.getTotal() >= 0, "Count is less than or equal to zero");
		}
		
	}
	
	
	/**
	 * Test 7
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyExposedUsersTotalsAsInVennDiagram() throws Exception {
		LogUtils.logTestDescription("This test verify exposed users");
		Logger.info( "Getting user totals ...");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
		
		ExposureTotals exposureTotals = getUserTotals(elapp.el_box.name(), qparams);
		
		Reporter.log("Verifying the internal user totals is greater than zero", true);
		for(TotalObject totalObject : exposureTotals.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getTotal() >=0 , totalObject.getId()  + " user total >= 0" , totalObject.getId()  + " user total < 0");
		}
	}
	
	/**
	 * Test 8
	 * @throws Exception
	 */
	
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyUserDocumentExposures() throws Exception {
		LogUtils.logTestDescription("This test verify the user documemt exposures");
		Logger.info( "Getting user totals ...");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		SecurletDocument documents = getUserExposures(elapp.el_box.name(), qparams);
		for (com.elastica.beatle.securlets.dto.Object object : documents.getObjects()) {
			CustomAssertion.assertTrue(object.getTotal() >=0, "User document exposure total can't be null");
			Reporter.log(String.valueOf(object.getTotal()), true);
		}
	}
	
	/**
	 * Test 9
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyExternalUsers() throws Exception {		
		
			String steps[] = {
					"1. This test verify the external users who exposed their documents with internal users"
					 };
		
			LogUtils.logTestDescription(steps);
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
			
			SecurletDocument documents = getExposedUsers(elapp.el_box.name(), docparams);
			CustomAssertion.assertTrue(documents.getObjects().size() >= 0, "Exposed user documents should be greater than or equal to zero");
			
			boolean found =false;
			
			for (com.elastica.beatle.securlets.dto.Object obj : documents.getObjects()) {
				if (obj.getEmail().equals(saasAppExternalUserAccount.getUsername())) {
					found = true;
				}
			}
			
			CustomAssertion.assertTrue(found, "External user "+ saasAppExternalUserAccount.getUsername() +" is present", "External user is not present");
			
	}
	
	/**
	 * Test 10
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyInternalCollabsOnExternalDocuments() throws Exception {		
		
			String steps[] = {
					"1. This test verify the internal collaborators on external documents"
					 };
		
			LogUtils.logTestDescription(steps);
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
			
			SecurletDocument documents = getCollaborators(elapp.el_box.name(), docparams);
			CustomAssertion.assertTrue(documents.getObjects().size() >= 0, "Internal collabs on external documents are greater than zero.");
			
			boolean found =false;
			
			for (com.elastica.beatle.securlets.dto.Object obj : documents.getObjects()) {
				if (obj.getEmail().equals(collaborator)) {
					found = true;
				}
			}
			
			CustomAssertion.assertTrue(found, "Internal user "+ collaborator +" is present", "Internal user is not present");
		
	}
	
	/**
	 * Test 11
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyExternalOwnersDocuments() throws Exception {		
		
			String steps[] = {
					"1. This test verify the external users documents who exposed their documents with internal users"
					 };
		
			LogUtils.logTestDescription(steps);
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			//docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
			docparams.add(new BasicNameValuePair("owned_by", saasAppExternalUserAccount.getUsername()));
			
			SecurletDocument documents = getExposedDocuments(elapp.el_box.name(), docparams);
			int beforeExposure = documents.getMeta().getTotalCount();
			
			createFolderAndCollaborateWithUser(collaborator, "editor");
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			documents = getExposedDocuments(elapp.el_box.name(), docparams);
			int afterExposure = documents.getMeta().getTotalCount();
			
			CustomAssertion.assertEquals(afterExposure, beforeExposure+1, "External owner document count not incremented.");
			universalApiExternal.deleteCollaboration(collaboration);
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			documents = getExposedDocuments(elapp.el_box.name(), docparams);
			afterExposure = documents.getMeta().getTotalCount();
			CustomAssertion.assertEquals(afterExposure, beforeExposure, "External owner document count not decremented.");
			
	}
	
	
	/**
	 * Test 12
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL_SWITCH", "REGRESSION", "FILTER"})
	public void verifyInternalCollaboratorDocuments() throws Exception {		
		
			String steps[] = {
					"1. This test verify the external users documents who exposed their documents with internal users"
					 };
		
			LogUtils.logTestDescription(steps);
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			//docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
			docparams.add(new BasicNameValuePair("exposures.internal", collaborator));
			
			SecurletDocument documents = getExposedDocuments(elapp.el_box.name(), docparams);
			int beforeExposure = documents.getMeta().getTotalCount();
			
			createFolderAndCollaborateWithUser(collaborator, "editor");
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			documents = getExposedDocuments(elapp.el_box.name(), docparams);
			int afterExposure = documents.getMeta().getTotalCount();
			
			CustomAssertion.assertEquals(afterExposure, beforeExposure+1, "Internal collaborator document count not incremented.");
			universalApiExternal.deleteCollaboration(collaboration);
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			documents = getExposedDocuments(elapp.el_box.name(), docparams);
			afterExposure = documents.getMeta().getTotalCount();
			CustomAssertion.assertEquals(afterExposure, beforeExposure, "Internal collaborator  document count not decremented.");
			
	}
	
	public SecurletDocument getExposedDocumentsWithUIServerCall(String appName, String searchText) throws Exception {
		searchText = searchText == null ? "" : searchText;
		UIExposedDoc payload = new UIExposedDoc();
		UISource uisource = new UISource();
		uisource.setApp(appName);
		uisource.setLimit(20);
		uisource.setOffset(0);
		uisource.setIsInternal(Boolean.FALSE.booleanValue());
		uisource.setSearchTextFromTable(searchText);
		uisource.setSearchText(searchText);
		uisource.setOrderBy("name");
		uisource.setObjectType(appName);
		payload.setSource(uisource);
		
		List<NameValuePair> uiQueryParams = new ArrayList<NameValuePair>(); 
		
		LogUtils.logStep("Getting the list of exposed documents with UI server call");
		SecurletDocument uiCallDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		return uiCallDocs;
	}
	
	
	
	public ExposureTotals  getBoxExposureMetricsWithAPI(boolean isInternal, List<NameValuePair> queryParams) throws Exception  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		ExposureTotals exposureTotals = getExposuresMetricsTotal(elapp.el_box.name(), qparams);
		return exposureTotals;
	}
	
	public ExposureTotals  getBoxExposureMetricsWithUI(boolean isInternal, List<NameValuePair> queryParams) throws Exception  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		ExposureTotals exposureTotals = getUIExposuresMetricsTotal(elapp.el_box.name(), qparams);
		return exposureTotals;
	}
	
	
	
	public void createFolderAndCollaborateWithUser(String collaborator, String currentRole) throws Exception {
		
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		//create folder1
		folderObj = universalApiExternal.createFolder(uniqueId);
		folderId = folderObj.getId();

		//Perform folder operations
		//upload the file to folder1
		FileUploadResponse uploadResponse = universalApiExternal.uploadFile(folderId, sourceFile, destinationFile);
		String fileId = uploadResponse.getFileId();

		Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//create collaboration object for saas app
		CollaborationInput collabInput = new CollaborationInput();
		Item item = new Item();
		item.setId(folderObj.getId());
		item.setType(folderObj.getType());

		AccessibleBy aby = new AccessibleBy();
		aby.setName(uniqueId);
		aby.setType("user");
		aby.setLogin(collaborator);

		collabInput.setItem(item);
		collabInput.setAccessibleBy(aby);
		collabInput.setRole(currentRole);

		//Create the collaboration
		this.collaboration = universalApiExternal.createCollaboration(collabInput);
		this.collaborationId = collaboration.getId();
		Reporter.log("Collaboration Id:" + collaboration.getId(), true);
		
		//get the collaborations and assert them
		Collaborations collaborations = universalApiExternal.getFolderCollaborations(folderId);
		CustomAssertion.assertTrue(collaborations.getEntries().get(0).getRole().equals(currentRole), "Role " + currentRole +" set correctly in SaasApp ", "Role " + currentRole +" not set correctly in SaasApp ");
		CustomAssertion.assertTrue(collaborations.getEntries().get(0).getAccessibleBy().getLogin().equals(collaborator),"Login " + collaborator +" set correctly in SaasApp " ,"Login " + collaborator +" not set correctly in SaasApp ");
		
	}
}
