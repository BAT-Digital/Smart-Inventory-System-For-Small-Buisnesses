package com.example.SmartInventorySystem.shared.model;

public class NotificationTypes {
    public enum NotificationType {
        // Inventory notifications
        LOW_STOCK,
        EXPIRATION_ALERT,
        NEW_BATCH,
        STOCK_ADJUSTMENT,
        INVENTORY_COUNT,
        
        // Sales notifications
        SALE_COMPLETED,
        HIGH_VALUE_SALE,
        REFUND_PROCESSED,
        DAILY_SALES_SUMMARY,
        
        // Supplier notifications
        ORDER_PLACED,
        ORDER_RECEIVED,
        ORDER_DELAYED,
        SUPPLIER_ISSUE,
        
        // System notifications
        SYSTEM_ERROR,
        SYSTEM_WARNING,
        SYSTEM_INFO,
        BACKUP_COMPLETED
    }

    public enum NotificationCategory {
        INVENTORY,
        SALES,
        SUPPLIER,
        SYSTEM
    }

    public enum NotificationPriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }
} 