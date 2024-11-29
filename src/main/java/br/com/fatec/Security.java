/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HexFormat;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author alberto
 */
public class Security {
    /**
     * Hashes a password with the PBKDF2 algorithm
     * @param psswd The password to be hashed
     * @param salt The unique salt to add
     * @return The hash of the salted password or an empty String if unsuccessful
     */
    public static String hashPassword(String psswd, String salt){
        
        byte[] h = salt.getBytes(StandardCharsets.UTF_8);

        KeySpec spec = new PBEKeySpec(psswd.toCharArray(), h, 65536, 128);
        SecretKeyFactory factory;

        
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
        
        
        byte[] hash;
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException ex) {
            return "";
        }

        HexFormat hex = HexFormat.of();

        return hex.formatHex(hash);

    }

    /**
     * Generates a random 16 byte array
     * @return The salt as a hexString
     */
    public static String newSalt(){
        
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        HexFormat hex = HexFormat.of();
        
        return hex.formatHex(salt);
    }
}
