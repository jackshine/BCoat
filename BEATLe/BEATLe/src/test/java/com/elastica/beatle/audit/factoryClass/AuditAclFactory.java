/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditAccessRoleBasedTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditAclFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
				
				 new AuditAccessRoleBasedTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY)
		};
	}
}
