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
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AttachVolumeRequest;
import com.amazonaws.services.ec2.model.AttachVolumeResult;
import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.DeleteVolumeRequest;
import com.amazonaws.services.ec2.model.DescribeVolumesResult;
import com.amazonaws.services.ec2.model.DetachVolumeRequest;
import com.amazonaws.services.ec2.model.DetachVolumeResult;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.amazonaws.services.ec2.model.Volume;
import com.amazonaws.services.ec2.model.VolumeAttachment;
import com.amazonaws.services.ec2.model.VolumeType;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.tools.awsMonitorTool.AWSMonitorDTO;

/**
 * @author anuvrath
 *
 */
public class AWSVolume extends AWSClient {
	private AmazonEC2 EC2handler;	
	private static final Integer DEFAULT_IOPS = 300;
	/**
	 * @param accessKey
	 * @param accessKeySecret
	 * @throws Exception
	 */
	public AWSVolume(String accessKey,String accessKeySecret) throws Exception {
		super(accessKey, accessKeySecret);
		EC2handler = new AmazonEC2Client(credentials);		
	}
	
	/**
	 * @param size
	 * @param region
	 */
	public 	CreateVolumeResult createVolume(Integer size, Regions region,String availabilityZone) throws AmazonServiceException {
		CreateVolumeResult createResult = null;
		Logger.info("################################ Creating volume with following properties ################################");
		Logger.info("Size: "+ size);
		Logger.info("Region: "+Region.getRegion(region).getName());
		Logger.info("Is Encryption Required:"+false);
		Logger.info("Volume Type: "+VolumeType.Gp2);
		Logger.info("Setting the region to: "+region);
		Logger.info(" ");
			
		EC2handler.setRegion(Region.getRegion(region));
		Logger.info("Getting availability zone for "+region);
		Logger.info("Creating volume in: "+availabilityZone+" availabilityZone");
		Logger.info(" ");
			
		CreateVolumeRequest createVolumeRequest = new CreateVolumeRequest(size, availabilityZone);
		createVolumeRequest.setVolumeType(VolumeType.Gp2);			
		createVolumeRequest.setEncrypted(false);			
		createResult = EC2handler.createVolume(createVolumeRequest);
						
		Logger.info("Volume Created with ID: "+createResult.getVolume().getVolumeId());
		Logger.info("Volume type: "+createResult.getVolume().getVolumeType());
		Logger.info("Volume state: "+createResult.getVolume().getState());
		Logger.info("Volume availability region: "+createResult.getVolume().getAvailabilityZone());
		Logger.info("Volume created time: "+createResult.getVolume().getCreateTime());
		Logger.info("################################ Creating Volume completed. But it Still may be in Creating state. Please wait for sometime################################");			
		
		return createResult;
	}
	
	/**
	 * @param volumeId
	 * @param instanceId
	 * @param device
	 * @param region
	 */
	public AttachVolumeResult attachVolume(String volumeId,String instanceId, String device,Regions region) throws AmazonServiceException {
		AttachVolumeResult attachResult = null;
		Logger.info("################################ Attaching Volume ################################");
		Logger.info("Attaching volume "+volumeId +" from "+region.getName()+" to "+instanceId);
		AttachVolumeRequest attachRequest = new AttachVolumeRequest(volumeId, instanceId, device);
		EC2handler.setRegion(Region.getRegion(region));
		attachResult = EC2handler.attachVolume(attachRequest);
		Logger.info("Volume status after attach: "+attachResult.getAttachment().getState());
		Logger.info("Volume attached to "+ attachResult.getAttachment().getInstanceId()+" instance");
		Logger.info("Volume attached to "+attachResult.getAttachment().getDevice()+" device");
		Logger.info("Volume attachment time "+attachResult.getAttachment().getAttachTime());
		Logger.info("################################ Attaching Volume completed ################################");		
		return attachResult;
	}

