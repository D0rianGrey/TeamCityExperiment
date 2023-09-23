package org.example.utils;

public enum Environment {
    LOCAL_CHROME,
    LOCAL_FIREFOX,
    CLOUD_CHROME;

    public static Environment getEnumFromString(String stringValue) {
        try {
            return Environment.valueOf(stringValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("There is no selected env in properties file " + stringValue);
        }
    }
}
