package icstar.kbdsi.apps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    String name;
    boolean isDeleted;
    Integer pageNum;
    Integer pageSize;
}
