/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;
import com.elastica.beatle.tests.audit.EoeComplexTests;


public class EOEComplexTestfactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
			//	new AuditPreferenceTests(),		
				new EoeComplexTests(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV)
		};
	}
}
