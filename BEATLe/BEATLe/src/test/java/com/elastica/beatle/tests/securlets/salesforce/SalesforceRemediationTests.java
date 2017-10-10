package com.elastica.beatle.tests.securlets.salesforce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.SecurletRemediation;
import com.elastica.beatle.securlets.dto.UIRemediationInnerObject;
import com.elastica.beatle.securlets.dto.UIRemediationObject;
import com.elastica.beatle.securlets.dto.UIRemediationSource;
import com.elastica.beatle.tests.securlets.CustomAssertion;
import com.universal.common.Salesforce;
import com.universal.common.UniversalApi;
import com.universal.constants.CommonConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.salesforce.ChatterFile;
import com.universal.dtos.salesforce.FileShareLink;
import com.universal.dtos.salesforce.FileShares;
import com.universal.dtos.salesforce.FileSharesInput;
import com.universal.dtos.salesforce.InternalFileShare;
import com.universal.dtos.salesforce.Share;
import com.universal.util.OAuth20Token;

public class SalesforceRemediationTests extends SecurletUtils{

	//Salesforce sfapi;
	SalesforceActivityLog sfActivityLog;
	HashMap<String, SalesforceActivity> sfActivities = new HashMap<String, SalesforceActivity>();
	LogValidator logValidator;
	protected ForensicSearchResults chatterLogs, sobjectLogs, contentInspectionLogs, leadLogs, 
						accountLogs, opportunityLogs, caseLogs, contactLogs;
	long maxWaitTime = 20;
	long minWaitTime = 10;
	long sleepTime   = CommonConstants.THREE_MINUTES_SLEEP;
	
	
	String saasAppUsername;
	String saasAppPassword;
	String saasAppUserRole;
	String createdTime;
	String destinationFile;
	String saasAppUser;
	String instanceUrl;
	String instanceId;
	
	String internalUserId1;
	String externalUserId1;
	String externalUserId2;

	public SalesforceRemediationTests() throws Exception {
		sfActivityLog = new SalesforceActivityLog();
		logValidator = new LogValidator(); 
	}

