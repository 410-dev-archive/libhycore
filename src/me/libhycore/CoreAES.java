package me.libhycore;

import me.libhycore.exceptions.InvalidInputException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class CoreAES {

    public static String encrypt(String input, String keyStr) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidInputException {

        if (input == null || keyStr == null) {
            throw new InvalidInputException("Input or key is null");
        }

        if (input.length() == 0 || keyStr.length() == 0) {
            return input;
        }

        keyStr = CoreSHA.hash512(keyStr);
        input = CoreBase64.encode(input);
        SecretKeySpec secretKey;
        MessageDigest sha = null;
        String encryptedString = "";
        byte[] key;
        key = keyStr.getBytes(StandardCharsets.UTF_8);
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] pass = cipher.doFinal(input.trim().getBytes(StandardCharsets.UTF_8));

        encryptedString = Base64.getEncoder().encodeToString(pass);
        encryptedString = CoreBase64.encode(encryptedString);

        return encryptedString;
    }

    public static String decrypt(String input, String keyStr) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidInputException {

        if (input == null || keyStr == null) {
            throw new InvalidInputException("Input or key is null");
        }

        if (input.length() == 0 || keyStr.length() == 0) {
            return input;
        }

        keyStr = CoreSHA.hash512(keyStr);
        input = CoreBase64.decode(input);
        byte[] key;
        String decryptedString = "";
        SecretKeySpec secretKey;

        MessageDigest sha = null;
        key = keyStr.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] pass = input.trim().getBytes();
        decryptedString = (new String(cipher.doFinal(Base64.getDecoder().decode(pass))));
        decryptedString = CoreBase64.decode(decryptedString);

        return decryptedString;
    }
}