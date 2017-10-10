package com.elastica.beatle.tests.gmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.ITestContext;
import org.testng.Reporter;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.MailAction;
import com.elastica.beatle.securlets.dto.MailRemediation;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.Source;
import com.google.api.services.gmail.Gmail;
import com.universal.common.GoogleMailServices;
import com.universal.constants.CommonConstants;


/**
 * All gmail securlets test related utils 
 * @author pushparajt
 *
 */
public class GmailUtils extends SecurletUtils{
	
	protected enum Remediation { ITEM_TRASH_MAIL};
	
	
	public GmailUtils() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public ForensicSearchResults getInvestigateLogs(int from, int to, String facility, HashMap<String, String> hmap, String email, 
			String apiServerUrl, String csrfToken, String sessionId, int offset, int limit, String sourceName) throws Exception {

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		ESQueryBuilder esQueryBuilder = new ESQueryBuilder();
		
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);	
		
		//Get headers
		List<NameValuePair> headers = getHeaders();
		String payload = "";
		payload = esQueryBuilder.getESQuery(tsfrom, tsto, facility, hmap, email, apiServerUrl, csrfToken, sessionId, offset, limit, sourceName);
		
		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);
		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		return fsr;

	}
	
	/**
	 * This method reads a given file and returns the data as string
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	static String readFile(String filePath) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(filePath));
		return new String(encoded, Charset.defaultCharset());
	}
	
	/**
	 * This is the utility method to create remediation object
	 * @param user
	 * @param docType
	 * @param docId
	 * @param remedialAction
	 * @return
	 */
	public MailRemediation getRemediationObject(String user, String docType, String docId, String remedialAction) {
		MailRemediation mailRemediation = new MailRemediation();

		mailRemediation.setDbName(suiteData.getTenantName());
		mailRemediation.setUser(user);
//		o365Remediation.setUserId(userId);
		mailRemediation.setDocType(docType);
		mailRemediation.setDocId(docId);
		mailRemediation.setObjectType("Mail");

		List<String> possibleValues = new ArrayList<String>();
		List<MailAction> actions = new ArrayList<MailAction>();
		MailAction mailAction = new MailAction();
		mailAction.setCode(remedialAction);
		mailAction.setObjectType("Mail");
		mailAction.setPossibleValues(possibleValues);

		mailAction.setMetaInfo(null);
		actions.add(mailAction);
		mailRemediation.setActions(actions);
		return mailRemediation;
	}
	
	/**
	 * This is the utility method to remediate the exposure through api.
	 * @param remediationObject
	 * @return
	 * @throws Exception
	 */
	public int remediateExposureWithAPI(MailRemediation remediationObject) throws Exception {

		List<NameValuePair> headers = getHeaders();

		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = "{\"objects\":[" + MarshallingUtils.marshall(remediationObject) + "]}";

		Reporter.log("Request body:" + payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getGmailRemediation")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
		HttpResponse response =  restClient.doPatch(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request body:" + payload, true);
		Reporter.log("************************************************");
		Reporter.log("Response status:"+ response.getStatusLine().getStatusCode() + " "+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:"+ responseBody, true);
		
		return response.getStatusLine().getStatusCode();
	}
	
	
	/**
	 * @param filterByKey
	 * @param filterByValue
	 * @param searchString
	 * @param searchForUser
	 * @param coutOfLogsExpected
	 * @param maxLogWaitTime
	 * @return
	 */
	@SuppressWarnings("null")
	public ForensicSearchResults searchLogsWithWaitTime(String filterByKey,String filterByValue, String searchString, String searchForUser, int coutOfLogsExpected, int maxLogWaitTime){

		ArrayList<String> logs = new ArrayList<String>();
		ArrayList<String> resultsArray = new ArrayList<String>();
		HashMap<String, String> hmap = new HashMap();
		ForensicSearchResults results = null;
		
		Set<String> hs = new HashSet<>();
		try {
			for (int i = 1; i <= ( maxLogWaitTime); i++ ) {
				
				hmap.put(filterByKey, filterByValue);
				hmap.put("query", searchString);

				Reporter.log("------------------------------------------------------------------------",true );
				Reporter.log("Searching for logs after :"+i+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				 results = getInvestigateLogs(-60, 60, facility.Gmail.name(),  hmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 	suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Google Apps");
				//Reporter.log("Actual file messages:" + actualMailMessages, true);
				if (results.getHits().getTotal()  >= coutOfLogsExpected) {break;}
			}
			


		}
		catch(Exception e) {}

		if(results.getHits().getTotal()==0){
			results =null;
		}
		return results;
	}
	
	/**
	 * This method copy the content of  source file and creates a file mentioned in destination
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFileUsingFileChannels(File source, File dest)
			throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}
	
	/**
	 * This method deletes the files passed in the array
	 * @param myFiles
	 * @throws InterruptedException
	 */
	void deleteFiles(ArrayList<File> myFiles) throws InterruptedException
	{
		
		if(myFiles.size()>=1){
		
			Reporter.log("-----------------------------",true);
			Reporter.log("Deleting temp files",true);
			Reporter.log("-----------------------------",true);
			int i=1;
		   for (File file : myFiles) {
				file.delete();

			    Reporter.log(i+") "+ file.getName(),true);
				i++;
		   }
			
			Reporter.log("-----------------------------",true);
		}
		
	}
	
	
	/**
	 * This method deletes the mails provided in subjectList from the requested Label for the given gmailuser object
	 * @param subjectList
	 * @param fromFolder
	 * @param userName
	 * @param userPassword
	 * @throws InterruptedException
	 */
	void deleteMails(ArrayList<String> subjectList , String fromFolder, GoogleMailServices objMailUser ) throws InterruptedException
	{
		
		if(subjectList.size()>=1){
			
			Reporter.log("-----------------------------",true);
			Reporter.log("Deleting Mails in "+fromFolder,true);
			Reporter.log("-----------------------------",true);
			int i=1;
			for (String subject : subjectList) {
				Reporter.log(i+") "+ subject,true);
				objMailUser.deleteMailFromLabel(subject,fromFolder);
				i++;
			}
			
			Reporter.log("-----------------------------",true);
		}
		
	}
	
	/**
	 * @param filterByKey
	 * @param filterByValue
	 * @param searchString
	 * @param searchForUser
	 * @param expectedCount
	 * @param maxWaitTimeInMinutes
	 * @return
	 */
	@SuppressWarnings("null")
	public boolean checkExposure(String docType,String isInternal, String documentName,  int expectedCount, int maxWaitTimeInMinutes){

		ArrayList<String> logs = new ArrayList<String>();
		ArrayList<String> resultsArray = new ArrayList<String>();
		SecurletDocument exposureDoc = new SecurletDocument();
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		queryParam.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
		queryParam.add(new BasicNameValuePair("name",  documentName));
		int resultCount =0;

		
		try {
			for (int i = 1; i <= ( maxWaitTimeInMinutes); i++ ) {
				

				Reporter.log("------------------------------------------------------------------------",true );
				Reporter.log("Checking for document after :"+i+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				if(docType.equals("docs")){
					exposureDoc = this.getExposedDocuments(elapp.el_google_apps.name(), queryParam);
				}
				else if(docType.equals("risky_docs")){
					exposureDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), queryParam);
				}
				
				resultCount = exposureDoc.getMeta().getTotalCount();
				Reporter.log("Document Count ="+resultCount, true);
				
				if (resultCount  == expectedCount) {return true;} // if count matches return true			
			}
			


		}
		catch(Exception e) {}

		return false;
	}
	
	
	/**
	 * This method returns the messages of the investigate logs in an array
	 * @param fsr
	 * @return
	 */
	public ArrayList<String> retrieveActualMessages(ForensicSearchResults fsr) {
		ArrayList<String> alist = new ArrayList<String>();
		//Reporter.log("Messages List");
		//Reporter.log("----------------------");
		String msg = "";
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			msg = source.getMessage();//.replace("\\", "");
			alist.add(msg);
			//Reporter.log(msg,true);
		}
		return alist;
	}
	
	/**
	 * This method displays the logs in a list
	 * @param myMessages
	 */
	void displayMessageContent(ArrayList<String> myMessages)
	{
		if(myMessages.size()>=1){
			Iterator itr = null;

			itr = myMessages.iterator();
			int i=1;
			Reporter.log("Available logs",true);
			Reporter.log("-----------------------------",true);
			while(itr.hasNext())
			{
				Reporter.log(i+") "+itr.next().toString(),true);
				i++;
			}
			Reporter.log("-----------------------------",true);
		}
		else{
			Reporter.log("----------No logs found----------",true);
		}
	}
	
}
