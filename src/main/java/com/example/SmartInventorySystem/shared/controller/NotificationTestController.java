package com.example.SmartInventorySystem.shared.controller;

import com.example.SmartInventorySystem.shared.service.NotificationService;
import com.example.SmartInventorySystem.shared.model.NotificationTypes.NotificationPriority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/test/notifications")
public class NotificationTestController {

    private final NotificationService notificationService;

    public NotificationTestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/test-all")
    public ResponseEntity<String> testAllNotifications() {
        try {
            // Test low stock notification
            notificationService.sendLowStockNotification("Test Product", 5, BigDecimal.valueOf(10));
            log.info("Sent low stock notification");

            // Test expiration notification
            LocalDateTime expiryDate = LocalDateTime.now().plusDays(5);
            notificationService.sendExpirationNotification("Test Product", expiryDate, 5);
            log.info("Sent expiration notification");

            // Test stock adjustment notification
            notificationService.sendStockAdjustmentNotification("Test Product", 100, "New stock arrival");
            log.info("Sent stock adjustment notification");

            // Test system notification
            notificationService.sendSystemNotification("System test notification", NotificationPriority.NORMAL);
            log.info("Sent system notification");

            return ResponseEntity.ok("All test notifications sent successfully!");
        } catch (Exception e) {
            log.error("Error sending test notifications", e);
            return ResponseEntity.internalServerError().body("Error sending notifications: " + e.getMessage());
        }
    }

    @PostMapping("/low-stock")
    public ResponseEntity<String> testLowStock() {
        try {
            notificationService.sendLowStockNotification("Test Product", 5, BigDecimal.valueOf(10));
            return ResponseEntity.ok("Low stock notification sent!");
        } catch (Exception e) {
            log.error("Error sending low stock notification", e);
            return ResponseEntity.internalServerError().body("Error sending notification: " + e.getMessage());
        }
    }

    @PostMapping("/expiration")
    public ResponseEntity<String> testExpiration() {
        try {
            LocalDateTime expiryDate = LocalDateTime.now().plusDays(5);
            notificationService.sendExpirationNotification("Test Product", expiryDate, 5);
            return ResponseEntity.ok("Expiration notification sent!");
        } catch (Exception e) {
            log.error("Error sending expiration notification", e);
            return ResponseEntity.internalServerError().body("Error sending notification: " + e.getMessage());
        }
    }

    @PostMapping("/stock-adjustment")
    public ResponseEntity<String> testStockAdjustment() {
        try {
            notificationService.sendStockAdjustmentNotification("Test Product", 100, "New stock arrival");
            return ResponseEntity.ok("Stock adjustment notification sent!");
        } catch (Exception e) {
            log.error("Error sending stock adjustment notification", e);
            return ResponseEntity.internalServerError().body("Error sending notification: " + e.getMessage());
        }
    }

    @PostMapping("/system")
    public ResponseEntity<String> testSystem() {
        try {
            notificationService.sendSystemNotification("System test notification", NotificationPriority.NORMAL);
            return ResponseEntity.ok("System notification sent!");
        } catch (Exception e) {
            log.error("Error sending system notification", e);
            return ResponseEntity.internalServerError().body("Error sending notification: " + e.getMessage());
        }
    }
} 