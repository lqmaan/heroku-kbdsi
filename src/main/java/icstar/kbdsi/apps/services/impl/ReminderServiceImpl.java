package icstar.kbdsi.apps.services.impl;

import icstar.kbdsi.apps.models.Reminder;
import icstar.kbdsi.apps.repository.ReminderRepository;
import icstar.kbdsi.apps.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReminderServiceImpl implements ReminderService {
    @Autowired
    ReminderRepository reminderRepository;

    public ReminderServiceImpl(ReminderRepository reminderRepository){
        super();
        this.reminderRepository = reminderRepository;
    }

    @Override
    public List<Reminder> getAllReminders() {
        return reminderRepository.findByIsDeleted(false);
    }

    @Override
    public List<Reminder> getAllRemindersWithStatus(String status) {
        return reminderRepository.findByStatusContainingAndIsDeleted(status, false);
    }

    public Page<Reminder> findReminderContainingStatus(String status, Integer pageNum, Integer pageSize){
        Page<Reminder> reminders = reminderRepository.findByStatusContainingAndIsDeleted(status, false ,PageRequest.of(pageNum, pageSize), Sort.by("scheduleDate").ascending().and(Sort.by("description")));
        return  reminders;
    }

    @Override
    public Page<Reminder> findReminderContainingDescriptionAndStatus(String description, String status, Integer pageNum, Integer pageSize) {
        Page<Reminder> reminders = reminderRepository.findByDescriptionContainingAndStatusContainingAndIsDeleted(description, status, false ,PageRequest.of(pageNum, pageSize), Sort.by("scheduleDate").ascending().and(Sort.by("description")));
        return  reminders;
    }

    @Override
    public List<Reminder> findAllReminderByScheduleAndStatusAndSend(Date scheduleDate, String status, boolean isSend) {
        return reminderRepository.findAllByScheduleDateAndStatusAndIsSendAndIsDeleted(scheduleDate, status, isSend, false);
    }

    @Override
    public List<Reminder> findAllReminderByStatusAndRepeatedAndSend(String status, boolean isRepeated, boolean isSend) {
        return reminderRepository.findAllByStatusAndIsRepeatedAndIsSendAndIsDeleted(status, isRepeated, isSend, false);
    }
}
