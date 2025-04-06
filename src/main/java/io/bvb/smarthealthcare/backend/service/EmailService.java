package io.bvb.smarthealthcare.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String mailFromAddress;

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
        helper.setFrom(mailFromAddress);

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
        helper.setFrom(mailFromAddress);
        mailSender.send(message);
    }


    public void sendDoctorApprovalEmail(String toEmail, String name) {
        Context context = new Context();
        context.setVariable("name", name);

        String body = templateEngine.process("approval-template.html", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(toEmail);
            helper.setSubject("Doctor Registration Approved");
            helper.setFrom(mailFromAddress);
            helper.setText(body, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendRejectionMail(String toEmail, String username) {
        Context context = new Context();
        context.setVariable("username", username);
        String htmlContent = templateEngine.process("doctor_rejection_mail.html", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Doctor Registration Rejected");
            helper.setText(htmlContent, true);
            helper.setFrom(mailFromAddress);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}


