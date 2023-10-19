package icstar.kbdsi.apps.services;

import icstar.kbdsi.apps.dto.EmailDetailsDto;

public interface EmailService {
    String sendSimpleMail(EmailDetailsDto emailDetailsDto);
    String sendMailWithAttachment(EmailDetailsDto emailDetailsDto);
}
