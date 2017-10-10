/**
 * 
 */
package com.elastica.beatle;



/**
 * @author pushparaj
 *
 */
public class TestRail {

	private static APIClient client = null;
	private TestRail() {
		// Exists only to defeat instantiation.
	}
	public static APIClient getInstance() {
		if(client == null) {
			client = new APIClient("https://elastica.testrail.com/");
			client.setUser("reviewer@elastica.co");
			client.setPassword("review@elastica");
		}
		return client;
	}
	
	public String getTestRun(String name) {
		APIClient client = TestRail.getInstance();
		return "";
	}
	
	public void updateTestCase(String caseId, String run) {
		
	}
}
