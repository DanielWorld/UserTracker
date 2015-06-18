package com.namgyuworld.usertracker.util.cryptography;

import com.namgyuworld.usertracker.util.cryptography.model.CryptoModel;

import junit.framework.Assert;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.SecretKey;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/18/15.
 */
public class CryptoUtilTest {

    String message = "in 2() **73982&#^7JBog2ouds";

    @org.junit.Test
    public void testAES() throws UnsupportedEncodingException, GeneralSecurityException {

        String seedKey = "diD*#)  2388 2*DH#* d389";

        // 1. Generate key using String seedKey
        // AES 128 security requires 16 bytes (128bits) key
        SecretKey key = CryptoUtil.AESUtils.generateKey(seedKey);

        // 2. Encrypt message by key
        CryptoModel encryptModel = CryptoUtil.AESUtils.encrypt(message, key);

        // 3. Decrypt model
        String decryptedMessage = CryptoUtil.AESUtils.decrypt(encryptModel, key);

        Assert.assertEquals(message, decryptedMessage);

    }

    @org.junit.Test
    public void testRSA() throws NoSuchAlgorithmException, InvalidKeySpecException {

        // 1. Generate key
        KeyPair keyPair = CryptoUtil.RSAUtils.generateRSAKeyPair();

        // Example) Convert public & private key to String format
        keyPair.getPublic().getEncoded();
        keyPair.getPublic().getFormat(); // Returns the encoded form of this key, or {@code null} if encoding is not supported by this key.
        keyPair.getPrivate().getEncoded();
        keyPair.getPrivate().getFormat();

        // 2. Encrypt message
        String cipherMessage = CryptoUtil.RSAUtils.encrypt(message, keyPair.getPublic());

        // 3. Decrypt cipherMessage
        String decryptedMessage = CryptoUtil.RSAUtils.decrypt(cipherMessage, keyPair.getPrivate());

        Assert.assertEquals(message, decryptedMessage);

        // Additional Test
        Assert.assertEquals(keyPair.getPublic(), KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyPair.getPublic().getEncoded())));
        Assert.assertEquals(keyPair.getPrivate(), KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded())));
    }

    @org.junit.Test
    public void testHash(){
        String message2 = "in 2() **73982&#^7JBog2ouds";

        // 1. Create SHA1 hash
        String sha1Hash1 = CryptoUtil.CreateHash.SHA1(message);
        String sha1Hash2 = CryptoUtil.CreateHash.SHA1(message2);

        // Create SHA256 hash
        String sha256Hash1 = CryptoUtil.CreateHash.SHA256(message);
        String sha256Hash2 = CryptoUtil.CreateHash.SHA256(message2);

        Assert.assertEquals(sha1Hash1, sha1Hash2);
        Assert.assertEquals(sha256Hash1, sha256Hash2);
        Assert.assertNotSame(sha1Hash1, sha256Hash2);
        Assert.assertNotSame(sha1Hash2, sha256Hash1);
    }
}
