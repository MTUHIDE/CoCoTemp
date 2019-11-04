
package space.hideaway.services;

import jdk.jfr.events.ExceptionThrownEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import space.hideaway.model.User;
import space.hideaway.services.user.UserToolsService;

import java.util.Properties;
import java.util.UUID;




@Service
public class EmailVerificationService {

    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    @Autowired
    private UserToolsService userToolsService;

    @Autowired
    private Environment env;

    @Autowired
    private TaskExecutor taskExecutor;

    public void EmailVerificationService(){

    }

    public void sendVerificationEmail(User user, String appurl){
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    sendSimpleMail(user,appurl);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    private void sendSimpleMail(User user, String appurl){
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername(env.getProperty("spring.mail.username"));
        javaMailSender.setPassword(env.getProperty("spring.mail.password"));

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.debug","true");

        String token = UUID.randomUUID().toString();
        userToolsService.createVerificationTokenForUser(user,token);

        String recipientEmail = user.getEmail();
        String subject = "Confirm your email";
        String confirmationUrl = appurl+"/registrationConfirm.html?token="+token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText("Click the Link to confirm your email"+"\r\n"+env.getProperty("spring.mail.url")+confirmationUrl);
        javaMailSender.send(email);
    }


}
