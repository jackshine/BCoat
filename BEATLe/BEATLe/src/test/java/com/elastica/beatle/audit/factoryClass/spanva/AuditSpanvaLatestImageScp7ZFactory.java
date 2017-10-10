/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditSpanVAUploadTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageScp7ZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageScp7ZFactoryMethod(){
		return new Object[]{
		
				
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED_7Z)
				
				
		};
	}
}
