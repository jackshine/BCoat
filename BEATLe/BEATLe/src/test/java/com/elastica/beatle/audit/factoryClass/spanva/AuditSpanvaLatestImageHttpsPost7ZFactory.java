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
public class AuditSpanvaLatestImageHttpsPost7ZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageHttpsPost7ZFactoryMethod(){
		return new Object[]{
		
				
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_7Z)
				
				
		};
	}
}
