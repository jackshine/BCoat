/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author Mallesh
 *
 */
public class AuditScpMultiFileCopyTests extends AuditInitializeTests{
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
	
	
	JSONArray hardWareIds=null;
	ArrayList<String> deviceLogsData=new ArrayList<String>();
	/**
	 * @param FireWallName
	 */
	public AuditScpMultiFileCopyTests(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
	}	
	/**
	 * Prepares the payload for datasource creation
	 * set the firewall log path. 
	 * @throws Exception
	 */
	@BeforeClass(alwaysRun = true)
	public void intScpSftpData() throws Exception{
		
	
		scpPayload = AuditTestUtils.createDeviceIdSCPUploadBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SCP_DEVICE_ID_DS_NAME,"NEG_MULTI");
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
		
	}
	
	@Test(priority=1)
	public void testCreateDeviceLogsUsingScpTransPort() throws IOException, Exception{						
				
		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(scpPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject scpConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		validateCreatedDataSource(scpConnectionObject);
		sourceID = (String) scpConnectionObject.get("id");
		sftpServerDestinationDir=(String)scpConnectionObject.get("upload_path");

		// get credentials of scp/sftp connection
		HttpResponse getScpCredentialsResponse = AuditFunctions.getCredentials(restClient);
		Assert.assertEquals(getScpCredentialsResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK);
		JSONObject scpCredentialsMetaData = (JSONObject) new JSONObject(ClientUtil.getResponseBody(getScpCredentialsResponse)).getJSONArray("objects").get(0);	
		//validatescpCrentials(scpCredentialsMetaData);
		sftpTenantUsername=(String) scpCredentialsMetaData.get("username");
		//sftpServerHost=(String) scpCredentialsMetaData.get("server");
		sftpServerHost=getScpSftpServerHost();
		

		//upload file to the datasource using scp
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(sftpTenantUsername, sftpServerHost,sftpServerDestinationDir);
		
		//attached 3 files of same firewall type to same scp data source
		String[] filesArray={"1","2","3"};
		File file=null;
		for(String fileno:filesArray)
		{
			firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType+""+fileno);
			FileInputStream fin;
			file= new File(System.getProperty("user.dir")+firewallLogFilePath);
			fin = new FileInputStream(file);
			String result = "";
			result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
			Reporter.log("sftp file upload status:  " + result,true);
			
			
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
			
		}
		

		//verification logfile process attached to the datasource
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&&
				currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Reporter.log("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of "+ sourceID +" is "+ last_Status, true);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))			
				break;
		}
		
		
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);

	
	}
	
	public String getScpSftpServerHost()
	{
		String sftpServerHost=null;
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			sftpServerHost=AuditTestConstants.AUDIT_QAVPC_SCP_SERVER;}
        else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
        	sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_PROD;
        }
        else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
        	sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_CEP;
        }
        else{
        	sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER;
        }
		Reporter.log("scp/sftp serverhost: "+sftpServerHost,true);
		return sftpServerHost;
	}
	
	
	
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
        if(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("vpcauditnegative")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_VPC_NEGATIVETESTS_SCP_PWD);}
        else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD);
        }
        else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
        	sftpUtils.setPassWord("D7slhZ74");
        }
        else{
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_EOE_NEGATIVETESTS_TENANT_SCP_PWD);
        }
        sftpUtils.setDestinationDir(sftpServerDestinationDir);
        return sftpUtils;
	}
	
	
	
}
