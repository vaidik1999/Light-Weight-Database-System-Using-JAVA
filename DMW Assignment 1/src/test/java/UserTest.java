import static org.junit.Assert.*;

import java.util.List;

import org.example.DBA1.User;
import org.junit.Before;
import org.junit.Test;

//Reference : https://www.softwaretestinghelp.com/junit-tests-examples/
//Reference : https://www.tutorialspoint.com/junit/junit_test_framework.htm
public class UserTest {
    User user;

    @Before
    public void setUp() {
        user = new User("testuser", "testpass", "testquestion", "testanswer");
    }

    @Test
    public void testGetUserName() {
        assertEquals("testuser", user.getUserName());
    }

    @Test
    public void testSetUserName() {
        user.setUserName("newuser");
        assertEquals("newuser", user.getUserName());
    }

    @Test
    public void testGetPassword() {
        assertEquals("testpass", user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newpass");
        assertEquals("newpass", user.getPassword());
    }

    @Test
    public void testGetQuestion() {
        assertEquals("testquestion", user.getQuestion());
    }

    @Test
    public void testSetQuestion() {
        boolean result = user.loadUserbyUsername("testuser");
        user.setQuestion("newquestion");
        assertEquals("newquestion", user.getQuestion());
    }

    @Test
    public void testGetAnswer() {
        assertEquals("testanswer", user.getAnswer());
    }

    @Test
    public void testSetAnswer() {
        boolean result = user.loadUserbyUsername("testuser");
        user.setAnswer("newanswer");
        assertEquals("newanswer", user.getAnswer());
    }

    @Test
    public void testGetAllUsers() {
        List<User> allUsers = User.getAllUsers();
        assertNotNull(allUsers);
        assertTrue(allUsers.size() > 0);
    }

    @Test
    public void testLoadUserbyUsername() {
        boolean result = user.loadUserbyUsername("testuser");
        assertTrue(result);
        assertEquals("testuser", user.getUserName());
        assertEquals("What is your favorite color?", user.getQuestion());
        assertEquals("blue", user.getAnswer());

        result = user.loadUserbyUsername("invaliduser");
        assertFalse(result);
    }
}
