package com.restfulbackend.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by hejiang on 14/12/12.
 */
@Service("mailService")
public class ApplicationMailer {
    @Autowired
    private JavaMailSenderImpl mailSender;

    public static String EMAIL_FROM = "";

    /**
     * This method will send compose and send the message
     */
    public void sendMail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(EMAIL_FROM);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
