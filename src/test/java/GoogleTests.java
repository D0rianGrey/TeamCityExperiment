import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class GoogleTests {

    @Test
    void testGoogle() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "src/main/java/org/example/drivers/chromedriver");
        open("https://www.google.com/");

//        WebDriver driver = new ChromeDriver();
//        driver.get("https://www.google.com/");
    }
}
