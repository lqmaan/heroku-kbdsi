package icstar.kbdsi.apps.services.impl;

import icstar.kbdsi.apps.dto.EmailDetailsDto;
import icstar.kbdsi.apps.models.Reminder;
import icstar.kbdsi.apps.repository.ReminderRepository;
import icstar.kbdsi.apps.services.EmailService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.number.CurrencyStyleFormatter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import icstar.kbdsi.apps.util.*;

import java.io.File;
import java.util.*;

import static org.antlr.v4.runtime.misc.Utils.readFile;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    private ReminderRepository reminderRepository;

    private Convert convert;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(ReminderRepository reminderRepository, Convert convert){
        super();
        this.convert = convert;
        this.reminderRepository = reminderRepository;
    }

    @Override
    public String sendSimpleMail(EmailDetailsDto emailDetailsDto) {
        try {
//            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            String rupiahFormat = new CurrencyStyleFormatter().print(emailDetailsDto.getAmount(), new Locale("id", "ID"));
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetailsDto.getRecipient());
            mailMessage.setSubject(emailDetailsDto.getSubject());
            String temp = "Hello!\n\n"
                    + "This is a reminder for you.\n"
                    + "The following informations are shown below: \n\n"
                    + "Description \t\t: \t" + emailDetailsDto.getDescription() +"\n"
                    + "Amount \t\t: \t" + rupiahFormat +"\n"
                    + "Due Date \t\t: \t" + emailDetailsDto.getPaymentDate() +"\n"
                    + "We hope you're having a great day!\n\n"
                    + "Best regards,\n"
                    + "Team 14";

            mailMessage.setText(temp);

            //mailMessage.setRecipient(Message.RecipientType.TO, InternetAddress.parse(emailDetailsDto.getRecipient()));
            //mailMessage.setText(emailDetailsDto.getMsgBody());

//            String htmlTemplate = Arrays.toString(readFile("html template"));
//            htmlTemplate = htmlTemplate.replace("${name}", "John Doe");
//            htmlTemplate = htmlTemplate.replace("${message}", "Hello, this is a test email.");
            javaMailSender.send(mailMessage);
            Optional<Reminder> optReminder = reminderRepository.findById(emailDetailsDto.getReminderId());
            if(optReminder.isPresent()) {
                Reminder oldReminder = optReminder.get();
                oldReminder.setSend(true);
                if(!oldReminder.isRepeated()){
                    oldReminder.setStatus("done");
                }
                else{
                    Date nextSchedule = convert.ConvertStringToDate(emailDetailsDto.getScheduleDate());
                    Date nextPayment = convert.ConvertStringToDate(emailDetailsDto.getPaymentDate());
                    System.out.println("currSchedule " + nextSchedule);
                    System.out.println("currPayment " + nextPayment);
                    nextSchedule.setMonth(nextSchedule.getMonth()+1);
                    nextPayment.setMonth(nextPayment.getMonth()+1);
                    System.out.println("nextSchedule " + nextSchedule);
                    System.out.println("nextPayment " + nextPayment);
                    oldReminder.setScheduleDate(nextSchedule);
                    oldReminder.setPaymentDate(nextPayment);
                }
                reminderRepository.save(oldReminder);
            }

            return "Mail sent successfully";
        }
        catch (Exception e) {
            System.out.println(e);
            return "Error while sending email...";
        }
    }

//    @Override
//    public String sendMailWithAttachment(EmailDetailsDto emailDetailsDto) {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper;
//
//            try {
//                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//                mimeMessageHelper.setFrom(sender);
//                mimeMessageHelper.setFrom(emailDetailsDto.getRecipient());
//                mimeMessageHelper.setText(emailDetailsDto.getMsgBody());
//                mimeMessageHelper.setSubject(emailDetailsDto.getSubject());
//
//                FileSystemResource file
//                        = new FileSystemResource(
//                        new File(emailDetailsDto.getAttachment()));
//
//                mimeMessageHelper.addAttachment(
//                        file.getFilename(), file);
//
//                javaMailSender.send(mimeMessage);
//                return "Mail sent Successfully";
//            } catch (MessagingException e) {
//                return "Error while sending mail!!!";
//            }
//    }
}
