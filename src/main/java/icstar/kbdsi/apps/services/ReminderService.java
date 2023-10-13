package icstar.kbdsi.apps.services;

import icstar.kbdsi.apps.models.Reminder;
import icstar.kbdsi.apps.models.User;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public interface ReminderService {

    List<Reminder> getAllReminders();
    List<Reminder> getAllRemindersWithStatus(String status);
    Page<Reminder> findReminderContainingStatus(String status, Integer pageNum, Integer pageSize);
    Page<Reminder> findReminderContainingDescriptionAndStatus(String description, String status, Integer pageNum, Integer pageSize);
    List<Reminder> findAllReminderByScheduleAndStatusAndSend(Date scheduleDate, String status, boolean isSend);
    List<Reminder> findAllReminderByStatusAndRepeatedAndSend(String status, boolean isRepeated, boolean isSend);
}
