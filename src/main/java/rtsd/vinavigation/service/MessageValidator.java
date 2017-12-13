package rtsd.vinavigation.service;

import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import rtsd.vinavigation.model.DeviceStatus;
import rtsd.vinavigation.model.Message;
import rtsd.vinavigation.model.MessageStatus;
import rtsd.vinavigation.model.Navigation;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sabina on 12/6/2017.
 */
@Component
public class MessageValidator {

    private SimpleDateFormat dateFormat;

    public MessageValidator() {
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    }

    boolean validateAddressee(String expectedAddressee, JSONObject jsonObject) {
        return isValidStringField(jsonObject, "deviceId") &&
                expectedAddressee.equals(jsonObject.getString("deviceId"));
    }

    boolean validateMessage(Message message, JSONObject jsonObject) {
        message.setDeviceId(jsonObject.getString("deviceId"));

        if(isValidStringField(jsonObject, "messageId")) {
            message.setMessageId(jsonObject.getString("messageId"));
        } else {
            return false;
        }

        if(isValidIntField(jsonObject, "command")) {
            message.setCommand(jsonObject.getInt("command"));
        } else {
            return false;
        }

        if(isValidIntField(jsonObject, "frequency")) {
            message.setFrequency(jsonObject.getInt("frequency"));
        } else {
            return false;
        }

        if(isValidIntField(jsonObject, "pulseDuration")) {
            message.setDuration(jsonObject.getInt("pulseDuration"));
        } else {
            return false;
        }

        if(isValidStringField(jsonObject, "message") && jsonObject.getInt("command") == 4) {
            message.setMessageText(jsonObject.getString("message"));
        } else if(jsonObject.getInt("command") == 4) {
            return false;
        }

        if(isValidStringField(jsonObject, "publishDate")) {
            try {
                message.setPublishDate(dateFormat.parse(jsonObject.getString("publishDate")));
            }
            catch (Exception e) {
                return false;
            }
        } else {
            message.setPublishDate(new Date());
        }

        message.setStatus(5);
        message.setStatusDate(new Date());

        return true;
    }

    boolean validateDeviceStatus(DeviceStatus deviceStatus, JSONObject jsonObject) {
        deviceStatus.setDeviceId(jsonObject.getString("deviceId"));

        if(isValidIntField(jsonObject, "status")) {
            deviceStatus.setStatus(jsonObject.getInt("status"));
        } else {
            return false;
        }

        if(isValidStringField(jsonObject, "statusDate")) {
            try {
                deviceStatus.setStatusDate(dateFormat.parse(jsonObject.getString("statusDate")));
            }
            catch (Exception e) {
                deviceStatus.setStatusDate(new Date());
            }
        } else {
            deviceStatus.setStatusDate(new Date());
        }

        return true;
    }

    boolean validateMessageStatus(MessageStatus messageStatus, JSONObject jsonObject) {
        if(isValidStringField(jsonObject, "messageId")) {
            messageStatus.setMessageId(jsonObject.getString("messageId"));
        } else {
            return false;
        }

        if(isValidIntField(jsonObject, "commandStatus")) {
            messageStatus.setStatus(jsonObject.getInt("commandStatus"));
        } else {
            return false;
        }

        if(!isValidIntField(jsonObject, "mode")) {
            return false; // if there is no mode specified, then this an erroneous case
        }

        if (!isValidIntField(jsonObject, "frequency")) {
            return false; // if Arduino does not send the valid info - that's a problem
        }

        if (!isValidIntField(jsonObject, "pulseDuration")) {
            return false; // if Arduino does not send the valid info - that's a problem
        }

        messageStatus.setStatusDate(new Date());
        return true;
    }

    // no need for that anymore:
    /*void getNavigationFromStatusUpdate(Navigation navigation, JSONObject jsonObject) {
        if (jsonObject.getInt("mode") == 1 || jsonObject.getInt("mode") == 2 || jsonObject.getInt("mode") == 3) {
            navigation.setType(jsonObject.getInt("mode"));
            navigation.setFrequency(jsonObject.getInt("frequency"));
            navigation.setDuration(jsonObject.getInt("pulseDuration"));
        } else {
            navigation.setType(-1); // this was morse code message
        }
        navigation.setDeviceId(jsonObject.getString("deviceId"));
    }*/

    private boolean isValidStringField(JSONObject jsonObject, String field) {
        return jsonObject.has(field) && !jsonObject.optString(field, "null").equals("null");
    }

    private boolean isValidIntField(JSONObject jsonObject, String field) {
        return jsonObject.has(field) && ! (jsonObject.optInt(field, -10) == -10);
    }

     boolean validateEdit(Navigation navigation, JSONObject jsonObject) {
        navigation.setDeviceId(jsonObject.getString("deviceId"));

        if(isValidIntField(jsonObject, "type")) {
            navigation.setType(jsonObject.getInt("type"));
        } else {
            return false;
        }

        if(isValidIntField(jsonObject, "frequency")) {
            navigation.setFrequency(jsonObject.getInt("frequency"));
        } else {
            return false;
        }

        if(isValidIntField(jsonObject, "duration")) {
            navigation.setDuration(jsonObject.getInt("duration"));
        } else {
            return false;
        }

        return true;
    }
}
