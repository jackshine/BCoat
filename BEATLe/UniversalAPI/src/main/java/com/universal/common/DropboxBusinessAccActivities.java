package com.universal.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.universal.common.DropBox;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;


/**
 * @author rahulkumar Ref:
 * https://www.dropbox.com/developers/business/docs#get-info
 * https://www.dropbox.com/developers/apps/create/business
 */
public class DropboxBusinessAccActivities {

    String getAccountInfo_uri = "https://api.dropbox.com/1/team/get_info";
    String getMemberList_uri = "https://api.dropbox.com/1/team/members/list";
    String getMemberInfo_uri = "https://api.dropbox.com/1/team/members/get_info";
    String get_info_batch_uri = "https://api.dropbox.com/1/team/members/get_info_batch";
    String addMember_uri = "https://api.dropbox.com/1/team/members/add";
    String set_profile_uri = "https://api.dropbox.com/1/team/members/set_profile";
    String set_permissions_uri = "https://api.dropbox.com/1/team/members/set_permissions";
    String send_welcome_email_uri = "https://api.dropbox.com/1/team/members/send_welcome_email";
    String members_remove = "https://api.dropbox.com/1/team/members/remove";
    String groupList_uri = "https://api.dropbox.com/1/team/groups/list";
    String getGroupInfo = "https://api.dropbox.com/1/team/groups/get_info";
    String createGroup = "https://api.dropbox.com/1/team/groups/create";
    String removeMembersFromGroup = "https://api.dropbox.com/1/team/groups/members/remove";
    String setMemberAccessInGroup = "https://api.dropbox.com/1/team/groups/members/set_access_type";

    // Dropbox Business Account Activities...
    String fileUpload = "https://api-content.dropbox.com/1/files_put/auto/";
    String TeamMemberFileAccess_accesstoken = "";
    String TeamMemberManagement_accesstoken = "";

    HashMap<String, String> headersForBusinessMemberSpecificOperations = new HashMap<>();

    enum MEMBER_ACCESS_TYPE {

        OWNER, MEMBER
    }

    public DropboxBusinessAccActivities(String TeamMemberManagement_accesstoken, String TeamMemberFileAccess_accesstoken) {
        if (TeamMemberManagement_accesstoken == null && TeamMemberManagement_accesstoken == null) {
            System.out.println("businessAccountToken=NULL ,Default Business Account will be taken...");
            System.out.println("Reading the Properties file for <TeamMemberManagement_accesstoken>.. ");
            ResourceBundle rb = ResourceBundle.getBundle("com.universal.properties.dropbox");
            this.TeamMemberManagement_accesstoken = (String) rb.getObject("TeamMemberManagement_accesstoken");
            this.TeamMemberFileAccess_accesstoken = (String) rb.getObject("TeamMemberFileAccess_accesstoken");
            System.out.println("TeamMemberManagement_accesstoken: XXX  found and set for business Activities.. ");
            System.out.println("\nVisit App Console for Token: https://www.dropbox.com/developers/apps");
            System.out.println("Ref for Business API Doc : \n https://www.dropbox.com/developers/core/docs#files_put \n https://www.dropbox.com/developers/business/docs#groups-members-set-access-type");
            createHeadersBusinessMemberSpecificOperations();
        } else {
            this.TeamMemberManagement_accesstoken = TeamMemberManagement_accesstoken;
            this.TeamMemberFileAccess_accesstoken = TeamMemberFileAccess_accesstoken;
            createHeadersBusinessMemberSpecificOperations();
        }
    }

    public void createHeadersBusinessMemberSpecificOperations() {
        System.out.println("\nCreating Headers To Perform Dropbox Business Activities...");
        headersForBusinessMemberSpecificOperations.put("Authorization", "Bearer " + this.TeamMemberManagement_accesstoken.trim());
        headersForBusinessMemberSpecificOperations.put("Content-Type", "application/json ");
        headersForBusinessMemberSpecificOperations.put("Accept-Language", "en-US,en;q=0.8");
        System.out.println("Headers Creation Successfully Completed/Done\n");
    }
    
