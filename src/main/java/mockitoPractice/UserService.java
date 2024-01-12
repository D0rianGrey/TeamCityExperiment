package mockitoPractice;

public class UserService {
    public boolean authenticate(String username, String password) {
        // Imagine this method checks against a database
        return "user".equals(username) && "pass".equals(password);
    }
}
