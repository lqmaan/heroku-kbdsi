package icstar.kbdsi.apps.controllers;

import icstar.kbdsi.apps.dto.EmailDetailsDto;
import icstar.kbdsi.apps.dto.ReminderDetailsDto;
import icstar.kbdsi.apps.dto.ReminderDto;
import icstar.kbdsi.apps.models.Reminder;
import icstar.kbdsi.apps.repository.ReminderRepository;
import icstar.kbdsi.apps.services.EmailService;
import icstar.kbdsi.apps.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ReminderController {
    @Autowired
    ReminderRepository reminderRepository;

    ReminderService reminderService;

    EmailService emailService;

    public ReminderController(ReminderService reminderService, EmailService emailService) {
        super();
        this.reminderService = reminderService;
        this.emailService =  emailService;
    }

    @GetMapping("/reminders")
    public ResponseEntity<List<Reminder>> getAllReminders (@RequestParam(required = false) String name){
        try{
            List<Reminder> reminders = new ArrayList<Reminder>();

            if(name == null)
                reminders.addAll(reminderRepository.findAll());
//            else
//                categoryRepository.findAllByName(name, PageRequest.of(0, 10, Sort.by("name").ascending()));
            if(reminders.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(reminders, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reminders")
    public ResponseEntity<Reminder> createReminder(@RequestBody ReminderDetailsDto reminderDetailsDto){
        try{
            Date schDate = new SimpleDateFormat("dd/MM/yyyy").parse(reminderDetailsDto.getScheduleDate());
            Date payDate = new SimpleDateFormat("dd/MM/yyyy").parse(reminderDetailsDto.getPaymentDate());
            System.out.println(reminderDetailsDto.isRepeated());
            Reminder newReminder =  new Reminder(reminderDetailsDto.getEmail(), reminderDetailsDto.getDescription(), reminderDetailsDto.getAmount(), reminderDetailsDto.isRepeated(), reminderDetailsDto.getCreatedBy(), reminderDetailsDto.getStatus(), schDate, payDate);
            System.out.println(newReminder.toString());
            reminderRepository.save(newReminder);
            return new ResponseEntity<>(newReminder, HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(new Reminder(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/reminders/{id}")
    public ResponseEntity<Reminder> updateReminderById(@PathVariable(required = true) Long id, @RequestBody Reminder newReminder) {
        Optional<Reminder> optReminder = reminderRepository.findById(id);
        if(optReminder.isPresent()) {
            Reminder oldReminder = optReminder.get();
            oldReminder.setDescription(newReminder.getDescription());
            oldReminder.setAmount(newReminder.getAmount());
            oldReminder.setRepeated(newReminder.isRepeated());
            oldReminder.setPaymentDate(newReminder.getPaymentDate());
            oldReminder.setScheduleDate(newReminder.getScheduleDate());
            oldReminder.setUpdatedBy(newReminder.getUpdatedBy());
            oldReminder.setEmail(newReminder.getEmail());
            reminderRepository.save(oldReminder);
            return new ResponseEntity<>(oldReminder, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/reminders/{id}")
    public ResponseEntity<String> deleteReminder(@PathVariable(required = true) Long id) {
        try {
            Optional<Reminder> optReminder = reminderRepository.findById(id);
            if(optReminder.isPresent()){
                Reminder oldReminder = optReminder.get();
                oldReminder.setDeleted(true);
                return new ResponseEntity<>("Reminder has been deleted",HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Reminder doesn't exist",HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reminders/status")
    private Page<Reminder> getRemindersWithStatus(@RequestBody ReminderDto reminderDto) {
        Page<Reminder> reminders = reminderService.findReminderContainingStatus(reminderDto.getStatus(), reminderDto.getPageNum(), reminderDto.getPageSize());
        return reminders;
    }

    @GetMapping("/reminders/schedule/ongoing")
    private List<Reminder> getRemindersWithScheduleDate() {
        Date curDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Reminder> reminders = reminderService.findAllReminderByScheduleAndStatusAndSend(curDate, "ongoing", false);
        return reminders;
    }

    //Send Reminder Email
    @PostMapping("/reminders/sendmail")
    public String sendMail(@RequestBody EmailDetailsDto emailDetailsDto)
    {
        String status = emailService.sendSimpleMail(emailDetailsDto);
        return status;
    }


//    @Scheduled(cron = "*/10 * * * * *")
//    public void EveryTenSeconds(){
//        System.out.println("Run cron at " + LocalDateTime.now());
//    }
    @Scheduled(cron = "* * */12 * * *")
    public void EveryTwelveHours(){
        System.out.println("Run cron at " + LocalDateTime.now());
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void DailyReminder(){
        System.out.println("Run cron at " + LocalDateTime.now());
        Date curDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Reminder> reminders = reminderService.findAllReminderByScheduleAndStatusAndSend(curDate, "ongoing", false);
        //Converting the Date to LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Reminder reminder: reminders) {
            EmailDetailsDto data = new EmailDetailsDto();
            //Convert Date to LocalDate via Instant
            LocalDate localDatePayment = reminder.getPaymentDate().toInstant().atZone(ZoneId.of("Asia/Jakarta")).toLocalDate();
            LocalDate localDateSchedule = reminder.getScheduleDate().toInstant().atZone(ZoneId.of("Asia/Jakarta")).toLocalDate();
            String strPayment = localDatePayment.format(formatter);
            String strSchedule = localDateSchedule.format(formatter);

            data.setSubject("Payment Reminder");
            data.setReminderId(reminder.getReminderId());
            data.setRecipient(reminder.getEmail());
            data.setDescription(reminder.getDescription());
            data.setAmount(reminder.getAmount());
            data.setPaymentDate(strPayment);
            data.setScheduleDate(strSchedule);
            data.setAttachment(null);
            data.setMsgBody(null);
            String status = emailService.sendSimpleMail(data);
        }
    }

    @Scheduled(cron = "0 */7 * * * *")
    public void EndMonthUpdate(){
        System.out.println("Run cron at " + LocalDateTime.now());
        List<Reminder> reminders = reminderService.findAllReminderByStatusAndRepeatedAndSend("ongoing", true, true);
        for (Reminder reminder: reminders) {
            Optional<Reminder> optReminder = reminderRepository.findById(reminder.getReminderId());
                Reminder oldReminder = optReminder.get();
                oldReminder.setSend(false);
                reminderRepository.save(oldReminder);
        }
    }
}
