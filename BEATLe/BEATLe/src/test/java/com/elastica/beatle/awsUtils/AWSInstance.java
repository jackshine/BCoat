/**
 * 
 */
package com.elastica.beatle.awsUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.tools.awsMonitorTool.AWSMonitorDTO;

/**
 * @author anuvrath
 *
 */
public class AWSInstance extends AWSClient{

	private AmazonEC2 EC2handler;	
	/**
	 * @param accessKey
	 * @param accessKeySecret
	 * @throws Exception
	 */
	public AWSInstance(String accessKey,String accessKeySecret) throws Exception {
		super(accessKey, accessKeySecret);
		EC2handler = new AmazonEC2Client(credentials);		
	}
	
	/**
	 * @param instanceIds
	 * @throws InterruptedException 
	 */
	public void rebootAWSInstance(List<String> instanceIds,Regions region) throws InterruptedException, AmazonServiceException {		
		Logger.info("#################### Rebooting instances ####################");
		RebootInstancesRequest rebootRequest = new RebootInstancesRequest(instanceIds);
		EC2handler.setRegion(Region.getRegion(region));
		EC2handler.rebootInstances(rebootRequest);
		Thread.sleep(30000);
		Logger.info("#################### Instance Rebooting.. It may take few seconds to reboot####################");			
	}
	
