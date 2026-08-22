package com.kpimailserver;

import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {
    private String senderEmail = System.getenv("MAIL_USERNAME");
    private String appPassword = System.getenv("MAIL_PASSWORD");
    private String receiverEmail = System.getenv("MAIL_RECEIVER");

    public void sendAlert(String report) {

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        senderEmail,
                        appPassword
                );
            }
        });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(senderEmail));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(receiverEmail)
            );

            message.setSubject("ALERTE MONITORING SERVEUR VIP02");

            message.setText(report);

            Transport.send(message);

            System.out.println("Mail envoyé avec succès !");

        } catch (MessagingException e) {

            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
