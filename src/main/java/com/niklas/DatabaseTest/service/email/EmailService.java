package com.niklas.DatabaseTest.service.email;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    // Eine Annotation in Java ist eine spezielle Art von Metadaten, die zu Klassen, Methoden, Feldern oder anderen Programmierelementen hinzugefügt wird.
    // Diese Metadaten geben zusätzliche Informationen, die von Tools, Frameworks oder zur Laufzeit verwendet werden können.

    // Wenn @Autowired vor einem Feld, Konstruktor oder Setter einer Klasse steht,
    // sucht "Spring" zur Laufzeit nach einer passenden Bean (einer konfigurierten Instanz) im Application Context, die zu dem Typ passt.

    // In diesem Beispiel wird Spring eine Instanz von JavaMailSender automatisch injizieren, sodass sie in der Klasse verwendet werden kann,
    // ohne dass der Entwickler selbst eine Instanz erstellen muss.
    @Autowired
    private JavaMailSender mailSender;


    @Value("${mail.sender.address}")
    private String senderAddress;
    @Value("${mail.sender.name}")
    private String senderName;

    // Methode um eine plain Text E-Mail zu senden
    public void sendEmail(String receiver, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Absenderadresse (From) setzen
        message.setFrom(senderAddress);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    // Methode um eine HTML-E-Mail zu senden
    public void sendHTMLEmail(String receiver, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        // Absenderadresse und Anzeigename im From setzen
        String fromAddress = String.format("%s <%s>", senderName, senderAddress);
        message.setFrom(fromAddress);
        message.setRecipients(MimeMessage.RecipientType.TO, receiver);
        message.setSubject(subject);

        message.setContent(body, "text/html; charset=utf-8");

        mailSender.send(message);
    }

}
