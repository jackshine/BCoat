/**
 * 
 */
package com.elastica.beatle.fileHandler;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.Test;

/**
 * @author anuvrath
 *
 */
public class CreateBoxUserPassword {

	static int doneCount=0;
	static int failCount = 0;
	
	@Test
	public void initTest(){
		CreateBoxUserPassword gmail = new CreateBoxUserPassword();
		gmail.read();
		System.out.println("doneCount: "+doneCount);
		System.out.println("failCount: "+failCount);
	}

	public void read() {
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.user", "qa-admin@elasticaqa.org");
			props.put("mail.smtp.password", "pwRYwm)nwHbs0LA");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.auth", "true");			
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com", "qa-admin@elasticaqa.org","pwRYwm)nwHbs0LA");
			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);
			int messageCount = inbox.getMessageCount();
						
			System.out.println("Total Messages:- " + messageCount);						
			
			Message[] messages = inbox.getMessages();
			System.out.println("------------------------------");
			for (int i = messageCount-1; i >0; i--) {
				System.out.println("Message No:"+ messages[i].getMessageNumber());
				System.out.println("Received at: "+messages[i].getReceivedDate());
				if(messages[i].getSubject().contains("Enterprise for Steve Jobs created a new Box account for you")){
					Multipart multipart = (Multipart) messages[i].getContent();
					String content;

				    for (int j = 0; j < multipart.getCount(); j++) {
				        BodyPart bodyPart = multipart.getBodyPart(j);
				        String disposition = bodyPart.getDisposition();
				          if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { // BodyPart.ATTACHMENT doesn't work for gmail
				              System.out.println("Mail have some attachment");
				              DataHandler handler = bodyPart.getDataHandler();
				              System.out.println("file name : " + handler.getName());                                 
				          }
				          else { 
				              content= bodyPart.getContent().toString();
				              String url =content.split("Create Password \\[")[1].split("]")[0]; 
				              System.out.println("Box URL to set the password: "+ url);
				              boolean status = setThePassword(url);
				              if(status)
				            	  doneCount++;
				              else
				            	  failCount++;
				              System.out.println("Status: "+status);
				              break;
				         }
				    }
				}
			}				
			inbox.close(true);
			store.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param url
	 * @throws InterruptedException 
	 */
	private boolean setThePassword(String url) {

		WebDriver driver = null;
		try{	
			driver = new HtmlUnitDriver();
			driver.get(url);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			WebElement element1 = driver.findElement(By.id("new_password"));
			element1.sendKeys("Elastica@123");

			WebElement element2 = driver.findElement(By.id("confirm_password"));
			element2.sendKeys("Elastica@123");

			WebElement element3 = driver.findElement(By.id("continue"));
			element3.submit();			
			return true;
		}catch(Exception e){
			return false;
		}
		finally{
			driver.quit();
		}
	}
}