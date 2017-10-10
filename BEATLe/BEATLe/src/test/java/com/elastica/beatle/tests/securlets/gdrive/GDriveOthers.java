package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.i18n.I18N;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 *
 * @author rahulkumar
 */
public class GDriveOthers extends GDriveUtils {

    List<String> generatedMailID;
    String folderName = "GDriveSharingWithUpperCase_" + uniqueIdentifier;
    List<String> fileNameList = new ArrayList();
    String from;
    String jsonResponse;
    List<List> listUpperCaseTest = new ArrayList<>();
    List<List> listLanguageSupportTest = new ArrayList<>();
    List<String> multipleLanguage = getStringInMultipleLanguage();
    
    @Test(groups = {"UPPERUSECASE", "P1"})
    public void fileSharingActivitiesWithUppercaseMailID() throws Exception {
        this.generatedMailID = generateString(gDriveExternalUser.getEmailId());
        from = getCurrentJodaTime();
        wait(10, "Wait before before Create...");
        String folderId = createFolder(folderName, gDriveAdmin);
        for (int i = 0; i < multipleLanguage.size(); i++) {
            String fileName;
            String userMailId;
            if (i < generatedMailID.size()) {
                userMailId=generatedMailID.get(i);
                fileName = multipleLanguage.get(i).split(":")[0]+"-"+uniqueIdentifier+"-"+multipleLanguage.get(i).split(":")[1]+"-"+userMailId;
            } else {
                userMailId=gDriveExternalUser.getEmailId();
                fileName = multipleLanguage.get(i).split(":")[0]+"-"+uniqueIdentifier+"-"+multipleLanguage.get(i).split(":")[1]+"-"+userMailId;
            }
            fileNameList.add(fileName);
            wait(5, "Uploading file to a folder :" + folderName);
            String fileId = uploadFileInToFolderWithMultipleAttempt(fileName, localFileLocation, folderId, gDriveAdmin);
            wait(5, "Sharing file with  :" + userMailId + " user , writer ");
            gDriveAdmin.insertPermission(gDriveAdmin.getDriveService(), fileId, userMailId, "user", "writer");
        }
        listLanguageSupportTest.add(multipleLanguage);
        listLanguageSupportTest.add(this.fileNameList);
        listUpperCaseTest.add(this.generatedMailID);
        listUpperCaseTest.add(this.fileNameList);
        wait(globalWaitTime*3,"waiting after file Operations for i18n and Uppcase Use cases");
        this.jsonResponse = getDisplayLogResponse("Waiting for Logs :", from);
    }

    @DataProvider(name = "data-provider-UpperCaseTest")
    public Object[][] dataProviderUserIdsWithUpperCases() {
        return arrayListToDataProvider(listUpperCaseTest);
    }

    @DataProvider(name = "data-provider-LanguageSupport")
    public Object[][] dataProviderforLanguageSupport() {
        return arrayListToDataProvider(listLanguageSupportTest);
    }
    
    @Test(groups = {"UPPERUSECASE", "P1"}, dataProvider = "data-provider-UpperCaseTest")
    public void validateUserIdsWithUpperCasesSharingActivities(String mailId, String fileName) throws InterruptedException {
        Reporter.log("User Mail Id :" + mailId, true);
        Reporter.log("File Name :" + fileName, true);
        Map<String, Object> expectedResult = new HashMap();
        expectedResult.put("message", "User shared " + fileName);
        expectedResult.put("SharedWith", gDriveExternalUser.getEmailId());
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }

    @Test(groups = {"UPPERUSECASE", "P1"}, dataProvider = "data-provider-LanguageSupport")
    public void validateFileNamesWithDiffLanguageInShareActivity(String language, String fileName) throws InterruptedException {
        Reporter.log("Language :" + language, true);
        Reporter.log("File Name :" + fileName, true);
        Map<String, Object> expectedResult = new HashMap();
        expectedResult.put("message", "User shared " + fileName);
        expectedResult.put("SharedWith", gDriveExternalUser.getEmailId());
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }
    
    @Test(groups = {"UPPERUSECASE", "P1"}, dataProvider = "data-provider-LanguageSupport")
    public void validateFileNamesWithDiffLanguageInUploadActivity(String language, String fileName) throws InterruptedException {
        Reporter.log("Language :" + language, true);
        Reporter.log("File Name :" + fileName, true);
        Map<String, Object> expectedResult = new HashMap();
        expectedResult.put("message", "User uploaded file " + fileName);
        RawJsonParser.LogValidator(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }
    
    @Test(groups = {"UPPERUSECASE", "P1"}, dataProvider = "data-provider-LanguageSupport")
    public void validateFileNamesWithDiffLanguageInCIActivity(String language, String fileName) throws InterruptedException, Exception {
        Reporter.log("Language :" + language, true);
        Reporter.log("File Name :" + fileName, true);
        Map<String, Object> expectedResult = new HashMap();
        expectedResult.put("message", "File "+fileName+" has risk(s)");
        RawJsonParser.LogValidatorPartialCheck(this.jsonResponse, expectedResult, "$.hits.hits[*].source", "message");
    }
    
    
    public void folderSharingActivitiesWithUppercaseMailID() {

    }

    public static void main(String[] args) {
        String name = "rahul";

        Locale locale;
        String string = I18N.getString(name, "hi_in");
        System.out.println("==>" + string);

    }

}
