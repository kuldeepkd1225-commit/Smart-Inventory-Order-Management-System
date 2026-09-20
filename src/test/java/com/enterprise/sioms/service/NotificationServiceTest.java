package com.enterprise.sioms.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceTest {

    @Test
    void testEmailNotification() {

        NotificationService notificationService =
                new EmailNotification();

        assertDoesNotThrow(() ->
                notificationService.sendNotification(
                        "test@sioms.com",
                        "Test email notification"
                )
        );
    }

    @Test
    void testSmsNotification() {

        NotificationService notificationService =
                new SMSNotification();

        assertDoesNotThrow(() ->
                notificationService.sendNotification(
                        "9876543210",
                        "Test SMS notification"
                )
        );
    }

    @Test
    void testNotificationPolymorphism() {

        NotificationService emailNotification =
                new EmailNotification();

        NotificationService smsNotification =
                new SMSNotification();

        assertNotNull(emailNotification);
        assertNotNull(smsNotification);

        assertDoesNotThrow(() ->
                emailNotification.sendNotification(
                        "test@sioms.com",
                        "Email test"
                )
        );

        assertDoesNotThrow(() ->
                smsNotification.sendNotification(
                        "9876543210",
                        "SMS test"
                )
        );
    }
}