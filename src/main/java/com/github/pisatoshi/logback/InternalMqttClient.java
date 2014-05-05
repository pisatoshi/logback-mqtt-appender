package com.github.pisatoshi.logback;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class InternalMqttClient extends MqttClient implements MqttCallback {
    private MqttClientPersistence clientPersistence;

    public InternalMqttClient(String serverURI, String clientId) throws MqttException {
        this(serverURI,clientId, new MqttDefaultFilePersistence());
    }

    public InternalMqttClient(String serverURI, String clientId, MqttClientPersistence persistence) throws MqttException {
        super(serverURI, clientId, persistence);
        clientPersistence = persistence;
    }

    @Override
    public void disconnect() throws MqttPersistenceException {
        clientPersistence.close();
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO reconnect
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
