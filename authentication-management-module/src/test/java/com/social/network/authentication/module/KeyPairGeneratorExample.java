package com.social.network.authentication.module;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class KeyPairGeneratorExample {
    public static void main(String[] args) throws Exception {
        // Генерация ключей
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024); // Очень небезопасная длина ключа
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Получение публичного и приватного ключей
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Преобразование приватного ключа в строку
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        // Преобразование обратно в приватный ключ
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey convertedPrivateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        // Вывод информации
        System.out.println("Private Key (converted): " + privateKeyString);
    }
}
