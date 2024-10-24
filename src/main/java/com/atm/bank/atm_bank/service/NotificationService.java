package com.atm.bank.atm_bank.service;


import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class NotificationService {

    private JavaMailSender mailSender;
    private MessageSource messageSource;

    public void sendNotification(String to, String subject, String body, String lang) {
        Locale locale = new Locale(lang);

        String localizedBody = messageSource.getMessage(body, null, locale);
        String localizedSubject = messageSource.getMessage(subject, null, locale);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(localizedSubject);
        message.setText(localizedBody);
        mailSender.send(message);

    }
}
