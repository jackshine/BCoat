package com.elastica.beatle.detect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.Map.Entry;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.testng.Reporter;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.GatewayClient;
import com.elastica.beatle.RestClient.GatewayClientWrapper;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.EPDV3LogParser;
import com.elastica.beatle.replayTool.LogParser;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayReponseDTO;
import com.elastica.beatle.replayTool.replayToolDTO.LogReplayRequestDTO;

public class DetectCommonutils {
	///BEATLe/src/test/resources/detect/configurations/saasAppConf.xml
	public static final String propertyFileName = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"properties"+File.separator+"saasApp.properties";
	
	public static final String DETECT_FILE_UPLOAD_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload";
	public static final String DETECT_FILE_UPLOAD_PATH_TEMP = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload"+File.separator+"temp";
	
public Map<String, String> getPropertyValues() throws IOException{
	Properties properties = new Properties();
	
	File file = new File(propertyFileName);
	FileInputStream fileInput = new FileInputStream(file);
	Map<String , String> propertyMap = new HashMap<>();
	
	//InputStream inStream = getClass().getClassLoader().getResourceAsStream(FilenameUtils.separatorsToSystem(propertyFileName).trim());
	
	if(fileInput!=null){
		properties.load(fileInput);
	//	System.out.println("property file :::::  "+properties);
		
		Enumeration enuKeys = properties.keys();
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
			String value = properties.getProperty(key);
			propertyMap.put(key.trim(), value.trim());
			//System.out.println(key + ": " + value);
		}
	}
	
	
	 return propertyMap;
}
	
 

public static void main(String[] args) throws IOException {
	DetectCommonutils utils = new DetectCommonutils();
	utils.getPropertyValues();
	
}

protected String readFile(String fileName) throws IOException {
	
    BufferedReader reader = new BufferedReader( new FileReader(fileName));
    String         line = null;
    StringBuilder  stringBuilder = new StringBuilder();
    String         ls = System.getProperty("line.separator");

    while( ( line = reader.readLine() ) != null ) {
        stringBuilder.append( line );
        stringBuilder.append( ls );
    }

    return stringBuilder.toString();
}
	

public void replayLogs(String fileName, TestSuiteDTO suiteData, String saasAppUserName) throws UnsupportedEncodingException, Exception {
	
	if (fileName!=null){
	Thread.sleep(5000);
	//HttpHost gateway = new HttpHost("10.0.54.16", 443, "http");
	
	
	
	HttpHost gateway = new HttpHost("10.0.50.68", 443, "http");
	//HttpHost gateway = new HttpHost("10.0.54.115", 443, "http");
	GatewayClient client = new GatewayClient(gateway);
	LogParser parser = new LogParser();
	//String replayFileName = "nwlogs/"+fileName;//"AFolder,CreateFolder.log";
	String replayFileName = suiteData.getSaasApp()+"/"+fileName;//"AFolder,CreateFolder.log";
	//String replayFileName = "logs/"+fileName;//"AFolder,CreateFolder.log";
	System.out.println("repla "+replayFileName);
	Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
	Logger.info("Number of Requests to be Played : " + requestList.size());
	Logger.info("\n Started Replaying Logs\n");
	Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
	System.out.println("User Name: "+suiteData.getSaasAppUsername());
	while (iterator.hasNext()) {  
        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
        if (requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com") || 
        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com") ||
        		requestPair.getKey().getRequestURI().toString().contains("img.en25.com") || 
        		requestPair.getKey().getRequestURI().toString().contains("mozilla.net") ||
        		requestPair.getKey().getRequestURI().toString().contains("gstatic.com") ||
        		requestPair.getKey().getRequestURI().toString().contains("eloqua.com")||
        		requestPair.getKey().getRequestURI().toString().contains("office_online")||
        		requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com")||
        		requestPair.getKey().getRequestURI().toString().contains("versioncheck-bg.addons.mozilla.org")||
        		requestPair.getKey().getRequestURI().toString().contains("addons.seleniumhq.org)")||
        		requestPair.getKey().getRequestURI().toString().contains("office.net")) {
        	
        	System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
        	continue;
        }  
        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), saasAppUserName);
        //Thread.sleep(5000);
        Logger.info("Response Received from gateway");
       // validateResponseStatus(requestPair.getValue(), response);
    }
   // Thread.sleep(55000);
	}
	
}

