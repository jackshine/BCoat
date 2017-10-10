/**
 * 
 */
package com.elastica.beatle.audit.dummy.factoryClass;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditScpTests;


/**
 * @author Mallesh
 *
 */
public class AuditDummyScpUploadFactory_limited{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SCPFactoryMethod(){
		return new Object[]{
				
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_MANDATORY_HEADERS)
				
				
		};
	}
}
