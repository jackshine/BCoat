/**
 * 
 */
package com.elastica.beatle.audit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Cell;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.AuditSummaryDataObjects.CompanyBrr;
import com.elastica.beatle.AuditSummaryDataObjects.TopRiskyService;
import com.elastica.beatle.AuditSummaryDataObjects.TopUsedService;
import com.elastica.beatle.AuditSummaryDataObjects.TopUser;
import com.elastica.beatle.AuditSummaryDataObjects.TopUser_;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.Objects;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * @author anuvrath
 *
 */
public class AuditTestUtils {

	protected static final String NAME = "name";
	private static final String RESOURCE_URI = "resource_uri";
	private static final String APPLICATION_JSON = "application/json";


	private static String getCompressFormat(String fireWallType){
		
		return (
				fireWallType.contains("7z")?"7z": fireWallType.contains("7za")?"7za" :fireWallType.contains("bz2")?"bz2":fireWallType.contains("gz")?"gz":"zip"
				);
		
	}
	/**
	 * @param fireWallType
	 * @return
	 * @throws JSONException
	 */
	public static String createWebUploadPostBody(String fireWallType, String env, String transportType) throws JSONException {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(fireWallType),transportType));
		//json.put("name", "BE_"+fireWallType);
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_WEBUPLOAD_FILEFORMAT);						
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));
		
		if (fileProperties.get(fireWallType+".filename").contains("BE_Bluecoat_Splunk_mandatory_headers_quote_header")||fileProperties.get(fireWallType+".filename").contains("BE_Bluecoat_Splunk_mandatory_headers_space_escaping"))
		{
			System.out.println("Adding logfile_headers : "+fireWallType);
			json.put("logfile_headers", fileProperties.get(fireWallType+".logfile_headers"));
		}
		
		return json.toString();
	}	
	public static String createWebUploadPostBody(String fireWallType, String env, String transportType, String partDSName) throws JSONException {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(fireWallType),transportType,partDSName));		
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_WEBUPLOAD_FILEFORMAT);						
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));
		return json.toString();
	}	

	public static String createWebUploadPostBodyForInvalidLogs(String fireWallType, String env) throws JSONException {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDataSourceNameForInvalidLog(env));		
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_WEBUPLOAD_FILEFORMAT);						
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));
		return json.toString();
	}



	/**
	 * @param fireWallType
	 * @param S3PropertiesObject
	 * @return
	 * @throws JSONException
	 */
	public static String createS3UploadPostBody(String fireWallType, JSONObject S3PropertiesObject, String env,String transportType,String partDSName) throws JSONException{
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name",generateDatasourceName(env+getCompressFormat(fireWallType),transportType,partDSName));
		json.put("type", fileProperties.get(fireWallType+".type"));
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("log_transport", AuditTestConstants.AUDIT_S3_LOGTRANSPORT);
		json.put("host_base", S3PropertiesObject.getString("host_base"));
		json.put("bucket", S3PropertiesObject.getString("bucket"));
		json.put("access_key", S3PropertiesObject.getString("access_key"));
		json.put("access_secret", S3PropertiesObject.getString("aws_secret"));
		json.put("use_https", true);
		json.put("input_folder", S3PropertiesObject.getString("input_folder"));
		json.put("delete_source", false);
		return json.toString();
	}
	public static String createS3UploadPostBody(String fireWallType, JSONObject S3PropertiesObject, String env,String transportType) throws JSONException{
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name",generateDatasourceName(env+getCompressFormat(fireWallType),transportType));
		json.put("type", fileProperties.get(fireWallType+".type"));
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("log_transport", AuditTestConstants.AUDIT_S3_LOGTRANSPORT);
		json.put("host_base", S3PropertiesObject.getString("host_base"));
		json.put("bucket", S3PropertiesObject.getString("bucket"));
		json.put("access_key", S3PropertiesObject.getString("access_key"));
		json.put("access_secret", S3PropertiesObject.getString("aws_secret"));
		json.put("use_https", true);
		json.put("input_folder", S3PropertiesObject.getString("input_folder"));
		json.put("delete_source", false);
		
		if (fileProperties.get(fireWallType+".filename").contains("BE_Bluecoat_Splunk_mandatory_headers_quote_header")||fileProperties.get(fireWallType+".filename").contains("BE_Bluecoat_Splunk_mandatory_headers_space_escaping"))
		{
			System.out.println("Adding logfile_headers : "+fireWallType);
			json.put("logfile_headers", fileProperties.get(fireWallType+".logfile_headers"));
		}
		
		return json.toString();
	}


	public static String createDeviceIdS3UploadPostBody(String fireWallType, JSONObject S3PropertiesObject,String env,String transportType) throws JSONException{
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name",generateDatasourceName(env+getCompressFormat(fireWallType),transportType));
		json.put("type", fileProperties.get(fireWallType+".type"));
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("log_transport", AuditTestConstants.AUDIT_S3_LOGTRANSPORT);
		json.put("host_base", S3PropertiesObject.getString("host_base"));
		json.put("bucket", S3PropertiesObject.getString("bucket"));
		json.put("access_key", S3PropertiesObject.getString("access_key"));
		json.put("access_secret", S3PropertiesObject.getString("aws_secret"));
		json.put("use_https", true);
		json.put("input_folder", S3PropertiesObject.getString("input_folder"));
		json.put("delete_source", false);
		return json.toString();
	}


	/**
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject buildS3CredentialCheckPostBody(String fireWallType) throws JSONException{
		JSONObject json = new JSONObject();
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);		
		json.put("host_base", s3Properties.get("S3HostName"));
		json.put("access_key", s3Properties.get("S3AccessKey"));
		json.put("aws_secret", s3Properties.get("S3AccessSecret"));
		json.put("bucket", s3Properties.get("BucketName"));
		json.put("is_secure", true);
		json.put("input_folder", s3Properties.get("folderName")+"/"+s3Properties.get(fireWallType+".S3FolderName"));
		return json;
	}
	
	public static JSONObject buildS3CredentialCheckPostBody(String fireWallType,String folderName) throws JSONException{
		JSONObject json = new JSONObject();
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);		
		json.put("host_base", s3Properties.get("S3HostName"));
		json.put("access_key", s3Properties.get("S3AccessKey"));
		json.put("aws_secret", s3Properties.get("S3AccessSecret"));
		json.put("bucket", s3Properties.get("BucketName"));
		json.put("is_secure", true);
		json.put("input_folder", folderName);
		return json;
	}
	public static String getUniqFolderName(String env, String regressionType,String tenantName){
        return env+"_"+regressionType+"_"+DateUtils.getCurrentTime()+"_".concat(tenantName);
    }
	
	public static JSONObject getS3BucketDetails() throws JSONException{
		JSONObject json = new JSONObject();
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);		
		json.put("host_base", s3Properties.get("S3HostName"));
		json.put("access_key", s3Properties.get("S3AccessKey"));
		json.put("aws_secret", s3Properties.get("S3AccessSecret"));
		json.put("bucket", s3Properties.get("BucketName"));
		json.put("is_secure", true);
		return json;
	}
	
	/**
	 * @return
	 */
	

	private static String generateDataSourceNameForInvalidLog(String env){
		return "BE_NEG_"+env+"_"+"_WU_"+""+new SimpleDateFormat("MM:dd:yy_HH:mm:ss.SSS").format(new Date());		
	}

	private static String generateDatasourceName(String env, String transportType)
	{
		return "BE_"+env+"_"+transportType+"_"+new SimpleDateFormat("MM:dd:yy_HH:mm:ss.SSS").format(new Date());
	}
	private static String generateDatasourceName(String env, String transportType,String partDSName)
	{
		return "BE_"+partDSName+"_"+env+"_"+transportType+"_"+new SimpleDateFormat("MM:dd:yy_HH:mm:ss.SSS").format(new Date());
	}



	/**
	 * @param fireWallType 
	 * @return 
	 */
	public static String getFireWallLogFileName(String fireWallType) {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		return fileProperties.get(fireWallType+".filename");
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getAuditPreferenceJSON() throws FileNotFoundException, IOException{
		return FileHandlingUtils.readDataFromFile(AuditTestConstants.PREFERENCEJSON_FILEPATH);
	}
	public static String getRestoreDefaultsPreferenceJSON() throws FileNotFoundException, IOException{
		return FileHandlingUtils.readDataFromFile(AuditTestConstants.RESTOREDEFAULTSPREFERENCEJSON_FILEPATH);
	}
	
	/**
	 * @param fireWallType
	 * @return
	 */
	public static String getFirewallLogFilePath(String fireWallType) {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		return fileProperties.get(fireWallType+".LogFilePath");
	}

	/**
	 * @param fireWallType
	 * @param env
	 * @param transportType
	 * @return
	 * @throws JSONException
	 */
	public static String createSCPUploadBody(String fireWallType,String env,String transportType) throws JSONException {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(fireWallType),transportType));		
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_SCP_FILEFORMAT);						
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));	
		
		if (fileProperties.get(fireWallType+".filename").contains("BE_Bluecoat_Splunk_mandatory_headers_quote_header")||fileProperties.get(fireWallType+".filename").contains("BE_Bluecoat_Splunk_mandatory_headers_space_escaping"))
		{
			System.out.println("Adding logfile_headers : "+fireWallType);
			json.put("logfile_headers", fileProperties.get(fireWallType+".logfile_headers"));
		}
		
		return json.toString();
	}
	/**
	 * @param fireWallType
	 * @param env
	 * @param transportType
	 * @param partDSName
	 * @return
	 * @throws JSONException
	 */
	public static String createSCPUploadBody(String fireWallType,String env,String transportType,String partDSName) throws JSONException {

		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(fireWallType),transportType,partDSName));		
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_SCP_FILEFORMAT);						
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));						
		return json.toString();

	}

	/**
	 * @param fireWallType
	 * @param env
	 * @param transportType
	 * @return
	 * @throws JSONException
	 */
	public static String createDeviceIdSCPUploadBody(String fireWallType,String env,String transportType) throws JSONException {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name",generateDatasourceName(env+getCompressFormat(fireWallType),transportType));		
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_SCP_FILEFORMAT);						
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));						
		return json.toString();
	}
	
	/**
	 * @param fireWallType
	 * @param env
	 * @param transportType
	 * @param partDSName
	 * @return
	 * @throws JSONException
	 */
	public static String createDeviceIdSCPUploadBody(String fireWallType,String env,String transportType,String partDSName) throws JSONException {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name",generateDatasourceName(env+getCompressFormat(fireWallType),transportType,partDSName));		
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_SCP_FILEFORMAT);						
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));						
		return json.toString();
	}

	/**
	 * @param fireWallType
	 * @param agentId
	 * @param env
	 * @param transportType
	 * @return
	 * @throws JSONException
	 */
	public static String createSpanVAUploadBody(String fireWallType,String agentId,String env, String transportType) throws JSONException {

		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(fireWallType),transportType));		
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);						
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));
		json.put("agent_id", agentId);
		JSONObject agentInfoJson = new JSONObject();
		agentInfoJson.put("log_collection_type", "ftp_server");
		json.put("agent_info", agentInfoJson);
		return json.toString();

	}
	
	/**
	 * @param firewallType
	 * @param agentId
	 * @param env
	 * @param transportType
	 * @return
	 * @throws JSONException
	 */
	public static String createSpanVAUploadDSPayloadForSyslog(String firewallType,String agentId,String env,String transportType) throws JSONException {
		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(firewallType),transportType));
		json.put("agent_id", agentId);
		json.put("type", fileProperties.get(firewallType+".type"));
		json.put("log_transport", AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);						
		json.put("log_format", fileProperties.get(firewallType+".log_format"));
		JSONObject subObject = new  JSONObject();
		subObject.put("log_collection_type", "syslog");
		subObject.put("protocol", "bsd");
		json.put("agent_info", subObject);
		return json.toString();
	}
	
	public static String createNFSUploadPayload(String fireWallType,String agentId,String env, String transportType, String srcdir, String spanvaHost) throws JSONException {

		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(fireWallType),transportType));	
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);	
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));
		json.put("agent_id", agentId);
		JSONObject agentInfoJson = new JSONObject();
		agentInfoJson.put("log_collection_type", "network_file");
		agentInfoJson.put("host", spanvaHost);
		agentInfoJson.put("type", "nfs");
		agentInfoJson.put("src_dir", srcdir);
		agentInfoJson.put("delete_remote_files", "false");
		
		json.put("agent_info", agentInfoJson);

		return json.toString();

	}
	
	public static String createSCPClientSUploadPayload(String fireWallType,String agentId,String env, String transportType, String srcdir, String networkHost,
			String username, String userPwd) throws JSONException {

		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(fireWallType),transportType));	
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);	
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));
		json.put("agent_id", agentId);
		JSONObject agentInfoJson = new JSONObject();
		agentInfoJson.put("log_collection_type", "scp_client");
		agentInfoJson.put("host", networkHost);
		agentInfoJson.put("src_dir", srcdir);
		agentInfoJson.put("delete_remote_files", "false");
		agentInfoJson.put("user", username);
		agentInfoJson.put("password", userPwd);
		
		json.put("agent_info", agentInfoJson);

		return json.toString();

	}
	public static String createFTPClientSUploadPayload(String fireWallType,String agentId,String env, String transportType, String srcdir, String networkHost,
			String username, String userPwd) throws JSONException {

		Map<String, String> fileProperties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_FIREWALL_CONFIG_FILEPATH);
		JSONObject json = new JSONObject();
		json.put("name", generateDatasourceName(env+getCompressFormat(fireWallType),transportType));	
		json.put("datasource_type", fileProperties.get(fireWallType+".DataSourceType"));		
		json.put("datasource_format", fileProperties.get(fireWallType+".DataSourceFormat"));
		json.put("log_transport", AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);	
		json.put("log_format", fileProperties.get(fireWallType+".log_format"));
		json.put("type", fileProperties.get(fireWallType+".type"));
		json.put("agent_id", agentId);
		JSONObject agentInfoJson = new JSONObject();
		agentInfoJson.put("log_collection_type", "ftp_client");
		agentInfoJson.put("host", networkHost);
		agentInfoJson.put("src_dir", srcdir);
		agentInfoJson.put("delete_remote_files", "false");
		agentInfoJson.put("user", username);
		agentInfoJson.put("password", userPwd);
		
		json.put("agent_info", agentInfoJson);

		return json.toString();

	}
	


	public static String createAgentPayload() throws JSONException {

		JSONObject json = new JSONObject();

		return json.toString();

	}

	//utility functions for scp/sftp

	public static String  uploadLogFileThrSFTPChannel(String privateKeyPath, String fileTobeUploaded, String username,
			String fileServer, String uploaded_Path, String datasourceId) throws IOException {

		System.out.println(""+privateKeyPath);

		JSch jsch = new JSch();
		String user = username;
		String host = fileServer;
		int port = 22;
		String privateKey = privateKeyPath;
		String strChannel = "sftp";
		String upload_path = uploaded_Path;
		String localFilePath = fileTobeUploaded;
		ChannelSftp channelSftp = null;
		Session session=null;
		String result_text = "";

		String setupWorkingDir = "/home/"+username+"/datasources/";

		try{
			jsch.addIdentity(privateKey);
			System.out.println("identity added ");

			session = jsch.getSession(user, host, port);
			System.out.println("session created.");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			System.out.println("session connected.....");

			ChannelSftp channel = null;
			channel = (ChannelSftp) session.openChannel(strChannel);
			channel.connect();

			System.out.println("channel connected.....");


			channelSftp = (ChannelSftp) channel;
			System.out.println("current sftp directory"+channelSftp.pwd());
			//			channelSftp.chmod(Integer.parseInt("8"), upload_path);
			//			channelSftp.mkdir(upload_path);
			channelSftp.cd(channelSftp.pwd());
			System.out.println("localFilePath..."+localFilePath);
			File f = new File(System.getProperty("user.dir")+localFilePath);

			System.out.println("Storing file as remote filename: "
					+ f.getName());

			FileInputStream ioStream=new FileInputStream(f);
			channelSftp.put(ioStream, f.getName());
			Thread.sleep(2000);

			ioStream.close();

			SftpATTRS fileAttributes=null;
			fileAttributes=channel.lstat(upload_path + "/" + f.getName());

			System.out.println("fileAttributes..."+fileAttributes.getSize());

			if(fileAttributes !=null && fileAttributes.getSize() != 0)
			{
				result_text = result_text + "Upload Sucess"; // returns to FM

			} else {

				result_text = result_text + "Upload fail"; // returns to FM

			}



		} catch (JSchException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SftpException e) {
			System.out.println(e.id);
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		} 
		finally {

			if(channelSftp != null && channelSftp.isConnected())channelSftp.exit();
			if(channelSftp != null && channelSftp.isConnected()) channelSftp.disconnect();
			if(session != null && session.isConnected()) session.disconnect();
		}

		System.out.println("Channel is connected? " + channelSftp.isConnected()); // returns false as i would expect
		System.out.println("Channel is closed? " + channelSftp.isClosed()); // returns true as i would expect

		return result_text;

	}



	// getting ssh key file path of jenkins server

	/**
	 * @param userName
	 * @return
	 */
	public static String getRsaPath(String userName) {
		if (userName.equalsIgnoreCase("madmin")) {
			return "-i /var/jenkins/.ssh/id_rsa.tester ";
		} else {
			return "";
		}
	}
	//get the ssh key file path of local server
	/**
	 * @return
	 */
	public static String getRsaLocalRsaPath() {

		return "-i /Users/mallesh/.ssh/id_rsa ";
	}
	/**
	 * scp Transfer through Runtime
	 * @param filePath
	 * @param fileServerUsername
	 * @param server
	 * @param serverLocPath
	 * @return
	 */
	public static String scpTranfer(String filePath, String fileServerUsername, String server, String serverLocPath) {
		String consoleLog = "";
		System.out.println("filePath" + filePath);
		Runtime rt = Runtime.getRuntime();
		Process proc;
		String cmdSCP = "scp " + getRsaPath("madmin") + System.getProperty("user.dir")+filePath + " " + fileServerUsername + "@" + server + ":"
				+ serverLocPath;
		try {
			System.out.println("scp CMD: " + cmdSCP);
			proc = rt.exec(cmdSCP);
			proc.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";

			while ((line = br.readLine()) != null) {
				System.out.println("scp upload sucess" + line);
				consoleLog += line;
			}

			br.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return consoleLog;
	}


	/**
	 * Scp Upload through Shell  script	
	 * @param privateKeyPath
	 * @param fileTobeUploaded
	 * @param username
	 * @param fileServer
	 * @param uploaded_Path
	 */

	public void uploadLogFileThrSCPUsingShellScript(String privateKeyPath, String fileTobeUploaded, String username,
			String fileServer, String uploaded_Path) {
		try {
			String arg1 = privateKeyPath;
			String arg2 = fileTobeUploaded + " ";
			String arg3 = username;
			String arg4 = fileServer;
			String arg5 = uploaded_Path;

			ProcessBuilder pb = new ProcessBuilder("src/test/resources/Audit/scpuploadparams.sh", arg1, arg2, arg3,
					arg4, arg5);
			Process proc = pb.start();

			BufferedReader read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			System.out.println("outside the loop");
			try {
				proc.waitFor();
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			while (read.ready()) {
				System.out.println("inside read.ready()");
				System.out.println("test" + read.readLine());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * scp/sftp upload other approach	
	 * @param privateKeyPath
	 * @param fileTobeUploaded
	 * @param username
	 * @param fileServer
	 * @param uploaded_Path
	 * @throws Exception
	 */

	public void uploadLogFileThrSCPChannel(String privateKeyPath, String fileTobeUploaded, String username,
			String fileServer, String uploaded_Path) throws Exception {


		// upload firewall log through scp channel; below code execute scp
		// command through java
		File privateKeyFile = new File(privateKeyPath);
		File uploadedFile = new File(fileTobeUploaded);
		String str[] = new String[] { "/bin/sh", "-c", "scp -i  ", privateKeyFile + "", uploadedFile + "",
				username + "@" + fileServer + ":" + uploaded_Path };
		Process p = Runtime.getRuntime().exec(str);
		int exitVal = p.waitFor();
		System.out.println("Process exitValue: " + exitVal);
		System.out.println("...." + p.exitValue());
		System.out.println("Sending  complete...");
	}	


	public static Properties getAuditFireWallData(String fireWallLogDataFilePath) throws Exception
	{
		Properties fireWallLogsDataProps=new Properties();
		fireWallLogsDataProps.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(fireWallLogDataFilePath)));
		return fireWallLogsDataProps;

	}



	/**
	 * The below method will validate Audit summary data
	 * if the golden set data number is equal to Api summary data- Simply call AssertEquals
	 * else
	 *  apply 10% of +/- on golden set data value that means
	 *  (ex: in case of services, suppose golden data is 30 and api returning 34 so 10% of 30 is [25 to 35] and logic allow the service number from 25 to 35) otherwise it is bug.
	 *  (ex: in case of number is less than 5 applying 20% of +/- on golden set data)
	 * @param props
	 * @param summaryObject
	 * @param firewaltype
	 * @throws Exception
	 */

	public static void validateAuditSummaryByJson(Properties props,JSONObject summaryObject, String firewaltype) throws Exception
	{

		String goldenSetLabel;
		String summaryLabel;

		//audit score verification
		JSONObject companyBrr = summaryObject.getJSONObject("company_brr");
		goldenSetLabel="audit_score";
		summaryLabel="value";

		if((String)props.get(goldenSetLabel)==(companyBrr.getString(summaryLabel))){
			Assert.assertEquals((String)props.get(goldenSetLabel), companyBrr.getString(summaryLabel),firewaltype+" Company Audit score should be  ");
		}
		else{
			int auditScoreRageMinValue=minusPercentValue(Integer.parseInt((String)props.get(goldenSetLabel)),AuditTestConstants.AUDIT_PERCENTAGE_10);
			int auditScoreRageMaxValue=plusPercentValue(Integer.parseInt((String)props.get(goldenSetLabel)),AuditTestConstants.AUDIT_PERCENTAGE_10);
			boolean auditScoreflag=checkScoreInBetweenPercentageRange(auditScoreRageMinValue,auditScoreRageMaxValue,Integer.parseInt(companyBrr.getString(summaryLabel)));
			Assert.assertTrue(auditScoreflag, firewaltype+" Company Audit score value is between or equal to values  ["+auditScoreRageMinValue+","+auditScoreRageMaxValue+"] but Company Audit score found ["+Integer.parseInt(companyBrr.getString(summaryLabel))+"]");
		}


		//allowed_services verification 
		goldenSetLabel="allowed_services";
		summaryLabel="total_services";
		if((String)props.get(goldenSetLabel)==summaryObject.getString(summaryLabel)){
			Assert.assertEquals((String)props.get(goldenSetLabel), summaryObject.getString(summaryLabel),firewaltype+" total_services should be ");//total_services
		}else{
			validateSummaryInfo((String)props.get(goldenSetLabel),summaryObject.getString(summaryLabel),firewaltype,summaryLabel);
		}


		//allowed_users
		goldenSetLabel="allowed_users";
		summaryLabel="total_users";
		if((String)props.get(goldenSetLabel)==summaryObject.getString(summaryLabel)){
			Assert.assertEquals((String)props.get(goldenSetLabel), summaryObject.getString(summaryLabel),firewaltype+" total_users should be ");//total_users

		}else{
			validateSummaryInfo((String)props.get(goldenSetLabel),summaryObject.getString(summaryLabel),firewaltype,summaryLabel);
		}


		//allowed_destinations
		goldenSetLabel="allowed_destinations";
		summaryLabel="total_destinations";
		if((String)props.get(goldenSetLabel)==summaryObject.getString(summaryLabel)){
			Assert.assertEquals((String)props.get(goldenSetLabel), summaryObject.getString(summaryLabel),firewaltype+" TotalDestinations should be ");//total_destinations

		}else{ validateSummaryInfo((String)props.get(goldenSetLabel),summaryObject.getString(summaryLabel),firewaltype,summaryLabel);
		}


		//Medium Risky Services
		goldenSetLabel="med_Risky_services";
		summaryLabel="med_risk_services";
		if((String)props.get(goldenSetLabel)==summaryObject.getString(summaryLabel)){
			Assert.assertEquals((String)props.get(goldenSetLabel), summaryObject.getString(summaryLabel),firewaltype+" med_risk_services should be ");//med_risk_services

		}else{ validateSummaryInfo((String)props.get(goldenSetLabel),summaryObject.getString(summaryLabel),firewaltype,summaryLabel);
		}


		//high_risky_services
		if(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS.equals(firewaltype))
		{
			goldenSetLabel="high_risky_services";
			summaryLabel="high_risk_services";
			if((String)props.get(goldenSetLabel)==summaryObject.getString(summaryLabel)){
				Assert.assertEquals((String)props.get(goldenSetLabel), summaryObject.getString(summaryLabel),firewaltype+" high_risk_services should be ");//high_risk_services

			}else{ validateSummaryInfo((String)props.get(goldenSetLabel),summaryObject.getString(summaryLabel),firewaltype,summaryLabel);

			}

		}

	}

	public static void validateDeniedAuditSummaryInfo(Properties props,JSONObject summaryObject, String firewaltype) throws Exception
	{

		Properties serviceNamesProps;
		//service sessions
		//golden set data preparation for the services
		serviceNamesProps=AuditTestUtils.getAuditFireWallData(AuditTestConstants.FIREWALL_SERVICENAMES);

		// top_risky_services
		Map<String,String> serviceSessionsMap=null;
		int topRiskyServicesArrayLength=0;
		int topUsedServicesArrayLength=0;
		String serviceId=null;
		String userCount=null;

		if (summaryObject.has("top_risky_services") && !summaryObject.isNull("top_risky_services")) {
			JSONArray topRiskyServicesArray = summaryObject.getJSONArray("top_risky_services");

			topRiskyServicesArrayLength =topRiskyServicesArray.length();


			for(int i=0; i<topRiskyServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topRiskyServicesArray.get(i)).getString("service_id");
				userCount=((JSONObject)topRiskyServicesArray.get(i)).getString("users_count");

				serviceSessionsMap= setServiceSessionMap( serviceNamesProps, serviceId, userCount,serviceSessionsMap);

			}
		}

		//top_used_services
		if (summaryObject.has("top_used_services") && !summaryObject.isNull("top_used_services")) {
			JSONArray topUsedArray = summaryObject.getJSONArray("top_used_services");

			topUsedServicesArrayLength=topUsedArray.length();
			for(int i=0; i<topUsedServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topUsedArray.get(i)).getString("service_id");
				userCount=((JSONObject)topUsedArray.get(i)).getString("users_count");

				serviceSessionsMap= setServiceSessionMap( serviceNamesProps, serviceId, userCount,serviceSessionsMap);

			}
		}


		System.out.println("service session object..."+serviceSessionsMap);
		Map<String,String>goldenSetDataMap=new HashMap<String,String>();
		for(String key : props.stringPropertyNames()) {

			if(key.contains("blocked"))
			{
				goldenSetDataMap.put(key, (String)props.get(key));
			}

		}
		System.out.println("actualServiceSessionsMap..."+goldenSetDataMap);
		if(!goldenSetDataMap.isEmpty() && !serviceSessionsMap.isEmpty())
			Assert.assertTrue(equalMaps(goldenSetDataMap,serviceSessionsMap),"blocked services not verified");

	}


	public static boolean equalMaps(Map<String, String> m1, Map<String, String> m2) {
		if (m1.size() != m2.size())
			return false;
		for (String key: m1.keySet())
			if (!m1.get(key).equals(m2.get(key)))
				return false;
		return true;
	}

	private static Map<String,String> setServiceSessionMap(Properties serviceNamesProps,String serviceId,String userCount,Map<String,String> serviceSessions) throws Exception
	{
		//Map<String,String> serviceSessionsNew=null;
		if(serviceSessions==null){
			serviceSessions=new HashMap<String,String>();}
		Set<Object> keys =serviceNamesProps.keySet();
		if(keys.contains(serviceId)){
			serviceSessions.put("blocked_"+(String)serviceNamesProps.get(serviceId), userCount);

		}

		return serviceSessions;
	}


	public static void validateSummaryInfo(String goldenSetDataField,String summaryValue,String firewallType, String summaryLable) throws Exception
	{
		int minValue=0;
		int maxValue=0;
		minValue=minusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_10);
		maxValue=plusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_10);
		boolean flag=false;
		if(minValue==maxValue){
			minValue=minusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_20);
			maxValue=plusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_20);
			flag=checkScoreInBetweenPercentageRange(minValue,maxValue,Integer.parseInt(summaryValue));
			Assert.assertTrue(flag, firewallType+" "+summaryLable+" value is between or equal to values ["+minValue+","+maxValue+"] but Actual "+ summaryLable+" found ["+Integer.parseInt(summaryValue)+"]");
		}else{
			flag=checkScoreInBetweenPercentageRange(minValue,maxValue,Integer.parseInt(summaryValue));
			Assert.assertTrue(flag, firewallType+" "+summaryLable+" value is between or equal to values ["+minValue+","+maxValue+"] but Actual "+ summaryLable+" found ["+Integer.parseInt(summaryValue)+"]");

		}
	}

	public static void validateSummaryInfoNew(String goldenSetDataField,String summaryValue,String firewallType, String summaryLable,ArrayList<String> goldenSetErrorList) throws Exception
	{
		int minValue=0;
		int maxValue=0;
		minValue=minusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_10);
		maxValue=plusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_10);
		boolean flag=false;
		if(minValue==maxValue){
			minValue=minusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_20);
			maxValue=plusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_20);
			flag=checkScoreInBetweenPercentageRange(minValue,maxValue,Integer.parseInt(summaryValue));
			String str=  firewallType+" "+summaryLable+" value is between or equal to values ["+minValue+","+maxValue+"] but Actual "+ summaryLable+" found ["+Integer.parseInt(summaryValue)+"]";
			if(!flag)
				goldenSetErrorList.add(str);
			else	
				Assert.assertTrue(flag, str);

		}else{
			flag=checkScoreInBetweenPercentageRange(minValue,maxValue,Integer.parseInt(summaryValue));
			String str=firewallType+" "+summaryLable+" value is between or equal to values ["+minValue+","+maxValue+"] but Actual "+ summaryLable+" found ["+Integer.parseInt(summaryValue)+"]";
			if(!flag)
				goldenSetErrorList.add(str);
			else	
				Assert.assertTrue(flag, str);

		}
	}
	public static int minusPercentValue(int goldenSetData,int percentage)
	{
		return (goldenSetData-(goldenSetData * percentage / 100));
	}
	public static int plusPercentValue(int goldenSetData,int percentage)
	{
		return  ((goldenSetData * percentage / 100)+goldenSetData);
	}

	public static boolean checkScoreInBetweenPercentageRange(int minusPercentValue, int plusPercentValue,int auditSummaryValue) throws Exception 
	{
		boolean flag=false; 

		if(auditSummaryValue>=minusPercentValue && auditSummaryValue<=plusPercentValue) // check that number between two ranges
		{
			flag=true;
		} 

		else{
			flag=false;
		}
		return flag;


	}

	//golden set data validations based on xl sheet
	public static  ArrayList<String> auditSummaryGoldenSetData(Map<String, ArrayList<Cell>> knownGoodMap,JSONObject summaryObject, String firewallType,ArrayList<String> goldenSetErrorList) throws Exception
	{

		Reporter.log("*******************GoldenSet Data Validations for "+firewallType+": *********************",true);
		Reporter.log("Expected GoldenSet Data for "+firewallType+ "::"+knownGoodMap,true);



		// ArrayList<String> goldenSetErrorList =new ArrayList<String>();

		String summaryLabel=null;


		//Audit Score validations
		String expected_audit_score=goldenSetSingleData(knownGoodMap,AuditTestConstants.GOLDENSETDATA_AUDIT_SCORE);
		JSONObject companyBrr = summaryObject.getJSONObject("company_brr");
		String actual_audit_score= companyBrr.getString("value"); 
		if(Integer.parseInt(actual_audit_score)==Integer.parseInt(expected_audit_score)){ //Asserting audit score if both the scores are equal 
			Assert.assertEquals(actual_audit_score,expected_audit_score, firewallType+" Company Audit score should be  ");


		}
		else{ //if audit_score is not matching applying +/- 10% (ex: Goldenset AScore=75, api  audit score should be expecting between or equal to [70,80] i.e., +/-10%
			int auditScoreRageMinValue=minusPercentValue(Integer.parseInt(expected_audit_score),AuditTestConstants.AUDIT_PERCENTAGE_10);
			int auditScoreRageMaxValue=plusPercentValue(Integer.parseInt(expected_audit_score),AuditTestConstants.AUDIT_PERCENTAGE_10);
			boolean auditScoreflag=checkScoreInBetweenPercentageRange(auditScoreRageMinValue,auditScoreRageMaxValue,Integer.parseInt(actual_audit_score));
			String str=firewallType+" Company Audit score value is between or equal to values  ["+auditScoreRageMinValue+","+auditScoreRageMaxValue+"] but Actual Company Audit score found ["+Integer.parseInt(actual_audit_score)+"]";
			if(!auditScoreflag)
				goldenSetErrorList.add(str);
			else
				Assert.assertTrue(auditScoreflag, str);

		}


		//Audit saas services validation	
		String expected_audit_saasservices=goldenSetSingleData(knownGoodMap,AuditTestConstants.GOLDENSETDATA_AUDIT_SERVICES);
		summaryLabel="total_services";
		String actual_audit_saasservices=summaryObject.getString(summaryLabel);
		if(Integer.parseInt(actual_audit_saasservices)==Integer.parseInt(expected_audit_saasservices)){
			Assert.assertEquals(actual_audit_saasservices, expected_audit_saasservices,firewallType+" total_services should be ");//total_services
		}else{
			validateSummaryInfoNew(expected_audit_saasservices,actual_audit_saasservices,firewallType,summaryLabel,goldenSetErrorList);
		}



		//Audit users validation	
		String expected_audit_users=goldenSetSingleData(knownGoodMap,AuditTestConstants.GOLDENSETDATA_AUDIT_USERS);
		summaryLabel="total_users";
		String actual_audit_users=summaryObject.getString(summaryLabel);
		if(Integer.parseInt(actual_audit_users)==Integer.parseInt(expected_audit_users)){
			Assert.assertEquals(actual_audit_users, expected_audit_users,firewallType+" total_users should be ");//total_users

		}else{
			validateSummaryInfoNew(expected_audit_users,actual_audit_users,firewallType,summaryLabel,goldenSetErrorList);
		}



		//Audit destinations validation	
		String expected_audit_destinations=goldenSetSingleData(knownGoodMap,AuditTestConstants.GOLDENSETDATA_AUDIT_DESTINATIONS);
		summaryLabel="total_destinations";
		String actual_audit_destinations=summaryObject.getString(summaryLabel);
		if(Integer.parseInt(actual_audit_destinations)==Integer.parseInt(expected_audit_destinations)){
			Assert.assertEquals(actual_audit_destinations, expected_audit_destinations,firewallType+" TotalDestinations should be ");//TotalDestinations

		}else{
			validateSummaryInfoNew(expected_audit_destinations,actual_audit_destinations,firewallType,summaryLabel,goldenSetErrorList);
		}

		//Audit TopRisky Services Validation
		Map<String,String> expectedTopRiskyServicesMap=new HashMap<String, String>();
		Map<String,String> actualTopRiskyServicesMap=new HashMap<String, String>();
		int topRiskyServicesArrayLength=0;
		int topUsedServicesArrayLength=0;
		String serviceId=null;
		String userCount=null;
		String serviceName=null;
		String top_risky_serviceBRR=null;
		String label="top_risky_services";

		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topRiskyServicesArray = summaryObject.getJSONArray(label);
			topRiskyServicesArrayLength =topRiskyServicesArray.length();


			for(int i=0; i<topRiskyServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topRiskyServicesArray.get(i)).getString("service_id");
				userCount=((JSONObject)topRiskyServicesArray.get(i)).getString("users_count");
				serviceName=getServiceName(serviceId);
				top_risky_serviceBRR=((JSONObject)topRiskyServicesArray.get(i)).getString("sort_key_brr");
				actualTopRiskyServicesMap.put(serviceName, top_risky_serviceBRR);

			}
		}
		//actual map
		expectedTopRiskyServicesMap=goldenSetServicesData(knownGoodMap, AuditTestConstants.GOLDENSETDATA_AUDIT_TOP_RISKY_SERVICES) ;
		Reporter.log("actualMap for TopRiskyServices..."+actualTopRiskyServicesMap,true);
		Reporter.log("expectedMap for TopRiskyServices..."+expectedTopRiskyServicesMap,true);



		// Assert.assertTrue(equalityMap(actualTopRiskyServicesMap,expectedTopRiskyServicesMap,firewallType,label),"blocked services not verified");
		goldenSetErrorList=equalityMap(expectedTopRiskyServicesMap,actualTopRiskyServicesMap,firewallType,label,goldenSetErrorList);

		//end of TopRiskyServices



		//Audit TopUsed Services
		Map<String,String> expectedTopUsedServicesMap=new HashMap<String, String>();
		Map<String,String> actualTopUsedServicesMap=new HashMap<String, String>();
		label="top_used_services";

		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topUsedServicesArray = summaryObject.getJSONArray(label);
			topUsedServicesArrayLength =topUsedServicesArray.length();

			for(int i=0; i<topUsedServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topUsedServicesArray.get(i)).getString("service_id");
				userCount=((JSONObject)topUsedServicesArray.get(i)).getString("users_count");
				serviceName=getServiceName(serviceId);
				actualTopUsedServicesMap.put(serviceName, "100");
			}

		}
		expectedTopUsedServicesMap=goldenSetServicesData(knownGoodMap, AuditTestConstants.GOLDENSETDATA_AUDIT_TOP_USED_SERVICES) ;
		Reporter.log("actualMap for top_used_services..."+actualTopUsedServicesMap,true);
		Reporter.log("expectedMp for top_used_services..."+expectedTopUsedServicesMap,true);

		goldenSetErrorList=equalityMap(expectedTopUsedServicesMap,actualTopUsedServicesMap,firewallType,label,goldenSetErrorList);
		//end of top_used_services

		//high risky services
		label="high_risk_services";
		String expected_audit_high_risky_services=goldenSetSingleData(knownGoodMap,AuditTestConstants.GOLDENSETDATA_AUDIT_HIGH_RISKY_SERVICES);
		String actual_audit_high_risky_services=summaryObject.getString(label);
		if(actual_audit_high_risky_services==expected_audit_high_risky_services){
			Assert.assertEquals(actual_audit_high_risky_services, expected_audit_high_risky_services,firewallType+" high_risk_services should be ");//high_risk_services
		}else{
			//goldenSetErrorList.add(firewallType+"::"+label+" high_risk_services should be:"+actual_audit_high_risky_services+" but found:"+expected_audit_high_risky_services);
			validateSummaryInfoNew(expected_audit_high_risky_services,actual_audit_high_risky_services,firewallType,label,goldenSetErrorList);

		}

		//medum risky services
		label="med_risk_services";
		String expected_audit_med_risk_services=goldenSetSingleData(knownGoodMap,AuditTestConstants.GOLDENSETDATA_AUDIT_MED_RISKY_SERVICES);
		String actual_audit_med_risk_services=summaryObject.getString(label);
		if(actual_audit_med_risk_services==expected_audit_med_risk_services){
			Assert.assertEquals(actual_audit_med_risk_services, expected_audit_med_risk_services,firewallType+" med_risk_services should be ");//med_risk_services
		}else{
			//goldenSetErrorList.add(firewallType+"::"+label+" med_risk_services should be:"+actual_audit_med_risk_services+" but found:"+expected_audit_med_risk_services);
			validateSummaryInfoNew(expected_audit_med_risk_services,actual_audit_med_risk_services,firewallType,label,goldenSetErrorList);

		}

		//low risky services

		//destinations

		//expected map
		return goldenSetErrorList;

	}

	public static ArrayList<String> equalityMap(Map<String, String> expectedMapm1, Map<String, String> m2,String firewallType,String colname,ArrayList<String>goldenSetErrorList) throws Exception{
		boolean flag=false;
		String str=null;


		if (expectedMapm1.size() != m2.size()){
			str=firewallType+"::"+colname+" size is not matching: "+"Expected GoldenSetData "+colname+" size is: "+expectedMapm1.size()+" but found "+m2.size();
			goldenSetErrorList.add(str);

			//Assert.assertEquals(m1.size(), m2.size(),firewallType+"::"+colname+" size is not matching: "+"GoldenSetData "+colname+ " expected");

			flag=false;//making sure below for loop will not call if size is not matching

		}//ReadyTalk=100
		else{
			for (String key: expectedMapm1.keySet()){
				//str= firewallType+"::"+colname+" key: "+key+" Actual BRR:"+m1.get(key)+"  Expected BRR:"+m2.get(key);
				//Reporter.log(str,true);
				if (!expectedMapm1.get(key).equals(m2.get(key)))
				{

					int auditBRRRageMinValue=minusPercentValue(Integer.parseInt(expectedMapm1.get(key)),AuditTestConstants.AUDIT_PERCENTAGE_10);
					int auditBRRRageMaxValue=plusPercentValue(Integer.parseInt(expectedMapm1.get(key)),AuditTestConstants.AUDIT_PERCENTAGE_10);

					if(m2.get(key)==null){
						str=firewallType+"::"+colname+" Expected Service "+key+" not found";
						goldenSetErrorList.add(str);
					}
					else{

						boolean auditBRRflag=checkScoreInBetweenPercentageRange(auditBRRRageMinValue,auditBRRRageMaxValue,Integer.parseInt(m2.get(key)));
						//Assert.assertTrue(auditScoreflag, firewallType+" "+colname+" BRR value is between or equal to values  ["+auditBRRRageMinValue+","+auditBRRRageMaxValue+"] but "+colname+" BRR found ["+Integer.parseInt(m2.get(key))+"]");
						if(!auditBRRflag){
							str=firewallType+" "+colname+" "+key+" Expected BRR value is between or equal to values  ["+auditBRRRageMinValue+","+auditBRRRageMaxValue+"] but Actual "+colname+" BRR found ["+Integer.parseInt(m2.get(key))+"]";
							goldenSetErrorList.add(str);
						}}

					flag=false;
					//goldenSetErrorList.add(str);

				}

			}
		}

		return goldenSetErrorList;
	}

	/*public static boolean equalityMap(Map<String, String> m1, Map<String, String> m2,String firewallType,String colname,ArrayList<String>goldenSetErrorList) {
			boolean flag=false;

				if (m1.size() != m2.size()){
					Assert.assertEquals(m1.size(), m2.size(),firewallType+"::"+colname+" size is not matching: "+"GoldenSetData "+colname+ " expected");

					return flag;
				}
				for (String key: m1.keySet()){
					String str= firewallType+"::"+colname+" key: "+key+" Actual BRR:"+m1.get(key)+"  Expected BRR:"+m2.get(key);
					Reporter.log(str,true);
					if (!m1.get(key).equals(m2.get(key)))
					{
						flag=false;
						goldenSetErrorList.add(str+"\n");
						Assert.assertTrue(flag, str);

					}

				}

				return flag;
			}*/
	public static String getServiceName(String serviceId) throws Exception
	{
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();	

		queryParam.add(new BasicNameValuePair("service_id", serviceId));
		queryParam.add(new BasicNameValuePair("format", "json"));

		HttpResponse response  = AuditFunctions.getServiceDetails(new Client(), queryParam);

		JSONObject serviceObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);	
		return serviceObject.getString("service_name");

	}

	
	public static String serviceIsResearch(String serviceId) throws Exception
	{
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();	

		queryParam.add(new BasicNameValuePair("service_id", serviceId));
		queryParam.add(new BasicNameValuePair("format", "json"));

		HttpResponse response  = AuditFunctions.getServiceDetails(new Client(), queryParam);

		JSONObject serviceObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		JSONObject question_answersJsonObj = serviceObject.getJSONObject("question_answers");
		
		return question_answersJsonObj.getString("research_completed");
	}
	public static ServiceObject serviceDetails(String serviceId) throws Exception
	{
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();	

		queryParam.add(new BasicNameValuePair("service_id", serviceId));
		queryParam.add(new BasicNameValuePair("format", "json"));

		HttpResponse response  = AuditFunctions.getServiceDetails(new Client(), queryParam);

		JSONObject serviceObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		ServiceObject service= new ServiceObject();
		service.setServiceName(serviceObject.getString("service_name"));
		service.setServiceCategory(serviceObject.getString("service_category"));
		
		JSONObject question_answersJsonObj = serviceObject.getJSONObject("question_answers");
		service.setServiceResearchCompleted(question_answersJsonObj.getString("research_completed"));
		service.setServiceUrl(question_answersJsonObj.getString("87"));
		
		
		return service;
	}
	
	public static Map<String, String> tenantServicesMap(String dsid) throws Exception
	{
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();	
		queryParam.add(new BasicNameValuePair("ds_id", dsid));
		
		HttpResponse tenantServicesHttpResp = AuditFunctions.getTenantServices(new Client(),queryParam);
		Assert.assertEquals(tenantServicesHttpResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		JSONArray jsonTenantServicesObj = (JSONArray) new JSONObject(ClientUtil.getResponseBody(tenantServicesHttpResp)).getJSONArray("objects");
		Map<String,String> tenantServicesMap=null;
		Map<String,String> finalTenantServicesMap=new HashMap<String, String>();
		Reporter.log("tenant services length...."+jsonTenantServicesObj.length(),true);
		
		for(int i=0; i<jsonTenantServicesObj.length(); i++)
		{
			tenantServicesMap = new HashMap<String, String>();
			tenantServicesMap=parse(jsonTenantServicesObj.getJSONObject(i).getJSONObject("services"),tenantServicesMap);
			finalTenantServicesMap.putAll(tenantServicesMap);
		}
		
		Reporter.log("finalTenantServicesMap before::...."+finalTenantServicesMap,true);
		
		
		
		return finalTenantServicesMap;
	}
	
	

	public static String goldenSetSingleData(Map<String, ArrayList<Cell>> knownGoodMap,String field){
		Double d=knownGoodMap.get(field).get(0).getNumericCellValue();
		return new Integer(d.intValue()).toString();

	}

	public static Map<String,String> goldenSetServicesData(Map<String, ArrayList<Cell>> knownGoodMap,String serviceType) 
	{

		Map<String,String> goldenSetServicesMap = new HashMap<String,String>();               
		for(Cell c:knownGoodMap.get(serviceType)){
			String[] entry=c.getStringCellValue().split("=");
			goldenSetServicesMap.put(entry[0].trim(), entry[1].trim());
		}
		//System.out.println("goldenSetTopRiskyServicesMap.."+goldenSetTopRiskyServicesMap);
		return goldenSetServicesMap;
	}	  




	public static void validateAuditSummary(Properties props,com.elastica.beatle.AuditSummaryDataObjects.Object summaryObject, String firewaltype)
	{
		//Validate main summary objects


		// Integer totalservices=(Integer) props.get("allowed_services");
		Assert.assertEquals((String)props.get("datasourceid"), summaryObject.getDatasourceId(),firewaltype+" datasource id should be ");//datasourceid validation
		Assert.assertEquals("1mo", summaryObject.getDateRange(),firewaltype+" daterange should be 1 month");//daterange validation
		Assert.assertEquals((String)props.get("allowed_services"), summaryObject.getTotalServices().toString(),firewaltype+" total_services should be ");//total_services
		Assert.assertEquals((String)props.get("allowed_users"), summaryObject.getTotalUsers().toString(),firewaltype+" total_users should be ");//total_users
		Assert.assertEquals((String)props.get("allowed_destinations"), summaryObject.getTotalDestinations().toString(),firewaltype+" TotalDestinations should be ");//total_destinations
		Assert.assertEquals((String)props.get("med_Risky_services"), summaryObject.getMedRiskServices().toString(),firewaltype+" med_risk_services should be ");//med_risk_services
		if( ("pansys".equals(summaryObject)) ){
			Assert.assertEquals((String)props.get("high_risky_services"), summaryObject.getHighRiskServices().toString(),firewaltype+" high_risk_services should be ");//high_risk_services
		}
		Assert.assertNotNull((String)summaryObject.getResourceUri(), "Resource URI is null");


		//validating Audit score
		CompanyBrr  companyAuditScoreObject= summaryObject.getCompanyBrr() ;
		Assert.assertEquals((String)props.get("audit_score"), companyAuditScoreObject.getValue().toString(),firewaltype+" Company Audit score should be  ");

		//validating top risky services
		List<TopRiskyService> topRiskyServices=summaryObject.getTopRiskyServices();
		Integer top_risky_serviceId;
		Integer top_risky_serviceBrr;
		Integer top_risky_UsersCount;
		List<TopUser> topUsersListForTopRiskyServices;
		Integer top_user_sessions;
		Integer top_user_id;
		Integer top_user_traffic;

		for(TopRiskyService trs:topRiskyServices)
		{
			top_risky_serviceId=trs.getServiceId();
			top_risky_serviceBrr=trs.getSortKeyBrr();
			top_risky_UsersCount=trs.getUsersCount();

			//TopUsers of TopRisky services
			topUsersListForTopRiskyServices=trs.getTopUsers();
			for(TopUser topUser:topUsersListForTopRiskyServices)
			{
				top_user_id=topUser.getUserId();
				top_user_sessions=topUser.getSessions();
				top_user_traffic=topUser.getTraffic();
			}

		}


		//validating top risky services

		List<TopUsedService> topUsedServices=summaryObject.getTopUsedServices();
		Integer top_used_serviceId;
		//Integer top_used_serviceBrr;
		Integer top_used_UsersCount;
		List<TopUser_> topUsersListForTopUsedServices;

		for(TopUsedService tus:topUsedServices){

			top_used_serviceId=tus.getServiceId();
			top_used_UsersCount=tus.getUsersCount();

			//TopUsers of TopUsedServices
			topUsersListForTopUsedServices=tus.getTopUsers();
			for(TopUser_ topUser_:topUsersListForTopUsedServices){

				top_user_id=topUser_.getUserId();
				top_user_sessions=topUser_.getSessions();
				top_user_traffic=topUser_.getTraffic();
			}


		}
	}


	/**
	 * This method will assert the  audit results should be >0	
	 * @param summaryObject
	 * @throws Exception
	 */
	public static void validateAuditSummaryForTheLogsHavingNoGoldenSetData(JSONObject summaryObject) throws Exception
	{
		Reporter.log(" callling validateAuditSummaryForTheLogsHavingNoGoldenSetData ..",true);

		String label=null;
		//company_brr
		label="company_brr";
		JSONObject companyBrr = summaryObject.getJSONObject(label);
		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			String expected_audit_score= companyBrr.getString("value"); 
			Assert.assertTrue(new Integer(expected_audit_score)>0,"Audit Score should be greater than 0 ");}

		//total services 
		label="total_services";
		Assert.assertTrue(new Integer(summaryObject.getString(label))>0,"total_services should be greater than 0 ");

		//total_users
		label="total_services";
		Assert.assertTrue(new Integer(summaryObject.getString(label))>0,"total_users should be greater than 0 ");

		//total_destinations
		label="total_services";
		Assert.assertTrue(new Integer(summaryObject.getString(label))>0,"total_destinations should be greater than 0 ");

		//total Risky services
		int topRiskyServicesArrayLength=0;
		String serviceId=null;
		String userCount=null;
		String serviceName=null;
		String top_risky_serviceBRR=null;
		label="top_risky_services";

		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topRiskyServicesArray = summaryObject.getJSONArray(label);
			topRiskyServicesArrayLength =topRiskyServicesArray.length();


			for(int i=0; i<topRiskyServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topRiskyServicesArray.get(i)).getString("service_id");
				userCount=((JSONObject)topRiskyServicesArray.get(i)).getString("users_count");
				serviceName=getServiceName(serviceId);
				top_risky_serviceBRR=((JSONObject)topRiskyServicesArray.get(i)).getString("sort_key_brr");
				Assert.assertTrue(new Integer(top_risky_serviceBRR)>0,"BRR rage should be greater than 0 ");

			}
		}

	}

	//compare two json objects
	public static boolean jsonsEqual(Object obj1, Object obj2) throws Exception

	{
		if (!obj1.getClass().equals(obj2.getClass()))
		{
			return false;
		}

		if (obj1 instanceof JSONObject)
		{
			JSONObject jsonObj1 = (JSONObject) obj1;

			JSONObject jsonObj2 = (JSONObject) obj2;

			String[] names = JSONObject.getNames(jsonObj1);
			String[] names2 = JSONObject.getNames(jsonObj1);
			if (names.length != names2.length)
			{
				return false;
			}

			for (String fieldName:names)
			{
				Object obj1FieldValue = jsonObj1.get(fieldName);

				Object obj2FieldValue = jsonObj2.get(fieldName);

				if (!jsonsEqual(obj1FieldValue, obj2FieldValue))
				{
					return false;
				}
			}
		}
		else if (obj1 instanceof JSONArray)
		{
			JSONArray obj1Array = (JSONArray) obj1;
			JSONArray obj2Array = (JSONArray) obj2;

			if (obj1Array.length() != obj2Array.length())
			{
				return false;
			}

			for (int i = 0; i < obj1Array.length(); i++)
			{
				boolean matchFound = false;

				for (int j = 0; j < obj2Array.length(); j++)
				{
					if (jsonsEqual(obj1Array.get(i), obj2Array.get(j)))
					{
						matchFound = true;
						break;
					}
				}

				if (!matchFound)
				{
					return false;
				}
			}
		}
		else
		{
			if (!obj1.equals(obj2))
			{
				return false;
			}
		}

		return true;
	}

	public static ArrayList<String> populateFirewallLogsListHavingNoGSData() {
		ArrayList<String> listFirewallLogs=new ArrayList<String>();
		//listFirewallLogs.add(AuditTestConstants.FIREWALL_BARRACUDA_ACTIVITYLOG);
		listFirewallLogs.add(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG);
		//listFirewallLogs.add(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES);
		listFirewallLogs.add(AuditTestConstants.FIREWALL_CISCO_WSA_W3C);
		//listFirewallLogs.add(AuditTestConstants.FIREWALL_JUNIPER_SCREENOS);
		//listFirewallLogs.add(AuditTestConstants.FIREWALL_SCANSAFE);

		return listFirewallLogs;


	}

	public static ArrayList<String> invalidLogs()
	{
		ArrayList<String> invalidLogsList=new ArrayList<String>();

		//empty logs
		invalidLogsList.add(AuditTestConstants.FIREWALL_WSA_ACCESS_ZIP);
		invalidLogsList.add(AuditTestConstants.FIREWALL_WSA_ACCESS_GZ);
		invalidLogsList.add(AuditTestConstants.FIREWALL_WSA_ACCESS_BZ2);

		//header only
		invalidLogsList.add(AuditTestConstants.FIREWALL_CWS_HEADERONLY_ZIP);
		invalidLogsList.add(AuditTestConstants.FIREWALL_CWS_HEADERONLY_GZ);
		invalidLogsList.add(AuditTestConstants.FIREWALL_CWS_HEADERONLY_BZ2);


		//image  logs
		invalidLogsList.add(AuditTestConstants.FIREWALL_CISCO_ASA_IMG_ZIP);
		invalidLogsList.add(AuditTestConstants.FIREWALL_CISCO_ASA_IMG_GZ);
		invalidLogsList.add(AuditTestConstants.FIREWALL_CISCO_ASA_IMG_BZ2);

		//exe logs
		invalidLogsList.add(AuditTestConstants.FIREWALL_PAN_CSV_EXE_ZIP);
		invalidLogsList.add(AuditTestConstants.FIREWALL_PAN_CSV_EXE_GZ);
		invalidLogsList.add(AuditTestConstants.FIREWALL_PAN_CSV_EXE_BZ2);

		return invalidLogsList;
	}

	public static AuditDSStatusDTO populateInCompleteDataSources(JSONObject dataSourceObject) throws Exception {
		//Reporter.log("dataSourceObject::populateInCompleteDataSources"+dataSourceObject,true);
		AuditDSStatusDTO auditDSStatusDTO= new AuditDSStatusDTO();
		auditDSStatusDTO.setDsId((String) dataSourceObject.get("id"));
		auditDSStatusDTO.setDsName((String) dataSourceObject.get("name"));
		auditDSStatusDTO.setDsType((String) dataSourceObject.get("type"));
		auditDSStatusDTO.setDsLogTransport((String) dataSourceObject.get("log_transport"));
		auditDSStatusDTO.setDsLogFormat((String) dataSourceObject.get("log_format"));
		auditDSStatusDTO.setDsSetUpBy((String) dataSourceObject.get("setup_by"));
		auditDSStatusDTO.setDsLastDetectStatus((String) dataSourceObject.get("last_detect_status"));
		auditDSStatusDTO.setDsLastStatus((String) dataSourceObject.get("last_status"));
		auditDSStatusDTO.setDsLastStatusMsg((String) dataSourceObject.get("last_status_message"));

		return auditDSStatusDTO;


	}
	
	public static void updateTIAAttributes(boolean enabled, AttributeBean attributeBean,
			org.json.JSONArray getResponseArray, String[] ioi_Codes)
					throws JSONException, Exception, UnsupportedEncodingException, JAXBException {
		HttpResponse resp;
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);

			if (Arrays.asList(ioi_Codes).contains(((String) attributeObject.get(NAME)))) {
			
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				object.setEnabled(enabled);
				object.setConfidence(10);
				object.setImportance(attributeBean.getImportance());
				object.setThreshold(attributeBean.getThreshold());
				object.setWindow(attributeBean.getWindow());
				object.setResource_uri(uri);
				objects.add(object);
			}else{
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				object.setEnabled(false);
				object.setConfidence(10);
				object.setImportance(attributeBean.getImportance());
				object.setThreshold(attributeBean.getThreshold());
				object.setWindow(attributeBean.getWindow());
				object.setResource_uri(uri);
				objects.add(object);

			}
		}

		postAttributes( objects);
		Reporter.log("setting preferences waiting for 1 min",true);
		Thread.sleep(1* 60* 1000);
		Reporter.log("");
		
		
	}
	
	public static void updateBBIAttributes(boolean enabled, AttributeBean attributeBean,
			org.json.JSONArray getResponseArray, String[] ioi_Codes)
					throws JSONException, Exception, UnsupportedEncodingException {
		HttpResponse resp;
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			
			if (Arrays.asList(ioi_Codes).contains(((String) attributeObject.get(NAME)))) {
				
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				object.setEnabled(enabled);
				object.setConfidence(attributeBean.getConfidence());
				object.setImportance(attributeBean.getImportance());
				object.setResource_uri(uri);
				objects.add(object);
			}
			/*else{
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				object.setEnabled(false);
				object.setConfidence(attributeBean.getConfidence());
				object.setImportance(attributeBean.getImportance());
				object.setResource_uri(uri);
				objects.add(object);
			}*/
		}
		
		postAttributes(objects);
		Reporter.log("setting preferences waiting for 1 min",true);
		Thread.sleep(1* 45* 1000);
		
	}

	//TIA code
	public static void updateTIAAttributes(boolean enabled, AttributeBean attributeBean,
			org.json.JSONArray getResponseArray, String ioi_Code)
					throws JSONException, Exception, UnsupportedEncodingException, JAXBException {
		HttpResponse resp;
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);

			if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {

				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();

				object.setEnabled(enabled);
				object.setConfidence(10);
				object.setImportance(attributeBean.getImportance());
				object.setThreshold(attributeBean.getThreshold());
				object.setWindow(attributeBean.getWindow());
				object.setResource_uri(uri);
				objects.add(object);
			}else{
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				object.setEnabled(false);
				object.setConfidence(10);
				object.setImportance(attributeBean.getImportance());
				object.setThreshold(attributeBean.getThreshold());
				object.setWindow(attributeBean.getWindow());
				object.setResource_uri(uri);
				objects.add(object);

			}
		}

		postAttributes( objects);
		Reporter.log("setting preferences waiting for 1 min",true);
		Thread.sleep(1* 60* 1000);
		Reporter.log("");
		Reporter.log("updated preferences ::::::     threshold=" + attributeBean.getThreshold()  + ", window=" + attributeBean.getWindow() + ", importance=" + attributeBean.getImportance()
		+ ", enabled=" + enabled , true);
		Reporter.log("");

	}
	private static void postAttributes(List<Objects> objects)
			throws Exception, UnsupportedEncodingException, JAXBException {

		Reporter.log("posting updated preferences:: "+true,true);
		HttpResponse resp;
		AuditTIAAttributesDTO tiaAttributesDto = new AuditTIAAttributesDTO();
		tiaAttributesDto.setObjects(objects);
		StringEntity se = new StringEntity(marshall(tiaAttributesDto));
		Reporter.log("payload for posting updated preference::: "+marshall(tiaAttributesDto),true);

		HttpResponse resp1=AuditFunctions.setTIAAuditPreferences(new Client(),se);
		Assert.assertEquals(resp1.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
	}

	public static String marshall(final Object object) throws JAXBException {

		try {
			return marshallJSON(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String marshallJSON(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().withSerializationInclusion(Inclusion.NON_NULL); 
		AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		// make deserializer use JAXB annotations (only)
		mapper.setAnnotationIntrospector(introspector);
		// make serializer use JAXB annotations (only)
		mapper.setAnnotationIntrospector(introspector);
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, object);
		return writer.toString();
	}	

	public static ArrayList<String> validateSummary( String firewallType,HashMap<String,String> sernameNameWithServiceIdMap,
			AuditSummary expectedAuditSummary, AuditSummary actualAuditSummary,ArrayList<String> auditSummaryValidationsErrors) throws Exception
	{
		
		// verify service count mismatch from expected to Actual
		int expectedServicesSize = expectedAuditSummary.getTotalAuditServicesList().size();
		int actualServicesSize = actualAuditSummary.getTotalAuditServicesList().size();

		Collections.sort(expectedAuditSummary.getTotalAuditServicesList());
		Collections.sort(actualAuditSummary.getTotalAuditServicesList());

		Reporter.log(firewallType+" Expected Services List: "+expectedAuditSummary.getTotalAuditServicesList(),true);
		Reporter.log(firewallType+" Actual Services List: "+actualAuditSummary.getTotalAuditServicesList(),true);
		
		
		String strMsg = "";
		if (!(expectedServicesSize == actualServicesSize)) {
			strMsg = firewallType+" Services Count mismatch: expected Services Count [" + expectedServicesSize + "]" + " but found ["
					+ actualServicesSize + "]";
			auditSummaryValidationsErrors.add(strMsg);
		} else {
			// check the service exist in aparaji
			for (int service_i = 0; service_i < expectedAuditSummary.getTotalAuditServicesList().size(); service_i++) {
				String expectedservice = expectedAuditSummary.getTotalAuditServicesList().get(service_i);
				String actualService = ((String) actualAuditSummary.getTotalAuditServicesList().get(service_i));

				if (!sernameNameWithServiceIdMap.containsKey(expectedservice)) {
					strMsg = firewallType+" Service " + expectedservice + " not exist in the aparaji";
					auditSummaryValidationsErrors.add(strMsg);
				}
				if (sernameNameWithServiceIdMap.containsKey(expectedservice)
						&& !(actualAuditSummary.getTotalAuditServicesList().contains(expectedservice))) {
					strMsg = firewallType+" Service " + expectedservice + " is exist in aparaji and not detected in actual list";
					auditSummaryValidationsErrors.add(strMsg);
				}
				if (sernameNameWithServiceIdMap.containsKey(actualService)
						&& !(expectedAuditSummary.getTotalAuditServicesList().contains(actualService))) {
					strMsg = firewallType+" Service " + expectedservice
							+ " is exist in aparaji and actual list but missing in expected list"; // need
																									// to
																									// update
																									// the
																									// goldenset
																									// data
					auditSummaryValidationsErrors.add(strMsg);
				}

			}

		}

		Reporter.log( firewallType+" auditSummaryValidationsErrors before summary tests valiation: " + auditSummaryValidationsErrors,
				true);

		if (auditSummaryValidationsErrors.isEmpty()) {

			String summaryLabel = "";

			// Audit score validation
			String actual_audit_score = actualAuditSummary.getAuditScore();
			String expected_audit_score = String
					.valueOf(Math.round(Double.parseDouble(expectedAuditSummary.getAuditScore())));

			if (Integer.parseInt(actual_audit_score) == Integer.parseInt(expected_audit_score)) { // Asserting
																									// audit
																									// score
																									// if
																									// both
																									// the
																									// scores
																									// are
																									// equal
				Assert.assertEquals(actual_audit_score, expected_audit_score,
						firewallType + " Company Audit score should be  ");

			} else { // if audit_score is not matching applying +/- 10% (ex:
						// Goldenset AScore=75, api audit score should be
						// expecting between or equal to [70,80] i.e., +/-10%
				int auditScoreRageMinValue = minusPercentValue(Integer.parseInt(expected_audit_score),
						AuditTestConstants.AUDIT_PERCENTAGE_10);
				int auditScoreRageMaxValue = plusPercentValue(Integer.parseInt(expected_audit_score),
						AuditTestConstants.AUDIT_PERCENTAGE_10);
				boolean auditScoreflag = checkScoreInBetweenPercentageRange(auditScoreRageMinValue,
						auditScoreRageMaxValue, Integer.parseInt(actual_audit_score));
				String str = firewallType + " Company Audit score value is between or equal to values  ["
						+ auditScoreRageMinValue + "," + auditScoreRageMaxValue
						+ "] but Actual Company Audit score found [" + Integer.parseInt(actual_audit_score) + "]";
				if (!auditScoreflag)
					auditSummaryValidationsErrors.add(str);
				else
					Assert.assertTrue(auditScoreflag, str);

			}

			// Audit saas services validation
			String expected_audit_saasservices = String.valueOf(expectedAuditSummary.getSaas_services_count());
			String actual_audit_saasservices = String.valueOf(actualAuditSummary.getSaas_services_count());
			summaryLabel = "total_services";

			if (!(Integer.parseInt(actual_audit_saasservices) == Integer.parseInt(expected_audit_saasservices))) {
				auditSummaryValidationsErrors.add(firewallType + " " + summaryLabel + "expected  " + summaryLabel
						+ " are:  " + expected_audit_saasservices + " but found" + actual_audit_saasservices);

				// Assert.assertEquals(actual_audit_saasservices,
				// expected_audit_saasservices,firewallType+" total_services
				// should be ");//total_services
			} /*
				 * else{ validateSummaryInfoNew(expected_audit_saasservices,
				 * actual_audit_saasservices,firewallType,summaryLabel,
				 * auditSummaryValidationsErrors); }
				 */

			// Audit users validation
			String expected_audit_users = String.valueOf(expectedAuditSummary.getUsers_count());
			String actual_audit_users = String.valueOf(actualAuditSummary.getUsers_count());
			summaryLabel = "total_users";
			if (!(Integer.parseInt(actual_audit_users) == Integer.parseInt(expected_audit_users))) {
				auditSummaryValidationsErrors.add(firewallType + " " + summaryLabel + "expected  " + summaryLabel
						+ " are:  " + expected_audit_users + " but found" + actual_audit_users);

				// Assert.assertEquals(actual_audit_users,
				// expected_audit_users,firewallType+" total_users should be
				// ");//total_users

			} /*
				 * else{ validateSummaryInfoNew(expected_audit_users,
				 * actual_audit_users,firewallType,summaryLabel,
				 * auditSummaryValidationsErrors); }
				 */

			// Audit destinations validation
			String expected_audit_destinations = String.valueOf(expectedAuditSummary.getDestination_count());
			String actual_audit_destinations = String.valueOf(actualAuditSummary.getDestination_count());
			summaryLabel = "total_destinations";
			if (!(Integer.parseInt(actual_audit_destinations) == Integer.parseInt(expected_audit_destinations))) {
				//auditSummaryValidationsErrors.add(firewallType + " " + summaryLabel + "expected  " + summaryLabel
					//	+ " are:  " + expected_audit_destinations + " but found" + actual_audit_destinations);

				// Assert.assertEquals(actual_audit_destinations,
				// expected_audit_destinations,firewallType+" TotalDestinations
				// should be ");//TotalDestinations

			} /*
				 * else{ validateSummaryInfoNew(expected_audit_destinations,
				 * actual_audit_destinations,firewallType,summaryLabel,
				 * auditSummaryValidationsErrors); }
				 */

			// high risky services
			String expected_audit_high_risky_services = String
					.valueOf(expectedAuditSummary.getHigh_risky_services_count());
			String actual_audit_high_risky_services = String.valueOf(actualAuditSummary.getHigh_risky_services_count());
			summaryLabel = "high_risk_services";
			if (actual_audit_high_risky_services == expected_audit_high_risky_services) {
				Assert.assertEquals(actual_audit_high_risky_services, expected_audit_high_risky_services,
						firewallType + " high_risk_services should be ");// high_risk_services
			} else {
				// goldenSetErrorList.add(firewallType+"::"+label+"
				// high_risk_services should
				// be:"+actual_audit_high_risky_services+" but
				// found:"+expected_audit_high_risky_services);
				validateSummaryInfoNew(expected_audit_high_risky_services, actual_audit_high_risky_services,
						firewallType, summaryLabel, auditSummaryValidationsErrors);

			}
			// medum risky services
			String expected_audit_med_risk_services = String
					.valueOf(expectedAuditSummary.getMed_risky_services_count());
			String actual_audit_med_risk_services = String.valueOf(actualAuditSummary.getMed_risky_services_count());
			summaryLabel = "med_risk_services";
			if (actual_audit_med_risk_services == expected_audit_med_risk_services) {
				Assert.assertEquals(actual_audit_med_risk_services, expected_audit_med_risk_services,
						firewallType + " med_risk_services should be ");// med_risk_services
			} else {
				// goldenSetErrorList.add(firewallType+"::"+label+"
				// med_risk_services should
				// be:"+actual_audit_med_risk_services+" but
				// found:"+expected_audit_med_risk_services);
				validateSummaryInfoNew(expected_audit_med_risk_services, actual_audit_med_risk_services, firewallType,
						summaryLabel, auditSummaryValidationsErrors);

			}
			// Risky Services Brr score validation
			List<AuditSummaryTopRiskyServices> expectedauditSummaryTopRiskyServicesList = expectedAuditSummary
					.getSummaryTopRiskyServicesList();
			Collections.sort(expectedauditSummaryTopRiskyServicesList,
					AuditSummaryTopRiskyServices.serviceNameComparator);

			List<AuditSummaryTopRiskyServices> actualSummaryTopRiskyServicesList = actualAuditSummary
					.getSummaryTopRiskyServicesList();
			Collections.sort(actualSummaryTopRiskyServicesList, AuditSummaryTopRiskyServices.serviceNameComparator);

			summaryLabel = "top_risky_services";
			for (int i = 0; i < expectedauditSummaryTopRiskyServicesList.size(); i++) {
				AuditSummaryTopRiskyServices expectedServices = expectedauditSummaryTopRiskyServicesList.get(i);
				String expectedService = expectedServices.getServicename();
				String expectedServiceBrr = expectedServices.getService_brr();

				String actualService = ((AuditSummaryTopRiskyServices) actualSummaryTopRiskyServicesList.get(i))
						.getServicename();
				String actualServiceBrr = ((AuditSummaryTopRiskyServices) actualSummaryTopRiskyServicesList.get(i))
						.getService_brr();
				if (!(expectedService.equals(actualService) && expectedServiceBrr.equals(actualServiceBrr))) {
					auditSummaryValidationsErrors.add(firewallType + " " + summaryLabel + "expected service "
							+ expectedService + " Brr: " + expectedServiceBrr + " but Brr found" + actualServiceBrr);
				}

			}

			Reporter.log("auditSummaryValidationsErrors" + auditSummaryValidationsErrors, true);

			//
		}
		
		return auditSummaryValidationsErrors;
		
	}
	
	public static List<String> validateAuditReport( String firewallType,AuditReport expectedReport, AuditReport actualReport,List<String> auditReportValidationsErrors)
	{
		
	
			
		List<String> reportToatalsValidationErrors=auditReportTotalsValidation(firewallType,expectedReport,actualReport);
		List<String> reportRiskyServicesValidationErrors=auditReportRiskyServicesValidation(firewallType,expectedReport,actualReport);
		List<String> mostUsedServicesValidationErrors=auditReportMostUsedServicesValidation(firewallType,expectedReport,actualReport);
		List<String> reportServiceDetailsValidation=auditReportServiceDetailsValidation(firewallType,expectedReport,actualReport);
		
		
		
		auditReportValidationsErrors.addAll(reportToatalsValidationErrors);
		auditReportValidationsErrors.addAll(reportRiskyServicesValidationErrors);
		auditReportValidationsErrors.addAll(mostUsedServicesValidationErrors);
		auditReportValidationsErrors.addAll(reportServiceDetailsValidation);
		
		return auditReportValidationsErrors;
		
	}
	
	private static List<String> auditReportServiceDetailsValidation(String firewallType,AuditReport expectedReport, AuditReport actualReport)
	{
		List<String> reportServiceDetailsValidationErrors = new ArrayList<String>();
		
		
		List<AuditReportServiceDetails> expectedAuditReportServiceDetailsList=expectedReport.getAuditReportServiceDetailsList();
		Collections.sort(expectedAuditReportServiceDetailsList,AuditReportServiceDetails.serviceIDComparator);

		List<AuditReportServiceDetails> actualAuditReportServiceDetailsList=actualReport.getAuditReportServiceDetailsList();
		Collections.sort(actualAuditReportServiceDetailsList,AuditReportServiceDetails.serviceIDComparator);
	
		String auditReportServiceDetailsValidation="Audit Report Service Details Validation";
		String auditReportServiceDetails_ServiceId = "Audit Report Service Details -ServiceId";
		String auditReportServiceDetails_IsNew = "Audit Report Service Details -IsNew";
		String auditReportServiceDetails_IsMostUsed = "Audit Report Service Details -IsMostUsed";
		String auditReportServiceDetails_Cat1 = "Audit Report Service Details -Cat1";
		String auditReportServiceDetails_Cat2 = "Audit Report Service Details -Cat2";
		String auditReportServiceDetails_Cat3 = "Audit Report Service Details -Cat3";
		String auditReportServiceDetails_Cat4 = "Audit Report Service Details -Cat4";
		String auditReportServiceDetails_UserCount = "Audit Report Service Details -UserCount";
		String auditReportServiceDetails_Uploads = "Audit Report Service Details -Uploads";
		String auditReportServiceDetails_Downloads = "Audit Report Service Details -Downloads";
		String auditReportServiceDetails_Sessions = "Audit Report Service Details -Sessions";
		String auditReportServiceDetails_LocationsCount = "Audit Report Service Details -LocationsCount";
		String auditReportServiceDetails_ServiceBrr = "Audit Report Service Details -ServiceBrr";
		String auditReportServiceDetails_ServiceUrl = "Audit Report Service Details -ServiceUrl";
		
		String errorMsg = null;
		AuditReportServiceDetails actualAuditReportServiceDetails;
		AuditReportServiceDetails expectedAuditReportServiceDetails;
		
		for(int i=0; i<actualAuditReportServiceDetailsList.size(); i++)
		{
			actualAuditReportServiceDetails=actualAuditReportServiceDetailsList.get(i);
			expectedAuditReportServiceDetails=expectedAuditReportServiceDetailsList.get(i);
			
			//serviceid verification
				if (!(actualAuditReportServiceDetails.getServiceId().equals(expectedAuditReportServiceDetails.getServiceId()))) {
					errorMsg = firewallType + " " + auditReportServiceDetailsValidation + "expected " + auditReportServiceDetails_ServiceId + " are:"
							+ expectedAuditReportServiceDetails.getServiceId() + " but actual " + auditReportServiceDetails_ServiceId + "found:"
							+ actualAuditReportServiceDetails.getServiceId();
					reportServiceDetailsValidationErrors.add(errorMsg);
				}
		
			//service users verification
				if (!(actualAuditReportServiceDetails.getUsers_Count()==expectedAuditReportServiceDetails.getUsers_Count())) {
					errorMsg = firewallType + " " + auditReportServiceDetailsValidation + "expected " + auditReportServiceDetails_UserCount + " are:"
							+ expectedAuditReportServiceDetails.getUsers_Count() + " but actual " + auditReportServiceDetails_UserCount + "found:"
							+ actualAuditReportServiceDetails.getUsers_Count();
					reportServiceDetailsValidationErrors.add(errorMsg);
				}
			//servicebrr verification
				if (!(actualAuditReportServiceDetails.getService_Brr().equals(expectedAuditReportServiceDetails.getService_Brr()))) {
					errorMsg = firewallType + " " + auditReportServiceDetailsValidation + "expected " + auditReportServiceDetails_ServiceBrr + " are:"
							+ expectedAuditReportServiceDetails.getService_Brr() + " but actual " + auditReportServiceDetails_ServiceBrr + "found:"
							+ actualAuditReportServiceDetails.getService_Brr();
					reportServiceDetailsValidationErrors.add(errorMsg);
				}
			//service uploads	
				if (!(actualAuditReportServiceDetails.getUploads()==expectedAuditReportServiceDetails.getUploads())) {
					errorMsg = firewallType + " " + auditReportServiceDetailsValidation + "expected " + auditReportServiceDetails_Uploads + " are:"
							+ expectedAuditReportServiceDetails.getUploads() + " but actual " + auditReportServiceDetails_Uploads + "found:"
							+ actualAuditReportServiceDetails.getUploads();
					reportServiceDetailsValidationErrors.add(errorMsg);
				}
			//service downloads	
				if (!(actualAuditReportServiceDetails.getDownloads()==expectedAuditReportServiceDetails.getDownloads())) {
					errorMsg = firewallType + " " + auditReportServiceDetailsValidation + "expected " + auditReportServiceDetails_Downloads + " are:"
							+ expectedAuditReportServiceDetails.getDownloads() + " but actual " + auditReportServiceDetails_Downloads + "found:"
							+ actualAuditReportServiceDetails.getDownloads();
					reportServiceDetailsValidationErrors.add(errorMsg);
				}
				
			//service sessions	
				if (!(actualAuditReportServiceDetails.getSessions()==expectedAuditReportServiceDetails.getSessions())) {
					errorMsg = firewallType + " " + auditReportServiceDetailsValidation + "expected " + auditReportServiceDetails_Sessions + " are:"
							+ expectedAuditReportServiceDetails.getSessions() + " but actual " + auditReportServiceDetails_Sessions + "found:"
							+ actualAuditReportServiceDetails.getSessions();
					reportServiceDetailsValidationErrors.add(errorMsg);
				}
			//service sessions	
				if (!(actualAuditReportServiceDetails.getCat1().equals(expectedAuditReportServiceDetails.getCat1()))) {
					errorMsg = firewallType + " " + auditReportServiceDetailsValidation + "expected " + auditReportServiceDetails_Cat1 + " are:"
							+ expectedAuditReportServiceDetails.getCat1() + " but actual " + auditReportServiceDetails_Cat1 + "found:"
							+ actualAuditReportServiceDetails.getCat1();
					reportServiceDetailsValidationErrors.add(errorMsg);
				}	
						
			}
		
		
		return reportServiceDetailsValidationErrors;
		

	}

	private static List<String> auditReportMostUsedServicesValidation(String firewallType,AuditReport expectedReport, AuditReport actualReport)
 {

		List<String> reportToatalsValidationErrors = new ArrayList<String>();
		
		AuditReportMostUsedServices expectedAuditMostUsedServices=expectedReport.getAuditMostUsedServices();
		AuditReportMostUsedServices actualAuditMostUsedServices=actualReport.getAuditMostUsedServices();
		
		String auditReportMostUsedServicesValidation="Audit Report Most Used Services Validation";
		String auditReportMostUsedServices_Risky = "Audit Report Most Used Services -Risky";
		String auditReportMostUsedServices_Users = "Audit Report Most Used Services -Users";
		String auditReportMostUsedServices_Traffic = "Audit Report Most Used Services -Traffic";
		String auditReportMostUsedServices_Sessions = "Audit Report Most Used Services -Sessions";
		String auditReportMostUsedServices_Categories = "Audit Report Most Used Services -Categories";
		String auditReportMostUsedServices_Destinations = "Audit Report Most Used Services -Destinations";
		String auditReportMostUsedServices_Newservices = "Audit Report Most Used Services -NewServices";
		
		String errorMsg = null;
		if (!(actualAuditMostUsedServices.getRiskyServices() == expectedAuditMostUsedServices.getRiskyServices())) {
			errorMsg = firewallType + " " + auditReportMostUsedServicesValidation + "expected " + auditReportMostUsedServices_Risky + " are:"
					+ expectedAuditMostUsedServices.getRiskyServices() + " but actual " + auditReportMostUsedServices_Risky + "found:"
					+ actualAuditMostUsedServices.getRiskyServices();
			reportToatalsValidationErrors.add(errorMsg);
		}
		
		if (!(actualAuditMostUsedServices.getUsers() == expectedAuditMostUsedServices.getUsers())) {
			errorMsg = firewallType + " " + auditReportMostUsedServicesValidation + "expected " + auditReportMostUsedServices_Users + " are:"
					+ expectedAuditMostUsedServices.getUsers() + " but actual " + auditReportMostUsedServices_Users + "found:"
					+ actualAuditMostUsedServices.getUsers();
			reportToatalsValidationErrors.add(errorMsg);
		}
		
		if (!(actualAuditMostUsedServices.getTotal_traffic() == expectedAuditMostUsedServices.getTotal_traffic())) {
			errorMsg = firewallType + " " + auditReportMostUsedServicesValidation + "expected " + auditReportMostUsedServices_Traffic + " are:"
					+ expectedAuditMostUsedServices.getTotal_traffic() + " but actual " + auditReportMostUsedServices_Traffic + "found:"
					+ actualAuditMostUsedServices.getTotal_traffic();
			reportToatalsValidationErrors.add(errorMsg);
		}
		
		if (!(actualAuditMostUsedServices.getSessions() == expectedAuditMostUsedServices.getSessions())) {
			errorMsg = firewallType + " " + auditReportMostUsedServicesValidation + "expected " + auditReportMostUsedServices_Sessions + " are:"
					+ expectedAuditMostUsedServices.getSessions() + " but actual " + auditReportMostUsedServices_Sessions + "found:"
					+ actualAuditMostUsedServices.getSessions();
			reportToatalsValidationErrors.add(errorMsg);
		}
		/*
		if (!(actualAuditMostUsedServices.getCategories() == expectedAuditMostUsedServices.getCategories())) {
			errorMsg = firewallType + " " + auditReportMostUsedServicesValidation + "expected " + auditReportMostUsedServices_Categories + " are:"
					+ expectedAuditMostUsedServices.getCategories() + " but actual " + auditReportMostUsedServices_Categories + "found:"
					+ actualAuditMostUsedServices.getCategories();
			reportToatalsValidationErrors.add(errorMsg);
		}
		
		if (!(actualAuditMostUsedServices.getTotal_new_discoServices() == expectedAuditMostUsedServices.getTotal_new_discoServices())) {
			errorMsg = firewallType + " " + auditReportMostUsedServicesValidation + "expected " + auditReportMostUsedServices_Newservices + " are:"
					+ expectedAuditMostUsedServices.getTotal_new_discoServices() + " but actual " + auditReportMostUsedServices_Newservices + "found:"
					+ actualAuditMostUsedServices.getTotal_new_discoServices();
			reportToatalsValidationErrors.add(errorMsg);
		}
		if (!(actualAuditMostUsedServices.getLocations() == expectedAuditMostUsedServices.getLocations())) {
			errorMsg = firewallType + " " + auditReportMostUsedServicesValidation + "expected " + auditReportMostUsedServices_Destinations + " are:"
					+ expectedAuditMostUsedServices.getLocations() + " but actual " + auditReportMostUsedServices_Destinations + "found:"
					+ actualAuditMostUsedServices.getLocations();
			reportToatalsValidationErrors.add(errorMsg);
		}
		
	*/
		
		return reportToatalsValidationErrors;
 }
	
	
	private static List<String> auditReportTotalsValidation(String firewallType,AuditReport expectedReport, AuditReport actualReport)
 {

		// Totals Validation
		AuditReportTotals expectedAuditReportTotals = expectedReport.getAuditReportTotals();
		AuditReportTotals actualAuditReportTotals = actualReport.getAuditReportTotals();
		List<String> reportToatalsValidationErrors = new ArrayList<String>();
		String auditReportTotalValidation = "Audit Report Totals validation:";
		String report_total_services = "Total services";
		String report_total_users = "Total users";
		String report_total_traffic = "Total traffic";
		String report_total_downloads = "Total downloads";
		String report_total_uploads = "Total uploads";
		String report_total_sessions = "Total sessions";
		
		String report_total_locations = "Total locations";
		String report_total_categories = "Total categories";
		String report_total_newservices = "Total new services";
		String errorMsg = null;

		if (!(actualAuditReportTotals.getServices() == expectedAuditReportTotals.getServices())) {
			errorMsg = firewallType + " " + auditReportTotalValidation + "expected " + report_total_services + " are:"
					+ expectedAuditReportTotals.getServices() + " but actual " + report_total_services + "found:"
					+ actualAuditReportTotals.getServices();
			reportToatalsValidationErrors.add(errorMsg);
		}
		if (!(actualAuditReportTotals.getUsers() == expectedAuditReportTotals.getUsers())) {
			errorMsg = firewallType + " " + auditReportTotalValidation + "expected " + report_total_users + " are:"
					+ expectedAuditReportTotals.getUsers() + " but actual " + report_total_users + "found:"
					+ actualAuditReportTotals.getUsers();
			reportToatalsValidationErrors.add(errorMsg);
		}

		if (!(actualAuditReportTotals.getTraffic() == expectedAuditReportTotals.getTraffic())) {
			errorMsg = firewallType + " " + auditReportTotalValidation + "expected " + report_total_traffic + " are:"
					+ expectedAuditReportTotals.getTraffic() + " but actual " + report_total_traffic + "found:"
					+ actualAuditReportTotals.getTraffic();
			reportToatalsValidationErrors.add(errorMsg);
		}
		if (!(actualAuditReportTotals.getDownloads() == expectedAuditReportTotals.getDownloads())) {
			errorMsg = firewallType + " " + auditReportTotalValidation + "expected " + report_total_downloads + " are:"
					+ expectedAuditReportTotals.getDownloads() + " but actual " + report_total_downloads + "found:"
					+ actualAuditReportTotals.getDownloads();
			reportToatalsValidationErrors.add(errorMsg);
		}
		if (!(actualAuditReportTotals.getUploads() == expectedAuditReportTotals.getUploads())) {
			errorMsg = firewallType + " " + auditReportTotalValidation + "expected " + report_total_uploads + " are:"
					+ expectedAuditReportTotals.getUploads() + " but actual " + report_total_uploads + "found:"
					+ actualAuditReportTotals.getUploads();
			reportToatalsValidationErrors.add(errorMsg);
		}
		if (!(actualAuditReportTotals.getSessions() == expectedAuditReportTotals.getSessions())) {
			errorMsg = firewallType + " " + auditReportTotalValidation + "expected " + report_total_sessions + " are:"
					+ expectedAuditReportTotals.getSessions() + " but actual " + report_total_sessions + "found:"
					+ actualAuditReportTotals.getSessions();
			reportToatalsValidationErrors.add(errorMsg);
		}
		/*if (!(actualAuditReportTotals.getLocations() == expectedAuditReportTotals.getLocations())) {
			errorMsg = firewallType + " " + auditReportTotalValidation + "expected " + report_total_locations + " are:"
					+ expectedAuditReportTotals.getLocations() + " but actual " + report_total_locations + "found:"
					+ actualAuditReportTotals.getLocations();
			reportToatalsValidationErrors.add(errorMsg);
		}
		if (!(actualAuditReportTotals.getCategories() == expectedAuditReportTotals.getCategories())) {
			errorMsg = firewallType + " " + auditReportTotalValidation + "expected " + report_total_categories + " are:"
					+ expectedAuditReportTotals.getCategories() + " but actual " + report_total_categories + "found:"
					+ actualAuditReportTotals.getCategories();
			reportToatalsValidationErrors.add(errorMsg);
		}
		
		if (!(actualAuditReportTotals.getNew_services() == expectedAuditReportTotals.getNew_services())) {
			errorMsg = firewallType + " " + auditReportTotalValidation + "expected " + report_total_newservices + " are:"
					+ expectedAuditReportTotals.getNew_services() + " but actual " + report_total_newservices + "found:"
					+ actualAuditReportTotals.getNew_services();
			reportToatalsValidationErrors.add(errorMsg);
		}*/
		return reportToatalsValidationErrors;
	}
	private static List<String> auditReportRiskyServicesValidation(String firewallType,AuditReport expectedReport, AuditReport actualReport)
	{
		//RiskyServices Validation
		AuditReportRiskyServices expectedAuditReportRiskyServices=expectedReport.getAuditReportRiskyServices();
		AuditReportRiskyServices actualAuditReportRiskyServices=actualReport.getAuditReportRiskyServices();
				
		List<String> reportRiskyServicesValidationErrors=new ArrayList<String>();
		String auditReportRiskyServicesValidation="Audit Report RiskyServices validation:";
		
		String report_risky_services_medium="risky_services medium";
		String report_risky_services_users="risky_services users";
		String report_risky_services_sessions="risky_services sessions";
		String report_risky_services_downloads="risky_services downloads";
		String report_risky_services_mostusedservices="risky_services most used services";
		String report_risky_services_total="risky_services total";
		String report_risky_services_uploads="risky_services uploads";
		String report_risky_services_high="risky_services high";
		String report_risky_services_total_traffic="risky_services total trafic";
		String report_risky_services_locations="risky_services locations";
		String report_risky_services_categories="risky_services categories";
		
		String errorMsg=null;
		
		if(!(actualAuditReportRiskyServices.getMed_risky_services()==expectedAuditReportRiskyServices.getMed_risky_services())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_medium+" are:"+
					expectedAuditReportRiskyServices.getMed_risky_services()+" but actual "+report_risky_services_medium+ "found:"+actualAuditReportRiskyServices.getMed_risky_services();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		if(!(actualAuditReportRiskyServices.getUsers()==expectedAuditReportRiskyServices.getUsers())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_users+" are:"+
					expectedAuditReportRiskyServices.getUsers()+" but actual "+report_risky_services_users+ "found:"+actualAuditReportRiskyServices.getUsers();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		if(!(actualAuditReportRiskyServices.getSessions()==expectedAuditReportRiskyServices.getSessions())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_sessions+" are:"+
					expectedAuditReportRiskyServices.getSessions()+" but actual "+report_risky_services_sessions+ "found:"+actualAuditReportRiskyServices.getSessions();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		if(!(actualAuditReportRiskyServices.getDownloads()==expectedAuditReportRiskyServices.getDownloads())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_downloads+" are:"+
					expectedAuditReportRiskyServices.getDownloads()+" but actual "+report_risky_services_downloads+ "found:"+actualAuditReportRiskyServices.getDownloads();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		
		if(!(actualAuditReportRiskyServices.getMost_used_services()==expectedAuditReportRiskyServices.getMost_used_services())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_mostusedservices+" are:"+
					expectedAuditReportRiskyServices.getMost_used_services()+" but actual "+report_risky_services_mostusedservices+ 
					"found:"+actualAuditReportRiskyServices.getMost_used_services();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		
		if(!(actualAuditReportRiskyServices.getTotal_services()==expectedAuditReportRiskyServices.getTotal_services())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_total+" are:"+
					expectedAuditReportRiskyServices.getTotal_services()+" but actual "+report_risky_services_total+ 
					"found:"+actualAuditReportRiskyServices.getTotal_services();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		if(!(actualAuditReportRiskyServices.getUploads()==expectedAuditReportRiskyServices.getUploads())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_uploads+" are:"+
					expectedAuditReportRiskyServices.getUploads()+" but actual "+report_risky_services_uploads+ 
					"found:"+actualAuditReportRiskyServices.getUploads();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		if(!(actualAuditReportRiskyServices.getHigh_risky_services()==expectedAuditReportRiskyServices.getHigh_risky_services())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_high+" are:"+
					expectedAuditReportRiskyServices.getHigh_risky_services()+" but actual "+report_risky_services_high+ 
					"found"+actualAuditReportRiskyServices.getHigh_risky_services();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		if(!(actualAuditReportRiskyServices.getTotal_traffic()==expectedAuditReportRiskyServices.getTotal_traffic())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_total_traffic+" are:"+
					expectedAuditReportRiskyServices.getTotal_traffic()+" but actual "+report_risky_services_total_traffic+ 
					"found:"+actualAuditReportRiskyServices.getTotal_traffic();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		/*if(!(actualAuditReportRiskyServices.getCategories()==expectedAuditReportRiskyServices.getCategories())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_categories+" are:"+
					expectedAuditReportRiskyServices.getCategories()+" but actual "+report_risky_services_categories+ 
					"found:"+actualAuditReportRiskyServices.getCategories();
			reportRiskyServicesValidationErrors.add(errorMsg);
		}
		if(!(actualAuditReportRiskyServices.getLocations()==expectedAuditReportRiskyServices.getLocations())){
			errorMsg=firewallType+" "+auditReportRiskyServicesValidation+"expected "+report_risky_services_locations+" are:"+
					expectedAuditReportRiskyServices.getLocations()+" but actual "+report_risky_services_locations+ 
					"found:"+actualAuditReportRiskyServices.getLocations();
			//reportRiskyServicesValidationErrors.add(errorMsg);
		}*/
		return reportRiskyServicesValidationErrors;
		
	}
	
	
//new summary validations end	

	public static SummaryTabDto getServicesTabData(String sourceID,String resolution, String earliestDate, String latestDate) throws Exception
	{
		JSONObject services_usersData = new JSONObject();
		services_usersData.put("ds_id", sourceID);
		services_usersData.put("version", "1");		
		services_usersData.put("ignore_hidden",true);						
		services_usersData.put("blocked", false);
		services_usersData.put("allowed", true);
		services_usersData.put("resolution", resolution);
		services_usersData.put("earliest_date", earliestDate);
		services_usersData.put("latest_date", latestDate);
		services_usersData.put("tab", "services");
		Reporter.log("services_usersData Request payload:"+services_usersData.toString(),true);
	
		HttpResponse response  = AuditFunctions.getServiceTabDataApiThroughPost(new Client(), new StringEntity(services_usersData.toString()));				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
	
		JSONObject servicesTabJsonObj = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response));			
		Reporter.log("Actual Audit summar services tab Response: "+sourceID+"::"+servicesTabJsonObj,true);


		SummaryTabDto summaryTabDto=new SummaryTabDto();

		//prepare services-users map
		String servicesTabDataLabel="services";
		if (servicesTabJsonObj.has(servicesTabDataLabel) && !servicesTabJsonObj.isNull(servicesTabDataLabel)) {
			JSONObject info = servicesTabJsonObj.getJSONObject(servicesTabDataLabel);
			Map<String,String> servicesMap = new HashMap<String, String>();
			parse(info,servicesMap);
			Reporter.log("services map object..."+servicesMap,true);
			summaryTabDto.setServiceUsersMap(servicesMap);
		} 


		//total users
		String totalUsersLable="total_users";
		if (servicesTabJsonObj.has(totalUsersLable) && !servicesTabJsonObj.isNull(totalUsersLable)) {
			summaryTabDto.setTotal_users(servicesTabJsonObj.getString(totalUsersLable));

		}

		//prepare location_users map

		String locationsUsersLabel="locations";
		if (servicesTabJsonObj.has(locationsUsersLabel) && !servicesTabJsonObj.isNull(locationsUsersLabel)) {
			JSONObject info = servicesTabJsonObj.getJSONObject(locationsUsersLabel);
			Map<String,String> locationsUsersMap = new HashMap<String, String>();
			parse(info,locationsUsersMap);
			Reporter.log("locations map object..."+locationsUsersMap,true);
			summaryTabDto.setLocationsMap(locationsUsersMap);

		}

		Reporter.log("summaryTabDto..."+summaryTabDto,true);
		return summaryTabDto;

	}
	
	public static SummaryTabDto getConsumerServicesTabData(String sourceID,String resolution, String earliestDate, String latestDate,boolean consumerFlag) throws Exception
	{
		JSONObject services_usersData = new JSONObject();
		services_usersData.put("ds_id", sourceID);
		services_usersData.put("version", "1");		
		services_usersData.put("ignore_hidden",true);						
		services_usersData.put("blocked", false);
		services_usersData.put("allowed", true);
		services_usersData.put("consumer", consumerFlag);
		services_usersData.put("resolution", resolution);
		services_usersData.put("earliest_date", earliestDate);
		services_usersData.put("latest_date", latestDate);
		services_usersData.put("tab", "services");
		Reporter.log("services_usersData Request payload:"+services_usersData.toString(),true);
	
		HttpResponse response  = AuditFunctions.getServiceTabDataApiThroughPost(new Client(), new StringEntity(services_usersData.toString()));				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		JSONObject servicesTabJsonObj = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response));			
		Reporter.log("Actual Audit summar services tab Response: "+sourceID+"::"+servicesTabJsonObj,true);


		SummaryTabDto summaryTabDto=new SummaryTabDto();

		//prepare services-users map
		String servicesTabDataLabel="services";
		if (servicesTabJsonObj.has(servicesTabDataLabel) && !servicesTabJsonObj.isNull(servicesTabDataLabel)) {
			JSONObject info = servicesTabJsonObj.getJSONObject(servicesTabDataLabel);
			Map<String,String> servicesMap = new HashMap<String, String>();
			parse(info,servicesMap);
			Reporter.log("services map object..."+servicesMap,true);
			summaryTabDto.setServiceUsersMap(servicesMap);
		} 


		//total users
		String totalUsersLable="total_users";
		if (servicesTabJsonObj.has(totalUsersLable) && !servicesTabJsonObj.isNull(totalUsersLable)) {
			summaryTabDto.setTotal_users(servicesTabJsonObj.getString(totalUsersLable));

		}

		//prepare location_users map

		String locationsUsersLabel="locations";
		if (servicesTabJsonObj.has(locationsUsersLabel) && !servicesTabJsonObj.isNull(locationsUsersLabel)) {
			JSONObject info = servicesTabJsonObj.getJSONObject(locationsUsersLabel);
			Map<String,String> locationsUsersMap = new HashMap<String, String>();
			parse(info,locationsUsersMap);
			Reporter.log("locations map object..."+locationsUsersMap,true);
			summaryTabDto.setLocationsMap(locationsUsersMap);

		}

		Reporter.log("summaryTabDto..."+summaryTabDto,true);
		return summaryTabDto;

	}
	
	public static Map<String,String> parse(JSONObject json , Map<String,String> out) throws JSONException{
	    Iterator<String> keys = json.keys();
	    while(keys.hasNext()){
	        String key = keys.next();
	        String val = null;
	        try{
	             JSONObject value = json.getJSONObject(key);
	             parse(value,out);
	        }catch(Exception e){
	            val = json.getString(key);
	        }

	        if(val != null){
	            out.put(key,val);
	        }
	    }
	    return out;
	}
	

	public String calculateProperFileSize(double bytes){
		
		String[] fileSizeUnits = {"bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
	    String sizeToReturn = "";// = FileUtils.byteCountToDisplaySize(bytes), unit = ""; 
	    int index = 0;
	    for(index = 0; index < fileSizeUnits.length; index++){
	        if(bytes < 1024){
	            break;
	        }
	        bytes = bytes / 1024;
	    }
	    System.out.println("Systematic file size: " + bytes + " " + fileSizeUnits[index]);
	    sizeToReturn = String.valueOf(bytes) + " " + fileSizeUnits[index];
	    return sizeToReturn;
	}
	
	public static String getSpanVATenantScpPwd(String tenant)	throws Exception{
		
		Properties spanvaScpPwds=new Properties();
		spanvaScpPwds.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.SPANVA_SCP_PWDS)));
		return spanvaScpPwds.getProperty(tenant);
	}
	
	/**
	 * @return
	 */
	public static String getPythonScriptPath() {
		return FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.SYSLOG_TRAFFIC_GENERATOR_SCRIPT_FILEPATH);
	}
	
	public static String s3RegEnvBucketName(String envname, String fireWallType,String tenantName) 
	{
		String folderName = null;
		if ("eoe".equals(envname)) {
			folderName = AuditTestConstants.EOE_S3_REG_BUCKET + File.separator + AuditTestUtils.getUniqFolderName(
					envname, "S3Reg" + fireWallType, tenantName);
		} else if ("cep".equals(envname)) {
			folderName = AuditTestConstants.CEP_S3_REG_BUCKET + File.separator + AuditTestUtils.getUniqFolderName(
					envname, "S3Reg" + fireWallType, tenantName);

		} else if ("prod".equals(envname)) {
			folderName = AuditTestConstants.PROD_S3_REG_BUCKET + File.separator + AuditTestUtils.getUniqFolderName(
					envname, "S3Reg" + fireWallType, tenantName);
		} 
		else if ("envX".equals(envname)) {
			folderName = AuditTestConstants.envX_S3_REG_BUCKET + File.separator + AuditTestUtils.getUniqFolderName(
					envname, "S3Reg" + fireWallType, tenantName);
		} 
		else {
			folderName = AuditTestConstants.QAVPC_S3_REG_BUCKET + File.separator + AuditTestUtils.getUniqFolderName(
					envname, "S3Reg" + fireWallType, tenantName);

		}
		return folderName;		
	}
	
	/*public synchronized AuditSummary populateActualAuditSummaryObject(Client restClient, String fireWallType, String sourceID ) throws Exception
	{

		String range ="";

		if(fireWallType.equals(AuditTestConstants.FIREWALL_SQUID_PROXY) ||
				fireWallType.equals(AuditTestConstants.FIREWALL_BE_WSA_ACCESS ) )
		{
			range="1y";
		}
		else{
			range = "1mo";	
		}
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);				
		Reporter.log("Actual Audit summar Api Response: "+fireWallType+"_"+sourceID+"::"+summaryObject,true);


		String summaryLabel;

		AuditSummary actualAuditSummaryObj=new AuditSummary();
		JSONObject companyBrr = summaryObject.getJSONObject("company_brr");
		String actual_audit_score= companyBrr.getString("value"); 
		actualAuditSummaryObj.setAuditScore(actual_audit_score);

		summaryLabel="total_services";
		String actual_audit_saasservices=summaryObject.getString(summaryLabel);
		actualAuditSummaryObj.setSaas_services_count(Integer.parseInt(actual_audit_saasservices));

		summaryLabel="total_destinations";
		String actual_audit_destinations=summaryObject.getString(summaryLabel);
		actualAuditSummaryObj.setDestination_count(Integer.parseInt(actual_audit_destinations));

		summaryLabel="total_users";
		String actual_audit_users=summaryObject.getString(summaryLabel);
		actualAuditSummaryObj.setUsers_count(Integer.parseInt(actual_audit_users));

		//Risky services
		String label="top_risky_services";
		int topRiskyServicesArrayLength=0;
		String top_risky_serviceBRR=null;
		String serviceId=null;
		String userCount=null;
		String serviceName=null;
		AuditSummaryTopRiskyServices auditSummaryTopRiskyServices=null;
		List<AuditSummaryTopRiskyServices> auditSummaryTopRiskyServicesList=new ArrayList<AuditSummaryTopRiskyServices>();
		Set<String> setServices=new HashSet<String>();
		List<String> totalAuditServicesList=new ArrayList<String>();;


		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topRiskyServicesArray = summaryObject.getJSONArray(label);
			topRiskyServicesArrayLength =topRiskyServicesArray.length();


			for(int i=0; i<topRiskyServicesArrayLength; i++)
			{
				auditSummaryTopRiskyServices= new AuditSummaryTopRiskyServices();
				serviceId=((JSONObject)topRiskyServicesArray.get(i)).getString("service_id");
				serviceName=AuditTestUtils.getServiceName(serviceId);
				auditSummaryTopRiskyServices.setServicename(serviceName);
				//setServices.add(serviceName);

				top_risky_serviceBRR=((JSONObject)topRiskyServicesArray.get(i)).getString("sort_key_brr");
				auditSummaryTopRiskyServices.setService_brr(top_risky_serviceBRR);

				userCount=((JSONObject)topRiskyServicesArray.get(i)).getString("users_count");
				auditSummaryTopRiskyServices.setService_user_count(Integer.parseInt(userCount));
				auditSummaryTopRiskyServicesList.add(auditSummaryTopRiskyServices);

			}
		}

		actualAuditSummaryObj.setSummaryTopRiskyServicesList(auditSummaryTopRiskyServicesList);

		//top used services
		label="top_used_services";
		int topUsedServicesArrayLength=0;

		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topUsedServicesArray = summaryObject.getJSONArray(label);
			topUsedServicesArrayLength =topUsedServicesArray.length();

			for(int i=0; i<topUsedServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topUsedServicesArray.get(i)).getString("service_id");
				serviceName=AuditTestUtils.getServiceName(serviceId);
				//setServices.add(serviceName);

			}

		}

		SummaryTabDto summaryTabDto=AuditTestUtils.getServicesTabData(sourceID);
		for(String serviceID:summaryTabDto.getServiceUsersMap().keySet())
		{
			serviceName=AuditTestUtils.getServiceName(serviceID);
			setServices.add(serviceName);
		}
		actualAuditSummaryObj.setTotalAuditServicesList(new ArrayList<>(setServices));


		label="high_risk_services";
		String actual_audit_high_risky_services=summaryObject.getString(label);
		actualAuditSummaryObj.setHigh_risky_services_count(Integer.parseInt(actual_audit_high_risky_services));


		label="med_risk_services";
		String actual_audit_med_risk_services=summaryObject.getString(label);
		actualAuditSummaryObj.setMed_risky_services_count(Integer.parseInt(actual_audit_med_risk_services));

		return actualAuditSummaryObj;

	}
	*/
	public synchronized AuditReport populateActualAuditReportData(Client restClient, String fireWallType,String sourceID) throws Exception
	{

		String range="";
		if(fireWallType.equals(AuditTestConstants.FIREWALL_SQUID_PROXY) ||
				fireWallType.equals(AuditTestConstants.FIREWALL_BE_WSA_ACCESS ) )
		{
			range="1y";
		}
		else{
			range = "1mo";	
		}

		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();

		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response = AuditFunctions.getAuditReport(restClient, queryParam);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response))
				.getJSONArray("objects").get(0);
		Reporter.log("Actual Audit Report Response:" + reportObject, true);

		String dsStatus=null;
		AuditReportTotals actualAuditReportTotal=null;
		AuditReportRiskyServices auditReportRiskyServices=null;
		AuditReportMostUsedServices auditReportMostUsedServices=null;
		AuditReportServiceCategories auditReportServiceCategories=null;
		AuditReport auditReport=new AuditReport();

		//Totals
		if (reportObject.has("total") && !reportObject.isNull("total")) {
			JSONObject totalobj = reportObject.getJSONObject("total");
			actualAuditReportTotal=new AuditReportTotals();
			actualAuditReportTotal.setUsers(Long.parseLong(totalobj.getString("users")));
			actualAuditReportTotal.setSessions(Long.parseLong(totalobj.getString("sessions")));
			actualAuditReportTotal.setServices(Long.parseLong(totalobj.getString("services")));
			actualAuditReportTotal.setLocations(Long.parseLong(totalobj.getString("locations")));
			actualAuditReportTotal.setCategories(Long.parseLong(totalobj.getString("categories")));
			actualAuditReportTotal.setTraffic(Long.parseLong(totalobj.getString("traffic")));
			actualAuditReportTotal.setUploads(Long.parseLong(totalobj.getString("uploads")));
			actualAuditReportTotal.setDownloads(Long.parseLong(totalobj.getString("downloads")));
			Reporter.log("actual actualAuditReportTotal::"+actualAuditReportTotal,true);
			auditReport.setAuditReportTotals(actualAuditReportTotal);
		}

		//Risky_services
		if (reportObject.has("risky_services") && !reportObject.isNull("risky_services")) {
			JSONObject risky_services_Obj = reportObject.getJSONObject("risky_services");
			auditReportRiskyServices=new AuditReportRiskyServices();
			auditReportRiskyServices.setMed_risky_services(Long.parseLong(risky_services_Obj.getString("medium_risk_services")));
			auditReportRiskyServices.setUsers(Long.parseLong(risky_services_Obj.getString("users")));
			auditReportRiskyServices.setSessions(Long.parseLong(risky_services_Obj.getString("sessions")));
			auditReportRiskyServices.setDownloads(Long.parseLong(risky_services_Obj.getString("downloads")));
			auditReportRiskyServices.setMost_used_services(Long.parseLong(risky_services_Obj.getString("mostused_services")));
			auditReportRiskyServices.setLocations(Long.parseLong(risky_services_Obj.getString("locations")));
			auditReportRiskyServices.setTotal_services(Long.parseLong(risky_services_Obj.getString("total_services")));
			auditReportRiskyServices.setUploads(Long.parseLong(risky_services_Obj.getString("uploads")));
			auditReportRiskyServices.setHigh_risky_services(Long.parseLong(risky_services_Obj.getString("high_risk_services")));
			auditReportRiskyServices.setTotal_traffic(Long.parseLong(risky_services_Obj.getString("total_traffic")));
			//auditReportRiskyServices.setUsers(Long.parseLong(risky_services_Obj.getString("new_disco_services")));
			auditReportRiskyServices.setCategories(Long.parseLong(risky_services_Obj.getString("categories")));
			Reporter.log("actual auditReportRiskyServices::"+auditReportRiskyServices,true);
			auditReport.setAuditReportRiskyServices(auditReportRiskyServices);
		}
		//most_used_services
		if (reportObject.has("most_used_services") && !reportObject.isNull("most_used_services")) {
			JSONObject most_used_services = reportObject.getJSONObject("most_used_services");
			auditReportMostUsedServices=new AuditReportMostUsedServices();
			auditReportMostUsedServices.setMed_risky_services(Long.parseLong(most_used_services.getString("medium_risk_services")));
			auditReportMostUsedServices.setUsers(Long.parseLong(most_used_services.getString("users")));
			auditReportMostUsedServices.setSessions(Long.parseLong(most_used_services.getString("sessions")));
			auditReportMostUsedServices.setDownloads(Long.parseLong(most_used_services.getString("downloads")));
			auditReportMostUsedServices.setLocations(Long.parseLong(most_used_services.getString("locations")));
			auditReportMostUsedServices.setTotal_services(Long.parseLong(most_used_services.getString("total_services")));
			auditReportMostUsedServices.setUploads(Long.parseLong(most_used_services.getString("uploads")));
			auditReportMostUsedServices.setHigh_risky_services(Long.parseLong(most_used_services.getString("high_risk_services")));
			auditReportMostUsedServices.setTotal_traffic(Long.parseLong(most_used_services.getString("total_traffic")));
			auditReportMostUsedServices.setCategories(Long.parseLong(most_used_services.getString("categories")));
			auditReportMostUsedServices.setRiskyServices(auditReportMostUsedServices.getHigh_risky_services()+auditReportMostUsedServices.getMed_risky_services());
			Reporter.log("actual auditReportMostUsedServices::"+auditReportMostUsedServices,true);
			auditReport.setAuditMostUsedServices(auditReportMostUsedServices);

		}
		//servicedetails object
		if (reportObject.has("service_details") && !reportObject.isNull("service_details")) {
			JSONObject serviceDetailsObject = reportObject.getJSONObject("service_details");

			JSONArray serviceDetailsDataArray = serviceDetailsObject.getJSONArray("data");
			int serviceDetailsArrayLength =serviceDetailsDataArray.length();

			JSONArray serviceDataArray;
			List<AuditReportServiceDetails> auditReportServiceDetailsList=new ArrayList<AuditReportServiceDetails>();
			AuditReportServiceDetails  auditReportServiceDetails= null;
			for(int i=0; i<serviceDetailsArrayLength; i++)
			{
				auditReportServiceDetails=new AuditReportServiceDetails();
				serviceDataArray=(JSONArray)serviceDetailsDataArray.get(i);
				auditReportServiceDetails.setServiceId((String)serviceDataArray.getString(0));
				auditReportServiceDetails.setIs_new((String)serviceDataArray.getString(1));
				auditReportServiceDetails.setIs_Most_Used((String)serviceDataArray.getString(2));
				auditReportServiceDetails.setCat1((String)serviceDataArray.getString(3));
				auditReportServiceDetails.setCat2((String)serviceDataArray.getString(4));
				auditReportServiceDetails.setCat3((String)serviceDataArray.getString(5));
				auditReportServiceDetails.setCat4((String)serviceDataArray.getString(6));
				auditReportServiceDetails.setUsers_Count(new Long((String)serviceDataArray.getString(7)).longValue());
				auditReportServiceDetails.setUploads(new Long((String)serviceDataArray.getString(8)).longValue());
				auditReportServiceDetails.setDownloads(new Long((String)serviceDataArray.getString(9)).longValue());
				auditReportServiceDetails.setSessions(new Long((String)serviceDataArray.getString(10)).longValue());
				auditReportServiceDetails.setLocations_Count(new Long((String)serviceDataArray.getString(11)).longValue());
				auditReportServiceDetails.setService_Brr((String)serviceDataArray.getString(12));
				auditReportServiceDetails.setService_Url((String)serviceDataArray.getString(13));
				auditReportServiceDetailsList.add(auditReportServiceDetails);
			}
			auditReport.setAuditReportServiceDetailsList(auditReportServiceDetailsList);
		}
		return auditReport;
	}
	
	public static SortedSet<String> getDatasourceAnonyUsers(String sourceID,String resolution, String earliestDate, String latestDate) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("version", "1");		
		json.put("ignore_hidden", "true");		
		json.put("allowed", "true");
		json.put("blocked", "false");						
		json.put("ds_id",sourceID);
		json.put("resolution", resolution);
		json.put("earliest_date", earliestDate);
		json.put("latest_date", latestDate);
		
		Reporter.log("getDatasourceAnonyUsers payload: "+json,true);
			
		HttpResponse response  = AuditFunctions.getDSAnonyUsers(new Client(), new StringEntity(json.toString()));				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		JSONObject anonyUsersJsonObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response));			
		Reporter.log("Anony Users Json Object "+sourceID+"::"+anonyUsersJsonObject,true);
		
		
		JSONArray anonyUsersJsonArray = anonyUsersJsonObject.getJSONArray("data");
		int anonyUsersJsonArrayLength =anonyUsersJsonArray.length();
		Reporter.log("anony users length..."+anonyUsersJsonArrayLength,true);
		SortedSet<String> anonyUsersSet=new TreeSet<String>();
		JSONArray anonyUsersArrayObj=null;
		for(int i=0; i<anonyUsersJsonArrayLength; i++)
		{
			anonyUsersArrayObj=(JSONArray)anonyUsersJsonArray.getJSONArray(i);
			anonyUsersSet.add((String)anonyUsersArrayObj.getString(0));
		}
		Reporter.log("Anony users Set..."+anonyUsersSet,true);
		return anonyUsersSet;

	}
	public static SortedSet<String> getDatasourceAnonyUsersOfConsumerServices(String sourceID,String resolution, String earliestDate, String latestDate, boolean flag) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("version", "1");		
		json.put("ignore_hidden", "true");		
		json.put("allowed", "true");
		json.put("blocked", "false");						
		json.put("ds_id",sourceID);
		json.put("resolution", resolution);
		json.put("earliest_date", earliestDate);
		json.put("latest_date", latestDate);
		json.put("consumer", flag);
		
		Reporter.log("getDatasourceAnonyUsers payload: "+json,true);
			
		HttpResponse response  = AuditFunctions.getDSAnonyUsers(new Client(), new StringEntity(json.toString()));				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		JSONObject anonyUsersJsonObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response));			
		Reporter.log("Anony Users Json Object "+sourceID+"::"+anonyUsersJsonObject,true);
		
		
		JSONArray anonyUsersJsonArray = anonyUsersJsonObject.getJSONArray("data");
		int anonyUsersJsonArrayLength =anonyUsersJsonArray.length();
		Reporter.log("anony users length..."+anonyUsersJsonArrayLength,true);
		SortedSet<String> anonyUsersSet=new TreeSet<String>();
		JSONArray anonyUsersArrayObj=null;
		for(int i=0; i<anonyUsersJsonArrayLength; i++)
		{
			anonyUsersArrayObj=(JSONArray)anonyUsersJsonArray.getJSONArray(i);
			anonyUsersSet.add((String)anonyUsersArrayObj.getString(0));
		}
		Reporter.log("Anony users Set..."+anonyUsersSet,true);
		return anonyUsersSet;

	}
	
  public static HttpResponse getRevealUserFromAnonyUser(String anonyUserId, String dpoEmail, String sessionId) throws Exception
  {
	    JSONObject revealAnonyJson = new JSONObject();
	    revealAnonyJson.put("anon_id", anonyUserId);		
	    revealAnonyJson.put("dpo_email", dpoEmail);		
	    revealAnonyJson.put("sessionid", sessionId);
	    Reporter.log("payload.."+revealAnonyJson,true);
		
		HttpResponse response  = AuditFunctions.getRevealUserFromAnonyUser(new Client(), new StringEntity(revealAnonyJson.toString()));				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		
		return response;
		
  }
  public static SortedSet<String> getRevealUsersSet(SortedSet<String> actulAnonyUsersSet, String datasourceId, String firewallType, String dpoUsername, String sessionId) throws Exception
	{

		HttpResponse revealUserHttpResp=null;
		JSONObject revealUserJsonObj = null;
		String revealUsername=null;
		SortedSet<String> revealUsersSet=new TreeSet<String>();
		SoftAssert softAssert=new SoftAssert();
		Map<String,List<String>> revealUsersMap=new HashMap<String,List<String>>();
		List<String> revealUsesList=new ArrayList<String>();
		for(String anonyUserId:actulAnonyUsersSet)
		{
			revealUserHttpResp=getRevealUserFromAnonyUser(anonyUserId, dpoUsername, sessionId);
			revealUserJsonObj = (JSONObject) new JSONObject(ClientUtil.getResponseBody(revealUserHttpResp));
			Reporter.log("reveal user response::"+"anon-user."+anonyUserId+" is: "+revealUserJsonObj,true);
			softAssert.assertEquals(revealUserJsonObj.get("status"), "success","Expected Status is success but found:"+revealUserJsonObj.get("status"));
			softAssert.assertNotNull((String)revealUserJsonObj.get("user_name"), "Reveal Username should be not null ");
			revealUsername=(String)revealUserJsonObj.get("user_name");
			Reporter.log("reveal user of anonymized User:"+"anon-user."+anonyUserId+" is: "+revealUsername,true);
			revealUsersSet.add(revealUsername);
			revealUsesList.add("anon-user."+anonyUserId+"~"+revealUsername);
			
		}
		softAssert.assertAll();
		revealUsersMap.put(firewallType+"~"+datasourceId, revealUsesList);
		Reporter.log("Reveal Users Map:######################"+revealUsersMap,true);
		return revealUsersSet;
	}

	public static void compareExpectedUsersWithRevealUsers(SortedSet<String> expectedAnonyUsersSet,
			SortedSet<String> actulAnonyUsersSet)
	{
		SoftAssert softAssert=new SoftAssert();
		for(String actualUser:actulAnonyUsersSet)
		{

			softAssert.assertTrue(expectedAnonyUsersSet.contains(actualUser),"Reveal User "+actualUser +" not exist in the Expected Users List..");
			
		}
		softAssert.assertAll();
	}
	
 public static String getAuditSummaryNewPayload(String sourceID, String range) throws Exception
 {
	    JSONObject auditSummaryReqPayload = new JSONObject();
	    auditSummaryReqPayload.put("ds_id", sourceID);
	    auditSummaryReqPayload.put("version", "1");		
	    auditSummaryReqPayload.put("range", range);
	    auditSummaryReqPayload.put("ignore_hidden",true);						
	    auditSummaryReqPayload.put("blocked", false);
	    auditSummaryReqPayload.put("allowed", true);
		Reporter.log("Audit Summary Request payload:"+auditSummaryReqPayload.toString(),true); 
		return auditSummaryReqPayload.toString();
	
 }
