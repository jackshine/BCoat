/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditTIAWebUploadTests;

/**
 * @author Mallesh
 *
 */
public class CEPAuditTIAWebUploadFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
														
				new AuditTIAWebUploadTests(AuditTestConstants.TIA_FIREWALL_MCA2)
				
		};
	}
}