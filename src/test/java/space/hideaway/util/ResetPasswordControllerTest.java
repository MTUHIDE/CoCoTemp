//package space.hideaway.util;
//
//import org.junit.*;
//import org.mockito.*;
//import org.springframework.context.MessageSource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestParam;
//import space.hideaway.controllers.login.ResetPasswordController;
//import space.hideaway.model.User;
//import space.hideaway.model.security.GenericResponse;
//import space.hideaway.services.user.UserToolsService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.xml.ws.RequestWrapper;
//
//import java.util.Locale;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class ResetPasswordControllerTest {
//
//    @Test
//        public void TestShowChangePasswordPage(){
//
//        Locale expectedLocale = new Locale("English","USA");
//
//        Model MockModel = mock(Model.class);
//        long expectedId = (long) 45;
//        String expectedToken  = "Token";
//
//        ResetPasswordController resetPasswordController = new ResetPasswordController();
//
//        String expectedWebPageRedirect = "redirect:/updatePassword.html?lang="+ expectedLocale.getLanguage();
//
//        Assert.assertEquals(expectedWebPageRedirect,resetPasswordController.showChangePasswordPage(expectedLocale,MockModel,expectedId,expectedToken));
//
//    }
//
//
//}