	/**
	 * @param instanceIds
	 * @param region
	 * @return
	 * @throws InterruptedException
	 */
	public StopInstancesResult stopAWSInstance(List<String> instanceIds, Regions region) throws InterruptedException, AmazonServiceException{
		StopInstancesResult result = null;
		Logger.info("#################### Stoping instances ####################");			
		StopInstancesRequest stopRequest = new StopInstancesRequest();
		stopRequest.setInstanceIds(instanceIds);
		EC2handler.setRegion(Region.getRegion(region));
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
	 * @param instanceIds
	 * @param region
	 * @throws InterruptedException
	 */
	public StartInstancesResult startAWSInstance(List<String> instanceIds, Regions region) throws InterruptedException, AmazonServiceException{
		StartInstancesResult result = null;
		Logger.info("#################### Starting instances ####################");
		StartInstancesRequest startRequest =  new StartInstancesRequest();
		startRequest.setInstanceIds(instanceIds);
		EC2handler.setRegion(Region.getRegion(region));
		result = EC2handler.startInstances(startRequest);
		Logger.info("Instance start request sent");
		List<InstanceStateChange> stateChange = result.getStartingInstances();
		for(InstanceStateChange istate: stateChange)					
			Logger.info("Instance: "+istate.getInstanceId()+" Current Status: "+istate.getCurrentState().getName());
		Logger.info("Waiting for 1 minute so that the instnce is up and running");
		Thread.sleep(60000);
		Logger.info("#################### Instances may take more than one minute to start sometimes. So please wait for sometime ####################");
		return result;
	}
	
	/**
	 * @param instanceIds
	 * @param region
	 * @throws InterruptedException 
	 */
	public TerminateInstancesResult terminateAWSInstance(List<String> instanceIds, Regions region) throws InterruptedException, AmazonServiceException{
		TerminateInstancesResult result = null;
		Logger.info("#################### Terminating instances ####################");
		TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest();			 
		terminateInstancesRequest.setInstanceIds(instanceIds);
		EC2handler.setRegion(Region.getRegion(region));
		result = EC2handler.terminateInstances(terminateInstancesRequest);
		Logger.info("Instance termination request sent");
		List<InstanceStateChange> statechange = result.getTerminatingInstances();
		for(InstanceStateChange istate: statechange){					
			Logger.info("Instance: "+istate.getInstanceId()+" in "+Region.getRegion(region) +"Current Status: "+istate.getCurrentState().getName());				
		}						
		Logger.info("#################### Instances may be in shutting-down status please wait for sometime ####################");
		Thread.sleep(60000);
		return result;
	}
	
	/**
	 * @param instanceType
	 * @param imageID
	 * @param keyPair
	 * @param securityGroup
	 * @throws InterruptedException 
	 */
	public RunInstancesResult launchAWSInstance(String instanceType, String imageID, String keyPair, String[] securityGroup, Regions region) throws InterruptedException, AmazonServiceException{
		RunInstancesResult runInstancesResult = null;
		
		Logger.info("#################### Creating new instance ####################");
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
		runInstancesRequest.withInstanceType(instanceType)
		.withImageId(imageID)
		.withMinCount(1)
	    .withMaxCount(1);
		if(keyPair!= null)
			runInstancesRequest.withKeyName(keyPair);
		if(securityGroup != null)
			runInstancesRequest.withSecurityGroups(securityGroup);
			
		if(region!= null){
			Logger.info("Setting the region to: "+Region.getRegion(region));
			EC2handler.setRegion(Region.getRegion(region));
		}				
		else
			Logger.info("No region sent. So creating instance in Default region: "+Regions.DEFAULT_REGION);
			
		runInstancesResult = EC2handler.runInstances(runInstancesRequest);
		Logger.info("Launch instance successful");
		Logger.info("Launched instance id: "+ runInstancesResult.getReservation().getInstances().get(0).getInstanceId());
		String instanceState = runInstancesResult.getReservation().getInstances().get(0).getState().getName();
		while(!instanceState.equals("running")){
			Logger.info("Waiting for the instance to come up");
			Logger.info("Waiting for 30 seconds");
			Thread.sleep(30000);								
			instanceState = getInstanceState(runInstancesResult.getReservation().getInstances().get(0).getInstanceId(),region);
		}			
		Logger.info("#################### Instance is up and running ####################");		
		return runInstancesResult;
	}
	
	/**
	 * @param instance
	 * @param region
	 * @return
	 */
	public String getInstanceState(String instanceID,Regions region){
		Logger.info("Looking for instance "+instanceID+" State");
		EC2handler.setRegion(Region.getRegion(region));
		DescribeInstancesResult result = EC2handler.describeInstances();
		List<Reservation> reservations = result.getReservations();
		for(Reservation reservation: reservations){
			List<Instance> instances = reservation.getInstances();
			for(Instance instance1: instances){
				if(instanceID.equals(instance1.getInstanceId())){
					Logger.info("Current state of "+instanceID+" instance is: "+instance1.getState().getName());
					return instance1.getState().getName();
				}													
			}
		}
		Logger.info("Passed instance not found");
		return null;
	}
	/**
	 * @param region
	 * @return
	 */
	private List<Instance> listAllAWSInstances(Regions region){
		List<Instance> runningInstances = new ArrayList<>();
		EC2handler.setRegion(Region.getRegion(region));
		DescribeInstancesResult result = EC2handler.describeInstances();
		List<Reservation> reservations = result.getReservations();
		for(Reservation reservation: reservations){
			List<Instance> instances = reservation.getInstances();
			for(Instance instance: instances){	
				if(instance.getState().getName().equals("running")){
					Logger.info("Running instance found in "+region +" !!!... with instance ID: "+instance.getInstanceId());
					runningInstances.add(instance);
				}					
			}								
		}
		return runningInstances;		
	}	
	
	/**
	 * @param region
	 * @return
	 */
	public int getTotalRunningInstanceCount(Regions region){
		return listAllAWSInstances(region).size();
	}
	
	/**
	 * @param region
	 * @return
	 */
	public List<AWSMonitorDTO> getRunningInstanceDetails(Regions region){
		List<AWSMonitorDTO> runningInstanceDTO = new ArrayList<>();		
		List<Instance> instances = listAllAWSInstances(region);		
		if(instances.size()>0){
			for(Instance instance: instances){
				AWSMonitorDTO instanceData = new AWSMonitorDTO();
				instanceData.setresourceID(instance.getInstanceId());
				instanceData.setresourceUpTime(getInstanceUpTime(instance.getLaunchTime()));
				instanceData.setresourceState(instance.getState().getName());
				instanceData.setresourceType(instance.getInstanceType());
				instanceData.setAvailabilityZone(instance.getPlacement().getAvailabilityZone());
				instanceData.setresourceLaunchDate(instance.getLaunchTime());
				runningInstanceDTO.add(instanceData);
			}
		}				 
		return runningInstanceDTO;
	}
	
	/**
	 * @param launchTime
	 * @return
	 */
	private int getInstanceUpTime(Date instanceLaunchTime) {
		return Days.daysBetween(new DateTime(instanceLaunchTime), new DateTime(new DateTime(DateUtils.getCurrentTime()))).getDays();
	}	
}
