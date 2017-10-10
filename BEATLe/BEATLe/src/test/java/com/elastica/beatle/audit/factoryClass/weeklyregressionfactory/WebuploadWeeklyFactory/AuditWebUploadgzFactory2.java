/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.WebuploadWeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionWebUploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditWebUploadgzFactory2{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_QUOTE_HEADER_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_SPACE_HEADER_GZ),

		};
	}
}
