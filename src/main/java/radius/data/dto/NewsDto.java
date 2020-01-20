package radius.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class NewsDto {

    private Date date;

    private String title;

    private String text;
}
