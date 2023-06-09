package com.example.FQW.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMailSender {

    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender mailSender;

    public void send(String emailCustomer, String title, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailCustomer);
        mailMessage.setSubject(title);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
