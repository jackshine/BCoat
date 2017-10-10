package com.elastica.constants.sso;

import java.io.File;


/**
 * Box Constants
 * @author Eldo Rajan
 *
 */
public class SSOConstants  {

	public static String metaDirectory=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"meta";
	public static String oneLoginMetaFile=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"meta"+File.separator+"one_login_meta.xml";
	public static String pingOneLoginMetaFile=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"meta"+File.separator+"saml2-metadata-idp.xml";
	public static String oneLoginMetaFileName="one_login_meta.xml";
	public static String pingOneLoginMetaFileName="saml2-metadata-idp.xml";
	
	public static String azureAdMetaFile=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"meta"+File.separator+"azureadmetadata.xml";
	
	
	
}