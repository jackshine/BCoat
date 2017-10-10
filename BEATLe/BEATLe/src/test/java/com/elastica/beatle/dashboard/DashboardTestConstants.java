/**
 * 
 */
package com.elastica.beatle.dashboard;


/**
 * @author Anuvrath Joshi
 *
 */
public class DashboardTestConstants {
	
	public static final String DASHBOARD_API_CONFIGURATION_FILEPATH = "/src/test/resources/dashboard/configurations/APIConfigurations.xml";
	public static final String ES_QUERY_JSON = "/src/test/resources/dashboard/data/esQueryLog.json";
	public static final String ES_QUERY_FOR_DETECT_INCIDENTS = "/src/test/resources/dashboard/data/esQueryDetect.json";
	
	public static enum USER_SEVERITY{
		HIGH_USERSEVERITY("min\": 60, \"max\": 100"),
		MEDIUM_USERSEVERITY("min\": 40, \"max\": 59"),
		LOW_USERSEVERITY("min\": 1, \"max\": 39");
		
		private String severityCode;
 
		private USER_SEVERITY(String severity) {
			severityCode = severity;
		}
 
		public String getSeverityCode() {
			return severityCode;
		}
	}
}