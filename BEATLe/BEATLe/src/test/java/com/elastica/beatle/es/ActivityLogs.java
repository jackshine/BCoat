package com.elastica.beatle.es;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Reporter;

import com.elastica.beatle.InitializeTests;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.detect.DetectInitializeTests;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.InputBean;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

public class ActivityLogs {


	
	public void produceActivityLogs(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, InputBean inputBean, TestSuiteDTO suiteData) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		try {
			Set<String> saasApps = new HashSet<>();
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	count++;
		    	Calendar cal = Calendar.getInstance();
		    	cal.add(Calendar.SECOND, idx);
		    	String strDateTime = formatDate.format(cal.getTime());
		    	String strYear = formatYear.format(cal.getTime());
		    	
		    	String sourceId = RandomStringUtils.randomAlphanumeric(22);
		    	
		    	
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        ((ObjectNode)_source).put("test_id", inputBean.getTestId());
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        ((ObjectNode)_source).put("inserted_timestamp", strDateTime);
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void produceActivityLogs(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, String testId, String strDateStart, String strDateStop) {
		
		String tmplFilePath = "src/test/resources/detect/golden_input_tmpl/";
		String resLogFilePath = "src/test/resources/detect/golden_input/";
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
		try {
			Date dateStart = formatDate.parse(strDateStart);
			Date dateStop = formatDate.parse(strDateStop);
			
			System.out.println("Org. Start Date: " + formatDate.format(dateStart));
			System.out.println("Org. Stop Date: " + formatDate.format(dateStop));
			
			//in milliseconds
			long diff = dateStop.getTime() - dateStart.getTime();
 
			long diffSeconds = diff / 1000;
			long diffMinutes = diff / (60 * 1000);
			long diffHours = diff / (60 * 60 * 1000);
			long diffDays = diff / (24 * 60 * 60 * 1000);
 
			System.out.println(diff + " MilliSeconds");
			
			System.out.println(diffDays + " days, ");
			System.out.println(diffHours + " hours, ");
			System.out.println(diffMinutes + " minutes, ");
			System.out.println(diffSeconds + " seconds.");
			
			Calendar cal = Calendar.getInstance();
			Date newStopDate = cal.getTime();
			String strYear = formatYear.format(cal.getTime());
			cal.add(Calendar.SECOND, ((int) diffSeconds * -1));
			Date newStartDate = cal.getTime();
			
			System.out.println("New Start Date: " + formatDate.format(newStartDate));
			System.out.println("New Stop Date: " + formatDate.format(newStopDate));
			
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			
		    for(String line; (line = br.readLine()) != null;) {
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        ((ObjectNode)_source).put("test_id", testId);
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        
		        String created_timestamp = _source.path("created_timestamp").asText();
		        Date dateCreatedTimestamp = formatDate.parse(created_timestamp);
		        long locDiff = dateCreatedTimestamp.getTime() - dateStart.getTime();
		        long locDiffSec = locDiff / 1000;
		        cal.setTime(newStartDate);
				cal.add(Calendar.SECOND, (int) locDiffSec);
				Date newCreatedTimestamp = cal.getTime();
				String strDateTime = formatDate.format(newCreatedTimestamp);
				
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        
		        Calendar insCal = Calendar.getInstance();
				String insTimeStamp = formatDate.format(insCal.getTime());
				((ObjectNode)_source).put("inserted_timestamp", insTimeStamp);
		        
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
//		        System.out.println(activity.toString());
		        bw.write(activity.toString() + "\n");
		    }
		    
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void produceActivityLogs(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, String testId, String strDateStart, String strDateStop, int count) {
		
		String tmplFilePath = "src/test/resources/detect/golden_input_tmpl/";
		String resLogFilePath = "src/test/resources/detect/golden_input/";
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
		try {
			Date dateStart = formatDate.parse(strDateStart);
			Date dateStop = formatDate.parse(strDateStop);
			
			System.out.println("Org. Start Date: " + formatDate.format(dateStart));
			System.out.println("Org. Stop Date: " + formatDate.format(dateStop));
			
			//in milliseconds
			long diff = dateStop.getTime() - dateStart.getTime();
 
			long diffSeconds = diff / 1000;
			long diffMinutes = diff / (60 * 1000);
			long diffHours = diff / (60 * 60 * 1000);
			long diffDays = diff / (24 * 60 * 60 * 1000);
 
			System.out.println(diff + " MilliSeconds");
			
			System.out.println(diffDays + " days, ");
			System.out.println(diffHours + " hours, ");
			System.out.println(diffMinutes + " minutes, ");
			System.out.println(diffSeconds + " seconds.");
			
			Calendar cal = Calendar.getInstance();
			Date newStopDate = cal.getTime();
			String strYear = formatYear.format(cal.getTime());
			cal.add(Calendar.SECOND, ((int) diffSeconds * -1));
			Date newStartDate = cal.getTime();
			
			System.out.println("New Start Date: " + formatDate.format(newStartDate));
			System.out.println("New Stop Date: " + formatDate.format(newStopDate));
			
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			
			int idx = count;
		    for(String line; (line = br.readLine()) != null; idx--) {
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        ((ObjectNode)_source).put("test_id", testId);
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        
		        String created_timestamp = _source.path("created_timestamp").asText();
		        Date dateCreatedTimestamp = formatDate.parse(created_timestamp);
		        long locDiff = dateCreatedTimestamp.getTime() - dateStart.getTime();
		        long locDiffSec = locDiff / 1000;
		        cal.setTime(newStartDate);
				cal.add(Calendar.SECOND, (int) locDiffSec);
				Date newCreatedTimestamp = cal.getTime();
				String strDateTime = formatDate.format(newCreatedTimestamp);
				
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        
		        Calendar insCal = Calendar.getInstance();
		        insCal.add(Calendar.SECOND, (idx * -1));
				String insTimeStamp = formatDate.format(insCal.getTime());
				((ObjectNode)_source).put("inserted_timestamp", insTimeStamp);
		        
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
//		        System.out.println(activity.toString());
		        bw.write(activity.toString() + "\n");
		    }
		    
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void produceActivityLogsForLargeBBI(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, String testId, InputBean inputBean, TestSuiteDTO suiteData) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		Reporter.log("");
		Reporter.log("==============================================");
		Reporter.log("Injected file path::::::: http://jenkins.elastica.co:8080/view/BEATLe%20EOE/job/Detect%20BBI%20BE%20Regression%20EoE/ws/BEATLe/"+resLogFilePath+resLogFileName, true);
		Reporter.log("==============================================");
		Reporter.log("");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
		try {
			Set<String> saasApps = new HashSet<>();
			
			
			//in milliseconds
 
			long diffSeconds = 1965;
 
			System.out.println(diffSeconds + " seconds.");
			
			Calendar cal = Calendar.getInstance();
			Date newStopDate = cal.getTime();
			String strYear = formatYear.format(cal.getTime());
			cal.add(Calendar.SECOND, ((int) diffSeconds * -1));
			Date newStartDate = cal.getTime();
			
			System.out.println("New Start Date: " + formatDate.format(newStartDate));
			System.out.println("New Stop Date: " + formatDate.format(newStopDate));
			
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			int idx = 1;
			int addSeconds= 10;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	count++;
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        ((ObjectNode)_source).put("test_id", testId);
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        String sourceId = RandomStringUtils.randomAlphanumeric(22);
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }else{
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        
		        if(11>=idx){
		        	cal.add(Calendar.SECOND, 45);
		        	String strDateTime = formatDate.format(cal.getTime());
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }else if(idx>11&&22>=idx){
		        	cal.add(Calendar.SECOND, 60);
		        	String strDateTime = formatDate.format(cal.getTime());
			        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }else if(idx>22&&31>=idx){
		        	cal.add(Calendar.SECOND, 90);
		        	String strDateTime = formatDate.format(cal.getTime());
			        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }else if(idx>31){
		        	Calendar insCal = Calendar.getInstance();
		        	addSeconds =addSeconds+10;
		        	insCal.add(Calendar.SECOND, addSeconds);
		        	
		        	String strDateTime = formatDate.format(insCal.getTime());
			        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }
		        
		        Calendar insCal = Calendar.getInstance();
		        insCal.add(Calendar.SECOND, addSeconds);
		      
				String insTimeStamp = formatDate.format(insCal.getTime());
				((ObjectNode)_source).put("inserted_timestamp", insTimeStamp);
		        
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
//		        System.out.println(activity.toString());
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void produceActivityLogsLargeBBITest1(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, InputBean inputBean, AttributeBean attributeBean, TestSuiteDTO suiteData) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		Reporter.log("");
		Reporter.log("==============================================");
		Reporter.log("Injected file path::::::: http://jenkins.elastica.co:8080/view/BEATLe%20EOE/job/Detect%20BBI%20BE%20Regression%20EoE/ws/BEATLe/"+resLogFilePath+resLogFileName, true);
		Reporter.log("==============================================");
		Reporter.log("");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
		try {
			Set<String> saasApps = new HashSet<>();
			
			List<Integer> dateDiffArray1 =new ArrayList<Integer>();
			
			BufferedReader reader = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			int lines = 0;
			while (reader.readLine() != null) 
			{
				lines++;
			}
			reader.close();
			Random rand = new Random();
			if (attributeBean.getConfidence()<=40) {
				
				for (int i = 1; i <= 11; i++) {
					int high =48;
					int low=42;
					int randomNum = rand.nextInt((high - low) + 1) + low;
					dateDiffArray1.add(randomNum);
				}
				for (int i = 12; i <= 22; i++) {
					int high =50;
					int low=40;
					int randomNum = rand.nextInt((high - low) + 1) + low;
					dateDiffArray1.add(randomNum);
				}
				for (int i = 23; i <= lines; i++) {
					int high =52;
					int low=42;
					int randomNum = rand.nextInt((high - low) + 1) + low;
					dateDiffArray1.add(randomNum);
				}
				
			} else {
				
				for (int i = 1; i <= 11; i++) {
					int high =120;
					int low=30;
					int randomNum = rand.nextInt((high - low) + 1) + low;
					dateDiffArray1.add(randomNum);
				}
				for (int i = 12; i <= 22; i++) {
					int high =200;
					int low=60;
					int randomNum = rand.nextInt((high - low) + 1) + low;
					dateDiffArray1.add(randomNum);
				}
				for (int i = 23; i <= lines; i++) {
					int high =300;
					int low=120;
					int randomNum = rand.nextInt((high - low) + 1) + low;
					dateDiffArray1.add(randomNum);
				}
			}
			
			
		
			
			int total = 0 ;
			for (int i = 0; i < dateDiffArray1.size(); i++) {
				
				total = total+dateDiffArray1.get(i);
				
			}
			Integer[] diffArray = dateDiffArray1.toArray(new Integer[dateDiffArray1.size()]);
			Reporter.log("total:::::  "+total+"  ::::::::::diffArray::::::  "+dateDiffArray1.toString(), true);	
 
			
			Calendar cal = Calendar.getInstance();
			Date newStopDate = cal.getTime();
			String strYear = formatYear.format(cal.getTime());
			cal.add(Calendar.SECOND, ((int) total * -1));
			Date newStartDate = cal.getTime();
			
			System.out.println("New Start Date: " + formatDate.format(newStartDate));
			System.out.println("New Stop Date: " + formatDate.format(newStopDate));
			
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	count++;
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        ((ObjectNode)_source).put("test_id", inputBean.getTestId());
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        String sourceId = RandomStringUtils.randomAlphanumeric(22);
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }else{
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        
		        if(idx<=diffArray.length){
		        	cal.add(Calendar.SECOND, diffArray[idx-1]);
		        	String strDateTime = formatDate.format(cal.getTime());
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }else{
		        	Calendar insCal = Calendar.getInstance();
			        insCal.add(Calendar.SECOND, idx-diffArray.length);
					String strDateTime = formatDate.format(insCal.getTime());
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }
		        
		        Calendar insCal = Calendar.getInstance();
		        insCal.add(Calendar.SECOND, idx);
				String insTimeStamp = formatDate.format(insCal.getTime());
				((ObjectNode)_source).put("inserted_timestamp", insTimeStamp);
		        
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
//		        System.out.println(activity.toString());
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void produceActivityLogsLargeBBITest2(String tmplFileName, String resLogFileName,
			String tenant, String tcId, String user, String userName, InputBean inputBean, TestSuiteDTO suiteData) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		Reporter.log("");
		Reporter.log("==============================================");
		Reporter.log("Injected file path::::::: http://jenkins.elastica.co:8080/view/BEATLe%20EOE/job/Detect%20BBI%20BE%20Regression%20EoE/ws/BEATLe/"+resLogFilePath+resLogFileName, true);
		Reporter.log("==============================================");
		Reporter.log("");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
		try {
			Set<String> saasApps = new HashSet<>();
						
			Calendar cal = Calendar.getInstance();
			Date newStopDate = cal.getTime();
			String strYear = formatYear.format(cal.getTime());
			Date newStartDate = cal.getTime();
			
			System.out.println("New Start Date: " + formatDate.format(newStartDate));
			System.out.println("New Stop Date: " + formatDate.format(newStopDate));
			
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	count++;
		    	if (count%1000 == 0) {
		    		//create new user and username.
		    		String randomString = RandomStringUtils.randomAlphanumeric(12);
		    		userName = "detect_" + randomString + "_" + tcId;
		    		user = userName + "@" + suiteData.getTenantDomainName();
		    		String testId = randomString+ "_" +tcId;
		    		inputBean.setTestId(testId);
		    	}
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        ((ObjectNode)_source).put("test_id", inputBean.getTestId());
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        String sourceId = RandomStringUtils.randomAlphanumeric(22);
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }else{
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        
		        String strDateTime = formatDate.format(newStartDate);
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        
				String insTimeStamp = formatDate.format(newStopDate);
				((ObjectNode)_source).put("inserted_timestamp", insTimeStamp);
		        
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
//		        System.out.println(activity.toString());
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void produceActivityLogsfrequentBBI(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, InputBean inputBean, AttributeBean attributeBean, TestSuiteDTO suiteData) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		Reporter.log("");
		Reporter.log("==============================================");
		Reporter.log("Injected file path::::::: http://jenkins.elastica.co:8080/view/BEATLe%20EOE/job/Detect%20BBI%20BE%20Regression%20EoE/ws/BEATLe/"+resLogFilePath+resLogFileName, true);
		Reporter.log("==============================================");
		Reporter.log("");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
		try {
			Set<String> saasApps = new HashSet<>();
			String dateDiff ="53,73,68,79,54,90,55,52,49,51,49,50,76,73,72,71,72,70,74,79,71,72,70,72,93,104,91,91,155,155";
			if(attributeBean.getConfidence()<=40){
			 dateDiff = "53,73,68,79,54,90,55,52,49,51,49,50,76,73,72,71,72,70,74,79,71,72,70,72,93,104,91,91,155,155";
			}else{
				//dateDiff = "53,73,68,79,54,90,55,52,49,51,49,50,76,73,72,71,72,70,74,79,71,72,70,72,93,104,91,91,155,155";
				dateDiff = "120, 100, 200, 120, 400, 120, 120, 90, 120, 120, 1200, 900, 300, 300, 450, 300, 300, 300, 300, 640, 300, 300, 1200, 600, 600, 600, 600, 600, 600, 600,"
					+ "1500,900,200,900,900,900,900,1000,900,900,900,900,200,900,900,900,900,1000,900,900,900,900,900,900,900,1000,900,900,900"	;
				
			//	dateDiff = "70,73,68,79,54,90,55,52,49,51,49,120,76,73,72,71,72,70,74,79,71,120,123,125,129,140,140,145,145,145,146,120, 120";
			}
			
			
			
//			String dateDiff = "120, 100, 200, 120, 400, 120, 120, 600, 120, 120, 1200, 900, 300, 300, 450, 300, 300, 300, 300, 640, 300, 300, 1200, 600, 600, 600, 600, 600, 600, 600,"
//					+ "1500,900,200,900,900,900,900,1000,900,900,900";
			
			String[] dateDiffArray =	dateDiff.split(",");
			int total = 0 ;
			Integer[] diffArray = new Integer[dateDiffArray.length];
			for (int i = 0; i < dateDiffArray.length; i++) {
				int diff = Integer.parseInt(dateDiffArray[i].trim());
				diffArray[i] = diff;
				total = total+diff;
				
			}
			System.out.println("total:::::  "+total+"  ::::::::::diffArray::::::  "+Arrays.toString(diffArray));	
 
			
			Calendar cal = Calendar.getInstance();
			Date newStopDate = cal.getTime();
			String strYear = formatYear.format(cal.getTime());
			cal.add(Calendar.SECOND, ((int) total * -1));
			Date newStartDate = cal.getTime();
			
			System.out.println("New Start Date: " + formatDate.format(newStartDate));
			System.out.println("New Stop Date: " + formatDate.format(newStopDate));
			
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	count++;
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        ((ObjectNode)_source).put("test_id", inputBean.getTestId());
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        String sourceId = RandomStringUtils.randomAlphanumeric(22);
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }else{
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        
		        if(idx<=diffArray.length){
		        	cal.add(Calendar.SECOND, diffArray[idx-1]);
		        	String strDateTime = formatDate.format(cal.getTime());
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }else{
		        	Calendar insCal = Calendar.getInstance();
			        insCal.add(Calendar.SECOND, idx-diffArray.length);
					String strDateTime = formatDate.format(insCal.getTime());
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }
		        
		        Calendar insCal = Calendar.getInstance();
		        insCal.add(Calendar.SECOND, idx);
				String insTimeStamp = formatDate.format(insCal.getTime());
				((ObjectNode)_source).put("inserted_timestamp", insTimeStamp);
		        
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
//		        System.out.println(activity.toString());
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void produceActivityLogsforEvolvingProfile(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, InputBean inputBean, AttributeBean attributeBean, TestSuiteDTO suiteData, String dateDiff) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		Reporter.log("");
		Reporter.log("==============================================");
		Reporter.log("Injected file path::::::: http://jenkins.elastica.co:8080/view/BEATLe%20EOE/job/Detect%20BBI%20BE%20Regression%20EoE/ws/BEATLe/"+resLogFilePath+resLogFileName, true);
		Reporter.log("==============================================");
		Reporter.log("");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
		try {
			Set<String> saasApps = new HashSet<>();
			
			String[] dateDiffArray =	dateDiff.split(",");
			int total = 0 ;
			Integer[] diffArray = new Integer[dateDiffArray.length];
			for (int i = 0; i < dateDiffArray.length; i++) {
				int diff = Integer.parseInt(dateDiffArray[i].trim());
				diffArray[i] = diff;
				total = total+diff;
				
			}
			System.out.println("total:::::  "+total+"  ::::::::::diffArray::::::  "+Arrays.toString(diffArray));	
 
			
			Calendar cal = Calendar.getInstance();
			Date newStopDate = cal.getTime();
			String strYear = formatYear.format(cal.getTime());
			cal.add(Calendar.SECOND, ((int) total * -1));
			Date newStartDate = cal.getTime();
			
			System.out.println("New Start Date: " + formatDate.format(newStartDate));
			System.out.println("New Stop Date: " + formatDate.format(newStopDate));
			
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =inputBean.getCount();
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	count++;
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        ((ObjectNode)_source).put("test_id", inputBean.getTestId());
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        String sourceId = RandomStringUtils.randomAlphanumeric(22);
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }else{
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        
		        if(idx<=diffArray.length){
		        	cal.add(Calendar.SECOND, diffArray[idx-1]);
		        	String strDateTime = formatDate.format(cal.getTime());
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }else{
		        	Calendar insCal = Calendar.getInstance();
			        insCal.add(Calendar.SECOND, idx-diffArray.length);
					String strDateTime = formatDate.format(insCal.getTime());
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        }
		        
		        Calendar insCal = Calendar.getInstance();
		        insCal.add(Calendar.SECOND, idx);
				String insTimeStamp = formatDate.format(insCal.getTime());
				((ObjectNode)_source).put("inserted_timestamp", insTimeStamp);
		        
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
//		        System.out.println(activity.toString());
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void produceActivityLogs(String tmplFileName, String resLogFileName,
			String tenant,  InputBean inputBean,AttributeBean attributeBean, TestSuiteDTO suiteData) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		Reporter.log("");
		Reporter.log("==============================================");
		//Reporter.log("Injected file path::::::: http://jenkins.elastica.co:8080/view/BEATLe%20EOE/job/Detect%20TBI%20BE%20Regression%20EoE/ws/BEATLe/"+resLogFilePath+resLogFileName, true);
		Reporter.log("==============================================");
		Reporter.log("");
		String user = inputBean.getUser();
		String userName = inputBean.getUserName();
		String testId = inputBean.getTestId();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		try {
			Set<String> saasApps = new HashSet<>();
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =inputBean.getCount();
			
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	count++;
		    	Calendar cal = Calendar.getInstance();
		    	cal.add(Calendar.SECOND, idx);
		    	String strDateTime = formatDate.format(cal.getTime());
		    	String strYear = formatYear.format(cal.getTime());
		    	
		    	String sourceId = RandomStringUtils.randomAlphanumeric(22);
		    	
		    	
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        ((ObjectNode)_source).put("test_id", testId);
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        ((ObjectNode)_source).put("inserted_timestamp", strDateTime);
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void produceActivityLogs(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, String testId, InputBean inputBean,AttributeBean attributeBean, TestSuiteDTO suiteData) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		Reporter.log("");
		Reporter.log("==============================================");
		Reporter.log("Injected file path::::::: http://jenkins.elastica.co:8080/view/BEATLe%20EOE/job/Detect%20TBI%20BE%20Regression%20EoE/ws/BEATLe/"+resLogFilePath+resLogFileName, true);
		Reporter.log("==============================================");
		Reporter.log("");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		try {
			Set<String> saasApps = new HashSet<>();
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	if(attributeBean.getThreshold()+2==idx){
		    		break;
		    	}
		    	count++;
		    	Calendar cal = Calendar.getInstance();
		    	cal.add(Calendar.SECOND, idx);
		    	String strDateTime = formatDate.format(cal.getTime());
		    	String strYear = formatYear.format(cal.getTime());
		    	
		    	String sourceId = RandomStringUtils.randomAlphanumeric(22);
		    	
		    	
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        ((ObjectNode)_source).put("test_id", testId);
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        ((ObjectNode)_source).put("inserted_timestamp", strDateTime);
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void produceActivityLogs(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, String testId, InputBean inputBean, TestSuiteDTO suiteData) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			Set<String> saasApps = new HashSet<>();
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	count++;
		    	Calendar cal = Calendar.getInstance();
		    	cal.add(Calendar.SECOND, idx);
		    	String strDateTime = formatDate.format(cal.getTime());
		    	String strYear = formatYear.format(cal.getTime());
		    	
		    	String sourceId = RandomStringUtils.randomAlphanumeric(22);
		    	
		    	
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        if (user!=null) {
		        	((ObjectNode)_source).put("user", user);
		        }
		        if (user!=null && _source.has("user_name")) {
		        	((ObjectNode)_source).put("user_name", userName);
		        }
		       
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        ((ObjectNode)_source).put("test_id", testId);
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        ((ObjectNode)_source).put("inserted_timestamp", strDateTime);
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void produceActivityLogsforSD1(String tmplFileName, String resLogFileName,
			String tenant, String user, String userName, String testId, InputBean inputBean, TestSuiteDTO suiteData) {
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + resLogFileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			Set<String> saasApps = new HashSet<>();
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		    	count++;
		    	Calendar cal = Calendar.getInstance();
		    	cal.add(Calendar.SECOND, idx);
		    	String strDateTime = formatDate.format(cal.getTime());
		    	String strYear = formatYear.format(cal.getTime());
		    	
		    	String sourceId = RandomStringUtils.randomAlphanumeric(22);
		    	
		    	
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		       
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        ((ObjectNode)_source).put("test_id", testId);
		        if (_source.has("elastica_user")) {
		        	((ObjectNode)_source).put("elastica_user", user);
		        }
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        ((ObjectNode)_source).put("inserted_timestamp", strDateTime);
		        ((ObjectNode)activity).put("_index", "alias_logs_" + tenant + "-" + strYear);
		        
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public CloseableHttpClient getClient() {
		CloseableHttpClient httpclient = null;

		try {
			// fire up the http client
			httpclient = HttpClientBuilder.create().build();
			SSLContextBuilder builder = new SSLContextBuilder();

			builder.loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			});

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (httpclient);
	}
	
	public HttpContext httpContext = new BasicHttpContext();
	HttpResponse loginResponse = null;
	
	private HttpResponse getLoginResponse() {
		CloseableHttpClient client = getClient();
		HttpResponse preLoginResponseEx = null;

		String sTargetUrl = "https://".concat("eoe.elastica-inc.com");
		Login login = new Login("farhan.jaleel@elasticaqa.net", "Elastica@123");
		String sLoginUrl = sTargetUrl + "/user/loginapi";

		CookieStore cookieStore = new BasicCookieStore();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		try {
			// loadPortalResponse = executeHttpRequest(httpContext,client,"GET",
			// "https://eoe.elastica-inc.com", null,null,null,null);
			preLoginResponseEx = executeHttpRequest(httpContext, client, "GET",
					"https://".concat("eoe.elastica-inc.com").concat("/admin/deployment"), null, null, null, null);
			preLogVerification(httpContext, client, sTargetUrl, preLoginResponseEx, "");

			StringEntity sEntity = new StringEntity(login.getPayloadAsString());
			loginResponse = executeHttpRequest(httpContext, client, "POST", sLoginUrl, null, headerArrayGlobal, sEntity, null);

		} catch (Exception e) {

		}

		return loginResponse;
	}
	
	public ArrayList<BasicHeader> headerArrayGlobal = new ArrayList<BasicHeader>();
	
	public HttpResponse preLogVerification(HttpContext ctx, CloseableHttpClient myClient, String sTargetUrl, HttpResponse loginResponse,
			String sOutputFile) {
		HttpResponse preLoginResponse = null;
		String sForensicsUrl = sTargetUrl + "/" + "static/ng/appForensics/index.html";// sTargetUrl+"/"+"admin/deployment";
		String spreLoggingURL = "https://".concat("eoe.elastica-inc.com").concat("/admin/deployment");
		try {

			Header[] loginHeaders = loginResponse.getAllHeaders();
			String csrfToken = getResponseHeaderAttribute(loginHeaders, "csrftoken");
			String sessionId = getResponseHeaderAttribute(loginHeaders, "sessionid");

			BasicCookieStore cookieStore = new BasicCookieStore();
			BasicClientCookie csrfTokenCookie = new BasicClientCookie("csrftoken", csrfToken);
			BasicClientCookie sessionCookie = new BasicClientCookie("sessionid", sessionId);

			cookieStore.addCookie(csrfTokenCookie);
			cookieStore.addCookie(sessionCookie);

			ArrayList<BasicHeader> headerArray = new ArrayList<BasicHeader>();

			// headerArray.add(new BasicHeader("Host", sHost));
			headerArray.add(new BasicHeader("Connection", "keep-alive"));
			// headerArray.add(new BasicHeader("Content-Length", "1010"));
			headerArray.add(new BasicHeader("Accept", "application/json, text/plain, */*"));
			headerArray.add(new BasicHeader("Origin", sTargetUrl));
			headerArray.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
			headerArray
					.add(new BasicHeader("User-Agent",
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"));
			headerArray.add(new BasicHeader("X-CSRFToken", csrfToken));
			headerArray.add(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
			headerArray.add(new BasicHeader("Referer", sForensicsUrl));
			// headerArray.add(new BasicHeader("Accept-Encoding",
			// "gzip,deflate,sdch"));
			headerArray.add(new BasicHeader("Accept-Language", "en-US,en;q=0.8"));
			// adding two additional headers per Chaks
			// this is really bad and you need to make this more elegant
			headerArray.add(new BasicHeader("Cookie", "csrftoken=" + csrfToken + "; " + "sessionid=" + sessionId));
//			headerArray.add(new BasicHeader("Cookie", "csrftoken=" + csrfToken + "; " + "sessionid=" + sessionId));

			headerArrayGlobal.addAll(headerArray);

			preLoginResponse = executeHttpRequest(ctx, myClient, "POST", spreLoggingURL, null, headerArray, null, sOutputFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (preLoginResponse);
	}
	
	public HttpResponse lastResponse;
	public ArrayList<String> responseOutputArray;
	public JsonObject lastPayloadProcessed;
	public String sActivityLogOutputFile = "";
	
	public HttpResponse executeHttpRequest(HttpContext ctx, CloseableHttpClient httpclient, String methodType, String targetUrl,
			Hashtable<String, String> params, ArrayList<BasicHeader> headers, StringEntity sEntity, String sOutputFile) {

		InputStream instream = null;
		String responseString = "";
		HttpResponse hResponse = null;

		try {

			// decide what to do based on a GET or POST
			if (methodType.equals("GET")) {
				hResponse = getRequest(ctx, httpclient, targetUrl);

			} else if (methodType.equals("POST")) {

				HttpPost httppost = new HttpPost(targetUrl);

				if (params != null) {

					ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

					for (Map.Entry<String, String> entry : params.entrySet()) {
						String key = entry.getKey();
						// System.out.println(ø"key is: "+key);
						String value = entry.getValue();
						// System.out.println("value is: "+value);
						postParameters.add(new BasicNameValuePair(key, value));
					}

					// httppost.setEntity(new
					// UrlEncodedFormEntity(postParameters));
				}

				if (headers != null) {
					for (Header header : headers) {
						httppost.addHeader(header);
					}
				}

				httppost.setEntity(sEntity);

				hResponse = httpclient.execute(httppost, ctx);
				lastResponse = hResponse;
				// printHeaderValues(hResponse);
			}

			/*
			 * if(sActivityLogOutputFile != null)
			 * this.getResponseEntityContents(hResponse,
			 * sActivityLogOutputFile);
			 */

			// get the entity out of the repsonse if it has one

			HttpEntity entity = hResponse.getEntity();

			// as long as we have a response, parse the stream into a single
			// string
			if (entity != null) {
				// added property on Jan. 8th
				// nulling it out and instantiating it every time it's called
				responseOutputArray = null;
				responseOutputArray = new ArrayList<String>();

				instream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

				// begin temporary hack
				JsonStreamParser jsonStreamParser = new JsonStreamParser(reader);
				JsonElement jsonElement;

				if (jsonStreamParser.hasNext()) {
					jsonElement = jsonStreamParser.next();
					if (jsonElement.isJsonObject()) {
						// System.out.println(jsonElement.toString());

						JsonObject jsonObject = new JsonObject();
						jsonObject.add("payloadFor " + targetUrl, jsonElement);

						lastPayloadProcessed = jsonObject;

					}
				}

				// end temporary hack

				String line;

				while ((line = reader.readLine()) != null) {
					responseOutputArray.add(line);// added this on Jan. 8th to
													// try to pull out the lines
													// in a JSON payload stored
													// in the entity
					responseString = responseString + line;
				}
				if (sActivityLogOutputFile != null) {
					// GenericUtility genericUtility=new GenericUtility();
					// genericUtility.appendFile(sActivityLogOutputFile,
					// responseString);
				}
				jsonStreamParser = null;
				reader.close();
				// System.out.println(responseString);
			}

			// always close the streams
			if (instream != null) {
				instream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}

		return hResponse;
	}
	
	public HttpResponse getRequest(HttpContext ctx, CloseableHttpClient httpclient, String sUrl) {
		HttpResponse hResponse = null;

		try {
			if (httpclient == null)
				httpclient = getClient();

			HttpGet httpget = new HttpGet(sUrl);

			hResponse = httpclient.execute(httpget);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (hResponse);
	}
	
	public String getResponseHeaderAttribute(Header[] lHeaders, String sAttribute) {
		String sHeaderAttribute = null;

		try {
			for (Header header : lHeaders) {
				// System.out.println(header.getValue().toString());

				if (header.getValue().indexOf(sAttribute) > -1) {
					sAttribute = sAttribute + "=";
					int startAt = header.getValue().indexOf(sAttribute) + sAttribute.length();
					int endAt = header.getValue().indexOf(";", startAt);
					sHeaderAttribute = header.getValue().substring(startAt, endAt);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (sHeaderAttribute);
	}
	
	public class Login {
		private String sUserName;
		private String sPassword;

		/**
		 * This method sets the user name and password fields in this class
		 *
		 * Revision History:
		 *
		 * Tim Mitchell 01/02/14 Created.
		 *
		 */
		public Login(String sUserName, String sPassword) {
			setUserName(sUserName);
			setPassword(sPassword);
		}

		/**
		 * This method returns the value held in the sUserName field
		 *
		 * Revision History:
		 *
		 * Tim Mitchell 01/02/14 Created.
		 *
		 */
		public String getUserName() {
			return (sUserName);
		}

		/**
		 * This method sets the user name field in this class
		 *
		 * Revision History:
		 *
		 * Tim Mitchell 01/02/14 Created.
		 *
		 */
		public void setUserName(String sUserNameAssigned) {
			sUserName = sUserNameAssigned;
		}

		/**
		 * This method returns the value held in the sPassword field
		 *
		 * Revision History:
		 *
		 * Tim Mitchell 01/02/14 Created.
		 *
		 */
		public String getPassword() {
			return (sPassword);
		}

		/**
		 * This method sets the password field in this class
		 *
		 * Revision History:
		 *
		 * Tim Mitchell 01/02/14 Created.
		 *
		 */
		public void setPassword(String sPasswordAssigned) {
			sPassword = sPasswordAssigned;
		}

		/**
		 * This method returns the payload as a string
		 *
		 * Revision History:
		 *
		 * Tim Mitchell 01/02/14 Created.
		 *
		 */
		public String getPayloadAsString() {
			String sPayload = null;

			try {
				JsonObject jsonLogin = new JsonObject();
				jsonLogin.addProperty("email", "qa-admin@o365security.net");
				jsonLogin.addProperty("password", "Elastica@123");
				sPayload = jsonLogin.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return (sPayload);
		}
	}
	
	
	
	private String getJSONValue(String json, String key) throws JsonProcessingException, IOException {
		JsonFactory factory = new JsonFactory();

		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode rootNode = mapper.readTree(json);
		return rootNode.get(key).toString();
	}
	
	public static String getResponseBody(HttpResponse response) {
		try {
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getAuthParam(String username, String password) throws IOException {
		WebDriver driver = null;
		String result = null;
		try {
			driver = new HtmlUnitDriver();
			driver.get("https://eoe.elastica-inc.com/static/ng/appLogin/index.html#/?redirect=false");

			String webPage = "https://eoe.elastica-inc.com";

			String authString = username + ":" + password;
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: " + authStringEnc);

			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = "Basic " + authStringEnc;

		} finally {
		}
		return result;
	}
	
	public boolean verifyActivityLog() {
		
		String requestUrl = "https://api-eoe.elastica-inc.com/eslogs/displaylogs/";
		CloseableHttpClient client = getClient();
		String response = null;
		loginResponse = getLoginResponse();
		
		try {
			long startTime = 0;
			long endTime = 0;

			Header[] cookieHeaders = loginResponse.getHeaders("Set-Cookie");

			HashMap<String, String> cookieMap = new HashMap<String, String>(cookieHeaders.length);

			String csrfToken = null, sessionId = null;

			System.out.println("Cookie Headers:");
			for (Header header : cookieHeaders) {
				cookieMap.put(header.getName(), header.getValue());

				if (header.getValue().startsWith("csrftoken")) {
					csrfToken = header.getValue().substring(header.getValue().indexOf("=") + 1, header.getValue().indexOf(";"));
				} else if (header.getValue().startsWith("sessionid")) {
					sessionId = header.getValue().substring(header.getValue().indexOf("=") + 1, header.getValue().indexOf(";"));
				}
				System.out.println(header);
			}
			
			HttpPost post = new HttpPost(requestUrl);
			
//			post.setHeader("X-CSRFToken", csrfToken);
//			post.setHeader("X-Session", sessionId);
			
			TestSuiteDTO suiteData = new TestSuiteDTO();
			suiteData.setHost("eoe.elastica-inc.com");
			suiteData.setScheme("https");
			HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders("farhan.jaleel@elasticaqa.net","Elastica@123", suiteData);
			String csfrToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
			post.setHeader("X-CSRFToken", csfrToken);
			sessionId = AuthorizationHandler.getUserSessionID(CSRFHeader);
			post.setHeader("X-Session", sessionId);
			post.setHeader("X-TenantToken", "8Mcfo3OUrXx33ByfPIlCCA==");
			post.setHeader("X-User", "farhan.jaleel@elasticaqa.net");

			StringEntity se = new StringEntity(
					"{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\"2015-05-23T07:00:00.000Z\",\"to\":\"2015-06-24T06:59:59.999Z\"}}}],\"must_not\":[{\"term\":{\"facility\":\"Elastica\"}}]}},\"filter\":{}}},\"from\":0,\"size\":50,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"facets\":{\"histoGreen\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":-7,\"post_zone\":-7},\"facet_filter\":{\"term\":{\"severity\":\"informational\"}}},\"histoOrange\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":-7,\"post_zone\":-7},\"facet_filter\":{\"term\":{\"severity\":\"warning\"}}},\"histoRed\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":-7,\"post_zone\":-7},\"facet_filter\":{\"term\":{\"severity\":\"critical\"}}},\"histoYellow\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":-7,\"post_zone\":-7},\"facet_filter\":{\"term\":{\"severity\":\"error\"}}},\"user\":{\"terms\":{\"field\":\"user\",\"size\":1000}},\"facility\":{\"terms\":{\"field\":\"facility\",\"size\":1000}},\"location\":{\"terms\":{\"field\":\"location\",\"size\":1000}},\"Object_type\":{\"terms\":{\"field\":\"Object_type\",\"size\":1000}},\"Activity_type\":{\"terms\":{\"field\":\"Activity_type\",\"size\":1000}},\"browser\":{\"terms\":{\"field\":\"browser\",\"size\":1000}},\"severity\":{\"terms\":{\"field\":\"severity\",\"size\":1000}},\"device\":{\"terms\":{\"field\":\"device\",\"size\":1000}},\"__source\":{\"terms\":{\"field\":\"__source\",\"size\":1000}}}},\"sourceName\":\"investigate\",\"apiServerUrl\":\"https://api-eoe.elastica-inc.com/\",\"csrftoken\":\"u9O6BW5vEQ6iWXpGdDWoEWhxWITRKfOz\",\"sessionid\":\"to9wsdvegxohf8wrw0lo9maav3mhxmnq\",\"userid\":\"farhan.jaleel@elasticaqa.net\"}"
					);
			
//			StringEntity se = new StringEntity(
//					"{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\"2015-05-01T07:00:00.000Z\",\"to\":\"2015-06-17T06:59:59.999Z\"}}}],\"must_not\":[{\"term\":{\"facility\":\"Elastica\"}}]}},\"filter\":{}}},\"from\":0,\"size\":50,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"facets\":{\"histoGreen\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":-7,\"post_zone\":-7},\"facet_filter\":{\"term\":{\"severity\":\"informational\"}}},\"histoOrange\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":-7,\"post_zone\":-7},\"facet_filter\":{\"term\":{\"severity\":\"warning\"}}},\"histoRed\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":-7,\"post_zone\":-7},\"facet_filter\":{\"term\":{\"severity\":\"critical\"}}},\"histoYellow\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\",\"time_zone\":-7,\"post_zone\":-7},\"facet_filter\":{\"term\":{\"severity\":\"error\"}}},\"user\":{\"terms\":{\"field\":\"user\",\"size\":1000}},\"facility\":{\"terms\":{\"field\":\"facility\",\"size\":1000}},\"location\":{\"terms\":{\"field\":\"location\",\"size\":1000}},\"Object_type\":{\"terms\":{\"field\":\"Object_type\",\"size\":1000}},\"Activity_type\":{\"terms\":{\"field\":\"Activity_type\",\"size\":1000}},\"browser\":{\"terms\":{\"field\":\"browser\",\"size\":1000}},\"severity\":{\"terms\":{\"field\":\"severity\",\"size\":1000}},\"device\":{\"terms\":{\"field\":\"device\",\"size\":1000}},\"__source\":{\"terms\":{\"field\":\"__source\",\"size\":1000}}}},\"sourceName\":\"investigate\",\"apiServerUrl\":\"https://api-eoe.elastica-inc.com/\",\"csrftoken\":\"s3Qf04dAp5i3DgZgjx29L6MREJAzyeur\",\"sessionid\":\"fgi3034hu50ry5hnufs4lv5xohscn7hd\",\"userid\":\"farhan.jaleel@elasticaqa.net\"}"
//					);
			
			post.setEntity(se);
			
			post.setHeader("Authorization", getAuthParam("farhan.jaleel@elasticaqa.net", "Elastica@123"));

			startTime = System.currentTimeMillis();
			HttpResponse postResponse = client.execute(post, httpContext);
			endTime = System.currentTimeMillis();
			System.out.println("Response Printing....... : " + postResponse);
			response = getResponseBody(postResponse);
			System.out.println("Response Body Printing....... : " + response);
			String hits = getJSONValue(getJSONValue(response, "hits"), "hits");
			System.out.println("Hits......" + hits);

			JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
			for (int i = 0; i < jArray.size(); i+=50) {
				String source = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
				String msg = getJSONValue(source, "message");
				System.out.println("Source Printing....." + source);
				System.out.println("Msg Printing....." + msg);
//
//				if (i == 0 && action.equals("Delete")) {
//					return msg.contains("File '" + fileName + fileExt + "' has been deleted");
//				} else if (i == 1 && action.equals("Risk")) {
//					if (fileName.startsWith("PII")) {
//						return msg.contains("File " + fileName + fileExt + " has risk(s) - PII");
//					} else if (fileName.startsWith("PCI")) {
//						return msg.contains("File " + fileName + fileExt + " has risk(s) - PCI");
//					} else if (fileName.startsWith("HIPAA")) {
//						return msg.contains("File " + fileName + fileExt + " has risk(s) - PII, HIPAA");
//					} else if (fileName.startsWith("SC")) {
//						return msg.contains("File " + fileName + fileExt + " has risk(s) - PII, Source Code");
//					}
//				} else if (i == 2 && action.equals("Add")) {
//					return msg.contains("User added '" + fileName + ".");
//				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void deserializeActivityLogs() {
		
		String inputFilePath = "src/test/resources/input/";
		String pathActivityLogs = "src/test/resources/activity_logs/";
		String templateFileName = "test1.json.elasticaqanet";
		String tenant = "elasticaqanet";
		String source = "GW";
		String service = "Google Drive";
		String user = "rocky.mohanraj@elasticaqa.net";
		String user_name = "Rocky";
		String action = "Upload";
		String outfile = "test1.elasticaqanet.json";
		
		Activity activity = new Activity();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFilePath + templateFileName));
			File logFile = new File(pathActivityLogs + outfile);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			
		    for(String line; (line = br.readLine()) != null; ) {
		    	Date curDate = new Date();
		    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		    	SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
		    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
		    	formatTime.setTimeZone(TimeZone.getTimeZone("GMT"));
		    	String strDateTime = formatDate.format(curDate) + "T" + formatTime.format(curDate);
		    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
		    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
		    	String strYear = formatYear.format(curDate);
		    	
		        activity = mapper.readValue(line, Activity.class);
		        
		        activity.get_source().set__source(source);
		        activity.get_source().setFacility(service);
		        activity.get_source().setUser(user);
		        activity.get_source().setUser_name(user_name);
		        activity.get_source().setTest_id("rocky.mohanraj");
		        activity.get_source().setActivity_type(action);
//		        ((ObjectNode)_source).put("elastica_user", user);
		        activity.get_source().setCreated_timestamp(strDateTime);
		        activity.get_source().setInserted_timestamp(strDateTime);
		        activity.set_index("alias_logs_" + tenant + "-" + strYear);
		        
		        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(activity));
		        String strActivity = mapper.writeValueAsString(activity);
		        bw.write(strActivity + "\n");
		        break;
		    }
		    
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