//This is for Version3
			public void replayLogsEPDV3(String fileName, TestSuiteDTO suiteData, String saasAppUserName) throws UnsupportedEncodingException, Exception {
				if (fileName!=null){
				Thread.sleep(5000);
				//HttpHost gateway = new HttpHost("10.0.54.16", 443, "http");
					
				//HttpHost gateway = new HttpHost("10.0.50.68", 443, "http");
				HttpHost gateway = new HttpHost("10.0.52.30", 443, "http");
				
				//HttpHost gateway = new HttpHost("10.0.54.115", 443, "http");
				GatewayClient client = new GatewayClient(gateway);
				EPDV3LogParser parser = new EPDV3LogParser();
				//String replayFileName = "nwlogs/"+fileName;//"AFolder,CreateFolder.log";
				String replayFileName = suiteData.getSaasApp()+"/"+fileName;//"AFolder,CreateFolder.log";
				//String replayFileName = "logs/"+fileName;//"AFolder,CreateFolder.log";
				System.out.println("repla "+replayFileName);
				Map<LogReplayRequestDTO, LogReplayReponseDTO> requestList = parser.readRequestsFromFile(replayFileName);
				
				Logger.info("Number of Requests to be Played : " + requestList.size());
				Logger.info("\n Started Replaying Logs\n");
				Iterator<Entry<LogReplayRequestDTO, LogReplayReponseDTO>> iterator = requestList.entrySet().iterator();
				System.out.println("User Name: "+suiteData.getSaasAppUsername());
				while (iterator.hasNext()) {  
			        Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO> requestPair = (Map.Entry<LogReplayRequestDTO, LogReplayReponseDTO>)iterator.next();
			        if (requestPair.getKey().getRequestURI().toString().contains("client-channel.google.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("google-analytics.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("img.en25.com") || 
			        		requestPair.getKey().getRequestURI().toString().contains("mozilla.net") ||
			        		requestPair.getKey().getRequestURI().toString().contains("gstatic.com") ||
			        		requestPair.getKey().getRequestURI().toString().contains("eloqua.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("office_online")||
			        		requestPair.getKey().getRequestURI().toString().contains("login.salesforce.com")||
			        		requestPair.getKey().getRequestURI().toString().contains("versioncheck-bg.addons.mozilla.org")||
			        		requestPair.getKey().getRequestURI().toString().contains("addons.seleniumhq.org)")||
			        		//requestPair.getKey().getRequestURI().toString().contains("office.net")||
			        		
			        		//(requestPair.getKey().getRequestURI().toString().contains("static.sharepointonline.com"))||
			        		//(requestPair.getKey().getRequestURI().toString().contains("gwo365beatle-my.sharepoint.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("download.cdn.mozilla.net"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("ns=PendingRequest&ev=PendingNotificationRequest&"))||
			        		(requestPair.getKey().getRequestURI().toString().contains(".css"))||
			        		(requestPair.getKey().getRequestURI().toString().contains(".js"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("secure.aadcdn.microsoftonline-p.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("prod.msocdn.com"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("aus4.mozilla.org"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("download.mozilla.org"))||
			        		(requestPair.getKey().getRequestURI().toString().contains("s1-word-edit-15"))||
			        		
			        		requestPair.getKey().getRequestURI().toString().contains("clientlog.portal.office.com")) {
			        	
			        	
			        	
			        	
			        	System.out.println("I skipped this request : " + requestPair.getKey().getRequestURI().toString());
			        	continue;
			        }  
			        HttpResponse response = GatewayClientWrapper.executeGatewayRequest(client, requestPair.getKey(), suiteData.getSaasAppUsername());
			        //Thread.sleep(5000);
			        Logger.info("Response Received from gateway");
			       // validateResponseStatus(requestPair.getValue(), response);
			    }
			   // Thread.sleep(55000);
				}
				
			}
			
			
			public String marshall(final Object object) throws JAXBException {
				
				try {
					return marshallJSON(object);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			public static String marshallJSON(Object object) throws IOException {
				ObjectMapper mapper = new ObjectMapper();
				mapper.getSerializationConfig().withSerializationInclusion(Inclusion.NON_NULL); 
				AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
				// make deserializer use JAXB annotations (only)
				mapper.setAnnotationIntrospector(introspector);
				// make serializer use JAXB annotations (only)
				mapper.setAnnotationIntrospector(introspector);
				StringWriter writer = new StringWriter();
				mapper.writeValue(writer, object);
				return writer.toString();
			}
			
			
			protected  List<NameValuePair> buildCookieHeaders(TestSuiteDTO suiteData, String csfrToken, String sessionID) throws Exception{
				List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
				requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
				requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + sessionID + "; csrftoken=" + csfrToken + ";"));
				requestHeader.add(new BasicNameValuePair("X-CSRFToken", csfrToken));
				requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
				requestHeader.add(new BasicNameValuePair("Host", suiteData.getHost()));
				requestHeader.add(new BasicNameValuePair("Accept", "application/json"));
				requestHeader.add(new BasicNameValuePair("Content-Type", "application/json"));
				System.out.println(requestHeader.toString());
				
				
				return requestHeader;
			}
			
			public  String getResponseBody(HttpResponse response) {
				try {
					return EntityUtils.toString(response.getEntity(), "utf-8");
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			
			public  String getJSONValue(String json, String key){
				JsonFactory factory = new JsonFactory();

				ObjectMapper mapper = new ObjectMapper(factory);
				JsonNode rootNode;
				try {
					rootNode = mapper.readTree(json);
					 return  rootNode.get(key).toString();
				} catch (Exception e) {
					Reporter.log("key::::::    "+key, true);
					Reporter.log("error::::  "+e.getMessage(), true);
					//e.printStackTrace();
				}
				return null;
				
				
			}



			public static String createSampleFileType(String fileName) {
		String uuId= UUID.randomUUID().toString();
		try {
			File dir = new File(DETECT_FILE_UPLOAD_PATH_TEMP);
			if (!dir.exists()) {
				if (dir.mkdir()) {
				} else {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(DETECT_FILE_UPLOAD_PATH+ File.separator + fileName);
			
			File dest = new File(DETECT_FILE_UPLOAD_PATH_TEMP+ File.separator + uuId + "_" + fileName);
			if (!dest.exists()) {
				dest.createNewFile();
			}
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uuId + "_" + fileName;
	}
			
			
			
}
