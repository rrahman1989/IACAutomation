package com.automation.utility.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


/**
 * This is a class for File Utiility
 * 
 * @author reazur.rahman
 *
 */

public class ApacheCommonIO {
	
	/**
	 * 
	 * @param srcDirectory The source directory of the file you want to move
	 * @param destDirectory The Destination directory of the file  
	 */
	
	public void moveFile(File srcDirectory, File destDirectory) {
				
		try {
			FileUtils.moveFile(srcDirectory, destDirectory);
			System.out.println("Moved files from: " + srcDirectory + " to: " + destDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * 
	 * @param srcDirectory he source directory of the file you want to copy
	 * @param destDirectory The Destination directory you want to move the file in
	 */
	
	public void copyFile(File srcDirectory, File destDirectory) {
		
		try {
			FileUtils.copyFile(srcDirectory, destDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * 
	 * @param deleteFile Deletes a file
	 */
	
	public void deleteFile(File deleteFile) {
		
		FileUtils.deleteQuietly(deleteFile);
	}
	
	/**
	 * 
	 * @param fileName Pass the file name to read
	 * @return Returning File content 
	 */
	
	public String readFileAsString(File fileName) {
		
		String fileContent = null;
		
		try {
		fileContent =	FileUtils.readFileToString(fileName);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContent;
	}

}