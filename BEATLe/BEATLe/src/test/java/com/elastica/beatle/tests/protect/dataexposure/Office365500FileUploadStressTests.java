package com.elastica.beatle.tests.protect.dataexposure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.UserRoleAssignment;

import junit.framework.Protectable;

/**
 * @author Mayur
 *
 */
public class Office365500FileUploadStressTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, File> fileCollection = new HashMap<String, File>();
	Map<String, Folder> folderCollection = new HashMap<String, Folder>();
	List<String> folderList = new ArrayList<String>();
	String filePath = ProtectTestConstants.PROTECT_RESOURCE_PATH + "policyfiletransfer.txt";
	

	/**
	 * 
	 * @throws Exception
	 */
	

	@Test(priority=1)
	public void test123() throws Exception{
		/*String[][] stringArray = getLevel();
		universalApi = protectFunctions.loginToApp(suiteData);
		restClient = new Client();
		protectFunctions.createFolderStructure(stringArray, universalApi, 25);*/
		//"qa-stress1@o365security.net", "Aut0mat10n#123"
		for(int i=1;i<=10000;i++){
			String[][] stringArray = getLevel();
			Reporter.log("Started file upload for *** Username: qa-stress"+i+"@o365security.net ----- Password: Aut0mat10n#123" , true);
			universalApi = protectFunctions.loginToApp(suiteData, "qa-stress"+i+"@o365security.net", "Aut0mat10n#123", "ENDUSER");
			protectFunctions.createFolderStructure(stringArray, universalApi, 25);
			Reporter.log("Completed ile upload for *** Username: qa-stress"+i+"@o365security.net ----- Password: Aut0mat10n#123" , true);
		}
	}
	
	public String[][] getLevel(){
		return new String[][]{
			/*
			 * 1. Folder Prefix
			 * 2. Number of folder levels
			 * 3. Number of sub folders in each folder
			 * 4. Exposure type of the file
			 * 5. Role
			 */
			new String[] {"A", "3", "2", "unexposed", "1"},
			new String[] {"B", "3", "2", "public", "1", },
			new String[] {"C", "2", "2", "external", "1"},
			new String[] {"D", "2", "2", "internal", "1"},
		};
	}
	
}
