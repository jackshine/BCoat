package com.elastica.beatle.audit.dummy.factoryClass;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.fileHandler.TestFileUtils;

public class AuditdummyScpSanityTests extends AuditInitializeTests {

	protected Client restClient;
	protected String sourceID = null;
	protected String fileToBeUploaded;
	protected String sftpTenantUsername;
	protected String sftpServerHost;
	protected String sftpServerDestinationDir;
	protected String fireWallType;
	protected String scpPayload;
	protected String firewallLogFilePath;
	Properties firewallLogDataProps;
	protected ArrayList<String> datasourceIdsList=new ArrayList<String>();
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	protected String scpcompltedCheckEmptyFilePath;
	protected String scpPreviousRunDsFilePath;

	String prevRunDsID;
	String folderName;
	String sanityprevrunFolderName="_Sanity_SCP_PRERUNDS";
	String sanityprevrunFileName="";
	private S3ActionHandler s3Handler;
	private JSONObject s3BucketDetails;






	public AuditdummyScpSanityTests(String fireWallType) {
		restClient = new Client();
		this.fireWallType = fireWallType;
	}

	/**
	 * Prepares the payload for datasource creation
	 * set the firewall log path. 
	 * @throws Exception
	 */
	@BeforeClass(alwaysRun = true)
	public void intScpSftpData() throws Exception{
		s3Handler = new S3ActionHandler();
		s3BucketDetails=AuditTestUtils.getS3BucketDetails();


		scpPayload = AuditTestUtils.createSCPUploadBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SCP_DS_NAME,"SANITY");
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
		scpPreviousRunDsFilePath=previousRunScpDSFileLocation(suiteData.getEnvironmentName());
		Reporter.log("previous run datasource fie name:"+scpPreviousRunDsFilePath,true);

