import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class GoogleTests {

    @Test
    void testGoogle() throws MalformedURLException {
////        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "src/main/java/org/example/drivers/chromedriver");
////        WebDriverManager.chromedriver().s
//        Configuration.browser = CHROME;
//        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions chromeOptions = new ChromeOptions();
        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://chromedriver:4444/wd/hub"), chromeOptions);
        driver.get("http://www.google.com");
        driver.quit();
//        open("https://www.google.com/");
////
//        WebDriver driver = new ChromeDriver();
//        driver.get("https://www.google.com/");
//        driver.quit();
    }
}
