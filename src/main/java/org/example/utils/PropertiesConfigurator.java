package org.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesConfigurator {
    private Properties propertiesFromFile;
    protected Properties finalProperties;

    public PropertiesConfigurator() {
        propertiesFromFile = new Properties();
        finalProperties = new Properties();
    }

    private Properties loadPropertiesFromFile() {
        propertiesFromFile = new Properties();
        try (InputStream inputStream = PropertiesConfigurator.class.getClassLoader().getResourceAsStream("default.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("config.properties not found");
            }
            propertiesFromFile.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
        return propertiesFromFile;
    }

    public Properties getFinalProperties() {

        Set<String> keys = loadPropertiesFromFile().stringPropertyNames();

        for (String key : keys) {
            // Get the value from the system properties
            String valueFromSystem = System.getProperty(key);

            // Get the value from the properties file
            String valueFromFile = propertiesFromFile.getProperty(key);

            // Decide the final value
            String finalValue = (valueFromSystem != null && valueFromFile != null) ? valueFromSystem : (valueFromFile != null ? valueFromFile : "default_value");

            // Store the final value
            finalProperties.setProperty(key, finalValue);
        }

        return finalProperties;
    }
}
