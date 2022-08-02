package shagiev.carwash.dto.carbox;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;

@Data
@AllArgsConstructor
public class CarBoxRequestDto {

    private Time openingTime;
    private Time closingTime;
    private double timeCoefficient;

}
