package com.pmt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


/**
 * Service encapsulant l'envoi d'e-mails via {@link JavaMailSender}.
 */
@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * Envoie un e-mail informant un utilisateur qu'une tâche lui a été assignée.
     *
     * @param to adresse e-mail du destinataire
     * @param taskName nom de la tâche attribuée
     * @param projectName nom du projet contenant la tâche
     * Le message inclut une salutation, le nom de la tâche et une invitation à
     * se connecter à la plateforme PMT pour plus de détails. Nécessite une
     * configuration SMTP valide et peut lever une {@link org.springframework.mail.MailException}
     * si l'envoi échoue.
     */
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
