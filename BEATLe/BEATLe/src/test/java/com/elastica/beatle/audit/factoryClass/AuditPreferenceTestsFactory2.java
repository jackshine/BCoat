/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionWebUploadTests;
//import com.elastica.beatle.tests.audit.AuditPreferenceTests2;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;


/**
 * @author anuvrath
 *
 */
public class AuditPreferenceTestsFactory2{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
			
				//new AuditPreferenceTests2(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				//new AuditPreferenceTests2(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
			    //new AuditPreferenceTests2(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA),
			    
			    //new AuditPreferenceTests2(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH),
			    
			    //new AuditPreferenceTests2(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS),
			    
			    //new AuditPreferenceTests2(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
			    
			    //new AuditPreferenceTests2(AuditTestConstants.FIREWALL_MCAFEE_SEF),
			    
		};
	}
}
