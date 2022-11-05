package com.simo333.driver.security.email_verification;

import com.simo333.driver.model.EmailVerificationToken;
import com.simo333.driver.model.User;
import com.simo333.driver.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String CONFIRMATION_URI = "/api/auth/confirmEmail?token=";
    @Value("${spring.mail.username}")
    private String hostEmail;
    @Value("${app.hostUrl}")
    private String appHostUrl;
    private final EmailVerificationService service;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        EmailVerificationToken savedToken = service.createVerificationToken(user);

        MimeMessage email = buildMessage(user, savedToken);

        mailSender.send(email);
    }

    private MimeMessage buildMessage(User user, EmailVerificationToken token) {
        String userEmail = user.getEmail();
        String username = user.getUsername();
        String senderName = "DRIVER APP";
        String subject = "Registration Confirmation";
        String confirmationUrl = appHostUrl + CONFIRMATION_URI + token.getToken();
        String content = String.format("""
                Dear %s,<br>
                Please click the link below to verify your registration:<br>
                <h3><a href="%s" target="_self">VERIFY</a></h3>
                Thank you,<br>
                %s""", username, confirmationUrl, senderName);

        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email);

        try {
            helper.setFrom(hostEmail, senderName);
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new IllegalStateException("Mail could not be send");
        }
        return email;
    }
}
