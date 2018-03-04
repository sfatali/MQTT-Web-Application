package rtsd.vinavigation.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rtsd.vinavigation.dao.DeviceDAO;
import rtsd.vinavigation.dao.MessageDAO;
import rtsd.vinavigation.dao.NavigationDAO;
import rtsd.vinavigation.model.DeviceStatus;
import rtsd.vinavigation.model.Message;
import rtsd.vinavigation.model.MessageStatus;
import rtsd.vinavigation.model.Navigation;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sabina on 11/22/2017.
 */
@Service
public class MessageHandler {

    @Autowired
    private NavigationDAO navigationDAO;
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private DeviceDAO deviceDAO;
    @Autowired
    private MessageValidator messageValidator;

    public MessageHandler() {
    }

    void handleMessage(String topicDeviceId, String messageString) {
        // validate device id
        try {
            if(!messageValidator.validateAddressee(topicDeviceId, new JSONObject(messageString))) {
                handleInvalidTopic("Invalid deviceId: " + topicDeviceId, messageString);
                return;
            }
        } catch (Exception ex) {
            handleInvalidTopic("Message JSON parsing problem: " + topicDeviceId, messageString);
            return;
        }

        // saving message - frequency/duration command ("navigation") or Morse code vibration
        Message message = new Message();

        boolean valid = messageValidator.validateMessage(message, new JSONObject(messageString));
        if (valid) {
            // now let's check if message is really new:
            if(messageDAO.count(message.getMessageId()) == 0) {
                messageDAO.insert(message);
            } else {
                messageDAO.insertFaultyMessageLog("Duplicate message: "+messageString);
            }

        } else {
            // invalid json was sent
            messageDAO.insertFaultyMessageLog("Message invalid: "+messageString);
        }
    }

    void handleDeviceStatus(String topicDeviceId, String messageString) {
        // validate device id
        try {
            if(!messageValidator.validateAddressee(topicDeviceId, new JSONObject(messageString))) {
                handleInvalidTopic("Invalid deviceId: " + topicDeviceId, messageString);
                return;
            }
        } catch (Exception ex) {
            handleInvalidTopic("Device status JSON parsing problem: " + topicDeviceId, messageString);
            return;
        }

        // updates on device connectivity
        DeviceStatus deviceStatus = new DeviceStatus();
        boolean valid = messageValidator.validateDeviceStatus(deviceStatus, new JSONObject(messageString));
        if (valid) {
            deviceDAO.insertDeviceStatus(deviceStatus);
        } else {
            // invalid json was sent
            messageDAO.insertFaultyMessageLog("Invalid device status: "+messageString);
        }
    }

    void handleMessageStatus(String topicDeviceId, String messageString) {
        // validate device id
        try{
            if(!messageValidator.validateAddressee(topicDeviceId, new JSONObject(messageString))) {
                handleInvalidTopic("Invalid deviceId: " + topicDeviceId, messageString);
                return;
            }
        }
        catch (Exception ex) {
            handleInvalidTopic("Message status JSON parsing problem: " + topicDeviceId, messageString);
            return;
        }

        // updating message status. it can success or error
        MessageStatus messageStatus = new MessageStatus();
        boolean valid = messageValidator.validateMessageStatus(messageStatus, new JSONObject(messageString));
        if (valid) {
            messageDAO.updateMessageStatus(messageStatus);
        } else {
            // invalid json was sent
            messageDAO.insertFaultyMessageLog("Invalid message status: "+messageString);
        }
    }

    void handleEdit(String topicDeviceId, String messageString) {
        // validate device id
        try {
            if(!messageValidator.validateAddressee(topicDeviceId, new JSONObject(messageString))) {
                handleInvalidTopic("Invalid deviceId: " + topicDeviceId, messageString);
                return;
            }
        } catch (Exception ex) {
            handleInvalidTopic("Navigation edit JSON parsing problem: " + topicDeviceId, messageString);
            return;
        }

        Navigation navigation = new Navigation();
        boolean valid = messageValidator.validateEdit(navigation, new JSONObject(messageString));
        if(valid) {
            navigationDAO.update(navigation);
        } else {
            messageDAO.insertFaultyMessageLog("Invalid edit message: "+messageString);
        }
    }

    void handleInvalidTopic(String topic, String messageString) {
        messageDAO.insertFaultyMessageLog(topic+"  Message: " + messageString);
    }

}
