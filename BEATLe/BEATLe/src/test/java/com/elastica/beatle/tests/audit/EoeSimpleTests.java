/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.logger.Logger;

public class EoeSimpleTests extends AuditInitializeTests{
	protected Client restClient;	
	protected String FireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	
	/**
	 * @param FireWallName
	 */
	public EoeSimpleTests(String FireWallName) {
		restClient = new Client();
		this.FireWallType = FireWallName;
	}	
	
	@Test
	public void test1()
	{
		Reporter.log("test1",true);
	}
	
	@Test
	public void test2()
	{
		Reporter.log("test2",true);
	}
	
	
}