public static String getAuditReportNewPayload(String sourceID, String range) throws Exception
{
	JSONObject auditReportReqPayload = new JSONObject();
	auditReportReqPayload.put("ds_id", sourceID);
	auditReportReqPayload.put("version", "1");		
	auditReportReqPayload.put("range", range);
	auditReportReqPayload.put("ignore_hidden",true);						
	auditReportReqPayload.put("blocked", false);
	auditReportReqPayload.put("allowed", true);
	Reporter.log("Audit Report Request payload:"+auditReportReqPayload.toString(),true);
	return auditReportReqPayload.toString();
}

public static String getAuditSummaryLatest(String sourceID, String resolution, String earliestDate, String latestDate) throws Exception
{
	    JSONObject auditSummaryReqPayload = new JSONObject();
	    auditSummaryReqPayload.put("ds_id", sourceID);
	    auditSummaryReqPayload.put("version", "1");		
	    auditSummaryReqPayload.put("resolution", resolution);
	    auditSummaryReqPayload.put("earliest_date", earliestDate);
        auditSummaryReqPayload.put("latest_date", latestDate);
	    auditSummaryReqPayload.put("ignore_hidden",true);						
	    auditSummaryReqPayload.put("blocked", false);
	    auditSummaryReqPayload.put("allowed", true);
		Reporter.log("Audit Summary Request Latest payload:"+auditSummaryReqPayload.toString(),true); 
		return auditSummaryReqPayload.toString();
}
public static String getAuditSummaryOfConsumerServices(String sourceID, String resolution, String earliestDate, String latestDate, boolean flag) throws Exception
{
	    JSONObject auditSummaryConsumerSerReqPayload = new JSONObject();
	    auditSummaryConsumerSerReqPayload.put("ds_id", sourceID);
	    auditSummaryConsumerSerReqPayload.put("version", "1");		
	    auditSummaryConsumerSerReqPayload.put("resolution", resolution);
	    auditSummaryConsumerSerReqPayload.put("earliest_date", earliestDate);
	    auditSummaryConsumerSerReqPayload.put("consumer", flag);
	    auditSummaryConsumerSerReqPayload.put("latest_date", latestDate);
	    auditSummaryConsumerSerReqPayload.put("ignore_hidden",true);						
	    auditSummaryConsumerSerReqPayload.put("blocked", false);
	    auditSummaryConsumerSerReqPayload.put("allowed", true);
		Reporter.log("Audit Summary Consumer Services Request payload:"+auditSummaryConsumerSerReqPayload.toString(),true); 
		return auditSummaryConsumerSerReqPayload.toString();
}
		
