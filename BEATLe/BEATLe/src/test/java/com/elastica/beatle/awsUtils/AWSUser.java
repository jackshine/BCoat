/**
 * 
 */
package com.elastica.beatle.awsUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.CreateUserRequest;
import com.amazonaws.services.identitymanagement.model.CreateUserResult;
import com.amazonaws.services.identitymanagement.model.DeleteUserRequest;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AWSUser extends AWSClient{
	
	AmazonIdentityManagementClient AWSUserHandler;
	/**
	 * @param accessKey
	 * @param accessKeySecret
	 * @throws Exception
	 */
	public AWSUser(String accessKey,String accessKeySecret) throws Exception {
		super(accessKey, accessKeySecret);
		AWSUserHandler = new AmazonIdentityManagementClient(credentials);		
	}
			
	/**
	 * @param userName
	 */
	public void createAWSUser(String userName) throws AmazonServiceException{	
		Logger.info("################################ Creating IAM user ################################");
		CreateUserRequest createUserRequest = new CreateUserRequest(userName);			
		CreateUserResult createUserResult = AWSUserHandler.createUser(createUserRequest);
		Logger.info("User "+createUserResult.getUser().getUserName()+"  created at " +createUserResult.getUser().getCreateDate() +" with user id "+createUserResult.getUser().getUserId());
		Logger.info("################################ IAM user created ################################");				
	}
	
	/**
	 * @param userName
	 */
	public void deleteAWSUser(String userName) throws AmazonServiceException{
		Logger.info("################################ Deleting IAM user ################################");
		DeleteUserRequest deleteUserRequest = new DeleteUserRequest(userName);
		AWSUserHandler.deleteUser(deleteUserRequest);
		Logger.info("User "+userName+" deleted");
		Logger.info("################################ User deleted ################################");
	}
}