package rtsd.vinavigation.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Primary;
import rtsd.vinavigation.model.Message;
import rtsd.vinavigation.model.MessageStatus;
import rtsd.vinavigation.model.Navigation;

/**
 * Created by Sabina on 12/2/2017.
 */

public interface MessageDAO {
    void insert(Message message);
    void updateMessageStatus(MessageStatus messageStatus);
    void insertFaultyMessageLog(@Param("message") String message);
    Navigation getNavigationParams(@Param("messageId") String message);
    int count(String messageId);
}
