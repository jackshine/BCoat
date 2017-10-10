/**
 * 
 */
package com.elastica.beatle.awsUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.AttachGroupPolicyRequest;
import com.amazonaws.services.identitymanagement.model.CreatePolicyRequest;
import com.amazonaws.services.identitymanagement.model.CreatePolicyResult;
import com.amazonaws.services.identitymanagement.model.DeleteGroupPolicyRequest;
import com.amazonaws.services.identitymanagement.model.DeletePolicyRequest;
import com.amazonaws.services.identitymanagement.model.DetachGroupPolicyRequest;
import com.amazonaws.services.identitymanagement.model.PutGroupPolicyRequest;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AWSGroupPolicy extends AWSClient {
	
	private AmazonIdentityManagementClient AWSUserHandler;
	/**
	 * @param accessKey
	 * @param accessKeySecret
	 * @throws Exception
	 */
	public AWSGroupPolicy(String accessKey,String accessKeySecret) throws Exception {
		super(accessKey, accessKeySecret);
		AWSUserHandler = new AmazonIdentityManagementClient(credentials);		
	}
	
	public CreatePolicyResult putAWSPolicy(String policyName, String PolicyDocument) throws AmazonServiceException{
		Logger.info("#################### Creating new Policy ####################");
		CreatePolicyRequest createPolicyRequest = new CreatePolicyRequest();
		createPolicyRequest.setPolicyName(policyName);
		createPolicyRequest.setPolicyDocument(PolicyDocument);
		CreatePolicyResult cratePolicyResult = AWSUserHandler.createPolicy(createPolicyRequest);
		Logger.info("Created policy details: ");
		Logger.info("Policy Name:"+cratePolicyResult.getPolicy().getPolicyName());
		Logger.info("Policy ID: "+cratePolicyResult.getPolicy().getPolicyId());
		Logger.info("Policy ARN: "+cratePolicyResult.getPolicy().getArn());
		Logger.info("#################### Policy created ####################");
		return cratePolicyResult;
	}
	
	public void PutGroupPolicy(String groupName, String policyName,String policyDocument){
		Logger.info("#################### Putting Group Policy ####################");
		PutGroupPolicyRequest putPolicyRequest = new PutGroupPolicyRequest(groupName,policyName, policyDocument);
		AWSUserHandler.putGroupPolicy(putPolicyRequest);
		Logger.info("#################### Putting Group Policy Done ####################");
	}
	
	/**
	 * @param groupName
	 * @param policyArn
	 */
	public void attachAWSGroupPolicy(String groupName, String policyArn) throws AmazonServiceException {
		Logger.info("#################### Attaching Policy to group ####################");
		AttachGroupPolicyRequest attachGrpPolicyRequest = new AttachGroupPolicyRequest();
		attachGrpPolicyRequest.setGroupName(groupName);
		attachGrpPolicyRequest.setPolicyArn(policyArn);
		AWSUserHandler.attachGroupPolicy(attachGrpPolicyRequest);
		Logger.info("#################### Attached Policy to group ####################");
	}
	
	
	/**
	 * @param groupName
	 * @param policyArn
	 * @throws AmazonServiceException
	 */
	public void detachAWSGroupPlicy(String groupName, String policyArn) throws AmazonServiceException {
		Logger.info("#################### Detaching Policy from group ####################");
		DetachGroupPolicyRequest detachGrpPolicyRequest = new DetachGroupPolicyRequest();
		detachGrpPolicyRequest.setGroupName(groupName);
		detachGrpPolicyRequest.setPolicyArn(policyArn);
		AWSUserHandler.detachGroupPolicy(detachGrpPolicyRequest);
		Logger.info("#################### Detached Policy from group ####################");
	}
	
	/**
	 * @param policyArn
	 * @throws AmazonServiceException
	 */
	public void deleteAWSPolicy(String policyArn) throws AmazonServiceException {
		Logger.info("#################### Deleting Policy ####################");
		DeletePolicyRequest deleteRequest = new DeletePolicyRequest();
		deleteRequest.setPolicyArn(policyArn);		
		AWSUserHandler.deletePolicy(deleteRequest);	
		Logger.info("#################### Deleted Policy ####################");
	}
	
	/**
	 * @param groupName
	 * @param policyName
	 * @throws AmazonServiceException
	 */
	public void deleteAWSGroupPolicy(String groupName,String policyName) throws AmazonServiceException {
		Logger.info("#################### Deleting Group Policy ####################");
		DeleteGroupPolicyRequest deleteRequest = new DeleteGroupPolicyRequest().withGroupName(groupName);
		deleteRequest.withPolicyName(policyName);
		AWSUserHandler.deleteGroupPolicy(deleteRequest);	
		Logger.info("#################### Deleted Group Policy ####################");
	}
}