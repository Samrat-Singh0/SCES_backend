package com.example.mainBase.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  public EmailServiceImpl(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendEmail(String to, String subject, String body) {
    MimeMessage message = mailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setFrom("sces307@gmail.com");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, true);

      mailSender.send(message);

      
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }

//    SimpleMailMessage message = new SimpleMailMessage();
//    message.setFrom("sces307@gmail.com");
//    message.setTo(to);
//    message.setSubject(subject);
//    message.setText(body);
//    mailSender.send(message);
  }
}
