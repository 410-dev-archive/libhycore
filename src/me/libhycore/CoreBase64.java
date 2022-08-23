package me.libhycore;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CoreBase64 {
    public static String encode(String toEncode) {
        try {
            byte[] utf8StringBuffer = toEncode.getBytes(StandardCharsets.UTF_8);
            return Base64.getEncoder().encodeToString(utf8StringBuffer);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decode(String toDecode) {
        try {
            byte[] utf8StringBuffer = Base64.getDecoder().decode(toDecode.getBytes(StandardCharsets.UTF_8));
            return new String(utf8StringBuffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}