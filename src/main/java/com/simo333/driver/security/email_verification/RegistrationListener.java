package com.simo333.driver.security.email_verification;

import com.simo333.driver.model.EmailVerificationToken;
import com.simo333.driver.model.User;
import com.simo333.driver.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String CONFIRMATION_URI = "/api/auth/confirmEmail?token=";
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

        //TODO ADD USER EMAIL
        String userMail = user.getUsername();
        String subject = "Registration Confirmation";
        String confirmationUrl = appHostUrl + CONFIRMATION_URI + savedToken.getToken();
        String message = "Confirm your e-mail with link: " + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("DriverApp");
        email.setTo(userMail);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);

    }
}
