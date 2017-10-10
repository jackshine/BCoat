package com.elastica.beatle.tests.auditdummay;

import org.testng.annotations.Factory;

import org.testng.annotations.Test;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditScpTests;

public class AuditScpUploadFactory extends AuditInitializeTests {
	
	/**
	 * @return
	 */
	@Factory
	public Object[] ScpUploadFactoryMethod(){
		return new Object[]{
				new AuditScpTests(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV),
				new AuditScpTests(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS),
				new AuditScpTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY)
		};
	}
}
