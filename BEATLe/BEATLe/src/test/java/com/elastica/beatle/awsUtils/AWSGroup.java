/**
 * 
 */
package com.elastica.beatle.awsUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.CreateGroupRequest;
import com.amazonaws.services.identitymanagement.model.CreateGroupResult;
import com.amazonaws.services.identitymanagement.model.DeleteGroupRequest;
import com.amazonaws.services.identitymanagement.model.UpdateGroupRequest;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AWSGroup extends AWSClient {
	
	private AmazonIdentityManagementClient AWSUserHandler;
	/**
	 * @param accessKey
	 * @param accessKeySecret
	 * @throws Exception
	 */
	public AWSGroup(String accessKey,String accessKeySecret) throws Exception {
		super(accessKey, accessKeySecret);
		AWSUserHandler = new AmazonIdentityManagementClient(credentials);		
	}
	
	/**
	 * @param groupName
	 */
	public CreateGroupResult createAWSGroup(String groupName)throws AmazonServiceException{
		CreateGroupResult grpResult =  null;
		Logger.info("################################ Creating Group ################################");
		CreateGroupRequest createGroupRequest = new CreateGroupRequest(groupName);
		grpResult = AWSUserHandler.createGroup(createGroupRequest);
		Logger.info("Group created with name: "+grpResult.getGroup().getGroupName());
		Logger.info("Group Id: "+grpResult.getGroup().getGroupId());
		Logger.info("Group Created time: "+grpResult.getGroup().getCreateDate());
		Logger.info("Group ARN: "+grpResult.getGroup().getArn());
		Logger.info("################################ Group Created ################################");		
		return grpResult;
	}
	
	/**
	 * @param groupName
	 */
	public void deleteAWSGroup(String groupName)throws AmazonServiceException{			
		Logger.info("################################ Deleting Group ################################");
		Logger.info("Deleting the Group: "+groupName);
		DeleteGroupRequest deleteGrpRequest = new DeleteGroupRequest(groupName);
		AWSUserHandler.deleteGroup(deleteGrpRequest);
		Logger.info("################################ Group Deleted ################################");	
	}
	
	/**
	 * @param groupName
	 * @param newGroupName 
	 */
	public void updateAWSGroup(String groupName, String newGroupName) throws AmazonServiceException{
		Logger.info("################################ Group Updating ################################");
		Logger.info("Updating the Group with groupname: "+newGroupName);
		UpdateGroupRequest updateGrpRequest = new UpdateGroupRequest(groupName);			
		updateGrpRequest.setNewGroupName(newGroupName);
		AWSUserHandler.updateGroup(updateGrpRequest);
		Logger.info("################################ Updated Group ################################");
	}		
}
