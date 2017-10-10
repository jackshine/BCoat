package com.elastica.action.saml;

import java.io.PrintWriter;
import com.elastica.action.Action;
import com.elastica.constants.sso.SSOConstants;

public class SAMLAction extends Action{

	
	
	public void createSAMLFile(String samlResponse) {
		String fileName = SSOConstants.oneLoginMetaFile;
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println(samlResponse);
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	

}
