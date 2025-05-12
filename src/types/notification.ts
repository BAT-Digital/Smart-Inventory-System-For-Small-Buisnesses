export type NotificationType = 'LOW_STOCK' | 'EXPIRATION_ALERT' | 'NEW_BATCH' | 'SALE_COMPLETED';
export type NotificationCategory = 'INVENTORY' | 'SALES';

export interface NotificationPayload {
    type: NotificationType;
    message: string;
    category: NotificationCategory;
}

export interface Notification extends NotificationPayload {
    id: number;
    timestamp?: string;
} 