package com.elastica.action.protect;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.elastica.common.SuiteData;

public class ProtectQueryBuilder {

	public String getFileSharingPolicyESQuery(ProtectDTO protectData) throws IOException{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", protectData.getPolicyName()).
					field("policy_description", "Policy Description").
					startArray("applications");
						if(protectData.getApplicationList() == null){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getApplicationList().size();i++){
								builder.value(protectData.getApplicationList().get(i));
							}
						}
					builder.endArray().
					field("is_exposed", true).
					field("is_public", false).
					
					startObject("users_scope").
						startArray("users");
							if(protectData.getUserList() == null){
								builder.value("__ALL_EL__");
							}else{
								for(int i=0;i<protectData.getUserList().size();i++){
									builder.value(protectData.getUserList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupList() == null){
								if(protectData.getUserList() == null){
									builder.value("__ALL_EL__");
								}
							}else{
								for(int i=0;i<protectData.getGroupList().size();i++){
									builder.value(protectData.getGroupList().get(i));
								}
							}
						builder.endArray().
						startArray("domains");
							if(protectData.getDomainList() == null){
								builder.value("__ALL_EL__");
							}else{
								for(int i=0;i<protectData.getDomainList().size();i++){
									builder.value(protectData.getDomainList().get(i));
								}
							}
						builder.endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							if(protectData.getUserWhiteList() != null){
								for(int i=0;i<protectData.getUserWhiteList().size();i++){
									builder.value(protectData.getUserWhiteList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupWhiteList() != null){
								for(int i=0;i<protectData.getGroupWhiteList().size();i++){
									builder.value(protectData.getGroupWhiteList().get(i));
								}
							}
						builder.endArray().
					endObject().
					field("risk_level", protectData.getThreadScore()).
					field("org_unit", "").
					startObject("response").
						startArray("action");
							for(int i=0;i<protectData.getActionList().size();i++){
								builder.value(protectData.getActionList().get(i));
							}
						builder.endArray().
						field("notify_user", true).
						startArray("notify_email").
						endArray().
						startArray("notify_sms").
						endArray().
						startArray("notify_ticketing_email").
						endArray().
						field("severity_level", protectData.getSeverity()).
					endObject().
					field("violations", 1).
					startObject("recipient_scope").
						startArray("users");
							if(protectData.getUserRecipientList() == null){
								builder.value("__ALL_EL__");
							}else{
								for(int i=0;i<protectData.getUserRecipientList().size();i++){
									builder.value(protectData.getUserRecipientList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupRecipientList() == null){
								if(protectData.getUserRecipientList() == null){
									builder.value("__ALL_EL__");
								}
							}else{
								for(int i=0;i<protectData.getGroupRecipientList().size();i++){
									builder.value(protectData.getGroupRecipientList().get(i));
								}
							}
						builder.endArray().
					endObject().
					startObject("recipient_scope_whitelist").
						startArray("users");
							if(protectData.getUserRecipientWhiteList() != null){
								for(int i=0;i<protectData.getUserRecipientWhiteList().size();i++){
									builder.value(protectData.getUserRecipientWhiteList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupRecipientWhiteList() != null){
								for(int i=0;i<protectData.getGroupRecipientWhiteList().size();i++){
									builder.value(protectData.getGroupRecipientWhiteList().get(i));
								}
							}
						builder.endArray().
					endObject().
					startArray("geoip_scope");
						if(protectData.getGeoIPScope() == null){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getGeoIPScope().size();i++){
								builder.value(protectData.getGeoIPScope().get(i));
							}
						}
					builder.endArray().
					startArray("geoip_whitelist");
						if(protectData.getGeoIPWhiteList() != null){
							for(int i=0;i<protectData.getGeoIPWhiteList().size();i++){
								builder.value(protectData.getGeoIPWhiteList().get(i));
							}
						}
					builder.endArray().
					startArray("vulnerability_type");
						if(protectData.getVulnerabilityType() != null){
							for(int i=0;i<protectData.getVulnerabilityType().size();i++){
								builder.value(protectData.getVulnerabilityType().get(i));
							}
						}
					builder.endArray().
					startArray("content_profiles");
						if(protectData.getContentProfiles() != null){
							for(int i=0;i<protectData.getContentProfiles().size();i++){
								builder.value(protectData.getContentProfiles().get(i));
							}
						}
					builder.endArray().
					startArray("filename_pattern");
						if(protectData.getFilenamePattern() != null){
							for(int i=0;i<protectData.getFilenamePattern().size();i++){
								builder.value(protectData.getFilenamePattern().get(i));
							}
						}
					builder.endArray().
					startArray("file_scope_types");
						if(protectData.getFileScopeTypes() == null){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getFileScopeTypes().size();i++){
								builder.value(protectData.getFileScopeTypes().get(i));
							}
						}
					builder.endArray().
					startArray("file_scope_whitelist");
						if(protectData.getFileScopeWhiteList() != null){
							for(int i=0;i<protectData.getFileScopeWhiteList().size();i++){
								builder.value(protectData.getFileScopeWhiteList().get(i));
							}
						}
					builder.endArray().
					startArray("account_type");
						if(protectData.getAccountType() == null){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getAccountType().size();i++){
								builder.value(protectData.getAccountType().get(i));
							}
						}
					builder.endArray().
					field("policy_type","documentshare").
					startObject("device_management_info").
						field("status","__ALL_EL__").
						field("posture","__ALL_EL__").
						field("ownership","__ALL_EL__").
					endObject().
					field("is_active",protectData.getStatus()).
				endObject();
		return builder.string();
	}
	
	public String getAccessEnforcementPolicyESQuery(ProtectDTO protectData) throws Exception{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", protectData.getPolicyName()).
					field("policy_description", "Policy Description").
					startArray("applications");
						if(protectData.getApplicationList().size() == 0){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getApplicationList().size();i++){
								builder.value(protectData.getApplicationList().get(i));
							}
						}
					builder.endArray().
					field("is_exposed", true).
					field("is_public", false).
					startObject("users_scope").
						startArray("users");
							if(protectData.getUserList() == null){
								builder.value("__ALL_EL__");
							}else{
								for(int i=0;i<protectData.getUserList().size();i++){
									builder.value(protectData.getUserList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupList() == null){
								if(protectData.getUserList() == null){
									builder.value("__ALL_EL__");
								}
							}else{
								for(int i=0;i<protectData.getGroupList().size();i++){
									builder.value(protectData.getGroupList().get(i));
								}
							}
						builder.endArray().
						startArray("domains");
							if(protectData.getDomainList() == null){
								builder.value("__ALL_EL__");
							}else{
								for(int i=0;i<protectData.getDomainList().size();i++){
									builder.value(protectData.getDomainList().get(i));
								}
							}
						builder.endArray().
					endObject().
						startObject("users_scope_whitelist").
						startArray("users");
							if(protectData.getUserWhiteList() != null){
								for(int i=0;i<protectData.getUserWhiteList().size();i++){
									builder.value(protectData.getUserWhiteList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupWhiteList() != null){
								for(int i=0;i<protectData.getGroupWhiteList().size();i++){
									builder.value(protectData.getGroupWhiteList().get(i));
								}
							}
						builder.endArray().
					endObject().
					field("risk_level", protectData.getThreadScore()).
					field("org_unit", "").
					startObject("response").
						startArray("action").
							value("BLOCK_SHARE").
							value("NOTIFY_USER").
							value("SEVERITY_LEVEL").
						endArray().
						field("notify_user", true).
						startArray("notify_email").
						endArray().
						startArray("notify_sms").
						endArray().
						startArray("notify_ticketing_email").
						endArray().
						field("severity_level", protectData.getSeverity()).
					endObject().
					field("violations", 1).
					startArray("geoip_scope");
						if(protectData.getGeoIPScope() == null){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getGeoIPScope().size();i++){
								builder.value(protectData.getGeoIPScope().get(i));
							}
						}
					builder.endArray().
					startArray("geoip_whitelist");
						if(protectData.getGeoIPWhiteList() != null){
							for(int i=0;i<protectData.getGeoIPWhiteList().size();i++){
								builder.value(protectData.getGeoIPWhiteList().get(i));
							}
						}
					builder.endArray().
					startArray("platform");
						if(protectData.getPlatformList() == null){
							builder.startObject().
								field("item", "__ALL_EL__").
							endObject();
						}else{
							for(int i=0;i<protectData.getPlatformList().size();i++){
								builder.startObject().
								field("item", protectData.getPlatformList().get(i).get("item")).
								field("version", protectData.getPlatformList().get(i).get("version")).
								field("condition", "").
							endObject();
							}
						}
					builder.endArray().
					startArray("platform_whitelist").
					endArray().
					startArray("browser");
						if(protectData.getBrowserList() == null){
							builder.startObject().
								field("item", "__ALL_EL__").
							endObject();
						}else{
							for(int i=0;i<protectData.getBrowserList().size();i++){
								builder.startObject().
								field("item", protectData.getBrowserList().get(i).get("item")).
								field("version", protectData.getBrowserList().get(i).get("version")).
								field("condition", "").
							endObject();
							}
						}
					builder.endArray().
					startArray("browser_whitelist").
					endArray().
					startArray("activities_scope").
						value("ActivityObjects").
					endArray().
					startArray("account_type");
						if(protectData.getAccountType() == null){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getAccountType().size();i++){
								builder.value(protectData.getAccountType().get(i));
							}
						}
					builder.endArray().
					field("policy_type", "accessenforcement").
					startObject("device_management_info").
						field("status", "__ALL_EL__").
						field("posture", "__ALL_EL__").
						field("ownership", "__ALL_EL__").
					endObject().
					field("is_active", protectData.getStatus()).
				endObject();
		return builder.string();
	}
	
	public String getFileTransferPolicyESQuery(ProtectDTO protectData) throws IOException{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", protectData.getPolicyName()).
					field("policy_description", "Policy Description").
					startArray("applications");
						if(protectData.getApplicationList().size() == 0){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getApplicationList().size();i++){
								builder.value(protectData.getApplicationList().get(i));
							}
						}
					builder.endArray().
					field("is_exposed", true).
					field("is_public", false).
					
					startObject("users_scope").
						startArray("users");
							if(protectData.getUserList() == null){
								builder.value("__ALL_EL__");
							}else{
								for(int i=0;i<protectData.getUserList().size();i++){
									builder.value(protectData.getUserList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupList() == null){
								if(protectData.getUserList() == null){
									builder.value("__ALL_EL__");
								}
							}else{
								for(int i=0;i<protectData.getGroupList().size();i++){
									builder.value(protectData.getGroupList().get(i));
								}
							}
						builder.endArray().
						startArray("domains");
							if(protectData.getDomainList() == null){
								builder.value("__ALL_EL__");
							}else{
								for(int i=0;i<protectData.getDomainList().size();i++){
									builder.value(protectData.getDomainList().get(i));
								}
							}
						builder.endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							if(protectData.getUserWhiteList() != null){
								for(int i=0;i<protectData.getUserWhiteList().size();i++){
									builder.value(protectData.getUserWhiteList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupWhiteList() != null){
								for(int i=0;i<protectData.getGroupWhiteList().size();i++){
									builder.value(protectData.getGroupWhiteList().get(i));
								}
							}
						builder.endArray().
					endObject().
					field("risk_level", protectData.getThreadScore()).
					field("org_unit", "").
					startObject("response").
						startArray("action");
							for(int i=0;i<protectData.getActionList().size();i++){
								builder.value(protectData.getActionList().get(i));
							}
						builder.endArray().
						field("notify_user", true).
						startArray("notify_email").
						endArray().
						startArray("notify_sms").
						endArray().
						startArray("notify_ticketing_email").
						endArray().
						field("severity_level", protectData.getSeverity()).
					endObject().
					field("violations", 1).
					startArray("transfer_type");
						for(int i=0;i<protectData.getTransferType().size();i++){
							builder.value(protectData.getTransferType().get(i));
						}
					builder.endArray().
					startObject("file_size").
						field("larger_than", protectData.getLargerSize()).
						field("smaller_than", protectData.getSmallerSize()).
					endObject().
					startArray("geoip_scope");
						if(protectData.getGeoIPScope() == null){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getGeoIPScope().size();i++){
								builder.value(protectData.getGeoIPScope().get(i));
							}
						}
					builder.endArray().
					startArray("geoip_whitelist");
						if(protectData.getGeoIPWhiteList() != null){
							for(int i=0;i<protectData.getGeoIPWhiteList().size();i++){
								builder.value(protectData.getGeoIPWhiteList().get(i));
							}
						}
					builder.endArray().
					startArray("vulnerability_type");
						if(protectData.getVulnerabilityType() != null){
							for(int i=0;i<protectData.getVulnerabilityType().size();i++){
								builder.value(protectData.getVulnerabilityType().get(i));
							}
						}
					builder.endArray().
					startArray("content_profiles");
						if(protectData.getContentProfiles() != null){
							for(int i=0;i<protectData.getContentProfiles().size();i++){
								builder.value(protectData.getContentProfiles().get(i));
							}
						}
					builder.endArray().
					startArray("filename_pattern");
						if(protectData.getFilenamePattern() != null){
							for(int i=0;i<protectData.getFilenamePattern().size();i++){
								builder.value(protectData.getFilenamePattern().get(i));
							}
						}
					builder.endArray().
					startArray("file_scope_types");
						if(protectData.getFileScopeTypes() == null){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getFileScopeTypes().size();i++){
								builder.value(protectData.getFileScopeTypes().get(i));
							}
						}
					builder.endArray().
					startArray("file_scope_whitelist");
						if(protectData.getFileScopeWhiteList() != null){
							for(int i=0;i<protectData.getFileScopeWhiteList().size();i++){
								builder.value(protectData.getFileScopeWhiteList().get(i));
							}
						}
					builder.endArray().
					startArray("account_type");
						if(protectData.getAccountType() == null){
							builder.value("__ALL_EL__");
						}else{
							for(int i=0;i<protectData.getAccountType().size();i++){
								builder.value(protectData.getAccountType().get(i));
							}
						}
					builder.endArray().
					field("policy_type","filexfer").
					startObject("device_management_info").
						field("status","__ALL_EL__").
						field("posture","__ALL_EL__").
						field("ownership","__ALL_EL__").
					endObject().
					field("is_active",protectData.getStatus()).
				endObject();
		return builder.string();
	}
	
	public String getThreatScorePolicyESQuery(ProtectDTO protectData, SuiteData suiteData) throws IOException{
		String nullValue = null;
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", protectData.getPolicyName()).
					field("policy_description", "Policy Description").
					startArray("applications");
						if(protectData.getApplicationList() == null){
							builder.value(suiteData.getSaasAppName());
						}else{
							for(int i=0;i<protectData.getApplicationList().size();i++){
								builder.value(protectData.getApplicationList().get(i));
							}
						}
					builder.endArray().
					field("is_exposed", true).
					field("is_public", false).
					
					startObject("users_scope").
						startArray("users");
							if(protectData.getUserList() == null){
								builder.value("__ALL_EL__");
							}else{
								for(int i=0;i<protectData.getUserList().size();i++){
									builder.value(protectData.getUserList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupList() == null){
								if(protectData.getUserList() == null){
									builder.value("__ALL_EL__");
								}
							}else{
								for(int i=0;i<protectData.getGroupList().size();i++){
									builder.value(protectData.getGroupList().get(i));
								}
							}
						builder.endArray().
						startArray("domains");
							if(protectData.getDomainList() == null){
								builder.value("__ALL_EL__");
							}else{
								for(int i=0;i<protectData.getDomainList().size();i++){
									builder.value(protectData.getDomainList().get(i));
								}
							}
						builder.endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							if(protectData.getUserWhiteList() != null){
								for(int i=0;i<protectData.getUserWhiteList().size();i++){
									builder.value(protectData.getUserWhiteList().get(i));
								}
							}
						builder.endArray().
						startArray("groups");
							if(protectData.getGroupWhiteList() != null){
								for(int i=0;i<protectData.getGroupWhiteList().size();i++){
									builder.value(protectData.getGroupWhiteList().get(i));
								}
							}
						builder.endArray().
					endObject().
					field("risk_level", protectData.getThreadScore()).
					field("org_unit", "").
					startObject("response").
						startArray("action");
							for(int i=0;i<protectData.getActionList().size();i++){
								builder.value(protectData.getActionList().get(i));
							}
						builder.endArray().
						startArray("notify_email").
						endArray().
						startArray("notify_sms").
						endArray().
						startArray("notify_ticketing_email").
						endArray().
						field("notify_user", false).
						field("template_id", nullValue).
						field("severity_level", protectData.getSeverity()).
					endObject().
					field("violations", 1).
					field("policy_type", "anomalydetect").
					field("is_active", protectData.getStatus()).
				endObject();
		return builder.string();
	}
}
