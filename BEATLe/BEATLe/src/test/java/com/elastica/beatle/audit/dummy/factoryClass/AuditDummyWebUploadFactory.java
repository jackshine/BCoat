/**
 * 
 */
package com.elastica.beatle.audit.dummy.factoryClass;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;


public class AuditDummyWebUploadFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
				
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_CHECKPOINT_CSV),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_MCAFEE_SEF),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_SQUID_PROXY),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_WSA_ACCESS),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_JUNIPER_SRX),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_SCANSAFE),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES)
				
				
		};
	}
}
