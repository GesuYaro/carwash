package shagiev.carwash.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class EntryRequestDto {

    private long service_id;
    private Date date;

}
