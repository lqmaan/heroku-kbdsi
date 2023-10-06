package icstar.kbdsi.apps;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name="scheduler.enabled", matchIfMissing = true)
public class ScheduledTasks {
    SimpleDateFormat dateFormat =  new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//    @Scheduled(cron = "* * * * * /1 *")
//    public void everyMinuteCron(){
//        System.out.println("run cron at " + dateFormat.format(new Date()));
//    }

}
