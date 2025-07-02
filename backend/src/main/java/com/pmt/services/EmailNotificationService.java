package com.pmt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendTaskAssignmentNotification(String to, String taskName, String projectName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Nouvelle tâche assignée dans le projet " + projectName);
        message.setText("Bonjour,\n\nUne nouvelle tâche vous a été assignée : " + taskName +
                "\n\nConnectez-vous à la plateforme PMT pour plus de détails.\n\nCordialement.\nPMT Team");

        mailSender.send(message);
    }
}
