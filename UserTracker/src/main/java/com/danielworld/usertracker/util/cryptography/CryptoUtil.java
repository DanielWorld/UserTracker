package com.danielworld.usertracker.util.cryptography;

import com.danielworld.usertracker.util.cryptography.model.CryptoModel;
import com.danielworld.usertracker.util.cryptography.type.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encryption & decryption in AES
 * <br><br>
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by Daniel Park on 2015-06-16.
 */
public class CryptoUtil {

    /**
     * AES symmetric Algorithm
     */
    public static class AESUtils {

        private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
        private static final String CIPHER = "AES";
        private static final String RANDOM_ALGORITHM = "SHA1PRNG";
        private static final int AES_KEY_LENGTH_BITS = 128;
        private static final int IV_LENGTH_BYTES = 16;

        //Made BASE_64_FLAGS public as it's useful to know for compatibility.
        private static final int BASE64_FLAGS = Base64.NO_WRAP;

        /**
         * A function that generates random AES key and prints out exceptions
         * but doesn't throw them since none should be encountered.
         * If they are encountered, the return value is null
         *
         * @return
         * @throws GeneralSecurityException if AES is not implemented on this system, or a suitable RNG is not available
         */
        public static SecretKey generateKey() throws GeneralSecurityException {
            KeyGenerator keyGen = KeyGenerator.getInstance(CIPHER);
            // No need to provide a SecureRandom or set a seed since that will happen automatically
            keyGen.init(AES_KEY_LENGTH_BITS);
            SecretKey integrityKey = keyGen.generateKey();
            return integrityKey;
        }

        /**
         * A function that generates AES key using String seedKey
         * @param seedKey
         * @return
         */
        public static SecretKey generateKey(String seedKey){
            byte[] byteKey = seedKey.getBytes();

            MessageDigest sha = null;
            try {
                sha = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byteKey = sha.digest(byteKey);
            byteKey = Arrays.copyOf(byteKey, 16); // use only first 128 bit

            SecretKey integrityKey = new SecretKeySpec(byteKey, CIPHER);
            return integrityKey;
        }

        /**
         * Genrates a random IV and encrypts this plain text with the given key.
         *
         * @param plainText The text that will be encrypted, which will be serialized with UTF-8
         * @param secretKey The AES key with which to encrypt
         * @return CryptoModel container which contains cipher text & IV
         * @throws UnsupportedEncodingException if AES is not implemented on this system
         * @throws GeneralSecurityException     if UTF-8 is not supported in this system
         */
        public static CryptoModel encrypt(String plainText, SecretKey secretKey) throws UnsupportedEncodingException, GeneralSecurityException {
            return encrypt(plainText, secretKey, "UTF-8");
        }

        private static CryptoModel encrypt(String plainText, SecretKey secretKey, String encoding) throws UnsupportedEncodingException, GeneralSecurityException {
            return encrypt(plainText.getBytes(encoding), secretKey);
        }

        private static CryptoModel encrypt(byte[] plainText, SecretKey secretKey) throws GeneralSecurityException {
            byte[] iv = generateIv();
            Cipher aesCipherForEncryption = Cipher.getInstance(CIPHER_TRANSFORMATION);
            aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

            iv = aesCipherForEncryption.getIV();
            byte[] byteCipherText = aesCipherForEncryption.doFinal(plainText);
            return new CryptoModel(Base64.encodeToString(byteCipherText, BASE64_FLAGS),
                    Base64.encodeToString(iv, BASE64_FLAGS));
        }

        /**
         * AEC CBC decrypt
         *
         * @param cipherModel it contains cipher text & IV
         * @param secretKey   The AES key
         * @return A plain text
         * @throws UnsupportedEncodingException
         * @throws GeneralSecurityException
         */
        public static String decrypt(CryptoModel cipherModel, SecretKey secretKey) throws UnsupportedEncodingException, GeneralSecurityException {
            return decrypt(cipherModel, secretKey, "UTF-8");
        }

        private static String decrypt(CryptoModel ciphers, SecretKey secretKey, String encoding) throws UnsupportedEncodingException, GeneralSecurityException {
            byte[] cipherText = Base64.decode(ciphers.getCryptoText(), BASE64_FLAGS);
            byte[] cipherIv = Base64.decode(ciphers.getIv(), BASE64_FLAGS);

            Cipher aesCipherForDecryption = Cipher.getInstance(CIPHER_TRANSFORMATION);
            aesCipherForDecryption.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(cipherIv));
            byte[] bytePlainText = aesCipherForDecryption.doFinal(cipherText);

            return new String(bytePlainText, encoding);
        }

        /**
         * Creates a random Initialization Vector (IV) of IV_LENGTH_BYTES.
         *
         * @return The byte array of this IV
         * @throws GeneralSecurityException if a suitable RNG is not available
         */
        private static byte[] generateIv() throws GeneralSecurityException {
            return randomBytes(IV_LENGTH_BYTES);
        }

        private static byte[] randomBytes(int length) throws GeneralSecurityException {
            SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            byte[] b = new byte[length];
            random.nextBytes(b);
            return b;
        }
    }

    /**
     * RSA Asymmetric Algorithm
     */
    public static class RSAUtils {

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

    /**
     * Create Hash (using SHA-256 perhaps?) from text
     */
    public static class CreateHash{

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

        final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
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
}
