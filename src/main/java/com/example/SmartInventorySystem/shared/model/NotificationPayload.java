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

    private NotificationPayload() {}

    public static NotificationPayloadBuilder builder() {
        return new NotificationPayloadBuilder()
                .timestamp(LocalDateTime.now());
    }

    public static class NotificationPayloadBuilder {
        private NotificationType type;
        private String message;
        private NotificationCategory category;
        private NotificationPriority priority;
        private LocalDateTime timestamp;
        private Map<String, String> metadata = new HashMap<>();

        public NotificationPayloadBuilder type(NotificationType type) {
            this.type = type;
            return this;
        }

        public NotificationPayloadBuilder message(String message) {
            this.message = message;
            return this;
        }

        public NotificationPayloadBuilder category(NotificationCategory category) {
            this.category = category;
            return this;
        }

        public NotificationPayloadBuilder priority(NotificationPriority priority) {
            this.priority = priority;
            return this;
        }

        public NotificationPayloadBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public NotificationPayloadBuilder addMetadata(String key, String value) {
            this.metadata.put(key, value);
            return this;
        }

        public NotificationPayload build() {
            NotificationPayload payload = new NotificationPayload();
            payload.type = this.type;
            payload.message = this.message;
            payload.category = this.category;
            payload.priority = this.priority;
            payload.timestamp = this.timestamp;
            payload.metadata = this.metadata;
            return payload;
        }
    }
}