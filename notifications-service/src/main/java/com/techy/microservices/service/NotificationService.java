package com.techy.microservices.service;

import com.techy.microservices.order.event.OrderPlacedEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "order-placed", groupId = "notification-group")
    public void listen(OrderPlacedEvent orderPlacedEvent) {
        LOGGER.info("Received OrderPlacedEvent: {}", orderPlacedEvent);

        String recipientEmail = orderPlacedEvent.getEmail().toString();
        String subject = "Order Confirmation - " + orderPlacedEvent.getOrderNumber();
        String message = "Dear Customer,\n\n" +
                "Thank you for your order! Your order number is: " + orderPlacedEvent.getOrderNumber() + "." +
                " We will notify you once it is shipped.\n\n" +
                "Best Regards,\nTechy Store";

        try {
            sendEmail(recipientEmail, subject, message);
            LOGGER.info("Order confirmation email sent successfully to {}", recipientEmail);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email to {}", recipientEmail, e);
        }
    }

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, false);
        mailSender.send(message);
    }
}
