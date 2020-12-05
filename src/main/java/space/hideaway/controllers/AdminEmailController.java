package space.hideaway.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;
import space.hideaway.model.Email;
import space.hideaway.repositories.UserRepository;

import java.util.List;
import java.util.Properties;

@Controller
public class AdminEmailController {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    @Autowired
    private Environment env;

    private UserRepository userRepository;

    @Autowired
    public AdminEmailController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/adminEmail")
    public String email(Model model){
        model.addAttribute("email", new Email());
        return "adminsettings/adminEmail.html";
    }

    @RequestMapping(value = "/adminEmail", method = RequestMethod.POST)
    public String sendEmail(@ModelAttribute("email") Email email) {
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername(env.getProperty("spring.mail.username"));
        javaMailSender.setPassword(env.getProperty("spring.mail.password"));

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.debug","true");
        List<String> emails = userRepository.getOptInEmails();

        SimpleMailMessage message = new SimpleMailMessage();
        String recipent;

        String subject = email.getSubject();
        String body = email.getBody();
        message.setSubject(subject);
        message.setText(body);

        for (String e: emails){
            recipent = e;
            message.setTo(recipent);
            javaMailSender.send(message);
        }
        return "redirect:/adminsettings";
    }
}