public static String getAuditSummaryNewPayloadForBlockedServicesLatest(String sourceID,  String resolution, String earliestDate, String latestDate) throws Exception
{
	    JSONObject auditSummaryReqPayload = new JSONObject();
	    auditSummaryReqPayload.put("ds_id", sourceID);
	    auditSummaryReqPayload.put("version", "1");		
	    auditSummaryReqPayload.put("resolution", resolution);
	    auditSummaryReqPayload.put("earliest_date", earliestDate);
        auditSummaryReqPayload.put("latest_date", latestDate);
	    auditSummaryReqPayload.put("ignore_hidden",true);						
	    auditSummaryReqPayload.put("blocked", true);
	    auditSummaryReqPayload.put("allowed", false);
		Reporter.log("Audit Summary Request payload:"+auditSummaryReqPayload.toString(),true); 
		return auditSummaryReqPayload.toString();
	
}

 public static String getAuditReportPayloadLatest(String sourceID, String resolution, String eariliestDate, String latestDate) throws Exception
{
	JSONObject auditReportReqPayload = new JSONObject();
	auditReportReqPayload.put("ds_id", sourceID);
	auditReportReqPayload.put("version", "1");		
	auditReportReqPayload.put("resolution", resolution);
	auditReportReqPayload.put("earliest_date", eariliestDate);
	auditReportReqPayload.put("latest_date", latestDate);
	auditReportReqPayload.put("ignore_hidden",true);						
	auditReportReqPayload.put("blocked", false);
	auditReportReqPayload.put("allowed", true);
	Reporter.log("Audit Report Request Latest payload:"+auditReportReqPayload.toString(),true);
	return auditReportReqPayload.toString();
}
 
 public static JSONObject getAuditMultipleDeviceidsSummaryPayloadLatest(String deviceLog,String sourceId,String deviceId,String resolution, String eariliestDate, String latestDate) throws Exception {
		
	//  String range = "1mo";	
	    
	    JSONObject json = new JSONObject();
	    json.put("version", "1");		
	    json.put("resolution", resolution);
	    json.put("earliest_date", eariliestDate);
	    json.put("latest_date", latestDate);
		json.put("ignore_hidden",true);						
		json.put("blocked", false);
		json.put("allowed", true);
		
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("resolution", resolution));
		queryParam.add(new BasicNameValuePair("earliest_date", eariliestDate));
		queryParam.add(new BasicNameValuePair("latest_date", latestDate));
		queryParam.add(new BasicNameValuePair("allowed", "true"));
		queryParam.add(new BasicNameValuePair("blocked", "false"));
		
		if(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS.equals(deviceLog) ||
				AuditTestConstants.FIREWALL_WSA_ACCESS_DEVICE_IDS.equals(deviceLog) ||
				AuditTestConstants.FIREWALL_WSA_W3C_DEVICE_IDS.equals(deviceLog) ||
				AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS.equals(deviceLog)||
				AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS.equals(deviceLog) ||
				AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS.equals(deviceLog) 
				
				) 
		{
			queryParam.add(new BasicNameValuePair("ds_id", sourceId));
			json.put("ds_id", sourceId);
			Reporter.log("payload: of main "+json,true);
		}
		else{
			json.put("ds_id", sourceId);
			json.put("logsource_ids", deviceId);
			Reporter.log("payload:of deviceid "+json,true);
		}
		return json;
	}

 public static JSONObject getAuditMultipleDeviceidsSummaryorReportBySelectingAllSourceIdsPayloadLatest(String sourceId,ArrayList<String> deviceLogsData,String resolution, String eariliestDate, String latestDate) throws Exception {
		
	    String range = "1mo";	
	    
	    JSONObject json = new JSONObject();
	    json.put("version", "1");		
	    json.put("resolution", resolution);
	    json.put("earliest_date", eariliestDate);
	    json.put("latest_date", latestDate);
		json.put("ignore_hidden",true);						
		json.put("blocked", false);
		json.put("allowed", true);
		json.put("ds_id", sourceId);
		if(deviceLogsData.contains(sourceId))
			deviceLogsData.remove(sourceId);
		json.put("logsource_ids", StringUtils.join(deviceLogsData, ","));
		
	    Reporter.log("getAuditMultipleDeviceidsSummaryBySelectingAllSourceIdsPayload:"+json,true);
		return json;
	}



