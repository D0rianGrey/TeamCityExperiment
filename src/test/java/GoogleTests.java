import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class GoogleTests {

    @Test
    void testGoogle() {
        WebDriverManager.chromedriver().setup();
        open("https://www.google.com/");
    }
}
