package com.test.drive.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.test.drive.utils.GoogleDriveUtils;

public class GetSubFolders {
	
	public static final List<File> getGoogleSubFolders(String googleFolderIdParent) throws IOException{
		Drive driveService = GoogleDriveUtils.getDriveService();
		String pageTokenString = null;
		List<File> list = new ArrayList<File>();
		String queryString = null;
		if(googleFolderIdParent == null) {
			queryString = "mimeType = 'application/vnd.google-apps.folder' and 'root' in parents";
		} else {
			queryString = "mimeType = 'application/vnd.google-apps.folder' and '"+googleFolderIdParent+"' in parents";
		}
		
		do {
			FileList resultFileList = driveService.files().list().setQ(queryString).setSpaces("drive")
					.setFields("nextPageToken, files(id, name, createdTime)")
					.setPageToken(pageTokenString).execute();
			for(File file: resultFileList.getFiles()) {
				list.add(file);
			}
			pageTokenString = resultFileList.getNextPageToken();
		}while(pageTokenString!=null);
		return list;
	}
	
	public static final List<File> getGoogleRootFolders() throws IOException{
		return getGoogleSubFolders(null);
	}
	
	public static void main(String[] args)throws IOException{
		List<File> googleRootFiles = getGoogleRootFolders();
		for(File folder: googleRootFiles) {
			System.out.println("Folder ID: "+folder.getId()+" --Name: "+folder.getName());
		}
	}

}
