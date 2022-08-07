package shagiev.carwash.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class EntryUserRequestDto {

    private long serviceId;
    private Date date;

}
