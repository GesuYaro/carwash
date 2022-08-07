package shagiev.carwash.dto.availableinterval;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;

@Data
@AllArgsConstructor
public class AvailableIntervalDto {

    private Time from;
    private Time until;

}