		sanityprevrunFileName=suiteData.getEnvironmentName()+"_sanityscpds.txt";
		folderName= suiteData.getEnvironmentName()+sanityprevrunFolderName;


	}

	public String previousRunScpDSFileLocation(String envName){

		String prevRunDSIDFileLocation=null;
		switch(envName)
		{
		case "prod": {
			prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_PROD_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH;
			break;
		}
		case "cep": {
			prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_CEP_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH;
			break;
		}
		case "qavpc": {
			prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_VPC_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH;
			break;
		}
		case "eoe": {
			prevRunDSIDFileLocation=AuditTestConstants.AUDIT_SANITY_EOE_SCP_DUMMY_PREVIOUS_RUN_DSID_LOCATION_PATH;
			break;
		}
		default:
			break;
		}
		return prevRunDSIDFileLocation;

	}


	@Test(description="Dataource creation for Scp/Sftp ")
	public void testSCPDatasourceCreation_new() throws Exception {
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through SCP Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);

		Reporter.log("*************Datasource Creation started for:"+fireWallType+"****************",true);
		Reporter.log("Request Payload: "+scpPayload,true);
		//TestFileUtils.writeDatasourceIntoFile("123", scpPreviousRunDsFilePath);


		String dsIdFromPreviousRun=s3Handler.readFromS3(s3BucketDetails.getString("bucket"),folderName+"/"+sanityprevrunFileName) ;
		Reporter.log("previous run datasource id..."+dsIdFromPreviousRun,true);


		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(scpPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject scpConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		Reporter.log("Actual ConnectionObject Response: "+scpConnectionObject,true);
		String str_last_status_or_detectstatus="Pending Data";
		String expectedConnectionObject=    " [sourceID=" + "sourceID should not be null/empty"+
				", sftpServerDestinationDir=" + "uploaded_path should not be null/empty" +
				", log_transport=" + AuditTestConstants.AUDIT_SCP_FILEFORMAT +
				", Resource URI =" + "Resource URI should not be null/empty" +
				", datasource_type=" + "datasource_type should not be null/empty" +
				", datasource_format=" + "datasource_format should not be null/empty"+
				", last_status=" + str_last_status_or_detectstatus +
				", last_detect_status=" + str_last_status_or_detectstatus+" ]";
		Reporter.log("Expected ConnectionObject Response fields: "+expectedConnectionObject,true);
		validateCreatedDataSource(scpConnectionObject);
		sourceID = (String) scpConnectionObject.get("id");
		sftpServerDestinationDir=(String)scpConnectionObject.get("upload_path");

		//TestFileUtils.writeDatasourceIntoFile(sourceID, scpPreviousRunDsFilePath);
		FileHandlingUtils.writeDatasourceIntoFile(sourceID, scpPreviousRunDsFilePath);
		uploadFile(scpPreviousRunDsFilePath);
		Reporter.log("new File is written..",true);

		// get credentials of scp/sftp connection
		Reporter.log("**********************Get Scp Credentials***************************************",true);
		HttpResponse getScpCredentialsResponse = AuditFunctions.getCredentials(restClient);
		Assert.assertEquals(getScpCredentialsResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK);
		JSONObject scpCredentialsMetaData = (JSONObject) new JSONObject(ClientUtil.getResponseBody(getScpCredentialsResponse)).getJSONArray("objects").get(0);
		Reporter.log("Actual Credentials Response: "+scpCredentialsMetaData,true);
		String expectedCredentials=    " [sourceID=" + "sourceID should not be null/empty"+
				", username=" + "scp server username should not be null/empty" +
				", password=" + "scp server password should not be null/empty" +
				", created_on =" + "created_on should not be null/empty" +
				", modified_on=" + "modified_on should not be null/empty" +
				", resource_uri=" + "resource_uri should not be null/empty"+
				", server=" + "server should not be null/empty" +" ]";
		Reporter.log("Expected Credentials Response: "+expectedCredentials,true);
		Reporter.log("Validating the credentials object:");
		//validatescpCrentials(scpCredentialsMetaData);
		sftpTenantUsername=(String) scpCredentialsMetaData.get("username");
		//sftpServerHost=(String) scpCredentialsMetaData.get("server");
		sftpServerHost=getScpSftpServerHost();

		Reporter.log("******************Upload file using Scp:****************************************",true);
		//upload file to the datasource using scp
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(sftpTenantUsername, sftpServerHost,sftpServerDestinationDir);
		Reporter.log("******************scp upload detais:********************",true);
		Reporter.log("scpUsername:"+sftpTenantUsername,true);
		Reporter.log("scpPassword:"+sftpUtils.getPassWord(),true);
		Reporter.log("scpServerHost:"+sftpServerHost,true);
		Reporter.log("scpServerDestinationDir:"+sftpServerDestinationDir,true);
		Reporter.log("scpUploadedfirewallLogFilePath:"+firewallLogFilePath,true);

		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
		Reporter.log("******************scp upload started:********************",true);
		result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
		Reporter.log("scp file upload status:  " + result,true);
		Reporter.log("******************scp file upload completed sucessfully:********************",true);

		//upload completed check
		FileInputStream fuploadCompltedCheckInputStream;
		File uploadCompletedFile = new File(System.getProperty("user.dir")+scpcompltedCheckEmptyFilePath);
		fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
		String uploadCompletedStatus = "";
		Reporter.log("******************scp upload completed started:********************",true);
		result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
		Reporter.log("scp completed status:  " + uploadCompletedStatus,true);
		Reporter.log("******************SCP COMPLETED:********************",true);
		//upload conpleted check end

		Thread.sleep(10000);//wait 10 sec

		Reporter.log("******************Get Datasource sttaus verification****************************************",true);
		//verification logfile process attached to the datasource
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);
		String last_Status = pollRespObject.getString("last_status");
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&&
				currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME){
			Reporter.log("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of "+ sourceID +" is "+ last_Status, true);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))	{	
				TestFileUtils.writeDatasourceIntoFile(sourceID, scpPreviousRunDsFilePath); //if current datasource is is completed we are writing this into new file.
				uploadFile(scpPreviousRunDsFilePath);
				Reporter.log("new File is written..",true);
				break;
			}
		}
		Reporter.log("current run ds status: after 30 mins::["+sourceID+"-"+last_Status+"]",true);

		if(!("Completed".equals(last_Status))){ //check the current datasource status after 30 mins
			Reporter.log("enter preivous run ds verifications"+dsIdFromPreviousRun,true);
			TestFileUtils.writeDatasourceIntoFile(sourceID, scpPreviousRunDsFilePath); //if current datasource is is not in completed we are writing this into new file.
			uploadFile(scpPreviousRunDsFilePath);
			Reporter.log("new File is written..",true);
			String previousRunDSStatus="";

			//Verify the previous run datasource; if null/empty failing the test
			Assert.assertNotNull(dsIdFromPreviousRun,"previous run datasource:["+dsIdFromPreviousRun+"] is empty/null and  Current Datasoure process took more than "+
					(int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. Current Datasource details id - status:["+sourceID+"-"+last_Status+"]" );


			//if previous run datasource not null then poll the datasource
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, dsIdFromPreviousRun);

			//check the previous run datasource, if reponse is other than 200 throwing previous run ds is not valid.
			Assert.assertTrue( (pollForStatusResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK), "previous run datasource:["+dsIdFromPreviousRun+"] is not exist in the system/not Valid and  Current Datasoure process took more than "+
					(int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. Current Datasource details id - status:["+sourceID+"-"+last_Status+"]" );


			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			previousRunDSStatus = pollRespObject.getString("last_status");

			if("Completed".equals(previousRunDSStatus))	{	//check the prev run ds completed status
				sourceID=dsIdFromPreviousRun;   //map the previous datasource id to current datasourceid
			}
			else{ //if the prevous run ds staus also not completed then failing the test with both previous and current ds id's with status.
				pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
				Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
				pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
				last_Status = pollRespObject.getString("last_status");
				Assert.assertTrue(false," Current Datasoure process took more than "+
						(int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. Current Datasource id - status:["+sourceID+"-"+last_Status+"]" +"previous run Datasource id - status:["+dsIdFromPreviousRun+"-"+previousRunDSStatus+"]");

			}
		}
		Reporter.log("************************** Scp Datasource process Completed sucessfully ******************************", true);
		Reporter.log("**************************Scp Datasource creation Test Completed**************************",true);

	}





	@Test(description="deleteDatasources created for Scp/Sftp",dependsOnMethods={"testSCPDatasourceCreation_new"})
	public void deleteDataSourceTest() throws Exception {

		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Datasource delete Api for the created Datasource", true);
		Reporter.log("2. Deleting Data Source for "+ fireWallType +" its ID is: "+sourceID+" started",true);
		Reporter.log("************************************************************************** ", true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
		Reporter.log("************************** deleteDataSourceTest comleted ****************************** ", true);

	}


	/**
	 * validate created datasource object
	 * @param scpConnectionObject
	 * @throws Exception
	 */
	private void validateCreatedDataSource(JSONObject scpConnectionObject)
			throws Exception {
		Assert.assertEquals(scpConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SCP_FILEFORMAT);
		//Assert.assertEquals(scpConnectionObject.get("datasource_type"), dataSourceDTO.get("datasource_type"));
		Assert.assertNotNull(scpConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(scpConnectionObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) scpConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertNotNull(scpConnectionObject.get("setup_by"), "SetUp by is null");
		Assert.assertFalse(((String) scpConnectionObject.get("datasource_format")).isEmpty(),
				"Data source format is empty");
		Assert.assertFalse(((String) scpConnectionObject.get("datasource_type")).isEmpty(),
				"Data source type is empty");
		Assert.assertFalse(((String) scpConnectionObject.get("setup_by")).isEmpty(), "Set Up by is empty");
		Assert.assertEquals(scpConnectionObject.get("last_status"), "Pending Data",
				"Last status is not \"Pending Data\"");
		Assert.assertEquals(scpConnectionObject.get("last_detect_status"), "Pending Data",
				"Last status is not \"Pending Data\"");
	}

	/**
	 * validate getcredentials api
	 * @param scpCredentialsObject
	 * @throws Exception
	 */
	private void validatescpCrentials(JSONObject scpCredentialsObject) throws Exception
	{
		Assert.assertNotNull(scpCredentialsObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(scpCredentialsObject.get("username"), "scp server username is null");
		Assert.assertNotNull(scpCredentialsObject.get("password"), "scp server password is null");
		Assert.assertNotNull(scpCredentialsObject.get("created_on"), "scp server created on  is null");
		Assert.assertNotNull(scpCredentialsObject.get("modified_on"), "scp server modified on is null");
		Assert.assertNotNull(scpCredentialsObject.get("resource_uri"), "scp server getCredentials resource url is null");
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_QAVPC_SCP_SERVER);
		}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER_PROD);
		}
		else if(suiteData.getApiserverHostName().contains("api-cep.elastica.net")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER_CEP);
		}
		else if(suiteData.getApiserverHostName().contains("api-cwsintg.elastica-inc.com")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER_CWSINTG);
		}
		else{
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER);

		}


	}	
	/**
	 * prepare sftpUtils configuration
	 * @param sftpTenantUsername
	 * @param sftpServerHost
	 * @param sftpServerDestinationDir
	 * @return
	 */
	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String sftpServerHost, String sftpServerDestinationDir ) {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_QAVPC_SCP_SANITY_PWD);}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_PROD_SCP_SANITY_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("api-cep.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_CEP_SCP_SANITY_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("api-cep.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_CEP_SCP_SANITY_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("api-cwsintg.elastica-inc.com")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CWS_SCP_PWD);
		}
		else{
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_EOE_SCP_SANITY_PWD);
		}
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
	}

	public String getScpSftpServerHost()
	{
		String sftpServerHost=null;
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			sftpServerHost=AuditTestConstants.AUDIT_QAVPC_SCP_SERVER;}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_PROD;
		}
		else if(suiteData.getApiserverHostName().contains("api-cep.elastica.net")){
			sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_CEP;
		}
		else{
			sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER;
		}
		Reporter.log("scp/sftp serverhost: "+sftpServerHost,true);
		return sftpServerHost;
	}

	private void uploadFile(String fileTobeUploaded) throws Exception{
		s3Handler.uploadLogFileToS3Sanity(s3BucketDetails.getString("bucket"),
				folderName+"/",
				sanityprevrunFileName, 
				new File(FileHandlingUtils.getFileAbsolutePath(fileTobeUploaded)));

	}




}
