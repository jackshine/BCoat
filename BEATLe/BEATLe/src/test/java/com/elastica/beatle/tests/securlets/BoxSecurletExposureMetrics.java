package com.elastica.beatle.tests.securlets;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.ExposureTypes;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.BoxAction;
import com.elastica.beatle.securlets.dto.BoxDocument;
import com.elastica.beatle.securlets.dto.BoxMetaInfo;
import com.elastica.beatle.securlets.dto.BoxRemediation;
import com.elastica.beatle.securlets.dto.CiqProfile;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.TotalObject;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.elastica.beatle.securlets.dto.VlType;
import com.elastica.beatle.securlets.dto.VulnerabilityTypes;
import com.universal.common.UniversalApi;
import com.universal.constants.CommonConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.CEntry;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.Collaborations;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;
import com.universal.dtos.onedrive.ItemResource;



public class BoxSecurletExposureMetrics extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	DocumentValidator docValidator;
	CIQValidator ciqValidator;
	String resourceId;
	BoxUserInfo userInfo;
	String shareExpiry ;
	String instanceId;
	UniversalApi universalApiEndUser;
	protected UserAccount saasAppEndUserAccount;
	String externalUser = "pushpan@gmail.com";
	String userRole = "editor";
	DCIFunctions dciFunctions = null;
	
	public BoxSecurletExposureMetrics() throws Exception {
		esQueryBuilder = new ESQueryBuilder();
		docValidator = new DocumentValidator();
		shareExpiry = DateUtils.getDaysFromCurrentTime(1);
	}


	@BeforeTest(alwaysRun=true)
	public void initInstance() throws Exception {
		//this.instanceId = this.getTenantInstanceId(facility.Box.name());
		instanceId = "";
		Reporter.log("Instance Name:"+instanceId, true);
		
		saasAppEndUserAccount = new UserAccount(suiteData.getSaasAppEndUser1Name(), 
														suiteData.getSaasAppEndUser1Password(), "ENDUSER");
		universalApiEndUser = new UniversalApi(suiteData.getSaasApp(), saasAppEndUserAccount);
		
		dciFunctions = new DCIFunctions();
		try {
			String payload = dciFunctions.getEnableContentInspectionLog();
			dciFunctions.enableContentInspection(restClient, 
					dciFunctions.getCookieHeaders(suiteData), new StringEntity(payload), suiteData.getScheme(), suiteData.getHost());

		} catch (Exception ex) {
			Logger.info("Issue with Enabling of Content Inspection" + ex.getLocalizedMessage());
		}
		
	}
	

	
	@BeforeClass(groups ={"All"})
	public void enableContentInspection() {
		dciFunctions = new DCIFunctions();
		try {
			String payload = dciFunctions.getEnableContentInspectionLog();
			dciFunctions.enableContentInspection(restClient, 
					dciFunctions.getCookieHeaders(suiteData), new StringEntity(payload), suiteData.getScheme(), suiteData.getHost());

		} catch (Exception ex) {
			Logger.info("Issue with Enabling of Content Inspection" + ex.getLocalizedMessage());
		}
	}
	
	
	/**
	 * Test 1
	 * Check the metrics as depicted in venn diagram
	 * Expose the file publicly and check public exposure count is incremented
	 * Expose the file internally(company) and check internal count is incremented
	 * Files can't be exposed to collaborators internally.. so no check needed.
	 * 
	 */
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "metricsExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposuresMetricsForFileExposures(String testname, String folderId, String fileName, String access, String exposureType, String server) throws Exception {

		FileEntry sharedFile = null;
		try {
			String steps[] = {
					"1. This test check the metrics as depicted in venn diagram ",
					"2. Upload a file and share it with " + access + " access.",
			"3. Verify the exposure totals get incremented after the exposure." };

			LogUtils.logTestDescription(steps);

			//Get the exposure count
			long countPublic   = 0;
			long countInternal = 0;
			long countExternal = 0;

			List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
			qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));

			Reporter.log("Internal exposure metrics totals before the exposure", true);
			ExposureTotals exposureTotals = getExposuresMetricsTotal(elapp.el_box.name(), qparams);
			countPublic 	= exposureTotals.getPublicExposouresCount();
			countInternal 	= exposureTotals.getInternalExposouresCount();
			countExternal 	= exposureTotals.getExternalExposouresCount();

			Reporter.log("Public count  :"+countPublic, true);
			Reporter.log("Internal count:"+countInternal, true);
			Reporter.log("External count:"+countExternal, true);

			//Create a file and share it
			sharedFile = uploadFileAndShareit(folderId, fileName, access);

			//3 mins sleep
			sleep(CommonConstants.THREE_MINUTES_SLEEP);

			//After  exposure
			Reporter.log("Internal exposure metrics totals after the exposure", true);
			exposureTotals = getExposuresMetricsTotal(elapp.el_box.name(), qparams);

			Reporter.log("Public count  :"+exposureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count:"+exposureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count:"+exposureTotals.getExternalExposouresCount(), true);

			if (exposureType.equals(ExposureTypes.PUBLIC.name().toLowerCase())) {
				CustomAssertion.assertEquals(countPublic+1, exposureTotals.getPublicExposouresCount(), "Public exposure count not incremented");
				CustomAssertion.assertEquals(countInternal, exposureTotals.getInternalExposouresCount(), "Internal exposure shouldn't change");
				CustomAssertion.assertEquals(countExternal, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");

			} else if(exposureType.equals(ExposureTypes.EXTERNAL.name().toLowerCase())) {
				//As it is shared with collaborators on file basis, it should not get incremented
				CustomAssertion.assertEquals(countPublic,   exposureTotals.getPublicExposouresCount(), "Public exposure shouldn't change");
				CustomAssertion.assertEquals(countInternal, exposureTotals.getInternalExposouresCount(), "Internal exposure shouldn't change");
				CustomAssertion.assertEquals(countExternal, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");

			} else if(exposureType.equals(ExposureTypes.INTERNAL.name().toLowerCase())) {
				CustomAssertion.assertEquals(countPublic, exposureTotals.getPublicExposouresCount(), "Public exposure count shouldn't change");
				CustomAssertion.assertEquals(countInternal+1, exposureTotals.getInternalExposouresCount(), "Internal exposure count not incremented");
				CustomAssertion.assertEquals(countExternal, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");
			}

			//Delete the shared link and check the count. It should be decremented by one
			sharedFile = universalApi.disableSharedLink(sharedFile.getId());

			//3 mins sleep
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			Reporter.log("Internal exposure metrics totals after removing the exposure", true);
			exposureTotals = getExposuresMetricsTotal(elapp.el_box.name(), qparams);

			Reporter.log("Public count  :"+exposureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count:"+exposureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count:"+exposureTotals.getExternalExposouresCount(), true);

			CustomAssertion.assertEquals(countPublic, exposureTotals.getPublicExposouresCount(), "Public exposure count not incremented");
			CustomAssertion.assertEquals(countInternal, exposureTotals.getInternalExposouresCount(), "Internal exposure shouldn't change");
			CustomAssertion.assertEquals(countExternal, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");

		}
		finally{
			universalApi.deleteFile(sharedFile.getId(), sharedFile.getEtag());
		}
	}


	/**
	 * Test 2
	 * Check the metrics as depicted in venn diagram
	 * Expose the folder publicly and check public exposure count is incremented
	 * Expose the folder internally(company) and check internal count is incremented
	 * Expose the folder externally with the collaborators and check count is incremented
	 * 
	 */
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "metricsFolderExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposuresMetricsForFolderExposures(String testname, String fileName, String access, 
			String exposureType, String[] collaborators, String server) throws Exception {

		String steps[] = {
				"1. This test verify the metrics as depicted in venn diagram ",
				"2. Upload a folder and file and share it with " + access + " access.",
		"3. Verify the exposure totals get incremented after the exposure." };

		LogUtils.logTestDescription(steps);

		BoxFolder sharedFolder = null;
		String uniqueFolderId= UUID.randomUUID().toString();

		Reporter.log("Started test "+testname, true);

		//create folder1
		BoxFolder folderObj = universalApi.createFolder(uniqueFolderId);
		String folderId = folderObj.getId();

		try {

			//Get the exposure count
			long countPublic = 0;
			long countInternal = 0;
			long countExternal = 0;

			List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
			qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));

			Reporter.log("Internal exposure metrics totals before the exposure", true);
			ExposureTotals exposureTotals = getExposuresMetricsTotal(elapp.el_box.name(), qparams);

			countPublic = exposureTotals.getPublicExposouresCount();
			countInternal = exposureTotals.getInternalExposouresCount();
			countExternal = exposureTotals.getExternalExposouresCount();

			Reporter.log("Public count:"+countPublic, true);
			Reporter.log("Internal count:"+countInternal, true);
			Reporter.log("External count:"+countExternal, true);

			//Share the folder as specified in data providers
			Reporter.log("Exposing the folder ...", true);
			sharedFolder = shareTheFolderPubliclyOrWithCollaborators(folderId, fileName, access, collaborators, null);

			//3 mins sleep
			sleep(CommonConstants.THREE_MINUTES_SLEEP);

			//After exposure
			exposureTotals = getExposuresMetricsTotal(elapp.el_box.name(), qparams);
			Reporter.log("Internal exposure metrics totals after the exposure", true);
			Reporter.log("Public count:"+exposureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count:"+exposureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count:"+exposureTotals.getExternalExposouresCount(), true);

			if (exposureType.equals(ExposureTypes.PUBLIC.name().toLowerCase())) {
				CustomAssertion.assertEquals(countPublic+2, exposureTotals.getPublicExposouresCount(), "Public exposure count not incremented");
				CustomAssertion.assertEquals(countInternal, exposureTotals.getInternalExposouresCount(), "Internal exposure shouldn't change");
				CustomAssertion.assertEquals(countExternal, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");

			} else if (exposureType.equals(ExposureTypes.COMPANY.name().toLowerCase())) {
				CustomAssertion.assertEquals(countPublic, exposureTotals.getPublicExposouresCount(), "Public exposure count not incremented");
				CustomAssertion.assertEquals(countInternal+2, exposureTotals.getInternalExposouresCount(), "Internal exposure shouldn't change");
				CustomAssertion.assertEquals(countExternal, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");

			}else if(exposureType.equals(ExposureTypes.EXTERNAL.name().toLowerCase())) {
				//As it is shared with collaborators on file basis, it should not get incremented
				CustomAssertion.assertEquals(countPublic, exposureTotals.getPublicExposouresCount(), "Public exposure shouldn't change");
				CustomAssertion.assertEquals(countInternal, exposureTotals.getInternalExposouresCount(), "Internal exposure shouldn't change");
				CustomAssertion.assertEquals(countExternal+2, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");

			} else if(exposureType.equals(ExposureTypes.INTERNAL.name().toLowerCase())) {
				//If shared with one or two internal collabs, then it is not internal exposure
				CustomAssertion.assertEquals(countPublic, exposureTotals.getPublicExposouresCount(), "Public exposure count shouldn't change");
				CustomAssertion.assertEquals(countInternal, exposureTotals.getInternalExposouresCount(), "Internal exposure count not incremented");
				CustomAssertion.assertEquals(countExternal, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");

			} else if (exposureType.equals(ExposureTypes.INTERNAL_EXTERNAL.name().toLowerCase())) {
				//If shared with one or two internal collabs, then it is not internal exposure
				CustomAssertion.assertEquals(countPublic, exposureTotals.getPublicExposouresCount(), "Public exposure count shouldn't change");
				CustomAssertion.assertEquals(countInternal, exposureTotals.getInternalExposouresCount(), "Internal exposure count not incremented");
				CustomAssertion.assertEquals(countExternal+2, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");
			}

			//Delete the shared link and check the count. It should be decremented by one
			sharedFolder = universalApi.disableSharedLinkForFolder(sharedFolder.getId());

			Collaborations collaborations = universalApi.getFolderCollaborations(folderId);

			for (BoxCollaboration collaboration : collaborations.getEntries()) {
				universalApi.deleteCollaboration(collaboration);
			}

			sleep(CommonConstants.THREE_MINUTES_SLEEP);

			Reporter.log("Internal exposure metrics totals after removing the exposure", true);
			Reporter.log("Public count:"+exposureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count:"+exposureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count:"+exposureTotals.getExternalExposouresCount(), true);
			exposureTotals = getExposuresMetricsTotal(elapp.el_box.name(), qparams);

			CustomAssertion.assertEquals(countPublic, exposureTotals.getPublicExposouresCount(), "Public exposure count not incremented");
			CustomAssertion.assertEquals(countInternal, exposureTotals.getInternalExposouresCount(), "Internal exposure shouldn't change");
			CustomAssertion.assertEquals(countExternal, exposureTotals.getExternalExposouresCount(), "External exposure shouldn't change");

		}

		finally{

			//Delete the folder as well
			folderObj = universalApi.getFolderInfo(folderId);
			universalApi.deleteFolder(folderId, true, folderObj.getEtag());
		}
	}


	/**
	 * Test 3
	 * 1. Upload a risk file.
	 * 2. Expose it publically and check the file classified as risk file and check exposed files.
	 * 3. Also check in other risks. It should not listed there.
	 * 4. Remove the exposure. Check the file listed in other risks but not in Exposed files
	 * @throws Exception
	 */
	@Test
	public void exposeRiskFileAndCheckExposedFilesAndOtherRisks() throws Exception {

		String steps[] = {
				"1. This test verify the exposed document list after uploading and exposing a document.",
				"2. Upload a risk file and expose it publically.",
				"3. Verify the publicly shared document is present in the exposed files tab.",
				"4. Verify the publicly shared document is not present in the other risks tab.",
				"5. Remove the exposure.",
				"6. Verify the publicly shared document is not present in the exposed files tab.",
				"7. Verify the publicly shared document is present in the other risks tab.",
		};

		LogUtils.logTestDescription(steps);

		FileEntry sharedFile = null;
		try {

			String filename = "Box_HIPAA_Test2.txt";
			//Create a file and share it

			sharedFile = uploadFileAndShareit("/", filename, "open");

			sleep(CommonConstants.THREE_MINUTES_SLEEP);

			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
			docparams.add(new BasicNameValuePair("name",  sharedFile.getName()));
			SecurletDocument documents = getExposedDocuments(elapp.el_box.name(), docparams);

			Reporter.log("After exposure, checking the publicly shared document is present in the exposed files tab...", true);
			docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount, sharedFile.getName(), "file");

			//Get the other risk and check the document it is not listed
			Reporter.log("After exposure, checking the publicly shared document is present in the other risks tab...", true);
			SecurletDocument riskyDocuments = getRiskyDocuments(elapp.el_box.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");

			Reporter.log("Disabling the share on  "+ sharedFile.getName(), true);
			sharedFile = universalApi.disableSharedLink(sharedFile.getId());

			//3 mins sleep
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			

			Reporter.log("After removing exposure, checking the publicly shared document is still present in the exposed files tab...", true);
			documents = getExposedDocuments(elapp.el_box.name(), docparams);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed files count is not zero");

			Reporter.log("After removing exposure, checking the exposure moved to other risk...", true);
			riskyDocuments = getRiskyDocuments(elapp.el_box.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 1, "Other risk is not zero");
			docValidator.verifyUnexposedDocument(riskyDocuments, this.saasAppUserAccount, sharedFile.getName(), "file");
		}

		finally{
			//universalApi.deleteFile(sharedFile.getId(), sharedFile.getEtag());
		}
	}	


	/**
	 * Test 4
	 * 1. Upload a risk file.
	 * 2. Expose it publically and check the file classified as risk file and check exposed files.
	 * 3. Also check in other risks. It should not listed there.
	 * 4. Remove the exposure. Check the file listed in other risks but not in Exposed files
	 * @throws Exception
	 */
	@Test
	public void exposeNonRiskFileAndCheckExposedFilesAndOtherRisks() throws Exception {

		String steps[] = {
				"1. This test verify the exposed document list after uploading and exposing a document.",
				"2. Upload a nonrisk file and expose it publically.",
				"3. Verify the publicly shared document is present in the exposed files tab.",
				"4. Verify the publicly shared document is not present in the other risks tab.",
				"5. Remove the exposure.",
				"6. Verify the publicly shared document is not present in the exposed files tab.",
				"7. Verify the publicly shared document is not present in the other risks tab.",
		};

		LogUtils.logTestDescription(steps);

		FileEntry sharedFile = null;
		try {

			String filename = "test.pdf";
			//Create a file and share it

			sharedFile = uploadFileAndShareit("/", filename, "open");

			sleep(CommonConstants.THREE_MINUTES_SLEEP);

			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
			docparams.add(new BasicNameValuePair("name",  sharedFile.getName()));
			SecurletDocument documents = getExposedDocuments(elapp.el_box.name(), docparams);

			Reporter.log("After exposure, checking the publicly shared document is present in the exposed files tab...", true);
			docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount, sharedFile.getName(), "file");

			//Get the other risk and check the document it is not listed
			Reporter.log("After exposure, checking the publicly shared document is present in the other risks tab...", true);
			SecurletDocument riskyDocuments = getRiskyDocuments(elapp.el_box.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");

			Reporter.log("Disabling the share on  "+ sharedFile.getName(), true);
			sharedFile = universalApi.disableSharedLink(sharedFile.getId());

			//3 mins sleep
			sleep(CommonConstants.THREE_MINUTES_SLEEP);

			Reporter.log("After removing exposure, checking the publicly shared document is still present in the exposed files tab...", true);
			documents = getExposedDocuments(elapp.el_box.name(), docparams);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed files count is not zero");

			Reporter.log("After removing exposure, checking the exposure not moved to other risk...", true);
			riskyDocuments = getRiskyDocuments(elapp.el_box.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");
		}

		finally{
			universalApi.deleteFile(sharedFile.getId(), sharedFile.getEtag());
		}
	}	


	/**
	 * Test 5
	 * 
	 * List all internally exposed files and validate all documents
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void listAllInternallyExposedFiles() throws Exception {
		LogUtils.logTestDescription("Retrieve all internally exposed files and verify each and every field as per schema for all documents.");

		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("exposures.types", "all_internal");

		Reporter.log("Fetching the box exposed documents to find the total count.", true);
		BoxDocument documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);

		//Fetch all the documents in one query
		int limit = documents.getMeta().getTotalCount();
		additionalParams.put("offset", "0");
		additionalParams.put("limit",  String.valueOf(limit));
		Reporter.log("Fetching all the box exposed documents", true);
		documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);

		//Verify all internally owned documents
		Reporter.log("Validating all the box internally owned exposed documents...", true);
		docValidator.validateExposedDocuments(documents, true);
	}


	/**
	 * Test 6
	 * 
	 * List all internally exposed files after internal exposure
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void listAllInternallyExposedFilesAfterInternalExposure() throws Exception {

		LogUtils.logTestDescription("Retrieve all internally exposed files and verify only the exposed document and its fields.");

		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("exposures.types", "all_internal");
		additionalParams.put("offset", "0");
		additionalParams.put("limit", "1000");

		Reporter.log("Getting all the documents ...", true);
		BoxDocument documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);
		int countBefore = documents.getMeta().getTotalCount();

		Reporter.log("Upload a file and share it ...", true);
		//Expose a document internally and check the count
		FileEntry sharedFile = uploadFileAndShareit("/", "Hello.java", "company");

		//Wait for three mins
		sleep(CommonConstants.THREE_MINUTES_SLEEP);

		documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);
		int countAfter = documents.getMeta().getTotalCount();

		//validate the newly added record count
		Reporter.log("Verifying the count before and after exposure ...", true);
		CustomAssertion.assertEquals(countAfter, countBefore+1, "Internally exposed document not returned");
		CustomAssertion.assertTrue(MarshallingUtils.marshall(documents).contains(sharedFile.getId()), "Internally exposed document not returned in the records");



		//validate the newly added document
		//Pass the additional params
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("createdBy", this.saasAppUserAccount.getUsername());
		hmap.put("ownedBy", this.saasAppUserAccount.getUsername());
		hmap.put("name", sharedFile.getName());
		hmap.put("format", "java");
		hmap.put("ownerId", sharedFile.getOwnedBy().getId());
		hmap.put("size", String.valueOf(sharedFile.getSize()));
		hmap.put("parentName", "All Files");
		hmap.put("docType", "file");

		docValidator.verifyDocument(documents, sharedFile.getId(), "ALL_INTERNAL", "company", hmap);

		Reporter.log("Disable the sharing on the file ...", true);
		//remove the exposure
		sharedFile = universalApi.disableSharedLink(sharedFile.getId());

		//Wait for three mins
		sleep(CommonConstants.THREE_MINUTES_SLEEP);

		documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);
		CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), countBefore, "Internally exposed document count not decremented");
		CustomAssertion.assertFalse(MarshallingUtils.marshall(documents).contains(sharedFile.getId()), "Internally exposed document not returned in the records");

		//clean up file
		universalApi.deleteFile(sharedFile.getId(), sharedFile.getEtag());

	}

	/**
	 * Test 7
	 * 
	 * Get only the internal exposure totals
	 * And verify the internal count incremented after one internal exposure
	 * Also it should be decremented when the exposure is deleted
	 * @throws Exception
	 */

	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void getAllInternalExposureTotalsOnly() throws Exception {
		LogUtils.logTestDescription("Get all internal exposure totals");
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("file_exposures", "internal");
		ExposureTotals beforeExposureTotals = getExposuresMetricsTotal("true", facility.Box.name(), additionalParams);

		long internalCount = beforeExposureTotals.getInternalExposouresCount();

		//Expose a document internally and check the count
		FileEntry sharedFile = uploadFileAndShareit("/", "Hello.java", "company");

		//Wait for three mins
		sleep(CommonConstants.THREE_MINUTES_SLEEP);

		ExposureTotals afterExpsoureTotals = getExposuresMetricsTotal("true", facility.Box.name(), additionalParams);

		CustomAssertion.assertEquals(afterExpsoureTotals.getInternalExposouresCount(), beforeExposureTotals.getInternalExposouresCount()+1, "Internally exposed document count not incremented");
		CustomAssertion.assertEquals(afterExpsoureTotals.getExternalExposouresCount(), beforeExposureTotals.getExternalExposouresCount(), "External exposure value greater than zero in internal only filter");
		CustomAssertion.assertEquals(afterExpsoureTotals.getPublicExposouresCount(), beforeExposureTotals.getPublicExposouresCount(), "Public exposure value greater than zero in internal only filter");

		//remove the exposure
		sharedFile = universalApi.disableSharedLink(sharedFile.getId());

		//Wait for three mins
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		afterExpsoureTotals = getExposuresMetricsTotal("true", facility.Box.name(), additionalParams);

		CustomAssertion.assertEquals(afterExpsoureTotals.getInternalExposouresCount(), internalCount, "Internally exposed document count not decrmented");

		//clean up file
		universalApi.deleteFile(sharedFile.getId(), sharedFile.getEtag());
	}



	/**
	 * Test 8
	 * 
	 * Verify the top 10 exposed file types and check count is always greater than zero
	 * 
	 * 
	 */
	@Test ( groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyTopExposedFileTypes() throws Exception {

		LogUtils.logTestDescription("Get top exposed file types");

		//After external exposure
		ExposureTotals exposedFileTypes = getExposedFileTypes("el_box", "true", "10");

		CustomAssertion.assertTrue(exposedFileTypes.getObjects().size() >= 0, "File type exposure is less than zero");

		for(TotalObject totalObject : exposedFileTypes.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getId()!= null, "Id is null");
			CustomAssertion.assertTrue(totalObject.getTerm()!= null, "Term is null");

			CustomAssertion.assertTrue(totalObject.getTotal() >= 0, "Count is less than or equal to zero");
			CustomAssertion.assertTrue(totalObject.getCount() >= 0, "Count is less than or equal to zero");
		}

	}


	/**
	 * Test 9
	 * 
	 * Expose a specific file and check the exposure count is incremented for the file type
	 * 
	 * 
	 */
	@Test (dataProviderClass = BoxDataProvider.class, dataProvider = "fileTypesExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void exposeAFileTypeAndVerifyExposedFileTypes(String testname, String folderId, String filename, String access, String filetype, String server) throws Exception {

		LogUtils.logTestDescription("Expose a specific file and check the exposure count is incremented for the file type");

		//before exposure
		ExposureTotals exposedFileTypes = getExposedFileTypes("el_box", "true", "10");
		HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();

		for(TotalObject totalObject : exposedFileTypes.getObjects()) {
			beforeMap.put(totalObject.getId(), totalObject.getCount());
		}

		//Expose the file
		FileEntry sharedFile = this.uploadFileAndShareit(folderId, filename, access);

		sleep(CommonConstants.FIVE_MINUTES_SLEEP);

		//After the exposure get the count
		exposedFileTypes = getExposedFileTypes("el_box", "true", "10");
		HashMap<String, Integer> afterMap = new HashMap<String, Integer>();

		for(TotalObject totalObject : exposedFileTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getCount());
		}

		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			if(key.equals(filetype)) {
				int expectedValue = beforeMap.containsKey(key) ? beforeMap.get(key) + 1 : 0;
				CustomAssertion.assertEquals(afterMap.get(key), expectedValue, key + " count not added after expsoure");
			} else {
				CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key),    key + " count should not get incremented for no expsoure");
			}
		}

		//Disable shared link
		universalApi.disableSharedLink(sharedFile.getId());
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);

		//After the exposure get the count
		exposedFileTypes = getExposedFileTypes("el_box", "true", "10");
		afterMap = new HashMap<String, Integer>();

		for(TotalObject totalObject : exposedFileTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getCount());
		}

		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the count after removing exposure :"+key, true );
			CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key),    key + " count should be decremented after delete expsoure");
		}
	}



	/**
	 * Test 10
	 * 
	 * Get all internally exposed content type filter
	 * like legal, executable, health, image, business, video, design
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void getAllInternallyExposedContentTypes() throws Exception {
		LogUtils.logTestDescription("Get all internally exposed content types.");

		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", "true");

		//ExposureTotals expsoureTotals = 
		ExposureTotals exposedContentTypes =  getExposedContentTypes(elapp.el_box.name(), additionalParams);

		CustomAssertion.assertTrue(exposedContentTypes.getObjects().size() >= 0, "File type exposure is less than zero");

		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getId()!= null, "Id is null");
			CustomAssertion.assertTrue(totalObject.getTotal() >= 0, "Count is less than or equal to zero");
		}
	}



	/**
	 * Test 11
	 * 
	 * Expose a specific content type and check the exposure count is incremented for the content type
	 * 
	 * 
	 */
	@Test (dataProviderClass = BoxDataProvider.class, dataProvider = "contentTypesExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void exposeAFileTypeAndVerifyExposedContentTypes(String testname, String folderId, String filename, String access, String filetype, String server) throws Exception {

		LogUtils.logTestDescription("Expose a content type and check exposure count is incremented for the content type.");

		Reporter.log("Started "+testname, true);

		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", "true");

		//before exposure
		ExposureTotals exposedContentTypes = getExposedContentTypes("el_box", additionalParams);
		HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();

		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			beforeMap.put(totalObject.getId(), totalObject.getTotal());
		}

		//Expose the file
		FileEntry sharedFile = this.uploadFileAndShareit(folderId, filename, access);

		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//After the exposure get the count
		exposedContentTypes = getExposedContentTypes("el_box", additionalParams);
		HashMap<String, Integer> afterMap = new HashMap<String, Integer>();

		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}

		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			if(key.equals(filetype)) {
				CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key) +1, key + " count not added after expsoure");
			} else {
				int expectedValue = (beforeMap.get(key) == null) ? 0: beforeMap.get(key); 
				CustomAssertion.assertEquals(afterMap.get(key), expectedValue,    key + " count should not get incremented for no expsoure");
			}
		}

		//Disable shared link
		universalApi.disableSharedLink(sharedFile.getId());
		sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//After the exposure get the count
		exposedContentTypes = getExposedContentTypes("el_box", additionalParams);
		afterMap = new HashMap<String, Integer>();

		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}

		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key),   key + " count should be decremented after delete expsoure");
		}
	}

	/**
	 * Test 12
	 * 
	 * Check the vulnerability types and ciq profile filters
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyVulnerabilityTypes() throws Exception {

		LogUtils.logTestDescription("Get the vulnerability types and verify them");
		Logger.info( "Getting vulnerability types ...");

		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", "true");
		additionalParams.put("vl_types", 	"all");

		//After external exposure
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_box", additionalParams);

		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};

		for (String vl : vulnerabilities) {
			Reporter.log("Verifying the vulnerability type:"+vl, true);
			Reporter.log("Vulnerability:" + vl + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(vl), true );
			CustomAssertion.assertTrue(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) >= 0, "Count is less than zero");
		}

		for(CiqProfile ciqprofile : vulnerabilityTypes.getObjects().getCiqProfiles()) {
			Reporter.log("Verifying the CIQ profile name:"+ciqprofile.getId(), true);
			Reporter.log("Ciq Profile name:" +ciqprofile.getId() + ":: Count:"+ciqprofile.getTotal(), true);
			CustomAssertion.assertTrue(ciqprofile.getId()!=null, "CIQ profile id is null");
			CustomAssertion.assertTrue(ciqprofile.getTotal() >= 0, "CIQ profile total is null");
		}
	}



	/**
	 * Test 13
	 * Expose a file of particular vulnerability and check the exposure count 
	 * @param testname
	 * @param folderId
	 * @param filename
	 * @param access
	 * @param filetype
	 * @param server
	 * @throws Exception
	 */


	@Test (dataProviderClass = BoxDataProvider.class, dataProvider = "vulnerabilityTypesExposuresTotal", groups={"DASHBOARD"})
	public void exposeAFileTypeAndVerifyVulnerabilityTypes(String testname, String folderId, String filename, String access, String filetype, String server) throws Exception {

		LogUtils.logTestDescription("Expose a file of particular vulnerability and check the exposure count gets incremented");

		Reporter.log("Started "+testname, true);

		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", "true");

		//before exposure
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_box", additionalParams);
		HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();

		for(VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
			beforeMap.put(totalObject.getId(), totalObject.getTotal());
		}

		//Expose the file
		FileEntry sharedFile = this.uploadFileAndShareit(folderId, filename, access);

		sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//After the exposure get the count
		vulnerabilityTypes = getVulnerabilityTypes("el_box", additionalParams);
		HashMap<String, Integer> afterMap = new HashMap<String, Integer>();

		for(VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}

		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			if(key.equals(filetype)) {
				CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key) +1, key + " count not added after expsoure");
			} else {
				CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key),    key + " count should not get incremented for no expsoure");
			}
		}

		//Disable shared link
		universalApi.disableSharedLink(sharedFile.getId());
		sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//After the exposure get the count
		vulnerabilityTypes = getVulnerabilityTypes("el_box", additionalParams);
		afterMap = new HashMap<String, Integer>();

		for(VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}

		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key),   key + " count should be decremented after delete expsoure");
		}
	}



	/**
	 * Test 14
	 * 
	 * Check the vulnerability types and ciq profile filters
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyVulnerabilityTypesWithFilters() throws Exception {

		Logger.info( "Getting vulnerability types ...");

		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", "true");
		additionalParams.put("vl_types", 	"all");

		HashMap<String, Integer> vlmap = new HashMap<String, Integer>();

		//getVulnerabilityTypes
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_box", additionalParams);

		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};

		for (String vl : vulnerabilities) {
			vlmap.put(vl, vulnerabilityTypes.getObjects().getVulnerabilityCount(vl));
		}

		for(CiqProfile ciqprofile : vulnerabilityTypes.getObjects().getCiqProfiles()) {
			vlmap.put(ciqprofile.getId(), ciqprofile.getTotal());
		}

		//Filter it
		for (String vl : vulnerabilities) {
			additionalParams.clear();
			additionalParams.put("is_internal", "true");
			additionalParams.put("vl_types", 	vl);
			vulnerabilityTypes = getVulnerabilityTypes("el_box", additionalParams);
			CustomAssertion.assertEquals(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl), vlmap.get(vl),   vl + " count should be equal");
		}
	}

	/**
	 * Test 15
	 * 
	 * Check the vulnerability types and ciq profile filters
	 * Expose a document by a user and check the document count 
	 * 
	 */

	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "metricsExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_USERS", "FILTER"})
	public void verifyInternallyExposedUsers(String testname, String folderId, String fileName, String access, String exposureType, String server) throws Exception {

		FileEntry sharedFile = null;
		try {
			String steps[] = {
					"1. This test verify the metrics as depicted in venn diagram for users",
					"2. Upload a file and share it with " + access + " access.",
			"3. Verify the document count for an user get incremented after the exposure." };

			LogUtils.logTestDescription(steps);
			HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
			//docparams.add(new BasicNameValuePair("name",  sharedFile.getName()));

			SecurletDocument documents = getExposedUsers(elapp.el_box.name(), docparams);
			CustomAssertion.assertTrue(documents.getObjects().size() >= 0, "Exposed user documents should be greater than or equal to zero");

			for (com.elastica.beatle.securlets.dto.Object doc: documents.getObjects()) {
				beforeMap.put(doc.getEmail(), doc.getDocsExposed());
			}

			//As an internal user expose a document
			//Create a file and share it
			sharedFile = uploadFileAndShareit(folderId, fileName, access);

			//3 mins sleep
			sleep(CommonConstants.THREE_MINUTES_SLEEP);

			HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
			//After  exposure
			Reporter.log("Verifying Internal user exposed document count after the exposure", true);
			documents = getExposedUsers(elapp.el_box.name(), docparams);
			for (com.elastica.beatle.securlets.dto.Object doc: documents.getObjects()) {
				afterMap.put(doc.getEmail(), doc.getDocsExposed());
			}


			CustomAssertion.assertEquals(afterMap.get(suiteData.getSaasAppUsername()), beforeMap.get(suiteData.getSaasAppUsername()) +1,
					"Exposed file count for user doesn't match");


			//Delete the shared link and check the count. It should be decremented by one
			sharedFile = universalApi.disableSharedLink(sharedFile.getId());

			//3 mins sleep
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			//After  removing exposure
			Reporter.log("Verifying Internal user exposed document count after removing the exposure", true);
			documents = getExposedUsers(elapp.el_box.name(), docparams);
			for (com.elastica.beatle.securlets.dto.Object doc: documents.getObjects()) {
				afterMap.put(doc.getEmail(), doc.getDocsExposed());
			}

			CustomAssertion.assertEquals(afterMap.get(suiteData.getSaasAppUsername()), beforeMap.get(suiteData.getSaasAppUsername()),
					"Exposed file count for user doesn't match");

		}

		finally{
			universalApi.deleteFile(sharedFile.getId(), sharedFile.getEtag());
		}
	}


	/** 
	 * Test 16
	 * @param testname
	 * @param fileName
	 * @param access
	 * @param exposureType
	 * @param collaborators
	 * @param server
	 * @throws Exception
	 */
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "metricsFolderExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyFolderExposuresByInternalUsers(String testname, String fileName, String access, 
			String exposureType, String[] collaborators, String server) throws Exception {

		String steps[] = {
				"1. This test upload a folder and expose",
				"2. Upload a folder and file and share it with " + access + " access.",
				"3. Verify the document count for an user get incremented after the exposure." };

		LogUtils.logTestDescription(steps);

		BoxFolder sharedFolder = null;
		String uniqueFolderId= UUID.randomUUID().toString();

		Reporter.log("Started test "+testname, true);

		//create folder1
		BoxFolder folderObj = universalApi.createFolder(uniqueFolderId);
		String folderId = folderObj.getId();

		try {

			HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));


			SecurletDocument documents = getExposedUsers(elapp.el_box.name(), docparams);
			CustomAssertion.assertTrue(documents.getObjects().size() >= 0, "Exposed user documents should be greater than or equal to zero");

			for (com.elastica.beatle.securlets.dto.Object doc: documents.getObjects()) {
				beforeMap.put(doc.getEmail(), doc.getDocsExposed());
			}

			//Share the folder as specified in data providers
			Reporter.log("Exposing the folder with a file...", true);
			sharedFolder = shareTheFolderPubliclyOrWithCollaborators(folderId, fileName, access, collaborators, null);

			//3 mins sleep
			sleep(CommonConstants.FIVE_MINUTES_SLEEP);

			//After exposure
			HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
			//After  exposure
			Reporter.log("Verifying Internal user exposed document count after the exposure", true);
			documents = getExposedUsers(elapp.el_box.name(), docparams);
			for (com.elastica.beatle.securlets.dto.Object doc: documents.getObjects()) {
				afterMap.put(doc.getEmail(), doc.getDocsExposed());
			}


			CustomAssertion.assertEquals(afterMap.get(suiteData.getSaasAppUsername()), beforeMap.get(suiteData.getSaasAppUsername()) +2,
					"Exposed file/folder count for user doesn't match");

			//Delete the shared link and check the count. It should be decremented by one
			sharedFolder = universalApi.disableSharedLinkForFolder(sharedFolder.getId());

			Collaborations collaborations = universalApi.getFolderCollaborations(folderId);

			for (BoxCollaboration collaboration : collaborations.getEntries()) {
				universalApi.deleteCollaboration(collaboration);
			}

			sleep(CommonConstants.FIVE_MINUTES_SLEEP);

			//After  removing exposure
			Reporter.log("Verifying Internal user exposed document count after removing the exposure", true);
			documents = getExposedUsers(elapp.el_box.name(), docparams);
			for (com.elastica.beatle.securlets.dto.Object doc: documents.getObjects()) {
				afterMap.put(doc.getEmail(), doc.getDocsExposed());
			}

			CustomAssertion.assertEquals(afterMap.get(suiteData.getSaasAppUsername()), beforeMap.get(suiteData.getSaasAppUsername()),
					"Exposed file count for user doesn't match");

		}

		finally{

			//Delete the folder as well
			folderObj = universalApi.getFolderInfo(folderId);
			//universalApi.deleteFolder(folderId, true, folderObj.getEtag());
		}
	}

	/**
	 * Test 17
	 * @throws Exception
	 */

	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyUserTotalsAsInVennDiagram() throws Exception {
		LogUtils.logTestDescription("This test verify the user totals as in venn diagram ");
		Logger.info( "Getting user totals ...");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		//qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		LogUtils.logStep("1. Get the user totals for venn diagram metrics and check it is greater than zero.");
		ExposureTotals exposureTotals = getUserTotals(elapp.el_box.name(), qparams);

		Reporter.log("Verifying the internal user totals is greater than zero", true);
		for(TotalObject totalObject : exposureTotals.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getTotal() >=0 , totalObject.getId()  + " user total >= 0" , totalObject.getId()  + " user total < 0");
		}
		LogUtils.logStep("2. Get the internally exposed users count and verify against the user totals.");
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		SecurletDocument users = getExposedUsers(elapp.el_box.name(), docparams);
		System.out.println(MarshallingUtils.marshall(users));
		
		CustomAssertion.assertEquals(users.getMeta().getTotalCount(), exposureTotals.getInternalExposouresCount(), 
												"Exposed user count and exposed users list are not matching");
		
		LogUtils.logStep("3. Get the external users collaborated on internal docs and verify the against the external user totals.");
		
		docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		SecurletDocument extusers = getCollaborators(elapp.el_box.name(), docparams);
		CustomAssertion.assertEquals(extusers.getMeta().getTotalCount(), exposureTotals.getExternalExposouresCount(), 
														"Collaborator count and external user totals are not matching");
	}

	/**
	 * Test 17.1
	 * @throws Exception
	 */	
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyUserTotalsAfterExposure() throws Exception {

		BoxCollaboration  collabs = null; 

		try{

			LogUtils.logTestDescription("This test verify the user totals as in venn diagram and after exposure ");
			Logger.info( "Getting user totals ...");
			List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
			qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			//qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));

			LogUtils.logStep("1. Get the user totals for venn diagram metrics before exposure.");
			ExposureTotals userTotals = getUserTotals(elapp.el_box.name(), qparams);

			long userCountBefore = userTotals.getInternalExposouresCount();

			LogUtils.logStep("2. As an end user create a folder and collaborate with external user.");
			collabs =  createFolderAndCollaborateWithUserAsEndUser(externalUser, userRole);


			LogUtils.logStep("3. Verify whether internally exposed user count is incremented or not");
			userTotals = getUserTotals(elapp.el_box.name(), qparams);
			long userCountAfter = userTotals.getInternalExposouresCount();
			CustomAssertion.assertEquals(userCountAfter, userCountBefore+1, "Internally exposed user count not incremented after one more user exposed a file.");

			LogUtils.logStep("4. Check list of users has the end user as well");
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
			SecurletDocument users = getExposedUsers(elapp.el_box.name(), docparams);

			boolean found =false;

			int docsExposed = 0;
			for (com.elastica.beatle.securlets.dto.Object obj : users.getObjects()) {
				if (obj.getEmail().equals(suiteData.getSaasAppEndUser1Name())) {
					found = true;
					docsExposed = obj.getDocsExposed();

				}
			}

			CustomAssertion.assertTrue(found, "Internal user "+ suiteData.getSaasAppEndUser1Name() +" is present", "Internal user is not present");
			CustomAssertion.assertTrue(docsExposed==2, "Internal user exposed doc count is 2", "Internal user exposed doc count is 2");
			
			LogUtils.logStep("5. Delete the collaboration.");
			//delete the collaboration
			universalApiEndUser.deleteCollaboration(collabs);
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			LogUtils.logStep("6. Get the user totals for venn diagram metrics after exposure.");
			userTotals = getUserTotals(elapp.el_box.name(), qparams);

			LogUtils.logStep("7. Verify whether internally exposed user count is decremented or not");
			CustomAssertion.assertEquals(userTotals.getInternalExposouresCount(), userCountBefore, "Internally exposed user count not decremented after removing exposure.");
			
			
			LogUtils.logStep("8. Check the end user name also not present in the exposed users list");
			users = getExposedUsers(elapp.el_box.name(), docparams);

			found =true;

			for (com.elastica.beatle.securlets.dto.Object obj : users.getObjects()) {
				if (obj.getEmail().equals(suiteData.getSaasAppEndUser1Name())) {
					found = false;
				}
			}
			CustomAssertion.assertTrue(found, "Internal user is removed after removing the exposure", "Internal user is not removed after removing the exposure");
		}

		finally{
			
			//Not necessary but for the clean up in case of any exception
			universalApiEndUser.deleteCollaboration(collabs);
		}
	}
	
	

	/**
	 * Test 18
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyUserDocumentExposures() throws Exception {
		LogUtils.logTestDescription("This test verify the user documemt exposures");
		Logger.info( "Getting user totals ...");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));

		SecurletDocument documents = getUserExposures(elapp.el_box.name(), qparams);
		for (com.elastica.beatle.securlets.dto.Object object : documents.getObjects()) {
			CustomAssertion.assertTrue(object.getTotal() >=0, "User document exposure total can't be null");
			Reporter.log(String.valueOf(object.getTotal()), true);
		}
	}

	/**
	 * Test 19
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyUserVulnerabilities() throws Exception {
		LogUtils.logTestDescription("This test verify the user vulnerabilities");

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));

		SecurletDocument documents = getUserVulnerabilities(elapp.el_box.name(), qparams);
		for (com.elastica.beatle.securlets.dto.Object object : documents.getObjects()) {
			CustomAssertion.assertTrue(object.getTotal() >=0, "User vulnerability exposure total can't be null");
			Reporter.log(String.valueOf(object.getTotal()), true);
		}
	}



	/**
	 * Bulk remediation
	 * @param collaborator
	 * @param currentRole
	 * @throws Exception
	 */

	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void bulkRemediateExternalUser() throws Exception {

		String collabName = "pushpan@gmail.com";

		//Retrieve the documents count for the users
		LogUtils.logTestDescription("1. Remove the external collaborator access for all internally owned documents.");

		int beforeCount = 0;
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));

		//Get external collabs on internal documents
		Reporter.log("2. Get the count of documents for external collaborators before collaboration", true);
		SecurletDocument documents = getCollaborators(elapp.el_box.name(), qparams);

		for (com.elastica.beatle.securlets.dto.Object docobject : documents.getObjects()) {

			Reporter.log("Doc object Id:"+ docobject.getUserId(), true);
			Reporter.log("Doc object Total:"+ docobject.getDocsExposed(), true);
			if (docobject.getUserId().equals(collabName)) {
				beforeCount = docobject.getDocsExposed();
			}
		}

		// Create two folders and collaborate with the same user
		createFolderAndCollaborateWithUser(collabName, "editor");
		createFolderAndCollaborateWithUser(collabName, "co-owner");


		//Wait for three minutes for collaboration to happen
		// as we already wait in previous method, commented the following line
		//sleep(CommonConstants.TWO_MINUTES_SLEEP);

		Reporter.log("3. Get the count of documents for external collaborators after collaboration", true);
		int afterCount = 0;

		//Get external collabs on internal documents
		documents = getCollaborators(elapp.el_box.name(), qparams);
		for (com.elastica.beatle.securlets.dto.Object docobject : documents.getObjects()) {
			if (docobject.getUserId().equals(collabName)) {
				afterCount = docobject.getDocsExposed();
			}
		}

		//Validate the aftercount is incremented by 4 (two folders + two files)
		CustomAssertion.assertEquals(afterCount, beforeCount+4, "Exposure count not incremented by 4(two folders + two files)");

		ArrayList<String> collabs = new ArrayList<String>();
		collabs.add(collabName);	
		BoxRemediation boxRemediation = getBulkRemediationObject(collabs);
		Reporter.log(MarshallingUtils.marshall(boxRemediation), true);

		//Apply the bulk remediation
		Reporter.log("4. Apply the remediation", true);
		remediateExposureWithAPI(boxRemediation);

		//Wait for remedial action
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);

		//Get external collabs on internal documents
		documents = getCollaborators(elapp.el_box.name(), qparams);
		afterCount = 0;
		for (com.elastica.beatle.securlets.dto.Object docobject : documents.getObjects()) {
			if (docobject.getUserId().equals(collabName)) {
				afterCount = docobject.getDocsExposed();
			} 
		}
		Reporter.log("5. Get the count of documents for external collaborator after remediation", true);
		CustomAssertion.assertEquals(afterCount, 0, "Bulk remediation didnot happen it seems");
	}


	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "metricsFolderExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void SearchForExposedFiles(String testname, String fileName, String access, 
									String exposureType, String[] collaborators, String server) throws Exception {

		String steps[] = {
				"1. This test upload a folder and expose",
				"2. Upload a folder and file and share it with " + access + " access.",
				"3. Verify the document count for an user get incremented after the exposure." };

		LogUtils.logTestDescription(steps);

		BoxFolder sharedFolder = null;
		String uniqueFolderId= UUID.randomUUID().toString();

		Reporter.log("Started test "+testname, true);

		//create folder1
		BoxFolder folderObj = universalApi.createFolder(uniqueFolderId);
		String folderId = folderObj.getId();

		try {
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));

			//Share the folder as specified in data providers
			Reporter.log("Exposing the folder with a file...", true);
			sharedFolder = shareTheFolderPubliclyOrWithCollaborators(folderId, fileName, access, collaborators, uniqueFolderId);
			
			//3 mins sleep
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			LogUtils.logStep("1. Search with the complete folder id as text string.");
			UIExposedDoc payload = this.getUIPayload(facility.Box.name(), null, null, "docs", 0, 10, true, uniqueFolderId, "name");
			SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			CustomAssertion.assertTrue(exposedDocs.getDocs().size() == 2, "Search results are matching", "Search results are matching");
			
			for (com.elastica.beatle.securlets.dto.Object document : exposedDocs.getDocs()) {
				CustomAssertion.assertTrue(document.getName().contains(uniqueFolderId), "Search string is matching", "Search string is not matching") ;
			}
			
			LogUtils.logStep("2. Search with the partial folder id as text string.");
			String partialText = uniqueFolderId.substring(0,10);
			
			
			payload = this.getUIPayload(facility.Box.name(), null, null, "docs", 0, 10, true, partialText, "name");
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			CustomAssertion.assertEquals(exposedDocs.getMeta().getTotalCount(), 2, "Search results size don't match");
			
			
			for (com.elastica.beatle.securlets.dto.Object document : exposedDocs.getDocs()) {
				CustomAssertion.assertTrue(document.getName().contains(uniqueFolderId), "Search string is matching", "Search string is not matching") ;
			}
			
			//Search for collaborators
			if (collaborators.length > 0) {
				LogUtils.logStep("3. Search with the collaborator name as text string.");
				//Need to file a bug for email search
				String searchString = collaborators[0].split("@")[0];


				payload = this.getUIPayload(facility.Box.name(), null, null, "docs", 0, 10, true, searchString, "name");
				exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
				CustomAssertion.assertTrue(exposedDocs.getDocs().size() > 0, "Search results are matching", "Search results are matching");
				for (com.elastica.beatle.securlets.dto.Object document : exposedDocs.getDocs()) {
					String actualString = MarshallingUtils.marshall(document);
					Reporter.log("Output String:"+actualString, true);
					CustomAssertion.assertTrue(actualString.contains(searchString), searchString + " is matching", searchString + " is not in "+ actualString) ;
				}
			}
			
			
			//Delete the shared link and check the count. It should be decremented by one
			sharedFolder = universalApi.disableSharedLinkForFolder(sharedFolder.getId());

			Collaborations collaborations = universalApi.getFolderCollaborations(folderId);

			for (BoxCollaboration collaboration : collaborations.getEntries()) {
				universalApi.deleteCollaboration(collaboration);
			}
		}

		finally{

			//Delete the folder as well
			folderObj = universalApi.getFolderInfo(folderId);
			//universalApi.deleteFolder(folderId, true, folderObj.getEtag());
		}
	}

	@Test 
	public void verifyRiskyDocuments() throws Exception {

		Logger.info( "Getting risky documents ...");

		//After external exposure
		getRiskyDocuments(true, 20);

		//		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};
		//
		//		for (String vl : vulnerabilities) {
		//			Logger.info(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) );
		//		}
	}




	@Test
	public void VerifyInternallyExposedFileTypes() throws Exception {
		Logger.info( "Getting file types ...");

		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("top", "10");

		//After external exposure
		ExposureTotals exposureTotals = getFileTypes(elapp.el_box.name(), hmap);

		HashMap<String, Integer> beforeCount = new HashMap<String, Integer>();

		for (TotalObject totalObj : exposureTotals.getObjects()) {
			beforeCount.put(totalObj.getId(), totalObj.getCount());
		}

		//upload a 

	}

	/**
	 * This is the utility method to remediate the exposure thro' api. 
	 * @param tenant
	 * @param facility
	 * @param user
	 * @param documentId
	 * @param userId
	 * @param action
	 * @throws Exception
	 */
	public void remediateExposureWithAPI(BoxRemediation remediationObject) throws Exception {

		List<NameValuePair> headers = getHeaders();

		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = "{\"objects\":[" + MarshallingUtils.marshall(remediationObject) + "]}";

		Reporter.log("Request body:" + payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getBoxRemediation")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
		HttpResponse response =  restClient.doPatch(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("Response code:"+ response.getStatusLine().getReasonPhrase(), true);
	}	


	private BoxRemediation getBulkRemediationObject(ArrayList<String> collaboratorName) {
		BoxRemediation boxRemediation = new BoxRemediation();

		boxRemediation.setDbName(suiteData.getTenantName());
		boxRemediation.setUser("__ALL_EL__");
		boxRemediation.setUserId("__ALL_EL__");
		boxRemediation.setDocType("__ALL_EL__");
		boxRemediation.setDocId("__ALL_EL__");
		boxRemediation.setInstance("");;

		//Meta Info
		BoxMetaInfo boxMetaInfo = new BoxMetaInfo();
		boxMetaInfo.setCurrentLink(null);
		boxMetaInfo.setCollabs(collaboratorName);
		boxMetaInfo.setExpireOn(null);
		boxMetaInfo.setAccess(null);

		List<BoxAction> actions = new ArrayList<BoxAction>();
		BoxAction boxActionForSharing = new BoxAction();
		boxActionForSharing.setCode("COLLAB_REMOVE");
		boxActionForSharing.setPossibleValues(new ArrayList<String>());
		boxActionForSharing.setMetaInfo(boxMetaInfo);
		boxActionForSharing.setCodeName("Remove");
		actions.add(boxActionForSharing);

		boxRemediation.setActions(actions);
		return boxRemediation;
	}	


	public void createFolderAndCollaborateWithUser(String collaborator, String currentRole) throws Exception {

		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		//create folder1
		BoxFolder folderObj = universalApi.createFolder(uniqueId);
		String folderId = folderObj.getId();

		//Perform folder operations
		//upload the file to folder1
		FileUploadResponse uploadResponse = universalApi.uploadFile(folderId, sourceFile, destinationFile);
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
		BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);


		Reporter.log("Waiting for the collaboration action ...going to sleep "+CommonConstants.THREE_MINUTES_SLEEP + " ms", true);
		//Sleep time of atleast 3 mins is needed as our portal has to get the collaboration event. otherwise test will fail
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//get the collaborations and assert them
		Collaborations collaborations = universalApi.getFolderCollaborations(folderId);
	}
	
	
	public BoxCollaboration createFolderAndCollaborateWithUserAsEndUser(String collaborator, String currentRole) throws Exception {

		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		//create folder1
		BoxFolder folderObj = universalApiEndUser.createFolder(uniqueId);
		String folderId = folderObj.getId();

		//Perform folder operations
		//upload the file to folder1
		FileUploadResponse uploadResponse = universalApiEndUser.uploadFile(folderId, sourceFile, destinationFile);
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
		BoxCollaboration collaboration = universalApiEndUser.createCollaboration(collabInput);


		Reporter.log("Waiting for the collaboration action ...going to sleep "+CommonConstants.THREE_MINUTES_SLEEP + " ms", true);
		//Sleep time of atleast 3 mins is needed as our portal has to get the collaboration event. otherwise test will fail
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//get the collaborations and assert them
		Collaborations collaborations = universalApiEndUser.getFolderCollaborations(folderId);
		return collaboration;
	}


}