package com.universal.unifiedapi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxWeblink;
import com.universal.dtos.box.Parent;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.box.WeblinkInput;

public class WeblinkTests {
	UniversalApi universalApi;


	@Test
	public void test1() {
		try{
			UserAccount ua = new UserAccount("qa-admin@elasticaqa.net", "Cloudsoc@123", "ADMIN");
			universalApi = new UniversalApi("BOX", ua);
			
			Parent parent = new Parent();
			parent.setId("0");
			
			WeblinkInput wlinput = new WeblinkInput();
			wlinput.setUrl("https://www.elastica.net");
			wlinput.setName("Elastica Home");
			wlinput.setDescription("Cloud security experts");
			wlinput.setParent(parent);
			BoxWeblink bwl = universalApi.createWeblink(wlinput);
			
			BoxFolder bfolder = universalApi.createFolder("Weblink Folder");
			universalApi.copyWeblink(bwl.getId(), bfolder.getId());
			
			
			//Share weblink
			SharedLink sharedLink = new SharedLink();
			sharedLink.setAccess("open"); //Allowed values open, company, collaborators, or null
			//System.out.println("####Request :" + marshall(sharedLink));
			universalApi.createSharedLinkForWeblink(bwl.getId(), sharedLink);
			
			Thread.sleep(10000);
			//Set the expiration 
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			sharedLink.setUnsharedAt(dateFormat.format(date));
			universalApi.updateSharedLinkForWeblink(bwl.getId(), sharedLink);
			
			Thread.sleep(10000);
			//disable the sharing
			universalApi.disableSharedLinkForWeblink(bwl.getId());
			
			
//			System.out.println("Box weblink:" + bwl.getId());
//			System.out.println("Box weblink name:" + bwl.getName());
//			Thread.sleep(10000);
//			//Update weblink name
//			bwl.setName("Elastica - The CloudSOC Platform");
//			bwl = universalApi.updateWeblink(bwl);
//			System.out.println("Box weblink:" + bwl.getId());
//			System.out.println("Box weblink name:" + bwl.getName());
//			
//			Thread.sleep(10000);
//			//Delete weblink
//			universalApi.deleteWeblink(bwl);
//			
//			Thread.sleep(10000);
//			//Restore item
//			universalApi.restoreWeblink(bwl);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
