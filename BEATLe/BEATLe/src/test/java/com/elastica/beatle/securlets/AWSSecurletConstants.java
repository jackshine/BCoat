/**
 * 
 */
package com.elastica.beatle.securlets;

/**
 * @author anuvrath
 *
 */
public class AWSSecurletConstants {
	public static final long MAX_WAITTIME = 1500000;
	public static final long MINIMUM_WAITIME = 420000;
	public static final long WAIT_CLOCK = 120000;
	public static final long ITERATION_WAIT_TIME = 30000;
	public static final String POLICY_DOCUMENT_PATH = "/src/test/resources/securlets/securletsData/AWSPolicyDocument.json";
	public static final String POLICYROLE_DOCUMENT_PATH = "/src/test/resources/securlets/securletsData/AWSPolicyRole.json";
	public enum AMIEnum {
		US_EAST_1("ami-e3106686"),
	    US_WEST_1("ami-cd3aff89"),
	    US_WEST_2("ami-9ff7e8af"),    
	    EU_WEST_1("ami-69b9941e"),
	    EU_CENTRAL_1("ami-daaeaec7"),    
	    AP_SOUTHEAST_1("ami-52978200"),
	    AP_SOUTHEAST_2("ami-c11856fb"),
	    AP_NORTHEAST_1("ami-9a2fb89a"),
	    SA_EAST_1("ami-3b0c9926");
	    
		 private String region;

		 private AMIEnum(String region) {
			 this.region = region;
		 }
		 
		 /**
		 * @return
		 */
		public String getAMIId() {
			 return this.region;
		 }
	}

}
