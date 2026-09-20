package com.enterprise.sioms.service;

public interface NotificationService {

    void sendNotification(
            String recipient,
            String message
    );
}