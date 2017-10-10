package com.universal.unifiedapi;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.Result;

public class OneDriveCheck {
	UniversalApi universalApi;
	
	
	@Test
	public void listFiles() {
		try{
			
			for (int i=0; i < 6000 ; i+=100) {

				UserAccount ua = new UserAccount("admin@securletO365beatle.com", "MHikwjETdOgeFS!", "ADMIN", "eoe", null, "securletO365beatle");
				universalApi = new UniversalApi("ONEDRIVEBUSINESS", ua);

				Result result = universalApi.getSPDocumentList();

				ArrayList<HashMap<String, String>> items = null;
				try{
					items = universalApi.listAllItems(result.getId(), false);
				}

				finally {
					for (HashMap<String, String> hmap : items) {
						System.out.println("odataEditLink"+hmap.get("odataEditLink"));
						universalApi.deleteListItem(hmap.get("odataEditLink"));
					}
					System.out.println("All Items:"+items.size());
				}
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
