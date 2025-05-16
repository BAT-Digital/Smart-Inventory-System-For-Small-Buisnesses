package com.example.SmartInventorySystem.shared.model;

public class NotificationTypes {
    public enum NotificationType {
        // Inventory notifications
        LOW_STOCK,
        EXPIRATION_ALERT,
        STOCK_ADJUSTMENT,
        INVENTORY_COUNT,
        
        // System notifications
        SYSTEM_ERROR,
        SYSTEM_WARNING,
        SYSTEM_INFO,
        BACKUP_COMPLETED
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