package com.elastica.beatle.splunk.GWTests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.testng.Reporter;

import com.elastica.beatle.logger.Logger;

public class GateletQueryBuilder {

	public String pendingDeactivateGateletQuery(String appName, Map<String, String>appAccounts, List<String> category, 
			List<String> policyBlacklist, Map<String, String> gateletData) throws IOException{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					startObject("app").
						field("api_activated", true).
						field("pending_delete", false).
						field("app_name", appName).
						field("policy_capable", true).
						field("video2_desc", gateletData.get(GateletStatusConstants.VIDEO2_DESC)).
						startArray("app_accounts").
							startObject();
							Iterator appAccountsIterator = appAccounts.entrySet().iterator();
							while(appAccountsIterator.hasNext()){
								Map.Entry appAccountPair = (Map.Entry)appAccountsIterator.next();
								builder.field(appAccountPair.getKey().toString(), appAccountPair.getValue().toString());
								/*field("domain", domain).
								field("admin_email", adminEmail).
								field("account_name", accountName).
								field("activation_time", activationTime).
								field("account_id", accountId).*/
							}
							builder.endObject().
						endArray().
						field("epd_less_category", gateletData.get(GateletStatusConstants.EPD_LESS_CATEGORY)).
						field("is_multi_account", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_MULTI_ACCOUNT))). //boolean
						field("gateway_subscription_id", gateletData.get(GateletStatusConstants.GATEWAY_SUBSCRIPTION_ID)).
						startArray("category");
						for(int i=0;i<category.size();i++){
							builder.value(category.get(i));
							/*value("File  Sharing").
							value("Storage").*/
						}
						builder.endArray().
						
						field("own_dashboard", Boolean.valueOf(gateletData.get(GateletStatusConstants.OWN_DASHBOARD))).   //boolean
						field("id", gateletData.get(GateletStatusConstants.ID)).
						field("video4_desc", gateletData.get(GateletStatusConstants.VIDEO4_DESC)).
						field("is_api_subscribed", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_API_SUBSCRIBED))).    //boolean
						field("video3_image_url", gateletData.get(GateletStatusConstants.VIDEO3_IMAGE_URL)).
						field("apparazzi_service_id", Integer.parseInt(gateletData.get(GateletStatusConstants.APPARAZZI_SERVICE_ID))).   //int
						field("display_in_showcase", Boolean.valueOf(gateletData.get(GateletStatusConstants.DISPLAY_IN_SHOWCASE))).    //boolean
						field("in_beta", Boolean.valueOf(gateletData.get(GateletStatusConstants.IN_BETA))).   //boolean
						field("tagline", gateletData.get(GateletStatusConstants.TAGLINE)).
						field("epd_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.EPD_CAPABLE))).    //boolean
						field("short_desc", gateletData.get(GateletStatusConstants.SHORT_DESC)).
						field("small_icon_uri", gateletData.get(GateletStatusConstants.SMALL_ICON_URI)).
						field("epd_less_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.EPD_LESS_CAPABLE))).   //false
						field("screenshot3_uri", gateletData.get(GateletStatusConstants.SCREENSHOT3_URI)).
						field("video2_image_url", gateletData.get(GateletStatusConstants.VIDEO2_IMAGE_URL)).
						field("api_policy_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.API_POLICY_CAPABLE))).   //boolean
						field("video3_url", gateletData.get(GateletStatusConstants.VIDEO3_URL)).
						field("generic_epd", Boolean.valueOf(gateletData.get(GateletStatusConstants.GENERIC_EPD))).   //boolean
						field("subscription_id", gateletData.get(GateletStatusConstants.SUBSCRIPTION_ID)).
						field("api_key", gateletData.get(GateletStatusConstants.API_KEY)).
						field("api_on_demand_scan_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.API_ON_DEMAND_SCAN_CAPABLE))).   //boolean
						field("video4_url", gateletData.get(GateletStatusConstants.VIDEO4_URL)).
						field("screenshot1_uri", gateletData.get(GateletStatusConstants.SCREENSHOT1_URI)).
						field("long_desc", gateletData.get(GateletStatusConstants.LONG_DESC)).
						field("screenshot5_uri", gateletData.get(GateletStatusConstants.SCREENSHOT5_URI)).
						startArray("policy_blacklist");
						for(int i=0;i<policyBlacklist.size();i++){
							builder.value(policyBlacklist.get(i));
						}
						builder.endArray().
						field("signup_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.SIGNUP_CAPABLE))). //boolean
						startArray("category_key");
						for(int i=0;i<category.size();i++){
							builder.value(category.get(i));
							/*value("File  Sharing").
							value("Storage").*/
						}
						builder.endArray().
						field("video1_desc", gateletData.get(GateletStatusConstants.VIDEO1_DESC)).
						field("price", Integer.parseInt(gateletData.get(GateletStatusConstants.PRICE))).  //int
						field("is_active", gateletData.get(GateletStatusConstants.IS_ACTIVE)).   //boolean
						field("video4_image_url", gateletData.get(GateletStatusConstants.VIDEO4_IMAGE_URL)).
						field("header_bg_color", gateletData.get(GateletStatusConstants.HEADER_BG_COLOR)).
						field("display_in_store", Boolean.valueOf(gateletData.get(GateletStatusConstants.DISPLAY_IN_STORE))).   //boolean
						field("is_available", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_AVAILABLE))).   //boolean
						field("is_alerting", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_ALERTING))).  //boolean
						field("modified_on", gateletData.get(GateletStatusConstants.MODIFIED_ON)).
						field("screenshot2_uri", gateletData.get(GateletStatusConstants.SCREENSHOT2_URI)).
						field("is_blocked", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_BLOCKED))).   //boolean
						field("is_gateway_subscribed", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_GATEWAY_SUBSCRIBED))).   //boolean
						field("long_desc1", gateletData.get(GateletStatusConstants.LONG_DESC1)).
						field("lang", gateletData.get(GateletStatusConstants.LANG)).
						field("video3_desc", gateletData.get(GateletStatusConstants.VIDEO3_DESC)).
						field("name", gateletData.get(GateletStatusConstants.NAME)).
						field("long_desc2", gateletData.get(GateletStatusConstants.LONG_DESC2)).
						field("is_gateway_subscription_requested", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_GATEWAY_SUBSCRIPTION_REQUESTED))).   //boolean
						field("screenshot4_uri", gateletData.get(GateletStatusConstants.SCREENSHOT4_URI)).
						field("big_icon_uri", gateletData.get(GateletStatusConstants.BIG_ICON_URI)).
						field("popularity", Integer.parseInt(gateletData.get(GateletStatusConstants.POPULARITY))).   //int
						field("addl_params_needed", Boolean.valueOf(gateletData.get(GateletStatusConstants.ADDL_PARAMS_NEEDED))).  //boolean
						field("is_api_subscription_requested", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_API_SUBSCRIPTION_REQUESTED))).   //boolean
						field("risk_status", gateletData.get(GateletStatusConstants.RISK_STATUS)).
						field("video1_image_url", gateletData.get(GateletStatusConstants.VIDEO1_IMAGE_URL)).
						field("hosts", gateletData.get(GateletStatusConstants.HOSTS)).
						field("api_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.API_CAPABLE))).   //boolean
						field("support_gateway_encryption", Boolean.valueOf(gateletData.get(GateletStatusConstants.SUPPORT_GATEWAY_ENCRYPTION))).   //boolean
						field("tenant_app_id", gateletData.get(GateletStatusConstants.TENANT_APP_ID)).
						field("resource_uri", gateletData.get(GateletStatusConstants.RESOURCE_URI)).
						field("pac_urls", gateletData.get(GateletStatusConstants.PAC_URLS)).
						field("video1_url", gateletData.get(GateletStatusConstants.VIDEO1_URL)).
						field("video2_url", gateletData.get(GateletStatusConstants.VIDEO2_URL)).
						
						
						field("_id", appName.toLowerCase()).
						field("_hasDetails", true).  //boolean
						field("_smallIconUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.SMALL_ICON_URI)).
						field("_activated", true).   //boolean
						field("_bigIconUrl", "/static/images/store/logos/"+gateletData.get(GateletStatusConstants.BIG_ICON_URI)).
						field("_canAddViaGateway", false).   //boolean
						field("_actionButtonLabel", "Configure").
						field("_apiActionButtonLabel", "Configure").
						field("_gatewayActionButtonLabel", "Deactivate").
						field("_mainDesc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[0]).
						startArray("videos").
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO1_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO1_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[1]).
							endObject().
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO2_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO2_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[2]).
							endObject().
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO3_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO3_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[3]).
							endObject().
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO4_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO4_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[4]).
							endObject().
						endArray().
					endObject().
					field("status", "pending").
				endObject();
		
		Reporter.log("Query String for Policy Alerts"+builder.toString());
		return builder.string();
	}
	
	public String deactivateFinalizeGateletQuery(String appName, Map<String, String>appAccounts, List<String> category, 
			List<String> policyBlacklist, Map<String, String> gateletData) throws IOException{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					startObject("app").
						field("api_activated", true).
						field("pending_delete", true).
						field("app_name", appName).
						field("policy_capable", true).
						field("video2_desc", gateletData.get(GateletStatusConstants.VIDEO2_DESC)).
						startArray("app_accounts").
							startObject();
							Iterator appAccountsIterator = appAccounts.entrySet().iterator();
							while(appAccountsIterator.hasNext()){
								Map.Entry appAccountPair = (Map.Entry)appAccountsIterator.next();
								builder.field(appAccountPair.getKey().toString(), appAccountPair.getValue().toString());
								/*field("domain", domain).
								field("admin_email", adminEmail).
								field("account_name", accountName).
								field("activation_time", activationTime).
								field("account_id", accountId).*/
							}
							builder.endObject().
						endArray().
						field("epd_less_category", gateletData.get(GateletStatusConstants.EPD_LESS_CATEGORY)).
						field("is_multi_account", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_MULTI_ACCOUNT))). //boolean
						field("gateway_subscription_id", gateletData.get(GateletStatusConstants.GATEWAY_SUBSCRIPTION_ID)).
						startArray("category");
						for(int i=0;i<category.size();i++){
							builder.value(category.get(i));
							/*value("File  Sharing").
							value("Storage").*/
						}
						builder.endArray().
						
						field("own_dashboard", Boolean.valueOf(gateletData.get(GateletStatusConstants.OWN_DASHBOARD))).   //boolean
						field("id", gateletData.get(GateletStatusConstants.ID)).
						field("video4_desc", gateletData.get(GateletStatusConstants.VIDEO4_DESC)).
						field("is_api_subscribed", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_API_SUBSCRIBED))).    //boolean
						field("video3_image_url", gateletData.get(GateletStatusConstants.VIDEO3_IMAGE_URL)).
						field("apparazzi_service_id", Integer.parseInt(gateletData.get(GateletStatusConstants.APPARAZZI_SERVICE_ID))).   //int
						field("display_in_showcase", Boolean.valueOf(gateletData.get(GateletStatusConstants.DISPLAY_IN_SHOWCASE))).    //boolean
						field("in_beta", Boolean.valueOf(gateletData.get(GateletStatusConstants.IN_BETA))).   //boolean
						field("tagline", gateletData.get(GateletStatusConstants.TAGLINE)).
						field("epd_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.EPD_CAPABLE))).    //boolean
						field("short_desc", gateletData.get(GateletStatusConstants.SHORT_DESC)).
						field("small_icon_uri", gateletData.get(GateletStatusConstants.SMALL_ICON_URI)).
						field("epd_less_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.EPD_LESS_CAPABLE))).   //false
						field("screenshot3_uri", gateletData.get(GateletStatusConstants.SCREENSHOT3_URI)).
						field("video2_image_url", gateletData.get(GateletStatusConstants.VIDEO2_IMAGE_URL)).
						field("api_policy_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.API_POLICY_CAPABLE))).   //boolean
						field("video3_url", gateletData.get(GateletStatusConstants.VIDEO3_URL)).
						field("generic_epd", Boolean.valueOf(gateletData.get(GateletStatusConstants.GENERIC_EPD))).   //boolean
						field("subscription_id", gateletData.get(GateletStatusConstants.SUBSCRIPTION_ID)).
						field("api_key", gateletData.get(GateletStatusConstants.API_KEY)).
						field("api_on_demand_scan_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.API_ON_DEMAND_SCAN_CAPABLE))).   //boolean
						field("video4_url", gateletData.get(GateletStatusConstants.VIDEO4_URL)).
						field("screenshot1_uri", gateletData.get(GateletStatusConstants.SCREENSHOT1_URI)).
						field("long_desc", gateletData.get(GateletStatusConstants.LONG_DESC)).
						field("screenshot5_uri", gateletData.get(GateletStatusConstants.SCREENSHOT5_URI)).
						startArray("policy_blacklist");
						for(int i=0;i<policyBlacklist.size();i++){
							builder.value(policyBlacklist.get(i));
						}
						builder.endArray().
						field("signup_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.SIGNUP_CAPABLE))). //boolean
						startArray("category_key");
						for(int i=0;i<category.size();i++){
							builder.value(category.get(i));
							/*value("File  Sharing").
							value("Storage").*/
						}
						builder.endArray().
						field("video1_desc", gateletData.get(GateletStatusConstants.VIDEO1_DESC)).
						field("price", Integer.parseInt(gateletData.get(GateletStatusConstants.PRICE))).  //int
						field("is_active", gateletData.get(GateletStatusConstants.IS_ACTIVE)).   //boolean
						field("video4_image_url", gateletData.get(GateletStatusConstants.VIDEO4_IMAGE_URL)).
						field("header_bg_color", gateletData.get(GateletStatusConstants.HEADER_BG_COLOR)).
						field("display_in_store", Boolean.valueOf(gateletData.get(GateletStatusConstants.DISPLAY_IN_STORE))).   //boolean
						field("is_available", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_AVAILABLE))).   //boolean
						field("is_alerting", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_ALERTING))).  //boolean
						field("modified_on", gateletData.get(GateletStatusConstants.MODIFIED_ON)).
						field("screenshot2_uri", gateletData.get(GateletStatusConstants.SCREENSHOT2_URI)).
						field("is_blocked", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_BLOCKED))).   //boolean
						field("is_gateway_subscribed", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_GATEWAY_SUBSCRIBED))).   //boolean
						field("long_desc1", gateletData.get(GateletStatusConstants.LONG_DESC1)).
						field("lang", gateletData.get(GateletStatusConstants.LANG)).
						field("video3_desc", gateletData.get(GateletStatusConstants.VIDEO3_DESC)).
						field("name", gateletData.get(GateletStatusConstants.NAME)).
						field("long_desc2", gateletData.get(GateletStatusConstants.LONG_DESC2)).
						field("is_gateway_subscription_requested", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_GATEWAY_SUBSCRIPTION_REQUESTED))).   //boolean
						field("screenshot4_uri", gateletData.get(GateletStatusConstants.SCREENSHOT4_URI)).
						field("big_icon_uri", gateletData.get(GateletStatusConstants.BIG_ICON_URI)).
						field("popularity", Integer.parseInt(gateletData.get(GateletStatusConstants.POPULARITY))).   //int
						field("addl_params_needed", Boolean.valueOf(gateletData.get(GateletStatusConstants.ADDL_PARAMS_NEEDED))).  //boolean
						field("is_api_subscription_requested", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_API_SUBSCRIPTION_REQUESTED))).   //boolean
						field("risk_status", gateletData.get(GateletStatusConstants.RISK_STATUS)).
						field("video1_image_url", gateletData.get(GateletStatusConstants.VIDEO1_IMAGE_URL)).
						field("hosts", gateletData.get(GateletStatusConstants.HOSTS)).
						field("api_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.API_CAPABLE))).   //boolean
						field("support_gateway_encryption", Boolean.valueOf(gateletData.get(GateletStatusConstants.SUPPORT_GATEWAY_ENCRYPTION))).   //boolean
						field("tenant_app_id", gateletData.get(GateletStatusConstants.TENANT_APP_ID)).
						field("resource_uri", gateletData.get(GateletStatusConstants.RESOURCE_URI)).
						field("pac_urls", gateletData.get(GateletStatusConstants.PAC_URLS)).
						field("video1_url", gateletData.get(GateletStatusConstants.VIDEO1_URL)).
						field("video2_url", gateletData.get(GateletStatusConstants.VIDEO2_URL)).
						
						
						field("_id", appName.toLowerCase()).
						field("_hasDetails", true).  //boolean
						field("_smallIconUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.SMALL_ICON_URI)).
						field("_activated", true).   //boolean
						field("_bigIconUrl", "/static/images/store/logos/"+gateletData.get(GateletStatusConstants.BIG_ICON_URI)).
						field("_canAddViaGateway", false).   //boolean
						field("_actionButtonLabel", "Configure").
						field("_apiActionButtonLabel", "Configure").
						field("_gatewayActionButtonLabel", "Deactivate").
						field("_mainDesc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[0]).
						startArray("videos").
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO1_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO1_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[1]).
							endObject().
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO2_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO2_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[2]).
							endObject().
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO3_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO3_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[3]).
							endObject().
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO4_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO4_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[4]).
							endObject().
						endArray().
					endObject().
					field("status", "deactivate").
				endObject();
		
		Reporter.log("Query String for Policy Alerts"+builder.toString());
		return builder.string();
	}
	
	public String activateGateletQuery(String appName, Map<String, String>appAccounts, List<String> category, 
			List<String> policyBlacklist, Map<String, String> gateletData) throws IOException{
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
					startObject("app").
						field("api_activated", true).
						field("pending_delete", false).
						field("app_name", appName).
						field("policy_capable", true).
						field("video2_desc", gateletData.get(GateletStatusConstants.VIDEO2_DESC)).
						startArray("app_accounts");
						if(appAccounts.size()>0){
							builder.startObject();
							Iterator appAccountsIterator = appAccounts.entrySet().iterator();
							while(appAccountsIterator.hasNext()){
								Map.Entry appAccountPair = (Map.Entry)appAccountsIterator.next();
								builder.field(appAccountPair.getKey().toString(), appAccountPair.getValue().toString());
								/*field("domain", domain).
								field("admin_email", adminEmail).
								field("account_name", accountName).
								field("activation_time", activationTime).
								field("account_id", accountId).*/
							}
							builder.endObject();
						}
						builder.endArray().
						field("epd_less_category", gateletData.get(GateletStatusConstants.EPD_LESS_CATEGORY)).
						field("is_multi_account", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_MULTI_ACCOUNT))). //boolean
						field("gateway_subscription_id", gateletData.get(GateletStatusConstants.GATEWAY_SUBSCRIPTION_ID)).
						startArray("category");
						for(int i=0;i<category.size();i++){
							builder.value(category.get(i));
							/*value("File  Sharing").
							value("Storage").*/
						}
						builder.endArray().
						
						field("own_dashboard", Boolean.valueOf(gateletData.get(GateletStatusConstants.OWN_DASHBOARD))).   //boolean
						field("id", gateletData.get(GateletStatusConstants.ID)).
						field("video4_desc", gateletData.get(GateletStatusConstants.VIDEO4_DESC)).
						field("is_api_subscribed", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_API_SUBSCRIBED))).    //boolean
						field("video3_image_url", gateletData.get(GateletStatusConstants.VIDEO3_IMAGE_URL)).
						field("apparazzi_service_id", Integer.parseInt(gateletData.get(GateletStatusConstants.APPARAZZI_SERVICE_ID))).   //int
						field("display_in_showcase", Boolean.valueOf(gateletData.get(GateletStatusConstants.DISPLAY_IN_SHOWCASE))).    //boolean
						field("in_beta", Boolean.valueOf(gateletData.get(GateletStatusConstants.IN_BETA))).   //boolean
						field("tagline", gateletData.get(GateletStatusConstants.TAGLINE)).
						field("epd_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.EPD_CAPABLE))).    //boolean
						field("short_desc", gateletData.get(GateletStatusConstants.SHORT_DESC)).
						field("small_icon_uri", gateletData.get(GateletStatusConstants.SMALL_ICON_URI)).
						field("epd_less_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.EPD_LESS_CAPABLE))).   //false
						field("screenshot3_uri", gateletData.get(GateletStatusConstants.SCREENSHOT3_URI)).
						field("video2_image_url", gateletData.get(GateletStatusConstants.VIDEO2_IMAGE_URL)).
						field("api_policy_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.API_POLICY_CAPABLE))).   //boolean
						field("video3_url", gateletData.get(GateletStatusConstants.VIDEO3_URL)).
						field("generic_epd", Boolean.valueOf(gateletData.get(GateletStatusConstants.GENERIC_EPD))).   //boolean
						field("subscription_id", gateletData.get(GateletStatusConstants.SUBSCRIPTION_ID)).
						field("api_key", gateletData.get(GateletStatusConstants.API_KEY)).
						field("api_on_demand_scan_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.API_ON_DEMAND_SCAN_CAPABLE))).   //boolean
						field("video4_url", gateletData.get(GateletStatusConstants.VIDEO4_URL)).
						field("screenshot1_uri", gateletData.get(GateletStatusConstants.SCREENSHOT1_URI)).
						field("long_desc", gateletData.get(GateletStatusConstants.LONG_DESC)).
						field("screenshot5_uri", gateletData.get(GateletStatusConstants.SCREENSHOT5_URI)).
						startArray("policy_blacklist");
						for(int i=0;i<policyBlacklist.size();i++){
							builder.value(policyBlacklist.get(i));
						}
						builder.endArray().
						field("signup_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.SIGNUP_CAPABLE))). //boolean
						startArray("category_key");
						for(int i=0;i<category.size();i++){
							builder.value(category.get(i));
							/*value("File  Sharing").
							value("Storage").*/
						}
						builder.endArray().
						field("video1_desc", gateletData.get(GateletStatusConstants.VIDEO1_DESC)).
						field("price", Integer.parseInt(gateletData.get(GateletStatusConstants.PRICE))).  //int
						field("is_active", gateletData.get(GateletStatusConstants.IS_ACTIVE)).   //boolean
						field("video4_image_url", gateletData.get(GateletStatusConstants.VIDEO4_IMAGE_URL)).
						field("header_bg_color", gateletData.get(GateletStatusConstants.HEADER_BG_COLOR)).
						field("display_in_store", Boolean.valueOf(gateletData.get(GateletStatusConstants.DISPLAY_IN_STORE))).   //boolean
						field("is_available", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_AVAILABLE))).   //boolean
						field("is_alerting", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_ALERTING))).  //boolean
						field("modified_on", gateletData.get(GateletStatusConstants.MODIFIED_ON)).
						field("screenshot2_uri", gateletData.get(GateletStatusConstants.SCREENSHOT2_URI)).
						field("is_blocked", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_BLOCKED))).   //boolean
						field("is_gateway_subscribed", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_GATEWAY_SUBSCRIBED))).   //boolean
						field("long_desc1", gateletData.get(GateletStatusConstants.LONG_DESC1)).
						field("lang", gateletData.get(GateletStatusConstants.LANG)).
						field("video3_desc", gateletData.get(GateletStatusConstants.VIDEO3_DESC)).
						field("name", gateletData.get(GateletStatusConstants.NAME)).
						field("long_desc2", gateletData.get(GateletStatusConstants.LONG_DESC2)).
						field("is_gateway_subscription_requested", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_GATEWAY_SUBSCRIPTION_REQUESTED))).   //boolean
						field("screenshot4_uri", gateletData.get(GateletStatusConstants.SCREENSHOT4_URI)).
						field("big_icon_uri", gateletData.get(GateletStatusConstants.BIG_ICON_URI)).
						field("popularity", Integer.parseInt(gateletData.get(GateletStatusConstants.POPULARITY))).   //int
						field("addl_params_needed", Boolean.valueOf(gateletData.get(GateletStatusConstants.ADDL_PARAMS_NEEDED))).  //boolean
						field("is_api_subscription_requested", Boolean.valueOf(gateletData.get(GateletStatusConstants.IS_API_SUBSCRIPTION_REQUESTED))).   //boolean
						field("risk_status", gateletData.get(GateletStatusConstants.RISK_STATUS)).
						field("video1_image_url", gateletData.get(GateletStatusConstants.VIDEO1_IMAGE_URL)).
						field("hosts", gateletData.get(GateletStatusConstants.HOSTS)).
						field("api_capable", Boolean.valueOf(gateletData.get(GateletStatusConstants.API_CAPABLE))).   //boolean
						field("support_gateway_encryption", Boolean.valueOf(gateletData.get(GateletStatusConstants.SUPPORT_GATEWAY_ENCRYPTION))).   //boolean
						field("tenant_app_id", gateletData.get(GateletStatusConstants.TENANT_APP_ID)).
						field("resource_uri", gateletData.get(GateletStatusConstants.RESOURCE_URI)).
						field("pac_urls", gateletData.get(GateletStatusConstants.PAC_URLS)).
						field("video1_url", gateletData.get(GateletStatusConstants.VIDEO1_URL)).
						field("video2_url", gateletData.get(GateletStatusConstants.VIDEO2_URL)).
						
						
						field("_id", appName.toLowerCase()).
						field("_hasDetails", true).  //boolean
						field("_smallIconUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.SMALL_ICON_URI)).
						field("_activated", true).   //boolean
						field("_bigIconUrl", "/static/images/store/logos/"+gateletData.get(GateletStatusConstants.BIG_ICON_URI)).
						field("_canAddViaGateway", false).   //boolean
						field("_actionButtonLabel", "Configure").
						field("_apiActionButtonLabel", "Configure").
						field("_gatewayActionButtonLabel", "Deactivate").
						field("_mainDesc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[0]).
						startArray("videos").
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO1_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO1_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[1]).
							endObject().
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO2_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO2_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[2]).
							endObject().
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO3_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO3_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[3]).
							endObject().
							startObject().
								field("url", gateletData.get(GateletStatusConstants.VIDEO4_URL)).
								field("imageUrl", "/static/images/store/"+appName.toLowerCase()+"/"+gateletData.get(GateletStatusConstants.VIDEO4_IMAGE_URL)).
								field("desc", gateletData.get(GateletStatusConstants.LONG_DESC).split("%%%")[4]).
							endObject().
						endArray().
					endObject().
					field("status", "activate").
				endObject();
		
		Reporter.log("Query String for Policy Alerts"+builder.toString());
		return builder.string();
	}
}
