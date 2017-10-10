package com.elastica.beatle.tests.protect.miscellaneous;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

import org.codehaus.jackson.JsonProcessingException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.ProtectAlertFilter;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.SecurletUtils;
import com.universal.common.GDrive;

/**
 * 
 * @author Shri
 *
 */

public class ProtectAlertFilterCombinationTests extends ProtectInitializeTests {
	Client restClient;
	GDrive gDrive;
	ProtectFunctions protectFunctions;

	List<String> services;
	List<String> users;
	List<String> policyTypes;
	List<String> policies;
	List<String> severity;
	ProtectAlertFilter protectAlertFilter;

	/**
	 * This method initializes the filter data, based on which test validations
	 * are performed
	 * 
	 * @param data
	 * @throws Exception
	 */
	@BeforeTest(alwaysRun = true)
	public void setUpData() throws Exception {
		restClient = new Client();
		protectFunctions = new ProtectFunctions();
		String responseBody = getPolicyAlerts(protectAlertFilter);
		String facets = ClientUtil.getJSONValue(responseBody, "facets");
		services = filterNamesList(populateAlertFilterDtlsMap(ClientUtil.getJSONValue(facets, "facility")));
		policyTypes = filterNamesList(populateAlertFilterDtlsMap(ClientUtil.getJSONValue(facets, "policy_type")));
		users = new ArrayList<String>();
		users.add(filterNamesList(populateAlertFilterDtlsMap(ClientUtil.getJSONValue(facets, "user"))).get(0));
		users.add(filterNamesList(populateAlertFilterDtlsMap(ClientUtil.getJSONValue(facets, "user"))).get(1));
		policies = new ArrayList<String>();
		policies.add(filterNamesList(populateAlertFilterDtlsMap(ClientUtil.getJSONValue(facets, "_PolicyViolated"))).get(0));
		policies.add(filterNamesList(populateAlertFilterDtlsMap(ClientUtil.getJSONValue(facets, "_PolicyViolated"))).get(1));
		setSeverity();
		Logger.info("Service Filter Size: " + services.size());
		Logger.info("Policy Name Filter Size: " + policies.size());
		Logger.info("Policy Type Filter Size: " + policyTypes.size());
		Logger.info("Users Filter Size: " + users.size());
		Logger.info("Severity Filter Size: " + severity.size());
	}

