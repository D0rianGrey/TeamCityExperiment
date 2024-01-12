package unitTests;

import mockitoPractice.LoginUI;
import mockitoPractice.UserService;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginUITest {

    @Test
    public void testSuccessfulLogin() {
        // Create a mock UserService
        UserService mockUserService = Mockito.mock(UserService.class);

        // Configure the mock to return true when authenticate is called with specific arguments
        when(mockUserService.authenticate("correctUser", "correctPass")).thenReturn(true);

        // Inject the mock into the LoginUI
        LoginUI ui = new LoginUI(mockUserService);

        // Test the login method
        String result = ui.login("correctUser", "correctPass");

        // Verify the result
        Assert.assertEquals("Success", result);

        // Optionally, verify that the mock was interacted with as expected
        verify(mockUserService).authenticate("correctUser", "correctPass");
    }

    @Test
    public void testFailedLogin() {
        // Similar setup as before
        UserService mockUserService = Mockito.mock(UserService.class);
        when(mockUserService.authenticate("wrongUser", "wrongPass")).thenReturn(false);
        LoginUI ui = new LoginUI(mockUserService);

        // Test the login method with incorrect credentials
        String result = ui.login("wrongUser", "wrongPass");

        // Verify the result
        Assert.assertEquals("Failure", result);

        // Verify interaction with the mock
        verify(mockUserService).authenticate("wrongUser", "wrongPass");
    }
}
