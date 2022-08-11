package shagiev.carwash.dto.carbox;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Time;

@Data
@AllArgsConstructor
public class CarBoxRequestDto {

    @NotNull
    private Time openingTime;

    @NotNull
    private Time closingTime;

    @NotNull
    private double timeCoefficient;

}
