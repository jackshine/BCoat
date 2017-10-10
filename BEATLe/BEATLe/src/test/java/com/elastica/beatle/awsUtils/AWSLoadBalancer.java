/**
 * 
 */
package com.elastica.beatle.awsUtils;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.DeleteLoadBalancerRequest;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import com.elastica.beatle.DateUtils;

/**
 * @author anuvrath
 *
 */
public class AWSLoadBalancer extends AWSClient {
	private AmazonElasticLoadBalancingClient LBBalancer;	
	/**
	 * @param accessKey
	 * @param accessKeySecret
	 * @throws Exception
	 */
	public AWSLoadBalancer(String accessKey,String accessKeySecret) throws Exception {
		super(accessKey, accessKeySecret);
		LBBalancer = new AmazonElasticLoadBalancingClient(credentials);
		
	}
	
	/**
	 * @return
	 */
	public List<LoadBalancerDescription> listAllLoadBalancers(){		
		DescribeLoadBalancersResult lbs = LBBalancer.describeLoadBalancers();
		return lbs.getLoadBalancerDescriptions();
	}
	
	/**
	 * @param lbID
	 */
	public void deleteLoadBalancer(String lbID){
		DeleteLoadBalancerRequest deleteRequest = new DeleteLoadBalancerRequest(lbID);
		LBBalancer.deleteLoadBalancer(deleteRequest);
	}
	/**
	 * @param launchTime
	 * @return
	 */
	public int getLBUpTime(Date volumeLaunchTime) {
		return Days.daysBetween(new DateTime(volumeLaunchTime), new DateTime(new DateTime(DateUtils.getCurrentTime()))).getDays();
	}
}