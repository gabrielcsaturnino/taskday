package com.example.taskday.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    private final JavaMailSender mailSender;
    
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    public void sendJobApplicationNotification(String contractorEmail, String jobTitle) {
        String subject = "Nova candidatura para vaga";
        String text = String.format("Você se candidatou para a vaga: %s", jobTitle);
        sendEmail(contractorEmail, subject, text);
    }
    
    public void sendJobApplicationAcceptedNotification(String contractorEmail, String jobTitle) {
        String subject = "Candidatura aceita!";
        String text = String.format("Parabéns! Sua candidatura para a vaga '%s' foi aceita.", jobTitle);
        sendEmail(contractorEmail, subject, text);
    }
    
    public void sendJobApplicationRejectedNotification(String contractorEmail, String jobTitle) {
        String subject = "Candidatura não aprovada";
        String text = String.format("Infelizmente, sua candidatura para a vaga '%s' não foi aprovada.", jobTitle);
        sendEmail(contractorEmail, subject, text);
    }
}
