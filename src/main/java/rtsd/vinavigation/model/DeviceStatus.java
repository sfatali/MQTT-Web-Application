package rtsd.vinavigation.model;

import java.util.Date;

/**
 * Created by Sabina on 11/22/2017.
 */
public class DeviceStatus {
    private String deviceId;
    private int status;
    private Date statusDate;

    public DeviceStatus() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
}
