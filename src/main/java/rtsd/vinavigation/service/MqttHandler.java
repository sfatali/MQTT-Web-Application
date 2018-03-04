package rtsd.vinavigation.service;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by Sabina on 12/6/2017.
 */
@Component
public class MqttHandler implements MqttCallback {
    private static final Logger LOGGER = Logger.getLogger(MqttHandler.class.getName());
    private static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
    private static final String clientID = "RTSDwebapp";
    private static final String[] topicArray = {"MKR1000-1", "MKR1000-2", "MKR1000-3"};

    private MqttClient mqttClient;
    @Autowired
    private MessageHandler messageHandler;

    public MqttHandler() {
    }

     public void run() {
        try {
            MqttConnectOptions connOpt = new MqttConnectOptions();
            connOpt.setCleanSession(false);
            connOpt.setKeepAliveInterval(1000000000);

            mqttClient = new MqttClient(BROKER_URL, clientID);
            mqttClient.setCallback(this);
            mqttClient.connect(connOpt);
            LOGGER.info("SUCCESSFULLY connected to broker ^_____^");

            subscribeToTopics();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        LOGGER.info("-------------------------------------------------");
        LOGGER.info("| Topic:" + topic);
        LOGGER.info("| Message: " + new String(message.getPayload()));
        LOGGER.info("-------------------------------------------------");

        String[] subtopicsArray = topic.split("/");
        String messageString = new String(message.getPayload());

        if(subtopicsArray.length == 2 && messageString.length() != 0) {
            String action = subtopicsArray[1];

            switch (action) {
                case "message":
                    messageHandler.handleMessage(subtopicsArray[0], messageString);
                    break;
                case "deviceStatus":
                    messageHandler.handleDeviceStatus(subtopicsArray[0], messageString);
                    break;
                case "messageStatus":
                    messageHandler.handleMessageStatus(subtopicsArray[0], messageString);
                    break;
                case "edit":
                    messageHandler.handleEdit(subtopicsArray[0], messageString);
                    break;
                default:
                    messageHandler.handleInvalidTopic("Invalid message topic: "
                            + subtopicsArray[1], messageString);
            }
        }
    }

    @Override
    public void connectionLost(Throwable t) {
        LOGGER.info("Connection lost! Reconnecting...");
        // 1 attempt to connect back
        run();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken var1) {
        //System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
    }

    private void subscribeToTopics() {
        for (String topic : topicArray) {
            try {
                mqttClient.subscribe(topic+"/+", 2);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
