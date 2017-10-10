/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;


/**
 * @author anuvrath
 *
 */
public class AuditWebUploadFactory2 {

	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod() {
		return new Object[] {

				new AuditWebUploadTests(AuditTestConstants.FIREWALL_SCANSAFE),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_WEBSENSE_ARC),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_SONICWALL),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_CU_HE_SP_ESC),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_CU_HE_QU_HE),
				

		};
	}
}
