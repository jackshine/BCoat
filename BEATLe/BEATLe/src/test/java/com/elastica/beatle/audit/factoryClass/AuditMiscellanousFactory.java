/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditMiscellaneousTests;
import com.elastica.beatle.tests.audit.AuditPreferenceTests;
import com.elastica.beatle.tests.audit.AuditScpMultiFileCopyTests;

/**
 * @author mallesh
 *
 */
public class AuditMiscellanousFactory extends AuditInitializeTests {
	/**
	 * @return
	 */
	@Factory
	public Object[] MiscellanousFactoryMethod(){
		return new Object[]{
				
				new AuditPreferenceTests(),
				//equality tests
				//new AuditDSEqualityTests(AuditTestConstants.FIREWALL_CISCO_CWS), // for negative test added this.
				
				//empty logs
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_WSA_ACCESS_ZIP),
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_WSA_ACCESS_GZ),
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_WSA_ACCESS_BZ2),
				
				//header only
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_CWS_HEADERONLY_ZIP),
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_CWS_HEADERONLY_GZ),
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_CWS_HEADERONLY_BZ2),
				
				
				//image  logs
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_CISCO_ASA_IMG_ZIP),
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_CISCO_ASA_IMG_GZ),
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_CISCO_ASA_IMG_BZ2),
				
				//exe logs
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_PAN_CSV_EXE_ZIP),
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_PAN_CSV_EXE_GZ),
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_PAN_CSV_EXE_BZ2),
				
				//file with empty jsp header check
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_INVALID),
				
				//Pan csv url only file
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_PAN_TRAFFIC_FILE),
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_PAN_URL_FILE),
				
				//File with slash
				new AuditMiscellaneousTests(AuditTestConstants.FIREWALL_FILE_WITH_SLASH),
				
               //Multi files with single scp
				//new AuditScpMultiFileCopyTests(AuditTestConstants.FIREWALL_WSA_ACCESS_HEADER_ONLY),
				new AuditScpMultiFileCopyTests(AuditTestConstants.FIREWALL_MCA_WEBGATEWAY_NEGATIVE)
				
								
		};
	}
}