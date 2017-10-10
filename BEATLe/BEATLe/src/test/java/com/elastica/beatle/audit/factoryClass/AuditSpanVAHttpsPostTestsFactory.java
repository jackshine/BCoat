package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAHttpsPostTests;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAHttpsPutTests;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVANetworkFileTests;
public class AuditSpanVAHttpsPostTestsFactory {
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanVAUploadFactoryMethod(){
		return new Object[]{
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY)/*,
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED)*/
				
			
		};
	}
}