	@BeforeClass(alwaysRun=true)
	public void initSalesforce() throws Exception {
		this.saasAppUsername 	= getRegressionSpecificSuitParameters("saasAppUsername");
		this.saasAppPassword 	= getRegressionSpecificSuitParameters("saasAppPassword");
		this.saasAppUserRole 	= getRegressionSpecificSuitParameters("saasAppUserRole");
		this.internalUserId1	= getRegressionSpecificSuitParameters("saasAppEndUser1Name");
		this.externalUserId1	= getRegressionSpecificSuitParameters("saasAppExternalUser1Name");
		this.externalUserId2	= getRegressionSpecificSuitParameters("saasAppExternalUser2Name");
		
		if(saasAppUsername.toLowerCase().contains(".sandbox")) {
			saasAppUser = StringUtils.chop(saasAppUsername.toLowerCase()).replace(".sandbox", "");
		} else {
			saasAppUser = saasAppUsername;
		}
		
		
		OAuth20Token tokenObj = sfapi.getTokenObject();
		
		//Reporter.log("Token Obj:"+MarshallingUtils.marshall(tokenObj), true);
		
		//https://test.salesforce.com/id/00D170000008i3DEAQ/005o0000000RTtEAAW  //4th param is the id
		instanceUrl = tokenObj.getInstanceUrl();
		Reporter.log("Token Id:" + tokenObj.getId(), true);
		instanceId = tokenObj.getId().split("/")[4];
		if (suiteData.getEnvironmentName().toLowerCase().contains("cep") || suiteData.getEnvironmentName().toLowerCase().contains("prod")) {
			this.sleepTime = CommonConstants.THREE_MINUTES_SLEEP;
		}
		
	}
	
	
	/**
	 * Test 1
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "SALESFORCE", "REGRESSION", "P1"})
	public void exposeFilepubliclyAndRemediateWithUnshare() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly.",
					"3. With remediation API, unshare the public link.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
		
			LogUtils.logStep("3. Checking the share link before remediation..");
			FileShareLink fsl = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl), true);
			
			sleep(sleepTime);
			
			//check the document exists in db or not
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE"};
			String[] metaInfo={"open"};
			
			LogUtils.logStep("4. File found in DB. so started applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	private boolean checkDocumentInDB(String name, String fileId) throws Exception {
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
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "SALESFORCE", "REGRESSION", "P1"})
	public void exposeFilepubliclyAndRemediateWithAnyoneInCompanyCanView() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly.",
					"3. With remediation API, add viewer access to the company.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
		
			LogUtils.logStep("3. Checking the share link before remediation..");
			FileShareLink fsl = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl), true);
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"SHARE_ACCESS"};
			String[] metaInfo={"company-V"};
			
			LogUtils.logStep("4. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "unicode", remedialAction, metaInfo);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) != null, "File is not unshared", "File is unshared");
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with view permission for the organization
				if (share.getSharingType().equals("V")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File is shared with view permission inside company", "File is not shared with view permission inside company");
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 3
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "SALESFORCE", "REGRESSION", "P1"})
	public void exposeFilepubliclyAndRemediateWithAnyoneInCompanyCanCollaborate() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly.",
					"3. With remediation API, add collaborator access to the company.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
		
			LogUtils.logStep("3. Checking the share link before remediation..");
			FileShareLink fsl = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl), true);
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"SHARE_ACCESS"};
			String[] metaInfo={"company-C"};
			
			LogUtils.logStep("4. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "unicode", remedialAction, metaInfo);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) != null, "File is not unshared", "File is unshared");
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with Collaborate permission for the organization
				if (share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File is shared with collaborate permission inside company", "File is not shared with collaborate permission inside company");
			LogUtils.logStep("5. Remediation completed successfully");
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 4
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "SALESFORCE", "REGRESSION"})
	public void exposeFilepubliclyAndRemediateWithAllViewUnshare() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly and internally with viewer permission.",
					"3. With remediation API, Unshare the public link and company access.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly and internally");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
			
			LogUtils.logStep("3. Share the file internally with View Permission as well.");
			InternalFileShare ifs = new InternalFileShare("V", fileId, this.instanceId);
			sfapi.shareFileInternally(ifs);
			
			LogUtils.logStep("4. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE", "UNSHARE"};
			String[] metaInfo={"open", "company"};
			
			LogUtils.logStep("5. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			boolean shared = true;
			for (Share share : fileShares.getShares()) {
				//check file is shared with Collaborate permission for the organization
				if (share.getSharingType().equals("V")) {
					shared = false;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File share(V) is remediated", "File share(V) is not remediated");
			LogUtils.logStep("6. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 5
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "SALESFORCE", "REGRESSION"})
	public void exposeFilepubliclyAndRemediateWithAllCollaborateUnshare() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly and internally with collaborator permission.",
					"3. With remediation API, Unshare the public link and company access.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly and internally");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
			
			LogUtils.logStep("3. Share the file internally with Collaborate Permission as well.");
			InternalFileShare ifs = new InternalFileShare("C", fileId, this.instanceId);
			sfapi.shareFileInternally(ifs);
			
			LogUtils.logStep("4. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE", "UNSHARE"};
			String[] metaInfo={"open", "company"};
			
			LogUtils.logStep("5. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			boolean shared = true;
			for (Share share : fileShares.getShares()) {
				//check file is shared with Collaborate permission for the organization
				if (share.getSharingType().equals("C")) {
					shared = false;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File share(C) is remediated", "File share(C) is not remediated");
			LogUtils.logStep("6. Remediation completed successfully");
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 6
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "SALESFORCE", "REGRESSION"})
	public void exposeFilepubliclyAndInternallyViewRemediateWithPublicLinkUnshare() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly and internally with viewer permission.",
					"3. With remediation API, Unshare the public link.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly and internally");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
			
			LogUtils.logStep("3. Share the file internally with view Permission as well.");
			InternalFileShare ifs = new InternalFileShare("V", fileId, this.instanceId);
			sfapi.shareFileInternally(ifs);
			
			LogUtils.logStep("4. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE"};
			String[] metaInfo={"open"};
			
			LogUtils.logStep("5. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with Collaborate permission for the organization
				if (share.getSharingType().equals("V")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File share(V) is retained", "File share(V) is not retained");
			LogUtils.logStep("6. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 7
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "SALESFORCE", "REGRESSION"})
	public void exposeFilepubliclyAndInternallyCollaborateRemediateWithPublicLinkUnshare() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly and internally with Collaborator permission.",
					"3. With remediation API, Unshare the public link.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly and internally");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
			
			LogUtils.logStep("3. Share the file internally with Collaborate Permission as well.");
			InternalFileShare ifs = new InternalFileShare("C", fileId, this.instanceId);
			sfapi.shareFileInternally(ifs);
			
			LogUtils.logStep("4. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE"};
			String[] metaInfo={"open"};
			
			LogUtils.logStep("5. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with Collaborate permission for the organization
				if (share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File share(C) is retained", "File share(C) is not retained");
			LogUtils.logStep("6. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 8
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "SALESFORCE", "REGRESSION"})
	public void exposeFilepubliclyAndInternalViewRemediateWithUnshareInternalCollaborate() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly and internally with viewer permission.",
					"3. With remediation API, Unshare the public link and change internal access to Collaborator.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly and internally");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
			
			LogUtils.logStep("3. Share the file internally with Collaborate Permission as well.");
			InternalFileShare ifs = new InternalFileShare("V", fileId, this.instanceId);
			sfapi.shareFileInternally(ifs);
			
			LogUtils.logStep("4. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE", "SHARE_ACCESS"};
			String[] metaInfo={"open", "company-C"};
			
			LogUtils.logStep("5. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with Collaborate permission for the organization
				if (share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File share(V) is remediated", "File share(V) is not remediated");
			LogUtils.logStep("6. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 9
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "SALESFORCE", "REGRESSION"})
	public void exposeFilepubliclyAndInternalCollabRemediateWithUnshareInternalView() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly and internally with Collaborator permission.",
					"3. With remediation API, Unshare the public link and change internal access to viewer.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly and internally");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
			
			LogUtils.logStep("3. Share the file internally with Collaborate Permission as well.");
			InternalFileShare ifs = new InternalFileShare("C", fileId, this.instanceId);
			sfapi.shareFileInternally(ifs);
			
			LogUtils.logStep("4. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE", "SHARE_ACCESS"};
			String[] metaInfo={"open", "company-V"};
			
			LogUtils.logStep("5. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with Collaborate permission for the organization
				if (share.getSharingType().equals("V")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File share(C) is remediated", "File share(C) is not remediated");
			LogUtils.logStep("6. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 10
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithExternalCollaboratorViewAndRemediateWithRemoveCollaborator() throws Exception {
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file to one external user with (View) permission.",
					"3. With remediation API, remove the collaborator.",
					"4. Verify whether remediation applied successfully or not."  };
		
		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file externally with View Permission as well.");
			
			
			//Get the user id for fileshare
			String userId = sfapi.getExternalUserId(externalUserId1, "Securletuser", "SUser", "ExtUser11");
			String ids[] = {userId};
			String sharingTypes[] = {"V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with view permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("3. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId) && share.getSharingType().equals("V")) {
					shared = true;
				}
			}
			CustomAssertion.assertTrue(shared, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"COLLAB_REMOVE"};
			String[] collaborators={externalUserId1};
			
			LogUtils.logStep("4. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, null, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId) && share.getSharingType().equals("V")) {
					shared = true;
				}
			}
			CustomAssertion.assertTrue(!shared, "File collaboarator is removed", "File collaboarator is not removed");
			
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 11
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithExternalCollaboratorEditAndRemediateWithRemoveCollaborator() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file to one external user with (Collaborator) permission.",
					"3. With remediation API, remove the collaborator.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file externally with Edit Permission as well.");
			
			
			//Get the user id for fileshare
			String userId = sfapi.getExternalUserId(externalUserId1, "Securletuser", "SUser", "ExtUser11");
			String ids[] = {userId};
			String sharingTypes[] = {"C"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("3. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId) && share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			CustomAssertion.assertTrue(shared, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"COLLAB_REMOVE"};
			String[] collaborators={externalUserId1};
			
			LogUtils.logStep("4. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, null, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId) && share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			CustomAssertion.assertTrue(!shared, "File collaboarator is removed", "File collaboarator is not removed");
			
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 12
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithMultipleExternalCollaboratorsEditAndRemediateWithRemoveCollaborator() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file to two external users of which user1(Collaborator) and user2(Viewer) permission.",
					"3. With remediation API, remove all collaborators.",
					"4. Verify whether remediation applied successfully or not."  };
		
		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file externally with Edit Permission as well.");
			
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "ExtUser11");
			String userId2 = sfapi.getExternalUserId(externalUserId2, "Securletuser2", "SUser2", "ExtUser2");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("3. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"COLLAB_REMOVE", "COLLAB_REMOVE"};
			String[] collaborators={externalUserId1, externalUserId2};
			
			LogUtils.logStep("4. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, null, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(!shared1, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 13
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithMultipleExternalCollaboratorsEditAndRemediateWithUpdateCollaborator() throws Exception {
		
		String steps[] = 
			{		"1. Upload a file.", 
					"2. Share the file to two external users of which user1(Collaborator) and user2(Viewer) permission.",
					"3. With remediation API, change user1 access as viewer, "
					 + "user2 access as Collaborator.",
					"4. Verify whether remediation applied successfully or not."  };
		
		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file externally with Edit Permission as well.");
			
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "ExtUser11");
			String userId2 = sfapi.getExternalUserId(externalUserId2, "Securletuser2", "SUser2", "Securlets");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("3. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"COLLAB_UPDATE", "COLLAB_UPDATE"};
			String[] collaborators={externalUserId1, externalUserId2};
			String[] metaInfo={"V", "C"};
			
			LogUtils.logStep("4. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File collaboarator is updated", "File collaboarator is not updated");
			CustomAssertion.assertTrue(shared2, "File collaboarator is updated", "File collaboarator is not updated");
			
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 14
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithOneExternalInternalCollaboratorEditAndRemediateWithUpdateCollaborator() throws Exception {
		
		String steps[] = 
			{		"1. Upload a file.", 
					"2. Share the file to an external user(Collaborator) and internal user(Collaborator) permission.",
					"3. With remediation API, change external user as viewer, "
					 + "internal user as viewer.",
					"4. Verify whether remediation applied successfully or not."  };
		
		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file externally with Edit Permission as well.");
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "SecurletCom1");
			String userId2 = sfapi.getUserId(internalUserId1, "Securletuser2", "SUser2", "SecBeatle1");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "C"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("3. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the internal collaborator", "File is not shared with Edit permission to the internal collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"COLLAB_UPDATE", "COLLAB_UPDATE"};
			String[] collaborators={externalUserId1, internalUserId1};
			String[] metaInfo={"V", "V"};
			
			LogUtils.logStep("4. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File collaboarator is updated", "File collaboarator is not updated");
			CustomAssertion.assertTrue(shared2, "File collaboarator is updated", "File collaboarator is not updated");
			
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 15
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithOneExternalInternalCollaboratorEditAndRemediateWithUpdateRemoveCollaborator() throws Exception {
		
		String steps[] = 
			{		"1. Upload a file.", 
					"2. Share the file to an external user(Collaborator) and internal user(Viewer) permission.",
					"3. With remediation API, change external user as viewer, "
					 + "internal user as collaborator.",
					"4. Verify whether remediation applied successfully or not."  };
		
		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file externally with Edit Permission as well.");
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "ExtUser11");
			String userId2 = sfapi.getUserId(internalUserId1, "Securletuser2", "SUser2", "SecBeatle1");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("3. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"COLLAB_UPDATE", "COLLAB_REMOVE"};
			String[] collaborators={externalUserId1, internalUserId1};
			String[] metaInfo={"V", "C"}; //Just passing two args.. no use
			
			LogUtils.logStep("4. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File collaboarator is updated", "File collaboarator is not updated");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

	/**
	 * Test 16
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithOneExternalInternalCollaboratorAndPubliclyEditAndRemediateWithUpdateRemoveCollaborator() throws Exception {
		
		String steps[] = 
			{		"1. Upload a file.", 
					"2. Share the publicly.",
					"3. Share the file to an external user(Collaborator) and internal user(Viewer) permission.",
					"4. With remediation API, unshare the public access, external user as viewer, "
					 + "internal user as collaborator.",
					"5. Verify whether remediation applied successfully or not."  };
		
		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);

			LogUtils.logStep("3. Checking the share link before remediation..");
			FileShareLink fsl1 = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl1), true);
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			LogUtils.logStep("4. Share the file externally with Edit Permission as well.");
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "ExtUser11");
			String userId2 = sfapi.getUserId(internalUserId1, "Securletuser2", "SUser2", "SecBeatle1");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("5. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE", "COLLAB_UPDATE", "COLLAB_REMOVE"};
			String[] collaborators={"",externalUserId1, internalUserId1};
			String[] metaInfo={"open", "V", "C"}; //Just passing two args.. no use
			
			LogUtils.logStep("6. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File collaboarator is updated", "File collaboarator is not updated");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			LogUtils.logStep("7. Remediation completed successfully");

			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Test 17
	 * @throws Exception
	 * 
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithOneExternalInternalCollaboratorAndPubliclyEditAndRemediateWithUpdateRemoveShareCollaborator() throws Exception {
		
		String steps[] = 
			{		"1. Upload a file.", 
					"2. Share the publicly.",
					"3. Share the file to an external user(Collaborator) and internal user(Viewer) permission.",
					"4. With remediation API, add viewer access to company, external user as viewer, "
					 + "internal user as collaborator.",
					"5. Verify whether remediation applied successfully or not."  };
		
		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);

			LogUtils.logStep("3. Checking the share link before remediation..");
			FileShareLink fsl1 = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl1), true);
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			LogUtils.logStep("4. Share the file externally with Edit Permission as well.");
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "Securlets");
			String userId2 = sfapi.getUserId(internalUserId1, "Securletuser2", "SUser2", "SecBeatle1");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("5. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for remediating the file
			
			String[] remedialAction={"SHARE_ACCESS", "COLLAB_UPDATE", "COLLAB_REMOVE"};
			String[] collaborators={"",externalUserId1, internalUserId1};
			String[] metaInfo={"company-V", "V", "C"}; //Just passing two args.. no use
			
			LogUtils.logStep("6. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with view permission for the organization
				if (share.getSharingType().equals("V")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File is shared with view permission inside company", "File is not shared with view permission inside company");
			
			CustomAssertion.assertTrue(shared1, "File collaboarator is updated", "File collaboarator is not updated");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) != null, "File is not unshared", "File is unshared");
			
			LogUtils.logStep("7. Remediation completed successfully");
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 18
	 * @throws Exception
	 * Share publicly
	 * Share to external and internal collaborator
	 * Share access with edit and collab update
	 * 
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithOneExternalInternalCollaboratorAndPubliclyEditAndRemediateWithUpdateRemoveCollaboratorShareAccess() throws Exception {
		
		String steps[] = 
				{	"1. Upload a file.", 
					"2. Share the publicly.",
					"3. Share the file to an external user(Collaborator) and internal user(Viewer) permission.",
					"4. With remediation API, add Collaborator access to company, external user as viewer, "
					 + "internal user as collaborator.",
					"5. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);

			LogUtils.logStep("3. Checking the share link before remediation..");
			FileShareLink fsl1 = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl1), true);
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			LogUtils.logStep("4. Share the file externally with Edit Permission as well.");
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "Securlets");
			String userId2 = sfapi.getUserId(internalUserId1, "Securletuser2", "SUser2", "Securlets");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("5. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for remediating the file
			
			String[] remedialAction={"SHARE_ACCESS", "COLLAB_UPDATE", "COLLAB_REMOVE"};
			String[] collaborators={"",externalUserId1, internalUserId1};
			String[] metaInfo={"company-C", "V", "C"}; //Just passing two args.. no use
			
			LogUtils.logStep("6. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with view permission for the organization
				if (share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File is shared with view permission inside company", "File is not shared with view permission inside company");
			
			CustomAssertion.assertTrue(shared1, "File collaboarator is updated", "File collaboarator is not updated");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) != null, "File is not unshared", "File is unshared");
			
			LogUtils.logStep("7. Remediation completed successfully");
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 19
	 * 
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithOneExternalInternalCollaboratorAndPubliclyEditAndRemediateWithUpdateRemoveCollaboratorUnshareAccess() throws Exception {
		
		String steps[] =
			{	"1. Upload a file.", 
				"2. Share the publicly.",
				"4. Share the file to an external user(Collaborator) and internal user(Viewer) permission.",
				"5. With remediation API, unshare the public link., add Collaborator access to company, external user as viewer, "
				 + "internal user as collaborator.",
				"6. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);

			LogUtils.logStep("3. Checking the share link before remediation..");
			FileShareLink fsl1 = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl1), true);
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			LogUtils.logStep("4. Share the file externally with Edit Permission as well.");
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "ExtUser11");
			String userId2 = sfapi.getUserId(internalUserId1, "Securletuser2", "SUser2", "SecBeatle1");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("5. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for remediating the file
			String[] remedialAction={"UNSHARE", "SHARE_ACCESS", "COLLAB_UPDATE", "COLLAB_REMOVE"};
			String[] collaborators={"", "",externalUserId1, internalUserId1};
			String[] metaInfo={"open", "company-C", "V", "C"}; //Just passing two args.. no use
			
			LogUtils.logStep("6. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with view permission for the organization
				if (share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File is shared with view permission inside company", "File is not shared with view permission inside company");
			
			CustomAssertion.assertTrue(shared1, "File collaboarator is updated", "File collaboarator is not updated");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is not unshared", "File is unshared");
			
			LogUtils.logStep("7. Remediation completed successfully");
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 20
	 * 
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithAllCombinationsAndRemediate() throws Exception {
		
		String steps[] = 
		{	"1. Upload a file.", 
			"2. Share the publicly.",
			"3. Share the file to all company with viewer permission.",
			"4. Share the file to an external user(Collaborator) and internal user(Viewer) permission.",
			"5. With remediation API, unshare the public link., remove the company access, remove internal/external collaborator.",
			"6. Verify whether remediation applied successfully or not."};
		

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);

			LogUtils.logStep("3. Share the file internally with View Permission as well.");
			InternalFileShare ifs = new InternalFileShare("V", fileId, this.instanceId);
			sfapi.shareFileInternally(ifs);
			
			LogUtils.logStep("4. Checking the share link before remediation..");
			FileShareLink fsl1 = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl1), true);
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			LogUtils.logStep("5. Share the file externally with Edit Permission as well.");
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "ExtUser11");
			String userId2 = sfapi.getUserId(internalUserId1, "Securletuser2", "SUser2", "SecBeatle1");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("6. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for remediating the file
			
			String[] remedialAction={"UNSHARE", "UNSHARE", "COLLAB_REMOVE", "COLLAB_REMOVE"};
			String[] collaborators={"", "", externalUserId1, internalUserId1};
			String[] metaInfo={"open", "company", "V", "C"}; //Just passing two args.. no use
			
			LogUtils.logStep("7. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with view permission for the organization
				if (share.getSharingType().equals("V") || share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			
			CustomAssertion.assertTrue(!shared, "File is shared inside company", "File is not shared inside company");
			CustomAssertion.assertTrue(!shared1, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			LogUtils.logStep("8. Remediation completed successfully");
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test 21
	 * 
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION"})
	public void exposeFileWithAllCombinationsAndRemediateWithCombinations() throws Exception {
		
		String steps[] = 
		{	"1. Upload a file.", 
			"2. Share the publicly.",
			"3. Share the file to all company with collaborate permission.",
			"4. Share the file to an external and internal user with viewer permission.",
			"5. With remediation API, unshare the public link., change company access to viewer permission, External user as viewer and remove internal collaborator.",
			"6. Verify whether remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);

			LogUtils.logStep("3. Share the file internally with Collaborate Permission as well.");
			InternalFileShare ifs = new InternalFileShare("C", fileId, this.instanceId);
			sfapi.shareFileInternally(ifs);
			
			LogUtils.logStep("4. Checking the share link before remediation..");
			FileShareLink fsl1 = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl1), true);
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			LogUtils.logStep("5. Share the file externally with Edit Permission as well.");
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "ExtUser11");
			String userId2 = sfapi.getUserId(internalUserId1, "Securletuser2", "SUser2", "SecBeatle1");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"V", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with view permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("6. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for remediating the file
			
			String[] remedialAction={"UNSHARE", "SHARE_ACCESS", "COLLAB_UPDATE", "COLLAB_REMOVE"};
			String[] collaborators={"", "", externalUserId1, internalUserId1};
			String[] metaInfo={"open", "company-V", "C", "C"}; //Just passing two args.. no use
			
			LogUtils.logStep("7. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			
			boolean shared = false;
			for (Share share : fileShares.getShares()) {
				//check file is shared with view permission for the organization
				if (share.getSharingType().equals("V")) {
					shared = true;
				}
			}
			
			LogUtils.logStep("8. Verifying the remediation..");
			
			CustomAssertion.assertTrue(shared, "File is shared inside company", "File is not shared inside company");
			CustomAssertion.assertTrue(shared1, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			LogUtils.logStep("9. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 22
	 * @throws Exception
	 * 
	 */
	@Test(groups={"UIREMEDIATION", "SALESFORCE", "REGRESSION"})
	public void remediateWithUIServerPublicExposureWithUnshare() throws Exception {
		
		String steps[] = 
		{	"1. This test uploads the file and expose it publicly.", 
			"2. With UI remediation API, applying the remediation as UNSHARE.",
			"3. After UI server remediation call, get the file sharelink from salesforce and check if it is unshared or not."
		};

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
		
			LogUtils.logStep("3. Checking the share link before remediation..");
			FileShareLink fsl = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE"};
			String[] metaInfo={"open"};
			
			LogUtils.logStep("4. Applying the UI remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo);
			
			//Prepare the UI remediation object
			UIRemediationObject UIRemedObject = new UIRemediationObject();
			UIRemediationSource UISource = new UIRemediationSource();
			UIRemediationInnerObject UIInnerObject = new UIRemediationInnerObject();
			ArrayList<SecurletRemediation> remedList = new ArrayList<SecurletRemediation>();
			remedList.add(sfremediation);
			UIInnerObject.setObjects(remedList);
			UISource.setObjects(UIInnerObject);
			UISource.setApp(facility.Salesforce.name());
			UIRemedObject.setSource(UISource);
			Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
			
			//Perform the UI remediation
			remediateExposureWithUIServer(UIRemedObject);
			
			sleep(CommonConstants.TWO_MINUTES_SLEEP);
			
			LogUtils.logStep("5. Verifying UI remediation applied or not.");
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			LogUtils.logStep("6. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 23
	 * @throws Exception
	 */
	@Test(groups={"UIREMEDIATION", "SALESFORCE", "REGRESSION", "P1"})
	public void exposeFilepubliclyAndUIRemediateWithAllViewUnshare() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file publicly and internally with viewer permission.",
					"3. With UI remediation API, Unshare the public link and company access.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter and expose publicly and internally");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);
			
			LogUtils.logStep("3. Share the file internally with View Permission as well.");
			InternalFileShare ifs = new InternalFileShare("V", fileId, this.instanceId);
			sfapi.shareFileInternally(ifs);
			
			LogUtils.logStep("4. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE", "UNSHARE"};
			String[] metaInfo={"open", "company"};
			
			LogUtils.logStep("5. Applying the UI remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo);
			
			
			//Prepare the UI remediation object
			UIRemediationObject UIRemedObject = new UIRemediationObject();
			UIRemediationSource UISource = new UIRemediationSource();
			UIRemediationInnerObject UIInnerObject = new UIRemediationInnerObject();
			ArrayList<SecurletRemediation> remedList = new ArrayList<SecurletRemediation>();
			remedList.add(sfremediation);
			UIInnerObject.setObjects(remedList);
			UISource.setObjects(UIInnerObject);
			UISource.setApp(facility.Salesforce.name());
			UIRemedObject.setSource(UISource);
			//Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
			
			//Perform the UI remediation
			remediateExposureWithUIServer(UIRemedObject);
			
			sleep(sleepTime);
			
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			boolean shared = true;
			for (Share share : fileShares.getShares()) {
				//check file is shared with Collaborate permission for the organization
				if (share.getSharingType().equals("V")) {
					shared = false;
				}
			}
			
			CustomAssertion.assertTrue(shared, "File share(V) is remediated", "File share(V) is not remediated");
			LogUtils.logStep("6. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 24
	 * @throws Exception
	 */
	@Test(groups={"UIREMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION", "P1"})
	public void exposeFileWithOneExternalInternalCollaboratorAndPubliclyEditAndUIRemediateWithUpdateRemoveCollaborator() throws Exception {
		
		String steps[] = 
			{		"1. Upload a file.", 
					"2. Share the publicly.",
					"3. Share the file to an external user(Collaborator) and internal user(Viewer) permission.",
					"4. With remediation API, unshare the public access, external user as viewer, "
					 + "internal user as collaborator.",
					"5. Verify whether remediation applied successfully or not."  };
		
		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file publicly.");
			sfapi.createFileShareLink(fileId);

			LogUtils.logStep("3. Checking the share link before remediation..");
			FileShareLink fsl1 = sfapi.getFileShareLink(fileId);
			Reporter.log("FileShareLink contents:"+ MarshallingUtils.marshall(fsl1), true);
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			
			LogUtils.logStep("4. Share the file externally with Edit Permission as well.");
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "ExtUser11");
			String userId2 = sfapi.getUserId(internalUserId1, "Securletuser2", "SUser2", "SecBeatle1");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("5. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"UNSHARE", "COLLAB_UPDATE", "COLLAB_REMOVE"};
			String[] collaborators={"",externalUserId1, internalUserId1};
			String[] metaInfo={"open", "V", "C"}; //Just passing two args.. no use
			
			LogUtils.logStep("6. Applying the UI remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			
			UIRemediationObject UIRemedObject = new UIRemediationObject();
			UIRemediationSource UISource = new UIRemediationSource();
			UIRemediationInnerObject UIInnerObject = new UIRemediationInnerObject();
			ArrayList<SecurletRemediation> remedList = new ArrayList<SecurletRemediation>();
			remedList.add(sfremediation);
			UIInnerObject.setObjects(remedList);
			UISource.setObjects(UIInnerObject);
			UISource.setApp(facility.Salesforce.name());
			UIRemedObject.setSource(UISource);
			//Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
			
			//Perform the UI remediation
			remediateExposureWithUIServer(UIRemedObject);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("V")) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("C")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File collaboarator is updated", "File collaboarator is not updated");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(sfapi.getFileShareLink(fileId) == null, "File is unshared", "File is not unshared");
			
			LogUtils.logStep("7. Remediation completed successfully");

			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 25
	 * @throws Exception
	 */
	@Test(groups={"UIREMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION", "P1"})
	public void exposeFileWithExternalCollaboratorEditAndUIRemediateWithRemoveCollaborator() throws Exception {
		
		String steps[] =
			{		"1. Upload a file.", 
					"2. Share the file to one external user with (Collaborator) permission.",
					"3. With UI remediation API, remove the collaborator.",
					"4. Verify whether remediation applied successfully or not."  };

		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file externally with Edit Permission as well.");
			
			
			//Get the user id for fileshare
			String userId = sfapi.getExternalUserId(externalUserId1, "Securletuser", "SUser", "ExtUser11");
			String ids[] = {userId};
			String sharingTypes[] = {"C"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("3. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId) && share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			CustomAssertion.assertTrue(shared, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			String[] remedialAction={"COLLAB_REMOVE"};
			String[] collaborators={externalUserId1};
			
			LogUtils.logStep("4. Applying the remediation..");
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, null, collaborators);
			
			UIRemediationObject UIRemedObject = new UIRemediationObject();
			UIRemediationSource UISource = new UIRemediationSource();
			UIRemediationInnerObject UIInnerObject = new UIRemediationInnerObject();
			ArrayList<SecurletRemediation> remedList = new ArrayList<SecurletRemediation>();
			remedList.add(sfremediation);
			UIInnerObject.setObjects(remedList);
			UISource.setObjects(UIInnerObject);
			UISource.setApp(facility.Salesforce.name());
			UIRemedObject.setSource(UISource);
			
			//Perform the UI remediation
			remediateExposureWithUIServer(UIRemedObject);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId) && share.getSharingType().equals("C")) {
					shared = true;
				}
			}
			CustomAssertion.assertTrue(!shared, "File collaboarator is removed", "File collaboarator is not removed");
			
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 26
	 * @throws Exception
	 */
	@Test(groups={"REMEDIATION", "EXTERNAL", "SALESFORCE", "REGRESSION", "P1"})
	public void bulkRemediateExternalCollaborator() throws Exception {
		
		String steps[] = 
			{		"1. Upload a file.", 
					"2. Share the file to two external users of which user1(Collaborator) and user2(Viewer) permission.",
					"3. With bulk remediation API, remove both the users",
					"4. Verify whether remediation applied successfully or not."  };
		
		LogUtils.logTestDescription(steps);
		String fileId = "";
		try {
			LogUtils.logStep("1. Uploading the file to salesforce chatter.");
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			destinationFile = randomId + "_" + sourceFile;
			
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			fileId = chatterfile.getId();
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			
			LogUtils.logStep("2. Share the file externally with Edit Permission as well.");
			
			
			//Get the user id for fileshare
			String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser1", "SUser1", "ExtUser11");
			String userId2 = sfapi.getExternalUserId(externalUserId2, "Securletuser2", "SUser2", "Securlets");
			
			String ids[] = {userId1, userId2};
			String sharingTypes[] = {"C", "V"};
			FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with edit permission.");
			sfapi.shareFilewithCollaborator(fileId, fsi);
			
			LogUtils.logStep("3. Checking the file shares before remediation..");
			FileShares fsl = sfapi.getFileShares(fileId);
			Reporter.log("FileShares:"+ MarshallingUtils.marshall(fsl), true);
			boolean shared1=false, shared2 = false;
			for (Share share : fsl.getShares()) {
				if (share.getEntity().getId().equals(userId1) && share.getSharingType().equals("C")) {
					shared1 = true;
				}
				
				if (share.getEntity().getId().equals(userId2) && share.getSharingType().equals("V")) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(shared1, "File is shared with Edit permission to the external collaborator", "File is not shared with Edit permission to the external collaborator");
			CustomAssertion.assertTrue(shared1, "File is shared with View permission to the external collaborator", "File is not shared with View permission to the external collaborator");
			
			sleep(sleepTime);
			CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_salesforce.name(), fileId), "Exposed document is available in db", "Exposed document is not available in db");
			//Prepare the input for unsharing the file
			
			//String[] remedialAction={"COLLAB_UPDATE", "COLLAB_UPDATE"};
			String[] collaborators={externalUserId1, externalUserId2};
			//String[] metaInfo={"V", "C"};
			
			LogUtils.logStep("4. Applying the bulk remediation..");
			/*			
			SecurletRemediation sfremediation = getSalesforceRemediationObject(saasAppUser, chatterfile.getOwner().getId(), chatterfile.getType(),
					chatterfile.getId(), this.instanceId, "open", remedialAction, metaInfo, collaborators);
			*/
			
			SecurletRemediation sfremediation = getSecurletBulkRemediationObject(new ArrayList<String>(Arrays.asList(collaborators)));
			
			//getBulkRemediationObject
			remediateSalesforceExposureWithAPI(sfremediation);
			
			sleep(sleepTime);
			
			Reporter.log("FileShares for the file "+ MarshallingUtils.marshall(sfapi.getFileShares(fileId)), true);
			FileShares fileShares = sfapi.getFileShares(fileId);
			
			shared1 = false; shared2 = false;
			for (Share share : fileShares.getShares()) {
				if (share.getEntity().getId().equals(userId1)) {
					shared1 = true;
				}
				if (share.getEntity().getId().equals(userId2)) {
					shared2 = true;
				}
			}
			CustomAssertion.assertTrue(!shared1, "File collaboarator is removed", "File collaboarator is not removed");
			CustomAssertion.assertTrue(!shared2, "File collaboarator is removed", "File collaboarator is not removed");
			
			LogUtils.logStep("5. Remediation completed successfully");
			
			Reporter.log("Cleaning the files...", true);
			sfapi.deleteFile(fileId);
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
