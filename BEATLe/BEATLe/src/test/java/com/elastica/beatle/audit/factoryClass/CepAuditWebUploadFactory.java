/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;

/**
 * @author Mallesh
 *
 */
public class CepAuditWebUploadFactory {
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
				
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_WSA_ACCESS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_CHECKPOINT_CSV),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_MCAFEE_SEF),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_PANCSV)
				
				
				
				
		};
	}
}