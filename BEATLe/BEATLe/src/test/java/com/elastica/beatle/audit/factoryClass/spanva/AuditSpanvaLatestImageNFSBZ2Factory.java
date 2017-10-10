/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVANetworkFileTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageNFSBZ2Factory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageNFSBZ2FactoryMethod(){
		return new Object[]{
		
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2),
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_BZ2)
				
		};
	}
}
