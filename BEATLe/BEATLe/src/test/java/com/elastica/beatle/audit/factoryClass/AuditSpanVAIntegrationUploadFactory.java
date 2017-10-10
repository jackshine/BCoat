package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditSpanVAIntegrationUploadTests;
import com.elastica.beatle.tests.audit.AuditSpanVAIntegrationUploadTests2;
//import com.elastica.beatle.tests.audit.AuditSpanVAUploadTests;
import com.elastica.beatle.tests.audit.AuditSpanVAUploadTests;

public class AuditSpanVAIntegrationUploadFactory {
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanVAUploadFactoryMethod(){
		return new Object[]{
				new AuditSpanVAIntegrationUploadTests2(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG)
		};
	}
}
