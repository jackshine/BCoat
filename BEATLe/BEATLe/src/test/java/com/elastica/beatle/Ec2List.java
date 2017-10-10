/**
 * 
 */
package com.elastica.beatle;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.testng.Assert;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;

/**
 * @author anuvrath
 *
 */
public class Ec2List {
	
	private Client restClient;
	private Map<String,Map<String,String>> hostInfoMap;
	
	public static enum Environment{
		EoE("eoe"),
		PROD("prd"),
		QAVPC("qa"),
		CEP("cep");
		
		private String environmentName;
 
		private Environment(String env) {
			environmentName = env;
		}
 
		public String getEnvName() {
			return environmentName;
		}
	}
	
	public static enum Service{
		GATEWAY("gateway"),
		AUDIT("audit"),
		INFRA("infra"),
		MOBILE_GATEWAY("mobile-gateway");
		
		private String serviceName;
 
		private Service(String service) {
			serviceName = service;
		}
 
		public String getServiceName() {
			return serviceName;
		}
	}
	
	/**
	 * @param restClient
	 * @throws Exception 
	 * @throws URISyntaxException 
	 */
	public Ec2List(Client client) throws URISyntaxException, Exception {
		super();
		this.restClient = client;
		HttpResponse ec2response = restClient.doGet(ClientUtil.BuidURI("https://zabbix.pub.elastica.net/ec2hosts.csv"), null);
		Assert.assertEquals(ec2response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		hostInfoMap = getHostInto(ClientUtil.getResponseBody(ec2response));
	}
	
	/**
	 * @param responseBody
	 * @return
	 */
	private Map<String, Map<String, String>> getHostInto(String ec2response) {
		Map<String, Map<String, String>> infoMap = new HashMap<String, Map<String,String>>();
		String[] listData = ec2response.split("\n");
		for(String data: listData){
			String[] rowData = data.split(",");
			Map<String, String> hostData = new HashMap<>();
			hostData.put("InstanceIP", rowData[2]);
			hostData.put("hostID", rowData[0]);
			infoMap.put(rowData[1],hostData);
		}
		return infoMap;
	}

	
	/**
	 * @param service
	 * @param env
	 * @return
	 */
	public List<String> getInstanceListForService(Service service,Environment env){
		String grepRegex = env.getEnvName()+"-[a-zA-Z]*-"+service.getServiceName()+"-[a-zA-Z0-9]*";
		List<String> hostInfo = new ArrayList<>();
		for (Entry<String, Map<String, String>> entry : hostInfoMap.entrySet()) {
			if(entry.getKey().matches(grepRegex))
				hostInfo.add(entry.getKey());
		}
		return hostInfo;
	}
	
	/**
	 * @param service
	 * @param env
	 * @return
	 */
	public int getTotalInstanceCountForService(Service service,Environment env){
		String grepRegex = env.getEnvName()+"-[a-zA-Z]*-"+service.getServiceName()+"-[a-zA-Z0-9]*";
		List<String> hostInfo = new ArrayList<>();
		for (Entry<String, Map<String, String>> entry : hostInfoMap.entrySet()) {
			if(entry.getKey().matches(grepRegex))
				hostInfo.add(entry.getKey());
		}
		return hostInfo.size();
	}
	
	/**
	 * @param grepRegex
	 * @return
	 */
	public List<String> getInstanceListForService(String grepRegex){
		List<String> hostInfo = new ArrayList<>();
		for (Entry<String, Map<String, String>> entry : hostInfoMap.entrySet()) {
			if(entry.getKey().matches(grepRegex))
				hostInfo.add(entry.getKey());
		}
		return hostInfo;
	}
	
	/**
	 * @param grepRegex
	 * @return
	 */
	public int getTotalInstanceCountForService(String grepRegex){
		List<String> hostInfo = new ArrayList<>();
		for (Entry<String, Map<String, String>> entry : hostInfoMap.entrySet()) {
			if(entry.getKey().matches(grepRegex))
				hostInfo.add(entry.getKey());
		}
		return hostInfo.size();
	}
}
