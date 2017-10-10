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
public class EOEAuditTIAWebUploadFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
														
				new AuditTIAWebUploadTests(AuditTestConstants.TIA_FIREWALL_MCA2),
				new AuditTIAWebUploadTests(AuditTestConstants.TIA_FIREWALL_BC_ULDL_TBI_BBI),
				new AuditTIAWebUploadTests(AuditTestConstants.TIA_FIREWALL_BC_NO_SESSIONS_TBI)
			
		};
	}
}