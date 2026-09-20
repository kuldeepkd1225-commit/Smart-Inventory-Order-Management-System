package com.enterprise.sioms.service;

public class SMSNotification implements NotificationService {

    @Override
    public void sendNotification(
            String recipient,
            String message
    ) {

        System.out.println(
                "SMS sent to " + recipient + ": " + message
        );
    }
}