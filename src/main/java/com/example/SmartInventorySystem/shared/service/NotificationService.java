package com.example.SmartInventorySystem.shared.service;

import com.example.SmartInventorySystem.shared.model.NotificationPayload;
import com.example.SmartInventorySystem.shared.model.NotificationTypes.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

@Slf4j
@Service
public class NotificationService {
    private final MqttClient mqttClient;
    private final ObjectMapper objectMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public NotificationService(MqttClient mqttClient, ObjectMapper objectMapper) {
        this.mqttClient = mqttClient;
        this.objectMapper = objectMapper;
    }

    private void publishNotification(String topic, NotificationPayload payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(1);
            mqttMessage.setRetained(false);
            mqttClient.publish(topic, mqttMessage);
            log.info("Published notification to topic: {}, type: {}", topic, payload.getType());
        } catch (Exception e) {
            log.error("Error publishing notification to topic: " + topic, e);
        }
    }

    // Inventory Notifications
    public void sendLowStockNotification(String productName, int currentStock, int threshold) {
        NotificationPayload payload = NotificationPayload.builder()
            .type(NotificationType.LOW_STOCK)
            .message(String.format("Low stock alert for %s: %d items remaining (threshold: %d)", 
                    productName, currentStock, threshold))
            .category(NotificationCategory.INVENTORY)
            .priority(currentStock == 0 ? NotificationPriority.URGENT : NotificationPriority.HIGH)
            .addMetadata("productName", productName)
            .addMetadata("currentStock", String.valueOf(currentStock))
            .addMetadata("threshold", String.valueOf(threshold))
            .build();
        
        publishNotification("inventory/alerts/stock", payload);
    }

    public void sendExpirationNotification(String productName, LocalDateTime expiryDate, int daysUntilExpiry) {
        NotificationPriority priority = daysUntilExpiry <= 7 ? NotificationPriority.HIGH :
                                      daysUntilExpiry <= 30 ? NotificationPriority.NORMAL :
                                      NotificationPriority.LOW;

        NotificationPayload payload = NotificationPayload.builder()
            .type(NotificationType.EXPIRATION_ALERT)
            .message(String.format("Product %s is expiring on %s (%d days remaining)", 
                    productName, expiryDate.format(DATE_FORMATTER), daysUntilExpiry))
            .category(NotificationCategory.INVENTORY)
            .priority(priority)
            .addMetadata("productName", productName)
            .addMetadata("expiryDate", expiryDate.format(DATE_FORMATTER))
            .addMetadata("daysUntilExpiry", String.valueOf(daysUntilExpiry))
            .build();

        publishNotification("inventory/alerts/expiration", payload);
    }

    public void sendStockAdjustmentNotification(String productName, int adjustment, String reason) {
        NotificationPayload payload = NotificationPayload.builder()
            .type(NotificationType.STOCK_ADJUSTMENT)
            .message(String.format("Stock adjusted for %s by %d units. Reason: %s", 
                    productName, adjustment, reason))
            .category(NotificationCategory.INVENTORY)
            .priority(NotificationPriority.NORMAL)
            .addMetadata("productName", productName)
            .addMetadata("adjustment", String.valueOf(adjustment))
            .addMetadata("reason", reason)
            .build();

        publishNotification("inventory/events/adjustment", payload);
    }

    // Sales Notifications
    public void sendSaleCompletedNotification(String transactionId, BigDecimal amount, int itemCount) {
        NotificationPayload payload = NotificationPayload.builder()
            .type(NotificationType.SALE_COMPLETED)
            .message(String.format("Sale completed: %d items sold for %s", itemCount, amount))
            .category(NotificationCategory.SALES)
            .priority(NotificationPriority.NORMAL)
            .addMetadata("transactionId", transactionId)
            .addMetadata("amount", amount.toString())
            .addMetadata("itemCount", String.valueOf(itemCount))
            .build();

        publishNotification("sales/events/completed", payload);

        // Check for high-value sale
        if (amount.compareTo(new BigDecimal("1000")) > 0) {
            sendHighValueSaleNotification(transactionId, amount);
        }
    }

    private void sendHighValueSaleNotification(String transactionId, BigDecimal amount) {
        NotificationPayload payload = NotificationPayload.builder()
            .type(NotificationType.HIGH_VALUE_SALE)
            .message(String.format("High-value sale completed: %s", amount))
            .category(NotificationCategory.SALES)
            .priority(NotificationPriority.HIGH)
            .addMetadata("transactionId", transactionId)
            .addMetadata("amount", amount.toString())
            .build();

        publishNotification("sales/events/high_value", payload);
    }

    public void sendRefundNotification(String transactionId, BigDecimal amount, String reason) {
        NotificationPayload payload = NotificationPayload.builder()
            .type(NotificationType.REFUND_PROCESSED)
            .message(String.format("Refund processed for transaction %s: %s", transactionId, amount))
            .category(NotificationCategory.SALES)
            .priority(NotificationPriority.HIGH)
            .addMetadata("transactionId", transactionId)
            .addMetadata("amount", amount.toString())
            .addMetadata("reason", reason)
            .build();

        publishNotification("sales/events/refund", payload);
    }

    // Supplier Notifications
    public void sendOrderPlacedNotification(String orderId, String supplierName, BigDecimal totalAmount) {
        NotificationPayload payload = NotificationPayload.builder()
            .type(NotificationType.ORDER_PLACED)
            .message(String.format("New order %s placed with %s for %s", orderId, supplierName, totalAmount))
            .category(NotificationCategory.SUPPLIER)
            .priority(NotificationPriority.NORMAL)
            .addMetadata("orderId", orderId)
            .addMetadata("supplierName", supplierName)
            .addMetadata("totalAmount", totalAmount.toString())
            .build();

        publishNotification("supplier/events/order_placed", payload);
    }

    public void sendOrderReceivedNotification(String orderId, String supplierName, boolean isComplete) {
        NotificationPayload payload = NotificationPayload.builder()
            .type(NotificationType.ORDER_RECEIVED)
            .message(String.format("Order %s received from %s - %s", 
                    orderId, supplierName, isComplete ? "Complete" : "Partial"))
            .category(NotificationCategory.SUPPLIER)
            .priority(NotificationPriority.NORMAL)
            .addMetadata("orderId", orderId)
            .addMetadata("supplierName", supplierName)
            .addMetadata("isComplete", String.valueOf(isComplete))
            .build();

        publishNotification("supplier/events/order_received", payload);
    }

    // System Notifications
    public void sendSystemNotification(String message, NotificationPriority priority) {
        NotificationType type = switch(priority) {
            case URGENT, HIGH -> NotificationType.SYSTEM_ERROR;
            case NORMAL -> NotificationType.SYSTEM_WARNING;
            case LOW -> NotificationType.SYSTEM_INFO;
        };

        NotificationPayload payload = NotificationPayload.builder()
            .type(type)
            .message(message)
            .category(NotificationCategory.SYSTEM)
            .priority(priority)
            .addMetadata("timestamp", LocalDateTime.now().toString())
            .build();

        publishNotification("system/events", payload);
    }

    public void sendBackupCompletedNotification(String backupLocation, boolean success) {
        NotificationPayload payload = NotificationPayload.builder()
            .type(NotificationType.BACKUP_COMPLETED)
            .message(String.format("System backup %s: %s", 
                    success ? "completed successfully" : "failed", backupLocation))
            .category(NotificationCategory.SYSTEM)
            .priority(success ? NotificationPriority.LOW : NotificationPriority.HIGH)
            .addMetadata("backupLocation", backupLocation)
            .addMetadata("success", String.valueOf(success))
            .build();

        publishNotification("system/events/backup", payload);
    }
} 