package com.universal.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.MapiPropertyType;
import microsoft.exchange.webservices.data.core.enumeration.property.Sensitivity;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.FolderTraversal;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.FolderSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.FileAttachment;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.definition.ExtendedPropertyDefinition;
import microsoft.exchange.webservices.data.property.definition.PropertyDefinitionBase;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.Reporter;

import com.universal.constants.CommonConstants;
import java.util.List;
import microsoft.exchange.webservices.data.property.complex.Attachment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author rahulkumar
 */
/**
 * @author Nissar
 *
 */
public class Office365MailActivities {
	
	private int maxRetryCount = 3;
	private int currRetryCounter = 0;
	private String userEmailId = new String();

    ExchangeService service;
    Folder folder; 
    public Office365MailActivities(String username, String password) {
        this.service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        ExchangeCredentials credentials = new WebCredentials(username, password);
        userEmailId=username;
        service.setCredentials(credentials);
        try {
            service.autodiscoverUrl(username, new RedirectionUrlCallback());
            this.folder=new Folder(service);
        } catch (Exception ex) {
            Reporter.log(ex.getLocalizedMessage(), true);
        }
    }
    static class RedirectionUrlCallback implements IAutodiscoverRedirectionUrl {

        public boolean autodiscoverRedirectionUrlValidationCallback(
                String redirectionUrl) {
            return redirectionUrl.toLowerCase().startsWith("https://");
        }
    }

