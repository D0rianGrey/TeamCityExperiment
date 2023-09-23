import com.codeborne.selenide.Configuration;
import org.example.utils.Environment;
import org.example.utils.PropertiesConfigurator;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;


public class BaseTest {
    protected PropertiesConfigurator propertiesConfigurator;

    BaseTest() {
        propertiesConfigurator = new PropertiesConfigurator();
    }

    @BeforeTest
    public void setUp() {

        Environment environment = Environment.getEnumFromString(propertiesConfigurator.getFinalProperties().getProperty("env"));

        switch (environment) {
            case LOCAL_CHROME -> {
                Configuration.browser = "chrome";
            }
            case LOCAL_FIREFOX -> {
                Configuration.browser = "firefox";
            }
            case CLOUD_CHROME -> {
                ChromeOptions chromeOptions = new ChromeOptions();
                Configuration.remote = "http://chromedriver:4444/wd/hub";
                Configuration.browserCapabilities = new DesiredCapabilities();
                Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            }
        }
    }
}
