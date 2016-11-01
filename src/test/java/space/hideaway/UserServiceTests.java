package space.hideaway;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import space.hideaway.model.User;
import space.hideaway.services.UserServiceImplementation;

/**
 * Created by dough on 11/1/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoCoTempApplication.class})
public class UserServiceTests {

    @Autowired
    UserServiceImplementation userServiceImplementation;

    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {


    }

    @Test
    public void usernameNotFound() throws Exception {
        User userThatDoesntExist = userServiceImplementation.findByUsername("USER_THAT_DOESNT_EXIST");
        Assert.assertNull("User should not exist", userThatDoesntExist);
    }

    @Test
    public void correctUserFound() throws Exception {
        final String TEST_USERNAME = "test";
        User testUser = userServiceImplementation.findByUsername(TEST_USERNAME);
        Assert.assertEquals("User obtained should be the same as the user requested.", testUser.getUsername(), TEST_USERNAME);
    }
}
