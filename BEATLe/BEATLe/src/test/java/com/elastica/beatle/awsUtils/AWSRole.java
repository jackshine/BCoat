/**
 * 
 */
package com.elastica.beatle.awsUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.CreateRoleRequest;
import com.amazonaws.services.identitymanagement.model.DeleteRoleRequest;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AWSRole extends AWSClient {
	
	AmazonIdentityManagementClient AWSRoleHandler;
	/**
	 * @param accessKey
	 * @param accessKeySecret
	 * @throws Exception
	 */
	public AWSRole(String accessKey,String accessKeySecret) throws Exception {
		super(accessKey, accessKeySecret);
		AWSRoleHandler = new AmazonIdentityManagementClient(credentials);		
	}
	
	/**
	 * @param roleName
	 * @param policyDocument
	 * @throws AmazonServiceException
	 */
	public void createAWSRole(String roleName, String policyDocument)throws AmazonServiceException{
		Logger.info("################################ Creating IAM Role ################################");
		CreateRoleRequest createRoleRequest = new  CreateRoleRequest();
		createRoleRequest.withRoleName(roleName);
		createRoleRequest.withAssumeRolePolicyDocument(policyDocument);
		AWSRoleHandler.createRole(createRoleRequest);
		Logger.info("################################ IAM Role Created ################################");
	}
	
	/**
	 * @param roleName
	 * @throws AmazonServiceException
	 */
	public void deleteAWSRole(String roleName)throws AmazonServiceException{
		Logger.info("################################ Deleting IAM Role ################################");
		DeleteRoleRequest deleteRoleRequest =  new DeleteRoleRequest();
		deleteRoleRequest.withRoleName(roleName);		
		AWSRoleHandler.deleteRole(deleteRoleRequest);
		Logger.info("################################ IAM Role Deleted ################################");
	}		
}