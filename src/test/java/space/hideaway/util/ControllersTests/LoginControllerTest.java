package space.hideaway.util.ControllersTests;
import org.junit.Assert;
import org.springframework.ui.Model;
import space.hideaway.controllers.login.LoginController;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class LoginControllerTest {

    @Test
        public void TestShowLogin(){
        LoginController loginController = new LoginController();
        Assert.assertEquals("login/login",loginController.showLogin());
    }
    @Test
        public void TestShowLoginError(){
        Model mockModel = mock(Model.class);
        LoginController loginController = new LoginController();
        Assert.assertEquals("login/login",loginController.showLoginError(mockModel));
        verify(mockModel).addAttribute("loginError",true);

    }

}
