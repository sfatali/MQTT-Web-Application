package rtsd.vinavigation.dto;

import java.util.List;

/**
 * Created by Sabina on 11/22/2017.
 */
public class DeviceDTO {
    private int id;
    private String deviceId;
    private String status;
    private String statusDate;
    private List<NavigationDTO> navigationParams;
    private int messagesInQueueCount;
    private List<MessageDTO> pendingMessagesList;

    public DeviceDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public List<NavigationDTO> getNavigationParams() {
        return navigationParams;
    }

    public void setNavigationParams(List<NavigationDTO> navigationParams) {
        this.navigationParams = navigationParams;
    }

    public int getMessagesInQueueCount() {
        return messagesInQueueCount;
    }

    public void setMessagesInQueueCount(int messagesInQueueCount) {
        this.messagesInQueueCount = messagesInQueueCount;
    }

    public List<MessageDTO> getPendingMessagesList() {
        return pendingMessagesList;
    }

    public void setPendingMessagesList(List<MessageDTO> pendingMessagesList) {
        this.pendingMessagesList = pendingMessagesList;
    }
}
