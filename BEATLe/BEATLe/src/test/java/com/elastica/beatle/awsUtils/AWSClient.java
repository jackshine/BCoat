package com.elastica.beatle.awsUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.elastica.beatle.logger.Logger;

public class AWSClient {
	
	protected AWSCredentials credentials;
	
	public AWSClient(String accessKey, String secretKey){
		try{
			Logger.info("Initializing AWS Credentials with accessKey: "+accessKey+" and secretKey: "+secretKey);
			credentials = new BasicAWSCredentials(accessKey, secretKey);
		}catch(AmazonServiceException exception){			
			Logger.info("Exception occured while initiating the AWS instance for service");
			Logger.info("Exception caused because of: "+exception.getCause().getMessage());
			Logger.info("Exception error code: "+exception.getErrorCode());
			Logger.info("Exception error message: "+exception.getErrorMessage());
			Logger.info("Exception error type: "+exception.getErrorType());
			Logger.info("Exception status code: "+exception.getStatusCode());
		}
	}			
}
