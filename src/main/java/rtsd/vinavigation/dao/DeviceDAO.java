package rtsd.vinavigation.dao;

import rtsd.vinavigation.dto.DeviceDTO;
import rtsd.vinavigation.model.DeviceStatus;

import java.util.List;

/**
 * Created by Sabina on 12/2/2017.
 */

public interface DeviceDAO {
    List<DeviceDTO> getDashboardData();
    void insertDeviceStatus(DeviceStatus deviceStatus);
}
