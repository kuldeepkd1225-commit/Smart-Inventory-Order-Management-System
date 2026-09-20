package com.enterprise.sioms.service;

public class EmailNotification implements NotificationService {

    @Override
    public void sendNotification(
            String recipient,
            String message
    ) {

        System.out.println(
                "Email sent to " + recipient + ": " + message
        );
    }
}