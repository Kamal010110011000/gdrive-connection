package com.test.drive.quickstart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class DriveQuickstart {

	private static final String APPLICATION_NAME_STRING= "Google Drive API java Quickstart";
	private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final java.io.File CREDENTIAL_FOLDER = new java.io.File(System.getProperty("user.home"), "credentials");
	private static final String CLIENT_SECRET_FILE_NAME_STRING = "client_secret.json";
	
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	
	private static Credential getCredential(final NetHttpTransport hTTP_Transport) throws IOException{
		java.io.File clientSecretFilePath = new java.io.File(CREDENTIAL_FOLDER, CLIENT_SECRET_FILE_NAME_STRING);
		if(!clientSecretFilePath.exists()) {
			throw new FileNotFoundException("Please copy "+CLIENT_SECRET_FILE_NAME_STRING+" to folder:"+CREDENTIAL_FOLDER.getAbsolutePath());
		}
		
		InputStream inputStream = new FileInputStream(clientSecretFilePath);
		
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
		
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(hTTP_Transport, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(CREDENTIAL_FOLDER)).setAccessType("offline").build();
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}
	
	public static void main(String[] args) throws IOException, GeneralSecurityException{
		System.out.println("CREDENTIALS_FOLDER:"+ CREDENTIAL_FOLDER.getAbsolutePath());
		if(!CREDENTIAL_FOLDER.exists()) {
			CREDENTIAL_FOLDER.mkdirs();
			System.out.println("Created Folder: "+CREDENTIAL_FOLDER.getAbsolutePath());
			System.out.println("Copy file "+ CLIENT_SECRET_FILE_NAME_STRING + " into folder above... and rerun this class!");
			return;
		}
		
		final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		Credential credential = getCredential(httpTransport);
		Drive serviceDrive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME_STRING).build();
		
		FileList resultFileList = serviceDrive.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
		List<File> files = resultFileList.getFiles();
		if(files == null || files.isEmpty()) {
			System.out.println("No files found");
		}else {
			System.out.println("files: ");
			for(File file: files) {
				System.out.printf("%s (%s)\n", file.getName(), file.getId());
			}
		}
	}
	
}
