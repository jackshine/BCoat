package com.elastica.action.backend;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.elastica.constants.CommonConstants;
import com.elastica.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AWSBEActions {
	
	private AmazonEC2 EC2handler;
	List<String> instanceIDs;
	
	/**
	 * @throws Exception
	 */
	public AWSBEActions() throws Exception {
		try{
			Logger.info("Initializing AWS Credentials with accessKey: "+CommonConstants.AWS_SECRET_KEY+" and secretKey: "+CommonConstants.AWS_SECRET_TOKEN);
			EC2handler = new AmazonEC2Client(new BasicAWSCredentials(CommonConstants.AWS_SECRET_KEY, CommonConstants.AWS_SECRET_TOKEN));
			EC2handler.setRegion(Region.getRegion(Regions.US_WEST_2));
		}catch(AmazonServiceException exception){			
			Logger.info("Exception occured while initiating the AWS instance for service");
			Logger.info("Exception caused because of: "+exception.getCause().getMessage());
			Logger.info("Exception error code: "+exception.getErrorCode());
			Logger.info("Exception error message: "+exception.getErrorMessage());
			Logger.info("Exception error type: "+exception.getErrorType());
			Logger.info("Exception status code: "+exception.getStatusCode());
		}
		instanceIDs = getInstanceList();
	}
	
	/**
	 * @return
	 */
	private List<String> getInstanceList() {
		List<String> list = new ArrayList<>();
		list.add("i-28549887"); // 10.0.63.195 this machine will be stopped/started
		list.add("i-648785a3"); // 10.0.60.26 this machine will be stopped/started
		list.add("i-668785a1"); // 10.0.60.24 this machine will be stopped/started
		list.add("i-638785a4"); // 10.0.60.27 this machine will be stopped/started
		list.add("i-658785a2"); // 10.0.60.25 this machine will be stopped/started
		list.add("i-e29f1125"); // 10.0.62.253 this machine will be stopped/started
		list.add("i-6f42e9b5"); // 10.0.1.192 this machine will be stopped/started
		list.add("i-e19f1126"); // 10.0.62.254 this machine will be stopped/started
		list.add("i-04fc1dab"); // 10.0.62.214 this machine will be stopped/started
		list.add("i-05fc1daa"); // 10.0.62.215 this machine will be stopped/started
		list.add("i-1bfc1db4"); // 10.0.62.213 this machine will be stopped/started
		list.add("i-2a549885"); // 10.0.63.198 this machine will be stopped/started
		list.add("i-2b549884"); // 10.0.63.196 this machine will be stopped/started
		
		return list;
	}

	/**
	 * @return
	 * @throws InterruptedException
	 * @throws AmazonServiceException
	 */
	public StopInstancesResult stopAWSInstance() throws InterruptedException, AmazonServiceException{
		StopInstancesResult result = null;
		Logger.info("#################### Stoping instances ####################");			
		StopInstancesRequest stopRequest = new StopInstancesRequest();
		stopRequest.setInstanceIds(instanceIDs);
		result = EC2handler.stopInstances(stopRequest);
		Logger.info("Instance Stop request sent");
		List<InstanceStateChange> stateChange = result.getStoppingInstances();
		for(InstanceStateChange istate: stateChange)					
			Logger.info("Instance: "+istate.getInstanceId()+" Current Status: "+istate.getCurrentState().getName());				
		Logger.info("Waiting for 1 minute for instance to stop");
		Thread.sleep(60000);
		Logger.info("#################### Instances may take more than one minute sometimes to stop. So please wait for sometime ####################");		
		return result;
	}
	
	/**
	 * @return
	 * @throws InterruptedException
	 * @throws AmazonServiceException
	 */
	public StartInstancesResult startAWSInstance() throws InterruptedException, AmazonServiceException{
		StartInstancesResult result = null;
		Logger.info("#################### Starting instances ####################");
		StartInstancesRequest startRequest =  new StartInstancesRequest();
		startRequest.setInstanceIds(instanceIDs);
		result = EC2handler.startInstances(startRequest);
		Logger.info("Instance start request sent");
		List<InstanceStateChange> stateChange = result.getStartingInstances();
		for(InstanceStateChange istate: stateChange)
			Logger.info("Instance: "+istate.getInstanceId()+" Current Status: "+istate.getCurrentState().getName());
		Logger.info("Waiting for 2 minute so that the instnce is up and running");
		Thread.sleep(120000);
		return result;
	}
	
	public static void main(String[] args) throws Exception{
		AWSBEActions aws = new AWSBEActions();
		//aws.stopAWSInstance();
		aws.startAWSInstance();
	}
}