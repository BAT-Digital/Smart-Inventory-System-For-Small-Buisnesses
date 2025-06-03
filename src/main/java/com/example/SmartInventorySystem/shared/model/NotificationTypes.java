package com.example.SmartInventorySystem.shared.model;

public class NotificationTypes {
    public enum NotificationType {
        // Inventory notifications
        LOW_STOCK,
        EXPIRATION_ALERT,
        STOCK_ADJUSTMENT,

        // System notifications
        SYSTEM_ERROR,
        SYSTEM_WARNING,
        SYSTEM_INFO
    }

    public enum NotificationCategory {
        INVENTORY,
        SYSTEM
    }

    public enum NotificationPriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }
} 