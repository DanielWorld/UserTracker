package com.danielworld.usertracker.util.cryptography;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by daniel on 15. 11. 2.
 */
public class CreateHash {
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String SHA1(String text){
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = text.getBytes("UTF-8");
            md.update(bytes, 0, bytes.length);
            bytes = md.digest();

            // this is ~55x faster than looping and String.formating()
            hash = bytesToHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return hash;
        }
    }

    public static String SHA256(String text){
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = text.getBytes("UTF-8");
            md.update(bytes, 0, bytes.length);
            bytes = md.digest();

            // this is ~55x faster than looping and String.formating()
            hash = bytesToHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return hash;
        }
    }

    private static String bytesToHex(byte[] bytes){
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }
        return new String( hexChars );
    }
}
