package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditSpanVAUploadTests;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAHttpsPutTests;
public class AuditSpanVAHttpsFactory {
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanVAUploadFactoryMethod(){
		return new Object[]{
				
				/*new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),*/
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED)
				
			};
	}
}
