package shagiev.carwash.dto.availableinterval;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class IntervalRequestDto {

//    private long carboxId;
    private long serviceId;
    private Date date;

}
