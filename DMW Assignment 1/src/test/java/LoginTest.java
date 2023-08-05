import org.example.DBA1.Login;
import org.junit.Test;
import static org.junit.Assert.*;

//Reference : https://www.softwaretestinghelp.com/junit-tests-examples/
//Reference : https://www.tutorialspoint.com/junit/junit_test_framework.htm
public class LoginTest {

    @Test
    public void testAuthenticateInvalidUsername() {
        Login login = new Login();
        assertFalse(login.authenticate("jane.doe", "password"));
    }

    @Test
    public void testAuthenticateInvalidPassword() {
        Login login = new Login();
        assertFalse(login.authenticate("john.doe", "wrongpassword"));
    }

    @Test
    public void testAuthenticateEmptyUsername() {
        Login login = new Login();
        assertFalse(login.authenticate("", "password"));
    }

    @Test
    public void testAuthenticateEmptyPassword() {
        Login login = new Login();
        assertFalse(login.authenticate("john.doe", ""));
    }

    @Test
    public void testAuthenticateNullUsername() {
        Login login = new Login();
        assertFalse(login.authenticate(null, "password"));
    }

    @Test
    public void testAuthenticateNullPassword() {
        Login login = new Login();
        assertFalse(login.authenticate("john.doe", null));
    }

    @Test
    public void testAuthenticateCaseSensitivity() {
        Login login = new Login();
        assertFalse(login.authenticate("John.Doe", "Password"));
    }


    @Test
    public void testAuthenticateInvalidAnswer() {
        Login login = new Login();
        assertFalse(login.authenticate("john.doe", "password"));
    }
}

