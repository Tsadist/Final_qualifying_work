package com.example.FQW.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSender {

    private JavaMailSender mailSender;

    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void  send(String emailCustomer, String title, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("");
        mailMessage.setTo(emailCustomer);
        mailMessage.setSubject(title);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
