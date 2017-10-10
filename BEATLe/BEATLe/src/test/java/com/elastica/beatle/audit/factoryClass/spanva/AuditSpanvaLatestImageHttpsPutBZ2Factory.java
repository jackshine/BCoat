/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;


import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAHttpsPutTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageHttpsPutBZ2Factory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageHttpsPutBZ2FactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_BZ2),
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2)
		};
	}
}
