import org.example.DBA1.Register;
import org.example.DBA1.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Reference : https://www.softwaretestinghelp.com/junit-tests-examples/
//Reference : https://www.tutorialspoint.com/junit/junit_test_framework.htm
class RegisterTest {

    private Register register;

    @BeforeEach
    void setUp() {
        register = new Register();
    }

    @Test
    void registerUserSuccess() {
        User user = new User("testuser3", "password", "What is your favorite color?", "blue");
        int result = register.registerUser(user);
        assertEquals(0, result);
    }

    @Test
    void registerUser_duplicateUsername() {
        User user1 = new User("testuser", "password", "What is your favorite color?", "blue");
        User user2 = new User("testuser", "password", "What is your favorite animal?", "dog");
        register.registerUser(user1);
        int result = register.registerUser(user2);
        assertEquals(1, result);
    }

    @Test
    void registerUser_fileError() {
        User user = null;
        int result = register.registerUser(user);
        assertEquals(2, result);
    }
}
