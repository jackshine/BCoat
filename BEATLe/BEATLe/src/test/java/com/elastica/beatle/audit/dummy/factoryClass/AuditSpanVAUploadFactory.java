package com.elastica.beatle.audit.dummy.factoryClass;

import org.testng.annotations.Factory;


import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditSpanVAIntegrationUploadTests;
//import com.elastica.beatle.tests.audit.AuditSpanVAUploadTests;
import com.elastica.beatle.tests.audit.AuditSpanVAUploadTests;

public class AuditSpanVAUploadFactory {
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanVAUploadFactoryMethod(){
		return new Object[]{
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_CISCO_WSA_W3C),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_ZSCALAR)
				
		};
	}
}
