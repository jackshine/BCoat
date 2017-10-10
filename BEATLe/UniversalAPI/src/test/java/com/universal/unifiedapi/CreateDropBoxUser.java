//package com.universal.unifiedapi;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONObject;
//import org.testng.Reporter;
//import org.testng.annotations.Test;
//
//import com.universal.common.CommonTest;
//import com.universal.util.Utility;
//
//public class CreateDropBoxUser extends CommonTest {
//	PrintWriter writer;
//	String filepath;
//	String logpath;
//	PrintWriter writer2;
//	public CreateDropBoxUser() throws Exception {
//		super();
//		filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers.txt";
//		logpath = System.getProperty("user.dir") + "/src/test/resources/log.txt";
//		//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//		writer  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//		writer2 = new PrintWriter(new FileOutputStream(new File(logpath), true));
//	}
//	
//	
//	
//	
//	
//	@Test
//	public void getMembersList() {
//		try {
//			
//			//curl 'https://www.dropbox.com/team/admin/members/list' -H 'Accept: text/plain, */*; q=0.01' -H 'Accept-Language: en-US,en;q=0.5' -H 'Cache-Control: no-cache' -H 'Connection: keep-alive' -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' -H 'Cookie: locale=en; gvc=MTEwNDg4MDA2ODg0MjM2ODgwNzA1MzM5NzkwNTMzODk5NjA3NDI0; puc=YTc5ZTc2NzEsZmU0YjhlNjE%3D; t=DT3rfWtQQ6FV7FIPbfwu0Gqz; js_csrf=DT3rfWtQQ6FV7FIPbfwu0Gqz; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1438762367098-80851; developer_lang=js; lid=AACGbRelvOeJ8UYbdMWqUUucm1b516r0J6rPu4v69ys9gg; blid=AACaS2dGiPds_6ffBXB8FfN2xddOZaYxszKyQQ9CdYev3A; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTcyMDk5LCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MTc2OTM1NzQzODU2NzY0MjMwMTI2MzE5MDI3MTAxNjI1ODE0MTA2; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDE3NjkzNTc0Mzg1Njc2NDIzMDEyNjMxOTAyNzEwMTYyNTgxNDEwNiwgImV4cGlyZXMiOiAxNDQyNTcyMDk5LCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D' -H 'Host: www.dropbox.com' -H 'Pragma: no-cache' -H 'Referer: https://www.dropbox.com/team/admin/members' -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:40.0) Gecko/20100101 Firefox/40.0' -H 'X-Requested-With: XMLHttpRequest' -H 'el_auth_param: RU5DOv521WJcMt5C5IJuKkr%2BzQkd3FDuy94zchGFN9gg4sxg1QrKojUs8OClSP9H1jBnJkmf8Fl2oKRTqCitU96MATHz1cQSCa7BNA0QqxqdIsSZ' -H 'x-elastica_gw: v1.0.1' --data 'is_xhr=true&t=DT3rfWtQQ6FV7FIPbfwu0Gqz&join_state=1&offset=0&limit=10&sort_column=display_name_lower&sort_direction=1'
//			
//			//https://www.dropbox.com/team/admin/members/list
//			Reporter.log("Started listing the members");
//
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/members.txt";
//			PrintWriter mwriter  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			//queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/team/admin/members/list";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/plain, */*; q=0.01"));
//			//requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			//requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "locale=en; gvc=MTEwNDg4MDA2ODg0MjM2ODgwNzA1MzM5NzkwNTMzODk5NjA3NDI0; puc=YTc5ZTc2NzEsZmU0YjhlNjE%3D; t=DT3rfWtQQ6FV7FIPbfwu0Gqz; js_csrf=DT3rfWtQQ6FV7FIPbfwu0Gqz; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1438762367098-80851; developer_lang=js; lid=AACGbRelvOeJ8UYbdMWqUUucm1b516r0J6rPu4v69ys9gg; blid=AACaS2dGiPds_6ffBXB8FfN2xddOZaYxszKyQQ9CdYev3A; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTcyMDk5LCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MTc2OTM1NzQzODU2NzY0MjMwMTI2MzE5MDI3MTAxNjI1ODE0MTA2; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDE3NjkzNTc0Mzg1Njc2NDIzMDEyNjMxOTAyNzEwMTYyNTgxNDEwNiwgImV4cGlyZXMiOiAxNDQyNTcyMDk5LCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//			int offset = 0;
//			int limit  = 0;
//			for (int i=10; i<= 1000; i=i+10) {
//
//				offset = offset - 10;
//				limit  = offset;
//
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//				nameValuePairs.add(new BasicNameValuePair("join_state", "1"));
//				nameValuePairs.add(new BasicNameValuePair("t", "DT3rfWtQQ6FV7FIPbfwu0Gqz"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("offset", String.valueOf(offset)));
//				nameValuePairs.add(new BasicNameValuePair("limit", String.valueOf(limit)));
//				nameValuePairs.add(new BasicNameValuePair("sort_column", "display_name_lower"));
//				nameValuePairs.add(new BasicNameValuePair("sort_direction", "-1"));
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				mwriter.println(responseBody);
//			}
//
//			mwriter.close();
//
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//
//
//	}
//	
//	
//	
//	@Test
//	public void createUser() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzE%3D; lid=AABihb56ydF9GkyCWSEID4ZHrjxm4XcVgpv2QxoCkAQgkA; blid=AAB5TYnt-g135v5rtWmJ7sPVRONKA3d6icPNkWYIpYqNFw; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNDcwNjQzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjE0OTc1NTk5NzgzODYwNDM0MjgwMjM4NjEyMjY0NTgxNDI2Mzg1; js_csrf=Wn6LUdZk63Nfb7lLjS4n8foS; t=Wn6LUdZk63Nfb7lLjS4n8foS; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDIxNDk3NTU5OTc4Mzg2MDQzNDI4MDIzODYxMjI2NDU4MTQyNjM4NSwgImV4cGlyZXMiOiAxNDQyNDcwNjQzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=4091; i<= 9990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "Wn6LUdZk63Nfb7lLjS4n8foS"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				writer.println(responseBody);
//			}
//			
//			writer.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	@Test
//	public void activateUser() throws Exception, IOException {
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		writer2.close();
//
//	}
//	
//	//Activate users in parallel
//	@Test
//	public void activateUser1() throws Exception, IOException {
//		Thread.sleep(10000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers1.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}	
//	
//	@Test
//	public void activateUser2() throws Exception, IOException {
//		Thread.sleep(12000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers2.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}	
//
//	@Test
//	public void activateUser3() throws Exception, IOException {
//		Thread.sleep(14000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers3.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}	
//
//	@Test
//	public void activateUser4() throws Exception, IOException {
//		Thread.sleep(16000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers4.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}	
//	
//	@Test
//	public void activateUser5() throws Exception, IOException {
//		Thread.sleep(4000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers5.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}	
//	
//	@Test
//	public void activateUser6() throws Exception, IOException {
//		Thread.sleep(6000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers6.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}
//	
//	@Test
//	public void activateUser7() throws Exception, IOException {
//		Thread.sleep(8000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers7.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}
//	
//	@Test
//	public void activateUser8() throws Exception, IOException {
//		Thread.sleep(18000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers8.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}
//	
//	@Test
//	public void activateUser9() throws Exception, IOException {
//		Thread.sleep(2000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers9.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}
//	
//	@Test
//	public void activateUser10() throws Exception, IOException {
//		Thread.sleep(9000);
//		String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers10.txt";
//		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				JSONObject jsonObject = new JSONObject(line);
//				JSONObject usersObject =  jsonObject.getJSONObject("users") ;
//				Iterator itr = usersObject.keys();
//				while(itr.hasNext()) {
//					//System.out.println(itr.next());
//					JSONObject invitedUser = (JSONObject) usersObject.get((String) itr.next());
//					String email = (String) invitedUser.get("email");
//					String signUpKey = (String) invitedUser.get("signup_key");
//
//
//					String username = email.split("@")[0];
//					String firstname = username.split("\\.")[0];
//					String lastname = username.split("\\.")[1];
//					//writer2.println(email);
//					System.out.println(email);
//					System.out.println(signUpKey);
//					System.out.println("Firstname:" + firstname);
//					System.out.println("Lastname :" + lastname);
//
//					Reporter.log("Started creating the user");
//
//					//write to the file
//
//
//					ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//					queryParams.add(new BasicNameValuePair("long_running", "1"));
//
//					String host ="www.dropbox.com";
//					String uriPath = "/team/join/new_ajax";
//
//					ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//					requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//					requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//					requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//					requestHeader.add(new BasicNameValuePair("csp", "active"));
//					requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//					requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj"));
//					requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//					requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/join/new?signup_key="+signUpKey));
//					requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//					requestHeader.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
//
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//					nameValuePairs.add(new BasicNameValuePair("email", email));
//					nameValuePairs.add(new BasicNameValuePair("signup_key", signUpKey));
//					nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//					nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//					nameValuePairs.add(new BasicNameValuePair("password", "Elastica@123"));
//					nameValuePairs.add(new BasicNameValuePair("fname", firstname));
//					nameValuePairs.add(new BasicNameValuePair("lname", lastname));
//					nameValuePairs.add(new BasicNameValuePair("tos_agree", "True"));
//					nameValuePairs.add(new BasicNameValuePair("cont", ""));
//
//					UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//					URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//					HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//					String responseBody = getResponseBody(response);
//					//writer2.println("Response body:" + responseBody);
//					System.out.println("Response body:" + responseBody);
//					//writer2.println("---------------------------------");
//
//				}
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		//writer2.close();
//
//	}	
//	
//	
//	//parallel tests
//	@Test
//	public void createUser1() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers1.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter1  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=10000; i<= 10990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter1.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter1.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	@Test
//	public void createUser2() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers2.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter2  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=11000; i<= 11990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//				
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter2.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter2.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void createUser3() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers3.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter3  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=12000; i<= 12990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter3.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter3.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void createUser4() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers4.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter4  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=13000; i<= 13990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter4.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter4.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void createUser5() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers5.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter5  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=14000; i<= 14990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter5.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter5.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void createUser6() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers6.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter6  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=15000; i<= 15990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter6.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter6.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	@Test
//	public void createUser7() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers7.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter7  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=16000; i<= 16990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter7.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter7.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void createUser8() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers8.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter8  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=17000; i<= 17990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter8.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter8.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void createUser9() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers9.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter9  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=18000; i<= 18990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter9.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter9.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void createUser10() {
//		try{
//
//			Reporter.log("Started creating the user");
//			
//			//write to the file
//			String filepath = System.getProperty("user.dir") + "/src/test/resources/dropboxusers10.txt";
//			
//			//File f = new File(FilenameUtils.separatorsToSystem(filepath));
//			PrintWriter uwriter10  = new PrintWriter(new FileOutputStream(new File(filepath), true));
//			
//			ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//			queryParams.add(new BasicNameValuePair("_subject_uid", "356514040"));
//
//			String host ="www.dropbox.com";
//			String uriPath = "/account/team/add_users";
//
//			ArrayList<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
//			requestHeader.add(new BasicNameValuePair("Host", "www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("accept", "text/javascript, text/html, application/xml, text/xml"));
//			requestHeader.add(new BasicNameValuePair("accept-encoding", "gzip, deflate"));
//			requestHeader.add(new BasicNameValuePair("accept-language", "en-US,en;q=0.8"));
//			requestHeader.add(new BasicNameValuePair("csp", "active"));
//			//requestHeader.put("content-length", "193");
//			requestHeader.add(new BasicNameValuePair("content-type", "application/x-www-form-urlencoded; charset=UTF-8"));
//			requestHeader.add(new BasicNameValuePair("cookie", "gvc=MTMxNTc5ODY2MDgyMTUwNjQzODg5MTQ1OTgzMTY3MTQ1MDQxOTgy; locale=en; developer_lang=java; _mkto_trk=id:077-ZJT-858&token:_mch-dropbox.com-1439833433869-59468; puc=YTc5ZTc2NzEsZmU0YjhlNjEsMGVmMWFlYzEsMzEwMmFlYzEsMzQ0OWNlYzEsYzIxM2ZlYzE%3D; lid=AAD9CgArkwQ_tfeLbMcjC4_lv_U1KlvWun3_1pv8zsX4YQ; blid=AAARkvPXsbnMwK3EzslkjfE0zEZYNmqeoMAgJq_WBdHK6Q; jar=W3sidWlkIjogMzU2NTE0MDQwLCAiaCI6ICJBQUJ0dVNnNkg1d1d5cmZJbFlPS3VMS0FSNmEzVkNsTV9SOHp1eVFDVXQ1UGlBIiwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAibnMiOiA3NTIzMjc5OTcsICJyZW1lbWJlciI6IHRydWV9XQ%3D%3D; l=MjU0MzE5MDYwMjcxNTA5NjEzNjM4MjcwNzI5ODY4ODU4MzQ5MTY5; js_csrf=ko68HgUg0D0aKRFeZfsECZAj; t=ko68HgUg0D0aKRFeZfsECZAj; bjar=W3sidWlkIjogMzU2NTE0MDQwLCAic2Vzc19pZCI6IDI1NDMxOTA2MDI3MTUwOTYxMzYzODI3MDcyOTg2ODg1ODM0OTE2OSwgImV4cGlyZXMiOiAxNDQyNTQ2NjMzLCAidGVhbV9pZCI6IDIxODA4MiwgInJvbGUiOiAid29yayJ9XQ%3D%3D"));
//			requestHeader.add(new BasicNameValuePair("origin", "https://www.dropbox.com"));
//			requestHeader.add(new BasicNameValuePair("referer", "https://www.dropbox.com/team/admin/members?tk=house_ad&ag=teams&ad=dfb_invite_colleagues&show_invite_members"));
//			requestHeader.add(new BasicNameValuePair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36"));
//			requestHeader.add(new BasicNameValuePair("x-firephp-version", "0.0.6"));
//			
//			
//			for (int i=19000; i<= 19990; i=i+10) {
//				Reporter.log("Started creating the user from "+i+" to "+ (i+10));
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				
//				for(int j=1; j<=10; j++) {
//					int id = i+j;
//					String email = "mitthan.meena+"+id+"@elasticaqa.net";
//					nameValuePairs.add(new BasicNameValuePair("emails", email));
//				}
//				
//				nameValuePairs.add(new BasicNameValuePair("team_id", "218082"));
//				nameValuePairs.add(new BasicNameValuePair("t", "ko68HgUg0D0aKRFeZfsECZAj"));
//				nameValuePairs.add(new BasicNameValuePair("is_xhr", "true"));
//				nameValuePairs.add(new BasicNameValuePair("parent_request_id", "003408ebce3fb44c824435a117968ee5"));
//
//
//
//				UrlEncodedFormEntity urlencodedentity = new UrlEncodedFormEntity(nameValuePairs);
//				URI uri = Utility.getURI("https", host, -1, uriPath, queryParams, null);
//				HttpResponse response = executeRestRequest(POST_METHOD, uri, requestHeader, urlencodedentity, null);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response body:" + responseBody);
//				uwriter10.println(responseBody);
//				Thread.sleep(15000);
//			}
//			
//			uwriter10.close();
//			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//
//}
//
