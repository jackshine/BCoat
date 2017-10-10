package com.elastica.beatle.tests.infra;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.common.collect.Ordering;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.es.ActivityLogs.Login;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
public class SecurletEndtoEndScenario extends CommonTest{
	private final static CloseableHttpClient client = HttpClientBuilder.create().build();
	List<NameValuePair> headers;
	String tenantDB;
	 @BeforeClass(alwaysRun= true)
	public void initTests(ITestContext suiteConfigurations) throws Exception {		
		headers =getHeaders();

}
	 /*Author: Vijay Gangwar
		*todo: delete user sysadmin 
		*params none
		*/
		@Test ( priority = 1)
		public void ActivateBoxSecurletE2Etescase()
		{
			//1. chekc if securlet is subcribed if not subscribe it
			//2. chekc if box is subscribed if not request subscription
			//3. chekc if enable box and request grant access from bop
			//4. activate box 
			//5. chekc for the user in soc after activation
			
			
		}
		
}

