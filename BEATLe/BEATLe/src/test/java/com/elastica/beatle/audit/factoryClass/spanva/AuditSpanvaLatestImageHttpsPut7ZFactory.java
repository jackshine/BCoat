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
public class AuditSpanvaLatestImageHttpsPut7ZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageHttpsPut7ZFactoryMethod(){
		return new Object[]{
		
				
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_7Z)
				
				
		};
	}
}
