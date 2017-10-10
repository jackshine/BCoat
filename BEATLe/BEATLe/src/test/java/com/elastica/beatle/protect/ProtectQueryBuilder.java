package com.elastica.beatle.protect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.testng.Reporter;

import com.elastica.beatle.logger.Logger;

public class ProtectQueryBuilder {

	public String getESQueryForActivityCount(String tsfrom, String tsto, String cloudService,
			String apiServerUrl, String csrfToken, String sessionId, String userId) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					startObject("source").
						startObject("query").
							startObject("filtered").
								startObject("query").
									startObject("bool").
										startArray("must").
											startObject().
												startObject("range").
													startObject("created_timestamp").
														field("from", tsfrom).
														field("to", tsto).
													endObject().
												endObject().
											endObject().
											startObject().
												startObject("term").
													field("facility", cloudService).
												endObject().
											endObject().
										endArray().
									endObject().
								endObject().
							endObject().
						endObject().
						field("from", 0).
						field("size", 30).
						startObject("sort").
							startObject("created_timestamp").
								field("order", "desc").
								field("ignore_unmapped", "true").
							endObject().
						endObject().
						startObject("facets").
							startObject("histoGreen").
								startObject("date_histogram").
									field("field", "created_timestamp").
									field("interval", "minute").
									field("time_zone", "-08:00").
									field("post_zone", "-08:00").
								endObject().
								startObject("facet_filter").
									startObject("term").
										field("severity", "informational").
									endObject().
								endObject().
							endObject().
							startObject("histoOrange").
								startObject("date_histogram").
									field("field", "created_timestamp").
									field("interval", "minute").
									field("time_zone", "-08:00").
									field("post_zone", "-08:00").
								endObject().
								startObject("facet_filter").
									startObject("term").
										field("severity", "warning").
									endObject().
								endObject().
							endObject().
							startObject("histoRed").
								startObject("date_histogram").
									field("field", "created_timestamp").
									field("interval", "minute").
									field("time_zone", "-08:00").
									field("post_zone", "-08:00").
								endObject().
								startObject("facet_filter").
									startObject("term").
										field("severity", "critical").
									endObject().
								endObject().
							endObject().
							startObject("histoYellow").
								startObject("date_histogram").
									field("field", "created_timestamp").
									field("interval", "minute").
									field("time_zone", "-08:00").
									field("post_zone", "-08:00").
								endObject().
								startObject("facet_filter").
									startObject("term").
										field("severity", "error").
									endObject().
								endObject().
							endObject().
							startObject("user").
								startObject("terms").
									field("field", "user").
									field("size", 1000).
								endObject().
							endObject().
							startObject("severity").
								startObject("terms").
									field("field", "severity").
									field("size", 1000).
								endObject().
							endObject().
							startObject("location").
								startObject("terms").
									field("field", "location").
									field("size", 1000).
								endObject().
							endObject().
							startObject("Object_type").
								startObject("terms").
									field("field", "Object_type").
									field("size", 1000).
								endObject().
							endObject().
							startObject("Activity_type").
								startObject("terms").
									field("field", "Activity_type").
									field("size", 1000).
								endObject().
							endObject().
							startObject("__source").
								startObject("terms").
									field("field", "__source").
									field("size", 1000).
								endObject().
							endObject().
							startObject("Resource_Id").
								startObject("terms").
									field("field", "Resource_Id").
									field("size", 1000).
								endObject().
							endObject().
						endObject().
					endObject().
					field("sourceName", cloudService).
					field("apiServerUrl", "https://"+apiServerUrl+"/").
					field("csrftoken", csrfToken).
					field("sessionid", sessionId).
					field("userid", userId).
					//field("userip", "54.186.103.182").
				endObject();
				
