import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class GoogleTests {

    @Test
    void testGoogle() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--no-sandbox");
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--disable-dev-shm-usage");
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--headless");
        open("https://www.google.com/");
    }
}
