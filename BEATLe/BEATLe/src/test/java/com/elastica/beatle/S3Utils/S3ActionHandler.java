/**
 * 
 */
package com.elastica.beatle.S3Utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.Reporter;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class S3ActionHandler {

	/**
	 * @param accessKey
	 * @param secretKey
	 * @param endPoint
	 * @return
	 */
	private AmazonS3 getS3Connection(String accessKey, String secretKey, String endPoint){
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);

		AmazonS3 conn = new AmazonS3Client(credentials, clientConfig);
		conn.setEndpoint(endPoint);
		return conn;
	}

	/**
	 * @param bucketName
	 * @param folderName
	 */
	public synchronized void createFolder(String bucketName, String folderName) {		
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);
		AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		conn.putObject(new PutObjectRequest(bucketName,folderName + "/", new ByteArrayInputStream(new byte[0]), metadata));
	}

	/**
	 * @param bucketName
	 * @param folderPath
	 * @param fileName
	 * @param fileBody
	 * @throws IOException
	 */
	public synchronized void uploadLogFileToS3Sanity(String bucketName, String folderPath, String fileName,File fileBody) throws IOException{		
		Logger.info("Uploading file "+fileName+" to S3 in bucket "+bucketName +" with prefix "+folderPath);
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);		
		AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));		
		if(conn.doesBucketExist(bucketName)){
			conn.putObject(new PutObjectRequest(bucketName, folderPath+fileName, fileBody));
		}
	}

	/**
	 * @param bucketName
	 * @param folderPath
	 * @param fileName
	 * @param fileBody
	 * @throws IOException
	 */
	public synchronized void uploadLogFileToS3(String bucketName, String folderPath, String fileName,File fileBody) throws IOException{
		Logger.info("Uploading file "+fileName+" to S3 in bucket "+bucketName +" with prefix "+folderPath);
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);
		AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));		
		if(conn.doesBucketExist(bucketName)){
			ObjectListing objects = conn.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderPath));
			if(!objects.getObjectSummaries().isEmpty()){
				conn.putObject(new PutObjectRequest(bucketName, folderPath+fileName, fileBody));
			}
			else{
				Logger.info("The specified FolderPath not found: "+folderPath);	}			
		}	


	}

	/**
	 * @param bucketName
	 * @param folderPath
	 * @param fileName
	 * @throws IOException
	 */
	public synchronized void deleteLogFileFromS3(String bucketName, String folderPath, String fileName) throws IOException{
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);
		AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));		
		if(conn.doesBucketExist(bucketName)){
			ObjectListing objects = conn.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderPath));
			if(!objects.getObjectSummaries().isEmpty()){
				conn.deleteObject(bucketName,folderPath+fileName);
			}
			else
				Logger.info("The specified FolderPath not found: "+folderPath);				
		}				
	}



	/**
	 * @param bucketName
	 * @param folderPath
	 * @param filename
	 * @return 
	 * @throws IOException
	 */
	public synchronized URL downloadFileFromS3(String bucketName, String folderPath, String filename) throws IOException{
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);
		AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));
		if(conn.doesBucketExist(bucketName)){
			GeneratePresignedUrlRequest request=new GeneratePresignedUrlRequest(bucketName+folderPath, s3Properties.get("S3AccessKey"), HttpMethod.GET);
			request.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
			return conn.generatePresignedUrl(request);
		}
		else
			return null;
	}	
	
	public synchronized boolean downloadFileFromS3Bucket(String bucketName, String sourcePath, String destinationPath) throws IOException{
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);
		AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));
		if(conn.doesBucketExist(bucketName)){
			File destinationFile = new File(destinationPath);
			conn.getObject(new GetObjectRequest(bucketName, sourcePath), destinationFile);
			
			return destinationFile.exists();
		}else{
			Logger.info("S3 Bucket:"+bucketName+" does not exist");
			return false;
		}
	}	
	
	
	public synchronized List<String> downloadFolderFromS3Bucket(String bucketName, String sourcePath,
			String destinationPath) {
	    String delimiter = "/";
	    if (!sourcePath.endsWith(delimiter)) {
	    	sourcePath += delimiter;
	    }
	    Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);
		AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));
		
	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	            .withBucketName(bucketName).withPrefix(sourcePath)
	            /*.withDelimiter(delimiter)*/;
	    ObjectListing objects = conn.listObjects(listObjectsRequest);
	    for(S3ObjectSummary object:objects.getObjectSummaries()){
	    	if(object.getKey().endsWith(delimiter)){
	    		Logger.info(object.getKey()+" is a folder");
	    	}else{
	    		Logger.info("File is being downloaded from "+
	    				sourcePath+" to "+destinationPath+File.separator+
	    				object.getKey().replaceAll("^.*?/", ""));
	    		File destinationFile = new File(destinationPath
	    				+File.separator+object.getKey().replaceAll("^.*?/", ""));
	    		
				conn.getObject(new GetObjectRequest(bucketName, object.getKey()), destinationFile);
	    	}
	    }
	    return objects.getCommonPrefixes();
	}
	
	/*
	 * Downloads only one file specified in source path
	 */
	
	public synchronized void downloadFilesFromFolder(String bucket, String sourcePath, File destinationFile) throws InterruptedException{
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);		
		try{
			AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));
			Download download = new TransferManager(conn).download(new GetObjectRequest(bucket, sourcePath),destinationFile);
			while(download.getProgress().getTotalBytesToTransfer() == download.getProgress().getBytesTransferred()){
				Thread.sleep(30000);
				Logger.info("Waiting for files to download");
			}
			Logger.info("File transfer completed");
		}catch(AmazonServiceException e){
			Logger.error("Caught an AmazonServiceException");
            Logger.error("Error Message:    " + e.getMessage());
            Logger.error("HTTP Status Code: " + e.getStatusCode());
            Logger.error("AWS Error Code:   " + e.getErrorCode());
            Logger.error("Error Type:       " + e.getErrorType());
            Logger.error("Request ID:       " + e.getRequestId());
		}
	}
	
	/*
	 * Downloads whole folder specified in source to destination folder 
	 */
	
	public synchronized void downloadWholeFolder(String bucket, String sourcePath, File destinationFolder) throws InterruptedException{
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);
		try{
			AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));
			MultipleFileDownload download = new TransferManager(conn).downloadDirectory(bucket, sourcePath, destinationFolder);
			while(download.getProgress().getTotalBytesToTransfer() == download.getProgress().getBytesTransferred()){
				Thread.sleep(30000);
				Logger.info("Waiting for files to download");
			}
			Logger.info("File transfer completed");
			
		}catch(AmazonServiceException e){
			Logger.error("Caught an AmazonServiceException");
            Logger.error("Error Message:    " + e.getMessage());
            Logger.error("HTTP Status Code: " + e.getStatusCode());
            Logger.error("AWS Error Code:   " + e.getErrorCode());
            Logger.error("Error Type:       " + e.getErrorType());
            Logger.error("Request ID:       " + e.getRequestId());
		}
	}

	public synchronized String readFromS3(String bucketName, String key) throws IOException {
		String prevRunDsID="";
		Map<String, String> s3Properties = FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_S3_PROPERTIES_FILEPATH);
		AmazonS3 conn = getS3Connection(s3Properties.get("S3AccessKey"),s3Properties.get("S3AccessSecret"),s3Properties.get("S3HostName"));
		S3Object s3object = conn.getObject(new GetObjectRequest(bucketName, key));
        BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
		String line;
		while((line = reader.readLine()) != null) {
			prevRunDsID=line;
		}
		return prevRunDsID;
	}
	
	public static void readBackUpRestoreFileFromS3AndKeepIntoTempLocation(String bucketName, String key, String fileName) throws IOException {
		try{
		S3ActionHandler s3 = new S3ActionHandler();
		s3.downloadFileFromS3Bucket(bucketName, key, 
				AuditTestConstants.AUDIT_BACKUP_RESTORE_FILE_TEMP_PATH+File.separator+fileName);
		Reporter.log("Complited downloading the file for S3 ::"+fileName,true);			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}