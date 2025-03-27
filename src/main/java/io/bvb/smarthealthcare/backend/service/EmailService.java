package io.bvb.smarthealthcare.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String fromAddress = "bvijay123reddy@gmail.com";

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendWelcomeEMail(String to, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", name);
        // Load and process the HTML template
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process("welcome_email", context);

        helper.setTo(to);
        helper.setSubject("Welcome to our Smart Health Care Service");
        helper.setText(htmlContent, true); // true to enable HTML content
        helper.setFrom(fromAddress);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String to, String username, String resetlink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("resetLink", resetlink);
        // Load and process the HTML template
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process("reset_password_email", context);

        helper.setTo(to);
        helper.setSubject("Reset Your Password");
        helper.setText(htmlContent, true); // Enable HTML content
        helper.setFrom(fromAddress);
        mailSender.send(message);
    }
}


