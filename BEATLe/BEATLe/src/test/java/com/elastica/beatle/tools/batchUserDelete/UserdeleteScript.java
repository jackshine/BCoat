/**
 * 
 */
package com.elastica.beatle.tools.batchUserDelete;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.infra.InfraActions;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class UserdeleteScript extends CommonTest {

	Client restClient;
	InfraActions infraAction;
	
	@BeforeClass(alwaysRun=true)
	public void initTool(){
		restClient = new Client();
		infraAction = new InfraActions();
	}
	
	@Test
	public void deleteAllTenantUsers() throws URISyntaxException, IOException, Exception{
		String getAllUsers= "/admin/user/ng/list/0?limit=30&offset=0&order_by=first_name&query=";
		HttpResponse response = restClient.doGet(ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(),getAllUsers), getHeaders());
		
		JSONObject userListObject = new JSONObject(ClientUtil.getResponseBody(response));
		int totalUsers = userListObject.getJSONObject("meta").getInt("total_count");		
		getAllUsers = "/admin/user/ng/list/0?limit="+totalUsers+"&offset=0&order_by=first_name&query=";		
		response = restClient.doGet(ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getHost(),getAllUsers), getHeaders());
		
		userListObject = new JSONObject(ClientUtil.getResponseBody(response));
		JSONArray userArray = userListObject.getJSONArray("userslist");
		Logger.info("Totla users in array :"+userArray.length());
		for(int i=0;i<userArray.length();i++){
			JSONObject object = userArray.getJSONObject(i);
			if(!suiteData.getUsername().equals(object.getString("email")))
				infraAction.deleteUser(suiteData, getHeaders(), object.getString("id"));		
		}		
	}		
}