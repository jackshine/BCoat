/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.gateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsfr.json.JsonSurfer;
import org.testng.Reporter;

public class RawJsonParser {

    private static final JsonSurfer surfer = JsonSurfer.simple();
    static String NotEmpty = "NOT_EMPTY";// not null...
    static String NotZero = "NON_ZERO";// not null...
    
    
    public static void main(String[] args) throws IOException {
        
        String response = "{\"took\":2,\"timed_out\":false,\"_shards\":{\"total\":3,\"successful\":3,\"failed\":0},\"hits\":{\"total\":15,\"max_score\":null,\"hits\":[{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"COirwI70S5KvahN_EAraiA\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:52:49\",\"created_timestamp\":\"2016-01-19T12:52:49\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User changed permissions for the entire organization to &lt;can view&gt; for item \\\"empty_folder\\\"\",\"Activity_type\":\"Update Link Sharing\",\"Object_type\":\"File/Folder\",\"_ObjectName\":\"empty_folder\",\"status_code\":\"200\",\"_SharedWith\":\"__ALL_EL__\",\"_log_hash\":\"ChangeSharingPermissions#605513d7f9f99d14573559cfcb387d1e32c3052d1b855abdc9b813398894bbc2\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://drive.google.com/sharing/commonshare\",\"req_size\":\"2320\",\"web_path\":\"drive.google.com ---> drive.google.com\",\"resp_code\":\"200\",\"resp_size\":\"908\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/sharing/share?id=0B8MUwvaBzVdlbElvYkhaa0pmcUk&foreignService=explorer&gaiaService=wise&shareService=explorer&command=init_share&subapp=10&popupWindowsEnabled=true&shareUiType=default&hl=en-US&authuser=0&rand=1453207945998&preload=false\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207969000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"Qdc4bhllTUOV5rHWQfLTTw\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:51:23\",\"created_timestamp\":\"2016-01-19T12:51:23\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User moved item \\\"empty_folder\\\" from \\\"new\\\" to folder \\\"root\\\"\",\"Activity_type\":\"Move\",\"Object_type\":\"File/Folder\",\"_ObjectName\":\"empty_folder\",\"status_code\":\"200\",\"_log_hash\":\"GoogleDriveMoveNewUI#fa12d4a548438054d829c85b48973496a981048af50acd1866d6ce14ddb64f1c\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://clients6.google.com/batch/drive\",\"req_size\":\"2869\",\"web_path\":\"drive.google.com ---> clients6.google.com\",\"resp_code\":\"200\",\"resp_size\":\"1433\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/drive/folders/0B8MUwvaBzVdlNDg5Y25uT0xIVlU\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207883000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"7bGR1dvARKOdnKAxi7lRkg\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:51:20\",\"created_timestamp\":\"2016-01-19T12:51:20\",\"message\":\"User browsed folder named \\\"new\\\"\",\"severity\":\"informational\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"_ObjectName\":\"new\",\"Activity_type\":\"View\",\"Object_type\":\"Folder\",\"_log_hash\":\"GoogleDriveFolderViewNewUI#333245ed3e5c5a445650508fa9e754ad611a457267edaae1143cd179620aa5be\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://clients6.google.com/batch/drive\",\"req_size\":\"2852\",\"web_path\":\"drive.google.com ---> clients6.google.com\",\"resp_code\":\"200\",\"resp_size\":\"2301\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/drive/folders/0B8MUwvaBzVdlNDg5Y25uT0xIVlU\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207880000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"UwQ0J4CfReqIbP6QNJKLOw\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:50:48\",\"created_timestamp\":\"2016-01-19T12:50:48\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User moved item \\\"empty_folder\\\" from \\\"root\\\" to folder \\\"new\\\"\",\"Activity_type\":\"Move\",\"Object_type\":\"File/Folder\",\"_ObjectName\":\"empty_folder\",\"status_code\":\"200\",\"_log_hash\":\"GoogleDriveMoveNewUI#fa12d4a548438054d829c85b48973496a981048af50acd1866d6ce14ddb64f1c\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://clients6.google.com/batch/drive\",\"req_size\":\"2841\",\"web_path\":\"drive.google.com ---> clients6.google.com\",\"resp_code\":\"200\",\"resp_size\":\"1437\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/drive/my-drive\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207848000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"vinXFGURQ26FWLNiIKHo7w\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:50:21\",\"created_timestamp\":\"2016-01-19T12:50:21\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User created folder named \\\"empty_folder\\\" in folder \\\"root\\\"\",\"Activity_type\":\"Create\",\"Object_type\":\"Folder\",\"_ObjectName\":\"empty_folder\",\"status_code\":\"200\",\"_log_hash\":\"EPDASSIST_CreateFolder_0#5224ecc6a764d3ffa215b14fbd6b37664dad9cdd8869e34084f4ae26a62cc8b5\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://clients6.google.com/batch/drive\",\"req_size\":\"2855\",\"web_path\":\"drive.google.com ---> clients6.google.com\",\"resp_code\":\"200\",\"resp_size\":\"2312\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/drive/my-drive\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207821000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"sTVaidghQB6x8K_y2B9UYw\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:49:58\",\"created_timestamp\":\"2016-01-19T12:49:58\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User deleted item(s) \\\"empty_folder\\\"\",\"Activity_type\":\"Delete\",\"Object_type\":\"File/Folder\",\"_ObjectName\":\"empty_folder\",\"status_code\":\"200\",\"_log_hash\":\"GoogleDriveSendItemToTrashNewUI#5dd25f672b00f6335c4bdde8cffe9bcf6e402798ab2a3a878a5f9289bb55751f\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://clients6.google.com/batch/drive\",\"req_size\":\"2792\",\"web_path\":\"drive.google.com ---> clients6.google.com\",\"resp_code\":\"200\",\"resp_size\":\"1443\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/drive/my-drive\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207798000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"_ClNqmWPTuCQbRx7cMMa5w\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:49:21\",\"created_timestamp\":\"2016-01-19T12:49:21\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User deleted item(s) \\\"my_doc\\\" permanantly\",\"Activity_type\":\"Delete Forever\",\"Object_type\":\"File/Folder\",\"_ObjectName\":\"my_doc\",\"status_code\":\"200\",\"_log_hash\":\"GoogleDriveDeletePermanantly#d82d3023b1ff3b4072d3f0e13fed54e6d64f8014e80dda7814c6ab0b9e40e5b6\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://clients6.google.com/batch/drive\",\"req_size\":\"1992\",\"web_path\":\"drive.google.com ---> clients6.google.com\",\"resp_code\":\"200\",\"resp_size\":\"833\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/drive/trash\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207761000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"JcLT5re9QvOzVx_qePy6UA\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:48:44\",\"created_timestamp\":\"2016-01-19T12:48:44\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User deleted item(s) \\\"my_doc\\\"\",\"Activity_type\":\"Delete\",\"Object_type\":\"File/Folder\",\"_ObjectName\":\"my_doc\",\"status_code\":\"200\",\"_log_hash\":\"GoogleDriveSendItemToTrashNewUI#5dd25f672b00f6335c4bdde8cffe9bcf6e402798ab2a3a878a5f9289bb55751f\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://clients6.google.com/batch/drive\",\"req_size\":\"2824\",\"web_path\":\"drive.google.com ---> clients6.google.com\",\"resp_code\":\"200\",\"resp_size\":\"1442\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/drive/my-drive\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207724000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"V9vac4BjSq-DSpqVAERxwQ\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:48:02\",\"created_timestamp\":\"2016-01-19T12:48:02\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User deleted item(s) \\\"Copy of my_doc\\\"\",\"Activity_type\":\"Delete\",\"Object_type\":\"File/Folder\",\"_ObjectName\":\"Copy of my_doc\",\"status_code\":\"200\",\"_log_hash\":\"GoogleDriveSendItemToTrashNewUI#5dd25f672b00f6335c4bdde8cffe9bcf6e402798ab2a3a878a5f9289bb55751f\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://clients6.google.com/batch/drive\",\"req_size\":\"2824\",\"web_path\":\"drive.google.com ---> clients6.google.com\",\"resp_code\":\"200\",\"resp_size\":\"1440\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/drive/my-drive\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207682000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"wVUwbfzhSla-W6crcJkHRw\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:47:31\",\"created_timestamp\":\"2016-01-19T12:47:31\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User made a copy of item \\\"my_doc\\\" named \\\"Copy of my_doc\\\" with parent \\\"root\\\"\",\"Activity_type\":\"Copy\",\"Object_type\":\"File/Folder\",\"_ObjectName\":\"my_doc\",\"_log_hash\":\"EPDASSIST_Copy_0#07535b6e09dc171589b91392c09ad546620848d93f8850c019d6a95b09085ad8\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://clients6.google.com/batch/drive\",\"req_size\":\"2889\",\"web_path\":\"drive.google.com ---> clients6.google.com\",\"resp_code\":\"200\",\"resp_size\":\"2283\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://drive.google.com/drive/my-drive\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207651000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"KVujEZ0oQSmbEAOy5VkpXQ\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:46:40\",\"created_timestamp\":\"2016-01-19T12:46:40\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User Renamed Document to \\\"my_doc\\\"\",\"Activity_type\":\"Rename\",\"Object_type\":\"Document\",\"_ObjectName\":\"my_doc\",\"_log_hash\":\"GoogleDriveRenameInsideDoc#bd1c389e132576c2facbdba01e2bdfa2cabc456e371a13cf44381e58af9f739f\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://docs.google.com/a/gatewaybeatle.com/document/d/15KIxSFvCqJ4KlfOiJesqhfVuUDOyPnPIZ4byT2GhG40/chrome?id=15KIxSFvCqJ4KlfOiJesqhfVuUDOyPnPIZ4byT2GhG40&token=AC4w5VhRMQtdSvDZM3m9swAMJYBEJxoT1w%3A1453207588631\",\"req_size\":\"1848\",\"web_path\":\"docs.google.com ---> docs.google.com\",\"resp_code\":\"200\",\"resp_size\":\"524\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://docs.google.com/document/d/15KIxSFvCqJ4KlfOiJesqhfVuUDOyPnPIZ4byT2GhG40/edit\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207600000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"g536QnmSQQqpB1LoVQ63cg\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:46:28\",\"created_timestamp\":\"2016-01-19T12:46:28\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User Viewed document \\\"Untitled document\\\"\",\"Activity_type\":\"View\",\"Object_type\":\"Document\",\"_ObjectName\":\"Untitled document\",\"status_code\":\"200\",\"_log_hash\":\"GoogleDriveViewDocPresDrawingDomain#4f5e80ec9973c0e8ec7b541554dcb6c97629078fbdc386c99192e79428245f1c\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://docs.google.com/a/gatewaybeatle.com/document/d/15KIxSFvCqJ4KlfOiJesqhfVuUDOyPnPIZ4byT2GhG40/edit\",\"req_size\":\"1432\",\"web_path\":\" ---> docs.google.com\",\"resp_code\":\"200\",\"resp_size\":\"264930\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207588000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"ZbHIKheOQJy4GpcPyQaI3Q\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:46:28\",\"created_timestamp\":\"2016-01-19T12:46:28\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User Created new Document in \\\"root\\\"\",\"Activity_type\":\"Create\",\"Object_type\":\"Document\",\"_ObjectName\":\"Untitled document\",\"_log_hash\":\"GoogleDriveCreateDoc#337787ef980e5ad0c0c38d1fb9eeed026e5df1355406835ff5eba31522b97723\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://docs.google.com/a/gatewaybeatle.com/document/create?usp=drive_web&folder=0AMMUwvaBzVdlUk9PVA\",\"req_size\":\"1428\",\"web_path\":\" ---> docs.google.com\",\"resp_code\":\"302\",\"resp_size\":\"820\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207588000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"AmRzquvKTpmCS6q2b3O96w\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:45:46\",\"created_timestamp\":\"2016-01-19T12:45:46\",\"facility\":\"Gmail\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User logged in\",\"Activity_type\":\"Login\",\"Object_type\":\"Session\",\"_log_hash\":\"GmailLoggedIn#2f112661afc541f44e9435981af208fc1d40992dec87f7811357e9730846ea75\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://accounts.google.com/ServiceLoginAuth\",\"req_size\":\"2033\",\"web_path\":\"accounts.google.com ---> accounts.google.com\",\"resp_code\":\"302\",\"resp_size\":\"3122\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://accounts.google.com/ServiceLogin?service=wise&passive=1209600&continue=https://drive.google.com/%23&followup=https://drive.google.com/&ltmpl=drive&emr=1\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207546000]},{\"_index\":\"alias_logs_gatewaybeatlecom-2016\",\"_type\":\"elastica_logs\",\"_id\":\"DCfgZue0RTagLV1SmemLpQ\",\"_score\":null,\"_source\":{\"version\":\"1.0\",\"host\":\"54.186.103.182\",\"inserted_timestamp\":\"2016-01-19T12:45:46\",\"created_timestamp\":\"2016-01-19T12:45:46\",\"facility\":\"Google Drive\",\"user\":\"testuser3@gatewaybeatle.com\",\"severity\":\"informational\",\"message\":\"User logged in\",\"Activity_type\":\"Login\",\"Object_type\":\"Session\",\"_log_hash\":\"GoogleDriveLoggedIn#7829de5f9fd68b981163ca838477b4b4f4f3902e502cc416e75af2b32f6045a9\",\"user_name\":\"GWTest2 Name\",\"user-agent\":\"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0\",\"req_uri\":\"https://accounts.google.com/ServiceLoginAuth\",\"req_size\":\"2033\",\"web_path\":\"accounts.google.com ---> accounts.google.com\",\"resp_code\":\"302\",\"resp_size\":\"3247\",\"browser\":\"Firefox\",\"device\":\"Windows 8.1\",\"location\":\"Boardman (United States)\",\"country\":\"United States\",\"city\":\"Boardman\",\"latitude\":\"45.778801\",\"longitude\":\"-119.528999\",\"time_zone\":\"America/Los_Angeles\",\"region\":\"OR\",\"is_anonymous_proxy\":\"false\",\"account_type\":\"Internal\",\"__source\":\"GW\",\"referer_uri\":\"https://accounts.google.com/ServiceLogin?service=wise&passive=1209600&continue=https://drive.google.com/%23&followup=https://drive.google.com/&ltmpl=drive&emr=1\",\"elastica_user\":\"testuser3@gatewaybeatle.com\",\"transit_hosts\":\"\",\"_domain\":\"gatewaybeatle.com\"},\"sort\":[1453207546000]}]}}";
        String query = "$.hits.hits[*].source";
        
        String expectedkey = "message";
        String expectedvalue = "User1 made a copy of item my_doc named Copy of my_doc with parent root";
        
        Map<String, Object> expectedResult=new HashMap<>();
        expectedResult.put(expectedkey, expectedvalue);
       // expectedResult.put("Activity_type", "Move");
        expectedResult.put("transit_hosts", "");
        
         
        
       // List<Map<String, Object>> fetchAllKeys = RawJsonParser.fetchAllKeys(response, query);
        
        
       // Map<String, Object> compareAndNullCheckInMap = compareAndNullCheckInMap(expectedResult,fetchAllKeys,expectedkey);
       // RawJsonParser.printMap(compareAndNullCheckInMap);
        
     System.out.println(findExpectedKeysAndPartialValues(response,query,expectedResult, expectedkey));
        
    }
    
    
    
    public static boolean findExpectedKeysAndPartialValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
        Reporter.log("Expected Result :"+expectedResult.toString(),true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        List<Map<String, Object>> fetchAllKeys = fetchAllKeys(httpjsonResponse, parentNode);
        
         for (Map<String, Object> fetchAllKey : fetchAllKeys) {
             for (Map.Entry<String, Object> entrySet : fetchAllKey.entrySet()) {
                 String key = entrySet.getKey();
                 Object value = entrySet.getValue();
                 fetchAllKey.put(key, value.toString().replaceAll("\"", ""));
             }
         }
        
        if (matchMapFromMapList(fetchAllKeys, expectedResult,uniqueKey,true,true)) {
            return true;
        }
        return resultFound;
    }
    
   /* public static boolean findExpectedKeysAndPartialValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
        Reporter.log("Expected Result :"+expectedResult.toString(),true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), expectedResult,uniqueKey,true,true)) {
            return true;
        }
        return resultFound;
    }*/
    
    /**
     * @param httpjsonResponse
     * @param parentNode Ex. $.hits.hits[*].source , it will look into all the
     * hits..
     * @param expectedResult Map where we put expected Json keys/fields and
     * values..
     * @return true if all keys and values are present in the node list else
     * false...
     */
    
    public static boolean findExpectedKeysAndValuesIgnoreNullCheck(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
        Reporter.log("Expected Result :"+expectedResult.toString(),true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), expectedResult,uniqueKey,false,false)) {
            return true;
        }
        return resultFound;
    }
    
    
    
    public static boolean findExpectedKeysAndValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult) {
        Reporter.log("Expected Result :"+expectedResult.toString(),true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), expectedResult,"message",true,false)) {
            return true;
        }
        return resultFound;
    }
    
    public static boolean findExpectedKeysAndValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), expectedResult,uniqueKey,true,false)) {
            return true;
        }
        return resultFound;
    }
    
    

    /**
     * @param total_result ...List of Map
     * @param expectedResult .. Expected Map...
     * @param uniqueKey
     * @param nullCheck
     * @return if Expected Map Found then true, else return false..
     */
    public static boolean matchMapFromMapList(List<Map<String, Object>> total_result, final Map<String, Object> expectedResult, String uniqueKey,boolean nullCheck,boolean partialSearch) {
        Reporter.log("==================================================================================",true);
        Reporter.log("Expected Result :---", true);
        printMap(expectedResult);
        Reporter.log("==================================================================================",true);
        boolean validationResult=true;
        Map<String, Object> foundResult = null;
        List<String> keyList = Arrays.asList(expectedResult.keySet().toArray(new String[expectedResult.size()])); 
        Map<String, Object> findMatchingMapInMapList = null;
        if (partialSearch) {
            findMatchingMapInMapList = findPartialMatchingMapInMapList(total_result, uniqueKey, expectedResult);
        } else {
            findMatchingMapInMapList = findMatchingMapInMapList(total_result, uniqueKey, expectedResult);
        }
        if(findMatchingMapInMapList!=null){
            int count=1;
            Reporter.log("==================================================================================",true);
            Reporter.log("!! Result Found !! Actual Result with Null & Value Check :---",true);
            for (String key_ExpectedResult : keyList) {  
                if(!findMatchingMapInMapList.containsKey(key_ExpectedResult)){
                  Reporter.log("Key : "+key_ExpectedResult +" $$$$ NOT FOUND IN ACTUAL RESULT $$$ ",true); 
                  validationResult=false;
                }   
            }
            Reporter.log("==================================================================================",true);
            for (Map.Entry<String, Object> entrySet : findMatchingMapInMapList.entrySet()) {
                String key = entrySet.getKey();
                Object value = entrySet.getValue();
                if(keyList.contains(key)){
                    if(expectedResult.get(key).equals(NotEmpty)){
                        if(nullValidate(value)){
                           Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true); 
                        }
                        else{
                          Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> FAILED", true); 
                          if(nullCheck){
                          validationResult=false;
                          }
                        }
                    } 
                    else if (expectedResult.get(key).equals(NotZero)) {
                        if (nonZero(value)) {
                            Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true);
                        } else {
                            Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> FAILED", true);
                            if (nullCheck) {
                                validationResult = false;
                            }
                        }
                    }
                    else if (partialSearch) {
                        if (value.toString().contains(expectedResult.get(key).toString())) {
                            Reporter.log(count++ + ">>  " + key + ": " + value.toString() + " < ### Partial Value Check #### > !! PASSED !!", true);
                        } else {
                            Reporter.log("===========================================================================", true);
                            Reporter.log(count++ + ")  " + key + ": " + value.toString() +  "  ## Expected :"+expectedResult.get(key).toString()+ " <### Partial Value Check ####> $$ FAILED $$", true);
                            Reporter.log("===========================================================================", true);
                            if (nullCheck) {
                                validationResult = false;
                            }
                        }
                    } else if (value.equals(expectedResult.get(key))) {
                        Reporter.log(count++ + ">>  " + key + ": " + value.toString() + " <Value Check> !! PASSED !!", true);
                    }
                    else{
                       Reporter.log("===========================================================================",true);
                       Reporter.log(count++ +">>  "+key +": "+value.toString() +" <Value Check> $$ FAILED $$",true); 
                       Reporter.log("===========================================================================",true);
                       if(nullCheck){
                       validationResult=false;
                       }
                    }
                }
                else{
                    if (nullValidate(value)) {
                        Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true);
                    } else {
                        Reporter.log("===========================================================================",true);
                        Reporter.log(count++ + ">>  " + key + " : " + value + " ## <Null Check> FAILED", true); 
                        Reporter.log("===========================================================================",true);
                        if(nullCheck){
                        validationResult=false;
                        }
                    }
                }
                }
            }    
        else{
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(uniqueKey);
            Reporter.log("==================================================================================",true);
            Reporter.log("## Expected Result Not Found....",true);
            Reporter.log("Actual Json Parsed Result for Unique Json Field <"+uniqueKey+ "> From Response:--", true);
            Reporter.log("==================================================================================",true);
            printMapList(getRequiredKeys(total_result,arrayList));
            Reporter.log("!!!! Expected Result Not Found !!!!", true);
            validationResult=false;
        }
        return validationResult;
    }

    private static boolean nullValidate(Object value) {
        // Null Check For Other Fields....=============================================
        boolean IsNotNull=true;
        if(value==null){
            return false;
        }
        Class<? extends Object> aClass = value.getClass();
        String name = aClass.getName();
        // Integer Null Validations....
        if (name.equals("java.lang.Integer")) {
            if ((Integer) value == null) {     
                IsNotNull=false;
                } else {
                IsNotNull=true;
                
                }
        } // String Null Validations...
        else if (value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {  
           
            IsNotNull=false;
        } else {
            IsNotNull=true;    
        }
        return IsNotNull;
    }
    
    private static boolean nonZero(Object value) {
        // Null Check For Other Fields....=============================================
        boolean IsNotNull=true;
        if(value==null){
            return false;
        }
        Class<? extends Object> aClass = value.getClass();
        String name = aClass.getName();
        // Integer Null Validations....
        if (name.equals("java.lang.Integer")) {
                if ((Integer) value == null || (Integer) value == 0) {
                IsNotNull = false;
            } else {
                IsNotNull = true;
            }
        } // String Null Validations...
        else if (value.toString().equals("0") || value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {  
            IsNotNull=false;
        } else {
            IsNotNull=true;    
        }
        return IsNotNull;
    }
    
    public static Map<String, Object> findPartialMatchingMapInMapList(List<Map<String, Object>> total_result, String uniqueKey, final Map<String, Object> expectedResult) {
        for (Map<String, Object> actualResult : total_result){
            if(actualResult.get(uniqueKey).toString().contains(expectedResult.get(uniqueKey).toString())){
                return actualResult;
            }
        }
        return null;
    }

    public static Map<String, Object> findMatchingMapInMapList(List<Map<String, Object>> total_result, String uniqueKey, final Map<String, Object> expectedResult) {
        for (Map<String, Object> actualResult : total_result){
            if(actualResult.get(uniqueKey).equals(expectedResult.get(uniqueKey))){
                return actualResult;
            }
        }
        return null;
    }
    
    public static void printMapList(List<Map<String, Object>> total_result){
        int count=1;
        for (Map<String, Object> total_result1 : total_result) {
            Reporter.log(count++ +" ) "+total_result1.toString(), true);
        }
    }
    
    public static boolean printMatchResultAndValidateNull(List<String> keys, Map<String, Object> matchedMap) {
        Reporter.log("==================================================================================",true);
        Reporter.log("Actual Result with NULL Check Validation :---",true);
        Reporter.log("==================================================================================",true);
        int count = 0;
        boolean nullFound=true;
        for (Map.Entry<String, Object> entrySet : matchedMap.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if (keys.contains(key)) {
                Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Value Check> Passed", true);
            }
            else{
                
                Class<? extends Object> aClass = value.getClass();
                String name = aClass.getName();
                // Integer Null Validations....
                if (name.equals("java.lang.Integer")) {
                    if ((Integer) value == null) {
                        Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> FAILED", true); 
                        nullFound=false;
                    }
                    else{
                       Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true);  
                    }
                }
                // String Null Validations...
                else if (value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {
                    Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> FAILED", true);
                    nullFound=false;
                }  
                else{
                   Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true); 
                }
            }  
            }
        return nullFound;
        }
    
    
    
    public static void printMap(final Map<String, Object> expectedResult) {
        int count=1;
        for (Map.Entry<String, Object> entrySet : expectedResult.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            Reporter.log(count++ +")  "+key + " : "+value.toString(),true);
        }
    }
    
    /**
     * @param httpjsonResponse
     * @param parentNode Ex. $.hits.hits[*].source , it will look into all the
     * hits..
     * @param keys Ex. Activity_Type
     * @return
     */
    public static List<Map<String, Object>> getRequiredKeys(String httpjsonResponse, String parentNode, List<String> keys) {
        List<Map<String, Object>> total_result = new ArrayList<>();
        List<Map<String, Object>> fetchAllKeys = fetchAllKeys(httpjsonResponse,parentNode);
        for (Map<String, Object> fetchAllKey : fetchAllKeys) {
             Map<String, Object> map=new HashMap<>();
             for (String key : keys) {
                if(fetchAllKey.containsKey(key)){
                map.put(key, fetchAllKey.get(key).toString());
                }
                else{
                  map.put(key,"NOT FOUND");  
                }
            }  
            total_result.add(map);
        }
        return total_result;
    }
    
    public static List<Map<String, Object>> getRequiredKeys(List<Map<String, Object>> fetchAllKeys, List<String> keys) {
        List<Map<String, Object>> total_result = new ArrayList<>();
        for (Map<String, Object> fetchAllKey : fetchAllKeys) {
             Map<String, Object> map=new HashMap<>();
             for (String key : keys) {
                if(fetchAllKey.containsKey(key)){
                map.put(key, fetchAllKey.get(key));
                }
                else{
                  map.put(key,"NOT FOUND");  
                }
            }  
            total_result.add(map);
        }
        return total_result;
    }
    

    /**
     *
     * @param httpjsonResponse
     * @param query Ex. "$.hits.total"
     * @return it will return first occurrence for matched parameter..
     */
    public static String getSingleKey(String httpjsonResponse, String query) {
        Object singleResult = null;
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        try {
            singleResult = surfer.collectOne(httpjsonResponse, query);
        } catch (Exception e) {
            return null;
        }
        return singleResult.toString();
    }

    /**
     * @param httpjsonResponse
     * @param query
     * @return it return all the matched keys..
     */
    public static String getAllKeys(String httpjsonResponse, String query) {
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        Object singleResult = surfer.collectAll(httpjsonResponse, query);
        return singleResult.toString();
    }
    

    public static Object fetchSingleField(String httpjsonResponse, String query) {
        Object singleResult = null;
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        try {
            singleResult = surfer.collectOne(httpjsonResponse, query);
        } catch (Exception e) {
            return null;
        }
        return singleResult;
    }

    /**
     * @param httpjsonResponse
     * @param query
     * @return it return all the matched keys..
     */
    public static Object fetchMultipleFields(String httpjsonResponse, String query) {
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        Object allResult = surfer.collectAll(httpjsonResponse, query);
        return allResult;
    }
    
    public static boolean validateNullValues(List<Map<String, Object>> multipleResults) {
        boolean nullFound = true;
        for (Map<String, Object> result : multipleResults) {
            for (Map.Entry<String, Object> entrySet : result.entrySet()) {
                String key = entrySet.getKey();
                Object value = entrySet.getValue();
                Class<? extends Object> aClass = value.getClass();
                String name = aClass.getName();
                // Integer Null Validations....
                if (name.equals("java.lang.Integer")) {
                    if ((Integer) value == null) {
                        Reporter.log("======================== NULL FOUND =========================================",true);
                        Reporter.log("Map Result :"+result.toString(),true);
                        Reporter.log("## NULL entry Found in Field : KEY :" + key + "## Value :" + value, true);
                        nullFound = false;//fail Scenarios
                    }
                }
                // String Null Validations...
                if (value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {
                    Reporter.log("======================= NULL FOUND ============================================",true);
                    Reporter.log("Map Result :"+result.toString(),true);
                    Reporter.log("## null entry Found in Field : KEY :" + key + "## Value :" + value, true);
                    nullFound = false;//fail Scenarios
                }
            }
        }
        Reporter.log("Null Found :"+nullFound + " !! Null Not Found..!!",true);
        return nullFound;
    }

    public static List<Map<String, Object>> fetchAllKeys(String jsonResponse, String parentNode){
        if (jsonResponse == null) {
            Reporter.log("## Invalid HTTP Response/Result :"+jsonResponse,true);
            return null;
        }
        jsonResponse = jsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        Reporter.log("### Filtering the Results from Json Response .....#### ", true);
        List<Map<String, Object>> filteredResults = new ArrayList<>();
        JsonSurfer jsonSurfer = JsonSurfer.simple();
        Collection<Object> multipleResults = jsonSurfer.collectAll(jsonResponse, parentNode);
        Reporter.log("### Results Count :" + multipleResults.size(),true);
        LinkedHashMap<String, Object> result = null;
        for (Object multipleResult : multipleResults) {
            String toString = multipleResult.toString();
            try {
                result = new ObjectMapper().readValue(toString, LinkedHashMap.class);
                for (Map.Entry<String, Object> entrySet : result.entrySet()) {
                    String key = entrySet.getKey();
                    Object value = entrySet.getValue();
                    if(value!=null){
                    Class<? extends Object> aClass = value.getClass();
                     String name = aClass.getName();
                     if(name.contains("String")){
                         result.put(key, value.toString().trim());
                     }
                    }
                    else{
                       result.put(key, value); 
                    }
                }
            } catch (IOException ex) {
                Reporter.log("### Issue Found with Object Mapper"+ex.getLocalizedMessage(),true);
            }
            filteredResults.add(result);
        }
        return filteredResults;
    }

}
