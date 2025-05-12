import mqtt, { MqttClient, IClientOptions } from 'mqtt';
import { NotificationPayload } from '../types/notification';

type NotificationCallback = (payload: NotificationPayload) => void;

class MqttService {
    private client: MqttClient | null = null;
    private subscriptions: Map<string, Set<NotificationCallback>> = new Map();
    private reconnectTimeout: number = 1000;
    private maxReconnectTimeout: number = 30000;
    private connecting: boolean = false;

    connect(): void {
        if (this.connecting || this.client?.connected) {
            return;
        }

        this.connecting = true;
        const options: IClientOptions = {
            protocol: 'ws',
            hostname: 'localhost',
            port: 9001,
            path: '/mqtt',
            reconnectPeriod: 5000,
            connectTimeout: 30000,
            keepalive: 60,
            clean: true,
            resubscribe: true,
            clientId: `mqtt_${Math.random().toString(16).slice(2, 10)}`,
            protocolVersion: 4,
            rejectUnauthorized: false
        };

        console.log('Attempting to connect to MQTT broker with options:', options);
        
        try {
            this.client = mqtt.connect(`ws://${options.hostname}:${options.port}${options.path}`, options);

            this.client.on('connect', () => {
                console.log('Successfully connected to MQTT broker');
                this.connecting = false;
                this.reconnectTimeout = 1000;
                
                const topics = ['inventory/alerts/#', 'inventory/events/#', 'sales/events/#'];
                topics.forEach(topic => {
                    this.client?.subscribe(topic, (err) => {
                        if (err) {
                            console.error(`Failed to subscribe to ${topic}:`, err);
                        } else {
                            console.log(`Successfully subscribed to ${topic}`);
                        }
                    });
                });
            });

            this.client.on('message', (topic: string, message: Buffer) => {
                try {
                    const payload = JSON.parse(message.toString()) as NotificationPayload;
                    console.log(`Received message on topic ${topic}:`, payload);
                    
                    const subscribers = this.subscriptions.get(topic);
                    if (subscribers) {
                        subscribers.forEach(callback => callback(payload));
                    }
                } catch (error) {
                    console.error('Error processing message:', error);
                }
            });

            this.client.on('error', (error: Error) => {
                console.error('MQTT Error:', error);
                this.connecting = false;
            });

            this.client.on('close', () => {
                console.log('MQTT connection closed');
                this.connecting = false;
            });

            this.client.on('offline', () => {
                console.log('MQTT client is offline');
                this.connecting = false;
            });

            this.client.on('reconnect', () => {
                console.log('Attempting to reconnect to MQTT broker...');
            });
        } catch (error) {
            console.error('Error creating MQTT client:', error);
            this.connecting = false;
        }
    }

    subscribe(topic: string, callback: NotificationCallback): void {
        if (!this.subscriptions.has(topic)) {
            this.subscriptions.set(topic, new Set());
        }
        this.subscriptions.get(topic)?.add(callback);
        
        if (this.client?.connected) {
            this.client.subscribe(topic, (err) => {
                if (err) {
                    console.error(`Failed to subscribe to ${topic}:`, err);
                } else {
                    console.log(`Successfully subscribed to ${topic}`);
                }
            });
        } else {
            console.log(`Will subscribe to ${topic} once connected`);
            this.connect(); // Try to connect if not already connected
        }
    }

    unsubscribe(topic: string, callback: NotificationCallback): void {
        const subscribers = this.subscriptions.get(topic);
        if (subscribers) {
            subscribers.delete(callback);
            if (subscribers.size === 0 && this.client?.connected) {
                this.client.unsubscribe(topic);
                console.log(`Unsubscribed from ${topic}`);
            }
        }
    }

    disconnect(): void {
        if (this.client) {
            this.client.end(true, () => {
                console.log('MQTT client disconnected');
                this.client = null;
                this.connecting = false;
            });
        }
    }

    isConnected(): boolean {
        return this.client?.connected ?? false;
    }
}

export const mqttService = new MqttService(); 