package icstar.kbdsi.apps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReminderDetailsDto {
    private String email;
    private String description;
    private Integer amount;
    private boolean isRepeated;
    private String createdBy;
    private String status;
    private String scheduleDate;
    private String paymentDate;
}
