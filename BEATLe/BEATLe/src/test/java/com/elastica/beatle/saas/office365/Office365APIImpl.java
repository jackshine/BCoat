package com.elastica.beatle.saas.office365;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;

public class Office365APIImpl {

	Client restClient = new Client();

	/**
	 * Utility method to get Bearer Access Token
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getAccessToken() throws Exception {

		String body = "client_id="
				+ URLEncoder.encode("48848214-762c-468e-8981-80c633502ed5", "UTF-8")
				+ "&"
				+ "client_secret="
				+ URLEncoder.encode("42M8gqi5PzbxiIy4zsxx6Wsf5hY72YqSZ2XHSqB9Rmw=", "UTF-8")
				+ "&"
				+ "refresh_token="
				+ URLEncoder
						.encode("AAABAAAAiL9Kn2Z27UubvWFPbm0gLYkieudnTaY6YOyu0tBRF0TvItQ_0cqnkyadfmbb7yYtTyCx2Ea51YtRbojTOR9OO2e2SNhKDmHaJOY336Yagf4HNdbqk4fEffDWz7eJq1Z88GVSQLvPmn_19NgsUpvhewrWcyGZniqUohbwgZ5aPIlLdqF_aWTwezLZ3H3uyhTECpmEnBJOYPdrQ1kTHAm4X7kgxWfLjAMf2jYOcpL93DuA57PyxkIp7u277G4-SMA7h1r8J3TDNStQx6uIIN14XLAxWE6PZUKWfp1uixg_bgvauJM4lzQXNQFXDBt_U0uryWpR1nsfqGHPKHewl-v2Ex-MOA42-sas8NzkMHFwvIhaqc5j4VPMRkQHdb9Pv4rmEZoh5CZmQtmowRljdjog5ynxjL5nZcUP8kSk1aN4GG62MNgkoRoIe_rdFjywLie2p6-Z4b_eDOjGg6squckTnmhGtV10c8BRwmYL6CkL7XAavOyV3GMPK2a1M-t6hM5FJPiU1QB4iHMSVuduN8lezW_sQMPgM3lLd1ZDhxsl3RMhTJvI2P1G17HcVq4C-yCjq8blr2GWaSJcvt4yVzblQiAA",
								"UTF-8") + "&" + "grant_type=" + URLEncoder.encode("refresh_token", "UTF-8") + "&" + "resource="
				+ URLEncoder.encode("https://o365securitynet-my.sharepoint.com/", "UTF-8") + "&" + "redirect_uri="
				+ URLEncoder.encode("https://eoe.elastica-inc.com/static/ng/appDashboards/index.html#/", "UTF-8");

		String uri = "https://login.windows.net/common/oauth2/token/";
		
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		requestHeader.add(new BasicNameValuePair("Charset", "UTF-8"));
		StringEntity entity = new StringEntity(body);
		HttpResponse response = restClient.doPost(ClientUtil.BuidURI(uri), requestHeader, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		// Create a new JSONObject to hold the access token and extract
		// the token from the response.
		org.json.JSONObject parsedObject = new org.json.JSONObject(responseBody);
		String accessToken = parsedObject.getString("access_token");
		System.out.println("Access Token printing....." + accessToken);

		return accessToken;

	}	

	/**
	 * Utility method to upload file in OneDrive
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public JSONObject uploadFile(String fileName) throws Exception {
		JSONObject parsedObject = null;
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();        
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			File oneDriveTestfile = new File(System.getProperty("user.dir") + "//src//test//resources//protect//" + "BE.txt");
			String uri = "https://o365securitynet-my.sharepoint.com/_api/v1.0/me/files/01CREHNAN6Y2GOVW7725BZO354PWSELRRZ/children/"
					+ fileName + "" + ("/content");
			builder.addPart("my_file", new FileBody(oneDriveTestfile)); 
			HttpEntity putBodyEntity = builder.build();
			List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
			requestHeader.add(new BasicNameValuePair("Content-Type", "*/*"));
			requestHeader.add(new BasicNameValuePair("Authorization", "Bearer " + getAccessToken()));
			HttpResponse response = restClient.doPut(ClientUtil.BuidURI(uri), requestHeader, null, putBodyEntity);
			String responseBody = ClientUtil.getResponseBody(response);
			parsedObject = new JSONObject(responseBody);
			System.out.println("File Creation...Response Body printing...." + responseBody.toString());

		} catch (Exception e) {
			System.out.println("Exception Printing....." + e.getMessage());
		}
		return parsedObject;
	}


	/**
	 * Utility method to delete file from OneDrive
	 * 
	 * @param id
	 * @param eTag
	 * @throws Exception
	 */
	public void deleteFile(String id, String eTag) throws Exception {

		try {

			String uri = "https://o365securitynet-my.sharepoint.com/_api/v1.0/me/files/" + id;
			List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
			requestHeader.add(new BasicNameValuePair("Content-Type", "*/*"));
			requestHeader.add(new BasicNameValuePair("Authorization", "Bearer " + getAccessToken()));
			requestHeader.add(new BasicNameValuePair("If-Match", eTag));
			HttpResponse response = restClient.doDelete(ClientUtil.BuidURI(uri), requestHeader);
			String responseBody = ClientUtil.getResponseBody(response);
			System.out.println("File Creation...Response Body printing...." + responseBody.toString());

		} catch (Exception e) {
			System.out.println("Exception Printing....." + e.getMessage());
		}

	}

	/**
	 * Utility method to share file from OneDrive
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void shareFile(String fileName) throws Exception {
		try {
			String uri = "https://o365securitynet-my.sharepoint.com/personal/qa-admin_o365security_net/_api/web/GetFileByServerRelativeUrl('/personal/qa-admin_o365security_net/Documents/"
					+ fileName + "')/ListItemAllFields/roleassignments/addroleassignment(principalid=4,roledefid=1073741827)";
			
			List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
			requestHeader.add(new BasicNameValuePair("Content-Type", "application/json"));
			requestHeader.add(new BasicNameValuePair("Authorization", "Bearer " + getAccessToken()));
			requestHeader.add(new BasicNameValuePair("Accept", "application/json; odata=verbose"));			
			HttpResponse response = restClient.doPost(ClientUtil.BuidURI(uri), requestHeader, null, null);
			String responseBody = ClientUtil.getResponseBody(response);
			System.out.println("Sharing Response Body Printing....."+responseBody);
			
			System.out.println("File Sharing....Response Status Printing....." + response.getStatusLine());

		} catch (Exception e) {
			System.out.println("Exception Printing....." + e.getMessage());
		}

	}
	
	/**
	 * Utility method to share file from OneDrive
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void unShareFile(String fileName) throws Exception {
		try {
			String uri = "https://o365securitynet-my.sharepoint.com/personal/qa-admin_o365security_net/_api/web/GetFileByServerRelativeUrl('/personal/qa-admin_o365security_net/Documents/"
					+ fileName + "')/ListItemAllFields/roleassignments/removeroleassignment(principalid=4,roledefid=1073741827)";
			
			List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
			requestHeader.add(new BasicNameValuePair("Content-Type", "application/json"));
			requestHeader.add(new BasicNameValuePair("Authorization", "Bearer " + getAccessToken()));
			requestHeader.add(new BasicNameValuePair("Accept", "application/json; odata=verbose"));			
			HttpResponse response = restClient.doPost(ClientUtil.BuidURI(uri), requestHeader, null, null);
			String responseBody = ClientUtil.getResponseBody(response);
			System.out.println("Sharing Response Body Printing....."+responseBody);
			System.out.println("File Sharing....Response Status Printing....." + response.getStatusLine());

		} catch (Exception e) {
			System.out.println("Exception Printing....." + e.getMessage());
		}

	}


}