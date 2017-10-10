package co.elastica.S3Handler;

/**
 * @author anuvrath
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import co.elastica.Constants.Constants;

public class S3Archive {
	
	private AWSCredentials credentials;
	private ClientConfiguration clientConfig;
	AmazonS3 conn;
	private String SUFFIX = "/";

	public S3Archive() {
		credentials = new BasicAWSCredentials(Constants.AWS_ACCESSKEY, Constants.AWS_SECREATE);
		clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		conn = new AmazonS3Client(credentials, clientConfig);
	}

	/**
	 * @return
	 */
	private List<String> listBucketContents(){
		List<String> objectList = new ArrayList<String>();
		ObjectListing objects = conn.listObjects(Constants.S3_BUCKET_NAME);
		do {
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				objectList.add(objectSummary.getKey().split("/")[0]);
		}
		objects = conn.listNextBatchOfObjects(objects);
		} while (objects.isTruncated());
		return objectList;
	}
	
	/**
	 * @param buildVersion
	 * @param inputStream
	 * @param fileName
	 * @throws IOException
	 */
	private void putObjectInBucket(String buildVersion, InputStream inputStream, String fileName) throws IOException{
		String filePath = buildVersion + SUFFIX + fileName;
	    ObjectMetadata metadata = new ObjectMetadata();
	    metadata.setContentLength(inputStream.available());
		conn.putObject(new PutObjectRequest(Constants.S3_BUCKET_NAME, filePath, inputStream,metadata));				
	}
	
	/**
	 * @param folderName
	 */
	private void createFolderInBucket(String folderName){		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		PutObjectRequest putObjectRequest = new PutObjectRequest(Constants.S3_BUCKET_NAME,folderName + SUFFIX, new ByteArrayInputStream(new byte[0]), metadata);
		conn.putObject(putObjectRequest);
	}
	
	/**
	 * @param buildVersion
	 * @param inputStream
	 * @param fileName
	 * @throws IOException
	 */
	public void pushReportToS3(String buildVersion, InputStream inputStream, String fileName) throws IOException{
		
		List<String> bucketContents = listBucketContents();
		if(bucketContents.contains(buildVersion)){
			System.out.println("Reports exist for "+buildVersion+" build, So pushing new report in same folder");
			putObjectInBucket(buildVersion,inputStream,fileName);
		}
		else{
			System.out.println("Reports doesn't exist for "+buildVersion);
			System.out.println("Creating folder "+buildVersion+" inside bucket "+Constants.S3_BUCKET_NAME);
			createFolderInBucket(buildVersion);
			putObjectInBucket(buildVersion,inputStream,fileName);
		}
	}
}