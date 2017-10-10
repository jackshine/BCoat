/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.S3WeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionS3UploadTests;


public class AuditS3UploadzipFactory2{

	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
			
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_QUOTE_HEADER_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_SPACE_HEADER_ZIP),
			
				
		};
	}
}
