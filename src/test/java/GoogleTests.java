import com.codeborne.selenide.Configuration;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Selenide.open;

public class GoogleTests {

    @Test
    void testGoogle() {
////        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "src/main/java/org/example/drivers/chromedriver");
////        WebDriverManager.chromedriver().s
        Configuration.browser = CHROME;
        open("https://www.google.com/");
////
//        WebDriver driver = new ChromeDriver();
//        driver.get("https://www.google.com/");
//        driver.quit();
    }
}
