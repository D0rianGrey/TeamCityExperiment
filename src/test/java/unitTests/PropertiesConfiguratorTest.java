package unitTests;

import org.example.utils.PropertiesConfigurator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Properties;

public class PropertiesConfiguratorTest {

    private PropertiesConfigurator propertiesConfigurator;
    private static final String TEST_KEY_FILE_ONLY = "test.key.fileonly";
    private static final String TEST_KEY_OVERRIDE = "test.key.override";
    private static final String TEST_KEY_SYSTEM_ONLY = "test.key.systemonly";

    @BeforeMethod
    public void setUp() {
        propertiesConfigurator = new PropertiesConfigurator();
        // Clear system properties that might be set by other tests or this test
        System.clearProperty(TEST_KEY_FILE_ONLY);
        System.clearProperty(TEST_KEY_OVERRIDE);
        System.clearProperty(TEST_KEY_SYSTEM_ONLY);
    }

    @AfterMethod
    public void tearDown() {
        // Clear system properties after each test to prevent interference
        System.clearProperty(TEST_KEY_FILE_ONLY);
        System.clearProperty(TEST_KEY_OVERRIDE);
        System.clearProperty(TEST_KEY_SYSTEM_ONLY);
    }

    @Test
    public void testPropertyLoadedFromFileWhenNotSetBySystem() {
        Properties finalProps = propertiesConfigurator.getFinalProperties();
        assertEquals(finalProps.getProperty(TEST_KEY_FILE_ONLY), "valueFromFileOnly",
                "Should retrieve the value from default.properties when no system property is set.");
        assertEquals(finalProps.getProperty("another.key"), "anotherValueFromFile",
                "Should retrieve another value from default.properties.");
    }

    @Test
    public void testSystemPropertyOverridesFileProperty() {
        String systemValue = "valueFromSystem";
        System.setProperty(TEST_KEY_OVERRIDE, systemValue);

        Properties finalProps = propertiesConfigurator.getFinalProperties();
        assertEquals(finalProps.getProperty(TEST_KEY_OVERRIDE), systemValue,
                "System property should override the value from default.properties.");
    }

    @Test
    public void testSystemPropertyIsNotAddedIfKeyNotInFile() {
        // This test verifies the current behavior: properties are only considered
        // if their keys are first present in the default.properties file.
        String systemValue = "valueSystemOnly";
        System.setProperty(TEST_KEY_SYSTEM_ONLY, systemValue);

        Properties finalProps = propertiesConfigurator.getFinalProperties();
        assertNull(finalProps.getProperty(TEST_KEY_SYSTEM_ONLY),
                "System property should not be added if its key is not in default.properties.");
    }
    
    @Test
    public void testDefaultValueLogicClarification() {
        // This test is to clarify the "default_value" part of PropertiesConfigurator.
        // Based on the current loop `Set<String> keys = loadPropertiesFromFile().stringPropertyNames();`,
        // a key will always come from the file.
        // System.getProperty(key) can be null.
        // propertiesFromFile.getProperty(key) should not be null for a key from `keys`.
        // Thus, the condition `else { finalValue = "default_value"; }` seems unreachable.

        // Let's ensure a file property, when no system property is set, doesn't get "default_value".
        Properties finalProps = propertiesConfigurator.getFinalProperties();
        assertEquals(finalProps.getProperty(TEST_KEY_OVERRIDE), "valueFromFileForOverride",
                "Value should be from file, not 'default_value', when no system prop is set for an existing file key.");
    }
}
