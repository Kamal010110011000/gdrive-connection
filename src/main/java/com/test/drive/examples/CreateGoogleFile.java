package com.test.drive.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.test.drive.utils.GoogleDriveUtils;

public class CreateGoogleFile {
	private static File _createGoogleFile(String googleFolderIdParent, String contentType, 
			String customFileString, AbstractInputStreamContent uploadStreamContent) throws IOException{
		File metadataFile = new File();
		metadataFile.setName(customFileString);
		List<String> parentList = Arrays.asList(googleFolderIdParent);
		metadataFile.setParents(parentList);
		Drive serviceDrive = GoogleDriveUtils.getDriveService();
		File file = serviceDrive.files().create(metadataFile, uploadStreamContent)
				.setFields("id, webContentLink, webViewLink, parents").execute();
		return file;
	}
	
	//create file from bytes
	public static File createGoogleFile(String googleFolderIdString, String contentTypeString, String custonerFileNameString,
			byte[] uploadData) throws IOException{
		AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentTypeString, uploadData);
		return _createGoogleFile(googleFolderIdString, contentTypeString, custonerFileNameString, uploadStreamContent);
	}
	//create file from java.io.file
	public static File createGoogleFile(String googleFolderIdString, String contentTypeString, String customNameString,
			java.io.File uploadFile) throws IOException{
		AbstractInputStreamContent uploadStreamContent = new FileContent(contentTypeString, uploadFile);
		return _createGoogleFile(googleFolderIdString, contentTypeString, customNameString, uploadStreamContent);
	}
	//create file from input stream
	public static File createGoogleFile(String googleFolderIdString,String contentTypeString, String customNameString,
			InputStream inputStream)throws IOException{
		AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentTypeString, inputStream);
		return _createGoogleFile(googleFolderIdString, contentTypeString, customNameString, uploadStreamContent);
	}
	
	public static void main(String[] args)throws IOException{
		java.io.File uploadFile = new java.io.File("/home/developer/Pictures/d/dp.jpg");
		File googleFile = createGoogleFile(null, "image/jpg", "divya.jpg", uploadFile);
		System.out.println("Created Google File!\nWebContentLink: "+googleFile.getWebContentLink());
		System.out.println("WebViewLink: "+googleFile.getWebViewLink());
		System.out.println("Done!");
	}
	

}