    public String getDropboxResponse(String url, HashMap<String, String> headersMap) {
        String response = null;
        try {
            response = DropBox.sentPost(url, headersMap);
        } catch (IOException ex) {
            Logger.getLogger(DropboxBusinessAccActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
       // System.out.println("\nURL :" + getAccountInfo_uri + "\nRESPONSE :" + response);
        return response;
    }
    
    public String getDropboxResponse(String url, HashMap<String, String> headersMap, byte[] payLoad) {
        String response = null;
        try {
            response = DropBox.sentPost(url, headersMap, payLoad);
        } catch (IOException ex) {
            Logger.getLogger(DropboxBusinessAccActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
       // System.out.println("\nURL :" + getAccountInfo_uri + "\nRESPONSE :" + response);
        return response;
    }

    public String getDropboxResponse(String url, HashMap<String, String> headersMap, String payLoad) {
        String response = null;
        try {
            response = DropBox.sentPost(url, headersMap, payLoad);
        } catch (IOException ex) {
            Logger.getLogger(DropboxBusinessAccActivities.class.getName()).log(Level.SEVERE, null, ex);
        }
       // System.out.println("\nURL :" + getAccountInfo_uri + "\nRESPONSE :" + response);
        return response;
    }

    public Map<String, String> getMap(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        Type stringStringMap = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(json, stringStringMap);
        return map;
    }

    // TEAM INFO...
    public Map<String, String> getAccInfo() {
        String payLoad = "{}";
        String dropboxResponse = getDropboxResponse(getAccountInfo_uri, headersForBusinessMemberSpecificOperations, payLoad);
        return getMap(dropboxResponse);
    }

    // MEMBER INFO...
    public String getMemberList() {
        String payLoad = "{}";
        String dropboxResponse = getDropboxResponse(getMemberList_uri, headersForBusinessMemberSpecificOperations, payLoad);
        return dropboxResponse;
    }

    public Map<String, String> getMemberInfo(String emailID) {
        String payLoad = "{\"email\":\"" + emailID + "\"}";
        String dropboxResponse_getMemberInfo = getDropboxResponse(getMemberInfo_uri, headersForBusinessMemberSpecificOperations, payLoad);
        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(dropboxResponse_getMemberInfo).getAsJsonObject();
        JsonObject locObj = rootObj.getAsJsonObject("profile");
        String member_id = locObj.get("member_id").getAsString();
       // System.out.println("member_id ==> :" + member_id);
        Map<String, String> getMemberInfo = new HashMap<>();
        getMemberInfo.put("member_id", member_id);
        return getMemberInfo;
    }

    public String getInfoBatch(List<String> emails) {
        String payLoad = "{\"emails\":["
                + "\""
                + ""
                + "qa-admin@elasticaqa.net"
                + "\","
                + "\""
                + "qa-admin@elasticaqa.net"
                + "\"]}";
        return getDropboxResponse(get_info_batch_uri, headersForBusinessMemberSpecificOperations, payLoad);
    }

    // MEMBER MANAGEMENT....
    public String addMember(String memberEmail, String memberGivenName, String memberSurname) {
        String payLoad = "{\"member_email\":\"" + memberEmail + "\",\"member_given_name\":\"" + memberGivenName + "\",\"member_surname\":\"" + memberSurname + "\",\"send_welcome_email\":true}";
        return getDropboxResponse(addMember_uri, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String setProfile(String memberId, String newMail, String newGivenName) {
        String payLoad = "{\"member_id\":\"" + memberId + "\",\"new_email\":\"" + newMail + "\",\"new_given_name\":\"" + newGivenName + "\"}";
        return getDropboxResponse(set_profile_uri, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String setPermission(String memberId, String isNewAdmin) {
        String payLoad = "{\"member_id\":\"" + memberId + "\",\"new_is_admin\":" + isNewAdmin + "}";
        return getDropboxResponse(set_permissions_uri, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String sendWelcomeMail(String memberId) {
        String payLoad = "{\"member_id\":\"" + memberId + "\"}";
        return getDropboxResponse(send_welcome_email_uri, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String removeMember(String memberId) {
        String payLoad = "{\"member_id\":\"" + memberId + "\"}";
        return getDropboxResponse(send_welcome_email_uri, headersForBusinessMemberSpecificOperations, payLoad);
    }

    // GROUP INFO...
    public String getGroupList(String groupName, String groupId, String num_members) {
        String payLoad = "{\"groups\":[{\"group_name\":\"" + groupName + "\",\"group_id\":\"" + groupId + "\",\"num_members\":" + num_members + "}]}";
        return getDropboxResponse(groupList_uri, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String getGroupInfo(String groupId) {
        String payLoad = "{\"group_ids\":[\"" + groupId + "\"]}";
        return getDropboxResponse(getGroupInfo, headersForBusinessMemberSpecificOperations, payLoad);
    }

    // GROUP MANAGEMENT....
    public String createGroup(String groupName) {
        String payLoad = "{\"group_name\":\"" + groupName + "\"}";
        return getDropboxResponse(createGroup, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String deleteGroup(String groupId) {
        String deleteGroup_uri = "https://api.dropbox.com/1/team/groups/delete";
        String payLoad = "{\"group_id\":\"" + groupId + "\"}";
        return getDropboxResponse(deleteGroup_uri, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String addMembersToGroup(String groupId, String teamMemberId, String member_access_type) {
        String addMembersToGroup_uri = "https://api.dropbox.com/1/team/groups/members/add";
        String payLoad = "";
        switch (member_access_type) {
            case "owner":
                payLoad = "{\"group_id\":\"" + groupId + "\",\"members\":[{\"team_member_id\":\"" + teamMemberId + "\",\"access_type\":\"owner\"}]}";
                System.out.println("owner");
                return getDropboxResponse(addMembersToGroup_uri, headersForBusinessMemberSpecificOperations, payLoad);
            case "member":
                payLoad = "{\"group_id\":\"" + groupId + "\",\"members\":[{\"team_member_id\":\"" + teamMemberId + "\",\"access_type\":\"member\"}]}";
                System.out.println("member");
                return getDropboxResponse(addMembersToGroup_uri, headersForBusinessMemberSpecificOperations, payLoad);
        }
        return getDropboxResponse(addMembersToGroup_uri, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String removeMembersFromGroup(String groupId, String teamMemberId) {
        String payLoad = "{\"" + "group_id\":\"" + groupId + "\",\"members\":[{\"team_member_id\":\"" + teamMemberId + "\"}]}";
        return getDropboxResponse(removeMembersFromGroup, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String setGroupMemberAccessType(String groupId, String teamMemberId, String access_type) {
        String payLoad = "{\"group_id\":\"" + groupId + "\",\"team_member_id\":\"" + teamMemberId + "\",\"access_type\":\"" + access_type + "\"}";
        return getDropboxResponse(setMemberAccessInGroup, headersForBusinessMemberSpecificOperations, payLoad);
    }

    public String getMemberId(String memberEmail) {
        Map<String, String> memberInfo = getMemberInfo(memberEmail);
        String memberId = memberInfo.get("member_id");
        //System.out.println("Member ID :" + memberId);
        return memberId;
    }

    // file Upload...
    public String fileUploadFromLocal(String memberId, String cloudfolderLocation, String LocalFileLocation) throws IOException {
        File file = new File(LocalFileLocation);
        String fileName=file.getName();
        String cloudfilePath=cloudfolderLocation+File.separator+fileName;
        String fileUploadurl = "https://api-content.dropbox.com/1/files_put/auto" + cloudfilePath + "?param=val";
        Map<String, String> headersForMemberSpecificOperations = createHeaderForFileMemberActivities(memberId);
        byte[] byteArray = null;
        try {
            int length = (int)file.length();
            byteArray = new byte[length];
            byteArray = FileUtils.readFileToByteArray(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getDropboxResponse(fileUploadurl, (HashMap<String, String>) headersForMemberSpecificOperations, byteArray);
    }

    // file Upload...
    public String fileUpload(String memberId, String cloudfilePath, String fileContent) throws IOException {
        String fileUploadurl = "https://api-content.dropbox.com/1/files_put/auto" + cloudfilePath + "?param=val";
        Map<String, String> headersForMemberSpecificOperations = createHeaderForFileMemberActivities(memberId);
        return getDropboxResponse(fileUploadurl, (HashMap<String, String>) headersForMemberSpecificOperations, fileContent);
    }

    // Folder Copy...
    public String folderCopyFromPath(String memberId, String sourceCloudPath, String destinationCloudPath) throws IOException {
        String fileCopyUrl = "https://api.dropboxapi.com/1/fileops/copy?from_path="+sourceCloudPath+"&to_path="+destinationCloudPath+"&root=auto";
        Map<String, String> headersForMemberSpecificOperations = createHeaderForFileMemberActivities(memberId);
        return getDropboxResponse(fileCopyUrl, (HashMap<String, String>) headersForMemberSpecificOperations);
    }
    
    // Folder Copy...folderCopyFromReference
    public String folderCopyFromReference(String memberId, String copyReference, String destinationCloudPath) throws IOException {
        String fileCopyUrl = "https://api.dropboxapi.com/1/fileops/copy?from_copy_ref="+copyReference+"&to_path="+destinationCloudPath+"&root=auto";
        Map<String, String> headersForMemberSpecificOperations = createHeaderForFileMemberActivities(memberId);
        return getDropboxResponse(fileCopyUrl, (HashMap<String, String>) headersForMemberSpecificOperations);
    }
    
    public String performPublicSharing(String memberId,String destinationCloudPath){
        String publicShareUrl="https://api.dropboxapi.com/1/shares/auto/"+destinationCloudPath;
        Map<String, String> headersForMemberSpecificOperations = createHeaderForFileMemberActivities(memberId);
        return getDropboxResponse(publicShareUrl, (HashMap<String, String>) headersForMemberSpecificOperations);
    }
    
    public String getRevision(String memberId, String destinationCloudPath) {
        String getRevision = "https://api.dropboxapi.com/1/revisions/auto/" + destinationCloudPath;
        Map<String, String> headersForMemberSpecificOperations = createHeaderForFileMemberActivities(memberId);
        return getDropboxResponse(getRevision, (HashMap<String, String>) headersForMemberSpecificOperations);
    }
    
//     public String performInternalSharing(String memberId,String destinationCloudPath){
//        String publicShareUrl="https://api.dropboxapi.com/1/shares/auto/"+destinationCloudPath;
//        Map<String, String> headersForMemberSpecificOperations = createHeaderForFileMemberActivities(memberId);
//        return getDropboxResponse(publicShareUrl, (HashMap<String, String>) headersForMemberSpecificOperations);
//    }
//    
    // Folder Copy...
    public String getCopyReference(String memberId, String cloudLocation) throws IOException {
        String copyReferenceUrl = "https://api.dropboxapi.com/1/copy_ref/auto/"+cloudLocation;
        Map<String, String> headersForMemberSpecificOperations = createHeaderForFileMemberActivities(memberId);
        return getDropboxResponse(copyReferenceUrl, (HashMap<String, String>) headersForMemberSpecificOperations);
    }
    
    private Map<String, String> createHeaderForFileMemberActivities(String memberId) {
        Map<String, String> headersForMemberSpecificOperations = new HashMap<>();
        String authorizationHeaderValue = "Bearer " + this.TeamMemberFileAccess_accesstoken;
        headersForMemberSpecificOperations.put("Authorization", authorizationHeaderValue);
        headersForMemberSpecificOperations.put("X-Dropbox-Perform-As-Team-Member", memberId);
        return headersForMemberSpecificOperations;
    }

    public static void main(String[] args) throws IOException {
        DropboxBusinessAccActivities activities = new DropboxBusinessAccActivities(null, null);
        String memberId = activities.getMemberId("rahul.kumar@elastica.co");
        activities.fileUpload(memberId, "/Rahul/Bangalore12345/hello.txt", "Where r u Rahul");
    }

    // REPORTS...
}
