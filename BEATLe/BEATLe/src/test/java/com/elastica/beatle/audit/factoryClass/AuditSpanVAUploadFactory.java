package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditSpanVAUploadTests;
public class AuditSpanVAUploadFactory {
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanVAUploadFactoryMethod(){
		return new Object[]{
				
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED)
			
				
		};
	}
}
