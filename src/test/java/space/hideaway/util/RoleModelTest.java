package space.hideaway.util;
import org.junit.*;
import space.hideaway.model.Role;
import space.hideaway.model.User;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;

public class RoleModelTest {

    Role  role;

    @Before
        public void setUp(){

        role = new Role();

    }

    @Test
        public void TestSetAndGetId(){
        Long expectedId = (long)3423;

        role.setId(expectedId);

        Assert.assertEquals(expectedId,role.getId());

    }

    @Test
    public void TestSetAndGetName(){
        String expectedName = "name";

        role.setName(expectedName);

        Assert.assertEquals(expectedName,role.getName());

    }

    @Test
    public void TestSetAndGetUserSet(){
        User MockUser = mock(User.class);
        Set<User> userSet = new HashSet<User>();

        userSet.add(MockUser);
        role.setUserSet(userSet);

        Assert.assertEquals(userSet,role.getUserSet());

    }
}
