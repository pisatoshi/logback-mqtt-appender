package com.github.pisatoshi.logback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class MqttAppender extends UnsynchronizedAppenderBase<LoggingEvent> {

    private static final String MQTT_URL = "tcp://localhost:61613";
    private static final String MQTT_CLIENT_ID = "logmqtt";
    private static final String MQTT_CHANNEL = "mqttlogger";
    private static final String MQTT_USER = "admin";
    private static final String MQTT_PASSWORD = "password";

    private InternalMqttClient client;

    public MqttAppender() {
    }

    @Override
    public void start() {
        super.start();
        try {
            client = new InternalMqttClient(MQTT_URL, MQTT_CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(MQTT_USER);
            options.setPassword(MQTT_PASSWORD.toCharArray());
            client.setCallback(client);
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.stop();
    }

    @Override
    protected void append(LoggingEvent event) {
        String json = format(event);
        MqttMessage message = new MqttMessage();
        message.setQos(0);
        message.setPayload(json.getBytes());
        try {
            client.publish(MQTT_CHANNEL, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private final int DEFAULT_BUFFER_SIZE = 512;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");

    private String format(LoggingEvent event) {
        StringBuilder buf = new StringBuilder(DEFAULT_BUFFER_SIZE);

        buf.append(event.getFormattedMessage());
        buf.append("\t");
        buf.append(df.format(new Date(event.getTimeStamp())));
        buf.append("\t");
        buf.append(event.getLoggerName());
        buf.append("\t");
        buf.append(event.getThreadName());
        buf.append("\t");
        buf.append(event.getLevel().toString());
        return buf.toString();
    }
}
