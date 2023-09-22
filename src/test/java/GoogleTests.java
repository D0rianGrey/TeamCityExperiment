import com.codeborne.selenide.Configuration;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class GoogleTests {

    @Test
    void testGoogle() {
        Configuration.headless = true;
        open("https://www.google.com/");
    }
}
