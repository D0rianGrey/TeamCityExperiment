import com.codeborne.selenide.Configuration;
import org.example.utils.Environment;
import org.example.utils.PropertiesConfigurator;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;

import java.util.Map;


public class BaseTest {
    protected PropertiesConfigurator propertiesConfigurator;
    private static final String SELENOID_URL = "http://143.244.207.125:8080/wd/hub";

    private final Map<Environment, Runnable> setupStrategies;

    public BaseTest() {
        propertiesConfigurator = new PropertiesConfigurator();
        setupStrategies = Map.of(
                Environment.LOCAL_CHROME, this::setupLocalChrome,
                Environment.LOCAL_FIREFOX, this::setupLocalFirefox,
                Environment.CLOUD_CHROME, this::setupCloudChrome,
                Environment.SELENOID_CHROME, this::setupSelenoidChrome,
                Environment.SELENOID_FIREFOX, this::setupSelenoidFirefox);

    }

    @BeforeTest
    public void setUp() {
        Environment environment = Environment.getEnumFromString(propertiesConfigurator.getFinalProperties().getProperty("env"));

        // approach with runable and map
        setupStrategies.get(environment).run();

        // approach without runable and with switch case
//        switch (environment) {
//            case LOCAL_CHROME -> setupLocalChrome();
//            case LOCAL_FIREFOX -> setupLocalFirefox();
//            case CLOUD_CHROME -> setupCloudChrome();
//            case SELENOID_CHROME -> setupSelenoidChrome();
//            case SELENOID_FIREFOX -> setupSelenoidFirefox();
//        }
    }

    private void setupLocalChrome() {
        Configuration.browser = "chrome";
    }

    private void setupLocalFirefox() {
        Configuration.browser = "firefox";
    }

    private void setupCloudChrome() {
        Configuration.browser = "chrome";
        setupCapabilities(new ChromeOptions(), "http://chromedriver:4444/wd/hub");
    }

    private void setupSelenoidChrome() {
        Configuration.browser = "chrome";
        setupCapabilities(new ChromeOptions(), SELENOID_URL);
    }

    private void setupSelenoidFirefox() {
        Configuration.browser = "firefox";
        setupCapabilities(new FirefoxOptions(), SELENOID_URL);
    }

    private void setupCapabilities(Object options, String url) {
        Configuration.browserCapabilities = new DesiredCapabilities();
        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        Configuration.remote = url;
    }
}
