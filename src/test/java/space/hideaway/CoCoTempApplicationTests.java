package space.hideaway;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import space.hideaway.model.User;
import space.hideaway.services.UserServiceImplementation;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoCoTempApplication.class})
public class CoCoTempApplicationTests {

    Logger logger = Logger.getLogger(getClass());

    @Autowired
    UserServiceImplementation userServiceImplementation;

    private User testUser;

    @Before
    public void setUp() throws Exception, UserNotFoundException {
        this.testUser = userServiceImplementation.findByUsername("test");
    }

    @After
    public void tearDown() throws Exception {


    }

    @Test
    public void contextLoads() {
    }


}
