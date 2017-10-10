/**
 * 
 */
package com.elastica.beatle.audit.dummy.factoryClass;
import org.testng.annotations.Factory;
import com.elastica.beatle.audit.AuditTestConstants;


/**
 * @author Mallesh
 *
 */
public class AuditDummyScpUploadFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SCPFactoryMethod(){
		return new Object[]{
				
					
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_CHECKPOINT_CSV),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_MCAFEE_SEF),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_SQUID_PROXY),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_WSA_ACCESS),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_JUNIPER_SRX),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_SCANSAFE),
				new AuditScpTests_2(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES)
				
				
		};
	}
}
