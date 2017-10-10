/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.S3WeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionS3UploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditS3Uploadb2zFactory2{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_QUOTE_HEADER_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_SPACE_HEADER_BZ2),

				
		};
	}
}