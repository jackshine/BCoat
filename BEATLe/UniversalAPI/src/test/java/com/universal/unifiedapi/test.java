package com.universal.unifiedapi;

import org.testng.annotations.Test;

import com.universal.common.UniversalApi;
import com.universal.common.Yammer;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.FileUploadResponse;

public class test {
	UniversalApi universalApi;
	
	
	@Test
	public void test1() {
		try{
			
			//UserAccount ua = new UserAccount("qa-admin@elasticaqa.net", "Cloudsoc@123", "ADMIN");
			UserAccount ua = new UserAccount("qa-admin@elasticaqa.com", "Elastica@123", "ADMIN");
			universalApi = new UniversalApi("BOX", ua);
			
			//BoxFolder bf = universalApi.createFolder("Folder Gatelet");
			//System.out.println("Folder id:" + bf.getId());
			FileUploadResponse uploadResponse = universalApi.uploadFile("/", "Box_HIPAA_Test2.txt", "Box Securlets gated.txt");
			System.out.println(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());
			
			
			//update the file
			universalApi.updateFile(uploadResponse.getFileId(), "PCI_test.txt", uploadResponse.getEtag(), "Box_HIPAA_Test2.txt");
			
			//Thread.sleep(20000);
			
			//universalApi.previewFile(uploadResponse.getFileId(), "securlets.png", "256", "256");
			
			//BoxUserInfo boxUserInfo = (BoxUserInfo) universalApi.listRootDrive("BOX", "ADMIN_USER", "/");
			//System.out.println(boxUserInfo.getName() + "==" + boxUserInfo.getLogin());
//			
//			String randomId = UUID.randomUUID().toString();
//			String sourceFile = "Box_HIPAA_Test2.txt";
//			String destinationFile = randomId + "_" + sourceFile;
//			
//			FileUploadResponse uploadResponse = universalApi.uploadFile("/", "Box_HIPAA_Test2.txt", destinationFile);
//			System.out.println(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());
//			
//			
//			universalApi.downloadFile(uploadResponse.getFileId(), destinationFile);
			//universalApi = new UniversalApi("DROPBOX", ua);
			
			//uploadResponse = universalApi.uploadFile("/", "/tmp/Hello.java");
			//System.out.println(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());
			
			//Entry entry = universalApi.getFileInfo("33463583017");
			
			//System.out.println("File info:" + entry.getId() + ": Etag:"+entry.getEtag());
			
			//non-existing file
			//universalApi.getFileInfo("12324232");
//			
//			BoxFolder boxfolder = universalApi.getFolderInfo(null);
//			System.out.println("Folder info:" + boxfolder.getName());
//			
//			ItemCollection items = universalApi.getFoldersItems("0", 0, 2);
//			System.out.println("Count:" + items.getTotalCount());
//			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		try{
			
			Yammer yam =  new Yammer();
			//yam.regenerateRefreshTokenUsingCodeFlow();
			
			yam.regenerateRefreshTokenUsingClientFlow("qa-stress15@o365security.net","Aut0mat10n#123");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
