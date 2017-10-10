/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAHttpsPostTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageHttpsPostBZ2Factory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageHttpsPostBZ2FactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_BZ2),
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2)
		};
	}
}
