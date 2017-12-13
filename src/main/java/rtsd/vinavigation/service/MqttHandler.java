package rtsd.vinavigation.service;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sabina on 12/6/2017.
 */
@Component
public class MqttHandler implements MqttCallback {

    MqttClient mqttClient;
    MqttConnectOptions connOpt;

    static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
    static final String clientID = "RTSDwebapp";
    static final String[] topicArray = {"MKR1000-1", "MKR1000-2", "MKR1000-3"};

    @Autowired
    MessageHandler messageHandler;

    public MqttHandler() {

    }

     public void run() {
        try {
            connOpt = new MqttConnectOptions();
            //connOpt.setAutomaticReconnect(true); this thing didn't work for some reason
            connOpt.setCleanSession(false);
            connOpt.setKeepAliveInterval(1000000000); // well, I don't know how to keep it alive forever

            mqttClient = new MqttClient(BROKER_URL, clientID);
            mqttClient.setCallback(this);
            mqttClient.connect(connOpt);
            System.out.println("SUCCESSFULLY connected to broker ^_____^");
            subscribeToTopics();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");

        String[] subtopicsArray = topic.split("/");
        String messageString = new String(message.getPayload());
        System.out.println("isDuplicate: "+message.isDuplicate());
        if(subtopicsArray.length == 2 && messageString.length() != 0/* && !message.isDuplicate()*/) {
            String action = subtopicsArray[1];

            if(action.equals("message")) {
                messageHandler.handleMessage(subtopicsArray[0], messageString);
            } else if(action.equals("deviceStatus")) {
                messageHandler.handleDeviceStatus(subtopicsArray[0], messageString);
            } else if(action.equals("messageStatus")) {
                messageHandler.handleMessageStatus(subtopicsArray[0], messageString);
            } else if(action.equals("edit")) {
                messageHandler.handleEdit(subtopicsArray[0], messageString);
            }
            else {
                messageHandler.handleInvalidTopic("Invalid message topic: " + subtopicsArray[1], messageString);
            }
        }
    }

    @Override
    public void connectionLost(Throwable t) {
        System.out.println("Connection lost!");
        // code to reconnect to the broker could be here...
        // 1 attempt to connect back
        System.out.println("Reconnecting...");
        run();

        // reconnect automatically didn't work
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken var1) {
        //System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
    }

    private void subscribeToTopics() {
        for (String topic : topicArray) {
            try {
                mqttClient.subscribe(topic+"/+", 2);
                System.out.println("SUBSCRIBED!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
