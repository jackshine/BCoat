/**
 * 
 */
package co.elastica.EMailHandler;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.parsers.ParserConfigurationException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.xml.sax.SAXException;

import co.elastica.Constants.Constants;
import co.elastica.DataHelper.DataReader;
import co.elastica.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class MailHandler {

	private EmailUtils emailUtil;
	DataReader dataReader;
	
	/**
	 * @param emailUtil
	 */
	public MailHandler() {
		super();
		emailUtil = new EmailUtils();
		dataReader = new DataReader();
	}



	/**
	 * @param content
	 * @param env
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public void sendEmail(String content, String env) throws AddressException, MessagingException, SAXException, IOException, ParserConfigurationException {

		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", Constants.EMIL_HOST);
		props.put("mail.smtp.user", Constants.EMAIL_FROM);
		props.put("mail.smtp.password", Constants.GMAIL_PASSWORD);
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props, null);		
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(Constants.EMAIL_FROM));		
		List<InternetAddress> toList = null;
		if("owner".equalsIgnoreCase(env)){
			toList = emailUtil.getToMailingList(dataReader.getOwnersEmailListFilePath(),env);
			message.setSubject("JOB NOT COMPLETED/NOT AVAILABLE");
		}			
		else{
			toList = emailUtil.getToMailingList(dataReader.getEmailListFilePath(env),env);
			message.setSubject(emailUtil.getEmailSubject(env));
		}			
		if(toList != null){
			for(InternetAddress address : toList)
				message.addRecipient(Message.RecipientType.TO, address);			
							
			message.setContent(content, "text/html");
			Transport transport = session.getTransport("smtps");
			transport.connect(Constants.EMIL_HOST, Constants.EMAIL_FROM, Constants.GMAIL_PASSWORD);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			Logger.info("Report Email Sent");
		}		
		else{
			Logger.info("To list is empty. Please Check the configurations");
		}
	}		
}