public static JSONObject populateMultipleDevicesActualAuditSummary(Client restClient,JSONObject multipleDeviceIdsRequestPayload ) throws Exception
{
	JSONObject summaryObject=null;
	HttpResponse response =null;
	
	response  = AuditFunctions.getSummary(restClient,new StringEntity(multipleDeviceIdsRequestPayload.toString()));				
	Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
	
	String strResponse=ClientUtil.getResponseBody(response);
	JSONArray jsonarray = new JSONArray(strResponse);
	summaryObject = jsonarray.getJSONObject(0);	
	return summaryObject;
	
}

public static JSONObject populateMultipleDevicesActualAuditReport(Client restClient,JSONObject multipleDeviceIdsRequestPayload ) throws Exception
{
	JSONObject reportObject=null;
	HttpResponse response =null;
	
	response  = AuditFunctions.getAuditReport(restClient,new StringEntity(multipleDeviceIdsRequestPayload.toString()));				
	Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
	
	String strResponse=ClientUtil.getResponseBody(response);
	JSONArray jsonarray = new JSONArray(strResponse);
	reportObject = jsonarray.getJSONObject(0);		
	return reportObject;
	
}
	

public static AuditSummary populateActualAuditSummaryObject(String fireWallType, String sourceID, String resolution, String earliestDate, String latestDate ) throws Exception
{
	String range ="";
	JSONObject summaryObject=null;
	HttpResponse response =null;
	
	response  = AuditFunctions.getSummary(new Client(), new StringEntity(AuditTestUtils.getAuditSummaryLatest(sourceID,resolution,earliestDate,latestDate)));				
	Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
	

	String strResponse=ClientUtil.getResponseBody(response);
	JSONArray jsonarray = new JSONArray(strResponse);
	summaryObject = jsonarray.getJSONObject(0);	

	
	//summary validation
				
	Reporter.log("Actual Audit summar Api Response: "+fireWallType+"_"+sourceID+"::"+summaryObject,true);
	String summaryLabel;

	HashMap<String, String> actuaSserviceBrrRateMap=new HashMap<String, String>();

	AuditSummary actualAuditSummaryObj=new AuditSummary();
	JSONObject companyBrr = summaryObject.getJSONObject("company_brr");
	String actual_audit_score= companyBrr.getString("value"); 
	actualAuditSummaryObj.setAuditScore(actual_audit_score);

	summaryLabel="total_services";
	String actual_audit_saasservices=summaryObject.getString(summaryLabel);
	actualAuditSummaryObj.setSaas_services_count(Integer.parseInt(actual_audit_saasservices));

	summaryLabel="total_destinations";
	String actual_audit_destinations=summaryObject.getString(summaryLabel);
	actualAuditSummaryObj.setDestination_count(Integer.parseInt(actual_audit_destinations));

	summaryLabel="total_users";
	String actual_audit_users=summaryObject.getString(summaryLabel);
	actualAuditSummaryObj.setUsers_count(Integer.parseInt(actual_audit_users));

	//Risky services
	String label="top_risky_services";
	int topRiskyServicesArrayLength=0;
	String top_risky_serviceBRR=null;
	String serviceId=null;
	String userCount=null;
	String serviceName=null;
	AuditSummaryTopRiskyServices auditSummaryTopRiskyServices=null;
	List<AuditSummaryTopRiskyServices> auditSummaryTopRiskyServicesList=new ArrayList<AuditSummaryTopRiskyServices>();
	Set<String> setServices=new HashSet<String>();
	List<String> totalAuditServicesList=new ArrayList<String>();;


	if (summaryObject.has(label) && !summaryObject.isNull(label)) {
		JSONArray topRiskyServicesArray = summaryObject.getJSONArray(label);
		topRiskyServicesArrayLength =topRiskyServicesArray.length();


		for(int i=0; i<topRiskyServicesArrayLength; i++)
		{
			auditSummaryTopRiskyServices= new AuditSummaryTopRiskyServices();
			serviceId=((JSONObject)topRiskyServicesArray.get(i)).getString("service_id");
			serviceName=AuditTestUtils.getServiceName(serviceId);
			auditSummaryTopRiskyServices.setServicename(serviceName);
			//setServices.add(serviceName);

			top_risky_serviceBRR=((JSONObject)topRiskyServicesArray.get(i)).getString("sort_key_brr");
			auditSummaryTopRiskyServices.setService_brr(top_risky_serviceBRR);

			userCount=((JSONObject)topRiskyServicesArray.get(i)).getString("users_count");
			auditSummaryTopRiskyServices.setService_user_count(Integer.parseInt(userCount));
			auditSummaryTopRiskyServicesList.add(auditSummaryTopRiskyServices);
			actuaSserviceBrrRateMap.put(serviceName, top_risky_serviceBRR);


		}
	}

	actualAuditSummaryObj.setSummaryTopRiskyServicesList(auditSummaryTopRiskyServicesList);

	//top used services
	label="top_used_services";
	int topUsedServicesArrayLength=0;

	if (summaryObject.has(label) && !summaryObject.isNull(label)) {
		JSONArray topUsedServicesArray = summaryObject.getJSONArray(label);
		topUsedServicesArrayLength =topUsedServicesArray.length();

		for(int i=0; i<topUsedServicesArrayLength; i++)
		{
			serviceId=((JSONObject)topUsedServicesArray.get(i)).getString("service_id");
			serviceName=AuditTestUtils.getServiceName(serviceId);
			//setServices.add(serviceName);

		}

	}

	SummaryTabDto summaryTabDto=AuditTestUtils.getServicesTabData(sourceID,resolution, earliestDate,latestDate);
	for(String serviceID:summaryTabDto.getServiceUsersMap().keySet())
	{
		serviceName=AuditTestUtils.getServiceName(serviceID);
		setServices.add(serviceName);
	}

	actualAuditSummaryObj.setTotalAuditServicesList(new ArrayList<>(setServices));
	
	
	label="high_risk_services";
	String actual_audit_high_risky_services=summaryObject.getString(label);
	actualAuditSummaryObj.setHigh_risky_services_count(Integer.parseInt(actual_audit_high_risky_services));


	label="med_risk_services";
	String actual_audit_med_risk_services=summaryObject.getString(label);
	actualAuditSummaryObj.setMed_risky_services_count(Integer.parseInt(actual_audit_med_risk_services));
	actualAuditSummaryObj.setServiceBrrMap(actuaSserviceBrrRateMap);

	return actualAuditSummaryObj;

}

