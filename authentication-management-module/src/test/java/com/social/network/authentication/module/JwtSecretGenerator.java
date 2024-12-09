package com.social.network.authentication.module;

import java.security.SecureRandom;

public class JwtSecretGenerator {
    public static void main(String[] args) {
        String secret = generateSecretKey();
        System.out.println("Generated Secret Key: " + secret);
    }

    public static String generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32]; // 256 бит (32 байта) для ключа в шестнадцатеричном формате
        secureRandom.nextBytes(key);

        StringBuilder secretKey = new StringBuilder();
        for (byte b : key) {
            secretKey.append(String.format("%02x", b));
        }

        return secretKey.toString();
    }

}
