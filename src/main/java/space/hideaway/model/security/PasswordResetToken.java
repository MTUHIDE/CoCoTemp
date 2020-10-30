package space.hideaway.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.*;
import org.springframework.mail.MailSender;

import org.springframework.mail.javamail.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.exceptions.UserNotFoundException;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.services.user.UserService;
import space.hideaway.services.user.UserServiceImplementation;

import space.hideaway.services.user.UserToolsService;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Entity
@Table
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;
    public PasswordResetToken() {
        super();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;



    public PasswordResetToken(final User user, final String token) {
        super();

        this.user = user;
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public String getToken(){
        return this.token;
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public User getUser(){
        return user;
    }



}
