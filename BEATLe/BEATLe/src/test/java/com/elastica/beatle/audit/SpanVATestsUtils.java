/**
 * 
 */
package com.elastica.beatle.audit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class SpanVATestsUtils {

	/**
	 * @param suiteData
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public synchronized Map<String,String> getSpanvaAliveAgentOnThisInstance(TestSuiteDTO suiteData,Client restClient) throws Exception
	{

		String agent_state="Alive";
		String agent_ip=suiteData.getSpanvaIp();
		String agent_name=suiteData.getSpanvaAgentName();


		Reporter.log("getting updated spanvaversion:", true);
		HttpResponse getAllAgents = AuditFunctions.getAllAgentsList(restClient);
		Assert.assertEquals(getAllAgents.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strAgents=(ClientUtil.getResponseBody(getAllAgents));
		JSONArray agentsList = (JSONArray) new JSONObject(strAgents).getJSONArray("objects");
		Reporter.log("agentsList.."+agentsList,true);
		Map<String,String> agentInfoMap=new HashMap<String,String>();

		int size=agentsList.length();
		for( int i=0; i<size; i++)
		{
			JSONObject object = agentsList.getJSONObject(i);

			if(agent_state.equals(object.get("agent_state")) && agent_ip.equals(object.get("agent_ip"))  && agent_name.equals(object.get("agent_name")) )
			{

				agentInfoMap.put("agent_name", (String)object.get("agent_name"));
				//agentInfoMap.put("updated_version", (String)object.get("update_version"));
				agentInfoMap.put("agent_id", (String)object.get("agent_id"));
				agentInfoMap.put("current_version", (String)object.get("version"));
				break;
			}

		}
		return agentInfoMap;
	}
	
	/**
	 * @param spanVAConnectionObject
	 * @throws Exception
	 */
	public synchronized void validateSpanVADataSourceCommonFields(JSONObject spanVAConnectionObject) throws Exception {
		Assert.assertNotNull(spanVAConnectionObject.get("name"), "Data Source name is null");		
		Assert.assertFalse(((String) spanVAConnectionObject.get("name")).isEmpty(), "name is empty");
		Assert.assertEquals(spanVAConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);		
		Assert.assertFalse(((String) spanVAConnectionObject.get("log_transport")).isEmpty(), "log_transport is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertFalse(((String) spanVAConnectionObject.get("id")).isEmpty(), "id is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("resource_uri"), "Resource URI is null");		
		Assert.assertFalse(((String) spanVAConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_id"), "Agent Id is null");
		Assert.assertFalse(((String) spanVAConnectionObject.get("agent_id")).isEmpty(), "agent_id is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_name"), "Agent Id is null");
		Assert.assertFalse(((String) spanVAConnectionObject.get("agent_name")).isEmpty(), "agent_name is empty");
		JSONObject agentInfoObject=spanVAConnectionObject.getJSONObject("agent_info");
		Assert.assertNotNull(agentInfoObject.get("host"), "Agent host is null");
		Assert.assertFalse(((String) agentInfoObject.get("host")).isEmpty(), "host is empty");
		Assert.assertNotNull(agentInfoObject.get("log_collection_type"), "log_collection_type is null");
		Assert.assertFalse(((String) agentInfoObject.get("log_collection_type")).isEmpty(), "log_collection_type is empty");
	}
	
	/**
	 * @param spanVAConnectionObject
	 * @throws Exception
	 */
	public synchronized void validateSpanVASysLogDSCreateResponse(JSONObject spanVAConnectionObject) throws Exception{
		Assert.assertNotNull(spanVAConnectionObject.get("discovery_ds_id"), "discovery_ds_id is null");		
		Assert.assertFalse(((String) spanVAConnectionObject.get("discovery_ds_id")).isEmpty(), "discovery_ds_id is empty");
		Assert.assertEquals(spanVAConnectionObject.getBoolean("__audit_done__"), true," __audit_done__ field is not true");
		Assert.assertEquals(spanVAConnectionObject.getInt("current_version"), 1," current_version field is not 1");		
		Assert.assertEquals(spanVAConnectionObject.getBoolean("mark_deleted"), false," mark_deleted field is not true");
		Assert.assertEquals(spanVAConnectionObject.getString("flex_format"), "null"," flex_format field is not 1");
		JSONObject agentInfoObject=spanVAConnectionObject.getJSONObject("agent_info");
		Assert.assertEquals(agentInfoObject.getString("protocol"), "bsd"," protocol field is doesn't match");
		Assert.assertNotNull(agentInfoObject.get("port"), "Agent port is null");
	}
}