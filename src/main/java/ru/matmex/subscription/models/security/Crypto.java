package ru.matmex.subscription.models.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.matmex.subscription.services.notifications.email.EmailNotificationSender;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Component
public class Crypto {
    Cipher cipher;
    String transformation = "AES/ECB/PKCS5Padding";
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationSender.class);
    private final SecretKeySpec secretKey;

    public Crypto(@Value("${crypto.secretkey}") String rawSecretKey) {
        this.secretKey = new SecretKeySpec(rawSecretKey.getBytes(), "AES");
    }

    public byte[] encrypt(byte[] plainText) {
        try {
            cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(plainText);
        } catch (Exception e) {
            logger.error("Не удалось зашифровать данные!");
        }
        return null;
    }

    public byte[] decrypt(byte[] encryptedText) {
        try {
            cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedText);
        } catch (Exception e) {
            logger.error("Не удалось расшифровать данные!");
        }
        return null;
    }
}
