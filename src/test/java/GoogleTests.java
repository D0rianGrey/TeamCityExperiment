import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class GoogleTests extends BaseTest {
    @Test
    void testGoogle() {

        var url = propertiesConfigurator.getFinalProperties().getProperty("url");
        var env = propertiesConfigurator.getFinalProperties().getProperty("env");

        System.out.println(url);
        System.out.println(env);

        open(url);

    }
}