		Reporter.log("Query String for Policy Alerts"+builder.toString());
		return builder.string();
	}
	public String getESQueryForPolicyAlert(String tsfrom, String tsto, List<String> query) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					startObject("query").
						startObject("bool").
					      	startArray("must").
					      		startObject().
					      		   	startObject("range").
					      		   		startObject("created_timestamp").
					      		   			field("from", tsfrom).
					      		   			field("to", tsto).
					      		   		endObject().
							      	endObject().
							    endObject().
						      	startObject().
						      		startObject("term"). 
							      		field("Activity_type", "Policy Violation").
							      		endObject().
						      	endObject().
				      		    /*startObject().
				      		    	startObject("term"). 
				      		            field("severity", "critical").
				      		        endObject().
			      		        endObject().*/
			      		        startObject().
			      		         	startObject("term"). 
			      		         		field("policy_action", "ALERT").
			      		         	endObject().
					      		endObject();
					      		for (int i=0;i<query.size();i++) {
									  builder.startObject().
						      		     startObject("query_string"). 
						      		        field("query", query.get(i)).
						      		     endObject().
					      		     endObject();
								}
								builder.
					      	endArray().
					    endObject().
					endObject().
				field("from", "0").
				field("size", "160").
				startObject("sort").
  		     		startObject("created_timestamp").
  		     			field("order", "desc").
  		     		endObject().
  		     	endObject().
			endObject();
			Logger.info("Query String for Policy Alerts"+builder.toString());
			return builder.string();
	}
	
	public String getESQueryForDataExposureEditPolicy(String policyName, List<String> fileOwnerUser, List<String> fileGroupUser, 
			List<String> domainName, List<String> fileOwnerUserException, List<String> fileOwnerGroupException,
			List<String> sharedWithUser, List<String> sharedWithGroup, List<String> sharedWithUserException, 
			List<String> sharedWithGroupException, List<String> cloudServiceList, List<String> cloudServiceSubfeature,
			List<String> contentIQList, List<String> fileNameList, List<String> fileExceptionList, List<String> risksList, 
			List<String> fileTypeList, List<String> exposureList, int largerSize, int smallerSize, String exposureMatch,
			String policyId) throws IOException{
		
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", policyName).
					startObject("users_scope").
						startArray("users");
							for(int i=0;i<fileOwnerUser.size();i++){
								builder.value(fileOwnerUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileGroupUser.size();i++){
							builder.value(fileGroupUser.get(i));
						}
						builder.endArray().
						startArray("domains");
						for(int i=0;i<domainName.size();i++){
							builder.value(domainName.get(i));
						}
						builder.endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							for(int i=0;i<fileOwnerUserException.size();i++){
								builder.value(fileOwnerUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileOwnerGroupException.size();i++){
							builder.value(fileOwnerGroupException.get(i));
						}
						builder.endArray().
					endObject().
					startObject("recipient_scope").
						startArray("users");
							for(int i=0;i<sharedWithUser.size();i++){
								builder.value(sharedWithUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<sharedWithGroup.size();i++){
							builder.value(sharedWithGroup.get(i));
						}
						builder.endArray().
					endObject().
					startObject("recipient_scope_whitelist").
						startArray("users");
							for(int i=0;i<sharedWithUserException.size();i++){
								builder.value(sharedWithUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<sharedWithGroupException.size();i++){
							builder.value(sharedWithGroupException.get(i));
						}
						builder.endArray().
					endObject().
					startArray("applications");
						for(int i=0;i<cloudServiceList.size();i++){
							builder.value(cloudServiceList.get(i));
						}
					builder.endArray().
					startArray("sub_features");
						for(int i=0;i<cloudServiceSubfeature.size();i++){
							builder.value(cloudServiceSubfeature.get(i));
						}
					builder.endArray().
					startArray("site_urls");
					builder.endArray().
					startArray("folder_scope");
					builder.endArray().					
					startArray("content_profiles");
						for(int i=0;i<contentIQList.size();i++){
							builder.value(contentIQList.get(i));
						}
					builder.endArray().
/*					startArray("file_type");
						for(int i=0;i<fileTypeList.size();i++){
							builder.value(fileTypeList.get(i));
						}
					builder.endArray().
					startArray("file_type_whitelist");
						for(int i=0;i<fileExceptionList.size();i++){
							builder.value(fileExceptionList.get(i));
						}
					builder	.endArray().*/
					startArray("vulnerability_type");
						for(int i=0;i<risksList.size();i++){
							builder.startObject().
								field("type", risksList.get(i)).
								startObject("meta_info").
								endObject().
							endObject();
						}
					builder.endArray().
					startArray("filename_pattern");
						for(int i=0;i<fileNameList.size();i++){
							builder.value(fileNameList.get(i));
						}
					builder.endArray().
					startObject("file_size").
						field("larger_than", largerSize).
						field("smaller_than", smallerSize).
					endObject().
					startArray("exposure_scope");
						for(int i=0;i<exposureList.size();i++){
							builder.value(exposureList.get(i));
						}
					builder.endArray().
					field("exposure_match", exposureMatch).
					startArray("response").
						startObject().
							field("type", "REMEDIATE").
							startObject("meta_info").
								startArray("actions").
									value("remediation").
								endArray().	
							endObject().
						endObject().
						startObject().
							field("type", "NOTIFY_USER").
							startObject("meta_info").
								field("notify_user", true).
							endObject().
						endObject().
						startObject().
							field("type", "SEVERITY_LEVEL").
							startObject("meta_info").
								field("severity_level", "high").
							endObject().
						endObject().
					endArray().
					field("policy_id", policyId).
					field("policy_type", "documentshareapi").
					field("policy_description", "Policy Desc").
					field("is_active", false).
				endObject();
		return builder.string();
	}
	
	public String getESQueryForDataExposureEditPolicyTemp(String policyName, List<String> fileOwnerUser, List<String> fileGroupUser, 
			List<String> domainName, List<String> fileOwnerUserException, List<String> fileOwnerGroupException,
			List<String> sharedWithUser, List<String> sharedWithGroup, List<String> sharedWithUserException, 
			List<String> sharedWithGroupException, List<String> cloudServiceList, List<String> cloudServiceSubfeature,
			List<String> contentIQList, List<String> fileNameList, List<String> fileExceptionList, List<String> risksList, 
			List<String> fileTypeList, List<String> exposureList, int largerSize, int smallerSize, String exposureMatch,
			String policyId) throws IOException{
		
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", policyName).
					startObject("users_scope").
						startArray("users");
							for(int i=0;i<fileOwnerUser.size();i++){
								builder.value(fileOwnerUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileGroupUser.size();i++){
							builder.value(fileGroupUser.get(i));
						}
						builder.endArray().
						startArray("domains");
						for(int i=0;i<domainName.size();i++){
							builder.value(domainName.get(i));
						}
						builder.endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							for(int i=0;i<fileOwnerUserException.size();i++){
								builder.value(fileOwnerUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileOwnerGroupException.size();i++){
							builder.value(fileOwnerGroupException.get(i));
						}
						builder.endArray().
					endObject().
					startObject("recipient_scope").
						startArray("users");
							for(int i=0;i<sharedWithUser.size();i++){
								builder.value(sharedWithUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<sharedWithGroup.size();i++){
							builder.value(sharedWithGroup.get(i));
						}
						builder.endArray().
					endObject().
					startObject("recipient_scope_whitelist").
						startArray("users");
							for(int i=0;i<sharedWithUserException.size();i++){
								builder.value(sharedWithUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<sharedWithGroupException.size();i++){
							builder.value(sharedWithGroupException.get(i));
						}
						builder.endArray().
					endObject().
					startArray("applications");
						for(int i=0;i<cloudServiceList.size();i++){
							builder.value(cloudServiceList.get(i));
						}
					builder.endArray().
					startArray("sub_features");
						for(int i=0;i<cloudServiceSubfeature.size();i++){
							builder.value(cloudServiceSubfeature.get(i));
						}
					builder.endArray().
					startArray("site_urls");
					builder.endArray().
					startArray("folder_scope");
					builder.endArray().
					startArray("content_profiles");
						for(int i=0;i<contentIQList.size();i++){
							builder.value(contentIQList.get(i));
						}
					builder.endArray().
					startArray("file_type");
						for(int i=0;i<fileTypeList.size();i++){
							builder.value(fileTypeList.get(i));
						}
					builder.endArray().
					startArray("file_type_whitelist");
						for(int i=0;i<fileExceptionList.size();i++){
							builder.value(fileExceptionList.get(i));
						}
					builder	.endArray().
					startArray("vulnerability_type");
						for(int i=0;i<risksList.size();i++){
							builder.startObject().
								field("type", risksList.get(i)).
								startObject("meta_info").
								endObject().
							endObject();
						}
					builder.endArray().
					startArray("filename_pattern");
						for(int i=0;i<fileNameList.size();i++){
							builder.value(fileNameList.get(i));
						}
					builder.endArray().
					startObject("file_size").
						field("larger_than", largerSize).
						field("smaller_than", smallerSize).
					endObject().
					startArray("exposure_scope");
						for(int i=0;i<exposureList.size();i++){
							builder.value(exposureList.get(i));
						}
					builder.endArray().
					field("exposure_match", exposureMatch).
					startArray("response").
						startObject().
							field("type", "REMEDIATE").
							startObject("meta_info").
								startArray("actions").
									value("remediation").
								endArray().	
							endObject().
						endObject().
						startObject().
							field("type", "NOTIFY_USER").
							startObject("meta_info").
								field("notify_user", true).
							endObject().
						endObject().
					endArray().
					field("policy_id", policyId).
					field("policy_type", "documentshareapi").
					field("policy_description", "Policy Desc").
					field("is_active", true).
				endObject();
		return builder.string();
	}
	
	
	
	public String getESQueryForDataExposurePolicy(String policyName, List<String> fileOwnerUser, List<String> fileGroupUser, 
			List<String> domainName, List<String> fileOwnerUserException, List<String> fileOwnerGroupException,
			List<String> sharedWithUser, List<String> sharedWithGroup, List<String> sharedWithUserException, 
			List<String> sharedWithGroupException, List<String> cloudServiceList, List<String> cloudServiceSubfeature,
			List<String> contentIQList, List<String> fileNameList, List<String> fileExceptionList, List<String> risksList, 
			List<String> fileTypeList, List<String> exposureList, int largerSize, int smallerSize, String exposureMatch, String severity, List<String> notifySmsList, List<String> ciqProfileExceptionList) throws IOException{
		
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", policyName).
					startObject("users_scope").
						startArray("users");
							for(int i=0;i<fileOwnerUser.size();i++){
								builder.value(fileOwnerUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileGroupUser.size();i++){
							builder.value(fileGroupUser.get(i));
						}
						builder.endArray().
						startArray("domains");
						for(int i=0;i<domainName.size();i++){
							builder.value(domainName.get(i));
						}
						builder.endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							for(int i=0;i<fileOwnerUserException.size();i++){
								builder.value(fileOwnerUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileOwnerGroupException.size();i++){
							builder.value(fileOwnerGroupException.get(i));
						}
						builder.endArray().
					endObject().
					startObject("recipient_scope").
						startArray("users");
							for(int i=0;i<sharedWithUser.size();i++){
								builder.value(sharedWithUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<sharedWithGroup.size();i++){
							builder.value(sharedWithGroup.get(i));
						}
						builder.endArray().
					endObject().
					startObject("recipient_scope_whitelist").
						startArray("users");
							for(int i=0;i<sharedWithUserException.size();i++){
								builder.value(sharedWithUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<sharedWithGroupException.size();i++){
							builder.value(sharedWithGroupException.get(i));
						}
						builder.endArray().
					endObject().
					startArray("applications");
						for(int i=0;i<cloudServiceList.size();i++){
							builder.value(cloudServiceList.get(i));
						}
					builder.endArray().
					startArray("sub_features");
						for(int i=0;i<cloudServiceSubfeature.size();i++){
							builder.value(cloudServiceSubfeature.get(i));
						}
					builder.endArray().
					startArray("site_urls");
						if(cloudServiceSubfeature.get(0).equals("Sites")){
							builder.value("https://protecto365autobeatlecom.sharepoint.com/sites/protectsite");
						}
					builder.endArray().	
					startArray("folder_scope");
					builder.endArray().
					startArray("content_profiles");
						for(int i=0;i<contentIQList.size();i++){
							builder.value(contentIQList.get(i));
						}
					builder.endArray().
					startArray("content_profiles_whitelist");
						for(int i=0;i<ciqProfileExceptionList.size();i++){
							builder.value(ciqProfileExceptionList.get(i));
						}
					builder	.endArray().
					startArray("file_type");
						for(int i=0;i<fileTypeList.size();i++){
							builder.value(fileTypeList.get(i));
						}
					builder.endArray().
					startArray("file_type_whitelist").
						/*for(int i=0;i<fileExceptionList.size();i++){
							builder.value(fileExceptionList.get(i));
						}*/
					endArray().
					startArray("vulnerability_type").
						/*for(int i=0;i<risksList.size();i++){
							builder.startObject().
								field("type", risksList.get(i)).
								startObject("meta_info").
								endObject().
							endObject();
						}*/
					endArray().
					startArray("filename_pattern");
						for(int i=0;i<fileNameList.size();i++){
							builder.value(fileNameList.get(i));
						}
					builder.endArray().
					startObject("file_size").
						field("larger_than", largerSize).
						field("smaller_than", smallerSize).
					endObject().
					startArray("exposure_scope");
						for(int i=0;i<exposureList.size();i++){
							builder.value(exposureList.get(i));
						}
					builder.endArray().
					field("exposure_match", exposureMatch).
					startArray("response").
						startObject().
							field("type", "REMEDIATE").
							startObject("meta_info").
								startArray("actions").
									value("remediation").
								endArray().	
							endObject().
						endObject().
						startObject().
							field("type", "NOTIFY_USER").
							startObject("meta_info").
								field("notify_user", true).
							endObject().
						endObject();
						if(notifySmsList.size()>0){
							builder.startObject().
							field("type", "NOTIFY_SMS").
								startObject("meta_info").
								startArray("notify_sms");
									for(int i=0;i<notifySmsList.size();i++){
										builder.value(notifySmsList.get(i));
									}
									builder.endArray().
								endObject().
							endObject();
						}
						
						builder.startObject().
							field("type", "SEVERITY_LEVEL").
							startObject("meta_info");
								if(severity == null){
									builder.field("severity_level", "high");
								}else{
									builder.field("severity_level", severity);
								}
							builder.endObject().
						endObject().
					endArray().
					field("is_active", false).
					field("policy_type", "documentshareapi").
					field("policy_description", "Policy Desc").
				endObject();
		return builder.string();
	}
	
	public String getESQueryForDataExposurePolicyTemp(String policyName, List<String> fileOwnerUser, List<String> fileGroupUser, 
			List<String> domainName, List<String> fileOwnerUserException, List<String> fileOwnerGroupException,
			List<String> sharedWithUser, List<String> sharedWithGroup, List<String> sharedWithUserException, 
			List<String> sharedWithGroupException, List<String> cloudServiceList, List<String> cloudServiceSubfeature,
			List<String> contentIQList, List<String> fileNameList, List<String> fileExceptionList, List<String> risksList, 
			List<String> fileTypeList, List<String> exposureList, int largerSize, int smallerSize, String exposureMatch, String severity, List<String> notifySmsList) throws IOException{
		
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", policyName).
					startObject("users_scope").
						startArray("users");
							for(int i=0;i<fileOwnerUser.size();i++){
								builder.value(fileOwnerUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileGroupUser.size();i++){
							builder.value(fileGroupUser.get(i));
						}
						builder.endArray().
						startArray("domains");
						for(int i=0;i<domainName.size();i++){
							builder.value(domainName.get(i));
						}
						builder.endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							for(int i=0;i<fileOwnerUserException.size();i++){
								builder.value(fileOwnerUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileOwnerGroupException.size();i++){
							builder.value(fileOwnerGroupException.get(i));
						}
						builder.endArray().
					endObject().
					startObject("recipient_scope").
						startArray("users");
							for(int i=0;i<sharedWithUser.size();i++){
								builder.value(sharedWithUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<sharedWithGroup.size();i++){
							builder.value(sharedWithGroup.get(i));
						}
						builder.endArray().
					endObject().
					startObject("recipient_scope_whitelist").
						startArray("users");
							for(int i=0;i<sharedWithUserException.size();i++){
								builder.value(sharedWithUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<sharedWithGroupException.size();i++){
							builder.value(sharedWithGroupException.get(i));
						}
						builder.endArray().
					endObject().
					startArray("applications");
						for(int i=0;i<cloudServiceList.size();i++){
							builder.value(cloudServiceList.get(i));
						}
					builder.endArray().
					startArray("sub_features");
						for(int i=0;i<cloudServiceSubfeature.size();i++){
							builder.value(cloudServiceSubfeature.get(i));
						}
					builder.endArray().
					startArray("site_urls");
					builder.endArray().	
					startArray("folder_scope");
					builder.endArray().
					startArray("content_profiles");
						for(int i=0;i<contentIQList.size();i++){
							builder.value(contentIQList.get(i));
						}
					builder.endArray().
					startArray("file_type");
					for(int i=0;i<fileTypeList.size();i++){
						builder.value(fileTypeList.get(i));
					}
					builder.endArray().
					startArray("file_type_whitelist");
					for(int i=0;i<fileExceptionList.size();i++){
						builder.value(fileExceptionList.get(i));
					}
					builder.endArray().
					startArray("vulnerability_type");
						for(int i=0;i<risksList.size();i++){
							builder.startObject().
								field("type", risksList.get(i)).
								startObject("meta_info").
								endObject().
							endObject();
						}
					builder.endArray().
					startArray("filename_pattern");
						for(int i=0;i<fileNameList.size();i++){
							builder.value(fileNameList.get(i));
						}
					builder.endArray().
					startObject("file_size").
						field("larger_than", largerSize).
						field("smaller_than", smallerSize).
					endObject().
					startArray("exposure_scope");
						for(int i=0;i<exposureList.size();i++){
							builder.value(exposureList.get(i));
						}
					builder.endArray().
					field("exposure_match", exposureMatch).
					startArray("response").
						startObject().
							field("type", "REMEDIATE").
							startObject("meta_info").
								startArray("actions").
									value("remediation").
								endArray().	
							endObject().
						endObject().
						startObject().
							field("type", "NOTIFY_USER").
							startObject("meta_info").
								field("notify_user", true).
							endObject().
						endObject().
						
						
						startObject().
						field("type", "NOTIFY_SMS").
							startObject("meta_info").
							startArray("notify_sms");
								for(int i=0;i<notifySmsList.size();i++){
									builder.value(notifySmsList.get(i));
								}
								builder.endArray().
							endObject().
						endObject().						
						
						
						startObject().
							field("type", "SEVERITY_LEVEL").
							startObject("meta_info");
								if(severity == null){
									builder.field("severity_level", "high");
								}else{
									builder.field("severity_level", severity);
								}
							builder.endObject().
						endObject().
					endArray().
					field("is_active", false).
					field("policy_type", "documentshareapi").
					field("policy_description", "Policy Desc").
				endObject();
		return builder.string();
	}
	
	public String getESQueryForAccessMonitoringPolicy(String policyName, int riskValue, Map<String, ArrayList<String>> objectActivities,
			List<String> fileOwnerUser, List<String> fileGroupUser, List<String> domainName, List<String> fileOwnerUserException, 
			List<String> fileOwnerGroupException, List<String> cloudServiceList, List<String> cloudServiceSubfeature) throws IOException{
		ArrayList<String> objectList = new ArrayList<String>();
		objectList.add("File");
		objectList.add("Folder");
		objectList.add("User");
		objectList.add("Group");
		objectList.add("Role");
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", policyName).
					field("policy_description", "Access Monitoring Policy Description").
					startArray("applications");
						for(int i=0;i<cloudServiceList.size();i++){
							builder.value(cloudServiceList.get(i));
						}
					builder.endArray().
					startArray("sub_features");
						for(int i=0;i<cloudServiceSubfeature.size();i++){
							builder.value(cloudServiceSubfeature.get(i));
						}
					builder.endArray().
					startArray("site_urls");
					builder.endArray().
					startArray("folder_scope");
					builder.endArray().
					startObject("users_scope").
						startArray("users");
							for(int i=0;i<fileOwnerUser.size();i++){
								builder.value(fileOwnerUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileGroupUser.size();i++){
							builder.value(fileGroupUser.get(i));
						}
						builder.endArray().
						startArray("domains");
						for(int i=0;i<domainName.size();i++){
							builder.value(domainName.get(i));
						}
						builder.endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							for(int i=0;i<fileOwnerUserException.size();i++){
								builder.value(fileOwnerUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileOwnerGroupException.size();i++){
							builder.value(fileOwnerGroupException.get(i));
						}
						builder.endArray().
					endObject().
					field("risk_level", riskValue).
					startArray("response").
						startObject().
							field("type", "NOTIFY_USER").
							startObject("meta_info").
							field("notify_user", true).
							endObject().
						endObject().
					endArray().
					field("violations", 1).
					startArray("geoip_scope").
						value("").
					endArray().
					startArray("geoip_whitelist").
						value("").
					endArray().
					startArray("platform").
						value("").
					endArray().
					startArray("platform_whitelist").
						value("").
					endArray().
					startArray("browser").
						value("").
					endArray().
					startArray("browser_whitelist").
						value("").
					endArray().
					startArray("activities_scope");
						for(int j=0; j<objectList.size(); j++){
							if(objectActivities.containsKey(objectList.get(j))){
								builder.startObject().
									startArray(objectList.get(j));
										for(int k=0; k<objectActivities.get(objectList.get(j)).size(); k++){
											builder.value(objectActivities.get(objectList.get(j)).get(k));
										}
									builder.endArray().
								endObject();	
							}
						}
					builder.endArray().
					startArray("account_type").
						value("").
					endArray().
					field("policy_type", "accessenforceapi").
					field("is_active", false).
				endObject();	
		return builder.string();
	}
	
	public String getESQueryForAccessMonitoringEditPolicy(String policyName, String policyId, int riskValue, Map<String, ArrayList<String>> objectActivities,
			List<String> fileOwnerUser, List<String> fileGroupUser, List<String> domainName, List<String> fileOwnerUserException, 
			List<String> fileOwnerGroupException, List<String> cloudServiceList, List<String> cloudServiceSubfeature) throws IOException{
		ArrayList<String> objectList = new ArrayList<String>();
		objectList.add("File");
		objectList.add("Folder");
		objectList.add("User");
		objectList.add("Group");
		objectList.add("Role");
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", policyName).
					field("policy_description", "Access Monitoring Policy Description").
					startArray("applications");
						for(int i=0;i<cloudServiceList.size();i++){
							builder.value(cloudServiceList.get(i));
						}
					builder.endArray().
					startArray("sub_features");
						for(int i=0;i<cloudServiceSubfeature.size();i++){
							builder.value(cloudServiceSubfeature.get(i));
						}
					builder.endArray().
					startArray("site_urls");
					builder.endArray().
					startArray("folder_scope");
					builder.endArray().
					startObject("users_scope").
						startArray("users");
							for(int i=0;i<fileOwnerUser.size();i++){
								builder.value(fileOwnerUser.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileGroupUser.size();i++){
							builder.value(fileGroupUser.get(i));
						}
						builder.endArray().
						startArray("domains");
						for(int i=0;i<domainName.size();i++){
							builder.value(domainName.get(i));
						}
						builder.endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							for(int i=0;i<fileOwnerUserException.size();i++){
								builder.value(fileOwnerUserException.get(i));
							}
						builder.endArray().
						startArray("groups");
						for(int i=0;i<fileOwnerGroupException.size();i++){
							builder.value(fileOwnerGroupException.get(i));
						}
						builder.endArray().
					endObject().
					field("risk_level", riskValue).
					startArray("response").
						startObject().
							field("type", "NOTIFY_USER").
							startObject("meta_info").
							field("notify_user", true).
							endObject().
						endObject().
					endArray().
					field("violations", 1).
					startArray("geoip_scope").
						value("").
					endArray().
					startArray("geoip_whitelist").
						value("").
					endArray().
					startArray("platform").
						value("").
					endArray().
					startArray("platform_whitelist").
						value("").
					endArray().
					startArray("browser").
						value("").
					endArray().
					startArray("browser_whitelist").
						value("").
					endArray().
					startArray("activities_scope");
						for(int j=0; j<objectList.size(); j++){
							if(objectActivities.containsKey(objectList.get(j))){
								builder.startObject().
									startArray(objectList.get(j));
										for(int k=0; k<objectActivities.get(objectList.get(j)).size(); k++){
											builder.value(objectActivities.get(objectList.get(j)).get(k));
										}
									builder.endArray().
								endObject();	
							}
						}
					builder.endArray().
					startArray("account_type").
						value("").
					endArray().
					field("policy_id", policyId).
					field("policy_type", "accessenforceapi").
					field("is_active", false).
				endObject();	
		return builder.string();
	}
	
	public String getESQueryForThreatScorePolicy(PolicyBean policyBean, List<String> fileOwnerUserException) throws IOException{
		String nullValue = null;
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("policy_name", policyBean.getPolicyName()).
					field("policy_description", "ThreatScore Policy").
					startArray("applications").
						value(policyBean.getCloudService()).		
					endArray().
					startObject("users_scope").
						startArray("users").
							value(policyBean.getFileOwnerUser()).		
						endArray().
						startArray("groups").
							value(policyBean.getFileOwnerGroup()).		
						endArray().
						startArray("domains").
							value(policyBean.getDomainName()).		
						endArray().
					endObject().
					startObject("users_scope_whitelist").
						startArray("users");
							for(int i=0;i<fileOwnerUserException.size();i++){
								builder.value(fileOwnerUserException.get(i));
							}
						builder.endArray().						
						
						startArray("groups").
							value(policyBean.getFileOwnerGroupException()).		
						endArray().
					endObject().
					field("risk_level", policyBean.getThreatScore()).
					startObject("response").
						startArray("action").
							value(policyBean.getBlock()).		
						endArray().
						startArray("notify_email").
							value("").		
						endArray().
						startArray("notify_sms").
							value("").		
						endArray().
						startArray("notify_ticketing_email").
							value("").		
						endArray().
						field("notify_user", false).
						field("template_id", nullValue).
					endObject().
					field("violations", 1).
					field("policy_type", "anomalydetect").
					field("is_active", false).
				endObject();
		Logger.info("Query String for Policy Alerts"+builder.toString());
		return builder.string();
	}
	
	
	public String getESQueryForContentIQ(String ciqProfileName, String source, List<String> values) throws IOException{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("description", "CIQ Description").
					field("name", ciqProfileName).
					field("severity", "high").
					startArray("groups").
						startObject().
							field("operator", "AND").
							startArray("operands").
								startObject().
									field("name", "").
									field("is_not", false).
									startArray("value");
										for(int i=0;i<values.size();i++){
											builder.startObject().
												field("key", values.get(i)).
												field("min_count", 1).
											endObject();
										}
									builder.endArray().
									field("source", source).
								endObject().
							endArray().
						endObject().
					endArray().
					field("appliesToSecurlets", false).
					field("api_enabled", true).
					startArray("domains").
						value("__ALL_EL__").
					endArray().
				endObject();
		Logger.info("Query String for Policy Alerts"+builder.toString());
		return builder.string();
	}
	
	public String generatePayloadForDeleteCIQProfile(String ciqid) throws IOException{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("url", "contentprofiles").
					field("id", ciqid).
					field("action", "delete").
				endObject();
		return builder.string();
	}
	
	public String generatePayloadForListCIQProfile() throws IOException{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					field("url", "contentprofiles").
					field("id", "").
					field("action", "list").
					startObject("params").
						field("limit", 0).
						field("format", "json").
					endObject().
				endObject();
		return builder.string();
	}
	
	public String generatePayloadForCalculateImpact(PolicyBean policyBean) throws IOException{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					startArray("owned_by");
						if(policyBean.getFileOwnerUser() != null){
							builder.value(policyBean.getFileOwnerUser());
						}
					builder.endArray().
					startArray("owned_by_exclude");
						if(policyBean.getFileOwnerUserException() != "no"){
							builder.value(policyBean.getFileOwnerUserException());
						}
					builder.endArray();
					if(policyBean.getCloudService().contains("Office 365")){
						builder.field("cloud_app", "Office 365");
					}
					builder.startArray("exposures");
						if(policyBean.getExposureType().contains(",")){
							String [] exposures = policyBean.getExposureType().split(",");
							for(int i=0; i<exposures.length; i++){
								System.out.println("."+exposures[i]+".");
								if(!exposures[i].contains("any") && !exposures[i].contains("all")){
									builder.value(exposures[i]);
								}
							}
						}else{
							builder.value(policyBean.getExposureType());
						}
					builder.endArray().
					startArray("collabs").
						//
					endArray().
					field("collabs_exclude", false).
					startArray("name").
						value(policyBean.getFileName()).
					endArray().
					startArray("format").
						//
					endArray().
					field("format_exclude", false).
					startArray("content_checks").
						//
					endArray().
					startArray("content_iq_profiles");
						if(policyBean.getCiqProfile().contains(",")){
							String [] ciqProfiles = policyBean.getCiqProfile().split(",");
							for(int i=0; i<ciqProfiles.length; i++){
								builder.value(ciqProfiles[i]);
							}
						}else{
							builder.value(policyBean.getCiqProfile());
						}
					builder.endArray().
					startObject("file_size").
					endObject().
					field("limit", 20).
					field("offset", 0).
					field("policyType", "documentshareapi");
					
					if(policyBean.getCloudService().equals("Office 365 Site")){
						builder.field("object_type", "Sites").
						startArray("site_urls").
							//value(policyBean.getSite()).
						endArray();
					}
					
					builder.startArray("domains").
						value("__ALL_EL__").
					endArray();
					if(policyBean.getExposureType().contains("all")){
						builder.field("is_any_exposure", "all");
					}else{
						builder.field("is_any_exposure", "any");
					}
					if(policyBean.getExposureType().contains("public")){
						builder.field("is_public", true);
					}else{
						builder.field("is_public", false);
					}
					builder.startArray("folder_scope").
					endArray().
					startArray("content_iq_profiles_exceptions");
						if(policyBean.getCiqProfileException() != null){
							if(policyBean.getCiqProfileException().contains(",")){
								String[] profiles = policyBean.getCiqProfileException().split(",");
								for(int i=0;i<profiles.length; i++){
									builder.value(profiles[i]);
								}
							}else{
								builder.value(policyBean.getCiqProfileException());
							}
						}
					builder.endArray().
					field("search_text", "").
				endObject();
		return builder.string();
	}
}
