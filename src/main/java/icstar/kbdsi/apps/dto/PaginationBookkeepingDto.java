package icstar.kbdsi.apps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationBookkeepingDto {
    String category;
    String startDate;
    String endDate;
    String year;
    int pageNum;
    int pageSize;
}