	/**
	 * @param volumeId
	 * @param region
	 */
	public DetachVolumeResult detachVolume(String volumeId,Regions region) throws AmazonServiceException {
		DetachVolumeResult detachResult = null;
		Logger.info("################################ Detaching Volume ################################");
		Logger.info("Detaching volume "+volumeId +" from "+region.getName());
		DetachVolumeRequest detechvolumeRequest = new DetachVolumeRequest(volumeId);
		EC2handler.setRegion(Region.getRegion(region));
		try{
			detachResult = EC2handler.detachVolume(detechvolumeRequest);
			Logger.info("Volume status after detach: "+detachResult.getAttachment().getState());
			Logger.info("Volume detach from "+ detachResult.getAttachment().getInstanceId()+" Instance");
			Logger.info("Volume detach from "+detachResult.getAttachment().getDevice()+" device");
			Thread.sleep(60000);
			Logger.info("################################ Detaching Volume completed ################################");
		}catch(Exception e){			
			Volume volume = getVolume(volumeId, region);
			List<VolumeAttachment> attachments = volume.getAttachments();
			for(VolumeAttachment attach: attachments){
				Logger.info("This is a root volume attached to "+attach.getInstanceId()+" Instance");
				Logger.info("So terminating the instance.");
				TerminateInstancesResult result = null;
				Logger.info("#################### Terminating instances ####################");
				TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest();			 
				terminateInstancesRequest.withInstanceIds(attach.getInstanceId());
				EC2handler.setRegion(Region.getRegion(region));
				result = EC2handler.terminateInstances(terminateInstancesRequest);
				Logger.info("Instance termination request sent");
				List<InstanceStateChange> statechange = result.getTerminatingInstances();
				for(InstanceStateChange istate: statechange){					
					Logger.info("Instance: "+istate.getInstanceId()+" in "+Region.getRegion(region) +"Current Status: "+istate.getCurrentState().getName());				
				}						
				Logger.info("#################### Instances may be in shutting-down status please wait for sometime ####################");
			}
		}			
				
		return detachResult;
	}
	
	/**
	 * @param volumeIdlist
	 * @param region
	 */
	public void deleteVolume(List<String> volumeIdlist,Regions region) throws AmazonServiceException {
		Logger.info("################################ Deleting Volumes from list ################################");
		for(String volumeID: volumeIdlist){
			Logger.info("Deleting volume: "+volumeID);
			Volume volume = getVolume(volumeID, region);			
			if(volume.getState().equalsIgnoreCase("in-use")){
				DetachVolumeResult detachResult = detachVolume(volumeID, region);
				if(detachResult!= null){
					DeleteVolumeRequest deleteVolumeRequest = new DeleteVolumeRequest(volumeID);
					EC2handler.setRegion(Region.getRegion(region));
					EC2handler.deleteVolume(deleteVolumeRequest);
				}
			}				
			else if(volume.getState().equals("available")){
				DeleteVolumeRequest deleteVolumeRequest = new DeleteVolumeRequest(volumeID);
				EC2handler.setRegion(Region.getRegion(region));
				EC2handler.deleteVolume(deleteVolumeRequest);
			}
		}	
		Logger.info("################################ Deleting Volumes Completed ################################");
	}
	
	/**
	 * @param size
	 * @param snapshotId
	 * @param region
	 */
	public void createVolume(Integer size, String snapshotId, Regions region) throws AmazonServiceException {

		Logger.info("################################ Creating volume with following properties ################################");
		Logger.info("Size: "+ size);
		Logger.info("Region: "+Region.getRegion(region).getName());
		Logger.info("Is Encryption Required:"+false);
		Logger.info("Snapshot ID: "+snapshotId);
		Logger.info("Volume Type: "+VolumeType.Standard);
		Logger.info("iops: "+DEFAULT_IOPS);
		CreateVolumeRequest createVolumeRequest = new CreateVolumeRequest(snapshotId, Region.getRegion(region).getName());
		createVolumeRequest.setVolumeType(VolumeType.Standard);
		createVolumeRequest.setSize(size);
		createVolumeRequest.setIops(DEFAULT_IOPS);
		createVolumeRequest.setEncrypted(false);
			
		EC2handler.setRegion(Region.getRegion(region));
		CreateVolumeResult createResult = EC2handler.createVolume(createVolumeRequest);
		Logger.info("Volume Created with ID: "+createResult.getVolume().getVolumeId());
		Logger.info("Volume type: "+createResult.getVolume().getVolumeType());
		Logger.info("Volume state: "+createResult.getVolume().getState());
		Logger.info("Volume availability region: "+createResult.getVolume().getAvailabilityZone());
		Logger.info("Volume created time: "+createResult.getVolume().getCreateTime());
		Logger.info("################################ Creating Volume completed ################################");		
	}
	