    public boolean sendMail(String recipient, String subject, String plainBodycontent, ArrayList<String> attachments, boolean save) throws InterruptedException{
    	String response = null;
    	EmailMessage message=null;
    	

    	try {
    		MessageBody msgBody = new MessageBody();
        	msgBody.setText(plainBodycontent);
        	msgBody.setBodyType(BodyType.Text);
    		String sendMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND MAIL ## Sender :"+userEmailId+" ## Recipient :" + recipient + " ## Subject :" + subject +  " ## Save To Sent Folder :" + save;
    		Reporter.log(sendMailLog, true);
    		message = new EmailMessage(service);
    		message.getToRecipients().add(recipient);
    		message.setSubject(subject);
    		message.setBody(msgBody);

    		if (attachments != null) {
    			if(attachments.size()>=1){
    				Reporter.log("Attachments:", true);
    				for (String currAttachment : attachments) {
    					Reporter.log(currAttachment, true);
    					message.getAttachments().addFileAttachment(currAttachment);
    				}
    			}
    		}
    		if (save) {
    			message.sendAndSaveCopy();
    		} else {
    			message.send();
    		}

    		return true;


    	} catch (Exception ex) {

    		//incase of failure in sending mail, retry sending it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue sending mail. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			///Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			sendMail( recipient,  subject,  plainBodycontent, attachments,  save);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;

    }
    
    public boolean sendMail(String recipient, String subject, String plainBodycontent, String bodyType, ArrayList<String> attachments, boolean save) throws InterruptedException{
    	String response = null;
    	EmailMessage message=null;
    	

    	try {
    		MessageBody msgBody = new MessageBody();
        	msgBody.setText(plainBodycontent);
        	msgBody.setBodyType(getBodyType(bodyType));
    		String sendMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND MAIL ## Sender :"+userEmailId+" ## Recipient :" + recipient + " ## Subject :" + subject +  " ## Save To Sent Folder :" + save;
    		Reporter.log(sendMailLog, true);
    		message = new EmailMessage(service);
    		message.getToRecipients().add(recipient);
    		message.setSubject(subject);
    		message.setBody(msgBody);

    		if (attachments != null) {
    			if(attachments.size()>=1){
    				Reporter.log("Attachments:", true);
    				for (String currAttachment : attachments) {
    					Reporter.log(currAttachment, true);
    					message.getAttachments().addFileAttachment(currAttachment);
    				}
    			}
    		}
    		if (save) {
    			message.sendAndSaveCopy();
    		} else {
    			message.send();
    		}

    		return true;


    	} catch (Exception ex) {

    		//incase of failure in sending mail, retry sending it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue sending mail. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			///Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			sendMail( recipient,  subject,  plainBodycontent, attachments,  save);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;

    }

    private BodyType getBodyType(String bodyType) {
		if(bodyType.equalsIgnoreCase("html")){
			return BodyType.HTML;
		}else if(bodyType.equalsIgnoreCase("text")){
			return BodyType.Text;
		}else{
			
		}
		return null;
	}

	public boolean deleteMail(String subject) throws InterruptedException {
        String response;
        String deleteMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : DELETE MAIL ## Subject :" + subject;
        Reporter.log(deleteMailLog, true);
        Reporter.log("Subject :"+subject+"Its goinf to be Hard Delete...",true);
        try {
            Item item = findItem(subject);
            System.out.println(item.getSubject());
            ItemId id = item.getId();
            String uniqueId = id.getUniqueId();
            // Bind to an existing message using its unique identifier.
            EmailMessage message = EmailMessage.bind(service, new ItemId(uniqueId));
            message.delete(DeleteMode.HardDelete);
            return true;
        } catch (Exception ex) {
        	//incase of failure in sending mail, retry sending it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			deleteMail( subject);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
    }

    public boolean deleteMail(String subject, DeleteMode deleteMode) throws InterruptedException {
        String response;
        String deleteMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : DELETE MAIL ## User :" + userEmailId+ "## Subject :" + subject+"## Folder :Inbox.";
        Reporter.log(deleteMailLog, true);
        try {
            Item item = findItem(subject);
            System.out.println(item.getSubject());
            ItemId id = item.getId();
            String uniqueId = id.getUniqueId();
            // Bind to an existing message using its unique identifier.
            EmailMessage message = EmailMessage.bind(service, new ItemId(uniqueId));
            message.delete(deleteMode);
            return true;
        } catch (Exception ex) {
        	//incase of failure in sending mail, retry sending it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			deleteMail( subject,deleteMode);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
    }

    
    public boolean forwardMail(String subject, String recipient) throws InterruptedException {
        String forwardMailResponse = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : FORWARD MAIL  ## Subject :" + subject + " ## Recipient :" + recipient;
        Reporter.log(forwardMailResponse, true);
        EmailMessage message;
        try {
            Item item = findItem(subject);
            System.out.println(item.getSubject());
            ItemId id = item.getId();
            String uniqueId = id.getUniqueId();
            // Bind to an existing message using its unique identifier.
            message = EmailMessage.bind(service, new ItemId(uniqueId));
            String sub = message.getSubject();
            System.out.println("Subject :" + sub);
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setAddress(recipient);
            message.forward(message.getBody(), emailAddress);
            return true;
        } catch (Exception ex) {
        	//incase of failure in sending mail, retry sending it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			forwardMail( subject,recipient);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
    }

    public String deleteAttachmentsAndForwardMail(String subject, String recipient) throws InterruptedException {
        String deleteAttachmentsAndForwardMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : DELETE ATTACHMENT & FORWARD MAIL \n ## Subject :" + subject + " ## Recipient :" + recipient;
        Reporter.log(deleteAttachmentsAndForwardMailLog, true);
        EmailMessage message;
        try {
            Item item = findItem(subject);
            System.out.println(item.getSubject());
            ItemId id = item.getId();
            String uniqueId = id.getUniqueId();
            // Bind to an existing message using its unique identifier.
            message = EmailMessage.bind(service, new ItemId(uniqueId));
            message.getAttachments().clear();
            message.update(ConflictResolutionMode.AlwaysOverwrite);
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setAddress(recipient);
            message.forward(message.getBody(), emailAddress);
            return message.getInternetMessageId();
        } catch (Exception ex) {
        	//incase of failure in sending mail, retry sending it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			deleteAttachmentsAndForwardMail(subject,recipient);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return null;
    }

    public boolean saveToDraft(String subject, String plainBodyContent) throws InterruptedException {
        String saveToDraftLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : SAVE TO DRAFT   ## Subject :" + subject + " ## Body Content :" + plainBodyContent;
        Reporter.log(saveToDraftLog, true);
        String response = null;
        try {
        	MessageBody msgBody = new MessageBody();
        	msgBody.setText(plainBodyContent);
        	msgBody.setBodyType(BodyType.Text);

            // Move to Draft....
            Item item = new EmailMessage(service);
            item.setSubject(subject);
            item.setBody(msgBody);
            item.setSensitivity(Sensitivity.Confidential);
            item.save(new FolderId(WellKnownFolderName.Drafts));
            response = item.getId().getUniqueId();
            return true;
        } catch (Exception ex) {
        	//incase of failure in sending mail, retry sending it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			saveToDraft(subject,plainBodyContent);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
    }

    public void createFolder(String folderName, FolderId parentFolderId) {
        Reporter.log("Action : CREATE FOLDER"+" \n Folder name :"+folderName +" ## Parent Folder :"+parentFolderId.toString());
        try {
            folder.setDisplayName(folderName);
            // creates the folder as a child of the Inbox folder.
            folder.save(parentFolderId);
        } catch (Exception ex) {
            Logger.getLogger(Office365MailActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Folder moveFolder(String folderName,WellKnownFolderName destinationWellKnownFolderName){ 
        Reporter.log("Action : MOVE FOLDER"+" \n Folder name :"+folderName +" ## Parent Folder Id :"+destinationWellKnownFolderName.toString());
        Folder move = null;
        try {
            move = getFolder(folderName).move(destinationWellKnownFolderName);
        } catch (Exception ex) {
            Logger.getLogger(Office365MailActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return move;
    }
    
    public Folder moveFolder(String folderName,FolderId parentFolderId){ 
        Reporter.log("Action : MOVE FOLDER"+" \n Folder name :"+folderName +" ## Parent Folder Id :"+parentFolderId);
        Folder move = null;
        try {
            move = getFolder(folderName).move(parentFolderId);
        } catch (Exception ex) {
            Logger.getLogger(Office365MailActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return move;
    }

    public Folder getFolder(String folderName) {
        Reporter.log("Folder Name :"+folderName,true);
        Folder folderSearch = null;
        try {
          //  Folder folder = new Folder(service);
            FindFoldersResults findFolders = folder.findFolders(new SearchFilter.ContainsSubstring(FolderSchema.DisplayName, folderName), new FolderView(100));
            for (Folder item : findFolders) {
                return item;
            }
        } catch (Exception ex) {
            Logger.getLogger(Office365MailActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return folderSearch;
    }

    public FolderId createFolder(String folderName, WellKnownFolderName wellKnownFolderName) {
        FolderId folderId = null;
        try {
            Folder folder = new Folder(service);
            folder.getId();
            folder.setDisplayName(folderName);
            // creates the folder as a child of the Inbox folder.
            folder.save(wellKnownFolderName);
            folderId = folder.getId();
        } catch (Exception ex) {
            Logger.getLogger(Office365MailActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return folderId;
    }

    // Search Mail By Subject.....
    public Item findItem(String subject) throws ServiceLocalException, Exception {

    	try{
    		ItemView view = new ItemView(10);
    		view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Ascending);
    		view.setPropertySet(new PropertySet(BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.DateTimeReceived));
    		FindItemsResults<Item> findResults
    		= service.findItems(WellKnownFolderName.Inbox,
    				new SearchFilter.SearchFilterCollection(
    						LogicalOperator.Or, new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject),
    						new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject)), view);
    		System.out.println("Total number of items found: " + findResults.getTotalCount());
    		for (Item item : findResults) {
    			return item;
    		}

    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
//    		currRetryCounter++;
//    		if (currRetryCounter <= maxRetryCount) {
//    			Reporter.log("--------------------------------------------------", true);
//    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
//    			Reporter.log("--------------------------------------------------", true);
//    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
//    			findItem(subject);
//    		}
    	}
//    	currRetryCounter = 0;// Reset Retry Counter...
    	return null;

    }
    
    // Search Mail By Subject.....
    public Item findItemInDecending(String subject) throws ServiceLocalException, Exception {

    	try{
    		ItemView view = new ItemView(10);
    		view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
    		view.setPropertySet(new PropertySet(BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.DateTimeReceived));
    		FindItemsResults<Item> findResults
    		= service.findItems(WellKnownFolderName.Inbox,
    				new SearchFilter.SearchFilterCollection(
    						LogicalOperator.Or, new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject),
    						new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject)), view);
    		System.out.println("Total number of items found: " + findResults.getTotalCount());
    		for (Item item : findResults) {
    			return item;
    		}

    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
//    		currRetryCounter++;
//    		if (currRetryCounter <= maxRetryCount) {
//    			Reporter.log("--------------------------------------------------", true);
//    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
//    			Reporter.log("--------------------------------------------------", true);
//    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
//    			findItem(subject);
//    		}
    	}
//    	currRetryCounter = 0;// Reset Retry Counter...
    	return null;

    }
    
    /**
     * @param subject
     * @param folderName
     * @return
     * @throws ServiceLocalException
     * @throws Exception
     * Search Mail By Subject in folder
     */
    
    
    public Item findItemInFolder(String subject, String folderName) throws ServiceLocalException, Exception {
    	try{


    		ItemView view = new ItemView(10);

    		WellKnownFolderName currFolder = null;
    		if(folderName.equals("Inbox")){
    			currFolder = WellKnownFolderName.Inbox;
    		}
    		else if(folderName.equals("Drafts")){
    			currFolder = WellKnownFolderName.Drafts;
    		}
    		else if(folderName.equals("SentItems")){
    			currFolder = WellKnownFolderName.SentItems;
    		}
    		view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Ascending);
    		view.setPropertySet(new PropertySet(BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.DateTimeReceived));
    		// view.setPropertySet(new PropertySet( EmailMessageSchema.Attachments . ));
    		FindItemsResults<Item> findResults
    		= service.findItems(currFolder,
    				new SearchFilter.SearchFilterCollection(
    						LogicalOperator.Or, new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject),
    						new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject)), view);
    		Reporter.log("Total number of items found: " + findResults.getTotalCount(),true);
    		for (Item item : findResults) {
    			return item;
    		}
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
//    		currRetryCounter++;
//    		if (currRetryCounter <= maxRetryCount) {
//    			Reporter.log("--------------------------------------------------", true);
//    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
//    			Reporter.log("--------------------------------------------------", true);
//    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
//    			findItemInFolder(subject,folderName);
//    		}
    	}
//    	currRetryCounter = 0;// Reset Retry Counter...
    	return null;

    }
    
    public Item findItemInFolderReturnWithAttachment(String subject, String folderName) throws ServiceLocalException, Exception {
    	try{

    		ItemView view = new ItemView(10);
    		Item myItem =null;

    		WellKnownFolderName currFolder = null;
    		if(folderName.equals("Inbox")){
    			currFolder = WellKnownFolderName.Inbox;
    		}
    		else if(folderName.equals("Drafts")){
    			currFolder = WellKnownFolderName.Drafts;
    		}
    		else if(folderName.equals("SentItems")){
    			currFolder = WellKnownFolderName.SentItems;
    		}
    		view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Ascending);
    		view.setPropertySet(new PropertySet(BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.DateTimeReceived));
    		// view.setPropertySet(new PropertySet( EmailMessageSchema.Attachments . ));
    		FindItemsResults<Item> findResults
    		= service.findItems(currFolder,
    				new SearchFilter.SearchFilterCollection(
    						LogicalOperator.Or, new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject),
    						new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject)), view);
    		Reporter.log("Total number of items found: " + findResults.getTotalCount(),true);
    		if(findResults.getTotalCount() >0){
    			myItem = findResults.getItems().get(0);
    			return Item.bind(service, new ItemId(myItem.getId().getUniqueId()));
    		}
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
//    		currRetryCounter++;
//    		if (currRetryCounter <= maxRetryCount) {
//    			Reporter.log("--------------------------------------------------", true);
//    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
//    			Reporter.log("--------------------------------------------------", true);
//    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
//    			findItemInFolderReturnWithAttachment(subject,folderName);
//    		}
    	}
//    	currRetryCounter = 0;// Reset Retry Counter...
    	return null;

    }
	
    /**
     * @param subject
     * @param plainBodyContent
     * @param attachments
     * @return
     * This method adds attachments to mail and saves in draft
     * @throws InterruptedException 
     */
    public boolean addAttachmentAndSaveInDraft(String subject, String plainBodyContent, ArrayList<String> attachments) throws InterruptedException {
        String saveToDraftLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : Add attachment to mail and sav SAVE TO DRAFT \n ## Subject :" + subject + " ## Body Content :" + plainBodyContent;
        Reporter.log(saveToDraftLog, true);
        Item item =null;
        try {
        	MessageBody msgBody = new MessageBody();
        	msgBody.setText(plainBodyContent);
        	msgBody.setBodyType(BodyType.Text);

            // Move to Draft....
             item = new EmailMessage(service);
            item.setSubject(subject);
            item.setBody(msgBody);
            
            if(attachments.size()>=1){
            	for (String currAttachment : attachments) {
            		item.getAttachments().addFileAttachment(currAttachment);
				}
            }
            
            item.setSensitivity(Sensitivity.Confidential);
            item.save(new FolderId(WellKnownFolderName.Drafts));
            return true;
        } catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			addAttachmentAndSaveInDraft(subject,plainBodyContent,attachments);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
    }
    
    /**
     * @param subject
     * @return
     * This method delete attachment from draft mail by searching subject
     * @throws InterruptedException 
     */
    public boolean deleteAttachmentFromDraftMail(String subject) throws InterruptedException {
        String deleteAttachmentResponse  = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : DELETE ATTACHMENT FROM DRAFT ## User :" + userEmailId+ "## Subject :" + subject+"## Folder :Draft";
        Reporter.log(deleteAttachmentResponse, true);
        EmailMessage message;
        try {
            Item item = findItemInFolder(subject, "Drafts");
            System.out.println(item.getSubject());
            ItemId id = item.getId();
            String uniqueId = id.getUniqueId();
            // Bind to an existing message using its unique identifier.
            message = EmailMessage.bind(service, new ItemId(uniqueId));
            message.getAttachments().clear();
            message.update(ConflictResolutionMode.AlwaysOverwrite);
            return true;
        } catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			deleteAttachmentFromDraftMail(subject);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
    }
    
    /**
     * @param recipients
     * @param subject
     * @param plainBodycontent
     * @param attachments
     * @param save
     * @return
     * @throws InterruptedException
     */
    public boolean sendMail(EmailAddressCollection recipients, String subject, String plainBodycontent, ArrayList<String> attachments, boolean save) throws InterruptedException {
        String response = null;
        EmailMessage message =null;
        try {
        	MessageBody msgBody = new MessageBody();
        	msgBody.setText(plainBodycontent);
        	msgBody.setBodyType(BodyType.Text);
        	String sendMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND MAIL ## Sender :"+userEmailId + " ## Subject :" + subject +  " ## Save To Sent Folder :" + save;
            Reporter.log(sendMailLog, true);
             message = new EmailMessage(service);
            for (EmailAddress recipient : recipients) {
            	Reporter.log("## Recipient :" + recipient , true);
            	message.getToRecipients().add(recipient);
            } 
            message.setSubject(subject);
            message.setBody(msgBody);

            if (attachments != null) {
            	if(attachments.size()>=1){
            		Reporter.log("Attachments:", true);
                	for (String currAttachment : attachments) {
                		Reporter.log(currAttachment, true);
                		message.getAttachments().addFileAttachment(currAttachment);
    				}
                }
            }
            if (save) {
                message.sendAndSaveCopy();
            } else {
                message.send();
            }
            
            return true;
            
        } catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			sendMail( recipients,  subject,  plainBodycontent,  attachments,  save);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
       
    }
    
   
    /**
     * @param toRecepients
     * @param ccRecepients
     * @param bccRecepients
     * @param subject
     * @param plainBodycontent
     * @param attachments
     * @param save
     * @return
     * @throws InterruptedException
     */
    public boolean sendMailWithCCAndBCC(EmailAddressCollection toRecepients, EmailAddressCollection ccRecepients,EmailAddressCollection bccRecepients, String subject, String plainBodycontent, ArrayList<String> attachments, boolean save) throws InterruptedException {
        String response = null;
        EmailMessage message =null;
        try {
        	MessageBody msgBody = new MessageBody();
        	msgBody.setText(plainBodycontent);
        	msgBody.setBodyType(BodyType.Text);
        	String sendMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND MAIL ## Sender :"+userEmailId + " ## Subject :" + subject +  " ## Save To Sent Folder :" + save;
            Reporter.log(sendMailLog, true);
             message = new EmailMessage(service);
           if(toRecepients!=null){
            	Reporter.log("## Recipients To :" + toRecepients.getItems().toString() , true);
            	for ( EmailAddress email : toRecepients){
            		message.getToRecipients().add(email);
            	}
            		
            } 
           
           if(ccRecepients!=null){
        	   Reporter.log("## Recipients CC :" + ccRecepients.getItems().toString() , true);
        	   for ( EmailAddress email : ccRecepients){
        		   message.getCcRecipients().add(email);
        	   }
        	   
           } 
           
           if(bccRecepients!=null){
           	Reporter.log("## Recipients BCC :" + bccRecepients.getItems().toString() , true);
           	for ( EmailAddress email : bccRecepients){
           		message.getBccRecipients().add(email);
           	}
           		
           } 
            message.setSubject(subject);
            message.setBody(msgBody);

            if (attachments != null) {
            	if(attachments.size()>=1){
            		Reporter.log("Attachments:", true);
                	for (String currAttachment : attachments) {
                		Reporter.log(currAttachment, true);
                		message.getAttachments().addFileAttachment(currAttachment);
    				}
                }
            }
            if (save) {
                message.sendAndSaveCopy();
            } else {
                message.send();
            }
            
            return true;
            
        } catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			sendMailWithCCAndBCC( toRecepients,ccRecepients,bccRecepients, subject,  plainBodycontent,  attachments,  save);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
       
    }
    
    
    /**
     * @param subject
     * @param folderName
     * @param deleteMode
     * @return
     * @throws InterruptedException
     */
    public boolean deleteMailFromFolder(String subject, String folderName, DeleteMode deleteMode) throws InterruptedException {
        String response;
        String deleteMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : DELETE MAIL ## User :" + userEmailId+ "## Subject :" + subject+"## Folder :" + folderName;
        Reporter.log(deleteMailLog, true);
        try {
            Item item = findItemInFolder(subject, folderName);
            if(item!=null){
            	System.out.println(item.getSubject());
            	ItemId id = item.getId();
            	String uniqueId = id.getUniqueId();
            	// Bind to an existing message using its unique identifier.
            	EmailMessage message = EmailMessage.bind(service, new ItemId(uniqueId));
            	message.delete(deleteMode);
            	return true;
            }
        } catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			deleteMailFromFolder( subject,  folderName,  deleteMode);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
    }
    
    /**
     * @param subject
     * @param recipients
     * @return
     * @throws ServiceLocalException
     * @throws Exception
     */
    public boolean sendMailFromDraft(String subject, EmailAddressCollection recipients) throws ServiceLocalException, Exception {
    	try{

    		ItemView view = new ItemView(10);
    		Item myItem =null;
    		EmailMessage message=null;

    		WellKnownFolderName currFolder = null;
    		currFolder = WellKnownFolderName.Drafts;
    		
    		view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Ascending);
    		view.setPropertySet(new PropertySet(BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.DateTimeReceived));
    		// view.setPropertySet(new PropertySet( EmailMessageSchema.Attachments . ));
    		FindItemsResults<Item> findResults
    		= service.findItems(currFolder,
    				new SearchFilter.SearchFilterCollection(
    						LogicalOperator.Or, new SearchFilter.IsEqualTo(ItemSchema.Subject, subject),
    						new SearchFilter.IsEqualTo(ItemSchema.Subject, subject)), view);
    		Reporter.log("Total number of items found: " + findResults.getTotalCount(),true);
    		if(findResults.getTotalCount() >0){
    			myItem = findResults.getItems().get(0);
    			message = EmailMessage.bind(service, new ItemId(myItem.getId().getUniqueId()));
    			 for (EmailAddress recipient : recipients) {
    	            	Reporter.log("## Recipient :" + recipient , true);
    	            	message.getToRecipients().add(recipient);
    	            } 
    			String sendMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND MAIL ## Sender :"+userEmailId + " ## Subject :" + subject +  " ## Save To Sent Folder : true" ;
    	        Reporter.log(sendMailLog, true);
    			message.sendAndSaveCopy();
    			return true;

    		}
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			sendMailFromDraft( subject,  recipients);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;

    }
    
    /**
     * @param subject
     * @param recipients
     * @return
     * @throws ServiceLocalException
     * @throws Exception
     */
    public boolean emptyFolder(String folderName) throws ServiceLocalException, Exception {
    	try{
    		String deleteMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : emptying folder :" + folderName + " for user:"+ userEmailId;
    		
    		WellKnownFolderName currFolder = null;
    		if(folderName.equals("Inbox")){
    			currFolder = WellKnownFolderName.Inbox;
    		}
    		else if(folderName.equals("Drafts")){
    			currFolder = WellKnownFolderName.Drafts;
    		}
    		else if(folderName.equals("SentItems")){
    			currFolder = WellKnownFolderName.SentItems;
    		}
    		else if(folderName.equals("DeletedItems")){
    			currFolder = WellKnownFolderName.DeletedItems;
    		}
    		
    		FolderId folderId = new FolderId(currFolder);
    		Folder folder = Folder.bind(service, folderId);
    		folder.empty(DeleteMode.HardDelete, false);
    		return true;
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue emptying folder. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			emptyFolder( folderName);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;

    }
    
    

    
    /**
     * @param subject
     * @param folderName
     * @return
     * @throws ServiceLocalException
     * @throws Exception
     * Search Mail By Subject in folder
     */
    
    
    public int searchItemAndReturnCount(String subject, String folderName) throws ServiceLocalException, Exception {
    	try{
    		
    		ItemView view = new ItemView(10);
    		
    		WellKnownFolderName currFolder = null;
    		if(folderName.equals("Inbox")){
    			currFolder = WellKnownFolderName.Inbox;
    		}
    		else if(folderName.equals("Drafts")){
    			currFolder = WellKnownFolderName.Drafts;
    		}
    		else if(folderName.equals("SentItems")){
    			currFolder = WellKnownFolderName.SentItems;
    		}
    		view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Ascending);
    		view.setPropertySet(new PropertySet(BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.DateTimeReceived));
    		// view.setPropertySet(new PropertySet( EmailMessageSchema.Attachments . ));
    		FindItemsResults<Item> findResults
    		= service.findItems(currFolder,
    				new SearchFilter.SearchFilterCollection(
    						LogicalOperator.Or, new SearchFilter.IsEqualTo(ItemSchema.Subject, subject),
    						new SearchFilter.IsEqualTo(ItemSchema.Subject, subject)), view);
//    		Reporter.log("Total number of items found: " + findResults.getTotalCount(),true);
    		int result=findResults.getTotalCount();
    		
    		Reporter.log("User "+userEmailId + " " + folderName + " " +result,true );
    		return result;
    		
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue search. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			searchItemAndReturnCount(subject,folderName);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return 0;
    	
    }
    public int totalCountInFolder(String folderName) throws ServiceLocalException, Exception {
    	try{
    		
    		WellKnownFolderName currFolder = null;
    		if(folderName.equals("Inbox")){
    			currFolder = WellKnownFolderName.Inbox;
    		}
    		else if(folderName.equals("Drafts")){
    			currFolder = WellKnownFolderName.Drafts;
    		}
    		else if(folderName.equals("SentItems")){
    			currFolder = WellKnownFolderName.SentItems;
    		}
    		
    		
    		FolderId folderId = new FolderId(currFolder);
    		
    		Folder folder = Folder.bind(service, folderId);
    		int result=folder.getTotalCount();
    		
    		Reporter.log("User "+userEmailId + " " + folderName + " " +result,true );
    		return result;
    		
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue search. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			totalCountInFolder(folderName);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return 0;
    	
    }
    
    public long totalAttachmentsInMails(String folderName) throws ServiceLocalException, Exception {
    	try{
    		ItemView view = new ItemView(50);
    		EmailMessage myItem = null;
    		FindItemsResults<Item> findResults;
    		long attachmentCount = 0;
    		long mailCount =0;
    		
    		WellKnownFolderName currFolder = null;
    		if(folderName.equals("Inbox")){
    			currFolder = WellKnownFolderName.Inbox;
    		}
    		else if(folderName.equals("Drafts")){
    			currFolder = WellKnownFolderName.Drafts;
    		}
    		else if(folderName.equals("SentItems")){
    			currFolder = WellKnownFolderName.SentItems;
    		}
    		
    		
    		do {
    			findResults = service.findItems(currFolder, view);
    			System.out.println("inside do");
    			
    			for(Item item : findResults.getItems())
    			{
    				mailCount++;
    				myItem=  (EmailMessage) Item.bind(service, new ItemId(item.getId().getUniqueId()));
    				attachmentCount += myItem.getAttachments().getCount();
    				System.out.println("count="+attachmentCount);
    				
    			}
    			
    			view.setOffset(view.getOffset()+50); 
    		} while (findResults.isMoreAvailable());
    		
    		
    		
    		Reporter.log("User "+userEmailId + " " + folderName + " mail count=" +mailCount+ " attachment count=" +attachmentCount,true );
    		return attachmentCount;
    		
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue search. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			totalCountInFolder(folderName);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return 0;
    	
    }
    
    public TreeMap<String, Long> getMailAndAttachmentCount(String mailSubject, String draftSubject) throws ServiceLocalException, Exception {
    	try{
    		ArrayList<Long> result = new ArrayList<Long>();
    		ItemView view = new ItemView(50);
    		EmailMessage myItem = null;
    		long attachmentCount = 0;
    		long mailCount =0;
    		FindItemsResults<Item> findResults = null;
    		long myMailCount =0;
    		long othersMailCount =0;
    		long myMailAttachmentsCount =0;
    		long othersMailAttachmentCount =0;
    		
    		long countInbox  = Folder.bind(service, WellKnownFolderName.Inbox).getTotalCount();
    		long countDraft  = Folder.bind(service, WellKnownFolderName.Drafts).getTotalCount();
    		long countSentItems  = Folder.bind(service, WellKnownFolderName.SentItems).getTotalCount();
    		long countDeletedItems = Folder.bind(service, WellKnownFolderName.DeletedItems).getTotalCount();
    		int tempCount =0;
    		
    		TreeMap<String, Long> results = new TreeMap<String, Long>();
    				
    		results.put("1.countInbox",countInbox );
    		results.put("2.countDraft",countDraft );
    		results.put("3.countSentItems",countSentItems );
    		results.put("4.countDeletedItems",countDeletedItems );
    		
    		WellKnownFolderName [] knownFolder = new  WellKnownFolderName[] {WellKnownFolderName.Inbox, WellKnownFolderName.Drafts, WellKnownFolderName.SentItems,  WellKnownFolderName.DeletedItems};
    		
    		for(WellKnownFolderName currFolder : knownFolder){
    			do {
    				findResults
    				= service.findItems(currFolder,view);
//    				    					new SearchFilter.SearchFilterCollection(
//    				    							LogicalOperator.And, new SearchFilter.IsEqualTo(ItemSchema.Subject, subject),
//    				    												 new SearchFilter.IsEqualTo(ItemSchema.HasAttachments, true)), view);

//    				int i=0;
//    				System.out.println(currFolder);
//    				System.out.println(findResults.getTotalCount());
    				for(Item item : findResults.getItems())
    				{
//    					++i;
//    					System.out.println(i+item.getSubject());

//    					
    					myItem=  (EmailMessage) Item.bind(service, new ItemId(item.getId().getUniqueId()));
    					tempCount=myItem.getAttachments().getCount();
    					

    					if(myItem.getSubject().equals(mailSubject) || myItem.getSubject().equals(draftSubject)){
    						myMailCount+=1;
    						myMailAttachmentsCount +=tempCount;
    					}
    					else{
    						othersMailCount+=1;
    						othersMailAttachmentCount+=tempCount;
    					}
    				}

//    				System.out.println(view.getOffset());
    				view.setOffset(view.getOffset()+50); 
    			} while (findResults.isMoreAvailable());
    			view.setOffset(0);
    			findResults=null;

    			results.put(currFolder.toString()+"_StressMailCount",myMailCount );
    			results.put(currFolder.toString()+"_StressMailAttachmentsCount",myMailAttachmentsCount );
    			results.put(currFolder.toString()+"_OthersMailCount",othersMailCount );
    			results.put(currFolder.toString()+"_OthersMailAttachmentCount",othersMailAttachmentCount );
    			 myMailCount =0;
        		 othersMailCount =0;
        		 myMailAttachmentsCount =0;
        		 othersMailAttachmentCount =0;
    		}
    		Reporter.log("--------------------------------------------------", true);
    		Reporter.log("User:"+userEmailId,true);
    		 Iterator it = results.entrySet().iterator();
    		    while (it.hasNext()) {
    		        Map.Entry pair = (Map.Entry)it.next();
    		        System.out.println(pair.getKey() + " = " + pair.getValue());
//    		       // it.remove(); // avoids a ConcurrentModificationException
    		    }
    		Reporter.log("--------------------------------------------------", true);
    		return results;
    		
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue search. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			getMailAndAttachmentCount(mailSubject,draftSubject);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return null;
    	
    }
    
    public ArrayList<Long> totalMailSentToExternal(String internalUserDetails) throws ServiceLocalException, Exception {
    	try{
    		ArrayList<Long> result = new ArrayList<Long>();
    		ItemView view = new ItemView(50);
    		EmailMessage myItem = null;
    	    long attachmentCount = 0;
    	    long mailCount =0;
    	    FindItemsResults<Item> findResults = null;

    	    do {
    	    	 findResults
        		= service.findItems(WellKnownFolderName.SentItems,
        				new SearchFilter.SearchFilterCollection(
        						LogicalOperator.And, new SearchFilter.Not(new SearchFilter.ContainsSubstring (ItemSchema.DisplayTo, internalUserDetails)),
        											 new SearchFilter.Not(new SearchFilter.ContainsSubstring (ItemSchema.DisplayTo, "DistGroup")),
        						new SearchFilter.IsEqualTo(ItemSchema.HasAttachments, true)), view);

    	        for(Item item : findResults.getItems())
    	        {
    	        	mailCount++;
    	        	myItem=  (EmailMessage) Item.bind(service, new ItemId(item.getId().getUniqueId()));
    	        	attachmentCount += myItem.getAttachments().getCount();
//    	        	System.out.println("count="+myItem.getDisplayTo());
    	        	
    	        }

    	        view.setOffset(view.getOffset()+50); 
    	    } while (findResults.isMoreAvailable());
    	    

    	    result.add(0, mailCount);
    	    result.add(1, attachmentCount);
    		Reporter.log("User "+userEmailId + " SentItems external mail count=" +mailCount+ " attachment count=" +attachmentCount,true );
    		return result;
    		
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue search. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			totalMailSentToExternal(internalUserDetails);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return null;

    }
    
    
    /**
     * @param toRecepients
     * @param ccRecepients
     * @param bccRecepients
     * @param subject
     * @param htmlBody
     * @param attachments
     * @param save
     * @return
     * @throws InterruptedException
     */
    public boolean sendMailWithInLineAttachment(EmailAddressCollection toRecepients, EmailAddressCollection ccRecepients,EmailAddressCollection bccRecepients, String subject, ArrayList<String> attachments, boolean save) throws InterruptedException {
        String response = null;
        EmailMessage message =null;
       
        Path p  = null;
        String fileName = null;
        try {
        	
        	String htmlBody=	"<html><head></head><body>This is test mail with inline attachment<br>";
        	 
        	
        	
        	 message = new EmailMessage(service);
        	 
             


        	
        	for (int i=0; i<=(attachments.size()-1);i++) {
        		
        		
        		
        		System.out.println(attachments.get(i));
        		
        		p  = Paths.get(attachments.get(i));
        		fileName = p.getFileName().toString();
        		message.getAttachments().addFileAttachment(fileName,attachments.get(i) );
        		message.getAttachments().getItems().get(i).setContentType("image"); 
        		message.getAttachments().getItems().get(i).setIsInline(true);
        		message.getAttachments().getItems().get(i).setContentId(fileName);
        		
        		htmlBody+= "<img  id=\""+i+1+"\" src=\"cid:"+fileName+"\"></img><br>";
				
			}
        	htmlBody+="</body></html>";
        	message.setBody(new MessageBody(BodyType.HTML,htmlBody));
        	message.setSubject(subject);
        			
        	
        	String sendMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : SEND MAIL ## Sender :"+userEmailId + " ## Subject :" + subject +  " ## Save To Sent Folder :" + save;
            Reporter.log(sendMailLog, true);
            
           if(toRecepients!=null){
            	Reporter.log("## Recipients To :" + toRecepients.getItems().toString() , true);
            	for ( EmailAddress email : toRecepients){
            		message.getToRecipients().add(email);
            	}
            		
            } 
           
           if(ccRecepients!=null){
        	   Reporter.log("## Recipients CC :" + ccRecepients.getItems().toString() , true);
        	   for ( EmailAddress email : ccRecepients){
        		   message.getCcRecipients().add(email);
        	   }
        	   
           } 
           
           if(bccRecepients!=null){
           	Reporter.log("## Recipients BCC :" + bccRecepients.getItems().toString() , true);
           	for ( EmailAddress email : ccRecepients){
           		message.getCcRecipients().add(email);
           	}
           		
           } 
           

//            if (attachments != null) {
//            	if(attachments.size()>=1){
//            		Reporter.log("Attachments:", true);
//                	for (String currAttachment : attachments) {
//                		Reporter.log(currAttachment, true);
//                		message.getAttachments().addFileAttachment(currAttachment);
//    				}
//                }
//            }
            if (save) {
                message.sendAndSaveCopy();
            } else {
                message.send();
            }
            
            return true;
            
        } catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			sendMailWithInLineAttachment( toRecepients,ccRecepients,bccRecepients, subject,  attachments,  save);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
       
    }

    public boolean deleteAllMailsWithSubject(String subject, String folderName,DeleteMode deleteMode) throws InterruptedException {
    	String response;
    	String deleteMailLog = "";

    	try {
    		@SuppressWarnings("unchecked")

    		WellKnownFolderName currFolder = null;
    		if(folderName.equals("Inbox")){
    			currFolder = WellKnownFolderName.Inbox;
    		}
    		else if(folderName.equals("Drafts")){
    			currFolder = WellKnownFolderName.Drafts;
    		}
    		else if(folderName.equals("SentItems")){
    			currFolder = WellKnownFolderName.SentItems;
    		}
    		else if(folderName.equals("DeletedItems")){
    			currFolder = WellKnownFolderName.DeletedItems;
    		}
    		//    		FindItemsResults<Item> fullResult;
    		FindItemsResults<Item> findResults;
    		ItemView view = new ItemView(50);
    		do {
    			view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Ascending);
    			view.setPropertySet(new PropertySet(BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.DateTimeReceived));
    			findResults
    			= service.findItems(currFolder,
    					new SearchFilter.SearchFilterCollection(
    							LogicalOperator.Or, new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject),
    							new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject)), view);
    			System.out.println("Total number of items found: " + findResults.getTotalCount());
    			
    			
    			if(findResults.getTotalCount()>0)
    			{
    				for(Item item : findResults){
    					String uniqueId = item.getId().getUniqueId();
    					deleteMailLog = "UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" Action : DELETE MAIL ## Subject :" + item.getSubject();
    					Reporter.log(deleteMailLog, true);
    					// Bind to an existing message using its unique identifier.
    					EmailMessage message = EmailMessage.bind(service, new ItemId(uniqueId));
    					message.delete(deleteMode);
    				}	
    			}

    			view.setOffset( view.getOffset()+50); 
    		} while (findResults.isMoreAvailable());
    		
    		return true;
    		
    	} catch (Exception ex) {
    		//incase of failure in sending mail, retry sending it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			deleteAllMailsWithSubject( subject,folderName,deleteMode);
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;
    }
    // Search Mail By Subject.....
    public FindItemsResults findItemAll(String subject, String folderName) throws ServiceLocalException, Exception {

    	try{
    		WellKnownFolderName currFolder = null;
    		if(folderName.equals("Inbox")){
    			currFolder = WellKnownFolderName.Inbox;
    		}
    		else if(folderName.equals("Drafts")){
    			currFolder = WellKnownFolderName.Drafts;
    		}
    		else if(folderName.equals("SentItems")){
    			currFolder = WellKnownFolderName.SentItems;
    		}
//    		FindItemsResults<Item> fullResult;
    		FindItemsResults<Item> findResults;
    		ItemView view = new ItemView(100);
    		 do {
    		view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Ascending);
    		view.setPropertySet(new PropertySet(BasePropertySet.IdOnly, ItemSchema.Subject, ItemSchema.DateTimeReceived));
    		findResults
    		= service.findItems(currFolder,
    				new SearchFilter.SearchFilterCollection(
    						LogicalOperator.Or, new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject),
    						new SearchFilter.ContainsSubstring(ItemSchema.Subject, subject)), view);
    		System.out.println("Total number of items found: " + findResults.getTotalCount());
    		
    		  view.setOffset( view.getOffset()+100); 
    		    } while (findResults.isMoreAvailable());
    		 
    		if(findResults.getTotalCount()>0){
    			return findResults;
    		}

    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
//    		currRetryCounter++;
//    		if (currRetryCounter <= maxRetryCount) {
//    			Reporter.log("--------------------------------------------------", true);
//    			Reporter.log("Issue in mail operation. Retrying attempt:" + currRetryCounter, true);
//    			Reporter.log("--------------------------------------------------", true);
//    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
//    			findItem(subject);
//    		}
    	}
//    	currRetryCounter = 0;// Reset Retry Counter...
    	return null;

    }
    
    /**
     * @return
     * @throws ServiceLocalException
     * @throws Exception
     */
    @SuppressWarnings("null")
	public boolean cleanupAccountMails() throws ServiceLocalException, Exception {
    	try{
    		String deleteMailLog = ""; 
    		WellKnownFolderName [] knownFolder = new  WellKnownFolderName[] {WellKnownFolderName.Inbox, WellKnownFolderName.Drafts, WellKnownFolderName.SentItems,  WellKnownFolderName.DeletedItems};
    		
    			for(WellKnownFolderName currFolder : knownFolder){
    				deleteMailLog=	"UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" User:"+userEmailId +" Action : emptying folder :" + currFolder.toString() ;
		    		FolderId folderId = new FolderId(currFolder);
		    		Folder folder = Folder.bind(service, folderId);
		    		folder.empty(DeleteMode.HardDelete, false);
		    		Reporter.log(deleteMailLog,true);
    			}
    			

//    			System.out.println("cleaned wellknown folders, going to Clutter");
    			deleteMailLog=	"UTC time:"+DateTime.now(DateTimeZone.UTC).toString()+" User:"+userEmailId +" Action : emptying folder : Clutter" ;
    			
    			Folder rootfolder = Folder.bind(service, WellKnownFolderName.MsgFolderRoot);
    			rootfolder.load();
    			Folder folder1= null;
    			
    			for (Folder folder : rootfolder.findFolders(new FolderView(10)))
    			{
    				if( folder.getDisplayName().equals("Clutter"))
    				{
//    					System.out.println("inside clutter"+folder.getId());

    					folder1 = Folder.bind(service, folder.getId());
    					folder1.empty(DeleteMode.HardDelete, false);
    					Reporter.log(deleteMailLog,true);
    				}
    			}
    			
	    		
    		return true;
    	} catch (Exception ex) {
    		//incase of failure in mail operation, retry  it 3 times
    		Reporter.log(ex.getLocalizedMessage(), true);        	
    		currRetryCounter++;
    		if (currRetryCounter <= maxRetryCount) {
    			Reporter.log("--------------------------------------------------", true);
    			Reporter.log("Issue emptying folder. Retrying attempt:" + currRetryCounter, true);
    			Reporter.log("--------------------------------------------------", true);
    			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
    			cleanupAccountMails();
    		}
    	}
    	currRetryCounter = 0;// Reset Retry Counter...
    	return false;

    }
       
    
       public List<String> getHref(String htmlContent) {
        List<String> urlRefList = new ArrayList();
        Document doc = Jsoup.parse(htmlContent);
        Elements links = doc.select("[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        for (Element src : media) {
            if (src.tagName().equals("img")) {
            } else {
            }
        }
        for (Element link : links) {
            urlRefList.add(link.attr("href"));
        }
        return urlRefList;
    }

    private static String trim(String s, int width) {
        if (s.length() > width) {
            return s.substring(0, width - 1) + ".";
        } else {
            return s;
        }
    }

    public EmailMessage getEmailMessage(String uniqueId) {
        try {
            // Bind to an existing message using its unique identifier.
            EmailMessage message = EmailMessage.bind(service, new ItemId(uniqueId));
            return message;
        } catch (Exception ex) {
            Logger.getLogger(Office365MailActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param findItem
     * @return
     */
    public String getDownloadHref(Item findItem){
            try {
                System.out.println("Subject :"+findItem.getSubject());
                ItemId id = findItem.getId();
                EmailMessage emailMessage = getEmailMessage(id.getUniqueId());
                String htmlContent=emailMessage.getBody().toString();
                List<String> href = getHref(htmlContent);
                List<String> printHref = getHref(htmlContent);
                for (String printHref1 : printHref) {
                    if(printHref1.contains("download")){
                        System.out.println("Links : "+printHref1);
                        return printHref1;
                    }
                }
                return null;
            } catch (ServiceLocalException ex) {
                Reporter.log("Service Local Exception :"+ex.getLocalizedMessage(),true);
            }
            return null;
    }
        public static void main(String[] args) throws Exception {
        
            Office365MailActivities office365MailActivities=new Office365MailActivities("admin@securletO365beatle.com", "MHikwjETdOgeFS!");
            // Get Download Reference URL..
            Item findItem = office365MailActivities.findItemInDecending("Log Export Request");
            String downloadHref = office365MailActivities.getDownloadHref(findItem);
            System.out.println("Download Url :"+downloadHref);
            
            //Get Attachements..
            Item findItem1 = office365MailActivities.findItemInDecending("Office365 Securlet Data Export");
            EmailMessage emailMessage = office365MailActivities.getEmailMessage(findItem1.getId().getUniqueId());
            AttachmentCollection attachments = emailMessage.getAttachments();
            for (Attachment attachment : attachments) {
                System.out.println("Attacment Name :"+attachment.getName());
                System.out.println("Attachment Size :"+attachment.getSize());
            }
            
        }
    
        
        public TreeMap<String, Long> getSelectedMailCount(String mailSubject, String draftSubject) throws ServiceLocalException, Exception {
        	try{
        		ArrayList<Long> result = new ArrayList<Long>();
        		ItemView view = new ItemView(50);
        		EmailMessage myItem = null;
        		long attachmentCount = 0;
        		long mailCount =0;
        		FindItemsResults<Item> findResults1 = null;
        		FindItemsResults<Item> findResults2 = null;
        		FindItemsResults<Item> findResults3 = null;
        		long myMailCount =0;
        		long othersMailCount =0;
        		long myMailAttachmentsCount =0;
        		long othersMailAttachmentCount =0;
        		String mySubject;
        		
        		long countInbox  = Folder.bind(service, WellKnownFolderName.Inbox).getTotalCount();
        		long countDraft  = Folder.bind(service, WellKnownFolderName.Drafts).getTotalCount();
        		long countSentItems  = Folder.bind(service, WellKnownFolderName.SentItems).getTotalCount();
        		long countDeletedItems = Folder.bind(service, WellKnownFolderName.DeletedItems).getTotalCount();
        		int tempCount =0;
        		
        		TreeMap<String, Long> results = new TreeMap<String, Long>();
        				
        		results.put("1.countInbox",countInbox );
        		results.put("2.countDraft",countDraft );
        		results.put("3.countSentItems",countSentItems );
        		results.put("4.countDeletedItems",countDeletedItems );
        		
        		WellKnownFolderName [] knownFolder = new  WellKnownFolderName[] {WellKnownFolderName.Inbox, WellKnownFolderName.Drafts, WellKnownFolderName.SentItems,  WellKnownFolderName.DeletedItems};

        		for(WellKnownFolderName currFolder : knownFolder){

        			mySubject = currFolder.equals(WellKnownFolderName.Drafts) ? draftSubject : mailSubject;


        			//stress mails with attachment
        			findResults1
        			= service.findItems(currFolder,//view);
        					new SearchFilter.SearchFilterCollection(
        							LogicalOperator.And, new SearchFilter.IsEqualTo(ItemSchema.Subject ,mySubject),
        							new SearchFilter.IsEqualTo(ItemSchema.HasAttachments, true)), view);
        			
        			results.put(currFolder.toString()+"_StressMailCount",(long) findResults1.getTotalCount() );

        			if(currFolder.equals(WellKnownFolderName.Inbox)){
        				//other mails with attachment
        				findResults2
        				= service.findItems(currFolder,//view);
        						new SearchFilter.SearchFilterCollection(
        								LogicalOperator.And, new SearchFilter.IsNotEqualTo(ItemSchema.Subject,mySubject ),
        								new SearchFilter.IsEqualTo(ItemSchema.HasAttachments, true)), view);

        				//other mails without attachment
        				findResults3
        				= service.findItems(currFolder,//view);
        						new SearchFilter.SearchFilterCollection(
        								LogicalOperator.And, new SearchFilter.IsNotEqualTo(ItemSchema.Subject,mySubject ),
        								new SearchFilter.IsEqualTo(ItemSchema.HasAttachments, false)), view);

        				results.put(currFolder.toString()+"_OthersMailCountWithAttachments",(long) findResults2.getTotalCount() );
        				results.put(currFolder.toString()+"_OthersMailCountWithoutAttachments",(long) findResults3.getTotalCount() );
        			}


        			

        		}
        		Reporter.log("--------------------------------------------------", true);
        		Reporter.log("User:"+userEmailId,true);
        		 Iterator it = results.entrySet().iterator();
        		    while (it.hasNext()) {
        		        Map.Entry pair = (Map.Entry)it.next();
        		        System.out.println(pair.getKey() + " = " + pair.getValue());
//        		       // it.remove(); // avoids a ConcurrentModificationException
        		    }
        		Reporter.log("--------------------------------------------------", true);
        		return results;
        		
        	} catch (Exception ex) {
        		//incase of failure in mail operation, retry  it 3 times
        		Reporter.log(ex.getLocalizedMessage(), true);        	
        		currRetryCounter++;
        		if (currRetryCounter <= maxRetryCount) {
        			Reporter.log("--------------------------------------------------", true);
        			Reporter.log("Issue search. Retrying attempt:" + currRetryCounter, true);
        			Reporter.log("--------------------------------------------------", true);
        			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
        			getMailAndAttachmentCount(mailSubject,draftSubject);
        		}
        	}
        	currRetryCounter = 0;// Reset Retry Counter...
        	return null;
        	
        }
        
    

    
}
