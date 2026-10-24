/**
 * 
 */
package com.qatest.utils;

/**
 * @author Divyang
 * This utility is good for png/jpg/jpeg images having size upto 100KB
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ImageToBase64 {
	public static String getImageToBase64(String imageFilePath) {
		//String htmlImageTag = null;
		String base64String = null;
		try {
			// 1. Specify the path to your local image
			File imageFile = new File(imageFilePath);
			// 2. Read all bytes from the image file
			byte[] fileContent = Files.readAllBytes(imageFile.toPath());

			// 3. Encode the byte array to a Base64 string
			base64String = Base64.getEncoder().encodeToString(fileContent);

			// 4. Generate the exact HTML image tag
			//htmlImageTag = String.format("<img src='data:image/png;base64,%s' alt='Screenshot of a test'>", base64String);

			// 5. Print out the result
			//System.out.println(htmlImageTag);
			
		} catch (IOException e) {
			System.err.println("Error reading the image file: " + e.getMessage());
			e.printStackTrace();
		} 
		// 6. Return the result
		//return htmlImageTag;
		return base64String;
	}
}