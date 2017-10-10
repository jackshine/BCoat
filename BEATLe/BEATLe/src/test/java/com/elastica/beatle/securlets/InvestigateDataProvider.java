package com.elastica.beatle.securlets;

import java.util.ArrayList;

import org.testng.annotations.DataProvider;

import com.elastica.beatle.DateUtils;

public class InvestigateDataProvider {
	
	@DataProvider(name = "ExportFormat")
    public static Object[][] ExportFormat()
    {	
        return new Object[][] { 
        						
		{ "cef" }, { "csv" }, { "leef" }
		
        };
    }
	
    
    @DataProvider(name = "ActivityTypeFilter")
    public static Object[][] ActivityTypeFilter() {
    	return new Object[][] { 
    		
    		{"Download"}, {"Delete"}, {"Upload"}, {"Share"}, {"Unshare"}, {"Login"}, {"Change Collaboration Role"}, 
    		{"Rename"}, {"Edit"}, {"Undelete"}, {"Transfer Ownership"}, {"Move"}, {"Copy"}, {"Unsync"}, {"Add"}, {"Create"},
    		{"Delete"}, {"Lock"}, {"Unlock"}, {"Remove"}, {"Accept Collaboration"}, {"Content Inspection"}, {"Expire Sharing"}
    		
    	};
    }
    
    @DataProvider(name = "ObjectTypeFilter")
    public static Object[][] ObjectTypeFilter() {
    	return new Object[][] { 
    		{"File"}, {"Folder"}, {"Session"}, {"Web_Link"}, {"Session"}, {"User"}, {"Group"}, {"Unknown Device"}
    	};
    }
    
   
    
    @DataProvider(name = "LocationFilter")
    public static Object[][] LocationFilter() {
    	return new Object[][] { 
    		{"informational", "Boardman (United States)"}, {"informational", "San Jose (United States)"}
    	};
    }
    
    @DataProvider(name = "AccountTypeFilter")
    public static Object[][] AccountTypeFilter() {
    	return new Object[][] { 
    		//With different offset and size
    		{"-1D", "0D", "Internal", 0, 500},
    		{"-1D", "0D", "Internal", 500, 500}, 
    		{"-1D", "0D", "Internal", 1000, 500}, 
    		{"-1D", "0D", "External", 0, 500},
    		{"-1D", "0D", "External", 500, 500},
    		{"-1D", "0D", "External", 1000, 500},
    		//missing or not available
    		{"-1D", "0D", "account_type", 0, 500},
    		{"-1D", "0D", "account_type", 500, 500},
    		{"-1D", "0D", "account_type", 1000, 500}
    	};
    }
    
    
    @DataProvider(name = "SeverityFilter")
    public static Object[][] SeverityFilter() {
    	return new Object[][] { 
    		{"-1D", "0D", "informational", 0, 500},
    		{"-1D", "0D", "informational", 500, 500},
    		{"-1D", "0D", "informational", 1000, 500},
    		{"-1D", "0D", "informational", 1500, 500},
    		{"-1D", "0D", "informational", 2000, 500},
    		{"-1D", "0D", "informational", 2500, 500},
    		{"-1D", "0D", "informational", 3000, 500},
    		{"-1D", "0D", "informational", 3500, 500},
    		{"-1D", "0D", "critical", 0, 500}, 
    		{"-1D", "0D", "warning", 0, 500},
    		{"-1D", "0D", "error", 0, 500},
    		{"-1D", "0D", "severity", 0, 500},
    	};
    }
    
    
    //Refined
    @DataProvider(name = "ServiceFilter")
    public static Object[][] ServiceFilter() {
    	return new Object[][] { 
    		{"-1D", "0D", "Salesforce"},
    		{"-1D", "0D", "Box"},
    		{"-1D", "0D", "Google Drive"},
    		{"-1D", "0D", "Gmail"},
    		{"-1D", "0D", "Dropbox"},
    		{"-1D", "0D", "ServiceNow"},
    		{"-2D", "1D", "Amazon Web Services"}
    	};
    }
    
    
    
    @DataProvider(name = "ExportFilter")
    public static Object[][] ExportFilter() {
    	return new Object[][] { 
    		{DateUtils.getDaysFromCurrentTime(-30), DateUtils.getDaysFromCurrentTime(0), "Elastica", "Box", "csv"},
    		{DateUtils.getDaysFromCurrentTime(-7), DateUtils.getDaysFromCurrentTime(-5), "Elastica", "Box", "csv"},
    		{DateUtils.getDaysFromCurrentTime(-7), DateUtils.getDaysFromCurrentTime(-6), "Elastica", "Box", "csv"},
    		{DateUtils.getDaysFromCurrentTime(-1), DateUtils.getDaysFromCurrentTime(0), "Elastica", "Box", "csv"},
    		{DateUtils.getDaysFromCurrentTime(-7), DateUtils.getDaysFromCurrentTime(-2), "Elastica", "Salesforce", "csv"},
    		{DateUtils.getDaysFromCurrentTime(-23), DateUtils.getDaysFromCurrentTime(-20), "Elastica", "Salesforce", "leef"},
    		{DateUtils.getDaysFromCurrentTime(-20), DateUtils.getDaysFromCurrentTime(-18), "Elastica", "Salesforce", "cef"},

    	};
    }
    
    @DataProvider(name = "InstanceFilter")
    public static Object[][] InstanceFilter() {
    	return new Object[][] { 
    		{"-7D", "0D"},
    	};
    }
    
    @DataProvider(name = "UserFilter")
    public static Object[][] UserFilter() {
    	return new Object[][] { 
    		{"-1D", "0D"},
    	};
    }
    
    @DataProvider(name = "AccountType")
    public static Object[][] AccountType() {
    	return new Object[][] { 
    		{"-60M", "0D"},
    	};
    }
    
    @DataProvider(name = "DashboardFilter")
    public static Object[][] DashboardFilter() {
    	return new Object[][] { 
    		{"-1D", "0D", "Object_type"},
    		{"-1D", "0D", "Activity_type"},
    		{"-1D", "0D", "severity"},
    		{"-1D", "0D", "location"},
    		{"-1D", "0D", "browser"},
    		{"-1D", "0D", "device"},
    		{"-1D", "0D", "device_mgmt"},
    		{"-1D", "0D", "device_owner"},
    		{"-1D", "0D", "device_owner"},
    		{"-1D", "0D", "device_compliance"},
    		{"-1D", "0D", "__source"},
    	};
    }
    
   
    
}
