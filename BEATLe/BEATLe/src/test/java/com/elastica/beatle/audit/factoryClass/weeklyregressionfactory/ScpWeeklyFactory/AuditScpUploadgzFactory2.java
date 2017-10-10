/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.ScpWeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionScpUploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditScpUploadgzFactory2{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SCPUploadFactoryMethod(){
		return new Object[]{

				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_QUOTE_HEADER_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_SPACE_HEADER_GZ),

				
		};
	}
}
