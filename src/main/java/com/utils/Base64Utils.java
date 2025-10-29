package com.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {
    // Private constructor to prevent instantiation
    private Base64Utils() {}

    /**
     * Decodes a Base64-encoded string back into its original text.
     * @return The decoded plain text string.
     */
    public static String decode(String base64Text) {
        if (base64Text == null || base64Text.isEmpty()) {
            throw new IllegalArgumentException("Input Base64 text cannot be null or empty.");
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Text);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 input: " + base64Text, e);
        }
    }
}
