package com.elastica.beatle.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.DataProvider;

/**
 *
 * @author rahulkumar
 */
public class AuditDataProvider {

    public static List<String> durationList=new ArrayList();
    public static List<String> rawParamList=new ArrayList();
    public static Map<String,String> rawParamMap=new HashMap();
    
    public static String dur_1day = "-1d";
    public static String dur_1week = "-7d";
    public static String dur_1mon = "-1mon";
    public static String dur_3mon = "-3mon";
    public static String dur_6mon = "-6mon";
    public static String dur_1yr = "-1y";

    public static List<String> getDurationList(){
        durationList.add(dur_1day);
        durationList.add(dur_1week);
        durationList.add(dur_1mon);
        durationList.add(dur_3mon);
        durationList.add(dur_6mon);
        durationList.add(dur_1yr);
        return durationList;
    }
    
    
    
    public static List<String> getRawParamList(){
        
        // Independent Selection...
        rawParamList.add("audit_details.device_id");
        rawParamList.add("audit_details.total_blocked_bytes");
        rawParamList.add("audit_details.browser_id");
        rawParamList.add("service_info.service_category");
        rawParamList.add("audit_details.date_time");
        rawParamList.add("audit_details.location_id");
        rawParamList.add("audit_details.downloaded_bytes");
        rawParamList.add("audit_details.session_duration");
        rawParamList.add("audit_details.hardware_id");
        rawParamList.add("risk.risk");
        rawParamList.add("service_info.service_id");
        rawParamList.add("audit_details.sessions");
        rawParamList.add("risk.service_tag_ids");
        rawParamList.add("audit_details.total_bytes");
        rawParamList.add("audit_details.total_packets");
        rawParamList.add("audit_details.up_bytes");
        rawParamList.add("audit_details.user_id");
        rawParamList.add("risk.brr");
        
        
        // Multiple Combination Selection
        rawParamList.add("risk.brr,audit_details.total_bytes,audit_details.user_id,audit_details.sessions");
        rawParamList.add("risk.brr,audit_details.total_bytes,audit_details.user_id,audit_details.session_duration");
        rawParamList.add("risk.brr,audit_details.user_id,audit_details.sessions");
        rawParamList.add("risk.brr,audit_details.total_bytes,audit_details.user_id,audit_details.sessions");
        rawParamList.add("audit_details.total_bytes,audit_details.user_id,audit_details.sessions");
        
        // Select All...
        rawParamList.add("audit_details.device_id,audit_details.total_blocked_bytes,audit_details.browser_id,service_info.service_category,"
                + "audit_details.date_time,audit_details.location_id,"
                + "audit_details.downloaded_bytes,audit_details.session_duration,audit_details.hardware_id,risk.risk,service_info.service_id,"
                + "audit_details.sessions,risk.service_tag_ids,audit_details.total_bytes,audit_details.total_packets,"
                + "audit_details.up_bytes,audit_details.user_id,risk.brr"); // All Raw Params
        
        return durationList;
    }
    
    
    public static Map<String,String> getRawParamMap(){
        rawParamMap.put("audit_details.device_id","DeviceId");
        rawParamMap.put("audit_details.total_blocked_bytes","TotalBlockedBytes");
        rawParamMap.put("audit_details.browser_id","BrowserId");
        rawParamMap.put("service_info.service_category","ServiceCategory");
        rawParamMap.put("audit_details.date_time","DateTime");
        rawParamMap.put("audit_details.location_id","LocationId");
        rawParamMap.put("audit_details.downloaded_bytes","DownloadedBytes");
        rawParamMap.put("audit_details.session_duration","SessionDuration");
        rawParamMap.put("audit_details.hardware_id","HardwareId");
        rawParamMap.put("risk.risk","Risk");
        rawParamMap.put("service_info.service_id","ServiceId");
        rawParamMap.put("audit_details.sessions","Sessions");
        rawParamMap.put("risk.service_tag_ids","ServiceTagId");
        rawParamMap.put("audit_details.total_bytes","TotalBytes");
        rawParamMap.put("audit_details.total_packets","TotalPackets");
        rawParamMap.put("audit_details.up_bytes","UpBytes");
        rawParamMap.put("audit_details.user_id","UserId");
        rawParamMap.put("risk.brr","BRR");
        return rawParamMap;
    }
    
    public static List<String> getIgnoredKeyList(){
        
        List<String> ignoredKeyList=new ArrayList<>();
        ignoredKeyList.add("BRR");
        ignoredKeyList.add("DateTime");
        ignoredKeyList.add("Risk");
        return ignoredKeyList;
    }
    
    public static List<String> getParamColumns(List<String> params){ 
        if(rawParamMap.isEmpty()){
           getRawParamMap();  
        }
        List<String> paramColumns=new ArrayList();
        for (String param : params) {
            paramColumns.add(rawParamMap.get(param.trim()));
        }
        return paramColumns;
    }
    
    @DataProvider(parallel = true ,name = "rawParametersRegression")
    public static Object[][] rawParametersRegression() {
        List<List> listOfArrayList=new ArrayList<>();
        
        getDurationList();
        getRawParamList();
        getRawParamMap();
        
        List<String> dataSourceParamList=new ArrayList<>();
        dataSourceParamList.addAll(rawParamList);
        dataSourceParamList.addAll(rawParamList);
        dataSourceParamList.addAll(rawParamList);
        dataSourceParamList.addAll(rawParamList);
        dataSourceParamList.addAll(rawParamList);
        dataSourceParamList.addAll(rawParamList);
        List<String> dataSourcedurationList=new ArrayList<>();
        for (String duration : durationList) {
            for(int i=0;i<rawParamList.size();i++){
                dataSourcedurationList.add(duration);
            }   
        }   
        listOfArrayList.add(dataSourceParamList);
        listOfArrayList.add(dataSourcedurationList);  
        return ReportingUtils.arrayListToDataProvider(listOfArrayList);    
    }
    
    // File type exposure total For Sanity ....
    @DataProvider(name = "rawParameters")
    public static Object[][] rawParameters() {
        return new Object[][]{
            {"service_info.service_id", dur_1day},
            {"service_info.service_id", dur_1week},
            {"service_info.service_id", dur_1mon},
            {"service_info.service_id", dur_3mon},
            {"service_info.service_id", dur_6mon},
            {"service_info.service_id", dur_1yr}
        };
    }
    
    // firewallType,
    @DataProvider(parallel = true ,name = "createDataSource")
    public static Object[][] createDataSource() {
        return new Object[][]{
            {"be_zscalar"},
            {"be_bluecoat_proxy"}
        };  
    }
    
}
