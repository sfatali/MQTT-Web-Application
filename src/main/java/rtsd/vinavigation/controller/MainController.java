package rtsd.vinavigation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import rtsd.vinavigation.dao.MessageDAO;
import rtsd.vinavigation.dao.NavigationDAO;
import rtsd.vinavigation.dto.DeviceDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import rtsd.vinavigation.dao.DeviceDAO;
import rtsd.vinavigation.model.DeviceStatus;
import rtsd.vinavigation.model.Message;
import rtsd.vinavigation.model.MessageStatus;
import rtsd.vinavigation.model.Navigation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sabina on 11/22/2017.
 */

@Controller
public class MainController {
    @Autowired
    private DeviceDAO deviceDAO;
    //@Autowired
    //private MessageDAO messageDAO;

    // step 1: run the app (the "main" method VinavApplication class)
    // step 2: paste in browser http://localhost:8080/dashboard
    @RequestMapping(value = "dashboard", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<DeviceDTO> dashboard() {

        // very lazy testing:

        /*DeviceStatus status = new DeviceStatus();
        status.setDeviceId("MKR1000-SERIAL1");
        status.setStatus(1);
        status.setStatusDate(new Date());
        deviceDAO.insertDeviceStatus(status);*/

        /*Message message = new Message();
        message.setCommand(1);
        message.setDeviceId("MKR1000-SERIAL1");
        message.setDuration(5);
        message.setFrequency(56);
        message.setMessageId("ghty65098");
        message.setPublishDate(new Date());
        message.setStatus(1);
        message.setStatusDate(new Date());
        messageDAO.insert(message);

        MessageStatus messageStatus = new MessageStatus();
        messageStatus.setMessageId("ghty65098");
        messageStatus.setStatus(2);
        messageStatus.setStatusDate(new Date());
        messageDAO.updateMessageStatus(messageStatus);*/

        /*Navigation navigation = new Navigation();
        navigation.setDeviceId("MKR1000-SERIAL1");
        navigation.setFrequency(60);
        navigation.setType(1);
        navigation.setDuration(100);
        navigationDAO.update(navigation);*/

        System.out.println("\nYAY, Controller was called ^___^\n");
        return deviceDAO.getDashboardData();
    }
}
