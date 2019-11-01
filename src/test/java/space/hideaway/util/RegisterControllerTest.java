package space.hideaway.util;

import org.junit.*;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import space.hideaway.controllers.registration.RegisterController;
import space.hideaway.model.User;
import space.hideaway.services.EmailVerificationService;
import space.hideaway.services.security.SecurityService;
import space.hideaway.services.user.UserService;
import space.hideaway.services.user.UserToolsService;
import space.hideaway.validation.PersonalDetailsValidator;
import space.hideaway.validation.UserAccountValidator;

import static org.mockito.Mockito.*;


public class RegisterControllerTest {


    RegisterController registerController;
    UserService MockUserService;
    SecurityService MockSecurityService;
    UserAccountValidator MockUserAccountValidator;
    PersonalDetailsValidator MockPersonalDetailsValidator;
    EmailVerificationService MockEmailVerificationService;
    UserToolsService MockUserToolsService;

    @Before

        public void setUp(){

         MockUserService = mock(UserService.class);
        MockSecurityService = mock(SecurityService.class);
        MockUserAccountValidator = mock(UserAccountValidator.class);
        MockPersonalDetailsValidator = mock(PersonalDetailsValidator.class);
        MockEmailVerificationService = mock(EmailVerificationService.class);
        MockUserToolsService = mock(UserToolsService.class);


        registerController = new RegisterController(MockSecurityService,MockUserAccountValidator,MockUserService,MockPersonalDetailsValidator,MockEmailVerificationService,MockUserToolsService);

    }


    @Test
    public void TestInit(){

    Assert.assertNotNull(registerController);
    }

    @Test
        public void TestInitialPage(){
        ModelMap MockModelMap = mock(ModelMap.class);
        Assert.assertEquals("registration/register",registerController.initialPage(MockModelMap));
    }

    @Test
        public void TestQuestionPageNoErrors(){
        User MockUser = mock(User.class);
        BindingResult MockBindingResult = mock(BindingResult.class);
        doNothing().when(MockUserAccountValidator).validate(MockUser,MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(false);
        Assert.assertEquals("registration/questionPage",registerController.questionPage(MockUser,MockBindingResult));
        verify(MockUserAccountValidator).validate(MockUser,MockBindingResult);
    }

    @Test
        public void TestQuestionPageWithErrors(){
        User MockUser = mock(User.class);
        BindingResult MockBindingResult = mock(BindingResult.class);
        doNothing().when(MockUserAccountValidator).validate(MockUser,MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(true);
        Assert.assertEquals("registration/register",registerController.questionPage(MockUser,MockBindingResult));
        verify(MockUserAccountValidator).validate(MockUser,MockBindingResult);
    }

    @Test
        public void TestProcessFinishNoErrors(){
        User MockUser = mock(User.class);
        WebRequest mockRequest = mock(WebRequest.class);
        SessionStatus MockSessionStatus = mock(SessionStatus.class);
        String expectedUserName = "CoCoTemp";
        String expectedPassword = "password";
        BindingResult MockBindingResult = mock(BindingResult.class);
        doNothing().when(MockPersonalDetailsValidator).validate(MockUser,MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(false);
        when(MockUser.getUsername()).thenReturn(expectedUserName);
        when(MockUser.getPassword()).thenReturn(expectedPassword);
        doNothing().when(MockUserService).save(MockUser);
        doNothing().when(MockSecurityService).autoLogin(expectedUserName,expectedPassword);
        doNothing().when(MockSessionStatus).setComplete();



        Assert.assertEquals("registration/emailVerification",registerController.processFinish(MockUser,MockBindingResult,MockSessionStatus,mockRequest));
        verify(MockUser,times(1)).getUsername();
        verify(MockUser,times(1)).getPassword();
        verify(MockUserService,times(1)).save(MockUser);
        verify(MockSessionStatus,times(1)).setComplete();

        verify(MockPersonalDetailsValidator).validate(MockUser,MockBindingResult);

    }

    @Test
    public void TestProcessFinishErrors() {
        User MockUser = mock(User.class);
        SessionStatus MockSessionStatus = mock(SessionStatus.class);
        String expectedUserName = "CoCoTemp";
        String expectedPassword = "password";
        WebRequest mockRequest = mock(WebRequest.class);
        BindingResult MockBindingResult = mock(BindingResult.class);
        doNothing().when(MockPersonalDetailsValidator).validate(MockUser, MockBindingResult);
        when(MockBindingResult.hasErrors()).thenReturn(true);
        when(MockUser.getUsername()).thenReturn(expectedUserName);
        when(MockUser.getPassword()).thenReturn(expectedPassword);
        doNothing().when(MockUserService).save(MockUser);
        doNothing().when(MockSecurityService).autoLogin(expectedUserName, expectedPassword);
        doNothing().when(MockSessionStatus).setComplete();


        Assert.assertEquals("registration/questionPage", registerController.processFinish(MockUser, MockBindingResult, MockSessionStatus,mockRequest));
        verify(MockUser, times(1)).getUsername();
        verify(MockUser, times(1)).getPassword();
        verifyZeroInteractions(MockUserService);
        verifyZeroInteractions(MockSecurityService);
        verifyZeroInteractions(MockSessionStatus);

        verify(MockPersonalDetailsValidator).validate(MockUser, MockBindingResult);
    }

    @Test
        public void TestProcessCancel(){
        SessionStatus MockSessionStatus = mock(SessionStatus.class);

        doNothing().when(MockSessionStatus).setComplete();

        Assert.assertEquals("index",registerController.processCancel(MockSessionStatus));
        verify(MockSessionStatus, times(1)).setComplete();


    }
}



