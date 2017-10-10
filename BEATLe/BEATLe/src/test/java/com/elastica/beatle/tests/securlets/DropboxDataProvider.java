/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle.tests.securlets;

import com.universal.common.GExcelDataProvider;
import java.util.UUID;
import org.testng.annotations.DataProvider;

/**
 *
 * @author rahulkumar
 */
public class DropboxDataProvider {

    private static GExcelDataProvider dataProvider;
    public static String uniqueIdentifier = UUID.randomUUID().toString();

    public static void initialize() {
        String excelId = "13n8EFQbFe_N8DyqzNbjy1hmxZ0Nh46ms-hs3ZVIW3GU";
        DropboxDataProvider.dataProvider = new GExcelDataProvider(excelId);
    }

    public static GExcelDataProvider getDataProvider() {
        return DropboxDataProvider.dataProvider;
    }

    @DataProvider(name = "data-provider-FilesAndFolderPermissionActivities")
    public static Object[][] dataProviderFilesAndFolderPermissionActivities() {
        DropboxDataProvider.dataProvider.updatedata("DropboxSharingActivities", "UNIQUEPREFIX", DropboxDataProvider.uniqueIdentifier);
        return DropboxDataProvider.dataProvider.getData("DropboxSharingActivities", "Test Name", "Message");
    }

    @DataProvider(name = "data-provider-FilesAndFolderActivities")
    public static Object[][] dataProviderFilesAndFolderActivities() {
        DropboxDataProvider.dataProvider.updatedata("DropboxFilesAndFolderActivities", "UNIQUEPREFIX", DropboxDataProvider.uniqueIdentifier);
        return DropboxDataProvider.dataProvider.getData("DropboxFilesAndFolderActivities", "Test Name", "Message");
    }

    @DataProvider(name = "data-provider-GroupActivities")
    public static Object[][] dataProviderGroupActivities() {
        DropboxDataProvider.dataProvider.updatedata("DropboxBusinessActivities", "UNIQUEPREFIX", DropboxDataProvider.uniqueIdentifier);
        return DropboxDataProvider.dataProvider.getData("DropboxBusinessActivities", "Test Name", "Message");
    }
    
    @DataProvider(name = "data-provider-ExportCSVSecurlet")
    public static Object[][] dataProviderExportCSVSecurlet() {
        return new Object[][]{
            {"Test Name:validate Export Exposed Files", "", "{\"source\":{\"limit\":20,\"offset\":0,\"isInternal\":true,\"searchTextFromTable\":\"\",\"orderBy\":\"name\",\"exportType\":\"docs\",\"app\":\"Dropbox\"}}"},
            {"Test Name:validate Export Exposed Users", "", "{\"source\":{\"limit\":100,\"offset\":0,\"isInternal\":true,\"searchTextFromTable\":\"\",\"exportType\":\"ext_collabs\",\"requestType\":\"collabs\",\"app\":\"Dropbox\"}}"},
            {"Test Name:validate Export Exposed Other Risks", "", "{\"source\":{\"limit\":20,\"offset\":0,\"isInternal\":true,\"requestType\":\"risky_docs\",\"searchTextFromTable\":\"\",\"orderBy\":\"name\",\"app\":\"Dropbox\",\"exportType\":\"risky_docs\"}}"},};
    }

    @DataProvider(name = "data-provider-ExportActivities")
    public static Object[][] dataProviderMethodExportActivities() {
        return new Object[][]{
            {"Test Name:validate CSV Export", "CSV"},
            {"Test Name:validate CEF Export", "CEF"},
            {"Test Name:validate LEEF Export", "LEEF"}
        };
    }

    @DataProvider(name = "SeverityFilter")
    public static Object[][] SeverityFilter() {
        return DropboxDataProvider.dataProvider.getData("DropboxDashboradMetricsAndFilters", "Severity Filter");
    }

    @DataProvider(name = "ActivityTypeFilter")
    public static Object[][] ActivityTypeFilter() {
        return DropboxDataProvider.dataProvider.getData("DropboxDashboradMetricsAndFilters", "Activity Filter");
    }

    @DataProvider(name = "ObjectTypeFilter")
    public static Object[][] ObjectTypeFilter() {
        return DropboxDataProvider.dataProvider.getData("DropboxDashboradMetricsAndFilters", "Object Filter");
    }

    @DataProvider(name = "LocationFilter")
    public static Object[][] LocationFilter() {
        return new Object[][]{
            {"informational", "Boardman (United States)"}
        };
    }
    
    @DataProvider(name = "CountIncrementInternalSwitch")
    public static Object[][] dashboardCountIncrementInternalSwitch() {
        return new Object[][]{
            {"Validate Total Exposed File INCREMENT - INTERNAL SWITCH", "totalExposedCount"},
            {"Validate External Exposed File INCREMENT - INTERNAL SWITCH", "externalExposedCount"},
            {"Validate Public Exposed File INCREMENT - INTERNAL SWITCH", "publicExposedCount"},
            {"Validate Internal Exposed File INCREMENT - INTERNAL SWITCH", "internalExposedCount"}        
        };
    }
    
    @DataProvider(name = "CountDecrementInternalSwitch")
    public static Object[][] dashboardCountDecrementInternalSwitch() {
        return new Object[][]{
            {"Validate Total Exposed File - DECREMENT - INTERNAL SWITCH", "totalExposedCount"},
            {"Validate External Exposed File - DECREMENT - INTERNAL SWITCH", "externalExposedCount"},
            {"Validate Public Exposed File - DECREMENT - INTERNAL SWITCH", "publicExposedCount"},
            {"Validate Internal Exposed File - DECREMENT - INTERNAL SWITCH", "internalExposedCount"}        
        };
    }
    
    @DataProvider(name = "CountIncrementExternalSwitch")
    public static Object[][] dashboardCountIncrementExternalSwitch() {
        return new Object[][]{
           // {"Validate Internal Exposed File INCREMENT - EXTERNAL SWITCH", "internalExposedCount"},
            {"Validate Total Exposed File INCREMENT - EXTERNAL SWITCH", "totalExposedCount"},
            {"Validate External Exposed File INCREMENT - EXTERNAL SWITCH", "externalExposedCount"},
            {"Validate Public Exposed File INCREMENT - EXTERNAL SWITCH", "publicExposedCount"}               
        };
    }
    
    @DataProvider(name = "CountDecrementExternalSwitch")
    public static Object[][] dashboardCountDecrementExternalSwitch() {
        return new Object[][]{
          //  {"Validate Internal Exposed File - DECREMENT - EXTERNAL SWITCH", "internalExposedCount"},        
            {"Validate Total Exposed File - DECREMENT - EXTERNAL SWITCH", "totalExposedCount"},
            {"Validate External Exposed File - DECREMENT - EXTERNAL SWITCH", "externalExposedCount"},
            {"Validate Public Exposed File - DECREMENT - EXTERNAL SWITCH", "publicExposedCount"}      
        };
    }
    
    @DataProvider(name = "ExposedFilesMetadataInternalSwitch")
    public static Object[][] ExposedFilesMetadataInternalSwitch() {
        
       return DropboxDataProvider.dataProvider.getData("DropboxDashboradMetricsAndFilters", 
               "Test Name ValidateFileExposure InternalSwitch","UploadedFileName","DocType",
               "Public Internal Switch","Internal Internal Switch","External Internal Switch");
    }
    
    
    

}
