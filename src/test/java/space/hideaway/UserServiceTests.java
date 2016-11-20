package space.hideaway;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import space.hideaway.model.User;
import space.hideaway.services.UserManagementImpl;

/**
 * Created by dough on 11/1/2016.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoCoTempApplication.class})
public class UserServiceTests {

    @Rule
    public final ExpectedException userNotFoundExpected = ExpectedException.none();

    @Autowired
    UserManagementImpl userManagementImpl;

    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {


    }

    @Test
    public void usernameNotFound() throws Exception, UserNotFoundException {
        userNotFoundExpected.expect(UserNotFoundException.class);
        userManagementImpl.findByUsername("USER_THAT_DOESNT_EXIST");
    }

    @Test
    public void correctUserFound() throws Exception {
        final String TEST_USERNAME = "test";
        User testUser = null;
        try {
            testUser = userManagementImpl.findByUsername(TEST_USERNAME);
        } catch (UserNotFoundException e) {
            Assert.fail("The user wasn't found.");
        }
        Assert.assertEquals("User obtained should be the same as the user requested.", testUser.getUsername(), TEST_USERNAME);
    }
}
