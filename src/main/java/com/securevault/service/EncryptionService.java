package com.securevault.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Service
public class EncryptionService {

    private static final String SECRET_KEY = "1234567890123456";

    public byte[] encrypt(byte[] data) throws Exception {

        SecretKeySpec key =
                new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(data);

    }

    public byte[] decrypt(byte[] encryptedData) throws Exception {

        SecretKeySpec key =
                new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(encryptedData);

    }

}
