package com.elastica.beatle.tests.audit;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestUtils;

public class AuditSpanVATests extends AuditInitializeTests {

	protected Client restClient;
	protected String spanVAPayload;
	protected String firewallLogFilePath;
	protected String agentId;
	protected String existingAgent;

	@BeforeClass
	public void init() throws Exception {
		spanVAPayload = AuditTestUtils.createAgentPayload();
		restClient = new Client();
	}

	/**
	 * The below test case will create new Registration key api[GET]:
	 * https://api-eoe.elastica-inc.com/auditspanva1com/api/admin/v1/agents
	 * 
	 * @throws Exception
	 */
	@Test(description = "createRegistrationKeyForNewAgent")
	public void testGenerateRegistrationKeyForNewAgent() throws Exception {
		Reporter.log("GenerateRegistrationKeyForNewAgent test case starts..",
				true);
		HttpResponse createResp = AuditFunctions
				.generateAgentRegistrationKey(restClient);
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(),
				HttpStatus.SC_OK);

		String strGetAllAgents = ClientUtil.getResponseBody(createResp);
		JSONObject agentsJsonObject = new JSONObject(strGetAllAgents);
		JSONArray objectArray = agentsJsonObject.getJSONArray("objects");
		if (!isEmpty(objectArray)) {
			JSONObject registrationKeyJsonResponse = objectArray
					.getJSONObject(0);
			validateRegistrationKeyData(registrationKeyJsonResponse);

			agentId = (String) registrationKeyJsonResponse.get("agent_id");
			Reporter.log("GenerateRegistrationKeyForNewAgent test case end..",
					true);

		}

	}

	/**
	 * The below test case will verify the Registration data for the new Agent
	 * api[GET]:
	 * https://api-eoe.elastica-inc.com/auditspanva1com/api/admin/v1/agents
	 * /{agentId}
	 * 
	 * @throws Exception
	 */
	@Test(description = "testGetRegistrationKeyDetailsForNewAgent", dependsOnMethods = "testGenerateRegistrationKeyForNewAgent")
	public void testGetRegistrationKeyDetailsForNewAgent() throws Exception {

		Reporter.log("GetRegistrationKeyForNewAgent test case starts..", true);
		HttpResponse getAgentResponse = AuditFunctions.getAgent(restClient,
				agentId);
		Assert.assertEquals(getAgentResponse.getStatusLine().getStatusCode(),
				HttpStatus.SC_OK);

		JSONObject agentInfo = (JSONObject) new JSONObject(
				ClientUtil.getResponseBody(getAgentResponse));
		validateRegistrationKeyData(agentInfo);
		Reporter.log("GetRegistrationKeyForNewAgent test case end..", true);

	}

	@Test(description = "deleteAgent", dependsOnMethods = "testGetRegistrationKeyDetailsForNewAgent")
	public void deleteAgent() throws Exception {
		Reporter.log("Deleting agent " + agentId, true);
		HttpResponse response = AuditFunctions.deleteAgent(restClient, agentId);
		Assert.assertEquals(response.getStatusLine().getStatusCode(),
				HttpStatus.SC_MOVED_PERMANENTLY);
	}

	/**
	 * The below test case will verify all the existing agents has able to
	 * download spanva image or not
	 * api[GET]https://api-eoe.elastica-inc.com/auditspanva1com
	 * /api/admin/v1/agent_download_urls/
	 * 
	 * @throws Exception
	 */
	@Test(description = "verifyAllAgentsShouldbeAbleToDownloadTheSpanVaImage")
	public void testgetAllAgentDownloadUrls() throws Exception {
		Reporter.log("testgetAllAgentDownloadUrls test case starts..", true);
		HttpResponse agentDownloadUrlsResp = AuditFunctions
				.getAllAgentsSpanVADetails(restClient);
		Assert.assertEquals(agentDownloadUrlsResp.getStatusLine()
				.getStatusCode(), HttpStatus.SC_OK);
		JSONObject agentsDownloadUrlData = (JSONObject) new JSONObject(
				ClientUtil.getResponseBody(agentDownloadUrlsResp))
				.getJSONArray("objects").get(0);
		// existingAgent

		validteAgentsDownloadUrlsResponse(agentsDownloadUrlData);

		existingAgent = (String) agentsDownloadUrlData.get("id");
		Reporter.log("testgetAllAgentDownloadUrls test case end..", true);

	}

	/**
	 * The below test case will the agennt has able to download spanva image or
	 * not
	 * api[GET]https://api-eoe.elastica-inc.com/auditspanva1com/api/admin/v1/
	 * agent_download_urls/{agentId}
	 * 
	 * @throws Exception
	 */
	@Test(description = "verifyAgentShouldDownloadtheSpanvaImage", dependsOnMethods = "testgetAllAgentDownloadUrls")
	public void testVerifyAgentShouldDownloadtheSpanvaImage() throws Exception {
		Reporter.log(
				"testVerifyAgentShouldDownloadtheSpanvaImage test case starts..",
				true);
		HttpResponse agentResp = AuditFunctions.getAgentsSpanVADetails(
				restClient, existingAgent);
		Assert.assertEquals(agentResp.getStatusLine().getStatusCode(),
				HttpStatus.SC_OK);

		JSONObject agentSpanVAInformation = (JSONObject) new JSONObject(
				ClientUtil.getResponseBody(agentResp));

		validteAgentsDownloadUrlsResponse(agentSpanVAInformation);
		Reporter.log(
				"testVerifyAgentShouldDownloadtheSpanvaImage test case ends..",
				true);

	}

	/**
	 * The below test case will return the Agents list
	 * api[GET]https://api-eoe.elastica
	 * -inc.com/auditspanva1com/api/admin/v1/agents
	 * 
	 * @throws Exception
	 */
	@Test(description = "verifyAgentsList")
	public void getAllAgentsList() throws Exception {

		Reporter.log("getAllAgentsList test case starts..", true);
		HttpResponse getAllAgents = AuditFunctions.getAllAgentsList(restClient);
		Assert.assertEquals(getAllAgents.getStatusLine().getStatusCode(),
				HttpStatus.SC_OK);
		/*
		 * JSONObject agentsList = (JSONObject) new JSONObject(
		 * ClientUtil.getResponseBody(getAllAgents))
		 * .getJSONArray("objects").get(0);
		 */

		String strGetAllAgents = ClientUtil.getResponseBody(getAllAgents);
		JSONObject agentsJsonObject = new JSONObject(strGetAllAgents);
		JSONArray objectArray = agentsJsonObject.getJSONArray("objects");
		if (!isEmpty(objectArray)) {

			JSONObject agentsList = objectArray.getJSONObject(0);

			validatedAgents(agentsList);
			Reporter.log("getAllAgentsList test case ends..", true);

		}

	}

	/**
	 * This method validate new Registration meta data
	 * 
	 * @param registrationKeyJsonResponse
	 * @throws Exception
	 */
	private void validateRegistrationKeyData(
			JSONObject registrationKeyJsonResponse) throws Exception {

		Assert.assertNotNull(registrationKeyJsonResponse.get("agent_id"),
				"Agent ID is null");
		Assert.assertNotNull(registrationKeyJsonResponse.get("agent_name"),
				"Agent name is null");
		Assert.assertNotNull(
				registrationKeyJsonResponse.get("registration_token"),
				"Registration token is null");
		Assert.assertNotNull(registrationKeyJsonResponse.get("resource_uri"),
				"Resource URI is null");
		Assert.assertNotNull(registrationKeyJsonResponse.get("agent_state"),
				"Aget State is null");
		Assert.assertNotNull(registrationKeyJsonResponse.get("agent_state"),
				"Agent State is Null");
		// Assert.assertEquals("New",registrationKeyJsonResponse.get("agent_state"),
		// "Agent State is New for the new registration key");

	}

	public void validteAgentsDownloadUrlsResponse(JSONObject agentsSpanVAInfo)
			throws Exception {
		Assert.assertNotNull(agentsSpanVAInfo.get("id"), "Agent Id is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("download_url"),
				"download_url is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("created_on"),
				"Created on  is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("image_md5"),
				"image_md5  is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("resource_uri"),
				"Resource_uri on  is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("image_path"),
				"image path  is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("version"), "version is null");

	}

	public void validatedAgents(JSONObject agentsSpanVAInfo) throws Exception {
		Assert.assertNotNull(agentsSpanVAInfo.get("agent_name"),
				"SpanVA Agent Name is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("agent_id"),
				"SpanVA Agent Id is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("agent_ip"),
				"SpanVA Agent IP is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("agent_state"),
				"SpanVA Agent State is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("agent_updates_url"),
				"SpanVA agent_updates_url is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("cpu_usage"),
				"CPU utilization  is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("disk_usage"),
				"disk_usage utillization  is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("mem_usage"),
				"memory utilization is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("registration_token"),
				"registration_token is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("resource_uri"),
				"resource_uri is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("update_version"),
				"update_version is null");
		Assert.assertNotNull(agentsSpanVAInfo.get("version"), "version is null");

	}

	public Boolean isEmpty(JSONArray array) {
		return array.length() == 0;
	}
}
