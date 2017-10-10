/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.universal.common;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.compute.ComputeCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Base64;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.universal.dtos.UserAccount;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.Reporter;

/**
 *
 * @author rahulkumar
 */
public class GoogleMailServices {

	private String userEmailId = new String();
    Gmail service;
    static String CLIENT_ID = "998314684213-34jm3g4k92nejio174qnnb32vojbqg0n.apps.googleusercontent.com"; // Initialization to avoid null pointer exception
    static String CLIENT_SECRET = "YkMqf5GWiHQgHbA1P7BNhxto";// Initialization to avoid null pointer exception

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME
            = "SecurletGmailQA-BLR";

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     */
    private static final List<String> SCOPES
            = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_INSERT,
                    GmailScopes.GMAIL_MODIFY, GmailScopes.GMAIL_SEND, GmailScopes.MAIL_GOOGLE_COM);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     *
     * @param CLIENT_ID
     * @param CLIENT_SECRET
     * @param refreshToken
     */
    
    public static String getGmailServiceAccessToken(String CLIENT_ID, String CLIENT_SECRET, String refreshToken){
        String aceessTokenFromRefreshAccessToken=null;
        GoogleMailServices.CLIENT_ID = CLIENT_ID;
        GoogleMailServices.CLIENT_SECRET = CLIENT_SECRET;
        try {
            aceessTokenFromRefreshAccessToken = getAceessTokenFromRefreshAccessToken(refreshToken);     
            Reporter.log(" ## Gmail Service Successfully Initialized..", true);
        } catch (IOException ex) {
            Reporter.log(" ## Gmail Initialization Failure .."+ex.getLocalizedMessage(), true);
        } catch (GeneralSecurityException ex) {
            Reporter.log(" ## Gmail Initialization Failure .."+ex.getLocalizedMessage(), true);
        }
        return aceessTokenFromRefreshAccessToken;
    }
    
    public GoogleMailServices(String CLIENT_ID, String CLIENT_SECRET, String refreshToken) {
    	GoogleMailServices.CLIENT_ID = CLIENT_ID;
    	GoogleMailServices.CLIENT_SECRET = CLIENT_SECRET;
    	try {
    		String aceessTokenFromRefreshAccessToken = getAceessTokenFromRefreshAccessToken(refreshToken);
    		this.service = getGmailService(aceessTokenFromRefreshAccessToken);
    		Reporter.log(" ## Gmail Service Successfully Initialized..", true);
    	} catch (IOException ex) {
    		Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
    	} catch (GeneralSecurityException ex) {
    		Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
    	}
    }
    public GoogleMailServices(String CLIENT_ID, String CLIENT_SECRET, String refreshToken, String userMailId) {
        GoogleMailServices.CLIENT_ID = CLIENT_ID;
        GoogleMailServices.CLIENT_SECRET = CLIENT_SECRET;
        userEmailId=userMailId;
        try {
            String aceessTokenFromRefreshAccessToken = getAceessTokenFromRefreshAccessToken(refreshToken);
            this.service = getGmailService(aceessTokenFromRefreshAccessToken);
            Reporter.log(" ## Gmail Service Successfully Initialized..", true);
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public GoogleMailServices(String accessToken) {
        try {
            this.service = getGmailService(accessToken);
            Reporter.log(" ## Gmail Service Successfully Initialized..", true);
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param transport
     * @param jsonFactory
     * @param tokenResponse
     * @return
     */
    public static GoogleCredential createCredentialWithRefreshToken(HttpTransport transport,
            JsonFactory jsonFactory, TokenResponse tokenResponse) {

        return new GoogleCredential.Builder().setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .build()
                .setFromTokenResponse(tokenResponse);
    }

    private static String getAceessTokenFromRefreshAccessToken(String refreshToken) throws IOException, GeneralSecurityException {
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = createCredentialWithRefreshToken(
                HTTP_TRANSPORT, JSON_FACTORY, new TokenResponse().setRefreshToken(refreshToken));
        credential.refreshToken();
        String newAccessToken = credential.getAccessToken();
        String refreshToken1 = credential.getRefreshToken();
        System.out.println("## Gmail Services : Refresh Token :" + refreshToken1);
        System.out.println("## Gmail Services : New Access Token from Refresh Token ===> " + newAccessToken);
        return newAccessToken;
    }

    private static Credential getCredential(String access_token) throws IOException {
        Credential credential = new ComputeCredential(HTTP_TRANSPORT, JSON_FACTORY);
        credential.setAccessToken(access_token);
        Reporter.log(" ## Gmail Service : Access Token  :" + credential.getAccessToken());
        Reporter.log(" ## Gmail Service : Refresh Token :" + credential.getRefreshToken());
        return credential;
    }

    public static Gmail getGmailService(String accessToken) throws IOException, GeneralSecurityException {
        Credential credential = getCredential(accessToken);
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     *
     */
    public void printLabelsInUserAccount() {
        try {
            // Print the labels in the user's account.
            String user = "me";
            ListLabelsResponse listResponse
                    = service.users().labels().list(user).execute();
            List<Label> labels = listResponse.getLabels();
            if (labels.size() == 0) {
                System.out.println("No labels found.");
            } else {
                System.out.println("Labels:");
                for (Label label : labels) {
                    System.out.printf("- %s\n", label.getName());
                }
            }
        } catch (IOException ex) {
            Reporter.log("Exception in Printing Labels :" + ex.getLocalizedMessage(), true);
        }
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param fileLocation
     * @param filename Name of file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     * @throws java.io.IOException
     */
    private MimeMessage createEmailWithAttachment(List<String> to,List<String> cc,List<String> bcc, String subject,
            String bodyText, String fileLocation, String filename) throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        InternetAddress fAddress = new InternetAddress("me");
        email.setFrom(fAddress);
       
        String sendMailLog = "Time: UTC "+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND MAIL "+(userEmailId.isEmpty()?"":"## Sender :"+userEmailId) + " ## Subject :" + subject ;
    	Reporter.log(sendMailLog, true);


    	for (String to1 : to) {
    		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to1));  
    	}
    	Reporter.log("## Recipients To :" + to.toString() , true);

    	if(cc!=null){
    		for (String cc1 : cc) {
    			email.addRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(cc1));  
    		}
    		Reporter.log("## Recipients CC :" + cc.toString() , true);
    	}
    	if(bcc!=null){
    		for (String bcc1 : bcc) {
    			email.addRecipient(javax.mail.Message.RecipientType.BCC, new InternetAddress(bcc1));  
    		}
    		Reporter.log("## Recipients BCC :" + bcc.toString() , true);
    	}
        email.setSubject(subject);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain; charset=UTF-8");
        mimeBodyPart.setHeader("Content-Type", "text/plain; charset=\"UTF-8\"");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileLocation);
        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(filename);
        String contentType = Files.probeContentType(FileSystems.getDefault().getPath(fileLocation));
        mimeBodyPart.setHeader("Content-Type", contentType + "; name=\"" + filename + "\"");
        mimeBodyPart.setHeader("Content-Transfer-Encoding", "base64");
        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);
        Reporter.log("Attachments:", true);
        Reporter.log(filename, true);
        return email;
    }

    
    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param fileLocation
     * @param filename Name of file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     * @throws java.io.IOException
     */
    public MimeMessage createEmailWithMultipleAttachment(List<String> to,List<String> cc,List<String> bcc, String subject,
    		String bodyText, List<String> fileLocations) throws MessagingException, IOException {
    	Properties props = new Properties();
    	Session session = Session.getDefaultInstance(props, null);
    	MimeMessage email = new MimeMessage(session);
    	InternetAddress fAddress = new InternetAddress("me");
    	email.setFrom(fAddress);
    	String sendMailLog = "Time: UTC "+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND MAIL "+(userEmailId.isEmpty()?"":"## Sender :"+userEmailId) + " ## Subject :" + subject ;
    	Reporter.log(sendMailLog, true);

    	for (String to1 : to) {
    		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to1));  
    	}
    	Reporter.log("## Recipients To :" + to.toString() , true);

    	if(cc!=null){
    		for (String cc1 : cc) {
    			email.addRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(cc1));  
    		}
    		Reporter.log("## Recipients CC :" + cc.toString() , true);
    	}
    	if(bcc!=null){
    		for (String bcc1 : bcc) {
    			email.addRecipient(javax.mail.Message.RecipientType.BCC, new InternetAddress(bcc1));  
    		}
    		Reporter.log("## Recipients BCC :" + bcc.toString() , true);
    	}
    	email.setSubject(subject);
    	MimeBodyPart mimeBodyPart = new MimeBodyPart();
    	mimeBodyPart.setContent(bodyText, "text/plain; charset=UTF-8");
    	mimeBodyPart.setHeader("Content-Type", "text/plain; charset=\"UTF-8\"");
    	Multipart multipart = new MimeMultipart();
    	multipart.addBodyPart(mimeBodyPart);

    	if(fileLocations.size()>0){
    		Reporter.log("Attachments:", true);
    	}
    	for (String fileLocation : fileLocations) {
    		File file=new File(fileLocation);
    		String filename=file.getName();
    		mimeBodyPart = new MimeBodyPart();
    		DataSource source = new FileDataSource(fileLocation);
    		mimeBodyPart.setDataHandler(new DataHandler(source));
    		mimeBodyPart.setFileName(filename);
    		String contentType = Files.probeContentType(FileSystems.getDefault().getPath(fileLocation));
    		mimeBodyPart.setHeader("Content-Type", contentType + "; name=\"" + filename + "\"");
    		mimeBodyPart.setHeader("Content-Transfer-Encoding", "base64");
    		multipart.addBodyPart(mimeBodyPart);
    		Reporter.log(filename, true);
    	}

    	email.setContent(multipart);
    	return email;
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    private MimeMessage createEmail(List<String> to,List<String> cc,List<String> bcc, String from, String subject,
            String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        for (String to1 : to) {
         email.addRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(to1));   
        }
        if(cc!=null){
        for (String cc1 : cc) {
          email.addRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(cc1));  
        }
        }
        if(bcc!=null){
        for (String bcc1 : bcc) {
          email.addRecipient(javax.mail.Message.RecipientType.BCC, new InternetAddress(bcc1));  
        }
        }
        email.setSubject(subject);
        email.setText(bodyText);
        email.setContent(bodyText, "text/plain; charset=UTF-8");
        email.setHeader("Content-Type", "text/plain; charset=\"UTF-8\"");
        
        return email;  
    }

    /**
     * Create a Message from an email
     *
     * @param email Email to be set to raw of message
     * @return Message containing base64url encoded email.
     * @throws IOException
     * @throws MessagingException
     */
    private Message createMessageWithEmail(MimeMessage email)
            throws MessagingException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        email.writeTo(bytes);
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
    /**
     *
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param bodyText
     * @param fileLocations
     * @return 
     */
    public boolean sendMessageWithMultipleAttachment(List<String> to, List<String> cc,List<String> bcc,String subject,
            String bodyText, List<String> fileLocations) {
        try {
            MimeMessage createEmailWithAttachment = createEmailWithMultipleAttachment(to,cc,bcc,subject,bodyText, fileLocations);
            sendMessage(createEmailWithAttachment);
            return true;
        } catch (MessagingException | IOException ex) {
            Reporter.log("Issue in Sending Multiple Mail/Message :"+ex.getLocalizedMessage(),true);
            return false;
        }
    }
    
    /**
     *
     * @param to
     * @param subject
     * @param bodyText
     * @param fileLocation
     * @param filename
     */
    public boolean sendMessageWithAttachment(List<String> to, List<String> cc,List<String> bcc,String subject,
            String bodyText, String fileLocation, String filename) {
        try {
            MimeMessage createEmailWithAttachment = createEmailWithAttachment(to,cc,bcc,subject,bodyText, fileLocation, filename);
            sendMessage(createEmailWithAttachment);
            return true;
        } catch (MessagingException | IOException ex) {
            Reporter.log("Issue in Sending Mail/Message :"+ex.getLocalizedMessage(),true);
            return false;
        }
    }
    
    /**
     * 
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param bodyText
     * @param fileLocation
     * @param filename
     * @return
     */
    public Message sendMailWithAttachment(List<String> to, List<String> cc,List<String> bcc,String subject,
            String bodyText, String fileLocation, String filename) {
    	Message message = null;
    	
    	try {
            MimeMessage createEmailWithAttachment = createEmailWithAttachment(to,cc,bcc,subject,bodyText, fileLocation, filename);
            message = createMessageWithEmail(createEmailWithAttachment);
            message = service.users().messages().send("me", message).execute();
        } catch (MessagingException | IOException ex) {
            Reporter.log("Issue in Sending Mail/Message :"+ex.getLocalizedMessage(),true);
        }
    	
    	return message;
    }
    
    /**
     * 
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param bodyText
     * @return
     */
    public Message sendMailWithBody(List<String> to,List<String> cc,List<String> bcc, String subject, String bodyText) {
        
    	Message message = null;
    	
    	try {
    		String sendMailLog = "Time: UTC "+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND MAIL "+(userEmailId.isEmpty()?"":"## Sender :"+userEmailId) + " ## Subject :" + subject ;
        	Reporter.log(sendMailLog, true);
        	
        	if(to!=null){
            	
        		Reporter.log("## Recipients To :" + to.toString() , true);
        	}

        	if(cc!=null){
        		
        		Reporter.log("## Recipients CC :" + cc.toString() , true);
        	}
        	if(bcc!=null){
        		
        		Reporter.log("## Recipients BCC :" + bcc.toString() , true);
        	}
        	
    		
    		MimeMessage createEmail = createEmail(to,cc,bcc,"me", subject, bodyText);
    		message = createMessageWithEmail(createEmail);
            message = service.users().messages().send("me", message).execute();
        } catch (MessagingException | IOException ex) {
            Reporter.log("Issue in Sending Mail/Message :"+ex.getLocalizedMessage(),true);
        }
    	
    	return message;
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param userIdList
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @param email Email to be sent.
     * @throws IOException
     */
    public boolean sendMessage(List<String> userIdList, MimeMessage email)
             {
        for (String userId: userIdList) {
            try {
                Message message = createMessageWithEmail(email);
                message = service.users().messages().send(userId, message).execute();
                Reporter.log("Message id: " + message.getId(),true);
                Reporter.log(message.toPrettyString(),true);
            } catch (MessagingException | IOException ex) {
                Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }  
        return true;
    }

    /**
     *
     * @param email
     * @return 
     * @throws IOException
     */
    public boolean sendMessage(MimeMessage email)
             {
        try {
            Message message = createMessageWithEmail(email);
            message = service.users().messages().send("me", message).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     *
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param bodyText
     * @return 
     */
    public boolean sendPlainMessage(List<String> to,List<String> cc,List<String> bcc, String subject, String bodyText) {
        try {
            MimeMessage createEmail = createEmail(to,cc,bcc,"me", subject, bodyText);
            String sendMailLog = "Time: UTC "+DateTime.now(DateTimeZone.UTC).toString()+" Action : Send "+(userEmailId.isEmpty()?"":"## User :"+userEmailId) + " ## Subject :" + subject ;
            Reporter.log(sendMailLog, true);
            
            sendMessage(createEmail);
        } catch (MessagingException ex) {
            Reporter.log("Issue Found with Compose Msg :" + ex.getLocalizedMessage(), true);
            return false;
        }
        return true;
    }

    
    // DRAFT ACTIVITIES........
    /**
     * Create draft email.
     *
     * @param email the MimeMessage used as email within the draft
     * @return the created draft
     * @throws MessagingException
     * @throws IOException
     */
    public Draft createDraft(MimeMessage email)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(email);
        Draft draft = new Draft();
        draft.setMessage(message);
        String sendMailLog = "Time: UTC "+DateTime.now(DateTimeZone.UTC).toString()+" Action : SAVE IN DRAFT "+(userEmailId.isEmpty()?"":"## User :"+userEmailId) + " ## Subject :" + email.getSubject() ;
        Reporter.log(sendMailLog, true);
        draft = service.users().drafts().create("me", draft).execute();
        return draft;
    }

    /**
     *
     * @param to
     * @param subject
     * @param bodyText
     * @return
     */
    public Draft createDraft(List<String> to,List<String> cc,List<String> bcc, String subject, String bodyText) {
        try {
            MimeMessage createEmail = createEmail(to,cc,bcc,"me", subject, bodyText);
            Draft createDraft = createDraft(createEmail);
            String sendMailLog = "Time: UTC "+DateTime.now(DateTimeZone.UTC).toString()+" Action : CREATE DRAFT "+(userEmailId.isEmpty()?"":"## Sender :"+userEmailId) + " ## Subject :" + subject ;
        	Reporter.log(sendMailLog, true);
        	if(to!=null){
        		Reporter.log("## Recipients To :" + to.toString() , true);
        	}
        	if(cc!=null){
        		Reporter.log("## Recipients CC :" + cc.toString() , true);
        	}
        	if(bcc!=null){
        		Reporter.log("## Recipients BCC :" + bcc.toString() , true);
        	}
        	
            return createDraft;
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param draft
     * @return 
     */
    public boolean sendDraft(Draft draft,String subject) {
        try {
        	String sendMailLog = "Time: UTC "+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND FROM DRAFT "+(userEmailId.isEmpty()?"":"## Sender :"+userEmailId) + " ## Subject :" + subject ;
        	Reporter.log(sendMailLog, true);
            service.users().drafts().send("me", draft).execute();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     *
     * @return
     */
    public Gmail.Users.Drafts.List listDraft() {
        try {
            Gmail.Users.Drafts.List list = service.users().drafts().list("me");
            return list;
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param draftId
     * @return
     */
    public Gmail.Users.Drafts.Delete deleteDraft(String draftId) {
        try {
            Gmail.Users.Drafts.Delete delete = service.users().drafts().delete("me", draftId);
            return delete;
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param draftId
     * @return
     */
    public Gmail.Users.Drafts.Get getDraft(String draftId) {
        try {
            Gmail.Users.Drafts.Get get = service.users().drafts().get("me", draftId);
            return get;
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param draftId
     * @param to
     * @param subject
     * @param bodyText
     * @return
     */
    public Gmail.Users.Drafts.Update updateDraft(String draftId, List<String> to, List<String> cc,List<String> bcc,String subject, String bodyText) {
        try {
            Draft createDraft = createDraft(to,cc,bcc, subject, bodyText);
            Gmail.Users.Drafts.Update update = service.users().drafts().update("me", draftId, createDraft);
            System.out.println("$$ Update Draft Activity $$  Draft ID : " + update.getId());
            return update;
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param filePath
     * @return
     */
    private InputStream fileToStream(String filePath) {
        try {
            System.out.println("Converting File  : < " + filePath + " > to Stream");
            InputStream is = new FileInputStream(filePath);
            return is;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //@ NEED TO FIX DRAFT MEDIA ATTACHMENT ISSUE...
    /**
     *
     * @param to
     * @param subject
     * @param bodyText
     * @param mediaAttachemntFile
     * @return
     */
    public Gmail.Users.Drafts.Create createDraftWithMediaContent(List<String> to,List<String> cc,List<String> bcc, String subject, String bodyText, String mediaAttachemntFile) {
        try {
            FileContent fileContent = new FileContent("image/jpeg", new File(mediaAttachemntFile));
            // fileContent.getInputStream();
            // AbstractInputStreamContent abstractInputStreamContent = fileContent;
            // String type = abstractInputStreamContent.getType();
            //  System.out.println("## Media Type :"+type);
            String sendMailLog = "Time: UTC "+DateTime.now(DateTimeZone.UTC).toString()+" Action : CREATE DRAFT "+(userEmailId.isEmpty()?"":"## Sender :"+userEmailId) + " ## Subject :" + subject ;
        	Reporter.log(sendMailLog, true);
        	if(to!=null){
        		Reporter.log("## Recipients TO :" + to.toString() , true);
        	}
        	if(cc!=null){
        		Reporter.log("## Recipients CC :" + cc.toString() , true);
        	}
        	if(bcc!=null){
        		Reporter.log("## Recipients BCC :" + bcc.toString() , true);
        	}
        	
        	Reporter.log("Attachments:"+mediaAttachemntFile, true);
        	
            Draft createDraft = createDraft(to,cc,bcc, subject, bodyText);
            Gmail.Users.Drafts.Create create = service.users().drafts().create("me", createDraft, fileContent);

            System.out.println("$$ Create Draft Activity $$  User ID : " + create.getUserId());
            return create;
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * List all Messages of the user's mailbox matching the query.
     *
     * @param query String used to filter the Messages listed.
     * @param count
     * @return
     * @throws IOException
     */
    public List<Message> listMessagesMatchingQuery(String query, int count) throws IOException {
        Reporter.log(" ---------------- ## Message Query Operations ## -------------------- ",true);
        Reporter.log(" ## Searching for mail with query : " + query,true);
        ListMessagesResponse response = service.users().messages().list("me").setQ(query).execute();
        Long resultSizeEstimate = response.getResultSizeEstimate();
        System.out.println(" ## Total Result Estimated Size :" + resultSizeEstimate);
        List<Message> messages = new ArrayList<>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list("me").setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }
        List<Message> messagesList_Return = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            messagesList_Return.add(messages.get(i));
        }
        for (Message message : messagesList_Return) {
            System.out.println(message.toPrettyString());
            try {
                getMessage(message.getId());
                System.out.println("===========================================================");
            } catch (Exception e) {
                System.out.println("Data Found Null...");
            }
        }
        return messagesList_Return;
    }

    public List<Message> listMessagesMatchingQuery(String query) throws IOException {
        System.out.println(" ---------------- ## Message Query Operations ## -------------------- ");
        Reporter.log((userEmailId.isEmpty()?"":"## User :"+userEmailId)+" ## Searching for mail with query : " + query,true);
        ListMessagesResponse response = service.users().messages().list("me").setQ(query).execute();
        Long resultSizeEstimate = response.getResultSizeEstimate();
        System.out.println(" ## Total Result Estimated Size :" + resultSizeEstimate);
        List<Message> messages = new ArrayList<>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list("me").setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }
        for (Message message : messages) {
            System.out.println(message.toPrettyString());
            try {
                getMessage(message.getId());
            } catch (Exception e) {
                System.out.println("Data Found Null...");
            }

        }
        return messages;
    }

    /**
     * List all Messages of the user's mailbox matching the query.
     *
     * @param query String used to filter the Messages listed.
     * @param count
     * @param labelIds
     * @param labelId
     * @return
     * @throws IOException
     */
    public List<Message> listMessagesMatchingQuery(String query,int count,List<String> labelIds) throws IOException {
        Reporter.log(" ---------------- ## Message Query Operations ## -------------------- ",true);
        Reporter.log(" ## Searching for mail with query : " + query,true);
        ListMessagesResponse response = service.users().messages().list("me").setLabelIds(labelIds).setQ(query).execute();
        Long resultSizeEstimate = response.getResultSizeEstimate();
        System.out.println(" ## Total Result Estimated Size :" + resultSizeEstimate);
        List<Message> messages = new ArrayList<>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list("me").setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }
        List<Message> messagesList_Return = new ArrayList<>();
        for (int i = 0; i < count; i++) {    
            messagesList_Return.add(messages.get(i));    
        }
        for (Message message : messagesList_Return) {
            System.out.println(message.toPrettyString());
            try {
                getMessage(message.getId());
                System.out.println("===========================================================");
            } catch (Exception e) {
                System.out.println("Data Found Null...");
            }
        }
        return messagesList_Return;
    }

    /**
     * Get Message with given ID.
     *
     * @param messageId ID of Message to retrieve.
     * @return Message Retrieved Message.
     * @throws IOException
     */
    public Message getMessage(String messageId)
            throws IOException {
        Message message = service.users().messages().get("me", messageId).execute();
        System.out.println("Message snippet: " + message.getSnippet());
        return message;
    }

    public String getHtmlContentFromMessage(String messageId) {
        try {
            Message message = service.users().messages().get("me", messageId).execute();
            String htmlContentAsString = "";
            MessagePart payload = message.getPayload();
            List<MessagePart> parts = payload.getParts();
            for (MessagePart part : parts) {
                System.out.println("Mime Type :" + part.getMimeType());
                if (part.getMimeType().equals("text/html")) {
                    byte[] decodeBase64 = Base64.decodeBase64(part.getBody().getData());
                    htmlContentAsString = new String(decodeBase64);
                }
            }
            return htmlContentAsString;
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Get a Message and use it to create a MimeMessage.
     *
     * @param messageId ID of Message to retrieve.
     * @return MimeMessage MimeMessage populated from retrieved Message.
     */
    public MimeMessage getMimeMessage(String messageId) {
        try {
            Message message = service.users().messages().get("me", messageId).setFormat("raw").execute();
            byte[] emailBytes = Base64.decodeBase64(message.getRaw());
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
            return email;
        } catch (IOException | MessagingException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Get Attachemnt from Message...
    /**
     * Get the attachments in a given email.
     *
     * @param messageId ID of Message containing attachment..
     * @return
     * @throws IOException
     */
    public Map<String, byte[]> getAttachments(String messageId) throws IOException {
        Message message = service.users().messages().get("me", messageId).execute();
        Map<String, byte[]> attachments = new HashedMap();
        List<MessagePart> parts = message.getPayload().getParts();
        for (MessagePart part : parts) {
            if (part.getFilename() != null && part.getFilename().length() > 0) {
                String filename = part.getFilename();
                String attId = part.getBody().getAttachmentId();
                MessagePartBody attachPart = service.users().messages().attachments().get("me", messageId, attId).execute();
                byte[] fileByteArray = Base64.decodeBase64(attachPart.getData());
                attachments.put(filename, fileByteArray);
            }
        }
        return attachments;
    }
    
    /**
     * Fetch the Attachment from Message and Store into Your Local..
     * @param messageId
     * @param dirLocation
     */
    public void getAndStoreAttacments(String messageId,String dirLocation){
        try {
            storeAttachmentInDir( getAttachments(messageId), dirLocation);
        } catch (IOException ex) {
            Reporter.log("Exception in Retrieve Attachement :"+ex.getLocalizedMessage());
        }
    }

    /**
     *
     * @param message
     * @param dirLocation
     */
    public void getAndStoreAttacments(Message message,String dirLocation){
        try {
            storeAttachmentInDir( getAttachments(message), dirLocation);
        } catch (IOException ex) {
            Reporter.log("Exception in Retrieve Attachement :"+ex.getLocalizedMessage());
        }
    }

    /**
     *
     * @param attachments
     * @param dirLocation
     */
    public void storeAttachmentInDir(Map<String, byte[]> attachments, String dirLocation) {
        for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
            String key = entrySet.getKey();
            byte[] value = entrySet.getValue();
            storeAttachmentInDir(key, value, dirLocation);
        }
    }

    /**
     *
     * @param filename
     * @param fileByteArray
     * @param dirLocation
     */
    private void storeAttachmentInDir(String filename, byte[] fileByteArray, String dirLocation) {
        FileOutputStream fileOutFile = null;
        try {
            fileOutFile = new FileOutputStream(dirLocation + filename);
            fileOutFile.write(fileByteArray);
            fileOutFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found Exception :" + ex.getLocalizedMessage());
        } catch (IOException ex) {
            System.out.println("IO Exception :" + ex.getLocalizedMessage());
        } finally {
            try {
                fileOutFile.close();
            } catch (IOException ex) {
                System.out.println("IO Exception :" + ex.getLocalizedMessage());
            }
        }
    }

    // GET MESSAGE OPERATIONS...
    /**
     *
     * @return Message
     */
    public Message getLatestMail() {
        List<Message> listMessagesMatchingQuery = null;
        try {
            listMessagesMatchingQuery = listMessagesMatchingQuery("", 1);
        } catch (IOException ex) {
            Reporter.log("Exception In Fetching Mail :"+ex.getLocalizedMessage(),true);
        }
        Message latestMessage = listMessagesMatchingQuery.get(0);
        return latestMessage;
    }

    /**
     *
     * @param count
     * @return List<Message>
     */
    public List<Message> getLatestMail(int count) {
        List<Message> listMessagesMatchingQuery = null;
        try {
            listMessagesMatchingQuery = listMessagesMatchingQuery("", 1);
        } catch (IOException ex) {
            Reporter.log("Exception In Fetching Mail :"+ex.getLocalizedMessage(),true);
        }
        return listMessagesMatchingQuery;
    }

    /**
     * IT will return Latest "Message" as per the search Query..
     * @param searchQuery
     * @return
     */
    public Message getLatestMail(String searchQuery) {
        List<Message> listMessagesMatchingQuery = null;
        try {
            listMessagesMatchingQuery = listMessagesMatchingQuery(searchQuery, 1);
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listMessagesMatchingQuery.get(0);
    }
    
    public List<Message> getLatestMail(String searchQuery,int count) {
        List<Message> listMessagesMatchingQuery = null;
        try {
            listMessagesMatchingQuery = listMessagesMatchingQuery(searchQuery, count);
        } catch (IOException ex) {
            Logger.getLogger(GoogleMailServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listMessagesMatchingQuery;
    }
    /**
     * Get the attachments in a given email.
     *
     * @param message
     * @return 
     * @throws IOException
     */
    public Map<String, byte[]> getAttachments(Message message)
            throws IOException {
        String messageId = message.getId();
        return getAttachments(messageId);
    }
    
    /**
   * Trash the specified message.
   *
   * @param msgId ID of Message to trash.
     * @return 
   * @throws IOException
   */
    public boolean trashMessage(String msgId)
    {
    	try {
    		service.users().messages().trash("me", msgId).execute();
    	} catch (IOException ex) {
    		Reporter.log("Exception in Trash mail :"+ex.getLocalizedMessage(),true);
    		return false;
    	}
    	Reporter.log("Message with id: " + msgId + " has been trashed.",true);
    	return true;

    }
  
  /**
   * Remove the specified message from Trash.
   *
   * @param msgId ID of Message to remove from trash.
     * @return
   */
  public boolean untrashMessage(String msgId) {
        try {
            this.service.users().messages().untrash("me", msgId).execute();
        } catch (IOException ex) {
            Reporter.log("Exception in Untrash Message :"+ex.getLocalizedMessage(),true);
            return false;
        }
    Reporter.log("Message with id: " + msgId + " has been untrashed.",true);
    return true;
  }
/**
   * Immediately and permanently deletes the specified thread. This operation cannot
   * be undone. Prefer threads.trash Gmail API instance.
   * @param threadId ID of Thread to delete.
     * @return 
   */
  public boolean deleteThread(String threadId) {
        try {
            this.service.users().threads().delete("me", threadId).execute();
        } catch (IOException ex) {
            Reporter.log("Exception in Delete Thread :"+ex.getLocalizedMessage(),true);
            return false;
        }
    Reporter.log("Thread with id: " + threadId + " deleted successfully.",true);
    return true;
  }

    /**
   * List all Messages of the user's mailbox with labelIds applied.
   *
   * @param labelIds Only return Messages with these labelIds applied.
     * @return 
   * @throws IOException
   */
  public List<Message> listMessagesWithLabels(List<String> labelIds) throws IOException {
    ListMessagesResponse response = this.service.users().messages().list("me")
        .setLabelIds(labelIds).execute();

    List<Message> messages = new ArrayList<>();
    while (response.getMessages() != null) {
      messages.addAll(response.getMessages());
      if (response.getNextPageToken() != null) {
        String pageToken = response.getNextPageToken();
        response = service.users().messages().list("me").setLabelIds(labelIds)
            .setPageToken(pageToken).execute();
      } else {
        break;
      }
    }
    return messages;
  }
  
  
  /**
   * Get specified Label.
   *
   * @param labelId ID of Label to get.
     * @return 
   * @throws IOException
   */
  public Label getLabel(String labelId)
      throws IOException {
    Label label = service.users().labels().get("me", labelId).execute();
    System.out.println("Label " + label.getName() + " retrieved.");
    System.out.println(label.toPrettyString());
    return label;
  }
    /**
     *
     * @param htmlContent
     * @return
     */
    
    private List<String> getHref(String htmlContent) {
        List<String> urlRefList=new ArrayList();
        Document doc = Jsoup.parse(htmlContent);
        Elements links = doc.select("[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }
        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
           // System.out.println("1==>Attr==>"+link.attr("abs:href"));
           // urlRefList.add(link.attr("abs:href"));
        }
        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("href"), trim(link.text(), 35));
           // System.out.println("2==>Attr==>"+link.attr("href"));
            urlRefList.add(link.attr("href"));
        }
        return urlRefList;
    }
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    
    public String getDownloadHref(String htmlContent){
        List<String> printHref = getHref(htmlContent);
        for (String printHref1 : printHref) {
            if(printHref1.contains("download")){
            System.out.println("Links : "+printHref1);
            return printHref1;
            }
        }
        return null;
    }
    
    public static void downloadFile(String downloadHref,String folderLocation){
        Reporter.log("Download URL :" + downloadHref, true);
        HttpResponse response = HttpClient.doGet(downloadHref, null);
        Header lastHeader = response.getLastHeader("Content-Disposition");
        HeaderElement[] elements = lastHeader.getElements();
        String fileName = lastHeader.getValue();
        Reporter.log("File Name As In Header <Content-Disposition> :" + fileName, true);
        fileName = fileName.replace("attachment; filename=\"", "").replaceAll("\"", "");
        Reporter.log("Actual File Name Downloaded :" + fileName, true);
            try {     
                URL url = new URL(downloadHref);  
                Reporter.log("## File Name :"+fileName,true);
                FileUtils.copyURLToFile(url, new File(folderLocation+"/"+fileName));
            } catch (MalformedURLException ex) {
                Reporter.log("Exception Found in File Download :"+ex.getLocalizedMessage());
            } catch (IOException ex) {
                 Reporter.log("Exception Found in File Download :"+ex.getLocalizedMessage());
            }    
    }
      
    public String getDownloadHref(Message message){
        String ref=getDownloadHref(getHtmlContentFromMessage(message.getId()));
        Reporter.log("Download Ref. Feteched From Message Body :"+ref,true);
        return ref;
    }
    
    public void downloadAndStoreFileFromLinkInMsgBody(String query,String targetFolderLocation) throws IOException, MalformedURLException, URISyntaxException{ 
        Reporter.log("Query To Search Mail :"+query,true);
        Reporter.log("Target Folder Location To download the file :"+targetFolderLocation,true);
        downloadAndStoreFileInDir(getDownloadHref(getLatestMail(query)),targetFolderLocation);  
    }
    
    public void downloadAndStoreFileInDir(String downloadUrl,String targetFolderLocation) throws MalformedURLException, IOException, URISyntaxException {     
        HttpResponse response = HttpClientBuilder.create().build().execute(new HttpGet(downloadUrl));
        String fileName = response.getFirstHeader("Content-Disposition").getValue().replaceAll("attachment; filename=","").replaceAll("\"", "");;
        Reporter.log("File Name To Be Downloaded :"+fileName,true);
        storeAttachmentInDir(fileName, IOUtils.toByteArray(response.getEntity().getContent()), targetFolderLocation);  
        Reporter.log("!!! File <"+fileName +"> Successfully Downloaded from :"+downloadUrl+" \n And Stored In :"+targetFolderLocation,true);
    }
   
    public Message getMessageWithSubject(String subject,String label) throws MessagingException, IOException{ 
        List<Message> listMessagesWithLabels = listMessagesMatchingQuery("subject:"+subject + " label:"+label);
        for (Message listMessagesWithLabel : listMessagesWithLabels) {  
        	MimeMessage mimeMessage = getMimeMessage(listMessagesWithLabel.getId());
        	if(mimeMessage.getSubject().equals(subject)){
        		return listMessagesWithLabel;
        	}
        }
        return null;   
  }
  
    public static void main(String[] args) throws IOException, GeneralSecurityException, MessagingException, Exception {

      
        
        String CLIENT_ID="998314684213-34jm3g4k92nejio174qnnb32vojbqg0n.apps.googleusercontent.com";
        String CLIENT_SECRET="YkMqf5GWiHQgHbA1P7BNhxto";
        String refreshToken ="1/fPDwECsPhJAPh6F_TsHHhz7Q1AArPFC-fa6XdV_bNko";
        String dirLocation="/Users/rahulkumar/Desktop/Elastica/Yammer/"; 
        String query="CSV Log Export Request On 04th, Apr 2016 11:00 AM (GMT)";
        
        GoogleMailServices googleMailServices = new GoogleMailServices(CLIENT_ID, CLIENT_SECRET, refreshToken);
        
        googleMailServices.downloadAndStoreFileFromLinkInMsgBody(query,dirLocation);
        
        
              
    }
    /**
     * This method  searches for mail thread under a particular label and delete the same permanently.
     * @param mailSubject
     * @param label
     * @return
     */
    public boolean deleteMailFromLabel(String mailSubject, String label) {
    	boolean result = false;
    	try {
//    		String deleteMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : DELETE MAIL ## User :" + userEmailId+ "## Subject :" + mailSubject+"## Folder :" + label;
    		String deleteMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : DELETE MAIL "+(userEmailId.isEmpty()?"":"## User :"+userEmailId) + "## Subject :" + mailSubject+"## Folder :" + label;
    		Reporter.log(deleteMailLog, true);
    		Message message=this.getMessageWithSubject(mailSubject, label);
    		String threadId=message.getThreadId();
    		result = deleteThread(threadId);
    		if(result){
    			Reporter.log("Mail with subject: " + mailSubject + " deleted successfully.",true);
    		}
    	} catch (Exception ex) {
    		Reporter.log("Exception in delete mail with subject :"+mailSubject+" "+ex.getLocalizedMessage(),true);
    		return false;
    	}
    	return result;
    }
    /**
     * This method  searches for mail thread under a particular label and trash the same.
     * @param mailSubject
     * @param label
     * @return
     */
    public boolean trashMailFromLabel(String mailSubject, String label) {
    	boolean result = false;
    	try {
//    		String deleteMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : DELETE MAIL ## User :" + userEmailId+ "## Subject :" + mailSubject+"## Folder :" + label;
    		 String deleteMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : TRASH MAIL "+(userEmailId.isEmpty()?"":"## User :"+userEmailId) + "## Subject :" + mailSubject+"## Folder :" + label;
    	     Reporter.log(deleteMailLog, true);
    		Message message=this.getMessageWithSubject(mailSubject, label);
			String threadId=message.getThreadId();
			result = trashMessage(threadId);
			if(result){
				Reporter.log("Mail with subject: " + mailSubject + " trashed successfully.",true);
			}
    	} catch (Exception ex) {
    		Reporter.log("Exception in trash mail with subject :"+mailSubject+" "+ex.getLocalizedMessage(),true);
    		return false;
    	}
    	return result;
    }
    
   

}
