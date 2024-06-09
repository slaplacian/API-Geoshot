package com.api.geoshot.apigeoshot.classes;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;


/**
 * Class that has static methods to encode into Base64 and decode from Base64.
 */
public class PhotoConverter {

//    public static void main(String[] args) {
//        String gay = encoder("/home/laplace/Downloads/laplace.jpg");
//        File photo = decoder(gay);
//        System.out.println(photo.getAbsoluteFile());
//        System.out.println(gay);
//    }

    /**
     * Static method that encode a photo from a file in our system.
     * @param filename The full file name or relative file name.
     * @return Base64 encoded String.
     */
    public static String encoder(String filename) {
        byte[] fileContent = null;
        String encodedString = null;
        try {
            fileContent   = FileUtils.readFileToByteArray(new File(filename));
            encodedString = Base64.getEncoder().encodeToString(fileContent);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        return encodedString;
    }

    /**
     * Static method that encodes an array of bytes (that was, originally, from a photo) into a Base64 String.
     * @param imageBytes An array of bytes (originally, from an image).
     * @return Base64 encoded String.
     */
    public  static  String encoder(byte[] imageBytes) {
        String encodedString = null;
        encodedString = Base64.getEncoder().encodeToString(imageBytes);
        return encodedString;
    }

    /**
     * Stattic method that create a Object File and saves it in our system from Base64 encoded String.
     * @param encodedString The Base64 encoded String.
     * @param pathToSaveFile The String that represents where in our system we will save it.
     * @return Obeject File (Image File decoded).
     */
    public static File decoder(String encodedString, String pathToSaveFile) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        File photo = new File(String.format("%s/%s.png",pathToSaveFile,HashGeneretor.getHash("MD5",timeStamp)));

        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        try {
            FileUtils.writeByteArrayToFile(photo, decodedBytes);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        return photo;
    }

    /**
     * Stattic method that create a Object File and saves it in our system from Base64 encoded String.
     * The Second Version saves the image file in the current directory.
     * @param encodedString The Base64 encoded String.
     * @return Obeject File (Image File decoded).
     */
    public static File decoder(String encodedString) {
        return PhotoConverter.decoder(encodedString,".");
    }

}
