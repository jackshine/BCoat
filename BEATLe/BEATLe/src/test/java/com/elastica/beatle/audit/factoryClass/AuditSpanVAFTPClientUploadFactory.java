package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAFtpClientTests;
public class AuditSpanVAFTPClientUploadFactory {
	@Factory
	public Object[] SpanVAUploadFactoryMethod(){
		return new Object[]{
				
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY)/*,
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED)*/
		};
	}
}
