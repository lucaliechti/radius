package radius.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatisticsDto {

    private int usersActive;
    private int usersTotal;

    private int registrationsWeek;
    private int registrationsMonth;

    private int matchesWeek;
    private int matchesMonth;

    private int onlineWeek;
    private int onlineMonth;

}
