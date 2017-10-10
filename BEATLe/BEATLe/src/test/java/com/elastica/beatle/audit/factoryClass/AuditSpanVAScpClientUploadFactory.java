package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAScpClientTests;
public class AuditSpanVAScpClientUploadFactory {
	@Factory
	public Object[] SpanVAUploadFactoryMethod(){
		return new Object[]{
	
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED)
				
		};
	}
}
