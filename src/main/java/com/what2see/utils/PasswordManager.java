package com.what2see.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class that provides methods for hashing passwords.
 */
public class PasswordManager {

    /**
     * Hashes a password using the SHA-256 algorithm.
     * @param password password to be hashed
     * @return hashed password
     */
    public static String hashPassword(String password) {
        // get an instance of the SHA-256 algorithm
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // hash the password
        md.update(password.getBytes());
        byte[] digest = md.digest();
        // convert the hash to string
        return String.format("%064x", new java.math.BigInteger(1, digest));
    }

}
