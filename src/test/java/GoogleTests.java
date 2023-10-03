import io.qameta.allure.Description;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class GoogleTests extends BaseTest {
    @Description("Test description")
    @Test
    void testGoogle() {

        var url = propertiesConfigurator.getFinalProperties().getProperty("url");
        var env = propertiesConfigurator.getFinalProperties().getProperty("env");

        System.out.println("Url :: " + url);
        System.out.println("Env :: " + env);

        open(url);
    }
}
