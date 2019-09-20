package space.hideaway.util;
import org.junit.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import space.hideaway.services.security.SecurityService;
import space.hideaway.services.security.SecurityServiceImplementation;
import space.hideaway.services.user.UserDetailsServiceImplementation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityServiceTest {

    AuthenticationManager mockAuthManager ;
            UserDetailsServiceImplementation mockUserDetailsService;

    @InjectMocks
    SecurityServiceImplementation securityServiceImplementation;


    @Mock
    SecurityContextHolder securityContextHolder = mock(SecurityContextHolder.class);



    @Before
    public void setUp(){
        mockAuthManager = mock(AuthenticationManager.class);
        mockUserDetailsService = mock(UserDetailsServiceImplementation.class);
        securityServiceImplementation = new SecurityServiceImplementation(mockAuthManager,mockUserDetailsService);
        MockitoAnnotations.initMocks(this);
    }

    @Test
        public void testConstructor(){
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        UserDetailsServiceImplementation mockUserDetailsService = mock(UserDetailsServiceImplementation.class);
        SecurityServiceImplementation serviceImplementation = new SecurityServiceImplementation(mockAuthManager,mockUserDetailsService);

        Assert.assertEquals(mockAuthManager,org.springframework.test.util.ReflectionTestUtils.getField(serviceImplementation, "authenticationManager"));
        Assert.assertEquals(mockUserDetailsService,org.springframework.test.util.ReflectionTestUtils.getField(serviceImplementation, "userDetailsServiceImplementation"));

    }
//TODO: Figure out how to test when can use SecurtityContextHolder.
//    @Test
//    public void testFindLoggedInUserName(){
//        SecurityContext mockContext = mock(SecurityContext.class);
//        Authentication mockAuth = mock(Authentication.class);
//        when(securityContextHolder.getContext()).thenReturn(mockContext);
//        when(mockContext.getAuthentication()).thenReturn(mockAuth);
//        String expectedName = "Jim";
//        when(mockAuth.getName()).thenReturn(expectedName);
//
//        Assert.assertEquals(expectedName, securityServiceImplementation.findLoggedInUsername());
//    }
//
//    @Test
//        public void testTryLogin(){
//
//    }
}
