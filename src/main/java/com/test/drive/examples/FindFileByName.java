package com.test.drive.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.test.drive.utils.GoogleDriveUtils;

public class FindFileByName {
	
	public static List<File> getGoogleFilesByName(String fileString) throws IOException{
		Drive serviceDrive = GoogleDriveUtils.getDriveService();
		String pageTokenString = null;
		List<File> list = new ArrayList<File>();
		String queryString = "name contains '"+fileString+"' and mimeType!='application/vnd.google-apps.folder' ";
		do {
			FileList result = serviceDrive.files().list().setQ(queryString).setSpaces("drive")
					.setFields("nextPageToken, files(id, name, createdTime, mimeType)")
					.setPageToken(pageTokenString).execute();
			for(File file: result.getFiles()) {
				list.add(file);
			}
			pageTokenString = result.getNextPageToken();
		}while(pageTokenString!=null);
		return list;
	}
	
	public static void main(String[] args)throws IOException {
		List<File> rootgoogleFiles = getGoogleFilesByName("K");
		for(File folder: rootgoogleFiles) {
			System.out.println("Mime Type: "+folder.getMimeType()+"---Name: "+folder.getName());
		}
		System.out.println("Done");
	}

}
