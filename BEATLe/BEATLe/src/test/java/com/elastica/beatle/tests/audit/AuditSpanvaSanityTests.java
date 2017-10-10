/**
 * 
 */
package com.elastica.beatle.tests.audit;

import org.apache.http.HttpResponse;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;

/**
 * @author Mallesh
 *
 */
public class AuditSpanvaSanityTests extends AuditInitializeTests{
	
	protected Client restClient;

	@BeforeClass(alwaysRun = true)
	public void init() throws Exception{
		restClient = new Client();
	}
	
	
	@Test
	public void testSpanvaImageDownload() throws Exception
	{
		  Reporter.log("Spanva Image Download  test started****************",true);
	     
	      HttpResponse spanvaImageDownloadResp = AuditFunctions.getSpanvaUrl(restClient);
		  Assert.assertEquals(spanvaImageDownloadResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			
		  String strSpanvaImageDownloadResp=ClientUtil.getResponseBody(spanvaImageDownloadResp);
		  Reporter.log("spanva Image Download Resp::"+strSpanvaImageDownloadResp,true);
		  JSONObject jsonSpanvaImageDownloadResp = new JSONObject(strSpanvaImageDownloadResp);
		  Assert.assertNotNull(jsonSpanvaImageDownloadResp.get("status"), "spanva_image_url download status is null ");
		  Assert.assertEquals("success", jsonSpanvaImageDownloadResp.get("status"),"Spanva Download Image Url status should be [success], But found ["+jsonSpanvaImageDownloadResp.get("status")+"]");
		  Assert.assertNotNull(jsonSpanvaImageDownloadResp.get("image_sha1"), "image_sha1 is null ");
		  Assert.assertNotNull(jsonSpanvaImageDownloadResp.get("image_sha512"), "image_sha512 is null ");
		  Assert.assertNotNull(jsonSpanvaImageDownloadResp.get("image_md5"), "image_md5 is null ");
		  Assert.assertNotNull(jsonSpanvaImageDownloadResp.get("download_url"), "download_url is null ");
		  
		  Reporter.log("Spanva Image Download  test ended****************",true);
			
		
	}


}