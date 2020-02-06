package com.godavari.appsnest.fms.dao.hashing;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class HashingMD5 {

    private static final String LOG_TAG = HashingMD5.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(LOG_TAG);

    public static String getSecurePassword(String passwordToHash, byte[] salt) throws Exception {
        String generatedPassword = null;
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        //Add password bytes to digest
        md.update(salt);

        //Get the hash's bytes
        byte[] bytes = md.digest(passwordToHash.getBytes());
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        generatedPassword = sb.toString();

        return generatedPassword;
    }

    //Add salt
    public static byte[] getSalt(int saltLength) throws NoSuchAlgorithmException, NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[saltLength];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }
}
