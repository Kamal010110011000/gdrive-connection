package com.test.drive.examples;

import java.io.IOException;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.test.drive.utils.GoogleDriveUtils;

public class ShareGoogleFile {
	
	public static Permission createPublicPermission(String googleFileIdString) throws IOException{
		String permissionTypeString = "anyone";
		String permissionRoleString = "reader";
		Permission newPermission = new Permission();
		newPermission.setType(permissionTypeString);
		newPermission.setRole(permissionRoleString);
		
		Drive serviceDrive = GoogleDriveUtils.getDriveService();
		return serviceDrive.permissions().create(googleFileIdString, newPermission).execute();
	}
	
	public static Permission createEmailPermission(String googleFileId, String googleEmail)throws IOException{
		String permissionTypeString = "user";
		String permissionRoleString = "reader";
		Permission newPermission = new Permission();
		newPermission.setType(permissionTypeString);
		newPermission.setRole(permissionRoleString);
		
		newPermission.setEmailAddress(googleEmail);
		Drive serviceDrive = GoogleDriveUtils.getDriveService();
		return serviceDrive.permissions().create(googleFileId, newPermission).execute();
	}
	
	public static void main(String[] args)throws IOException{
		String googleFileId, googleEmail;
		googleEmail = "kamalsingh.4iv1@gmail.con";
		googleFileId = "";
		//share with kamal
		createEmailPermission(googleFileId, googleEmail);
		//share with everyone
		String googleFileId2 = "";
		createPublicPermission(googleFileId2);
		System.out.println("done!");
	}

}
