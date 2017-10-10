package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.tests.securlets.*;
import com.universal.common.GExcelDataProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.testng.annotations.DataProvider;

/**
 *
 * @author rahulkumar
 */
public class GDriveDataProvider {

    private static GExcelDataProvider dataProvider;
    public static String uniqueIdentifier = UUID.randomUUID().toString();
    public static void initialize() {
        String excelId = "13n8EFQbFe_N8DyqzNbjy1hmxZ0Nh46ms-hs3ZVIW3GU";
        GDriveDataProvider.dataProvider = new GExcelDataProvider(excelId);
    }
    
    public static Map<String,String> getDriveCredentials(){
        Map<String,String> credentials=new HashMap<>();
        List<Map<String, Object>> dataAsMapList = dataProvider.getDataAsMapList("GdriveCredentials", "KEY", "VALUE");
        System.out.println("==>"+dataAsMapList.toString());
        for (Map<String, Object> dataAsMapList1 : dataAsMapList) {   
                credentials.put(dataAsMapList1.get("KEY").toString(), dataAsMapList1.get("VALUE").toString());
            }
        return credentials;
    }

    public static GExcelDataProvider getDataProvider() {
        return GDriveDataProvider.dataProvider;
    }

    //=========================== GDrive Export  <GDriveDashboradMetricsAndFilter> ================================================================= 
    @DataProvider(name = "data-provider-ExportCSVSecurlet")
    public static Object[][] dataProviderExportCSVSecurlet() {
        return dataProvider.getData("GDriveDashboradMetricsAndFilter", "ExportCSVSecurlet Test Name", "ExportCSVSecurlet Mail Search Query", "ExportCSVSecurlet Payload");
    }
    @DataProvider(name = "data-provider-ExportActivities")
    public static Object[][] dataProviderMethodExportActivities() {
        return dataProvider.getData("GDriveDashboradMetricsAndFilter", "Export Activity Test Description", "Export Activity");
    }
    @DataProvider(name = "data-provider-ExternalActivities")
    public static Object[][] dataProviderMethodExternalActivities() {
        dataProvider.updatedata("GDriveExternalUserActivity","UNIQUEPREFIX",GDriveDataProvider.uniqueIdentifier);
        return dataProvider.getData("GDriveExternalUserActivity", "Test Name", "Message");
    }
    
      //=============================== Dashboard Count <GDriveDashboradMetricsAndFilters> ===============================================
    @DataProvider(name = "data-provider-IncrementCount")
    public static Object[][] dataProviderIncrementCount() {
        return dataProvider.getData("GDriveDashboradMetricsAndFilter", "TestDescCountIncrement", "Drive Exposure");
    }  
    @DataProvider(name = "data-provider-DecrementCount")
    public static Object[][] dataProviderDecrementCount() {
        return dataProvider.getData("GDriveDashboradMetricsAndFilter", "Test Desc Count Decrement", "Drive Exposure");
    }
   
    //======================== Filters <GDriveDashboradMetricsAndFilter> =============================================================================
    @DataProvider(name = "ActivityTypeFilter")
    public static Object[][] ActivityTypeFilter() {
        return dataProvider.getData("GDriveDashboradMetricsAndFilter", "Activity Filter");
    }
    @DataProvider(name = "ObjectTypeFilter")
    public static Object[][] ObjectTypeFilter() {
        return dataProvider.getData("GDriveDashboradMetricsAndFilter", "Object Filter");
    }
    @DataProvider(name = "SeverityFilter")
    public static Object[][] SeverityFilter() {
        return dataProvider.getData("GDriveDashboradMetricsAndFilter", "Severity Filter");
    }
    @DataProvider(name = "LocationFilter")
    public static Object[][] LocationFilter() {
        return dataProvider.getData("GDriveDashboradMetricsAndFilter", "Location Severity Filter", "Location Filter");
    }

    // =============================== GDrive Folder Permissions <GDriveFolderPermission> ======================================================
    @DataProvider(name = "data-provider-FolderRemovePermission")
    public static Object[][] dataProviderMethodFolderRemovePermission() {
        return dataProvider.getData("GDriveFolderPermission", "Test Name Remove Folder Permission", "Folder Name");
    }
    @DataProvider(name = "data-provider-FolderUpdatePermission")
    public static Object[][] dataProviderMethodFolderUpdatePermission() {
        return dataProvider.getData("GDriveFolderPermission", "Test Name Update Folder Permission", "Folder Name");
    }
    @DataProvider(name = "data-provider-FolderInsertPermission")
    public static Object[][] dataProviderMethodFolderInsertPermission() {
        return dataProvider.getData("GDriveFolderPermission", "Test Name Insert Folder Permission", "Folder Name");
    }

