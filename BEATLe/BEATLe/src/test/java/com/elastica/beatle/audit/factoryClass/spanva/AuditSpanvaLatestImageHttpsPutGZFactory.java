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
public class AuditSpanvaLatestImageHttpsPutGZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageHttpsPutGZFactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_BE_PANCSV_GZ),
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_BE_WSAW3C_GZ)
		};
	}
}