	/**
	 * 
	 * @param serviceUserPolicyTypeSeverityProvider
	 * @throws Exception
	 */
	@Test(dataProvider = "ServiceUserPolicyTypeSeverityProviderList")
	public void filterServiceUserPolicyTypeSeverity(
			List<Object[]> serviceUserPolicyTypeSeverityProvider) throws Exception {
		String service, user, policyType, severity;

		Iterator<Object[]> iterator = serviceUserPolicyTypeSeverityProvider.iterator();
		while (iterator.hasNext()) {
			Object[] objItr = iterator.next();
			String strArr = Arrays.copyOf(objItr, objItr.length, String[].class)[0];
			String[] data = strArr.split(",");

			protectAlertFilter = new ProtectAlertFilter();
			try {
				service = data[0];
				user = data[1];
				policyType = data[2];
				severity = data[3];
				protectAlertFilter.setService(service);
				protectAlertFilter.setUser(user);
				protectAlertFilter.setSeverity(severity);
				protectAlertFilter.setPolicyType(policyType);
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + data, "2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + data);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	/**
	 * 
	 * @param serviceUserSeverityProvider
	 * @throws Exception
	 */
	@Test(dataProvider = "ServiceUserSeverityProviderList")
	public void filterServiceUserSeverityProvider(List<Object[]> serviceUserSeverityProvider)
			throws Exception {
		String service, user, severity;

		Iterator<Object[]> iterator = serviceUserSeverityProvider.iterator();
		while (iterator.hasNext()) {
			Object[] objItr = iterator.next();
			String strArr = Arrays.copyOf(objItr, objItr.length, String[].class)[0];
			String[] data = strArr.split(",");
			protectAlertFilter = new ProtectAlertFilter();
			try {

				service = data[0];
				user = data[1];
				severity = data[2];
				protectAlertFilter.setService(service);
				protectAlertFilter.setUser(user);
				protectAlertFilter.setSeverity(severity);
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + serviceUserSeverityProvider,
					"2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + serviceUserSeverityProvider);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	/**
	 * 
	 * @param serviceUserPolicyTypesProvider
	 * @throws Exception
	 */
	@Test(dataProvider = "ServiceUserPolicyTypesProviderList")
	public void filterServiceUserPolicyTypes(List<Object[]> serviceUserPolicyTypesProvider)
			throws Exception {
		String service, user, policyType;

		Iterator<Object[]> iterator = serviceUserPolicyTypesProvider.iterator();
		while (iterator.hasNext()) {
			Object[] objItr = iterator.next();
			String strArr = Arrays.copyOf(objItr, objItr.length, String[].class)[0];
			String[] data = strArr.split(",");
			protectAlertFilter = new ProtectAlertFilter();

			try {
				service = data[0];
				user = data[1];
				policyType = data[2];
				protectAlertFilter.setService(service);
				protectAlertFilter.setUser(user);
				protectAlertFilter.setPolicyType(policyType);
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + serviceUserPolicyTypesProvider,
					"2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + serviceUserPolicyTypesProvider);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	/**
	 * 
	 * @param servicesAndUsers
	 * @throws Exception
	 */
	@Test(dataProvider = "ServiceAndUserProviderList")
	public void filterServicesAndUsers(List<Object[]> servicesAndUsers) throws Exception {
		String service, user;
		Iterator<Object[]> iterator = servicesAndUsers.iterator();
		while (iterator.hasNext()) {
			Object[] objItr = iterator.next();
			String strArr = Arrays.copyOf(objItr, objItr.length, String[].class)[0];
			String[] data = strArr.split(",");

			protectAlertFilter = new ProtectAlertFilter();
			try {
				service = data[0];
				user = data[1];
				protectAlertFilter.setService(service);
				protectAlertFilter.setUser(user);
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + servicesAndUsers,
					"2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + servicesAndUsers);

			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	/**
	 * 
	 * @param serviceUserPoliciesProvider
	 * @throws Exception
	 */
	@Test(dataProvider = "ServiceUserPoliciesProviderList")
	public void filterServicesUsersPolicyNames(List<Object[]> serviceUserPoliciesProvider) throws Exception {
		String service, user, policy;

		Iterator<Object[]> iterator = serviceUserPoliciesProvider.iterator();
		while (iterator.hasNext()) {
			Object[] objItr = iterator.next();
			String strArr = Arrays.copyOf(objItr, objItr.length, String[].class)[0];
			String[] data = strArr.split(",");
			protectAlertFilter = new ProtectAlertFilter();
			try {
				service = data[0];
				user = data[1];
				policy = data[2];
				protectAlertFilter.setService(service);
				protectAlertFilter.setUser(user);
				protectAlertFilter.setPolicy(policy);
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + serviceUserPoliciesProvider,
					"2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + serviceUserPoliciesProvider);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);
		}

	}

	/**
	 * 
	 * @param serviceUserPoliciesWithTypesProvider
	 * @throws Exception
	 */
	@Test(dataProvider = "ServiceUserPoliciesWithTypesProviderList")
	public void filterServicesUsersPolicyNamesWithTypes(List<Object[]> serviceUserPoliciesWithTypesProvider)
			throws Exception {
		String service, user, policy, policyType;

		Iterator<Object[]> iterator = serviceUserPoliciesWithTypesProvider.iterator();
		while (iterator.hasNext()) {
			Object[] objItr = iterator.next();
			String strArr = Arrays.copyOf(objItr, objItr.length, String[].class)[0];
			String[] data = strArr.split(",");

			protectAlertFilter = new ProtectAlertFilter();
			try {
				service = data[0];
				user = data[1];
				policy = data[2];
				policyType = data[3];

				protectAlertFilter.setService(service);
				protectAlertFilter.setUser(user);
				protectAlertFilter.setPolicy(policy);
				protectAlertFilter.setPolicyType(policyType);
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + serviceUserPoliciesWithTypesProvider,
					"2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + serviceUserPoliciesWithTypesProvider);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	/**
	 * 
	 * @param serviceUserPoliciesWithTypesAndSeverityProvider
	 * @throws Exception
	 */
	@Test(dataProvider = "ServiceUserPoliciesWithTypesAndSeverityProviderList")
	public void filterServiceUserPoliciesWithTypesAndSeverity(
			List<Object[]> serviceUserPoliciesWithTypesAndSeverityProvider) throws Exception {
		String service, user, policy, policyType, severity;

		Iterator<Object[]> iterator = serviceUserPoliciesWithTypesAndSeverityProvider.iterator();
		while (iterator.hasNext()) {
			Object[] objItr = iterator.next();
			String strArr = Arrays.copyOf(objItr, objItr.length, String[].class)[0];
			String[] data = strArr.split(",");

			protectAlertFilter = new ProtectAlertFilter();
			try {
				service = data[0];
				user = data[1];
				policy = data[2];
				policyType = data[3];
				severity = data[4];

				protectAlertFilter.setService(service);
				protectAlertFilter.setUser(user);
				protectAlertFilter.setPolicy(policy);
				protectAlertFilter.setPolicyType(policyType);
				protectAlertFilter.setPolicyType(severity);
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + serviceUserPoliciesWithTypesAndSeverityProvider,
					"2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + serviceUserPoliciesWithTypesAndSeverityProvider);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	@Test
	public void testAlertServiceNameSearch() throws Exception {

		protectAlertFilter = new ProtectAlertFilter();
		protectAlertFilter.setService("Box");
		protectAlertFilter.setSearchFilter(true);

		// search by service name
		String policyAlertsSearchResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);
		int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsSearchResponse, "hits"), "total"));
		policyAlertsSearchResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
		// iterate the list and assert for service
		assertFilters(policyAlertsSearchResponse, protectAlertFilter);

	}

	@Test
	public void testAlertUserNameSearch() throws Exception {

		protectAlertFilter = new ProtectAlertFilter();
		protectAlertFilter.setUser("box-admin@protectbeatle.com");
		protectAlertFilter.setSearchFilter(true);

		// search by service name
		String policyAlertsSearchResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);
		int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsSearchResponse, "hits"), "total"));
		policyAlertsSearchResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
		// iterate the list and assert for service
		assertFilters(policyAlertsSearchResponse, protectAlertFilter);

	}

	/**
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleServiceDataProviderList")
	public void filterServices(List<Object[]> service) throws Exception {

		Iterator<Object[]> iterator = service.iterator();
		while (iterator.hasNext()) {
			protectAlertFilter = new ProtectAlertFilter();
			try {
				protectAlertFilter.setService(iterator.next().toString());
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + service,
					"2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + service);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}
	}

	/**
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleUserDataProviderList")
	public void filterUsers(List<Object[]> user) throws Exception {

		Iterator<Object[]> iterator = user.iterator();
		while (iterator.hasNext()) {

			protectAlertFilter = new ProtectAlertFilter();
			try {
				protectAlertFilter.setUser(iterator.next().toString());
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + user, "2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + user);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	/**
	 * @throws Exception
	 */
	@Test(dataProvider = "SinglePolicyDataProviderList")
	public void filterPolicies(List<Object[]> policy) throws Exception {

		Iterator<Object[]> iterator = policy.iterator();
		while (iterator.hasNext()) {

			protectAlertFilter = new ProtectAlertFilter();
			try {

				protectAlertFilter.setPolicy(iterator.next().toString());
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + policy, "2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + policy);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	/**
	 * @throws Exception
	 */
	@Test(dataProvider = "SinglePolicyTypeDataProviderList")
	public void filterPolicyTypes(List<Object[]> policyType) throws Exception {

		Iterator<Object[]> iterator = policyType.iterator();
		while (iterator.hasNext()) {

			protectAlertFilter = new ProtectAlertFilter();
			try {

				protectAlertFilter.setPolicyType(iterator.next().toString());
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + policyType,
					"2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + policyType);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	/**
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleSeverityDataProviderList")
	public void filterSeverity(List<Object[]> severity) throws Exception {

		Iterator<Object[]> iterator = severity.iterator();
		while (iterator.hasNext()) {

			protectAlertFilter = new ProtectAlertFilter();
			try {

				protectAlertFilter.setSeverity(iterator.next().toString());
			} catch (NullPointerException e) {
				throw new SkipException("Skipping this test");
			}

			String steps[] = { "1. Get the policy alerts based on the filter: " + severity,
					"2. Verify the applied filters in all the alerts" };

			LogUtils.logTestDescription(steps);
			LogUtils.logStep("1. Get the policy alerts based on the filter: " + severity);
			String policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);

			// Get total count
			int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
			policyAlertsResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, totalCount);
			LogUtils.logStep("2. Verify the applied filters in all the alerts");
			assertFilters(policyAlertsResponse, protectAlertFilter);

		}

	}

	@DataProvider(name = "SingleServiceDataProviderList")
	public Object[][] singleServiceDataProvider() throws Exception {
		List<String> allcombinations = new SecurletUtils().getAllCombinationsOfAList(services, 1);
		List<Object[]> services = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			services.add(data);
		}
		return new Object[][] { { services } };
	}

	@DataProvider(name = "SingleUserDataProviderList")
	public Object[][] singleUserDataProvider() throws Exception {
		List<String> allcombinations = new SecurletUtils().getAllCombinationsOfAList(users, 1);
		List<Object[]> users = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			users.add(data);
		}
		return new Object[][] { { users } };
	}

	@DataProvider(name = "SinglePolicyDataProviderList")
	public Object[][] singlePolicyDataProvider() throws Exception {
		List<String> allcombinations = new SecurletUtils().getAllCombinationsOfAList(policies, 1);
		List<Object[]> policies = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			policies.add(data);
		}
		return new Object[][] { { policies } };
	}

	@DataProvider(name = "SinglePolicyTypeDataProviderList")
	public Object[][] singlePolicyTypeDataProvider() throws Exception {
		List<String> allcombinations = new SecurletUtils().getAllCombinationsOfAList(policyTypes, 1);
		List<Object[]> policyTypes = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			policyTypes.add(data);
		}
		return new Object[][] { { policyTypes } };
	}

	@DataProvider(name = "SingleSeverityDataProviderList")
	public Object[][] singleSeverityDataProvider() throws Exception {
		List<String> allcombinations = new SecurletUtils().getAllCombinationsOfAList(severity, 1);
		List<Object[]> severities = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			severities.add(data);
		}
		return new Object[][] { { severities } };
	}

	/**
	 * 
	 * @param policyAlertsResponse
	 * @param protectAlertFilter
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private void assertFilters(String policyAlertsResponse, ProtectAlertFilter protectAlertFilter) throws JsonProcessingException,
			IOException {

		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "hits");
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, "hits"), "total"));
		int serviceCount = 0;
		int userCount = 0;
		int policyCount = 0;
		int policyTypeCount = 0;
		int severityCount = 0;

		if (jArray.size() == 0)
			Logger.info("No Alert combination is not found for this combination");

		for (int i = 0; i < jArray.size(); i++) {
			String source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String service = ClientUtil.getJSONValue(source, "facility");
			if (protectAlertFilter.getService() != null) {
				serviceCount = serviceCount + 1;
				Logger.info("Services...");
				Logger.info("Actual:" + protectAlertFilter.getService());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, "facility"));
				boolean isServiceMatch = protectAlertFilter.getService().equals(service.substring(1, service.length() - 1))
						|| "Across Services".equals(service.substring(1, service.length() - 1));
				Assert.assertTrue(isServiceMatch);
			}
			if (protectAlertFilter.getUser() != null) {
				userCount = userCount + 1;
				String user = ClientUtil.getJSONValue(source, "user");
				Logger.info("Users...");
				Logger.info("Actual:" + protectAlertFilter.getUser());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, "user"));
				Assert.assertTrue(protectAlertFilter.getUser().equals(user.substring(1, user.length() - 1)));
			}
			if (protectAlertFilter.getPolicy() != null) {
				policyCount = policyCount + 1;
				String policy = ClientUtil.getJSONValue(source, "_PolicyViolated");
				Logger.info("Policy Name...");
				Logger.info("Actual:" + protectAlertFilter.getPolicy());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, "_PolicyViolated"));
				Assert.assertTrue(protectAlertFilter.getPolicy().equals(policy.substring(1, policy.length() - 1)));
			}
			if (protectAlertFilter.getPolicyType() != null) {
				policyTypeCount = policyTypeCount + 1;
				String policyType = ClientUtil.getJSONValue(source, "policy_type");
				Logger.info("Policy Types...");
				Logger.info("Actual:" + protectAlertFilter.getPolicyType());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, "policy_type"));
				Assert.assertTrue(protectAlertFilter.getPolicyType().equals(policyType.substring(1, policyType.length() - 1)));
			}
			if (protectAlertFilter.getSeverity() != null) {
				severityCount = severityCount + 1;
				String severity = ClientUtil.getJSONValue(source, "severity");
				Logger.info("Severity...");
				Logger.info("Actual:" + protectAlertFilter.getSeverity());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, "severity"));
				Assert.assertTrue(protectAlertFilter.getSeverity().equals(severity.substring(1, severity.length() - 1)));
			}

		}

		if (serviceCount > 0) {
			Logger.info("Service Count:" + serviceCount);
			Logger.info("Display Total Service Count:" + totalCount);
			Assert.assertTrue(serviceCount == totalCount);
		}
		if (userCount > 0) {
			Logger.info("User Count:" + userCount);
			Logger.info("Display Total Service Count:" + totalCount);
			Assert.assertTrue(userCount == totalCount);
		}
		if (policyCount > 0) {
			Logger.info("Policy Count:" + policyCount);
			Logger.info("Display Total Service Count:" + totalCount);
			Assert.assertTrue(policyCount == totalCount);
		}
		if (policyTypeCount > 0) {
			Logger.info("Policy Type Count:" + policyTypeCount);
			Logger.info("Display Total Service Count:" + totalCount);
			Assert.assertTrue(policyTypeCount == totalCount);
		}
		if (severityCount > 0) {
			Logger.info("Severity Count:" + severityCount);
			Logger.info("Display Total Service Count:" + totalCount);
			Assert.assertTrue(severityCount == totalCount);
		}

	}

	/**
	 * 
	 * @param filterMap
	 * @return
	 */
	private List<String> filterNamesList(Map<String, String> filterMap) {
		Iterator<Map.Entry<String, String>> entries = filterMap.entrySet().iterator();
		List<String> filterList = new ArrayList<String>();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			filterList.add(entry.getKey().toString());
		}
		return filterList;
	}

	/**
	 * 
	 * @param protectAlertFilter
	 * @return
	 * @throws Exception
	 */
	private String getPolicyAlerts(ProtectAlertFilter protectAlertFilter) throws Exception {
		String responseBody = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 30);
		while (responseBody.startsWith("Error")) {
			responseBody = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 30);
		}
		return responseBody;
	}

	/**
	 * 
	 * @param filter
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private HashMap<String, String> populateAlertFilterDtlsMap(String filter) throws JsonProcessingException, IOException {
		String terms = ClientUtil.getJSONValue(filter, "terms");
		JSONArray jsonArray = (JSONArray) new JSONTokener(terms).nextValue();
		HashMap<String, String> filterMap = new HashMap<String, String>();

		for (int i = 0; i < jsonArray.size(); i++) {
			String term = ClientUtil.getJSONValue(jsonArray.getJSONObject(i).toString(), "term");
			term = term.substring(1, term.length() - 1);
			String count = ClientUtil.getJSONValue(jsonArray.getJSONObject(i).toString(), "count");
			filterMap.put(term, count);
		}

		return filterMap;
	}

	/**
	 * 
	 * @param services
	 * @param users
	 * @param policies
	 * @param policyType
	 * @param severity
	 * @return
	 */
	public List<String> generateCombinations(List<String> services, List<String> users, List<String> policies, List<String> policyType,
			List<String> severity) {
		List<String> resultList = new ArrayList<String>();

		LinkedList<List<String>> lists = new LinkedList<List<String>>();
		if (services != null && services.size() > 0) {
			lists.add(services);
		}
		if (users != null && users.size() > 0) {
			lists.add(users);
		}

		if (policies != null && policies.size() > 0) {
			lists.add(policies);
		}

		if (policyType != null && policyType.size() > 0) {
			lists.add(policyType);
		}

		if (severity != null && severity.size() > 0) {
			lists.add(severity);
		}

		Set<String> combinations = new TreeSet<String>();
		Set<String> newCombinations;

		for (String s : lists.removeFirst())
			combinations.add(s);

		while (!lists.isEmpty()) {
			List<String> next = lists.removeFirst();
			newCombinations = new TreeSet<String>();
			for (String s1 : combinations)
				for (String s2 : next)
					newCombinations.add(s1 + "," + s2);

			combinations = newCombinations;
		}
		Reporter.log("Number of combinations:" + combinations.size(), true);

		for (String s : combinations) {
			Logger.info(s);
			resultList.add(s);
		}
		return resultList;
	}

	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "ServiceAndUserProviderList")
	public Object[][] serviceAndUserProviderList() throws InterruptedException {
		List<String> allcombinations = generateCombinations(services, users, null, null, null);
		Logger.info("Number of Service and User Combinations: " + allcombinations.size());
		List<Object[]> filters = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			filters.add(data);
		}
		return new Object[][] { { filters } };
	}

	@DataProvider(name = "ServiceUserPoliciesProviderList")
	public Object[][] serviceUserPoliciesProvider() throws InterruptedException {
		List<String> allcombinations = generateCombinations(services, users, policies, null, null);
		Logger.info("Number of Service, User and Policies Combinations: " + allcombinations.size());
		List<Object[]> filters = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			filters.add(data);
		}
		return new Object[][] { { filters } };
	}

	@DataProvider(name = "ServiceUserPoliciesWithTypesProviderList")
	public Object[][] serviceUserPoliciesWithTypesProvider() throws InterruptedException {
		List<String> allcombinations = generateCombinations(services, users, policies, policyTypes, null);
		Logger.info("Number of Service, User, Policies and Types Combinations: " + allcombinations.size());
		List<Object[]> filters = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			filters.add(data);
		}
		return new Object[][] { { filters } };
	}

	@DataProvider(name = "ServiceUserPoliciesWithTypesAndSeverityProviderList")
	public Object[][] serviceUserPoliciesWithTypesAndSeverityProvider() throws InterruptedException {
		List<String> allcombinations = generateCombinations(services, users, policies, policyTypes, severity);
		Logger.info("Number of Service, User, Policies, Types and Severity Combinations: " + allcombinations.size());
		List<Object[]> filters = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			filters.add(data);
		}
		return new Object[][] { { filters } };
	}

	@DataProvider(name = "ServiceUserPolicyTypesProviderList")
	public Object[][] serviceUserPolicyTypesProvider() throws InterruptedException {
		List<String> allcombinations = generateCombinations(services, users, null, policyTypes, null);
		Logger.info("Number of Service, User and Policy Types Combinations: " + allcombinations.size());
		List<Object[]> filters = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			filters.add(data);
		}
		return new Object[][] { { filters } };
	}

	@DataProvider(name = "ServiceUserSeverityProviderList")
	public Object[][] serviceUserSeverityProvider() throws InterruptedException {
		List<String> allcombinations = generateCombinations(services, users, null, null, severity);
		Logger.info("Number of Service, User and Severity Combinations: " + allcombinations.size());
		List<Object[]> filters = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			filters.add(data);
		}
		return new Object[][] { { filters } };
	}

	@DataProvider(name = "ServiceUserPolicyTypeSeverityProviderList")
	public Object[][] serviceUserPolicyTypeSeverity() throws InterruptedException {
		List<String> allcombinations = generateCombinations(services, users, null, policyTypes, severity);
		Logger.info("Number of Service, User, Policy Type and Severity Combinations: " + allcombinations.size());
		List<Object[]> filters = new ArrayList<>();
		Object[] data = null;
		for (String exp : allcombinations) {
			data = new Object[] { exp };
			filters.add(data);
		}
		return new Object[][] { { filters } };
	}

	private void setSeverity() {
		severity = new ArrayList<String>();
		severity.add("critical");
		severity.add("high");
		severity.add("medium");
		severity.add("low");
		severity.add("information");
	}

}