    //=============================== File Permissions <GDrivePermissions> ===============================================
    @DataProvider(name = "data-provider-FileRemovePermission")
    public static Object[][] dataProviderMethodFileRemovePermission() {
        return dataProvider.getData("GDrivePermissions", "Test Name File Remove Permission", "File Name");
    }
    @DataProvider(name = "data-provider-FilePatchPermission")
    public static Object[][] dataProviderMethodFilePatchPermission() {
        return dataProvider.getData("GDrivePermissions", "Test Name File Patch Permission", "File Name");
    }
    @DataProvider(name = "data-provider-FileUpdatePermission")
    public static Object[][] dataProviderMethodFileUpdatePermission() {
        return dataProvider.getData("GDrivePermissions", "Test Name File Update Permission", "File Name");
    }
    @DataProvider(name = "data-provider-FileInsertPermission")
    public static Object[][] dataProviderMethodFileInsertPermission() {
        return dataProvider.getData("GDrivePermissions", "Test Name File Insert Permission", "File Name");
    }
    
    //=============================== File Permissions <GDriveInputPermission> ===============================================
    @DataProvider(name = "data-provider-ExposedFileInternalSwitch")
    public static Object[][] dataProviderExposedFileInternalSwitch() {
        return dataProvider.getData("GDriveInputPermission", "Test Description", "File Name","Expected Exposure");
    }
    
  
    
    
    // contentTypesExposuresTotal
    @DataProvider(name = "contentTypesExposuresTotal")
    public static Object[][] dataProviderForContentTypesExposuresTotal() {
        return new Object[][]{
            {"Expose a jpg  file and verify the  count", "Sample.jpg", "public", "image"}
        };
    }

    // File type exposure total
    @DataProvider(name = "fileTypesExposuresTotal")
    public static Object[][] dataProviderForFileTypesExposuresTotal() {
        return new Object[][]{
            //testname, 						folder, filename, 	shared access, 	term,  remedialRoles, expectedRole, Service to call
            {"Expose a java file and verify the filetype count", "/", "Hello.java", "open", "java", "APIServer"}
//    		{ "Expose a text file and verify the filetype count", "/", "BE.txt",     				"open", 	"txt", 		"APIServer" },
//    		{ "Expose a pdf file and verify the filetype count",  "/", "test.pdf",   				"open", 	"pdf", 		"APIServer" },
//    		{ "Expose a rtf file and verify the filetype count",  "/", "PII_TXT.rtf",				"open", 	"rtf", 		"APIServer" },
//    		{ "Expose a xlsx file and verify the filetype count", "/", "PII_PCI_EmployeeList.xlsx",	"open", 	"xlsx", 	"APIServer" },
//    		{ "Expose a xls  file and verify the filetype count", "/", "Sample.xls",				"open", 	"xls", 		"APIServer" },
//    		{ "Expose a jpg  file and verify the filetype count", "/", "Sample.jpg",				"open", 	"jpg", 		"APIServer" },
//    		{ "Expose a png  file and verify the filetype count", "/", "Sample.png",				"open", 	"png", 		"APIServer" },
//    		

        //	{ "Expose a text file and verify the filetype count", "/", "BE.txt",     "company", "internal", "APIServer" }
        };

    }

    @DataProvider(name = "vulnerabilityTypesExposuresTotal")
    public static Object[][] dataProviderForVulnerabilityTypesExposuresTotal() {
        return new Object[][]{
            //testname, 										folder, filename, 	shared access, 	term,  remedialRoles, expectedRole, Service to call
            {"Expose a source code file", "DisplayLogFields.java", "open", "source_code", "APIServer"},};
    }

    @DataProvider(name = "metricsExposuresTotal")
    public static Object[][] dataProviderForMetricsExposuresTotal() {
        return new Object[][]{
            //testname, 	rootfolder, filename, 		shared access, 	expectedchange,  remedialRoles, expectedRole, Service to call
            //    		{ "Test1", "/", "Hello.java", "open", "public", "APIServer" },
            {"Test1", "/", "BE.txt", "open", "public", "APIServer"}
        //{ "Test1", "/", "BE.txt",     "company", "internal", "APIServer" }
        };
    }

    
    //================================ PREVENT REMEDIATION =================================================================
   
    @DataProvider(name = "preventRemediation")
    public static Object[][] dataProviderForpreventRemediation() {
        dataProvider.updatedata("GDriveRemediation", "UNIQUEPREFIX", uniqueIdentifier);
        return dataProvider.getData("GDriveRemediation", "Test Name", "Restriction Type", "File Name", "Permission Role", "Permission Type", "Permission Value", "Additional Value/Comment","WebLink");
    }
    
    @DataProvider(name = "data-provider-FolderOperations")
    public static Object[][] dataProviderMethodFolderOperations() {
        return new Object[][]{
            {"Test Name:Validate Folder Create", "User created folder QATesting"},
            {"Test Name:validate Folder Permanent Delete", "User permanently deleted QATestingAfterRename from trash"},
            {"Test Name:validate Folder Rename", "User renamed folder from QATesting to QATestingAfterRename"},
            {"Test Name:validate Folder Restore", "User restored QATesting"}
        };
    }
    
    @DataProvider(name = "dataProviderRemediationValues")
    public static Object[][] dataProviderMethodRemediationAction() {
      return dataProvider.getData("GDriveRemediation", "Test Name Remediation", "Remediation Type", "Expected Permission", "Current Link");  
    }

    
 }