//for consumer services
public static AuditSummary populateActualAuditSummaryofConsumerServices(String fireWallType, String sourceID, String resolution, String earliestDate, String latestDate ) throws Exception
{
	JSONObject summaryObject=null;
	HttpResponse response =null;
	
	response  = AuditFunctions.getSummary(new Client(), new StringEntity(AuditTestUtils.getAuditSummaryOfConsumerServices(sourceID,resolution,earliestDate,latestDate,true)));				
	Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
	

	String strResponse=ClientUtil.getResponseBody(response);
	JSONArray jsonarray = new JSONArray(strResponse);
	summaryObject = jsonarray.getJSONObject(0);	

	
	//summary validation
				
	Reporter.log("Actual Audit summar Api Response: "+fireWallType+"_"+sourceID+"::"+summaryObject,true);
	String summaryLabel;

	AuditSummary actualAuditSummaryObj=new AuditSummary();
	JSONObject companyBrr = summaryObject.getJSONObject("company_brr");
	String actual_audit_score= companyBrr.getString("value"); 
	actualAuditSummaryObj.setAuditScore(actual_audit_score);

	summaryLabel="total_services";
	String actual_audit_saasservices=summaryObject.getString(summaryLabel);
	actualAuditSummaryObj.setSaas_services_count(Integer.parseInt(actual_audit_saasservices));

	summaryLabel="total_destinations";
	String actual_audit_destinations=summaryObject.getString(summaryLabel);
	actualAuditSummaryObj.setDestination_count(Integer.parseInt(actual_audit_destinations));

	summaryLabel="total_users";
	String actual_audit_users=summaryObject.getString(summaryLabel);
	actualAuditSummaryObj.setUsers_count(Integer.parseInt(actual_audit_users));

	//Risky services
	String label="top_risky_services";
	int topRiskyServicesArrayLength=0;
	String top_risky_serviceBRR=null;
	String serviceId=null;
	String userCount=null;
	String serviceName=null;
	AuditSummaryTopRiskyServices auditSummaryTopRiskyServices=null;
	List<AuditSummaryTopRiskyServices> auditSummaryTopRiskyServicesList=new ArrayList<AuditSummaryTopRiskyServices>();
	Set<String> setServices=new HashSet<String>();
	List<String> totalAuditServicesList=new ArrayList<String>();;


	if (summaryObject.has(label) && !summaryObject.isNull(label)) {
		JSONArray topRiskyServicesArray = summaryObject.getJSONArray(label);
		topRiskyServicesArrayLength =topRiskyServicesArray.length();


		for(int i=0; i<topRiskyServicesArrayLength; i++)
		{
			auditSummaryTopRiskyServices= new AuditSummaryTopRiskyServices();
			serviceId=((JSONObject)topRiskyServicesArray.get(i)).getString("service_id");
			serviceName=AuditTestUtils.getServiceName(serviceId);
			auditSummaryTopRiskyServices.setServicename(serviceName);
			//setServices.add(serviceName);

			top_risky_serviceBRR=((JSONObject)topRiskyServicesArray.get(i)).getString("sort_key_brr");
			auditSummaryTopRiskyServices.setService_brr(top_risky_serviceBRR);

			userCount=((JSONObject)topRiskyServicesArray.get(i)).getString("users_count");
			auditSummaryTopRiskyServices.setService_user_count(Integer.parseInt(userCount));
			auditSummaryTopRiskyServicesList.add(auditSummaryTopRiskyServices);

		}
	}

	actualAuditSummaryObj.setSummaryTopRiskyServicesList(auditSummaryTopRiskyServicesList);

	//top used services
	label="top_used_services";
	int topUsedServicesArrayLength=0;

	if (summaryObject.has(label) && !summaryObject.isNull(label)) {
		JSONArray topUsedServicesArray = summaryObject.getJSONArray(label);
		topUsedServicesArrayLength =topUsedServicesArray.length();

		for(int i=0; i<topUsedServicesArrayLength; i++)
		{
			serviceId=((JSONObject)topUsedServicesArray.get(i)).getString("service_id");
			serviceName=AuditTestUtils.getServiceName(serviceId);
			//setServices.add(serviceName);

		}

	}


	//getConsumer Services 
	
	SummaryTabDto consumerSummaryDto=AuditTestUtils.getConsumerServicesTabData(sourceID,resolution, earliestDate,latestDate,true);
	for(String serviceID:consumerSummaryDto.getServiceUsersMap().keySet())
	{
		serviceName=AuditTestUtils.getServiceName(serviceID);
		setServices.add(serviceName);
	}
	actualAuditSummaryObj.setTotalAuditServicesList(new ArrayList<>(setServices));
	
	
	label="high_risk_services";
	String actual_audit_high_risky_services=summaryObject.getString(label);
	actualAuditSummaryObj.setHigh_risky_services_count(Integer.parseInt(actual_audit_high_risky_services));


	label="med_risk_services";
	String actual_audit_med_risk_services=summaryObject.getString(label);
	actualAuditSummaryObj.setMed_risky_services_count(Integer.parseInt(actual_audit_med_risk_services));

	return actualAuditSummaryObj;

}
public static AuditReport populateActualAuditReportData(String fireWallType,String sourceID, String resolution, String earliestDate, String latestDate) throws Exception
{

	String range="";
	JSONObject reportObject=null;
	HttpResponse response=null;
			
			
	response  = AuditFunctions.getAuditReport(new Client(), new StringEntity(AuditTestUtils.getAuditReportPayloadLatest(sourceID,resolution,earliestDate,latestDate)));				
	Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
	String strResponse=ClientUtil.getResponseBody(response);
	JSONArray jsonarray = new JSONArray(strResponse);
	reportObject = jsonarray.getJSONObject(0);	
	
	
	String dsStatus=null;
	AuditReportTotals actualAuditReportTotal=null;
	AuditReportRiskyServices auditReportRiskyServices=null;
	AuditReportMostUsedServices auditReportMostUsedServices=null;
	AuditReportServiceCategories auditReportServiceCategories=null;
	AuditReportNewDiscoveredServices auditReportNewDiscoveredServices=null;
	AuditReport auditReport=new AuditReport();


	//Totals
	if (reportObject.has("total") && !reportObject.isNull("total")) {
		JSONObject totalobj = reportObject.getJSONObject("total");
		actualAuditReportTotal=new AuditReportTotals();
		actualAuditReportTotal.setUsers(Long.parseLong(totalobj.getString("users")));
		actualAuditReportTotal.setSessions(Long.parseLong(totalobj.getString("sessions")));
		actualAuditReportTotal.setServices(Long.parseLong(totalobj.getString("services")));
		actualAuditReportTotal.setLocations(Long.parseLong(totalobj.getString("locations")));
		actualAuditReportTotal.setCategories(Long.parseLong(totalobj.getString("categories")));
		actualAuditReportTotal.setTraffic(Long.parseLong(totalobj.getString("traffic")));
		actualAuditReportTotal.setUploads(Long.parseLong(totalobj.getString("uploads")));
		actualAuditReportTotal.setDownloads(Long.parseLong(totalobj.getString("downloads")));
		Reporter.log("actual actualAuditReportTotal::"+actualAuditReportTotal,true);

		auditReport.setAuditReportTotals(actualAuditReportTotal);
	}

	//Risky_services
	if (reportObject.has("risky_services") && !reportObject.isNull("risky_services")) {
		JSONObject risky_services_Obj = reportObject.getJSONObject("risky_services");
		auditReportRiskyServices=new AuditReportRiskyServices();

		auditReportRiskyServices.setMed_risky_services(Long.parseLong(risky_services_Obj.getString("medium_risk_services")));
		auditReportRiskyServices.setUsers(Long.parseLong(risky_services_Obj.getString("users")));
		auditReportRiskyServices.setSessions(Long.parseLong(risky_services_Obj.getString("sessions")));
		auditReportRiskyServices.setDownloads(Long.parseLong(risky_services_Obj.getString("downloads")));
		auditReportRiskyServices.setMost_used_services(Long.parseLong(risky_services_Obj.getString("mostused_services")));
		auditReportRiskyServices.setLocations(Long.parseLong(risky_services_Obj.getString("locations")));
		auditReportRiskyServices.setTotal_services(Long.parseLong(risky_services_Obj.getString("total_services")));
		auditReportRiskyServices.setUploads(Long.parseLong(risky_services_Obj.getString("uploads")));
		auditReportRiskyServices.setHigh_risky_services(Long.parseLong(risky_services_Obj.getString("high_risk_services")));
		auditReportRiskyServices.setTotal_traffic(Long.parseLong(risky_services_Obj.getString("total_traffic")));
		//auditReportRiskyServices.setUsers(Long.parseLong(risky_services_Obj.getString("new_disco_services")));
		auditReportRiskyServices.setCategories(Long.parseLong(risky_services_Obj.getString("categories")));
		Reporter.log("actual auditReportRiskyServices::"+auditReportRiskyServices,true);

		auditReport.setAuditReportRiskyServices(auditReportRiskyServices);
	}

	//most_used_services

	if (reportObject.has("most_used_services") && !reportObject.isNull("most_used_services")) {
		JSONObject most_used_services = reportObject.getJSONObject("most_used_services");
		auditReportMostUsedServices=new AuditReportMostUsedServices();

		auditReportMostUsedServices.setMed_risky_services(Long.parseLong(most_used_services.getString("medium_risk_services")));
		auditReportMostUsedServices.setUsers(Long.parseLong(most_used_services.getString("users")));
		auditReportMostUsedServices.setSessions(Long.parseLong(most_used_services.getString("sessions")));
		auditReportMostUsedServices.setDownloads(Long.parseLong(most_used_services.getString("downloads")));
		auditReportMostUsedServices.setLocations(Long.parseLong(most_used_services.getString("locations")));
		auditReportMostUsedServices.setTotal_services(Long.parseLong(most_used_services.getString("total_services")));
		auditReportMostUsedServices.setUploads(Long.parseLong(most_used_services.getString("uploads")));
		auditReportMostUsedServices.setHigh_risky_services(Long.parseLong(most_used_services.getString("high_risk_services")));
		auditReportMostUsedServices.setTotal_traffic(Long.parseLong(most_used_services.getString("total_traffic")));
		auditReportMostUsedServices.setCategories(Long.parseLong(most_used_services.getString("categories")));
		auditReportMostUsedServices.setRiskyServices(auditReportMostUsedServices.getHigh_risky_services()+auditReportMostUsedServices.getMed_risky_services());
		Reporter.log("actual auditReportMostUsedServices::"+auditReportMostUsedServices,true);
		auditReport.setAuditMostUsedServices(auditReportMostUsedServices);

	}

	//servicedetails object
	if (reportObject.has("service_details") && !reportObject.isNull("service_details")) {
		JSONObject serviceDetailsObject = reportObject.getJSONObject("service_details");

		JSONArray serviceDetailsDataArray = serviceDetailsObject.getJSONArray("data");
		int serviceDetailsArrayLength =serviceDetailsDataArray.length();

		JSONArray serviceDataArray;
		List<AuditReportServiceDetails> auditReportServiceDetailsList=new ArrayList<AuditReportServiceDetails>();
		AuditReportServiceDetails  auditReportServiceDetails= null;
		for(int i=0; i<serviceDetailsArrayLength; i++)
		{
			auditReportServiceDetails=new AuditReportServiceDetails();
			serviceDataArray=(JSONArray)serviceDetailsDataArray.get(i);
			auditReportServiceDetails.setServiceId((String)serviceDataArray.getString(0));
			auditReportServiceDetails.setIs_new((String)serviceDataArray.getString(1));
			auditReportServiceDetails.setIs_Most_Used((String)serviceDataArray.getString(2));
			auditReportServiceDetails.setCat1((String)serviceDataArray.getString(3));
			auditReportServiceDetails.setCat2((String)serviceDataArray.getString(4));
			auditReportServiceDetails.setCat3((String)serviceDataArray.getString(5));
			auditReportServiceDetails.setCat4((String)serviceDataArray.getString(6));
			auditReportServiceDetails.setUsers_Count(new Long((String)serviceDataArray.getString(7)).longValue());
			auditReportServiceDetails.setUploads(new Long((String)serviceDataArray.getString(8)).longValue());
			auditReportServiceDetails.setDownloads(new Long((String)serviceDataArray.getString(9)).longValue());
			auditReportServiceDetails.setSessions(new Long((String)serviceDataArray.getString(10)).longValue());
			auditReportServiceDetails.setLocations_Count(new Long((String)serviceDataArray.getString(11)).longValue());
			auditReportServiceDetails.setService_Brr((String)serviceDataArray.getString(12));
			auditReportServiceDetails.setService_Url((String)serviceDataArray.getString(13));
			auditReportServiceDetailsList.add(auditReportServiceDetails);


		}
		auditReport.setAuditReportServiceDetailsList(auditReportServiceDetailsList);
	}
	
	//new services object preparation
			if (reportObject.has("new_discovered_services") && !reportObject.isNull("new_discovered_services")) {
				JSONObject newDiscoveredServicesObj = reportObject.getJSONObject("new_discovered_services");
				auditReportNewDiscoveredServices=new AuditReportNewDiscoveredServices();
				
				auditReportNewDiscoveredServices.setMedium_risk_services(Long.parseLong(newDiscoveredServicesObj.getString("medium_risk_services")));
				auditReportNewDiscoveredServices.setUsers(Long.parseLong(newDiscoveredServicesObj.getString("users")));
				auditReportNewDiscoveredServices.setSessions(Long.parseLong(newDiscoveredServicesObj.getString("sessions")));
				auditReportNewDiscoveredServices.setDownloads(Long.parseLong(newDiscoveredServicesObj.getString("downloads")));
				auditReportNewDiscoveredServices.setMostused_services(Long.parseLong(newDiscoveredServicesObj.getString("mostused_services")));
				auditReportNewDiscoveredServices.setLocations(Long.parseLong(newDiscoveredServicesObj.getString("locations")));
				auditReportNewDiscoveredServices.setTotal_services(Long.parseLong(newDiscoveredServicesObj.getString("total_services")));
				auditReportNewDiscoveredServices.setUploads(Long.parseLong(newDiscoveredServicesObj.getString("uploads")));
				auditReportNewDiscoveredServices.setHigh_risk_services(Long.parseLong(newDiscoveredServicesObj.getString("high_risk_services")));
				auditReportNewDiscoveredServices.setTotal_traffic(Long.parseLong(newDiscoveredServicesObj.getString("total_traffic")));
				auditReportNewDiscoveredServices.setCategories(Long.parseLong(newDiscoveredServicesObj.getString("categories")));
				
				Reporter.log("actual auditReportNewDiscoveredServices::"+auditReportNewDiscoveredServices,true);

				auditReport.setAuditReportNewDiscoveredServices(auditReportNewDiscoveredServices);
			}




	return auditReport;

}

 
}