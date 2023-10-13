package icstar.kbdsi.apps.util;

import icstar.kbdsi.apps.dto.EmailDetailsDto;
import icstar.kbdsi.apps.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Scheduler {

    @Autowired
    EmailService emailService;

    public Scheduler(EmailService emailService) {
        this.emailService = emailService;
    }

    //Every 10 seconds
//    @Scheduled(cron = "*/10 * * * * * ")
//    public void ScheduleMethod(){
//        System.out.println("Run cron at " + LocalDateTime.now());
//    }
//
//    @Scheduled(cron = "* * /12 * * *")

//    @Scheduled(cron = "@daily")
//    public void SendReminder(){
//        EmailDetailsDto data = new EmailDetailsDto();
//
//        String status = emailService.sendSimpleMail();
//
//    }


}
