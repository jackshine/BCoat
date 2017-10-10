/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditDSEqualityTests;
import com.elastica.beatle.tests.audit.AuditPreferenceTests;
import com.elastica.beatle.tests.audit.AuditScpTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;

/**
 * @author Mallesh
 *
 */
public class AuditWeeklyDatasourceEqualityTestsFactory {
	
	/**
	 * @return
	 */
	@Factory
	public Object[] DataSourceEqualityTestsFactoryMethod(){
		return new Object[]{
				new AuditDSEqualityTests(AuditTestConstants.FIREWALL_CISCO_CWS),
				new AuditDSEqualityTests(AuditTestConstants.FIREWALL_CISCO_WSA_W3C),
				new AuditDSEqualityTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG),
				new AuditDSEqualityTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditDSEqualityTests(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS),
				new AuditDSEqualityTests(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS),
				new AuditDSEqualityTests(AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS),
				new AuditDSEqualityTests(AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS)
				
				
		};
	}
}