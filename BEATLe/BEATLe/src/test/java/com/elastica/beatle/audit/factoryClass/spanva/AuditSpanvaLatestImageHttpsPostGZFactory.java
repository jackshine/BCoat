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
public class AuditSpanvaLatestImageHttpsPostGZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageHttpsPostGZFactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_BE_PANCSV_GZ),
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_BE_WSAW3C_GZ)
		};
	}
}
