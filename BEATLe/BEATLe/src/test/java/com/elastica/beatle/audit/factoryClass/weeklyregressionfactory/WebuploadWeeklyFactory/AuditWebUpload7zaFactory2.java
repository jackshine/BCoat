/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.WebuploadWeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionWebUploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditWebUpload7zaFactory2{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_QUOTE_HEADER_7ZA),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_SPACE_HEADER_7ZA),

				
		};
	}
}
