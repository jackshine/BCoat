package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditScpTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;


public class AuditScpUploadFactory2{
	
	/*
	  @return
	 */
	@Factory
	public Object[] ScpUploadFactoryMethod(){
		return new Object[]{	
				
			    new AuditScpTests(AuditTestConstants.FIREWALL_SCANSAFE),
				new AuditScpTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditScpTests(AuditTestConstants.FIREWALL_WEBSENSE_ARC),
				new AuditScpTests(AuditTestConstants.FIREWALL_SONICWALL),
				new AuditScpTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED),
				new AuditScpTests(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV),
				new AuditScpTests(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS),
				new AuditScpTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_WSA_ACCESS),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditScpTests(AuditTestConstants.FIREWALL_JUNIPER_SRX),
				
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_CU_HE_SP_ESC),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_CU_HE_QU_HE),
			};
	}
}