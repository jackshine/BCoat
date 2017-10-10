package com.universal.util;

import com.universal.common.CommonTestClient;

public class OAuthHandler extends CommonTestClient{
	static OAuth20Token oAuth20Token;
	 public static OAuth20Token getAccessTokenByRefreshTokenAndClientIdForBox() {
		 oAuth20Token = new OAuth20Token();
		 oAuth20Token.setAccessToken("AccessToken");
		 long startTime = System.currentTimeMillis();
		 /*
	        WebResource webResource = client.resource(OneDriveEnum.OAUTH20_TOKEN_URL.toString());
	        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
	        ObjectMapper objectMapper = new ObjectMapper();
	        OAuth20Token oAuth20Token = new OAuth20Token();

	        queryParams.add(API_PARAM_CLIENT_ID, principal.getClientId());
	        queryParams.add(API_PARAM_REDIRECT_URI, OneDriveEnum.OAUTH20_DESKTOP_REDIRECT_URL.toString());
	        queryParams.add(API_PARAM_REFRESH_TOKEN, principal.getoAuth20Token().getRefresh_token());
	        queryParams.add(API_PARAM_GRANT_TYPE, OneDriveEnum.GRANT_TYPE_REFRESH_TOKEN.toString());

	        ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, queryParams);

	        try {
	            oAuth20Token = objectMapper.readValue(clientResponse.getEntity(String.class).toString(), OAuth20Token.class);
	            oAuth20Token.setStatus(clientResponse.getStatus());
	        } catch (Exception exception) {
	            logger.error("Error while converting HTTP response to JSON");
	            exception.printStackTrace();
	        }

	        long elapsedTime = System.currentTimeMillis() - startTime;
	        logger.debug("Executed " + webResource.getURI() + " in " + elapsedTime + " ms");
		  */
		 return oAuth20Token;
	 }
	
}

