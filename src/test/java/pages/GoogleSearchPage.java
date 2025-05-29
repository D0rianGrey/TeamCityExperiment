package pages;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;
import static org.testng.Assert.assertTrue;

public class GoogleSearchPage {

    // Method to open the Google search page
    public void openPage(String url) {
        open(url);
    }

    // Example of a method that might be used for assertions or further interactions
    public void verifyTitleContains(String expectedText) {
        assertTrue(title().toLowerCase().contains(expectedText.toLowerCase()),
                "Page title does not contain '" + expectedText + "'. Current title: '" + title() + "'");
    }

    // Example of a search method if the test were more complex
    // public void searchFor(String searchTerm) {
    //     // SelenideElement searchInput = $(By.name("q")); // Example locator
    //     // searchInput.setValue(searchTerm);
    //     // searchInput.pressEnter();
    // }
}
