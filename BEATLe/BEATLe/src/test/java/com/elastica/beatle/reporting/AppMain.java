/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle.reporting;

import com.elastica.beatle.RawJsonParser;
//import static com.elastica.beatle.reporting.ReportingUtils.validateDashboardReport;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.universal.common.GExcelDataProvider;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.testng.Reporter;

/**
 *
 * @author rahulkumar
 */
public class AppMain {

    public static void main(String[] args) throws IOException {

        String duration = "-7d";

//        String rawParams = "audit_details.device_id,audit_details.total_blocked_bytes,audit_details.browser_id,service_info.service_category,"
//                + "audit_details.date_time,audit_details.location_id,"
//                + "audit_details.downloaded_bytes,audit_details.session_duration,audit_details.hardware_id,risk.risk,service_info.service_id,"
//                + "audit_details.sessions,risk.service_tag_ids,audit_details.total_bytes,audit_details.total_packets,"
//                + "audit_details.up_bytes,audit_details.user_id,risk.brr";
//        String rawParams = "audit_details.total_bytes";
//
//        List<Map<String, Object>> actualResultSet = new ArrayList<>();
//        String reportDataReportJsonResponse = "{\"count\": 2, \"all_count_label\": \"All\", \"errors\": [], \"all_count_supported\": false, \"fields\": {\"audit_details.date_time\": {\"display_text\": \"Date/Time\", \"description\": null, \"filtering_supported\": false, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"datetime\", \"meta_type\": \"epoch_milli_seconds\", \"aggregation_supported\": false, \"type\": \"datetime\", \"grouping_supported\": true, \"name\": \"audit_details.date_time\"}, \"service_info.service_id\": {\"display_text\": \"Service\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"choice_multi\", \"meta_type\": \"apparazzi_service_id\", \"aggregation_supported\": true, \"type\": \"text\", \"grouping_supported\": true, \"name\": \"service_info.service_id\"}, \"service_info.service_category\": {\"display_text\": \"Category\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"choice\", \"meta_type\": \"apparazzi_category\", \"aggregation_supported\": true, \"type\": \"text\", \"grouping_supported\": true, \"name\": \"service_info.service_category\"}, \"audit_details.user_id\": {\"display_text\": \"User\", \"description\": null, \"filtering_supported\": false, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"text\", \"aggregation_supported\": true, \"type\": \"text\", \"grouping_supported\": true, \"name\": \"audit_details.user_id\"}, \"audit_details.device_id\": {\"display_text\": \"User Device\", \"description\": null, \"filtering_supported\": false, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"text\", \"aggregation_supported\": true, \"type\": \"text\", \"grouping_supported\": true, \"name\": \"audit_details.device_id\"}, \"audit_details.hardware_id\": {\"display_text\": \"Network Device\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"text\", \"aggregation_supported\": true, \"type\": \"text\", \"grouping_supported\": true, \"name\": \"audit_details.hardware_id\"}, \"audit_details.location_id\": {\"display_text\": \"Destination\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"choice_multi\", \"meta_type\": \"audit_location_id\", \"aggregation_supported\": true, \"type\": \"text\", \"grouping_supported\": true, \"name\": \"audit_details.location_id\"}, \"audit_details.browser_id\": {\"display_text\": \"Browser\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"choice_multi\", \"meta_type\": \"audit_browser_id\", \"aggregation_supported\": true, \"type\": \"text\", \"grouping_supported\": true, \"name\": \"audit_details.browser_id\"}, \"audit_details.sessions\": {\"display_text\": \"Session Count\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"number\", \"aggregation_supported\": true, \"type\": \"number\", \"grouping_supported\": false, \"name\": \"audit_details.sessions\"}, \"audit_details.total_bytes\": {\"display_text\": \"Total Bytes\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"number\", \"meta_type\": \"bytes\", \"aggregation_supported\": true, \"type\": \"number\", \"grouping_supported\": false, \"name\": \"audit_details.total_bytes\"}, \"audit_details.total_packets\": {\"display_text\": \"Total Packets\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"number\", \"aggregation_supported\": true, \"type\": \"number\", \"grouping_supported\": false, \"name\": \"audit_details.total_packets\"}, \"audit_details.up_bytes\": {\"display_text\": \"Uploaded Bytes\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"number\", \"meta_type\": \"bytes\", \"aggregation_supported\": true, \"type\": \"number\", \"grouping_supported\": false, \"name\": \"audit_details.up_bytes\"}, \"audit_details.downloaded_bytes\": {\"display_text\": \"Downloaded Bytes\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"number\", \"meta_type\": \"bytes\", \"aggregation_supported\": true, \"type\": \"number\", \"grouping_supported\": false, \"name\": \"audit_details.downloaded_bytes\"}, \"audit_details.total_blocked_bytes\": {\"display_text\": \"Blocked Bytes\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"number\", \"meta_type\": \"bytes\", \"aggregation_supported\": true, \"type\": \"number\", \"grouping_supported\": false, \"name\": \"audit_details.total_blocked_bytes\"}, \"audit_details.session_duration\": {\"display_text\": \"Duration\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"number\", \"meta_type\": \"duration_seconds\", \"aggregation_supported\": true, \"type\": \"number\", \"grouping_supported\": false, \"name\": \"audit_details.session_duration\"}, \"risk.brr\": {\"display_text\": \"BRR\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {\"max\": 100, \"range_options\": {\"High\": {\"to\": 49, \"from\": 0}, \"Medium\": {\"to\": 79, \"from\": 50}, \"Low\": {\"to\": 100, \"from\": 80}}, \"min\": 0}, \"filter_type\": \"range\", \"meta_type\": \"brr\", \"aggregation_supported\": true, \"type\": \"number\", \"grouping_supported\": true, \"name\": \"risk.brr\"}, \"risk.risk\": {\"display_text\": \"Risk Score\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"number\", \"meta_type\": \"risk_score\", \"aggregation_supported\": true, \"type\": \"number\", \"grouping_supported\": true, \"name\": \"risk.risk\"}, \"risk.service_tag_ids\": {\"display_text\": \"Tags\", \"description\": null, \"filtering_supported\": true, \"only_for_filtering\": false, \"filter_config\": {}, \"filter_type\": \"tag_bitmap\", \"meta_type\": \"tag_ids_bitmap\", \"aggregation_supported\": false, \"type\": \"tags_array\", \"grouping_supported\": false, \"name\": \"risk.service_tag_ids\"}}, \"headers\": [\"audit_details.total_bytes\"], \"limit\": 100, \"total\": 2, \"data\": [[11800], [11800]], \"sample_data\": false}";
//        ReportingUtils.initExpectedData();
//        List<String> fields = Arrays.asList(rawParams.split(","));
//        Reporter.log("Raw Params : " + fields + "## Duration : " + duration, true);
//        List<Map<String, Object>> expectedResult = ReportingUtils.getExpectedResult(fields, duration);
//        System.out.println("===== Result Filtered As per date =====");
//        int i = 0;
//        for (Map<String, Object> expectedResult1 : expectedResult) {
//            System.out.println("========== Map Printing =============(" + ++i + ")");
//            RawJsonParser.printMap(expectedResult1);
//        }
//        System.out.println("##### =============== EXPECTED MAP LIST ================ #####");
//        List<Map<String, Object>> expectedMapList = ReportingUtils.getFilteredMap(expectedResult, AuditDataProvider.getParamColumns(Arrays.asList(rawParams.split(","))));
//        for (Map<String, Object> expectedResult1 : expectedMapList) {
//            System.out.println("========== Map Printing =============(" + ++i + ")");
//            RawJsonParser.printMap(expectedResult1);
//        }
//
//        System.out.println("##### =============== ACTUAL MAP LIST ================ #####");
//        List<Map<String, Object>> actualMapList = ReportingUtils.getMaplistFromJsonResponse(reportDataReportJsonResponse);
//        for (Map<String, Object> expectedResult1 : actualMapList) {
//            System.out.println("========== Map Printing =============(" + ++i + ")");
//            RawJsonParser.printMap(expectedResult1);
//        }
//
//        System.out.println("##### =============== VALIDATION ACTUAL RESULT================ #####");
//        for (Map<String, Object> actualMapList1 : actualMapList) {    
//            for (int y=0;y<expectedMapList.size();y++) {    
//                 MapDifference<String, Object> difference = Maps.difference(actualMapList1, expectedMapList.get(y));
//                 System.out.println("Equal : "+difference.areEqual());
//                 System.out.println("Difference :"+difference.toString());
//                 if(difference.areEqual()){
//                     expectedMapList.remove(y);
//                 }            
//            }
//        }


    }

    public static Map<String, Object> compareAndNullCheckInMap(Map<String, Object> expected, List<Map<String, Object>> listMap) {
        boolean found = true;
        Map<String, Object> result = new HashMap<>();
        Object[] toArray = expected.keySet().toArray();
        for (Map<String, Object> actualmap : listMap) {
            found = true;
            for (Map.Entry<String, Object> entrySet : expected.entrySet()) {
                if (found) {
                    String key = entrySet.getKey();
                    String value = entrySet.getValue().toString();
                    if (value != null) {
                        String get = actualmap.get(key).toString();
                        if (!get.equals(value)) {
                            found = false;
                        }
                    }
                }
            }
            if (found) {
                result = actualmap;
                return result;
            }
        }
        return result;
    }

}
