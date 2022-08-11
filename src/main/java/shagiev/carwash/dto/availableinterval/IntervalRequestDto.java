package shagiev.carwash.dto.availableinterval;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@AllArgsConstructor
public class IntervalRequestDto {

    @Min(1)
    private long serviceId;

    @NotNull
    private Date date;

}
