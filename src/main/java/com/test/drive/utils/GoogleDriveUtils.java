package com.test.drive.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class GoogleDriveUtils {
	
	private static final String APPLICARION_NAME_STRING = "Google Drive API Jave Quickstart";
	private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final java.io.File CREDENTIALS_FOLDER = new java.io.File(System.getProperty("user.home"), "credentials");
	private static final String CLIENT_SECRET_FILE_STRING = "client_secret.json";
	
	private static final List<String> SCOPE_LIST = Collections.singletonList(DriveScopes.DRIVE);
	private static FileDataStoreFactory dataStoreFactory;
	private static HttpTransport httpTransport;
	private static Drive _driveService;
	
	static {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(CREDENTIALS_FOLDER);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}
	
	public static Credential getCredential() throws IOException{
		java.io.File clientSecretFile = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_STRING);
		if(!clientSecretFile.exists()) {
			throw new FileNotFoundException("Please copy "+CLIENT_SECRET_FILE_STRING+" to folder: "+CREDENTIALS_FOLDER.getAbsolutePath());
		}
		
		InputStream inputStream = new FileInputStream(clientSecretFile);
		
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
		
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPE_LIST)
				.setDataStoreFactory(dataStoreFactory).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user"); 
		return credential;
	}
	
	public static Drive getDriveService() throws IOException{
		if(_driveService!=null) {
			return _driveService;
		}
		Credential credential = getCredential();
		_driveService = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICARION_NAME_STRING).build();
		return _driveService;
	}

}
