import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class GoogleTests {

    @Test
    void testGoogle() {
        open("https://www.google.com/");
    }
}
