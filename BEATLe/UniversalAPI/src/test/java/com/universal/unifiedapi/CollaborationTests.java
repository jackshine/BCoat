package com.universal.unifiedapi;

import org.testng.annotations.Test;

import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.GroupInput;
import com.universal.dtos.box.Item;

public class CollaborationTests {
	UniversalApi universalApi;


	@Test
	public void test1() {
		try{
			UserAccount ua = new UserAccount("qa-admin@elasticaqa.net", "Cloudsoc@123", "ADMIN");
			universalApi = new UniversalApi("BOX", ua);
			BoxFolder bfolder = universalApi.createFolder("Securtoboration");
			
			BoxFolder bfolder1 = universalApi.createFolder("Another Folder");
			
			
			Thread.sleep(30000);
			
			FileUploadResponse uploadResponse = universalApi.uploadFile(bfolder.getId(), "Box_HIPAA_Test2.txt", "Box Securlets.txt");
			System.out.println(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());
			
			Thread.sleep(30000);
			
			CollaborationInput ci = new CollaborationInput();
			Item item = new Item();
			item.setId(bfolder.getId());
			item.setType("folder");
			
			AccessibleBy aby = new AccessibleBy();
			//aby.setId("pushparaj.thangaraj@elastica.co");
			
			aby.setName("Pushparaj");
			aby.setType("user");
			aby.setLogin("pushpan@gmail.com");
			
			ci.setItem(item);
			ci.setAccessibleBy(aby);
			ci.setRole("editor");
			
			
			BoxCollaboration bc = universalApi.createCollaboration(ci);
			System.out.println("Collaboration Id:" + bc.getId());
			//editor, viewer, previewer, uploader, previewer uploader, viewer uploader, co-owner, or owner
			
			Thread.sleep(60000);
			
			bc.setExpiresAt("2015-07-31T13:44:45-07:00");
			universalApi.updateCollaboration(bc);
			
			Thread.sleep(60000);
			universalApi.deleteCollaboration(bc);
			
			
			
			//create a group
			GroupInput ginput = new GroupInput();
			
			ginput.setName("Elastica Automation Group");
			ginput.setProvenance("Elastica");
			ginput.setDescription("Bangalore Team");
			
			
			
			
			BoxGroup bgroup = universalApi.createGroup(ginput);
			
			ci = new CollaborationInput();
			item = new Item();
			item.setId(bfolder.getId());
			item.setType("folder");
			
			aby = new AccessibleBy();
			//aby.setId("pushparaj.thangaraj@elastica.co");
			
			aby.setId(bgroup.getId());
			aby.setType("group");
			
			ci.setItem(item);
			ci.setAccessibleBy(aby);
			ci.setRole("editor");
			
			bc = universalApi.createCollaboration(ci);
			
			Thread.sleep(70000);
			
			universalApi.deleteCollaboration(bc);
			
			universalApi.deleteGroup(bgroup);
			
//			
//			//bfolder.setName("Securlets Updated FolderName");
//			BoxFolder bf =universalApi.updateFolder(bfolder.getId(), "Securlets Updated FolderName");
//			
//			Thread.sleep(30000);
//			
//			//Move to different folder
//			bfolder.getParent().setId(bfolder1.getId());
//			
//			Thread.sleep(30000);
//			
			universalApi.deleteFolder(bfolder.getId(), true, bfolder.getEtag());
//			
//			Thread.sleep(30000);
//			
			universalApi.purgeFolder(bfolder.getId());
//			
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
