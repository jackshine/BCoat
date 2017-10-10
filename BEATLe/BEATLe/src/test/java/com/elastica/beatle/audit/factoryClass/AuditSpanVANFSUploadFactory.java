package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVANetworkFileTests;
public class AuditSpanVANFSUploadFactory {
	@Factory
	public Object[] SpanVAUploadFactoryMethod(){
		return new Object[]{
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED)
		};
	}
}
