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
            return;
        }

        // saving message - frequency/duration change ("navigation") or Morse code vibration
        Message message = new Message();
        JSONObject jsonObject = new JSONObject(messageString);

        boolean valid = messageValidator.validateMessage(message, jsonObject);
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
            return;
        }

        // updating message status. it can success or error
        MessageStatus messageStatus = new MessageStatus();
        boolean valid = messageValidator.validateMessageStatus(messageStatus, new JSONObject(messageString));
        if (valid) {
            messageDAO.updateMessageStatus(messageStatus);
            // this no longer makes any sense:
            /*if(messageStatus.getStatus() == 3) { // if message came with "success" status
                // let's see if it is the navigation message
                Navigation navigation = new Navigation();
                messageValidator.getNavigationFromStatusUpdate(navigation, new JSONObject(messageString));
                if (navigation.getType() != -1) {
                    // Let's get message's navigation params from database and check some stuff
                    // if params are not the same - then this is considered an error
                    Navigation navigationParamsFromDB = messageDAO.getNavigationParams(messageStatus.getMessageId());

                    if(navigationParamsFromDB != null &&
                            navigation.getDeviceId().equals(navigationParamsFromDB.getDeviceId()) &&
                            navigation.getType() == navigationParamsFromDB.getType() &&
                            navigation.getFrequency() == navigationParamsFromDB.getFrequency() &&
                            navigation.getDuration() == navigationParamsFromDB.getDuration()) {
                        // then everything is valid
                        navigationDAO.update(navigation);
                    } else {
                        // invalid case from Arduino
                        messageDAO.insertFaultyMessageLog("Invalid navigation params: "+messageString);
                    }
                }
            }*/
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
