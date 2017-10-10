package com.elastica.beatle.tests.infra;

public class InfraConstants {
	public static final String BOP_EDIT_SUBSCRIPTION = "/bop/editSubscription/?";
	public static final String BOP_SUCCESS = "success";
	public static final String INFRA_EDIT_SUBSCRIPTION = "https://qa-vpc-api.elastica-inc.com/eslogs/displaylogs/";
	public static final String INFRA_DATA_LOC = "src/test/resources/Infra/Data/";
	public static final String API_CALL_USERS="/api/admin/v1/users/";
	public static final String API_CALL="/api/admin/v1/";
	public static final String API_CALL_GROUP="/elasticaco/api/admin/v1/usergroups/";
	public static final String API_GROUP_DEL="/api/admin/v1/usergroups/";
	//add 
	public static final String API_ADD_USER="/admin/user/ng/add/";
	public static final String API_ADD_GROUP="/admin/group/ng/add/";
	public static final String SEARCH_GROUP="/admin/group/ng/list/0?limit=90&offset=0&order_by=name&query=";
	public static final String GROUP_EDIT="/admin/group/ng/edit";
	public static final String SEARCH_USER="/admin/user/ng/list/0?limit=90&offset=0&order_by=first_name&query=";
	public static final String GROUP_API="/api/admin/v1/groups/";
	public static final String API_NOTIFICATION_LIST="/admin/notification/list";
	public static final String ADD_DPO_pl="{\"user\":{\"first_name\":\"AugDpo\",\"last_name\":\"dpo\",\"email\":\"vijay.gangwar+62@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":true}}";
	public static final String DPO_EMAIL="vijay.gangwar+62@infrabeatle.com";
	public static final String LIST_USERS="/admin/user/ng/list/0?";
	public static final String LIST_GROUP="/admin/group/ng/list/0?filters=";
	public static final String LIST_PROFILE="/admin/profiles/list?filters=";
	public static final String REMOVE_FILTER="/admin/user/ng/list/0?limit=30&offset=0&order_by=first_name";
	public static final String REMOVE_GROUP_FILTER="/admin/group/ng/list/0?limit=30&offset=0&order_by=name";
	public static final String REMOVE_PROFILE_FILTER="admin/profiles/list?order_by=name";
	public static final String API_ADMIN_NOTIFICATIONS="elasticaco/api/admin/v1/notifications/";
	public static final String QUERY_PARAM_PROFILE="access_profiles";
	public static final String QUERY_PARAM_CREATED_BY="created_by";
	public static final String QUERY_PARAM_ACTIVE="is_active";
	public static final String QUERY_PARAM_ROLE="role";
	public static final String DEFAULT_SEARCH="&limit=30&offset=0&order_by=first_name";
	public static final String GROUP_SEARCH="&limit=30&offset=0&order_by=name";
	public static final String USER_FILTER="/admin/user/ng/filters?";
	public static final String SUBSCRIPTION="/admin/user/subscription";
	public static final String USER="user";
	public static final String GROUP="group";
	public static final String PROFILES="profiles";
	public static final int OK=0;
	public static final int BAD_REQUEST=400;
	public static final String EDIT_ACTION="/admin/user/ng/edit";
	public static final String APP_NAME="app_name";
	public static final String APP_STATUS="access_status";
	public static final String REQUESTED="REQUESTED";
	public static final String REVOKED="REVOKED";
	public static final String GRANTED="GRANTED";
	public static final String REQUESTED_ACCESS="/admin/application/requestaccess/";
	public static final String LIST_TENANTS="/admin/application/list/tenantavailableapps?lang=en";
	public static final String BOP_GRANT="grant";
	public static final String BOP_REVOKE="revoke";
	public static final String CATEGORY_ELASTICA="Elastica";
	public static final String CATEGORY_GATEWAY="Gateway";
	public static final String CATEGORY_API="API";
	public static final String BOP_ADMIN="vijay.gangwar%40elastica.co";
	public static final String APPLICATIONS="applications";
	public static final String AUDIT="Audit";
	public static final String RESET_PASSWORD="/admin/user/ng/reset_password/";
	public static final String PAYLOAD_RESET="{\"user\":{\"is_dpo\":false,\"last_name\":\"may\",\"changed_password\":\"2015-09-08T10:54:16.493000\",\"is_alerting\":false,\"is_super_admin\":false,\"quarantine_info\":null,\"created_on\":\"2015-09-08T10:54:16.402000\",\"access_profiles\":[],\"timezone\":null,\"id\":\"uid\",\"first_name\":\"AjayInactiveUser\",\"is_blocked\":false,\"modified_by\":null,\"title\":\"\",\"work_phone\":\"\",\"created_by\":\"admin@infrabeatle.com\",\"is_partner\":false,\"email\":\"vijay.gangwar+16@infrabeatle.com\",\"is_two_factor_auth_enabled\":false,\"is_active\":false,\"threatscore\":{},\"two_factor_auth_key\":\"\",\"secondary_user_id\":\"\",\"blocked_apps\":{},\"is_admin\":false,\"groups\":[],\"modified_on\":null,\"force_logout\":false,\"is_dummy\":false,\"lock_time\":null,\"is_quarantined\":false,\"notes\":\"\",\"risk_status\":\"\",\"cell_phone\":\"\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/uid/\"}}";
	public static final String FORGOT_PWD="/user/forgotpasswordapi";
	public static final String ASSING_USER_PL="{\"email\":\"vijay.gangwar+16@infrabeatle.com\",\"deleted_groups\":[],\"added_groups\":[\"Blr\"]}";
	public static final String GROUP_PL="{\"group\":{\"name\":\"BlrInactivegrp\",\"description\":\"test group\",\"is_active\":false,\"notes\":\"\"}}";
	public static final String INACTIVE_GROUP_PL="{\"group\":{\"name\":\"AInactive_test \",\"description\":\"test group\",\"is_active\":false,\"notes\":\"\"}}";
	public static final String DUMMY="dummy";
	
	
}
