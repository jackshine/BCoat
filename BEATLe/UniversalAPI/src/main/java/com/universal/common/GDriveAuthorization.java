/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.universal.common;

/**
 *
 * @author rahulkumar
 */
import com.google.api.services.drive.Drive.Properties.Delete;
import com.google.api.services.drive.Drive.Properties.Patch;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.compute.ComputeCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import java.io.InputStreamReader;
import java.util.List;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.Drive.Properties.Get;
import com.google.api.services.drive.Drive.Properties.Update;
import com.google.api.services.drive.model.Property;
import com.google.api.services.gmail.GmailScopes;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.model.File;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.universal.dtos.box.FileUploadResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.testng.Reporter;

public class GDriveAuthorization {

    static String CLIENT_ID = "298616982965-3qk9q9eci2r79k0aeig173gqmapv0cil.apps.googleusercontent.com";
    static String CLIENT_SECRET = "BuJysjSFHNr5Cb0kQqGuCK2N";
    FileDataStoreFactory DATA_STORE_FACTORY;

    public GDriveAuthorization(String CLIENT_ID, String CLIENT_SECRET) {
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        String dataStoreDirectory = null;
        try {
            dataStoreDirectory = new java.io.File( "." ).getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(GDriveAuthorization.class.getName()).log(Level.SEVERE, null, ex);
        }
        Reporter.log(" #### Data Store Directory :"+dataStoreDirectory);
        java.io.File DATA_STORE_DIR = new java.io.File(dataStoreDirectory);
        try {
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (IOException ex) {
            Reporter.log("Issue with GDrive Authorization in Data Store Factory", true);
        }
    }

    public Credential generateCredentials() throws GeneralSecurityException, IOException {
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
       
        List<String> SCOPES
        = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_INSERT,
                GmailScopes.GMAIL_MODIFY, GmailScopes.GMAIL_SEND, GmailScopes.MAIL_GOOGLE_COM,
                DriveScopes.DRIVE, Oauth2Scopes.USERINFO_EMAIL, Oauth2Scopes.USERINFO_PROFILE, "https://spreadsheets.google.com/feeds", "https://docs.google.com/feeds");

        
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        String access_token = credential.getAccessToken();
        String refreshToken = credential.getRefreshToken();
        System.out.println(" ## Refresh Token :"+refreshToken);
        System.out.println(" ### Pls Save the Token Received.. It is valid till the next time revoked......");
        System.out.println(" ### Access Token Received " + access_token);
        return credential;
    }
 
    public String  getAceessTokenFromRefreshAccessToken(String refreshToken) throws IOException, GeneralSecurityException {
    	HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    	JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    	GoogleCredential credential = createCredentialWithRefreshToken(
    			HTTP_TRANSPORT, JSON_FACTORY, new TokenResponse().setRefreshToken(refreshToken));
    	credential.refreshToken();
    	String newAccessToken = credential.getAccessToken();
    	String refreshToken1 = credential.getRefreshToken();
    	System.out.println("Refresh Token :"+refreshToken1);
    	System.out.println(" ###==== New Access Token from Refresh Token ===> "+newAccessToken);
    	return newAccessToken;
    }
    public GoogleCredential  getCredentialsFromRefreshAccessToken(String refreshToken) throws IOException, GeneralSecurityException {
         HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = createCredentialWithRefreshToken(
                HTTP_TRANSPORT, JSON_FACTORY, new TokenResponse().setRefreshToken(refreshToken));
        credential.refreshToken();
        return credential;
    }

    public static GoogleCredential createCredentialWithRefreshToken(HttpTransport transport,
            JsonFactory jsonFactory, TokenResponse tokenResponse) {
        return new GoogleCredential.Builder().setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .build()
                .setFromTokenResponse(tokenResponse);
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException {
    	 CLIENT_ID = "416395240442-utts3fjpugmrp4jmabbfa33rg2opol0m.apps.googleusercontent.com";
    	    CLIENT_SECRET = "yRFp40EpWG4bBOQvu2a90JbM";
        GDriveAuthorization gDriveAuthorization = new GDriveAuthorization(CLIENT_ID, CLIENT_SECRET);
        gDriveAuthorization.generateCredentials();   
    }

}
