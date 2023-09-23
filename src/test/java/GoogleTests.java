import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class GoogleTests extends BaseTest {
    @Test
    void testGoogle() {

        var url = propertiesConfigurator.getFinalProperties().getProperty("url");

        System.out.println(url);

        open(url);

    }
}
