package rtsd.vinavigation.model;

import java.util.Date;

/**
 * Created by Sabina on 12/2/2017.
 */
public class MessageStatus {
    private String messageId;
    private int status;
    private Date statusDate;

    public MessageStatus() {
    }

    public MessageStatus(String packageId, int status, Date statusDate) {
        this.messageId = packageId;
        this.status = status;
        this.statusDate = statusDate;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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
