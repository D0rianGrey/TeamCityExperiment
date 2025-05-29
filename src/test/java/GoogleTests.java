import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.GoogleSearchPage; // Import the new Page Object

public class GoogleTests extends BaseTest {
    @Description("Test description")
    @Test
    void testGoogle() {
        GoogleSearchPage googleSearchPage = new GoogleSearchPage(); // Instantiate the Page Object

        var url = propertiesConfigurator.getFinalProperties().getProperty("url");
        var env = propertiesConfigurator.getFinalProperties().getProperty("env");

        System.out.println("Url :: " + url);
        System.out.println("Env :: " + env);

        googleSearchPage.openPage(url); // Use the POM method to open the page
        // Example of using another POM method, though the original test didn't have assertions
        // googleSearchPage.verifyTitleContains("Google"); 
    }
}
