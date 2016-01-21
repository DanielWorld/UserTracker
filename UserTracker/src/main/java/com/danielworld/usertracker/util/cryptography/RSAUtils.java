package com.danielworld.usertracker.util.cryptography;

import com.danielworld.usertracker.util.cryptography.type.Base64;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by daniel on 15. 11. 2.
 */
public class RSAUtils {
    private static final String ALGORITHM = "RSA";
    private static final int BASE64_FLAG = Base64.URL_SAFE;

    /**
     * Generating Key for RSA <br><br>
     *     Seeding SecureRandom may be insecure <br>

     A seed is an array of bytes used to bootstrap random number generation. To produce cryptographically secure random numbers, both the seed and the algorithm must be secure.
     By default, instances of this class will generate an initial seed using an internal entropy source, such as /dev/urandom. This seed is unpredictable and appropriate for secure use.

     Using the seeded constructor or calling setSeed(byte[]) may completely replace the cryptographically strong default seed causing the instance to return a predictable sequence of numbers unfit for secure use. Due to variations between implementations it is not recommended to use setSeed at all.
     * @return KeyPair
     */
    public static KeyPair generateRSAKeyPair() {
        KeyPair keyPair = null;

        // Original generating RSA key method
//            try {
//                keyPair = KeyPairGenerator.getInstance(ALGORITHM).generateKeyPair();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } finally {
//                return keyPair;
//            }
        // Using 2048 bit
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(2048, new SecureRandom());
            keyPair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            return keyPair;
        }
    }

    /**
     * Encrypt message with Public key and then encode byte encrypted message by Base64.<br>
     *     it returns String text
     * @param message
     * @param publicKey
     * @return
     */
    public static String encrypt(String message, PublicKey publicKey){
        byte[] bytes = message.getBytes();
        String encryptedMessage = null;

        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedMessage = Base64.encodeToString(cipher.doFinal(bytes), BASE64_FLAG);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }finally {
            return encryptedMessage;
        }
    }

    /**
     * Decrypt encrypted & encoded Base64 text with private key
     * @param encryptedBase64Text
     * @param privateKey
     * @return
     */
    public static String decrypt(String encryptedBase64Text, PrivateKey privateKey){
        byte[] bytes = Base64.decode(encryptedBase64Text, BASE64_FLAG);
        String decryptedMessage = null;

        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedMessage = new String(cipher.doFinal(bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            return decryptedMessage;
        }
    }

    /**
     * Generate RSAPublicKeySpec Object from RSA public key
     * @param publicKey
     * @return
     */
    public static RSAPublicKeySpec getRSAPublicKeySpec(PublicKey publicKey){
        RSAPublicKeySpec spec = null;

        try{
            spec = KeyFactory.getInstance(ALGORITHM).getKeySpec(publicKey, RSAPublicKeySpec.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } finally {
            return spec;
        }
    }

    /**
     * Generate RSAPrivateKeySpec Object from RSA private key
     * @param privateKey
     * @return
     */
    public static RSAPrivateCrtKeySpec getRSAPrivateKeySpec(PrivateKey privateKey){
        RSAPrivateCrtKeySpec spec = null;

        try{
            spec = KeyFactory.getInstance(ALGORITHM).getKeySpec(privateKey, RSAPrivateCrtKeySpec.class);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }finally {
            return spec;
        }
    }

    /**
     * Generate PublicKey using Modulus and Exponent
     * @param modulus RSA Public Key Modulus
     * @param exponent RSA Public Key exponent
     * @return PublicKey Object
     */
    public static PublicKey getPublicKey(String modulus, String exponent) {
        BigInteger modulus_ = new BigInteger(modulus);
        BigInteger exponent_ = new BigInteger(exponent);
        PublicKey publicKey = null;

        try {
            publicKey = KeyFactory
                    .getInstance(ALGORITHM)
                    .generatePublic(new RSAPublicKeySpec(modulus_, exponent_));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    /**
     * Generate PrivateKey using Modulus, Exponent
     * @param modulus RSA private key Modulus
     * @param privateExponent RSA private key exponent
     * @return PrivateKey Object
     */
    public static PrivateKey getPrivateKey(String modulus, String privateExponent) {
        BigInteger modulus_ = new BigInteger(modulus);
        BigInteger privateExponent_ = new BigInteger(privateExponent);
        PrivateKey privateKey = null;

        try {
            privateKey = KeyFactory
                    .getInstance(ALGORITHM)
                    .generatePrivate(new RSAPrivateKeySpec(modulus_, privateExponent_));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return privateKey;
    }

    /**
     * Encrypt message with Private key and then encode byte encrypted message by Base64.<br>
     *     it returns String text
     * @param message
     * @param privateKey
     * @return
     */
    public static String encrypt(String message, PrivateKey privateKey){
        byte[] bytes = message.getBytes();
        String encryptedMessage = null;

        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            encryptedMessage = Base64.encodeToString(cipher.doFinal(bytes), BASE64_FLAG);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }finally {
            return encryptedMessage;
        }
    }

    /**
     * Decrypt encrypted & encoded Base64 text with Public Key
     * @param encryptedBase64Text
     * @param publicKey
     * @return
     */
    public static String decrypt(String encryptedBase64Text, PublicKey publicKey){
        byte[] bytes = Base64.decode(encryptedBase64Text, BASE64_FLAG);
        String decryptedMessage = null;

        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            decryptedMessage = new String(cipher.doFinal(bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            return decryptedMessage;
        }
    }
}