	/**
	 * @param size
	 * @param region
	 * @param isEncryptionRequired
	 * @param snapshotId
	 * @param volumeType
	 * @param iops
	 */
	public void createVolume(Integer size,Regions region,boolean isEncryptionRequired,String snapshotId, VolumeType volumeType, Integer iops) throws AmazonServiceException {
		Logger.info("################################ Creating volume with following properties ################################");

		Logger.info("Creating volume with following properties");
		Logger.info("Size: "+ size);
		Logger.info("Region: "+Region.getRegion(region).getName());
		Logger.info("Is Encryption Required:"+isEncryptionRequired);
		Logger.info("Snapshot ID: "+snapshotId);
		Logger.info("Volume Type: "+volumeType);
		Logger.info("iops: "+iops);
			
		CreateVolumeRequest createVolumeRequest = new CreateVolumeRequest(size, Region.getRegion(region).getName());
		createVolumeRequest.setEncrypted(isEncryptionRequired);
		createVolumeRequest.setSnapshotId(snapshotId);
		createVolumeRequest.setVolumeType(volumeType);
		createVolumeRequest.setIops(iops);
		CreateVolumeResult createResult = EC2handler.createVolume(createVolumeRequest);
		Logger.info("Volume Created with ID: "+createResult.getVolume().getVolumeId());
		Logger.info("Volume type: "+createResult.getVolume().getVolumeType());
		Logger.info("Volume state: "+createResult.getVolume().getState());
		Logger.info("Volume availability region: "+createResult.getVolume().getAvailabilityZone());
		Logger.info("Volume created time: "+createResult.getVolume().getCreateTime());
		Logger.info("################################ Creating Volume completed ################################");	
	}	
	
	/**
	 * @param region
	 * @return
	 */
	public List<Volume> listAllVolumes(Regions region){
		List<Volume> runningVolumeList = new ArrayList<Volume>();

		EC2handler.setRegion(Region.getRegion(region));
		DescribeVolumesResult result = EC2handler.describeVolumes();
		runningVolumeList = result.getVolumes();
		if(!runningVolumeList.isEmpty())
			Logger.info("Volumes found in "+Region.getRegion(region)+" !!!... You may want to shut them down");						
		
		return runningVolumeList;
	}		
	
	/**
	 * @param region
	 * @return
	 */
	public List<AWSMonitorDTO> getRunningVolumeDetails(Regions region){
		List<AWSMonitorDTO> runningVolumeList = new ArrayList<>();		
		List<Volume> volumeList = listAllVolumes(region);		
		if(volumeList.size()>0){
			for(Volume volume: volumeList){
				AWSMonitorDTO volumeData = new AWSMonitorDTO();
				volumeData.setresourceID(volume.getVolumeId());
				volumeData.setresourceUpTime(getVolumeUpTime(volume.getCreateTime()));
				volumeData.setresourceState(volume.getState());
				volumeData.setresourceType(volume.getVolumeType());
				volumeData.setAvailabilityZone(volume.getAvailabilityZone());
				volumeData.setresourceLaunchDate(volume.getCreateTime());
				runningVolumeList.add(volumeData);
			}
		}				 
		return runningVolumeList;
	}
	
	/**
	 * @param instance
	 * @param region
	 * @return
	 */
	public String getVolumeState(String volumeID,Regions region){
		Logger.info("*********************************************************");
		Logger.info("Looking for Volume "+volumeID+" State");
		EC2handler.setRegion(Region.getRegion(region));
		List<Volume> runningVolumeList = EC2handler.describeVolumes().getVolumes();
		for(Volume volume: runningVolumeList){
			if(volume.getVolumeId().equals(volumeID)){
				Logger.info("Current state of "+volumeID+" volume is: "+volume.getState());
				Logger.info("*********************************************************");
				return volume.getState();
			}
		}			
		Logger.info("Given Volume not found "+ volumeID);
		Logger.info("*********************************************************");
		return null;
	}
	
	public Volume getVolume(String volumeID, Regions region){
		Logger.info("Looking for Volume "+volumeID+" State");
		EC2handler.setRegion(Region.getRegion(region));
		List<Volume> runningVolumeList = EC2handler.describeVolumes().getVolumes();
		for(Volume volume: runningVolumeList){
			if(volume.getVolumeId().equals(volumeID)){
				Logger.info("Current state of "+volumeID+" volume is: "+volume.getState());
				return volume;
			}
		}				
		Logger.info("Given Volume not found");
		return null;
	}
	/**
	 * @param launchTime
	 * @return
	 */
	private int getVolumeUpTime(Date volumeLaunchTime) {
		return Days.daysBetween(new DateTime(volumeLaunchTime), new DateTime(new DateTime(DateUtils.getCurrentTime()))).getDays();
	}	
}