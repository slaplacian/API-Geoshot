package com.api.geoshot.apigeoshot.classes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class that has a staic method for generating hashes using a specified algorithm.
 */
public class HashGeneretor {

//    public static void main(String[] args) {
//        System.out.println(getHash("SHA256", "1234"));
//    }

    /**
     * Static method that returns a hash string from a string.
     * @param algorithm A String which specifies the used algorithm. In GeoShot the most used (and unique) is SHA256.
     * @param plainPassword The String we want to turn into hash. In most cases, it will be user's password.
     * @return A hash String.
     */
    public static String getHash(String algorithm, String plainPassword) {
        StringBuilder byteToHexString = new StringBuilder();
        MessageDigest digest = null;
        byte[] bytePassword;

        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch(NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }

        try {

            bytePassword = digest.digest(plainPassword.getBytes("UTF-8"));
            for(byte b: bytePassword) {
                String hex = String.format("%02x",b);
                byteToHexString.append(hex);
            }

        } catch(UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        } catch(NullPointerException ex) {
            System.out.println(ex.getMessage());
        }

        return byteToHexString.toString();
    }

}
