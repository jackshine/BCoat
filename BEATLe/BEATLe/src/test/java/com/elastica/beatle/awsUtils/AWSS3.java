/**
 * 
 */
package com.elastica.beatle.awsUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Iterator;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AWSS3  extends AWSClient{
	
	private AmazonS3 s3handler;
	private final static String FOLDER_SUFFIX = "/";
	
	/**
	 * @param accessKey
	 * @param accessKeySecret
	 * @throws Exception
	 */
	public AWSS3(String accessKey,String accessKeySecret) throws Exception {
		super(accessKey, accessKeySecret);
		s3handler = new AmazonS3Client(credentials);
	}
	
	/**
	 * @param bucketName
	 * @param folderPath
	 * @param fileName
	 */
	public void deleteS3File(String bucketName, String folderPath, String fileName){
		try{
			if(s3handler.doesBucketExist(bucketName)){
				ObjectListing objects = s3handler.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderPath));
				if(!objects.getObjectSummaries().isEmpty()){
					try{
						s3handler.deleteObject(bucketName,folderPath+fileName);
					}catch(AmazonServiceException exception){
						printException(exception);
					}
				}	
				else
					Logger.info("No objects found in the bucket/folderpath specified");
			}
			else
				Logger.info("Bucket not found to upload the file");
		}catch(AmazonServiceException exception){
			printException(exception);
		}		
	}
	
	/**
	 * @param bucketName
	 * @param folderPath
	 * @param fileName
	 * @return
	 */
	public S3ObjectInputStream downloadS3File(String bucketName, String folderPath, String fileName){
		try{
			if(s3handler.doesBucketExist(bucketName)){
				try{
					ObjectListing objects = s3handler.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderPath));
					if(!objects.getObjectSummaries().isEmpty()){
						for(S3ObjectSummary objectsummary: objects.getObjectSummaries())
							if(objectsummary.getKey().contains(fileName)){
								S3Object s3object = s3handler.getObject(new GetObjectRequest(bucketName, folderPath+fileName));
								return s3object.getObjectContent();
							}
					}				
				}
				catch(AmazonServiceException exception){
					printException(exception);
				}
			}
			else
				Logger.info("Bucket not found to upload the file");
		}catch(AmazonServiceException exception){
			printException(exception);
		}
		return null;
	}
	
	/**
	 * @param bucketName
	 * @param folderPath: This value can be null if we are uploading the file directly in Bucket without creating any folders inside the bucket.
	 * @param fileName
	 * @param fileBody
	 */
	public void uploadS3File(String bucketName, String folderPath, String fileName, File fileBody){
		Logger.info("Uploading file "+ fileName+" to S3 under "+bucketName);
		try{
			if(s3handler.doesBucketExist(bucketName)){
				ObjectListing objects;
				if(folderPath != null){
					objects = s3handler.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderPath));
					if(!objects.getObjectSummaries().isEmpty())
						s3handler.putObject(new PutObjectRequest(bucketName, folderPath+fileName, fileBody));					
					else
						Logger.info("The specified FolderPath not found: "+folderPath);
				}					
				else{					
					s3handler.putObject(new PutObjectRequest(bucketName, fileName, fileBody));
				}											
			}	
			else
				Logger.info("Bucket not found to upload the file");
		}catch(AmazonServiceException exception){
			printException(exception);
		}
	}
	
	/**
	 * @param bucketName
	 * @throws Exception
	 */
	public void createBucket(String bucketName) throws Exception{
		Logger.info("Creating bucket");
		try{
			if(!s3handler.doesBucketExist(bucketName))
				s3handler.createBucket(bucketName);					
			else
				Logger.info("This bucket already present");
		}catch(AmazonServiceException exception){
			printException(exception);
		}		
	}
	
	/**
	 * @param bucketName
	 * @throws Exception
	 */
	public void deleteBucket(String bucketName) throws Exception{
		Logger.info("Creating bucket");
		try{
			if(s3handler.doesBucketExist(bucketName)){
				ObjectListing objects = s3handler.listObjects(new ListObjectsRequest().withBucketName(bucketName));
				if(objects.getObjectSummaries().isEmpty()){
					s3handler.deleteBucket(bucketName);
				}
			}							
			else
				Logger.info("This bucket doesn't exist");
		}catch(AmazonServiceException exception){
			printException(exception);
		}
	}	
	
	/**
	 * @param bucketName
	 * @param folderName
	 * @throws Exception
	 */
	public void createFolder(String bucketName, String folderName) throws Exception {
		Logger.info("Creating Folder"+ folderName +" under bucket "+bucketName);
		try{
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);
			PutObjectRequest putObjectRequest =  new PutObjectRequest(bucketName, folderName+FOLDER_SUFFIX, new ByteArrayInputStream(new byte[0]), metadata);
			s3handler.putObject(putObjectRequest);
		}catch(AmazonServiceException exception){
			printException(exception);
		}		
	}
	
	/**
	 * @param bucketName
	 * @param folderName
	 * @throws Exception
	 */
	public void deleteFolder(String bucketName, String folderName) throws Exception {
		Logger.info("Deleting folder "+folderName+ "under "+bucketName);
		try{
			if(s3handler.doesBucketExist(bucketName)){
				ObjectListing objects = s3handler.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderName));
				if(objects.getObjectSummaries().isEmpty())
					s3handler.deleteObject(bucketName, folderName);
			}			
		}catch(AmazonServiceException exception){
			printException(exception);
		}
		
	}
	
	/**
	 * @param bucketName
	 * @throws Exception
	 */
	public void emptyBucket(String bucketName) throws Exception {
		Logger.info("Emptying the folder "+bucketName);
		try{
			ObjectListing objectListing = s3handler.listObjects(bucketName);
	         while (true) {
	             for ( Iterator<?> iterator = objectListing.getObjectSummaries().iterator(); iterator.hasNext(); ) {
	                 S3ObjectSummary objectSummary = (S3ObjectSummary) iterator.next();
	                 s3handler.deleteObject(bucketName, objectSummary.getKey());
	             }
	  
	             if (objectListing.isTruncated()) {
	                 objectListing = s3handler.listNextBatchOfObjects(objectListing);
	             } else {
	                 break;
	             }
	         };
	         VersionListing list = s3handler.listVersions(new ListVersionsRequest().withBucketName(bucketName));
	         for ( Iterator<?> iterator = list.getVersionSummaries().iterator(); iterator.hasNext(); ) {
	             S3VersionSummary s = (S3VersionSummary)iterator.next();
	             s3handler.deleteVersion(bucketName, s.getKey(), s.getVersionId());
	         }
		}catch(AmazonServiceException exception){
			printException(exception);
		}				
	}

	/**
	 * @param exception
	 */
	private void printException(AmazonServiceException exception) {
		Logger.info("###########################################################");
		Logger.info("############ Exception occured while doing the AWS Operation ");
		Logger.info("Request ID: "+exception.getRequestId());		
		Logger.info("Service Name: "+exception.getServiceName());
		Logger.info("Message: "+exception.getMessage());
		Logger.info("Status Code: "+exception.getStatusCode());		
		Logger.info("Error Type: "+exception.getErrorType());
		Logger.info("Error Code: "+exception.getErrorCode());
		Logger.info("###########################################################");
	}	
}