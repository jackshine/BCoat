/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditPreferenceTests;
import com.elastica.beatle.tests.audit.AuditScpTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;

/**
 * @author Mallesh
 *
 */
public class ProdAuditScpUploadFactory extends AuditInitializeTests {
	
	/**
	 * @return
	 */
	@Factory
	public Object[] ScpUploadFactoryMethod(){
		return new Object[]{
				
				new AuditScpTests(AuditTestConstants.FIREWALL_PALOALTO_CSV),
				new AuditScpTests(AuditTestConstants.FIREWALL_CISCO_CWS),
				new AuditScpTests(AuditTestConstants.FIREWALL_BARRACUDA_ACTIVITYLOG),
				new AuditScpTests(AuditTestConstants.FIREWALL_WSA_ACCESS_DEVICE_IDS)
		};
	}
}