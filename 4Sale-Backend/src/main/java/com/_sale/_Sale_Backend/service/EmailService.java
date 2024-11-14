package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.exception.EmailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String subject, String code, String expiryTime) throws EmailSendException {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("purnabdrranadev6@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // HTML content for the email
            String emailContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333333;'>" +
                    "<h2 style='color: #4CAF50;'>Your Verification Code</h2>" +
                    "<p style='font-size: 16px;'>Hello,</p>" +
                    "<p style='font-size: 16px;'>Your verification code is:</p>" +
                    "<h1 style='font-size: 28px; color: #333333; background-color: #f2f2f2; padding: 10px; text-align: center;'>" + code + "</h1>" +
                    "<p style='font-size: 16px;'>Please use this code within " + expiryTime + " as it will expire shortly.</p>" +
                    "<p style='font-size: 14px; color: #555555;'>If you did not request this code, please ignore this email.</p>" +
                    "<br><p style='font-size: 14px;'>Thank you,</p>" +
                    "<p style='font-size: 14px;'>The 4Sale Team</p>" +
                    "</body></html>";

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
            System.out.println("Mail sent successfully...");
        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send email: " + e.getMessage());
        }
    }
}
