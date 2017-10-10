/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditPreferenceTests;
import com.elastica.beatle.tests.audit.AuditSanityWebUploadTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;

/**
 * @author Mallesh
 *
 */
public class AuditSanityWebUploadFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
														
				new AuditSanityWebUploadTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG)
				
		};
	}
}