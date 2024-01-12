package mockitoPractice;

public class LoginUI {
    private final UserService userService;

    public LoginUI(UserService userService) {
        this.userService = userService;
    }

    public String login(String username, String password) {
        if (userService.authenticate(username, password)) {
            return "Success";
        } else {
            return "Failure";
        }
    }
}
