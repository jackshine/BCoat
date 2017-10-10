/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditWebUploadFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS),
		        new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
		        new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
		        new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2),
		        new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ),
		        new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_CHECKPOINT_CSV),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_MCAFEE_SEF),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_SQUID_PROXY),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_WSA_ACCESS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_JUNIPER_SRX)
				
				
				
		};
	}
}
