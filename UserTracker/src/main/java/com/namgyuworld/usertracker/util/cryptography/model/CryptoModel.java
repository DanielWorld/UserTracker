package com.namgyuworld.usertracker.util.cryptography.model;

/**
 * Created by Daniel Park on 2015-06-16.
 */
public class CryptoModel {
    private String cryptoText;
    private String iv;

    public CryptoModel(String ct, String iv){
        this.cryptoText = ct;
        this.iv = iv;
    }

    public String getCryptoText() {
        return cryptoText;
    }

    public void setCryptoText(String cryptoText) {
        this.cryptoText = cryptoText;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    @Override
    public String toString() {
        return cryptoText+"::"+iv;
    }
}
