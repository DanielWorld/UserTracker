package com.danielworld.usertracker.util.cryptography;

import com.danielworld.usertracker.util.cryptography.model.CryptoModel;
import com.danielworld.usertracker.util.cryptography.type.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by daniel on 15. 11. 2.
 */
public class AESUtils {
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
    public static SecretKey generateKey() throws Exception {
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
    public static CryptoModel encrypt(String plainText, SecretKey secretKey) throws Exception {
        return encrypt(plainText, secretKey, "UTF-8");
    }

    private static CryptoModel encrypt(String plainText, SecretKey secretKey, String encoding) throws Exception {
        return encrypt(plainText.getBytes(encoding), secretKey);
    }

    private static CryptoModel encrypt(byte[] plainText, SecretKey secretKey) throws Exception{
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
    public static String decrypt(CryptoModel cipherModel, SecretKey secretKey) throws Exception{
        return decrypt(cipherModel, secretKey, "UTF-8");
    }

    public static String decrypt(String encryptedData, String encryptedIv, String seedKey) throws Exception {
        SecretKey secretKey = generateKey(seedKey);
        CryptoModel encryptedModel = new CryptoModel(encryptedData, new String(Base64.decode(encryptedIv, Base64.NO_WRAP)));

        return AESUtils.decrypt(encryptedModel, secretKey);
    }

    private static String decrypt(CryptoModel ciphers, SecretKey secretKey, String encoding) throws Exception {
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
    private static byte[] generateIv() throws Exception {
        return randomBytes(IV_LENGTH_BYTES);
    }

    private static byte[] randomBytes(int length) throws Exception {
        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        byte[] b = new byte[length];
        random.nextBytes(b);
        return b;
    }
}
