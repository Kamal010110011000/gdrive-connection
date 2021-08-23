package com.test.drive.examples;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.test.drive.utils.GoogleDriveUtils;

public class CreateFolder {

	public static File createGoogleFolder(String folderIdParent, String folderName)throws IOException{
		File metadataFile = new File();
		metadataFile.setName(folderName);
		metadataFile.setMimeType("application/vnd.google-apps.folder");
		if(folderIdParent!=null) {
			List<String> parents = Arrays.asList(folderIdParent);
			metadataFile.setParents(parents);
		}
		Drive serviceDrive = GoogleDriveUtils.getDriveService();
		File file = serviceDrive.files().create(metadataFile).setFields("id, name").execute();
		return file;
	}
	
	public static void main(String[] args)throws IOException {
		File folderFile = createGoogleFolder(null, "project");
		System.out.println("created folder with id= "+folderFile.getId());
		System.out.println("\t\t name= "+folderFile.getName());
		
		System.out.println("done");
	}
}
