package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditScpTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;


public class AuditScpUploadFactory{
	
	/*
	  @return
	 */
	@Factory
	public Object[] ScpUploadFactoryMethod(){
		return new Object[]{	
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS),
		        new AuditScpTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
		        new AuditScpTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
		        new AuditScpTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2),
		        new AuditScpTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ),
		        new AuditScpTests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH),
				new AuditScpTests(AuditTestConstants.FIREWALL_CHECKPOINT_CSV),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
				new AuditScpTests(AuditTestConstants.FIREWALL_MCAFEE_SEF),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH),
				new AuditScpTests(AuditTestConstants.FIREWALL_SQUID_PROXY)
				
				
			};
	}
}