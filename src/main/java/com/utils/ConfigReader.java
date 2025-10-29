package com.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String configPath = "src/test/resources/config.properties";
    private static final Properties prop = new Properties();

    private ConfigReader() {
    }

    static {
        try (InputStream input = new FileInputStream(configPath)) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file from: " + configPath, e);
        }
    }

    /**
     * Returns the value for a given key from config.properties.
     *
     * @param key Property key (e.g. "base.url", "username", "password")
     * @return Property value
     */
    public static String getProperty(String key) {
        String value = prop.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Property '" + key + "' not found in " + configPath);
        }
        return value.trim();
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    /**
     * Method to securely manage sensitive data like Login and by storing them in a Base64 encoded format
     * @return The decoded, plain-text username
     */
    public static String getUsername() {
        return Base64Utils.decode(getProperty("username"));
    }

    /**
     * Method to securely manage sensitive data like Password and by storing them in a Base64 encoded format
     * @return The decoded, plain-text password
     */
    public static String getPassword() {
        return  Base64Utils.decode(getProperty("password"));
    }

}