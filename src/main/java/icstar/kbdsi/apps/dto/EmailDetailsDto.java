package icstar.kbdsi.apps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetailsDto {
    private long reminderId;
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
    private Integer amount;
    private String description;
    private String scheduleDate;
    private String paymentDate;
}
