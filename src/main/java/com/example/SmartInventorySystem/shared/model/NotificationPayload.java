package com.example.SmartInventorySystem.shared.model;

import com.example.SmartInventorySystem.shared.model.NotificationTypes.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Data
@Builder
public class NotificationPayload {
    private NotificationType type;
    private String message;
    private NotificationCategory category;
    private NotificationPriority priority;
    private LocalDateTime timestamp;
    private Map<String, String> metadata;

    public static class NotificationPayloadBuilder {
        private Map<String, String> metadata = new HashMap<>();

        public NotificationPayloadBuilder addMetadata(String key, String value) {
            this.metadata.put(key, value);
            return this;
        }

        public NotificationPayloadBuilder addMetadata(Map<String, String> metadata) {
            if (this.metadata == null) {
                this.metadata = new HashMap<>();
            }
            this.metadata.putAll(metadata);
            return this;
        }
    }

    public static NotificationPayloadBuilder builder() {
        return new NotificationPayloadBuilder()
            .timestamp(LocalDateTime.now());
    }
} 