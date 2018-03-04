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

    @RequestMapping(value = "dashboard", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<DeviceDTO> dashboard() {
        return deviceDAO.getDashboardData();
    }
}
