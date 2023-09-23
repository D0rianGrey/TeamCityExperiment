import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class GoogleTests {

    @BeforeTest
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        Configuration.remote = "http://chromedriver:4444/wd/hub";
        Configuration.browserCapabilities = new DesiredCapabilities();
        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
    }

    @Test
    void testGoogle() {
        Selenide.open("http://www.google.com");
    }
}
