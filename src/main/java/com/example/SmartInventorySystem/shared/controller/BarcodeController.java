package com.example.SmartInventorySystem.shared.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BarcodeController {

    private final SimpMessagingTemplate messagingTemplate;

    public BarcodeController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // For mobile app to send barcode
    @MessageMapping("/barcode/send")
    @SendTo("/topic/barcodes")
    public String handleBarcode(String barcode) {
        return barcode;
    }

    // Alternatively, you can send to specific user session
    @MessageMapping("/barcode/send-user")
    public void handleBarcodeToUser(String barcode) {
        // Assuming you have user session management
        messagingTemplate.convertAndSendToUser(
                "someUserId",
                "/queue/barcodes",
                barcode
        );
    }
}
